package com.inn.foresight.module.nv.report.service.impl;

import com.google.gson.Gson;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.encoder.AESUtils;
import com.inn.commons.io.IOUtils;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.report.dao.IAnalyticsRepositoryDao;
import com.inn.foresight.core.report.model.AnalyticsRepository;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;
import com.inn.foresight.module.nv.layer3.constants.NVLayer3Constants;
import com.inn.foresight.module.nv.report.dao.INVReportHdfsDao;
import com.inn.foresight.module.nv.report.service.*;
import com.inn.foresight.module.nv.report.utils.MergePDF;
import com.inn.foresight.module.nv.report.utils.ReportConstants;
import com.inn.foresight.module.nv.report.utils.ReportUtil;
import com.inn.foresight.module.nv.report.wrapper.*;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.foresight.module.nv.core.workorder.dao.impl.IGenericWorkorderDao;

import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;

@Service("NVReportServiceImpl")
public class NVReportServiceImpl implements INVReportService {
	/** The logger. */
	private Logger logger = LogManager.getLogger(NVReportServiceImpl.class);

	@Autowired
	private ISSVTReportService issvtReportService;
	@Autowired
	private IGenericWorkorderDao igenericWorkorderDao;
	@Autowired
	private IClotReportService iClotReportService;
	@Autowired
	private ILiveDriveReportService iLiveDriveReportService;
	@Autowired
	private INVInBuildingReportService inbuildingService;
	@Autowired
	private IStationaryReportService stationaryReportService;
	@Autowired
	private IReportService reportService;
	@Autowired
	private IAnalyticsRepositoryDao analyticsRepositoryDao;
	@Autowired
	private INVReportHdfsDao nvReportHdfsDao;

	@Override
	public void generateReportForWorkOrderId(Integer workorderId) {
	}

	@Override
	public Response updateSiteInfoInSSVTReport(String json) {
		Response response = null;
		try {
		Integer analyticsrepoId = null;
		Map<String, Object> jsonMap = reportService.getJsonDataMap(json);
		Integer workorderId = (Integer) jsonMap.get(ReportConstants.WORKORDER_ID);
		analyticsrepoId = (Integer) jsonMap.get(ReportConstants.ANALYTICS_REPOSITORY_ID);
		GenericWorkorder workorderObj = igenericWorkorderDao.findByPk(workorderId);
			boolean iSiteAcceptance = false;
			Map<String, String> gwoMeta = workorderObj.getGwoMeta();
			if (gwoMeta != null && !gwoMeta.isEmpty() && gwoMeta.containsKey("isSiteAcceptance")) {
				iSiteAcceptance = Boolean.parseBoolean(gwoMeta.get("isSiteAcceptance"));
			}
		List<SiteInformationWrapper> siteData = getSiteData(workorderObj);
		if(Utils.isValidList(siteData)){
			SSVTSiteInfoWrapper siteInfoWrapper = new SSVTSiteInfoWrapper();
			updateBasicSiteInfo(siteInfoWrapper, siteData);
			String siteName = siteInfoWrapper.getSiteId();
			Collections.sort(siteData, (s1, s2) -> s1.getSector().compareTo(s2.getSector()));
			int sectorValue = 1;

			for (SiteInformationWrapper site : siteData) {
				SSVTSectorWiseWrapper sectorDataWrapper = new SSVTSectorWiseWrapper();
				commonSectorUpdation(site, sectorDataWrapper);
				setSectorDataToSiteInfoWrapper(siteInfoWrapper, sectorDataWrapper, sectorValue);
				sectorValue++;
			}
			SSVTSiteMainWrapper mainWrapper = new SSVTSiteMainWrapper();
			mainWrapper.setSiteInfo(Arrays.asList(siteInfoWrapper));
			String updatedSitePageFilePath = proceedToCreateSSVTReport(mainWrapper, workorderObj, jsonMap, new HashMap<>());
			AnalyticsRepository analyticsRepository = analyticsRepositoryDao.findByPk(analyticsrepoId);
			String reportFilePath = analyticsRepository.getFilepath();
			//			SSVT_SITE_UPDATE_JASPER_FOLDER_PATH = /home/ist/Documents/ServerApps/FS-Product-Development/Jaspers/Nv/Rakuten/1C_SITE_JASPER/
			//			SSVT_UPDATED_REPORT_PATH=/home/ist/Desktop/SSVT/UPDATED/
			//			String filePath = ConfigUtils.getString(ReportConstants.SSVT_UPDATED_REPORT_PATH);
			String filePath = "/home/ist/Desktop/SSVT/UPDATED/";
			ReportUtil.createDirectory(filePath);
			filePath += analyticsRepository.getDownloadFileName();
			nvReportHdfsDao.copyHdfsToServer(filePath, reportFilePath);
			File reportFile = new File(filePath);
			File updatedSitePageFile = new File(updatedSitePageFilePath);
			if(reportFile != null && updatedSitePageFile != null && reportFile.exists() && updatedSitePageFile.exists()){
				File targetFile = MergePDF.addPage(reportFile, updatedSitePageFile);
				boolean fileSavedToHdfs = nvReportHdfsDao.saveFileToHdfs(targetFile, reportFilePath);
				if(fileSavedToHdfs){
					response = Response.ok(NVLayer3Constants.SUCCESS_JSON).build();
					putSFIntegrationDataToJsonMap(jsonMap, iSiteAcceptance, siteName);
					if (gwoMeta != null && !gwoMeta.isEmpty() && gwoMeta.containsKey("projectId")) {
						Map<String, Object> payLoadJsonMap = new HashMap<>();
						payLoadJsonMap.put("projectId", workorderObj.getGwoMeta().get("projectId"));
						payLoadJsonMap.put("reportId", analyticsrepoId);
						payLoadJsonMap.put("taskName", jsonMap.get("taskName"));
						try {
							sendPostRequestWithoutTimeOut(
									ConfigUtils.getString("NV_SSVT_SF_INTEGRATION_URL") + jsonMap.get(
											"siteName"),
									new StringEntity(new Gson().toJson(payLoadJsonMap), ContentType.APPLICATION_JSON));
						} catch (Exception e) {
							logger.info("Going to print stacktrace {}", Utils.getStackTrace(e));
						}
					}
					boolean fileDeleted = reportService.deleteFileIfExist(targetFile.getAbsolutePath());
					if(fileDeleted){
						logger.info("Generated File Deleted Successfully");
					}
				} else{
					logger.error("Error while adding site page in SSVT Report, File not copied to HDFS");
					response = Response.ok(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
				}
			} else{
				logger.error("Error while adding site page in SSVT Report, One of the file does not exists.");
				response = Response.ok(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
			}
		} else{
			logger.error("Error while adding site page in SSVT Report, Site data not available");
			response = Response.ok(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
		}
		} catch (Exception e) {
			logger.error("Error while adding site page in SSVT Report {}", Utils.getStackTrace(e));
			response = Response.ok(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
		}
		return response;
	}

	private void putSFIntegrationDataToJsonMap(Map<String, Object> jsonDataMap, boolean isSiteAcceptance,
			String siteName) {
		jsonDataMap.put("taskName",
				isSiteAcceptance ? "ATP 1C" : "ATP 1E");
		if (!com.inn.commons.lang.StringUtils.isBlank(siteName)) {
			jsonDataMap.put("siteName", siteName);
		}
	}

	public HttpResponse sendPostRequestWithoutTimeOut(String uri, StringEntity httpEntity) throws IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(uri);
//		DefaultHttpClient httpclient = ReportUtil.getHttpsClient();
		try {
			logger.info("Going to hit URL : {}",uri);
			httpPost.setEntity(httpEntity);
			httpPost.addHeader("X-API-KEY", ConfigUtils.getString("NV_SSVT_SF_INTEGRATION_X_API_KEY"));
			CloseableHttpResponse response = httpClient.execute(httpPost);

			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				throw new RestException("Failed with HTTP error code : " + statusCode);
			}
			logger.info("Return Response Code : {}",statusCode);
			return response;
		} catch (Exception e) {
			throw new RestException(ReportConstants.EXCEPTION_ON_CONNECTION);
		}finally {
			httpClient.close();
		}

	}

	private void setSectorDataToSiteInfoWrapper(SSVTSiteInfoWrapper siteInfoWrapper, SSVTSectorWiseWrapper sectorDataWrapper, int sectorValue) {
		switch (sectorValue){
		case 1:
			siteInfoWrapper.setAlpha(Arrays.asList(sectorDataWrapper));
			break;
		case 2:
			siteInfoWrapper.setBeta(Arrays.asList(sectorDataWrapper));
			break;
		case 3:
			siteInfoWrapper.setGamma(Arrays.asList(sectorDataWrapper));
			break;
		}
	}

	private List<SiteInformationWrapper> getSiteData(GenericWorkorder workorderObj) {
		List<SiteInformationWrapper> siteInfoList = reportService.getSiteDataForSSVTReport(workorderObj);
		String band = findBandDetailByGWOMetaData(workorderObj);
		if (!StringUtils.isBlank(band)) {
			return siteInfoList.stream()
							   .filter(x -> x.getNeFrequency().equalsIgnoreCase(band))
							   .collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	private String findBandDetailByGWOMetaData(GenericWorkorder genricWorkOrder) {
		return genricWorkOrder.getGwoMeta().get(NVWorkorderConstant.BAND) != null
				? findBandByGWOMeta(genricWorkOrder.getGwoMeta().get(NVWorkorderConstant.BAND))
				: null;
	}

	private String findBandByGWOMeta(String band) {
		if (band.equalsIgnoreCase("FDD3")) {
			band = ForesightConstants.BAND1800;
		} else if (band.equalsIgnoreCase("FDD5")) {
			band = ForesightConstants.BAND850;
		} else if (band.equalsIgnoreCase("TDD40")) {
			band = ForesightConstants.BAND2300;
		} else {
			band = null;
		}
		logger.info("Going to return band {}", band);
		return band;
	}

	private void updateBasicSiteInfo(SSVTSiteInfoWrapper siteInfo, List<SiteInformationWrapper> filteredSiteInfo) {
		logger.info("Going to update Basic Site Infomation");
		SiteInformationWrapper wrapper = filteredSiteInfo.get(0);
		siteInfo.setLat(wrapper.getLat().toString());
		siteInfo.setLng(wrapper.getLon().toString());
		siteInfo.setSiteId(wrapper.getSiteName());
		siteInfo.setSiteName(wrapper.getFriendlyName());
		siteInfo.setTac(wrapper.getTac());
		siteInfo.setMcc(wrapper.getMcc().toString());
		siteInfo.setMnc(wrapper.getMnc().toString());
	}

	private void commonSectorUpdation(SiteInformationWrapper siteObj, SSVTSectorWiseWrapper sectorWraper) {
		if (siteObj.getCellId() != null) {
			sectorWraper.setCellId(siteObj.getCellId().toString());
		}
		if (siteObj.getActualAzimuth() != null) {
			sectorWraper.setAzimuth(siteObj.getActualAzimuth().toString());
		}
		if (siteObj.getmTilt() != null) {
			sectorWraper.setmTilt(siteObj.getmTilt());
		}
		if (siteObj.geteTilt() != null) {
			sectorWraper.seteTilt(siteObj.geteTilt());
		}
		if (siteObj.getPci() != null) {
			sectorWraper.setPci(siteObj.getPci().toString());
		}
		if (siteObj.getDlEarfcn() != null) {
			sectorWraper.setEarfcn(siteObj.getDlEarfcn().toString());
		}
	}

	private String proceedToCreateSSVTReport(SSVTSiteMainWrapper mainWrapper, GenericWorkorder workorderObj,
			Map<String, Object> jsonMap, Map<String, Object> imageMap) {
		logger.info("Going to create SSVT Report");
		String reportAssetRepo = "/home/ist/Documents/ServerApps/FS-Product-Development/Jaspers/Nv/Rakuten/1C_SITE_JASPER/";
		//		String reportAssetRepo = ConfigUtils.getString(ReportConstants.SSVT_SITE_UPDATE_JASPER_FOLDER_PATH);
		List<SSVTSiteMainWrapper> dataSourceList = new ArrayList<>();
		dataSourceList.add(mainWrapper);
		JRBeanCollectionDataSource rfbeanColDataSource = new JRBeanCollectionDataSource(dataSourceList);
		imageMap.put(ReportConstants.SUBREPORT_DIR, reportAssetRepo);
		imageMap.put(ReportConstants.LOGO_CLIENT_KEY, reportAssetRepo + ReportConstants.LOGO_CLIENT_IMG);
		imageMap.put("netvelocityLogo", reportAssetRepo + ReportConstants.LOGO_NV_IMG);

		logger.info("Found Parameter map: {}", new Gson().toJson(imageMap));

		String filePath = "/home/ist/Desktop/SSVT/UPDATED/";
		//		String filePath = ConfigUtils.getString(ReportConstants.SSVT_UPDATED_REPORT_PATH);
		File file = new File(filePath);
		if(!file.exists()){
			file.mkdirs();
		}
		String fileName = ReportUtil.getFileName("UPDATED_SITE" + workorderObj.getWorkorderId(),
				(Integer) jsonMap.get(ReportConstants.ANALYTICS_REPOSITORY_ID), filePath);
		try {
			logger.info("Going to save report on path {}", fileName);
			JasperRunManager.runReportToPdfFile(reportAssetRepo + ReportConstants.MAIN_JASPER, fileName, imageMap,
					rfbeanColDataSource);
			return fileName;
		} catch (Exception e) {
			logger.info("Exception while processing Jasper on path {} trace ==> {}", reportAssetRepo,
					Utils.getStackTrace(e));
		}
		return fileName;
	}

}

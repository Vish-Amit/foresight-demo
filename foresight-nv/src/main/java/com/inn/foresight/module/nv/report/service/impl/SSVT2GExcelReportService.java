package com.inn.foresight.module.nv.report.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import org.apache.commons.collections.MapUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.inn.commons.Symbol;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.report.dao.IAnalyticsRepositoryDao;
import com.inn.foresight.core.report.model.AnalyticsRepository;
import com.inn.foresight.core.report.model.AnalyticsRepository.progress;
import com.inn.foresight.module.nv.core.workorder.dao.impl.IGenericWorkorderDao;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;
import com.inn.foresight.module.nv.layer3.service.INVLayer3DashboardService;
import com.inn.foresight.module.nv.layer3.utils.NetworkDataFormats;
import com.inn.foresight.module.nv.report.RangeSlab;
import com.inn.foresight.module.nv.report.constant.ReportIndexWrapper;
import com.inn.foresight.module.nv.report.service.IMapImagesService;
import com.inn.foresight.module.nv.report.service.IReportService;
import com.inn.foresight.module.nv.report.service.ISSVT2gReportService;
import com.inn.foresight.module.nv.report.utils.DriveHeaderConstants;
import com.inn.foresight.module.nv.report.utils.ReportConstants;
import com.inn.foresight.module.nv.report.utils.ReportUtil;
import com.inn.foresight.module.nv.report.utils.Statistics;
import com.inn.foresight.module.nv.report.workorder.wrapper.DriveImageWrapper;
import com.inn.foresight.module.nv.report.workorder.wrapper.KPIWrapper;
import com.inn.foresight.module.nv.report.wrapper.GraphDataWrapper;
import com.inn.foresight.module.nv.report.wrapper.GraphWrapper;
import com.inn.foresight.module.nv.report.wrapper.SSVTReportSubWrapper;
import com.inn.foresight.module.nv.report.wrapper.SSVTReportWrapper;
import com.inn.foresight.module.nv.report.wrapper.SiteInformationWrapper;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;
import com.inn.foresight.module.nv.workorder.dao.IWORecipeMappingDao;
import com.inn.foresight.module.nv.workorder.model.WORecipeMapping;
import com.inn.product.legends.dao.ILegendRangeDao;
import com.inn.product.legends.utils.LegendWrapper;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;


@Service("SSVT2GExcelReportService")
public class SSVT2GExcelReportService implements ISSVT2gReportService {
	@Autowired
	private IGenericWorkorderDao genericWorkorderDao;
	@Autowired
	private IReportService reportService;
	@Autowired
	private IWORecipeMappingDao recipeMappingDao;
	@Autowired
	private IAnalyticsRepositoryDao analyticsrepositoryDao;
	@Autowired
	private IMapImagesService mapImageService;
	@Autowired
	private INVLayer3DashboardService nvLayer3DashboardService;
	@Autowired	
	private ILegendRangeDao legendRangeDao;
	
	private ObjectMapper mapper = new ObjectMapper();
	
	private Logger logger = LogManager.getLogger(SSVT2GExcelReportService.class);
	

	public Response createReport(Integer workorderId, Map<String, Object> jsonMap, Integer analyticsrepoId,GenericWorkorder genericWorkorder)
		 {
		logger.info("inside method createReport  for 2g Report");
		AnalyticsRepository analyticsRepository = analyticsrepositoryDao.findByPk(analyticsrepoId);
		try {
			if (genericWorkorder != null && genericWorkorder.getGwoMeta().containsKey(NVWorkorderConstant.RECIPE_PCI_MAP)) {
				SSVTReportWrapper mainWrapper = new SSVTReportWrapper();
				SSVTReportSubWrapper subwrapper = new SSVTReportSubWrapper();
				Map<String, Object> imagemap = new HashMap<>();
				List<SiteInformationWrapper> siteInfoList = reportService.getSiteDataForSSVTReport(genericWorkorder);
				setSiteDataToWrapper(siteInfoList, subwrapper, genericWorkorder);
				Map<String, List<String>> recipeOperatorListMap = nvLayer3DashboardService
						.getDriveRecipeDetail(Arrays.asList(workorderId));
				List<String> fetchKPIList = ReportIndexWrapper.getLiveDriveKPIs();
				Map<String, Integer> kpiIndexMap = ReportIndexWrapper.getLiveDriveKPIIndexMap(fetchKPIList);
				kpiIndexMap.put(DriveHeaderConstants.CGI, kpiIndexMap.get(ReportConstants.GSM_CELLID));
				List<WORecipeMapping> recipeMappings = recipeMappingDao.getWORecipeByGWOId(workorderId);
				List<String> recipeList = SSVTExcelReportServiceImpl.transform(
						recipeMappings.stream().map(WORecipeMapping::getId).collect(Collectors.toList()), String::valueOf);
				List<String[]> driveData = reportService.getDriveDataRecipeWiseTaggedForReport(Arrays.asList(workorderId),
						recipeList, fetchKPIList, kpiIndexMap);
				setPlotData(genericWorkorder, subwrapper, kpiIndexMap, driveData);
				mainWrapper.setSubWrapperList(Arrays.asList(subwrapper));
				String filePath = proceedToCreateReport(mainWrapper, genericWorkorder, jsonMap, imagemap);
				if (filePath != null) {
					return saveFileToHdfsAndUpdateStatus(analyticsRepository, filePath, genericWorkorder);
				} else {
					return Response.ok(ForesightConstants.FAILURE_JSON).build();
				}
			}
		} catch (Exception e) {
			logger.info("Exception inside method create report {}",Utils.getStackTrace(e));
		} 
		return null;
	}


	private void setPlotData(GenericWorkorder genericWorkorder, SSVTReportSubWrapper subwrapper,
			Map<String, Integer> kpiIndexMap, List<String[]> driveData) {
		List<SiteInformationWrapper> siteInfoList = reportService.getSiteDataForSSVTReport(genericWorkorder);

		List<String[]> dlTestDataList = ReportUtil.filterDataByTestType(driveData,
				kpiIndexMap.get(ReportConstants.TEST_TYPE), NetworkDataFormats.TEST_TYPE_FTP_DOWNLOAD,
				NetworkDataFormats.TEST_TYPE_HTTP_DOWNLOAD);
		if(Utils.isValidList(dlTestDataList)) {
			subwrapper.setGraphplotList(getPlotsDataForDlUl(driveData, kpiIndexMap, siteInfoList));
		}
//		logger.info("graphplotList {}",subwrapper.getGraphplotList());
		List<String[]> longCallDataList = ReportUtil.filterDataByTestType(driveData,
				kpiIndexMap.get(ReportConstants.TEST_TYPE), NetworkDataFormats.SHORT_CALL_TEST,
				NetworkDataFormats.LONG_CALL_TEST);
		if(Utils.isValidList(longCallDataList)) {
			subwrapper.setVoltePlotList(getPlotsDataForDlUl(longCallDataList, kpiIndexMap, siteInfoList));
		}
//		logger.info("VoltePlotList {}",subwrapper.getVoltePlotList());
		List<String[]> idelDataList = ReportUtil.filterDataByTestType(driveData,
				kpiIndexMap.get(ReportConstants.TEST_TYPE),	
				ReportConstants.IDLE);
		if(Utils.isValidList(idelDataList)) {
			subwrapper.setStationaryDataList(getPlotsDataForDlUl(idelDataList, kpiIndexMap, siteInfoList));
		}
//		logger.info("StationaryDataList {}",subwrapper.getStationaryDataList());
	}
	
	
	private List<GraphWrapper> getPlotsDataForDlUl(List<String[]> driveData, Map<String, Integer> kpiIndexMap,
			List<SiteInformationWrapper> siteInfoList) {
		List<GraphWrapper> graphList = new ArrayList<>();
		List<LegendWrapper> legendList = legendRangeDao.findAllLegendRangesAppliedTo(ReportConstants.SSVT_REPORT);
		
		if (Utils.isValidList(driveData)) {
			setGraphDataAndPlotImages(kpiIndexMap, graphList, legendList, driveData,
					getKpiIndexMapForDownloadload(kpiIndexMap), siteInfoList);
		}
		return graphList;
		
		
	}

	public static final Map<String, Integer> getKpiIndexMapForDownloadload(Map<String, Integer> kpiIndexMap) {
		Map<String, Integer> map = new HashMap();
		map.put(ReportConstants.RXLEVEL, kpiIndexMap.get(ReportConstants.RXLEVEL));
		map.put(ReportConstants.RXQUAL, kpiIndexMap.get(ReportConstants.RXQUAL));
		map.put(ReportConstants.WROST_CI, kpiIndexMap.get(ReportConstants.WROST_CI));
		map.put(DriveHeaderConstants.CGI,  kpiIndexMap.get(DriveHeaderConstants.CGI));
		map.put(DriveHeaderConstants.ROUTE, kpiIndexMap.get(DriveHeaderConstants.ROUTE));
		return map;
	}

	private void setGraphDataAndPlotImages(Map<String, Integer> kpiIndexMap, List<GraphWrapper> graphList,
			List<LegendWrapper> legendList, List<String[]> dataList, Map<String, Integer> filterKpiMap,
			List<SiteInformationWrapper> siteInfoList) {
		List<KPIWrapper> kpiList = ReportUtil.convertLegendsListToKpiWrapperList(legendList, filterKpiMap);
		List<SiteInformationWrapper> siteList = reportService.getSiteDataForReportByDataList(dataList, kpiIndexMap);
		siteList.addAll(siteInfoList);

		DriveImageWrapper driveImageWrapper = new DriveImageWrapper(dataList, kpiIndexMap.get(ReportConstants.LATITUDE),
				kpiIndexMap.get(ReportConstants.LONGITUDE), kpiIndexMap.get(ReportConstants.PCI_PLOT), kpiList,
				siteList);
		try {
			HashMap<String, String> imageMap = getImagesForReport(kpiList, driveImageWrapper, kpiIndexMap);

			for (KPIWrapper kpiWrapper : kpiList) {
				if (imageMap != null && MapUtils.isNotEmpty(imageMap) && imageMap.containsKey(kpiIndexMap.get(kpiWrapper.getKpiName()) + Symbol.EMPTY_STRING)) {
					if (DriveHeaderConstants.CGI.equalsIgnoreCase(kpiWrapper.getKpiName())) {
						GraphWrapper graphWrapper = new GraphWrapper();
						graphWrapper.setKpiName("CI");
						graphWrapper.setKpiPlotImg(
								imageMap.get(kpiIndexMap.get(kpiWrapper.getKpiName()) + Symbol.EMPTY_STRING));
						graphWrapper.setKpiLegendImg(imageMap.get(DriveHeaderConstants.CGI));
						graphList.add(graphWrapper);

					}
					else if (ReportConstants.RXLEVEL.equalsIgnoreCase(kpiWrapper.getKpiName())) {
						setIntoGraphWrapper(kpiIndexMap, graphList, dataList, imageMap, kpiWrapper);
					}else if (ReportConstants.RXQUAL.equalsIgnoreCase(kpiWrapper.getKpiName())) {
						setIntoGraphWrapper(kpiIndexMap, graphList, dataList, imageMap, kpiWrapper);
					}else if (ReportConstants.WROST_CI.equalsIgnoreCase(kpiWrapper.getKpiName())) {
						setIntoGraphWrapper(kpiIndexMap, graphList, dataList, imageMap, kpiWrapper);
					}else if (ReportConstants.ROUTE.equalsIgnoreCase(kpiWrapper.getKpiName())) {
						GraphWrapper graphWrapper = new GraphWrapper();
						graphWrapper.setKpiName(kpiWrapper.getKpiName());
						graphWrapper.setKpiPlotImg(
								imageMap.get(kpiIndexMap.get(kpiWrapper.getKpiName()) + Symbol.EMPTY_STRING));
						graphList.add(graphWrapper);
					}else {
						GraphWrapper graphWrapper = new GraphWrapper();

						graphWrapper.setKpiPlotImg(
								imageMap.get(kpiIndexMap.get(kpiWrapper.getKpiName()) + Symbol.EMPTY_STRING));
						graphWrapper.setKpiLegendImg(imageMap.get(ReportConstants.LEGEND + ReportConstants.UNDERSCORE
								+ kpiIndexMap.get(kpiWrapper.getKpiName())));
						graphWrapper.setKpiName(graphWrapper.getKpiName());

						graphList.add(graphWrapper);
					} 
				}
			}
		} catch (IOException e) {
			logger.error("Exception inside the mehthod getGraphAndPlotsData {}", Utils.getStackTrace(e));
		} catch (NullPointerException ne) {
			logger.error("Exception inside the mehthod getGraphAndPlotsData {}", Utils.getStackTrace(ne));
		}
	}


	private void setIntoGraphWrapper(Map<String, Integer> kpiIndexMap, List<GraphWrapper> graphList,
			List<String[]> dataList, HashMap<String, String> imageMap, KPIWrapper kpiWrapper) {
		GraphWrapper graphWrapper = setGraphDataForKpi(kpiWrapper,
				ReportUtil.convetArrayToList(dataList, kpiWrapper.getIndexKPI()));
		graphWrapper.setKpiPlotImg(
				imageMap.get(kpiIndexMap.get(kpiWrapper.getKpiName()) + Symbol.EMPTY_STRING));
		graphWrapper.setKpiLegendImg(imageMap.get(ReportConstants.LEGEND + ReportConstants.UNDERSCORE
				+ kpiIndexMap.get(kpiWrapper.getKpiName())));
			graphWrapper.setKpiName(graphWrapper.getKpiName());
		graphList.add(graphWrapper);
	}
	private HashMap<String, String> getImagesForReport(List<KPIWrapper> kpiList, DriveImageWrapper driveImageWrapper,
			Map<String, Integer> kpiIndexMap) throws IOException {
		HashMap<String, BufferedImage> bufferdImageMap = mapImageService.getLegendImages(kpiList,
				driveImageWrapper.getDataKPIs());
		bufferdImageMap.putAll(mapImageService.getDriveImagesForReport(driveImageWrapper, null, kpiIndexMap));
		String saveImagePath = (ConfigUtils.getString(ReportConstants.IMAGE_PATH_FOR_NV_REPORT) + ReportConstants.SSVT
				+ ReportConstants.FORWARD_SLASH
				+ ReportUtil.getFormattedDate(new Date(), ReportConstants.DATE_FORMAT_DD_MM_YY_HH_SS)
				+ ReportConstants.FORWARD_SLASH).toString();
		HashMap<String, String> imagemap = mapImageService.saveDriveImages(bufferdImageMap, saveImagePath, false);
		logger.info("Returning images Map with Data: {}", imagemap != null ? new Gson().toJson(imagemap): imagemap);
		return imagemap;
	}
	public GraphWrapper setGraphDataForKpi(KPIWrapper kpiWrapper, List<Double> dataList) {
		logger.info("inside the method setGraphDataForKpi  {} ,totalcount {}", kpiWrapper.getKpiName(),
				kpiWrapper.getTotalCount());
		GraphWrapper graphWrapper = new GraphWrapper();

		if (kpiWrapper.getTotalCount() != null && kpiWrapper.getTotalCount() > ForesightConstants.ZERO) {
			List<GraphDataWrapper> graphDataList = new ArrayList<>();
			Statistics staticstic = new Statistics(dataList);
			try {
				graphWrapper.setMax(staticstic.getMax());
				graphWrapper.setMin(staticstic.getMin());
				graphWrapper.setStDev(staticstic.getStdDev());
				graphWrapper.setVariance(staticstic.getVariance());
				graphWrapper.setMean(staticstic.getMean());
				graphWrapper.setKpiName(kpiWrapper.getKpiName().replace(Symbol.UNDERSCORE_STRING, Symbol.SPACE_STRING));
				graphWrapper.setUnit(ReportUtil.getUnitByKPiName(kpiWrapper.getKpiName()));
				ReportUtil.getThresholdValue(graphWrapper, dataList, kpiWrapper);
				graphWrapper.setCount(kpiWrapper.getTotalCount());
				graphWrapper.setTargetValue(kpiWrapper.getTargetValue());
				ReportUtil.setGraphDataToList(kpiWrapper, graphDataList, kpiWrapper.getRangeSlabs());
				graphDataList.sort(Comparator.comparing(GraphDataWrapper::getFrom).reversed());
				graphWrapper.setGraphDataList(graphDataList);
			} catch (Exception e) {
				logger.error("Exception inside the method setGraphDataForKpi {}", Utils.getStackTrace(e));
			}
		}
		return graphWrapper;
	}

	private void setSiteDataToWrapper(List<SiteInformationWrapper> siteInfoList, SSVTReportSubWrapper subwrapper,
			GenericWorkorder genericWorkorder) {
		List<SiteInformationWrapper> list = new ArrayList<>();
		Map<String, Integer> recipePciMap = getRecipePCIMap(genericWorkorder);
		if (Utils.isValidList(siteInfoList)) {
			for (SiteInformationWrapper siteData : siteInfoList) {
				if (siteData.getPci() != null && recipePciMap != null && recipePciMap.containsValue(siteData.getPci())) {
					list.add(siteData);
				}
				subwrapper.setSiteName(siteData.getSiteName());
			}
		}
		logger.info(" Size of SiteDataList {}", list.size());
		subwrapper.setSiteInfoList(list);
	}

	private String proceedToCreateReport(SSVTReportWrapper mainWrapper, GenericWorkorder workorderObj,
			Map<String, Object> jsonMap, Map<String, Object> imageMap) {
		logger.info("Going to create 2G SSVT Excel Report");
		String reportAssetRepo = ConfigUtils.getString(ReportConstants.SSVT_2G_REPORT_JASPER_PATH);
		List<SSVTReportWrapper> dataSourceList = new ArrayList<>();
		dataSourceList.add(mainWrapper);
		JRBeanCollectionDataSource rfbeanColDataSource = new JRBeanCollectionDataSource(dataSourceList);
		imageMap.put(ReportConstants.SUBREPORT_DIR, reportAssetRepo);
		imageMap.put(ReportConstants.LOGO_CLIENT_KEY, reportAssetRepo + ReportConstants.LOGO_CLIENT_IMG);
		logger.info("Found Parameter map: {}", imageMap);

		String filePath = ConfigUtils.getString(ReportConstants.NV_REPORTS_PATH) + "SSVT_EXCEL_2G/";
		String fileName = ReportUtil.getFileName(workorderObj.getWorkorderId(),
				(Integer) jsonMap.get(ReportConstants.ANALYTICS_REPOSITORY_ID), filePath);
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		try {
			logger.info("Going to save report on path {}", fileName);
			fileName = fileName.replace(".pdf", ".xls");
			String[] sheetNames = { "Idle Plot", "Dedicated DL Plot", "Dedicated Long Call Plot", "Site Information","KPI" };
			ReportUtil.fillDataInXlsxExporter(imageMap, reportAssetRepo + ReportConstants.MAIN_JASPER,
					rfbeanColDataSource, fileName, sheetNames);
			return fileName;
		} catch (JRException e) {
			logger.info("Exception while processing Jasper on path {} trace ==> {}", reportAssetRepo,
					Utils.getStackTrace(e));
		}
		return null;
	}

	private Response saveFileToHdfsAndUpdateStatus(AnalyticsRepository analyticObj, String filePath,
			GenericWorkorder genericWorkorder) {
		String hdfsFilePath = ConfigUtils.getString(ReportConstants.NV_REPORTS_PATH_HDFS) + "SSVT_EXCEL"
				+ ReportConstants.FORWARD_SLASH;
		return reportService.saveFileAndUpdateStatus(analyticObj.getId(), hdfsFilePath, genericWorkorder,
				new File(filePath), ReportUtil.getFileNameFromFilePath(filePath),
				NVWorkorderConstant.REPORT_INSTACE_ID);
	}
	
	private Map<String, Integer> getRecipePCIMap(GenericWorkorder genericWorkorder) {
		try {
			return mapper.readValue(genericWorkorder.getGwoMeta().get(NVWorkorderConstant.RECIPE_PCI_MAP),
					new TypeReference<Map<String, Integer>>() {
					});
		} catch (JsonProcessingException e) {
			logger.error("JsonProcessingException inside the method createReport {}", Utils.getStackTrace(e));
		}
		return null;
	}
}

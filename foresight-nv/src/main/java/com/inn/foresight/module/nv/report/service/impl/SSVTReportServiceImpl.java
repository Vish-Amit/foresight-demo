package com.inn.foresight.module.nv.report.service.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.imageio.ImageIO;
import javax.persistence.NoResultException;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.core.Response;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.xerces.impl.io.MalformedByteSequenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.inn.commons.Symbol;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.lang.NumberUtils;
import com.inn.core.generic.exceptions.application.BusinessException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.model.NeighbourCellDetail;
import com.inn.foresight.core.infra.service.INeighbourCellDetailService;
import com.inn.foresight.core.report.dao.IAnalyticsRepositoryDao;
import com.inn.foresight.core.report.model.AnalyticsRepository.progress;
import com.inn.foresight.module.fm.core.wrapper.AlarmDataWrapper;
import com.inn.foresight.module.nv.NVConfigUtil;
import com.inn.foresight.module.nv.NVConstant;
import com.inn.foresight.module.nv.core.workorder.dao.impl.IGenericWorkorderDao;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder.TemplateType;
import com.inn.foresight.module.nv.layer3.constants.NVLayer3Constants;
import com.inn.foresight.module.nv.layer3.constants.QMDLConstant;
import com.inn.foresight.module.nv.layer3.dao.INVLayer3HDFSDao;
import com.inn.foresight.module.nv.layer3.service.INVLayer3DashboardService;
import com.inn.foresight.module.nv.layer3.utils.NVLayer3Utils;
import com.inn.foresight.module.nv.report.constant.ReportIndexWrapper;
import com.inn.foresight.module.nv.report.constant.ReportSummaryIndexWrapper;
import com.inn.foresight.module.nv.report.service.IMapImagesService;
import com.inn.foresight.module.nv.report.service.IReportService;
import com.inn.foresight.module.nv.report.service.ISSVTReportService;
import com.inn.foresight.module.nv.report.service.Inbuilding.Utils.InbuildingReportUtil;
import com.inn.foresight.module.nv.report.utils.DriveHeaderConstants;
import com.inn.foresight.module.nv.report.utils.MergePDF;
import com.inn.foresight.module.nv.report.utils.ReportConstants;
import com.inn.foresight.module.nv.report.utils.ReportUtil;
import com.inn.foresight.module.nv.report.workorder.wrapper.DriveDataWrapper;
import com.inn.foresight.module.nv.report.workorder.wrapper.DriveImageWrapper;
import com.inn.foresight.module.nv.report.workorder.wrapper.KPIWrapper;
import com.inn.foresight.module.nv.report.wrapper.CapturedImagesWrapper;
import com.inn.foresight.module.nv.report.wrapper.ImageDetailWrapper;
import com.inn.foresight.module.nv.report.wrapper.KPISummaryDataWrapper;
import com.inn.foresight.module.nv.report.wrapper.NVReportConfigurationWrapper;
import com.inn.foresight.module.nv.report.wrapper.PSDataWrapper;
import com.inn.foresight.module.nv.report.wrapper.RecipeMappingWrapper;
import com.inn.foresight.module.nv.report.wrapper.RemarkReportWrapper;
import com.inn.foresight.module.nv.report.wrapper.ReportDataHolder;
import com.inn.foresight.module.nv.report.wrapper.SSVTReportSubWrapper;
import com.inn.foresight.module.nv.report.wrapper.SSVTReportWrapper;
import com.inn.foresight.module.nv.report.wrapper.SiteInformationWrapper;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.LiveDriveVoiceAndSmsWrapper;
import com.inn.foresight.module.nv.service.ISiteDetailService;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;
import com.inn.foresight.module.nv.workorder.dao.ICustomGeographyDao;
import com.inn.foresight.module.nv.workorder.dao.IWOFileDetailDao;
import com.inn.foresight.module.nv.workorder.dao.IWORecipeMappingDao;
import com.inn.foresight.module.nv.workorder.model.CustomGeography;
import com.inn.foresight.module.nv.workorder.model.WOFileDetail;
import com.inn.foresight.module.nv.workorder.model.WORecipeMapping;
import com.inn.foresight.module.nv.workorder.service.ICustomGeographyService;
import com.inn.foresight.module.nv.workorder.utils.NVWorkorderUtils;
import com.inn.foresight.module.nv.workorder.wrapper.CustomGeographyWrapper;
import com.inn.product.legends.dao.ILegendRangeDao;
import com.inn.product.legends.utils.LegendWrapper;
import com.inn.product.systemconfiguration.dao.SystemConfigurationDao;
import com.inn.product.systemconfiguration.model.SystemConfiguration;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * The Class SSVTReportServiceImpl.
 */
@Service("SSVTReportServiceImpl")
public class SSVTReportServiceImpl implements ISSVTReportService {

	/** The logger. */
	private Logger logger = LogManager.getLogger(SSVTReportServiceImpl.class);

	// ** The legend range dao. *//*

	@Autowired
	private ILegendRangeDao legendRangeDao;

	/** The macro site detail dao. */

	@Autowired
	private ISiteDetailService siteDetailService;

	/** The map image service. */

	@Autowired
	private IMapImagesService mapImageService;

	@Autowired
	private IGenericWorkorderDao iGenericWorkorderDao;

	@Autowired
	private IWORecipeMappingDao iwoRecipeMappingDao;

	@Autowired
	private IAnalyticsRepositoryDao analyticsrepositoryDao;
	@Autowired
	private INVLayer3DashboardService nvLayer3DashboardService;
	@Autowired
	private IReportService reportService;

	@Autowired
	private ICustomGeographyService customGeographyService;

	@Autowired
	private ICustomGeographyDao customGeographyDao;

	@Autowired
	private INeighbourCellDetailService neighbourCellDetailService;

	@Autowired
	private SystemConfigurationDao iSystemConfigurationDao;

	@Autowired
	private INVLayer3HDFSDao invLayer3HDFSDao;
	@Autowired
	private IWOFileDetailDao woFileDetailDao;

	/** The mapper. */
	ObjectMapper mapper = new ObjectMapper();

	public SSVTReportServiceImpl() {
		super();
	}

	@Override
	@Transactional
	public Response execute(String json) {
		logger.info("Inside execute method to create SSVT Report with json {} ", json);
		Integer analyticsrepoId = null;
		try {
			Map<String, Object> jsonMap = reportService.getJsonDataMap(json);
			Integer workorderId = (Integer) jsonMap.get(ReportConstants.WORKORDER_ID);
			Integer quickWorkorderId = (Integer) jsonMap.get(ReportConstants.QUICK_WORKORDER_ID);
			analyticsrepoId = (Integer) jsonMap.get(ReportConstants.ANALYTICS_REPOSITORY_ID);
			String username = jsonMap.get(NVLayer3Constants.ASSIGN_TO_JSON_KEY) != null
					? jsonMap.get(NVLayer3Constants.ASSIGN_TO_JSON_KEY).toString()
					: null;
			boolean isFilesProcessed = reportService.getFileProcessedStatusForWorkorders(Arrays.asList(workorderId));
			if (isFilesProcessed) {
				Integer woIdForData = workorderId;
				if (quickWorkorderId != null) {
					woIdForData = quickWorkorderId;
				}
				generateReport(jsonMap, analyticsrepoId, woIdForData, username, workorderId);
			}
		} catch (IOException | ForbiddenException e) {
			logger.error("Error nside the method createReportForWorkOrderID for json {} , {} ", json,
					Utils.getStackTrace(e));
			analyticsrepositoryDao.updateStatusInAnalyticsRepository(analyticsrepoId, null, "Something Went Wrong",
					progress.Failed, null);
		} catch (Exception e1) {
			logger.error("Error nside the method createReportForWorkOrderID for json {} , {} ", json,
					Utils.getStackTrace(e1));
			analyticsrepositoryDao.updateStatusInAnalyticsRepository(analyticsrepoId, null, "Something Went Wrong",
					progress.Failed, null);
		}

		return Response.ok(ForesightConstants.FAILURE_JSON).build();
	}

	private Response generateReport(Map<String, Object> jsonDataMap, Integer analyticsrepoId, Integer workorderId,
			String username, Integer woIdForUpdate) throws IOException {
		logger.info("Inside method generate Report with workorderId {}  ,woIdForUpdate {} ", workorderId,
				woIdForUpdate);
		try {
			GenericWorkorder workorderObj = iGenericWorkorderDao.findByPk(workorderId);
			GenericWorkorder workorderObjForUpdate = iGenericWorkorderDao.findByPk(woIdForUpdate);
			String filePath = ConfigUtils.getString(ReportConstants.NV_REPORTS_PATH) + ReportConstants.SSVT
					+ ReportConstants.FORWARD_SLASH + workorderObj.getTemplateType().name()
					+ ReportConstants.FORWARD_SLASH + workorderObj.getGwoMeta().get(ReportConstants.SITE_ID)
					+ ReportConstants.FORWARD_SLASH;
			jsonDataMap.put(ReportConstants.TEST_TYPE, workorderObj.getTemplateType().name());
			String band = getBandfromGwoMeta(workorderObj);
			Map<String, List<String>> driveRecipeDetailMap = nvLayer3DashboardService
					.getDriveRecipeDetail(workorderObj.getId());
			if (driveRecipeDetailMap != null && !driveRecipeDetailMap.get(QMDLConstant.RECIPE).isEmpty()) {

				return generateSSVTReport(jsonDataMap, filePath, analyticsrepoId, username, workorderObj,
						driveRecipeDetailMap, band, workorderObjForUpdate);
			} else {
				analyticsrepositoryDao.updateStatusInAnalyticsRepository(analyticsrepoId, null, "Data is Not Available",
						progress.Failed, null);
			}

		} catch (Exception e) {
			logger.error("Exception In generateReport {} ", Utils.getStackTrace(e));
		}
		return Response.ok(ForesightConstants.FAILURE_JSON).build();
	}

	@Override
	public Map<String, Map<String, List<String>>> getRecipeMapForRecipeId(Integer workorderId,
			Map<String, String> recipeMap) {
		Map<String, Map<String, List<String>>> map = new HashMap<>();

		for (Entry<String, String> entry : recipeMap.entrySet()) {
			List<String> operatorList = new ArrayList<>();
			Map<String, List<String>> driveRecipeMap = new HashMap<>();
			List<String> recipeList = new ArrayList<>();

			List<WORecipeMapping> mappingList = iwoRecipeMappingDao.getWORecipeByGWOId(workorderId);
			for (WORecipeMapping woRecipeMapping : mappingList) {
				if (woRecipeMapping.getRecipe() != null
						&& entry.getValue().equalsIgnoreCase(woRecipeMapping.getRecipe().getRecipeId())) {
					List<WOFileDetail> fileDetail = woFileDetailDao
							.getFileDetailByWORecipeMappingId(woRecipeMapping.getId());
					recipeList.add(woRecipeMapping.getId().toString());
					for (WOFileDetail woFileDetail : fileDetail) {
						operatorList.add(NVWorkorderUtils.getOperatorNameFromFileName(woFileDetail.getFileName()));

					}

				}
			}
			driveRecipeMap.put(QMDLConstant.OPERATOR, operatorList);
			driveRecipeMap.put(QMDLConstant.RECIPE, recipeList);
			map.put(entry.getKey(), driveRecipeMap);

		}

		return map;

	}

	private String getBandfromGwoMeta(GenericWorkorder workorderObj) {
		String bandValue = null;
		String band = null;
		try {
			Map<String, String> geoMap = workorderObj.getGwoMeta();
			band = geoMap.get("band");
			String bandJson = iSystemConfigurationDao.getValueByName("BAND_CONFIGURATION");
			bandValue = getBandFromJson(bandJson).get(band);
			logger.info("band Value is {} ,bandValue {}  ", band, bandValue);
		} catch (Exception e) {
			logger.error("Unable to fetch the Band alias for  band {} ,  {} ", band, Utils.getStackTrace(e));
		}
		return bandValue != null ? bandValue : "A";
	}

	private void setCallData(String[] summaryData, SSVTReportSubWrapper subWrapper,
			Map<String, Integer> summaryKpiIndexMap) {
		try {
			List<LiveDriveVoiceAndSmsWrapper> callList = ReportUtil.callDataListForReport(summaryData,
					summaryKpiIndexMap);
			List<LiveDriveVoiceAndSmsWrapper> callDataList = ReportUtil.getCallPlotDataListForReport(summaryData,
					summaryKpiIndexMap);

			if (!callList.isEmpty()) {
				subWrapper.setCallDataList(callList);
			}

			subWrapper.setCallPlotDataList(callDataList);
		} catch (Exception e) {
			logger.error("Exception inside the method setCallData{}", Utils.getStackTrace(e));

		}
	}

	/**
	 * @param json
	 * @param filePath
	 * @param workorderId
	 * @param analyticsrepoId
	 * @param username
	 * @param workorderObj
	 * @param band
	 * @param workorderObjForUpdate
	 * @param summaryData
	 * @param driveRecipeDetailMap2
	 * @param subWrapperList
	 * @param subWrapper
	 * @param mainWrapper
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 */
	@SuppressWarnings("unchecked")
	private Response generateSSVTReport(Map<String, Object> jsonDataMap, String filePath, Integer analyticsrepoId,
			String username, GenericWorkorder workorderObj, Map<String, List<String>> driveRecipeDetailMap, String band,
			GenericWorkorder workorderObjForUpdate) throws IOException, IllegalArgumentException,
			IllegalAccessException, NoSuchFieldException, SecurityException {
		SSVTReportSubWrapper subWrapper = new SSVTReportSubWrapper();
		SSVTReportWrapper mainWrapper = new SSVTReportWrapper();
		List<String> fetchSummaryKPIList = ReportSummaryIndexWrapper.getLiveDriveKPIs();
		Map<String, Integer> summaryKpiIndexMap = ReportSummaryIndexWrapper
				.getLiveDriveKPIIndexMap(fetchSummaryKPIList);
		List<String> fetchKPIList = ReportIndexWrapper.getLiveDriveKPIs();
		Map<String, Integer> kpiIndexMap = ReportIndexWrapper.getLiveDriveKPIIndexMap(fetchKPIList);
		logger.info("kpiIndexMap ==={}",kpiIndexMap);
		try {
			List<String[]> csvDataArray = null;
			String[] summaryData = reportService.getSummaryDataForReport(workorderObj.getId(),
					driveRecipeDetailMap.get(QMDLConstant.RECIPE), fetchSummaryKPIList);
			List<DriveDataWrapper> imageDataList = reportService.getSpeedTestDatafromHbase(workorderObj.getId(),
					driveRecipeDetailMap.get(QMDLConstant.RECIPE));
			List<KPIWrapper> kpiList = null;
			List<KPISummaryDataWrapper> kpiSummaryDataList = null;
			if (checkISAutomationWorkorder(jsonDataMap)) {
				logger.error("inside the filter data  for quick and full");
				String mapJson = iSystemConfigurationDao
						.getSystemConfigurationByType(ReportConstants.NV_SSVT_REPORT_RECIPEID_MAP).get(0).getValue();
				Map<String, String> recipeIDMap = ReportUtil.convertCSVStringToMap(mapJson);

				Map<String, Map<String, List<String>>> recipeWiseIDMap = getRecipeMapForRecipeId(workorderObj.getId(),
						recipeIDMap);
				Map<String, String[]> recipeWiseSummaryMap = getRecipeWiseSummary(recipeWiseIDMap, workorderObj.getId(),
						fetchSummaryKPIList);
				csvDataArray = reportService.getDriveDataRecipeWiseTaggedForReport(Arrays.asList(workorderObj.getId()),
						recipeWiseIDMap.get(ReportConstants.FTP_DL).get(QMDLConstant.RECIPE), fetchKPIList,
						kpiIndexMap);

				setSummaryData(subWrapper, mainWrapper, csvDataArray, kpiIndexMap);
				kpiList = getKpiStatsList(workorderObj, recipeWiseIDMap.get(ReportConstants.FTP_DL),
						DriveHeaderConstants.getKpiIndexMapFORSsvT(kpiIndexMap));
				List<LiveDriveVoiceAndSmsWrapper> handoverPlotDataList = ReportUtil
						.getHandoverPlotDataListForReport(recipeWiseSummaryMap.get(ReportConstants.FTP_DL),summaryKpiIndexMap);
				subWrapper.setHandoverPlotDataList(handoverPlotDataList);

				ReportDataHolder reportDataHolder = getReportDataHolder(summaryKpiIndexMap, kpiIndexMap, csvDataArray,
						recipeWiseSummaryMap);
				kpiSummaryDataList = getkpiSummaryListAutomationReport(recipeWiseIDMap.get(ReportConstants.FTP_DL),
						summaryData, workorderObj, imageDataList, kpiList,
						TemplateType.valueOf(jsonDataMap.get(ReportConstants.TEST_TYPE).toString()).getValue(),
						reportDataHolder);
				setRRCInformation(recipeWiseSummaryMap.get(ReportConstants.KEY_RRC), subWrapper, summaryKpiIndexMap);

			} else {
				csvDataArray = ReportUtil
						.convertCSVStringToDataList(getDriveData(workorderObj.getId(), driveRecipeDetailMap));
				
				csvDataArray = reportService.getDriveDataRecipeWiseTaggedForReport(Arrays.asList(workorderObj.getId()),
						driveRecipeDetailMap.get(QMDLConstant.RECIPE), fetchKPIList, kpiIndexMap);

				setSummaryData(subWrapper, mainWrapper, csvDataArray, kpiIndexMap);
				List<LiveDriveVoiceAndSmsWrapper> handoverPlotDataList = ReportUtil
						.getHandoverPlotDataListForReport(summaryData, summaryKpiIndexMap);
				subWrapper.setHandoverPlotDataList(handoverPlotDataList);
				kpiList = getKpiStatsList(workorderObj, driveRecipeDetailMap,
						DriveHeaderConstants.getKpiIndexMapFORSsvT(kpiIndexMap));
				ReportDataHolder reportDataHolder = getReportDataHolder(summaryKpiIndexMap, kpiIndexMap, csvDataArray,
						null);

				kpiSummaryDataList = getkpiSummaryList(driveRecipeDetailMap, summaryData, workorderObj, imageDataList,
						csvDataArray, kpiList,
						TemplateType.valueOf(jsonDataMap.get(ReportConstants.TEST_TYPE).toString()).getValue(),
						reportDataHolder);
				setRRCInformation(summaryData, subWrapper, summaryKpiIndexMap);

			}

			Map<String, Object> siteDataMap = getSiteDataMap(workorderObj, csvDataArray,kpiIndexMap);
			List<SiteInformationWrapper> siteInfoList = ReportUtil.filterNEDataByBand(
					ReportUtil.getSiteFromMap(siteDataMap), ReportUtil.findBandDetailByGWOMetaData(workorderObj));
			List<List<List<Double>>> driveRoute = getDriveRoute(workorderObj, mainWrapper);
			HashMap<String, Object> images = getImageMapOfCombinedData(csvDataArray, siteInfoList, imageDataList,
					kpiList, driveRoute, band, kpiIndexMap);
			reportService.saveKpiSummaryData(workorderObj, kpiSummaryDataList);
			String fileName = getFileName(filePath, analyticsrepoId, workorderObj, mainWrapper);
			subWrapper = populateGraphData(subWrapper, kpiList, csvDataArray,kpiIndexMap);
			subWrapper.setEarfcnList(reportService.setDataForEarfcnGraph(csvDataArray, kpiIndexMap));
			setCallData(summaryData, subWrapper, summaryKpiIndexMap);
			mainWrapper = preparedWrapperForJasper(workorderObj, ReportUtil.getSiteFromMap(siteDataMap),
					kpiSummaryDataList, subWrapper, mainWrapper, username, getSiteHavingAlaram(siteDataMap));

			//////////////////// Capture Images Data ////////////////////
			mainWrapper = setSiteAuditImageInWrapper(workorderObj, mainWrapper, images);
			/////////////////////////////////////////////////
			Response response= createFile(jsonDataMap, filePath, analyticsrepoId, workorderObj, workorderObjForUpdate, mainWrapper,
					images, fileName, getHDFSFilePath(workorderObj));
			reportService.saveKpiSummaryData(workorderObj, kpiSummaryDataList);
			return response;

		} catch (Exception e) {
			logger.error("Unable to generate the SSVT Report due to exception {} ", Utils.getStackTrace(e));
		}
		return Response.ok(ForesightConstants.FAILURE_JSON).build();
	}

	private boolean checkISAutomationWorkorder(Map<String, Object> jsonDataMap) {
		return jsonDataMap.get(ReportConstants.TEST_TYPE).toString()
				.equalsIgnoreCase(TemplateType.NV_SSVT_QUICK.name())
				|| jsonDataMap.get(ReportConstants.TEST_TYPE).toString()
						.equalsIgnoreCase(TemplateType.NV_SSVT_FULL.name());
	}

	private ReportDataHolder getReportDataHolder(Map<String, Integer> summaryKpiIndexMap,
			Map<String, Integer> kpiIndexMap, List<String[]> csvDataArray, Map<String, String[]> recipeWiseSummaryMap) {
		ReportDataHolder wrapper = new ReportDataHolder();
		wrapper.setDataList(csvDataArray);
		wrapper.setSummaryKpiIndexMap(summaryKpiIndexMap);
		wrapper.setKpiIndexMap(kpiIndexMap);
		wrapper.setRecipeWiseSummaryMap(recipeWiseSummaryMap);
		return wrapper;
	}

	@Override
	public Map<String, String[]> getRecipeWiseSummary(Map<String, Map<String, List<String>>> recipeWiseIDMap,
			Integer workorderId, List<String> fetchSummaryKPIList) {
		Map<String, String[]> recipeWiseSummaryMap = new HashMap<>();
		for (Entry<String, Map<String, List<String>>> entry : recipeWiseIDMap.entrySet()) {
			if (Utils.isValidList(entry.getValue().get(QMDLConstant.RECIPE))
					&& Utils.isValidList(entry.getValue().get(QMDLConstant.OPERATOR))) {
				recipeWiseSummaryMap.put(entry.getKey(), reportService.getSummaryDataForReport(workorderId,
						entry.getValue().get(QMDLConstant.RECIPE), fetchSummaryKPIList));
			}
		}
		return recipeWiseSummaryMap;
	}

	private List<List<List<Double>>> getDriveRoute(GenericWorkorder workorderObj, SSVTReportWrapper mainWrapper) {
		List<List<List<Double>>> driveRoute = getCustomDriveRoute(workorderObj);
		if (driveRoute != null && driveRoute.size() > ReportConstants.INDEX_ZER0) {
			mainWrapper.setSiteAddressTitle("Site Address with Planned Route");
		} else {
			mainWrapper.setSiteAddressTitle("Site Address");
		}
		return driveRoute;
	}

	private String getFileName(String filePath, Integer analyticsrepoId, GenericWorkorder workorderObj,
			SSVTReportWrapper mainWrapper) {
		String fileName = ReportUtil.getFileName(workorderObj.getWorkorderId(), analyticsrepoId, filePath);
		mainWrapper.setFileName(filePath + ReportConstants.WORKORDER_ID + Symbol.HYPHEN + workorderObj.getWorkorderId()
				+ ReportConstants.PDF_EXTENSION);
		return fileName;
	}

	private Map<String, Object> getSiteDataMap(GenericWorkorder workorderObj, List<String[]> csvDataArray, Map<String, Integer> kpiIndexMap) {
		Map<String, Long> driveTimeStampMap = ReportUtil.getDriveTimeStampMap(csvDataArray,kpiIndexMap);
		logger.info("driveTimeStampMap Data {} ", driveTimeStampMap);
		return reportService.getSiteDataForReport(workorderObj, driveTimeStampMap.get(ReportConstants.START_TIMESTAMP),
				driveTimeStampMap.get(ReportConstants.END_TIMESTAMP), csvDataArray, false, false);
	}

	private List<AlarmDataWrapper> getSiteHavingAlaram(Map<String, Object> siteDataMap) {
		List<AlarmDataWrapper> listOfNeHaveAlarm = siteDataMap.get(ReportConstants.NE_ALARM_DATA) != null
				? (List<AlarmDataWrapper>) siteDataMap.get(ReportConstants.NE_ALARM_DATA)
				: null;
		logger.info("listOfNeHaveAlarm Data Size {} ", listOfNeHaveAlarm != null ? listOfNeHaveAlarm.size() : null);
		return listOfNeHaveAlarm;
	}

	private String getHDFSFilePath(GenericWorkorder workorderObj) {
		return ConfigUtils.getString(ReportConstants.NV_REPORTS_PATH_HDFS) + ReportConstants.SSVT
				+ ReportConstants.FORWARD_SLASH + workorderObj.getTemplateType().name() + ReportConstants.FORWARD_SLASH
				+ workorderObj.getGwoMeta().get(ReportConstants.SITE_ID) + ReportConstants.FORWARD_SLASH;

	}

	private Response createFile(Map<String, Object> jsonDataMap, String filePath, Integer analyticsrepoId,
			GenericWorkorder workorderObj, GenericWorkorder workorderObjForUpdate, SSVTReportWrapper mainWrapper,
			HashMap<String, Object> images, String fileName, String hdfsPath) throws IOException {
		createAndMergeFiles(jsonDataMap, filePath, workorderObj.getId(), analyticsrepoId, mainWrapper, images,
				fileName);
		reportService.genrateDocxReport(hdfsPath, fileName,
				ReportUtil.getFileNameForDoc(workorderObj.getWorkorderId(), analyticsrepoId));
		return reportService.saveFileAndUpdateStatus(analyticsrepoId, hdfsPath, workorderObjForUpdate,
				new File(fileName), getFileNameFromFilePath(fileName), NVWorkorderConstant.REPORT_INSTACE_ID); // Last
																												// parameter
																												// File
																												// Name
	}

	private void setRRCInformation(String[] smmryData, SSVTReportSubWrapper subWrapper,
			Map<String, Integer> summaryKpiIndexMap) {
		try {
			if (smmryData != null && !smmryData[summaryKpiIndexMap.get(ReportConstants.RRC_INITIATE)].isEmpty()
					&& !smmryData[summaryKpiIndexMap.get(ReportConstants.RRC_SUCCESS)].isEmpty()
					&& NumberUtils.isValidNumber(
							Double.parseDouble(smmryData[summaryKpiIndexMap.get(ReportConstants.RRC_INITIATE)]))
					&& NumberUtils.isValidNumber(
							Double.parseDouble(smmryData[summaryKpiIndexMap.get(ReportConstants.RRC_SUCCESS)]))) {
				Double rrcAttempt = Double.parseDouble(smmryData[summaryKpiIndexMap.get(ReportConstants.RRC_INITIATE)]);
				Double rrcSuccess = Double.parseDouble(smmryData[summaryKpiIndexMap.get(ReportConstants.RRC_SUCCESS)]);
				Double val = (rrcSuccess * ReportConstants.INDEX_HUNDRED) / rrcAttempt;
				val = ReportUtil.parseToFixedDecimalPlace(val, ReportConstants.INDEX_TWO);
				subWrapper.setRrcAttempt(rrcAttempt);
				subWrapper.setRrcConnect(rrcSuccess);
				subWrapper.setRrcReqSuccessRate(val);
			}
		} catch (Exception e) {
			logger.error("setRRCInformation=== {}", Utils.getStackTrace(e));
		}
	}

	private String getFileNameFromFilePath(String filePath) {
		if (filePath != null && (filePath.contains(ReportConstants.PDF_EXTENSION)
				|| filePath.contains(ReportConstants.PPTX_EXTENSION))) {
			try {
				String fileName = filePath.substring(filePath.lastIndexOf(ReportConstants.FORWARD_SLASH) + 1,
						filePath.length());
				logger.info("Going to return the downloadFileName {} ", fileName);
				return fileName;
			} catch (Exception e) {
				logger.error("Exception inside method getFileNameFromFilePath {} ", Utils.getStackTrace(e));
			}
		}
		return null;
	}

	@Override
	public SSVTReportWrapper setSiteAuditImageInWrapper(GenericWorkorder workorderObj, SSVTReportWrapper mainWrapper,
			Map<String, Object> map) {
		try {
			Integer workOrderId = workorderObj.getId();
			String imgFilePath = getValueFromGWOMetaForKey(workorderObj, ReportConstants.IMG_FILE_PATH);
			if (imgFilePath != null) {
				Map<String, List<ImageDetailWrapper>> groupByImageCategory = getCategoryWiseImageDataList(workOrderId,
						imgFilePath);
				logger.info("groupByImageCategory Data {} ", groupByImageCategory);
				getListCapturedImagesWrapper(groupByImageCategory, mainWrapper, map);
				return mainWrapper;
			}
		} catch (Exception e) {
			logger.error("Exception inside method setCaptureImageDataInMainWrapper {} ", Utils.getStackTrace(e));
		}
		return mainWrapper;
	}

	private Map<String, List<ImageDetailWrapper>> getCategoryWiseImageDataList(Integer workOrderId,
			String imgFilePath) {
		// New Api to get the images data from SF
		List<String> listOfLocalFilePath = null;
		if (imgFilePath != null) {
			listOfLocalFilePath = getLocalFilePathForImagesDataFromSF(imgFilePath);
		} else {
			listOfLocalFilePath = getLocalFilePathForImagesData(workOrderId);
		}
		logger.info("imageFIlePath List {} ", listOfLocalFilePath);
		List<ImageDetailWrapper> imageDetailList = getCapturedImagesList(listOfLocalFilePath);
		logger.info("imageDetailList Data {} ", imageDetailList != null ? imageDetailList.size() : null);
		Map<String, List<ImageDetailWrapper>> groupByImageCategory = new HashMap<>();
		if (imageDetailList != null && imageDetailList.size() > ReportConstants.INDEX_ZER0) {
			imageDetailList = imageDetailList.stream().filter(x -> x.getImageCategory() != null)
					.collect(Collectors.toList());
			groupByImageCategory = imageDetailList.stream()
					.collect(Collectors.groupingBy(ImageDetailWrapper::getImageCategory));
		}
		ReportUtil.deleteFilesFromLocal(listOfLocalFilePath);
		return groupByImageCategory;
	}

	private List<List<List<Double>>> getCustomDriveRoute(GenericWorkorder workorderObj) {
		CustomGeographyWrapper customGeography = getCustomGeographyRoute(workorderObj);
		logger.debug("customGeography Data {} ", customGeography != null ? new Gson().toJson(customGeography) : customGeography);
		if (customGeography != null && customGeography.getBoundary() != null) {
			return new Gson().fromJson(customGeography.getBoundary(), new TypeToken<List<List<List<Double>>>>() {
			}.getType());
		}
		return Collections.emptyList();
	}

	/**
	 * @param csvDataArray
	 * @param siteInfoList
	 * @param imageDataList
	 * @param kpiList
	 * @param driveRoute
	 * @param band
	 * @param kpiIndexMap
	 * @return
	 * @throws IOException
	 * @throws BusinessException
	 * @throws MalformedByteSequenceException
	 * @throws ForbiddenException
	 */
	private HashMap<String, Object> getImageMapOfCombinedData(List<String[]> csvDataArray,
			List<SiteInformationWrapper> siteInfoList, List<DriveDataWrapper> imageDataList, List<KPIWrapper> kpiList,
			List<List<List<Double>>> driveRoute, String band, Map<String, Integer> kpiIndexMap) throws IOException {
		DriveImageWrapper driveImageWrapper = new DriveImageWrapper(csvDataArray,
				kpiIndexMap.get(ReportConstants.LATITUDE), kpiIndexMap.get(ReportConstants.LONGITUDE),
				kpiIndexMap.get(ReportConstants.PCI_PLOT), kpiList, siteInfoList, null, driveRoute, band);
		HashMap<String, Object> images = prepareImageMap(
				getImagesForReport(kpiList, driveImageWrapper, imageDataList, kpiIndexMap),kpiIndexMap);
		logger.info("image map is  {}", images);
		images.putAll(getSectorWiseImageFromList(imageDataList));
		return images;
	}

	/**
	 * @param workorderObj
	 * @param driveRecipeDetailMap
	 * @return
	 * @throws RestException
	 */
	@Override
	public List<KPIWrapper> getKpiStatsList(GenericWorkorder workorderObj,
			Map<String, List<String>> driveRecipeDetailMap, Map<String, Integer> indexMap) {
		List<LegendWrapper> legendList = legendRangeDao.findAllLegendRangesAppliedTo(ReportConstants.SSVT_REPORT);
		return ReportUtil.convertLegendsListToKpiWrapperList(legendList, indexMap);

	}

	/**
	 * @param driveRecipeDetailMap
	 * @param summaryData
	 * @param genericWorkorder
	 * @param imageDataList
	 * @param kpiList
	 * @param csvDataArray
	 * @param reportDataHolder
	 * @param dlRecipeSummary
	 * @param driveImageWrapper
	 * @return
	 */
	private List<KPISummaryDataWrapper> getkpiSummaryList(Map<String, List<String>> driveRecipeDetailMap,
			String[] summaryData, GenericWorkorder genericWorkorder, List<DriveDataWrapper> imageDataList,
			List<String[]> csvDataArray, List<KPIWrapper> kpiList, String reportTestType,
			ReportDataHolder reportDataHolder) {
		List<Double> dlSpeedtestList = imageDataList.stream().map(DriveDataWrapper::getDltpt)
				.collect(Collectors.toList());
		dlSpeedtestList = dlSpeedtestList != null ? dlSpeedtestList.stream().distinct().collect(Collectors.toList())
				: dlSpeedtestList;
		List<KPISummaryDataWrapper> kpisummarList = getKPISummaryDataForWorkOrderId(genericWorkorder.getId(),
				csvDataArray, kpiList, summaryData, reportTestType, null, dlSpeedtestList, driveRecipeDetailMap,
				reportDataHolder);
		logger.info("After generating Data reportTestType {} , FINAL LIST SIZE {} ", reportTestType,
				kpisummarList.size());
		return kpisummarList;

	}

	@Override
	public List<KPISummaryDataWrapper> getkpiSummaryListAutomationReport(Map<String, List<String>> driveRecipeDetailMap,
			String[] summaryData, GenericWorkorder genericWorkorder, List<DriveDataWrapper> imageDataList,
			List<KPIWrapper> kpiList, String reportTestType, ReportDataHolder reportDataHolder) {
		List<Double> dlSpeedtestList = imageDataList.stream().map(DriveDataWrapper::getDltpt)
				.collect(Collectors.toList());
		dlSpeedtestList = dlSpeedtestList != null ? dlSpeedtestList.stream().distinct().collect(Collectors.toList())
				: dlSpeedtestList;
		List<KPISummaryDataWrapper> kpisummarList = getKPISummaryDataForAutomationReport(genericWorkorder.getId(),
				kpiList, reportDataHolder.getRecipeWiseSummaryMap(), reportTestType, dlSpeedtestList,
				driveRecipeDetailMap, summaryData, reportDataHolder);

		logger.info("After generating Data reportTestType {} , FINAL LIST SIZE {} ", reportTestType,
				kpisummarList.size());
		return kpisummarList;

	}

	/**
	 * @param json
	 * @param filePath
	 * @param workorderId
	 * @param analyticsrepoId
	 * @param ssvtReportWrapper
	 * @param images
	 * @param fileName
	 * @throws RestException
	 * @throws IOException
	 * @throws BusinessException
	 * @throws MalformedByteSequenceException
	 * @throws ForbiddenException
	 */
	private void createAndMergeFiles(Map<String, Object> jsonDataMap, String filePath, Integer workorderId,
			Integer analyticsrepoId, SSVTReportWrapper ssvtReportWrapper, HashMap<String, Object> images,
			String fileName) throws IOException {
		logger.info("Inside method createAndMergeFiles  with ssvtReportWrapper");
		List<File> files = new ArrayList<>();
		if (jsonDataMap.get(ReportConstants.TEST_TYPE).toString().equalsIgnoreCase(TemplateType.NV_SSVT_QUICK.name())
				&& !(jsonDataMap.get(ReportConstants.IS_PRE_DT) != null
						&& jsonDataMap.get(ReportConstants.IS_PRE_DT).equals(Boolean.TRUE))) {
			files.add(generateCombinedDataReport(ReportConstants.QUICK_SSVTREPORT_JASPER_FOLDER_PATH, images,
					ssvtReportWrapper));
		} else {
			files.add(generateCombinedDataReport(ReportConstants.SSVTREPORT_JASPER_FOLDER_PATH, images,
					ssvtReportWrapper));

			files.addAll(generateRecipeWiseFiles(jsonDataMap, ssvtReportWrapper));

			files.add(generateRemarkPageOfReport(filePath, workorderId, ssvtReportWrapper, files));
		}
		files.add(generateThankyouPageOfReport(workorderId, analyticsrepoId));

		MergePDF.mergeFiles(fileName, files);
		logger.info("mainWrapper.getFileName() {}", ssvtReportWrapper.getFileName());
	}

	private File generateThankyouPageOfReport(Integer workorderId, Integer analyticsrepoId) {
		String fileName = null;
		try {
			String reportAssetRepo = ConfigUtils.getString(ReportConstants.SSVTREPORT_JASPER_FOLDER_PATH);
			String filePath = ConfigUtils.getString(ReportConstants.NV_REPORTS_PATH) + ReportConstants.SSVT
					+ ReportConstants.FORWARD_SLASH;
			fileName = ReportUtil.getFileName(workorderId + Symbol.HYPHEN + ReportConstants.THANK_YOU, analyticsrepoId,
					filePath);
			HashMap<String, Object> imageMap = new HashMap<>();
			imageMap.put(ReportConstants.SUBREPORT_DIR, reportAssetRepo);
			imageMap.put(ReportConstants.LOGO_CLIENT_KEY, reportAssetRepo + ReportConstants.LOGO_CLIENT_IMG);
			imageMap.put(ReportConstants.LOGO_NV_KEY, reportAssetRepo + ReportConstants.LOGO_NV_IMG);
			JasperRunManager.runReportToPdfFile(reportAssetRepo + ReportConstants.SSVT_THANK_YOU_PAGE, fileName,
					imageMap);
		} catch (JRException e) {
			logger.info("JRException inside method getThankYouPage of Report {} ", Utils.getStackTrace(e));
		} catch (Exception e) {
			logger.info("Exception inside method getThankYouPage of Report {} ", Utils.getStackTrace(e));
		}
		return ReportUtil.getIfFileExists(fileName);
	}

	/**
	 * @param filePath
	 * @param workorderId
	 * @param ssvtReportWrapper
	 * @param files
	 * @return
	 */
	private File generateRemarkPageOfReport(String filePath, Integer workorderId, SSVTReportWrapper ssvtReportWrapper,
			List<File> files) {
		RemarkReportWrapper remarkWrapper = new RemarkReportWrapper();
		remarkWrapper.setSiteName(ssvtReportWrapper.getSiteName());
		remarkWrapper.setTestDate(ssvtReportWrapper.getTestDate());
		return reportService.generateRemarkReport(workorderId, filePath, files,
				ConfigUtils.getString(ReportConstants.SSVTREPORT_JASPER_FOLDER_PATH), remarkWrapper);
	}

	/**
	 * @param json
	 * @param ssvtReportWrapper
	 * @param files
	 * @throws RestException
	 * @throws IOException
	 * @throws BusinessException
	 * @throws MalformedByteSequenceException
	 * @throws ForbiddenException
	 */
	private List<File> generateRecipeWiseFiles(Map<String, Object> jsonDataMap, SSVTReportWrapper ssvtReportWrapper)
			throws IOException {
		SSVTReportWrapper mainRecipeWrapper = new SSVTReportWrapper();
		mainRecipeWrapper.setSiteName(ssvtReportWrapper.getSiteName());
		mainRecipeWrapper.setTestDate(ssvtReportWrapper.getTestDate());
		return executeForRecipesFile(jsonDataMap, mainRecipeWrapper);

	}

	private CustomGeographyWrapper getCustomGeographyRoute(GenericWorkorder genericWorkorder) {
		Map<String, String> gwoMeta = genericWorkorder.getGwoMeta();
		if (gwoMeta != null) {
			try {
				String json = gwoMeta.get(ReportConstants.KEY_RECIPE_CUSTOM_GEO_MAP);
				if (json != null) {
					@SuppressWarnings("unchecked")
					Map<String, Integer> recipeWiseGeographyIdMap = new ObjectMapper().readValue(json, HashMap.class);
					if (recipeWiseGeographyIdMap != null) {
						Collection<Integer> rouetIds = recipeWiseGeographyIdMap.values();
						List<Integer> routeIdList = new ArrayList<>(rouetIds);
						if (routeIdList.size() > ReportConstants.INDEX_ZER0) {
							CustomGeography customGeography = customGeographyDao
									.findByPk(routeIdList.get(ReportConstants.INDEX_ZER0));
							if (customGeography != null) {
								return customGeographyService.getCustomGeography(
										routeIdList.get(ReportConstants.INDEX_ZER0), customGeography.getType());
							}
						}
					}
				}
			} catch (Exception e) {
				logger.error("Unable to find the Custom geography route {} ", Utils.getStackTrace(e));
			}
		}
		return null;
	}

	private SSVTReportSubWrapper populateGraphData(SSVTReportSubWrapper subWrapper, List<KPIWrapper> kpiList,
			List<String[]> arlist, Map<String, Integer> kpiIndexMap) {
		for (KPIWrapper kpiWrapper : kpiList) {
			populateGraphDataForkpi(subWrapper, kpiWrapper, arlist,kpiIndexMap);
		}
		return subWrapper;
	}

	private SSVTReportSubWrapper populateGraphDataForkpi(SSVTReportSubWrapper subWrapper, KPIWrapper kpiWrapper,
			List<String[]> arlist, Map<String, Integer> kpiIndexMap) {
		logger.info("inside the method populateGraphDataForkpi");
		try {
			if (ReportConstants.RSRP.equalsIgnoreCase(kpiWrapper.getKpiName())) {
				subWrapper.setRsrpList(ReportUtil.setGraphDataForKpi(kpiWrapper,
						ReportUtil.convetArrayToList(arlist, kpiWrapper.getIndexKPI())));
			} else if (ReportConstants.SINR.equalsIgnoreCase(kpiWrapper.getKpiName())) {
				subWrapper.setSinrList(ReportUtil.setGraphDataForKpi(kpiWrapper,
						ReportUtil.convetArrayToList(arlist, kpiWrapper.getIndexKPI())));
			} else if (ReportConstants.DL.equalsIgnoreCase(kpiWrapper.getKpiName())) {
				subWrapper.setDlList(ReportUtil.setGraphDataForKpi(kpiWrapper,
						ReportUtil.convetArrayToList(arlist, kpiWrapper.getIndexKPI())));

			} else if (ReportConstants.UL.equalsIgnoreCase(kpiWrapper.getKpiName())) {
				subWrapper.setUlList(ReportUtil.setGraphDataForKpi(kpiWrapper,
						ReportUtil.convetArrayToList(arlist, kpiWrapper.getIndexKPI())));

			} else if (ReportConstants.FTP_DL_RSRP.equalsIgnoreCase(kpiWrapper.getKpiName())) {
				subWrapper.setFtpRsrpDlList(
						ReportUtil.setGraphDataForKpi(kpiWrapper, ReportUtil.convetArrayToListForCustomKpi(arlist,
								DriveHeaderConstants.INDEX_RSRP, ReportConstants.FTP_DOWNLOAD, kpiIndexMap.get(ReportConstants.TEST_TYPE))));

			} else if (ReportConstants.HTTP_DL_RSRP.equalsIgnoreCase(kpiWrapper.getKpiName())) {
				subWrapper.setHttpRsrpDlList(
						ReportUtil.setGraphDataForKpi(kpiWrapper, ReportUtil.convetArrayToListForCustomKpi(arlist,
								DriveHeaderConstants.INDEX_RSRP, ReportConstants.HTTP_DOWNLOAD, kpiIndexMap.get(ReportConstants.TEST_TYPE))));

			} else if (ReportConstants.FTP_DL_SINR.equalsIgnoreCase(kpiWrapper.getKpiName())) {
				subWrapper.setFtpSinrDlList(
						ReportUtil.setGraphDataForKpi(kpiWrapper, ReportUtil.convetArrayToListForCustomKpi(arlist,
								DriveHeaderConstants.INDEX_SINR, ReportConstants.FTP_DOWNLOAD, kpiIndexMap.get(ReportConstants.TEST_TYPE))));

			} else if (ReportConstants.HTTP_DL_SINR.equalsIgnoreCase(kpiWrapper.getKpiName())) {
				subWrapper.setHttpSinrDlList(
						ReportUtil.setGraphDataForKpi(kpiWrapper, ReportUtil.convetArrayToListForCustomKpi(arlist,
								DriveHeaderConstants.INDEX_SINR, ReportConstants.HTTP_DOWNLOAD, kpiIndexMap.get(ReportConstants.TEST_TYPE))));

			}

			else if (ReportConstants.FTP_UL_RSRP.equalsIgnoreCase(kpiWrapper.getKpiName())) {
				subWrapper.setFtpRsrpUlList(
						ReportUtil.setGraphDataForKpi(kpiWrapper, ReportUtil.convetArrayToListForCustomKpi(arlist,
								DriveHeaderConstants.INDEX_RSRP, ReportConstants.FTP_UPLOAD, kpiIndexMap.get(ReportConstants.TEST_TYPE))));

			} else if (ReportConstants.HTTP_UL_RSRP.equalsIgnoreCase(kpiWrapper.getKpiName())) {
				subWrapper.setHttpRsrpUlList(
						ReportUtil.setGraphDataForKpi(kpiWrapper, ReportUtil.convetArrayToListForCustomKpi(arlist,
								DriveHeaderConstants.INDEX_RSRP, ReportConstants.HTTP_UPLOAD, kpiIndexMap.get(ReportConstants.TEST_TYPE))));

			} else if (ReportConstants.FTP_UL_SINR.equalsIgnoreCase(kpiWrapper.getKpiName())) {
				subWrapper.setFtpSinrUlList(
						ReportUtil.setGraphDataForKpi(kpiWrapper, ReportUtil.convetArrayToListForCustomKpi(arlist,
								DriveHeaderConstants.INDEX_SINR, ReportConstants.FTP_UPLOAD, kpiIndexMap.get(ReportConstants.TEST_TYPE))));

			} else if (ReportConstants.HTTP_UL_SINR.equalsIgnoreCase(kpiWrapper.getKpiName())) {
				subWrapper.setHttpSinrUlList(
						ReportUtil.setGraphDataForKpi(kpiWrapper, ReportUtil.convetArrayToListForCustomKpi(arlist,
								DriveHeaderConstants.INDEX_SINR, ReportConstants.HTTP_UPLOAD, kpiIndexMap.get(ReportConstants.TEST_TYPE))));

			}

		} catch (Exception e) {
			logger.error("Exception inside populateGraphDataForkpi {}", Utils.getStackTrace(e));
		}
		return subWrapper;
	}

	private Map<String, Object> getSectorWiseImageFromList(List<DriveDataWrapper> imageDataList) {
		Map<String, Object> sectorImageMap = new HashMap<>();
		logger.info("inside method getSectorWiseImageFromList {} ", imageDataList.size());
		Integer i = 1;
		for (DriveDataWrapper driveData : imageDataList) {
			try {
				InputStream in = new ByteArrayInputStream(driveData.getImg());
				BufferedImage buffredImage = ImageIO.read(in);
				ImageIO.write(buffredImage, ReportConstants.JPG,
						new File(ConfigUtils.getString(ReportConstants.SSVT_REPORT_PATH) + ReportConstants.SECTOR + i
								+ ReportConstants.DOT_JPG));
				sectorImageMap.put(ReportConstants.SECTOR + i, ConfigUtils.getString(ReportConstants.SSVT_REPORT_PATH)
						+ ReportConstants.SECTOR + i + ReportConstants.DOT_JPG);
				i++;
			} catch (Exception e) {
				logger.warn("Inside method getSectorWiseImageFromList {}", e.getMessage());
			}
		}
		logger.debug("sectorImageMap data {}", sectorImageMap);
		return sectorImageMap;
	}

	@Override
	public String getSummaryData(Integer workorderId, Map<String, List<String>> map) {
		try {
			logger.info("WORKORDER_ID {} , RECIPE LIST {} , OPERATOR LIST {} ", workorderId,
					map.get(QMDLConstant.RECIPE), map.get(QMDLConstant.OPERATOR));
			return reportService.getSummaryData(workorderId, map);
		} catch (Exception e) {
			logger.error("Error inside the method getSummaryData {}", Utils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public String getDriveData(Integer workorderId, Map<String, List<String>> map) {
		String combineData = null;
		try {

			combineData = nvLayer3DashboardService.getDriveDetailReceipeWise(workorderId, map.get(QMDLConstant.RECIPE),
					map.get(QMDLConstant.OPERATOR));
			Map<String, String> dataMap = ReportUtil.convertCSVStringToMap(combineData);
			return dataMap.get(ReportConstants.RESULT);
		} catch (Exception e) {
			logger.error("Exception in method getDriveData {} ", Utils.getStackTrace(e));
		}
		return combineData;
	}

	@Override
	public List<SiteInformationWrapper> getFirstTierNieghbourSites(List<String> neNameCellIdList) {
		List<SiteInformationWrapper> siteInfoList = new ArrayList<>();
		Map<String, List<String>> cellWiseNeighbourSiteMap = getCellWiseNeighbourSites(neNameCellIdList);
		logger.info("cellWiseNeighbourSiteMap Data {} ", cellWiseNeighbourSiteMap);
		cellWiseNeighbourSiteMap.forEach((key, value) -> {
			try {
				siteInfoList.addAll(siteDetailService.getMacroSiteDetailsForCellLevelForReport(null, null, value, null,
						true, true, true));
			} catch (Exception e) {
				logger.error("Exception in calculating the first tier Neighbour {} ", Utils.getStackTrace(e));
			}
		});
		return siteInfoList;
	}

	private Map<String, List<String>> getCellWiseNeighbourSites(List<String> neNameCellIdList) {
		Map<String, List<String>> cellWiseNeighbourSiteMap = new HashMap<>();// need to change and use the api that will
																				// be developed by sudeep sir
		Map<String, List<NeighbourCellDetail>> cellWiseNeighborInfoMap = neighbourCellDetailService
				.getNeighbourCellDetailsForSourceCells(neNameCellIdList, getAutoCellWeekNo());
		if (cellWiseNeighborInfoMap != null && cellWiseNeighborInfoMap.size() > ReportConstants.INDEX_ZER0) {
			cellWiseNeighborInfoMap.forEach((key, value) -> cellWiseNeighbourSiteMap.put(key,
					value.stream().map(NeighbourCellDetail::getSapid1).collect(Collectors.toList())));
		}
		return cellWiseNeighbourSiteMap;
	}

	private Integer getAutoCellWeekNo() {
		Integer weekNo = null;
		try {
			List<SystemConfiguration> listOfSystemConfig = iSystemConfigurationDao
					.getSystemConfigurationByName(ReportConstants.AUTO_CELL_RANGE_MAX_WEEK);
			logger.info("listOfSystemConfig {} ", listOfSystemConfig);
			if (listOfSystemConfig != null && !listOfSystemConfig.isEmpty()) {
				for (SystemConfiguration systemConfigObj : listOfSystemConfig) {
					if (systemConfigObj.getValue() != null) {
						weekNo = Integer.parseInt(systemConfigObj.getValue());
						logger.info("Going to return the Auto cell Range week no {} ", weekNo);
						return weekNo;
					}
				}
			}
		} catch (NoResultException e) {
			logger.error("NoResultException occured inside method Auto cell week no calculation {} ",
					Utils.getStackTrace(e));
		} catch (Exception e) {
			logger.error("Exception occured inside method Auto cell week no calculation {} ", Utils.getStackTrace(e));
		}
		return weekNo;
	}

	// enodebID-cellId - (ecgi - MCC-MNC-CGI)

	private HashMap<String, String> getImagesForReport(List<KPIWrapper> kpiList, DriveImageWrapper driveImageWrapper,
			List<DriveDataWrapper> imageDataList, Map<String, Integer> kpiIndexMap) throws IOException {
		List<Double[]> pinLatLonList = imageDataList.stream().map(
				driveDataWrapper -> new Double[] { driveDataWrapper.getLongitude(), driveDataWrapper.getLatitutde() })
				.collect(Collectors.toList());
		// kpiList = reportService.modifyIndexOfCustomKpis(kpiList);

		HashMap<String, BufferedImage> bufferdImageMap = mapImageService.getLegendImagesForReport(kpiList,
				driveImageWrapper.getDataKPIs(),kpiIndexMap.get(ReportConstants.TEST_TYPE));
		bufferdImageMap.putAll(mapImageService.getDriveImagesForReport(driveImageWrapper, pinLatLonList, kpiIndexMap));
		HashMap<String, String> imagemap = mapImageService.saveDriveImages(bufferdImageMap,
				ConfigUtils.getString(ReportConstants.IMAGE_PATH_FOR_NV_REPORT) + ReportConstants.SSVT
						+ ReportConstants.FORWARD_SLASH
						+ ReportUtil.getFormattedDate(new Date(), ReportConstants.DATE_FORMAT_DD_MM_YY_HH_SS)
						+ ReportConstants.FORWARD_SLASH,
				false);

		HashMap<String, String> atollImageFinalMap = getCoveragePredictionImage(driveImageWrapper);
		logger.info("atollImageFinalMap {} ", atollImageFinalMap != null ? new Gson().toJson(atollImageFinalMap) : atollImageFinalMap);
		imagemap.putAll(atollImageFinalMap);
		return imagemap;
	}

	@Override
	public HashMap<String, String> getCoveragePredictionImage(DriveImageWrapper driveImageWrapper) {
		HashMap<String, BufferedImage> atollImageMap = getAtollImagesMap(driveImageWrapper);
		return mapImageService.saveDriveImages(atollImageMap,
				ConfigUtils.getString(ReportConstants.IMAGE_PATH_FOR_NV_REPORT) + ReportConstants.SSVT
						+ ReportConstants.FORWARD_SLASH + ReportConstants.ATOLL.toUpperCase()
						+ ReportConstants.FORWARD_SLASH
						+ ReportUtil.getFormattedDate(new Date(), ReportConstants.DATE_FORMAT_DD_MM_YY_HH_SS)
						+ ReportConstants.FORWARD_SLASH,
				true);

	}

	private HashMap<String, BufferedImage> getAtollImagesMap(DriveImageWrapper driveImageWrapper) {
		HashMap<String, BufferedImage> atollIMagesMap = new HashMap<>();
		try {
			if (driveImageWrapper != null) {
				List<KPIWrapper> filterList = driveImageWrapper.getKpiWrappers().stream()
						.filter(x -> x.getKpiName().equalsIgnoreCase(ReportConstants.RSRP))
						.collect(Collectors.toList());
				driveImageWrapper.setKpiWrappers(filterList);
				HashMap<String, BufferedImage> atollImageMap = mapImageService
						.getAtollPredictionImages(driveImageWrapper);
				logger.info("atollImageMap Data {} ", atollImageMap.size());
				HashMap<String, BufferedImage> atollLegendImageMap = mapImageService
						.getAtollLegendImages(driveImageWrapper.getKpiWrappers());
				logger.info("atollLegendImageMap Data {} ",
						atollLegendImageMap != null ? atollLegendImageMap.size() : null);
				atollImageMap.forEach((key, value) -> atollIMagesMap
						.put(ReportConstants.ATOLL + ReportConstants.UNDERSCORE + key, value));
				atollIMagesMap.putAll(atollLegendImageMap);
			}
		} catch (Exception e) {
			logger.debug("Exception inside method getAtollImagesMap {} ", Utils.getStackTrace(e));
		}
		return atollIMagesMap;
	}

	/**
	 * Sets the summary data.
	 *
	 * @param dataKPIs         the data KP is
	 * @param listOfKPidata    the list of K pidata
	 * @param reportType
	 * @param dlSpeedtestList
	 * @param map
	 * @param reportDataHolder
	 * @return the KPI summary data from csv data
	 */

	@Override
	public List<KPISummaryDataWrapper> getKPISummaryDataForWorkOrderId(Integer workOrderID, List<String[]> dataKPIs,
			List<KPIWrapper> listOfKPidata, String[] summaryData, String reportType, String operator,
			List<Double> dlSpeedtestList, Map<String, List<String>> map, ReportDataHolder reportDataHolder) {
		logger.info("Inside method getKPISummaryDataForWorkOrderId  for id {}  ", workOrderID);
		List<Object[]> objectList = iGenericWorkorderDao.getNVReportConfiguration(reportType);
		List<NVReportConfigurationWrapper> nvWrapperList = ReportUtil.getNVReportconfiguratioinList(objectList);
		List<KPISummaryDataWrapper> listofSummaryData = getKpiDataForSummary(dataKPIs, listOfKPidata, nvWrapperList);
		listofSummaryData.addAll(getSummaryDataListOfSamples(nvWrapperList, reportDataHolder));
		if ((reportType.equalsIgnoreCase(ReportConstants.SSVTQUICK)
				|| reportType.equalsIgnoreCase(ReportConstants.SSVTFULL)) && map != null) {
			listofSummaryData.addAll(getDlPeakValuesBandWise(workOrderID, map));
		}
		try {
			List<String> summaryFeildArrayList = getSummaryFieldKeysList(reportType);
			for (String key : summaryFeildArrayList) {
				List<NVReportConfigurationWrapper> filterList = nvWrapperList.stream()
						.filter(wrapper -> wrapper.getKpi().equalsIgnoreCase(key)).collect(Collectors.toList());
				listofSummaryData.add(ReportUtil.getKPiSummaryDataWrapperForKey(key, summaryData, filterList,
						dlSpeedtestList, reportDataHolder));
			}
		} catch (Exception e) {
			logger.error("Error during Casting of Summary data {} ", Utils.getStackTrace(e));
		}
		logger.info("Going to return the KPi Summary data of list Size {} ", listofSummaryData.size());
		return listofSummaryData;
	}

	public List<KPISummaryDataWrapper> getKPISummaryDataForAutomationReport(Integer workOrderID,
			List<KPIWrapper> listOfKPidata, Map<String, String[]> recipeWiseSummaryMap, String reportType,
			List<Double> dlSpeedtestList, Map<String, List<String>> map, String[] summaryData,
			ReportDataHolder reportDataHolder) {
		logger.info("Inside method getKPISummaryDataForWorkOrderId  for id {}  ", workOrderID);
		List<Object[]> objectList = iGenericWorkorderDao.getNVReportConfiguration(reportType);
		List<NVReportConfigurationWrapper> nvWrapperList = ReportUtil.getNVReportconfiguratioinList(objectList);
		List<KPISummaryDataWrapper> listofSummaryData = getKpiDataForSummary(reportDataHolder.getDataList(),
				listOfKPidata, nvWrapperList);
		listofSummaryData.addAll(getSummaryDataListOfSamples(nvWrapperList, reportDataHolder));
		if (map != null) {
			GenericWorkorder workOrderObj = iGenericWorkorderDao.findByPk(workOrderID);
			if (workOrderObj != null && workOrderObj.getGwoMeta() != null
					&& "2300".equalsIgnoreCase(workOrderObj.getGwoMeta().get("band"))) {
				setPeakDlValue(reportDataHolder, listofSummaryData, workOrderObj);
			} else if (workOrderObj != null && TemplateType.NV_SSVT_QUICK.equals(workOrderObj.getTemplateType())) {
				setPeakDlValue(reportDataHolder, listofSummaryData, workOrderObj);
			}

			else {
				// listofSummaryData.addAll(getDlPeakValuesBandWise(workOrderID, map));
				listofSummaryData.addAll(getDlPeakValuesBandWise(reportDataHolder));

			}
		}
		try {
			List<String> summaryFeildArrayList = getSummaryFieldKeysList(reportType);
			for (String key : summaryFeildArrayList) {
				List<NVReportConfigurationWrapper> filterList = nvWrapperList.stream()
						.filter(wrapper -> wrapper.getKpi().equalsIgnoreCase(key)).collect(Collectors.toList());
				listofSummaryData.add(ReportUtil.getKPiSummaryDataWrapperForSVVTAutomationReport(key, summaryData,
						filterList, dlSpeedtestList, recipeWiseSummaryMap, reportDataHolder));
			}
		} catch (Exception e) {
			logger.error("Error during Casting of Summary data {} ", Utils.getStackTrace(e));
		}
		logger.info("Going to return the KPi Summary data of list Size {} ", listofSummaryData.size());
		return listofSummaryData;
	}

	public List<KPISummaryDataWrapper> getDlPeakValuesBandWise(ReportDataHolder reportDataHolder) {
		List<KPISummaryDataWrapper> kpiSmmryList = new ArrayList<>();

		try {
			if (reportDataHolder != null) {
				Map<String, Double> peakvalueData = getPeakValueData(reportDataHolder);
				logger.info("peakvalueData {} ", peakvalueData);
				if (peakvalueData != null && peakvalueData.size() > 0) {
					for (Entry<String, Double> peakDLvalueMap : peakvalueData.entrySet()) {
						kpiSmmryList.add(getPeakValueRow(ReportConstants.PEAK_VALUE_SINGLE_SAMPLE,
								ReportConstants.DT_MOBILE, peakDLvalueMap));
					}
				}
			}
		} catch (Exception e) {
			logger.error("Exception inside methdo getDlPeakValuesBandWise {} ", Utils.getStackTrace(e));
		}
		return kpiSmmryList;
	}

	private Map<String, Double> getPeakValueData(ReportDataHolder reportDataHolder) {
		Map<String, Double> peakvalueData = new HashMap<>();
		List<String[]> dataList = reportDataHolder.getDataList();
		dataList=ReportUtil.addKPIInEmptyFields(dataList, reportDataHolder.getKpiIndexMap().get(ReportConstants.DL_BANWIDTH));
		for (String[] ar : dataList) {
			try {
				if (ReportUtil.checkValidString(reportDataHolder.getKpiIndexMap().get(ReportConstants.DL_BANWIDTH), ar)
						&& ReportUtil.checkIndexValue(
								reportDataHolder.getKpiIndexMap().get(ReportConstants.DL_THROUGHPUT), ar)) {
					if (peakvalueData
							.containsKey(ar[reportDataHolder.getKpiIndexMap().get(ReportConstants.DL_BANWIDTH)])) {
						Double value = peakvalueData
								.get(ar[reportDataHolder.getKpiIndexMap().get(ReportConstants.DL_BANWIDTH)]);
						if (NumberUtils
								.isParsable(ar[reportDataHolder.getKpiIndexMap().get(ReportConstants.DL_THROUGHPUT)])) {
							Double dl = Double.parseDouble(
									ar[reportDataHolder.getKpiIndexMap().get(ReportConstants.DL_THROUGHPUT)]);
							if (value < dl) {
								peakvalueData.put(
										ar[reportDataHolder.getKpiIndexMap().get(ReportConstants.DL_BANWIDTH)], dl);
							}
						}
					} else {
						if (NumberUtils
								.isParsable(ar[reportDataHolder.getKpiIndexMap().get(ReportConstants.DL_THROUGHPUT)])) {
							Double dl = Double.parseDouble(
									ar[reportDataHolder.getKpiIndexMap().get(ReportConstants.DL_THROUGHPUT)]);
							peakvalueData.put(ar[reportDataHolder.getKpiIndexMap().get(ReportConstants.DL_BANWIDTH)],
									dl);
						}

					}

				}
			} catch (NumberFormatException e) {
				logger.error("getPeakValueData {}", Utils.getStackTrace(e));
			} catch (Exception e) {
				logger.error("Exception getPeakValueData {}", Utils.getStackTrace(e));
			}

		}
		logger.info("inside the ,mehtod ");
		return peakvalueData;
	}

	public List<KPISummaryDataWrapper> getDlPeakValuesBandWise(Integer workorderId, Map<String, List<String>> map) {
		List<KPISummaryDataWrapper> kpiSmmryList = new ArrayList<>();
		try {
			if (map != null) {
				Map<String, Double> peakvalueData = getPeakValueData(workorderId, map);
				logger.info("peakvalueData {} ", peakvalueData);
				if (peakvalueData != null && peakvalueData.size() > 0) {
					for (Entry<String, Double> peakDLvalueMap : peakvalueData.entrySet()) {
						kpiSmmryList.add(getPeakValueRow(ReportConstants.PEAK_VALUE_SINGLE_SAMPLE,
								ReportConstants.DT_MOBILE, peakDLvalueMap));
					}
				}
			}
		} catch (BusinessException e) {
			logger.error("Exception inside methdo getDlPeakValuesBandWise {} ", Utils.getStackTrace(e));
		}
		return kpiSmmryList;
	}

	private KPISummaryDataWrapper getPeakValueRow(String testName, String source,
			Entry<String, Double> peakDLvalueMap) {
		KPISummaryDataWrapper wrapper = new KPISummaryDataWrapper();
		try {
			wrapper.setTestName(testName);
			wrapper.setSource(source);
			String key = peakDLvalueMap.getKey().replace(ReportConstants.SPACE, ReportConstants.BLANK_STRING);
			wrapper.setTarget(key.replace(ReportConstants.COMMA, ReportConstants.SPACE));
			if (peakDLvalueMap.getValue() != null) {
				wrapper.setAchived(ReportUtil.parseToFixedDecimalPlace(peakDLvalueMap.getValue(), ForesightConstants.INDEX_TWO) + ReportConstants.SPACE + ReportConstants.MBPS);
				if (peakDLvalueMap.getKey().contains("5")) {
					wrapper.setStatus((peakDLvalueMap.getValue() > 27.0) ? ReportConstants.PASS : ReportConstants.FAIL);
					wrapper.setTarget(wrapper.getTarget() + ">27 Mbps");
				} else if (peakDLvalueMap.getKey().contains("10")) {
					wrapper.setStatus((peakDLvalueMap.getValue() > 50.0) ? ReportConstants.PASS : ReportConstants.FAIL);
					wrapper.setTarget(wrapper.getTarget() + ">50 Mbps");
				} else if (peakDLvalueMap.getKey().contains("20")) {
					wrapper.setStatus((peakDLvalueMap.getValue() > 75.0) ? ReportConstants.PASS : ReportConstants.FAIL);
					wrapper.setTarget(wrapper.getTarget() + ">75 Mbps");
				}
			}
		} catch (Exception e) {
			logger.info("Exception inside method getPeakValueRow {} ", e.getMessage());
		}
		return wrapper;
	}

	private List<String> getSummaryFieldKeysList(String reportType) {
		List<String> summaryFeildArrayList = new ArrayList<>(
				Arrays.asList(ReportConstants.ERAB_DROP_RATE, ReportConstants.HANDOVER_SUCCESS_RATE,
						ReportConstants.VOLTE_SETUP_SUCCESS_RATE, ReportConstants.VOLTE_ERAB_DROP_RATE));
		if (reportType.equalsIgnoreCase(ReportConstants.SSVT) || reportType.equalsIgnoreCase(ReportConstants.NVSSVT)
				|| reportType.equalsIgnoreCase(ReportConstants.SSVTQUICK)
				|| reportType.equalsIgnoreCase(ReportConstants.SSVTFULL)) {
			summaryFeildArrayList.add(ReportConstants.NETVELOCITY_SPEED_TEST);
			summaryFeildArrayList.add(ReportConstants.RRC_CONNECTION_SUCCESS_RATE);
		} else {
			summaryFeildArrayList.add(ReportConstants.RRC_CONNECTION_REQUEST_SUCCESS);
			summaryFeildArrayList.add(ReportConstants.AVERAGE_HANDOVER_INTERRUPTION_TIME);
			summaryFeildArrayList.add(ReportConstants.AVERAGE_SINR);
			summaryFeildArrayList.add(ReportConstants.VOLTE_VOICE_MEAN_OPINION_SOURCE);
			summaryFeildArrayList.add(ReportConstants.VOLTE_CALL_DROP_RATE);
		}
		return summaryFeildArrayList;
	}

	private List<KPISummaryDataWrapper> getSummaryDataListOfSamples(List<NVReportConfigurationWrapper> nvWrapperList,
			ReportDataHolder reportDataHolder) {
		List<KPISummaryDataWrapper> kpiSummaryList = new ArrayList<>();
		for (NVReportConfigurationWrapper wrapper : nvWrapperList) {
			if (wrapper.getKpi().contains("CASE") || wrapper.getKpi().contains("CASE1")) {
				kpiSummaryList.add(getsummaryDataforKpi(wrapper, reportDataHolder));
			}
		}
		logger.info("kpiSummaryList Size {}  , data {} ", kpiSummaryList.size(), kpiSummaryList.toString());
		return kpiSummaryList;
	}

	private KPISummaryDataWrapper getsummaryDataforKpi(NVReportConfigurationWrapper wrapper,
			ReportDataHolder reportDataHolder) {
		logger.info("Inside method  getsummaryDataforKpi for kpi {} ", wrapper.getConfiguration());
		String[] kpiPart = wrapper.getConfiguration().split(ReportConstants.SPACE);
		String tech = kpiPart[3];
		String dlbandwidth = kpiPart[4];
		String ulOrdl = kpiPart[6].replace(ReportConstants.OPEN_BRACKET, ReportConstants.BLANK_STRING)
				.replace(ReportConstants.CLOSED_BRACKET, ReportConstants.BLANK_STRING);
		logger.info(" tech {} , dlbandwidth {} , ulOrdl {} ", tech, dlbandwidth, ulOrdl);
		switch (ulOrdl) {
		case "DL":
			return getSummaryDataforDLUL(reportDataHolder.getDataList(), wrapper, tech, dlbandwidth,
					reportDataHolder.getKpiIndexMap().get(ReportConstants.DL_BANWIDTH),
					reportDataHolder.getKpiIndexMap().get(ReportConstants.DL_THROUGHPUT), kpiPart[0]);
		case "UL":
			return getSummaryDataforDLUL(reportDataHolder.getDataList(), wrapper, tech, dlbandwidth,
					reportDataHolder.getKpiIndexMap().get(ReportConstants.UL_BANWIDTH),
					reportDataHolder.getKpiIndexMap().get(ReportConstants.UL_THROUGHPUT), kpiPart[0]);
		default:
			return null;
		}
	}

	private KPISummaryDataWrapper getSummaryDataforDLUL(List<String[]> dataKPIs, NVReportConfigurationWrapper wrapper,
			String tech, String dlbandwidth, Integer bandWidthIndex, Integer indexOfkpi, String criteria) {
		KPISummaryDataWrapper kpiWrapper = new KPISummaryDataWrapper();
		double count = 0.0;
		double totalCount = 0.0;
		try {
			for (String[] data : dataKPIs) {
				if (data != null && data.length > DriveHeaderConstants.TECHNOLOGY_INDEX
						&& data[DriveHeaderConstants.TECHNOLOGY_INDEX] != null && data[bandWidthIndex] != null
						&& data[DriveHeaderConstants.TECHNOLOGY_INDEX].equalsIgnoreCase(tech)
						&& data[bandWidthIndex].replace(" ", ReportConstants.BLANK_STRING)
								.replace("MHz", ReportConstants.BLANK_STRING).equalsIgnoreCase(dlbandwidth)) {
					logger.info(
							"TEMP LOG with indexOfkpi {} ,data[indexOfkpi] {} , tech {} ,bandwidth {} , {} , {} , {} ",
							indexOfkpi, data[indexOfkpi], tech, dlbandwidth,
							data[DriveHeaderConstants.TECHNOLOGY_INDEX], data[bandWidthIndex],
							data[bandWidthIndex].replace(" ", ReportConstants.BLANK_STRING).replace("MHz",
									ReportConstants.BLANK_STRING));
					if (data[indexOfkpi] != null && !data[indexOfkpi].isEmpty() && data.length > indexOfkpi) {
						totalCount++;
						if (NumberUtils.toDouble(data[indexOfkpi]) > NumberUtils.toDouble(wrapper.getTargetvalue())) {
							count++;
						}
					}
				}
			}
			if (count != 0.0 && totalCount != 0.0) {
				Double achivedValue = (count * 100) / (totalCount);
				kpiWrapper.setAchived(String.valueOf(ReportUtil.parseToFixedDecimalPlace(achivedValue, 2)));
			}
			if (kpiWrapper.getAchived() != null && !kpiWrapper.getAchived().isEmpty()) {
				if (criteria.contains("<")) {
					String compValue = StringUtils.substringBetween(criteria, "<", "%");
					kpiWrapper.setStatus((Double.parseDouble(kpiWrapper.getAchived()) < Double.parseDouble(compValue))
							? ReportConstants.PASS
							: ReportConstants.FAIL);
				} else if (criteria.contains(">")) {
					String compValue = StringUtils.substringBetween(criteria, ">", "%");
					kpiWrapper.setStatus((Double.parseDouble(kpiWrapper.getAchived()) > Double.parseDouble(compValue))
							? ReportConstants.PASS
							: ReportConstants.FAIL);
				}
				kpiWrapper.setAchived(kpiWrapper.getAchived() + ReportConstants.PERCENT);
			}
		} catch (Exception e) {
			logger.error("Exception insdie method getSummaryDataforKPi {} , {}  ", wrapper.getConfiguration(),
					Utils.getStackTrace(e));
		}
		kpiWrapper.setItem(ReportConstants.INTEGRITY);
		kpiWrapper.setTestName(wrapper.getConfiguration());
		kpiWrapper.setTarget((wrapper.getKpi().equalsIgnoreCase("CASE") ? ("<" + wrapper.getTargetvalue())
				: (">" + wrapper.getTargetvalue())) + ReportConstants.MBPS);
		logger.info("Going to retun kpi wrapper for kpi {} , {} ", wrapper.getConfiguration(), kpiWrapper.toString());
		return kpiWrapper;
	}

	/**
	 * Gets the Summary data for Kpi
	 *
	 * @param dataKPIs      the data KP is
	 * @param listOfKPidata the list of K pidata
	 * @param nvWrapperList
	 * @return the kpi data for summary
	 */
	private List<KPISummaryDataWrapper> getKpiDataForSummary(List<String[]> dataKPIs, List<KPIWrapper> listOfKPidata,
			List<NVReportConfigurationWrapper> nvWrapperList) {
		List<KPISummaryDataWrapper> listofSummaryData = new ArrayList<>();
		for (KPIWrapper kpiwrapper : listOfKPidata) {

			try {
				List<NVReportConfigurationWrapper> filterList = nvWrapperList.stream()
						.filter(wrapper -> wrapper.getKpi().equalsIgnoreCase(kpiwrapper.getKpiName()))
						.collect(Collectors.toList());
				if (filterList != null && !filterList.isEmpty()) {
					kpiwrapper.setTargetValue(Double.parseDouble(filterList.get(0).getTargetvalue()));
				}
				if (kpiwrapper.getTargetValue() != null) {
					List<String[]> tempList = dataKPIs.stream()
							.filter(dataArray -> (kpiwrapper.getIndexKPI() != null
									&& (dataArray[kpiwrapper.getIndexKPI()] != null)
									&& !dataArray[kpiwrapper.getIndexKPI()].isEmpty()))
							.collect(Collectors.toList());
					logger.info("tempList Size {} , for kpi {} ", tempList != null ? tempList.size() : null,
							kpiwrapper.getIndexKPI());
					if (tempList != null && tempList.size() > ForesightConstants.ZERO) {
						KPISummaryDataWrapper wrapper = getKpiDataSummaryWrapper(kpiwrapper, tempList);
						listofSummaryData.add(wrapper);
					}
				}
			} catch (NumberFormatException e) {
				logger.error("NumberFormatException occured inside method getKpiDataForSummary {} ",
						Utils.getStackTrace(e));
			} catch (Exception e) {
				logger.error("Exception inside method getKpiDataForSummary {} ", Utils.getStackTrace(e));
			}
		}
		return listofSummaryData;
	}

	private KPISummaryDataWrapper getKpiDataSummaryWrapper(KPIWrapper kpiwrapper, List<String[]> tempList) {
		KPISummaryDataWrapper wrapper = new KPISummaryDataWrapper();

		List<Double> list = ReportUtil.convetArrayToList(tempList, kpiwrapper.getIndexKPI());
		long count = list.stream().filter(value -> value > kpiwrapper.getTargetValue()).count();

		Double achivedValue = (count * ReportConstants.HUNDRED) / list.size();
		achivedValue = ReportUtil.parseToFixedDecimalPlace(achivedValue, ReportConstants.INDEX_TWO);

		if (kpiwrapper.getKpiName().equalsIgnoreCase(ReportConstants.DL)
				|| kpiwrapper.getKpiName().equalsIgnoreCase(ReportConstants.UL)) {
			wrapper.setTestName(kpiwrapper.getKpiName().replaceAll(ReportConstants.UNDERSCORE, ReportConstants.SPACE)
					+ ReportConstants.GREATER_THAN + kpiwrapper.getTargetValue() + ReportConstants.SPACE
					+ ReportConstants.MBPS + ForesightConstants.SPACE + ReportConstants.OPEN_BRACKET
					+ ReportConstants.PERCENT + ReportConstants.CLOSED_BRACKET);
		} else {
			wrapper.setTestName(kpiwrapper.getKpiName().replaceAll(ReportConstants.UNDERSCORE, ReportConstants.SPACE)
					+ ReportConstants.GREATER_THAN + kpiwrapper.getTargetValue() + ForesightConstants.SPACE
					+ ReportConstants.OPEN_BRACKET + ReportConstants.PERCENT + ReportConstants.CLOSED_BRACKET);
		}
		wrapper.setAchived(achivedValue != null ? achivedValue.toString() + ReportConstants.PERCENT : null);
		wrapper.setTarget(ReportConstants.N_SLASH_A);
		wrapper.setStatus(ReportConstants.N_SLASH_A);
		wrapper.setSource(ReportConstants.DT_MOBILE.replace(ReportConstants.UNDERSCORE, ReportConstants.SPACE));
		return wrapper;
	}

	/**
	 * Sets the summary data.
	 *
	 * @param subWrapper   the sub wrapper
	 * @param mainWrapper
	 * @param csvDataArray
	 * @param summaryMap   the drive data
	 */
	private void setSummaryData(SSVTReportSubWrapper subWrapper, SSVTReportWrapper mainWrapper,
			List<String[]> csvDataArray, Map<String, Integer> kpiIndexMap) {
		try {

			mainWrapper.setTechnology(InbuildingReportUtil.getDistinctKpiValues(csvDataArray,
					kpiIndexMap.get(ReportConstants.TECHNOLOGY)));

			subWrapper.setAvgRsrp(ReportUtil.parseToFixedDecimalPlace(
					ReportUtil.convetArrayToList(csvDataArray, kpiIndexMap.get(ReportConstants.RSRP)).stream()
							.mapToDouble(x -> x).average().getAsDouble(),
					2));
			subWrapper.setAvgSinr(ReportUtil.parseToFixedDecimalPlace(
					ReportUtil.convetArrayToList(csvDataArray, kpiIndexMap.get(ReportConstants.SINR)).stream()
							.mapToDouble(x -> x).average().getAsDouble(),
					2));
			subWrapper.setAvgDl(ReportUtil.parseToFixedDecimalPlace(
					ReportUtil.convetArrayToList(csvDataArray, kpiIndexMap.get(ReportConstants.DL_THROUGHPUT)).stream()
							.mapToDouble(x -> x).average().getAsDouble(),
					2));
			subWrapper.setAvgUl(ReportUtil.parseToFixedDecimalPlace(
					ReportUtil.convetArrayToList(csvDataArray, kpiIndexMap.get(ReportConstants.UL_THROUGHPUT)).stream()
							.mapToDouble(x -> x).average().getAsDouble(),
					2));

		} catch (Exception e) {
			logger.error("Exception inside the method setSummaryData{}", e.getMessage());
		}
	}

	/**
	 * Sets the kpi states intokpi list.
	 *
	 * @param kpiList     the kpi list
	 * @param workOrderId the work order id
	 * @param list3
	 * @param list2
	 * @return the list
	 */
	@Override
	public List<KPIWrapper> goingToSetKpiStats(List<KPIWrapper> kpiList, Integer workOrderId, List<String> recepiList,
			List<String> operatorList) {
		List<KPIWrapper> list = new ArrayList<>();
		kpiList.forEach(kpiWrapper -> {
			kpiWrapper.setKpiStats(getKPiStatsDataFromHbase(workOrderId,
					ReportUtil.getHbaseColumnNameByKpiName(kpiWrapper), recepiList, operatorList));
			list.add(kpiWrapper);
		});
		return list;
	}

	/**
	 * Prepare image map.
	 *
	 * @param imagemap the imagemap
	 * @param kpiIndexMap 
	 * @return the hash map
	 */
	private HashMap<String, Object> prepareImageMap(HashMap<String, String> imagemap, Map<String, Integer> kpiIndexMap) {
		logger.info("inside the method prepareImageMap");
		Map<String, Object> map = new HashMap<>();
		try {
			if(kpiIndexMap.containsKey(ReportConstants.RSRP)) {
			map.put(ReportConstants.IMAGE_RSRP, imagemap.get(kpiIndexMap.get(ReportConstants.RSRP).toString()));
			map.put(ReportConstants.IMAGE_RSRP_LEGEND, imagemap
					.get(ReportConstants.LEGEND + ReportConstants.UNDERSCORE +kpiIndexMap.get(ReportConstants.RSRP)));
			}
			if(kpiIndexMap.containsKey(ReportConstants.SINR)) {

			map.put(ReportConstants.IMAGE_SINR, imagemap.get(kpiIndexMap.get(ReportConstants.SINR).toString()));
			map.put(ReportConstants.IMAGE_SINR_LEGEND, imagemap
					.get(ReportConstants.LEGEND + ReportConstants.UNDERSCORE + kpiIndexMap.get(ReportConstants.SINR)));
			}
			if(kpiIndexMap.containsKey(ReportConstants.DL_THROUGHPUT)) {

			map.put(ReportConstants.IMAGE_DL, imagemap.get(kpiIndexMap.get(ReportConstants.DL_THROUGHPUT).toString()));
			map.put(ReportConstants.IMAGE_DL_LEGEND,
					imagemap.get(ReportConstants.LEGEND + ReportConstants.UNDERSCORE + kpiIndexMap.get(ReportConstants.DL_THROUGHPUT)));
			}
			if(kpiIndexMap.containsKey(ReportConstants.UL_THROUGHPUT)) {
	
			map.put(ReportConstants.IMAGE_UL, imagemap.get(kpiIndexMap.get(ReportConstants.UL_THROUGHPUT).toString()));
			map.put(ReportConstants.IMAGE_UL_LEGEND,
					imagemap.get(ReportConstants.LEGEND + ReportConstants.UNDERSCORE + kpiIndexMap.get(ReportConstants.UL_THROUGHPUT)));
			}
			if(kpiIndexMap.containsKey(ReportConstants.PCI_PLOT)) {
				
			map.put(ReportConstants.IMAGE_PCI, imagemap.get(kpiIndexMap.get(ReportConstants.PCI_PLOT).toString()));
			map.put(ReportConstants.IMAGE_PCI_LEGEND, imagemap.get(ReportConstants.PCI_LEGEND));

			}
			map.put(ReportConstants.IMAGE_SITE, imagemap.get(ReportConstants.SITE_IMAGE));
			map.put(ReportConstants.JUSTFICATION_IMG, imagemap.get(ReportConstants.SATELLITE_VIEW3));
			/// HTTP and FTTP
		
			if(kpiIndexMap.containsKey(ReportConstants.HTTP_DL_SINR)) {
			
			map.put(ReportConstants.IMAGE_SINR_HTTP_DL,
					imagemap.get(kpiIndexMap.get(ReportConstants.HTTP_DL_SINR).toString()));
			map.put(ReportConstants.IMAGE_SINR_HTTP_DL_LEGEND, imagemap.get(ReportConstants.LEGEND
					+ ReportConstants.UNDERSCORE + kpiIndexMap.get(ReportConstants.HTTP_DL_SINR)));
			
			}
			if(kpiIndexMap.containsKey(ReportConstants.HTTP_UL_SINR)) {
				
			map.put(ReportConstants.IMAGE_SINR_HTTP_UL,
					imagemap.get(kpiIndexMap.get(ReportConstants.HTTP_UL_SINR).toString()));
			map.put(ReportConstants.IMAGE_SINR_HTTP_UL_LEGEND, imagemap.get(ReportConstants.LEGEND
					+ ReportConstants.UNDERSCORE + kpiIndexMap.get(ReportConstants.HTTP_UL_SINR)));

			}
			if(kpiIndexMap.containsKey(ReportConstants.FTP_DL_SINR)) {
				
			map.put(ReportConstants.IMAGE_SINR_FTP_DL,
					imagemap.get(kpiIndexMap.get(ReportConstants.FTP_DL_SINR).toString()));
			map.put(ReportConstants.IMAGE_SINR_FTP_DL_LEGEND, imagemap.get(ReportConstants.LEGEND
					+ ReportConstants.UNDERSCORE + kpiIndexMap.get(ReportConstants.FTP_DL_SINR).toString()));
			}
			if(kpiIndexMap.containsKey(ReportConstants.FTP_UL_SINR)) {
				
			map.put(ReportConstants.IMAGE_SINR_FTP_UL,
					imagemap.get(kpiIndexMap.get(ReportConstants.FTP_UL_SINR).toString()));
			map.put(ReportConstants.IMAGE_SINR_FTP_UL_LEGEND, imagemap.get(ReportConstants.LEGEND
					+ ReportConstants.UNDERSCORE + kpiIndexMap.get(ReportConstants.FTP_UL_SINR)));
			}
			if(kpiIndexMap.containsKey(ReportConstants.HTTP_DL_RSRP)) {
				
			map.put(ReportConstants.IMAGE_RSRP_HTTP_DL,
					imagemap.get(kpiIndexMap.get(ReportConstants.HTTP_DL_RSRP).toString()));
			map.put(ReportConstants.IMAGE_RSRP_HTTP_DL_LEGEND, imagemap.get(ReportConstants.LEGEND
					+ ReportConstants.UNDERSCORE +kpiIndexMap.get(ReportConstants.HTTP_DL_RSRP)));
			}
			if(kpiIndexMap.containsKey(ReportConstants.HTTP_UL_RSRP)) {
				
			map.put(ReportConstants.IMAGE_RSRP_HTTP_UL,
					imagemap.get(kpiIndexMap.get(ReportConstants.HTTP_UL_RSRP).toString()));
			map.put(ReportConstants.IMAGE_RSRP_HTTP_UL_LEGEND, imagemap.get(ReportConstants.LEGEND
					+ ReportConstants.UNDERSCORE + kpiIndexMap.get(ReportConstants.HTTP_UL_RSRP)));
			}
			if(kpiIndexMap.containsKey(ReportConstants.FTP_DL_RSRP)) {
				
			map.put(ReportConstants.IMAGE_RSRP_FTP_DL,
					imagemap.get(kpiIndexMap.get(ReportConstants.FTP_DL_RSRP).toString()));
			map.put(ReportConstants.IMAGE_RSRP_FTP_DL_LEGEND, imagemap.get(ReportConstants.LEGEND
					+ ReportConstants.UNDERSCORE + kpiIndexMap.get(ReportConstants.FTP_DL_RSRP)));
			}
			if(kpiIndexMap.containsKey(ReportConstants.FTP_DL_RSRP)) {
				
			map.put(ReportConstants.IMAGE_RSRP_FTP_UL,
					imagemap.get(kpiIndexMap.get(ReportConstants.FTP_UL_RSRP).toString()));
			map.put(ReportConstants.IMAGE_RSRP_FTP_UL_LEGEND, imagemap.get(ReportConstants.LEGEND
					+ ReportConstants.UNDERSCORE +kpiIndexMap.get(ReportConstants.FTP_DL_RSRP)));
			}
			if(kpiIndexMap.containsKey(ReportConstants.FTP_DL_RSRP)) {
					
			map.put(ReportConstants.ATOLL_RSRP_IMAGE, imagemap.get(
					ReportConstants.ATOLL + ReportConstants.UNDERSCORE + kpiIndexMap.get(ReportConstants.RSRP)));
			map.put(ReportConstants.ATOLL_RSRP_LEGEND_IMAGE, imagemap.get(ReportConstants.ATOLL_LEGEND
					+ ReportConstants.UNDERSCORE +kpiIndexMap.get(ReportConstants.RSRP)));
			}
			
			if(kpiIndexMap.containsKey(ReportConstants.CALL_PLOT)) {
			map.put(ReportConstants.CALL_PLOT, imagemap.get(kpiIndexMap.get(ReportConstants.CALL_PLOT).toString()));
			}
			if(kpiIndexMap.containsKey(ReportConstants.HANDOVER_PLOT)) {

			map.put(ReportConstants.HANDOVER_PLOT, imagemap.get(kpiIndexMap.get(ReportConstants.HANDOVER_PLOT).toString()));
			}
			if(kpiIndexMap.containsKey(ReportConstants.EARFCN)) {

			map.put(ReportConstants.IMAGE_FC, imagemap.get(kpiIndexMap.get(ReportConstants.EARFCN).toString()));
			map.put(ReportConstants.IMAGE_FC_LEGEND, imagemap.get(ReportConstants.DL_EARFCN));
			}
			map.put(ReportConstants.IMAGE_CGI, imagemap.get(kpiIndexMap.get(DriveHeaderConstants.CGI).toString()));
			map.put(ReportConstants.IMAGE_CGI_LEGEND, imagemap.get(DriveHeaderConstants.CGI));

		} catch (Exception e) {
			logger.error("Error inside the method prepareImageMap{}", e.getMessage());
		}
		logger.info("image map is {}", new Gson().toJson(map));

		return (HashMap<String, Object>) map;
	}

	/**
	 * Prepared wrapper for jasper.
	 *
	 * @param genricWorkOrder   the genric work order
	 * @param address           the address
	 * @param siteInfoList      the site info list
	 * @param kpiInfoList       the kpi info list
	 * @param subWrapperList    the sub wrapper list
	 * @param subWrapper        the sub wrapper
	 * @param mainWrapper       the main wrapper
	 * @param listOfNeHaveAlarm
	 * @return the SSVT report wrapper
	 */
	@Override
	public SSVTReportWrapper preparedWrapperForJasper(GenericWorkorder genricWorkOrder,
			List<SiteInformationWrapper> siteInfoList, List<KPISummaryDataWrapper> kpiInfoList,
			SSVTReportSubWrapper subWrapper, SSVTReportWrapper mainWrapper, String username,
			List<AlarmDataWrapper> listOfNeHaveAlarm) {
		List<SSVTReportSubWrapper> subWrapperList = new ArrayList<>();
		try {
			if (genricWorkOrder.getGwoMeta() != null
					&& genricWorkOrder.getGwoMeta().get(NVWorkorderConstant.SITE_INFO) != null) {
				mainWrapper.setSiteName((String) ReportUtil
						.convertStringToJsonObject(genricWorkOrder.getGwoMeta().get(NVWorkorderConstant.SITE_INFO))
						.get("siteName"));
			}
			String address = null;
			if (siteInfoList != null && !siteInfoList.isEmpty()) {
				address = ReportUtil.getAddressByLatLon(siteInfoList.get(ReportConstants.INDEX_ZER0).getLat(),
						siteInfoList.get(ReportConstants.INDEX_ZER0).getLon());
				mainWrapper.setCityName(siteInfoList.get(ReportConstants.INDEX_ZER0).getCityName());
			}
			mainWrapper.setAddress(address);
			mainWrapper.setTesterName(username);
			genricWorkOrder.getModificationTime();
			mainWrapper.setTestDate(ReportUtil.parseDateToString(ReportConstants.DATE_FORMAT_YYYYMMDD,
					genricWorkOrder.getModificationTime()));
			subWrapper.setTestDate(ReportUtil.parseDateToString(ReportConstants.DATE_FORMAT_YYYYMMDD,
					genricWorkOrder.getModificationTime()));
			subWrapper.setKpiInfoList(kpiInfoList);
			subWrapper.setSiteInfoList(siteInfoList);
			subWrapper.setListOfNeHaveAlarm(listOfNeHaveAlarm);
			subWrapperList.add(subWrapper);
			mainWrapper.setSubWrapperList(subWrapperList);
		} catch (NullPointerException e) {
			logger.error(" NullPointerException in method preparedWrapperForJasper{}", Utils.getStackTrace(e));
		} catch (Exception e) {
			logger.error(" Exception in method preparedWrapperForJasper{}", Utils.getStackTrace(e));

		}
		return mainWrapper;
	}

	/**
	 * Gets the k pi stats data from hbase.
	 *
	 * @param workOrderId the work order id
	 * @param kpiname     the kpiname
	 * @return the k pi stats data from hbase
	 */
	private String[] getKPiStatsDataFromHbase(Integer workOrderId, String kpiname, List<String> recepiList,
			List<String> operatorList) {
		String[] kpistats = null;
		try {
			String kpistat = nvLayer3DashboardService.getKpiStatsRecipeDataForReport(workOrderId, kpiname, recepiList,
					operatorList);
			logger.info(" kpiname {} ,kpistat {}", kpiname, kpistat);
			Map<String, String[]> map = mapper.readValue(kpistat, new TypeReference<Map<String, String[]>>() {
			});
			kpistats = map.get(ReportConstants.RESULT);
			logger.info("kpistats {} ", kpistats != null ? Arrays.toString(kpistats) : null);
			return kpistats;
		} catch (IOException | BusinessException e) {
			logger.error("Exception inside method getKPiStatsDataFromHbase {} ", Utils.getStackTrace(e));
		}
		return kpistats;
	}

	/**
	 * Proceed to create SSVT report.
	 *
	 * @param imageMap    the image map
	 * @param mainWrapper the main wrapper
	 * @return the file
	 */
	private File generateCombinedDataReport(String configName, Map<String, Object> imageMap,
			SSVTReportWrapper mainWrapper) {
		logger.info("Inside method generateCombinedDataReport {} ", mainWrapper);
		try {
			String reportAssetRepo = ConfigUtils.getString(configName);
			List<SSVTReportWrapper> dataSourceList = new ArrayList<>();
			dataSourceList.add(mainWrapper);
			JRBeanCollectionDataSource rfbeanColDataSource = new JRBeanCollectionDataSource(dataSourceList);
			imageMap.put(ReportConstants.SUBREPORT_DIR, reportAssetRepo);
			imageMap.put(ReportConstants.LOGO_CLIENT_KEY, reportAssetRepo + ReportConstants.LOGO_CLIENT_IMG);
			imageMap.put(ConfigUtils.getString(ReportConstants.NV_REPORT_LOGO_CLIENT_KEY),
					reportAssetRepo + ReportConstants.LOGO_CLIENT_IMG);
			imageMap.put(ReportConstants.LOGO_NV_KEY, reportAssetRepo + ReportConstants.LOGO_NV_IMG);
			String destinationFileName = mainWrapper.getFileName();
			JasperRunManager.runReportToPdfFile(reportAssetRepo + ReportConstants.MAIN_JASPER, destinationFileName,
					imageMap, rfbeanColDataSource);
			logger.info("Report Created successfully  ");

			return ReportUtil.getIfFileExists(destinationFileName);
		} catch (Exception e) {
			logger.error("@proceedToCreateSSVTReport getting err={}", Utils.getStackTrace(e));
		}
		logger.info(
				"@proceedToCreateSSVTReport going to return null as there has been some problem in generating report");
		return null;
	}

	public Map<String, Double> getPeakValueData(Integer workorderId, Map<String, List<String>> map) {
		try {
			logger.info("WORKORDER_ID {} , RECIPE LIST {} , OPERATOR LIST {} ", workorderId,
					map.get(QMDLConstant.RECIPE), map.get(QMDLConstant.OPERATOR));
			return nvLayer3DashboardService.getSummaryPeakValueReceipeWise(workorderId, map.get(QMDLConstant.RECIPE),
					map.get(QMDLConstant.OPERATOR));
		} catch (Exception e) {
			logger.error("Error inside the method getPeakValueData {}", Utils.getStackTrace(e));
		}
		return null;
	}

	public List<File> executeForRecipesFile(Map<String, Object> jsonMap, SSVTReportWrapper mainRecipeWrapper)
			throws IOException {
		logger.info("Insde the execute method for SSVT Report generation  with json  {} ", jsonMap);
		List<File> files = new ArrayList<>();
		try {
			Integer workorderId = (Integer) jsonMap.get(ReportConstants.WORKORDER_ID);
			if (reportService.getFileProcessedStatusForWorkorders(Arrays.asList(workorderId))) {
				Map<String, RecipeMappingWrapper> finalMap = getCategoryWiseRecipeWrapperMap(workorderId);
				if (finalMap != null && finalMap.size() > ReportConstants.INDEX_ZER0) {
					int index = ReportConstants.INDEX_ONE;
					for (Entry<String, RecipeMappingWrapper> recipeMappingMap : finalMap.entrySet()) {
						if (recipeMappingMap.getKey().equalsIgnoreCase(ReportConstants.RECIPE_CATEGORY_DRIVE)
								|| recipeMappingMap.getKey()
										.equalsIgnoreCase(ReportConstants.RECIPE_CATEGORY_STATIONARY)
										&& recipeMappingMap.getValue() != null) {
							int recipeIndex = 0;
							for (String recipeId : recipeMappingMap.getValue().getRecpiList()) {
								WORecipeMapping woRecipe = iwoRecipeMappingDao.findByPk(Integer.parseInt(recipeId));
								mainRecipeWrapper.setRecipeNo("Recipe : " + (index++) + "\nRecipe Id : "
										+ woRecipe.getRecipe().getRecipeId());
								File file = reportService.getRecipeWiseKpiFile(ReportConstants.SSVT, workorderId,
										recipeId, recipeMappingMap.getValue().getOperatorList().get(recipeIndex++),
										mainRecipeWrapper, ReportConstants.SSVT_REPORT);
								if (file != null) {
									files.add(file);
								}
							}
						}
					}
				}

			}
		} catch (Exception e1) {
			logger.error("Error inside the method createReportForWorkOrderID for json {} , {} ", jsonMap,
					Utils.getStackTrace(e1));
		}
		return files;
	}

	private List<ImageDetailWrapper> getCapturedImagesList(List<String> listOfLocalFilePath) {
		List<ImageDetailWrapper> imageDetailList = null;
		String path = ConfigUtils.getString(ReportConstants.IMAGE_PATH_FOR_NV_REPORT) + ReportConstants.SSVT
				+ ReportConstants.FORWARD_SLASH
				+ ReportUtil.getFormattedDate(new Date(), ReportConstants.DATE_FORMAT_DD_MMM_YY_HH_MM_SSSSS)
				+ ReportConstants.FORWARD_SLASH;
		ReportUtil.createDirectory(path);
		try {
			if (listOfLocalFilePath != null && listOfLocalFilePath.size() > ReportConstants.INDEX_ZER0) {
				imageDetailList = new ArrayList<>();
				for (String filepath : listOfLocalFilePath) {
					try (ZipFile zipFile = new ZipFile(filepath)) {

						Enumeration<? extends ZipEntry> entries = zipFile.entries();
						while (entries.hasMoreElements()) {
							ZipEntry entry = entries.nextElement();
							imageDetailList.add(setDataInImageDetailWrapper(zipFile, entry, path));
						}
					}
				}
				return getSortedListByTimestamp(imageDetailList);
			}
		} catch (Exception e) {
			logger.error("Exception inside method getCapturedImagesList {} ", e.getMessage());
		}
		return imageDetailList;
	}

	private static List<ImageDetailWrapper> getSortedListByTimestamp(List<ImageDetailWrapper> imageDetailList) {
		List<ImageDetailWrapper> listOfImages = imageDetailList.stream()
				.filter(wrapper -> wrapper.getCaptureTime() != null).collect(Collectors.toList());
		if (listOfImages.size() == imageDetailList.size()) {
			imageDetailList.sort(Comparator.comparingLong(ImageDetailWrapper::getCaptureTime));
			return imageDetailList;
		} else {
			listOfImages.sort(Comparator.comparingLong(ImageDetailWrapper::getCaptureTime));
			listOfImages.addAll(imageDetailList.stream().filter(wrapper -> wrapper.getCaptureTime() == null)
					.collect(Collectors.toList()));
			return listOfImages;
		}
	}

	private ImageDetailWrapper setDataInImageDetailWrapper(ZipFile zipFile, ZipEntry entry, String path) {
		ImageDetailWrapper wrapper = null;
		if (entry != null) {
			try {
				wrapper = new ImageDetailWrapper();
				String name = entry.getName();

				if (name.contains(Symbol.SLASH_FORWARD_STRING)) {
					String[] ar = name.split(Symbol.SLASH_FORWARD_STRING, ReportConstants.INDEX_TWO);
					if (ar.length > 0) {
						name = ar[ReportConstants.INDEX_ONE];
					}
				}
				String imagePath = path + name;

				String[] fileNameArray = name.split(ReportConstants.UNDERSCORE);

				wrapper.setImageCategory(NVLayer3Utils.getStringFromCsv(fileNameArray, ReportConstants.INDEX_ZER0));
				wrapper.setImageName(getImageName(fileNameArray));
				wrapper.setCaptureTime(getTimeStamp(fileNameArray));
				wrapper.setImgPath(ReportUtil.writeStreamToFile(zipFile.getInputStream(entry), imagePath));
				return wrapper;
			} catch (Exception e) {
				logger.error("Exception inside method setDataInImageDetailWrapper {} ", Utils.getStackTrace(e));
			}
		}
		return wrapper;
	}

	Long getTimeStamp(String[] fileNameArray) {
		if (fileNameArray != null) {
			String value = fileNameArray[fileNameArray.length - ReportConstants.INDEX_ONE];
			if (value != null && value.contains(Symbol.DOT_STRING)) {

				return NVLayer3Utils.getLongFromCsv(value.split("\\."), ReportConstants.INDEX_ZER0);
			} else {
				return NVLayer3Utils.getLongFromCsv(fileNameArray, ReportConstants.INDEX_TWO);
			}

		}
		return null;

	}

	String getImageName(String[] fileNameArray) {
		StringBuilder builder = new StringBuilder();
		for (int i = ReportConstants.INDEX_ONE; i < fileNameArray.length; i++) {

			if (i > ReportConstants.INDEX_ONE) {
				builder.append(Symbol.HYPHEN_STRING);

			}
			if (fileNameArray[i].contains(Symbol.DOT_STRING)) {
				builder.append(fileNameArray[i].split("\\.")[ReportConstants.INDEX_ZER0]);

			} else {
				builder.append(fileNameArray[i]);
			}

		}
		return builder.toString();
	}

	private List<String> getLocalFilePathForImagesData(Integer workorderId) {
		List<WOFileDetail> woFileDetailList = nvLayer3DashboardService.getFileDetailsListForWorkOrderId(workorderId);
		List<String> filePathList = new ArrayList<>();
		if (woFileDetailList != null && woFileDetailList.size() > ReportConstants.INDEX_ZER0) {
			filePathList = new ArrayList<>();
			for (WOFileDetail woFileObj : woFileDetailList) {
				if (woFileObj.getWoRecipeMapping().getRecipe().getCategory() != null && woFileObj.getWoRecipeMapping()
						.getRecipe().getCategory().equalsIgnoreCase(ReportConstants.RECIPE_CATEGORY_CAPTURE_IMAGE)) {
					String filePath = invLayer3HDFSDao.copyFileFromHdfsToLocalPath(woFileObj.getFilePath(),
							ConfigUtils.getString(NVConfigUtil.QMDL_TEMP_PATH), woFileObj.getFileName());
					if (filePath != null)
						filePathList.add(filePath);
				}
			}
		}
		return filePathList;
	}

	private Map<String, RecipeMappingWrapper> getCategoryWiseRecipeWrapperMap(Integer workorderId) {
		Map<String, RecipeMappingWrapper> recipeMappingWrapperMap = nvLayer3DashboardService
				.getDriveRecipeDetailForMasterReport(Arrays.asList(workorderId));
		return getCategoryWiseRecipeMappinWrappermap(recipeMappingWrapperMap);
	}

	private Map<String, RecipeMappingWrapper> getCategoryWiseRecipeMappinWrappermap(
			Map<String, RecipeMappingWrapper> map) {
		Map<String, RecipeMappingWrapper> finalMap = new HashMap<>();
		for (Entry<String, RecipeMappingWrapper> wrapper1 : map.entrySet()) {
			if (wrapper1.getKey().equalsIgnoreCase(ReportConstants.RECIPE_CATEGORY_STATIONARY)) {
				finalMap.put(wrapper1.getKey(), map.get(wrapper1.getKey()));
			} else if (wrapper1.getKey().equalsIgnoreCase(ReportConstants.RECIPE_CATEGORY_CAPTURE_IMAGE)) {
				finalMap.put(wrapper1.getKey(), map.get(wrapper1.getKey()));
			} else {
				if (wrapper1.getKey().equalsIgnoreCase(ReportConstants.RECIPE_CATEGORY_DRIVE)
						|| wrapper1.getKey().equalsIgnoreCase(ReportConstants.CALL_SMALL)) {
					RecipeMappingWrapper oldwrapper = finalMap.get(ReportConstants.RECIPE_CATEGORY_DRIVE);
					if (oldwrapper != null) {
						List<String> list = oldwrapper.getRecpiList();
						list.addAll(wrapper1.getValue() != null ? wrapper1.getValue().getRecpiList() : null);
						oldwrapper.setRecpiList(list);
						finalMap.put(ReportConstants.RECIPE_CATEGORY_DRIVE, oldwrapper);
					} else {
						finalMap.put(ReportConstants.RECIPE_CATEGORY_DRIVE, wrapper1.getValue());
					}
				}
			}
		}
		logger.info("Inside getCategoryWiseRecipeMappinWrappermap returning Map : => {} ",
				"Size " + finalMap != null ? finalMap.size() : null);
		return finalMap;
	}

	private void getListCapturedImagesWrapper(Map<String, List<ImageDetailWrapper>> groupByImageCategory,
			SSVTReportWrapper mainWrapper, Map<String, Object> map) {
		List<CapturedImagesWrapper> capturedAzimuthImages = new ArrayList<>();
		List<CapturedImagesWrapper> capturedAntennaHeightImages = new ArrayList<>();
		List<CapturedImagesWrapper> capturedETiltImages = new ArrayList<>();
		List<CapturedImagesWrapper> capturedPanoramaImages = new ArrayList<>();
		PSDataWrapper psDataWrapper = new PSDataWrapper();
		if (groupByImageCategory != null && groupByImageCategory.size() > ReportConstants.INDEX_ZER0) {
			setCaptureImagesInWrapper(groupByImageCategory, map, capturedAzimuthImages, capturedAntennaHeightImages,
					capturedETiltImages, capturedPanoramaImages, psDataWrapper);
		}
		if (mainWrapper.getSubWrapperList() != null && !mainWrapper.getSubWrapperList().isEmpty()) {
			psDataWrapper
					.setSiteInfoList(mainWrapper.getSubWrapperList().get(ReportConstants.INDEX_ZER0).getSiteInfoList());
			if (mainWrapper.getSubWrapperList().get(0).getSiteInfoList() != null
					&& !mainWrapper.getSubWrapperList().get(0).getSiteInfoList().isEmpty()) {

				setDataInPsDataWrapper(psDataWrapper, mainWrapper.getSubWrapperList().get(0).getSiteInfoList().get(0),
						mainWrapper);
			}
		}
		mainWrapper.setListPsDataWrapperList(Arrays.asList(psDataWrapper));

	}

	private void setDataInPsDataWrapper(PSDataWrapper psDataWrapper, SiteInformationWrapper siteInformationWrapper,
			SSVTReportWrapper mainWrapper) {
		logger.error("inside the method  setDataInPsDataWrapper ");
		psDataWrapper.setLatitude(siteInformationWrapper.getLat());
		psDataWrapper.setLongitude(siteInformationWrapper.getLon());
		psDataWrapper.setSfId(siteInformationWrapper.getSiteName());
		psDataWrapper.setRegion(siteInformationWrapper.getRegion());
		psDataWrapper.setCity(siteInformationWrapper.getCityName());
		psDataWrapper.setSiteId(siteInformationWrapper.getSiteName());
		psDataWrapper.setSiteType(siteInformationWrapper.getSiteType());
		psDataWrapper.setCluster(siteInformationWrapper.getCluster());
		psDataWrapper.setDateOfVisit(mainWrapper.getTestDate());
		psDataWrapper.setNameAndContactNumber(mainWrapper.getTesterName());

	}

	private void setCaptureImagesInWrapper(Map<String, List<ImageDetailWrapper>> groupByImageCategory,
			Map<String, Object> map, List<CapturedImagesWrapper> capturedAzimuthImages,
			List<CapturedImagesWrapper> capturedAntennaHeightImages, List<CapturedImagesWrapper> capturedETiltImages,
			List<CapturedImagesWrapper> capturedPanoramaImages, PSDataWrapper psDataWrapper) {
		Set<Entry<String, List<ImageDetailWrapper>>> categoryList = groupByImageCategory.entrySet();
		logger.info("setCaptureImagesInWrapper setCaptureImagesInWrapper, Category List Size Is: {} ", categoryList.size());
		for (Entry<String, List<ImageDetailWrapper>> categoryObject : categoryList) {
			if (categoryObject.getKey().contains(ReportConstants.IMAGE_CATEGORY_AZIMUTH)) {
				setAzimuthImages(categoryObject.getKey(), groupByImageCategory, capturedAzimuthImages, psDataWrapper,
						map);
			}
			if (categoryObject.getKey().contains(ReportConstants.IMAGE_CATEGORY_ANTENNA_HEIGHT)) {
				setAntenaHeightImages(categoryObject.getKey(), groupByImageCategory, capturedAntennaHeightImages,
						psDataWrapper, map);
			}
			if (categoryObject.getKey().contains(ReportConstants.IMAGE_CATEGORY_ELECTRONIC_TILT)) {
				setETiltImages(categoryObject.getKey(), groupByImageCategory, capturedETiltImages, psDataWrapper, map);
			}
			if (categoryObject.getKey().contains(ReportConstants.IMAGE_CATEGORY_PANORAMA_VIEW)) {
				setPanormaImages(categoryObject.getKey(), groupByImageCategory, capturedPanoramaImages, psDataWrapper,
						map);
			}
			if (categoryObject.getKey().contains(ReportConstants.IMAGE_CATEGORY_MECHNICAL_TILT)) {
				List<CapturedImagesWrapper> mTiltImages = new ArrayList<>();
				setMTiltImages(categoryObject.getKey(), groupByImageCategory, mTiltImages, psDataWrapper, map);
			}
		}
	}

	private void setMTiltImages(String key, Map<String, List<ImageDetailWrapper>> groupByImageCategory,
			List<CapturedImagesWrapper> capturedMTiltImages, PSDataWrapper psDataWrapper, Map<String, Object> map) {

		try {
			List<List<ImageDetailWrapper>> listOfListOfImageDetailWrapper = ListUtils
					.partition(groupByImageCategory.get(key), ReportConstants.INDEX_THREE);
			getImageDetailWrapperList(capturedMTiltImages, listOfListOfImageDetailWrapper);
			logger.info("listOfMTiltImages key {}, {}", groupByImageCategory.get(key).size(),
					listOfListOfImageDetailWrapper.size());
			psDataWrapper.setListOfMTiltImages(capturedMTiltImages);
			if (!listOfListOfImageDetailWrapper.isEmpty()) {
				map.put("showMTiltImages", ReportConstants.TRUE);
			}
		} catch (Exception e) {
			logger.error("Inside showMTiltImages Data not Avaiable");
		}

	}

	private void setPanormaImages(String key, Map<String, List<ImageDetailWrapper>> groupByImageCategory,
			List<CapturedImagesWrapper> capturedPanoramaImages, PSDataWrapper psDataWrapper, Map<String, Object> map) {
		try {
			List<List<ImageDetailWrapper>> listOfListOfImageDetailWrapper = ListUtils
					.partition(groupByImageCategory.get(key), ReportConstants.INDEX_THREE);
			getImageDetailWrapperList(capturedPanoramaImages, listOfListOfImageDetailWrapper);
			logger.info("setPanormaImages setPanormaImages {}, {}", groupByImageCategory.get(key).size(),
					listOfListOfImageDetailWrapper.size());
			psDataWrapper.setListOfPanoramaImages(capturedPanoramaImages);
			if (!listOfListOfImageDetailWrapper.isEmpty()) {
				map.put("showPanormaImages", ReportConstants.TRUE);
			}
		} catch (Exception e) {
			logger.error("Inside setPanormaImages Data not Avaiable");
		}
	}

	private void setETiltImages(String key, Map<String, List<ImageDetailWrapper>> groupByImageCategory,
			List<CapturedImagesWrapper> capturedETiltImages, PSDataWrapper psDataWrapper, Map<String, Object> map) {
		try {
			List<List<ImageDetailWrapper>> listOfListOfImageDetailWrapper = ListUtils
					.partition(groupByImageCategory.get(key), ReportConstants.INDEX_THREE);
			getImageDetailWrapperList(capturedETiltImages, listOfListOfImageDetailWrapper);
			logger.info("setETiltImages setETiltImages {}, {}", groupByImageCategory.get(key).size(),
					listOfListOfImageDetailWrapper.size());
			psDataWrapper.setListOfETiltImages(capturedETiltImages);
			if (!listOfListOfImageDetailWrapper.isEmpty()) {
				map.put("showETiltImages", ReportConstants.TRUE);
			}
		} catch (Exception e) {
			logger.error("Inside setETiltImages Data not Avaiable");
		}
	}

	private void setAntenaHeightImages(String key, Map<String, List<ImageDetailWrapper>> groupByImageCategory,
			List<CapturedImagesWrapper> capturedAntennaHeightImages, PSDataWrapper psDataWrapper,
			Map<String, Object> map) {
		try {
			List<List<ImageDetailWrapper>> listOfListOfImageDetailWrapper = ListUtils
					.partition(groupByImageCategory.get(key), ReportConstants.INDEX_THREE);
			getImageDetailWrapperList(capturedAntennaHeightImages, listOfListOfImageDetailWrapper);
			logger.info("setAntenaHeightImages setAntenaHeightImages {}, {} ", groupByImageCategory.get(key).size(),
					listOfListOfImageDetailWrapper.size());
			psDataWrapper.setListOfAntennaHeightImages(capturedAntennaHeightImages);
			if (!listOfListOfImageDetailWrapper.isEmpty()) {
				map.put("showAntenaHeightImages", ReportConstants.TRUE);
			}
		} catch (Exception e) {
			logger.error("Inside setAntenaHeightImages Data not Avaiable");
		}
	}

	private void setAzimuthImages(String key, Map<String, List<ImageDetailWrapper>> groupByImageCategory,
			List<CapturedImagesWrapper> capturedAzimuthImages, PSDataWrapper psDataWrapper, Map<String, Object> map) {
		try {
			List<List<ImageDetailWrapper>> listOfListOfImageDetailWrapper = ListUtils
					.partition(groupByImageCategory.get(key), ReportConstants.INDEX_THREE);
			getImageDetailWrapperList(capturedAzimuthImages, listOfListOfImageDetailWrapper);
			logger.info("setAzimuthImages setAzimuthImages {}, {}", groupByImageCategory.get(key).size(),
					listOfListOfImageDetailWrapper.size());
			psDataWrapper.setListOfAzimuthImages(capturedAzimuthImages);
			if (!listOfListOfImageDetailWrapper.isEmpty()) {
				map.put("showAzimuthImages", ReportConstants.TRUE);
			}
		} catch (Exception e) {
			logger.error("Inside setAzimuthImages Data not Avaiable");
		}
	}

	private void getImageDetailWrapperList(List<CapturedImagesWrapper> capturedAzimuthImages,
			List<List<ImageDetailWrapper>> listOfListOfImageDetailWrapper) {
		for (List<ImageDetailWrapper> listOfImageDetailWrapper : listOfListOfImageDetailWrapper) {
			capturedAzimuthImages.add(new CapturedImagesWrapper(listOfImageDetailWrapper));
		}
	}

	private List<String> getLocalFilePathForImagesDataFromSF(String imgFilePath) {
		logger.info("Inside method getLocalFilePathForImagesDataFromSF is  {} ", imgFilePath);
		List<String> listOfFilePaths = null;
		String fileNameFromPath = imgFilePath.substring(
				imgFilePath.lastIndexOf(ReportConstants.FORWARD_SLASH) + ReportConstants.INDEX_ONE,
				imgFilePath.length());
		String localFilePath = ConfigUtils.getString(NVConfigUtil.QMDL_TEMP_PATH);
		ReportUtil.createDirectory(localFilePath);
		logger.info("localFilePath for ssvt images {} ", localFilePath);
		String filePath = getLocalFilePathOfZipFileFromSf(imgFilePath, localFilePath + fileNameFromPath);
		if (filePath != null) {
			listOfFilePaths = new ArrayList<>();
			listOfFilePaths.add(filePath);
		}

		return listOfFilePaths;
	}

	private String getLocalFilePathOfZipFileFromSf(String requestFilePath, String localFilePath) {
		String attachemntUrl = ConfigUtils.getString(ReportConstants.SF_DONWLOAD_ATTACHMENT_URL);
		attachemntUrl += requestFilePath;
		try {

			attachemntUrl = attachemntUrl.replaceAll(ForesightConstants.SPACE, ForesightConstants.URL_SPACE_REPLACER);
			return writeResponseOfUrl(attachemntUrl, localFilePath);
		} catch (Exception e) {
			logger.error("Exception occured during getting zip file for url , from SF {} ", attachemntUrl,
					Utils.getStackTrace(e));
		}
		return null;
	}

	private String writeResponseOfUrl(String url, String localFilePath) {
		logger.info("Inside method writeResponseOfUrl with url is  {} ,localFilePath {}  ", url, localFilePath);
		try (CloseableHttpClient client = HttpClients.createDefault()){
			HttpGet httpGet = new HttpGet(url);
			logger.info("Foresight api username {} , checksum {} ", NVConstant.FS_API_USERNAME,
					NVConstant.FS_API_CHECKSUM);
			httpGet.setHeader(NVConstant.FS_API_USERNAME, ConfigUtils.getString(NVConstant.FS_API_USERNAME));
			httpGet.setHeader(NVConstant.FS_API_CHECKSUM, ConfigUtils.getString(NVConstant.FS_API_CHECKSUM));
			HttpResponse response = client.execute(httpGet);
			return ReportUtil.writeStreamToFile(response.getEntity().getContent(), localFilePath);
		} catch (ParseException e) {
			logger.error("ParseException inside method sendGetRequest :{}", Utils.getStackTrace(e));
		} catch (Exception e) {
			logger.error("Exception inside method sendGetRequest :{}", Utils.getStackTrace(e));
		}
		return null;
	}

	public InputStream sendGetRequest(String url) throws IOException {
		logger.info("Inside method sendGetRequest with url is  {} ", url);
		InputStream iStream = null;
		CloseableHttpClient client = HttpClients.createDefault();
		try {
			HttpGet httpGet = new HttpGet(url);
			logger.info("foresight api username {} , checksum {} ", NVConstant.FS_API_USERNAME,
					NVConstant.FS_API_CHECKSUM);
			httpGet.setHeader(NVConstant.FS_API_USERNAME, ConfigUtils.getString(NVConstant.FS_API_USERNAME));
			httpGet.setHeader(NVConstant.FS_API_CHECKSUM, ConfigUtils.getString(NVConstant.FS_API_CHECKSUM));
			HttpResponse response = client.execute(httpGet);
			iStream = response.getEntity() != null ? response.getEntity().getContent() : null;
			return iStream;
		} catch (ParseException e) {
			logger.info("ParseException inside method sendGetRequest :{}", Utils.getStackTrace(e));
		} catch (Exception e) {
			logger.info("Exception inside method sendGetRequest :{}", Utils.getStackTrace(e));
		} finally {
			client.close();
		}
		return iStream;
	}

	private static Map<String, String> getBandFromJson(String bandJson) {
		Type listType = new TypeToken<Map<String, String>>() {
		}.getType();
		return new Gson().fromJson(bandJson, listType);
	}

	private String getValueFromGWOMetaForKey(GenericWorkorder workorderObj, String key) {
		String value = null;
		try {
			Map<String, String> geoMap = workorderObj.getGwoMeta();
			value = geoMap.get(key);
			logger.info(" EntityType {} , its value  {}  ", key, value);
		} catch (Exception e) {
			logger.error("Unable to fetch the data for key  {} , {} ", key, Utils.getStackTrace(e));
		}
		return value;
	}

	private void setPeakDlValue(ReportDataHolder reportDataHolder, List<KPISummaryDataWrapper> listofSummaryData,
			GenericWorkorder workOrderObj) {
		List<Double> dlDataList = ReportUtil.convetArrayToList(reportDataHolder.getDataList(), reportDataHolder.getKpiIndexMap().get(ReportConstants.DL_THROUGHPUT));
		if (Utils.isValidList(dlDataList)) {
			KPISummaryDataWrapper wrapper = new KPISummaryDataWrapper();
			wrapper.setTestName(ReportConstants.PEAK_VALUE_SINGLE_SAMPLE);
			wrapper.setSource(ReportConstants.DT_MOBILE);
			Optional<Double> max = dlDataList.stream().max(Comparator.naturalOrder());
			if (max.isPresent()) {
				wrapper.setAchived(ReportUtil.parseToFixedDecimalPlace(max.get(), ForesightConstants.INDEX_TWO)
						+ Symbol.SPACE_STRING + ReportConstants.MBPS);
				if (TemplateType.NV_SSVT_QUICK.equals(workOrderObj.getTemplateType())) {
					wrapper.setTarget(ReportConstants.N_SLASH_A);
					wrapper.setStatus(ReportConstants.N_SLASH_A);
				} else {
					wrapper.setTarget("20 Mhz +10 Mhz (CA) > 110 Mbps");
					wrapper.setStatus((max.get() > 110.0) ? ReportConstants.PASS : ReportConstants.FAIL);
				}
				listofSummaryData.add(wrapper);
			}
		}
	}

}

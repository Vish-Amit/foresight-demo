package com.inn.foresight.module.nv.report.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.maplayer.service.IGenericMapService;
import com.inn.foresight.core.maplayer.utils.GenericMapUtils;
import com.inn.foresight.module.nv.NVConfigUtil;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;
import com.inn.foresight.module.nv.layer3.constants.Layer3PPEConstant;
import com.inn.foresight.module.nv.layer3.constants.QMDLConstant;
import com.inn.foresight.module.nv.layer3.service.INVLayer3DashboardService;
import com.inn.foresight.module.nv.report.constant.ReportIndexWrapper;
import com.inn.foresight.module.nv.report.constant.ReportSummaryIndexWrapper;
import com.inn.foresight.module.nv.report.dao.INVReportHbaseDao;
import com.inn.foresight.module.nv.report.service.IMapImagesService;
import com.inn.foresight.module.nv.report.service.IReportService;
import com.inn.foresight.module.nv.report.service.IStationaryReportService;
import com.inn.foresight.module.nv.report.utils.MergePDF;
import com.inn.foresight.module.nv.report.utils.ReportConstants;
import com.inn.foresight.module.nv.report.utils.ReportUtil;
import com.inn.foresight.module.nv.report.workorder.wrapper.DriveDataWrapper;
import com.inn.foresight.module.nv.report.workorder.wrapper.DriveImageWrapper;
import com.inn.foresight.module.nv.report.wrapper.GeographyWrapper;
import com.inn.foresight.module.nv.report.wrapper.KPIStatsWrapper;
import com.inn.foresight.module.nv.report.wrapper.LiveDriveReportWrapper;
import com.inn.foresight.module.nv.report.wrapper.LiveDriveSubWrapper;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.LiveDriveVoiceAndSmsWrapper;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.QuickTestWrapper;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.YoutubeTestWrapper;
import com.inn.foresight.module.nv.workorder.dao.IWORecipeMappingDao;
import com.inn.foresight.module.nv.workorder.model.CustomGeography.GeographyType;
import com.inn.foresight.module.nv.workorder.model.WORecipeMapping;
import com.inn.foresight.module.nv.workorder.service.ICustomGeographyService;
import com.inn.foresight.module.nv.workorder.wrapper.CustomGeographyWrapper;
import com.inn.product.systemconfiguration.dao.SystemConfigurationDao;
import com.inn.product.systemconfiguration.model.SystemConfiguration;
import com.inn.product.um.geography.dao.GeographyL1Dao;
import com.inn.product.um.geography.dao.GeographyL2Dao;
import com.inn.product.um.geography.dao.GeographyL3Dao;
import com.inn.product.um.geography.dao.GeographyL4Dao;
import com.inn.product.um.geography.model.GeographyL1;
import com.inn.product.um.geography.model.GeographyL2;
import com.inn.product.um.geography.model.GeographyL3;
import com.inn.product.um.geography.model.GeographyL4;

import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service("StationaryReportServiceImpl")
public class StationaryReportServiceImpl implements IStationaryReportService, ReportConstants {
	private Logger logger = LogManager.getLogger(StationaryReportServiceImpl.class);

	@Autowired
	private GeographyL2Dao iGeographyL2Dao;

	@Autowired
	private GeographyL3Dao iGeographyL3Dao;

	@Autowired
	private GeographyL4Dao iGeographyL4Dao;

	@Autowired
	private GeographyL1Dao iGeographyL1Dao;

	@Autowired
	private IGenericMapService genericMapService;

	@Autowired
	private SystemConfigurationDao systemConfigurationDao;

	/** The n V report hbase dao. */
	@Autowired
	private INVReportHbaseDao nVReportHbaseDao;

	@Autowired
	private IMapImagesService mapImageService;

	@Autowired
	private INVLayer3DashboardService nvLayer3DashboardService;

	@Autowired
	private ICustomGeographyService customGeographyService;

	@Autowired
	private IReportService reportService;
	@Autowired
	private IWORecipeMappingDao recipeMappingDao ;

	@Override
	@SuppressWarnings("unchecked")
	public File getStationaryTestReportForMasterReport(List<Integer> workorderIds, GenericWorkorder genericWorkorder,
			Map<String, List<String>> driveRecipedetailsMap) {
		logger.info("inside method getStationaryTestReportForMasterReport for workorder Id {} ", workorderIds);
		File stationaryFile = null;
		String filePath = ConfigUtils.getString(NV_REPORTS_PATH) + STATIONARY + FORWARD_SLASH;
		try {
			Map<String, String> gwoMetaMap = genericWorkorder.getGwoMeta();

			String gwoMeta = genericWorkorder.getGwoMeta().get(KEY_RECIPE_CUSTOM_GEO_MAP);
			logger.info("gwoMeta Data {} ", gwoMeta);
			Map<String, Integer> recipeCustomGeographyMap = null;
			if (gwoMeta != null) {
				recipeCustomGeographyMap = new ObjectMapper().readValue(gwoMeta, HashMap.class);
			}

			/** Summary Data Fetch */
			Map<String, String[]> recipeWiseSummaryMap = new HashMap<>();
			List<String> fetchSummaryKPIList = ReportSummaryIndexWrapper.getLiveDriveKPIs();
			Map<String, Integer> summaryKpiIndexMap = ReportSummaryIndexWrapper
					.getLiveDriveKPIIndexMap(fetchSummaryKPIList);

			for (String recipe : driveRecipedetailsMap.get(RECIPE)) {

				String[] summaryData = reportService.getSummaryDataForReport(workorderIds, Arrays.asList(recipe),
						fetchSummaryKPIList);
				recipeWiseSummaryMap.put(recipe, summaryData);
			}

			LiveDriveReportWrapper initialPagesWrapper = getInitialPagesData(workorderIds.get(ReportConstants.INDEX_ZER0), recipeWiseSummaryMap,
					gwoMetaMap, filePath, summaryKpiIndexMap);
			HashMap<String, Object> imageMap = null;
			logger.debug("initialPagesWrapper Data {} ", new Gson().toJson(initialPagesWrapper));
			Set<String> dynamicKpis = reportService.getDynamicKpiName(workorderIds, null, Layer3PPEConstant.ADVANCE);
			List<String> fetchKPIList = ReportIndexWrapper.getLiveDriveKPIs().stream()
					.filter(k -> dynamicKpis.contains(k)).collect(Collectors.toList());
			Map<String, Integer> kpiIndexMap = ReportIndexWrapper.getLiveDriveKPIIndexMap(fetchKPIList);

			if (initialPagesWrapper.getPinPointList() != null) {
				List<Double[]> pinPointList = initialPagesWrapper.getPinPointList();
				if ((pinPointList == null || !pinPointList.isEmpty()) && recipeCustomGeographyMap != null) {
					pinPointList = getLatLongFromRecipeCustomGeographyMapping(recipeCustomGeographyMap);
					logger.info("pinPointList from Custom Geography data {} ", pinPointList);
				}
				DriveImageWrapper imageWrapper = getImageWrapperWithBoundaryData(initialPagesWrapper.getGeograhyName(),
						initialPagesWrapper.getGeographyType());

				
				List<String[]> dataList = reportService.getDriveDataForReport(workorderIds,
						driveRecipedetailsMap.get(RECIPE), fetchKPIList);
				imageWrapper.setIndexLatitude(kpiIndexMap.get(LATITUDE));
				imageWrapper.setIndexLongitude(kpiIndexMap.get(LONGITUDE));
				imageWrapper.setDataKPIs(dataList);
				String imageName = STATIONARY_MAP_INITIAL_IMAGE;
				logger.info("imageName===  {} ", imageName);
				if (pinPointList != null && !pinPointList.isEmpty()) {
					HashMap<String, BufferedImage> bufferedImageMap = mapImageService.getStationaryImages(imageWrapper,
							pinPointList, imageName);
					imageMap = prepareImageMap(mapImageService.saveDriveImages(bufferedImageMap,
							ConfigUtils.getString(FINAL_IMAGE_PATH), false), imageName);
				}
			}
			File file = proceedToCreateLiveDriveReport(imageMap, initialPagesWrapper, true);
			List<WORecipeMapping> mappings=recipeMappingDao.getWORecipeByGWOIds(workorderIds);
			Map<Integer, List<WORecipeMapping>> woRecipeMap=mappings.stream().collect(Collectors.groupingBy(WORecipeMapping::getId));
			List<File> files = getFilesforEachRecipeofMap(woRecipeMap, driveRecipedetailsMap, initialPagesWrapper,
					filePath, recipeWiseSummaryMap, summaryKpiIndexMap,kpiIndexMap,fetchKPIList);
			files.add(0, file);
			MergePDF.mergeFiles(initialPagesWrapper.getFileName(), files);
			stationaryFile = ReportUtil.getIfFileExists(initialPagesWrapper.getFileName());
		} catch (Exception e) {
			logger.error("Error inside the method in getStationaryTestReportForMasterReport{} ",
					Utils.getStackTrace(e));
		}

		return stationaryFile;
	}

	private LiveDriveReportWrapper getInitialPagesData(Integer workorderId, Map<String, String[]> recipeWiseSummaryMap,
			Map<String, String> gwoMetaMap, String filePath, Map<String, Integer> summaryKpiIndexMap) {
		logger.info("Inside method getInitialPagesData for workOrderId {} ", workorderId);
		LiveDriveReportWrapper initialPagesWrapper = new LiveDriveReportWrapper();
		try {
			getPointList(recipeWiseSummaryMap, initialPagesWrapper, summaryKpiIndexMap);
			Optional<Entry<String, String[]>> optional = recipeWiseSummaryMap.entrySet().stream().findFirst();
			setDateInwrapper(optional.isPresent() ? optional.get().getValue() : null, initialPagesWrapper,
					summaryKpiIndexMap);
			initialPagesWrapper.setFileName(ReportUtil.getFileName("STATIONARY_TEST", workorderId, filePath));
			setGeographyDataSubwrapper(initialPagesWrapper, gwoMetaMap);
		} catch (Exception e) {
			logger.warn("Exception inside method during seting InitialPagesData {} ", Utils.getStackTrace(e));
		}
		return initialPagesWrapper;
	}

	private void getPointList(Map<String, String[]> recipeWiseSummaryMap, LiveDriveReportWrapper initialPagesWrapper,
			Map<String, Integer> kpiSummaryIndexMap) {
		if (recipeWiseSummaryMap != null) {
			logger.info("inside method getPointList with lsitArray OF size {} ", recipeWiseSummaryMap.size());
			List<Double[]> pinPointList = new ArrayList<>();
			try {
				for (String[] sumaary : recipeWiseSummaryMap.values()) {
					Double[] arr = new Double[2];
					if (sumaary != null && kpiSummaryIndexMap.get(LATEST_UNDERSCORE + START_LATITUDE) != null
							&& kpiSummaryIndexMap.get(LATEST_UNDERSCORE + START_LONGITUDE) != null
							&& sumaary.length > kpiSummaryIndexMap.get(LATEST_UNDERSCORE + START_LATITUDE)
							&& sumaary[kpiSummaryIndexMap.get(LATEST_UNDERSCORE + START_LATITUDE)] != null
							&& sumaary[kpiSummaryIndexMap.get(LATEST_UNDERSCORE + START_LONGITUDE)] != null
							&& StringUtils
									.isNotBlank(sumaary[kpiSummaryIndexMap.get(LATEST_UNDERSCORE + START_LATITUDE)])
							&& StringUtils
									.isNotBlank(sumaary[kpiSummaryIndexMap.get(LATEST_UNDERSCORE + START_LONGITUDE)])) {

						arr[1] = Double
								.parseDouble(sumaary[kpiSummaryIndexMap.get(LATEST_UNDERSCORE + START_LATITUDE)]);
						arr[0] = Double
								.parseDouble(sumaary[kpiSummaryIndexMap.get(LATEST_UNDERSCORE + START_LONGITUDE)]);
						logger.info("arr[] {} , arr {} ", arr[0], arr[1]);
						pinPointList.add(arr);

					}
				}
			} catch (Exception e) {
				logger.error("exception inside method getPointList {} ", Utils.getStackTrace(e));
			}
			initialPagesWrapper.setPinPointList(pinPointList);
		}
	}

	private DriveImageWrapper getImageWrapperWithBoundaryData(String geographyName, String geographyType) {
		logger.info("getImageWrapperWithBoundaryData geographyName {} , geographyType {} ", geographyName,
				geographyType);
		DriveImageWrapper imageWrapper = new DriveImageWrapper();
		String tableName = ReportUtil.getGeographyTableNameBYGeographyType(geographyType);
		if (tableName != null) {
			List<Map<String, String>> boundariesMap = genericMapService.getBoundaryDataByGeographyNamesMS(
					Arrays.asList(geographyName), GenericMapUtils.GEOGRAPHY_TABLE_NAME,
					GenericMapUtils.getColumnListForQuery(), null, tableName.toUpperCase());
			ObjectMapper mapper = new ObjectMapper();
			List<List<List<Double>>> boundary = null;
			List<List<List<List<Double>>>> boundaries = new ArrayList<>();
			if (boundariesMap != null && !boundariesMap.isEmpty()) {
				try {
					for (int index = INDEX_ZER0; index < boundariesMap.size(); index++) {
						if (boundariesMap.get(index) != null && boundariesMap.get(index).get(COORDINATES) != null) {
							boundary = mapper.readValue(boundariesMap.get(index).get(COORDINATES),
									new TypeReference<List<List<List<Double>>>>() {
									});
							if (boundary != null) {
								boundaries.add(boundary);
							}
						}
					}
				} catch (Exception e) {
					logger.error("Error while parsing boundary data: {}  ", e.getMessage());
				}
				logger.info("boundaries Data Size for geography {} , of type {} ,is  {} ", geographyName, geographyType,
						boundaries.size());
				imageWrapper.setBoundaries(boundaries);
			}
		}
		return imageWrapper;
	}

	private HashMap<String, Object> prepareImageMap(HashMap<String, String> imagemap, String fileName) {
		logger.info("inside the method prepareImageMap");
		Map<String, Object> map = new HashMap<>();
		try {
			map.put(fileName, imagemap.get(fileName));
		} catch (Exception e) {
			logger.error("Error inside the method prepareImageMap{}", e.getMessage());
		}
		return (HashMap<String, Object>) map;
	}

	private File proceedToCreateLiveDriveReport(Map<String, Object> imageMap, LiveDriveReportWrapper mainWrapper,
			Boolean isInitialPage) {
		logger.info("inside the methid proceedToCreateSSVTReport ");
		try {
			String jasperReportSourcePath = null;
			String jasperName;
			String destinationFileName = mainWrapper.getFileName();
			if (isInitialPage) {
				jasperReportSourcePath = ConfigUtils.getString(NVConfigUtil.GET_STATIONARY_TEST_MAIN_JASPER);
				logger.info("jasperReportSourcePath ==>{}", jasperReportSourcePath);
				jasperName = STATIONARY_MAIN_JASPER_NAME;
			} else {
				jasperReportSourcePath = ConfigUtils.getString(NVConfigUtil.GET_STATIONARY_TEST_SUB_JASPER);
				logger.info("jasperReportSourcePath level ==>{}", jasperReportSourcePath);

				jasperName = MAIN_JASPER;
			}
			if (imageMap == null) {
				logger.info("inside the image map is null");
				imageMap = new HashMap<>();
			}
			List<LiveDriveReportWrapper> dataSourceList = new ArrayList<>();
			dataSourceList.add(mainWrapper);
			JRBeanCollectionDataSource rfbeanColDataSource = new JRBeanCollectionDataSource(dataSourceList);
			imageMap.put(SUBREPORT_DIR, jasperReportSourcePath);
			imageMap.put(IMAGE_PARAM_HEADER_LOGO, jasperReportSourcePath + IMAGE_HEADER_LOGO);
			imageMap.put(IMAGE_PARAM_HEADER_BG, jasperReportSourcePath + IMAGE_HEADER_BG);
			imageMap.put(IMAGE_PARAM_HEADER_LOG, jasperReportSourcePath + IMAGE_HEADER_LOG);
			imageMap.put(IMAGE_PARAM_SCREEN_LOG, jasperReportSourcePath + IMAGE_SCREEN_LOGO_STATIONARY);
			imageMap.put(IMAGE_PARAM_SCREEN_BG, jasperReportSourcePath + IMAGE_SCREEN_BG);

			JasperRunManager.runReportToPdfFile(jasperReportSourcePath + jasperName, destinationFileName, imageMap,
					rfbeanColDataSource);

			logger.info("end");

			return ReportUtil.getIfFileExists(destinationFileName);
		} catch (Exception e) {
			logger.error("@proceedToCreateSSVTReport getting err={}" + Utils.getStackTrace(e));
		}
		logger.info(
				"@proceedToCreateSSVTReport going to return null as there has been some problem in generating report");
		return null;
	}

	private List<File> getFilesforEachRecipeofMap(Map<Integer, List<WORecipeMapping>> woRecipeMap, Map<String, List<String>> driveRecipedetailsMap,
			LiveDriveReportWrapper initialPagesWrapper, String filePath, Map<String, String[]> recipeWiseSummaryMap,
			Map<String, Integer> summaryKpiIndexMap, Map<String, Integer> kpiIndexMap, List<String> fetchKPIList) {

		int recipeIndex = 0;
		List<File> files = new ArrayList<>();
		if (!recipeWiseSummaryMap.isEmpty()) {
			for (Entry<String, String[]> recipeSmmry : recipeWiseSummaryMap.entrySet()) {
				if (recipeSmmry.getValue() != null && recipeSmmry.getValue().length > 0) {
					List<WORecipeMapping> mappings=woRecipeMap.get(Integer.valueOf(recipeSmmry.getKey()));
					File file = getFileForEachRecipe(mappings.get(ReportConstants.INDEX_ZER0).getGenericWorkorder().getId(), recipeSmmry.getKey(), recipeSmmry.getValue(),
							driveRecipedetailsMap.get(OPERATOR), recipeIndex + 1, initialPagesWrapper, filePath,
							summaryKpiIndexMap,kpiIndexMap,fetchKPIList);
					if (file != null) {
						files.add(file);
					}
					recipeIndex++;
				}
			}
		}
		return files;
	}

	private LiveDriveReportWrapper setGeographyDataSubwrapper(LiveDriveReportWrapper initialPagesWrapper,
			Map<String, String> gwoMetaMap) {
		List<LiveDriveSubWrapper> listInitSubWrapper = new ArrayList<>();
		LiveDriveSubWrapper initialSubWrapper = new LiveDriveSubWrapper();
		List<GeographyWrapper> list = new ArrayList<>();
		logger.info("gwoMetaMap {} ", gwoMetaMap.get("geographyType"));
		try {
			Map<String, String> map = getGeographyNameValueMap();
			if (GEOGRAPHYL4.equalsIgnoreCase(gwoMetaMap.get(GEOGRAPHY_TYPE))) {
				GeographyL4 geoGraphyL4 = iGeographyL4Dao.findByPk(Integer.parseInt(gwoMetaMap.get(GEOGRAPHY_ID)));
				initialPagesWrapper.setGeograhyName(geoGraphyL4.getDisplayName());
				initialPagesWrapper.setGeographyType(GEOGRAPHYL4);

				list.add(getGeographyWrapper(map.get(GEOGRAPHYL4),geoGraphyL4.getDisplayName()));
				list.add(getGeographyWrapper(map.get(GEOGRAPHYL3),geoGraphyL4.getGeographyL3().getDisplayName()));
				list.add(getGeographyWrapper(map.get(GEOGRAPHYL2),geoGraphyL4.getGeographyL3().getGeographyL2().getDisplayName()));
				list.add(getGeographyWrapper(map.get(GEOGRAPHYL1),geoGraphyL4.getGeographyL3().getGeographyL2().getGeographyL1().getDisplayName()));
			}else if(GEOGRAPHYL3.equalsIgnoreCase(gwoMetaMap.get(GEOGRAPHY_TYPE))) {

				GeographyL3 geoGraphyL3 = iGeographyL3Dao.findByPk(Integer.parseInt(gwoMetaMap.get(GEOGRAPHY_ID)));
				initialPagesWrapper.setGeographyType(GEOGRAPHYL3);
				initialPagesWrapper.setGeograhyName(geoGraphyL3.getDisplayName());

				
				list.add(getGeographyWrapper(map.get(GEOGRAPHYL3),geoGraphyL3.getDisplayName()));
				list.add(getGeographyWrapper(map.get(GEOGRAPHYL2),geoGraphyL3.getGeographyL2().getDisplayName()));
				list.add(getGeographyWrapper(map.get(GEOGRAPHYL1),geoGraphyL3.getGeographyL2().getGeographyL1().getDisplayName()));
			}else if(GEOGRAPHYL2.equalsIgnoreCase(gwoMetaMap.get(GEOGRAPHY_TYPE))){

				initialPagesWrapper.setGeographyType(GEOGRAPHYL2);
				GeographyL2 geoGraphyL2 = iGeographyL2Dao
						.getGeographyL2ById(Integer.parseInt(gwoMetaMap.get(GEOGRAPHY_ID)));

				initialPagesWrapper.setGeograhyName(geoGraphyL2.getDisplayName());
				list.add(getGeographyWrapper(map.get(GEOGRAPHYL2),geoGraphyL2.getDisplayName()));
				list.add(getGeographyWrapper(map.get(GEOGRAPHYL1),geoGraphyL2.getGeographyL1().getDisplayName()));
			}else if(GEOGRAPHYL1.equalsIgnoreCase(gwoMetaMap.get(GEOGRAPHY_TYPE))){

				initialPagesWrapper.setGeographyType(GEOGRAPHYL1);
				GeographyL1 geoGraphyL1 = iGeographyL1Dao
						.getGeographyL1ById(Integer.parseInt(gwoMetaMap.get(GEOGRAPHY_ID)));

				initialPagesWrapper.setGeograhyName(geoGraphyL1.getDisplayName());
				list.add(getGeographyWrapper(map.get(GEOGRAPHYL1),geoGraphyL1.getDisplayName()));

			}
		} catch (Exception e) {
			logger.error("Exception in setting geography Data  {} ", Utils.getStackTrace(e));
		}
		initialSubWrapper.setGeoWrapperList(list);
		listInitSubWrapper.add(initialSubWrapper);
		initialPagesWrapper.setNhi2DataList(listInitSubWrapper);
		return initialPagesWrapper;
	}

	private Map<String, String> getGeographyNameValueMap() {
		ArrayList<String> typeList = new ArrayList<>();
		typeList.add(GEOGRAPHY);
		List<SystemConfiguration> list = systemConfigurationDao.getSystemConfigurationByType(typeList);
		return list.stream()
				.collect(Collectors.toMap(SystemConfiguration::getName, SystemConfiguration::getValue));
	}

	private GeographyWrapper getGeographyWrapper(String geoName, String geographyValue) {
		GeographyWrapper wrapper = new GeographyWrapper();
		wrapper.setGeographyLevelName(geoName);
		wrapper.setGeographyName(geographyValue);
		return wrapper;
	}

	private LiveDriveReportWrapper setDateInwrapper(String[] listArray, LiveDriveReportWrapper initialPagesWrapper,
			Map<String, Integer> kpiSummaryIndexMap) {
		logger.info("Inside method setDateInwrapper ");
		SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_DD_MM_YY, Locale.ENGLISH);
		Date date = new Date();
		String testDate = format.format(date);
		initialPagesWrapper.setTestDate(testDate);
		try {
			Integer timestampIndex = kpiSummaryIndexMap.get(LATEST_UNDERSCORE + TIMESTAMP);
			if (listArray != null && timestampIndex != null && listArray.length >= timestampIndex
					&& listArray[timestampIndex] != null && !listArray[timestampIndex].isEmpty()
					&& StringUtils.isNotBlank(listArray[timestampIndex])) {
				long timeStamp = Long.parseLong(listArray[timestampIndex]);
				initialPagesWrapper.setTestDate(format.format(new Date(timeStamp)));

			}
		} catch (Exception e) {
			logger.error("Exception inside method setDateInwrapper {} , initialPagesWrapper {} ", initialPagesWrapper,
					Utils.getStackTrace(e));
		}
		return initialPagesWrapper;
	}

	private File getFileForEachRecipe(Integer workorderId, String recipe, String[] smryArray, List<String> operatorList,
			int recipeIndex, LiveDriveReportWrapper initialPagesWrapper, String filePath,
			Map<String, Integer> summaryKpiIndexMap, Map<String, Integer> kpiIndexMap, List<String> fetchKPIList) {
		logger.info("inside method getFileForEachRecipe for workorderId {} , recipe {} , operatorList {} ", workorderId,
				recipe, operatorList);
		try {
			
			
			List<String[]> listArray = reportService.getDriveDataForReport(Arrays.asList(workorderId),Arrays.asList(recipe), fetchKPIList);

			List<YoutubeTestWrapper> youtubeDataList = reportService
					.getYouTubeDataForReport(listArray, kpiIndexMap);
			List<QuickTestWrapper> quickTestDataList = reportService
					.getQuickTestDataRecipeForListOfOperators(workorderId, recipe, operatorList);
			LiveDriveReportWrapper driveReportWrapper = setDataForLiveDriveReport(smryArray, youtubeDataList,
					quickTestDataList, recipeIndex, initialPagesWrapper.getGeograhyName(), summaryKpiIndexMap);
			if (driveReportWrapper != null) {
				if (summaryKpiIndexMap.get(START_TIMESTAMP) != null
						&& smryArray.length >= summaryKpiIndexMap.get(START_TIMESTAMP)) {
					driveReportWrapper
							.setTestDate(getTestDateforSummaryData(smryArray[summaryKpiIndexMap.get(START_TIMESTAMP)]));
				}
				driveReportWrapper.setFileName(
						ReportUtil.getFileName("STATIONARY_TEST_REPORT" + recipeIndex, recipeIndex, filePath));
				driveReportWrapper.setListPsDataWrapper(
						reportService.getPsDataWrapperFromSummaryDataForReport(smryArray, summaryKpiIndexMap));
			}
			DriveImageWrapper imageWrapper = getImageWrapperWithBoundaryData(initialPagesWrapper.getGeograhyName(),
					initialPagesWrapper.getGeographyType());
			String imageName = STATIONARY_MAP_IMAGE;
			String combineData = nvLayer3DashboardService.getDriveDetailReceipeWise(workorderId, Arrays.asList(recipe),
					operatorList);
			if (!combineData.contains(QMDLConstant.NO_RESULT_FOUND)) {
				Map<String, String> dataMap = ReportUtil.convertCSVStringToMap(combineData);
				List<String[]> dataList = ReportUtil.convertCSVStringToDataList(dataMap.get(RESULT));
				imageWrapper.setDataKPIs(dataList);
			}
			imageWrapper.setIndexLatitude(summaryKpiIndexMap.get(START_LATITUDE));
			imageWrapper.setIndexLongitude(summaryKpiIndexMap.get(START_LONGITUDE));
			HashMap<String, Object> imageMap = null;
			imageMap = populateDataForRecipe(workorderId, recipe, smryArray, operatorList, imageWrapper, imageName,
					imageMap, summaryKpiIndexMap);
			return proceedToCreateLiveDriveReport(imageMap, driveReportWrapper, false);
		} catch (Exception e) {
			logger.error("Excetpion inside methgod getFileForEachRecipe {} ", Utils.getStackTrace(e));
		}
		return null;
	}

	private HashMap<String, Object> populateDataForRecipe(Integer workorderId, String recipe, String[] smryArray,
			List<String> operatorList, DriveImageWrapper imageWrapper, String imageName,
			HashMap<String, Object> imageMap, Map<String, Integer> summaryKpiIndexMap) {
		try {
			if (summaryKpiIndexMap.get(START_LATITUDE) != null && summaryKpiIndexMap.get(START_LONGITUDE) != null
					&& smryArray.length - 1 > summaryKpiIndexMap.get(START_LATITUDE)
					&& smryArray.length - 1 > summaryKpiIndexMap.get(START_LONGITUDE)) {
				Double[] lonlat = new Double[2];
				List<Double[]> lonlatList = new ArrayList<>();
				if (StringUtils.isNotEmpty(smryArray[summaryKpiIndexMap.get(START_LATITUDE)])
						&& StringUtils.isNotEmpty(smryArray[summaryKpiIndexMap.get(START_LONGITUDE)])) {
					lonlat[0] = Double.parseDouble(smryArray[summaryKpiIndexMap.get(START_LONGITUDE)]);
					lonlat[1] = Double.parseDouble(smryArray[summaryKpiIndexMap.get(START_LATITUDE)]);
					lonlatList.add(lonlat);
				}
				HashMap<String, BufferedImage> bufferedImageMap = mapImageService.getStationaryImages(imageWrapper,
						lonlatList, imageName);
				imageMap = prepareImageMap(mapImageService.saveDriveImages(bufferedImageMap,
						ConfigUtils.getString(FINAL_IMAGE_PATH), false), imageName);
				List<DriveDataWrapper> imageDataList = nVReportHbaseDao.getSpeedTestDatafromHbase(workorderId,
						Arrays.asList(recipe), operatorList);
				Map<String, Object> sectorImg = ReportUtil.getSectorWiseImageFromList(imageDataList);
				if (sectorImg != null) {
					imageMap.putAll(sectorImg);
				}
			}
		} catch (Exception e) {
			logger.error("Excetpion inside method getFileForEachRecipe {} ", Utils.getStackTrace(e));
		}
		return imageMap;
	}

	private List<Double[]> getLatLongFromRecipeCustomGeographyMapping(
			Map<String, Integer> recipeCustomGeographyMapping) {
		List<Double[]> latLongList = new ArrayList<>();
		try {
			for (Entry<String, Integer> entry : recipeCustomGeographyMapping.entrySet()) {
				CustomGeographyWrapper customGeographyWrapper = customGeographyService
						.getCustomGeography(entry.getValue(), GeographyType.POINT);
				Double[] latLong = new Double[2];
				if (customGeographyWrapper != null) {
					latLong[1] = customGeographyWrapper.getStartLatitude();
					latLong[0] = customGeographyWrapper.getStartLongitude();
					latLongList.add(latLong);
				}
			}
			return latLongList;
		} catch (RestException re) {
			logger.error("Error while getting custom geography data: {}" + re.getMessage());
		}
		return latLongList;
	}

	private LiveDriveReportWrapper setDataForLiveDriveReport(String[] summaryArray,
			List<YoutubeTestWrapper> youTubeDataList, List<QuickTestWrapper> qiuckTestDataList, Integer recipeNo,
			String geoGraphyName, Map<String, Integer> summaryKpiIndexMap) {
		LiveDriveReportWrapper driveReportWrapper = null;
		if (summaryArray != null && summaryArray.length > 0) {
			try {
				LiveDriveSubWrapper driveSubWrapper = new LiveDriveSubWrapper();
				setStartandEndTime(summaryArray, driveSubWrapper, summaryKpiIndexMap);
				driveSubWrapper.setUniqueCells(getNumberOfUniqueCells(summaryArray, summaryKpiIndexMap));
				driveSubWrapper.setRecipeNo("Recipe " + recipeNo);
				driveSubWrapper.setNoOfLocation(INDEX_ONE);
				if (summaryKpiIndexMap.get(START_LATITUDE) != null && summaryKpiIndexMap.get(LONGITUDE) != null
						&& summaryArray.length - 1 > summaryKpiIndexMap.get(START_LATITUDE)
						&& summaryArray.length - 1 > summaryKpiIndexMap.get(START_LONGITUDE)) {
					if (NumberUtils.isCreatable(summaryArray[summaryKpiIndexMap.get(START_LATITUDE)])) {
						driveSubWrapper.setLatitude(ReportUtil.parseToFixedDecimalPlace(
								Double.parseDouble(summaryArray[summaryKpiIndexMap.get(START_LATITUDE)]), INDEX_SIX));
					}
					if (NumberUtils.isCreatable(summaryArray[summaryKpiIndexMap.get(START_LONGITUDE)])) {
						driveSubWrapper.setLongitude(ReportUtil.parseToFixedDecimalPlace(
								Double.parseDouble(summaryArray[summaryKpiIndexMap.get(START_LONGITUDE)]), INDEX_SIX));
					}

				}
				driveSubWrapper.setGeographyL4(geoGraphyName);
				driveReportWrapper = setVoiceAndDataList(summaryArray, driveSubWrapper, summaryKpiIndexMap);

				setSmsCallDataList(summaryArray, driveReportWrapper, driveSubWrapper, summaryKpiIndexMap);
				setYoutubeAndQuickData(youTubeDataList, qiuckTestDataList, driveReportWrapper, driveSubWrapper);
				logger.debug("driveReportWrapper Stationary: {}", new Gson().toJson(driveReportWrapper));
			} catch (Exception e) {
				logger.error("Exception inside method setDataForLiveDriveReport {} ",Utils.getStackTrace(e));
			}
		}
		return driveReportWrapper;
	}

	private void setYoutubeAndQuickData(List<YoutubeTestWrapper> youTubeDataList,
			List<QuickTestWrapper> qiuckTestDataList, LiveDriveReportWrapper driveReportWrapper,
			LiveDriveSubWrapper driveSubWrapper) {
		if(youTubeDataList!=null && !youTubeDataList.isEmpty()) {
			driveSubWrapper.setYoutubeTestWrapperList(youTubeDataList);
			driveReportWrapper.setYouTubePage(TRUE);
		}
		if(qiuckTestDataList!=null && !qiuckTestDataList.isEmpty()) {
			driveSubWrapper.setSpeedTestWrapperList(qiuckTestDataList);
			driveReportWrapper.setQuicTestPage(TRUE);
		}
	}

	private void setSmsCallDataList(String[] summaryArray, LiveDriveReportWrapper driveReportWrapper,
			LiveDriveSubWrapper driveSubWrapper, Map<String, Integer> summaryKpiIndexMap) {
		List<LiveDriveVoiceAndSmsWrapper> smsDataList = ReportUtil.smsDataListForReport(summaryArray,
				summaryKpiIndexMap);
		List<LiveDriveVoiceAndSmsWrapper> callDataList = ReportUtil.callDataListForReport(summaryArray,
				summaryKpiIndexMap);
		if (Utils.isValidList(smsDataList)) {
			driveReportWrapper.setSmsPage(TRUE);
			driveSubWrapper.setSmsDataList(smsDataList);
		}
		if (Utils.isValidList(callDataList)) {
			driveReportWrapper.setCallPage(TRUE);
			driveSubWrapper.setCallDataList(callDataList);
		}
	}

	private void setStartandEndTime(String[] summaryArray, LiveDriveSubWrapper driveSubWrapper,
			Map<String, Integer> summaryKpiIndexMap) {
		if (summaryKpiIndexMap.get(START_TIMESTAMP) != null
				&& summaryArray.length >= summaryKpiIndexMap.get(START_TIMESTAMP)
				&& summaryArray[summaryKpiIndexMap.get(START_TIMESTAMP)] != null) {
			driveSubWrapper
					.setStartTime(getTestTimeforSummaryData(summaryArray[summaryKpiIndexMap.get(START_TIMESTAMP)]));
		}
		if (summaryKpiIndexMap.get(END_TIMESTAMP) != null
				&& summaryArray.length >= summaryKpiIndexMap.get(END_TIMESTAMP)
				&& summaryArray[summaryKpiIndexMap.get(END_TIMESTAMP)] != null) {
			driveSubWrapper.setEndTime(getTestTimeforSummaryData(summaryArray[summaryKpiIndexMap.get(END_TIMESTAMP)]));
		}
	}

	private Integer getNumberOfUniqueCells(String[] summaryCsv, Map<String, Integer> summaryKpiIndexMap) {
		return summaryKpiIndexMap.get(CELL_ID) != null
				? summaryCsv[summaryKpiIndexMap.get(CELL_ID)].split(UNDERSCORE).length
				: null;
	}

	private String getTestTimeforSummaryData(String timeStamp) {
		try {
			long time;
			if (timeStamp != null && !timeStamp.isEmpty()) {
				time = Long.parseLong(timeStamp);
			} else {
				time = new Date().getTime();
			}
			Date date = new Date(time);
			SimpleDateFormat format = new SimpleDateFormat("dd-MMMM-yyyy hh:mm:ss a", Locale.ENGLISH);
			return format.format(date);
		} catch (Exception p) {
			logger.error("Error inside the method getTestTimeforSummaryData {}", p.getMessage());
		}
		return null;
	}

	private String getTestDateforSummaryData(String timeStamp) {
		long time;
		if (timeStamp != null && StringUtils.isNotBlank(timeStamp)) {
			time = Long.parseLong(timeStamp);
		} else {
			time = new Date().getTime();
		}
		Date date = new Date(time);
		SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_DD_MM_YY, Locale.ENGLISH);
		return format.format(date);
	}

	private LiveDriveReportWrapper setVoiceAndDataList(String[] listArray, LiveDriveSubWrapper driveSubWrapper,
			Map<String, Integer> summaryKpiIndexMap) {
		LiveDriveReportWrapper driveReportWrapper = new LiveDriveReportWrapper();
		List<LiveDriveSubWrapper> driveSubWrappers = new ArrayList<>();
		driveSubWrapper.setVoiceList(setVoiceList(listArray, summaryKpiIndexMap));
		driveSubWrapper.setDataList(setDataList(listArray, summaryKpiIndexMap));
		driveSubWrappers.add(driveSubWrapper);
		driveReportWrapper.setNhi2DataList(driveSubWrappers);
		return driveReportWrapper;
	}

	private List<KPIStatsWrapper> setVoiceList(String[] listArray, Map<String, Integer> summaryKpiIndexMap) {
		return ReportUtil.getVoiceKpiWrappersList(listArray, summaryKpiIndexMap);
	}

	private List<KPIStatsWrapper> setDataList(String[] listArray, Map<String, Integer> summaryKpiIndexMap) {
		return ReportUtil.getDataKpiWrappersList(listArray, summaryKpiIndexMap);
	}
}

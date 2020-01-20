package com.inn.foresight.module.nv.report.service.impl;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.imageio.ImageIO;
import javax.ws.rs.core.Response;

import com.inn.foresight.module.nv.layer3.constants.Layer3PPEConstant;
import com.inn.foresight.module.nv.report.constant.ReportIndexWrapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inn.commons.Symbol;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.dao.IUnitDataDao;
import com.inn.foresight.core.report.dao.IAnalyticsRepositoryDao;
import com.inn.foresight.core.report.model.AnalyticsRepository;
import com.inn.foresight.core.report.model.AnalyticsRepository.progress;
import com.inn.foresight.module.nv.NVConstant;
import com.inn.foresight.module.nv.core.workorder.dao.impl.IGenericWorkorderDao;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;
import com.inn.foresight.module.nv.inbuilding.model.NVIBUnitResult;
import com.inn.foresight.module.nv.layer3.constants.QMDLConstant;
import com.inn.foresight.module.nv.layer3.dao.INVLayer3HDFSDao;
import com.inn.foresight.module.nv.layer3.service.INVLayer3DashboardService;
import com.inn.foresight.module.nv.report.dao.INVReportHbaseDao;
import com.inn.foresight.module.nv.report.dao.INVReportHdfsDao;
import com.inn.foresight.module.nv.report.ib.utils.IBReportUtils;
import com.inn.foresight.module.nv.report.service.IMapImagesService;
import com.inn.foresight.module.nv.report.service.INVIBWifiReportService;
import com.inn.foresight.module.nv.report.service.IReportService;
import com.inn.foresight.module.nv.report.utils.DriveHeaderConstants;
import com.inn.foresight.module.nv.report.utils.IBWifiConstants;
import com.inn.foresight.module.nv.report.utils.IBWifiReportUtils;
import com.inn.foresight.module.nv.report.utils.JsonMapParser;
import com.inn.foresight.module.nv.report.utils.ReportConstants;
import com.inn.foresight.module.nv.report.utils.ReportUtil;
import com.inn.foresight.module.nv.report.workorder.wrapper.DriveDataWrapper;
import com.inn.foresight.module.nv.report.workorder.wrapper.KPIWrapper;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.InbuildingPointWrapper;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.NoAccessWrapper;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.wifi.NVIBWifiReportWrapper;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.wifi.NVIBWifiSubReportWrapper;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;
import com.inn.foresight.module.nv.workorder.dao.IWORecipeMappingDao;

@Service("NVIBWifiReportServiceImpl")
public class NVIBWifiReportServiceImpl extends IBWifiReportUtils implements INVIBWifiReportService {
	private Logger logger = LogManager.getLogger(NVIBWifiReportServiceImpl.class);

	@Autowired
	private IAnalyticsRepositoryDao analyticsRepositoryDao;

	@Autowired
	private IGenericWorkorderDao iGenericWorkorderDao;

	@Autowired
	private INVLayer3DashboardService nvLayer3DashboardService;

	@Autowired
	private IReportService reportService;

	@Autowired
	private IMapImagesService mapImagesService;

	@Autowired
	private IWORecipeMappingDao woRecipeMappingDao;

	@Autowired
	private IUnitDataDao buildingUnitDao;

	@Autowired
	private INVReportHdfsDao nvReportHdfsDao;

	@Autowired
	private INVReportHbaseDao nvReportHbaseDao;

	@Autowired
	private INVLayer3HDFSDao nvLayer3HdfsDao;

	private List<String> filesToDeleteAfterExecution = new ArrayList<>();

	@Override
	@Transactional
	public Response execute(String json) {
		logger.info("Going to generate Inbuilding wifi report with data {}", json);
		Integer analyticrepositoryId = null;
		try {
			Map<String, Object> jsonMap = new JsonMapParser<String, Object>().convertJsonToMap(json);
			if (!jsonMap.isEmpty()) {
				AnalyticsRepository analyticsRepo = getAnalyticsRepoFromJson(jsonMap);
				analyticrepositoryId = analyticsRepo.getId();
				Integer workorderId = getWorkorderIdFromAnalyticsRepo(analyticsRepo);
				String filePathHdfs = ConfigUtils.getString(ReportConstants.NV_REPORTS_PATH_HDFS) + ReportConstants.INBUILDING
						+ ReportConstants.FORWARD_SLASH;
				if (workorderId != null) {
					GenericWorkorder genericWorkorders = iGenericWorkorderDao.findByPk(workorderId);
					
					boolean qmdlResponse = reportService.getFileProcessedStatusForWorkorders(Arrays.asList(workorderId));
					if (qmdlResponse) {
						String fileName = IBWifiConstants.IB_WIFI_REPORT_NAME_PREFIX + workorderId.toString()
								+ Symbol.UNDERSCORE_STRING + System.currentTimeMillis() + ReportConstants.PDF_EXTENSION;
						File file = generateInbuildingWifiReportAndReturnResponse(Arrays.asList(genericWorkorders),
								fileName);
						removeJunkFilesFromDisk(filesToDeleteAfterExecution);
						return saveFileAndUpdatesStatus(analyticsRepo.getId(), filePathHdfs, workorderId, file);
					}
				}
			}
		} catch (Exception e) {
			logger.error("Exception thrown at execute() in NVIBWifiReportServiceImpl {} ", Utils.getStackTrace(e));
			analyticsRepositoryDao.updateStatusInAnalyticsRepository(analyticrepositoryId,null,e.getMessage(),progress.Failed,null);
		}
		return Response	.ok(ForesightConstants.FAILURE_JSON)
						.build();
	}

	@Override
	@Transactional
	public File generateIBWIFIFloorLevelReport(List<NVIBUnitResult> nvIBunitResultList, String fileName)
			throws Exception {
		Map<GenericWorkorder, Map<Integer, List<String>>> workorderUnitRecipeMap = nvIBunitResultList	.stream()
																										.filter(nvIBUnitResult -> nvIBUnitResult.getWoRecipeMapping() != null)
																										.collect(
																												Collectors.groupingBy(
																														nvIBUnitResult -> nvIBUnitResult.getWoRecipeMapping()
																																						.getGenericWorkorder(),
																														Collectors.groupingBy(
																																nvIBUnitResult -> (Integer) nvIBUnitResult	.getUnit()
																																											.getId(),
																																Collectors.mapping(
																																		nvIBUnitResult -> (String) nvIBUnitResult	.getWoRecipeMapping()
																																													.getId()
																																													.toString(),
																																		Collectors.toList()))));
		List<NVIBWifiReportWrapper> reportWrapperList = new ArrayList<>();
		StringBuilder fileNameBuilder = new StringBuilder();
		for (Entry<GenericWorkorder, Map<Integer, List<String>>> woUnitRecipeEntry : workorderUnitRecipeMap.entrySet()) {
			fileNameBuilder.append(woUnitRecipeEntry.getKey()
													.getId()
													.toString()
					+ Symbol.UNDERSCORE_STRING);
			reportWrapperList.add(
					generateReportUnitRecipeWise(woUnitRecipeEntry.getKey(), woUnitRecipeEntry.getValue()));
		}
		File file = proceedToCreateReport(reportWrapperList, fileName);
		removeJunkFilesFromDisk(filesToDeleteAfterExecution);
		return file;
	}

	@Override
	@Transactional
	public File generateIBWIFIBuildingLevelReport(Integer buildingId, String fileName) {
		try {
			List<Integer> workorderIds = iGenericWorkorderDao.getListOfWorkOrderIdByBuildingId(buildingId,
					ReportConstants.TECHNOLOGY_WIFI);
			File file = generateInbuildingWifiReportAndReturnResponse(iGenericWorkorderDao.findByIds(workorderIds),
					fileName);
			removeJunkFilesFromDisk(filesToDeleteAfterExecution);
			return file;
		} catch (Exception e) {
			logger.error("Exception thrown at generateIBWIFIBuildingLevelReport() in NVIBWifiReportServiceImpl {} ",
					Utils.getStackTrace(e));
		}
		return null;
	}

	@Override
	@Transactional
	public File generateIBWIFITaskLevelReport(Integer recipeId, Integer workorderId, String fileName) {
		try {
			GenericWorkorder genericWorkorder = iGenericWorkorderDao.findByPk(workorderId);
			List<NVIBWifiReportWrapper> reportWrapperList = new ArrayList<>();
			Map<String, Integer> recipeUnitIdMap = new JsonMapParser<String, Integer>().convertJsonToMap(
					genericWorkorder.getGwoMeta()
									.get(ReportConstants.RECIPE_UNITID_MAP));
			Integer unitId = recipeUnitIdMap.get(recipeId.toString());
			Map<Integer, List<String>> unitRecipeMap = new HashMap<>();
			unitRecipeMap.put(unitId, Arrays.asList(recipeId.toString()));
			reportWrapperList.add(generateReportUnitRecipeWise(genericWorkorder, unitRecipeMap));
			File file = proceedToCreateReport(reportWrapperList, fileName);
			removeJunkFilesFromDisk(filesToDeleteAfterExecution);
			return file;
		} catch (Exception e) {
			logger.error("Exception thrown at generateIBWIFITaskLevelReport() in NVIBWifiReportServiceImpl {} ",
					Utils.getStackTrace(e));
		}
		return null;
	}

	private File generateInbuildingWifiReportAndReturnResponse(List<GenericWorkorder> genericWorkorders,
			String fileName) throws Exception {
		logger.info("Inside method generateInbuildingWifiReportAndReturnResponse {}", genericWorkorders);
		if (genericWorkorders != null && !genericWorkorders.isEmpty()) {
			List<NVIBWifiReportWrapper> reportWrapperList = new ArrayList<>();
			for (GenericWorkorder genericWorkorder : genericWorkorders) {
				Map<String, List<String>> recipeOperatorDetailMap = nvLayer3DashboardService.getDriveRecipeDetail(
						genericWorkorder.getId());
				List<String> recipeList = recipeOperatorDetailMap.get(QMDLConstant.RECIPE);
				Map<String, Integer> recipeUnitIdMap = new JsonMapParser<String, Integer>().convertJsonToMap(
						genericWorkorder.getGwoMeta()
										.get(ReportConstants.RECIPE_UNITID_MAP));
				Map<Integer, List<String>> unitRecipeMap = getUnitWiseRecipeListMap(recipeList, recipeUnitIdMap);
				reportWrapperList.add(generateReportUnitRecipeWise(genericWorkorder, unitRecipeMap));
			}
			return proceedToCreateReport(reportWrapperList, fileName);
		}
		return null;
	}

	private NVIBWifiReportWrapper generateReportUnitRecipeWise(GenericWorkorder genericWorkorder,
			Map<Integer, List<String>> unitRecipeMap) throws Exception {
		List<NVIBWifiSubReportWrapper> resultDataList = new ArrayList<>();
		logger.info("unitRecipeMap: {}", unitRecipeMap);
		for (Entry<Integer, List<String>> unitRecipeEntry : unitRecipeMap.entrySet()) {
			for (String recipe : unitRecipeEntry.getValue()) {
				NVIBWifiSubReportWrapper reportSubWrapper = new NVIBWifiSubReportWrapper();
				reportSubWrapper.setSurveyDetailsList(
						getSurveyDetailsPageData(buildingUnitDao.findByPk(unitRecipeEntry.getKey()),
								woRecipeMappingDao.getWORecipeMappingById(Integer.parseInt(recipe))));
				/** fetch drive data recipe wise */
				logger.info("going to get drive data for workorderid: {}", genericWorkorder.getId());
				Set<String> dynamicKpis = reportService.getDynamicKpiName(Arrays.asList(genericWorkorder.getId()),
												null, Layer3PPEConstant.ADVANCE);

				List<String> fetchKPIList = ReportIndexWrapper.getLiveDriveKPIs()
						.stream()
						.filter(k -> dynamicKpis.contains(k))
						.collect(Collectors.toList());

				Map<String, Integer> kpiIndexMap = ReportIndexWrapper.getLiveDriveKPIIndexMap(fetchKPIList);

				List<String[]> recipeDriveData = reportService.getDriveDataForReport(genericWorkorder.getId(),
													Arrays.asList(recipe), fetchKPIList);

				reportSubWrapper.setSurveyResultDataList(getSpeedTestDataListForReport(recipeDriveData, kpiIndexMap));
				Map<String, String> plotImages = getPlotImagesForReport(genericWorkorder.getId(), recipeDriveData,
						recipe, IBWifiConstants.IB_WIFI_OPERATOR_NAME, reportSubWrapper, kpiIndexMap);

				reportSubWrapper.setBand(getBandForRecipeData(recipeDriveData, kpiIndexMap.get(ReportConstants.BAND)));
				if (MapUtils.isNotEmpty(plotImages)) {
					setPlotImagesToReportWrapper(plotImages, reportSubWrapper);

					if (plotImages.get(IBWifiConstants.KEY_FLOOR_PLAN_IMAGE) != null) {
						reportSubWrapper.setFloorPlanOverviewList(
								getFloorPlanOverviewData(plotImages.get(IBWifiConstants.KEY_FLOOR_PLAN_IMAGE), null, null));
					}
					reportSubWrapper.setDepAndApConfigList(getAPConfigList());
					reportSubWrapper.setApConfigTableList(getAPConfigTableListData(recipeDriveData, kpiIndexMap));
					reportSubWrapper.setOverallSignalDataList(getKpiDataList(removeSpeedTestDataFromRecipeData(recipeDriveData,
							kpiIndexMap.get(ReportConstants.SPEED_TEST_PIN_NUMBER)), kpiIndexMap.get(ReportConstants.WIFI_RSSI),
							IBWifiConstants.OVERALL_SIGNAL_PERCENTAGE_THRESHOLD, IBWifiConstants.DESIRED_SIGNAL_THRESHOLD));
					reportSubWrapper.setSnrDataList(getKpiDataList(removeSpeedTestDataFromRecipeData(recipeDriveData,
							kpiIndexMap.get(ReportConstants.SPEED_TEST_PIN_NUMBER)), kpiIndexMap.get(ReportConstants.WIFI_SNR),
							IBWifiConstants.SNR_PERCENTAGE_THRESHOLD, IBWifiConstants.MIN_SNR_THRESHOLD));
					reportSubWrapper.setDlThroughputList(getKpiDataList(removeSpeedTestDataFromRecipeData(recipeDriveData,
							kpiIndexMap.get(ReportConstants.SPEED_TEST_PIN_NUMBER)), kpiIndexMap.get(ReportConstants.DL_THROUGHPUT),
							IBWifiConstants.DL_PERCENTAGE_THRESHOLD, IBWifiConstants.MIN_DOWNLOAD_THRESHOLD));
					reportSubWrapper.setUlThroughputList(getKpiDataList(removeSpeedTestDataFromRecipeData(recipeDriveData,
							kpiIndexMap.get(ReportConstants.SPEED_TEST_PIN_NUMBER)), kpiIndexMap.get(ReportConstants.UL_THROUGHPUT),
							IBWifiConstants.UL_PERCENTAGE_THRESHOLD, IBWifiConstants.MIN_UPLOAD_THRESHOLD));
					resultDataList.add(reportSubWrapper);
				}
			}
		}
		NVIBWifiReportWrapper reportWrapper = new NVIBWifiReportWrapper();
		reportWrapper.setReportDataList(resultDataList);
		return reportWrapper;
	}

	private AnalyticsRepository getAnalyticsRepoFromJson(Map<String, Object> jsonMap) {
		logger.info("Inside method getAnalyticsRepoFromJson {}", jsonMap);
		try {
			Integer analyticsrepoId = (Integer) jsonMap.get(ForesightConstants.ANALYTICAL_REPORT_KEY);
			logger.info("analyticsrepositoryId {} ", analyticsrepoId);
			return analyticsRepositoryDao.findByPk(analyticsrepoId);
		} catch (Exception e) {
			logger.error("Error inside getAnalyticsRepoFromJson method in NVIBBenchmarkReportServiceImpl{} ",
					Utils.getStackTrace(e));
			throw new RestException("Unable to get Analytics Repo");
		}
	}

	private Map<String, String> getPlotImagesForReport(Integer workorderId, List<String[]> dataList, String recipe,
													   String operator, NVIBWifiSubReportWrapper reportSubWrapper, Map<String, Integer> kpiIndexMap) {
		logger.info("inside getPlotImagesForReport for recipe {}",recipe);
		Map<String, String> plotImagesMap = new HashMap<>();
		String localDirPath = ConfigUtils.getString(ReportConstants.IB_BENCHMARK_REPORT_TEMP_PATH);
		DriveDataWrapper driveDataWrapper = nvLayer3DashboardService.getFloorplanDataFromLayer3ReportForFramework(workorderId, recipe);

		if (driveDataWrapper != null) {
			String floorPlanImage = getFloorPlanImageForRecipe(workorderId, recipe, operator, driveDataWrapper);

			if (floorPlanImage != null) {
				plotImagesMap.put(IBWifiConstants.KEY_FLOOR_PLAN_IMAGE, floorPlanImage);

                InbuildingPointWrapper pointWrapper = IBReportUtils	.getInstance().drawFloorPlan(floorPlanImage, driveDataWrapper.getJson(),
						dataList, kpiIndexMap.get(ReportConstants.X_POINT), kpiIndexMap.get(ReportConstants.Y_POINT));

				List<KPIWrapper> kpiWrapperList = reportService.getKpiWrapperListForReport(workorderId, Arrays.asList(recipe), operator,
						IBWifiConstants.KEY_LEGEND_INBUILDING_WIFI, kpiIndexMap);
				if (pointWrapper != null) {
					plotImagesMap.putAll(
							getImagesForReport(floorPlanImage, localDirPath, pointWrapper.getArlist(), kpiWrapperList, kpiIndexMap));
				}
				List<DriveDataWrapper> noAccessDataList = nvReportHbaseDao.getNoAcessDataFromLayer3Report(workorderId, recipe);
				if (noAccessDataList != null && !noAccessDataList.isEmpty()) {
					setNoAccessDataList(noAccessDataList, floorPlanImage, reportSubWrapper, pointWrapper, localDirPath);
				}
			}
		}
		filesToDeleteAfterExecution.addAll(plotImagesMap.values());
		return plotImagesMap;
	}

	private Map<String, String>  getImagesForReport(String imgFloorPlan, String localDirPath, List<String[]> arlist,
												   List<KPIWrapper> kpiList, Map<String, Integer> kpiIndexMap) {
		List<String[]> dataListWithoutSpeedTestData = removeSpeedTestDataFromRecipeData(arlist, kpiIndexMap.get(ReportConstants.SPEED_TEST_PIN_NUMBER));
		Map<String, String> heatMaps = InBuildingHeatMapGenerator	.getInstance()
																	.generateHeatMapsForReport(dataListWithoutSpeedTestData,
																			localDirPath, imgFloorPlan, kpiList, kpiIndexMap);
		heatMaps.putAll(getLegendImages(dataListWithoutSpeedTestData, kpiList));

		List<KPIWrapper> collect = kpiList.stream().filter(data -> data.getKpiName().equalsIgnoreCase(IBWifiConstants.KEY_WIFI_RSSI_IMAGE)).collect(Collectors.toList());

		if (CollectionUtils.isNotEmpty(collect)) {
			File speedTestImageFile = InBuildingHeatMapGenerator.getInstance()
																.generateSpeedTestImage(arlist, imgFloorPlan,
																		collect.get(IBWifiConstants.LIST_INDEX_ZERO),
																		ConfigUtils.getString(
																				ReportConstants.IB_BENCHMARK_REPORT_TEMP_PATH), kpiIndexMap);
			if (speedTestImageFile != null) {
				heatMaps.put(IBWifiConstants.KEY_SPEED_TEST_IMAGE, speedTestImageFile.getAbsolutePath());
			}
		}
		String surveyPathImage = InBuildingHeatMapGenerator	.getInstance() .getSurveyPathWithColor(dataListWithoutSpeedTestData,
									Color.GRAY, localDirPath, imgFloorPlan,
									kpiIndexMap.get(ReportConstants.X_POINT), kpiIndexMap.get(ReportConstants.Y_POINT));
		heatMaps.put(IBWifiConstants.KEY_SURVEY_PATH_IMAGE, surveyPathImage);
		return heatMaps;
	}

	private String getFloorPlanImageForRecipe(Integer workorderId, String recipe, String operator,
											  DriveDataWrapper driveDataWrapper) {
		try {
			return reportService.getFloorplanImg(workorderId, recipe, operator, driveDataWrapper);
		} catch (IOException e) {
			logger.error("IOExceptionwhile fteching floor plan image from method getFloorPlanImageForRecipe{} ",
					Utils.getStackTrace(e));
		}
		return null;
	}

	private HashMap<String, String> getLegendImages(List<String[]> arList, List<KPIWrapper> kpiList) {
		HashMap<String, BufferedImage> bufferdImageMap = mapImagesService.getLegendImagesForIBBenchmark(kpiList,
				arList);
		return mapImagesService.saveDriveImages(bufferdImageMap,
				ConfigUtils.getString(ReportConstants.IB_BENCHMARK_REPORT_TEMP_PATH), false);
	}

	private Response saveFileAndUpdatesStatus(Integer reportinstanceId, String filePath, Integer workorderId,
			File file) {
		if (file != null) {
			filePath += file.getName();
			if (nvReportHdfsDao.saveFileToHdfs(file, filePath)) {
				logger.info("File saved Successfully ");
				analyticsRepositoryDao.updateStatusInAnalyticsRepository(reportinstanceId, filePath,
						ReportConstants.HDFS, progress.Generated, null);
				logger.info("Status in report Instance is updated ");
				GenericWorkorder genericWorkorder = iGenericWorkorderDao.findByPk(workorderId);
				Map<String, String> geoMap = genericWorkorder.getGwoMeta();
				geoMap.put(NVWorkorderConstant.REPORT_INSTACE_ID, reportinstanceId.toString());
				genericWorkorder.setGwoMeta(geoMap);
				iGenericWorkorderDao.update(genericWorkorder);
				return Response	.ok(ForesightConstants.SUCCESS_JSON)
								.build();
			} else {
				return Response	.ok(ForesightConstants.FAILURE_JSON)
								.build();
			}
		} else {
			return Response	.ok(NVConstant.REPORT_NOT_FOUND_JSON)
							.build();
		}
	}

	private void setNoAccessDataList(List<DriveDataWrapper> noAccessDataList, String imgFloorPlan,
			NVIBWifiSubReportWrapper reportSubWrapper, InbuildingPointWrapper pointWrapper, String localDirPath) {
		List<NoAccessWrapper> noAccessWrapperList = new ArrayList<>();
		try {
			int i = ReportConstants.INDEX_ZER0;
			for (DriveDataWrapper noAcessData : noAccessDataList) {
				NoAccessWrapper noAccesswrapper = new NoAccessWrapper();
				String floorplanimg = InBuildingHeatMapGenerator.getInstance()
																.drawNoAcessImage(imgFloorPlan,
																		localDirPath + ReportConstants.NOACCESS + i
																				+ Symbol.UNDERSCORE_STRING
																				+ System.currentTimeMillis()
																				+ ReportConstants.DOT_PNG,
																		noAcessData, pointWrapper);
				if (noAcessData.getImageName() != null) {
					if (new File(floorplanimg).exists()) {
						noAccesswrapper.setFloorPlanImgPath(floorplanimg);
						filesToDeleteAfterExecution.add(floorplanimg);
					}

					BufferedImage img = getNoAccessImg(noAcessData);
					if (img != null) {
						ImageIO.write(img, ReportConstants.JPG, new File(localDirPath + noAcessData.getImageName()));
						noAccesswrapper.setNoAccessImgPath(localDirPath + noAcessData.getImageName());
						filesToDeleteAfterExecution.add(localDirPath + noAcessData.getImageName());
						noAccessWrapperList.add(noAccesswrapper);
					}
					i++;
				}
			}

			reportSubWrapper.setNoAccessDataList(noAccessWrapperList);
		} catch (Exception e) {
			logger.warn("unable to set No Aceess img  {}", Utils.getStackTrace(e));
		}
	}

	private BufferedImage getNoAccessImg(DriveDataWrapper driveDataWrapper) {
		try {
			BufferedImage imBuff = null;
			String localFilepath = nvLayer3HdfsDao.copyFileFromHdfsToLocalPath(driveDataWrapper.getFilePath(),
					ConfigUtils.getString(ReportConstants.IBREPORT_FLOORPLAN_IMAGE_PATH),
					ReportConstants.NOACCESS + ReportConstants.DOT_ZIP);
			logger.info("Going to process zip file {}  ", localFilepath);
			try (ZipFile zipFile = new ZipFile(localFilepath)) {
				Enumeration<? extends ZipEntry> entries = zipFile.entries();
				while (entries.hasMoreElements()) {
					ZipEntry entry = entries.nextElement();
					if (driveDataWrapper.getImageName()
										.contains(QMDLConstant.NO_ACCESS_IMAGE_PREFIX)
							&& entry.getName()
									.contains(driveDataWrapper.getImageName())) {
						logger.info("inside the no access img " + driveDataWrapper.getImageName());
						InputStream is = zipFile.getInputStream(entry);
						imBuff = ImageIO.read(is);
					}
				}
			}
			return imBuff;
		} catch (Exception e) {
			logger.error("Exception inside method getBgImagePath {} ", e.getMessage());
		}
		return null;
	}
}
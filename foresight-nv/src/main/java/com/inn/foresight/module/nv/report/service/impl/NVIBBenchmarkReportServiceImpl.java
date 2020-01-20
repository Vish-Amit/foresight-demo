package com.inn.foresight.module.nv.report.service.impl;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.imageio.ImageIO;
import javax.ws.rs.core.Response;

import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inn.commons.Symbol;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.lang.NumberUtils;
import com.inn.commons.lang.StringUtils;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.dao.IUnitDataDao;
import com.inn.foresight.core.infra.model.Unit;
import com.inn.foresight.core.report.dao.IAnalyticsRepositoryDao;
import com.inn.foresight.core.report.model.AnalyticsRepository;
import com.inn.foresight.core.report.model.AnalyticsRepository.progress;
import com.inn.foresight.module.nv.core.workorder.dao.impl.IGenericWorkorderDao;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;
import com.inn.foresight.module.nv.layer3.constants.Layer3PPEConstant;
import com.inn.foresight.module.nv.layer3.constants.QMDLConstant;
import com.inn.foresight.module.nv.layer3.dao.INVLayer3HDFSDao;
import com.inn.foresight.module.nv.layer3.service.INVLayer3DashboardService;
import com.inn.foresight.module.nv.report.constant.ReportIndexWrapper;
import com.inn.foresight.module.nv.report.dao.INVReportHbaseDao;
import com.inn.foresight.module.nv.report.dao.INVReportHdfsDao;
import com.inn.foresight.module.nv.report.ib.utils.IBReportUtils;
import com.inn.foresight.module.nv.report.service.IMapImagesService;
import com.inn.foresight.module.nv.report.service.INVIBBenchmarkReportService;
import com.inn.foresight.module.nv.report.service.IReportService;
import com.inn.foresight.module.nv.report.utils.MergePDF;
import com.inn.foresight.module.nv.report.utils.ReportConstants;
import com.inn.foresight.module.nv.report.utils.ReportUtil;
import com.inn.foresight.module.nv.report.workorder.wrapper.DriveDataWrapper;
import com.inn.foresight.module.nv.report.workorder.wrapper.KPIWrapper;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.InbuildingPointWrapper;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.NoAccessWrapper;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.benchmark.IBBenchMarkReportWrapper;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.benchmark.IBBenchmarkGraphDataWrapper;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.benchmark.IBBenchmarkGraphPlotWrapper;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.benchmark.IBBenchmarkImagePlotWrapper;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.benchmark.IBBenchmarkKPIAnalysisWrapper;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.benchmark.IBBenchmarkKPIDataWrapper;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.benchmark.IBBenchmarkOverallDataWrapper;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.benchmark.IBBenchmarkSummaryKpiWrapper;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.benchmark.IBBenchmarkSummaryWrapper;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.benchmark.IBKPIWrapper;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.benchmark.IBLteThreeGDataWrapper;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.benchmark.IBMappingwrapper;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.benchmark.KPI;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;
import com.inn.foresight.module.nv.workorder.dao.IWOFileDetailDao;
import com.inn.foresight.module.nv.workorder.dao.IWORecipeMappingDao;
import com.inn.foresight.module.nv.workorder.model.WOFileDetail;
import com.inn.foresight.module.nv.workorder.model.WORecipeMapping;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service("NVIBBenchmarkReportServiceImpl")

public class NVIBBenchmarkReportServiceImpl extends ReportUtil implements INVIBBenchmarkReportService {
	private Logger logger = LogManager.getLogger(NVIBBenchmarkReportServiceImpl.class);
	@Autowired
	private IAnalyticsRepositoryDao analyticsrepositoryDao;

	@Autowired
	private IGenericWorkorderDao iGenericWorkorderDao;

	@Autowired
	private INVLayer3DashboardService nvLayer3dashboardService;

	@Autowired
	private INVLayer3HDFSDao nvLayer3hdfsDao;

    @Autowired
	private INVReportHdfsDao nvReportHdfsDao;

	@Autowired
	private IMapImagesService mapImagesService;

	@Autowired
	private IWORecipeMappingDao woRecipeMappingDao;

	@Autowired
	private IUnitDataDao buildingUnitDao;

	@Autowired
	private IWOFileDetailDao woFileDetailDao;

	@Autowired
	private INVReportHbaseDao nvReportHbaseDao;
	
	@Autowired
	private IReportService reportService;

	List<String> junkFiles = new ArrayList<>();
	
	@Transactional
	@Override
	public Response execute(String json) {
		Integer workorderId =null;
		Integer analyticrepositoryId = null;
		try {
			Map<String, Object> jsonMap = new ObjectMapper().readValue(json, HashMap.class);
			AnalyticsRepository analyticsRepo = getAnalyticsRepoFromJson(jsonMap);
			analyticrepositoryId = analyticsRepo.getId();
			workorderId = getWorkorderIdFromAnalyticsRepo(analyticsRepo);
			GenericWorkorder genericWorkorder = getGenericWOFromWorkorderId(workorderId);
			logger.info("WorkorderId is: {}", workorderId);

			boolean isFilesProcessed = reportService.getFileProcessedStatusForWorkorders(Arrays.asList(workorderId));
			if (isFilesProcessed) {
				List<File> pdfFileList = new ArrayList<>();
				Map<String, Map<String, Map<String, String>>> recipeUnitOperatorMap = getRecipeUnitOperatorMapforWorkorder(
						genericWorkorder);
				Entry<String, Map<String, Map<String, String>>> unitId = recipeUnitOperatorMap.entrySet()
																							  .iterator()
																							  .next();
				Map<String, Object> paramMap = getBuildingDetailsByUnitId(unitId);
				paramMap.put("date", ReportUtil.getFormattedDate(genericWorkorder.getCreationTime(),
						ReportConstants.DATE_FORMAT_DD_MM_YYYY_HH_MM_SS));
				File initFile = proceedToCreateReport(new IBBenchMarkReportWrapper(), paramMap,
						ReportConstants.IB_BENCHMARK_REPORT_NAME_PREFIX + workorderId + System.currentTimeMillis()
								+ ReportConstants.PDF_EXTENSION, ReportConstants.IB_BENCHMARK_INITIAL_JASPER_FILE_NAME);
				if (initFile != null) {
					pdfFileList.add(initFile);
					junkFiles.add(initFile.getAbsolutePath());
				}
				addUnitWiseReportInReportList(workorderId, pdfFileList, recipeUnitOperatorMap);
				File thankYouFile = proceedToCreateReport(new IBBenchMarkReportWrapper(), paramMap,
						ReportConstants.IB_BENCHMARK_REPORT_NAME_PREFIX + workorderId + System.currentTimeMillis()
								+ ReportConstants.PDF_EXTENSION,
						ReportConstants.IB_BENCHMARK_THANK_YOU_PAGE_JASPER_FILE_NAME);
				if (thankYouFile != null) {
					pdfFileList.add(thankYouFile);

					junkFiles.add(thankYouFile.getAbsolutePath());
				}
				String filePath = ConfigUtils.getString(ReportConstants.NV_REPORTS_PATH) + ReportConstants.IB_BENCHMARK
						+ ReportConstants.FORWARD_SLASH;
				String filePathHdfs =
						ConfigUtils.getString(ReportConstants.NV_REPORTS_PATH_HDFS) + ReportConstants.IB_BENCHMARK
								+ ReportConstants.FORWARD_SLASH;
				File file = new File(filePath);
				if (!file.exists()) {
					file.mkdirs();
				}
				String fileName =
						ReportConstants.IB_BENCHMARK_REPORT_NAME_PREFIX + workorderId + ReportConstants.PDF_EXTENSION;
				MergePDF.mergeFiles(filePath + fileName, pdfFileList);
				saveFileAndUpdateStatus(analyticsRepo.getId(), filePathHdfs, genericWorkorder,
						ReportUtil.getIfFileExists(filePath + fileName));
				junkFiles.add(filePath + fileName);
				removeJunkFilesFromDisk();
			}
		} catch (Exception e) {
			logger.error("Error inside execute method in NVIBBenchmarkReportServiceImpl for workOrder Id {} , msg is  {} ",workorderId,e.getMessage());
			analyticsrepositoryDao.updateStatusInAnalyticsRepository(analyticrepositoryId,null,e.getMessage(),progress.Failed,null);
		}

		return null;
	}

	private void addUnitWiseReportInReportList(Integer workorderId, List<File> pdfFileList,
			Map<String, Map<String, Map<String, String>>> recipeUnitOperatorMap) {
		for (Entry<String, Map<String, Map<String, String>>> unitRecipeEntry : recipeUnitOperatorMap.entrySet()) {
			String unitName = null;
			List<String> fetchKPIList = null;
			Map<String, Integer> kpiIndexMap = null;
			try {
				unitName = buildingUnitDao.findByPk(Integer.parseInt(unitRecipeEntry.getKey())).getUnitName();
				logger.info("going to get drive data for workorderid: {}", workorderId);
				Set<String> dynamicKpis = reportService.getDynamicKpiName(Arrays.asList(workorderId),
						null, Layer3PPEConstant.ADVANCE);

				fetchKPIList = ReportIndexWrapper.getLiveDriveKPIs()
						.stream()
						.filter(k -> dynamicKpis.contains(k))
						.collect(Collectors.toList());
				kpiIndexMap = ReportIndexWrapper.getLiveDriveKPIIndexMap(fetchKPIList);

			} catch (NumberFormatException e) {
				logger.error("NumberFormatException inside the methd addUnitWiseReportInReportList {}",e.getMessage());
			} catch (DaoException e) {
				logger.error("DaoException inside the methd addUnitWiseReportInReportList {}",e.getMessage());
			} catch (IllegalAccessException e) {
				logger.error("IllegalAccessException inside the methd addUnitWiseReportInReportList {}",e.getMessage());
			} catch (NoSuchFieldException e) {
				logger.error("NoSuchFieldException inside the methd addUnitWiseReportInReportList {}",e.getMessage());
			}


			for (Entry<String, Map<String, String>> recipeNameEntry : unitRecipeEntry.getValue().entrySet()) {
				Map<String, List<String[]>> inBuildingDataMap = getInBuildingDataForWorkorderId(workorderId,
						recipeNameEntry.getValue(), fetchKPIList);

				Map<String, Object> reportParamMap = new HashMap<>();

				reportParamMap.put("unit name", unitName);
				reportParamMap.put("recipe name", recipeNameEntry.getKey());

				IBBenchMarkReportWrapper reportWrapper = getBenchmarkReportWrapperFromRecipeData(inBuildingDataMap,
						workorderId, recipeNameEntry.getValue(), reportParamMap, kpiIndexMap);

				File file = proceedToCreateReport(reportWrapper,
						reportParamMap, ReportConstants.IB_BENCHMARK_REPORT_NAME_PREFIX + workorderId
								+ System.currentTimeMillis() + ReportConstants.PDF_EXTENSION,
						ReportConstants.MAIN_JASPER);
				if (file != null) {
					pdfFileList.add(file);
					junkFiles.add(file.getAbsolutePath());
				}
			}
		}
	}

	private Map<String, Object> getBuildingDetailsByUnitId(Entry<String, Map<String, Map<String, String>>> unitId) {
		Unit unit = buildingUnitDao.findByPk(Integer.parseInt(unitId.getKey()));

		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("building name", unit.getFloor().getWing().getBuilding().getBuildingName());
		paramMap.put("wing name", unit.getFloor().getWing().getWingName());
		paramMap.put("floor name", unit.getFloor().getFloorName());

		Set<String> operatorSet = unitId.getValue().entrySet().iterator().next().getValue().keySet();
		int operatorCount = 1;
		for (String operator : operatorSet) {
			paramMap.put("operator" + operatorCount, operator);
			operatorCount++;
		}

		return paramMap;
	}
	
	private void removeJunkFilesFromDisk() {
		if(junkFiles != null && !junkFiles.isEmpty()) {
			for(String filePath : junkFiles) {
				File file = new File(filePath);
				if(file.exists() && file.isFile()) {
					boolean delete = file.delete();
					logger.info("Delete file {}",delete);
				}
			}
		}
	}

	private File proceedToCreateReport(IBBenchMarkReportWrapper mainWrapper, Map<String, Object> parameterMap,
			String fileName, String jasperMainFile) {
		try {
			List<IBBenchMarkReportWrapper> dataSourceList = new ArrayList<>();
			dataSourceList.add(mainWrapper);
			JRBeanCollectionDataSource rfbeanColDataSource = new JRBeanCollectionDataSource(dataSourceList);
			String DESTINATION_FILE_NAME = ConfigUtils.getString(ReportConstants.IB_BENCHMARK_GENERATED_REPORT_PATH)
					+ fileName;
			String REPORT_ASSET_REPO = ConfigUtils.getString(ReportConstants.IB_BENCHMARK_JASPER_FILE_PATH);
			parameterMap.put(ReportConstants.SUBREPORT_DIR, REPORT_ASSET_REPO);
			parameterMap.put(ReportConstants.IMAGE_FIRST, REPORT_ASSET_REPO + ReportConstants.IMAGE_FIRST_IMG);
			parameterMap.put(ReportConstants.IMAGE_THANKU, REPORT_ASSET_REPO + ReportConstants.IMAGE_THANKU_IMG);
			parameterMap.put(ReportConstants.PIE_CHART, REPORT_ASSET_REPO + ReportConstants.PIE_CHART_IMG);
			parameterMap.put(ReportConstants.IMAGE_PARAM_HEADER_BG,
					REPORT_ASSET_REPO + ReportConstants.IMAGE_HEADER_BG);
			parameterMap.put(ReportConstants.IMAGE_PARAM_SCREEN_BG,
					REPORT_ASSET_REPO + ReportConstants.IMAGE_SCREEN_BG);

			JasperRunManager.runReportToPdfFile(REPORT_ASSET_REPO + jasperMainFile, DESTINATION_FILE_NAME, parameterMap,
					rfbeanColDataSource);

			return ReportUtil.getIfFileExists(DESTINATION_FILE_NAME);
		} catch (JRException e) {
			logger.error("Error inside proceedToCreateReport method in NVIBBenchmarkReportServiceImpl{} ",
					Utils.getStackTrace(e));
		}

		return null;
	}

	private AnalyticsRepository getAnalyticsRepoFromJson(Map<String, Object> jsonMap){
		try {
			Integer analyticsrepoId = (Integer) jsonMap.get(ForesightConstants.ANALYTICAL_REPORT_KEY);
			logger.info("analyticsrepositoryId {} ", analyticsrepoId);
			return analyticsrepositoryDao.findByPk(analyticsrepoId);
		} catch (Exception e) {
			logger.error("Error inside getAnalyticsRepoFromJson method in NVIBBenchmarkReportServiceImpl{} ",
					Utils.getStackTrace(e));
			throw new RestException("Unable to get Analytics Repo");
		}
	}

	private Response saveFileAndUpdateStatus(Integer analyticsrepoId, String filePath,
			GenericWorkorder genericWorkorder, File file) {
		logger.info("Inside method going to saveFileAndUpdateStatus filePath {} ", filePath);
		if (file != null) {
			filePath += file.getName();
			if (nvReportHdfsDao.saveFileToHdfs(file, filePath)) {
				logger.info("File saved Successfully");
				analyticsrepositoryDao.updateStatusInAnalyticsRepository(analyticsrepoId, filePath,
						ReportConstants.HDFS, progress.Generated, null);
				logger.info("Status in analytics repository is updated");
				Map<String, String> geoMap = genericWorkorder.getGwoMeta();
				geoMap.put(NVWorkorderConstant.REPORT_INSTACE_ID, analyticsrepoId.toString());
				genericWorkorder.setGwoMeta(geoMap);
				iGenericWorkorderDao.update(genericWorkorder);
				return Response.ok(ForesightConstants.SUCCESS_JSON).build();
			} else {
				return Response.ok(ForesightConstants.FAILURE_JSON).build();
			}
		}
		return Response.ok(ForesightConstants.FAILURE_JSON).build();
	}

	private Integer getWorkorderIdFromAnalyticsRepo(AnalyticsRepository analyticrepositoryObj) {
		try {
			Map<String, Object> configMap = new ObjectMapper().readValue(analyticrepositoryObj.getReportConfig().replaceAll("\'", "\""),
					HashMap.class);
			logger.info("AnalyticsRepository jsonObject {} ", configMap);
			Integer workorderId = (Integer) configMap.get(ReportConstants.WORKORDER_ID);
			logger.info("going to invoke report generation method for workOrderId {} ", workorderId);
			return workorderId;
		} catch (Exception e) {
			logger.error("Error inside getWorkorderIdFromJson method in NVIBBenchmarkReportServiceImpl{} ",
					Utils.getStackTrace(e));
		}
		return null;
	}

	private Map<String, String> getRecipeOperatorMapforWorkorder(GenericWorkorder genericWorkorder) {
		try {
			return new ObjectMapper().readValue(
					genericWorkorder.getGwoMeta().get(ReportConstants.KEY_RECIPE_OPERATOR_MAP), HashMap.class);
		} catch (Exception e) {
			logger.error("Error inside getRecipeOperatorMapforWorkorderId method in NVIBBenchmarkReportServiceImpl{} ",
					Utils.getStackTrace(e));
		}
		return null;
	}

	private Map<String, Map<String, Map<String, String>>> getRecipeUnitOperatorMapforWorkorder(
			GenericWorkorder genericWorkorder) {
		Map<String, Map<String, Map<String, String>>> recipeUnitOperatorMap = new HashMap<>();

		try {
			List<Integer> woRecipeMappingList = getWoRecipeListFromWOFileDetail(genericWorkorder);

			Map<Integer, WORecipeMapping> recipeMap = getWoRecipeMap(genericWorkorder);
			Map<String, String> recipeOperatorMap = new ObjectMapper().readValue(
					genericWorkorder.getGwoMeta().get(ReportConstants.RECIPE_OPERATOR_MAP), HashMap.class);

			Map<String, Integer> recipeUnitMap = new ObjectMapper().readValue(
					genericWorkorder.getGwoMeta().get(ReportConstants.RECIPE_UNITID_MAP), HashMap.class);

			for (Entry<String, Integer> recipeUnitEntry : recipeUnitMap.entrySet()) {
				int recipeId = Integer.parseInt(recipeUnitEntry.getKey());
				WORecipeMapping woFileDetail = recipeMap.get(recipeId);
				String operator = recipeOperatorMap.get(recipeUnitEntry.getKey());
				if (woRecipeMappingList.contains(recipeId)) {
					if (recipeUnitOperatorMap.containsKey(recipeUnitEntry.getValue().toString())) {
						Map<String, Map<String, String>> recipeNameOperatorMap = recipeUnitOperatorMap.get(
								recipeUnitEntry.getValue().toString());
						putRecipeAndOperatorIntoMap(recipeUnitEntry, woFileDetail, operator, recipeNameOperatorMap);
					} else {
						putUnitIntoMap(recipeUnitOperatorMap, recipeUnitEntry, woFileDetail, operator);
					}
				}
			}
		} catch (Exception e) {
			logger.error("Error inside getRecipeOperatorMapforWorkorderId method in NVIBBenchmarkReportServiceImpl{} ",
					Utils.getStackTrace(e));
		}
		return recipeUnitOperatorMap;
	}

	private void putUnitIntoMap(Map<String, Map<String, Map<String, String>>> recipeUnitOperatorMap,
			Entry<String, Integer> recipeUnitEntry, WORecipeMapping woFileDetail, String operator) {
		Map<String, String> operatorMap = new HashMap<>();
		operatorMap.put(operator, recipeUnitEntry.getKey());
		Map<String, Map<String, String>> recipeNameOperatorMap = new HashMap<>();
		recipeNameOperatorMap.put(woFileDetail.getRecipe().getRecipeId(), operatorMap);
		recipeUnitOperatorMap.put(recipeUnitEntry.getValue().toString(), recipeNameOperatorMap);
	}

	private List<Integer> getWoRecipeListFromWOFileDetail(GenericWorkorder genericWorkorder) {
		List<Integer> woRecipeMappingList = new ArrayList<>();
		try {
			List<WOFileDetail> woFileDetailList = woFileDetailDao.getFileDetailByWorkOrderId(genericWorkorder.getId());
			for (WOFileDetail woFileDetail : woFileDetailList) {
				woRecipeMappingList.add(woFileDetail.getWoRecipeMapping().getId());
			}
		} catch (Exception e) {
			logger.error("Error inside getWoRecipeListFromWOFileDetail method in NVIBBenchmarkReportServiceImpl{} ",
					Utils.getStackTrace(e));
		}
		return woRecipeMappingList;
	}

	private void putRecipeAndOperatorIntoMap(Entry<String, Integer> recipeUnitEntry, WORecipeMapping woFileDetail,
			String operator, Map<String, Map<String, String>> recipeNameOperatorMap) {
		if (recipeNameOperatorMap.containsKey(woFileDetail.getRecipe().getRecipeId())) {
			Map<String, String> operatorMap = recipeNameOperatorMap.get(woFileDetail.getRecipe().getRecipeId());
			operatorMap.put(operator, recipeUnitEntry.getKey());
		} else {
			Map<String, String> operatorMap = new HashMap<>();
			operatorMap.put(operator, recipeUnitEntry.getKey());
			recipeNameOperatorMap.put(woFileDetail.getRecipe().getRecipeId(), operatorMap);
		}
	}

	private Map<Integer, WORecipeMapping> getWoRecipeMap(GenericWorkorder genericWorkorder) {
		List<WORecipeMapping> woRecipeList = woRecipeMappingDao.getWoRecipeMappingByWorkOrderId(
				genericWorkorder.getId());
		Map<Integer, WORecipeMapping> recipeMap = new HashMap<>();
		for (WORecipeMapping woRecipe : woRecipeList) {
			recipeMap.put(woRecipe.getId(), woRecipe);
		}
		return recipeMap;
	}

	private GenericWorkorder getGenericWOFromWorkorderId(Integer workorderId) {
		return iGenericWorkorderDao.findByPk(workorderId);
	}

	private Map<String, List<String[]>> getInBuildingDataForWorkorderId(Integer workorderId,
						Map<String, String> recipeOperatorMap, List<String> fetchKPIList) {
		Map<String, List<String[]>> inBuildingDataMap = new HashMap<>();
		try {
			for (Entry<String, String> operatorRecipeMapEntry : recipeOperatorMap.entrySet()) {

				/** fetch drive data recipe wise */
				List<String[]> recipeDriveData = reportService.getDriveDataForReport(workorderId,
						Arrays.asList(operatorRecipeMapEntry.getValue()), fetchKPIList);

				inBuildingDataMap.put(operatorRecipeMapEntry.getKey(), recipeDriveData);
			}
		} catch (Exception e) {
			logger.error("Error inside getInBuildingDataForWorkorderId method in NVIBBenchmarkReportServiceImpl{} ",
					Utils.getStackTrace(e));
		}

		return inBuildingDataMap;
	}

	private IBBenchMarkReportWrapper getBenchmarkReportWrapperFromRecipeData(
			Map<String, List<String[]>> operatorDataMap, Integer workorderId, Map<String, String> operatorRecipeMap,
			Map<String, Object> reportParamMap, Map<String, Integer> kpiIndexMap) {
		IBBenchMarkReportWrapper mainWrapper = new IBBenchMarkReportWrapper();

		Map<String, Map<String, String>> operatorImagesMap = getPlotImagesForOperator(workorderId, operatorDataMap,
				operatorRecipeMap, kpiIndexMap);

		setNoAccessDataToWrapper(workorderId, operatorDataMap, operatorRecipeMap, mainWrapper);

		IBBenchmarkImagePlotWrapper rsrpPlotWrapper = new IBBenchmarkImagePlotWrapper();
		IBBenchmarkImagePlotWrapper sinrPlotWrapper = new IBBenchmarkImagePlotWrapper();
		IBBenchmarkImagePlotWrapper dlPlotWrapper = new IBBenchmarkImagePlotWrapper();
		IBBenchmarkImagePlotWrapper ulPlotWrapper = new IBBenchmarkImagePlotWrapper();
		IBBenchmarkImagePlotWrapper pciPlotWrapper = new IBBenchmarkImagePlotWrapper();
		IBBenchmarkImagePlotWrapper cqiPlotWrapper = new IBBenchmarkImagePlotWrapper();
		Map<String, Map<KPI, List<ArrayList<Double>>>> finalMapLte = new HashMap<>();
		List<Map<KPI, Double>> summaryKpiWrapperMap = new ArrayList<>();
		List<IBBenchmarkSummaryKpiWrapper> kpiSummaryList = new ArrayList<>();
		List<IBBenchmarkSummaryKpiWrapper> throughputSummaryList = new ArrayList<>();
		Map<String, Map<KPI, ArrayList<Integer>>> allOperatorDataMap = new HashMap<>();
		List<String[]> combinedDataList = null;

		String imgFloorPlan = null;
		boolean isRsrpAvailable = true, isSinrAvailable = true, isDlAvailable = true, isUlAvailable = true,
				isPciAvailable = true, isCqiAvailable = true;
		int operatorCount = 1;

		for (Entry<String, List<String[]>> operatorDataEntry : operatorDataMap.entrySet()) {
			if (operatorImagesMap != null && operatorImagesMap.get(operatorDataEntry.getKey()) != null) {
				if (!StringUtils.isBlank(operatorImagesMap.get(operatorDataEntry.getKey()).get(ReportConstants.RSRP))) {
					setImagesToWrapper(rsrpPlotWrapper, operatorImagesMap.get(operatorDataEntry.getKey()),
							operatorCount, operatorDataEntry.getKey(), ReportConstants.RSRP, ReportConstants.RSRP);
				} else {
					isRsrpAvailable = false;
				}

				if (!StringUtils.isBlank(operatorImagesMap.get(operatorDataEntry.getKey()).get(ReportConstants.SINR))) {
					setImagesToWrapper(sinrPlotWrapper, operatorImagesMap.get(operatorDataEntry.getKey()),
							operatorCount, operatorDataEntry.getKey(), ReportConstants.SINR, ReportConstants.SINR);
				} else {
					isSinrAvailable = false;
				}
				if (!StringUtils.isBlank(
						operatorImagesMap.get(operatorDataEntry.getKey()).get(ReportConstants.DL_THROUGHPUT))) {
					setImagesToWrapper(dlPlotWrapper, operatorImagesMap.get(operatorDataEntry.getKey()), operatorCount,
							operatorDataEntry.getKey(), ReportConstants.DL_THROUGHPUT, ReportConstants.TITLE_DL);
				} else {
					isDlAvailable = false;
				}
				if (!StringUtils.isBlank(
						operatorImagesMap.get(operatorDataEntry.getKey()).get(ReportConstants.UL_THROUGHPUT))) {
					setImagesToWrapper(ulPlotWrapper, operatorImagesMap.get(operatorDataEntry.getKey()), operatorCount,
							operatorDataEntry.getKey(), ReportConstants.UL_THROUGHPUT, ReportConstants.TITLE_UL);
				} else {
					isUlAvailable = false;
				}

				if (!StringUtils.isBlank(
						operatorImagesMap.get(operatorDataEntry.getKey()).get(ReportConstants.PCI_PLOT))) {
					setImagesToWrapper(pciPlotWrapper, operatorImagesMap.get(operatorDataEntry.getKey()), operatorCount,
							operatorDataEntry.getKey(), ReportConstants.PCI_PLOT, ReportConstants.PCI_PLOT);
				} else {
					isPciAvailable = false;
				}

				if (!StringUtils.isBlank(operatorImagesMap.get(operatorDataEntry.getKey()).get(ReportConstants.CQI))) {
					setImagesToWrapper(cqiPlotWrapper, operatorImagesMap.get(operatorDataEntry.getKey()), operatorCount,
							operatorDataEntry.getKey(), ReportConstants.CQI, ReportConstants.CQI);
				} else {
					isCqiAvailable = false;
				}
			}

			IBLteThreeGDataWrapper lteThreeGDataWrapper = technologyWiseDataList(operatorDataEntry.getValue(), kpiIndexMap);

			if (lteThreeGDataWrapper != null && lteThreeGDataWrapper.getLteDataList() != null) {
				Map<KPI, List<ArrayList<Double>>> map = getKpiMapForLteChart(lteThreeGDataWrapper.getLteDataList(),
						operatorDataEntry.getKey());
				finalMapLte.put(operatorDataEntry.getKey(), map);
				allOperatorDataMap.put(operatorDataEntry.getKey(), calculateKPIForOverallScore(lteThreeGDataWrapper));
			}

			reportParamMap.put(ReportConstants.OPERATOR + operatorCount, operatorDataEntry.getKey());

			summaryKpiWrapperMap.add(getSummaryKpiWrapperList(operatorDataEntry.getValue(), kpiIndexMap));

			operatorCount++;

			if (combinedDataList == null) {
				combinedDataList = new ArrayList<>(operatorDataEntry.getValue());
			}

			if (imgFloorPlan == null) {
				imgFloorPlan = operatorImagesMap.get(operatorDataEntry.getKey()).get(ReportConstants.FLOORPLANIMG);
			}
		}

		reportParamMap.put("surveypath", imgFloorPlan);

		setReportSummaryWrapperForKPI(kpiSummaryList, getNetworkKPIList(), summaryKpiWrapperMap, operatorCount - 1);
		setReportSummaryWrapperForKPI(throughputSummaryList, getThroughputKPIList(), summaryKpiWrapperMap,
				operatorCount - 1);

		IBBenchmarkSummaryWrapper summaryWrapper = new IBBenchmarkSummaryWrapper();

		summaryWrapper.setKpiSummaryList(kpiSummaryList);
		summaryWrapper.setThroughputSummaryList(throughputSummaryList);

		List<IBBenchmarkImagePlotWrapper> rsrpPlotList = Arrays.asList(rsrpPlotWrapper);
		List<IBBenchmarkImagePlotWrapper> sinrPlotList = Arrays.asList(sinrPlotWrapper);
		List<IBBenchmarkImagePlotWrapper> dlPlotList = Arrays.asList(dlPlotWrapper);
		List<IBBenchmarkImagePlotWrapper> ulPlotList = Arrays.asList(ulPlotWrapper);
		List<IBBenchmarkImagePlotWrapper> pciPlotList = Arrays.asList(pciPlotWrapper);
		List<IBBenchmarkImagePlotWrapper> cqiPlotList = Arrays.asList(cqiPlotWrapper);

		List<IBBenchmarkGraphPlotWrapper> rsrpGraphList = new ArrayList<>();
		rsrpGraphList.add(
				getGraphPlotWrapper(finalMapLte, ReportConstants.RSRP + ReportConstants.TITLE_GRAPH, KPI.RSRP));
		List<IBBenchmarkGraphPlotWrapper> sinrGraphList = new ArrayList<>();
		sinrGraphList.add(
				getGraphPlotWrapper(finalMapLte, ReportConstants.SINR + ReportConstants.TITLE_GRAPH, KPI.SINR));
		List<IBBenchmarkGraphPlotWrapper> dlGraphList = new ArrayList<>();
		dlGraphList.add(
				getGraphPlotWrapper(finalMapLte, ReportConstants.TITLE_DL + ReportConstants.TITLE_GRAPH, KPI.DL));
		List<IBBenchmarkGraphPlotWrapper> ulGraphList = new ArrayList<>();
		ulGraphList.add(
				getGraphPlotWrapper(finalMapLte, ReportConstants.TITLE_UL + ReportConstants.TITLE_GRAPH, KPI.UL));

		if (isRsrpAvailable) {
			mainWrapper.setRsrpPlotList(rsrpPlotList);
			mainWrapper.setRsrpGraphList(rsrpGraphList);
		}
		if (isSinrAvailable) {
			mainWrapper.setSinrPlotList(sinrPlotList);
			mainWrapper.setSinrGraphList(sinrGraphList);
		}
		if (isDlAvailable) {
			mainWrapper.setDlPlotList(dlPlotList);
			mainWrapper.setDlGraphList(dlGraphList);
		}
		if (isUlAvailable) {
			mainWrapper.setUlPlotList(ulPlotList);
			mainWrapper.setUlGraphList(ulGraphList);
		}
		if (isPciAvailable) {
			mainWrapper.setPciPlotList(pciPlotList);
		}
		if (isCqiAvailable) {
			mainWrapper.setCqiPlotList(cqiPlotList);
		}

		mainWrapper.setSummaryWrapperList(Arrays.asList(summaryWrapper));

		IBBenchmarkKPIAnalysisWrapper kpiAnalysisWrapper = new IBBenchmarkKPIAnalysisWrapper();

		kpiAnalysisWrapper.setKpiresultList(getKPiScoreDataForOperator(allOperatorDataMap));

		mainWrapper.setKpiAnalysisList(Arrays.asList(kpiAnalysisWrapper));

		List<IBMappingwrapper> mappingWrapperList = populateMapForLte3gTableList(allOperatorDataMap);

		IBMappingwrapper oveallScoreWrapper = prepareOverAllScore(mappingWrapperList);

		List<IBMappingwrapper> mappingList = getScoreGraphList(oveallScoreWrapper);
		List<IBMappingwrapper> oveallScoreList = getOverAllScoreList(oveallScoreWrapper);

		Collections.sort(oveallScoreList);

		IBBenchmarkOverallDataWrapper overAllScore = new IBBenchmarkOverallDataWrapper();

		overAllScore.setOveallScoreList(oveallScoreList);
		overAllScore.setScoreGraph(mappingList);

		mainWrapper.setOverallScoreList(Arrays.asList(overAllScore));

		return mainWrapper;
	}

	private List<IBMappingwrapper> getScoreGraphList(IBMappingwrapper overallScoreWrapper) {
		List<IBMappingwrapper> scoreGraphList = new ArrayList<>();

		if (overallScoreWrapper != null) {
			if (overallScoreWrapper.getOperator1() != null) {
				IBMappingwrapper mappingWrapper = new IBMappingwrapper();
				mappingWrapper.setOperatorName(overallScoreWrapper.getOperator1());
				mappingWrapper.setAvgValue(overallScoreWrapper.getoperator1Score());
				scoreGraphList.add(mappingWrapper);
			}
			if (overallScoreWrapper.getOperator2() != null) {
				IBMappingwrapper mappingWrapper = new IBMappingwrapper();
				mappingWrapper.setOperatorName(overallScoreWrapper.getOperator2());
				mappingWrapper.setAvgValue(overallScoreWrapper.getoperator2Score());
				scoreGraphList.add(mappingWrapper);
			}
			if (overallScoreWrapper.getOperator3() != null) {
				IBMappingwrapper mappingWrapper = new IBMappingwrapper();
				mappingWrapper.setOperatorName(overallScoreWrapper.getOperator3());
				mappingWrapper.setAvgValue(overallScoreWrapper.getoperator3Score());
				scoreGraphList.add(mappingWrapper);
			}
			if (overallScoreWrapper.getOperator4() != null) {
				IBMappingwrapper mappingWrapper = new IBMappingwrapper();
				mappingWrapper.setOperatorName(overallScoreWrapper.getOperator4());
				mappingWrapper.setAvgValue(overallScoreWrapper.getoperator4Score());
				scoreGraphList.add(mappingWrapper);
			}
		}

		return scoreGraphList;
	}

	private void setNoAccessDataToWrapper(Integer workorderId, Map<String, List<String[]>> operatorDataMap,
			Map<String, String> operatorRecipeMap, IBBenchMarkReportWrapper wrapper) {
		try {
			logger.info("Going to set no Access images, WorkorderId: {}, OperatorRecipeMap: {}", workorderId,
					operatorRecipeMap);
			String localDirPath = ConfigUtils.getString(ReportConstants.IB_BENCHMARK_REPORT_TEMP_PATH);
			for (Entry<String, List<String[]>> operatorEntry : operatorDataMap.entrySet()) {
				DriveDataWrapper driveDataWrapper = nvLayer3dashboardService.getFloorplanDataFromLayer3ReportForFramework(
						workorderId, operatorRecipeMap.get(operatorEntry.getKey()));
				String imgFloorPlan = reportService.getFloorplanImg(workorderId, operatorRecipeMap.get(operatorEntry.getKey()),
						operatorEntry.getKey(), driveDataWrapper);
				junkFiles.add(imgFloorPlan);
				InbuildingPointWrapper pointWrapper = IBReportUtils.getInstance().drawFloorPlan(imgFloorPlan,
						driveDataWrapper.getJson(), ReportConstants.FLOORPLANNAME, operatorEntry.getValue());
				List<DriveDataWrapper> noAccessDataList = nvReportHbaseDao.getNoAcessDataFromLayer3Report(workorderId,
						operatorRecipeMap.get(operatorEntry.getKey()));
				if (CollectionUtils.isNotEmpty(noAccessDataList)) {
					setNoAccessDataList(noAccessDataList, imgFloorPlan, wrapper, pointWrapper, localDirPath);
				}
			}
		} catch (Exception e) {
			logger.error("Error inside setNoAccessDataToWrapper method in NVIBBenchmarkReportServiceImpl{} ",
					Utils.getStackTrace(e));
		}
	}

	private List<KPI> getNetworkKPIList() {
		List<KPI> kpiList = new ArrayList<>();
		kpiList.add(KPI.RSRP);
		kpiList.add(KPI.SINR);
		kpiList.add(KPI.RSRQ);
		kpiList.add(KPI.RSSI);
		return kpiList;
	}

	private List<KPI> getThroughputKPIList() {
		List<KPI> kpiList = new ArrayList<>();
		kpiList.add(KPI.DL);
		kpiList.add(KPI.UL);
		return kpiList;
	}

	private Map<KPI, ArrayList<Integer>> calculateKPIForOverallScore(IBLteThreeGDataWrapper lteThreeGDataWrapper) {
		Integer goodDlLte = 0, avgDlLte = 0, badDlLte = 0, goodDl3g = 0, avgDL3g = 0, badDl3g = 0;
		Integer goodRsrp = 0, avgRsrp = 0, badRsrp = 0, goodRscp = 0, badRscp = 0, avgRscp = 0;
		Integer goodUlLte = 0, avgUlLte = 0, badUlLte = 0, goodUl3g = 0, avgUl3g = 0, badUl3g = 0;
		Integer goodSinr = 0, avgSinr = 0, badSinr = 0, goodEcio = 0, badEcio = 0, avgEcio = 0;
		Map<KPI, ArrayList<Integer>> map = new java.util.EnumMap<>(KPI.class);
		ArrayList<Integer> arrayList;
		if (lteThreeGDataWrapper.getLteDataList() != null) {
			for (IBKPIWrapper walktestParameter : lteThreeGDataWrapper.getLteDataList()) {
				if (walktestParameter.getRsrp() != null) {
					if (walktestParameter.getRsrp() >= -80) {
						goodRsrp++;
					} else if (walktestParameter.getRsrp() >= -100 && walktestParameter.getRsrp() < -80) {
						avgRsrp++;
					} else {
						badRsrp++;
					}
				}
				if (walktestParameter.getUlRate() != null) {
					if (walktestParameter.getUlRate() >= 20) {
						goodUlLte++;
					} else if (walktestParameter.getUlRate() >= 10 && walktestParameter.getUlRate() < 20) {
						avgUlLte++;
					} else {
						badUlLte++;
					}
				}
				if (walktestParameter.getDlRate() != null) {
					if (walktestParameter.getDlRate() >= 20) {
						goodDlLte++;
					} else if (walktestParameter.getDlRate() >= 10 && walktestParameter.getDlRate() < 20) {
						avgDlLte++;
					} else {
						badDlLte++;
					}
				}

				if (walktestParameter.getSinr() != null) {
					if (walktestParameter.getSinr() >= 20) {
						goodSinr++;
					} else if (walktestParameter.getSinr() >= 10 && walktestParameter.getSinr() < 20) {
						avgSinr++;
					} else {
						badSinr++;
					}
				}
			}
		}

		arrayList = prepareListFromArray(goodRsrp + goodRscp, avgRscp + avgRsrp, badRsrp + badRscp);
		map.put(KPI.RSRP, (ArrayList<Integer>) arrayList);
		arrayList = prepareListFromArray(goodDlLte + goodDl3g, avgDlLte + avgDL3g, badDlLte + badDl3g);
		map.put(KPI.DL, arrayList);
		arrayList = prepareListFromArray(goodUlLte + goodUl3g, avgUlLte + avgUl3g, badUlLte + badUl3g);
		map.put(KPI.UL, arrayList);
		arrayList = prepareListFromArray(goodSinr + goodEcio, avgSinr + avgEcio, badSinr + badEcio);
		map.put(KPI.SINR, arrayList);
		return map;
	}

	private ArrayList<Integer> prepareListFromArray(Integer... arr) {
		ArrayList<Integer> arrayList = new ArrayList<>();
		Collections.addAll(arrayList, arr);
		return arrayList;
	}

	private void setReportSummaryWrapperForKPI(List<IBBenchmarkSummaryKpiWrapper> reportSummaryWrapperList,
			List<KPI> kpiList, List<Map<KPI, Double>> kpiListMap, Integer operatorCount) {
		for (KPI kpi : kpiList) {
			IBBenchmarkSummaryKpiWrapper summaryWrapper = new IBBenchmarkSummaryKpiWrapper();
			summaryWrapper.setKpiName(getKPILabelWithUnit(kpi));
			switch (operatorCount) {
			case ReportConstants.OPERATOR_COUNT_1:
				if (kpiListMap != null && kpiListMap.get(0).get(kpi) != null) {
					summaryWrapper.setOperator1Kpi(String.format("%.2f", kpiListMap.get(0).get(kpi)));
				}
				break;
			case ReportConstants.OPERATOR_COUNT_2:
				if (kpiListMap != null && kpiListMap.get(0).get(kpi) != null) {
					summaryWrapper.setOperator1Kpi(String.format("%.2f", kpiListMap.get(0).get(kpi)));
				}
				if (kpiListMap != null && kpiListMap.get(1).get(kpi) != null) {
					summaryWrapper.setOperator2Kpi(String.format("%.2f", kpiListMap.get(1).get(kpi)));
				}
				break;
			case ReportConstants.OPERATOR_COUNT_3:
				if (kpiListMap != null && kpiListMap.get(0).get(kpi) != null) {
					summaryWrapper.setOperator1Kpi(String.format("%.2f", kpiListMap.get(0).get(kpi)));
				}
				if (kpiListMap != null && kpiListMap.get(1).get(kpi) != null) {
					summaryWrapper.setOperator2Kpi(String.format("%.2f", kpiListMap.get(1).get(kpi)));
				}
				if (kpiListMap != null && kpiListMap.get(2).get(kpi) != null) {
					summaryWrapper.setOperator3Kpi(String.format("%.2f", kpiListMap.get(2).get(kpi)));
				}
				break;
			case ReportConstants.OPERATOR_COUNT_4:
				if (kpiListMap != null && kpiListMap.get(0).get(kpi) != null) {
					summaryWrapper.setOperator1Kpi(String.format("%.2f", kpiListMap.get(0).get(kpi)));
				}
				if (kpiListMap != null && kpiListMap.get(1).get(kpi) != null) {
					summaryWrapper.setOperator2Kpi(String.format("%.2f", kpiListMap.get(1).get(kpi)));
				}
				if (kpiListMap != null && kpiListMap.get(2).get(kpi) != null) {
					summaryWrapper.setOperator3Kpi(String.format("%.2f", kpiListMap.get(2).get(kpi)));
				}
				if (kpiListMap != null && kpiListMap.get(3).get(kpi) != null) {
					summaryWrapper.setOperator4Kpi(String.format("%.2f", kpiListMap.get(3).get(kpi)));
				}
				break;
			default:
				break;
			}
			reportSummaryWrapperList.add(summaryWrapper);
		}
	}

	private IBBenchmarkGraphPlotWrapper getGraphPlotWrapper(Map<String, Map<KPI, List<ArrayList<Double>>>> finalMap,
			String title, KPI kpi) {
		IBBenchmarkGraphPlotWrapper graphPlotWrapper = new IBBenchmarkGraphPlotWrapper();
		graphPlotWrapper.setTitle(title);
		graphPlotWrapper.setKpiChartList(convertLteMapToWrapper(finalMap, kpi));
		return graphPlotWrapper;
	}

	private Map<String, Map<String, String>> getPlotImagesForOperator(Integer workorderId,
						Map<String, List<String[]>> operatorDataMap, Map<String, String> operatorRecipeMap,
																	  Map<String, Integer> kpiIndexMap) {
		try {
//			logger.debug("going to get Plot images, workorderId: {}, operatorDataMap: {}, driveDataWrapper: {}, operatorRecipeMap: {}",
//					workorderId, operatorDataMap, operatorRecipeMap);
			String localDirPath = ConfigUtils.getString(ReportConstants.IB_BENCHMARK_REPORT_TEMP_PATH);
			Map<String, String> pciImagesMap = null;
			Map<String, Map<String, String>> operatorImagesMap = new HashMap<>();
			for (Entry<String, List<String[]>> operatorEntry : operatorDataMap.entrySet()) {
				DriveDataWrapper driveDataWrapper = nvLayer3dashboardService.getFloorplanDataFromLayer3ReportForFramework(
						workorderId, operatorRecipeMap.get(operatorEntry.getKey()));
				String imgFloorPlan = reportService.getFloorplanImg(workorderId, operatorRecipeMap.get(operatorEntry.getKey()),
						operatorEntry.getKey(), driveDataWrapper);
				junkFiles.add(imgFloorPlan);
				InbuildingPointWrapper pointWrapper = IBReportUtils.getInstance().drawFloorPlan(imgFloorPlan,
						driveDataWrapper.getJson(),  operatorEntry.getValue(),
						kpiIndexMap.get(ReportConstants.X_POINT), kpiIndexMap.get(ReportConstants.Y_POINT));
				List<KPIWrapper> kpiList = reportService.getKpiWrapperListForReport(workorderId,
						Arrays.asList(operatorRecipeMap.get(operatorEntry.getKey())), operatorEntry.getKey(),
						ReportConstants.SSVT_REPORT, kpiIndexMap);
				if (pciImagesMap == null) {
					pciImagesMap = InBuildingHeatMapGenerator.getInstance().generatePCIHeatMapForIBBenchmark(
							operatorDataMap, localDirPath, imgFloorPlan, kpiList, kpiIndexMap);
					if (pciImagesMap != null && !pciImagesMap.isEmpty()) {
						junkFiles.addAll(pciImagesMap.values());
					}
				}
				if (pointWrapper != null) {
					Map<String, String> heatMaps = getImagesForReport(imgFloorPlan, localDirPath,
							pointWrapper.getArlist(), kpiList, kpiIndexMap);
					if (heatMaps.containsKey(ReportConstants.PCI_PLOT)) {
						heatMaps.replace(ReportConstants.PCI_PLOT, pciImagesMap.get(
								ReportConstants.PCI_PLOT + ReportConstants.UNDERSCORE + operatorEntry.getKey()));
					}
					if (heatMaps.containsKey(ReportConstants.PCI_LEGEND)) {
						heatMaps.replace(ReportConstants.PCI_LEGEND, pciImagesMap.get(ReportConstants.PCI_LEGEND));
					}
					if (!heatMaps.containsKey(ReportConstants.FLOORPLANIMG)) {
						String surveyPathImage = InBuildingHeatMapGenerator.getInstance().getSurveyPathWithColor(
								pointWrapper.getArlist(), Color.GRAY, localDirPath, imgFloorPlan,
								kpiIndexMap.get(ReportConstants.X_POINT), kpiIndexMap.get(ReportConstants.Y_POINT));
						heatMaps.put(ReportConstants.FLOORPLANIMG, surveyPathImage);
						junkFiles.add(surveyPathImage);
					}

					operatorImagesMap.put(operatorEntry.getKey(), heatMaps);
				}
			}
			return operatorImagesMap;
		} catch (Exception e) {
			logger.error("Error inside getPlotImagesForOperator method in NVIBBenchmarkReportServiceImpl{} ",
					Utils.getStackTrace(e));
		}

		return null;
	}

	private IBLteThreeGDataWrapper technologyWiseDataList(List<String[]> listParameter, Map<String, Integer> kpiIndexMap) {
		IBLteThreeGDataWrapper lteThreeGDataWrapper = new IBLteThreeGDataWrapper();

		lteThreeGDataWrapper = updateLTEparameterList(lteThreeGDataWrapper, listParameter, kpiIndexMap);
		// lteThreeGDataWrapper =
		// updateThreeGparameterList(lteThreeGDataWrapper,
		// listParameter);

		if (lteThreeGDataWrapper.getLteDataList() == null && lteThreeGDataWrapper.getThreeGDataList() == null) {
			return null;
		} else {
			return lteThreeGDataWrapper;
		}
	}

	private IBLteThreeGDataWrapper updateLTEparameterList(IBLteThreeGDataWrapper lteThreeGDataWrapper,
														  List<String[]> csvData, Map<String, Integer> kpiIndexMap) {
		List<IBKPIWrapper> lteDataList = new ArrayList<>();
		if (csvData != null) {
			for (String[] parameter : csvData) {
				if (isXYPointAvailable(parameter, kpiIndexMap)) {
					IBKPIWrapper walktTestwrapper = new IBKPIWrapper();
					if (kpiIndexMap.get(ReportConstants.RSRP) != null
							&& parameter[kpiIndexMap.get(ReportConstants.RSRP)] != null
							&& !parameter[kpiIndexMap.get(ReportConstants.RSRP)].isEmpty()) {
						walktTestwrapper.setRsrp(Double.parseDouble(parameter[kpiIndexMap.get(ReportConstants.RSRP)]));
					}
					if (kpiIndexMap.get(ReportConstants.SINR) != null
							&& parameter[kpiIndexMap.get(ReportConstants.SINR)] != null
							&& !parameter[kpiIndexMap.get(ReportConstants.SINR)].isEmpty()) {
						walktTestwrapper.setSinr(Double.parseDouble(parameter[kpiIndexMap.get(ReportConstants.SINR)]));
					}
					if (kpiIndexMap.get(ReportConstants.DL_THROUGHPUT) != null
							&& parameter[kpiIndexMap.get(ReportConstants.DL_THROUGHPUT)] != null
							&& !parameter[kpiIndexMap.get(ReportConstants.DL_THROUGHPUT)].isEmpty()) {
						walktTestwrapper.setDlRate(Double.parseDouble(parameter[kpiIndexMap.get(ReportConstants.DL_THROUGHPUT)]));
					}
					if (kpiIndexMap.get(ReportConstants.UL_THROUGHPUT) != null
							&& parameter[kpiIndexMap.get(ReportConstants.UL_THROUGHPUT)] != null
							&& !parameter[kpiIndexMap.get(ReportConstants.UL_THROUGHPUT)].isEmpty()) {
						walktTestwrapper.setUlRate(Double.parseDouble(parameter[kpiIndexMap.get(ReportConstants.UL_THROUGHPUT)]));
					}

					lteDataList.add(walktTestwrapper);
				}
			}
		}

		if (!lteDataList.isEmpty()) {
			lteThreeGDataWrapper.setLteDataList(lteDataList);
		}
		return lteThreeGDataWrapper;
	}

	private Map<KPI, Double> getSummaryKpiWrapperList(List<String[]> listParameter, Map<String, Integer> kpiIndexMap) {
		Map<KPI, Double> summaryKPIMap = new java.util.EnumMap<>(KPI.class);
		if (listParameter != null) {
			double rsrpTotal = 0.0;
			double sinrTotal = 0.0;
			double dlTotal = 0.0;
			double ulTotal = 0.0;
			double rsrqTotal = 0.0;
			double rssiTotal = 0.0;
			int rsrpCount = 0;
			int sinrCount = 0;
			int dlCount = 0;
			int ulCount = 0;
			int rsrqCount = 0;
			int rssiCount = 0;
			for (String[] parameter : listParameter) {
				if (isXYPointAvailable(parameter, kpiIndexMap)) {
					Integer indexRsrp = kpiIndexMap.get(ReportConstants.RSRP);
					if (indexRsrp != null && parameter[indexRsrp] != null
							&& !parameter[indexRsrp].isEmpty()) {
						rsrpTotal = rsrpTotal + Double.parseDouble(parameter[indexRsrp]);
						rsrpCount++;
					}
					Integer indexSinr = kpiIndexMap.get(ReportConstants.SINR);
					if (indexSinr != null && parameter[indexSinr] != null
							&& !parameter[indexSinr].isEmpty()) {
						sinrTotal = sinrTotal + Double.parseDouble(parameter[indexSinr]);
						sinrCount++;
					}
					Integer indexDl = kpiIndexMap.get(ReportConstants.DL_THROUGHPUT);
					if (indexDl != null && parameter[indexDl] != null
							&& !parameter[indexDl].isEmpty()) {
						dlTotal = dlTotal + Double.parseDouble(parameter[indexDl]);
						dlCount++;
					}
					Integer indexUl = kpiIndexMap.get(ReportConstants.UL_THROUGHPUT);
					if (indexUl != null && parameter[indexUl] != null
							&& !parameter[indexUl].isEmpty()) {
						ulTotal = ulTotal + Double.parseDouble(parameter[indexUl]);
						ulCount++;
					}
					Integer indexRsrq = kpiIndexMap.get(ReportConstants.RSRQ);
					if (indexRsrq != null && parameter[indexRsrq] != null
							&& !parameter[indexRsrq].isEmpty()) {
						rsrqTotal = rsrqTotal + Double.parseDouble(parameter[indexRsrq]);
						rsrqCount++;
					}
					Integer indexRssi = kpiIndexMap.get(ReportConstants.RSSI);
					if (indexRssi != null && parameter[indexRssi] != null
							&& !parameter[indexRssi].isEmpty()) {
						rssiTotal = rssiTotal + Double.parseDouble(parameter[indexRssi]);
						rssiCount++;
					}
				}
			}

			if (rsrpCount != 0) {
				summaryKPIMap.put(KPI.RSRP, rsrpTotal / rsrpCount);
			}
			if (sinrCount != 0) {
				summaryKPIMap.put(KPI.SINR, sinrTotal / sinrCount);
			}
			if (rsrqCount != 0) {
				summaryKPIMap.put(KPI.RSRQ, rsrqTotal / rsrqCount);
			}
			if (rssiCount != 0) {
				summaryKPIMap.put(KPI.RSSI, rssiTotal / rssiCount);
			}
			if (dlCount != 0) {
				summaryKPIMap.put(KPI.DL, dlTotal / dlCount);
			}
			if (ulCount != 0) {
				summaryKPIMap.put(KPI.UL, ulTotal / ulCount);
			}
			return summaryKPIMap;
		}
		return null;
	}

	private boolean isXYPointAvailable(String[] parameter, Map<String, Integer> kpiIndexMap) {
		return kpiIndexMap.get(ReportConstants.X_POINT) != null
				&& kpiIndexMap.get(ReportConstants.Y_POINT) != null
				&& parameter.length > kpiIndexMap.get(ReportConstants.X_POINT)
				&& parameter.length > kpiIndexMap.get(ReportConstants.Y_POINT)
				&& !StringUtils.isBlank(parameter[kpiIndexMap.get(ReportConstants.X_POINT)])
				&& !StringUtils.isBlank(parameter[kpiIndexMap.get(ReportConstants.Y_POINT)]);
	}

	private Map<KPI, List<ArrayList<Double>>> getKpiMapForLteChart(List<IBKPIWrapper> list, String operatorName) {
		Map<KPI, List<ArrayList<Double>>> map = new java.util.EnumMap<>(KPI.class);
		Map<KPI, Double> averageWiseKpiMap = new java.util.EnumMap<>(KPI.class);
		IBMappingwrapper wrapper = new IBMappingwrapper();
		List<ArrayList<Double>> arrayList;
		Double totalRsrp = 0.0, totalDl = 0.0, totalUl = 0.0, totalSinr = 0.0, avg = 0.0;
		int countDl = 0, countRsrp = 0, countUl = 0, countSinr = 0;
		Integer rsrp4090 = 0, rsrp9095 = 0, rsrp95100 = 0, rsrp100105 = 0, rsrp105113 = 0, rsrp113140 = 0, rsrp = 0;
		Integer dl50026 = 0, dl2620 = 0, dl2010 = 0, dl106 = 0, dl62 = 0, dl20 = 0, dl = 0;
		Integer ul50026 = 0, ul2620 = 0, ul2010 = 0, ul106 = 0, ul62 = 0, ul20 = 0, ul = 0;
		Integer sinr3025 = 0, sinr2520 = 0, sinr2010 = 0, sinr100 = 0, sinr0m2 = 0, sinrm2m6 = 0, sinrm6m20 = 0,
				sinr = 0;
		for (IBKPIWrapper walktestParameter : list) {
			if (walktestParameter != null) {
				if (walktestParameter.getRsrp() != null) {
					if (walktestParameter.getRsrp() < -40 && walktestParameter.getRsrp() >= -90) {
						rsrp4090++;
					} else if (walktestParameter.getRsrp() < -90 && walktestParameter.getRsrp() >= -95) {
						rsrp9095++;
					} else if (walktestParameter.getRsrp() < -95 && walktestParameter.getRsrp() >= -100) {
						rsrp95100++;
					} else if (walktestParameter.getRsrp() < -100 && walktestParameter.getRsrp() >= -105) {
						rsrp100105++;
					} else if (walktestParameter.getRsrp() < -105 && walktestParameter.getRsrp() >= -113) {
						rsrp105113++;
					} else if (walktestParameter.getRsrp() < -113 && walktestParameter.getRsrp() >= -140) {
						rsrp113140++;
					} else {
						rsrp++;
					}
					countRsrp++;
					totalRsrp = totalRsrp + walktestParameter.getRsrp();
				}
				if (walktestParameter.getDlRate() != null) {
					if (walktestParameter.getDlRate() < 500 && walktestParameter.getDlRate() >= 26) {
						dl50026++;
					} else if (walktestParameter.getDlRate() < 26 && walktestParameter.getDlRate() >= 20) {
						dl2620++;
					} else if (walktestParameter.getDlRate() < 20 && walktestParameter.getDlRate() >= 10) {
						dl2010++;
					} else if (walktestParameter.getDlRate() < 10 && walktestParameter.getDlRate() >= 6) {
						dl106++;
					} else if (walktestParameter.getDlRate() < 6 && walktestParameter.getDlRate() >= 2) {
						dl62++;
					} else if (walktestParameter.getDlRate() < 2 && walktestParameter.getDlRate() >= 0) {
						dl20++;
					} else {
						dl++;
					}
					countDl++;
					totalDl = totalDl + walktestParameter.getDlRate();
				}
				if (walktestParameter.getUlRate() != null) {
					if (walktestParameter.getUlRate() < 500 && walktestParameter.getUlRate() >= 26) {
						ul50026++;
					} else if (walktestParameter.getUlRate() < 26 && walktestParameter.getUlRate() >= 20) {
						ul2620++;
					} else if (walktestParameter.getUlRate() < 20 && walktestParameter.getUlRate() >= 10) {
						ul2010++;
					} else if (walktestParameter.getUlRate() < 10 && walktestParameter.getUlRate() >= 6) {
						ul106++;
					} else if (walktestParameter.getUlRate() < 6 && walktestParameter.getUlRate() >= 2) {
						ul62++;
					} else if (walktestParameter.getUlRate() < 2 && walktestParameter.getUlRate() >= 0) {
						ul20++;
					} else {
						ul++;
					}
					countUl++;
					totalUl = totalUl + walktestParameter.getUlRate();
				}

				if (walktestParameter.getSinr() != null) {
					if (walktestParameter.getSinr() < 30 && walktestParameter.getSinr() >= 25) {
						sinr3025++;
					} else if (walktestParameter.getSinr() < 25 && walktestParameter.getSinr() >= 20) {
						sinr2520++;
					} else if (walktestParameter.getSinr() < 20 && walktestParameter.getSinr() >= 10) {
						sinr2010++;
					} else if (walktestParameter.getSinr() < 10 && walktestParameter.getSinr() >= 0) {
						sinr100++;
					} else if (walktestParameter.getSinr() < 0 && walktestParameter.getSinr() >= -2) {
						sinr0m2++;
					} else if (walktestParameter.getSinr() < -2 && walktestParameter.getSinr() >= -6) {
						sinrm2m6++;
					} else if (walktestParameter.getSinr() < -6 && walktestParameter.getSinr() >= -20) {
						sinrm6m20++;
					} else {
						sinr++;
					}
					countSinr++;
					totalSinr = totalSinr + walktestParameter.getSinr();
				}
			}
		}
		avg = totalRsrp / countRsrp;

		averageWiseKpiMap.put(KPI.RSRP, avg != null ? avg : 0.0);
		avg = totalDl / countDl;
		averageWiseKpiMap.put(KPI.DL, avg != null ? avg : 0.0);
		avg = totalUl / countUl;
		averageWiseKpiMap.put(KPI.UL, avg != null ? avg : 0.0);
		avg = totalSinr / countSinr;
		averageWiseKpiMap.put(KPI.SINR, avg != null ? avg : 0.0);
		wrapper.setKpiWiseAverage(averageWiseKpiMap);

		wrapper.setOperatorName(operatorName);
		arrayList = getCdfvalue(countRsrp, rsrp4090, rsrp9095, rsrp95100, rsrp100105, rsrp105113, rsrp113140, rsrp);
		map.put(KPI.RSRP, arrayList);
		arrayList = getCdfvalue(countDl, dl50026, dl2620, dl2010, dl106, dl62, dl20, dl);
		map.put(KPI.DL, arrayList);
		arrayList = getCdfvalue(countUl, ul50026, ul2620, ul2010, ul106, ul62, ul20, ul);
		map.put(KPI.UL, arrayList);
		arrayList = getCdfvalue(countSinr, sinr3025, sinr2520, sinr2010, sinr100, sinr0m2, sinrm2m6, sinrm6m20, sinr);
		map.put(KPI.SINR, arrayList);
		return map;
	}

	private List<ArrayList<Double>> getCdfvalue(Integer count, Integer... kpiValue) {
		List<ArrayList<Double>> list = new ArrayList<>();
		ArrayList<Double> listCDF = new ArrayList<>();
		ArrayList<Double> listPDF = new ArrayList<>();
		double cdf = 0.0;
		Double percent = 0.0;
		for (int i = 0; i < kpiValue.length; i++) {
			Double temp;
			
			percent = Utils.getPercentage(kpiValue[i], count);
			
			cdf = cdf + percent;
			temp = cdf;
			listCDF.add(temp);
			listPDF.add(percent);
		}
		list.add(listCDF);
		list.add(listPDF);
		return list;
	}

	private List<IBBenchmarkGraphDataWrapper> convertLteMapToWrapper(
			Map<String, Map<KPI, List<ArrayList<Double>>>> finalMap, KPI kpi) {
		List<IBBenchmarkGraphDataWrapper> list = new ArrayList<>();
		try {
			List<String> operators = new ArrayList<>(finalMap.keySet());
			int listIndex = operators.size();
			int loopCount = 7;
			if (KPI.SINR.equals(kpi)) {
				loopCount = 8;
			}
			for (int j = 0; j < loopCount; j++) {
				IBBenchmarkGraphDataWrapper wrapper = new IBBenchmarkGraphDataWrapper();
				if (listIndex > 0 && listIndex <= operators.size()) {
					wrapper.setOperator1CDF(finalMap.get(operators.get(operators.size() - listIndex))
													.get(kpi)
													.get(ReportConstants.INDEX_ZER0)
													.get(j));
					wrapper.setOperator1PDF(finalMap.get(operators.get(operators.size() - listIndex))
													.get(kpi)
													.get(ReportConstants.INDEX_ONE)
													.get(j));
					wrapper.setOperator1(operators.get(operators.size() - listIndex));
					listIndex--;
				}
				if (listIndex > 0 && listIndex < operators.size()) {
					wrapper.setOperator2CDF(finalMap.get(operators.get(operators.size() - listIndex))
													.get(kpi)
													.get(ReportConstants.INDEX_ZER0)
													.get(j));
					wrapper.setOperator2PDF(finalMap.get(operators.get(operators.size() - listIndex))
													.get(kpi)
													.get(ReportConstants.INDEX_ONE)
													.get(j));
					wrapper.setOperator2(operators.get(operators.size() - listIndex));
					listIndex--;
				}
				if (listIndex > 0 && listIndex < operators.size()) {
					wrapper.setOperator3CDF(finalMap.get(operators.get(operators.size() - listIndex))
													.get(kpi)
													.get(ReportConstants.INDEX_ZER0)
													.get(j));
					wrapper.setOperator3PDF(finalMap.get(operators.get(operators.size() - listIndex))
													.get(kpi)
													.get(ReportConstants.INDEX_ONE)
													.get(j));
					wrapper.setOperator3(operators.get(operators.size() - listIndex));
					listIndex--;
				}
				if (listIndex > 0 && listIndex < operators.size()) {
					wrapper.setOperator4CDF(finalMap.get(operators.get(operators.size() - listIndex))
													.get(kpi)
													.get(ReportConstants.INDEX_ZER0)
													.get(j));
					wrapper.setOperator4PDF(finalMap.get(operators.get(operators.size() - listIndex))
													.get(kpi)
													.get(ReportConstants.INDEX_ONE)
													.get(j));
					wrapper.setOperator4(operators.get(operators.size() - listIndex));
				}

				wrapper.setRateKpi(getKpiRanges(kpi, j));
				wrapper.setxAxisLabel(getKPILabelWithUnit(kpi));
				wrapper.setyAxisLabel("CDF/PDF");
				list.add(wrapper);
				listIndex = operators.size();
			}
			return list;
		}

		catch (Exception e) {
			logger.error("Error inside the convertLteMapToWrapper method in NVIBBenchmarkReportServiceImpl{} ",
					Utils.getStackTrace(e));
		}
		return list;
	}

	private String getKpiRanges(KPI kpi, int index) {
		switch (kpi) {
		case RSRP:
			return index < ReportConstants.RSRP_DL_UL_RANGE_LIMIT ? ReportUtil.RSRPRANGES[index] : null;
		case SINR:
			return index < ReportConstants.SINR_RANGE_LIMIT ? ReportUtil.SINRRANGES[index] : null;
		case DL:
		case UL:
			return index < ReportConstants.RSRP_DL_UL_RANGE_LIMIT ? ReportUtil.DLULRANGES[index] : null;
		default:
			return null;
		}
	}

	private String getKPILabelWithUnit(KPI kpi) {
		switch (kpi) {
		case RSRP:
			return ReportConstants.RSRP_LABEL_WITH_UNIT;
		case SINR:
			return ReportConstants.SINR_LABEL_WITH_UNIT;
		case RSRQ:
			return ReportConstants.RSRQ_LABEL_WITH_UNIT;
		case RSSI:
			return ReportConstants.RSSI_LABEL_WITH_UNIT;
		case DL:
			return ReportConstants.DL_LABEL_WITH_UNIT;
		case UL:
			return ReportConstants.UL_LABEL_WITH_UNIT;
		default:
			return Symbol.EMPTY_STRING;
		}
	}

	private List<IBMappingwrapper> populateMapForLte3gTableList(
			Map<String, Map<KPI, ArrayList<Integer>>> finalMapLte3g) {
		Set<String> opratorNameSet = finalMapLte3g.keySet();

		List<IBMappingwrapper> mappingWrapperList = new ArrayList<>();
		for (String operatorName : opratorNameSet) {
			Set<KPI> opratorWiseKpiSet = finalMapLte3g.get(operatorName).keySet();
			for (KPI opratorWiseKpi : opratorWiseKpiSet) {
				IBMappingwrapper wrapper = new IBMappingwrapper();

				ArrayList<Integer> valuesList = finalMapLte3g.get(operatorName).get(opratorWiseKpi);
				Double result = NumberUtils.toDouble((valuesList.get(0) * 5 + valuesList.get(1) * 3 + valuesList.get(2) * 2) / 5);
				wrapper.setOperatorName(operatorName);
				wrapper.setKpi(opratorWiseKpi.toString());
				wrapper.setAvgValue(result);
				mappingWrapperList.add(wrapper);
			}
		}
		return mappingWrapperList;
	}

	private List<IBBenchmarkKPIDataWrapper> getKPiScoreDataForOperator(
			Map<String, Map<KPI, ArrayList<Integer>>> finalMapLte3g) {
		List<IBBenchmarkKPIDataWrapper> kpiDataScore = new ArrayList<>();
		Set<String> opratorNameSet = finalMapLte3g.keySet();
		for (String operatorName : opratorNameSet) {
			IBBenchmarkKPIDataWrapper kpiWrapper = new IBBenchmarkKPIDataWrapper();
			kpiWrapper.setOperatorName(operatorName);
			Set<KPI> opratorWiseKpiSet = finalMapLte3g.get(operatorName).keySet();
			for (KPI opratorWiseKpi : opratorWiseKpiSet) {
				ArrayList<Integer> valuesList = finalMapLte3g.get(operatorName).get(opratorWiseKpi);
				Double result = NumberUtils.toDouble((valuesList.get(0) * 5 + valuesList.get(1) * 3 + valuesList.get(2) * 2) / 5);
				if (opratorWiseKpi == KPI.RSRP) {
					kpiWrapper.setResultRSRP(result);
				} else if (opratorWiseKpi == KPI.SINR) {
					kpiWrapper.setResultSINR(result);
				} else if (opratorWiseKpi == KPI.DL) {
					kpiWrapper.setResultDL(result);
				} else if (opratorWiseKpi == KPI.UL) {
					kpiWrapper.setResultUL(result);
				}
			}
			kpiDataScore.add(kpiWrapper);
		}
		return kpiDataScore;
	}

	private IBMappingwrapper prepareOverAllScore(List<IBMappingwrapper> mappingWrapperList) {
		IBMappingwrapper wrapper = new IBMappingwrapper();
		List<String> operators = new ArrayList<>();
		for (IBMappingwrapper mappingWrapper : mappingWrapperList) {
			if (!operators.contains(mappingWrapper.getOperatorName())) {
				operators.add(mappingWrapper.getOperatorName());
			}
		}
		for (int i = 0; i < operators.size(); i++) {
			if (wrapper.getoperator1Score() == null && wrapper.getOperator1() == null) {
				wrapper.setoperator1Score(getOperatorWiseScore(operators.get(i), mappingWrapperList));
				wrapper.setOperator1(operators.get(i));
				continue;
			}
			if (wrapper.getoperator2Score() == null && wrapper.getOperator2() == null) {
				wrapper.setoperator2Score(getOperatorWiseScore(operators.get(i), mappingWrapperList));
				wrapper.setOperator2(operators.get(i));
				continue;
			}
			if (wrapper.getoperator3Score() == null && wrapper.getOperator3() == null) {
				wrapper.setoperator3Score(getOperatorWiseScore(operators.get(i), mappingWrapperList));
				wrapper.setOperator3(operators.get(i));
				continue;
			}
			if (wrapper.getoperator4Score() == null && wrapper.getOperator4() == null) {
				wrapper.setoperator4Score(getOperatorWiseScore(operators.get(i), mappingWrapperList));
				wrapper.setOperator4(operators.get(i));
				continue;
			}
		}
		return wrapper;
	}

	private Double getOperatorWiseScore(String opreatorName, List<IBMappingwrapper> mappingWrapperList) {
		Double coverage = 0.0, quality = 0.0, dlput = 0.0, ulput = 0.0;
		for (IBMappingwrapper wrapper : mappingWrapperList) {
			if (wrapper.getOperatorName().equalsIgnoreCase(opreatorName)) {
				if (wrapper.getKpi().equalsIgnoreCase(KPI.RSRP.toString())) {
					coverage = wrapper.getAvgValue();
				} else if (wrapper.getKpi().equalsIgnoreCase(KPI.SINR.toString())) {
					quality = wrapper.getAvgValue();
				} else if (wrapper.getKpi().equalsIgnoreCase(KPI.DL.toString())) {
					dlput = wrapper.getAvgValue();
				} else if (wrapper.getKpi().equalsIgnoreCase(KPI.UL.toString())) {
					ulput = wrapper.getAvgValue();
				}
			}
		}
		return coverage * 0.2 + quality * 0.3 + dlput * 0.3 + ulput * 0.2;
	}

	private List<IBMappingwrapper> getOverAllScoreList(IBMappingwrapper oveallScoreWrapper) {
		List<IBMappingwrapper> list = new ArrayList<>();
		IBMappingwrapper op1 = new IBMappingwrapper();
		IBMappingwrapper op2 = new IBMappingwrapper();
		IBMappingwrapper op3 = new IBMappingwrapper();
		IBMappingwrapper op4 = new IBMappingwrapper();
		op1.setAvgValue(oveallScoreWrapper.getoperator1Score());
		op1.setOperatorName(oveallScoreWrapper.getOperator1());
		op2.setAvgValue(oveallScoreWrapper.getoperator2Score());
		op2.setOperatorName(oveallScoreWrapper.getOperator2());
		op3.setAvgValue(oveallScoreWrapper.getoperator3Score());
		op3.setOperatorName(oveallScoreWrapper.getOperator3());
		op4.setAvgValue(oveallScoreWrapper.getoperator4Score());
		op4.setOperatorName(oveallScoreWrapper.getOperator4());
		list.add(op1);
		list.add(op2);
		list.add(op3);
		list.add(op4);

		return list;
	}

	private Map<String, String> getImagesForReport(String imgFloorPlan, String localDirPath, List<String[]> arlist,
												   List<KPIWrapper> kpiList, Map<String, Integer> kpiIndexMap) {
		Map<String, String> heatMaps = InBuildingHeatMapGenerator.getInstance().generateHeatMapsForReport(arlist, localDirPath,
				imgFloorPlan, kpiList, kpiIndexMap);
		heatMaps.putAll(getLegendImages(arlist, kpiList, kpiIndexMap));
		junkFiles.addAll(heatMaps.values());
		return heatMaps;
	}

	private HashMap<String, String> getLegendImages(List<String[]> arList, List<KPIWrapper> kpiList, Map<String, Integer> kpiIndexMap) {
		HashMap<String, BufferedImage> bufferdImageMap = mapImagesService.getLegendImagesForIBBenchmark(kpiList,
				arList);
		return mapImagesService.saveDriveImages(bufferdImageMap,
				ConfigUtils.getString(ReportConstants.IB_BENCHMARK_REPORT_TEMP_PATH), false);
	}

	private void setImagesToWrapper(IBBenchmarkImagePlotWrapper imagePlotWrapper, Map<String, String> imagesMap,
			Integer operatorCount, String operatorName, String kpiName, String title) {
		if (ReportConstants.PCI_PLOT.equalsIgnoreCase(kpiName)) {
			imagePlotWrapper.setLegend(imagesMap.get(ReportConstants.PCI_LEGEND));
		} else if (imagesMap.containsKey(ReportConstants.LEGEND + ReportConstants.UNDERSCORE + kpiName)) {
			imagePlotWrapper.setLegend(imagesMap.get(ReportConstants.LEGEND + ReportConstants.UNDERSCORE + kpiName));
		}
		if (title != null) {
			imagePlotWrapper.setTitle(title);
		}
		switch (operatorCount) {
		case ReportConstants.OPERATOR_COUNT_1:
			imagePlotWrapper.setOperator1(operatorName);
			imagePlotWrapper.setOperator1Img(imagesMap.get(kpiName));
			break;
		case ReportConstants.OPERATOR_COUNT_2:
			imagePlotWrapper.setOperator2(operatorName);
			imagePlotWrapper.setOperator2Img(imagesMap.get(kpiName));
			break;
		case ReportConstants.OPERATOR_COUNT_3:
			imagePlotWrapper.setOperator3(operatorName);
			imagePlotWrapper.setOperator3Img(imagesMap.get(kpiName));
			break;
		case ReportConstants.OPERATOR_COUNT_4:
			imagePlotWrapper.setOperator4(operatorName);
			imagePlotWrapper.setOperator4Img(imagesMap.get(kpiName));
			break;
		default:
			break;
		}
	}

	private void setNoAccessDataList(List<DriveDataWrapper> noAccessDataList, String imgFloorPlan,
			IBBenchMarkReportWrapper wrapper, InbuildingPointWrapper pointWrapper, String localDirPath) {
		List<NoAccessWrapper> noAccessimglist = new ArrayList<>();

		logger.info("setNoAccessDataList inside the method set no access data list");
		try {
			String filePath = localDirPath;
			int i = 0;
			for (DriveDataWrapper noAcessData : noAccessDataList) {

				if (noAcessData.getImg() != null) {
					NoAccessWrapper noAccesswrapper = new NoAccessWrapper();
					String floorplanimg = InBuildingHeatMapGenerator.getInstance().drawNoAcessImage(imgFloorPlan,
							filePath + ReportConstants.NOACCESS + i + ".png", noAcessData, pointWrapper);
					noAccesswrapper.setFloorPlanImgPath(floorplanimg);
					junkFiles.add(floorplanimg);
					BufferedImage img = getNoAccessImg(noAcessData);
					if (img != null) {
						ImageIO.write(img, ReportConstants.JPG, new File(filePath + noAcessData.getImageName()));
					}
					noAccesswrapper.setNoAccessImgPath(filePath + noAcessData.getImageName());
					junkFiles.add(filePath + noAcessData.getImageName());
					i++;
					noAccessimglist.add(noAccesswrapper);
				}
			}
//			logger.info("noAccessImgList {}", new Gson().toJson(noAccessimglist));
			wrapper.setNoAccessimglist(noAccessimglist);
		} catch (Exception e) {
			logger.warn("unable to set No Aceess img  {}", Utils.getStackTrace(e));
		}
	}

	private BufferedImage getNoAccessImg(DriveDataWrapper driveDataWrapper) {
		try {
			BufferedImage imBuff = null;

			String localFilepath = nvLayer3hdfsDao.copyFileFromHdfsToLocalPath(driveDataWrapper.getFilePath(),
					ConfigUtils.getString(ReportConstants.IBREPORT_FLOORPLAN_IMAGE_PATH),
					ReportConstants.NOACCESS + ReportConstants.DOT_ZIP);
			junkFiles.add(localFilepath);
			logger.info("Going to process zip file {}  ", localFilepath);
			if (localFilepath != null) {
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
			}
			return imBuff;
		} catch (Exception e) {
			logger.error("Exception inside method getBgImagePath {} ", e.getMessage());
		}
		return null;
	}
}

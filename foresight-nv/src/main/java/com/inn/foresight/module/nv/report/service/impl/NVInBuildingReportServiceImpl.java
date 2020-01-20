package com.inn.foresight.module.nv.report.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inn.commons.Symbol;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.lang.NumberUtils;
import com.inn.commons.lang.StringUtils;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.dao.IBuildingDataDao;
import com.inn.foresight.core.infra.model.Building;
import com.inn.foresight.core.infra.utils.InfraConstants;
import com.inn.foresight.core.report.dao.IAnalyticsRepositoryDao;
import com.inn.foresight.core.report.model.AnalyticsRepository;
import com.inn.foresight.core.report.model.AnalyticsRepository.progress;
import com.inn.foresight.module.nv.NVConfigUtil;
import com.inn.foresight.module.nv.NVConstant;
import com.inn.foresight.module.nv.core.workorder.dao.impl.IGenericWorkorderDao;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder.TemplateType;
import com.inn.foresight.module.nv.inbuilding.model.NVIBUnitResult;
import com.inn.foresight.module.nv.inbuilding.service.INVIBUnitResultService;
import com.inn.foresight.module.nv.layer3.constants.Layer3PPEConstant;
import com.inn.foresight.module.nv.layer3.constants.NVLayer3Constants;
import com.inn.foresight.module.nv.layer3.constants.QMDLConstant;
import com.inn.foresight.module.nv.layer3.dao.INVLayer3HDFSDao;
import com.inn.foresight.module.nv.layer3.service.INVLayer3DashboardService;
import com.inn.foresight.module.nv.layer3.service.parse.INVL3ParsingService;
import com.inn.foresight.module.nv.report.constant.ReportIndexWrapper;
import com.inn.foresight.module.nv.report.constant.ReportSummaryIndexWrapper;
import com.inn.foresight.module.nv.report.dao.INVReportHbaseDao;
import com.inn.foresight.module.nv.report.dao.INVReportHdfsDao;
import com.inn.foresight.module.nv.report.ib.utils.IBReportUtils;
import com.inn.foresight.module.nv.report.ib.utils.floorplandrawer.FloorPlanJsonParser;
import com.inn.foresight.module.nv.report.service.IMapImagesService;
import com.inn.foresight.module.nv.report.service.INVIBBenchmarkReportService;
import com.inn.foresight.module.nv.report.service.INVIBWifiReportService;
import com.inn.foresight.module.nv.report.service.INVInBuildingReportService;
import com.inn.foresight.module.nv.report.service.IReportService;
import com.inn.foresight.module.nv.report.service.Inbuilding.Service.IInbuildingService;
import com.inn.foresight.module.nv.report.service.inbuilding.service.IB1EReportService;
import com.inn.foresight.module.nv.report.utils.DriveHeaderConstants;
import com.inn.foresight.module.nv.report.utils.IBWifiConstants;
import com.inn.foresight.module.nv.report.utils.LegendUtil;
import com.inn.foresight.module.nv.report.utils.MergePDF;
import com.inn.foresight.module.nv.report.utils.ReportConstants;
import com.inn.foresight.module.nv.report.utils.ReportUtil;
import com.inn.foresight.module.nv.report.workorder.wrapper.DriveDataWrapper;
import com.inn.foresight.module.nv.report.workorder.wrapper.KPIDataWrapper;
import com.inn.foresight.module.nv.report.workorder.wrapper.KPIWrapper;
import com.inn.foresight.module.nv.report.wrapper.benchmark.VoiceStatsWrapper;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.INBuildingSubWrapper;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.InBuildingReportWrapper;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.InbuildingPointWrapper;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.NoAccessImgWrapper;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.NoAccessWrapper;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.WalkTestSummaryWrapper;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;
import com.inn.product.legends.dao.ILegendRangeDao;
import com.inn.product.legends.utils.LegendWrapper;

import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service("NVInBuildingReportServiceImpl")
public class NVInBuildingReportServiceImpl implements INVInBuildingReportService, ReportConstants {

	private Logger logger = LogManager.getLogger(NVInBuildingReportServiceImpl.class);
	private String errorCreatingReport = "Exception inside method createIBReport {} ";
	private String recipeAndOpList = "recipeIdList === {} opList ==={}";
	@Autowired
	private ILegendRangeDao legendRangeDao;
	@Autowired
	private IMapImagesService mapImageService;
	@Autowired
	private INVLayer3DashboardService nvLayer3DashboardService;
	@Autowired
	private INVIBUnitResultService nvIBUnitResultService;
	@Autowired
	private INVLayer3HDFSDao nvLayer3hdfsDao;
	@Autowired
	private IGenericWorkorderDao genericWorkorderDao;
	@Autowired
	private IAnalyticsRepositoryDao analyticsRepositoryDao;
	/** The n V report hbase dao. */

	@Autowired
	private INVReportHbaseDao nVReportHbaseDao;
	/** The n V report Hdfs dao. */
	@Autowired
	private INVReportHdfsDao nVReportHdfsDao;

	@Autowired
	private INVL3ParsingService nvL3Parsing;
	@Autowired
	private IBuildingDataDao buildingDataDao;
	@Autowired
	private IReportService reportService;
	@Autowired
	private INVIBBenchmarkReportService nVIBBenchmarkReportService;
	@Autowired
	private INVIBWifiReportService nvIBWifiReportService;
	
	@Autowired
	private IInbuildingService inbuildingOnec;
	@Autowired
	private IB1EReportService ib1eReportService;

	public NVInBuildingReportServiceImpl() {
		super();
	}

	@Override
	@Transactional
	public Response execute(String json) {
		Integer reportinstanceId = null;
		try {
			Map<String, Object> jsonMap = getJsonDataMap(json);
			Integer woId = (Integer) jsonMap.get(WORKORDER_ID);
			Integer analyticsrepoId = (Integer) jsonMap.get(ForesightConstants.ANALYTICAL_REPORT_KEY);
			List<Integer> workOrderIds = getWorkordersFromJson(jsonMap);
			Integer buildingId =(Integer) jsonMap.get(BUILDING_ID_KEY);
			Integer projectId =(Integer) jsonMap.get(PROJECT_ID);
			String assignto = jsonMap.get(NVLayer3Constants.ASSIGN_TO_JSON_KEY) != null ?
					jsonMap.get(NVLayer3Constants.ASSIGN_TO_JSON_KEY).toString() :
					null;
			if (workOrderIds != null && buildingId != null&&jsonMap.containsKey(InfraConstants.TASK_NAME_KEY)&&NVConstant.INBUILDING_ATP_1C.equalsIgnoreCase((String)jsonMap.get(InfraConstants.TASK_NAME_KEY))) {
             return inbuildingOnec.createBuildingLevelInbuildingReport(buildingId, workOrderIds,analyticsrepoId,assignto,projectId,jsonMap);
			}
			 if(workOrderIds != null && jsonMap.containsKey(InfraConstants.TASK_NAME_KEY)&& NVConstant.INBUILDING_ATP_1E.equalsIgnoreCase((String)jsonMap.get(InfraConstants.TASK_NAME_KEY))) {
            	return ib1eReportService.createFloorLevelReport(workOrderIds, analyticsrepoId, jsonMap, true);
			}
			if (woId != null) {
				if (reportService.getFileProcessedStatusForWorkorders(Arrays.asList(woId))) {
					GenericWorkorder workOrder = genericWorkorderDao.findByPk(woId);
					Map<String, String> gwoMeta = workOrder.getGwoMeta();
					String reporttype = gwoMeta.containsKey(InfraConstants.TASK_NAME_KEY) ? gwoMeta.get(InfraConstants.TASK_NAME_KEY) : null;
					if (reporttype != null && reporttype.equalsIgnoreCase(NVConstant.INBUILDING_ATP_1C)) {
						return inbuildingOnec.createFloorLevelInbuildingReport(woId, workOrder, analyticsrepoId,
								jsonMap);
					}
					if (reporttype != null && reporttype.equalsIgnoreCase(NVConstant.INBUILDING_ATP_1E)) {
						return ib1eReportService.createFloorLevelReport(Arrays.asList(woId), analyticsrepoId, jsonMap, false);
					}
				}
			}			
			Map<String, Object> configMap = new ObjectMapper().readValue(json, HashMap.class);
			reportinstanceId = (Integer) configMap.get(ForesightConstants.ANALYTICAL_REPORT_KEY);
			String username = configMap.get(NVLayer3Constants.ASSIGN_TO_JSON_KEY) != null ?
					configMap.get(NVLayer3Constants.ASSIGN_TO_JSON_KEY).toString() :
					null;
			logger.info("reportInstanceId {} ", reportinstanceId);
			String filePath = ConfigUtils.getString(ReportConstants.NV_REPORTS_PATH) + ReportConstants.INBUILDING
					+ ReportConstants.FORWARD_SLASH;
			Map<String, Object> recipeOperatorListMap = getRecipeAndOperatorList(json, reportinstanceId);
			if (recipeOperatorListMap.get(ReportConstants.WORKORDER_ID) != null
					&& recipeOperatorListMap.get(ReportConstants.OPERATOR_LIST) != null) {
				Integer workorderId = Integer.parseInt(
						recipeOperatorListMap.get(ReportConstants.WORKORDER_ID).toString());
				boolean qmdlResponse = reportService.getFileProcessedStatusForWorkorders(Arrays.asList(workorderId));
				if (checkifBenchMarkWorkOrder(json, recipeOperatorListMap) && checkifWIFIWorkOrder(json,
						recipeOperatorListMap)) {
					logger.info("file processsing status {}", qmdlResponse);
					if (qmdlResponse) {
						File file = createIBReportForRecipeId(recipeOperatorListMap.get(ReportConstants.RECIPE_LIST),
								recipeOperatorListMap.get(ReportConstants.OPERATOR_LIST),
								recipeOperatorListMap.get(ReportConstants.WORKORDER_ID), WORKORDERLEVEL,
								reportinstanceId);
						String hdfsFilePath =
								ConfigUtils.getString(ReportConstants.NV_REPORTS_PATH_HDFS) + ReportConstants.INBUILDING
										+ ReportConstants.FORWARD_SLASH;
						return saveFileAndUpdatesStatus(reportinstanceId, hdfsFilePath, recipeOperatorListMap, file);
					}
				}
			}
		} catch (Exception e) {
			logger.error("Exception inside method during creation of IB Report {} ", Utils.getStackTrace(e));
			analyticsRepositoryDao.updateStatusInAnalyticsRepository(reportinstanceId, null, e.getMessage(),
					progress.Failed, null);
			return Response.ok("{\"result\":\"" + e.getLocalizedMessage() + " \"}").build();
		}
		return Response.ok(ForesightConstants.FAILURE_JSON).build();

	}

	private boolean checkifBenchMarkWorkOrder(String json, Map<String, Object> recipeOperatorListMap) {
		logger.info("inside the method checkifBenchMarkWorkOrder ");
		try {
			Integer workorderId = recipeOperatorListMap.get(ReportConstants.WORKORDER_ID) != null ?
					Integer.parseInt(recipeOperatorListMap.get(ReportConstants.WORKORDER_ID).toString()) :
					null;

			GenericWorkorder workorder = genericWorkorderDao.findByPk(workorderId);
			Map<String, String> map = workorder.getGwoMeta();
			if (workorder.getTemplateType().equals(TemplateType.NV_IB_BENCHMARK)) {
				logger.info("Going to process Benchmark workorder report");
				nVIBBenchmarkReportService.execute(json);
				return false;
			}
		} catch (Exception e) {
			logger.error("unable to process BenchMark Report {} ", Utils.getStackTrace(e));
		}
		return true;
	}

	private boolean checkifWIFIWorkOrder(String json, Map<String, Object> recipeOperatorListMap) {
		logger.info("inside the method checkifWIFIWorkOrder ");
		try {
			Integer workorderId = recipeOperatorListMap.get(ReportConstants.WORKORDER_ID) != null ?
					Integer.parseInt(recipeOperatorListMap.get(ReportConstants.WORKORDER_ID).toString()) :
					null;

			GenericWorkorder workorder = genericWorkorderDao.findByPk(workorderId);
			Map<String, String> map = workorder.getGwoMeta();
			if (map.containsKey(ReportConstants.TECHNOLOGY) && ReportConstants.TECHNOLOGY_WIFI.equalsIgnoreCase(
					map.get(ReportConstants.TECHNOLOGY))) {
				logger.info("Going to process WIFI workorder report");
				nvIBWifiReportService.execute(json);
				return false;
			}
		} catch (Exception e) {
			logger.error("unable to process WIFI Report {} ", Utils.getStackTrace(e));
		}
		return true;
	}

	private Response saveFileAndUpdatesStatus(Integer reportinstanceId, String filePath,
			Map<String, Object> recipeOperatorListMap, File file) {
		if (file != null) {
			filePath += file.getName();
			if (nVReportHdfsDao.saveFileToHdfs(file, filePath)) {
				logger.info("File saved Successfully ");
				analyticsRepositoryDao.updateStatusInAnalyticsRepository(reportinstanceId, filePath,
						ReportConstants.HDFS, progress.Generated, null);
				logger.info("Status in report Instance is updated ");
				GenericWorkorder genericWorkorder = genericWorkorderDao.findByPk(
						Integer.parseInt(recipeOperatorListMap.get(ReportConstants.WORKORDER_ID).toString()));
				Map<String, String> geoMap = genericWorkorder.getGwoMeta();
				geoMap.put(NVWorkorderConstant.REPORT_INSTACE_ID, reportinstanceId.toString());
				genericWorkorder.setGwoMeta(geoMap);
				genericWorkorderDao.update(genericWorkorder);
				return Response.ok(ForesightConstants.SUCCESS_JSON).build();
			} else {
				return Response.ok(ForesightConstants.FAILURE_JSON).build();
			}

		} else {
			return Response.ok(NVConstant.REPORT_NOT_FOUND_JSON).build();
		}
	}

	private Map<String, Object> getRecipeAndOperatorList(String json, Integer reportinstanceId) {
		logger.info("Inside method getRecipeAndOperatorList from json {} ", json);
		Map<String, Object> recipeAndOperatorListMap = new HashMap<>();
		try {
			AnalyticsRepository reportInstanceObj = analyticsRepositoryDao.findByPk(reportinstanceId);
			Map jsonMap = new ObjectMapper().readValue(reportInstanceObj.getReportConfig().replaceAll("\'", "\""),
					HashMap.class);
			Integer recipeId = (Integer) jsonMap.get(RECIPEID);
			String operator = (String) jsonMap.get(OPERATOR);
			Integer buildingId = (Integer) jsonMap.get(INBUILDINGID);
			Integer workorderId = (Integer) jsonMap.get(WORKORDER_ID);
			List<Integer> recipeList = null;
			List<String> operatorList = null;
			if (recipeId != null && !StringUtils.isEmpty(operator) && workorderId != null) {
				Map<String, List<String>> map = nvLayer3DashboardService.getDriveRecipeDetail(workorderId);
				if (map != null) {
					List<String> recipeIdList = map.get(QMDLConstant.RECIPE);
					List<String> opList = map.get(QMDLConstant.OPERATOR);
					logger.info(recipeAndOpList, recipeIdList, opList);
					int i = 0;
					for (String string : recipeIdList) {
						if (string.equalsIgnoreCase(recipeId.toString())) {
							recipeList = new ArrayList<>(Arrays.asList(recipeId));
							operatorList = new ArrayList<>(Arrays.asList(opList.get(i)));
						}
						i++;
					}
				}

			} else if (workorderId != null) {
				Map<String, List<String>> map = nvLayer3DashboardService.getDriveRecipeDetail(workorderId);
				if (map != null) {
					List<String> recipeIdList = map.get(QMDLConstant.RECIPE);
					logger.info("recipeIdList {}", recipeIdList);
					recipeList = recipeIdList.stream()
											 .map(id -> NumberUtils.toInteger(id))
											 .collect(Collectors.toList());
					operatorList = map.get(QMDLConstant.OPERATOR);
				}
			} else if (buildingId != null) {
				List<Integer> workorderIdList = genericWorkorderDao.getListOfWorkOrderIdByBuildingId(buildingId,
						ReportConstants.TECHNOLOGY_LTE);
				Map<String, List<String>> map = nvLayer3DashboardService.getRecipeDetailByWorkorderList(
						workorderIdList);
				if (map != null) {
					List<String> recipeIdList = map.get(QMDLConstant.RECIPE);
					logger.info("recipeIdList {}", recipeIdList);
					recipeList = recipeIdList.stream().map(NumberUtils::toInteger).collect(Collectors.toList());
					operatorList = map.get(QMDLConstant.OPERATOR);
				}
			}
			recipeAndOperatorListMap.put(ReportConstants.RECIPE_LIST, recipeList);
			recipeAndOperatorListMap.put(ReportConstants.OPERATOR_LIST, operatorList);
			recipeAndOperatorListMap.put(ReportConstants.WORKORDER_ID, workorderId);
		} catch (Exception e) {
			logger.error("Exception inside method getRecipeAndOperatorList {} ", Utils.getStackTrace(e));
		}
		logger.info("recipeOperatorListMap Size {} ,data {}  ", recipeAndOperatorListMap.size(),
				recipeAndOperatorListMap);
		return recipeAndOperatorListMap;
	}

	@Override
	public File createIBReportForRecipeId(Object recipeList, Object OperatorList, Object workOrderid, String level,
			Integer reportinstanceId) {
		logger.info("Inside method createIBReportFor RecipeId  recipeList  {} ,OperatorList {} , workOrderid {} ",
				recipeList, OperatorList, workOrderid);
		InBuildingReportWrapper mainWrapper = new InBuildingReportWrapper();
		try {
			List<Integer> recipeidList = (List<Integer>) recipeList;
			List<String> operatorList = (List<String>) OperatorList;
			Integer workorderId = workOrderid != null ? Integer.parseInt(workOrderid.toString()) : null;
			List<Integer> idList = recipeidList.stream()
											   .map(id -> NumberUtils.toInteger(id))
											   .collect(Collectors.toList());
			logger.info("operatorList {} , recipeidList {} ", operatorList, idList);
			
			/** Summary Data Fetch */
			List<String> fetchSummaryKPIList = ReportSummaryIndexWrapper.getLiveDriveKPIs();

			Map<String, Integer> summaryKpiIndexMap = ReportSummaryIndexWrapper.getLiveDriveKPIIndexMap(fetchSummaryKPIList);

			List<WalkTestSummaryWrapper> summaryList = nvIBUnitResultService.getIBUnitResultForRecipe(idList);
			addAtDtCallPingDataToSummary(workorderId, idList, operatorList, summaryList, fetchSummaryKPIList, summaryKpiIndexMap);
			
			logger.info("summary list size is: {}", summaryList.size());
			if (summaryList.size() > ReportConstants.INDEX_ZER0) {
				GenericWorkorder workOrder = genericWorkorderDao.findByPk(workorderId);
				setFileName(mainWrapper, workOrder, idList, level, summaryList);
				setPdschPuschForWrapper(summaryList, mainWrapper);
				setSummaryListToWrapper(mainWrapper, summaryList, getBuildingFromWorkOrder(workOrder));
				File file = getSummaryReport(mainWrapper);
				List<File> files = getFileListByRecipeWise(workorderId, summaryList, operatorList);
				files.add(ReportConstants.INDEX_ZER0, file);
				MergePDF.mergeFiles(mainWrapper.getFileName(), files);
				return ReportUtil.getIfFileExists(mainWrapper.getFileName());
			}
		} catch (Exception e) {
			logger.error("Error inside the method createIBReportForRecipeId{}", Utils.getStackTrace(e));
		}
		return null;
	}

	private void addAtDtCallPingDataToSummary(Integer workorderId, List<Integer> recipeidList,
			List<String> operatorList, List<WalkTestSummaryWrapper> summaryList, List<String> columnList, Map<String, Integer> summaryKPIIndexMap) {
		Map<Integer, String[]> recipeWiseSummaryMap = getRecipeWiseSummaryData(workorderId, recipeidList, operatorList, columnList);

		for (WalkTestSummaryWrapper walkTestSummary : summaryList) {
			Integer recipeId = walkTestSummary.getWoRecipeMappingId();
			if (recipeWiseSummaryMap.containsKey(recipeId)) {
				String[] driveSummary = recipeWiseSummaryMap.get(recipeId);
				if (ReportUtil.checkIndexValue(summaryKPIIndexMap.get(ReportConstants.SUM_UNDERSCORE + ReportConstants.ATTACH_REQUEST), driveSummary)
						&& ReportUtil.checkIndexValue(summaryKPIIndexMap.get(ReportConstants.SUM_UNDERSCORE + ReportConstants.ATTACH_COMPLETE), driveSummary)) {
					walkTestSummary.setAttachSuccessRate(ReportUtil.getKpiSuccessRate(
							Integer.parseInt(driveSummary[summaryKPIIndexMap.get(ReportConstants.SUM_UNDERSCORE + ReportConstants.ATTACH_REQUEST)]),
							Integer.parseInt(driveSummary[summaryKPIIndexMap.get(ReportConstants.SUM_UNDERSCORE + ReportConstants.ATTACH_COMPLETE)]), false,
							null));
				}
				if (ReportUtil.checkIndexValue(summaryKPIIndexMap.get(ReportConstants.SUM_UNDERSCORE + ReportConstants.DETACH_REQUEST), driveSummary)) {
					walkTestSummary.setDetachSuccessRate(ReportUtil.getKpiSuccessRate(
							Integer.parseInt(driveSummary[summaryKPIIndexMap.get(ReportConstants.SUM_UNDERSCORE + ReportConstants.DETACH_REQUEST)]),
							Integer.parseInt(driveSummary[summaryKPIIndexMap.get(ReportConstants.SUM_UNDERSCORE + ReportConstants.DETACH_REQUEST)]), false, null));
				}

				if (ReportUtil.checkIndexValue(summaryKPIIndexMap.get(ReportConstants.CONNECTION_SETUP_TIME), driveSummary)) {
					walkTestSummary.setAttachLatency(
							ReportUtil.round(NumberUtils.toDouble(driveSummary[summaryKPIIndexMap.get(ReportConstants.CONNECTION_SETUP_TIME)]),
							ReportConstants.TWO_DECIMAL_PLACES));
				}
				if (ReportUtil.checkIndexValue(summaryKPIIndexMap.get(ReportConstants.MSG1_COUNT), driveSummary)
						&& ReportUtil.checkIndexValue(summaryKPIIndexMap.get(ReportConstants.MSG3_COUNT), driveSummary)) {
					walkTestSummary.setRachSuccessRate(ReportUtil.getKpiSuccessRate(
							Integer.parseInt(driveSummary[summaryKPIIndexMap.get(ReportConstants.MSG1_COUNT)]),
							Integer.parseInt(driveSummary[summaryKPIIndexMap.get(ReportConstants.MSG3_COUNT)]), false,
							null));
				}

				if (ReportUtil.checkIndexValue(summaryKPIIndexMap.get(ReportConstants.TOTAL_PACKET_COUNT), driveSummary)
						&& ReportUtil.checkIndexValue(summaryKPIIndexMap.get(ReportConstants.RTP_PACKET_LOSS), driveSummary)) {
					walkTestSummary.setRtpPacketLossRate(String.valueOf((ReportUtil.getPercentage(
							Double.parseDouble(driveSummary[summaryKPIIndexMap.get(ReportConstants.RTP_PACKET_LOSS)]),
							Double.parseDouble(driveSummary[summaryKPIIndexMap.get(ReportConstants.TOTAL_PACKET_COUNT)])))));

				}
				if (ReportUtil.checkIndexValue(summaryKPIIndexMap.get(ReportConstants.CALL_INITIATE), driveSummary) && ReportUtil
						.checkIndexValue(summaryKPIIndexMap.get(ReportConstants.CALL_SETUP_SUCCESS), driveSummary)) {
					walkTestSummary.setCallSetupSuccess(ReportUtil.getKpiSuccessRate(
							Integer.parseInt(driveSummary[summaryKPIIndexMap.get(ReportConstants.CALL_INITIATE)]),
							Integer.parseInt(driveSummary[summaryKPIIndexMap.get(ReportConstants.CALL_SETUP_SUCCESS)]),
							false, null));
				}
				if (ReportUtil.checkIndexValue(summaryKPIIndexMap.get(ReportConstants.IMS_REGISTRATION_TIME), driveSummary)) {
					logger.info("value of IMS Registration in summary : {}",
							driveSummary[summaryKPIIndexMap.get(ReportConstants.IMS_REGISTRATION_TIME)]);
					walkTestSummary.setImsRegistrationSetupTime(ReportUtil.round(NumberUtils.toDouble(driveSummary[summaryKPIIndexMap.get(ReportConstants.IMS_REGISTRATION_TIME)])/1000,
							ReportConstants.TWO_DECIMAL_PLACES));
				}
				if (ReportUtil.checkIndexValue(summaryKPIIndexMap.get(ReportConstants.CALL_SETUP_TIME), driveSummary)) {
					logger.info("value of call connection setup time in summary : {}",
							driveSummary[DriveHeaderConstants.SUMMARY_CALL_SETUP_TIME]);
					walkTestSummary.setCallConnectionSetupTime(ReportUtil.round(
							NumberUtils.toDouble(driveSummary[summaryKPIIndexMap.get(ReportConstants.CALL_SETUP_TIME)]),
							ReportConstants.TWO_DECIMAL_PLACES));
				}
				if (ReportUtil.checkIndexValue(summaryKPIIndexMap.get(ReportConstants.VOLTE_JITTER), driveSummary)) {
					logger.info("value of volte jitter in summary : {}",
							driveSummary[DriveHeaderConstants.SUMMARY_VOLTE_JITTER_DLF]);
					walkTestSummary.setVoLTEJitter(ReportUtil.round(
							NumberUtils.toDouble(driveSummary[summaryKPIIndexMap.get(ReportConstants.VOLTE_JITTER)]),
							ReportConstants.TWO_DECIMAL_PLACES));
				}

				if (ReportUtil.checkIndexValue(summaryKPIIndexMap.get(ReportConstants.VOLTE_PACKET_LOSS), driveSummary)) {
				 	logger.info("value of volte packet loss in summary : {}",driveSummary[summaryKPIIndexMap.get(ReportConstants.VOLTE_PACKET_LOSS)]);
				 	walkTestSummary.setVoLTEPacketLoss(ReportUtil.round(NumberUtils.toDouble(driveSummary[summaryKPIIndexMap.get(ReportConstants.VOLTE_PACKET_LOSS)]),
								ReportConstants.TWO_DECIMAL_PLACES));
				}
				if (ReportUtil.checkIndexValue(summaryKPIIndexMap.get(ReportConstants.CALL_INITIATE), driveSummary)
						&& ReportUtil.checkIndexValue(summaryKPIIndexMap.get(ReportConstants.CALL_DROP), driveSummary)) {
					walkTestSummary.setCallDroppedRate(ReportUtil.getKpiSuccessRate(
							Integer.parseInt(driveSummary[summaryKPIIndexMap.get(ReportConstants.CALL_INITIATE)]),
							Integer.parseInt(driveSummary[summaryKPIIndexMap.get(ReportConstants.CALL_DROP)]), false, null));
				}
				if (ReportUtil.checkIndexValue(summaryKPIIndexMap.get(ReportConstants.MOS), driveSummary)) {
					walkTestSummary.setAvgMos(
							ReportUtil.round(NumberUtils.toDouble(driveSummary[summaryKPIIndexMap.get(ReportConstants.MOS)]),
									ReportConstants.TWO_DECIMAL_PLACES));
				}

				if (ReportUtil.checkIndexValue(summaryKPIIndexMap.get(ReportConstants.CALL_INITIATE), driveSummary)) {
					walkTestSummary.setCallInitCount(driveSummary[summaryKPIIndexMap.get(ReportConstants.CALL_INITIATE)]);
				}
				if (ReportUtil.checkIndexValue(summaryKPIIndexMap.get(ReportConstants.CALL_DROP), driveSummary)) {
					walkTestSummary.setCallDropCount(driveSummary[summaryKPIIndexMap.get(ReportConstants.CALL_DROP)]);
				}
				if (ReportUtil.checkIndexValue(summaryKPIIndexMap.get(ReportConstants.CALL_SUCCESS), driveSummary)) {
					walkTestSummary.setCallSuccessCount(driveSummary[summaryKPIIndexMap.get(ReportConstants.CALL_SUCCESS)]);
				}
				if (ReportUtil.checkIndexValue(summaryKPIIndexMap.get(ReportConstants.CALL_FAILURE), driveSummary)) {
					walkTestSummary.setCallFailCount(driveSummary[summaryKPIIndexMap.get(ReportConstants.CALL_FAILURE)]);
				}
				setLatencyData(walkTestSummary, driveSummary, summaryKPIIndexMap);
			}
		}
	}

	private void setLatencyData(WalkTestSummaryWrapper walkTestSummary, String[] driveSummary, Map<String, Integer> summaryKPIIndexMap) {
		List<KPIDataWrapper> kpiwrapperlist = new ArrayList<>();
		KPIDataWrapper wrapper = new KPIDataWrapper();
		String pingBufferSize = "";
		walkTestSummary.setShowPingRecipe(false);
		if (driveSummary.length > summaryKPIIndexMap.get(PING_BUFFER_SIZE)
				&& driveSummary[summaryKPIIndexMap.get(PING_BUFFER_SIZE)] != null
				&& !driveSummary[summaryKPIIndexMap.get(PING_BUFFER_SIZE)].isEmpty()) {
			pingBufferSize = driveSummary[summaryKPIIndexMap.get(PING_BUFFER_SIZE)];
		//	walkTestSummary.setShowPingRecipe(true);
		}
		if (pingBufferSize.contains("32")) {
			wrapper.setKpiId(32);
			if (ReportUtil.checkIndexValue(summaryKPIIndexMap.get(MIN_UNDERSCORE + LATENCY32), driveSummary)) {
				wrapper.setMinValue(
						Double.parseDouble(driveSummary[summaryKPIIndexMap.get(MIN_UNDERSCORE + LATENCY32)]));
			}
			if (ReportUtil.checkIndexValue(summaryKPIIndexMap.get(MAX_UNDERSCORE + LATENCY32), driveSummary)) {
				wrapper.setMaxValue(
						Double.parseDouble(driveSummary[summaryKPIIndexMap.get(MAX_UNDERSCORE + LATENCY32)]));
			}
			if (ReportUtil.checkIndexValue(summaryKPIIndexMap.get(AVG_UNDERSCORE + LATENCY32), driveSummary)) {
				wrapper.setAvgValue(
						Double.parseDouble(driveSummary[summaryKPIIndexMap.get(AVG_UNDERSCORE + LATENCY32)]));
			}
			kpiwrapperlist.add(wrapper);
//			logger.info("getting wrapper for latency32: {}", wrapper);
			wrapper = new KPIDataWrapper();
		} else {
			kpiwrapperlist.add(wrapper);
			wrapper = new KPIDataWrapper();
		}
		if (pingBufferSize.contains("1000")) {
			wrapper.setKpiId(1000);
			if (ReportUtil.checkIndexValue(summaryKPIIndexMap.get(MIN_UNDERSCORE + LATENCY1000), driveSummary)) {
				wrapper.setMinValue(
						Double.parseDouble(driveSummary[summaryKPIIndexMap.get(MIN_UNDERSCORE + LATENCY1000)]));
			}
			if (ReportUtil.checkIndexValue(summaryKPIIndexMap.get(MAX_UNDERSCORE + LATENCY1000), driveSummary)) {
				wrapper.setMaxValue(
						Double.parseDouble(driveSummary[summaryKPIIndexMap.get(MAX_UNDERSCORE + LATENCY1000)]));
            }
			if (ReportUtil.checkIndexValue(summaryKPIIndexMap.get(AVG_UNDERSCORE + LATENCY1000), driveSummary)) {
				wrapper.setAvgValue(
						Double.parseDouble(driveSummary[summaryKPIIndexMap.get(AVG_UNDERSCORE + LATENCY1000)]));
			}
			kpiwrapperlist.add(wrapper);
			wrapper = new KPIDataWrapper();
		} else {
			kpiwrapperlist.add(wrapper);
			wrapper = new KPIDataWrapper();
		}
		if (pingBufferSize.contains("1500")) {
			wrapper.setKpiId(1500);
			if (ReportUtil.checkIndexValue(summaryKPIIndexMap.get(MIN_UNDERSCORE + LATENCY1500), driveSummary)) {
				wrapper.setMinValue(
						Double.parseDouble(driveSummary[summaryKPIIndexMap.get(MIN_UNDERSCORE + LATENCY1500)]));
			}
			if (ReportUtil.checkIndexValue(summaryKPIIndexMap.get(MAX_UNDERSCORE + LATENCY1500), driveSummary)) {
				wrapper.setMaxValue(
						Double.parseDouble(driveSummary[summaryKPIIndexMap.get(MAX_UNDERSCORE + LATENCY1500)]));
			}
			if (ReportUtil.checkIndexValue(summaryKPIIndexMap.get(AVG_UNDERSCORE + LATENCY1500), driveSummary)) {
				wrapper.setAvgValue(
						Double.parseDouble(driveSummary[summaryKPIIndexMap.get(AVG_UNDERSCORE + LATENCY1500)]));
			}
			kpiwrapperlist.add(wrapper);
			wrapper = new KPIDataWrapper();
		} else {
			kpiwrapperlist.add(wrapper);
			wrapper = new KPIDataWrapper();
		}
		if (kpiwrapperlist != null && kpiwrapperlist.size() > 0) {
			
			int size = kpiwrapperlist.size();
			int loopsize = 3 - size;
			for (int i = 0; i < loopsize; i++) {
				kpiwrapperlist.add(wrapper);
			}
		} else {
			for (int i = 0; i < 3; i++) {
				kpiwrapperlist.add(wrapper);
				}
			}
		
		walkTestSummary.setBufferWiseKpiList(kpiwrapperlist);
	}



	private Map<Integer, String[]> getRecipeWiseSummaryData(Integer workorderId, List<Integer> recipeidList,
			List<String> operatorList, List<String> columnList) {
		Map<Integer, String[]> recipeWiseSummaryMap = new HashMap<>();
		logger.info("recipeId list: {}", recipeidList);
		for (Integer recipe : recipeidList) {
			Map<String, List<String>> recipeOperatorMap = new HashMap<>();
			recipeOperatorMap.put(RECIPE, Arrays.asList(new String[] { String.valueOf(recipe) }));
			recipeOperatorMap.put(OPERATOR, operatorList);
			String[] summaryData = reportService.getSummaryDataForReport(workorderId, Arrays.asList(String.valueOf(recipe)), columnList);

			recipeWiseSummaryMap.put(recipe, summaryData);
		}
		return recipeWiseSummaryMap;
	}

	private Building getBuildingFromWorkOrder(GenericWorkorder workOrder) {
		logger.info("going to get GWOMeta using workorder id: {}", workOrder);
		Map<String, String> gwoMeta = workOrder.getGwoMeta();
//		logger.info("gwoMeta=====: {}",  new Gson().toJson(gwoMeta));
		String buidingId = gwoMeta.get(NVWorkorderConstant.BUILDING_ID);
		logger.info("getting building id: {}",buidingId);
		if (buidingId != null) {
			return buildingDataDao.findByPk(Integer.parseInt(buidingId));
		}
		return null;
	}

	private void setFileName(InBuildingReportWrapper mainWrapper, GenericWorkorder workOrder, List<Integer> idList,
			String level, List<WalkTestSummaryWrapper> summaryList) {
		logger.info("inside set fileName");
		
		if (RECEPILEVEL.equalsIgnoreCase(level)) {
			mainWrapper.setFileName(ReportUtil.getFileName(summaryList.get(ReportConstants.INDEX_ZER0).getRecipeName(),
					idList.get(ReportConstants.INDEX_ZER0)));
		} else {
			mainWrapper.setFileName(ReportUtil.getFileName(workOrder.getWorkorderId(), workOrder.getId()));
		}
	}

	private void setSummaryListToWrapper(InBuildingReportWrapper mainWrapper, List<WalkTestSummaryWrapper> summaryList,
			Building building) {
//		logger.info("summaryList {}", new Gson().toJson(summaryList));
//		logger.info("building : {}", building);
		List<INBuildingSubWrapper> subList = new ArrayList<>();
		INBuildingSubWrapper subWrapper = new INBuildingSubWrapper();
		if (summaryList != null && summaryList.size() > ReportConstants.INDEX_ZER0) {
			subWrapper.setBuildingName(building.getBuildingName());
			subWrapper.setFloorName(summaryList.get(ReportConstants.INDEX_ZER0).getFloorNumber());
			mainWrapper.setDate(summaryList.get(ReportConstants.INDEX_ZER0).getDate());
		}
		setBuildingDataToWrapper(mainWrapper, building);
		//setPdschPuschForWrapper(summaryList,Wrapper);
		subWrapper.setSumaaryList(summaryList);
		subList.add(subWrapper);

		mainWrapper.setSubList(subList);
	}

	public void setPdschPuschForWrapper(List<WalkTestSummaryWrapper> summaryList, InBuildingReportWrapper Wrapper) {

		for (WalkTestSummaryWrapper summary : summaryList) {
			if (summary.getAvgPDSCHDL() != null && !summary.getAvgPDSCHDL().toString().isEmpty()) {
				Wrapper.setPdschStatus(true);
			}
			if (summary.getAvgPUSCHUL() != null && !summary.getAvgPUSCHUL().toString().isEmpty()) {
				Wrapper.setPuschStatus(true);
			}
		}
	}

	private void setBuildingDataToWrapper(InBuildingReportWrapper mainWrapper, Building building) {
		String address = null;
		if (building != null) {
			mainWrapper.setLatitude(building.getLatitude());
			mainWrapper.setLongitude(building.getLongitude());
			address = building.getAddress();
			if (address != null) {
				mainWrapper.setAddress(StringEscapeUtils.unescapeHtml(building.getAddress().replace("\\", "/")));
			} else {
				address = ReportUtil.getAddressByLatLon(building.getLatitude(), building.getLongitude());
				if (address != null) {
					address = address.replace("\\", "/");
					mainWrapper.setAddress(StringEscapeUtils.unescapeHtml(address));
				}
			}
		}
	}

	private File getSummaryReport(InBuildingReportWrapper mainWrapper) {
		logger.info("inside the method getSummaryReport ");
		try {
			Map<String, Object> imageMap = new HashMap<>();
			String reportAssetsPath = ConfigUtils.getString(INBUILDING_REPORT_SUMMARY_PATH);
			List<InBuildingReportWrapper> dataSourceList = new ArrayList<>();
			dataSourceList.add(mainWrapper);
			JRBeanCollectionDataSource rfbeanColDataSource = new JRBeanCollectionDataSource(dataSourceList);

			imageMap.put(SUBREPORT_DIR, reportAssetsPath);
			imageMap.put(IMAGE_PARAM_HEADER_LOGO, reportAssetsPath + IMAGE_HEADER_LOGO);
			imageMap.put(LOGO_NV_KEY, reportAssetsPath + LOGO_NV_IMG);
			imageMap.put(IMAGE_PARAM_FOOTER, reportAssetsPath + IMAGE_FOOTER);
			String destinationFileName = mainWrapper.getFileName();
			
			if(destinationFileName.contains(Symbol.PLUS_STRING)) {
				destinationFileName=destinationFileName.replace(Symbol.PLUS_STRING, Symbol.HYPHEN_STRING);
			}
			
			JasperRunManager.runReportToPdfFile(reportAssetsPath + MAIN_JASPER, destinationFileName, imageMap,
					rfbeanColDataSource);
			logger.info("Report Created successfully  ");
			return ReportUtil.getIfFileExists(destinationFileName);
		} catch (Exception e) {
			logger.error("@getSummaryReport getting err={}", Utils.getStackTrace(e));
		}
		logger.info("@getSummaryReport going to return null as there has been some problem in generating report");
		return null;

	}

	private List<File> getFileListByRecipeWise(Integer workorderId, List<WalkTestSummaryWrapper> summaryList,
			List<String> operatorList) {
		List<File> files = new ArrayList<>();
		Integer index = ReportConstants.INDEX_ZER0;

		for (WalkTestSummaryWrapper walkTestsummar : summaryList) {
			try {
				File file = generateReportRecipeWise(workorderId, walkTestsummar, getOperatorName(operatorList, index));
				if (file != null) {
					logger.info("inside the file not null for index: {}", index);
					files.add(file);
				}
				index++;
			} catch (NullPointerException ne) {
				logger.info("NullPointerException inside the method createIBReportForRecipeId{} ",
						Utils.getStackTrace(ne));

			} catch (Exception e) {
				logger.info("Exception inside the method createIBReportForRecipeId{} ", Utils.getStackTrace(e));
			}
		}
		return files;
	}

	private String getOperatorName(List<String> operatorList, Integer i) {
		String opName = null;
		try {
			opName = operatorList.get(i) != null ?
					operatorList.get(i) :
					ConfigUtils.getString(NVConfigUtil.OPERATOR_DEFAULT);
		} catch (Exception e) {
			logger.error("Exception inside the method getOperatorName {}", e.getMessage());

		}

		return opName;
	}

	private File generateReportRecipeWise(Integer workorderId, WalkTestSummaryWrapper walkTestsummar, String opName) {
		String localDirPath = ConfigUtils.getString(INBUILDING_REPORT_PATH) + new Date().getTime();
		ReportUtil.createDirectory(localDirPath);
		try {
			logger.info("generateReportRecipeWise for recipeId: {} and workorderId", walkTestsummar.getWoRecipeMappingId(), workorderId);

			INBuildingSubWrapper wrapper = setWalkTestSummary(walkTestsummar, opName);
			wrapper.setCallPlotDataList(setCallData(walkTestsummar,wrapper));
			wrapper.setFileName(ReportUtil.getFileName(RECEPILEVEL, walkTestsummar.getWoRecipeMappingId()));

			Map<String, Object> images = new HashMap<>();
			DriveDataWrapper driveDataWrapper = getDataWrapperForRecipeId(workorderId,
					walkTestsummar.getWoRecipeMappingId());
			logger.info("floorplanJson {}", driveDataWrapper.getJson());
			if (driveDataWrapper.getJson() != null) {
				String imgFloorPlan = getFloorplanImg(walkTestsummar.getWoRecipeMappingId(), opName, driveDataWrapper,
						driveDataWrapper.getJson(), localDirPath);
				logger.info("image floor plan {}",imgFloorPlan);

				/** fetch drive data */
				logger.info("going to get drive data for workorderid: {}", workorderId);
				Set<String> dynamicKpis = reportService.getDynamicKpiName(Arrays.asList(workorderId), null, Layer3PPEConstant.ADVANCE);

				List<String> fetchKPIList = ReportIndexWrapper.getLiveDriveKPIs()
						.stream()
						.filter(k -> dynamicKpis.contains(k))
						.collect(Collectors.toList());

				Map<String, Integer> kpiIndexMap = ReportIndexWrapper.getLiveDriveKPIIndexMap(fetchKPIList);

//				logger.info("fetchKPIList === {}",fetchKPIList);
				List<String[]> arlist = getInbuildingDriveData(workorderId, walkTestsummar, fetchKPIList);

//				logger.info("driveData === {}",arlist);
				List<DriveDataWrapper> noAccessDataList = nVReportHbaseDao.getNoAcessDataFromLayer3Report(workorderId,
						walkTestsummar.getWoRecipeMappingId() + "");

//				logger.info("imgFloorPlan ===> {}", imgFloorPlan);
				InbuildingPointWrapper pointWrapper = IBReportUtils.getInstance().drawFloorPlan(imgFloorPlan,
																	driveDataWrapper.getJson(), 
																	arlist, kpiIndexMap.get(X_POINT), kpiIndexMap.get(Y_POINT));
				modifyKPIIndexMapForMimo(kpiIndexMap);
				List<KPIWrapper> kpiList = getKpiWrapperList(workorderId, walkTestsummar.getWoRecipeMappingId() + "",
						opName, kpiIndexMap);
				images = getImagesForReport(imgFloorPlan, localDirPath, pointWrapper.getArlist(), kpiList, kpiIndexMap);
				if (noAccessDataList.size() > ReportConstants.INDEX_ZER0) {
					setNoAccessDataList(noAccessDataList, imgFloorPlan, wrapper, pointWrapper, localDirPath);
					if (Utils.isValidList(wrapper.getNoAccessimglist())) {
//					logger.info("noAccessDataList {}",wrapper.getNoAccessimglist());
						wrapper.setIsNoAccessAvailable(ReportConstants.TRUE);

					}
				}
//			logger.debug("wrapper {}", new Gson().toJson(wrapper));

				return proceedToCreatePlot(wrapper, images);
			}
		} catch (Exception e) {
			logger.error(" Exception inside the method {}", Utils.getStackTrace(e));
		} finally {
			try {
				FileUtils.deleteDirectory(new File(localDirPath));
			} catch (IOException e) {
				logger.error("Error in deleting directory localDirPath {} message {}", localDirPath, e.getMessage());
			} catch (Exception e) {
				logger.error("Error occuring to delte the directory {}", e.getMessage());
			}
		}
		return null;
	}
	
	private void modifyKPIIndexMapForMimo(Map<String, Integer> kpiIndexMap) {
		if(kpiIndexMap.containsKey(RI)) {
			kpiIndexMap.put(MIMO, kpiIndexMap.get(RI));
		}
	}

	@Override
	public DriveDataWrapper getDataWrapperForRecipeId(Integer woId, Integer recipeId) {
		GenericWorkorder workorder = genericWorkorderDao.findByPk(woId);
		DriveDataWrapper driveDataWrapper = null;
		logger.info("workorder.getTemplateType() : {}", workorder.getTemplateType());
		if (GenericWorkorder.TemplateType.NV_IB_BENCHMARK.equals(workorder.getTemplateType())) {
			logger.info("getDataWrapperForRecipeId inside if ");
			driveDataWrapper = nvLayer3DashboardService.getFloorplanDataFromLayer3ReportForFramework(woId,
					String.valueOf(recipeId));
		} else {
			logger.info("getDataWrapperForRecipeId inside else ");
			driveDataWrapper = nvLayer3DashboardService.getFloorplanDataFromLayer3ReportForFramework(woId,
					String.valueOf(recipeId));
		}
		return driveDataWrapper;
	}

	private List<String[]> getInbuildingDriveData(Integer workorderId, WalkTestSummaryWrapper walkTestsummar,
									List<String> fetchKPIList) throws Exception {
		logger.info("inside the Method getInbuildingDriveData for workorder ID {}", workorderId);
		List<String> recepi = new ArrayList<>();
		recepi.add(walkTestsummar.getWoRecipeMappingId().toString());

		List<String[]> arlist = reportService.getDriveDataForReport(workorderId, recepi, fetchKPIList);
		return arlist;
	}

	private void setNoAccessDataList(List<DriveDataWrapper> noAccessDataList, String imgFloorPlan,
			INBuildingSubWrapper wrapper, InbuildingPointWrapper pointWrapper, String localDirPath) {
		List<NoAccessWrapper> list = new ArrayList<>();
		NoAccessImgWrapper imgWrapper = new NoAccessImgWrapper();
		List<NoAccessImgWrapper> imgList = new ArrayList<>();

		logger.info("setNoAccessDataList inside the method set no access data list");
		try {
			String filePath = localDirPath + ReportConstants.FORWARD_SLASH;
			int i = ReportConstants.INDEX_ZER0;
			for (DriveDataWrapper noAcessData : noAccessDataList) {
				NoAccessWrapper noAccesswrapper = new NoAccessWrapper();
				String floorplanimg = InBuildingHeatMapGenerator.getInstance()
																.drawNoAcessImage(imgFloorPlan, filePath + NOACCESS + i
																				+ ReportConstants.DOT_PNG, noAcessData,
																		pointWrapper);
				noAccesswrapper.setFloorPlanImgPath(floorplanimg);
				BufferedImage img = getNoAccessImg(noAcessData);
				if (img != null) {
					ImageIO.write(img, ReportConstants.JPG, new File(filePath + noAcessData.getImageName()));
					noAccesswrapper.setNoAccessImgPath(filePath + noAcessData.getImageName());
					list.add(noAccesswrapper);

				}
				i++;
			}
			if (Utils.isValidList(list)) {
				imgWrapper.setImageList(list);
				imgList.add(imgWrapper);
				wrapper.setNoAccessimglist(imgList);

			}
		} catch (Exception e) {
			logger.warn("unable to set No Aceess img  {}", Utils.getStackTrace(e));
		}
	}

	private INBuildingSubWrapper setWalkTestSummary(WalkTestSummaryWrapper walkTestsummar, String opName) {
		List<WalkTestSummaryWrapper> sumaaryList = new ArrayList<>();
		sumaaryList.add(walkTestsummar);
		INBuildingSubWrapper wrapper = new INBuildingSubWrapper();
		wrapper.setUnitName(walkTestsummar.getUnitName());
		wrapper.setRecipeName(walkTestsummar.getRecipeName());
		wrapper.setBuildingName(walkTestsummar.getBuildingName());
		wrapper.setFloorName(walkTestsummar.getFloorNumber());
		wrapper.setWingName(walkTestsummar.getWingName());
		wrapper.setSumaaryList(sumaaryList);
		wrapper.setOperatorName(opName);
		return wrapper;
	}

	@Override
	public String getFloorplanImg(Integer recepiMappingId, String opName, DriveDataWrapper driveDataWrapper,
			String floorplanJson, String localDirPath) throws IOException {
		String floorPlanImagePath =
				localDirPath + Symbol.SLASH_FORWARD + IB_REPORT_IMAGE_NAME_PREFIX + recepiMappingId + UNDERSCORE
						+ opName + IMAGE_FILE_EXTENSION;
		logger.info("floorPlanImagePath {}", floorPlanImagePath);

		BufferedImage imgPath = getBgImage(floorplanJson, driveDataWrapper);
		if (imgPath != null) {
			ImageIO.write(imgPath, ReportConstants.JPEG, new File(floorPlanImagePath));
		}
		return floorPlanImagePath;
	}

	private BufferedImage getBgImage(String floorplanJson, DriveDataWrapper driveDataWrapper) {
		try {
			BufferedImage imBuff = null;
			boolean isbgImageAvailable = FloorPlanJsonParser.isImagePickedFromGallery(floorplanJson);
			if (isbgImageAvailable) {
				logger.info("driveDataWrapper.getFilePath() {}", driveDataWrapper.getFilePath());

				String localFilepath = nvLayer3hdfsDao.copyFileFromHdfsToLocalPath(driveDataWrapper.getFilePath(),
						ConfigUtils.getString(IBREPORT_FLOORPLAN_IMAGE_PATH), FLOORPLANNAME + DOT_ZIP);
				logger.info("Going to process zip file {}  ", localFilepath);

				try (ZipFile zipFile = new ZipFile(localFilepath)) {
					Enumeration<? extends ZipEntry> entries = zipFile.entries();
					while (entries.hasMoreElements()) {
						ZipEntry entry = entries.nextElement();
						if (entry.getName().contains(BACKGROUNDIMAGE)) {
							//						logger.info("inside the FloorPlanImage image  ");
							InputStream is = zipFile.getInputStream(entry);
							imBuff = ImageIO.read(is);
						}
					}
				}
			}
			return imBuff;
		} catch (JSONException e) {
			logger.error("JSONException inside method  getBgImagePath {} ", e.getMessage());
		} catch (Exception e) {
			logger.error("Exception inside method getBgImagePath {} ", e.getMessage());
		}
		return null;
	}

	private Map<String, Object> getImagesForReport(String imgFloorPlan, String localDirPath, List<String[]> arlist,
			List<KPIWrapper> kpiList, Map<String, Integer> kpiIndexMap) {
		logger.info("Inside getImagesForReport imgFloorPlan : {}, localDirPath : {}", imgFloorPlan, localDirPath);
		try {
			kpiList = reportService.modifyIndexOfCustomKpisForReport(kpiList, kpiIndexMap);
			HashMap<String, BufferedImage> bufferdImageMap = reportService.getLegendImagesForReport(kpiList, arlist,
					kpiIndexMap);
			HashMap<String, String> imagemap = mapImageService.saveDriveImages(bufferdImageMap, localDirPath, false);
			Map<String, String> heatMaps = InBuildingHeatMapGenerator.getInstance()
																	 .generateHeatMapsForReport(arlist, localDirPath,
																			 imgFloorPlan, kpiList, kpiIndexMap);
			setLegendImageForMimo(arlist,kpiIndexMap,localDirPath,heatMaps);
			imagemap.putAll(heatMaps);
//			logger.info("imagemap :{}", imagemap);
			return setImagesForReport(imagemap, kpiIndexMap);
		} catch (Exception e) {
			logger.error("Inside method getImagesForReport {} ", Utils.getStackTrace(e));
		}
		return null;
	}

	private List<KPIWrapper> getKpiWrapperList(Integer workorderId, String recipe, String opName, Map<String, Integer> kpiIndexMap) {
		List<LegendWrapper> legendList = legendRangeDao.findAllLegendRangesAppliedTo(ReportConstants.SSVT_REPORT);
		List<KPIWrapper> kpiList = ReportUtil.convertLegendsListToKpiWrapperList(legendList,
				kpiIndexMap);
		return reportService.setKpiStatesIntokpiListForReport(kpiList, workorderId, Arrays.asList(recipe),
				Arrays.asList(opName));
	}

	private File proceedToCreatePlot(INBuildingSubWrapper mainWrapper, Map<String, Object> imageMap) {

		logger.info("inside the method proceedToCreatePlot ");
		try {
			String reportAssets = ConfigUtils.getString(ReportConstants.INBUILDING_REPORT_PLOT);
			List<INBuildingSubWrapper> dataSourceList = new ArrayList<>();
			dataSourceList.add(mainWrapper);
			JRBeanCollectionDataSource rfbeanColDataSource = new JRBeanCollectionDataSource(dataSourceList);
			imageMap.put(SUBREPORT_DIR, reportAssets);
			imageMap.put(IMAGE_PARAM_HEADER_LOGO, reportAssets + ReportConstants.IMAGE_HEADER_LOGO);
			imageMap.put(LOGO_NV_KEY, reportAssets + ReportConstants.LOGO_NV_IMG);
			imageMap.put(ReportConstants.LOGO_CLIENT_KEY, reportAssets + ReportConstants.LOGO_CLIENT_IMG);
			imageMap.put(IMAGE_PARAM_FOOTER, reportAssets + IMAGE_FOOTER);
			String destinationFilePath = mainWrapper.getFileName();
			JasperRunManager.runReportToPdfFile(reportAssets + ReportConstants.MAIN_JASPER, destinationFilePath,
					imageMap, rfbeanColDataSource);
			logger.info("Report Created successfully  ");

			return ReportUtil.getIfFileExists(destinationFilePath);
		} catch (Exception e) {
			logger.error("@proceedToCreatePlot getting err={}", Utils.getStackTrace(e));
		}
		logger.info("@proceedToCreatePlot going to return null as there has been some problem in generating report");
		return null;

	}

	private Map<String, Object> setImagesForReport(HashMap<String, String> imagemap, Map<String, Integer> kpiIndexMap) {
		Map<String, Object> map = new HashMap<>();
		try {
			map.put(ReportConstants.IMAGE_RSRP, imagemap.get(ReportConstants.RSRP));
			map.put(ReportConstants.IMAGE_RSRP_LEGEND, imagemap.get(
					ReportConstants.LEGEND + ReportConstants.UNDERSCORE + kpiIndexMap.get(ReportConstants.RSRP)));

			map.put(ReportConstants.IMAGE_SINR, imagemap.get(ReportConstants.SINR));
			map.put(ReportConstants.IMAGE_SINR_LEGEND, imagemap.get(
					ReportConstants.LEGEND + ReportConstants.UNDERSCORE + kpiIndexMap.get(ReportConstants.SINR)));
			map.put(ReportConstants.IMAGE_DL, imagemap.get(ReportConstants.DL_THROUGHPUT));
			map.put(ReportConstants.IMAGE_DL_LEGEND,
					imagemap.get(ReportConstants.LEGEND + ReportConstants.UNDERSCORE + kpiIndexMap.get(ReportConstants.DL_THROUGHPUT)));

			map.put(ReportConstants.IMAGE_UL, imagemap.get(ReportConstants.UL_THROUGHPUT));
			map.put(ReportConstants.IMAGE_UL_LEGEND,
					imagemap.get(ReportConstants.LEGEND + ReportConstants.UNDERSCORE + kpiIndexMap.get(ReportConstants.UL_THROUGHPUT)));
			//PUSCH & PDSCH IMAGE
			map.put(ReportConstants.IMAGE_PDSCH, imagemap.get(ReportConstants.PDSCH_THROUGHPUT));
			map.put(ReportConstants.IMAGE_PDSCH_LEGEND, imagemap.get(
					ReportConstants.LEGEND + ReportConstants.UNDERSCORE + kpiIndexMap.get(ReportConstants.PDSCH_THROUGHPUT)));
			map.put(ReportConstants.IMAGE_PUSCH, imagemap.get(ReportConstants.PUSCH_THROUGHPUT));
			map.put(ReportConstants.IMAGE_PUSCH_LEGEND, imagemap.get(
					ReportConstants.LEGEND + ReportConstants.UNDERSCORE + kpiIndexMap.get(ReportConstants.PUSCH_THROUGHPUT)));

			map.put(ReportConstants.IMAGE_PCI, imagemap.get(ReportConstants.PCI_PLOT));
			map.put(ReportConstants.IMAGE_PCI_LEGEND, imagemap.get(ReportConstants.PCI_LEGEND));
			map.put(IMAGE_CQI, imagemap.get(DriveHeaderConstants.CQI));
			map.put(IMAGE_CQI_LEGEND, imagemap.get(LEGEND + UNDERSCORE + kpiIndexMap.get(ReportConstants.CQI)));
			map.put(ReportConstants.IMAGE_RI, imagemap.get(ReportConstants.MIMO));
			map.put(ReportConstants.IMAGE_RI_LEGEND,
					imagemap.get(ReportConstants.MIMO + Symbol.UNDERSCORE_STRING + ReportConstants.LEGEND));

			map.put(ReportConstants.IMAGE_MCS, imagemap.get(ReportConstants.MCS));
			map.put(ReportConstants.IMAGE_MCS_LEGEND,
					imagemap.get(ReportConstants.LEGEND + ReportConstants.UNDERSCORE + kpiIndexMap.get(ReportConstants.MCS)));
			map.put(ReportConstants.IMAGE_FC_LEGEND,
					imagemap.get(ReportConstants.DL_EARFCN + ReportConstants.UNDERSCORE + ReportConstants.KEY_LEGENDS));
			map.put(ReportConstants.IMAGE_FC, imagemap.get(ReportConstants.DL_EARFCN));
/*
			map.put(ReportConstants.HTTP_IMAGE_DL, imagemap.get(ReportConstants.HTTP_DL_THROUGHPUT));
			map.put(ReportConstants.FTP_IMAGE_DL, imagemap.get(ReportConstants.FTP_DL_THROUGHPUT));

			map.put(ReportConstants.IMAGE_FTP_DL_LEGEND, imagemap.get(
					ReportConstants.LEGEND + ReportConstants.UNDERSCORE + kpiIndexMap.get(ReportConstants.FTP_DL)));
			map.put(ReportConstants.IMAGE_HTTP_DL_LEGEND, imagemap.get(
					ReportConstants.LEGEND + ReportConstants.UNDERSCORE + kpiIndexMap.get(ReportConstants.HTTP_DL)));
			map.put(ReportConstants.HTTP_IMAGE_UL, imagemap.get(ReportConstants.HTTP_UL_THROUGHPUT));
			map.put(ReportConstants.FTP_IMAGE_UL, imagemap.get(ReportConstants.FTP_UL_THROUGHPUT));
			map.put(ReportConstants.IMAGE_HTTP_UL_LEGEND, imagemap.get(
					ReportConstants.LEGEND + ReportConstants.UNDERSCORE + kpiIndexMap.get(ReportConstants.HTTP_UL)));

			map.put(ReportConstants.IMAGE_FTP_UL_LEGEND, imagemap.get(
					ReportConstants.LEGEND + ReportConstants.UNDERSCORE + kpiIndexMap.get(ReportConstants.FTP_UL)));
*/
			map.put(ReportConstants.IMAGE_MOS, imagemap.get(DriveHeaderConstants.MOS));
			map.put(ReportConstants.IMAGE_MOS_LEGEND,
					imagemap.get(ReportConstants.LEGEND + ReportConstants.UNDERSCORE + kpiIndexMap.get(ReportConstants.MOS)));

			map.put(ReportConstants.IMAGE_CGI, imagemap.get(DriveHeaderConstants.CGI));
			map.put(ReportConstants.IMAGE_CGI_LEGEND,
					imagemap.get(DriveHeaderConstants.CGI + Symbol.UNDERSCORE_STRING + ReportConstants.KEY_LEGENDS));
			map.put(CALL_PLOT, imagemap.get(CALL_PLOT));
			
		} catch (Exception e) {
			logger.error("Error inside the method prepareImageMap{}", e.getMessage());
		}
		//	logger.debug("image map is {}", new Gson().toJson(map));
		return map;
	}

	@Override
	public Response createIBReport(String json) {
		logger.info("Inside method createIBReport for json 1234 {} ", json);
		try {
			Map jsonMap = new ObjectMapper().readValue(json, HashMap.class);
			logger.info("ReportInstance jsonObject {} ", jsonMap);
			Integer buildingId = (Integer) jsonMap.get(INBUILDINGID);
			if (buildingId != null) {
				logger.info("building id is not null");
				File file = createIBReportForBuildingId(buildingId);
				Response.ResponseBuilder builder = Response.status(200);
				return builder.entity(file).header(NVConstant.CONTENT_TYPE, "application/pdf").build();
			} else {
				logger.info("building id is null");
				Map<String, Object> recipeOperatorListMap = getRecipeOperatorList(json);
				logger.info("recipeOperatorListMap Data  {} ", recipeOperatorListMap);
				File file = createIBReportForRecipeId(recipeOperatorListMap.get(ReportConstants.RECIPE_LIST),
						recipeOperatorListMap.get(ReportConstants.OPERATOR_LIST),
						recipeOperatorListMap.get(ReportConstants.WORKORDER_ID), RECEPILEVEL, null);
				if (file != null) {
					Response.ResponseBuilder builder = Response.status(200);
					return builder.entity(file).header(NVConstant.CONTENT_TYPE, "application/pdf").build();
				} else {
					return Response.ok(NVConstant.REPORT_NOT_FOUND_JSON).build();
				}
			}
		} catch (Exception e) {
			logger.error(errorCreatingReport, Utils.getStackTrace(e));
			return Response.ok("{\"result\":\"" + e.getLocalizedMessage() + " \"}").build();
		}
	}

	private Map<String, Object> getRecipeOperatorList(String json) {
		logger.info("Inside method getRecipeAndOperatorList from json 1234 {} ", json);
		Map<String, Object> recipeAndOperatorListMap = new HashMap<>();
		try {
			Map jsonMap = new ObjectMapper().readValue(json, HashMap.class);
			logger.info("ReportInstance jsonObject {} ", jsonMap);
			Integer recipeId = (Integer) jsonMap.get(RECIPEID);
			String operator = (String) jsonMap.get(OPERATOR);
			Integer buildingId = (Integer) jsonMap.get(INBUILDINGID);
			Integer workorderId = (Integer) jsonMap.get(WORKORDER_ID);
			List<Integer> recipeList = null;
			List<String> operatorList = null;
			if (recipeId != null && !StringUtils.isEmpty(operator) && workorderId != null) {
				Map<String, List<String>> map = nvLayer3DashboardService.getDriveRecipeDetail(workorderId);
				if (map != null) {
					List<String> recipeIdList = map.get(QMDLConstant.RECIPE);
					List<String> opList = map.get(QMDLConstant.OPERATOR);
					logger.info(recipeAndOpList, recipeIdList, opList);
					int i = 0;
					for (String string : recipeIdList) {
						if (string.equalsIgnoreCase(recipeId.toString())) {
							recipeList = new ArrayList<>(Arrays.asList(recipeId));
							operatorList = new ArrayList<>(Arrays.asList(opList.get(i)));
						}
						i++;
					}
				}
				nvL3Parsing.fileProcessWorkorderWise(workorderId, recipeId, null);
			} else if (workorderId != null) {
				Map<String, List<String>> map = nvLayer3DashboardService.getDriveRecipeDetail(workorderId);
				List<String> recipeIdList = map.get(QMDLConstant.RECIPE);
				logger.info("recipeIdList {}", recipeIdList);
				recipeList = recipeIdList.stream().map(NumberUtils::toInteger).collect(Collectors.toList());
				operatorList = map.get(QMDLConstant.OPERATOR);
				nvL3Parsing.fileProcessWorkorderWise(workorderId, null, null);
			} else if (buildingId != null) {
				List<Integer> workorderIdList = genericWorkorderDao.getListOfWorkOrderIdByBuildingId(buildingId,
						ReportConstants.TECHNOLOGY_LTE);
				Map<String, List<String>> map = nvLayer3DashboardService.getRecipeDetailByWorkorderList(
						workorderIdList);
				List<String> recipeIdList = map.get(QMDLConstant.RECIPE);
				logger.info("recipeIdList {}", recipeIdList);
				recipeList = recipeIdList.stream().map(NumberUtils::toInteger).collect(Collectors.toList());
				operatorList = map.get(QMDLConstant.OPERATOR);
			}
			recipeAndOperatorListMap.put(ReportConstants.RECIPE_LIST, recipeList);
			recipeAndOperatorListMap.put(ReportConstants.OPERATOR_LIST, operatorList);
			recipeAndOperatorListMap.put(ReportConstants.WORKORDER_ID, workorderId);
		} catch (Exception e) {
			logger.error("Exceptio inside method getRecipeAndOperatorList {} ", Utils.getStackTrace(e));
		}
		logger.info("recipeOperatorListMap Size {} ,data {}  ", recipeAndOperatorListMap.size(),
				recipeAndOperatorListMap);
		return recipeAndOperatorListMap;
	}

	public File createIBReportForBuildingId(Integer buildingId) {
		try {
			List<Integer> workOrderList = genericWorkorderDao.getListOfWorkOrderIdByBuildingId(buildingId,
					ReportConstants.TECHNOLOGY_LTE);
			InBuildingReportWrapper mainWrapper = new InBuildingReportWrapper();
			List<WalkTestSummaryWrapper> summaryListofWprkOrders = new ArrayList<>();
			List<File> fileList = new ArrayList<>();
			logger.info("workOrderList {}", workOrderList);
			try {
				for (Integer workOrderId : workOrderList) {
					if (reportService.getFileProcessedStatusForWorkorders(Arrays.asList(workOrderId))) {
						Map<String, List<String>> map = nvLayer3DashboardService.getDriveRecipeDetail(workOrderId);
						if (map != null) {
							List<Integer> idList = map.get(QMDLConstant.RECIPE)
													  .stream()
													  .map(NumberUtils::toInteger)
													  .collect(Collectors.toList());
							logger.info("idList {}", idList);
							List<WalkTestSummaryWrapper> summaryList = nvIBUnitResultService.getIBUnitResultForRecipe(
									idList);
							summaryListofWprkOrders.addAll(summaryList);
							List<File> files = getFileListByRecipeWise(workOrderId, summaryList,
									map.get(QMDLConstant.OPERATOR));
							fileList.addAll(files);
						}
					}
				}
			} catch (Exception e) {
				logger.error("Exception inside the method createIBReportForBuildingId to process workorder wise flow{}",
						Utils.getStackTrace(e));
			}
			Building building = buildingDataDao.findByPk(buildingId);
			setPdschPuschForWrapper(summaryListofWprkOrders, mainWrapper);
			setSummaryListToWrapper(mainWrapper, summaryListofWprkOrders, building);
			mainWrapper.setFileName(ReportUtil.getFileName(INBUILDING, buildingId));
			File file = getSummaryReport(mainWrapper);
			fileList.add(0, file);
			MergePDF.mergeFiles(mainWrapper.getFileName(), fileList);
			return ReportUtil.getIfFileExists(mainWrapper.getFileName());
		} catch (Exception e) {
			logger.info("Exception inside the method createIBReportForBuildingId{}", Utils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public Response createIBReport(Integer recipeId, String operator, Integer inbuildingid, Integer workorderId,
			String technology) {
		logger.info("Inside method createIBReport for json {}, {},{},{} ", recipeId, operator, inbuildingid,
				workorderId);
		Response response = null;
		
		try {
			if (inbuildingid != null) {
				File file = null;
				if (technology != null && ReportConstants.TECHNOLOGY_WIFI.equalsIgnoreCase(technology)) {
					String fileName = IBWifiConstants.IB_WIFI_REPORT_NAME_PREFIX + inbuildingid.toString()
							+ Symbol.UNDERSCORE_STRING + System.currentTimeMillis() + ReportConstants.PDF_EXTENSION;
					if(fileName.contains(Symbol.PLUS_STRING)) {
						fileName=fileName.replace(Symbol.PLUS_STRING, Symbol.HYPHEN_STRING);
					}
					
					file = nvIBWifiReportService.generateIBWIFIBuildingLevelReport(inbuildingid, fileName);
				} else {
					file = createIBReportForBuildingId(inbuildingid);
				}
				if (file != null) {
					logger.info("Overall fileName: {}", file.getName());

					Map<String, String> responseMap = new HashMap<>();
					responseMap.put(NVLayer3Constants.FILE_PATH, file.getPath());
					response = Response.ok(responseMap).build();

				} else {
					return Response.ok(NVConstant.REPORT_NOT_FOUND_JSON).build();
				}
			} else {
				Map<String, Object> recipeOperatorListMap = getRecipeOperatorList(recipeId, operator, workorderId);
				logger.info("recipeOperatorListMap Data  {} ", recipeOperatorListMap);
				File file = null;
				if (technology != null && ReportConstants.TECHNOLOGY_WIFI.equalsIgnoreCase(technology)) {
					String fileName =
							IBWifiConstants.IB_WIFI_REPORT_NAME_PREFIX + recipeId.toString() + Symbol.UNDERSCORE_STRING
									+ workorderId.toString() + Symbol.UNDERSCORE_STRING + System.currentTimeMillis()
									+ ReportConstants.PDF_EXTENSION;
					if(fileName.contains(Symbol.PLUS_STRING)) {
						fileName=fileName.replace(Symbol.PLUS_STRING, Symbol.HYPHEN_STRING);
					}
					
					file = nvIBWifiReportService.generateIBWIFITaskLevelReport(recipeId, workorderId, fileName);
				} else {
					file = createIBReportForRecipeId(recipeOperatorListMap.get(ReportConstants.RECIPE_LIST),
							recipeOperatorListMap.get(ReportConstants.OPERATOR_LIST),
							recipeOperatorListMap.get(ReportConstants.WORKORDER_ID), RECEPILEVEL, null);
				}
				if (file != null) {
					logger.info("Overall fileName: {}", file.getName());
					
					Map<String, String> responseMap = new HashMap<>();
					responseMap.put(NVLayer3Constants.FILE_PATH, file.getPath());
					response = Response.ok(responseMap).build();

				} else {
					return Response.ok(NVConstant.REPORT_NOT_FOUND_JSON).build();
				}
			}
		} catch (Exception e) {
			logger.error(errorCreatingReport, Utils.getStackTrace(e));
			return Response.ok("{\"result\":\"" + e.getLocalizedMessage() + " \"}").build();
		}
		return response;
	}

	@Override
	@Transactional
	public Response createIBReportByFloor(Integer inbuildingid, Integer floorId, String technology) {
		logger.info("Inside method createIBReportByFloor for json inbuildingid {}, floorId  {} ", inbuildingid,
				floorId);
		Response response = null;
		try {
			if (inbuildingid != null && floorId != null) {
				File file = createIBReportForFloorId(inbuildingid, floorId, technology);

				if (file != null) {
					logger.info("Overall fileName: {}", file.getName());

					Map<String, String> responseMap = new HashMap<>();
					responseMap.put(NVLayer3Constants.FILE_PATH, file.getPath());
					response = Response.ok(responseMap).build();

				} else {
					return Response.ok(NVConstant.REPORT_NOT_FOUND_JSON).build();
				}
			}
		} catch (Exception e) {
			logger.error(errorCreatingReport, Utils.getStackTrace(e));
			return Response.ok("{\"result\":\"" + e.getLocalizedMessage() + " \"}").build();
		}
		return response;
	}

	public File createIBReportForFloorId(Integer buildingId, Integer floorId, String technology) {
		try {
			List<TemplateType> templateList = getInbuildingTemplateList();
			List<NVIBUnitResult> nvIbUnitResult = nvIBUnitResultService.getListOfWorkOrderIdByFloorIdAndTemplateType(
					buildingId, floorId, templateList);
			if (nvIbUnitResult != null && !nvIbUnitResult.isEmpty()) {
				if (ReportConstants.TECHNOLOGY_WIFI.equalsIgnoreCase(technology)) {
					String fileName =
							IBWifiConstants.IB_WIFI_REPORT_NAME_PREFIX + floorId.toString() + Symbol.UNDERSCORE_STRING
									+ buildingId.toString() + Symbol.UNDERSCORE_STRING + System.currentTimeMillis()
									+ ReportConstants.PDF_EXTENSION;
					
					if(fileName.contains(Symbol.PLUS_STRING)) {
						fileName=fileName.replace(Symbol.PLUS_STRING, Symbol.HYPHEN_STRING);
					}
					return nvIBWifiReportService.generateIBWIFIFloorLevelReport(nvIbUnitResult.stream()
																							  .filter(nvIbResult -> ReportConstants.TECHNOLOGY_WIFI
																									  .equalsIgnoreCase(
																											  nvIbResult
																													  .getTechnology()
																													  .toUpperCase()))
																							  .collect(
																									  Collectors.toList()),
							fileName);
				} else {
					nvIbUnitResult = nvIbUnitResult.stream()
												   .filter(nvIbResult -> ReportConstants.TECHNOLOGY_LTE.equalsIgnoreCase(
														   nvIbResult.getTechnology().toUpperCase()))
												   .collect(Collectors.toList());
				}
			}
			Map<Integer, Map<String, List<String>>> workorderWiseMap = new HashMap<>();
			Map<Integer, List<WalkTestSummaryWrapper>> workorderWiseNVIbResult = new HashMap<>();
			explotResultOfNVIBunit(nvIbUnitResult, workorderWiseMap, workorderWiseNVIbResult);

			InBuildingReportWrapper mainWrapper = new InBuildingReportWrapper();
			List<WalkTestSummaryWrapper> summaryListofWprkOrders = new ArrayList<>();
			List<File> fileList = new ArrayList<>();
			logger.info("workOrderList {}", workorderWiseMap.keySet());
			processFileRecipeWise(workorderWiseMap, workorderWiseNVIbResult, summaryListofWprkOrders, fileList);

			Building building = buildingDataDao.findByPk(buildingId);
			mainWrapper.setIsFloor(ReportConstants.TRUE);
			setPdschPuschForWrapper(summaryListofWprkOrders, mainWrapper);
			setSummaryListToWrapper(mainWrapper, summaryListofWprkOrders, building);
			mainWrapper.setFileName(ReportUtil.getFileName(FLOOR, floorId));
			File file = getSummaryReport(mainWrapper);
			fileList.add(ReportConstants.INDEX_ZER0, file);
			MergePDF.mergeFiles(mainWrapper.getFileName(), fileList);
			return ReportUtil.getIfFileExists(mainWrapper.getFileName());
		} catch (Exception e) {
			logger.info("Exception inside the method createIBReportForBuildingId{}", Utils.getStackTrace(e));
		}
		return null;
	}

	private void processFileRecipeWise(Map<Integer, Map<String, List<String>>> workorderWiseMap,
			Map<Integer, List<WalkTestSummaryWrapper>> workorderWiseNVIbResult,
			List<WalkTestSummaryWrapper> summaryListofWprkOrders, List<File> fileList) {
		try {
			for (Entry<Integer, Map<String, List<String>>> workOrderIdEntry : workorderWiseMap.entrySet()) {
				Map<String, List<String>> map = workOrderIdEntry.getValue();
				List<WalkTestSummaryWrapper> summaryList = workorderWiseNVIbResult.get(workOrderIdEntry.getKey());
				summaryListofWprkOrders.addAll(summaryList);
				List<File> files = getFileListByRecipeWise(workOrderIdEntry.getKey(),
						workorderWiseNVIbResult.get(workOrderIdEntry.getKey()),
						new ArrayList<String>(map.get(QMDLConstant.OPERATOR)));
				fileList.addAll(files);
			}
		} catch (Exception e) {
			logger.error("Exception inside the method createIBReportForBuildingId to process workorder wise flow{}",
					Utils.getStackTrace(e));
		}
	}

	private void explotResultOfNVIBunit(List<NVIBUnitResult> nvIbUnitResult,
			Map<Integer, Map<String, List<String>>> workorderWiseDetailMap,
			Map<Integer, List<WalkTestSummaryWrapper>> workorderWiseNVIbResult) {
		for (NVIBUnitResult nvIbUnit : nvIbUnitResult) {
			Integer workOrderId = nvIbUnit.getWoRecipeMapping().getGenericWorkorder().getId();
			if (workorderWiseDetailMap.containsKey(workOrderId)) {
				setValuesIntoWorkorderWiseDetailMap(workorderWiseDetailMap.get(workOrderId), nvIbUnit);
				setValuesIntoWalkTestWrapper(workorderWiseNVIbResult.get(workOrderId), nvIbUnit);

			} else {
				intializeMapForWorkorder(workorderWiseDetailMap, nvIbUnit, workOrderId);
				List<WalkTestSummaryWrapper> walkTestList = new ArrayList<>();
				setValuesIntoWalkTestWrapper(walkTestList, nvIbUnit);
				workorderWiseNVIbResult.put(workOrderId, walkTestList);
			}
		}

	}

	private void setValuesIntoWalkTestWrapper(List<WalkTestSummaryWrapper> list, NVIBUnitResult nvIbUnit) {

		WalkTestSummaryWrapper walkTest = new WalkTestSummaryWrapper(nvIbUnit.getTotalRsrp(), nvIbUnit.getCountRsrp(),
				nvIbUnit.getCountRsrpGreaterThan100Dbm(), nvIbUnit.getTotalSinr(), nvIbUnit.getCountSinr(),
				nvIbUnit.getCountSinrGreaterThan12Db(), nvIbUnit.getTotalDl(), nvIbUnit.getCountDl(),
				nvIbUnit.getTotalUl(), nvIbUnit.getCountUl(), nvIbUnit.getPciStrongest(), nvIbUnit.getTotalCqi(),
				nvIbUnit.getCountCqi(), nvIbUnit.getTotalMimo(), nvIbUnit.getCountMimo(), nvIbUnit.getRrcInitiate(),
				nvIbUnit.getRrcSuccess(), nvIbUnit.getErabDrop(), nvIbUnit.getErabDrop(), nvIbUnit.getHandOverSuccess(),
				nvIbUnit.getHandOverInitiate(), nvIbUnit.getCountTDD(), nvIbUnit.getCountFDD(),
				nvIbUnit.getUnit() != null ?
						nvIbUnit.getUnit().getFloor().getWing().getBuilding().getBuildingName() :
						null, nvIbUnit.getUnit() != null ? nvIbUnit.getUnit().getFloor().getWing().getWingName() : null,
				nvIbUnit.getUnit() != null ? nvIbUnit.getUnit().getFloor().getFloorName() : null,
				nvIbUnit.getUnit() != null ? nvIbUnit.getUnit().getUnitName() : null,
				nvIbUnit.getWoRecipeMapping().getRecipe().getId(), nvIbUnit.getWoRecipeMapping().getRecipe().getName(),
				nvIbUnit.getWoRecipeMapping().getId(), nvIbUnit.getCreationTime(), nvIbUnit.getCountMos(),
				nvIbUnit.getTotalMos(), nvIbUnit.getCallInitiateCount(), nvIbUnit.getCallSetupSuccessCount(),
				nvIbUnit.getCallDropCount(), nvIbUnit.getTotalPuschUl(), nvIbUnit.getCountPuschUl(),
				nvIbUnit.getTotalPdschDl(), nvIbUnit.getCountPdschDl());
		list.add(walkTest);

	}

	private void intializeMapForWorkorder(Map<Integer, Map<String, List<String>>> workorderWiseDetailMap,
			NVIBUnitResult nvIbUnit, Integer workOrderId) {
		List<String> recipeList = new ArrayList<>();
		List<String> operatorList = new ArrayList<>();
		Map<String, List<String>> map = new HashMap<>();
		setValuesIntoSet(nvIbUnit, recipeList, operatorList);
		map.put(QMDLConstant.RECIPE, recipeList);
		map.put(QMDLConstant.OPERATOR, operatorList);
		workorderWiseDetailMap.put(workOrderId, map);
	}

	private void setValuesIntoWorkorderWiseDetailMap(Map<String, List<String>> map, NVIBUnitResult nvIbUnit) {
		List<String> recipeList = map.get(QMDLConstant.RECIPE);
		List<String> operatorList = map.get(QMDLConstant.OPERATOR);
		setValuesIntoSet(nvIbUnit, recipeList, operatorList);

	}

	private void setValuesIntoSet(NVIBUnitResult nvIbUnit, List<String> recipeList, List<String> operatorList) {
		if (nvIbUnit.getOperator() != null) {
			operatorList.add(nvIbUnit.getOperator().replace(Symbol.SPACE_STRING, Symbol.EMPTY_STRING).toUpperCase());
		}
		if (nvIbUnit.getWoRecipeMapping() != null) {
			recipeList.add(nvIbUnit.getWoRecipeMapping().getId().toString());
		}
	}

	private List<TemplateType> getInbuildingTemplateList() {
		List<TemplateType> templateList = new ArrayList<>();
		templateList.add(TemplateType.NV_INBUILDING);
		templateList.add(TemplateType.NV_ADHOC_IB);
		templateList.add(TemplateType.NV_IB_BENCHMARK);
		templateList.add(TemplateType.INBUILDING_WORKFLOW);
		return templateList;
	}

	private Map<String, Object> getRecipeOperatorList(Integer recipeId, String operator, Integer workorderId) {
		logger.info("Inside method getRecipeAndOperatorList from json");
		Map<String, Object> recipeAndOperatorListMap = new HashMap<>();
		try {
			List<Integer> recipeList = null;
			List<String> operatorList = null;
			if (recipeId != null && !StringUtils.isEmpty(operator) && workorderId != null) {
				Map<String, List<String>> map = nvLayer3DashboardService.getDriveRecipeDetail(workorderId);
				if (map != null) {
					List<String> recipeIdList = map.get(QMDLConstant.RECIPE);
					List<String> opList = map.get(QMDLConstant.OPERATOR);
					logger.info(recipeAndOpList, recipeIdList, opList);
					int index = ReportConstants.INDEX_ZER0;
					for (String string : recipeIdList) {
						if (string.equalsIgnoreCase(recipeId.toString())) {
							recipeList = new ArrayList<>(Arrays.asList(recipeId));
							operatorList = new ArrayList<>(Arrays.asList(opList.get(index)));
						}
						index++;
					}
				}

			} else if (workorderId != null) {
				Map<String, List<String>> map = nvLayer3DashboardService.getDriveRecipeDetail(workorderId);
				List<String> recipeIdList = map.get(QMDLConstant.RECIPE);
				logger.info("recipeIdList {}", recipeIdList);
				recipeList = recipeIdList.stream().map(NumberUtils::toInteger).collect(Collectors.toList());
				operatorList = map.get(QMDLConstant.OPERATOR);
			}
			recipeAndOperatorListMap.put(ReportConstants.RECIPE_LIST, recipeList);
			recipeAndOperatorListMap.put(ReportConstants.OPERATOR_LIST, operatorList);
			recipeAndOperatorListMap.put(ReportConstants.WORKORDER_ID, workorderId);
		} catch (Exception e) {
			logger.error("Exceptio inside method getRecipeAndOperatorList {} ", Utils.getStackTrace(e));
		}

		logger.info("recipeOperatorListMap Size {} ,data {}  ", recipeAndOperatorListMap.size(),
				recipeAndOperatorListMap);
		return recipeAndOperatorListMap;

	}

	private BufferedImage getNoAccessImg(DriveDataWrapper driveDataWrapper) {
		try {
			BufferedImage imBuff = null;

			String localFilepath = nvLayer3hdfsDao.copyFileFromHdfsToLocalPath(driveDataWrapper.getFilePath(),
					ConfigUtils.getString(IBREPORT_FLOORPLAN_IMAGE_PATH), NOACCESS + DOT_ZIP);
			logger.info("Going to process zip file {}  ", localFilepath);
			try {
				try (ZipFile zipFile = new ZipFile(localFilepath)) {
					Enumeration<? extends ZipEntry> entries = zipFile.entries();
					while (entries.hasMoreElements()) {
						ZipEntry entry = entries.nextElement();
						if (driveDataWrapper.getImageName().contains(QMDLConstant.NO_ACCESS_IMAGE_PREFIX)
								&& entry.getName().contains(driveDataWrapper.getImageName())) {
							logger.info("inside the no access img " + driveDataWrapper.getImageName());
							InputStream is = zipFile.getInputStream(entry);
							imBuff = ImageIO.read(is);
						}
					}
				}
			} catch (Exception e) {
				logger.error("Error in processing file {}   ", Utils.getStackTrace(e));
			}
			return imBuff;
		} catch (Exception e) {
			logger.error("Exception inside method getBgImagePath {} ", e.getMessage());
		}
		return null;
	}
	
	private List<VoiceStatsWrapper> setCallData(WalkTestSummaryWrapper walkTestsummary, INBuildingSubWrapper subwrapper) {
		List<VoiceStatsWrapper> voicestatslist = new ArrayList<VoiceStatsWrapper>();
		if(walkTestsummary!=null) {
		VoiceStatsWrapper wrapper = new VoiceStatsWrapper();
        ///////////CallInitiateCount//////////////
		if(walkTestsummary.getCallInitCount()!=null && Integer.parseInt(walkTestsummary.getCallInitCount())>0) {
		wrapper.setCallAttemptCount(walkTestsummary.getCallInitCount());
		subwrapper.setShowCallPage(true);
		}
		///////////CallDropCount//////////////
		if(walkTestsummary.getCallDropCount()!=null) {
		wrapper.setCallDroppedCount(walkTestsummary.getCallDropCount());
		}
		///////////CallSuccessCount//////////////
		if(walkTestsummary.getCallSuccessCount()!=null) {
		wrapper.setCallConnectedCount(walkTestsummary.getCallSuccessCount());
		}
        ///////////CallFailureCount//////////////
		if(walkTestsummary.getCallFailCount()!=null) {
	    wrapper.setCallDropRate(walkTestsummary.getCallFailCount());
		}
	    voicestatslist.add(wrapper);
	    }
		return voicestatslist;
	}
	
	public Map<String, Object> getJsonDataMap(String json){
		Map<String, Object> jsonMap=null;
		try {
			jsonMap = new ObjectMapper().readValue(json, HashMap.class);
			Integer analyticsrepoId = (Integer) jsonMap.get(ForesightConstants.ANALYTICAL_REPORT_KEY);
			AnalyticsRepository analyticrepositoryObj = analyticsRepositoryDao.findByPk(analyticsrepoId);
			logger.info("analyticsrepositoryId {} , {}  ",analyticsrepoId,analyticrepositoryObj.getReportConfig());
			String reportConfig =  analyticrepositoryObj.getReportConfig()!=null?analyticrepositoryObj.getReportConfig().replaceAll("\'", "\""):null;
			Map<String, Object> configMap = new ObjectMapper().readValue(reportConfig, HashMap.class);
			logger.info("AnalyticsRepository jsonObject {} ",configMap);
			Integer workOrderId = (Integer) configMap.get(WORKORDER_ID);
			Integer projectId = configMap.containsKey(PROJECT_ID)?Integer.parseInt((String)configMap.get(PROJECT_ID)):null;
			List<Integer> workOrderIds = configMap.get(WORKORDER_IDS)!=null?(ArrayList<Integer>)configMap.get(WORKORDER_IDS):null;
			Integer buildingId = (Integer) configMap.get(NVWorkorderConstant.BUILDING_ID);
			String buildingName = configMap.containsKey(KEY_BUILDING_NAME)?(String)configMap.get(KEY_BUILDING_NAME):null;
			jsonMap.put(BUILDING_ID_KEY, buildingId);
			jsonMap.put(WORKORDER_ID, workOrderId);
			jsonMap.put(ForesightConstants.ANALYTICAL_REPORT_KEY, analyticsrepoId);
			jsonMap.put(WORKORDER_IDS, workOrderIds);
			jsonMap.put(PROJECT_ID, projectId);
			jsonMap.put(KEY_BUILDING_NAME, buildingName);
		} catch (Exception e) {
			logger.error("Exception inside method getJsonDataMap {} ",Utils.getStackTrace(e));
		}
		return jsonMap;
	}
	
	private List<Integer> getWorkordersFromJson(Map<String, Object> jsonMap) {
		logger.info("Inside method getWorkordersFromJson {}", jsonMap);
		if (jsonMap != null && !jsonMap.isEmpty() && jsonMap.containsKey(ReportConstants.WORKORDER_IDS)) {
			return (List<Integer>) jsonMap.get(ReportConstants.WORKORDER_IDS);
		}
		return null;
	}
	
	private void setLegendImageForMimo(List<String[]> data, Map<String, Integer> kpiIndexMap, String folderPath,
			Map<String, String> heatMaps) {
		if (Utils.isValidList(data)) {
			try {
				BufferedImage riImage = LegendUtil.getLegendImageForMimo(data, kpiIndexMap);
				if(riImage!=null) {
				ImageIO.write(riImage, ReportConstants.JPG, new File(
						folderPath + ReportConstants.FORWARD_SLASH + "mimo_legends" + ReportConstants.DOT_PNG));
				heatMaps.put(ReportConstants.MIMO + Symbol.UNDERSCORE_STRING + ReportConstants.LEGEND,
						folderPath + ReportConstants.FORWARD_SLASH + "mimo_legends" + ReportConstants.DOT_PNG);
				}
			} catch (Exception e) {
				logger.info("Exception inside setLegendImageForMimo {} ", Utils.getStackTrace(e));
			}
		}
	}
	

}

package com.inn.foresight.module.nv.report.customreport.reports.master.service.impl;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.hadoop.hbase.client.Get;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.inn.commons.Symbol;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.hadoop.hbase.HBaseResult;
import com.inn.commons.lang.StringUtils;
import com.inn.commons.maps.Corner;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.report.dao.IAnalyticsRepositoryDao;
import com.inn.foresight.core.report.model.AnalyticsRepository;
import com.inn.foresight.module.nv.core.workorder.dao.impl.IGenericWorkorderDao;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;
import com.inn.foresight.module.nv.layer3.constants.Layer3PPEConstant;
import com.inn.foresight.module.nv.layer3.constants.NVLayer3Constants;
import com.inn.foresight.module.nv.layer3.dao.INVLayer3HbaseDao;
import com.inn.foresight.module.nv.layer3.service.INVLayer3DashboardService;
import com.inn.foresight.module.nv.layer3.utils.NVLayer3Utils;
import com.inn.foresight.module.nv.livedrive.constants.KPI;
import com.inn.foresight.module.nv.report.RangeSlab;
import com.inn.foresight.module.nv.report.constant.ReportIndexWrapper;
import com.inn.foresight.module.nv.report.constant.ReportSummaryIndexWrapper;
import com.inn.foresight.module.nv.report.customreport.reports.constants.NVReportConstants;
import com.inn.foresight.module.nv.report.customreport.reports.master.service.INVMasterReportService;
import com.inn.foresight.module.nv.report.customreport.reports.master.wrapper.GraphPlotDataWrapper;
import com.inn.foresight.module.nv.report.customreport.reports.master.wrapper.MasterGraphDataWrapper;
import com.inn.foresight.module.nv.report.customreport.reports.master.wrapper.MasterGraphPlotContainerWrapper;
import com.inn.foresight.module.nv.report.customreport.reports.master.wrapper.MasterPlotDataWrapper;
import com.inn.foresight.module.nv.report.customreport.reports.master.wrapper.MasterReportMainWrapper;
import com.inn.foresight.module.nv.report.customreport.reports.master.wrapper.MasterSummaryDataWrapper;
import com.inn.foresight.module.nv.report.customreport.reports.master.wrapper.MasterSummaryWrapper;
import com.inn.foresight.module.nv.report.customreport.reports.master.wrapper.RecipeDataWrapper;
import com.inn.foresight.module.nv.report.customreport.reports.master.wrapper.VolteCallData;
import com.inn.foresight.module.nv.report.customreport.reports.master.wrapper.VolteCallDataWrapper;
import com.inn.foresight.module.nv.report.customreport.ssvt.constants.SSVTConstants;
import com.inn.foresight.module.nv.report.customreport.ssvt.util.SSVTReportUtils;
import com.inn.foresight.module.nv.report.customreport.ssvt.wrapper.HOItemWrapper;
import com.inn.foresight.module.nv.report.customreport.ssvt.wrapper.HandoverDataWrapper;
import com.inn.foresight.module.nv.report.service.IMapImagesService;
import com.inn.foresight.module.nv.report.service.IMasterReportService;
import com.inn.foresight.module.nv.report.service.IReportService;
import com.inn.foresight.module.nv.report.utils.DriveHeaderConstants;
import com.inn.foresight.module.nv.report.utils.JsonMapParser;
import com.inn.foresight.module.nv.report.utils.ReportConstants;
import com.inn.foresight.module.nv.report.utils.ReportUtil;
import com.inn.foresight.module.nv.report.workorder.wrapper.DriveDataWrapper;
import com.inn.foresight.module.nv.report.workorder.wrapper.DriveImageWrapper;
import com.inn.foresight.module.nv.report.workorder.wrapper.KPIWrapper;
import com.inn.foresight.module.nv.report.wrapper.RecipeMappingWrapper;
import com.inn.foresight.module.nv.report.wrapper.SiteInformationWrapper;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.YoutubeTestWrapper;
import com.inn.foresight.module.nv.service.ISiteDetailService;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;
import com.inn.foresight.module.nv.workorder.dao.IWORecipeMappingDao;
import com.inn.foresight.module.nv.workorder.model.WORecipeMapping;
import com.inn.product.legends.dao.ILegendRangeDao;
import com.inn.product.legends.utils.LegendWrapper;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service("NVMasterReportServiceImpl")
public class NVMasterReportServiceImpl implements INVMasterReportService {

	private Logger logger = LogManager.getLogger(NVMasterReportServiceImpl.class);

	@Autowired
	private IMasterReportService masterReportService;
	@Autowired
	private IWORecipeMappingDao recipeMappingDao;
	@Autowired
	private INVLayer3DashboardService nvLayer3DashboardService;
	@Autowired
	private IReportService reportService;
	@Autowired
	private ILegendRangeDao legendRangeDao;
	@Autowired
	private IGenericWorkorderDao iGenericWorkorderDao;
	@Autowired
	private IMapImagesService mapImageService;
	
	@Autowired
	private INVLayer3HbaseDao nvLayer3HbaseDao;
	@Autowired
	private IAnalyticsRepositoryDao analyticsrepositoryDao;
	@Autowired
	private ISiteDetailService siteDetailService;

	
	private List<String> junkFiles = new ArrayList<>();

	@Override
	public Response execute(String json) {
		try {
			Map<String, Object> jsonMap = new JsonMapParser().convertJsonToMap(json);
			Integer workorderid = (Integer) jsonMap.get(NVWorkorderConstant.WORKORDER_ID);
			Integer analyticsId = (Integer) jsonMap.get(ForesightConstants.ANALYTICAL_REPORT_KEY);
			if (workorderid != null) {
				GenericWorkorder genericWorkorder = iGenericWorkorderDao.findByPkWithGeographyL3(workorderid);
				String isFUTTest = null;
				if (SSVTReportUtils.validateMap(genericWorkorder.getGwoMeta(), NVReportConstants.KEY_IS_FUT_TEST)) {
					isFUTTest = genericWorkorder.getGwoMeta().get(NVReportConstants.KEY_IS_FUT_TEST);
				}
				if (!StringUtils.isBlank(isFUTTest) && NVReportConstants.TRUE.equalsIgnoreCase(isFUTTest)) {
					if (reportService.getFileProcessedStatusForWorkorders(Arrays.asList(workorderid))) {
						AnalyticsRepository analyticsRepository = analyticsrepositoryDao.findByPk(
								analyticsId);
						Map<String, RecipeMappingWrapper> categoryRecipeMap = nvLayer3DashboardService.getDriveRecipeDetailForMasterReport(Arrays.asList(
								workorderid));
						Map<String, List<WORecipeMapping>> driveWiseRecipeMap = SSVTReportUtils.getDriveTypeWiseRecipeMapping(
								recipeMappingDao.getWORecipeByGWOId(workorderid));
						Map<String, String> mobilityTestTypeRecipeMap = getMobilityTestTypeMap(driveWiseRecipeMap);
						
						MasterReportMainWrapper masterMainWrapper = new MasterReportMainWrapper();

						/** fetch drive data */
						Set<String> dynamicKpis = reportService.getDynamicKpiName(Arrays.asList(workorderid), null, Layer3PPEConstant.ADVANCE);
						
						List<String> fetchKPIList = ReportIndexWrapper.getLiveDriveKPIs()
																	  .stream()
																	  .filter(k -> dynamicKpis.contains(k))
																	  .collect(Collectors.toList());
						Map<String, Integer> kpiIndexMap = ReportIndexWrapper.getLiveDriveKPIIndexMap(fetchKPIList);
					
						logger.info("fetchKPIList === {}",fetchKPIList);
					
						/** fetch summary data */
//						Set<String> dynamicKpisForSummary = reportService.getDynamicKpiName(workorderid, null, Layer3PPEConstant.SUMMARY);
						
						List<String> fetchSummaryKPIList = ReportSummaryIndexWrapper.getLiveDriveKPIs();
						logger.info("fetchSummaryKPIList === {}",fetchSummaryKPIList);
						
						Map<String, Integer> summaryIndexMap = ReportSummaryIndexWrapper.getLiveDriveKPIIndexMap(
								fetchSummaryKPIList);
						
						setSummaryDataToMainWrapper(workorderid, categoryRecipeMap, masterMainWrapper,
								fetchSummaryKPIList, summaryIndexMap);
						
						logger.info("summaryIndexMap {}",summaryIndexMap);
						MasterGraphPlotContainerWrapper masterContainerWrapper = new MasterGraphPlotContainerWrapper();

						Map<String, List<String[]>> testTypeWiseDriveData = getTestTypeWiseDataFromHbase(workorderid,
								categoryRecipeMap, mobilityTestTypeRecipeMap, fetchKPIList);
						testTypeWiseDriveData=SSVTReportUtils.tagRecordsWithTestType(testTypeWiseDriveData,kpiIndexMap);
						setGraphDataToMainWrapper(workorderid, categoryRecipeMap, masterContainerWrapper,
								masterMainWrapper, testTypeWiseDriveData, kpiIndexMap, fetchKPIList);
						setMapPlotDataToMainWrapper(categoryRecipeMap, workorderid, masterContainerWrapper,
								testTypeWiseDriveData, mobilityTestTypeRecipeMap, kpiIndexMap);
						masterMainWrapper.setGraphPlotDataList(Arrays.asList(masterContainerWrapper));
						setCallDataToMainWrapper(testTypeWiseDriveData, masterMainWrapper, kpiIndexMap);
						setHandoverDataToMainWrapper(masterMainWrapper, workorderid,
								categoryRecipeMap.get(ReportConstants.RECIPE_CATEGORY_DRIVE).getRecpiList(),
								categoryRecipeMap.get(ReportConstants.RECIPE_CATEGORY_DRIVE).getOperatorList(),
								testTypeWiseDriveData, kpiIndexMap);
						String filePath = proceedToCreateReport(masterMainWrapper, genericWorkorder, jsonMap,
								getParameterMapForReport(genericWorkorder));
						if (filePath != null) {
							Response response = saveFileToHdfsAndUpdateStatus(analyticsRepository, filePath);
							SSVTReportUtils.deleteAllFilesWithDirectory(junkFiles);
							return response;
						} else {
							return Response.ok(ForesightConstants.FAILURE_JSON).build();
						}
					}
				} else {
					return masterReportService.execute(json);
				}
			}
		} catch (Exception e) {
			logger.error("Error occured while creating FUT Report... Error: {}", Utils.getStackTrace(e));
		}
		return Response.ok(ForesightConstants.FAILURE_JSON).build();
	}

	private Map<String, List<String[]>> getTestTypeWiseDataFromHbase(Integer workorderid,
			Map<String, RecipeMappingWrapper> categoryRecipeMap, Map<String, String> mobilityTestTypeRecipeMap,
			List<String> fetchKPIList) {
		Map<String, List<String[]>> testTypeWiseDataMap = new HashMap<>();
		List<String[]> combinedDataList = new ArrayList<>();
		if (SSVTReportUtils.validateMap(categoryRecipeMap, ReportConstants.RECIPE_CATEGORY_DRIVE)) {
			List<String[]> driveData = new ArrayList<>();
			for (Map.Entry<String, String> testTypeEntry : mobilityTestTypeRecipeMap.entrySet()) {
				List<String[]> testTypeDriveData = reportService.getDriveDataForReport(workorderid,
						Arrays.asList(testTypeEntry.getValue()), fetchKPIList);
				driveData.addAll(testTypeDriveData);
				testTypeWiseDataMap.put(testTypeEntry.getKey(), testTypeDriveData);
			}
			testTypeWiseDataMap.put(SSVTConstants.DRIVE_DATA_TYPE_DRIVE, driveData);
			combinedDataList.addAll(driveData);
		}
		testTypeWiseDataMap.put(SSVTConstants.DRIVE_DATA_TYPE_COMBINED, combinedDataList);
		return testTypeWiseDataMap;
	}

	private Map<String, String> getMobilityTestTypeMap(Map<String, List<WORecipeMapping>> driveWiseRecipeMap) {
		Map<String, String> mobilityTestTypeMap = new HashMap<>();
		if (SSVTReportUtils.validateMap(driveWiseRecipeMap, SSVTConstants.DRIVE_DATA_TYPE_DRIVE)) {
			List<WORecipeMapping> mobilityRecipeList = driveWiseRecipeMap.get(SSVTConstants.DRIVE_DATA_TYPE_DRIVE);
			for (WORecipeMapping woRecipeMapping : mobilityRecipeList) {
				String recipeId = String.valueOf(woRecipeMapping.getId());
				String scriptJson = woRecipeMapping.getRecipe().getScriptJson();
				List<Map<String, Object>> scriptMapList = new Gson().fromJson(scriptJson, ArrayList.class);
				if (Utils.isValidList(scriptMapList)) {
					for (Map<String, Object> scriptMap : scriptMapList) {
						String scriptName = (String) scriptMap.get(SSVTConstants.KEY_RECIPE_SCRIPT_NAME);
						if ((SSVTConstants.RECIPE_SCRIPT_NAME_DOWNLOAD.equalsIgnoreCase(scriptName)
								|| SSVTConstants.RECIPE_SCRIPT_NAME_SHORT_CALL.equalsIgnoreCase(scriptName)
								|| SSVTConstants.RECIPE_SCRIPT_NAME_LONG_CALL.equalsIgnoreCase(scriptName))
								&& !mobilityTestTypeMap.containsKey(scriptName)) {
							mobilityTestTypeMap.put(scriptName, recipeId);
						} else if (scriptMapList.size() == ForesightConstants.ONE
								&& SSVTConstants.RECIPE_SCRIPT_NAME_IDLE.equalsIgnoreCase(scriptName)
								&& !mobilityTestTypeMap.containsKey(scriptName)) {
							mobilityTestTypeMap.put(scriptName, recipeId);
						}
					}
				}
			}
		}
		logger.info("Going to return mobility recipe map with data: {}", new Gson().toJson(mobilityTestTypeMap));
		return mobilityTestTypeMap;
	}

	private void setCallDataToMainWrapper(Map<String, List<String[]>> testTypeWiseDriveData,
			MasterReportMainWrapper masterMainWrapper, Map<String, Integer> kpiIndexMap) {
		List<String[]> callTypeData = new ArrayList<>();
		if (SSVTReportUtils.validateMap(testTypeWiseDriveData, SSVTConstants.RECIPE_SCRIPT_NAME_SHORT_CALL)) {
			callTypeData.addAll(testTypeWiseDriveData.get(SSVTConstants.RECIPE_SCRIPT_NAME_SHORT_CALL));
		}
		if (SSVTReportUtils.validateMap(testTypeWiseDriveData, SSVTConstants.RECIPE_SCRIPT_NAME_LONG_CALL)) {
			callTypeData.addAll(testTypeWiseDriveData.get(SSVTConstants.RECIPE_SCRIPT_NAME_LONG_CALL));
		}
		VolteCallDataWrapper callDataWrapper = getCallDataWrapperFromDriveData(callTypeData, kpiIndexMap);
		masterMainWrapper.setVolteCallSummaryList(Arrays.asList(callDataWrapper));
	}

	private VolteCallDataWrapper getCallDataWrapperFromDriveData(List<String[]> callTypeData,
			Map<String, Integer> kpiIndexMap) {
		VolteCallDataWrapper callDataWrapper = new VolteCallDataWrapper();
		List<List<String[]>> callDataList = new ArrayList<>();
		int callInitiateCount = 0;
		Integer indexCallInitiate = kpiIndexMap.get(ReportConstants.CALL_INITIATE);
		Integer indexCallSuccess = kpiIndexMap.get(ReportConstants.CALL_SUCCESS);
		Integer indexCallDropped = kpiIndexMap.get(ReportConstants.CALL_DROP);
		Integer indexCallFailed = kpiIndexMap.get(ReportConstants.CALL_FAILURE);
		List<String[]> tempDataList = new ArrayList<>();
		if (indexCallInitiate != null && indexCallSuccess != null && indexCallDropped != null
				&& indexCallFailed != null) {
			for (String[] callDataRow : callTypeData) {
				if (callDataRow != null && callDataRow.length > indexCallSuccess) {
					if (callInitiateCount > 0) {
						tempDataList.add(callDataRow);
					}
					if (NumberUtils.isCreatable(callDataRow[indexCallInitiate])
							&& Integer.parseInt(callDataRow[indexCallInitiate]) > 0) {
						callInitiateCount += Integer.parseInt(callDataRow[indexCallInitiate]);
						tempDataList.add(callDataRow);
					}
					if ((NumberUtils.isCreatable(callDataRow[indexCallSuccess])
							&& Integer.parseInt(callDataRow[indexCallSuccess]) > 0) || (
							NumberUtils.isCreatable(callDataRow[indexCallFailed])
									&& Integer.parseInt(callDataRow[indexCallFailed]) > 0) || (
							NumberUtils.isCreatable(callDataRow[indexCallDropped])
									&& Integer.parseInt(callDataRow[indexCallDropped]) > 0)) {
						if (callInitiateCount > 0) {
							Integer callSuccessCount = NumberUtils.isCreatable(callDataRow[indexCallSuccess]) ?
									Integer.parseInt(callDataRow[indexCallSuccess]) :
									0;
							Integer callFailureCount = NumberUtils.isCreatable(callDataRow[indexCallFailed]) ?
									Integer.parseInt(callDataRow[indexCallFailed]) :
									0;
							Integer callDroppedCount = NumberUtils.isCreatable(callDataRow[indexCallDropped]) ?
									Integer.parseInt(callDataRow[indexCallDropped]) :
									0;
							Integer callCompleteCount = callSuccessCount + callFailureCount + callDroppedCount;
							callInitiateCount -= callCompleteCount;
							for (int i = 0; i < callCompleteCount; i++) {
								List<String[]> dataList = new ArrayList<>(tempDataList);
								callDataList.add(dataList);
							}
							if (callInitiateCount == 0) {
								tempDataList = new ArrayList<>();
							}
						}
					}
				}
			}
		}
		logger.info("Drive Data List for Call Analysis Table: {}", new Gson().toJson(callDataList));
		callDataWrapper.setVolteCallDataList(getCallDataWrapperListFromDriveData(callDataList, kpiIndexMap));
		return callDataWrapper;
	}

	private List<VolteCallData> getCallDataWrapperListFromDriveData(List<List<String[]>> callDataList,
			Map<String, Integer> kpiIndexMap) {
		List<VolteCallData> callDataWrapperList = new ArrayList<>();
		for (List<String[]> singleCallData : callDataList) {
			VolteCallData callDataWrapper = new VolteCallData();
			setTimeDataToCallWrapper(singleCallData, callDataWrapper, kpiIndexMap);
			setKpiDataToCallWrapper(singleCallData, callDataWrapper, kpiIndexMap);
			setCallReleaseReasonToCallWrapper(singleCallData, callDataWrapper, kpiIndexMap);
			setCallSetupTimeInCallWrapper(singleCallData, callDataWrapper, kpiIndexMap);
			callDataWrapperList.add(callDataWrapper);
		}
		logger.info("Call Info Data: {}", new Gson().toJson(callDataWrapperList));
		return callDataWrapperList;
	}

	private void setCallReleaseReasonToCallWrapper(List<String[]> singleCallData, VolteCallData callDataWrapper,
			Map<String, Integer> kpiIndexMap) {
		Integer callDropped = null;
		Integer indexCallDropped = kpiIndexMap.get(ReportConstants.CALL_DROPPED);
		if (indexCallDropped != null && NumberUtils.isCreatable(
				singleCallData.get(singleCallData.size() - 1)[indexCallDropped])) {
			callDropped = Integer.parseInt(singleCallData.get(singleCallData.size() - 1)[indexCallDropped]);
		}
		callDataWrapper.setCallReleaseReason(callDropped != null && callDropped > 0 ? "Dropped" : "Normal");
	}

	private void setTimeDataToCallWrapper(List<String[]> singleCallData, VolteCallData callDataWrapper,
			Map<String, Integer> kpiIndexMap) {
		Integer indexTimestamp = kpiIndexMap.get(ReportConstants.TIMESTAMP);
		if (indexTimestamp != null && NumberUtils.isCreatable(singleCallData.get(0)[indexTimestamp])
				&& NumberUtils.isCreatable(singleCallData.get(singleCallData.size() - 1)[indexTimestamp])) {
			Long startTimeStamp = Long.parseLong(singleCallData.get(0)[indexTimestamp]);
			Date startDate = new Date(startTimeStamp);
			callDataWrapper.setCallStartTime(
					ReportUtil.getFormattedDate(startDate, ReportConstants.DATE_FORMAT_DD_MM_YYYY_HH_MM_SS));
			Long endTimeStamp = Long.parseLong(singleCallData.get(singleCallData.size() - 1)[indexTimestamp]);
			Date endDate = new Date(endTimeStamp);
			callDataWrapper.setCallEndTime(
					ReportUtil.getFormattedDate(endDate, ReportConstants.DATE_FORMAT_DD_MM_YYYY_HH_MM_SS));
			Long callDuration = endTimeStamp - startTimeStamp;
			Long tempDate = SSVTReportUtils.getStartOfDayInMillis();
			Date date = new Date(tempDate + callDuration);
			callDataWrapper.setCallDuration(ReportUtil.getFormattedDate(date, ReportConstants.DATE_FORMAT_HH_MM_SS));
		}
	}

	private void setKpiDataToCallWrapper(List<String[]> singleCallData, VolteCallData callDataWrapper,
			Map<String, Integer> kpiIndexMap) {
		Integer indexRsrp = kpiIndexMap.get(ReportConstants.RSRP);
		Integer indexPci = kpiIndexMap.get(ReportConstants.PCI_PLOT);
		Integer indexPMOS = kpiIndexMap.get(ReportConstants.POLQA_MOS);
		Integer indexIMOS = kpiIndexMap.get(ReportConstants.INST_MOS);
		Integer indexVoLTEJitter = kpiIndexMap.get(ReportConstants.VOLTE_JITTER);
		Integer indexVoLTEPacketLoss = kpiIndexMap.get(ReportConstants.VOLTE_PACKET_LOSS);
		callDataWrapper.setInitRsrp(indexRsrp != null && NumberUtils.isCreatable(singleCallData.get(0)[indexRsrp]) ?
				ReportUtil.round(Double.parseDouble(singleCallData.get(0)[indexRsrp]),
						ReportConstants.TWO_DECIMAL_PLACES) :
				null);
		callDataWrapper.setInitPci(indexPci != null && NumberUtils.isCreatable(singleCallData.get(0)[indexPci]) ?
				Integer.parseInt(singleCallData.get(0)[indexPci]) :
				null);
		if (indexPMOS != null) {
			OptionalDouble optionalMos = getOptionalKpiValueFromData(singleCallData, indexPMOS);
			if (optionalMos.isPresent()) {
				callDataWrapper.setAvgMOS(
						ReportUtil.round(optionalMos.getAsDouble(), ReportConstants.TWO_DECIMAL_PLACES));
			} else if (indexIMOS != null) {
				OptionalDouble optionalInstMos = getOptionalKpiValueFromData(singleCallData, indexIMOS);
				if (optionalInstMos.isPresent()) {
					callDataWrapper.setAvgMOS(
							ReportUtil.round(optionalInstMos.getAsDouble(), ReportConstants.TWO_DECIMAL_PLACES));
				}
			}
		}
		if (indexVoLTEJitter != null) {
			OptionalDouble optionalVolteJitter = getOptionalKpiValueFromData(singleCallData, indexVoLTEJitter);
			if (optionalVolteJitter.isPresent()) {
				callDataWrapper.setJitter(
						ReportUtil.round(optionalVolteJitter.getAsDouble(), ReportConstants.TWO_DECIMAL_PLACES));
			}
		}
		if (indexVoLTEPacketLoss != null) {
			OptionalDouble optionalVoltePacketLoss = getOptionalKpiValueFromData(singleCallData, indexVoLTEPacketLoss);
			if (optionalVoltePacketLoss.isPresent()) {
				callDataWrapper.setAvgPktLossRate(
						ReportUtil.round(optionalVoltePacketLoss.getAsDouble(), ReportConstants.TWO_DECIMAL_PLACES));
			}
		}
	}

	private OptionalDouble getOptionalKpiValueFromData(List<String[]> singleCallData, Integer kpiIndex) {
		return singleCallData.stream()
							 .filter(data -> data != null && NumberUtils.isCreatable(data[kpiIndex]))
							 .mapToDouble(data -> Double.parseDouble(data[kpiIndex]))
							 .average();
	}

	private void setCallSetupTimeInCallWrapper(List<String[]> singleCallData, VolteCallData callDataWrapper,
			Map<String, Integer> kpiIndexMap) {
		Long cstStartTimestamp = null;
		Long cstEndTimestamp = null;
		Integer indexTimestamp = kpiIndexMap.get(ReportConstants.TIMESTAMP);
		Integer indexCallInitiate = kpiIndexMap.get(ReportConstants.CALL_INITIATE);
		Integer indexCallSetupSuccess = kpiIndexMap.get(ReportConstants.CALL_SETUP_SUCCESS);
		if (indexTimestamp != null && indexCallInitiate != null && indexCallSetupSuccess != null) {
			for (String[] row : singleCallData) {
				if (row != null && row.length > indexCallSetupSuccess) {
					if (NumberUtils.isCreatable(row[indexCallInitiate]) && Integer.parseInt(row[indexCallInitiate]) > 0
							&& NumberUtils.isCreatable(row[indexTimestamp])) {
						cstStartTimestamp = Long.parseLong(row[indexTimestamp]);
					}
					if (NumberUtils.isCreatable(row[indexCallSetupSuccess])
							&& Integer.parseInt(row[indexCallSetupSuccess]) > 0 && NumberUtils.isCreatable(
							row[indexTimestamp])) {
						cstEndTimestamp = Long.parseLong(row[indexTimestamp]);
						break;
					}
				}
			}
		}
		if (cstStartTimestamp != null && cstEndTimestamp != null) {
			Long callSetupTime = cstEndTimestamp - cstStartTimestamp;
			Long tempDate = SSVTReportUtils.getStartOfDayInMillis();
			Date date = new Date(tempDate + callSetupTime);
			callDataWrapper.setCallSetupTime(ReportUtil.getFormattedDate(date, ReportConstants.DATE_FORMAT_HH_MM_SS));
		}
	}

	private Map<String, Object> getParameterMapForReport(GenericWorkorder genericWorkorder) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("woName", genericWorkorder.getWorkorderName());
		paramMap.put("testDate", ReportUtil.getFormattedDate(genericWorkorder.getStartDate(),
				ReportConstants.DATE_FORMAT_DD_MM_YY));
		paramMap.put("cityName",
				genericWorkorder.getGeographyl3() != null ? genericWorkorder.getGeographyl3().getDisplayName() : null);
		return paramMap;
	}

	private void setGraphDataToMainWrapper(Integer workorderid, Map<String, RecipeMappingWrapper> categoryRecipeMap,
			MasterGraphPlotContainerWrapper masterContainerWrapper, MasterReportMainWrapper masterMainWrapper,
			Map<String, List<String[]>> mobilityTestTypeRecipeMap, Map<String, Integer> kpiIndexMap,
			List<String> fetchKPIList) {
		List<RecipeDataWrapper> recipeDataList = new ArrayList<>();
		List<LegendWrapper> legendList = legendRangeDao.findAllLegendRangesAppliedTo(ReportConstants.SSVT_REPORT);
		List<KPIWrapper> kpiList = ReportUtil.convertLegendsListToKpiWrapperList(legendList, kpiIndexMap);
		List<MasterPlotDataWrapper> dataWrapper = prepareGraphDataForStationary(workorderid, recipeDataList,
				categoryRecipeMap, kpiList, kpiIndexMap, fetchKPIList);

		List<MasterPlotDataWrapper> driveGraphDataList = prepareGraphDataForDrive(kpiList, mobilityTestTypeRecipeMap);

		//		masterContainerWrapper.setStationaryGraphList(dataWrapper);
		masterContainerWrapper.setMobilityGraphList(driveGraphDataList);
		masterMainWrapper.setStationaryMapList(setImageForStationryTest(recipeDataList, kpiIndexMap));
	}

	private List<MasterPlotDataWrapper> setImageForStationryTest(List<RecipeDataWrapper> recipeDataList,
			Map<String, Integer> kpiIndexMap) {
		List<MasterPlotDataWrapper> stationaryMapList = new ArrayList<>();
		MasterPlotDataWrapper dataWrapper = new MasterPlotDataWrapper();
		DriveImageWrapper driveWrapper = new DriveImageWrapper();
		driveWrapper.setIndexLatitude(kpiIndexMap.get(ReportConstants.LATITUDE));
		driveWrapper.setIndexLongitude(kpiIndexMap.get(ReportConstants.LONGITUDE));
		List<String[]> list = new ArrayList<>();
		for (RecipeDataWrapper recipeWrapper : recipeDataList) {
			list.addAll(recipeWrapper.getDriveDataList());
		}
		driveWrapper.setDataKPIs(list);
		driveWrapper.setSiteDataList(getSiteInfoByReportType(list, kpiIndexMap));
		List<Double[]> pinPointList = getPinPointLatLong(recipeDataList);
		logger.info("pinPointList {}", pinPointList);
		String imageName = ReportConstants.STATIONARY_MAP_INITIAL_IMAGE;
		logger.info("imageName===  {} ", imageName);
		HashMap<String, BufferedImage> bufferedImageMap = mapImageService.getStationaryImages(driveWrapper,
				pinPointList, imageName);
		String stationaryFilePath =
				ConfigUtils.getString(ReportConstants.FINAL_IMAGE_PATH) + Symbol.SLASH_FORWARD_STRING
						+ System.currentTimeMillis();
		HashMap<String, String> imageMap = mapImageService.saveDriveImages(bufferedImageMap, stationaryFilePath, false);
		logger.info("imageMap {}", imageMap);
		dataWrapper.setMapPlot(imageMap.get(ReportConstants.STATIONARY_MAP_INITIAL_IMAGE));
		junkFiles.add(stationaryFilePath);
		dataWrapper.setPlotTitle("Stationary Test Locations");
		stationaryMapList.add(dataWrapper);
		return stationaryMapList;
	}

	private List<Double[]> getPinPointLatLong(List<RecipeDataWrapper> recipeDataList) {
		List<Double[]> latLongList = new ArrayList<>();
		for (RecipeDataWrapper wrapper : recipeDataList) {
			Double[] latLong = new Double[2];
			List<String[]> dataList = wrapper.getDriveDataList();
			if (dataList != null && !dataList.isEmpty()) {
				for (String[] row : dataList) {
					if (SSVTReportUtils.checkValidation(DriveHeaderConstants.INDEX_LAT, row)
							&& SSVTReportUtils.checkValidation(DriveHeaderConstants.INDEX_LON, row)) {
						latLong[1] = Double.valueOf(row[DriveHeaderConstants.INDEX_LAT]);
						latLong[0] = Double.valueOf(row[DriveHeaderConstants.INDEX_LON]);
						latLongList.add(latLong);
						break;
					}
				}
			}
		}
		return latLongList;
	}

	private List<MasterPlotDataWrapper> prepareGraphDataForDrive(List<KPIWrapper> kpiList,
			Map<String, List<String[]>> mobilityTestTypeRecipeMap) {
		List<MasterPlotDataWrapper> driveGraphDataList = new ArrayList<>();
		List<String[]> dataList = mobilityTestTypeRecipeMap.get(SSVTConstants.RECIPE_SCRIPT_NAME_DOWNLOAD);
		List<String[]> idelDataList = mobilityTestTypeRecipeMap.get(SSVTConstants.RECIPE_SCRIPT_NAME_IDLE);
		List<KPIWrapper> idelKpiData = filterKpiData(kpiList,
				Arrays.asList(DriveHeaderConstants.RSRP, DriveHeaderConstants.SINR, ReportConstants.RSRQ));
		List<KPIWrapper> driveDataList = filterKpiData(kpiList,
				Arrays.asList(DriveHeaderConstants.RSRP, DriveHeaderConstants.SINR, ReportConstants.RSRQ,
						ReportConstants.PDSCH_THROUGHPUT));
		if (Utils.isValidList(idelDataList)) {
			driveGraphDataList.addAll(
					setDrivePlotGraphData(calculateStatsFromRawDataForEachKPI(idelKpiData, idelDataList), "IDLE"));
		}
		if (Utils.isValidList(dataList)) {
			driveGraphDataList.addAll(
					setDrivePlotGraphData(calculateStatsFromRawDataForEachKPI(driveDataList, dataList), "CONNECTED"));
		}
		logger.info("driveGraphDataList  {}", new Gson().toJson(driveGraphDataList));
		return driveGraphDataList;
	}

	private List<MasterPlotDataWrapper> setDrivePlotGraphData(List<KPIWrapper> idelKpiData, String testType) {
		List<MasterPlotDataWrapper> driveGraphDataList = new ArrayList<>();
		List<GraphPlotDataWrapper> kpiDataList = new ArrayList<>();
		MasterPlotDataWrapper dataWrapper = new MasterPlotDataWrapper();
		dataWrapper.setPlotTitle(testType + Symbol.SPACE_STRING + "Graph Plots");
		for (KPIWrapper kpiWrapper : idelKpiData) {
			List<MasterGraphDataWrapper> kpiPlotList = new ArrayList<>();
			for (RangeSlab rangeSlab : kpiWrapper.getRangeSlabs()) {
				MasterGraphDataWrapper graphDataWrapper = new MasterGraphDataWrapper();
				graphDataWrapper.setAlphaValue(
						ReportUtil.getPercentage(rangeSlab.getCount(), kpiWrapper.getTotalCount()));
				graphDataWrapper.setTo(String.valueOf(rangeSlab.getLowerLimit()));
				graphDataWrapper.setFrom(String.valueOf(rangeSlab.getUpperLimit()));
				graphDataWrapper.setKpiName(
						kpiWrapper.getKpiName() + Symbol.SPACE_STRING + Symbol.PARENTHESIS_OPEN_STRING
								+ ReportUtil.getUnitByKPiName(kpiWrapper.getKpiName())
								+ Symbol.PARENTHESIS_CLOSE_STRING);
				kpiPlotList.add(graphDataWrapper);
			}
			GraphPlotDataWrapper graphPlotDataWrapper = new GraphPlotDataWrapper();
			graphPlotDataWrapper.setKpiPlotList(kpiPlotList);
			kpiDataList.add(graphPlotDataWrapper);
		}
		dataWrapper.setKpiPlotList(kpiDataList);
		driveGraphDataList.add(dataWrapper);
		return driveGraphDataList;
	}

	List<KPIWrapper> filterKpiData(List<KPIWrapper> idelKpiData, List<String> kpiNames) {
		return idelKpiData.stream()
						  .filter(wrapper -> kpiNames.contains(wrapper.getKpiName().toUpperCase()))
						  .collect(Collectors.toList());
	}

	private void setMapPlotDataToMainWrapper(Map<String, RecipeMappingWrapper> recipeCategoryMap, Integer workorderId,
			MasterGraphPlotContainerWrapper masterContainerWrapper, Map<String, List<String[]>> testTypeWiseDriveData,
			Map<String, String> mobilityTestTypeRecipeMap, Map<String, Integer> kpiIndexMap) {
		if (SSVTReportUtils.validateMap(recipeCategoryMap, ReportConstants.RECIPE_CATEGORY_DRIVE)) {
			RecipeMappingWrapper recipeMappingWrapper = recipeCategoryMap.get(ReportConstants.RECIPE_CATEGORY_DRIVE);
			List<String> operatorList = recipeMappingWrapper.getOperatorList();
			List<MasterPlotDataWrapper> mobilityMapList = new ArrayList<>();
			for (Entry<String, List<String[]>> testTypeMapEntry : testTypeWiseDriveData.entrySet()) {
				if (Utils.isValidList(Arrays.asList(mobilityTestTypeRecipeMap.get(testTypeMapEntry.getKey())))) {
					mobilityMapList.addAll(getMapDataWrapperForRecipe(workorderId,
							Arrays.asList(mobilityTestTypeRecipeMap.get(testTypeMapEntry.getKey())), operatorList,
							testTypeMapEntry.getValue(), testTypeMapEntry.getKey(), kpiIndexMap));
				}
			}
			masterContainerWrapper.setMobilityMapList(mobilityMapList);
		}
	}

	private List<MasterPlotDataWrapper> getMapDataWrapperForRecipe(Integer workorderId, List<String> recipeList,
			List<String> operatorList, List<String[]> driveData, String testType, Map<String, Integer> kpiIndexMap) {
		try {
			if (SSVTConstants.RECIPE_SCRIPT_NAME_DOWNLOAD.equalsIgnoreCase(testType)
					|| SSVTConstants.RECIPE_SCRIPT_NAME_SHORT_CALL.equalsIgnoreCase(testType)
					|| SSVTConstants.RECIPE_SCRIPT_NAME_LONG_CALL.equalsIgnoreCase(testType)
					|| SSVTConstants.RECIPE_SCRIPT_NAME_IDLE.equalsIgnoreCase(testType)) {
				List<MasterPlotDataWrapper> plotDataWrapperList = new ArrayList<>();
				HashMap<String, Object> imagesMap = getDriveImagesForReport(getKpiIndexMap(testType, kpiIndexMap), false, driveData, kpiIndexMap);
				if (SSVTConstants.RECIPE_SCRIPT_NAME_DOWNLOAD.equalsIgnoreCase(testType)) {
					setMapPlotDataToWrapperList(getMapPlotWrapperForKPI(
							testType + Symbol.UNDERSCORE_STRING + ReportConstants.PDSCH_THROUGHPUT,
							(String) imagesMap.get(ReportConstants.IMAGE_DL),
							(String) imagesMap.get(ReportConstants.IMAGE_DL_LEGEND)), plotDataWrapperList);
					setMapPlotDataToWrapperList(
							getMapPlotWrapperForKPI(testType + Symbol.UNDERSCORE_STRING + KPI.RSRP.name(),
									(String) imagesMap.get(ReportConstants.IMAGE_RSRP),
									(String) imagesMap.get(ReportConstants.IMAGE_RSRP_LEGEND)), plotDataWrapperList);
					setMapPlotDataToWrapperList(
							getMapPlotWrapperForKPI(testType + Symbol.UNDERSCORE_STRING + KPI.RSRQ.name(),
									(String) imagesMap.get(ReportConstants.IMAGE_RSRQ),
									(String) imagesMap.get(ReportConstants.IMAGE_RSRQ_LEGEND)), plotDataWrapperList);
					setMapPlotDataToWrapperList(
							getMapPlotWrapperForKPI(testType + Symbol.UNDERSCORE_STRING + KPI.SINR.name(),
									(String) imagesMap.get(ReportConstants.IMAGE_SINR),
									(String) imagesMap.get(ReportConstants.IMAGE_SINR_LEGEND)), plotDataWrapperList);
					setMapPlotDataToWrapperList(
							getMapPlotWrapperForKPI(testType + Symbol.UNDERSCORE_STRING + KPI.PCI.name(),
									(String) imagesMap.get(ReportConstants.IMAGE_PCI),
									(String) imagesMap.get(ReportConstants.IMAGE_PCI_LEGEND)), plotDataWrapperList);
				} else if ((SSVTConstants.RECIPE_SCRIPT_NAME_SHORT_CALL.equalsIgnoreCase(testType)
						|| SSVTConstants.RECIPE_SCRIPT_NAME_LONG_CALL.equalsIgnoreCase(testType))) {
					setMapPlotDataToWrapperList(getMapPlotWrapperForKPI(NVReportConstants.VOLTE_JITTER,
							(String) imagesMap.get(NVReportConstants.IMAGE_VOLTE_JITTER),
							(String) imagesMap.get(NVReportConstants.IMAGE_VOLTE_JITTER_LEGEND)), plotDataWrapperList);
					setMapPlotDataToWrapperList(getMapPlotWrapperForKPI(NVReportConstants.VOLTE_PACKET_LOSS,
							(String) imagesMap.get(NVReportConstants.IMAGE_VOLTE_PACKET_LOSS),
							(String) imagesMap.get(NVReportConstants.IMAGE_VOLTE_PACKET_LOSS_LEGEND)),
							plotDataWrapperList);
					if (!StringUtils.isBlank((String) imagesMap.get(NVReportConstants.IMAGE_VOLTE_MOS))) {
						setMapPlotDataToWrapperList(getMapPlotWrapperForKPI(ReportConstants.MOS,
								(String) imagesMap.get(NVReportConstants.IMAGE_VOLTE_MOS),
								(String) imagesMap.get(NVReportConstants.IMAGE_VOLTE_MOS_LEGEND)), plotDataWrapperList);
					} else {
						setMapPlotDataToWrapperList(getMapPlotWrapperForKPI(ReportConstants.MOS,
								(String) imagesMap.get(ReportConstants.IMAGE_MOS),
								(String) imagesMap.get(ReportConstants.IMAGE_MOS_LEGEND)), plotDataWrapperList);
					}
				} else if (SSVTConstants.RECIPE_SCRIPT_NAME_IDLE.equalsIgnoreCase(testType)) {
					setMapPlotDataToWrapperList(
							getMapPlotWrapperForKPI(testType + Symbol.UNDERSCORE_STRING + KPI.RSRP.name(),
									(String) imagesMap.get(ReportConstants.IMAGE_RSRP),
									(String) imagesMap.get(ReportConstants.IMAGE_RSRP_LEGEND)), plotDataWrapperList);
					setMapPlotDataToWrapperList(getMapPlotWrapperForKPI(testType + KPI.RSRQ.name(),
							(String) imagesMap.get(ReportConstants.IMAGE_RSRQ),
							(String) imagesMap.get(ReportConstants.IMAGE_RSRQ_LEGEND)), plotDataWrapperList);
					setMapPlotDataToWrapperList(getMapPlotWrapperForKPI(testType + KPI.SINR.name(),
							(String) imagesMap.get(ReportConstants.IMAGE_SINR),
							(String) imagesMap.get(ReportConstants.IMAGE_SINR_LEGEND)), plotDataWrapperList);
				}
				return plotDataWrapperList;
			}
		} catch (IOException e) {
			logger.error("Error while getting images for workorder: {}, Error: {}", workorderId,
					Utils.getStackTrace(e));
		}
		return Collections.emptyList();
	}

	private void setMapPlotDataToWrapperList(MasterPlotDataWrapper plotWrapper,
			List<MasterPlotDataWrapper> plotDataWrapperList) {
		if (plotWrapper != null && !StringUtils.isBlank(plotWrapper.getMapPlot())) {
			plotDataWrapperList.add(plotWrapper);
		}
	}

	private MasterPlotDataWrapper getMapPlotWrapperForKPI(String kpiName, String plotImage, String legendImage) {
		if (plotImage != null) {
			MasterPlotDataWrapper plotDataWrapper = new MasterPlotDataWrapper();
			plotDataWrapper.setPlotTitle(kpiName);
			plotDataWrapper.setMapPlot(plotImage);
			plotDataWrapper.setLegendImage(legendImage);
			return plotDataWrapper;
		}
		return null;
	}

	private void setSummaryDataToMainWrapper(Integer workorderid, Map<String, RecipeMappingWrapper> categoryRecipeMap,
			MasterReportMainWrapper masterMainWrapper, List<String> fetchSummaryKPIList,
			Map<String, Integer> kpiSummaryIndexMap) {
		List<MasterSummaryDataWrapper> stationarySummaryDataList = new ArrayList<>();
		List<MasterSummaryDataWrapper> driveSummaryDataList = new ArrayList<>();
		getSummaryDataForStationary(workorderid, categoryRecipeMap, stationarySummaryDataList,
				ReportConstants.RECIPE_CATEGORY_STATIONARY, kpiSummaryIndexMap, fetchSummaryKPIList);
		
		getSummaryDataForDrive(workorderid, categoryRecipeMap, driveSummaryDataList,
				ReportConstants.RECIPE_CATEGORY_DRIVE, kpiSummaryIndexMap, fetchSummaryKPIList);
		MasterSummaryWrapper summaryWrapper = new MasterSummaryWrapper();
		summaryWrapper.setStationaryDataList(stationarySummaryDataList);
		summaryWrapper.setMobilityDataList(driveSummaryDataList);
		masterMainWrapper.setSummaryDataList(Arrays.asList(summaryWrapper));
	}

	private List<MasterPlotDataWrapper> prepareGraphDataForStationary(Integer workorderid,
			List<RecipeDataWrapper> recipeDataist, Map<String, RecipeMappingWrapper> categoryRecipeMap,
			List<KPIWrapper> kpiList, Map<String, Integer> kpiIndexMap, List<String> fetchKPIList) {
		RecipeMappingWrapper recipeMappingWrapper = categoryRecipeMap.get(ReportConstants.RECIPE_CATEGORY_STATIONARY);

		List<String> recipeList = recipeMappingWrapper.getRecpiList();
		for (int i = ReportConstants.INDEX_ZER0; i < recipeList.size(); i++) {
			RecipeDataWrapper wrapper = new RecipeDataWrapper();
			List<String[]> dataList = reportService.getDriveDataForReport(workorderid,
					Arrays.asList(recipeList.get(i)), fetchKPIList);
			wrapper.setDriveDataList(dataList);
			wrapper.setSectorName("Sector" + (i + ReportConstants.INDEX_ONE));
			wrapper.setRecipeId(recipeList.get(i));
			wrapper.setOperatorName(recipeMappingWrapper.getOperatorList().get(i));
			recipeDataist.add(wrapper);
		}
		return setStationaryGraphData(kpiList, recipeDataist);
	}

	private List<MasterPlotDataWrapper> setStationaryGraphData(List<KPIWrapper> kpiList,
			List<RecipeDataWrapper> recipeDataist) {
		List<MasterPlotDataWrapper> stationarGraphDataList = new ArrayList<>();
		List<GraphPlotDataWrapper> kpiDataList = new ArrayList<>();
		MasterPlotDataWrapper plotDataWrapper = new MasterPlotDataWrapper();
		plotDataWrapper.setPlotTitle("Stationary Graph Plots");
		for (KPIWrapper kpiWrapper : kpiList) {
			if (!kpiWrapper.getKpiName().equalsIgnoreCase(ReportConstants.HO_INTERRUPTION_TIME)) {
				List<MasterGraphDataWrapper> kpiPlotList = new ArrayList<>();
				List<RangeSlab> sortedRangeSlab = ReportUtil.getSortedRangeSlabList(kpiWrapper.getRangeSlabs());
				for (RangeSlab rangeSlab : sortedRangeSlab) {
					MasterGraphDataWrapper graphDataWrapper = new MasterGraphDataWrapper();
					graphDataWrapper.setFrom(String.valueOf(rangeSlab.getUpperLimit()));
					graphDataWrapper.setTo(String.valueOf(rangeSlab.getLowerLimit()));
					graphDataWrapper.setKpiName(kpiWrapper.getKpiName());
					setRecipeWiseDataForRange(rangeSlab.getLowerLimit(), rangeSlab.getUpperLimit(), graphDataWrapper,
							recipeDataist, kpiWrapper.getIndexKPI());
					kpiPlotList.add(graphDataWrapper);
				}
				GraphPlotDataWrapper graphPlotDataWrapper = new GraphPlotDataWrapper();
				graphPlotDataWrapper.setKpiPlotList(kpiPlotList);
				kpiDataList.add(graphPlotDataWrapper);
			}
		}
		plotDataWrapper.setKpiPlotList(kpiDataList);
		stationarGraphDataList.add(plotDataWrapper);
		logger.info("stationarGraphDataList {}", new Gson().toJson(stationarGraphDataList));
		return stationarGraphDataList;
	}

	private String getPlotTitleByKpiName(String kpiName) {
		if (kpiName.equalsIgnoreCase(ReportConstants.LATENCY32)) {
			return "ICMP Ping(32 Bytes)";
		} else if (kpiName.equalsIgnoreCase(ReportConstants.LATENCY1000)) {
			return "ICMP Ping(1000 Bytes)";
		} else if (kpiName.equalsIgnoreCase(ReportConstants.LATENCY1500)) {
			return "ICMP Ping(1500 Bytes)";
		}
		return kpiName;
	}

	private void setRecipeWiseDataForRange(Double lowerLimit, Double upperLimit,
			MasterGraphDataWrapper graphDataWrapper, List<RecipeDataWrapper> recipeDataist, Integer kpiIndex) {
		for (int i = ReportConstants.INDEX_ZER0; i < recipeDataist.size(); i++) {
			RecipeDataWrapper wrapper = recipeDataist.get(i);
			if (wrapper != null && wrapper.getDriveDataList() != null && (!wrapper.getDriveDataList().isEmpty())) {
				List<Double> dataList = ReportUtil.convetArrayToList(wrapper.getDriveDataList(), kpiIndex);
				Long count = dataList.stream().filter(value -> (value < upperLimit && value >= lowerLimit)).count();
				if (i == ReportConstants.INDEX_ZER0) {
					graphDataWrapper.setAlphaValue(ReportUtil.getPercentage(count.intValue(), dataList.size()));
				} else if (i == ReportConstants.INDEX_ONE) {
					graphDataWrapper.setBetaValue(ReportUtil.getPercentage(count.intValue(), dataList.size()));
				} else if (i == ReportConstants.INDEX_TWO) {
					graphDataWrapper.setGammaValue(ReportUtil.getPercentage(count.intValue(), dataList.size()));
				}
			}

		}
	}

	private List<KPIWrapper> calculateStatsFromRawDataForEachKPI(List<KPIWrapper> kpiList, List<String[]> rawDataList) {
		logger.info("inside method calculateStatsFromRawDataForEachKPI");
		List<KPIWrapper> newList = new ArrayList<>();
		for (KPIWrapper kpiWrapper : kpiList) {
			List<Double> dataList = ReportUtil.convetArrayToList(rawDataList, kpiWrapper.getIndexKPI());
			kpiWrapper.setTotalCount(dataList.size());
			List<RangeSlab> sortedRangeSlab = ReportUtil.getSortedRangeSlabList(kpiWrapper.getRangeSlabs());
			for (RangeSlab rangeSlab : sortedRangeSlab) {
				Long count = dataList.stream()
									 .filter(value -> (value < rangeSlab.getUpperLimit()
											 && value >= rangeSlab.getLowerLimit()))
									 .count();
				rangeSlab.setCount(count.intValue());
			}
			newList.add(kpiWrapper);
		}
		return newList;
	}

	static Map<String, Integer> getKpiIndexMap() {
		Map<String, Integer> map = new HashMap<>();
		map.put(DriveHeaderConstants.RSRP, DriveHeaderConstants.INDEX_RSRP); // 5
		map.put(DriveHeaderConstants.SINR, DriveHeaderConstants.INDEX_SINR); // 7
		map.put(ReportConstants.RSRQ, DriveHeaderConstants.INDEX_RSRQ);
		map.put(ReportConstants.PDSCH_THROUGHPUT, NVReportConstants.DRIVE_INDEX_PDSCH_THROUGHPUT);
		return map;
	}

	static Map<String, Integer> getKpiIndexMap(String testType, Map<String, Integer> kpiIndexMap) {
		Map<String, Integer> map = new HashMap<>();
		switch (testType) {
		case SSVTConstants.RECIPE_SCRIPT_NAME_DOWNLOAD:
			map.put(ReportConstants.RSRP, kpiIndexMap.get(ReportConstants.RSRP)); // 5
			map.put(ReportConstants.SINR, kpiIndexMap.get(ReportConstants.SINR)); // 7
			map.put(ReportConstants.RSRQ, kpiIndexMap.get(ReportConstants.RSRQ));
			map.put(ReportConstants.PDSCH_THROUGHPUT, kpiIndexMap.get(ReportConstants.PDSCH_THROUGHPUT));
			map.put(ReportConstants.PCI_PLOT, kpiIndexMap.get(ReportConstants.PCI_PLOT));
			break;
		case SSVTConstants.RECIPE_SCRIPT_NAME_LONG_CALL:
		case SSVTConstants.RECIPE_SCRIPT_NAME_SHORT_CALL:
			map.put(NVReportConstants.VOLTE_JITTER, kpiIndexMap.get(ReportConstants.VOLTE_JITTER));
			map.put(NVReportConstants.VOLTE_PACKET_LOSS, kpiIndexMap.get(ReportConstants.VOLTE_PACKET_LOSS));
			map.put(ReportConstants.MOS, kpiIndexMap.get(ReportConstants.MOS));
			map.put(NVReportConstants.POLQA_MOS, kpiIndexMap.get(ReportConstants.POLQA_MOS));
			break;
		case SSVTConstants.RECIPE_SCRIPT_NAME_IDLE:
			map.put(ReportConstants.RSRP, kpiIndexMap.get(ReportConstants.RSRP)); // 5
			map.put(ReportConstants.SINR, kpiIndexMap.get(ReportConstants.SINR)); // 7
			map.put(ReportConstants.RSRQ, kpiIndexMap.get(ReportConstants.RSRQ));
			break;
		default:
			break;
		}
		return map;
	}

	private void getSummaryDataForStationary(Integer workorderid, Map<String, RecipeMappingWrapper> categoryRecipeMap,
			List<MasterSummaryDataWrapper> summaryDataList, String category, Map<String, Integer> kpiSummaryIndexMap,
			List<String> fetchSummaryKPIList) {
		RecipeMappingWrapper recipeMappingWrapper = categoryRecipeMap.get(category);

		if (recipeMappingWrapper != null) {
			List<String> recipeList = recipeMappingWrapper.getRecpiList();
			//List<String> operatorList = recipeMappingWrapper.getOperatorList();
			if (recipeList != null && !recipeList.isEmpty()) {
				for (int i = 0; i < recipeList.size(); i++) {
					String[] summaryData = reportService.getSummaryDataForReport(workorderid,
							Arrays.asList(recipeList.get(i)), fetchSummaryKPIList);
					//Double avgBufferTime = getYouTubeBufferTimeAvg(workorderid, recipeList, operatorList, i);
					setSummaryDataToWrapper(summaryDataList, summaryData, kpiSummaryIndexMap);
				}
			}

		}
	}

	private Double getYouTubeBufferTimeAvg(Integer workorderid, List<String> recipeList, List<String> operatorList,
			int i) {
		try {
			String youTubeData = nvLayer3DashboardService.getYoutubeDataFromLayer3Report(workorderid,
					operatorList.get(i), recipeList.get(i));
			logger.info("youTubeData  is {}", youTubeData);
			List<YoutubeTestWrapper> listYoutubeData = new ArrayList<>();
			if (youTubeData != null && !youTubeData.isEmpty()) {
				listYoutubeData.addAll(ReportUtil.getYouTubeTestDataWrapper(youTubeData));
			}
			return Utils.isValidList(listYoutubeData) ?
					listYoutubeData.stream()
								   .mapToDouble(YoutubeTestWrapper::getTotalBufferTime)
								   .average()
								   .getAsDouble() :
					null;
		} catch (Exception e) {
			logger.error("Exception inside the method getYouTubeBufferTimeAvg {} ", Utils.getStackTrace(e));
		}
		return null;
	}

	private void getSummaryDataForDrive(Integer workorderid, Map<String, RecipeMappingWrapper> categoryRecipeMap,
			List<MasterSummaryDataWrapper> summaryDataList, String category, Map<String, Integer> kpiSummaryIndexMap,
			List<String> fetchSummaryKPIList) {
		RecipeMappingWrapper recipeMappingWrapper = categoryRecipeMap.get(category);
		if (recipeMappingWrapper != null) {
			String[] summaryData = reportService.getSummaryDataForReport(workorderid,
					recipeMappingWrapper.getRecpiList(), fetchSummaryKPIList);
			setSummaryDataToWrapper(summaryDataList, summaryData, kpiSummaryIndexMap);
		}
	}


	private void setSummaryDataToWrapper(List<MasterSummaryDataWrapper> stationrySummaryDataList, String[] summaryDataArray,
			Map<String, Integer> kpiSummaryIndexMap) {
		logger.info("inside the method setSummaryDataToWrapper kpiSummaryIndexMap {}",new Gson().toJson(kpiSummaryIndexMap));
		MasterSummaryDataWrapper wrapper = new MasterSummaryDataWrapper();
		// set rsrp
		wrapper.setMinRsrp(
				kpiSummaryIndexMap.get(ReportConstants.MIN_UNDERSCORE + ReportConstants.RSRP) != null ?
						NVLayer3Utils.getDoubleFromCsv(summaryDataArray, kpiSummaryIndexMap.get(
								ReportConstants.MIN_UNDERSCORE + ReportConstants.RSRP)) :
						null);
		
		wrapper.setMaxRsrp(
				kpiSummaryIndexMap.get(ReportConstants.MAX_UNDERSCORE + ReportConstants.RSRP) != null ?
						NVLayer3Utils.getDoubleFromCsv(summaryDataArray, kpiSummaryIndexMap.get(
								ReportConstants.MAX_UNDERSCORE + ReportConstants.RSRP)) :
						null);
		wrapper.setAvgRsrp(kpiSummaryIndexMap.get(ReportConstants.AVG_UNDERSCORE+ReportConstants.RSRP) != null ?
				NVLayer3Utils.getDoubleFromCsv(summaryDataArray, kpiSummaryIndexMap.get(ReportConstants.AVG_UNDERSCORE+ReportConstants.RSRP)) :
				null);

		// set sinr
		wrapper.setMinSinr(
				kpiSummaryIndexMap.get(ReportConstants.MIN_UNDERSCORE + ReportConstants.SINR) != null ?
						NVLayer3Utils.getDoubleFromCsv(summaryDataArray, kpiSummaryIndexMap.get(
								ReportConstants.MIN_UNDERSCORE+ ReportConstants.SINR)) :
						null);
		wrapper.setMaxSinr(
				kpiSummaryIndexMap.get(ReportConstants.MAX_UNDERSCORE + ReportConstants.SINR) != null ?
						NVLayer3Utils.getDoubleFromCsv(summaryDataArray, kpiSummaryIndexMap.get(
								ReportConstants.MAX_UNDERSCORE + ReportConstants.SINR)) :
						null);
		wrapper.setAvgSinr(kpiSummaryIndexMap.get(ReportConstants.AVG_UNDERSCORE+ReportConstants.SINR) != null ?
				NVLayer3Utils.getDoubleFromCsv(summaryDataArray, kpiSummaryIndexMap.get(ReportConstants.AVG_UNDERSCORE+ReportConstants.SINR)) :
				null);

		// set rsrq
		wrapper.setMinRsrq(
				kpiSummaryIndexMap.get(ReportConstants.MIN_UNDERSCORE + ReportConstants.RSRQ) != null ?
						NVLayer3Utils.getDoubleFromCsv(summaryDataArray, kpiSummaryIndexMap.get(
								ReportConstants.MIN_UNDERSCORE + ReportConstants.RSRQ)) :
						null);
		wrapper.setMaxRsrq(
				kpiSummaryIndexMap.get(ReportConstants.MAX_UNDERSCORE + ReportConstants.RSRQ) != null ?
						NVLayer3Utils.getDoubleFromCsv(summaryDataArray, kpiSummaryIndexMap.get(
								ReportConstants.MAX_UNDERSCORE + ReportConstants.RSRQ)) :
						null);
		wrapper.setAvgRsrq(kpiSummaryIndexMap.get(ReportConstants.AVG_UNDERSCORE+ReportConstants.RSRQ) != null ?
				NVLayer3Utils.getDoubleFromCsv(summaryDataArray, kpiSummaryIndexMap.get(ReportConstants.AVG_UNDERSCORE+ReportConstants.RSRQ)) :
				null);
		// set http dl
		wrapper.setAvgDl(kpiSummaryIndexMap.get(ReportConstants.AVG_UNDERSCORE+ReportConstants.PDSCH_THROUGHPUT) != null ?
				NVLayer3Utils.getDoubleFromCsv(summaryDataArray,
						kpiSummaryIndexMap.get(ReportConstants.AVG_UNDERSCORE+ReportConstants.PDSCH_THROUGHPUT)) :
				null);
		wrapper.setMinDl(kpiSummaryIndexMap.get(
				ReportConstants.MIN_UNDERSCORE+ ReportConstants.PDSCH_THROUGHPUT) != null ?
				NVLayer3Utils.getDoubleFromCsv(summaryDataArray, kpiSummaryIndexMap.get(
						ReportConstants.MIN_UNDERSCORE + ReportConstants.PDSCH_THROUGHPUT)) :
				null);
		wrapper.setMaxDl(kpiSummaryIndexMap.get(
				ReportConstants.MAX_UNDERSCORE + ReportConstants.PDSCH_THROUGHPUT) != null ?
				NVLayer3Utils.getDoubleFromCsv(summaryDataArray, kpiSummaryIndexMap.get(
						ReportConstants.MAX_UNDERSCORE + ReportConstants.PDSCH_THROUGHPUT)) :
				null);

		// set http ul
		wrapper.setAvgUl(kpiSummaryIndexMap.get(ReportConstants.AVG_UNDERSCORE+ReportConstants.PUSCH_THROUGHPUT) != null ?
				NVLayer3Utils.getDoubleFromCsv(summaryDataArray,
						kpiSummaryIndexMap.get(ReportConstants.AVG_UNDERSCORE+ReportConstants.PUSCH_THROUGHPUT)) :
				null);
		wrapper.setMinUl(kpiSummaryIndexMap.get(
				ReportConstants.MIN_UNDERSCORE + ReportConstants.PUSCH_THROUGHPUT) != null ?
				NVLayer3Utils.getDoubleFromCsv(summaryDataArray, kpiSummaryIndexMap.get(
						ReportConstants.MIN_UNDERSCORE + ReportConstants.PUSCH_THROUGHPUT)) :
				null);
		wrapper.setMaxUl(kpiSummaryIndexMap.get(
				ReportConstants.MAX_UNDERSCORE+ ReportConstants.PUSCH_THROUGHPUT) != null ?
				NVLayer3Utils.getDoubleFromCsv(summaryDataArray, kpiSummaryIndexMap.get(
						ReportConstants.MAX_UNDERSCORE + ReportConstants.PUSCH_THROUGHPUT)) :
				null);

		// set attached
		wrapper.setAttachAttempt(kpiSummaryIndexMap.get(ReportConstants.SUM_UNDERSCORE+ReportConstants.ATTACH_REQUEST) != null ?
				NVLayer3Utils.getInteger(summaryDataArray, kpiSummaryIndexMap.get(ReportConstants.SUM_UNDERSCORE+ReportConstants.ATTACH_REQUEST)) :
				null);
		wrapper.setAttachSuccess(kpiSummaryIndexMap.get(ReportConstants.SUM_UNDERSCORE+ReportConstants.ATTACH_COMPLETE) != null ?
				NVLayer3Utils.getInteger(summaryDataArray, kpiSummaryIndexMap.get(ReportConstants.SUM_UNDERSCORE+ReportConstants.ATTACH_COMPLETE)) :
				null);
		wrapper.setAttachSuccessRatio(ReportUtil.getPercentage(wrapper.getAttachSuccess(), wrapper.getAttachAttempt()));

		wrapper.setDetachAttempt(kpiSummaryIndexMap.get(ReportConstants.SUM_UNDERSCORE+ReportConstants.DETACH_REQUEST) != null ?
				NVLayer3Utils.getInteger(summaryDataArray, kpiSummaryIndexMap.get(ReportConstants.SUM_UNDERSCORE+ReportConstants.DETACH_REQUEST)) :
				null);
		wrapper.setDetachSuccess(kpiSummaryIndexMap.get(ReportConstants.SUM_UNDERSCORE+ReportConstants.DETACH_REQUEST) != null ?
				NVLayer3Utils.getInteger(summaryDataArray, kpiSummaryIndexMap.get(ReportConstants.SUM_UNDERSCORE+ReportConstants.DETACH_REQUEST)) :
				null);
		wrapper.setDetachSuccessRatio(ReportUtil.getPercentage(wrapper.getDetachSuccess(), wrapper.getDetachAttempt()));

		wrapper.setReselectionAttempt(kpiSummaryIndexMap.get(ReportConstants.SUM_UNDERSCORE+ReportConstants.RESELECTION_SUCCESS) != null ?
				NVLayer3Utils.getInteger(summaryDataArray,
						kpiSummaryIndexMap.get(ReportConstants.SUM_UNDERSCORE+ReportConstants.RESELECTION_SUCCESS)) :
				null);
		wrapper.setReselectionSuccess(kpiSummaryIndexMap.get(ReportConstants.SUM_UNDERSCORE+ReportConstants.RESELECTION_SUCCESS) != null ?
				NVLayer3Utils.getInteger(summaryDataArray,
						kpiSummaryIndexMap.get(ReportConstants.SUM_UNDERSCORE+ReportConstants.RESELECTION_SUCCESS)) :
				null);
		wrapper.setReselectionSuccessRatio(
				ReportUtil.getPercentage(wrapper.getReselectionAttempt(), wrapper.getReselectionAttempt()));

		wrapper.setHandoverAttempt(kpiSummaryIndexMap.get(ReportConstants.SUM_UNDERSCORE +ReportConstants.HANDOVER_INITIATE) != null ?
				NVLayer3Utils.getInteger(summaryDataArray, kpiSummaryIndexMap.get(ReportConstants.SUM_UNDERSCORE +ReportConstants.HANDOVER_INITIATE)) :
				null);
		wrapper.setHandoverSuccess(kpiSummaryIndexMap.get(ReportConstants.SUM_UNDERSCORE +ReportConstants.HANDOVER_SUCCESS) != null ?
				NVLayer3Utils.getInteger(summaryDataArray, kpiSummaryIndexMap.get(ReportConstants.SUM_UNDERSCORE +ReportConstants.HANDOVER_SUCCESS)) :
				null);
		wrapper.setHandoverSuccessRatio(
				ReportUtil.getPercentage(wrapper.getHandoverSuccess(), wrapper.getHandoverAttempt()));

		wrapper.setMinInterruptionTime(kpiSummaryIndexMap.get(
				ReportConstants.MIN_UNDERSCORE + ReportConstants.HO_INTERRUPTION_TIME) != null ?
				NVLayer3Utils.getDoubleFromCsv(summaryDataArray, kpiSummaryIndexMap.get(
						ReportConstants.MIN_UNDERSCORE + ReportConstants.HO_INTERRUPTION_TIME)) :
				null);
		wrapper.setMaxInterruptionTime(kpiSummaryIndexMap.get(
				ReportConstants.MAX_UNDERSCORE+ ReportConstants.HO_INTERRUPTION_TIME) != null ?
				NVLayer3Utils.getDoubleFromCsv(summaryDataArray, kpiSummaryIndexMap.get(
						ReportConstants.MAX_UNDERSCORE + ReportConstants.HO_INTERRUPTION_TIME)) :
				null);
		wrapper.setAvgInterruptionTime(kpiSummaryIndexMap.get(ReportConstants.AVG_UNDERSCORE +ReportConstants.HO_INTERRUPTION_TIME) != null ?
				NVLayer3Utils.getDoubleFromCsv(summaryDataArray,
						kpiSummaryIndexMap.get(ReportConstants.AVG_UNDERSCORE +ReportConstants.HO_INTERRUPTION_TIME)) :
				null);

		wrapper.setMinPing32(
				kpiSummaryIndexMap.get(ReportConstants.MIN_UNDERSCORE + ReportConstants.LATENCY32)
						!= null ?
						NVLayer3Utils.getDoubleFromCsv(summaryDataArray, kpiSummaryIndexMap.get(
								ReportConstants.MIN_UNDERSCORE + ReportConstants.LATENCY32)) :
						null);
		wrapper.setMaxPing32(
				kpiSummaryIndexMap.get(ReportConstants.MAX_UNDERSCORE + ReportConstants.LATENCY32)
						!= null ?
						NVLayer3Utils.getDoubleFromCsv(summaryDataArray, kpiSummaryIndexMap.get(
								ReportConstants.MAX_UNDERSCORE + ReportConstants.LATENCY32)) :
						null);
		wrapper.setAvgPing32(kpiSummaryIndexMap.get(ReportConstants.AVG_UNDERSCORE+ReportConstants.LATENCY32) != null ?
				NVLayer3Utils.getDoubleFromCsv(summaryDataArray, kpiSummaryIndexMap.get(ReportConstants.LATENCY32)) :
				null);

		wrapper.setMinPing1000(
				kpiSummaryIndexMap.get(ReportConstants.MIN_UNDERSCORE + ReportConstants.LATENCY1000)
						!= null ?
						NVLayer3Utils.getDoubleFromCsv(summaryDataArray, kpiSummaryIndexMap.get(
								ReportConstants.MIN_UNDERSCORE + ReportConstants.LATENCY1000)) :
						null);
		wrapper.setMaxPing1000(
				kpiSummaryIndexMap.get(ReportConstants.MAX_UNDERSCORE + ReportConstants.LATENCY1000)
						!= null ?
						NVLayer3Utils.getDoubleFromCsv(summaryDataArray, kpiSummaryIndexMap.get(
								ReportConstants.MAX_UNDERSCORE + ReportConstants.LATENCY1000)) :
						null);
		wrapper.setAvgPing1000(kpiSummaryIndexMap.get(ReportConstants.AVG_UNDERSCORE+ReportConstants.LATENCY1000) != null ?
				NVLayer3Utils.getDoubleFromCsv(summaryDataArray, kpiSummaryIndexMap.get(ReportConstants.AVG_UNDERSCORE+ReportConstants.LATENCY1000)) :
				null);

		wrapper.setMinPing1500(
				kpiSummaryIndexMap.get(ReportConstants.MIN_UNDERSCORE + ReportConstants.LATENCY1500)
						!= null ?
						NVLayer3Utils.getDoubleFromCsv(summaryDataArray, kpiSummaryIndexMap.get(
								ReportConstants.MIN_UNDERSCORE + ReportConstants.LATENCY1500)) :
						null);
		wrapper.setMaxPing1500(
				kpiSummaryIndexMap.get(ReportConstants.MAX_UNDERSCORE + ReportConstants.LATENCY1500)
						!= null ?
						NVLayer3Utils.getDoubleFromCsv(summaryDataArray, kpiSummaryIndexMap.get(
								ReportConstants.MAX_UNDERSCORE + ReportConstants.LATENCY1500)) :
						null);
		wrapper.setAvgPing1500(kpiSummaryIndexMap.get(ReportConstants.AVG_UNDERSCORE+ReportConstants.LATENCY1500) != null ?
				NVLayer3Utils.getDoubleFromCsv(summaryDataArray, kpiSummaryIndexMap.get(ReportConstants.AVG_UNDERSCORE+ReportConstants.LATENCY1500)) :
				null);

		wrapper.setMinBrowsingLoadTime(
				kpiSummaryIndexMap.get(ReportConstants.MIN_UNDERSCORE + ReportConstants.WEB_DOWNLOAD_DELAY)
						!= null ?
						NVLayer3Utils.getDoubleFromCsv(summaryDataArray, kpiSummaryIndexMap.get(
								ReportConstants.MIN_UNDERSCORE + ReportConstants.WEB_DOWNLOAD_DELAY)) :
						null);
		wrapper.setMaxBrowsingLoadTime(
				kpiSummaryIndexMap.get(ReportConstants.MAX_UNDERSCORE + ReportConstants.WEB_DOWNLOAD_DELAY)
						!= null ?
						NVLayer3Utils.getDoubleFromCsv(summaryDataArray, kpiSummaryIndexMap.get(
								ReportConstants.MAX_UNDERSCORE + ReportConstants.WEB_DOWNLOAD_DELAY)) :
						null);
		wrapper.setAvgBrowsingLoadTime(kpiSummaryIndexMap.get(ReportConstants.AVG_UNDERSCORE+ReportConstants.WEB_DOWNLOAD_DELAY) != null ?
				NVLayer3Utils.getDoubleFromCsv(summaryDataArray,
						kpiSummaryIndexMap.get(ReportConstants.AVG_UNDERSCORE+ReportConstants.WEB_DOWNLOAD_DELAY)) :
				null);
		wrapper.setAvgBufferingTime(kpiSummaryIndexMap.get(ReportConstants.AVG_UNDERSCORE+ReportConstants.YOUTUBE_BUFFER_TIME) != null ?
				NVLayer3Utils.getDoubleFromCsv(summaryDataArray,
						kpiSummaryIndexMap.get(ReportConstants.AVG_UNDERSCORE+ReportConstants.YOUTUBE_BUFFER_TIME)) :
				null);

		//Set Volte KPI's

		Double volteMoAttempts = kpiSummaryIndexMap.get(ReportConstants.SUM_UNDERSCORE+ReportConstants.MO_CALL_ATTEMPT) != null ? NVLayer3Utils.getDoubleFromCsv(summaryDataArray,
				kpiSummaryIndexMap.get(ReportConstants.SUM_UNDERSCORE+ReportConstants.MO_CALL_ATTEMPT)) : null;
		Double volteMoSuccess = kpiSummaryIndexMap.get(ReportConstants.SUM_UNDERSCORE+ReportConstants.MO_CALL_SUCCESS) != null ? NVLayer3Utils.getDoubleFromCsv(summaryDataArray,
				kpiSummaryIndexMap.get(ReportConstants.SUM_UNDERSCORE+ReportConstants.MO_CALL_SUCCESS)) : null;
		Double volteMtAttempts = kpiSummaryIndexMap.get(ReportConstants.SUM_UNDERSCORE+ReportConstants.MT_CALL_ATTEMPT) != null ? NVLayer3Utils.getDoubleFromCsv(summaryDataArray,
				kpiSummaryIndexMap.get(ReportConstants.SUM_UNDERSCORE+ReportConstants.MT_CALL_ATTEMPT)) : null;
		Double volteMtSuccess = kpiSummaryIndexMap.get(ReportConstants.SUM_UNDERSCORE+ReportConstants.MT_CALL_SUCCESS) != null ? NVLayer3Utils.getDoubleFromCsv(summaryDataArray,
				kpiSummaryIndexMap.get(ReportConstants.SUM_UNDERSCORE+ReportConstants.MT_CALL_SUCCESS)) : null;

		wrapper.setJitter(kpiSummaryIndexMap.get(ReportConstants.AVG_UNDERSCORE+ReportConstants.VOLTE_JITTER) != null ?
				NVLayer3Utils.getDoubleFromCsv(summaryDataArray, kpiSummaryIndexMap.get(ReportConstants.AVG_UNDERSCORE+ReportConstants.VOLTE_JITTER)) : null);
		wrapper.setVolteRTPPktLossRate(kpiSummaryIndexMap.get(ReportConstants.AVG_UNDERSCORE+ReportConstants.VOLTE_PACKET_LOSS) != null ?
				NVLayer3Utils.getDoubleFromCsv(summaryDataArray, kpiSummaryIndexMap.get(ReportConstants.AVG_UNDERSCORE+ReportConstants.VOLTE_PACKET_LOSS)) : null);
	
		if (kpiSummaryIndexMap.get(ReportConstants.AVG_UNDERSCORE + ReportConstants.IMS_REGISTRATION_TIME) != null) {
			Double value = NVLayer3Utils.getDoubleFromCsv(summaryDataArray,
					kpiSummaryIndexMap.get(ReportConstants.AVG_UNDERSCORE + ReportConstants.IMS_REGISTRATION_TIME));
			if (value != null) {
				wrapper.setiMSRegistration(ReportUtil.parseToFixedDecimalPlace(value / ReportConstants.THOUSNAND,
						ReportConstants.INDEX_TWO));
			}
		}
		wrapper.setMoSetupSuccessRate(ReportUtil.getPercentage(volteMoSuccess, volteMoAttempts));
		wrapper.setMtSetupSuccessRate(ReportUtil.getPercentage(volteMtSuccess, volteMtAttempts));

		logger.info("wrapper ===={}",wrapper);
		stationrySummaryDataList.add(wrapper);

	}

	private HashMap<String, Object> getDriveImagesForReport(Map<String, Integer> kpiIndexMapForPlots, boolean isHandoverPlot,
			List<String[]> driveData, Map<String, Integer> allKpiIndexMap) throws IOException {
		List<LegendWrapper> legendList = legendRangeDao.findAllLegendRangesAppliedTo(ReportConstants.SSVT_REPORT);
		List<KPIWrapper> kpiList = ReportUtil.convertLegendsListToKpiWrapperList(legendList, kpiIndexMapForPlots);
		List<SiteInformationWrapper> siteInfoList = getSiteInfoByReportType(driveData, allKpiIndexMap);
		List<DriveDataWrapper> imageDataList = new ArrayList<>();
		return getImageMapOfCombinedData(driveData, siteInfoList, imageDataList, kpiList,
				isHandoverPlot, allKpiIndexMap);
	}

	@SuppressWarnings("unchecked")
	private List<SiteInformationWrapper> getSiteInfoByReportType(List<String[]> arList, Map<String, Integer> indexMap) {
		try {
			Integer indexLatitude = indexMap.get(ReportConstants.LATITUDE);
			Integer indexLongitude = indexMap.get(ReportConstants.LONGITUDE);
			if (indexLatitude != null && indexLongitude != null) {
				Corner corner = ReportUtil.getminmaxLatlOnDriveList(arList, indexLatitude, indexLongitude);
				Map<String, Double> viewportMap = ReportUtil.getViewPortMap(corner);
				return siteDetailService.getMacroSiteDetailsForCellLevelForReport(viewportMap, null, null, null, false,
						true, true);
			}
		} catch (DaoException e) {
			logger.error("Error inside the method getSiteInfoByReportType{}", Utils.getStackTrace(e));
		}
		return Collections.emptyList();
	}

	private HashMap<String, Object> getImageMapOfCombinedData(List<String[]> csvDataArray,
			List<SiteInformationWrapper> siteInfoList, List<DriveDataWrapper> imageDataList, List<KPIWrapper> kpiList,
			boolean isHandoverPlot, Map<String, Integer> indexMap) throws IOException {
		Integer indexLatitude = indexMap.get(ReportConstants.LATITUDE);
		Integer indexLongitude = indexMap.get(ReportConstants.LONGITUDE);
		Integer indexPci = indexMap.get(ReportConstants.PCI_PLOT);
		if (indexLatitude != null && indexLongitude != null) {
			DriveImageWrapper driveImageWrapper = new DriveImageWrapper(csvDataArray, indexLatitude, indexLongitude,
					indexPci, kpiList, siteInfoList);
			return prepareImageMap(
					getImagesForReport(kpiList, driveImageWrapper, imageDataList, indexMap), isHandoverPlot, indexMap);
		}
		return new HashMap<>();
	}

	private HashMap<String, String> getImagesForReport(List<KPIWrapper> kpiList, DriveImageWrapper driveImageWrapper,
			List<DriveDataWrapper> imageDataList, Map<String, Integer> indexMap) throws IOException {
		List<Double[]> pinLatLonList = imageDataList.stream()
													.map(driveDataWrapper -> new Double[] {
															driveDataWrapper.getLongitude(),
															driveDataWrapper.getLatitutde() })
													.collect(Collectors.toList());

		kpiList = reportService.modifyIndexOfCustomKpis(kpiList);
		HashMap<String, BufferedImage> bufferdImageMap = mapImageService.getLegendImagesForReport(kpiList,
				driveImageWrapper.getDataKPIs(), indexMap.get(ReportConstants.TEST_TYPE));
		bufferdImageMap.putAll(mapImageService.getDriveImagesForReport(driveImageWrapper, pinLatLonList, indexMap));
		String saveImagePath = (ConfigUtils.getString(ReportConstants.IMAGE_PATH_FOR_NV_REPORT) + ReportConstants.SSVT
				+ ReportConstants.FORWARD_SLASH + ReportUtil.getFormattedDate(new Date(),
				ReportConstants.DATE_FORMAT_DD_MM_YY_HH_SS));
		HashMap<String, String> imagemap = mapImageService.saveDriveImages(bufferdImageMap, saveImagePath, false);
		imagemap.put(ForesightConstants.TO_DELETED_KEY, saveImagePath);
				junkFiles.add(saveImagePath);
		logger.info("Returning images Map with Data: {}", new Gson().toJson(imagemap));
		return imagemap;
	}

	private HashMap<String, Object> prepareImageMap(HashMap<String, String> imagemap, boolean isHandoverPlot,
			Map<String, Integer> indexMap) {
		logger.info("inside the method prepareImageMap");
		Map<String, Object> map = new HashMap<>();
		try {
			if (!isHandoverPlot) {
				map.put(ReportConstants.IMAGE_RSRP,
						imagemap.get(indexMap.get(ReportConstants.RSRP) + Symbol.EMPTY_STRING));
				map.put(ReportConstants.IMAGE_RSRP_LEGEND, imagemap.get(
						ReportConstants.LEGEND + ReportConstants.UNDERSCORE + indexMap.get(ReportConstants.RSRP)));
				map.put(ReportConstants.IMAGE_RSRQ,
						imagemap.get(indexMap.get(ReportConstants.RSRQ) + Symbol.EMPTY_STRING));
				map.put(ReportConstants.IMAGE_RSRQ_LEGEND, imagemap.get(
						ReportConstants.LEGEND + ReportConstants.UNDERSCORE + indexMap.get(ReportConstants.RSRQ)));
				map.put(ReportConstants.IMAGE_SINR,
						imagemap.get(indexMap.get(ReportConstants.SINR) + Symbol.EMPTY_STRING));
				map.put(ReportConstants.IMAGE_SINR_LEGEND, imagemap.get(
						ReportConstants.LEGEND + ReportConstants.UNDERSCORE + indexMap.get(ReportConstants.SINR)));
				map.put(ReportConstants.IMAGE_DL,
						imagemap.get(indexMap.get(ReportConstants.PDSCH_THROUGHPUT) + Symbol.EMPTY_STRING));
				map.put(ReportConstants.IMAGE_DL_LEGEND, imagemap.get(
						ReportConstants.LEGEND + ReportConstants.UNDERSCORE + indexMap.get(
								ReportConstants.PDSCH_THROUGHPUT)));
				map.put(ReportConstants.IMAGE_PCI,
						imagemap.get(indexMap.get(ReportConstants.PCI_PLOT) + Symbol.EMPTY_STRING));
				map.put(ReportConstants.IMAGE_PCI_LEGEND, imagemap.get(ReportConstants.PCI_LEGEND));
				map.put(NVReportConstants.IMAGE_VOLTE_JITTER,
						imagemap.get(indexMap.get(ReportConstants.VOLTE_JITTER) + Symbol.EMPTY_STRING));
				map.put(NVReportConstants.IMAGE_VOLTE_JITTER_LEGEND, imagemap.get(
						ReportConstants.LEGEND + ReportConstants.UNDERSCORE + indexMap.get(
								ReportConstants.VOLTE_JITTER)));
				map.put(NVReportConstants.IMAGE_VOLTE_PACKET_LOSS,
						imagemap.get(indexMap.get(ReportConstants.VOLTE_PACKET_LOSS) + Symbol.EMPTY_STRING));
				map.put(NVReportConstants.IMAGE_VOLTE_PACKET_LOSS_LEGEND, imagemap.get(
						ReportConstants.LEGEND + ReportConstants.UNDERSCORE + indexMap.get(
								ReportConstants.VOLTE_PACKET_LOSS)));
				map.put(NVReportConstants.IMAGE_VOLTE_MOS,
						imagemap.get(indexMap.get(ReportConstants.POLQA_MOS) + Symbol.EMPTY_STRING));
				map.put(NVReportConstants.IMAGE_VOLTE_MOS_LEGEND, imagemap.get(
						ReportConstants.LEGEND + ReportConstants.UNDERSCORE + indexMap.get(ReportConstants.POLQA_MOS)));
				map.put(ReportConstants.IMAGE_MOS,
						imagemap.get(indexMap.get(ReportConstants.MOS) + Symbol.EMPTY_STRING));
				map.put(ReportConstants.IMAGE_MOS_LEGEND, imagemap.get(
						ReportConstants.LEGEND + ReportConstants.UNDERSCORE + indexMap.get(ReportConstants.MOS)));
			} else {
				map.put(ReportConstants.HANDOVER_PLOT,
						imagemap.get(indexMap.get(ReportConstants.HANDOVER_PLOT) + Symbol.EMPTY_STRING));
				map.put(ReportConstants.RESELECTION_PLOT,
						imagemap.get(indexMap.get(ReportConstants.RESELECTION_PLOT) + Symbol.EMPTY_STRING));
			}
		} catch (Exception e) {
			logger.error("Error inside the method prepareImageMap{}", e.getMessage());
		}
		logger.info("image map is {}", new Gson().toJson(map));

		return (HashMap<String, Object>) map;
	}

	private String proceedToCreateReport(MasterReportMainWrapper mainWrapper, GenericWorkorder workorderObj,
			Map<String, Object> jsonMap, Map<String, Object> imageMap) {
		logger.info("Going to create OPEN DRIVE Report");
		String reportAssetRepo = ConfigUtils.getString(NVReportConstants.NV_OPEN_DRIVE_REPORT_JASPER_PATH);
		List<MasterReportMainWrapper> dataSourceList = new ArrayList<>();
		dataSourceList.add(mainWrapper);
		JRBeanCollectionDataSource rfbeanColDataSource = new JRBeanCollectionDataSource(dataSourceList);
		imageMap.put(ReportConstants.SUBREPORT_DIR, reportAssetRepo);
		imageMap.put(ReportConstants.LOGO_CLIENT_KEY, reportAssetRepo + ReportConstants.LOGO_CLIENT_IMG);
		imageMap.put(NVReportConstants.PARAM_KEY_NV_LOGO, reportAssetRepo + ReportConstants.LOGO_NV_IMG);

		logger.info("Found Parameter map: {}", new Gson().toJson(imageMap));

		String filePath = ConfigUtils.getString(NVReportConstants.OPEN_DRIVE_REPORT_PATH);
		String fileName = ReportUtil.getFileName(workorderObj.getWorkorderId(),
				(Integer) jsonMap.get(ReportConstants.ANALYTICS_REPOSITORY_ID), filePath);
		try {
			logger.info("Going to save report on path {}", fileName);
			//			JasperRunManager.runReportToPdfFile(reportAssetRepo + ReportConstants.MAIN_JASPER, fileName, imageMap,
			//					rfbeanColDataSource);
			fileName = fileName.replace(".pdf", ".docx");
			ReportUtil.fillDataInDocxExporter(imageMap, reportAssetRepo + ReportConstants.MAIN_JASPER,
					rfbeanColDataSource, fileName);
			return fileName;
		} catch (JRException e) {
			logger.info("Exception while processing Jasper on path {} trace ==> {}", reportAssetRepo,
					Utils.getStackTrace(e));
		}
		return null;
	}

	private Response saveFileToHdfsAndUpdateStatus(AnalyticsRepository analyticObj, String filePath) {
		String hdfsFilePath = ConfigUtils.getString(ReportConstants.NV_REPORTS_PATH_HDFS) + NVReportConstants.OPEN_DRIVE
				+ ReportConstants.FORWARD_SLASH;
		Response responseReturn = reportService.saveFileAndUpdateStatus(analyticObj.getId(), hdfsFilePath, null,
				new File(filePath), ReportUtil.getFileNameFromFilePath(filePath),NVWorkorderConstant.REPORT_INSTACE_ID);
					ReportUtil.deleteAllFilesFromDirectory(ConfigUtils.getString(NVReportConstants.OPEN_DRIVE_REPORT_PATH));
		return responseReturn;
	}

	private void setHandoverDataToMainWrapper(MasterReportMainWrapper mainWrapper, Integer workorderId,
			List<String> recipeList, List<String> operatorList, Map<String, List<String[]>> testTypeWiseDriveData,
			Map<String, Integer> indexMap) {
		try {
			Map<String, Integer> kpiIndexMap = new HashMap<>();
			kpiIndexMap.put(ReportConstants.HANDOVER_PLOT, indexMap.get(ReportConstants.HANDOVER_PLOT));
			kpiIndexMap.put(ReportConstants.RESELECTION_PLOT, indexMap.get(ReportConstants.RESELECTION_PLOT));
			HashMap<String, Object> imagesMap = getDriveImagesForReport(kpiIndexMap, true,
					testTypeWiseDriveData.get(ReportConstants.RECIPE_CATEGORY_DRIVE), indexMap);
			setHandoverDataToWrapper(mainWrapper, imagesMap,
					getHandoverDataFromHbase(workorderId, recipeList, operatorList));
		} catch (IOException e) {
			logger.error("Exception  int setHandoverDataToMainWrapper {}",Utils.getStackTrace(e));
		}
	}

	private void setHandoverDataToWrapper(MasterReportMainWrapper mainWrapper, HashMap<String, Object> imagesMap,
			List<String[]> csvDataArray) {
		HandoverDataWrapper hoDataWrapper = new HandoverDataWrapper();
		if (SSVTReportUtils.validateMap(imagesMap, ReportConstants.HANDOVER_PLOT)) {
			hoDataWrapper.setHandoverPlot((String) imagesMap.get(ReportConstants.HANDOVER_PLOT));
			hoDataWrapper.setHoInitateLegend(SSVTReportUtils.getLegendColorImage(Color.BLUE));
			hoDataWrapper.setHoSuccessLegend(SSVTReportUtils.getLegendColorImage(Color.GREEN));
			hoDataWrapper.setHoFailureLegend(SSVTReportUtils.getLegendColorImage(Color.RED));
		}
		setHandoverDetailDataToHOWrapper(hoDataWrapper, csvDataArray);
		setReselectionDataToWrapper(mainWrapper, imagesMap);
		mainWrapper.setHandoverDataList(Arrays.asList(hoDataWrapper));
	}

	private void setReselectionDataToWrapper(MasterReportMainWrapper mainWrapper, HashMap<String, Object> imagesMap) {
		MasterPlotDataWrapper reselectionDataWrapper = new MasterPlotDataWrapper();
		reselectionDataWrapper.setMapPlot((String) imagesMap.get(ReportConstants.RESELECTION_PLOT));
		reselectionDataWrapper.setPlotTitle("Reselection Analysis");
		mainWrapper.setReselectionDataList(Arrays.asList(reselectionDataWrapper));
	}

	private void setHandoverDetailDataToHOWrapper(HandoverDataWrapper hoDataWrapper, List<String[]> csvDataArray) {
		logger.info("Inside method setHandoverDetailDataToHOWrapper, hoDataWrapper: {}", hoDataWrapper);
		List<HOItemWrapper> hoDataList = new ArrayList<>();

		Integer hoInitiated = 0;
		Integer hoSucceeded = 0;
		Integer hoFailed = 0;
		for (String[] csvData : csvDataArray) {
			if (csvData != null && csvData.length > SSVTConstants.INDEX_HO_DATA_STATUS) {
				HOItemWrapper itemWrapper = new HOItemWrapper();
				itemWrapper.setTimestamp(ReportUtil.getFormattedDate(
						new Date(Long.parseLong(csvData[SSVTConstants.INDEX_HO_DATA_TIMESTAMP])),
						ReportConstants.DATE_FORMAT_DD_MM_YY_SS_AA));
				itemWrapper.setLatitude(csvData[SSVTConstants.INDEX_HO_DATA_LATITUDE]);
				itemWrapper.setLongitude(csvData[SSVTConstants.INDEX_HO_DATA_LONGITUDE]);
				itemWrapper.setInitRsrp(csvData[SSVTConstants.INDEX_HO_DATA_INIT_RSRP]);
				itemWrapper.setSourcePci(csvData[SSVTConstants.INDEX_HO_DATA_SOURCE_PCI]);
				itemWrapper.setTargetPCI(csvData[SSVTConstants.INDEX_HO_DATA_TARGET_PCI]);
				itemWrapper.setCompleteRsrp(csvData[SSVTConstants.INDEX_HO_DATA_COMPLETE_RSRP]);
				itemWrapper.setInterruptionTime(csvData[SSVTConstants.INDEX_HO_DATA_INTERRUPTION_TIME]);
				hoInitiated++;
				if (!StringUtils.isBlank(csvData[SSVTConstants.INDEX_HO_DATA_STATUS])
						&& SSVTConstants.TEST_STATUS_PASS.equalsIgnoreCase(
						csvData[SSVTConstants.INDEX_HO_DATA_STATUS])) {
					itemWrapper.setTestStatus(SSVTConstants.TEST_STATUS_PASS);
					hoSucceeded++;
				} else {
					itemWrapper.setTestStatus(SSVTConstants.TEST_STATUS_FAIL);
					hoFailed++;
				}
				hoDataList.add(itemWrapper);
			}
		}
		hoDataWrapper.setHoInitate(String.valueOf(hoInitiated));
		hoDataWrapper.setHoSuccess(String.valueOf(hoSucceeded));
		hoDataWrapper.setHoFailure(String.valueOf(hoFailed));
		hoDataWrapper.setHoSuccessRate(String.valueOf(
				ReportUtil.round(ReportUtil.getPercentage(hoSucceeded, hoInitiated),
						ReportConstants.TWO_DECIMAL_PLACES)));
		hoDataWrapper.setHoFailureRate(String.valueOf(
				ReportUtil.round(ReportUtil.getPercentage(hoFailed, hoInitiated), ReportConstants.TWO_DECIMAL_PLACES)));
		hoDataWrapper.setHoAnalysisList(hoDataList);
	}

	private List<String[]> getHandoverDataFromHbase(Integer workorderId, List<String> recipeList,
			List<String> operatorList) throws IOException {
		logger.info(
				"Inside Method getHandoverDataFromHbase with data=> workorderid: {}, recipeList: {}, operatorList: {}",
				workorderId, recipeList, operatorList);
		Set<String> operatorSet = new HashSet<>(operatorList);
		String table = ConfigUtils.getString(NVLayer3Constants.LAYER3_REPORT_TABLE);
		List<Get> getList = SSVTReportUtils.getHandoverQueryList(workorderId, recipeList, new ArrayList<>(operatorSet));
		List<HBaseResult> resultList = nvLayer3HbaseDao.getQMDLDataFromHBase(getList, table);
		return SSVTReportUtils.getHandoverDataListFromHBaseResult(resultList);
	}

}

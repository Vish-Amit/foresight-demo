package com.inn.foresight.module.nv.report.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.core.Response;

import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.inn.commons.Symbol;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.lang.StringUtils;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.report.dao.IAnalyticsRepositoryDao;
import com.inn.foresight.core.report.model.AnalyticsRepository.progress;
import com.inn.foresight.module.nv.NVConstant;
import com.inn.foresight.module.nv.core.workorder.dao.impl.IGenericWorkorderDao;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;
import com.inn.foresight.module.nv.layer3.constants.NVLayer3Constants;
import com.inn.foresight.module.nv.layer3.service.INVLayer3DashboardService;
import com.inn.foresight.module.nv.report.RangeSlab;
import com.inn.foresight.module.nv.report.constant.ReportIndexWrapper;
import com.inn.foresight.module.nv.report.constant.ReportSummaryIndexWrapper;
import com.inn.foresight.module.nv.report.dao.INVReportHdfsDao;
import com.inn.foresight.module.nv.report.service.IBenchmarkService;
import com.inn.foresight.module.nv.report.service.IMapImagesService;
import com.inn.foresight.module.nv.report.service.IReportService;
import com.inn.foresight.module.nv.report.utils.DriveHeaderConstants;
import com.inn.foresight.module.nv.report.utils.LegendUtil;
import com.inn.foresight.module.nv.report.utils.ReportConstants;
import com.inn.foresight.module.nv.report.utils.ReportUtil;
import com.inn.foresight.module.nv.report.workorder.wrapper.DriveImageWrapper;
import com.inn.foresight.module.nv.report.workorder.wrapper.KPIDataWrapper;
import com.inn.foresight.module.nv.report.workorder.wrapper.KPIWrapper;
import com.inn.foresight.module.nv.report.wrapper.KPISummaryDataWrapper;
import com.inn.foresight.module.nv.report.wrapper.SiteInformationWrapper;
import com.inn.foresight.module.nv.report.wrapper.benchmark.BenchMarkComparison;
import com.inn.foresight.module.nv.report.wrapper.benchmark.BenchMarkOperatorInfo;
import com.inn.foresight.module.nv.report.wrapper.benchmark.BenchMarkReportWrapper;
import com.inn.foresight.module.nv.report.wrapper.benchmark.BenchMarksubwrapper;
import com.inn.foresight.module.nv.report.wrapper.benchmark.KpiRankWrapper;
import com.inn.foresight.module.nv.report.wrapper.benchmark.LegendListWrapper;
import com.inn.foresight.module.nv.report.wrapper.benchmark.VoiceStatsWrapper;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;
import com.inn.foresight.module.nv.workorder.recipe.wrapper.RecipeWrapper;
import com.inn.product.legends.dao.ILegendRangeDao;
import com.inn.product.legends.utils.LegendWrapper;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRPptxExporter;

@Service("BenchmarkServiceImpl")
public class BenchmarkServiceImpl implements IBenchmarkService{

	/** The logger. */
	private Logger logger = LogManager.getLogger(BenchmarkServiceImpl.class);

	/** The mapper. */
	ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private INVReportHdfsDao nvReportHdfsDao;
	@Autowired
	private IGenericWorkorderDao iGenericWorkorderDao;
	@Autowired
	private IAnalyticsRepositoryDao analyticsrepositoryDao ;
	@Autowired
	private ILegendRangeDao legendRangeDao;
	@Autowired
	private INVLayer3DashboardService nvLayer3DashboardService;
	@Autowired
	private IReportService reportService;
	@Autowired
	private IMapImagesService mapImagesService;
//
//	@SuppressWarnings("unchecked")
//	private static Map<Integer, String> indexKpiMap = MapUtils.invertMap(DriveHeaderConstants.getKpiIndexMap());

	@Override
	public Response execute(String json)
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		logger.info("Going to execute method for json {} ", json);
		String filePath = ConfigUtils.getString(ReportConstants.NV_REPORTS_PATH) + ReportConstants.BENCHMARK
				+ ReportConstants.FORWARD_SLASH;
		Map<String, Object> jsonMap = reportService.getJsonDataMap(json);
		Integer analyticsRespositoryId = null;
		List<String> dynamicKpis = ReportIndexWrapper.getLiveDriveKPIs();
		List<String> fetchKPIList = ReportIndexWrapper.getLiveDriveKPIs().stream().filter(k -> dynamicKpis.contains(k))
				.collect(Collectors.toList());
		Map<String, Integer> kpiIndexMap = ReportIndexWrapper.getLiveDriveKPIIndexMap(fetchKPIList);

		try {
			List<Integer> workOrderIds = getListOfWorkOrderIds(jsonMap);
			analyticsRespositoryId = (Integer) jsonMap.get(ReportConstants.ANALYTICS_REPOSITORY_ID);
			String assignTo = (String) jsonMap.get(NVLayer3Constants.ASSIGN_TO_JSON_KEY);
			logger.info("analyticsRespositoryId {} ,workOrderIds {} , assignTo {} ", analyticsRespositoryId,
					workOrderIds, assignTo);
			if (reportService.getFileProcessedStatusForWorkorders(workOrderIds)) {
				logger.debug("check status ===> {} ", workOrderIds.get(ReportConstants.INDEX_ZER0));
				GenericWorkorder genericWorkorder = iGenericWorkorderDao
						.findByPk(workOrderIds.get(ReportConstants.INDEX_ZER0));
				Map<String, List<KPIDataWrapper>> kpiDataWrapperMap = new HashMap<>();
				List<LegendWrapper> legendList = legendRangeDao.findAllLegendRangesAppliedTo(ReportConstants.BENCHMARK);
				List<KPIWrapper> kpiWrapperList = ReportUtil.convertLegendsListToKpiWrapperList(legendList,
						kpiIndexMap);
				Map<String, List<String>> recipeOperatorListMap = nvLayer3DashboardService
						.getDriveRecipeDetail(workOrderIds);
				if (recipeOperatorListMap != null) {
					return generateBenchmarkReport(filePath, analyticsRespositoryId, workOrderIds, genericWorkorder,
							kpiDataWrapperMap, kpiWrapperList, recipeOperatorListMap, fetchKPIList, kpiIndexMap);
				} else {
					analyticsrepositoryDao.updateStatusInAnalyticsRepository(analyticsRespositoryId, null, null,
							progress.Failed, null);
				}
			}
		} catch (IOException | ForbiddenException fe) {
			analyticsrepositoryDao.updateStatusInAnalyticsRepository(analyticsRespositoryId, null, fe.getMessage(),
					progress.Failed, null);
		} catch (Exception e) {
			logger.error("Unable to Genearate the report for workOrder Id {} , reason is {} ",
					jsonMap.get(ReportConstants.WORKORDER_ID), Utils.getStackTrace(e));
		}
		return Response.ok(ForesightConstants.FAILURE_JSON).build();
	}

	private Response generateBenchmarkReport(String filePath, Integer analyticsRespositoryId,
			List<Integer> workOrderIds, GenericWorkorder genericWorkorder,
			Map<String, List<KPIDataWrapper>> kpiDataWrapperMap, List<KPIWrapper> kpiWrapperList,
			Map<String, List<String>> recipeOperatorListMap, List<String> fetchKPIList, Map<String, Integer> kpiIndexMap) throws IOException, IllegalAccessException, NoSuchFieldException, SecurityException {
		Map<String, List<KPIWrapper>> operatorWiseStatMap = getOperatorWiseKpiStatsMap(workOrderIds,kpiIndexMap);
		Map<String, List<String[]>> operatorWiseDriveDataMap = getOperatorWiseDriveData(workOrderIds,fetchKPIList);
		List<VoiceStatsWrapper> voiceStatsList = new ArrayList<>();
		Map<String, Map<String, String>> operatorWisekpiImageMap = getOperatorWiseKPiImagesMap(
				operatorWiseStatMap, operatorWiseDriveDataMap,kpiIndexMap);
//		logger.info("image map with kpiname {}",operatorWisekpiImageMap);
		Map<String, List<KPISummaryDataWrapper>> operatorWiseSummaryMap = getOperatorWiseSummaryMap(
				workOrderIds, operatorWiseStatMap, operatorWiseDriveDataMap, kpiDataWrapperMap, kpiWrapperList,
				voiceStatsList);
		logger.info("Voice Stats List Calculated {} ", new Gson().toJson(voiceStatsList));
		Map<String, HashMap<String, BufferedImage>> operatorWiseLegendImageMap = getOperatorWiseLegendImageMap(
				operatorWiseStatMap, operatorWiseDriveDataMap,kpiIndexMap);
		
		ArrayList<BenchMarkReportWrapper> benchmarkReportWrapperList = getBenchMarkDataList(
				genericWorkorder, kpiDataWrapperMap, operatorWisekpiImageMap, operatorWiseSummaryMap,
				operatorWiseLegendImageMap, voiceStatsList,operatorWiseDriveDataMap, operatorWiseStatMap,kpiIndexMap);
		File file = createBenchMarkReport(benchmarkReportWrapperList, analyticsRespositoryId, filePath);
//		logger.info("\n\noperatorWiseSummaryMap : {} ", operatorWiseSummaryMap);
		String hdfsFilePath = ConfigUtils.getString(ReportConstants.NV_REPORTS_PATH_HDFS) + ReportConstants.MASTER + ReportConstants.FORWARD_SLASH;
		return saveFileAndUpdateStatus(analyticsRespositoryId, hdfsFilePath, genericWorkorder, file,workOrderIds);
	}

	private Double getTotalDistance(Map<String, List<KPISummaryDataWrapper>> operatorWiseSummaryMap) {
		for (Entry<String, List<KPISummaryDataWrapper>> summaryMap : operatorWiseSummaryMap.entrySet()) {
			try {
//					logger.info("VALUES DATA {} ",summaryMap	.getValue());
				if(summaryMap != null && Utils.isValidList(summaryMap.getValue())) {
					List<KPISummaryDataWrapper> list = summaryMap.getValue()
																 .stream()
																 .filter(wrapper -> wrapper.getTestName()
																						   .equalsIgnoreCase(
																								   ReportConstants.TOTAL_DISTANCE))
																 .collect(Collectors.toList());
					//					logger.info("list Data {} ",list);
					if (list != null && list.size() > ReportConstants.INDEX_ZER0 && NumberUtils.isCreatable(
							list.get(ReportConstants.INDEX_ZER0).getAchived())) {
						return Double.parseDouble(list.get(ReportConstants.INDEX_ZER0).getAchived());
					}
				}
			} catch (Exception e) {
				logger.info("Unable to find the distane from wrapper data {} ", e.getMessage());
			}
		}
		return null;
	}

	private Map<String,String> getSitesLocationImage(Map<String, List<String[]>> operatorWiseDriveDataMap, Map<String, Integer> kpiIndexMap) {
		for (Entry<String, List<String[]>> driveDataMap : operatorWiseDriveDataMap.entrySet()) {
			try {
				if(driveDataMap.getKey()!= null && !driveDataMap.getKey().isEmpty()) {
					List<SiteInformationWrapper> siteDataList = reportService.getSiteDataForReportByDataList(
							operatorWiseDriveDataMap.get(driveDataMap.getKey()),kpiIndexMap);
					if (siteDataList != null && siteDataList.size() > ReportConstants.INDEX_ZER0) {
						logger.info("siteDataList Size {} , statues {}  ",siteDataList.size(),siteDataList.stream().map(SiteInformationWrapper::getOperationalStatus).collect(Collectors.toList()));
						siteDataList =siteDataList.stream().filter(wrapper->(wrapper.getOperationalStatus()!=null && "null".equals(wrapper.getOperationalStatus()))).collect(Collectors.toList());
						DriveImageWrapper driveImageWrapper = new DriveImageWrapper(driveDataMap.getValue(),
								kpiIndexMap.get(ReportConstants.LATITUDE),kpiIndexMap.get(ReportConstants.LONGITUDE),
								kpiIndexMap.get(ReportConstants.PCI_PLOT), null, siteDataList, null);
						HashMap<String, BufferedImage> siteBufferedImageMap = mapImagesService.getSitesLocationImage(
								driveImageWrapper);
						Map<String, List<SiteInformationWrapper>> statusWiseSiteMap = siteDataList.stream().collect(
								Collectors.groupingBy(SiteInformationWrapper::getOperationalStatus));
						siteBufferedImageMap.put(ReportConstants.SITE_DATABASE_LEGEND_IMG, LegendUtil.getSiteDatabaseCustomImage(siteDataList.size(), statusWiseSiteMap, "Site Database Status"));
						String imagePath = ConfigUtils.getString(ReportConstants.IMAGE_PATH_FOR_NV_REPORT)
								+ ReportConstants.BENCHMARK + ReportConstants.FORWARD_SLASH
								+ ReportUtil.getFormattedDate(new Date(), ReportConstants.DATE_FORMAT_DD_MM_YY_HH_SS)
								+ ReportConstants.FORWARD_SLASH;
						return mapImagesService.saveDriveImages(siteBufferedImageMap,imagePath, false);
					}
					return null;
				}
			} catch (Exception e) {
				logger.debug("Inside method getSitesLocationImage {} , {}  ",e.getMessage(),Utils.getStackTrace(e));
			}
		}
		return null;
	}
	
	

	private InputStream getDriveRouteImageForReport(Map<String, List<String[]>> operatorWiseDriveDataMap, Map<String, Integer> kpiIndexMap) {
		try {
			logger.debug("[DriveImage] inside method getDriveRouteImageForReport");
			List<String[]> driveDataList = getDriveDataListFromDriveDataMap(operatorWiseDriveDataMap);
			logger.debug("[DriveImage] driveDataList size: {}", driveDataList.size());

			DriveImageWrapper driveImageWrapper = new DriveImageWrapper(driveDataList, kpiIndexMap.get(ReportConstants.LATITUDE),
					kpiIndexMap.get(ReportConstants.LONGITUDE), kpiIndexMap.get(ReportConstants.PCI_PLOT), getKpiWrapperListForDriveRoute(kpiIndexMap),
					reportService.getSiteDataForReportByDataList(driveDataList, kpiIndexMap), null);
			Map<String, BufferedImage> imagesMap = mapImagesService.getDriveImagesForReport(driveImageWrapper, null,kpiIndexMap);
//			logger.debug("[DriveImage] final Image map: {}", imagesMap);
			return ReportUtil.getInputStreamFromBufferedImage(imagesMap.get(kpiIndexMap.get(ReportConstants.RSRP).toString()));

		} catch (Exception e) {
			logger.error("Exception inside method getDriveRouteImageForReport {} ",Utils.getStackTrace(e));
		}

		return null;
	}
	
	private List<String[]> getDriveDataListFromDriveDataMap(Map<String, List<String[]>> operatorWiseDriveDataMap){
		List<String[]> driveDataList = new ArrayList<>();
		for(Entry<String, List<String[]>> operatorDataEntry : operatorWiseDriveDataMap.entrySet()) {
			driveDataList.addAll(operatorDataEntry.getValue());
		}
		return driveDataList;
	}
	
	private List<KPIWrapper> getKpiWrapperListForDriveRoute(Map<String, Integer> kpiIndexMap) {
		List<KPIWrapper> kpiWrapperList = new ArrayList<>();
		KPIWrapper kpiWrapper = new KPIWrapper();
		kpiWrapper.setKpiName(DriveHeaderConstants.RSRP);
		kpiWrapper.setIndexKPI(kpiIndexMap.get(ReportConstants.RSRP));
		kpiWrapperList.add(kpiWrapper);
		return kpiWrapperList;
	}

	private List<Integer> getListOfWorkOrderIds(Map<String, Object> jsonMap) {
		List<Integer> workOrderIds=null;
		Integer workorderId = null;
		if(jsonMap.get(ReportConstants.WORKORDER_ID)!=null){
			workorderId = (Integer) jsonMap.get(ReportConstants.WORKORDER_ID);
		}
		if(workorderId!=null){
			workOrderIds = new ArrayList<>();
			workOrderIds.add(workorderId);
		} else{
			workOrderIds = (List<Integer>) jsonMap.get(ReportConstants.WORKORDER_IDS);
		}
		return workOrderIds;
	}

	private ArrayList<BenchMarkReportWrapper> getBenchMarkDataList(GenericWorkorder genericWorkorder,
			Map<String, List<KPIDataWrapper>> kpiDataWrapperMap,
			Map<String, Map<String, String>> operatorWisekpiImageMap,
			Map<String, List<KPISummaryDataWrapper>> operatorWiseSummaryMap,
			Map<String, HashMap<String, BufferedImage>> operatorWiseLegendImageMap, List<VoiceStatsWrapper> voiceStatsList,
			Map<String, List<String[]>> operatorWiseDriveDataMap, Map<String, List<KPIWrapper>> operatorWiseStatMap, Map<String, Integer> kpiIndexMap) {
		ArrayList<BenchMarkReportWrapper> benchmarkReportWrapperList = new ArrayList<>();
		try {
			BenchMarkReportWrapper wrapper = getBenchMarkReportWrapper(operatorWisekpiImageMap, operatorWiseLegendImageMap,kpiDataWrapperMap,kpiIndexMap);
			logger.debug("VoiceStatsList: {} ", new Gson().toJson(voiceStatsList));
			prepareBenchmarkWrapper(genericWorkorder, operatorWiseSummaryMap, voiceStatsList, operatorWiseDriveDataMap,
					operatorWiseStatMap, wrapper,kpiIndexMap);
			logger.info("getBenchMarkDataList wrapper {}", wrapper);
			benchmarkReportWrapperList.add(wrapper);
		} catch (Exception e) {
			logger.error("Exception  inside method getBenchMarkDataList {} ",Utils.getStackTrace(e));
		}
		return benchmarkReportWrapperList;
	}

	private void prepareBenchmarkWrapper(GenericWorkorder genericWorkorder,
			Map<String, List<KPISummaryDataWrapper>> operatorWiseSummaryMap, List<VoiceStatsWrapper> voiceStatsList,
			Map<String, List<String[]>> operatorWiseDriveDataMap, Map<String, List<KPIWrapper>> operatorWiseStatMap,
			BenchMarkReportWrapper wrapper, Map<String, Integer> kpiIndexMap) {
		wrapper.getSubList().get(ReportConstants.INDEX_ZER0).setBenchmarkComparisonList(getOperatorComparisonListFromMapNew(operatorWiseSummaryMap,operatorWiseDriveDataMap,voiceStatsList,kpiIndexMap));
		wrapper.getSubList().get(ReportConstants.INDEX_ZER0).setVoiceKpiList(voiceStatsList);
		wrapper.getSubList().get(ReportConstants.INDEX_ZER0).setRouteLength(ReportUtil.round(getTotalDistance(operatorWiseSummaryMap), ReportConstants.TWO_DECIMAL_PLACES) + ReportConstants.TOTAL_DISTANCE_UNIT);
		goingToSetTestDate(genericWorkorder, wrapper);
		setLegendDataToBenchmarkWrapper(operatorWiseStatMap, wrapper);
		setSiteDetailDataToBenchmarkWrapper(operatorWiseDriveDataMap, wrapper,kpiIndexMap);
		setRankDataListToBenchmarkWrapper(operatorWiseDriveDataMap, wrapper.getSubList().get(ReportConstants.INDEX_ZER0),kpiIndexMap);
		wrapper.getSubList().get(ReportConstants.INDEX_ZER0).setRouteImage(getDriveRouteImageForReport(operatorWiseDriveDataMap,kpiIndexMap));
	}
	
	private void setSiteDetailDataToBenchmarkWrapper(Map<String, List<String[]>> operatorWiseDriveDataMap,
			BenchMarkReportWrapper wrapper, Map<String, Integer> kpiIndexMap) {
		Map<String, String> siteStatusImgs = getSitesLocationImage(operatorWiseDriveDataMap,kpiIndexMap);
		logger.info("siteImagePath {} ", siteStatusImgs);
		if (siteStatusImgs != null && !siteStatusImgs.isEmpty()
				&& siteStatusImgs.containsKey(ReportConstants.SITE_LOCATION_IMG)) {
			wrapper	.getSubList()
					.get(ReportConstants.INDEX_ZER0)
					.setPlannedSitesImage(siteStatusImgs.get(ReportConstants.SITE_LOCATION_IMG));
			wrapper	.getSubList()
					.get(ReportConstants.INDEX_ZER0)
					.setPlannedSitesLegend(siteStatusImgs.get(ReportConstants.SITE_DATABASE_LEGEND_IMG));
			wrapper.setShowSitesPage(true);
		}
	}

	private void goingToSetTestDate(GenericWorkorder genericWorkorder, BenchMarkReportWrapper wrapper) {
		try {
			if(genericWorkorder.getStartDate()!=null){
				wrapper.setTestDate(ReportUtil.parseDateToString(ReportConstants.DATE_FORMAT_DD_SP_MM_SP_YY,genericWorkorder.getStartDate()));
			}
		} catch (Exception e) {
			logger.error("Exception during setting date {} ",Utils.getStackTrace(e));
			wrapper.setTestDate(ReportUtil.parseDateToString(ReportConstants.DATE_FORMAT_DD_SP_MM_SP_YY,new Date()));
		}
	}
	
	private void setLegendDataToBenchmarkWrapper(Map<String, List<KPIWrapper>> operatorWiseStatMap, BenchMarkReportWrapper wrapper) {
		BenchMarksubwrapper subWrapper = wrapper.getSubList().get(ReportConstants.INDEX_ZER0);
		Map<String, List<KPIWrapper>> kpiWiseStatsMap = getKpiWiseStatMap(operatorWiseStatMap);
		if (!kpiWiseStatsMap.isEmpty()) {
			for (Entry<String, List<KPIWrapper>> kpiWrapperList : kpiWiseStatsMap.entrySet()) {
				List<LegendListWrapper> kpiLegendList = getKpiWiseLegendWrapper(kpiWrapperList, subWrapper);
				setKPILegendToBenchmarkWrapper(kpiWrapperList.getKey(), subWrapper, kpiLegendList);
			}
		}
	}
	
	private void setKPILegendToBenchmarkWrapper(String kpiName, BenchMarksubwrapper subWrapper, List<LegendListWrapper> kpiLegendList){
		switch(kpiName) {
		case DriveHeaderConstants.RSRP:
			Collections.sort(kpiLegendList, getLegendListComparator());
			Collections.reverse(kpiLegendList);
			subWrapper.setRsrpLegendList(kpiLegendList);
			break;
		case DriveHeaderConstants.SINR:
			Collections.sort(kpiLegendList, getLegendListComparator());
			Collections.reverse(kpiLegendList);
			subWrapper.setSinrLegendList(kpiLegendList);
			break;
		case DriveHeaderConstants.DL_THROUGHPUT:
			Collections.sort(kpiLegendList, getLegendListComparator());
			Collections.reverse(kpiLegendList);
			subWrapper.setDlLegendList(kpiLegendList);
			break;
		case DriveHeaderConstants.UL_THROUGHPUT:
			Collections.sort(kpiLegendList, getLegendListComparator());
			Collections.reverse(kpiLegendList);
			subWrapper.setUlLegendList(kpiLegendList);
			break;
		case DriveHeaderConstants.CQI:
			Collections.sort(kpiLegendList, getLegendListComparator());
			Collections.reverse(kpiLegendList);
			subWrapper.setCqiLegendList(kpiLegendList);
			break;
		case ReportConstants.MOS:
			Collections.sort(kpiLegendList, getLegendListComparator());
			Collections.reverse(kpiLegendList);
			subWrapper.setMosLegendList(kpiLegendList);
			break;
		case ReportConstants.BANDWIDTH_DL:
			Collections.sort(kpiLegendList, getLegendListComparator());
			Collections.reverse(kpiLegendList);
			subWrapper.setDlBandwidthLegendList(kpiLegendList);
			break;
		default:
				break;
		}
	}
	
	private Map<String, List<KPIWrapper>> getKpiWiseStatMap(Map<String, List<KPIWrapper>> operatorWiseStatMap) {
		Map<String, List<KPIWrapper>> kpiWiseStatsMap = new HashMap<>();
		for(Entry<String, List<KPIWrapper>> operatorKpiStat : operatorWiseStatMap.entrySet()) {
			for(KPIWrapper kpiWrapper : operatorKpiStat.getValue()) {
				kpiWrapper.setOperatorName(operatorKpiStat.getKey());
				if(kpiWiseStatsMap.containsKey(kpiWrapper.getKpiName())) {
					List<KPIWrapper> kpiWrapperList = kpiWiseStatsMap.get(kpiWrapper.getKpiName());
					kpiWrapperList.add(kpiWrapper);
					kpiWiseStatsMap.replace(kpiWrapper.getKpiName(), kpiWrapperList);
				} else {
					List<KPIWrapper> kpiWrapperList = new ArrayList<>();
					kpiWrapperList.add(kpiWrapper);
					kpiWiseStatsMap.put(kpiWrapper.getKpiName(), kpiWrapperList);
				}
			}
		}
		return kpiWiseStatsMap;
	}
	
	private List<LegendListWrapper> getKpiWiseLegendWrapper(Entry<String, List<KPIWrapper>> kpiWrapperList, BenchMarksubwrapper subWrapper) {
		Map<String, LegendListWrapper> rangeLegendMap = getRangeWiseLegendListWrapperMap(kpiWrapperList.getValue(), subWrapper);
		return new ArrayList<>(rangeLegendMap.values());
	}
	
	private Map<String, LegendListWrapper> getRangeWiseLegendListWrapperMap(List<KPIWrapper> kpiWrapperList, BenchMarksubwrapper subWrapper) {
		Map<String, LegendListWrapper> rangeLegendMap = new HashMap<>();
		int count = 0;
		int noOfOperator = kpiWrapperList.size();
		for (KPIWrapper wrapper : kpiWrapperList) {
			setOperatorToBenchmarkSubWrapper(subWrapper, count, wrapper.getOperatorName());
			List<RangeSlab> sortedRangeSlabList = ReportUtil.getSortedRangeSlabList(wrapper.getRangeSlabs());
			for (RangeSlab rangeSlab : sortedRangeSlabList) {
				String range = rangeSlab.getUpperLimit() + " to " + rangeSlab.getLowerLimit();
				if (rangeLegendMap.containsKey(range)) {
					setOperatorValueToLegendWrapper(count, rangeLegendMap.get(range),
							ReportUtil.getPercentage(rangeSlab.getCount(), wrapper.getTotalCount()));
				} else {
					LegendListWrapper legendListWrapper = new LegendListWrapper();
					legendListWrapper.setLegendValue(range);
					legendListWrapper.setRemark(getLegendRemarkForRangeColor(wrapper.getKpiName(), range));
					legendListWrapper.setColorImage(ReportUtil.getLegendColorImageForBenchmark(rangeSlab.getColorCode()));
					setOperatorValueToLegendWrapper(count, legendListWrapper,
							ReportUtil.round(ReportUtil.getPercentage(rangeSlab.getCount(), wrapper.getTotalCount()), ReportConstants.TWO_DECIMAL_PLACES));
					rangeLegendMap.put(range, legendListWrapper);
				}
			}
			if (count < noOfOperator) {
				count++;
			}
		}
		
		return rangeLegendMap;
	}
	
	private String getLegendRemarkForRangeColor(String kpiName, String range) {
		try {
			switch (kpiName) {
			case DriveHeaderConstants.RSRP:
				Map<String, String> rsrpMap = new ObjectMapper().readValue(ReportConstants.RSRP_LEGEND_REMARK_JSON,
						HashMap.class);
				return rsrpMap.containsKey(range) ? rsrpMap.get(range) : null;
			case DriveHeaderConstants.SINR:
				Map<String, String> sinrMap = new ObjectMapper().readValue(ReportConstants.SINR_LEGEND_REMARK_JSON,
						HashMap.class);
				return sinrMap.containsKey(range) ? sinrMap.get(range) : null;
			case DriveHeaderConstants.DL_THROUGHPUT:
				Map<String, String> dlMap = new ObjectMapper().readValue(ReportConstants.DL_LEGEND_REMARK_JSON,
						HashMap.class);
				return dlMap.containsKey(range) ? dlMap.get(range) : null;
			case DriveHeaderConstants.UL_THROUGHPUT:
				Map<String, String> ulMap = new ObjectMapper().readValue(ReportConstants.UL_LEGEND_REMARK_JSON,
						HashMap.class);
				return ulMap.containsKey(range) ? ulMap.get(range) : null;
				default : return null;
			}
		} catch (Exception e) {
			logger.error("Unable to get remark for kpi: {}, Range: {}, Error: {}", kpiName, range,
					Utils.getStackTrace(e));
		}
		return null;
	}
	
	private void setOperatorToBenchmarkSubWrapper(BenchMarksubwrapper subWrapper, Integer count, String operatorName) {
		if (count != null) {
			switch (count) {
			case ForesightConstants.ZERO:
				subWrapper.setOperator1(operatorName + Symbol.EMPTY_STRING);
				break;
			case ForesightConstants.ONE:
				subWrapper.setOperator2(operatorName + Symbol.EMPTY_STRING);
				break;
			case ForesightConstants.TWO:
				subWrapper.setOperator3(operatorName + Symbol.EMPTY_STRING);
				break;
			case NVConstant.THREE_INT:
				subWrapper.setOperator4(operatorName + Symbol.EMPTY_STRING);
				break;
		    default: 
				break;
			}
		}
	}
	
	private void setOperatorValueToLegendWrapper(Integer count, LegendListWrapper legendWrapper, Double value) {
		if (count != null) {
			switch (count) {
			case ForesightConstants.ZERO:
				legendWrapper.setOperator1Value(value + Symbol.EMPTY_STRING);
				break;
			case ForesightConstants.ONE:
				legendWrapper.setOperator2Value(value + Symbol.EMPTY_STRING);
				break;
			case ForesightConstants.TWO:
				legendWrapper.setOperator3Value(value + Symbol.EMPTY_STRING);
				break;
			case NVConstant.THREE_INT:
				legendWrapper.setOperator4Value(value + Symbol.EMPTY_STRING);
				break;
			default: 
				break;
			}
		}
	}

	private Map<String, HashMap<String, BufferedImage>> getOperatorWiseLegendImageMap(Map<String, List<KPIWrapper>> operatorWiseStatMap, Map<String, List<String[]>> operatorWiseDriveDataMap, Map<String, Integer> kpiIndexMap) {
		Map<String, HashMap<String, BufferedImage>>  operatorWiseLegendImageMap = new HashMap<>();
		for (Entry<String, List<KPIWrapper>> operatorwisestatMap : operatorWiseStatMap.entrySet()) {
			DriveImageWrapper driveImageWrapper = new DriveImageWrapper(operatorWiseDriveDataMap.get(operatorwisestatMap.getKey()), kpiIndexMap.get(ReportConstants.LATITUDE),
					kpiIndexMap.get(ReportConstants.LONGITUDE),kpiIndexMap.get(ReportConstants.PCI_PLOT), operatorwisestatMap.getValue(), null, null);
			operatorWiseLegendImageMap.put(operatorwisestatMap.getKey(), reportService.getLegendImages(operatorwisestatMap.getValue(),driveImageWrapper.getDataKPIs(), DriveHeaderConstants.INDEX_TEST_TYPE));
		}
		logger.info("Going to return the Operator Wise legend Image Map");
		return operatorWiseLegendImageMap;
	}

	private Map<String, List<KPISummaryDataWrapper>> getOperatorWiseSummaryMap(List<Integer> workOrderIds,
			Map<String, List<KPIWrapper>> operatorWiseStatMap,

			Map<String, List<String[]>> operatorWiseDriveDataMap, Map<String, List<KPIDataWrapper>> kpiDataWrapperMap, List<KPIWrapper> kpiWrapperList,
			List<VoiceStatsWrapper> voiceStatsList)
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		logger.info("inside method getOperatorWiseSummaryMap");
		Map<String, List<RecipeWrapper>> recipeoperatormap = nvLayer3DashboardService.getRecipeMappingWrapperByWOId(workOrderIds.get(0));
		Map<String, List<String>> operatorRecipeMap = getOperatorWiseRecipeMap(recipeoperatormap);
     	List<String> fetchSummaryKPIList = ReportSummaryIndexWrapper.getLiveDriveKPIs();
		Map<String, Integer> summaryKpiIndexMap = ReportSummaryIndexWrapper
				.getLiveDriveKPIIndexMap(fetchSummaryKPIList);
		Map<String, List<KPISummaryDataWrapper>> summaryDataMap = new HashMap<>();	
		for (Entry<String, List<KPIWrapper>> operatorWisestat : operatorWiseStatMap.entrySet()) {
			for (Entry<String, List<String>> recipeoperatorMap : operatorRecipeMap.entrySet()) {
				String[] summaryData = reportService.getSummaryDataForReport(workOrderIds.get(ReportConstants.INDEX_ZER0),
							recipeoperatorMap.getValue(),fetchSummaryKPIList);
					List<KPISummaryDataWrapper> listOfKpiSummary = reportService.getKPISummaryDataForWorkOrderId(
							workOrderIds, operatorWiseDriveDataMap.get(recipeoperatorMap.getKey()),
							operatorWisestat.getValue(), summaryData, ReportConstants.NVSSVT,
							recipeoperatorMap.getKey());
					kpiDataWrapperMap.put(recipeoperatorMap.getKey(),reportService.populateKPiAggData(summaryData, kpiWrapperList,summaryKpiIndexMap));
					listOfKpiSummary = setTotalDistanceDriven(listOfKpiSummary, summaryData,recipeoperatorMap.getKey(),summaryKpiIndexMap);
					if(summaryDataMap.containsKey(recipeoperatorMap.getKey())) {
						List<KPISummaryDataWrapper> previousData = summaryDataMap.get(recipeoperatorMap.getKey());
					    previousData.addAll(listOfKpiSummary);
					    summaryDataMap.replace(recipeoperatorMap.getKey(), previousData);
					    } else {
				    	    summaryDataMap.put(recipeoperatorMap.getKey(), listOfKpiSummary);    
					        voiceStatsList.add(ReportUtil.getVoiceStatsbyOperator(summaryData,recipeoperatorMap.getKey(),summaryKpiIndexMap));
                  	}}
	//		logger.info("summaryDataMap {} ", summaryDataMap);
		}
        logger.info("Going to return the kpiDataWrapperMap of Size {} , with data {} ", kpiDataWrapperMap.size(),
				new Gson().toJson(kpiDataWrapperMap));
		return summaryDataMap;
	}

	private List<KPISummaryDataWrapper> setTotalDistanceDriven(List<KPISummaryDataWrapper> listOfKpiSummary,String[] summaryData, String operator, Map<String, Integer> summaryKpiIndexMap) {
		KPISummaryDataWrapper distanceKpiWrapper = getDrivenDistanceWrapper(summaryData,operator,summaryKpiIndexMap);
		if(listOfKpiSummary!=null && !listOfKpiSummary.isEmpty()){
			listOfKpiSummary.add(distanceKpiWrapper);
		}else{
			listOfKpiSummary = new ArrayList<>();
			listOfKpiSummary.add(distanceKpiWrapper);
		}
		return listOfKpiSummary;
	}

	private KPISummaryDataWrapper getDrivenDistanceWrapper(String[] summaryData, String operator,
			Map<String, Integer> summaryKpiIndexMap) {
		KPISummaryDataWrapper wrapper = new KPISummaryDataWrapper();
		wrapper.setTestName(ReportConstants.TOTAL_DISTANCE);
		try {
			if (summaryKpiIndexMap.get(ReportConstants.TOTAL_DISTANCE) != null
					&& !summaryKpiIndexMap.get(ReportConstants.TOTAL_DISTANCE).toString().isEmpty()
					&& summaryData.length > summaryKpiIndexMap.get(ReportConstants.TOTAL_DISTANCE)) {
				wrapper.setAchived(summaryData[summaryKpiIndexMap.get(ReportConstants.TOTAL_DISTANCE)]);
			}

		} catch (Exception e) {
			logger.error("Unable to find the total distance for operator {} , {} ", operator, Utils.getStackTrace(e));
		}
		logger.info("Going to retrun the total distance wrapper {} ", wrapper);
		return wrapper;
	}

	private Map<String, Map<String, String>> getOperatorWiseKPiImagesMap(
			Map<String, List<KPIWrapper>> operatorWiseStatMap, Map<String, List<String[]>> operatorWiseDriveDataMap, Map<String, Integer> kpiIndexMap) throws IOException {
		Map<String,Map<String,String>> operatorWisekpiImageMap = new HashMap<>();
		for (Entry<String, List<KPIWrapper>> operatorWisestatmap : operatorWiseStatMap.entrySet()) {
			operatorWisestatmap.setValue(reportService.modifyIndexOfCustomKpisForReport(operatorWisestatmap.getValue(),kpiIndexMap));
			List<SiteInformationWrapper> siteDataList =null;
			try {

				siteDataList=reportService.getSiteDataForReportByDataList(operatorWiseDriveDataMap.get(operatorWisestatmap.getKey()),kpiIndexMap);
			} catch (Exception e) {
				logger.error("Unable to find Site Data for operator {}, {} ",operatorWisestatmap.getKey(), e.getMessage());
			}
			DriveImageWrapper driveImageWrapper = new DriveImageWrapper(operatorWiseDriveDataMap.get(operatorWisestatmap.getKey()), kpiIndexMap.get(ReportConstants.LATITUDE),
					kpiIndexMap.get(ReportConstants.LONGITUDE), kpiIndexMap.get(ReportConstants.PCI_PLOT), operatorWisestatmap.getValue(), siteDataList, null);
			operatorWisekpiImageMap.put(operatorWisestatmap.getKey(), reportService.getImagesForReport(operatorWisestatmap.getValue(), driveImageWrapper,null,operatorWisestatmap.getKey(),ReportConstants.BENCHMARK,kpiIndexMap));
		}
		logger.info("Going to return the operatorWisekpiImageMap of Size {} , with data {} ",operatorWisekpiImageMap.size(),new Gson().toJson(operatorWisekpiImageMap));
		return operatorWisekpiImageMap;
	}

	private Map<String, List<String[]>> getOperatorWiseDriveData(List<Integer> workOrderIds, List<String> KPIList)
		      throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		   Map<String, List<RecipeWrapper>> recipeoperatormap = nvLayer3DashboardService.getRecipeMappingWrapperByWOId(
		         workOrderIds.get(0));
		   Map<String, List<String>> OperatorRecipeMap = getOperatorWiseRecipeMap(recipeoperatormap);
		   Map<String, List<String[]>> operatorWiseDataMap = new HashMap<>();
		   for (Entry<String, List<String>> operatorwisestatmap : OperatorRecipeMap.entrySet()) {
		         List<String[]> data = reportService.getDriveDataForReport(workOrderIds.get(ReportConstants.INDEX_ZER0), operatorwisestatmap.getValue(), KPIList);
		         if(operatorWiseDataMap.containsKey(operatorwisestatmap.getKey())) {
		            List<String[]> previousData = operatorWiseDataMap.get(operatorwisestatmap.getKey());
		            previousData.addAll(data);
		            operatorWiseDataMap.replace(operatorwisestatmap.getKey(), previousData);
		         } else {
		            operatorWiseDataMap.put(operatorwisestatmap.getKey(), data);
		         }
		     }
		   return operatorWiseDataMap;
		}

	/** Debug Here. 
	 * @param kpiIndexMap */
	private Map<String, List<KPIWrapper>> getOperatorWiseKpiStatsMap(List<Integer> workOrderIds,
			Map<String, Integer> kpiIndexMap) {
		Map<String, List<KPIWrapper>> operatorWiseStatsMap = new HashMap<>();
		List<LegendWrapper> legendList;

		Map<String, List<RecipeWrapper>> recipeoperatormap = nvLayer3DashboardService
				.getRecipeMappingWrapperByWOId(workOrderIds.get(0));
		logger.info("inside metho getOperatorWiseKpiStatsMap {} ", recipeoperatormap);

		try {
			for (Entry<String, List<RecipeWrapper>> recipewrappermap : recipeoperatormap.entrySet()) {
				for (RecipeWrapper recipewrapper : recipewrappermap.getValue()) {
					legendList = legendRangeDao.findAllLegendRangesAppliedTo(ReportConstants.BENCHMARK);
					List<KPIWrapper> kpiList = ReportUtil.convertLegendsListToKpiWrapperList(legendList, getKpiIndexMapForStats(kpiIndexMap));
					logger.info("Going to find the KpiWiseRange Stats for operator {} ", recipewrapper.getOperator());
                    operatorWiseStatsMap.put(recipewrapper.getDisplayOperatorName(), kpiList);
				}
			}
		} catch (Exception e) {
			logger.error("Exception inside method getOperatorWiseKpiStatsMap {} ", Utils.getStackTrace(e));
		}

		logger.info("Going to return the operatorWiseStatsMap of Size {} , with data {} ", operatorWiseStatsMap.size(),
				new Gson().toJson(operatorWiseStatsMap));
		return operatorWiseStatsMap;
	}
     
	public Map<String, Integer> getKpiIndexMapForStats(Map<String, Integer> kpiIndexMap) {
		Map<String, Integer> kpIndexMapforStats = new HashMap<String, Integer>();
		List<String> kpiname = Arrays.asList(ReportConstants.RSRP, ReportConstants.SINR, ReportConstants.MOS,
				ReportConstants.CQI, ReportConstants.CA, ReportConstants.RI, ReportConstants.DL_THROUGHPUT,
				ReportConstants.UL_THROUGHPUT, ReportConstants.SERVING_SYSTEM,ReportConstants.DL_BANWIDTH);
		for (Entry<String, Integer> kpiIndex : kpiIndexMap.entrySet()) {
			if (kpiname.contains(kpiIndex.getKey())) {
				kpIndexMapforStats.put(kpiIndex.getKey(), kpiIndex.getValue());
			}
		 }
		return kpIndexMapforStats;
	}

	private List<KPIWrapper> getKPiWiseRangeStatsData(List<Integer> workOrderIds, List<String> recipeList,
			List<String> operatorList, List<KPIWrapper> kpiList) {
		List<KPIWrapper> operatorWiseKPiList = new ArrayList<>(kpiList);
		try {
			operatorWiseKPiList = reportService.setKpiStatesIntokpiListForWoList(operatorWiseKPiList, workOrderIds,
					recipeList, operatorList);
		} catch (Exception e) {
			logger.error("Exception inside method getKPiWiseRangeStatsData {} ", Utils.getStackTrace(e));
		}
		return operatorWiseKPiList;
	}

	private BenchMarkReportWrapper getBenchMarkReportWrapper(Map<String, Map<String, String>> operatorKpiImageMap,
			Map<String, HashMap<String, BufferedImage>> operatorWiseLegendImageMap, Map<String, List<KPIDataWrapper>> kpiDataWrapperMap, Map<String, Integer> kpiIndexMap) {

		//logger.info("Inside getImageMap : {}, {} \n\n{} ",operatorKpiImageMap, operatorWiseLegendImageMap, kpiDataWrapperMap);
      	BenchMarkReportWrapper wrapper = new BenchMarkReportWrapper();

		List<BenchMarksubwrapper> subList = new ArrayList<>();

		List<BenchMarkOperatorInfo> rsrpImgList = new ArrayList<>();
		List<BenchMarkOperatorInfo> sinrImgList = new ArrayList<>();
		List<BenchMarkOperatorInfo> dlImgList = new ArrayList<>();
		List<BenchMarkOperatorInfo> ulImgList = new ArrayList<>();
		List<BenchMarkOperatorInfo> mcsImgList = new ArrayList<>();
		List<BenchMarkOperatorInfo> cqiImgList = new ArrayList<>();
		List<BenchMarkOperatorInfo> mimoImgList = new ArrayList<>();
		List<BenchMarkOperatorInfo> pciImgList = new ArrayList<>();
		List<BenchMarkOperatorInfo> mosImgList = new ArrayList<>();
		List<BenchMarkOperatorInfo> servingSystemImgList = new ArrayList<>();
		List<BenchMarkOperatorInfo> caImgList = new ArrayList<>();
		List<BenchMarkOperatorInfo> dlBandWidthImgList = new ArrayList<>();
		
		for (Map.Entry<String, Map<String, String>> operatorKpiImageEntry : operatorKpiImageMap.entrySet()) {
			Map<String, String> kpiImageMap = operatorKpiImageEntry.getValue();
			for (Map.Entry<String, String> kpiImageEntry : kpiImageMap.entrySet()) {
				BenchMarkOperatorInfo info = getBenchMarkInfoObject(operatorWiseLegendImageMap,operatorKpiImageEntry, kpiImageEntry,kpiIndexMap);
				setMinMaxAndAvgForKPI(kpiDataWrapperMap,info);
	//			logger.info("inside the method set kpi img {}",kpiImageEntry.getKey());
				if(kpiImageEntry.getKey().equalsIgnoreCase(kpiIndexMap.get(ReportConstants.RSRP).toString())){
					rsrpImgList.add(info);
					wrapper.setShowRsrpPage(true);
				} else if(kpiImageEntry.getKey().equalsIgnoreCase(kpiIndexMap.get(ReportConstants.SINR).toString())){
					sinrImgList.add(info);
					wrapper.setShowSinrPage(true);
				} else if(kpiImageEntry.getKey().equalsIgnoreCase(kpiIndexMap.get(ReportConstants.DL_THROUGHPUT).toString())){
					dlImgList.add(info);
					wrapper.setShowDLPage(true);
				} else if(kpiImageEntry.getKey().equalsIgnoreCase(kpiIndexMap.get(ReportConstants.UL_THROUGHPUT).toString())){
					ulImgList.add(info);
					wrapper.setShowUlPage(true);
				} else if(kpiImageEntry.getKey().equalsIgnoreCase(kpiIndexMap.get(ReportConstants.MCS).toString())){
					mcsImgList.add(info);
					wrapper.setShowMcsPage(true);
				} else if(kpiImageEntry.getKey().equalsIgnoreCase(kpiIndexMap.get(ReportConstants.CQI).toString())){
					cqiImgList.add(info);
					wrapper.setShowcqiPage(true);
				} else if(kpiImageEntry.getKey().equalsIgnoreCase(kpiIndexMap.get(ReportConstants.RI).toString())){
					mimoImgList.add(info);
					wrapper.setShowMimoPage(true);
				}
				else if(kpiImageEntry.getKey().equalsIgnoreCase(kpiIndexMap.get(ReportConstants.PCI_PLOT).toString())){
					pciImgList.add(info);
					wrapper.setShowpciPage(true);
				}
				else if(kpiImageEntry.getKey().equalsIgnoreCase(kpiIndexMap.get(ReportConstants.MOS).toString())){
					mosImgList.add(info);
					wrapper.setShowMosPage(true);
				}
				else if(kpiImageEntry.getKey().equalsIgnoreCase(kpiIndexMap.get(ReportConstants.SERVING_SYSTEM).toString())){
					servingSystemImgList.add(info);
					wrapper.setShowServingSystemPage(true);
				}
				else if(kpiImageEntry.getKey().equalsIgnoreCase(kpiIndexMap.get(ReportConstants.CA).toString())){
					caImgList.add(info);
					wrapper.setShowCaPage(true);
				}
				else if(kpiImageEntry.getKey().equalsIgnoreCase(kpiIndexMap.get(ReportConstants.DL_BANWIDTH).toString())){
					dlBandWidthImgList.add(info);
					wrapper.setShowDlBandwidthPage(true);
				}
			}
		}

		BenchMarksubwrapper subWrapper = new BenchMarksubwrapper();
		subWrapper.setRsrpImgList(rsrpImgList);
		subWrapper.setSinrImgList(sinrImgList);
		subWrapper.setDlImgList(dlImgList);
		subWrapper.setUlImgList(ulImgList);
		subWrapper.setMcsImgList(mcsImgList);
		subWrapper.setMimoImgList(mimoImgList);
		subWrapper.setMosImgList(mosImgList);
		subWrapper.setCqiImgList(cqiImgList);
		subWrapper.setPciImgList(pciImgList);
		subWrapper.setServingSystemImgList(servingSystemImgList);
		subWrapper.setCarrierAggImgList(caImgList);
		subWrapper.setDlBandwidthImgList(dlBandWidthImgList);
		subList.add(subWrapper);

		wrapper.setSubList(subList);
		return wrapper;
	}

	private void setMinMaxAndAvgForKPI(Map<String, List<KPIDataWrapper>> kpiDataWrapperMap,
			BenchMarkOperatorInfo info) {
		if (info != null && kpiDataWrapperMap !=null) {
			List<KPIDataWrapper> kpiDataWrappers = kpiDataWrapperMap.get(info.getOperatorName());
			if (kpiDataWrappers != null) {
				for (KPIDataWrapper kpiDataWrapper : kpiDataWrappers) {
					if (kpiDataWrapper != null && kpiDataWrapper.getKpiName().equalsIgnoreCase(info.getKpiName())) {
						info.setMinValue(ReportUtil.round(kpiDataWrapper.getMinValue(), ReportConstants.TWO_DECIMAL_PLACES));
						info.setMaxValue(ReportUtil.round(kpiDataWrapper.getMaxValue(), ReportConstants.TWO_DECIMAL_PLACES));
						info.setAvgValue(ReportUtil.round(kpiDataWrapper.getAvgValue(), ReportConstants.TWO_DECIMAL_PLACES));
					}
				}
			}}

	}

	private BenchMarkOperatorInfo getBenchMarkInfoObject(Map<String, HashMap<String, BufferedImage>> operatorWiseLegendImageMap, Entry<String, Map<String, String>> operatorKpiImageEntry, Entry<String, String> kpiImageEntry, Map<String, Integer> kpiIndexMap) {
		BenchMarkOperatorInfo info = new BenchMarkOperatorInfo();
		try{
			String path = ConfigUtils.getString(ReportConstants.IMAGE_PATH_FOR_NV_REPORT);
			Map<Integer, String> indexKpiMap = MapUtils.invertMap(kpiIndexMap);
			info.setOperatorName(operatorKpiImageEntry.getKey());
			setKPiNameByIndex(info,kpiImageEntry,indexKpiMap,kpiIndexMap);
			if(kpiImageEntry.getKey().equalsIgnoreCase(kpiIndexMap.get(ReportConstants.PCI_PLOT).toString())){
				info.setKpiName(indexKpiMap.get(Integer.parseInt(kpiImageEntry.getKey())));
				info.setKpiImg(kpiImageEntry.getValue());
				info.setKpiLegendImg(operatorKpiImageEntry.getValue().get(ReportConstants.PCI_LEGEND));
				logger.info("pci legend Image is {}",info.getKpiLegendImg());
			}else if(kpiImageEntry.getKey().equalsIgnoreCase(kpiIndexMap.get(ReportConstants.SERVING_SYSTEM).toString())){
				info.setKpiImg(kpiImageEntry.getValue());
				info.setKpiLegendImg(operatorKpiImageEntry.getValue().get(ReportConstants.SERVING_SYSTEM));
				logger.info("Serving System legend image is {}",info.getKpiLegendImg());
			}
			else if(kpiImageEntry.getKey().equalsIgnoreCase(kpiIndexMap.get(ReportConstants.DL_BANWIDTH).toString())){
				info.setKpiImg(kpiImageEntry.getValue());
				info.setKpiLegendImg(operatorKpiImageEntry.getValue().get(ReportConstants.DL_BANWIDTH));
				logger.info("Bandwidth Dl legend image is {}",info.getKpiLegendImg());
			}
			
			else {
				if(kpiImageEntry.getKey().equalsIgnoreCase((kpiIndexMap.get(ReportConstants.FTP_DL_THROUGHPUT)).toString())){
					info.setKpiName(ReportConstants.FTP_DL_THROUGHPUT);
				}else if( kpiImageEntry.getKey().equalsIgnoreCase((kpiIndexMap.get(ReportConstants.FTP_UL_THROUGHPUT)).toString())){
					info.setKpiName(ReportConstants.FTP_UL_THROUGHPUT);
				}else if(kpiImageEntry.getKey().equalsIgnoreCase((kpiIndexMap.get(ReportConstants.HTTP_DL_THROUGHPUT)).toString())){
					info.setKpiName(ReportConstants.HTTP_DL_THROUGHPUT);
				}else if(kpiImageEntry.getKey().equalsIgnoreCase((kpiIndexMap.get(ReportConstants.HTTP_UL_THROUGHPUT)).toString())){
					info.setKpiName(ReportConstants.HTTP_UL_THROUGHPUT);
				}else if(kpiImageEntry.getKey().equalsIgnoreCase((kpiIndexMap.get(ReportConstants.DL_THROUGHPUT)).toString())){
					info.setKpiName(ReportConstants.DL_THROUGHPUT);
				}else if(kpiImageEntry.getKey().equalsIgnoreCase((kpiIndexMap.get(ReportConstants.UL_THROUGHPUT)).toString())){
					info.setKpiName(ReportConstants.UL_THROUGHPUT);
				}
				info.setKpiImg(kpiImageEntry.getValue());
				Map<String, String> legendMap = mapImagesService.saveDriveImages(
						operatorWiseLegendImageMap.get(operatorKpiImageEntry.getKey()),
						path+ReportConstants.BENCHMARK+ ReportConstants.FORWARD_SLASH+operatorKpiImageEntry.getKey(), false);
				logger.info("legendMap Images json {} ",new Gson().toJson(legendMap));
				info.setKpiLegendImg(legendMap.get("LEGEND_"+kpiImageEntry.getKey()));
			}}
		catch (Exception e) {
			logger.error("Exception inside method getBenchMarkInfoObject {} ",e.getMessage());
		}
		return info;
	}


	private BenchMarkOperatorInfo setKPiNameByIndex(BenchMarkOperatorInfo info, Entry<String, String> kpiImageEntry, Map<Integer, String> indexKpiMap, Map<String, Integer> kpiIndexMap) {
		try {
			if(kpiImageEntry.getKey().equalsIgnoreCase((kpiIndexMap.get(ReportConstants.FTP_DL_THROUGHPUT)).toString())){
				info.setKpiName(ReportConstants.FTP_DL_THROUGHPUT);
			}else if( kpiImageEntry.getKey().equalsIgnoreCase((kpiIndexMap.get(ReportConstants.FTP_UL_THROUGHPUT)).toString())){
				info.setKpiName(ReportConstants.FTP_UL_THROUGHPUT);
			}else if(kpiImageEntry.getKey().equalsIgnoreCase((kpiIndexMap.get(ReportConstants.HTTP_DL_THROUGHPUT)).toString())){
				info.setKpiName(ReportConstants.HTTP_DL_THROUGHPUT);
			}else if(kpiImageEntry.getKey().equalsIgnoreCase((kpiIndexMap.get(ReportConstants.HTTP_UL_THROUGHPUT)).toString())){
				info.setKpiName(ReportConstants.HTTP_UL_THROUGHPUT);
			}else if(kpiImageEntry.getKey().equalsIgnoreCase((kpiIndexMap.get(ReportConstants.DL_THROUGHPUT)).toString())){
				info.setKpiName(ReportConstants.DL_THROUGHPUT);
			}else if(kpiImageEntry.getKey().equalsIgnoreCase((kpiIndexMap.get(ReportConstants.UL_THROUGHPUT)).toString())){
				info.setKpiName(ReportConstants.UL_THROUGHPUT);
			}else if(kpiImageEntry.getKey().equalsIgnoreCase((kpiIndexMap.get(ReportConstants.RSRP)).toString())){
				info.setKpiName(ReportConstants.RSRP);
			}else if(kpiImageEntry.getKey().equalsIgnoreCase((kpiIndexMap.get(ReportConstants.SINR)).toString())){
				info.setKpiName(ReportConstants.SINR);
			}else if(kpiImageEntry.getKey().equalsIgnoreCase(ReportConstants.SERVING_SYSTEM) ||
					kpiImageEntry.getKey().contains("SATELLITE") || 
					kpiImageEntry.getKey().contains("DL_EARFCN") ||
					kpiImageEntry.getKey().contains("legend")){
				//This Block is Empty
			}else {
				info.setKpiName(indexKpiMap.get(Integer.parseInt(kpiImageEntry.getKey())));
			}
		} catch (Exception e) {
			logger.debug("Exception in setting kpiNameBy Index for kpi  {} , {} ",kpiImageEntry.getKey(),e.getMessage());
		}
		return info;
	}

	private File  createBenchMarkReport(ArrayList<BenchMarkReportWrapper> benchmarkReportWrapperList,Integer analyticsRespositoryId, String filePath){
		logger.info("Inside createBenchMarkReport : {} ",benchmarkReportWrapperList);
		try {
		String jasperReportSourcePath = ConfigUtils.getString(ReportConstants.BENCHMARK_JASPER_PATH);
		Map<String, Object> imageMap = new HashMap<>();
		imageMap.put(ReportConstants.SUBREPORT_DIR, jasperReportSourcePath);
		imageMap.put(ReportConstants.IMAGE_PARAM_HEADER_BG, jasperReportSourcePath + ReportConstants.IMAGE_HEADER_BG);
		imageMap.put(ReportConstants.IMAGE_PARAM_HEADER_LOG, jasperReportSourcePath + ReportConstants.IMAGE_HEADER_LOG_NV);
		imageMap.put(ReportConstants.IMAGE_PARAM_SCREEN_LOG, jasperReportSourcePath + ReportConstants.IMAGE_SCREEN_LOGO_STATIONARY);
		imageMap.put(ReportConstants.IMAGE_PARAM_SCREEN_BG, jasperReportSourcePath + ReportConstants.IMAGE_SCREEN_BG_JPEG);
		imageMap.put(ReportConstants.IMAGE_PARAM_THANK_YOU_BENCHMARK, jasperReportSourcePath + ReportConstants.THANK_YOU_JPEG);
		imageMap.put(ReportConstants.IMAGE_PARAM_FOOTER_LOGO, jasperReportSourcePath + ReportConstants.FOOTER_LOGO_IMG);
		JRBeanCollectionDataSource rfbeanColDataSource = new JRBeanCollectionDataSource(benchmarkReportWrapperList);
		
			String absolutefilePath= ReportUtil.getFileName(ReportConstants.BENCHMARK,analyticsRespositoryId,filePath);
			String finalFilePath = absolutefilePath.replace(ReportConstants.PDF_EXTENSION, ReportConstants.PPTX_EXTENSION);
			JasperFillManager.fillReportToFile(jasperReportSourcePath + ReportConstants.BENCHMARK_JASPER_NAME ,finalFilePath ,imageMap, rfbeanColDataSource);
			logger.info("Report Created");
			getPPTXFromReport(finalFilePath, finalFilePath);
			return ReportUtil.getIfFileExists(finalFilePath);
		} catch (Exception e) {
			logger.error("Exception In createBenchMarkReport : {} ",Utils.getStackTrace(e));
		}
		return null;
	}
	
	private boolean getPPTXFromReport(String inputFileName, String outputFileName) throws JRException {
		if(!StringUtils.isBlank(inputFileName)) {
			JRPptxExporter exporter = new JRPptxExporter();
			exporter.setExporterInput(new SimpleExporterInput(new File(inputFileName)));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(new File(outputFileName)));
//			exporter.setParameter(JRExporterParameter.INPUT_FILE, new File(inputFileName));
//			exporter.setParameter(JRExporterParameter.OUTPUT_FILE, new File(outputFileName));
			exporter.exportReport();
			return true;
		}
		return false;
	}

	@Override
	@Transactional
	public Response saveFileAndUpdateStatus(Integer analyticsrepoId,  String filePath,
			GenericWorkorder genericWorkorder, File file, List<Integer> workOrderIds) {
		logger.info("Inside method going to saveFileAndUpdateStatus filePath {} ",filePath);
		if (file != null) {
			filePath+=file.getName();
			if (nvReportHdfsDao.saveFileToHdfs(file, filePath)) {
				logger.info("File saved Successfully ");
				analyticsrepositoryDao.updateStatusInAnalyticsRepository(analyticsrepoId,filePath,ReportConstants.HDFS,progress.Generated,null);
				logger.info("Status in analytics repository is updated ");
				if(workOrderIds.size()<=ReportConstants.INDEX_ONE){
					Map<String, String> geoMap = genericWorkorder.getGwoMeta();
					geoMap.put(NVWorkorderConstant.REPORT_INSTACE_ID, analyticsrepoId.toString());
					genericWorkorder.setGwoMeta(geoMap);
					try {
						iGenericWorkorderDao.update(genericWorkorder);
					} catch (DaoException e) {
						logger.error("Error inside the method saveFileAndUpdateStatus {}",e.getMessage());
					}
				}
				logger.info("Going to delete file from Local if exist {} ",filePath);
				reportService.deleteFileIfExist(filePath);
				return Response.ok(ForesightConstants.SUCCESS_JSON).build();
			} else {
				return Response.ok(ForesightConstants.FAILURE_JSON).build();
			}
		}
		return Response.ok(ForesightConstants.FAILURE_JSON).build();
	}


	private List<BenchMarkComparison> getOperatorComparisonListFromMapNew(Map<String, List<KPISummaryDataWrapper>> operatorWiseSummaryMap, 
			Map<String, List<String[]>> operatorWiseDriveDataMap, List<VoiceStatsWrapper> voiceStatsList, Map<String, Integer> kpiIndexMap){
//		logger.info("Inside methdo getOperatorComparisonListFromMapNew {} ",operatorWiseSummaryMap);
		ArrayList<BenchMarkComparison> listComparisonData = new ArrayList<>();
		List<String> kpiList = new ArrayList<>(Arrays.asList(ReportConstants.RSRP,ReportConstants.SINR,ReportConstants.DL,ReportConstants.UL,ReportConstants.MOS));
		List<Double> comparisonValue = new ArrayList<>(Arrays.asList(-105.0,3.0,5.01,3.0,3.01));
//		Map<String,Integer> kpiIndexMap = DriveHeaderConstants.getKpiIndexMap();
		for (Entry<String, List<String[]>> operatorWiseDriveMap : operatorWiseDriveDataMap.entrySet()) {
			BenchMarkComparison comparison = new BenchMarkComparison();
			if(operatorWiseDriveMap	.getKey()!=null && !operatorWiseDriveMap.getKey().isEmpty())
			{
			comparison.setOperatorName(operatorWiseDriveMap	.getKey()
															.trim()
															.toUpperCase());
			
			String operatorImagePath = ConfigUtils.getString(ReportConstants.BENCHMARK_REPORT_OPERATOR_LOGO) + operatorWiseDriveMap.getKey().trim().toUpperCase() + ReportConstants.DOT_JPG;
			if (ReportUtil.getIfFileExists(operatorImagePath) != null) {
				comparison.setOperatorImage(ConfigUtils.getString(ReportConstants.BENCHMARK_REPORT_OPERATOR_LOGO)
						+ operatorWiseDriveMap	.getKey()
												.trim()
												.toUpperCase()
						+ ReportConstants.DOT_JPG);
			}
			}
			List<String[]> jsonStream = operatorWiseDriveMap.getValue().stream().filter(Objects::nonNull).collect(Collectors.toList());
			int index=0;
			for (String kpi : kpiList) {
				if(kpiIndexMap.get(kpi)!=null && !kpiIndexMap.get(kpi).toString().isEmpty())
				{
					int kpiDataIndex = kpiIndexMap.get(kpi);
					populateKpiData(comparisonValue, kpiDataIndex, jsonStream, index, kpi,comparison);
					index++;
				}
				
			}
			setVoiceDataToComparisonData(operatorWiseDriveMap.getKey(), voiceStatsList, comparison);
			listComparisonData.add(comparison);
		}
//		logger.info("listComparisonData {} ",new Gson().toJson(listComparisonData));
		return listComparisonData;
	}

	private void setVoiceDataToComparisonData(String operator, List<VoiceStatsWrapper> voiceStatsList,
			BenchMarkComparison comparison) {
		for(VoiceStatsWrapper voiceStat : voiceStatsList) {
			if(voiceStat.getOperator() != null && operator != null && operator.equalsIgnoreCase(voiceStat.getOperator())) {
				comparison.setVoiceAccessPercent(NumberUtils.isCreatable(voiceStat.getCallSetupSucessRate()) ? ReportUtil.round(Double.parseDouble(voiceStat.getCallSetupSucessRate()), ReportConstants.TWO_DECIMAL_PLACES) : null);
				comparison.setVoiceRetainPercent(NumberUtils.isCreatable(voiceStat.getCallDropRate()) ? ReportUtil.round(Double.parseDouble(voiceStat.getCallDropRate()), ReportConstants.TWO_DECIMAL_PLACES) : null);
			}
		}
	}

	private Double populateKpiData(List<Double> comparisonValue, int kpiDataIndex, List<String[]> jsonStream,
			final int index, String kpi, BenchMarkComparison comparison) {
		if (kpi.equalsIgnoreCase(ReportConstants.RSRP)) {
			comparison.setRsrpPercent(ReportUtil.round(getKPiPercentage(comparisonValue, kpiDataIndex, jsonStream, index), ReportConstants.TWO_DECIMAL_PLACES));
		} else if (kpi.equalsIgnoreCase(ReportConstants.SINR)) {
			comparison.setSinrPercent(ReportUtil.round(getKPiPercentage(comparisonValue, kpiDataIndex, jsonStream, index), ReportConstants.TWO_DECIMAL_PLACES));
		} else if (kpi.equalsIgnoreCase(ReportConstants.DL)) {
			comparison.setDlPercent(ReportUtil.round(getKPiPercentage(comparisonValue, kpiDataIndex, jsonStream, index), ReportConstants.TWO_DECIMAL_PLACES));
		} else if (kpi.equalsIgnoreCase(ReportConstants.UL)) {
			comparison.setUlPercent(ReportUtil.round(getKPiPercentage(comparisonValue, kpiDataIndex, jsonStream, index), ReportConstants.TWO_DECIMAL_PLACES));
		} else if (kpi.equalsIgnoreCase(ReportConstants.MOS)) {
			comparison.setMosPercent(ReportUtil.round(getKPiPercentage(comparisonValue, kpiDataIndex, jsonStream, index), ReportConstants.TWO_DECIMAL_PLACES));
		}
		return null;
	}

	private Double getKPiPercentage(List<Double> comparisonValue, int kpiDataIndex, List<String[]> jsonStream,
			final int index) {
		int totalSamples = 0;
		int samplesInCriteria = 0;
		Stream<String[]> filteredStream = jsonStream.stream()
													.filter(array -> array.length > kpiDataIndex)
													.filter(array -> (array[kpiDataIndex] != null
															&& !array[kpiDataIndex].isEmpty() && NumberUtils.isCreatable(array[kpiDataIndex])));
		List<String[]> reList = filteredStream.collect(Collectors.toList());
		totalSamples = reList.size();
		samplesInCriteria = reList	.stream()
									.filter(array -> (Double.parseDouble(array[kpiDataIndex]) >= comparisonValue.get(
											index)))
									.collect(Collectors.toList())
									.size();
		if (totalSamples != 0) {
			return ReportUtil.round(ReportUtil.getPercentage(samplesInCriteria, totalSamples),
					ReportConstants.TWO_DECIMAL_PLACES);
		}
		return null;
	}
	
	private Double getKPiAverage(int kpiDataIndex, List<String[]> jsonStream) {
		Long sampleCounts = null;
		Double smapleCountSum = null;
		List<String[]> filteredStream = jsonStream	.stream()
													.filter(array -> array.length > kpiDataIndex)
													.filter(array -> (array[kpiDataIndex] != null
															&& !array[kpiDataIndex].isEmpty()))
													.collect(Collectors.toList());
		sampleCounts = (long) filteredStream.size();
		smapleCountSum = filteredStream	.stream()
										.map(array -> Double.parseDouble(array[kpiDataIndex]))
										.mapToDouble(Double::doubleValue)
										.sum();
		if (sampleCounts != null && smapleCountSum != null && sampleCounts != 0) {
			return (smapleCountSum / sampleCounts);
		}
		return null;
	}
	
	private void setRankDataListToBenchmarkWrapper(Map<String, List<String[]>> operatorWiseDriveDataMap, BenchMarksubwrapper subWrapper, Map<String, Integer> kpiIndexMap) {
		
//		Map<String,Integer> kpiIndexMap = DriveHeaderConstants.getKpiIndexMap();
		List<String> kpiList = new ArrayList<>(Arrays.asList(ReportConstants.RSRP,ReportConstants.SINR,ReportConstants.DL,ReportConstants.UL));
		List<Double> comparisonValue = new ArrayList<>(Arrays.asList(-105.0,3.0,5.0,3.0));
		
		for (String kpi : kpiList) {
			if (kpi.equalsIgnoreCase(ReportConstants.RSRP)) {
				subWrapper.setRsrpRankList(
						getRankListForKpi(comparisonValue, ForesightConstants.ZERO, operatorWiseDriveDataMap, kpiIndexMap.get(kpi)));
			} else if (kpi.equalsIgnoreCase(ReportConstants.SINR)) {
				subWrapper.setSinrRankList(
						getRankListForKpi(comparisonValue, ForesightConstants.ONE, operatorWiseDriveDataMap, kpiIndexMap.get(kpi)));
			} else if (kpi.equalsIgnoreCase(ReportConstants.DL)) {
				subWrapper.setDlRankList(
						getRankListForKpi(comparisonValue, ForesightConstants.TWO, operatorWiseDriveDataMap, kpiIndexMap.get(kpi)));
			} else if (kpi.equalsIgnoreCase(ReportConstants.UL)) {
				subWrapper.setUlRankList(
						getRankListForKpi(comparisonValue, NVConstant.THREE_INT, operatorWiseDriveDataMap, kpiIndexMap.get(kpi)));
			}
			
		}
	}

	private List<KpiRankWrapper> getRankListForKpi(List<Double> comparisonValue, int index,
			Map<String, List<String[]>> operatorWiseDriveDataMap, Integer kpiIndex) {
		List<KpiRankWrapper> kpiRankWrapperList = new ArrayList<>();
		
		for(Entry<String, List<String[]>> operatorDataEntry : operatorWiseDriveDataMap.entrySet()) {
			KpiRankWrapper kpiRankWrapper = new KpiRankWrapper();
			kpiRankWrapper.setOperatorName(operatorDataEntry.getKey());
			Double kpiAverage = ReportUtil.round(getKPiAverage(kpiIndex, operatorDataEntry.getValue()), ReportConstants.TWO_DECIMAL_PLACES);
			kpiRankWrapper.setAvgKpi(kpiAverage != null ? kpiAverage.toString() : null);
			Double kpiPercentage  = getKPiPercentage(comparisonValue, kpiIndex, operatorDataEntry.getValue(), index);
			kpiRankWrapper.setKpiPercent(kpiPercentage != null ? ReportUtil.round((ReportConstants.TOTAL_PERCENT - kpiPercentage),ReportConstants.TWO_DECIMAL_PLACES) + Symbol.EMPTY_STRING : null);
			kpiRankWrapperList.add(kpiRankWrapper);
		}
		Collections.sort(kpiRankWrapperList, getKpiPercentComparator());
		return kpiRankWrapperList;
	}
	
	private static Comparator<KpiRankWrapper> getKpiPercentComparator() {
		return (wrapper1, wrapper2) -> {
			if(wrapper1 != null && NumberUtils.isCreatable(wrapper1.getKpiPercent()) && wrapper2 != null && NumberUtils.isCreatable(wrapper2.getKpiPercent())) {
				Double percent1 = Double.parseDouble(wrapper1.getKpiPercent());
				Double percent2 = Double.parseDouble(wrapper2.getKpiPercent());
				return percent1.compareTo(percent2);
			}
			return NumberUtils.INTEGER_MINUS_ONE;
		};
	}
	
	private static Comparator<LegendListWrapper> getLegendListComparator() {
		return (wrapper1, wrapper2) -> {
			if(wrapper1 != null && wrapper2 != null) {
				Double range1 = Double.parseDouble(wrapper1.getLegendValue().substring(ReportConstants.INDEX_ZER0, wrapper1.getLegendValue().indexOf(ReportConstants.BENCHMARK_RANGE_COMPARATOR_CHARACTER) - 1).trim());
				Double range2 = Double.parseDouble(wrapper2.getLegendValue().substring(ReportConstants.INDEX_ZER0, wrapper2.getLegendValue().indexOf(ReportConstants.BENCHMARK_RANGE_COMPARATOR_CHARACTER) - 1).trim());
				return range1.compareTo(range2);
			}
			return NumberUtils.INTEGER_MINUS_ONE;
		};
	}
	
	private Map<String, List<String>> getOperatorWiseRecipeMap(Map<String, List<RecipeWrapper>> recipeoperatormap) {
		logger.info("Inside method getOperatorWiseRecipeMap ");
		Map<String, List<String>> operatorrecipemap = new HashMap<>();
		List<String> recipelist = new ArrayList<>();
		for (Entry<String, List<RecipeWrapper>> recipeoperatorMap : recipeoperatormap.entrySet()) {
			for (RecipeWrapper operatorWisestatmap : recipeoperatorMap.getValue()) {
				if (operatorrecipemap.containsKey(operatorWisestatmap.getDisplayOperatorName())) {
					List<String> previousrecipelist = operatorrecipemap
							.get(operatorWisestatmap.getDisplayOperatorName());
					previousrecipelist.add(operatorWisestatmap.getRecipeId());
					operatorrecipemap.replace(operatorWisestatmap.getDisplayOperatorName(), previousrecipelist);
					recipelist = new ArrayList<>();
				} else {
					recipelist.add(operatorWisestatmap.getRecipeId());
					operatorrecipemap.put(operatorWisestatmap.getDisplayOperatorName(), recipelist);
					recipelist = new ArrayList<>();
				}
    		}
		}
		logger.info("operator recipe map {}",operatorrecipemap);
		return operatorrecipemap;

	}
	
}

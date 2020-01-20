package com.inn.foresight.module.nv.report.service.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.inn.commons.encoder.AESUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.protobuf.ServiceException;
import com.inn.commons.Symbol;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.hadoop.hbase.HBaseResult;
import com.inn.commons.lang.StringUtils;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.NVConstant;
import com.inn.foresight.module.nv.core.workorder.dao.impl.IGenericWorkorderDao;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;
import com.inn.foresight.module.nv.layer3.constants.NVLayer3Constants;
import com.inn.foresight.module.nv.layer3.constants.QMDLConstant;
import com.inn.foresight.module.nv.layer3.service.INVHbaseService;
import com.inn.foresight.module.nv.report.RangeSlab;
import com.inn.foresight.module.nv.report.dao.INVReportHdfsDao;
import com.inn.foresight.module.nv.report.service.IMapImagesService;
import com.inn.foresight.module.nv.report.service.IReportService;
import com.inn.foresight.module.nv.report.service.IStealthReportService;
import com.inn.foresight.module.nv.report.utils.DriveHeaderConstants;
import com.inn.foresight.module.nv.report.utils.ReportConstants;
import com.inn.foresight.module.nv.report.utils.ReportUtil;
import com.inn.foresight.module.nv.report.utils.StealthUtils;
import com.inn.foresight.module.nv.report.workorder.wrapper.KPIWrapper;
import com.inn.foresight.module.nv.report.wrapper.GraphDataWrapper;
import com.inn.foresight.module.nv.report.wrapper.stealth.StealthDateWiseStatusWrapper;
import com.inn.foresight.module.nv.report.wrapper.stealth.StealthWOBestWorstDataWrapper;
import com.inn.foresight.module.nv.report.wrapper.stealth.StealthWOBestWorstItemWrapper;
import com.inn.foresight.module.nv.report.wrapper.stealth.StealthWOCoverageAnalysisWrapper;
import com.inn.foresight.module.nv.report.wrapper.stealth.StealthWOCoverageGraphDataWrapper;
import com.inn.foresight.module.nv.report.wrapper.stealth.StealthWODetailItemWrapper;
import com.inn.foresight.module.nv.report.wrapper.stealth.StealthWODetailsWrapper;
import com.inn.foresight.module.nv.report.wrapper.stealth.StealthWOKpiAnalysisWrapper;
import com.inn.foresight.module.nv.report.wrapper.stealth.StealthWOMapDataWrapper;
import com.inn.foresight.module.nv.report.wrapper.stealth.StealthWOPingAnalysisWrapper;
import com.inn.foresight.module.nv.report.wrapper.stealth.StealthWOSummaryWrapper;
import com.inn.foresight.module.nv.report.wrapper.stealth.StealthWOWPTAnalysisWrapper;
import com.inn.foresight.module.nv.report.wrapper.stealth.StealthWOYoutubeAnalysisWrapper;
import com.inn.foresight.module.nv.report.wrapper.stealth.StealthWorkorderReportWrapper;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;
import com.inn.foresight.module.nv.workorder.stealth.dao.IStealthTaskDetailDao;
import com.inn.foresight.module.nv.workorder.stealth.dao.IStealthTaskResultDao;
import com.inn.foresight.module.nv.workorder.stealth.wrapper.StealthWOWrapper;
import com.inn.product.legends.dao.ILegendRangeDao;
import com.inn.product.legends.utils.LegendWrapper;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service("StealthReportServiceImpl")
public class StealthReportServiceImpl implements IStealthReportService{

	/** The logger. */
	private Logger logger = LogManager.getLogger(StealthReportServiceImpl.class);

	@Autowired
	private IGenericWorkorderDao genericWorkorderDao ;

	@Autowired
	private IStealthTaskDetailDao stealthTaskDetailDao;

	@Autowired
	private INVHbaseService nvHbaseService;

	@Autowired
	private ILegendRangeDao legendRangeDao;
	
	@Autowired
	private IStealthTaskResultDao stealthTaskResultDao;

	@Autowired
	private INVReportHdfsDao nvReportHdfsDao;
	
	@Autowired
	private IMapImagesService mapImagesServiceImpl;
	
	@Autowired
	private IReportService reportService;
	
	
	

	
	@Override
	public File getPDFReportForStealthWO(String workorderId, List<Integer> taskIdList, Long startDate, Long endDate,Integer analyticsRepositoryId) throws ServiceException {
		logger.info("Going to generate the Stealth workorder Report for workorderId {} ",workorderId);
		try {
			String mainFilePath = ConfigUtils.getString(ReportConstants.NV_REPORTS_PATH) + ReportConstants.STEALTH
					+ ReportConstants.FORWARD_SLASH;
			ReportUtil.deleteAllFilesFromDirectory(mainFilePath);
			String filePath = mainFilePath + new Date().getTime() + ReportConstants.FORWARD_SLASH;
			ReportUtil.createDirectory(filePath);
			GenericWorkorder genericWorkorder = genericWorkorderDao.findByPkWithUser(Integer.parseInt(workorderId));
			StealthWorkorderReportWrapper stealthDataWrapper = getFinalDataWrapperForJapser(genericWorkorder, taskIdList, startDate, endDate);
			Map<String, Object> parameterMap = new HashMap<>();
			addMapImageToParameterMap(genericWorkorder, taskIdList, parameterMap, filePath, startDate, endDate);
			parameterMap.put(ReportConstants.KEY_WO_NAME, genericWorkorder.getWorkorderName());
			parameterMap.put(ReportConstants.KEY_DATE_TIME, ReportUtil.getFormattedDate(
					genericWorkorder.getCreationTime(), ReportConstants.DATE_FORMAT_DD_MM_YYYY_HH_MM_SS));
			return proceedToCreateReport(analyticsRepositoryId,genericWorkorder,stealthDataWrapper, parameterMap, getFileNameForReport(filePath, workorderId),
					ReportConstants.MAIN_JASPER);
		} catch (Exception e) {
			throw new ServiceException("Unable to Generate report due to {} " + ExceptionUtils.getStackTrace(e));
		}
	}
	
	private void addMapImageToParameterMap(GenericWorkorder genericWorkorder, List<Integer> taskIdList,
			Map<String, Object> parameterMap, String filePath, Long startDate, Long endDate) {
		try {
			List<HBaseResult> deviceDetailsResults = nvHbaseService.getHBaseResultListForStealthReport(genericWorkorder.getId().toString(), taskIdList, null, null);
			deviceDetailsResults = ReportUtil.filterStealthDataByDate(startDate, endDate, deviceDetailsResults);
			List<StealthWOMapDataWrapper> mapDataWrapperList = new ArrayList<>();
			for (HBaseResult hbaseResult : deviceDetailsResults) {
				if (hbaseResult != null
						&& NumberUtils.isParsable(hbaseResult.getString(ReportConstants.STEALTH_DETAIL_LATITUDE_COLUMN_KEY.getBytes()))
						&& NumberUtils.isParsable(hbaseResult.getString(ReportConstants.STEALTH_DETAIL_LONGITUDE_COLUMN_KEY.getBytes()))
						&& !StringUtils.isBlank(hbaseResult.getString(ReportConstants.STEALTH_DETAIL_SCORE_COLUMN_KEY.getBytes()))) {
					
					StealthWOMapDataWrapper mapData = new StealthWOMapDataWrapper();
					mapData.setLatitude(Double.parseDouble(hbaseResult.getString(ReportConstants.STEALTH_DETAIL_LATITUDE_COLUMN_KEY.getBytes())));
					mapData.setLongitude(Double.parseDouble(hbaseResult.getString(ReportConstants.STEALTH_DETAIL_LONGITUDE_COLUMN_KEY.getBytes())));
					mapData.setScoreImagePath(ConfigUtils.getString(ReportConstants.STEALTH_WO_REPORT_JASPER_FILE_PATH) + hbaseResult.getString(ReportConstants.STEALTH_DETAIL_SCORE_COLUMN_KEY.getBytes()).trim() + ReportConstants.DOT_PNG);
					mapDataWrapperList.add(mapData);
				}
			}
			
			BufferedImage image = mapImagesServiceImpl.getMapImageForStealthWO(mapDataWrapperList);
			if(image != null) {
				ImageIO.write(image, ReportConstants.JPG, new File(filePath + ReportConstants.STEALTH_DEVICES_MAP_IMAGE_NAME));
				parameterMap.put(ReportConstants.STEALTH_DEVICES_MAP_IMAGE_KEY, filePath + ReportConstants.STEALTH_DEVICES_MAP_IMAGE_NAME);
			} else {
				parameterMap.put(ReportConstants.STEALTH_DEVICES_MAP_IMAGE_KEY, null);
			}
		} catch (Exception e) {
			logger.error("Exception while adding Devices map image to parameter map {}",
					Utils.getStackTrace(e));
		}
	}
	
	private String getFileNameForReport(String filePath, String workorderId) {
		return filePath + ReportConstants.STEALTH_REPORT + workorderId + ReportConstants.PDF_EXTENSION;
	}

	private StealthWorkorderReportWrapper getFinalDataWrapperForJapser(GenericWorkorder genericWorkorder, List<Integer> taskIdList, Long startDate, Long endDate) throws ServiceException {
		StealthWorkorderReportWrapper wrapper = new StealthWorkorderReportWrapper();
		try {
			logger.info("Timestamps are: startDate: {}, endDate: {}", startDate, endDate);
			Integer workorderId = genericWorkorder.getId();
			List<StealthWODetailsWrapper> stealthWoDetail = getStealthWoDetailWrapper(genericWorkorder);
			wrapper.setWoDetails(stealthWoDetail);
			logger.debug("stealthWoDetail {} ",new Gson().toJson(stealthWoDetail));
			String woFrequency = genericWorkorder.getGwoMeta().get(NVConstant.WO_FREQUENCY);

			List<StealthWOSummaryWrapper> summaryDetail = getStealthSummaryWrapper(workorderId, taskIdList, startDate, endDate, woFrequency);
			wrapper.setWoSummary(summaryDetail);
			logger.debug("summaryDetail {} ",new Gson().toJson(summaryDetail));

			List<HBaseResult> hbaseResultList = nvHbaseService.getHbaseResultListforStealth(String.valueOf(workorderId), taskIdList, null, startDate, endDate);
			
			List<StealthWOCoverageAnalysisWrapper> coverageDataAnalysisList = getCoverageDataOfStealth(hbaseResultList ,workorderId, taskIdList, startDate, endDate);
			wrapper.setCoverageDataAnalysis(coverageDataAnalysisList);
			logger.debug("coverageDataAnalysis List {} ",new Gson().toJson(coverageDataAnalysisList));
			
			List<StealthWOWPTAnalysisWrapper> wptAnalysisDataList = nvHbaseService.getWptAnalysisDataList(hbaseResultList,workorderId.toString(), taskIdList, null, startDate, endDate, woFrequency);
			wrapper.setWptDataAnalysisList(wptAnalysisDataList);
			logger.debug("wptAnalysisDataList List {} ",new Gson().toJson(wptAnalysisDataList));
			
			List<StealthWOKpiAnalysisWrapper> kpiAnalysisDataList = nvHbaseService.getKpiAnalysisDataList(hbaseResultList,workorderId.toString(), taskIdList, null, startDate, endDate, woFrequency);
			wrapper.setKpiDataAnalysisList(kpiAnalysisDataList);
			logger.debug("kpiAnalysisDataList List {} ",new Gson().toJson(wptAnalysisDataList));
			
			List<StealthWOPingAnalysisWrapper> pingAnalysisDataList = nvHbaseService.getPingAnalysisDataList(hbaseResultList ,workorderId.toString(), taskIdList, null, startDate, endDate, woFrequency);
			wrapper.setPingDataAnalysisList(pingAnalysisDataList);
			logger.debug("pingAnalysisDataList List {} ",new Gson().toJson(pingAnalysisDataList));	
			
			List<StealthWOYoutubeAnalysisWrapper> youtubeDataAnalysisList = nvHbaseService.getYoutubeAnalysisData(hbaseResultList,workorderId, taskIdList, startDate, endDate, woFrequency);
			wrapper.setYoutubeDataAnalysisList(youtubeDataAnalysisList);
			logger.debug("youtubeDataAnalysis List {} ",new Gson().toJson(youtubeDataAnalysisList));
			
			List<StealthWOBestWorstDataWrapper> bestWorstGeographyWrapperList  = getBestWorstGeographyDataList(hbaseResultList,workorderId.intValue(), taskIdList, startDate, endDate);
			wrapper.setBestWorstDataList(bestWorstGeographyWrapperList);
			logger.debug("bestWorstGeographyWrapperList {} ",new Gson().toJson(bestWorstGeographyWrapperList));
			return wrapper;
		} catch (Exception e) {
			logger.error("Exception occured inside FinalDataWrapperForJapser method ",Utils.getStackTrace(e));
			throw new ServiceException("Unable to Generate report due to {} "+ ExceptionUtils.getStackTrace(e));
		}
	}

	private List<StealthWOCoverageAnalysisWrapper> getCoverageDataOfStealth(List<HBaseResult> hbaseResultList, Integer workorderId, List<Integer> taskIdList, Long startDate, Long endDate) {
		List<StealthWOCoverageAnalysisWrapper> list = new ArrayList<>();
		List<KPIWrapper> kpiList =  getKpiListWraperToProcess(ReportConstants.STEALTH);
		for (KPIWrapper kpiWrapper : kpiList) {
			StealthWOCoverageAnalysisWrapper analysisWrapper = null;
			List<StealthWOCoverageGraphDataWrapper> stealthCoverageList = getCoverageGraphData(hbaseResultList,workorderId, kpiWrapper, taskIdList, startDate, endDate);
			if(stealthCoverageList!=null && stealthCoverageList.size()>ReportConstants.INDEX_ZER0){
				analysisWrapper = new StealthWOCoverageAnalysisWrapper();
				analysisWrapper.setCoverageDataList(stealthCoverageList);
				list.add(analysisWrapper);
			}
		}
		return list;
	}


	private List<StealthWOCoverageGraphDataWrapper> getCoverageGraphData(List<HBaseResult> hbaseResultList, Integer workorderId, KPIWrapper kpiWrapper, List<Integer> taskIdList, Long startDate, Long endDate) {
		List<StealthWOCoverageGraphDataWrapper> listOfGraphDataWrapper = null;
		StealthWOCoverageGraphDataWrapper wrapper = null;
			List<GraphDataWrapper> graphDatalist = getStealthGraphDataList(hbaseResultList,kpiWrapper,workorderId, taskIdList, startDate, endDate);
			if(graphDatalist!=null && !graphDatalist.isEmpty()){
				Collections.sort(graphDatalist, (GraphDataWrapper o1, GraphDataWrapper o2) -> o1.getFrom().compareTo(o2.getFrom()));
				listOfGraphDataWrapper = new ArrayList<>();
				wrapper = new StealthWOCoverageGraphDataWrapper();
				wrapper.setKpiName(kpiWrapper.getKpiName().replaceAll(ReportConstants.UNDERSCORE, ReportConstants.SPACE) + " (" + ReportUtil.getUnitByKPiName(kpiWrapper.getKpiName()).trim() + ")");
				wrapper.setPageTitle(kpiWrapper.getKpiName().toUpperCase().replace(ReportConstants.UNDERSCORE, ReportConstants.SPACE));
				wrapper.setGraphDataList(graphDatalist);
				listOfGraphDataWrapper.add(wrapper);
			}
		return listOfGraphDataWrapper;
	}


	private List<GraphDataWrapper> getStealthGraphDataList(List<HBaseResult> hbaseResultList, KPIWrapper kpiWrapper, Integer workorderId, List<Integer> taskIdList, Long startDate, Long endDate) {
		List<GraphDataWrapper> listOfGraphData = new ArrayList<>();
		try {
			List<HBaseResult> stealthHbaseData = ReportUtil.filterStealthDataByDate(startDate, endDate, hbaseResultList);
			Map<String, Double> kpiDataMap = getFinalJsonMapData(stealthHbaseData, getColumnNameForHbase(kpiWrapper.getKpiName()));
			List<Double> deviceWiseKpiData = getdeviceWiseStealthKPIAvg(stealthHbaseData, getColumnNameForHbase(kpiWrapper.getKpiName()));
			int counter = 1;
			if (kpiDataMap != null) {
				if(kpiDataMap.get(ReportConstants.COUNT.toLowerCase()) != null && kpiDataMap.get(ReportConstants.COUNT.toLowerCase()) != 0.0){
						List<RangeSlab> rangeSlabList = ReportUtil.getSortedRangeSlabList(kpiWrapper.getRangeSlabs());
						for (RangeSlab rangeSlab : rangeSlabList) {
							GraphDataWrapper graphData = setGraphDataWrapper(kpiWrapper, kpiDataMap, deviceWiseKpiData,
									counter, rangeSlab);
							listOfGraphData.add(graphData);
							counter++;
						}
					} else {
					return Collections.emptyList();
				}
			}
		} catch (Exception e) {
			logger.error("Error in processing the graph data for  kpi {} ,{}  ",kpiWrapper.getKpiName(),Utils.getStackTrace(e));
		}
		return listOfGraphData;
	}

	private GraphDataWrapper setGraphDataWrapper(KPIWrapper kpiWrapper, Map<String, Double> kpiDataMap,
			List<Double> deviceWiseKpiData, int counter, RangeSlab rangeSlab) {
		GraphDataWrapper graphData = new GraphDataWrapper();
		graphData.setFrom(rangeSlab.getLowerLimit());
		graphData.setTo(rangeSlab.getUpperLimit());
		graphData.setKpiName(kpiWrapper.getKpiName() + "(" + ReportUtil.getUnitByKPiName(kpiWrapper.getKpiName()) + ")");
		Double count = kpiDataMap.get(ReportConstants.COLUMN_FAMILY_R + counter) != null
				? Double.parseDouble(kpiDataMap.get(ReportConstants.COLUMN_FAMILY_R + counter).toString()) : null;
		if(count != null) {
			Double percentage =ReportUtil.getPercentage(count, kpiDataMap.get(ReportConstants.COUNT));
			graphData.setValue(ReportUtil.round(percentage, ReportConstants.INDEX_TWO));
		}
		if(deviceWiseKpiData != null && !deviceWiseKpiData.isEmpty()) {
			int devicesInRange = deviceWiseKpiData.stream().filter(d -> d >= rangeSlab.getLowerLimit() && d < rangeSlab.getUpperLimit()).collect(Collectors.toList()).size();
			Double percentage =ReportUtil.getPercentage(devicesInRange, deviceWiseKpiData.size());
			graphData.setDeviceCount(ReportUtil.round(percentage, ReportConstants.INDEX_TWO));
		}
		return graphData;
	}

	private String getColumnNameForHbase(String kpiName) {
		String name="";
		if(kpiName.equalsIgnoreCase(ReportConstants.RSRP)){
			name= kpiName.toLowerCase();
		}else if(kpiName.equalsIgnoreCase(ReportConstants.SINR)){
			name= kpiName.toLowerCase();
		}else if(kpiName.equalsIgnoreCase(ReportConstants.UL_THROUGHPUT)){
			name= "ul";
		}else if(kpiName.equalsIgnoreCase(ReportConstants.DL_THROUGHPUT)){
			name= "dl";
		}
		name=name+"Json";
		logger.info("Going to return the name {} ",name);
		return name;
	}

	private List<KPIWrapper> getKpiListWraperToProcess(String reportType) {
		List<LegendWrapper> legendList = legendRangeDao.findAllLegendRangesAppliedTo(reportType);
		return ReportUtil.convertLegendsListToKpiWrapperList(legendList, DriveHeaderConstants.getKpiIndexMapFORSsvT());
	}

	private List<StealthWOBestWorstDataWrapper> getBestWorstGeographyDataList(List<HBaseResult> hbaseResultList, int workorderId, List<Integer> taskIdList, Long startDate, Long endDate) {
		List<StealthWOBestWorstDataWrapper> bestWorstGeographyWrapperList = new ArrayList<>();
		StealthWOBestWorstDataWrapper wrapper = new StealthWOBestWorstDataWrapper();
		List<StealthWOBestWorstItemWrapper> listOfBestWorstItemWrapper = getRsrpWiseSortedDataList(hbaseResultList,workorderId, taskIdList, startDate, endDate);
		if (!listOfBestWorstItemWrapper.isEmpty()) {
			listOfBestWorstItemWrapper.removeIf(data -> data.getRsrp() == null);
			List<StealthWOBestWorstItemWrapper> shallowCopy = new ArrayList<>();
			shallowCopy.addAll(listOfBestWorstItemWrapper);
			Comparator<StealthWOBestWorstItemWrapper> rsrpComparator = getBestWorstRSRPComparator();
			Collections.sort(listOfBestWorstItemWrapper, rsrpComparator);
			Collections.sort(shallowCopy, rsrpComparator);
			Collections.reverse(listOfBestWorstItemWrapper);
			if (listOfBestWorstItemWrapper.size() > ReportConstants.INDEX_FIVE) {
				wrapper.setTopDataList(listOfBestWorstItemWrapper.subList(ReportConstants.INDEX_ZER0, ReportConstants.INDEX_FIVE));
				wrapper.setBottomDataList(shallowCopy.subList(ReportConstants.INDEX_ZER0, ReportConstants.INDEX_FIVE));
			} else {
				wrapper.setTopDataList(listOfBestWorstItemWrapper.subList(ReportConstants.INDEX_ZER0, listOfBestWorstItemWrapper.size()));
				wrapper.setBottomDataList(shallowCopy.subList(ReportConstants.INDEX_ZER0, shallowCopy.size()));
			}
		}
		bestWorstGeographyWrapperList.add(wrapper);
		return bestWorstGeographyWrapperList;
	}
	
	private Comparator<StealthWOBestWorstItemWrapper> getBestWorstRSRPComparator(){
		return new Comparator<StealthWOBestWorstItemWrapper>() {
			@Override
			public int compare(StealthWOBestWorstItemWrapper o1, StealthWOBestWorstItemWrapper o2) {
				if (o1 != null && o1.getRsrp() != null && o2 != null && o2.getRsrp() != null) {
					return o1.getRsrp().compareTo(o2.getRsrp());
				} else {
					return -1;
				}
			}
		};
	}

	private String getAvgValueForKpiFromData(int index, List<String[]> value) {
		OptionalDouble dlAvgValue = value	.stream()
											.filter(Objects::nonNull)
											.filter(array -> (array.length > index && array[index] != null && !array[index].equalsIgnoreCase(Symbol.NULL_STRING) && !array[index].isEmpty() && NumberUtils.isCreatable(array[index])))
											.map(array -> array[index])
											.filter(e -> (e != null && !e.equalsIgnoreCase(Symbol.NULL_STRING) && e.trim().length() > 0))
											.mapToDouble(Double::parseDouble)
											.average();
		Double avgValue = dlAvgValue != null && dlAvgValue.isPresent() ? dlAvgValue.getAsDouble() : null;
		avgValue = ReportUtil.parseToFixedDecimalPlace(avgValue, ReportConstants.INDEX_TWO);
		return avgValue != null ? avgValue.toString() : null;
	}

	private List<StealthWOSummaryWrapper> getStealthSummaryWrapper(Integer workorderId, List<Integer> taskIdList, Long startDate, Long endDate, String type) {
		List<StealthWOSummaryWrapper>  listOfStealthSummary = new ArrayList<>();
			List<Map<String, Long>>  stealthSummaryList = getStealthWoSummary(workorderId, taskIdList, startDate, endDate, type);
			listOfStealthSummary.add(getStealthWOSummaryWrappeNew(stealthSummaryList, type));
			return listOfStealthSummary;
	}

	private StealthWOSummaryWrapper getStealthWOSummaryWrappeNew(List<Map<String, Long>> stealthSummaryList, String type) {
		StealthWOSummaryWrapper smmryWrapper = new StealthWOSummaryWrapper();
		if(stealthSummaryList!=null && !stealthSummaryList.isEmpty()){
			smmryWrapper.setAccepted(getCountByStatus(stealthSummaryList,Arrays.asList(ReportConstants.ACCEPTED.toUpperCase())));
			smmryWrapper.setRejected(getCountByStatus(stealthSummaryList,Arrays.asList(ReportConstants.REJECTED.toUpperCase())));
			smmryWrapper.setPending(getCountByStatus(stealthSummaryList,Arrays.asList(ReportConstants.PENDING.toUpperCase())));
			smmryWrapper.setAssigned(getCountByStatus(stealthSummaryList,Arrays.asList(ReportConstants.ACCEPTED.toUpperCase()
					,ReportConstants.REJECTED.toUpperCase(),ReportConstants.PENDING.toUpperCase())));
			sortListOntheBasisOfTime(stealthSummaryList);
			getDateWiseStausList(stealthSummaryList,smmryWrapper, type);
		}
		return smmryWrapper;
	}

	private StealthWOSummaryWrapper getDateWiseStausList(List<Map<String, Long>> stealthSummaryList,
			StealthWOSummaryWrapper smmryWrapper, String type) {
		List<StealthDateWiseStatusWrapper> dateWiseStatusList = new ArrayList<>();
		stealthSummaryList.forEach(map -> {
			StealthDateWiseStatusWrapper dateWiseCountWrapper = new StealthDateWiseStatusWrapper();
			dateWiseCountWrapper.setDate(Utils.parseDateToString(new Date(map.get(ReportConstants.TIME.toUpperCase())),
					getDateFormatForSummaryDetail(type)));
			dateWiseCountWrapper.setSuccess(map	.get(ReportConstants.SUCCESS.toUpperCase())
												.intValue());
			dateWiseCountWrapper.setFailure(map	.get(ReportConstants.FAILURE.toUpperCase())
												.intValue());
			dateWiseCountWrapper.setInProgress(map	.get(ReportConstants.INPROGRESS.toUpperCase())
													.intValue());
			dateWiseCountWrapper.setClosed(map	.get(ReportConstants.CLOSED.toUpperCase())
												.intValue());
			setSummaryPercentageToWrapper(dateWiseCountWrapper, map);
			dateWiseStatusList.add(dateWiseCountWrapper);
		});
		smmryWrapper.setDateWiseStatusList(dateWiseStatusList);
		return smmryWrapper;
	}
	
	public static String getDateFormatForSummaryDetail(String type) {
		return type != null && type.equals(NVConstant.HOURLY) ? ReportConstants.STEALTH_DATE_FORMAT_FOR_REPORT : ReportConstants.DATE_FORMAT_DD_MM_YY;
	}
	
	private void setSummaryPercentageToWrapper(StealthDateWiseStatusWrapper dateWiseCountWrapper, Map<String, Long> summaryMap) {
		Long totalCount = getTotalStatusCount(summaryMap);
		dateWiseCountWrapper.setSuccessPercent(ReportUtil.getPercentage(summaryMap.get(ReportConstants.SUCCESS.toUpperCase()).intValue(), totalCount.intValue()));
		dateWiseCountWrapper.setFailurePercent(ReportUtil.getPercentage(summaryMap.get(ReportConstants.FAILURE.toUpperCase()).intValue(), totalCount.intValue()));
		dateWiseCountWrapper.setInProgressPercent(ReportUtil.getPercentage(summaryMap.get(ReportConstants.INPROGRESS.toUpperCase()).intValue(), totalCount.intValue()));
		dateWiseCountWrapper.setClosedPercent(ReportUtil.getPercentage(summaryMap.get(ReportConstants.CLOSED.toUpperCase()).intValue(), totalCount.intValue()));
	}
	
	private long getTotalStatusCount(Map<String, Long> summaryMap) {
		return getValueIfValid(summaryMap, ReportConstants.SUCCESS.toUpperCase()) 
				+ getValueIfValid(summaryMap, ReportConstants.FAILURE.toUpperCase())
				+ getValueIfValid(summaryMap, ReportConstants.INPROGRESS.toUpperCase())
				+ getValueIfValid(summaryMap, ReportConstants.CLOSED.toUpperCase());
	}

	private long getValueIfValid(Map<String, Long> summaryMap, String key) {
		return summaryMap != null && summaryMap.containsKey(key) && summaryMap.get(key)!= null ? summaryMap.get(key) : 0L;
	}
	
	private List<Map<String, Long>> sortListOntheBasisOfTime(List<Map<String, Long>> stealthSummaryList) {
		Comparator<Map<String, Long>> mapComparator = new Comparator<Map<String, Long>>() {
		    public int compare(Map<String, Long> m1, Map<String, Long> m2) {
		        return m1.get(ReportConstants.TIME.toUpperCase()).compareTo(m2.get(ReportConstants.TIME.toUpperCase()));
		    }
		};
		Collections.sort(stealthSummaryList, mapComparator);
		return stealthSummaryList;
	}

	private String getCountByStatus(List<Map<String, Long>> stealthSummaryList, List<String> statusList) {
		 AtomicInteger atomicInteger = new AtomicInteger(ReportConstants.INDEX_ZER0);
		 IntStream.range(ReportConstants.INDEX_ZER0, ReportConstants.INDEX_ONE).forEach(index->{
			 for (String status : statusList) {
					atomicInteger.addAndGet(stealthSummaryList.get(index).get(status).intValue());
				}
		 });
		return atomicInteger.get()+ReportConstants.BLANK_STRING;
	}

	private List<StealthWODetailsWrapper> getStealthWoDetailWrapper(GenericWorkorder genericWorkorder) throws ServiceException {
		logger.info("Inside method getStealthWoDetailWrapper for workOrder Id {} ",genericWorkorder.getId());
		List<StealthWODetailsWrapper> list = new ArrayList<>();
		StealthWODetailsWrapper wrapper = getStealthWoDetailWrapperData(genericWorkorder);
		list.add(wrapper);
		return list;
	}

	private StealthWODetailsWrapper getStealthWoDetailWrapperData(GenericWorkorder genericWorkorder) throws ServiceException {
		StealthWODetailsWrapper detailWrapper = new StealthWODetailsWrapper();
		List<StealthWODetailItemWrapper> detailItemList = new ArrayList<>();
		if (genericWorkorder != null) {
			detailItemList.add(getItemData(ReportConstants.STEALTH_KEY_USER_NAME,genericWorkorder.getAssignedTo()!=null?genericWorkorder.getAssignedTo().getUserName():ReportConstants.HIPHEN));
			detailItemList.add(getItemData(ReportConstants.STEALTH_KEY_DATE,Utils.parseDateToString(genericWorkorder.getCreationTime(),ReportConstants.DATE_FORMAT_DD_MM_YY)));
			detailItemList.add(getItemData(ReportConstants.STEALTH_KEY_WORKORDER_NAME,genericWorkorder.getWorkorderName()));
			detailItemList.add(getItemData(ReportConstants.STEALTH_KEY_WO_SCHEDULE_TYPE, genericWorkorder.getGwoMeta().get(ReportConstants.META_KEY_WO_FREQUENCY)));
			if (genericWorkorder.getGwoMeta().containsKey(ReportConstants.META_KEY_FREQUENCY)) {
				detailItemList.add(getItemData(ReportConstants.STEALTH_KEY_NO_OF_EXECUTIONS,genericWorkorder.getGwoMeta().get(ReportConstants.META_KEY_FREQUENCY)));
			}
			if(ReportConstants.STEALTH_META_FREQUENCY_TYPE_WEEKLY.equalsIgnoreCase(genericWorkorder.getGwoMeta().get(ReportConstants.META_KEY_WO_FREQUENCY))) {
				detailItemList.add(getItemData(ReportConstants.STEALTH_KEY_WO_EXECUTION_DAY_WEEKLY, Utils.parseDateToString(genericWorkorder.getStartDate(),ReportConstants.DATE_FORMAT_DAY_NAME)));
			}
			setExecutionDateAndTimeInWODetail(detailItemList, genericWorkorder);
			if(genericWorkorder.getDueDate() != null) {
				detailItemList.add(getItemData(ReportConstants.STEALTH_KEY_END_DATE, ReportUtil.getFormattedDate(genericWorkorder.getDueDate(), ReportConstants.DATE_FORMAT_DD_MM_YYYY)));
			}
			detailWrapper.setWoDetailsList(detailItemList);
			return detailWrapper;
		} else {
			throw new ServiceException("No workorder Data Available in method getStealthWoDetailWrapperData {} ");
		}
	}
	
	private void setExecutionDateAndTimeInWODetail(List<StealthWODetailItemWrapper> detailItemList, GenericWorkorder genericWorkorder) {		
		Date executionDateTime = genericWorkorder.getStartDate();
		detailItemList.add(getItemData(ReportConstants.STEALTH_KEY_EXECUTION_TIME, ReportUtil.getFormattedDate(executionDateTime, ReportConstants.DATE_FORMAT_HH_MM_SS)));
		detailItemList.add(getItemData(ReportConstants.STEALTH_KEY_EXECUTION_DATE, ReportUtil.getFormattedDate(executionDateTime, ReportConstants.DATE_FORMAT_DD_MM_YYYY)));
	}

	private StealthWODetailItemWrapper getItemData(String label,String value) {
		StealthWODetailItemWrapper wrapper = new StealthWODetailItemWrapper();
		wrapper.setItemLabel(label);
		wrapper.setItemValue(value);
		return wrapper;
	}

	private File proceedToCreateReport(Integer analyticsRepositoryId,GenericWorkorder genericWorkorder, StealthWorkorderReportWrapper mainWrapper, Map<String, Object> parameterMap,
			String fileName, String jasperMainFile) {
		try {
			List<StealthWorkorderReportWrapper> dataSourceList = new ArrayList<>();
			dataSourceList.add(mainWrapper);
			JRBeanCollectionDataSource rfbeanColDataSource = new JRBeanCollectionDataSource(dataSourceList);
			String REPORT_ASSET_REPO = ConfigUtils.getString(ReportConstants.STEALTH_WO_REPORT_JASPER_FILE_PATH);
			parameterMap.put(ReportConstants.SUBREPORT_DIR, REPORT_ASSET_REPO);
			parameterMap.put(ReportConstants.KEY_WELCOME_IMAGE, REPORT_ASSET_REPO + ReportConstants.IMAGE_FIRST_IMG);
			parameterMap.put(ReportConstants.KEY_HEADER_IMAGE,
					REPORT_ASSET_REPO + ReportConstants.IMAGE_HEADER_BG);
			parameterMap.put(ReportConstants.KEY_TITLE_IMAGE,
					REPORT_ASSET_REPO + ReportConstants.IMAGE_SCREEN_BG);

			JasperRunManager.runReportToPdfFile(REPORT_ASSET_REPO + jasperMainFile, fileName, parameterMap,
					rfbeanColDataSource);

			//temporary to support download from workorder page and BI
			if(analyticsRepositoryId!=null) {
				String hdfsFilePath =
						ConfigUtils.getString(ReportConstants.NV_REPORTS_PATH_HDFS) + ReportConstants.STEALTH + ReportConstants.FORWARD_SLASH;
				logger.info("HDFS file path is : {}", hdfsFilePath);
				 reportService.saveFileAndUpdateStatus(analyticsRepositoryId, hdfsFilePath,
						genericWorkorder, ReportUtil.getIfFileExists(fileName), null,NVWorkorderConstant.REPORT_INSTACE_ID);
				
				 return null;
			}
			else {
			return ReportUtil.getIfFileExists(fileName);
			}
		} catch (JRException e) {
			logger.error("Error inside proceedToCreateReport method in StealthReportServiceImpl{} ",
					Utils.getStackTrace(e));
		}
		return null;
	}
	
	public List<Map<String,Long>> getStealthWoSummary(Integer workorderId, List<Integer> taskIdList, Long startDate, Long endDate, String type) {
		try {
			logger.info("Going to getStealthWoSummary for woId: {}",workorderId);
			List<StealthWOWrapper> resultList = null;
			if(taskIdList != null && !taskIdList.isEmpty()) {
				resultList = stealthTaskResultDao.getStatusSummaryForReport(workorderId, taskIdList);
			} else {
				resultList = stealthTaskResultDao.getStatusSummary(workorderId,type);
			}
			List<StealthWOWrapper> taskList =stealthTaskDetailDao.getAcknowledgementSummary(workorderId, type);
			logger.info("result List: {}",new Gson().toJson(resultList));
			logger.info("task List: {}",new Gson().toJson(taskList));
			logger.info("Going to getStealthWoSummary for woId: {}",workorderId);
			return StealthUtils.getStealthResponseToReturn(taskList, resultList, startDate, endDate, type);
		} catch (Exception e) {
			logger.error(NVWorkorderConstant.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
		}
		return Collections.emptyList();
	}
	
	private List<StealthWOBestWorstItemWrapper> getRsrpWiseSortedDataList(List<HBaseResult> hbaseResultList, Integer workorderId, List<Integer> taskIdList, Long startDate, Long endDate) {
		List<StealthWOBestWorstItemWrapper> bestWorstItemList = new ArrayList<>();
		Map<String,List<String[]>> geographyWiseMap = nvHbaseService.getGeographyWiseData(hbaseResultList,workorderId.toString(), taskIdList, null, QMDLConstant.JSON, startDate, endDate);	
			if(geographyWiseMap!=null && !geographyWiseMap.isEmpty()){
			geographyWiseMap.forEach((key,value)->{
				if (key != null && !key.equalsIgnoreCase(ReportConstants.BLANK_STRING) && value != null
						&& !value.isEmpty()) {
					StealthWOBestWorstItemWrapper innerWrapper = new StealthWOBestWorstItemWrapper();
					innerWrapper.setClusterName(key);
					innerWrapper
							.setDlThroughput(getAvgValueForKpiFromData(ReportConstants.STEALTH_JSON_DL_INDEX, value)); // dlINdex
					String averageRsrp = getAvgValueForKpiFromData(ReportConstants.STEALTH_JSON_RSRP_INDEX, value);
					innerWrapper.setRsrp(NumberUtils.isParsable(averageRsrp) ? Double.parseDouble(averageRsrp) : null); // rsrpIndex
					innerWrapper.setTtl(getAvgValueForKpiFromData(ReportConstants.STEALTH_JSON_TTL_INDEX, value)); // ttlIndex
					bestWorstItemList.add(innerWrapper);
				}
			});
		}
		return bestWorstItemList;
	}
	
	public Map<String, Double> getFinalJsonMapData(List<HBaseResult> listResults, String columnName) {
		Map<String, Double> finalJsonDataMap = new HashMap<>();
		if (listResults != null && !listResults.isEmpty()) {
			for (HBaseResult result : listResults) {
				try {
					String json = result.getString(columnName.getBytes());
					if(json!=null){
						Map<String, Object> jsonMap = new ObjectMapper().readValue(json, HashMap.class);
						jsonMap.forEach((k, v) -> functionByKey(k,v,finalJsonDataMap));
					}
				} catch (Exception e) {
					logger.error("Exception insdie method finalJsonDataMap {} ",Utils.getStackTrace(e));
				}
			}
		}
		
//		logger.info("FINAL JSON DATA MAP for kpi {} , {} ",finalJsonDataMap,columnName);
		return finalJsonDataMap;
	}
	
	private Map<String, Double>  functionByKey(String key, Object value, Map<String, Double> finalJsonDataMap) {
		if(key.equalsIgnoreCase(ReportConstants.R1) || key.equalsIgnoreCase(ReportConstants.R2) ||
				key.equalsIgnoreCase(ReportConstants.R3) || key.equalsIgnoreCase(ReportConstants.R4) ||
				key.equalsIgnoreCase(ReportConstants.R5) || key.equalsIgnoreCase(ReportConstants.R6) ||
				key.equalsIgnoreCase(ReportConstants.R7)){
			finalJsonDataMap.merge(key, value!=null?Double.parseDouble(value.toString()):null, Double::sum);
		}else if(key.equalsIgnoreCase(ReportConstants.MIN)){
			finalJsonDataMap.merge(key, value!=null?Double.parseDouble(value.toString()):null, Double::min);
		}else if(key.equalsIgnoreCase(ReportConstants.MAX)){
			finalJsonDataMap.merge(key, value!=null?Double.parseDouble(value.toString()):null, Double::max);
		}else if(key.equalsIgnoreCase(ReportConstants.AVG)){
			finalJsonDataMap.put(key, finalJsonDataMap.get(key)!=null?(finalJsonDataMap.get(key)+Double.parseDouble(value.toString()))/2.0:Double.parseDouble(value.toString()));	
		}else if(key.equalsIgnoreCase(ReportConstants.COUNT)){
			finalJsonDataMap.merge(key, value!=null?Double.parseDouble(value.toString()):null, Double::sum);
		}
		return finalJsonDataMap;
	}
	
	private List<Double> getdeviceWiseStealthKPIAvg(List<HBaseResult> hbaseResultList, String columnName) {
		logger.info("Inside method getdeviceWiseStealthKPIAvg hbaseList Size {}, columnName {}", hbaseResultList.size(), columnName);
		Map<String, List<HBaseResult>> rowKeyMap = new HashMap<>();
		for(HBaseResult hbaseResult : hbaseResultList) {
			String deviceId = hbaseResult.getString(ReportConstants.STEALTH_DETAIL_DEVICE_ID_COLUMN_KEY.getBytes());
			if (deviceId != null && !deviceId.isEmpty()) {
				if (rowKeyMap.containsKey(deviceId)) {
					rowKeyMap.get(deviceId).add(hbaseResult);
				} else {
					List<HBaseResult> hbaseList = new ArrayList<>();
					hbaseList.add(hbaseResult);
					rowKeyMap.put(deviceId, hbaseList);
				}
			}
		}
		List<Double> deviceAverageList = new ArrayList<>();
		for(Map.Entry<String, List<HBaseResult>> entry : rowKeyMap.entrySet()) {
			deviceAverageList.add(getDeviceKpiAverageFromHBaseResult(entry.getValue(), columnName));
		}
		return deviceAverageList;
	}
	
	private Double getDeviceKpiAverageFromHBaseResult(List<HBaseResult> hBaseResultList, String columnName) {
		Double avg = 0.0;
		int count = 0;
		try {
			for (HBaseResult hBaseResult : hBaseResultList) {
				if (hBaseResult != null) {
					String json = hBaseResult.getString(columnName.getBytes());
					if (json != null && !json.isEmpty()) {
						Map<String, Object> jsonMap = new ObjectMapper().readValue(json, HashMap.class);
						if (jsonMap != null && jsonMap.containsKey(ReportConstants.AVG)) {
							avg += (Double) jsonMap.get(ReportConstants.AVG);
							count++;
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("Exception inside method getDeviceKpiAverageFromHBaseResult {} ", Utils.getStackTrace(e));
		}
		return count != 0 ? avg/count : 0.0;
	}

	@Override
	public String processWOCsvDump(String workorderId) {
		try {
			String columnName = ConfigUtils.getString(QMDLConstant.STEALTH_DATA_DUMP_COLUMN_NAME);
			List<HBaseResult> hBaseResultList = nvHbaseService.getHBaseResultListForStealthReport(workorderId, null,
					null, columnName);

			String filePath = ConfigUtils.getString(ReportConstants.NV_REPORTS_PATH_HDFS) + ReportConstants.STEALTH;

			String fileName = NVLayer3Constants.REPORT_NAME_PREFIX + workorderId + Symbol.UNDERSCORE_STRING
					+ System.currentTimeMillis();
			
			FSDataOutputStream outStream = nvReportHdfsDao.getHDFSOutputStream(filePath, fileName + ReportConstants.DOT_ZIP);
			if(writeHbaseResultToHDFSFile(columnName, hBaseResultList, outStream, fileName + ReportConstants.DOT_CSV)) {
				Map<String, String> responseMap = new HashMap<>();
				responseMap.put(ReportConstants.KEY_FILE_PATH, fileName + ReportConstants.DOT_ZIP);
				return new Gson().toJson(responseMap);
			}
		} catch (Exception e) {
			logger.error("Exception inside method processWOCsvDump {} ", Utils.getStackTrace(e));
		}
		return null;
	}

	private boolean writeHbaseResultToHDFSFile(String columnName, List<HBaseResult> hBaseResultList,
			FSDataOutputStream outStream, String csvFileName) {
		try {
			boolean isheaderAdded = false;
			if (hBaseResultList != null && !hBaseResultList.isEmpty()) {
				StringBuilder sb = new StringBuilder();
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				ZipOutputStream zos = new ZipOutputStream(out);
				zos.putNextEntry(new ZipEntry(csvFileName));
				for (HBaseResult result : hBaseResultList) {
					if (columnName == null || QMDLConstant.RAW_DATA.equalsIgnoreCase(columnName)) {
						String strResult = result.getString(QMDLConstant.RAW_DATA.getBytes());
						if (strResult != null && !strResult.isEmpty()) {
							sb.append(strResult).append(QMDLConstant.NEXT_LINE);
						}
					} else {
						String strResult = result.getString(columnName.getBytes());
						if (strResult != null && !strResult.isEmpty()) {
							if (!isheaderAdded) {
								String csvHeaders = ConfigUtils.getString(QMDLConstant.STEALTH_DUMP_HEADERS)
															   .replaceAll(Symbol.UNDERSCORE_STRING,
																	   Symbol.COMMA_STRING);
								sb.append(csvHeaders).append(NVLayer3Constants.NEW_LINE_SEPERATOR);
								isheaderAdded = true;
							}
							String headers = result.getString(QMDLConstant.STEALTH_HEADERS_COLUMNS_NAME.getBytes());
							String csvContent = ReportUtil.converToCSVStringFromArrayString(strResult);
							csvContent = ReportUtil.addHeadersDataToCsv(headers, csvContent);
							csvContent = ReportUtil.addDateToStealthCSVData(csvContent);
							sb.append(csvContent);
						}
					}
					writeDataToZipOutputStream(zos, new ByteArrayInputStream(sb.toString().getBytes()));
					sb = new StringBuilder();
				}
				zos.closeEntry();
				zos.close();
				if(out != null) {
					outStream.write(out.toByteArray());
				}
			}
			if (outStream != null) {
				outStream.close();
			}
			return true;
		} catch (Exception e) {
			logger.error("Exception inside method writeHbaseResultToHDFSFile {} ", Utils.getStackTrace(e));
		}
		return false;
	}

	@Override
	public Response getFileForDownloadFromHDFS(String fileName) {
		try {
			
			String filePath = ConfigUtils.getString(ReportConstants.NV_REPORTS_PATH_HDFS) + ReportConstants.STEALTH
					+ ReportConstants.FORWARD_SLASH+fileName ;
			byte[] reportData = nvReportHdfsDao.getInputStreamFromHdfs(null, filePath);
			if(reportData != null && reportData.length > 0){
				Response.ResponseBuilder builder = Response.status(200);
				builder = builder.entity(reportData)
						.header(ForesightConstants.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM)
						.header(ForesightConstants.CONTENT_DISPOSITION,
								NVLayer3Constants.ATTACHMENT_FILE + fileName + "\"");
				return builder.build();
			}else{
				return  Response.ok(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
			}
		} catch (Exception e) {
			logger.error("Exception inside method getWOCsvDump {} ", Utils.getStackTrace(e));
		}
		return null;
	}
	
	private void writeDataToZipOutputStream(ZipOutputStream zos,InputStream in) throws IOException {  
		final int BUFFER = 2048;
		byte[] buffer = new byte[BUFFER];
		int length;
		while ((length = in.read(buffer)) >= 0) {
			zos.write(buffer, 0, length);
		}
	}

	@Override
	public String processStealthWOPdfForTaskId(String workorderId, List<Integer> taskIdList, Long startDate, Long endDate) {
		try {
			File file = getPDFReportForStealthWO(workorderId, taskIdList, startDate, endDate,null);
			String mainFilePath = ConfigUtils.getString(ReportConstants.NV_REPORTS_PATH_HDFS) + ReportConstants.STEALTH
					+ ReportConstants.FORWARD_SLASH + ReportConstants.PDF_REPORT + ReportConstants.FORWARD_SLASH;
			if(nvReportHdfsDao.saveFileToHdfs(file, mainFilePath + file.getName())) {
				Map<String, String> fileJsonMap = new HashMap<>();
				fileJsonMap.put(ReportConstants.KEY_FILE_PATH, mainFilePath + file.getName());
				return new Gson().toJson(fileJsonMap);
			}
		} catch (ServiceException e) {
			logger.error("Exception inside method processStealthWOPdfForTaskId {} ", Utils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public Response execute(String json) {
		logger.info("Inside execute method for Stealth Report with json {} ", json);
		Integer analyticrepositoryId = null;
		try {
			Map<String, Object> jsonMap = reportService.getJsonDataMap(json);
			Integer workorderId = (Integer) jsonMap.get(ReportConstants.WORKORDER_ID);
			if (workorderId != null) {
				analyticrepositoryId = (Integer) jsonMap.get(ReportConstants.ANALYTICS_REPOSITORY_ID);
				getPDFReportForStealthWO(workorderId.toString(), new ArrayList<Integer>(), null, null,
						analyticrepositoryId);
				return Response.ok(ForesightConstants.SUCCESS_JSON).build();
			}
		} catch (Exception e) {

			logger.info("Exception while processing pdf for Stealth {}", Utils.getStackTrace(e));

		}
		return Response.ok(ForesightConstants.FAILURE_JSON).build();
	}

}

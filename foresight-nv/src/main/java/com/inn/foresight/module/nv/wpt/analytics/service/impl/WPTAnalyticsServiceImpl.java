package com.inn.foresight.module.nv.wpt.analytics.service.impl;

import java.io.File;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.inn.commons.Symbol;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.io.FileUtils;
import com.inn.commons.lang.NumberUtils;
import com.inn.commons.lang.StringUtils;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.app.constants.AppConstants;
import com.inn.foresight.module.nv.report.utils.ReportUtil;
import com.inn.foresight.module.nv.reportgeneration.wrapper.NVReportWrapper;
import com.inn.foresight.module.nv.workorder.stealth.constants.StealthConstants;
import com.inn.foresight.module.nv.wpt.analytics.dao.ICategorySiteAggDetailDao;
import com.inn.foresight.module.nv.wpt.analytics.dao.IWPTHbaseDao;
import com.inn.foresight.module.nv.wpt.analytics.dao.IWebCategoryDao;
import com.inn.foresight.module.nv.wpt.analytics.dao.IWebsiteDao;
import com.inn.foresight.module.nv.wpt.analytics.model.WebCategory;
import com.inn.foresight.module.nv.wpt.analytics.service.IWPTAnalyticsService;
import com.inn.foresight.module.nv.wpt.analytics.utils.WPTAnalyticsUtils;
import com.inn.foresight.module.nv.wpt.analytics.utils.wrapper.WPTDetailedViewWrapper;
import com.inn.foresight.module.nv.wpt.analytics.utils.wrapper.WPTOperatorWrapper;
import com.inn.foresight.module.nv.wpt.analytics.utils.wrapper.WPTRawDataWrapper;
import com.inn.foresight.module.nv.wpt.analytics.utils.wrapper.WPTScatterPlotWrapper;
import com.inn.foresight.module.nv.wpt.analytics.utils.wrapper.WPTStatesWrapper;
import com.inn.foresight.module.nv.wpt.analytics.utils.wrapper.WPTSummaryWrapper;
import com.inn.product.um.geography.dao.GeographyL3Dao;
import com.inn.product.um.geography.model.GeographyL3;

/** The Class WPTAnalyticsServiceImpl. */
@Service("WPTAnalyticsServiceImpl")
public class WPTAnalyticsServiceImpl extends WPTAnalyticsUtils implements IWPTAnalyticsService{
	
	/** The logger. */
	private Logger logger = LogManager.getLogger(WPTAnalyticsServiceImpl.class);
	
	/** GeographyL3Dao - Instance of GeographyL3Dao. */
	@Autowired
	private GeographyL3Dao geographyL3Dao;
	
	/** WebCategoryDao - Instance of WebCategoryDao. */
	@Autowired
	private IWebCategoryDao webCategoryDao;
	
	/** CategorySiteAggDetailDao - Instance of CategorySiteAggDetailDao. */
	@Autowired
	private ICategorySiteAggDetailDao categorySiteAggDetailDao;
	
	/** HbaseDao - Instance of WPTHbaseDaoImpl. */
	@Autowired
	private IWPTHbaseDao hbaseDao;
	
	
	@Autowired
	private IWebsiteDao websiteDao;
	/**
	 * This method is used to WPT Pie Chart Data.
	 *
	 * @param geographyL3 - name of geographyL3 to search
	 * @param fromDate - startTime
	 * @param toDate - enndTime
	 * @param operatorA - First Operator
	 * @param operatorB - Second Operator
	 * @param networkA - First NetworkType
	 * @param networkB - Second NetworkType
	 * @param configA - First Configuration
	 * @param configB - Second Configuration
	 * @return Map of statistic JSON & categories average response time
	 * @throws Exception the exception
	 */
	@Override
	@Transactional
	public Map<String, WPTStatesWrapper> getWPTPieChartData(String geographyL3, String fromDate, String toDate, String operatorA, String operatorB,
			String networkA, String networkB, String configA, String configB) throws Exception {
		logger.info("Inside @method getWPTPieChartData()");
		Map<String, WPTStatesWrapper> response = new HashMap<>();
		
		//Getting Data for 1st Pie Chart
		WPTStatesWrapper statesWrappersA = mergeWrapperForStatsAndAvg(
				categorySiteAggDetailDao.getWPTPieChartData(geographyL3, fromDate, toDate, operatorA, networkA, configA),
				categorySiteAggDetailDao.getWPTPieChartStats(geographyL3, fromDate, toDate, operatorA, networkA, configA));
		
		//Getting Data for 2nd Pie Chart
		WPTStatesWrapper statesWrappersB = mergeWrapperForStatsAndAvg(
				categorySiteAggDetailDao.getWPTPieChartData(geographyL3, fromDate, toDate, operatorB, networkB, configB),
				categorySiteAggDetailDao.getWPTPieChartStats(geographyL3, fromDate, toDate, operatorB, networkB, configB));
		
		response.put(PIE1, statesWrappersA);
		response.put(PIE2, statesWrappersB);
		
		logger.info(RESULT_SIZE_LOGGER, response.size());
		return response;
	}
	
	/**
	 * Merging Stats JSON and Category-Wise Averages For Multiple Dates .
	 *
	 * @param categoryAvgList the category avg list
	 * @param statsList the stats list
	 * @return the WPT states wrapper
	 */
	private WPTStatesWrapper mergeWrapperForStatsAndAvg(List<WPTStatesWrapper> categoryAvgList, List<WPTStatesWrapper> statsList) {
		WPTStatesWrapper statsWrapper = new WPTStatesWrapper();
		
		Map<String, Float> categoryAvgMap = new HashMap<>();
		Map<String, Integer> categoryCountMap = new HashMap<>();
		String statsValue = null;
		
		List<WPTStatesWrapper> finalAvgList = mergeWPTWrapperForDate(categoryAvgList);
		for(WPTStatesWrapper wrapper : finalAvgList) {
			mergeCategoryAvgMap(categoryAvgMap, categoryCountMap, 
					wrapper.getCategoryAvg(), wrapper.getCategoryCount());
		}
		
		for(WPTStatesWrapper wrapper : statsList) {
			statsValue = mergeStatsJSON(statsValue, wrapper.getValues());
		}
		
		statsWrapper.setValues(statsValue);
		statsWrapper.setCategoryAvg(categoryAvgMap);
		return statsWrapper;
	}

	/**
	 * Getting Detailed Log Report showing Raw Data.
	 *
	 * @param geographyL3 - name of geographyL3 to search
	 * @param fromDate - startTime
	 * @param toDate - enndTime
	 * @param operatorA - First Operator
	 * @param operatorB - Second Operator
	 * @param networkA - First NetworkType
	 * @param networkB - Second NetworkType
	 * @param configA - First Configuration
	 * @param configB - Second Configuration
	 * @return Detailed Log Report CSV in bytes[]
	 * @throws Exception the exception
	 */
	@Override
	public byte[] getWPTDetailedLogReport(String geographyL3, String fromDate, String toDate, String operatorA, String operatorB,
			String networkA, String networkB, String configA, String configB) throws Exception {
		
		Long[] timeRange = getTimeRange(fromDate, toDate);		
		
		List<WPTRawDataWrapper> dataList = filterRawRecord(hbaseDao.scanDataWithTimeRange(timeRange[ForesightConstants.ZERO], timeRange[1], null),
				geographyL3, operatorA, operatorB, networkA, networkB, configA, configB);
		
		StringBuilder csv = new StringBuilder();
		String header = getHeaderForReport(geographyL3, fromDate, toDate);
		csv.append(header).append(NEW_LINE);
		
		writeIntoCSV(csv, dataList);
		
		return Bytes.toBytes(csv.toString());
	}

	/**
	 * Filter out the record bases on filters selected i.e. operators, networks and configurations
	 *
	 * @param dataList the data list
	 * @param geographyL3 the geography L 3
	 * @param operatorA the operator A
	 * @param operatorB the operator B
	 * @param networkA the network A
	 * @param networkB the network B
	 * @param configA the config A
	 * @param configB the config B
	 * @return List<WPTRawDataWrapper> filtered List
	 */
	private List<WPTRawDataWrapper> filterRawRecord(List<WPTRawDataWrapper> dataList, String geographyL3,
			String operatorA, String operatorB, String networkA, String networkB, String configA, String configB) {
		List<WPTRawDataWrapper> filteredList = new ArrayList<>();
		for (WPTRawDataWrapper wrapper : dataList) {
			String operator = wrapper.getOperatorName();
			String network = wrapper.getNetworkType();
			String ipVersion = wrapper.getIpVersion();
			String geography = wrapper.getGeography();
				if ((geography != null && operator != null && network != null && ipVersion != null) && (geography.equalsIgnoreCase(geographyL3) || geographyL3.equalsIgnoreCase(ALL))
						&& (operator.equalsIgnoreCase(operatorA) || operator.equalsIgnoreCase(operatorB))
						&& (network.equalsIgnoreCase(networkA) || network.equalsIgnoreCase(networkB))
						&& (ipVersion.equalsIgnoreCase(configA) || ipVersion.equalsIgnoreCase(configB))) {
					filteredList.add(wrapper);
				}
		}
		logger.info(RESULT_SIZE_LOGGER, filteredList.size());
		return filteredList;
	}

	/**
	 * This method is used to WPT Histogram Chart Data.
	 *
	 * @param geographyL3 - name of geographyL3 to search
	 * @param fromDate - startTime
	 * @param toDate - enndTime
	 * @param operatorA - First Operator
	 * @param operatorB - Second Operator
	 * @param networkA - First NetworkType
	 * @param networkB - Second NetworkType
	 * @param configA - First Configuration
	 * @param configB - Second Configuration
	 * @return Map of scatter plot JSON
	 * @throws Exception the exception
	 */
	@Override
	public Map<String, WPTStatesWrapper> getWPTHistogramData(String geographyL3, String fromDate, String toDate, String operatorA, String operatorB,
			String networkA, String networkB, String configA, String configB) throws Exception {
		logger.info("Inside @method getWPTHistogramData()");
		Map<String, WPTStatesWrapper> response = new HashMap<>();
			
		//Getting Data for 1st Histogram
		WPTStatesWrapper statesWrappersA = mergeWrapperForStats(
				categorySiteAggDetailDao.getWPTHistogramData(geographyL3, fromDate, toDate, operatorA, networkA, configA));
		
		//Getting Data for 2nd Histogram
		WPTStatesWrapper statesWrappersB = mergeWrapperForStats(
				categorySiteAggDetailDao.getWPTHistogramData(geographyL3, fromDate, toDate, operatorB, networkB, configB));
		response.put(PIE1, statesWrappersA);
		response.put(PIE2, statesWrappersB);
		
		logger.info(RESULT_SIZE_LOGGER, response.size());
		return response;
	}
	
	/**
	 * Merging Stats JSON for multiple Dates.
	 *
	 * @param wrapperList the wrapper list
	 * @return the WPT states wrapper
	 */
	private WPTStatesWrapper mergeWrapperForStats(List<WPTStatesWrapper> wrapperList) {
		WPTStatesWrapper statsWrapper = new WPTStatesWrapper();
		String statsValue = null;
		for(WPTStatesWrapper wrapper : wrapperList) {
			statsValue = mergeStatsJSON(statsValue, wrapper.getValues());
		}
		//Setting Merged Stats
		statsWrapper.setValues(statsValue);
		return statsWrapper;
	}
	
	/**
	 * This method is used to get WPT Scatter Plot Data.
	 *
	 * @param geographyL3 - name of geographyL3 to search
	 * @param fromDate - startTime
	 * @param toDate - enndTime
	 * @param operatorA - First Operator
	 * @param operatorB - Second Operator
	 * @param networkA - First NetworkType
	 * @param networkB - Second NetworkType
	 * @param configA - First Configuration
	 * @param configB - Second Configuration
	 * @return Map of statistic plot JSON
	 * @throws Exception the exception
	 */
	@Override
	public Map<String, List<WPTScatterPlotWrapper>> getWPTScatterPlotData(String geographyL3, String fromDate, String toDate, String operatorA, String operatorB,
			String networkA, String networkB, String configA, String configB) throws Exception {
		logger.info("Inside @method getWPTScatterPlotData()");
		Map<String, List<WPTScatterPlotWrapper>> response = new HashMap<>();
		
		//Getting Data for 1st Scatter Chart
		List<WPTScatterPlotWrapper> scatterA = mergeWrapperForScatterChart(
				categorySiteAggDetailDao.getWPTScatterPlotData(geographyL3, fromDate, toDate, operatorA, networkA, configA));
		
		//Getting Data for 2nd Scatter Chart
		List<WPTScatterPlotWrapper> scatterB = mergeWrapperForScatterChart(
				categorySiteAggDetailDao.getWPTScatterPlotData(geographyL3, fromDate, toDate, operatorB, networkB, configB));
		
		response.put(PIE1, scatterA);
		response.put(PIE2, scatterB);
		
		logger.info(RESULT_SIZE_LOGGER, response.size());
		return response;
	}
	
	/**
	 * Merging Scatter Chart JSON for multiple Dates.
	 *
	 * @param wrapperList the wrapper list
	 * @return the list
	 * @throws JSONException the JSON exception
	 */
	private List<WPTScatterPlotWrapper> mergeWrapperForScatterChart(List<WPTStatesWrapper> wrapperList) throws JSONException {
		List<WPTScatterPlotWrapper> scatterPlotWrappers = new ArrayList<>();
		String scatterJSON = null;
		for(WPTStatesWrapper wrapper : wrapperList) {
			scatterJSON = mergeScatterChartJSON(scatterJSON, wrapper.getValues());
		}
		if(StringUtils.isNotEmpty(scatterJSON)) {
			Gson gson = new Gson();
			Type scatterType = new TypeToken<List<WPTScatterPlotWrapper>>() {
				private static final long serialVersionUID = 1L;
			}.getType();			
			return gson.fromJson(scatterJSON, scatterType);
		}
		return scatterPlotWrappers;
	}
	
	/**
	 * This method is use to get WPT Summary Data.
	 *
	 * @param fromDate - startTime
	 * @param toDate - enndTime
	 * @param siteName - name of site to search optional
	 * @param upperLimit - pagination Upper limit
	 * @param lowerLimit - pagination Lower limit
	 * @return List of WPTSummaryWrapper
	 * @throws Exception the exception
	 */
	@Override
	@Transactional
	public List<WPTSummaryWrapper> getWPTResultSummaryData(String fromDate, String toDate, String siteName,
			Integer upperLimit, Integer lowerLimit)
			throws Exception {
		logger.info("Inside @method getWPTResultSummaryData()");
		List<WPTSummaryWrapper> resultList = categorySiteAggDetailDao.getWPTSummaryWrappers(fromDate, toDate,siteName,lowerLimit,upperLimit);
		if (CollectionUtils.isNotEmpty(resultList)) {
			Map<String, List<WPTSummaryWrapper>> wrapperMap = assembleWPTSummaryWrappers(resultList);
			resultList = mergeWPTSummaryWrapper(wrapperMap);
			logger.info(RESULT_SIZE_LOGGER, resultList.size());
		} else {
			logger.info(RESULT_SIZE_LOGGER, NumberUtils.INTEGER_ZERO);
		}
		return resultList;
	}

	/**
	 * Method merge assembled WPT Summary wrappers based on sites & ipVersion.
	 *
	 * @param wrapperMap the wrapper map
	 * @return the list
	 */
	private static List<WPTSummaryWrapper> mergeWPTSummaryWrapper(Map<String, List<WPTSummaryWrapper>> wrapperMap) {
		List<WPTSummaryWrapper> response = new ArrayList<>();
		for (Entry<String, List<WPTSummaryWrapper>> entry : wrapperMap.entrySet()) {
			List<WPTSummaryWrapper> wrappers = entry.getValue();
			if(wrappers != null && wrappers.size() == ForesightConstants.TWO) {
			WPTSummaryWrapper wrapper1 = wrappers.get(ForesightConstants.INDEX_ZERO);
			WPTSummaryWrapper wrapper2 = wrappers.get(ForesightConstants.INDEX_ONE);
			if (wrapper1 == null) {
				response.add(wrapper2);
			} else if (wrapper2 != null) {
				joinWPTSummaryWrapper(wrapper1, wrapper2);
				response.add(wrapper1);
			}
			} else {
				response.addAll(wrappers);
			}
		}
		return response;
	}

	/**
	 * Method join two summary wrapper & return single wrapper.
	 *
	 * @param wrapper1 the wrapper 1
	 * @param wrapper2 the wrapper 2
	 */
	private static void joinWPTSummaryWrapper(WPTSummaryWrapper wrapper1, WPTSummaryWrapper wrapper2) {
		switch (wrapper2.getIpv()) {
			case IPV4 :
				wrapper1.setIpv4LoadTime(wrapper2.getIpv4LoadTime());
				wrapper1.setIpv4SuccessRate(wrapper2.getIpv4SuccessRate());
				break;
			case IPV6 :
				wrapper1.setIpv6LoadTime(wrapper2.getIpv6LoadTime());
				wrapper1.setIpv6SuccessRate(wrapper2.getIpv6SuccessRate());
				break;
			default :
				break;
		}
	}

	/**
	 * Method assemble summary wrapper of same site having different ipVersion.
	 *
	 * @param summaryWrappers the summary wrappers
	 * @return Map of list sites summary wrapper
	 */
	private static Map<String, List<WPTSummaryWrapper>> assembleWPTSummaryWrappers(List<WPTSummaryWrapper> summaryWrappers) {
		Map<String, List<WPTSummaryWrapper>> wrapperMap = new LinkedHashMap<>();
		for(WPTSummaryWrapper summaryWrapper : summaryWrappers) {
			String key = summaryWrapper.getCaptureTime()+summaryWrapper.getSite();
			if(wrapperMap.containsKey(key) && wrapperMap.get(key) != null) {
				wrapperMap.get(key).add(summaryWrapper);
			} else {
				List<WPTSummaryWrapper> wrapperList = new ArrayList<>();
				wrapperList.add(summaryWrapper);
 				wrapperMap.put(key,wrapperList);
			}
		}
		return wrapperMap;
	}
	
	/**
	 * This method is used to get WPT Detailed View Data.
	 *
	 * @param operator -  Operator
	 * @param network - NetworkType
	 * @param configuration - Configuration
	 * @param fromDate - startTime
	 * @param toDate - enndTime
	 * @param isSiteWise - Site wise records require or category wise
	 * @param geographyL3 - name of geographyL3 to search
	 * @param upperLimit - pagination Upper limit
	 * @param lowerLimit - pagination Lower limit
	 * @return list Detailed View Wrapper
	 * @throws Exception the exception
	 */
	@Override
	public List<WPTDetailedViewWrapper> getWPTDetailedViewData(String operator, String network, String configuration, String fromDate,
			String toDate, Boolean isSiteWise, String geographyL3, Integer upperLimit, Integer lowerLimit)
			throws Exception {
		logger.info("Inside @method : getWPTDetailedViewData()");		
		List<WPTDetailedViewWrapper> list = null;
		if(isSiteWise) {
			list = categorySiteAggDetailDao.getWPTDetailedViewDataSiteWise(geographyL3, fromDate, toDate,
							operator, network, configuration, lowerLimit, upperLimit);	
		} else {
			Map<Integer, WebCategory> categories = getWebCategoryMap();
			list = categorySiteAggDetailDao.getWPTDetailedViewDataCategoryWise(geographyL3, fromDate, toDate, operator,
					network, configuration);
			for(WPTDetailedViewWrapper wrapper : list) {
				WebCategory category = categories.get(wrapper.getCategoryId());
				wrapper.setSite(category != null ? category.getCategoryName() : ALL);
			}
		}
		logger.info(RESULT_SIZE_LOGGER, list.size());
		return list;
	}
	
	/**
	 * Getting list of all filters i.e. geography, operators, network and ipversions
	 *
	 * @param operator the operator
	 * @return Map of filterList
	 */
	@Override
	@Cacheable("WPTAnalyticsFilterCache")
	public Map<String, Object> getFilterList(String operator) {
		Map<String, Object> map = new HashMap<>();
		List<GeographyL3> geographyL3s = null;
		try {
			geographyL3s = geographyL3Dao.findAll();
		} catch (Exception e) {
			logger.error("No GeographyL3 found for getFilterList error={}  ", e.getMessage());
		}
		List<WPTOperatorWrapper> operatorsList = getOperatorsList(operator);
		List<String> configurationsList = ConfigUtils.getStringList(WPT_ANALATICS_CONFIGURATION);
		map.put("geographyL3", geographyL3s);
		map.put(OPERATOR, operatorsList);
		map.put(CONFIGURATION, configurationsList);
		return map;
	}

	/**
	 * Gets the operators list.
	 *
	 * @param operator the operator
	 * @return the operators list
	 */
	private List<WPTOperatorWrapper> getOperatorsList(String operator) {
		List<WPTOperatorWrapper> list = new ArrayList<>();
		List<String> operatorList = ConfigUtils.getStringList(WPT_ANALATICS_OPERATOR);
		for(String value : operatorList) {
			WPTOperatorWrapper wrapper = getWPTOperatorWrapper(operator, value);
			if(wrapper != null) {
				list.add(wrapper);
			}
		}
		return list;
	}

	/**
	 * Gets the WPT operator wrapper.
	 *
	 * @param operator the operator
	 * @param value the value
	 * @return the WPT operator wrapper
	 */
	private WPTOperatorWrapper getWPTOperatorWrapper(String operator, String value) {
		WPTOperatorWrapper wrapper = new WPTOperatorWrapper();
		String operatorFromConfig = value.split(ForesightConstants.UNDERSCORE)[ForesightConstants.ZERO];
		String technology = value.split(ForesightConstants.UNDERSCORE)[1];
		wrapper.setOperator(operatorFromConfig);
		wrapper.setTechnology(technology);	
		if(operator != null) {
			if(operator.equalsIgnoreCase(operatorFromConfig)) {
				return wrapper;
			}else {
				return null;
			}
		}
		return wrapper;
	}
	
	/**
	 * Gets the web category map.
	 *
	 * @return the web category map
	 */
	private Map<Integer, WebCategory> getWebCategoryMap() {
		Map<Integer, WebCategory> sortedMap = new HashMap<>();
		try {
			List<WebCategory> webCategories = webCategoryDao.findAll();
			for (WebCategory webCategory : webCategories) {
				sortedMap.put(webCategory.getId(), webCategory);
			}
		} catch (DaoException e) {
			logger.error("DBException found in @getWebCategoryMap: err={}", e.getMessage());
		} catch (Exception e) {
			logger.error("No WebCategory found for error={}  ", e.getMessage());
		}
		return sortedMap;
	}

	/**
	 * Gets the WPT dump report from hbase.
	 *
	 * @param deviceId the device id
	 * @param testId the test id
	 * @param testStartTime the test start time
	 * @param testEndTime the test end time
	 * @return the WPT dump report from hbase
	 * @throws RestException the rest exception
	 */
	@Override
	public NVReportWrapper getWPTDumpReportFromHbase(String deviceId, Integer testId, Long testStartTime, Long testEndTime) {
		try {
			Filter filter = null;
			if (deviceId != null) {
				
				logger.info("Going to fetch data for deviceId: {}", deviceId);
				String rowKeyPrefix = StringUtils.reverse(StringUtils.replace(deviceId, AppConstants.DEVICE_ID_PREFIX, ForesightConstants.BLANK_STRING));
				logger.info("Row Key Prefix: {}", rowKeyPrefix);
				filter = new PrefixFilter(Bytes.toBytes(rowKeyPrefix));
				logger.info("Row Key Prefix: {}, startTime: {}, endTime: {}", rowKeyPrefix, testStartTime, testEndTime);
				List<WPTRawDataWrapper> wptWrapperList = hbaseDao.scanDataWithTimeRange(testStartTime, testEndTime, filter);
				return createWPTDumpReport(wptWrapperList, testId);
			} else {
				logger.warn("Invalid input for creation of WPT Dump Report: {}", deviceId);
				throw new RestException("Unable to create Dump Report");
			}
		
		} catch (Exception e) {
			logger.error("Error while getting data for WPT dump report: {}", Utils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}
	
	/**
	 * Creates the WPT dump report.
	 *
	 * @param wptDataList the wpt data list
	 * @param testId the test id
	 * @return the report wrapper
	 * @throws RestException the rest exception
	 */
	private NVReportWrapper createWPTDumpReport(List<WPTRawDataWrapper> wptDataList, Integer testId) {
		NVReportWrapper reportWrapper = new NVReportWrapper();
		try {
				String wptDumpCsv = writeWPTDumpIntoCSV(wptDataList, testId);
				DateFormat formatter = new SimpleDateFormat("ddMMyy_hh:mm:ss");
				String fileName = ConfigUtils.getString(StealthConstants.WPT_DUMP_REPORT_FILE_PATH)
						+ StealthConstants.PULSE + Symbol.UNDERSCORE_STRING + formatter.format(new Date()) + ForesightConstants.CSV_EXTENSION;
				logger.info("Going to write file to local path   {}", fileName);
				ReportUtil.writeStringIntoCsvFile(wptDumpCsv, fileName);
				File file = new File(fileName);
				reportWrapper.setFile(FileUtils.readFileToByteArray(file));
				reportWrapper.setReportName(file.getName());
				FileUtils.deleteQuietly(file);
				logger.info("Report created at: {}", fileName);
		} catch (Exception e) {
			logger.error("Error while getting data for WPT dump report: {}", Utils.getStackTrace(e));
			throw new RestException("Unable to create WPT Dump Report");
		}
		
		return reportWrapper;
	}
	
	/**
	 * Write WPT dump into CSV.
	 *
	 * @param wptDataList the wpt data list
	 * @param testId the test id
	 * @return the string
	 */
	private String writeWPTDumpIntoCSV(List<WPTRawDataWrapper> wptDataList, Integer testId) {
		StringBuilder csvBuilder = new StringBuilder();
		logger.info("Inside writeWPTDumpIntoCSV for testId: {}", testId);
		createHeaderForWPTDumpReport(csvBuilder);
		int recordCount = ForesightConstants.ZERO;

		if (wptDataList != null && !wptDataList.isEmpty()) {
			logger.info("WPT Data List Size: {}", wptDataList.size());
			for (WPTRawDataWrapper wptWrapper : wptDataList) {
				if (testId != null && wptWrapper.getTestId() != null && testId.equals(wptWrapper.getTestId())) {
					appendDeviceGroupInformation(wptWrapper, csvBuilder);
					appendNetworkInformation(wptWrapper, csvBuilder);
					appendBoundaryInformation(wptWrapper, csvBuilder);
					appendBasicDeviceInformation(wptWrapper, csvBuilder);
					appendDeviceNetworkInformation(wptWrapper, csvBuilder);
					appendKPIInformation(wptWrapper, csvBuilder);
					csvBuilder.append(ForesightConstants.NEW_LINE);
					recordCount++;
				}
			}
		} else {
			logger.warn("No data available for testId: {}", testId);
		}
		logger.info("Total: {} records written into csv",recordCount);
		return csvBuilder.toString();
	}
	
	/**
	 * Append device group information.
	 *
	 * @param wptWrapper the wpt wrapper
	 * @param csvBuilder the csv builder
	 * @return the string builder
	 */
	private StringBuilder appendDeviceGroupInformation(WPTRawDataWrapper wptWrapper, StringBuilder csvBuilder) {
		appendDataToCsv(wptWrapper.getTestPerformed(),csvBuilder);
		appendDataToCsv(wptWrapper.getNetworkType(),csvBuilder);
		appendDataToCsv(wptWrapper.getDate(),csvBuilder);
		appendDataToCsv(wptWrapper.getTestStartTime(),csvBuilder);
		appendDataToCsv(wptWrapper.getTestEndTime(),csvBuilder);
		appendDataToCsv(wptWrapper.getWebUrl(),csvBuilder);
		appendDataToCsv(wptWrapper.getIteration(),csvBuilder);
		appendDataToCsv(wptWrapper.getFirstDNSResolutionTime(),csvBuilder);
		appendDataToCsv(wptWrapper.getTotalDnsResolutionTime(),csvBuilder);
		appendDataToCsv(wptWrapper.getTtfb(),csvBuilder);
		appendDataToCsv(wptWrapper.getTtl(),csvBuilder);
		return csvBuilder;
	}
	
	/**
	 * Append network information.
	 *
	 * @param wptWrapper the wpt wrapper
	 * @param csvBuilder the csv builder
	 * @return the string builder
	 */
	private StringBuilder appendNetworkInformation(WPTRawDataWrapper wptWrapper, StringBuilder csvBuilder) {
		appendDataToCsv(wptWrapper.getTargetedIp(),csvBuilder);
		appendDataToCsv(wptWrapper.getExternalIp(),csvBuilder);
		appendDataToCsv(wptWrapper.getGeographyL3(),csvBuilder);
		appendDataToCsv(wptWrapper.getNoOfRedirection(),csvBuilder);
		appendDataToCsv(wptWrapper.getRedirectedUrl(),csvBuilder);
		appendDataToCsv(wptWrapper.getRouteHolderList(),csvBuilder);
		appendDataToCsv(wptWrapper.getHopeCount(),csvBuilder);
		appendTargetIPVersion(wptWrapper, csvBuilder);
		appendDataToCsv(wptWrapper.getIpVersion(),csvBuilder);
		appendDataToCsv(wptWrapper.getIpv4list(),csvBuilder);
		appendDataToCsv(wptWrapper.getIpv6list(),csvBuilder);
		return csvBuilder;
	}


	/**
	 * Append target IP version.
	 *
	 * @param wptWrapper the wpt wrapper
	 * @param csvBuilder the csv builder
	 */
	private void appendTargetIPVersion(WPTRawDataWrapper wptWrapper, StringBuilder csvBuilder) {
		if(wptWrapper.getIsIpv6() != null ) {
			if(wptWrapper.getIsIpv6()) {
			appendDataToCsv(StealthConstants.IP_VERSION_6,csvBuilder);
			}else {
				appendDataToCsv(StealthConstants.IP_VERSION_4,csvBuilder);
			}
		} else {
			appendDataToCsv(wptWrapper.getIsIpv6(),csvBuilder);
		}
	}
	

	/**
	 * Append boundary information.
	 *
	 * @param wptWrapper the wpt wrapper
	 * @param csvBuilder the csv builder
	 * @return the string builder
	 */
	private StringBuilder appendBoundaryInformation(WPTRawDataWrapper wptWrapper, StringBuilder csvBuilder) {
		appendDataToCsv(wptWrapper.getGeographyL2(),csvBuilder);
		appendDataToCsv(wptWrapper.getGeographyL3(),csvBuilder);
		appendDataToCsv(wptWrapper.getLatitude(),csvBuilder);
		appendDataToCsv(wptWrapper.getLongitude(),csvBuilder);
		return csvBuilder;
	}

	/**
	 * Append basic device information.
	 *
	 * @param wptWrapper the wpt wrapper
	 * @param csvBuilder the csv builder
	 * @return the string builder
	 */
	private StringBuilder appendBasicDeviceInformation(WPTRawDataWrapper wptWrapper, StringBuilder csvBuilder) {
		appendDataToCsv(wptWrapper.getOperatorName(), csvBuilder);
		appendDataToCsv(wptWrapper.getImsi(), csvBuilder);
		appendDataToCsv(wptWrapper.getImei(), csvBuilder);
		appendDataToCsv(wptWrapper.getManufacturer(), csvBuilder);
		appendDataToCsv(wptWrapper.getModel(), csvBuilder);
		appendDataToCsv(wptWrapper.getDeviceOS(), csvBuilder);
		appendDataToCsv(wptWrapper.getLocationType(), csvBuilder);
		appendDataToCsv(wptWrapper.getRemark(), csvBuilder);
		appendDataToCsv(wptWrapper.getGpsStatus(), csvBuilder);
		appendDataToCsv(wptWrapper.getChargerConnectedStatus(), csvBuilder);
		appendDataToCsv(wptWrapper.getBatteryLevel(), csvBuilder);
		appendDataToCsv(wptWrapper.getVoltage(), csvBuilder);
		appendDataToCsv(wptWrapper.getTemperature(), csvBuilder);
		appendDataToCsv(wptWrapper.getPageSize(), csvBuilder);
		appendDataToCsv(wptWrapper.getBaseband(), csvBuilder);
		appendDataToCsv(wptWrapper.getBuildNumber(), csvBuilder);
		appendDataToCsv(wptWrapper.getIsAutoDataTime(), csvBuilder);
		appendDataToCsv(wptWrapper.getMac(), csvBuilder);
		return csvBuilder;
	}
	
	/**
	 * Append device network information.
	 *
	 * @param wptWrapper the wpt wrapper
	 * @param csvBuilder the csv builder
	 * @return the string builder
	 */
	private StringBuilder appendDeviceNetworkInformation(WPTRawDataWrapper wptWrapper, StringBuilder csvBuilder) {
		appendDataToCsv(wptWrapper.getProbeId(),csvBuilder);
		appendDataToCsv(wptWrapper.getMcc(),csvBuilder);
		appendDataToCsv(wptWrapper.getMnc(),csvBuilder);
		appendDataToCsv(wptWrapper.getCellId(),csvBuilder);
		appendDataToCsv(wptWrapper.getPci(),csvBuilder);
		appendDataToCsv(wptWrapper.getPsc(),csvBuilder);
		appendDataToCsv(wptWrapper.getTac(),csvBuilder);
		appendDataToCsv(wptWrapper.getLac(),csvBuilder);
		return csvBuilder;
	}
	
	/**
	 * Append KPI information.
	 *
	 * @param wptWrapper the wpt wrapper
	 * @param csvBuilder the csv builder
	 * @return the string builder
	 */
	private StringBuilder appendKPIInformation(WPTRawDataWrapper wptWrapper, StringBuilder csvBuilder) {
		appendDataToCsv(wptWrapper.getAvgRsrp(),csvBuilder);
		appendDataToCsv(wptWrapper.getAvgSinr(),csvBuilder);
		appendDataToCsv(wptWrapper.getAvgRsrq(),csvBuilder);
		appendDataToCsv(wptWrapper.getAvgRssi(),csvBuilder);
		appendDataToCsv(wptWrapper.getAvgRscp(),csvBuilder);
		appendDataToCsv(wptWrapper.getEcno(),csvBuilder);
		appendDataToCsv(wptWrapper.getRxLevel(),csvBuilder);
		csvBuilder.append(getValueForCell(wptWrapper.getRxQuality()));
		return csvBuilder;
	}

	/**
	 * Append data to csv.
	 *
	 * @param value the value
	 * @param csvBuilder the csv builder
	 */
	private void appendDataToCsv(Object value, StringBuilder csvBuilder) {
		csvBuilder.append(getValueForCell(value));
		csvBuilder.append(ForesightConstants.COMMA);
	}

	/**
	 * Gets the value for cell.
	 *
	 * @param value the value
	 * @return the value for cell
	 */
	private Object getValueForCell(Object value) {
		String stringValue = String.valueOf(value);
		if (value == null || stringValue.equalsIgnoreCase("null")) {
			return ForesightConstants.HIPHEN;
		} else {
			return stringValue;
		}
	}
	
	/**
	 * Creates the header for WPT dump report.
	 *
	 * @param csvBuilder the csv builder
	 */
	private void createHeaderForWPTDumpReport(StringBuilder csvBuilder) {
		csvBuilder.append(StealthConstants.WPT_DUMP_REPORT_HEADER);
		logger.info("Header written successfully in WPT Dump Report");
		csvBuilder.append(ForesightConstants.NEW_LINE);
	}

	@Override
	public Map<String,String> getCategoryWiseSites() {
		return websiteDao.getSiteCategoryMap();
	}


}
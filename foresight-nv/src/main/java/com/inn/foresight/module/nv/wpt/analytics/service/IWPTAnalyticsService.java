package com.inn.foresight.module.nv.wpt.analytics.service;

import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.module.nv.reportgeneration.wrapper.NVReportWrapper;
import com.inn.foresight.module.nv.wpt.analytics.utils.wrapper.WPTDetailedViewWrapper;
import com.inn.foresight.module.nv.wpt.analytics.utils.wrapper.WPTScatterPlotWrapper;
import com.inn.foresight.module.nv.wpt.analytics.utils.wrapper.WPTStatesWrapper;
import com.inn.foresight.module.nv.wpt.analytics.utils.wrapper.WPTSummaryWrapper;

/** The Interface IWPTAnalyticsService. */
public interface IWPTAnalyticsService {
	
	/**
	 * Gets the filter list.
	 *
	 * @param operator the operator
	 * @return the filter list
	 */
	Map<String, Object> getFilterList(String operator);

	/**
	 * Gets the WPT pie chart data.
	 *
	 * @param city the city
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @param operatorA the operator A
	 * @param operatorB the operator B
	 * @param networkA the network A
	 * @param networkB the network B
	 * @param configA the config A
	 * @param configB the config B
	 * @return the WPT pie chart data
	 * @throws Exception the exception
	 */
	Map<String, WPTStatesWrapper> getWPTPieChartData(String city, String fromDate, String toDate, String operatorA,
			String operatorB, String networkA, String networkB, String configA, String configB) throws Exception;

	/**
	 * Gets the WPT detailed log report.
	 *
	 * @param city the city
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @param operatorA the operator A
	 * @param operatorB the operator B
	 * @param configA the config A
	 * @param configB the config B
	 * @param configA2 the config A 2
	 * @param configB2 the config B 2
	 * @return the WPT detailed log report
	 * @throws Exception the exception
	 */
	Object getWPTDetailedLogReport(String city, String fromDate, String toDate, String operatorA, String operatorB,
			String configA, String configB, String configA2, String configB2) throws Exception;

	/**
	 * Gets the WPT histogram data.
	 *
	 * @param city the city
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @param operatorA the operator A
	 * @param operatorB the operator B
	 * @param networkA the network A
	 * @param networkB the network B
	 * @param configA the config A
	 * @param configB the config B
	 * @return the WPT histogram data
	 * @throws Exception the exception
	 */
	Map<String, WPTStatesWrapper> getWPTHistogramData(String city, String fromDate, String toDate, String operatorA, String operatorB,
			String networkA, String networkB, String configA, String configB) throws Exception;
	
	/**
	 * Gets the WPT scatter plot data.
	 *
	 * @param city the city
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @param operatorA the operator A
	 * @param operatorB the operator B
	 * @param networkA the network A
	 * @param networkB the network B
	 * @param configA the config A
	 * @param configB the config B
	 * @return the WPT scatter plot data
	 * @throws Exception the exception
	 */
	Map<String, List<WPTScatterPlotWrapper>> getWPTScatterPlotData(String city, String fromDate, String toDate, String operatorA, String operatorB,
			String networkA, String networkB, String configA, String configB) throws Exception;

	/**
	 * Gets the WPT result summary data.
	 *
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @param siteName the site name
	 * @param upperLimit the upper limit
	 * @param lowerLimit the lower limit
	 * @return the WPT result summary data
	 * @throws Exception the exception
	 */
	List<WPTSummaryWrapper> getWPTResultSummaryData(String fromDate, String toDate, String siteName, Integer upperLimit, Integer lowerLimit) throws Exception;

	/**
	 * Gets the WPT detailed view data.
	 *
	 * @param operator the operator
	 * @param network the network
	 * @param configuration the configuration
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @param isSiteWise the is site wise
	 * @param city the city
	 * @param upperLimit the upper limit
	 * @param lowerLimit the lower limit
	 * @return the WPT detailed view data
	 * @throws Exception the exception
	 */
	List<WPTDetailedViewWrapper> getWPTDetailedViewData(String operator, String network, String configuration, String fromDate, String toDate,
			Boolean isSiteWise, String city, Integer upperLimit, Integer lowerLimit) throws Exception;

	/**
	 * Gets the WPT dump report from hbase.
	 *
	 * @param deviceId the device id
	 * @param testId the test id
	 * @param testStartTime the test start time
	 * @param testEndTime the test end time
	 * @return the WPT dump report from hbase
	 * @throws RestException 
	 */
	NVReportWrapper getWPTDumpReportFromHbase(String deviceId, Integer testId, Long testStartTime,
			Long testEndTime);
	
	
	Map<String, String> getCategoryWiseSites();

}
package com.inn.foresight.module.nv.wpt.analytics.dao;

import java.util.List;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.foresight.module.nv.wpt.analytics.model.CategorySiteAggDetail;
import com.inn.foresight.module.nv.wpt.analytics.utils.wrapper.WPTDetailedViewWrapper;
import com.inn.foresight.module.nv.wpt.analytics.utils.wrapper.WPTStatesWrapper;
import com.inn.foresight.module.nv.wpt.analytics.utils.wrapper.WPTSummaryWrapper;

/** The Interface ICategorySiteAggDetailDao. */
public interface ICategorySiteAggDetailDao extends IGenericDao<Integer, CategorySiteAggDetail>{

	/**
	 * Gets the WPT pie chart data.
	 *
	 * @param city the city
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @param operatorA the operator A
	 * @param networkA the network A
	 * @param configA the config A
	 * @return the WPT pie chart data
	 * @throws Exception the exception
	 */
	List<WPTStatesWrapper> getWPTPieChartData(String city, String fromDate, String toDate, String operatorA,
			String networkA, String configA) throws Exception;
	
	/**
	 * Gets the WPT pie chart stats.
	 *
	 * @param city the city
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @param operatorA the operator A
	 * @param networkA the network A
	 * @param configA the config A
	 * @return the WPT pie chart stats
	 * @throws Exception the exception
	 */
	List<WPTStatesWrapper> getWPTPieChartStats(String city, String fromDate, String toDate, String operatorA,
			String networkA, String configA) throws Exception;

	/**
	 * Gets the WPT histogram data.
	 *
	 * @param city the city
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @param operatorA the operator A
	 * @param networkA the network A
	 * @param configA the config A
	 * @return the WPT histogram data
	 * @throws Exception the exception
	 */
	List<WPTStatesWrapper> getWPTHistogramData(String city, String fromDate, String toDate, String operatorA,
			String networkA, String configA) throws Exception;

	/**
	 * Gets the WPT detailed view data category wise.
	 *
	 * @param city the city
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @param operator the operator
	 * @param networkType the network type
	 * @param config the config
	 * @return the WPT detailed view data category wise
	 */
	List<WPTDetailedViewWrapper> getWPTDetailedViewDataCategoryWise(String city, String fromDate, String toDate,
			String operator, String networkType, String config);

	/**
	 * Gets the WPT detailed view data site wise.
	 *
	 * @param city the city
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @param operator the operator
	 * @param networkType the network type
	 * @param config the config
	 * @param lowerLimit the lower limit
	 * @param upperLimit the upper limit
	 * @return the WPT detailed view data site wise
	 */
	List<WPTDetailedViewWrapper> getWPTDetailedViewDataSiteWise(String city, String fromDate, String toDate,
			String operator, String networkType, String config, int lowerLimit, int upperLimit);

	/**
	 * Gets the WPT summary wrappers.
	 *
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @param site the site
	 * @param lowerLimit the lower limit
	 * @param upperLimit the upper limit
	 * @return the WPT summary wrappers
	 */
	List<WPTSummaryWrapper> getWPTSummaryWrappers(String fromDate, String toDate, String site, int lowerLimit, int upperLimit);

	/**
	 * Gets the WPT scatter plot data.
	 *
	 * @param city the city
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @param operatorA the operator A
	 * @param networkA the network A
	 * @param configA the config A
	 * @return the WPT scatter plot data
	 * @throws Exception the exception
	 */
	List<WPTStatesWrapper> getWPTScatterPlotData(String city, String fromDate, String toDate, String operatorA,
			String networkA, String configA) throws Exception;

}

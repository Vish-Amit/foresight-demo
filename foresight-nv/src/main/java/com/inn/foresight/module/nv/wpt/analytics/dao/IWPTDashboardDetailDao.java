package com.inn.foresight.module.nv.wpt.analytics.dao;

import java.util.List;
import java.util.Map;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.exceptions.ValueNotFoundException;
import com.inn.foresight.module.nv.wpt.analytics.utils.wrapper.TopURLDetailWrapper;
import com.inn.foresight.module.nv.wpt.analytics.utils.wrapper.WPTDashboardKpiWrapper;
import com.inn.foresight.module.nv.wpt.analytics.utils.wrapper.WPTDayWiseCountWrapper;
import com.inn.foresight.module.nv.wpt.analytics.utils.wrapper.WPTHTTPStatsWrapper;

/** The Interface IWPTDashboardDetailDao. */
public interface IWPTDashboardDetailDao {

	/**
	 * Gets the target ip version count.
	 *
	 * @param date the date
	 * @param processType the process type
	 * @param filterMap the filter map
	 * @param geographyId the geography id
	 * @param operator the operator
	 * @param technology the technology
	 * @return the target ip version count
	 * @throws RestException the rest exception
	 */
	Map<String, Long> getTargetIpVersionCount(String date, String processType, Map<String, List> filterMap,
			Integer geographyId, String operator, String technology);
	
	/**
	 * Gets the total record count.
	 *
	 * @param timeConstantList the time constant list
	 * @param processType the process type
	 * @param filterMap the filter map
	 * @param geographyId the geography id
	 * @param operator the operator
	 * @param technology the technology
	 * @return the total record count
	 * @throws RestException the rest exception
	 */
	List<WPTDayWiseCountWrapper> getTotalRecordCount(List<String> timeConstantList, String processType,
			Map<String, List> filterMap, Integer geographyId, String operator, String technology);
	
	/**
	 * Gets the total device count.
	 *
	 * @param timeConstantList the time constant list
	 * @param processType the process type
	 * @param filterMap the filter map
	 * @param geographyId the geography id
	 * @param operator the operator
	 * @param technology the technology
	 * @return the total device count
	 * @throws RestException the rest exception
	 */
	List<WPTDayWiseCountWrapper> getTotalDeviceCount(List<String> timeConstantList, String processType,
			Map<String, List> filterMap, Integer geographyId, String operator, String technology);
	
	/**
	 * Gets the total url count.
	 *
	 * @param timeConstantList the time constant list
	 * @param processType the process type
	 * @param filterMap the filter map
	 * @param geographyId the geography id
	 * @param operator the operator
	 * @param technology the technology
	 * @return the total url count
	 * @throws RestException the rest exception
	 */
	List<WPTDayWiseCountWrapper> getTotalUrlCount(List<String> timeConstantList, String processType,
			Map<String, List> filterMap, Integer geographyId, String operator, String technology);
	
	/**
	 * Gets the top tested URL counts.
	 *
	 * @param date the date
	 * @param processType the process type
	 * @param filterMap the filter map
	 * @param geographyId the geography id
	 * @param operator the operator
	 * @param technology the technology
	 * @return the top tested URL counts
	 * @throws ValueNotFoundException the value not found exception
	 * @throws RestException the rest exception
	 */
	List<TopURLDetailWrapper> getTopTestedURLCounts(String date, String processType, Map<String, List> filterMap,
			Integer geographyId, String operator, String technology) throws ValueNotFoundException;
	
	/**
	 * Gets the top ping counts.
	 *
	 * @param date the date
	 * @param processType the process type
	 * @param filterMap the filter map
	 * @param geographyId the geography id
	 * @param operator the operator
	 * @param technology the technology
	 * @return the top ping counts
	 * @throws ValueNotFoundException the value not found exception
	 * @throws RestException the rest exception
	 */
	List<TopURLDetailWrapper> getTopPingCounts(String date, String processType, Map<String, List> filterMap,
			Integer geographyId, String operator, String technology) throws ValueNotFoundException;

	/**
	 * Gets the top tested URL details.
	 *
	 * @param date the date
	 * @param processType the process type
	 * @param filterMap the filter map
	 * @param geographyId the geography id
	 * @param operator the operator
	 * @param technology the technology
	 * @param ulimit the ulimit
	 * @param llimit the llimit
	 * @return the top tested URL details
	 * @throws ValueNotFoundException the value not found exception
	 * @throws RestException the rest exception
	 */
	List<TopURLDetailWrapper> getTopTestedURLDetails(String date, String processType, Map<String, List> filterMap,
			Integer geographyId, String operator, String technology,Integer ulimit,Integer llimit) throws ValueNotFoundException;
	
	/**
	 * Gets the total tested URL count.
	 *
	 * @param date the date
	 * @param processType the process type
	 * @param filterMap the filter map
	 * @param geographyId the geography id
	 * @param operator the operator
	 * @param technology the technology
	 * @return the total tested URL count
	 * @throws ValueNotFoundException the value not found exception
	 * @throws RestException the rest exception
	 */
	Long getTotalTestedURLCount(String date, String processType, Map<String, List> filterMap,
			Integer geographyId, String operator, String technology) throws ValueNotFoundException;

	/**
	 * Gets the top ping details.
	 *
	 * @param date the date
	 * @param processType the process type
	 * @param filterMap the filter map
	 * @param geographyId the geography id
	 * @param operator the operator
	 * @param technology the technology
	 * @param ulimit the ulimit
	 * @param llimit the llimit
	 * @return the top ping details
	 * @throws ValueNotFoundException the value not found exception
	 * @throws RestException the rest exception
	 */
	List<TopURLDetailWrapper> getTopPingDetails(String date, String processType, Map<String, List> filterMap,
			Integer geographyId, String operator, String technology,Integer ulimit,Integer llimit) throws ValueNotFoundException;
	
	/**
	 * Gets the total ping count.
	 *
	 * @param date the date
	 * @param processType the process type
	 * @param filterMap the filter map
	 * @param geographyId the geography id
	 * @param operator the operator
	 * @param technology the technology
	 * @return the total ping count
	 * @throws ValueNotFoundException the value not found exception
	 * @throws RestException the rest exception
	 */
	Long getTotalPingCount(String date, String processType, Map<String, List> filterMap,
			Integer geographyId, String operator, String technology) throws ValueNotFoundException;
	
	/**
	 * Gets the avg TTFB detail.
	 *
	 * @param timeConstantList the time constant list
	 * @param processType the process type
	 * @param filterMap the filter map
	 * @param geographyId the geography id
	 * @param operator the operator
	 * @param technology the technology
	 * @return the avg TTFB detail
	 * @throws ValueNotFoundException the value not found exception
	 * @throws RestException the rest exception
	 */
	List<WPTDashboardKpiWrapper> getAvgTTFBDetail(List<String> timeConstantList, String processType, Map<String, List> filterMap,
			Integer geographyId, String operator, String technology) throws ValueNotFoundException;

	/**
	 * Gets the HTTP and HTTPS count.
	 *
	 * @param timeConstantList the time constant list
	 * @param processType the process type
	 * @param filterMap the filter map
	 * @param geographyId the geography id
	 * @param operator the operator
	 * @param technology the technology
	 * @return the HTTP and HTTPS count
	 * @throws RestException the rest exception
	 */
	List<WPTHTTPStatsWrapper> getHTTPAndHTTPSCount(List<String> timeConstantList, String processType, Map<String, List> filterMap,
			Integer geographyId, String operator, String technology);

	/**
	 * Gets the trace route and TTFB data.
	 *
	 * @param timeConstantList the time constant list
	 * @param processType the process type
	 * @param comparatorTechList the comparator tech list
	 * @param categoryIdList the category id list
	 * @param locationIdList the location id list
	 * @param technology the technology
	 * @param operator the operator
	 * @param geographyId the geography id
	 * @param filterMap the filter map
	 * @return the trace route and TTFB data
	 * @throws RestException the rest exception
	 */
	List<WPTDashboardKpiWrapper> getTraceRouteAndTTFBData(List<String> timeConstantList, String processType, List<Object> comparatorTechList,
			List<Object> categoryIdList, List<Object> locationIdList, String technology, String operator, Integer geographyId,
			Map<String, List> filterMap);
	
	/**
	 * Gets the TTFB and TDNS data.
	 *
	 * @param timeConstantList the time constant list
	 * @param processType the process type
	 * @param comparatorTechList the comparator tech list
	 * @param categoryIdList the category id list
	 * @param locationIdList the location id list
	 * @param technology the technology
	 * @param operator the operator
	 * @param geographyId the geography id
	 * @param filterMap the filter map
	 * @return the TTFB and TDNS data
	 * @throws RestException the rest exception
	 */
	List<WPTDashboardKpiWrapper> getTTFBAndTDNSData(List<String> timeConstantList, String processType, List<Object> comparatorTechList,
			List<Object> categoryIdList, List<Object> locationIdList, String technology, String operator, Integer geographyId,
			Map<String, List> filterMap);
	
	/**
	 * Gets the TTFB category wise data.
	 *
	 * @param timeConstantList the time constant list
	 * @param processType the process type
	 * @param comparatorTechList the comparator tech list
	 * @param categoryIdList the category id list
	 * @param locationIdList the location id list
	 * @param technology the technology
	 * @param operator the operator
	 * @param geographyId the geography id
	 * @param filterMap the filter map
	 * @return the TTFB category wise data
	 * @throws RestException the rest exception
	 */
	List<WPTDashboardKpiWrapper> getTTFBCategoryWiseData(List<String> timeConstantList, String processType, List<Object> comparatorTechList,
			List<Object> categoryIdList, List<Object> locationIdList, String technology, String operator, Integer geographyId,
			Map<String, List> filterMap);
	
	/**
	 * Gets the analysis graph data.
	 *
	 * @param timeConstantList the time constant list
	 * @param processType the process type
	 * @param categories the categories
	 * @param locations the locations
	 * @param filters the filters
	 * @param technology the technology
	 * @return the analysis graph data
	 * @throws RestException the rest exception
	 */
	List<TopURLDetailWrapper> getAnalysisGraphData(List<String> timeConstantList, String processType, 
			List<Object> categories, List<Object> locations, Map<String, List> filters, String technology);

}

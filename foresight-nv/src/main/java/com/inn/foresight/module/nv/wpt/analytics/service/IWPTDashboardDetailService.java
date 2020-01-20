package com.inn.foresight.module.nv.wpt.analytics.service;

import java.util.List;
import java.util.Map;

import com.inn.foresight.module.nv.wpt.analytics.utils.wrapper.TopURLCountWrapper;
import com.inn.foresight.module.nv.wpt.analytics.utils.wrapper.WPTAnalysisGraphWrapper;

/** The Interface IWPTDashboardDetailService. */
public interface IWPTDashboardDetailService {

	/**
	 * Gets the filter list.
	 *
	 * @param geographyLevel the geography level
	 * @return the filter list
	 */
	Map<String, Object> getFilterList(String geographyLevel);

	/**
	 * Gets the target ip version percent.
	 *
	 * @param date the date
	 * @param processType the process type
	 * @param geographyLevel the geography level
	 * @param locationId the location id
	 * @param operator the operator
	 * @param technology the technology
	 * @return the target ip version percent
	 */
	Map<String, Double> getTargetIpVersionPercent(String date, String processType, String geographyLevel,
			Integer locationId, String operator, String technology);
	
	/**
	 * Gets the day wise counts.
	 *
	 * @param date the date
	 * @param processType the process type
	 * @param geographyLevel the geography level
	 * @param geographyId the geography id
	 * @param operator the operator
	 * @param technology the technology
	 * @return the day wise counts
	 */
	Map<String, Map<String, Long>> getDayWiseCounts(String date, String processType, String geographyLevel, Integer geographyId,
			String operator, String technology);
	
	/**
	 * Gets the top url counts.
	 *
	 * @param date the date
	 * @param processType the process type
	 * @param geographyLevel the geography level
	 * @param locationId the location id
	 * @param operator the operator
	 * @param technology the technology
	 * @return the top url counts
	 */
	Map<String, TopURLCountWrapper> getTopUrlCounts(String date, String processType, String geographyLevel,
			Integer locationId, String operator, String technology);
	
	/**
	 * Gets the URL details.
	 *
	 * @param date the date
	 * @param processType the process type
	 * @param geographyLevel the geography level
	 * @param geographyId the geography id
	 * @param operator the operator
	 * @param technology the technology
	 * @param isPingDataRequired the is ping data required
	 * @param ulimit the ulimit
	 * @param llimit the llimit
	 * @return the URL details
	 */
	Map<String, Object> getURLDetails(String date, String processType, String geographyLevel, Integer geographyId,
			String operator, String technology, Boolean isPingDataRequired,Integer ulimit,Integer llimit);

	/**
	 * Gets the avg TTFB detail.
	 *
	 * @param date the date
	 * @param processType the process type
	 * @param geographyLevel the geography level
	 * @param geographyId the geography id
	 * @param operator the operator
	 * @param technology the technology
	 * @return the avg TTFB detail
	 */
	Map<String, Double> getAvgTTFBDetail(String date, String processType, String geographyLevel, Integer geographyId,
			String operator, String technology);

	/**
	 * Gets the HTTP and HTTPS count.
	 *
	 * @param date the date
	 * @param processType the process type
	 * @param geographyLevel the geography level
	 * @param geographyId the geography id
	 * @param operator the operator
	 * @param technology the technology
	 * @return the HTTP and HTTPS count
	 */
	Map<String,  Object> getHTTPAndHTTPSCount(String date, String processType, String geographyLevel, Integer geographyId,
			String operator, String technology);

	/**
	 * Gets the trace route and TTFB data.
	 *
	 * @param map the map
	 * @param compareType the compare type
	 * @param date the date
	 * @param processType the process type
	 * @param geographyLevel the geography level
	 * @param geographyId the geography id
	 * @param operator the operator
	 * @param technology the technology
	 * @return the trace route and TTFB data
	 */
	Object getTraceRouteAndTTFBData(Map<String, List<Object>> map, String compareType, String date,
			String processType, String geographyLevel, Integer geographyId, String operator, String technology);

	/**
	 * Gets the TTFB and TDNS data.
	 *
	 * @param map the map
	 * @param compareType the compare type
	 * @param date the date
	 * @param processType the process type
	 * @param geographyLevel the geography level
	 * @param geographyId the geography id
	 * @param operator the operator
	 * @param technology the technology
	 * @return the TTFB and TDNS data
	 */
	Object getTTFBAndTDNSData(Map<String, List<Object>> map, String compareType, String date,
			String processType, String geographyLevel, Integer geographyId, String operator, String technology);

	/**
	 * Gets the TTFB category wise data.
	 *
	 * @param map the map
	 * @param compareType the compare type
	 * @param date the date
	 * @param processType the process type
	 * @param geographyLevel the geography level
	 * @param geographyId the geography id
	 * @param operator the operator
	 * @param technology the technology
	 * @return the TTFB category wise data
	 */
	Map<String, Map<String, Double[]>> getTTFBCategoryWiseData(Map<String, List<Object>> map, String compareType, String date,
			String processType, String geographyLevel, Integer geographyId, String operator, String technology);

	/**
	 * Gets the analysis graph data.
	 *
	 * @param compareType the compare type
	 * @param date the date
	 * @param processType the process type
	 * @param geographyLevel the geography level
	 * @param comapererTech the comaperer tech
	 * @param geographyId the geography id
	 * @param operator the operator
	 * @param categories the categories
	 * @param locations the locations
	 * @param technology the technology
	 * @return the analysis graph data
	 */
	WPTAnalysisGraphWrapper getAnalysisGraphData(String compareType,String date, String processType, String geographyLevel,
			List<Object> comapererTech, Integer geographyId, String operator, List<Object> categories, List<Object> locations, 
			String technology);

	/**
	 * Save and get filters.
	 *
	 * @param filterJSON the filter JSON
	 * @return the object
	 */
	Object saveAndGetFilters(String filterJSON);
	
}

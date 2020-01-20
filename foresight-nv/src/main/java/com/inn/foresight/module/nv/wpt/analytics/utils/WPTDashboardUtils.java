package com.inn.foresight.module.nv.wpt.analytics.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.inn.commons.Symbol;
import com.inn.commons.lang.NumberUtils;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.module.nv.wpt.analytics.constants.WPTAnalyticsConstants;
import com.inn.foresight.module.nv.wpt.analytics.model.WPTLocation;
import com.inn.foresight.module.nv.wpt.analytics.model.WebCategory;
import com.inn.foresight.module.nv.wpt.analytics.utils.wrapper.TopURLDetailWrapper;
import com.inn.foresight.module.nv.wpt.analytics.utils.wrapper.WPTDashboardKpiWrapper;

/** The Class WPTDashboardUtils. */
public class WPTDashboardUtils extends WPTAnalyticsConstants {

	/**
	 * Gets the percentage.
	 *
	 * @param totalCount the total count
	 * @param value the value
	 * @return the percentage
	 */
	public static Double getPercentage(Long totalCount, Long value) {
		try {
			return ((double) value/ totalCount) * CENT;
		} catch (Exception e) {
			return NumberUtils.DOUBLE_ZERO;
		}
	}
	
	/**
	 * Gets the average.
	 *
	 * @param protocolCount the protocol count
	 * @param dayCount the day count
	 * @return the average
	 */
	public static Double getAverage(Long protocolCount, Integer dayCount) {
		try {
			return roundOff((double)protocolCount / dayCount);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static Double roundOff(Double value) {
		try {
			return (value*100)/100D;
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Gets the long array from map.
	 *
	 * @param map the map
	 * @param key the key
	 * @return the long array from map
	 */
	public static Long[] getLongArrayFromMap(Map<String, Long[]> map, String key) {
		if(map.containsKey(key)) {
			return map.get(key);
		} else {
			return new Long[2];
		}
	}
	
	/**
	 * Gets the double array from map.
	 *
	 * @param map the map
	 * @param key the key
	 * @return the double array from map
	 */
	public static Map<String, Double[]> getDoubleArrayFromMap(Map<String, Map<String, Double[]>> map, String key) {
		if (map.containsKey(key)) {
			return map.get(key);
		} else {
			return new HashMap<>();
		}
	}
	
	/**
	 * Update long map for all dates.
	 *
	 * @param oldMap the old map
	 * @param timeConstantList the time constant list
	 * @return the map
	 */
	public static Map<String, Long> updateLongMapForAllDates(Map<String, Long> oldMap, List<String> timeConstantList) {
		Map<String, Long> map = new LinkedHashMap<>();
		for(String timeKey : timeConstantList) {
			map.put(timeKey, oldMap.get(timeKey));
		}		
		return map;
	}
	
	/**
	 * Update double map for all dates.
	 *
	 * @param oldMap the old map
	 * @param timeConstantList the time constant list
	 * @return the map
	 */
	public static Map<String, Double> updateDoubleMapForAllDates(Map<String, Double> oldMap, List<String> timeConstantList) {
		Map<String, Double> map = new LinkedHashMap<>();
		for(String timeKey : timeConstantList) {
			map.put(timeKey, oldMap.get(timeKey));
		}		
		return map;
	}
	
	/**
	 * Update long array map for all dates.
	 *
	 * @param oldMap the old map
	 * @param timeConstantList the time constant list
	 * @return the map
	 */
	public static Map<String, Long[]> updateLongArrayMapForAllDates(Map<String, Long[]> oldMap, List<String> timeConstantList) {
		Map<String, Long[]> map = new LinkedHashMap<>();
		for(String timeKey : timeConstantList) {
			map.put(timeKey, oldMap.get(timeKey));
		}		
		return map;
	}
	
	/**
	 * Update double array map for all dates.
	 *
	 * @param oldMap the old map
	 * @param timeConstantList the time constant list
	 * @return the map
	 */
	public static Map<String, Double[]> updateDoubleArrayMapForAllDates(Map<String, Double[]> oldMap, 
			List<String> timeConstantList) {
		Map<String, Double[]> map = new LinkedHashMap<>();
		for(String timeKey : timeConstantList) {
			map.put(timeKey, oldMap.get(timeKey));
		}		
		return map;
	}
	
	/**
	 * Update single kpi map with all keys.
	 *
	 * @param map the map
	 * @param comparIPVList the compar IPV list
	 */
	public static void updateSingleKpiMapWithAllKeys(Map<String, Map<String, Object>> map,
			List<Object> comparIPVList) {
		for(Object key : comparIPVList) {
			if(!map.containsKey(key)) {
				map.put((String) key, null);
			}
		}
	}
	
	/**
	 * Update kpi map with all keys.
	 *
	 * @param map the map
	 * @param comparIPVList the compar IPV list
	 */
	public static void updateKpiMapWithAllKeys(Map<String, Map<String, Double[]>> map,
			List<Object> comparIPVList) {
		for(Object key : comparIPVList) {
			if(!map.containsKey(key)) {
				map.put((String) key, null);
			}
		}
	}
	
	/**
	 * Gets the date constant list.
	 *
	 * @param date the date
	 * @param dateRangeParam the date range param
	 * @return the date constant list
	 */
	public static List<String> getDateConstantList(String date, String dateRangeParam) {
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(com.inn.core.generic.utils.Utils.parseStringToDate(date, DDMMYY));
		List<String> list = new ArrayList<>();
		if(dateRangeParam.equals(PROCESS_TYPE_DAILY)) {
			list = getPreviousSevenDays(calendar);
		} else if (dateRangeParam.equals(PROCESS_TYPE_WEEKLY)) {
			list = getPreviousSevenWeeks(calendar);
		} else if(dateRangeParam.equals(PROCESS_TYPE_MONTHLY)) {
			list = getPreviousSevenMonths(calendar);
		}
		return list;
	}

	/**
	 * Gets the previous seven months.
	 *
	 * @param calendar the calendar
	 * @return the previous seven months
	 */
	private static List<String> getPreviousSevenMonths(Calendar calendar) {
		return getPreviousSevenTimeConstants(calendar, Calendar.MONTH);
	}

	/**
	 * Gets the previous seven weeks.
	 *
	 * @param calendar the calendar
	 * @return the previous seven weeks
	 */
	private static List<String> getPreviousSevenWeeks(Calendar calendar) {
		return getPreviousSevenTimeConstants(calendar, Calendar.WEEK_OF_YEAR);
	}

	/**
	 * Gets the previous seven days.
	 *
	 * @param calendar the calendar
	 * @return the previous seven days
	 */
	private static List<String> getPreviousSevenDays(Calendar calendar) {
		return getPreviousSevenTimeConstants(calendar, Calendar.DATE);
	}
	
	/**
	 * Gets the previous seven time constants.
	 *
	 * @param calendar the calendar
	 * @param param the param
	 * @return the previous seven time constants
	 */
	private static List<String> getPreviousSevenTimeConstants(Calendar calendar, int param) {
		List<String> list = new ArrayList<>(PREVIOUS_TIME_CONSTANT);
		//Date Formaters
		DateFormat format = new SimpleDateFormat(DDMMYY);
		for(int i = ZERO_INT; i < PREVIOUS_TIME_CONSTANT ; i++) {
			list.add(format.format(calendar.getTime()));
			calendar.add(param, -ONE_INT);
		}
		return list;
	}
	
	/**
	 * Gets the filter and value map.
	 *
	 * @param geographyLevel the geography level
	 * @param compareType the compare type
	 * @param operator the operator
	 * @param geographyId the geography id
	 * @param camparerList the camparer list
	 * @return the filter and value map
	 * @throws RestException the rest exception
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String, List> getFilterAndValueMap(String geographyLevel, String compareType, String operator,
			Integer geographyId, List<Object> camparerList) {
		Map<String, List> map = new HashMap<>();
		
		List<String> filterList = getFilterNameList(geographyLevel, compareType, geographyId);
		List<String> paramList = new ArrayList<>();
		List<Object> valueList = new ArrayList<>();

		for(String fiterName : filterList) {
			if(fiterName.equals(GEOGRAPHYL1_FILTER)
					|| fiterName.equals(GEOGRAPHYL2_FILTER)
					|| fiterName.equals(GEOGRAPHYL3_FILTER)
					|| fiterName.equals(GEOGRAPHYL4_FILTER)) {
				paramList.add(GEOGRAPHY_ID);
				valueList.add(geographyId);
			} else if(fiterName.equals(GEOGRAPHYL1_TECHNOLOGY_FILTER)
					|| fiterName.equals(GEOGRAPHYL2_TECHNOLOGY_FILTER)
					|| fiterName.equals(GEOGRAPHYL3_TECHNOLOGY_FILTER)
					|| fiterName.equals(GEOGRAPHYL4_TECHNOLOGY_FILTER)
					|| fiterName.equals(OPERATOR_TECHNOLOGY_FILTER)) {
				paramList.add(COMPARATOR_TECH_LIST);
				valueList.add(camparerList);
			} else if (fiterName.equals(OPERATOR_FILTER)) {
				paramList.add(OPERATOR);
				valueList.add(operator);
			} else if (fiterName.equals(GEOGRAPHY_LEVEL_FILTER)) {
				paramList.add(GEOGRAPHY_LEVEL);
				valueList.add(getGeographyLevelColumnValue(geographyLevel));
			} else if(fiterName.equals(ALL_GEOGRAPHY_FILTER)) {
				paramList.add(null);
				valueList.add(null);
			}
		}
		map.put(FILTER_NAME, filterList);
		map.put(FILTER_PARAM, paramList);
		map.put(FILTER_VALUE, valueList);
		return map;
	}
	
	/**
	 * Gets the filter name list.
	 *
	 * @param geographyLevel the geography level
	 * @param compareType the compare type
	 * @param geographyId the geography id
	 * @return the filter name list
	 * @throws RestException the rest exception
	 */
	public static List<String> getFilterNameList(String geographyLevel, String compareType, Integer geographyId) {
		List<String> list = new ArrayList<>();
		if(geographyId != null && geographyId.equals(ALL_GEOGRAPHY_ID)) {
			list.add(GEOGRAPHY_LEVEL_FILTER);
			list.add(ALL_GEOGRAPHY_FILTER);
		} else {
			if(compareType.equals(GEOGRAPHY)) {
				list.add(getGeographyAndIpVersionFilter(geographyLevel));
				list.add(OPERATOR_FILTER);
			} else if (compareType.equals(OPERATOR)) {
				list.add(getGeographyFilter(geographyLevel));
				list.add(OPERATOR_TECHNOLOGY_FILTER);
			} else if (compareType.equals(GEOGRAPHY_ONLY)) {
				list.add(getGeographyFilter(geographyLevel));
			} else {
				throw new RestException(INVALID_COMPARE_TYPE);
			}
		}
		return list;
	}
	
	/**
	 * Gets the geography filter.
	 *
	 * @param geographyLevel the geography level
	 * @return the geography filter
	 * @throws RestException the rest exception
	 */
	public static String getGeographyFilter(String geographyLevel) {
		if(geographyLevel.equals(GEOGRAPHYL1)) {
			return GEOGRAPHYL1_FILTER;
		} else if(geographyLevel.equals(GEOGRAPHYL2)) {
			return GEOGRAPHYL2_FILTER;
		} else if(geographyLevel.equals(GEOGRAPHYL3)) {
			return GEOGRAPHYL3_FILTER;
		} else if(geographyLevel.equals(GEOGRAPHYL4)) {
			return GEOGRAPHYL4_FILTER;
		} else {
			throw new RestException(INVALID_GEOGRAPHY_LEVEL);
		}
	}

	/**
	 * Gets the geography and ip version filter.
	 *
	 * @param geographyLevel the geography level
	 * @return the geography and ip version filter
	 * @throws RestException the rest exception
	 */
	private static String getGeographyAndIpVersionFilter(String geographyLevel) {
		if(geographyLevel.equals(GEOGRAPHYL1)) {
			return GEOGRAPHYL1_TECHNOLOGY_FILTER;
		} else if(geographyLevel.equals(GEOGRAPHYL2)) {
			return GEOGRAPHYL2_TECHNOLOGY_FILTER;
		} else if(geographyLevel.equals(GEOGRAPHYL3)) {
			return GEOGRAPHYL3_TECHNOLOGY_FILTER;
		} else if(geographyLevel.equals(GEOGRAPHYL4)) {
			return GEOGRAPHYL4_TECHNOLOGY_FILTER;
		} else {
			throw new RestException(INVALID_GEOGRAPHY_LEVEL);
		}
	}
	
	/**
	 * Gets the geography level column value.
	 *
	 * @param geographyLevel the geography level
	 * @return the geography level column value
	 * @throws RestException the rest exception
	 */
	public static String getGeographyLevelColumnValue(String geographyLevel) {
		if(geographyLevel.equals(GEOGRAPHYL1)) {
			return L1;
		} else if(geographyLevel.equals(GEOGRAPHYL2)) {
			return L2;
		} else if(geographyLevel.equals(GEOGRAPHYL3)) {
			return L3;
		} else if(geographyLevel.equals(GEOGRAPHYL4)) {
			return L4;
		} else {
			throw new RestException(INVALID_GEOGRAPHY_LEVEL);
		}
	}
	
	/**
	 * Gets the json key for kpi comparison graph.
	 *
	 * @param geographyLevel the geography level
	 * @param compareType the compare type
	 * @param wrapper the wrapper
	 * @return the json key for kpi comparison graph
	 * @throws RestException the rest exception
	 */
	public static String getJsonKeyForKpiComparisonGraph(String geographyLevel, String compareType, WPTDashboardKpiWrapper wrapper) {
		String key;
		if(compareType.equals(GEOGRAPHY)) {
			key = getSelectedGeographyId(geographyLevel, wrapper.getGeographyL1(), wrapper.getGeographyL2(), 
					wrapper.getGeographyL3(), wrapper.getGeographyL4());
		} else if(compareType.equalsIgnoreCase(OPERATOR)){
			key = wrapper.getOperator();
		} else {
			throw new RestException(INVALID_COMPARE_TYPE);
		}
		key += ForesightConstants.UNDERSCORE + wrapper.getIpVersion();
		return key;
	}

	/**
	 * Gets the key for analysis graph.
	 *
	 * @param compareType the compare type
	 * @param geographyLevel the geography level
	 * @param detailWrapper the detail wrapper
	 * @return the key for analysis graph
	 * @throws RestException the rest exception
	 */
	public static String getKeyForAnalysisGraph(String compareType, String geographyLevel, TopURLDetailWrapper detailWrapper) {
		String key ;
		if(compareType.equalsIgnoreCase(OPERATOR)) {
			key = detailWrapper.getOperator();
		} else if(compareType.equalsIgnoreCase(GEOGRAPHY)){
			key = getSelectedGeographyId(geographyLevel, detailWrapper.getGeographyL1(), detailWrapper.getGeographyL2(), 
					detailWrapper.getGeographyL3(), detailWrapper.getGeographyL4());
		} else {
			throw new RestException(INVALID_COMPARE_TYPE);
		}
		key += Symbol.UNDERSCORE_STRING + detailWrapper.getIpVersion();
		return key;
	}
	
	/**
	 * Gets the selected geography id.
	 *
	 * @param geographyLevel the geography level
	 * @param geographyL1Id the geography L 1 id
	 * @param geographyL2Id the geography L 2 id
	 * @param geographyL3Id the geography L 3 id
	 * @param geographyL4Id the geography L 4 id
	 * @return the selected geography id
	 * @throws RestException the rest exception
	 */
	private static String getSelectedGeographyId(String geographyLevel, Integer geographyL1Id, Integer geographyL2Id,
			Integer geographyL3Id, Integer geographyL4Id) {
		if(geographyLevel.equals(GEOGRAPHYL1)) {
			return String.valueOf(geographyL1Id);
		} else if(geographyLevel.equals(GEOGRAPHYL2)) {
			return String.valueOf(geographyL2Id);
		} else if(geographyLevel.equals(GEOGRAPHYL3)) {
			return String.valueOf(geographyL3Id);
		} else if (geographyLevel.equalsIgnoreCase(GEOGRAPHYL4)){
			return String.valueOf(geographyL4Id);
		} else {
			throw new RestException(INVALID_GEOGRAPHY_LEVEL);
		}
	}
	
	/**
	 * Update sub map.
	 *
	 * @param key the key
	 * @param map the map
	 * @param subKey the sub key
	 * @param value the value
	 */
	public static void updateSubMap(String key, Map<String, Map<String, Object>> map, String subKey, Object value) {
		if(map.containsKey(key)) {
			map.get(key).put(subKey, value);
		} else { 
			Map<String, Object> subMap = new HashMap<>();
			subMap.put(subKey, value);
			map.put(key,subMap);
		}
	}
	
	/**
	 * Update category list.
	 *
	 * @param list the list
	 * @return the list
	 */
	public static List<WebCategory>  updateCategoryList(List<WebCategory> list) {
		WebCategory zeroIndexWrapper = list.get(ZERO_INT);
		Integer allWrapperIndex = null;
		for(WebCategory w : list) {
			if(w.getCategoryName().equalsIgnoreCase(ALL)) {
				allWrapperIndex = list.indexOf(w);
				break;
			}
		}
		list.set(ZERO_INT, list.get(allWrapperIndex));
		list.set(allWrapperIndex, zeroIndexWrapper);
		return list;
	}

	/**
	 * Update location list.
	 *
	 * @param list the list
	 * @return the list
	 */
	public static List<WPTLocation> updateLocationList(List<WPTLocation> list) {
		WPTLocation zeroIndexWrapper = list.get(ZERO_INT);
		Integer allWrapperIndex = null;
		for(WPTLocation w : list) {
			if(w.getName().equalsIgnoreCase(ALL)) {
				allWrapperIndex = list.indexOf(w);
				break;
			}
		}
		list.set(ZERO_INT, list.get(allWrapperIndex));
		list.set(allWrapperIndex, zeroIndexWrapper);
		return list;
	}

}

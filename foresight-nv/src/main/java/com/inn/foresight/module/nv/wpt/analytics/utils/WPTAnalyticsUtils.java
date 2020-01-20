package com.inn.foresight.module.nv.wpt.analytics.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTimeConstants;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.lang.NumberUtils;
import com.inn.commons.lang.StringUtils;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.utils.Utils;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.module.nv.wpt.analytics.constants.WPTAnalyticsConstants;
import com.inn.foresight.module.nv.wpt.analytics.utils.wrapper.WPTRawDataWrapper;
import com.inn.foresight.module.nv.wpt.analytics.utils.wrapper.WPTStatesWrapper;

/** The Class WPTAnalyticsUtils. */
public class WPTAnalyticsUtils extends WPTAnalyticsConstants{	

	/** The logger. */
	private static Logger logger = LogManager.getLogger(WPTAnalyticsUtils.class);
	
	/**
	 * Merging Two Stats JSON  by Adding them.
	 *
	 * @param newStats the new stats
	 * @param oldStats the old stats
	 * @return Merged Stats JSON
	 */
	public static String mergeStatsJSON(String newStats, String oldStats) {
		if(newStats != null) {
			StringBuilder stats = new StringBuilder();
			String[] newArr = newStats.split(COMMA);
			String[] oldArr = oldStats.split(COMMA);
			for (int sec = NumberUtils.INTEGER_ZERO; sec < DateTimeConstants.SECONDS_PER_MINUTE; sec++) {
				stats.append(Integer.parseInt(newArr[sec].trim()) + Integer.parseInt(oldArr[sec].trim())).append(COMMA);
			}
			return stats.substring(NumberUtils.INTEGER_ZERO, stats.length() - NumberUtils.INTEGER_ONE);
		}return oldStats;
	}
	
	/**
	 * Merging two scatterPlot JSON by calculating Avgs at same coordinates.
	 *
	 * @param newScatter the new scatter
	 * @param oldScatter the old scatter
	 * @return merged scatterPlot JSON
	 * @throws JSONException the JSON exception
	 */
	public static String mergeScatterChartJSON(String newScatter, String oldScatter) throws JSONException {
		if(newScatter != null) {
			Map<String, String> newMap = getScatterMap(newScatter);
			Map<String, String> oldMap = getScatterMap(oldScatter);
			for (Map.Entry<String, String> entry : oldMap.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				if (key != null && value != null && value.contains(UNDERSCORE)) {
					Integer count = Integer.parseInt(value.split(UNDERSCORE)[ForesightConstants.ZERO]);
					Double avg = Double.parseDouble(value.split(UNDERSCORE)[ForesightConstants.ZERO]);
					Double total = avg * count;
					if (newMap.containsKey(key) && newMap.get(key) != null) {
						Integer countMap = Integer.parseInt(newMap.get(key).split(UNDERSCORE)[ForesightConstants.ONE]);
						Double totalMap = (Double.parseDouble(newMap.get(key).split(UNDERSCORE)[ForesightConstants.ZERO]) * countMap);
						Integer finalCount = count + countMap;
						Double finalTotal = total + totalMap;
						Double finalAvg = (finalTotal / finalCount);
						newMap.put(key, finalAvg + UNDERSCORE + finalCount);
					} else {
						newMap.put(key, value);
					}
				}
			}
			return convertMapToScatterPlot(newMap);
		}else {
			return oldScatter;
		}
	}
     
	/**
	 * Parse scatterPlot map to scatterPlot JSON .
	 *
	 * @param scatterMap the scatter map
	 * @return scatterPlot JSON
	 */
	private static String convertMapToScatterPlot(Map<String,String> scatterMap) {
		String finalResult = SQUARE_BRACES_OPEN;
		StringBuilder finalJSON = new StringBuilder();
		finalJSON.append(SQUARE_BRACES_OPEN);
		if (scatterMap != null && !scatterMap.isEmpty()) {
			for (Map.Entry<String, String> entry : scatterMap.entrySet()) {
				Integer x = Integer.parseInt(entry.getKey().split(UNDERSCORE)[ForesightConstants.ZERO]);
				Integer y = Integer.parseInt(entry.getKey().split(UNDERSCORE)[ForesightConstants.ONE]);
				Double avg = Double.parseDouble(entry.getValue().split(UNDERSCORE)[ForesightConstants.ZERO]);
				Integer count = Integer.parseInt(entry.getValue().split(UNDERSCORE)[ForesightConstants.ONE]);
				finalJSON.append(CURLY_BRACES_OPEN)
				.append(X).append(x).append(COMMA)
				.append(Y).append(y).append(COMMA)
				.append(VALUE).append(avg).append(COMMA)
				.append(COUNT).append(count)
				.append(CURLY_BRACES_CLOSE).append(COMMA);
			}
			finalResult = finalJSON.substring(ForesightConstants.ZERO, finalJSON.length() - ForesightConstants.ONE);
		}
		return finalResult + SQUARE_BRACES_CLOSE;
	}
	
	/**
	 * Parse scatterPlot JSON to scatterPlot map.
	 *
	 * @param scatterPlot JSON
	 * @return scatterPlot map
	 * @throws JSONException the JSON exception
	 */
	private static Map<String, String> getScatterMap(String scatterPlot) throws JSONException {
		Map<String, String> scatterMap = new HashMap<>();
		JSONArray array = new JSONArray(scatterPlot);
		for (int size = ForesightConstants.ZERO; size < array.length(); size++) {
			JSONObject object = array.getJSONObject(size);
			Integer x = object.getInt(X_KEY);
			Integer y = object.getInt(Y_KEY);
			Double avg = object.getDouble(VALUE_KEY);
			Integer count = object.getInt(COUNT_KEY);
			String key = x + UNDERSCORE + y;
			scatterMap.put(key, avg + UNDERSCORE + count);
		}
		return scatterMap;
	}
	
	/**
	 * Merging old & new categoryWise maps of TotalResponseTime average.
	 *
	 * @param newMap the new map
	 * @param oldMap the old map
	 * @return merged categoryWise map of TotalResponseTime average
	 */
	public static Map<String, Float> mergeCategoryMap(Map<String, Float> newMap, Map<String, Float> oldMap) {
		for (Map.Entry<String, Float> entry : oldMap.entrySet()) {
			String category = entry.getKey();
			Float total1 = entry.getValue();
			Float total2 = (newMap.containsKey(category) && newMap.get(category) != null)
					? newMap.get(category)
					: NumberUtils.FLOAT_ZERO;
			newMap.put(category, total1 + total2);
		}
		return newMap;
	}
	
	/**
	 * Merging old & new categoryWise maps of TotalResponseTime count.
	 *
	 * @param newMap the new map
	 * @param oldMap the old map
	 * @return merged categoryWise map of TotalResponseTime count
	 */
	public static Map<String, Integer> mergeCountMap(Map<String, Integer> newMap, Map<String, Integer> oldMap) {
		for (Map.Entry<String, Integer> entry : oldMap.entrySet()) {
			String category = entry.getKey();
			Integer total1 = entry.getValue();
			Integer total2 = (newMap.containsKey(category) && newMap.get(category) != null)
					? newMap.get(category)
					: NumberUtils.INTEGER_ZERO;
			newMap.put(category, total1 + total2);

		}
		return newMap;
	}

	/**
	 * Merging categoryAvg and count maps of TotalResponseTime of single date in a single WPTStatesWrapper.
	 *
	 * @param wrapperList the wrapper list
	 * @return List of WPTStatesWrapper having merged categoryWise
	 */
	public static List<WPTStatesWrapper> mergeWPTWrapperForDate(List<WPTStatesWrapper> wrapperList) {
		Map<String, List<WPTStatesWrapper>> wrapperMap = getDateWiseMapFromList(wrapperList);
		wrapperList.clear();
		for(Map.Entry<String, List<WPTStatesWrapper>> entry : wrapperMap.entrySet()) {
			List<WPTStatesWrapper> list = entry.getValue();
			WPTStatesWrapper statsWrapper = list.get(NumberUtils.INTEGER_ZERO);
			for(WPTStatesWrapper wrapper : list) {
				mergeAndSetStatsWrapper(statsWrapper, wrapper);
			}
			wrapperList.add(statsWrapper);
		}
		return wrapperList;
	}

	/**
	 * Getting Map by arranging wrapper of Same dates into single list.
	 *
	 * @param wrapperList the wrapper list
	 * @return Date-Wise Map of Stats Wrappers
	 */
	private static Map<String, List<WPTStatesWrapper>> getDateWiseMapFromList(List<WPTStatesWrapper> wrapperList) {
		Map<String, List<WPTStatesWrapper>> wrapperMap = new HashMap<>();
		for(WPTStatesWrapper wrapper : wrapperList) {
			if(wrapperMap.containsKey(wrapper.getCreationDate()) && wrapperMap.get(wrapper.getCreationDate()) != null) {
				wrapperMap.get(wrapper.getCreationDate()).add(wrapper);
			} else {
				List<WPTStatesWrapper> wrappers = new ArrayList<>();
				wrappers.add(wrapper);
				wrapperMap.put(wrapper.getCreationDate(), wrappers);
			}
		}
		return wrapperMap;
	}

	/**
	 * Setting All categoryAvg and category count maps into single wrapper.
	 *
	 * @param statsWrapper the stats wrapper
	 * @param wrapper the wrapper
	 */
	private static void mergeAndSetStatsWrapper(WPTStatesWrapper statsWrapper, WPTStatesWrapper wrapper) {
		statsWrapper.getCategoryAvg().putAll(wrapper.getCategoryAvg());
		statsWrapper.getCategoryCount().putAll(wrapper.getCategoryCount());
	}

	/**
	 * Merging Two categoryAvg maps into single map by calculating there total averages using counts
	 * and Setting into merged average into newAvgMap.
	 *
	 * @param newAvgMap the new avg map
	 * @param newCountMap the new count map
	 * @param oldAvgMap the old avg map
	 * @param oldCountMap the old count map
	 */
	public static void mergeCategoryAvgMap(Map<String, Float> newAvgMap, 
			Map<String, Integer> newCountMap, 
			Map<String, Float> oldAvgMap, 
			Map<String, Integer> oldCountMap) {

		for (Entry<String, Float> entry : oldAvgMap.entrySet()) {
			String categoryName = entry.getKey();

			Float oldAvg = entry.getValue();
			Integer oldCount = oldCountMap.get(categoryName);
			Float newAvg = newAvgMap.get(categoryName);
			Integer newCount = newCountMap.get(categoryName);

			if (newAvg != null && newCount != null) {
				Float oldTotalValue = getTotalValue(oldAvg, oldCount);
				Float newTotalValue = getTotalValue(newAvg, newCount);

				oldCount = getTotalCount(oldCount, newCount);
				oldAvg = getTotalAvg(oldTotalValue, newTotalValue, oldCount);
			}
			newAvgMap.put(categoryName, oldAvg);
			newCountMap.put(categoryName, oldCount);
		}
	}
	
	/**
	 * Getting total count by adding them or returning Zero otherwise.
	 *
	 * @param oldCount the old count
	 * @param newCount the new count
	 * @return Added Count
	 */
	private static Integer getTotalCount(Integer oldCount, Integer newCount) {
		if(oldCount != null && newCount != null) {
			return oldCount + newCount;
		} else if(oldCount != null) {
			return oldCount;
		} else if (newCount != null) {
			return newCount;
		}
		return NumberUtils.INTEGER_ZERO;
	}

	/**
	 * Getting Total Average from Total values and count.
	 *
	 * @param oldTotalValue the old total value
	 * @param newTotalValue the new total value
	 * @param totalCount the total count
	 * @return Calculated Average
	 */
	private static Float getTotalAvg(Float oldTotalValue, Float newTotalValue, Integer totalCount) {
		Float avg = NumberUtils.FLOAT_ZERO;
		if(oldTotalValue != null && newTotalValue != null && totalCount != null) {
			try {
				avg = (oldTotalValue + newTotalValue) / totalCount;
			}catch(NumberFormatException e) {
				return avg;
			}
		}
		return avg;
	}

	/**
	 * Getting Original value from Average and count.
	 *
	 * @param avg the avg
	 * @param count the count
	 * @return Calcluted Value
	 */
	private static Float getTotalValue(Float avg, Integer count) {
		if(avg != null && count != null) {
			return avg * count;
		}
		return NumberUtils.FLOAT_ZERO;
	}
	
	/**
	 * Getting minTimestamp,, maxTimestamp for Raw Data Hbase Scan.
	 *
	 * @param from the from
	 * @param to the to
	 * @return Long Array contain min and max Timestamp
	 */
	public static Long[] getTimeRange(String from, String to) {
		Date fromDate = Utils.parseStringToDate(from, DDMMYY);
		Date toDate = Utils.parseStringToDate(to, DDMMYY);
		
		Calendar fromCal = Calendar.getInstance();
		fromCal.setTime(fromDate);
		
		Calendar toCal = Calendar.getInstance();
		toCal.setTime(toDate);
		toCal.set(Calendar.HOUR, 23);
		toCal.set(Calendar.MINUTE, 59);
		toCal.set(Calendar.SECOND, 59);
		toCal.set(Calendar.MILLISECOND, 999);
		
		return new Long[] {fromCal.getTimeInMillis(), toCal.getTimeInMillis()};
	}
	
	/**
	 * Getting File Name for Detailed Log Report CSV.
	 *
	 * @param city the city
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return File Name
	 */
	public static String getFileNameForReport(String city, String fromDate, String toDate) {
		String fileName = WPT_ANALATICS_LOG_REPORT_NAME + UNDERSCORE + city;
		fileName = fileName + UNDERSCORE + fromDate + UNDERSCORE + toDate;
		return fileName + CSV_EXTENSION;
	}
	
	/**
	 * Getting Top Header For Writing in Detailed Log Report CSV.
	 *
	 * @param city the city
	 * @param fromDate the from date
	 * @param toDate the to date
	 * @return Top Header String
	 * @throws RestException the rest exception
	 */
	public static String getHeaderForReport(String city, String fromDate, String toDate) {
		StringBuilder header = new StringBuilder();
		header.append(DATERANGE).append(COMMA);
		header.append(getDateRangeString(fromDate, toDate)).append(NEW_LINE);
		header.append(CITY_CAMELCASE).append(COMMA);
		header.append(city).append(NEW_LINE).append(NEW_LINE);
		header.append(ConfigUtils.getString(WPT_ANALATICS_LOG_REPORT_HEADER).replace(UNDERSCORE, COMMA));
		return header.toString();
	}
	
	/**
	 * Getting Date Range String For Writing in Top Header of Detailed Log Report CSV.
	 *
	 * @param fromString the from string
	 * @param toString the to string
	 * @return Date Range String
	 * @throws RestException the rest exception
	 */
	private static String getDateRangeString(String fromString, String toString) {
		Date fromDate = Utils.parseStringToDate(fromString, DDMMYY);
		Date toDate = Utils.parseStringToDate(toString, DDMMYY);
		
		fromString = Utils.parseDateToStringFormat(fromDate, DDMMYYYY_SLASH);
		toString = Utils.parseDateToStringFormat(toDate, DDMMYYYY_SLASH);

		return fromString + SPACE + COLON + SPACE + toString;
	}

	/**
	 * Writing Data into Detailed Log Report CSV.
	 *
	 * @param csv the csv
	 * @param dataList (Contains Raw Data from HBase)
	 */
	public void writeIntoCSV(StringBuilder csv, List<WPTRawDataWrapper> dataList){
		for(WPTRawDataWrapper wrapper : dataList) {
			csv.append(validate(wrapper.getGeography())).append(COMMA);
			csv.append(validate(wrapper.getCategory())).append(COMMA);
			csv.append(validate(wrapper.getWebUrl())).append(COMMA);		
			csv.append(validate(wrapper.getTestType())).append(COMMA);
			csv.append(validate(wrapper.getNetworkType())).append(COMMA);
			csv.append(validate(wrapper.getOperatorName())).append(COMMA);
			csv.append(validate(wrapper.getMcc())).append(COMMA);
			csv.append(validate(wrapper.getMnc())).append(COMMA);
			csv.append(validate(wrapper.getCellId())).append(COMMA);
			csv.append(validate(wrapper.getImei())).append(COMMA);
			csv.append(validate(wrapper.getImsi())).append(COMMA);
			csv.append(validate(wrapper.getLatitude())).append(COMMA);
			csv.append(validate(wrapper.getLongitude())).append(COMMA);
			csv.append(validate(wrapper.getMake())).append(COMMA);
			csv.append(validate(wrapper.getModel())).append(COMMA);		
			csv.append(validate(wrapper.getDeviceOS())).append(COMMA);
			csv.append(validate(wrapper.getBatteryLevel())).append(COMMA);
			csv.append(validate(wrapper.getVoltage())).append(COMMA);
			csv.append(validate(wrapper.getTemperature())).append(COMMA);
			csv.append(validate(wrapper.getIsDualSim())).append(COMMA);
			csv.append(validate(wrapper.getLocCategory())).append(COMMA);
			csv.append(validate(wrapper.getLocInfo())).append(COMMA);
			csv.append(validate(wrapper.getGpsStatus())).append(COMMA);
			csv.append(validate(wrapper.getBaseband())).append(COMMA);		
			csv.append(validate(wrapper.getBuildNumber())).append(COMMA);
			csv.append(validate(wrapper.getDeviceChipSet())).append(COMMA);
			csv.append(validate(wrapper.getDeviceSerialNo())).append(COMMA);
			csv.append(validate(wrapper.getDeviceCoreArch())).append(COMMA);
			csv.append(validate(wrapper.getIsAutoDataTime())).append(COMMA);
			csv.append(validate(wrapper.getAppVersion())).append(COMMA);
			csv.append(validate(wrapper.getAddress())).append(COMMA);
			csv.append(validate(wrapper.getTestStartTime())).append(COMMA);
			csv.append(validate(wrapper.getTestEndTime())).append(COMMA);	
			csv.append(validate(wrapper.getFirstDNSResolutionTime())).append(COMMA);
			csv.append(validate(wrapper.getTotalDnsResolutionTime())).append(COMMA);
			csv.append(validate(wrapper.getTargetedIp())).append(COMMA);
			csv.append(validate(wrapper.getFirstByteResponseTime())).append(COMMA);
			csv.append(validate(wrapper.getTotalResponseTime())).append(COMMA);
			csv.append(validate(wrapper.getAvgPingTime())).append(COMMA);
			csv.append(validate(wrapper.getTraceRoute())).append(COMMA);
			csv.append(validate(wrapper.getIsIpv6())).append(COMMA);
			csv.append(validate(wrapper.getIpVersion())).append(COMMA);			
			csv.append(validate(wrapper.getPci())).append(COMMA);
			csv.append(validate(wrapper.getTac())).append(COMMA);
			csv.append(validate(wrapper.getAvgRsrp())).append(COMMA);
			csv.append(validate(wrapper.getAvgSinr())).append(COMMA);
			csv.append(validate(wrapper.getAvgRsrq())).append(COMMA);
			csv.append(validate(wrapper.getAvgRssi())).append(NEW_LINE);
		}
	}

	/**
	 * Cheking for null before writing in report.
	 *
	 * @param data the data
	 * @return data or Hiphen
	 */
	private Object validate(Object data) {
		if(data != null) {
			return data;
		}
		return HIPHEN;
	}
	
	
	/**
	 * Gets the formatted date.
	 *
	 * @author Aishwary
	 * @param timeStamp the time stamp
	 * @param dateFormat the date format
	 * @return It will return the time in given format
	 */
	public static String getFormattedDate(String timeStamp, String dateFormat) {

		try {
			Date date = new Date(Long.valueOf(timeStamp));
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			return sdf.format(date);
		} catch (NumberFormatException e) {
			logger.error("error in parsing timestamp : {},  for time : {} ", e.getMessage(), timeStamp);
		}
		return null;
	}
	
	
	
	/**
	 * Checks for valid value.
	 *
	 * @author Aishwary
	 * @param value the value
	 * @return Boolean value true/false.
	 */
	public static boolean hasValidValue(String value) {
		if (value != null && value.trim().length() > WPTAnalyticsConstants.ZERO_INT 
				&& !value.trim().equalsIgnoreCase(WPTAnalyticsConstants.NULL_STRING) 
				&& !value.trim().equalsIgnoreCase(WPTAnalyticsConstants.NAN) 
				&& !value.trim().equalsIgnoreCase(WPTAnalyticsConstants.DASH)
				&& !value.trim().equalsIgnoreCase(WPTAnalyticsConstants.BLANK_STRING)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Gets the masked IMSI or IMEI.
	 *
	 * @param value / imei
	 * @return masked imsi . imei(last 4 digit will be visible) e.g. XXXXXXXXXXX6797
	 */
	public static String getMaskedIMSIOrIMEI(String value) {
		int length = value.length();
		if (length >= ForesightConstants.FOUR) {
			return MASKED_IMSI_PREFIX + StringUtils.substring(value, length-ForesightConstants.FOUR);
		}
		return value;
	}

	/**
	 * Gets the checks if is https.
	 *
	 * @param url the url
	 * @return the checks if is https
	 */
	public static Boolean getIsHttps(String url) {
		if(url.contains(HTTPS)) {
			return true;
		}
		return false;
	}
	
	
	
}


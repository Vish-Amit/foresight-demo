package com.inn.foresight.module.nv.layer3.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.math3.util.Precision;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jfree.data.time.Quarter;
import org.jfree.data.time.Year;
import org.json.JSONException;

import com.google.gson.Gson;
import com.inn.commons.Symbol;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.hadoop.hbase.HBaseResult;
import com.inn.commons.http.HttpGetRequest;
import com.inn.commons.http.HttpPostRequest;
import com.inn.commons.lang.StringUtils;
import com.inn.commons.maps.LatLng;
import com.inn.commons.maps.grid.DegreeGrid;
import com.inn.commons.unit.Duration;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ConfigEnum;
import com.inn.foresight.core.generic.utils.ConfigUtil;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.NVConfigUtil;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder.TemplateType;
import com.inn.foresight.module.nv.layer3.constants.NVLayer3Constants;
import com.inn.foresight.module.nv.layer3.constants.QMDLConstant;
import com.inn.foresight.module.nv.layer3.qmdlParser.wrappers.QMDLLogCodeWrapper;
import com.inn.foresight.module.nv.report.utils.ReportConstants;
import com.inn.foresight.module.nv.workorder.model.WOFileDetail;
import com.inn.foresight.module.nv.workorder.recipe.wrapper.RecipeWrapper;

/**
 * The Class NVLayer3Utils.
 *
 * @author innoeye date - 29-Dec-2017 1:53:12 PM
 */

public class NVLayer3Utils implements NVLayer3Constants{

	/** The logger. */
	private static Logger logger = LogManager.getLogger(NVLayer3Utils.class);
	private static Map<Integer,Integer> bandFrequencyMap=new HashMap<>();

	/**
	 * Extract file items from http request.
	 * 
	 * @param request
	 *            the HttpServletRequest
	 * @return the list of FileItem
	 * @throws FileUploadException
	 *             the file upload exception
	 */
	public static List<FileItem> extractFileItemsFromHttpRequest(HttpServletRequest request)
			throws FileUploadException {
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload fileUpload = new ServletFileUpload(factory);
		return fileUpload.parseRequest(request);
	}

	/**
	 * Send http get request.
	 *
	 * @param url
	 *            the url
	 * @param isToEnableTimeOut
	 *            the is to enable time out
	 * @param duration
	 *            the duration
	 * @return HttpGetRequest
	 */

	public static HttpGetRequest sendHttpGetRequest(String url, boolean isToEnableTimeOut, Duration duration) {
		logger.info("Sending Http Get Request, url {}, isToEnableTimeOut {}, duration {}", url, isToEnableTimeOut,
				duration);
		HttpGetRequest httpGetRequest = new HttpGetRequest(url);
		if (duration != null) {
			httpGetRequest.setConnectionTimeout(duration);
			httpGetRequest.setEnableTimeout(isToEnableTimeOut);
		}
		return httpGetRequest;
	}

	/**
	 * Send http post request.
	 *
	 * @param url
	 *            the url
	 * @param httpEntity
	 *            the http entity
	 * @param isToEnableTimeOut
	 *            the is to enable time out
	 * @param duration
	 *            the duration
	 * @return the http post request
	 */
	public static HttpPostRequest sendHttpPostRequest(String url, StringEntity httpEntity, boolean isToEnableTimeOut,
			Duration duration) {
		try {
			HttpPostRequest httpPostRequest = new HttpPostRequest(url, httpEntity);
			if (duration != null) {
				httpPostRequest.setConnectionTimeout(duration);
				httpPostRequest.setEnableTimeout(isToEnableTimeOut);
			}
			return httpPostRequest;
		} catch (Exception e) {
			throw new RestException(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}

	/**
	 * Gets the start milli of minute.
	 *
	 * @param mills
	 *            the mills
	 * @return the start milli of minute
	 */
	public static Long getStartMilliOfMinute(Long mills) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(mills);
		calendar.set(Calendar.MILLISECOND, QMDLConstant.ZERO_MILLI_SECOND);
		calendar.set(Calendar.SECOND, QMDLConstant.ZERO_SECOND);
		return calendar.getTimeInMillis();
	}

	/**
	 * Gets the file type.
	 *
	 * @param fileName
	 *            the file name
	 * @return the file type
	 */
	public static String getFileType(String fileName) {
		if (fileName.endsWith(NVLayer3Constants.QMDL_FILE_EXTENTION)) {
			return NVLayer3Constants.QMDL_FILE_EXTENTION.replace(".", "").toUpperCase();
		} else if (fileName.endsWith(NVLayer3Constants.CSV_FILE_EXTENTION)) {
			return NVLayer3Constants.CSV_FILE_EXTENTION.replace(".", "").toUpperCase();
		}
		return null;
	}

	
	public static String getPaddedKeyForMap(Map<String, String> map, String key, String padding) {
		if (!map.containsKey(key + padding)) {
			return key + padding;
		} else {
			Integer value = Integer.parseInt(padding);
			padding =String.valueOf(++value);
			return getPaddedKeyForMap(map, key, padding);
		}
	}
	
	public static String getPaddedKeyForMapForLayer3(Map<String, Map<String, String>> map, String key, String padding) {
		if (!map.containsKey(key + padding)) {
			return key + padding;
		} else {
			Integer value = Integer.parseInt(padding);
			padding =String.valueOf(++value);
			return getPaddedKeyForMapForLayer3(map, key, padding);
		}
	}
	
	
	
	public static String getPaddedKeyForLayer3(Map<String, List<RecipeWrapper>> map, String key, String padding) {
		if (!map.containsKey(key + padding)) {
			return key + padding;
		} else {
			Integer value = Integer.parseInt(padding);
			padding =String.valueOf(++value);
			return getPaddedKeyForLayer3(map, key, padding);
		}
	}
	
	public static HttpResponse sendPostRequestWithoutTimeOut(String uri, StringEntity httpEntity) throws IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(uri);
		try {
			logger.info("Going to hit URL : {}",uri);
			httpPost.setEntity(httpEntity);
			CloseableHttpResponse response = httpClient.execute(httpPost);
			
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				throw new RestException("Failed with HTTP error code : " + statusCode);
			}
				logger.info("Return Response Code : {}",statusCode);
				return response;
		} catch (Exception e) {
			throw new RestException(ReportConstants.EXCEPTION_ON_CONNECTION);
		} finally {
			httpClient.close();
		}
	}



	/**
	 * Merge stats.
	 *
	 * @param oldStats
	 *            the old stats
	 * @param newStats
	 *            the new stats
	 * @return the string
	 */
	public static String mergeStats(String oldStats, String newStats) {
		if (oldStats == null) {
			return newStats;
		} else if (newStats == null) {
			return oldStats;
		} else {
			Gson gson = new Gson();
			Long[] newStatsArray = gson.fromJson(newStats, Long[].class);
			Long[] oldStatsArray = gson.fromJson(oldStats, Long[].class);
			for (int i = NumberUtils.INTEGER_ZERO; i < newStatsArray.length; i++) {
				oldStatsArray[i] += newStatsArray[i];
			}
			return gson.toJson(oldStatsArray);
		}
	}

	/**
	 * @param value1
	 *            first double value
	 * @param value2
	 *            second double value
	 * @return avg of both values if they r not null if any one is null another one
	 *         is return
	 */
	public static Double calculateAvg(Double value1, Double value2) {
		if (value1 == null) {
			return value2;
		} else if (value2 == null) {
			return value1;
		} else {
			return ((value1 + value2) / QMDLConstant.TWO);
		}
	}

	/**
	 * Search and return nearest record in the map to given key.
	 *
	 * @param map
	 *            the sorted map in which need to search nearest record to the key
	 * @param key
	 *            the key to search in map
	 * @return the nearest record to the key
	 */
	public static Put searchNearestRecord(NavigableMap<Long, Put> map, Long key) {
		Map.Entry<Long, Put> lowerEntry = map.floorEntry(key);
		Map.Entry<Long, Put> higherEntry = map.ceilingEntry(key);
		Put res = null;
		if (lowerEntry != null && higherEntry != null) {
			res = Math.abs(key - lowerEntry.getKey()) > Math.abs(key - higherEntry.getKey()) ? higherEntry.getValue()
					: lowerEntry.getValue();
		} else if (lowerEntry != null || higherEntry != null) {
			res = lowerEntry != null ? lowerEntry.getValue() : higherEntry.getValue();
		}
		return res;
	}


	/**
	 * Search and return nearest record in the map to given key.
	 *
	 * @param map
	 *            the sorted map in which need to search nearest record to the key
	 * @param key
	 *            the key to search in map
	 * @return the nearest record to the key
	 */
	public static QMDLLogCodeWrapper searchNearestRecordWrapper(NavigableMap<Long, QMDLLogCodeWrapper> map, Long key) {
		Map.Entry<Long, QMDLLogCodeWrapper> lowerEntry = map.floorEntry(key);
		Map.Entry<Long, QMDLLogCodeWrapper> higherEntry = map.ceilingEntry(key);
		QMDLLogCodeWrapper res = null;
		if (lowerEntry != null && higherEntry != null) {
			res = Math.abs(key - lowerEntry.getKey()) > Math.abs(key - higherEntry.getKey()) ? higherEntry.getValue()
					: lowerEntry.getValue();
		} else if (lowerEntry != null || higherEntry != null) {
			res = lowerEntry != null ? lowerEntry.getValue() : higherEntry.getValue();
		}
		return res;
	}
	
	
	/**
	 * Search and return nearest record in the map to given key.
	 *
	 * @param map
	 *            the sorted map in which need to search nearest record to the key
	 * @param key
	 *            the key to search in map
	 * @return the nearest record to the key
	 */
	public static QMDLLogCodeWrapper searchNearestRecordWrapperAndGetHigherEntry(NavigableMap<Long, QMDLLogCodeWrapper> map, Long key) {
		Map.Entry<Long, QMDLLogCodeWrapper> lowerEntry = map.floorEntry(key);
		Map.Entry<Long, QMDLLogCodeWrapper> higherEntry = map.ceilingEntry(key);
		QMDLLogCodeWrapper res = null;
		if (lowerEntry != null && higherEntry != null) {
			res = Math.abs(key - lowerEntry.getKey()) > Math.abs(key - higherEntry.getKey()) ? higherEntry.getValue()
					: lowerEntry.getValue();
		} else if (lowerEntry != null || higherEntry != null) {
			res = higherEntry != null ? higherEntry.getValue() : lowerEntry.getValue();
		}
		return res;
	}

	/**
	 * Adds the string to put.
	 *
	 * @param put
	 *            object in which add qualifier
	 * @param value
	 *            value of qualifier
	 * @param qualifierName
	 *            qualier name
	 * @param family
	 *            column family of table
	 */
	public static void addStringToPut(Put put, Object value, String qualifierName, String family) {
		if (value != null) {
				put.addColumn(family.getBytes(), qualifierName.getBytes(), String.valueOf(value).getBytes());
		}
	}

	/**
	 * Gets the row key for data.
	 *
	 * @param keyWithPad
	 *            postfix value of rowkey
	 * @param rowPrefix
	 *            prefix value of rowKey
	 * @return prefix and postfix value seperated with delimeter
	 */
	public static String getrowKeyForData(String keyWithPad, String rowPrefix) {
		if (keyWithPad != null) {
			return rowPrefix + keyWithPad;
		}
		return null;
	}

	/**
	 * Gets the row key for data.
	 *
	 * @param key
	 *            postfix value of rowkey
	 * @param rowPrefix
	 *            prefix value of rowKey
	 * @return prefix and postfix value seperated with delimeter
	 */
	public static String getrowKeyForData(Long key, String rowPrefix) {
		if (key != null) {
			return rowPrefix + key;
		}
		return null;
	}

	public static String getRowKeyForLayer3(Integer workorderId, String operatorName, String receipeId) {
		return NVLayer3Utils.getRowkeyFromWorkOrderId(workorderId) + operatorName
				+ NVLayer3Utils.getRowkeyFromWorkOrderId(receipeId);
	}
	
	

	public static String getRowKeyForLayer3PPEForRecipeAndFile(Integer workorderId,String receipeId,String fileId) {
		return NVLayer3Utils.getRowkeyFromWorkOrderId(workorderId) 
				+ NVLayer3Utils.getRowkeyFromWorkOrderId(receipeId)
				+NVLayer3Utils.getRowkeyFromWorkOrderId(fileId) ;
	}
	
	
	
	
	public static String getRowKeyForLayer3PPE(Integer workorderId,String receipeId) {
		return NVLayer3Utils.getRowkeyFromWorkOrderId(workorderId) 
				+ NVLayer3Utils.getRowkeyFromWorkOrderId(receipeId);
	}
	
	public static void addPostFixOfcsvIntoJson(StringBuilder finalJsonString) {
		if (finalJsonString.length() > QMDLConstant.EMPTY_JSON_LENGTH) {
			finalJsonString.setLength(finalJsonString.length() - QMDLConstant.STRING_EXTRA_LENTH);
			finalJsonString.append(Symbol.BRACKET_CLOSE_STRING).append(QMDLConstant.QUOTES)
					.append(Symbol.BRACE_CLOSE_STRING);
		} else {
			finalJsonString.setLength(QMDLConstant.EMPTY_ARRAY_SIZE);
			finalJsonString.append(QMDLConstant.NO_RESULT_FOUND);
		}
	}

	public  StringBuilder getIntialStringForJson() {
		StringBuilder jsonString = new StringBuilder();
		jsonString.append(QMDLConstant.CSV_START_STRING);
		return jsonString;
	}


	public  String getFinalJson(String stringValue) {
		if (stringValue != null) {
			StringBuilder finalJson = getInitialJson();
			finalJson.append(stringValue);
			addPostFixOfcsvIntoJson(finalJson);
			return finalJson.toString();
		}
		return QMDLConstant.NO_RESULT_FOUND;
	}

	public  StringBuilder getInitialJson() {
		StringBuilder initialString = new StringBuilder();
		initialString.append(QMDLConstant.RESPONSE_START_JSON);
		return initialString;
	}

	public  StringBuilder getInitialJsonForSummary() {
		StringBuilder initialString = new StringBuilder();
		initialString.append(QMDLConstant.SUMMARY_RESPONSE_START_JSON);
		return initialString;
	}	
	
	
	
	
	
	
	/**
	 * Gets the dropwizard url.
	 *
	 * @param request
	 *            the request
	 * @return the dropwizard url
	 */
	public String getDropwizardUrlForLayer3PPE(HttpServletRequest request) {
		String url = ConfigUtils.getString(ConfigEnum.MICRO_SERVICE_BASE_URL, Boolean.TRUE)+ConfigUtils.getString(NVConfigUtil.DROPWIZARD_LAYRE3_PPE_DATA_SERVICE_URL, Boolean.TRUE);
		String requestStr = request.getRequestURI();
		String queryString = request.getQueryString();
		logger.info("Getting url {}  query {}  request {}", url, queryString, requestStr);
		requestStr = requestStr.substring(requestStr.lastIndexOf(Symbol.SLASH_FORWARD), requestStr.length());
		url += requestStr;
		if (StringUtils.isNotEmpty(queryString)) {
			url += Symbol.QUESTION_MARK_STRING + queryString;
		}
		logger.info("Getting url {}", url);
		return url;
	}
	
	
	
	
	
	
	public String getPrefixFromRecipeAndOperatorList(Integer workorderId, List<String> recipeId,
			List<String> operatorList) {
		if (recipeId.size() == QMDLConstant.SINGLE_RECORD && operatorList.size() == QMDLConstant.SINGLE_RECORD) {
			return getRowKeyForLayer3(workorderId, operatorList.get(QMDLConstant.FIRST_RECORD_INDEX),
					recipeId.get(QMDLConstant.FIRST_RECORD_INDEX));
		} else {
			return NVLayer3Utils.getRowkeyFromWorkOrderId(workorderId);
		}
	}
	
	
	public String getPrefixFromRecipeAndOperatorList(Integer workorderId, List<String> recipeId) {
		if (recipeId.size() == QMDLConstant.SINGLE_RECORD) {
			return getRowKeyForLayer3PPE(workorderId,recipeId.get(QMDLConstant.FIRST_RECORD_INDEX));
		} else {
			return NVLayer3Utils.getRowkeyFromWorkOrderId(workorderId);
		}
	}
	
	/**
	 * 
	 * Adds the padding and put data into map.
	 *
	 * @param putMapData
	 *            sorted map to check key Exist or not
	 * @param timestamp
	 *            time stamp of record
	 * @param padding
	 *            3 character starting from 000 and max value 999
	 * @param key
	 *            contains value not found in map (for next rowKey)
	 */
	public static void addPaddingAndPutDataIntoMap(SortedMap<String, Put> putMapData, Long timestamp, String padding,
			StringBuilder key) {
		if (!putMapData.containsKey(timestamp + padding)) {
			key.append(timestamp + padding);
		} else {
			Integer value = Integer.parseInt(padding);
			padding = StringUtils.leftPad(String.valueOf(++value), QMDLConstant.PADDING_LENGTH,
					QMDLConstant.PADDING_VAR);
			addPaddingAndPutDataIntoMap(putMapData, timestamp, padding, key);
		}
	}

	

	/**
	 * Calculate sinr from SNR.
	 *
	 * @param snrRx0Data
	 *            the snr rx 0 data
	 * @param snrRx1Data
	 *            the snr rx 1 data
	 * @return the double
	 */
	public static Double calculateSinrFromSNR(Double snrRx0Data, Double snrRx1Data) {
		if (snrRx0Data != null && snrRx1Data != null) {
			return ((snrRx0Data + snrRx1Data) / QMDLConstant.TWO);
		}
		return null;
	}

	/**
	 * Gets the string from csv.
	 *
	 * @param data
	 *            the data
	 * @param index
	 *            the index
	 * @return the string from csv
	 */
	public static String getStringFromCsv(String[] data, Integer index) {
		String value = null;
		if (index != null && data.length > index && Utils.hasValue(data[index])) {
			try {
				value = (data[index].trim());
				if(value.contains(Symbol.BAR_STRING)){
					value=value.replace(Symbol.BAR_STRING, Symbol.UNDERSCORE_STRING);
				}
			} catch (Exception e) {
				logger.warn("getString data {}   Index : {}   Error Msg {} ", data[index], index, e.getMessage());
			}
		}
		return value;
	}

	/**
	 * Gets the double from csv.
	 *
	 * @param data
	 *            the data
	 * @param index
	 *            the index
	 * @return the double from csv
	 */
	public static Double getDoubleFromCsv(String[] data, Integer index) {
		Double value = null;
		if (index != null && data.length > index && Utils.hasValue(data[index])) {
			try {
				value = Double.parseDouble(data[index].trim());
			} catch (Exception e) {
				logger.warn("getDouble data {}   Index : {}   Error Msg {} ", data[index], index, e.getMessage());
			}
		}
		return value;

	}
	public static Double getDoubleFromString(String[] data, Integer index) {
		Double value = null;
		if (index != null && data.length > index && Utils.hasValue(data[index])) {
			try {
				String temp=data[index].replaceAll("[a-zA-Z]","") ;
				if(temp.isEmpty()) {
					temp="0";
				}
				value = Double.parseDouble(temp.trim());
			} catch (Exception e) {
				logger.warn("getDouble data {}   Index : {}   Error Msg {} ", data[index], index, e.getMessage());
			}
		}
		return value;

	}

	/**
	 * Gets the long from csv.
	 *
	 * @param data
	 *            the data
	 * @param index
	 *            the index
	 * @return the long from csv
	 */
	public static Long getLongFromCsv(String[] data, Integer index) {
		Long value = null;
		if (index != null && data.length > index && Utils.hasValue(data[index])) {
			try {
				value = Long.parseLong(data[index].trim());
			} catch (Exception e) {
				logger.warn("getLong data {}   Index : {}   Error Msg {} ", data[index], index, e.getMessage());
			}
		}
		return value;

	}
	/**
	 * Gets the Integer from csv.
	 *
	 * @param data
	 *            the data
	 * @param index
	 *            the index
	 * @return the Integer from csv
	 */
	public static Integer getIntegerFromCsv(String[] data, Integer index) {
		Integer value = null;
		if (index != null && data.length > index && Utils.hasValue(data[index])) {
			try {
				value = Integer.parseInt(data[index].trim());
			} catch (Exception e) {
				logger.warn("getInteger data {}   Index : {}   Error Msg {} ", data[index], index, e.getMessage());
			}
		}
		return value;

	}

	/**
	 * Cast KPI time wise map to list.
	 *
	 * @param kpiMap
	 *            the kpi map
	 * @return the navigable map
	 */
	public static NavigableMap<Long, NavigableMap<Integer, Object>> castKPITimeWiseMapToList(
			NavigableMap<Long, Object> kpiMap) {
		try {
			NavigableMap<Long, NavigableMap<Integer, Object>> map = new TreeMap<>();
			for (Entry<Long, Object> entry : kpiMap.entrySet()) {
				Long key = getStartMilliOfMinute(entry.getKey());
				NavigableMap<Integer, Object> innerMap = null;
				if (map.containsKey(key)) {
					innerMap = map.get(key);
				} else {
					innerMap = new TreeMap<>();
				}
				innerMap.put(((Long) (entry.getKey() - key)).intValue(), entry.getValue());
				map.put(key, innerMap);
			}
			return map;
		} catch (Exception e) {
			logger.error("Exception in castKPITimeWiseMapToList : {}", ExceptionUtils.getStackTrace(e));
			throw e;
		}
	}


	/**
	 * Round off the number according to pattern and returns a double value.
	 *
	 * @param pattern
	 *            the pattern
	 * @param number
	 *            the number
	 * @return the double
	 * @throws ParseException
	 *             the parse exception
	 */
	public static Double roundOffDouble(String pattern, Number number) throws ParseException {
		DecimalFormat format = new DecimalFormat(pattern);
		String value = format.format(number);
		return format.parse(value).doubleValue();
	}

	/**
	 * Return time bean.
	 *
	 * @param timeStamp
	 *            epoch time
	 * @return bean time
	 */
	public static Long getKeyForBean(Long timeStamp) {
		/*Integer beanTime = ConfigUtils.getInteger(ConfigUtil.LAYER3_BEAN_TIME);
		if (timeStamp != null && beanTime != null) {
			return Long.valueOf(StringUtils.rightPad(String.valueOf(timeStamp / beanTime),String.valueOf(timeStamp).length(),"0"));
		}
		logger.warn("Invalid Time timeStamp {}  beanTime {}", timeStamp, beanTime);*/
		return timeStamp;
	}
	
	
	/**
	 * Get latest value using time stamp.
	 *
	 * @param timestamp1
	 *            first record time
	 * @param value1
	 *            first record value
	 * @param value2
	 *            second record value
	 * @param timestamp2
	 *            second record time
	 * @return latest value
	 */
	public static Long getLatestValue(Long timestamp1, Long value1, Long value2, Long timestamp2) {
		if (value1 == null) {
			return value2;
		} else if (value2 == null) {
			return value1;
		} else if (timestamp1 > timestamp2) {
			return value1;
		}
		return value2;
	}

	/**
	 * Get latest value using time stamp.
	 *
	 * @param timestamp1
	 *            first record time
	 * @param value1
	 *            first record value
	 * @param value2
	 *            second record value
	 * @param timestamp2
	 *            second record time
	 * @return latest value
	 */
	public static String getLatestValue(Long timestamp1, String value1, String value2, Long timestamp2) {
		if (value1 == null) {
			return value2;
		} else if (value2 == null) {
			return value1;
		} else if (timestamp1 > timestamp2) {
			return value1;
		}
		return value2;
	}

	/**
	 * Get latest value using time stamp.
	 *
	 * @param timestamp1
	 *            first record time
	 * @param value1
	 *            first record value
	 * @param value2
	 *            second record value
	 * @param timestamp2
	 *            second record time
	 * @return latest value
	 */
	public static Integer getLatestValue(Long timestamp1, Integer value1, Integer value2, Long timestamp2) {
		if (value1 == null||timestamp1==null) {
			return value2;
		} else if (value2 == null||timestamp2==null) {
			return value1;
		} else if (timestamp1 > timestamp2) {
			return value1;
		}
		return value2;
	}

	/**
	 * Get Double Array index1 sum of values & index2 count of values Add if old
	 * value is exist and count increase by 1.
	 *
	 * @param dataArray
	 *            old data array
	 * @param value
	 *            new record value
	 * @return updated Double Array
	 */
	public static Double[] getDoubleArrayValue(Double[] dataArray, Double value) {
		if (value != null) {
			Double[] valueArray = new Double[QMDLConstant.AVERAGE_KPI_ARRAY_SIZE];
			if (dataArray != null) {
				valueArray[QMDLConstant.VALUE_INDEX] = dataArray[QMDLConstant.VALUE_INDEX] + value;
				valueArray[QMDLConstant.COUNT_INDEX] = dataArray[QMDLConstant.COUNT_INDEX] + 1;
			} else {
				valueArray[QMDLConstant.VALUE_INDEX] = value;
				valueArray[QMDLConstant.COUNT_INDEX] = QMDLConstant.INITIAL_VALUE_COUNT;
			}
			return valueArray;
		}
		return dataArray;
	}

	
	/**
	 * Get Double Array index1 sum of values & index2 count of values Add if old
	 * value is exist and count increase by 1.
	 *
	 * @param dataArray
	 *            old data array
	 * @param value
	 *            new record value
	 * @return updated Double Array
	 */
	public static Double[] getDoubleArrayValue(Double value) {
		if (value != null) {
			Double[] valueArray = new Double[QMDLConstant.AVERAGE_KPI_ARRAY_SIZE];
				valueArray[QMDLConstant.VALUE_INDEX] = value;
				valueArray[QMDLConstant.COUNT_INDEX] = QMDLConstant.INITIAL_VALUE_COUNT;
			return valueArray;
		}
		return null;
	}

	
	/**
	 * Get Double Array index1 sum of values & index2 count of values Add if old
	 * value is exist and count increase by 1.
	 *
	 * @param dataArray
	 *            old data array
	 * @param value
	 *            new record value
	 * @return updated Double Array
	 */
	public static Double[] getDoubleArrayValue(Double[] dataArray, Integer value) {
		if (value != null) {
			Double[] valueArray = new Double[QMDLConstant.AVERAGE_KPI_ARRAY_SIZE];
			if (dataArray != null) {
				valueArray[QMDLConstant.VALUE_INDEX] = dataArray[QMDLConstant.VALUE_INDEX] + value;
				valueArray[QMDLConstant.COUNT_INDEX] = dataArray[QMDLConstant.COUNT_INDEX] + 1;
			} else {
				valueArray[QMDLConstant.VALUE_INDEX] = Double.valueOf(value);
				valueArray[QMDLConstant.COUNT_INDEX] = QMDLConstant.INITIAL_VALUE_COUNT;
			}
			return valueArray;
		}
		return dataArray;
	}

	/**
	 * Get Double Array index1 sum of values & index2 count of values Add if old
	 * value is exist and count increase by 1.
	 *
	 * @param dataArray
	 *            old data array
	 * @param value
	 *            new record value
	 * @return updated Double Array
	 */
	public static Double[] getDoubleArrayValue(Double[] dataArray, Long value) {
		if (value != null) {
			Double[] valueArray = new Double[QMDLConstant.AVERAGE_KPI_ARRAY_SIZE];
			if (dataArray != null) {
				valueArray[QMDLConstant.VALUE_INDEX] = dataArray[QMDLConstant.VALUE_INDEX] + value;
				valueArray[QMDLConstant.COUNT_INDEX] = dataArray[QMDLConstant.COUNT_INDEX] + 1;
			} else {
				valueArray[QMDLConstant.VALUE_INDEX] = Double.valueOf(value);
				valueArray[QMDLConstant.COUNT_INDEX] = QMDLConstant.INITIAL_VALUE_COUNT;
			}
			return valueArray;
		}
		return dataArray;
	}

	/**
	 * @param valueArray
	 *            containing sum of values and count of values
	 * @return average of values
	 * @throws ParseException
	 */
	public static Double getAvgFromArray(Double[] valueArray) {
		if (valueArray != null && valueArray[QMDLConstant.COUNT_INDEX].equals(QMDLConstant.ZERO_ELEMENT)) {
			try {
				return roundOffDouble(QMDLConstant.DOUBLEVALUE_ROUND,
						valueArray[QMDLConstant.VALUE_INDEX] / valueArray[QMDLConstant.COUNT_INDEX]);
			} catch (ParseException e) {
				logger.error("Error in parsing number {}, {}",
						valueArray[QMDLConstant.VALUE_INDEX] / valueArray[QMDLConstant.COUNT_INDEX],
						Utils.getStackTrace(e));
			}
		}
		return null;
	}
	
	
	public static Double getAvgFromArrayAndReturnIntegerValue(Double[] valueArray) {
		if (valueArray != null && valueArray[QMDLConstant.COUNT_INDEX].equals(QMDLConstant.ZERO_ELEMENT)) {
			try {
				return roundOffDouble(QMDLConstant.INTEGERVALUE_ROUND,
						valueArray[QMDLConstant.VALUE_INDEX] / valueArray[QMDLConstant.COUNT_INDEX]);
			} catch (ParseException e) {
				logger.error("Error in parsin number {} {}  ",
						valueArray[QMDLConstant.VALUE_INDEX] / valueArray[QMDLConstant.COUNT_INDEX],
						Utils.getStackTrace(e));
			}
		}
		return null;
	}
	
	
	
	public static Double getDoubleValueUptoTwoDecimal(Double value) {
		if (value!= null) {
			try {
				return roundOffDouble(QMDLConstant.DOUBLEVALUE_ROUND,value);
			} catch (ParseException e) {
				logger.error("Error in parsin number {}",value);
			}
		}
		return null;
	}
	
	public static Double getAvgFromArrayUptoFourDecimals(Double[] valueArray) {
		if (valueArray != null && valueArray[QMDLConstant.COUNT_INDEX].equals(QMDLConstant.ZERO_ELEMENT)) {
			try {
				return roundOffDouble(QMDLConstant.DOUBLEVALUE_ROUND_TO_FOUR_DECIMAL,
						valueArray[QMDLConstant.VALUE_INDEX] / valueArray[QMDLConstant.COUNT_INDEX]);
			} catch (ParseException e) {
				logger.error("Error in parsin number {} {}  ",
						valueArray[QMDLConstant.VALUE_INDEX] / valueArray[QMDLConstant.COUNT_INDEX],
						Utils.getStackTrace(e));
			}
		}
		return null;
	}
	public static void addValueToStringBuilder(StringBuilder json, Object value) {
		if (value != null) {
			json.append(value);
		}
		json.append(ForesightConstants.COMMA);
	}
	
	public static void addValueToStringBuilderJson(StringBuilder json, Object value) {
		if (value != null) {
			json.append(Symbol.QUOTE);
			json.append(value);
			json.append(Symbol.QUOTE);
		}
		json.append(ForesightConstants.COMMA);
	}

	public static Double[] getDoubleAggrigateArray(Double[] arrayOld, Double[] arrayNew) {
		if (arrayOld == null) {
			return arrayNew;
		} else if (arrayNew == null) {
			return arrayOld;
		} else {
			arrayOld[QMDLConstant.VALUE_INDEX] = arrayOld[QMDLConstant.VALUE_INDEX]
					+ arrayNew[QMDLConstant.VALUE_INDEX];
			arrayOld[QMDLConstant.COUNT_INDEX] = arrayOld[QMDLConstant.COUNT_INDEX]
					+ arrayNew[QMDLConstant.COUNT_INDEX];
		}
		return arrayOld;
	}

	public static Integer addIntegerValue(Integer oldValues, Integer recordCount) {
		if (oldValues == null) {
			return recordCount;
		} else if (recordCount == null) {
			return oldValues;
		} else {
			return oldValues + recordCount;
		}
	}

	public static Double getAvgFromArrayForLatLong(Double[] lat) {
		if (lat != null && lat[QMDLConstant.COUNT_INDEX].equals(QMDLConstant.ZERO_ELEMENT)) {
			return (lat[QMDLConstant.VALUE_INDEX] / lat[QMDLConstant.COUNT_INDEX]);
		}
		return null;
	}

	/*public static Double[] getDoubleArrayValueFromArrays(Double[] oldvalue, Double[] newValue) {
		if (newValue == null) {
			return oldvalue;
		} else if (oldvalue == null) {
			Double[] valueArray = new Double[QMDLConstant.AVERAGE_KPI_ARRAY_SIZE];
			valueArray[QMDLConstant.VALUE_INDEX] = newValue[QMDLConstant.VALUE_INDEX];
			valueArray[QMDLConstant.COUNT_INDEX] = newValue[QMDLConstant.COUNT_INDEX];
			return valueArray;
		} else {
			return NVLayer3Utils.getDoubleArrayValue(oldvalue, newValue[QMDLConstant.VALUE_INDEX]);
		}
	}*/

	public static Integer getInteger(String[] data, Integer index) {
		Integer value = null;
		if (index != null && data.length > index) {
			String strValue = data[index];
			if (strValue != null && !strValue.trim().isEmpty() && !strValue.trim().equalsIgnoreCase("null")) {
				try {
					value = Integer.parseInt(data[index].trim());
				} catch (Exception e) {
					logger.warn("Index : {}  converting data to Integer {} Error {} ", index, data[index],
							e.getMessage());
				}
			}
		}
		return value;
	}

	public static Integer getBand(Integer earfcn) {
		if (earfcn >= 0 && earfcn <= 599) {
			return 1;
		} else if (earfcn >= 600 && earfcn <= 1199) {
			return 2;
		} else if (earfcn >= 1200 && earfcn <= 1949) {
			return 3;
		} else if (earfcn >= 1950 && earfcn <= 2399) {
			return 4;
		} else if (earfcn >= 2400 && earfcn <= 2649) {
			return 5;
		} else if (earfcn >= 2650 && earfcn <= 2749) {
			return 6;
		} else if (earfcn >= 2750 && earfcn <= 3449) {
			return 7;
		} else if (earfcn >= 3450 && earfcn <= 3799) {
			return 8;
		} else if (earfcn >= 3800 && earfcn <= 4149) {
			return 9;
		} else if (earfcn >= 4150 && earfcn <= 4749) {
			return 10;
		} else if (earfcn >= 4750 && earfcn <= 4949) {
			return 11;
		} else if (earfcn >= 5010 && earfcn <= 5179) {
			return 12;
		} else if (earfcn >= 5180 && earfcn <= 5279) {
			return 13;
		} else if (earfcn >= 5280 && earfcn <= 5379) {
			return 14;
		} else if (earfcn >= 5730 && earfcn <= 5849) {
			return 17;
		} else if (earfcn >= 5850 && earfcn <= 5999) {
			return 18;
		} else if (earfcn >= 6000 && earfcn <= 6149) {
			return 19;
		} else if (earfcn >= 6150 && earfcn <= 6449) {
			return 20;
		} else if (earfcn >= 6450 && earfcn <= 6599) {
			return 21;
		} else if (earfcn >= 6600 && earfcn <= 7399) {
			return 22;
		} else if (earfcn >= 7500 && earfcn <= 7699) {
			return 23;
		} else if (earfcn >= 7700 && earfcn <= 8039) {
			return 24;
		} else if (earfcn >= 8040 && earfcn <= 8689) {
			return 25;
		} else if (earfcn >= 8690 && earfcn <= 9039) {
			return 26;
		} else if (earfcn >= 9040 && earfcn <= 9209) {
			return 27;
		} else if (earfcn >= 9210 && earfcn <= 9659) {
			return 28;
		} else if (earfcn >= 9660 && earfcn <= 9769) {
			return 29;
		} else if (earfcn >= 9770 && earfcn <= 9869) {
			return 30;
		} else if (earfcn >= 9870 && earfcn <= 9919) {
			return 31;
		} else if (earfcn >= 9920 && earfcn <= 10359) {
			return 32;
		} else if (earfcn >= 36000 && earfcn <= 36199) {
			return 33;
		} else if (earfcn >= 36200 && earfcn <= 36349) {
			return 34;
		} else if (earfcn >= 36350 && earfcn <= 36949) {
			return 35;
		} else if (earfcn >= 36950 && earfcn <= 37549) {
			return 36;
		} else if (earfcn >= 37550 && earfcn <= 37749) {
			return 37;
		} else if (earfcn >= 37750 && earfcn <= 38249) {
			return 38;
		} else if (earfcn >= 38250 && earfcn <= 38649) {
			return 39;
		} else if (earfcn >= 38650 && earfcn <= 39649) {
			return 40;
		} else if (earfcn >= 39650 && earfcn <= 41589) {
			return 41;
		} else if (earfcn >= 41590 && earfcn <= 43589) {
			return 42;
		} else if (earfcn >= 43590 && earfcn <= 45589) {
			return 43;
		} else if (earfcn >= 45590 && earfcn <= 46589) {
			return 44;
		} else if (earfcn >= 46590 && earfcn <= 46789) {
			return 45;
		} else if (earfcn >= 46790 && earfcn <= 54539) {
			return 46;
		} else if (earfcn >= 65536 && earfcn <= 66435) {
			return 65;
		} else if (earfcn >= 66436 && earfcn <= 67335) {
			return 66;
		} else if (earfcn >= 67336 && earfcn <= 67535) {
			return 67;
		} else if (earfcn >= 67536 && earfcn <= 67835) {
			return 68;
		}
		return null;
	}

	public static String getRowkeyFromWorkOrderId(String receipeId) {
		return StringUtils
				.reverse(StringUtils.leftPad(receipeId, QMDLConstant.ROWKEY_LENGTH, QMDLConstant.PADDING_VAR));
	}
	public static String getRowkeyFromWorkOrderId(String receipeId,String paddingValue) {
		return StringUtils
		.reverse(StringUtils.leftPad(receipeId, QMDLConstant.ROWKEY_LENGTH, paddingValue));
	}

	public static List<String> getKpiListForValueMapping() {
		List<String> kpiList = new ArrayList<>();
		kpiList.add(QMDLConstant.PHYSICAL_CELL_ID);
		kpiList.add(QMDLConstant.UL_EARFCN);
		kpiList.add(QMDLConstant.DL_EARFCN);
		kpiList.add(QMDLConstant.DL_BANDWIDTH);
		kpiList.add(QMDLConstant.UL_BANDWIDTH);
		kpiList.add(QMDLConstant.RANK_INDEX);
		kpiList.add(QMDLConstant.SPATIAL_RANK);
		kpiList.add(QMDLConstant.PMI_INDEX);
		kpiList.add(QMDLConstant.NUM_RBS);
		kpiList.add(QMDLConstant.MCS);
		kpiList.add(QMDLConstant.OUT_OF_SYNC_BLER);
		kpiList.add(QMDLConstant.TRACKING_AREA_CODE);
		kpiList.add(QMDLConstant.TIMING_ADVANCE);
		kpiList.add(QMDLConstant.CARRIER_INDEX);
		kpiList.add(QMDLConstant.CQI);
		kpiList.add(QMDLConstant.CA_TYPE);
		return kpiList;
	}

	/**
	 * Copy File to Local Path.
	 *
	 * @param filePath
	 *            the File path
	 * @param dataStream
	 *            the input stream for the file
	 */
	public static boolean copyFileToLocalPath(String filePath, InputStream dataStream) {
		File file = new File(filePath);
		try (FileOutputStream outputStream = new FileOutputStream(file)) {
			int bufferSize;
			byte[] buffer = new byte[FILE_BUFFER_SIZE];
			while ((bufferSize = dataStream.read(buffer)) > DATA_STREAM_END_VALUE) {
				outputStream.write(buffer, OUTPUT_STREAM_START_OFFSET, bufferSize);
			}
			return true;
		} catch (IOException e) {
			logger.error("Error! Exception while writing file to local: {}", Utils.getStackTrace(e));
			return false;
		}
	}

	public static Long getLongValueFromJson(Object object) {
		if (object != null && Utils.hasValidValue(String.valueOf(object))) {
			return Long.valueOf(String.valueOf(object));
		}
		return null;
	}

	public static Double getDoubleFromJson(Object object) {
		if (object != null && Utils.hasValidValue(String.valueOf(object))) {
			return Double.valueOf(String.valueOf(object));
		}
		return null;
	}

	public static Integer getIntegerFromJson(Object object) {
		if (object != null && Utils.hasValidValue(String.valueOf(object))) {
			return Integer.parseInt((String.valueOf(object)));
		}
		return null;
	}

	public static String getStringFromJson(Object object) {
		if (object != null && Utils.hasValidValue(String.valueOf(object))) {
			return String.valueOf(object);
		}
		return null;
	}

	public static String getStringFromIntegerSetValues(Set<Integer> cellidSet) {
		if(cellidSet==null||cellidSet.isEmpty()){
			return null;
		}
		StringBuilder jsn=new StringBuilder();
		for(Integer cellid:cellidSet){
			if(cellid!=null){
				jsn.append(cellid).append(Symbol.UNDERSCORE);
			}
		}
		jsn.setLength(jsn.length()-QMDLConstant.STRING_EXTRA_LENTH);
		return jsn.toString();
	}

	public static String getStringHashSeperatedFromSetValues(Set<String> setValues) {
			if(setValues==null||setValues.isEmpty()){
				return null;
			}
			StringBuilder jsn=new StringBuilder();
			for(String tech:setValues){
				if(tech!=null){
					jsn.append(tech).append(QMDLConstant.DELIMETER_HASH);
				}
			}
			jsn.setLength(jsn.length()-QMDLConstant.STRING_EXTRA_LENTH);
			return jsn.toString();

	}

	public static String getStringFromSetValues(Set<String> setValues) {
		if(setValues==null||setValues.isEmpty()){
			return null;
		}
		StringBuilder jsn=new StringBuilder();
		for(String tech:setValues){
			if(tech!=null){
				jsn.append(tech).append(Symbol.UNDERSCORE);
			}
		}
		jsn.setLength(jsn.length()-QMDLConstant.STRING_EXTRA_LENTH);
		return jsn.toString();

}
	
	public static String getStringFromSetValues(List<String> setValues) {
		if(setValues==null||setValues.isEmpty()){
			return null;
		}
		StringBuilder jsn=new StringBuilder();
		for(String tech:setValues){
			if(tech!=null){
				jsn.append(tech).append(Symbol.UNDERSCORE);
			}
		}
		jsn.setLength(jsn.length()-QMDLConstant.STRING_EXTRA_LENTH);
		return jsn.toString();

}
	
	public static String getStringFromSetValues(Set<String> setValues,String seperator) {
		if(setValues==null||setValues.isEmpty()){
			return null;
		}
		StringBuilder jsn=new StringBuilder();
		for(String tech:setValues){
			if(tech!=null){
				jsn.append(tech).append(seperator);
			}
		}
		jsn.setLength(jsn.length()-QMDLConstant.STRING_EXTRA_LENTH);
		return jsn.toString();

}
	


	public static Set<String> setValuesIntoSet(Set<String> set, String value) {
		if (set != null && value != null) {
			set.add(value);
			return set;
		} else if (value != null) {
			set = new HashSet<>();
			set.add(value);
			return set;
		}
		return set;
	}
	public static Set<Integer> setIntegerValuesIntoSet(Set<Integer> set, Integer value) {
		if (set != null && value != null) {
			set.add(value);
			return set;
		} else if (value != null) {
			set = new HashSet<>();
			set.add(value);
			return set;
		}
		return set;
	}

	public static Double getMinValue(Double avgFromArray, Double minJitter) {
		if(avgFromArray==null){
		return 	minJitter;
		}else if (minJitter==null){
			return avgFromArray;
		}else if(minJitter>avgFromArray){
			return avgFromArray;
		}
		return minJitter;
	}

	public static Double getMaxValue(Double avgFromArray, Double minJitter) {
		if(avgFromArray==null){
		return 	minJitter;
		}else if (minJitter==null){
			return avgFromArray;
		}else if(minJitter<avgFromArray){
			return avgFromArray;
		}
		return minJitter;
	}

	public static String getRowkeyFromWorkOrderId(Integer woId) {
		return StringUtils.reverse(
				StringUtils.leftPad(String.valueOf(woId), QMDLConstant.ROWKEY_LENGTH, QMDLConstant.PADDING_VAR));

	}

	public static String getTechnologyFromBand(String band) {
		
		if (band != null) {
			if ((Double.parseDouble(band) > QMDLConstant.TDD_TECHNOLOGY_BAND_START && Double.parseDouble(band) <= QMDLConstant.TDD_TECHNOLOGY_BAND_END)) {
				return QMDLConstant.TDD_TECHNOLOGY;
			} else {
				return QMDLConstant.FDD_TECHNOLOGY;
			}
		}
		return null;
	}

	public static  List<String> getColumnListForGeography() {
		 List<String> columnList=new ArrayList<>();
		 columnList.add(QMDLConstant.GEOGRAPHY_COLUMN);
		return columnList;
	}

	public static Set<Integer> setValuesIntoSet(Set<Integer> set, Integer value) {
		if (set != null && value != null) {
			set.add(value);
			return set;
		} else if (value != null) {
			set = new HashSet<>();
			set.add(value);
			return set;
		}
		return set;
	}

	public static Long addLongValue(Long oldValues, Long recordCount) {
		if (oldValues == null) {
			return recordCount;
		} else if (recordCount == null) {
			return oldValues;
		} else {
			return oldValues + recordCount;
		}
	}
	
	public static Double addDoubleValue(Double oldValues, Double recordCount) {
		if (oldValues == null) {
			return recordCount;
		} else if (recordCount == null) {
			return oldValues;
		} else {
			return oldValues + recordCount;
		}
	}
	
	public static Long getLongValueForDriveDetail(String value){
		if(!StringUtils.isBlank(value) ) {
			return Long.parseLong(value);
		}
		return null;
	}
	
	public static Double getDoubleValueForDriveDetail(String value){
		if(!StringUtils.isBlank(value) ) {
			return Double.parseDouble(value);
		}
		return null;
	}

	
	public static LatLng getGridLatLong(Double lat, Double lon,Integer gridSize) {
		Double refLat=ConfigUtils.getDouble(ConfigUtil.REFRENCE_LATITUDE_FOR_ADDRESS);
		Double refLon=ConfigUtils.getDouble(ConfigUtil.REFRENCE_LONGTITUDE_FOR_ADDRESS);
		//Integer gridSize=ConfigUtils.getInteger(ConfigUtil.GRID_SIZE_FOR_ADDRESS);
		return new DegreeGrid(gridSize, new LatLng(refLat, refLon)).getGrid(new LatLng(lat, lon));
	}
	
	/**
	 * Get latest value.
	 *
	 * @param oldValue
	 *            old value
	 * @param newValue
	 *            new Value
	 * @return latest value
	 */
	public static Integer getLatestValue(Integer oldValue, Integer newValue) {
		if (oldValue == null) {
			return newValue;
		} else if (newValue == null) {
			return oldValue;
		} else {
			return newValue;
		}
	}
	
	/**
	 * Get latest value.
	 *
	 * @param oldValue
	 *            old value
	 * @param newValue
	 *            new Value
	 * @return latest value
	 */
	public static String getLatestValue(String oldValue, String newValue) {
		if (oldValue == null) {
			return newValue;
		} else if (newValue == null) {
			return oldValue;
		} else {
			return newValue;
		}
	}
	
	/**
	 * Get latest value.
	 *
	 * @param oldValue
	 *            old value
	 * @param newValue
	 *            new Value
	 * @return latest value
	 */
	public static Long getLatestValue(Long oldValue, Long newValue) {
		if (oldValue == null) {
			return newValue;
		} else if (newValue == null) {
			return oldValue;
		} else {
			return newValue;
		}
	}
	
	/**
	 * Get latest value.
	 *
	 * @param oldValue
	 *            old value
	 * @param newValue
	 *            new Value
	 * @return latest value
	 */
	public static Double getLatestValue(Double oldValue, Double newValue) {
		if (oldValue == null) {
			return newValue;
		} else if (newValue == null) {
			return oldValue;
		} else {
			return newValue;
		}
	}

	public static boolean isInBuidlingRecord(WOFileDetail woFileDetailObj) {
		return woFileDetailObj.getWoRecipeMapping().getGenericWorkorder().getTemplateType().name().equals(TemplateType.NV_INBUILDING.name())
				||woFileDetailObj.getWoRecipeMapping().getGenericWorkorder().getTemplateType().name().equals(TemplateType.NV_ADHOC_IB.name())
				||woFileDetailObj.getWoRecipeMapping().getGenericWorkorder().getTemplateType().name().equals(TemplateType.NV_IB_BENCHMARK.name())
				||woFileDetailObj.getWoRecipeMapping().getGenericWorkorder().getTemplateType().name().equals(TemplateType.INBUILDING_WORKFLOW.name())
				||woFileDetailObj.getWoRecipeMapping().getGenericWorkorder().getTemplateType().name().equals(TemplateType.NV_SSVT_IBC_QUICK.name())
				||woFileDetailObj.getWoRecipeMapping().getGenericWorkorder().getTemplateType().name().equals(TemplateType.NV_SSVT_IBC_FULL.name());
	}
	

	public static boolean isInBuidlingRecord(GenericWorkorder genericWorkorder) {
		return genericWorkorder.getTemplateType().name().equals(TemplateType.NV_INBUILDING.name())
				||genericWorkorder.getTemplateType().name().equals(TemplateType.NV_ADHOC_IB.name())
				||genericWorkorder.getTemplateType().name().equals(TemplateType.NV_IB_BENCHMARK.name())
				||genericWorkorder.getTemplateType().name().equals(TemplateType.INBUILDING_WORKFLOW.name())
				||genericWorkorder.getTemplateType().name().equals(TemplateType.NV_SSVT_IBC_QUICK.name())
				||genericWorkorder.getTemplateType().name().equals(TemplateType.NV_SSVT_IBC_FULL.name());
	}

	public static String getDateFromTimestamp(String timestamp) {
		try {
			if (!StringUtils.isBlank(timestamp)) {
				Date date = new Date();
				date.setTime(Long.parseLong(timestamp));
				SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_FOR_CSV_DUMP);
				return dateFormat.format(date);
			}
		} catch (Exception e) {
			logger.error("Error in method getDateFromTimestamp Timestamp:{}	Error{}", timestamp, e.getMessage());
		}
		return Symbol.EMPTY_STRING;
	}
	
	public static String getDateFromTimestampNew(String timestamp) {
		try {
			if (!StringUtils.isBlank(timestamp)) {
				Date date = new Date();
				date.setTime(Long.parseLong(timestamp));
				SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_FOR_EXCEL_DUMP);
				return dateFormat.format(date);
			}
		} catch (Exception e) {
			logger.error("Error in method getDateFromTimestamp Timestamp:{}	Error{}", timestamp, e.getMessage());
		}
		return Symbol.EMPTY_STRING;
	}
	
	public static String getDateFromTimestamp(Long timestamp) {
		try {
			if (timestamp != null) {
				Date date = new Date();
				date.setTime(timestamp);
				SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_FOR_CSV_DUMP);
				return dateFormat.format(date);
			}
		} catch (Exception e) {
			logger.error("Error in method getDateFromTimestamp Timestamp:{}	Error{}", timestamp, e.getMessage());
		}
		return Symbol.EMPTY_STRING;
	}
	
	public static Scan getScanForSignalMessageForBin(Long startTime, Long endTime, String rowPrefix, String seperator) {
		try {
			logger.error("Preparing Scan Object for StartTime:{}	EndTime:{}	RowPrefix:{}", startTime, endTime, rowPrefix);
			Scan scan = new Scan();
			scan.setRowPrefixFilter(Bytes.toBytes(rowPrefix));			
			//String startRow = rowPrefix + seperator + startTime;
			String stopRow = rowPrefix + seperator + endTime;
			//scan.withStartRow(Bytes.toBytes(startRow));
			scan.withStopRow(Bytes.toBytes(stopRow));
			return scan;
		} catch (Exception e) {
			logger.error("Error in method getScanForSignalMessageForBin StartTime:{}	EndTime:{}	RowPrefix:{}	Error:{}", startTime, endTime, rowPrefix, e.getMessage());
		}
		return null;
	}
	
	public static Scan getScanForSignalMessageForBinByEndRowKeyPrefix(String endRowPrefix, String rowPrefix,Boolean inclusive) {
		try {
			if(inclusive==null) {
				inclusive=true;
			}
			logger.info("Preparing Scan Object for endRowPrefix:{}	RowPrefix:{}", endRowPrefix, rowPrefix);
			Scan scan = new Scan();
			scan.setRowPrefixFilter(Bytes.toBytes(endRowPrefix));
			scan.withStopRow(Bytes.toBytes(rowPrefix),inclusive);
			return scan;
		} catch (Exception e) {
			logger.error(
					"Error in method getScanForSignalMessageForBin 	endRowPrefix:{}	RowPrefix:{}	Error:{}",
					 endRowPrefix, rowPrefix, e.getMessage());
		}
		return null;
	}
	
	
	public static Scan getScanForSignalMessageForBinByStartRowKeyPrefix(String startRowPrefix, String rowPrefix,Boolean inclusive) {
		try {
			if(inclusive==null) {
				inclusive=true;
			}
			logger.info("Preparing Scan Object for startRowPrefix:{}	RowPrefix:{}", startRowPrefix, rowPrefix);
			Scan scan = new Scan();
			scan.setRowPrefixFilter(Bytes.toBytes(startRowPrefix));
			scan.withStartRow(Bytes.toBytes(rowPrefix),inclusive);
			return scan;
		} catch (Exception e) {
			logger.error(
					"Error in method getScanForSignalMessageForBin 	endRowPrefix:{}	RowPrefix:{}	Error:{}",
					 startRowPrefix, rowPrefix, e.getMessage());
		}
		return null;
	}
	
	
	public static Scan getScanForSignalMessageForBinByStartTime(Long startTime, Long endTime, String rowPrefix, String seperator) {
		try {
			logger.error("Preparing Scan Object for StartTime:{}	EndTime:{}	RowPrefix:{}", startTime, endTime, rowPrefix);
			Scan scan = new Scan();
			scan.setRowPrefixFilter(Bytes.toBytes(rowPrefix));			
			String startRow = rowPrefix + seperator + startTime;
//			String stopRow = rowPrefix + seperator + endTime;
			scan.withStartRow(Bytes.toBytes(startRow));
//			scan.withStopRow(Bytes.toBytes(stopRow));
			return scan;
		} catch (Exception e) {
			logger.error("Error in method getScanForSignalMessageForBin StartTime:{}	EndTime:{}	RowPrefix:{}	Error:{}", startTime, endTime, rowPrefix, e.getMessage());
		}
		return null;
	}
	
	public static Scan getScanForSignalMessageForBinByStartAndEndTime(Long startTime, Long endTime, String rowPrefix, String seperator) {
		try {
			logger.error("Preparing Scan Object for StartTime:{}	EndTime:{}	RowPrefix:{}", startTime, endTime, rowPrefix);
			Scan scan = new Scan();
			scan.setRowPrefixFilter(Bytes.toBytes(rowPrefix));			
			String startRow = rowPrefix + seperator + startTime;
			String stopRow = rowPrefix + seperator + endTime;
			scan.withStartRow(Bytes.toBytes(startRow));
			scan.withStopRow(Bytes.toBytes(stopRow));
			return scan;
		} catch (Exception e) {
			logger.error("Error in method getScanForSignalMessageForBin StartTime:{}	EndTime:{}	RowPrefix:{}	Error:{}", startTime, endTime, rowPrefix, e.getMessage());
		}
		return null;
	}
	
	
	public static Scan getScanForSignalMessageForByRowKeyPrefix(String rowPrefix, String seperator) {
		try {
			logger.info("Preparing Scan Object for RowPrefix:{}",  rowPrefix);
			Scan scan = new Scan();
			scan.setRowPrefixFilter(Bytes.toBytes(rowPrefix));
			
			return scan;
		} catch (Exception e) {
			logger.error("Error in method getScanForSignalMessageForBin RowPrefix:{}	Error:{}", rowPrefix, e.getMessage());
		}
		return null;
	}
	
	
	public static Scan getScanForSignalMessageForBinByRowKeyPrefix(Long startTime, Long endTime, String rowPrefix, String seperator) {
		try {
			logger.error("Preparing Scan Object for StartTime:{}	EndTime:{}	RowPrefix:{}", startTime, endTime, rowPrefix);
			Scan scan = new Scan();
			scan.setRowPrefixFilter(Bytes.toBytes(rowPrefix));			
//			String startRow = rowPrefix + seperator + endTime;
//			String stopRow = rowPrefix + seperator + endTime;
//			scan.withStartRow(Bytes.toBytes(startRow));
//			scan.withStopRow(Bytes.toBytes(stopRow));
				
			return scan;
		} catch (Exception e) {
			logger.error("Error in method getScanForSignalMessageForBin StartTime:{}	EndTime:{}	RowPrefix:{}	Error:{}", startTime, endTime, rowPrefix, e.getMessage());
		}
		return null;
	}
	
	public static FilterList addFilterToLayer3SignalMsgForBin(String prefix) {
		FilterList filter = new FilterList(FilterList.Operator.MUST_PASS_ALL);
		filter.addFilter(new PrefixFilter((prefix).getBytes()));
		filter.addFilter(new PageFilter(100L));		
		return filter;
	}
	
	public static Scan getScanForSearchSignalMessage(String prefix) {
		Scan scan = new Scan();
		scan.setFilter(new PrefixFilter((prefix).getBytes()));
		return scan;
	}
	
	public static void addColumnToCsv(StringBuilder csv, String stringValue, String delimeter) {
		if (stringValue != null) {
			if(stringValue.contains(delimeter)){
				stringValue=stringValue.replace(delimeter, Symbol.EMPTY_STRING);
			}
			csv.append(stringValue);
		}
		csv.append(delimeter);
	}

	public static Map<String, Long> getStartEndTimeFromPeriodInfo(List<Integer> quarterList, List<Integer> yearList) {
		Long startTime = null;
		Long endTime = null;
		Map<String, Long> timeStampMap = new HashMap<>();
		if(quarterList!=null && quarterList.size()>QMDLConstant.INTEGER_ZERO){
			for(int index=0;index<quarterList.size();index++){
				Quarter quarter = new Quarter(quarterList.get(index),yearList.get(QMDLConstant.INTEGER_ZERO));
				if(startTime!=null && endTime!=null){
					startTime= startTime>quarter.getFirstMillisecond()?quarter.getFirstMillisecond():startTime;
					endTime= endTime<quarter.getLastMillisecond()?quarter.getLastMillisecond():endTime;
				}else{
					startTime =quarter.getFirstMillisecond();
					endTime = quarter.getLastMillisecond();
				}
			}
			timeStampMap.put(START_TIMESTAMP, startTime);
			timeStampMap.put(END_TIMESTAMP, endTime);
			return timeStampMap;
		}else{
			for(int index=0;index<yearList.size();index++){
				Year year = new Year(yearList.get(index));
				if(startTime!=null && endTime!=null){
					startTime= startTime>year.getFirstMillisecond()?year.getFirstMillisecond():startTime;
					endTime= endTime<year.getLastMillisecond()?year.getLastMillisecond():endTime;
				}else{
					startTime =year.getFirstMillisecond();
					endTime = year.getLastMillisecond();
				}
			}
			timeStampMap.put(START_TIMESTAMP, startTime);
			timeStampMap.put(END_TIMESTAMP, endTime);
			return timeStampMap;
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map getWorkorderIdJSON(GenericWorkorder genericWorkorder) throws JSONException {
		Map map = new HashMap<>();
		map.put(VALUE, genericWorkorder.getId());
		map.put(NAME, genericWorkorder.getWorkorderId());
		return map;
	}

	
	
	
	public static Integer getFrequencyFromBand(Integer band) {
		if(bandFrequencyMap.isEmpty()){
			loadBandFreqencyMap();
		}
		return bandFrequencyMap.get(band);
	}

	private static void loadBandFreqencyMap() {
		logger.info("Going to load Band Map");
		bandFrequencyMap.put(1, 2100);
		bandFrequencyMap.put(2, 1900);
		bandFrequencyMap.put(3, 1800);
		bandFrequencyMap.put(4, 1700);
		bandFrequencyMap.put(5, 850);
		bandFrequencyMap.put(7, 2600);
		bandFrequencyMap.put(8, 900);
		bandFrequencyMap.put(10, 1700);
		bandFrequencyMap.put(11, 1500);
		bandFrequencyMap.put(12, 700);
		bandFrequencyMap.put(13, 700);
		bandFrequencyMap.put(14, 700);
		bandFrequencyMap.put(17, 700);
		bandFrequencyMap.put(18, 850);
		bandFrequencyMap.put(19, 850);
		bandFrequencyMap.put(20, 800);
		bandFrequencyMap.put(21, 1500);
		bandFrequencyMap.put(22, 3500);
		bandFrequencyMap.put(24, 1600);
		bandFrequencyMap.put(25, 1900);
		bandFrequencyMap.put(26, 850);
		bandFrequencyMap.put(27, 800);
		bandFrequencyMap.put(28, 700);
		bandFrequencyMap.put(29, 700);
		bandFrequencyMap.put(30, 2300);
		bandFrequencyMap.put(31, 450);
		bandFrequencyMap.put(32, 1500);
		bandFrequencyMap.put(33, 2100);
		bandFrequencyMap.put(34, 2100);	
		bandFrequencyMap.put(35, 1900);
		bandFrequencyMap.put(36, 1900);
		bandFrequencyMap.put(37, 1900);
		bandFrequencyMap.put(38, 2600);
		bandFrequencyMap.put(39, 1900);
		bandFrequencyMap.put(40, 2300);
		bandFrequencyMap.put(41, 2500);
		bandFrequencyMap.put(42, 3500);
		bandFrequencyMap.put(43, 3700);
		bandFrequencyMap.put(44, 700);
		bandFrequencyMap.put(45, 1500);
		bandFrequencyMap.put(46, 5200);
		bandFrequencyMap.put(47, 5900);
		bandFrequencyMap.put(48, 3600);
		bandFrequencyMap.put(50, 1500);
		bandFrequencyMap.put(51, 1500);
		bandFrequencyMap.put(65,2100);
		bandFrequencyMap.put(66,1700);
		bandFrequencyMap.put(67,700);
		bandFrequencyMap.put(68,700);
		bandFrequencyMap.put(69,2600);
		bandFrequencyMap.put(70,2000);
		bandFrequencyMap.put(71,600);		
		bandFrequencyMap.put(72,450);
		bandFrequencyMap.put(73,450);
		bandFrequencyMap.put(74,1500);
		bandFrequencyMap.put(75,1500);
		bandFrequencyMap.put(76,1500);
	
	}
	
	public static String getAddressByLatLong(Double latitute, Double longitute) throws IOException {
		return latitute + Symbol.UNDERSCORE_STRING + longitute;
	}
	

	public static double getDistanceFromLatLng(double lat1, double lon1, double lat2, double lon2) {
		  double theta = lon1 - lon2;
		  double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		  dist = Math.acos(dist);
		  dist = rad2deg(dist);
		  dist = dist * QMDLConstant.INTEGER_SIXTY * QMDLConstant.DISTANCE_CALCULATION_1;
		  dist = dist * QMDLConstant.DISTANCE_CALCULATION_2 * QMDLConstant.INTEGER_THOUSAND; 
		  return Precision.round(dist,QMDLConstant.INTEGER_THREE);
		}
	
	
	public static double calculateDistanceBetweenPoints(
			  double x1, 
			  double y1, 
			  double x2, 
			  double y2) {       
			    return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
			}
	    private static double deg2rad(double deg) {
		  return (deg * Math.PI / QMDLConstant.RADIAN_DEGREE_CONVERT_MULTIPLIER);
		}
		
		private static double rad2deg(double rad) {
		  return (rad * QMDLConstant.RADIAN_DEGREE_CONVERT_MULTIPLIER / Math.PI);
		}
	
	public static String getFileNameFromFilePath(String filePath) {
		if (filePath !=null&&!StringUtils.isBlank(filePath)) {
			return filePath.substring(filePath.lastIndexOf(Symbol.SLASH_FORWARD_STRING) + QMDLConstant.ONE);
		}
		return null;
	}
	
	public static void addFileToZipOutputStream(byte[] fileData, ZipOutputStream zos, String filePath) throws IOException {

		logger.info("Writing to zip file {}", filePath);

		InputStream is = new ByteArrayInputStream(fileData);
		ZipEntry zipEntry = new ZipEntry(filePath);
		zos.putNextEntry(zipEntry);

		byte[] bytes = new byte[QMDLConstant.ONE_KB_IN_BYTES];
		int length;
		while ((length = is.read(bytes)) >= QMDLConstant.ZERO_INDEX) {
			zos.write(bytes, QMDLConstant.ZERO_INDEX, length);
		}

		zos.closeEntry();
		is.close();
	}
	
	
	public static String getValidValueFromSet(Set<String> values) {
		if (values != null && !values.isEmpty()) {
			return values.iterator().next();
		}
		return null;
	}

	
	public static boolean isStationaryRecord(WOFileDetail woFileDetail) {
		String category=woFileDetail.getWoRecipeMapping().getRecipe().getCategory();
	return category.equalsIgnoreCase(QMDLConstant.STATIONARY)|| category.equalsIgnoreCase(QMDLConstant.STEALTH);
	}
	
		
	public static String searchNearestBandwidthWrapper(Long key, NavigableMap<Long, String> dlBandwidthMapForPRB) {
		String res = null;
		if(dlBandwidthMapForPRB!=null) {
			Map.Entry<Long, String> lowerEntry = dlBandwidthMapForPRB.floorEntry(key);
			Map.Entry<Long, String> higherEntry = dlBandwidthMapForPRB.ceilingEntry(key);
			
			if (lowerEntry != null && higherEntry != null) {
				if(key < higherEntry.getKey()) {
					res = lowerEntry.getValue();
				}
				else {
					res = higherEntry.getValue();
				}
			} else if (lowerEntry != null || higherEntry != null) {
				res = lowerEntry != null ? lowerEntry.getValue() : higherEntry.getValue();
			}
		}
		return res;
	}

	public static void sortHbaseResultByTimestamp(List<HBaseResult> resultList) {

		Collections.sort(resultList, new Comparator<HBaseResult>() {
			@Override
			public int compare(HBaseResult o1, HBaseResult o2) {
				return o1.getStringAsLong(QMDLConstant.TIMESTAMP).compareTo(o2.getStringAsLong(QMDLConstant.TIMESTAMP));

			}
		});

	}
	
	
	protected static final String [] STATISTICS_SHEET_HEADER = {"Mean","Median","Maximum","Minimum",COUNT,"Standard Deviation","Variance"};
	protected static final String SERIES_SHEET_HEADER="S.No._Date and Time_Distance(Meter)_Latitude_Longitude_";
	protected static final String SERIES_SHEET_INBUILDING_HEADER="S.No._Date and Time_";
	protected static final List<Integer> KPI_HAVING_RANGES_INDEX=Arrays.asList(4,5,6,7,21,22,23,39,35,74,75,76,77,78,79,80,81) ;

	protected static final List<Integer> KPI_HAVING_COUNT_INDEX=Arrays.asList(3,4,8,10,11,12) ;

	protected static final List<Integer> NEIGHBOUR_KPI_HAVING_RANGES_INDEX=Arrays.asList(1,2,3);
	protected static final List<Integer> NEIGHBOUR_KPI_HAVING_COUNT_INDEX=Arrays.asList(4);

	
	protected static final List<String> COMMON_HBASE_COLUMNS=Arrays.asList("latitude", "longitude", LAT_LNG_TIMESTAMP);
	protected static final List<String> COMMON_HBASE_HEADER=Arrays.asList("lat","lng","timestamp");

	
	
	
	
	public static void validateFilePath(String filePath) {
		/*try {
			new com.inn.commons.io.SafeFile(filePath);
		} catch (IOException e) {
			logger.error(Utils.getStackTrace(e));
			throw new BusinessException(NVLayer3Constants.EXCEPTION_INVALLID_FILE_PATH_JSON);
		}*/
	}
	
}

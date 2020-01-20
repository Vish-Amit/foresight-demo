package com.inn.foresight.module.nv.report.utils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.hadoop.hbase.HBaseResult;
import com.inn.commons.http.HttpGetRequest;
import com.inn.commons.lang.NumberUtils;
import com.inn.commons.lang.StringUtils;
import com.inn.commons.maps.LatLng;
import com.inn.commons.maps.grid.DegreeGrid;
import com.inn.commons.unit.Duration;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.utils.Utils;
import com.inn.foresight.core.generic.utils.ConfigEnum;
import com.inn.foresight.core.generic.utils.DateUtil;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.module.nv.NVConstant;
import com.inn.foresight.module.nv.report.wrapper.GraphWrapper;
import com.inn.foresight.module.nv.workorder.stealth.constants.StealthConstants;
import com.inn.foresight.module.nv.workorder.stealth.kpi.wrapper.ProbeDetailWrapper;
import com.inn.foresight.module.nv.workorder.stealth.wrapper.StealthDataWrapper;
import com.inn.foresight.module.nv.workorder.stealth.wrapper.StealthWOWrapper;

/**
 * The Class StealthUtils.
 *
 * @author innoeye date - 30-Apr-2018 7:16:46 PM
 */
public class StealthUtils extends StealthConstants {

	private static Logger logger = LogManager.getLogger(StealthUtils.class);

	/**
	 * Gets the filter and value map.
	 *
	 *
	 * @param dataType     the data type
	 * @param kpi          the kpi
	 * @param threshold    the threshold
	 * @param startDate    the start date
	 * @param endDate      the end date
	 * @param isDevicewise the is devicewise
	 * @param resultType   the result type
	 * @return the filter and value map
	 * @throws RestException the rest exception
	 */
	
	
	
	
	@SuppressWarnings("rawtypes")
	public static Map<String, List> getFilterAndValueMap(String dataType, String kpi, Double threshold, Date startDate,
			Date endDate, Boolean isDevicewise, String resultType) {
		Map<String, List> map = new HashMap<>();
		if (resultType != null && resultType.equalsIgnoreCase(ALL))
			return null;
		if (!NumberUtils.isValidNumber(threshold)) {
			throw new RestException("In-Valid threshold for Global Filter");
		}
		String filter = getFilterName(dataType, kpi, isDevicewise, resultType);
		if (filter != null) {
			map.put(FILTER_NAME, Arrays.asList(filter));
			map.put(FILTER_PARAM, Arrays.asList(Arrays.asList(START_DATE, END_DATE, THRESHOLD)));
			map.put(FILTER_VALUE, Arrays.asList(Arrays.asList(getFormater("yyyy-MM-dd").format(startDate),
					getFormater("yyyy-MM-dd").format(endDate), threshold)));
		}
		logger.info(new Gson().toJson(map));
		return map;
	}

	public static SimpleDateFormat getFormater(String formate) {
		return new SimpleDateFormat(formate);

	}

	/**
	 * Gets the filter for day wise.
	 *
	 * @param startDate  the start date
	 * @param endDate    the end date
	 * @param kpi        the kpi
	 * @param threshold  the threshold
	 * @param resultType the result type
	 * @return the filter for day wise
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String, List> getFilterForDayWise(Date startDate, Date endDate, String kpi, Double threshold,
			String resultType) {
		Map<String, List> map = new HashMap<>();
		if (resultType == null || resultType.equalsIgnoreCase(ALL))
			return null;
		String filter = kpi != null ? getFilterDataDaywise(resultType, kpi) : null;
		map.put(FILTER_NAME, Arrays.asList(filter));
		map.put(FILTER_PARAM, Arrays.asList(Arrays.asList(START_DATE, END_DATE, THRESHOLD)));
		map.put(FILTER_VALUE, Arrays.asList(Arrays.asList(getFormater("yyyy-MM-dd").format(startDate),
				getFormater("yyyy-MM-dd").format(endDate), threshold)));
		return map;
	}

	/**
	 * Gets the filter data daywise.
	 *
	 * @param resultType the result type
	 * @param kpi        the kpi
	 * @return the filter data daywise
	 */
	private static String getFilterDataDaywise(String resultType, String kpi) {
		if (resultType.equalsIgnoreCase(BAD)) {
			if (kpi.equalsIgnoreCase(TTFB)) {
				return TTFB_DAY_WISE_BAD_FILTER;
			} else if (kpi.equalsIgnoreCase(TTL)) {
				return TTL_DAY_WISE_BAD_FILTER;
			} else if (kpi.equalsIgnoreCase(TDNS)) {
				return TDNS_DAY_WISE_BAD_FILTER;
			} else if (kpi.equalsIgnoreCase(FDNS)) {
				return FDNS_DAY_WISE_BAD_FILTER;
			}
		} else {
			if (resultType.equalsIgnoreCase(GOOD)) {
				if (kpi.equalsIgnoreCase(TTFB)) {
					return TTFB_DAY_WISE_GOOD_FILTER;
				} else if (kpi.equalsIgnoreCase(TTL)) {
					return TTL_DAY_WISE_GOOD_FILTER;
				} else if (kpi.equalsIgnoreCase(TDNS)) {
					return TDNS_DAY_WISE_GOOD_FILTER;
				} else if (kpi.equalsIgnoreCase(FDNS)) {
					return FDNS_DAY_WISE_GOOD_FILTER;
				}
			}
		}
		return null;

	}
	

	public static Map<String, Object> getStealthResponseToReturn(List<StealthWOWrapper> stealthWOWrapperList) {
		Map<String, Object> woMap = new HashMap<>();
		Map<String, List<StealthWOWrapper>> groupByDate = stealthWOWrapperList.stream().filter(d -> d.getDate() != null)
				.collect(Collectors.groupingBy(StealthWOWrapper::getDate));
		logger.info("groupByDate in getStealthResponseToReturn {}", groupByDate);
		groupByDate.forEach((key, value) -> {
			Map<String, Object> subMap = new HashMap<>();

			Map<String, List<StealthWOWrapper>> groupAck = value.stream()
					.collect(Collectors.groupingBy(StealthWOWrapper::getAcknowledgement));
			logger.info("groupAck in getStealthResponseToReturn {}", groupAck);

			groupAck.forEach((key1, value1) -> {
				Map<String, Long> subMap2 = new HashMap<>();

				value1.stream().forEach(val -> subMap2.put(val.getStatus(), val.getCount()));

				updateDefaultStatusValue(subMap2);

				subMap.put(key1, subMap2);
			});
			logger.info("subMap in getStealthResponseToReturn {}", subMap);

			updateDefaultAcknowledgmentValue(subMap);
			woMap.put(key, subMap);
		});
		logger.info("Going to return woMap from getStealthResponseToReturn {}", woMap);
		return woMap;
	}


	
	public static List<Map<String, Long>> getStealthResponseToReturnForSummary(List<StealthWOWrapper> taskList,
			List<StealthWOWrapper> resultList, String type, StealthWOWrapper deviceCountWrapper) throws ParseException {
		SimpleDateFormat hourFormat = new SimpleDateFormat(DATE_FORMAT_HOUR);
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMATE);
		List<Map<String, Long>> returnList=new ArrayList<>();
		Map<Long, Map<String, Long>> resultMapMap = new LinkedHashMap<>();
		
		Long time = null;
		long accepted = 0;
		long rejected = 0;
		long pending = deviceCountWrapper.getCount();
		Long startDate=null;
		
		for (StealthWOWrapper stealthDetail : taskList) {
			
			String date = null;
			if (NVConstant.HOURLY.equalsIgnoreCase(type)) {
				date = hourFormat.format(new Date(stealthDetail.getTime()));
				time = hourFormat.parse(date).getTime();
				
			} else {
				date = dateFormat.format(new Date(stealthDetail.getTime()));
				time = dateFormat.parse(date).getTime();
			}
			
			if ("ACCEPT".equalsIgnoreCase(stealthDetail.getStatus())) {
				accepted = accepted + stealthDetail.getCount();
				if (!stealthDetail.getPostLogin() && pending >= stealthDetail.getCount()) {
					pending = pending - stealthDetail.getCount();
				}
			}

			if ("REJECT".equalsIgnoreCase(stealthDetail.getStatus())) {
				rejected = rejected + stealthDetail.getCount();
				if (!stealthDetail.getPostLogin() && pending >= stealthDetail.getCount()) {
					pending = pending - stealthDetail.getCount();
				}
			}
			
			if("PENDING".equalsIgnoreCase(stealthDetail.getStatus()) && stealthDetail.getPostLogin())
			{
				pending = pending + stealthDetail.getCount();
			}

			Map<String, Long> taskDetailMap = new HashMap<>();
			taskDetailMap.put(StealthConstants.ACCEPTED, accepted);
			taskDetailMap.put(StealthConstants.REJECTED, rejected);
			taskDetailMap.put(StealthConstants.PENDING, pending);
			taskDetailMap.put("TIME", stealthDetail.getTime());
			resultMapMap.put(time, taskDetailMap);
		}

		if (resultList == null || resultList.isEmpty() && taskList != null && taskList.size() > 0) {
			logger.info("empty result sending default ");

			Map<String, Long> resultMap = new HashMap<>();
			resultMap.put("INPROGRESS", 0L);
			resultMap.put("SUCCESS", 0L);
			resultMap.put("FAILURE", 0L);
			resultMap.put("NA", 0L);
			resultMap.put("CLOSED", 0L);
			for(Entry<Long, Map<String, Long>> entry:resultMapMap.entrySet()) {
				entry.getValue().putAll(resultMap);		
			}
		}
		else if (resultList != null && !resultList.isEmpty()) {

			logger.info("resultlist not empty {}", resultList);

			for (StealthWOWrapper stealthWOWrapper : resultList) {
				String date = null;
				Long timeValue=null;
				if (NVConstant.HOURLY.equalsIgnoreCase(type)) {
					date = hourFormat.format(new Date(stealthWOWrapper.getTime()));
					timeValue= hourFormat.parse(date).getTime();
				} else {
					date = dateFormat.format(new Date(stealthWOWrapper.getTime()));
					timeValue = dateFormat.parse(date).getTime();				
					 }
				if (!resultMapMap.containsKey(timeValue)) {
					Map<String, Long> resultMap = new HashMap<>();
					Map<String, Long> taskData= resultMapMap.get(timeValue);
					if(taskData!=null) {
					resultMap.putAll(taskData);
					}
					else {
						final Long cloneVar=timeValue;
						OptionalLong max = resultMapMap.entrySet().stream().filter(c->c.getKey()!=null&&c.getKey()<cloneVar).mapToLong(c->c.getKey()).max();
						if(max.isPresent()) {
						resultMap.putAll(resultMapMap.getOrDefault(max.getAsLong() , new HashMap<>()));
						}
					}
					resultMap.put("INPROGRESS", 0L);
					resultMap.put("SUCCESS", 0L);
					resultMap.put("FAILURE", 0L);
					resultMap.put("NA", 0L);
					resultMap.put("CLOSED", 0L);
					resultMap.put("TIME", stealthWOWrapper.getTime());
					resultMapMap.put(timeValue, resultMap);
				}
				Map<String, Long> resultMap = resultMapMap.get(timeValue);
				resultMap.put(stealthWOWrapper.getStatus(), stealthWOWrapper.getCount());
				resultMapMap.put(timeValue, resultMap);
			}
		}
		
		Set<Long> keySet = resultMapMap.keySet();
		
		for(Long key:keySet) {
			Map<String, Long> resultMap = resultMapMap.get(key);
			resultMap.putIfAbsent("INPROGRESS", 0L);
			resultMap.putIfAbsent("SUCCESS", 0L);
			resultMap.putIfAbsent("FAILURE", 0L);
			resultMap.putIfAbsent("NA", 0L);
			resultMap.putIfAbsent("CLOSED", 0L);
		}
		returnList.addAll(resultMapMap.values());
		return returnList;

	}
	
	public static List<Map<String, Long>> getStealthResponseToReturn(List<StealthWOWrapper> taskList,
			List<StealthWOWrapper> resultList, String type) {
		List<Map<String, Long>> resultMapList = new ArrayList<>();
		Map<String, Map<String, Long>> resultMapMap = new LinkedHashMap<>();
		long accepted = 0;
		long rejected = 0;
		long pending = 0;
      String startTime=null;

		for (StealthWOWrapper stealthWOWrapper : taskList) {

			String date = null;
			if (NVConstant.HOURLY.equalsIgnoreCase(type)) {
				date = new SimpleDateFormat(DATE_FORMAT_HOUR).format(new Date(stealthWOWrapper.getTime()));
			} else {
				date = new SimpleDateFormat(DATE_FORMATE).format(new Date(stealthWOWrapper.getTime()));
			}

			if ("ACCEPT".equalsIgnoreCase(stealthWOWrapper.getStatus())) {
				accepted = accepted + stealthWOWrapper.getCount();
			}

			if ("REJECT".equalsIgnoreCase(stealthWOWrapper.getStatus())) {
				rejected = rejected + stealthWOWrapper.getCount();
			}

			if ("PENDING".equalsIgnoreCase(stealthWOWrapper.getStatus())) {
				pending = pending + stealthWOWrapper.getCount();
			}

			Map<String, Long> taskDetailMap = new HashMap<>();
			taskDetailMap.put(StealthConstants.ACCEPTED, accepted);
			taskDetailMap.put(StealthConstants.REJECTED, rejected);
			taskDetailMap.put(StealthConstants.PENDING, pending);
			taskDetailMap.put("TIME", stealthWOWrapper.getTime());
			resultMapMap.put(date, taskDetailMap);
		}
		
		
		
				

		
		System.out.println("TASK MAP {}"+new Gson().toJson(resultMapMap));
		
		if (resultList == null || resultList.isEmpty() && taskList != null && taskList.size() > 0) {
			logger.info("empty result sending default ");

			Map<String, Long> resultMap = new HashMap<>();
			resultMap.put("INPROGRESS", 0L);
			resultMap.put("SUCCESS", 0L);
			resultMap.put("FAILURE", 0L);
			resultMap.put("NA", 0L);
			resultMap.put("CLOSED", 0L);
			for(Entry<String, Map<String, Long>> entry:resultMapMap.entrySet()) {
				entry.getValue().putAll(resultMap);		
			}

		} else if (resultList != null && !resultList.isEmpty()) {

			logger.info("resultlist not empty {}", resultList);

			for (StealthWOWrapper stealthWOWrapper : resultList) {
				String date = null;
				if (NVConstant.HOURLY.equalsIgnoreCase(type)) {
					date = new SimpleDateFormat(DATE_FORMAT_HOUR).format(new Date(stealthWOWrapper.getTime()));
				} else {
					date = new SimpleDateFormat(DATE_FORMATE).format(new Date(stealthWOWrapper.getTime()));
				}
				
				if (!resultMapMap.containsKey(date)) {
					Map<String, Long> resultMap = new HashMap<>();
					
					Map<String, Long> taskData= resultMapMap.get(date);
					if(taskData!=null) {
					resultMap.putAll(taskData);
					}
				
					resultMap.put("INPROGRESS", 0L);
					resultMap.put("SUCCESS", 0L);
					resultMap.put("FAILURE", 0L);
					resultMap.put("NA", 0L);
					resultMap.put("CLOSED", 0L);
					resultMap.put("TIME", stealthWOWrapper.getTime());
					resultMapMap.put(date, resultMap);
				}
				

				Map<String, Long> resultMap = resultMapMap.get(date);
				resultMap.put(stealthWOWrapper.getStatus(), stealthWOWrapper.getCount());
				resultMapMap.put(date, resultMap);
			}

			Set<String> keySet = resultMapMap.keySet();
			
			for(String key:keySet) {
				Map<String, Long> resultMap = resultMapMap.get(key);
				resultMap.putIfAbsent("INPROGRESS", 0L);
				resultMap.putIfAbsent("SUCCESS", 0L);
				resultMap.putIfAbsent("FAILURE", 0L);
				resultMap.putIfAbsent("NA", 0L);
				resultMap.putIfAbsent("CLOSED", 0L);
			}
			resultMapList.addAll(resultMapMap.values());
		}

		logger.info("final List to return  {}", resultMapList);
		return resultMapList;

	}

	public static List<Map<String, Long>> getStealthResponseToReturn(List<StealthWOWrapper> taskList,
			List<StealthWOWrapper> resultList, Long startDate, Long endDate, String type) {
		if (startDate == null || endDate == null) {
			return getStealthResponseToReturn(taskList, resultList, type);
		}
		logger.info("Timestamps are: startDate: {}, endDate: {}", startDate, endDate);

		List<Map<String, Long>> resultMapList = new ArrayList<>();
		Long time = null;
		long accepted = 0;
		long rejected = 0;
		long pending = 0;

		for (StealthWOWrapper stealthWOWrapper : taskList) {
			time = stealthWOWrapper.getTime();

			if ("ACCEPT".equalsIgnoreCase(stealthWOWrapper.getStatus())) {
				accepted = accepted + stealthWOWrapper.getCount();
			}

			if ("REJECT".equalsIgnoreCase(stealthWOWrapper.getStatus())) {
				rejected = rejected + stealthWOWrapper.getCount();
			}

			if ("PENDING".equalsIgnoreCase(stealthWOWrapper.getStatus())) {
				pending = pending + stealthWOWrapper.getCount();
			}
		}

		logger.info("after ack Counts  A {} R {} P {}", accepted, rejected, pending);

		if (resultList == null || resultList.isEmpty() && taskList != null && taskList.size() > 0) {
			logger.info("empty result sendinf default ");

			Map<String, Long> resultMap = new HashMap<>();
			resultMap.put("ACCEPTED", accepted);
			resultMap.put("REJECTED", rejected);
			resultMap.put("PENDING", pending);
			resultMap.put("INPROGRESS", 0L);
			resultMap.put("SUCCESS", 0L);
			resultMap.put("FAILURE", 0L);
			resultMap.put("NA", 0L);
			resultMap.put("CLOSED", 0L);
			resultMap.put("TIME", time);
			resultMapList.add(resultMap);

		} else if (resultList != null && !resultList.isEmpty()) {

			logger.info("resultlist not empty {}", resultList.size());

			Map<String, Map<String, Long>> resultMapMap = new HashMap<>();

			for (StealthWOWrapper stealthWOWrapper : resultList) {

				logger.info("resultlist stealthWOWrapper {}", new Gson().toJson(stealthWOWrapper));

				if (stealthWOWrapper.getTime() != null
						&& ReportUtil.isTimeStampInRange(startDate, endDate, stealthWOWrapper.getTime())) {

					logger.info("resultlist criteria fulfilled {}", stealthWOWrapper.getTaskId());

					String date = null;
					if (NVConstant.HOURLY.equalsIgnoreCase(type)) {
						date = new SimpleDateFormat(DATE_FORMAT_HOUR).format(new Date(stealthWOWrapper.getTime()));
					} else {
						date = new SimpleDateFormat(DATE_FORMATE).format(new Date(stealthWOWrapper.getTime()));
					}

					if (!resultMapMap.containsKey(date)) {
						Map<String, Long> resultMap = new HashMap<>();
						resultMap.put("ACCEPTED", accepted);
						resultMap.put("REJECTED", rejected);
						resultMap.put("PENDING", pending);
						resultMap.put("INPROGRESS", 0L);
						resultMap.put("SUCCESS", 0L);
						resultMap.put("FAILURE", 0L);
						resultMap.put("NA", 0L);
						resultMap.put("CLOSED", 0L);
						resultMap.put("TIME", stealthWOWrapper.getTime());
						resultMapMap.put(date, resultMap);
					}

					Map<String, Long> resultMap = resultMapMap.get(date);
					resultMap.replace(stealthWOWrapper.getStatus(), resultMap.get(stealthWOWrapper.getStatus()) + 1);
					resultMapMap.put(date, resultMap);
				}
			}
			resultMapList.addAll(resultMapMap.values());
		}

		logger.info("final List to return  {}", resultMapList);
		return resultMapList;

	}

	private static void updateDefaultAcknowledgmentValue(Map<String, Object> subMap) {
		Map<String, Integer> map = new HashMap<>();
		map.put(Status.FAIL.name(), 0);
		map.put(Status.INPROGRESS.name(), 0);
		map.put(Status.SUCCESS.name(), 0);

		if (subMap.get(Acknowledgement.ACCEPT.name()) == null) {
			subMap.put(Acknowledgement.ACCEPT.name(), map);
		}

		if (subMap.get(Acknowledgement.REJECT.name()) == null) {
			subMap.put(Acknowledgement.REJECT.name(), map);
		}
		if (subMap.get(Acknowledgement.PENDING.name()) == null) {
			subMap.put(Acknowledgement.PENDING.name(), map);
		}

		subMap.remove(null);
	}

	private static void updateDefaultStatusValue(Map<String, Long> subMap2) {

		if (subMap2.get(Status.INPROGRESS.name()) == null) {
			subMap2.put(Status.INPROGRESS.name(), 0L);
		}
		if (subMap2.get(Status.FAIL.name()) == null) {
			subMap2.put(Status.FAIL.name(), 0L);
		}
		if (subMap2.get(Status.SUCCESS.name()) == null) {
			subMap2.put(Status.SUCCESS.name(), 0L);
		}
		subMap2.remove(null);
	}

	public static Scan getScanObj(Integer workorderIdPrefix) {
		logger.info("Going to getScanObj for workorderId prefix: {}", workorderIdPrefix);
		byte[] prefix = Bytes.toBytes(workorderIdPrefix + "");
		Scan scan = new Scan();
		scan.addColumn(Bytes.toBytes(StealthConstants.COLUMN_FAMILY), Bytes.toBytes(StealthConstants.COLUMN_DATE));
		scan.addColumn(Bytes.toBytes(StealthConstants.COLUMN_FAMILY), Bytes.toBytes(StealthConstants.COLUMN_LATITUDE));
		scan.addColumn(Bytes.toBytes(StealthConstants.COLUMN_FAMILY), Bytes.toBytes(StealthConstants.COLUMN_LONGITUDE));
		scan.addColumn(Bytes.toBytes(StealthConstants.COLUMN_FAMILY), Bytes.toBytes(StealthConstants.COLUMN_SCORE));
		scan.addColumn(Bytes.toBytes(StealthConstants.COLUMN_FAMILY), Bytes.toBytes(StealthConstants.COLUMN_DEVICE_ID));
		scan.addColumn(Bytes.toBytes(StealthConstants.COLUMN_FAMILY), Bytes.toBytes(StealthConstants.COLUMN_WORKORDER_ID));
		scan.addColumn(Bytes.toBytes(StealthConstants.COLUMN_FAMILY), Bytes.toBytes(StealthConstants.COLUMN_TASK_ID));
		scan.addColumn(Bytes.toBytes(StealthConstants.COLUMN_FAMILY), Bytes.toBytes(StealthConstants.COLUMN_GL4));
		scan.addColumn(Bytes.toBytes(StealthConstants.COLUMN_FAMILY), Bytes.toBytes(StealthConstants.COLUMN_GL3));
		scan.addColumn(Bytes.toBytes(StealthConstants.COLUMN_FAMILY), Bytes.toBytes(StealthConstants.COLUMN_GL2));
		scan.addColumn(Bytes.toBytes(StealthConstants.COLUMN_FAMILY), Bytes.toBytes(StealthConstants.COLUMN_TIME));
		scan.setRowPrefixFilter(prefix);
		logger.info("getScanObj {}", scan.getFilter());
		return scan;
	}

	/**
	 * Gets the active data from result list.
	 *
	 * @param resultList the result list
	 * @return the active data from result list
	 */
	public static List<StealthWOWrapper> getStealthKPIsFromResult(List<HBaseResult> resultList, LatLng latLng,
			Integer zoomLevel, String type) {
		List<StealthWOWrapper> stealthKpiList = new ArrayList<>();
		if (resultList != null && !resultList.isEmpty()) {
			DegreeGrid degreeGrid = new DegreeGrid(getGridLength(zoomLevel), latLng);
			for (HBaseResult result : resultList) {
				stealthKpiList.add(getStealthWOWrapper(result, degreeGrid, zoomLevel, type));
			}
		}
		stealthKpiList = stealthKpiList.stream().filter(x -> x.getLatitude() != null && x.getLongitude() != null)
				.collect(Collectors.toList());

		logger.info("stealthKpiList is {}", stealthKpiList);
		return stealthKpiList;
	}

	private static double getGridLength(Integer zoomLevel) {
		if (zoomLevel >= 14 && zoomLevel < 16) {
			return StealthConstants.GRID_LENGTH_200;
		} else if (zoomLevel.equals(16)) {
			return StealthConstants.GRID_LENGTH_100;
		} else if (zoomLevel.equals(17)) {
			return StealthConstants.GRID_LENGTH_50;
		} else if (zoomLevel.equals(18)) {
			return StealthConstants.GRID_LENGTH_25;
		}
		return 0;
	}

	public static List<StealthWOWrapper> applyDateFilter(List<StealthWOWrapper> stealthKpiList, Long startTime,
			Long endTime) {
		if (startTime != null && !stealthKpiList.isEmpty()) {
			logger.info("Going to apply date filter for startDate :{} endDate :{}", startTime, endTime);
			// Remove null entry of dateToTimestamp from List
			stealthKpiList = stealthKpiList.stream().filter(s -> s.getDateToTimestamp() != null)
					.collect(Collectors.toList());
			if (!stealthKpiList.isEmpty()) {
				return stealthKpiList.stream()
						.filter(s -> s.getDateToTimestamp() >= startTime && s.getDateToTimestamp() <= endTime)
						.collect(Collectors.toList());
			} else {
				logger.debug("List is Empty after removing  null entry of dateToTimestamp from List");
				return Collections.emptyList();
			}
		} else {
			return stealthKpiList;
		}
	}

	private static StealthWOWrapper getStealthWOWrapper(HBaseResult result, DegreeGrid degreeGrid, Integer zoomLevel,
			String type) {
		StealthWOWrapper stealthWOWrapper = new StealthWOWrapper();
		stealthWOWrapper.setLatitude(result.getStringAsDouble((StealthConstants.COLUMN_LATITUDE)));
		stealthWOWrapper.setLongitude(result.getStringAsDouble((StealthConstants.COLUMN_LONGITUDE)));
		stealthWOWrapper.setDeviceId(result.getString(StealthConstants.COLUMN_DEVICE_ID));
		stealthWOWrapper.setCescore(result.getString(StealthConstants.COLUMN_SCORE));
		stealthWOWrapper.setDate(result.getString(StealthConstants.COLUMN_DATE));
		stealthWOWrapper.setDateToInt(result.getStringAsInteger(StealthConstants.COLUMN_DATE));
		stealthWOWrapper.setWorkorderId(result.getStringAsInteger(StealthConstants.COLUMN_WORKORDER_ID));
		stealthWOWrapper.setTaskId(result.getStringAsInteger(StealthConstants.COLUMN_TASK_ID));
		stealthWOWrapper.setGeographyL4Name(result.getString(StealthConstants.COLUMN_GL4));
		stealthWOWrapper.setGeographyL3Name(result.getString(StealthConstants.COLUMN_GL3));
		stealthWOWrapper.setGeographyL2Name(result.getString(StealthConstants.COLUMN_GL2));
		stealthWOWrapper.setTime(result.getStringAsLong(StealthConstants.COLUMN_TIME));

		if (NVConstant.HOURLY.equalsIgnoreCase(type)) {
			// For hourly changes not tested yet
			stealthWOWrapper.setDateToTimestamp(result.getStringAsLong(StealthConstants.COLUMN_TIME));
			stealthWOWrapper
					.setUniqueId(stealthWOWrapper.getTaskId().toString() + stealthWOWrapper.getDateToTimestamp());

		} else {
			stealthWOWrapper
					.setDateToTimestamp(getTimestampFromDate(stealthWOWrapper.getDate(), DATE_FORMATE_TIME_STAMP));
			stealthWOWrapper.setUniqueId(stealthWOWrapper.getTaskId().toString() + stealthWOWrapper.getDate());

		}

		if (zoomLevel > 13 && zoomLevel <= 18) {
			if (stealthWOWrapper.getLatitude() != null && stealthWOWrapper.getLongitude() != null) {
				LatLng referenceLocation = new LatLng(stealthWOWrapper.getLatitude(), stealthWOWrapper.getLongitude());
				referenceLocation = degreeGrid.getGrid(referenceLocation);
				stealthWOWrapper.setLatitude(referenceLocation.getLatitude());
				stealthWOWrapper.setLongitude(referenceLocation.getLongitude());
			}
		}

		return stealthWOWrapper;
	}

	public static String getStringValue(String columnName, HBaseResult result) {
		return result.getString(columnName.getBytes());
	}

	public static Double getDoubleValue(String columnName, HBaseResult result) {
		return result.getStringAsDouble(columnName.getBytes());
	}

	public static List<StealthWOWrapper> getStealthKPIsResponse(List<HBaseResult> list, Long startTime, LatLng latLng,
			Integer zoomLevel, Long endTime, String type) {
		List<StealthWOWrapper> newkpiList = new ArrayList<>();

		List<StealthWOWrapper> kpiList = getStealthKPIsFromResult(list, latLng, zoomLevel, type);
		logger.info("stealth kpi list size before applying date filter :{}", kpiList.size());
		if (!kpiList.isEmpty()) {
			newkpiList = applyUniqueIdGrouping(kpiList, newkpiList);
			newkpiList = applyDateFilter(newkpiList, startTime, endTime);
			newkpiList = applyGrouping(newkpiList, zoomLevel);
		}
		return newkpiList;
	}

	// apply unique id grouping for partitioned data
	private static List<StealthWOWrapper> applyUniqueIdGrouping(List<StealthWOWrapper> kpiList,
			List<StealthWOWrapper> newkpiList) {
		kpiList = kpiList.stream().filter(obj -> obj.getUniqueId() != null).collect(Collectors.toList());
		Map<String, List<StealthWOWrapper>> map = kpiList.stream()
				.collect(Collectors.groupingBy(StealthWOWrapper::getUniqueId));
		map.forEach((k, v) -> {

			newkpiList.add(v.get(ForesightConstants.INDEX_ZERO));

		});
		return newkpiList;
	}

	private static List<StealthWOWrapper> applyGrouping(List<StealthWOWrapper> stealthWOWrapperList,
			Integer zoomLevel) {
		if (stealthWOWrapperList.isEmpty()) {
			return Collections.emptyList();
		}
		logger.info("Going to Apply grouping for zoomLevel : {} ", zoomLevel);
		List<StealthWOWrapper> finalList = new ArrayList<>();
		if (zoomLevel >= 5 && zoomLevel < 8) {
			applyGeographyL2WiseGrouping(stealthWOWrapperList, finalList);
		} else if (zoomLevel >= 8 && zoomLevel < 11) {
			applyGeographyL3WiseGrouping(stealthWOWrapperList, finalList);
		} else if (zoomLevel >= 11 && zoomLevel < 13) {
			applyGeographyL4WiseGrouping(stealthWOWrapperList, finalList);
		} else if (zoomLevel.equals(13)) {
			applyGeographyL4WiseGroupingWithAveraging(stealthWOWrapperList, finalList);
		} else if (zoomLevel >= 14 && zoomLevel <= 18) {
			applyGridWiseGrouping(stealthWOWrapperList, finalList);
		}
		return finalList;
	}

	private static void applyGeographyL4WiseGroupingWithAveraging(List<StealthWOWrapper> stealthWOWrapperList,
			List<StealthWOWrapper> finalList) {
		logger.info("Going to applyGeographyL4WiseGroupingWithAveraging");
		stealthWOWrapperList = stealthWOWrapperList.stream().filter(s -> s.getGeographyL4Name() != null)
				.collect(Collectors.toList());
		Map<String, List<StealthWOWrapper>> list = stealthWOWrapperList.stream()
				.collect(Collectors.groupingBy(StealthWOWrapper::getGeographyL4Name));
		list.forEach((gl4Name, stealthWrapperList) -> {
			finalList.add(new StealthWOWrapper(Long.valueOf(stealthWrapperList.size()),
					getAvgOfLatitude(stealthWrapperList), getAvgOfLongitude(stealthWrapperList),
					getTaskList(stealthWrapperList), getAvgScore(stealthWrapperList), gl4Name, null, null));
		});
	}

	private static Double getAvgOfLatitude(List<StealthWOWrapper> stealthWrapperList) {
		return stealthWrapperList.stream().mapToDouble(StealthWOWrapper::getLatitude).average().getAsDouble();
	}

	private static Double getAvgOfLongitude(List<StealthWOWrapper> stealthWrapperList) {
		return stealthWrapperList.stream().mapToDouble(StealthWOWrapper::getLongitude).average().getAsDouble();
	}

	private static void applyGridWiseGrouping(List<StealthWOWrapper> stealthWOWrapperList,
			List<StealthWOWrapper> finalList) {
		Map<Double, Map<Double, List<StealthWOWrapper>>> list = stealthWOWrapperList.stream().collect(Collectors
				.groupingBy(StealthWOWrapper::getLatitude, Collectors.groupingBy(StealthWOWrapper::getLongitude)));
		list.forEach((latKey,
				value) -> value.forEach((longKey, stealthWrapperList) -> finalList
						.add(new StealthWOWrapper(Long.valueOf(stealthWrapperList.size()), latKey, longKey,
								getAvgScore(stealthWrapperList), null, null, null, stealthWrapperList))));
	}

	private static void applyGeographyL4WiseGrouping(List<StealthWOWrapper> stealthWOWrapperList,
			List<StealthWOWrapper> finalList) {
		stealthWOWrapperList = stealthWOWrapperList.stream().filter(s -> s.getGeographyL4Name() != null)
				.collect(Collectors.toList());
		Map<String, List<StealthWOWrapper>> list = stealthWOWrapperList.stream()
				.collect(Collectors.groupingBy(StealthWOWrapper::getGeographyL4Name));
		list.forEach((gl4Name, stealthWrapperList) -> {
			finalList.add(new StealthWOWrapper(Long.valueOf(stealthWrapperList.size()), null, null,
					getTaskList(stealthWrapperList), getAvgScore(stealthWrapperList), gl4Name, null, null));
		});
	}

	private static void applyGeographyL3WiseGrouping(List<StealthWOWrapper> stealthWOWrapperList,
			List<StealthWOWrapper> finalList) {
		stealthWOWrapperList = stealthWOWrapperList.stream().filter(s -> s.getGeographyL3Name() != null)
				.collect(Collectors.toList());
		Map<String, List<StealthWOWrapper>> list = stealthWOWrapperList.stream()
				.collect(Collectors.groupingBy(StealthWOWrapper::getGeographyL3Name));
		list.forEach((gl3Name, stealthWrapperList) -> {
			finalList.add(new StealthWOWrapper(Long.valueOf(stealthWrapperList.size()), null, null,
					getTaskList(stealthWrapperList), getAvgScore(stealthWrapperList), null, gl3Name, null));
		});

	}

	private static void applyGeographyL2WiseGrouping(List<StealthWOWrapper> stealthWOWrapperList,
			List<StealthWOWrapper> finalList) {
		stealthWOWrapperList = stealthWOWrapperList.stream().filter(s -> s.getGeographyL2Name() != null)
				.collect(Collectors.toList());
		Map<String, List<StealthWOWrapper>> list = stealthWOWrapperList.stream()
				.collect(Collectors.groupingBy(StealthWOWrapper::getGeographyL2Name));
		list.forEach((gl2Name, stealthWrapperList) -> {
			finalList.add(new StealthWOWrapper(Long.valueOf(stealthWrapperList.size()), null, null,
					getTaskList(stealthWrapperList), getAvgScore(stealthWrapperList), null, null, gl2Name));
		});
	}

	private static String getAvgScore(List<StealthWOWrapper> stealthWrapperList) {
		stealthWrapperList = stealthWrapperList.stream().filter(s -> s.getCescore() != null)
				.collect(Collectors.toList());
		Long goodCount = stealthWrapperList.stream().filter(s -> CONST_GOOD.equalsIgnoreCase(s.getCescore()))
				.collect(Collectors.counting());
		Long fairCount = stealthWrapperList.stream().filter(s -> CONST_FAIR.equalsIgnoreCase(s.getCescore()))
				.collect(Collectors.counting());
		long totalCount = stealthWrapperList.stream().count();
		if ((goodCount / totalCount) * 100 >= THRESHOLD_VALUE) {
			return CONST_GOOD;
		} else if (((goodCount + fairCount) / totalCount) * 100 >= THRESHOLD_VALUE) {
			return CONST_FAIR;
		} else {
			return CONST_POOR;
		}
	}

	private static List<Integer> getTaskList(List<StealthWOWrapper> stealthWrapperList) {
		return stealthWrapperList.stream().map(StealthWOWrapper::getTaskId).collect(Collectors.toList());
	}

	public static String[] getStartAndEndRow(String prefix) {
		String[] objToReturn = new String[2];
		byte[] startRow = Bytes.toBytes(prefix);
		byte[] endRow = new byte[startRow.length + 1];
		System.arraycopy(startRow, 0, endRow, 0, startRow.length);
		endRow[startRow.length] = (byte) 255;

		String startRowToSend = Bytes.toString(startRow);
		String endRowToSend = Bytes.toString(endRow);
		objToReturn[0] = startRowToSend;
		objToReturn[1] = endRowToSend;
		return objToReturn;
	}

	public static String getPrefixValue(Integer id, Long timeStamp) {
		return ReportUtil.getFormattedDate(new Date(timeStamp), DATE_FORMATE) + String.valueOf(id);
	}

	public static String getPrefixValueForSite(String neName, Long timeStamp) {
		return ReportUtil.getFormattedDate(new Date(timeStamp), DATE_FORMATE) + neName;
	}

	public static String getGeographyL4Prefix(Integer geoL4Id, Long timeStamp) {
		return ReportUtil.getFormattedDate(new Date(timeStamp), DATE_FORMATE)
				+ StringUtils.reverse(StringUtils.leftPad(String.valueOf(geoL4Id), 7, "0"));
	}

	public static List<String> getColumnListForENBSummary() {
		List<String> columnList = new ArrayList<>();
		columnList.add("r:ltJson");
		columnList.add("r:pktlJson");
		columnList.add("r:rsrpJson");
		columnList.add("r:rsrqJson");
		columnList.add("r:sinrJson");
		columnList.add("r:ulJson");
		columnList.add("r:dlJson");
		columnList.add("r:poorCount");
		columnList.add("r:goodCount");
		columnList.add("r:fairCount");
		columnList.add("r:ttl");
		columnList.add("r:dns");
		columnList.add("r:ytDl");
		columnList.add("r:pktlJson");
		columnList.add("r:bfring");
		return columnList;
	}

	public static List<String> getColumnListForCellSummary() {
		List<String> columnList = new ArrayList<>();
		columnList.add("r:avgRsrp");
		columnList.add("r:avgRsrq");
		columnList.add("r:avgDl");
		columnList.add("r:avgUl");
		columnList.add("r:avgSinr");
		columnList.add("r:cgi");
		columnList.add("r:score");
		return columnList;
	}

	public static Map<String, Object> getKPIDistributionAndCountResponse(List<HBaseResult> list) {
		logger.info("enodeBSummeryList size from hbase {}: ", list.size());
		JSONParser parser = new JSONParser();
		Map<String, Object> maptoReturn = new HashMap<>();
		Map<String, Object> distMap = new HashMap<>();
		Map<String, Object> kpiCountMap = new HashMap<>();
		Map<String, Object> enbCountMap = new HashMap<>();
		if (!list.isEmpty()) {
			list.stream().forEach(result -> {
				setEnodeBCount(enbCountMap, result);

				setKpiDistribution(distMap, result, COLUMN_RSRP_JSON, KEY_RSRP_DIST, parser);
				setKpiDistribution(distMap, result, COLUMN_RSRQ_JSON, KEY_RSRQ_DIST, parser);
				setKpiDistribution(distMap, result, COLUMN_SINR_JSON, KEY_SINR_DIST, parser);

				setKpiDistribution(distMap, result, COLUMN_UL_JSON, KEY_UL, parser);
				setKpiDistribution(distMap, result, COLUMN_DL_JSON, KEY_DL, parser);
				setKpiDistribution(distMap, result, COLUMN_PKT_JSON, KEY_PKT_LOSS, parser);

				setKpiDistribution(distMap, result, COLUMN_TTL_JSON, KEY_TTL, parser);
				setKpiDistribution(distMap, result, COLUMN_DNS_JSON, KEY_DNS, parser);
				setKpiDistribution(distMap, result, COLUMN_BUFFERING_JSON, KEY_BUFFER_TIME, parser);
				setKpiDistribution(distMap, result, COLUMN_THROUGHPUT_JSON, KEY_THROUGHPUT, parser);
				setKpiDistribution(distMap, result, COLUMN_LATENCY_JSON, KEY_LATENCY, parser);

				setKpiCount(kpiCountMap, result, COLUMN_RSRP_JSON, KEY_RSRP, parser);
				setKpiCount(kpiCountMap, result, COLUMN_RSRQ_JSON, KEY_RSRQ, parser);
				setKpiCount(kpiCountMap, result, COLUMN_SINR_JSON, KEY_SINR, parser);
				setKpiCount(kpiCountMap, result, COLUMN_UL_JSON, KEY_UL, parser);
				setKpiCount(kpiCountMap, result, COLUMN_DL_JSON, KEY_DL, parser);
				setKpiCount(kpiCountMap, result, COLUMN_PKT_JSON, KEY_PKT_LOSS, parser);

				setKpiCount(kpiCountMap, result, COLUMN_TTL_JSON, KEY_TTL, parser);
				setKpiCount(kpiCountMap, result, COLUMN_DNS_JSON, KEY_DNS, parser);
				setKpiCount(kpiCountMap, result, COLUMN_BUFFERING_JSON, KEY_BUFFER_TIME, parser);
				setKpiCount(kpiCountMap, result, COLUMN_THROUGHPUT_JSON, KEY_THROUGHPUT, parser);
				setKpiCount(kpiCountMap, result, COLUMN_LATENCY_JSON, KEY_LATENCY, parser);

			});
		} else {
			setDefaultMap(enbCountMap, distMap, kpiCountMap);
		}

		maptoReturn.put(KEY_DISTRIBUTION, distMap);
		maptoReturn.put(KEY_KPI_COUNT, kpiCountMap);
		maptoReturn.put(KEY_ENB_COUNT, enbCountMap);
		return maptoReturn;
	}

	private static void setEnodeBCount(Map<String, Object> enbCountMap, HBaseResult result) {
		if (result == null) {
			enbCountMap.put(KEY_GOOD_ENODEB_COUNT, ForesightConstants.ZERO);
			enbCountMap.put(KEY_FAIR_ENODEB_COUNT, ForesightConstants.ZERO);
			enbCountMap.put(KEY_POOR_ENODEB_COUNT, ForesightConstants.ZERO);
		}

		else {
			if (enbCountMap.containsKey(KEY_GOOD_ENODEB_COUNT)) {
				Integer goodCount = result.getStringAsInteger(COLUMN_GOOD_COUNT);
				if (goodCount != null) {
					enbCountMap.put(KEY_GOOD_ENODEB_COUNT,
							(Integer) enbCountMap.get(KEY_GOOD_ENODEB_COUNT) + goodCount);

				}

			} else {
				Integer goodCount = result.getStringAsInteger(COLUMN_GOOD_COUNT);
				if (goodCount != null) {
					enbCountMap.put(KEY_GOOD_ENODEB_COUNT, goodCount);

				} else {
					enbCountMap.put(KEY_GOOD_ENODEB_COUNT, ForesightConstants.ZERO);

				}

			}

			if (enbCountMap.containsKey(KEY_FAIR_ENODEB_COUNT)) {
				Integer fairCount = result.getStringAsInteger(COLUMN_FAIR_COUNT);
				if (fairCount != null) {
					enbCountMap.put(KEY_FAIR_ENODEB_COUNT,
							(Integer) enbCountMap.get(KEY_FAIR_ENODEB_COUNT) + fairCount);

				}

			} else {
				Integer fairCount = result.getStringAsInteger(COLUMN_FAIR_COUNT);
				if (fairCount != null) {
					enbCountMap.put(KEY_FAIR_ENODEB_COUNT, fairCount);
				} else {
					enbCountMap.put(KEY_FAIR_ENODEB_COUNT, ForesightConstants.ZERO);

				}
			}
			if (enbCountMap.containsKey(KEY_POOR_ENODEB_COUNT)) {
				Integer poorCount = result.getStringAsInteger(COLUMN_POOR_COUNT);
				if (poorCount != null) {
					enbCountMap.put(KEY_POOR_ENODEB_COUNT,
							(Integer) enbCountMap.get(KEY_POOR_ENODEB_COUNT) + poorCount);

				}

			} else {
				Integer poorCount = result.getStringAsInteger(COLUMN_POOR_COUNT);
				if (poorCount != null) {
					enbCountMap.put(KEY_POOR_ENODEB_COUNT, poorCount);
				} else {
					enbCountMap.put(KEY_POOR_ENODEB_COUNT, ForesightConstants.ZERO);
				}
			}
		}
	}

	private static void setDefaultMap(Map<String, Object> enbCountMap, Map<String, Object> distMap,
			Map<String, Object> kpiCountMap) {
		setEnodeBCount(enbCountMap, null);

		setKpiDistribution(distMap, null, COLUMN_RSRP_JSON, KEY_RSRP_DIST, null);
		setKpiDistribution(distMap, null, COLUMN_RSRQ_JSON, KEY_RSRQ_DIST, null);
		setKpiDistribution(distMap, null, COLUMN_SINR_JSON, KEY_SINR_DIST, null);

		setKpiCount(kpiCountMap, null, COLUMN_RSRP_JSON, KEY_RSRP, null);
		setKpiCount(kpiCountMap, null, COLUMN_RSRQ_JSON, KEY_RSRQ, null);
		setKpiCount(kpiCountMap, null, COLUMN_SINR_JSON, KEY_SINR, null);
		setKpiCount(kpiCountMap, null, COLUMN_UL_JSON, KEY_UL, null);
		setKpiCount(kpiCountMap, null, COLUMN_DL_JSON, KEY_DL, null);
		setKpiCount(kpiCountMap, null, COLUMN_PKT_JSON, KEY_PKT_LOSS, null);

		setKpiCount(kpiCountMap, null, COLUMN_TTL_JSON, KEY_TTL, null);
		setKpiCount(kpiCountMap, null, COLUMN_DNS_JSON, KEY_DNS, null);
		setKpiCount(kpiCountMap, null, COLUMN_BUFFERING_JSON, KEY_BUFFER_TIME, null);
		setKpiCount(kpiCountMap, null, COLUMN_THROUGHPUT_JSON, KEY_THROUGHPUT, null);
		setKpiCount(kpiCountMap, null, COLUMN_LATENCY_JSON, KEY_LATENCY, null);
	}

	@SuppressWarnings("unchecked")
	private static void setKpiCount(Map<String, Object> maptoReturn, HBaseResult result, String columnName, String key,
			JSONParser parser) {
		try {
			String kpiJsonString = null;
			if (result != null) {
				kpiJsonString = result.getString(columnName);
			}
			JSONObject json = null;
			if (kpiJsonString != null) {
				json = (JSONObject) parser.parse(kpiJsonString);
			} else {
				json = new JSONObject();
			}
			Map<String, Integer> subMap = (Map<String, Integer>) maptoReturn.get(key);
			if (subMap == null) {
				subMap = new HashMap<>();
			}
			setKpiWiseCount(json, subMap, COLUMN_FAIR_COUNT, KEY_FAIR);
			setKpiWiseCount(json, subMap, COLUMN_GOOD_COUNT, KEY_GOOD);
			setKpiWiseCount(json, subMap, COLUMN_POOR_COUNT, KEY_POOR);
			maptoReturn.put(key, subMap);
		} catch (Exception e) {
			logger.error("Exception in setKpiCount: {}", ExceptionUtils.getStackTrace(e));
		}

	}

	private static void setKpiWiseCount(JSONObject json, Map<String, Integer> subMap, String columnName, String key) {
		Integer count = 0;
		if (subMap.containsKey(key)) {
			count = Integer.parseInt(String.valueOf(subMap.get(key)));
		}
		if (json.containsKey(columnName)) {
			subMap.put(key, count + Integer.parseInt(String.valueOf(json.get(columnName))));
		} else {
			subMap.put(key, 0);
		}
	}

	@SuppressWarnings("unchecked")
	private static void setKpiDistribution(Map<String, Object> maptoReturn, HBaseResult result, String columnName,
			String key, JSONParser parser) {
		try {
			String kpiJsonString = null;
			if (result != null) {
				kpiJsonString = result.getString(columnName);
			}
			logger.info("kpiJsonString{}", kpiJsonString);
			Map<String, Integer> subMap = (Map<String, Integer>) maptoReturn.get(key);
			if (subMap == null) {
				subMap = new HashMap<>();
			}
			JSONObject json = null;
			if (kpiJsonString != null) {
				json = (JSONObject) parser.parse(kpiJsonString);
				maptoReturn.put(key, setRangeWiseDist(key, subMap, json));
			} else {
				maptoReturn.put(key, setRangeWiseDist(key, subMap, new JSONObject()));
			}
		} catch (Exception e) {
			logger.error("Exception in setKpiDistribution {} ", ExceptionUtils.getStackTrace(e));
		}
	}

	private static Map<String, Integer> setRangeWiseDist(String parentKey, final Map<String, Integer> subMap,
			JSONObject json) {
		try {
			if (json.containsKey(KPI_STRING)) {
				String rangesString = json.get(KPI_STRING) != null ? json.get(KPI_STRING).toString() : null;
				ObjectMapper mapper = new ObjectMapper();
				Map<String, Integer> map = mapper.readValue(rangesString, Map.class);

				map.forEach((key, value) -> {
					if (subMap.containsKey(key)) {
						subMap.put(key, subMap.get(key) + value);
					} else {
						subMap.put(key, value);
					}

				});

			}
		} catch (JsonParseException e) {
			logger.error("JsonParseException inside the method setRangeWiseDist {}", Utils.getStackTrace(e));
		} catch (JsonMappingException e) {
			logger.error("JsonMappingException inside the method setRangeWiseDist {}", Utils.getStackTrace(e));

		} catch (IOException e) {
			logger.error("IOException inside the method setRangeWiseDist {}", Utils.getStackTrace(e));

		} catch (Exception e) {
			logger.error("Exception inside the method setRangeWiseDist {}", Utils.getStackTrace(e));

		}
		logger.info("submap to return", subMap);
		return subMap;
	}

	public static Map<String, Object> getTopAndWorstEnodeBResponse(List<HBaseResult> list) {
		Map<String, Object> mapToReturn = new HashMap<>();
		Map<String, Object> mapTopEnb = new HashMap<>();
		Map<String, Object> mapBottomEnb = new HashMap<>();
		JSONParser parser = new JSONParser();

		if (list != null && !list.isEmpty()) {
			list.stream().forEach(result -> {
				setTopBottomSites(mapTopEnb, mapBottomEnb, parser, result);

			});
		}
		mapToReturn.put(KEY_TOP, mapTopEnb);
		mapToReturn.put(KEY_BOTTOM, mapBottomEnb);
		return mapToReturn;
	}

	public static Map<String, Object> getTopAndWorstSiteBResponse(List<HBaseResult> list) {
		Map<String, Object> mapToReturn = new HashMap<>();
		Map<String, Object> mapTopEnb = new HashMap<>();
		Map<String, Object> mapBottomEnb = new HashMap<>();
		if (list != null && !list.isEmpty()) {
			list.stream().forEach(result -> {
				setTopBottomSites(mapTopEnb, mapBottomEnb, result);

			});

		}

		mapToReturn.put(KEY_TOP, mapTopEnb);
		mapToReturn.put(KEY_BOTTOM, mapBottomEnb);
		return mapToReturn;
	}

	private static void setTopBottomSites(Map<String, Object> mapTopEnb, Map<String, Object> mapBottomEnb,
			JSONParser parser, HBaseResult result) {
		setTopWorstKpi(mapTopEnb, result, COLUMN_TOP_RSRP, KEY_RSRP, parser);
		setTopWorstKpi(mapTopEnb, result, COLUMN_TOP_RSRQ, KEY_RSRQ, parser);
		setTopWorstKpi(mapTopEnb, result, COLUMN_TOP_SINR, KEY_SINR, parser);
		setTopWorstKpi(mapTopEnb, result, COLUMN_TOP_UL, KEY_UL, parser);
		setTopWorstKpi(mapTopEnb, result, COLUMN_TOP_DL, KEY_DL, parser);

		setTopWorstKpi(mapBottomEnb, result, COLUMN_BOTTOM_RSRP, KEY_RSRP, parser);
		setTopWorstKpi(mapBottomEnb, result, COLUMN_BOTTOM_RSRQ, KEY_RSRQ, parser);
		setTopWorstKpi(mapBottomEnb, result, COLUMN_BOTTOM_SINR, KEY_SINR, parser);
		setTopWorstKpi(mapBottomEnb, result, COLUMN_BOTTOM_UL, KEY_UL, parser);
		setTopWorstKpi(mapBottomEnb, result, COLUMN_BOTTOM_DL, KEY_DL, parser);
	}

	private static void setTopBottomSites(Map<String, Object> mapTopEnb, Map<String, Object> mapBottomEnb,
			HBaseResult result) {
		setTopWorstKpi(mapTopEnb, result, COLUMN_TOP_RSRP, KEY_RSRP, true);
		setTopWorstKpi(mapTopEnb, result, COLUMN_TOP_RSRQ, KEY_RSRQ, true);
		setTopWorstKpi(mapTopEnb, result, COLUMN_TOP_SINR, KEY_SINR, true);
		setTopWorstKpi(mapTopEnb, result, COLUMN_TOP_UL, KEY_UL, true);
		setTopWorstKpi(mapTopEnb, result, COLUMN_TOP_DL, KEY_DL, true);

		setTopWorstKpi(mapBottomEnb, result, COLUMN_BOTTOM_RSRP, KEY_RSRP, false);
		setTopWorstKpi(mapBottomEnb, result, COLUMN_BOTTOM_RSRQ, KEY_RSRQ, false);
		setTopWorstKpi(mapBottomEnb, result, COLUMN_BOTTOM_SINR, KEY_SINR, false);
		setTopWorstKpi(mapBottomEnb, result, COLUMN_BOTTOM_UL, KEY_UL, false);
		setTopWorstKpi(mapBottomEnb, result, COLUMN_BOTTOM_DL, KEY_DL, false);
	}

	private static void setTopWorstKpi(Map<String, Object> maptoReturn, HBaseResult result, String columnName,
			String key, JSONParser parser) {
		try {
			String kpiJsonString = null;
			if (result != null) {
				kpiJsonString = result.getString(columnName);
			}
			logger.info("kpiJsonString{}", kpiJsonString);
			if (kpiJsonString != null) {
				maptoReturn.put(key, parser.parse(kpiJsonString));
			} else {
				maptoReturn.put(key, Collections.emptyList());
			}
		} catch (Exception e) {
			logger.error("Exception in setTopWorstKpi {} ", ExceptionUtils.getStackTrace(e));
		}
	}

	private static void setTopWorstKpi(Map<String, Object> maptoReturn, HBaseResult result, String columnName,
			String key, Boolean isTop) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();

			String kpiJsonString = null;
			if (result != null) {
				kpiJsonString = result.getString(columnName);
			}
			logger.info("kpiJsonString{}", kpiJsonString);
			if (kpiJsonString != null) {
				List<StealthDataWrapper> list = objectMapper.readValue(kpiJsonString,
						new TypeReference<List<StealthDataWrapper>>() {
						});

				if (maptoReturn.containsKey(key)) {
					List<StealthDataWrapper> newdataList = new ArrayList<>();
					List<StealthDataWrapper> dataList = (List<StealthDataWrapper>) maptoReturn.get(key);
					dataList.addAll(list);
					filterDataByNename(maptoReturn, key, newdataList, dataList, isTop);

				} else {
					maptoReturn.put(key, list);
				}
			} else {
				maptoReturn.put(key, Collections.emptyList());
			}
		} catch (Exception e) {
			logger.error("Exception in setTopWorstKpi {} ", ExceptionUtils.getStackTrace(e));
		}
	}

	private static void filterDataByNename(Map<String, Object> maptoReturn, String key,
			List<StealthDataWrapper> newdataList, List<StealthDataWrapper> dataList, Boolean isTop) {
		Map<String, List<StealthDataWrapper>> map = dataList.stream()
				.collect(Collectors.groupingBy(StealthDataWrapper::getNename));
		map.forEach((keyf, listh) -> {
			if (isTop) {
				Optional<StealthDataWrapper> i = listh.stream()
						.collect(Collectors.maxBy(Comparator.comparing(StealthDataWrapper::getAvg)));
				if (i.isPresent()) {
					StealthDataWrapper wrapper = i.get();
					newdataList.add(wrapper);
				}
			} else {

				Optional<StealthDataWrapper> i = listh.stream()
						.collect(Collectors.minBy(Comparator.comparing(StealthDataWrapper::getAvg)));
				if (i.isPresent()) {
					StealthDataWrapper wrapper = i.get();
					newdataList.add(wrapper);
				}

			}
		});
		if (isTop) {
			newdataList.sort(Comparator.comparing(StealthDataWrapper::getAvg).reversed());
		} else {
			newdataList.sort(Comparator.comparing(StealthDataWrapper::getAvg));

		}
		if (newdataList != null && newdataList.size() > 5) {
			List<StealthDataWrapper> data = newdataList.subList(0, 5);
			maptoReturn.put(key, data);

		} else {
			maptoReturn.put(key, newdataList);

		}
	}

	public static List<String> getTopWorstColumnList() {
		List<String> columnList = new ArrayList<>();
		columnList.add("r:rsrpTop");
		columnList.add("r:rsrqTop");
		columnList.add("r:sinrTop");
		columnList.add("r:ulTop");
		columnList.add("r:dlTop");
		columnList.add("r:rsrpBottom");
		columnList.add("r:rsrqBottom");
		columnList.add("r:sinrBottom");
		columnList.add("r:ulBottom");
		columnList.add("r:dlBottom");
		return columnList;
	}

	public static String getDropwizardUrl(String path) {

		return ConfigUtils.getString(ConfigEnum.MICRO_SERVICE_BASE_URL.getValue())
				+ ConfigUtils.getString(ConfigEnum.DROPWIZARD_URL_FOR_STEALTH_KPI, Boolean.TRUE).concat(path);
	}

	/**
	 * Send http get request.
	 *
	 * @param url               the url
	 * @param isToEnableTimeOut the is to enable time out
	 * @param duration          the duration
	 * @return HttpGetRequest
	 */

	public static HttpGetRequest sendHttpGetRequest(String url, boolean isToEnableTimeOut, Duration duration) {
		HttpGetRequest httpGetRequest = new HttpGetRequest(url);
		if (duration != null) {
			httpGetRequest.setConnectionTimeout(duration);
			httpGetRequest.setEnableTimeout(isToEnableTimeOut);
		}
		return httpGetRequest;
	}

	/**
	 * Gets the filter name.
	 *
	 * @param dataType     the data type
	 * @param kpi          the kpi
	 * @param isDevicewise the is devicewise
	 * @param resultType   the result type
	 * @return the filter name
	 * @throws RestException the rest exception
	 */
	private static String getFilterName(String dataType, String kpi, Boolean isDevicewise, String resultType) {
		// search call filter
		if (!isDevicewise && resultType == null) {
			if (dataType.equalsIgnoreCase(FAILURE)) {
				return FAILURE_FILTER;
			} else {
				if (kpi != null) {
					if (kpi.equalsIgnoreCase(TTFB)) {
						return TTFB_FILTER;
					} else if (kpi.equalsIgnoreCase(TTL)) {
						return TTL_FILTER;
					} else if (kpi.equalsIgnoreCase(TDNS)) {
						return TDNS_FILTER;
					} else if (kpi.equalsIgnoreCase(FDNS)) {
						return FDNS_FILTER;
					}
				}
			}
			// device wise call filter
		} else if (resultType != null) {
			if (resultType.equalsIgnoreCase(BAD)) {
				if (kpi.equalsIgnoreCase(TTFB)) {
					return TTFB_DEVICE_WISE_BAD_FILTER;
				} else if (kpi.equalsIgnoreCase(TTL)) {
					return TTL_DEVICE_WISE_BAD_FILTER;
				} else if (kpi.equalsIgnoreCase(TDNS)) {
					return TDNS_DEVICE_WISE_BAD_FILTER;
				} else if (kpi.equalsIgnoreCase(FDNS)) {
					return FDNS_DEVICE_WISE_BAD_FILTER;
				}
			} else {
				if (resultType.equalsIgnoreCase(GOOD)) {
					if (kpi.equalsIgnoreCase(TTFB)) {
						return TTFB_DEVICE_WISE_GOOD_FILTER;
					} else if (kpi.equalsIgnoreCase(TTL)) {
						return TTL_DEVICE_WISE_GOOD_FILTER;
					} else if (kpi.equalsIgnoreCase(TDNS)) {
						return TDNS_DEVICE_WISE_GOOD_FILTER;
					} else if (kpi.equalsIgnoreCase(FDNS)) {
						return FDNS_DEVICE_WISE_GOOD_FILTER;
					}
				}
			}
		}

		throw new RestException("In-Valid KPI for Global Filter");
	}

	public static Long getTimestampFromDate(String sDate, String formate) {
		try {
			DateFormat formatter;
			formatter = new SimpleDateFormat(formate);
			return formatter.parse(sDate).getTime();
		} catch (Exception e) {
			logger.error("Exception in getTimestampFromDate {} ", ExceptionUtils.getStackTrace(e));
		}
		return 0L;

	}

	public static Integer getHourFromTimestamp(Long timeStamp) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timeStamp);
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	public static List<String> getDateRange(Date startDate, Date endDate, String dateFormat) {
		SimpleDateFormat formate = new SimpleDateFormat(dateFormat);
		List<String> dateList = new ArrayList<>();
		int difference = DateUtil.getDifference(startDate, endDate);
		for (int i = 0; i <= difference; i++) {
			dateList.add(formate.format(DateUtil.add(startDate, i)));
		}
		return dateList;
	}

	public static String getRowkeyForProbe(ProbeDetailWrapper wrapper) {
		SimpleDateFormat sdf=new SimpleDateFormat(ForesightConstants.DDMMYY);
		String rowkey=new StringBuilder(wrapper.getStealthTaskResultId().toString()).reverse().toString();
		if(wrapper.getTimeStamp()!=null) {
			rowkey+=sdf.format(new Date(wrapper.getTimeStamp()));
		}
		return rowkey;
	}
	
	public static String getJsonForProbeColumn(ProbeDetailWrapper wrapper) {
		Gson gson=new Gson();
		HashMap map = gson.fromJson(gson.toJson(wrapper), HashMap.class);
		map.remove("stealthTaskResultId");
		map.remove("timeStamp");
		return gson.toJson(map);
	}
	
	private static String getP1CategoryKpiScore(Double goodSamples, Double fairSamples, Double poorSamples,
			Double totalCount) {
		Double goodPercentage = ReportUtil.getPercentage(goodSamples, totalCount);
		Double fairPercentage = ReportUtil.getPercentage(fairSamples, totalCount);
		Double poorpercentage = ReportUtil.getPercentage(poorSamples, totalCount);
		if (goodPercentage != null && goodPercentage >= ReportConstants.EIGHTY && fairPercentage <= ReportConstants.TWENTY) {
			return ReportConstants.STR_GOOD;
		} else if (fairPercentage != null && goodPercentage >= ReportConstants.EIGHTY && poorpercentage < ReportConstants.TWENTY) {
			return ReportConstants.STR_FAIR;
		} else {
			return ReportConstants.STR_POOR;
		}
	}

	private static String getP2CategoryKpiScore(Double goodSamples, Double fairSamples, Double poorSamples,
			Double totalCount) {
		Double goodPercentage = ReportUtil.getPercentage(goodSamples, totalCount);
		Double fairPercentage = ReportUtil.getPercentage(fairSamples, totalCount);
		Double poorpercentage = ReportUtil.getPercentage(poorSamples, totalCount);
		if (goodPercentage != null && fairPercentage !=null && goodPercentage >= ReportConstants.INDEX_FIFTY && fairPercentage <= ReportConstants.INDEX_FIFTY) {
			return ReportConstants.STR_GOOD;
		} else if (goodPercentage != null && poorpercentage != null && goodPercentage >= ReportConstants.INDEX_FIFTY && poorpercentage < ReportConstants.INDEX_FIFTY) {
			return ReportConstants.STR_FAIR;
		} else {
			return ReportConstants.STR_POOR;
		}
	}

	public static String getUlScore(List<Double> list) {
		if(list!=null && !list.isEmpty()){
		list = list.stream().filter(Objects::nonNull).collect(Collectors.toList());
		Double totalCount = (double) list.size();
		Double goodSamples = (double) list.stream().filter(value -> (value >= ReportConstants.UL_GOOD_VLAUE)).count();
		Double fairSamples = (double) list.stream().filter(value -> (value < ReportConstants.UL_GOOD_VLAUE && value >= ReportConstants.UL_POOR_VLAUE)).count();
		Double poorSamples = (double) list.stream().filter(value -> (value < ReportConstants.UL_POOR_VLAUE)).count();

		return getP1CategoryKpiScore(goodSamples, fairSamples, poorSamples, totalCount);
		}
		return null;
	}
		public static String getYtDlScore(List<Double> list) {
		if(list!=null && !list.isEmpty()){
		list = list.stream().filter(Objects::nonNull).collect(Collectors.toList());
		Double totalCount = (double) list.size();
		Double goodSamples = (double) list.stream().filter(value -> (value >= ReportConstants.YT_DL_GOOD_VLAUE)).count();
		Double fairSamples = (double) list.stream().filter(value -> (value < ReportConstants.YT_DL_GOOD_VLAUE && value >= ReportConstants.YT_DL_POOR_VLAUE)).count();
		Double poorSamples = (double) list.stream().filter(value -> (value < ReportConstants.YT_DL_POOR_VLAUE)).count();

		return getP1CategoryKpiScore(goodSamples, fairSamples, poorSamples, totalCount);
		}
		return null;
	}
		public static String getDlScore(List<Double> list) {
		if(list!=null && !list.isEmpty()){
		list = list.stream().filter(Objects::nonNull).collect(Collectors.toList());
		Double totalCount = (double) list.size();
		Double goodSamples = (double) list.stream().filter(value -> (value >= ReportConstants.DL_GOOD_VLAUE)).count();
		Double fairSamples = (double) list.stream().filter(value -> (value < ReportConstants.DL_GOOD_VLAUE && value >= ReportConstants.DL_POOR_VLAUE)).count();
		Double poorSamples = (double) list.stream().filter(value -> (value < ReportConstants.DL_POOR_VLAUE)).count();

		return getP1CategoryKpiScore(goodSamples, fairSamples, poorSamples, totalCount);
		}
		return null;
	}

	public static String getLatencyScore(List<Double> list) {
		if(list!=null && !list.isEmpty()){
		list = list.stream().filter(Objects::nonNull).collect(Collectors.toList());
		Double totalCount = (double) list.size();
		Double goodSamples = (double) list.stream().filter(value -> (value <= ReportConstants.LATENCY_GOOD_VLAUE)).count();
		Double fairSamples = (double) list.stream().filter(value -> (value > ReportConstants.LATENCY_GOOD_VLAUE && value <= ReportConstants.LATENCY_POOR_VLAUE)).count();
		Double poorSamples = (double) list.stream().filter(value -> (value > ReportConstants.LATENCY_POOR_VLAUE)).count();

		return getP1CategoryKpiScore(goodSamples, fairSamples, poorSamples, totalCount);
		}
		return null;
	}

	public static String getResponseTimeScore(List<Double> list) {
		if(list!=null && !list.isEmpty()){
		list = list.stream().filter(Objects::nonNull).collect(Collectors.toList());
		Double totalCount = (double) list.size();
		Double goodSamples = (double) list.stream().filter(value -> (value <= ReportConstants.RESPONSE_TIME_GOOD_VLAUE)).count();
		Double fairSamples = (double) list.stream().filter(value -> (value > ReportConstants.RESPONSE_TIME_GOOD_VLAUE && value <= ReportConstants.RESPONSE_TIME_POOR_VLAUE)).count();
		Double poorSamples = (double) list.stream().filter(value -> (value > ReportConstants.RESPONSE_TIME_POOR_VLAUE)).count();

		return getP1CategoryKpiScore(goodSamples, fairSamples, poorSamples, totalCount);
		}
		return null;
	}

	public static String getPacketLossScore(List<Double> list) {
		if(list!=null && !list.isEmpty()){
		list = list.stream().filter(Objects::nonNull).collect(Collectors.toList());
		Double totalCount = (double) list.size();
		Double goodSamples = (double) list.stream().filter(value -> (value < ReportConstants.PACKET_LOSS_GOOD_VLAUE)).count();
		Double fairSamples = (double) list.stream().filter(value -> (value >= ReportConstants.PACKET_LOSS_GOOD_VLAUE && value <= ReportConstants.PACKET_LOSS_POOR_VLAUE)).count();
		Double poorSamples = (double) list.stream().filter(value -> (value > ReportConstants.PACKET_LOSS_POOR_VLAUE)).count();

		return getP1CategoryKpiScore(goodSamples, fairSamples, poorSamples, totalCount);
		}
		return null;
	}

	public static String getCoverageKpiScore(List<Double> list, List<Double> kpiPeakValueList) {
		String kpiScore = null;
		  try{
		if(list!=null && !list.isEmpty() && !kpiPeakValueList.isEmpty()){
		list = list.stream().filter(Objects::nonNull).collect(Collectors.toList());
		Double totalCount = (double) list.size();
		Double goodSamples = (double) list.stream().filter(value -> (value > kpiPeakValueList.get(ReportConstants.INDEX_ZER0))).count();
		Double fairSamples = (double) list.stream().filter(value -> (value >= kpiPeakValueList.get(ReportConstants.INDEX_ONE))).count();
		logger.info("kpi good  goodSamples {} ",goodSamples);
		logger.info("kpi fair Samples {} ",fairSamples);
		kpiScore = getCoverageKpiScore(goodSamples, fairSamples, totalCount);
		}
		  }catch (Exception e) {
			logger.error("Exception Inside getCoverageKpiScore {} ",e.getMessage());
		}
		  logger.info("getCoverageKpiScore Data{} ",kpiScore);
		return kpiScore;
	}
		public static String getCoverageKpiScoreForRSRP(List<Double> list, List<Double> kpiPeakValueList) {
		String kpiScore = null;
		  try{
		if(list!=null && !list.isEmpty() && !kpiPeakValueList.isEmpty()){
		list = list.stream().filter(Objects::nonNull).collect(Collectors.toList());
		Double totalCount = (double) list.size();
		Double goodSamples = (double) list.stream().filter(value -> (value > kpiPeakValueList.get(ReportConstants.INDEX_ZER0))).count();
		Double poorSamples = (double) list.stream().filter(value -> (value <= kpiPeakValueList.get(ReportConstants.INDEX_ONE))).count();
		logger.info("kpi good  goodSamples {} ",goodSamples);
		logger.info("kpi fair Samples {} ",poorSamples);
		kpiScore = getCoverageKpiScoreForRSRP(goodSamples, totalCount);
		}
		  }catch (Exception e) {
			logger.error("Exception Inside getCoverageKpiScore {} ",e.getMessage());
		}
		  logger.info("Returning  kpiScore {} ",kpiScore);
		return kpiScore;
	}
		private static String getCoverageKpiScoreForRSRP(Double goodSamples,
			Double totalCount) {
		String coverageScore = null;
		try{
		Double goodPercentage = ReportUtil.getPercentage(goodSamples, totalCount);
		if (goodPercentage != null && goodPercentage >= ReportConstants.INDEX_NINTY_FIVE) {
			coverageScore = ReportConstants.STR_GOOD;
		} else {
			coverageScore = ReportConstants.STR_POOR;
		}
		}
		catch (Exception e) {
			logger.error("Exception in  getCoverageKpiScore {} ",e.getMessage());
		}
		logger.info("Returning  getCoverageKpiScore {} ",coverageScore);
		return coverageScore;
			}

	private static String getCoverageKpiScore(Double goodSamples, Double fairSamples,
			Double totalCount) {
		String coverageScore = null;
		try{
		Double goodPercentage = ReportUtil.getPercentage(goodSamples, totalCount);
		Double fairPercentage = ReportUtil.getPercentage(fairSamples, totalCount);
		if (goodPercentage != null && goodPercentage >= ReportConstants.NINTY) {
			coverageScore = ReportConstants.STR_GOOD;
		} else if (fairPercentage != null && fairPercentage>= ReportConstants.NINTY){
			coverageScore = ReportConstants.STR_FAIR;
		} else {
			coverageScore = ReportConstants.STR_POOR;
		}
		}
		catch (Exception e) {
			logger.error("Exception in  getCoverageKpiScore {} ",e.getMessage());
		}
		logger.info("Returning  getCoverageKpiScore {} ",coverageScore);
		return coverageScore;
			}

	public static String getUlScoreForP2(List<Double> list) {
		logger.info("Inside getUlScoreForP2");
		String p2Sore= null;
		try{
		if(list!=null && !list.isEmpty()){
		list = list.stream().filter(Objects::nonNull).collect(Collectors.toList());
		Double totalCount = (double) list.size();
		Double goodSamples = (double) list.stream().filter(value -> (value >= ReportConstants.UL_GOOD_VLAUE)).count();
		Double fairSamples = (double) list.stream().filter(value -> (value < ReportConstants.UL_GOOD_VLAUE && value >= ReportConstants.UL_POOR_VLAUE)).count();
		Double poorSamples = (double) list.stream().filter(value -> (value < ReportConstants.UL_POOR_VLAUE)).count();
		logger.info("P1 getRsrpScore goodSamples {} ",goodSamples);
		logger.info("P1 getRsrpScore fairSamples {} ",fairSamples);
		logger.info("P1 getRsrpScore poorSamples {} ",poorSamples);
		p2Sore = getP1CategoryKpiScore(goodSamples, fairSamples, poorSamples, totalCount);
		}
		}catch (Exception e) {
			logger.error("Error Inside getUlScoreForP2 {} ",e.getMessage());
		}
		logger.info("Returning getUlScoreForP2 {} ",p2Sore);
		return p2Sore;
	}

	public static String getRsrpScoreForP2(List<Double> list) {
		logger.info("Inside getRsrpScoreForP2");
		String p1Sore= null;
		try{
		if(list!=null && !list.isEmpty()){
		list = list.stream().filter(Objects::nonNull).collect(Collectors.toList());
		Double totalCount = (double) list.size();
		Double goodSamples = (double) list.stream().filter(value -> (value >= ReportConstants.RSRP_GOOD_VLAUE)).count();
		Double fairSamples = (double) list.stream().filter(value -> (value < ReportConstants.RSRP_GOOD_VLAUE && value >= ReportConstants.RSRP_POOR_VLAUE)).count();
		Double poorSamples = (double) list.stream().filter(value -> (value > ReportConstants.RSRP_POOR_VLAUE)).count();
		logger.info("P2 getRsrpScore goodSamples {} ",goodSamples);
		logger.info("P2 getRsrpScore fairSamples {} ",fairSamples);
		logger.info("P2 getRsrpScore poorSamples {} ",poorSamples);
		return getP2CategoryKpiScore(goodSamples, fairSamples, poorSamples, totalCount);
		}
	}catch (Exception e) {
		logger.error("Error Inside getUlScoreForP2 {} ",e.getMessage());
	}
		logger.info("Returning getUlScoreForP2 {} ",p1Sore);
		return p1Sore;
	}

	public static List<Double> getMinMaxvalueListBYkpi(String kpi) {
		try{
		List<Double> kpiPeakValueList = new ArrayList<>();
		if(ReportConstants.RSRP.equals(kpi)){
			kpiPeakValueList = Arrays.asList(ReportConstants.RSRP_GOOD_VLAUE,ReportConstants.RSRP_GOOD_VLAUE,ReportConstants.RSRP_WORST_VALUE);
		}else if(ReportConstants.SINR.equals(kpi)){
			kpiPeakValueList = Arrays.asList(ReportConstants.SINR_GOOD_VLAUE,ReportConstants.SINR_POOR_VLAUE,ReportConstants.SINR_WORST_VALUE);
		}else if(ReportConstants.RSRQ.equals(kpi)){
			kpiPeakValueList = Arrays.asList(ReportConstants.RSRQ_GOOD_VLAUE,ReportConstants.RSRQ_POOR_VLAUE,ReportConstants.RSRQ_WORST_VALUE);
		}else if(ReportConstants.UL_THROUGHPUT.equals(kpi)){
			kpiPeakValueList = Arrays.asList(ReportConstants.UL_GOOD_VLAUE,ReportConstants.UL_POOR_VLAUE,ReportConstants.UL_WORST_VALUE);
		}
		return kpiPeakValueList;
				}catch (Exception e) {
		logger.error("Error Inside getMinMaxvalueListBYkpi {} ",e.getMessage());
		}
		return Collections.emptyList();
		}
		public static String getFinalCoverageScore(List<GraphWrapper> graphWrapperList) {
		logger.info("Inside getFinalCoverageScore");
		String rsrpScore = null;
		String sinrScore = null;
		String ulScore = null;
		String rsrqScore=null;
		String finalScore = null;
		try{
		if(graphWrapperList!=null && !graphWrapperList.isEmpty()){
			for(GraphWrapper wrapper: graphWrapperList){
				if(wrapper.getKpiName().contains(ReportConstants.RSRP)){
					rsrpScore = wrapper.getKpiScore();
				}else if(wrapper.getKpiName().contains(ReportConstants.SINR)){
					sinrScore = wrapper.getKpiScore();
				}else if(wrapper.getKpiName().contains(ReportConstants.UL_THROUGHPUT)){
					ulScore = wrapper.getKpiScore();
				}else if(wrapper.getKpiName().contains(ReportConstants.RSRQ)){
					rsrqScore = wrapper.getKpiScore();
				}
			}
			finalScore = getFinalScore(rsrpScore, sinrScore, ulScore, rsrqScore, finalScore);
		}
					}catch (Exception e) {
			logger.error("Exception Inside getFinalCoverageScore  {} ",e.getMessage());
		}
		logger.info("Finally going to return the kpi Score of Rsrp value {} ",rsrpScore);
		return finalScore;
	}

	private static String getFinalScore(String rsrpScore, String sinrScore, String ulScore, String rsrqScore,
			String finalScore) {
		if(ReportConstants.STR_POOR.equalsIgnoreCase(rsrpScore) || ReportConstants.STR_POOR.equalsIgnoreCase(sinrScore) ||
				ReportConstants.STR_POOR.equalsIgnoreCase(rsrqScore) || ReportConstants.STR_POOR.equalsIgnoreCase(ulScore)){
			finalScore =  ReportConstants.STR_POOR;
		}else if(ReportConstants.STR_FAIR.equalsIgnoreCase(rsrpScore) || ReportConstants.STR_FAIR.equalsIgnoreCase(sinrScore) ||
				ReportConstants.STR_FAIR.equalsIgnoreCase(rsrqScore) || ReportConstants.STR_FAIR.equalsIgnoreCase(ulScore)){
			finalScore =  ReportConstants.STR_FAIR;
		}else if((ReportConstants.STR_GOOD.equalsIgnoreCase(rsrpScore) && sinrScore!=null&&ReportConstants.STR_GOOD.equalsIgnoreCase(sinrScore) &&
				rsrqScore!=null&&ReportConstants.STR_GOOD.equalsIgnoreCase(rsrqScore)) || ReportConstants.STR_GOOD.equalsIgnoreCase(ulScore) ||
				ReportConstants.STR_FAIR.equalsIgnoreCase(rsrqScore) || ReportConstants.STR_FAIR.equalsIgnoreCase(ulScore)){
			finalScore =  ReportConstants.STR_GOOD;
		}
		return finalScore;
	}

	
}

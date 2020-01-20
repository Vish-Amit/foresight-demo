package com.inn.foresight.module.nv.webrtc.service.impl;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.exception.ValueNotFoundException;
import com.inn.commons.hadoop.hbase.HBaseResult;
import com.inn.commons.http.HttpException;
import com.inn.commons.maps.Corner;
import com.inn.commons.maps.LatLng;
import com.inn.commons.maps.grid.DegreeGrid;
import com.inn.commons.maps.tiles.Tile;
import com.inn.commons.maps.tiles.TileBoundaryUtils;
import com.inn.commons.unit.Duration;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.core.generic.utils.ConfigEnum;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.NVConfigUtil;
import com.inn.foresight.module.nv.NVConstant;
import com.inn.foresight.module.nv.app.dao.IDeviceInfoDao;
import com.inn.foresight.module.nv.app.model.DeviceInfo;
import com.inn.foresight.module.nv.dashboard.passive.service.INVPassiveDashboardService;
import com.inn.foresight.module.nv.dashboard.passive.wrapper.NVPassiveDeviceWrapper;
import com.inn.foresight.module.nv.layer3.dao.INVLayer3HDFSDao;
import com.inn.foresight.module.nv.report.utils.StealthUtils;
import com.inn.foresight.module.nv.webrtc.constant.WebRTCConstant;
import com.inn.foresight.module.nv.webrtc.constant.WebRTCUtils;
import com.inn.foresight.module.nv.webrtc.dao.IViberDashboardDao;
import com.inn.foresight.module.nv.webrtc.dao.IViberSubscriberDao;
import com.inn.foresight.module.nv.webrtc.dao.IWebRTCDao;
import com.inn.foresight.module.nv.webrtc.model.IViberLayerDao;
import com.inn.foresight.module.nv.webrtc.model.ViberDashboard;
import com.inn.foresight.module.nv.webrtc.model.ViberLayer;
import com.inn.foresight.module.nv.webrtc.model.ViberSubscriber;
import com.inn.foresight.module.nv.webrtc.service.IWebRTCService;
import com.inn.foresight.module.nv.webrtc.wrapper.CallStatsWrapper;
import com.inn.foresight.module.nv.webrtc.wrapper.WebRTCCallDataWrapper;
import com.inn.foresight.module.nv.webrtc.wrapper.WebRTCCallStatsWrapper;
import com.inn.foresight.module.nv.webrtc.wrapper.WebRTCCallSummaryDataWrapper;
import com.inn.foresight.module.nv.webrtc.wrapper.WebRTCDeviceSummaryWrapper;
import com.inn.foresight.module.nv.webrtc.wrapper.WebRTCViberDashBoardDataWrapper;
import com.inn.foresight.module.nv.webrtc.wrapper.WebRTCViberLayerWrapper;

@Service("WebRTCServiceImpl")
public class WebRTCServiceImpl implements IWebRTCService {
	@Autowired
	private IDeviceInfoDao iDeviceInfoDao;
	@Autowired
	private IWebRTCDao webRtcDao;

	@Autowired
	private INVLayer3HDFSDao nvLayer3hdfsDao;
	@Autowired
	private IViberDashboardDao viberDashboardDao;

	@Autowired
	private IViberSubscriberDao viberSubscriberDao;

	@Autowired
	INVPassiveDashboardService iNVPassiveDashboardService;

	@Autowired
	IViberLayerDao iViberLayer;
	
	@Autowired
	private INVLayer3HDFSDao nvHdfsDao;
	
	private Logger logger = LogManager.getLogger(WebRTCServiceImpl.class);

	@Override
	public WebRTCDeviceSummaryWrapper getDeviceSummaryDataByDeviceId(String deviceId, String startDate,
			String endDate) {
		logger.info("inside the getDeviceSummaryDataByDeviceId  deviceId {},startDate {} endDate {}", deviceId,
				startDate, endDate);

		List<WebRTCCallSummaryDataWrapper> deviceData = null;
		List<WebRTCCallSummaryDataWrapper> deviceDataOfWeek = null;

		Scan scan = null;
		Scan scanForWeek = null;
		try {
			scan = getScanObject(deviceId, startDate, endDate, false);
			scanForWeek = getScanObject(deviceId, startDate, endDate, true);

		} catch (Exception e) {
			logger.info("Exception in create scan object {}", Utils.getStackTrace(e));
		}
		WebRTCDeviceSummaryWrapper data = new WebRTCDeviceSummaryWrapper();

		try {
			deviceData = webRtcDao.setHBaseDataIntoWrapper(scan, ConfigUtils.getString(NVConfigUtil.WEBSRTC_TABLE_NAME),
					Bytes.toBytes(WebRTCConstant.COLUMN_FAMILY));
			deviceDataOfWeek = webRtcDao.setHBaseDataIntoWrapper(scanForWeek,
					ConfigUtils.getString(NVConfigUtil.WEBSRTC_TABLE_NAME),
					Bytes.toBytes(WebRTCConstant.COLUMN_FAMILY));
		} catch (IOException e) {
			logger.info("Exception in setHBaseDataIntoWrapper {}", Utils.getStackTrace(e));
		}

		try {
			List<List<String>> callList = getCallList(deviceData);
			Map<String, String> allCallDetails = getCallCounts(deviceData);
			Map<String, Map<String, Long>> mosAndCallReleaseData = getMOSAndCallReleaseData(deviceData);
			Map<String, Map<String, Long>> codecData = getCodecData(deviceData);
			Map<String, Map<String, String>> callMosDetailsForWeek = getCallMosDetailsForWeek(deviceDataOfWeek);
			Map<String, Map<String, Long>> countOfNetworkType = getCountOfNetworkType(deviceDataOfWeek);

			data.setCallList(callList);
			data.setAllCallDetails(allCallDetails);
			data.setMosAndCallReleaseData(mosAndCallReleaseData);
			data.setCodecData(codecData);
			data.setCallMosDetailsForWeek(callMosDetailsForWeek);
			data.setCountOfNetworkType(countOfNetworkType);
			return data;
		} catch (Exception e) {
			logger.info("Exception in getDeviceSummaryDataByDeviceId for device summary data {}",
					Utils.getStackTrace(e));
		}
		return data;
	}

	private Scan getScanObject(String deviceId, String startDate, String endDate, boolean isWeekDataReq)
			throws IOException {
		long startTime = com.inn.core.generic.utils.Utils.parseStringToDate(startDate, WebRTCConstant.DATE_FORMATE_DDMMYY).getTime() / NVConstant.INT_THOUSAND;
		long endTime = com.inn.core.generic.utils.Utils.parseStringToDate(endDate, WebRTCConstant.DATE_FORMATE_DDMMYY).getTime() / NVConstant.INT_THOUSAND;
		endTime = endTime + (ForesightConstants.TWENTY_FOUR * ForesightConstants.SIXTY * ForesightConstants.SIXTY);

		deviceId = deviceId.replaceAll(WebRTCConstant.DC_STRING, "");
		deviceId = new StringBuilder(deviceId).reverse().toString();

		if (isWeekDataReq) {
			startTime = startTime - (ForesightConstants.SEVEN * ForesightConstants.TWENTY_FOUR
					* ForesightConstants.SIXTY * ForesightConstants.SIXTY);
		}
		logger.info("StartTine and endtime and deviceid {} {} {}", deviceId, startTime, endTime);
		Scan scan = new Scan();
		scan.setRowPrefixFilter(Bytes.toBytes(deviceId));
		// if timestamp value saved in millisecond plz do this startTime *1000 and
		// endTime *1000
		scan.setTimeRange(startTime, endTime);
		return scan;
	}

	@Override
	public byte[] getFileFromHDFS(String callToken, String callType, Boolean isRCS) throws Exception {
		String filePath = null;

		if(isRCS != null && isRCS){
			filePath = ConfigUtils.getString(NVConfigUtil.WEBRTC_RCS_FILEPATH);
		} else {
			filePath = ConfigUtils.getString(NVConfigUtil.WEBRTC_FILEPATH);
		}
		String localTempPath = ConfigUtils.getString(NVConfigUtil.WEBRTC_TEMP_LOCAL_FILEPATH);
		File file = new File(localTempPath);
		if (!file.exists()) {
			file.getAbsoluteFile().mkdirs();
		}
		logger.info("file path for HDFS {} and temp local {}", filePath, localTempPath);
		callToken = callToken.replace(ForesightConstants.DOT, ForesightConstants.UNDERSCORE);
		String fileName = callToken + ForesightConstants.AT_THE_RATE + callType.toUpperCase()
				+ WebRTCConstant.ZIP_EXTENSION;

		logger.info("file name is after replace key {}", fileName);

		byte[] ba = null;
		try {
			String fullLocalPath = nvHdfsDao.copyFileFromHdfsToLocalPath(filePath + fileName, localTempPath, fileName);
			Path localFilePath = Paths.get(fullLocalPath);
			ba = Files.readAllBytes(localFilePath);
			Files.delete(localFilePath);
		} catch (Exception e) {

			logger.error("Exeption in getFileFromHDFS method {}", ExceptionUtils.getStackTrace(e));
		}

		return ba;
	}

	private List<List<String>> getCallList(List<WebRTCCallSummaryDataWrapper> wrapperList) {
		List<List<String>> callListData = new ArrayList<>();
		if (wrapperList != null) {
			for (WebRTCCallSummaryDataWrapper data : wrapperList) {
				List<String> singleCallData = new ArrayList<>();
				addFieldIntoList(singleCallData, data.getLatitude());
				addFieldIntoList(singleCallData, data.getLongitude());
				addFieldIntoList(singleCallData, data.getAvgMos());
				addFieldIntoList(singleCallData, data.getCallType());
				addFieldIntoList(singleCallData, data.getCallReleaseType());
				addFieldIntoList(singleCallData, data.getMediaType());
				addFieldIntoList(singleCallData, data.getMosType());
				addFieldIntoList(singleCallData, data.getNetworkType());
				addFieldIntoList(singleCallData, data.getCallStartTime());
				addFieldIntoList(singleCallData, data.getCallEndTime());
				addFieldIntoList(singleCallData, data.getCallToken());

				callListData.add(singleCallData);
			}
		}
		return callListData;

	}

	private void addFieldIntoList(List<String> singleCallData, Object field) {
		if (field != null) {
			singleCallData.add(field.toString());
		} else {
			singleCallData.add("");
		}

	}

	private Map<String, String> getCallCounts(List<WebRTCCallSummaryDataWrapper> wrapperList) {
		Map<String, String> callDataMap = new HashMap<>();

		Map<String, Long> collect = wrapperList.stream()
				.collect(Collectors.groupingBy(WebRTCCallSummaryDataWrapper::getCallType, Collectors.counting()));

		for (Map.Entry<String, Long> entry : collect.entrySet()) {
			callDataMap.put(entry.getKey(), entry.getValue().toString());
		}
		Double average = null;
		try {
			average = wrapperList.stream().filter(s -> s.getAvgMos() != null && !s.getAvgMos().equals(Double.NaN))
					.mapToDouble(WebRTCCallSummaryDataWrapper::getAvgMos).average().getAsDouble();
		} catch (NoSuchElementException e) {

			logger.info("Exception in getting average of mos for device summary");
		}

		if (average != null) {
			callDataMap.put(WebRTCConstant.MOSKEY, String.valueOf(average));
		} else {
			callDataMap.put(WebRTCConstant.MOSKEY, null);
		}
		return callDataMap;
	}

	private static Map<String, Map<String, String>> getCallMosDetailsForWeek(
			List<WebRTCCallSummaryDataWrapper> wrapperList) {
		Map<String, Map<String, String>> oneWeekDataMap = new HashMap<>();

		Map<String, Double> dateWiseMosMap = wrapperList.stream()
				.filter(f -> f.getDate() != null && f.getAvgMos() != null)
				.collect(Collectors.groupingBy(WebRTCCallSummaryDataWrapper::getDate,
						Collectors.averagingDouble(WebRTCCallSummaryDataWrapper::getAvgMos)));

		Map<String, Long> dateWiseCallMap = wrapperList.stream().filter(f -> f.getDate() != null)
				.collect(Collectors.groupingBy(WebRTCCallSummaryDataWrapper::getDate, Collectors.counting()));

		for (Map.Entry<String, Long> entry : dateWiseCallMap.entrySet()) {
			Map<String, String> mosMap = new HashMap<>();
			Double mos = dateWiseMosMap.get(entry.getKey());
			if (mos != null) {
				mosMap.put(WebRTCConstant.MOSKEY, mos.toString());
			}
			mosMap.put(WebRTCConstant.CALL, entry.getValue().toString());
			oneWeekDataMap.put(entry.getKey(), mosMap);
		}

		return oneWeekDataMap;

	}

	private static Map<String, Map<String, Long>> getMOSAndCallReleaseData(
			List<WebRTCCallSummaryDataWrapper> wrapperList) {
		Map<String, Map<String, Long>> finalmap = new HashMap<>();

		Map<String, Long> callTypeMap = wrapperList.stream().filter(x -> x.getCallReleaseType() != null).collect(
				Collectors.groupingBy(WebRTCCallSummaryDataWrapper::getCallReleaseType, Collectors.counting()));

		Map<String, Long> mosMap = wrapperList.stream().filter(x -> x.getMosType() != null)
				.collect(Collectors.groupingBy(WebRTCCallSummaryDataWrapper::getMosType, Collectors.counting()));

		finalmap.put(WebRTCConstant.MOSKEY, mosMap);
		finalmap.put(WebRTCConstant.CALLTYPE, callTypeMap);

		return finalmap;
	}

	private static Map<String, Map<String, Long>> getCodecData(List<WebRTCCallSummaryDataWrapper> wrapperList) {

		Map<String, Map<String, Long>> mediaTypeMap = null;

		return wrapperList.stream().filter(f -> f.getmType() != null && f.getCodecType() != null)
				.collect(Collectors.groupingBy(WebRTCCallSummaryDataWrapper::getmType,
						Collectors.groupingBy(WebRTCCallSummaryDataWrapper::getCodecType, Collectors.counting())));

	}

	private Map<String, Map<String, Long>> getCountOfNetworkType(List<WebRTCCallSummaryDataWrapper> wrapperList) {
		Map<String, Map<String, Long>> oneWeekDataMap = new HashMap<>();

		if (wrapperList != null) {
			oneWeekDataMap = wrapperList.stream().filter(f -> f.getDate() != null && f.getNetworkType() != null)
					.collect(Collectors.groupingBy(WebRTCCallSummaryDataWrapper::getDate, Collectors
							.groupingBy(WebRTCCallSummaryDataWrapper::getNetworkType, Collectors.counting())));
		}
		return oneWeekDataMap;

	}

	@Override
	public String getDataByIMSI(String deviceId, String startDate, String endDate) {
		logger.trace("inside the method getDataByIMSI deviceId {} startDate{} endDate{}", deviceId, startDate, endDate);
		String response = null;
		try {
			if (deviceId != null) {
				String url = ConfigUtils.getString(ConfigEnum.MICRO_SERVICE_BASE_URL.getValue())
						+ ConfigUtils.getString(NVConfigUtil.GET_WEB_RTC_DATA_FOR_DEVICEID).concat("?deviceId=")
						+ deviceId + "&startDate=" + startDate + "&endDate=" + endDate;
				Duration duration = Duration
						.minutes(Integer.parseInt(ConfigUtils.getString(ConfigEnum.TIMEOUT_VALUE.getValue())));

				logger.trace("Micro service url inside method  getDataByIMSI is {} ", url);
				response = StealthUtils.sendHttpGetRequest(url, false, duration).getString();
				logger.debug("Micro service Response of method getDataByIMSI is  {}", response);
			}
		} catch (HttpException e) {
			logger.error("HttpException insid the method getDataByIMSI imsi {},reason {}", deviceId, e.getMessage());

		} catch (Exception e) {
			logger.error("Exception insid the method getDataByIMSI imsi {},reason {}", deviceId, e.getMessage());

		}
		return response;
	}

	@Override
	public WebRTCCallStatsWrapper getWebRtcDataByCallToken(String callToken) {
		logger.info("inside the method getWebRtcDataByCallToken {}", callToken);
		try {
			List<WebRTCCallDataWrapper> receiverDataList = new ArrayList<>();
			List<WebRTCCallDataWrapper> senderDataList = new ArrayList<>();

			WebRTCCallStatsWrapper wrapper = new WebRTCCallStatsWrapper();
			HBaseResult hbaseResult = webRtcDao.getResultByRowKey(callToken,
					ConfigUtils.getString(NVConfigUtil.WEBSRTC_TABLE_NAME),
					Bytes.toBytes(WebRTCConstant.COLUMN_FAMILY));
			String senderRowkey = hbaseResult.getString(WebRTCConstant.SENDER_ROW_KEY);
			logger.debug("senderRowkey : {}", senderRowkey);
			String receiverRowkey = hbaseResult.getString(WebRTCConstant.RECEIVER_ROW_KEY);
			logger.debug("receiverRowkey : {}", receiverRowkey);
			WebRTCCallDataWrapper senderSummary = getCallDataWrapperByRowKey(senderRowkey);
			senderDataList = getSenderOrReciverDataList(senderDataList, senderSummary);
			WebRTCCallDataWrapper receiverSummary = getCallDataWrapperByRowKey(receiverRowkey);
			receiverDataList = getSenderOrReciverDataList(receiverDataList, receiverSummary);
			return prepareCallDataWrapper(receiverDataList, senderDataList, wrapper, senderSummary, receiverSummary);
		} catch (IOException e) {
			logger.error("IOException insdie The method getWebRtcDataByCallToken callToken {} because {}", callToken,
					Utils.getStackTrace(e));
		} catch (Exception e) {
			logger.error("Exception insdie The method getWebRtcDataByCallToken callToken {} because {}", callToken,
					Utils.getStackTrace(e));
		}
		return null;

	}

	private List<WebRTCCallDataWrapper> getSenderOrReciverDataList(List<WebRTCCallDataWrapper> senderDataList,
			WebRTCCallDataWrapper senderSummary) {
		if (senderSummary != null) {
			senderDataList = webRtcDao.getViberCallData(senderSummary.getDeviceId(), senderSummary.getStartTime(),
					senderSummary.getEndTime(), senderSummary.getMediaType());
		}
		return senderDataList;
	}

	private WebRTCCallStatsWrapper prepareCallDataWrapper(List<WebRTCCallDataWrapper> reciverDataList,
			List<WebRTCCallDataWrapper> senderDataList, WebRTCCallStatsWrapper wrapper,
			WebRTCCallDataWrapper senderSummary, WebRTCCallDataWrapper reciverSummary) {
		wrapper.setSenderLatLongList(getLatLongList(senderDataList));
		wrapper.setReciverLatLongList(getLatLongList(reciverDataList));
		wrapper.setSenderDataList(senderDataList);
		wrapper.setReciverDataList(reciverDataList);
		wrapper.setReciverSummary(reciverSummary != null ? reciverSummary : new WebRTCCallDataWrapper());
		wrapper.setSenderSummary(senderSummary != null ? senderSummary : new WebRTCCallDataWrapper());
		return wrapper;
	}

	private List<List<String>> getLatLongList(List<WebRTCCallDataWrapper> senderDataList) {
		List<List<String>> latLongList = new ArrayList<>();

		if (!senderDataList.isEmpty()) {

			senderDataList.forEach(s -> {
				if (s.getLatitude() != null) {
					List<String> list = new ArrayList<>();
					list.add(s.getLatitude().toString());
					list.add(s.getLongitude().toString());
					list.add(s.getvCallRelType());
					latLongList.add(list);
				}
			});

		}
		return latLongList;

	}

	private WebRTCCallDataWrapper getCallDataWrapperByRowKey(String senderRowkey) {
		try {
			WebRTCCallDataWrapper senderCallDataWrapper = null;
			if (senderRowkey != null) {
				HBaseResult senderHbaseResult = webRtcDao.getResultByRowKey(senderRowkey,
						ConfigUtils.getString(NVConfigUtil.WEBSRTC_TABLE_NAME),
						Bytes.toBytes(WebRTCConstant.COLUMN_FAMILY));
				senderCallDataWrapper = getWebRtcCallDataWrapperFromHbaseResult(senderHbaseResult);

			}
			return senderCallDataWrapper;
		} catch (IOException ioe) {
			logger.error("IOException inside the method getCallDataWrapperByRowKey {}", ioe.getMessage());
			throw new ValueNotFoundException();
		} catch (Exception e) {
			logger.error("Exception inside the method getCallDataWrapperByRowKey {}", e.getMessage());
			throw new ValueNotFoundException();
		}
	}

	private WebRTCCallDataWrapper getWebRtcCallDataWrapperFromHbaseResult(HBaseResult hbaseResult) {
		WebRTCCallDataWrapper wrapper = new WebRTCCallDataWrapper();
		if (hbaseResult != null) {
			wrapper.setRsrp(hbaseResult.getStringAsDouble(WebRTCConstant.COLUMN_AVG_RSRP));
			wrapper.setDeviceId(hbaseResult.getString(WebRTCConstant.COLUMN_DEVICEID));
			wrapper.setSinr(hbaseResult.getStringAsDouble(WebRTCConstant.COLUMN_AVG_SINR));
			wrapper.setMos(hbaseResult.getStringAsDouble(WebRTCConstant.COLUMN_MOS));
			wrapper.setAvgRtt(hbaseResult.getStringAsDouble(WebRTCConstant.COLUMN_AVG_RTT));
			wrapper.setvCallRelType(hbaseResult.getString(WebRTCConstant.COLUMN_V_CALL_RELTYPE));
			wrapper.setMediaType(hbaseResult.getString(WebRTCConstant.COLUMN_V_MEDIA_TYPE));
			setImsiByDeviceId(wrapper);
			WebRTCUtils.setDataByMediaType(hbaseResult, wrapper.getMediaType(), wrapper, null);

			WebRTCUtils.setPacketLossByMediaType(hbaseResult, wrapper.getMediaType(), wrapper);

			wrapper.setStartTime(hbaseResult.getStringAsLong(WebRTCConstant.COLUMN_CALL_START_TIME));
			wrapper.setEndTime(hbaseResult.getStringAsLong(WebRTCConstant.COLUMN_CALL_END_TIME));
			wrapper.setNetworkType(hbaseResult.getString(WebRTCConstant.NETWORK_TYPE));
			wrapper.setrFactor(hbaseResult.getStringAsDouble(WebRTCConstant.RFACTOR));
		}
		logger.info("wrapper =============>{}", wrapper);
		return wrapper;
	}

	private void setImsiByDeviceId(WebRTCCallDataWrapper wrapper) {
		if (wrapper.getDeviceId() != null) {
			try {
				DeviceInfo deviceInfo = iDeviceInfoDao.getDeviceInfoByDeviceId(wrapper.getDeviceId());
				if (deviceInfo != null) {
					wrapper.setImsi(deviceInfo.getImsi());

				}
			} catch (DaoException e) {
				logger.error("Exception inside the method setImsiByDeviceId deviceId{},DaoException",
						wrapper.getDeviceId(), e.getMessage());
			}
		}
	}

	@Override
	public String getCallDataByCallToken(String callToken, HttpServletRequest request) {
			logger.trace("inside the mtheod getCallDataByCallToken callToken{}", callToken);
		String response = null;
		try {
			String url = ConfigUtils.getString(ConfigEnum.MICRO_SERVICE_BASE_URL.getValue())
					+ ConfigUtils.getString(NVConfigUtil.GET_WEB_RTC_CALL_DATA).concat("?" + request.getQueryString());
			Duration duration = Duration
					.minutes(Integer.parseInt(ConfigUtils.getString(ConfigEnum.TIMEOUT_VALUE.getValue())));

			logger.trace("getCallDataForDevice dropwizards url is {}", url);
			response = StealthUtils.sendHttpGetRequest(url, false, duration).getString();
			logger.debug("MicroService Response of getCallDataByCallToken is {}", response);
		}

		catch (HttpException e) {
			logger.error("HttpException insid the method getCallDataForDevice callToken {},reason {}", callToken,
					e.getMessage());

		} catch (Exception e) {
			logger.error("Exception insid the method getCallDataForDevice callToken {},reason {}", callToken,
					e.getMessage());

		}
		return response;
	}

	@Override
	public WebRTCViberDashBoardDataWrapper getViberDashboardCallData(String startDate, String endDate,
			String technology, String operator, String country, String nvModule) {
		logger.trace(
				"inside the method getViberDashboardData startDate {},endDate {},technology {},operator {},country {}, nvModule{}",
				startDate, endDate, technology, operator, country, nvModule);

		Map<String, Long> totalSubcriber = new HashMap<>();
		Map<String, Long> totalCalls = new HashMap<>();
		Map<String, Long> dropCalls = new HashMap<>();
		Map<String, Double> mos = new HashMap<>();

		WebRTCViberDashBoardDataWrapper response = new WebRTCViberDashBoardDataWrapper();
		try {
			List<ViberDashboard> list = viberDashboardDao.getViberDashboardData(startDate, endDate, technology,
					operator, country, nvModule, false);
			List<ViberSubscriber> userCount = viberSubscriberDao.getUserCount(startDate, endDate, technology, operator,
					country, nvModule, false);
			for (ViberSubscriber object : userCount) {
				totalSubcriber = getUserCountMap(object, totalSubcriber);
			}
			for (ViberDashboard object : list) {
				totalCalls = getCallMap(object, totalCalls);
				dropCalls = getDropCallMap(object, dropCalls);
				mos = getMosDataMap(object, mos);
			}
			addTotalSubcriberIntoWrapper(response, totalSubcriber);
			addTotalCallsIntoWrapper(response, totalCalls);
			addDropCallsIntoWrapper(response, dropCalls);
			addMosMapIntoWrapper(response, mos);

			logger.debug("Micro service Response of method getViberDashboardData is  {}", new Gson().toJson(response));
		} catch (Exception e) {
			logger.error(
					"Exception inside the method getViberDashboardData startDate {} , endDate {},technology {}, operator {}, country {} error {}",
					startDate, endDate, technology, operator, country, Utils.getStackTrace(e));
		}
		return response;
	}

	private void addDropCallsIntoWrapper(WebRTCViberDashBoardDataWrapper response, Map<String, Long> dropCalls) {
		if (dropCalls != null) {
			response.setDropCalls(dropCalls);
		}

	}

	private void addTotalCallsIntoWrapper(WebRTCViberDashBoardDataWrapper response, Map<String, Long> totalCalls) {
		if (totalCalls != null) {
			response.setTotalCalls(totalCalls);
		}

	}

	private void addTotalSubcriberIntoWrapper(WebRTCViberDashBoardDataWrapper response,
			Map<String, Long> totalSubcriber) {

		if (totalSubcriber != null) {
			response.setTotalSubcriber(totalSubcriber);
		}
	}

	private void addMosMapIntoWrapper(WebRTCViberDashBoardDataWrapper response, Map<String, Double> mos) {

		if (mos != null) {
			response.setMos(mos);
		}

	}

	private Map<String, Double> getMosDataMap(ViberDashboard object, Map<String, Double> avgMosMap) {
		if (object != null) {
			Double avgMosAudio = object.getAvgMosAudio();
			Double avgMosVideo = object.getAvgMosVideo();
			Double avgMosSwitch = object.getAvgMosMixed();
			Double avgTotalMos = object.getAvgTotalMos();
			putMosDataIntoInnerMap(WebRTCConstant.AUDIO_KEY, avgMosAudio, avgMosMap);
			putMosDataIntoInnerMap(WebRTCConstant.VIDEO_KEY, avgMosVideo, avgMosMap);
			putMosDataIntoInnerMap(WebRTCConstant.SWITCH_KEY, avgMosSwitch, avgMosMap);
			putMosDataIntoInnerMap(WebRTCConstant.AVG_MOS, avgTotalMos, avgMosMap);
		}
		return avgMosMap;
	}

	private void putMosDataIntoInnerMap(String key, Double value, Map<String, Double> map) {
		if (value != null) {
			if (map.containsKey(key)) {
				double avgMos = (map.get(key) + value) / 2;
				map.put(key, avgMos);
			} else {
				map.put(key, value);
			}
		}

	}

	private void putCallDataIntoInnerMap(String key, Long value, Map<String, Long> map) {
		if (value != null) {
			if (map.containsKey(key)) {
				map.put(key, map.get(key) + value);
			} else {
				map.put(key, value);
			}
		}

	}

	private Map<String, Long> getDropCallMap(ViberDashboard object, Map<String, Long> dropCallMap) {
		if (object != null) {
			Long audioDropCall = object.getAudioDropCall();
			Long videoDropCall = object.getVideoDropCall();
			Long switchDropCall = object.getMixedDropCall();
			putCallDataIntoInnerMap(WebRTCConstant.AUDIO_KEY, audioDropCall, dropCallMap);
			putCallDataIntoInnerMap(WebRTCConstant.VIDEO_KEY, videoDropCall, dropCallMap);
			putCallDataIntoInnerMap(WebRTCConstant.SWITCH_KEY, switchDropCall, dropCallMap);
		}

		return dropCallMap;

	}

	private Map<String, Long> getCallMap(ViberDashboard object, Map<String, Long> totalCallMap) {
		if (object != null) {
			Long audioCall = object.getAudioCall();
			Long videoCall = object.getVideoCall();
			Long switchCall = object.getMixedCall();
			putCallDataIntoInnerMap(WebRTCConstant.AUDIO_KEY, audioCall, totalCallMap);
			putCallDataIntoInnerMap(WebRTCConstant.VIDEO_KEY, videoCall, totalCallMap);
			putCallDataIntoInnerMap(WebRTCConstant.SWITCH_KEY, switchCall, totalCallMap);
		}
		return totalCallMap;

	}

	private Map<String, Long> getUserCountMap(ViberSubscriber object, Map<String, Long> subcriberMap) {

		if (object != null) {
			Long audioUc = object.getAudioUserCount();
			Long videoUc = object.getVideoUserCount();
			Long switchUc = object.getMixedUserCount();
			Long totalUserCount = object.getTotalUserCount();
			putCallDataIntoInnerMap(WebRTCConstant.AUDIO_KEY, audioUc, subcriberMap);
			putCallDataIntoInnerMap(WebRTCConstant.VIDEO_KEY, videoUc, subcriberMap);
			putCallDataIntoInnerMap(WebRTCConstant.SWITCH_KEY, switchUc, subcriberMap);
			putCallDataIntoInnerMap(WebRTCConstant.TOTAL_USER, totalUserCount, subcriberMap);
		}
		return subcriberMap;
	}

	@Override
	public WebRTCViberDashBoardDataWrapper getKpiDistribution(String startDate, String endDate, String technology,
			String operator, String country, String nvModule) {
		logger.trace(
				"inside the mtheod getKpiDistribution startDate {},endDate {},technology {},operator {},country {}",
				startDate, endDate, technology, operator, country);

		Map<String, Map<String, Map<String, Long>>> osDistribution = new HashMap<>();
		Map<String, Map<String, Long>> deviceDistribution = new HashMap<>();
		Map<String, Map<String, Long>> kpiRangeWiseMap = new HashMap<>();

		List<NVPassiveDeviceWrapper> parentChildList = new ArrayList<>();
		WebRTCViberDashBoardDataWrapper response = new WebRTCViberDashBoardDataWrapper();
		try {
			List<ViberDashboard> deviceOsRawData = viberDashboardDao.getViberDashboardData(startDate, endDate,
					technology, operator, country, nvModule, false);
			List<ViberDashboard> kpiDistributionRsrp = viberDashboardDao.getViberDashboardData(startDate, endDate,
					WebRTCConstant.ALL, operator, country, nvModule, false);

			if (deviceOsRawData != null) {
				populateKpiSummaryData(kpiDistributionRsrp, kpiRangeWiseMap, technology);
				getOsDistributionData(deviceOsRawData, osDistribution);
				for (ViberDashboard viberData : deviceOsRawData) {
					populateDeviceDistributionData(viberData, deviceDistribution);
				}
				String top5MakeAndModel = iNVPassiveDashboardService.getTop5MakeAndModel(deviceDistribution);
				Type schema = new TypeToken<List<NVPassiveDeviceWrapper>>() {
				}.getType();
				parentChildList = new Gson().fromJson(top5MakeAndModel, schema);
				response.setDeviceDistribution(parentChildList);
				response.setOsDistribution(osDistribution);
				response.setKpiDistribution(kpiRangeWiseMap);
			}
			logger.debug("Response of method getKpiDistribution is  {}", new Gson().toJson(response));

		} catch (Exception e) {
			logger.error(
					"Exception inside the method getKpiDistribution startDate {} , endDate {},technology {}, operator {}, country {} error {}",
					startDate, endDate, technology, operator, country, Utils.getStackTrace(e));
		}
		return response;
	}

	private void populateKpiSummaryData(List<ViberDashboard> kpiDistributionData,
			Map<String, Map<String, Long>> kpiRangeWiseMap, String technology) {
		List<String> rsrp = kpiDistributionData.stream().filter(x -> x.getRsrp() != null).map(ViberDashboard::getRsrp)
				.collect(Collectors.toList());
		List<String> rssi = kpiDistributionData.stream().filter(x -> x.getRssi() != null).map(ViberDashboard::getRssi)
				.collect(Collectors.toList());
		List<String> sinr = kpiDistributionData.stream().filter(x -> x.getSinr() != null).map(ViberDashboard::getSinr)
				.collect(Collectors.toList());
		List<String> rxLevel = kpiDistributionData.stream().filter(x -> x.getRxlevel() != null)
				.map(ViberDashboard::getRxlevel).collect(Collectors.toList());
		List<String> snr = kpiDistributionData.stream().filter(x -> x.getSnr() != null).map(ViberDashboard::getSnr)
				.collect(Collectors.toList());
		List<String> rscp = kpiDistributionData.stream().filter(x -> x.getRscp() != null).map(ViberDashboard::getRscp)
				.collect(Collectors.toList());
		getTechnologyWiseResponse(kpiRangeWiseMap, rsrp, rssi, sinr, rxLevel, snr, rscp, technology);
	}

	private void getTechnologyWiseResponse(Map<String, Map<String, Long>> kpiRangeWiseMap, List<String> rsrp,
			List<String> rssi, List<String> sinr, List<String> rxLevel, List<String> snr, List<String> rscp,
			String technology) {
		if (technology.equalsIgnoreCase(WebRTCConstant.ALL)) {
			convertAndPopulateJsonToMap(WebRTCConstant.RSRP, rsrp, kpiRangeWiseMap);
			convertAndPopulateJsonToMap(WebRTCConstant.SINR, sinr, kpiRangeWiseMap);
			convertAndPopulateJsonToMap(WebRTCConstant.RSSI, rssi, kpiRangeWiseMap);
			convertAndPopulateJsonToMap(WebRTCConstant.RSCP, rscp, kpiRangeWiseMap);
			convertAndPopulateJsonToMap(WebRTCConstant.RX_LEVEL, rxLevel, kpiRangeWiseMap);
			convertAndPopulateJsonToMap(WebRTCConstant.SNR, snr, kpiRangeWiseMap);
		} else if (technology.equalsIgnoreCase(WebRTCConstant.TYPE_3G)) {
			convertAndPopulateJsonToMap(WebRTCConstant.RSCP, rscp, kpiRangeWiseMap);
		} else if (technology.equalsIgnoreCase(WebRTCConstant.TYPE_2G)) {
			convertAndPopulateJsonToMap(WebRTCConstant.RX_LEVEL, rxLevel, kpiRangeWiseMap);
		} else if (technology.equalsIgnoreCase(WebRTCConstant.WIFI_TYPE)) {
			convertAndPopulateJsonToMap(WebRTCConstant.RSSI, rssi, kpiRangeWiseMap);
			convertAndPopulateJsonToMap(WebRTCConstant.SNR, snr, kpiRangeWiseMap);

		} else if (technology.equalsIgnoreCase(WebRTCConstant.TYPE_LTE)) {
			convertAndPopulateJsonToMap(WebRTCConstant.RSRP, rsrp, kpiRangeWiseMap);
			convertAndPopulateJsonToMap(WebRTCConstant.SINR, sinr, kpiRangeWiseMap);
		}

	}

	private void convertAndPopulateJsonToMap(String key, List<String> rangeWiseList,
			Map<String, Map<String, Long>> genericKPIMap) {
		if (rangeWiseList != null) {
			Type type = new TypeToken<Map<String, Long>>() {
			}.getType();
			Map<String, Long> rangeWiseValueMap = null;
			for (String range : rangeWiseList) {
				rangeWiseValueMap = new Gson().fromJson(range, type);
				if (rangeWiseValueMap != null) {
					populateKpiGenericMap(rangeWiseValueMap, genericKPIMap, key);
				}
			}
		}
	}

	private void populateKpiGenericMap(Map<String, Long> rangeWiseValueMap, Map<String, Map<String, Long>> genericMap,
			String key) {
		if (!genericMap.isEmpty()) {
			if (genericMap.containsKey(key)) {
				Map<String, Long> map = genericMap.get(key);
				for (Entry<String, Long> keyValuePair : rangeWiseValueMap.entrySet()) {
					if (map.containsKey(keyValuePair.getKey())) {
						map.put(keyValuePair.getKey(), keyValuePair.getValue() + map.get(keyValuePair.getKey()));
					} else {
						map.put(keyValuePair.getKey(), keyValuePair.getValue());
					}
				}
				genericMap.put(key, map);
			} else {
				genericMap.put(key, rangeWiseValueMap);
			}
		} else {
			genericMap.put(key, rangeWiseValueMap);
		}
	}

	private void populateDeviceDistributionData(ViberDashboard viberData, Map<String, Map<String, Long>> genericMap) {
		if (viberData != null) {
			Type type = new TypeToken<Map<String, Map<String, Long>>>() {
			}.getType();
			Map<String, Map<String, Long>> deviceDistribution = null;
			deviceDistribution = new Gson().fromJson(viberData.getDeviceDistribution(), type);
			if (deviceDistribution != null) {
				getDeviceDistributionData(genericMap, deviceDistribution);
			}

		}
	}

	private void getDeviceDistributionData(Map<String, Map<String, Long>> genericMap,
			Map<String, Map<String, Long>> deviceDistribution) {
		if (genericMap.size() > ForesightConstants.ZERO) {
			for (Entry<String, Map<String, Long>> outerRawMap : deviceDistribution.entrySet()) {
				String outerRawKey = outerRawMap.getKey();
				if (genericMap.containsKey(outerRawKey)) {
					Map<String, Long> genericInnerMap = genericMap.get(outerRawKey);
					for (Entry<String, Long> innerRawMap : outerRawMap.getValue().entrySet()) {
						if (genericInnerMap.containsKey(innerRawMap.getKey())) {
							Long newValue = innerRawMap.getValue();
							Long oldValue = genericInnerMap.get(innerRawMap.getKey());
							genericInnerMap.put(innerRawMap.getKey(), newValue + oldValue);
						} else {
							genericInnerMap.put(innerRawMap.getKey(), innerRawMap.getValue());
						}
					}
					genericMap.put(outerRawKey, genericInnerMap);
				} else {
					genericMap.put(outerRawKey, outerRawMap.getValue());
				}
			}
		} else {
			genericMap.putAll(deviceDistribution);
		}
	}

	private void getOsDistributionData(List<ViberDashboard> kpiDistributionData,
			Map<String, Map<String, Map<String, Long>>> finalDistributionResult) {
		Map<String, Map<String, Long>> osType = new HashMap<>();
		Map<String, Long> callTypeIos = new HashMap<>();
		Map<String, Long> callTypeAndroid = new HashMap<>();
		Long videoIos = kpiDistributionData.stream().filter(x -> x.getVideoIosCount() != null)
				.mapToLong(ViberDashboard::getVideoIosCount).sum();
		Long audioIos = kpiDistributionData.stream().filter(x -> x.getAudioIosCount() != null)
				.mapToLong(ViberDashboard::getAudioIosCount).sum();
		Long switchIos = kpiDistributionData.stream().filter(x -> x.getMixedIosCount() != null)
				.mapToLong(ViberDashboard::getMixedIosCount).sum();
		Long videoAndroid = kpiDistributionData.stream().filter(x -> x.getVideoAndroidCount() != null)
				.mapToLong(ViberDashboard::getVideoAndroidCount).sum();
		Long audioAndroid = kpiDistributionData.stream().filter(x -> x.getAudioAndroidCount() != null)
				.mapToLong(ViberDashboard::getAudioAndroidCount).sum();
		Long switchAndroid = kpiDistributionData.stream().filter(x -> x.getMixedAndroidCount() != null)
				.mapToLong(ViberDashboard::getMixedAndroidCount).sum();

		callTypeIos.put(WebRTCConstant.AUDIO_KEY, audioIos);
		callTypeIos.put(WebRTCConstant.VIDEO_KEY, videoIos);
		callTypeIos.put(WebRTCConstant.SWITCH_KEY, switchIos);
		osType.put(WebRTCConstant.IOS, callTypeIos);
		callTypeAndroid.put(WebRTCConstant.AUDIO_KEY, audioAndroid);
		callTypeAndroid.put(WebRTCConstant.VIDEO_KEY, videoAndroid);
		callTypeAndroid.put(WebRTCConstant.SWITCH_KEY, switchAndroid);
		osType.put(WebRTCConstant.ANDROID, callTypeAndroid);
		finalDistributionResult.put(WebRTCConstant.OS, osType);
	}

	@Override
	public Map<String, CallStatsWrapper> processLastSevenDaysData(String startDate, String endDate, String technology,
			String operator, String country, String nvModule) {
		logger.trace(
				"inside the mtheod processLastSevenDaysData startDate {},endDate {},technology {},operator {},country {}, nvModule {}",
				startDate, endDate, technology, operator, country, nvModule);
		Map<String, CallStatsWrapper> response = new HashMap<>();
		try {
			List<ViberDashboard> viberDashboardData = viberDashboardDao.getViberDashboardData(startDate, endDate,
					technology, operator, country, nvModule, true);
			List<ViberSubscriber> userCount = viberSubscriberDao.getUserCount(startDate, endDate, technology, operator,
					country, nvModule, true);
			extractAggregatedValues(response, viberDashboardData, userCount);
		} catch (Exception e) {
			logger.error(
					"Exception inside the method processLastSevenDaysData startDate {} , endDate {},technology {}, operator {}, country {} error {}",
					startDate, endDate, technology, operator, country, Utils.getStackTrace(e));
		}
		return response;

	}

	private void extractAggregatedValues(Map<String, CallStatsWrapper> response,
			List<ViberDashboard> viberDashboardData, List<ViberSubscriber> userCount) {

		Map<String, Long> audioDropCallMap = convertDateToStringLong(
				viberDashboardData.stream().filter(x -> x.getAudioDropCall() != null).collect(Collectors.groupingBy(
						ViberDashboard::getCreationDate, Collectors.summingLong(ViberDashboard::getAudioDropCall))));
		Map<String, Long> videoDropCallMap = convertDateToStringLong(
				viberDashboardData.stream().filter(x -> x.getVideoDropCall() != null).collect(Collectors.groupingBy(
						ViberDashboard::getCreationDate, Collectors.summingLong(ViberDashboard::getVideoDropCall))));
		Map<String, Long> switchDropCallMap = convertDateToStringLong(
				viberDashboardData.stream().filter(x -> x.getMixedDropCall() != null).collect(Collectors.groupingBy(
						ViberDashboard::getCreationDate, Collectors.summingLong(ViberDashboard::getMixedDropCall))));
		Map<String, Long> audioSuccessCallMap = convertDateToStringLong(
				viberDashboardData.stream().filter(x -> x.getAudioSuccessCall() != null).collect(Collectors.groupingBy(
						ViberDashboard::getCreationDate, Collectors.summingLong(ViberDashboard::getAudioSuccessCall))));
		Map<String, Long> videoSuccessCallMap = convertDateToStringLong(
				viberDashboardData.stream().filter(x -> x.getVideoSuccessCall() != null).collect(Collectors.groupingBy(
						ViberDashboard::getCreationDate, Collectors.summingLong(ViberDashboard::getVideoSuccessCall))));
		Map<String, Long> switchSuccessCallMap = convertDateToStringLong(
				viberDashboardData.stream().filter(x -> x.getMixedSuccessCall() != null).collect(Collectors.groupingBy(
						ViberDashboard::getCreationDate, Collectors.summingLong(ViberDashboard::getMixedSuccessCall))));
		Map<String, Long> audioUserCountMap = convertDateToStringLong(
				userCount.stream().filter(x -> x.getAudioUserCount() != null).collect(Collectors.groupingBy(
						ViberSubscriber::getCreationDate, Collectors.summingLong(ViberSubscriber::getAudioUserCount))));
		Map<String, Long> videoUserCountMap = convertDateToStringLong(
				userCount.stream().filter(x -> x.getVideoUserCount() != null).collect(Collectors.groupingBy(
						ViberSubscriber::getCreationDate, Collectors.summingLong(ViberSubscriber::getVideoUserCount))));
		Map<String, Long> switchUserCountMap = convertDateToStringLong(
				userCount.stream().filter(x -> x.getMixedUserCount() != null).collect(Collectors.groupingBy(
						ViberSubscriber::getCreationDate, Collectors.summingLong(ViberSubscriber::getMixedUserCount))));
		Map<String, Double> avgJitterMap = convertDateToStringDouble(
				viberDashboardData.stream().filter(x -> x.getAvgJitter() != null).collect(Collectors.groupingBy(
						ViberDashboard::getCreationDate, Collectors.averagingDouble(ViberDashboard::getAvgJitter))));
		Map<String, Double> avgRTTMap = convertDateToStringDouble(
				viberDashboardData.stream().filter(x -> x.getAvgRtt() != null).collect(Collectors.groupingBy(
						ViberDashboard::getCreationDate, Collectors.averagingDouble(ViberDashboard::getAvgRtt))));
		Map<String, Double> avgPacketLossMap = convertDateToStringDouble(
				viberDashboardData.stream().filter(x -> x.getAvgPacketLoss() != null)
						.collect(Collectors.groupingBy(ViberDashboard::getCreationDate,
								Collectors.averagingDouble(ViberDashboard::getAvgPacketLoss))));
		Map<String, Double> avgMosAudio = convertDateToStringDouble(
				viberDashboardData.stream().filter(x -> x.getAvgMosAudio() != null).collect(Collectors.groupingBy(
						ViberDashboard::getCreationDate, Collectors.averagingDouble(ViberDashboard::getAvgMosAudio))));
		Map<String, Double> avgMosVideo = convertDateToStringDouble(
				viberDashboardData.stream().filter(x -> x.getAvgMosVideo() != null).collect(Collectors.groupingBy(
						ViberDashboard::getCreationDate, Collectors.averagingDouble(ViberDashboard::getAvgMosVideo))));
		Map<String, Double> avgMosMixed = convertDateToStringDouble(
				viberDashboardData.stream().filter(x -> x.getAvgMosMixed() != null).collect(Collectors.groupingBy(
						ViberDashboard::getCreationDate, Collectors.averagingDouble(ViberDashboard::getAvgMosMixed))));
		Map<String, Double> avgTotalMosMap = convertDateToStringDouble(
				viberDashboardData.stream().filter(x -> x.getAvgTotalMos() != null).collect(Collectors.groupingBy(
						ViberDashboard::getCreationDate, Collectors.averagingDouble(ViberDashboard::getAvgTotalMos))));

		Double avgMosMixedValue = null;
		Double avgMosAudioValue = null;
		Double avgMosVideoValue = null;
		if (avgMosAudio != null && !avgMosAudio.isEmpty()) {
			avgMosAudioValue = avgMosAudio.values().stream().filter(Objects::nonNull).mapToDouble(x -> x).average()
					.getAsDouble();
		}
		if (avgMosVideo != null && !avgMosVideo.isEmpty()) {
			avgMosVideoValue = avgMosVideo.values().stream().filter(Objects::nonNull).mapToDouble(x -> x).average()
					.getAsDouble();
		}
		if (avgMosMixed != null && !avgMosMixed.isEmpty()) {
			avgMosMixedValue = avgMosMixed.values().stream().filter(Objects::nonNull).mapToDouble(x -> x).average()
					.getAsDouble();
		}
		processPreviousData(audioDropCallMap, WebRTCConstant.AUDIO_KEY, response, avgMosAudioValue,
				WebRTCConstant.DROP_CALL_KEY);
		processPreviousData(audioSuccessCallMap, WebRTCConstant.AUDIO_KEY, response, avgMosAudioValue,
				WebRTCConstant.SUCCESS_CALL_KEY);
		processPreviousData(audioUserCountMap, WebRTCConstant.AUDIO_KEY, response, avgMosAudioValue,
				WebRTCConstant.USER_KEY);
		processPreviousData(videoDropCallMap, WebRTCConstant.VIDEO_KEY, response, avgMosVideoValue,
				WebRTCConstant.DROP_CALL_KEY);
		processPreviousData(videoSuccessCallMap, WebRTCConstant.VIDEO_KEY, response, avgMosVideoValue,
				WebRTCConstant.SUCCESS_CALL_KEY);
		processPreviousData(videoUserCountMap, WebRTCConstant.VIDEO_KEY, response, avgMosVideoValue,
				WebRTCConstant.USER_KEY);
		processPreviousData(switchDropCallMap, WebRTCConstant.SWITCH_KEY, response, avgMosMixedValue,
				WebRTCConstant.DROP_CALL_KEY);
		processPreviousData(switchSuccessCallMap, WebRTCConstant.SWITCH_KEY, response, avgMosMixedValue,
				WebRTCConstant.SUCCESS_CALL_KEY);
		processPreviousData(switchUserCountMap, WebRTCConstant.SWITCH_KEY, response, avgMosMixedValue,
				WebRTCConstant.USER_KEY);
		processPreviousData(avgJitterMap, WebRTCConstant.MOSKEY, response, null, WebRTCConstant.JITTER_KEY);
		processPreviousData(avgRTTMap, WebRTCConstant.MOSKEY, response, null, WebRTCConstant.RTT_KEY);
		processPreviousData(avgPacketLossMap, WebRTCConstant.MOSKEY, response, null, WebRTCConstant.PACKET_LOSS_KEY);
		processPreviousData(avgTotalMosMap, WebRTCConstant.MOSKEY, response, null, WebRTCConstant.AVERAGE_MOS_KEY);

		logger.info("Response::::: {}", new Gson().toJson(response));

	}

	private void processPreviousData(Map<String, ?> daywiseMap, String key,
			Map<String, CallStatsWrapper> genericResponseMap, Double totalAvgMosLast7Days, String secondKey) {

		if (genericResponseMap != null && key != null) {
			CallStatsWrapper wrapper = new CallStatsWrapper();
			Map<String, Map<String, Map<String, Object>>> finalMap = new HashMap<>();
			Map<String, Map<String, Object>> secondInnerMap = new HashMap<>();

			if (genericResponseMap.containsKey(key)) {
				CallStatsWrapper callStatsWrapper = genericResponseMap.get(key);
				Map<String, Map<String, Map<String, Object>>> callTypeData = callStatsWrapper.getCallTypeData();
				if (callTypeData.containsKey(key.concat(WebRTCConstant.DATA))) {
					Map<String, Map<String, Object>> inputSecondInnerMap = callTypeData
							.get(key.concat(WebRTCConstant.DATA));
					for (Entry<String, ?> daywiseKpiEntry : daywiseMap.entrySet()) {
						if (inputSecondInnerMap.containsKey(daywiseKpiEntry.getKey())) {
							Map<String, Object> inputInnerMap = inputSecondInnerMap.get(daywiseKpiEntry.getKey());
							inputInnerMap.put(secondKey, daywiseKpiEntry.getValue());
							inputSecondInnerMap.put(daywiseKpiEntry.getKey(), inputInnerMap);
						} else {
							Map<String, Object> innerMap = new HashMap<>();
							Object value = daywiseKpiEntry.getValue();
							String dayKey = daywiseKpiEntry.getKey();
							innerMap.put(secondKey, value);
							inputSecondInnerMap.put(dayKey, innerMap);
						}
					}
					callTypeData.put(key.concat(WebRTCConstant.DATA), inputSecondInnerMap);
				}
				callStatsWrapper.setCallTypeData(callTypeData);
				genericResponseMap.put(key, callStatsWrapper);
			} else {
				if (totalAvgMosLast7Days != null) {
					wrapper.setAvgTotalMos(totalAvgMosLast7Days);
				}
				for (Entry<String, ?> dayWiseMap : daywiseMap.entrySet()) {
					Map<String, Object> innerMap = new HashMap<>();
					Object value = dayWiseMap.getValue();
					String dayKey = dayWiseMap.getKey();
					innerMap.put(secondKey, value);
					secondInnerMap.put(dayKey, innerMap);
				}
				finalMap.put(key.concat(WebRTCConstant.DATA), secondInnerMap);
				wrapper.setCallTypeData(finalMap);
				genericResponseMap.put(key, wrapper);
			}
		}
	}

	private Map<String, Double> convertDateToStringDouble(Map<Date, Double> inputMap) {
		if (inputMap != null) {
			Map<String, Double> map = new HashMap<>();
			SimpleDateFormat formatter = new SimpleDateFormat("dd MMM");
			for (Entry<Date, Double> entry : inputMap.entrySet()) {
				String key = formatter.format(entry.getKey());
				map.put(key, entry.getValue());
			}
			return map;
		}
		return null;
	}

	private Map<String, Long> convertDateToStringLong(Map<Date, Long> inputMap) {
		if (inputMap != null) {
			Map<String, Long> map = new HashMap<>();
			SimpleDateFormat formatter = new SimpleDateFormat("dd MMM");
			for (Entry<Date, Long> entry : inputMap.entrySet()) {
				String key = formatter.format(entry.getKey());
				map.put(key, entry.getValue());
			}
			return map;
		}
		return null;
	}

	@Override
	public Map<String, ?> getLayerData(Double nELat, Double nELng, Double sWLat, Double sWLng, Integer zoom,
			String mediaType, String callDirection, String releaseType, String os, Long startTime, Long endTime,
			String minValue, String maxValue, String layerType) throws ParseException {
		Map<String, Long> callLayer;
		List<ViberLayer> viberLayerData = null;
		try {
			Corner corner = new Corner(sWLat, sWLng, nELat, nELng);
			List<String> tileIdList = getTileIdByZoom(zoom, corner);

			if (tileIdList != null) {

				viberLayerData = iViberLayer.getViberLayerData(tileIdList, zoom, mediaType, callDirection, releaseType,
						os, startTime, endTime, minValue, maxValue, layerType);

				List<WebRTCViberLayerWrapper> viberWrapperList = setModelDataIntoWrapper(viberLayerData, zoom);

				if (viberLayerData != null && zoom >= WebRTCConstant.FIVE && zoom <= WebRTCConstant.SEVENTEEN) {

					callLayer = viberWrapperList.stream().filter(x -> x.getGrid() != null)
							.collect(Collectors.groupingBy(WebRTCViberLayerWrapper::getGrid, Collectors.counting()));

					if (layerType.equalsIgnoreCase(WebRTCConstant.CALL_LAYER)) {

						return callLayer;
					} else if (layerType.equalsIgnoreCase(WebRTCConstant.MOS_LAYER)) {

						Map<String, Double> avgMOSMap = viberWrapperList.stream()
								.filter(x -> x.getGrid() != null && x.getMosValue() != null)
								.collect(Collectors.groupingBy(WebRTCViberLayerWrapper::getGrid,
										Collectors.averagingDouble(WebRTCViberLayerWrapper::getMosValue)));
						return mergeMap(callLayer, avgMOSMap);

					} else if (layerType.equalsIgnoreCase(WebRTCConstant.PKTLOSS_LAYER)) {

						Map<String, Double> pkltLossLayerMap = viberWrapperList.stream()
								.filter(x -> x.getGrid() != null)
								.collect(Collectors.groupingBy(WebRTCViberLayerWrapper::getGrid,
										Collectors.averagingDouble(WebRTCViberLayerWrapper::getPacketLoss)));

						return mergeMap(callLayer, pkltLossLayerMap);
					}

				} else if (viberLayerData != null && zoom <= WebRTCConstant.EIGHTEEN) {

					return viberWrapperList.stream().filter(x -> x.getGrid() != null)
							.collect(Collectors.groupingBy(WebRTCViberLayerWrapper::getGrid, Collectors.toList()));

				}
			} else {
				throw new NullPointerException("Tileid list is null for given viewport and zoom");
			}
		} catch (Exception e) {
			logger.info("Exception is getLayerData {}", Utils.getStackTrace(e));
		}

		return null;
	}

	private Map<String, List<String>> mergeMap(Map<String, Long> callLayer, Map<String, Double> kpiMap) {
		Map<String, List<String>> map = new HashMap<>();
		Iterator<Entry<String, Long>> iterator = callLayer.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, Long> entry = iterator.next();
			String callLayerKey = entry.getKey();
			Long callCount = entry.getValue();
			Double avgMOS = kpiMap.get(callLayerKey);
			List<String> valueList = new ArrayList<>();
			if (callCount != null) {
				valueList.add(callCount.toString());
			}
			if (avgMOS != null) {
				valueList.add(avgMOS.toString());
			}
			map.put(callLayerKey, valueList);
		}
		return map;
	}

	private List<WebRTCViberLayerWrapper> setModelDataIntoWrapper(List<ViberLayer> viberLayerData, int zoom) {
		List<WebRTCViberLayerWrapper> wrapperList = new ArrayList<>();
		for (ViberLayer layerData : viberLayerData) {
			WebRTCViberLayerWrapper viberWrapper = new WebRTCViberLayerWrapper();

			setGrid(zoom, layerData, viberWrapper);

			setCallToken(layerData, viberWrapper);
			setCallDirection(layerData, viberWrapper);

			setDeviceId(layerData, viberWrapper);

			setMediaType(layerData, viberWrapper);

			setMos(layerData, viberWrapper);

			setOsName(layerData, viberWrapper);

			setPacketLoss(layerData, viberWrapper);

			setNetworkType(layerData, viberWrapper);

			setStartTime(layerData, viberWrapper);

			setEndTime(layerData, viberWrapper);

			wrapperList.add(viberWrapper);
		}
		return wrapperList;

	}

	private void setEndTime(ViberLayer layerData, WebRTCViberLayerWrapper viberWrapper) {
		long endTime = layerData.getEndTime();

		if (Utils.hasValue(endTime)) {
			viberWrapper.setEndTime(endTime);
		}
	}

	private void setStartTime(ViberLayer layerData, WebRTCViberLayerWrapper viberWrapper) {
		long startTime = layerData.getStartTime();
		if (Utils.hasValue(startTime)) {
			viberWrapper.setStartTime(startTime);
		}
	}

	private void setNetworkType(ViberLayer layerData, WebRTCViberLayerWrapper viberWrapper) {
		String networkType = layerData.getNetworkType();
		if (Utils.hasValue(networkType)) {
			viberWrapper.setNetworkType(networkType);
		}
	}

	private void setPacketLoss(ViberLayer layerData, WebRTCViberLayerWrapper viberWrapper) {
		Double packetLoss = layerData.getPacketLoss();
		if (Utils.hasValue(packetLoss)) {
			viberWrapper.setPacketLoss(packetLoss);
		}
	}

	private void setOsName(ViberLayer layerData, WebRTCViberLayerWrapper viberWrapper) {
		String osName = layerData.getOsName();
		if (Utils.hasValue(osName)) {
			viberWrapper.setOsName(osName);
		}
	}

	private void setMos(ViberLayer layerData, WebRTCViberLayerWrapper viberWrapper) {
		Double mosValue = layerData.getMosValue();
		if (Utils.hasValue(mosValue)) {
			viberWrapper.setMosValue(mosValue);
			String mosType = getMosType(mosValue);
			viberWrapper.setMosType(mosType);
		}
	}

	private void setMediaType(ViberLayer layerData, WebRTCViberLayerWrapper viberWrapper) {
		String mediaType = layerData.getMediaType();
		if (Utils.hasValue(mediaType)) {
			viberWrapper.setMediaType(mediaType);
		}
	}

	private void setDeviceId(ViberLayer layerData, WebRTCViberLayerWrapper viberWrapper) {
		String deviceId = layerData.getDeviceId();
		if (Utils.hasValue(deviceId)) {
			viberWrapper.setDeviceId(deviceId);
		}
	}

	private void setCallDirection(ViberLayer layerData, WebRTCViberLayerWrapper viberWrapper) {
		String callDirection = layerData.getCallDirection();
		if (Utils.hasValue(callDirection)) {
			viberWrapper.setCallDirection(callDirection);
		}
	}

	private void setCallToken(ViberLayer layerData, WebRTCViberLayerWrapper viberWrapper) {
		String callToken = layerData.getCallToken();
		if (Utils.hasValue(callToken)) {
			viberWrapper.setCallToken(callToken);
		}
	}

	private void setGrid(int zoom, ViberLayer layerData, WebRTCViberLayerWrapper viberWrapper) {
		Double latitude = layerData.getLatitude();
		Double longitude = layerData.getLongitude();
		String grid = null;
		if (Utils.hasValue(latitude) && Utils.hasValue(longitude)) {
			grid = getGrid(latitude, longitude, zoom);

			viberWrapper.setGrid(grid);

		}
	}

	private String getMosType(Double mosValue) {
		if (mosValue >= WebRTCConstant.GOOD_MOS_VALUE) {
			return WebRTCConstant.GOOD_KEY;
		} else if (mosValue >= WebRTCConstant.BAD_MOS_VALUE && mosValue < WebRTCConstant.GOOD_MOS_VALUE) {
			return WebRTCConstant.AVERAGE_MOS_KEY;
		} else if (mosValue < WebRTCConstant.BAD_MOS_VALUE) {
			return WebRTCConstant.BAD_KEY;
		}
		return null;
	}

	private String getGrid(Double latitude, Double longitude, int zoom) {

		Double referenceLat = 19.0760;
		Double referenceLong = 72.8777;

		double gridsize = getGridSize(zoom);

		DegreeGrid degreeGrid = new DegreeGrid(gridsize, new LatLng(referenceLat, referenceLong));

		Corner grid = degreeGrid.getCornerForTopLeftGrid(degreeGrid.getGrid(new LatLng(latitude, longitude)));
		LatLng centroid = grid.getCentroid();
		return centroid.getLatitude() + ForesightConstants.UNDERSCORE + centroid.getLongitude();

	}

	private double getGridSize(int zoom) {

		switch (zoom) {
		case 5:
			return 51200.00 * 2;
		case 6:
			return 25600.00 * 2;
		case 7:
			return 12800.00 * 2;

		case 8:
			return 6400.00 * 2;

		case 9:
			return 3200.00 * 2;

		case 10:
			return 1600.00 * 2;

		case 11:
			return 800.00 * 2;

		case 12:
			return 400.00 * 2;

		case 13:
			return 200.00 * 2;

		case 14:
			return 100.00 * 2;

		case 15:
			return 50.00 * 2;

		case 16:
			return 25.00 * 2;

		case 17:
			return 10.00 * 2;

		case 18:
			return 10.00 * 2;

		default:
			throw new IllegalArgumentException("Invalid zoom level value");
		}

	}

	private List<String> getTileIdByZoom(int zoom, Corner corner) {
		List<Tile> tiles = null;
		logger.info("inside getTileIdByZoom method {} {}", corner, zoom);
		if (zoom == WebRTCConstant.FIVE || zoom == WebRTCConstant.SIX || zoom == WebRTCConstant.SEVEN
				|| zoom == WebRTCConstant.EIGHT) {

			logger.info("condition is true for zoom level");
			tiles = TileBoundaryUtils.getTiles(corner, WebRTCConstant.FIVE);
		} else if (zoom == WebRTCConstant.NINE || zoom == WebRTCConstant.TEN || zoom == WebRTCConstant.ELEVEN
				|| zoom == WebRTCConstant.TWELVE) {
			tiles = TileBoundaryUtils.getTiles(corner, WebRTCConstant.NINE);

		} else if (zoom == WebRTCConstant.THIRTEEN || zoom == WebRTCConstant.FOURTEEN
				|| zoom == WebRTCConstant.FIFTEEN) {
			tiles = TileBoundaryUtils.getTiles(corner, WebRTCConstant.THIRTEEN);

		} else if (zoom == WebRTCConstant.SIXTEEN || zoom == WebRTCConstant.SEVENTEEN
				|| zoom == WebRTCConstant.EIGHTEEN) {
			tiles = TileBoundaryUtils.getTiles(corner, WebRTCConstant.SIXTEEN);

		}
		List<String> listOfTileId = null;
		if (tiles != null) {
			listOfTileId = tiles.stream().map(Tile::getIdWithZoom).collect(Collectors.toList());
		}
		return listOfTileId;
	}
}

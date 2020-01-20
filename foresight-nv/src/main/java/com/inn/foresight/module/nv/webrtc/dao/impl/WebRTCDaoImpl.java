package com.inn.foresight.module.nv.webrtc.dao.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.hadoop.hbase.HBaseResult;
import com.inn.commons.lang.StringUtils;
import com.inn.foresight.core.generic.dao.impl.hbase.AbstractHBaseDao;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.NVConfigUtil;
import com.inn.foresight.module.nv.webrtc.constant.WebRTCConstant;
import com.inn.foresight.module.nv.webrtc.constant.WebRTCUtils;
import com.inn.foresight.module.nv.webrtc.dao.IWebRTCDao;
import com.inn.foresight.module.nv.webrtc.wrapper.WebRTCCallDataWrapper;
import com.inn.foresight.module.nv.webrtc.wrapper.WebRTCCallSummaryDataWrapper;

@Service("WebRTCDaoImpl")
public class WebRTCDaoImpl extends AbstractHBaseDao implements IWebRTCDao {
	private Logger logger = LogManager.getLogger(WebRTCDaoImpl.class);

	public WebRTCDaoImpl() {
		super();
	}

	@Override
	public List<WebRTCCallSummaryDataWrapper> setHBaseDataIntoWrapper(Scan scan, String tableName, byte[] columnFamily)
			throws IOException {
		List<WebRTCCallSummaryDataWrapper> dataList = new ArrayList<>();
		logger.info("inside the method setHBaseDataIntoWrapper {}", tableName);

		List<HBaseResult> scanResultByPool = scanResultByPool(scan, tableName, columnFamily);
		logger.info("Size of fetched data from hbase {}", scanResultByPool.size());
		try {
			for (HBaseResult hbaseData : scanResultByPool) {
				WebRTCCallSummaryDataWrapper wrapperData = new WebRTCCallSummaryDataWrapper();
				// get Data from Hbase
				Double latitude = hbaseData.getStringAsDouble(WebRTCConstant.COLUMN_LAT);
				Double longitude = hbaseData.getStringAsDouble(WebRTCConstant.COLUMN_LONG);
				Double avgMOS = hbaseData.getStringAsDouble(WebRTCConstant.COLUMN_MOS);
				String callType = hbaseData.getString(WebRTCConstant.V_CALL_TYPE);
				String callRelease = hbaseData.getString(WebRTCConstant.V_CALL_REL_TYPE);
				String mediaType = hbaseData.getString(WebRTCConstant.V_MEDIA_TYPE);
				String networkType = hbaseData.getString(WebRTCConstant.NETWORK_TYPE);
				Long callStartTime = hbaseData.getStringAsLong(WebRTCConstant.COLUMN_CALL_START_TIME);
				Long callEndTime = hbaseData.getStringAsLong(WebRTCConstant.COLUMN_CALL_END_TIME);
				String callToken = hbaseData.getString(WebRTCConstant.V_CALL_TOKEN);
				String latestMediaType = hbaseData.getString(WebRTCConstant.M_TYPE);
				String codecName = setCodecName(hbaseData, mediaType, latestMediaType);

				// set hbase Data into Wrapper
				if (latitude != null) {

					wrapperData.setLatitude(latitude);
				}
				if (longitude != null) {
					wrapperData.setLongitude(longitude);
				}
				if (avgMOS != null && !avgMOS.equals(Double.NaN)) {
					wrapperData.setAvgMos(avgMOS);
					String modeType = getModeType(avgMOS);
					if (modeType != null) {
						wrapperData.setMosType(modeType);
					}
				}

				if (callType != null) {
					wrapperData.setCallType(callType);
				}

				// will be remove
				if (callRelease != null) {
					if (callRelease.equalsIgnoreCase("error")) {
						wrapperData.setCallReleaseType("Drop");
					} else {
						wrapperData.setCallReleaseType(callRelease);
					}
				}
				if (mediaType != null) {
					wrapperData.setMediaType(mediaType);
				}
				if(latestMediaType != null) {
					wrapperData.setmType(latestMediaType);
				} else if (mediaType != null) {
					wrapperData.setmType(mediaType);
				}

				if (networkType != null) {
					networkType = networkType.split(ForesightConstants.COMMA)[ForesightConstants.ZERO];
					wrapperData.setNetworkType(networkType);
				}
				if (callStartTime != null) {
					wrapperData.setCallStartTime(callStartTime);
				}

				if (callEndTime != null) {
					wrapperData.setCallEndTime(callEndTime);
					wrapperData.setDate(convertToDate(callEndTime));
				}

				if (callToken != null) {
					wrapperData.setCallToken(callToken);
				}
				if (codecName != null) {
					wrapperData.setCodecType(codecName);
				}

				dataList.add(wrapperData);
			}
			logger.info("WebRTCCallSummaryDataWrapper Wrapper size is {}", dataList.size());
			return dataList;
		} catch (Exception e) {

			logger.info("Exception in set hbase data into wrapper {}", Utils.getStackTrace(e));
		}
		return dataList;

	}

	private String setCodecName(HBaseResult hbaseData, String mediaType, String latestMediaType) {
		String codecName = null;
		String media = null;
		if(latestMediaType != null) {
			media = latestMediaType;
		} else {
			media = mediaType;
		}
		if (media != null && media.equalsIgnoreCase(WebRTCConstant.AUDIO_KEY)) {
			codecName = hbaseData.getString(WebRTCConstant.G_CDC_NM_AS);
		} else if (media != null && media.equalsIgnoreCase(WebRTCConstant.VIDEO_KEY)) {
			codecName = hbaseData.getString(WebRTCConstant.G_CDC_NM_VS);
		}
		
		return codecName;
	}

	private String getModeType(Double mosValue) {
		if (mosValue >= WebRTCConstant.GOOD_MOS_VALUE) {
			return WebRTCConstant.GOOD_KEY;
		} else if (mosValue >= WebRTCConstant.BAD_MOS_VALUE && mosValue < WebRTCConstant.GOOD_MOS_VALUE) {
			return WebRTCConstant.AVERAGE;
		} else if (mosValue < WebRTCConstant.BAD_MOS_VALUE) {
			return WebRTCConstant.BAD_KEY;
		}
		return null;
	}

	private String convertToDate(long time) {

		return new SimpleDateFormat(WebRTCConstant.DATE_FORMATE).format(new Date(time * ForesightConstants.THOUSAND));
	}

	@Override
	public List<WebRTCCallDataWrapper> getViberCallData(String deviceId, Long startTime, Long endTime,
			String mediaType) {
		String startRow = getRowKeyForViberCallData(deviceId, startTime);
		String stopRow = getRowKeyForViberCallData(deviceId, endTime);
		logger.info("startRow: {}", startRow);
		logger.info("stopRow: {}", stopRow);
		Scan scan = new Scan();
		scan.withStartRow(startRow.getBytes());
		scan.withStopRow(stopRow.getBytes());

		List<WebRTCCallDataWrapper> wrapperList = extractWebRTCDataFromHbase(scan, mediaType);
		logger.info("wrapperList size: {}", wrapperList.size());
		return wrapperList;
	}

	private List<WebRTCCallDataWrapper> extractWebRTCDataFromHbase(Scan scan, String mediaType) {
		List<WebRTCCallDataWrapper> wrapperList = new ArrayList<>();
		try {

			List<HBaseResult> scanResultByPool = scanResultByPool(scan,
					ConfigUtils.getString(NVConfigUtil.WEBRTC_TABLE_NAME),
					Bytes.toBytes(ConfigUtils.getString(NVConfigUtil.WEBRTC_COLUMN_FAMILY)));
			WebRTCCallDataWrapper previousWrapper = null;
			for (int index = 0; index < scanResultByPool.size(); index++) {
				previousWrapper = setWebRTCCallDataWrapper(scanResultByPool.get(index), mediaType, previousWrapper);
				wrapperList.add(previousWrapper);
			}
		} catch (Exception e) {
			logger.error("Error while getting WebRTC Call Data: {}", ExceptionUtils.getStackTrace(e));
		}
		return wrapperList;
	}

	private WebRTCCallDataWrapper setWebRTCCallDataWrapper(HBaseResult hbaseResult, String mediaType,
			WebRTCCallDataWrapper previousWrapper) {
		WebRTCCallDataWrapper wrapper = new WebRTCCallDataWrapper();
		if (hbaseResult != null) {
			String networkType = hbaseResult.getString(WebRTCConstant.NETWORK_TYPE);
			Long timestamp = hbaseResult.getTimestamp(Bytes.toBytes(WebRTCConstant.HANDOVER));
			wrapper.setDeviceId(hbaseResult.getString(WebRTCConstant.COLUMN_DEVICEID));
			wrapper.setvCallRelType(hbaseResult.getString(WebRTCConstant.COLUMN_V_CALL_RELTYPE));
			wrapper.setMediaType(hbaseResult.getString(WebRTCConstant.COLUMN_V_MEDIA_TYPE));
			wrapper.setLatitude(hbaseResult.getStringAsDouble(WebRTCConstant.COLUMN_LAT));
			wrapper.setLongitude(hbaseResult.getStringAsDouble(WebRTCConstant.COLUMN_LONG));
			WebRTCUtils.setPacketLossByMediaType(hbaseResult, mediaType, wrapper);
			WebRTCUtils.setDataByMediaType(hbaseResult, mediaType, wrapper, previousWrapper);
			String callEvent = hbaseResult.getString(WebRTCConstant.COLUMN_V_EVENT);

			wrapper.setNetworkType(networkType);
			if (networkType != null && networkType.equalsIgnoreCase(WebRTCConstant.TYPE_LTE)) {
				wrapper.setRsrp(hbaseResult.getStringAsDouble(WebRTCConstant.COLUMN_AVG_RSRP));
				wrapper.setSinr(hbaseResult.getStringAsDouble(WebRTCConstant.COLUMN_AVG_SINR));
				wrapper.setCgi(hbaseResult.getStringAsInteger(WebRTCConstant.CGI));
				wrapper.setPci(hbaseResult.getStringAsInteger(WebRTCConstant.PCI));
			} else if (networkType != null && networkType.equalsIgnoreCase(WebRTCConstant.WIFI_TYPE)) {
				wrapper.setSnr(hbaseResult.getStringAsDouble(WebRTCConstant.COLUMN_SNR));
				wrapper.setRssi(hbaseResult.getStringAsDouble(WebRTCConstant.COLUMN_AVG_RSSI));
			} else if (networkType != null && networkType.equalsIgnoreCase(WebRTCConstant.TYPE_3G)) {
				wrapper.setRscp(hbaseResult.getStringAsDouble(WebRTCConstant.COLUMN_RSCP));
				wrapper.setPsc(hbaseResult.getStringAsInteger(WebRTCConstant.PSC));
				wrapper.setCellId(hbaseResult.getStringAsInteger(WebRTCConstant.CELL_ID));
			} else if (networkType != null && networkType.equalsIgnoreCase(WebRTCConstant.TYPE_2G)) {
				wrapper.setRxlevel(hbaseResult.getStringAsDouble(WebRTCConstant.COLUMN_RXLEVEL));
				wrapper.setCellId(hbaseResult.getStringAsInteger(WebRTCConstant.CELL_ID));
			}
			wrapper.setvCallRelType(hbaseResult.getString(WebRTCConstant.COLUMN_V_CALL_RELTYPE));

			wrapper.setrFactor(hbaseResult.getStringAsDouble(WebRTCConstant.RFACTOR));
			wrapper.setMos(hbaseResult.getStringAsDouble(WebRTCConstant.MOS));
			wrapper.setAvgPcktlost(hbaseResult.getStringAsDouble(WebRTCConstant.AVG_PKTLOSS));
			wrapper.setAvgRtt(hbaseResult.getStringAsDouble(WebRTCConstant.AVG_RTT));
			callEvent = getHandOver(wrapper, previousWrapper,callEvent);
			setCallEvent(wrapper, timestamp, callEvent);
		}
		return wrapper;
	}


	private String getHandOver(WebRTCCallDataWrapper wrapper, WebRTCCallDataWrapper previousWrapper,String callEvent) {

		if(callEvent != null && callEvent.contains(WebRTCConstant.HANDOVER)){
			callEvent = null;
		}
		if(previousWrapper != null && wrapper!= null && previousWrapper.getNetworkType() != null && wrapper.getNetworkType() != null) {
			if(previousWrapper.getNetworkType().equalsIgnoreCase(WebRTCConstant.TYPE_LTE)) {

				if(!previousWrapper.getPci().equals(wrapper.getPci())) {
					callEvent = WebRTCConstant.HANDOVER;
				}
			}
			else if(previousWrapper.getNetworkType().equalsIgnoreCase(WebRTCConstant.TYPE_3G)) {
				if(!previousWrapper.getPsc().equals(wrapper.getPsc())) {
					callEvent = WebRTCConstant.HANDOVER;
				}
			}
			else if(previousWrapper.getNetworkType().equalsIgnoreCase(WebRTCConstant.TYPE_2G)) {
				if(!previousWrapper.getCellId().equals(wrapper.getCellId())) {
					callEvent = WebRTCConstant.HANDOVER;
				}
			}
		}
		return callEvent;
		
	}

	private void setCallEvent(WebRTCCallDataWrapper wrapper, Long timestamp, String callEvent) {
		if (callEvent != null && callEvent.equalsIgnoreCase(WebRTCConstant.HANDOVER)) {
			Map<String, Object> hashMap = new HashMap<>();
			hashMap.put("event", callEvent);
			hashMap.put("timestamp", timestamp);
			wrapper.setCallEvent(new Gson().toJson(Arrays.asList(hashMap)).replaceAll(ForesightConstants.COMMA, ForesightConstants.UNDERSCORE));
		}
		else {
			wrapper.setCallEvent(callEvent);
		}
	}
	
	public static void main(String[] args) {
		String event = "handover";
		String timeStamp = "12345";
		Map<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("event", event);
		hashMap.put("timestamp", timeStamp);
		System.out.println(new Gson().toJson(Arrays.asList(hashMap)).replaceAll(ForesightConstants.COMMA, ForesightConstants.UNDERSCORE));
	}

	private String getRowKeyForViberCallData(String deviceId, Long timestamp) {
		StringBuilder rowKeyBuilder = new StringBuilder();
		if (deviceId != null) {
			rowKeyBuilder.append(StringUtils.reverse(deviceId.substring(ForesightConstants.TWO)));
		}
		if (timestamp != null) {
			rowKeyBuilder.append(timestamp);
		}
		return rowKeyBuilder.toString();
	}

	@Override
	public HBaseResult getResultByRowKey(String rowKey, String tableName, byte[] columnFamily) throws IOException {
		Get get = new Get(Bytes.toBytes(rowKey));
		return super.getResult(get, tableName, columnFamily);
	}

}

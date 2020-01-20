package com.inn.foresight.module.nv.dashboard.wifi.dao.impl;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.plexus.util.ExceptionUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

import com.inn.commons.hadoop.hbase.HBaseResult;
import com.inn.foresight.core.generic.dao.impl.hbase.AbstractHBaseDao;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.maplayer.utils.GenericLayerUtil;
import com.inn.foresight.module.nv.NVConstant;
import com.inn.foresight.module.nv.dashboard.wifi.dao.IWIFIAPDetailHbaseDao;
import com.inn.foresight.module.nv.dashboard.wifi.utils.constants.WIFIPerformanceConstants;
import com.inn.foresight.module.nv.dashboard.wifi.wrapper.WIFIAPDashboardWrapper;

@Service("WIFIAPDetailHbaseDaoImpl")
public class WIFIAPDetailHbaseDaoImpl extends AbstractHBaseDao implements IWIFIAPDetailHbaseDao {

	private static Logger logger = LogManager.getLogger(WIFIAPDetailHbaseDaoImpl.class);

	public WIFIAPDetailHbaseDaoImpl() {
		super();
	}

	@Override
	public WIFIAPDashboardWrapper getPerformanceDataFromHbase(Integer floorId, String macAddress, String startDate,
			String hour, Boolean isFloorWiseAPData) {
		String rowKey = getRowKey(floorId, macAddress, startDate);

		Get get = new Get(Bytes.toBytes(rowKey));

		if (hour != null) {
			setColumnsForHbaseHourly(get);
		} else {
			setColumnsForHbaseDaily(get, isFloorWiseAPData);
		}

		logger.info("Going to get Result for rowKey {} ", rowKey);

		try {
			HBaseResult result = super.getResultByPool(get, WIFIPerformanceConstants.WIFI_PERFORMANCE_HBASE_TABLE,
					Bytes.toBytes("r"));
			if (result != null) {
				WIFIAPDashboardWrapper wifiDashboardWrapper = new WIFIAPDashboardWrapper();
				if (macAddress != null) {
					wifiDashboardWrapper.setMacAddress(macAddress);
				}
				if (hour != null) {
					setResultInWrapperHourly(result, wifiDashboardWrapper, hour);
				} else {
					setResultInWrapperDaily(result, wifiDashboardWrapper, isFloorWiseAPData);
				}
				return wifiDashboardWrapper;
			}
		} catch (IOException e) {
			logger.info(ForesightConstants.LOG_EXCEPTION, Utils.getStackTrace(e));
		}
		return null;
	}

	private void setResultInWrapperHourly(HBaseResult result, WIFIAPDashboardWrapper wifiDashboardWrapper,
			String hour) {

		wifiDashboardWrapper.setRssi(
				extractDataFromJson(result.getString(WIFIPerformanceConstants.RSSI_HOURLY_JSON_COLUMN), hour));
		wifiDashboardWrapper.setSnr(
				extractDataFromJson(result.getString(WIFIPerformanceConstants.SNR_HOURLY_JSON_COLUMN), hour));
		
		
		wifiDashboardWrapper.setDl(
				extractDataFromJson(result.getString(WIFIPerformanceConstants.DL_HOURLY_JSON_COLUMN), hour));
		wifiDashboardWrapper.setUl(
				extractDataFromJson(result.getString(WIFIPerformanceConstants.UL_HOURLY_JSON_COLUMN), hour));
		wifiDashboardWrapper.setRssiPerformance(
				extractDataFromJson(result.getString(WIFIPerformanceConstants.RSSIP_HOURLY_JSON_COLUMN), hour));
		wifiDashboardWrapper.setSnrPerformance(
				extractDataFromJson(result.getString(WIFIPerformanceConstants.SNRP_HOURLY_JSON_COLUMN), hour));
		wifiDashboardWrapper.setTtl(
				extractDataFromJson(result.getString(WIFIPerformanceConstants.TTL_HOURLY_JSON_COLUMN), hour));

		wifiDashboardWrapper.setLatency(
				extractDataFromJson(result.getString(WIFIPerformanceConstants.LATENCY_HOURLY_JSON_COLUMN), hour));
		wifiDashboardWrapper.setPacketLoss(
				extractDataFromJson(result.getString(WIFIPerformanceConstants.PL_HOURLY_JSON_COLUMN), hour));
		wifiDashboardWrapper.setBufferTime(extractDataFromJson(
				result.getString(WIFIPerformanceConstants.BUFFERTIME_HOURLY_JSON_COLUMN), hour));
		
		wifiDashboardWrapper.setDns(extractDataFromJson(
				result.getString(WIFIPerformanceConstants.DNS_HOURLY_JSON_COLUMN), hour));
		

	}

	public static String extractDataFromJson(String json, String key) {
		try {
			if (json != null && !json.isEmpty()) {
				JSONParser parser = new JSONParser();
				JSONObject jsonObject = (JSONObject) parser.parse(json);
				if (jsonObject.containsKey(key)) {
					return jsonObject.get(key).toString();
				}
			}
		} catch (Exception e) {
			logger.error(ForesightConstants.LOG_EXCEPTION, ExceptionUtils.getStackTrace(e));

		}
		return null;

	}

	private void setColumnsForHbaseHourly(Get get) {

		get.addColumn(Bytes.toBytes(WIFIPerformanceConstants.COLUMN_FAMILY),
				Bytes.toBytes(WIFIPerformanceConstants.RSSI_HOURLY_JSON_COLUMN));
		get.addColumn(Bytes.toBytes(WIFIPerformanceConstants.COLUMN_FAMILY),
				Bytes.toBytes(WIFIPerformanceConstants.SNR_HOURLY_JSON_COLUMN));
		get.addColumn(Bytes.toBytes(WIFIPerformanceConstants.COLUMN_FAMILY),
				Bytes.toBytes(WIFIPerformanceConstants.TTL_HOURLY_JSON_COLUMN));
		get.addColumn(Bytes.toBytes(WIFIPerformanceConstants.COLUMN_FAMILY),
				Bytes.toBytes(WIFIPerformanceConstants.DNS_HOURLY_JSON_COLUMN));
		get.addColumn(Bytes.toBytes(WIFIPerformanceConstants.COLUMN_FAMILY),
				Bytes.toBytes(WIFIPerformanceConstants.PL_HOURLY_JSON_COLUMN));
		get.addColumn(Bytes.toBytes(WIFIPerformanceConstants.COLUMN_FAMILY),
				Bytes.toBytes(WIFIPerformanceConstants.LATENCY_HOURLY_JSON_COLUMN));

		get.addColumn(Bytes.toBytes(WIFIPerformanceConstants.COLUMN_FAMILY),
				Bytes.toBytes(WIFIPerformanceConstants.RSSIP_HOURLY_JSON_COLUMN));
		get.addColumn(Bytes.toBytes(WIFIPerformanceConstants.COLUMN_FAMILY),
				Bytes.toBytes(WIFIPerformanceConstants.SNRP_HOURLY_JSON_COLUMN));
		get.addColumn(Bytes.toBytes(WIFIPerformanceConstants.COLUMN_FAMILY),
				Bytes.toBytes(WIFIPerformanceConstants.UL_HOURLY_JSON_COLUMN));

		get.addColumn(Bytes.toBytes(WIFIPerformanceConstants.COLUMN_FAMILY),
				Bytes.toBytes(WIFIPerformanceConstants.DL_HOURLY_JSON_COLUMN));

		get.addColumn(Bytes.toBytes(WIFIPerformanceConstants.COLUMN_FAMILY),
				Bytes.toBytes(WIFIPerformanceConstants.BUFFERTIME_HOURLY_JSON_COLUMN));

	}

	private void setColumnsForHbaseDaily(Get get, Boolean isFloorWiseAPData) {

		if (!isFloorWiseAPData) {
			get.addColumn(Bytes.toBytes(WIFIPerformanceConstants.COLUMN_FAMILY),
					Bytes.toBytes(WIFIPerformanceConstants.RSSI_JSON_COLUMN));
			get.addColumn(Bytes.toBytes(WIFIPerformanceConstants.COLUMN_FAMILY),
					Bytes.toBytes(WIFIPerformanceConstants.SNR_JSON_COLUMN));
			get.addColumn(Bytes.toBytes(WIFIPerformanceConstants.COLUMN_FAMILY),
					Bytes.toBytes(WIFIPerformanceConstants.DL_JSON_COLUMN));
			get.addColumn(Bytes.toBytes(WIFIPerformanceConstants.COLUMN_FAMILY),
					Bytes.toBytes(WIFIPerformanceConstants.UL_JSON_COLUMN));
			get.addColumn(Bytes.toBytes(WIFIPerformanceConstants.COLUMN_FAMILY),
					Bytes.toBytes(WIFIPerformanceConstants.TTL_JSON_COLUMN));
			get.addColumn(Bytes.toBytes(WIFIPerformanceConstants.COLUMN_FAMILY),
					Bytes.toBytes(WIFIPerformanceConstants.LATENCY_JSON_COLUMN));
			get.addColumn(Bytes.toBytes(WIFIPerformanceConstants.COLUMN_FAMILY),
					Bytes.toBytes(WIFIPerformanceConstants.DNS_JSON_COLUMN));
			get.addColumn(Bytes.toBytes(WIFIPerformanceConstants.COLUMN_FAMILY),
					Bytes.toBytes(WIFIPerformanceConstants.BUFFERTIME_JSON_COLUMN));
			get.addColumn(Bytes.toBytes(WIFIPerformanceConstants.COLUMN_FAMILY),
					Bytes.toBytes(WIFIPerformanceConstants.PACKETLOSS_JSON_COLUMN));
			get.addColumn(Bytes.toBytes(WIFIPerformanceConstants.COLUMN_FAMILY),
					Bytes.toBytes(WIFIPerformanceConstants.AP_COUNT_COLUMN));
			get.addColumn(Bytes.toBytes(WIFIPerformanceConstants.COLUMN_FAMILY),
					Bytes.toBytes(WIFIPerformanceConstants.AP_DISTRIBUTION_COLUMN));
			get.addColumn(Bytes.toBytes(WIFIPerformanceConstants.COLUMN_FAMILY),
					Bytes.toBytes(WIFIPerformanceConstants.RSSI_PERFORMANCE_COLUMN));
			get.addColumn(Bytes.toBytes(WIFIPerformanceConstants.COLUMN_FAMILY),
					Bytes.toBytes(WIFIPerformanceConstants.SNR_PERFORMANCE_COLUMN));
			get.addColumn(Bytes.toBytes(WIFIPerformanceConstants.COLUMN_FAMILY),
					Bytes.toBytes(WIFIPerformanceConstants.HOURLY_USER_DISTRIBUTION_COLUMN));

			get.addColumn(Bytes.toBytes(WIFIPerformanceConstants.COLUMN_FAMILY),
					Bytes.toBytes(WIFIPerformanceConstants.HOURLY_SAMPLE_DISTRIBUTION_COLUMN));
			
			get.addColumn(Bytes.toBytes(WIFIPerformanceConstants.COLUMN_FAMILY),
					Bytes.toBytes(WIFIPerformanceConstants.DL_SPEED_JSON_COLUMN));
			

		}
		get.addColumn(Bytes.toBytes(WIFIPerformanceConstants.COLUMN_FAMILY),
				Bytes.toBytes(WIFIPerformanceConstants.PERFORMANCE_COLUMN));
		get.addColumn(Bytes.toBytes(WIFIPerformanceConstants.COLUMN_FAMILY),
				Bytes.toBytes(WIFIPerformanceConstants.TOTAL_SAMPLES_COLUMN));
		get.addColumn(Bytes.toBytes(WIFIPerformanceConstants.COLUMN_FAMILY),
				Bytes.toBytes(WIFIPerformanceConstants.TOTAL_USERS_COLUMN));

	}

	private String getRowKey(Integer floorId, String macAddress, String startDate) {
		if (macAddress != null) {
			String crcMacAdd = GenericLayerUtil.getPaddedCrc(macAddress);
			String floorPadding = StringUtils.leftPad(floorId.toString(), NVConstant.EIGHT, ForesightConstants.ZERO_IN_STRING);
			return floorPadding.concat(crcMacAdd.concat(startDate));
		} else {
			return StringUtils.leftPad(floorId.toString(), NVConstant.EIGHT, ForesightConstants.ZERO_IN_STRING).concat(startDate);
		}
	}

	private void setResultInWrapperDaily(HBaseResult result, WIFIAPDashboardWrapper wifiDashboardWrapper,
			Boolean isFloorWiseAPData) {

		if (!isFloorWiseAPData) {
			wifiDashboardWrapper.setRssi(result.getString(WIFIPerformanceConstants.RSSI_JSON_COLUMN));
			wifiDashboardWrapper.setSnr(result.getString(WIFIPerformanceConstants.SNR_JSON_COLUMN));
			wifiDashboardWrapper.setDl(result.getString(WIFIPerformanceConstants.DL_JSON_COLUMN));
			wifiDashboardWrapper.setUl(result.getString(WIFIPerformanceConstants.UL_JSON_COLUMN));
			wifiDashboardWrapper.setTtl(result.getString(WIFIPerformanceConstants.TTL_JSON_COLUMN));
			wifiDashboardWrapper.setDns(result.getString(WIFIPerformanceConstants.DNS_JSON_COLUMN));
			wifiDashboardWrapper.setBufferTime(result.getString(WIFIPerformanceConstants.BUFFERTIME_JSON_COLUMN));
			wifiDashboardWrapper.setPacketLoss(result.getString(WIFIPerformanceConstants.PACKETLOSS_JSON_COLUMN));
			wifiDashboardWrapper.setLatency(result.getString(WIFIPerformanceConstants.LATENCY_JSON_COLUMN));
			wifiDashboardWrapper.setApCount(result.getStringAsLong(WIFIPerformanceConstants.AP_COUNT_COLUMN));
			wifiDashboardWrapper.setApDist(result.getString(WIFIPerformanceConstants.AP_DISTRIBUTION_COLUMN));
			wifiDashboardWrapper
					.setRssiPerformance(result.getString(WIFIPerformanceConstants.RSSI_PERFORMANCE_COLUMN));
			wifiDashboardWrapper
					.setSnrPerformance(result.getString(WIFIPerformanceConstants.SNR_PERFORMANCE_COLUMN));
			wifiDashboardWrapper.setHourlyUserDistribution(
					result.getString(WIFIPerformanceConstants.HOURLY_USER_DISTRIBUTION_COLUMN));

			wifiDashboardWrapper.setHourlySampleDistribution(
					result.getString(WIFIPerformanceConstants.HOURLY_SAMPLE_DISTRIBUTION_COLUMN));
			
			wifiDashboardWrapper.setDlSpeed(
					result.getString(WIFIPerformanceConstants.DL_SPEED_JSON_COLUMN));
			
			

		}

		wifiDashboardWrapper
				.setTotalSamples(result.getStringAsLong(WIFIPerformanceConstants.TOTAL_SAMPLES_COLUMN));
		wifiDashboardWrapper.setPerformance(result.getString(WIFIPerformanceConstants.PERFORMANCE_COLUMN));
		wifiDashboardWrapper.setTotalUser(result.getStringAsLong(WIFIPerformanceConstants.TOTAL_USERS_COLUMN));
	}

}

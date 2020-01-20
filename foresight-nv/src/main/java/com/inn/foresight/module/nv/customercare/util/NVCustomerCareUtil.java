package com.inn.foresight.module.nv.customercare.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.hadoop.hbase.HBaseResult;
import com.inn.foresight.core.generic.utils.ConfigEnum;
import com.inn.foresight.module.nv.app.model.DeviceInfo;
import com.inn.foresight.module.nv.customercare.constant.NVCustomerCareConstant;
import com.inn.foresight.module.nv.customercare.wrapper.NVCustomerCareDataWrapper;


public class NVCustomerCareUtil {
	private NVCustomerCareUtil() {
		
	}
	/** The logger. */
	private static final Logger logger = LogManager.getLogger(NVCustomerCareUtil.class);

	public static String getDeviceIdString(List<DeviceInfo> deviceList) {
		return getCommaSeparatedStringFromList(deviceList	.parallelStream()
															.map(DeviceInfo::getDeviceId)
															.collect(Collectors.toList()));

	}

	private static String getCommaSeparatedStringFromList(List<String> deviceIdList) {
		return String.join(NVCustomerCareConstant.COMMA_SEPARATOR, deviceIdList);
	}

	public static String getDropwizardUrl(String path) {

		return ConfigUtils.getString(ConfigEnum.MICRO_SERVICE_BASE_URL.getValue())
				+ ConfigUtils	.getString(ConfigEnum.DROPWIZARD_URL_NV_DATA, Boolean.TRUE)
							.concat(path);
	}

	public static String getQueryString(String type, String deviceId,  Long minTimeStamp,
			Long maxTimeStmap, String lastRowKey, Long noOfRecords, String rowKey) {
		String queryString = null;
		if (type != null) {
			queryString = "type=" + type;
		}
		if (deviceId != null) {
			queryString = queryString + "&deviceIds=" + deviceId;
		}
		if (minTimeStamp != null) {
			queryString = queryString + "&minTimeStamp=" + minTimeStamp;
		}
		if (maxTimeStmap != null) {
			queryString = queryString + "&maxTimeStmap=" + maxTimeStmap;
		}
		if (lastRowKey != null) {
			queryString = queryString + "&lastRowKey=" + lastRowKey;
		}
		if (noOfRecords != null) {
			queryString = queryString + "&noOfRecords=" + noOfRecords;
		}
		if (rowKey != null) {
			queryString = queryString + "&rowKey=" + rowKey;
		}
		return queryString;

	}

	public static Map<String, String> setDeviceIdImeiMap(List<DeviceInfo> deviceList) {
		return deviceList	.parallelStream()
							.collect(Collectors.toMap(DeviceInfo::getDeviceId, DeviceInfo::getImei));
	}

/**
	 * Gets the row key list.
	 *
	 * @param rowkeylist
	 *            the rowkeylist
	 * @param columnFamily
	 *            the column family
	 * @param columns
	 *            the columns
	 * @return the row key list
	 */
	public static List<Get> getRowKeyList(List<String> rowkeylist, byte[] columnFamily, List<String> columns) {
		List<Get> getList = new ArrayList<>();
		for (String rowkey : rowkeylist) {
			Get get = new Get(Bytes.toBytes(rowkey));
			for (String column : columns) {
				get.addColumn(columnFamily, Bytes.toBytes(column));
			}
			getList.add(get);
		}
		return getList;
	}

	/**
	 * Gets the active data from result list.
	 *
	 * @param resultList
	 *            the result list
	 * @return the active data from result list
	 */
	public static List<NVCustomerCareDataWrapper> getActiveDataFromResultList(List<HBaseResult> resultList) {
		List<NVCustomerCareDataWrapper> activePassiveDataWrappers = new ArrayList<>();
		if (resultList != null && !resultList.isEmpty()) {
			activePassiveDataWrappers = resultList	.stream()
													.filter(Objects::nonNull)
													.map(result -> getActiveDataToWrapper(result))
													.collect(Collectors.toList());
		}
		return activePassiveDataWrappers;
	}

	/**
	 * Gets the passive data from result list.
	 *
	 * @param resultList
	 *            the result list
	 * @return the passive data from result list
	 */
	public static List<NVCustomerCareDataWrapper> getPassiveDataFromResultList(List<HBaseResult> resultList) {
		List<NVCustomerCareDataWrapper> activePassiveDataWrappers = new ArrayList<>();
		if (resultList != null && !resultList.isEmpty()) {
			activePassiveDataWrappers = resultList	.stream()
													.filter(Objects::nonNull)
													.map(result -> getPassiveDataToWrapper(result))
													.collect(Collectors.toList());
		}
		return activePassiveDataWrappers;
	}

	/**
	 * Gets the passive data to wrapper.
	 *
	 * @param result
	 *            the result
	 * @return the passive data to wrapper
	 */
	public static NVCustomerCareDataWrapper getPassiveDataToWrapper(HBaseResult result) {
		NVCustomerCareDataWrapper activePassiveDataWrapper = new NVCustomerCareDataWrapper();
		setActivePassiveCommonFields(activePassiveDataWrapper, result);

		activePassiveDataWrapper.set_2Grxlevel(getIntValue(NVCustomerCareConstant.COLUMN_2G_RX_LEVEL, result));
		activePassiveDataWrapper.set_2gRxQuality(getIntValue(NVCustomerCareConstant.COLUMN_2G_RX_QUALITY, result));
		activePassiveDataWrapper.set_3gEcno(getIntValue(NVCustomerCareConstant.COLUMN_3G_ECNO, result));
		activePassiveDataWrapper.set_3gRscp(getIntValue(NVCustomerCareConstant.COLUMN_3G_RSCP, result));
		activePassiveDataWrapper.setAltitude(getDoubleValue(NVCustomerCareConstant.COLUMN_ALTITUDE, result));
		activePassiveDataWrapper.setAutoDateTime(getBooleanValue(NVCustomerCareConstant.COLUMN_AUTO_DATE_TIME, result));
		activePassiveDataWrapper.setBarometerList(getStringValue(NVCustomerCareConstant.COLUMN_BAROMETER_LIST, result));
		activePassiveDataWrapper.setBatteryTemp(getStringValue(NVCustomerCareConstant.COLUMN_BATTERY_TEMP, result));
		activePassiveDataWrapper.setBatteryVoltage(
				getStringValue(NVCustomerCareConstant.COLUMN_BATTERY_VOLTAGE, result));
		activePassiveDataWrapper.setCallDuration(getStringValue(NVCustomerCareConstant.COLUMN_CALL_DURATION, result));
		activePassiveDataWrapper.setCallEstStatus(
				getStringValue(NVCustomerCareConstant.COLUMN_CALL_EST_STATUS, result));
		activePassiveDataWrapper.setCallReleaseType(
				getStringValue(NVCustomerCareConstant.COLUMN_CALL_RELEASE_TYPE, result));
		activePassiveDataWrapper.setCallType(getStringValue(NVCustomerCareConstant.COLUMN_CALL_TYPE, result));

		activePassiveDataWrapper.setCharger(getStringValue(NVCustomerCareConstant.COLUMN_CHARGER, result));
		activePassiveDataWrapper.setCharging(getBooleanValue(NVCustomerCareConstant.COLUMN_CHARGING, result));
		activePassiveDataWrapper.setChargingType(getStringValue(NVCustomerCareConstant.COLUMN_CHARGING_TYPE, result));
		activePassiveDataWrapper.setChipSet(getStringValue(NVCustomerCareConstant.COLUMN_CHIP_SET, result));
		activePassiveDataWrapper.setCollectionType(
				getStringValue(NVCustomerCareConstant.COLUMN_COLLECTION_TYPE, result));
		activePassiveDataWrapper.setConnectionType(
				getStringValue(NVCustomerCareConstant.COLUMN_CONNECTION_TYPE, result));
		activePassiveDataWrapper.setCoreArch(getStringValue(NVCustomerCareConstant.COLUMN_CORE_ARCH, result));
		activePassiveDataWrapper.setFingerPrint(getStringValue(NVCustomerCareConstant.COLUMN_FINGERPRINT, result));
		activePassiveDataWrapper.setFrequency(getIntValue(NVCustomerCareConstant.COLUMN_FREQUENCY, result));
		activePassiveDataWrapper.setGpsEnabled(getBooleanValue(NVCustomerCareConstant.COLUMN_GPS_ENABLED, result));
		activePassiveDataWrapper.setIpV4(getStringValue(NVCustomerCareConstant.COLUMN_IPV4, result));
		activePassiveDataWrapper.setIpV6(getStringValue(NVCustomerCareConstant.COLUMN_IPV6, result));
		activePassiveDataWrapper.setIsDciEnabled(getBooleanValue(NVCustomerCareConstant.COLUMN_IS_DCI_ENABLED, result));
		activePassiveDataWrapper.setIsEnterprise(getBooleanValue(NVCustomerCareConstant.COLUMN_IS_ENTERPRISE, result));
		activePassiveDataWrapper.setIsgwEnabled(getBooleanValue(NVCustomerCareConstant.COLUMN_ISGW_ENABLED, result));
		activePassiveDataWrapper.setLinkSpeed(getDoubleValue(NVCustomerCareConstant.COLUMN_LINK_SPEED, result));
		activePassiveDataWrapper.setLocAccuracy(getDoubleValue(NVCustomerCareConstant.COLUMN_LOC_ACCURACY, result));
		activePassiveDataWrapper.setMacAddress(getStringValue(NVCustomerCareConstant.COLUMN_MAC_ADDRESS, result));
		activePassiveDataWrapper.setNetworkSubtype(
				getStringValue(NVCustomerCareConstant.COLUMN_NETWORK_SUB_TYPE, result));
		activePassiveDataWrapper.setNetworkType(getStringValue(NVCustomerCareConstant.COLUMN_NETWORK_TYPE, result));
		activePassiveDataWrapper.setNetworkTypeWhenWifi(
				getStringValue(NVCustomerCareConstant.COLUMN_NETWORK_TYPE_WHEN_WIFI, result));
		activePassiveDataWrapper.setNvModule(getStringValue(NVCustomerCareConstant.COLUMN_NV_MODULE, result));
		activePassiveDataWrapper.setOperatorName(getStringValue(NVCustomerCareConstant.COLUMN_OPERATOR_NAME, result));
		activePassiveDataWrapper.setOperatorNameWhenWiFi(
				getStringValue(NVCustomerCareConstant.COLUMN_CLIENT_OP_NAME_WHEN_WIFI, result));
		activePassiveDataWrapper.setProfileId(getStringValue(NVCustomerCareConstant.COLUMN_PROFILE_ID, result));
		activePassiveDataWrapper.setProfileStatus(getStringValue(NVCustomerCareConstant.COLUMN_PROFILE_STATUS, result));
		activePassiveDataWrapper.setRoamingStatus(getStringValue(NVCustomerCareConstant.COLUMN_ROAMING_STATUS, result));
		activePassiveDataWrapper.setRssi(getIntValue(NVCustomerCareConstant.COLUMN_RSSI, result));
		activePassiveDataWrapper.setSdkVersion(getStringValue(NVCustomerCareConstant.COLUMN_SDK_VERSION, result));
		activePassiveDataWrapper.setSerialNo(getStringValue(NVCustomerCareConstant.COLUMN_SERIAL_NO, result));
		activePassiveDataWrapper.setSim1(getStringValue(NVCustomerCareConstant.COLUMN_SIM1, result));
		activePassiveDataWrapper.setSim2(getStringValue(NVCustomerCareConstant.COLUMN_SIM2, result));
		activePassiveDataWrapper.setSimType(getStringValue(NVCustomerCareConstant.COLUMN_SIM_TYPE, result));
		activePassiveDataWrapper.setSnr(getIntValue(NVCustomerCareConstant.COLUMN_SNR, result));
		activePassiveDataWrapper.setSocModel(getStringValue(NVCustomerCareConstant.COLUMN_SOC_MODEL, result));
		activePassiveDataWrapper.setSsid(getStringValue(NVCustomerCareConstant.COLUMN_SSID, result));
		activePassiveDataWrapper.setVoiceCellId(getIntValue(NVCustomerCareConstant.COLUMN_VOICE_CELL_ID, result));
		activePassiveDataWrapper.setVoiceEcno(getIntValue(NVCustomerCareConstant.COLUMN_VOICE_ECNO, result));
		activePassiveDataWrapper.setVoiceLac(getIntValue(NVCustomerCareConstant.COLUMN_VOICE_LAC, result));
		activePassiveDataWrapper.setVoiceMcc(getIntValue(NVCustomerCareConstant.COLUMN_VOICE_MCC, result));
		activePassiveDataWrapper.setVoiceMnc(getIntValue(NVCustomerCareConstant.COLUMN_VOICE_MNC, result));
		activePassiveDataWrapper.setVoiceNetworkType(
				getStringValue(NVCustomerCareConstant.COLUMN_VOICE_NW_TYPE, result));
		activePassiveDataWrapper.setVoiceOperatorName(
				getStringValue(NVCustomerCareConstant.COLUMN_VOICE_OPERATOR_NAME, result));
		activePassiveDataWrapper.setVoicePci(getIntValue(NVCustomerCareConstant.COLUMN_VOICE_PCI, result));
		activePassiveDataWrapper.setVoicePsc(getIntValue(NVCustomerCareConstant.COLUMN_VOICE_PSC, result));
		activePassiveDataWrapper.setVoiceRscp(getIntValue(NVCustomerCareConstant.COLUMN_VOICE_RSCP, result));
		activePassiveDataWrapper.setVoiceRsrp(getIntValue(NVCustomerCareConstant.COLUMN_VOICE_RSRP, result));
		activePassiveDataWrapper.setVoiceRssi(getIntValue(NVCustomerCareConstant.COLUMN_VOICE_RSSI, result));
		activePassiveDataWrapper.setVoiceRsrq(getIntValue(NVCustomerCareConstant.COLUMN_VOICE_RSRQ, result));
		activePassiveDataWrapper.setVoiceRxLevel(getIntValue(NVCustomerCareConstant.COLUMN_VOICE_RX_LEVEL, result));
		activePassiveDataWrapper.setVoiceRxQuality(getIntValue(NVCustomerCareConstant.COLUMN_VOICE_RX_QUALITY, result));
		activePassiveDataWrapper.setVoiceSinr(getDoubleValue(NVCustomerCareConstant.COLUMN_VOICE_SINR, result));
		activePassiveDataWrapper.setVoiceTac(getIntValue(NVCustomerCareConstant.COLUMN_VOICE_TAC, result));
		return activePassiveDataWrapper;
	}

	/**
	 * Gets the active data to wrapper.
	 *
	 * @param result
	 *            the result
	 * @return the active data to wrapper
	 */
	public static NVCustomerCareDataWrapper getActiveDataToWrapper(HBaseResult result) {
		NVCustomerCareDataWrapper activePassiveDataWrapper = new NVCustomerCareDataWrapper();
		setActivePassiveCommonFields(activePassiveDataWrapper, result);
		
		activePassiveDataWrapper.setChipSet(getStringValue(NVCustomerCareConstant.COLUMN_ACTIVE_CHIP_SET, result));

		activePassiveDataWrapper.setGpsStatus(getStringValue(NVCustomerCareConstant.COLUMN_GPS_STATUS, result));
		activePassiveDataWrapper.setDataSimDetail(
				getStringValue(NVCustomerCareConstant.COLUMN_DATA_SIM_DETAIL, result));
		activePassiveDataWrapper.setVoiceSimDetail(
				getStringValue(NVCustomerCareConstant.COLUMN_VOICE_SIM_DETAIL, result));
		activePassiveDataWrapper.setDeviceSerialNo(
				getStringValue(NVCustomerCareConstant.COLUMN_DEVICE_SERIAL_NO, result));
		activePassiveDataWrapper.setActiveTestType(
				getStringValue(NVCustomerCareConstant.COLUMN_ACTIVE_TEST_TYPE, result));
		activePassiveDataWrapper.setType(getStringValue(NVCustomerCareConstant.COLUMN_TYPE, result));
		activePassiveDataWrapper.setUserid(getIntValue(NVCustomerCareConstant.COLUMN_USER_ID, result));
		activePassiveDataWrapper.setStarRating(getStringValue(NVCustomerCareConstant.COLUMN_STAR_RATING, result));
		activePassiveDataWrapper.setMaxDlRate(getDoubleValue(NVCustomerCareConstant.COLUMN_MAX_DL_RATE, result));
		activePassiveDataWrapper.setMaxEcNo(getIntValue(NVCustomerCareConstant.COLUMN_MAX_ECNO, result));
		activePassiveDataWrapper.setMaxLatency(getDoubleValue(NVCustomerCareConstant.COLUMN_MAX_LATENCY, result));
		activePassiveDataWrapper.setMaxRscp(getIntValue(NVCustomerCareConstant.COLUMN_MAX_RSCP, result));
		activePassiveDataWrapper.setMaxRsrp(getIntValue(NVCustomerCareConstant.COLUMN_MAX_RSRP, result));
		activePassiveDataWrapper.setMaxRsrq(getIntValue(NVCustomerCareConstant.COLUMN_MAX_RSRQ, result));
		activePassiveDataWrapper.setMaxRssi(getIntValue(NVCustomerCareConstant.COLUMN_MAX_RSSI, result));
		activePassiveDataWrapper.setMaxRxLevel(getIntValue(NVCustomerCareConstant.COLUMN_MAX_RX_LEVEL, result));
		activePassiveDataWrapper.setMaxRxquality(getIntValue(NVCustomerCareConstant.COLUMN_MAX_RX_QUALITY, result));
		activePassiveDataWrapper.setMaxSinr(getDoubleValue(NVCustomerCareConstant.COLUMN_MAX_SINR, result));
		activePassiveDataWrapper.setMaxSnr(getIntValue(NVCustomerCareConstant.COLUMN_MAX_SNR, result));
		activePassiveDataWrapper.setMaxUlRate(getDoubleValue(NVCustomerCareConstant.COLUMN_MAX_UL_RATE, result));
		activePassiveDataWrapper.setMinDlRate(getDoubleValue(NVCustomerCareConstant.COLUMN_MIN_DL_RATE, result));
		activePassiveDataWrapper.setMinEcNo(getIntValue(NVCustomerCareConstant.COLUMN_MIN_ECNO, result));
		activePassiveDataWrapper.setMinLatency(getDoubleValue(NVCustomerCareConstant.COLUMN_MIN_LATENCY, result));
		activePassiveDataWrapper.setMinRscp(getIntValue(NVCustomerCareConstant.COLUMN_MIN_RSCP, result));
		activePassiveDataWrapper.setMinRsrp(getIntValue(NVCustomerCareConstant.COLUMN_MIN_RSRP, result));
		activePassiveDataWrapper.setMinRsrq(getIntValue(NVCustomerCareConstant.COLUMN_MIN_RSRQ, result));
		activePassiveDataWrapper.setMinRssi(getIntValue(NVCustomerCareConstant.COLUMN_MIN_RSSI, result));
		activePassiveDataWrapper.setMinRxLevel(getIntValue(NVCustomerCareConstant.COLUMN_MIN_RX_LEVEL, result));
		activePassiveDataWrapper.setMinRxquality(getIntValue(NVCustomerCareConstant.COLUMN_MIN_RX_QUALITY, result));
		activePassiveDataWrapper.setMinSinr(getDoubleValue(NVCustomerCareConstant.COLUMN_MIN_SINR, result));
		activePassiveDataWrapper.setMinSnr(getIntValue(NVCustomerCareConstant.COLUMN_MIN_SNR, result));
		activePassiveDataWrapper.setMinUlRate(getDoubleValue(NVCustomerCareConstant.COLUMN_MIN_UL_RATE, result));
		activePassiveDataWrapper.setAvgEcNo(getIntValue(NVCustomerCareConstant.COLUMN_AVG_EC_NO, result));
		activePassiveDataWrapper.setAvgLatency(getDoubleValue(NVCustomerCareConstant.COLUMN_AVERAGE_LATENCY, result));
		activePassiveDataWrapper.setAvgRscp(getIntValue(NVCustomerCareConstant.COLUMN_AVERAGE_RSCP, result));
		activePassiveDataWrapper.setAvgRxLevel(getIntValue(NVCustomerCareConstant.COLUMN_AVERAGE_RX_LEVEL, result));
		activePassiveDataWrapper.setAvgRxquality(getIntValue(NVCustomerCareConstant.COLUMN_AVERAGE_RX_QUALITY, result));
		activePassiveDataWrapper.setAvgSnr(getIntValue(NVCustomerCareConstant.COLUMN_AVERAGE_SNR, result));
		activePassiveDataWrapper.setJitter(getDoubleValue(NVCustomerCareConstant.COLUMN_JITTER, result));
		activePassiveDataWrapper.setPcktLoss(getDoubleValue(NVCustomerCareConstant.COLUMN_PCKT_LOSS, result));
		activePassiveDataWrapper.setClientOpName(getStringValue(NVCustomerCareConstant.COLUMN_CLIENT_OP_NAME, result));
		activePassiveDataWrapper.setUrl1Time(getDoubleValue(NVCustomerCareConstant.COLUMN_URL1_TIME, result));
		activePassiveDataWrapper.setUrl2Time(getDoubleValue(NVCustomerCareConstant.COLUMN_URL2_TIME, result));
		activePassiveDataWrapper.setUrl3Time(getDoubleValue(NVCustomerCareConstant.COLUMN_URL3_TIME, result));
		activePassiveDataWrapper.setAddress(getStringValue(NVCustomerCareConstant.COLUMN_ADDRESS, result));
		activePassiveDataWrapper.setUserComment(getStringValue(NVCustomerCareConstant.COLUMN_USER_COMMENT, result));
		activePassiveDataWrapper.setChargerConnected(
				getBooleanValue(NVCustomerCareConstant.COLUMN_CHARGER_CONNECTED, result));
		activePassiveDataWrapper.setTemperature(getStringValue(NVCustomerCareConstant.COLUMN_TEMERATURE, result));
		activePassiveDataWrapper.setVoltage(getStringValue(NVCustomerCareConstant.COLUMN_VOLTAGE, result));
		activePassiveDataWrapper.setNearestCity(getStringValue(NVCustomerCareConstant.COLUMN_NEAREST_CITY, result));
		activePassiveDataWrapper.setNearestIP(getStringValue(NVCustomerCareConstant.COLUMN_NEAREST_IP, result));
		activePassiveDataWrapper.setDestPingIpAddr(
				getStringValue(NVCustomerCareConstant.COLUMN_DEST_PING_IP_ADDR, result));
		activePassiveDataWrapper.setSpeedTestFailure(
				getStringValue(NVCustomerCareConstant.COLUMN_SPEED_TEST_FAILURE, result));
		activePassiveDataWrapper.setMobileNumber(getStringValue(NVCustomerCareConstant.COLUMN_MOBILE_NUMBER, result));
		activePassiveDataWrapper.setIsManualSync(getStringValue(NVCustomerCareConstant.COLUMN_IS_MANUAL_SYNC, result));
		activePassiveDataWrapper.setEnodeBType(getStringValue(NVCustomerCareConstant.ENODE_B_TYPE, result));
		activePassiveDataWrapper.setEnodeBVendor(getStringValue(NVCustomerCareConstant.COLUMN_ENODE_B_VENDOR, result));
		activePassiveDataWrapper.setAutoDateAndTime(
				getBooleanValue(NVCustomerCareConstant.COLUMN_AUTO_DATE_AND_TIME, result));
		activePassiveDataWrapper.setArfcn(getStringValue(NVCustomerCareConstant.COLUMN_ARFCN, result));
		activePassiveDataWrapper.setEarfcn(getIntValue(NVCustomerCareConstant.COLUMN_EARFCN, result));
		activePassiveDataWrapper.setUarfcn(getStringValue(NVCustomerCareConstant.COLUMN_UARFCN, result));
		activePassiveDataWrapper.setDeviceIPV4Status(getStringValue(NVCustomerCareConstant.COLUMN_DEVICE_IPV4_STATUS, result));
		activePassiveDataWrapper.setDeviceIPV6Status(getStringValue(NVCustomerCareConstant.COLUMN_DEVICE_IPV6_STATUS, result));
		return activePassiveDataWrapper;
	}

	/**
	 * Sets the active passive common fields.
	 *
	 * @param activePassiveDataWrapper
	 *            the active passive data wrapper
	 * @param result
	 *            the result
	 */
	private static void setActivePassiveCommonFields(NVCustomerCareDataWrapper activePassiveDataWrapper,
			HBaseResult result) {

		activePassiveDataWrapper.setKey(result.getRowKey());
		activePassiveDataWrapper.setRecordType(getStringValue(NVCustomerCareConstant.COLUMN_RECORD_TYPE, result));
		activePassiveDataWrapper.setAndroidId(getStringValue(NVCustomerCareConstant.COLUMN_ANDROID_ID, result));
		activePassiveDataWrapper.setgL1(getStringValue(NVCustomerCareConstant.COLUMN_GL1, result));
		activePassiveDataWrapper.setgL2(getStringValue(NVCustomerCareConstant.COLUMN_GL2, result));
		activePassiveDataWrapper.setgL3(getStringValue(NVCustomerCareConstant.COLUMN_GL3, result));
		activePassiveDataWrapper.setgL4(getStringValue(NVCustomerCareConstant.COLUMN_GL4, result));
		activePassiveDataWrapper.setCapturedOn(getLongValue(NVCustomerCareConstant.COLUMN_CAPTURED_ON, result));
		activePassiveDataWrapper.setDate(getStringValue(NVCustomerCareConstant.COLUMN_DATE, result));
		activePassiveDataWrapper.setDeviceId(getStringValue(NVCustomerCareConstant.COLUMN_DEVICE_ID, result));
		activePassiveDataWrapper.setLatitude(getDoubleValue(NVCustomerCareConstant.COLUMN_LATITUDE, result));
		activePassiveDataWrapper.setLongitude(getDoubleValue(NVCustomerCareConstant.COLUMN_LONGITUDE, result));
		activePassiveDataWrapper.setMnc((getIntValue(NVCustomerCareConstant.COLUMN_MNC, result)));
		activePassiveDataWrapper.setMcc(getIntValue(NVCustomerCareConstant.COLUMN_MCC, result));
		activePassiveDataWrapper.setCellId(getIntValue(NVCustomerCareConstant.COLUMN_CELL_ID, result));
		activePassiveDataWrapper.setCgi(getIntValue(NVCustomerCareConstant.COLUMN_CGI, result));
		activePassiveDataWrapper.setBand(getStringValue(NVCustomerCareConstant.COLUMN_BAND, result));
		activePassiveDataWrapper.setPci(getIntValue(NVCustomerCareConstant.COLUMN_PCI, result));
		activePassiveDataWrapper.setPsc(getIntValue(NVCustomerCareConstant.COLUMN_PCI, result));
		activePassiveDataWrapper.setTac(getIntValue(NVCustomerCareConstant.COLUMN_TAC, result));
		activePassiveDataWrapper.setLac(getIntValue(NVCustomerCareConstant.COLUMN_LAC, result));
		activePassiveDataWrapper.setAvgRsrp(getIntValue(NVCustomerCareConstant.COLUMN_AVERAGE_RSRP, result));
		activePassiveDataWrapper.setAvgSinr(getDoubleValue(NVCustomerCareConstant.COLUMN_AVERAGE_SINR, result));
		activePassiveDataWrapper.setAvgRsrq(getIntValue(NVCustomerCareConstant.COLUMN_AVERAGE_RSRQ, result));
		activePassiveDataWrapper.setMake(getStringValue(NVCustomerCareConstant.COLUMN_MAKE, result));
		activePassiveDataWrapper.setModel(getStringValue(NVCustomerCareConstant.COLUMN_MODEL, result));
		activePassiveDataWrapper.setDeviceOS(getStringValue(NVCustomerCareConstant.COLUMN_DEVICE_OS, result));
		activePassiveDataWrapper.setVersionName(getStringValue(NVCustomerCareConstant.COLUMN_VERSION_NAME, result));
		activePassiveDataWrapper.setDualSimEnable(
				getBooleanValue(NVCustomerCareConstant.COLUMN_DUAL_SIM_ENABLE, result));
		activePassiveDataWrapper.setBuildNumber(getStringValue(NVCustomerCareConstant.COLUMN_BUILD_NUMBER, result));
		activePassiveDataWrapper.seteNodeB(getStringValue(NVCustomerCareConstant.COLUMN_E_NODE_B, result));
		activePassiveDataWrapper.setBssid(getStringValue(NVCustomerCareConstant.COLUMN_BSSID, result));
		activePassiveDataWrapper.setBatteryLevel(getStringValue(NVCustomerCareConstant.COLUMN_BATTERY_LEVEL, result));
		activePassiveDataWrapper.setAvgRssi(getIntValue(NVCustomerCareConstant.COLUMN_AVERAGE_RSSI, result));
		activePassiveDataWrapper.setAvgDlRate(getDoubleValue(NVCustomerCareConstant.COLUMN_AVERAGE_DL_RATE, result));
		activePassiveDataWrapper.setAvgUlRate(getDoubleValue(NVCustomerCareConstant.COLUMN_AVERAGE_UL_RATE, result));
		activePassiveDataWrapper.setBaseband(getStringValue(NVCustomerCareConstant.COLUMN_BASE_BAND, result));
		activePassiveDataWrapper.setCpuUsage(getStringValue(NVCustomerCareConstant.COLUMN_CPU_USAGE, result));
		activePassiveDataWrapper.setMemoryUsage(getStringValue(NVCustomerCareConstant.COLUMN_MEMORY_USAGE, result));

	}

	/**
	 * Gets the active column list.
	 *
	 * @return the active column list
	 */
	public static List<String> getActiveColumnList() {
		String[] activeColumns = NVCustomerCareConstant.ACTIVE_COLUMNS.split(NVCustomerCareConstant.SEPERATOR);
		return Arrays.asList(activeColumns);
	}

	/**
	 * Gets the passive column list.
	 *
	 * @return the passive column list
	 */
	public static List<String> getPassiveColumnList() {
		String[] passiveColumns = NVCustomerCareConstant.PASSIVE_COLUMNS.split(NVCustomerCareConstant.SEPERATOR);
		return Arrays.asList(passiveColumns);
	}

	/**
	 * Gets the double value.
	 *
	 * @param columnName
	 *            the column name
	 * @param result
	 *            the result
	 * @return the double value
	 */
	public static Double getDoubleValue(String columnName, HBaseResult result) {
		return result.getStringAsDouble(columnName.getBytes());
	}

	/**
	 * Gets the long value.
	 *
	 * @param columnName
	 *            the column name
	 * @param result
	 *            the result
	 * @return the long value
	 */
	public static Long getLongValue(String columnName, HBaseResult result) {
		return result.getStringAsLong(columnName.getBytes());
	}

	/**
	 * Gets the int value.
	 *
	 * @param columnName
	 *            the column name
	 * @param result
	 *            the result
	 * @return the int value
	 */
	public static Integer getIntValue(String columnName, HBaseResult result) {
		return result.getStringAsInteger(columnName.getBytes());
	}

	/**
	 * Gets the string value.
	 *
	 * @param columnName
	 *            the column name
	 * @param result
	 *            the result
	 * @return the string value
	 */
	public static String getStringValue(String columnName, HBaseResult result) {
		return result.getString(columnName.getBytes());
	}

	/**
	 * Gets the boolean value.
	 *
	 * @param columnName
	 *            the column name
	 * @param result
	 *            the result
	 * @return the boolean value
	 */
	public static Boolean getBooleanValue(String columnName, HBaseResult result) {
		return result.getStringAsBoolean(columnName.getBytes());
	}

	/**
	 * Gets the scan list.
	 *
	 * @param rowKeyPrefixList
	 *            the row key prefix list
	 * @param minStamp
	 *            the min stamp
	 * @param maxStamp
	 *            the max stamp
	 * @param lastRowKey
	 *            the last row key
	 * @param maxResultSize
	 *            the max result size
	 * @return the scan list
	 */
	public static List<Scan> getScanList(List<String> rowKeyPrefixList, Long minStamp, Long maxStamp, String lastRowKey,
			Long maxResultSize) {
		logger.info("Going to getScanList deviceIds: {}", rowKeyPrefixList);
		return rowKeyPrefixList	.stream()
								.map(rowKeyPrefix -> getScanObj(minStamp, maxStamp, lastRowKey, maxResultSize,
										rowKeyPrefix))
								.collect(Collectors.toList());

	}

	/**
	 * Gets the scan obj.
	 *
	 * @param minStamp
	 *            the min stamp
	 * @param maxStamp
	 *            the max stamp
	 * @param lastRowKey
	 *            the last row key
	 * @param maxResultSize
	 *            the max result size
	 * @param deviceIdPrefix
	 *            the device id prefix
	 * @return the scan obj
	 */
	private static Scan getScanObj(Long minStamp, Long maxStamp, String lastRowKey, Long maxResultSize,
			String deviceIdPrefix) {
		logger.info("Going to getScanObj for deviceId prefix: {}", deviceIdPrefix);
		Scan scan = new Scan();
		try {
			scan.setRowPrefixFilter(Bytes.toBytes(deviceIdPrefix));
			if (lastRowKey != null) {
				scan.withStartRow(Bytes.toBytes(lastRowKey));
			}
			if (maxResultSize != null) {
				scan.setMaxResultSize(maxResultSize);
			}
			if (minStamp != null && maxStamp != null) {
				scan.setTimeRange(minStamp, maxStamp);
			}
		} catch (IOException e) {
			logger.info("IOException in getScanList {}: ", ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			logger.error("Error in get scan object  : {}", ExceptionUtils.getStackTrace(e));
		}
		return scan;
	}

	/**
	 * Gets the row prefix.
	 *
	 * @param deviceIds
	 *            the device ids
	 * @return the row prefix
	 */
	public static List<String> getRowPrefix(List<String> deviceIds) {
		return deviceIds.stream()
						.map(deviceId -> StringUtils.reverse(deviceId.substring(3, deviceId.length())))
						.collect(Collectors.toList());
	}
}

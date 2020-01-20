package com.inn.foresight.module.nv.wpt.analytics.dao.impl.hbase;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.hadoop.hbase.HBaseResult;
import com.inn.foresight.core.generic.dao.impl.hbase.AbstractHBaseDao;
import com.inn.foresight.module.nv.wpt.analytics.constants.WPTAnalyticsConstants;
import com.inn.foresight.module.nv.wpt.analytics.constants.YouTubeHbaseConstants;
import com.inn.foresight.module.nv.wpt.analytics.dao.IYouTubeHbaseDao;
import com.inn.foresight.module.nv.wpt.analytics.utils.WPTAnalyticsUtils;
import com.inn.foresight.module.nv.wpt.analytics.utils.wrapper.YouTubeRawDataWrapper;

@Repository("YouTubeHbaseDaoImpl")
public class YouTubeHbaseDaoImpl extends AbstractHBaseDao implements IYouTubeHbaseDao{

	/** The logger. */
	private Logger logger = LogManager.getLogger(YouTubeHbaseDaoImpl.class);

	@Override
	public List<YouTubeRawDataWrapper> scanDataWithTimeRange(Long minStamp, Long maxStamp, List<Filter> filters) throws Exception {
		logger.info("Going to scan records from HBase for TimeRange : {} to {}", minStamp, maxStamp);
		List<YouTubeRawDataWrapper> wrapperList = new ArrayList<>();
		try {
			Scan scan = new Scan();
			if (filters != null && !filters.isEmpty()) {
				for(Filter filter : filters) {
					scan.setFilter(filter);
				}
			}
			scan.setTimeRange(minStamp, maxStamp);

			iterateResultByPool(scan, ConfigUtils.getString(YouTubeHbaseConstants.YOU_TUBE_TABLE_NAME),
					Bytes.toBytes(ConfigUtils.getString(YouTubeHbaseConstants.YOU_TUBE_COLUMN_FAMILY)),
					result -> wrapperList.add(setYouTubeRawDataWrapper(result)));

			logger.info(WPTAnalyticsConstants.RESULT_SIZE_LOGGER, wrapperList.size());
			return wrapperList;
		} catch (Exception exception) {
			logger.error(WPTAnalyticsUtils.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(exception));
			throw exception;
		}
	}
	
	private YouTubeRawDataWrapper setYouTubeRawDataWrapper(HBaseResult result) {
		YouTubeRawDataWrapper wrapper = new YouTubeRawDataWrapper();
		try {	
			setDeviceGroupInformation(wrapper,result);	
			setNetworkInformation(wrapper,result);
			setBoundaryInformation(wrapper,result);
			setBasicDeviceInformation(wrapper,result);
			setDeviceNetworkInformation(wrapper, result);
			setKPIInformation(wrapper,result);
					
		} catch (Exception e) {
			logger.error(WPTAnalyticsUtils.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
		}
		return wrapper;
	}
	
	private YouTubeRawDataWrapper setDeviceGroupInformation(YouTubeRawDataWrapper wrapper, HBaseResult result) {
		wrapper.setDate(result.getString(YouTubeHbaseConstants.DATE));
		wrapper.setTimestamp(result.getString(YouTubeHbaseConstants.TIMESTAMP));
		wrapper.setOperatorName(result.getString(YouTubeHbaseConstants.OPERATOR_NAME));	
		wrapper.setDeviceId(result.getString(YouTubeHbaseConstants.DEVICE_ID));	
		wrapper.setAndriodId(result.getString(YouTubeHbaseConstants.ANDROID_ID));	
		wrapper.setIsLayer3Enabled(result.getString(YouTubeHbaseConstants.IS_LAYER3_ENABLED));	
		wrapper.setProfileId(result.getString(YouTubeHbaseConstants.PROFILE_ID));	
		wrapper.setPci(result.getString(YouTubeHbaseConstants.PCI));	
		wrapper.setTac(result.getString(YouTubeHbaseConstants.TAC));	
		wrapper.setPsc(result.getString(YouTubeHbaseConstants.PSC));	
		wrapper.setLac(result.getString(YouTubeHbaseConstants.LAC));	
		wrapper.setBand(result.getString(YouTubeHbaseConstants.BAND));	
		wrapper.setBaseband(result.getString(YouTubeHbaseConstants.BASE_BAND));	
		wrapper.setBuildNumber(result.getString(YouTubeHbaseConstants.BUILD_ID));	
		
		return wrapper;
	}

	private YouTubeRawDataWrapper setNetworkInformation(YouTubeRawDataWrapper wrapper, HBaseResult result) {
		wrapper.setNetworkType(result.getString(YouTubeHbaseConstants.NETWORK_TYPE));
		wrapper.setNetworkSubType(result.getString(YouTubeHbaseConstants.NETWORK_SUBTYPE));
		wrapper.setMcc(result.getString(YouTubeHbaseConstants.MCC));	
		wrapper.setMnc(result.getString(YouTubeHbaseConstants.MNC));	
		wrapper.setNetworkTypeWhenWifi(result.getString(YouTubeHbaseConstants.NETWORK_TYPE_WHEN_WIFI));	
		wrapper.setGpsStatus(result.getString(YouTubeHbaseConstants.GPS_STATUS));	
		wrapper.setWifiFrequency(result.getString(YouTubeHbaseConstants.WIFI_FREQUENCY));	
		wrapper.setDlSpeed(result.getString(YouTubeHbaseConstants.DL_SPEED));	
		wrapper.setVideoDuration(result.getString(YouTubeHbaseConstants.VIDEO_DURATION));	
			
		return wrapper;
	}
	
	private YouTubeRawDataWrapper setBoundaryInformation(YouTubeRawDataWrapper wrapper, HBaseResult result) {
		wrapper.setGeographyL1(result.getString(YouTubeHbaseConstants.GEOGRAPHY_L1));	
		wrapper.setGeographyL2(result.getString(YouTubeHbaseConstants.GEOGRAPHY_L2));	
		wrapper.setGeographyL3(result.getString(YouTubeHbaseConstants.GEOGRAPHY_L3));	
		wrapper.setGeographyL4(result.getString(YouTubeHbaseConstants.GEOGRAPHY_L4));	
		wrapper.setLocationAccuracy(result.getString(YouTubeHbaseConstants.LOCATION_ACCURACY));	
		wrapper.setAltitude(result.getString(YouTubeHbaseConstants.ALTITUDE));	
		wrapper.setLatitude(result.getString(YouTubeHbaseConstants.LATITUDE));	
		wrapper.setLongitude(result.getString(YouTubeHbaseConstants.LONGITUDE));	

		return wrapper;
	}

	private YouTubeRawDataWrapper setBasicDeviceInformation(YouTubeRawDataWrapper wrapper, HBaseResult result) {
		wrapper.setCellId(result.getString(YouTubeHbaseConstants.CELL_ID));	
		wrapper.setCgi(result.getString(YouTubeHbaseConstants.CGI));	
		wrapper.setEnodeB(result.getString(YouTubeHbaseConstants.ENODE_B));	
		wrapper.setMac(result.getString(YouTubeHbaseConstants.MAC));	
		wrapper.setDeviceOS(result.getString(YouTubeHbaseConstants.OPERATING_SYSTEM));	
		wrapper.setBatteryLevel(result.getString(YouTubeHbaseConstants.BATTERY_LEVEL));	
		wrapper.setVoltage(result.getString(YouTubeHbaseConstants.VOLTAGE));	
		wrapper.setTemperature(result.getString(YouTubeHbaseConstants.TEMPRATURE));	
		wrapper.setSocModel(result.getString(YouTubeHbaseConstants.SOC_MODEL));	
		wrapper.setCoreArchitecture(result.getString(YouTubeHbaseConstants.CORE_ARCHITECTURE));	
		wrapper.setFingerPrint(result.getString(YouTubeHbaseConstants.FINGERPRINT));	
		wrapper.setSerialNumber(result.getString(YouTubeHbaseConstants.SERIAL_NUMBER));	
		wrapper.setDeviceChipSet(result.getString(YouTubeHbaseConstants.CHIP_SET));	
		wrapper.setIsEnterprise(result.getString(YouTubeHbaseConstants.IS_ENTERPRISE));	
		wrapper.setDualSimEnable(result.getString(YouTubeHbaseConstants.IS_DUAL_SIM_ENABLE));	
		wrapper.setIsAutoDateTime(result.getString(YouTubeHbaseConstants.AUTO_DATE_TIME));	
		wrapper.setChargingStatus(result.getString(YouTubeHbaseConstants.CHARGING_STATUS));	
		wrapper.setMake(result.getString(YouTubeHbaseConstants.MAKE));	
		wrapper.setModel(result.getString(YouTubeHbaseConstants.MODEL));	
	
		return wrapper;
	}

	private YouTubeRawDataWrapper setDeviceNetworkInformation(YouTubeRawDataWrapper wrapper, HBaseResult result) {
		wrapper.setScreenResolution(result.getString(YouTubeHbaseConstants.SCREEN_RESOLUTION));	
		wrapper.setTotalBufferingTime(result.getString(YouTubeHbaseConstants.BUFFERING_TIME));	
		wrapper.setVideoStalling(result.getString(YouTubeHbaseConstants.VIDEO_STALLING));	
		wrapper.setVideoFreezingRatio(result.getString(YouTubeHbaseConstants.VIDEO_FREEZING_RATIO));	
		wrapper.setVideoLoadTime(result.getString(YouTubeHbaseConstants.VIDEO_LOAD_TIME));	
		wrapper.setChannel(result.getString(YouTubeHbaseConstants.CHANNEL));	
		wrapper.setLinkSpeed(result.getString(YouTubeHbaseConstants.LINK_SPEED));	
		wrapper.setInternalIp(result.getString(YouTubeHbaseConstants.INTERNAL_IP));	
		wrapper.setBaseAppVersion(result.getString(YouTubeHbaseConstants.APP_VERSION));	
		wrapper.setModuleVersion(result.getString(YouTubeHbaseConstants.MODULE_VERSION));	
		
		return wrapper;
	}
	private YouTubeRawDataWrapper setKPIInformation(YouTubeRawDataWrapper wrapper, HBaseResult result) {
		wrapper.setBssid(result.getString(YouTubeHbaseConstants.BSSID));	
		wrapper.setSsid(result.getString(YouTubeHbaseConstants.SSID));	
		wrapper.setRsrp(result.getString(YouTubeHbaseConstants.RSRP));	
		wrapper.setRsrq(result.getString(YouTubeHbaseConstants.RSRQ));	
		wrapper.setRssi(result.getString(YouTubeHbaseConstants.RSSI));	
		wrapper.setSinr(result.getString(YouTubeHbaseConstants.SINR));	
		wrapper.setWifiRSSI(result.getString(YouTubeHbaseConstants.WIFI_RSSI));	
		wrapper.setWifiSNR(result.getString(YouTubeHbaseConstants.WIFI_SNR));	
		wrapper.setRscp(result.getString(YouTubeHbaseConstants.RSCP));
		wrapper.setEcno(result.getString(YouTubeHbaseConstants.ECNO));	
		wrapper.setRxLevel(result.getString(YouTubeHbaseConstants.RX_LEVEL));	
		wrapper.setRxQuality(result.getString(YouTubeHbaseConstants.RX_QUALITY));	
	
		return wrapper;
	}



	

	
	
}

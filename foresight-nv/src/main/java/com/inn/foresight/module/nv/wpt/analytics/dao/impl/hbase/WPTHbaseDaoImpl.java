package com.inn.foresight.module.nv.wpt.analytics.dao.impl.hbase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.hadoop.hbase.HBaseResult;
import com.inn.foresight.core.generic.dao.impl.hbase.AbstractHBaseDao;
import com.inn.foresight.module.nv.app.dao.IDeviceInfoDao;
import com.inn.foresight.module.nv.app.model.DeviceInfo;
import com.inn.foresight.module.nv.wpt.analytics.constants.WPTAnalyticsConstants;
import com.inn.foresight.module.nv.wpt.analytics.constants.WPTHbaseConstants;
import com.inn.foresight.module.nv.wpt.analytics.dao.IWPTHbaseDao;
import com.inn.foresight.module.nv.wpt.analytics.utils.WPTAnalyticsUtils;
import com.inn.foresight.module.nv.wpt.analytics.utils.wrapper.WPTRawDataWrapper;

/** The Class WPTHbaseDaoImpl. */
@Repository("WPTHbaseDaoImpl")
public class WPTHbaseDaoImpl extends AbstractHBaseDao implements IWPTHbaseDao {

	/** The logger. */
	private Logger logger = LogManager.getLogger(WPTHbaseDaoImpl.class);

	@Autowired
	private IDeviceInfoDao deviceInfoDao;

	/**
	 * Insert WPT result list.
	 *
	 * @param putList
	 *            the put list
	 * @param table
	 *            the table
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Override
	public void insertWPTResultList(List<Put> putList, String table) throws IOException {
		logger.info("Going to insert record into Hbase Table {}", table);
		insert(table, putList);
	}

	/**
	 * Scan data with time range.
	 *
	 * @param minStamp
	 *            the min stamp
	 * @param maxStamp
	 *            the max stamp
	 * @return the list
	 * @throws Exception
	 *             the exception
	 */
	@Override
	public List<WPTRawDataWrapper> scanDataWithTimeRange(Long minStamp, Long maxStamp, Filter filter) throws Exception {
		logger.info("Going to scan records from HBase for TimeRange : {} to {}", minStamp, maxStamp);
		List<WPTRawDataWrapper> wrapperList = new ArrayList<>();
		try {
			Scan scan = new Scan();
			if (filter != null) {
				scan.setFilter(filter);
			}
			scan.setTimeRange(minStamp, maxStamp);

			iterateResultByPool(scan, ConfigUtils.getString(WPTHbaseConstants.WPT_TABLE_NAME),
					Bytes.toBytes(ConfigUtils.getString(WPTHbaseConstants.WPT_COLUMN_FAMILY)),
					result -> wrapperList.add(setWPTRawWrapper(result)));

			logger.info(WPTAnalyticsConstants.RESULT_SIZE_LOGGER, wrapperList.size());
			return wrapperList;
		} catch (Exception exception) {
			logger.error(WPTAnalyticsUtils.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(exception));
			throw exception;
		}
	}

	/**
	 * Sets the WPT raw wrapper.
	 *
	 * @param result
	 *            the result
	 * @return the WPT raw data wrapper
	 */
	private WPTRawDataWrapper setWPTRawWrapper(HBaseResult result) {
		WPTRawDataWrapper wrapper = new WPTRawDataWrapper();
		try {
			wrapper.setDeviceId(result.getString(WPTHbaseConstants.DEVICE_ID));
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

	private WPTRawDataWrapper setDeviceGroupInformation(WPTRawDataWrapper wrapper, HBaseResult result) {
		wrapper.setTestId(result.getStringAsInteger(WPTHbaseConstants.TEST_ID));
		wrapper.setTestPerformed(result.getString(WPTHbaseConstants.TEST_PERFORMED));
		wrapper.setNetworkType(result.getString(WPTHbaseConstants.NETWORK_TYPE));
		wrapper.setDate(result.getString(WPTHbaseConstants.DATE));
		wrapper.setTestStartTime(result.getStringAsLong(Bytes.toBytes(WPTHbaseConstants.TEST_START_TIME)));
		wrapper.setTestEndTime(result.getStringAsLong(Bytes.toBytes(WPTHbaseConstants.TEST_END_TIME)));
		wrapper.setWebUrl(result.getString(WPTHbaseConstants.WEB_URL));
		wrapper.setIteration(result.getString(WPTHbaseConstants.ITERATION));
		wrapper.setFirstDNSResolutionTime(result.getStringAsLong(Bytes.toBytes(WPTHbaseConstants.FDNS)));
		wrapper.setTotalDnsResolutionTime(result.getStringAsLong(Bytes.toBytes(WPTHbaseConstants.TDNS)));
		wrapper.setTtfb(result.getString(Bytes.toBytes(WPTHbaseConstants.TTFB)));
		wrapper.setTtl(result.getString(Bytes.toBytes(WPTHbaseConstants.TTL)));

		return wrapper;
	}
	private WPTRawDataWrapper setNetworkInformation(WPTRawDataWrapper wrapper, HBaseResult result) {
		
		wrapper.setTargetedIp(result.getString(WPTHbaseConstants.TARGET_IP));
		wrapper.setExternalIp(result.getString(WPTHbaseConstants.EXTERNAL_IP));
		wrapper.setCity(result.getString(WPTHbaseConstants.GEOGRAPHYL3));
		wrapper.setNoOfRedirection(result.getString(WPTHbaseConstants.NO_OF_REDICTION));
		wrapper.setRedirectedUrl(result.getString(WPTHbaseConstants.REDIRECTED_URL));
		wrapper.setRouteHolderList(result.getString(WPTHbaseConstants.ROUTE_HOLDER_LIST));
		wrapper.setHopeCount(result.getString(WPTHbaseConstants.HOPE_COUNT));
		wrapper.setIsIpv6(result.getStringAsBoolean(Bytes.toBytes(WPTHbaseConstants.IS_IPV6)));
		wrapper.setIpVersion(result.getString(WPTHbaseConstants.IPV));
		wrapper.setIpv4list(result.getString(WPTHbaseConstants.IPV4_LIST));
		wrapper.setIpv6list(result.getString(WPTHbaseConstants.IPV6_LIST));
	
		return wrapper;
	}
	private WPTRawDataWrapper setBoundaryInformation(WPTRawDataWrapper wrapper, HBaseResult result) {
		
		wrapper.setGeographyL2(result.getString(WPTHbaseConstants.GEOGRAPHYL2));
		wrapper.setGeographyL3(result.getString(WPTHbaseConstants.GEOGRAPHYL3));
		wrapper.setLatitude(result.getStringAsDouble(Bytes.toBytes(WPTHbaseConstants.LATITUDE)));
		wrapper.setLongitude(result.getStringAsDouble(Bytes.toBytes(WPTHbaseConstants.LONGTITUDE)));
	
		return wrapper;
	}
	private WPTRawDataWrapper setBasicDeviceInformation(WPTRawDataWrapper wrapper, HBaseResult result) {
		try {
			wrapper.setOperatorName(result.getString(WPTHbaseConstants.OP_NAME));
			DeviceInfo deviceInfo = deviceInfoDao.getDeviceInfoByDeviceId(wrapper.getDeviceId());
			wrapper.setImei(deviceInfo.getImei());
			wrapper.setImsi(deviceInfo.getImsi());
			wrapper.setManufacturer(result.getString(WPTHbaseConstants.MAKE));
			wrapper.setModel(result.getString(WPTHbaseConstants.MODEL));
			wrapper.setDeviceOS(result.getString(WPTHbaseConstants.DEVICE_OS));
			wrapper.setLocationType(result.getString(WPTHbaseConstants.LOCATION_TYPE));
			wrapper.setRemark(result.getString(WPTHbaseConstants.REMARK));
			wrapper.setGpsStatus(result.getString(WPTHbaseConstants.GPS_STATUS));
			wrapper.setChargerConnectedStatus(result.getString(WPTHbaseConstants.CHARGER_CONNECTED_STATUS));
			wrapper.setBatteryLevel(result.getString(WPTHbaseConstants.BATTERYLEVEL));
			wrapper.setVoltage(result.getString(WPTHbaseConstants.VOLTAGE));
			wrapper.setTemperature(result.getString(WPTHbaseConstants.TEMPERATURE));
			wrapper.setPageSize(result.getString(WPTHbaseConstants.PAGE_SIZE));
			wrapper.setBaseband(result.getString(WPTHbaseConstants.BASEBAND));
			wrapper.setBuildNumber(result.getString(WPTHbaseConstants.BUILNUMBER));
			wrapper.setIsAutoDataTime(result.getStringAsBoolean(Bytes.toBytes(WPTHbaseConstants.IS_AUTO_DATA_TIME)));
			wrapper.setMac(result.getString(WPTHbaseConstants.MAC));
			
		}catch (Exception e) {
			logger.error(WPTAnalyticsUtils.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
		}
	
		return wrapper;
	}
	private WPTRawDataWrapper setDeviceNetworkInformation(WPTRawDataWrapper wrapper, HBaseResult result) {
		
		wrapper.setProbeId(result.getString(WPTHbaseConstants.PROBE_ID));
		wrapper.setMcc(result.getStringAsInteger(Bytes.toBytes(WPTHbaseConstants.MCC)));
		wrapper.setMnc(result.getStringAsInteger(Bytes.toBytes(WPTHbaseConstants.MNC)));
		wrapper.setCellId(result.getStringAsInteger(Bytes.toBytes(WPTHbaseConstants.CELLID)));
		wrapper.setPci(result.getString(WPTHbaseConstants.PCI));
		wrapper.setPsc(result.getString(WPTHbaseConstants.PSC));
		wrapper.setTac(result.getString(WPTHbaseConstants.TAC));
		wrapper.setLac(result.getString(WPTHbaseConstants.LAC));
	
		return wrapper;
	}
	private WPTRawDataWrapper setKPIInformation(WPTRawDataWrapper wrapper, HBaseResult result) {
		
		wrapper.setAvgRsrp(result.getString(WPTHbaseConstants.AVG_RSRP));
		wrapper.setAvgRsrq(result.getString(WPTHbaseConstants.AVG_RSRQ));
		wrapper.setAvgSinr(result.getString(WPTHbaseConstants.AVG_SINR));
		wrapper.setAvgRssi(result.getString(WPTHbaseConstants.AVG_RSSI));
		wrapper.setAvgRscp(result.getString(WPTHbaseConstants.AVG_RSCP));
		wrapper.setEcno(result.getString(WPTHbaseConstants.AVG_ECNO));
		wrapper.setRxLevel(result.getString(WPTHbaseConstants.AVG_RX_LEVEL));
		wrapper.setRxQuality(result.getString(WPTHbaseConstants.AVG_RX_QUALITY));

		return wrapper;
	}
}

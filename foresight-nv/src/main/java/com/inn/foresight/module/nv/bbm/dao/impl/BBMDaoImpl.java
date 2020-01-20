package com.inn.foresight.module.nv.bbm.dao.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.hadoop.hbase.HBaseResult;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.dao.impl.hbase.AbstractHBaseDao;
import com.inn.foresight.core.generic.utils.ConfigUtil;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.bbm.constant.BSPConstants;
import com.inn.foresight.module.nv.bbm.dao.IBBMDao;
import com.inn.foresight.module.nv.bbm.utils.BBMComparator;
import com.inn.foresight.module.nv.bbm.utils.wrapper.BBMDetailWrapper;


@Service("BBMDaoImpl")
public class BBMDaoImpl extends AbstractHBaseDao implements IBBMDao {

	private final Logger logger = LogManager.getLogger(BBMDaoImpl.class);

	private List<BBMDetailWrapper> getFilteredList(Long noOfRecords, List<BBMDetailWrapper> wrapperList) {
		logger.info("Going to apply filter for noOfRecords {} :", noOfRecords);
		if (!wrapperList.isEmpty()) {
			logger.info("SIZE {}",wrapperList.size());
			Collections.sort(wrapperList, new BBMComparator());
		}
		if (noOfRecords != null && noOfRecords > 0) {
			logger.info("LIST SORTED {}",wrapperList.stream().limit(noOfRecords).collect(Collectors.toList()));
			return wrapperList.stream().limit(noOfRecords).collect(Collectors.toList());
		} else {
			logger.info("ALL RECORDS");
			return wrapperList;
		}
	}
	
	@Override
	public List<BBMDetailWrapper> getBBMDetailByDeviceIdPrefix(String deviceIdPrefix, Long minTimeRange, Long maxTimeRange) {
		List<BBMDetailWrapper> wrapperList = new ArrayList<>();
		logger.info("Going to get BBM detail by device id Prefix {} ", deviceIdPrefix);
		try {
			Scan scan = new Scan();
			scan.setRowPrefixFilter(Bytes.toBytes(deviceIdPrefix));

			if (minTimeRange != null && maxTimeRange != null) {
				logger.info("Fetching data by minTimeRange and maxTimeRange");
				scan.setTimeRange(minTimeRange, maxTimeRange);
				wrapperList = fetchBBMHistoryDataFromHbase(wrapperList, scan);
			} else {
				logger.info("Fetching latest one records by device id");
				wrapperList = getFilteredList(1L, fetchBBMDataFromHbase(wrapperList, scan));
			}
			logger.info("BBM wrapper list size :  {} ", wrapperList.size());
			return wrapperList;
		} catch (Exception exception) {

			logger.error("Error in getting BBM Detail by device id : {}  ", Utils.getStackTrace(exception));
			throw new RestException("Unable to get BBM detail");
		}
	}

	private List<BBMDetailWrapper> fetchBBMDataFromHbase(List<BBMDetailWrapper> wrapperList, Scan scan) throws IOException {
		logger.info("Inside fetchBBMDataFromHbase");
		try {
			List<String> bbmColumnList = ConfigUtils.getStringList(BSPConstants.BBM_COLUMN_LIST);
			List<HBaseResult> result = scanResultByPool(scan, ConfigUtil.getConfigProp(BSPConstants.BBM_TABLE),
					Bytes.toBytes(ConfigUtil.getConfigProp(BSPConstants.BBM_COLUMN_FAMILY)));
			for (HBaseResult hBaseResult : result) {
				String latitude = hBaseResult.getString(Bytes.toBytes(BSPConstants.LATITUDE));
				String longitude = hBaseResult.getString(Bytes.toBytes(BSPConstants.LONGITUDE));

				setValuesInWrapper(wrapperList, bbmColumnList, hBaseResult, latitude, longitude);
			}
		} catch (Exception e) {
			logger.error("Error in fetching BBM data from hbase : {}", Utils.getStackTrace(e));
		}
		logger.info("LIST SIZE ==============={}", wrapperList.size());
		return wrapperList;
	}

	private void setValuesInWrapper(List<BBMDetailWrapper> wrapperList, List<String> bbmColumnList,
			HBaseResult hBaseResult, String latitude, String longitude) {
		if (StringUtils.isEmpty(latitude) && StringUtils.isEmpty(longitude)) {
			BBMDetailWrapper wrapper = new BBMDetailWrapper();
			logger.info("LATITUDE : {} AND LONGITUDE : {}  NOT NULL", latitude, longitude);
			for (String column : bbmColumnList) {
				setBBMDataIntoWrapper(wrapper, hBaseResult, column);
			}	
			wrapperList.add(wrapper);
		} else {
			logger.info("LATITUDE AND LONGITUDE IS NULL ");
		}
	}

	private List<BBMDetailWrapper> fetchBBMHistoryDataFromHbase(List<BBMDetailWrapper> wrapperList, Scan scan) throws IOException {
		logger.info("Inside fetchBBMHistoryDataFromHbase");
		try {
			List<String> bbmColumnList = ConfigUtils.getStringList(BSPConstants.BBM_COLUMN_LIST);
			List<HBaseResult> result = scanResultByPool(scan, ConfigUtil.getConfigProp(BSPConstants.BBM_TABLE),
					Bytes.toBytes(ConfigUtil.getConfigProp(BSPConstants.BBM_COLUMN_FAMILY)));
			for (HBaseResult hBaseResult : result) {
				BBMDetailWrapper wrapper = new BBMDetailWrapper();
				for (String column : bbmColumnList) {
					setBBMDataIntoWrapper(wrapper, hBaseResult, column);
				}
				wrapperList.add(wrapper);
			}
		} catch (Exception e) {
			logger.error("Error in fetching BBM data from hbase : {}", Utils.getStackTrace(e));
		}
		return wrapperList;
	}


	private void setBBMDataIntoWrapper(BBMDetailWrapper wrapper, HBaseResult hBaseResult, String column) {
		try {
			setDeviceParameters(wrapper, hBaseResult, column);
			setNetworkParameters(wrapper, hBaseResult, column);
			
		} catch (Exception e) {
			logger.error("Error in setting BBM data in to wrapper : {}", Utils.getStackTrace(e));
		}
	}

	private void setNetworkParameters(BBMDetailWrapper wrapper, HBaseResult hBaseResult, String column) {
		if (column.equalsIgnoreCase(BSPConstants.CALL_DURATION)) {
			wrapper.setCallDuration(hBaseResult.getString(Bytes.toBytes(column)));
		} else if (column.equalsIgnoreCase(BSPConstants.CALL_END_TIME)) {
			wrapper.setCallEndTime(hBaseResult.getString(Bytes.toBytes(column)));
		} else if (column.equalsIgnoreCase(BSPConstants.CALL_STAR_TTIME)) {
			wrapper.setCallStartTime(hBaseResult.getString(Bytes.toBytes(column)));
		} else if (column.equalsIgnoreCase(BSPConstants.CALL_TYPE)) {
			wrapper.setCallType(hBaseResult.getString(Bytes.toBytes(column)));
		} else if (column.equalsIgnoreCase(BSPConstants.RX_LEVEL)) {
			wrapper.setRxLevel(hBaseResult.getString(Bytes.toBytes(column)));
		} else if (column.equalsIgnoreCase(BSPConstants.RELEASE_CAUSE)) {
			wrapper.setReleaseCause(hBaseResult.getString(Bytes.toBytes(column)));
		} else if (column.equalsIgnoreCase(BSPConstants.RSCP)) {
			wrapper.setRscp(hBaseResult.getString(Bytes.toBytes(column)));
		} else if (column.equalsIgnoreCase(BSPConstants.RSRP)) {
			wrapper.setRsrp(hBaseResult.getString(Bytes.toBytes(column)));
		} else if (column.equalsIgnoreCase(BSPConstants.RSRQ)) {
			wrapper.setRsrq(hBaseResult.getString(Bytes.toBytes(column)));
		} else if (column.equalsIgnoreCase(BSPConstants.CGI)) {
			wrapper.setCgi(hBaseResult.getString(Bytes.toBytes(column)));
		} else if (column.equalsIgnoreCase(BSPConstants.SINR)) {
			wrapper.setSinr(hBaseResult.getString(Bytes.toBytes(column)));
		} else if (column.equalsIgnoreCase(BSPConstants.SESSION_THROUGHPUT)) {
			wrapper.setSessionThroughput(hBaseResult.getString(Bytes.toBytes(column)));
		} else if (column.equalsIgnoreCase(BSPConstants.PCI)) {
			wrapper.setPci(hBaseResult.getString(Bytes.toBytes(column)));
		} else if (column.equalsIgnoreCase(BSPConstants.JITTER)) {
			wrapper.setJitter(hBaseResult.getString(Bytes.toBytes(column)));
		} else if (column.equalsIgnoreCase(BSPConstants.LATENCY)) {
			wrapper.setLatency(hBaseResult.getString(Bytes.toBytes(column)));
		}
	}

	private void setDeviceParameters(BBMDetailWrapper wrapper, HBaseResult hBaseResult, String column) {
		if (column.equalsIgnoreCase(BSPConstants.MAKE)) {
			wrapper.setMake(hBaseResult.getString(Bytes.toBytes(column)));
		} else if (column.equalsIgnoreCase(BSPConstants.MODEL)) {
			wrapper.setModel(hBaseResult.getString(Bytes.toBytes(column)));
		} else if (column.equalsIgnoreCase(BSPConstants.DEVICE_ID)) {
			wrapper.setDeviceId(hBaseResult.getString(Bytes.toBytes(column)));
		} else if (column.equalsIgnoreCase(BSPConstants.DEVICE_OS)) {
			wrapper.setDeviceOs(hBaseResult.getString(Bytes.toBytes(column)));
		} else if (column.equalsIgnoreCase(BSPConstants.BBM_APP_VERSION)) {
			wrapper.setBbmAppVersion(hBaseResult.getString(Bytes.toBytes(column)));
		} else if (column.equalsIgnoreCase(BSPConstants.LATITUDE)) {
			wrapper.setLatitude(hBaseResult.getString(Bytes.toBytes(column)));
		} else if (column.equalsIgnoreCase(BSPConstants.LONGITUDE)) {
			wrapper.setLongitude(hBaseResult.getString(Bytes.toBytes(column)));
		} else if (column.equalsIgnoreCase(BSPConstants.TIME)) {
			wrapper.setTime(hBaseResult.getString(Bytes.toBytes(column)));
		} else if (column.equalsIgnoreCase(BSPConstants.OPERATOR_NAME)) {
			wrapper.setOperatorName(hBaseResult.getString(Bytes.toBytes(column)));
		} else if (column.equalsIgnoreCase(BSPConstants.NETWORK_TYPE)) {
			wrapper.setNetworkType(hBaseResult.getString(Bytes.toBytes(column)));
		} else if (column.equalsIgnoreCase(BSPConstants.MNC)) {
			wrapper.setMnc(hBaseResult.getString(Bytes.toBytes(column)));
		} else if (column.equalsIgnoreCase(BSPConstants.MCC)) {
			wrapper.setMcc(hBaseResult.getString(Bytes.toBytes(column)));
		}
	}

}
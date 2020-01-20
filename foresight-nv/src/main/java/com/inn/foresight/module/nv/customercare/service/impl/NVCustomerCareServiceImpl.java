package com.inn.foresight.module.nv.customercare.service.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import com.inn.commons.hadoop.hbase.HBaseResult;
import com.inn.commons.http.HttpException;
import com.inn.commons.maps.LatLng;
import com.inn.commons.maps.nns.NNS;
import com.inn.commons.unit.Duration;
import com.inn.commons.unit.Length;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ConfigEnum;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.app.dao.IDeviceInfoDao;
import com.inn.foresight.module.nv.app.model.DeviceInfo;
import com.inn.foresight.module.nv.customercare.constant.NVCustomerCareConstant;
import com.inn.foresight.module.nv.customercare.dao.IActiveHbaseDao;
import com.inn.foresight.module.nv.customercare.dao.IPassiveHbaseDao;
import com.inn.foresight.module.nv.customercare.service.INVCustomerCareService;
import com.inn.foresight.module.nv.customercare.util.CustomerCareDataComparator;
import com.inn.foresight.module.nv.customercare.util.NVCustomerCareUtil;
import com.inn.foresight.module.nv.customercare.wrapper.NVCustomerCareDataWrapper;
import com.inn.foresight.module.nv.inbuilding.utils.NVBuildingUtils;
import com.inn.product.security.utils.AuthenticationCommonUtil;

/** The Class CustomerCareServiceImpl. */
@Service("NVCustomerCareServiceImpl")
public class NVCustomerCareServiceImpl implements INVCustomerCareService {
	/** The logger. */
	private Logger logger = LogManager.getLogger(NVCustomerCareServiceImpl.class);

	/** The i device info dao. */
	@Autowired
	private IDeviceInfoDao iDeviceInfoDao;
	@Autowired
	private IActiveHbaseDao iActiveHbaseDao;
	@Autowired
	private IPassiveHbaseDao iPassiveHbaseDao;


	/**
	 * Gets the NV latest data.
	 *
	 * @param type
	 *            the type
	 * @param deviceId
	 *            the device id
	 * @param imei
	 *            the imei
	 * @param imsi
	 *            the imsi
	 * @param minTimeStamp
	 *            the min time stamp
	 * @param maxTimeStmap
	 *            the max time stmap
	 * @param lastRowKey
	 *            the last row key
	 * @param noOfRecords
	 *            the no of records
	 * @param rowKey
	 *            the row key
	 * @return the NV latest data
	 * @throws RestException
	 *             the rest exception
	 */
	@Override
	public String getNVLatestData(String type, String deviceId, String imei, String imsi, Long minTimeStamp,
			Long maxTimeStmap, String lastRowKey, Long noOfRecords, String rowKey) {
		String response = null;
		Map<String, String> deviceIdImeiMap = new HashMap<>();

		List<DeviceInfo> deviceList = new ArrayList<>();
		if (rowKey == null && deviceId == null && (imei != null || imsi != null)) {
			try {
				deviceList = iDeviceInfoDao.getDeviceInfoList(imei, imsi);
				deviceIdImeiMap = NVCustomerCareUtil.setDeviceIdImeiMap(deviceList);
			} catch (DaoException e) {
				logger.error("DaoException in getNVLatestData when getDeviceInfoList {} ",
						ExceptionUtils.getStackTrace(e));
			}
			if (deviceList == null || deviceList.isEmpty()) {
				throw new RestException(NVCustomerCareConstant.NO_DEVICE_FOUND_FOR	.concat("imei =")
																					.concat(imei)
						+ "& imsi=".concat(imsi));
			}
			deviceId = NVCustomerCareUtil.getDeviceIdString(deviceList);
		} else if (deviceId != null) {
			try {
				deviceList.add(iDeviceInfoDao.getDeviceInfoByDeviceId(deviceId));
				deviceIdImeiMap = NVCustomerCareUtil.setDeviceIdImeiMap(deviceList);
			} catch (DaoException e) {
				logger.error("DaoException in getNVLatestData when getDeviceInfoByDeviceId {}",
						ExceptionUtils.getStackTrace(e));
			}
		}
		response = sendRequestToDropwizard(type, deviceId, minTimeStamp, maxTimeStmap, lastRowKey,
				noOfRecords, rowKey);

		return tagImeiAndGetEncryptedResponse(response, deviceIdImeiMap);
	}

	/**
	 * Tag imei and get encrypted response.
	 *
	 * @param response
	 *            the response
	 * @param deviceIdImeiMap
	 *            the device id imei map
	 * @return the string
	 */
	private String tagImeiAndGetEncryptedResponse(String response, Map<String, String> deviceIdImeiMap) {
		logger.info("Going to tagImeiAndGetEncryptedResponse");
		List<NVCustomerCareDataWrapper> activePassiveDataWrappers = null;
		if (response != null) {
			Type type = new TypeToken<List<NVCustomerCareDataWrapper>>() {
			}.getType();
			activePassiveDataWrappers = new Gson().fromJson(response, type);
			if (deviceIdImeiMap.isEmpty()) {
				activePassiveDataWrappers	.parallelStream()
											.forEach(a -> a.setImei(getDeviceImei(a.getDeviceId())));
			} else {
				activePassiveDataWrappers	.parallelStream()
											.forEach(a -> a.setImei(deviceIdImeiMap.get(a.getDeviceId())));
			}
		}
		logger.info("Done tagImeiAndGetEncryptedResponse");
		return AuthenticationCommonUtil.checkForValueEncryption(new Gson().toJson(activePassiveDataWrappers),null);
	}

	/**
	 * Gets the device imei.
	 *
	 * @param deviceId
	 *            the device id
	 * @return the device imei
	 */
	private String getDeviceImei(String deviceId) {
		try {
			return iDeviceInfoDao	.getDeviceInfoByDeviceId(deviceId)
									.getImei();
		} catch (DaoException e) {
			logger.error("DaoException in getNVLatestData when getDeviceImei by deviceID {}",
					ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	/**
	 * Send request to dropwizard.
	 *
	 * @param type
	 *            the type
	 * @param deviceId
	 *            the device id
	 * @param imei
	 *            the imei
	 * @param imsi
	 *            the imsi
	 * @param minTimeStamp
	 *            the min time stamp
	 * @param maxTimeStmap
	 *            the max time stmap
	 * @param lastRowKey
	 *            the last row key
	 * @param noOfRecords
	 *            the no of records
	 * @param rowKey
	 *            the row key
	 * @return the string
	 * @throws RestException
	 *             the rest exception
	 */
	private String sendRequestToDropwizard(String type, String deviceId, Long minTimeStamp,
			Long maxTimeStmap, String lastRowKey, Long noOfRecords, String rowKey) {
		String response = null;
		try {
			Duration duration = Duration.minutes(
					Integer.parseInt(ConfigUtils.getString(ConfigEnum.TIMEOUT_VALUE.getValue())));

			String url = NVCustomerCareUtil.getDropwizardUrl(
					NVCustomerCareConstant.GET_NV_LATEST_DATA_URI.concat(NVCustomerCareUtil.getQueryString(type, deviceId, minTimeStamp, maxTimeStmap, lastRowKey, noOfRecords, rowKey)));
			logger.info("DropWizard URL to getNVLatestData:{}", url);
			response = NVBuildingUtils	.sendHttpGetRequest(url, true, duration)
										.getString();
			logger.info("Done   getNVLatestData response from dropwizard: {}", response);
		} catch (HttpException e1) {
			logger.error("HttpException in getNVLatestData {}", ExceptionUtils.getStackTrace(e1));
			throw new RestException(NVCustomerCareConstant.EXCEPTION_PROBLEM_IN_DROPWIZARD);
		}
		return response;
	}

	/**
	 * Gets the NV latest data.
	 *
	 * @param type
	 *            the type
	 * @param imei
	 *            the imei
	 * @param imsi
	 *            the imsi
	 * @param deviceId
	 *            the device id
	 * @param minTimeStamp
	 *            the min time stamp
	 * @param maxTimeStmap
	 *            the max time stmap
	 * @param lLimit
	 *            the l limit
	 * @param noOfRecords
	 *            the no of records
	 * @param rowkey
	 *            the rowkey
	 * @return the NV latest data
	 */
	@Override
	public List<NVCustomerCareDataWrapper> getNVLatestData(String type, String deviceIds, Long minTimeStamp,
			Long maxTimeStmap, String lastRowKey, Long noOfRecords, String rowKey) {
		List<NVCustomerCareDataWrapper> activePassiveDataWrapperList = new ArrayList<>();
		if (rowKey != null) {
			logger.info("Going to getNVLatestData for rowKey:{} and type :{}", rowKey, type);
			activePassiveDataWrapperList = getActivePassiveDataForRowKey(rowKey, type);
		} else if (deviceIds != null) {
			logger.info("Going to getNVLatestData for deviceIds:{} and type:{}", deviceIds, type);
			activePassiveDataWrapperList = getActivePassiveDataForDeviceIdPrefix(Arrays.asList(deviceIds.split(",")),
					type, minTimeStamp, maxTimeStmap, lastRowKey, noOfRecords);
		}
		return getFilteredList(noOfRecords, activePassiveDataWrapperList);
	}
	
	

	private List<NVCustomerCareDataWrapper> getActivePassiveDataForDeviceIdPrefix(List<String> deviceIds, String type,
			Long minTimeStamp, Long maxTimeStmap, String lastRowKey, Long noOfRecords) {

		List<Scan> scanList = NVCustomerCareUtil.getScanList(NVCustomerCareUtil.getRowPrefix(deviceIds), minTimeStamp,
				maxTimeStmap, lastRowKey, noOfRecords);

		List<NVCustomerCareDataWrapper> activeDataWrapperList = null;
		List<NVCustomerCareDataWrapper> passiveDataWrapperList = null;
		if (NVCustomerCareConstant.ACTIVE_TYPE.equalsIgnoreCase(type)) {
			List<HBaseResult> resultList = iActiveHbaseDao.getActiveData(scanList);
			activeDataWrapperList = NVCustomerCareUtil.getActiveDataFromResultList(resultList);
		} else if (NVCustomerCareConstant.PASSIVE_TYPE.equalsIgnoreCase(type)) {
			List<HBaseResult> resultList = iPassiveHbaseDao.getPassiveData(scanList);
			passiveDataWrapperList = NVCustomerCareUtil.getActiveDataFromResultList(resultList);
		} else if (NVCustomerCareConstant.BOTH_TYPE.equalsIgnoreCase(type)) {
			List<HBaseResult> activeResultList = iActiveHbaseDao.getActiveData(scanList);
			List<HBaseResult> passiveResultList = iPassiveHbaseDao.getPassiveData(scanList);
			activeDataWrapperList = NVCustomerCareUtil.getActiveDataFromResultList(activeResultList);
			passiveDataWrapperList = NVCustomerCareUtil.getActiveDataFromResultList(passiveResultList);
		}
		List<NVCustomerCareDataWrapper> activePassiveDataWrapperList = new ArrayList<>();
		if (activeDataWrapperList != null) {
			activePassiveDataWrapperList.addAll(activeDataWrapperList);
		}
		if (passiveDataWrapperList != null) {
			activePassiveDataWrapperList.addAll(passiveDataWrapperList);
		}
		return activePassiveDataWrapperList;
	}

	private List<NVCustomerCareDataWrapper> getFilteredList(Long noOfRecords,
			List<NVCustomerCareDataWrapper> activePassiveDataWrapperList) {
		logger.info("Going to apply filter for noOfRecords {} :", noOfRecords);
		logger.info("activePassiveDataWrapperList size:{}", activePassiveDataWrapperList.size());
		if (!activePassiveDataWrapperList.isEmpty()) {
			Collections.sort(activePassiveDataWrapperList, new CustomerCareDataComparator());
		}
		if (noOfRecords != null && noOfRecords > ForesightConstants.ZERO) {
			return activePassiveDataWrapperList	.stream()
												.limit(noOfRecords)
												.collect(Collectors.toList());
		} else {
			return activePassiveDataWrapperList;
		}
	}

	private List<NVCustomerCareDataWrapper> getActivePassiveDataForRowKey(String rowKey, String type) {
		List<String> rowkeylist = new ArrayList<>();
		rowkeylist.add(rowKey);
		List<NVCustomerCareDataWrapper> activeDataWrapperList = null;
		List<NVCustomerCareDataWrapper> passiveDataWrapperList = null;
		if (NVCustomerCareConstant.ACTIVE_TYPE.equalsIgnoreCase(type)) {
			List<HBaseResult> resultList = iActiveHbaseDao.getActiveDataByRowKey(NVCustomerCareUtil.getRowKeyList(
					rowkeylist, Bytes.toBytes(NVCustomerCareConstant.COLUMN_FAMILY), NVCustomerCareUtil.getActiveColumnList()));
			activeDataWrapperList = NVCustomerCareUtil.getActiveDataFromResultList(resultList);
		} else if (NVCustomerCareConstant.PASSIVE_TYPE.equalsIgnoreCase(type)) {
			List<HBaseResult> resultList = iPassiveHbaseDao.getPassiveDataByRowKey(NVCustomerCareUtil.getRowKeyList(
					rowkeylist,  Bytes.toBytes(NVCustomerCareConstant.COLUMN_FAMILY), NVCustomerCareUtil.getPassiveColumnList()));
			passiveDataWrapperList = NVCustomerCareUtil.getPassiveDataFromResultList(resultList);
		} else if (NVCustomerCareConstant.BOTH_TYPE.equalsIgnoreCase(type)) {
			List<HBaseResult> activeresultList = iActiveHbaseDao.getActiveDataByRowKey(NVCustomerCareUtil.getRowKeyList(
					rowkeylist, Bytes.toBytes(NVCustomerCareConstant.COLUMN_FAMILY), NVCustomerCareUtil.getActiveColumnList()));
			List<HBaseResult> passiveresultList = iPassiveHbaseDao.getPassiveDataByRowKey(
					NVCustomerCareUtil.getRowKeyList(rowkeylist,  Bytes.toBytes(NVCustomerCareConstant.COLUMN_FAMILY),
							NVCustomerCareUtil.getPassiveColumnList()));
			activeDataWrapperList = NVCustomerCareUtil.getActiveDataFromResultList(activeresultList);
			passiveDataWrapperList = NVCustomerCareUtil.getPassiveDataFromResultList(passiveresultList);
		}
		List<NVCustomerCareDataWrapper> activePassiveDataWrapperList = new ArrayList<>();
		if (activeDataWrapperList != null) {
			activePassiveDataWrapperList.addAll(activeDataWrapperList);
		}
		if (passiveDataWrapperList != null) {
			activePassiveDataWrapperList.addAll(passiveDataWrapperList);
		}
		return activePassiveDataWrapperList;
	}
	
	@Override
	public NVCustomerCareDataWrapper getLatestSpeedTestDataByDeviceId(String deviceId, Double latitude, Double longitude) {
		logger.info("Going to get latest nv data by device id {} ", deviceId);
		NVCustomerCareDataWrapper nvDataWrapper = new NVCustomerCareDataWrapper();
		try {
			List<NVCustomerCareDataWrapper> wrapperList = new ArrayList<>();
			NNS<NVCustomerCareDataWrapper> nvDeviceDatalist = null;
			List<String> deviceIds = new ArrayList<>();
			deviceIds.add(deviceId);
			List<Scan> scanList = NVCustomerCareUtil.getScanList(NVCustomerCareUtil.getRowPrefix(deviceIds), null, null, null, null);
			List<HBaseResult> resultList = iActiveHbaseDao.getActiveData(scanList);
			wrapperList = NVCustomerCareUtil.getActiveDataFromResultList(resultList);
			logger.info("wrapperList before jsi size : {}", wrapperList.size());
			nvDeviceDatalist = populateNVDataJSI(wrapperList, nvDeviceDatalist);
			
			if (nvDeviceDatalist != null) {
				wrapperList = getNearestNVData(latitude, longitude, nvDeviceDatalist);
			}
			
			if (wrapperList != null && !wrapperList.isEmpty()) {
				wrapperList = getFilteredList(1l, wrapperList);
				nvDataWrapper = wrapperList.get(0);
				logger.info("Found NV Last Speed Test data : {}", nvDataWrapper);
				logger.info("wrapperList after jsi size : {}", wrapperList.size());
			}
		} catch (Exception e) {
			logger.error("Error in getting latest nv data by deviceid : {} Execption : {}", deviceId, Utils.getStackTrace(e));
		}
		return nvDataWrapper;
	}

	private NNS<NVCustomerCareDataWrapper> populateNVDataJSI(List<NVCustomerCareDataWrapper> wrapperList, NNS<NVCustomerCareDataWrapper> nvDeviceDatalist) {
		if (wrapperList != null && !wrapperList.isEmpty()) {
			nvDeviceDatalist = new NNS<>(wrapperList);
		}
		return nvDeviceDatalist;
	}

	private List<NVCustomerCareDataWrapper> getNearestNVData(Double latitude, Double longitude,
			NNS<NVCustomerCareDataWrapper> nvDeviceDatalist) {
		return nvDeviceDatalist.getLocationInRange(new LatLng(latitude, longitude),
				Length.meter(ConfigUtils.getInteger(NVCustomerCareConstant.NUMBER_OF_METERS)));
	}
	
}

package com.inn.foresight.module.nv.device.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;

import com.inn.foresight.module.nv.app.exceptions.InvalidEncryptionException;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.inn.commons.Symbol;
import com.inn.commons.Validate;
import com.inn.commons.configuration.ConfigUtils;
import org.apache.commons.io.FileUtils;
import com.inn.commons.maps.LatLng;
import com.inn.core.generic.exceptions.application.BusinessException;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.maplayer.service.IGenericMapService;
import com.inn.foresight.module.nv.NVConstant;
import com.inn.foresight.module.nv.app.dao.IDeviceInfoDao;
import com.inn.foresight.module.nv.device.constant.DeviceConstant;
import com.inn.foresight.module.nv.device.dao.INVDeviceDataDao;
import com.inn.foresight.module.nv.device.model.Device;
import com.inn.foresight.module.nv.device.model.NVDeviceData;
import com.inn.foresight.module.nv.device.service.IDeviceService;
import com.inn.foresight.module.nv.device.service.INVDeviceDataService;
import com.inn.foresight.module.nv.device.service.INVDeviceDataTransactionService;
import com.inn.foresight.module.nv.device.util.NVDeviceUtil;
import com.inn.foresight.module.nv.device.wrapper.NVDeviceCountWrapper;
import com.inn.foresight.module.nv.device.wrapper.NVDeviceDataWrapper;
import com.inn.foresight.module.nv.device.wrapper.NVDeviceWrapper;
import com.inn.foresight.module.nv.profile.utils.NVUtilService;
import com.inn.foresight.module.nv.pushnotification.model.PushNotification;
import com.inn.foresight.module.nv.pushnotification.model.PushNotification.ModuleName;
import com.inn.foresight.module.nv.pushnotification.service.IPushNotificationService;
import com.inn.foresight.module.nv.pushnotification.wrapper.PushNotificationWrapper;
import com.inn.foresight.module.nv.report.utils.ReportUtil;
import com.inn.foresight.module.nv.reportgeneration.wrapper.NVReportWrapper;
import com.inn.foresight.module.nv.workorder.dao.INVDeviceWOMappingDao;
import com.inn.product.security.utils.AuthenticationCommonUtil;

/** The Class NVDeviceDataServiceImpl. */
@Service("INVDeviceDataService")
public class NVDeviceDataServiceImpl implements INVDeviceDataService {
	/** The logger. */
	private static Logger logger = LogManager.getLogger(NVDeviceDataServiceImpl.class);

	/** The i device service. */
	@Autowired
	IDeviceService iDeviceService;

	/** The i NV device data dao. */
	@Autowired
	INVDeviceDataDao iNVDeviceDataDao;

	/** The i push notification. */
	@Autowired
	IPushNotificationService pushNotificationService;

	/** The i device info dao. */
	@Autowired
	IDeviceInfoDao iDeviceInfoDao;


	/** The device model map. */
	private static Map<String, Device> deviceModelMap;

	/** The i generic map service. */
	@Autowired
	private IGenericMapService iGenericMapService;


	/** The device WO mapping dao. */
	@Autowired
	private INVDeviceWOMappingDao deviceWOMappingDao;

	@Autowired
	private INVDeviceDataTransactionService nvDeviceDataTransactionService;


	/**
	 * Gets the all device list.
	 *
	 * @param lLimit   the l limit
	 * @param uLimit   the u limit
	 * @param operator the operator
	 * @return the all device list
	 * @throws RestException the rest exception
	 */
	@Override
	public List<NVDeviceDataWrapper> getAllDeviceList(Integer lLimit, Integer uLimit, String operator) {
		List<NVDeviceDataWrapper> nvDeviceList;
		try {
			nvDeviceList = iNVDeviceDataDao.getAllDeviceList(lLimit, uLimit, operator,
					NVDeviceUtil.getFilterValueMap(operator, null, null, null));
		} catch (DaoException e) {
			logger.error("Exception in getAllDeviceList : {}", ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtils.getMessage(e));
		}
		return nvDeviceList;
	}

	/**
	 * Gets the device count.
	 *
	 * @param operator the operator
	 * @return the device count
	 * @throws RestException the rest exception
	 */
	@Cacheable("NVDeviceDataCahce")
	@Override
    public long getDeviceCount(String operator) {
		long deviceCount;
		try {
			deviceCount = iNVDeviceDataDao.getDeviceCount(operator,
					NVDeviceUtil.getFilterValueMap(operator, null, null, null));
		} catch (DaoException e) {
			logger.error("Exception in getDeviceCount :{} ", ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtils.getMessage(e));
		}
		return deviceCount;
	}

	/**
	 * Gets the device count for view port.
	 *
	 * @param northEastLat  the north east lat
	 * @param northEastLong the north east long
	 * @param southWestLat  the south west lat
	 * @param southWestLong the south west long
	 * @param zoomLevel     the zoom level
	 * @param operator      the operator
	 * @return the device count for view port
	 * @throws RestException the rest exception
	 */
	@Override
	public List<NVDeviceCountWrapper> getDeviceCountForViewPort(Double northEastLat, Double northEastLong,
			Double southWestLat, Double southWestLong, Integer zoomLevel, String operator, String userType) {
		List<NVDeviceCountWrapper> nvDeviceDataWrapperList;
		try {
			nvDeviceDataWrapperList = iNVDeviceDataDao.getDeviceCountWrapperForZoomLevel(zoomLevel, operator,
					northEastLat, northEastLong, southWestLat, southWestLong,
					NVDeviceUtil.getFilterValueMap(operator, null, null, userType));
		} catch (DaoException e) {
			logger.error("Exception in getDeviceCountForViewPort : {}", ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtils.getMessage(e));
		}
		return nvDeviceDataWrapperList;
	}

	/**
	 * Gets the device details by device id.
	 *
	 * @param deviceId the device id
	 * @return the device details by device id
	 * @throws RestException the rest exception
	 */
	@Override
	public NVDeviceDataWrapper getDeviceDetailsByDeviceId(String deviceId) {
		NVDeviceDataWrapper nvDeviceData = null;
		try {
			nvDeviceData = iNVDeviceDataDao.getNVDeviceDataWrapperByDeviceId(deviceId);
			Device deviceSpecification = getDeviceSpecification(nvDeviceData.getModel());
			nvDeviceData.setSpecification(deviceSpecification);
		} catch (DaoException e) {
			logger.error("Exception in getDeviceDetailsByDeviceId : {}", ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtils.getMessage(e));
		}
		return nvDeviceData;
	}

	/**
	 * Gets the device specification.
	 *
	 * @param model the model
	 * @return the device specification
	 */
	private Device getDeviceSpecification(String model) {
		if (deviceModelMap == null || deviceModelMap.isEmpty()) {
			logger.info("deviceModelMap:{}",deviceModelMap);
			deviceModelMap = iDeviceService.getMapOfModelAndDevice();
		}
		return deviceModelMap.get(model);
	}

	/**
	 * Gets the device inside cluster.
	 *
	 * @param northEastLat  the north east lat
	 * @param northEastLong the north east long
	 * @param southWestLat  the south west lat
	 * @param southWestLong the south west long
	 * @param operator      the operator
	 * @param geographyId   the geography id
	 * @return the device inside cluster
	 * @throws RestException the rest exception
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List<NVDeviceDataWrapper> getDeviceInsideViewPort(Double northEastLat, Double northEastLong,
															 Double southWestLat, Double southWestLong, String operator, String geographyId, String userType) {
		List<NVDeviceDataWrapper> nvDeviceDataWrapperList;
		try {
			Map<String, List> filter;
			if (geographyId != null) {
				filter = NVDeviceUtil.getFilterValueMap(operator, "geographyL4", geographyId, userType);
			} else {
				filter = NVDeviceUtil.getFilterValueMap(operator, null, null, userType);
			}
			nvDeviceDataWrapperList = iNVDeviceDataDao.getDeviceInsideViewPort(operator, northEastLat, northEastLong,
					southWestLat, southWestLong, geographyId, filter);
		} catch (DaoException e) {
			logger.error("Exception in getDeviceInsideViewPort :{} ", ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtils.getMessage(e));
		}
		return nvDeviceDataWrapperList;
	}

	/**
	 * Creates the NV device data.
	 *
	 * @param nvDeviceData the nv device data
	 * @return the NV device data
	 * @throws RestException the rest exception
	 */
	@Override
	public NVDeviceData createNVDeviceData(NVDeviceData nvDeviceData) {
		try {
			nvDeviceData = iNVDeviceDataDao.create(nvDeviceData);
		} catch (DaoException e) {
			logger.error("Exception in createNVDeviceData :{} ", ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtils.getMessage(e));
		}
		return nvDeviceData;
	}

	/**
	 * Find NV device data by device id.
	 *
	 * @param deviceId the device id
	 * @return the NV device data
	 * @throws RestException the rest exception
	 */
	@Override
	public NVDeviceData findNVDeviceDataByDeviceId(String deviceId) {
		NVDeviceData nvDeviceData;
		
		nvDeviceData = iNVDeviceDataDao.getDeviceByDeviceId(deviceId);
		
		return nvDeviceData;
	}

	/**
	 * Update NV device data.
	 *
	 * @param nvDeviceData the nv device data
	 * @return the NV device data
	 * @throws RestException the rest exception
	 */
	@Override
	public NVDeviceData updateNVDeviceData(NVDeviceData nvDeviceData) {
		NVDeviceData nvDevice;
		try {
			nvDevice = iNVDeviceDataDao.update(nvDeviceData);
		} catch (DaoException e) {
			logger.error("Exception in updateNVDeviceData :{} ", ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtils.getMessage(e));
		}
		return nvDevice;
	}

	/**
	 * Gets the device list inside cluster.
	 *
	 * @param cluster  the cluster
	 * @param operator the operator
	 * @return the device list inside cluster
	 * @throws RestException the rest exception
	 */
	@Override
	public List<NVDeviceDataWrapper> getDeviceListInsideCluster(String cluster, String operator, String userType) {
		List<NVDeviceDataWrapper> nvDeviceDataWrapperList;
		try {
			nvDeviceDataWrapperList = iNVDeviceDataDao.getDeviceListInsideCluster(cluster, operator,
					NVDeviceUtil.getFilterValueMap(operator, null, null, userType));
		} catch (DaoException e) {
			logger.error("Exception in getting device list inside luster :{} ", ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtils.getMessage(e));
		}
		return nvDeviceDataWrapperList;
	}

	/**
	 * Gets the device count for table view.
	 *
	 * @param northEastLat   the north east lat
	 * @param northEastLong  the north east long
	 * @param southWestLat   the south west lat
	 * @param southWestLong  the south west long
	 * @param operator       the operator
	 * @param geographyLevel the geography level
	 * @param geographyId    the geography id
	 * @return the device count for table view
	 * @throws RestException the rest exception
	 */
	@Override
	public List<NVDeviceCountWrapper> getDeviceCountForTableView(Double northEastLat, Double northEastLong,
																 Double southWestLat, Double southWestLong, String operator, String geographyLevel, String geographyId) {

		List<NVDeviceCountWrapper> nvDeviceDataWrapperList;
		try {
			nvDeviceDataWrapperList = iNVDeviceDataDao.getDeviceCountForTableView(northEastLat, northEastLong,
					southWestLat, southWestLong, NVDeviceUtil.getFilterValueMap(operator, null, null, null),
					geographyLevel);
		} catch (DaoException e) {
			logger.error("Exception in getDeviceCountForTableView : {}", ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtils.getMessage(e));
		}
		return nvDeviceDataWrapperList;
	}

	/**
	 * Gets the device list for WO.
	 *
	 * @param woStartTime    the wo start time
	 * @param geographyLevel the geography level
	 * @param geographyId    the geography id
	 * @param lLimit         the l limit
	 * @param uLimit         the u limit
	 * @return the device list for WO
	 * @throws RestException the rest exception
	 */
	@Override
	public List<NVDeviceDataWrapper> getDeviceListForWO(Long woStartTime, String geographyLevel, Integer geographyId,
														Integer lLimit, Integer uLimit) {
		List<NVDeviceDataWrapper> deviceDatas = null;
		try {
			logger.info("Going to getDeviceListForWO startTime : {}, geographyLevel : {} & geographyId : {}",
					woStartTime, geographyLevel, geographyId);

			deviceDatas = iNVDeviceDataDao.getDeviceListForWO(new Date(woStartTime),
					NVDeviceUtil.getFilterAndValueMap(geographyLevel, geographyId, null, false), lLimit, uLimit);
		} catch (DaoException e) {
			logger.error("Exception in get device list for WO :{} ", ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtils.getMessage(e));
		}
		return deviceDatas;
	}

	/**
	 * Gets the NV device data.
	 *
	 * @param nvDeviceDataIds the nv device data ids
	 * @return the NV device data
	 * @throws RestException the rest exception
	 */
	@Override
	public List<NVDeviceData> getNVDeviceData(List<Integer> nvDeviceDataIds) {
		List<NVDeviceData> nvDeviceDatas = null;
		try {
			Validate.checkNotEmpty(nvDeviceDataIds, "Get empty NVDeviceData Id list");
			logger.info("Going to getNVDeviceData nvDeviceDataIds size : {}", nvDeviceDataIds.size());
			List<List<Integer>> nvDeviceDataIdSplittedList = ListUtils.partition(nvDeviceDataIds, NVConstant.MAX_PARTITION_LIMIT);
			for (List<Integer> nvDeviceDataIdSubList : nvDeviceDataIdSplittedList) {
				List<NVDeviceData> nvDeviceDataSubList = iNVDeviceDataDao.getNVDeviceData(nvDeviceDataIdSubList);
				if (nvDeviceDatas == null) {
					nvDeviceDatas = new ArrayList<>();
				}
				nvDeviceDatas.addAll(nvDeviceDataSubList);
			}
			logger.info("Done getNVDeviceData nvDeviceDatas size : {}",
					nvDeviceDatas != null ? nvDeviceDatas.size() : ForesightConstants.ZERO);
		} catch (DaoException e) {
			logger.error("Exception in getDeviceListInsideCluster :{} ", ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtils.getMessage(e));
		}
		return nvDeviceDatas;
	}

	@Override
	public List<NVDeviceData> getNVDeviceDataByDeviceInfo(List<Integer> deviceInfoIds) {
		List<NVDeviceData> nvDeviceDatas = null;
		try {
			Validate.checkNotEmpty(deviceInfoIds, "Get empty deviceInfoIds Id list");
			logger.info("Going to getNVDeviceData deviceInfoIds size : {}", deviceInfoIds.size());
			List<List<Integer>> deviceInfoIdSplittedList = ListUtils.partition(deviceInfoIds, NVConstant.MAX_PARTITION_LIMIT);
			for (List<Integer> deviceInfoIdSubList : deviceInfoIdSplittedList) {
				List<NVDeviceData> nvDeviceDataSubList = iNVDeviceDataDao.getNVDeviceDataByDeviceInfo(
						deviceInfoIdSubList);
				if (nvDeviceDatas == null) {
					nvDeviceDatas = new ArrayList<>();
				}
				nvDeviceDatas.addAll(nvDeviceDataSubList);
			}
			logger.info("Done getNVDeviceData nvDeviceDatas size : {}",
					nvDeviceDatas != null ? nvDeviceDatas.size() : ForesightConstants.ZERO);
		} catch (DaoException e) {
			logger.error("Exception in getDeviceListInsideCluster :{} ", ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtils.getMessage(e));
		}
		return nvDeviceDatas;
	}

	/**
	 * Gets the device count list for WO.
	 *
	 * @param woStartTime    the wo start time
	 * @param geographyLevel the geography level
	 * @param geographyId    the geography id
	 * @return the device count list for WO
	 * @throws RestException the rest exception
	 */
	@Override
	public Long getDeviceCountListForWO(Long woStartTime, String geographyLevel, Integer geographyId) {
		Long deviceCount;
		try {
			logger.info("Going to getDeviceListForWO startTime : {}, geographyLevel : {} & geographyId : {}",
					woStartTime, geographyLevel, geographyId);
			deviceCount = iNVDeviceDataDao.getDeviceCountListForWO(new Date(woStartTime),
					NVDeviceUtil.getFilterAndValueMap(geographyLevel, geographyId, null, false));
		} catch (DaoException e) {
			logger.error("Exception in getDeviceCount :{} ", ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtils.getMessage(e));
		}
		return deviceCount;
	}

	/**
	 * Search NV devices.
	 *
	 * @param northEastLat  the north east lat
	 * @param northEastLong the north east long
	 * @param southWestLat  the south west lat
	 * @param southWestLong the south west long
	 * @param operator      the operator
	 * @param zoomLevel     the zoom level
	 * @return the list
	 * @throws RestException the rest exception
	 */
    @Cacheable("NVDeviceDataDetailCahce")
    @Override
   public List<NVDeviceDataWrapper> searchNVDevices(String operator, Integer zoomLevel, String userType) {
     logger.error("inside the method searchNVDevices {} operator {}",zoomLevel,operator);
		List<NVDeviceDataWrapper> nvDeviceDataWrapperList;
		try {
			nvDeviceDataWrapperList = iNVDeviceDataDao.searchNVDevices(operator, null, null,
					null, null, NVDeviceUtil.getFilterValueMap(operator, null, null, userType),
					zoomLevel);
		} catch (DaoException e) {
			logger.error("Exception in searchNVDevices :{} ", ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtils.getMessage(e));
		}
		return NVDeviceUtil.getGridWiseDevices(nvDeviceDataWrapperList, zoomLevel);
	}

	
	@Override
	public  List<NVDeviceDataWrapper> searchNVDeviceDetail(Double northEastLat, Double northEastLong, Double southWestLat,
			Double southWestLong, String operator, Integer zoomLevel, String userType) {
		List<NVDeviceDataWrapper> nvDeviceDataWrapperList;

		try {
			nvDeviceDataWrapperList = iNVDeviceDataDao.searchNVDevices(operator, northEastLat, northEastLong,
					southWestLat, southWestLong, NVDeviceUtil.getFilterValueMap(operator, null, null, userType),
					zoomLevel);
		} catch (DaoException e) {
			logger.error("Exception in searchNVDevices :{} ", ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtils.getMessage(e));
		}
		return NVDeviceUtil.getGridWiseDevices(nvDeviceDataWrapperList,zoomLevel);
	}
	/**
	 * Creates the.
	 *
	 * @param deviceId the device id
	 * @return the push notification
	 * @throws RestException the rest exception
	 */
	@Override
	@Transactional
	public PushNotificationWrapper refreshDeviceData(String deviceId) {
		logger.info("Going to refresh NV device Status");
		PushNotificationWrapper notification = null;
		try {
			if (deviceId != null) {
				notification = new PushNotificationWrapper(deviceId, ModuleName.PULSE,
						DeviceConstant.PAYLOAD_FOR_DEVICE_UPDATE_NOTIFICATION);
				notification = pushNotificationService.createNotification(notification);
			} else {
				logger.error("Recieved device id as :{} ", deviceId);
			}
		} catch (RestException e) {
			logger.error("Exception in refresh NV Device Status :{} ", ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtils.getMessage(e));
		}
		return notification;
	}

	/**
	 * Update recharge status.
	 *
	 * @param deviceDataWrappper the device data wrappper
	 * @return the string
	 */
	@Transactional
	@Override
	public String updateRechargeStatus(NVDeviceDataWrapper deviceDataWrappper) {
		logger.info("Going to update Recharge Status");
		try {
			NVDeviceData nvDevice = iNVDeviceDataDao.getDeviceByDeviceId(deviceDataWrappper.getDeviceId());
			nvDevice.setRechargePlan(deviceDataWrappper.getRechargePlan());
			nvDevice.setValidity(deviceDataWrappper.getValidity());
			nvDevice.setLastRechargeDate(
					(Date) isValidDeviceData(deviceDataWrappper.getLastRechargeDate(), nvDevice.getLastRechargeDate()));
			nvDevice.setNextRechargeDate(
					(Date) isValidDeviceData(deviceDataWrappper.getNextRechargeDate(), nvDevice.getNextRechargeDate()));
			Integer deviceId = iNVDeviceDataDao.update(nvDevice)
					.getId();
			logger.info("Recharge Status Updated for deviceId: {}", deviceId);
			return ForesightConstants.SUCCESS_JSON;
		} catch (Exception e) {
			logger.error("Error while updating Recharge Status: {}", Utils.getStackTrace(e));
			return ForesightConstants.FAILURE_JSON;
		}
	}

	/**
	 * Update device data.
	 *
	 * @param requestWrapper the request wrapper
	 * @return the string
	 */
	@Override
	public String updateDeviceData(String requestWrapper) {
		logger.info("Going to  update Device Data");
		Boolean isGcmAlgorithm = null;
		try {
			isGcmAlgorithm = AuthenticationCommonUtil.isGCMAlgorithm(requestWrapper);
			NVDeviceDataWrapper nvDeviceDataWrapper = new Gson().fromJson(
					AuthenticationCommonUtil.checkForValueDecryption(requestWrapper), NVDeviceDataWrapper.class);
			String[] geographies = getGeographiesForDeviceRequest(nvDeviceDataWrapper);
			persistNVDeviceData(nvDeviceDataWrapper, geographies, null);
			logger.info("Done update Device Data");
			return AuthenticationCommonUtil.checkForValueEncryption(DeviceConstant.SUCCESS_JSON, isGcmAlgorithm);
		} catch (Exception e) {
			logger.error("Error while updating device Data: {}", Utils.getStackTrace(e));
			return AuthenticationCommonUtil.checkForValueEncryption(DeviceConstant.FAILURE_JSON, isGcmAlgorithm);
		}
	}

	/**
	 * Gets the device operator.
	 *
	 * @param mcc the mcc
	 * @param mnc the mnc
	 * @return the device operator
	 */
	public String getDeviceOperator(Integer mcc, Integer mnc) {
		String operator = null;
		try {
			if (NVUtilService.getMccMncOperatorMap() != null) {
				operator = NVUtilService.getMccMncOperatorMap().get(mcc + Symbol.UNDERSCORE_STRING + mnc);
			}
		} catch (Exception e) {
			logger.error("Error in getDeviceOperator : {}", ExceptionUtils.getStackTrace(e));
		}
		logger.info("Operator for device : {} ,mcc : {} , mnc : {} ", operator, mcc, mnc);
		return operator;
	}

	/**
	 * Gets the NV device data.
	 *
	 * @param wrapper the wrapper
	 * @return the NV device data
	 * @throws DaoException the dao exception
	 */
	private NVDeviceData getNVDeviceData(NVDeviceDataWrapper wrapper) {
		NVDeviceData nvDevice = null;
		logger.info("Going to get NVDevice for deviceID{} ", wrapper.getDeviceId());
		try {
			nvDevice = iNVDeviceDataDao.getDeviceByDeviceId(wrapper.getDeviceId());
		} catch (NoResultException | EmptyResultDataAccessException e) {
			logger.info("New NVDevice Object is Created");
			nvDevice = new NVDeviceData();
			nvDevice.setCreationTime(new Date());
		}
		return nvDevice;
	}

	/**
	 * Checks if is valid device data.
	 *
	 * @param newData the new data
	 * @param oldData the old data
	 * @return the object
	 */
	private Object isValidDeviceData(Object newData, Object oldData) {
		if (newData instanceof Long) {
			newData = new Date((long) newData);
		}
		return newData != null ? newData : oldData;
	}


	/**
	 * Gets the geographies for device request.
	 *
	 * @param wrapper the wrapper
	 * @return the geographies for device request
	 */
	@Override
	public String[] getGeographiesForDeviceRequest(NVDeviceDataWrapper wrapper) {
		logger.info("Inside getGeographiesForDeviceRequest");
		String[] geographies = null;
		if (NVDeviceUtil.isValidNumber(wrapper.getLatitude()) && NVDeviceUtil.isValidNumber(wrapper.getLongitude())) {
			logger.info("Got Valid Lat & Long");
			geographies = iGenericMapService.getAllGeographyName(new LatLng(wrapper.getLatitude(), wrapper.getLongitude()));
		}
		logger.info("Done getGeographiesForDeviceRequest result : {}",
				geographies != null ? new Gson().toJson(geographies) : geographies);
		return geographies;
	}

	/**
	 * get filters for getting device data
	 * @return
	 */
	@Override
	public Map<String, List<String>> getFilters() {
		return iNVDeviceDataDao.getFilters();
	}


	/**
	 * Gets the device recharge detail.
	 *
	 * @return the device recharge detail
	 * @throws RestException the rest exception
	 */
	@Override
	public NVReportWrapper getDeviceRechargeDetail() {
		logger.info("Inside getDeviceRechargeDetail");
		try {
			List<NVDeviceDataWrapper> nvDeviceData = iNVDeviceDataDao.getDeviceRechargeDetail(
					DeviceConstant.RECHARGE_DAY_DIFFERENCE);
			return createDeviceRechargeDetailReport(nvDeviceData);

		} catch (Exception e) {
			logger.error("Error while getting data for device recharge detail: {}", Utils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}

	/**
	 * Gets the device recharge detail count.
	 *
	 * @return the device recharge detail count
	 * @throws RestException the rest exception
	 */
	@Override
	public Long getDeviceRechargeDetailCount() {
		try {
			logger.info("Inside getAllDeviceGroupsCount");
			Long deviceDataCount = iNVDeviceDataDao.getDeviceRechargeDetailCount(
					DeviceConstant.RECHARGE_DAY_DIFFERENCE);
			logger.info("Done getDeviceRechargeDetailCount with result : {}", deviceDataCount);
			return deviceDataCount;
		} catch (Exception e) {
			logger.error("Exception in getDeviceRechargeDetailCount : {} ", ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}

	/**
	 * Creates the header for device recharge detail report.
	 *
	 * @param csvBuilder the csv builder
	 */
	private void createHeaderForDeviceRechargeDetailReport(StringBuilder csvBuilder) {
		String testHeader = "Make,Model,IMEI,IMSI,Probe-ID,Location,City,Mobile Number,Application Version,Last Recharge Date,Recharge Plan,Recharge Validity,Next Recharge Date";
		csvBuilder.append(testHeader);
		logger.info("Header written successfully in Device Recharge Detail Report");
		csvBuilder.append(ForesightConstants.NEW_LINE);
	}

	/**
	 * Creates the device recharge detail report.
	 *
	 * @param dataWrapper the data wrapper
	 * @return the report wrapper
	 * @throws Exception the exception
	 */
	private NVReportWrapper createDeviceRechargeDetailReport(List<NVDeviceDataWrapper> dataWrapper) throws Exception {
		NVReportWrapper reportWrapper = new NVReportWrapper();
		try {
			if (dataWrapper != null && !dataWrapper.isEmpty()) {
				String rechargeDetailCSV = writeDeviceRechargeDetailReportIntoCSV(dataWrapper);
				String fileName = ConfigUtils.getString(DeviceConstant.DEVICE_RECHARGE_DETAIL_REPORT_PATH)
						+ DeviceConstant.PULSE + ForesightConstants.UNDERSCORE
						+ DeviceConstant.DEVICE_RECHARGE_DETAIL_REPORT + ForesightConstants.CSV_EXTENSION;
				logger.info("Going to write file to local path {}", fileName);
				ReportUtil.writeStringIntoCsvFile(rechargeDetailCSV, fileName);
				File file = new File(fileName);
				reportWrapper.setFile(FileUtils.readFileToByteArray(file));
				reportWrapper.setReportName(fileName);
				FileUtils.deleteQuietly(file);
				logger.info("Report created at: {}", fileName);
			} else {
				throw new BusinessException("Invalid argument for recharge detail");
			}
		} catch (Exception e) {
			logger.error("Error while getting device data for create device recharge report: {}",
					Utils.getStackTrace(e));
			throw e;
		}
		return reportWrapper;
	}

	/**
	 * Write device recharge detail report into CSV.
	 *
	 * @param dataList the data list
	 * @return the string
	 */
	private String writeDeviceRechargeDetailReportIntoCSV(List<NVDeviceDataWrapper> dataList) {
		StringBuilder csvBuilder = new StringBuilder();
		logger.info("Inside writeDeviceRechargeDetailReportIntoCSV");
		createHeaderForDeviceRechargeDetailReport(csvBuilder);

		if (dataList != null && !dataList.isEmpty()) {
			for (NVDeviceDataWrapper dataWrapper : dataList) {
				appendDataToCsv(dataWrapper.getMake(), csvBuilder);
				appendDataToCsv(dataWrapper.getModel(), csvBuilder);
				appendDataToCsv(dataWrapper.getImei(), csvBuilder);
				appendDataToCsv(dataWrapper.getImsi(), csvBuilder);
				appendDataToCsv(dataWrapper.getGroupId(), csvBuilder);
				appendDataToCsv(dataWrapper.getAddress() != null ? dataWrapper.getAddress()
						.replace(ForesightConstants.COMMA,
								ForesightConstants.UNDERSCORE)
						: null, csvBuilder);
				appendDataToCsv(dataWrapper.getGeographyName() != null ? dataWrapper.getGeographyName() : null,
						csvBuilder);
				appendDataToCsv(dataWrapper.getMsisdn(), csvBuilder);
				appendDataToCsv(dataWrapper.getAppVersion(), csvBuilder);
				appendDataToCsv(new Date(dataWrapper.getLastRechargeDate()), csvBuilder);
				appendDataToCsv(dataWrapper.getRechargePlan(), csvBuilder);
				appendDataToCsv(dataWrapper.getValidity(), csvBuilder);
				csvBuilder.append(getValueForCell(new Date(dataWrapper.getNextRechargeDate())));
				csvBuilder.append(ForesightConstants.NEW_LINE);
			}
		}
		return csvBuilder.toString();
	}

	/**
	 * Append data to csv.
	 *
	 * @param value      the value
	 * @param csvBuilder the csv builder
	 */
	private void appendDataToCsv(Object value, StringBuilder csvBuilder) {
		csvBuilder.append(getValueForCell(value));
		csvBuilder.append(ForesightConstants.COMMA);
	}

	/**
	 * Gets the value for cell.
	 *
	 * @param value the value
	 * @return the value for cell
	 */
	private Object getValueForCell(Object value) {
		String stringValue = String.valueOf(value);
		if (value == null || stringValue.equalsIgnoreCase("null")) {
			return ForesightConstants.HIPHEN;
		} else {
			return stringValue;
		}
	}

	/**
	 * Gets the device list for work order.
	 *
	 * @param woStartTime    the wo start time
	 * @param woEndTime      the wo end time
	 * @param geographyLevel the geography level
	 * @param geographyId    the geography id
	 * @param lLimit         the l limit
	 * @param uLimit         the u limit
	 * @return the device list for work order
	 * @throws RestException the rest exception
	 * @throws Exception
	 */
	@Override
	public List<NVDeviceDataWrapper> getDeviceListForWorkOrder(Long woStartTime, Long woEndTime, String geographyLevel,
															   Integer geographyId, Integer lLimit, Integer uLimit) throws Exception {
		List<NVDeviceDataWrapper> deviceDatas = null;
		try {
			logger.info(
					"Going to getDeviceListForWorkOrder startTime : {}, endTime : {}, geographyLevel : {} & geographyId : {}",
					woStartTime, woEndTime, geographyLevel, geographyId);

			deviceDatas = deviceWOMappingDao.getDeviceListForWorkOrder(new Date(woStartTime), new Date(woEndTime),
					NVDeviceUtil.getFilterAndValueMap(geographyLevel, geographyId, null, false), lLimit, uLimit);
		} catch (RestException e) {
			logger.error("Exception in getDeviceListForWO :{} ", ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtils.getMessage(e));
		}
		return deviceDatas;
	}

	/**
	 * Gets the device list count for work order.
	 *
	 * @param woStartTime    the wo start time
	 * @param woEndTime      the wo end time
	 * @param geographyLevel the geography level
	 * @param geographyId    the geography id
	 * @return the device list count for work order
	 */
	@Override
	public Long getDeviceListCountForWorkOrder(Long woStartTime, Long woEndTime, String geographyLevel,
											   Integer geographyId) throws Exception {
		Long deviceDataCount = null;
		try {
			logger.info(
					"Going to getDeviceListForWorkOrder startTime : {}, endTime : {}, geographyLevel : {} & geographyId : {}",
					woStartTime, woEndTime, geographyLevel, geographyId);

			deviceDataCount = deviceWOMappingDao.getDeviceListCountForWorkOrder(new Date(woStartTime),
					new Date(woEndTime), NVDeviceUtil.getFilterAndValueMap(geographyLevel, geographyId, null, false));
		} catch (RestException e) {
			logger.error("Exception in getDeviceListForWO :{} ", ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtils.getMessage(e));
		}
		return deviceDataCount;
	}


	@Override
	@Transactional
	public String updateNVDeviceData(String wrapper) {
		logger.info("Going to update NVDevice Data wrapper : {}", wrapper);
		String responseToReturn = null;
		NVDeviceDataWrapper nvDataWrapper = null;
		try {

			nvDataWrapper = new Gson().fromJson(AuthenticationCommonUtil.checkForValueDecryption(wrapper), NVDeviceDataWrapper.class);
			logger.info("decrypted  nvDataWrapper : {}", nvDataWrapper);
		} catch (InvalidEncryptionException e) {
			logger.error("InvalidEncryptionException in updateNVDeviceData :{} ", ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			logger.error("Exception in updateNVDeviceData :{} ", ExceptionUtils.getStackTrace(e));
		}

		if (nvDataWrapper != null) {
			if (nvDataWrapper.getDeviceId() != null && nvDataWrapper.getPushNotificationId() != null) {
				PushNotification pushNotification = pushNotificationService.updateLiveLocationDetail(nvDataWrapper);
				NVDeviceData nvDeviceData = getDeviceByDeviceId(nvDataWrapper.getDeviceId());
				String[] geographies = getGeographiesForDeviceRequest(nvDataWrapper);
				nvDeviceDataTransactionService.updateNVDeviceDataAndGetGeographies(nvDataWrapper, nvDeviceData, geographies, pushNotification);
				responseToReturn = pushNotificationService.updateLiveLocationPushNotification(nvDataWrapper, geographies, pushNotification);
			} else {
				logger.error(DeviceConstant.RESULT_DEVICE_ID_NULL);
				responseToReturn = DeviceConstant.RESULT_DEVICE_ID_NULL;
			}
		} else {
			logger.error("nvDataWrapper is null");
			responseToReturn = DeviceConstant.RESULT_FAILURE;
		}
		return responseToReturn;
	}


	@Override
	public NVDeviceData getDeviceByDeviceId(String deviceId) {
		try {
			logger.info("Going to get NVDevice for deviceid {} ", deviceId);
			return iNVDeviceDataDao.getDeviceByDeviceId(deviceId);
		} catch (DaoException e) {
			logger.error("Exception in getDevicesByDeviceWorkOrderId :{} ", ExceptionUtils.getStackTrace(e));
		}
		return null;
	}


	@Override
	public List<Integer> getDeviceListForUser(Integer userId) {
		logger.info("Going to get NV Device List For user{} ", userId);
		List<Integer> deviceList = new ArrayList<>();
		try {
			deviceList = iNVDeviceDataDao.getDeviceListForUser(userId);
		} catch (Exception exception) {
			logger.error("Error in getting NV Device list for user {} Exception {} ", userId, Utils.getStackTrace(exception));
		}
		return deviceList;

	}

	@Override
	public List<NVDeviceDataWrapper> getStealthDeviceListForGeography(String geographyLevel,
																	  Integer geographyId, Integer lLimit, Integer uLimit) {
		List<NVDeviceDataWrapper> deviceDatas = null;
		try {
			logger.info("Going to getStealthDeviceListForGeography, geographyLevel : {} & geographyId : {}",
					geographyLevel, geographyId);

			deviceDatas = iNVDeviceDataDao.getDeviceListForWO(null,
					NVDeviceUtil.getFilterAndValueMap(geographyLevel, geographyId, null, true), lLimit, uLimit);
		} catch (DaoException e) {
			logger.error("Exception in getStealthDeviceListForGeography :{} ", ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtils.getMessage(e));
		}
		return deviceDatas;
	}

	@Override
	public Long getStealthDeviceListCountForGeography(String geographyLevel, Integer geographyId) {
		Long deviceCount;
		try {
			logger.info("Going to getStealthDeviceListCountForGeography  geographyLevel : {} & geographyId : {}"
					, geographyLevel, geographyId);
			deviceCount = iNVDeviceDataDao.getDeviceCountListForWO(null,
					NVDeviceUtil.getFilterAndValueMap(geographyLevel, geographyId, null, true));
		} catch (DaoException e) {
			logger.error("Exception in getStealthDeviceListCountForGeography :{} ", ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtils.getMessage(e));
		}
		return deviceCount;
	}


	@Override
	public NVDeviceData persistNVDeviceData(NVDeviceDataWrapper wrapper, String[] geographies, String operator) {
		logger.info("Going to  persist NVDevice Data for device id : {} ", wrapper.getDeviceId());
		if (wrapper.getDeviceId() == null) {
			logger.error("Device id is null ,NVdevice data not persisted ");
			return null;
		}
		if (operator == null) {
			operator = getDeviceOperator(wrapper.getMcc(), wrapper.getMnc());
		}
		NVDeviceData nvDevice = null;
		try {
			nvDevice = getNVDeviceData(wrapper);
			
			if (nvDevice.getId() == null || (wrapper.getLatitude() != null && wrapper.getLongitude() != null)) {
               
				nvDevice = nvDeviceDataTransactionService.createNVDevice(wrapper, geographies, operator, nvDevice);
				logger.info("Going to Persist NVDevice Object");
				nvDevice= nvDeviceDataTransactionService.create(nvDevice);
				logger.info("Done persistNVDeviceData");

			} else {
				logger.error("NVdevice data not persist for deviceId:  {} ,nvDeviceID_pk: {} ,latituded : {} longitude: {}",
						wrapper.getDeviceId(), nvDevice.getId(), wrapper.getLatitude(),wrapper.getLongitude());
			}
		} catch (DaoException e) {
			logger.error("Exception in persistNVDeviceData : {}", e.getMessage());
		}
		return nvDevice;
	}

	/**
	 * get device data using different filters added in wrapper
	 * @param wrapper
	 * @param lLimit
	 * @param uLimit
	 * @return
	 */
	@Override
	public List<NVDeviceDataWrapper> getDeviceData(NVDeviceWrapper wrapper, Integer lLimit, Integer uLimit) {
		return iNVDeviceDataDao.getDeviceData(wrapper, lLimit, uLimit);
	}

	/**
	 * get device data count using different filters added in wrapper
	 * @param wrapper
	 * @return
	 */
	@Override
	public Long getDeviceDataCount(NVDeviceWrapper wrapper) {
		return iNVDeviceDataDao.getDeviceDataCount(wrapper);
	}

}

package com.inn.foresight.module.nv.workorder.stealth.kpi.dao.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.inn.commons.lang.MapUtils;
import com.inn.commons.lang.NumberUtils;
import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.module.nv.device.constant.DeviceConstant;
import com.inn.foresight.module.nv.device.model.NVDeviceData;
import com.inn.foresight.module.nv.device.wrapper.NVDeviceDataWrapper;
import com.inn.foresight.module.nv.device.wrapper.NVDeviceGroupWrapper;
import com.inn.foresight.module.nv.workorder.stealth.constants.StealthConstants;
import com.inn.foresight.module.nv.workorder.stealth.kpi.dao.IDriveTestKPIDao;
import com.inn.foresight.module.nv.workorder.stealth.kpi.model.DriveTestKPI;
import com.inn.product.systemconfiguration.utils.SystemConfigurationUtils;

/** The Class DriveTestKPIDaoImpl. */
@Repository("DriveTestKPIDaoImpl")
public class DriveTestKPIDaoImpl extends HibernateGenericDao<Integer, DriveTestKPI> implements IDriveTestKPIDao {

	/** The logger. */
	private Logger logger = LogManager.getLogger(DriveTestKPIDaoImpl.class);
	
	/**
	 * Instantiates a new drive test KPI dao impl.
	 * @param type the type
	 */
	public DriveTestKPIDaoImpl() {
		super(DriveTestKPI.class);
	}

	/**
	 * Gets the KPI data.
	 *
	 * @param startDate the start date
	 * @param endDate the end date
	 * @param kpi the kpi
	 * @param threshold the threshold
	 * @param uLimit the u limit
	 * @param lLimit the l limit
	 * @param filterAndValueMap the filter and value map
	 * @return the KPI data
	 * @throws DaoException the dao exception
	 */
	@Override
	public List<NVDeviceDataWrapper> getKPIData(Long startDate, Long endDate, String kpi, Double threshold,
			Integer uLimit, Integer lLimit, Map<String, List> filterMap) {
		logger.info("Going to getDeviceData for  startDate {}, endDate {}, kpi {},threshold {}, uLimit {}, uLimit {}", startDate, endDate,
				kpi, threshold,uLimit,lLimit);
		List<NVDeviceDataWrapper> deviceData = new ArrayList<>();
		try {
			Query query = getEntityManager().createNamedQuery(
					StealthConstants.GET_DEVICE_DATA_BETWEEN_STARTDATE_AND_ENDDATE);
			query.setParameter(StealthConstants.START_DATE, new Timestamp(startDate));
			query.setParameter(StealthConstants.END_DATE, new Timestamp(endDate));
			enableFilters(filterMap);
			setPaginationLimits(uLimit, lLimit, query);
			deviceData = query.getResultList();
			disableFilters(filterMap);
			logger.info("result list size : {}", deviceData.size());
		} catch (Exception ex) {
			logger.info("Error occur while getDeviceData,err msg {}", ExceptionUtils.getStackTrace(ex));
		}
		logger.info("Returning result from getDeviceData with size {}", deviceData.size());
		return deviceData;
	}
	

	/**
	 * Gets the failure data.
	 *
	 * @param startDate the start date
	 * @param endDate the end date
	 * @param kpi the kpi
	 * @param threshold the threshold
	 * @param uLimit the u limit
	 * @param lLimit the l limit
	 * @return the failure data
	 * @throws DaoException the dao exception
	 */
	@Override
	public List<NVDeviceDataWrapper> getFailureData(Long startDate, Long endDate, String kpi, Double threshold,
			Integer uLimit, Integer lLimit) {
		logger.info("Going to getFailureDeviceData for  startDate {}, endDate {}, kpi {},threshold {}, uLimit {}, uLimit {}", startDate, endDate,
				kpi, threshold,uLimit,lLimit);
		List<NVDeviceDataWrapper> failureDeviceData = new ArrayList<>();
		try {
			Query query = getEntityManager().createNamedQuery(
					StealthConstants.GET_FAILURE_DEVICE_DATA_BETWEEN_STARTDATE_AND_ENDDATE);
			query.setParameter(StealthConstants.START_DATE, new Timestamp(startDate));
			query.setParameter(StealthConstants.END_DATE, new Timestamp(endDate));
			setPaginationLimits(uLimit, lLimit, query);
			failureDeviceData = query.getResultList();

			logger.info("result list size : {}", failureDeviceData.size());
		} catch (Exception ex) {
			logger.info("Error occur while getFailureDeviceData,err msg {}", ExceptionUtils.getStackTrace(ex));
		}
		logger.info("Returning result from getFailureDeviceData with size {}", failureDeviceData.size());
		return failureDeviceData;
	}

	/**
	 * Gets the failure device remark.
	 *
	 * @param startDate the start date
	 * @param endDate the end date
	 * @param deviceId the device id
	 * @return the failure device remark
	 */
	@Override
	public NVDeviceDataWrapper getFailureDeviceRemark(Long startDate, Long endDate, String deviceId){
		NVDeviceDataWrapper dataWrapper = null;
		try {
			Query query = getEntityManager().createNamedQuery(
					StealthConstants.GET_FAILURE_DEVICE_REMARK);
			query.setParameter(StealthConstants.ID,deviceId);
			query.setParameter(StealthConstants.START_DATE, new Timestamp(startDate));
			query.setParameter(StealthConstants.END_DATE, new Timestamp(endDate));
			query.setMaxResults(DeviceConstant.MAX_RESULT);
			 dataWrapper = (NVDeviceDataWrapper) query.getSingleResult();
			logger.info("result : {}", dataWrapper.getRemark());
		} catch (Exception ex) {
			logger.info("Error occur while getFailureDeviceData,err msg {}", ExceptionUtils.getStackTrace(ex));
		}
		return dataWrapper;
	}
	
	/**
	 * Sets the pagination limits.
	 *
	 * @param ulimit the upper limit
	 * @param llimit the lower limit
	 * @param query the query
	 */
	private void setPaginationLimits(Integer ulimit, Integer llimit, Query query) {
		if (ulimit != null && llimit != null) {
			if (ulimit >= NumberUtils.INTEGER_ZERO) {
				query.setMaxResults(ulimit - llimit + NumberUtils.INTEGER_ONE);
			}

			if (llimit >= NumberUtils.INTEGER_ZERO) {
				query.setFirstResult(llimit);
			}
		}
	}
	
	/**
	 * Enable filters.
	 *
	 * @param filters the filters
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void enableFilters(Map<String, List> filters) {
		if (filters != null && MapUtils.isNotEmpty(filters)) {
			List<String> filterNames = filters.get(DeviceConstant.FILTER_NAME);
			List<List<String>> filterParams = filters.get(DeviceConstant.FILTER_PARAM);
			List<List<Object>> filterValues = filters.get(DeviceConstant.FILTER_VALUE);
			for (int count = NumberUtils.INTEGER_ZERO; count < filterNames.size(); count++) {
				enableFilter(filterNames.get(count), filterParams.get(count), filterValues.get(count));
			}
		}
	}
	
	/**
	 * Disable filters.
	 *
	 * @param filters the filters
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void disableFilters(Map<String,List> filters){
		if (filters != null && MapUtils.isNotEmpty(filters)) {
			List<String> filterNames = filters.get(DeviceConstant.FILTER_NAME);
			for (int count = NumberUtils.INTEGER_ZERO; count < filterNames.size(); count++) {
				disableFilter(filterNames.get(count));
			}
		}
	}
	
	/**
	 * Enable filter.
	 *
	 * @param filterName the filter name
	 * @param filterParams the filter params
	 * @param filterValues the filter values
	 */
	private void enableFilter(String filterName, List<String> filterParams,
			List<Object> filterValues) {
		Session s = (Session) getEntityManager().getDelegate();
		Filter filter = s.enableFilter(filterName);
		if (filterParams != null && filterValues != null) {
			int index = NumberUtils.INTEGER_ZERO;
			for (String filterParam : filterParams) {
				if (filterValues.get(index) instanceof List) {
					filter.setParameterList(filterParam,(List) filterValues.get(index));
				} else {
					filter.setParameter(filterParam, filterValues.get(index));
				}
				index++;
			}
		}
	}
	
	/**
	 * Disable filter.
	 *
	 * @param filterName the filter name
	 */
	private void disableFilter(String filterName) {
		Session s = (Session) getEntityManager().getDelegate();
		s.disableFilter(filterName);
	}


	/**
	 * Gets the KPI data for day wise.
	 *
	 * @param startDate the start date
	 * @param endDate the end date
	 * @param deviceid the deviceid
	 * @param uLimit the u limit
	 * @param lLimit the l limit
	 * @param filterForDayWise the filter for day wise
	 * @return the KPI data for day wise
	 */
	@Override
	public List<NVDeviceDataWrapper> getKPIDataForDayWise(Long startDate, Long endDate, String deviceid,
			Integer uLimit, Integer lLimit, Map<String, List> filterMap) {
		List<NVDeviceDataWrapper> deviceData = new ArrayList<>();
		try {
			Query query = getEntityManager().createNamedQuery(
					StealthConstants.GET_DEVICE_DATA_FOR_DAYWISE);
			query.setParameter(StealthConstants.ID,deviceid);
			query.setParameter(StealthConstants.START_DATE, new Timestamp(startDate));
			query.setParameter(StealthConstants.END_DATE, new Timestamp(endDate));
			enableFilters(filterMap);
			setPaginationLimits(uLimit, lLimit, query);
			deviceData = query.getResultList();		
			disableFilters(filterMap);
			logger.info("result list size : {}", deviceData.size());
		} catch (Exception ex) {
			logger.info("Error occur while getDeviceData,err msg {}", ExceptionUtils.getStackTrace(ex));
		}
		logger.info("Returning result from getDeviceData with size {}", deviceData.size());
		return deviceData;
	}
	
	/**
	 * Gets the failure device data for day wise.
	 *
	 * @param startDate the start date
	 * @param endDate the end date
	 * @param deviceid the deviceid
	 * @param uLimit the u limit
	 * @param lLimit the l limit
	 * @return the failure device data for day wise
	 */
	@Override
	public List<NVDeviceDataWrapper> getFailureDataForDayWise(Long startDate, Long endDate, String deviceid,
			Integer uLimit, Integer lLimit) {
		List<NVDeviceDataWrapper> failureDeviceData = new ArrayList<>();
		try {
			Query query = getEntityManager().createNamedQuery(
					StealthConstants.GET_FAILURE_DEVICE_DATA_FOR_DAYWISE);
			query.setParameter(StealthConstants.ID,deviceid);
			query.setParameter(StealthConstants.START_DATE, new Timestamp(startDate));
			query.setParameter(StealthConstants.END_DATE, new Timestamp(endDate));
			setPaginationLimits(uLimit, lLimit, query);
			failureDeviceData = query.getResultList();
			logger.info("result list size : {}", failureDeviceData.size());
		} catch (Exception ex) {
			logger.info("Error occur while getFailureDeviceData,err msg {}", ExceptionUtils.getStackTrace(ex));
		}
		logger.info("Returning result from getFailureDeviceData with size {}", failureDeviceData.size());
		return failureDeviceData;
	}

	/**
	 * Gets the non respond devices.
	 *
	 * @return the non respond devices
	 */
	@Override
	public List<NVDeviceData> getNonRespondDevices() {
		logger.info("Going to getDeviceData to get device id pk not available in DriveTestKPI");
		List<NVDeviceData> deviceData = new ArrayList<>(); 
		try {
			Query query = getEntityManager().createNamedQuery(
					StealthConstants.GET_NON_RESPONDING_DEVICES);
			deviceData = query.getResultList();
			logger.info("result list size :");
		} catch (Exception ex) {
			logger.info("Error occur while getDeviceData,err msg {}", ExceptionUtils.getStackTrace(ex));
		}
		logger.info("Returning result from getDeviceData with size{}", deviceData.size());
		return deviceData;
	}

	/**
	 * Gets the KPI data count.
	 *
	 * @param startDate the start date
	 * @param endDate the end date
	 * @param map the map
	 * @return the KPI data count
	 */

	@Override
	public Long getKPIDataCount(Long startDate, Long endDate,
			 Map<String, List> filterMap) {
		logger.info("Going to getDeviceDataCount for  startDate {}, endDate {}", startDate, endDate);
		Long deviceDataCount = 0L;
		try {
			Query query = getEntityManager().createNamedQuery(StealthConstants.GET_DEVICE_DATA_COUNT_BETWEEN_STARTDATE_AND_ENDDATE);
			query.setParameter(StealthConstants.START_DATE, new Timestamp(startDate));
			query.setParameter(StealthConstants.END_DATE, new Timestamp(endDate));
			enableFilters(filterMap);
			deviceDataCount = (Long)query.getSingleResult();
			disableFilters(filterMap);
		} catch (Exception ex) {
			logger.info("Error occur while getDeviceDataCount,err msg {}", ExceptionUtils.getStackTrace(ex));
		}
		logger.info("Returning result from getDeviceDataCount{}", deviceDataCount);
		return deviceDataCount;
	}

	
	/**
	 * Gets the failure device data count.
	 *
	 * @param startDate the start date
	 * @param endDate the end date
	 * @return the failure device data count
	 */
	@Override
	public Long getFailureDataCount(Long startDate, Long endDate) {
		logger.info("Going to getFailureDeviceDataCount for  startDate {}, endDate {}", startDate, endDate);
		Long failureDeviceDataCount = 0L;
		try {
			Query query = getEntityManager().createNamedQuery(StealthConstants.GET_FAILURE_DEVICE_DATA_COUNT_BETWEEN_STARTDATE_AND_ENDDATE);
			query.setParameter(StealthConstants.START_DATE, new Timestamp(startDate));
			query.setParameter(StealthConstants.END_DATE, new Timestamp(endDate));
			failureDeviceDataCount = (Long)query.getSingleResult();
		} catch (Exception ex) {
			logger.info("Error occur while getFailureDeviceDataCount,err msg {}", ExceptionUtils.getStackTrace(ex));
		}
		logger.info("Returning result from getFailureDeviceDataCount {}", failureDeviceDataCount);
		return failureDeviceDataCount;
	}
	
	/**
	 * Gets the failure device data count for day wise.
	 *
	 * @param startDate the start date
	 * @param endDate the end date
	 * @return the failure device data count
	 */
	@Override
	public Long getFailureDataCountForDayWise(Long startDate, Long endDate, String deviceId) {
		logger.info("Going to getFailureDeviceDataCountForDayWise for  startDate {}, endDate {}, deviceId {}", startDate, endDate, deviceId);
		Long failureDeviceDataCount = 0L;
		try {
			Query query = getEntityManager().createNamedQuery(StealthConstants.GET_FAILURE_DEVICE_DATA_COUNT_DAYWISE);
			query.setParameter(StealthConstants.ID, deviceId);
			query.setParameter(StealthConstants.START_DATE, new Timestamp(startDate));
			query.setParameter(StealthConstants.END_DATE, new Timestamp(endDate));
			failureDeviceDataCount = (Long)query.getSingleResult();
		} catch (Exception ex) {
			logger.info("Error occur while getFailureDeviceDataCountForDayWise,err msg {}", ExceptionUtils.getStackTrace(ex));
		}
		logger.info("Returning result from getFailureDeviceDataCountForDayWise {}", failureDeviceDataCount);
		return failureDeviceDataCount;
	}
	
	
	/**
	 * Gets the KPI data count for day wise.
	 *
	 * @param startDate the start date
	 * @param endDate the end date
	 * @param deviceId the device id
	 * @param map the map
	 * @return the KPI data count for day wise
	 */
	@Override
	public Long getKPIDataCountForDayWise(Long startDate, Long endDate, String deviceId, Map<String, List> filterMap) {
		logger.info("Going to getDeviceDataCountForDayWise for  startDate {}, endDate {}", startDate, endDate);
		Long deviceDataCount = 0L;
		try {
			Query query = getEntityManager().createNamedQuery(StealthConstants.GET_DEVICE_DATA_COUNT_DAYWISE);
			query.setParameter(StealthConstants.ID, deviceId);
			query.setParameter(StealthConstants.START_DATE, new Timestamp(startDate));
			query.setParameter(StealthConstants.END_DATE, new Timestamp(endDate));
			enableFilters(filterMap);
			deviceDataCount = (Long)query.getSingleResult();
			disableFilters(filterMap);
		} catch (Exception ex) {
			logger.info("Error occur while getDeviceDataCountForDayWise,err msg {}", ExceptionUtils.getStackTrace(ex));
		}
		logger.info("Returning result from getDeviceDataCountForDayWise{}", deviceDataCount);
		return deviceDataCount;
		}


	/**
	 * Search NV device groups.
	 *
	 * @param startDate the start date
	 * @param endDate the end date
	 * @param swLat the sw lat
	 * @param swLng the sw lng
	 * @param neLat the ne lat
	 * @param neLng the ne lng
	 * @param zoomLevel the zoom level
	 * @param filterMap the filter map
	 * @return the list
	 * @throws DaoException the dao exception
	 */
	@Override
	public List<NVDeviceGroupWrapper> searchNVDeviceGroups(Long startDate, Long endDate, Double swLat, Double swLng, Double neLat, Double neLng, 
			Integer zoomLevel, Map<String, List> filterMap) {
		logger.info("Going to searchNVDeviceGroups for sWLat {},sWLong {},nELat {},nELong {}, zoomLevel {}", swLat,
				swLng, neLat, neLng, zoomLevel);
		List<NVDeviceGroupWrapper> nvDeviceGroups = new ArrayList<>();
		try {
			Integer geographyL1Zoom = Integer.parseInt(SystemConfigurationUtils.systemConfMap.get(DeviceConstant.GEOGRAPHY_L1_ZOOM));
			Integer geographyL2Zoom = Integer.parseInt(SystemConfigurationUtils.systemConfMap.get(DeviceConstant.GEOGRAPHY_L2_ZOOM));
			Integer geographyL3Zoom = Integer.parseInt(SystemConfigurationUtils.systemConfMap.get(DeviceConstant.GEOGRAPHY_L3_ZOOM));
			Integer geographyL4Zoom = Integer.parseInt(SystemConfigurationUtils.systemConfMap.get(DeviceConstant.GEOGRAPHY_L4_ZOOM));
			Integer deviceGroupZoom = Integer
					.parseInt(SystemConfigurationUtils.systemConfMap.get(DeviceConstant.DEVICE_GROUP_ZOOM));
			Query query = null;
			if (zoomLevel >= geographyL1Zoom && zoomLevel < geographyL2Zoom) {
				query = getEntityManager().createNamedQuery(DeviceConstant.GET_DEVICE_GROUP_COUNT_GROUP_BY_GEOL1);
			} else if (zoomLevel >= geographyL2Zoom && zoomLevel < geographyL3Zoom) {
				query = getEntityManager().createNamedQuery(DeviceConstant.GET_DEVICE_GROUP_COUNT_GROUP_BY_GEOL2);
			} else if (zoomLevel >= geographyL3Zoom && zoomLevel < geographyL4Zoom) {
				query = getEntityManager().createNamedQuery(DeviceConstant.GET_DEVICE_GROUP_COUNT_GROUP_BY_GEOL3);
			} else if (zoomLevel >= geographyL4Zoom && zoomLevel < deviceGroupZoom) {
				query = getEntityManager().createNamedQuery(DeviceConstant.GET_DEVICE_GROUP_COUNT_GROUP_BY_GEOL4);
			} else if (zoomLevel >= deviceGroupZoom) {
				query = getEntityManager().createNamedQuery(DeviceConstant.GET_DEVICE_GROUP_BY_VIEWPORT);
			} else {
				return nvDeviceGroups;
			}
			query.setParameter(DeviceConstant.START_DATE, new Timestamp(startDate));
			query.setParameter(DeviceConstant.END_DATE, new Timestamp(endDate));
			query.setParameter(DeviceConstant.NORTH_EAST_LAT, neLat);
			query.setParameter(DeviceConstant.NORTH_EAST_LONG, neLng);
			query.setParameter(DeviceConstant.SOUTH_WEST_LAT, swLat);
			query.setParameter(DeviceConstant.SOUTH_WEST_LONG, swLng);
			enableFilters(filterMap);
			nvDeviceGroups = query.getResultList();
			disableFilters(filterMap);
			logger.info("result list size:{}", nvDeviceGroups.size());
		} catch (Exception ex) {
			logger.info("Error occur while searchNVDeviceGroups,err msg {}", ExceptionUtils.getStackTrace(ex));
		}
		logger.info("Returning result from searchNVDeviceGroups with size {}", nvDeviceGroups.size());
		return nvDeviceGroups;
	}
	
	/**
	 * Gets Cems Data from DriveTestKPI table by Stealth Task Detail id and start time.
	 *
	 * @param startDate
	 *            the start date
	 * @param stealthTaskDetailId
	 *            primary key for Stealth Task Detail table
	 * @return Cems data json from DriveTestKPI table
	 */
	@Override
	public String getCemsDataByStealthTaskIdAndDate(Long startDate, Integer stealthTaskDetailId) {
		logger.info("Going to getCemsDataByStealthTaskIdAndDate for  startDate {}, stealthTaskDetailId {}", startDate, stealthTaskDetailId);
		String cemsDataJson = null;
		try {
			Query query = getEntityManager().createNamedQuery(StealthConstants.GET_CEMS_DATA_BY_STEALTH_TASK_RESULT);
			query.setParameter(StealthConstants.TASK_ID, stealthTaskDetailId);
			query.setParameter(StealthConstants.START_DATE, new Timestamp(startDate));
			cemsDataJson = (String) query.getSingleResult();
		} catch (Exception ex) {
			logger.info("Error occur while getCemsDataByStealthTaskIdAndDate,err msg {}", ExceptionUtils.getStackTrace(ex));
		}
		logger.info("Returning result from getCemsDataByStealthTaskIdAndDate{}", cemsDataJson);
		return cemsDataJson;
	}

}
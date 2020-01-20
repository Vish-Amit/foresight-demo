package com.inn.foresight.module.nv.device.dao.impl;

import static com.inn.bpmn.utils.constants.BpmnConstants.NA;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.lang.MapUtils;
import com.inn.commons.lang.NumberUtils;
import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.constants.InBuildingConstants;
import com.inn.foresight.module.nv.app.model.DeviceInfo;
import com.inn.foresight.module.nv.device.constant.DeviceConstant;
import com.inn.foresight.module.nv.device.dao.INVDeviceDataDao;
import com.inn.foresight.module.nv.device.model.NVDeviceData;
import com.inn.foresight.module.nv.device.wrapper.NVDeviceCountWrapper;
import com.inn.foresight.module.nv.device.wrapper.NVDeviceDataWrapper;
import com.inn.foresight.module.nv.device.wrapper.NVDeviceWrapper;
import com.inn.foresight.module.nv.wpt.analytics.constants.WPTAnalyticsConstants;
import com.inn.product.systemconfiguration.utils.SystemConfigurationUtils;
import com.inn.product.um.geography.model.GeographyL3;
import com.inn.product.um.geography.model.GeographyL4;
import com.inn.product.um.user.model.User;

/** The Class NVDeviceDataDaoImpl. */
@Repository("NVDeviceDataDaoImpl")
public class NVDeviceDataDaoImpl extends HibernateGenericDao<Integer, NVDeviceData> implements INVDeviceDataDao {

	
	/** The logger. */
	private Logger logger = LogManager.getLogger(NVDeviceDataDaoImpl.class);

	/** Construct NVProfileDataDaoImpl object. */
	public NVDeviceDataDaoImpl() {
		super(NVDeviceData.class);
	}

	/**
	 * Gets the device profile by device id.
	 *
	 * @param deviceId
	 *            the device id
	 * @return the device profile by device id
	 * @throws DaoException
	 *             the dao exception
	 */
	@Override
	@Transactional(readOnly=true)
	public NVDeviceData getDeviceByDeviceId(String deviceId)  {
		logger.info("Inside getDeviceByDeviceId  {} ",deviceId);
		try {
			Query query = getEntityManager().createNamedQuery(DeviceConstant.GET_DEVICE_DATA_BY_DEVICE_ID);
			query.setParameter(DeviceConstant.DEVICE_ID, deviceId);
			return (NVDeviceData) query.getSingleResult();
		} catch (NoResultException e) {
			logger.error("NVDevice not found inside getDeviceByDeviceId :{} ,NoResultException stackTrace : {}",deviceId,e.getMessage());
			return null;
		} catch (Exception e) {
			logger.error("Exception inside getDeviceByDeviceId :{} ,Exception stackTrace : {}",deviceId,Utils.getStackTrace(e));

			return null;
		}
	}

	/**
	 * Gets the all device list.
	 *
	 * @param lLimit            the l limit
	 * @param uLimit            the u limit
	 * @param operator the operator
	 * @param filters the filters
	 * @return the all device list
	 * @throws DaoException             the dao exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<NVDeviceDataWrapper> getAllDeviceList(Integer lLimit, Integer uLimit, String operator,
			Map<String, List> filters) {
		List<NVDeviceDataWrapper> deviceList = null;
		try {
			Query query = getEntityManager().createNamedQuery(DeviceConstant.GET_ALL_NV_DEVICE_LIST);
			query.setFirstResult(lLimit);
			query.setMaxResults(uLimit);
			enableFilters(filters);
			deviceList = query.getResultList();
			disableFilters(filters);
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return deviceList;
	}

	/**
	 * Gets the device count.
	 *
	 * @param operator the operator
	 * @param filters the filters
	 * @return the device count
	 * @throws DaoException             the dao exception
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public long getDeviceCount(String operator, Map<String, List> filters) {
		long deviceCount;
		try {
			Query query = getEntityManager().createNamedQuery(DeviceConstant.GET_DEVICE_LIST_COUNT);
			enableFilters(filters);
			deviceCount = (Long) query.getSingleResult();
			disableFilters(filters);
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return deviceCount;
	}

	/**
	 * Gets the device count wrapper for zoom level.
	 *
	 * @param zoomLevel
	 *            the zoom level
	 * @param operator
	 *            the operator
	 * @param northEastLat
	 *            the north east lat
	 * @param northEastLong
	 *            the north east long
	 * @param southWestLat
	 *            the south west lat
	 * @param southWestLong
	 *            the south west long
	 * @param filters
	 *            the filters
	 * @return the device count wrapper for zoom level
	 * @throws DaoException
	 *             the dao exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<NVDeviceCountWrapper> getDeviceCountWrapperForZoomLevel(Integer zoomLevel, String operator,
			Double northEastLat, Double northEastLong, Double southWestLat, Double southWestLong,
			Map<String, List> filters)  {
		List<NVDeviceCountWrapper> nvDeviceDataWrappers = null;
		try {
			Query query = null;
			if (zoomLevel >= DeviceConstant.MINIMUM_ZOOM_LEVEL_FOR_L2
					&& zoomLevel <= DeviceConstant.MAXIMUM_ZOOM_LEVEL_FOR_L2) {
				query = getEntityManager().createNamedQuery(DeviceConstant.GET_DEVICE_COUNT_WRAPPER_BY_L2);
			} else if (zoomLevel >= DeviceConstant.MINIMUM_ZOOM_LEVEL_FOR_L3
					&& zoomLevel <= DeviceConstant.MAXIMUM_ZOOM_LEVEL_FOR_L3) {
				query = getEntityManager().createNamedQuery(DeviceConstant.GET_DEVICE_COUNT_WRAPPER_BY_L3);
			} else if (zoomLevel >= DeviceConstant.MINIMUM_ZOOM_LEVEL_FOR_L4
					&& zoomLevel <= DeviceConstant.MAXIMUM_ZOOM_LEVEL_FOR_L4) {
				query = getEntityManager().createNamedQuery(DeviceConstant.GET_DEVICE_COUNT_WRAPPER_BY_L4);
			}
			query = setQueryParams(northEastLat, northEastLong, southWestLat, southWestLong, query);
			enableFilters(filters);
			nvDeviceDataWrappers = query.getResultList();
			disableFilters(filters);
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return nvDeviceDataWrappers;
	}

	/**
	 * Sets the query params.
	 *
	 * @param northEastLat
	 *            the north east lat
	 * @param northEastLong
	 *            the north east long
	 * @param southWestLat
	 *            the south west lat
	 * @param southWestLong
	 *            the south west long
	 * @param query
	 *            the query
	 * @return the query
	 */
	private Query setQueryParams(Double northEastLat, Double northEastLong, Double southWestLat, Double southWestLong,
			Query query) {
		query.setParameter(DeviceConstant.NE_LAT_PLACEHOLDER, northEastLat);
		query.setParameter(DeviceConstant.NE_LNG_PLACEHOLDER, northEastLong);
		query.setParameter(DeviceConstant.SW_LAT_PLACEHOLDER, southWestLat);
		query.setParameter(DeviceConstant.SW_LNG_PLACEHOLDER, southWestLong);
		return query;
	}

	/**
	 * Gets the device inside cluster.
	 *
	 * @param operator            the operator
	 * @param northEastLat            the north east lat
	 * @param northEastLong            the north east long
	 * @param southWestLat            the south west lat
	 * @param southWestLong            the south west long
	 * @param geographyId the geography id
	 * @param filters            the filters
	 * @return the device inside cluster
	 * @throws DaoException             the dao exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<NVDeviceDataWrapper> getDeviceInsideViewPort(String operator, Double northEastLat, Double northEastLong,
			Double southWestLat, Double southWestLong, String geographyId, Map<String, List> filters)  {
		List<NVDeviceDataWrapper> nvDeviceDataWrappers = null;
		try {
			Query query = null;
			query = getEntityManager().createNamedQuery(DeviceConstant.GET_DEVICE_FOR_VIEW_PORT);
			setQueryParams(northEastLat, northEastLong, southWestLat, southWestLong, query);
			enableFilters(filters);
			nvDeviceDataWrappers = query.getResultList();
			disableFilters(filters);
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return nvDeviceDataWrappers;
	}

	/**
	 * Gets the NV device data wrapper by device id.
	 *
	 * @param deviceId
	 *            the device id
	 * @return the NV device data wrapper by device id
	 * @throws DaoException
	 *             the dao exception
	 */
	@Override
	public NVDeviceDataWrapper getNVDeviceDataWrapperByDeviceId(String deviceId)  {
		NVDeviceDataWrapper nvDeviceDataWrapper;
		try {
			Query query = getEntityManager().createNamedQuery(DeviceConstant.GET_NV_DEVICE_DATA_WRAPPER_BY_DEVICE_ID);
			query.setParameter(DeviceConstant.DEVICE_ID_PLACE_HOLDER, deviceId);
			query.setMaxResults(DeviceConstant.MAX_RESULT);
			nvDeviceDataWrapper = (NVDeviceDataWrapper) query.getSingleResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return nvDeviceDataWrapper;
	}

	/**
	 * Enable filters.
	 *
	 * @param filters
	 *            the filters
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void enableFilters(Map<String, List> filters) {
		if (filters != null && MapUtils.isNotEmpty(filters)) {
			List<String> filterNames = filters.get(WPTAnalyticsConstants.FILTER_NAME);
			List<String> filterParams = filters.get(WPTAnalyticsConstants.FILTER_PARAM);
			List<Object> filterValues = filters.get(WPTAnalyticsConstants.FILTER_VALUE);
			if (filterNames != null && !filterNames.isEmpty()) {
				int listSize = filterNames.size();
				if (filterParams != null && filterValues != null) {
					enableFilter(filterNames, filterParams, filterValues, listSize);
				}
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void enableFilter(List<String> filterNames, List<String> filterParams, List<Object> filterValues,
			int listSize) {
		for (int count = NumberUtils.INTEGER_ZERO; count < listSize; count++) {
			if (filterValues.size()>count&&filterValues.get(count) instanceof List) {
				enableFilter(filterNames.get(count), filterParams.get(count),
						(List) filterValues.get(count));
			} else {
				if(filterParams.size()>count&&filterValues.size()>count) {

				   enableFilter(filterNames.get(count), filterParams.get(count), filterValues.get(count));
				}else {
					enableFilter(filterNames.get(count), null,null);
				}
			}
		}
	}

	/**
	 * Disable filters.
	 *
	 * @param filters
	 *            the filters
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void disableFilters(Map<String, List> filters) {
		if (filters != null && MapUtils.isNotEmpty(filters)) {
			List<String> filterNames = filters.get(WPTAnalyticsConstants.FILTER_NAME);
			if (filterNames != null && !filterNames.isEmpty()) {
				int listSize = filterNames.size();
				for (int count = NumberUtils.INTEGER_ZERO; count < listSize; count++) {
					disableFilter(filterNames.get(count));
				}
			}
		}
	}

	/**
	 * Enable filter.
	 *
	 * @param filterName the filter name
	 * @param filterParam the filter param
	 * @param filterValue the filter value
	 */
	private void enableFilter(String filterName, String filterParam, List<String> filterValue) {
		Session s = (Session) getEntityManager().getDelegate();
		Filter filter = s.enableFilter(filterName);
		if(filterParam != null && filterValue != null) {
			filter.setParameterList(filterParam, filterValue);
		}
	}
	
	/**
	 * Enable filter.
	 *
	 * @param filterName the filter name
	 * @param filterParam the filter param
	 * @param filterValue the filter value
	 */
	private void enableFilter(String filterName, String filterParam, Object filterValue) {
		Session s = (Session) getEntityManager().getDelegate();
		Filter filter = s.enableFilter(filterName);
		if(filterParam != null && filterValue != null) {
			filter.setParameter(filterParam, filterValue);
		}
	}

	/**
	 * Disable filter.
	 *
	 * @param filterName
	 *            the filter name
	 */
	private void disableFilter(String filterName) {
		Session s = (Session) getEntityManager().getDelegate();
		s.disableFilter(filterName);
	}

	/**
	 * Gets the device list inside cluster.
	 *
	 * @param cluster
	 *            the cluster
	 * @param operator
	 *            the operator
	 * @param filters
	 *            the filters
	 * @return the device list inside cluster
	 * @throws DaoException
	 *             the dao exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<NVDeviceDataWrapper> getDeviceListInsideCluster(String cluster, String operator,
			Map<String, List> filters)  {
		List<NVDeviceDataWrapper> nvDeviceDataWrappers = null;
		try {
			Query query = null;
			query = getEntityManager().createNamedQuery(DeviceConstant.GET_DEVICE_LIST_INSIDE_CLUSTER);
			query.setParameter(DeviceConstant.GEOGRAPHY_PLACE_HOLDER, cluster);
			enableFilters(filters);
			nvDeviceDataWrappers = query.getResultList();
			disableFilters(filters);
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return nvDeviceDataWrappers;
	}

	/**
	 * Gets the device list for WO.
	 *
	 * @param woStartTime
	 *            the wo start time
	 * @param filters
	 *            the filters
	 * @param lLimit
	 *            the l limit
	 * @param uLimit
	 *            the u limit
	 * @return the device list for WO
	 * @throws DaoException
	 *             the dao exception
	 */
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<NVDeviceDataWrapper> getDeviceListForWO(Date woStartTime, Map<String, List> filters, Integer lLimit,
			Integer uLimit)  {
		logger.info("Going to getDeviceListForWO startTime : {} ", woStartTime);
		List<NVDeviceDataWrapper> deviceDatas = null;
		try {
			Query query = null;
			query = getEntityManager().createNamedQuery(DeviceConstant.GET_DEVICE_LIST_FOR_WO);
			if (filters != null) {
				enableFilters(filters);
			}
			setPaginationLimits(uLimit, lLimit, query);
			deviceDatas = query.getResultList();
			if (filters != null) {
				disableFilters(filters);
			}
			logger.info("Done getDeviceListForWO size : {}", deviceDatas.size());
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return deviceDatas;
	}

	/**
	 * Sets the pagination limits.
	 *
	 * @param ulimit
	 *            the ulimit
	 * @param llimit
	 *            the llimit
	 * @param query
	 *            the query
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
	 * Gets the NV device data.
	 *
	 * @param nvDeviceDataIds
	 *            the nv device data ids
	 * @return the NV device data
	 * @throws DaoException
	 *             the dao exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<NVDeviceData> getNVDeviceData(List<Integer> nvDeviceDataIds)  {
		logger.info("Going to getNVDeviceData");
		List<NVDeviceData> nvDeviceDatas = null;
		try {
			Query query = null;
			query = getEntityManager().createNamedQuery(DeviceConstant.GET_DEVICE_LIST_BY_IDS);
			query.setParameter(DeviceConstant.ID_LIST, nvDeviceDataIds);
			nvDeviceDatas = query.getResultList();
			logger.info("Done getNVDeviceData size : {}", nvDeviceDatas.size());
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return nvDeviceDatas;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<NVDeviceData> getNVDeviceDataByDeviceInfo(List<Integer> deviceInfoIDs) {
		logger.info("Going to getNVDeviceData");
		List<NVDeviceData> nvDeviceDatas = null;
		try {
			Query query = null;
			query = getEntityManager().createNamedQuery(DeviceConstant.GET_DEVICE_LIST_BY_DEVICE_INFO_IDS);
			query.setParameter(DeviceConstant.ID_LIST, deviceInfoIDs);
			nvDeviceDatas = query.getResultList();
			logger.info("Done getNVDeviceData size : {}", nvDeviceDatas.size());
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return nvDeviceDatas;
	}
	
	
	/**
	 * Gets the device count list for WO.
	 *
	 * @param woStartTime
	 *            the wo start time
	 * @param filters
	 *            the filters
	 * @return the device count list for WO
	 * @throws DaoException
	 *             the dao exception
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Long getDeviceCountListForWO(Date woStartTime, Map<String, List> filters)  {
		logger.info("Going to getDeviceCountListForWO");
		Long deviceCount = null;
		try {
			Query query = getEntityManager().createNamedQuery(DeviceConstant.GET_DEVICE_LIST_COUNT_FOR_WO);
			if(filters != null) {
			enableFilters(filters);
			}
			deviceCount = (Long) query.getSingleResult();
			if(filters != null) {
			disableFilters(filters);
			}
			logger.info("Done getDeviceCountListForWO count : {}", deviceCount);
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return deviceCount;

	}


	/**
	 * Gets the device count for table view.
	 *
	 * @param northEastLat the north east lat
	 * @param northEastLong the north east long
	 * @param southWestLat the south west lat
	 * @param southWestLong the south west long
	 * @param filterAndValueMap the filter and value map
	 * @param geography the geography
	 * @return the device count for table view
	 * @throws DaoException the dao exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<NVDeviceCountWrapper> getDeviceCountForTableView(Double northEastLat, Double northEastLong,
			Double southWestLat, Double southWestLong, Map<String, List> filterAndValueMap, String geography)
		 {

		List<NVDeviceCountWrapper> nvDeviceDataWrappers = null;
		try {
			Query query = null;
			if (DeviceConstant.GEOGRAPHYL2.equalsIgnoreCase(geography)) {
				query = getEntityManager().createNamedQuery(DeviceConstant.GET_DEVICE_COUNT_WRAPPER_BY_L2);
			} else if (DeviceConstant.GEOGRAPHYL3.equalsIgnoreCase(geography)) {
				query = getEntityManager().createNamedQuery(DeviceConstant.GET_DEVICE_COUNT_WRAPPER_BY_L3);
			} else if (DeviceConstant.GEOGRAPHYL4.equalsIgnoreCase(geography)) {
				query = getEntityManager().createNamedQuery(DeviceConstant.GET_DEVICE_COUNT_WRAPPER_BY_L4);
			}
			query = setQueryParams(northEastLat, northEastLong, southWestLat, southWestLong, query);
			enableFilters(filterAndValueMap);
			nvDeviceDataWrappers = query.getResultList();
			disableFilters(filterAndValueMap);
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return nvDeviceDataWrappers;
	}
	
	/**
	 * Gets the devices by device ids.
	 *
	 * @param deviceId the device id
	 * @return the devices by device ids
	 * @throws DaoException the dao exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<NVDeviceData> getDevicesByDeviceIds(List<String> deviceId)  {
		logger.info("Inside getDevicesByDeviceIds");
		try {
			Query query = getEntityManager().createNamedQuery(DeviceConstant.GET_DEVICES_BY_DEVICE_IDS);
			query.setParameter(DeviceConstant.DEVICE_ID, deviceId);
			List<NVDeviceData> deviceDatas = query.getResultList();
			logger.info("Done getDevicesByDeviceIds");
			return deviceDatas;
		} catch (NoResultException e) {
			logger.error("No Result found for ids : {}",deviceId != null ? new Gson().toJson(deviceId): null);
			throw new DaoException(e.getMessage());
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}


	/**
	 * Search NV devices.
	 *
	 * @param operator the operator
	 * @param northEastLat the north east lat
	 * @param northEastLong the north east long
	 * @param southWestLat the south west lat
	 * @param southWestLong the south west long
	 * @param filterValueMap the filter value map
	 * @param zoomLevel the zoom level
	 * @return the list
	 * @throws DaoException the dao exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<NVDeviceDataWrapper> searchNVDevices(String operator, Double northEastLat, Double northEastLong,
			Double southWestLat, Double southWestLong, Map<String, List> filterValueMap, Integer zoomLevel)
			 {
		logger.info(
				"Going to searchNVDevices for sWLat {},sWLong {},nELat {},nELong {},zoomLevel{}",
				southWestLat, southWestLong, northEastLat, northEastLong, zoomLevel);
		List<NVDeviceDataWrapper> nvDeviceDataWrappers = new ArrayList<>();
		try {
			Integer geographyL1Zoom = Integer.parseInt(
					SystemConfigurationUtils.systemConfMap.get(InBuildingConstants.GEOGRAPHY_L1_ZOOM));
			Integer geographyL2Zoom =Integer.parseInt(
					SystemConfigurationUtils.systemConfMap.get(InBuildingConstants.GEOGRAPHY_L2_ZOOM));
			Integer geographyL3Zoom =Integer.parseInt(
					SystemConfigurationUtils.systemConfMap.get(InBuildingConstants.GEOGRAPHY_L3_ZOOM));
			Integer geographyL4Zoom = Integer.parseInt(
					SystemConfigurationUtils.systemConfMap.get(InBuildingConstants.GEOGRAPHY_L4_ZOOM));
			Query query = null;
			if (zoomLevel >= geographyL1Zoom && zoomLevel < geographyL2Zoom) {
				query = getEntityManager().createNamedQuery(DeviceConstant.GET_DEVICE_COUNT_GROUP_BY_L1);
			} else if (zoomLevel >= geographyL2Zoom && zoomLevel < geographyL3Zoom) {
				query = getEntityManager().createNamedQuery(DeviceConstant.GET_DEVICE_COUNT_GROUP_BY_L2);
			} else if (zoomLevel >= geographyL3Zoom && zoomLevel < geographyL4Zoom) {
				query = getEntityManager().createNamedQuery(DeviceConstant.GET_DEVICE_COUNT_GROUP_BY_L3);
			} else if (zoomLevel >= geographyL4Zoom &&zoomLevel <DeviceConstant.ZOOM_LEVEL_16) {
				query = getEntityManager().createNamedQuery(DeviceConstant.GET_DEVICE_COUNT_GROUP_BY_L4);
			} else if(zoomLevel >= DeviceConstant.ZOOM_LEVEL_16 &&zoomLevel <=DeviceConstant.ZOOM_LEVEL_18){
				query = getEntityManager().createNamedQuery(DeviceConstant.GET_DEVICE_FOR_VIEW_PORT);
				setQueryParams(northEastLat, northEastLong, southWestLat, southWestLong, query);

			}
			enableFilters(filterValueMap);
			if (query != null) {
				nvDeviceDataWrappers = query.getResultList();
			}
			disableFilters(filterValueMap);
			logger.info("result list size : {}", nvDeviceDataWrappers.size());
		} catch (Exception ex) {
			logger.info("Error occur while searchNVDevices,err msg {}", ExceptionUtils.getStackTrace(ex));
		}
		logger.info("Returning result from searchNVDevices with size {}", nvDeviceDataWrappers.size());
		return nvDeviceDataWrappers;
	}
	
	/**
	 * Gets the devices by device ids.
	 *
	 * @param groupId the group id
	 * @param deviceId the device id
	 * @return the devices by device ids
	 * @throws DaoException the dao exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<NVDeviceData> getDevicesByDeviceIdsAndGroupId(Integer groupId , List<String> deviceId)  {
		logger.info("Inside getDevicesByDeviceIdsAndGroupId");
		try {
			Query query = getEntityManager().createNamedQuery(DeviceConstant.GET_DEVICES_BY_DEVICE_IDS_AND_GROUP_ID);
			query.setParameter(DeviceConstant.DEVICE_ID, deviceId)
			.setParameter(DeviceConstant.GROUP_ID, groupId);
			List<NVDeviceData> deviceDatas = query.getResultList();
			logger.info("Done getDevicesByDeviceIds");
			return deviceDatas;
		} catch (NoResultException e) {
			logger.error("No Result found for Device id : {}",deviceId != null ? new Gson().toJson(deviceId): null);
			throw new DaoException(e.getMessage());
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	/**
	 * Gets the device data by group id.
	 *
	 * @param groupId the group id
	 * @param filters the filters
	 * @return the device data by group id
	 * @throws DaoException the dao exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<NVDeviceData> getDeviceDataByGroupId(Integer groupId, Map<String, List> filters )  {
		logger.info("Inside getDevicesByGroupId");
		try {
			Query query = getEntityManager().createNamedQuery(DeviceConstant.GET_DEVICE_DATA_BY_GROUP_ID);
			query.setParameter(DeviceConstant.GROUP_ID, groupId);
			enableFilters(filters);
			List<NVDeviceData> deviceList = query.getResultList();
			disableFilters(filters);
			if(deviceList != null && !deviceList.isEmpty()) {
				logger.info("Total: {} Devices found with Group Id: {}",deviceList,groupId );
			}
			return deviceList;
		} catch (NoResultException e) {
			logger.error("No Result found for ids : {}",groupId != null ? new Gson().toJson(groupId): null);
			throw new DaoException(e.getMessage());
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	
	/**
	 * Gets the devices by group id.
	 *
	 * @param groupId the group id
	 * @return the devices by group id
	 * @throws DaoException the dao exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<NVDeviceDataWrapper> getDevicesByGroupId(Integer groupId)  {
		logger.info("Inside getDevicesByGroupId");
		try {
			Query query = getEntityManager().createNamedQuery(DeviceConstant.GET_DEVICES_BY_GROUP_ID);
			query.setParameter(DeviceConstant.GROUP_ID, groupId);
			List<NVDeviceDataWrapper> deviceDatas = query.getResultList();
			logger.info("Done getDevicesByGroupId");
			return deviceDatas;
		} catch (NoResultException e) {
			logger.error("No Result found for groupId : {}",groupId);
			throw new DaoException(e.getMessage());
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * Gets the device count by group id.
	 *
	 * @param groupId the group id
	 * @return the device count by group id
	 * @throws DaoException the dao exception
	 */
	@Override
	public Long getDeviceCountByGroupId(Integer groupId) {
		logger.info("Inside getDeviceCountByGroupId");
		try {
			Query query = getEntityManager().createNamedQuery(DeviceConstant.GET_DEVICE_COUNT_BY_GROUP_ID);
			query.setParameter(DeviceConstant.GROUP_ID, groupId);
			Long deviceCount = (Long) query.getSingleResult();
			logger.info("Done getDeviceCountByGroupId");
			return deviceCount;
		} catch (NoResultException e) {
			logger.error("No Result found for groupId : {}",groupId);
			throw new DaoException(e.getMessage());
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	/**
	 * Gets the device recharge detail.
	 *
	 * @param daysOfDifference the days of difference
	 * @return the device recharge detail
	 * @throws DaoException the dao exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<NVDeviceDataWrapper> getDeviceRechargeDetail(Integer daysOfDifference) {
		logger.info("Inside getDeviceRechargeDetail");
		try {
			Query query = getEntityManager().createNamedQuery(DeviceConstant.GET_DEVICE_RECHARGE_NOTIFICATION);
			query.setParameter("daysOfDifference", daysOfDifference);
			List<NVDeviceDataWrapper> deviceData = query.getResultList();
			logger.info("Done getDeviceRechargeDetail");
			return deviceData;
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	/**
	 * Gets the device recharge detail count.
	 *
	 * @param daysOfDifference the Integer
	 * @return the device recharge detail count
	 * @throws DaoException the dao exception
	 */
	@Override
	public Long getDeviceRechargeDetailCount(int daysOfDifference) {
		logger.info("Inside getDeviceRechargeDetailCount");
		try {
			Query query = getEntityManager().createNamedQuery(DeviceConstant.GET_DEVICE_RECHARGE_NOTIFICATION_COUNT);
			query.setParameter("daysOfDifference", daysOfDifference);
			Long deviceDataCount = (Long)query.getSingleResult();
			logger.info("Done getDeviceRechargeDetailCount");
			return deviceDataCount;
		} catch (Exception e) {
			logger.error("Error in getDeviceRechargeDetailCount : {} " ,ExceptionUtils.getStackTrace(e));
			throw new DaoException(e.getMessage());
		}
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> getDeviceListForUser(Integer userId){
		logger.info("Going to get NV Device List For user{} ",userId);
		try {
			Query query=getEntityManager().createNamedQuery("getDeviceListForUser").setParameter("userId", userId);
			return query.getResultList();
		}catch(Exception exception) {
			logger.error("Error in getting NV Device list for user {} Exception {} ",userId,Utils.getStackTrace(exception));
		}
		return Collections.emptyList();
		
	}
	
	@Override
	public String getNvInstallationDetail(String deviceId) {
		logger.info("Going to check nv installation detail for deviceid {} : ", deviceId);
		String nvStatus = ForesightConstants.BLANK_STRING;
		try {
			Query query = getEntityManager().createNamedQuery("getNvInstallationDetail");
			query.setParameter(DeviceConstant.NV_CURRENT_DATE, new Date());
			query.setParameter(DeviceConstant.NV_PREVIOUS_DATE,
					DateUtils.addDays(new Date(), ConfigUtils.getInteger(DeviceConstant.NV_DURATION_TIME)));
			query.setParameter(DeviceConstant.NV_DEVICE_ID, deviceId);
			nvStatus = (String) query.getSingleResult();
			logger.info("NV Status : {}", nvStatus);
		} catch (Exception exception) {
			logger.error("Error in getting nv installation detail for deviceid : {} Exception: {}", deviceId, Utils.getStackTrace(exception));
		}
		return nvStatus;
	}

	@Override
	public Map<String, List<String>> getFilters() {

		logger.info("Going to get filters");
		Map<String, List<String>> responseMap = new HashMap<>();
		try {
			List<String> predicateList = ConfigUtils.getStringList("NV_DEVICE_DATA_FILTER");

			CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<String> query = builder.createQuery(String.class);
			Root<NVDeviceData> root = query.from(NVDeviceData.class);

			for(String predicate :predicateList){
				query.select(root.get(predicate)).distinct(true);
				List<String> resultList = getEntityManager().createQuery(query).getResultList();
				resultList.removeAll(Collections.singleton(null));
				resultList.removeAll(Collections.singleton(NA));
				responseMap.put(predicate, resultList);
			}

		} catch (Exception exception) {
			logger.error("Error in getting filters {} ",Utils.getStackTrace(exception));
		}

		return responseMap;
	}

	/**
	 * get device data using different filters added in wrapper
	 * @param wrapper
	 * @param lLimit
	 * @param uLimit
	 * @return
	 */
	@Override
	public List<NVDeviceDataWrapper> getDeviceData(NVDeviceWrapper wrapper, Integer lLimit, Integer uLimit){

		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<NVDeviceDataWrapper> query = builder.createQuery(NVDeviceDataWrapper.class);
		Root<NVDeviceData> nvDeviceData = query.from(NVDeviceData.class);
		Join<NVDeviceData, DeviceInfo> deviceInfo = nvDeviceData.join(DeviceConstant.DEVICE_INFO, JoinType.LEFT);
		Join<NVDeviceData, GeographyL4> geographyL4 = nvDeviceData.join(DeviceConstant.GEOGRAPHYL4, JoinType.LEFT);
		Join<NVDeviceData, GeographyL3> geographyL3 = nvDeviceData.join(DeviceConstant.GEOGRAPHYL3, JoinType.LEFT);
		Join<DeviceInfo, User> user = deviceInfo.join(DeviceConstant.USER, JoinType.LEFT);

		query.multiselect(deviceInfo.get(DeviceConstant.DEVICE_ID),
				nvDeviceData.get(DeviceConstant.MAKE),
				nvDeviceData.get(DeviceConstant.MODEL),
				nvDeviceData.get(DeviceConstant.IMEI_PARAM),
				nvDeviceData.get(DeviceConstant.OS),
				nvDeviceData.get(DeviceConstant.APP_VERSION),
				nvDeviceData.get(DeviceConstant.MODIFICATION_TIME),
				geographyL4.get(DeviceConstant.NAME),
				nvDeviceData.get(DeviceConstant.OPERATOR_PARAM),
				nvDeviceData.get(DeviceConstant.MODULE),
				nvDeviceData.get(DeviceConstant.IMSI_PARAM),
				nvDeviceData.get(DeviceConstant.SOURCE),
				builder.selectCase().when(user.isNotNull(), Boolean.TRUE).otherwise(Boolean.FALSE),
				builder.selectCase().when(user.isNotNull(), user.get(DeviceConstant.FIRST_NAME)).otherwise(ForesightConstants.NULL_STRING),
				builder.selectCase().when(user.isNotNull(), user.get(DeviceConstant.LAST_NAME)).otherwise(ForesightConstants.NULL_STRING))
				.orderBy(builder.desc(nvDeviceData.get(DeviceConstant.MODIFICATION_TIME)));


		List<Predicate> predicateList = new ArrayList<>();
		predicateList.add(geographyL3.isNotNull());
		if(wrapper.getDeviceId() != null){ predicateList.add(deviceInfo.get(DeviceConstant.DEVICE_ID).in(Arrays.asList(wrapper.getDeviceId()))); }
		if(CollectionUtils.isNotEmpty(wrapper.getOs())){ predicateList.add(nvDeviceData.get(DeviceConstant.OS).in((wrapper.getOs()))); }
		if(CollectionUtils.isNotEmpty(wrapper.getMake())){ predicateList.add(nvDeviceData.get(DeviceConstant.MAKE).in(wrapper.getMake())); }
		if(CollectionUtils.isNotEmpty(wrapper.getModel())){ predicateList.add(nvDeviceData.get(DeviceConstant.MODEL).in(wrapper.getModel())); }
		if(CollectionUtils.isNotEmpty(wrapper.getSource())){ predicateList.add(nvDeviceData.get(DeviceConstant.SOURCE).in(wrapper.getSource())); }
		if(CollectionUtils.isNotEmpty(wrapper.getOperator())){ predicateList.add(nvDeviceData.get(DeviceConstant.OPERATOR_PARAM).in(wrapper.getOperator())); }
		if(CollectionUtils.isNotEmpty(wrapper.getAppVersion())){ predicateList.add(nvDeviceData.get(DeviceConstant.APP_VERSION).in(wrapper.getAppVersion())); }

		query = query.where(predicateList.toArray(new Predicate[] {}));
		TypedQuery<NVDeviceDataWrapper> typedQuery = getEntityManager().createQuery(query);

		if(lLimit!=null && uLimit!=null) {
			typedQuery.setFirstResult(lLimit) // offset
			 	.setMaxResults(uLimit - lLimit + 1); // limit
		}

		return typedQuery.getResultList();
	}

	/**
	 * get device data count using different filters added in wrapper
	 * @param wrapper
	 * @return
	 */
	@Override
	public Long getDeviceDataCount(NVDeviceWrapper wrapper){

		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Tuple> query = criteriaBuilder.createQuery(Tuple.class);

		Root<NVDeviceData> nvDeviceData = query.from(NVDeviceData.class);
		Join<NVDeviceData, DeviceInfo> deviceInfo = nvDeviceData.join(DeviceConstant.DEVICE_INFO, JoinType.LEFT);

		query.multiselect(criteriaBuilder.count(nvDeviceData.get("deviceInfo").get("deviceId")));

		List<Predicate> predicateList = new ArrayList<>();
		if(wrapper.getDeviceId() != null){ predicateList.add(deviceInfo.get(DeviceConstant.DEVICE_ID).in(Arrays.asList(wrapper.getDeviceId()))); }
		if(CollectionUtils.isNotEmpty(wrapper.getOs())){ predicateList.add(nvDeviceData.get(DeviceConstant.OS).in((wrapper.getOs()))); }
		if(CollectionUtils.isNotEmpty(wrapper.getMake())){ predicateList.add(nvDeviceData.get(DeviceConstant.MAKE).in(wrapper.getMake())); }
		if(CollectionUtils.isNotEmpty(wrapper.getModel())){ predicateList.add(nvDeviceData.get(DeviceConstant.MODEL).in(wrapper.getModel())); }
		if(CollectionUtils.isNotEmpty(wrapper.getSource())){ predicateList.add(nvDeviceData.get(DeviceConstant.SOURCE).in(wrapper.getSource())); }
		if(CollectionUtils.isNotEmpty(wrapper.getOperator())){ predicateList.add(nvDeviceData.get(DeviceConstant.OPERATOR_PARAM).in(wrapper.getOperator())); }
		if(CollectionUtils.isNotEmpty(wrapper.getAppVersion())){ predicateList.add(nvDeviceData.get(DeviceConstant.APP_VERSION).in(wrapper.getAppVersion())); }

		query.where(predicateList.toArray(new Predicate[] {}));
		TypedQuery<Tuple> typedQuery1 = getEntityManager().createQuery(query);

		return (Long)typedQuery1.getSingleResult().get(0);

	}

    @Override
	@Transactional
	public NVDeviceData create(NVDeviceData arg0){
		return super.create(arg0);

	}

	
}

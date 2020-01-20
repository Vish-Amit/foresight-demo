package com.inn.foresight.module.nv.device.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.inn.commons.lang.NumberUtils;
import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.module.nv.device.constant.DeviceConstant;
import com.inn.foresight.module.nv.device.dao.INVDeviceGroupDao;
import com.inn.foresight.module.nv.device.model.NVDeviceGroup;
import com.inn.foresight.module.nv.device.wrapper.NVDeviceDataWrapper;
import com.inn.foresight.module.nv.device.wrapper.NVDeviceGroupWrapper;
import com.inn.product.systemconfiguration.utils.SystemConfigurationUtils;

/**
 * The Class NVDeviceGroupDaoImpl.
 *
 * @author innoeye
 * date - 24-Apr-2018 12:09:19 PM
 */
@Repository("NVDeviceGroupDaoImpl")
public class NVDeviceGroupDaoImpl
		extends HibernateGenericDao<Integer, NVDeviceGroup> implements INVDeviceGroupDao{

	/** The logger. */
	private Logger logger = LogManager.getLogger(NVDeviceGroupDaoImpl.class);
	
	/** Construct NVDeviceGroupDaoImpl object. */
	public NVDeviceGroupDaoImpl() {
		super(NVDeviceGroup.class);
	}

	/**
	 * Gets the all device groups.
	 *
	 * @param ulimit the ulimit
	 * @param llimit the llimit
	 * @return the all device groups
	 * @throws DaoException the dao exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<NVDeviceGroupWrapper> getAllDeviceGroups(Integer ulimit,Integer llimit) {
		logger.info("Inside getAllDeviceGroups");
		try {
			Query query = getEntityManager().createNamedQuery(DeviceConstant.GET_ALL_DEVICE_GROUP);
			setPaginationLimits(ulimit, llimit, query);
			List<NVDeviceGroupWrapper> deviceGroups = query.getResultList();
			logger.info("Done getAllDeviceGroups");
			return deviceGroups;
		} catch (Exception e) {
			logger.error("Error in getAllDeviceGroups : {} " ,ExceptionUtils.getStackTrace(e));
			throw new DaoException(e.getMessage());
		}
	}
	
	/**
	 * Gets the device group within view port.
	 *
	 * @param swLat the sw lat
	 * @param swLng the sw lng
	 * @param neLat the ne lat
	 * @param neLng the ne lng
	 * @return the device group within view port
	 * @throws DaoException the dao exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<NVDeviceGroupWrapper> getDeviceGroupWithinViewPort(Double swLat, Double swLng, Double neLat,
			Double neLng){
		logger.info("Inside getDeviceGroupWithinViewPort");
		try {
			Query query = getEntityManager().createNamedQuery(DeviceConstant.GET_DEVICE_GROUP_WITHIN_VIEW_PORT);
			query.setParameter(DeviceConstant.NORTH_EAST_LAT, neLat);
			query.setParameter(DeviceConstant.NORTH_EAST_LONG, neLng);
			query.setParameter(DeviceConstant.SOUTH_WEST_LAT, swLat);
			query.setParameter(DeviceConstant.SOUTH_WEST_LONG, swLng);
			List<NVDeviceGroupWrapper> deviceGroups = query.getResultList();
			logger.info("Done getDeviceGroupWithinViewPort");
			return deviceGroups;
		} catch (Exception e) {
			logger.error("Error in getDeviceGroupWithinViewPort : {} " ,ExceptionUtils.getStackTrace(e));
			throw new DaoException(e.getMessage());
		}
	}
	
	/**
	 * Search NV device groups.
	 *
	 * @param swLat the sw lat
	 * @param swLng the sw lng
	 * @param neLat the ne lat
	 * @param neLng the ne lng
	 * @param zoomLevel the zoom level
	 * @return the list
	 * @throws DaoException the dao exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<NVDeviceGroupWrapper> searchAllNVDeviceGroups(Double swLat, Double swLng, Double neLat, Double neLng, 
			Integer zoomLevel, Date endDate) {
		logger.info("Going to searchNVDeviceGroups for sWLat {},sWLong {},nELat {},nELong {} and zoomLevel {}", swLat,
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
				query = getEntityManager().createNamedQuery(DeviceConstant.GET_ALL_DEVICE_GROUP_COUNT_GROUP_BY_GEOL1);
			} else if (zoomLevel >= geographyL2Zoom && zoomLevel < geographyL3Zoom) {
				query = getEntityManager().createNamedQuery(DeviceConstant.GET_ALL_DEVICE_GROUP_COUNT_GROUP_BY_GEOL2);
			} else if (zoomLevel >= geographyL3Zoom && zoomLevel < geographyL4Zoom) {
				query = getEntityManager().createNamedQuery(DeviceConstant.GET_ALL_DEVICE_GROUP_COUNT_GROUP_BY_GEOL3);
			} else if (zoomLevel >= geographyL4Zoom && zoomLevel < deviceGroupZoom) {
				query = getEntityManager().createNamedQuery(DeviceConstant.GET_ALL_DEVICE_GROUP_COUNT_GROUP_BY_GEOL4);
			} else if (zoomLevel >= deviceGroupZoom) {
				query = getEntityManager().createNamedQuery(DeviceConstant.GET_ALL_DEVICE_GROUP_BY_VIEWPORT);
			} else {
				return nvDeviceGroups;
			}
			query.setParameter(DeviceConstant.NORTH_EAST_LAT, neLat);
			query.setParameter(DeviceConstant.NORTH_EAST_LONG, neLng);
			query.setParameter(DeviceConstant.SOUTH_WEST_LAT, swLat);
			query.setParameter(DeviceConstant.SOUTH_WEST_LONG, swLng);
			query.setParameter(DeviceConstant.END_DATE, endDate);
			nvDeviceGroups = query.getResultList();
			logger.info("result list size:{}", nvDeviceGroups.size());
		} catch (Exception ex) {
			logger.info("Error occur while searchAllNVDeviceGroups,err msg {}", ExceptionUtils.getStackTrace(ex));
		}
		logger.info("Returning result from searchAllNVDeviceGroups with size {}", nvDeviceGroups.size());
		return nvDeviceGroups;
	}
	
	/**
	 * Gets the NV device data wrapper by device id.
	 *
	 * @param deviceId the device id
	 * @return the NV device data wrapper by device id
	 * @throws DaoException the dao exception
	 */
	@Override
	public NVDeviceDataWrapper getNVDeviceDetailsByDeviceId(String deviceId) {
		NVDeviceDataWrapper nvDeviceDataWrapper;
		try {
			Query query = getEntityManager().createNamedQuery(DeviceConstant.GET_NV_DEVICE_DETAIL_BY_DEVICE_ID);
			query.setParameter(DeviceConstant.DEVICE_ID_PLACE_HOLDER, deviceId);
			query.setMaxResults(DeviceConstant.MAX_RESULT);
			nvDeviceDataWrapper = (NVDeviceDataWrapper) query.getSingleResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return nvDeviceDataWrapper;
	}

	
	/**
	 * Sets the pagination limits.
	 *
	 * @param ulimit the ulimit
	 * @param llimit the llimit
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
	 * Gets the all device groups count.
	 *
	 * @return the all device groups count
	 * @throws DaoException the dao exception
	 */
	@Override
	public Long getAllDeviceGroupsCount() {
		logger.info("Inside getAllDeviceGroupsCount");
		try {
			Query query = getEntityManager().createNamedQuery(DeviceConstant.GET_ALL_DEVICE_GROUP_COUNT);
			Long deviceGroupsCount = (Long)query.getSingleResult();
			logger.info("Done getAllDeviceGroups");
			return deviceGroupsCount;
		} catch (Exception e) {
			logger.error("Error in getAllDeviceGroups : {} " ,ExceptionUtils.getStackTrace(e));
			throw new DaoException(e.getMessage());
		}
	}
}
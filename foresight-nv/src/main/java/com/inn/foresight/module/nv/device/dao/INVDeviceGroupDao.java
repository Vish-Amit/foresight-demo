package com.inn.foresight.module.nv.device.dao;

import java.util.Date;
import java.util.List;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.module.nv.device.model.NVDeviceGroup;
import com.inn.foresight.module.nv.device.wrapper.NVDeviceDataWrapper;
import com.inn.foresight.module.nv.device.wrapper.NVDeviceGroupWrapper;

/**
 * The Interface INVDeviceGroupDao.
 *
 * @author innoeye
 * date - 24-Apr-2018 12:07:05 PM
 */
public interface INVDeviceGroupDao extends IGenericDao<Integer, NVDeviceGroup> {

	/**
	 * Gets the all device groups.
	 *
	 * @param ulimit the ulimit
	 * @param llimit the llimit
	 * @return the all device groups
	 * @throws DaoException the dao exception
	 */
	List<NVDeviceGroupWrapper> getAllDeviceGroups(Integer ulimit,Integer llimit);

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
	List<NVDeviceGroupWrapper> getDeviceGroupWithinViewPort(Double swLat,
			Double swLng, Double neLat, Double neLng);

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
	List<NVDeviceGroupWrapper> searchAllNVDeviceGroups(Double swLat, Double swLng,
			Double neLat, Double neLng, Integer zoomLevel, Date endDate);

	/**
	 * Gets the NV device data wrapper by device id.
	 *
	 * @param deviceId the device id
	 * @return the NV device data wrapper by device id
	 * @throws DaoException the dao exception
	 */
	NVDeviceDataWrapper getNVDeviceDetailsByDeviceId(String deviceId);

	/**
	 * Gets the all device groups count.
	 *
	 * @return the all device groups count
	 * @throws DaoException the dao exception
	 */
	Long getAllDeviceGroupsCount();

		
}
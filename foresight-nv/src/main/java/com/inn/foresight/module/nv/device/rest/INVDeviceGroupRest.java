package com.inn.foresight.module.nv.device.rest;

import javax.ws.rs.core.Response;

import com.inn.foresight.module.nv.device.wrapper.NVDeviceGroupWrapper;

/**
 * The Interface INVDeviceGroupRest.
 *
 * @author innoeye
 * date - 24-Apr-2018 12:39:59 PM
 */
public interface INVDeviceGroupRest {

	/**
	 * Gets the all device groups.
	 *
	 * @param ulimit the ulimit
	 * @param llimit the llimit
	 * @return the all device groups
	 */
	Response getAllDeviceGroups(Integer ulimit,Integer llimit);

	/**
	 * Creates the.
	 *
	 * @param probeJSON the probe JSON
	 * @return the response
	 */
	Response create(String probeJSON);

	/**
	 * Adds the device.
	 *
	 * @param id the id
	 * @param devices the devices
	 * @return the response
	 */
	Response addDevice(String id,String devices);

	/**
	 * Removes the device.
	 *
	 * @param id the id
	 * @param devices the devices
	 * @return the response
	 */
	Response removeDevice(String id,String devices);

	/**
	 * Gets the device group.
	 *
	 * @param id the id
	 * @return the device group
	 */
	Response getDeviceGroup(String id);

	/**
	 * Removes the.
	 *
	 * @param id the id
	 * @return the response
	 */
	Response remove(Integer id);

	/**
	 * Gets the device details by device id.
	 *
	 * @param deviceId the device id
	 * @return the device details by device id
	 */
	Response getDeviceDetailsByDeviceId(String deviceId);

	/**
	 * Gets the all device group count.
	 *
	 * @return the all device group count
	 */
	Response getAllDeviceGroupCount();

	/**
	 * Update device group.
	 *
	 * @param deviceGroup the device group
	 * @return the response
	 */
	Response updateDeviceGroup(NVDeviceGroupWrapper deviceGroup);

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
	 * @param dataType the data type
	 * @param kpi the kpi
	 * @param threshold the threshold
	 * @param resultType the result type
	 * @return the response
	 */
	Response searchNVDeviceGroups(Long startDate, Long endDate,Double swLat, Double swLng, Double neLat,
			Double neLng, Integer zoomLevel, String dataType, String kpi,
			Double threshold, String resultType);

}
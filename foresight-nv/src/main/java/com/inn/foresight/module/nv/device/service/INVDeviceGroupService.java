package com.inn.foresight.module.nv.device.service;

import java.util.List;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.module.nv.device.model.NVDeviceGroup;
import com.inn.foresight.module.nv.device.wrapper.NVDeviceDataWrapper;
import com.inn.foresight.module.nv.device.wrapper.NVDeviceGroupWrapper;
import com.inn.product.um.user.model.User;

/**
 * The Interface INVDeviceGroupService.
 *
 * @author innoeye
 * date - 24-Apr-2018 12:34:50 PM
 */
public interface INVDeviceGroupService extends IGenericService<Integer, NVDeviceGroup>{

	/**
	 * Gets the all device groups.
	 *
	 * @param ulimit the ulimit
	 * @param llimit the llimit
	 * @return the all device groups
	 * @throws RestException the rest exception
	 */
	List<NVDeviceGroupWrapper> getAllDeviceGroups(Integer ulimit,Integer llimit);

	/**
	 * Creates the.
	 *
	 * @param deviceGroup the device group
	 * @param creator the creator
	 * @return the NV device group
	 * @throws RestException the rest exception
	 */
	NVDeviceGroup create(NVDeviceGroup deviceGroup, User creator);
	
	/**
	 * Adds the device.
	 *
	 * @param id the id
	 * @param devices the devices
	 * @param isEncryptEnable the is encrypt enable
	 * @return the string
	 * @throws RestException the rest exception
	 */
	String addDevice(String id, String devices, boolean isEncryptEnable);

	/**
	 * Removes the device.
	 *
	 * @param id the id
	 * @param devices the devices
	 * @param isEncryptEnable the is encrypt enable
	 * @return the string
	 * @throws RestException the rest exception
	 */
	String removeDevice(String id, String devices, boolean isEncryptEnable);

	/**
	 * Gets the device group.
	 *
	 * @param id the id
	 * @param isEncryptEnable the is encrypt enable
	 * @return the device group
	 * @throws RestException the rest exception
	 */
	String getDeviceGroup(String id, boolean isEncryptEnable);

	/**
	 * Gets the all device groups.
	 *
	 * @param encryptedLat the encrypted lat
	 * @param encryptedLong the encrypted long
	 * @return the all device groups
	 * @throws RestException the rest exception
	 */
	String getAllDeviceGroups(String encryptedLat,
			String encryptedLong);

	/**
	 * Gets the device details by device id.
	 *
	 * @param deviceId the device id
	 * @return the device details by device id
	 * @throws RestException the rest exception
	 */
	NVDeviceDataWrapper getDeviceDetailsByDeviceId(String deviceId);

	/**
	 * Gets the all device groups count.
	 *
	 * @return the all device groups count
	 * @throws RestException the rest exception
	 */
	Long getAllDeviceGroupsCount();

	/**
	 * Update device group.
	 *
	 * @param deviceGroup the device group
	 * @return the NV device group wrapper
	 * @throws Exception 
	 */
	NVDeviceGroupWrapper updateDeviceGroup(NVDeviceGroupWrapper deviceGroup) throws Exception;

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
	 * @return the List<NVDeviceGroupWrapper>
	 * @throws RestException 
	 */
	List<NVDeviceGroupWrapper> searchNVDeviceGroups(Long startDate, Long endDate, Double swLat, Double swLng, Double neLat,
			Double neLng, Integer zoomLevel, String dataType, String kpi, Double threshold, String resultType);

	
}
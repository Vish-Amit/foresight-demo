package com.inn.foresight.module.nv.app.service;

import java.util.List;

import javax.ws.rs.core.Response;

import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.module.nv.app.model.DeviceInfo;
import com.inn.foresight.module.nv.app.wrapper.DeviceInfoWrapper;

/** The Interface IDeviceInfoService. */
public interface IDeviceInfoService {

	/**
	 * Creates the device.
	 *
	 * @param device
	 *            the device
	 * @return the device info
	 * @throws DaoException
	 *             the dao exception
	 */
	DeviceInfo createDevice(DeviceInfo device);

	/**
	 * Check for device info.
	 *
	 * @param apkDeviceInfo
	 *            the apk device info
	 * @return the device info
	 * @throws Exception
	 *             the exception
	 */
	DeviceInfo checkForDeviceInfo(DeviceInfo apkDeviceInfo, boolean isEncryptionReq) throws Exception;

	/**
	 * Device registration.
	 *
	 * @param apkDeviceInfo            the apk device info
	 * @param userId the user id
	 * @return the string userId
	 * @throws Exception             the exception
	 */
	Response deviceValidationAndRegistrationService(String apkDeviceInfo, String userId) ;

	/**
	 * Creates the BBM device.
	 *
	 * @param deviceInfoList the device info list
	 */
	void createBBMDevice(List<DeviceInfo> deviceInfoList);

	/**
	 * Update BBM device.
	 *
	 * @param deviceInfoList the device info list
	 * @return the list
	 */
	List<DeviceInfo> updateBBMDevice(List<DeviceInfo> deviceInfoList);

	/**
	 * Gets the device list by user name.
	 *
	 * @param userName the user name
	 * @param llimit the llimit
	 * @param ulimit the ulimit
	 * @return the device list by user name
	 */
	List<DeviceInfoWrapper> getDeviceListByUserName(List<String> userName, Integer llimit, Integer ulimit,Boolean isInstalled);

	/**
	 * Gets the device list count by user name.
	 *
	 * @param userName the user name
	 * @return the device list count by user name
	 */
	long getDeviceListCountByUserName(List<String> userName,Boolean isInstalled);

	/**
	 * Gets the device list.
	 *
	 * @param lLimit the l limit
	 * @param uLimit the u limit
	 * @return the device list
	 */
	List<DeviceInfo> getDeviceList(Integer lLimit, Integer uLimit);

	/**
	 * Gets the device list count.
	 *
	 * @return the device list count
	 * @throws Exception the exception
	 */
	long getDeviceListCount() throws Exception;

	/**
	 * Update user for device.
	 *
	 * @param deviceId the device id
	 * @return the device info
	 */
	DeviceInfo updateUserForDevice(String deviceId);

	/**
	 * Gets the NV installation detail.
	 *
	 * @param imsi the imsi
	 * @param deviceId the device id
	 * @param deviceOs the device os
	 * @return the NV installation detail
	 */
	Boolean getNVInstallationDetail(String imsi, String deviceId, String deviceOs);

	/**
	 * Gets the device info detail by imsi or device id.
	 *
	 * @param imsi the imsi
	 * @param deviceId the device id
	 * @return the device info detail by imsi or device id
	 */
	DeviceInfo getDeviceInfoDetailByImsiOrDeviceId(String imsi, String deviceId);
}

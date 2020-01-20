package com.inn.foresight.module.nv.app.dao;

import java.util.List;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.exceptions.ValueNotFoundException;
import com.inn.foresight.module.nv.app.model.DeviceInfo;
import com.inn.foresight.module.nv.app.wrapper.DeviceInfoWrapper;

/** The Interface IDeviceInfoDao. */
public interface IDeviceInfoDao extends IGenericDao<Integer, DeviceInfo>{

	/**
	 * Gets the device info by apk id.
	 *
	 * @param imei the imei
	 * @param imsi the imsi
	 * @return the device info by apk id
	 * @throws ValueNotFoundException the value not found exception
	 * @throws RestException the rest exception
	 */
	DeviceInfo getDeviceInfoByApkId(String imei, String imsi)
			throws  ValueNotFoundException;
	
	/**
	 * Gets the device info by user.
	 *
	 * @param userId the user id
	 * @return the device info by user
	 * @throws DaoException the dao exception
	 */
	DeviceInfo getDeviceInfoByUser(Integer userId) ;
	
	/**
	 * Gets the device info by imei and imsi.
	 *
	 * @param imei the imei
	 * @param imsi the imsi
	 * @return the device info by imei and imsi
	 */
	DeviceInfo getDeviceInfoByImeiAndImsi(String imei,String imsi) ;
	
	/**
	 * Gets the device info by imsi.
	 *
	 * @param imsi the imsi
	 * @return the device info by imsi
	 */
	DeviceInfo getDeviceInfoByImsi(String imsi);
	
	/**
	 * This method is used to update all deviceInfo instance in DB.
	 *
	 * @param deviceInfos the device infos
	 * @throws DaoException the dao exception
	 */
	void updateAll(List<DeviceInfo> deviceInfos) ;
	
	/**
	 * Gets the distinct make.
	 *
	 * @return the distinct make
	 * @throws DaoException the dao exception
	 */
	List<String> getDistinctMake();
	
	/**
	 * Gets the distinct model.
	 *
	 * @param make the make
	 * @return the distinct model
	 * @throws DaoException the dao exception
	 */
	List<String> getDistinctModel(String make) ;
	
	/**
	 * Gets the distinct OS.
	 *
	 * @return the distinct OS
	 * @throws DaoException the dao exception
	 */
	List<String> getDistinctOS() ;
	
	/**
	 * Gets the device info by device id.
	 *
	 * @param deviceId the device id
	 * @return the device info by device id
	 * @throws DaoException the dao exception
	 */
	DeviceInfo getDeviceInfoByDeviceId(String deviceId);
	/**
	 * Gets the device info list.
	 *
	 * @param imei the imei
	 * @param imsi the imsi
	 * @return the device info list
	 * @throws DaoException the dao exception
	 */
	List<DeviceInfo>getDeviceInfoList(String imei,String imsi);
	
	/**
	 * Gets the device info by MSISDN.
	 *
	 * @param msisdnList the msisdn list
	 * @return the device info by MSISDN
	 * @throws DaoException the dao exception
	 */
	List<DeviceInfo> getDeviceInfoByMSISDN(List<String> msisdnList) ;
	
	/**
	 * Gets the device info list by user name.
	 *
	 * @param userName the user name
	 * @param llimit the llimit
	 * @param ulimit the ulimit
	 * @return the device info list by user name
	 * @throws DaoException the dao exception
	 */
	List<DeviceInfoWrapper> getDeviceInfoListByUserName(List<String> userName, Integer llimit, Integer ulimit,Boolean isInstalled) ;

	/**
	 * Gets the device info list count by user name.
	 *
	 * @param userName the user name
	 * @return the device info list count by user name
	 * @throws DaoException the dao exception
	 */
	long getDeviceInfoListCountByUserName(String userName,Boolean isInstalled);

	/**
	 * Gets the device list.
	 *
	 * @param llimit the llimit
	 * @param ulimit the ulimit
	 * @return the device list
	 * @throws DaoException the dao exception
	 */
	List<DeviceInfo> getDeviceList(Integer llimit, Integer ulimit) ;

	/**
	 * Gets the device list count.
	 *
	 * @return the device list count
	 * @throws DaoException the dao exception
	 */
	long getDeviceListCount() ;

	/**
	 * Gets the device id by IMEI.
	 *
	 * @param imei the imei
	 * @return the device id by IMEI
	 */
	List<String> getDeviceIdByIMEI(String imei);

	/**
	 * Gets the BBM location by msisdn.
	 *
	 * @param msisdn the msisdn
	 * @return the BBM location by msisdn
	 */
	DeviceInfo getBBMLocationByMsisdn(String msisdn);
	
	/**
	 * Gets the device info by imei.
	 *
	 * @param imei the imei
	 * @return the device info by imei
	 */
	DeviceInfo getDeviceInfoByImei(String imei);

	/**
	 * Gets the device info by device id list.
	 *
	 * @param deviceIdList the device id list
	 * @return the device info by device id list
	 */
	List<DeviceInfo> getDeviceInfoByDeviceIdList(List<String> deviceIdList);

	/**
	 * Gets the device info by apk id and imei.
	 *
	 * @param imei the imei
	 * @return the device info by apk id and imei
	 * @throws ValueNotFoundException the value not found exception
	 */
	DeviceInfo getDeviceInfoByApkIdAndImei(String imei)throws ValueNotFoundException;

	/**
	 * Update device info by device id.
	 *
	 * @param deviceId the device id
	 * @param imsi the imsi
	 * @param msisdn the msisdn
	 * @return the int
	 */
	int updateDeviceInfoByDeviceId(String deviceId, String imsi, String msisdn);
	
}
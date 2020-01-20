package com.inn.foresight.module.nv.app.dao;

import java.util.List;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.exceptions.ValueNotFoundException;
import com.inn.foresight.module.nv.app.model.LicenseInfo;
import com.inn.foresight.module.nv.app.model.LicenseMaster;
import com.inn.foresight.module.nv.app.wrapper.DeviceInfoWrapper;


/**
 * The Interface ILicenseInfoDao.
 *
 * @author innoeye
 * date - 16-Nov-2017 3:36:13 PM
 */
public interface ILicenseInfoDao extends IGenericDao<Integer, LicenseInfo> {
	
	/**
	 * Gets the license info.
	 *
	 * @param imei the imei
	 * @param appName the app name
	 * @param clientId the client id
	 * @return the license info
	 * @throws ValueNotFoundException the value not found exception
	 */
	LicenseInfo getLicenseInfo(String imei,String appName,String clientId) throws ValueNotFoundException;
	
	/**
	 * Gets the license info count by license master.
	 *
	 * @param licenseMaster the license master
	 * @return the license info count by license master
	 */
	Long getLicenseInfoCountByLicenseMaster(LicenseMaster licenseMaster);

	/**
	 * Gets the license info from ID.
	 *
	 * @param licenceId the licence id
	 * @return the license info from ID
	 * @throws ValueNotFoundException the value not found exception
	 * @throws RestException the rest exception
	 */
	LicenseInfo getLicenseInfoFromID(String licenceId) throws ValueNotFoundException;
	
    List<DeviceInfoWrapper>getLicenceCountGroupByLicenceMaster();
}

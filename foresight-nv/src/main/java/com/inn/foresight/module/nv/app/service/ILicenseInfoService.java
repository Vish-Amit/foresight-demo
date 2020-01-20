package com.inn.foresight.module.nv.app.service;

import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.core.generic.exceptions.ValueNotFoundException;
import com.inn.foresight.module.nv.app.model.DeviceInfo;
import com.inn.foresight.module.nv.app.model.LicenseInfo;
import com.inn.foresight.module.nv.app.model.LicenseMaster;

/**
 * The Interface ILicenseInfoService.
 *
 * @author innoeye
 * date - 16-Nov-2017 3:41:42 PM
 */
public interface ILicenseInfoService extends IGenericService<Integer, LicenseInfo> {

	/**
	 * Gets the license info.
	 *
	 * @param apkDeviceInfo the apk device info
	 * @param isEncryptionReq 
	 * @return the license info
	 * @throws ValueNotFoundException the value not found exception
	 */
	LicenseInfo getLicenseInfo(DeviceInfo apkDeviceInfo, boolean isEncryptionReq) throws ValueNotFoundException;

	/**
	 * Creates the license info.
	 *
	 * @param apkDeviceInfo the apk device info
	 * @param licenseMaster the license master
	 * @param isEncryptionReq 
	 * @return the license info
	 * @throws DaoException the dao exception
	 */
	LicenseInfo createLicenseInfo(DeviceInfo apkDeviceInfo, LicenseMaster licenseMaster, boolean isEncryptionReq);

	/**
	 * Gets the license info count by license master.
	 *
	 * @param licenseMaster the license master
	 * @return the license info count by license master
	 */
	Long getLicenseInfoCountByLicenseMaster(LicenseMaster licenseMaster);

	/**
	 * Check license validity by id.
	 *
	 * @param licenceId the licence id
	 * @return the boolean
	 * @throws RestException the rest exception
	 */
	Boolean checkLicenseValidityById(String licenceId);
		
}
package com.inn.foresight.module.nv.app.service.impl;

import java.util.Date;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inn.commons.encoder.AESUtils;
import com.inn.commons.lang.StringUtils;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.service.impl.AbstractService;
import com.inn.foresight.core.generic.exceptions.ValueNotFoundException;
import com.inn.foresight.module.nv.app.constants.AppConstants;
import com.inn.foresight.module.nv.app.dao.ILicenseInfoDao;
import com.inn.foresight.module.nv.app.exceptions.InvalidEncryptionException;
import com.inn.foresight.module.nv.app.model.DeviceInfo;
import com.inn.foresight.module.nv.app.model.LicenseInfo;
import com.inn.foresight.module.nv.app.model.LicenseMaster;
import com.inn.foresight.module.nv.app.service.ILicenseInfoService;
import com.inn.foresight.module.nv.app.utils.AppUtils;
import com.inn.product.security.utils.AuthenticationCommonUtil;

/**
 * The Class LicenseInfoServiceImpl.
 *
 * @author innoeye
 * date - 16-Nov-2017 3:46:34 PM
 */
@Service("LicenseInfoServiceImpl")
public class LicenseInfoServiceImpl extends AbstractService<Integer, LicenseInfo> implements ILicenseInfoService {

	/** The Logger *. */
	Logger logger = LogManager.getLogger(LicenseInfoServiceImpl.class);

	/** The LicenseInfo dao. */
	private ILicenseInfoDao dao;

	/**
	 * Instantiates a new license info service impl.
	 *
	 * @param licenseInfoDao the license info dao
	 */
	@Autowired
	public LicenseInfoServiceImpl(ILicenseInfoDao licenseInfoDao) {
		super.setDao(licenseInfoDao);
		this.dao = licenseInfoDao;
	}

	/**
	 * Gets the license info.
	 *
	 * @param apkDeviceInfo the apk device info
	 * @return the license info
	 * @throws ValueNotFoundException the value not found exception
	 */
	@Override
	@Transactional(readOnly=true)
	public LicenseInfo getLicenseInfo(DeviceInfo apkDeviceInfo,boolean isEncryptionReq) throws ValueNotFoundException{
		String oldImei = apkDeviceInfo.getOldImei();
		String imei = apkDeviceInfo.getImei();
		if(oldImei != null && isEncryptionReq) {
			oldImei = AESUtils.encrypt(oldImei);
		}
		if(imei != null && isEncryptionReq) {
			imei = AESUtils.encrypt(imei);
		}
		logger.info("Going to get LicenseInfo for apkId : {} & clientName : {}",
				apkDeviceInfo.getApkDetail().getApkId(),apkDeviceInfo.getClientName());
		return dao.getLicenseInfo(StringUtils.isNotEmpty(oldImei) ? oldImei :imei,
				apkDeviceInfo.getApkDetail().getApkId(), apkDeviceInfo.getClientName());
	}
	
	/**
	 * Creates the license info.
	 *
	 * @param apkDeviceInfo the apk device info
	 * @param licenseMaster the license master
	 * @return the license info
	 * @throws DaoException the dao exception
	 */
	@Override
	public LicenseInfo createLicenseInfo(DeviceInfo apkDeviceInfo, LicenseMaster licenseMaster, boolean isEncryptionReq) {
		logger.info("Going to create LicenseInfo");
		LicenseInfo licenseInfo = new LicenseInfo();
		String imei = apkDeviceInfo.getImei();
		if (isEncryptionReq) {
		//		licenseInfo.setImei(apkDeviceInfo.getImei());
			imei = AESUtils.encrypt(apkDeviceInfo.getImei());
		}
		licenseInfo.setImei(imei);
		licenseInfo.setLicenceIssueDate(new java.sql.Date(new Date().getTime()));
		licenseInfo.setLicenseMaster(licenseMaster);
		licenseInfo.setCreatedTime(new java.sql.Date(new Date().getTime()));
		licenseInfo.setModifiedTime(new java.sql.Date(new Date().getTime()));
		licenseInfo = dao.create(licenseInfo);
		String licenseId = AppUtils.generateUniqueIdWithPrefix(licenseInfo.getId(), AppConstants.LICENCE_ID_PREFIX);
		licenseInfo.setLicenceId(licenseId);
		licenseInfo.setModifiedTime(new java.sql.Date(new Date().getTime()));
		return dao.update(licenseInfo);
	}
	
	/**
	 * Gets the license info count by license master.
	 *
	 * @param licenseMaster the license master
	 * @return the license info count by license master
	 */
	@Override
	public Long getLicenseInfoCountByLicenseMaster(LicenseMaster licenseMaster) {
		logger.info("Going to get LicenseInfo counts");
		return dao.getLicenseInfoCountByLicenseMaster(licenseMaster);
	}

	/**
	 * Cheking whether the Licence issued for given Licence Id is valid or not.
	 *
	 * @param licenceId the licence id
	 * @return true if licence is valid, false otherwise
	 * @throws RestException the rest exception
	 * @throw RestException, if something went wrong
	 */
	@Override
	@Transactional
	public Boolean checkLicenseValidityById(String licenceId) {
		logger.info("Inside checkLicenseValidityById(), licenceId {}", licenceId);	
		try {
			LicenseInfo licenceInfo = dao.getLicenseInfoFromID(AuthenticationCommonUtil.checkForValueDecryption(licenceId));
			return AppUtils.isValidLicense(licenceInfo.getLicenseMaster().getLicenseValidity(),
					licenceInfo.getCreatedTime());
		} catch (ValueNotFoundException e) {
			return false;
		} catch (InvalidEncryptionException e) {
			logger.error(AppConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(AppConstants.INVALID_ENCRYPTED_VALUE);
		} catch (NullPointerException e) {
			logger.error(AppConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(AppConstants.INVALID_LICENCE_DETAILS);
		} catch (Exception e) {
			logger.error(AppConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(AppConstants.SOMETHING_WENT_WRONG);
		}
	}

	
	
}

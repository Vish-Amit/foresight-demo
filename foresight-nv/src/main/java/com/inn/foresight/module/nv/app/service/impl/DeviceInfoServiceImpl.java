package com.inn.foresight.module.nv.app.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.encoder.AESUtils;
import com.inn.commons.lang.StringUtils;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.utils.ApplicationContextProvider;
import com.inn.foresight.core.generic.exceptions.ValueNotFoundException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.model.AdvanceSearch;
import com.inn.foresight.core.infra.model.AdvanceSearchConfiguration;
import com.inn.foresight.core.infra.service.IAdvanceSearchConfigurationService;
import com.inn.foresight.core.infra.service.IAdvanceSearchProvider;
import com.inn.foresight.core.infra.service.IAdvanceSearchService;
import com.inn.foresight.core.infra.utils.InfraConstants;
import com.inn.foresight.module.nv.NVConfigUtil;
import com.inn.foresight.module.nv.app.constants.AppConstants;
import com.inn.foresight.module.nv.app.dao.IAPKDetailDao;
import com.inn.foresight.module.nv.app.dao.IDeviceInfoDao;
import com.inn.foresight.module.nv.app.dao.ILicenseMasterDao;
import com.inn.foresight.module.nv.app.exceptions.InvalidAppDetailsException;
import com.inn.foresight.module.nv.app.exceptions.InvalidDeviceDetailException;
import com.inn.foresight.module.nv.app.exceptions.LicenseIsExpiredException;
import com.inn.foresight.module.nv.app.model.APKDetail;
import com.inn.foresight.module.nv.app.model.APKDetail.APP_OS;
import com.inn.foresight.module.nv.app.model.DeviceInfo;
import com.inn.foresight.module.nv.app.model.DeviceInfo.Source;
import com.inn.foresight.module.nv.app.model.LicenseInfo;
import com.inn.foresight.module.nv.app.model.LicenseMaster;
import com.inn.foresight.module.nv.app.service.IDeviceInfoService;
import com.inn.foresight.module.nv.app.service.ILicenseInfoService;
import com.inn.foresight.module.nv.app.utils.AppUtils;
import com.inn.foresight.module.nv.app.wrapper.DeviceInfoWrapper;
import com.inn.foresight.module.nv.device.constant.DeviceConstant;
import com.inn.foresight.module.nv.device.dao.INVDeviceDataDao;
import com.inn.foresight.module.nv.device.model.NVDeviceData;
import com.inn.foresight.module.nv.device.service.INVDeviceDataService;
import com.inn.foresight.module.nv.device.util.NVDeviceUtil;
import com.inn.foresight.module.nv.fcm.service.IFCMServerService;
import com.inn.foresight.module.nv.pushnotification.model.PushNotification.OS;
import com.inn.product.security.utils.AuthenticationCommonUtil;
import com.inn.product.um.user.dao.UserDao;

/** The Class DeviceInfoServiceImpl. */
@Service("DeviceInfoServiceImpl")
public class DeviceInfoServiceImpl extends AppUtils implements IDeviceInfoService ,IAdvanceSearchProvider{

	/** The logger. */
	private Logger logger = LogManager.getLogger(DeviceInfoServiceImpl.class);

	/** Instance of DeviceInfoDaoImpl. */
	@Autowired
	private IDeviceInfoDao dao;

	/** Instance of ApkDetail Service. */
	@Autowired
	private IAPKDetailDao apkDetailDao;

	/** Instance of LicenseMaster dao. */
	@Autowired
	private ILicenseMasterDao licenseMasterDao;

	/** Instance of DeviceInfo dao. */
	@Autowired
	private IDeviceInfoDao deviceInfoDao;

	/** Instance of LicenseInfo Service. */
	@Autowired
	private ILicenseInfoService licenseInfoService;

	/** The user dao. */
	@Autowired
	private UserDao userDao;

	@Autowired
	private INVDeviceDataService iNVDeviceDataService;

	@Autowired
	private INVDeviceDataDao iNVDeviceDataDao;
	
	@Autowired
	private IAdvanceSearchService iAdvanceSerach;
	
	@Autowired
	private IAdvanceSearchConfigurationService iAdvanceSearchConfigurationService;

	/**
	 * Method used to create DeviceInfo record and update deviceId generated from
	 * record PK.
	 *
	 * @param device the device
	 * @return DeviceInfo Object Created
	 * @throws DaoException the dao exception
	 */
	@Override
	public DeviceInfo createDevice(DeviceInfo device) {
		logger.info("Inside registerDevice()");
		DeviceInfo createdDevice = dao.create(device);
		String deviceId = generateUniqueIdWithPrefix(createdDevice.getId(), DEVICE_ID_PREFIX);
		createdDevice.setDeviceId(deviceId);
		createdDevice.setModificationTime(new Date());
		
		try {
			if(createdDevice.getUserCustomName() != null) {
				   addEntryInAdvanceSearch(createdDevice);
				}
		} catch (Exception e) {
			logger.error("error for insert data into AdvanceSearch table {}",ExceptionUtils.getStackTrace(e));
		}
		return dao.update(createdDevice);
	}

	private void addEntryInAdvanceSearch(DeviceInfo createdDevice) {
		logger.info("add new entry in AdvanceSearch table");
		AdvanceSearchConfiguration advanceSearchConfiguration = iAdvanceSearchConfigurationService.findAll().stream()                      
		            .filter(x -> x.getType().equalsIgnoreCase(DeviceConstant.DEVICE_ID))       
		            .findAny()                                     
		            .orElse(null);   
					
			AdvanceSearch advanceSearch = new AdvanceSearch();
			advanceSearch.setAdvanceSearchConfiguration(advanceSearchConfiguration);
			advanceSearch.setName(createdDevice.getUserCustomName());
			advanceSearch.setDisplayName(createdDevice.getUserCustomName());
			advanceSearch.setPriorityValue(ForesightConstants.ONE);
			advanceSearch.setTypereference(createdDevice.getId());
			advanceSearch.setRowKeyPrefix(createdDevice.getDeviceId());
			advanceSearch.setCreationTime(new Date());
			iAdvanceSerach.create(advanceSearch);
	}

	/**
	 * Checking whether DeviceInfo record with given details already exist or not,
	 * if exist then return the existing record else create and return new record.
	 *
	 * @param apkDeviceInfo the apk device info
	 * @param isEncryptionReq 
	 * @return DeviceInfo Object
	 * @throws Exception the exception
	 */
	@Override
	public DeviceInfo checkForDeviceInfo(DeviceInfo apkDeviceInfo, boolean isEncryptionReq) throws Exception {
		String imei = apkDeviceInfo.getImei();
		String imsi = apkDeviceInfo.getImsi();
		String oldImei = apkDeviceInfo.getOldImei();
		if (imei != null && isEncryptionReq) {
			imei = AESUtils.encrypt(imei);
		}
		if (imsi != null && isEncryptionReq) {
			imsi = AESUtils.encrypt(imsi);
		}
		if (oldImei != null && isEncryptionReq) {
			oldImei = AESUtils.encrypt(oldImei);
		}
		
		logger.info("checkForDeviceInfo oldImei,imei,imsi {} {} {}",oldImei,imei,imsi);
		try {
			if (APP_OS.IPHONE.name().equalsIgnoreCase(apkDeviceInfo.getApkDetail().getApkOS().name())) {
				return dao.getDeviceInfoByApkIdAndImei(
						StringUtils.isNotEmpty(oldImei) ? oldImei
								: imei);

			} else {

				return dao.getDeviceInfoByApkId(
						StringUtils.isNotEmpty(oldImei) ? oldImei
								: imei,
								imsi);

			}
		} catch (ValueNotFoundException e) {
			throw e;
		} catch (Exception e) {
			logger.error(EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}

	/**
	 * Casting LicenseWrapper Into DeviceInfo instance.
	 *
	 * @param apkDeviceInfo the apk device info
	 * @param uId
	 * @param isEncryptionReq 
	 * @return DeviceInfo Object Created
	 * @throws Exception the exception
	 */
	private DeviceInfo castWrapperToDeviceInfo(DeviceInfo apkDeviceInfo, Integer uId, APKDetail apkDetail, boolean isEncryptionReq) {
		DeviceInfo device = new DeviceInfo();
		setImei(device, apkDeviceInfo.getImei(), isEncryptionReq);
		setImsi(device, apkDeviceInfo.getImsi(), isEncryptionReq);
		device.setDeviceOS(apkDeviceInfo.getDeviceOS());
		device.setMake(apkDeviceInfo.getMake());
		device.setModel(apkDeviceInfo.getModel());
		device.setAppVersion(apkDeviceInfo.getAppVersion());
		device.setApkDetail(apkDetail);
		device.setCreationTime(new Date());
		device.setModificationTime(new Date());
		if (uId != null) {
			device.setUser(userDao.findByPk(uId));
		}
		setFcmKey(apkDeviceInfo, device);
		String deviceCustomName = apkDeviceInfo.getUserCustomName();
		if ( deviceCustomName != null) {
			device.setUserCustomName(deviceCustomName);
		}
		

		if(apkDeviceInfo.getSdkAppVersion() != null) {
			device.setSdkAppVersion(apkDeviceInfo.getSdkAppVersion());
		}
		if(apkDeviceInfo.getAppName() != null) {
			device.setAppName(apkDeviceInfo.getAppName());
		}
		if(apkDeviceInfo.getPseudoId() != null) {
			device.setPseudoId(apkDeviceInfo.getPseudoId());
		}
		return device;
	}

	@Override
	@Transactional
	public Response deviceValidationAndRegistrationService(String apkDeviceJSON, String uId) {
		Boolean isGCMAlgo=null;
		try {
			isGCMAlgo=AuthenticationCommonUtil.isGCMAlgorithm(apkDeviceJSON);
			
			if (StringUtils.isNotBlank(apkDeviceJSON) && StringUtils.isNotEmpty(apkDeviceJSON)) {
				DeviceInfoWrapper deviceValidationAndRegistration = deviceValidationAndRegistration(apkDeviceJSON, uId);
				return Response.ok(AuthenticationCommonUtil.checkForValueEncryption(deviceValidationAndRegistration,isGCMAlgo))
						.build();
			} else {
				logger.error(INVALID_APP_DETAILS);
				return Response.ok(AuthenticationCommonUtil.checkForValueEncryption(new DeviceInfoWrapper(FAILURE, INVALID_APP_DETAILS),isGCMAlgo))
						.build();
			}
		} catch (Exception e) {
			logger.error("Exception inside the method deviceValidationAndRegistrationService {} ", Utils.getStackTrace(e));
			return Response.ok(AuthenticationCommonUtil.checkForValueEncryption(new DeviceInfoWrapper(FAILURE, e.getMessage()),isGCMAlgo)).build();
		}

	}

	/**
	 * This method is used to validate deviceInfo & license respectively and if
	 * device is not registered then register it & generate license for the same.
	 *
	 * @param apkDeviceJSON the apk device JSON
	 * @param uId           the u id
	 * @return a JSON containing deviceId licenseId & daysToExpiry
	 * @throws Exception the exception
	 */

public DeviceInfoWrapper deviceValidationAndRegistration(String apkDeviceJSON, String uId) throws Exception {
		logger.info("Going for deviceValidationAndRegistration");
		Boolean isEncryptionReq = ConfigUtils.getBoolean(NVConfigUtil.IS_ENCRYPTION_REQ);
		DeviceInfo apkDeviceInfo = new Gson().fromJson(AuthenticationCommonUtil.checkForValueDecryption(apkDeviceJSON), DeviceInfo.class);
		logger.info("apkDeviceInfo ==={}",apkDeviceInfo != null ? new Gson().toJson(apkDeviceInfo): apkDeviceInfo);
		if (apkDeviceInfo != null && apkDeviceInfo.getImei() != null && apkDeviceInfo.getImsi() != null) {
			Integer userId = uId != null ? Integer.parseInt(AuthenticationCommonUtil.checkForValueDecryption(uId)) : null;
			try {
				return deviceValidation(apkDeviceInfo, userId,isEncryptionReq);
			} catch (ValueNotFoundException e) {
				return deviceRegistration(apkDeviceInfo, userId,isEncryptionReq);
			} catch (DaoException e) {
				logger.error("Exception in :update DeviceInfo {}", ExceptionUtils.getStackTrace(e));
				throw new InvalidDeviceDetailException(AppConstants.INVALID_DEVICE_DETAIL);
			}
		} else {
			logger.error("Exception in :deviceValidationAndRegistration imei or imsi is null");
			throw new InvalidDeviceDetailException(AppConstants.INVALID_DEVICE_DETAIL);
		}
	}

	/**
	 * This method is used to validate deviceInfo & license respectively.
	 *
	 * @param apkDeviceInfo the apk device info
	 * @param userId        the user id
	 * @param isEncryptionReq 
	 * @return the string
	 * @throws ValueNotFoundException    the value not found exception
	 * @throws Exception                 the exception
	 * @throws LicenseIsExpiredException the license is expired exception
	 */
private DeviceInfoWrapper deviceValidation(DeviceInfo apkDeviceInfo, Integer userId,boolean isEncryptionReq) throws Exception {
	logger.info("Validating device .......");
	DeviceInfo deviceInfo = updateDeviceInfoDetails(apkDeviceInfo, userId,isEncryptionReq);
	Integer daysToExpiry = 10000;
	long dateToExpiry = 10000;
	String licenceId = AppConstants.NA;
	if (apkDeviceInfo.getApkDetail().getApkId().toUpperCase().contains(AppConstants.ENG_BUILD_ID)) {
		LicenseInfo licenseInfo = licenseInfoService.getLicenseInfo(apkDeviceInfo, isEncryptionReq);
		daysToExpiry = getLicenseDaysToExpiry(licenseInfo.getLicenseMaster().getLicenseValidity(),
				licenseInfo.getLicenceIssueDate());
		dateToExpiry = NVDeviceUtil.getExpiryDateInMillis(daysToExpiry);
		if (daysToExpiry > 0) {
			return new DeviceInfoWrapper(deviceInfo.getDeviceId(), licenseInfo.getLicenceId(), daysToExpiry,
					dateToExpiry, userId, SUCCESS);
		} else {
			logger.error("Error Inside deviceValidation : {}", AppConstants.LICENSE_IS_EXPIRED);
			throw new LicenseIsExpiredException(AppConstants.LICENSE_IS_EXPIRED);
		} 
	}
	return new DeviceInfoWrapper(deviceInfo.getDeviceId(), licenceId, daysToExpiry,
			dateToExpiry, userId, SUCCESS);
}
	/**
	 * This method is used to create or update deviceInfo record for apkDeviceInfo.
	 *
	 * @param apkDeviceInfo the apk device info
	 * @param uId         
	 * @param isEncryptionReq 
	 * @return updated deviceInfo
	 * @throws Exception the exception
	 */
	private DeviceInfo updateDeviceInfoDetails(DeviceInfo apkDeviceInfo, Integer uId, boolean isEncryptionReq) throws Exception {
		APKDetail apkDetail = null;
		try {
			apkDetail = getAPKDetails(apkDeviceInfo);
		} catch (ValueNotFoundException e) {
			logger.error(AppConstants.INVALID_APP_DETAILS);
			logger.error("Exception in :getting ApkDetail : {}", e.getMessage());
			throw new InvalidAppDetailsException(AppConstants.INVALID_APP_DETAILS);
		}
		DeviceInfo deviceInfo = null;
		try {
			deviceInfo = checkForDeviceInfo(apkDeviceInfo,isEncryptionReq);
			deviceInfo.setAppVersion(apkDeviceInfo.getAppVersion());
			deviceInfo.setDeviceOS(apkDeviceInfo.getDeviceOS());
			setImei(deviceInfo,apkDeviceInfo.getImei(),isEncryptionReq);
			setAPKDetails(apkDeviceInfo, deviceInfo, apkDetail);
			setFcmKey(apkDeviceInfo, deviceInfo);
			deviceInfo.setModificationTime(new Date());
			if (uId != null) {
				deviceInfo.setUser(userDao.findByPk(uId));
			}
			deviceInfo.setIsInstalled(Boolean.TRUE);
			createDevcieIIDEntryInAdvanceSearch(apkDeviceInfo, deviceInfo, apkDeviceInfo.getUserCustomName(),
					deviceInfo.getUserCustomName());
			
			if(apkDeviceInfo.getSdkAppVersion() != null) {
				deviceInfo.setSdkAppVersion(apkDeviceInfo.getSdkAppVersion());
			}
			if(apkDeviceInfo.getAppName() != null) {
				deviceInfo.setAppName(apkDeviceInfo.getAppName());
			}
			if(apkDeviceInfo.getPseudoId() != null) {
				deviceInfo.setPseudoId(apkDeviceInfo.getPseudoId());
			}
			deviceInfo = deviceInfoDao.update(deviceInfo);
		} catch (ValueNotFoundException e) {
			deviceInfo = createDevice(castWrapperToDeviceInfo(apkDeviceInfo, uId, apkDetail,isEncryptionReq));
		}
		updateNVDeviceData(deviceInfo,apkDeviceInfo);
		return deviceInfo;

	}

	private void setImei(DeviceInfo deviceInfo, String imei, boolean isEncryptionReq) {
		if(isEncryptionReq) {
			deviceInfo.setImei(AESUtils.encrypt(imei));
		}
		else {
			deviceInfo.setImei(imei);
		}
		
	}
	
	private void setImsi(DeviceInfo deviceInfo, String imsi, boolean isEncryptionReq) {
		if(isEncryptionReq) {
			deviceInfo.setImsi(AESUtils.encrypt(imsi));
		}
		else {
			deviceInfo.setImsi(imsi);
		}
		
	}

	private void createDevcieIIDEntryInAdvanceSearch(DeviceInfo apkDeviceInfo, DeviceInfo deviceInfo,
			String newUserCustomName, String oldUserCustomName) {
		logger.info("Custom user new name {} and old name {}",newUserCustomName,oldUserCustomName);

		try {
			if (newUserCustomName != null
					&& (oldUserCustomName == null || !(newUserCustomName.equalsIgnoreCase(oldUserCustomName)))) {
				List<AdvanceSearch> advanceSearchByName = iAdvanceSerach.getAdvanceSearchByName(oldUserCustomName);
				logger.debug("Size of list {}", advanceSearchByName.size());
				if (advanceSearchByName.size() > ForesightConstants.ZERO) {
					logger.info("update entry in AdvanceSearch table");
					AdvanceSearch advanceSearch = advanceSearchByName.get(ForesightConstants.ZERO);
					advanceSearch.setId(advanceSearch.getId());
					advanceSearch.setName(newUserCustomName);
					advanceSearch.setRowKeyPrefix(deviceInfo.getDeviceId());
					advanceSearch.setDisplayName(newUserCustomName);
					iAdvanceSerach.update(advanceSearch);
				} else {
					addEntryInAdvanceSearch(apkDeviceInfo);
				}

			}
		} catch (Exception e) {
			logger.error("Getting Exception for update userCustom name in AdvanceSerach table {}",e.getMessage());
		}
		if (newUserCustomName != null) {
			deviceInfo.setUserCustomName(newUserCustomName);
		}
	}

	private void updateNVDeviceData(DeviceInfo deviceInfo, DeviceInfo apkDeviceInfo) {
		// Removing Check now both Nvdevice is created in Registration call
		NVDeviceData nvDeviceData;
		try {
			nvDeviceData = iNVDeviceDataService.findNVDeviceDataByDeviceId(deviceInfo.getDeviceId());
			if (nvDeviceData != null) {
				setDeviceInfoDetail(deviceInfo, nvDeviceData, apkDeviceInfo);
				iNVDeviceDataDao.update(nvDeviceData);
			}
			else {
				nvDeviceData = new NVDeviceData();
				setDeviceInfoDetail(deviceInfo, nvDeviceData,apkDeviceInfo);
				createNVDevideData(nvDeviceData);	
			}
		} catch (Exception e) {
			logger.error("Exception updateNVDeviceData: {}", ExceptionUtils.getStackTrace(e));
		}

	}

	private void createNVDevideData(NVDeviceData nvDeviceData) {
		try {
			iNVDeviceDataDao.create(nvDeviceData);
		} catch (RestException e) {
			logger.error("Exception createNVDeviceData: {}", ExceptionUtils.getStackTrace(e));
		}
	}

	private void setDeviceInfoDetail(DeviceInfo deviceInfo, NVDeviceData nvDeviceData, DeviceInfo apkDeviceInfo) {
		if (nvDeviceData.getCreationTime() == null) {
			nvDeviceData.setCreationTime(new Date());
		}
		nvDeviceData.setAppVersion(deviceInfo.getAppVersion());
		nvDeviceData.setModificationTime(new Date());
		nvDeviceData.setDeviceInfo(deviceInfo);
		nvDeviceData.setImei(deviceInfo.getImei());
		nvDeviceData.setImsi(deviceInfo.getImsi());
		nvDeviceData.setMake(deviceInfo.getMake());
		nvDeviceData.setModel(deviceInfo.getModel());
		nvDeviceData.setOs(deviceInfo.getDeviceOS());
		if (apkDeviceInfo != null && apkDeviceInfo.getApkDetail() != null) {
			nvDeviceData.setModule(apkDeviceInfo.getApkDetail().getApkId());
		}
	}

	private void setFcmKey(DeviceInfo apkDeviceInfo, DeviceInfo deviceInfo) {
		String apkID = deviceInfo.getApkDetail().getApkId();
		if (checkApkIDEquals(apkID)|| checkApkContains(apkID)) {
			deviceInfo.setFcmKey(apkDeviceInfo.getFcmKey());
		} else {
			deviceInfo.setClientFcmKey(apkDeviceInfo.getFcmKey());
		}
	}

	private boolean checkApkIDEquals(String apkID) {
		return "NV_ENTERPRISE".equalsIgnoreCase(apkID) || getApkIDStartAndEnd(apkID)
				|| apkID.equalsIgnoreCase("NV_CONSUMER");
	}

	private boolean checkApkContains(String apkID) {
		return apkID.contains("NV_POC")|| apkID.contains("RAKUTEN_NVAPPSTORE")|| apkID.contains("RAKUTEN_NVPLAYSTORE");
	}

	private boolean getApkIDStartAndEnd(String apkID) {
		return apkID.startsWith("NV_") && apkID.endsWith("_ENG");
	}

	/**
	 * Check APK details.
	 *
	 * @param apkDeviceInfo the apk device info
	 * @param deviceInfo    the device info
	 * @throws ValueNotFoundException the value not found exception
	 */
	private void setAPKDetails(DeviceInfo apkDeviceInfo, DeviceInfo deviceInfo, APKDetail apkDetail) {
		if (!(deviceInfo.getApkDetail().getVersionName()
				.equalsIgnoreCase(apkDeviceInfo.getApkDetail().getVersionName()))
				|| !(deviceInfo.getApkDetail().getApkOS().equals(apkDeviceInfo.getApkDetail().getApkOS())) || !(deviceInfo.getApkDetail()
						.getApkId()
						.equals(apkDeviceInfo.getApkDetail().getApkId()))) {
			deviceInfo.setApkDetail(apkDetail);
		}
	}

	private APKDetail getAPKDetails(DeviceInfo apkDeviceInfo) throws ValueNotFoundException {

		try {
			return apkDetailDao.getAPKDetailById(apkDeviceInfo.getApkDetail().getApkId(),
					apkDeviceInfo.getApkDetail().getApkOS(), apkDeviceInfo.getApkDetail().getVersionName(),
					Boolean.FALSE);
		} catch (RestException e) {
			throw new ValueNotFoundException(e);
		}
	}

	/**
	 * This method is used to register new deviceInfo.
	 *
	 * @param apkDeviceInfo the apk device info
	 * @param uId           the u id
	 * @return a JSON containing deviceId licenseId & daysToExpiry for new device
	 * @throws Exception the exception
	 */
	private DeviceInfoWrapper deviceRegistration(DeviceInfo apkDeviceInfo, Integer uId,Boolean isEncryptionReq) throws Exception {
		logger.info("Registering Device.......");
		try {
			LicenseMaster licenseMaster = licenseMasterDao.getLicenseMaster(apkDeviceInfo.getApkDetail().getApkId(),
					apkDeviceInfo.getClientName());
			LicenseInfo licenseInfo;
			DeviceInfo deviceInfo = null;
			Integer daysToExpiry = 10000;
			long dateToExpiry = 10000;
			String licenceId = AppConstants.NA;
			deviceInfo = updateDeviceInfoDetails(apkDeviceInfo, uId,isEncryptionReq);
			if (apkDeviceInfo.getApkDetail().getApkId().toUpperCase().contains(AppConstants.ENG_BUILD_ID) ) {
				licenseInfo = licenseInfoService.createLicenseInfo(apkDeviceInfo, licenseMaster, isEncryptionReq);
				 daysToExpiry = getLicenseDaysToExpiry(licenseInfo.getLicenseMaster().getLicenseValidity(),
						licenseInfo.getLicenceIssueDate());
				 dateToExpiry = NVDeviceUtil.getExpiryDateInMillis(daysToExpiry);
				 licenceId = licenseInfo.getLicenceId();
			}
					
			logger.info("values are {} {} {} {} {}",deviceInfo.getDeviceId(), licenceId, daysToExpiry,
							dateToExpiry, uId);
					
				return new DeviceInfoWrapper(deviceInfo.getDeviceId(), licenceId, daysToExpiry,
						dateToExpiry, uId, SUCCESS);

		} catch (ValueNotFoundException e) {
			logger.error("ValueNotFoundException in :create LicenseInfo {}", AppConstants.INVALID_APP_DETAILS);
			throw new InvalidAppDetailsException(AppConstants.INVALID_APP_DETAILS);
		} catch (DaoException d) {
			logger.error("Exception in :create LicenseInfo {}", ExceptionUtils.getStackTrace(d));
			throw new RestException(d);
		}
	}

	@Override
	@Transactional
	public void createBBMDevice(List<DeviceInfo> deviceInfoList) {
		List<DeviceInfo> persistDeviceInfoList = dao
				.getDeviceInfoByDeviceIdList(NVDeviceUtil.getDeviceIdList(deviceInfoList));
		logger.info("persistDeviceInfoList size : {}", persistDeviceInfoList.size());
		logger.info("deviceInfoList size : {}", deviceInfoList.size());
		for (DeviceInfo deviceInfo : deviceInfoList) {
			DeviceInfo persistDeviceInfo = NVDeviceUtil.getDeviceByDeviceID(deviceInfo, persistDeviceInfoList);
			if (persistDeviceInfo == null) {
				deviceInfo.setCreationTime(new Date());
				deviceInfo.setModificationTime(new Date());
				deviceInfo.setIsRegistered(Boolean.TRUE);
				deviceInfo.setSource(Source.BBM);
				dao.create(deviceInfo);
				logger.info("BBM Device registered");
			} else if (Boolean.FALSE.equals(persistDeviceInfo.getIsRegistered())) {
				persistDeviceInfo.setIsRegistered(Boolean.TRUE);
				persistDeviceInfo.setModificationTime(new Date());
				persistDeviceInfo.setMsisdn(deviceInfo.getMsisdn());
				persistDeviceInfo.setImei(deviceInfo.getImei());
				persistDeviceInfo.setRegistrationDate(deviceInfo.getRegistrationDate());
				persistDeviceInfo.setIsInstalled(Boolean.TRUE);
				dao.update(persistDeviceInfo);
				logger.info("BBM Device Re-registered for device ID : {}", persistDeviceInfo.getDeviceId());
			}
		}
	}

	@Override
	@Transactional
	public List<DeviceInfo> updateBBMDevice(List<DeviceInfo> deviceInfoList) {
		List<DeviceInfo> persistDeviceList = dao.getDeviceInfoByMSISDN(NVDeviceUtil.getMSISDNList(deviceInfoList));

		if (persistDeviceList != null) {
			for (DeviceInfo persistDevice : persistDeviceList) {
				DeviceInfo deviceInfo = NVDeviceUtil.getDeviceByMSISDN(persistDevice, deviceInfoList);
				if (deviceInfo != null) {
					persistDevice.setDeregistrationDate(deviceInfo.getDeregistrationDate());
					persistDevice.setRemark(deviceInfo.getRemark());
					persistDevice.setIsRegistered(Boolean.FALSE);
					persistDevice.setIsInstalled(Boolean.FALSE);
					persistDevice.setModificationTime(new Date());
				}
			}
			if (persistDeviceList.size() <= AppConstants.BULK_UPDATE_BATCH_SIZE) {
				dao.updateAll(persistDeviceList);
			} else {
				List<List<DeviceInfo>> sublist = ListUtils.partition(persistDeviceList,
						AppConstants.BULK_UPDATE_BATCH_SIZE);
				if (sublist != null) {
					sublist.forEach(l -> dao.updateAll(l));
				}
			}
			logger.info("Done BBM Device update");
		}

		return persistDeviceList;
	}

	@Override
	public List<DeviceInfoWrapper> getDeviceListByUserName(List<String> userName, Integer llimit, Integer ulimit,
			Boolean isInstalled) {
		logger.info("Going to getDeviceListByUserName {} ", userName);
		List<DeviceInfoWrapper> list = dao.getDeviceInfoListByUserName(userName, llimit, ulimit, isInstalled);
		if (list != null && !list.isEmpty()) {
			return filterDataListByModificationTime(list);

		}

		return Collections.emptyList();

	}

	private List<DeviceInfoWrapper> filterDataListByModificationTime(List<DeviceInfoWrapper> list) {
		try {
			List<DeviceInfoWrapper> newDeviceList = new ArrayList<>();
			list = list.stream().filter(d -> d.getImei() != null).collect(Collectors.toList());
			Map<String, List<DeviceInfoWrapper>> imeiWiseGroup = list.stream()
					.collect(Collectors.groupingBy(DeviceInfoWrapper::getImei));
			imeiWiseGroup.forEach((k, v) -> {
				if (v != null && !v.isEmpty()) {
					if (v.size() > DeviceConstant.SECOND_INDEX) {
						newDeviceList.add(v.get(DeviceConstant.FIRST_INDEX));
					} else {
						newDeviceList.addAll(v);
					}
				}
			});
			return newDeviceList;
		} catch (Exception e) {
			logger.error("Exception inside the method filterDataListByModificationTime {}", Utils.getStackTrace(e));
		}
		return list;
	}

	@Override
	public long getDeviceListCountByUserName(List<String> userName, Boolean isInstalled) {
		logger.info("Going to getDeviceIdListCountByUserName {} ", userName);
		List<DeviceInfoWrapper> list = dao.getDeviceInfoListByUserName(userName, null, null, isInstalled);
		if (list != null && !list.isEmpty()) {
			List<DeviceInfoWrapper> newlist = filterDataListByModificationTime(list);
			return newlist.size();
		}
		return DeviceConstant.FIRST_INDEX;
	}

	@Override
	public List<DeviceInfo> getDeviceList(Integer llimit, Integer ulimit) {
		logger.info("Going to getDeviceList ");
		List<DeviceInfo> list = dao.getDeviceList(llimit, ulimit);
		if (list != null && !list.isEmpty()) {
			return list;
		}
		return Collections.emptyList();

	}

	@Override
	public long getDeviceListCount() throws Exception {
		logger.info("Going to getDeviceListCount ");
		try {
			return dao.getDeviceListCount();
		} catch (Exception e) {
			logger.error("Error in getDeviceListCount : {}", ExceptionUtils.getStackTrace(e));
			throw e;
		}

	}

	@Override
	@Transactional
	public DeviceInfo updateUserForDevice(String deviceId) {
		logger.info("Going to updateUserForDevice for deviceId: {}", deviceId);
		try {
			DeviceInfo deviceInfo = dao.getDeviceInfoByDeviceId(deviceId);
			if (deviceInfo != null) {
				deviceInfo.setUser(null);
				deviceInfo.setModificationTime(new Date());
				return dao.update(deviceInfo);
			}
		} catch (Exception e) {
			logger.error("Error in updateUserForDevice : {}", ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public DeviceInfo getDeviceInfoDetailByImsiOrDeviceId(String imsi, String deviceId) {
		DeviceInfo deviceInfo = null;
		try {
			if (Boolean.TRUE.equals(Utils.checkForValueInString(imsi))) {
				logger.info("getting device info by imsi");
				deviceInfo = dao.getDeviceInfoByImsi(imsi);
			} else if (Boolean.TRUE.equals(Utils.checkForValueInString(deviceId))) {
				logger.info("geting device info by device id");
				deviceInfo = dao.getDeviceInfoByDeviceId(deviceId);
			}
		} catch (DaoException daoException) {
			logger.error("Error in getting device info detail by imsi : {} or device id {} DaoException : {}", imsi,
					deviceId, Utils.getStackTrace(daoException));
		} catch (Exception e) {
			logger.error("Error in getting device info detail by imsi : {} or device id {} Exception : {}", imsi,
					deviceId, Utils.getStackTrace(e));
		}
		return deviceInfo;
	}

	private OS checkDeviceOs(String deviceOs) {
		if (Boolean.TRUE.equals(Utils.checkForValueInString(deviceOs))) {
			if (deviceOs.equalsIgnoreCase(OS.ANDROID.name())) {
				logger.info("checking device os:{}", OS.ANDROID);
				return OS.ANDROID;
			} else if (deviceOs.equalsIgnoreCase(OS.IOS.name())) {
				logger.info("checking device os:{}", OS.IOS);
				return OS.IOS;
			}
		}
		return null;
	}

	@Override
	public Boolean getNVInstallationDetail(String imsi, String deviceId, String deviceOs) {
		logger.info("Going to get NV installation info by Imsi : {} , device id : {} and device os : {}", imsi,
				deviceId, deviceOs);
		Boolean installed = null;
		try {
			DeviceInfo deviceInfo = getDeviceInfoDetailByImsiOrDeviceId(imsi, deviceId);
			if (deviceOs.equalsIgnoreCase(DeviceConstant.DEVICE_OS_IOS) && Boolean.FALSE.equals(Utils.checkForValueInString(deviceId))) {
				installed = false;
			} else if (deviceInfo != null && StringUtils.isNotEmpty(deviceInfo.getFcmKey())) {
				logger.info("FCM KEY : {}", deviceInfo.getFcmKey());
				JSONArray keysArray = new JSONArray();
				keysArray.put(deviceInfo.getFcmKey());
				JSONObject dataPayload = new JSONObject();
				dataPayload.put(DeviceConstant.NV_FCM_KEY, keysArray);
				IFCMServerService ifcmServerService = ApplicationContextProvider.getApplicationContext()
						.getBean(IFCMServerService.class);

				installed = ifcmServerService.sendNotification(dataPayload, checkDeviceOs(deviceOs));
			}
		} catch (Exception e) {
			logger.error("Error in getting nv installation information : {}", Utils.getStackTrace(e));
		}
		logger.info("INSTALLED STATUS : {}", installed);
		return installed != null ? installed : Boolean.FALSE;
	}

	@Override
	public Object getSearchData(Map<String, Object> map) {

		if (map != null && !map.isEmpty()) {
			AdvanceSearch advanceSearch = (AdvanceSearch) map.get(InfraConstants.ADVANCESEARCH);
			if (advanceSearch != null && advanceSearch.getTypereference() != null) {
				return getProbeUserDetailsForAdvanceSearch(advanceSearch.getTypereference());
			} else {
				throw new RestException("Invalid Reference");
			}
		}else {
			throw new RestException("Invalid Parameter ");
		}
	
	}

	private Map<String,DeviceInfo> getProbeUserDetailsForAdvanceSearch(Integer deviceIdPk) {

		 Map<String,DeviceInfo> probUserDetail = new  HashMap<>();
		logger.info("Going to get device data for this id : {} ", deviceIdPk);
		try {
			DeviceInfo deviceInfoByPK = dao.findByPk(deviceIdPk);
			DeviceInfo deviceInfoForProbe = new DeviceInfo();
			if (deviceInfoByPK.getUserCustomName() != null && deviceInfoByPK.getDeviceId() != null) {
				deviceInfoForProbe.setUserCustomName(deviceInfoByPK.getUserCustomName());
				deviceInfoForProbe.setDeviceId(deviceInfoByPK.getDeviceId());
			}
			probUserDetail.put(DeviceConstant.PROBE, deviceInfoForProbe);
			return probUserDetail;

		} catch (DaoException e) {
			logger.error(EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtils.getMessage(e));
		}
	
	}


}

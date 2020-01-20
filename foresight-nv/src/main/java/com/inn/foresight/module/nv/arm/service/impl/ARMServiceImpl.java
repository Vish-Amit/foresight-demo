package com.inn.foresight.module.nv.arm.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.encoder.AESUtils;
import com.inn.commons.io.FileUtils;
import com.inn.commons.io.IOUtils;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.core.generic.exceptions.ValueNotFoundException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.app.constants.AppConstants;
import com.inn.foresight.module.nv.app.dao.IAPKDetailDao;
import com.inn.foresight.module.nv.app.dao.ILicenseMasterDao;
import com.inn.foresight.module.nv.app.model.APKDetail;
import com.inn.foresight.module.nv.app.model.LicenseMaster;
import com.inn.foresight.module.nv.arm.service.ARMService;
import com.inn.foresight.module.nv.arm.wrapper.ARMRequestWrapper;
import com.inn.foresight.module.nv.profile.dao.INVProfileDataDao;
import com.inn.foresight.module.nv.profile.model.NVProfileData;
import com.inn.foresight.module.nv.report.utils.ReportUtil;

@Service("ARMServiceImpl")
public class ARMServiceImpl  implements ARMService {
	
	/** The Constant ZIP_FILE_EXTENTION. */
	public static final String  APK_FILE_EXTENTION = ".apk";

	private Logger logger = LogManager.getLogger(ARMServiceImpl.class);

	@Autowired
	IAPKDetailDao apkDetailDao;

	@Autowired
	INVProfileDataDao profileDao;

	@Autowired
	ILicenseMasterDao iLicenseMasterDao;

	@Override
	public Object getAppData(ARMRequestWrapper wrapper, Integer lLimit, Integer uLimit)
			throws ValueNotFoundException {
		Map<String, Object> map = new HashMap<>();
		if (lLimit != null && uLimit != null && uLimit > lLimit) {
			List<APKDetail> apkDetails = apkDetailDao.getAPKDetails(wrapper, lLimit, uLimit);
			Set<String> apkIds = apkDetails.stream().filter(x -> x.getApkId() != null).map(APKDetail::getApkId)
					.collect(Collectors.toSet());
			List<NVProfileData> nvProfile = profileDao.findAll().stream()
					.filter(x -> x.isDeleted() && apkIds.contains(x.getModule())).collect(Collectors.toList());
			List<LicenseMaster> licenseMasterList = iLicenseMasterDao.findAll().stream()
					.filter(x -> apkIds.contains(x.getAppName())).collect(Collectors.toList());

			map.put(AppConstants.APK_KEY, apkDetails);
			map.put(AppConstants.LICENSE_KEY, licenseMasterList);
			map.put(AppConstants.PROFILE_KEY, nvProfile);
			return map;
		} else {
			return apkDetailDao.getAPKDetails(wrapper, lLimit, uLimit).size();
		}
	}
	
	@Override
	public String updateAppDetails(ARMRequestWrapper wrapper) throws IOException {
		try {
			if (wrapper.getApkDetail() != null) {
				APKDetail apkDetail = apkDetailDao.findByPk(wrapper.getApkDetail().getId());
				if (apkDetail != null) {
					apkDetailDao.update(wrapper.getApkDetail());
				} else if(wrapper.getApkDetail() != null){
					apkDetailDao.create(wrapper.getApkDetail());
				}
			}
			if (wrapper.getLicenseMaster() != null) {
				if (iLicenseMasterDao.findByPk(wrapper.getLicenseMaster().getId()) != null) {
					iLicenseMasterDao.update(wrapper.getLicenseMaster());
				} else if(wrapper.getLicenseMaster() != null){
					iLicenseMasterDao.create(wrapper.getLicenseMaster());
				}
			}
			if (wrapper.getNvProfile() != null) {
				if (profileDao.findByPk(wrapper.getNvProfile().getId()) != null) {
					profileDao.update(wrapper.getNvProfile());
				} else if(wrapper.getNvProfile() != null){
					profileDao.create(wrapper.getNvProfile());
				}
			}
			return ForesightConstants.SUCCESS;
		} catch (DaoException e) {
			logger.info("Error while updating record {}", Utils.getStackTrace(e));
		}
		return ForesightConstants.FAILURE;
	}

	private void modifyPreExistingPath(InputStream inputFile, String apkType, APKDetail apkDetail, String apkName,
			String uploadPath) throws IOException {
		File previousPath = new File(uploadPath);
		for (File entry : previousPath.listFiles()) {
			logger.info("Going to take backup of file {}", entry.getName());
			if (!entry.getName().contains("_bkup")) {
				boolean renameTo = entry.renameTo(new File(entry.getPath().concat("_bkup")));
				logger.info(renameTo ? "Backup Done" : "Backup Not Done");
			} else {
				logger.info("Already have backup of file");
			}
		}
		File file = new File(uploadPath.concat(apkName).concat(APK_FILE_EXTENTION));
		processFileCreation(inputFile, uploadPath, file, apkDetail, apkType);
	}

	private String getUploadPath(String apkType, String versionName, Integer versionCode) {
		
		String baseURL = ConfigUtils.getString(AppConstants.APK_CUSTOM_PATH);
		logger.info("Found apk base URL from config {}",baseURL);
		StringBuilder uploadURL = new StringBuilder(baseURL);
		uploadURL.append(apkType).
		append(ForesightConstants.FORWARD_SLASH).
		append(versionName).
		append(ForesightConstants.FORWARD_SLASH).
		append(versionCode).
		append(ForesightConstants.FORWARD_SLASH);
		logger.info("Going to return upload URL {}",uploadURL);
		return uploadURL.toString();
	}


	private void processFileCreation(InputStream inputFile, String path, File file, APKDetail apkDetail, String apkType) throws IOException {
		try {
		OutputStream outputStream = new FileOutputStream(file);
		IOUtils.copy(inputFile, outputStream);
		StringBuilder downloadURL = createDownloadURL(apkDetail,apkType);
		apkDetail.setDownloadURL(downloadURL.toString());
		apkDetail.setReleaseTime(new Date());
		logger.info("File Created Successfully at path {}", path);
		outputStream.close();
		} catch (FileNotFoundException e) {
			logger.info("File Not Found Exception {}", Utils.getStackTrace(e));
		} catch (IOException e) {
			logger.info("IO Exception {}", Utils.getStackTrace(e));
		} 
	}


	private StringBuilder createDownloadURL(APKDetail apkDetail, String apkType) {
		String baseURL = ConfigUtils.getString("APP_BASE_URL");
		logger.info("Base URL Found :::: {}", baseURL);
		StringBuilder downloadURL = new StringBuilder(baseURL);
		downloadURL.append(AppConstants.APK_DOWNLOAD_PATH)
		.append(apkType).append(ForesightConstants.FORWARD_SLASH)
		.append(apkDetail.getVersionName()).append(ForesightConstants.FORWARD_SLASH)
		.append(apkDetail.getVersionCode()).append(ForesightConstants.FORWARD_SLASH )
		.append(apkDetail.getApkName()).append(APK_FILE_EXTENTION);
		logger.info("APK Download URL {}",downloadURL);
		
		return downloadURL;
	}
	
	@Override
	public String uploadAPK(Integer id, InputStream inputFile, String appType) {
		logger.info("Going to find apk Detail by id {}",id);
		APKDetail apkDetail = apkDetailDao.findByPk(id);
		if (apkDetail != null) {
			String uploadPath = getUploadPath(appType, apkDetail.getVersionName(), apkDetail.getVersionCode());
			if (FileUtils.exists(uploadPath)) {
				try {
					modifyPreExistingPath(inputFile, appType, apkDetail, apkDetail.getApkName(), uploadPath);
				} catch (IOException e) {
					logger.info("Can't take backup of previous File at path {} because of {}", uploadPath,
							Utils.getStackTrace(e));
				}
			} else {
				try {
					logger.info("Inside Else Condition");
					ReportUtil.createDirectory(uploadPath);
					logger.info("Directory created Successfully at path {}",uploadPath);
				} catch (Exception e1) {
					logger.info("Error while creating directory at path {}",uploadPath);
				}
				File file = new File(uploadPath.concat(apkDetail.getApkName()).concat(APK_FILE_EXTENTION));
				try {
					processFileCreation(inputFile, uploadPath, file, apkDetail, appType);
				} catch (IOException e) {
					logger.info("Can't create apk entry {}", Utils.getStackTrace(e));
				}

			}

			apkDetailDao.update(apkDetail);

			return ForesightConstants.SUCCESS;
		}
		return ForesightConstants.NOT_EXISTS;
	}
}

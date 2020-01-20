package com.inn.foresight.module.nv.app.service.impl;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.exceptions.ValueNotFoundException;
import com.inn.foresight.module.nv.app.constants.AppConstants;
import com.inn.foresight.module.nv.app.dao.IAPKDetailDao;
import com.inn.foresight.module.nv.app.model.APKDetail;
import com.inn.foresight.module.nv.app.model.APKDetail.APP_OS;
import com.inn.foresight.module.nv.app.service.IAPKDetailService;
import com.inn.foresight.module.nv.app.utils.AppUtils;
import com.inn.product.security.utils.AuthenticationCommonUtil;

/** The Class APKDetailServiceImpl. */
@Service("APKDetailServiceImpl")
public class APKDetailServiceImpl extends AppUtils implements IAPKDetailService{
	
	/** The logger. */
	private Logger logger = LogManager.getLogger(APKDetailServiceImpl.class);
	
	/** Instance of APKDetailDaoImpl. */
	@Autowired
	private IAPKDetailDao dao;
	
	/**
	 * Method Used to get APKDetail from APK ID .
	 *
	 * @param apkId the apk id
	 * @param apkOS the apk OS
	 * @return the APK detail by id
	 * @throws RestException the rest exception
	 * @returns Encrypted APKDetail Object
	 */
	@Override
	public String getAPKDetailById(String apkId, String apkOS) {
		try {
			APKDetail apkDetail = dao.getAPKDetailById(AuthenticationCommonUtil.checkForValueDecryption(apkId),
					APP_OS.valueOf(AuthenticationCommonUtil.checkForValueDecryption(apkOS)));
			
			return AuthenticationCommonUtil.checkForValueEncryption(new Gson().toJson(apkDetail),null);
		} catch (ValueNotFoundException e) {
			logger.info(AppConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(DATA_NOT_FOUND);
		} catch (Exception e) {
			logger.info(AppConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(SOMETHING_WENT_WRONG);
		}
	}

	/**
	 * Getting Latest APKDetail record By apkId, apkOS and versionName.
	 *
	 * @param apkId the apk id
	 * @param appOS the app OS
	 * @param versionName the version name
	 * @return APKDetail Object
	 * @throws RestException the rest exception
	 */
	@Override
	public APKDetail getAPKDetailById(String apkId, APP_OS appOS, String versionName,Boolean releaseTimeCheck) {
		logger.info("Going to find latest ApkDetail if Available By apkId {}, appOS {}, versionName {}", apkId, appOS, versionName);
		try {
			return dao.getAPKDetailById(apkId, appOS, versionName,releaseTimeCheck);
		} catch (Exception e) {
			logger.info(AppConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}

}
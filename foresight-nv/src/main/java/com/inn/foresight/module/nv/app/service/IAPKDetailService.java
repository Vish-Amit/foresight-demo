package com.inn.foresight.module.nv.app.service;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.module.nv.app.model.APKDetail;
import com.inn.foresight.module.nv.app.model.APKDetail.APP_OS;

/** The Interface IAPKDetailService. */
public interface IAPKDetailService {

	/**
	 * Gets the APK detail by id.
	 *
	 * @param apkId the apk id
	 * @param apkOS the apk OS
	 * @return the APK detail by id
	 * @throws RestException the rest exception
	 */
	String getAPKDetailById(String apkId, String apkOS);
	
	/**
	 * Gets the APK detail by id.
	 *
	 * @param apkId the apk id
	 * @param appOS the app OS
	 * @param versionName the version name
	 * @return the APK detail by id
	 * @throws RestException the rest exception
	 */

	APKDetail getAPKDetailById(String apkId, APP_OS appOS, String versionName, Boolean releaseTimeCheck);

}

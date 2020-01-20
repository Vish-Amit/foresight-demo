package com.inn.foresight.module.nv.app.dao;

import java.util.List;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.exceptions.ValueNotFoundException;
import com.inn.foresight.module.nv.app.model.APKDetail;
import com.inn.foresight.module.nv.app.model.APKDetail.APP_OS;
import com.inn.foresight.module.nv.arm.wrapper.ARMRequestWrapper;

/** The Interface IAPKDetailDao. */
public interface IAPKDetailDao extends IGenericDao<Integer, APKDetail>{

	/**
	 * Gets the APK detail by id.
	 *
	 * @param apkId the apk id
	 * @param apkOS the apk OS
	 * @return the APK detail by id
	 * @throws RestException the rest exception
	 * @throws ValueNotFoundException the value not found exception
	 */
	APKDetail getAPKDetailById(String apkId, APP_OS apkOS) throws ValueNotFoundException;

	/**
	 * Gets the APK detail by id.
	 *
	 * @param apkId the apk id
	 * @param appOS the app OS
	 * @param versionName the version name
	 * @return the APK detail by id
	 * @throws ValueNotFoundException the value not found exception
	 * @throws RestException the rest exception
	 */
	APKDetail getAPKDetailById(String apkId, APP_OS appOS, String versionName,boolean releaseTimeCheck)
			throws ValueNotFoundException;

	/**
	 * @param wrapper
	 * @param uLimit
	 * @param lLimit
	 * @return
	 * @throws ValueNotFoundException
	 */
	List<APKDetail> getAPKDetails(ARMRequestWrapper wrapper,Integer uLimit,Integer lLimit)
			throws ValueNotFoundException;

}

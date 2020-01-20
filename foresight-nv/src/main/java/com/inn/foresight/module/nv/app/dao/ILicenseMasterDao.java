package com.inn.foresight.module.nv.app.dao;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.foresight.core.generic.exceptions.ValueNotFoundException;
import com.inn.foresight.module.nv.app.model.LicenseMaster;


/**
 * The Interface ILicenseMasterDao.
 *
 * @author innoeye
 * date - 16-Nov-2017 3:29:08 PM
 */
public interface ILicenseMasterDao extends IGenericDao<Integer, LicenseMaster> {
	
	/**
	 * Gets the license master.
	 *
	 * @param appName the app name
	 * @param clientId the client id
	 * @return the license master
	 * @throws ValueNotFoundException the value not found exception
	 */
	LicenseMaster getLicenseMaster(String appName, String clientName) throws ValueNotFoundException;

}

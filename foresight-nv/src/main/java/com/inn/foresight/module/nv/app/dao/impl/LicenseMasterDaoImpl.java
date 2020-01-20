package com.inn.foresight.module.nv.app.dao.impl;

import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.foresight.core.generic.exceptions.ValueNotFoundException;
import com.inn.foresight.module.nv.app.constants.AppConstants;
import com.inn.foresight.module.nv.app.dao.ILicenseMasterDao;
import com.inn.foresight.module.nv.app.model.LicenseMaster;


/**
 * The Class LicenseMasterDaoImpl.
 *
 * @author innoeye
 * date - 16-Nov-2017 3:31:12 PM
 */
@Repository("LicenseMasterDaoImpl")
public class LicenseMasterDaoImpl extends HibernateGenericDao<Integer,LicenseMaster> implements ILicenseMasterDao {

	/** The logger. */
	private Logger logger = LogManager.getLogger(LicenseMasterDaoImpl.class);
			
	/** Construct LicenseMasterDaoImpl instance. */
	public LicenseMasterDaoImpl() {
		super(LicenseMaster.class);
	}

	/**
	 * Getting LicenseMaster instance related to given appName & clientName.
	 *
	 * @param appName the app name
	 * @param clientName the client name
	 * @return LicenseMaster instance
	 * @throws ValueNotFoundException the value not found exception
	 */
	@Override
	public LicenseMaster getLicenseMaster(String appName, String clientName) throws ValueNotFoundException {
		try {
			Query query = getEntityManager().createNamedQuery(AppConstants.GET_LICENSEMASTER_BY_APPNAME_CLIENTID)
					.setParameter(AppConstants.APPNAME, appName)
					.setParameter(AppConstants.CLIENTNAME, clientName);
			return (LicenseMaster) query.getSingleResult();
		} catch (Exception e) {
			logger.info(AppConstants.NO_RECORD_FOUND_LOGGER);
			throw new ValueNotFoundException(e);
		}
	}

}

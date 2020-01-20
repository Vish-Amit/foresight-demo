package com.inn.foresight.module.nv.app.dao.impl;

import java.util.Collections;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.inn.commons.lang.NumberUtils;
import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.utils.Utils;
import com.inn.foresight.core.generic.exceptions.ValueNotFoundException;
import com.inn.foresight.module.nv.app.constants.AppConstants;
import com.inn.foresight.module.nv.app.dao.ILicenseInfoDao;
import com.inn.foresight.module.nv.app.model.LicenseInfo;
import com.inn.foresight.module.nv.app.model.LicenseMaster;
import com.inn.foresight.module.nv.app.wrapper.DeviceInfoWrapper;


/**
 * The Class LicenseInfoDaoImpl.
 *
 * @author innoeye
 * date - 16-Nov-2017 3:37:48 PM
 */
@Repository("LicenseInfoDaoImpl")
public class LicenseInfoDaoImpl extends HibernateGenericDao<Integer, LicenseInfo> implements ILicenseInfoDao {

	/** The logger. */
	private Logger logger = LogManager.getLogger(LicenseInfoDaoImpl.class);
			
	/** Construct LicenseInfoDaoImpl instance. */
	public LicenseInfoDaoImpl() {
		super(LicenseInfo.class);
	}

	/**
	 * Getting LicenceInfo instance related to given imei , apkId , clientName.
	 *
	 * @param imei the imei
	 * @param appName the app name
	 * @param clientName the client name
	 * @return LicenseInfo instance
	 * @throws ValueNotFoundException the value not found exception
	 */
	@Override
	public LicenseInfo getLicenseInfo(String imei,String appName,String clientName) throws ValueNotFoundException {
		logger.info("Going to getLicenseInfo for imei {},  appName {} , clientName {} ",imei,appName,clientName);
		try {
			Query query = getEntityManager().createNamedQuery(AppConstants.GET_LICENSEINFO_BY_IMEI_APPNAME_CLIENTID)
					.setParameter(AppConstants.IMEI, imei)
					.setParameter(AppConstants.APPNAME, appName)
					.setParameter(AppConstants.CLIENTNAME, clientName);
			return (LicenseInfo) query.getSingleResult();
		} catch (Exception e) {
			logger.error("Exception in  getLicenseInfo for imei {},  appName {} , clientName {}, errorMessage {}   ",imei,appName,clientName,ExceptionUtils.getMessage(e));
			throw new ValueNotFoundException(e);
		}
	}

	/**
	 * Getting count of issued license for given licenseMaster.
	 *
	 * @param licenseMaster the license master
	 * @return license count
	 */
	@Override
	public Long getLicenseInfoCountByLicenseMaster(LicenseMaster licenseMaster) {
		try {
			Query query = getEntityManager().createNamedQuery(AppConstants.GET_LICENSEINFO_COUNT_BY_LICENSEMASTER)
					.setParameter(AppConstants.LICENSEMASTER, licenseMaster);
			return (Long) query.getSingleResult();
		} catch (Exception e) {
			logger.info(AppConstants.NO_RECORD_FOUND_LOGGER);
			return NumberUtils.LONG_ZERO;
		}
	}

	/**
	 * Getting LicenceInfo Object related to given Licence Id.
	 *
	 * @param licenceId the licence id
	 * @return LicenseInfo Object
	 * @throws ValueNotFoundException the value not found exception
	 * @throws RestException the rest exception
	 * @throw ValueNotFoundException in case of NoResultException, RestException
	 */
	@Override
	public LicenseInfo getLicenseInfoFromID(String licenceId) throws ValueNotFoundException  {
		logger.info("Getting LicenceInfo by licenceId {}", licenceId);
		try {
			Query query = getEntityManager().createNamedQuery(AppConstants.GET_LICENSEINFO_FROM_ID)
					.setParameter(AppConstants.LICENCE_ID, licenceId);
			return (LicenseInfo) query.getSingleResult();
		} catch (NoResultException e) {
			logger.error(AppConstants.NO_RECORD_FOUND_LOGGER);
			throw new ValueNotFoundException(e);
		} catch (Exception e) {
			logger.error(AppConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DeviceInfoWrapper> getLicenceCountGroupByLicenceMaster() {
		try {
			Query query=getEntityManager()
					.createNamedQuery("getLicenceCountGroupByLicenceMaster");
			return query.getResultList();
		} catch (Exception e) {
			logger.error("Exception in getLicenceCountGroupByLicenceMaster {}",Utils.getStackTrace(e));
		}
		return Collections.emptyList();
	}

}

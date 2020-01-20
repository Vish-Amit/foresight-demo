package com.inn.foresight.module.nv.app.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.exceptions.ValueNotFoundException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.app.constants.AppConstants;
import com.inn.foresight.module.nv.app.dao.IAPKDetailDao;
import com.inn.foresight.module.nv.app.model.APKDetail;
import com.inn.foresight.module.nv.app.model.APKDetail.APP_OS;
import com.inn.foresight.module.nv.arm.wrapper.ARMRequestWrapper;

/** The Class APKDetailDaoImpl. */
@Repository("APKDetailDaoImpl")
public class APKDetailDaoImpl extends HibernateGenericDao<Integer, APKDetail> implements IAPKDetailDao {
	
	/** The logger. */
	private Logger logger = LogManager.getLogger(APKDetailDaoImpl.class);
	
	/** Instantiates a new APK detail dao impl. */
	public APKDetailDaoImpl() {
		super(APKDetail.class);
	}
	
	/**
	 * Gets the APK detail based on apkName and Apk OS.
	 *
	 * @param apkId the apk id
	 * @param apkOS the apk OS
	 * @return the APK detail by id
	 * @throws RestException the rest exception
	 * @throws ValueNotFoundException the value not found exception
	 */
	@Override
	public APKDetail getAPKDetailById(String apkId, APP_OS apkOS) throws ValueNotFoundException {
		logger.info("Finding apk detail by apkId {}, apkOS {} ", apkId, apkOS);
		try {
			Query query = getEntityManager().createNamedQuery(AppConstants.GET_APKDETAIL_BY_NAME)
					.setParameter(AppConstants.APK_ID_STR, apkId)
					.setParameter(AppConstants.APP_OS_STR, apkOS);
		
			return (APKDetail) query.getResultList().get(0);
		
		}catch(IndexOutOfBoundsException e){
			logger.info(AppConstants.NO_RECORD_FOUND_LOGGER);
			throw new ValueNotFoundException(e);
		}catch (Exception e) {
			logger.error(AppConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}

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
	@Override
	public APKDetail getAPKDetailById(String apkId, APP_OS appOS, String versionName,boolean releaseTimeCheck) throws ValueNotFoundException {
		logger.info("Finding latest ApkDetail if Available By apkId {}, appOS {}, versionName {}", apkId, appOS, versionName);
		APKDetail apkDetail = null;
		try {
			if(releaseTimeCheck) {
				enableFilter(AppConstants.APP_RELEASE_TIME_FILTER);
			}
			Query query = getEntityManager().createNamedQuery(AppConstants.GET_APKDETAIL_BY_ID)
					.setParameter(AppConstants.APK_ID_STR, apkId)
					.setParameter(AppConstants.APP_OS_STR, appOS)
					.setParameter(AppConstants.VERSION_NAME, versionName);
			
			apkDetail = (APKDetail) query.getResultList().get(0);
			if(releaseTimeCheck) {
				disableFilter(AppConstants.APP_RELEASE_TIME_FILTER);
			}
		} catch (IndexOutOfBoundsException e) {
			logger.info(AppConstants.NO_RECORD_FOUND_LOGGER);
			throw new ValueNotFoundException(e);
		} catch (Exception e) {
			logger.info(AppConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
		return apkDetail;
	}
	
	@Override
	public List<APKDetail> getAPKDetails(ARMRequestWrapper wrapper,Integer lLimit,Integer uLimit) throws ValueNotFoundException {
	
		try {
			APKDetail apkDetail = wrapper.getApkDetail();
			logger.info("Finding ApkDetail if Available By apkId {}, appOS {}, versionName {} and apkName {}", apkDetail.getApkId(), wrapper.getAppOS(),
					apkDetail.getVersionName(),apkDetail.getApkName());
			
			List<APP_OS> convertAppOSToEnum = convertAppOSToEnum(wrapper.getAppOS());
			Query query = getEntityManager().createNamedQuery("getAPKDetails").setParameter(AppConstants.APP_OS_STR,
					convertAppOSToEnum);
			if (wrapper.getApplyReleaseCheck().booleanValue()) {
				enableFilter(AppConstants.APP_RELEASE_TIME_FILTER);
			}
			String enableAPKIdFilter = enableAPKIdFilter(apkDetail.getApkId());
			String enableVersionNameFilter = enableVersionNameFilter(apkDetail.getVersionName());
			String enableVersionCodeFilter = enableVersionCodeFilter(apkDetail.getVersionCode());
			String enableAPKNameFilter = enableAPKNameFilter(apkDetail.getApkName());

			if (lLimit != null && uLimit != null && uLimit > lLimit) {
				query.setFirstResult(lLimit).setMaxResults(uLimit - lLimit + 1);
				List<APKDetail> response = query.getResultList();
				logger.info("Found the result size {}",response.size());
				return response;
			}

			List<APKDetail> response = query.getResultList();
			if (wrapper.getApplyReleaseCheck().booleanValue()) {
				disableFilter(AppConstants.APP_RELEASE_TIME_FILTER);
			}
			disableFilter(enableAPKIdFilter);
			disableFilter(enableVersionNameFilter);
			disableFilter(enableVersionCodeFilter);
			disableFilter(enableAPKNameFilter);
			return response;

		} catch (IndexOutOfBoundsException e) {
			logger.info(AppConstants.NO_RECORD_FOUND_LOGGER);
			throw new ValueNotFoundException(e);
		} catch (Exception e) {
			logger.info(AppConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}
	
	private String enableAPKNameFilter(String apkName) {
		if (Utils.hasValidValue(apkName)) {
			Session s = (Session) getEntityManager().getDelegate();
			Filter filter = s.enableFilter(AppConstants.APK_NAME_FILTER);
			filter.setParameter(AppConstants.APK_NAME,
					ForesightConstants.MODULUS + apkName + ForesightConstants.MODULUS);
		return AppConstants.APK_NAME_FILTER;
		}
		return null;
	}

	/**
	 * @param appOS
	 * @return
	 */
	private List<APP_OS> convertAppOSToEnum(List<String> appOS) {
		List<APP_OS> list = new ArrayList<>();
		for (String s : appOS) {
			list.add(APP_OS.valueOf(s));
		}
		return list;
	}

	/**
	 * @param apkId
	 * @return
	 */
	private String enableAPKIdFilter(String apkId) {
		if (Utils.hasValidValue(apkId)) {
			Session s = (Session) getEntityManager().getDelegate();
			Filter filter = s.enableFilter(AppConstants.APK_ID_FILTER);
			filter.setParameter(AppConstants.APK_ID_STR,
					ForesightConstants.MODULUS + apkId + ForesightConstants.MODULUS);
		return AppConstants.APK_ID_FILTER;
		}
		return null;
	}
	
	/**
	 * @param versionName
	 * @return
	 */
	private String enableVersionNameFilter(String versionName) {
		if (Utils.hasValidValue(versionName)) {
			Session s = (Session) getEntityManager().getDelegate();
			Filter filter = s.enableFilter(AppConstants.APK_VERSION_NAME_FILTER);
			filter.setParameter(AppConstants.VERSION_NAME,
					ForesightConstants.MODULUS + versionName + ForesightConstants.MODULUS);
			return AppConstants.APK_VERSION_NAME_FILTER;
		}
		return null;
	}
	
	/**
	 * @param versionCode
	 * @return
	 */
	private String enableVersionCodeFilter(Integer versionCode) {
		if (versionCode != null) {
			Session s = (Session) getEntityManager().getDelegate();
			Filter filter = s.enableFilter(AppConstants.APK_CODE_FILTER);
			filter.setParameter(AppConstants.VERSION_CODE,
					versionCode);
			return AppConstants.APK_CODE_FILTER;
		}
		return null;
	}

	/**
	 * Enable filter.
	 *
	 * @param filterName the filter name
	 * @param filterParam the filter param
	 * @param filterValue the filter value
	 */
	private void enableFilter(String filterName) {
		Session s = (Session) getEntityManager().getDelegate();
		s.enableFilter(filterName);
	}
	
	/**
	 * Disable filter.
	 *
	 * @param filterName the filter name
	 */
	private void disableFilter(String filterName) {
		if (Utils.hasValidValue(filterName)) {
			Session s = (Session) getEntityManager().getDelegate();
			s.disableFilter(filterName);
		}
	}
}

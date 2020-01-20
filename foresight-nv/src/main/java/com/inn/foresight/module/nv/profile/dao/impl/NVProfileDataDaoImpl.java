package com.inn.foresight.module.nv.profile.dao.impl;

import java.util.Collections;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.module.nv.profile.constants.NVProfileConstants;
import com.inn.foresight.module.nv.profile.dao.INVProfileDataDao;
import com.inn.foresight.module.nv.profile.model.NVProfileData;
import com.inn.foresight.module.nv.profile.wrapper.NVProfileDataWrapper;


/**
 * The Class NVProfileDataDaoImpl.
 *
 * @author innoeye
 * date - 26-Feb-2018 2:22:44 PM
 */
@Repository("NVProfileDataDaoImpl")
public class NVProfileDataDaoImpl extends HibernateGenericDao<Integer, NVProfileData> implements INVProfileDataDao {
	
	/** The logger. */
	private Logger logger = LogManager.getLogger(NVProfileDataDaoImpl.class);
	
	/**
	 * Construct NVProfileDataDaoImpl object.
	 *
	 * @param type the type
	 */
	public NVProfileDataDaoImpl() {
		super(NVProfileData.class);
	}
	
	/**
	 * Gets the all profiles.
	 *
	 * @return the all profiles
	 * @throws DaoException the dao exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<NVProfileDataWrapper> getAllProfiles(){
		logger.info("Inside getAllProfiles");
		try {
			Query query = getEntityManager().createNamedQuery(NVProfileConstants.GET_ALL_PROFILE);
			List<NVProfileDataWrapper> profiles = query.getResultList();
			logger.info("Done getAllProfiles");
			return profiles;
		} catch (Exception e) {
			logger.error("Error in getAllProfiles : {} " ,ExceptionUtils.getStackTrace(e));
			throw new DaoException(
					e.getMessage());
		}
	}
	
	/**
	 * Gets the profile by type.
	 *
	 * @param type the type
	 * @return the profile by type
	 * @throws DaoException the dao exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<NVProfileData> getProfileByTypeAndModule(String type,String module) {
		logger.info("Inside getProfileByTypeAndModule type {} : , module {} :",type,module);
		try {
			Query query = getEntityManager().createNamedQuery(NVProfileConstants.GET_PROFILE_BY_TYPE_AND_MODULE)
					.setParameter(NVProfileConstants.TYPE, type)
					.setParameter(NVProfileConstants.MODULE, module);
			List<NVProfileData> nvProfiles =  query.getResultList();
			logger.info("Done getProfileByTypeAndModule");
			return nvProfiles;
		} catch (Exception e) {
			logger.error("Error in getProfileByTypeAndModule : {} " ,ExceptionUtils.getStackTrace(e));
			throw new DaoException(
					e.getMessage());
		}
	}
	
	/**
	 * Gets the all profiles.
	 *
	 * @return the all Vsalid profiles
	 * @throws DaoException the dao exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<NVProfileData> getAllValidProfiles() {
		logger.info("Inside getAllValidProfiles");
		try {
			Query query = getEntityManager().createNamedQuery(NVProfileConstants.GET_ALL_VALID_PROFILES);
			List<NVProfileData> profiles = query.getResultList();
			logger.info("Done getAllValidProfiles size : {}",profiles.size());
			return profiles;
		} catch (Exception e) {
			logger.error("Error in getAllValidProfiles : {} " ,ExceptionUtils.getStackTrace(e));
			throw new DaoException(
					e.getMessage());
		}
	}
	
	/**
	 * Gets the NV profile by profile id.
	 *
	 * @param profileId the profile id
	 * @return the NV profile by profile id
	 * @throws DaoException the dao exception
	 */
	@Override
	public NVProfileData getNVProfileByProfileId(String profileId) {
		logger.info("Inside getNVProfileByProfileId id:{}",profileId);
		try {
			Query query = getEntityManager().createNamedQuery(NVProfileConstants.GET_PROFILE_DATA_BY_PROFILE_ID);
			query.setParameter(NVProfileConstants.PROFILE_ID_PLACEHOLDER, profileId);
			NVProfileData nvProfileData = (NVProfileData) query.getSingleResult();
			logger.info("Done getNVProfileByProfileId");
			return nvProfileData;
		} catch (Exception e) {
			logger.error("Error in getNVProfileByProfileId : {} " ,ExceptionUtils.getStackTrace(e));
			throw new DaoException(
					e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<NVProfileData> getProfileByType(String type) {
		logger.info("Inside getProfileByType type {} :",type);
		try {
			Query query = getEntityManager().createNamedQuery("getProfileByType")
					.setParameter(NVProfileConstants.TYPE, type);
			List<NVProfileData> nvProfiles =  query.getResultList();
			logger.info("Done getProfileByType");
			return nvProfiles;
		} catch (Exception e) {
			logger.error("Error in getProfileByType : {} " ,ExceptionUtils.getStackTrace(e));
			
		}
		return Collections.emptyList();
	}
}

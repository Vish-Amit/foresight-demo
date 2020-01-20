package com.inn.foresight.module.nv.profile.dao;

import java.util.List;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.module.nv.profile.model.NVProfileData;
import com.inn.foresight.module.nv.profile.wrapper.NVProfileDataWrapper;

/**
 * The Interface INVProfileDataDao.
 *
 * @author innoeye
 * date - 26-Feb-2018 2:21:59 PM
 */
public interface INVProfileDataDao extends IGenericDao<Integer,NVProfileData> {
	
	/**
	 * Gets the all profiles.
	 *
	 * @return the all profiles
	 * @throws DaoException the dao exception
	 */
	List<NVProfileDataWrapper> getAllProfiles();
	
	
	/**
	 * Gets the profile by type.
	 *
	 * @param type the type
	 * @param module the module
	 * @return the profile by type
	 * @throws DaoException the dao exception
	 */
	List<NVProfileData> getProfileByTypeAndModule(String type,String module);



	/**
	 * Gets the all Valid profiles.
	 *
	 * @return the all Valid profiles
	 * @throws DaoException the dao exception
	 */
	List<NVProfileData> getAllValidProfiles();


	/**
	 * Gets the NV profile by profile id.
	 *
	 * @param profileId the profile id
	 * @return the NV profile by profile id
	 * @throws DaoException the dao exception
	 */
	NVProfileData getNVProfileByProfileId(String profileId);
	
	List<NVProfileData>getProfileByType(String type);

}

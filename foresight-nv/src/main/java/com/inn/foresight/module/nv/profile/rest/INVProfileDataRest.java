package com.inn.foresight.module.nv.profile.rest;

import javax.ws.rs.core.Response;

/**
 * The Interface INVProfileDataRest.
 *
 * @author innoeye
 * date - 26-Feb-2018 2:23:30 PM
 */
 public interface INVProfileDataRest {
	
	/**
	 * Gets the all profiles.
	 *
	 * @return the all profiles
	 */
	 Response getAllProfiles();
	
	/**
	 * Gets the all profile templates.
	 *
	 * @return the all profile templates
	 */
	 Response getAllProfileTemplates(String module);
	
	/**
	 * Save profile.
	 *
	 * @param profileData the profile data
	 * @return the response
	 */
	 Response saveProfile(String profileDataJson);
	
	/**
	 * Publish profile.
	 *
	 * @param profileId the profile data id
	 * @return the response
	 */
	 Response publishProfile(Integer profileId ,Boolean status);
	
	/**
	 * Gets the all device make.
	 *
	 * @return the all device make
	 */
	 Response getAllDeviceMake();

	/**
	 * Gets the all device model.
	 *
	 * @param make the make
	 * @return the all device model
	 */
	 Response getAllDeviceModel(String make);
	
	/**
	 * Gets the all device OS.
	 *
	 * @return the all device OS
	 */
	 Response getAllDeviceOS();

	/**
	 * Gets the all operator.
	 *
	 * @return the all operator
	 */
	 Response getAllOperator();
	
	/**
	 * Gets the all technology.
	 *
	 * @return the all technology
	 */
	 Response getAllTechnology();
	
	/**
	 * Find profile data.
	 *
	 * @param id the id
	 * @return the NV profile data wrapper
	 */
	 Response findProfileData(Integer id);
	
	/**
	 * Delete profile.
	 *
	 * @param id the id
	 * @return the response
	 */
	 Response deleteProfile(Integer id);


	/**
	 * Gets NVProfileData Object from Db by Type of Profile.
	 *
	 * @param type of profileData
	 * @return the response
	 */
	 Response getProfile(String type,String module);

}

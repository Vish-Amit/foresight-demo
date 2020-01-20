package com.inn.foresight.module.nv.profile.service;

import java.util.List;
import java.util.Set;

import org.springframework.security.access.prepost.PreAuthorize;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.module.nv.profile.model.NVProfileData;
import com.inn.foresight.module.nv.profile.wrapper.NVProfileDataWrapper;
import com.inn.foresight.module.nv.profile.wrapper.NVProfileTemplate;
import com.inn.product.um.user.model.User;

/**
 * The Interface INVProfileDataService.
 *
 * @author innoeye
 * date - 26-Feb-2018 2:24:16 PM
 */
public interface INVProfileDataService extends IGenericService<Integer, NVProfileData> {
	
	/**
	 * Gets the all profiles.
	 *
	 * @return the all profiles
	 * @throws RestException the rest exception
	 */
	@PreAuthorize("hasRole('ROLE_NV_PROFILE_viewProfile')") List<NVProfileDataWrapper> getAllProfiles();
	
	/**
	 * Gets the all profile templates.
	 *
	 * @param module the module
	 * @return the all profile templates
	 * @throws RestException the rest exception
	 */
	@PreAuthorize("hasRole('ROLE_NV_PROFILE_editProfile')") List<NVProfileTemplate> getAllProfileTemplates(String module);
	
	/**
	 * Save profile.
	 *
	 * @param profileData the profile data
	 * @param creator the creator
	 * @return the NV profile data
	 * @throws RestException the rest exception
	 */
	@PreAuthorize("hasRole('ROLE_NV_PROFILE_createProfile')") NVProfileData saveProfile(NVProfileData profileData,User creator);
	
	/**
	 * Publish profile.
	 *
	 * @param profileId the profile data id
	 * @param status the status
	 * @return the NV profile data
	 * @throws RestException the rest exception
	 */
	 @PreAuthorize("hasRole('ROLE_NV_PROFILE_editProfile')") void publishProfile(Integer profileId ,Boolean status);
	
	/**
	 * Gets the device profile.
	 *
	 * @param requestWrapper the request wrapper
	 * @return the device profile
	 * @throws RestException the rest exception
	 * @PreAuthorize("hasRole('ROLE_NV_PROFILE_viewProfile')")
	 */
	String getDeviceProfile(String requestWrapper);
	
	/**
	 * Gets the all device make.
	 *
	 * @return the all device make
	 * @throws RestException the rest exception
	 */
	List<String> getAllDeviceMake();

	/**
	 * Gets the all device model.
	 *
	 * @param make the make
	 * @return the all device model
	 * @throws RestException the rest exception
	 */
	List<String> getAllDeviceModel(String make);
	
	/**
	 * Gets the all device OS.
	 *
	 * @return the all device OS
	 * @throws RestException the rest exception
	 */
	List<String> getAllDeviceOS();

	/**
	 * Gets the all operator.
	 *
	 * @return the all operator
	 * @throws RestException the rest exception
	 */
	Set<String> getAllOperator();
	
	/**
	 * Gets the all technology.
	 *
	 * @return the all technology
	 * @throws RestException the rest exception
	 */
	List<String> getAllTechnology();
	
	/**
	 * Find profile data.
	 *
	 * @param id the id
	 * @return the NV profile data wrapper
	 * @throws RestException the rest exception
	 */
	@PreAuthorize("hasRole('ROLE_NV_PROFILE_viewProfile')") NVProfileDataWrapper findProfileData(Integer id);

	/**
	 * Gets the profile by type.
	 *
	 * @param type the type
	 * @param module the module
	 * @return the profile by type
	 * @throws RestException the rest exception
	 */
	@PreAuthorize("hasRole('ROLE_NV_PROFILE_viewProfile')") List<NVProfileData> getProfileByTypeAndModule(String type,String module);
	
	/** Update profile map. */
	void updateProfileMap();
	
	
	/**
	 * Delete profile.
	 *
	 * @param id the id
	 * @throws RestException the rest exception
	 */
	@PreAuthorize("hasRole('ROLE_NV_PROFILE_deleteProfile')") void deleteProfile(Integer id);
	
	/**
	 * Gets the NV profile by profile id.
	 *
	 * @param profileId the profile id
	 * @return the NV profile by profile id
	 * @throws RestException the rest exception
	 */
	NVProfileData getNVProfileByProfileId(String profileId);

}

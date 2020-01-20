package com.inn.foresight.module.nv.profile.rest.impl;

import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inn.commons.lang.StringUtils;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.profile.model.NVProfileData;
import com.inn.foresight.module.nv.profile.rest.INVProfileDataRest;
import com.inn.foresight.module.nv.profile.service.INVProfileDataService;
import com.inn.foresight.module.nv.profile.utils.NVProfileDataUtils;
import com.inn.foresight.module.nv.profile.wrapper.NVProfileDataWrapper;
import com.inn.foresight.module.nv.profile.wrapper.NVProfileTemplate;
import com.inn.product.um.user.model.User;
import com.inn.product.um.user.service.impl.UserContextServiceImpl;


/**
 * The Class NVProfileDataRestImpl.
 *
 * @author innoeye
 * date - 26-Feb-2018 2:23:52 PM
 */
@Path("/profile")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Service("NVProfileDataRestImpl")
public class NVProfileDataRestImpl extends NVProfileDataUtils implements INVProfileDataRest {
	
	/** The logger. */
	private Logger logger = LogManager.getLogger(NVProfileDataRestImpl.class);
	
	/** The profile data service. */
	@Autowired
	private INVProfileDataService profileDataService;

	/**
	 * Gets the all profiles.
	 *
	 * @return the all profiles
	 */
	@GET
	@Override
	@Path("getAllProfiles")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllProfiles() {
		logger.info("Inside getAllProfiles");
		try {
			List<NVProfileDataWrapper> profileDatas = profileDataService.getAllProfiles();
			logger.info("Done getAllProfiles");
			return Response.ok(profileDatas).build();
		} catch (Exception e) {
			logger.error("Exception in getAllProfiles : {} ", ExceptionUtils.getStackTrace(e));
			return Response.ok(FAILURE_JSON).build();
		}
	}
	
	/**
	 * Gets the all profile templates.
	 *
	 * @return the all profile templates
	 */
	@GET
	@Override
	@Path("getAllProfileTemplates/{"+MODULE+"}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllProfileTemplates(@PathParam(MODULE) String module) {
		logger.info("Inside getAllProfileTemplates");
		try {
			List<NVProfileTemplate> templates = profileDataService.getAllProfileTemplates(module);
			logger.info("Done getAllProfileTemplates");
			return Response.ok(templates).build();
		} catch (Exception e) {
			logger.error("Exception in getAllProfileTemplates : {} ", ExceptionUtils.getStackTrace(e));
			return Response.ok(FAILURE_JSON).build();
		}
	}

	/**
	 * Save profile.
	 *
	 * @param profileData the profile data
	 * @return the response
	 */
	@POST
	@Override
	@Path("saveProfile")
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveProfile(String profileDataJson) {
		logger.info("Inside saveProfile");
		try {
			if (StringUtils.isNotBlank(profileDataJson) && StringUtils.isNotEmpty(profileDataJson)) {
				NVProfileData profileData = new ObjectMapper().readValue(profileDataJson, NVProfileData.class);
				User creator = UserContextServiceImpl.getUserInContext();
				NVProfileData nvProfileData = profileDataService.saveProfile(profileData, creator);
				logger.info("Done saveProfile");
				return Response.ok(nvProfileData).build();
			} else {
				return Response.ok(INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error("Exception in saveProfile : {} ", ExceptionUtils.getStackTrace(e));
			return Response.ok(MESSAGE_START+e.getMessage()+MESSAGE_END).build();
		}
	}
	
	/**
	 * Publish profile.
	 *
	 * @param profileData the profile data
	 * @return the response
	 */
	@GET
	@Override
	@Path("publishProfile/{profileId}/{status}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response publishProfile(@PathParam(PROFILE_ID) Integer profileId ,@PathParam(STATUS) Boolean status){
		logger.info("Inside publishProfile");
		try {
			if (profileId != null) {
				profileDataService.publishProfile(profileId,status);
				logger.info("Done publishProfile");
				return Response.ok(SUCCESS_JSON).build();
			} else {
				return Response.ok(INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error("Exception in publishProfile : {} ", ExceptionUtils.getStackTrace(e));
			return Response.ok(FAILURE_JSON).build();
		}
	}
	
	/**
	 * Gets the all device make.
	 *
	 * @return the all device make
	 */
	@GET
	@Override
	@Path("getAllDeviceMake")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllDeviceMake() {
		logger.info("Inside getAllDeviceMake");
		try {
			List<String> makes = profileDataService.getAllDeviceMake();
			logger.info("Done getAllDeviceMake");
			return Response.ok(makes).build();
		} catch (Exception e) {
			logger.error("Exception in getAllDeviceMake : {} ", ExceptionUtils.getStackTrace(e));
			return Response.ok(FAILURE_JSON).build();
		}
	}
	
	/**
	 * Gets the all device model.
	 *
	 * @return the all device model
	 */
	@GET
	@Override
	@Path("getAllDeviceModel")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllDeviceModel(@QueryParam(MAKE) String make) {
		logger.info("Inside getAllDeviceModel");
		try {
			if (StringUtils.isNotBlank(make) && StringUtils.isNotEmpty(make)) {
				List<String> models = profileDataService.getAllDeviceModel(make);
				logger.info("Done getAllDeviceModel");
				return Response.ok(models).build();
			} else {
				return Response.ok(INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error("Exception in getAllDeviceModel : {} ", ExceptionUtils.getStackTrace(e));
			return Response.ok(FAILURE_JSON).build();
		}
	}
	
	/**
	 * Gets the all device OS.
	 *
	 * @return the all device OS
	 */
	@GET
	@Override
	@Path("getAllDeviceOS")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllDeviceOS() {
		logger.info("Inside getAllDeviceOS");
		try {
			List<String> os = profileDataService.getAllDeviceOS();
			logger.info("Done getAllDeviceOS");
			return Response.ok(os).build();
		} catch (Exception e) {
			logger.error("Exception in getAllDeviceOS : {} ", ExceptionUtils.getStackTrace(e));
			return Response.ok(FAILURE_JSON).build();
		}
	}
	
	/**
	 * Gets the all operator.
	 *
	 * @return the all operator
	 */
	@GET
	@Override
	@Path("getAllOperator")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllOperator() {
		logger.info("Inside getAllOperator");
		try {
			Set<String> operators = profileDataService.getAllOperator();
			logger.info("Done getAllOperator");
			return Response.ok(operators).build();
		} catch (Exception e) {
			logger.error("Exception in getAllOperator : {} ", ExceptionUtils.getStackTrace(e));
			return Response.ok(FAILURE_JSON).build();
		}
	}
	
	/**
	 * Gets the all technology.
	 *
	 * @return the all technology
	 */
	@GET
	@Override
	@Path("getAllTechnology")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllTechnology() {
		logger.info("Inside getAllTechnology");
		try {
			List<String> technologies = profileDataService.getAllTechnology();
			logger.info("Done getAllTechnology");
			return Response.ok(technologies).build();
		} catch (Exception e) {
			logger.error("Exception in getAllTechnology : {} ", ExceptionUtils.getStackTrace(e));
			return Response.ok(FAILURE_JSON).build();
		}
	}
	
	/**
	 * Find profie data.
	 *
	 * @param id the id
	 * @return the NV profile data wrapper
	 */
	@GET
	@Override
	@Path("findProfileData/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findProfileData(@PathParam(ID) Integer id) {
		logger.info("Inside findProfieData");
		try {
			if (id != null) {
				NVProfileDataWrapper profileData = profileDataService.findProfileData(id);
				logger.info("Done findProfieData");
				return Response.ok(profileData).build();
			} else {
				return Response.ok(INVALID_PARAMETER_JSON).build();
			}
		} catch (

		Exception e) {
			logger.error("Exception in findProfieData : {} ", ExceptionUtils.getStackTrace(e));
			return Response.ok(FAILURE_JSON).build();
		}
	}
	
	/**
	 * Delete profile.
	 *
	 * @param id the id
	 * @return the response
	 */
	@GET
	@Override
	@Path("deleteProfile/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteProfile(@PathParam(ID) Integer id) {
		logger.info("Inside deleteProfile");
		try {
			profileDataService.deleteProfile(id);
			logger.info("Done deleteProfile");
			return Response.ok(SUCCESS_JSON).build();
		} catch (Exception e) {
			logger.error("Exception in deleteProfile : {} ", ExceptionUtils.getStackTrace(e));
			return Response.ok(FAILURE_JSON).build();
		}
	}
	
	/**
	 * Gets NVProfileData Object from Db by Type of Profile.
	 *
	 * @param type of profileData
	 * @return the response
	 */
	@GET
	@Override
	@Path("getProfile/{type}/{module}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProfile(@PathParam(TYPE) String type,@PathParam(MODULE) String module) {
		logger.info("Inside getProfileByType");
		try {
			if (StringUtils.isNotBlank(type)) {
				return Response.ok(profileDataService.getProfileByTypeAndModule(type,module)).build();
			} else {
				throw new RestException(ForesightConstants.INVALID_PARAMETER);
			}
		} catch (Exception e) {
			logger.error("Exception in getProfileByType : {} ", ExceptionUtils.getStackTrace(e));
			return Response.ok(FAILURE_JSON).build();
		}
	}
	
	
}
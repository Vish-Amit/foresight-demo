package com.inn.foresight.module.nv.app.rest.impl;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.commons.lang.StringUtils;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.app.constants.AppConstants;
import com.inn.foresight.module.nv.app.service.IDeviceInfoService;
import com.inn.foresight.module.nv.app.service.ILicenseInfoService;
import com.inn.foresight.module.nv.app.utils.AppUtils;
import com.inn.foresight.module.nv.app.wrapper.DeviceInfoWrapper;
import com.inn.foresight.module.nv.profile.service.INVProfileDataService;
import com.inn.product.security.utils.AuthenticationCommonUtil;


/** The Class DeviceInfoRestImpl. */
@Path("/DeviceInfo")
@Service("DeviceInfoRestImpl")
public class DeviceInfoRestImpl extends AppUtils {
	
	/** The Logger*. */
	private Logger logger = LogManager.getLogger(DeviceInfoRestImpl.class);

	/** Instance of DeviceInfoServiceImpl. */
	
	@Autowired
	private IDeviceInfoService service;
	
	/** Instance of LicenseInfoServiceImpl. */
	@Autowired
	private ILicenseInfoService licenseInfoService;
	
	/** The profile data service. */
	@Autowired
	private INVProfileDataService profileDataService;
	
	
	/**
	 * Device registration.
	 *
	 * @param apkDeviceJSON the apk device JSON
	 * @param userId the user id
	 * @return the response
	 */
	@POST
	@Path("deviceRegistration")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.TEXT_PLAIN)
	public Response deviceRegistration(String apkDeviceJSON,@QueryParam(UID)String userId) {
		logger.info("Going to register device");
		try {
			if (StringUtils.isNotBlank(apkDeviceJSON) && StringUtils.isNotEmpty(apkDeviceJSON)) {
				return service.deviceValidationAndRegistrationService(apkDeviceJSON,userId);
			} else {
				logger.error(INVALID_APP_DETAILS);
				return Response.ok(AuthenticationCommonUtil.checkForValueEncryption(new DeviceInfoWrapper(FAILURE,INVALID_APP_DETAILS),null)).build();
			}
		} catch (Exception e) {
			logger.error(AppConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			return Response.ok(AuthenticationCommonUtil.checkForValueEncryption(new DeviceInfoWrapper(FAILURE,e.getMessage()),null)).build();
		}
	}
	
	/**
	 * This method is used to check Licence validity .
	 *
	 * @param licenceId : unique id related to Licence issued
	 * @return true if Licence is valid, false otherwise or error message if anything unexpected occurs
	 */
	@POST
	@Path("checkLicenseValidity")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.TEXT_PLAIN)
	public Response checkLicenseValidityById(String licenceId) {
		logger.info("Cheking For Licence Validation by licenceId {}", licenceId);
		try {
			if(licenceId != null) {
				return Response.ok(licenseInfoService.checkLicenseValidityById(licenceId)).build();
			}else {
				return Response.ok(ForesightConstants.INVALID_PARAMETERS).build();
			}
		} catch (RestException e) {
			logger.error(AppConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			return Response.ok(AuthenticationCommonUtil.checkForValueEncryption(e.getMessage(),null)).build();
		}
	}
	
	/**
	 * Gets the device profile.
	 *
	 * @param deviceId the device id
	 * @return the device profile
	 */
	@POST
	@Path("getDeviceProfile")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDeviceProfile(String requestWrapper) {
		logger.info("Inside getDeviceProfile");
		try {
			if(StringUtils.isNotBlank(requestWrapper) && StringUtils.isNotEmpty(requestWrapper)) {
			String profiles =  profileDataService.getDeviceProfile(requestWrapper);
			logger.info("Done getDeviceProfile");
				return Response.ok(profiles).build();
			} else {
				return Response.ok(AuthenticationCommonUtil.checkForValueEncryption(INVALID_PARAMETER_JSON,null)).build();
			}
		} catch (Exception e) {
			logger.error("Exception in getAllDeviceMake : {} ", ExceptionUtils.getStackTrace(e));
			return Response.ok(AuthenticationCommonUtil.checkForValueEncryption(FAILURE_JSON,null)).build();
		}
	}
	
	/** Gets the Nv installation detail. */
	@GET
	@Path("getNVInstallationDetail")
	@Produces("application/json")
	@Consumes("application/json")
	public Boolean getNVInstallationDetail(@QueryParam("imsi") String imsi, @QueryParam("deviceId") String deviceId, @QueryParam("deviceOs") String deviceOs) {
		logger.info("Inside getNVInstallationDetail");
		if (Utils.checkForValueInString(deviceOs).booleanValue()) {
			return service.getNVInstallationDetail(imsi,deviceId,deviceOs);
		} else {
			throw new RestException(ForesightConstants.INVALID_PARAMETER);
		}
	}

}

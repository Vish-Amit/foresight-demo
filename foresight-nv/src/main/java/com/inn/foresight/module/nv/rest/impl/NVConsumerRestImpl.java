package com.inn.foresight.module.nv.rest.impl;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.inn.commons.Validate;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.lang.StringUtils;
import com.inn.commons.maps.LatLng;
import com.inn.commons.maps.nns.NNS;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.service.IOperatorDetailService;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.service.IBuildingDataService;
import com.inn.foresight.core.maplayer.service.IGenericMapService;
import com.inn.foresight.module.nv.NVConfigUtil;
import com.inn.foresight.module.nv.NVConstant;
import com.inn.foresight.module.nv.app.constants.AppConstants;
import com.inn.foresight.module.nv.app.service.IAPKDetailService;
import com.inn.foresight.module.nv.app.service.IDeviceInfoService;
import com.inn.foresight.module.nv.app.service.ILicenseInfoService;
import com.inn.foresight.module.nv.app.utils.AppUtils;
import com.inn.foresight.module.nv.device.service.DeviceDashboardService;
import com.inn.foresight.module.nv.device.service.INVDeviceDataService;
import com.inn.foresight.module.nv.device.service.INVDeviceGroupService;
import com.inn.foresight.module.nv.feedback.model.ConsumerFeedback;
import com.inn.foresight.module.nv.feedback.service.IConsumerFeedbackService;
import com.inn.foresight.module.nv.layer.service.LayerPlotService;
import com.inn.foresight.module.nv.layer.wrapper.BoundaryDataWrapper;
import com.inn.foresight.module.nv.layer3.constants.NVLayer3Constants;
import com.inn.foresight.module.nv.layer3.service.INVLayer3DashboardService;
import com.inn.foresight.module.nv.layer3.utils.NVLayer3Utils;
import com.inn.foresight.module.nv.nps.model.NPSRawDetail;
import com.inn.foresight.module.nv.nps.service.INetPromoterService;
import com.inn.foresight.module.nv.profile.service.INVProfileDataService;
import com.inn.foresight.module.nv.profile.utils.NVUtilService;
import com.inn.foresight.module.nv.pushnotification.constants.PushNotificationConstants;
import com.inn.foresight.module.nv.pushnotification.service.IPushNotificationService;
import com.inn.foresight.module.nv.report.wrapper.SiteInformationWrapper;
import com.inn.foresight.module.nv.service.ISiteDetailService;
import com.inn.foresight.module.nv.sitesuggestion.service.ISiteSuggestionService;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;
import com.inn.foresight.module.nv.workorder.service.INVWorkorderService;
import com.inn.foresight.module.nv.workorder.stealth.constants.StealthConstants;
import com.inn.foresight.module.nv.workorder.stealth.service.IStealthTaskDetailService;
import com.inn.foresight.module.nv.workorder.stealth.wrapper.StealthWOParameters;
import com.inn.foresight.module.nv.workorder.utils.NVWorkorderUtils;
import com.inn.product.security.utils.AuthenticationCommonUtil;
import com.inn.product.um.user.service.IAppAuthService;

/**
 * The Class NVConsumerRestImpl.
 */
@Path("/NVConsumer")
@Service("NVConsumerRestImpl")
public class NVConsumerRestImpl extends AppUtils {

    /**
     * The logger.
     */
    Logger logger = LogManager.getLogger(NVConsumerRestImpl.class);

    /**
     * Instance of AppAuthServiceImpl.
     */
    @Autowired
    private IAppAuthService appAuthService;

    /**
     * Instance of APKDetailServiceImpl.
     */
    @Autowired
    private IAPKDetailService apkDetailService;

    /**
     * Instance of APKDetailServiceImpl.
     */
    @Autowired
    private INVWorkorderService nvWorkorderService;

    /**
     * Instance of DeviceInfoServiceImpl.
     */
    @Autowired
    private IDeviceInfoService service;

    /**
     * Instance of LicenseInfoServiceImpl.
     */
    @Autowired
    private ILicenseInfoService licenseInfoService;

    /**
     * The profile data service.
     */
    @Autowired
    private INVProfileDataService profileDataService;

    /**
     * The consumer feedback service.
     */
    @Autowired
    private IConsumerFeedbackService consumerFeedbackService;

    /**
     * The device group service.
     */
    @Autowired
    private INVDeviceGroupService deviceGroupService;

    /**
     * The stealth task detail service.
     */
    @Autowired
    private IStealthTaskDetailService stealthTaskDetailService;

    /**
     * The device data service.
     */
    @Autowired
    private INVDeviceDataService deviceDataService;

    /**
     * The i push notification service.
     */
    @Autowired
    private IPushNotificationService iPushNotificationService;

    /**
     * The Net Promoter service.
     */
    @Autowired
    private INetPromoterService iNetPromoterService;

    /**
     * The notification enable.
     */
    private static Boolean notificationEnable = false;

    @Autowired
    private IBuildingDataService iBuildingDataService;

    @Autowired
    private ISiteDetailService siteDetailService;

    @Autowired
    private INVLayer3DashboardService iNVLayer3DashboardService;

    @Autowired
    private NVUtilService nVUtilService;

    @Autowired
    private IGenericMapService genericMapService;

    @Autowired
    IOperatorDetailService iOpeatorService;

    @Autowired
    ISiteDetailService siteDataService;

    @Autowired
    DeviceDashboardService deviceDashboardService;

    @Autowired
    LayerPlotService layerPlotService;

	@Autowired(required = false)
	private ISiteSuggestionService siteSuggestionService;

    
    public static Boolean getNotificationEnable() {
        return notificationEnable;
    }

    public static void setNotificationEnable(Boolean notificationEnable) {
        NVConsumerRestImpl.notificationEnable = notificationEnable;
    }

    /**
     * Method to Check the User Name and Password,validate it and returns the user
     * related information or authentication failure results.
     *
     * @param username the username
     * @param password the password
     * @param request  the request
     * @return the string
     * @parameter password
     * @parameter username
     * @returns User Id
     */
    @POST
    @Path(AppConstants.MOBILE_AUTH)
    public String checkAuthentication(@FormParam(AppConstants.USERNAME) String username,
                                      @FormParam(AppConstants.QUERY_PARAM_PW) String password, @FormParam("parameter") String parameter, @Context HttpServletRequest request, @Context HttpServletResponse response) {


        Boolean isGCMAlgo = null;


        logger.info(" mobile request headers  getRemoteAddr {}" , request.getRemoteAddr());
        logger.info(" mobile request headers  getRemoteHost {}" , request.getRemoteHost());

        String ip = request.getHeader(ForesightConstants.X_FORWORDED_FOR);
        String verificationCode = request.getParameter("vcode");
        logger.info(" ip  url FOR X_FORWORDED_FOR IP : {} and VCODE : {}", ip, verificationCode);
        if (ip == null || ip.equalsIgnoreCase(ForesightConstants.BLANK_STRING)) {
            ip = request.getRemoteAddr();
            logger.info(" ip  url FOR ACCESSURL IP  getRemoteAddr {}" , ip);
        }
        if ((username != null && ! username.equals(AppConstants.EMPTY_STRING))
                && (password != null && ! password.equals(AppConstants.EMPTY_STRING))) {
            logger.info(AppConstants.USERNAME_CHECKSUM_LOGGER, username);

            try {
                isGCMAlgo = AuthenticationCommonUtil.isGCMAlgorithm(username);
                Cookie cookie = new Cookie(ConfigUtils.getString(AppConstants.CSRF_TOKEN_MOBILE), UUID.randomUUID().toString());
                cookie.setPath(ConfigUtils.getString(AppConstants.CSRF_TOKEN_MOBILE_COOKIE_PATH));
                cookie.setMaxAge(ConfigUtils.getInteger(AppConstants.CSRF_TOKEN_MOBILE_COOKIE_MAX_AGE));
                if (ConfigUtils.getBoolean(AppConstants.CSRF_TOKEN_MOBILE_COOKIE_SECURE).booleanValue())
                    cookie.setSecure(true);
                cookie.setHttpOnly(true);
                response.addCookie(cookie);
                return appAuthService.checkAuthentication(parameter, username, password, ip, verificationCode);
            } catch (Exception e) {
                logger.error(AppConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
                return AuthenticationCommonUtil.checkForValueEncryption(e.getMessage(), isGCMAlgo);
            }
        }
        return AuthenticationCommonUtil.checkForValueEncryption(AppConstants.RESULT_USERNAME_OR_PW_IS_EMPTY_OR_NULL, isGCMAlgo);
    }
    /**
     * Method Used to get APKDetail from APK ID .
     *
     * @param apkId the apk id
     * @param apkOS the apk OS
     * @return the apk detail by module name
     * @returns Encrypted APKDetail Object
     */
    @GET
    @Path(AppConstants.APK_DETAILS_BY_NAME)
    public String getApkDetailByModuleName(@QueryParam(AppConstants.APK_ID_STR) String apkId,
                                           @QueryParam(AppConstants.APP_OS_STR) String apkOS) {
        logger.info("Going to get APK details based in apkId {}, apkOS {} ", apkId, apkOS);
        try {
            if (apkId != null && apkOS != null) {
                return apkDetailService.getAPKDetailById(apkId, apkOS);
            } else {
                return AuthenticationCommonUtil.checkForValueEncryption(ForesightConstants.INVALID_PARAMETERS, null);
            }
        } catch (RestException e) {
            return AuthenticationCommonUtil.checkForValueEncryption(e.getMessage(), null);
        }
    }

    /**
     * Gets All Workorder Created For Provided User or workorder Id.
     *
     * @param encryptedId the encrypted id
     * @return NVWorkorderWrapper
     */
    @POST
    @Path("getWorkorderDetailsByWOIdFromMobile")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public Response getWorkorderDetailsByWOIdFromMobile(String encryptedId) {
        logger.info("Going to get Recipe and Workorder from Encrypted Work OrderId {}", encryptedId);
        try {
            if (Utils.hasValue(encryptedId)) {
                String decryptedId = AuthenticationCommonUtil.checkForValueDecryption(encryptedId);
                if (NVWorkorderUtils.isParsebleInteger(decryptedId)) {
                    return Response.ok(AuthenticationCommonUtil.checkForValueEncryption(
                            nvWorkorderService.getWorkorderDetailsByWOIdFromMobile(Integer.parseInt(decryptedId)), null))
                            .build();
                }
            }
            return Response.ok(AuthenticationCommonUtil.checkForValueEncryption(NVWorkorderConstant.INVALID_PARAMETER_JSON, null)).build();
        } catch (Exception e) {
            logger.info("Getting Exception {}", ExceptionUtils.getStackTrace(e));
            return Response.ok(AuthenticationCommonUtil.checkForValueEncryption(NVWorkorderConstant.FAILURE_JSON, null)).build();
        }
    }

    /**
     * Fetch stealth work order.
     *
     * @param encryptedStealthWOParameters the encrypted stealth WO parameters
     * @return the response
     * @throws Exception the exception
     */
    @POST
    @Path("fetchStealthWorkOrder")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public Response fetchStealthWorkOrder(String encryptedStealthWOParameters) {
        logger.info("Going to get Recipe and Workorder, encryptedStealthWOParameters:  {} ",
                encryptedStealthWOParameters);
        try {
            if (encryptedStealthWOParameters != null && !encryptedStealthWOParameters.isEmpty()) {
                Boolean isGcmAlgorithm = AuthenticationCommonUtil.isGCMAlgorithm(encryptedStealthWOParameters);
                List<StealthWOParameters> stealthWOParameters = decryptWOTaskJsonList(encryptedStealthWOParameters);
                return Response
                        .ok(AuthenticationCommonUtil.checkForValueEncryption(nvWorkorderService.fetchStealthWorkOrder(stealthWOParameters), isGcmAlgorithm))
                        .build();
            }
        } catch (Exception e) {
            logger.info("Getting Exception {}", ExceptionUtils.getStackTrace(e));
            throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
        }
        return null;
    }

    /**
     * Decrypt WO task json list.
     *
     * @param encryptedWOTaskJsonList the encrypted WO task json list
     * @return the list
     * @throws Exception the exception
     */
    private List<StealthWOParameters> decryptWOTaskJsonList(String encryptedWOTaskJsonList) {
        try {
            if (encryptedWOTaskJsonList != null) {
                String woTaskJsonList = AuthenticationCommonUtil.checkForValueDecryption(encryptedWOTaskJsonList);
                logger.info("decryptedWOTaskJsonList: {}", woTaskJsonList);
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(woTaskJsonList, new TypeReference<List<StealthWOParameters>>() {
                });
            } else {
                throw new RestException(ForesightConstants.INVALID_PARAMETERS_JSON);
            }
        } catch (Exception e) {
            logger.error("Error while decrypting stealth task json to Wrapper: {}", Utils.getStackTrace(e));
            throw new RestException(e);
        }
    }

    /**
     * Device registration.
     *
     * @param apkDeviceJSON the apk device JSON
     * @param userId        the user id
     * @return the response
     * @throws Exception
     */
    @POST
    @Path("deviceRegistration")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public Response deviceRegistration(String apkDeviceJSON, @QueryParam(UID) String userId) {
        logger.info("Going to register device");
        return service.deviceValidationAndRegistrationService(apkDeviceJSON, userId);

    }

    /**
     * This method is used to check Licence validity .
     *
     * @param licenceId : unique id related to Licence issued
     * @return true if Licence is valid, false otherwise or error message if
     * anything unexpected occurs
     */
    @POST
    @Path("checkLicenseValidity")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public Response checkLicenseValidityById(String licenceId) {
        logger.info("Cheking For Licence Validation by licenceId {}", licenceId);
        try {
            if (licenceId != null) {
                return Response.ok(licenseInfoService.checkLicenseValidityById(licenceId)).build();
            } else {
                return Response.ok(ForesightConstants.INVALID_PARAMETERS).build();
            }
        } catch (RestException e) {
            logger.error(AppConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
            return Response.ok(AuthenticationCommonUtil.checkForValueEncryption(e.getMessage(), null)).build();
        }
    }

    /**
     * Gets the device profile.
     *
     * @param requestWrapper the request wrapper
     * @return the device profile
     */
    @POST
    @Path("getDeviceProfile")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDeviceProfile(String requestWrapper) {
        logger.info("Inside getDeviceProfile");
        try {
            if (StringUtils.isNotBlank(requestWrapper) && StringUtils.isNotEmpty(requestWrapper)) {
                String profiles = profileDataService.getDeviceProfile(requestWrapper);
                logger.info("Done getDeviceProfile");
                return Response.ok(profiles).build();
            } else {
                return Response.ok(AuthenticationCommonUtil.checkForValueEncryption(INVALID_PARAMETER_JSON, null)).build();
            }
        } catch (Exception e) {
            logger.error("Exception in getAllDeviceMake : {} ", ExceptionUtils.getStackTrace(e));
            return Response.ok(AuthenticationCommonUtil.checkForValueEncryption(FAILURE_JSON, null)).build();
        }
    }

    /**
     * Creates the feedback.
     *
     * @param encryptedString the encrypted string
     * @return plain text response
     * @throws RestException the rest exception
     */
    @POST
    @Path("createFeedback")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String createFeedback(String encryptedString) {
        if (encryptedString != null) {
            try {
                ConsumerFeedback feedback = consumerFeedbackService.createFeedback(encryptedString);
                if (feedback != null) {
                    return ForesightConstants.SUCCESS_JSON;
                } else {
                    return ForesightConstants.FAILURE_JSON;
                }
            } catch (RestException e) {
                logger.error("error in createFeedback, error msg = {}", ExceptionUtils.getStackTrace(e));
                return ExceptionUtils.getMessage(e);
            }
        }
        return ForesightConstants.EXCEPTION_INVALID_PARAMS;
    }

    /**
     * Update stealth task details.
     *
     * @param encryptedTaskJson the encrypted task json
     * @return the response
     */
    @POST
    @Path("updateStealthTaskDetails")
    public Response updateStealthTaskDetails(String encryptedTaskJson) {
        try {
            logger.info("Going to update task details in database");
            return Response.ok(stealthTaskDetailService.updateStealthTaskDetails(encryptedTaskJson)).build();
        } catch (Exception e) {
            logger.error("Exception in updateStealthTaskDetails : {} ", ExceptionUtils.getStackTrace(e));
            return Response.ok(FAILURE_JSON).build();
        }
    }

    /**
     * Fetch stealth WO task id.
     *
     * @param encryptedDeviceId the encrypted device id
     * @param encryptedWoId     the encrypted wo id
     * @return the response
     */
    @GET
    @Path("fetchStealthWOTaskId")
    public Response fetchStealthWOTaskId(@QueryParam(DEVICE_ID) String encryptedDeviceId,
                                         @QueryParam(StealthConstants.WORKORDER_ID) String encryptedWoId) {
        try {
            logger.info("Going to fetchStealthWOTaskId");
            if (encryptedDeviceId == null || encryptedWoId == null) {
                logger.info("Invalid parameters: deviceId {}, woId {}", encryptedDeviceId, encryptedWoId);
                return Response.ok(INVALID_PARAMETER_JSON).build();
            } else {
                return Response.ok(stealthTaskDetailService.fetchStealthWOTaskId(encryptedDeviceId, encryptedWoId))
                        .build();
            }
        } catch (Exception e) {
            logger.error("Exception in fetchStealthWOTaskId : {} ", ExceptionUtils.getStackTrace(e));
            return Response.ok(FAILURE_JSON).build();
        }
    }

    /**
     * Update acknowledgement.
     *
     * @param encryptedTaskJson the encrypted task json
     * @return the response
     */
    @POST
    @Path("updateAcknowledgement")
    public Response updateAcknowledgement(String encryptedTaskJson) {
        try {
            logger.info("Going to updateAcknowledgement, encryptedTaskJson: {}", encryptedTaskJson);
            if (encryptedTaskJson == null) {
                logger.info("Invalid parameters: encryptedTaskJson: {}", encryptedTaskJson);
                return Response.ok(INVALID_PARAMETER_JSON).build();
            } else {
                return Response.ok(stealthTaskDetailService.updateAcknowledgement(encryptedTaskJson)).build();
            }
        } catch (Exception e) {
            logger.error("Exception in updateAcknowledgement : {} ", ExceptionUtils.getStackTrace(e));
            return Response.ok(FAILURE_JSON).build();
        }
    }

    /**
     * Gets the all device groups.
     *
     * @param latitude  the latitude
     * @param longitude the longitude
     * @return the all device groups
     */
    @GET
    @Path("getAllDeviceGroups")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllDeviceGroups(@QueryParam("latitude") String latitude,
                                       @QueryParam("longitude") String longitude) {
        logger.info("Inside getAllDeviceGroups lat : {} long : {}", latitude, longitude);
        try {
            if (Utils.hasValidValue(latitude) && Utils.hasValidValue(longitude) ) {
                String deviceGroups = deviceGroupService.getAllDeviceGroups(latitude, longitude);
                logger.info("Done getAllDeviceGroups");
                return Response.ok(deviceGroups).build();
            } else {
                return Response.ok(INVALID_PARAMETER_JSON).build();
            }
        } catch (Exception e) {
            logger.error("Exception in getAllDeviceGroups : {} ", ExceptionUtils.getStackTrace(e));
            return Response.ok(FAILURE_JSON).build();
        }
    }

    /**
     * Adds the device.
     *
     * @param id      the id
     * @param devices the devices
     * @return the response
     */
    @POST
    @Path("addDevice")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addDevice(@QueryParam("id") String id, String devices) {
        logger.info("Inside addDevice id :{} devices : {}", id, devices);
        try {
            if (Utils.hasValidValue(id) && Utils.hasValidValue(devices)) {
                String response = deviceGroupService.addDevice(id, devices, Boolean.TRUE);
                logger.info("Done addDevice");
                return Response.ok(response).build();
            } else {
                return Response.ok(INVALID_PARAMETER_JSON).build();
            }
        } catch (Exception e) {
            logger.error("Exception in addDevice : {} ", ExceptionUtils.getStackTrace(e));
            return Response.ok(MESSAGE_START + e.getMessage() + MESSAGE_END).build();
        }
    }

    /**
     * Removes the device.
     *
     * @param id      the id
     * @param devices the devices
     * @return the response
     */
    @POST
    @Path("removeDevice")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeDevice(@QueryParam("id") String id, String devices) {
        logger.info("Inside removeDevice id :{} devices : {}", id, devices);
        try {
            if (Utils.hasValidValue(id) && Utils.hasValidValue(devices)) {
                String response = deviceGroupService.removeDevice(id, devices, Boolean.TRUE);
                logger.info("Done removeDevice");
                return Response.ok(response).build();
            } else {
                return Response.ok(INVALID_PARAMETER_JSON).build();
            }
        } catch (Exception e) {
            logger.error("Exception in removeDevice : {} ", ExceptionUtils.getStackTrace(e));
            return Response.ok(MESSAGE_START + e.getMessage() + MESSAGE_END).build();
        }
    }

    /**
     * Gets the device group.
     *
     * @param id the id
     * @return the device group
     */
    @GET
    @Path("getDeviceGroup")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDeviceGroup(@QueryParam("id") String id) {
        logger.info("Inside getDeviceGroup id : {}", id);
        try {
            if (id != null) {
                String deviceGroupWrapper = deviceGroupService.getDeviceGroup(id, Boolean.TRUE);
                logger.info("Done getDeviceGroup");
                return Response.ok(deviceGroupWrapper).build();
            } else {
                return Response.ok(INVALID_PARAMETER_JSON).build();
            }
        } catch (Exception e) {
            logger.error("Exception in getDeviceGroup : {} ", ExceptionUtils.getStackTrace(e));
            return Response.ok(FAILURE_JSON).build();
        }
    }

    /**
     * Update device data.
     *
     * @param requestWrapper the request wrapper
     * @return the response
     */
    @POST
    @Path("updateDeviceData")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateDeviceData(String requestWrapper) {
        logger.info("Inside updateDeviceData");
        try {
            if (StringUtils.isNotBlank(requestWrapper) && StringUtils.isNotEmpty(requestWrapper)) {
                String deviceData = deviceDataService.updateDeviceData(requestWrapper);
                logger.info("Done updateDeviceData");
                return Response.ok(deviceData).build();
            } else {
                return Response.ok(AuthenticationCommonUtil.checkForValueEncryption(INVALID_PARAMETER_JSON, null)).build();
            }
        } catch (Exception e) {
            logger.error("Exception in updateDeviceData : {} ", ExceptionUtils.getStackTrace(e));
            return Response.ok(AuthenticationCommonUtil.checkForValueEncryption(FAILURE_JSON, null)).build();
        }
    }

    /**
     * Update notification for acknowledgement.
     *
     * @param encrptedNotificationId the encrpted notification id
     * @return the response
     * @throws RestException the rest exception
     */
    @POST
    @Path("updateNotificationForAcknowledgement")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateNotificationForAcknowledgement(String encrptedNotificationId) {
        Response responseToReturn = null;
        if (encrptedNotificationId != null) {
            responseToReturn = Response
                    .ok(iPushNotificationService.updateNotificationForAcknowledgement(encrptedNotificationId)).build();
        } else {
            responseToReturn = Response.ok(PushNotificationConstants.INVALID_PARAMETER_JSON).build();
        }
        return responseToReturn;
    }

    /**
     * Sync net promoter scores.
     *
     * @param npsJson the nps json
     * @return plain text response
     * @throws RestException the rest exception
     */
    @POST
    @Path("syncNPSData")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String syncNetPromoterScores(String npsJson) {

        if (Utils.hasValidValue(npsJson)) {
            try {
                NPSRawDetail wrapper = iNetPromoterService.persistNPSData(npsJson);
                if (wrapper == null) {
                    return ForesightConstants.FAILURE_JSON;
                } else {
                    return ForesightConstants.SUCCESS_JSON;
                }
            } catch (RestException e) {
                logger.error("error in sync Net Promoter Raw Data, error msg = {}", ExceptionUtils.getStackTrace(e));
                return ExceptionUtils.getMessage(e);
            }
        }
        return ForesightConstants.EXCEPTION_INVALID_PARAMS;
    }

    /**
     * Update NV data.
     *
     * @param encryptedString the encrypted string
     * @return plain text response
     * @throws RestException the rest exception
     */
    @POST
    @Path("updateNVData")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public Response updateNVData(String encryptedString) {

        if (Utils.hasValidValue(encryptedString)) {
            return Response.ok(deviceDataService.updateNVDeviceData(encryptedString)).build();
        }
        return Response.ok(ForesightConstants.EXCEPTION_INVALID_PARAMS).build();
    }

    /**
     * Check WO status.
     *
     * @param encryptedTaskJson the encrypted task json
     * @return the response
     */
    @POST
    @Path("checkWOStatus")
    public Response checkWOStatus(String encryptedTaskJson) {
        try {
            logger.info("Going to checkWOStatus, encryptedTaskJson: {}", encryptedTaskJson);
            if (encryptedTaskJson == null) {
                logger.info("Invalid parameters: encryptedTaskJson: {}", encryptedTaskJson);
                return Response.ok(INVALID_PARAMETER_JSON).build();
            } else {
                return Response.ok(stealthTaskDetailService.checkWOStatus(encryptedTaskJson)).build();
            }
        } catch (Exception e) {
            logger.error("Exception in updateAcknowledgement : {} ", ExceptionUtils.getStackTrace(e));
            return Response.ok(FAILURE_JSON).build();
        }
    }

    /**
     * Check notification enability.
     *
     * @return the response
     */
    @GET
    @Path("checkNotificationEnability")
    public Response checkNotificationEnability() {
        try {
            logger.info("catalina base {} ", System.getProperty("catalina.base"));
            logger.info("catalina home {}", System.getProperty("catalina.home"));
        } catch (Exception e) {
            logger.info("Exception when getting tomcat path :{} ", ExceptionUtils.getStackTrace(e));
        }
        return Response.ok(notificationEnable).build();
    }

    /**
     * Update notification enability.
     *
     * @return the response
     */
    @GET
    @Path("updateNotificationEnability")
    public Response updateNotificationEnability() {
        notificationEnable = !notificationEnable;
        return Response.ok(notificationEnable).build();
    }

    /**
     * Update stealth task detail list.
     *
     * @param encryptedTaskJsonList the encrypted task json list
     * @return the response
     */
    @POST
    @Path("updateStealthTaskDetailList")
    public Response updateStealthTaskDetailList(String encryptedTaskJsonList) {
        try {
            logger.info("Going to update task detail list in database");
            return Response.ok(stealthTaskDetailService.updateStealthTaskDetailList(encryptedTaskJsonList)).build();
        } catch (Exception e) {
            logger.error("Exception in updateStealthTaskDetailList : {} ", ExceptionUtils.getStackTrace(e));
            return Response.ok(FAILURE_JSON).build();
        }
    }

    @GET
    @Path("tagBuildingGeographyL4")
    public Response tagBuildingGeographyL4() {
        try {
            return Response.ok(iBuildingDataService.tagBuildingGeographyL4()).build();
        } catch (Exception e) {
            return Response.ok(ExceptionUtils.getStackTrace(e)).build();
        }

    }

    @SuppressWarnings("unchecked")
    @POST
    @Path("getCellLocation")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public Response getCellLocation(String encryptedTaskJson) {
        logger.info("inside method getCellLocation()");
        try {
            if (Utils.hasValidValue(encryptedTaskJson)) {
                String decryptedString = AuthenticationCommonUtil.checkForValueDecryption(encryptedTaskJson);
                HashMap<String, Integer> map = new ObjectMapper().readValue(decryptedString, HashMap.class);
                if (map.get(NVConstant.CGI_KEY) != null && map.get(NVConstant.PCI_KEY) != null) {
                    return Response
                            .ok(AuthenticationCommonUtil.checkForValueEncryption(siteDetailService
                                    .getLocationByCGIAndPci(map.get(NVConstant.CGI_KEY), map.get(NVConstant.PCI_KEY)), null))
                            .build();
                }
            }
        } catch (Exception e) {
            return Response.ok(AuthenticationCommonUtil.checkForValueEncryption(ForesightConstants.FAILURE_JSON, null)).build();
        }

        return Response.ok(AuthenticationCommonUtil.checkForValueEncryption(ForesightConstants.FAILURE_JSON, null)).build();

    }

    @POST
    @Path("/isUserTFAuthorised")
    public String isUserTFAuthorised(@FormParam(AppConstants.USERNAME) String username) {
        if (Utils.hasValidValue(username)) {
            return appAuthService.isUserTFAuthorised(username);
        }
        return AuthenticationCommonUtil.checkForValueEncryption(AppConstants.RESULT_USERNAME_IS_EMPTY_OR_NULL, null);

    }

    @POST
    @Path("checkPCIWiseNNSMapSizeForMacroSite")
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkPCIWiseNNSMapSizeForMacroSite() {

        try {
            logger.info("Going to check pci wise NNS map size");
            return iNVLayer3DashboardService.checkPCIWiseNNSMapSizeForMacroSite();
        } catch (Exception e) {
            logger.error("Error while clear pci wise NNS map: {}", e.getMessage());
            return Response.ok(FAILURE_JSON).build();
        }
    }

    @POST
    @Path("updatePCIWiseNNSMapForMacroSite")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePCIWiseNNSMapForMacroSite() {
        try {
            Map<Integer, NNS<LatLng>> pciWiseNNSMap = iNVLayer3DashboardService.getPCIWiseNNSMapForRanSite();
            logger.info("pci wise NNS map updated successfully!!! updated map size is: {}", pciWiseNNSMap.size());
            return Response.ok(SUCCESS_JSON).build();
        } catch (Exception e) {
            logger.error("Error while updating pci wise NNS map: {}", e.getMessage());
            return Response.ok(FAILURE_JSON).build();
        }
    }

    @POST
    @Path("clearPCIWiseNNSMapForMacroSite")
    @Produces(MediaType.APPLICATION_JSON)
    public Response clearPCIWiseNNSMapForMacroSite() {
        try {
            Response response = iNVLayer3DashboardService.clearPCIWiseNNSMapForMacroSite();
            logger.info("pci wise NNS map cleared successfully!!!");
            return response;
        } catch (Exception e) {
            logger.error("Error while clear pci wise NNS map: {}", e.getMessage());
            return Response.ok(FAILURE_JSON).build();
        }
    }

    @POST
    @Path("clearAllProfileAndDeviceMap")
    @Produces(MediaType.APPLICATION_JSON)
    public Response clearAllProfileAndDeviceMap() {
        try {
            nVUtilService.clearAllMap();
            logger.info("Profile & Device Reg map cleared successfully!!!");
            return Response.ok(SUCCESS_JSON).build();
        } catch (Exception e) {
            logger.error("Error while Profile & Device Reg map: {}", e.getMessage());
            return Response.ok(FAILURE_JSON).build();
        }
    }

    @POST
    @Path("updateAllProfileAndDeviceMap")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateAllProfileAndDeviceMap() {
        try {
            nVUtilService.init();
            logger.info("Profile & Device Reg map updated successfully!!!");
            return Response.ok(SUCCESS_JSON).build();
        } catch (Exception e) {
            logger.error("Error while updating Profile & Device Reg map: {}", e.getMessage());
            return Response.ok(FAILURE_JSON).build();
        }
    }

    @POST
    @Path("getDataForCoverageLayer")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public Response getDataForCoverageLayer(String columnListJson,
                                            @QueryParam("zoomLevel") String zoom, @QueryParam("NELat") String nELat, @QueryParam("NELng") String nELng,
                                            @QueryParam("SWLat") String sWLat, @QueryParam("SWLng") String sWLng, @QueryParam("postFix") String postFix) {
        try {
            logger.info("Getting parameter getDataForCoverageLayer {}, {} ,[{}] ,{} , {} ,{}  ,{}",
                    zoom, nELat, nELng, sWLat, sWLng, columnListJson, postFix);
            List<String> columnList = new Gson().fromJson(AuthenticationCommonUtil.checkForValueDecryption(columnListJson), new TypeToken<List<String>>() {
            }.getType());
            int decryZoom = Integer.parseInt(zoom);
            double decryNELat = Double.parseDouble(nELat);
            double decryNELng = Double.parseDouble(nELng);
            double decrySWLat = Double.parseDouble(sWLat);
            double decrySWLng = Double.parseDouble(sWLng);

            String coverageTable = ConfigUtils.getString(NVConfigUtil.NV_COVERAGE_TABLE);
            logger.info("table name for coverage module {}", coverageTable);

            if (postFix == null) {
                postFix = "RAK";
            }
            String dataFromHbase = genericMapService.getDataFromHbase(columnList, coverageTable, decryZoom, decryNELat, decryNELng, decrySWLat,
                    decrySWLng, null, null, postFix, null, null);
            return Response.ok(AuthenticationCommonUtil.checkForValueEncryption(dataFromHbase, null)).build();

        } catch (Exception e) {
            logger.info("Exception in getDataForCoverageLayer method {}", ExceptionUtils.getStackTrace(e));
            return Response.ok(AuthenticationCommonUtil.checkForValueEncryption(FAILURE_JSON, null)).build();
        }


    }

    @POST
    @Path("getSitesFromLatLong")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public Response getSitesFromLatLong(String encLatLongMap, @QueryParam("neStatus") String neStatusJson, @QueryParam("neType") String neTypeJson) {
        logger.info("inside method getSitesFromLatLong {}", encLatLongMap);
        try {
            String neStatus = null;
            String neType = null;
            if (Utils.hasValidValue(neStatusJson)) {
                neStatus = AuthenticationCommonUtil.checkForValueDecryption(neStatusJson);
            }
            if (Utils.hasValidValue(neType)) {
                neType = AuthenticationCommonUtil.checkForValueDecryption(neTypeJson);
            }

            if (Utils.hasValidValue(encLatLongMap)) {
                return Response.ok(AuthenticationCommonUtil.checkForValueEncryption(nvWorkorderService
                        .getSitesFromLatLong(AuthenticationCommonUtil.checkForValueDecryption(encLatLongMap), neStatus, neType), null))
                        .build();
            }
        } catch (Exception e) {
            logger.error("Exception in getSitesFromLatLong : {}", ExceptionUtils.getStackTrace(e));
            return Response.ok(AuthenticationCommonUtil.checkForValueEncryption(NVWorkorderConstant.FAILURE_JSON, null))
                    .build();
        }
        return Response
                .ok(AuthenticationCommonUtil.checkForValueEncryption(NVWorkorderConstant.INVALID_PARAMETER_JSON, null))
                .build();
    }


    @GET
    @Path("getNECellDetailData")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getNECellDetailData(@QueryParam("siteId") String siteId) {
        logger.info("inside method getNECellDetailData {}", siteId);
        try {
            return Response.ok(siteDataService.getNECellDetailData(Arrays.asList(siteId), new SiteInformationWrapper())).build();

        } catch (Exception e) {
            logger.error("Exception in getSitesFromLatLong : {}", ExceptionUtils.getStackTrace(e));

        }
        return null;

    }


    @GET
    @Path("sitesDetail")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getSitesDetail(@QueryParam("lastModifiedTime") Long lastModifiedTime, @QueryParam("cgi") Integer cgi, @QueryParam("countRequired") Boolean isCountRequired) {
        logger.info("inside method getSiteDetail{}", lastModifiedTime);
        try {
            if (cgi == null) {
                return Response.ok(AuthenticationCommonUtil.checkForValueEncryption(new Gson().toJson(siteDataService.getSitesDetail(lastModifiedTime)), null)).build();
            } else {
                return Response.ok(AuthenticationCommonUtil.checkForValueEncryption(new Gson().toJson(siteDataService.getSiteDetailByCGI(cgi, isCountRequired)), null)).build();
            }
        } catch (Exception e) {
            logger.error("Exception in getSitesDetail : {}", ExceptionUtils.getStackTrace(e));

            return Response.ok(AuthenticationCommonUtil.checkForValueEncryption("Problem in getting sites", null)).build();
        }

    }

    @POST
    @Path("deviceDashboard")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public String syncDeviceDashboardData(String encryptedWrapper) {

        return deviceDashboardService.persistDeviceDashboardData(encryptedWrapper);


    }

    @POST
    @Path("getBoundaryData")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public Response getBoundaryData(String encryptedWrapper) {
        logger.info("inside method getBoundaryData");

        try {
            if (StringUtils.isNotEmpty(encryptedWrapper)) {
                BoundaryDataWrapper wrapper = new Gson().fromJson(AuthenticationCommonUtil.checkForValueDecryption(encryptedWrapper),
                        BoundaryDataWrapper.class);
                return Response.ok(AuthenticationCommonUtil.checkForValueEncryption(new Gson().toJson(layerPlotService.getBoundaryData(wrapper)), null)).build();
            }
        } catch (Exception e) {
            logger.error("Exception in getBoundaryData : {}", ExceptionUtils.getStackTrace(e));
        }
        return Response.ok(AuthenticationCommonUtil.checkForValueEncryption("Problem in getting boundary", null)).build();
    }

    @POST
    @Path("getClusterViewData")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public Response getClusterViewData(String encryptedString) {
        if (encryptedString != null) {
            try {
                String data = layerPlotService.getResponseForCluster(encryptedString);
                if (data != null) {
                    return Response.ok(AuthenticationCommonUtil.checkForValueEncryption(data, null))
                            .build();
                } else {
                    return Response
                            .ok(AuthenticationCommonUtil
                                    .checkForValueEncryption(new Gson().toJson(ForesightConstants.FAILURE_JSON), null))
                            .build();
                }
            } catch (Exception e) {
                logger.error("Caught Exception {}", ExceptionUtils.getStackTrace(e));
            }
        }
        return Response
                .ok(AuthenticationCommonUtil.checkForValueEncryption(ForesightConstants.EXCEPTION_INVALID_PARAMS, null))
                .build();
    }


    @GET
    @Path("getTop10Devices")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public Response getTop10Devices(@QueryParam("deviceId") String encryptedString) {
        if (encryptedString != null) {
            try {
                String deviceId = AuthenticationCommonUtil.checkForValueEncryption(encryptedString, null);
                String data = new Gson().toJson(deviceDashboardService.getTop10DeviceWithRank(deviceId));
                if (data != null) {
                    return Response.ok(AuthenticationCommonUtil.checkForValueEncryption(data, null))
                            .build();
                } else {
                    return Response
                            .ok(AuthenticationCommonUtil
                                    .checkForValueEncryption(new Gson().toJson(ForesightConstants.FAILURE_JSON), null))
                            .build();
                }
            } catch (Exception e) {
                logger.error("Caught Exception {}", ExceptionUtils.getStackTrace(e));
            }
        }
        return Response
                .ok(AuthenticationCommonUtil.checkForValueEncryption(ForesightConstants.EXCEPTION_INVALID_PARAMS, null))
                .build();
    }

	
	@POST
	@Path("generateSiteSuggestion")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public String generateSiteSuggestion(@Multipart(value = ForesightConstants.FILE) InputStream inputFile,
			@Multipart(value = ForesightConstants.FILENAME) String fileName,
			@Multipart(NVLayer3Constants.VALUE) String encryptedJson) {
		try {
			Validate.checkNoneNull(encryptedJson);
			
			logger.info("fileName {}", fileName);
			if (encryptedJson != null) {
				String json = AuthenticationCommonUtil.checkForValueDecryption(encryptedJson);
				siteSuggestionService.persistFriendlySiteSuggestion(json, inputFile, fileName);
				
			} else {
				throw new RestException("json is null from Mobile");
			}
			logger.info("Done persist Site Suggestion");
			return AuthenticationCommonUtil.checkForValueEncryption(NVLayer3Constants.SUCCESS_JSON, null);
		} catch (NullPointerException e) {
			logger.error("NullPointerException in generateSiteSuggestion : {}", ExceptionUtils.getStackTrace(e));
			return AuthenticationCommonUtil.checkForValueEncryption(NVLayer3Constants.FAILURE_JSON, null);
		} catch (RestException e) {
			logger.error("RestException in generateSiteSuggestion : {}", ExceptionUtils.getStackTrace(e));
			return AuthenticationCommonUtil.checkForValueEncryption(e.getMessage(), null);
		} catch (IllegalArgumentException e) {
			logger.error("Exception in generateSiteSuggestion === : {}", ExceptionUtils.getStackTrace(e));
			return AuthenticationCommonUtil.checkForValueEncryption(NVWorkorderConstant.FILE_IS_EMPTY_JSON, null);
		} catch (Exception e) {
			logger.error("Exception in generateSiteSuggestion : {}", ExceptionUtils.getStackTrace(e));
			return AuthenticationCommonUtil.checkForValueEncryption(NVLayer3Constants.FAILURE_JSON, null);
		}	
	}
	    @GET
	    @Path("getSiteSuggestionData")
	    @Produces(MediaType.TEXT_PLAIN)
	    @Consumes(MediaType.TEXT_PLAIN)
	    public Response getSiteSuggestionData(@QueryParam("zoomLevel") String zoom, @QueryParam("NELat") String nELat, @QueryParam("NELng") String nELng,
	                                            @QueryParam("SWLat") String sWLat, @QueryParam("SWLng") String sWLng) {
	        try {

	        	int decryZoom = Integer.parseInt(AuthenticationCommonUtil.checkForValueDecryption(zoom));
	            double decodeNELat = Double.parseDouble(AuthenticationCommonUtil.checkForValueDecryption(nELat));
	            double decodeNELng = Double.parseDouble(AuthenticationCommonUtil.checkForValueDecryption(nELng));
	            double decodeSWLat = Double.parseDouble(AuthenticationCommonUtil.checkForValueDecryption(sWLat));
	            double decodeSWLng = Double.parseDouble(AuthenticationCommonUtil.checkForValueDecryption(sWLng));
	            logger.info("Getting parameter for getSiteSuggestionData zoom , nELat ,NELng,SWLat, SWLng {}, {} , {} ,{} ,{}",
	            		decryZoom, decodeNELat, decodeNELng, decodeSWLat, decodeSWLng);
	        
	            String siteSuggestionDataResponce = siteSuggestionService.getSiteSuggestionData(decryZoom, decodeNELat, decodeNELng, decodeSWLat, decodeSWLng);
				
	            if (siteSuggestionDataResponce != null) {
					return Response
							.ok(AuthenticationCommonUtil.checkForValueEncryption(siteSuggestionDataResponce, null))
							.build();
				}
	            else {
	            	return Response.ok(AuthenticationCommonUtil.checkForValueEncryption(ForesightConstants.EMPTY_SQUARE_BRACKET, null)).build();
	            }

	        } catch (Exception e) {
	            logger.info("Exception in getSiteSuggestionData method {}", ExceptionUtils.getStackTrace(e));
	            return Response.ok(AuthenticationCommonUtil.checkForValueEncryption(FAILURE_JSON, null)).build();
	        }


	    }

}

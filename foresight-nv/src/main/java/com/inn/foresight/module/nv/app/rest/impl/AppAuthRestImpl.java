package com.inn.foresight.module.nv.app.rest.impl;

import javax.servlet.http.HttpServletRequest;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.app.constants.AppConstants;
import com.inn.foresight.module.nv.app.service.IAPKDetailService;
import com.inn.foresight.module.nv.app.service.IAppAuthService;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;
import com.inn.foresight.module.nv.workorder.service.INVWorkorderService;
import com.inn.foresight.module.nv.workorder.utils.NVWorkorderUtils;
import com.inn.product.security.utils.AuthenticationCommonUtil;



/** The Class AppAuthRestImpl. */
@Service("AppAuthRestImpl")
@Path("/appauth/")
public class AppAuthRestImpl {

	/** The logger. */
	private Logger logger = LogManager.getLogger(AppAuthRestImpl.class);
	
	/** Instance of AppAuthServiceImpl. */
	@Autowired
	private IAppAuthService appAuthService;
	
	/** Instance of APKDetailServiceImpl. */
	@Autowired
	private IAPKDetailService apkDetailService;
	
	/** Instance of APKDetailServiceImpl. */
	@Autowired
	private INVWorkorderService nvWorkorderService;

	/**
	 * Method to Check the User Name and Password,validate it and returns the user related information or authentication failure results.
	 *
	 * @param username the username
	 * @param password the password
	 * @return the string
	 * @parameter password
	 * @parameter username
	 * @returns User Id
	 */
	@POST
	@Path(AppConstants.MOBILE_AUTH)
	public String checkAuthentication(@FormParam(AppConstants.USERNAME) String username,   
			@FormParam(AppConstants.QUERY_PARAM_PW) String password,@Context HttpServletRequest request) {
		String ip = request.getHeader(ForesightConstants.X_FORWORDED_FOR);
		logger.info(" ip  url FOR ACCESSURL IP {} ", ip);
		
		if ((username != null &&  !AppConstants.EMPTY_STRING.equalsIgnoreCase(username)) && (password != null && !AppConstants.EMPTY_STRING.equalsIgnoreCase(username))) {
			logger.info(AppConstants.USERNAME_CHECKSUM_LOGGER, username, password);
			try {   
				String verificationCode = request.getParameter("vcode");
				return appAuthService.checkAuthentication(username,password,ip,verificationCode);
			} catch (Exception e) {
				logger.error(AppConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
				return AuthenticationCommonUtil.checkForValueEncryption(e.getMessage(),null);
			}
		} 
		return AuthenticationCommonUtil.checkForValueEncryption(AppConstants.RESULT_USERNAME_OR_PW_IS_EMPTY_OR_NULL,null);
	}
	
	@POST
	@Path("mobileAppAuth1")
	public String checkAuthentication1(@FormParam(AppConstants.USERNAME) String username,   
			@FormParam(AppConstants.QUERY_PARAM_PW) String password,@Context HttpServletRequest request) {
		String ip = request.getHeader(ForesightConstants.X_FORWORDED_FOR);
		logger.info(" ip  url FOR ACCESSURL IP {}", ip);
		
		if ((username != null && !AppConstants.EMPTY_STRING.equalsIgnoreCase(username)) && (password != null && !AppConstants.EMPTY_STRING.equalsIgnoreCase(username))) {
			logger.info(AppConstants.USERNAME_CHECKSUM_LOGGER, username, password);
			try {   
				String verificationCode = request.getParameter("vcode");
				return appAuthService.checkAuthentication(username,password,ip,verificationCode);
			} catch (Exception e) {
				logger.error(AppConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
				return AuthenticationCommonUtil.checkForValueEncryption(e.getMessage(),null);
			}
		} 
		return AuthenticationCommonUtil.checkForValueEncryption(AppConstants.RESULT_USERNAME_OR_CHECKSUM_IS_EMPTY_OR_NULL,null);
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
			if(apkId != null && apkOS != null) {
				return apkDetailService.getAPKDetailById(apkId, apkOS);
			}else {
				return AuthenticationCommonUtil.checkForValueEncryption(ForesightConstants.INVALID_PARAMETERS,null);
			}
		} catch (RestException e) {
			return AuthenticationCommonUtil.checkForValueEncryption(e.getMessage(),null);
		}
	}
	
	/**
	 * Gets All Workorder Created For Provided User or workorder Id.
	 * @param workorderId the workorder id
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
							nvWorkorderService.getWorkorderDetailsByWOIdFromMobile(Integer.parseInt(decryptedId)),null)).build();
				}
			}
			return Response.ok(AuthenticationCommonUtil.checkForValueEncryption(NVWorkorderConstant.INVALID_PARAMETER_JSON,null)).build();
		} catch (Exception e) {
			logger.info("Getting Exception {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(AuthenticationCommonUtil.checkForValueEncryption(NVWorkorderConstant.FAILURE_JSON,null)).build();
		}
	}
}

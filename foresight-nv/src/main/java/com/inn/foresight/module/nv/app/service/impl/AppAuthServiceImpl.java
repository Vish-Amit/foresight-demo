package com.inn.foresight.module.nv.app.service.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.plexus.util.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.core.generic.utils.ApplicationContextProvider;
import com.inn.foresight.core.generic.utils.ConfigEnum;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.app.constants.AppConstants;
import com.inn.foresight.module.nv.app.exceptions.InvalidEncryptionException;
import com.inn.foresight.module.nv.app.service.IAppAuthService;
import com.inn.product.audit.service.AuditService;
import com.inn.product.audit.utils.AuditActionName;
import com.inn.product.audit.utils.AuditActionType;
import com.inn.product.security.auth.model.BasicAuthenticationDetail;
import com.inn.product.security.auth.service.ApplicationFeatureConfigurationService;
import com.inn.product.security.auth.service.BasicAuthenticationDetailService;
import com.inn.product.security.authentication.MobileAuthentication;
import com.inn.product.security.authentication.authenticator.ldap.LdapAuthenticator;
import com.inn.product.security.um.model.ORGAuthenticationType;
import com.inn.product.security.utils.AuthenticationCommonUtil;
import com.inn.product.security.utils.IPRangeChecker;
import com.inn.product.security.utils.enums.ApplicationFeatureKey;
import com.inn.product.security.wrapper.AccessLevelWrapper;
import com.inn.product.um.permission.utils.UmPermissionConstants;
import com.inn.product.um.role.model.UserRole;
import com.inn.product.um.user.dao.UserDao;
import com.inn.product.um.user.model.User;
import com.inn.product.um.user.service.UserService;
import com.inn.product.um.user.utils.UmConstants;

/**
 * The Class AppAuthServiceImpl.
 */
@Service("AppAuthServiceImpl")
public class AppAuthServiceImpl extends MobileAuthentication implements IAppAuthService {

	/** The logger. */
	private Logger logger = LogManager.getLogger(AppAuthServiceImpl.class);

	/** The auth manager. */
	@Autowired(required = false)
	@Qualifier("authenticationManager")
	AuthenticationManager authManager;

	/** The user service. */
	@Autowired
	UserService userService;
	@Autowired
	UserDao userDao;
	
	@Autowired
	private AuditService auditService;

	@Autowired
	private BasicAuthenticationDetailService basicAuthService;
	
	@Autowired
	private ApplicationFeatureConfigurationService applicationFeauture;

	/**
	 * Check authentication.
	 *
	 * @param username the username
	 * @param password the password
	 * @return the string
	 * @throws Exception the exception
	 */

	@Override
	public String checkAuthentication(String username, String password, String ip, String verificationcode)
		{
		Boolean isGcmAlgo = null;
		try {
			logger.info("inside checkAuthentication ");
			isGcmAlgo = AuthenticationCommonUtil.isGCMAlgorithm(username);
			password = AuthenticationCommonUtil.checkForValueDecryption(password);
			username = AuthenticationCommonUtil.checkForValueDecryption(username).toLowerCase();
			logger.info("Username :{} ", username);
			UserService userServiceFromContext = ApplicationContextProvider.getApplicationContext().getBean(UserService.class);
			User user = userServiceFromContext.findByName(username);
			String isAccessLevel = ConfigUtils.getString(ConfigEnum.IS_ACCESS_LEVEL_MOBILE);
			BasicAuthenticationDetail basicAuthDetail = basicAuthService.findByUsername(username);
			logger.info(" isAccessLevel  is True");
			String accessLevel = basicAuthDetail.getAccesslevel();
			logger.info(" accessLevel value {}", accessLevel);
			if (ForesightConstants.TRUE_LOWERCASE.equalsIgnoreCase(isAccessLevel) && !checkAccessLevel(accessLevel, ip)) {
				logger.info(" checkAccessLevel is false");
				return AuthenticationCommonUtil.checkForValueEncryption(AppConstants.UNAUTHORIZED_ACCESS, isGcmAlgo);
			}

			boolean isMobileLoginPermission = userServiceFromContext
					.authenticateUserByPermission(UmPermissionConstants.MOBILE_LOGIN_PERMISSION, username);
			logger.info("isMobileLoginPermission : {}", isMobileLoginPermission);
			if (!isMobileLoginPermission) {
				logger.info("User don't have mobile login permission");
				return AuthenticationCommonUtil
						.checkForValueEncryption(ForesightConstants.UNAUTHORIZED_MOBILE_LOGIN, isGcmAlgo);
			}
			HttpServletRequest request = AuthenticationCommonUtil.getRequest();
			String isTFACheck = request.getHeader("isTFACheck");
			if (Boolean.TRUE.equals(Boolean.valueOf(applicationFeauture.getValueByName(ApplicationFeatureKey.IS_TFA_ENABLED.getValue())))
					&& Strings.isNullOrEmpty(isTFACheck)) {
					return AuthenticationCommonUtil.checkForValueEncryption(
							AppConstants.FAILURE_JSON_WITH_REASON.replace("{0}", "Invalid Headers"), isGcmAlgo);
			} else if (Boolean.TRUE.equals(Boolean.valueOf(AuthenticationCommonUtil.checkForValueDecryption(isTFACheck)))) {
				BasicAuthenticationDetail basicAuthenticationDetail = basicAuthService.findByUsername(username);

				boolean isTFAAuthorized = AuthenticationCommonUtil.checkVerificationCode(
					AuthenticationCommonUtil.checkForValueDecryption(verificationcode),
					basicAuthenticationDetail.getSecretkey());
				if (!isTFAAuthorized) {
					return AuthenticationCommonUtil.checkForValueEncryption(
							AppConstants.FAILURE_JSON_WITH_REASON.replace("{0}", "Invalid verification Code"),
							isGcmAlgo);
				}
			}

			authenticateUsingContext(username, password, userServiceFromContext, user);
			return checkAuthenticationByGCMAlgoAndUser(isGcmAlgo, user);
			
			} catch (NullPointerException e) {
			logger.error(AppConstants.NULL_ERROR_LOGGER, ExceptionUtils.getStackTrace(e));
			return AuthenticationCommonUtil.checkForValueEncryption(AppConstants.FAILURE_JSON, isGcmAlgo);
		} catch (Exception e) {
			if (e.getMessage().contains("Invalid Verification Code")) {
				return AuthenticationCommonUtil.checkForValueEncryption(
						AppConstants.FAILURE_JSON_WITH_REASON.replace("{0}", "Invalid Verification Code"), isGcmAlgo);
			}
			logger.error(AppConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			return AuthenticationCommonUtil.checkForValueEncryption(AppConstants.FAILURE_JSON, isGcmAlgo);
		}
	}

	private String checkAuthenticationByGCMAlgoAndUser(Boolean isGcmAlgo, User user) {
		if(user!=null) {
			return AuthenticationCommonUtil.checkForValueEncryption(
				AppConstants.RESULT_SUCCESS_WITH_USER_ID + user.getUserid() + AppConstants.COMMA+ getUserRoleJson(user.getUserRole())+ AppConstants.CLOSE_CURELY_BRACE, isGcmAlgo);
		}
		else {
			return AuthenticationCommonUtil.checkForValueEncryption(AppConstants.FAILURE_JSON, isGcmAlgo);
		}
	}

	private void authenticateUsingContext(String username, String password, UserService userService, User user) {
		Map<String, String> userpass = new HashMap<>();
		userpass.put(AppConstants.USERNAME, username);
		userpass.put(AppConstants.CHECKSUM, password);
		checkLDAPAuthenticationForMobile(userpass);
		username = userpass.get(AppConstants.USERNAME);
		password = userpass.get(AppConstants.CHECKSUM);
		logger.info(AppConstants.USERNAME_CHECKSUM_LOGGER, username, password);
		logger.info("Encoded password : {}", password);
		SecurityContext securityContext = SecurityContextHolder.getContextHolderStrategy().getContext();
		Authentication authentication = securityContext.getAuthentication();
		logger.info("Authentication : {}", authentication);

		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
		// Gulab// only for temp solution for testing-- hitesh to update on mobile login
		authManager.authenticate(authToken);
		securityContext.setAuthentication(authToken);
		Authentication authentication1 = securityContext.getAuthentication();
		logger.info("Authentication AFTER : {}", authentication1);

		ServletRequestAttributes attribute = (ServletRequestAttributes) RequestContextHolder
				.currentRequestAttributes();
		attribute.getRequest().getSession(Boolean.TRUE).setAttribute(AppConstants.SPRING_SECURITY_CONTEXT,
					securityContext);
		attribute.getRequest().getSession().setAttribute(ForesightConstants.NON_SSO_CONTEXT, true);
		attribute.getRequest().getSession().setAttribute(AppConstants.FIRST_REQUEST, true);
		if (user != null) {
			logger.info("going to update mobile user : {}", user.getUserName() != null ? user.getUserName(): user);
			userService.updateMobileLoginTimeAndStatus(user, true);
			auditService.createAuditForTabletAuthentication(user, AuditActionType.LOGIN, AuditActionName.MOBILE_LOGIN, true);
			
		}
	}

	private String getUserRoleJson(Set<UserRole> userRoles) {
		if (CollectionUtils.isNotEmpty(userRoles)) {
			Set<String> roleNames = new HashSet<>();

			for (UserRole role : userRoles) {
				roleNames.add(role.getRole().getRoleName());
			}

			return AppConstants.ROLES_JSON_STRING+new Gson().toJson(roleNames);

		} else {

			return AppConstants.BLANK_ROLE_JSON;
		}
	}

	private void checkLDAPAuthenticationForMobile(Map<String, String> userPass) {
		logger.info("Going to check LDAP Authentication For Mobile");
		UserService userServiceFromContext = ApplicationContextProvider.getApplicationContext().getBean(UserService.class);

		User user = userServiceFromContext.findByName(userPass.get(ForesightConstants.USERNAME_SMALL).toLowerCase());
		BasicAuthenticationDetail findByUsername = basicAuthService.findByUsername(user.getUserName());

		ORGAuthenticationType org = findByUsername.getOrgAuthenticationType();
		logger.info("ORGAuthenticationType: {}", org.getAuthenticationtype());
		if (org.getAuthenticationtype().equalsIgnoreCase(UmConstants.ACTIVEDIRECTORY)
				&& Boolean.TRUE.equals(LdapAuthenticator.isAuthenticated(userPass.get(AppConstants.USERNAME),
						userPass.get(AppConstants.CHECKSUM)))) {
			userPass.put(AppConstants.CHECKSUM, findByUsername.getChecksum());
			logger.info("Authenticated via LDAP Authenticator");
		}
	}

	private boolean checkAccessLevel(String accessLevel, String ipCheck) {
		if (!Strings.isNullOrEmpty(accessLevel)) {
			logger.info(" ipCheck is not null");
			try {
				Map<String, AccessLevelWrapper> map = new ObjectMapper().readValue(accessLevel,
						new TypeReference<Map<String, AccessLevelWrapper>>() {
						});
				AccessLevelWrapper accessWrapper = map.get(ConfigEnum.SP_FORESIGHT.getValue());
				return accessWrapper != null ? getFlagByAccessWrapper(ipCheck, accessWrapper) : Boolean.FALSE;
			} catch (Exception e) {
				logger.error("error while checkAccessLevel {}", Utils.getStackTrace(e));
			}
		}
		return false;
	}

	private boolean getFlagByAccessWrapper(String ipCheck, AccessLevelWrapper accessWrapper) {
		boolean flag = false;
		if (Boolean.TRUE.equals(accessWrapper.getMobile_internet())) {
			flag = true;
		} else if (Boolean.TRUE.equals(accessWrapper.getMobile_intranet())) {
			logger.info(" MOBILE_INTRANET is true");
			List<String> ipRangeString = ConfigUtils.getStringList(ConfigEnum.ACCESS_IP_RANGES_MOBILE);
			if (CollectionUtils.isNotEmpty(ipRangeString)) {
				logger.info("ipRangeString  {}", ipRangeString);
				for (String ip : ipRangeString) {
					String[] ipRange = ip.trim().split(ForesightConstants.UNDERSCORE);
					logger.info(" ipRange[0] {}, ipRange[1] {} ipCheck {}",ipRange[0],ipRange[1],ipCheck);
					if (IPRangeChecker.isValidRange(ipRange[0], ipRange[1], ipCheck)) {
						flag = true;
					}
				}
			} else if (ipRangeString == null || ipRangeString.isEmpty()) {
				flag = true;
			}
		}
		return flag;
	}

	@Override
	public String isUserTFAuthorised(String username) {
		logger.trace("inside the method isUserTFAuthorised username {}", username);
		Boolean isGcmAlgo = null;
		try {
			isGcmAlgo = AuthenticationCommonUtil.isGCMAlgorithm(username);
			username = AuthenticationCommonUtil.checkForValueDecryption(username).toLowerCase();
			if (username != null) {
				List<String> permissionList = userDao.getAllPermissionsByUserName(username);
				if (permissionList.contains(AppConstants.TFA_PERMISSION_NAME)) {
					return AuthenticationCommonUtil.checkForValueEncryption(Boolean.FALSE, isGcmAlgo);
				}
			}
		} catch (InvalidEncryptionException ie) {
			logger.error("InvalidEncryptionException in method isUserTFAuthorised {}", Utils.getStackTrace(ie));
			return AuthenticationCommonUtil.checkForValueEncryption("Invalid Encryption Used", isGcmAlgo);

		} catch (Exception e) {
			logger.error("Exception inside the method isUserTFAuthorised {}", Utils.getStackTrace(e));
			return AuthenticationCommonUtil.checkForValueEncryption("User doesn't have sufficient permission", isGcmAlgo);
		}
		return AuthenticationCommonUtil.checkForValueEncryption(Boolean.TRUE, isGcmAlgo);
	}

	@Override
	public String checkAuthentication1(String username, String password) {
		return super.authenticate(username, password);
	}

	@Override
	public String postAuthentication(String username) {
		User user = userService.findByName(username);
		userService.updateMobileLoginTimeAndStatus(user, true);
		return user.getUserid().toString();
	}

}

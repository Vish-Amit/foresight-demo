package com.inn.foresight.module.nv.pushnotification.utils;

import java.net.InetAddress;
import java.util.HashMap;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.utils.Utils;
import com.inn.foresight.core.generic.utils.ConfigEnum;
import com.inn.foresight.module.nv.pushnotification.constants.PushNotificationConstants;
import com.inn.foresight.module.nv.pushnotification.model.PushNotification;
import com.inn.foresight.module.nv.pushnotification.model.PushNotification.OS;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;
import com.inn.product.security.utils.AuthenticationCommonUtil;

/** The Class PushNotificationUtils. */
public class PushNotificationUtils {
	
    private PushNotificationUtils() {
    	
    }
	/** The logger. */
	private static Logger logger = LogManager.getLogger(PushNotificationUtils.class);
	/**
	 * Prepare pay load JSON object.
	 *
	 * @param pushNotification
	 *            the push notification
	 * @return the JSON object
	 * @throws RestException
	 */
	@SuppressWarnings({ "rawtypes" })
	public static JSONObject preparePayLoadJSONObject(PushNotification pushNotification) {
		JSONArray fcmKeyList = new JSONArray();
		if (pushNotification.getUseClientFcmKey()) {
			if(pushNotification.getDeviceInfo().getClientFcmKey()==null) {
				throw new RestException("Client FCM Key is null");
			}
			fcmKeyList.put(pushNotification	.getDeviceInfo()
											.getClientFcmKey());
			logger.info("Using client FCM Key");
		} else {
			if(pushNotification.getDeviceInfo().getFcmKey()==null) {
				throw new RestException("FCM Key is null");
			}
			fcmKeyList.put(pushNotification	.getDeviceInfo()
											.getFcmKey());
		}
		try {
			if (pushNotification.getNotificationPayLoad() != null) {
				JSONObject json = new JSONObject();
				
				String payLoad=pushNotification.getNotificationPayLoad();
				HashMap map = new ObjectMapper().readValue(pushNotification.getNotificationPayLoad(), HashMap.class);
				if(payLoad.toCharArray().length>=4096) {
				map.remove("wrapper");	
				}
				json.put(PushNotificationConstants.KEY_DATA,map);
				json.put(PushNotificationConstants.KEY_REGISTRATION_IDS, fcmKeyList);
				setPriority(pushNotification, json);
				updateIOSPayload(pushNotification, json);
				return json;
			} else {
				throw new RestException("PushNotification Payload is null");
			}
		} catch (Exception e) {
			logger.error(NVWorkorderConstant.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtils.getMessage(e));
		}
	}

	private static void setPriority(PushNotification pushNotification, JSONObject json)  {
		if (pushNotification.getOs() != null && OS.ANDROID	.name()
															.equalsIgnoreCase(pushNotification	.getOs()
																								.name())) {
			try {
				json.put(PushNotificationConstants.KEY_PRIORITY, PushNotificationConstants.PRIORITY_VALUE);
			} catch (JSONException e) {
				logger.error("JSONException  in side setPriority {}",Utils.getStackTrace(e));
			}
		}

	}
	

	private static void updateIOSPayload(PushNotification pushNotification, JSONObject json) throws JSONException  {
		if(pushNotification.getOs()!=null&&OS.IOS.name().equalsIgnoreCase(pushNotification.getOs().name())) {
			json.put(PushNotificationConstants.KEY_CONTENT_AVAILABLE, true);
			if(pushNotification.getNotificationPayLoad().contains(PushNotificationConstants.LIVE_LOCATION)) {
			    json.put(PushNotificationConstants.KEY_NOTIFICATION,  new JSONObject());//Silent notification
			}else if(pushNotification.getNotificationPayLoad().contains(PushNotificationConstants.QUICK_TEST)){
				//Mandatory to send notification key with body structure to show ios-notification on UI
				HashMap<String,Object> newMap=new HashMap<>();
				newMap.put(PushNotificationConstants.KEY_BODY, PushNotificationConstants.IOS_QUICKE_TEST_MESSAGE);
				json.put(PushNotificationConstants.KEY_NOTIFICATION, newMap);
			}
		}
	}

	public static Integer decryptAndParseNotificationID( String encrptedNotificationId) {
		try {
			return Integer.parseInt(AuthenticationCommonUtil.checkForValueDecryption(encrptedNotificationId));
		} catch (Exception e) {
			logger.error("Exception while decryptAndParseNotificationID for encrptedNotificationId {}",encrptedNotificationId);
			throw new RestException(e.getMessage());
		}
	}	
	public static boolean isToEnableNotification() {
		String tomcatPath=System.getProperty("catalina.base");
		String configTomcatPath=ConfigUtils.getString(ConfigEnum.TOMCAT_PATH.getValue());
		String configHostName=ConfigUtils.getString(ConfigEnum.HOST_NAME_TO_CHECK.getValue());
		logger.info("current tomcat path : {} , config tomcat path : {} ",tomcatPath,configTomcatPath);	
		try {
			String hostName=InetAddress.getLocalHost().getHostName();
			logger.info("current hostname : {}  , config host name : {}", hostName,configHostName);
			if(configTomcatPath.equalsIgnoreCase(tomcatPath) &&configHostName.equalsIgnoreCase(hostName)) {
				logger.info("Notification is enabled");
				return true;
			}
		} catch (Exception e) {
			logger.info("Exception in isToEnableNotification {} ",ExceptionUtils.getStackTrace(e));
		}
		return false;
	}
}

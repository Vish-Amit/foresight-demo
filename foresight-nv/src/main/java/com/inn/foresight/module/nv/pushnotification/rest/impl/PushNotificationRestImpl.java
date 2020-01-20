package com.inn.foresight.module.nv.pushnotification.rest.impl;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.module.nv.pushnotification.constants.PushNotificationConstants;
import com.inn.foresight.module.nv.pushnotification.rest.IPushNotificationRest;
import com.inn.foresight.module.nv.pushnotification.service.IPushNotificationService;
import com.inn.foresight.module.nv.pushnotification.wrapper.PushNotificationWrapper;

@Path("/PushNotification")
@Service("PushNotificationRestImpl")
public class PushNotificationRestImpl implements IPushNotificationRest {
	/** The logger. */
	private Logger logger = LogManager.getLogger(PushNotificationRestImpl.class);

	@Autowired
	private IPushNotificationService iPushNotificationService;

	/**
	 * Gets the all device list.
	 *
	 * @param lLimit
	 *            the l limit
	 * @param noOfRecords
	 *            the no of records
	 * @return the all device list
	 */
	@POST
	@Path("createPushNotification")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public Response createPushNotification(PushNotificationWrapper pushNotificationWrapper) {
		logger.error("Going to create Pushnotification {}", new Gson().toJson(pushNotificationWrapper));
		Response responseToReturn = null;
		if (pushNotificationWrapper != null && (pushNotificationWrapper.getDeviceId() != null
				|| pushNotificationWrapper.getImei() != null || pushNotificationWrapper.getImsi() != null)) {

			try {
				PushNotificationWrapper pNotificationWrapper = iPushNotificationService.createNotification(
						pushNotificationWrapper);
				if (pNotificationWrapper != null) {
					responseToReturn = Response	.ok(pNotificationWrapper)
												.build();
				} else {
					responseToReturn = Response	.ok(PushNotificationConstants.FAILURE_JSON)
												.build();
				}
			} catch (RestException e) {
				logger.error("RestException in createPushNotification {}", ExceptionUtils.getStackTrace(e));
				responseToReturn = Response	.ok(ExceptionUtils.getMessage(e))
											.build();
			}
		} else {
			responseToReturn = Response	.ok(PushNotificationConstants.INVALID_NOTIFICATION_OBJECT_JSON)
										.build();
		}
		return responseToReturn;
	}

	@POST
	@Path("updateNotificationForAcknowledgement")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response updateNotificationForAcknowledgement(String encrptedNotificationId) {
		Response responseToReturn = null;
		if (encrptedNotificationId != null) {
			responseToReturn = Response	.ok(iPushNotificationService.updateNotificationForAcknowledgement(encrptedNotificationId))
										.build();
		} else {
			responseToReturn = Response	.ok(PushNotificationConstants.INVALID_PARAMETER_JSON)
										.build();
		}
		return responseToReturn;
	}

}

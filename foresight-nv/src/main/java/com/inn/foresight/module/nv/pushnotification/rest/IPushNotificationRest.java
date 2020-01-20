package com.inn.foresight.module.nv.pushnotification.rest;

import javax.ws.rs.core.Response;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.module.nv.pushnotification.wrapper.PushNotificationWrapper;

/** The Interface IPushNotificationRest. */
public interface IPushNotificationRest {

	/**
	 * Creates the push notification.
	 *
	 * @param pushNotificationWrapper the push notification wrapper
	 * @return the response
	 * @throws RestException the rest exception
	 */
	Response createPushNotification(PushNotificationWrapper pushNotificationWrapper);
	
	/**
	 * Send notification acknowledgment.
	 *
	 * @param reqData the req data
	 * @return the response
	 * @throws RestException the rest exception
	 */
	Response updateNotificationForAcknowledgement(String notificationId);
	
}

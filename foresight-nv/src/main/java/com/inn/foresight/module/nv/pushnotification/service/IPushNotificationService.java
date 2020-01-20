package com.inn.foresight.module.nv.pushnotification.service;

import java.util.List;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.module.nv.device.wrapper.NVDeviceDataWrapper;
import com.inn.foresight.module.nv.pushnotification.model.PushNotification;
import com.inn.foresight.module.nv.pushnotification.wrapper.PushNotificationWrapper;

/** The Interface IPushNotificationService. */
public interface IPushNotificationService {
	
	/**
	 * Creates the notification.
	 *
	 * @param notification the notification
	 * @return the push notification
	 * @throws RestException the rest exception
	 */
	PushNotification createNotification(PushNotification notification);
	
	
	PushNotificationWrapper createNotificationForCustomercare(PushNotificationWrapper notification);
	
	/**
	 * Creates the notification.
	 *
	 * @param notificationWrapper the notification wrapper
	 * @return the push notification
	 * @throws RestException the rest exception
	 */
	PushNotificationWrapper createNotification(PushNotificationWrapper notificationWrapper);
	
	/**
	 * Process notification.
	 *
	 * @throws RestException the rest exception
	 */
	void processNotification();

	/**
	 * Gets the notification file detail by file id.
	 *
	 * @param id the id
	 * @param llimit the llimit
	 * @param ulimit the ulimit
	 * @return the notification file detail by file id
	 * @throws RestException the rest exception
	 */
	List<PushNotification> getNotificationFileDetailByFileId(Integer id, Integer llimit, Integer ulimit);
	
	/**
	 * Gets the feed back notification file detail list count.
	 *
	 * @param id the id
	 * @return the feed back notification file detail list count
	 * @throws RestException the rest exception
	 */
	Long getFeedBackNotificationFileDetailListCount(Integer id);

	/**
	 * Update notification for acknowledgement.
	 *
	 * @param encrptedNotificationId the encrpted notification id
	 * @return the string
	 */
	String updateNotificationForAcknowledgement(String encrptedNotificationId);
	
	/**
	 * Update notification.
	 *
	 * @param notification the notification
	 * @return the push notification
	 */
	PushNotification updateNotification(PushNotification notification);

	/**
	 * Find by pk.
	 *
	 * @param pushNotificationId the push notification id
	 * @return the push notification
	 */
	PushNotification findByPk(Integer pushNotificationId);

	/**
	 * Update live location detail.
	 *
	 * @param nvDataWrapper the nv data wrapper
	 * @return the push notification
	 */
	PushNotification updateLiveLocationDetail(NVDeviceDataWrapper nvDataWrapper);

	String updateLiveLocationPushNotification(NVDeviceDataWrapper nvDataWrapper, String[] geographies,
			PushNotification pushNotification);


	
	
	

}

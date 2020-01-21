package com.inn.foresight.core.mail.service;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.core.mail.model.EmailNotification;

/**
 * The Interface IEmailNotificationService.
 * @author innoeye
 */
public interface IEmailNotificationService extends IGenericService<Integer, EmailNotification> {

	/**
	 * Send queue mail async.
	 *
	 * @param sentNotification the sent notification
	 */
	void sendQueueMailAsync(EmailNotification sentNotification);

	/**
	 * Delete notifications before seven days by scheduler.
	 */
	void deleteNotificationsBeforeSevenDaysByScheduler();

	/**
	 * Send mail on demand.
	 *
	 * @param notificationId the notification id
	 * @return the boolean
	 */
	Boolean sendEMailOnDemand(Long notificationId);


	/**
	 * Sent mail notification to user.
	 *
	 * @param recipientEmail the recipient email
	 * @param subject the subject
	 * @param message the message
	 * @throws RestException the rest exception
	 */
	void sentEMailNotificationToUser(String recipientEmail, String subject, String message);

	

}


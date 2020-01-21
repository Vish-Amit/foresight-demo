package com.inn.foresight.core.mail.dao;

import java.util.Date;
import java.util.List;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.foresight.core.mail.model.EmailNotification;
import com.inn.foresight.core.mail.utils.NotificationStatus;

/**
 * The Interface IEmailNotificationDao.
 * @author innoeye
 */
public interface IEmailNotificationDao extends IGenericDao<Integer, EmailNotification>
{

	/**
	 * Gets the queue data to schedule.
	 *
	 * @param queue the queue
	 * @param regulatoryLogoutEvent the regulatory logout event
	 * @return the queue data to schedule
	 */
	List<EmailNotification> getQueueDataToSchedule(NotificationStatus queue, String regulatoryLogoutEvent);

	/**
	 * Creates the notification.
	 *
	 * @param emailId the email id
	 * @param mailSubject the mail subject
	 * @param mailBody the mail body
	 * @param date the date
	 * @param status the status
	 * @param senderMailId the sender mail id
	 */
	void createNotification(String emailId, String mailSubject,
			String mailBody, Date date, NotificationStatus status, String senderMailId);

	/**
	 * Gets the queue data to mail.
	 *
	 * @param queue the queue
	 * @return the queue data to mail
	 */
	List<EmailNotification> getQueueDataToMail(NotificationStatus queue);

	/**
	 * Creates the notification with attachment.
	 *
	 * @param emailId the email id
	 * @param bcc the bcc
	 * @param mailSubject the mail subject
	 * @param mailBody the mail body
	 * @param date the date
	 * @param status the status
	 * @param attachment the attachment
	 * @param downloadFileNames the download file names
	 * @param senderMailId the sender mail id
	 */
	void createNotificationWithAttachment(String emailId, String bcc,
			String mailSubject, String mailBody, Date date,
			NotificationStatus status, String attachment,
			String downloadFileNames, String senderMailId);

	/**
	 * Update status by id.
	 *
	 * @param ids the ids
	 * @param status the status
	 */
	void updateStatusById(List<Long> ids, NotificationStatus status);
	
	/**
	 * Delete notifications before days by status.
	 *
	 * @param status the status
	 * @param date the date
	 */
	void deleteNotificationsBeforeDaysByStatus(NotificationStatus status, Date date);

	/**
	 * Creates the notification with out attachment and CC.
	 *
	 * @param emailId the email id
	 * @param cc the cc
	 * @param mailSubject the mail subject
	 * @param mailBody the mail body
	 * @param date the date
	 * @param status the status
	 */
	void createNotificationWithOutAttachmentAndCC(String emailId, String cc, String mailSubject, String mailBody, Date date,
			NotificationStatus status);

	/**
	 * Find by notification id.
	 *
	 * @param notificationId the notification id
	 * @return the sent notifications
	 */
	EmailNotification findByNotificationId(Long notificationId);


	/**
	 * Creates the notification with attachment and CC.
	 *
	 * @param emailId the email id
	 * @param bcc the bcc
	 * @param cc the cc
	 * @param mailSubject the mail subject
	 * @param mailBody the mail body
	 * @param date the date
	 * @param status the status
	 * @param attachment the attachment
	 * @param downloadFileNames the download file names
	 */
	void createNotificationWithAttachmentAndCC(String emailId, String bcc,
			String cc, String mailSubject, String mailBody, Date date,
			NotificationStatus status, String attachment,
			String downloadFileNames);


	/**
	 * Creates the notification with out attachment and CC and BCC.
	 *
	 * @param emailId the email id
	 * @param bcc the bcc
	 * @param cc the cc
	 * @param mailSubject the mail subject
	 * @param mailBody the mail body
	 * @param date the date
	 * @param status the status
	 */
	void createNotificationWithOutAttachmentAndCCAndBCC(String emailId,
			String bcc, String cc, String mailSubject, String mailBody,
			Date date, NotificationStatus status);

	/**
	 * Creates the notification with inline image and attachment.
	 *
	 * @param emailId the email id
	 * @param cc the cc
	 * @param bcc the bcc
	 * @param mailSubject the mail subject
	 * @param mailBody the mail body
	 * @param date the date
	 * @param status the status
	 * @param attachment the attachment
	 * @param downloadFileNames the download file names
	 * @param inlineImageId the inline image id
	 * @param inlineImagePath the inline image path
	 */
	void createNotificationWithInlineImageAndAttachment(String emailId,String cc,
			String bcc, String mailSubject, String mailBody, Date date,
			NotificationStatus status, String attachment,
			String downloadFileNames, String inlineImageId,
			String inlineImagePath);




	
 

}

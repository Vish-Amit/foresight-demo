package com.inn.foresight.core.mail.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inn.commons.configuration.ConfigUtils;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.service.impl.AbstractService;
import com.inn.core.generic.utils.ApplicationContextProvider;
import com.inn.foresight.core.generic.utils.ConfigUtil;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.mail.dao.IEmailNotificationDao;
import com.inn.foresight.core.mail.model.EmailNotification;
import com.inn.foresight.core.mail.service.IEmailNotificationService;
import com.inn.foresight.core.mail.service.IMailSender;
import com.inn.foresight.core.mail.utils.NotificationStatus;
import com.inn.foresight.core.mail.utils.wrapper.mail.Attachment;
import com.inn.foresight.core.mail.utils.wrapper.mail.Attachment.AttachmentType;
import com.inn.foresight.core.report.utils.ReportUtils;

/**
 * The Class EmailNotificationServiceImpl.
 * 
 * @author innoeye
 */
@Service("EmailNotificationServiceImpl")
@Transactional
public class EmailNotificationServiceImpl extends AbstractService<Integer, EmailNotification>
		implements IEmailNotificationService {

	/** The logger. */
	private Logger logger = LogManager.getLogger(EmailNotificationServiceImpl.class);

	/** The emailNotificationsDao dao. */
	@Autowired
	private IEmailNotificationDao emailNotificationsDao;

	/** The mail sender. */
	@Autowired
	private IMailSender mailSender;

	public void setDao(IEmailNotificationDao emailNotificationsDao) {
		super.setDao(emailNotificationsDao);
		this.emailNotificationsDao = emailNotificationsDao;
	}

	/**
	 * Returns the list of EmailNotification using entity.
	 *
	 */

	@Override
	public List<EmailNotification> search(EmailNotification emailnotifications) {
		logger.info("Finding record by CellSite name :" + emailnotifications);
		return super.search(emailnotifications);
	}

	/**
	 * Returns the emailnotifications finding by id.
	 *
	 */

	@Override
	public EmailNotification findById(Integer primaryKey) {
		logger.info("Finding record by primaryKey :" + primaryKey);
		try {
			return super.findById(primaryKey);
		} catch (EmptyResultDataAccessException ex) {
			throw ex;
		} catch (NoResultException ex) {
			throw new NoResultException("No result found on serching data using primary key" + primaryKey);
		}
	}

	/**
	 * Returns the list of EmailNotification .
	 *
	 */

	@Override
	public List<EmailNotification> findAll() {
		try {
			return super.findAll();

		} catch (EmptyResultDataAccessException ex) {
			throw ex;
		} catch (NoResultException ex) {
			throw new NoResultException("No result found on serching all data of type " + this.getClass().getName());
		}
	}

	/**
	 * Send queue mail async.
	 *
	 * @param emailNotification the sent notification
	 */
	@Override
	public void sendQueueMailAsync(EmailNotification emailNotification) {
		try {
			getEmailNotificationDetail(emailNotification);
		} catch (FileNotFoundException e) {
			increaseRetryCount(emailNotification, emailNotification.getRetryCount(), emailNotification.getStatus(),
					new RestException(e));
			updateemailNotificationEntity(emailNotification);
			logger.error("file doesn't exist for id: {} ", emailNotification.getId());
		} catch (Exception e) {
			logger.error("@sendQueueMailAsync getting Exception for id: {} and err={}", emailNotification.getId(),
					ExceptionUtils.getStackTrace(e));
		}
	}

	private void getEmailNotificationDetail(EmailNotification emailNotification) throws FileNotFoundException {
		final String emailId = emailNotification.getSendTo();
		final String[] emailIds = StringUtils.split(emailId, ",");
		final String message = emailNotification.getMessage();
		final String subject = emailNotification.getSubject();
		final String[] cc = StringUtils.split(emailNotification.getCc(), ",");
		final String[] bcc = StringUtils.split(emailNotification.getBcc(), ",");
		final String fromEmail = emailNotification.getFromEmail();
		Integer retryCount = emailNotification.getRetryCount();
		String inlineImagePath = emailNotification.getInlineImagePath();
		NotificationStatus status = emailNotification.getStatus();
		String attachmentStr = emailNotification.getAttachmentPath();
		String[] attachmentArr = StringUtils.split(attachmentStr, ",");
		String downloadFileNamesStr = emailNotification.getDownloadFileName();
		String[] downloadFileNamesArr = StringUtils.split(downloadFileNamesStr, ",");
		final Map<String, String> inlineImageMap = prepareInlineImageMap(emailNotification);
		List<Attachment> attachments = prepareAttachmentList(attachmentArr, downloadFileNamesArr);
		try {
			if (attachmentStr != null || inlineImagePath != null) {
				if (!attachments.isEmpty() || inlineImageMap.size() > 0) {
					logger.info("@going to send mail with attachment for the  id={}", emailNotification.getId());
					emailNotification.setModificationtime(new Date());
					updateQueueMailStatus(emailNotification);
					mailSender.sendMailWithMultipleRecipientAndAttachments(emailIds, cc, bcc, subject, message,
							attachments, inlineImageMap, fromEmail);
					logger.info("mail send successfully {}", emailNotification.getId());
				}
			} else {
				logger.info("@going to send mail for the  id={}", emailNotification.getId());
				emailNotification.setModificationtime(new Date());
				updateQueueMailStatus(emailNotification);
				mailSender.sendMailWithMultipleRecipient(emailIds, cc, bcc, subject, message, fromEmail);
				logger.info("mail send success fully {}", emailNotification.getId());
			}
		} catch (JDBCConnectionException e) {
			logger.info("@sendQueueMailAsync getting JDBC close Connection for  id: {}", emailNotification.getId());
		} catch (RestException e) {
			logger.error("@sendQueueMailAsync getting business Exception for id: {}", emailNotification.getId());
			increaseRetryCount(emailNotification, retryCount, status, e);
			updateemailNotificationEntity(emailNotification);
		}
	}

	/**
	 * Update sent notification entity.
	 *
	 * @param emailNotification the sent notification
	 */
	private void updateemailNotificationEntity(EmailNotification emailNotification) {
		try {
			super.update(emailNotification);
		} catch (RestException e1) {
			logger.error("@sendQueueMailAsync getting business Exception when retrying to Updation for id: {}",
					emailNotification.getId());
		}
	}

	/**
	 * Increase retry count.
	 *
	 * @param emailNotification the sent notification
	 * @param retryCount        the retry count
	 * @param status            the status
	 * @param e                 the e
	 */
	private void increaseRetryCount(EmailNotification emailNotification, Integer retryCount, NotificationStatus status,
			RestException e) {
		if (retryCount == null) {
			retryCount = 0;
			emailNotification.setRetryCount(++retryCount);
		} else {
			emailNotification.setRetryCount(++retryCount);
		}

		Integer rCount = Utils.toInteger(ConfigUtils.getString(ConfigUtil.SENT_NOTIFICATION_RETRY_COUNT));
		if (retryCount <= rCount) {
			// Update with Old status when Mail is not send
			emailNotification.setStatus(status); // Update with old status
			emailNotification.setModificationtime(new Date());
		} else {
			emailNotification.setStatus(NotificationStatus.ERROR);
			emailNotification.setModificationtime(new Date());
			emailNotification.setRetryCount(--retryCount);
		}
		String errorDetail = ExceptionUtils.getStackTrace(e);
		if (errorDetail.length() > 4000) {
			emailNotification.setErrorDetail(errorDetail.substring(0, 4000));
		} else {
			emailNotification.setErrorDetail(errorDetail);
		}
	}

	/**
	 * Update queue mail status.
	 *
	 * @param emailNotification the sent notification
	 * @throws RestException the rest exception
	 */
	private void updateQueueMailStatus(EmailNotification emailNotification) {
		emailNotification.setStatus(NotificationStatus.SENT);
		try {
			emailNotificationsDao.update(emailNotification);
		} catch (DaoException e) {
			logger.error("DaoException in updateQueueMailStatus  {} ", e.getMessage());
		} catch (Exception e) {
			logger.error("Exception in updateQueueMailStatus  {} ", e.getMessage());
		}

	}

	/**
	 * Prepare attachment list.
	 *
	 * @param attachmentArr     the attachment arr
	 * @param downloadFileNames the download file names
	 * @return the list
	 * @throws FileNotFoundException the file not found exception
	 */
	private List<Attachment> prepareAttachmentList(String[] attachmentArr, String[] downloadFileNames)
			throws FileNotFoundException {
		List<Attachment> attachments = new ArrayList<>();
		if (attachmentArr != null) {
			for (int i = 0; i < attachmentArr.length; i++) {
				File file = ReportUtils.getIfFileExists(ReportUtils.getAbsoluteFilePath(attachmentArr[i]));
				if (file != null) {
					Attachment obj = new Attachment();
					obj.setFilePath(attachmentArr[i]);
					obj.setType(AttachmentType.MS_EXCEL);
					obj.setFileName(downloadFileNames[i]);
					try {
						byte[] fileBytes = ReportUtils.getBytes(file);
						if (fileBytes != null) {
							logger.info("fileBytes size= " + fileBytes.length);
							obj.setAttachmentFile(fileBytes);
						}
					} catch (IOException e) {
						logger.error("IOException in getting bytes of File from filePath={} ,err={} ", attachmentArr[i],
								e.getMessage());
					}
					attachments.add(obj);
				} else {
					throw new FileNotFoundException(
							"FileNotFoundException for file path " + ReportUtils.getAbsoluteFilePath(attachmentArr[i]));
				}
			}
		}
		return attachments;
	}

	/**
	 * Prepare inline image map.
	 *
	 * @param emailNotification the sent notification
	 * @return the map
	 */
	private Map<String, String> prepareInlineImageMap(EmailNotification emailNotification) {
		Map<String, String> imageMap = new HashMap<>();
		String inlineImagePath = emailNotification.getInlineImagePath();
		String[] inlineImageArr = StringUtils.split(inlineImagePath, ",");
		String inlineImageId = emailNotification.getInlineImageId();
		String[] imageIdArr = StringUtils.split(inlineImageId, ",");
		try {
			if (inlineImageArr != null) {
				for (int i = 0; i < inlineImageArr.length; i++) {
					if (imageIdArr != null) {
						imageMap.put(imageIdArr[i], inlineImageArr[i]);
					}
				}
			}
		} catch (Exception e) {
			logger.error("error in prepareInlineImageMap, error = {}", ExceptionUtils.getStackTrace(e));
		}
		return imageMap;
	}

	/**
	 * Gets the bean.
	 *
	 * @return the bean
	 */
	public IEmailNotificationService getBean() {
		return (IEmailNotificationService) ApplicationContextProvider.getApplicationContext()
				.getBean("EmailNotificationServiceImpl");
	}

	/**
	 * Delete notifications before seven days by scheduler.
	 */
	@Override
	public void deleteNotificationsBeforeSevenDaysByScheduler() {
		try {
			logger.info("inside @deleteNotificationsBeforeSevenDaysByScheduler ");
			Date date = ReportUtils.getDateUsingDays(new Date(), 0, 7);
			emailNotificationsDao.deleteNotificationsBeforeDaysByStatus(NotificationStatus.SENT, date);
		} catch (Exception ex) {
			logger.error("error in method deleteNotificationsBeforeSevenDaysByScheduler,err  "
					+ ExceptionUtils.getStackTrace(ex));
		}
	}

	/**
	 * Send mail on demand.
	 *
	 * @param notificationId the id
	 * @return the boolean
	 */
	@Override
	public Boolean sendEMailOnDemand(Long notificationId) {
		try {
			EmailNotification mailsToSend = emailNotificationsDao.findByNotificationId(notificationId);
			logger.info("@sendEMailOnDemand mail sending started at: {} for : {}", new Date(), mailsToSend);
			if (mailsToSend != null) {
				List<Long> notificationIds = new ArrayList<>();
				notificationIds.add(mailsToSend.getId());
				IEmailNotificationService bean = getBean();
				bean.sendQueueMailAsync(mailsToSend);
			}
			return true;
		} catch (Exception e) {
			logger.error("@sendEMailOnDemand getting Exception as:{}", ExceptionUtils.getStackTrace(e));
			return false;
		}
	}

	/**
	 * Sent mail notification to user.
	 *
	 * @param recipientEmail the recipient email
	 * @param subject        the subject
	 * @param message        the message
	 * @throws RestException the rest exception
	 */
	@Override
	public void sentEMailNotificationToUser(String recipientEmail, String subject, String message) {
		logger.info("going to send mail with recipientEmail : {}, subject : {}, message : {}", recipientEmail, subject,
				message);
		if (StringUtils.isNotEmpty(recipientEmail)) {
			EmailNotification emailNotification = new EmailNotification();
			emailNotification.setCreationtime(new Date());
			emailNotification.setStatus(NotificationStatus.QUEUE);
			emailNotification.setSendTo(recipientEmail);
			emailNotification.setSubject(subject);
			emailNotification.setMessage(message);
			create(emailNotification);
		}
	}

}

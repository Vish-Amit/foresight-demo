package com.inn.foresight.core.mail.dao.impl;


import java.util.Date;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.mail.dao.IEmailNotificationDao;
import com.inn.foresight.core.mail.model.EmailNotification;
import com.inn.foresight.core.mail.utils.NotificationStatus;

/**
 * Dao .
 * @author innoeye
 */
 @Repository("EmailNotificationDaoImpl")
public class EmailNotificationDaoImpl extends HibernateGenericDao<Integer, EmailNotification> implements IEmailNotificationDao{

	/** The logger. */
	private Logger logger=LogManager.getLogger(EmailNotificationDaoImpl.class);
		
		/**
		 * Instantiates a new cellSite dao impl.
		 */
		public EmailNotificationDaoImpl() {
			super(EmailNotification.class);
		}
		
		/**
		 * Method to remove emailnotifications record.
		 * @throws DaoException 
		 *
		 */
		
		@Override
		public void delete(EmailNotification emailnotifications) {
		logger.info("Inside delete @class :"+this.getClass().getName()+" @Method :delete @Param: emailnotifications "+emailnotifications);
			super.delete(emailnotifications);
		}
		
		/**
		 * Method to remove emailnotifications record by primary key.
		 * @throws DaoException 
		 *
		 */
		
		@Override
		public void deleteByPk(Integer emailnotificationsPk){
		logger.info("Inside deleteByPk @class :"+this.getClass().getName()+" @Method :deleteByPk @Param: emailnotificationsPk "+emailnotificationsPk);
			super.deleteByPk(emailnotificationsPk);
		}
	
		/**
		 * Returns  the record of cellSite  finding by primary key .
		 * @throws DaoException 
		 *
		 */
		
		@Override
		public EmailNotification findByPk(Integer emailnotificationsPk){
		logger.info("Inside  @class :"+this.getClass().getName()+" @Method :findByPk @Param: emailnotificationsPk "+emailnotificationsPk);
			return super.findByPk(emailnotificationsPk);
		}

		/**
		 * Gets the queue data to schedule.
		 *
		 * @param status the status
		 * @param type the type
		 * @return the queue data to schedule
		 */
		@SuppressWarnings("unchecked")
		@Override
		public List<EmailNotification> getQueueDataToSchedule(NotificationStatus status , String type) {
			return getEntityManager().createNamedQuery("getQueueDataToSchedule")
									 .setParameter(ForesightConstants.STATUS, status)
									 .setParameter(ForesightConstants.NOTIFICATIONTYPE, type)
									 .getResultList();
		}
		
		/**
		 * Creates the notification.
		 *
		 */
		@Override
		@Transactional
		public void createNotification(String emailId ,String mailSubject ,String mailBody ,Date date ,NotificationStatus status, String senderMailId) {
			logger.info("Going to create notification");
			EmailNotification emailNotification = new EmailNotification();
			emailNotification.setSendTo(emailId);
			emailNotification.setSubject(mailSubject);	
			emailNotification.setMessage(mailBody);
			emailNotification.setStatus(status);
			emailNotification.setCreationtime(date);
			emailNotification.setFromEmail(senderMailId);
			try {
				update(emailNotification);
			} catch (DataAccessException | DaoException e) {
				logger.error(Utils.getStackTrace(e));
			}
		}
		
		/**
		 * Creates the notification with attachment.
		 *
		 */
		@Override
		@Transactional
		public void createNotificationWithAttachment(String emailId ,String bcc,String mailSubject ,String mailBody ,Date date ,NotificationStatus status,String attachment,String downloadFileNames, String senderMailId) {
			EmailNotification emailNotification = new EmailNotification();
			emailNotification.setSendTo(emailId);
			emailNotification.setSubject(mailSubject);	
			emailNotification.setMessage(mailBody);
			emailNotification.setStatus(status);
			emailNotification.setCreationtime(date);
			emailNotification.setAttachmentPath(attachment);
			emailNotification.setDownloadFileName(downloadFileNames);
			emailNotification.setBcc(bcc);
			emailNotification.setFromEmail(senderMailId);
			try {
				emailNotification=super.create(emailNotification);
			} catch (DaoException e) {
				logger.error(Utils.getStackTrace(e));
			}
			logger.info("createNotificationWithAttachment NotificationID= {} ",emailNotification.getId());
		}
		
		/**
		 * Creates the notification with out attachment and CC.
		 *
		 */
		@Override
		@Transactional
		public void createNotificationWithOutAttachmentAndCC(String emailId ,String cc,String mailSubject ,String mailBody ,Date date ,NotificationStatus status) {
			EmailNotification emailNotification = new EmailNotification();
			emailNotification.setSendTo(emailId);
			emailNotification.setSubject(mailSubject);	
			emailNotification.setMessage(mailBody);
			emailNotification.setStatus(status);
			emailNotification.setCreationtime(date);
			emailNotification.setCc(cc);
			try {
				emailNotification=super.create(emailNotification);
			} catch (DaoException e) {
				logger.error(Utils.getStackTrace(e));
			}
			logger.info("createNotificationWithOutAttachmentAndCC NotificationID={} ",emailNotification.getId());
		}
		
		/**
		 * Gets the queue data to mail.
		 *
		 */
		@SuppressWarnings("unchecked")
		@Override
		public List<EmailNotification> getQueueDataToMail(NotificationStatus status) {
			return getEntityManager().createNamedQuery("getQueueDataToMail")
									 .setParameter(ForesightConstants.STATUS, status)
									 .getResultList();
	}

	/**
	 * Update status by id.
	 *
	 * @param ids the ids
	 * @param status the status
	 */
	@Override
	public void updateStatusById(List<Long> ids,NotificationStatus status) {
		try {
			Query query=getEntityManager().createNamedQuery("updateStatusById");
			query.setParameter(ForesightConstants.IDS, ids);
			query.setParameter(ForesightConstants.STATUS, status);
			int updateCount= query.executeUpdate();	
			logger.info("@updateStatusById: status={}, updateCount={} ",status,updateCount);
		}catch(Exception e){
			logger.info("unable to update the status={} for Ids={} ",status,ids);
		}
	}

	/**
	 * Delete notifications before days by status.
	 *
	 */
	@Override
	public void deleteNotificationsBeforeDaysByStatus(NotificationStatus status,Date date){
		logger.info("inside method deleteNotificationsBeforeDaysByStatus ");
		try{
			Query query=getEntityManager().createNamedQuery("deleteNotificationsBeforeDaysByStatus");
			query.setParameter(ForesightConstants.STATUS, status);
			query.setParameter(ForesightConstants.DATE, date, TemporalType.DATE);
			int updateCount=query.executeUpdate() ;
			logger.info("total sent notifications are deleted {}",updateCount);
		}catch(Exception ex){
			logger.error(Utils.getStackTrace(ex));
			logger.error("unable to delete sent notification err={}",ex.getMessage());
		}
	}
	
	/**
	 * Find by notification id.
	 *
	 */
	@Override
	public EmailNotification findByNotificationId(Long notificationId) {
		
		Query query=getEntityManager().createNamedQuery("findByNotificationId");
		query.setParameter(ForesightConstants.ID, notificationId);
		return (EmailNotification) query.getSingleResult();
	}
	
	/**
	 * Creates the notification with attachment and CC.
	 *
	 */
	@Transactional
	@Override
	public void createNotificationWithAttachmentAndCC(String emailId ,String bcc,String cc,String mailSubject ,String mailBody ,Date date ,NotificationStatus status,String attachment,String downloadFileNames) {
		EmailNotification emailNotification = new EmailNotification();
		emailNotification.setSendTo(emailId);
		emailNotification.setSubject(mailSubject);	
		emailNotification.setMessage(mailBody);
		emailNotification.setStatus(status);
		emailNotification.setCreationtime(date);
		emailNotification.setAttachmentPath(attachment);
		emailNotification.setDownloadFileName(downloadFileNames);
		emailNotification.setBcc(bcc);
		emailNotification.setCc(cc);
		try {
			emailNotification=super.create(emailNotification);
		} catch (DaoException e) {
			logger.error(Utils.getStackTrace(e));
		}
		logger.info("createNotificationWithAttachmentAndCC NotificationID={} ",emailNotification.getId());
	}
	
	/**
	 * Creates the notification with out attachment and CC and BCC.
	 *
	 */
	@Transactional
	@Override
	public void createNotificationWithOutAttachmentAndCCAndBCC(String emailId ,String bcc,String cc,String mailSubject ,String mailBody ,Date date ,NotificationStatus status) {
		EmailNotification emailNotification = new EmailNotification();
		emailNotification.setSendTo(emailId);
		emailNotification.setSubject(mailSubject);	
		emailNotification.setMessage(mailBody);
		emailNotification.setStatus(status);
		emailNotification.setCreationtime(date);
		emailNotification.setCc(cc);
		emailNotification.setBcc(bcc);
		try {
			emailNotification=super.create(emailNotification);
		} catch (DaoException e) {
			logger.error(Utils.getStackTrace(e));
		}
		logger.info("createNotificationWithOutAttachmentAndCCAndBCC NotificationID={} ",emailNotification.getId());
	}

	/**
	 * Creates the notification with inline image and attachment.
	 * this method is used for inline image notification creation
	 *
	 */
	@Override
	@Transactional
	public void createNotificationWithInlineImageAndAttachment(String emailId ,String cc,String bcc,String mailSubject ,String mailBody ,Date date ,NotificationStatus status,
			String attachment,String downloadFileNames,String inlineImageId,String inlineImagePath) {
		logger.info("Inside Method createNotificationWithInlineImageAndAttachment {} " ,inlineImagePath);
		EmailNotification emailNotification = new EmailNotification();
		emailNotification.setSendTo(emailId);
		emailNotification.setSubject(mailSubject);	
		emailNotification.setMessage(mailBody);
		emailNotification.setStatus(status);
		emailNotification.setCreationtime(date);
		emailNotification.setAttachmentPath(attachment);
		emailNotification.setDownloadFileName(downloadFileNames);
		emailNotification.setCc(cc);
		emailNotification.setBcc(bcc);
		emailNotification.setInlineImageId(inlineImageId);
		emailNotification.setInlineImagePath(inlineImagePath);
		try {
			emailNotification=super.create(emailNotification);
		} catch (DaoException e) {
			logger.error(Utils.getStackTrace(e));
		}
		logger.info("createNotificationWithAttachment NotificationID={} ",emailNotification.getId());
	}
}

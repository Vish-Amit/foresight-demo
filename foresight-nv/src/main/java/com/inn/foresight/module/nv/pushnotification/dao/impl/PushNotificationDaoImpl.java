package com.inn.foresight.module.nv.pushnotification.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.module.nv.pushnotification.constants.PushNotificationConstants;
import com.inn.foresight.module.nv.pushnotification.dao.IPushNotificationDao;
import com.inn.foresight.module.nv.pushnotification.model.PushNotification;
import com.inn.foresight.module.nv.pushnotification.notificationfile.model.NotificationFile;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;

/** The Class PushNotificationDaoImpl. */
@Repository("PushNotificationDaoImpl")
public class PushNotificationDaoImpl extends HibernateGenericDao<Integer, PushNotification>
		implements IPushNotificationDao {

	/** The logger. */
	private Logger logger = LogManager.getLogger(PushNotificationDaoImpl.class);

	/** Instantiates a new push notification dao impl. */
	public PushNotificationDaoImpl() {
		super(PushNotification.class);
	}

	/**
	 * Creates the notification.
	 *
	 * @param woNotification the wo notification
	 * @return the push notification
	 * @throws DaoException the dao exception
	 */
	public PushNotification createNotification(PushNotification pushNotification) {
		try {
			return super.create(pushNotification);
		} catch (Exception e) {
			logger.error(NVWorkorderConstant.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new DaoException(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Find all new and failed notification.
	 *
	 * @return the list
	 * @throws DaoException the dao exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PushNotification> findAllNewAndFailedNotification() {
		try {
			return getEntityManager().createNamedQuery(PushNotificationConstants.GET_ALL_NEW_AND_FAILED_NOTIFICATION)
					.getResultList();
		} catch (Exception e) {
			logger.error(NVWorkorderConstant.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new DaoException(ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * Gets the notification by file id.
	 *
	 * @param notificationfile the notificationfile
	 * @param llimit the llimit
	 * @param ulimit the ulimit
	 * @return the notification by file id
	 * @throws DaoException the dao exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PushNotification> getNotificationByFileId(NotificationFile notificationFile, Integer llimit,
			Integer ulimit) {
		Query query = null;
		try {
			
			query = getEntityManager()
					.createNamedQuery(PushNotificationConstants.GET_ALL_NOTIFICATION_BY_NOTIFICATIONFILE)
					.setParameter(PushNotificationConstants.NOTIFICATION_FILE_PLACE_HOLDER, notificationFile);
		} catch (Exception e) {
			logger.error(NVWorkorderConstant.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new DaoException(ExceptionUtils.getStackTrace(e));
		}
		
		
		if (PushNotificationConstants.MIN_UPPER_LIMIT.equals(ulimit)) {
			query.setFirstResult(llimit);
			query.setMaxResults(ulimit);
		}
		return query.getResultList();
	}

	/**
	 * Gets the feed back notification file detail list count.
	 *
	 * @param notificationfile the notificationfile
	 * @return the feed back notification file detail list count
	 * @throws DaoException the dao exception
	 */
	@Override
	public Long getFeedBackNotificationFileDetailListCount(NotificationFile notificationFile) {
		Query query = null;
		try {
			query = getEntityManager()
					.createNamedQuery(PushNotificationConstants.GET_ALL_NOTIFICATION_BY_NOTIFICATIONFILE_COUNT)
					.setParameter(PushNotificationConstants.NOTIFICATION_FILE_PLACE_HOLDER, notificationFile);
		} catch (Exception e) {
			logger.error(NVWorkorderConstant.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new DaoException(ExceptionUtils.getStackTrace(e));
		}
		
		return (Long)query.getSingleResult();
	}
}
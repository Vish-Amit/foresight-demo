package com.inn.foresight.module.nv.pushnotification.dao;

import java.util.List;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.module.nv.pushnotification.model.PushNotification;
import com.inn.foresight.module.nv.pushnotification.notificationfile.model.NotificationFile;

/** The Interface IPushNotificationDao. */
public interface IPushNotificationDao extends IGenericDao<Integer, PushNotification> {
	
	/**
	 * Creates the notification.
	 *
	 * @param woNotification the wo notification
	 * @return the push notification
	 * @throws DaoException the dao exception
	 */
	PushNotification createNotification(PushNotification woNotification);

	/**
	 * Find all new and failed notification.
	 *
	 * @return the list
	 * @throws DaoException the dao exception
	 */
	List<PushNotification> findAllNewAndFailedNotification();

	/**
	 * Gets the notification by file id.
	 *
	 * @param notificationfile the notificationfile
	 * @param llimit the llimit
	 * @param ulimit the ulimit
	 * @return the notification by file id
	 * @throws DaoException the dao exception
	 */
	List<PushNotification> getNotificationByFileId(NotificationFile notificationfile, Integer llimit, Integer ulimit);

	/**
	 * Gets the feed back notification file detail list count.
	 *
	 * @param notificationfile the notificationfile
	 * @return the feed back notification file detail list count
	 * @throws DaoException the dao exception
	 */
	Long getFeedBackNotificationFileDetailListCount(NotificationFile notificationfile);
}

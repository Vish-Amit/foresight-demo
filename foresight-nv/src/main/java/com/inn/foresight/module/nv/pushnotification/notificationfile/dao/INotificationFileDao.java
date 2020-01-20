package com.inn.foresight.module.nv.pushnotification.notificationfile.dao;

import java.util.List;

import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.module.nv.pushnotification.notificationfile.model.NotificationFile;

/** The Interface INotificationFileDao. */
public interface INotificationFileDao {
	
	/**
	 * Insert notification file.
	 *
	 * @param notificationFiles the notification files
	 * @return the notification file
	 * @throws DaoException the dao exception
	 */
	NotificationFile insertNotificationFile(NotificationFile notificationFiles);

	/**
	 * Gets the all notification file list.
	 *
	 * @param lLimit the l limit
	 * @param uLimit the u limit
	 * @return the all notification file list
	 * @throws DaoException the dao exception
	 */
	List<NotificationFile> getAllNotificationFileList(Integer lLimit, Integer uLimit);

	/**
	 * Delete notification file by id.
	 *
	 * @param id the id
	 * @return true, if successful
	 * @throws DaoException the dao exception
	 */
	boolean deleteNotificationFileById(Integer id);

	/**
	 * Find by PK.
	 *
	 * @param id the id
	 * @return the notification file
	 * @throws DaoException the dao exception
	 */
	NotificationFile findByPK(Integer id);

	/**
	 * Update.
	 *
	 * @param notificationFile the notification file
	 * @return the notification file
	 * @throws DaoException the dao exception
	 */
	NotificationFile update(NotificationFile notificationFile);

	/**
	 * Gets the feedback notification file count.
	 *
	 * @return the feedback notification file count
	 * @throws DaoException the dao exception
	 */
	Long getFeedbackNotificationFileCount();
	
	

}

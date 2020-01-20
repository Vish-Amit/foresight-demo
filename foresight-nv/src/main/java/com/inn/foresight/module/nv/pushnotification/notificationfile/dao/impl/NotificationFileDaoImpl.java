package com.inn.foresight.module.nv.pushnotification.notificationfile.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.module.nv.pushnotification.constants.PushNotificationConstants;
import com.inn.foresight.module.nv.pushnotification.notificationfile.dao.INotificationFileDao;
import com.inn.foresight.module.nv.pushnotification.notificationfile.model.NotificationFile;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;

/** The Class NotificationFileDaoImpl. */
@Repository("NotificationFileDaoImpl")
public class NotificationFileDaoImpl extends HibernateGenericDao<Integer, NotificationFile>
		implements INotificationFileDao {
	/** The logger. */
	private Logger logger = LogManager.getLogger(NotificationFileDaoImpl.class);

	/** Instantiates a new notification file dao impl. */
	public NotificationFileDaoImpl() {
		super(NotificationFile.class);
	}


	/**
	 * Insert notification file.
	 *
	 * @param notificationFiles the notification files
	 * @return the notification file
	 * @throws DaoException the dao exception
	 */
	@Override
	public NotificationFile insertNotificationFile(NotificationFile notificationFiles) {
		try {
			return super.create(notificationFiles);
		} catch (Exception e) {
			logger.error(NVWorkorderConstant.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new DaoException(ExceptionUtils.getMessage(e));
		}
	}

	/**
	 * Gets the all notification file list.
	 *
	 * @param lLimit the l limit
	 * @param uLimit the u limit
	 * @return the all notification file list
	 * @throws DaoException the dao exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<NotificationFile> getAllNotificationFileList(Integer lLimit, Integer uLimit) {
		Query query = null;
		try {
			query = getEntityManager().createNamedQuery(PushNotificationConstants.GET_ALL_NOTIFICATION_FILE_LIST);

		} catch (Exception e) {
			logger.error(NVWorkorderConstant.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new DaoException(ExceptionUtils.getMessage(e));
		}
		query.setFirstResult(lLimit);
		query.setMaxResults(uLimit);
		return query.getResultList();
	}

	/**
	 * Delete notification file by id.
	 *
	 * @param id the id
	 * @return true, if successful
	 * @throws DaoException the dao exception
	 */
	@Override
	public boolean deleteNotificationFileById(Integer id) {
		try {
			super.deleteByPk(id);
			return true;
		} catch (DaoException e) {
			logger.error(NVWorkorderConstant.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new DaoException(ExceptionUtils.getMessage(e));
		}
	}

	/**
	 * Find by PK.
	 *
	 * @param id the id
	 * @return the notification file
	 * @throws DaoException the dao exception
	 */
	@Override
	public NotificationFile findByPK(Integer id) {
		try {
			return super.findByPk(id);
		} catch (DaoException e) {
			logger.error(NVWorkorderConstant.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new DaoException(ExceptionUtils.getMessage(e));
		}
	}

	/**
	 * Update.
	 *
	 * @param notificationFile the notification file
	 * @return the notification file
	 * @throws DaoException the dao exception
	 */
	@Override
	public NotificationFile update(NotificationFile anEntity) {
		try {
			return super.update(anEntity);
		} catch (DaoException e) {
			logger.error(NVWorkorderConstant.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new DaoException(ExceptionUtils.getMessage(e));
		}
	}

	/**
	 * Gets the feedback notification file count.
	 *
	 * @return the feedback notification file count
	 * @throws DaoException the dao exception
	 */
	@Override
	public Long getFeedbackNotificationFileCount() {
		Query query = null;
		try {
			query = getEntityManager().createNamedQuery(PushNotificationConstants.GET_FEEDBACK_NOTIFICATION_FILE_COUNT);
		} catch (Exception e) {
			logger.error(NVWorkorderConstant.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new DaoException(ExceptionUtils.getMessage(e));
		}
		return (Long)query.getSingleResult();
	}

}

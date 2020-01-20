package com.inn.foresight.module.nv.pushnotification.notificationfile.service;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.module.nv.feedback.wrapper.FeedbackFileWrapper;
import com.inn.foresight.module.nv.pushnotification.notificationfile.model.NotificationFile;

/** The Interface INotificationFileService. */
public interface INotificationFileService {
	
	/**
	 * Insert notification file.
	 *
	 * @param notificationFiles the notification files
	 * @return the notification file
	 * @throws RestException the rest exception
	 */
	NotificationFile insertNotificationFile(NotificationFile notificationFiles);

	/**
	 * Find by pk.
	 *
	 * @param id the id
	 * @return the notification file
	 * @throws RestException the rest exception
	 */
	NotificationFile findByPk(Integer id);

	/**
	 * Gets the all notification file list.
	 *
	 * @param lLimit the l limit
	 * @param uLimit the u limit
	 * @return the all notification file list
	 * @throws RestException the rest exception
	 */
	List<NotificationFile> getAllNotificationFileList(Integer lLimit, Integer uLimit);

	/**
	 * Delete notification file by id.
	 *
	 * @param ids the ids
	 * @return true, if successful
	 * @throws RestException the rest exception
	 */
	boolean deleteNotificationFileById(List<Integer> ids);

	/**
	 * Upload feedback notification file.
	 *
	 * @param fileName the file name
	 * @param date the date
	 * @param inputFile the input file
	 * @return the string
	 * @throws RestException the rest exception
	 */
	String uploadFeedbackNotificationFile(String fileName, Date date, InputStream inputFile);

	/**
	 * Download feed back file by file id.
	 *
	 * @param id the id
	 * @return the string
	 * @throws RestException the rest exception
	 */
	String downloadFeedBackFileByFileId(Integer id);

	/**
	 * Download feed back file template.
	 *
	 * @return the string
	 * @throws RestException the rest exception
	 */
	String downloadFeedBackFileTemplate();

	/**
	 * Gets the feed back file detail by id.
	 *
	 * @param id the id
	 * @param llimit the llimit
	 * @param ulimit the ulimit
	 * @return the feed back file detail by id
	 * @throws RestException the rest exception
	 */
	List<FeedbackFileWrapper> getFeedBackFileDetailById(Integer id, Integer llimit, Integer ulimit);

	/**
	 * Gets the feedback notification file count.
	 *
	 * @return the feedback notification file count
	 * @throws RestException the rest exception
	 */
	Long getFeedbackNotificationFileCount();

	/**
	 * Gets the feed back notification file detail list count.
	 *
	 * @param id the id
	 * @return the feed back notification file detail list count
	 * @throws RestException the rest exception
	 */
	Long getFeedBackNotificationFileDetailListCount(Integer id);
}

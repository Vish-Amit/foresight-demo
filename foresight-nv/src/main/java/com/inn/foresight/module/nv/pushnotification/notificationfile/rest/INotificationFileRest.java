package com.inn.foresight.module.nv.pushnotification.notificationfile.rest;

import java.io.InputStream;
import java.util.List;

import javax.ws.rs.core.Response;

import com.inn.core.generic.exceptions.application.RestException;

/** The Interface INotificationFileRest. */
public interface INotificationFileRest {

	/**
	 * Gets the all notification file list.
	 *
	 * @param llimit the llimit
	 * @param ulimit the ulimit
	 * @return the all notification file list
	 * @throws RestException the rest exception
	 */
	Response getAllNotificationFileList(Integer llimit, Integer ulimit);

	/**
	 * Download feed back notification file by id.
	 *
	 * @param id the id
	 * @return the response
	 * @throws RestException the rest exception
	 */
	Response downloadFeedBackNotificationFileById(Integer id);

	/**
	 * Download feed back notification file template.
	 *
	 * @return the response
	 * @throws RestException the rest exception
	 */
	Response downloadFeedBackNotificationFileTemplate();

	/**
	 * Gets the feed back notification file detail by id.
	 *
	 * @param id the id
	 * @param llimit the llimit
	 * @param ulimit the ulimit
	 * @return the feed back notification file detail by id
	 * @throws RestException the rest exception
	 */
	Response getFeedBackNotificationFileDetailById(Integer id,Integer llimit, Integer ulimit);

	/**
	 * Delete feed back notification files.
	 *
	 * @param ids the ids
	 * @return the string
	 * @throws RestException the rest exception
	 */
	String deleteFeedBackNotificationFiles(List<Integer> ids);

	/**
	 * Upload feedback notification file.
	 *
	 * @param fileName the file name
	 * @param scheduledTime the scheduled time
	 * @param inputFile the input file
	 * @return the string
	 * @throws RestException the rest exception
	 */
	String uploadFeedbackNotificationFile(String fileName, Long scheduledTime, InputStream inputFile);
	
	/**
	 * Gets the feedback notification file count.
	 *
	 * @return the feedback notification file count
	 * @throws RestException the rest exception
	 */
	Response getFeedbackNotificationFileCount();

	/**
	 * Gets the feed back notification file detail list count.
	 *
	 * @param id the id
	 * @return the feed back notification file detail list count
	 * @throws RestException the rest exception
	 */
	Response getFeedBackNotificationFileDetailListCount(Integer id);
}

package com.inn.foresight.module.nv.pushnotification.notificationfile.rest.impl;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.module.nv.pushnotification.notificationfile.rest.INotificationFileRest;
import com.inn.foresight.module.nv.pushnotification.notificationfile.service.INotificationFileService;

@Path("/NotificationFile")
@Service("NotificationFileRestImpl")
public class NotificationFileRestImpl implements INotificationFileRest {
	/** The logger. */
	private Logger logger = LogManager.getLogger(NotificationFileRestImpl.class);

	@Autowired
	private INotificationFileService iNotificationFileService;

	/** 
	 * @return List of notification files
	 * @throws RestException
	 */
	@GET
	@Path("getAllNotificationFileList")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response getAllNotificationFileList(@QueryParam("llimit") Integer llimit,
			@QueryParam("noOfRecord") Integer ulimit) {
		try {
			logger.info("Going to getAllNotificationFileList llimit :{} ,noOfRecord {}", llimit, ulimit);
			return Response	.ok(iNotificationFileService.getAllNotificationFileList(llimit, ulimit))
							.build();
		} catch (RestException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}

	/**
	 * Download feed back file by id.
	 *
	 * @param id
	 *            the id
	 * @return the response
	 * @throws RestException
	 *             the rest exception
	 */
	@GET
	@Path("downloadFeedBackNotificationFileById/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response downloadFeedBackNotificationFileById(@PathParam("id") Integer id) {
		logger.info("Going to downloadFeedBackNotificationFileById id :{} ", id);

		try {
			Response.ResponseBuilder builder = Response.ok(iNotificationFileService	.downloadFeedBackFileByFileId(id)
																					.getBytes());
			return builder.build();
		} catch (RestException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}

	/**
	 * Download feed back file template by id.
	 *
	 * @return the response
	 * @throws RestException
	 *             the rest exception
	 */
	@GET
	@Path("downloadFeedBackNotificationFileTemplate")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response downloadFeedBackNotificationFileTemplate() {
		logger.info("Going to downloadFeedBackNotificationFileTemplate  ");
		try {
			Response.ResponseBuilder builder = Response.ok(iNotificationFileService	.downloadFeedBackFileTemplate()
																					.getBytes());
			return builder.build();
		} catch (RestException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtils.getMessage(e));
		}
	}

	/**
	 * Get feed back file Detail by id.
	 *
	 * @param id
	 *            the id
	 * @return the response
	 * @throws RestException
	 *             the rest exception
	 */
	@GET
	@Path("getFeedBackNotificationFileDetailById/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response getFeedBackNotificationFileDetailById(@PathParam("id") Integer id,
			@QueryParam("llimit") Integer llimit, @QueryParam("noOfRecord") Integer ulimit) {
		logger.info("Going to getFeedBackNotificationFileDetailById llimit :{} ,ulimit {},id {}", llimit,
				ulimit, id);
		try {
			return Response	.ok(iNotificationFileService.getFeedBackFileDetailById(id, llimit, ulimit))
							.build();
		} catch (RestException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}

	/**
	 * Upload feedback notification.
	 *
	 * @param fileName
	 *            the file name
	 * @param scheduledTime
	 *            the scheduled time
	 * @param inputFile
	 *            the input file
	 * @return the string
	 * @throws RestException
	 *             the rest exception
	 */
	@POST
	@Path("uploadFeedbackNotificationFile")
	@Consumes("multipart/form-data")
	@Override
	public String uploadFeedbackNotificationFile(@Multipart(value = "filename") String fileName,
			@Multipart(value = "scheduledTime") Long scheduledTime, @Multipart(value = "file") InputStream inputFile) {

		logger.info("Going to uploadFeedbackNotificationFile Filename {},scheduledTime{}", fileName, scheduledTime);
		if (inputFile == null) {
			throw new RestException("Invalid File ");
		}
		if (scheduledTime == null)
			throw new RestException("ScheduledTime Required");
		logger.info("inside @Class:" + this	.getClass()
											.getName()
				+ " @uploadFeedbackNotification");
		return iNotificationFileService.uploadFeedbackNotificationFile(fileName, new Date(scheduledTime), inputFile);
	}

	/**
	 * Delete feed back notifications by ids.
	 *
	 * @param ids
	 *            the ids
	 * @return the string
	 * @throws RestException
	 *             the rest exception
	 */

	@POST
	@Path("deleteFeedBackNotificationFiles")
	public String deleteFeedBackNotificationFiles(List<Integer> ids) {
		logger.info("Going to deleteFeedBackNotificationFiles ids {}", ids);
		String message = null;
		try {
			boolean isDeleted = iNotificationFileService.deleteNotificationFileById(ids);
			if (isDeleted) {
				message = ForesightConstants.SUCCESS_JSON;
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtils.getMessage(e));
		}
		return message;
	}

	/** 
	 * @return count of notification files
	 * @throws RestException
	 */
	@GET
	@Path("getFeedbackNotificationFileCount")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response getFeedbackNotificationFileCount() {
		try {
			logger.info("Going to getFeedbackNotificationFileCount ");
			return Response	.ok(iNotificationFileService.getFeedbackNotificationFileCount())
							.build();
		} catch (RestException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}

	/**
	 * Get feed back file Detail count by id.
	 *
	 * @param id
	 *            the id
	 * @return the response
	 * @throws RestException
	 *             the rest exception
	 */
	@GET
	@Path("getFeedBackNotificationFileDetailListCount/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response getFeedBackNotificationFileDetailListCount(@PathParam("id") Integer id) {
		try {
			logger.info("Going to getFeedBackNotificationFileDetailListCount id {} ",id);
			return Response	.ok(iNotificationFileService.getFeedBackNotificationFileDetailListCount(id))
							.build();
		} catch (RestException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}

}

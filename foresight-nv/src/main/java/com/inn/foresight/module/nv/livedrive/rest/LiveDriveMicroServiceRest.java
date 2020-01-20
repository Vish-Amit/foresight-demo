package com.inn.foresight.module.nv.livedrive.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.module.nv.livedrive.constants.LiveDriveConstant;
import com.inn.foresight.module.nv.livedrive.service.ILiveDriveService;
import com.inn.foresight.module.nv.livedrive.utils.LiveDriveUtil;
import com.inn.foresight.module.nv.livedrive.wrapper.TrackSpanSummaryWrapper;

/** The Class LiveDriveRest. */
@Path("/ms/liveDrive")
@Produces(MediaType.APPLICATION_JSON)
public class LiveDriveMicroServiceRest {

	/** The logger. */
	private static final Logger logger = LogManager.getLogger(LiveDriveMicroServiceRest.class);

	/** The live drive service. */
	@Autowired
	private ILiveDriveService liveDriveService;


	/**
	 * Find trips by imei and tripid.
	 *
	 * @param taskId
	 *            the task id
	 * @param limit
	 *            the limit
	 * @return the response
	 * @throws RestException
	 *             the rest exception
	 */
	@GET
	@Path("/getLiveDriveResultDataByTaskId")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findTripsByImeiAndTripid(@QueryParam(LiveDriveConstant.DRIVEID) Integer taskId,
			@QueryParam(LiveDriveConstant.LIMIT) Integer limit) {
		logger.info("Inside @class: LiveDriveRest Method findTripsByImeiAndTripid for taskid {} ", taskId);
		TrackSpanSummaryWrapper trackSpanSummaryWrapper = null;
		try {
			if (taskId != null && taskId >= 0) {
				trackSpanSummaryWrapper = liveDriveService.getLiveDriveDataByTaskId(taskId, limit);
				if (trackSpanSummaryWrapper != null && trackSpanSummaryWrapper.getTrackSpanWrappers() != null
						&& !trackSpanSummaryWrapper.getTrackSpanWrappers().isEmpty()) {
					return Response.ok(trackSpanSummaryWrapper).build();
				} else {
					return Response.ok(
							LiveDriveUtil.createIntoJson(LiveDriveConstant.DATA_IS_NOT_AVAILABLE_FOR_THIS_ID, taskId))
							.build();
				}
			} else {
				return Response.ok(LiveDriveConstant.TASKID_SHOULD_NOT_BE_NULL_AND_LESS_THAN_ZERO).build();
			}
		} catch (Exception e) {
			logger.error("Error occured {}", ExceptionUtils.getStackTrace(e));
		}
		return Response.ok(ForesightConstants.FAILURE_JSON).build();
	}
}

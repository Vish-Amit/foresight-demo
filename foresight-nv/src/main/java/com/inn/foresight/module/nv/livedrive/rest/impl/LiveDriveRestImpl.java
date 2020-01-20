package com.inn.foresight.module.nv.livedrive.rest.impl;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.module.nv.livedrive.constants.LiveDriveConstant;
import com.inn.foresight.module.nv.livedrive.service.ILiveDriveService;

/**
 * @author Innoeye
 */
@Path("/liveDrive")
@Service("LiveDriveRestImpl")
public class LiveDriveRestImpl {

	private static Logger logger = LogManager.getLogger(LiveDriveRestImpl.class);

	@Autowired
	ILiveDriveService iLiveDriveService;
	
	/**
	 * Method returns test drive data whose trackid is greater than given trackid
	 * with pagination.
	 *
	 * @param driveid
	 *            the driveid
	 * @param trackid
	 *            the trackid
	 * @return csv file
	 * @throws RestException
	 *             the rest exception
	 */

	@GET
	@Path("getLiveDriveData")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLiveDriveDataFromHbase(@QueryParam("driveid") Integer driveid,
			@QueryParam("trackid") Integer trackid) {

		logger.info("In findDriveTestLocationData: driveid : {} trackid : {}", driveid, trackid);
		if (driveid == null || trackid == null) {
			return Response.ok(LiveDriveConstant.INVALID_PARAMETER).build();
		}

		String liveDriveCsv = iLiveDriveService.findTripsByImeiAndTripid(driveid, trackid, LiveDriveConstant.ONGOING);
		logger.info("Final Response Found: {}",!liveDriveCsv.isEmpty());
		return Response.ok(liveDriveCsv).build();
	}

	/**
	 * Get summary of live drive and device info.
	 * 
	 * @param driveid
	 * @param trackid
	 * @return
	 * @throws RestException
	 */
	@GET
	@Path("liveDriveSummary")
	@Produces(MediaType.APPLICATION_JSON)
	public Response liveDriveSummary(@QueryParam("driveid") Integer driveid, @QueryParam("trackid") Integer trackid) {

		logger.info("In liveDriveSummary: driveid : {} trackid : {}", driveid, trackid);
		if (driveid == null || trackid == null) {
			throw new RestException(LiveDriveConstant.INVALID_PARAMETER);
		}

		String liveDriveCsv = iLiveDriveService.findTripsByImeiAndTripid(driveid, trackid, LiveDriveConstant.SUMMARY);
		logger.info("liveDriveCsv : {}", liveDriveCsv);

		return Response.ok(liveDriveCsv).build();
	}
}

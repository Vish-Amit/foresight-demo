package com.inn.foresight.module.nv.inbuilding.result.rest.impl;

import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.commons.lang.StringUtils;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.constants.InBuildingConstants;
import com.inn.foresight.module.nv.inbuilding.result.rest.IInBuildingResultRest;
import com.inn.foresight.module.nv.inbuilding.result.service.IInBuildingResultService;


/** The Class InBuildingResultImpl. */
@Path("/NVInBuildingResult")
@Service("InBuildingResultRestImpl")
public class InBuildingResultRestImpl implements IInBuildingResultRest {

	/** The logger. */
	private Logger logger = LogManager.getLogger(InBuildingResultRestImpl.class);

	@Autowired
	IInBuildingResultService iInBuildingResultService;

	/**
	 * Download floor plan.
	 *
	 * @param unitId
	 *            the unit id
	 * @param Technology
	 *            the technology
	 * @return the response
	 */
	@GET
	@Path("/downloadFloorPlan/{unitId}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Override
	public Response downloadFloorPlan(@PathParam("unitId") Integer unitId) {
		Response response;
		try {
			if (Utils.hasValidValue(String.valueOf(unitId))) {
				response = iInBuildingResultService.downloadFloorPlan(unitId);
				if (response == null) {
					response = Response.ok(InBuildingConstants.FAILURE_JSON).build();
				}
			} else {
				response = Response.ok(InBuildingConstants.INVALID_PARAMETER_JSON).build();
			}
		} catch (RestException e) {
			logger.error("Error while downloading floor plan image: {}", ExceptionUtils.getMessage(e));
			response = Response.ok(InBuildingConstants.EXCEPTION_SOMETHING_WENT_WRONG).build();
		}
		return response;
	}

	@POST
	@Path("/uploadFloorPlanData/{unitId}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Override
	public Response uploadFloorPlanData(@PathParam("unitId") Integer unitId,
			@Multipart(value = ForesightConstants.FILE) InputStream inputStream,
			@Multipart(value = ForesightConstants.FILENAME) String fileName) {
		Response response = Response.ok(InBuildingConstants.EXCEPTION_SOMETHING_WENT_WRONG).build();
		try {
			if (!Utils.isNotNull(unitId) || StringUtils.isBlank(fileName) || inputStream == null) {
				response = Response.ok(InBuildingConstants.INVALID_PARAMETER_JSON).build();
			} else {
				response = Response.ok(iInBuildingResultService.uploadFloorPlanData(unitId, inputStream, fileName))
						.build();
			}
		} catch (RestException e) {
			logger.error("Error while uploading floor plan data: {}", ExceptionUtils.getMessage(e));
			response = Response.ok(InBuildingConstants.EXCEPTION_SOMETHING_WENT_WRONG).build();
		}
		return response;
	}

	@GET
	@Path("/downloadFloorPlanData/{unitId}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Override
	public Response downloadFloorPlanData(@PathParam("unitId") Integer unitId) {
		Response response = Response.ok(InBuildingConstants.INVALID_PARAMETER_JSON).build();
		try {
			if (Utils.isNotNull(unitId)) {
				Response.ResponseBuilder builder = Response.status(200);
				response = builder.entity(iInBuildingResultService.downloadFloorPlanData(unitId))
						.header("Content-Type", MediaType.APPLICATION_OCTET_STREAM)
						.header("Content-Disposition",
								"attachment;filename=" + InBuildingConstants.FLOOR_PLAN_FILE_NAME + unitId
										+ InBuildingConstants.FLOOR_PLAN_FILE_SEPERATOR + System.currentTimeMillis()
										+ InBuildingConstants.FLOOR_PLAN_FILE_EXTENSION)
						.build();
			}
		} catch (RestException e) {
			logger.error("Error while preparing download for floor plan data: {}", ExceptionUtils.getMessage(e));
			response = Response.ok(InBuildingConstants.EXCEPTION_SOMETHING_WENT_WRONG).build();
		}
		return response;
	}
	// /**
	// * Sync in building result file.
	// *
	// * @param inputFile
	// * the input file
	// * @param fileName
	// * the file name
	// * @return the response
	// */
	// @Override
	// @POST
	// @Path("/syncInBuildingResultFile")
	// @Consumes(MediaType.MULTIPART_FORM_DATA)
	// public Response syncInBuildingResultFile(@Multipart(value =
	// ForesightConstants.FILE) InputStream inputFile,
	// @Multipart(value = ForesightConstants.FILENAME) String fileName) throws
	// RestException {
	// if (inputFile == null || fileName == null) {
	// return Response.ok(InBuildingConstants.INVALID_PARAMETER_JSON).build();
	// }
	// try {
	// return
	// Response.ok(iInBuildingResultService.syncInBuildingResultFile(inputFile,
	// fileName)).build();
	// } catch (RestException e) {
	// return Response.ok(e.getMessage()).build();
	// } catch (Exception e) {
	// return Response.ok(e.getMessage()).build();
	// }
	// }

	// /**
	// * Upload floor plan.
	// *
	// * @param unitId
	// * the unit id
	// * @param technology
	// * the technology
	// * @return the response
	// * @throws RestException
	// * the rest exception
	// */
	// @POST
	// @Path("/uploadFloorPlan/{unitId}/{technology}")
	// @Consumes(MediaType.MULTIPART_FORM_DATA)
	// @Override
	// public Response uploadFloorPlan(@PathParam("unitId") Integer unitId,
	// @PathParam("technology") String technology,
	// @Multipart(value = ForesightConstants.FILE) InputStream inputStream,
	// @Multipart(value = ForesightConstants.FILENAME) String fileName) {
	// if (inputStream == null || fileName == null) {
	// return Response.ok(InBuildingConstants.INVALID_PARAMETER_JSON).build();
	// }
	// try {
	// return Response.ok(iInBuildingResultService.uploadFloorPlan(unitId,
	// technology, inputStream, fileName))
	// .build();
	// } catch (RestException e) {
	// e.printStackTrace();
	// return Response .ok(e.getMessage())
	// .build();
	// }
	// }

}

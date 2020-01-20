package com.inn.foresight.module.nv.inbuilding.rest.impl;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.constants.InBuildingConstants;
import com.inn.foresight.core.infra.wrapper.BuildingWrapper;
import com.inn.foresight.module.nv.inbuilding.rest.IInBuildingResultRest;
import com.inn.foresight.module.nv.inbuilding.result.service.IInBuildingResultService;

@Path("/ms/NVInBuildingResult")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class InBuildingResultMicroServiceRestImpl implements IInBuildingResultRest {

	/** The logger. */
	private static final Logger logger = LogManager.getLogger(InBuildingResultMicroServiceRestImpl.class);

	@Autowired
	IInBuildingResultService iInBuildingResultService;


	/**
	 * Sync in building result file.
	 *
	 * @param inputFile
	 *            the input file
	 * @param fileName
	 *            the file name
	 * @return the response
	 */
	@Override
	@POST
	@Path("/syncInBuildingResultFile")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response syncInBuildingResultFile(@Multipart(InBuildingConstants.FILE) InputStream inputFile,
			@Multipart(InBuildingConstants.FILENAME) String fileName) {

		if (inputFile == null || fileName == null) {
			return Response	.ok(InBuildingConstants.INVALID_PARAMETER_JSON)
							.build();
		} else {
			return Response	.ok(iInBuildingResultService.processIBFloorPlanAndTestResultData(inputFile))
							.build();
		}

	}

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
		Response response = null;
		try {
			response = iInBuildingResultService.downloadFloorPlanImage(unitId);
		} catch (Exception e) {
			logger.error("Getting Error in uploadFloorPlan{}", Utils.getStackTrace(e));
			response = Response	.ok(InBuildingConstants.FAILURE_JSON)
								.build();
		}
		return response;
	}

	/**
	 * Upload floor plan.
	 *
	 * @param unitId
	 *            the unit id
	 * @param technology
	 *            the technology
	 * @return the response
	 */
	//Is It unused???
	
	@POST
	@Path("/uploadFloorPlan/{unitId}/{technology}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Override
	public Response uploadFloorPlan(@PathParam("unitId") Integer unitId, @PathParam("technology") String technology,
			@Multipart(InBuildingConstants.FILE) InputStream inputFile,
			@Multipart(InBuildingConstants.FILENAME) String fileName) {
		Response response = null;
		try {
			if (inputFile == null || fileName == null) {
				response = Response	.ok(InBuildingConstants.INVALID_PARAMETER_JSON)
									.build();
			} else {
				response = Response	.ok(
						iInBuildingResultService.uploadFloorPlan("",unitId, technology, inputFile, fileName))
									.build();
			}
		} catch (Exception e) {
			logger.error("Getting Error in uploadFloorPlan{}", Utils.getStackTrace(e));
			response = Response	.ok(InBuildingConstants.FAILURE_JSON)
								.build();
		}
		return response;
	}

	/**
	 * Upload floor plan data.
	 *
	 * @param unitId
	 *            the unit id
	 * @return the response
	 */
	@POST
	@Path("/uploadFloorPlanData/{unitId}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response uploadFloorPlanData(@PathParam("unitId") Integer unitId,
			@Multipart(InBuildingConstants.FILE) InputStream inputFile,
			@Multipart(InBuildingConstants.FILENAME) String fileName) {
		Response response = null;
		try {
			if (inputFile == null || fileName == null) {
				response = Response	.ok(InBuildingConstants.INVALID_PARAMETER_JSON)
									.build();
			} else {
				response = Response	.ok(iInBuildingResultService.putFloorPlanData(unitId, inputFile, fileName))
									.build();
			}
		} catch (Exception e) {
			logger.error("Getting Error in uploadFloorPlanData {}", Utils.getStackTrace(e));
			response = Response	.ok(e.getMessage())
								.build();
		}
		return response;
	}

	/**
	 * Upload floor plan.
	 *
	 * @param request
	 *            the request
	 * @return the response
	 */
	@POST
	@Path("/uploadFloorPlans")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response uploadFloorPlans(@Context HttpServletRequest request) {
		Response response = null;
		try {
			BuildingWrapper wrapper = iInBuildingResultService.uploadMultipleFloorPlan(request);
			if (wrapper != null) {
				response = Response	.ok(new Gson().toJson(wrapper))
									.build();
			}
		} catch (Exception e) {
			logger.error("Getting Error in uploadFloorPlans{}", Utils.getStackTrace(e));
			response = Response	.ok(InBuildingConstants.PROBLEM_IN_UPLOADING_FLOOR_PLANS)
								.build();
		}
		return response;
	}

	/**
	 * Download floor plan data.
	 *
	 * @param unitId
	 *            the unit id
	 * @return the response
	 */

	@GET
	@Path("/downloadFloorPlanData/{unitId}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Override
	public Response downloadFloorPlanData(@PathParam("unitId") Integer unitId) {
		Response response = null;
		try {
			if (unitId != null) {
				response = Response	.ok(iInBuildingResultService.getFloorPlanData(unitId))
									.build();
			} else {
				response = Response	.ok(InBuildingConstants.INVALID_PARAMETER_JSON)
									.build();
			}
		} catch (Exception e) {
			logger.error("Error while fetching floor plan data from Hbase: {}", Utils.getStackTrace(e));
			response = Response	.ok(InBuildingConstants.FAILURE_JSON)
								.build();
		}
		return response;
	}
}

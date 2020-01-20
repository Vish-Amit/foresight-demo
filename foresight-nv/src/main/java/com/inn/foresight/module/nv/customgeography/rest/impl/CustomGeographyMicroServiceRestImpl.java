package com.inn.foresight.module.nv.customgeography.rest.impl;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.module.nv.workorder.model.CustomGeography.GeographyType;
import com.inn.foresight.module.nv.workorder.service.ICustomGeographyService;
import com.inn.foresight.module.nv.workorder.wrapper.CustomGeographyWrapper;

@Path("/ms/CustomGeography")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomGeographyMicroServiceRestImpl {

	/** The logger. */
	private Logger logger = LogManager.getLogger(CustomGeographyMicroServiceRestImpl.class);
	@Autowired
	private ICustomGeographyService service;

	@POST
	@Path("createCustomGeography")
	@Produces(MediaType.APPLICATION_JSON)
	public Response createCustomGeography(CustomGeographyWrapper wrapper) {
		logger.info("Going to create Custom Geography wrapper {}", wrapper);
		try{
			return Response.ok(service.createCustomGeography(wrapper)).build();
		} catch (Exception e){
			logger.error("Exception in createCustomBoundary : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(ForesightConstants.FAILURE_JSON).build();
		}
	}

	@GET
	@Path("getCustomGeography/{geographyId}/{type}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCustomGeography(@PathParam("geographyId") Integer geographyId,
			@PathParam("type") GeographyType type) {
		logger.info("Going to get Custom Geography geographyId {}, type {}", geographyId, type);
		try{
			return Response.ok(service.getCustomGeography(geographyId, type)).build();
		} catch (Exception e){
			logger.error("Exception in getCustomBoundary : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(ForesightConstants.FAILURE_JSON).build();
		}
	}
}

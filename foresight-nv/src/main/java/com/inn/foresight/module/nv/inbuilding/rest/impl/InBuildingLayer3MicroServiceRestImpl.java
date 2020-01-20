package com.inn.foresight.module.nv.inbuilding.rest.impl;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.constants.InBuildingConstants;
import com.inn.foresight.module.nv.inbuilding.service.InBuildingLayer3Service;

@Path("/ms/InBuildingLayer3/")
@Service("InBuildingLayer3MicroServiceRestImpl")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class InBuildingLayer3MicroServiceRestImpl {
	private Logger logger = LogManager.getLogger(InBuildingLayer3MicroServiceRestImpl.class);

	@Autowired
	InBuildingLayer3Service iBuildingLayer3Service ;
	@GET
	@Path("/getFloorPlanImage/")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getFloorPlanImage(@QueryParam("woId") Integer woId,@QueryParam("recipeId")Integer recipeId,@QueryParam("operatorName")String operatorName,@QueryParam("isDrive")Boolean isDrive) {
	logger.info("inside the method getFloorPlanImage woId {} recipeId {} operatorName {} isDrive {}",woId,recipeId,operatorName,isDrive);
	Response response = null;
		try {
			if (Utils.hasValidValue(String.valueOf(woId))&&Utils.hasValidValue(String.valueOf(recipeId))) {
				response=iBuildingLayer3Service.drawFloorPlanImgForLayer3(woId,recipeId,operatorName,isDrive);
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
	
}

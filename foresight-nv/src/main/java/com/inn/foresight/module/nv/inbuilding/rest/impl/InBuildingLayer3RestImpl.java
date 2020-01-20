package com.inn.foresight.module.nv.inbuilding.rest.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
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
import com.inn.foresight.module.nv.inbuilding.rest.InBuildingLayer3Rest;
import com.inn.foresight.module.nv.inbuilding.service.InBuildingLayer3Service;
import com.inn.foresight.module.nv.inbuilding.wrapper.NVIBResultWrapper;

@Path("/InBuildingLayer3/")
@Service("InBuildingLayer3RestImpl")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class InBuildingLayer3RestImpl implements InBuildingLayer3Rest{
	private Logger logger = LogManager.getLogger(InBuildingLayer3RestImpl.class);

	@Autowired
	InBuildingLayer3Service iBuildingLayer3Service;

	@GET
	@Path("/getFloorPlanImage/")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Override
	public Response getFloorPlanImage(@QueryParam("woId") Integer woId, @QueryParam("recipeId") Integer recipeId,
			@QueryParam("operatorName") String operatorName, @QueryParam("isDrive") Boolean isDrive,
			@Context HttpServletRequest request) {
		Response response = null;
		try {
			if (Utils.hasValidValue(String.valueOf(woId)) && Utils.hasValidValue(String.valueOf(recipeId))) {
				response = iBuildingLayer3Service.getFloorPlanImage(woId, recipeId, operatorName, isDrive, request);
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

	@GET
	@Path("/getBuildingInFoByWoId/{woId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response getBuildingInFoByWoId(@PathParam("woId") Integer woId) {
		try {
			if (Utils.hasValidValue(String.valueOf(woId))) {
				NVIBResultWrapper wrapper = iBuildingLayer3Service.getBuildingInFoByWoId(woId);
				if (wrapper != null) {
					return Response.ok(wrapper).build();
				}
			} else {
				return Response.ok(InBuildingConstants.INVALID_PARAMETER_JSON).build();
			}
		} catch (RestException e) {
			logger.error("Error while downloading floor plan image: {}", ExceptionUtils.getMessage(e));
			return Response.ok(InBuildingConstants.EXCEPTION_SOMETHING_WENT_WRONG).build();
		}
		return Response.ok(InBuildingConstants.FAILURE_JSON).build();
	}

	@GET
	@Path("/getKpiAvgByBuildingId/{buildingId}")
	@Override
	public Response getKpiAvgByBuildingId(@PathParam("buildingId") Integer buildingId,@QueryParam("floorId") Integer floorId, @QueryParam("band") Integer band,
			@QueryParam("startTime") Long startTime, @QueryParam("endTime") Long endTime) {
		logger.info("inside the method getKpiAvgByBuildingId buildingId is {}", buildingId);
		try {
			if (Utils.hasValidValue(String.valueOf(buildingId))) {
				return iBuildingLayer3Service.getKpiAvgByBuildingId(buildingId,floorId,startTime,endTime);

			} else {
				return Response.ok(InBuildingConstants.INVALID_PARAMETER_JSON).build();
			}
		} catch (RestException e) {
			logger.error("Error in calculate avg by building id : {}", ExceptionUtils.getMessage(e));
			return Response.ok(InBuildingConstants.EXCEPTION_SOMETHING_WENT_WRONG).build();
		}

	}

	@GET
	@Path("/getKpiAvgByFloorId/{floorId}")
	@Override
	public Response getKpiAvgByFloorId(@PathParam("floorId") Integer floorId, @QueryParam("band") Integer band,
			@QueryParam("startTime") Long startTime, @QueryParam("endTime") Long endTime) {
		logger.info("inside the method getKpiAvgByFloorId floorId is {}", floorId);
		try {
			if (Utils.hasValidValue(String.valueOf(floorId))) {
				NVIBResultWrapper wrapper = iBuildingLayer3Service.getKpiAvgByFloorId(floorId, band, startTime,
						endTime);
				if (wrapper != null) {
					return Response.ok(wrapper).build();
				}
			} else {
				return Response.ok(InBuildingConstants.INVALID_PARAMETER_JSON).build();
			}
		} catch (RestException e) {
			logger.error("Error in calculate avg by floorid id : {}", ExceptionUtils.getMessage(e));
			return Response.ok(InBuildingConstants.EXCEPTION_SOMETHING_WENT_WRONG).build();
		}
		return Response.ok(InBuildingConstants.FAILURE_JSON).build();

	}

	@GET
	@Path("/getRelativeChangeForRsrp/{buildingId}")
	@Override
	public Response getRelativeChangeForRsrp(@PathParam("buildingId") Integer buildingId,
			@QueryParam("floorId") Integer floorId) {
		logger.info("inside the method getRelativeChangeForRsrp buildingId is {}", buildingId);
		try {
			if (Utils.hasValidValue(String.valueOf(buildingId))) {
				List<NVIBResultWrapper> list = iBuildingLayer3Service.getRelativeChangeForRsrp(buildingId, floorId);
				if (list != null) {
					return Response.ok(list).build();
				}
			} else {
				return Response.ok(InBuildingConstants.INVALID_PARAMETER_JSON).build();
			}
		} catch (RestException e) {
			logger.error("Error getRelativeChangeForRsrp  building id : {}", ExceptionUtils.getMessage(e));
			return Response.ok(InBuildingConstants.EXCEPTION_SOMETHING_WENT_WRONG).build();
		}
		return Response.ok(InBuildingConstants.FAILURE_JSON).build();

	}

}

package com.inn.foresight.module.nv.workorder.rest.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.commons.Symbol;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.module.nv.NVConfigUtil;
import com.inn.foresight.module.nv.workorder.model.CustomGeography.GeographyType;
import com.inn.foresight.module.nv.workorder.service.ICustomGeographyService;
import com.inn.foresight.module.nv.workorder.utils.NVWorkorderUtils;
import com.inn.foresight.module.nv.workorder.wrapper.CustomGeographyWrapper;

@Path("/CustomGeography")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Service("CustomGeographyRestImpl")
public class CustomGeographyRestImpl {
	
	private Logger logger = LogManager.getLogger(CustomGeographyRestImpl.class);
	
	@Autowired
	private ICustomGeographyService service;

	@POST
	@Path("createCustomGeography")
	@Produces(MediaType.APPLICATION_JSON)
	public Response createCustomGeography(CustomGeographyWrapper wrapper,
			@Context HttpServletRequest request) {
		logger.info("Going to create Custom Geography, wrapper {}", wrapper);
		try{
			return Response.ok(service.createCustomGeography(wrapper, request)).build();
		} catch (Exception e){
			logger.error("Exception in saveWorkorderBoundry : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG).build();
		}
	}
	
	@GET
	@Path("getCustomGeography/{geographyId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCustomGeography(@PathParam("geographyId") Integer geographyId,
			@Context HttpServletRequest request) {
		logger.info("Going to get Custom Geography, geographyId {}", geographyId);
		try{
			String url = NVWorkorderUtils.getCustomGeographyDropwizardUrl(request,ConfigUtils.getString(NVConfigUtil.GET_CUSTOM_GEOGRAPHY_URL));
			url = url + Symbol.SLASH_FORWARD_STRING + geographyId;
			logger.info("Getting Dropwizard URL {}", url);
			return Response.ok(service.getCustomGeography(geographyId, url)).build();
		} catch (Exception e){
			logger.error("Exception in getCustomGeography : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG).build();
		}
	}
	
	@POST
	@Path("findAllCustomGeography")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findAllCustomGeography(@QueryParam("geographyId") Integer geographyId,
			@QueryParam("geographyType") String geographyType, 
			List<GeographyType> typeList) {
		logger.info("Going to get All Custom Geography for geographyId {}, geographyType {}, typeList {}", 
				geographyId, geographyType, typeList);
		try{
			return Response.ok(service.findAllCustomGeography(geographyId, geographyType, typeList)).build();
		} catch (Exception e){
			logger.error("Exception in findAllCustomGeography : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG).build();
		}
	}
	
}
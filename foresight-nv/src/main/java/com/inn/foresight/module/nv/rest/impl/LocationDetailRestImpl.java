package com.inn.foresight.module.nv.rest.impl;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.model.LocationDetailWrapper;
import com.inn.foresight.module.nv.service.ILocationDetailService;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;

/* The Class LocationDetailRestImpl. @author Team @version 2.0 */
/** Category LocationDetail. */
@Path("/LocationDetail")
@Produces("application/json")
@Consumes("application/json")
@Service("LocationDetailRestImpl")
public class LocationDetailRestImpl {
	
	/** The logger. */
	Logger logger = LogManager.getLogger(LocationDetailRestImpl.class);
	
	/** The location detail service. */
	@Autowired
	ILocationDetailService locationDetailService;
	
	/**
	 * Gets the location map for cell id mnc mcc.
	 *
	 * @return the location map for cell id mnc mcc
	 */
	@GET
	@Path("/getLocationMapForCellIdMncMcc")
	public Response getLocationMapForCellIdMncMcc(){
		logger.info("Inside method getLocationMapForCellId ");
		try {
			Map<String, LocationDetailWrapper> locationMap = locationDetailService.getLocationDetailForCellIdMncMcc();
			
			return Response.ok(locationMap).build();
		} catch (Exception e) {
			logger.error("Err in creating location Map based on cellId, mnc & mcc : {} ",e.getMessage());
		}
		return Response.noContent().build();
	}
	
	
	@GET
	@Path("/getSiteLocationByCGI")
	public Response getSiteLocationByCGI(@QueryParam("cgi") Integer cgi){
		logger.info("Inside method getLocationMapForCellId ");
		try {
			logger.info("Got the request for CGI :: {} ", cgi);
			if (Utils.hasValidValue(cgi.toString())) {
				return Response.ok(locationDetailService.getLocationbyCGI(cgi)).build();
			} else {
				return Response.ok(NVWorkorderConstant.INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) { 
			logger.error("Error in Getting Site Location based on CGI :: {}: Exception :: {} ",cgi ,e.getMessage());
		}
		return null;
	}
	}

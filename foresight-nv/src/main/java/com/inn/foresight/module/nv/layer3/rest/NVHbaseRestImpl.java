package com.inn.foresight.module.nv.layer3.rest;

import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.inn.commons.lang.StringUtils;
import com.inn.foresight.module.nv.layer3.service.INVHbaseService;
import com.inn.foresight.module.nv.layer3.utils.NVLayer3Utils;

@Path("/ms/NVHbaseRest")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NVHbaseRestImpl  extends NVLayer3Utils {

	/** The logger. */
	private Logger logger = LogManager.getLogger(NVHbaseRestImpl.class);
	@Autowired	
	INVHbaseService nvHbaseService;

	
	
	
	@POST
	@Path("/getLiveDriveReportData")
	public Response getLiveDriveReportData(Map<String, Map<String, List<String>>> valueMap) {
		try {
			if (valueMap != null) {
				logger.info("Going to get NVLayer3 getDriveKpiData for woId {} ", valueMap);
				String response = nvHbaseService.getLiveDriveReportData(valueMap);
				return Response.ok(response).build();
			} else {
				logger.error(INVALID_PARAMETER);
				return Response.ok(INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error("Exception in getDriveDetailData : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
		}
	}
	
	@GET
	@Path("/getHbaseDataForTimeStamp")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getHbaseDataForTimeStamp(@QueryParam(KEY_START_TIME) Long startTime,
			@QueryParam(KEY_END_TIME) Long endTime, @QueryParam(KEY_IMEI) String imei) {
		try {
			if (startTime != null && endTime != null && !StringUtils.isBlank(imei)) {
				logger.info("Going to get NVLayer3 getHbaseDataForTimeStamp Data for startTime {} endTime {} imei {}  ",
						startTime,endTime,imei);
				return Response.ok(nvHbaseService.getHbaseDataForTimeStamp(startTime, endTime,
						imei)).build();
			} else {
				return Response.ok(INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error("Exception in getHbaseDataForTimeStamp : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
		}
	}
	
	@POST
	@Path("/getAggregatedHbaseDataForRowKey")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getAggregatedHbaseDataForTimeStamp(Map<String, Map<String,List<String>>> locationTestTypeKeyMap) {
		
		try {
			if (locationTestTypeKeyMap != null) {
				logger.info("Going to get NVLayer3 getAggregatedHbaseDataForRowKey Data for locationTestTypeKeyMap {} ",locationTestTypeKeyMap);
				return Response.ok(nvHbaseService.getAggregatedHbaseDataForRowKey(locationTestTypeKeyMap)).build();
			} else {
				return Response.ok(INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error("Exception in getHbaseDataForTimeStamp : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
		}
	}
	
	
	
}

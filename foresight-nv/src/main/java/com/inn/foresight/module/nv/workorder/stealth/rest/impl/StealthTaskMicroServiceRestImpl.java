package com.inn.foresight.module.nv.workorder.stealth.rest.impl;

import java.util.Collections;
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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.inn.foresight.module.nv.workorder.stealth.constants.StealthConstants;
import com.inn.foresight.module.nv.workorder.stealth.service.IStealthTaskService;
import com.inn.foresight.module.nv.workorder.stealth.wrapper.StealthWOWrapper;
import com.inn.foresight.module.nv.workorder.wrapper.EnodeBWrapper;

/** The Class StealthTaskRestImpl. */
@Path("ms/NVStealth")
@Produces(MediaType.APPLICATION_JSON)
public class StealthTaskMicroServiceRestImpl {

	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(StealthTaskMicroServiceRestImpl.class);

	@Autowired
	private IStealthTaskService iStealthTaskService;

	@GET
	@Path("getStealthKPISummary")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStealthKPISummary(@QueryParam("workorderId") Integer workorderId,
			@QueryParam("zoomLevel") Integer zoomLevel,@QueryParam("startTime") Long startTime,@QueryParam("endTime") Long endTime) {
		logger.info("Going to getStealthKPIByWorkorderId: {}  ,zoomLevel : {}  startTime :{} endTime : {}", workorderId,zoomLevel,startTime,endTime);
		List<StealthWOWrapper> list = iStealthTaskService.getStealthKPISummary(workorderId,zoomLevel,startTime,endTime);
		if (list != null&&!list.isEmpty()) {
			return Response	.ok((list))
							.build();
		}
		return Response	.ok(String.format(StealthConstants.NO_DATA_AVAILABLE_JSON, workorderId))
						.build();
	}

	@GET
	@Path("getKPIDistributionAndCount")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getKPIDistributionAndCount(@QueryParam("tableName") String tableName, @QueryParam("type") String type,
			@QueryParam("name") String name, @QueryParam("timeStamp") Long timeStamp,
			@QueryParam("enodeBId") Integer enodeBId,@QueryParam("endTime") Long endTime) {
		logger.info("Going to getKPIDistributionAndCount for tableName: {} ,type :{} ,name :{},timeStamp :{} ,enodeBId :{} ",
				tableName, type, name, timeStamp, enodeBId);
		if (tableName != null && timeStamp != null && ((type != null && name != null) || enodeBId != null)) {
			Map<String, Object> list = iStealthTaskService.getKPIDistributionAndCount(tableName, type, name, timeStamp, enodeBId,endTime);
			if (list != null&&!list.isEmpty()) {
				return Response	.ok(new Gson().toJson(list))
								.build();
			}else {
				return Response	.ok(StealthConstants.NO_DATA_AVAILABLE_JSON)
						.build();
			}
		} else {
			return Response	.ok(new Gson().toJson(StealthConstants.INVALID_PARAMETER_JSON))
							.build();
		}
	}

	@GET
	@Path("getTopEnodeBDetails")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTopEnodeBDetails( @QueryParam("tableName") String tableName,
			@QueryParam("type") String type, @QueryParam("name") String name,@QueryParam("timeStamp")Long timeStamp,@QueryParam("endTime") Long endTime) {
		logger.info("Going to getTopEnodeBDeatails:for  tableName :{} ,type :{}, name :{}", tableName, type, name);
		if (tableName != null && type != null && name != null && timeStamp != null) {
			Map<String, Object> list = null;
			if (endTime != null) {
				list=iStealthTaskService.getTopEnodeBDetailsByDateRang(tableName, type, name, timeStamp, endTime);
			} else {
				list = iStealthTaskService.getTopEnodeBDetails(tableName, type, name, timeStamp);
			}
			if (list != null && !list.isEmpty()) {
				return Response.ok(new Gson().toJson(list)).build();
			} else {
				return Response.ok(StealthConstants.NO_DATA_AVAILABLE_JSON).build();
			}
		} else {
			return Response	.ok(new Gson().toJson(StealthConstants.INVALID_PARAMETER_JSON))
							.build();
		}
	}

	@GET
	@Path("getCellWiseData")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCellWiseData(@QueryParam("tableName") String tableName,@QueryParam("type") String type,@QueryParam("name") String name,
			@QueryParam("enodeBId") Integer enodeBId, @QueryParam("timeStamp") Long timestamp,@QueryParam("endTime") Long endTime) {
		List<EnodeBWrapper> list = iStealthTaskService.getCellWiseData(tableName, type, name, enodeBId, timestamp,endTime);
		if (list != null) {
			return Response.ok(list).build();
		}
		return Response.ok(Collections.emptyList()).build();
	}

	
	
	@POST
	@Path("getDashboardDataFromHbase")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDashboardDataFromHbase(List<String> columnList, @QueryParam("tableName") String tableName,@QueryParam("rowkeyPrefix") String rowkeyPrefix,
			@QueryParam("startDate") String startDate,@QueryParam("endDate") String endDate) {
		logger.info("Hbase REST getDashboardDataFromHbase {},{},{},{}", tableName, rowkeyPrefix,startDate,endDate);
		try {
			return Response.ok(iStealthTaskService.getDashboardDataFromHbase(columnList, tableName, startDate,
					rowkeyPrefix, endDate)).build();
		} catch (Exception e) {
				return Response.ok("{\"response\":\"error getting data\"}").build();
			
		}
	}

}

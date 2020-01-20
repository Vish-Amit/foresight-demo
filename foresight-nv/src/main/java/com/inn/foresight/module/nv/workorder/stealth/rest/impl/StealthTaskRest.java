package com.inn.foresight.module.nv.workorder.stealth.rest.impl;

import java.util.List;

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
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.utils.Utils;
import com.inn.foresight.module.nv.layer3.utils.NVLayer3Utils;
import com.inn.foresight.module.nv.workorder.stealth.rest.IStealthTaskRest;
import com.inn.foresight.module.nv.workorder.stealth.service.IStealthTaskDetailService;
import com.inn.foresight.module.nv.workorder.stealth.service.IStealthTaskResultService;

@Path("/NVStealth")
@Service("StealthTaskRest")
public class StealthTaskRest extends NVLayer3Utils implements IStealthTaskRest {
	/** The logger. */
	Logger logger = LogManager.getLogger(StealthTaskRest.class);

	@Autowired
	private IStealthTaskResultService iStealthTaskResultService;

	@Autowired
	private IStealthTaskDetailService iStealthTaskDetailService;

	@GET
	@Path("getStealthDeviceListByWOId")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response getStealthDeviceListByWOId(@QueryParam("workorderId") Integer workorderId) {
		logger.info("Going to getStealthDeviceListByWOId: {} ",workorderId);
		try {
			return Response	.ok(iStealthTaskResultService.getStealthDeviceListByWOId(workorderId))
							.build();

		} catch (Exception e) {
			logger.error("Exception in getStealthDeviceListByWOId: {} ", ExceptionUtils.getStackTrace(e));
			return Response	.ok(FAILURE_JSON)
							.build();
		}
	}

	@GET
	@Path("getStealthWoSummaryByWOId")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response getStealthWoSummary(@QueryParam("workorderId") Integer workorderId) {
		logger.info("Going to getStealthWoSummaryByWOId for workorderId:{}", workorderId);
		try {
			if (workorderId != null) {

				return Response	.ok(iStealthTaskResultService.getStealthWoSummary(workorderId))
								.build();
			} else {
				return Response	.ok(INVALID_PARAMETER_JSON)
								.build();
			}
		} catch (RestException e) {
			logger.error("RestException in getStealthWoSummaryByWOId : {} ", ExceptionUtils.getStackTrace(e));
			return Response	.ok(ExceptionUtils.getMessage(e))
							.build();
		} catch (Exception e) {
			logger.error("Exception in getStealthWoSummaryByWOId : {} ", ExceptionUtils.getStackTrace(e));
			return Response	.ok(FAILURE_JSON)
							.build();
		}
	}

	@GET
	@Path("completeStealthWorkorder")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response completeStealthWorkorder(@QueryParam("workorderId") Integer workorderId) {
		logger.info("Going to completeStealthWorkorder for workorderId:{}", workorderId);
		try {
			if (workorderId != null) {

				return Response	.ok(iStealthTaskDetailService.completeStealthWorkorder(workorderId))
								.build();
			} else {
				return Response	.ok(INVALID_PARAMETER_JSON)
								.build();
			}
		} catch (RestException e) {
			logger.error("RestException in completeStealthWorkorder : {} ", ExceptionUtils.getStackTrace(e));
			return Response	.ok(ExceptionUtils.getMessage(e))
							.build();
		} catch (Exception e) {
			logger.error("Exception in completeStealthWorkorder : {} ", ExceptionUtils.getStackTrace(e));
			return Response	.ok(FAILURE_JSON)
							.build();
		}
	}
    /**
     * Stealth Map visualization.
     * @param workorderId
     * @param date
     * @return
     */
	@GET
	@Path("getStealthKPISummary")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response getStealthKPISummary(@QueryParam("workorderId") Integer workorderId,
			@QueryParam("zoomLevel") Integer zoomLevel,@QueryParam("startTime") Long startTime,@QueryParam("endTime") Long endTime) {
		logger.info("Going to getStealthSummary for workorderId:{} ,startTime :{} endTime :{} zoomLevel :{}  ", workorderId, startTime,endTime,zoomLevel);
		try {
			if (workorderId != null) {

				return Response	.ok(iStealthTaskDetailService.getStealthKPISummary(workorderId,startTime,endTime,zoomLevel))
								.build();
			} else {
				return Response	.ok(INVALID_PARAMETER_JSON)
								.build();
			}
		} catch (RestException e) {
			logger.error("RestException in getStealthKPISummary : {} ", ExceptionUtils.getStackTrace(e));
			return Response	.ok(ExceptionUtils.getMessage(e))
							.build();
		} catch (Exception e) {
			logger.error("Exception in getStealthKPISummary : {} ", ExceptionUtils.getStackTrace(e));
			return Response	.ok(FAILURE_JSON)
							.build();
		}
	}
	/** *CEM Dashboard Start. */
	@GET
	@Path("getKPIDistributionAndCount")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response getKPIDistributionAndCount(@QueryParam("tableName") String tableName, @QueryParam("type") String type,
			@QueryParam("name") String name, @QueryParam("timeStamp") Long timeStamp,
			@QueryParam("enodeBId") Integer enodeBId, @QueryParam("endTime") Long endTime) {
		logger.info("Going to getKPIDistributionAndCount for tableName: {} ,type :{} ,name :{},timeStamp :{} ,enodeBId :{} ",
				tableName, type, name, timeStamp, enodeBId);		try {
			if (tableName != null && timeStamp != null && ((type != null && name != null) || enodeBId != null)) {

				return Response	.ok(iStealthTaskDetailService.getKPIDistributionAndCount(tableName, type, name, timeStamp, enodeBId,endTime))
								.build();
			} else {
				return Response	.ok(INVALID_PARAMETER_JSON)
								.build();
			}
		}  catch (Exception e) {
			logger.error("Exception in getKPIDistributionAndCount : {} ", ExceptionUtils.getStackTrace(e));
			return Response	.ok(FAILURE_JSON)
							.build();
		}
	}
	@GET
	@Path("getCellWiseData")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response getCellWiseData(@QueryParam("tableName") String tableName, @QueryParam("type") String type,
			@QueryParam("name") String name, @QueryParam("timeStamp") Long timeStamp,
			@QueryParam("enodeBId") Integer enodeBId, @QueryParam("endTime") Long endTime) {
		logger.info("Going to getCellWiseData for tableName: {} ,type :{} ,name :{},timeStamp :{} ,enodeBId :{} ",
				tableName, type, name, timeStamp, enodeBId);		
		try {
			if (tableName != null && timeStamp != null && ((type != null && name != null) || enodeBId != null)) {

				return Response	.ok(iStealthTaskDetailService.getCellWiseData(tableName, type, name, timeStamp, enodeBId,endTime))
								.build();
			} else {
				return Response	.ok(INVALID_PARAMETER_JSON)
								.build();
			}
		}  catch (Exception e) {
			logger.error("Exception in getKPIDistributionAndCount : {} ", ExceptionUtils.getStackTrace(e));
			return Response	.ok(FAILURE_JSON)
							.build();
		}
	}
	@GET
	@Path("getTopEnodeBDetails")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response getTopEnodeBDetails(@QueryParam("tableName") String tableName,
			@QueryParam("type") String type, @QueryParam("name") String name,@QueryParam("timeStamp")Long timeStamp,@QueryParam("endTime") Long endTime) {
		logger.info("Going to getTopEnodeBDetails for  tableName :{} ,type :{}, name :{}", tableName, type, name);
		try {
		if (tableName != null && type != null && name != null&&timeStamp!=null) {
				return Response	.ok(iStealthTaskDetailService.getTopEnodeBDetails(tableName, type, name,timeStamp,endTime))
								.build();
		} else {
			return Response	.ok(new Gson().toJson(INVALID_PARAMETER_JSON))
							.build();
		}
		}catch(Exception e) {
			logger.error("Exception in getTopEnodeBDeatails:for : {} ", ExceptionUtils.getStackTrace(e));
			return Response	.ok(FAILURE_JSON)
							.build();
		}
	}

	/** *CEM Dashboard End. */
	
	
	/** New CEM Dashboard Start. */
	
	// rest/NVPassiveDashboard/getNVPassiveData?date=280119&geographyId=0&level=ALL

	@POST
	@Path("getDashboardDataFromHbase")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response getDashboardDataFromHbase(List<String> hbaseColumns,@QueryParam("tableName") String tableName,
			@QueryParam("rowkeyprefix") String rowkeyPrefix,@QueryParam("startDate") String startDate,@QueryParam("endDate") String endDate) {
		logger.info("Going to getDashboardDataFromHbase for  tableName :{} ,rowkeyPrefix :{}, startDate :{} ,endDate :{}, hbase column [{}]", tableName,rowkeyPrefix,startDate,endDate,hbaseColumns);
		try {
		if (Utils.isValidString(tableName) && Utils.isValidString(rowkeyPrefix) && Utils.isValidString(startDate) && Utils.isValidString(endDate)) {
				return Response	.ok(iStealthTaskDetailService.getDashboardDataFromHbase(hbaseColumns,tableName,rowkeyPrefix,startDate,endDate))
								.build();
		} else {
			return Response	.ok(new Gson().toJson(INVALID_PARAMETER_JSON))
							.build();
		}
		}catch(Exception e) {
			logger.error("Exception in getDashboardDataFromHbase : {} ", ExceptionUtils.getStackTrace(e));
			return Response	.ok(FAILURE_JSON)
							.build();
		}
	}

}

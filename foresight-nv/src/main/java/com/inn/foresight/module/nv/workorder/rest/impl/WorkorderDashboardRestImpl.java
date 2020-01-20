package com.inn.foresight.module.nv.workorder.rest.impl;

import javax.ws.rs.GET;
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

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.module.nv.report.utils.ReportConstants;
import com.inn.foresight.module.nv.reportgeneration.wrapper.NVReportWrapper;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;
import com.inn.foresight.module.nv.workorder.service.IWorkorderDashboardService;

@Path("/WorkorderDashboard")
@Service("WorkorderDashboardRestImpl")
public class WorkorderDashboardRestImpl {

	@Autowired
	private IWorkorderDashboardService iWorkorderDashboardService;

	/** The logger. */
	private Logger logger = LogManager.getLogger(WorkorderDashboardRestImpl.class);

	@GET
	@Path("/getWorkorderCount")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getWorkorderCount(@QueryParam("type") String type, @QueryParam("currentDate") Long currentDate,
			@QueryParam("geographyLevel") String geographyLevel, @QueryParam("geographyId") Integer geographyId) {
		Response responseToReturn = Response.ok(NVWorkorderConstant.FAILURE_JSON)
											.build();
		try {
			if (type != null && currentDate != null && geographyLevel != null && geographyId != null) {
				responseToReturn = Response	.ok(
						iWorkorderDashboardService.getWOCount(type, currentDate, geographyLevel, geographyId))
											.build();
			} else {
				responseToReturn = Response	.ok(NVWorkorderConstant.INVALID_PARAMETER_JSON)
											.build();
			}
		} catch (RestException e) {
			logger.error("RestException in getWorkorderCount: {} ", ExceptionUtils.getStackTrace(e));
		}
		return responseToReturn;
	}

	@GET
	@Path("/getWorkorderCountByStatus")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getWorkorderCount(@QueryParam("type") String type, @QueryParam("templateType") String templateType,
			@QueryParam("currentDate") Long currentDate, @QueryParam("geographyLevel") String geographyLevel,
			@QueryParam("geographyId") Integer geographyId) {
		Response responseToReturn = Response.ok(NVWorkorderConstant.FAILURE_JSON)
											.build();
		try {
			if (type != null && templateType != null && currentDate != null && geographyLevel != null
					&& geographyId != null) {
				responseToReturn = Response	.ok(iWorkorderDashboardService.getWorkorderCountByStatus(type, templateType,
						currentDate, geographyLevel, geographyId))
											.build();
			} else {
				responseToReturn = Response	.ok(NVWorkorderConstant.INVALID_PARAMETER_JSON)
											.build();
			}
		} catch (RestException e) {
			logger.error("RestException in getWorkorderCountByStatus: {} ", ExceptionUtils.getStackTrace(e));
		}
		return responseToReturn;
	}

	@GET
	@Path("/getWorkorderCountByAssignedType")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getWorkorderCountByAssignedType(@QueryParam("assignedType") String assignedType,
			@QueryParam("currentDate") Long currentDate, @QueryParam("geographyLevel") String geographyLevel,
			@QueryParam("geographyId") Integer geographyId) {
		Response responseToReturn = Response.ok(NVWorkorderConstant.FAILURE_JSON)
											.build();
		try {
			if (assignedType != null && currentDate != null && geographyLevel != null && geographyId != null) {
				responseToReturn = Response	.ok(iWorkorderDashboardService.getWorkorderCountByAssignedType(assignedType,
						currentDate, geographyLevel, geographyId))
											.build();
			} else {
				responseToReturn = Response	.ok(NVWorkorderConstant.INVALID_PARAMETER_JSON)
											.build();
			}
		} catch (RestException e) {
			logger.error("RestException in getWorkorderCountByAssignedType: {} ", ExceptionUtils.getStackTrace(e));
		}
		return responseToReturn;
	}

	@GET
	@Path("/getDueWorkorderDayWiseCount")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDueWorkorderDayWiseCount(@QueryParam("currentDate") Long currentDate,
			@QueryParam("geographyLevel") String geographyLevel, @QueryParam("geographyId") Integer geographyId) {
		Response responseToReturn = Response.ok(NVWorkorderConstant.FAILURE_JSON)
											.build();
		try {
			if (currentDate != null && geographyLevel != null && geographyId != null) {
				responseToReturn = Response	.ok(iWorkorderDashboardService.getDueWorkorderDayWiseCount(currentDate,
						geographyLevel, geographyId))
											.build();
			} else {
				responseToReturn = Response	.ok(NVWorkorderConstant.INVALID_PARAMETER_JSON)
											.build();
			}
		} catch (RestException e) {
			logger.error("RestException in getDueWorkorderDayWiseCount: {} ", ExceptionUtils.getStackTrace(e));
		}
		return responseToReturn;
	}

	@GET
	@Path("/getDueWorkorderList")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDueWorkorderList(@QueryParam("currentDate") Long currentDate,
			@QueryParam("geographyLevel") String geographyLevel, @QueryParam("geographyId") Integer geographyId,
			@QueryParam("llimit") Integer llimit, @QueryParam("ulimit") Integer ulimit) {
		Response responseToReturn = Response.ok(NVWorkorderConstant.FAILURE_JSON)
											.build();
		try {
			if (currentDate != null && geographyLevel != null && geographyId != null) {
				responseToReturn = Response	.ok(
						iWorkorderDashboardService.getDueWorkorderList(currentDate, geographyLevel, geographyId,llimit,ulimit))
											.build();
			} else {
				responseToReturn = Response	.ok(NVWorkorderConstant.INVALID_PARAMETER_JSON)
											.build();
			}
		} catch (RestException e) {
			logger.error("RestException in getDueWorkorderList: {} ", ExceptionUtils.getStackTrace(e));
		}
		return responseToReturn;
	}

	@GET
	@Path("/getWorkorderReport")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getWorkorderReport(@QueryParam("currentDate") Long currentDate,
			@QueryParam("geographyLevel") String geographyLevel, @QueryParam("geographyId") Integer geographyId) {
		logger.info("Going to get Report for date {}, geographyId {}, geographyType {}", currentDate, geographyId,
				geographyLevel);
		Response responseToReturn = Response.ok(NVWorkorderConstant.FAILURE_JSON)
											.build();
		if (currentDate != null && geographyLevel != null && geographyId != null) {
			Response.ResponseBuilder builder = Response.status(200);

			NVReportWrapper reportWrapper;
			try {
				reportWrapper = iWorkorderDashboardService.getWorkorderReportData(currentDate, geographyLevel,
						geographyId);
				builder = builder	.entity(reportWrapper.getFile())
									.header(ReportConstants.CONTENT_TYPE, ReportConstants.CONTENT_TYPE_CSV)
									.header(ReportConstants.CONTENT_DISPOSITION,
											"attachment; filename=\"" + reportWrapper.getReportName() + "\"")
									.header("filename", reportWrapper.getReportName());

				return builder.build();
			} catch (RestException e) {
				logger.error("RestException in getWorkorderReport: {} ", ExceptionUtils.getStackTrace(e));

			}

		} else {
			responseToReturn = Response	.ok(NVWorkorderConstant.INVALID_PARAMETER_JSON)
										.build();
		}
		return responseToReturn;
	}

}

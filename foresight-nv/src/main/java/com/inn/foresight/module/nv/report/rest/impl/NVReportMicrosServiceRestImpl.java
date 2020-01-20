package com.inn.foresight.module.nv.report.rest.impl;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.inn.foresight.module.nv.report.service.INVReportService;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.inn.commons.Symbol;
import com.inn.commons.io.IOUtils;
import com.inn.commons.lang.NumberUtils;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.core.workorder.model.GWOMeta;
import com.inn.foresight.module.nv.dashboard.utils.NVDashboardConstants;
import com.inn.foresight.module.nv.layer3.constants.NVLayer3Constants;
import com.inn.foresight.module.nv.layer3.utils.NVLayer3Utils;
import com.inn.foresight.module.nv.layer3.wrapper.Layer3ReportWrapper;
import com.inn.foresight.module.nv.report.customreport.ssvt.service.ISSVTService;
import com.inn.foresight.module.nv.report.service.ICustomerExperienceReportService;
import com.inn.foresight.module.nv.report.service.INVDashboardReportService;
import com.inn.foresight.module.nv.report.service.IStealthReportService;
import com.inn.foresight.module.nv.report.utils.ReportConstants;

@Path("/ms/NVReportRest")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NVReportMicrosServiceRestImpl {
	/** The logger. */
	private Logger logger = LogManager.getLogger(NVReportMicrosServiceRestImpl.class);
	
	@Autowired
	private ICustomerExperienceReportService cstexservice;
	
	@Autowired
	private IStealthReportService iStealthReportService;
	
	@Autowired
	private INVDashboardReportService nvDashboardReportService;

	@Autowired
	private INVReportService nvReportService;
	
	@Autowired
	private ISSVTService ssvtService;
	
	
	@GET
	@Path("generateReportforWoId")
	public String generateReportForWorkOrderId(@QueryParam("workorderId") Integer workorderId) {
		logger.info("Going to generate the SSVT report for workOrderid {} ", workorderId);
		return "SUCCESS";
	}

	@GET
	@Path("/getDeviceReportForStealth")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getDeviceReportForStealth(@QueryParam(NVLayer3Constants.WORKORDER_ID) String workorderId, @QueryParam(NVLayer3Constants.TASK_ID) String taskId, @QueryParam(NVLayer3Constants.DATE) String date) {
		logger.info("Inside method getDeviceReportForStealth, TaskId: {}, WorkorderId: {} ",taskId, workorderId);
		Response response = null;
		try {
			if (taskId!=null && workorderId!=null && date != null) {
				Layer3ReportWrapper wrapper = cstexservice.getDeviceReportForStealth(taskId, workorderId, date);
				Response.ResponseBuilder builder = Response.status(200);
				if (wrapper != null && wrapper.getFile() != null && wrapper.getFileName() != null) {
					builder = builder	.entity(wrapper.getFile())
										.header("Content-Type", MediaType.APPLICATION_OCTET_STREAM)
										.header("Content-Disposition",
												"attachment; filename=\"" + wrapper.getFileName() + "\"");
					response = builder.build();
				} else {
					response = Response	.ok(NVLayer3Constants.DATA_NOT_FOUND_JSON)
										.build();
				}
			}
		} catch (Exception e) {
			logger.error("Exception in getWOReportDumpForStealth : {}", ExceptionUtils.getStackTrace(e));
			response = Response.ok(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
		}
		return response;
	}
	
		@GET
	@Path("/getPDFReportForStealthWO")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getPDFReportForStealthWO(@QueryParam(NVLayer3Constants.WORKORDER_ID) String workorderId) {
		logger.info("Inside method getDeviceReportForStealth  WorkorderId: ",workorderId);
		Response response = null;
		try {
			if (workorderId!=null) {
				List<Integer> taskIdList = new ArrayList<>();
				File file = iStealthReportService.getPDFReportForStealthWO(workorderId, taskIdList, null,null,null);
				Response.ResponseBuilder builder = Response.status(200);
				builder = builder.entity(IOUtils.toByteArray(new FileInputStream(file))).header("Content-Type", MediaType.APPLICATION_OCTET_STREAM)
						.header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
				response = builder.build();
			}
		} catch (Exception e) {
			logger.error("Exception in getDevicePDFReportForStealth : {}", ExceptionUtils.getStackTrace(e));
			response = Response.ok(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
		}
		return response;
	}

	@GET
	@Path("/generateNvDashboardReport")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response generateNvDashboardReport(
			@QueryParam(NVDashboardConstants.GEOGRAPHY_NAME) String geographyName,
			@QueryParam(NVDashboardConstants.GEOGRAPHY_TYPE) String geographyType,
			@QueryParam(NVDashboardConstants.BAND) String band,
			@QueryParam(NVDashboardConstants.TECHNOLOGY) String technology,
			@QueryParam(NVDashboardConstants.OPERATOR) String operator,
			@QueryParam(NVDashboardConstants.START_DATE) String startDate,
			@QueryParam(NVDashboardConstants.END_DATE) String endDate) {
		logger.info("Inside method generateNvDashboardReport, GEOGRAPHY_NAME: {}, GEOGRAPHY_TYPE: {}, BAND: {}, TECHNOLOGY: {}, OPERATOR: {}, START_DATE: {}, END_DATE: {} ",geographyName,geographyType,band,technology,operator,startDate,endDate);
		Response response = null;
		Response.ResponseBuilder builder = Response.status(200);
		try {
			File nvDashboardReport = nvDashboardReportService.generateNvDashboardReport(geographyName, geographyType, band, technology, operator,startDate,startDate);
			builder = builder.entity(IOUtils.toByteArray(new FileInputStream(nvDashboardReport))).header("Content-Type", MediaType.APPLICATION_OCTET_STREAM)
					.header("Content-Disposition", "attachment; filename=\"" + ReportConstants.NETVELOCITY + Symbol.UNDERSCORE + nvDashboardReport.getName() + "\"");
		} catch (Exception e) {
			logger.error("Exception in generateNvDashboardReport : {}", ExceptionUtils.getStackTrace(e));
			builder = builder.entity(e.getMessage()).header("Content-Type", MediaType.APPLICATION_OCTET_STREAM)
					.header("Content-Disposition", "attachment; filename=\""  + "Unable_To_Generate_Report.txt" + "\"");
		}
		return builder.build();
	}
	
	@GET
	@Path("/processStealthWOCsvDump")
	@Produces(MediaType.APPLICATION_JSON)
	public Response processStealthWOCsvDump(@QueryParam(ReportConstants.WORKORDER_ID) String workorderId) {
		logger.info("Going to process Stealth WO csv dump for workorderId {} ", workorderId);
		Response response = null;
		try {
			if (!StringUtils.isBlank(workorderId)) {
				String fileName = iStealthReportService.processWOCsvDump(workorderId);
				if (fileName != null) {
					response = Response.ok(fileName).build();
				}
			} else {
				response = Response.ok(NVLayer3Constants.INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error("Exception in processStealthWOCsvDump : {}", Utils.getStackTrace(e));
			response = Response.ok(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
		}
		return response;
	}
	
	@GET
	@Path("/getStealthWOCsvDump")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getStealthWOCsvDump(@QueryParam(ReportConstants.KEY_FILE_PATH) String filePath) {
		logger.info("Going to get Stealth WO csv dump from hdfs path: {} ", filePath);
		Response response = null;
		try {

			NVLayer3Utils.validateFilePath(filePath);				
			
			if (!StringUtils.isBlank(filePath)) {
				response = iStealthReportService.getFileForDownloadFromHDFS(filePath);
			} else {
				response = Response.ok(NVLayer3Constants.INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error("Exception in getStealthWOCsvDump : {}", Utils.getStackTrace(e));
			response = Response.ok(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
		}
		return response;
	}
	
	@POST
	@Path("/processStealthWOPdfForTaskId")
	@Produces(MediaType.APPLICATION_JSON)
	public Response processStealthWOPdfForTaskId(@QueryParam(ReportConstants.WORKORDER_ID) String workorderId, @QueryParam(ReportConstants.START_DATE) Long startDate, @QueryParam(ReportConstants.END_DATE) Long endDate, List<Integer> taskIdList) {
		logger.info("Going to process Stealth WO PDF for workorderId {} ", workorderId);
		Response response = null;
		try {
			if (!StringUtils.isBlank(workorderId) && taskIdList != null) {
				String fileName = iStealthReportService.processStealthWOPdfForTaskId(workorderId, taskIdList, startDate, endDate);
				if (fileName != null) {
					response = Response.ok(fileName).build();
				} else {
					response = Response.ok(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
				}
			} else {
				response = Response.ok(NVLayer3Constants.INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error("Exception in processStealthWOPdfForTaskId : {}", Utils.getStackTrace(e));
			response = Response.ok(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
		}
		return response;
	}
	
	@GET
	@Path("/getStealthWOPdfForTaskId")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getStealthWOPdfForTaskId(@QueryParam(ReportConstants.KEY_FILE_PATH) String filePath) {
		logger.info("Going to get Stealth WO PDF from hdfs path: {} ", filePath);
		Response response = null;
		try {
			NVLayer3Utils.validateFilePath(filePath);
		
			if (!StringUtils.isBlank(filePath)) {
				response = iStealthReportService.getFileForDownloadFromHDFS(filePath);
			} else {
				response = Response.ok(NVLayer3Constants.INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error("Exception in getStealthWOPdfForTaskId : {}", Utils.getStackTrace(e));
			response = Response.ok(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
		}
		return response;
	}

	@POST
	@Path("/updateSiteInfoInSSVTReport")
	public Response updateSiteInfoInSSVTReport(String json) {
		logger.info("Going to update site info for JSON: {} ", json);
		Response response = null;
		if(!StringUtils.isBlank(json)){
			response = nvReportService.updateSiteInfoInSSVTReport(json);
		} else{
			response = Response.ok(NVLayer3Constants.INVALID_PARAMETER_JSON).build();
		}
		return response;
	}
	
	
	@GET
	@Path("/generateRecipeSummary")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response generateRecipeSummary(@QueryParam(ReportConstants.RECIPEID) Integer recipeId) {
		logger.info("Going to generateRecipeSummary for recipeId : {} ", recipeId);
		Response response = null;
		try {
			if(NumberUtils.isValidNumber(recipeId.doubleValue())) {
				GWOMeta gwoMeta = ssvtService.getRecipeWiseSSVTSummary(recipeId);
				if(gwoMeta != null) {
					response = Response.ok(gwoMeta).build();
				} else {
					response = Response.ok(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
				}
			} else {
				response = Response.ok(NVLayer3Constants.INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error("Exception in getStealthWOPdfForTaskId : {}", Utils.getStackTrace(e));
			response = Response.ok(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
		}
		return response;
	}
}

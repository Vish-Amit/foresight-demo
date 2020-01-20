package com.inn.foresight.module.nv.reportgeneration.rest.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.report.service.IReportService;
import com.inn.foresight.module.nv.report.workorder.wrapper.KPIWrapper;
import com.inn.foresight.module.nv.reportgeneration.constants.NVReportGenerationConstants;
import com.inn.foresight.module.nv.reportgeneration.service.INVReportGenerationService;
import com.inn.foresight.module.nv.reportgeneration.wrapper.NVReportWrapper;
import com.inn.foresight.module.nv.workorder.wrapper.NVWorkorderWrapper;


@Path("/ms/NVReport")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NVReportGenerationMicrosServiceRestImpl {
	/** The logger. */
	private Logger logger = LogManager.getLogger(NVReportGenerationMicrosServiceRestImpl.class);

	@Autowired
	private INVReportGenerationService service;
	
	@Autowired
	private IReportService reportService;
	
	private static final String EXCEPTION_IN_REPORT="Exception in getNVReport : {}";
	private static final String EXCEPTION_MSG="msg {} , {} ";
	
	@GET
	@Path("/getNVReport/{analyticsrepoId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getReport(@PathParam("analyticsrepoId") Integer rowkey) {
		logger.info("Going to get report data for rowKey {}", rowkey);
		try {
			NVReportWrapper reportWrapper = service.getReportData(rowkey);
			Response.ResponseBuilder builder = Response.status(NVReportGenerationConstants.INDEX_TW0_HUNDRED);

			builder = builder.entity(reportWrapper.getFile()).header("Content-Type", MediaType.APPLICATION_JSON)
					.header("Content-Disposition", "attachment; filename=\"" + reportWrapper
							.getReportName() + "\"");

			return builder.build();
		} catch (Exception e) {
			logger.error(EXCEPTION_IN_REPORT, ExceptionUtils.getStackTrace(e));
			return Response.ok(ForesightConstants.FAILURE_JSON).build();
		}
	}


	@POST
	@Path("/generateReport")
	public Response generateReport(String json){
		logger.info("Inside method generateReport with input json {} ",json);
		if(json!=null && !json.isEmpty()){
			try {
				Map<String, Object> jsonMap = new ObjectMapper().readValue(json, HashMap.class);
				Integer analyticsrepositoryId = (Integer) jsonMap.get(ForesightConstants.ANALYTICAL_REPORT_KEY);
				logger.info("analyticsrepositoryId {} ",analyticsrepositoryId);
				if(analyticsrepositoryId!=null){
					service.generateReport(json);
					return Response.ok(ForesightConstants.SUCCESS_JSON).build();
				}else{
					return Response.ok(ForesightConstants.FAILURE_JSON).build();
				}
			} catch (Exception e) {
				logger.error(EXCEPTION_MSG, e.getLocalizedMessage(), Utils.getStackTrace(e));
				return Response.ok("{\"result\":\""+e.getLocalizedMessage()+" \"}").build();
			}
		}else{
			return Response.ok(ForesightConstants.FAILURE_JSON).build();
		}
	}


	@GET
	@Path("/generateReportById")
	public Response generateReportById(@QueryParam("analyticsrepositoryId") Integer analyticsRepositoryId){
		logger.info("Inside method generateReport with input analyticsRepositoryId {} ",analyticsRepositoryId);

			try {
				if(analyticsRepositoryId!=null){
					service.generateReport(analyticsRepositoryId.toString());
				}else{
					return Response.ok(ForesightConstants.FAILURE_JSON).build();
				}
			} catch (Exception e) {
					logger.error("Error inside method generateReportById for id {} ",analyticsRepositoryId);
				 return Response.ok("{\"result\":\""+e.getMessage()+" \"}").build();
			}

		return Response.ok(ForesightConstants.SUCCESS_JSON).build();
	}

	@GET
	@Path("/getReportById/{analyticsrepoId}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getReportById(@PathParam("analyticsrepoId") Integer rowkey,@QueryParam("extension") String extension) {
		logger.info("Going to get report data for analyticsrepoId {}", rowkey);
		try {
			return service.getReportById(rowkey,extension);
		} catch (Exception e) {
			logger.error("Exception in getReportById method  : {}",e.getMessage() );
			return Response.ok(ForesightConstants.FAILURE_JSON).build();
		}
	}

	@GET
	@Path("/getPdfReportByInstanceId")
		public Response getPdfReportByInstanceId(@QueryParam("analyticsRepositoryId") Integer analyticsRepositoryId){
		logger.info("Inside method getPdfReportByInstanceId with input analyticsRepositoryId {} ",analyticsRepositoryId);
			logger.info("analyticsRepositoryId {} ",analyticsRepositoryId);
			if(analyticsRepositoryId!=null){
				return service.getPdfReportByInstanceId(analyticsRepositoryId);
			}
    else{
			return Response.ok("{\"result\":\"Invald reportInstanceId , Please insert Valid Id \"}").build();
		}
	}


	@POST
	@Path("/getInBuildingReport")
	public Response getInBuildingReport(String json){
		logger.info("Inside method generateInBuildingReport with input json {} ",json);
		if(json!=null && !json.isEmpty()){
			try {
				return service.getInBuildingReport(json);
			} catch (Exception e) {
				logger.info(EXCEPTION_MSG,e.getMessage(),e.getLocalizedMessage());
				return Response.ok("{\"result\":\""+e.getLocalizedMessage()+" \"}").build();
			}
		}else{
			return Response.ok(ForesightConstants.FAILURE_JSON).build();
		}
	}



	@GET
	@Path("/getInBuildingReportPdf")
	public Response getInBuildingReport(@QueryParam("recipeId") Integer recipeId,@QueryParam("operator") String operator,
			@QueryParam("inbuildingid") Integer inbuildingid,@QueryParam("workorderId") Integer workorderId,@QueryParam("technology") String technology){
		logger.info("Inside method generateInBuildingReport");
			try {
				return service.getInBuildingReport(recipeId,operator,inbuildingid,workorderId, technology);
			} catch (Exception e) {
				logger.info(EXCEPTION_MSG,e.getMessage(),e.getLocalizedMessage());
				return Response.ok("{\"result\":\""+e.getLocalizedMessage()+" \"}").build();
			}

	}

	@GET
	@Path("/getInBuildingReportFloorWise")
	public Response getInBuildingReportFloorWise(@QueryParam("floorId") Integer floorId,
			@QueryParam("inbuildingid") Integer inbuildingid, @QueryParam("technology") String technology){
		logger.info("Inside method getInBuildingReportFloorWise with input json floorId {}   inbuildingid {}",floorId,inbuildingid);
			try {
				return service.getInBuildingReportFloorWise(floorId,inbuildingid, technology);
			} catch (Exception e) {
				logger.info(EXCEPTION_MSG,e.getMessage(),e.getLocalizedMessage());
				return Response.ok("{\"result\":\""+e.getLocalizedMessage()+" \"}").build();
			}

	}

	@GET
	@Path("/saveImage")
	public String saveImagefromGoogleApi() {
		logger.info("Inside method save Image ");
		try {
			String response=service.saveImagefromGoogleApi();
			if(response!=null){
				return response;
			}
		} catch (Exception e) {
			logger.error("Exception inside method saveImagefromGoogleApi {} ", ExceptionUtils.getStackTrace(e));
		}
		return ForesightConstants.FAILURE_JSON;
	}
	
	
	

	@GET
	@Path("/checkLegendData")
	@Produces(MediaType.APPLICATION_JSON)
	public Response checkLegendData() {
		logger.info("Inside method checkLegendData ");
		try {
			List<KPIWrapper> reportWrapper = reportService.getListOfKpisWithRanges(new HashMap<>(), "SSVT REPORT");
			logger.info("reportWrapper data {}  ",reportWrapper);
		} catch (Exception e) {
			logger.error("Exception inside method checkLegendData {} ", ExceptionUtils.getStackTrace(e));
			return Response.ok(ForesightConstants.FAILURE_JSON).build();
		}
		return null;
	}
	@POST
	@Path("/removeRejectedReportData")
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeRejectedReportData(NVWorkorderWrapper nvWorkorderWrapper){
		logger.info("Inside method gremoveRejectedReportData");
		try {
			boolean isDeleted=service.removeRejectedData(nvWorkorderWrapper);
			if(isDeleted){
				return Response.ok(ForesightConstants.SUCCESS_JSON).build();	
			}else{
				return Response.ok(ForesightConstants.FAILURE_JSON).build();
			}
			
		} catch (Exception e) {
			logger.error("Exception inside method checkLegendData {} ", ExceptionUtils.getStackTrace(e));
			return Response.ok(ForesightConstants.FAILURE_JSON).build();
		}
	}

}

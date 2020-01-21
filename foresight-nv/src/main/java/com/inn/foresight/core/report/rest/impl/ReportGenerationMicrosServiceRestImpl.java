package com.inn.foresight.core.report.rest.impl;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.report.service.IReportGenerationService;

@Path("/ms/ReportGeneration")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReportGenerationMicrosServiceRestImpl {
	
	/** The logger. */
	private Logger logger = LogManager.getLogger(ReportGenerationMicrosServiceRestImpl.class);
	
	@Autowired
	private IReportGenerationService service;


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
	@Path("/getReportByPath")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getReportByPath(@QueryParam("filePath") String filePath,@QueryParam("downloadFileName") String downloadFileName,@QueryParam("extension") String extension) {
		logger.info("Going to get report data for filePath {}", filePath);
		try {
			return service.getReportByPath(filePath,downloadFileName,extension);
		} catch (Exception e) {
			logger.error("Exception in getReportByPath method  : {}",e.getMessage() );
			return Response.ok(ForesightConstants.FAILURE_JSON).build();
		}
	}

}

package com.inn.foresight.module.nv.layer3.rest;

import java.util.List;

import javax.ws.rs.Consumes;
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

import com.inn.foresight.module.nv.layer3.qmdlParser.wrappers.Layer3SummaryWrapper;
import com.inn.foresight.module.nv.layer3.qmdlParser.wrappers.NVL3CsvDataWrapper;
import com.inn.foresight.module.nv.layer3.service.parse.INVAdhocDriveProcessingService;
import com.inn.foresight.module.nv.layer3.utils.NVLayer3Utils;

@Path("/ms/NVAdhocDrive")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NVAdhocDriveMicroServiceRestImpl {

	
	private Logger logger = LogManager.getLogger(NVAdhocDriveMicroServiceRestImpl.class);
	@Autowired
	private INVAdhocDriveProcessingService nvAdhocDrive;


	@GET
	@Path("/processAdhocDriveCsv")
	public Response processAdhocDriveCsv(@QueryParam(NVLayer3Utils.WORKORDER_ID) Integer woId) {
		try {
			if (woId != null) {
				logger.info("Going to Process data for woId {} ", woId);
				List<NVL3CsvDataWrapper> response = nvAdhocDrive.processAdhocFileForReport(woId, new Layer3SummaryWrapper());
				return Response.ok(response).build();
			} else {
				logger.error(NVLayer3Utils.INVALID_PARAMETER);
				return Response.ok(NVLayer3Utils.INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error("Exception in processAdhocDriveCsv : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(NVLayer3Utils.EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
		}
	}
	
	
}

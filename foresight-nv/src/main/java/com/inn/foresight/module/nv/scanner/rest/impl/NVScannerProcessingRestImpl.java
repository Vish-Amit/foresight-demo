package com.inn.foresight.module.nv.scanner.rest.impl;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.foresight.module.nv.scanner.rest.NVScannerProcessingRest;
import com.inn.foresight.module.nv.scanner.service.NVScannerProcessingService;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;

@Path("/NVScanner")
@Service("NVScannerProcessingRestImpl")
@Produces(MediaType.APPLICATION_JSON)
public class NVScannerProcessingRestImpl implements NVScannerProcessingRest {

	private static Logger logger = LogManager.getLogger(NVScannerProcessingRestImpl.class);

	@Autowired
	NVScannerProcessingService scannerService;

	@GET
	@Path("getScannerDump")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Override
	public Response getCSVDumpFromHDFS(@QueryParam("workorderId") Integer workorderId,
			@QueryParam("recipeId") Integer recipeId) {
		try {
			logger.info("getting workorderId {} and recipe id {} for scanner CSV dump",workorderId,recipeId);
			if (workorderId != null && recipeId != null) {
				return scannerService.getCSVDumpFromMicroService(workorderId, recipeId);
			} else {
				Response.ok(NVWorkorderConstant.INVALID_PARAMETERS).build();
			}

		} catch (Exception e) {
			logger.error("Exception while download scanner CSV dump {}", ExceptionUtils.getStackTrace(e));
		}

		return Response.ok(NVWorkorderConstant.FILE_NOT_FOUND).build();
	}

}

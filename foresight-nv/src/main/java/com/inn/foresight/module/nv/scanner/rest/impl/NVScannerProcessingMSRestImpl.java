package com.inn.foresight.module.nv.scanner.rest.impl;

import java.io.IOException;

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

import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.http.HttpException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.module.nv.NVConfigUtil;
import com.inn.foresight.module.nv.NVConstant;
import com.inn.foresight.module.nv.scanner.service.NVScannerProcessingService;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;

@Path("/ms/NVScanner")
@Service("NVScannerProcessingMSRestImpl")
@Produces(MediaType.APPLICATION_JSON)
public class NVScannerProcessingMSRestImpl {

	private static Logger logger = LogManager.getLogger(NVScannerProcessingRestImpl.class);

	@Autowired
	NVScannerProcessingService scannerService;

	@GET
	@Path("getScannerDump")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getCSVDumpFromHDFS(@QueryParam("workorderId") Integer workorderId,
			@QueryParam("recipeId") Integer recipeId) {
		try {
			byte[] fileContent = scannerService.getScannerDumpDataFromHbase(workorderId, recipeId);
			String outputFileName = ConfigUtils.getString(NVConfigUtil.SCANNER_CSV_FILE_NAME);
			outputFileName = outputFileName + ForesightConstants.UNDERSCORE + workorderId
					+ ForesightConstants.UNDERSCORE + recipeId + ForesightConstants.CSV_EXTENSION;

			Response.ResponseBuilder builder = Response.status(200);

			builder = builder.entity(fileContent)
					.header(ForesightConstants.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM)
					.header(ForesightConstants.CONTENT_DISPOSITION,
							NVConstant.ATTACHMENT_FILE_NAME + outputFileName);
			return builder.build();
		} catch (Exception e) {
			logger.error("Exception while download scanner CSV dump {}", ExceptionUtils.getStackTrace(e));
		}

		return Response.ok(NVWorkorderConstant.FILE_NOT_FOUND).build();
	}
}

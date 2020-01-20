package com.inn.foresight.module.nv.rest.impl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.HttpResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.commons.Symbol;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.lang.StringUtils;
import com.inn.foresight.core.generic.utils.ConfigEnum;
import com.inn.foresight.module.nv.inbuilding.utils.NVBuildingUtils;
import com.inn.foresight.module.nv.layer3.constants.QMDLConstant;
import com.inn.foresight.module.nv.layer3.service.ILayer3PPEService;
import com.inn.foresight.module.nv.layer3.utils.NVLayer3Utils;
import com.inn.foresight.module.nv.report.utils.ReportConstants;
import com.inn.foresight.module.nv.rest.NVFilesDownloadUploadRest;

@Path("/NVFilesDownloadUpload")
@Produces("application/json")
@Consumes("application/json")
@Service("NVFilesDownloadUploadRestImpl")
public class NVFilesDownloadUploadRestImpl extends NVLayer3Utils implements NVFilesDownloadUploadRest {

	/** The logger. */
	Logger logger = LogManager.getLogger(NVFilesDownloadUploadRestImpl.class);


	@Autowired
	private ILayer3PPEService iLayer3PPEService;



	@POST
	@Path("/createSignalMessageRecipeWiseCsv")
	@Override
	public Response createSignalMessageRecipeWiseCsv(@QueryParam(WORKORDER_ID) Integer workorderId,
			Map<String, List<String>> map, @Context HttpServletRequest request,
			@QueryParam(WORKORDER_NAME) String workorderName) {
		return iLayer3PPEService.createSignalMessageRecipeWiseCsvMicroServiceUrl(workorderId, map, request);

	}


	@POST
	@Path("/processWOReportDump")
	@Override
	public Response processWOReportDump(@QueryParam(WORKORDER_ID) Integer workorderId, Map<String, List<String>> map,
			@Context HttpServletRequest request) {
		return iLayer3PPEService.processWOReportDumpMicroServiceUrl(workorderId, map, request);
	}


	@POST
	@Path("/getDriveDetailForStatisticDataXls")
	@Override
	public Response getDriveDetailForStatisticDataXls(@QueryParam(WORKORDER_ID) Integer workorderId,
			@QueryParam(FILE_NAME) String fileName, @QueryParam("isInBuilding") Boolean isInBuilding,
			Map<String, List<String>> map, @Context HttpServletRequest request) {
		return iLayer3PPEService.getDriveDetailForStatisticDataXlsMicroServiceUrl(workorderId, map, request);
	}


	@GET
	@Path("/getLogFileForRecipe")
	@Override
	public Response getLogFileForRecipe(@QueryParam(FILE_PATH) String recipeId,@QueryParam(FILE_ID) String fileId, @Context HttpServletRequest request) {
		try {
			HttpResponse response = NVBuildingUtils.sendGetRequestWithoutTimeOut(
					getDropwizardUrlForWoReport(request, ConfigUtils.getString(ConfigEnum.MICRO_SERVICE_BASE_URL)
							+ ConfigUtils.getString(DROPWIZARD_RECIPE_LOG_FILE_DOWNLOAD_URL, Boolean.TRUE)));
			Response.ResponseBuilder builder = Response.status(200);
			builder = builder.entity(response.getEntity().getContent())
					.header(ReportConstants.CONTENT_TYPE, response.getEntity().getContentType())
					.header(ReportConstants.CONTENT_DISPOSITION,
							response.getFirstHeader(ReportConstants.CONTENT_DISPOSITION).getValue());
			return builder.build();
		} catch (Exception e) {
			logger.error("Exception in getLogFileForRecipe : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(QMDLConstant.EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
		}
	}


	private String getDropwizardUrlForWoReport(HttpServletRequest request, String url) {
		String queryString = request.getQueryString();
		logger.info("Getting url {}  query {} ", url, queryString);
		if (StringUtils.isNotEmpty(queryString)) {
			url += Symbol.QUESTION_MARK_STRING + queryString;
		}
		logger.info("Getting url {}", url);
		return url;
	}

}

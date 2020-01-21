package com.inn.foresight.core.report.rest.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.HttpResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.inn.commons.Symbol;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.lang.StringUtils;
import com.inn.foresight.core.generic.utils.ConfigEnum;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.report.utils.ReportUtils;

@Path("/ReportGeneration")
@Produces("application/json")
@Consumes("application/json")
@Service("ReportGenerationRestImpl")
public class ReportGenerationRestImpl  {
	
	private Logger logger = LogManager.getLogger(ReportGenerationRestImpl.class);

	@GET
	@Path("/getReportById/{analyticsrepositoryId}")
	public Response getReportById(@PathParam("analyticsrepositoryId") Integer analyticsRepositoryId,
			@Context HttpServletRequest request) {
		logger.info("Inside method getReportById with input analyticsRepositoryId {} ", analyticsRepositoryId);
		try {
			HttpResponse response = ReportUtils
					.sendGetRequestWithoutTimeOut(getDropwizardUrl(analyticsRepositoryId, request.getQueryString()));
			Response.ResponseBuilder builder = Response.status(200);
			builder = builder.entity(response.getEntity().getContent())
					.header(ForesightConstants.CONTENT_TYPE, response.getEntity().getContentType())
					.header(ForesightConstants.CONTENT_DISPOSITION,
							response.getFirstHeader(ForesightConstants.CONTENT_DISPOSITION).getValue());
			return builder.build();

		} catch (Exception e) {
			logger.error("Exception in getReportById : {}", ExceptionUtils.getStackTrace(e));
		}
		return Response.ok(ForesightConstants.FAILURE_JSON).build();

	}

	private String getDropwizardUrl(Integer rowkey, String queryString) {
		String finalUrl = ConfigUtils.getString(ConfigEnum.MICRO_SERVICE_BASE_URL.getValue())
				.concat(ConfigUtils.getString(ConfigEnum.DROPWIZARD_REPORT_URL.getValue())).concat(rowkey.toString());
		if (StringUtils.isNotEmpty(queryString)) {
			finalUrl += Symbol.QUESTION_MARK_STRING + queryString;
		}
		return finalUrl;
	}
	
	@GET
	@Path("/getReportByPath")
	public Response getReportByPath(@QueryParam("filePath") String filePath,@QueryParam("downloadFileName") String downloadFileName,@QueryParam("extension") String extension,
			@Context HttpServletRequest request) {
		logger.info("Inside method getReportByPath with input filePath {} ", filePath);
		try {
			String finalUrl = ConfigUtils.getString(ConfigEnum.MICRO_SERVICE_BASE_URL.getValue())
					.concat(ConfigUtils.getString(ConfigEnum.DROPWIZARD_GET_FILE_BY_PATH.getValue()));
			if (StringUtils.isNotEmpty(request.getQueryString())) {
				finalUrl += Symbol.QUESTION_MARK_STRING + request.getQueryString();
			}
			HttpResponse response = ReportUtils
					.sendGetRequestWithoutTimeOut(finalUrl);
			Response.ResponseBuilder builder = Response.status(200);
			builder = builder.entity(response.getEntity().getContent())
					.header(ForesightConstants.CONTENT_TYPE, response.getEntity().getContentType())
					.header(ForesightConstants.CONTENT_DISPOSITION,
							response.getFirstHeader(ForesightConstants.CONTENT_DISPOSITION).getValue());
			return builder.build();
		} catch (Exception e) {
			logger.error("Exception in getReportByPath : {}", ExceptionUtils.getStackTrace(e));
		}
		return Response.ok(ForesightConstants.FAILURE_JSON).build();

	}

}

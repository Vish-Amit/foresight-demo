package com.inn.foresight.core.report.rest.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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

import com.amazonaws.util.StringUtils;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.http.HttpGetRequest;
import com.inn.commons.http.HttpRequest;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ConfigEnum;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.report.model.AnalyticsRepository;
import com.inn.foresight.core.report.service.IAnalyticsRepositoryService;
import com.inn.foresight.core.report.utils.ReportUtils;
import com.inn.product.security.authentication.GenerateReportURL;
import com.inn.product.um.user.model.User;
import com.inn.product.um.user.service.impl.UserContextServiceImpl;

@Path("/AnalyticsRepository")
@Produces("application/json")
@Consumes("application/json")
@Service("AnalyticsRepositoryRestImpl")
public class AnalyticsRepositoryRestImpl {

	private Logger logger = LogManager.getLogger(AnalyticsRepositoryRestImpl.class);
	
	@Autowired
	private IAnalyticsRepositoryService service;
	
	@GET
	@Path("getReport/{instanceId}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getReport(@PathParam(ForesightConstants.INSTANCE_ID) Integer rowkey) {
		logger.info("Going to get report data for rowKey {}", rowkey);
		try {
			HttpResponse response = ReportUtils.sendGetRequestWithoutTimeOut(getDropwizardUrl(rowkey));

			Response.ResponseBuilder builder = Response.status(200);

			builder = builder.entity(response.getEntity().getContent())
					.header(ForesightConstants.CONTENT_TYPE, response.getEntity().getContentType())
					.header(ForesightConstants.CONTENT_DISPOSITION,
							response.getFirstHeader(ForesightConstants.CONTENT_DISPOSITION).getValue());

			return builder.build();

		} catch (Exception e) {
			logger.error("Exception in getNVReport : {}", ExceptionUtils.getStackTrace(e));

		}
		return null;
	}

	private String getDropwizardUrl(Integer rowkey) {
		return ConfigUtils.getString(ConfigEnum.DROPWIZARD_URL.getValue()).concat(rowkey.toString());

	}
	
	@GET
	@Path("/getReportById/{analyticsrepositoryId}")	
	public Response getReportById(@PathParam("analyticsrepositoryId") Integer analyticsRepositoryId){
		logger.info("Inside method getReportById with input analyticsRepositoryId {} ",analyticsRepositoryId);
		try {
			HttpResponse response = ReportUtils.sendGetRequestWithoutTimeOut(getDropwizardUrl(analyticsRepositoryId));
			return Response.ok(String.format(ForesightConstants.FILE_PATH_JSON, Utils.saveAndGetFilePath(response, 
					ConfigUtils.getString(ConfigEnum.NV_REPORT_BASE_PATH.getValue())))).build();
		} catch (Exception e) {
			logger.error("Exception in getReportById : {}", ExceptionUtils.getStackTrace(e));

		}
		return Response.ok(ForesightConstants.FAILURE_JSON).build();

	}
	
	@POST
	@Path("createCustomReportRepository")
	public AnalyticsRepository createCustomReportRepository(@QueryParam("templateId")Integer templateId, AnalyticsRepository repository){
		logger.info("Inside  @class :AnalyticsRepositoryRestImpl @Method :createCustomReportRepository,report name "+repository.getName());
		try {
			return service.createCustomReportRepository(templateId,repository);
		}catch(Exception ex) {
			logger.error("Error  occurred  @class: AnalyticsRepositoryRestImpl @method: createRepository ", ex.getMessage());
			throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}
		
	@POST
	@Path("hdfsFileUpload")
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	public String hdfsFileUpload(@Context HttpServletRequest request) {
		logger.info("Inside rest ==> Going to get report data for http request {}", request);

		User user = UserContextServiceImpl.getUserInContext();
		if (user != null) {
			Integer userid = user.getUserid();
			if (userid != null) {
				logger.info("uploading file converted file");
				return service.hdfsFileUpload(request, user);
			}
		} else {
			throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
		return null;
	}
	
	@GET
	@Path("getRequestedReport/{instanceId}")
	public Map<String, String> getRequestedReport(@PathParam(ForesightConstants.INSTANCE_ID) Integer rowkey) {
		logger.info("Going to get report data for rowKey {}", rowkey);
		 Map<String,String> map=new HashMap<>();
		try {
			HttpRequest http = new HttpGetRequest(
					ConfigUtils.getString(ConfigEnum.MICRO_SERVICE_BASE_URL.getValue())+ConfigUtils.getString(ConfigEnum.DROPWIZARD_URL_FOR_REQUESTED_REPORT.getValue()).concat(rowkey.toString()));
			http.setEnableTimeout(false);
			 String response=http.getString();
			
			if(!response.equalsIgnoreCase(ForesightConstants.FAILURE_JSON)){
				logger.info("response @getRequestedReport {}",response);
				map.put("filePath", new GenerateReportURL(response).toUriString());
		    	return map;
			}
		} catch (Exception e) {
			logger.error("Error while getRequestedReport report {}", ExceptionUtils.getStackTrace(e));
		}
		map.put("result",ForesightConstants.FAILURE.toLowerCase());
		return 	map;
	}


	   @GET
	   @Path("getReportInfoByName")
	   @Produces("application/json")
	  public List<AnalyticsRepository> getReportInfoByName(@QueryParam("FileName") String FileName) throws Exception {
			logger.info("Going to get Filename", FileName);
			if (StringUtils.isNullOrEmpty(FileName)) {
		throw new RestException("Error in fetching details");
			}
			return service.getReportInfoByName(FileName);
	   }
	  }
	


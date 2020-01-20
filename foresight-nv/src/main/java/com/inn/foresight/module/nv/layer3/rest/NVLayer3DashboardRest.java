package com.inn.foresight.module.nv.layer3.rest;

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
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.inn.commons.Symbol;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.http.HttpGetRequest;
import com.inn.commons.lang.MapUtils;
import com.inn.commons.lang.StringUtils;
import com.inn.commons.unit.Duration;
import com.inn.foresight.core.generic.utils.ConfigEnum;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.NVConstant;
import com.inn.foresight.module.nv.inbuilding.utils.NVBuildingUtils;
import com.inn.foresight.module.nv.layer3.constants.NVLayer3Constants;
import com.inn.foresight.module.nv.layer3.constants.QMDLConstant;
import com.inn.foresight.module.nv.layer3.service.INVLayer3DashboardService;
import com.inn.foresight.module.nv.layer3.utils.NVLayer3Utils;
import com.inn.foresight.module.nv.report.utils.ReportConstants;
import com.inn.foresight.module.nv.workorder.recipe.wrapper.RecipeWrapper;

/**
 * The Class NVLayer3DashboardRest.
 *
 * @author innoeye date - 08-Jan-2018 1:56:40 PM
 */
@Path("/NVLayer3Dashboard")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NVLayer3DashboardRest extends NVLayer3Utils {

	/** The logger. */
	private Logger logger = LogManager.getLogger(NVLayer3DashboardRest.class);

	@Autowired
	private INVLayer3DashboardService nvLayer3Service;

	

	/**
	 * Gets the NVLayer3 Signal Param Stats data for given workOrder FileDetail
	 * instances.
	 *
	 * @param filters
	 *            the KPI & fileIds whose data to serve
	 * @param request
	 *            the HttpServletRequest
	 * @return the dashboard data
	 */
	@POST
	@Path("/getSignalParamStats")
	public Response getSignalParamStats(Map<String, List<String>> filters, @Context HttpServletRequest request) {
		try {
			logger.info("Going to get NVLayer3 Signal Param Stats");
			if (MapUtils.isNotEmpty(filters)) {
				String json = new Gson().toJson(filters);
				StringEntity httpEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
				Duration duration = Duration
						.minutes(Integer.parseInt(ConfigUtils.getString(ConfigEnum.TIMEOUT_VALUE.getValue())));
				String result = NVLayer3Utils.sendHttpPostRequest(getDropwizardUrl(request), httpEntity, true, duration)
						.getString();
				logger.info("Done at get NVLayer3 Signal Param Stats");
				return Response.ok(result).build();
			} else {
				logger.error("Filters value is null");
				return Response.ok(INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error("Exception in getSignalParamStats : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(EXCEPTION_SOMETHING_WENT_WRONG).build();
		}
	}

	/**
	 * Gets the drive summary.
	 *
	 * @param workrorderId
	 *            the workrorder id
	 * @param request
	 *            the HttpServletRequest
	 * @return the drive summary
	 */
	@GET
	@Path("/getDriveSummary")
	public Response getDriveSummary(@QueryParam(WORKORDER_ID) String workrorderId,
			@Context HttpServletRequest request) {
		logger.info("Going to get Drive Summary for workrorderId {}", workrorderId);
		try {
			Duration duration = Duration
					.minutes(Integer.parseInt(ConfigUtils.getString(ConfigEnum.TIMEOUT_VALUE.getValue())));
			HttpGetRequest result = NVLayer3Utils.sendHttpGetRequest(getDropwizardUrl(request), true, duration);
			return Response.ok(result.getString()).build();
		} catch (Exception e) {
			logger.error("Exception in getDriveSummary : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(EXCEPTION_SOMETHING_WENT_WRONG).build();
		}
	}

	/**
	 * Gets the NVLayer3 Signal Param data for given workOrderId with pagination.
	 *
	 * @param workrorderId
	 *            the workrorder id
	 * @param request
	 *            the HttpServletRequest
	 * @return the Signal Param data
	 */
	@GET
	@Path("/getDriveDetailData")
	public Response getDriveDetailData(@QueryParam(WORKORDER_ID) String workrorderId,
			@Context HttpServletRequest request) {
		logger.info("Going to get Drive getDriveDetailData for workrorderId {}", workrorderId);
		try {
			Duration duration = Duration
					.minutes(Integer.parseInt(ConfigUtils.getString(ConfigEnum.TIMEOUT_VALUE.getValue())));
			HttpGetRequest result = NVLayer3Utils.sendHttpGetRequest(getDropwizardUrl(request), true, duration);
			return Response.ok(result.getString()).build();
		} catch (Exception e) {
			logger.error("Exception in getDriveDetailData : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(EXCEPTION_SOMETHING_WENT_WRONG).build();
		}

	}

	/**
	 * Gets the signal message data.
	 *
	 * @param workorderId
	 *            the workorder id
	 * @param timeStamp
	 *            the time stamp
	 * @param direction
	 *            the direction
	 * @param request
	 *            the HttpServletRequest
	 * @return the signal message data
	 */
	@GET
	@Path("/getSignalMessageData")
	public Response getSignalMessageData(@QueryParam(WORKORDER_ID) String workorderId,
			@QueryParam(QMDLConstant.TIMESTAMP) Long timeStamp, @QueryParam(QMDLConstant.DIRECTION) String direction,
			@Context HttpServletRequest request) {
		logger.info("Going to getSignalMessageData for workrorderId {}  TIMESTAMP {} direction {}   ", workorderId,
				timeStamp, direction);
		try {
			Duration duration = Duration
					.minutes(Integer.parseInt(ConfigUtils.getString(ConfigEnum.TIMEOUT_VALUE.getValue())));
			HttpGetRequest result = NVLayer3Utils.sendHttpGetRequest(getDropwizardUrl(request), true, duration);
			return Response.ok(result.getString()).build();
		} catch (Exception e) {
			logger.error("Exception in getSignalMessageData : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(EXCEPTION_SOMETHING_WENT_WRONG).build();
		}
	}

	/**
	 * Gets the signal message detail.
	 *
	 * @param rowKey
	 *            the row key
	 * @param msgType
	 *            the msg type
	 * @param request
	 *            the HttpServletRequest
	 * @return the signal message detail
	 */
	@GET
	@Path("/getSignalMessageDetail")
	public Response getSignalMessageDetail(@QueryParam(QMDLConstant.ROWKEY) String rowKey,
			@QueryParam(QMDLConstant.MSG_TYPE) String msgType, @Context HttpServletRequest request) {
		logger.info("Going to getSignalMessageDetail for rowKey {} msgType {}   ", rowKey, msgType);
		try {
			Duration duration = Duration
					.minutes(Integer.parseInt(ConfigUtils.getString(ConfigEnum.TIMEOUT_VALUE.getValue())));
			HttpGetRequest result = NVLayer3Utils.sendHttpGetRequest(getDropwizardUrl(request), true, duration);
			return Response.ok(result.getString()).build();
		} catch (Exception e) {
			logger.error("Exception in getSignalMessageDetail : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(EXCEPTION_SOMETHING_WENT_WRONG).build();
		}
	}

	/**
	 * Gets the kpi stats data.
	 *
	 * @param workorderId
	 *            the workorder id
	 * @param kpi
	 *            the kpi
	 * @param request
	 *            the HttpServletRequest
	 * @return the kpi stats data
	 */
	@GET
	@Path("/getKpiStatsData")
	public Response getKpiStatsData(@QueryParam(WORKORDER_ID) String workorderId,
			@QueryParam(QMDLConstant.KPI) String kpi, @Context HttpServletRequest request) {
		logger.info("Going to getKpiStatsData for workorderId {} kpi {}   ", workorderId, kpi);
		try {
			Duration duration = Duration
					.minutes(Integer.parseInt(ConfigUtils.getString(ConfigEnum.TIMEOUT_VALUE.getValue())));
			HttpGetRequest result = NVLayer3Utils.sendHttpGetRequest(getDropwizardUrl(request), true, duration);
			return Response.ok(result.getString()).build();
		} catch (Exception e) {
			logger.error("Exception in getKpiStatsData : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(EXCEPTION_SOMETHING_WENT_WRONG).build();
		}
	}

	/**
	 * Gets the dropwizard url.
	 *
	 * @param request
	 *            the request
	 * @return the dropwizard url
	 */
	private String getDropwizardUrl(HttpServletRequest request) {
		String url = ConfigUtils.getString(ConfigEnum.MICRO_SERVICE_BASE_URL, Boolean.TRUE)+ConfigUtils.getString(ConfigEnum.DROPWIZARD_NVDASHBOARD_DATA_SERVICE_URL, Boolean.TRUE);
		String requestStr = request.getRequestURI();
		String queryString = request.getQueryString();
		logger.info("Getting url {}  query {}  request {}", url, queryString, requestStr);
		requestStr = requestStr.substring(requestStr.lastIndexOf(Symbol.SLASH_FORWARD), requestStr.length());
		url += requestStr;
		if (StringUtils.isNotEmpty(queryString)) {
			url += Symbol.QUESTION_MARK_STRING + queryString;
		}
		logger.info("Getting url {}", url);
		return url;
	}

	/**
	 * Gets the Drive Recipe Detail.
	 *
	 * @param workrorderId
	 *            the workrorder id
	 * @return the Drive Recipe Detail
	 */
	@GET
	@Path("/getDriveRecipeDetail")
	public Response getDriveRecipeDetail(@QueryParam(WORKORDER_ID) Integer workrorderId,
			@Context HttpServletRequest request) {
		logger.info("Going to getDriveRecipeDetail for workrorderId {}", workrorderId);
		try {
			String result = nvLayer3Service.getDriveRecipeDetailForLayer3(workrorderId);
			return Response.ok(result).build();
		} catch (Exception e) {
			logger.error("Exception in getDriveRecipeDetail : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(EXCEPTION_SOMETHING_WENT_WRONG).build();
		}
	}

	
	@GET
	@Path("/getRecipeDetail")
	public Response getRecipeDetail(@QueryParam(WORKORDER_ID) Integer workrorderId,
			@Context HttpServletRequest request) {
		logger.info("Going to getDriveRecipeDetail for workrorderId {}", workrorderId);
		try {
			Map<String, List<RecipeWrapper>> result = nvLayer3Service.getRecipeMappingWrapperByWOId(workrorderId);
			return Response.ok(result).build();
		} catch (Exception e) {
			logger.error("Exception in getDriveRecipeDetail : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(EXCEPTION_SOMETHING_WENT_WRONG).build();
		}
	}

	
	@POST
	@Path("/getDriveDetailForStatisticDataXls")
	public Response getKpiStatisticsXls(@QueryParam(WORKORDER_ID) Integer workorderId,@QueryParam(FILE_NAME) String fileName,
			@QueryParam("isInBuilding")Boolean isInBuilding,	Map<String, List<String>> map,	@Context HttpServletRequest request) {
		try {
			if (workorderId != null && map != null) {
				logger.info("Going to get NVLayer3 getDriveDetailForStatisticDataXls Data for workorderId {} map {}  ",
						workorderId, map);
				Duration duration = Duration
						.minutes(Integer.parseInt(ConfigUtils.getString(ConfigEnum.TIMEOUT_VALUE.getValue())));
				String json = new Gson().toJson(map);
				StringEntity httpEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
				String response = NVLayer3Utils
						.sendHttpPostRequest(getDropwizardUrl(request), httpEntity, true, duration).getString();
				return Response.ok(response).build();
			} else {
				logger.error(QMDLConstant.INVALID_PARAMETER);
				return Response.ok(INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error("Exception in getDriveDetailForStatisticDataXls : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(QMDLConstant.EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
		}
	}

	@POST
	@Path("/getDriveSummaryReceipeWise")
	public Response getDriveSummaryReceipeWise(@QueryParam(WORKORDER_ID) Integer workorderId,
			Map<String, List<String>> map, @Context HttpServletRequest request) {
		try {
			if (workorderId != null && map != null) {
				logger.info("Going to get NVLayer3 getDriveSummaryReceipeWise Data for workorderId {} map {}  ",
						workorderId, map);
				Duration duration = Duration
						.minutes(Integer.parseInt(ConfigUtils.getString(ConfigEnum.TIMEOUT_VALUE.getValue())));
				String json = new Gson().toJson(map);
				StringEntity httpEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
				String response = NVLayer3Utils
						.sendHttpPostRequest(getDropwizardUrl(request), httpEntity, true, duration).getString();
				return Response.ok(response).build();
			} else {
				logger.error(QMDLConstant.INVALID_PARAMETER);
				return Response.ok(INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error("Exception in getDriveSummaryReceipeWise : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(QMDLConstant.EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
		}
	}

	@POST
	@Path("/getDriveDetailReceipeWise")
	public Response getDriveDetailReceipeWise(@QueryParam(WORKORDER_ID) Integer workorderId,
			Map<String, List<String>> map, @Context HttpServletRequest request) {
		try {
			if (workorderId != null && map != null) {
				logger.info("Going to get NVLayer3 getDriveDetailReceipeWise Data for workorderId {} map {}  ",
						workorderId, map);
				Duration duration = Duration
						.minutes(Integer.parseInt(ConfigUtils.getString(ConfigEnum.TIMEOUT_VALUE.getValue())));
				String json = new Gson().toJson(map);
				StringEntity httpEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
				String response = NVLayer3Utils
						.sendHttpPostRequest(getDropwizardUrl(request), httpEntity, true, duration).getString();
				return Response.ok(response).build();
			} else {
				logger.error(QMDLConstant.INVALID_PARAMETER);
				return Response.ok(INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error("Exception in getDriveDetailReceipeWise : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(QMDLConstant.EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
		}
	}

	
	@POST
	@Path("/getNeighbourDataRecipeWise")
	public Response getNeighbourDataRecipeWise(@QueryParam(WORKORDER_ID) Integer workorderId,
			Map<String, List<String>> map, @Context HttpServletRequest request) {
		try {
			if (workorderId != null && map != null) {
				logger.info("Going to get Neighbour Data RecipeWise  for workorderId {} map {}  ",
						workorderId, map);
				Duration duration = Duration
						.minutes(Integer.parseInt(ConfigUtils.getString(ConfigEnum.TIMEOUT_VALUE.getValue())));
				String json = new Gson().toJson(map);
				StringEntity httpEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
				String response = NVLayer3Utils
						.sendHttpPostRequest(getDropwizardUrl(request), httpEntity, true, duration).getString();
				return Response.ok(response).build();
			} else {
				logger.error(QMDLConstant.INVALID_PARAMETER);
				return Response.ok(INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error("Exception in getNeighbourDataRecipeWise : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(QMDLConstant.EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
		}
	}

	
	
	
	@POST
	@Path("/processWOReportDump")
	public Response processWOReportDump(@QueryParam(WORKORDER_ID) Integer workorderId, Map<String, List<String>> map,
			@Context HttpServletRequest request) {
		try {
			if (workorderId != null && map != null) {
				String json = new Gson().toJson(map);
				StringEntity httpEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
				HttpResponse response = NVLayer3Utils.sendPostRequestWithoutTimeOut(getDropwizardUrl(request), httpEntity);
				return Response
						.ok(String
								.format(ForesightConstants.FILE_PATH_JSON,
										Utils.saveAndGetFilePath(response,
												ConfigUtils.getString(ConfigEnum.NV_REPORT_BASE_PATH.getValue()))))
						.build();

			} else {
				logger.error(QMDLConstant.INVALID_PARAMETER);
				return Response.ok(INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error("Exception in processWOReportDump : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(QMDLConstant.EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
		}
	}

	@GET
	@Path("/getWOReportDump")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getWOReportDump(@QueryParam(FILE_NAME) String fileName, @Context HttpServletRequest request) {
		try {
			HttpResponse response = NVBuildingUtils.sendGetRequestWithoutTimeOut(getDropwizardUrl(request));
			Response.ResponseBuilder builder = Response.status(NVConstant.TWO_HUNDRED);
			builder = builder.entity(response.getEntity().getContent())
					.header(ReportConstants.CONTENT_TYPE, response.getEntity().getContentType())
					.header(ReportConstants.CONTENT_DISPOSITION,
							response.getFirstHeader(ReportConstants.CONTENT_DISPOSITION).getValue());
			return builder.build();
		} catch (Exception e) {
			logger.error("Exception in getDriveDetailReceipeWise : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(QMDLConstant.EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
		}
	}

	@POST
	@Path("/getKpiStatsRecipeData")
	public Response getKpiStatsRecipeData(@QueryParam(WORKORDER_ID) Integer workorderId, Map<String, List<String>> map,
			@Context HttpServletRequest request) {
		try {
			if (workorderId != null && map != null) {
				logger.info("Going to get NVLayer3 getKpiStatsRecipeData Data for workorderId {} map {}  ", workorderId,
						map);
				Duration duration = Duration
						.minutes(Integer.parseInt(ConfigUtils.getString(ConfigEnum.TIMEOUT_VALUE.getValue())));
				String json = new Gson().toJson(map);
				StringEntity httpEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
				String response = NVLayer3Utils
						.sendHttpPostRequest(getDropwizardUrl(request), httpEntity, true, duration).getString();
				return Response.ok(response).build();
			} else {
				logger.error(QMDLConstant.INVALID_PARAMETER);
				return Response.ok(INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error("Exception in getKpiStatsRecipeData : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(QMDLConstant.EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
		}
	}
	
	
	@POST
	@Path("/getSignalMessageRecipeWise")
	public Response getSignalMessageRecipeWise(@QueryParam(WORKORDER_ID) Integer workorderId, Map<String, List<String>> map,
			@QueryParam(QMDLConstant.ROWKEY) String lastRowKey, @QueryParam(QMDLConstant.DIRECTION) String direction,
			@QueryParam(QMDLConstant.MESSAGE) String message,@Context HttpServletRequest request) {
		try {
			if (workorderId != null && map != null) {
				logger.info("Going to get NVLayer3 getSignalMessageRecipeWise Data for workorderId {} lastRowKey {}   direction {}  map {}  ", workorderId,
						lastRowKey,direction,map);
				Duration duration = Duration
						.minutes(Integer.parseInt(ConfigUtils.getString(ConfigEnum.TIMEOUT_VALUE.getValue())));
				String json = new Gson().toJson(map);
				StringEntity httpEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
				String response = NVLayer3Utils
						.sendHttpPostRequest(getDropwizardUrl(request), httpEntity, true, duration).getString();
				return Response.ok(response).build();
			} else {
				logger.error(QMDLConstant.INVALID_PARAMETER);
				return Response.ok(INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error("Exception in getSignalMessageRecipeWise : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(QMDLConstant.EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
		}
	}
	
	@POST
	@Path("/createSignalMessageRecipeWiseCsv")
	public Response createSignalMessageRecipeWiseCsv(@QueryParam(WORKORDER_ID) Integer workorderId, Map<String, List<String>> map,@Context HttpServletRequest request,@QueryParam(WORKORDER_NAME) String workorderName) {
		try {
			if (workorderId != null && map != null) {
				logger.info("Going to  createSignalMessageRecipeWiseCsv Data for workorderId {}  map {}  ", workorderId,
						map);
				Duration duration = Duration
						.minutes(Integer.parseInt(ConfigUtils.getString(ConfigEnum.TIMEOUT_VALUE.getValue())));
				String json = new Gson().toJson(map);
				StringEntity httpEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
				String response = NVLayer3Utils
						.sendHttpPostRequest(getDropwizardUrl(request), httpEntity, true, duration).getString();
				return Response.ok(response).build();
			} else {
				logger.error(QMDLConstant.INVALID_PARAMETER);
				return Response.ok(INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error("Exception in createSignalMessageRecipeWiseCsv : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(QMDLConstant.EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
		}
	
	}
	
	@GET
	@Path("/getSignalMessagesForBin")
	public Response getSignalMessagesForBin(@QueryParam(KEY_START_TIME) Long startTime,
			@QueryParam(KEY_END_TIME) Long endTime, @QueryParam(KEY_ROW_PREFIX) String rowPrefix, @Context HttpServletRequest request) {
		try {
			if (startTime != null && endTime != null && !StringUtils.isBlank(rowPrefix)) {
				logger.info("Going to get NVLayer3 getSignalMessageRecipeWise Data for startTime {} endTime {} rowPrefix {}  ",
						startTime,endTime,rowPrefix);
				Duration duration = Duration
						.minutes(Integer.parseInt(ConfigUtils.getString(ConfigEnum.TIMEOUT_VALUE.getValue())));
				HttpGetRequest result = NVLayer3Utils.sendHttpGetRequest(getDropwizardUrl(request), true, duration);
				return Response.ok(result.getString()).build();
			} else {
				logger.error(QMDLConstant.INVALID_PARAMETER);
				return Response.ok(INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error("Exception in getSignalMessagesForBin : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(QMDLConstant.EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
		}
	}
	
	@GET
	@Path("/getHbaseDataForTimeStamp")
	public Response getHbaseDataForTimeStamp(@QueryParam(KEY_START_TIME) Long startTime,
			@QueryParam(KEY_END_TIME) Long endTime, @QueryParam(KEY_IMEI) String imei, @Context HttpServletRequest request) {
		try {
			if (startTime != null && endTime != null && !StringUtils.isBlank(imei)) {
				logger.info("Going to get NVLayer3 getSignalMessageRecipeWise Data for startTime {} endTime {} imei {}  ",
						startTime,endTime,imei);
				Duration duration = Duration
						.minutes(Integer.parseInt(ConfigUtils.getString(ConfigEnum.TIMEOUT_VALUE.getValue())));
				HttpGetRequest result = NVLayer3Utils.sendHttpGetRequest(getDropwizardUrl(request), true, duration);
				return Response.ok(result.getString()).build();
			} else {
				logger.error(QMDLConstant.INVALID_PARAMETER);
				return Response.ok(INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error("Exception in getHbaseDataForTimeStamp : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(QMDLConstant.EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
		}
	}
	


	/**
	 * Gets the Dynamic Kpi And Events.
	 *
	 * @param workrorderId
	 *            the workrorder id
	 * 
	 * @return the list of kpi
	 */
	@POST
	@Path("/getDynamicKpiAndEvents")
	public Response getDynamicKpiAndEvents(@QueryParam(WORKORDER_ID) Integer workorderId,List<Integer> recipeList){
		try {
			if(workorderId != null && recipeList!=null && !recipeList.isEmpty()) {
				return Response.ok(nvLayer3Service.getDynamicKpiAndEvents(workorderId,recipeList)).build();
			}else {
				return Response.ok(NVLayer3Constants.INVALID_PARAMETER_JSON).build();			
			}
		}catch (Exception e) {
			logger.error("Error while creating getDynamicKpiAndEvents {}", e.getMessage());
			return Response.ok(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG).build();
		}
	}

	@POST
	@Path("/searchLayer3SignalMessage")
	public Response searchLayer3SignalMessage(@QueryParam(WORKORDER_ID) Integer workorderId,
			Map<String, List<String>> map, @QueryParam(KEY_SEARCH) String searchTerm, @Context HttpServletRequest request) {
		try {
			if (workorderId != null && map != null && searchTerm != null) {
				logger.info("Going to  search Layer3 Signal Message for workorderId {}  map {} searchTerm {} ", workorderId,
						map, searchTerm);

				Duration duration = Duration
						.minutes(Integer.parseInt(ConfigUtils.getString(ConfigEnum.TIMEOUT_VALUE.getValue())));
				String json = new Gson().toJson(map);
				StringEntity httpEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
				String response = NVLayer3Utils
						.sendHttpPostRequest(getDropwizardUrl(request), httpEntity, true, duration).getString();
				return Response.ok(response).build();
			} else {
				logger.error(QMDLConstant.INVALID_PARAMETER);
				return Response.ok(INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error("Exception in searchLayer3SignalMessage : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(QMDLConstant.EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
		}
	}
	
	
	@GET
	@Path("/getNeighbourForBin")
	public Response getNeighbourForBin(@QueryParam(KEY_START_TIME) Long startTime,@QueryParam(KEY_ROW_PREFIX) String rowPrefix, @Context HttpServletRequest request) {
		try {
			if (startTime != null  && !StringUtils.isBlank(rowPrefix)) {
				logger.info("Going to get NVLayer3 getNeighbourForBin Data for startTime {}  rowPrefix {}  ",
						startTime,rowPrefix);
				Duration duration = Duration
						.minutes(Integer.parseInt(ConfigUtils.getString(ConfigEnum.TIMEOUT_VALUE.getValue())));
				HttpGetRequest result = NVLayer3Utils.sendHttpGetRequest(getDropwizardUrl(request), true, duration);
				return Response.ok(result.getString()).build();
			} else {
				logger.error(QMDLConstant.INVALID_PARAMETER);
				return Response.ok(INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error("Exception in getSignalMessagesForBin : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(QMDLConstant.EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
		}
	}
	
	@POST
	@Path("/getDataFromHbase")
	public Response getDashboardDataFromHbase(@QueryParam(WORKORDER_ID) Integer workorderId,
			Map<String, List<String>> map, @Context HttpServletRequest request) {
		logger.info("Going to getDashboardDataFromHbase for workorderId :{} ", workorderId);
		try {
			if (Utils.isNotNull(workorderId)) {
				Duration duration = Duration
						.minutes(Integer.parseInt(ConfigUtils.getString(ConfigEnum.TIMEOUT_VALUE.getValue())));
				String json = new Gson().toJson(map);
				StringEntity httpEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
				String response = NVLayer3Utils
						.sendHttpPostRequest(getDropwizardUrl(request), httpEntity, true, duration).getString();
				return Response.ok(response).build();
			} else {
				return Response.ok(new Gson().toJson(INVALID_PARAMETER_JSON)).build();
			}
		} catch (Exception e) {
			logger.error("Exception in getDataFromHbase : {} ", ExceptionUtils.getStackTrace(e));
			return Response.ok(FAILURE_JSON).build();
		}
	}
		
	
	
	@GET
	@Path("/reprocessFile")
	public Response reprocessFile(@QueryParam(WORKORDER_ID) Integer workorderId,@QueryParam(QMDLConstant.RECEIPE_ID) Integer recipeId) {
		logger.info("Going to reprocess File  workorderId {} RECEIPE_ID {} ",workorderId,recipeId);

		try {
			if (Utils.hasValidValue(String.valueOf(workorderId)) || Utils.hasValidValue(String.valueOf(recipeId))) {
				return nvLayer3Service.updateDLFLogFileStatusToReprocess(workorderId, recipeId);

			} else {
				return Response.ok(INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error("Error while reprocessFile :  {}", ExceptionUtils.getMessage(e));
			return Response.ok(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG).build();
		}
	}
	
}

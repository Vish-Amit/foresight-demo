package com.inn.foresight.module.nv.layer3.rest;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.inn.commons.Symbol;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.lang.StringUtils;
import com.inn.core.generic.exceptions.application.BusinessException;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.NVConstant;
import com.inn.foresight.module.nv.layer3.constants.NVLayer3Constants;
import com.inn.foresight.module.nv.layer3.constants.QMDLConstant;
import com.inn.foresight.module.nv.layer3.service.INVLayer3DashboardService;
import com.inn.foresight.module.nv.layer3.service.parse.INVL3ParsingService;
import com.inn.foresight.module.nv.layer3.utils.NVLayer3Utils;
import com.inn.foresight.module.nv.layer3.wrapper.Layer3ReportWrapper;
import com.inn.foresight.module.nv.report.utils.ReportConstants;
@Path("/ms/NVLayer3Dashboard")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NVLayer3MicrosServiceDashboardRest extends NVLayer3Utils{


	
	/** The logger. */
	private Logger logger = LogManager.getLogger(NVLayer3DashboardRest.class);

	/** The nv layer 3 dashboard service. */

	@Autowired
	private INVLayer3DashboardService nvLayer3DashboardService;

	@Autowired
	private INVL3ParsingService nvParsingService;

	/**
	 * Instantiates a new NV layer 3 dashboard rest.
	 *
	 * @param nvLayer3DashboardService
	 *            the nv layer 3 dashboard service
	 */

	@GET
	@Path("/getSignalMessageDetail")
	public Response getSignalMessageDetail(@QueryParam(QMDLConstant.ROWKEY) String rowKey,
			@QueryParam(MSG_TYPE) String msgType) {
		try {
			if (StringUtils.isNotEmpty(rowKey) && StringUtils.isNotEmpty(msgType)) {
				logger.info("Going to get NVLayer3 Signal Message Detail for rowKey {} ,msgType {} ", rowKey, msgType);
				return Response.ok(nvLayer3DashboardService.getSignalMessageDetail(rowKey, msgType)).build();
			} else {
				logger.error(INVALID_PARAMETER);
				return Response.ok(INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error("Exception in getSignalMessageDetail : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
		}
	}

	@GET
	@Path("/getKpiStatsData")
	public Response getKpiStatsData(@QueryParam(WORKORDER_ID) Integer workorderId, @QueryParam(KPI) String kpi) {
		try {
			if (workorderId != null && StringUtils.isNotEmpty(kpi)) {
				logger.info("Going to get NVLayer3 getKpiStatsData Data for workorderId {}  kpi {} ", workorderId, kpi);
				String response = nvLayer3DashboardService.getKpiStatsData(workorderId, kpi);
				return Response.ok(response).build();
			} else {
				logger.error(INVALID_PARAMETER);
				return Response.ok(INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error("Exception in getKpiStats Data : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
		}
	}

	@GET
	@Path("/fileProcess")
	public Response processFile(@QueryParam(WORKORDER_ID) Integer workorderId,
			@QueryParam(QMDLConstant.RECEIPE_ID) Integer recipeId) {
	
		String isEnabled = ConfigUtils.getString(NVLayer3Constants.IS_LAYER3_FRAMEWORK_ENABLED);
		if (ReportConstants.TRUE.equalsIgnoreCase(isEnabled)) {
			return Response.ok(NVLayer3Constants.IS_LAYER3_FRAMEWORK_ENABLED+":::"+isEnabled).build();
		} else {

			logger.info("Going to process  fileProcessWorkorderWise workorderId {} RECEIPE_ID {} ", workorderId,
					recipeId);
			try {
				return Response.ok(nvParsingService.fileProcessWorkorderWise(workorderId, recipeId, null)).build();
			} catch (IOException e) {
				logger.error("Error in processing file {}  ", ExceptionUtils.getStackTrace(e));
				return Response.ok(QMDLConstant.QMDL_FILE_PROCESSING_ERROR_MSG).build();

			}
		}
	}


	@GET
	@Path("/fileProcessWorkorderWise")
	public Response fileProcessWorkorderWise(@QueryParam(WORKORDER_ID) Integer workorderId) {
		logger.info("Going to process  fileProcessWorkorderWise  {} ",workorderId);
		String isEnabled = ConfigUtils.getString(NVLayer3Constants.IS_LAYER3_FRAMEWORK_ENABLED);
		if (ReportConstants.TRUE.equalsIgnoreCase(isEnabled)) {
			return Response.ok(NVLayer3Constants.IS_LAYER3_FRAMEWORK_ENABLED+":::"+isEnabled).build();
		} else {				
			try {
				return Response.ok(nvParsingService.fileProcessWorkorderWise(workorderId, null,null)).build();
			} catch (IOException e) {
				logger.error("Error in fileProcessWorkorderWise  file {}  ",ExceptionUtils.getStackTrace(e));
				return	Response.ok(QMDLConstant.QMDL_FILE_PROCESSING_ERROR_MSG).build();

			}
		}

	}

	@POST
	@Path("/processWOReportDump")
	@Produces(MediaType.TEXT_PLAIN)
	public Response processWOReportDump(@QueryParam(WORKORDER_ID) Integer workorderId, Map<String, List<String>> map) {
		logger.info("Getting  workorderId {} map {} ",workorderId,map);
		Response response = null;
		try {
			if (workorderId != null && map != null) {
				List<String> recipeId = map.get(NVLayer3Constants.RECEIPE_ID);
				List<String> operatorList = map.get(NVLayer3Constants.OPERATOR);
				if(operatorList == null || operatorList.isEmpty()) {
					operatorList = nvLayer3DashboardService.getDriveRecipeDetail(workorderId).get(NVLayer3Constants.OPERATOR);
					logger.info("Operator list was null Getting operator for WO: {}",
							operatorList);
				}
				String fileName = nvLayer3DashboardService.processWOReportDump(workorderId, recipeId, operatorList);
				if (fileName != null) {
					return Response.ok(fileName).build();
				} else {
					logger.error("Exception in process WOReportDump : {}", "File not created");
					response = Response.ok(EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
				}
			}
		} catch (Exception e) {
			logger.error("Exception in processWOReportDump : {}", ExceptionUtils.getStackTrace(e));
			response = Response.ok(EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
		}
		return response;
	}


	@GET
	@Path("/getWOReportDump")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getWOReportDump(@QueryParam(FILE_NAME) String fileName) {
		Response response = null;
		try {
			
			
			if (fileName != null) {
				Layer3ReportWrapper wrapper = nvLayer3DashboardService.getWOReportDump(fileName);
				Response.ResponseBuilder builder = Response.status(200);
				builder = builder.entity(wrapper.getFile()).header("Content-Type", MediaType.APPLICATION_OCTET_STREAM)
						.header("Content-Disposition", "attachment; filename=\"" + wrapper.getFileName() + "\"");
				response = builder.build();
			}
		} catch (Exception e) {
			logger.error("Exception in processWOReportDump : {}", ExceptionUtils.getStackTrace(e));
			response = Response.ok(EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
		}
		return response;
	}

	@POST
	@Path("/getDriveSummaryReceipeWise")
	public Response getDriveSummaryReceipeWise(@QueryParam(WORKORDER_ID) Integer workorderId,
			Map<String, List<String>> map) {
		try {
			if (workorderId != null && map != null) {
				List<String> recipeId = map.get(NVLayer3Constants.RECEIPE_ID);
				List<String> operatorList = map.get(NVLayer3Constants.OPERATOR);
				logger.info(
						"Going to get NVLayer3 getDriveSummary Data for workorderId {} receipeId {}  operatorName  {}",
						workorderId, recipeId, operatorList);
				String response = nvLayer3DashboardService.getDriveSummaryReceipeWise(workorderId, recipeId,
						operatorList);
				return Response.ok(response).build();
			} else {
				logger.error(INVALID_PARAMETER);
				return Response.ok(INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error("Exception in getDriveSummary : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
		}
	}

	@POST
	@Path("/getDriveDetailReceipeWise")
	public Response getDriveDetailReceipeWise(@QueryParam(WORKORDER_ID) Integer workorderId,
			Map<String, List<String>> map) {
		try {
			if (workorderId != null && map != null) {
				List<String> recipeId = map.get(NVLayer3Constants.RECEIPE_ID);
				List<String> operatorList = map.get(NVLayer3Constants.OPERATOR);
				logger.info(
						"Going to get NVLayer3 getDriveDetailReceipeWise Data for workorderId {} receipeId {}  operatorName  {}",
						workorderId, recipeId, operatorList);
				String response = nvLayer3DashboardService.getDriveDetailReceipeWise(workorderId, recipeId,
						operatorList);
				return Response.ok(response).build();
			} else {
				logger.error(INVALID_PARAMETER);
				return Response.ok(INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error("Exception in getDriveDetailReceipeWise : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
		}
	}
	
	
	@POST
	@Path("/getNeighbourDataRecipeWise")
	public Response getNeighbourDataRecipeWise(@QueryParam(WORKORDER_ID) Integer workorderId,
			Map<String, List<String>> map) {
		try {
			if (workorderId != null && map != null) {
				List<String> recipeId = map.get(NVLayer3Constants.RECEIPE_ID);
				List<String> operatorList = map.get(NVLayer3Constants.OPERATOR);
				logger.info(
						"Going to get Neighbour Data RecipeWise for workorderId {} receipeId {}  operatorName  {}",
						workorderId, recipeId, operatorList);
				Map<String, String> response = nvLayer3DashboardService.getNeighbourDataRecipeWise(workorderId, recipeId,
						operatorList);
				return Response.ok(response).build();
			} else {
				logger.error(INVALID_PARAMETER);
				return Response.ok(INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error("Exception in getNeighbourDataRecipeWise : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
		}
	}

	@POST
	@Path("/getKpiStatsRecipeData")
	public Response getKpiStatsRecipeData(@QueryParam(WORKORDER_ID) Integer workorderId, Map<String, List<String>> map,
			@QueryParam(KPI) String kpi) {
		try {
			if (workorderId != null && StringUtils.isNotEmpty(kpi) && map != null) {
				List<String> recipeId = map.get(NVLayer3Constants.RECEIPE_ID);
				List<String> operatorList = map.get(NVLayer3Constants.OPERATOR);

				logger.info(
						"Going to get NVLayer3 getKpiStatsData Data for workorderId {} receipeId {}  operatorName  {} kpi {} ",
						workorderId, recipeId, operatorList, kpi);
				String response = nvLayer3DashboardService.getKpiStatsRecipeData(workorderId, kpi, recipeId,
						operatorList);
				return Response.ok(response).build();
			} else {
				logger.error(INVALID_PARAMETER);
				return Response.ok(INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error("Exception in getKpiStatsRecipeData : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
		}
	}


	@POST
	@Path("/getSignalMessageRecipeWise")
	public Response getSignalMessageRecipeWise(@QueryParam(WORKORDER_ID) Integer workorderId, Map<String, List<String>> map,
			@QueryParam(QMDLConstant.ROWKEY) String lastRowKey, @QueryParam(DIRECTION) String direction,@QueryParam(MESSAGE) String message) {
		try {
			if (StringUtils.isNotEmpty(direction) && workorderId!=null && map!=null) {
				List<String> recipeId = map.get(NVLayer3Constants.RECEIPE_ID);
				List<String> operatorList = map.get(NVLayer3Constants.OPERATOR);

				logger.info("Going to get NVLayer3 Signal Message Data for workorderId {} ,lastRowKey {} , direction {} recipeId {} operatorList {} message {}",
						workorderId, lastRowKey, direction,recipeId,operatorList,message);
				return Response.ok(nvLayer3DashboardService.getSignalMessageRecipeWise(workorderId, lastRowKey, direction,recipeId,operatorList,message))
						.build();
			} else {
				logger.error(INVALID_PARAMETER);
				return Response.ok(INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error("Exception in getSignalMessageData : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
		}
	}


	@POST
	@Path("/getKpiStatsRecipeDataForReport")
	public Response getKpiStatsRecipeDataForReport(@QueryParam(WORKORDER_ID) Integer workorderId, Map<String, List<String>> map,
			@QueryParam(KPI) String kpi) {
		try {
			if (workorderId != null && StringUtils.isNotEmpty(kpi) && map != null) {
				List<String> recipeId = map.get(NVLayer3Constants.RECEIPE_ID);
				List<String> operatorList = map.get(NVLayer3Constants.OPERATOR);

				logger.info(
						"Going to get NVLayer3 getKpiStatsRecipeDataForReport Data for workorderId {} receipeId {}  operatorName  {} kpi {} ",
						workorderId, recipeId, operatorList, kpi);
				String response = nvLayer3DashboardService.getKpiStatsRecipeDataForReport(workorderId, kpi, recipeId,
						operatorList);
				return Response.ok(response).build();
			} else {
				logger.error(INVALID_PARAMETER);
				return Response.ok(INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error("Exception in getKpiStatsData : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
		}
	}
	
	
	@POST
	@Path("/createSignalMessageRecipeWiseCsv")
	public Response createSignalMessageRecipeWiseCsv(@QueryParam(WORKORDER_ID) Integer workorderId, Map<String, List<String>> map,@QueryParam(WORKORDER_NAME) String workorderName) {
		if (workorderId != null && map != null&& workorderName!=null) {
			List<String> recipeId = map.get(NVLayer3Constants.RECEIPE_ID);
			List<String> operatorList = map.get(NVLayer3Constants.OPERATOR);
			logger.info("Going to get NVLayer3 createSignalMessageRecipeWise Data for workorderId {} receipeId {}  operatorName  {}  workorderName {}",
					workorderId, recipeId, operatorList,workorderName);
			try {
				return  Response.ok(nvLayer3DashboardService.createSignalMessageRecipeWise(workorderId,  recipeId,
						operatorList, workorderName)).build();
			} catch (BusinessException e) {
				logger.error("Error in generation Report {}  ",Utils.getStackTrace(e));
				return Response.ok(QMDLConstant.ERROR_MSG).build();
			}
		} else {
			logger.error(INVALID_PARAMETER);
			return Response.ok(INVALID_PARAMETER_JSON).build();
		}
	}
	

	@POST
	@Path("/getAggrigationDataByLatLong")
	public Response getAggrigationDataByLatLong(@QueryParam(WORKORDER_ID) Integer workorderId, Map<String, List<String>> map) {
		try {
			if (workorderId != null && map != null) {
				List<String> recipeId = map.get(NVLayer3Constants.RECEIPE_ID);
				List<String> operatorList = map.get(NVLayer3Constants.OPERATOR);

				logger.info(
						"Going to get NVLayer3 getAggrigationDataByLatLong Data for workorderId {} receipeId {}  operatorName  {} ",
						workorderId, recipeId, operatorList);
				String response = nvLayer3DashboardService.getAggrigationDataByLatLong(workorderId, recipeId,
						operatorList);
				return Response.ok(response).build();
			} else {
				logger.error(INVALID_PARAMETER);
				return Response.ok(INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error("Exception in getKpiStatsData : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
		}
	}
	
	@GET
	@Path("/getSignalMessagesForBin")
	public Response getSignalMessagesForBin(@QueryParam(KEY_START_TIME) Long startTime,
			@QueryParam(KEY_END_TIME) Long endTime, @QueryParam(KEY_ROW_PREFIX) String rowPrefix) {
		try {
			if (startTime != null && endTime != null && !StringUtils.isBlank(rowPrefix)) {
				logger.info(
						"Going to get Signal Messages for bin: startTime: {} endTime: {}  rowPrefix: {} ",
						startTime, endTime, rowPrefix);
				String response = nvLayer3DashboardService.getSignalMessagesForBin(startTime, endTime, rowPrefix);
				return Response.ok(response).build();
			}else{
				return Response.ok(QMDLConstant.INVALID_ARGUMENT).build();
			}
		} catch (Exception e) {
			logger.error("Exception in getSignalMessagesForBin : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
		}
	}
	
	@POST
	@Path("/searchLayer3SignalMessage")
	public Response searchLayer3SignalMessage(@QueryParam(WORKORDER_ID) Integer workorderId,
			Map<String, List<String>> map, @QueryParam(KEY_SEARCH) String searchTerm) {
		try {
			if (workorderId != null && map != null && searchTerm != null) {
				List<String> recipeId = map.get(NVLayer3Constants.RECEIPE_ID);
				List<String> operatorList = map.get(NVLayer3Constants.OPERATOR);
				searchTerm = searchTerm.toUpperCase();
				searchTerm = URLDecoder.decode(searchTerm , "UTF-8");
				logger.info("Going to get NVLayer3 Signal Message Data for workorderId {} recipeId {} operatorList {}  searchTerm {}",
						workorderId, recipeId, operatorList,searchTerm);
				return Response
						.ok(nvLayer3DashboardService.searchLayer3SignalMessage(workorderId, recipeId, operatorList, searchTerm))
						.build();
			} else {
				logger.error(INVALID_PARAMETER);
				return Response.ok(INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error("Exception in getSignalMessageData : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
		}
	}
	
	@GET
	@Path("/getNeighbourForBin")
	public Response getNeighbourForBin(@QueryParam(KEY_START_TIME) Long startTime,@QueryParam(KEY_ROW_PREFIX) String rowPrefix) {
		try {
			if (startTime != null && !StringUtils.isBlank(rowPrefix)) {
				logger.info(
						"Going to get Neighbour for bin: startTime: {}  rowPrefix: {} ",
						startTime, rowPrefix);
				String response = nvLayer3DashboardService.getNeighbourForBin(startTime, rowPrefix);
				return Response.ok(response).build();
			}else{
				return Response.ok(QMDLConstant.INVALID_ARGUMENT).build();
			}
		} catch (Exception e) {
			logger.error("Exception in getSignalMessagesForBin : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
		}
	}
	
	@GET
	@Path("/getWOReportDumpForStealth")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getWOReportDumpForStealth(@QueryParam(WORKORDER_ID) String workorderId,
			@QueryParam(TASK_ID) String taskId, @QueryParam(DATE) String date) {
		Response response = null;
		Response.ResponseBuilder builder = Response.status(200);
		String fileName=Symbol.EMPTY_STRING;
		try {
			if (taskId != null && workorderId != null && date != null) {
				Layer3ReportWrapper wrapper = nvLayer3DashboardService.getWOReportDumpForStealth(workorderId, taskId, date);
				 fileName =wrapper.getFileName();
				if (wrapper.getFile() != null && wrapper.getFileName() != null) {
					builder = builder	.entity(wrapper.getFile())
										.header(NVConstant.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM)
										.header(NVConstant.CONTENT_DISPOSITION,
												NVConstant.ATTACHMENT_FILE_NAME + fileName);
					response = builder.build();
				} else {
					builder = builder	.entity(DATA_NOT_FOUND_JSON)
										.header(NVConstant.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM)
										.header(NVConstant.CONTENT_DISPOSITION,
												"attachment; filename=" + fileName);
					response = builder.build();
				}
			}
		} catch (Exception e) {
			logger.error("Exception in getWOReportDumpForStealth : {}", ExceptionUtils.getStackTrace(e));
			builder = builder	.entity(EXCEPTION_SOMETHING_WENT_WRONG_JSON)
								.header(NVConstant.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM)
								.header(NVConstant.CONTENT_DISPOSITION,
										"attachment; filename=" + fileName);
			response = builder.build();
		}
		return response;
	}
	
	@POST
	@Path("/getDriveDetailForStatisticDataXls")
	public Response getKpiStatisticsXls(@QueryParam(WORKORDER_ID) Integer workorderId,@QueryParam(FILE_NAME) String fileName,@QueryParam("isInBuilding")Boolean isInBuilding ,
			Map<String, List<String>> map) {
		try {
			if (workorderId != null && map != null) {
				List<String> recipeId = map.get(NVLayer3Constants.RECEIPE_ID);
				List<String> operatorList = map.get(NVLayer3Constants.OPERATOR);
				List<String> kpiList = map.get(NVLayer3Constants.KPI_INDEXES);
				List<String> kpiNeighbourList = map.get(NVLayer3Constants.KPI_NEIGHBOUR_INDEXES);
			
				logger.info("kpiList{}", kpiList);
				logger.info(
						"Going to get NVLayer3 getDriveDetailReceipeWise Data for workorderId {} receipeId {}  operatorName  {}",
						workorderId, recipeId, operatorList);
				String response = nvLayer3DashboardService.getDriveDetailForStatisticData(kpiList, workorderId,
						recipeId, operatorList, fileName, isInBuilding,kpiNeighbourList);
				return Response.ok(response).build();
			} else {
				logger.error(INVALID_PARAMETER);
				return Response.ok(INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error("Exception in getDriveDetailReceipeWise : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
		}
	}
	
	@GET
	@Path("/getLogFileForRecipe")
	public Response getLogFileForRecipe(@QueryParam(RECIPE_ID) String recipeId, @QueryParam(FILE_ID) String fileId) {
		Response response = null;
		try {
			String filePath = nvLayer3DashboardService.getLogFileForRecipe(recipeId, fileId);

			if (filePath != null) {
				Map<String, String> responseMap = new HashMap<>();
				responseMap.put(FILE_PATH, filePath);				
				response = Response.ok(responseMap).build();
			} else {
				response = Response.ok(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
			}
		} catch (Exception e) {
			logger.error("Exception in getLogFileForRecipe : {}", Utils.getStackTrace(e));
			response = Response.ok(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
		}

		return response;

	}

}

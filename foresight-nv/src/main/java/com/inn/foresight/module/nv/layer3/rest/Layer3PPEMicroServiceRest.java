package com.inn.foresight.module.nv.layer3.rest;

import java.io.IOException;
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

import com.inn.commons.lang.StringUtils;
import com.inn.core.generic.exceptions.application.BusinessException;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.app.constants.AppConstants;
import com.inn.foresight.module.nv.layer3.constants.NVLayer3Constants;
import com.inn.foresight.module.nv.layer3.constants.QMDLConstant;
import com.inn.foresight.module.nv.layer3.service.ILayer3PPEService;
import com.inn.foresight.module.nv.layer3.utils.NVLayer3Utils;
import com.inn.foresight.module.nv.layer3.wrapper.Layer3PPEWrapper;

@Path("/ms/Layer3PPE")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class Layer3PPEMicroServiceRest extends NVLayer3Utils {

	private Logger logger = LogManager.getLogger(Layer3PPEMicroServiceRest.class);

	@Autowired
	private ILayer3PPEService iLayer3PPEService;

	@GET
	@Path("/getSignalMessageDetail")
	public Response getSignalMessageDetail(@QueryParam(QMDLConstant.ROWKEY) String rowKey,
			@QueryParam(MSG_TYPE) String msgType) {
		try {
			if (StringUtils.isNotEmpty(rowKey) && StringUtils.isNotEmpty(msgType)) {
				logger.info("Going to get NVLayer3 Signal Message Detail for rowKey {} ,msgType {} ", rowKey, msgType);
				return Response.ok(iLayer3PPEService.getSignalMessageDetail(rowKey, msgType)).build();
			} else {
				logger.error(INVALID_PARAMETER);
				return Response.ok(INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error("Exception in getSignalMessageDetail : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
		}
	}

	

	@POST
	@Path("/getSignalMessageRecipeWise")
	public Response getSignalMessageRecipeWise(@QueryParam(WORKORDER_ID) Integer workorderId,
			Map<String, List<String>> map, @QueryParam(QMDLConstant.ROWKEY) String lastRowKey,
			@QueryParam(DIRECTION) String direction, @QueryParam(MESSAGE) String message) {
		try {
			if (StringUtils.isNotEmpty(direction) && workorderId != null && map != null) {
				List<String> recipeId = map.get(NVLayer3Constants.RECIPE_ID);
				List<String> fileId = map.get(NVLayer3Constants.FILE_ID);

				
				logger.info(
						"Going to get NVLayer3 Signal Message Data for workorderId {} ,lastRowKey {} , direction {} recipeId {} message {}",
						workorderId, lastRowKey, direction, recipeId, message);
				return Response.ok(iLayer3PPEService.getSignalMessageRecipeWise(workorderId, lastRowKey, direction,
						recipeId, message,fileId)).build();
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
	@Path("/createSignalMessageRecipeWiseCsv")
	public Response createSignalMessageRecipeWiseCsv(@QueryParam(WORKORDER_ID) Integer workorderId,
			Map<String, List<String>> map, @QueryParam(WORKORDER_NAME) String workorderName) {
		if (workorderId != null && map != null && workorderName != null) {
			List<String> recipeId = map.get(NVLayer3Constants.RECIPE_ID);
			List<String> fileId=map.get(NVLayer3Constants.FILE_ID);
			
			
			logger.info(
					"Going to get NVLayer3 createSignalMessageRecipeWise Data for workorderId {} receipeId {}   workorderName {}",
					workorderId, recipeId, workorderName);
			try {
				return Response
						.ok(iLayer3PPEService.createSignalMessageRecipeWise(workorderId, recipeId,fileId, workorderName))
						.build();
			} catch (BusinessException e) {
				logger.error("Error in generation Report {}  ", Utils.getStackTrace(e));
				return Response.ok(QMDLConstant.ERROR_MSG).build();
			}
		} else {
			logger.error(INVALID_PARAMETER);
			return Response.ok(INVALID_PARAMETER_JSON).build();
		}
	}

	@GET
	@Path("/getSignalMessagesForBin")
	public Response getSignalMessagesForBin(@QueryParam(KEY_ROW_PREFIX) String rowPrefix) {
		try {
			if (rowPrefix != null && !StringUtils.isBlank(rowPrefix)) {
				logger.info("Going to get Signal Messages for bin: rowPrefix: {} ",
						 rowPrefix);
				String response = iLayer3PPEService.getSignalMessagesForBin(rowPrefix);
				return Response.ok(response).build();
			} else {
				return Response.ok(QMDLConstant.INVALID_ARGUMENT).build();
			}
		} catch (Exception e) {
			logger.error("Exception in getSignalMessagesForBin : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
		}
	}

	@POST
	@Path("/processWOReportDump")
	@Produces(MediaType.TEXT_PLAIN)
	public Response processWOReportDump(@QueryParam(WORKORDER_ID) Integer workorderId, Map<String, List<String>> map) {
		logger.info("Getting  workorderId {} map {} ", workorderId, map);
		Response response = null;
		try {
			if (workorderId != null && map != null) {
				List<String> recipeId = map.get(NVLayer3Constants.RECIPE_ID);
				List<String> fileId=map.get(NVLayer3Constants.FILE_ID);
				
				
				String fileName = iLayer3PPEService.processWOReportDump(workorderId, recipeId,fileId);
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
	
	
	@POST
	@Path("/getDriveDetailForStatisticDataXls")
	public Response getDriveDetailForStatisticDataXls(@QueryParam(WORKORDER_ID) Integer workorderId,@QueryParam(FILE_NAME) String fileName,@QueryParam("isInBuilding")Boolean isInBuilding ,
			Map<String, List<String>> map) {
		try {
			if (workorderId != null && map != null && map.get(NVLayer3Constants.RECIPE_ID) != null
					&& map.get(NVLayer3Constants.COLUMNS) != null) {
				List<String> recipeId = map.get(NVLayer3Constants.RECIPE_ID);
				List<String> fileId = map.get(NVLayer3Constants.FILE_ID);				
				List<String> hbaseColumns = map.get(NVLayer3Constants.COLUMNS);
				List<String> kpiNeighbourList = map.get(NVLayer3Constants.KPI_NEIGHBOUR_INDEXES);

				logger.info("Going to get NVLayer3 getDriveDetailReceipeWise Data for workorderId {} receipeId {}  hbaseColumns  {}",
						workorderId, recipeId, hbaseColumns);
				
				String response = iLayer3PPEService.getDriveDetailForStatisticData(workorderId,
						recipeId,fileId, hbaseColumns, fileName, isInBuilding,kpiNeighbourList);
				return Response.ok(response).build();
			} else {
				logger.error(INVALID_PARAMETER);
				return Response.ok(INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error("Exception in getDriveDetailForStatisticDataXls : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
		}
	}

	@POST
	@Path("/getPPEDataForMap")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPPEDataForMap(Layer3PPEWrapper wrapper, @QueryParam("tableName") String tableName) {
		try {
			List<String> columnNameList = wrapper.getColumnNameList();
			Map<String, List<String>> summaryMap = wrapper.getSummaryMap();
			String layerType = wrapper.getLayerType();
			return Response.ok(iLayer3PPEService.getPPEDataForMap(columnNameList,summaryMap,tableName,layerType)).build();
		} catch (Exception e) {
			logger.error("Exception while Getting PPE data for Map {}",ExceptionUtils.getStackTrace(e));
		}
		return Response.ok(AppConstants.FAILURE_JSON).build();
	}
}

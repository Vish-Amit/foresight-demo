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

import com.inn.foresight.module.nv.workorder.wrapper.WOFileDetailWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.inn.commons.http.HttpException;
import com.inn.foresight.module.nv.layer3.constants.NVLayer3Constants;
import com.inn.foresight.module.nv.layer3.constants.QMDLConstant;
import com.inn.foresight.module.nv.layer3.model.Layer3MetaData;
import com.inn.foresight.module.nv.layer3.service.ILayer3PPEService;
import com.inn.foresight.module.nv.layer3.utils.NVLayer3Utils;
import com.inn.foresight.module.nv.layer3.wrapper.Layer3PPEWrapper;
import com.inn.foresight.module.nv.workorder.wrapper.WOFileDetailWrapper;

@Path("/Layer3PPE")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class Layer3PPERest extends NVLayer3Utils {

	private Logger logger = LogManager.getLogger(Layer3PPERest.class);

	@Autowired
	private ILayer3PPEService iLayer3PPEService;	

	@GET
	@Path("/getSignalMessageDetail")
	public Response getSignalMessageDetail(@QueryParam(QMDLConstant.ROWKEY) String rowKey,
			@QueryParam(QMDLConstant.MSG_TYPE) String msgType, @Context HttpServletRequest request) {
		return iLayer3PPEService.getSignalMessageDetailMicroServiceUrl(rowKey, msgType, request);
	}

	@POST
	@Path("/processWOReportDump")
	public Response processWOReportDump(@QueryParam(WORKORDER_ID) Integer workorderId, Map<String, List<String>> map,
			@Context HttpServletRequest request) {
		return iLayer3PPEService.processWOReportDumpMicroServiceUrl(workorderId, map, request);
	}

	@GET
	@Path("/getWOReportDump")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getWOReportDump(@QueryParam(FILE_NAME) String fileName, @Context HttpServletRequest request) {
		return iLayer3PPEService.getWOReportDumpMicroServiceUrl(request);
	}

	@POST
	@Path("/getSignalMessageRecipeWise")
	public Response getSignalMessageRecipeWise(@QueryParam(WORKORDER_ID) Integer workorderId,
			Map<String, List<String>> map, @QueryParam(QMDLConstant.ROWKEY) String lastRowKey,
			@QueryParam(QMDLConstant.DIRECTION) String direction, @QueryParam(QMDLConstant.MESSAGE) String message,
			@Context HttpServletRequest request) {
		return iLayer3PPEService.getSignalMessageRecipeWiseMicroServiceUrl(workorderId, map, lastRowKey, direction,
				request);
	}

	@POST
	@Path("/createSignalMessageRecipeWiseCsv")
	public Response createSignalMessageRecipeWiseCsv(@QueryParam(WORKORDER_ID) Integer workorderId,
			Map<String, List<String>> map, @Context HttpServletRequest request,
			@QueryParam(WORKORDER_NAME) String workorderName) {
		return iLayer3PPEService.createSignalMessageRecipeWiseCsvMicroServiceUrl(workorderId, map, request);

	}

	@GET
	@Path("/getSignalMessagesForBin")
	public Response getSignalMessagesForBin(@QueryParam(KEY_ROW_PREFIX) String rowPrefix,
			@Context HttpServletRequest request) {
		return iLayer3PPEService.getSignalMessagesForBinMicroServiceUrl(rowPrefix, request);
	}

	

	@POST
	@Path("/getDriveDetailForStatisticDataXls")
	public Response getDriveDetailForStatisticDataXls(@QueryParam(WORKORDER_ID) Integer workorderId,
			@QueryParam(FILE_NAME) String fileName, @QueryParam("isInBuilding") Boolean isInBuilding,
			Map<String, List<String>> map, @Context HttpServletRequest request) {
		return iLayer3PPEService.getDriveDetailForStatisticDataXlsMicroServiceUrl(workorderId, map, request);
	}

	@POST
	@Path("/getKPIBuilderMeta")
	public Response getKPIBuilderMeta(Layer3PPEWrapper layer3PPEWrapper) {
		logger.info("Getting values for KPI builder Meta wrapper {}",layer3PPEWrapper);
		Object response = iLayer3PPEService.getKPIBuilderMeta(layer3PPEWrapper);
		return Response.ok(response).build();

	}
	
	
	@POST
	@Path("/getLayer3MetaData")
	public Response getLayer3MetaData(Layer3PPEWrapper layer3MetaData) {
		logger.info("Getting values for Layer3 Meta Data wrapper {}",layer3MetaData);
		Object response = iLayer3PPEService.getLayer3MetaData(layer3MetaData);
		return Response.ok(response).build();

	}
		
	
	@POST
	@Path("/updateLayer3MetaData")
	public Response updateLayer3MetaData(Layer3MetaData layer3MetaData) {
		logger.info("Getting values for Layer3 Meta Data {}",layer3MetaData);
		Object response = iLayer3PPEService.updateLayer3MetaData(layer3MetaData);
		return Response.ok(response).build();

	}
	@POST
	@Path("/deleteLayer3MetaData")
	public Response deleteLayer3MetaData(Layer3MetaData layer3MetaData) {
		logger.info("Getting values for Layer3 Meta Data {}",layer3MetaData);
		Object response = iLayer3PPEService.deleteLayer3MetaData(layer3MetaData);
		return Response.ok(response).build();

	}
	
	
	@POST
	@Path("/getPPEDataForMap")
	public Response getPPEDataForMap(Layer3PPEWrapper wrapper, @QueryParam("tableName") String tableName) throws HttpException {
		Object ppeDataForMap = iLayer3PPEService.getPPEDataFromMicroService(wrapper, tableName);
		return Response.ok(ppeDataForMap).build();

	}

	@POST
	@Path("/getLayer3FileProcessDetail")
	public Response getLayer3FileProcessDetail(WOFileDetailWrapper wrapper, @QueryParam("lLimit") String lowerLimit, @QueryParam("uLimit") String upperLimit) {
		Object ppeDataForMap = iLayer3PPEService.getLayer3FileProcessDetail(lowerLimit, upperLimit, wrapper);
		return Response.ok(ppeDataForMap).build();
	}

	@POST
	@Path("/getAllLayer3FilesCount")
	public Response getAllLayer3FilesCount(WOFileDetailWrapper wrapper) {
		Object ppeDataForMap = iLayer3PPEService.getAllLayer3FilesCount(wrapper);
		return Response.ok(ppeDataForMap).build();
	}

}

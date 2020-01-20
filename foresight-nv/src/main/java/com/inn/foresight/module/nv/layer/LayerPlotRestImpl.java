package com.inn.foresight.module.nv.layer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
import org.springframework.stereotype.Service;

import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.maplayer.rest.impl.GenericMapRestMicroServiceImpl;
import com.inn.foresight.module.nv.layer.service.LayerPlotService;
import com.inn.foresight.module.nv.sitesuggestion.service.ISiteSuggestionService;

/** The Class LayerPlotRestImpl. */
@Path("/layer")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Service("LayerPlotRestImpl")
public class LayerPlotRestImpl {
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(GenericMapRestMicroServiceImpl.class);

	@Autowired
	LayerPlotService layerService;
	
	@Autowired
	ISiteSuggestionService iSiteSuggestionService;
	
	/**
	 * @param columnList
	 * @param tableName
	 * @param zoom
	 * @param nELat
	 * @param nELng
	 * @param sWLat
	 * @param sWLng
	 * @param minDate
	 * @param maxDate
	 * @param postFix
	 * @param gridColName
	 * @param recordType
	 * @param mapType
	 * @return
	 */
	@POST
	@Path("getLayerPlotData")
	public String getLayerPlotData(List<String> columnList, @QueryParam("tableName") String tableName,
			@QueryParam("zoomLevel") Integer zoom, @QueryParam("NELat") Double nELat, @QueryParam("NELng") Double nELng,
			@QueryParam("SWLat") Double sWLat, @QueryParam("SWLng") Double sWLng, @QueryParam("minDate") String minDate,
			@QueryParam("maxDate") String maxDate, @QueryParam("postFix") String postFix,
			@QueryParam("gridColName") String gridColName, @QueryParam("recordType") String recordType,@QueryParam("mapType") String mapType
			,@QueryParam("filterColName") String filterColName) {
		try {
			logger.info("Getting parameter getLayerPlotData {}, {}, {}, {}, {}, {}, {}, {}, [{}] ,{} , {} , {} , {} , {}",
					tableName, zoom, nELat, nELng, sWLat, sWLng, minDate, maxDate, columnList, postFix, gridColName,
					recordType,mapType,filterColName);
			String response = layerService.getLayerData(columnList, tableName, zoom, nELat, nELng, sWLat,
					sWLng, minDate, maxDate, postFix, gridColName, recordType,mapType,filterColName);
			if (response != null) {
				return response;
			} else {
				return ForesightConstants.ERROR;
			}
		} catch (Exception e) {
			logger.error("Exception inside getDataFromHbase {} ", ExceptionUtils.getStackTrace(e));
			return ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG;
		}
	}
	
	
	@GET
	@Path("getSiteAcquisitionLayerData")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getSiteAcquisitionLayerData(@QueryParam("zoomLevel") Integer zoom, @QueryParam("NELat") Double nELat, @QueryParam("NELng") Double nELng,
			@QueryParam("SWLat") Double sWLat, @QueryParam("SWLng") Double sWLng, @QueryParam("fromDate") String fromDate,
			@QueryParam("toDate") String toDate,@QueryParam("buildingType") String buildingType,@QueryParam("siteType") String siteType,
			@QueryParam("displayZoomLevel") Integer displayZoomLevel) {
		try {
			if ((fromDate == null || fromDate.equalsIgnoreCase("null"))
					&& (toDate == null || toDate.equalsIgnoreCase("null"))) {
				Date date = new Date();
				String df = ForesightConstants.DATE_FORMAT_DDMMYY;
				SimpleDateFormat sfd = new SimpleDateFormat(df);
				fromDate = sfd.format(date);
				toDate = sfd.format(date);
			}
			if (Utils.hasValidValue(fromDate) || Utils.hasValidValue(toDate)) {
				return Response.ok(iSiteSuggestionService.getSiteAcquisitionLayerData(zoom, nELat, nELng, sWLat, sWLng, fromDate,
						toDate, buildingType, siteType,displayZoomLevel)).build();
			} else {
				return Response.ok(ForesightConstants.INVALID_PARAMETERS_JSON).build();
			}
		} catch (Exception e) {
			logger.error("Error while getting Site Acquisition Layer Data = {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(ForesightConstants.FAILURE_JSON).build();
		}
	}
	
	
	
	
}

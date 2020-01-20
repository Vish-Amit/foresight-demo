package com.inn.foresight.module.nv.layer;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.layer.service.LayerPlotService;

@Path("/ms/layer")
public class LayerPlotMicroServiceRestImpl {
	
	private Logger logger = LogManager.getLogger(LayerPlotMicroServiceRestImpl.class);
	
	@Autowired
	LayerPlotService layerService;
	
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
	 * @param latestDataDate
	 * @param postFix
	 * @param gridColName
	 * @param recordType
	 * @param mapType
	 * @return
	 * @throws ParseException
	 */
	@POST
	@Path("getLayerPlotData")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLayerPlotData(List<String> columnList, @QueryParam("tableName") String tableName,
			@QueryParam("zoomLevel") Integer zoom, @QueryParam("NELat") Double nELat, @QueryParam("NELng") Double nELng,
			@QueryParam("SWLat") Double sWLat, @QueryParam("SWLng") Double sWLng, @QueryParam("minDate") String minDate,
			@QueryParam("maxDate") String maxDate, @QueryParam("latestDataDate") Long latestDataDate,
			@QueryParam("postFix") String postFix, @QueryParam("gridColName") String gridColName,
			@QueryParam("recordType") String recordType,@QueryParam("mapType") String mapType,
			@QueryParam("filterColName") String filterColName) throws ParseException
	{
		logger.info("Hbase REST {}, {}, {}, {}, {}, {}, {}, {}, {}, {} ,{} ,{} ,{} , {}",tableName, zoom, columnList, nELat, 
				nELng, sWLat, sWLng, minDate, maxDate, postFix,gridColName,recordType,mapType,filterColName);

		logger.info("Going to startProcessing {}",new Date(System.currentTimeMillis()));
		
		if (Utils.hasValidValue(tableName) && zoom != null && columnList != null
				&& nELat != null && nELng != null && sWLat != null && sWLng != null) {
			try {
				List<List<String>> layerPlotData = layerService.getLayerPlotData(tableName, zoom, columnList, nELat,
						nELng, sWLat, sWLng, minDate, maxDate, postFix,gridColName,recordType,mapType,filterColName);
				return Response.ok(layerPlotData).build();
			} catch (Exception e) {
				return Response.ok("{\"response\":\"error getting data\"}").build();
			}
		}
		else {
			return Response.ok("{\"response\":\"invalid params\"}").build();
		}
	}
	
	
	@POST
	@Path("getCoverageClusterData")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCoverageClusterData(String json) throws ParseException, IOException
	{
		logger.info("Microservice Json Found {}", json);
		if (Utils.hasValidValue(json)) {
			return Response.ok(layerService.getCoverageClusterHbaseData(json)).build();
		} else {
			return Response.ok("{\"response\":\"invalid params\"}").build();
		}
	}
	
}

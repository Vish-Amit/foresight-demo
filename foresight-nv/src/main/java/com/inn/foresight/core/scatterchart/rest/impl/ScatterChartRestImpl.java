package com.inn.foresight.core.scatterchart.rest.impl;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.scatterchart.service.IScatterChartService;
import com.inn.foresight.core.scatterchart.util.ScatterChartUtil;
import com.inn.foresight.core.scatterchart.wrapper.ScatterChartRequest;

/**
 * The Class ScatterChartRestImpl.
 */
@Path(value = "scatterChart")
@Produces("application/json")
@Consumes("application/json")
public class ScatterChartRestImpl {

	/** The service. */
	@Autowired
	private IScatterChartService service;

	/** The logger. */
	private static Logger logger = LogManager.getLogger(ScatterChartRestImpl.class);

	/**
	 * Gets the scatter chart data.
	 *
	 * @param request the request
	 * @param latitude the latitude
	 * @param longitude the longitude
	 * @param startDate the start date
	 * @param endDate the end date
	 * @param zoomLevel the zoom level
	 * @return the scatter chart data
	 * @throws RestException the rest exception
	 */
	@POST
	@Path("ms/getScatterChartData")
	public String getScatterChartData(ScatterChartRequest request, @QueryParam("startDate") String startDate,
			@QueryParam("endDate") String endDate, @QueryParam("zoomLevel") Integer zoomLevel) {
		logger.info("going to get scatter chart data kpi1 : {} kpi2 : {} startDate : {} endDate : {}",
				request.getKpi1(), request.getKpi2(), startDate, endDate);
		return service.getScatterChartData(request, startDate, endDate, zoomLevel);
	}

	@POST
	@Path("ms/getScatterChartDataForImage")
	public String getScatterChartDataForImage(ScatterChartRequest request, @QueryParam("zoomLevel") Integer zoomLevel,
			@QueryParam("frequency") String frequency, @QueryParam("siteStatus") String siteStatus) {
		logger.info("going to get scatter chart data for  : {} zoom : {}, frequency : {}", request, zoomLevel,
				frequency);
		return service.getScatterChartDataForImage(request, frequency, zoomLevel, siteStatus);
	}
 
	// app server--------------------------------------------------------------------
	/**
	 * Gets the scatter chart data.
	 *
	 * @param request the request
	 * @param latitude the latitude
	 * @param longitude the longitude
	 * @param startDate the start date
	 * @param endDate the end date
	 * @param zoomLevel the zoom level
	 * @param kpi1 the kpi 1
	 * @param kpi2 the kpi 2
	 * @return the scatter chart data
	 * @throws RestException the rest exception
	 */
	@POST
	@Path("getScatterChartData")
	public String getScatterChartData(ScatterChartRequest request, @QueryParam("latitude") Double latitude,
			@QueryParam("longitude") Double longitude, @QueryParam("startDate") String startDate,
			@QueryParam("endDate") String endDate, @QueryParam("zoomLevel") Integer zoomLevel) {

		ScatterChartUtil.validateRequest(request, latitude, longitude, zoomLevel, request.getKpi1(), request.getKpi2());
		try {
			if (!request.getGeographyList().isEmpty()) {
				return service.getScatterChartDataByGeographyList(startDate, endDate, zoomLevel, request);
			} else {
				return service.getScatterChartDataByCentroid(latitude, longitude, startDate, endDate, zoomLevel,
						request);
			}
		} catch (RestException be) {
			throw new RestException(be.getGuiMessage());
		}
	}

	
	/**
	 * Gets the geography list for scatter chart.
	 *
	 * @param latitude the latitude
	 * @param longitude the longitude
	 * @param sourceGeographyName the source geography name
	 * @return the geography list for scatter chart
	 * @throws RestException the rest exception
	 */
	@GET
	@Path("getGeographyListForScatterChart")
	public List<String> getGeographyListForScatterChart(@QueryParam("latitude") Double latitude,
			@QueryParam("longitude") Double longitude, @QueryParam("sourceGeographyName") String sourceGeographyName) {
		if (latitude != null && longitude != null && sourceGeographyName != null) {
			return service.getGeographyListForScatterChart(latitude, longitude, sourceGeographyName);
		} else {
			throw new RestException(ForesightConstants.INVALID_PARAMETER);
		}
	}
	
	/**
	 * Gets the scatter chart data for image.
	 *
	 * @param request the request
	 * @param latitude the latitude
	 * @param longitude the longitude
	 * @param kpi1Date the kpi 1 date
	 * @param kpi2Date the kpi 2 date
	 * @param zoomLevel the zoom level
	 * @param kpi1 the kpi 1
	 * @param kpi2 the kpi 2
	 * @return the scatter chart data for image
	 * @throws RestException the rest exception
	 */
	@POST
	@Path("getScatterChartDataForImage")
	public String getScatterChartDataForImage(ScatterChartRequest request, @QueryParam("latitude") Double latitude,
			@QueryParam("longitude") Double longitude, @QueryParam("zoomLevel") Integer zoomLevel,
			@QueryParam("frequency") String frequency, @QueryParam("siteStatus") String siteStatus) {
		ScatterChartUtil.validateRequestForImage(request, latitude, longitude, zoomLevel, frequency, siteStatus);
		try {
			if (!request.getGeographyList().isEmpty()) {
				return service.getScatterChartForImageByGeographyList(zoomLevel, request, frequency, siteStatus);
			} else {
				return service.getScatterChartForImageByCentroid(latitude, longitude, zoomLevel, request, frequency, siteStatus);
			}
		} catch (RestException be) {
			throw new RestException(be.getGuiMessage());
		}
	}
}

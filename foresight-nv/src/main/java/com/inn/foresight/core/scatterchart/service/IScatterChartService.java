package com.inn.foresight.core.scatterchart.service;

import java.util.List;

import com.inn.foresight.core.scatterchart.util.Kpi;
import com.inn.foresight.core.scatterchart.wrapper.DataPoint;
import com.inn.foresight.core.scatterchart.wrapper.ScatterChartData;
import com.inn.foresight.core.scatterchart.wrapper.ScatterChartRequest;

/**
 * The Interface IScatterChartService.
 */
public interface IScatterChartService {

	/**
	 * Gets the scatter chart data.
	 * @param request TODO
	 * @param startDate the start date
	 * @param endDate the end date
	 * @param zoomLevel the zoom level
	 * @param frequency TODO
	 *
	 * @return the scatter chart data
	 */
	String getScatterChartData(ScatterChartRequest request, String startDate, String endDate, Integer zoomLevel);

	/**
	 * Gets the scatter chart data for image.
	 * @param request TODO
	 * @param frequency the frequency
	 * @param zoomLevel the zoom level
	 * @param siteStatus TODO
	 * @return the scatter chart data for image
	 */
	String getScatterChartDataForImage(ScatterChartRequest request, String frequency, Integer zoomLevel, String siteStatus);

	ScatterChartData calculateScatterChartData(List<DataPoint> dataPoints, Kpi kpi1, Kpi kpi2, String clutter);

	String getScatterChartDataByGeographyList(String startDate, String endDate, Integer zoomLevel,
			ScatterChartRequest request);

	String getScatterChartDataByCentroid(Double latitude, Double longitude, String startDate, String endDate,
			Integer zoomLevel, ScatterChartRequest request);

	List<String> getGeographyListForScatterChart(Double latitude, Double longitude, String sourceGeographyName);

	String getScatterChartForImageByGeographyList(Integer zoomLevel, ScatterChartRequest request, String frequency,
			String siteStatus);

	String getScatterChartForImageByCentroid(Double latitude, Double longitude, Integer zoomLevel,
			ScatterChartRequest request, String frequency, String siteStatus);

	

}

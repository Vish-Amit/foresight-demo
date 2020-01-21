package com.inn.foresight.core.scatterchart.service.impl;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.inn.commons.io.image.ImageUtils;
import com.inn.commons.maps.LatLng;
import com.inn.commons.maps.geometry.GeometryUtils;
import com.inn.commons.maps.geometry.gis.GIS2DPolygon;
import com.inn.commons.maps.tiles.Tile;
import com.inn.commons.maps.tiles.TileBoundaryUtils;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.maplayer.service.IGenericMapService;
import com.inn.foresight.core.maplayer.utils.GenericMapUtils;
import com.inn.foresight.core.scatterchart.service.IScatterChartService;
import com.inn.foresight.core.scatterchart.util.Kpi;
import com.inn.foresight.core.scatterchart.util.ScatterChartConstants;
import com.inn.foresight.core.scatterchart.util.ScatterChartUtil;
import com.inn.foresight.core.scatterchart.wrapper.DataPoint;
import com.inn.foresight.core.scatterchart.wrapper.ScatterChartData;
import com.inn.foresight.core.scatterchart.wrapper.ScatterChartRequest;
import com.inn.product.systemconfiguration.service.SystemConfigurationService;
import com.inn.product.um.geography.dao.GeographyL3Dao;
import com.inn.product.um.geography.dao.GeographyL4Dao;
import com.inn.product.um.geography.model.GeographyL3;
import com.inn.product.um.geography.model.GeographyL4;



/**
 * The Class ScatterChartServiceImpl.
 */
@Service("ScatterChartServiceImpl")
public class ScatterChartServiceImpl implements IScatterChartService {

	/** The generic map service. */
	@Autowired
	@Qualifier("GenericMapServiceImpl")
	private IGenericMapService genericMapService ;

	@Autowired
	private SystemConfigurationService sysConfService ;  
	
	/** The geography L3 dao. */
	@Autowired
	private GeographyL3Dao geoL3Dao;
	
	/** The geography L4 dao. */
	@Autowired
	private GeographyL4Dao geoL4Dao;
	
	/** The logger. */
	private static Logger logger = LogManager.getLogger(ScatterChartServiceImpl.class);

	@Override
	public String getScatterChartData(ScatterChartRequest request, String startDate, String endDate,
			Integer zoomLevel) {
		logger.info("getting scatter chart data from kpiTable : {} geographyTable : {} kpi1 : {}, kpi2 : {}",
				request.getKpiTable(), request.getGeographyTable(), request.getKpi1(), request.getKpi2());

		String clutter = "U"; // TODO assumed for now

		List<DataPoint> dataPoints = getDataPointsForGeography(request, startDate, endDate, zoomLevel);

		ScatterChartData scatterChartData = calculateScatterChartData(dataPoints, Kpi.valueOf(request.getKpi1()),
				Kpi.valueOf(request.getKpi2()), clutter);

		return ScatterChartUtil.buildResponse(scatterChartData, clutter, request.getGeographyList());
	}

	/**
	 * Calculate scatter chart data.
	 *
	 * @param dataPoints the data points
	 * @param kpi1 the kpi 1
	 * @param kpi2 the kpi 2
	 * @param clutter the clutter
	 * @return the scatter chart data
	 */
	@Override
	public ScatterChartData calculateScatterChartData(List<DataPoint> dataPoints, Kpi kpi1, Kpi kpi2, String clutter) {
		ScatterChartData scatterChartData = new ScatterChartData();
		Map<String, Boolean> kpiValueMap = new HashMap<>();
		for(DataPoint dataPoint : dataPoints) {
			try {
				Integer quadrantNumber = getQuadrant(dataPoint, kpi1, kpi2, clutter);
				if (quadrantNumber > ForesightConstants.ZERO) {
					List<Double> quadrantValues = getQuadrantValuesListAndUpdateMap(dataPoint, kpi1, kpi2, kpiValueMap);

					if(!quadrantValues.isEmpty()) {
						updateMinMaxValuesForAxes(quadrantValues, scatterChartData);

						updatePointsAndCounterForQuadrant(quadrantNumber, quadrantValues, scatterChartData);

						calculateValuesForTrendLine(quadrantValues, scatterChartData);
					}

				}
			}catch (Exception e) {
				logger.error("Exception in scatter chart data calculation : {}",ExceptionUtils.getStackTrace(e));
			}
		}
		scatterChartData.setKpiValueMap(kpiValueMap);
		return scatterChartData;
	}

	/**
	 * Gets the quadrant.
	 *
	 * @param dataPoint the data point
	 * @param kpi1 the kpi 1
	 * @param kpi2 the kpi 2
	 * @param clutter the clutter
	 * @return the quadrant
	 */
	private Integer getQuadrant(DataPoint dataPoint, Kpi kpi1, Kpi kpi2, String clutter) {
		Integer actualQuadrant = ScatterChartUtil.getQuadrantNumber(dataPoint.getFirstKPIValue(),
				dataPoint.getSecondKPIValue(), kpi1, kpi2, clutter);

		Double roundedValueFirst = ScatterChartUtil.ceilDecimalPlace(dataPoint.getFirstKPIValue());
		Double roundedValueSecond = ScatterChartUtil.ceilDecimalPlace(dataPoint.getSecondKPIValue());

		Integer roundedQuadrant = ScatterChartUtil.getQuadrantNumber(roundedValueFirst, roundedValueSecond, kpi1, kpi2,
				clutter);

		if (!actualQuadrant.equals(roundedQuadrant)) {
			dataPoint.setFirstKPIValue(ScatterChartUtil.setPrecisionForDouble(dataPoint.getFirstKPIValue(),
					ScatterChartConstants.ONE_DIGIT_PRECISION));
			dataPoint.setSecondKPIValue(ScatterChartUtil.setPrecisionForDouble(dataPoint.getSecondKPIValue(),
					ScatterChartConstants.ONE_DIGIT_PRECISION));
		} else {
			dataPoint.setFirstKPIValue(roundedValueFirst);
			dataPoint.setSecondKPIValue(roundedValueSecond);
		}

		return actualQuadrant;
	}

	/**
	 * Calculate values for trend line.
	 *
	 * @param quadrantValues the quadrant values
	 * @param scatterChartData the scatter chart data
	 */
	private void calculateValuesForTrendLine(List<Double> quadrantValues, ScatterChartData scatterChartData) {
		Double x = quadrantValues.get(ForesightConstants.ZERO);
		Double y = quadrantValues.get(ForesightConstants.ONE);
		if (x != null) {
			scatterChartData.addXpoint(x);
			scatterChartData.aggregateX(x);
			scatterChartData.aggregateXSquareSum(x);
		}
		if (y != null) {
			scatterChartData.addYpoint(y);
			scatterChartData.aggregateY(y);
		}
		scatterChartData.aggregateSumOfXY(x, y);
	}

	/**
	 * Update points and counter for quadrant.
	 *
	 * @param quadrantNumber the quadrant number
	 * @param quadrantValues the quadrant values
	 * @param scatterChartData the scatter chart data
	 */
	private void updatePointsAndCounterForQuadrant(Integer quadrantNumber, List<Double> quadrantValues,
			ScatterChartData scatterChartData) {
		switch (quadrantNumber) {
		case 1:
			scatterChartData.incrementFirstQuadrant();
			scatterChartData.addFirstQuadPoints(quadrantValues);
			break;
		case 2:
			scatterChartData.incrementSecondQuadrant();
			scatterChartData.addSecondQuadPoints(quadrantValues);
			break;
		case 3:
			scatterChartData.incrementThirdQuadrant();
			scatterChartData.addThirdQuadPoints(quadrantValues);
			break;
		case 4:
			scatterChartData.incrementFourthQuadrant();
			scatterChartData.addFourthQuadPoints(quadrantValues);
			break;
		default:
			logger.info("not an invalid quadrant number : {}", quadrantNumber);
		}
	}

	/**
	 * Update min max values for axes.
	 *
	 * @param quadrantValues the quadrant values
	 * @param scatterChartData the scatter chart data
	 */
	private void updateMinMaxValuesForAxes(List<Double> quadrantValues, ScatterChartData scatterChartData) {
		Map<String, Double> minMaxMap = scatterChartData.getMinAndMaxMap();
		try {
			if (quadrantValues != null && !quadrantValues.isEmpty()) {
				if (!minMaxMap.isEmpty()) {
					if (quadrantValues.get(0) < minMaxMap.get(ScatterChartConstants.X_MIN)) {
						minMaxMap.put(ScatterChartConstants.X_MIN, quadrantValues.get(0));
					}
					if (quadrantValues.get(1) < minMaxMap.get(ScatterChartConstants.Y_MIN)) {
						minMaxMap.put(ScatterChartConstants.Y_MIN, quadrantValues.get(1));
					}
					if (quadrantValues.get(0) > minMaxMap.get(ScatterChartConstants.X_MAX)) {
						minMaxMap.put(ScatterChartConstants.X_MAX, quadrantValues.get(0));
					}
					if (quadrantValues.get(1) > minMaxMap.get(ScatterChartConstants.Y_MAX)) {
						minMaxMap.put(ScatterChartConstants.Y_MAX, quadrantValues.get(1));
					}

				} else {
					minMaxMap.put(ScatterChartConstants.X_MIN, quadrantValues.get(0));
					minMaxMap.put(ScatterChartConstants.X_MAX, quadrantValues.get(0));
					minMaxMap.put(ScatterChartConstants.Y_MIN, quadrantValues.get(1));
					minMaxMap.put(ScatterChartConstants.Y_MAX, quadrantValues.get(1));
				}
			}
		} catch (final Exception e) {
			logger.error("Error in calculating min and max value {}", ExceptionUtils.getStackTrace(e));
		}
		scatterChartData.setMinAndMaxMap(minMaxMap);
	}

	/**
	 * Gets the quadrant values list and update map.
	 *
	 * @param dataPoint the data point
	 * @param kpi1 the kpi 1
	 * @param kpi2 the kpi 2
	 * @param kpiValueMap the kpi value map
	 * @return the quadrant values list and update map
	 */
	private List<Double> getQuadrantValuesListAndUpdateMap(DataPoint dataPoint, Kpi kpi1, Kpi kpi2,
			Map<String, Boolean> kpiValueMap) {
		List<Double> quardValue = new ArrayList<>();
		if (kpi1 == Kpi.RSRP && kpi2 == Kpi.SINR) {
			String key = dataPoint.getFirstKPIValue() + ForesightConstants.UNDERSCORE + dataPoint.getSecondKPIValue();
			if (kpiValueMap.get(key) == null) {
				kpiValueMap.put(key, true);
				quardValue.add(dataPoint.getFirstKPIValue());
				quardValue.add(dataPoint.getSecondKPIValue());
			}
		}
		return quardValue;
	}

	/**
	 * Gets the data points for geography.
	 *
	 * @param geographyList the geography list
	 * @param columns the columns
	 * @param kpiTableName the kpi table name
	 * @param geographyTableName the geography table name
	 * @param startDate the start date
	 * @param endDate the end date
	 * @param zoomLevel the zoom level
	 * @return the data points for geography
	 */
	private List<DataPoint> getDataPointsForGeography(ScatterChartRequest request, String startDate, String endDate,
			Integer zoomLevel) {
		List<DataPoint> filteredDataPoints = new ArrayList<>();
		List<String> geographyColumns = GenericMapUtils.getGeoCordColumnList();

		for (String geography : request.getGeographyList()) {
			try {

				List<List<List<Double>>> geographyBoundaries = getBoundariesForGeography(geography, request.getGeographyTable(),
						geographyColumns);

				List<List<String>> kpiData = genericMapService.getDataForTableForMultiBoundary(request.getKpiTable(),
						zoomLevel, request.getColumns(), geographyBoundaries, startDate, endDate);

				List<DataPoint> dataPoints = parseAndFilterDataPoints(kpiData, geographyBoundaries);
				logger.info("data points : {} for geography : {}", dataPoints.size(), geography);

				if (!dataPoints.isEmpty()) {
					filteredDataPoints.addAll(dataPoints);
				}

			} catch (IOException io) {
				logger.error("IOException in getting scatter data : {}", ExceptionUtils.getStackTrace(io));
			}
		}
		return filteredDataPoints;
	}


	/**
	 * Parses the and filter data points.
	 *
	 * @param kpiData the kpi data
	 * @param geographyBoundaries the geography boundaries
	 * @return the list
	 */
	private List<DataPoint> parseAndFilterDataPoints(List<List<String>> kpiData, List<List<List<Double>>> geographyBoundaries) {
		List<DataPoint> filteredDataPoints = new ArrayList<>();

		List<GIS2DPolygon> geographyPolygons = ScatterChartUtil.getPolygonListFromBoundaries(geographyBoundaries);

		for(DataPoint dataPoint : parseDataPoints(kpiData)) {
			for(GIS2DPolygon polygon : geographyPolygons) {
				if(GeometryUtils.contains(polygon,new LatLng(dataPoint.getLatitude(), dataPoint.getLongitude()))) {
					filteredDataPoints.add(dataPoint);
				}
			}
		}

		return filteredDataPoints;
	}

	/**
	 * Parses the data points.
	 *
	 * @param kpiData the kpi data
	 * @return the list
	 */
	private List<DataPoint> parseDataPoints(List<List<String>> kpiData) {
		List<DataPoint> dataPoints = new ArrayList<>();
		for (List<String> dataList : kpiData) {
			if (ScatterChartUtil.isValidKPIData(dataList)) {
				try {
					DataPoint dataPoint = new DataPoint();
					dataPoint.setLatitude(Double.parseDouble(dataList.get(ScatterChartConstants.INDEX_LATITUDE)));
					dataPoint.setLongitude(Double.parseDouble(dataList.get(ScatterChartConstants.INDEX_LONGITUDE)));
					dataPoint.setFirstKPIValue(Double.parseDouble(dataList.get(ScatterChartConstants.INDEX_KPI1_VALUE)));
					dataPoint.setSecondKPIValue(Double.parseDouble(dataList.get(ScatterChartConstants.INDEX_KPI2_VALUE)));
					dataPoints.add(dataPoint);
				} catch (Exception e) {
					logger.error("Exception in parsing data points : {}", ExceptionUtils.getStackTrace(e));
				}
			}
		}
		return dataPoints;
	}

	/**
	 * Gets the boundaries for geography.
	 *
	 * @param geography the geography
	 * @param geographyTable the geography table
	 * @param geographyColumns the geography columns
	 * @return the boundaries for geography
	 */
	private List<List<List<Double>>> getBoundariesForGeography(String geography, String geographyTable,
			List<String> geographyColumns) {
		List<Map<String, String>> boundaryData = genericMapService
				.getBoundaryDataByGeographyNamesMS(Arrays.asList(geography), GenericMapUtils.GEOGRAPHY_TABLE_NAME, geographyColumns,null,geographyTable.toUpperCase());

		if (!boundaryData.isEmpty()) {
			Map<String, String> dataMap = boundaryData.get(0);
			if (!dataMap.isEmpty()) {
				String boundaryString = dataMap.get(ScatterChartConstants.COORDINATES);
				if (boundaryString != null) {
					logger.info("boundary size : {} for : {}",boundaryString.length(), geography);
					return ScatterChartUtil.convertStringToMultiBoundary(boundaryString);
				} else {
					logger.info("boundary is null for : {}", geography);
				}
			}
		}
		return new ArrayList<>();
	}

	@Override
	public String getScatterChartDataForImage(ScatterChartRequest request, String frequency, Integer zoomLevel,
			String siteStatus) {
		logger.info("getting scatter chart data from kpiTable : {} geographyTable : {} kpi1 : {}, kpi2 : {}",
				request.getKpiTable(), request.getGeographyTable(), request.getKpi1(), request.getKpi2());

		String clutter = "U"; // TODO assumed for now

		List<DataPoint> dataPoints = getDataPointsForImage(request, zoomLevel, frequency, siteStatus);

		ScatterChartData scatterChartData = calculateScatterChartData(dataPoints, Kpi.valueOf(request.getKpi1()),
				Kpi.valueOf(request.getKpi2()), clutter);

		return ScatterChartUtil.buildResponse(scatterChartData, clutter, request.getGeographyList());
	}

	/**
	 * Gets the data points for image.
	 *
	 * @param geographyList the geography list
	 * @param columnName the column name
	 * @param kpiTableName the kpi table name
	 * @param geographyTableName the geography table name
	 * @param kpi1Date the kpi 1 date
	 * @param kpi2Date the kpi 2 date
	 * @param kpi1 the kpi 1
	 * @param kpi2 the kpi 2
	 * @param zoomLevel the zoom level
	 * @return the data points for image
	 */
	private List<DataPoint> getDataPointsForImage(ScatterChartRequest request, Integer zoomLevel, String frequency,
			String siteStatus) {
		List<DataPoint> filteredDataPoints = new ArrayList<>();
		List<String> geographyColumns = GenericMapUtils.getGeoCordColumnList();
		for (String geography : request.getGeographyList()) {
			try {
				List<List<List<Double>>> geographyBoundaries = getBoundariesForGeography(geography,
						request.getGeographyTable(), geographyColumns);

				List<Tile> tiles = TileBoundaryUtils.getExactTilesForMultiBoundary(geographyBoundaries, zoomLevel);

				for (Tile tile : tiles) {
					logger.info("going to get image for tile : {}", tile);
					List<DataPoint> dataPoints = getDataPointsForTile(tile, request.getKpi1Date(),
							request.getKpi2Date(), request.getKpiTable(), request.getColumns().get(0),
							request.getKpi1(), request.getKpi2(), frequency, siteStatus);
					logger.info("image data points size : {}", dataPoints.size());
					if (!dataPoints.isEmpty()) {
						filteredDataPoints.addAll(dataPoints);
					}
				}
			} catch (Exception e) {
				logger.error("Exception in getting scatter data for image : {}", ExceptionUtils.getStackTrace(e));
			}
		}
		return filteredDataPoints;
	}

	/**
	 * Gets the data points for tile.
	 *
	 * @param tile the tile
	 * @param kpi1Date the kpi 1 date
	 * @param kpi2Date the kpi 2 date
	 * @param kpiTableName the kpi table name
	 * @param columnName the column name
	 * @param kpi1 the kpi 1
	 * @param kpi2 the kpi 2
	 * @return the data points for tile
	 */
	private List<DataPoint> getDataPointsForTile(Tile tile, String kpi1Date, String kpi2Date, String kpiTableName,
			String columnName, String kpi1, String kpi2, String band, String siteStatus) {
		List<DataPoint> dataPoints = new ArrayList<>();
		// TODO : passing hard coded null for floor
		BufferedImage kpi1Image=null;
		BufferedImage kpi2Image=null;
		try {
			kpi1Image = ImageUtils.toBufferedImage(genericMapService.getImageForKpiAndZone(kpiTableName, tile.getIdWithZoom(),
					columnName, kpi1, kpi1Date, siteStatus, null, band));
			kpi2Image = ImageUtils.toBufferedImage(genericMapService.getImageForKpiAndZone(kpiTableName, tile.getIdWithZoom(),
					columnName, kpi2, kpi2Date, siteStatus, null, band));
		} catch (IOException e) {
			logger.error("Error in converting image {}", ExceptionUtils.getStackTrace(e));
		}
		if (kpi1Image != null && kpi2Image != null) {

			for (int x = ForesightConstants.ZERO; x < ImageUtils.IMAGE_SIZE; x++) {
				for (int y = ForesightConstants.ZERO; y < ImageUtils.IMAGE_SIZE; y++) {

					int kpi1Color = kpi1Image.getRGB(x, y);
					int kpi2Color = kpi2Image.getRGB(x, y);
					DataPoint dataPoint = getDataPointByKPI(kpi1Color, Kpi.valueOf(kpi1), kpi2Color, Kpi.valueOf(kpi2));
					if (dataPoint != null) {
						dataPoints.add(dataPoint);
					}
				}
			}
		}
		return dataPoints;
	}

	/**
	 * Gets the data point by KPI.
	 *
	 * @param kpi1Color the kpi 1 color
	 * @param kpi1 the kpi 1
	 * @param kpi2Color the kpi 2 color
	 * @param kpi2 the kpi 2
	 * @return the data point by KPI
	 */
	private DataPoint getDataPointByKPI(int kpi1Color, Kpi kpi1, int kpi2Color, Kpi kpi2) {
		DataPoint dataPoint = null;
		Double value1 = null;
		Double value2 = null;

		if (kpi1Color != ForesightConstants.ZERO && kpi2Color != ForesightConstants.ZERO) {
			if (Kpi.RSRP.equals(kpi1) && Kpi.SINR.equals(kpi2)) {
				value1 = sysConfService.getDbfColorLegendMap(kpi1.getValue()).get(kpi1Color);
				//AtollUtils.getRSRPColorMap().get(kpi1Color);
				value2 = sysConfService.getDbfColorLegendMap(kpi2.getValue()).get(kpi2Color);
				//AtollUtils.getSINRColorMap().get(kpi2Color);
			}
			// Add check for KPI when required
		}
		if (value1 != null && value2 != null) {
			dataPoint = new DataPoint();
			dataPoint.setFirstKPIValue(value1);
			dataPoint.setSecondKPIValue(value2);
		}
		return dataPoint;
	}
	
	/**
	 * Gets the scatter chart data by geography list.
	 *
	 * @param startDate the start date
	 * @param endDate the end date
	 * @param zoomLevel the zoom level
	 * @param request the request
	 * @return the scatter chart data by geography list
	 * @throws RestException the rest exception
	 */
	@Override
	public String getScatterChartDataByGeographyList(String startDate, String endDate, Integer zoomLevel,
			ScatterChartRequest request) {
		try {
			String url = ScatterChartUtil.getDropwizardUrl(startDate, endDate, zoomLevel);
			logger.info("scatter chart URL for micro-service : {}", url);
			StringEntity entity = new StringEntity(new Gson().toJson(request), ContentType.APPLICATION_JSON);
			return Utils.sendPostRequest(url, entity);
		} catch (RestException be) {
			throw be;
		} catch (Exception e) {
			logger.error("Exception in creating scatter chart data : {}", ExceptionUtils.getStackTrace(e));
			return ForesightConstants.BLANK_STRING;
		}
	}
	
	/**
	 * Gets the scatter chart data by centroid.
	 *
	 * @param latitude the latitude
	 * @param longitude the longitude
	 * @param startDate the start date
	 * @param endDate the end date
	 * @param zoomLevel the zoom level
	 * @param request the request
	 * @return the scatter chart data by centroid
	 * @throws RestException the rest exception
	 */
	@Override
	public String getScatterChartDataByCentroid(Double latitude, Double longitude, String startDate, String endDate,
			Integer zoomLevel, ScatterChartRequest request) {
  
		setGeographyForLatLong(latitude, longitude, zoomLevel, request);
		
		return getScatterChartDataByGeographyList(startDate, endDate, zoomLevel, request);
	}
	
	/**
	 * Sets the geography for lat long.
	 *
	 * @param latitude the latitude
	 * @param longitude the longitude
	 * @param zoomLevel the zoom level
	 * @param request the request
	 * @throws RestException the rest exception
	 */
	private void setGeographyForLatLong(Double latitude, Double longitude, Integer zoomLevel,
			ScatterChartRequest request) {
		List<String> geographyList = getGeographyByLatLongAndZoom(latitude, longitude, zoomLevel,
				request.getGeographyTable());
		if (geographyList.isEmpty()) {
			throw new RestException(ScatterChartConstants.GEOGRAPHY_CANT_BE_EMPTY);
		}
		request.setGeographyList(geographyList);
	}
	
	/**
	 * Gets the geography by latitude longitude and zoom.
	 *
	 * @param latitude the latitude
	 * @param longitude the longitude
	 * @param zoomLevel the zoom level
	 * @param geographyTable the geography table
	 * @return the geography by latitude long and zoom
	 */
	private List<String> getGeographyByLatLongAndZoom(Double latitude, Double longitude, Integer zoomLevel,
			String geographyTable) {
		String response = genericMapService.getGeographyDataByPoint(GenericMapUtils.getGeoColumnList(),
				GenericMapUtils.GEOGRAPHY_TABLE_NAME, latitude, longitude, false, geographyTable.toUpperCase());
		logger.info("got geography : {} for {},{}", response, latitude, longitude);

		if (response.length() <= 2) { // If point not within, find nearest geography. 
			response = genericMapService.getNearestPointByLatLong(geographyTable.toUpperCase(), latitude, longitude);
			logger.info("got nearest geography : {} for {},{}", response, latitude, longitude);
		}
		
		return ScatterChartUtil.getGeographyNameFromResponse(response);
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
	@Override
	public List<String> getGeographyListForScatterChart(Double latitude, Double longitude, String sourceGeographyName) {
		String geoName = getGeographyName(latitude, longitude, sourceGeographyName);
		List<String> geographyList = null;
		if (sourceGeographyName.equals(ScatterChartConstants.GEOGRAPHYL2)) {
			geographyList = getGeographyListForL2(geoName);
		} else if (sourceGeographyName.equals(ScatterChartConstants.GEOGRAPHYL3)) {
			geographyList = getGeographyListForL3(geoName);
		} else {
			throw new RestException(ScatterChartConstants.GEOGRAPHY_NOT_SUPPORTED);
		}
		return geographyList;
	}
	
	/**
	 * Gets the geography name.
	 *
	 * @param latitude            the latitude
	 * @param longitude            the longitude
	 * @param sourceGeographyName the source geography name
	 * @return the geography name
	 */
	private String getGeographyName(Double latitude, Double longitude, String sourceGeographyName) {
		List<String> list = getGeographyByLatLongAndZoom(latitude, longitude, ScatterChartConstants.INT_TEN,
				sourceGeographyName);
		String geoName = null;
		if (!list.isEmpty()) {
			geoName = list.get(0);
		}
		return geoName;
	}

	/**
	 * Gets the geography list for L 2.
	 *
	 * @param geoName
	 *            the geography name
	 * @return the geography list for L 2
	 */
	private List<String> getGeographyListForL2(String geoName) {
		List<String> geoList = new ArrayList<>();
		try {
			List<GeographyL3> geoL3List = geoL3Dao.getL3ByL2Name(geoName);
			if (geoL3List != null && geoName != null) {
				for (GeographyL3 geowrapper : geoL3List) {
					geoList.add(geowrapper.getName());
				}
			}
		} catch (Exception e) {
			logger.error("error in getting geoL3List: {}", ExceptionUtils.getStackTrace(e));
		}

		return geoList;
	}
	
	/**
	 * Gets the geography list for L 3.
	 *
	 * @param geoName
	 *            the geography name
	 * @return the geography list for L 3
	 */
	private List<String> getGeographyListForL3(String geoName) {
		List<String> geoList = new ArrayList<>();
		try {
			List<GeographyL4> geoL4List = geoL4Dao.getL4ByL3Name(geoName);
			if (geoL4List != null && geoName != null) {
				for (GeographyL4 geowrapper : geoL4List) {
					geoList.add(geowrapper.getName());
				}
			}
		} catch (Exception e) {
			logger.error("error in getting geoL4List: {}", ExceptionUtils.getStackTrace(e));
		}
		return geoList;
	}
	
	/**
	 * Gets the scatter chart for image by geography list.
	 * @param zoomLevel the zoom level
	 * @param request the request
	 * @return the scatter chart for image by geography list
	 * @throws RestException the rest exception
	 */
	@Override
	public String getScatterChartForImageByGeographyList(Integer zoomLevel, ScatterChartRequest request, String frequency, String siteStatus) {
		try {
			String url = ScatterChartUtil.getDropwizardUrlForImage(zoomLevel, frequency, siteStatus);
			StringEntity entity = new StringEntity(new Gson().toJson(request), ContentType.APPLICATION_JSON);
			return Utils.sendPostRequest(url, entity);
		} catch (RestException be) {
			throw be;
		} catch (Exception e) {
			logger.error("Exception in creating scatter chart data : {}", ExceptionUtils.getStackTrace(e));
			return ForesightConstants.BLANK_STRING;
		}
	
	}
	
	/**
	 * Gets the scatter chart for image by centroid.
	 *
	 * @param latitude the latitude
	 * @param longitude the longitude
	 * @param zoomLevel the zoom level
	 * @param request the request
	 * @return the scatter chart for image by centroid
	 * @throws RestException the rest exception
	 */
	@Override
	public String getScatterChartForImageByCentroid(Double latitude, Double longitude, Integer zoomLevel, ScatterChartRequest request,
			String frequency, String siteStatus) {
		setGeographyForLatLong(latitude, longitude, zoomLevel, request);
		return getScatterChartForImageByGeographyList(zoomLevel, request, frequency, siteStatus);
	}
}

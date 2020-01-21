package com.inn.foresight.core.scatterchart.util;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.maps.geometry.gis.GIS2DPolygon;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ConfigEnum;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.maplayer.utils.GenericMapUtils;
import com.inn.foresight.core.scatterchart.wrapper.DataPoint;
import com.inn.foresight.core.scatterchart.wrapper.ScatterChartData;
import com.inn.foresight.core.scatterchart.wrapper.ScatterChartRequest;

/**
 * The Class ScatterChartUtil.
 */
public class ScatterChartUtil {

	/** The logger. */
	private static Logger logger = LogManager.getLogger(ScatterChartUtil.class);

	/**
	 * Instantiates a new scatter chart utility.
	 */
	private ScatterChartUtil() {

	}
	
	/**
	 * Convert string to multi boundary.
	 *
	 * @param boundaryString the boundary string
	 * @return the list
	 */
	public static List<List<List<Double>>> convertStringToMultiBoundary(String boundaryString) {
		List<List<List<Double>>> responseData = new ArrayList<>();
		try {
			responseData = new Gson().fromJson(boundaryString, new TypeToken<List<List<List<Double>>>>() {

                /**
                 *
                 */
                private static final long serialVersionUID = 8636214015725261114L;
			}.getType());
		} catch (Exception e) {
			logger.error("Exception in converting string to boundary : {}", ExceptionUtils.getStackTrace(e));
		}
		return responseData;
	}

	/**
	 * Gets the polygon list from boundaries.
	 *
	 * @param geographyBoundaries the geography boundaries
	 * @return the polygon list from boundaries
	 */
	public static List<GIS2DPolygon> getPolygonListFromBoundaries(List<List<List<Double>>> geographyBoundaries) {
		List<GIS2DPolygon> polygonList = new ArrayList<>();
		try {
			for (List<List<Double>> boundary : geographyBoundaries) {
				polygonList.add(new GIS2DPolygon(boundary));
			}
		} catch (Exception e) {
			logger.error("Exception in creating polygon list from boundaries : {}", ExceptionUtils.getStackTrace(e));
		}
		return polygonList;
	}

	/**
	 * Checks if is valid KPI data.
	 *
	 * @param dataList the data list
	 * @return true, if is valid KPI data
	 */
	public static boolean isValidKPIData(List<String> dataList) {
		return !isBlank(dataList.get(ScatterChartConstants.INDEX_LATITUDE))
				&& !isBlank(dataList.get(ScatterChartConstants.INDEX_LONGITUDE))
				&& !isBlank(dataList.get(ScatterChartConstants.INDEX_KPI1_VALUE))
				&& !isBlank(dataList.get(ScatterChartConstants.INDEX_KPI2_VALUE));
	}

	/**
	 * Checks if is blank.
	 *
	 * @param value the value
	 * @return true, if is blank
	 */
	public static boolean isBlank(String value) {
		return (value == null || value.equalsIgnoreCase(ForesightConstants.BLANK_STRING));
	}

	/**
	 * Gets the quadrant number.
	 *
	 * @param value1 the value 1
	 * @param value2 the value 2
	 * @param kpi1 the kpi 1
	 * @param kpi2 the kpi 2
	 * @param clutter the clutter
	 * @return the quadrant number
	 */
	public static Integer getQuadrantNumber(Double value1, Double value2, Kpi kpi1, Kpi kpi2, String clutter) {
		// Add check for other KPI when required
		if (kpi1 == Kpi.RSRP && kpi2 == Kpi.SINR) {
			return getQuadrantByRSRPAndSINR(value1, value2, clutter);
		} else {
			return 0;
		}
	}

	/**
	 * Gets the quardent by RSRP and DL.
	 *
	 * @param dataPoint the data point
	 * @param clutter the clutter
	 * @return the quardent by RSRP and DL
	 */
	private static Integer getQuardentByRSRPAndDL(DataPoint dataPoint, String clutter) {
		if (ScatterChartConstants.CLUTTER_DU.equalsIgnoreCase(clutter)) {
			if (dataPoint.getFirstKPIValue() >= -95 && dataPoint.getSecondKPIValue() >= 3.75) {
				return 1;
			} else if (dataPoint.getFirstKPIValue() >= -95 && dataPoint.getSecondKPIValue() < 3.75) {
				return 2;
			} else if (dataPoint.getFirstKPIValue() < -95 && dataPoint.getSecondKPIValue() < 3.75) {
				return 3;
			} else if (dataPoint.getFirstKPIValue() < -95 && dataPoint.getSecondKPIValue() >= 3.75) {
				return 4;
			}
		} else if (ScatterChartConstants.CLUTTER_U.equalsIgnoreCase(clutter)) {
			if (dataPoint.getFirstKPIValue() >= -96.5 && dataPoint.getSecondKPIValue() >= 3.75) {
				return 1;
			} else if (dataPoint.getFirstKPIValue() >= -96.5 && dataPoint.getSecondKPIValue() < 3.75) {
				return 2;
			} else if (dataPoint.getFirstKPIValue() < -96.5 && dataPoint.getSecondKPIValue() < 3.75) {
				return 3;
			} else if (dataPoint.getFirstKPIValue() < -96.5 && dataPoint.getSecondKPIValue() >= 3.75) {
				return 4;
			}
		} else if (ScatterChartConstants.CLUTTER_SU.equalsIgnoreCase(clutter)) {
			if (dataPoint.getFirstKPIValue() >= -98 && dataPoint.getSecondKPIValue() >= 3.75) {
				return 1;
			} else if (dataPoint.getFirstKPIValue() >= -98 && dataPoint.getSecondKPIValue() < 3.75) {
				return 2;
			} else if (dataPoint.getFirstKPIValue() < -98 && dataPoint.getSecondKPIValue() < 3.75) {
				return 3;
			} else if (dataPoint.getFirstKPIValue() < -98 && dataPoint.getSecondKPIValue() >= 3.75) {
				return 4;
			}
		} else if (ScatterChartConstants.CLUTTER_RU.equalsIgnoreCase(clutter)) {
			if (dataPoint.getFirstKPIValue() >= -101 && dataPoint.getSecondKPIValue() >= 3.75) {
				return 1;
			} else if (dataPoint.getFirstKPIValue() >= -101 && dataPoint.getSecondKPIValue() < 3.75) {
				return 2;
			} else if (dataPoint.getFirstKPIValue() < -101 && dataPoint.getSecondKPIValue() < 3.75) {
				return 3;
			} else if (dataPoint.getFirstKPIValue() < -101 && dataPoint.getSecondKPIValue() >= 3.75) {
				return 4;
			}
		} else {
			return 0;
		}
		return 0;
	}

	/**
	 * Gets the quardent by SINR and DL.
	 *
	 * @param dataPoint the data point
	 * @return the quardent by SINR and DL
	 */
	public static Integer getQuardentBySINRAndDL(DataPoint dataPoint) {
		// Add check for clutter when required
		if (dataPoint.getFirstKPIValue() >= 1.5 && dataPoint.getSecondKPIValue() >= 3.75) {
			return 1;
		} else if (dataPoint.getFirstKPIValue() >= 1.5 && dataPoint.getSecondKPIValue() < 3.75) {
			return 2;
		} else if (dataPoint.getFirstKPIValue() < 1.5 && dataPoint.getSecondKPIValue() < 3.75) {
			return 3;
		} else if (dataPoint.getFirstKPIValue() < 1.5 && dataPoint.getSecondKPIValue() >= 3.75) {
			return 4;
		} else {
			return 0;
		}
	}

	/**
	 * Gets the quadrant by RSRP and SINR.
	 *
	 * @param rsrp the rsrp
	 * @param sinr the sinr
	 * @param clutter the clutter
	 * @return the quadrant by RSRP and SINR
	 */
	public static Integer getQuadrantByRSRPAndSINR(Double rsrp, Double sinr, String clutter) {
		if (ScatterChartConstants.CLUTTER_DU.equalsIgnoreCase(clutter)) {
			if (rsrp >= -95 && sinr >= 1.5) {
				return 1;
			} else if (rsrp >= -95 && sinr < 1.5) {
				return 2;
			} else if (rsrp < -95 && sinr < 1.5) {
				return 3;
			} else if (rsrp < -95 && sinr >= 1.5) {
				return 4;
			}

		} else if (ScatterChartConstants.CLUTTER_U.equalsIgnoreCase(clutter)) {
			if (rsrp >= -96.5 && sinr >= 1.5) {
				return 1;
			} else if (rsrp >= -96.5 && sinr < 1.5) {
				return 2;
			} else if (rsrp < -96.5 && sinr < 1.5) {
				return 3;
			} else if (rsrp < -96.5 && sinr >= 1.5) {
				return 4;
			}
		} else if (ScatterChartConstants.CLUTTER_SU.equalsIgnoreCase(clutter)) {
			if (rsrp >= -98 && sinr >= 1.5) {
				return 1;
			} else if (rsrp >= -98 && sinr < 1.5) {
				return 2;
			} else if (rsrp < -98 && sinr < 1.5) {
				return 3;
			} else if (rsrp < -98 && sinr >= 1.5) {
				return 4;
			}
		} else if (ScatterChartConstants.CLUTTER_RU.equalsIgnoreCase(clutter)) {
			if (rsrp >= -101 && sinr >= 1.5) {
				return 1;
			} else if (rsrp >= -101 && sinr < 1.5) {
				return 2;
			} else if (rsrp < -101 && sinr < 1.5) {
				return 3;
			} else if (rsrp < -101 && sinr >= 1.5) {
				return 4;
			}
		} else {
			return 0;
		}
		return 0;
	}

	/**
	 * Builds the response.
	 *
	 * @param scatterChartData the scatter chart data
	 * @param morphology the clutter
	 * @param geographyList the geography list
	 * @return the string
	 */
	@SuppressWarnings("unchecked")
	public static String buildResponse(ScatterChartData scatterChartData, String morphology, List<String> geographyList) {
		Map map = new HashMap();
		Long total = scatterChartData.getAllQuadrantCount();
		Map innerMap = new HashMap();
		innerMap.put(ScatterChartConstants.POINTS, scatterChartData.getQuadSecondPoints());
		if (scatterChartData.getSecondQuadrantCount() >  ForesightConstants.ZERO) {
			Double secondQuadrantPercent = calculatePercentage(scatterChartData.getSecondQuadrantCount(), total);
			scatterChartData.aggregateQuadPercentage(secondQuadrantPercent);
			innerMap.put(ScatterChartConstants.PERCENT, secondQuadrantPercent);
		} else {
			innerMap.put(ScatterChartConstants.PERCENT, ForesightConstants.ZERO);
		}
		map.put(ScatterChartConstants.INTERFERENCE, innerMap);

		innerMap = new HashMap();
		innerMap.put(ScatterChartConstants.POINTS, scatterChartData.getQuadThirdPoints());
		if (scatterChartData.getThirdQuadrantCount() > ForesightConstants.ZERO) {
			Double thirdQuadrantPercent = calculatePercentage(scatterChartData.getThirdQuadrantCount(), total);
			scatterChartData.aggregateQuadPercentage(thirdQuadrantPercent);
			innerMap.put(ScatterChartConstants.PERCENT, thirdQuadrantPercent);
		} else {
			innerMap.put(ScatterChartConstants.PERCENT, ForesightConstants.ZERO);
		}
		map.put(ScatterChartConstants.EXPECTED, innerMap);

		innerMap = new HashMap();
		innerMap.put(ScatterChartConstants.POINTS, scatterChartData.getQuadFourthPoints());
		if (scatterChartData.getFourthQuadrantCount() > ForesightConstants.ZERO) {
			Double forthQuadrantPercent = calculatePercentage(scatterChartData.getFourthQuadrantCount(), total);
			scatterChartData.aggregateQuadPercentage(forthQuadrantPercent);
			innerMap.put(ScatterChartConstants.PERCENT, forthQuadrantPercent);
		} else {
			innerMap.put(ScatterChartConstants.PERCENT, ForesightConstants.ZERO);
		}
		map.put(ScatterChartConstants.POTENTIAL, innerMap);

		innerMap = new HashMap();
		innerMap.put(ScatterChartConstants.POINTS, scatterChartData.getQuadFirstPoints());
		if (scatterChartData.getFirstQuadrantCount() > ForesightConstants.ZERO) {
			Double firstQuadrantPercent = ScatterChartConstants.DOUBLE_HUNDRED;
			if (scatterChartData.getSumOfPercentage() > ForesightConstants.ZERO) {
				String sumOfQuard = String.format(ScatterChartConstants.TWO_DECIMAL_PLACE, scatterChartData.getSumOfPercentage());
				if (isValidDoubleStringValue(sumOfQuard)) {
					firstQuadrantPercent = firstQuadrantPercent - Double.parseDouble(sumOfQuard);
				}
			}
			innerMap.put(ScatterChartConstants.PERCENT, firstQuadrantPercent);
		} else {
			innerMap.put(ScatterChartConstants.PERCENT, ForesightConstants.ZERO);
		}
		map.put(ScatterChartConstants.GOOD_COVERAGE, innerMap);

		map.put(ScatterChartConstants.GEOGRAPHY_LIST, geographyList);
		map.put(ScatterChartConstants.MORPHOLOGY_NAME, morphology);
		Map<String, Double> minAndMaxMap = scatterChartData.getMinAndMaxMap();
		if (minAndMaxMap != null && !minAndMaxMap.isEmpty()) {
			map.putAll(minAndMaxMap);
			Map<String, Double> trendLine = getTrendLine(scatterChartData);
			map.put(ScatterChartConstants.TREND_LINE, trendLine);
			minAndMaxMap.clear();
		}
		return new Gson().toJson(map);
	}

	/**
	 * Gets the trend line.
	 *
	 * @param scatterChartData the scatter chart data
	 * @return the trend line
	 */
	private static Map<String, Double> getTrendLine(ScatterChartData scatterChartData) {
		/** Equation y = mx + b */
		logger.info("going to calculate value for line ");
		final Map<String, Double> trendLine = new HashMap<>();
		Map<String, Double> minAndMaxMap = scatterChartData.getMinAndMaxMap();
		try {
			if (!minAndMaxMap.get(ScatterChartConstants.X_MIN).equals(minAndMaxMap.get(ScatterChartConstants.X_MAX))
					&& !minAndMaxMap.get(ScatterChartConstants.Y_MIN).equals(minAndMaxMap.get(ScatterChartConstants.Y_MAX))) {
				final Integer n = scatterChartData.getxPoints().size();
				final Double m = ((n * scatterChartData.getSumOfXY()) - (scatterChartData.getxTotal() * scatterChartData.getyTotal())) /
						((n * scatterChartData.getSumOfXSquare()) - Math.pow(scatterChartData.getxTotal(), 2));
				final Double b = (scatterChartData.getyTotal() - (m * scatterChartData.getxTotal())) / n;

				final Double ymin = (m * minAndMaxMap.get(ScatterChartConstants.X_MIN)) + b;
				if (ymin < minAndMaxMap.get(ScatterChartConstants.Y_MIN)) {
					trendLine.put(ScatterChartConstants.START_X, (minAndMaxMap.get(ScatterChartConstants.Y_MIN) - b) / m);
					trendLine.put(ScatterChartConstants.START_Y, minAndMaxMap.get(ScatterChartConstants.Y_MIN));
				} else {
					trendLine.put(ScatterChartConstants.START_X, minAndMaxMap.get(ScatterChartConstants.X_MIN));
					trendLine.put(ScatterChartConstants.START_Y, ymin);
				}

				final Double ymax = (m * minAndMaxMap.get(ScatterChartConstants.X_MAX)) + b;
				if (ymax > minAndMaxMap.get(ScatterChartConstants.Y_MAX)) {
					trendLine.put(ScatterChartConstants.END_X, (minAndMaxMap.get(ScatterChartConstants.Y_MAX) - b) / m);
					trendLine.put(ScatterChartConstants.END_Y, minAndMaxMap.get(ScatterChartConstants.Y_MAX));
				} else {
					trendLine.put(ScatterChartConstants.END_X, minAndMaxMap.get(ScatterChartConstants.X_MAX));
					trendLine.put(ScatterChartConstants.END_Y, ymax);
				}
			} else {
				logger.info(" values are equals for xmin {} and xmax {} and ymin {} and ymax {}",
						minAndMaxMap.get(ScatterChartConstants.X_MIN), minAndMaxMap.get(ScatterChartConstants.X_MAX), minAndMaxMap.get(ScatterChartConstants.Y_MIN),
						minAndMaxMap.get(ScatterChartConstants.Y_MAX));
			}
		} catch (final Exception e) {
			logger.error("Exception in line calculating : " + e.getMessage());
		}
		return trendLine;
	}

	/**
	 * Calculate percentage.
	 *
	 * @param count the count
	 * @param total the total
	 * @return the double
	 */
	public static Double calculatePercentage(Long count, Long total) {
		Double percentage = ScatterChartConstants.ZERO_DOUBLE;
		try {
			percentage = Double.parseDouble(String.format(ScatterChartConstants.TWO_DECIMAL_PLACE,
					(count * ScatterChartConstants.DOUBLE_HUNDRED) / total));
		} catch (Exception e) {
			logger.error("Exception in calculation percentage : {}", e.getMessage());
		}
		return percentage;
	}

	/**
	 * Checks if is valid double string value.
	 *
	 * @param value the value
	 * @return the boolean
	 */
	public static Boolean isValidDoubleStringValue(String value) {
		return (value != null && !value.equalsIgnoreCase(ForesightConstants.BLANK_STRING)
				&& !value.equalsIgnoreCase("NaN"));
	}
	/**
	 * Ceil decimal place.
	 *
	 * @param value the value
	 * @return the double
	 */
	public static Double ceilDecimalPlace(Double value) {
		if (value >= 0) {
			if ((value - value.intValue()) > 0.5) {
				return value.intValue() + 1d;
			} else {
				return value.intValue() + 0.5d;
			}
		} else {
			if ((value - value.intValue()) < -0.5) {
				return value.intValue() - 0.5;
			} else if ((value - value.intValue()) > -0.1) {
				final DecimalFormat df = new DecimalFormat("##.##");
				df.setRoundingMode(RoundingMode.DOWN);
				return Double.parseDouble(df.format(value));
			} else {
				return value.intValue() - 0.0;
			}
		}
	}

	public static Double setPrecisionForDouble(Double value, String precision) {
		final DecimalFormat df = new DecimalFormat(precision);
		df.setRoundingMode(RoundingMode.DOWN);
		return Double.parseDouble(df.format(value));
	}
	
	/**
	 * Validates request.
	 *
	 * @param request the request
	 * @param latitude the latitude
	 * @param longitude the longitude
	 * @param zoomLevel the zoom level
	 * @param kpi1 the kpi 1
	 * @param kpi2 the kpi 2
	 * @throws RestException the rest exception
	 */
	public static void validateRequest(ScatterChartRequest request, Double latitude,
			Double longitude, Integer zoomLevel, String kpi1, String kpi2) {
		if (request == null || request.getColumns() == null || request.getKpiTable() == null
				|| request.getGeographyTable() == null || zoomLevel == null || kpi1 == null || kpi2 == null)
			throw new RestException(ForesightConstants.INVALID_PARAMETER);

		if ((request.getGeographyList() == null || request.getGeographyList().isEmpty())
				&& (latitude == null || longitude == null))
			throw new RestException(ForesightConstants.INVALID_PARAMETER);
	}

	/**
	 * Gets the dropwizard url.
	 *
	 * @param startDate the start date
	 * @param endDate the end date
	 * @param zoomLevel the zoom level
	 * @param kpi1 the kpi 1
	 * @param kpi2 the kpi 2
	 * @return the dropwizard url
	 */
	public static String getDropwizardUrl(String startDate, String endDate, Integer zoomLevel) {
		List<String> paramList = new ArrayList<>();
		paramList.add(startDate);
		paramList.add(endDate);
		paramList.add(String.valueOf(zoomLevel));
		return ConfigUtils.getString(ConfigEnum.SCATTER_CHART_DROPWIZARD_URL.getValue())
				+ ForesightConstants.QUESTIONMARK
				+ GenericMapUtils.createGenericQuery(paramList, ScatterChartConstants.START_DATE_PARAM,
						ScatterChartConstants.END_DATE_PARAM, ScatterChartConstants.ZOOM_PARAM);
	}
	
	/**
	 * Gets the geography name from response.
	 *
	 * @param response the response
	 * @return the geography name from response
	 */
	public static List<String> getGeographyNameFromResponse(String response) {
		try {
			List<List<String>> responseData = new Gson().fromJson(response, new TypeToken<List<List<String>>>() {
			}.getType());
			if (responseData != null && !responseData.isEmpty()) {
				return responseData.get(0);
			}
		} catch (Exception e) {
			logger.error("Exception in converting response : {}", ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}
	

	/**
	 * Validate request for image.
	 *
	 * @param request the request
	 * @param latitude the latitude
	 * @param longitude the longitude
	 * @param zoomLevel the zoom level
	 * @param frequency the frequency
	 * @param siteStatus the site status
	 * @throws RestException the rest exception
	 */
	public static void validateRequestForImage(ScatterChartRequest request, Double latitude, Double longitude,
			Integer zoomLevel, String frequency, String siteStatus) {
		validateRequest(request, latitude, longitude, zoomLevel, frequency, siteStatus);
		if (request.getKpi1Date() == null || request.getKpi2Date() == null) {
			throw new RestException(ForesightConstants.INVALID_PARAMETER);
		}
	}
	
	/**
	 * Gets the dropwizard url for image.
	 *
	 * @param zoomLevel the zoom level
	 * @param frequency the frequency
	 * @param siteStatus the site status
	 * @return the dropwizard url for image
	 */
	public static String getDropwizardUrlForImage(Integer zoomLevel, String frequency, String siteStatus) {
		List<String> paramList = new ArrayList<>();
		paramList.add(String.valueOf(zoomLevel));
		paramList.add(frequency);
		paramList.add(siteStatus);
		return ConfigUtils.getString(ConfigEnum.SCATTER_CHART_IMAGE_DROPWIZARD_URL.getValue())
				+ ForesightConstants.QUESTIONMARK
				+ GenericMapUtils.createGenericQuery(paramList, ScatterChartConstants.ZOOM_PARAM,
						ScatterChartConstants.FREQUENCY, ScatterChartConstants.SITE_STATUS);
	}

}

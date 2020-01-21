package com.inn.foresight.core.kpicomparison.service.impl;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.transaction.Transactional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.http.HttpException;
import com.inn.commons.http.HttpPostRequest;
import com.inn.commons.http.HttpRequest;
import com.inn.commons.io.image.ImageUtils;
import com.inn.commons.maps.rowkey.RowKeyUtils;
import com.inn.commons.maps.tiles.BigTile;
import com.inn.commons.maps.tiles.Tile;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.utils.Utils;
import com.inn.foresight.core.generic.utils.ConfigEnum;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.kpicomparison.dao.IKpiComparisonDao;
import com.inn.foresight.core.kpicomparison.service.IKPIComparisonService;
import com.inn.foresight.core.kpicomparison.utils.KpiComparisonConstants;
import com.inn.foresight.core.kpicomparison.wrapper.KPIComparisonRequest;
import com.inn.foresight.core.maplayer.service.IGenericMapService;
import com.inn.foresight.core.maplayer.utils.GenericMapUtils;
import com.inn.product.systemconfiguration.dao.SystemConfigurationDao;
import com.inn.product.systemconfiguration.model.SystemConfiguration;
import com.inn.product.um.geography.dao.GeographyL4Dao;
import com.inn.product.um.geography.model.GeographyL4;

/**
 * The Class KPIComparisonServiceImpl.
 */
@Service("KpiComparisonServiceImpl")
public class KPIComparisonServiceImpl implements IKPIComparisonService {
	
	/** The generic service. */
	@Autowired
	private IGenericMapService genericService;

	/** The l 4 dao. */
	@Autowired
	private GeographyL4Dao l4Dao;
	
	@Autowired
	private SystemConfigurationDao systemConfDao = null;
	
	@Autowired
	private IKpiComparisonDao kpiComparisonDao = null;

	/** The logger. */
	private static Logger logger = LogManager.getLogger(KPIComparisonServiceImpl.class);

	/** The Constant CLUSTER_INDEX. */
	private static final int CLUSTER_INDEX = 3;

	/** The clutter map. */
	private static Map<String, String> clutterMap = null;

//	--------------- micro service start
	/** The Constant redColor. */
	private static final Color redColor = new Color(255, 0, 0);

	/** The Constant greenColor. */
	private static final Color greenColor = new Color(91, 191, 67);

	/** The Constant orangeColor. */
	private static final Color orangeColor = new Color(222, 133, 51);

	/** The Constant yellowColor. */
	private static final Color yellowColor = new Color(255, 190, 0);

	/** The kpi1 color value map. */
	private Map<Integer, Double> kpi1ColorValueMap;

	/** The kpi2 color value map. */
	private Map<Integer, Double> kpi2ColorValueMap;

	private static int ZERO = 0;

	private static int ONE = 1;

	private static int TWO = 2;

	private static int FOUR = 4;

	private static String NAME_LIST = "TILE_RANGES";

	private static String DATE_FORMATE = "ddMMyy";

	private static String RSRP = "RSRP";

	private static String SINR = "SINR";

	private static String DL = "DL";

	private static Map<String, Integer> zoneMap = null;

	private static Map<String, Integer> clutterColorMap = null;
	
	public static final Integer ZONE_ZOOM_LEVEL=6;
//	----------------micro service end
	
	
	
	/* (non-Javadoc)
	 * @see com.inn.foresight.module.kpicomparison.service.IKPIComparisonService#getKPIComparison(java.lang.String, java.lang.String, java.lang.String, com.inn.foresight.module.kpicomparison.wrapper.KPIComparisonRequest)
	 */
	@Override
	public BufferedImage getKPIComparison(String tileId, String siteStatus, String band, KPIComparisonRequest request) {
		try {
			List<String> valueList = new ArrayList<>();
			valueList.add(tileId);
			valueList.add(siteStatus);
			valueList.add(band);

			String query = GenericMapUtils.createGenericQuery(valueList, GenericMapUtils.TILEID,
					GenericMapUtils.SITESTATUS, GenericMapUtils.BAND);
			String url = ConfigUtils.getString(ConfigEnum.MICRO_SERVICE_BASE_URL.getValue())
					+ ConfigUtils.getString(ConfigEnum.DROPWIZARD_GENERIC_KPI_COMPARISON_URL.getValue());
			url = url + ForesightConstants.QUESTIONMARK + query;
			logger.info("Rest service Image url {} in getKPIComparision() ", url);
			return getComparisonImage(request, url);
		} catch (Exception e) {
			logger.error("Error in parsing stream data in getKPIComparision () {}", Utils.getStackTrace(e));
		}
		return null;
	}

	/**
	 * Gets the comparison image.
	 *
	 * @param request the request
	 * @param url the url
	 * @return the comparison image
	 * @throws HttpException the http exception
	 */
	private BufferedImage getComparisonImage(KPIComparisonRequest request, String url) throws HttpException {
		BufferedImage img = null;
		try {
			String json = new Gson().toJson(request);
			StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
			HttpRequest postRequest = new HttpPostRequest(url, entity);
			img = postRequest.getImage();
		} catch (Exception e) {
			logger.error("Exception in getting comparision image from micro-service : {}", Utils.getStackTrace(e));
		}
		return img;
	}

	/* (non-Javadoc)
	 * @see com.inn.foresight.module.kpicomparison.service.IKPIComparisonService#getKPIDataComparison(com.inn.foresight.module.kpicomparison.wrapper.KPIComparisonRequest)
	 */
	@Transactional
	@Override
	public List<List<String>> getKPIDataComparison(KPIComparisonRequest request) {
		logger.info("going to get KPI comparision data for request : {}", request);
		try {
			if (clutterMap == null) {
				clutterMap = getClutterMap();
			}
			String responseData = genericService.getDataFromHbase(request.getColumnList(), request.getTable(),
					request.getZoom(), request.getnELat(), request.getnELng(), request.getsWLat(), request.getsWLng(),
					request.getMinDate(), request.getMaxDate(), request.getPostfix(),null,null);
			List<List<String>> dataList = parseResponse(responseData);

			if (!dataList.isEmpty() && clutterMap != null) {
				for (List<String> list : dataList) {
					String clutter = clutterMap.get(list.get(CLUSTER_INDEX)); // cluster sequence no. in col. list from
																				// UI
					if (clutter != null) {
						list.remove(CLUSTER_INDEX);
						list.add(CLUSTER_INDEX, clutter);
					}
				}
			}
			return dataList;
		} catch (Exception e) {
			logger.error("Exception in getKPIDataComparison : {}", ExceptionUtils.getStackTrace(e));
			throw new RestException(e);
		}
	}

	/**
	 * Gets the clutter map.
	 *
	 * @return the clutter map
	 * @throws DaoException the dao exception
	 */
	public Map<String, String> getClutterMap() {
		logger.info("Loading clutter map in KPI comparison.");
		Map<String, String> l4ClutterMap = new HashMap<>();
		List<GeographyL4> geographyL4List = l4Dao.getGeoList();
		if (geographyL4List != null) {
			for (GeographyL4 l4Obj : geographyL4List) {
				l4ClutterMap.put(l4Obj.getName(), l4Obj.getMorphology());
			}
		}
		return l4ClutterMap;
	}

	/**
	 * Parses the response.
	 *
	 * @param response the response
	 * @return the list
	 */
	public List<List<String>> parseResponse(String response) {
		List<List<String>> parsedResponse = new ArrayList<>();
		try {
			if (!response.isEmpty()) {
				return new Gson().fromJson(response, new TypeToken<List<List<String>>>() {
				}.getType());
			}
			return parsedResponse;
		} catch (Exception e) {
			logger.error("Exception while parsing response {}", ExceptionUtils.getStackTrace(e));
		}
		return parsedResponse;
	}
	
	
//	------------------- micro service
	@Override
	public BufferedImage getKPIComparisonForMS(String tileId, String siteStatus, String frequency, KPIComparisonRequest request) {
		BufferedImage generatedImage = null;
		BufferedImage kpi1Image = null;
		BufferedImage kpi2Image = null;
		BufferedImage clutterImage = null;
		try {
			Tile tileObj = new Tile(tileId);
			Map<String, BufferedImage> tileImageMap = new HashMap<>();
			BigTile bigTile = new BigTile();
			List<Tile> tileList = bigTile.getBaseZoomChildTiles(tileObj);

			for (Tile tile : tileList) {
				kpi1Image = ImageUtils.toBufferedImage(genericService.getImageForKpiAndZone(request.getTable(), tile.getIdWithZoom(),
						request.getKpi1Column(), request.getKpi1(), request.getKpi1Date(), siteStatus, null, frequency));
				kpi2Image = ImageUtils.toBufferedImage(genericService.getImageForKpiAndZone(request.getTable(), tile.getIdWithZoom(),
						request.getKpi2Column(), request.getKpi2(), request.getKpi2Date(), siteStatus, null, frequency));

				if (zoneMap == null) {
					zoneMap = kpiComparisonDao.getZoneMap();
				}
				String rowkey = RowKeyUtils.getRowKeyForTileTable(tile, "", zoneMap, ZONE_ZOOM_LEVEL);
				clutterImage = kpiComparisonDao.getClutterImage(rowkey);

				if (clutterColorMap == null) {
					clutterColorMap = kpiComparisonDao.getClutterColors(KpiComparisonConstants.MORPHOLOGY_BOUNDARY_COLORS);
				}
				populateKPIColorMap(request.getKpi1(), request.getKpi2());
				generatedImage = createComparisonImage(kpi1Image, kpi2Image, clutterImage, request.getKpi1(),
						request.getKpi2());
			}
			tileImageMap.put(tileId, generatedImage);
			return bigTile.getMergedBigTile(tileObj, tileImageMap);
		} catch (Exception e) {
			logger.error("Exception while getting image in getKpiComparison {}, {}", ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	public void populateKPIColorMap(String kpi1Name, String kpi2Name) {
		String kpiColors = null;
		List<String> nameList = new ArrayList<>();
		nameList.add(NAME_LIST);
		try {
			List<SystemConfiguration> colorList = systemConfDao.getSystemConfigurationValueFromNameList(nameList);
			if (colorList != null && !colorList.isEmpty()) {
				kpiColors = colorList.get(ZERO).getValue();
			}
			if (kpiColors != null && !kpiColors.isEmpty()) {
				Map<String, Map<List<Integer>, Double>> kpiWiseColor = loadColorMap(kpiColors);
				Map<List<Integer>, Double> colorMapKPI1Wise = kpiWiseColor.get(kpi1Name);
				Map<List<Integer>, Double> colorMapKPI2Wise = kpiWiseColor.get(kpi2Name);

				kpi1ColorValueMap = setKPIColorToMap(colorMapKPI1Wise);
				kpi2ColorValueMap = setKPIColorToMap(colorMapKPI2Wise);
			}
		} catch (Exception e) {
			logger.error("Exception while populatin KPI color map {}", ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtils.getStackTrace(e));
		}
	}

	public Map<String, Map<List<Integer>, Double>> loadColorMap(String kpiColors) {
		Map<String, Map<List<Integer>, Double>> kpiWiseColor = new HashMap<>();
		try {
			return new Gson().fromJson(kpiColors, new TypeToken<Map<String, Map<List<Integer>, Double>>>() {
			}.getType());
		} catch (Exception e) {
			logger.error("Exception while loading color map {}", ExceptionUtils.getStackTrace(e));
		}
		return kpiWiseColor;
	}

	public Map<Integer, Double> setKPIColorToMap(Map<List<Integer>, Double> colorMap) {

		Map<Integer, Double> colorValueMap = new HashMap<>();
		if (colorMap != null && !colorMap.isEmpty()) {
			for (Entry<List<Integer>, Double> kpiEntry : colorMap.entrySet()) {
				List<Integer> kpiColorList = kpiEntry.getKey();
				colorValueMap.put(
						(new Color(kpiColorList.get(ZERO), kpiColorList.get(ONE), kpiColorList.get(TWO)).getRGB()),
						kpiEntry.getValue());
			}
		}
		return colorValueMap;
	}

	public BufferedImage createComparisonImage(BufferedImage kpiImage1, BufferedImage kpiImage2,
			BufferedImage clutterImage, String kpi1Name, String kpi2Name) {
		BufferedImage generatedImage = null;
		try {
			int currentcolorKPI1 = 0;
			int currentcolorKPI2 = 0;
			generatedImage = new BufferedImage(ForesightConstants.TILE_SIZE, ForesightConstants.TILE_SIZE,
					BufferedImage.TYPE_INT_ARGB);
			Graphics2D graphics = (Graphics2D) generatedImage.getGraphics();

			if (kpiImage1 != null && kpiImage2 != null && clutterImage != null) {

				for (int x = 0; x < ForesightConstants.TILE_SIZE; x++) {
					for (int y = 0; y < ForesightConstants.TILE_SIZE; y++) {
						try {
							currentcolorKPI1 = kpiImage1.getRGB(x, y);
							currentcolorKPI2 = kpiImage2.getRGB(x, y);
							if (currentcolorKPI1 != 0 && currentcolorKPI2 != 0) {

								double kpi1Value = kpi1ColorValueMap.get(currentcolorKPI1);
								double kpi2Value = kpi2ColorValueMap.get(currentcolorKPI2);

								setGeneratedImageColorByClutter(kpi1Value, kpi2Value, clutterImage, graphics, x, y,
										kpi1Name, kpi2Name);
							}
						} catch (Exception e) {
							logger.error(
									"Inside @class ComparisonServiceImpl Method createComparisonImage Exception {}",
									ExceptionUtils.getStackTrace(e));
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("Exception inside ComparisonServiceImpl.createComparisonImage {}"
					+ ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtils.getStackTrace(e));
		}
		return generatedImage;
	}

	/**
	 * Sets the generated image color by clutter.
	 *
	 * @param kpi1Value
	 *            the kpi1 value
	 * @param kpi2Value
	 *            the kpi2 value
	 * @param clutterImage
	 *            the clutter image
	 * @param graphics
	 *            the graphics
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 */
	public void setGeneratedImageColorByClutter(double kpi1Value, double kpi2Value, BufferedImage clutterImage,
			Graphics2D graphics, int x, int y, String kpi1Name, String kpi2Name) {
		int color = clutterImage.getRGB(x, y);

		if (kpi1Name.equalsIgnoreCase(RSRP) && kpi2Name.equalsIgnoreCase(SINR)) {
			setColorInSMLTileForRSRPAndSINR(color, kpi1Value, kpi2Value, graphics, x, y);

		} else if (kpi1Name.equalsIgnoreCase(RSRP) && kpi2Name.equalsIgnoreCase(DL)) {
			setColorInSMLTileForRSRPAndDL(color, kpi1Value, kpi2Value, graphics, x, y);

		} else {
			setColorInSMLTileForSINRAndDL(color, kpi1Value, kpi2Value, graphics, x, y);
		}

	}

	/**
	 * Sets the color in SML tile.
	 *
	 * @param color
	 *            the color
	 * @param kpi1Value
	 *            the kpi1 value
	 * @param kpi2Value
	 *            the kpi2 value
	 * @param graphics
	 *            the graphics
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 */
	public void setColorInSMLTileForRSRPAndSINR(int color, double kpi1Value, double kpi2Value, Graphics2D graphics,
			int x, int y) {

		if (color == clutterColorMap.get("DU")) {
			if (kpi1Value >= -95 && kpi2Value >= 1.5) {
				graphics.setColor(greenColor);
				graphics.drawLine(x, y, x, y);
			} else if ((kpi1Value >= -95 && kpi2Value < 1.5)) {
				graphics.setColor(orangeColor);
				graphics.drawLine(x, y, x, y);
			} else if ((kpi1Value < -95 && kpi2Value < 1.5)) {
				graphics.setColor(redColor);
				graphics.drawLine(x, y, x, y);
			} else if ((kpi1Value < -95 && kpi2Value >= 1.5)) {
				graphics.setColor(yellowColor);
				graphics.drawLine(x, y, x, y);
			}
		} else if (color == clutterColorMap.get("U")) {
			if ((kpi1Value >= -96.5 && kpi2Value >= 1.5)) {
				graphics.setColor(greenColor);
				graphics.drawLine(x, y, x, y);
			} else if ((kpi1Value >= -96.5 && kpi2Value < 1.5)) {
				graphics.setColor(orangeColor);
				graphics.drawLine(x, y, x, y);
			} else if ((kpi1Value < -96.5 && kpi2Value < 1.5)) {
				graphics.setColor(redColor);
				graphics.drawLine(x, y, x, y);
			} else if ((kpi1Value < -96.5 && kpi2Value >= 1.5)) {
				graphics.setColor(yellowColor);
				graphics.drawLine(x, y, x, y);
			}

		} else if (color == clutterColorMap.get("SU")) {
			if ((kpi1Value >= -98 && kpi2Value >= 1.5)) {
				graphics.setColor(greenColor);
				graphics.drawLine(x, y, x, y);
			} else if ((kpi1Value >= -98 && kpi2Value < 1.5)) {
				graphics.setColor(orangeColor);
				graphics.drawLine(x, y, x, y);
			} else if ((kpi1Value < -98 && kpi2Value < 1.5)) {
				graphics.setColor(redColor);
				graphics.drawLine(x, y, x, y);
			} else if ((kpi1Value < -98 && kpi2Value >= 1.5)) {
				graphics.setColor(yellowColor);
				graphics.drawLine(x, y, x, y);
			}

		} else if (color == clutterColorMap.get("RU")) {
			if ((kpi1Value >= -101 && kpi2Value >= 1.5)) {
				graphics.setColor(greenColor);
				graphics.drawLine(x, y, x, y);
			} else if ((kpi1Value >= -101 && kpi2Value < 1.5)) {
				graphics.setColor(orangeColor);
				graphics.drawLine(x, y, x, y);
			} else if ((kpi1Value < -101 && kpi2Value < 1.5)) {
				graphics.setColor(redColor);
				graphics.drawLine(x, y, x, y);
			} else if ((kpi1Value < -101 && kpi2Value >= 1.5)) {
				graphics.setColor(yellowColor);
				graphics.drawLine(x, y, x, y);
			}
		}
	}

	public void setColorInSMLTileForRSRPAndDL(int color, double kpi1Value, double kpi2Value, Graphics2D graphics, int x,
			int y) {

		if (color == clutterColorMap.get("DU")) {
			if (kpi1Value >= -95 && kpi2Value >= 3.75) {
				graphics.setColor(greenColor);
				graphics.drawLine(x, y, x, y);
			} else if ((kpi1Value >= -95 && kpi2Value < 3.75)) {
				graphics.setColor(orangeColor);
				graphics.drawLine(x, y, x, y);
			} else if ((kpi1Value < -95 && kpi2Value < 3.75)) {
				graphics.setColor(redColor);
				graphics.drawLine(x, y, x, y);
			} else if ((kpi1Value < -95 && kpi2Value >= 3.75)) {
				graphics.setColor(yellowColor);
				graphics.drawLine(x, y, x, y);
			}
		} else if (color == clutterColorMap.get("U")) {
			if ((kpi1Value >= -96.5 && kpi2Value >= 3.75)) {
				graphics.setColor(greenColor);
				graphics.drawLine(x, y, x, y);
			} else if ((kpi1Value >= -96.5 && kpi2Value < 3.75)) {
				graphics.setColor(orangeColor);
				graphics.drawLine(x, y, x, y);
			} else if ((kpi1Value < -96.5 && kpi2Value < 3.75)) {
				graphics.setColor(redColor);
				graphics.drawLine(x, y, x, y);
			} else if ((kpi1Value < -96.5 && kpi2Value >= 3.75)) {
				graphics.setColor(yellowColor);
				graphics.drawLine(x, y, x, y);
			}

		} else if (color == clutterColorMap.get("SU")) {
			if ((kpi1Value >= -98 && kpi2Value >= 3.75)) {
				graphics.setColor(greenColor);
				graphics.drawLine(x, y, x, y);
			} else if ((kpi1Value >= -98 && kpi2Value < 3.75)) {
				graphics.setColor(orangeColor);
				graphics.drawLine(x, y, x, y);
			} else if ((kpi1Value < -98 && kpi2Value < 3.75)) {
				graphics.setColor(redColor);
				graphics.drawLine(x, y, x, y);
			} else if ((kpi1Value < -98 && kpi2Value >= 3.75)) {
				graphics.setColor(yellowColor);
				graphics.drawLine(x, y, x, y);
			}

		} else if (color == clutterColorMap.get("RU")) {
			if ((kpi1Value >= -101 && kpi2Value >= 3.75)) {
				graphics.setColor(greenColor);
				graphics.drawLine(x, y, x, y);
			} else if ((kpi1Value >= -101 && kpi2Value < 3.75)) {
				graphics.setColor(orangeColor);
				graphics.drawLine(x, y, x, y);
			} else if ((kpi1Value < -101 && kpi2Value < 3.75)) {
				graphics.setColor(redColor);
				graphics.drawLine(x, y, x, y);
			} else if ((kpi1Value < -101 && kpi2Value >= 3.75)) {
				graphics.setColor(yellowColor);
				graphics.drawLine(x, y, x, y);
			}
		}
	}

	public void setColorInSMLTileForSINRAndDL(int color, double kpi1Value, double kpi2Value, Graphics2D graphics, int x,
			int y) {
		if (color == clutterColorMap.get("DU") || color == clutterColorMap.get("U")
				|| color == clutterColorMap.get("SU") || color == clutterColorMap.get("RU")) {
			if ((kpi1Value >= 1.5 && kpi2Value >= 3.75)) {
				graphics.setColor(greenColor);
				graphics.drawLine(x, y, x, y);
			} else if ((kpi1Value >= 1.5 && kpi2Value < 3.75)) {
				graphics.setColor(orangeColor);
				graphics.drawLine(x, y, x, y);
			} else if ((kpi1Value < 1.5 && kpi2Value < 3.75)) {
				graphics.setColor(redColor);
				graphics.drawLine(x, y, x, y);
			} else if ((kpi1Value < 1.5 && kpi2Value >= 3.75)) {
				graphics.setColor(yellowColor);
				graphics.drawLine(x, y, x, y);
			}
		}
	}

}

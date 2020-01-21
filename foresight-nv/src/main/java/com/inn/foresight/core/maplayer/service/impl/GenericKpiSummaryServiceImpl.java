package com.inn.foresight.core.maplayer.service.impl;

import static com.inn.commons.maps.rowkey.RowKeyUtils.KPI.DL;
import static com.inn.commons.maps.rowkey.RowKeyUtils.KPI.RSRP;
import static com.inn.commons.maps.rowkey.RowKeyUtils.KPI.SINR;
import static com.inn.commons.maps.rowkey.RowKeyUtils.KPI.UL;
import static com.inn.commons.maps.rowkey.RowKeyUtils.SITE_STATUS.ONAIR;
import static com.inn.commons.maps.rowkey.RowKeyUtils.SITE_STATUS.PLANNED;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.math3.stat.descriptive.rank.Median;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.inn.commons.Symbol;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.hadoop.hbase.HBaseResult;
import com.inn.commons.http.HttpPostRequest;
import com.inn.commons.index.SpatialIndex;
import com.inn.commons.io.image.ImageUtils;
import com.inn.commons.lang.NumberUtils;
import com.inn.commons.lang.StringUtils;
import com.inn.commons.maps.Corner;
import com.inn.commons.maps.LatLng;
import com.inn.commons.maps.geometry.GeometryUtils;
import com.inn.commons.maps.rowkey.RowKeyUtils;
import com.inn.commons.maps.tiles.Tile;
import com.inn.commons.maps.tiles.TileBoundaryUtils;
import com.inn.commons.maps.tiles.TileImageUtils;
import com.inn.commons.maps.tiles.TileRelationship;
import com.inn.core.generic.exceptions.application.BusinessException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.dao.impl.hbase.AbstractHBaseDao;
import com.inn.foresight.core.generic.utils.ConfigEnum;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.maplayer.dao.IGenericKpiSummaryDao;
import com.inn.foresight.core.maplayer.dao.IGenericMapDao;
import com.inn.foresight.core.maplayer.model.GenKpiSummaryWrapper;
import com.inn.foresight.core.maplayer.model.GenericKpiRange;
import com.inn.foresight.core.maplayer.model.KpiWrapper;
import com.inn.foresight.core.maplayer.service.IGenericKpiSummaryService;
import com.inn.foresight.core.maplayer.service.IGenericMapService;
import com.inn.foresight.core.maplayer.wrapper.TileImageWrapper;
import com.inn.product.legends.dao.ILegendRangeDao;
import com.inn.product.legends.utils.LegendWrapper;
import com.inn.product.systemconfiguration.dao.SystemConfigurationDao;
import com.inn.product.systemconfiguration.model.SystemConfiguration;
import com.inn.product.systemconfiguration.service.SystemConfigurationService;
import com.inn.product.um.geography.dao.GeographyL4Dao;;

@Service("GenericKpiSummaryServiceImpl")
public class GenericKpiSummaryServiceImpl extends AbstractHBaseDao implements IGenericKpiSummaryService {

	@Autowired
	SystemConfigurationService sysConfService;
	@Autowired
	IGenericMapDao genericMapDao;
	@Autowired
	IGenericKpiSummaryDao kpiSummaryDao;
	@Autowired
	GeographyL4Dao geographyL4Dao;
	@Autowired
	IGenericMapService genericMapService;
	@Autowired
	ILegendRangeDao legendRangeDao;
	@Autowired
	SystemConfigurationDao systemconfigurationdao;

	public static final String DATE_FORMAT_DDMM = "ddMM";
	public static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
	public static final String DATE_FORMAT_YY = "yy";
	public static final int LEGEND_INTERVAL = 1;
	public static final long INITIAL_COUNT = 0L;
	public static final String PREDICTIVECOVERAGE_TABLENAME = "PredictiveCoverage";
	public static final String SMARTLAYER_TABLENAME = "SmartLayer";
	public static final String PREDICTIVECOVERAGE_IDENTIFIER = "COV";
	public static final String SMARTLAYER_IDENTIFIER = "SNC";
	public static final int BASE_ZOOM = 15;
	public static final int HIGHEST_ZOOM = 18;
	public static final int XINDEX = 0;
	public static final int YINDEX = 1;
	public static final int NO_COLOR = 0;
	public static final int X_CORD = 0;
	public static final int Y_CORD = 1;
	public static final int ZOOM_INDEX = 2;
	public static final String TILE_TO_ZONE_COUNT = "TILE_TO_ZONE_COUNT";

	public static final Map<String, Map<Double, Color>> dbfColorWithLegends = null;

	private static final Logger logger = LogManager.getLogger(GenericKpiSummaryServiceImpl.class);

	/** Redirect execution flow of viewport according to table name */
	public String getKpiSummaryData(GenKpiSummaryWrapper kpiWrapper) {
		try {
			if (kpiWrapper.getTableName().equalsIgnoreCase(PREDICTIVECOVERAGE_TABLENAME)
					|| kpiWrapper.getTableName().equalsIgnoreCase(SMARTLAYER_TABLENAME)) {
				return getPredictionKpiSummaryData(kpiWrapper);
			} else {
				return getDriveKpiSummaryData(kpiWrapper);
			}
		} catch (Exception e) {
			logger.error("Getting error in getKpiSummaryData method {}", ExceptionUtils.getStackTrace(e));
		}
		return Symbol.EMPTY_STRING;
	}

	/**
	 * This method Redirect execution flow of Polygon according to table name like
	 * PredictiveCoverage, SmartLayer
	 * 
	 * @param kpiwrapper
	 */
	public String getKpiSummaryDataByPolyList(GenKpiSummaryWrapper kpiWrapper) {
		try {
			if (kpiWrapper.getIdentifier().equalsIgnoreCase(PREDICTIVECOVERAGE_IDENTIFIER)
					|| kpiWrapper.getIdentifier().equalsIgnoreCase(SMARTLAYER_IDENTIFIER)) {
				return getPredictionKpiSummaryDataByPolyList(kpiWrapper);
			} else {
				return getDriveKpiSummaryDataByPolyList(kpiWrapper);
			}
		} catch (Exception e) {
			logger.error("Getting error in getKpiSummaryDataByPolyList method {}", ExceptionUtils.getStackTrace(e));
		}
		return Symbol.EMPTY_STRING;
	}

	/**
	 * this method gets images from hbase and compute stats for this images
	 * 
	 * @param kpi Summary Wrapper
	 * 
	 * @return kpi summary data
	 */

	@Override
	public String getPredictionKpiSummaryData(GenKpiSummaryWrapper kpiWrapper) {
		List<TileImageWrapper> tileImageList;
		try {
			tileImageList = getImagesFromHBase(kpiWrapper);
			if (tileImageList != null && !tileImageList.isEmpty()) {
				logger.info("Stats Process for kpi {} band {} images size {}", kpiWrapper.getKpi(),
						kpiWrapper.getBand(), tileImageList.size());
				return computeStatsFromImages(tileImageList, kpiWrapper);
			} else {
				logger.info("No Images Found");
			}
		} catch (BusinessException be) {
			logger.error("Getting Error in getPredictionKpiSummaryData with trace {}",
					ExceptionUtils.getStackTrace(be));
		}
		return Symbol.EMPTY_STRING;

	}

	/**
	 * This method create tile list and and using this tile list gets Images from
	 * Hbase
	 * 
	 * @param kpiWrapper
	 * @return return list of Buffered Images
	 * @throws BusinessException
	 */
	private List<TileImageWrapper> getImagesFromHBase(GenKpiSummaryWrapper kpiWrapper) {
		Corner corner = new Corner(kpiWrapper.getSwLat(), kpiWrapper.getSwLng(), kpiWrapper.getNeLat(),
				kpiWrapper.getNeLng());
		List<Tile> tilesList = TileBoundaryUtils.getTiles(corner, kpiWrapper.getZoom());
		String finalDate = getRowKeyDate(kpiWrapper.getFromDate());
		logger.info("tilesList : {},finalDate :{} ", tilesList, finalDate);
		List<TileImageWrapper> tileImageList = new ArrayList<>();
		if (kpiWrapper.getZoom() <= BASE_ZOOM) {
			return getImagesForLessThan15Zoom(kpiWrapper, tilesList);
		} else {
			getImagesForGreaterThan15Zoom(kpiWrapper, tilesList, finalDate, tileImageList);
		}
		return tileImageList;
	}

	/**
	 * This method return images for less than fifteen zoom level
	 * 
	 * @param kpiWrapper
	 * @param tilesList
	 * @return
	 * @throws BusinessException
	 */
	private List<TileImageWrapper> getImagesForLessThan15Zoom(GenKpiSummaryWrapper kpiWrapper, List<Tile> tilesList) {
		List<Get> getList = new ArrayList<>();
		for (Tile tile : tilesList) {
			createRowKeysAndGetList(kpiWrapper, getList, tile);
		}
		return getImagesFromHBase(getList, kpiWrapper);
	}

	/**
	 * this method return images for greater than fifteen zoom level
	 * 
	 * @param kpiWrapper
	 * @param tilesList
	 * @param finalDate
	 * @param tileImageList
	 * @throws BusinessException
	 */
	private void getImagesForGreaterThan15Zoom(GenKpiSummaryWrapper kpiWrapper, List<Tile> tilesList, String finalDate,
			List<TileImageWrapper> tileImageList) {
		for (Tile tile : tilesList) {
			TileImageWrapper tileImageWrapper = new TileImageWrapper();
			setDataInWrapper(kpiWrapper, tile, tileImageWrapper);
			String tileRowKey = createRowKey(kpiWrapper, finalDate, tileImageWrapper.getParentTileId());
			Get getObj = getGetObj(kpiWrapper, tileRowKey);
			getHbaseResultForGetObj(kpiWrapper, tileImageList, tileImageWrapper, getObj);
		}
	}

	/**
	 * This method create parent tile id by child tile id and child grid id by
	 * parent tile id then set both values in tile image wrapper
	 * 
	 * @param kpiWrapper
	 * @param tile
	 * @param tileImageWrapper
	 */
	private void setDataInWrapper(GenKpiSummaryWrapper kpiWrapper, Tile tile, TileImageWrapper tileImageWrapper) {
		int[] parentTileId = getParentTileIdByChildTileId(tile);
		tileImageWrapper.setTileId(new int[] { tile.getTx(), tile.getTy(), tile.getTz() });
		tileImageWrapper.setParentTileId(parentTileId);
		int[] childGridId = getChildGridId(tile, kpiWrapper.getZoom(), parentTileId);
		tileImageWrapper.setChildGridId(childGridId);
	}

	/**
	 * this method create row key for For Greater Than fifteen Zoom level
	 * 
	 * @param kpiWrapper
	 * @param finalDate
	 * @param tile
	 * @return row key
	 * @throws BusinessException
	 */
	private String createRowKey(GenKpiSummaryWrapper kpiWrapper, String finalDate, int[] parentTileId) {
		int zoneCount = Integer.parseInt(systemconfigurationdao.getValueByName(TILE_TO_ZONE_COUNT));
		Tile tile = new Tile(parentTileId[XINDEX], parentTileId[YINDEX], parentTileId[ZOOM_INDEX]);
		int projectNumber = GenericMapServiceImpl.getProjectNumber(kpiWrapper.getKpi(), kpiWrapper.getSiteStatus());
		return RowKeyUtils.getRowKeyForAtoll(tile, finalDate, projectNumber, kpiWrapper.getBand(),
				genericMapService.getZoneAndTileIdInMap(), null, zoneCount);
	}

	/**
	 * This method get images from HBase for single Get object
	 * 
	 * @param kpiWrapper
	 * @param tileImageList
	 * @param tileImage
	 * @param getObj
	 */
	private void getHbaseResultForGetObj(GenKpiSummaryWrapper kpiWrapper, List<TileImageWrapper> tileImageList,
			TileImageWrapper tileImage, Get getObj) {
		try {
			HBaseResult result = getResultByPool(getObj, kpiWrapper.getTableName(),
					Bytes.toBytes(kpiWrapper.getColumnFamily()));
			if (result != null) {
				byte[] imgBytes = result.getValue(Bytes.toBytes(kpiWrapper.getColumn()));
				if (imgBytes != null) {
					BufferedImage image = ImageUtils.toBufferedImage(imgBytes);
					tileImage.setImage(image);
					tileImageList.add(tileImage);
				}
			}
		} catch (IOException e) {
			logger.error("Error in getBufferedImagesFromHBase method {}", ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * this method create Get object
	 * 
	 * @param kpiWrapper
	 * @param tileRowKey
	 * @return
	 */
	private Get getGetObj(GenKpiSummaryWrapper kpiWrapper, String tileRowKey) {
		Get getObj = new Get(Bytes.toBytes(tileRowKey));
		getObj.addFamily(Bytes.toBytes(kpiWrapper.getColumnFamily()));
		getObj.addColumn(Bytes.toBytes(kpiWrapper.getColumnFamily()), Bytes.toBytes(kpiWrapper.getColumn()));
		return getObj;
	}

	/**
	 * This method get parent tile ,zoom and child tile id as argument and return
	 * child grid id
	 * 
	 * @param tile
	 * @param zoom
	 * @param parentTileId
	 * @return
	 */
	private int[] getChildGridId(Tile tile, Integer zoom, int[] parentTileId) {
		Tile parentTile = new Tile(parentTileId[XINDEX] + Symbol.UNDERSCORE_STRING + parentTileId[YINDEX]
				+ Symbol.UNDERSCORE_STRING + BASE_ZOOM);
		return TileRelationship.getGridIDGeneric(parentTile, tile);
	}

	/**
	 * this method get child tile id as argument and return parent tile id
	 * 
	 * @param tile
	 * @param zoom
	 * @return parent tile id
	 */
	private int[] getParentTileIdByChildTileId(Tile tile) {
		Tile parentTile = TileRelationship.getParentTileRecursively(tile, BASE_ZOOM);
		return new int[] { parentTile.getTx(), parentTile.getTy(), parentTile.getTz() };
	}

	/** This method create row key for tiles */
	private void createRowKeysAndGetList(GenKpiSummaryWrapper kpiWrapper, List<Get> getList, Tile tile) {
		int zoneCount = Integer.parseInt(systemconfigurationdao.getValueByName(TILE_TO_ZONE_COUNT));
		String finalDate = getRowKeyDate(kpiWrapper.getFromDate());
		if (finalDate != null) {
			int projectNumber = GenericMapServiceImpl.getProjectNumber(kpiWrapper.getKpi(), kpiWrapper.getSiteStatus());
			String tileRowKey = RowKeyUtils.getRowKeyForAtoll(tile, finalDate, projectNumber, kpiWrapper.getBand(),
					genericMapService.getZoneAndTileIdInMap(), null, zoneCount);
			logger.info("rowkey ====>>>{}", tileRowKey);
			createGetList(kpiWrapper, getList, tileRowKey);
		}
	}

	/** This method create Get object for all row key and add it to List */
	private void createGetList(GenKpiSummaryWrapper kpiWrapper, List<Get> getList, String tileRowKey) {
		Get getObj = new Get(Bytes.toBytes(tileRowKey));
		getObj.addFamily(Bytes.toBytes(kpiWrapper.getColumnFamily()));
		getObj.addColumn(Bytes.toBytes(kpiWrapper.getColumnFamily()), Bytes.toBytes(kpiWrapper.getColumn()));
		getList.add(getObj);
	}

	/** This method create formatted date like DDMMY */
	@Override
	public String getRowKeyDate(String date) {
		String rowKeyDate;
		String rowKeyYear;
		String finalDate = null;
		try {
			if (date != null) {
				rowKeyDate = new SimpleDateFormat(DATE_FORMAT_DDMM)
						.format(new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD).parse(date));
				rowKeyYear = new SimpleDateFormat(DATE_FORMAT_YY)
						.format(new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD).parse(date));
				finalDate = rowKeyDate + rowKeyYear.charAt(ForesightConstants.ONE);
			}
		} catch (Exception e) {
			logger.error("getting error during processing of date {} for getRowKeyDate {}", date, e.getMessage());
		}
		return finalDate;
	}

	/**
	 * This method gets images for less than fifteen zoom level
	 * 
	 * @param getList
	 * @param kpiWrapper
	 * @return
	 */
	private List<TileImageWrapper> getImagesFromHBase(List<Get> getList, GenKpiSummaryWrapper kpiWrapper) {
		return kpiSummaryDao.getHBaseDataFromGetList(getList, kpiWrapper.getTableName(), kpiWrapper.getColumn(),
				kpiWrapper.getColumnFamily());
	}

	/** This method compute Statistics for images and calculate Average */
	private String computeStatsFromImages(List<TileImageWrapper> tileImageList, GenKpiSummaryWrapper kpiWrapper) {
		Map<Integer, Double> colors = sysConfService.getDbfColorLegendMap(kpiWrapper.getKpi());
		kpiWrapper = updateCountAndValidValues(tileImageList, colors, kpiWrapper);
		kpiWrapper.calculateAvg();
		JSONObject json = new JSONObject();
		logger.info(
				"getAverageMap(kpiWrapper) :{}, getReportMap(kpiWrapper) :{}, kpiWrapper.getRecordCount() :{},kpiWrapper.calculateAvg()",
				getAverageMap(kpiWrapper), getReportMap(kpiWrapper), kpiWrapper.getRecordCount());
		json.put(ForesightConstants.AVERAGE_LOWER, getAverageMap(kpiWrapper));
		json.put(ForesightConstants.REPORT, getReportMap(kpiWrapper));
		updateRangewiseValues(kpiWrapper.getRanges(), kpiWrapper.getRecordCount());
		return addRanges(json, kpiWrapper).toString();
	}

	/**
	 * This method Add range number in json
	 * 
	 * @param json
	 * @param kpiWrapper
	 * @return
	 */
	private JSONObject addRanges(JSONObject json, GenKpiSummaryWrapper kpiWrapper) {
		logger.info("inside addranges method =====>> json : {}, band :{}, kpi : {}", json, kpiWrapper.getBand(),
				kpiWrapper.getKpi());
		for (GenericKpiRange range : kpiWrapper.getRanges()) {
			try {
				json.put(ForesightConstants.RANGE + range.getRangeNumber(), new JSONObject(range));
			} catch (JSONException e) {
				logger.error("Get Error while setting ranges to JSON with trace {}", ExceptionUtils.getStackTrace(e));
			}
		}
		return json;
	}

	/**
	 * This method create Average map
	 * 
	 * @param kpiWrapper
	 * @return average and ranges
	 */
	private Map<String, Object> getAverageMap(GenKpiSummaryWrapper kpiWrapper) {
		Map<String, Object> avgMap = new HashMap<>();

		String colorCode = getColorCodeFromValue(kpiWrapper.getAvg(), kpiWrapper.getRanges());
		logger.info("Avg Value {} Color {}", kpiWrapper.getAvg(), colorCode);
		avgMap.put(ForesightConstants.VALUE_LOWER, kpiWrapper.getAvg());
		avgMap.put(ForesightConstants.COLOR_LOWER, colorCode);
		return avgMap;
	}

	/**
	 * This method calculate 5 and 95 percentile
	 * 
	 * @param kpiWrapper
	 * @return
	 */
	private Map<String, Double> getReportMap(GenKpiSummaryWrapper kpiWrapper) {
		List<Double> validValues = kpiWrapper.getValidValueList();
		Map<String, Double> reportMap = new HashMap<>();
		if (!validValues.isEmpty() && validValues.size() > ForesightConstants.ZERO) {
			Double[] values = validValues.toArray(new Double[validValues.size()]);
			Arrays.sort(values);
			Double median = getMedian(values);
			Double percentile5 = getPercentile5(values);
			Double percentile95 = getPercentile95(values);
			logger.info("Median {}, Percentile5 {}, Percentile95 {}", median, percentile5, percentile95);
			reportMap.put(ForesightConstants.MEDIAN_LOWER, median);
			reportMap.put(ForesightConstants.PERCENTILE5_LOWER, percentile5);
			reportMap.put(ForesightConstants.PERCENTILE95_LOWER, percentile95);
		}
		return reportMap;

	}

	/** This method update range wise value */
	private LinkedList<GenericKpiRange> updateRangewiseValues(LinkedList<GenericKpiRange> ranges, long recordCount) {
		for (GenericKpiRange range : ranges) {
			range.setValue((range.getValue() / recordCount) * ForesightConstants.HUNDRED);
		}
		Comparator<GenericKpiRange> comparator = new Comparator<GenericKpiRange>() {

			public int compare(GenericKpiRange one, GenericKpiRange other) {
				return Integer.compare(one.getRangeNumber(), other.getRangeNumber());
			}
		};
		Collections.sort(ranges, comparator);
		logger.info("ranges values: {}", ranges);
		return ranges;
	}

	/** This method Returns Color code for valid values **/
	private String getColorCodeFromValue(Double avg, List<GenericKpiRange> ranges) {
		for (GenericKpiRange range : ranges) {
			if (avg >= range.getMin() && avg < range.getMax())
				return range.getColour();
		}
		return null;
	}

	/** This method Compute 95th Percentile **/
	private Double getPercentile95(Double[] values) {
		int percentile95 = (values.length * ForesightConstants.NINETY_FIVE) / ForesightConstants.HUNDRED;
		if (percentile95 == ForesightConstants.ZERO) {
			return values[percentile95];
		} else {
			return values[percentile95 - ForesightConstants.ONE];
		}
	}

	/** This method Compute 5th Percentile **/
	private Double getPercentile5(Double[] values) {
		int percentile5 = (values.length * ForesightConstants.NINETY_FIVE) / ForesightConstants.HUNDRED;
		logger.info("percentile5 value :{}", percentile5);
		if (percentile5 == ForesightConstants.ZERO) {
			return values[percentile5];
		} else {
			return values[percentile5 - ForesightConstants.ONE];
		}
	}

	/** This method Compute Median **/
	private Double getMedian(Double[] values) {
		return new Median().evaluate(ArrayUtils.toPrimitive(values));
	}

	private GenKpiSummaryWrapper updateCountAndValidValues(List<TileImageWrapper> tileImageList,
			Map<Integer, Double> colorMap, GenKpiSummaryWrapper kpiWrapper) {
		Double rangeMinVal = kpiWrapper.getRanges().get(NumberUtils.INTEGER_ZERO).getMin();
		Double rangeMaxVal = kpiWrapper.getRanges().get(kpiWrapper.getRanges().size() - NumberUtils.INTEGER_ONE)
				.getMax();
		logger.info("rangeMinVal :{},rangeMaxVal : {},band : {}", rangeMinVal, rangeMaxVal, kpiWrapper.getBand());
		processTileIdAndupdateColorCount(tileImageList, colorMap, kpiWrapper, rangeMinVal, rangeMaxVal);
		return kpiWrapper;
	}

	/**
	 * This method process Images And update Color Count
	 * 
	 * @param tileImageList
	 * @param colorMap
	 * @param kpiWrapper
	 * @param rangeMinVal
	 * @param rangeMaxVal
	 */
	private void processTileIdAndupdateColorCount(List<TileImageWrapper> tileImageList, Map<Integer, Double> colorMap,
			GenKpiSummaryWrapper kpiWrapper, Double rangeMinVal, Double rangeMaxVal) {
		for (TileImageWrapper tileImage : tileImageList) {
			int[] tileIdArray = tileImage.getTileId();
			int[][] pixel = getViewPortTilePixel(kpiWrapper.getSwLat(), kpiWrapper.getSwLng(), kpiWrapper.getNeLat(),
					kpiWrapper.getNeLng(), kpiWrapper.getZoom(), tileIdArray);
			if (kpiWrapper.getZoom() <= BASE_ZOOM) {
				updateColorCountForLessThan15Zoom(colorMap, kpiWrapper, rangeMinVal, rangeMaxVal, tileImage, pixel);
			} else {
				updateColorCountForGreaterThan15Zoom(colorMap, kpiWrapper, rangeMinVal, rangeMaxVal, tileImage, pixel);
			}
		}
	}

	/**
	 * this method update color count for greater than fifteen zoom level
	 * 
	 * @param colorMap
	 * @param kpiWrapper
	 * @param rangeMinVal
	 * @param rangeMaxVal
	 * @param tileImage
	 * @param pixel
	 */
	private void updateColorCountForGreaterThan15Zoom(Map<Integer, Double> colorMap, GenKpiSummaryWrapper kpiWrapper,
			Double rangeMinVal, Double rangeMaxVal, TileImageWrapper tileImage, int[][] pixel) {
		for (int xCord = pixel[ForesightConstants.ZERO][ForesightConstants.ZERO]; xCord < ForesightConstants.TILE_SIZE
				&& xCord <= pixel[ForesightConstants.ONE][ForesightConstants.ZERO]; xCord++) {
			for (int yCord = pixel[ForesightConstants.ZERO][ForesightConstants.ONE]; yCord < ForesightConstants.TILE_SIZE
					&& yCord <= pixel[ForesightConstants.ONE][ForesightConstants.ONE]; yCord++) {
				updateColorCountByPixel(colorMap, kpiWrapper, rangeMinVal, rangeMaxVal, tileImage, xCord, yCord);
			}
		}
	}

	/**
	 * this method update color count for less than fifteen zoom level
	 * 
	 * @param colorMap
	 * @param kpiWrapper
	 * @param rangeMinVal
	 * @param rangeMaxVal
	 * @param tileImage
	 * @param pixel
	 */
	private void updateColorCountForLessThan15Zoom(Map<Integer, Double> colorMap, GenKpiSummaryWrapper kpiWrapper,
			Double rangeMinVal, Double rangeMaxVal, TileImageWrapper tileImage, int[][] pixel) {
		for (int xCord = pixel[ForesightConstants.ZERO][ForesightConstants.ZERO]; xCord < ForesightConstants.TILE_SIZE
				&& xCord <= pixel[ForesightConstants.ONE][ForesightConstants.ZERO]; xCord++) {
			for (int yCord = pixel[ForesightConstants.ZERO][ForesightConstants.ONE]; yCord < ForesightConstants.TILE_SIZE
					&& yCord <= pixel[ForesightConstants.ONE][ForesightConstants.ONE]; yCord++) {
				updateColorCount(tileImage.getImage().getRGB(xCord, yCord), colorMap, kpiWrapper, rangeMinVal,
						rangeMaxVal);
			}
		}
	}

	/**
	 * this method update color count using pixel
	 * 
	 * @param colorMap
	 * @param kpiWrapper
	 * @param rangeMinVal
	 * @param rangeMaxVal
	 * @param tileImage
	 * @param xCord
	 * @param yCord
	 */
	private void updateColorCountByPixel(Map<Integer, Double> colorMap, GenKpiSummaryWrapper kpiWrapper,
			Double rangeMinVal, Double rangeMaxVal, TileImageWrapper tileImage, int xCord, int yCord) {
		int currentcolor = 0;
		try {
			int[] parentPixel = getParentPixelCorrespondingToChildPixel(tileImage.getChildGridId(),
					new int[] { xCord, yCord }, kpiWrapper.getZoom());
			currentcolor = tileImage.getImage().getRGB(parentPixel[X_CORD], parentPixel[Y_CORD]);
			updateColorCount(currentcolor, colorMap, kpiWrapper, rangeMinVal, rangeMaxVal);

		} catch (Exception e) {
			logger.error("Exception in Greater than 15 with trace {}", ExceptionUtils.getStackTrace(e));
		}
	}

	/**
	 * this method return Parent Pixel Corresponding To Child Pixel
	 * 
	 * @param gridID
	 * @param childPixel
	 * @param childZoom
	 * @return
	 */
	public int[] getParentPixelCorrespondingToChildPixel(int gridID[], int childPixel[], int childZoom) {
		int parent[] = new int[ForesightConstants.TWO];
		int divisor = (int) Math.pow(ForesightConstants.TWO, (double) childZoom - BASE_ZOOM);
		int skip = ForesightConstants.TILE_SIZE / divisor;

		parent[ForesightConstants.ZERO] = childPixel[ForesightConstants.ZERO] / divisor;
		parent[ForesightConstants.ZERO] = parent[ForesightConstants.ZERO] + gridID[ForesightConstants.ZERO] * skip;
		parent[ForesightConstants.ONE] = childPixel[ForesightConstants.ONE] / divisor;
		parent[ForesightConstants.ONE] = parent[ForesightConstants.ONE] + gridID[ForesightConstants.ONE] * skip;

		return parent;
	}

	/** getting View Port Tile Pixel */
	public static int[][] getViewPortTilePixel(double latMin, double lonMin, double latMax, double lonMax, int zoom,
			int[] googleTile) {
		LatLng latLng = new com.inn.commons.maps.LatLng(latMax, lonMin);
		Tile googleTileMin = new Tile(latLng, zoom);

		logger.info("latLng {} tile googleTileMin {} googleTile {} {}", latLng, googleTileMin,
				googleTile[ForesightConstants.ZERO], googleTile[ForesightConstants.ONE]);

		LatLng latLng1 = new com.inn.commons.maps.LatLng(latMin, lonMax);
		Tile googleTileMax = new Tile(latLng1, zoom);

		logger.info("latLng1 {} tile googleTileMax {}", latLng1, googleTileMax);

		int[][] pixels = new int[][] { { ForesightConstants.ZERO, ForesightConstants.ZERO },
				{ ForesightConstants.ZERO, ForesightConstants.ZERO } };

		Tile tile = new Tile(googleTile[ForesightConstants.ZERO] + ForesightConstants.UNDERSCORE
				+ googleTile[ForesightConstants.ONE] + ForesightConstants.UNDERSCORE + zoom);

		Corner bounds = tile.getCorner();
		logger.info("tile.getCorner() {}", tile.getCorner());

		int pixel[] = null;
		if (googleTile[ForesightConstants.ZERO] < googleTileMin.getTx()) {
			return null;
		} else {
			pixel = getViewPortTilePixel(bounds, latMax, lonMin);
			pixels[ForesightConstants.ZERO][ForesightConstants.ZERO] = pixel[ForesightConstants.ZERO];
		}
		if (googleTile[ForesightConstants.ZERO] <= googleTileMax.getTx()) {
			pixel = getViewPortTilePixel(bounds, latMax, lonMax);
			pixels[ForesightConstants.ONE][ForesightConstants.ZERO] = pixel[ForesightConstants.ZERO];
		} else {
			return null;
		}
		if (googleTile[ForesightConstants.ONE] < googleTileMin.getTy()) {
			return null;
		} else {
			pixel = getViewPortTilePixel(bounds, latMax, lonMin);
			pixels[ForesightConstants.ZERO][ForesightConstants.ONE] = pixel[ForesightConstants.ONE];
		}
		if (googleTile[ForesightConstants.ONE] <= googleTileMax.getTy()) {
			pixel = getViewPortTilePixel(bounds, latMin, lonMin);
			pixels[ForesightConstants.ONE][ForesightConstants.ONE] = pixel[ForesightConstants.ONE];
		} else {
			return null;
		}

		return pixels;
	}

	/**
	 * getting View Port Tile Pixel
	 * 
	 * @param bounds
	 * @param lat
	 * @param lon
	 * @return
	 */
	public static int[] getViewPortTilePixel(Corner bounds, double lat, double lon) {

		double latOrigin = bounds.getMinLatitude();
		double lonOrigin = bounds.getMinLongitude();
		double latExtent = bounds.getMaxLatitude() - bounds.getMinLatitude();
		double lonExtent = bounds.getMaxLongitude() - bounds.getMinLongitude();
		int pixel[] = new int[ForesightConstants.TWO];
		pixel[ForesightConstants.ZERO] = (int) ((ForesightConstants.TILE_SIZE * (lon - lonOrigin)) / lonExtent)
				- ForesightConstants.ONE;
		pixel[ForesightConstants.ONE] = (int) (ForesightConstants.TILE_SIZE
				- ((ForesightConstants.TILE_SIZE * (lat - latOrigin)) / latExtent));
		if (pixel[ForesightConstants.ZERO] < ForesightConstants.ZERO)
			pixel[ForesightConstants.ZERO] = ForesightConstants.ZERO;
		if (pixel[ForesightConstants.ZERO] > ForesightConstants.Two_Fifty_Five)
			pixel[ForesightConstants.ZERO] = ForesightConstants.Two_Fifty_Five;
		if (pixel[ForesightConstants.ONE] < ForesightConstants.ZERO)
			pixel[ForesightConstants.ONE] = ForesightConstants.ZERO;
		if (pixel[ForesightConstants.ONE] > ForesightConstants.Two_Fifty_Five)
			pixel[ForesightConstants.ONE] = ForesightConstants.Two_Fifty_Five;
		return pixel;
	}

	/**
	 * Update count, average value and valid value
	 * 
	 * @param currentColor
	 * @param colorMap
	 * @param kpiWrapper
	 * @param rangeMinVal
	 * @param rangeMaxVal
	 */
	private void updateColorCount(int currentColor, Map<Integer, Double> colorMap, GenKpiSummaryWrapper kpiWrapper,
			Double rangeMinVal, Double rangeMaxVal) {
		if (currentColor != NumberUtils.INTEGER_ZERO) {
			kpiWrapper.updateCount(NumberUtils.LONG_ONE);
			kpiWrapper.updateAvgValue(Double.valueOf(colorMap.get(currentColor)));
			kpiWrapper.updateValidValue(rangeMinVal, rangeMaxVal, colorMap.get(currentColor));
			for (GenericKpiRange range : kpiWrapper.getRanges()) {
				if (range.inRange(Double.valueOf(colorMap.get(currentColor)))) {
					range.updateCount(NumberUtils.LONG_ONE);
					break;
				}
			}
		}
	}

	/**
	 * This method return KPI summary data for for selected Geography
	 */
	@Override
	public String getKpiSummaryDataByGeographyList(GenKpiSummaryWrapper kpiSummaryWrapper) {
		try {
			Map<String, Integer> geographyIdMap = geographyL4Dao.getGeographyIdMap();
			List<String> kpiDataList = getKpiSummaryDataFromHbase(kpiSummaryWrapper, geographyIdMap);
			if (kpiDataList.size() > ForesightConstants.ZERO) {
				logger.info("logG=> Kpi Summary Data List {}", kpiDataList);
				int[] countArray = getCountArray(kpiDataList, kpiSummaryWrapper);
				updateKpiRangeCount(kpiSummaryWrapper, countArray);
				kpiSummaryWrapper.calculateAvg();
				JSONObject json = new JSONObject();
				json.put(ForesightConstants.AVERAGE_LOWER, getAverageMap(kpiSummaryWrapper));
				json.put(ForesightConstants.REPORT, kpiSummaryWrapper.getReportMap());
				logger.info("kpiSummaryWrapper.getReportMap() {}", kpiSummaryWrapper.getReportMap());
				updateRangewiseValues(kpiSummaryWrapper.getRanges(), kpiSummaryWrapper.getRecordCount());
				return addRanges(json, kpiSummaryWrapper).toString();
			}
		} catch (Exception e) {
			logger.error("Error inside getKpiSummaryDataByGeographyList method: {}", e);
		}
		return ForesightConstants.EMPTY;
	}

	/**
	 * This method get KPI Summary Data From HBase
	 * 
	 * @param kpiSummaryWrapper
	 * @param geographyIdMap
	 * @return
	 * @throws IOException
	 */
	private List<String> getKpiSummaryDataFromHbase(GenKpiSummaryWrapper kpiSummaryWrapper,
			Map<String, Integer> geographyIdMap) throws IOException {
		List<String> kpiDataList = new ArrayList<>();
		List<String> rowKeysList = getKpiSummaryRowKeys(kpiSummaryWrapper, geographyIdMap);
		logger.info("logG=> Rowkeys List {}", rowKeysList);
		if (!rowKeysList.isEmpty()) {
			List<HBaseResult> resultList = getDataforPrefixList(rowKeysList,
					kpiSummaryWrapper.getColumnFamily().getBytes(), kpiSummaryWrapper.getTableName());
			for (HBaseResult result : resultList) {
				kpiDataList.add(Bytes.toString(result.getValue(kpiSummaryWrapper.getColumn().getBytes())));
			}
		} else {
			logger.error("Hbase prefix is empty please check [{}] [{}]", kpiSummaryWrapper, geographyIdMap);
		}
		return kpiDataList;
	}

	/**
	 * Update KPI value and Range count
	 * 
	 * @param kpiWrapper
	 * @param countArray
	 */
	private void updateKpiRangeCount(GenKpiSummaryWrapper kpiWrapper, int[] countArray) {
		Double rangeMinVal = kpiWrapper.getRanges().get(NumberUtils.INTEGER_ZERO).getMin();
		Double rangeMaxVal = kpiWrapper.getRanges().get(kpiWrapper.getRanges().size() - NumberUtils.INTEGER_ONE)
				.getMax();
		if ((kpiWrapper.getIdentifier().equalsIgnoreCase(ForesightConstants.IDENTIFIER_COV)
				&& kpiWrapper.getKpi().equalsIgnoreCase(ForesightConstants.RSRP))
				|| (kpiWrapper.getKpi().equalsIgnoreCase(ForesightConstants.RSRP)
						&& kpiWrapper.getIdentifier().equalsIgnoreCase(ForesightConstants.IDENTIFIER_SNC))) {
			rangeMaxVal = ForesightConstants.RSRP_MAX_VALUE;
		}
		logger.info("Inside  updateKpiRange method min value :{} max value : {}", rangeMinVal, rangeMaxVal);
		int counter = ForesightConstants.ZERO;
		for (double rangeValue = rangeMinVal; rangeValue <= rangeMaxVal; rangeValue = rangeValue + LEGEND_INTERVAL) {
			kpiWrapper.updateAvgValue((double) (rangeValue * countArray[counter]));
			kpiWrapper.updateCount((long) countArray[counter]);
			for (GenericKpiRange range : kpiWrapper.getRanges()) {
				if (range.inRange((double) rangeValue)) {
					range.updateCount((long) countArray[counter]);
					break;
				}
			}
			counter++;
		}
		getKpiValueList(LEGEND_INTERVAL, countArray, kpiWrapper);
	}

	private void getKpiValueList(int interval, int[] countArray, GenKpiSummaryWrapper genKpiSummaryWrapper) {
		double rangeMinVal = genKpiSummaryWrapper.getRanges().get(NumberUtils.INTEGER_ZERO).getMin();
		long totalCount = genKpiSummaryWrapper.getRecordCount();
		long percentile5Index = getPercentileIndex(countArray, totalCount, ForesightConstants.FIVE);
		long percentile95Index = getPercentileIndex(countArray, totalCount, ForesightConstants.NINETY_FIVE);
		long medianIndex = getMedianIndex(countArray, totalCount);
		double percentile5 = rangeMinVal + percentile5Index;
		double percentile95 = rangeMinVal + percentile95Index;
		double median = rangeMinVal + medianIndex;
		Map<String, Double> reportMap = new HashMap<>();
		reportMap.put(ForesightConstants.MEDIAN_LOWER, median);
		reportMap.put(ForesightConstants.PERCENTILE5_LOWER, percentile5);
		reportMap.put(ForesightConstants.PERCENTILE95_LOWER, percentile95);
		genKpiSummaryWrapper.setReportMap(reportMap);
	}

	/** Returns Data Index required for Median */
	private long getMedianIndex(int[] countArray, long totalCount) {
		long medianIndex = (totalCount + ForesightConstants.ONE) / ForesightConstants.TWO;
		return getDataIndex(countArray, medianIndex);
	}

	/** Returns Data Index for respective percentile */
	private Long getPercentileIndex(int[] countArray, long totalCount, int percentile) {
		long percentileIndex = (totalCount * percentile) / ForesightConstants.HUNDRED;
		if (percentileIndex != ForesightConstants.ZERO) {
			percentileIndex = percentileIndex - ForesightConstants.ONE;
		}
		return getDataIndex(countArray, percentileIndex);
	}

	/** Returns the index of Data for given percentileIndex */
	private Long getDataIndex(int[] countArray, long thresholdIndex) {
		long index = ForesightConstants.ZERO;
		long dataIndex = ForesightConstants.ZERO;
		for (int valueCount : countArray) {
			if (index == ForesightConstants.ZERO) {
				index = valueCount;
			} else {
				index += valueCount;
			}
			if (index >= thresholdIndex) {
				return dataIndex;
			}
			dataIndex++;
		}
		return null;
	}

	/**
	 * this method return row key list
	 * 
	 * @param kpiSummaryWrapper
	 * @param geographyIdMap
	 * @return
	 */
	private List<String> getKpiSummaryRowKeys(GenKpiSummaryWrapper kpiSummaryWrapper,
			Map<String, Integer> geographyIdMap) {
		List<String> rowKeyList = new ArrayList<>();
		if (kpiSummaryWrapper.getToDate() == null) {
			String formattedDate = getRowKeyDate(kpiSummaryWrapper.getFromDate());
			getRowKeyListForGeographies(kpiSummaryWrapper, geographyIdMap, rowKeyList, formattedDate);
		} else {
			LocalDate fromDate = new LocalDate(kpiSummaryWrapper.getFromDate());
			LocalDate toDate = new LocalDate(kpiSummaryWrapper.getToDate());
			while (fromDate.isBefore(toDate) || fromDate.equals(toDate)) {
				String formattedDate = getRowKeyDate(fromDate.toString());
				getRowKeyListForGeographies(kpiSummaryWrapper, geographyIdMap, rowKeyList, formattedDate);
				fromDate = fromDate.plusDays(ForesightConstants.ONE);
			}
		}
		return rowKeyList;
	}

	/**
	 * this method create row key for geography
	 * 
	 * @param genKpiSummaryWrapper
	 * @param geographyIdMap
	 * @param rowKeyList
	 * @param formattedDate
	 */
	private void getRowKeyListForGeographies(GenKpiSummaryWrapper genKpiSummaryWrapper,
			Map<String, Integer> geographyIdMap, List<String> rowKeyList, String formattedDate) {
		for (String geographyName : genKpiSummaryWrapper.getGeographyList()) {
			if (geographyIdMap.get(geographyName) != null) {
				StringBuffer rowKey = createRowKeyForGeography(genKpiSummaryWrapper, geographyIdMap, formattedDate,
						geographyName);
				rowKeyList.add(rowKey.toString());
			}
		}
	}

	/**
	 * this method creating row key prefix for geography list
	 * 
	 * @param genKpiSummaryWrapper
	 * @param geographyIdMap
	 * @param formattedDate
	 * @param geographyName
	 * @return
	 */
	private StringBuffer createRowKeyForGeography(GenKpiSummaryWrapper genKpiSummaryWrapper,
			Map<String, Integer> geographyIdMap, String formattedDate, String geographyName) {
		StringBuffer rowKey = appendParameters(genKpiSummaryWrapper, geographyIdMap, formattedDate, geographyName);
		if (genKpiSummaryWrapper.getIdentifier().equalsIgnoreCase(PREDICTIVECOVERAGE_IDENTIFIER)
				|| genKpiSummaryWrapper.getIdentifier().equalsIgnoreCase(SMARTLAYER_IDENTIFIER)) {
			appendBand(genKpiSummaryWrapper, rowKey);
			// rowKey.append(genKpiSummaryWrapper.getBand());
			appendSiteStatus(genKpiSummaryWrapper, rowKey);
			appendKpi(genKpiSummaryWrapper, rowKey);
		}
		return rowKey;
	}

	/**
	 * this method append kpi prefix value into row key according to kpi
	 * 
	 * @param genKpiSummaryWrapper
	 * @param rowPreFix
	 */
	private void appendKpi(GenKpiSummaryWrapper genKpiSummaryWrapper, StringBuffer rowPreFix) {
		if (genKpiSummaryWrapper.getKpi() != null) {
			if (genKpiSummaryWrapper.getKpi().equalsIgnoreCase(String.valueOf(RSRP))) {
				rowPreFix.append(ForesightConstants.RSRP_PREFIX);
			} else if (genKpiSummaryWrapper.getKpi().equalsIgnoreCase(String.valueOf(SINR))) {
				rowPreFix.append(ForesightConstants.SINR_PREFIX);
			} else if (genKpiSummaryWrapper.getKpi().equalsIgnoreCase(String.valueOf(UL))) {
				rowPreFix.append(ForesightConstants.UL_PREFIX);
			} else if (genKpiSummaryWrapper.getKpi().equalsIgnoreCase(String.valueOf(DL))) {
				rowPreFix.append(ForesightConstants.DL_PREFIX);
			} else if (genKpiSummaryWrapper.getKpi().equalsIgnoreCase(String.valueOf("RSRQ"))) {
				rowPreFix.append(ForesightConstants.RSRQ_PREFIX);
			}
		}
	}

	/**
	 * this method append site status prefix value into row key according to site
	 * status
	 * 
	 * @param genKpiSummaryWrapper
	 * @param rowPreFix
	 */
	private void appendSiteStatus(GenKpiSummaryWrapper genKpiSummaryWrapper, StringBuffer rowPreFix) {
		if (genKpiSummaryWrapper.getSiteStatus() != null) {
			if (genKpiSummaryWrapper.getSiteStatus().equalsIgnoreCase(String.valueOf(ONAIR))) {
				rowPreFix.append(ForesightConstants.ON_AIR_PREFIX);
			} else if (genKpiSummaryWrapper.getSiteStatus().equalsIgnoreCase(String.valueOf(PLANNED))) {
				rowPreFix.append(ForesightConstants.PLANNED_PREFIX);
			}
		}
	}

	/**
	 * this method append band prefix value into row key according to band
	 * 
	 * @param genKpiSummaryWrapper
	 * @param rowPreFix
	 */
	private void appendBand(GenKpiSummaryWrapper genKpiSummaryWrapper, StringBuffer rowPreFix) {
		logger.debug("inside @appendBand method going to append band");
		if (genKpiSummaryWrapper.getBand() != null) {
			ObjectMapper mapper = new ObjectMapper();
			SystemConfiguration configurationByName = systemconfigurationdao
					.getConfigurationByName("BAND_CONFIGURATION_KPI_SUMMARY");
			if (configurationByName != null) {
				try {
					Map<String,String>bandMap = mapper.readValue(configurationByName.getValue(), new TypeReference<Map<String, String>>() {});
					rowPreFix.append(bandMap.get(genKpiSummaryWrapper.getBand()));
				} catch (IOException e) {
					logger.error("Exception occured while parsing band configurtion map :{}",
							ExceptionUtils.getStackTrace(e));
					throw new BusinessException(ExceptionUtils.getMessage(e));
				}
			} else {
				logger.warn("Config is not exist in SystemConfiguration");
				throw new BusinessException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
			}
		}else {
			logger.warn("Band name is null !!");
		}
	}

	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
//	 String tilesZoneMapAsString = systemConfiguration.getValue();
//     ObjectMapper mapper = new ObjectMapper();
//     tilesZoneMap =
//         mapper.readValue(tilesZoneMapAsString, new TypeReference<Map<String, Integer>>() {});
	 ObjectMapper mapper = new ObjectMapper();
	 
	 Map<String,String>bandmap= mapper.readValue("{\"COM\": \"D\",\"2300\": \"A\",\"1800\": \"B\",\"850\":\"C\",\"A\":\"2300\",\"B\":\"1800\",\"C\":\"850\",\"D\":\"COM\"}", new TypeReference<Map<String, String>>() {});
	 System.out.println(bandmap.get("COM")); 
	 
	
}
	/**
	 * this method append all parameter into row key
	 * 
	 * @param genKpiSummaryWrapper
	 * @param geographyIdMap
	 * @param formattedDate
	 * @param geographyName
	 * @return
	 */
	private StringBuffer appendParameters(GenKpiSummaryWrapper genKpiSummaryWrapper,
			Map<String, Integer> geographyIdMap, String formattedDate, String geographyName) {
		StringBuffer rowPreFix = new StringBuffer();
		if (geographyIdMap.size() > ForesightConstants.ZERO && geographyName != null
				&& genKpiSummaryWrapper.getIdentifier() != null && genKpiSummaryWrapper.getGeographyType() != null) {
			if (formattedDate != null) {
				rowPreFix.append(formattedDate);
			}
			rowPreFix.append(getRowKeyPartFromGeography(geographyIdMap, geographyName));
			appendGeographyType(genKpiSummaryWrapper, rowPreFix);
			rowPreFix.append(genKpiSummaryWrapper.getIdentifier());
			return rowPreFix;
		}
		return rowPreFix;
	}

	/**
	 * @param genKpiSummaryWrapper
	 * @param rowPreFix
	 */
	private void appendGeographyType(GenKpiSummaryWrapper genKpiSummaryWrapper, StringBuffer rowPreFix) {
		if (genKpiSummaryWrapper.getGeographyType().equalsIgnoreCase(ForesightConstants.GeographyL1)) {
			rowPreFix.append(ForesightConstants.GeographyType_L1);
		} else if (genKpiSummaryWrapper.getGeographyType().equalsIgnoreCase(ForesightConstants.GeographyL2)) {
			rowPreFix.append(ForesightConstants.GeographyType_L2);
		} else if (genKpiSummaryWrapper.getGeographyType().equalsIgnoreCase(ForesightConstants.GeographyL3)) {
			rowPreFix.append(ForesightConstants.GeographyType_L3);
		} else if (genKpiSummaryWrapper.getGeographyType().equalsIgnoreCase(ForesightConstants.GeographyL4)) {
			rowPreFix.append(ForesightConstants.GeographyType_L4);
		}
	}

	private String getRowKeyPartFromGeography(Map<String, Integer> geographyIdMap, String geographyName) {
		return StringUtils.reverse(StringUtils.leftPad(geographyIdMap.get(geographyName).toString(),
				ForesightConstants.FIVE, ForesightConstants.ZERO_STRING));
	}

	/**
	 * this method create count array
	 * 
	 * @param kpiDataStringlist
	 * @param genKpiSummaryWrapper
	 * @return
	 */
	private int[] getCountArray(List<String> kpiDataStringlist, GenKpiSummaryWrapper kpiSummaryWrapper) {
		LinkedList<GenericKpiRange> ranges = kpiSummaryWrapper.getRanges();
		Double rangeMinVal = ranges.get(NumberUtils.INTEGER_ZERO).getMin();
		Double rangeMaxVal = ranges.get(ranges.size() - NumberUtils.INTEGER_ONE).getMax();
		int arraySize = (int) (rangeMaxVal - rangeMinVal) + NumberUtils.INTEGER_ONE;
		int[] countArray = null;
		for (String kpiDataString : kpiDataStringlist) {
			String[] countStringArray = kpiDataString.split(ForesightConstants.COMMA);
			int[] splittedArray = (int[]) ConvertUtils.convert(countStringArray, Integer.TYPE);
			int[] convertedArray = Arrays.copyOf(splittedArray, arraySize);
			if (countArray == null) {
				countArray = convertedArray;
			} else {
				for (int indexValue = ForesightConstants.ZERO; indexValue < countArray.length; indexValue++) {
					countArray[indexValue] = countArray[indexValue] + convertedArray[indexValue];
				}
			}
		}
		logger.info("Count Array ==>> : {}", Arrays.toString(countArray));
		return countArray;
	}

	/**
	 * this method create Get list and get data for selected geography
	 * 
	 * @param rowKeyList
	 * @param columfamily
	 * @param tableName
	 * @return
	 * @throws IOException
	 */
	public List<HBaseResult> getDataforPrefixList(List<String> rowKeyList, byte[] columfamily, String tableName)
			throws IOException {
		logger.info("Going to fetch data from table- {} , columnfamily {} ", tableName, Bytes.toString(columfamily));
		List<Get> getList = new ArrayList<>();
		for (String rowkey : rowKeyList) {
			Get get = new Get(Bytes.toBytes(rowkey));
			get.addFamily(columfamily);
			getList.add(get);
		}
		return getResultByPool(getList, tableName, columfamily);
	}

	/**
	 * this method return kpi summary data of polygon list with table prediction
	 * 
	 * @throws BusinessException
	 */
	@Override
	public String getPredictionKpiSummaryDataByPolyList(GenKpiSummaryWrapper kpiWrapper) {
		String response = Symbol.EMPTY_STRING;
		if (kpiWrapper.getZoom() > BASE_ZOOM && kpiWrapper.getZoom() <= HIGHEST_ZOOM) {
			kpiWrapper.setZoom(BASE_ZOOM);
		}
		List<TileImageWrapper> tileImagWrapper = getBufferedImagesForPolyList(kpiWrapper);
		Map<String, BufferedImage> layerImageMap = createLayerMap(tileImagWrapper, kpiWrapper);
		Map<String, BufferedImage> polyImageMap = new HashMap<>();
		updatePolyMap(kpiWrapper, polyImageMap);
		if (layerImageMap.size() > NumberUtils.INTEGER_ZERO && polyImageMap.size() > NumberUtils.INTEGER_ZERO) {
			return computeStatsForpolygonList(layerImageMap, polyImageMap, kpiWrapper);
		}
		return response;
	}

	/**
	 * @param kpiWrapper
	 * @param polyImageMap
	 */
	private void updatePolyMap(GenKpiSummaryWrapper kpiWrapper, Map<String, BufferedImage> polyImageMap) {
		for (List<List<Double>> poly : kpiWrapper.getPolyList()) {
			polyImageMap.putAll(TileImageUtils.getTileImage(poly, kpiWrapper.getZoom()));
		}
	}

	/**
	 * This method compute Statistics for polygon
	 * 
	 * @param layerImageMap
	 * @param polyImageMap
	 * @param kpiWrapper
	 * @return
	 * @throws JSONException
	 */
	private String computeStatsForpolygonList(Map<String, BufferedImage> layerImageMap,
			Map<String, BufferedImage> polyImageMap, GenKpiSummaryWrapper kpiWrapper) {
		Map<Integer, Double> colorsMap = sysConfService.getDbfColorLegendMap(kpiWrapper.getKpi());
		updateValidValues(layerImageMap, polyImageMap, kpiWrapper, colorsMap);
		kpiWrapper.calculateAvg();
		JSONObject json = new JSONObject();
		json.put(ForesightConstants.AVERAGE_LOWER, getAverageMap(kpiWrapper));
		json.put(ForesightConstants.REPORT, getReportMap(kpiWrapper));
		updateRangewiseValues(kpiWrapper.getRanges(), kpiWrapper.getRecordCount());
		return addRanges(json, kpiWrapper).toString();
	}

	/**
	 * this method update valid value and color count
	 * 
	 * @param layerImageMap
	 * @param polyImageMap
	 * @param kpiWrapper
	 * @param colorsMap
	 * @param rangeMinVal
	 * @param rangeMaxVal
	 */
	private void updateValidValues(Map<String, BufferedImage> layerImageMap, Map<String, BufferedImage> polyImageMap,
			GenKpiSummaryWrapper kpiWrapper, Map<Integer, Double> colorsMap) {
		Double rangeMinVal = kpiWrapper.getRanges().get(NumberUtils.INTEGER_ZERO).getMin();
		Double rangeMaxVal = kpiWrapper.getRanges().get(kpiWrapper.getRanges().size() - NumberUtils.INTEGER_ONE)
				.getMax();
		logger.info("inside updateValidValues rangeMinVal : {},rangeMaxVal {}", rangeMinVal, rangeMaxVal);

		for (Map.Entry<String, BufferedImage> entry : layerImageMap.entrySet()) {
			BufferedImage polyImage = polyImageMap.get(entry.getKey());
			for (int xCord = ForesightConstants.ZERO; xCord < ForesightConstants.TILE_SIZE; xCord++) {
				for (int yCord = ForesightConstants.ZERO; yCord < ForesightConstants.TILE_SIZE; yCord++) {

					int rgb = polyImage.getRGB(xCord, yCord);
					if (rgb != NO_COLOR) {
						updateColorCount(entry.getValue().getRGB(xCord, yCord), colorsMap, kpiWrapper, rangeMinVal,
								rangeMaxVal);
					}
				}
			}
		}

	}

	/**
	 * this method create layer map using tile id and buffered images and return
	 * 
	 * @param tileImagWrapper
	 * @param genKpiSummaryWrapper
	 * @return
	 */
	private Map<String, BufferedImage> createLayerMap(List<TileImageWrapper> tileImagWrapper,
			GenKpiSummaryWrapper genKpiSummaryWrapper) {
		Map<String, BufferedImage> layerImageMap = new HashMap<>();

		for (TileImageWrapper tileImageWrapper : tileImagWrapper) {
			layerImageMap.put(tileImageWrapper.getTileId()[ForesightConstants.ZERO] + Symbol.UNDERSCORE_STRING
					+ tileImageWrapper.getTileId()[ForesightConstants.ONE] + Symbol.UNDERSCORE_STRING
					+ genKpiSummaryWrapper.getZoom(), tileImageWrapper.getImage());
		}
		logger.info("inside getPredictionKpiSummaryDataByPolyList method layerImageMap size : {} values: {}",
				layerImageMap.size(), layerImageMap);
		return layerImageMap;
	}

	/**
	 * this method get images from hbase for polygon list and return
	 * 
	 * @param kpiWrapper
	 * @return
	 * @throws BusinessException
	 */
	private List<TileImageWrapper> getBufferedImagesForPolyList(GenKpiSummaryWrapper kpiWrapper) {
		List<Tile> tileList = TileBoundaryUtils.getExactTilesForMultiBoundary(kpiWrapper.getPolyList(),
				kpiWrapper.getZoom());
		logger.info("tile list size : {} values: {}", tileList.size(), tileList);
		List<Get> getList = new ArrayList<>();
		for (Tile tile : tileList) {
			try {
				createRowKeysAndGetList(kpiWrapper, getList, tile);
			} catch (BusinessException e) {
				logger.error(" error inside getBufferedImagesForPolyList : {}", ExceptionUtils.getStackTrace(e));
			}
		}
		return getImagesFromHBase(getList, kpiWrapper);
	}

	/**
	 * this method return kpi summary data of polygon list for without table
	 * prediction
	 * 
	 * @param kpiWrapper
	 * @return
	 */
	public String getDriveKpiSummaryDataByPolyList(GenKpiSummaryWrapper kpiWrapper) {
		List<String> prefixList = getPrefixListForTilesWithInPolygon(kpiWrapper);
		List<HBaseResult> hbaseResult = getDataFromHbaseForPolyList(kpiWrapper.getColumnList(),
				kpiWrapper.getTableName(), prefixList, kpiWrapper.getColumn(), kpiWrapper.getColumnFamily(),
				kpiWrapper.getPolyList());
		if (hbaseResult.size() > ForesightConstants.ZERO) {
			int[] countArray = getCountArrayForHBaseResult(hbaseResult, kpiWrapper);
			logger.info("count array value :{}", countArray);
			if (countArray.length > ForesightConstants.ZERO) {
				updateKpiRangeCount(kpiWrapper, countArray);
				kpiWrapper.calculateAvg();
				JSONObject json = new JSONObject();
				try {
					json.put(ForesightConstants.AVERAGE_LOWER, getAverageMap(kpiWrapper));
					json.put(ForesightConstants.REPORT, kpiWrapper.getReportMap());
					updateRangewiseValues(kpiWrapper.getRanges(), kpiWrapper.getRecordCount());
				} catch (Exception e) {
					logger.error("Get Error with trace {}", ExceptionUtils.getStackTrace(e));
				}
				return addRanges(json, kpiWrapper).toString();
			}

		}
		return Symbol.EMPTY_STRING;
	}

	/**
	 * this method creating prefix list for tile which belong with in polygon
	 * 
	 * @param kpiWrapper
	 * @return
	 */
	private List<String> getPrefixListForTilesWithInPolygon(GenKpiSummaryWrapper kpiWrapper) {
		List<Tile> tileList = TileBoundaryUtils.getExactTilesForMultiBoundary(kpiWrapper.getPolyList(),
				kpiWrapper.getZoom());
		List<String> tileIdPrefixList = new ArrayList<>();
		getRowPrefixes(kpiWrapper, tileList, tileIdPrefixList);
		logger.info("tile id list size : {}", tileIdPrefixList.size());
		return tileIdPrefixList;

	}

	/**
	 * this method get data from hbase for polygon list
	 * 
	 * @param list
	 */
	private List<HBaseResult> getDataFromHbaseForPolyList(List<String> colList, String tableName,
			List<String> tileIdPrefixList, String column, String columnFamily, List<List<List<Double>>> polyList) {
		List<String> columns = new ArrayList<>();
		addColumnsToList(column, columns, columnFamily, colList);
		logger.info("columns list : {}", columns);
		List<HBaseResult> resultList = kpiSummaryDao.getHbaseResultForPrefixList(tableName, tileIdPrefixList, columns,
				columnFamily);
		List<HBaseResult> validResults = new ArrayList<>();
		logger.info("hbase result list getDataFromHbaseForpolylist resultList size:{}", resultList.size());
		for (HBaseResult result : resultList) {
			double latitude = result.getStringAsDouble(Bytes.toBytes(colList.get(ForesightConstants.LATITUDE_INDEX)));
			double longitude = result.getStringAsDouble(Bytes.toBytes(colList.get(ForesightConstants.LONGITUDE_INDEX)));
			for (List<List<Double>> polygon : polyList) {
				if (GeometryUtils.contains(polygon, new LatLng(latitude, longitude))) {
					validResults.add(result);
				}
			}
		}
		return validResults;
	}

	private void addColumnsToList(String column, List<String> columns, String columnFamily, List<String> colList) {
		columns.add(columnFamily + ForesightConstants.COLON + colList.get(ForesightConstants.LATITUDE_INDEX));
		columns.add(columnFamily + ForesightConstants.COLON + colList.get(ForesightConstants.LONGITUDE_INDEX));
		columns.add(columnFamily + ForesightConstants.COLON + column);
	}

	/**
	 * This method return kpi summary data for view port
	 */
	@Override
	public String getDriveKpiSummaryData(GenKpiSummaryWrapper kpiWrapper) {
		List<String> tileIdPrefixList = getPrefixListByViewPort(kpiWrapper);
		List<HBaseResult> hbaseResult = getDataFromHbase(kpiWrapper.getTableName(), tileIdPrefixList,
				kpiWrapper.getColumn(), kpiWrapper.getColumnFamily());
		if (hbaseResult.size() > NumberUtils.INTEGER_ZERO) {
			int[] countArray = getCountArrayForHBaseResult(hbaseResult, kpiWrapper);
			logger.info("Array Value Count:{}", countArray);
			if (countArray.length > ForesightConstants.ZERO) {
				updateKpiRangeCount(kpiWrapper, countArray);
				kpiWrapper.calculateAvg();
				JSONObject json = new JSONObject();
				try {
					json.put(ForesightConstants.AVERAGE_LOWER, getAverageMap(kpiWrapper));
					json.put(ForesightConstants.REPORT, kpiWrapper.getReportMap());
					updateRangewiseValues(kpiWrapper.getRanges(), kpiWrapper.getRecordCount());
				} catch (Exception e) {
					logger.error("Error with trace {}", ExceptionUtils.getStackTrace(e));
				}
				return addRanges(json, kpiWrapper).toString();
			}
		}
		return ForesightConstants.EMPTY;
	}

	/**
	 * this method create count array using drive data
	 * 
	 * @param driveDataList
	 * @param kpiWrapper
	 * @return
	 */
	private int[] getCountArrayForHBaseResult(List<HBaseResult> driveDataList, GenKpiSummaryWrapper kpiWrapper) {
		LinkedHashMap<Double, Long> countMap = new LinkedHashMap<>();
		loadKpiwiseMap(kpiWrapper, countMap);
		logger.info("driveDataList value inside getHBaseResultStringList method size:{}", driveDataList.size());
		for (HBaseResult driveData : driveDataList) {
			byte[] byteVal = driveData.getValue(Bytes.toBytes(kpiWrapper.getColumn()));
			if (byteVal != null) {
				Double val = NumberUtils.toDouble(Bytes.toString(byteVal));
				val = NumberUtils.toDouble(Math.round(val));
				if (countMap.containsKey(val)) {
					countMap.put(val, countMap.get(val) + ForesightConstants.ONE);
				}
			}
		}
		return getCountArrayOfCountMap(countMap);
	}

	/** this method create count array for count map which received as argument */
	private int[] getCountArrayOfCountMap(LinkedHashMap<Double, Long> countMap) {
		int[] countArray = new int[countMap.size()];
		int count = ForesightConstants.ZERO;
		for (Map.Entry<Double, Long> entry : countMap.entrySet()) {
			countArray[count] = entry.getValue().intValue();
			count++;
		}
		return countArray;
	}

	/** this method load map kpi wise */
	private void loadKpiwiseMap(GenKpiSummaryWrapper kpiWrapper, Map<Double, Long> countMap) {
		Double rangeMinVal = kpiWrapper.getRanges().get(NumberUtils.INTEGER_ZERO).getMin();
		Double rangeMaxVal = kpiWrapper.getRanges().get(kpiWrapper.getRanges().size() - NumberUtils.INTEGER_ONE)
				.getMax();

		for (double rangeValue = rangeMinVal; rangeValue <= rangeMaxVal; rangeValue = rangeValue + LEGEND_INTERVAL) {
			countMap.put(rangeValue, INITIAL_COUNT);

		}
	}

	/** this method get data from hbase for view port */
	private List<HBaseResult> getDataFromHbase(String tableName, List<String> tileIdPrefixList, String column,
			String columnFamily) {
		List<String> columns = new ArrayList<>();
		columns.add(columnFamily + ForesightConstants.COLON + column);
		return kpiSummaryDao.getHbaseResultForPrefixList(tableName, tileIdPrefixList, columns, columnFamily);
	}

	/**
	 * this method creates prefix list for view port
	 * 
	 * @param kpiWrapper
	 * @return
	 */
	private List<String> getPrefixListByViewPort(GenKpiSummaryWrapper kpiWrapper) {
		Corner corner = new Corner(kpiWrapper.getSwLat(), kpiWrapper.getSwLng(), kpiWrapper.getNeLat(),
				kpiWrapper.getNeLng());
		List<Tile> tilesList = TileBoundaryUtils.getTiles(corner, kpiWrapper.getZoom());
		List<String> prefixList = new ArrayList<>();
		getRowPrefixes(kpiWrapper, tilesList, prefixList);
		logger.info("prefixList value : {}", prefixList);
		return prefixList;
	}

	private void getRowPrefixes(GenKpiSummaryWrapper kpiWrapper, List<Tile> tilesList, List<String> prefixList) {
		if (kpiWrapper.getToDate() == null && kpiWrapper.getFromDate() == null) {
			getRowPrefixes(tilesList, prefixList, kpiWrapper.getToDate(), kpiWrapper.getIdentifier());
		} else {
			LocalDate fromDate = new LocalDate(kpiWrapper.getFromDate());
			LocalDate toDate = new LocalDate(kpiWrapper.getToDate());
			while (fromDate.isBefore(toDate) || fromDate.equals(toDate)) {
				String formattedDate = getRowKeyDate(fromDate.toString());
				getRowPrefixes(tilesList, prefixList, formattedDate, kpiWrapper.getIdentifier());
				fromDate = fromDate.plusDays(ForesightConstants.ONE);
			}
		}
	}

	private void getRowPrefixes(List<Tile> tilesList, List<String> prefixList, String formattedDate,
			String identifier) {
		for (Tile tileId : tilesList) {
			String prefix;
			if (formattedDate == null) {
				prefix = StringUtils.leftPad(SpatialIndex.getSpatialIndex(tileId.getPaddedTile().getIdWithZoom()),
						ForesightConstants.TEN, ForesightConstants.ZERO_STRING) + identifier;
			} else {
				prefix = StringUtils.leftPad(SpatialIndex.getSpatialIndex(tileId.getPaddedTile().getIdWithZoom()),
						ForesightConstants.TEN, ForesightConstants.ZERO_STRING) + formattedDate + identifier;
			}
			prefixList.add(prefix);
		}
	}

	@Override
	public String getKpiSummaryData(List<String> columnList, List<List<List<Double>>> polyList,
			KpiWrapper kpiSummaryWrapper, Double northEastLat, Double northEastLong, Double southWestLat,
			Double southWestLong, Integer zoomLevel, String tableName, String columnName, String geographyType,
			String columnFamily, List<String> geographyList, String identifier) {

		GenKpiSummaryWrapper genKpiSummaryWrapper = new GenKpiSummaryWrapper();
		try {
			genKpiSummaryWrapper.setRanges(getGenericKpiRangeList(kpiSummaryWrapper));
			genKpiSummaryWrapper.setPolyList(polyList);
			genKpiSummaryWrapper.setKpi(kpiSummaryWrapper.getKpi());
			genKpiSummaryWrapper.setSiteStatus(kpiSummaryWrapper.getSiteStatus());
			genKpiSummaryWrapper.setBand(kpiSummaryWrapper.getBand());
			genKpiSummaryWrapper.setToDate(kpiSummaryWrapper.getToDate());
			genKpiSummaryWrapper.setFromDate(kpiSummaryWrapper.getFromDate());
			genKpiSummaryWrapper.setTableName(tableName);
			genKpiSummaryWrapper.setColumn(columnName);
			genKpiSummaryWrapper.setLegendId(kpiSummaryWrapper.getLegendId());
			genKpiSummaryWrapper.setZoom(zoomLevel);
			genKpiSummaryWrapper.setNeLat(northEastLat);
			genKpiSummaryWrapper.setNeLng(northEastLong);
			genKpiSummaryWrapper.setSwLat(southWestLat);
			genKpiSummaryWrapper.setSwLng(southWestLong);
			genKpiSummaryWrapper.setGeographyType(geographyType);
			genKpiSummaryWrapper.setColumnFamily(columnFamily);
			genKpiSummaryWrapper.setGeographyList(geographyList);
			genKpiSummaryWrapper.setIdentifier(identifier);
			genKpiSummaryWrapper.setColumnList(columnList);
			String dataJson = new Gson().toJson(genKpiSummaryWrapper);
			StringEntity entity = new StringEntity(dataJson, ContentType.APPLICATION_JSON);
			logger.info("inside getKpiSummaryData method = {}", genKpiSummaryWrapper);
			return new HttpPostRequest(getDropwizardUrl(null), entity).getString();
		} catch (Exception e) {
			logger.error("Error while getting data from rest service {} ",
					com.inn.foresight.core.generic.utils.Utils.getStackTrace(e));
		}
		return "";

	}

	/**
	 * Gets the dropwizard url.
	 *
	 * @param query the query
	 * @return the dropwizard url
	 */
	public static String getDropwizardUrl(String query) {
		String url = ConfigUtils.getString(ConfigEnum.MICRO_SERVICE_BASE_URL.getValue())
				+ ConfigUtils.getString(ConfigEnum.DROPWIZARD_GENERICMAP_KPI_SUMMARY_URL.getValue());
		logger.info("Generic map dropwizard URL {} , [{}]", url, query);
		if (query != null && query.length() != 0) {
			url += "?" + query;
		}
		logger.info("Final dropwizard URL {}", url);
		return url;
	}

	/**
	 * this method return Generic KPI Range List
	 * 
	 * @param kpiSummaryWrapper
	 * @return
	 * @throws RestException
	 * @throws JSONException
	 */
	private LinkedList<GenericKpiRange> getGenericKpiRangeList(KpiWrapper kpiSummaryWrapper) {
		Integer legendId = kpiSummaryWrapper.getLegendId();
		LinkedList<GenericKpiRange> genericKpiRangeList = new LinkedList<>();
		List<LegendWrapper> legendWrapperList = legendRangeDao.findAllLegendRangesByLegendId(legendId, null);
		for (LegendWrapper legendWrapper : legendWrapperList) {
			String configuration = legendWrapper.getConfiguration();
			if (configuration != null) {
				JSONObject jsonObject = new JSONObject(configuration);
				// if
				// (jsonObject.get(kpiSummaryWrapper.getLegendConfigType()).toString().equals(kpiSummaryWrapper.getLegendConfigValue())
				// &&
				// jsonObject.get("type").toString().equals(kpiSummaryWrapper.getLegendConfigType()))
				// {
				JSONArray type = (JSONArray) jsonObject.get("type");
				List<String> typeList = new ArrayList<>();
				for (int i = 0; i < type.length(); i++) {
					typeList.add(type.getString(i));
				}
				logger.info("typeList : {}", typeList);
				logger.info("ConfigType : {}", kpiSummaryWrapper.getLegendConfigType());
				if (typeList.size() == 1) {
					for (String legendType : typeList) {
						if (jsonObject.get(legendType).toString().equals(kpiSummaryWrapper.getLegendConfigValue())
								&& legendType.equals(kpiSummaryWrapper.getLegendConfigType())) {
							addDataToRangeList(genericKpiRangeList, legendWrapper);
						}
					}
				}
				// }
			} else {
				addDataToRangeList(genericKpiRangeList, legendWrapper);
			}
		}
		sortList(genericKpiRangeList);
		return genericKpiRangeList;
	}

	/**
	 * @param genericKpiRangeList
	 * @param legendWrapper
	 */
	private void addDataToRangeList(List<GenericKpiRange> genericKpiRangeList, LegendWrapper legendWrapper) {
		GenericKpiRange genericKpiRange = new GenericKpiRange();
		genericKpiRange.setMin(legendWrapper.getMinValue());
		genericKpiRange.setMax(legendWrapper.getMaxValue());
		genericKpiRange.setColour(legendWrapper.getColorCode());
		genericKpiRangeList.add(genericKpiRange);
	}

	/**
	 * this method sort the list
	 * 
	 * @param genericKpiRangeList
	 */
	private void sortList(List<GenericKpiRange> genericKpiRangeList) {
		Comparator<GenericKpiRange> kpiRangeComparator = (GenericKpiRange minFirstValue,
				GenericKpiRange minSecondValue) -> Double.compare(minFirstValue.getMin().doubleValue(),
						minSecondValue.getMin().doubleValue());
		logger.info("inside sortlist method");
		Collections.sort(genericKpiRangeList, kpiRangeComparator);
		setRangeNumber(genericKpiRangeList);
	}

	/* Set Range Number To Ranges */
	private void setRangeNumber(List<GenericKpiRange> genericKpiRangeList) {
		int i = 0;
		for (GenericKpiRange range : genericKpiRangeList) {
			range.setRangeNumber(i);
			i++;
		}
	}

}
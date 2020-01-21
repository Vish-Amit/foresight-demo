package com.inn.foresight.core.maplayer.service.impl;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.math.DoubleUtils;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.MultiRowRangeFilter;
import org.apache.hadoop.hbase.filter.MultiRowRangeFilter.RowRange;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.inn.commons.Symbol;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.hadoop.hbase.HBaseResult;
import com.inn.commons.http.HttpException;
import com.inn.commons.http.HttpGetRequest;
import com.inn.commons.http.HttpPostRequest;
import com.inn.commons.http.HttpRequest;
import com.inn.commons.io.image.ColorUtils;
import com.inn.commons.io.image.ImageUtils;
import com.inn.commons.io.image.PopulationUtils;
import com.inn.commons.io.kml.KMLGenerator;
import com.inn.commons.io.kml.KMLParser;
import com.inn.commons.lang.StringUtils;
import com.inn.commons.maps.Corner;
import com.inn.commons.maps.LatLng;
import com.inn.commons.maps.geometry.GeometryConstants;
import com.inn.commons.maps.geometry.gis.GIS3DPolygon;
import com.inn.commons.maps.geometry.gis.GISGeometry;
import com.inn.commons.maps.geometry.gis.GISPoint;
import com.inn.commons.maps.rowkey.RowKeyUtils;
import com.inn.commons.maps.tiles.BigTile;
import com.inn.commons.maps.tiles.Tile;
import com.inn.commons.maps.tiles.TileBoundaryUtils;
import com.inn.commons.maps.tiles.TileImageUtils;
import com.inn.commons.maps.tiles.TileRelationship;
import com.inn.commons.maps.tiles.TileUtils;
import com.inn.core.generic.exceptions.application.BusinessException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ConfigEnum;
import com.inn.foresight.core.generic.utils.ConfigUtil;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.generic.wrapper.HBaseResponse;
import com.inn.foresight.core.infra.dao.BoundaryDataDao;
import com.inn.foresight.core.infra.wrapper.Geography;
import com.inn.foresight.core.infra.wrapper.GeographyWrapper;
import com.inn.foresight.core.maplayer.dao.IGenericMapDao;
import com.inn.foresight.core.maplayer.model.KPISummaryDataWrapper;
import com.inn.foresight.core.maplayer.service.IGenericMapService;
import com.inn.foresight.core.maplayer.utils.GenericLayerUtil;
import com.inn.foresight.core.maplayer.utils.GenericMapUtils;
import com.inn.foresight.indexlucene.SpatialIndexSearcher;
import com.inn.product.security.spring.userdetails.CustomerInfo;
import com.inn.product.security.wrapper.UserGeographyWrapper;
import com.inn.product.systemconfiguration.dao.SystemConfigurationDao;
import com.inn.product.systemconfiguration.model.SystemConfiguration;
import com.inn.product.um.geography.dao.GeographyL1Dao;
import com.inn.product.um.geography.dao.GeographyL2Dao;
import com.inn.product.um.geography.dao.GeographyL3Dao;
import com.inn.product.um.geography.dao.GeographyL4Dao;
import com.inn.product.um.geography.dao.OtherGeographyDao;
import com.inn.product.um.geography.dao.SalesL1Dao;
import com.inn.product.um.geography.dao.SalesL2Dao;
import com.inn.product.um.geography.dao.SalesL3Dao;
import com.inn.product.um.geography.dao.SalesL4Dao;
import com.inn.product.um.geography.model.GeographyL1;
import com.inn.product.um.geography.model.GeographyL2;
import com.inn.product.um.geography.model.GeographyL3;
import com.inn.product.um.geography.model.GeographyL4;
import com.inn.product.um.geography.model.SalesL1;
import com.inn.product.um.geography.model.SalesL2;
import com.inn.product.um.geography.model.SalesL3;
import com.inn.product.um.geography.model.SalesL4;
import com.inn.product.um.user.utils.UmConstants;

/**
 * The Class GenericMapServiceImpl.Oss
 */

@Service("GenericMapServiceImpl")
public class GenericMapServiceImpl implements IGenericMapService {

  private static final String IS_AREA_INDEXING_AVAILABLE = "IS_AREA_INDEXING_AVAILABLE";

  /** The generic map service dao. */
  @Autowired
  private IGenericMapDao genericMapServiceDao;

  /** The systemconfigurationdao. */
  @Autowired
  private SystemConfigurationDao systemconfigurationdao;

  /** The geography L 4 dao. */
  @Autowired
  private GeographyL4Dao geographyL4Dao;

  @Autowired
  private GeographyL1Dao geographyL1Dao;

  /** The geography L 2 dao. */
  @Autowired
  private GeographyL2Dao geographyL2Dao;

  /** The geography L 3 dao. */
  @Autowired
  private GeographyL3Dao geographyL3Dao;

  @Autowired
  private OtherGeographyDao otherGeographyDao;

  @Autowired
  private SalesL1Dao salesL1Dao;

  @Autowired
  private SalesL2Dao salesL2Dao;

  @Autowired
  private SalesL3Dao salesL3Dao;

  @Autowired
  private SalesL4Dao salesL4Dao;

  /** The Constant TILE_TO_ZONE_MAP. */
  public static final String TILE_TO_ZONE_MAP = "TILE_TO_ZONE_MAP";
  public static final String TILE_TO_ZONE_COUNT = "TILE_TO_ZONE_COUNT";
  private static final String RSRP = "rsrp";
  private static final String SINR = "sinr";
  private static final String DL = "dl";
  private static final String UL = "ul";
  private static final String RSRQ = "rsrq";
  private static final String BSP = "bsp";
  private static final String PLANNED = "planned";
  private static final String ONAIR = "onair";
  private static Double amslAccuracyFactor;
  private static Map<Integer, Double> populationAccuracyFactorMap;

  /** The format. */
  private DateFormat format = new SimpleDateFormat(ForesightConstants.DDMMYY);

  /** The Constant logger. */
  private static final Logger logger = LogManager.getLogger(GenericMapServiceImpl.class);

  private static final Map<String, Set<Object>> levelWisegeographyMap = new HashMap<>();

  private Map<String, Integer> tilesZoneMap = new HashMap<>();

  /** The AMSL color range list. */
  private List<Map<String, Integer>> amslColorRangeList;

  /** The population color range list. */
  private List<Map<String, Integer>> populationColorRangeList;

  @Autowired
  private BoundaryDataDao boundaryDataDao;

  @Autowired
  private CustomerInfo customerInfo;

  private Boolean isAreaIndexingAvailable;

  /**
   * Gets the dropwizard url.
   * 
   * @param query the query
   * @param restCall the rest call
   * @return the dropwizard url
   */
  public String getDropwizardUrl(String query, String restCall) {

    String url = ConfigUtils.getString(ConfigEnum.MICRO_SERVICE_BASE_URL.getValue()) + restCall;
    logger.info("Generic map dropwizard URL {} , [{}]", url, query);
    if (query != null && query.length() != 0) {
      url += "?" + query;
    }
    logger.info("Final dropwizard URL {}", url);
    return url;
  }

  @Override
  public String getDataFromHbase(List<String> columnList, String tableName, Integer zoom,
      Double nELat, Double nELng, Double sWLat, Double sWLng, String minDate, String maxDate,
      String postFix, String gridColName, String aggType) {
    try {
      String columns = new Gson().toJson(columnList);
      logger.info("Column name  for layer {}", columns);
      StringEntity entity = new StringEntity(columns, ContentType.APPLICATION_JSON);
      List<String> valueList = new ArrayList<>();
      valueList.add(tableName);
      valueList.add(String.valueOf(zoom));
      valueList.add(String.valueOf(nELat));
      valueList.add(String.valueOf(nELng));
      valueList.add(String.valueOf(sWLat));
      valueList.add(String.valueOf(sWLng));
      valueList.add(minDate);
      valueList.add(maxDate);
      valueList.add(postFix);
      valueList.add(gridColName);
      valueList.add(aggType);

      String query = GenericMapUtils.createGenericQuery(valueList, GenericMapUtils.TABLENAME,
          GenericMapUtils.ZOOM, GenericMapUtils.NELAT, GenericMapUtils.NELONG,
          GenericMapUtils.SWLAT, GenericMapUtils.SWLONG, GenericMapUtils.MINDATE,
          GenericMapUtils.MAXDATE, GenericMapUtils.POSTFIX, GenericMapUtils.GRID_COL_NAME,
          GenericMapUtils.AGGREGATION_TYPE);
      logger.info("request parameters for service [{}], [{}], [{}] ,[{}], [{}], [{}], [{}]",
          columns, query, zoom, nELat, nELng, sWLat, sWLng);
      String restCall = GenericMapUtils.DROPWIZARD_GENERICMAP_URL;
      String dropwizardUrl = getDropwizardUrl(query, restCall);
      return new HttpPostRequest(dropwizardUrl, entity).getString();

    } catch (Exception e) {
      logger.error("Error while getting data from rest service {} ",
          ExceptionUtils.getStackTrace(e));
    }

    return Symbol.EMPTY_STRING;
  }

  @Override
  public byte[] getImageForKpiAndZone(String tableName, String tileId, String image, String kpi,
      String date, String siteStatus, String floor, String band, boolean isBigTile) {
    try {
      List<String> valueList = new ArrayList<>();
      valueList.add(tableName);
      valueList.add(tileId);
      valueList.add(image);
      valueList.add(kpi);
      valueList.add(date);
      valueList.add(siteStatus);
      valueList.add(floor);
      valueList.add(band);
      String query = GenericMapUtils.createGenericQuery(valueList, GenericMapUtils.TABLENAME,
          GenericMapUtils.TILEID, GenericMapUtils.IMAGE, GenericMapUtils.KPI, GenericMapUtils.DATE,
          GenericMapUtils.SITESTATUS, GenericMapUtils.FLOOR, GenericMapUtils.BAND);
      String apiName;
      if (isBigTile) {
        apiName = GenericMapUtils.DROPWIZARD_GENERIC_BIGTILE_KPI_ZONE_URL;
      } else {
        apiName = GenericMapUtils.DROPWIZARD_GENERIC_KPI_ZONE_URL;
      }
      HttpGetRequest request = new HttpGetRequest(getDropwizardUrl(query, apiName));
      byte[] streamData = request.getByteArray();
      if (streamData != null) {
        return streamData;
      }
    } catch (Exception e) {
      logger.error("Exception in getting data from Rest Service {}",
          ExceptionUtils.getStackTrace(e));
    }
    return null;
  }

  @Override
  public byte[] getImageForKpiAndZone(String tableName, String tileId, String image, String kpi,
      String date, String siteStatus, String floor, String band) {
    Tile tile = new Tile(tileId);
    BufferedImage finalImage = null;
    byte finalImgArr[] = null;
    Tile parentTile = null;
    String tileRowKey = null;
    int zoneCount = Integer.parseInt(systemconfigurationdao.getValueByName(TILE_TO_ZONE_COUNT));

    if (band != null) {
      Integer projectNumber = null;
      try {
        projectNumber = getProjectNumber(kpi, siteStatus);
        date = date.substring(ForesightConstants.ZERO, ForesightConstants.FOUR)
            + date.charAt(ForesightConstants.FIVE);
      } catch (BusinessException e) {
        logger.error("Exception in getImageForKpiAndZone with trace {}",
            ExceptionUtils.getStackTrace(e));
      }
      if (tile.getTz() > GenericLayerUtil.TILE_BASE_ZOOM) {
        parentTile = TileRelationship.getParentTile(tile, GenericLayerUtil.TILE_BASE_ZOOM);
        tileRowKey = RowKeyUtils.getRowKeyForAtoll(parentTile, date, projectNumber, band,
            getZoneAndTileIdInMap(), floor, zoneCount);
        finalImage = getChildDataFromParent(tableName, tile, image, parentTile, tileRowKey);
        try {
          finalImgArr = ImageUtils.toBytes(finalImage);
        } catch (IOException e) {
          logger.error("Exception inside getImageForKpiAndZone due to {}",
              ExceptionUtils.getStackTrace(e));
        }
      } else {
        tileRowKey = RowKeyUtils.getRowKeyForAtoll(tile, date, projectNumber, band,
            getZoneAndTileIdInMap(), floor, zoneCount);
        byte[] imgArr = getDataForTable(tableName, tileRowKey, image);
        if (imgArr != null) {
          finalImgArr = imgArr;
        }
      }
    }
    return finalImgArr;
  }

  @Override
  public BufferedImage getImageForKpi(String tableName, String tileId, String image, String kpi,
      String date) {
    try {
      List<String> valueList = new ArrayList<>();
      valueList.add(tableName);
      valueList.add(tileId);
      valueList.add(image);
      valueList.add(kpi);
      valueList.add(date);
      String query = GenericMapUtils.createGenericQuery(valueList, GenericMapUtils.TABLENAME,
          GenericMapUtils.TILEID, GenericMapUtils.IMAGE, GenericMapUtils.KPI, GenericMapUtils.DATE);
      String restCall = GenericMapUtils.DROPWIZARD_GENERIC_KPI_URL;
      return new HttpGetRequest(getDropwizardUrl(query, restCall)).getImage();
    } catch (Exception e) {
      logger.error("Error inside getImageForKpi while parsing stream data {}",
          ExceptionUtils.getStackTrace(e));
    }
    return null;
  }

  @Override
  public BufferedImage getBigImageForKpiAndZone(String tableName, String tileId, String image,
      String kpi, String date, String siteStatus, String floor, String band) {
    try {
      Tile tile = new Tile(tileId);
      Map<String, BufferedImage> tileImageMap;
      BigTile bigTile = new BigTile();
      List<Tile> tileList = bigTile.getBaseZoomChildTiles(tile);
      int zoom =
          Integer.parseInt(tileId.split(ForesightConstants.UNDERSCORE)[ForesightConstants.TWO]);
      if (zoom <= ForesightConstants.FIFTEEN) {
        tileImageMap = getImageMapTileForLessorEqualZoom15(tileList, kpi, siteStatus, date, floor,
            zoom, tableName, image, band);
      } else {
        tileImageMap = getImageMapTileForGreaterZoom15(tileList, kpi, siteStatus, date, floor, zoom,
            tableName, image, band);
      }
      return bigTile.getMergedBigTile(tile, tileImageMap);
    } catch (Exception e) {
      logger.error("Exceeption in getBigImageForKpiAndZone {}", ExceptionUtils.getStackTrace(e));
    }
    return null;
  }

  @Override
  public List<Map<String, String>> getBoundaryDataByGeographyNames(List<String> geoGraphyNameList,
      String tableName, List<String> columnList, String area, String type) {
    logger.info("Inside getBoundaryDataByGeographyNames method [{},{},{}]", geoGraphyNameList,
        tableName, columnList);
    try {
      List<List<String>> combineList = new ArrayList<>();
      combineList.add(geoGraphyNameList);
      combineList.add(columnList);
      List<String> valueList = new ArrayList<>();
      valueList.add(tableName);
      valueList.add(area);
      valueList.add(type);
      String query = GenericMapUtils.createGenericQuery(valueList, GenericMapUtils.TABLENAME,
          GenericMapUtils.AREA, "type");
      logger.info("request parameter for Geographydata [{}] , [{}]", query, valueList);
      String restCall = GenericMapUtils.DROPWIZARD_GENERICBOUNDARYBYGEOGRAPHYNAME_URL;
      String url = getDropwizardUrl(query, restCall);
      String columns = new Gson().toJson(combineList);
      return getHttpPostResponse(url, columns);
    } catch (Exception e) {
      logger.error("Error inside getBoundaryDataByGeographyNames method {}",
          ExceptionUtils.getStackTrace(e));
    }
    throw new RestException(GenericMapUtils.ERROR_MSG_FETCHING_DATA);
  }

  private List<Map<String, String>> getHttpPostResponse(String url, String columns)
      throws HttpException, IOException, JsonParseException, JsonMappingException {
    logger.info("Column  name for layer {}", columns);
    StringEntity entity = new StringEntity(columns, ContentType.APPLICATION_JSON);
    HttpRequest http = new HttpPostRequest(url, entity);
    String response = http.getString();
    return new ObjectMapper().readValue(response,
        new TypeReference<List<Map<String, String>>>() {});
  }

  @Override
  public GeographyL4 getGeographyL4LatLng(Double latitude, Double longitude) {
    GeographyL4 geography = null;
    try {
      String geographyName = getGeographyname(GenericMapUtils.GEOGRAPHY_TABLE_NAME, latitude,
          longitude, GenericMapUtils.L4_TYPE);
      if (Utils.isValidString(geographyName)) {
        geography = geographyL4Dao.getGeographyL4ByName(geographyName);
      }
    } catch (Exception e) {
      logger.error("Exception in getGeographyL4IdByLatLng : {} ", ExceptionUtils.getStackTrace(e));
    }
    return geography;
  }

  @Override
  public GeographyL3 getGeographyL3LatLng(Double latitude, Double longitude) {
    GeographyL3 geography = null;
    try {
      String geographyName = getGeographyname(GenericMapUtils.GEOGRAPHY_TABLE_NAME, latitude,
          longitude, GenericMapUtils.L3_TYPE);
      if (Utils.isValidString(geographyName)) {
        geography = geographyL3Dao.getGeographyL3ByName(geographyName);
      }
    } catch (Exception e) {
      logger.error("Exception in getGeographyL3IdByLatLng : {} ", ExceptionUtils.getStackTrace(e));
    }
    return geography;
  }

  @Override
  public List<List<String>> getDataForTable(String tableName, Integer zoom, List<String> columnList,
      Double nELat, Double nELng, Double sWLat, Double sWLng, String minDate, String maxDate,
      Long latestDataDate, String postFix, String gridColName, String aggType) throws IOException {
    List<List<String>> response = new ArrayList<>();
    try {
      if (Utils.hasValidValue(gridColName) && Utils.hasValidValue(aggType)) {
        getTileIdPrefixFullynPartially(zoom, nELat, nELng, sWLat, sWLng, minDate, maxDate, response,
            latestDataDate, postFix, columnList, tableName, gridColName, aggType);
      } else {
        List<String> rowKeyPrefix =
            getLayerRowPreFix(sWLat, sWLng, nELat, nELng, zoom, minDate, maxDate);
        logger.info("Row key prefix are [{}], [{}], [{}],[{}]", tableName, zoom, columnList,
            rowKeyPrefix.size());
        List<List<String>> sublist = Lists.partition(rowKeyPrefix, ForesightConstants.THOUSAND);
        logger.info("Sublist size count {}", sublist.size());
        Scan scan = null;
        for (List<String> prefix : sublist) {
          scan = GenericMapUtils.getScanByColumnList(columnList);
          if (latestDataDate != null) {
            scan.setTimeRange(latestDataDate, System.currentTimeMillis());
          }
          scan.setCacheBlocks(true);
          scan.setCaching(10);
          for (String rkPrefix : prefix) {
            if (postFix != null) {
              rkPrefix = rkPrefix + postFix;
            }
            scan.setRowPrefixFilter(Bytes.toBytes(rkPrefix));
            List<HBaseResult> resultList = genericMapServiceDao.getResultListForScan(scan,
                tableName, GenericMapUtils.GEOGRAPHY_CF);
            response.addAll(convertResultToColumnData(resultList, columnList));
          }
        }
      }
    } catch (Exception e) {
      logger.error("Error in parsing  String {}, {} , ERROR {}", minDate, maxDate,
          ExceptionUtils.getStackTrace(e));
    }
    return response;
  }


  /*
   * @Override public List<List<String>> getDataForTable(String tableName, Integer zoom,
   * List<String> columnList, Double nELat, Double nELng, Double sWLat, Double sWLng, String
   * minDate, String maxDate, Long latestDataDate, String postFix, String gridColName, String
   * aggType) throws IOException { List<List<String>> response = new ArrayList<>(); try {
   * logger.info("Going to get Data for table: {}, zoom: {}, latestDataDate: {}, postFix: {}"
   * ,tableName,zoom,latestDataDate, postFix );
   * 
   * if (Utils.hasValidValue(gridColName) && Utils.hasValidValue(aggType)) { logger.info(
   * "Inside if block of getDataForTable"); getTileIdPrefixFullynPartially(zoom, nELat, nELng,
   * sWLat, sWLng, minDate, maxDate, response, latestDataDate, postFix, columnList, tableName,
   * gridColName, aggType); } else { logger.info("Inside else block of getDataForTable");
   * List<String> rowKeyPrefix = getLayerRowPreFix(sWLat, sWLng, nELat, nELng, zoom, minDate,
   * maxDate); logger.info("Row key prefix are [{}], [{}], [{}],[{}]", tableName, zoom, columnList,
   * rowKeyPrefix.size()); List<List<String>> sublist = Lists.partition(rowKeyPrefix,
   * ForesightConstants.THOUSAND); logger.info("Sublist size count {}", sublist.size()); for
   * (List<String> prefix : sublist) { getDataForPrefixList(tableName, columnList, latestDataDate,
   * postFix, response, prefix); } } } catch (Exception e) { logger.error(
   * "Error in parsing  String {}, {} , ERROR {}", minDate, maxDate,
   * ExceptionUtils.getStackTrace(e)); } return response; }
   */

  /*
   * private void getDataForPrefixList(String tableName, List<String> columnList, Long
   * latestDataDate, String postFix, List<List<String>> response, List<String> prefix) throws
   * IOException { List<Scan> scanList = new ArrayList<>(); Scan scan; scan =
   * GenericMapUtils.getScanByColumnList(columnList); if (latestDataDate != null) { logger.info(
   * "Setting timerange: startTime: {}, endTime: {}",latestDataDate,System.currentTimeMillis());
   * scan.setTimeRange(latestDataDate, System.currentTimeMillis()); } scan.setCacheBlocks(true);
   * scan.setCaching(10); for (String rkPrefix : prefix) { if (postFix != null) { rkPrefix =
   * rkPrefix + postFix; } scan.setRowPrefixFilter(Bytes.toBytes(rkPrefix)); scanList.add(scan); }
   * List<HBaseResult> resultList = new ArrayList<>(); resultList =
   * genericMapServiceDao.getHbaseResultListForScanList(tableName, GenericMapUtils.GEOGRAPHY_CF,
   * scanList, resultList); response.addAll(convertResultToColumnData(resultList, columnList)); }
   */



  private void getTileIdPrefixFullynPartially(Integer zoom, Double nELat, Double nELng,
      Double sWLat, Double sWLng, String minDate, String maxDate, List<List<String>> response,
      Long latestDataDate, String postFix, List<String> columnList, String tableName,
      String gridColName, String aggType) throws ParseException, IOException {

    List<List<List<Double>>> cornerBoundary = new ArrayList<>();
    Corner corner = new Corner(sWLat, sWLng, nELat, nELng);
    cornerBoundary.add(corner.getBoundary());

    Map<String, List<String>> tileIdPrefixFullynPartially = new HashMap<>();
    List<List<String>> tempResponse = new ArrayList<>();
    long tileIdStartTime = System.currentTimeMillis();
    Map<String, List<Tile>> scopeWiseTiles =
        TileBoundaryUtils.getFullynPartiallyCoveredTiles(cornerBoundary, zoom, zoom);
    logger.info("total taking time getting list of tiles in millisecond {} zoom {}",
        System.currentTimeMillis() - tileIdStartTime, zoom);

    if (Utils.hasValidValue(gridColName) && Utils.hasValidValue(minDate)
        && Utils.hasValidValue(maxDate)) {
      columnList.add(GenericMapUtils.GEOGRAPHY_CF + ForesightConstants.COLON
          + gridColName.split(ForesightConstants.COMMA)[0]); // gLat
      columnList.add(GenericMapUtils.GEOGRAPHY_CF + ForesightConstants.COLON
          + gridColName.split(ForesightConstants.COMMA)[1]); // gLng
    }
    long updateTileStartTime = System.currentTimeMillis();

    updateTilePrefixMap(minDate, maxDate, tileIdPrefixFullynPartially, scopeWiseTiles);
    logger.info("total taking time for update tileId prefix in millisecond {} ",
        System.currentTimeMillis() - updateTileStartTime);
    /*
     * List<List<String>> fullTiles =
     * Lists.partition(tileIdPrefixFullynPartially.get(GeometryConstants.FULL), ForesightConstants.
     * THOUSAND); List<List<String>> partialTiles =
     * Lists.partition(tileIdPrefixFullynPartially.get(GeometryConstants.PARTIAL),
     * ForesightConstants.THOUSAND);
     */

    tempResponse.addAll(populatePrefixResponse(nELat, nELng, sWLat, sWLng, latestDataDate, postFix,
        columnList, tableName, gridColName, tileIdPrefixFullynPartially.get(GeometryConstants.FULL),
        ForesightConstants.FALSE, minDate, maxDate));
    tempResponse.addAll(
        populatePrefixResponse(nELat, nELng, sWLat, sWLng, latestDataDate, postFix, columnList,
            tableName, gridColName, tileIdPrefixFullynPartially.get(GeometryConstants.PARTIAL),
            ForesightConstants.TRUE, minDate, maxDate));

    logger.info("Got the Response from the db size:: {}", tempResponse.size());
    if (Utils.hasValidValue(minDate) && Utils.hasValidValue(maxDate)) {
      logger.info("Found Data For only DayWise And Going to Aggregate Data");
      response.addAll(getGridWiseAggregationRecords(tempResponse, columnList, postFix, aggType));
    } else {
      logger.info("Found Data For only Latest");
      response.addAll(tempResponse);
    }
    logger.info("Got the Aggregated Response List Size:::: {}", response.size());
  }

  private void updateTilePrefixMap(String minDate, String maxDate,
      Map<String, List<String>> tileIdPrefixFullynPartially, Map<String, List<Tile>> scopeWiseTiles)
      throws ParseException {
    for (Entry<String, List<Tile>> entry : scopeWiseTiles.entrySet()) {
      List<String> prefixList = new ArrayList<>();
      List<String> tileList = Utils.getPaddedTileIdWithzoom(entry.getValue());
      for (String tile : tileList) {
        String prefixKey = GenericLayerUtil.getPaddedCrc(tile);
        if (minDate != null && maxDate != null && !minDate.equalsIgnoreCase("null")
            && !maxDate.equalsIgnoreCase("null")) {
          getPrefixForDateRange(prefixList, minDate, maxDate, prefixKey);
        } else {
          prefixList.add(prefixKey);
        }
      }
      tileIdPrefixFullynPartially.put(entry.getKey(), prefixList);
    }
  }

  private List<List<String>> populatePrefixResponse(Double nELat, Double nELng, Double sWLat,
      Double sWLng, Long latestDataDate, String postFix, List<String> columnList, String tableName,
      String gridColName, List<String> tileIdCRCPrefix, Boolean isPartial, String minDate,
      String maxDate) throws IOException {
    long readFilterStartTime = System.currentTimeMillis();
    Scan scan = null;
    List<List<String>> tempResponse = new ArrayList<>();
    logger.info("Size of tileid list is {} isPartial {}", tileIdCRCPrefix.size(), isPartial);
    long readStartTime = System.currentTimeMillis();
    scan = GenericMapUtils.getScanByColumnList(columnList);

    for (String rkPrefix : tileIdCRCPrefix) {
      if (postFix != null) {
        rkPrefix = rkPrefix + postFix;
      }
      scan.setRowPrefixFilter(Bytes.toBytes(rkPrefix));
      List<HBaseResult> resultList =
          genericMapServiceDao.getResultListForScan(scan, tableName, GenericMapUtils.GEOGRAPHY_CF);
      logger.info("total taking time for read data from hbase {} for tileIdPrefix {}",
          System.currentTimeMillis() - readStartTime, rkPrefix);
      if (isPartial) {
        long currentTimeMillis = System.currentTimeMillis();
        tempResponse.addAll(convertResultToColumnData(resultList, columnList, nELat, nELng, sWLat,
            sWLng, tableName, minDate, maxDate));
        logger.info("total taking time for partial tileid and filter data {}",
            System.currentTimeMillis() - currentTimeMillis);
      } else {
        long currentTimeMillis = System.currentTimeMillis();
        tempResponse.addAll(convertResultToColumnData(resultList, columnList));
        logger.info("total taking time for full tileid {}",
            System.currentTimeMillis() - currentTimeMillis);

      }

    }
    logger.info("total taking time for read data and filter data {}",
        System.currentTimeMillis() - readFilterStartTime);
    return tempResponse;
  }

  private List<List<String>> getGridWiseAggregationRecords(List<List<String>> tempRecords,
      List<String> columnList, String postFix, String aggType) {
    Map<String, List<List<String>>> gridWiseMap = new HashMap<>();
    List<List<String>> finalOutputList = new ArrayList<>();
    for (List<String> singlePartialRecord : tempRecords) {
      String gLat = singlePartialRecord.get(columnList.size() - ForesightConstants.TWO);
      String gLng = singlePartialRecord.get(columnList.size() - ForesightConstants.ONE);
      String gridKey = gLat + ForesightConstants.COMMA + gLng;
      if (gridWiseMap.containsKey(gridKey)) {
        List<List<String>> gridPreviousRecord = gridWiseMap.get(gridKey);
        gridPreviousRecord.add(singlePartialRecord);
        gridWiseMap.put(gridKey, gridPreviousRecord);
      } else {
        List<List<String>> outerList = new ArrayList<>();
        outerList.add(singlePartialRecord);
        gridWiseMap.put(gridKey, outerList);
      }
    }
    for (Entry<String, List<List<String>>> entry : gridWiseMap.entrySet()) {
      List<List<String>> multipleInputRecord = entry.getValue();
      Map<Integer, String> outputSingleGridRecord = new HashMap<>();
      Boolean isBlank = true;
      Integer first = 0;
      List<String> gridProcessedData = new ArrayList<>();
      for (List<String> singleRecord : multipleInputRecord) {
        int listSize = singleRecord.size();
        if (Utils.hasValidValue(singleRecord.get(listSize - ForesightConstants.FOUR))
            && Utils.hasValidValue(singleRecord.get(listSize - ForesightConstants.FIVE))) {
          first++;
          if (ForesightConstants.LATEST.equalsIgnoreCase(aggType)) {
            for (int i = 0; i < singleRecord.size(); i++) {
              outputSingleGridRecord.put(i, singleRecord.get(i));
            }
            isBlank = false;
          } else if (ForesightConstants.AVERAGE_LOWER.equalsIgnoreCase(aggType)) {
            if (outputSingleGridRecord.isEmpty()) {
              for (int i = 0; i < singleRecord.size(); i++) {
                outputSingleGridRecord.put(i, singleRecord.get(i));
              }
            }
            isBlank = false;
          }
          if (!outputSingleGridRecord.isEmpty()
              && ForesightConstants.AVERAGE_LOWER.equalsIgnoreCase(aggType)
              && first != ForesightConstants.ONE) {
            if (!postFix.equalsIgnoreCase(ForesightConstants.NONE)) {
              Long currentKPICount =
                  Long.parseLong(singleRecord.get(singleRecord.size() - ForesightConstants.FOUR)); // KPI
                                                                                                   // Count
              Double currentKPIValue = Double
                  .parseDouble(singleRecord.get(singleRecord.size() - ForesightConstants.FIVE)); // KPI
                                                                                                 // Value
              Long previousKPICount = Long.parseLong(outputSingleGridRecord
                  .get(outputSingleGridRecord.size() - ForesightConstants.FOUR)); // KPI
              Double previousKPIValue = Double.parseDouble(outputSingleGridRecord
                  .get(outputSingleGridRecord.size() - ForesightConstants.FIVE)); // KPI
              // Value
              Double totalWeightedAvg =
                  ((currentKPICount * currentKPIValue) + (previousKPICount * previousKPIValue))
                      / (currentKPICount + previousKPICount);
              currentKPICount = currentKPICount + previousKPICount;
              outputSingleGridRecord.put(outputSingleGridRecord.size() - ForesightConstants.FOUR,
                  currentKPICount.toString());
              outputSingleGridRecord.put(outputSingleGridRecord.size() - ForesightConstants.FIVE,
                  totalWeightedAvg.toString());
            } else {
              Long ncCount =
                  Long.parseLong(singleRecord.get(singleRecord.size() - ForesightConstants.FOUR));
              Long imsiCount =
                  Long.parseLong(singleRecord.get(singleRecord.size() - ForesightConstants.FIVE));
              Integer previousNCCount = Integer.parseInt(outputSingleGridRecord
                  .get(outputSingleGridRecord.size() - ForesightConstants.FOUR));
              Double previousIMSICount = Double.parseDouble(outputSingleGridRecord
                  .get(outputSingleGridRecord.size() - ForesightConstants.FIVE));

              ncCount = ncCount + previousNCCount;

              if (imsiCount > previousIMSICount) {
                outputSingleGridRecord.put(outputSingleGridRecord.size() - ForesightConstants.FOUR,
                    imsiCount.toString());
              } else {
                outputSingleGridRecord.put(outputSingleGridRecord.size() - ForesightConstants.FOUR,
                    previousIMSICount.toString());
              }
              outputSingleGridRecord.put(outputSingleGridRecord.size() - ForesightConstants.FIVE,
                  ncCount.toString());
            }
          }
        }
      }
      for (int i = 0; i < outputSingleGridRecord.size(); i++) {
        gridProcessedData.add(outputSingleGridRecord.get(i));

      }
      if (!isBlank)
        finalOutputList.add(gridProcessedData);
    }
    return finalOutputList;
  }

  @Override
  public GeographyL2 getGeographyL2LatLng(Double latitude, Double longitude) {
    GeographyL2 geography = null;
    try {
      String geographyName = getGeographyname(GenericMapUtils.GEOGRAPHY_TABLE_NAME, latitude,
          longitude, GenericMapUtils.L2_TYPE);
      if (Utils.isValidString(geographyName)) {
        geography = geographyL2Dao.getGeographyL2ByName(geographyName);
      }
    } catch (Exception e) {
      logger.error("Exception in getGeographyL2IdByLatLng : {} ", ExceptionUtils.getStackTrace(e));
    }
    return geography;
  }

  @Override
  public GeographyL1 getGeographyL1LatLng(Double latitude, Double longitude) {
    GeographyL1 geography = null;
    try {
      String geographyName = getGeographyname(GenericMapUtils.GEOGRAPHY_TABLE_NAME, latitude,
          longitude, GenericMapUtils.L1_TYPE);
      if (Utils.isValidString(geographyName)) {
        geography = geographyL1Dao.getGeographyL1ByName(geographyName);
      }
    } catch (Exception e) {
      logger.error("Exception in getGeographyL1IdByLatLng : {} ", ExceptionUtils.getStackTrace(e));
    }
    return geography;
  }

  @Override
  public String getNearestPointByLatLong(String tableName, Double lat, Double lon) {
    try {
      logger.info("Inside getNearestPointByLatLong method {},{},{}", tableName, lat, lon);
      List<String> valueList = new ArrayList<>();
      valueList.add(tableName);
      valueList.add(String.valueOf(lat));
      valueList.add(String.valueOf(lon));
      String query = GenericMapUtils.createGenericQuery(valueList, GenericMapUtils.TABLENAME,
          GenericMapUtils.LAT, GenericMapUtils.LON);
      logger.info("request parameters for service [{}], [{}], [{}] ,[{}]", valueList, query, lat,
          lon);
      String restCall = GenericMapUtils.DROPWIZARD_NEARESTBOUNDARYBYPOINT_URL;
      String dropwizardUrl = getDropwizardUrl(query, restCall);
      return new HttpGetRequest(dropwizardUrl).getString();
    } catch (Exception e) {
      logger.error(
          "Error occered inside getNearestPointByLatLong while fetching geography data by point {} ",
          ExceptionUtils.getStackTrace(e));
    }
    return ForesightConstants.ERROR;
  }

  @Override
  public String getNearestPointByLatLon(LatLng latLng, String geoLevel) {
    return boundaryDataDao.getNearestGeographyLocations(latLng, geoLevel);
  }

  @Override
  public Scan getScanForRowRange(List<String> rowKeys, List<String> columnList) {
    try {
      List<RowRange> rowrangeList = new ArrayList<>();
      for (String rowKey : rowKeys) {
        byte[] startRow = Bytes.toBytes(rowKey);
        byte[] endRow = new byte[startRow.length + 1];
        System.arraycopy(startRow, 0, endRow, 0, startRow.length);
        endRow[startRow.length] = (byte) ForesightConstants.END_ROW_INDEX;
        RowRange rowRange = new RowRange(startRow, true, endRow, false);
        rowrangeList.add(rowRange);
      }
      logger.info("row range size {}", rowrangeList.size());
      Scan scan = new Scan();
      for (String columnName : columnList) {
        if (columnName.contains(Symbol.COLON_STRING)) {
          String[] cellValues = columnName.split(Symbol.COLON_STRING);
          scan.addColumn(Bytes.toBytes(cellValues[ForesightConstants.ZERO]),
              Bytes.toBytes(cellValues[ForesightConstants.ONE]));
        }
      }
      scan.setFilter(new MultiRowRangeFilter(rowrangeList));
      scan.setCacheBlocks(true);
      scan.setCaching(ForesightConstants.CACHE_SIZE);
      return scan;
    } catch (Exception e) {
      logger.error("Exception Inside @Method getScanForRowRange :{}",
          ExceptionUtils.getStackTrace(e));
    }
    return null;
  }

  @Override
  public List<List<String>> getDataForTableForMultiBoundary(String tableName, Integer zoom,
      List<String> columnList, List<List<List<Double>>> multiBoundary, String minDate,
      String maxDate) throws IOException {
    List<List<String>> response = new ArrayList<>();
    try {
      List<String> rowKeyPrefix =
          getLayerRowPreFixForMultiBoundary(multiBoundary, zoom, minDate, maxDate);

      logger.info("Row key prefix are [{}], [{}], [{}],[{}]", tableName, zoom, columnList,
          rowKeyPrefix.size());
      List<List<String>> sublist = Lists.partition(rowKeyPrefix, ForesightConstants.THOUSAND);
      logger.info("Sublist size count {}", sublist.size());
      for (List<String> prefix : sublist) {
        Scan scan = getScanForRowRange(prefix, columnList);
        List<HBaseResult> results = genericMapServiceDao.getResultListForScan(scan, tableName,
            GenericMapUtils.GEOGRAPHY_CF);
        logger.info("Result size  {}", results.size());
        response.addAll(convertResultToColumnData(results, columnList));
      }
      return response;
    } catch (ParseException e) {
      logger.error("Error in parsing date getDataForTableForMultiBoundary {}, {} , ERROR {}",
          minDate, maxDate, ExceptionUtils.getStackTrace(e));
    }
    return response;
  }

  @Override
  public byte[] getDataForTable(String tableName, String rowKey, String columnName) {
    logger.info("Getting image data {} from table {} and rowKey {}", columnName, tableName, rowKey);
    if (columnName.contains(Symbol.COLON_STRING)) {
      String[] clmData = columnName.split(Symbol.COLON_STRING);
      Get get = new Get(Bytes.toBytes(rowKey));
      get.addFamily(Bytes.toBytes(clmData[0]));
      get.addColumn(Bytes.toBytes(clmData[0]), Bytes.toBytes(clmData[1]));
      try {
        HBaseResult result = genericMapServiceDao.getResultForGet(get, tableName, clmData[0]);
        return result.getValue(Bytes.toBytes(clmData[1]));
      } catch (IOException | BusinessException ex) {
        logger.error("Exception inside getDataForTable due to {}",
            ExceptionUtils.getStackTrace(ex));
      }
    } else {
      throw new RestException(GenericMapUtils.COLUMN_FAMILY_NOT_SUPPLIED);
    }
    throw new RestException(GenericMapUtils.ERROR_MSG_FETCHING_DATA);
  }

  @Override
  public List<String> getCoverageHolePreFix(Double sELat, Double sELng, Double nWLat, Double nWLng,
      Integer zoom, String date) throws ParseException {
    Corner corner = new Corner(sELat, sELng, nWLat, nWLng);
    List<Tile> tileList = TileBoundaryUtils.getTiles(corner, zoom);
    List<String> tileIdList = Utils.getTileId(tileList);
    List<String> prefix = new ArrayList<>();
    for (String tileId : tileIdList) {
      prefix.add(GenericLayerUtil.getCrcOfString(date + ForesightConstants.UNDERSCORE + tileId));
    }
    return prefix;
  }

  @Override
  public List<List<String>> getCoverageHoleData(String tableName, Integer zoom,
      List<String> columnList, Double nWLat, Double nWLng, Double sELat, Double sELng,
      String minDate) {
    List<List<String>> response = new ArrayList<>();

    try {
      List<String> prefixList = getCoverageHolePreFix(sELat, sELng, nWLat, nWLng, zoom, minDate);
      List<List<String>> rowPreFix = Lists.partition(prefixList, ForesightConstants.THOUSAND);
      logger.info("CoverageHole prefix size {}, {}", rowPreFix.size(), prefixList.size());
      for (List<String> prefix : rowPreFix) {
        Scan scan = getScanForRowRange(prefix, columnList);
        List<HBaseResult> results = genericMapServiceDao.getResultListForScan(scan, tableName,
            GenericMapUtils.GEOGRAPHY_CF);
        logger.info("Result size {}", results.size());
        response.addAll(convertResultToColumnData(results, columnList));
      }

    } catch (ParseException | IOException e) {
      logger.error("Error in getting Coveragehole data from Hbase table {}, ERROR {}", tableName,
          ExceptionUtils.getStackTrace(e));
    }

    return response;

  }

  @Override
  public HBaseResponse getDataFromHbaseRest(String query) {
    String restURL = ConfigUtils.getString(ConfigEnum.HBASE_REST_URL.getValue());
    restURL = restURL + query;
    logger.info("Going to get data for URL {}", restURL);
    try {
      HttpRequest request = new HttpGetRequest(restURL);
      request.addHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON);
      return new Gson().fromJson(request.getString(), HBaseResponse.class);
    } catch (HttpException e) {
      logger.error("Errorinside getDataFromHbaseRest while getting data from HBase rest {}",
          ExceptionUtils.getStackTrace(e));
      throw new RestException("Data not found for request.");
    } catch (Exception e) {
      logger.error("Error inside getDataFromHbaseRest while getting data from HBase rest {}",
          ExceptionUtils.getStackTrace(e));
      throw new RestException(e.getMessage());
    }
  }

  @Override
  public String getHbaseDataByRowOrTimeRange(List<String> columnList, String tableName,
      String startRow, String endRow, Long startTime, Long endTime) {
    logger.info(
        "Inside getDataFromHbaseByTimeRange method, taleName {}, columnList: {} startTime {} endTime {}",
        tableName, columnList, startTime, endTime);
    try {
      String columns = new Gson().toJson(columnList);
      logger.info("Column name for layer {}", columns);
      StringEntity entity = new StringEntity(columns, ContentType.APPLICATION_JSON);
      List<String> valueList = new ArrayList<>();
      valueList.add(tableName);
      valueList.add(startRow);
      valueList.add(endRow);
      valueList.add(startTime != null ? startTime.toString() : null);
      valueList.add(endTime != null ? endTime.toString() : null);
      String query = GenericMapUtils.createGenericQuery(valueList, GenericMapUtils.TABLENAME,
          "startRow", "endRow", "startTime", "endTime");
      logger.info(" request parameters for service [{}], [{}], [{}] ,[{}]", valueList, query);
      String restCall = GenericMapUtils.DROPWIZARD_HBASEDATABYROWORTIMERANGE_URL;
      String dropwizardUrl = getDropwizardUrl(query, restCall);
      return new HttpPostRequest(dropwizardUrl, entity).getString();
    } catch (Exception e) {
      logger.error("Errror occured in getHbaseDataByRowOrTimeRange mehtod {}",
          ExceptionUtils.getStackTrace(e));
    }
    throw new RestException(GenericMapUtils.ERROR_MSG_FETCHING_DATA);
  }

  @Override
  public List<Map<String, String>> getHbaseDataByRowOrTimeRangeList(List<String> columnList,
      String startRow, String endRow, String tableName, Long startTime, Long endTime) {
    logger.info(
        "Inside getDataFromHbaseByTimeRange method, taleName {}, columnList: {} startTime {} endTime {}",
        tableName, columnList, startTime, endTime);
    List<Map<String, String>> geographyDataWrapperLIst = new ArrayList<>();
    try {
      List<HBaseResult> resultList = genericMapServiceDao.getHbaseDataByRowOrTimeRange(columnList,
          startRow, endRow, tableName, startTime, endTime);
      return convertResultToMap(resultList, columnList);
    } catch (Exception e) {
      logger.error("Errror occured in getDataFromHbaseByTimeRange mehtod tableName {}", tableName);
    }
    return geographyDataWrapperLIst;
  }

  @Override
  public GeographyL4 getNearestGeographyL4ByLatLng(Double latitude, Double longitude) {
    GeographyL4 geography = null;
    String geographyName = null;
    try {
      geographyName = getGeoName(latitude, longitude);
      if (!Utils.isValidString(geographyName)) {
        geographyName = getNearestPointByLatLong(GenericMapUtils.L4_TYPE, latitude, longitude);
        logger.info("Nearest geographyName : {}", geographyName);
      }
      if (Utils.isValidString(geographyName)) {
        geography = geographyL4Dao.getGeographyL4ByName(geographyName);
      }
    } catch (Exception e) {
      logger.error("Exception in getNearestGeographyL4ByLatLng : {} ",
          ExceptionUtils.getStackTrace(e));
    }
    return geography;
  }

  private String getGeoName(Double latitude, Double longitude) {
    String geographyName = null;
    try {
      geographyName = getGeographyname(GenericMapUtils.GEOGRAPHY_TABLE_NAME, latitude, longitude,
          GenericMapUtils.L4_TYPE);
      logger.info("geographyName : {}", geographyName);
    } catch (Exception e) {
      logger.error("Exception in getGeographyL4IdByLatLng : {} ", ExceptionUtils.getStackTrace(e));
    }
    return geographyName;
  }

  @Override
  public String getGeographyDataByPoint(List<String> columnList, String tableName, Double lat,
      Double lon, Boolean isExact, String subPath) {
    try {
      logger.info("Inside getGeographyDataByPoint method {},{},{},{}", tableName, lat, lon,
          columnList);
      String columns = new Gson().toJson(columnList);
      logger.info("Column name for layer {}", columns);
      StringEntity entity = new StringEntity(columns, ContentType.APPLICATION_JSON);
      List<String> valueList = new ArrayList<>();
      valueList.add(tableName);
      valueList.add(String.valueOf(lat));
      valueList.add(String.valueOf(lon));
      valueList.add(String.valueOf(isExact));
      valueList.add(subPath);
      String query = GenericMapUtils.createGenericQuery(valueList, GenericMapUtils.TABLENAME,
          GenericMapUtils.LAT, GenericMapUtils.LON, GenericMapUtils.ISEXACT,
          GenericMapUtils.SUBPATH);
      logger.info(" request parameters for service [{}], [{}], [{}] ,[{}]", valueList, query, lat,
          lon);
      String restCall = GenericMapUtils.DROPWIZARD_GENERICBOUNDARYBYPOINT_URL;
      String dropwizardUrl = getDropwizardUrl(query, restCall);
      return new HttpPostRequest(dropwizardUrl, entity).getString();
    } catch (Exception e) {
      logger.error(
          "Error occered inside getGeographyDataByPoint while fetching  geography data by point {} ",
          ExceptionUtils.getStackTrace(e));
    }
    return ForesightConstants.ERROR;
  }

  @Override
  public List<List<String>> getGeographyDataByPoint(String tableName, List<String> columnList,
      Double lat, Double lon, Boolean isExact, String subPath) {
    List<HBaseResult> resultList = new ArrayList<>();
    List<List<String>> result = new ArrayList<>();
    try {
      String indexDir = getIndexDir(isExact, subPath, tableName, Symbol.EMPTY_STRING);
      logger.info("index directory {}", indexDir);
      SpatialIndexSearcher spatialIndexSearcher = getSpatialIndexSearcher(tableName, indexDir);
      List<String> rowKeyList = spatialIndexSearcher.getRowKeyListByPoint(lat, lon,
          GenericMapUtils.SEARCH_KEY, tableName);
      List<Get> rowKeyGetList = new ArrayList<>();

      if (CollectionUtils.isEmpty(columnList)) {
        columnList = GenericMapUtils.getColumnListForQuery();
      }

      Set<String> columnSet = new HashSet<>(columnList);

      if (isExact != null && isExact && rowKeyList.size() > NumberUtils.INTEGER_ONE) {
        isExact = false;
      }

      for (String rowKey : rowKeyList) {
        Get get = new Get(rowKey.getBytes());
        for (String column : columnSet) {
          String cName = column.split(Symbol.COLON_STRING)[1];
          get.addColumn(GenericMapUtils.GEOGRAPHY_CF.getBytes(), cName.getBytes());
        }
        if (isExact == null || !isExact) {
          get.addColumn(GenericMapUtils.GEOGRAPHY_CF.getBytes(),
              GenericMapUtils.COORDINATES.getBytes());
        }
        rowKeyGetList.add(get);
      }
      resultList = genericMapServiceDao.getResultForGetList(rowKeyGetList, tableName,
          GenericMapUtils.GEOGRAPHY_CF);
      logger.info("Size of HBase resultLists is {}", resultList.size());
      if (isExact == null || !isExact) {
        resultList =
            getActualGeography(resultList, new LatLng(lat, lon), GenericMapUtils.COORDINATES);
      }
      result = convertResultToColumnData(resultList, columnList);
    } catch (Exception e) {
      logger.error("Exception while fetching data from hbase table due to {}",
          ExceptionUtils.getStackTrace(e));
    }
    return result;
  }

  @Override
  public String getAllGeographyName(Double latitude, Double longitude) {
    logger.info("Inside getAllGeographyName latitude {}, longitude {}", latitude, longitude);
    if (latitude != null && longitude != null) {
      List<String> valueList = new ArrayList<>();
      valueList.add(String.valueOf(latitude));
      valueList.add(String.valueOf(longitude));
      String query =
          GenericMapUtils.createGenericQuery(valueList, GenericMapUtils.LAT, GenericMapUtils.LON);
      try {
        return new HttpGetRequest(
            getDropwizardUrl(query, GenericMapUtils.DOPWIZARD_GET_ALL_GEOGRAPHY_NAME_URL))
                .getString();
      } catch (HttpException e) {
        logger.error("Unable to getAllGeographyName from Micro Service due to {}",
            ExceptionUtils.getStackTrace(e));
      }
    } else {
      logger.error(ForesightConstants.INVALID_PARAMETER);
    }
    return null;
  }

  @Override
  public String[] getAllGeographyName(LatLng location) {
    String[] geographyNames = new String[ForesightConstants.INDEX_FOUR];
    try {
      geographyNames[ForesightConstants.INDEX_ZERO] = boundaryDataDao.findGeographyL1(location);
      geographyNames[ForesightConstants.INDEX_ONE] = boundaryDataDao.findGeographyL2(location);
      geographyNames[ForesightConstants.INDEX_TWO] = boundaryDataDao.findGeographyL3(location);
      geographyNames[ForesightConstants.INDEX_THREE] = boundaryDataDao.findGeographyL4(location);
      logger.info("Returning geographyNames {} for location {}", Arrays.toString(geographyNames),
          location);
    } catch (Exception exception) {
      logger.error("Exception inside getAllGeographyName due to {}",
          ExceptionUtils.getStackTrace(exception));
    }
    return geographyNames;
  }
 
  
  
  @Override
  public String getCustomerCareGeagraphyData(Double lat, Double lng) {
    List<String> valueList = new ArrayList<>();
    valueList.add(String.valueOf(lat));
    valueList.add(String.valueOf(lng));
    String query =
        GenericMapUtils.createGenericQuery(valueList, GenericMapUtils.LAT, GenericMapUtils.LON);
    try {
      return new HttpGetRequest(
          getDropwizardUrl(query, GenericMapUtils.DROPWIZARD_CUSOMER_CARE_GEOGRAPHYNAME_URL))
              .getString();
    } catch (Exception e) {
      logger.error("Unable to get CustomerCareGeagraphyData from Micro Service due to {}",
          ExceptionUtils.getStackTrace(e));
      return ForesightConstants.ERROR;
    }
  }

  @Override
  public String[] getCustomerCareGeography(LatLng location) {
    String[] allGeographyName = getAllGeographyName(location);
    String customerCareGeographyName = boundaryDataDao
        .getCustomerCareGeography(GenericMapUtils.CUSTOMER_REGION_TABLE_NAME, location);
    allGeographyName[0] = customerCareGeographyName;
    logger.info("Returning customercare geographyNames {} for location {}",
        Arrays.toString(allGeographyName), location);
    return allGeographyName;
  }

  @Override
  public Map<String, Set<Object>> getAllNetworkGeography(String polygon) {
    return getAllGeographyByPolygon(polygon, GenericMapUtils.L4_TYPE,
        GenericMapUtils.GEOGRAPHY_TABLE_NAME);
  }

  @Override
  public Map<String, Set<Object>> getAllSalesGeography(String polygon) {
    return getAllGeographyByPolygon(polygon, GenericMapUtils.L4_TYPE,
        ForesightConstants.SALES_GEOGRAPHY_TABLE);
  }

  public Map<String, Set<Object>> getAllGeographyByPolygon(String polygon, String subPath,
      String tableName) {
    try {
      List<String> columnList = Arrays.asList(GenericMapUtils.GEO_COL_NAME);
      String indexDir = getIndexDir(true, subPath, tableName, Symbol.EMPTY_STRING);
      logger.info("IndexDir {}", indexDir);
      SpatialIndexSearcher spatialIndexSearcher = getSpatialIndexSearcher(tableName, indexDir);
      List<String> rowKeyList =
          spatialIndexSearcher.getRowKeyListByViewPort(polygon, GenericMapUtils.SEARCH_KEY);
      List<Get> rowKeyGetList = getRowKeyGetList(rowKeyList, columnList);
      List<HBaseResult> resultForGetList = genericMapServiceDao.getResultForGetList(rowKeyGetList,
          tableName, GenericMapUtils.GEOGRAPHY_CF);
      logger.info("ResultForGetList size {}", resultForGetList.size());
      List<String> l4NameList = new ArrayList<>();
      for (HBaseResult result : resultForGetList) {
        String geographyName = result.getString(GenericMapUtils.GEOGRAPHY_NAME);
        l4NameList.add(geographyName);
      }
      List<Object[]> allGeographyNameByL4 = null;
      if (GenericMapUtils.GEOGRAPHY_TABLE_NAME.equals(tableName)) {
        allGeographyNameByL4 = geographyL4Dao.getAllGeographyNameByL4(l4NameList);
      } else {
        allGeographyNameByL4 = salesL4Dao.getAllGeographyNameBySalesL4(l4NameList);
      }
      initializeLevelWiseGeographyMap();
      for (Object[] obj : allGeographyNameByL4) {
        levelWisegeographyMap.get(GenericMapUtils.L1_TYPE).add(obj[ForesightConstants.INDEX_ZERO]);
        levelWisegeographyMap.get(GenericMapUtils.L2_TYPE).add(obj[ForesightConstants.INDEX_ONE]);
        levelWisegeographyMap.get(GenericMapUtils.L3_TYPE).add(obj[ForesightConstants.INDEX_TWO]);
        levelWisegeographyMap.get(GenericMapUtils.L4_TYPE).add(obj[ForesightConstants.INDEX_THREE]);
      }
    } catch (Exception e) {
      logger.error("Exception inside getAllGeographyByPolygon {}", ExceptionUtils.getStackTrace(e));
    }
    return levelWisegeographyMap;
  }

  private void initializeLevelWiseGeographyMap() {

    levelWisegeographyMap.put(GenericMapUtils.L1_TYPE, new HashSet<>());
    levelWisegeographyMap.put(GenericMapUtils.L2_TYPE, new HashSet<>());
    levelWisegeographyMap.put(GenericMapUtils.L3_TYPE, new HashSet<>());
    levelWisegeographyMap.put(GenericMapUtils.L4_TYPE, new HashSet<>());
  }

  /**
   * getFacebookPopulation
   *
   * @param polygon
   * @param zoomLevel
   * @return
   */
  @Override
  public Double getFacebookPopulation(List<List<Double>> polygon, Integer zoomLevel) {
    logger.info("Inside getFacebookPopulation method, zoomLevel {}", zoomLevel);
    zoomLevel = zoomLevel != null ? zoomLevel : GenericMapUtils.POPULATION_BASE_ZOOM;
    Double populationCount = 0.0;
    try {
      Map<String, Integer> zoneAndTileIdInMap = getZoneAndTileIdInMap();
      Double accuracyFactor =
          getAccuracyFactor(zoomLevel, ForesightConstants.FACEBOOK_POPULATION_TABLE);
      Map<String, BufferedImage> boundaryTileImageMap =
          TileImageUtils.getTileImage(polygon, zoomLevel);
      logger.info("AccuracyFactor is {}, zoomLevel {}", accuracyFactor, zoomLevel);
      for (Map.Entry<String, BufferedImage> boundaryTileImage : boundaryTileImageMap.entrySet()) {
        BufferedImage bufferImage = boundaryTileImage.getValue();
        Tile tile = new Tile(boundaryTileImage.getKey());
        String rowKeyForTileTable = RowKeyUtils.getRowKeyForTileTable(tile, null,
            zoneAndTileIdInMap, GenericMapUtils.TILE_ZONE_MAP_ZOOM);
        HBaseResult resultForGet =
            genericMapServiceDao.getResultForGet(new Get(Bytes.toBytes(rowKeyForTileTable)),
                GenericMapUtils.FACEBOOK_POPULATION_TABLE, GenericMapUtils.GEOGRAPHY_CF);
        byte[] facebookByteImage =
            resultForGet.getValue(Bytes.toBytes(GenericMapUtils.FACEBOOK_IMAGE_COL));
        if (facebookByteImage != null) {
          BufferedImage facebookImage = ImageUtils.toBufferedImage(facebookByteImage);
          populationCount =
              calculatePopulation(populationCount, accuracyFactor, bufferImage, facebookImage);
        } else {
          logger.warn("Facebook image not found for rowKey {}", boundaryTileImage.getKey());
        }
      }
    } catch (Exception e) {
      logger.error("Exception inside getFacebookPopulation due to {} ",
          ExceptionUtils.getStackTrace(e));
    }
    logger.info("populationCount {}", populationCount);
    return populationCount;
  }

  @Override
  public byte[] getImageByteArray(GeographyWrapper geographyWrapper) {
    logger.info("Inside getImageByteArray ");
    try {
      Tile tile = new Tile(geographyWrapper.getTileId());
      int zoom = tile.getTz();
      Map<String, Integer> tileZoneMap = getZoneAndTileIdInMap();
      if (geographyWrapper.getIsBigTile() != null && geographyWrapper.getIsBigTile()) {
        return getBigTileByteArray(geographyWrapper.getTableName(), geographyWrapper.getImgColumn(),
            geographyWrapper.getType(), tile, zoom, tileZoneMap);
      } else {
        if (zoom > geographyWrapper.getBaseZoom()) {
          return getChildFromParent(geographyWrapper, tile, tileZoneMap);
        } else {
          return getChildData(geographyWrapper, tile, tileZoneMap);
        }
      }
    } catch (IOException exception) {
      logger.error("Error in getting getImageByteArray due to {}",
          ExceptionUtils.getStackTrace(exception));
    }
    throw new RestException(ForesightConstants.EXCEPTION_NO_RECORD_FOUND);
  }

  private byte[] getChildData(GeographyWrapper geographyWrapper, Tile tile,
      Map<String, Integer> tileZoneMap) {
    String rowKey = RowKeyUtils.getRowKeyForTileTable(tile, geographyWrapper.getType(), tileZoneMap,
        GenericMapUtils.TILE_ZONE_MAP_ZOOM);
    return getDataForTable(geographyWrapper.getTableName(), rowKey,
        geographyWrapper.getImgColumn());
  }

  private byte[] getChildFromParent(GeographyWrapper geographyWrapper, Tile tile,
      Map<String, Integer> tileZoneMap) throws IOException {
    Tile parentTile = TileRelationship.getParentTile(tile, geographyWrapper.getBaseZoom());
    String parentRowKey = RowKeyUtils.getRowKeyForTileTable(parentTile, geographyWrapper.getType(),
        tileZoneMap, GenericMapUtils.TILE_ZONE_MAP_ZOOM);
    return ImageUtils.toBytes(getChildDataFromParent(geographyWrapper.getTableName(), tile,
        geographyWrapper.getImgColumn(), parentTile, parentRowKey));
  }

  @Override
  public Map<String, Integer> getZoneAndTileIdInMap() {
    if (tilesZoneMap.isEmpty()) {
      try {
        List<SystemConfiguration> systemConfigurationByName =
            systemconfigurationdao.getSystemConfigurationByName(TILE_TO_ZONE_MAP);
        SystemConfiguration systemConfiguration =
            systemConfigurationByName.get(NumberUtils.INTEGER_ZERO);
        String tilesZoneMapAsString = systemConfiguration.getValue();
        ObjectMapper mapper = new ObjectMapper();
        tilesZoneMap =
            mapper.readValue(tilesZoneMapAsString, new TypeReference<Map<String, Integer>>() {});
      } catch (IOException e) {
        logger.error("IO exception with trace {}", ExceptionUtils.getStackTrace(e));
      } catch (Exception e) {
        logger.error("Exception with trace {}", ExceptionUtils.getStackTrace(e));
      }
    }
    return tilesZoneMap;
  }

  /**
   * Gets the child data from parent.
   * 
   * @param tableName the table name
   * @param childTileId the child tile id
   * @param imgColumn the img column
   * @param parentTileId the parent tile id
   * @param parentRowKey the parent row key
   * @return the child data from parent
   */
  private BufferedImage getChildDataFromParent(String tableName, Tile childTileId, String imgColumn,
      Tile parentTileId, String parentRowKey) {
    logger.info(
        "Inside getChildDataFromParent , tableName {},childTileId {},parentTildId {},parentRowKey {}",
        tableName, childTileId.getIdWithZoom(), parentTileId.getIdWithZoom(), parentRowKey);
    BufferedImage childImage = TileUtils.createNewTileImage();
    try {
      byte[] byteArrImg = getDataForTable(tableName, parentRowKey, imgColumn);
      if (byteArrImg != null) {
        BufferedImage parentImage = ImageUtils.toBufferedImage(byteArrImg);
        if (parentImage != null) {
          TileRelationship.drawChildFromParent(childImage, parentImage, childTileId, parentTileId);
        }
      }
    } catch (IOException e) {
      logger.error("Exception inside getChildDataFromParent due to {}",
          ExceptionUtils.getStackTrace(e));
    }
    return childImage;
  }

  /**
   * Gets the image tile for greaterEqual zoom 15.
   * 
   * @param tileList the tile list
   * @param kpi the kpi
   * @param siteStatus the site status
   * @param date the date
   * @param floor the floor
   * @param zoom the zoom
   * @param tableName the table name
   * @param columnData the column data
   * @param band the band
   * @return the image map tile for greater zoom 15
   */
  private Map<String, BufferedImage> getImageMapTileForGreaterZoom15(List<Tile> tileList,
      String kpi, String siteStatus, String date, String floor, int zoom, String tableName,
      String columnData, String band) {
    List<Get> getList = new ArrayList<>();
    int parentZoom = ForesightConstants.FIFTEEN;
    String[] clmData = columnData.split(Symbol.COLON_STRING);
    byte[] columnFamilyBytes = Bytes.toBytes(clmData[ForesightConstants.ZERO]);
    byte[] columnQualifierBytes = Bytes.toBytes(clmData[ForesightConstants.ONE]);
    Integer projectNumber = null;
    int zoneCount = Integer.parseInt(systemconfigurationdao.getValueByName(TILE_TO_ZONE_COUNT));

    try {
      projectNumber = getProjectNumber(kpi, siteStatus);
      date = date.substring(ForesightConstants.ZERO, ForesightConstants.FOUR)
          + date.charAt(ForesightConstants.FIVE);
    } catch (BusinessException e) {
      logger.error("Exception with  trace {}", ExceptionUtils.getStackTrace(e));
    }
    for (Tile tile : tileList) {
      Tile parentTile = TileRelationship.getParentTileRecursively(tile, parentZoom);
      String rowkey = RowKeyUtils.getRowKeyForAtoll(parentTile, date, projectNumber, band,
          getZoneAndTileIdInMap(), floor, zoneCount);
      logger.info("log=> rowkey => {} for tile {} | date {} | kpi {} | siteStatus {} | band {}",
          rowkey, tile, date, kpi, siteStatus, band);
      Get getObject = new Get(Bytes.toBytes(rowkey));
      getObject.addColumn(columnFamilyBytes, columnQualifierBytes);
      getList.add(getObject);
    }
    Map<String, BufferedImage> tileImageMap = genericMapServiceDao.getBufferedImageMap(getList,
        tableName, columnFamilyBytes, columnQualifierBytes, zoom);
    return getChildImageMapforTileMap(parentZoom, tileImageMap);
  }

  @Override
  public byte[] getTileImageData(String tableName, String tileId, String imgColumn, String type,
      Boolean isBigTile, Integer baseZoom, boolean isColorUpdation, Double accuracyFactor) {
    logger.info(
        "Inside getTileImageData of GenericMapService for the parameters tableName: {},"
            + " tileId {} , imgColumn {} , type {},isBigTile {}",
        tableName, tileId, imgColumn, type, isBigTile);
    try {
      List<String> valueList = new ArrayList<>();
      valueList.add(tableName);
      valueList.add(tileId);
      valueList.add(imgColumn);
      valueList.add(type);
      valueList.add(String.valueOf(isBigTile));
      valueList.add(String.valueOf(baseZoom));
      valueList.add(String.valueOf(isColorUpdation));
      valueList.add(String.valueOf(accuracyFactor));
      logger.info("Going to create generic query ");
      String query = GenericMapUtils.createGenericQuery(valueList, GenericMapUtils.TABLENAME,
          GenericMapUtils.TILEID, GenericMapUtils.IMAGE_COLUMN, GenericMapUtils.TYPE,
          GenericMapUtils.IS_BIGTILE, GenericMapUtils.BASE_ZOOM, GenericMapUtils.IS_COLOR_UPDATION,
          GenericMapUtils.ACCURACY_FACTOR);
      byte[] imageByte = new HttpGetRequest(
          getDropwizardUrl(query, GenericMapUtils.DOPWIZARD_GENERIC_TILEIMAGE_URL)).getByteArray();
      if (imageByte != null) {
        return imageByte;
      }
    } catch (Exception exception) {
      logger.error("Error inside getTileImageData while parsing stream data {}",
          ExceptionUtils.getStackTrace(exception));
    }
    throw new RestException(GenericMapUtils.ERROR_MSG_FETCHING_DATA);
  }

  /**
   * Gets the child image mapfor tile map.
   * 
   * @param parentZoom the parent zoom
   * @param zoom the zoom
   * @param tileImageMap the tile image map
   * @return the child image mapfor tile map
   */
  private Map<String, BufferedImage> getChildImageMapforTileMap(int parentZoom,
      Map<String, BufferedImage> tileImageMap) {
    for (Map.Entry<String, BufferedImage> tileImage : tileImageMap.entrySet()) {
      BufferedImage childImage = new BufferedImage(GeometryConstants.TILE_SIZE,
          GeometryConstants.TILE_SIZE, BufferedImage.TYPE_INT_ARGB);
      Tile childTile = new Tile(tileImage.getKey());
      BufferedImage parentImage = tileImage.getValue();
      Tile parentTile = TileRelationship.getParentTileRecursively(childTile, parentZoom);
      TileRelationship.drawChildFromParent(childImage, parentImage, childTile, parentTile);
      tileImageMap.put(childTile.getIdWithZoom(), childImage);
    }
    return tileImageMap;
  }

  /**
   * Gets the image tile for lessorEqual zoom 15.
   * 
   * @param tileList the tile list
   * @param kpi the kpi
   * @param siteStatus the site status
   * @param date the date
   * @param floor the floor
   * @param zoom the zoom
   * @param tableName the table name
   * @param columnName the column name
   * @param band the band
   * @return the image map tile for lessor equal zoom 15
   */
  private Map<String, BufferedImage> getImageMapTileForLessorEqualZoom15(List<Tile> tileList,
      String kpi, String siteStatus, String date, String floor, int zoom, String tableName,
      String columnName, String band) {
    String[] clmData = columnName.split(Symbol.COLON_STRING);
    byte[] columnFamilyBytes = Bytes.toBytes(clmData[ForesightConstants.ZERO]);
    byte[] columnQualifierBytes = Bytes.toBytes(clmData[ForesightConstants.ONE]);
    List<Get> getList = new ArrayList<>();
    Integer projectNumber = null;
    String zoneCount = systemconfigurationdao.getValueByName(TILE_TO_ZONE_COUNT);
    int zone = Integer.parseInt(zoneCount);
    try {
      projectNumber = getProjectNumber(kpi, siteStatus);
      date = date.substring(ForesightConstants.ZERO, ForesightConstants.FOUR)
          + date.charAt(ForesightConstants.FIVE);
    } catch (BusinessException e) {
      logger.error("Exception with trace {}", ExceptionUtils.getStackTrace(e));
    }

    for (Tile tile : tileList) {
      String rowkey = RowKeyUtils.getRowKeyForAtoll(tile, date, projectNumber, band,
          getZoneAndTileIdInMap(), floor, zone);
      logger.info("log=> rowkey => {} for tile {} | date {} | kpi {} | siteStatus {} | band {}",
          rowkey, tile, date, kpi, siteStatus, band);
      Get getObject = new Get(Bytes.toBytes(rowkey));
      getObject.addColumn(columnFamilyBytes, columnQualifierBytes);
      getList.add(getObject);
    }
    return genericMapServiceDao.getBufferedImageMap(getList, tableName, columnFamilyBytes,
        columnQualifierBytes, zoom);
  }

  /**
   * Gets the geographyname.
   * 
   * @param tableName the table name
   * @param geographyZoomLevelName the geography zoom level name
   * @param latitude the latitude
   * @param longitude the longitude
   * @param geoLevel the geo level
   * @return the geographyname
   * @throws RestException the rest exception
   */
  private String getGeographyname(String tableName, Double latitude, Double longitude,
      String geoLevel) {
    try {
      String response = getGeographyDataByPoint(GenericMapUtils.getGeoColumnList(), tableName,
          latitude, longitude, false, geoLevel);
      List<List<String>> list =
          new Gson().fromJson(response, new TypeToken<List<List<String>>>() {}.getType());
      if (list.isEmpty()) {
        throw new RestException(ForesightConstants.GEOGRAPHY_NOT_FOUND);
      }
      return list.get(0).get(0);
    } catch (Exception e) {
      logger.error("Exception inside getGeographyname while getting Geography Name : {}",
          ExceptionUtils.getStackTrace(e));
      throw new RestException(ForesightConstants.GEOGRAPHY_NOT_FOUND);
    }
  }

  private Double calculatePopulation(Double populationCount, Double accuracyFactor,
      BufferedImage bufferImage, BufferedImage facebookImage) {
    for (int x = 0; x < GeometryConstants.TILE_SIZE; x++) {
      for (int y = 0; y < GeometryConstants.TILE_SIZE; y++) {
        if (bufferImage.getRGB(x, y) != 0) {
          int populationColor = facebookImage.getRGB(x, y);
          if (populationColor != 0) {
            double popFromRGB = PopulationUtils.getPOPFromRGB(populationColor, accuracyFactor);
            populationCount = populationCount + popFromRGB;
          }

        }
      }
    }
    return populationCount;
  }

  /**
   * Populate get list.
   * 
   * @param type the type
   * @param tileZoneMap the tile zone map
   * @param getList the get list
   * @param imgColumn the img column
   * @param tiles the tiles
   */
  private void populateGetList(String type, Map<String, Integer> tileZoneMap, List<Get> getList,
      String imgColumn, Tile tiles) {
    String rowKey = RowKeyUtils.getRowKeyForTileTable(tiles, type, tileZoneMap,
        GenericMapUtils.TILE_ZONE_MAP_ZOOM);
    logger.info("rowKey {}", rowKey);
    Get get = new Get(Bytes.toBytes(rowKey));
    get.addFamily(Bytes.toBytes(imgColumn.split(Symbol.COLON_STRING)[0]));
    get.addColumn(Bytes.toBytes(imgColumn.split(Symbol.COLON_STRING)[0]),
        Bytes.toBytes(imgColumn.split(Symbol.COLON_STRING)[1]));
    getList.add(get);

  }

  /**
   * Gets the tile from rowkey.
   * 
   * @param rowKey the row key
   * @param zoom the zoom
   * @return the tile from rowkey
   */
  private List<String> getTileFromRowkey(String rowKey, int zoom) {
    List<String> tileIdList = new ArrayList<>();
    if (zoom > GenericLayerUtil.TILE_BASE_ZOOM) {
      String tileId = rowKey.substring(rowKey.length() - ForesightConstants.TWELVE)
          + StringUtils.leftPad(String.valueOf(GenericLayerUtil.TILE_BASE_ZOOM),
              ForesightConstants.TWO, ForesightConstants.ZERO_STRING);
      Tile tile = new Tile(tileId);
      List<Tile> tiles = TileRelationship.getBufferedChildren(tile, zoom, ForesightConstants.ZERO);
      tileIdList = Utils.getTileIdWithZoom(tiles);
    } else {
      String tileId = rowKey.substring(rowKey.length() - ForesightConstants.TWELVE) + StringUtils
          .leftPad(String.valueOf(zoom), ForesightConstants.TWO, ForesightConstants.ZERO_STRING);
      Tile tile = new Tile(tileId);
      String idWithZoom = tile.getIdWithZoom();
      tileIdList.add(idWithZoom);
    }
    return tileIdList;

  }

  /**
   * Populate tile image map.
   * 
   * @param tableName the table name
   * @param imgColumn the img column
   * @param zoom the zoom
   * @param getList the get list
   * @return the map
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private Map<String, BufferedImage> populateTileImageMap(String tableName, String imgColumn,
      int zoom, List<Get> getList) throws IOException {
    Map<String, BufferedImage> tileImageMap = new HashMap<>();
    List<HBaseResult> resultForGetList = genericMapServiceDao.getResultForGetList(getList,
        tableName, imgColumn.split(Symbol.COLON_STRING)[0]);
    if (CollectionUtils.isNotEmpty(resultForGetList)) {
      logger.info("getList size {} HBaseResult size {}", getList.size(), resultForGetList.size());
      for (HBaseResult result : resultForGetList) {
        if (result != null && Bytes.toString(result.getRow()) != null) {
          byte[] value = result.getValue(Bytes.toBytes(imgColumn.split(Symbol.COLON_STRING)[1]));
          BufferedImage bufferedImage = ImageUtils.toBufferedImage(value);
          List<String> tileIdList = getTileFromRowkey(Bytes.toString(result.getRow()), zoom);
          for (String tiles : tileIdList) {
            tileImageMap.put(tiles, bufferedImage);
          }
        }
      }
    }
    return tileImageMap;
  }

  /**
   * Gets the big tile byte array.
   * 
   * @param tableName the table name
   * @param imgColumn the img column
   * @param type the type
   * @param tile the tile
   * @param zoom the zoom
   * @param tileZoneMap the tile zone map
   * @return the big tile byte array
   */
  private byte[] getBigTileByteArray(String tableName, String imgColumn, String type, Tile tile,
      int zoom, Map<String, Integer> tileZoneMap) {
    BigTile bigTile = new BigTile();
    List<Tile> tileList = bigTile.getBaseZoomChildTiles(tile);
    logger.info("tileList for bigtile size {}", tileList.size());
    List<Get> getList = new ArrayList<>();
    try {
      if (zoom > GenericLayerUtil.TILE_BASE_ZOOM) {
        for (Tile tiles : tileList) {
          Tile parentTile = TileRelationship.getParentTile(tiles, GenericLayerUtil.TILE_BASE_ZOOM);
          populateGetList(type, tileZoneMap, getList, imgColumn, parentTile);
        }
        Map<String, BufferedImage> populateTileImageMap =
            populateTileImageMap(tableName, imgColumn, zoom, getList);
        populateTileImageMap =
            getChildImageMapforTileMap(GenericLayerUtil.TILE_BASE_ZOOM, populateTileImageMap);
        return ImageUtils.toBytes(bigTile.getMergedBigTile(tile, populateTileImageMap));
      } else {
        for (Tile tiles : tileList) {
          populateGetList(type, tileZoneMap, getList, imgColumn, tiles);
        }
        Map<String, BufferedImage> populateTileImageMap =
            populateTileImageMap(tableName, imgColumn, zoom, getList);
        return ImageUtils.toBytes(bigTile.getMergedBigTile(tile, populateTileImageMap));
      }
    } catch (Exception e) {
      logger.error("Error inside getBigTileByteArray due to {}", ExceptionUtils.getStackTrace(e));
    }
    throw new RestException(GenericMapUtils.ERROR_MSG_FETCHING_DATA);
  }

  /**
   * Gets the spatial index searcher.
   * 
   * @param tableName the table name
   * @param indexDir the index dir
   * @return the spatial index searcher
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public SpatialIndexSearcher getSpatialIndexSearcher(String tableName, String indexDir)
      throws IOException {
    SpatialIndexSearcher indexSearcher = new SpatialIndexSearcher();
    String indexPath = ConfigUtils.getString(ConfigUtil.HDFS_HBASE_INDEXING_PATH);
    if (indexDir == null) {
      indexPath += tableName;
    } else {
      indexPath += indexDir;
    }
    logger.info("Index path :{}", indexPath);
    indexSearcher.setSearchIndexPath(indexPath, indexDir);
    return indexSearcher;

  }

  /**
   * Filter hbase result for area.
   * 
   * @param results the results
   * @param area the area
   * @return the list
   */
  private List<HBaseResult> filterHbaseResultForArea(List<HBaseResult> results, Double area) {
    List<HBaseResult> resultNewList = new ArrayList<>();
    try {
      for (HBaseResult result : results) {
        Double areaOfBoundary = result.getStringAsDouble(GenericMapUtils.AREA);
        if (areaOfBoundary != null && area < areaOfBoundary) {
          resultNewList.add(result);
        }
      }
      logger.info("Result size after areawise filter {}", resultNewList.size());
    } catch (Exception e) {
      logger.error("Exception inside filterHbaseResultForArea due to {}",
          ExceptionUtils.getStackTrace(e));
    }
    return resultNewList;
  }

  /**
   * Gets the layer row pre fix for multi boundary.
   * 
   * @param multiBoundary the multi boundary
   * @param zoom the zoom
   * @param minDate the min date
   * @param maxDate the max date
   * @return the layer row pre fix for multi boundary
   * @throws ParseException the parse exception
   */
  private List<String> getLayerRowPreFixForMultiBoundary(List<List<List<Double>>> multiBoundary,
      Integer zoom, String minDate, String maxDate) throws ParseException {
    List<String> tileIdList = GenericLayerUtil.getTileIdFromMultiBoundary(multiBoundary, zoom);
    logger.info("TileId list size : {}", tileIdList.size());
    List<String> prefix = new ArrayList<>();
    for (String tileId : tileIdList) {
      String crc = GenericLayerUtil.getCrcOfString(tileId);
      if (minDate != null && maxDate != null) {
        getPrefixForDateRange(prefix, minDate, maxDate, crc);
      } else {
        prefix.add(crc);
      }
    }
    return prefix;
  }

  /**
   * Gets the row key get list.
   * 
   * @param rowKeyList the row key list
   * @param columnList the column list
   * @return the row key get list
   */
  public List<Get> getRowKeyGetList(List<String> rowKeyList, List<String> columnList) {
    Set<String> columnSet = new HashSet<>(columnList);
    logger.info("Getting getList for rowKeyList size {} and column list size {}", rowKeyList.size(),
        columnList.size());
    List<Get> getList = new ArrayList<>();
    for (String rowKey : rowKeyList) {
      Get get = new Get(rowKey.getBytes());
      for (String column : columnSet) {
        get.addColumn(GenericMapUtils.GEOGRAPHY_CF.getBytes(),
            column.split(Symbol.COLON_STRING)[1].getBytes());
      }
      getList.add(get);
    }
    return getList;
  }

  /**
   * Gets the actual geography.
   * 
   * @param results the results
   * @param latLng the lat lng
   * @param coordinatesColumn the coordinates column
   * @return the actual geography
   * @throws JsonParseException the json parse exception
   * @throws JsonMappingException the json mapping exception
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private List<HBaseResult> getActualGeography(List<HBaseResult> results, LatLng latLng,
      String coordinatesColumn) throws IOException {
    List<HBaseResult> resultList = new ArrayList<>();
    for (HBaseResult result : results) {
      String value = Bytes.toString(result.getValue(Bytes.toBytes(coordinatesColumn)));
      if (value != null) {
        ObjectMapper mapper = new ObjectMapper();
        List<List<List<Double>>> points3D =
            mapper.readValue(value, new TypeReference<List<List<List<Double>>>>() {});
        if (new GIS3DPolygon(points3D).contains(new GISPoint(latLng))) {
          resultList.add(result);
        }
      }
    }
    return resultList;
  }

  /**
   * Get Date wise HBase row key.
   * 
   * @param rowKeys the row keys
   * @param minDate the min date
   * @param maxDate the max date
   * @param prefix the prefix
   * @return the prefix for date range
   * @throws ParseException the parse exception
   */
  private List<String> getPrefixForDateRange(List<String> rowKeys, String minDate, String maxDate,
      String prefix) throws ParseException {
    logger.info("minDate: {}, maxDate: {}", minDate, maxDate);
    long minValue = format.parse(minDate).getTime();
    long maxValue = format.parse(maxDate).getTime();
    logger.info("minValue: {}, maxValue: {}", minValue, maxValue);

    while (minValue <= maxValue) {
      String date = format.format(new Date(minValue));
      date = new StringBuffer(date).deleteCharAt(ForesightConstants.FOUR).toString();
      rowKeys.add(prefix + date);
      minValue = DateUtils.addDays(new Date(minValue), ForesightConstants.ONE).getTime();
    }

    return rowKeys;
  }

  /**
   * Convert result to map.
   * 
   * @param results the results
   * @param columnList the column list
   * @return the list
   */
  private List<Map<String, String>> convertResultToMap(List<HBaseResult> results,
      List<String> columnList) {
    List<Map<String, String>> boundaryData = new ArrayList<>();
    try {
      for (HBaseResult result : results) {
        Map<String, String> mapData = new HashMap<>();
        for (String clms : columnList) {
          String[] cell = clms.split(Symbol.COLON_STRING);
          String value = Bytes.toString(result.getValue(Bytes.toBytes(cell[1])));
          if (value != null) {
            mapData.put(cell[1], value);
          }
        }
        boundaryData.add(mapData);
      }
    } catch (Exception e) {
      logger.error("Exception while converting result data into map due to {}",
          ExceptionUtils.getStackTrace(e));
    }
    return boundaryData;
  }

  /**
   * Convert result to column data.
   * 
   * @param results the results
   * @param columnList the column list
   * @return the list
   */
  public List<List<String>> convertResultToColumnData(List<HBaseResult> results,
      List<String> columnList) {
    List<List<String>> data = new ArrayList<>();
    if (results != null && !results.isEmpty()) {
      for (HBaseResult result : results) {
        List<String> resultData = new ArrayList<>();
        for (String clms : columnList) {
          String[] cell = clms.split(Symbol.COLON_STRING);
          String value = result.getString(cell[NumberUtils.INTEGER_ONE]);
          if (value == null) {
            value = Symbol.EMPTY_STRING;
          }
          resultData.add(value);
        }
        data.add(resultData);
      }
    }
    return data;
  }

  public List<List<String>> convertResultToColumnData(List<HBaseResult> results,
      List<String> columnList, Double nELat, Double nElng, Double sWLat, Double sWLng,
      String tableName, String minDate, String maxDate) {
    logger.info("Found the column List {}", columnList);
    Corner viewPort = new Corner(sWLat, sWLng, nELat, nElng);
    List<List<String>> data = new ArrayList<>();
    for (HBaseResult result : results) {
      Boolean isDataAddIntoList = true;
      Boolean isAddHbaseDataIntoList = true;
      List<String> resultData = new ArrayList<>();
      for (String clms : columnList) {
        Double bottomRightLat = null;
        Double bottomRightLong = null;
        String[] cell = clms.split(Symbol.COLON_STRING);
        String value = result.getString(cell[NumberUtils.INTEGER_ONE]);
        Double topLeftLat = result.getStringAsDouble(
            columnList.get(0).split(Symbol.COLON_STRING)[NumberUtils.INTEGER_ONE]);
        Double topLeftLong = result.getStringAsDouble(
            columnList.get(1).split(Symbol.COLON_STRING)[NumberUtils.INTEGER_ONE]);

        if (columnList.contains("r:rlat") && columnList.contains("r:rlon")) {
          bottomRightLat = result.getStringAsDouble(
              columnList.get(2).split(Symbol.COLON_STRING)[NumberUtils.INTEGER_ONE]);
          bottomRightLong = result.getStringAsDouble(
              columnList.get(3).split(Symbol.COLON_STRING)[NumberUtils.INTEGER_ONE]);
        }
        if (value == null) {
          value = Symbol.EMPTY_STRING;
        }
        if (bottomRightLat != null && bottomRightLong != null) {
          Corner gridCorner = new Corner(new LatLng(topLeftLat, topLeftLong),
              new LatLng(bottomRightLat, bottomRightLong));
          LatLng bottomLeftPosition = gridCorner.getBottomLeftPosition();
          LatLng bottomRightPosition = gridCorner.getBottomRightPosition();
          LatLng topLeftPosition = gridCorner.getTopLeftPosition();
          LatLng topRightPosition = gridCorner.getTopRightPosition();
          if (isContains(viewPort, bottomLeftPosition) || isContains(viewPort, bottomRightPosition)
              || isContains(viewPort, topLeftPosition) || isContains(viewPort, topRightPosition)) {
            resultData.add(value);
            isAddHbaseDataIntoList = false;
          }
        } else {
          if (topLeftLat != null) {
            Double latitude = Double.valueOf(topLeftLat);
            if (latitude > nELat || latitude < sWLat) {
              isDataAddIntoList = false;
              break;
            }
          }
          if (topLeftLong != null) {
            Double longitude = Double.valueOf(topLeftLong);
            if (longitude > nElng || longitude < sWLng) {
              isDataAddIntoList = false;
              break;
            }
          }
        }

        if (isAddHbaseDataIntoList) {
          resultData.add(value);
        }
      }
      if (isDataAddIntoList) {
        data.add(resultData);
      }
    }
    return data;
  }

  public boolean isContains(Corner corner, LatLng point) {
    if (corner.getMinLatitude() < point.getLatitude()
        && corner.getMaxLatitude() > point.getLatitude()
        && corner.getMinLongitude() < point.getLongitude()
        && corner.getMaxLongitude() > point.getLongitude()) {
      return true;
    }
    return false;
  }

  /**
   * Gets the index dir.
   * 
   * @param isExact the is exact
   * @param subPath the sub path
   * @param tableName the table name
   * @return the index dir
   */
  private String getIndexDir(Boolean isExact, String subPath, String tableName,
      String areaFilterPath) {

    areaFilterPath = StringUtils.isNotEmpty(areaFilterPath) ? areaFilterPath : Symbol.EMPTY_STRING;
    if (StringUtils.isNotEmpty(subPath)) {
      if ((StringUtils.equalsIgnoreCase(tableName, GenericMapUtils.SALES_GEOGRAPHY_TABLE)
          || StringUtils.equalsIgnoreCase(tableName, GenericMapUtils.GEOGRAPHY_TABLE_NAME))
          && !subPath.contains(Symbol.SLASH_FORWARD_STRING)) {
        String boundaryDate = boundaryDataDao.getLatestBoundaryDate(tableName, subPath);
        if (StringUtils.isNotEmpty(boundaryDate)) {
          subPath += Symbol.SLASH_FORWARD_STRING + boundaryDate;
        }
      }

      return tableName + Symbol.SLASH_FORWARD_STRING + subPath.toUpperCase()
          + Symbol.SLASH_FORWARD_STRING + areaFilterPath + getIndexType(isExact);
    } else {
      return tableName + Symbol.SLASH_FORWARD_STRING + areaFilterPath + getIndexType(isExact);
    }
  }

  /**
   * @param subPath
   * @param tableName
   * @return
   */
  private static String getIndexDirPoint(String subPath, String tableName) {
    if (StringUtils.isNotEmpty(subPath)) {
      return tableName + Symbol.SLASH_FORWARD_STRING + subPath.toUpperCase()
          + Symbol.SLASH_FORWARD_STRING + GenericMapUtils.POINT_INDEXPATH;
    } else {
      return tableName + Symbol.SLASH_FORWARD_STRING + GenericMapUtils.POINT_INDEXPATH;
    }
  }

  /**
   * Gets the index type.
   * 
   * @param isExact the is exact
   * @return the index type
   */
  private static String getIndexType(Boolean isExact) {
    if (isExact != null && isExact) {
      return GenericMapUtils.POLY_INDEXPATH;
    }
    return GenericMapUtils.BOX_INDEXPATH;
  }

  /**
   * Gets the boundary data.
   * 
   * @param nELat the n E lat
   * @param nELng the n E lng
   * @param sWLat the s W lat
   * @param sWLng the s W lng
   * @param tableName the table name
   * @param columnList the column list
   * @param indexDir the index dir
   * @return the boundary data
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public List<HBaseResult> getBoundaryData(Double nELat, Double nELng, Double sWLat, Double sWLng,
      String tableName, List<String> columnList, String indexDir) throws IOException {
    List<HBaseResult> resultList = new ArrayList<>();
    try {
      SpatialIndexSearcher spatialIndexSearcher = getSpatialIndexSearcher(tableName, indexDir);
      List<String> rowKeyList = spatialIndexSearcher.getRowKeyListByViewPort(sWLng, sWLat, nELng,
          nELat, GenericMapUtils.SEARCH_KEY);
      List<Get> rowKeyGetList = getRowKeyGetList(rowKeyList, columnList);
      return genericMapServiceDao.getResultForGetList(rowKeyGetList, tableName,
          GenericMapUtils.GEOGRAPHY_CF);
    } catch (Exception e) {
      logger.error("Exception inside getBoundaryData due to {}", ExceptionUtils.getStackTrace(e));
    }
    return resultList;
  }

  /**
   * Create HBase RowKey Prefix Use RowKeyUtils.getRowKeyForBin(latlng, zoom) method while creating
   * row key for layer, because we have introduced left padding with zero on crc of tile id (10
   * digit) in generic structure
   * 
   * @param sELat the s E lat
   * @param sELng the s E lng
   * @param nWLat the n W lat
   * @param nWLng the n W lng
   * @param zoom the zoom
   * @param minDate the min date
   * @param maxDate the max date
   * @return the layer row pre fix
   * @throws ParseException the parse exception
   */
  private List<String> getLayerRowPreFix(Double sELat, Double sELng, Double nWLat, Double nWLng,
      Integer zoom, String minDate, String maxDate) throws ParseException {

    List<String> tileIdList =
        GenericLayerUtil.getTileIdFromViewPort(sELat, sELng, nWLat, nWLng, zoom);
    logger.info("TILE ID LIST SIZE : {}", tileIdList.size());
    List<String> prefixList = new ArrayList<>();
    for (String tileId : tileIdList) {
      String prefix = GenericLayerUtil.getPaddedCrc(tileId);
      if (minDate != null && maxDate != null && !minDate.equalsIgnoreCase("null")
          && !maxDate.equalsIgnoreCase("null")) {
        getPrefixForDateRange(prefixList, minDate, maxDate, prefix);
      } else {
        prefixList.add(prefix);
      }
    }
    logger.info("Prefix List [{}]", prefixList);
    return prefixList;
  }

  @Override
  public List<Map<String, String>> getBoundaryDataByGeographyNamesMS(List<String> geoGraphyNameList,
      String tableName, List<String> columnList, Double area, String type) {

    List<Map<String, String>> geographyDataWrapperLIst = new ArrayList<>();
    try {

      if (CollectionUtils.isEmpty(columnList)) {
        columnList = GenericMapUtils.getColumnListForQuery();
      }

      List<String> selectColumnList = new ArrayList<>(columnList);
      boolean isAreaFilterRequired = false;
      if (area != null && area > 0) {
        selectColumnList.add(GenericMapUtils.GEO_COL_AREA);
        isAreaFilterRequired = true;
      }

      List<HBaseResult> resultList = genericMapServiceDao.getResultForBoundaryDataByGeographyNames(
          geoGraphyNameList, tableName, selectColumnList, area, type);
      logger.info("result size {}", resultList.size());
      if (isAreaFilterRequired)
        resultList = filterHbaseResultForArea(resultList, area);
      geographyDataWrapperLIst = convertResultToMap(resultList, columnList);
      logger.info("geographyDataWrapperLIst{}", geographyDataWrapperLIst.size());

    } catch (Exception e) {
      logger.error("Exception inside getBoundaryDataByGeographyNames due to {}",
          ExceptionUtils.getStackTrace(e));
    }
    return geographyDataWrapperLIst;
  }

  @Override
  public List<HBaseResult> getHbaseResultForPrefixList(String tableName,
      List<String> tileIdPrefixList, List<String> columns, String columnFamily) {
    Scan scan = getScanForRowRange(tileIdPrefixList, columns);
    List<HBaseResult> hbaseResult = new ArrayList<>();
    try {
      hbaseResult = genericMapServiceDao.getResultListForScan(scan, tableName, columnFamily);
    } catch (IOException e) {
      logger.error("Getting Error in getHbaseResultForPrefixList method {}",
          ExceptionUtils.getStackTrace(e));
    }
    return hbaseResult;
  }

  /**
   * Gets the Population And Height.
   *
   * @param latLng the lat lng
   * @param accFactor the accuracy Factor
   * @param tableName the table Name
   * @param zoomLevel the tile zoom
   * @param maxRange the max range
   * @param minRange the min range
   * @return the Population And Height
   */
  @Override
  public Object getPopulationAndHeight(LatLng latLng, Double accFactor, String tableName,
      Integer zoomLevel, Integer maxRange, Integer minRange) {
    Double valueAtLatLong = GenericMapUtils.DEFAULT_AMSL_POPULATION_VALUE;
    try {
      accFactor = GenericMapUtils.checkAccuracyFactor(accFactor);
      logger.info(
          "Inside getPopulationAndHeight for latitude {}, longitude {},accuracyFactor {},tableName {}",
          latLng.getLatitude(), latLng.getLongitude(), accFactor, tableName);
      Tile tile = new Tile(latLng, zoomLevel);
      Map<String, Integer> zoneAndTileIdMap = getZoneAndTileIdInMap();
      String rowKeyForTileTable = RowKeyUtils.getRowKeyForTileTable(tile, null, zoneAndTileIdMap,
          GenericMapUtils.TILE_ZONE_MAP_ZOOM);
      HBaseResult populationAMSLResult = genericMapServiceDao.getResultForGet(
          new Get(Bytes.toBytes(rowKeyForTileTable)), tableName, GenericMapUtils.GEOGRAPHY_CF);
      byte[] byteImage = populationAMSLResult.getValue(Bytes.toBytes(GenericMapUtils.IMAGE_COL));
      if (byteImage != null) {
        BufferedImage bufferedImage = ImageUtils.toBufferedImage(byteImage);
        Integer rgbColor = TileUtils.getColorAtPosition(latLng, zoomLevel, bufferedImage);
        valueAtLatLong = getAMSLPopulationRGBValue(accFactor, rgbColor);
      } else {
        logger.error("Image is null for Lat {},Lng {}, table {}", latLng.getLatitude(),
            latLng.getLongitude(), tableName);
      }
    } catch (Exception exception) {
      logger.error("Exception inside getPopulationnAndHeight due to {} ",
          ExceptionUtils.getStackTrace(exception));
    }
    logger.info("value at latlong {}", valueAtLatLong);
    return checkeValueInRange(maxRange, minRange, valueAtLatLong);
  }

  private Object checkeValueInRange(Integer maxRange, Integer minRange, Double valueAtLatLong) {
    if (Utils.hasValue(minRange) && Utils.hasValue(maxRange)) {
      if (valueAtLatLong >= minRange && valueAtLatLong <= maxRange) {
        return valueAtLatLong;
      } else {
        return GenericMapUtils.DEFAULT_AMSL_POPULATION_STRING_VALUE;
      }
    } else {
      return valueAtLatLong;
    }
  }

  /**
   * Gets the AMSL population RGB value.
   *
   * @param accuracyFactor the accuracy factor
   * @param rgbInt the rgb int
   * @return the AMSL population RGB value
   */
  private Double getAMSLPopulationRGBValue(Double accuracyFactor, Integer rgbInt) {
    Color color = new Color(rgbInt);
    int[] rgb = {color.getRed(), color.getGreen(), color.getBlue()};
    int numberFromRGB = ColorUtils.getNumberFromRGB(rgb);
    return numberFromRGB / accuracyFactor;
  }

  /**
   * Updates the Population And the AMSL Color
   *
   * @param tileImage
   * @param tableName
   * @param accuracyFactor
   * @return Colored Image
   */
  @Override
  public BufferedImage updatePopulationAndAMSLColor(BufferedImage tileImage, String tableName,
      Double accuracyFactor) {
    accuracyFactor = GenericMapUtils.checkAccuracyFactor(accuracyFactor);
    logger.info("Inside updatePopulationAndAMSLColor for tableName {}, accuracyFactor {}",
        tableName, accuracyFactor);
    BufferedImage newTileImage = null;
    try {
      if (tileImage != null && tableName != null) {
        List<Map<String, Integer>> colorRangeList = checkColorRangeListType(tableName);
        int width = tileImage.getWidth();
        int height = tileImage.getHeight();
        newTileImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < width; i++) {
          for (int j = 0; j < height; j++) {
            int rgb = tileImage.getRGB(i, j);
            if (rgb != 0) {
              newTileImage.setRGB(i, j,
                  getColorFromRGBWithInRange(accuracyFactor, colorRangeList, rgb));
            }
          }
        }
      }
      return newTileImage;
    } catch (Exception exception) {
      logger.error("Exception inside updatePopulationAndAMSLColor due to {}",
          ExceptionUtils.getStackTrace(exception));
    }
    return newTileImage;
  }

  /**
   * Gets the color from RGB with in range.
   *
   * @param accuracyFactor the accuracy factor
   * @param colorRangeList the color range list
   * @param rgb the rgb
   * @return the color from RGB with in range
   */
  private Integer getColorFromRGBWithInRange(Double accuracyFactor,
      List<Map<String, Integer>> colorRangeList, int rgb) {
    Double valueAtRGB = getAMSLPopulationRGBValue(accuracyFactor, rgb);
    return colorRangeList.stream()
        .filter(colorRange -> valueAtRGB >= colorRange.get(ForesightConstants.MIN_COLOR_RANGE)
            && valueAtRGB <= colorRange.get(ForesightConstants.MAX_COLOR_RANGE))
        .collect(Collectors.toList()).get(0).get(ForesightConstants.PIXEL_COLOR);
  }

  /**
   * Check color range list type.
   *
   * @param tableName the table name
   * @return the Map of Color and Range
   */
  private List<Map<String, Integer>> checkColorRangeListType(String tableName) {
    List<Map<String, Integer>> colorRangeList = new ArrayList<>();
    if (tableName.equalsIgnoreCase(ForesightConstants.AMSL_TABLE)) {
      if (CollectionUtils.isEmpty(amslColorRangeList)) {
        amslColorRangeList = getColorRangeMapList(ForesightConstants.AMSL_LEGENDS_KEY);
        colorRangeList = amslColorRangeList;
      } else {
        colorRangeList = amslColorRangeList;
      }
    } else if (tableName.equalsIgnoreCase(ForesightConstants.FACEBOOK_POPULATION_TABLE)) {
      if (CollectionUtils.isEmpty(populationColorRangeList)) {
        populationColorRangeList = getColorRangeMapList(ForesightConstants.FACEBOOK_LEGENDS_KEY);
        colorRangeList = populationColorRangeList;
      } else {
        colorRangeList = populationColorRangeList;
      }
    }
    return colorRangeList;
  }

  /**
   * Gets the color range map list.
   *
   * @param type the type
   * @return the color range map list
   */
  public List<Map<String, Integer>> getColorRangeMapList(String type) {
    List<Map<String, Integer>> colorRangeMapList = null;
    String colorRangeList = systemconfigurationdao.getValueByName(type);
    if (StringUtils.isEmpty(colorRangeList)) {
      throw new RestException(ForesightConstants.SYSTEM_CONFIGURATION_KEY_MISSING_MESSAGE + type);
    }
    try {
      ObjectMapper mapper = new ObjectMapper();
      colorRangeMapList =
          mapper.readValue(colorRangeList, new TypeReference<List<Map<String, Integer>>>() {});
    } catch (Exception exception) {
      logger.error("Exception inside getColorRangeMapList {}",
          ExceptionUtils.getStackTrace(exception));
    }
    return colorRangeMapList;
  }

  @Override
  public List<List<String>> getPointData(List<String> columnList, GeographyWrapper geographyWrapper)
      throws IOException {
    String indexDir =
        getIndexDirPoint(geographyWrapper.getSubPath(), geographyWrapper.getTableName());
    logger.info("inside getPointData index directory {}", indexDir);
    if (CollectionUtils.isEmpty(columnList)) {
      columnList = GenericMapUtils.getColumnListForQuery();
    }
    List<String> selectColumnList = new ArrayList<>(columnList);
    List<HBaseResult> results = getBoundaryData(geographyWrapper.getnELat(),
        geographyWrapper.getnELng(), geographyWrapper.getsWLat(), geographyWrapper.getsWLng(),
        geographyWrapper.getTableName(), selectColumnList, indexDir);
    logger.info("inside getPointData Result size {}", results.size());
    return convertResultToColumnData(results, columnList);
  }

  @Override
  public String getBoundaryData(List<String> columnList, String tableName, Double nELat,
      Double nELng, Double sWLat, Double sWLng, Boolean isExact, String subPath, String area) {
    try {
      String columns = new Gson().toJson(columnList);
      StringEntity entity = new StringEntity(columns, ContentType.APPLICATION_JSON);
      List<String> valueList = new ArrayList<>();
      valueList.add(tableName);
      valueList.add(String.valueOf(nELat));
      valueList.add(String.valueOf(nELng));
      valueList.add(String.valueOf(sWLat));
      valueList.add(String.valueOf(sWLng));
      valueList.add(String.valueOf(isExact));
      valueList.add(subPath);
      valueList.add(area);

      String query = GenericMapUtils.createGenericQuery(valueList, GenericMapUtils.TABLENAME,
          GenericMapUtils.NELAT, GenericMapUtils.NELONG, GenericMapUtils.SWLAT,
          GenericMapUtils.SWLONG, GenericMapUtils.ISEXACT, GenericMapUtils.SUBPATH,
          GenericMapUtils.AREA);

      logger.info(
          "request parameters for service [{}], [{}], [{}] , [{}], [{}], [{}],[{}],[{}],[{}]",
          valueList, query, nELat, nELng, sWLat, sWLng, isExact, subPath, area);
      String restCall = GenericMapUtils.DROPWIZARD_GENERICBOUNDARY_URL;
      return new HttpPostRequest(getDropwizardUrl(query, restCall), entity).getString();
    } catch (HttpException e) {
      logger.error("Error inside getBoundaryData while getting data from rest service {} ",
          ExceptionUtils.getStackTrace(e));
    }
    return Symbol.EMPTY_STRING;
  }

  @Override
  public List<List<String>> getBoundaryDataForTable(String tableName, List<String> columnList,
      Double nWLat, Double nWLng, Double sELat, Double sELng, Boolean isExact, String subPath,
      Double area) throws IOException {


    String areaFilterPath = Symbol.EMPTY_STRING;
    if (area != null && isAreaIndexingAvailable != null && isAreaIndexingAvailable) {
      if ((StringUtils.equalsIgnoreCase(tableName, GenericMapUtils.SALES_GEOGRAPHY_TABLE)
          || StringUtils.equalsIgnoreCase(tableName, GenericMapUtils.GEOGRAPHY_TABLE_NAME))) {

        if ((subPath.toUpperCase().contains(GenericMapUtils.L1_TYPE)
            || subPath.toUpperCase().contains(GenericMapUtils.L2_TYPE))) {
          areaFilterPath = "10" + Symbol.SLASH_FORWARD_STRING;
          area = null;
        }

      }
    } else {
      try {
        String value = systemconfigurationdao.getValueByName(IS_AREA_INDEXING_AVAILABLE);
        isAreaIndexingAvailable = Boolean.valueOf(value);
      } catch (Exception e) {
        logger.error("System Configuration key is not available {}", IS_AREA_INDEXING_AVAILABLE);
      }
    }

    String indexDir = getIndexDir(isExact, subPath, tableName, areaFilterPath);
    logger.info("index directory {}", indexDir);

    if (CollectionUtils.isEmpty(columnList)) {
      columnList = GenericMapUtils.getColumnListForQuery();
    }

    List<String> selectColumnList = new ArrayList<>(columnList);
    boolean isAreaFilterRequired = false;
    if (area != null && area > 0) {
      selectColumnList.add(GenericMapUtils.GEO_COL_AREA);
      isAreaFilterRequired = true;
    }

    List<HBaseResult> results =
        getBoundaryData(nWLat, nWLng, sELat, sELng, tableName, selectColumnList, indexDir);
    logger.info("Result size {}", results.size());
    if (isAreaFilterRequired)
      results = filterHbaseResultForArea(results, area);
    return convertResultToColumnData(results, columnList);
  }

  @Override
  public byte[] getImageByteArray(String tableName, String tileId, String imgColumn, String type,
      Boolean isBigTile, Integer baseZoom) {
    logger.info("Inside getImageByteArray tableName {},tileId {},imgColumn {},type {} isBigTile {}",
        tableName, tileId, imgColumn, type, isBigTile);
    try {
      Tile tile = new Tile(tileId);
      int zoom = tile.getTz();
      Map<String, Integer> tileZoneMap = getZoneAndTileIdInMap();
      if (isBigTile != null && isBigTile) {
        return getBigTileByteArray(tableName, imgColumn, type, tile, zoom, tileZoneMap);
      } else {
        if (zoom > baseZoom) {
          Tile parentTile = TileRelationship.getParentTile(tile, baseZoom);
          String parentRowKey = RowKeyUtils.getRowKeyForTileTable(parentTile, type, tileZoneMap,
              GenericMapUtils.TILE_ZONE_MAP_ZOOM);
          return ImageUtils.toBytes(
              getChildDataFromParent(tableName, tile, imgColumn, parentTile, parentRowKey));
        } else {
          String rowKey = RowKeyUtils.getRowKeyForTileTable(tile, type, tileZoneMap,
              GenericMapUtils.TILE_ZONE_MAP_ZOOM);
          return getDataForTable(tableName, rowKey, imgColumn);
        }
      }
    } catch (IOException exception) {
      logger.error("Error in getting getImageByteArray due to {}",
          ExceptionUtils.getStackTrace(exception));
    }
    throw new RestException(ForesightConstants.EXCEPTION_NO_RECORD_FOUND);
  }

  public static int getProjectNumber(String kpi, String siteStatus) {
    if (kpi.equalsIgnoreCase(SINR) && siteStatus.equalsIgnoreCase(PLANNED)) {
      return ForesightConstants.ONE;
    } else if (kpi.equalsIgnoreCase(RSRP) && siteStatus.equalsIgnoreCase(PLANNED)) {
      return ForesightConstants.TWO;
    } else if (kpi.equalsIgnoreCase(RSRQ) && siteStatus.equalsIgnoreCase(PLANNED)) {
      return ForesightConstants.THREE;
    } else if (kpi.equalsIgnoreCase(BSP) && siteStatus.equalsIgnoreCase(PLANNED)) {
      return ForesightConstants.FOUR;
    } else if (kpi.equalsIgnoreCase(SINR) && siteStatus.equalsIgnoreCase(ONAIR)) {
      return ForesightConstants.FIVE;
    } else if (kpi.equalsIgnoreCase(RSRP) && siteStatus.equalsIgnoreCase(ONAIR)) {
      return ForesightConstants.SIX;
    } else if (kpi.equalsIgnoreCase(RSRQ) && siteStatus.equalsIgnoreCase(ONAIR)) {
      return ForesightConstants.SEVEN;
    } else if (kpi.equalsIgnoreCase(BSP) && siteStatus.equalsIgnoreCase(ONAIR)) {
      return ForesightConstants.EIGHT;
    } else if (kpi.equalsIgnoreCase(DL) && siteStatus.equalsIgnoreCase(ONAIR)) {
      return ForesightConstants.NINE;
    } else if (kpi.equalsIgnoreCase(UL) && siteStatus.equalsIgnoreCase(ONAIR)) {
      return ForesightConstants.TEN;
    } else if (kpi.equalsIgnoreCase(DL) && siteStatus.equalsIgnoreCase(PLANNED)) {
      return ForesightConstants.ELEVEN;
    } else if (kpi.equalsIgnoreCase(UL) && siteStatus.equalsIgnoreCase(PLANNED)) {
      return ForesightConstants.TWELVE;
    } else {
      return ForesightConstants.ZERO;
    }
  }

  @Override
  public List<Map<String, String>> getBoundaryByPk(GeographyWrapper geoWrapper, String tableName,
      String area, String type) {
    try {
      List<String> valueList = new ArrayList<>();
      valueList.add(tableName);
      valueList.add(area);
      valueList.add(type);
      String query = GenericMapUtils.createGenericQuery(valueList, GenericMapUtils.TABLENAME,
          GenericMapUtils.AREA, "type");
      logger.info("request parameter for Geographydata [{}] , [{}]", query, valueList);
      String restCall = GenericMapUtils.DROPWIZARD_GENERICBOUNDARYBYPK_URL;
      String url = getDropwizardUrl(query, restCall);
      String columns = new Gson().toJson(geoWrapper);
      return getHttpPostResponse(url, columns);
    } catch (Exception e) {
      logger.error("Error inside getBoundaryByPk method {}", ExceptionUtils.getStackTrace(e));
    }
    throw new RestException(GenericMapUtils.ERROR_MSG_FETCHING_DATA);
  }

  @Override
  public List<Map<String, String>> getBoundaryDataByRowkeyPrefix(GeographyWrapper geoWrapper,
      String tableName, String area) {
    try {
      List<String> valueList = new ArrayList<>();
      valueList.add(tableName);
      valueList.add(area);
      String query = GenericMapUtils.createGenericQuery(valueList, GenericMapUtils.TABLENAME,
          GenericMapUtils.AREA, "type");
      logger.info("request parameter for Geographydata [{}] , [{}]", query, valueList);
      String restCall = GenericMapUtils.MICROSERVICE_GET_BOUNDARYDATA_BY_ROWKEYPREFIX_URL;
      String url = getDropwizardUrl(query, restCall);
      String columns = new Gson().toJson(geoWrapper);
      return getHttpPostResponse(url, columns);
    } catch (Exception e) {
      logger.error("Error inside getBoundaryByPk method {}", ExceptionUtils.getStackTrace(e));
    }
    throw new RestException(GenericMapUtils.ERROR_MSG_FETCHING_DATA);
  }

  @Override
  public List<Map<String, String>> getBoundaryDataByRowkeyPrefix(List<String> rowKeyPrefix,
      String tableName, List<String> columnList, Double area) {
    List<Map<String, String>> geographyDataWrapperLIst = new ArrayList<>();
    try {
      if (CollectionUtils.isEmpty(columnList)) {
        columnList = GenericMapUtils.getColumnListForQuery();
      }
      List<String> selectColumnList = new ArrayList<>(columnList);
      boolean isAreaFilterRequired = false;
      if (area != null && area > 0) {
        selectColumnList.add(GenericMapUtils.GEO_COL_AREA);
        isAreaFilterRequired = true;
      }
      List<HBaseResult> resultList = genericMapServiceDao
          .getResultForBoundaryDataByRowkeyPrefixes(rowKeyPrefix, selectColumnList, tableName);
      logger.info("result size {}", resultList.size());
      if (isAreaFilterRequired)
        resultList = filterHbaseResultForArea(resultList, area);
      geographyDataWrapperLIst = convertResultToMap(resultList, columnList);
      logger.info("geographyDataWrapperLIst{}", geographyDataWrapperLIst.size());
    } catch (Exception e) {
      logger.error("Exception inside getBoundaryDataByRowkeyPrefix due to {}",
          ExceptionUtils.getStackTrace(e));
    }
    return geographyDataWrapperLIst;
  }

  @Override
  public List<Map<String, String>> getBoundaryDataByPKMS(List<Integer> geoGraphyPkList,
      String tableName, List<String> columnList, Double area, String type) {
    List<String> rowKeyPrefix = getRowkeyPrefixListByType(type, geoGraphyPkList);
    return getBoundaryDataByRowkeyPrefix(rowKeyPrefix, tableName, columnList, area);
  }

  private List<String> getRowkeyPrefixListByType(String type, List<Integer> pkList) {
    List<String> rowKeyList = new ArrayList<>();
    if (type.equalsIgnoreCase(GenericMapUtils.GEOGRAPHY_L1)) {
      geographyL1Dao.getGeographyL1ListByIds(pkList).stream()
          .forEach(x -> rowKeyList.add(x.getRowkeyprefix()));
    } else if (type.equalsIgnoreCase(GenericMapUtils.GEOGRAPHY_L2)) {
      geographyL2Dao.getGeographyL2ListByIds(pkList).stream()
          .forEach(x -> rowKeyList.add(x.getRowkeyprefix()));
    } else if (type.equalsIgnoreCase(GenericMapUtils.GEOGRAPHY_L3)) {
      geographyL3Dao.getGeographyL3ListByIds(pkList).stream()
          .forEach(x -> rowKeyList.add(x.getRowkeyprefix()));
    } else if (type.equalsIgnoreCase(GenericMapUtils.GEOGRAPHY_L4)) {
      List<GeographyL4> listByIds = geographyL4Dao.getGeographyL4ListByIds(pkList);
      if (CollectionUtils.isNotEmpty(listByIds))
        listByIds.stream().forEach(x -> rowKeyList.add(x.getRowkeyprefix()));
    } else {
      otherGeographyDao.getOtherGeographyListByIds(pkList).stream()
          .forEach(x -> rowKeyList.add(x.getRowKeyPrefix()));
    }
    return rowKeyList;
  }

  @Override
  public Map<String, List<Tile>> getCommonTiles(Corner viewPort, List<Integer> geoGraphyPkList,
      String geoLevel, int zoomLevel) {
    if (viewPort.isValid() && CollectionUtils.isNotEmpty(geoGraphyPkList)
        && StringUtils.isNotEmpty(geoLevel)) {
      GeographyWrapper geoWrapper = new GeographyWrapper();
      geoWrapper.setGeographyPkList(geoGraphyPkList);
      geoWrapper.setBoundaryColumnList(Arrays.asList(GenericMapUtils.GEO_COL_COORDINATES));
      List<Map<String, String>> boundaryByPk =
          getBoundaryByPk(geoWrapper, GenericMapUtils.GEOGRAPHY_TABLE_NAME, null, geoLevel);
      List<List<List<List<Double>>>> multiBoundaryWithHole = new ArrayList<>();
      boundaryByPk.stream().forEach(response -> multiBoundaryWithHole
          .add(GenericMapUtils.getBoundary(response.get(GenericMapUtils.COORDINATES))));
      return TileBoundaryUtils.getTiles(viewPort, multiBoundaryWithHole, zoomLevel);
    } else {
      logger.error(ForesightConstants.INVALID_PARAMETERS);
    }
    return new HashMap<>();
  }

  @Override
  public byte[] exportKML(String fileName, List<Map<String, Object>> placemarkList) {
    return KMLGenerator.generateKML(fileName, placemarkList).toByteArray();
  }

  @Override
  public List<Map<String, Object>> importKML(HttpServletRequest request) {
    try {
      List<FileItem> listOfFileItems = Utils.getListOfFileItems(request);
      logger.info("Inside importKML fileItems list size {}", listOfFileItems.size());
      return KMLParser.parse(listOfFileItems.get(0).getInputStream());
    } catch (FileUploadException | IOException e) {
      logger.error("Exception inside importKML due to {}", ExceptionUtils.getStackTrace(e));
    }
    throw new RestException("Unable to parse kml file");
  }

  @Override
  public String getAllSalesGeographyNameByPolygon(List<List<Double>> polygon2d) {
    try {
      String url = ConfigUtils.getString(ConfigEnum.MICRO_SERVICE_BASE_URL)
          + GenericMapUtils.DROPWIZARD_ALL_SALES_GEOGRAPHY_BY_POLYGON;
      String columns = new Gson().toJson(polygon2d);
      StringEntity entity = new StringEntity(columns, ContentType.APPLICATION_JSON);
      logger.info("url {}", url);
      HttpRequest http = new HttpPostRequest(url, entity);
      return http.getString();
    } catch (HttpException e) {
      logger.error("Error inside getBoundaryData while getting data from rest service {} ",
          ExceptionUtils.getStackTrace(e));
    }
    throw new RestException("Error while getting the polygon data");
  }

  @Override
  public Map<String, Set<Object>> getAllNWGeographyNameByPolygon(List<List<Double>> polygon2d) {
    try {
      String url = ConfigUtils.getString(ConfigEnum.MICRO_SERVICE_BASE_URL)
          + GenericMapUtils.DROPWIZARD_ALL_NW_GEOGRAPHY_BY_POLYGON;
      String columns = new Gson().toJson(polygon2d);
      StringEntity entity = new StringEntity(columns, ContentType.APPLICATION_JSON);
      logger.info("url {}", url);
      HttpRequest http = new HttpPostRequest(url, entity);
      return new Gson().fromJson(http.getString(),
          new TypeToken<Map<String, Set<Object>>>() {}.getType());
    } catch (HttpException e) {
      logger.error("Exception inside getAllNWGeographyNameByPolygon , due to {} ",
          ExceptionUtils.getStackTrace(e));
    }
    throw new RestException(ForesightConstants.UNABLE_TO_PROCESS_REQUEST);
  }

  @Override
  public Double getAccuracyFactor(int zoom, String tableName) {
    if (tableName.equalsIgnoreCase(ForesightConstants.AMSL_TABLE)) {
      if (!DoubleUtils.isValid(amslAccuracyFactor)) {
        amslAccuracyFactor = Double.parseDouble(
            systemconfigurationdao.getValueByName(GenericMapUtils.AMSL_ACCURACY_FACTOR));
      }
      return amslAccuracyFactor;
    } else if (tableName.equalsIgnoreCase(ForesightConstants.FACEBOOK_POPULATION_TABLE)) {

      if (MapUtils.isEmpty(populationAccuracyFactorMap)) {
        populationAccuracyFactorMap = new Gson().fromJson(
            systemconfigurationdao
                .getValueByName(GenericMapUtils.POPULATION_ACCURACY_FACTOR_ZOOM_WISE),
            new TypeToken<Map<Integer, Double>>() {}.getType());
      }
      return populationAccuracyFactorMap.get(zoom);
    }
    return GenericMapUtils.DEFAULT_ACCURACY_FACTOR;
  }

  @Override
  public LatLng getCentroidForUserInContext() {
    UserGeographyWrapper filterGeography = customerInfo.getFilterGeography();
    Map<String, Double> geography = getGeographyByLevel(filterGeography.getLevelType(),
        filterGeography.getGeographyMap().keySet().toArray(new Integer[1])[0]);
    return new LatLng(geography.get("lat"), geography.get("lng"));
  }

  public Map<String, Double> getGeographyByLevel(String levelType, Integer id) {
    Map<String, Double> geoMap = new HashMap<>();
    if (levelType.equals(UmConstants.L1)) {
      GeographyL1 geoL1 = geographyL1Dao.findByPk(id);
      geoMap.put("lat", geoL1.getLatitude());
      geoMap.put("lng", geoL1.getLongitude());
    } else if (levelType.equals(UmConstants.L2)) {
      GeographyL2 geoL2 = geographyL2Dao.findByPk(id);
      geoMap.put("lat", geoL2.getLatitude());
      geoMap.put("lng", geoL2.getLongitude());
    } else if (levelType.equals(UmConstants.L3)) {
      GeographyL3 geoL3 = geographyL3Dao.findByPk(id);
      geoMap.put("lat", geoL3.getLatitude());
      geoMap.put("lng", geoL3.getLongitude());
    } else if (levelType.equals(UmConstants.L4)) {
      GeographyL4 geoL4 = geographyL4Dao.findByPk(id);
      geoMap.put("lat", geoL4.getLatitude());
      geoMap.put("lng", geoL4.getLongitude());
    } else if (levelType.equals(UmConstants.SALES_L1)) {
      SalesL1 salesL1 = salesL1Dao.findByPk(id);
      geoMap.put("lat", salesL1.getLatitude());
      geoMap.put("lng", salesL1.getLongitude());
    } else if (levelType.equals(UmConstants.SALES_L2)) {
      SalesL2 salesL2 = salesL2Dao.findByPk(id);
      geoMap.put("lat", salesL2.getLatitude());
      geoMap.put("lng", salesL2.getLongitude());
    } else if (levelType.equals(UmConstants.SALES_L3)) {
      SalesL3 salesL3 = salesL3Dao.findByPk(id);
      geoMap.put("lat", salesL3.getLatitude());
      geoMap.put("lng", salesL3.getLongitude());
    } else if (levelType.equals(UmConstants.SALES_L4)) {
      SalesL4 salesL4 = salesL4Dao.findByPk(id);
      geoMap.put("lat", salesL4.getLatitude());
      geoMap.put("lng", salesL4.getLongitude());
    } else {
      geoMap.put("lat", Double
          .parseDouble(systemconfigurationdao.getValueByName(ForesightConstants.COUNTRY_LATITUDE)));
      geoMap.put("lng", Double.parseDouble(
          systemconfigurationdao.getValueByName(ForesightConstants.COUNTRY_LONGITUDE)));
    }
    return geoMap;
  }

  /**
   * getFbPopulationForKpiSummary
   *
   * @param kpiSummaryDataWrapper
   * @param northEastLat
   * @param northEastLong
   * @param southWestLat
   * @param southWestLong
   * @param geographyType
   * @param zoomLevel
   * @return
   */
  @Override
  public Map<String, Map<String, Object>> getFbPopulationForKpiSummary(
      KPISummaryDataWrapper kpiSummaryDataWrapper, Double northEastLat, Double northEastLong,
      Double southWestLat, Double southWestLong, String geographyType, Integer zoomLevel) {
    logger.debug("Inside getPopulationForKpiSummary");
    try {
      String url = ConfigUtils.getString(ConfigEnum.MICRO_SERVICE_BASE_URL.getValue())
          + GenericMapUtils.DROPWIZARD_FB_POPULATION_FOR_KPI_SUMMARY;
      url = url + ForesightConstants.QUESTIONMARK + ForesightConstants.NELAT
          + ForesightConstants.EQUALS + northEastLat + ForesightConstants.AMPERSAND
          + ForesightConstants.NELON + ForesightConstants.EQUALS + northEastLong
          + ForesightConstants.AMPERSAND + ForesightConstants.SWLAT + ForesightConstants.EQUALS
          + southWestLat + ForesightConstants.AMPERSAND + ForesightConstants.SWLON
          + ForesightConstants.EQUALS + southWestLong + ForesightConstants.AMPERSAND
          + ForesightConstants.GEOGRAPHYTYPE + ForesightConstants.EQUALS + geographyType
          + ForesightConstants.AMPERSAND + ForesightConstants.ZOOM_LEVEL + ForesightConstants.EQUALS
          + zoomLevel;

      String wrapper = new Gson().toJson(kpiSummaryDataWrapper);
      StringEntity entity = new StringEntity(wrapper, ContentType.APPLICATION_JSON);
      String response = new HttpPostRequest(url, entity).getString();
      Map<String, Map<String, Object>> responseJson = new HashMap<>();
      Map<String, Object> colorValueMap = new HashMap<>();
      colorValueMap.put(ForesightConstants.COLOR_LOWER, null);
      if (StringUtils.isNotEmpty(response)) {
        colorValueMap.put(ForesightConstants.VALUE_LOWER, new BigDecimal(response).longValue());
      } else {
        colorValueMap.put(ForesightConstants.VALUE_LOWER, null);
      }
      responseJson.put(ForesightConstants.AVERAGE_LOWER, colorValueMap);
      return responseJson;
    } catch (Exception exception) {
      logger.error("Error in Getting Data from Rest Service getPopulationForKpiSummary {}",
          ExceptionUtils.getStackTrace(exception));
    }
    throw new RestException(ForesightConstants.ERROR_RESPONSE);
  }

  /**
   * getFbPopulationForKpiSummaryMS
   *
   * @param kpiSummaryDataWrapper
   * @param northEastLat
   * @param northEastLong
   * @param southWestLat
   * @param southWestLong
   * @param geographyType
   * @param zoomLevel
   * @return
   */
  @Override
  public Double getFbPopulationForKpiSummaryMS(KPISummaryDataWrapper kpiSummaryDataWrapper,
      Double northEastLat, Double northEastLong, Double southWestLat, Double southWestLong,
      String geographyType, Integer zoomLevel) {
    logger.debug("Inside getFbPopulationForKpiSummary");
    Corner corner = new Corner(southWestLat, southWestLong, northEastLat, northEastLong);
    List<List<List<Double>>> polyList = null;
    List<String> geographyNameList = null;
    Double facebookPopulation = 0.0;
    zoomLevel = zoomLevel > ForesightConstants.ZOOM_LEVEL_FIFTEEN
        ? ForesightConstants.ZOOM_LEVEL_FIFTEEN : zoomLevel;
    if (Utils.hasValue(kpiSummaryDataWrapper)) {
      polyList = kpiSummaryDataWrapper.getPolyList();
      geographyNameList = kpiSummaryDataWrapper.getGeographyList();
    }
    if (CollectionUtils.isNotEmpty(polyList)) {
      zoomLevel = zoomLevel < ForesightConstants.ZOOM_LEVEL_TEN ? ForesightConstants.ZOOM_LEVEL_TEN
          : zoomLevel;
      for (List<List<Double>> polygon : polyList) {
        facebookPopulation += getFacebookPopulation(polygon, zoomLevel);
      }
    } else if (StringUtils.isNotEmpty(geographyType)
        && CollectionUtils.isNotEmpty(geographyNameList)) {
      List<String> columnList = Arrays.asList(
          ForesightConstants.HBASE_COLUMN_FAMILY + ForesightConstants.COLON
              + ForesightConstants.FACEBOOK_POPULATION_COLUMN,
          ForesightConstants.HBASE_COLUMN_FAMILY + ForesightConstants.COLON
              + ForesightConstants.GEOGRAPHY_NAME_COLUMN);
      List<Map<String, String>> geoPopulationList =
          getBoundaryDataByGeographyNamesMS(geographyNameList,
              boundaryDataDao.getTableName(geographyType), columnList, null, geographyType);
      for (Map<String, String> geoPopulation : geoPopulationList) {
        facebookPopulation +=
            Double.valueOf(geoPopulation.get(ForesightConstants.FACEBOOK_POPULATION_COLUMN));
      }

    } else if (corner.isValid() && Utils.hasValue(zoomLevel)) {
      if (zoomLevel >= ForesightConstants.ZOOM_LEVEL_TEN) {
        facebookPopulation = getFacebookPopulation(corner.getBoundary(), zoomLevel);
      } else {
        return null;
      }
    }
    return facebookPopulation;
  }

  /**
   * Gets the all sales geography name.
   *
   * @param location the location
   * @return the all sales geography name
   */
  @Override
  public String[] getAllSalesGeographyName(LatLng location) {
    String[] geographyNames = new String[ForesightConstants.INDEX_FOUR];
    try {
      geographyNames[ForesightConstants.INDEX_ZERO] = boundaryDataDao.findSalesL1(location);
      geographyNames[ForesightConstants.INDEX_ONE] = boundaryDataDao.findSalesL2(location);
      geographyNames[ForesightConstants.INDEX_TWO] = boundaryDataDao.findSalesL3(location);
      geographyNames[ForesightConstants.INDEX_THREE] = boundaryDataDao.findSalesL4(location);
      logger.info("Returning geographyNames {} for location {}", Arrays.toString(geographyNames),
          location);
    } catch (Exception exception) {
      logger.error("Exception inside getAllSalesGeographyName due to {}",
          ExceptionUtils.getStackTrace(exception));
    }
    return geographyNames;
  }

  @Override
  public String getAllSalesGeographyName(Double latitude, Double longitude) {
    logger.info("Inside getAllSalesGeographyName latitude {}, longitude {}", latitude, longitude);
    if (latitude != null && longitude != null) {
      List<String> valueList = new ArrayList<>();
      valueList.add(String.valueOf(latitude));
      valueList.add(String.valueOf(longitude));
      String query =
          GenericMapUtils.createGenericQuery(valueList, GenericMapUtils.LAT, GenericMapUtils.LNG);
      try {
        return new HttpGetRequest(
            getDropwizardUrl(query, GenericMapUtils.DOPWIZARD_GET_ALL_SALES_GEOGRAPHY_NAME_URL))
                .getString();
      } catch (HttpException e) {
        logger.error("Unable to getAllSalesGeographyName from Micro Service due to {}",
            ExceptionUtils.getStackTrace(e));
      }
    } else {
      logger.error(ForesightConstants.INVALID_PARAMETER);
    }
    return null;
  }

  @Override
  public Map<String, List<String>> getNonComplianceParameterOfSite(String rowKey, String tableName,
      String column) {
    logger.info("inside getNonComplianceParameterOfSite rowkey : {},tablename : {}  ", rowKey,
        tableName);
    List<String> columnList = new ArrayList<>();
    try {
      columnList.add(column);
      List<HBaseResult> resultList =
          genericMapServiceDao.getResultDataByRowkeyPrefixes(rowKey, columnList, tableName);
      if (resultList != null) {
        return getMapDataFromColumn(resultList, columnList);
      } else {
        return null;
      }
    } catch (Exception e) {
      logger.error("Execption : {} ", Utils.getStackTrace(e));
    }
    return null;
  }

  private Map<String, List<String>> getMapDataFromColumn(List<HBaseResult> resultList,
      List<String> columnList) {
    logger.info("inside getMapDataFromColumn rowkey : {},columnName : {}  ", resultList.size(),
        columnList);
    Map<String, List<String>> finalResult = new HashMap<>();
    for (HBaseResult result : resultList) {
      List<String> resultData = new ArrayList<>();
      for (String clms : columnList) {
        String value = result.getString((String) clms.split(Symbol.COLON_STRING)[1]);
        if (value == null) {
          value = Symbol.EMPTY_STRING;
        }
        resultData.add(value);
      }
      finalResult.put(result.getRowKey(), resultData);
    }
    logger.info("Data : {} ", finalResult.size());
    return finalResult;

  }

  @Override
  public String findGeographyL1(Double lat, Double lng) {
    if (new LatLng(lat, lng).isValid()) {
      String query = getGeographyQueryParam(lat, lng);
      String restCall = GenericMapUtils.MICROSERVICE_FINDGEOGRAPHY_L1_URL;
      return new HttpGetRequest(getDropwizardUrl(query, restCall)).toString();
    }
    throw new RestException(ForesightConstants.INVALID_PARAMETER);
  }

  private String getGeographyQueryParam(Double lat, Double lng) {
    List<String> valueList = new ArrayList<>();
    valueList.add(String.valueOf(lat));
    valueList.add(String.valueOf(lng));
    return GenericMapUtils.createGenericQuery(valueList, GenericMapUtils.LAT, GenericMapUtils.LNG);
  }

  @Override
  public String findGeographyL2(Double lat, Double lng) {
    if (new LatLng(lat, lng).isValid()) {
      String query = getGeographyQueryParam(lat, lng);
      String restCall = GenericMapUtils.MICROSERVICE_FINDGEOGRAPHY_L2_URL;
      return new HttpGetRequest(getDropwizardUrl(query, restCall)).toString();
    }
    throw new RestException(ForesightConstants.INVALID_PARAMETER);
  }

  @Override
  public String findGeographyL3(Double lat, Double lng) {
    if (new LatLng(lat, lng).isValid()) {
      String query = getGeographyQueryParam(lat, lng);
      String restCall = GenericMapUtils.MICROSERVICE_FINDGEOGRAPHY_L3_URL;
      return new HttpGetRequest(getDropwizardUrl(query, restCall)).toString();
    }
    throw new RestException(ForesightConstants.INVALID_PARAMETER);
  }

  @Override
  public String findGeographyL4(Double lat, Double lng) {
    if (new LatLng(lat, lng).isValid()) {
      String query = getGeographyQueryParam(lat, lng);
      String restCall = GenericMapUtils.MICROSERVICE_FINDGEOGRAPHY_L4_URL;
      return new HttpGetRequest(getDropwizardUrl(query, restCall)).toString();
    }
    throw new RestException(ForesightConstants.INVALID_PARAMETER);
  }

  @Override
  public Map<String, String[]> getGeographyNames(LatLng latLng) {
    Map<String, String[]> geographyMap = new HashMap<>();
    if (latLng.isValid()) {
      String[] nwGeography = getAllGeographyName(latLng);
      geographyMap.put("nw", nwGeography);
      String[] salesGeography = getAllSalesGeographyName(latLng);
      geographyMap.put("sales", salesGeography);
    } else {
      logger.error("Inside getGeographyNames, Invaild latLng {}", latLng);
    }
    return geographyMap;
  }

  public List<List<String>> convertResultToColumnDataRowkeyWise(List<HBaseResult> results,
      List<String> columnList) {
    List<List<String>> data = new ArrayList<>();
    for (HBaseResult result : results) {
      List<String> resultData = new ArrayList<>();
      for (String clms : columnList) {
        String value = result.getString(clms);
        if (value == null) {
          value = Symbol.EMPTY_STRING;
        }
        resultData.add(value);
      }
      data.add(resultData);
    }
    return data;
  }

  @Override
  public List<List<String>> getDataFromHBase(List<String> hbaseColumns, String tableName,
      String rowkeyPrefix) throws IOException {
    Scan scan = new Scan();
    scan.setRowPrefixFilter(Bytes.toBytes(rowkeyPrefix));
    List<HBaseResult> resultList = genericMapServiceDao.getResultListForScan(scan, tableName,
        ForesightConstants.HBASE_COLUMN_FAMILY);
    return convertResultToColumnDataRowkeyWise(resultList, hbaseColumns);
  }

  @Override
  public Scan getScanForSingleRowKeyPrefix(String rowKey, List<String> columns) {
    try {
      Scan scan = new Scan();
      for (String columnName : columns) {
        if (columnName.contains(Symbol.COLON_STRING)) {
          String[] cellValues = columnName.split(Symbol.COLON_STRING);
          scan.addColumn(Bytes.toBytes(cellValues[ForesightConstants.ZERO]),
              Bytes.toBytes(cellValues[ForesightConstants.ONE]));
        }
      }
      scan.setRowPrefixFilter(Bytes.toBytes(rowKey));
      scan.setCacheBlocks(true);
      scan.setCaching(ForesightConstants.CACHE_SIZE);
      return scan;
    } catch (Exception e) {
      logger.error("Exception Inside @Method getScanForRowRange :{}",
          ExceptionUtils.getStackTrace(e));
    }
    return null;
  }

	@Override
	public Map<String,String> getRouteDataMS(GeographyWrapper geoWrapper) {
		Map<String,String> result = new HashMap<>();
		List<Integer> geoGraphyPkList = geoWrapper.getGeographyPkList();
		List<String> columnList = geoWrapper.getBoundaryColumnList();
		String tableName = geoWrapper.getTableName();
		logger.info("tablename {} columnList {} geoGraphyPkList {} ", tableName, columnList, geoGraphyPkList);
		try {
			List<String> colList = getColListForBoundary(columnList);
			if (!colList.isEmpty()) {
				List<List<List<List<Double>>>> boundary = new ArrayList<>();
				getGeographyDetail(result, geoGraphyPkList, columnList, boundary,geoWrapper.getAccuracyFactor());
			}
		} catch (Exception e) {
			logger.error("Exception Inside @method getRouteData : {}", Utils.getStackTrace(e));
		}
		return result;
	}
	
	private void getGeographyDetail(Map<String,String> result, List<Integer> geoGraphyPkList, List<String> columnList,
			List<List<List<List<Double>>>> boundary,Double tolerance) {
		List<Map<String, String>> boundaryDataByPKMS = getBoundaryDataByPKMS(geoGraphyPkList,
				GenericMapUtils.GEOGRAPHY_TABLE_NAME, columnList, 0.0, GenericMapUtils.GEOGRAPHY_L4);
		List<List<List<List<Double>>>> bufferBoundary = new ArrayList<>();
		for (Map<String, String> map : boundaryDataByPKMS) {
			String string = map.get(GenericMapUtils.COORDINATES);
			if (string != null) {
				List<List<List<Double>>> bound = new Gson().fromJson(string, new TypeToken<List<List<List<Double>>>>() {
				}.getType());
				
				boundary.add(bound);
				if(!bound.isEmpty()) {
				   GISGeometry geom = new GIS3DPolygon(bound);
				   bufferBoundary.add(geom.putBuffer(tolerance).getMultipolygonFromGeomerty().get(0));
				}
			}
		}
		getBufferdBoundary(result, boundary, boundaryDataByPKMS, bufferBoundary);
	}

	private void getBufferdBoundary(Map<String, String> result, List<List<List<List<Double>>>> boundary,
			List<Map<String, String>> boundaryDataByPKMS, List<List<List<List<Double>>>> bufferBoundary) {
		if (!boundaryDataByPKMS.isEmpty()) {
			String cent = boundaryDataByPKMS.get(0).get(GenericMapUtils.CENTROID);
			String name = boundaryDataByPKMS.get(0).get(GenericMapUtils.GEOGRAPHY_NAME);
			if(!boundary.isEmpty())
			 result.put(GenericMapUtils.COORDINATES,String.valueOf(boundary));
			if(cent!=null)
			 result.put(GenericMapUtils.CENTROID,cent);
			if(name!=null)
			 result.put(GenericMapUtils.GEOGRAPHY_NAME,name);
			if(!bufferBoundary.isEmpty())
				result.put(GenericMapUtils.BUFFER_COORDINATES,String.valueOf(bufferBoundary));
		}
	}

private List<String> getColListForBoundary(List<String> columnList) {
	List<String> colList = new ArrayList<>();
	if(columnList.contains(GenericMapUtils.GEO_COL_COORDINATES)) {
		colList.add(GenericMapUtils.GEO_COL_COORDINATES);
	}
	if(columnList.contains(GenericMapUtils.GEO_COL_CENTROID)) {
		colList.add(GenericMapUtils.GEO_COL_CENTROID);
	}
	if(columnList.contains(GenericMapUtils.GEO_COL_NAME)){
		colList.add(GenericMapUtils.GEO_COL_NAME);
	}
	return colList;
}

@Override
public Map<String,String> getRouteData(GeographyWrapper geoWrapper) {
  try {
    String restCall = GenericMapUtils.DROPWIZARD_ROUTE_BYPK_URL;
    String url = getDropwizardUrl(null, restCall);
    String columns = new Gson().toJson(geoWrapper);
    StringEntity entity = new StringEntity(columns, ContentType.APPLICATION_JSON);
    HttpRequest http = new HttpPostRequest(url, entity);
    String response = http.getString();
    return new ObjectMapper().readValue(response,
        new TypeReference<Map<String,String>>() {});
  } catch (Exception e) {
    logger.error("Error inside getRouteData method {}", ExceptionUtils.getStackTrace(e));
  }
  throw new RestException(GenericMapUtils.ERROR_MSG_FETCHING_DATA);
}

@Override
public String getAllGeographyDisplayName(Double latitude, Double longitude) {
  logger.info("Inside getAllGeographyDisplayName latitude {}, longitude {}", latitude, longitude);
  if (latitude != null && longitude != null) {
    List<String> valueList = new ArrayList<>();
    valueList.add(String.valueOf(latitude));
    valueList.add(String.valueOf(longitude));
    String query =
        GenericMapUtils.createGenericQuery(valueList, GenericMapUtils.LAT, GenericMapUtils.LON);
    try {
      return new HttpGetRequest(
          getDropwizardUrl(query, GenericMapUtils.DOPWIZARD_GET_ALL_GEOGRAPHY_DISPLAY_NAME_URL))
              .getString();
    } catch (HttpException e) {
      logger.error("Unable to getAllGeographyDisplayName from Micro Service due to {}",
          ExceptionUtils.getStackTrace(e));
    }
  } else {
    logger.error(ForesightConstants.INVALID_PARAMETER);
  }
  return null;
}

@Override
public String[] getAllGeographyDisplayName(LatLng location) {
  String[] geographyDisplayNames = new String[ForesightConstants.INDEX_FOUR];
  try {
    geographyDisplayNames[ForesightConstants.INDEX_ZERO] = getDisplayName(boundaryDataDao.findL1(location));
    geographyDisplayNames[ForesightConstants.INDEX_ONE] =  getDisplayName(boundaryDataDao.findL2(location));
    geographyDisplayNames[ForesightConstants.INDEX_TWO] =  getDisplayName(boundaryDataDao.findL3(location));
    geographyDisplayNames[ForesightConstants.INDEX_THREE] = getDisplayName(boundaryDataDao.findL4(location));
    logger.info("Returning getAllGeographyDisplayName {} for location {}", Arrays.toString(geographyDisplayNames),
        location);
  } catch (Exception exception) {
    logger.error("Exception inside getAllGeographyDisplayName due to {}",
        ExceptionUtils.getStackTrace(exception));
  }
  return geographyDisplayNames;
}
 private String getDisplayName(Geography geogaphy){
   if(geogaphy!=null){
     return geogaphy.getDisplayName();
   }
  return null; 
 }
}

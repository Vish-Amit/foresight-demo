package com.inn.foresight.core.infra.dao.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.NoResultException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.math.DoubleUtils;
import org.apache.hadoop.hbase.CompareOperator;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.inn.commons.Symbol;
import com.inn.commons.hadoop.hbase.HBaseResult;
import com.inn.commons.lang.MapUtils;
import com.inn.commons.maps.LatLng;
import com.inn.commons.maps.geometry.gis.GIS3DPolygon;
import com.inn.commons.maps.geometry.gis.GISGeometry;
import com.inn.commons.maps.nns.NNS;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.utils.Utils;
import com.inn.foresight.core.generic.dao.impl.hbase.AbstractHBaseDao;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.infra.dao.BoundaryDataDao;
import com.inn.foresight.core.infra.wrapper.Geography;
import com.inn.foresight.core.maplayer.utils.GenericMapUtils;
import com.inn.product.systemconfiguration.dao.SystemConfigurationDao;
import com.inn.product.um.geography.dao.GeographyL1Dao;
import com.inn.product.um.geography.dao.GeographyL2Dao;
import com.inn.product.um.geography.dao.GeographyL3Dao;
import com.inn.product.um.geography.dao.GeographyL4Dao;
import com.inn.product.um.geography.model.GeographyL1;
import com.inn.product.um.geography.model.GeographyL2;
import com.inn.product.um.geography.model.GeographyL3;
import com.inn.product.um.geography.model.GeographyL4;

/**
 * The Class BoundaryDataDaoImpl.
 */
@Service("BoundaryDataDaoImpl")
public class BoundaryDataDaoImpl extends AbstractHBaseDao implements BoundaryDataDao {

  /** The logger. */
  private static Logger logger = LogManager.getLogger(BoundaryDataDaoImpl.class);

  /** The geography zoom level mapping. */
  private Map<Integer, String> geographyZoomLevelMapping = new HashMap<>();

  /** The geographies centroid. */
  private static Map<String, NNS<Geography>> geographiesCentroid = new HashMap<>();

  /** The geographies map. */
  private static Map<String, List<Geography>> geographiesMap = new HashMap<>();

  /** The level table name. */
  private Map<String, String> levelTableName = new HashMap<>();

  /** The level wise zoom. */
  private Map<String, Integer> levelWiseZoom = new HashMap<>();

  /** The level wise area. */
  private Map<String, Integer> levelWiseArea = new HashMap<>();

  /** The level TO geo graphy mapping. */
  private Map<String, String> levelTOGeoGraphyMapping = new HashMap<>();

  /** The nw L 1 date. */
  private String nwL1Date = Symbol.EMPTY_STRING;

  /** The nw L 2 date. */
  private String nwL2Date = Symbol.EMPTY_STRING;

  /** The sl L 1 date. */
  private String slL1Date = Symbol.EMPTY_STRING;

  /** The sl L 2 date. */
  private String slL2Date = Symbol.EMPTY_STRING;

  /** The geography L 1 dao. */
  @Autowired
  private GeographyL1Dao geographyL1Dao;

  /** The geography L 2 dao. */
  @Autowired
  private GeographyL2Dao geographyL2Dao;

  /** The geography L 3 dao. */
  @Autowired
  private GeographyL3Dao geographyL3Dao;

  /** The geography L 4 dao. */
  @Autowired
  private GeographyL4Dao geographyL4Dao;

  /** The system configuration dao. */
  @Autowired
  private SystemConfigurationDao systemConfigurationDao;

  /**
   * Sets the geography zoom level mapping.
   */
  public void setGeographyZoomLevelMapping() {
    if (MapUtils.isEmpty(geographyZoomLevelMapping)) {
      String geographyZoomLevel =
          systemConfigurationDao.getValueByName(GenericMapUtils.GEOGRAPHY_ZOOM_LEVEL_MAPPING);
      if (StringUtils.isEmpty(geographyZoomLevel)) {
        throw new RestException(ForesightConstants.SYSTEM_CONFIGURATION_KEY_MISSING_MESSAGE
            + GenericMapUtils.GEOGRAPHY_ZOOM_LEVEL_MAPPING);
      }
      geographyZoomLevelMapping = new Gson().fromJson(geographyZoomLevel,
          new TypeToken<Map<Integer, String>>() {}.getType());
      logger.info("inside method setGeographyZoomLevelMapping size {}:",
          geographyZoomLevelMapping.size());

    }
  }

  /**
   * Sets the level TO geo graphy mapping.
   */
  public void setLevelTOGeoGraphyMapping() {
    if (MapUtils.isEmpty(levelTOGeoGraphyMapping)) {
      String levelTOGeoGraphy =
          systemConfigurationDao.getValueByName(GenericMapUtils.LEVEL_TO_GEOGRAPHY_MAPPING);
      if (StringUtils.isEmpty(levelTOGeoGraphy)) {
        throw new RestException(ForesightConstants.SYSTEM_CONFIGURATION_KEY_MISSING_MESSAGE
            + GenericMapUtils.LEVEL_TO_GEOGRAPHY_MAPPING);
      }
      levelTOGeoGraphyMapping =
          new Gson().fromJson(levelTOGeoGraphy, new TypeToken<Map<String, String>>() {}.getType());
      logger.info("inside method setLevelTOGeoGraphyMapping size {}:",
          levelTOGeoGraphyMapping.size());

    }
  }

  @Override
  public String getLatestBoundaryDate(String tableName, String geoLevel) {
    String date = Symbol.EMPTY_STRING;
    String systemConfigurationKey = getSystemConfigurationKey(tableName, geoLevel);
    if (StringUtils.isNotEmpty(systemConfigurationKey)) {
      try {
        String value = systemConfigurationDao.getValueByName(systemConfigurationKey);
        if (StringUtils.isNotEmpty(value)) {
          Date boundaryDate =
              Utils.parseStringToDate(value, ForesightConstants.DATE_FORMAT_yyyy_MM_dd);
          date = Utils.parseDateToString(boundaryDate, ForesightConstants.DDMMYY);
          logger.info("find value {} for systemConfigurationKey {}, final date {} ", value,
              systemConfigurationKey, date);
        }
      } catch (Exception e) {
        logger.error(
            "Inside getLatestBoundaryDate, not able to find SystemConfiguration vale for key {}, due to {}",
            systemConfigurationKey, e.getMessage());
      }
    }
    return date;
  }

  /**
   * Gets the system configuration key.
   *
   * @param tableName the table name
   * @param geoLevel the geo level
   * @return the system configuration key
   */
  private String getSystemConfigurationKey(String tableName, String geoLevel) {
    String systemConfigurationKey = Symbol.EMPTY_STRING;
    if (tableName.equalsIgnoreCase(GenericMapUtils.GEOGRAPHY_TABLE_NAME)) {
      if (geoLevel.equalsIgnoreCase(GenericMapUtils.L1_TYPE)) {
        systemConfigurationKey = ForesightConstants.NETWORK_BOUNDARY_L1_DATE;
      } else if (geoLevel.equalsIgnoreCase(GenericMapUtils.L2_TYPE)) {
        systemConfigurationKey = ForesightConstants.NETWORK_BOUNDARY_L2_DATE;
      } else if (geoLevel.equalsIgnoreCase(GenericMapUtils.L3_TYPE)) {
        systemConfigurationKey = ForesightConstants.NETWORK_BOUNDARY_L3_DATE;
      } else if (geoLevel.equalsIgnoreCase(GenericMapUtils.L4_TYPE)) {
        systemConfigurationKey = ForesightConstants.NETWORK_BOUNDARY_L4_DATE;
      }
    } else if (tableName.equalsIgnoreCase(GenericMapUtils.SALES_GEOGRAPHY_TABLE)) {
      if (geoLevel.equalsIgnoreCase(GenericMapUtils.L1_TYPE)) {
        systemConfigurationKey = ForesightConstants.SALES_BOUNDARY_L1_DATE;
      } else if (geoLevel.equalsIgnoreCase(GenericMapUtils.L2_TYPE)) {
        systemConfigurationKey = ForesightConstants.SALES_BOUNDARY_L2_DATE;
      } else if (geoLevel.equalsIgnoreCase(GenericMapUtils.L3_TYPE)) {
        systemConfigurationKey = ForesightConstants.SALES_BOUNDARY_L3_DATE;
      } else if (geoLevel.equalsIgnoreCase(GenericMapUtils.L4_TYPE)) {
        systemConfigurationKey = ForesightConstants.SALES_BOUNDARY_L4_DATE;
      }
    }
    return systemConfigurationKey;
  }

  /**
   * Find geography name by passing lat long location and zoomLevel it using NNS API( through
   * getNearestGeographyLocations).
   *
   * @param location the location
   * @param zoomLevel the zoom level
   * @return the map
   * @throws IOException Signals that an I/O exception has occurred.
   */
  @Override
  public Map<String, String> findGeographyNameByLatLong(LatLng location, int zoomLevel)
      throws IOException {
    logger.info("LatLng for findGeographyNameByLatLong for location: {} , zoomLevel: {}   ",
        location, zoomLevel);

    Map<String, String> geographyData = new HashMap<>();

    if (geographyZoomLevelMapping.isEmpty()) {
      setGeographyZoomLevelMapping();
    }

    if (levelTOGeoGraphyMapping.isEmpty()) {
      setLevelTOGeoGraphyMapping();
    }

    try {

      String level = geographyZoomLevelMapping.get(zoomLevel);
      logger.info("find level:{} for zoomLevel: {} ", level, zoomLevel);

      String type = levelTOGeoGraphyMapping.get(level);
      logger.info("type :{} for level:{}", type, level);

      String findGeography = findGeography(location, type);
      if (StringUtils.isEmpty(findGeography)) {
        findGeography = getNearestGeographyLocations(location, type);
      }
      geographyData.put(level, findGeography);

    } catch (Exception e) {
      logger.error("Exception inside findGeographyNameByLatLong due to: {} ",
          ExceptionUtils.getStackTrace(e));
    }
    return geographyData;
  }

  /**
   * <p>
   * <b>Usecase:</b>The methods returns the name of the nearest geography to the input Lat Lng. The
   * geography name is returned according to the passed geography level>
   * 
   * <p>
   * <b>Input:</b> LatLng location like [115.21045113757663, -8.494646301421007] and geoLevel like
   * (L1,L2,L3,L4) </br>
   * </p>
   *
   * 
   * <p>
   * 1. populate NNS map (if not already available) of geographys' centroids 2. Fetch nearest
   * centroid using NNS map 3. Return the nearest centroid's geography name </br>
   * </p>
   * <p>
   * Marked deprecated b/c nearest geography is returened according to geography's centroid and not
   * it's polygon </br>
   * NNS api(Nearest Neighbour Service) returning nearest location<br>
   * <br>
   * </br>
   * </p>
   * <p>
   * 
   * @param location the location
   * @param geoLevel the geo level
   * @return the nearest geography locations
   *         </P>
   */
  @Override
  public String getNearestGeographyLocations(LatLng location, String geoLevel) {
    if (geographiesCentroid.get(geoLevel) == null) {
      List<String> columnListForTable = GenericMapUtils.getColumnListForTable();
      Scan scan = GenericMapUtils.getScanByColumnList(columnListForTable);
      String date = getLatestBoundaryDate(GenericMapUtils.GEOGRAPHY_TABLE_NAME, geoLevel);
      String regex = GenericMapUtils.getRegExForGeographyLevel(date, geoLevel);
      scan.setFilter(new RowFilter(CompareOperator.EQUAL, new RegexStringComparator(regex)));
      NNS<Geography> centroidNNS = getCentroidNNS(scan, GenericMapUtils.GEOGRAPHY_TABLE_NAME);
      geographiesCentroid.put(geoLevel, centroidNNS);
    }
    return findNearestLocation(location, geographiesCentroid.get(geoLevel));
  }

  /**
   * Gets the nearest geography locations.
   *
   * @param location the location
   * @param zoomLevel the zoom level
   * @return the nearest geography locations
   */
  @Override
  public String getNearestGeographyLocations(LatLng location, int zoomLevel) {
    String geoLevel = getGeoLevelFromZoomLevel(zoomLevel);
    return getNearestGeographyLocations(location, geoLevel);
  }

  /**
   * Finding nearest location by using Lat Lng location and centroidList(using NNS api) private
   * method only used by getNearestGeographyLocations method.
   *
   * @param location the location
   * @param centroidList the centroid list
   * @return the string
   */

  private String findNearestLocation(LatLng location, NNS<Geography> centroidList) {
    if (centroidList != null) {
      return centroidList.getNearestSingleLocation(location).getName();
    }
    return null;
  }

  /**
   * Gets the centroidNNS by scan and table name CentroidNNS hava key and value,key represents
   * centroid and value represent geography data geting list(scanResultByPool) from HBaseResult.
   *
   * @param scan the scan
   * @param tableName the table name
   * @return the centroid NNS
   */
  private NNS<Geography> getCentroidNNS(Scan scan, String tableName) {
    List<Geography> geographys = new ArrayList<>();
    try {
      List<HBaseResult> scanResultByPool =
          scanResultByPool(scan, tableName, GenericMapUtils.getGeographyCfBytes());
      for (HBaseResult result : scanResultByPool) {
        String geographyName = result.getString(GenericMapUtils.getGeographyNameBytes());
        String centroid = result.getString(GenericMapUtils.getCentroidBytes());

        if (StringUtils.isEmpty(geographyName) || StringUtils.isEmpty(centroid)) {
          logger.error("GeographyName or centroid not found in geography data for rowKey {}",
              result.getRowKey());
          continue;
        }
        List<Double> latlng = GenericMapUtils.getCentroid(centroid);
        Geography geography = new Geography();
        geography.setName(geographyName);
        geography.setLatitude(latlng.get(1));
        geography.setLongitude(latlng.get(0));
        geographys.add(geography);
      }
    } catch (Exception exception) {
      logger.error("Exception while iterating scanner in getGeographyL4Data, StackTrace:{} ",
          ExceptionUtils.getStackTrace(exception));
    }
    logger.info("Inside getCentroidNNS, geographys count :{}", geographys.size());
    if (CollectionUtils.isEmpty(geographys)) {
      throw new RestException(GenericMapUtils.EMPTY_DATA_ERROR_MESSAGE);
    }
    return new NNS<>(geographys);
  }

  /**
   * Gets the geography data by row key.
   *
   * @param geoLevel the geo level
   * @param rowKey the row key
   * @return the geography data by row key
   */
  @Override
  public Map<String, String> getGeographyDataByRowKey(String geoLevel, String rowKey) {
    logger.info("Inside getGeographyDataByRowKey method  GeoLevel {}, rowKey {} ", geoLevel,
        rowKey);
    Map<String, String> resultMap = new HashMap<>();
    try {
      Table table = getTable(GenericMapUtils.GEOGRAPHY_TABLE_NAME);
      List<String> columnList = GenericMapUtils.getColumnListForQuery();
      Result result = table.get(new Get(Bytes.toBytes(rowKey)));
      for (String column : columnList) {
        String columnName = column.split(Symbol.COLON_STRING)[1];
        String value = Bytes.toString(
            result.getValue(GenericMapUtils.GEOGRAPHY_CF.getBytes(), columnName.getBytes()));
        resultMap.put(columnName, value);
      }
    } catch (Exception e) {
      logger.error("Exception while getting data from Geography table due to {}",
          ExceptionUtils.getStackTrace(e));
    }
    return resultMap;
  }

  /**
   * Gets the geography data by row key prefix and Zoom.
   * 
   *
   * @param geoLevel the geo level
   * @param rowkeyPrefix the rowkey prefix
   * @param zoom the zoom
   * @return the geography data by row key prefix
   */
  @Override
  public List<Map<String, String>> getGeographyDataByRowKeyPrefix(String geoLevel,
      String rowkeyPrefix, Integer zoom) {
    logger.info("Inside getGeographyDataByRowKey method  GeoLevel {}, rowKey {} ", geoLevel,
        rowkeyPrefix);
    List<Map<String, String>> resultMap = new ArrayList<>();
    String tableName = getTableName(geoLevel);
    zoom = getCoordinateZoom(geoLevel, zoom);
    Integer area = getFilterArea(geoLevel);
    try {

      if (StringUtils.isNotEmpty(tableName)) {
        zoom = zoom != null ? zoom : GenericMapUtils.DEFAULT_ADVANCE_COORDINATES_ZOOM;
        List<String> columnList = GenericMapUtils.getColumnListForQuery(zoom);
        columnList.add(GenericMapUtils.GEO_COL_ILA);
        columnList.add(GenericMapUtils.DISP_COL);
        if (area != null) {
          columnList.add(GenericMapUtils.GEO_COL_AREA);
        }

        Scan scan = GenericMapUtils.getScanByColumnList(columnList);
        scan.setRowPrefixFilter(Bytes.toBytes(rowkeyPrefix));

        List<HBaseResult> result =
            scanResultByPool(scan, tableName, Bytes.toBytes(GenericMapUtils.GEOGRAPHY_CF));

        for (HBaseResult hBaseResult : result) {
          Map<String, String> map = new HashMap<>();
          map.put(GenericMapUtils.GEOGRAPHY_NAME,
              hBaseResult.getString(Bytes.toBytes(GenericMapUtils.GEOGRAPHY_NAME)));
          map.put(GenericMapUtils.CENTROID,
              hBaseResult.getString(Bytes.toBytes(GenericMapUtils.CENTROID)));
          map.put(GenericMapUtils.COORDINATES,
              hBaseResult.getString(Bytes.toBytes(GenericMapUtils.COORDINATES + zoom)));
          map.put(GenericMapUtils.IS_LARGE_AREA,
              hBaseResult.getString(Bytes.toBytes(GenericMapUtils.IS_LARGE_AREA)));
          map.put(GenericMapUtils.DISPLAY_NAME,
              hBaseResult.getString(Bytes.toBytes(GenericMapUtils.DISPLAY_NAME)));

          if (area != null) {
            Double areaOfBoundary = hBaseResult.getStringAsDouble(GenericMapUtils.AREA);
            if (areaOfBoundary != null && areaOfBoundary < area) {
              continue;
            }
          }
          resultMap.add(map);
        }
        logger.info("Returning polygon size {} and Hbase result size {}", resultMap.size(),
            result.size());
      }
    } catch (Exception e) {
      logger.error("Exception while getting data from Geography table due to {}",
          ExceptionUtils.getStackTrace(e));
    }
    return resultMap;
  }


  @Override
  public String getTableName(String geoLevel) {
    geoLevel = geoLevel.toUpperCase();
    if (MapUtils.isEmpty(levelTableName)) {
      String geographyVales = Symbol.EMPTY_STRING;
      try {
        geographyVales =
            systemConfigurationDao.getValueByName(GenericMapUtils.ADVANCE_SEARCH_GEOGRAPHY_NAME);
      } catch (Exception e) {
        logger.error("Exception inside getTableName due to {}", e.getMessage());
      }
      if (StringUtils.isEmpty(geographyVales)) {
        throw new RestException(ForesightConstants.SYSTEM_CONFIGURATION_KEY_MISSING_MESSAGE
            + GenericMapUtils.ADVANCE_SEARCH_GEOGRAPHY_NAME);
      }
      levelTableName =
          new Gson().fromJson(geographyVales, new TypeToken<Map<String, String>>() {}.getType());
      logger.info("inside method getTableName jsonData {}:", levelTableName);
    }

    String tableName = levelTableName.get(geoLevel);
    logger.error("Table name {} found for geoLevel {}", tableName, geoLevel);
    return tableName;
  }


  /**
   * Gets the coordinate zoom.
   *
   * @param geoLevel the geo level
   * @param zoom the zoom
   * @return the coordinate zoom
   */
  public Integer getCoordinateZoom(String geoLevel, Integer zoom) {
    geoLevel = geoLevel.toUpperCase();
    if (MapUtils.isEmpty(levelWiseZoom)) {
      String geographyVales = Symbol.EMPTY_STRING;
      try {
        geographyVales =
            systemConfigurationDao.getValueByName(GenericMapUtils.ADVANCE_SEARCH_GEOGRAPHY_ZOOM);
      } catch (Exception e) {
        logger.error("Exception inside getCoordinateZoom due to {}", e.getMessage());
      }
      if (StringUtils.isEmpty(geographyVales)) {
        logger.error(
            "Inside getCoordinateZoom, System configuration key AdvanceSearchGeographyZoom is missing, now setting UI zoom {}",
            zoom);
        return zoom;
      }
      levelWiseZoom =
          new Gson().fromJson(geographyVales, new TypeToken<Map<String, Integer>>() {}.getType());
      logger.info("inside method getCoordinateZoom jsonData {}:", levelWiseZoom);
    }

    Integer coordinateZoom = levelWiseZoom.get(geoLevel);
    logger.error("Coordinate zoom {} found for geoLevel {}", coordinateZoom, geoLevel);
    return coordinateZoom;
  }

  /**
   * Gets the filter area.
   *
   * @param geoLevel the geo level
   * @return the filter area
   */
  public Integer getFilterArea(String geoLevel) {
    geoLevel = geoLevel.toUpperCase();
    if (MapUtils.isEmpty(levelWiseArea)) {
      String geographyVales = Symbol.EMPTY_STRING;
      try {
        geographyVales =
            systemConfigurationDao.getValueByName(GenericMapUtils.ADVANCE_SEARCH_AREA_FILTER);
      } catch (Exception e) {
        logger.error("Exception inside getFilterArea due to {}", e.getMessage());
      }
      if (StringUtils.isEmpty(geographyVales)) {
        logger.error(
            "Inside getFilterArea, System configuration key AdvanceSearchAreaFilter is missing, then area filter will not be applied");
        return null;
      }
      levelWiseArea =
          new Gson().fromJson(geographyVales, new TypeToken<Map<String, Integer>>() {}.getType());
      logger.info("inside method getFilterArea jsonData {}:", levelWiseArea);
    }

    Integer area = levelWiseArea.get(geoLevel);
    logger.error("Area {} found for geoLevel {}", area, geoLevel);

    return area;
  }

  /**
   * Gets the customer care geography.
   *
   * @param tableName the table name
   * @param location the location
   * @return the customer care geography
   */
  @Override
  public String getCustomerCareGeography(String tableName, LatLng location) {
    try {
      synchronized (this) {
        if (CollectionUtils.isEmpty(geographiesMap.get(GenericMapUtils.CUSTOMER_CARE))) {
          List<Geography> geographies = getGeographies(GenericMapUtils.getScanForGeography(),
              GenericMapUtils.CUSTOMER_REGION_TABLE_NAME);
          geographiesMap.put(GenericMapUtils.CUSTOMER_CARE, geographies);
        }
      }
      return getGeographyName(geographiesMap.get(GenericMapUtils.CUSTOMER_CARE), location);
    } catch (Exception e) {
      logger.error("Exception inside getCustomerCareGeography due to {}",
          ExceptionUtils.getStackTrace(e));
    }
    return null;
  }

  /**
   * Find geography L 1.
   *
   * @param location the location
   * @return the string
   */
  @Override
  public String findGeographyL1(LatLng location) {
    synchronized (this) {
      if (CollectionUtils.isEmpty(geographiesMap.get(GenericMapUtils.L1_TYPE))) {
        String date =
            getLatestBoundaryDate(GenericMapUtils.GEOGRAPHY_TABLE_NAME, GenericMapUtils.L1_TYPE);
        Scan scan = GenericMapUtils.getScanWithGeographyLevelFilter(GenericMapUtils.L1_TYPE, date);
        List<Geography> geographies = getGeographies(scan, GenericMapUtils.GEOGRAPHY_TABLE_NAME);
        geographiesMap.put(GenericMapUtils.L1_TYPE, geographies);
      }
    }
    return getGeographyName(geographiesMap.get(GenericMapUtils.L1_TYPE), location);
  }

  /**
   * Find geography L 2.
   *
   * @param location the location
   * @return the string
   */
  @Override
  public String findGeographyL2(LatLng location) {
    synchronized (this) {
      if (CollectionUtils.isEmpty(geographiesMap.get(GenericMapUtils.L2_TYPE))) {
        String date =
            getLatestBoundaryDate(GenericMapUtils.GEOGRAPHY_TABLE_NAME, GenericMapUtils.L2_TYPE);
        Scan scan = GenericMapUtils.getScanWithGeographyLevelFilter(GenericMapUtils.L2_TYPE, date);
        List<Geography> geographies = getGeographies(scan, GenericMapUtils.GEOGRAPHY_TABLE_NAME);
        geographiesMap.put(GenericMapUtils.L2_TYPE, geographies);
      }
    }
    return getGeographyName(geographiesMap.get(GenericMapUtils.L2_TYPE), location);
  }

  /**
   * Find geography L 3.
   *
   * @param location the location
   * @return the string
   */
  @Override
  public String findGeographyL3(LatLng location) {
    synchronized (this) {
      if (CollectionUtils.isEmpty(geographiesMap.get(GenericMapUtils.L3_TYPE))) {
        String date =
            getLatestBoundaryDate(GenericMapUtils.GEOGRAPHY_TABLE_NAME, GenericMapUtils.L3_TYPE);
        Scan scan = GenericMapUtils.getScanWithGeographyLevelFilter(GenericMapUtils.L3_TYPE, date);
        List<Geography> geographies = getGeographies(scan, GenericMapUtils.GEOGRAPHY_TABLE_NAME);
        geographiesMap.put(GenericMapUtils.L3_TYPE, geographies);
      }
    }
    return getGeographyName(geographiesMap.get(GenericMapUtils.L3_TYPE), location);
  }

  /**
   * Find geography L 4.
   *
   * @param location the location
   * @return the string
   */
  @Override
  public String findGeographyL4(LatLng location) {
    synchronized (this) {
      if (CollectionUtils.isEmpty(geographiesMap.get(GenericMapUtils.L4_TYPE))) {
        String date =
            getLatestBoundaryDate(GenericMapUtils.GEOGRAPHY_TABLE_NAME, GenericMapUtils.L4_TYPE);
        Scan scan = GenericMapUtils.getScanWithGeographyLevelFilter(GenericMapUtils.L4_TYPE, date);
        List<Geography> geographies = getGeographies(scan, GenericMapUtils.GEOGRAPHY_TABLE_NAME);
        geographiesMap.put(GenericMapUtils.L4_TYPE, geographies);
        // Only for testing purpose ,Don't delete this code
        calculateMapSize(geographiesMap);
      }
    }
    return getGeographyName(geographiesMap.get(GenericMapUtils.L4_TYPE), location);
  }

  /**
   * Find geography.
   *
   * @param location the location
   * @param geoLevel the geo level
   * @return the string
   */
  @Override
  public String findGeography(LatLng location, String geoLevel) {
    if (geoLevel.equalsIgnoreCase(GenericMapUtils.L1_TYPE)) {
      return findGeographyL1(location);
    } else if (geoLevel.equalsIgnoreCase(GenericMapUtils.L2_TYPE)) {
      return findGeographyL2(location);
    } else if (geoLevel.equalsIgnoreCase(GenericMapUtils.L3_TYPE)) {
      return findGeographyL3(location);
    } else if (geoLevel.equalsIgnoreCase(GenericMapUtils.L4_TYPE)) {
      return findGeographyL4(location);
    } else {
      logger.error("{} geoLevel not found", geoLevel);
    }
    return null;
  }

  /**
   * Find geography.
   *
   * @param location the location
   * @param zoomLevel the zoom level
   * @return the map
   */
  @Override
  public Map<String, String> findGeography(LatLng location, int zoomLevel) {
    Map<String, String> resultMap = new HashMap<>();
    String geoLevel = getGeoLevelFromZoomLevel(zoomLevel);
    String geographyName = findGeography(location, geoLevel);
    if (StringUtils.isEmpty(geographyName)) {
      geographyName = getNearestGeographyLocations(location, geoLevel);
    }
    resultMap.put(GenericMapUtils.GEOGRAPHY_LEVEL, geoLevel);
    resultMap.put(GenericMapUtils.GEOGRAPHY_NAME, geographyName);
    resultMap.putAll(findGeoIdAndDisplayName(geoLevel, geographyName));
    return resultMap;
  }

  /**
   * Find geo id and display name.
   *
   * @param geoLevel the geo level
   * @param geoName the geo name
   * @return the map
   */
  private Map<String, String> findGeoIdAndDisplayName(String geoLevel, String geoName) {
    Map<String, String> result = new HashMap<>();
    if (StringUtils.isNotEmpty(geoLevel) && StringUtils.isNotEmpty(geoName)) {
      try {
        if (geoLevel.equalsIgnoreCase(GenericMapUtils.L1_TYPE)) {
          GeographyL1 geographyL1ByName = geographyL1Dao.getGeographyL1ByName(geoName);
          result.put(GenericMapUtils.GEO_ID, String.valueOf(geographyL1ByName.getId()));
          result.put(GenericMapUtils.DISPLAY_NAME, geographyL1ByName.getDisplayName());
        } else if (geoLevel.equalsIgnoreCase(GenericMapUtils.L2_TYPE)) {
          GeographyL2 geographyL2ByName = geographyL2Dao.getGeographyL2ByName(geoName);
          result.put(GenericMapUtils.GEO_ID, String.valueOf(geographyL2ByName.getId()));
          result.put(GenericMapUtils.DISPLAY_NAME, geographyL2ByName.getDisplayName());
        } else if (geoLevel.equalsIgnoreCase(GenericMapUtils.L3_TYPE)) {
          GeographyL3 geographyL3ByName = geographyL3Dao.getGeographyL3ByName(geoName);
          result.put(GenericMapUtils.GEO_ID, String.valueOf(geographyL3ByName.getId()));
          result.put(GenericMapUtils.DISPLAY_NAME, geographyL3ByName.getDisplayName());
        } else if (geoLevel.equalsIgnoreCase(GenericMapUtils.L4_TYPE)) {
          GeographyL4 geographyL4ByName = geographyL4Dao.getGeographyL4ByName(geoName);
          result.put(GenericMapUtils.GEO_ID, String.valueOf(geographyL4ByName.getId()));
          result.put(GenericMapUtils.DISPLAY_NAME, geographyL4ByName.getDisplayName());
        } else {
          logger.error("{} geoLevel not found", geoLevel);
        }
      } catch (NoResultException e) {
        logger.error("geoId not found for geoName {}", geoName);
      } catch (Exception e) {
        logger.error("Exception inside findGeoId ,due to {}", ExceptionUtils.getStackTrace(e));
      }
    }
    return result;
  }

  /**
   * Gets the geo level from zoom level.
   *
   * @param zoomLevel the zoom level
   * @return the geo level from zoom level
   */
  private String getGeoLevelFromZoomLevel(int zoomLevel) {
    if (geographyZoomLevelMapping.isEmpty()) {
      setGeographyZoomLevelMapping();
    }
    if (levelTOGeoGraphyMapping.isEmpty()) {
      setLevelTOGeoGraphyMapping();
    }

    String level = geographyZoomLevelMapping.get(zoomLevel);
    String geoLevel = levelTOGeoGraphyMapping.get(level);
    logger.info("find geoLevel:{} for zoomLevel: {} ", geoLevel, zoomLevel);
    return geoLevel;
  }

  /**
   * Gets the geographies.
   *
   * @param scan the scan
   * @param tableName the table name
   * @return the geographies
   */
  @Override
  public List<Geography> getGeographies(Scan scan, String tableName) {
    logger.info("Inside getGeographies, Getting Geograhy List for tableName {}", tableName);
    List<Geography> geographies = new ArrayList<>();
    try {
      List<HBaseResult> scanResultByPool =
          scanResultByPool(scan, tableName, GenericMapUtils.getGeographyCfBytes());
      for (HBaseResult result : scanResultByPool) {
        String geographyName = result.getString(GenericMapUtils.getGeographyNameBytes());
        String displayName = result.getString(GenericMapUtils.getDisplayNameBytes());
        Integer l1Pk = result.getStringAsInteger(GenericMapUtils.getL1PkBytes());
        Integer l2Pk = result.getStringAsInteger(GenericMapUtils.getL2PkBytes());
        Integer l3Pk = result.getStringAsInteger(GenericMapUtils.getL3PkBytes());
        Integer l4Pk = result.getStringAsInteger(GenericMapUtils.getL4PkBytes());
        String polygon = result.getString(GenericMapUtils.getCoordinatesBytes());
        Double minLat = result.getStringAsDouble(GenericMapUtils.getMinLatBytes());
        Double minLng = result.getStringAsDouble(GenericMapUtils.getMinLngBytes());
        Double maxLat = result.getStringAsDouble(GenericMapUtils.getMaxLatBytes());
        Double maxLng = result.getStringAsDouble(GenericMapUtils.getMaxLngBytes());
        if (StringUtils.isEmpty(polygon) || !DoubleUtils.isValid(minLat)
            || !DoubleUtils.isValid(minLng) || !DoubleUtils.isValid(maxLat)
            || !DoubleUtils.isValid(maxLng) || StringUtils.isEmpty(geographyName)) {
          logger.error("Min/Max LatLng or Coordinate not found in geography data for rowKey {}",
              result.getRowKey());
          continue;
        }
        List<List<List<Double>>> coordinates = GenericMapUtils.getBoundary(polygon);
        GISGeometry gisPolygon = new GIS3DPolygon(coordinates);

        Geography geography = new Geography();
        geography.setBoundary(gisPolygon);
        geography.setName(geographyName);
        geography.setMinLat(minLat);
        geography.setMinLng(minLng);
        geography.setMaxLat(maxLat);
        geography.setMaxLng(maxLng);

        geographies.add(geography);
      }
    } catch (IOException e) {
      logger.error("IOException inside getGeographies due to {}", ExceptionUtils.getStackTrace(e));
    } catch (Exception e) {
      logger.error("Exception inside getGeographies due to {}", ExceptionUtils.getStackTrace(e));
    }
    logger.info("Inside getGeographies, geography list count :{}", geographies.size());
    return geographies;
  }

  /**
   * Gets the geography name.
   *
   * @param geographies the geographies
   * @param location the location
   * @return the geography name
   */
  private String getGeographyName(List<Geography> geographies, LatLng location) {
    List<Geography> estimatedGeographies = geographies.stream()
        .filter(geography -> geography.getMinLat() <= location.getLatitude()
            && geography.getMaxLat() >= location.getLatitude()
            && geography.getMinLng() <= location.getLongitude()
            && geography.getMaxLng() >= location.getLongitude())
        .collect(Collectors.toList());

    if (CollectionUtils.isNotEmpty(estimatedGeographies)) {
      for (Geography geography : estimatedGeographies) {
        if (geography.contains(location))
          return geography.getName();
      }
    }
    return null;
  }
  
  /**
   * Gets the geography display name.
   *
   * @param geographies the geographies
   * @param location the location
   * @return the geography obj
   */
  private Geography getGeographyDisplayName(List<Geography> geographies, LatLng location) {
    List<Geography> estimatedGeographies = geographies.stream()
        .filter(geography -> geography.getMinLat() <= location.getLatitude()
            && geography.getMaxLat() >= location.getLatitude()
            && geography.getMinLng() <= location.getLongitude()
            && geography.getMaxLng() >= location.getLongitude())
        .collect(Collectors.toList());

    if (CollectionUtils.isNotEmpty(estimatedGeographies)) {
      for (Geography geography : estimatedGeographies) {
        if (geography.contains(location))
          return geography;
      }
    }
    return null;
  }

  /**
   * Find sales geography L 1.
   *
   * @param location the location
   * @return the string
   */
  @Override
  public String findSalesL1(LatLng location) {
    synchronized (this) {
      if (CollectionUtils.isEmpty(geographiesMap.get(ForesightConstants.L1_SALES))) {
        String date =
            getLatestBoundaryDate(GenericMapUtils.SALES_GEOGRAPHY_TABLE, GenericMapUtils.L1_TYPE);
        Scan scan = GenericMapUtils.getScanWithGeographyLevelFilter(GenericMapUtils.L1_TYPE, date);
        List<Geography> geographies = getGeographies(scan, GenericMapUtils.SALES_GEOGRAPHY_TABLE);
        geographiesMap.put(ForesightConstants.L1_SALES, geographies);
      }
    }
    return getGeographyName(geographiesMap.get(ForesightConstants.L1_SALES), location);
  }


  /**
   * Find sales geography L 2.
   *
   * @param location the location
   * @return the string
   */
  @Override
  public String findSalesL2(LatLng location) {
    synchronized (this) {
      if (CollectionUtils.isEmpty(geographiesMap.get(ForesightConstants.L2_SALES))) {
        String date =
            getLatestBoundaryDate(GenericMapUtils.SALES_GEOGRAPHY_TABLE, GenericMapUtils.L2_TYPE);
        Scan scan = GenericMapUtils.getScanWithGeographyLevelFilter(GenericMapUtils.L2_TYPE, date);
        List<Geography> geographies = getGeographies(scan, GenericMapUtils.SALES_GEOGRAPHY_TABLE);
        geographiesMap.put(ForesightConstants.L2_SALES, geographies);
      }
    }
    return getGeographyName(geographiesMap.get(ForesightConstants.L2_SALES), location);
  }


  /**
   * Find sales geography L 3.
   *
   * @param location the location
   * @return the string
   */
  @Override
  public String findSalesL3(LatLng location) {
    synchronized (this) {
      if (CollectionUtils.isEmpty(geographiesMap.get(ForesightConstants.L3_SALES))) {
        String date =
            getLatestBoundaryDate(GenericMapUtils.SALES_GEOGRAPHY_TABLE, GenericMapUtils.L3_TYPE);
        Scan scan = GenericMapUtils.getScanWithGeographyLevelFilter(GenericMapUtils.L3_TYPE, date);
        List<Geography> geographies = getGeographies(scan, GenericMapUtils.SALES_GEOGRAPHY_TABLE);
        geographiesMap.put(ForesightConstants.L3_SALES, geographies);
      }
    }
    return getGeographyName(geographiesMap.get(ForesightConstants.L3_SALES), location);
  }


  /**
   * Find sales geography L 4.
   *
   * @param location the location
   * @return the string
   */
  @Override
  public String findSalesL4(LatLng location) {
    synchronized (this) {
      if (CollectionUtils.isEmpty(geographiesMap.get(ForesightConstants.L4_SALES))) {
        String date =
            getLatestBoundaryDate(GenericMapUtils.SALES_GEOGRAPHY_TABLE, GenericMapUtils.L4_TYPE);
        Scan scan = GenericMapUtils.getScanWithGeographyLevelFilter(GenericMapUtils.L4_TYPE, date);
        List<Geography> geographies = getGeographies(scan, GenericMapUtils.SALES_GEOGRAPHY_TABLE);
        geographiesMap.put(ForesightConstants.L4_SALES, geographies);
      }
    }
    return getGeographyName(geographiesMap.get(ForesightConstants.L4_SALES), location);
  }

  /**
   * Calculate map size.
   *
   * @param map the map
   */
  public void calculateMapSize(Map<String, List<Geography>> map) {
    try {
      logger.info("HashMap size :{}", map.size());
      byte[] vb = Bytes.toBytes(map.toString());
      long sizeInMb = vb.length / (1024 * 1024);
      logger.info("HashMap size in Bytes :{} ", vb.length);
      logger.info("HashMap size in MB :{}", sizeInMb);
    } catch (Exception e) {
      logger.error("Exception while calculating map size {}", ExceptionUtils.getStackTrace(e));
    }
  }

  /**
   * refreshGeographyMap.
   *
   * @param tableName the table name
   * @param dateType the date type
   * @param mapType the map type
   */
  @Override
  public void refreshGeographyMap(String tableName, String dateType, String mapType) {
    logger.info("Refresh Geography Map for type {}", mapType);
    try {
      String date = getLatestBoundaryDate(tableName, dateType);
      Scan scan = GenericMapUtils.getScanWithGeographyLevelFilter(dateType, date);
      List<Geography> geographies = getGeographies(scan, tableName);
      geographiesMap.put(mapType, geographies);
      logger.info("Geography Map size after refresh for type {} is {}", mapType,
          geographiesMap.get(mapType).size());
    } catch (Exception exception) {
      logger.error("Error while refreshingGeographyMap {}",
          ExceptionUtils.getStackTrace(exception));
    }
  }


  /**
   * Refresh geography map date wise.
   *
   * @param tableName the table name
   * @param dateType the date type
   * @param mapType the map type
   * @param systemConfDate the date
   */
  public void refreshGeographyMapDateWise(String tableName, String dateType, String mapType,
      String systemConfDate) {
    logger.info("Refresh Date Wise Geography Map for type {}", mapType);
    try {
      Scan scan = GenericMapUtils.getScanWithGeographyLevelFilter(dateType, systemConfDate);
      List<Geography> geographies = getGeographies(scan, tableName);
      geographiesMap.put(mapType, geographies);
      logger.info("Date Wise Geography Map size after refresh for type {} is {}", mapType,
          geographiesMap.get(mapType).size());
    } catch (Exception exception) {
      logger.error("Error while refreshGeographyMapDateWise {}",
          ExceptionUtils.getStackTrace(exception));
    }
  }

  @Override
  public void refreshGeographyMapByDate() {
    try {
      String networkL1Date =
          getLatestBoundaryDate(GenericMapUtils.GEOGRAPHY_TABLE_NAME, GenericMapUtils.L1_TYPE);
      String networkL2Date =
          getLatestBoundaryDate(GenericMapUtils.GEOGRAPHY_TABLE_NAME, GenericMapUtils.L2_TYPE);
      String salesL1Date =
          getLatestBoundaryDate(GenericMapUtils.SALES_GEOGRAPHY_TABLE, GenericMapUtils.L1_TYPE);
      String salesL2Date =
          getLatestBoundaryDate(GenericMapUtils.SALES_GEOGRAPHY_TABLE, GenericMapUtils.L2_TYPE);

      logger.info(
          "Inside refreshGeographyMapByDate , networkL1Date {}, networkL2Date {}, salesL1Date {}, salesL2Date {}, nwL1Date {}, nwL2Date {}, slL1Date {}, slL2Date {}",
          networkL1Date, networkL2Date, salesL1Date, salesL2Date, nwL1Date, nwL2Date, slL1Date,
          slL2Date);

      if (!(networkL1Date.equalsIgnoreCase(nwL1Date))) {
        refreshGeographyMapDateWise(GenericMapUtils.GEOGRAPHY_TABLE_NAME, GenericMapUtils.L1_TYPE,
            GenericMapUtils.L1_TYPE, networkL1Date);
        nwL1Date = networkL1Date;
      }
      if (!(networkL2Date.equalsIgnoreCase(nwL2Date))) {
        refreshGeographyMapDateWise(GenericMapUtils.GEOGRAPHY_TABLE_NAME, GenericMapUtils.L2_TYPE,
            GenericMapUtils.L2_TYPE, networkL2Date);
        nwL2Date = networkL2Date;
      }
      if (!(salesL1Date.equalsIgnoreCase(slL1Date))) {
        refreshGeographyMapDateWise(GenericMapUtils.SALES_GEOGRAPHY_TABLE, GenericMapUtils.L1_TYPE,
            ForesightConstants.L1_SALES, salesL1Date);
        slL1Date = salesL1Date;
      }
      if (!(salesL2Date.equalsIgnoreCase(slL2Date))) {
        refreshGeographyMapDateWise(GenericMapUtils.SALES_GEOGRAPHY_TABLE, GenericMapUtils.L2_TYPE,
            ForesightConstants.L2_SALES, salesL2Date);
        slL2Date = salesL2Date;
      }
    } catch (Exception exception) {
      logger.error("Error while refreshGeographyMapByDate due to {}",
          ExceptionUtils.getStackTrace(exception));
    }
  }
  
  /**
   * Find geography L 1.
   *
   * @param location the location
   * @return the geographyObj
   */
  @Override
  public Geography findL1(LatLng location) {
    synchronized (this) {
      if (CollectionUtils.isEmpty(geographiesMap.get(GenericMapUtils.L1_TYPE))) {
        String date =
            getLatestBoundaryDate(GenericMapUtils.GEOGRAPHY_TABLE_NAME, GenericMapUtils.L1_TYPE);
        Scan scan = GenericMapUtils.getScanWithGeographyLevelFilter(GenericMapUtils.L1_TYPE, date);
        List<Geography> geographies = getGeographies(scan, GenericMapUtils.GEOGRAPHY_TABLE_NAME);
        geographiesMap.put(GenericMapUtils.L1_TYPE, geographies);
      }
    }
    return getGeographyDisplayName(geographiesMap.get(GenericMapUtils.L1_TYPE), location);
  }

  /**
   * Find geography L 2.
   *
   * @param location the location
   * @return the geography obj
   */
  @Override
  public Geography findL2(LatLng location)  {
    synchronized (this) {
      if (CollectionUtils.isEmpty(geographiesMap.get(GenericMapUtils.L2_TYPE))) {
        String date =
            getLatestBoundaryDate(GenericMapUtils.GEOGRAPHY_TABLE_NAME, GenericMapUtils.L2_TYPE);
        Scan scan = GenericMapUtils.getScanWithGeographyLevelFilter(GenericMapUtils.L2_TYPE, date);
        List<Geography> geographies = getGeographies(scan, GenericMapUtils.GEOGRAPHY_TABLE_NAME);
        geographiesMap.put(GenericMapUtils.L2_TYPE, geographies);
      }
    }
    return getGeographyDisplayName(geographiesMap.get(GenericMapUtils.L2_TYPE), location);
  }

  /**
   * Find geography L 3.
   *
   * @param location the location
   * @return the geography obj
   */
  @Override
  public Geography findL3(LatLng location) {
    synchronized (this) {
      if (CollectionUtils.isEmpty(geographiesMap.get(GenericMapUtils.L3_TYPE))) {
        String date =
            getLatestBoundaryDate(GenericMapUtils.GEOGRAPHY_TABLE_NAME, GenericMapUtils.L3_TYPE);
        Scan scan = GenericMapUtils.getScanWithGeographyLevelFilter(GenericMapUtils.L3_TYPE, date);
        List<Geography> geographies = getGeographies(scan, GenericMapUtils.GEOGRAPHY_TABLE_NAME);
        geographiesMap.put(GenericMapUtils.L3_TYPE, geographies);
      }
    }
    return getGeographyDisplayName(geographiesMap.get(GenericMapUtils.L3_TYPE), location);
  }

  /**
   * Find geography L 4.
   *
   * @param location the location
   * @return the geography obj
   */
  @Override
  public Geography findL4(LatLng location) {
    synchronized (this) {
      if (CollectionUtils.isEmpty(geographiesMap.get(GenericMapUtils.L4_TYPE))) {
        String date =
            getLatestBoundaryDate(GenericMapUtils.GEOGRAPHY_TABLE_NAME, GenericMapUtils.L4_TYPE);
        Scan scan = GenericMapUtils.getScanWithGeographyLevelFilter(GenericMapUtils.L4_TYPE, date);
        List<Geography> geographies = getGeographies(scan, GenericMapUtils.GEOGRAPHY_TABLE_NAME);
        geographiesMap.put(GenericMapUtils.L4_TYPE, geographies);
        // Only for testing purpose ,Don't delete this code
        calculateMapSize(geographiesMap);
      }
    }
    return getGeographyDisplayName(geographiesMap.get(GenericMapUtils.L4_TYPE), location);
  }
}

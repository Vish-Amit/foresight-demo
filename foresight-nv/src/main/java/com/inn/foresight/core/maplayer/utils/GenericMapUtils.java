package com.inn.foresight.core.maplayer.utils;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.CompareOperator;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.inn.commons.Symbol;
import com.inn.commons.maps.LatLng;
import com.inn.commons.maps.geometry.GeometryUtils;

/**
 * The Class GenericMapUtils.
 */
public class GenericMapUtils {

  /**
   * Instantiates a new generic map utils.
   */
  private GenericMapUtils() {
    super();
  }

  /** The Constant GEO_ID. */
  public static final String GEO_ID = "geoId";

  /** The Constant LEVEL_TO_GEOGRAPHY_MAPPING. */
  public static final String LEVEL_TO_GEOGRAPHY_MAPPING = "LEVEL_TO_GEOGRAPHY_MAPPING";


  /** The Constant GEOGRAPHY_ZOOM_LEVEL_MAPPING. */
  public static final String GEOGRAPHY_ZOOM_LEVEL_MAPPING = "GEOGRAPHY_ZOOM_LEVEL_MAPPING";


  /** The Constant ADVANCE_SEARCH_GEOGRAPHY_NAME. */
  public static final String ADVANCE_SEARCH_GEOGRAPHY_NAME = "AdvanceSearchGeography";

  /** The Constant ADVANCE_SEARCH_GEOGRAPHY_ZOOM. */
  public static final String ADVANCE_SEARCH_GEOGRAPHY_ZOOM = "AdvanceSearchGeographyZoom";

  /** The Constant ADVANCE_SEARCH_AREA_FILTER. */
  public static final String ADVANCE_SEARCH_AREA_FILTER = "AdvanceSearchAreaFilter";

  /** The Constant TABLENAME. */
  public static final String TABLENAME = "tableName";

  /** The Constant ZOOM. */
  public static final String ZOOM = "zoomLevel";

  /** The Constant NWLAT. */
  public static final String NWLAT = "NWLat";

  /** The Constant NWLONG. */
  public static final String NWLONG = "NWLng";

  /** The Constant NELAT. */
  public static final String NELAT = "NELat";

  /** The Constant NELONG. */
  public static final String NELONG = "NELng";

  /** The Constant SELAT. */
  public static final String SELAT = "SELat";

  /** The Constant SELONG. */
  public static final String SELONG = "SELng";

  /** The Constant SWLAT. */
  public static final String SWLAT = "SWLat";

  /** The Constant SWLONG. */
  public static final String SWLONG = "SWLng";

  /** The Constant TILEID. */
  public static final String TILEID = "tileId";

  /** The Constant IMAGE. */
  public static final String IMAGE = "image";

  /** The Constant MINDATE. */
  public static final String MINDATE = "minDate";

  /** The Constant MAXDATE. */
  public static final String MAXDATE = "maxDate";

  /** The Constant NETWORKTYPE. */
  public static final String NETWORKTYPE = "networkType";

  /** The Constant DATASOURCE. */
  public static final String DATASOURCE = "dataSource";

  /** The Constant KPI. */
  public static final String KPI = "kpi";

  /** The Constant DATE. */
  public static final String DATE = "date";

  /** The Constant SUBPATH. */
  public static final String SUBPATH = "subPath";

  /** The Constant SITESTATUS. */
  public static final String SITESTATUS = "siteStatus";

  /** The Constant FLOOR. */
  public static final String FLOOR = "floor";

  /** The Constant LON. */
  public static final String LON = "long";

  /** The Constant LAT. */
  public static final String LAT = "lat";

  /** The Constant SERVICE. */
  public static final String SERVICE = "service";

  /** The Constant SITENAME. */
  public static final String SITENAME = "siteName";

  /** The Constant CELLNAME. */
  public static final String CELLNAME = "cellName";

  /** The Constant BAND. */
  public static final String BAND = "band";

  /** The Constant FILETYPE. */
  public static final String DIMENSION = "dimension";

  /** The Constant INDEX_DIR. */
  public static final String INDEX_DIR = "indexDir";

  /** The Constant AREA. */
  public static final String AREA = "area";

  /** The Constant morphology. */
  public static final String MORPHOLOGY = "morphology";

  /** The Constant POSTFIX. */
  public static final String POSTFIX = "postFix";

  /** The Constant KPI1_NAME. */
  public static final String KPI1_NAME = "KPI1_NAME";

  /** The Constant KPI1_COLUMN. */
  public static final String KPI1_COLUMN = "KPI1_COLUMN";

  /** The Constant KPI1_DATE. */
  public static final String KPI1_DATE = "KPI1_DATE";

  /** The Constant KPI2_TABLENAME. */
  public static final String KPI2_TABLENAME = "KPI2_TABLENAME";

  /** The Constant KPI2_NAME. */
  public static final String KPI2_NAME = "KPI2_NAME";

  /** The Constant KPI2_COLUMN. */
  public static final String KPI2_COLUMN = "KPI2_COLUMN";

  /** The Constant KPI2_DATE. */
  public static final String KPI2_DATE = "KPI2_DATE";

  /** The Constant KPI1_TABLENAME. */
  public static final String KPI1_TABLENAME = "kpi1Tablename";

  /** The Constant ISEXACT. */
  public static final String ISEXACT = "isExact";

  /** The Constant GEOGRAPHY_CF. */
  public static final String GEOGRAPHY_CF = "r";

  /** The Constant GEOGRAPHY_NAME. */
  public static final String GEOGRAPHY_NAME = "gn";
  
  public static final String CENTROID = "cent";

  /** The Constant L1_TYPE. */
  public static final String L1_TYPE = "GEOGRAPHYL1";

  /** The Constant L2_TYPE. */
  public static final String L2_TYPE = "GEOGRAPHYL2";

  /** The Constant L3_TYPE. */
  public static final String L3_TYPE = "GEOGRAPHYL3";

  /** The Constant L4_TYPE. */
  public static final String L4_TYPE = "GEOGRAPHYL4";

  /** The Constant COORDINATES. */
  public static final String COORDINATES = "cord";

  /** The Constant DISPLAY_NAME. */
  public static final String DISPLAY_NAME = "dn";
  
  /** The Constant L1_PK. */
  public static final String L1_PK = "l1pk";
  
  /** The Constant L2_PK. */
  public static final String L2_PK = "l2pk";
  
  /** The Constant L3_PK. */
  public static final String L3_PK = "l3pk";
  
  /** The Constant L4_PK. */
  public static final String L4_PK = "l4pk";
  

  /** The Constant DISP_COL. */
  public static final String DISP_COL = GEOGRAPHY_CF + Symbol.COLON + DISPLAY_NAME;

  /** The Constant GEO_COL_NAME. */
  public static final String GEO_COL_NAME = GEOGRAPHY_CF + Symbol.COLON + GEOGRAPHY_NAME;

  /** The Constant GEO_COL_COORDINATES. */
  public static final String GEO_COL_COORDINATES = GEOGRAPHY_CF + Symbol.COLON + COORDINATES;

  /** The Constant GEO_COL_CENTROID. */
  public static final String GEO_COL_CENTROID = GEOGRAPHY_CF + Symbol.COLON + CENTROID;

  /** The Constant GEOGRAPHY_TABLE_NAME. */
  public static final String GEOGRAPHY_TABLE_NAME = "Geography";

  /** The Constant DOPWIZARD_GENERIC_TILEIMAGE_URL. */
  public static final String DOPWIZARD_GENERIC_TILEIMAGE_URL = "/rest/ms/map/getTileImage";

  /** The Constant DOPWIZARD_GET_ALL_GEOGRAPHY_NAME_URL. */
  public static final String DOPWIZARD_GET_ALL_GEOGRAPHY_NAME_URL =
      "/rest/ms/map/getAllGeographyName";
  
  /** The Constant DOPWIZARD_GET_ALL_GEOGRAPHY_DISPLAY_NAME_URL. */
  public static final String DOPWIZARD_GET_ALL_GEOGRAPHY_DISPLAY_NAME_URL = "/rest/ms/map/getAllGeographyDisplayName";

  /** The Constant IMAGE_COLUMN. */
  public static final String IMAGE_COLUMN = "imgColumn";

  /** The Constant GEOGRAPHY_L1. */
  public static final String GEOGRAPHY_L1 = "GEOGRAPHYL1";

  /** The Constant GEOGRAPHY_L2. */
  public static final String GEOGRAPHY_L2 = "GEOGRAPHYL2";

  /** The Constant GEOGRAPHY_L3. */
  public static final String GEOGRAPHY_L3 = "GEOGRAPHYL3";

  /** The Constant GEOGRAPHY_L4. */
  public static final String GEOGRAPHY_L4 = "GEOGRAPHYL4";

  /** The Constant DROPWIZARD_GENERICBOUNDARY_URL. */
  public static final String DROPWIZARD_GENERICBOUNDARY_URL = "/rest/ms/map/getBoundaryData/";

  /** The Constant DROPWIZARD_GENERICBOUNDARYBYPOINT_URL. */
  public static final String DROPWIZARD_GENERICBOUNDARYBYPOINT_URL =
      "/rest/ms/map/getGeographyDataByPoint/";

  /** The Constant DROPWIZARD_GENERICBOUNDARYBYGEOGRAPHYNAME_URL. */
  public static final String DROPWIZARD_GENERICBOUNDARYBYGEOGRAPHYNAME_URL =
      "/rest/ms/map/getBoundaryDataByGeographyNames";

  /** The Constant DROPWIZARD_NEARESTBOUNDARYBYPOINT_URL. */
  public static final String DROPWIZARD_NEARESTBOUNDARYBYPOINT_URL =
      "/rest/ms/map/getNearestPointByLatLng";

  /** The Constant DROPWIZARD_GENERIC_KPI_URL. */
  public static final String DROPWIZARD_GENERIC_KPI_URL = "/rest/ms/map/getImageForKpi";

  /** The Constant DROPWIZARD_HBASEDATABYROWORTIMERANGE_URL. */
  public static final String DROPWIZARD_HBASEDATABYROWORTIMERANGE_URL =
      "/rest/ms/map/getHbaseDataByRowOrTimeRange/";

  /** The Constant DROPWIZARD_GENERICMAP_URL. */
  public static final String DROPWIZARD_GENERICMAP_URL = "/rest/ms/map/getDataFromHbase/";

  /** The Constant DROPWIZARD_GENERIC_IMAGE_URL. */
  public static final String DROPWIZARD_GENERIC_IMAGE_URL = "/rest/ms/map/getImage";

  /** The Constant DROPWIZARD_GENERIC_KPI_ZONE_URL. */
  public static final String DROPWIZARD_GENERIC_KPI_ZONE_URL = "/rest/ms/map/getImageForKpiAndZone";

  /** The Constant DROPWIZARD_GENERIC_BIGTILE_KPI_ZONE_URL. */
  public static final String DROPWIZARD_GENERIC_BIGTILE_KPI_ZONE_URL =
      "/rest/ms/map/getBigImageForKpiAndZone";

  /** The Constant IS_BIGTILE. */
  public static final String IS_BIGTILE = "isBigTile";

  /** The Constant BASE_ZOOM. */
  public static final String BASE_ZOOM = "baseZoom";

  /** The Constant TILE_BASE_ZOOM. */
  public static final Integer TILE_BASE_ZOOM = 15;

  /** The Constant POPULATION_BASE_ZOOM. */
  public static final Integer POPULATION_BASE_ZOOM = 15;

  /** The Constant DROPWIZARD_CUSOMER_CARE_GEOGRAPHYNAME_URL. */
  public static final String DROPWIZARD_CUSOMER_CARE_GEOGRAPHYNAME_URL =
      "/rest/ms/map/getCustomerCareGeography";

  /** The Constant MIN_LAT. */
  public static final String MIN_LAT = "miLat";

  /** The Constant MIN_LNG. */
  public static final String MIN_LNG = "miLng";

  /** The Constant MAX_LAT. */
  public static final String MAX_LAT = "maLat";

  /** The Constant MAX_LNG. */
  public static final String MAX_LNG = "maLng";

  /** The Constant L1_CODE. */
  public static final String L1_CODE = "A";

  /** The Constant L2_CODE. */
  public static final String L2_CODE = "B";

  /** The Constant L3_CODE. */
  public static final String L3_CODE = "C";

  /** The Constant L4_CODE. */
  public static final String L4_CODE = "D";

  /** The Constant IS_LARGE_AREA. */
  public static final String IS_LARGE_AREA = "ila";

  /** The Constant GEO_COL_ILA. */
  public static final String GEO_COL_ILA = GEOGRAPHY_CF + Symbol.COLON + IS_LARGE_AREA;

  /** The Constant TYPE. */
  public static final String TYPE = "type";

  /** The Constant TRUE. */
  public static final String TRUE = "true";

  /** The Constant TILE_ZONE_MAP_ZOOM. */
  public static final int TILE_ZONE_MAP_ZOOM = 6;

  /** The Constant POLY_INDEXPATH. */
  public static final String POLY_INDEXPATH = "polygon";

  /** The Constant BOX_INDEXPATH. */
  public static final String BOX_INDEXPATH = "box";

  /** The Constant GEO_COL_AREA. */
  public static final String GEO_COL_AREA = GEOGRAPHY_CF + Symbol.COLON + AREA;

  /** The Constant SEARCH_KEY. */
  public static final String SEARCH_KEY = "rowKey";

  /** The Constant COLUMN_FAMILY_INDEX. */
  private static final int COLUMN_FAMILY_INDEX = 0;

  /** The Constant COLUMN_QUALIFIER_INDEX. */
  private static final int COLUMN_QUALIFIER_INDEX = 1;

  /** The Constant CUSTOMER_REGION_TABLE_NAME. */
  public static final String CUSTOMER_REGION_TABLE_NAME = "CustomerRegion";

  /** The Constant CUSTOMER_CARE. */
  public static final String CUSTOMER_CARE = "CustomerCare";

  /** The Constant GEOGRAPHY_LEVEL. */
  public static final String GEOGRAPHY_LEVEL = "geoLevel";

  /** The Constant GEOGRAPHY_CF_BYTES. */
  private static final byte[] GEOGRAPHY_CF_BYTES = Bytes.toBytes(GEOGRAPHY_CF);

  /** The Constant GEOGRAPHY_NAME_BYTES. */
  private static final byte[] GEOGRAPHY_NAME_BYTES = Bytes.toBytes(GEOGRAPHY_NAME);
  
  private static final byte[] DISPLAY_NAME_BYTES = Bytes.toBytes(DISPLAY_NAME);

  /** The Constant COORDINATES_BYTES. */
  private static final byte[] COORDINATES_BYTES = Bytes.toBytes(COORDINATES);

  /** The Constant CENTROID_BYTES. */
  private static final byte[] CENTROID_BYTES = Bytes.toBytes(CENTROID);

  /** The Constant MIN_LAT_BYTES. */
  private static final byte[] MIN_LAT_BYTES = Bytes.toBytes(MIN_LAT);

  /** The Constant MIN_LNG_BYTES. */
  private static final byte[] MIN_LNG_BYTES = Bytes.toBytes(MIN_LNG);

  /** The Constant MAX_LAT_BYTES. */
  private static final byte[] MAX_LAT_BYTES = Bytes.toBytes(MAX_LAT);

  /** The Constant MAX_LNG_BYTES. */
  private static final byte[] MAX_LNG_BYTES = Bytes.toBytes(MAX_LNG);

  /** The Constant TYPE_BYTES. */
  private static final byte[] TYPE_BYTES = Bytes.toBytes(TYPE);
  
  private static final byte[] L1_PK_BYTES = Bytes.toBytes(L1_PK);
  
  private static final byte[] L2_PK_BYTES = Bytes.toBytes(L2_PK);
  
  private static final byte[] L3_PK_BYTES = Bytes.toBytes(L3_PK);
  
  private static final byte[] L4_PK_BYTES = Bytes.toBytes(L4_PK);

  /** The Constant POPULATION_ACCURACY_FACTOR. */
  public static final String POPULATION_ACCURACY_FACTOR = "POPULATION_ACCURACY_FACTOR";

  /** The Constant FACEBOOK_POPULATION_TABLE. */
  public static final String FACEBOOK_POPULATION_TABLE = "Population";

  /** The Constant FACEBOOK_IMAGE_COL. */
  public static final String FACEBOOK_IMAGE_COL = "i";

  /** The Constant DROPWIZARD_ALL_GEOGRAPHY_URL. */
  public static final String DROPWIZARD_ALL_GEOGRAPHY_URL = "/rest/ms/map/getAllGeographyName/";

  /** The Constant DROPWIZARD_ALL_SALES_GEOGRAPHY_BY_POLYGON. */
  public static final String DROPWIZARD_ALL_SALES_GEOGRAPHY_BY_POLYGON =
      "/rest/ms/map/sales/getAllGeographyNameByPolygon/";

  /** The Constant DROPWIZARD_ALL_NW_GEOGRAPHY_BY_POLYGON. */
  public static final String DROPWIZARD_ALL_NW_GEOGRAPHY_BY_POLYGON =
      "/rest/ms/map/getAllGeographyNameByPolygon/";

  /** The Constant IMAGE_COL. */
  public static final String IMAGE_COL = "i";

  /** The Constant DEFAULT_ACCURACY_FACTOR. */
  public static final Double DEFAULT_ACCURACY_FACTOR = 1.0;

  /** The Constant ZERO. */
  public static final Double ZERO = 0.0;

  /** The Constant DEFAULT_AMSL_POPULATION_VALUE. */
  public static final Double DEFAULT_AMSL_POPULATION_VALUE = 0.0;

  /** The Constant DEFAULT_AMSL_POPULATION_STRING_VALUE. */
  public static final String DEFAULT_AMSL_POPULATION_STRING_VALUE = "NO_DATA";

  /** The Constant ACCEPT. */
  public static final String ACCEPT = "ACCEPT";

  /** The Constant APP_OCTECT_STREM. */
  public static final String APP_OCTECT_STREM = "application/octet-stream";

  /** The Constant ERROR_MSG_FETCHING_DATA. */
  public static final String ERROR_MSG_FETCHING_DATA =
      "{\"errorMsg\": \"Error while fetching data\"}";


  /** The Constant INAVLID_PARAMS. */
  public static final String INAVLID_PARAMS = "Invalid params";

  /** The Constant COLUMN_FAMILY_NOT_SUPPLIED. */
  public static final String COLUMN_FAMILY_NOT_SUPPLIED =
      "Column family is not supplied in request";

  /** The Constant POINT_INDEXPATH. */
  public static final String POINT_INDEXPATH = "point";

  /** The Constant IS_COLOR_UPDATION. */
  public static final String IS_COLOR_UPDATION = "isColorUpdation";

  /** The Constant ACCURACY_FACTOR. */
  public static final String ACCURACY_FACTOR = "accuracyFactor";

  /** The Constant GRID_COL_NAME. */
  public static final String GRID_COL_NAME = "gridColName";


  /** The Constant DROPWIZARD_GENERICBOUNDARYBYPK_URL. */
  public static final String DROPWIZARD_GENERICBOUNDARYBYPK_URL =
      "/rest/ms/map/getBoundaryDataByPk";

  /** The Constant MICROSERVICE_GET_BOUNDARYDATA_BY_ROWKEYPREFIX_URL. */
  public static final String MICROSERVICE_GET_BOUNDARYDATA_BY_ROWKEYPREFIX_URL =
      "/rest/ms/map/getBoundaryDataByRowkeyPrefix";

  /** The Constant AGGREGATION_TYPE. */
  public static final String AGGREGATION_TYPE = "aggType";

  /** The Constant SALES_GEOGRAPHY_TABLE. */
  public static final String SALES_GEOGRAPHY_TABLE = "SalesGeography";

  /** The Constant AMSL_ACCURACY_FACTOR. */
  public static final String AMSL_ACCURACY_FACTOR = "AMSL_ACCURACY_FACTOR";

  /** The Constant POPULATION_ACCURACY_FACTOR_ZOOM_WISE. */
  public static final String POPULATION_ACCURACY_FACTOR_ZOOM_WISE =
      "POPULATION_ACCURACY_FACTOR_ZOOM_WISE";

  /** The Constant DROPWIZARD_FB_POPULATION_FOR_KPI_SUMMARY. */
  public static final String DROPWIZARD_FB_POPULATION_FOR_KPI_SUMMARY =
      "/rest/ms/map/getFbPopulationForKpiSummary";

  /** The Constant DOPWIZARD_GET_ALL_SALES_GEOGRAPHY_NAME_URL. */
  public static final String DOPWIZARD_GET_ALL_SALES_GEOGRAPHY_NAME_URL =
      "/rest/ms/map/sales/getAllGeographyName";

  /** The Constant LNG. */
  public static final String LNG = "lng";

  /** The Constant MICROSERVICE_FINDGEOGRAPHY_L1_URL. */
  public static final String MICROSERVICE_FINDGEOGRAPHY_L1_URL =
      "/rest/ms/BoundaryData/findGeographyL1/";

  /** The Constant MICROSERVICE_FINDGEOGRAPHY_L2_URL. */
  public static final String MICROSERVICE_FINDGEOGRAPHY_L2_URL =
      "/rest/ms/BoundaryData/findGeographyL2/";

  /** The Constant MICROSERVICE_FINDGEOGRAPHY_L3_URL. */
  public static final String MICROSERVICE_FINDGEOGRAPHY_L3_URL =
      "/rest/ms/BoundaryData/findGeographyL3/";

  /** The Constant MICROSERVICE_FINDGEOGRAPHY_L4_URL. */
  public static final String MICROSERVICE_FINDGEOGRAPHY_L4_URL =
      "/rest/ms/BoundaryData/findGeographyL4/";

  /** The Constant NW_GEOGRAPHY_TYPE. */
  public static final String NW_GEOGRAPHY_TYPE = "GEOGRAPHY";

  /** The Constant SALES_GEOGRAPHY_TYPE. */
  public static final String SALES_GEOGRAPHY_TYPE = "SALES";

  /** The Constant DROPWIZARD_GENERICMAP_GETHBASEDATA_URL. */
  public static final String DROPWIZARD_GENERICMAP_GETHBASEDATA_URL =
      "/rest/ms/map/getDataFromHbaseByRowkeyPrefix";

  /** The Constant EMPTY_DATA_ERROR_MESSAGE. */
  public static final String EMPTY_DATA_ERROR_MESSAGE = "Unable to load geography data";

  /** The Constant DEFAULT_ADVANCE_COORDINATES_ZOOM. */
  public static final int DEFAULT_ADVANCE_COORDINATES_ZOOM = 10;

  public static final String DROPWIZARD_ROUTE_BYPK_URL = "/rest/ms/map/getRouteData";

  public static final String BUFFER_COORDINATES = "bcord";

  /**
   * Gets the column list for table.
   *
   * @return the column list for table
   */
  public static List<String> getColumnListForTable() {
    List<String> columnList = new ArrayList<>();
    columnList.add(GEO_COL_NAME);
    columnList.add(GEO_COL_CENTROID);
    return columnList;
  }

  /**
   * Gets the geo column list.
   *
   * @return the geo column list
   */
  public static List<String> getGeoColumnList() {
    List<String> columnList = new ArrayList<>();
    columnList.add(GEO_COL_NAME);
    return columnList;
  }

  /**
   * Gets the column list for query.
   *
   * @return the column list for query
   */
  public static List<String> getColumnListForQuery() {
    List<String> columnList = new ArrayList<>();
    columnList.add(GEO_COL_NAME);
    columnList.add(GEO_COL_CENTROID);
    columnList.add(GEO_COL_COORDINATES);
    return columnList;
  }

  /**
   * Gets the column list for query.
   *
   * @param zoom the zoom
   * @return the column list for query
   */
  public static List<String> getColumnListForQuery(int zoom) {
    List<String> columnList = new ArrayList<>();
    columnList.add(GEO_COL_NAME);
    columnList.add(GEO_COL_CENTROID);
    columnList.add(GEO_COL_COORDINATES + zoom);
    return columnList;
  }

  /**
   * Creates the generic query.
   *
   * @param values the values
   * @param fieldNames the field names
   * @return the string
   */
  public static String createGenericQuery(List<String> values, String... fieldNames) {
    StringBuilder query = new StringBuilder();
    int index = 0;
    for (String value : values) {
      if (value != null) {
        query.append(fieldNames[index]).append("=").append(value);
        if (values.size() > index) {
          query.append("&");
        }
      }
      index++;
    }
    if (query.toString().endsWith("&")) {
      String queryString = query.toString();
      return queryString.substring(0, queryString.length() - 1);
    }

    return query.toString();
  }

  /**
   * Check lat lng in boundary.
   *
   * @param boundary the boundary
   * @param latLng the lat lng
   * @return the boolean
   */
  public static Boolean checkLatLngInBoundary(String boundary, LatLng latLng) {
    if (boundary != null) {
      List<List<List<Double>>> boundary3D =
          new Gson().fromJson(boundary, new TypeToken<List<List<List<Double>>>>() {}.getType());
      if (GeometryUtils.containsWithHole(boundary3D, latLng)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Gets the type code by geography level.
   *
   * @param geoLevel the geo level
   * @return the type code by geography level
   */
  public static String getTypeCodeByGeographyLevel(String geoLevel) {
    if (geoLevel.equalsIgnoreCase(L1_TYPE)) {
      return L1_CODE;
    } else if (geoLevel.equalsIgnoreCase(L2_TYPE)) {
      return L2_CODE;
    } else if (geoLevel.equalsIgnoreCase(L3_TYPE)) {
      return L3_CODE;
    } else if (geoLevel.equalsIgnoreCase(L4_TYPE)) {
      return L4_CODE;
    } else {
      return geoLevel;
    }
  }

  /**
   * Gets the reg ex for geography level.
   *
   * @param geoLevel the geo level
   * @return the reg ex for geography level
   */
  public static String getRegExForGeographyLevel(String geoLevel) {
    return getRegExForGeographyLevel(Symbol.EMPTY_STRING, geoLevel);
  }

  /**
   * Gets the reg ex for geography level.
   *
   * @param date the date
   * @param geoLevel the geo level
   * @return the reg ex for geography level
   */
  public static String getRegExForGeographyLevel(String date, String geoLevel) {
    date = StringUtils.isNotEmpty(date) ? date : Symbol.EMPTY_STRING;
    return "^.*" + date + getTypeCodeByGeographyLevel(geoLevel) + ".*$";
  }

  /**
   * Gets the scan by column list.
   *
   * @param columnList the column list
   * @return the scan by column list
   */
  public static Scan getScanByColumnList(List<String> columnList) {
    Scan scan = new Scan();
    for (String column : columnList) {
      if (column.contains(Symbol.COLON_STRING)) {
        String[] cfq = column.split(Symbol.COLON_STRING);
        scan.addColumn(Bytes.toBytes(cfq[COLUMN_FAMILY_INDEX]),
            Bytes.toBytes(cfq[COLUMN_QUALIFIER_INDEX]));
      }
    }
    return scan;
  }

  /**
   * Gets the geo cord column list.
   *
   * @return the geo cord column list
   */
  public static List<String> getGeoCordColumnList() {
    List<String> columnList = new ArrayList<>();
    columnList.add(GEO_COL_COORDINATES);
    return columnList;
  }

  /**
   * Gets the scan for geography.
   *
   * @return the scan for geography
   */
  public static Scan getScanForGeography() {
    Scan scan = new Scan();
    scan.addColumn(GEOGRAPHY_CF_BYTES, GEOGRAPHY_NAME_BYTES);
    scan.addColumn(GEOGRAPHY_CF_BYTES, COORDINATES_BYTES);
    scan.addColumn(GEOGRAPHY_CF_BYTES, MIN_LAT_BYTES);
    scan.addColumn(GEOGRAPHY_CF_BYTES, MIN_LNG_BYTES);
    scan.addColumn(GEOGRAPHY_CF_BYTES, MAX_LAT_BYTES);
    scan.addColumn(GEOGRAPHY_CF_BYTES, MAX_LNG_BYTES);
    return scan;
  }

  /**
   * Gets the scan with geography level filter.
   *
   * @param geoLevel the geo level
   * @return the scan with geography level filter
   */
  public static Scan getScanWithGeographyLevelFilter(String geoLevel) {
    Scan scan = getScanForGeography();
    updateGeoLevelFilter(geoLevel, scan);
    return scan;
  }

  /**
   * Gets the scan with geography level filter.
   *
   * @param geoLevel the geo level
   * @param date the date
   * @return the scan with geography level filter
   */
  public static Scan getScanWithGeographyLevelFilter(String geoLevel, String date) {
    Scan scan = getScanForGeography();
    String regex = GenericMapUtils.getRegExForGeographyLevel(date, geoLevel);
    scan.setFilter(new RowFilter(CompareOperator.EQUAL, new RegexStringComparator(regex)));
    return scan;
  }

  /**
   * Update geo level filter.
   *
   * @param geoLevel the geo level
   * @param scan the scan
   */
  public static void updateGeoLevelFilter(String geoLevel, Scan scan) {
    scan.addColumn(GEOGRAPHY_CF_BYTES, TYPE_BYTES);
    FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
    SingleColumnValueFilter filterType = new SingleColumnValueFilter(GEOGRAPHY_CF_BYTES, TYPE_BYTES,
        CompareOperator.EQUAL, Bytes.toBytes(geoLevel.toUpperCase()));
    filterList.addFilter(filterType);
    scan.setFilter(filterList);
  }

  /**
   * Gets the boundary.
   *
   * @param coordinateJson the string
   * @return the boundary
   */
  public static List<List<List<Double>>> getBoundary(String coordinateJson) {
    return new Gson().fromJson(coordinateJson,
        new TypeToken<List<List<List<Double>>>>() {}.getType());
  }

  /**
   * Gets the line.
   *
   * @param coordinateJson the coordinate json
   * @return the line
   */
  public static List<List<Double>> getLine(String coordinateJson) {
    return new Gson().fromJson(coordinateJson, new TypeToken<List<List<Double>>>() {}.getType());
  }

  /**
   * Gets the centroid.
   *
   * @param coordinateJson the string
   * @return the centroid
   */
  public static List<Double> getCentroid(String coordinateJson) {
    return new Gson().fromJson(coordinateJson, new TypeToken<List<Double>>() {}.getType());
  }

  /**
   * Check accuracy factor.
   *
   * @param accuracyFactor the accuracy factor
   * @return the double
   */
  public static Double checkAccuracyFactor(Double accuracyFactor) {
    if (accuracyFactor == null || accuracyFactor == GenericMapUtils.ZERO) {
      accuracyFactor = GenericMapUtils.DEFAULT_ACCURACY_FACTOR;
    }
    return accuracyFactor;
  }

  /**
   * Gets the polygon by string.
   * 
   * @param polyString the poly string
   * @return the polygon by string
   */
  public static String getPolygonByString(String polyString) {
    String[] latlonArray = polyString.split(Symbol.SPACE_STRING);
    StringBuilder pol = new StringBuilder();
    for (String latlon : latlonArray) {
      String[] strings = latlon.split(Symbol.COMMA_STRING);
      String lonlat = strings[1] + Symbol.SPACE_STRING + strings[0];
      pol.append(Symbol.COMMA_STRING);
      pol.append(lonlat);
    }

    return "POLYGON((" + pol.toString().replaceFirst(Symbol.COMMA_STRING, Symbol.EMPTY_STRING)
        + "))";
  }

  /**
   * Gets the geography cf bytes.
   *
   * @return the geography cf bytes
   */
  public static byte[] getGeographyCfBytes() {
    return GEOGRAPHY_CF_BYTES;
  }

  /**
   * Gets the geography name bytes.
   *
   * @return the geography name bytes
   */
  public static byte[] getGeographyNameBytes() {
    return GEOGRAPHY_NAME_BYTES;
  }

  /**
   * Gets the coordinates bytes.
   *
   * @return the coordinates bytes
   */
  public static byte[] getCoordinatesBytes() {
    return COORDINATES_BYTES;
  }

  /**
   * Gets the centroid bytes.
   *
   * @return the centroid bytes
   */
  public static byte[] getCentroidBytes() {
    return CENTROID_BYTES;
  }

  /**
   * Gets the min lat bytes.
   *
   * @return the min lat bytes
   */
  public static byte[] getMinLatBytes() {
    return MIN_LAT_BYTES;
  }

  /**
   * Gets the min lng bytes.
   *
   * @return the min lng bytes
   */
  public static byte[] getMinLngBytes() {
    return MIN_LNG_BYTES;
  }

  /**
   * Gets the max lat bytes.
   *
   * @return the max lat bytes
   */
  public static byte[] getMaxLatBytes() {
    return MAX_LAT_BYTES;
  }

  /**
   * Gets the max lng bytes.
   *
   * @return the max lng bytes
   */
  public static byte[] getMaxLngBytes() {
    return MAX_LNG_BYTES;
  }

  
  /**
   * Gets the type bytes.
   *
   * @return the type bytes
   */
  public static byte[] getTypeBytes() {
    return TYPE_BYTES;
  }
  
  

  public static byte[] getDisplayNameBytes() {
    return DISPLAY_NAME_BYTES;
  }
  
  

  public static byte[] getL1PkBytes() {
    return L1_PK_BYTES;
  }

  public static byte[] getL2PkBytes() {
    return L2_PK_BYTES;
  }

  public static byte[] getL3PkBytes() {
    return L3_PK_BYTES;
  }

  public static byte[] getL4PkBytes() {
    return L4_PK_BYTES;
  }

  public static List<Scan> getScanList(List<String> rowkeyPrefixList, List<String> columnList) {
    List<Scan> scans = new ArrayList<>();

    for (String prefix : rowkeyPrefixList) {
      Scan scan = new Scan();
      scan.setRowPrefixFilter(Bytes.toBytes(prefix));
      for (String column : columnList) {
        scan.addColumn(GenericMapUtils.GEOGRAPHY_CF.getBytes(),
            column.split(Symbol.COLON_STRING)[1].getBytes());
      }
      scans.add(scan);
    }

    return scans;
  }

}
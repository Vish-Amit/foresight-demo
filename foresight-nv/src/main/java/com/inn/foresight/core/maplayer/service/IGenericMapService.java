package com.inn.foresight.core.maplayer.service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.hadoop.hbase.client.Scan;
import com.inn.commons.hadoop.hbase.HBaseResult;
import com.inn.commons.maps.Corner;
import com.inn.commons.maps.LatLng;
import com.inn.commons.maps.tiles.Tile;
import com.inn.foresight.core.generic.wrapper.HBaseResponse;
import com.inn.foresight.core.infra.wrapper.GeographyWrapper;
import com.inn.foresight.core.maplayer.model.KPISummaryDataWrapper;
import com.inn.product.um.geography.model.GeographyL1;
import com.inn.product.um.geography.model.GeographyL2;
import com.inn.product.um.geography.model.GeographyL3;
import com.inn.product.um.geography.model.GeographyL4;

/** The Interface IGenericMapService. */
public interface IGenericMapService {

  /**
   * Gets the data from hbase.
   *
   * @param columnList the column list
   * @param tableName the table name
   * @param zoom the zoom
   * @param nELat the n E lat
   * @param nELng the n E lng
   * @param sWLat the s W lat
   * @param sWLng the s W lng
   * @param minDate the min date
   * @param maxDate the max date
   * @param postFix the post fix
   * @param gridColName for Aggregation
   * @param aggType for Aggregation Type
   * @return the data from hbase
   */
  String getDataFromHbase(List<String> columnList, String tableName, Integer zoom, Double nELat,
      Double nELng, Double sWLat, Double sWLng, String minDate, String maxDate, String postFix,
      String gridColName, String aggType);

  /**
   * Gets the image for kpiand zone.
   *
   * @param tableName the table name
   * @param tileId the tile id
   * @param image the image
   * @param kpi the kpi
   * @param date the date
   * @param siteStatus the site status
   * @param floor the floor
   * @param band the band
   * @param isBigTile the is big tile
   * @return the image for kpiand zone
   */
  byte[] getImageForKpiAndZone(String tableName, String tileId, String image, String kpi,
      String date, String siteStatus, String floor, String band, boolean isBigTile);

  /**
   * Gets the image for kpi and zone.
   *
   * @param tableName the table name
   * @param tileId the tile id
   * @param image the image
   * @param kpi the kpi
   * @param date the date
   * @param siteStatus the site status
   * @param floor the floor
   * @param band the band
   * @return the image for kpi and zone
   */
  byte[] getImageForKpiAndZone(String tableName, String tileId, String image, String kpi,
      String date, String siteStatus, String floor, String band);

  /**
   * Gets the image for kpi.
   *
   * @param tableName the table name
   * @param tileId the tile id
   * @param image the image
   * @param kpi the kpi
   * @param date the date
   * @return the image for kpi
   */
  BufferedImage getImageForKpi(String tableName, String tileId, String image, String kpi,
      String date);

  /**
   * Gets the big image for kpi and zone.
   *
   * @param tableName the table name
   * @param tileId the tile id
   * @param image the image
   * @param kpi the kpi
   * @param date the date
   * @param siteStatus the site status
   * @param floor the floor
   * @param band the band
   * @return the big image for kpi and zone
   */
  BufferedImage getBigImageForKpiAndZone(String tableName, String tileId, String image, String kpi,
      String date, String siteStatus, String floor, String band);

  /**
   * Gets the boundary data by geography names.
   *
   * @param geoGraphyNameList the geo graphy name list
   * @param tableName the table name
   * @param columnList the column list
   * @param area the area
   * @param type the type
   * @return the boundary data by geography names
   */
  List<Map<String, String>> getBoundaryDataByGeographyNames(List<String> geoGraphyNameList,
      String tableName, List<String> columnList, String area, String type);

  /**
   * Gets the geography L 4 lat lng.
   *
   * @param latitude the latitude
   * @param longitude the longitude
   * @return the geography L 4 lat lng
   */
  GeographyL4 getGeographyL4LatLng(Double latitude, Double longitude);

  /**
   * Gets the boundary data by geography names for MS.
   *
   * @param geoGraphyNameList the geo graphy name list
   * @param tableName the table name
   * @param columnList the column list
   * @param area the area
   * @param type the type
   * @return the boundary data by geography names
   */
  List<Map<String, String>> getBoundaryDataByGeographyNamesMS(List<String> geoGraphyNameList,
      String tableName, List<String> columnList, Double area, String type);

  /**
   * Gets the geography L 3 lat lng.
   *
   * @param latitude the latitude
   * @param longitude the longitude
   * @return the geography L 3 lat lng
   */
  GeographyL3 getGeographyL3LatLng(Double latitude, Double longitude);

  /**
   * Gets the data for table.
   *
   * @param tableName the table name
   * @param zoom the zoom
   * @param columnList the column list
   * @param nELat the n E lat
   * @param nELng the n E lng
   * @param sWLat the s W lat
   * @param sWLng the s W lng
   * @param minDate the min date
   * @param maxDate the max date
   * @param latestDataDate the latest data date
   * @param postFix the post fix
   * @param gridColName for Aggregation
   * @param aggType for Aggregation Type
   * @return the data for table
   * @throws IOException Signals that an I/O exception has occurred.
   */
  List<List<String>> getDataForTable(String tableName, Integer zoom, List<String> columnList,
      Double nELat, Double nELng, Double sWLat, Double sWLng, String minDate, String maxDate,
      Long latestDataDate, String postFix, String gridColName, String aggType) throws IOException;

  /**
   * Gets the geography L 2 lat lng.
   *
   * @param latitude the latitude
   * @param longitude the longitude
   * @return the geography L 2 lat lng
   */
  GeographyL2 getGeographyL2LatLng(Double latitude, Double longitude);

  /**
   * Gets the geography L 1 lat lng.
   *
   * @param latitude the latitude
   * @param longitude the longitude
   * @return the geography L 1 lat lng
   */

  GeographyL1 getGeographyL1LatLng(Double latitude, Double longitude);

  /**
   * Gets the nearest point by lat long.
   *
   * @param tableName the table name
   * @param lat the lat
   * @param lon the lon
   * @return the nearest point by lat long
   */
  String getNearestPointByLatLong(String tableName, Double lat, Double lon);

  /**
   * Gets the nearest point by lat lon.
   *
   * @param latLng the lat lng
   * @param geoLevel the geo level
   * @return the nearest point by lat lon
   */
  String getNearestPointByLatLon(LatLng latLng, String geoLevel);

  /**
   * Gets the scan for row range.
   *
   * @param rowKeys the row keys
   * @param columnList the column list
   * @return the scan for row range
   */
  Scan getScanForRowRange(List<String> rowKeys, List<String> columnList);

  /**
   * Gets the data for table for multi boundary.
   *
   * @param tableName the table name
   * @param zoom the zoom
   * @param columnList the column list
   * @param multiBoundary the multi boundary
   * @param minDate the min date
   * @param maxDate the max date
   * @return the data for table for multi boundary
   * @throws IOException Signals that an I/O exception has occurred.
   */
  List<List<String>> getDataForTableForMultiBoundary(String tableName, Integer zoom,
      List<String> columnList, List<List<List<Double>>> multiBoundary, String minDate,
      String maxDate) throws IOException;

  /**
   * Gets the data for table.
   *
   * @param tableName the table name
   * @param roeKey the roe key
   * @param columnName the column name
   * @return the data for table
   */
  byte[] getDataForTable(String tableName, String roeKey, String columnName);

  /**
   * Gets the coverage hole pre fix.
   *
   * @param sELat the s E lat
   * @param sELng the s E lng
   * @param nWLat the n W lat
   * @param nWLng the n W lng
   * @param zoom the zoom
   * @param date the date
   * @return the coverage hole pre fix
   * @throws ParseException the parse exception
   */
  List<String> getCoverageHolePreFix(Double sELat, Double sELng, Double nWLat, Double nWLng,
      Integer zoom, String date) throws ParseException;

  /**
   * Gets the coverage hole data.
   *
   * @param tableName the table name
   * @param zoom the zoom
   * @param columnList the column list
   * @param nWLat the n W lat
   * @param nWLng the n W lng
   * @param sELat the s E lat
   * @param sELng the s E lng
   * @param minDate the min date
   * @return the coverage hole data
   */
  List<List<String>> getCoverageHoleData(String tableName, Integer zoom, List<String> columnList,
      Double nWLat, Double nWLng, Double sELat, Double sELng, String minDate);

  /**
   * Gets the data from hbase rest.
   *
   * @param query the query
   * @return the data from hbase rest
   */
  HBaseResponse getDataFromHbaseRest(String query);

  /**
   * Gets the hbase data by row or time range.
   *
   * @param columnList the column list
   * @param tableName the table name
   * @param startRow the start row
   * @param endRow the end row
   * @param startTime the start time
   * @param endTime the end time
   * @return the hbase data by row or time range
   */
  String getHbaseDataByRowOrTimeRange(List<String> columnList, String tableName, String startRow,
      String endRow, Long startTime, Long endTime);

  /**
   * Gets the hbase data by row or time range.
   *
   * @param columnList the column list
   * @param startRow the start row
   * @param endRow the end row
   * @param tableName the table name
   * @param startTime the start time
   * @param endTime the end time
   * @return the hbase data by row or time range
   */
  List<Map<String, String>> getHbaseDataByRowOrTimeRangeList(List<String> columnList,
      String startRow, String endRow, String tableName, Long startTime, Long endTime);

  /**
   * Gets the nearest geography L 4 by lat lng.
   *
   * @param latitude the latitude
   * @param longitude the longitude
   * @return the nearest geography L 4 by lat lng
   */
  GeographyL4 getNearestGeographyL4ByLatLng(Double latitude, Double longitude);

  /**
   * Gets the geography data by point.
   *
   * @param columnList the column list
   * @param tableName the table name
   * @param lat the lat
   * @param lon the lon
   * @param isExact the is exact
   * @param subPath the sub path
   * @return the geography data by point
   */
  String getGeographyDataByPoint(List<String> columnList, String tableName, Double lat, Double lon,
      Boolean isExact, String subPath);

  /**
   * Gets the geography data by point.
   *
   * @param tableName the table name
   * @param columnList the column list
   * @param lat the lat
   * @param lon the lon
   * @param isExact the is exact
   * @param subPath the sub path
   * @return the geography data by point
   */
  List<List<String>> getGeographyDataByPoint(String tableName, List<String> columnList, Double lat,
      Double lon, Boolean isExact, String subPath);

  /**
   * Gets the all geography name.
   *
   * @param latitude the latitude
   * @param longitude the longitude
   * @return the all geography name
   */
  String getAllGeographyName(Double latitude, Double longitude);

  /**
   * Gets the all geography name.
   *
   * @param location the location
   * @return the all geography name
   */
  String[] getAllGeographyName(LatLng location);

  String getCustomerCareGeagraphyData(Double lat, Double lng);

  /**
   * Gets the customer care geography.
   *
   * @param location the location
   * @return the customer care geography
   */
  String[] getCustomerCareGeography(LatLng location);

  /**
   * Gets the facebook population.
   *
   * @param polygon the polygon
   * @return the facebook population
   */
  Double getFacebookPopulation(List<List<Double>> polygon, Integer zoomLevel);

  /**
   * Gets the image byte array.
   *
   * @param tableName the table name
   * @param tileId the tile id
   * @param imgColumn the img column
   * @param type the type
   * @param isBigTile is big tile
   * @param baseZoom the base zoom
   * @return the image byte array
   */

  byte[] getImageByteArray(GeographyWrapper geographyWrapper);

  /**
   * Gets the zone and tile id in map.
   *
   * @return the zone and tile id in map
   */
  Map<String, Integer> getZoneAndTileIdInMap();

  List<HBaseResult> getHbaseResultForPrefixList(String tableName, List<String> tileIdPrefixList,
      List<String> columns, String columnFamily);

  /**
   * Gets the population and height.
   *
   * @param latLng the lat lng
   * @param accFactor the acc factor
   * @param tableName the table name
   * @param zoomLevel the zoom level
   * @param maxRange the max range
   * @param minRange the min range
   * @return the population and height
   */
  Object getPopulationAndHeight(LatLng latLng, Double accFactor, String tableName,
      Integer zoomLevel, Integer maxRange, Integer minRange);

  /**
   * Update population and AMSL color.
   *
   * @param tileImage the tile image
   * @param tableName the table name
   * @param accuracyFactor the accuracy factor
   * @return the buffered image
   */
  BufferedImage updatePopulationAndAMSLColor(BufferedImage tileImage, String tableName,
      Double accuracyFactor);

  List<List<String>> getPointData(List<String> columnList, GeographyWrapper geographyWrapper)
      throws IOException;

  List<List<String>> getBoundaryDataForTable(String tableName, List<String> columnList,
      Double nWLat, Double nWLng, Double sELat, Double sELng, Boolean isExact, String subPath,
      Double area) throws IOException;

  byte[] getImageByteArray(String tableName, String tileId, String imgColumn, String type,
      Boolean isBigTile, Integer baseZoom);

  String getBoundaryData(List<String> columnList, String tableName, Double nELat, Double nELng,
      Double sWLat, Double sWLng, Boolean isExact, String subPath, String area);

  byte[] getTileImageData(String tableName, String tileId, String imgColumn, String type,
      Boolean isBigTile, Integer baseZoom, boolean isColorUpdation, Double accuracyFactor);

  List<Map<String, String>> getBoundaryDataByPKMS(List<Integer> geoGraphyPkList, String tableName,
      List<String> columnList, Double area, String type);

  Map<String, List<Tile>> getCommonTiles(Corner viewPort, List<Integer> geoGraphyPkList,
      String geoLevel, int zoomLevel);

  byte[] exportKML(String fileName, List<Map<String, Object>> placemarkList);

  Map<String, Set<Object>> getAllSalesGeography(String polygon);

  Map<String, Set<Object>> getAllNetworkGeography(String polygon);

  String getAllSalesGeographyNameByPolygon(List<List<Double>> polygon2d);

  Double getAccuracyFactor(int zoom, String tableName);

  LatLng getCentroidForUserInContext();

  List<Map<String, Object>> importKML(HttpServletRequest request);

  /**
   * Gets the fb population for kpi summary.
   *
   * @param kpiSummaryDataWrapper the kpi summary data wrapper
   * @param northEastLat the north east lat
   * @param northEastLong the north east long
   * @param southWestLat the south west lat
   * @param southWestLong the south west long
   * @param geographyType the geography type
   * @param zoomLevel the zoom level
   * @return the fb population for kpi summary
   */
  Map<String, Map<String, Object>> getFbPopulationForKpiSummary(
      KPISummaryDataWrapper kpiSummaryDataWrapper, Double northEastLat, Double northEastLong,
      Double southWestLat, Double southWestLong, String geographyType, Integer zoomLevel);

  /**
   * Gets the fb population for kpi summary MS.
   *
   * @param kpiSummaryDataWrapper the kpi summary data wrapper
   * @param northEastLat the north east lat
   * @param northEastLong the north east long
   * @param southWestLat the south west lat
   * @param southWestLong the south west long
   * @param geographyType the geography type
   * @param zoomLevel the zoom level
   * @return the fb population for kpi summary MS
   */
  Double getFbPopulationForKpiSummaryMS(KPISummaryDataWrapper kpiSummaryDataWrapper,
      Double northEastLat, Double northEastLong, Double southWestLat, Double southWestLong,
      String geographyType, Integer zoomLevel);

  List<Map<String, String>> getBoundaryByPk(
      com.inn.foresight.core.infra.wrapper.GeographyWrapper geoWrapper, String tableName,
      String area, String type);

  /**
   * Gets the all sales geography name.
   *
   * @param location the location
   * @return the all sales geography name
   */
  String[] getAllSalesGeographyName(LatLng location);

  /**
   * Gets the all sales geography name.
   *
   * @param latitude the latitude
   * @param longitude the longitude
   * @return the all sales geography name
   */
  String getAllSalesGeographyName(Double latitude, Double longitude);

  /**
   * Gets the all NW geography name by polygon.
   *
   * @param polygon2d the polygon 2 d
   * @return the all NW geography name by polygon
   */
  Map<String, Set<Object>> getAllNWGeographyNameByPolygon(List<List<Double>> polygon2d);

  Map<String, List<String>> getNonComplianceParameterOfSite(String rowKey, String tableName,
      String column);

  String findGeographyL1(Double lat, Double lng);
  
  Map<String,String> getRouteDataMS(GeographyWrapper geoWrapper);

  Map<String,String> getRouteData(GeographyWrapper geoWrapper);

  String findGeographyL2(Double lat, Double lng);

  String findGeographyL3(Double lat, Double lng);

  String findGeographyL4(Double lat, Double lng);

  Map<String, String[]> getGeographyNames(LatLng latLng);

  List<List<String>> getDataFromHBase(List<String> hbaseColumns, String tableName,
      String rowkeyPrefix) throws IOException;

  Scan getScanForSingleRowKeyPrefix(String rowKey, List<String> columns);

  public List<Map<String, String>> getBoundaryDataByRowkeyPrefix(GeographyWrapper geoWrapper,
      String tableName, String area);

  public List<Map<String, String>> getBoundaryDataByRowkeyPrefix(List<String> rowKeyPrefix,
      String tableName, List<String> columnList, Double area);

  String[] getAllGeographyDisplayName(LatLng location);

  String getAllGeographyDisplayName(Double latitude, Double longitude);
}

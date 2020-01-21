package com.inn.foresight.core.maplayer.dao;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Scan;
import com.inn.commons.hadoop.hbase.HBaseResult;
import com.inn.foresight.core.maplayer.wrapper.TileImageWrapper;

/**
 * The Interface IGenericMapDao.
 */
public interface IGenericMapDao {

  /**
   * Gets the result list for scan.
   *
   * @param scan the scan
   * @param tableName the table name
   * @param columnFamily the column family
   * @return the result list for scan
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public List<HBaseResult> getResultListForScan(Scan scan, String tableName, String columnFamily)
      throws IOException;

  /**
   * Gets the result for get.
   *
   * @param get the get
   * @param tableName the table name
   * @param columnFamily the column family
   * @return the result for get
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public HBaseResult getResultForGet(Get get, String tableName, String columnFamily)
      throws IOException;

  /**
   * Gets the result for get list.
   *
   * @param rowKeyGetList the row key get list
   * @param tableName the table name
   * @param columnFamily the column family
   * @return the result for get list
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public List<HBaseResult> getResultForGetList(List<Get> rowKeyGetList, String tableName,
      String columnFamily) throws IOException;

  /**
   * Gets the result for boundary data by geography names.
   *
   * @param geoGraphyNameList the geo graphy name list
   * @param tableName the table name
   * @param columnList the column list
   * @param area the area
   * @param type the type
   * @return the result for boundary data by geography names
   */
  public List<HBaseResult> getResultForBoundaryDataByGeographyNames(List<String> geoGraphyNameList,
      String tableName, List<String> columnList, Double area, String type);

  /**
   * Gets the buffered image map.
   *
   * @param getList the get list
   * @param tableName the table name
   * @param columnFamily the column family
   * @param columnQualifier the column qualifier
   * @param zoom the zoom
   * @return the buffered image map
   */
  Map<String, BufferedImage> getBufferedImageMap(List<Get> getList, String tableName,
      byte[] columnFamily, byte[] columnQualifier, int zoom);

  /**
   * Gets the result for boundary data by rowkey prefixes.
   *
   * @param rowKeyPrefixList the row key prefix list
   * @param columnList the column list
   * @param tableName the table name
   * @param filterList the filter list
   * @return the result for boundary data by rowkey prefixes
   */
  public List<HBaseResult> getResultForBoundaryDataByRowkeyPrefixes(List<String> rowKeyPrefixList,
      List<String> columnList, String tableName);

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
  List<HBaseResult> getHbaseDataByRowOrTimeRange(List<String> columnList, String startRow,
      String endRow, String tableName, Long startTime, Long endTime);

  /**
   * Gets the h base data from get list.
   *
   * @param getList the get list
   * @param tableName the table name
   * @param column the column
   * @param columnFamily the column family
   * @return the h base data from get list
   */
  List<TileImageWrapper> getHBaseDataFromGetList(List<Get> getList, String tableName, String column,
      String columnFamily);

  /**
   * Gets the result list for scan.
   *
   * @param scan the scan
   * @param tableName the table name
   * @return the result list for scan
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public List<HBaseResult> getResultListForScan(Scan scan, String tableName) throws IOException;

  List<HBaseResult> getResultDataByRowkeyPrefixes(String rowKeyPrefixList, List<String> columnList,
      String tableName);

  // public List<HBaseResult> getHbaseResultListForScanList(String tableName, String columnFamily,
  // List<Scan> scanList, List<HBaseResult> hbaseResult) throws IOException;
}

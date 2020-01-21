package com.inn.foresight.core.maplayer.dao.impl;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.hadoop.hbase.CompareOperator;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.inn.commons.Symbol;
import com.inn.commons.hadoop.hbase.HBaseResult;
import com.inn.commons.io.image.ImageUtils;
import com.inn.commons.maps.tiles.Tile;
import com.inn.commons.maps.tiles.TileRelationship;
import com.inn.foresight.core.generic.dao.impl.hbase.AbstractHBaseDao;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.infra.dao.BoundaryDataDao;
import com.inn.foresight.core.maplayer.dao.IGenericMapDao;
import com.inn.foresight.core.maplayer.utils.GenericMapUtils;
import com.inn.foresight.core.maplayer.wrapper.TileImageWrapper;

/**
 * The Class GenericMapDaoImpl.
 */
@Repository("GenericMapDaoImpl")
public class GenericMapDaoImpl extends AbstractHBaseDao implements IGenericMapDao {

  /** The Constant logger. */
  private static final Logger logger = LogManager.getLogger(GenericMapDaoImpl.class);

  @Autowired
  private BoundaryDataDao boundaryDataDao;

  /**
   * Gets the result list for scan.
   *
   * @param scan the scan
   * @param tableName the table name
   * @param columnFamily the column family
   * @return the result list for scan
   * @throws IOException Signals that an I/O exception has occurred.
   */
  @Override
  public List<HBaseResult> getResultListForScan(Scan scan, String tableName, String columnFamily)
      throws IOException {
    return super.scanResultByPool(scan, tableName, columnFamily.getBytes());
  }

  /**
   * Gets the result list for scan.
   *
   * @param scan the scan
   * @param tableName the table name
   * @return the result list for scan
   * @throws IOException Signals that an I/O exception has occurred.
   */
  @Override
  public List<HBaseResult> getResultListForScan(Scan scan, String tableName) throws IOException {
    return super.scanResultByPool(scan, tableName, GenericMapUtils.GEOGRAPHY_CF.getBytes());
  }

  /**
   * Gets the result for get
   * 
   * @param get the get
   * @param tableName the table name
   * @param columnFamily the column family
   * @return the result for get
   * @throws IOException Signals that an I/O exception has occurred.
   * 
   */
  @Override
  public HBaseResult getResultForGet(Get get, String tableName, String columnFamily)
      throws IOException {
    return super.getResultByPool(get, tableName, columnFamily.getBytes());
  }

  /**
   * Gets the result for get list.
   *
   * @param rowKeyGetList the row key get list
   * @param tableName the table name
   * @param columnFamily the column family
   * @return the result for get list
   * @throws IOException Signals that an I/O exception has occurred.
   */
  @Override
  public List<HBaseResult> getResultForGetList(List<Get> rowKeyGetList, String tableName,
      String columnFamily) throws IOException {
    return super.getResultByPool(rowKeyGetList, tableName, columnFamily.getBytes());
  }

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
  @Override
  public List<HBaseResult> getResultForBoundaryDataByGeographyNames(List<String> geoGraphyNameList,
      String tableName, List<String> columnList, Double area, String type) {
    List<HBaseResult> resultList = new ArrayList<>();
    try {
      logger.info("geoGraphyNameList size {}", geoGraphyNameList.size());
      for (String geographyName : geoGraphyNameList) {
        FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
        SingleColumnValueFilter filter = new SingleColumnValueFilter(
            GenericMapUtils.GEOGRAPHY_CF.getBytes(), GenericMapUtils.GEOGRAPHY_NAME.getBytes(),
            CompareOperator.EQUAL, Bytes.toBytes(geographyName));
        filter.setFilterIfMissing(true);
        filterList.addFilter(filter);
        if (!columnList.contains(GenericMapUtils.GEO_COL_NAME)) {
          columnList.add(GenericMapUtils.GEO_COL_NAME);
        }
        Scan scan = GenericMapUtils.getScanByColumnList(columnList);
        if (type != null) {
          String date = boundaryDataDao.getLatestBoundaryDate(tableName, type);
          String regex = GenericMapUtils.getRegExForGeographyLevel(date, type);
          RowFilter rowFilter =
              new RowFilter(CompareOperator.EQUAL, new RegexStringComparator(regex));
          filterList.addFilter(rowFilter);
        }
        scan.setFilter(filterList);
        List<HBaseResult> scanResultByPool =
            scanResultByPool(scan, tableName, GenericMapUtils.GEOGRAPHY_CF.getBytes());
        resultList.addAll(scanResultByPool);
      }
    } catch (Exception e) {
      logger.error("Exception while fetching boundary data by geographyName due to {}",
          ExceptionUtils.getStackTrace(e));
    }
    return resultList;
  }

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
  @Override
  public Map<String, BufferedImage> getBufferedImageMap(List<Get> getList, String tableName,
      byte[] columnFamily, byte[] columnQualifier, int zoom) {
    Map<String, BufferedImage> tileImageMap = new HashMap<>();
    try {
      List<HBaseResult> resultList = getResultByPool(getList, tableName, columnFamily);
      if (resultList != null) {
        for (HBaseResult result : resultList) {
          if (result != null && Bytes.toString(result.getRow()) != null) {
            byte[] value = result.getValue(columnQualifier);
            List<Tile> tileIdList = getTilesListFromRowKey(Bytes.toString(result.getRow()), zoom);
            for (Tile tileId : tileIdList) {
              tileImageMap.put(tileId.getIdWithZoom(), ImageUtils.toBufferedImage(value));
            }
          }
        }
      }
    } catch (Exception e) {
      logger.error("error while getting record from hbase getBufferedImageMap {}",
          ExceptionUtils.getStackTrace(e));
    }
    return tileImageMap;
  }

  /**
   * Gets the tile id from row key.
   *
   * @param rowKey the row key
   * @param zoom the zoom
   * @return the tile id from row key
   */
  private List<Tile> getTilesListFromRowKey(String rowKey, int zoom) {
    List<Tile> tileIdList = new ArrayList<>();
    String substring = rowKey.substring(ForesightConstants.TEN);
    StringBuilder tileId = new StringBuilder();
    String[] splitArray = substring.split("(?<=\\G......)");
    tileId.append(Integer.parseInt(splitArray[0]));
    tileId.append(Symbol.UNDERSCORE_STRING);
    tileId.append(Integer.parseInt(splitArray[1]));
    tileId.append(Symbol.UNDERSCORE_STRING);
    tileId.append(zoom);

    if (zoom > ForesightConstants.FIFTEEN) {
      String[] tileArray = tileId.toString().split(Symbol.UNDERSCORE_STRING);
      Tile parentTile = new Tile(tileArray[0] + Symbol.UNDERSCORE_STRING + tileArray[1]
          + Symbol.UNDERSCORE_STRING + ForesightConstants.FIFTEEN);
      tileIdList = TileRelationship.getBufferedChildren(parentTile, zoom, ForesightConstants.ZERO);
    } else {
      tileIdList.add(new Tile(tileId.toString()));
    }
    return tileIdList;
  }

  /**
   * Gets the result for boundary data by rowkey prefixes.
   *
   * @param rowKeyPrefixList the row key prefix list
   * @param columnList the column list
   * @param tableName the table name
   * @return the result for boundary data by rowkey prefixes
   */
  @Override
  public List<HBaseResult> getResultForBoundaryDataByRowkeyPrefixes(List<String> rowKeyPrefixList,
      List<String> columnList, String tableName) {
    try {
      List<Scan> scans = GenericMapUtils.getScanList(rowKeyPrefixList, columnList);
      return scanResultByPool(scans, tableName, GenericMapUtils.GEOGRAPHY_CF.getBytes());

    } catch (Exception e) {
      logger.error("Exception while fetching boundary data by rowKey prefixes due to {}",
          ExceptionUtils.getStackTrace(e));
    }
    return Collections.emptyList();
  }

  /**
   * Gets the rowkey prefix filter.
   *
   * @param rowKeyPrefixList the row key prefix list
   * @return the rowkey prefix filter
   */
  private FilterList getRowkeyPrefixFilter(List<String> rowKeyPrefixList) {
    FilterList rowKeyPrefixFilter = new FilterList(FilterList.Operator.MUST_PASS_ONE);
    for (String prefix : rowKeyPrefixList) {
      PrefixFilter prefixFilter = new PrefixFilter(Bytes.toBytes(prefix));
      rowKeyPrefixFilter.addFilter(prefixFilter);
    }
    return rowKeyPrefixFilter;
  }

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
  @Override
  public List<HBaseResult> getHbaseDataByRowOrTimeRange(List<String> columnList, String startRow,
      String endRow, String tableName, Long startTime, Long endTime) {
    List<HBaseResult> resultList = new ArrayList<>();
    logger.info(
        "inside getHbaseDataByRowAndTimeRange columnList {} tableName {} startTime {} endTime {}",
        tableName, startTime, endTime);
    Scan scan = new Scan();
    try {
      Set<String> columnSet = new HashSet<>(columnList);
      String columnFamily = null;
      for (String column : columnSet) {
        scan.addColumn(column.split(Symbol.COLON_STRING)[0].getBytes(),
            column.split(Symbol.COLON_STRING)[1].getBytes());
        columnFamily = column.split(Symbol.COLON_STRING)[0];
      }
      if (startRow != null && endRow != null) {
        scan.setStartRow(startRow.getBytes());
        scan.setStopRow(endRow.getBytes());
      }
      if (startTime != null && endTime != null && columnFamily != null) {
        scan.setTimeRange(startTime, endTime);
        resultList = scanResultByPool(scan, tableName, columnFamily.getBytes());
        logger.info("Result size {}", resultList.size());
      }
    } catch (Exception e) {
      logger.error("Error occured in getHbaseDataByRowAndTimeRange {}",
          ExceptionUtils.getStackTrace(e));
    }
    return resultList;
  }

  /**
   * Gets the h base data from get list.
   *
   * @param getList the get list
   * @param tableName the table name
   * @param column the column
   * @param columnFamily the column family
   * @return the h base data from get list
   */
  @Override
  public List<TileImageWrapper> getHBaseDataFromGetList(List<Get> getList, String tableName,
      String column, String columnFamily) {
    List<TileImageWrapper> imageWrapperList = new ArrayList<>();

    List<HBaseResult> resultList = null;
    try {
      resultList = getResultByPool(getList, tableName, Bytes.toBytes(columnFamily));
    } catch (IOException e) {
      logger.error("Getting error in getHBaseDataFromGetList with trace {}",
          ExceptionUtils.getStackTrace(e));
    }
    for (HBaseResult result : resultList) {
      TileImageWrapper imagesWrapper = new TileImageWrapper();
      BufferedImage image = null;
      try {
        byte[] imgBytes = result.getValue(Bytes.toBytes(column));
        if (imgBytes != null) {
          String rowkey = result.getRowKey();
          String substring = rowkey.substring(ForesightConstants.TEN);
          String[] splitArray = substring.split("(?<=\\G......)");
          int[] tileIdArray = (int[]) ConvertUtils.convert(splitArray, Integer.TYPE);
          image = ImageUtils.toBufferedImage(imgBytes);
          imagesWrapper.setTileId(tileIdArray);
          imagesWrapper.setImage(image);
          imageWrapperList.add(imagesWrapper);
        }
      } catch (IOException e) {
        logger.error("Getting error in getHBaseDataFromGetList with trace {}",
            ExceptionUtils.getStackTrace(e));
      }
    }
    return imageWrapperList;
  }

  @Override
  public List<HBaseResult> getResultDataByRowkeyPrefixes(String rowKeyPrefixList,
      List<String> columnList, String tableName) {
    List<HBaseResult> resultList = new ArrayList<>();
    Scan scan = new Scan();
    try {
      scan.setRowPrefixFilter(Bytes.toBytes(rowKeyPrefixList));
      for (String column : columnList) {
        scan.addColumn(GenericMapUtils.GEOGRAPHY_CF.getBytes(),
            column.split(Symbol.COLON_STRING)[1].getBytes());
      }
      List<HBaseResult> results =
          scanResultByPool(scan, tableName, GenericMapUtils.GEOGRAPHY_CF.getBytes());
      for (HBaseResult result : results) {
        resultList.add(result);
      }
      return resultList;
    } catch (Exception e) {
      logger.error("Exception while fetching boundary data by rowKey prefixes due to {}",
          ExceptionUtils.getStackTrace(e));
    }
    return resultList;
  }
}

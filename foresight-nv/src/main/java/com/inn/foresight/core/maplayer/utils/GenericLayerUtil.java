package com.inn.foresight.core.maplayer.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.FilterList.Operator;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.inn.commons.index.SpatialIndex;
import com.inn.commons.lang.StringUtils;
import com.inn.commons.maps.Corner;
import com.inn.commons.maps.tiles.Tile;
import com.inn.commons.maps.tiles.TileBoundaryUtils;
import com.inn.foresight.core.generic.utils.Constants;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;

public class GenericLayerUtil {

  private static final Logger logger = LogManager.getLogger(GenericLayerUtil.class);


  public static final Integer TILE_BASE_ZOOM = 15;

  /**
   * Instantiates a new generic layer util.
   */
  private GenericLayerUtil() {

  }

  /**
   * Create HBase RowKey Prefix
   * 
   * @param sELat
   * @param nWLng
   * @param nWLat
   * @param sELng
   * @param zoom
   * @param minDate
   * @param maxDate
   * @return
   * @throws ParseException
   */
  public static List<String> getPreFix(Double sELat, Double sELng, Double nWLng, Double nWLat,
      Integer zoom, String minDate, String maxDate) throws ParseException {
    List<String> rowKeys = getPrefixListFromViewPort(sELat, sELng, nWLng, nWLat, zoom);
    if (minDate != null && maxDate != null) {
      rowKeys = getPrefixForDateRange(rowKeys, minDate, maxDate);
    }
    return rowKeys;
  }


  private static List<String> getPrefixListFromViewPort(Double southWestLat, Double southWestLon,
      Double northEastLat, Double northEastLon, Integer zoom) {

    Corner corner = new Corner(southWestLat, southWestLon, northEastLat, northEastLon);
    List<String> spatialList = new ArrayList<>();
    List<Tile> tileList = TileBoundaryUtils.getTiles(corner, zoom);
    List<String> gridList = Utils.getPaddedTileIdWithzoom(tileList);
    for (String string : gridList) {
      spatialList.add(SpatialIndex.getSpatialIndex(string));
    }
    return spatialList;
  }

  /**
   * Get Date wise HBase row key
   * 
   * @param rowKeys
   * @param minDate
   * @param maxDate
   * @return
   * @throws ParseException
   */
  private static List<String> getPrefixForDateRange(List<String> rowKeys, String minDate,
      String maxDate) throws ParseException {
    List<String> updateList = new ArrayList<>();
    DateFormat format = new SimpleDateFormat(Constants.DDMMYY);
    long minValue = format.parse(minDate).getTime();
    long maxValue = format.parse(maxDate).getTime();
    while (minValue <= maxValue) {
      String date = format.format(new Date(minValue));
      for (String crc : rowKeys) {
        updateList.add(crc + ForesightConstants.HIPHEN + date + ForesightConstants.HIPHEN);
      }
      minValue = DateUtils.addDays(new Date(minValue), ForesightConstants.ONE).getTime();
    }
    return updateList;
  }

  public static Scan createScanForRequest(List<String> rowKeys, List<String> columnList) {
    final FilterList filterList = new FilterList(Operator.MUST_PASS_ONE);
    Scan scan = new Scan();
    for (String columnName : columnList) {
      if (columnName.contains(ForesightConstants.COLON)) {
        String[] cellValues = columnName.split(ForesightConstants.COLON);
        scan.addColumn(Bytes.toBytes(cellValues[ForesightConstants.ZERO]),
            Bytes.toBytes(cellValues[1]));
      }
    }
    for (final String rkPrefix : rowKeys) {
      filterList.addFilter(new PrefixFilter(Bytes.toBytes(rkPrefix)));
    }
    scan.setFilter(filterList);

    return scan;

  }

  /**
   * Get Zoom level from TileId
   * 
   * @param tileId
   * @return
   */
  public static Integer getZoomFromTileId(String tileId) {
    String[] axis = tileId.split(ForesightConstants.UNDERSCORE);
    if (axis.length == ForesightConstants.THREE) {
      return Integer.valueOf(axis[ForesightConstants.TWO]);
    }
    return null;
  }

  /**
   * Get Axis from TileId
   * 
   * @param tileId
   * @return
   */
  public static int[] getAxisFromTileId(String tileId) {
    String[] axis = tileId.split(ForesightConstants.UNDERSCORE);
    int[] axisValue = new int[axis.length];
    for (Integer index = 0; axis.length > index; index++) {
      axisValue[index] = Integer.valueOf(axis[index]);
    }
    return axisValue;
  }


  /**
   * create CRC of a String
   * 
   * @param tileId
   * @return
   */
  public static String getCrcOfString(String value) {
    return SpatialIndex.getSpatialIndex(value);
  }

  public static String getPaddedCrc(String tileId) {
    return StringUtils.leftPad(GenericLayerUtil.getCrcOfString(tileId), ForesightConstants.TEN,
        ForesightConstants.ZERO_STRING);
  }

  /**
   * Get tileId based on view port and zoom level
   * 
   * @param southWestLat
   * @param southWestLon
   * @param northEastLat
   * @param northEastLon
   * @param zoom
   * @return
   */
  public static List<String> getTileIdFromViewPort(Double southWestLat, Double southWestLon,
      Double northEastLat, Double northEastLon, Integer zoom) {
    Corner corner = new Corner(southWestLat, southWestLon, northEastLat, northEastLon);
    List<Tile> tileList = TileBoundaryUtils.getTiles(corner, zoom);
    return Utils.getPaddedTileIdWithzoom(tileList);
  }

  /**
   * Get tileId based on view port and zoom level without zoom appended
   * 
   * @param southWestLat
   * @param southWestLon
   * @param northEastLat
   * @param northEastLon
   * @param zoom
   * @return
   */
  public static List<String> getTileIdWithoutZoomFromViewPort(Double southWestLat,
      Double southWestLon, Double northEastLat, Double northEastLon, Integer zoom,
      Boolean isZoomRequired) {
    logger.info("inside getTileIdFromViewPort --------- isZoomRequired {}", isZoomRequired);
    Corner corner = new Corner(southWestLat, southWestLon, northEastLat, northEastLon);
    List<Tile> tileList = TileBoundaryUtils.getTiles(corner, zoom);
    List<String> gridList;
    if (isZoomRequired) {
      gridList = Utils.getTileIdWithZoom(tileList);
    } else {
      gridList = Utils.getTileId(tileList);
    }
    return gridList;
  }

  /**
   * Gets the tile id from multi boundary.
   *
   * @param multiBoundary the multi boundary
   * @param zoom the zoom
   * @return the tile id from multi boundary
   */
  public static List<String> getTileIdFromMultiBoundary(List<List<List<Double>>> multiBoundary,
      Integer zoom) {
    List<Tile> tileList = TileBoundaryUtils.getExactTilesForMultiBoundary(multiBoundary, zoom);
    return Utils.getPaddedTileIdWithzoom(tileList);
  }

}


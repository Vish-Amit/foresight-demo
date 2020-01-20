package com.inn.foresight.module.nv.layer;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.inn.commons.Symbol;
import com.inn.commons.hadoop.hbase.HBaseResult;
import com.inn.commons.maps.Corner;
import com.inn.commons.maps.LatLng;
import com.inn.commons.maps.tiles.Tile;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.maplayer.utils.GenericLayerUtil;
import com.inn.foresight.core.maplayer.utils.GenericMapUtils;
import com.inn.foresight.module.nv.layer.constants.LayerPlotConstants;

public class LayerPlotUtils {
	
	public static final Logger logger = LogManager.getLogger(LayerPlotUtils.class);
	
	private LayerPlotUtils() {
		super();
	}

	/**
	 * @param minDate
	 * @param maxDate
	 * @param tileIdPrefixFullynPartially
	 * @param scopeWiseTiles
	 * @param postFix 
	 * @return 
	 * @throws ParseException
	 */
	public static Map<String, List<String>> getPrefixMap(String minDate, String maxDate,
			 Map<String, List<Tile>> scopeWiseTiles, String postFix)
			throws ParseException {
		Map<String, List<String>> prefixMap = new HashMap<>();
		for (Entry<String, List<Tile>> entry : scopeWiseTiles.entrySet()) {
			List<String> prefixList = new ArrayList<>();
			List<String> tileList = Utils.getPaddedTileIdWithzoom(entry.getValue());
			for (String tile : tileList) {
				String prefixKey = GenericLayerUtil.getPaddedCrc(tile);
				if (Utils.hasValidValue(minDate) && Utils.hasValidValue(maxDate)) {
					getPrefixForDateRange(prefixList, minDate, maxDate, prefixKey, postFix);
				} else {
					prefixList.add(Utils.hasValidValue(postFix) ? prefixKey + postFix : prefixKey);
				}
			}
			prefixMap.put(entry.getKey(), prefixList);
		}
		return prefixMap;
	}
	
	/**
	 * @param rowKeys
	 * @param minDate
	 * @param maxDate
	 * @param prefix
	 * @param postFix
	 * @return
	 * @throws ParseException
	 */
	public static List<String> getPrefixForDateRange(List<String> rowKeys, String minDate, String maxDate, String prefix, String postFix)
			throws ParseException {
		 DateFormat format = new SimpleDateFormat(ForesightConstants.DDMMYY);
		long minValue = format.parse(minDate).getTime();
		long maxValue = format.parse(maxDate).getTime();
		while (minValue <= maxValue) {
			String date = format.format(new Date(minValue));
			date = new StringBuffer(date).deleteCharAt(ForesightConstants.FOUR).toString();
			rowKeys.add(Utils.hasValidValue(postFix) ? prefix + date + postFix : prefix + date);
			minValue = DateUtils.addDays(new Date(minValue), ForesightConstants.ONE).getTime();
		}
		return rowKeys;
	}

	/**
	 * @param hbaseColumnList
	 * @param gridColNames
	 */
	public static void getGridColumns(List<String> hbaseColumnList, String gridColNames) {
		if (Utils.hasValue(gridColNames)) {
			String[] gridColumnNames = gridColNames.split(ForesightConstants.COMMA);
			for (String column : gridColumnNames) {
				hbaseColumnList.add(getDesiredPattern(column));
			}
		}
	}

	/**
	 * @param column
	 * @return
	 */
	private static String getDesiredPattern(String column) {
		return GenericMapUtils.GEOGRAPHY_CF + ForesightConstants.COLON + column +  ForesightConstants.COLON + LayerPlotConstants.AVERAGE;
	}
	
	/**
	 * @param viewPort
	 * @param bottomRightLat
	 * @param bottomRightLong
	 * @param topLeftLat
	 * @param topLeftLong
	 * @return
	 */
	public static Boolean checkGridInsideViewPort(Corner viewPort,Double bottomRightLat,Double bottomRightLong,Double topLeftLat,
			Double topLeftLong)
	{
		if (bottomRightLat != null && bottomRightLong != null) {
			Corner gridCorner = new Corner(new LatLng(topLeftLat, topLeftLong),
					new LatLng(bottomRightLat, bottomRightLong));
			LatLng bottomLeftPosition = gridCorner.getBottomLeftPosition();
			LatLng bottomRightPosition = gridCorner.getBottomRightPosition();
			LatLng topLeftPosition = gridCorner.getTopLeftPosition();
			LatLng topRightPosition = gridCorner.getTopRightPosition();
			if (isContains(viewPort, bottomLeftPosition) || isContains(viewPort, bottomRightPosition)
					|| isContains(viewPort, topLeftPosition) || isContains(viewPort, topRightPosition)) {
				return true;
			}
		}
		return false;
	}

	
	/**
	 * @param corner
	 * @param point
	 * @return
	 */
	public static boolean isContains(Corner corner, LatLng point) {
		if (corner.getMinLatitude() < point.getLatitude() && corner.getMaxLatitude() > point.getLatitude()
				&& corner.getMinLongitude() < point.getLongitude() && corner.getMaxLongitude() > point.getLongitude()) {
			return true;
		}
		return false;
	}
	
	public static String roundDoubleValue(Double value) {
		DecimalFormat df=new DecimalFormat("#.####");
		return value!=null?df.format(value):null;		
	}
	
	public static List<HBaseResult> filterViewPortBasisData(List<HBaseResult> hbaseResultList,
			String filterColName, Corner corner) {
		List<HBaseResult> filteredList = new ArrayList<>();
		if (Utils.hasValue(filterColName)) {
			String[] filterColNames = filterColName.split(ForesightConstants.COMMA);
			String lat = filterColNames[0];
			String lon = filterColNames[1];
			 Boolean isDataAddIntoList=true;
			 Double nELat = corner.getNorthEastLat();
			 Double sWLat = corner.getSouthWestLat();
			 Double nElng = corner.getNorthEastLon();
			 Double sWLng = corner.getSouthWestLon();
			logger.info("Before Filter Data result size {}",hbaseResultList.size());
			for (HBaseResult result : hbaseResultList) {
				Double latitude = result.getStringAsDouble(lat);
				Double longitude = result.getStringAsDouble(lon);
				isDataAddIntoList = true;
		          if (latitude != null) {
		            if (latitude > nELat || latitude < sWLat) {
		              isDataAddIntoList = false;
		            }
		          }
		          if (longitude != null) {
		            if (longitude > nElng || longitude < sWLng) {
		              isDataAddIntoList = false;
		            }
		          }
		          if (isDataAddIntoList) {
		        	  filteredList.add(result);
		            }
			}
			logger.info("After Filter Data result size {}",filteredList.size());
		}
		return filteredList;
	}
	
	public static List<List<String>> convertResultToColumnData(List<HBaseResult> results,
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
}

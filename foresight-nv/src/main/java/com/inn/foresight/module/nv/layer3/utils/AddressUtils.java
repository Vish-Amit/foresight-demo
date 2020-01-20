package com.inn.foresight.module.nv.layer3.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.inn.commons.Symbol;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.maps.LatLng;
import com.inn.commons.maps.grid.DegreeGrid;
import com.inn.commons.maps.rowkey.RowKeyUtils;
import com.inn.foresight.core.generic.utils.ConfigUtil;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.layer3.constants.QMDLConstant;


public class AddressUtils {

	private static final Logger logger = LogManager.getLogger(AddressUtils.class);

	public static String getRowKeyForAddress(Double lat,Double lon){
		LatLng grid = getGridLatLong(lat, lon);
		Integer zoomLevel=ConfigUtils.getInteger(ConfigUtil.ZOOM_LEVEL_FOR_ADDRESS);
		return RowKeyUtils.getRowKeyForBin(grid, zoomLevel);
	}

	public static LatLng getGridLatLong(Double lat, Double lon) {
		Double refLat=ConfigUtils.getDouble(ConfigUtil.REFRENCE_LATITUDE_FOR_ADDRESS);
		Double refLon=ConfigUtils.getDouble(ConfigUtil.REFRENCE_LONGTITUDE_FOR_ADDRESS);
		Integer gridSize=ConfigUtils.getInteger(ConfigUtil.GRID_SIZE_FOR_ADDRESS);
		return new DegreeGrid(gridSize, new LatLng(refLat, refLon)).getGrid(new LatLng(lat, lon));
	}


	public static String getRowKeyFromGridLatLongForAddress(LatLng grid){
		Integer zoomLevel=ConfigUtils.getInteger(ConfigUtil.ZOOM_LEVEL_FOR_ADDRESS);
		return RowKeyUtils.getRowKeyForBin(grid, zoomLevel);
	}

	/**
	 * Read data.
	 *
	 * @param rd
	 *            the rd
	 * @return the string
	 */
	public static String readData(InputStream rd) {
		try {
			return IOUtils.toString(rd, StandardCharsets.UTF_8);
		} catch (IOException e) {
			logger.error("error in reading data: {} ", ExceptionUtils.getStackTrace(e));
		} finally {
			IOUtils.closeQuietly(rd);
		}
		return null;
	}



	/**
	 * Gets the address by lat lon.
	 *
	 * @param latitute
	 *            the latitute
	 * @param longitute
	 *            the longitute
	 * @return the address by lat lon
	 */
	public static String getAddressByLatLon(Double latitute, Double longitute) {
		String url = getUrlFromLatlong(latitute, longitute);
		try {
			String msg = readData(new URL(url).openStream());
			return getaddressFromJSONObject(msg);
		} catch (Exception e) {
			logger.warn("getLocationAddress getting Error-{}" , Utils.getStackTrace(e));
		}
		return null;
	}

	private static String getUrlFromLatlong(Double latitute, Double longitute) {
		String url=ConfigUtils.getString(QMDLConstant.GOOGLE_ADDRESS_API);
		url=url.replace(QMDLConstant.LATITUDE_API_STRING, String.valueOf(latitute));
		url=url.replace(QMDLConstant.LONGITUDE_API_STRING, String.valueOf(longitute));
		return url.replace(Symbol.UNDERSCORE, Symbol.COMMA);
	}

	/**
	 * Gets the address from JSON object.
	 *in
	 * @param addressJson
	 *            the address json
	 * @return the address from JSON object
	 */
	private static String getaddressFromJSONObject(String addressJson) {
		if (addressJson != null) {
			try {
				org.json.JSONObject jsonObject = new org.json.JSONObject(addressJson);
				org.json.JSONObject location = jsonObject.getJSONArray(QMDLConstant.ADDRESS_RESULT_JSON_KEY)
						.getJSONObject(QMDLConstant.FORMATTED_JSON_INDEX);
				return location.getString(QMDLConstant.FOREMATTED_ADDRESS_JSON_KEY);
			} catch (org.json.JSONException e) {
				logger.debug("Error in converting address {}  Error {}  ", addressJson, Utils.getStackTrace(e));
			}
		}
		return null;
	}


}


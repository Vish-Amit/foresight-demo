package com.inn.foresight.module.nv.wpt.analytics.utils;

import java.util.Arrays;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.lang.StringUtils;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.module.nv.core.generic.utils.hb.HBaseProperties;
import com.inn.foresight.module.nv.wpt.analytics.constants.WPTHbaseConstants;
import com.inn.product.security.utils.AuthenticationCommonUtil;


/** The Class WPTHbaseUtils. */
public class WPTHbaseUtils extends WPTHbaseConstants implements HBaseProperties {

	
	public WPTHbaseUtils() {
		super();
		
	}

	/** The counter. */
	static int counter = 0;
	
	/** The logger. */
	private static Logger logger = LogManager.getLogger(WPTHbaseUtils.class);
	
	/**
	 * Gets the decrypted data.
	 *
	 * @param csvSeparatorCounter the csv separator counter
	 * @param data the data
	 * @return the decrypted data
	 */
	public static String getDecryptedData(int csvSeparatorCounter, String data) {
		logger.info("csvSeparatorCounter {} ", csvSeparatorCounter);
		if (csvSeparatorCounter < COMMA_SEPERATOR_COUNTER_LIMIT) {
			try {
				return AuthenticationCommonUtil.checkForValueDecryption(data);
			} catch (Exception e) {
				logger.error("Getting Exception in Decrypting Record {}", ExceptionUtils.getStackTrace(e));
			}
		}
		return data;
	}

	/**
	 * Gets the net work type.
	 *
	 * @param data the data
	 * @return the net work type
	 */
	public static int getNetWorkType(String[] data) {

		String networkType = data[WPT_NETWORK_TYPE_INDEX];
		logger.info("Getting NetworkType {}", networkType);

		if (NETWORK_TYPE_2G.equalsIgnoreCase(networkType)) {
			return WPT_NETWORK_TYPE_2G_ID;
		} else if (NETWORK_TYPE_3G.equalsIgnoreCase(networkType)) {
			return WPT_NETWORK_TYPE_3G_ID;
		} else if (NETWORK_TYPE_LTE.equalsIgnoreCase(networkType)) {
			return WPT_NETWORK_TYPE_LTE_ID;
		} else if (NETWORK_TYPE_WIFI.equalsIgnoreCase(networkType)) {
			return WPT_NETWORK_TYPE_WIFI_ID;
		}

		return NETWORK_TYPE_UNKNOWN_ID;
	}

	/**
	 * Gets the put.
	 *
	 * @param data the data
	 * @param networkType the network type
	 * @param columnFamily the column family
	 * @return the put
	 */
	public static Put getPut(String[] data, int networkType, byte[] columnFamily) {
		Put put = null;

		switch (networkType) {
			case WPT_NETWORK_TYPE_2G_ID:
				put = convertToPut(data, getAndSplitConfig(WPT_2G_DATA_FIELD),
						columnFamily);
				break;
	
			case WPT_NETWORK_TYPE_3G_ID:
				put = convertToPut(data, getAndSplitConfig(WPT_3G_DATA_FIELD),
						columnFamily);
				break;
	
			case WPT_NETWORK_TYPE_LTE_ID:
				put = convertToPut(data,  getAndSplitConfig(WPT_LTE_DATA_FIELD),
						columnFamily);
				break;
	
			case WPT_NETWORK_TYPE_WIFI_ID:
				put = convertToPut(data,  getAndSplitConfig(WPT_WIFI_DATA_FIELD),
						columnFamily);
				break;
			default:
				logger.error("Getting Invalid Network type {}", networkType);
		}
		return put;
	}
	
	/**
	 * Gets the and split config.
	 *
	 * @param config the config
	 * @return the and split config
	 */
	private static String[] getAndSplitConfig(String config) {
		return ConfigUtils.getString(config).split(ForesightConstants.UNDERSCORE);
	}

	/**
	 * Convert to put.
	 *
	 * @param fieldData the field data
	 * @param dataFormat the data format
	 * @param columnFamily the column family
	 * @return the put
	 */
	private static Put convertToPut(String[] fieldData, String[] dataFormat,
			byte[] columnFamily) {

		Put put = null;
	
		if (fieldData != null && fieldData.length > 0) {
			String rowKey = generateRowKeyForWPT(DF_000.format(++counter),
					fieldData[WPT_IMEI_INDEX], fieldData[WPT_TEST_STARTTIME_INDEX]);

			if (fieldData.length > dataFormat.length) {
				logger.info("Data length is greater than header {}/{} data {} header {}", fieldData.length, dataFormat.length,
						Arrays.toString(fieldData),Arrays.toString(dataFormat));
			}
		
			put = new Put(Bytes.toBytes(rowKey));
			updatePutWithColumns(fieldData, dataFormat, columnFamily, put);
		
		} else {
			logger.error("data length mismatch. Unable to process fieldData.length {},  dataFormat.length {}, imei index {}",
					fieldData!=null?fieldData.length:0, dataFormat.length, WPT_IMEI_INDEX);
		}
		return put;
	}

	/**
	 * Update put with columns.
	 *
	 * @param fieldData the field data
	 * @param dataFormat the data format
	 * @param columnFamily the column family
	 * @param put the put
	 */
	private static void updatePutWithColumns(String[] fieldData, String[] dataFormat, byte[] columnFamily, Put put) {
		for (int i = 0; i < fieldData.length; i++) {
			put.addColumn(columnFamily, Bytes.toBytes(dataFormat[i]),
					Bytes.toBytes(fieldData[i]));
		}
	}
	
	/**
	 * Generate row key for WPT.
	 *
	 * @param counter the counter
	 * @param imei the imei
	 * @param testStartTime the test start time
	 * @return the string
	 */
	private static String generateRowKeyForWPT(String counter, String imei, String testStartTime) {
		String rowKey = StringUtils.getCRC(imei) + testStartTime  + counter;
		logger.info("Netvelocity rowkey {} ", rowKey);
		return rowKey;
	}

}

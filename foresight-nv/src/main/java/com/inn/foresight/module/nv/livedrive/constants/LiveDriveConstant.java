package com.inn.foresight.module.nv.livedrive.constants;

public class LiveDriveConstant {

	public static final String CAP_SYMBOL = "^";

	public static final String LIVE_DRIVE_ROWKEY_REGEX = "-.*.*$";

	/** The Constant LIVE_DRIVE_TABLE. */
	public static final String LIVE_DRIVE_TABLE = "LIVE_DRIVE_TABLE";

	public static final String DRIVEID = "driveId";

	public static final String LIMIT = "limit";

	/** The DATA. */
	public static final String DATA = "data";

	/** The Constant GEO_BOND_COLUMNS_FAMILY. */

	public static final String LIVE_DRIVE_COLUMNS_FAMILY = "LIVE_DRIVE_COLUMNS_FAMILY";

	/** The Constant TASKID_SHOULD_NOT_BE_NULL. */

	public static final String TASKID_SHOULD_NOT_BE_NULL_AND_LESS_THAN_ZERO = "{\"result\":\"taskId should not be null and less then 0 \"}";

	public static final String LIMIT_SHOULD_NOT_LESS_THAN_MINUS_ONE = "limit should not less than -1";

	public static final String DATA_IS_NOT_AVAILABLE_FOR_THIS_ID = "Data is not available for this id ";

	/** The Enum AreaType. */
	public enum AreaType {

		/** The clusters. */
		CLUSTERS("CLUSTERS"),
		/** The town. */
		TOWN("TOWN"),
		/** The circle. */
		CIRCLE("CIRCLE");
		/** The name. */
	    String areaTypeVal;

		/**
		 * Instantiates a new Drive status.
		 *
		 * @param value
		 *            the value
		 */
		AreaType(String value) {
			this.areaTypeVal = value;
		}
	}

	/** LIVE DRIVE HBASE TABLE NAME. */
	public static final String LIVEDRIVE_HBASE_TABLE_NAME = "";

	/** INVALID DRIVE ID. */
	public static final String INVALID_DRIVE_ID = "Invalid Drive ID";

	/** DRIVE ID DOES NOT EXIST. */
	public static final String DRIVE_ID_DOES_NOT_EXIST = "Drive ID does not exist";

	/** DRIVE HAS BEEN STOPPED. */
	public static final String DRIVE_HAS_BEEN_STOPPED = "Drive has been stopped";

	/** DRIVE HAS BEEN PAUSED. */
	public static final String DRIVE_HAS_BEEN_PAUSED = "Drive has been paused";

	/** CSV EXTENSION. */
	public static final String CSV_EXTENSION = ".csv";

	/** FILE IS NOT OF CSV TYPE. */
	public static final String FILE_IS_NOT_OF_CSV_TYPE = "{\"message\":\"File is not CSV Type\"}";

	/** IF DATA DOES NOT GET. */
	public static final String UNABLE_TO_GET_DATA = "Unable to get data";

	/** SOME PROBLEM IN GETTING DATA. */
	public static final String SOME_PROBLEM_IN_GETTING_DATA = "Some problem in getting Data.";

	/** ERROR IN CREATING NEW TEST DRIVE. */
	public static final String ERROR_IN_CREATING_NEW_TEST_DRIVE = "Error in creating new test drive";

	/** COULMN FAMILY OF LIVE DRIVE CSV. */
	public static final String COULMN_FAMILY_LIVE_DRIVE_CSV = "";

	/** LIVE DRIVE IS NULL. */
	public static final String LIVE_DRIVE_VALUE = "Live drive is null";

	/** USER NOT EXIST. */
	public static final String USER_NOT_EXIST = "User not exist";

	/** Unicode-based encoding UTF-8. */
	public static final String UTF_8 = "UTF-8";

	/** ERROR IN GETTING TEST DRIVE DATA. */
	public static final String ERROR_IN_GETTING_TEST_DRIVE_DATA = "Error in getting test drive data";

	public static final String COMMA_SEPARATOR = ",";

	public static final String INVALID_ZIP = "{\"message\":\"Invalid zip.\"}";
	
	/** The Constant INVALID_PARAMETER. */
	public static final  String INVALID_PARAMETER = "{\"message\":\"Invalid Parameter\"}";

	public static final String DL_THROUGHPUT = "DL Throughput";
	
	public static final String UL_THROUGHPUT = "UL Throughput";
	
	public static final String MBPS = " (Mbps)";
	
	public static final String MS = " (ms)";
	
	public static final String DBM = " (dBm)";
	
	public static final String DB = " (dB)";
	
	public static final String LATENCY = "Ping Latency";
	
	public static final String JITTER = "Jitter";
	
	public static final String WEBDELAY = "Web page download delay(s)";

	public static final String SUMMARY = "summary";

	public static final String ONGOING = "ongoing";

	public static final String FAIL = "FAIL";

	public static final String PASS = "PASS";

	public static final String ONE = "1";

	
	public static final String NA = "NA";
	public static final String GEOGRAPHY_TYPE = "geographyType";
	
	public static final String GRAPH_HEADING_RSRP = "Connected Mode RSRP Statistics";
	public static final String GRAPH_HEADING_SINR = "SINR Statistics";
	public static final String GRAPH_HEADING_WEBDL = "Web page download delay";
	public static final String GRAPH_HEADING_DL = "Downlink Physical Layer Throughput Statistics";
	public static final String GRAPH_HEADING_UL = "Uplink Physical Layer Throughput Statistics";
	public static final String GRAPH_HEADING_JITTER = "Jitter";
	public static final String GRAPH_HEADING_LATENCY = "Ping Latency";

	/** The Constant GEOGRAPHY_NAME. */
	public static final String GEOGRAPHY_NAME = "geographyName";
	public static final String GEOGRAPHY_CF = "r:";
	public static final int ZERO_VALUE = 0;
	public static final Integer INDEX_ZERO = 0;

	public static final String CHART_TITLE_RSRP = "RSRP";
	public static final String CHART_TITLE_SINR = "SINR";
	public static final String CHART_TITLE_PCI = "Service Cell PCI";
	public static final String CHART_TITLE_WEBDELAY = "Web Page Download Delay";
	public static final String CHART_TITLE_LATENCY = "Latency";
	public static final String CHART_TITLE_JITTER = "Jitter";
	public static final String CHART_TITLE_DL = "DL Throughput";
	public static final String CHART_TITLE_UL = "UL Throughput";
	
	public static final String CHART_TYPE_FOR_RSRP = "Signal Strength";
	public static final String CHART_TYPE_FOR_SINR = "Signal Quality";
	public static final String CHART_TYPE_FOR_PCI = "Service Cell PCI";
	public static final String CHART_TYPE_FOR_WEBDELAY = "Web Page Download";
	public static final String CHART_TYPE_FOR_LATENCY = "Ping";
	public static final String CHART_TYPE_FOR_JITTER = "Ping";
	public static final String CHART_TYPE_FOR_DL = "FTP Download";
	public static final String CHART_TYPE_FOR_UL = "FTP Upload";
	
	public static final String COVERAGE_HEATH_INDEX = "chi";
	public static final String DATA_HEATH_INDEX = "dhi";
	
	/** * SCORE DEFINITIONS. */
	public static final String SCORE_GOOD = "Good";
	public static final String SCORE_AVG = "Average";
	public static final String SCORE_POOR = "Poor";
	public static final String SCORE_EXCELLENT = "Excellent";
	
	public static final String NETWORK_4G = "LTE";
	public static final String NETWORK_3G = "WIFI";
	public static final String NETWORK_2G = "2G";
	public static final String NETWORK_CHECK_NOT_REQUIRED = null;
	public static final String THROUGHTPUT = "_THROUGHTPUT";
}

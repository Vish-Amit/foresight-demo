package com.inn.foresight.module.nv.wpt.analytics.constants;

import java.text.DecimalFormat;

import org.apache.hadoop.hbase.util.Bytes;

/** The Class WPTHbaseConstants. */
public class WPTHbaseConstants {
	
	protected WPTHbaseConstants() {
		super();
	}

	/** The Constant DF_000. */
	public static final DecimalFormat DF_000 = new DecimalFormat("000");

	/** The Constant ADDRESS. */
	public static final String ADDRESS = "address";
	
	/** The Constant APP_VER. */
	public static final String APP_VER = "appVer";
	
	/** The Constant AVG_RSRP. */
	public static final String AVG_RSRP = "avgRsrp";
	
	/** The Constant AVG_RSRQ. */
	public static final String AVG_RSRQ = "avgRsrq";
	
	/** The Constant AVG_RSSI. */
	public static final String AVG_RSSI = "avgRssi";
	
	/** The Constant AVG_SINR. */
	public static final String AVG_SINR= "avgSinr";
	
	/** The Constant BASEBAND. */
	public static final String BASEBAND = "baseband";
	
	/** The Constant BATTERYLEVEL. */
	public static final String BATTERYLEVEL = "batteryLevel";
	
	/** The Constant BUILNUMBER. */
	public static final String BUILNUMBER = "buildNumber";
	
	/** The Constant CATEGORY. */
	public static final String CATEGORY = "category";
	
	/** The Constant CELLID. */
	public static final String CELLID = "cellId";
	
	/** The Constant GEOL3. */
	public static final String GEOL3 = "geoL3";
	
	public static final String CITY = "city";
	
	public static final String CIRCLE = "circle";

	public static final String DEVICE_ID = "deviceId";

	
	/** The Constant DEVICE_CHIPSET. */
	public static final String DEVICE_CHIPSET = "deviceChipSet";
	
	/** The Constant DEVICE_COREARCH. */
	public static final String DEVICE_COREARCH = "deviceCoreArch";
	
	/** The Constant DEVICE_OS. */
	public static final String DEVICE_OS = "operatingSystem";
	
	/** The Constant DEVICE_SNO. */
	public static final String DEVICE_SNO = "deviceSerialNo";
	
	/** The Constant DUAL_SIM_ENABLE. */
	public static final String DUAL_SIM_ENABLE = "dualSimEnable";
	
	/** The Constant END_TIME. */
	public static final String END_TIME = "endTime";
	
	/** The Constant FBYTE_RSPNS_TIME. */
	public static final String FBYTE_RSPNS_TIME = "fbyterspnstime";
	
	/** The Constant FDNS_RSLN_TIME. */
	public static final String FDNS_RSLN_TIME = "frstdnsrslntime";
	
	/** The Constant GPS_STATUS. */
	public static final String GPS_STATUS = "gpsStatus";
	
	/** The Constant IMEI. */
	public static final String IMEI = "imei";
	
	/** The Constant IMSI. */
	public static final String IMSI = "imsi";
	
	/** The Constant IPV. */
	public static final String IPV = "ipVersion";
	
	/** The Constant IS_AUTO_DATA_TIME. */
	public static final String IS_AUTO_DATA_TIME = "isAutoDataTime";
	
	/** The Constant IS_IPV6. */
	public static final String IS_IPV6 = "isIpv6";
	
	/** The Constant LATITUDE. */
	public static final String LATITUDE = "latitude";
	
	/** The Constant LOC_CATEGORY. */
	public static final String LOC_CATEGORY = "locCategory";
	
	/** The Constant LOC_INFO. */
	public static final String LOC_INFO = "locInfo";
	
	/** The Constant LONGTITUDE. */
	public static final String LONGTITUDE = "longitude";
	
	/** The Constant MAKE. */
	public static final String MAKE = "make";
	
	/** The Constant MCC. */
	public static final String MCC = "mcc";
	
	/** The Constant MNC. */
	public static final String MNC = "mnc";
	
	/** The Constant MODEL. */
	public static final String MODEL = "model";
	
	/** The Constant NTWRK_TYPE. */
	public static final String NTWRK_TYPE = "ntwrkType";
	
	/** The Constant OP_NAME. */
	public static final String OP_NAME= "operatorName";
	
	/** The Constant PCI. */
	public static final String PCI = "pci";
	
	/** The Constant PING_TIME. */
	public static final String PING_TIME = "pingtime";
	
	/** The Constant START_TIME. */
	public static final String START_TIME = "startTime";
	
	/** The Constant TAC. */
	public static final String TAC = "tac";
	
	/** The Constant TDNS_RSLN_TIME. */
	public static final String TDNS_RSLN_TIME= "tdnsrslntime";
	
	/** The Constant TEMPERATURE. */
	public static final String TEMPERATURE = "temperature";
	
	/** The Constant TEST_TYPE. */
	public static final String TEST_TYPE = "testType";
	
	/** The Constant TRACE_ROUTE. */
	public static final String TRACE_ROUTE = "trceRoute";
	
	/** The Constant TRGT_IP. */
	public static final String TRGT_IP = "trgtIp";
	
	/** The Constant TRSPNS_TIME. */
	public static final String TRSPNS_TIME = "trspnstime";
	
	/** The Constant VOLTAGE. */
	public static final String VOLTAGE = "voltage";
	
	/** The Constant WEB. */
	public static final String WEB = "web";
	
	/** The Constant HOPE_COUNT. */
	public static final String HOPE_COUNT = "hopeCount";
	
	/** The Constant TRACE_TIME. */
	public static final String TRACE_TIME = "traceTime";
	
	/** The Constant TARGET_PROTOOCOL. */
	public static final String TARGET_PROTOOCOL = "trgtPrtcl";
	
	/** The Constant GEOGRAPHY_L3. */
	public static final String GEOGRAPHY_L3 = "geographyL3";
	
	/** The Constant HOPE_NUMBER. */
	public static final String HOPE_NUMBER = "hopeNumber";
	
	/** The Constant IS_RESPOND. */
	public static final String IS_RESPOND = "isRespond";
	
	/** The Constant CSV_EXTENSION. */
	public static final String CSV_EXTENSION = ".csv";
	
	/** The Constant WPT_CSV_FILE_ENCODING. */
	public static final String WPT_CSV_FILE_ENCODING = "UTF-8";
	
	/** The Constant WPT_CSV_FILE_SPLITTER. */
	public static final String WPT_CSV_FILE_SPLITTER = "\\,";
	
	/** The Constant COMMA_SEPERATOR_COUNTER_LIMIT. */
	public static final Integer COMMA_SEPERATOR_COUNTER_LIMIT = 5;
	
	/** The Constant IPV6. */
	public static final String IPV6 = "IPV6";
	
	/** The Constant IPV4. */
	public static final String IPV4 = "IPV4";
	
	/** The Constant FORMATTED_DATE. */
	public static final String FORMATTED_DATE = "formattedDate";
	
	/** The Constant WPT_TABLE_NAME. */
	public static final String WPT_TABLE_NAME = "NV_WPT_TABLE_NAME";
	
	/** The Constant WPT_COLUMN_FAMILY. */
	public static final String WPT_COLUMN_FAMILY = "wptColumnFamily";
	
	/** The Constant WPT_2G_DATA_FIELD. */
	public static final String WPT_2G_DATA_FIELD = "wpt2GDataField";
	
	/** The Constant WPT_3G_DATA_FIELD. */
	public static final String WPT_3G_DATA_FIELD = "wpt3GDataField";
	
	/** The Constant WPT_LTE_DATA_FIELD. */
	public static final String WPT_LTE_DATA_FIELD = "wptLTEDataField";
	
	/** The Constant WPT_WIFI_DATA_FIELD. */
	public static final String WPT_WIFI_DATA_FIELD = "wptWIFIDataField";
	
	/** The Constant WPT_LAT_INDEX. */
	public static final Integer WPT_LAT_INDEX = 8;
	
	/** The Constant WPT_LONG_INDEX. */
	public static final Integer WPT_LONG_INDEX = 9;
	
	/** The Constant CELL_ID_INDEX. */
	public static final Integer CELL_ID_INDEX = 5;
	
	/** The Constant MNC_INDEX. */
	public static final Integer MNC_INDEX = 4;
	
	/** The Constant MCC_INDEX. */
	public static final Integer MCC_INDEX = 3;
	
	/** The Constant WPT_TEST_START_TIME_INDEX. */
	public static final Integer WPT_TEST_START_TIME_INDEX = 28;
	
	/** The Constant WPT_IPV6_INDEX. */
	public static final Integer WPT_IPV6_INDEX = 38;
	
	/** The Constant WPT_ENDTIME_INDEX. */
	public static final Integer WPT_ENDTIME_INDEX = 29;
	
	/** The Constant WPT_JSON_INDEX. */
	public static final Integer WPT_JSON_INDEX = 37;
	

	/** The Constant NO_OF_CITY_SAMPLE_COUNT. */
	public static final String NO_OF_CITY_SAMPLE_COUNT = "NO_OF_CITY_SAMPLE_COUNT";

	public static final String TEST_PERFORMED = "testPerformed";

	public static final String NETWORK_TYPE = "networkType";
	
	public static final String DATE = "formattedDate";
	
	public static final String TEST_START_TIME = "testStartTime";
	
	public static final String TEST_END_TIME = "testEndTime";
	
	public static final String WEB_URL = "webUrl";
	
	public static final String ITERATION = "iteration";

	public static final String FDNS = "firstDnsResolveTime";
	
	public static final String TDNS = "dnsResolveTime";
	
	public static final String TTFB = "firstByte";
	
	public static final String TTL = "totalLoadTime";
	
	public static final String TARGET_IP = "targetIp";
	
	public static final String EXTERNAL_IP = "externalIp";
	
	public static final String NO_OF_REDICTION = "noOfRedirection";

	public static final String REDIRECTED_URL = "redirectedUrl";

	public static final String ROUTE_HOLDER_LIST = "routeHolderList";

	public static final String IPV4_LIST = "ipv4list";

	public static final String GEOGRAPHYL2 = "gL2";

	public static final String GEOGRAPHYL3 = "gL3";

	public static final String RESULT = "result";

	public static final String CHARGER_CONNECTED_STATUS = "chargerConnectedStatus";

	public static final String PAGE_SIZE = "pageSize";

	public static final String MAC = "mac";

	public static final String IPV6_LIST = "ipv6list";
	
	public static final String MANUFACTURER = "manufacturer";
	
	public static final String LAC = "lac";
	
	public static final String PSC = "psc";
	
	public static final String LOCATION_TYPE = "locationDetails";
	
	public static final String REMARK = "subLocation";

	public static final String PROBE_ID = "deviceGroupId";
	
	public static final String AVG_RSCP = "avgRscp";
	
	public static final String AVG_ECNO = "avgEcno";
	
	public static final String AVG_RX_LEVEL = "avgRxLevel";
	
	public static final String AVG_RX_QUALITY = "avgRxQuality";

	public static final String TEST_ID = "recipeUniqueId";
}

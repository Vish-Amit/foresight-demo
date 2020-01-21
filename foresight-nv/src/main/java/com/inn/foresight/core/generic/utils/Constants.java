package com.inn.foresight.core.generic.utils;


import com.inn.commons.Symbol;

public class Constants {

	public static final String EXTENSION_EXCEL = ".xlsx";
	public static final String EXTENSION_TEXT = ".txt";
	public static final String EXTENSION_CSV = ".csv";

	public static final String EXCEPTION_INVALID_PARAMS = "Invalid Parameters";
	public static final String EXCEPTION_INVALID_DATE_FORMAT = "Invalid date format";
	public static final String EXCEPTION_MESSAGE = "Unable to get requested information";
	public static final String EXCEPTION_ON_CONNECTION = "Unable to connect to the server!";

	public static final double MAX_PERCENTAGE_VALUE = 100d;

	/************* Novelvox Constants **************************************/

	public static final String SOUTH_WEST_LONGITUDE = "southWestLong";
	public static final String SOUTH_WEST_LATITUDE = "southWestLat";
	public static final String NORTH_EAST_LONGITUDE = "northEastLong";
	public static final String NORTH_EAST_LATITUDE = "northEastLat";

	// ***********LSR Constants********
	public static final String CONFIDENCE = "031";
	public static final String LSR_LAT = "029";
	public static final String LSR_LON = "030";
	public static final String LSR_MAKE = "004";
	public static final String LSR_MODEL = "005";
	public static final String LSR_JC = "129";
	public static final String LSR_RSRP = "108";
	public static final String STARTCELLID = "016";
	public static final String STARTTIME = "007";
	public static final String N1_PCI = "113";
	public static final String N1_RSRP = "114";
	public static final String N1_RSRQ = "115";
	public static final String LSR_RSRQ = "115";
	public static final String ENVIRONMENT = "032";
	public static final String IMSI_LSR = "imsi";
	public static final String LSR_QCI = "035";
	public static final String LSR_IMSI = "000";
	public static final String COLUMN_VOLTE_ERAB_RELEASE_ATTEMPTS = "053";
	public static final String COLUMN_VOLTE_ERAB_DROPS = "054";
	public static final String COLUMN_S1RELEASECAUSE = "039";
	public static final String CALLRELEASECAUSE = "041";
	public static final String LASTCQI = "042";
	public static final String MISC_UNSPECIFIED = "Misc Unspecified";

	/*********************** APPLICATION CONSTANTS ***********************/

	public static final String NULL_STRING = "null";
	public static final String FILE_UPLOAD_DIR = "FILE_UPLOAD_DIR";

	public static final short BAND_2300 = 2300;
	public static final short BAND_1800 = 1800;
	public static final short BAND_850 = 850;

	public static final String BAND_2300_STRING = String.valueOf(BAND_2300);
	public static final String BAND_1800_STRING = String.valueOf(BAND_1800);
	public static final String BAND_850_STRING = String.valueOf(BAND_850);

	public static final String ALL = "ALL";
	public static final String ALL_CAMELCASE = "All";
	public static final String HISTORY = "HISTORY";

	public static final String RJSAPID = "rjsapid";
	public static final String RJNETWORKENTITYID = "rjnetworkentityid";
	public static final String STRUCTURE_COLLECTION = "structure";
	public static final String CLEAR_SEVERITY = "clear";
	public static final String CLEARED_SEVERITY = "cleared";

	public static final String WIFIVENDOR = "Vendor";
	public static final String CURRENT_STATUS = "Current Status";
	public static final String OUTAGE_FREQUENCY = "Outage Frequency";
	public static final String LAST_OUTAGE_START_DATE = "Last Outage Start Date";
	public static final String LAST_OUTAGE_START_TIME = "Last Outage Start Time";
	public static final String LAST_OUTAGE_END_DATE = "Last Outage End Date";
	public static final String LAST_OUTAGE_END_TIME = "Last Outage End Time";
	public static final String OUTAGE_HOSTORY = "Outage History";
	public static final String SITE_ID = "Site Id";

	public static final String AP_NAME = "AccessPoint Name";
	public static final String AP_STATUS = "Status";
	public static final String LATCHED_USER = "Latched User";
	public static final String LAST_UPTIME = "Last UpTime";
	public static final String SIMPLE_DATE_FORMATE2 = "dd MM yyyy HH:mm:ss";

	public static final String DATE_FORMAT = "dd MMM yyyy";
	public static final String TIME_FORMAT = "HH:mm:ss";
	public static final String SIMPLE_DATEONLY_FORMAT = "dd MMM yyyy";
	public static final String SIMPLE_TIME_FORMAT = "HH:mm:ss";

	public static final int TILE_COORDINATE_MAX_LENGTH = 6;
	public static final String TILE_SEPERATOR = Symbol.UNDERSCORE_STRING;
	public static final String ALARM_ID = "Alarm Id";
	public static final String START_DATE = "Start Date";
	public static final String START_TIME = "Start Time";
	public static final String END_DATE = "End Date";
	public static final String END_TIME = "End Time";
	public static final String JCP_CLASSIFIATION = "Jcp Classification";
	public static final String ONE = "1";

	public static final String JCP_CLS_OUTAGE = "Outages";
	public static final String JCP_CLS_PERFORMANCE_DEGRAD = "Performance_Degrading";
	public static final String JCP_CLS_SERVICE_THREAT = "Service_Threatening";
	public static final String JCP_CLS_INFORMATION = "Information";
	public static final String JCP_CLS_NOT_REACHABLE = "Not_Reachable";
	public static final String JCP_CLS_OPTIMZ_EFFECTING = "Optimization_Effecting";

	public static final String SITE_CURRENTLY_DOWN = "CURRENTLY DOWN";

	public static final String TWITTER = "TWITTER";
	public static final String INSTAGRAM = "INSTAGRAM";
	public static final String SOCIAL_FEEDS_DATE_FORMAT = "yyyyMMdd";

	public static final String WIFIALARM_COUNT_PARENT1 = "p1";
	public static final String WIFIALARM_COUNT_PARENT2 = "p2";
	public static final String WIFIALARM_COUNT_PARENT3 = "p3";
	public static final String WIFIALARM_COUNT_PARENT4 = "tileId";

	public static final String WIFI_TABLE = "wifialarm";
	public static final String INVALID_PARAMETERS = "Invalid Parameters";
	public static final String ANTENA = "ANTENNA";
	public static final String GOLDEN = "GOLDEN";
	public static final String GENERAL = "GENERAL";

	public static final String SMALL_CELL = "SMALL_CELL";
	public static final String SMALL_CELL_CAMEL_CASE = "Small Cell";
	public static final String MACRO = "MACRO";

	public static final String ANTENNA_CONF = "Antenna Conf";
	public static final String SRBID = "SI";
	public static final String AZIMUTH = "azimuth";
	public static final String ELECTRICAL_TILT = "elecTilt";
	public static final String MECHANICAL_TILT = "mechTilt";
	public static final String ANTENNA_TYPE = "antennaType";
	public static final String ANTENAA_HEIGHT = "antennaHeight";
	public static final Object ENB = "ENB";
	public static final Object ENODEB = "ENODEB";
	public static final String GL = "GL";
	public static final String GN = "GN";
	public static final String RAW = "raw";
	public static final String LASTPARSEDDATE = "lpd";

	// Mongo constants
	public static final String COLLECTION_SITE_DATA = "sitedata";
	public static final String COLLECTION_SITE_WISE_DATA_2300 = "sitewisedata2300";
	public static final String COLLECTION_SMALL_CELLS_OUTDOOR = "smallcells";
	public static final String COLLECTION_SMALL_CELLS_INDOOR = "smallcellsIndoor";

	public static final String MONGO_KEY_SAP_ID = "sapid";
	public static final String MONGO_KEY_LATITUDE = "latitude";
	public static final String MONGO_KEY_LONGITUDE = "longitude";
	public static final String MONGO_KEY_JIOCENTER = "jioCenter";

	// Handover constants
	public static final String TIME_FORMAT_IN_HANDOVER_REPORT = "yyyy-MM-dd HH:mm:ss";
	public static final String TIME_FORMAT_FOR_HANDOVER_REST = "yyyyMMdd-HH";

	// LSMR FM constants
	public static final String ALARM_CATEGORY_PERFORMANCE_DEGRADING = "Performance Degrading";
	public static final String ALARM_CATEGORY_OPTIMIZATION_EFFECTING = "Optimization Effecting";
	public static final String ALARM_CATEGORY_OUTAGE = "OUTAGE";
	public static final String ALARM_CATEGORY_INFORMATIONAL = "Informational";
	public static final String ALARM_CATEGORY_NOT_REACHABLE = "NOT REACHABLE";
	public static final String ALARM_CATEGORY_SERVICE_THREATENING = "Service Threatening";

	public static final String ALARM_STATUS_CLEARED = "cleared";

	public static final String OPERATIONAL = "Operational";
	public static final String OUTAGE = "Outage";

	public static final String SITE_LEVEL = "SITE LEVEL";

	public static final String SITE_LEVEL_CNUM = "0";
	public static final String CNUM_0 = "0";
	public static final String CNUM_1 = "1";
	public static final String CNUM_2 = "2";
	public static final String CNUM_3 = "3";
	public static final String CNUM_4 = "4";
	public static final String CNUM_5 = "5";
	public static final String CNUM_6 = "6";
	public static final String CNUM_7 = "7";
	public static final String CNUM_12 = "12";
	public static final String CNUM_13 = "13";
	public static final String CNUM_14 = "14";
	public static final String CNUM_15 = "15";
	public static final String CNUM_16 = "16";
	public static final String CNUM_24 = "24";
	public static final String CNUM_25 = "25";

	public static final String VENDOR_LSMR = "Samsung";

	public static final String CELL_ID = "Cell ID";
	public static final String VENDOR = "Vendor";
	public static String BAND_STRING = "Band";
	public static final String LSMR_ALARM_CLEAR_TYPE = "Clear Type";
	public static final String LSMR_ALARM_JCP_CLOSED_VALUE = "1";
	public static final String LSMR_ALARM_SYSTEM_CLEARED = "System";
	public static final String LSMR_ALARM_NORMAL_CLEARED = "Auto";
	public static final Integer LIST_MAX_SIZE = 990;

	// WiFi FM Constants
	public static final String WIFI_ALARM_CATEGORY_OUTAGE = "Outages";
	public static final String WIFI_ALARM_CATEGORY_PERFORMANCE_DEGRADING = "Performance_Degrading";
	public static final String WIFI_ALARM_CATEGORY_NOT_REACHABLE = "Not_Reachable";
	public static final String WIFI_ALARM_CATEGORY_SERVICE_THREATENING = "Service_Threatening";
	public static final String WIFI_ALARM_CATEGORY_INTEGRITY = "Integrity";
	public static final String WIFI_ALARM_CATEGORY_SECURITY = "Security";

	public static String _WIFI_ALARM_CATEGORY_INFORMATIONAL = "Information";
	public static final String AMPERSAND = "&";
	public static final String UNDERSCORE_ONE = "_1";
	public static final String UNDERSCORE_ZERO = "_0";
	public static final String WIFI_TIME_FORMAT = "hh:mm:ss a";
	public static final String LSMR_ALARM_BATCH_CLOSED_VALUE = "2";
	public static final String LSMR_ALARM_BATCH_CLEARED = "Batch Clear";
	public static final String VERSION_5 = "5.0.1";
	public static final String VERSION_6 = "6.0.0-01";
	public static final String UNDERSCORE = "_";
	public static final String CELL = "CELL";
	public static final String GOLDEN_TABLE = "golden2";
	public static final String SMALL_CELL_TABLE = "smcgolden2";
	public static final String ZONE = "ZONE";
	public static final String CIRCLE = "CIRCLE";
	public static final String CITY = "CITY";
	public static final String JIOCENTER = "JIOCENTER";
	public static final String CLUSTER = "CLUSTER";
	public static final String SITE = "SITE";
	public static final String SAMSUNG = "Samsung";
	public static final String PANINDIA = "PANINDIA";
	public static final String COMMA = ",";
	public static final String CISCO = "CISCO";
	public static final String AIRTIGHT = "AIRTIGHT";
	public static final String SLASH = "|";
	public static final String SPACE = " ";
	public static final String OPEN_BRACKET = "(";
	public static final String CLOSED_BRACKET = ")";
	public static final String AT_THE_RATE = "@";
	public static final String HIPHEN = "-";
	public static final String BLANK_STRING = " ";
	public static String[] TOTAL_CELLNUMS = { "0", "1", "2", "3", "4", "5",
			"6", "7", "12", "13", "14", "15", "16", "24", "25" };
	// ****************NOVELVOX****************
	public static final String LSR_JC_STR = "jc";
	public static final String LSR_MAKEMODEL = "MM";
	public static final String APPLICATION_SLASH_JSON = "application/json";
	public static final String B_COM = "COMBINED";
	public static final String RAW_IMAGE = "raw:image";
	public static final String RSRP = "RSRP";
	public static final String ONAIR = "onAir";
	public static final String DESCETE_PROCESS = "descrete";
	public static final String BSP_DATE = "BSP_DATE";
	public static final Integer BSP_ZOOM = 12;
	public static final String SINR = "SINR";
	public static final String THROUGHPUT = "Throughput";
	public static final String LOCATION = "location";
	public static final String VOLTY = "VOLTE";
	public static final String HSI = "HSI";
	public static final String HSI_QCI = "9";
	public static final String CSL_VOLTE = "volte";
	public static final String DASH = "-";
	public static final String OSC_DASH = "-OSC-";
	public static final String O_DASH = "-O-";
	public static final String E_DASH = "-E-";
	public static final String SMC_DASH = "-SMC";
	public static final String ISC_DASH = "-ISC-";
	public static final CharSequence ENB_DASH = "-ENB-";
	public static final String I_DASH = "I-";

	public static final String BLANK_STRING_FOR_CONF_AUDIT = "";
	/****************** MasterSiteStage (FM) **********************/
	public static final String ONAIR_UPPERCASE = "ONAIR";
	public static final String LSMRFM_SITE_LEVEL = "SITE LEVEL";
	public static final String LSMRFM_DATE_FORMAT = "dd-MMM-yyyy hh:mm:ss a";
	public static final String LSMRFM_AUTOCLEARED = "Auto Clear";
	public static final String CM_GENERAL_PARAMETER_TABLE = "cmparam3";
	public static final String CM_PARAM_DUMP_TABLE = "CMDump";
	public static final String CONFIG_AUDIT_TABLE = "configaudit";
	public static final String FAILURE_RESPONSE = "{\"Result\":\"Failure\",\"Reason\":\"Invalid data\"}";
	public static final String SUCCESS_RESPONSE = "{\"Result\":\"Success\"}";
	public static final String SAP_ID_GENERATED = "SAP ID Generated";
	public static final String SENT_TO_GIS = "Sent to GIS";
	public static final String REJECTED_BY_GIS = "Rejected by GIS";
	public static final String OSC = "OSC";
	public static final String XNB_DASH = "-XNB-";
	public static final String DAS = "DAS";
	public static final String ISC = "ISC";
	/****************** HeartBeat Alarm (FM) **********************/
	public static final String HBALARM_HOSTNAME = "Lsmr Host Name";
	public static final String HBALARM_CIRCLE = "Circle";
	public static final String HBALARM_LASTHEARTBEAT = "Last HeartBeat Detection";
	public static final String HBALARM_LASTHBMISSED = "Last HeartBeat Missed";
	public static final String HBALARM_TOTALHB = "HeartBeat Detected";
	public static final String HBALARM_STATUS = "Current Status";
	public static final String HBALARM_MISSEDTIME = "Heartbeats Missed Date Time";
	public static final String HB_ALARM_STATUS_CONNECTED = "connected";
	public static final String HB_ALARM_STATUS_DISCONNECTED = "disconnected";

	public static final String EMERSON_ALARM_STATUS_DEACTIVATED = "deactivated";
	public static final String EMERSON_ALARM_STATUS_ACTIVATED = "activated";
	public static final String WPT_TABLE_NAME = "WPTBinnedData";
	public static final String LAT = "lat";
	public static final String LON = "lng";
	public static final String FIRST_BYTE_LOAD_TIME = "firstByteLoadTime";
	public static final String TILEID = "tileid";
	public static final String ZOOM = "zoom";
	public static final String THRESHOLD = "threshold";
	public static final String RESULT = "result";
	public static final String FAIL_AVG = "tbltAvgFail";
	public static final String PASS_AVG = "tbltAvgPass";
	public static final String FAIL_COUNT = "tbltFailCount";
	public static final String PASS_COUNT = "tbltPassCount";
	public static final String TOTAL_BYTE_LOAD_TIME = "totalByteLoadTime";
	public static final String BAND_INFO = "bandInfo";
	public static final String SOURCE_SAPID = "SOURCE_SAPID";
	public static final String DESTINATION_SAPID = "DESTINATION_SAPID";
	public static final String BSP_DATE_PLANNED_COMBINED = "BSP_DATE_PLANNED_COMBINED";
	public static final String PLANNED_UPPERCASE = "PLANNED";

	public static final String MITIGATION = "mitigation";
	public static final String FOCUS_TOWN = "Focus Town";
	// public static final String PANINDIA = "Pan India";
	public static final String RSRP_LSR = "lsrRsrp";
	public static final String OLDRSRP = "oldRsrp";
	public static final String NEWRSRP = "newRsrp";

	public static final String OLDIMEI = "oldImei";
	public static final String NEWIMEI = "newImei";
	public static final String OLDTCDC = "oldtcdc";
	public static final String NEWTCDC = "newtcdc";
	public static final String IMEICOUNT = "imeiCount";
	public static final String TCDC = "tCDC";
	public static final String OLDSINR = "oldSinr";
	public static final String NEWSINR = "newSinr";
	public static final String OLDUNIQUEUSER = "oldUniqueuser";
	public static final String OLDCALLDROPCOUNT = "oldTcdc";
	public static final String NEWUNIQUEUSER = "newUniqueuser";
	public static final String NEWCALLDROPCOUNT = "newTcdc";
	public static final String RESOLVEDAREA = "resolvedArea";
	public static final String USER_IMPACT = "callUniqueUsers";
	public static final String CALL_DROP_IMPACT = "callDropImpact";
	public static final String ALLCOMPETITORCOUNT = "allCompetitorCount";
	public static final String POLYGON_CENTROID = "polygonCentroid";

	public static final String AREAFACTOR = "areaFactor";
	public static final String MORPHOLOGYFACTOR = "morphologyFact";
	public static final String BAD_RSRP_SAPMPLES_FACTOR = "bd_sampleFact";
	public static final String TRAFFIC_DENSITY_FACTOR = "tcdcFact";
	public static final String USER_DENSITY = "imeiFact";
	public static final String CALLDROP_FACTOR = "dropCallsFact";
	public static final String COMP_FACTOR = "compFact";
	public static final String TOTAL_FACTOR = "totalFact";
	public static final String COMMAND_RESULT = "commandexecuted";
	public static final String FAILED = "Failed";
	public static final String COMMANDOUTPUT = "commandoutput";
	public static final String COMMAND_ERROR = "commanderror";
	public static final String SUCCESS = "Success";
	public static final String JIOCENTER_ID = "jioCenter";;
	public static final String SAPID = "sapid";
	public static final String CMPNAME = "CMP NAME";
	public static final String OWNERSHIP = "Ownership";
	public static final String DG = "DG";
	public static final String SITETYPE = "Site Type";
	public static final String BATTERYCAPACITY = "Battery Capacity";
	public static final String ALARMCODE = "Alarm Code";
	public static final String PROCESSEDBYSPARKFLOW = "PROCESSEDBYSPARKFLOW";
	public static final String CHANGE_AUDIT_TABLE = "ChangeAudit";
	public static final String DDMMYY = "ddMMyy";
	public static final String DDMMMYY = "dd MMM, YY";
	public static final String HHMMA = "hh:mm a";
	public static final String DDMMYY_HHMMSSA = "dd-MM-yy HH:mm:ss a";
	public static final String CONFIGPARAMETABLE = "CONFIGPARAMETABLE";
	public static final String SMALLCELL = "SMALLCELL";
	public static final String DD_MMM_YYYY = "dd MMM, YYYY";
	public static final String HH_MM_A = "hh:mm a";
	public static final String NONCOMPLIANCE = "NonCompliance";
	public static final Integer ZOOM_LEVEL_15 = 15;
	public static final String  INVALID_GEOTYPE="GeoType can't be null";
	
	//Constants for AttachmentService
	public static final String ATTACHMENT_SUCCESS_RESPONSE = "{\"Result\" : \"Success\"}";
	public static final String ATTACHMENT_FAILURE_RESPONSE = "{\"Result\" : \"Failure\"}";
	public static final String ATTACHMENT_SUCCESS_JSON = "{\"result\":\"success\"}";
	public static final String ATTACHMENT_FAILURE_JSON = "{\"result\":\"failure\"}";
	public static final String TEMP = "temp";
	public static final String GET_ATTACHMENT_FILE_NOT_FOUND = "{\"Result\":\"Failure\",\"Reason\":\"File not found\"}";
	public static final String SAVED_MESSAGE = "File saved";
	public static final String MISSING_FILE_PATH_MESSAGE = "File path not mentioned...";
	public static final String MISSING_FILE = "File not found...";
	public static final String ERROR_LOG = "Error while saving attachment, err : ";
	public static final String CONTENT_DISPOSITION = "Content-Disposition";
	public static final String ENUM_NOT_FOUND = "No enum found which matchs: ";
}
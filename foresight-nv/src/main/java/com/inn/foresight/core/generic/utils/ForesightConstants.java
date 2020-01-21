package com.inn.foresight.core.generic.utils;

import org.apache.hadoop.hbase.util.Bytes;

/**
 * The Class ForesightConstants.
 */
public class ForesightConstants {

    public static final String HEXADECIMAL = "HEXADECIMAL";
    public static final String SALES_L1 = "SalesL1";
    public static final String SALES_L2 = "SalesL2";
    public static final String SALES_L3 = "SalesL3";
    public static final String SALES_L4 = "SalesL4";

    /** The Constant URL_SPACE_REPLACER. */
    public static final String URL_SPACE_REPLACER = "%20";

    /** The Constant EXCEPTION_NO_RECORD_FOUND. */
    public static final String EXCEPTION_NO_RECORD_FOUND = "No data found";
    /** The Constant CONTENT_TYPE. */
    public static final String CONTENT_TYPE = "Content-Type";

    /** The Constant CONTENT_TYPE_VALUE. */
    public static final String CONTENT_TYPE_VALUE = "text/html;charset=utf-8";

    /** The Constant CONTENT_IMG. */
    public static final String CONTENT_IMG = "image/png";
    /** The Constant AREA. */
    public static final String AREA = "area";
    /** The Constant TABLENAME. */
    public static final String TABLENAME = "tableName";
    /** The Constant INVALID_PARAMETERS_JSON. */
    public static final String INVALID_PARAMETERS_JSON = "{\"message\":\"Invalid parameters\"}";
    /** The Constant ERROR. */
    public static final String ERROR = "Error";
    /** The Constant EXCEPTION_SOMETHING_WENT_WRONG. */
    public static final String EXCEPTION_SOMETHING_WENT_WRONG = "There is some problem on getting data for the request";
    /** The Constant ZERO_VALUE. */
    public static final int ZERO = 0;
    public static final int ONE = 1;

    public static final int TWO = 2;
    /** The Constant APPLICATION_SLASH_JSON. */
    public static final String APPLICATION_SLASH_JSON = "application/json";
    public static final String EMPTY_SQUARE_BRACKET = "[]";
    public static final String KPI = "kpi";
    /** The Constant EXCEPTION_INVALID_PARAMS. */
    public static final String EXCEPTION_INVALID_PARAMS = "Invalid Parameters";
    public static final String ERROR_INVALIDPARAMS = "{\"response\":\"invalid params\"}";
    public static final String ERROR_RESPONSE = "{\"response\":\"error getting data\"}";
    public static final String RSRP_PREFIX = "RS";
    public static final String SINR_PREFIX = "SI";
    public static final String DL_PREFIX = "DL";
    public static final String UL_PREFIX = "UL";
    public static final String ON_AIR_PREFIX = "OA";
    public static final String PLANNED_PREFIX = "PL";
    public static final String GeographyL1 = "GeographyL1";
    public static final String GeographyL2 = "GeographyL2";
    public static final String GeographyL3 = "GeographyL3";
    public static final String GeographyL4 = "GeographyL4";
    public static final String GeographyType_L1 = "L1";
    public static final String GeographyType_L2 = "L2";
    public static final String GeographyType_L3 = "L3";
    public static final String GeographyType_L4 = "L4";
    public static final String SALES_DATA_INPROGRESS = "SALES_DATA_INPROGRESS";

    public static final String COUNTRY_LATITUDE = "CountryLatitude";
    public static final String COUNTRY_LONGITUDE = "CountryLongitude";

    /** The Constant FIVE. */
    public static final int FIVE = 5;

    /** The Constant SIX. */
    public static final int SIX = 6;

    /** The Constant FIFTEEN. */
    public static final int FIFTEEN = 15;

    /** The Constant TWENTY_FOUR. */
    public static final int TWENTY_FOUR = 24;

    /** The Constant NINE. */
    public static final int NINE = 9;

    /** The Constant TEN. */
    public static final int TEN = 10;
    public static final String ZERO_STRING = "0";
    public static final String AVERAGE_LOWER = "average";
    public static final String MEDIAN_LOWER = "median";

    /** The Constant TILE_SIZE. */
    public static final int TILE_SIZE = 256;

    /** The Constant TILE_SIZE_BUFFERED. */
    public static final int TILE_SIZE_BUFFERED = 260;
    public static final String REPORT = "report";
    /** The Constant FOUR. */
    public static final int FOUR = 4;
    /** The Constant COLON. */
    public static final String COLON = ":";
    /** The Constant AT_THE_RATE. */
    public static final String AT_THE_RATE = "@";

    /** The Constant COMMA. */
    public static final String COMMA = ",";
    public static final String PERCENTILE5_LOWER = "percentile5";
    public static final String PERCENTILE95_LOWER = "percentile95";
    public static final String LEGEND_ID = "legendID";

    public static final String RANGE = "range";

    public static final int NINETY_FIVE = 95;
    public static final int HUNDRED = 100;
    public static final double HUNDRED_DOUBLE = 100.0;
    public static final String VALUE_LOWER = "value";
    public static final String COLOR_LOWER = "color";

    /** The Constant UNDERSCORE. */
    public static final String UNDERSCORE = "_";
    public static final int Two_Fifty_Five = 255;
    public static final String EMPTY = "";

    /** The Constant RSRP. */
    public static final String RSRP = "RSRP";
    public static final String IDENTIFIER_COV = "COV";
    public static final String IDENTIFIER_SNC = "SNC";
    public static final Double RSRP_MAX_VALUE = -44.0;

    /** The Constant EXCEPTION_ON_CONNECTION. */
    public static final String EXCEPTION_ON_CONNECTION = "Unable to connect to the server!";
    /** The Constant BAND2300. */
    public static final String BAND2300 = "2300";

    /** The Constant BAND1800. */
    public static final String BAND1800 = "1800";

    /** The Constant BAND850. */
    public static final String BAND850 = "850";
    /** The Constant DIVISION_DUPLEX_FDD. */
    public static final String DIVISION_DUPLEX_FDD = "FDD";

    /** The Constant DIVISION_DUPLEX_TDD. */
    public static final String DIVISION_DUPLEX_TDD = "TDD";
    public static final String MASKED_IMSI_PREFIX = "XXXXXXXXXXX";

    /** The Constant DECIMAL_FORMAT. */
    public static final String DECIMAL_FORMAT = "#.##";
    /** The Constant FILE_READ_BUFFER_SIZE. */
    public static final int FILE_READ_BUFFER_SIZE = 1024;
    /** The Constant BLANK_STRING. */
    public static final String BLANK_STRING = "";
    /** The Constant HIPHEN. */
    public static final String HIPHEN = "-";
    public static final Integer THREE = 3;
    /** The Constant SINR. */
    public static final String SINR = "SINR";
    /** The Constant NOVELVOX_DISTANCE_RANGE. */
    public static final Double NOVELVOX_DISTANCE_RANGE = 100d;

    /** The Constant NOVELVOX_ZOOM_LEVEL. */
    public static final Integer NOVELVOX_ZOOM_LEVEL = 15;

    /** The Constant ZOOM_LEVEL_SIXTEEN. */
    public static final Integer ZOOM_LEVEL_SIXTEEN = 16;

    /** The Constant NOVELVOX_KPITYPE. */
    public static final String NOVELVOX_KPITYPE = "RSRP";

    /** The Constant NOVELVOX_TYPE. */
    public static final String NOVELVOX_TYPE = "ONAIR";

    /** The Constant DDMMYY. */
    public static final String DDMMYY = "ddMMyy";
    /** The Constant GEOGRAPHYL1_ZOOM. */
    public static final String GEOGRAPHYL1_ZOOM = "GeographyL1Zoom";

    /** The Constant GEOGRAPHYL2_ZOOM. */
    public static final String GEOGRAPHYL2_ZOOM = "GeographyL2Zoom";

    /** The Constant GEOGRAPHYL3_ZOOM. */
    public static final String GEOGRAPHYL3_ZOOM = "GeographyL3Zoom";

    /** The Constant GEOGRAPHYL4_ZOOM. */
    public static final String GEOGRAPHYL4_ZOOM = "GeographyL4Zoom";
    public static final Integer THOUSAND = 1000;
    /** The Constant INVALID_PARAMETER. */
    public static final String INVALID_PARAMETER = "Invalid Parameter";
    /** The Constant OTHER_GEOGRAPHY_NOT_FOUND. */
    public static final String OTHER_GEOGRAPHY_NOT_FOUND = "Other Geography not found for name %s and type %s";

    /** The Constant NOVELVOX_GOOD_OUTDOOR_INDORE. */
    public static final String NOVELVOX_GOOD_OUTDOOR_INDORE = "NOVELVOX_GOOD_OUTDOOR_INDORE";

    /** The Constant NOVELVOX_GOOD_OUTDOOR_LIMITED_INDORE. */
    public static final String NOVELVOX_GOOD_OUTDOOR_LIMITED_INDORE = "NOVELVOX_GOOD_OUTDOOR_LIMITED_INDORE";

    /** The Constant NOVELVOX_OUTDOOR_ONLY. */
    public static final String NOVELVOX_OUTDOOR_ONLY = "NOVELVOX_OUTDOOR_ONLY";

    /** The Constant NOVELVOX_NO_COVERAGE. */
    public static final String NOVELVOX_NO_COVERAGE = "NOVELVOX_NO_COVERAGE";
    /** The Constant INDDOOR_OUTDOOR_RESULT. */
    public static final String INDDOOR_OUTDOOR_RESULT = "result";
    /** The Constant GEOGRAPHY_NOT_FOUND. */
    public static final String GEOGRAPHY_NOT_FOUND = "Geography not found";
    /** The Constant SUCCESS_JSON. */
    public static final String SUCCESS_JSON = "{\"result\":\"success\"}";

    /** The Constant FAILURE_JSON. */
    public static final String FAILURE_JSON = "{\"result\":\"failure\"}";
    /** The Constant LOGGED_IN. */
    public static final String LOGGED_IN = "Loggedin";

    /** The Constant LOGGED_OUT. */
    public static final String LOGGED_OUT = "LoggedOut";

    /** The Constant JAVA_LANG_STRING. */
    public static final String JAVA_LANG_STRING = "java.lang.String";

    /** The Constant JAVA_LANG_INTEGER. */
    public static final String JAVA_LANG_INTEGER = "java.lang.Integer";

    /** The Constant JAVA_LANG_DOUBLE. */
    public static final String JAVA_LANG_DOUBLE = "java.lang.Double";

    /** The Constant JAVA_LANG_LONG. */
    public static final String JAVA_LANG_LONG = "java.lang.Long";
    /** The Constant JAVA_LANG_FLOAT. */
    public static final String JAVA_LANG_FLOAT = "java.lang.Float";
    /** The Constant X_FORWORDED_FOR. */
    public static final String X_FORWORDED_FOR = "x-forwarded-for";

    /** The Constant USER_AGENT. */
    public static final String USER_AGENT = "user-agent";
    /** The Constant DOUBLE_SPACE. */
    public static final String DOUBLE_SPACE = "  ";
    /** The Constant CREATED_TIME. */
    public static final String CREATED_TIME = "CreatedTime";
    /** The Constant TRUE. */
    public static final Boolean TRUE = true;
    /** The Constant MODIFIED_TIME. */
    public static final String MODIFIED_TIME = "Time";

    /** The Constant MODIFIEDTIME. */
    public static final String MODIFIEDTIME = "modifiedTime";

    /** The Constant GET_SMALL. */
    public static final String GET_SMALL = "get";

    /** The Constant IDENTIFIER. */
    public static final String IDENTIFIER = "Identifier";

    /** The Constant USERNAME. */
    public static final String USERNAME = "Username";

    /** The Constant PASSWORD. */
    public static final String PASSWORD = "Password";
    public static final String SPACE = " ";
    /** The Constant LOGIN. */
    public static final String LOGIN = "login";

    /** The Constant KEY_LAST_NAME. */
    public static final String KEY_LAST_NAME = "lastName";

    /** The Constant KEY_CHECK_SUM. */
    public static final String KEY_CHECK_SUM = "checksum";

    /** The Constant LOGOUT. */
    public static final String LOGOUT = "logout";
    /** The Constant PASSED_DATE. */
    public static final String PASSED_DATE = "passedDate";
    /** The Constant INVALID_PARAMETERS. */
    public static final String INVALID_PARAMETERS = "Invalid Parameters";
    public static final String KEY_USER_ID = "userid";

    /** The Constant KEY_USER_NAME. */
    public static final String KEY_USER_NAME = "username";
    /** The Constant UPDATE. */
    public static final String UPDATE = "UPDATE";

    public static final String BULKUPLOAD_CUSTOMER_CARE = "CUSTOMERCARE";
    public static final String NON_SSO_CONTEXT = "NON_SSO_CONTEXT";

    /** The Constant CREATE. */
    public static final String CREATE = "CREATE";

    /** The Constant ACTION_TYPE. */
    public static final String ACTION_TYPE = "actiontype";

    /** The Constant ACTION_TYPE_FILTER. */
    public static final String ACTION_TYPE_FILTER = "actiontypeFilter";

    /** The Constant ACTION_NAME. */
    public static final String ACTION_NAME = "actionname";

    /** The Constant ACTION_NAME_FILTER. */
    public static final String ACTION_NAME_FILTER = "actionnameFilter";

    /** The Constant SUCCESS_FOR_FILTER. */
    public static final String SUCCESS_FOR_FILTER = "success";

    /** The Constant SUCCESS_FILTER. */
    public static final String SUCCESS_FILTER = "successFilter";

    /** The Constant MODULUS. */
    public static final String MODULUS = "%";
    /** The Constant USER_FULL_NAME. */
    public static final String USER_FULL_NAME = "userfullname";

    /** The Constant USER_FULL_NAME_FILTER. */
    public static final String USER_FULL_NAME_FILTER = "userfullnameFilter";
    /** The Constant CONTAINS. */
    public static final String CONTAINS = "contains";

    /** The Constant ROLE_ID. */
    public static final String ROLE_ID = "roleId";

    /** The Constant TEAM_ID. */
    public static final String TEAM_ID = "teamId";
    /** The Constant GEOGRAPHY_TYPE. */
    public static final String GEOGRAPHY_TYPE = "geographyType";
    /** The Constant ONE_IN_STRING. */
    public static final String ONE_IN_STRING = "1";

    /** The Constant THREE_IN_STRING. */
    public static final String THREE_IN_STRING = "3";
    /** The Constant USERID. */
    public static final String USERID = "userId";
    /** The Constant MITIGATION_COVERAGE_HOLE_2300. */
    public static final String MITIGATION_COVERAGE_HOLE_2300 = "MITIGATION_COVERAGE_HOLE_2300";

    /** The Constant MITIGATION_COVERAGE_HOLE_850. */
    public static final String MITIGATION_COVERAGE_HOLE_850 = "MITIGATION_COVERAGE_HOLE_850";

    /** The Constant MITIGATION_COVERAGE_HOLE_1800. */
    public static final String MITIGATION_COVERAGE_HOLE_1800 = "MITIGATION_COVERAGE_HOLE_1800";

    /** The Constant MITIGATION_COVERAGE_HOLE_COM. */
    public static final String MITIGATION_COVERAGE_HOLE_COM = "MITIGATION_COVERAGE_HOLE_COM";

    /** The Constant BSP_DATE_850. */
    public static final String BSP_DATE_850 = "BSP_DATE_850";

    /** The Constant BSP_DATE_1800. */
    public static final String BSP_DATE_1800 = "BSP_DATE_1800";

    /** The Constant BSP_DATE_2300. */
    public static final String BSP_DATE_2300 = "BSP_DATE_2300";

    /** The Constant BSP_DATE_COM. */
    public static final String BSP_DATE_COM = "BSP_DATE";
    /** The Constant MITIGATION_COMBINED_LAYER_LAST_PROCESSED_DATE. */
    public static final String MITIGATION_COMBINED_LAYER_LAST_PROCESSED_DATE = "MITIGATION_COMBINED_LAYER_LAST_PROCESSED_DATE";

    /** The Constant MITIGATION. */
    public static final String MITIGATION = "MITIGATION";
    /** The Constant COVERAGE_HOLE_2300. */
    public static final String COVERAGE_HOLE_2300 = "COVERAGE_HOLE_2300";

    /** The Constant COVERAGE_HOLE_1800. */
    public static final String COVERAGE_HOLE_1800 = "COVERAGE_HOLE_1800";

    /** The Constant COVERAGE_HOLE_850. */
    public static final String COVERAGE_HOLE_850 = "COVERAGE_HOLE_850";

    /** The Constant COVERAGE_HOLE_COM. */
    public static final String COVERAGE_HOLE_COM = "COVERAGE_HOLE_COM";
    /** The Constant LOCAL. */
    public static final String LOCAL = "local";
    /** The Constant EXCEL_EXTENSION. */
    public static final String EXCEL_EXTENSION = ".xlsx";
    /** The Constant XMLEXTENSION. */
    public static final String XMLEXTENSION = ".xml";
    /** The Constant FILE_EXTENSION_CSV. */
    public static final String FILE_EXTENSION_CSV = ".csv";
    /** The Constant ALL. */
    public static final String ALL = "ALL";

    /** The Constant CORE_SITE_XML. */
    public static final String CORE_SITE_XML = "core-site" + XMLEXTENSION;

    /** The Constant HDFS_SITE_XML. */
    public static final String HDFS_SITE_XML = "hdfs-site" + XMLEXTENSION;
    /** The Constant FORWARD_SLASH. */
    public static final String FORWARD_SLASH = "/";
    /** The Constant XLSX_EXTENSION. */
    public static final String XLSX_EXTENSION = ".xlsx";

    /** The Constant TMO. */
    public static final String TMO = "TMO";

    /** The Constant CSPIRE. */
    public static final String CSPIRE = "CSPIRE";

    /** The Constant VERIZON. */
    public static final String VERIZON = "VERIZON";

    /** The Constant SPRINT. */
    public static final String SPRINT = "SPRINT";

    /** The Constant ALL_MMEs_CLUBBED. */
    public static final String ALL_MMEs_CLUBBED = "All MMEs - Clubbed";

    /** The Constant ALL_MMEs_INDIVIDUAL. */
    public static final String ALL_MMEs_INDIVIDUAL = "All MMEs - Individual";

    /** The Constant ALL_SAEGWs_CLUBBED. */
    public static final String ALL_SAEGWs_CLUBBED = "All SAEGWs - Clubbed";

    /** The Constant ALL_SAEGWs_INDIVIDUAL. */
    public static final String ALL_SAEGWs_INDIVIDUAL = "All SAEGWs - Individual";

    /** The Constant DATE_FORMAT_dd_MM_yy. */
    public static final String DATE_FORMAT_dd_MM_yy = "dd-MM-yy";

    /** The Constant DATE_FORMAT_YYYYdd. */
    public static final String DATE_FORMAT_YYYYww = "YYYYww";

    public static final int Sixty_Five = 65;
    public static final int Seventy_Five = 75;

    /** The Constant COVERAGE_HOLE_105. */
    public static final String COVERAGE_HOLE_105 = "COVERAGE_HOLE_105";
    /** The Constant VALUE. */
    public static final String VALUE = "value";

    /** The Constant USERNAME_SMALL. */
    public static final String USERNAME_SMALL = "username";
    /** The Constant SEARCH_SOFTWARE_VERSION. */
    public static final String SEARCH_SOFTWARE_VERSION = "SoftwareVersion";

    public static final String INSTANCE_ID = "instanceId";

    /** The netype camel case. */
    public static String NETYPE_CAMEL_CASE = "NEType";
    /** The Constant DOMAIN. */
    public static final String DOMAIN = "Domain";
    /** The Constant INTEGER_SECTOR_1. */
    public static final Integer INTEGER_SECTOR_1 = 1;

    /** The Constant INTEGER_SECTOR_2. */
    public static final Integer INTEGER_SECTOR_2 = 2;

    /** The Constant INTEGER_SECTOR_3. */
    public static final Integer INTEGER_SECTOR_3 = 3;

    /** The Constant INTEGER_SECTOR_4. */
    public static final Integer INTEGER_SECTOR_4 = 4;

    public static final Integer INTEGER_SECTOR_5 = 5;

    public static final Integer INTEGER_SECTOR_6 = 6;
    /** The Constant STRING_MHZ. */
    public static final String STRING_MHZ = "MHz";

    /** The Constant BANDWIDTH_CAMAL_CASE. */
    public static final String BANDWIDTH_CAMAL_CASE = "Bandwidth";
    /** The Constant CELL_ID_CAMEL. */
    public static final String CELL_ID_CAMEL = "Cell Id";

    /** The Constant CID. */
    public static final String CID = "CID";

    /** The Constant SECTORID_. */
    public static final String SECTORID_ = "Sector ID";

    /** The Constant PCI. */
    public static final String PCI = "PCI";
    /** The Constant FIRST. */
    public static final String FIRST = "FIRST";
    /** The Constant SECOND. */
    public static final String SECOND = "SECOND";
    /** The Constant SITE_TYPE. */
    public static final String SITE_TYPE = "siteType";

    /** The Constant SMALL_LAT. */
    public static final String SMALL_LAT = "smallLat";

    /** The Constant SMALL_LNG. */
    public static final String SMALL_LNG = "smallLong";

    /** The Constant LARGE_LAT. */
    public static final String LARGE_LAT = "largeLat";

    /** The Constant LARGE_LNG. */
    public static final String LARGE_LNG = "largeLong";
    /** The Constant IS_DECOMMISSIONED. */
    public static final String IS_DECOMMISSIONED = "isDecommissioned";
    /** The Constant PROGRESS_STATE. */
    public static final String PROGRESS_STATE = "progressState";

    /** The Constant DISPLAY_SITES. */
    public static final String DISPLAY_SITES = "displaySite";
    /** The Constant COMMMISSION_ENODEB. */
    public static final String COMMMISSION_ENODEB = "Commission_eNodeB";
    /** The Constant EXECUTE_EMF. */
    public static final String EXECUTE_EMF = "Execute_EMF";

    /** The Constant EXECUTE_ATP_11B. */
    public static final String EXECUTE_ATP_11B = "Execute_ATP_11B";

    /** The Constant EXECUTE_SCFT. */
    public static final String EXECUTE_SCFT = "Execute_SCFT";

    /** The Constant EXECUTE_SSCVT. */
    public static final String EXECUTE_SSCVT = "Execute_SSCVT";
    /** The Constant INSTALL_ENODEB. */
    public static final String INSTALL_ENODEB = "Install_eNodeB";

    /** The Constant EXECUTE_ATP_11A. */
    public static final String EXECUTE_ATP_11A = "Execute_ATP_11A";

    /** The Constant RFE1_ACCEPTANCE. */
    public static final String RFE1_ACCEPTANCE = "RFE1_Acceptance";
    /** The Constant STATUS_RFC. */
    public static final String STATUS_RFC = "RFC";

    /** The Constant STATUS_RFA. */
    public static final String STATUS_RFA = "RFA";

    /** The Constant STATUS_RFI. */
    public static final String STATUS_RFI = "RFI";
    /** The Constant RFE1_SURVEY. */
    public static final String RFE1_SURVEY = "RFE1_Survey";
    public static final String SMALLCELL_SITE = "SmallCellSummary";
    /** The Constant NULL_STRING. */
    public static final String NULL_STRING = "null";
    public static final String SMALL_CELL_OAM = "Small Cell OAM";

    public static final String OAM_VLAN = "OAM VLAN";

    public static final String OAM_GATEWAY_IPV6_ADDRESS = "OAM Gateway IPv6 Address";

    public static final String OAM_IP = "OAM IP";
    public static final String RAN_DETAILS = "RAN Details";
    public static final String SMALL_CELL_SIGNALING = "Small Cell Signaling";

    public static final String SIGNALING_VLAN = "Signaling VLAN";

    public static final String SIGNALING_GATEWAY_IPV6_ADDRESS = "Signaling Gateway IPv6 Address";

    public static final String SIGNALING_IP = "Signaling IP";
    public static final String BEARER_VLAN = "Bearer VLAN";

    public static final String BEARER_GATEWAY_IPV6_ADDRESS = "Bearer Gateway IPv6 Address";

    public static final String BEARER_IP = "Bearer IP";
    public static final String EMS_DETAIL = "EMS Detail";

    public static final String EMS_IPV6_ADDRESS = "EMS IPv6 Address";

    public static final String EMS_ID = "EMS ID";

    public static final String EMS_HOST_NAME = "EMS Host Name";
    public static final String SMALL_CELL_BEARER = "Small Cell Bearer";
    public static final String SMALL_CELL_INFORMATION = "Small Cell Information";
    public static final String SYSTEM_CONFIG_UPDATED_SUCCESSFULLY = "Configuration updated successfully";
    public static final String SYSTEM_CONFIGURATION_CONFIGTYPE_CORE = "CORE";

    public static final String SYSTEM_CONFIGURATION_CONFIGTYPE_CUSTOM = "CUSTOM";

    public static final String CUSTOMER_CARE_CONFIGURATION = "CUSTOMER_CARE";
    /** The Constant ONAIR_UPPERCASE. */
    public static final String ONAIR_UPPERCASE = "ONAIR";
    /** The Constant FILE_EXTENSION_EXCEL. */
    public static final String FILE_EXTENSION_EXCEL = ".xlsx";
    public static final String DATE_FORMAT_YYYY_MM_DD = "YYYY-MM-DD";
    /** The Constant DATE_FORMAT_yyyy_MM_dd. */
    public static final String DATE_FORMAT_yyyy_MM_dd = "yyyy-MM-dd";
    /** The Constant DATE_FORMAT_dd_MM_yyyy. */
    public static final String DATE_FORMAT_dd_MM_yyyy = "dd-MM-yyyy";

    /** The Constant PASSWORD_SMALL. */
    public static final String PASSWORD_SMALL = "password";

    /** The Constant USAGE_DATE. */
    public static final String USAGE_DATE = "usageDate";
    public static final String NAME = "name";
	public static final String DISPLAY_NAME = "displayName";
    /** The Constant GEOGRAPHY_L1. */
    public static final String GEOGRAPHY_L0 = "GeographyL0";
    
    /** The Constant GEOGRAPHY_L1. */
    public static final String GEOGRAPHY_L1 = "GeographyL1";

    /** The Constant GEOGRAPHY_L2. */
    public static final String GEOGRAPHY_L2 = "GeographyL2";

    /** The Constant GEOGRAPHY_L3. */
    public static final String GEOGRAPHY_L3 = "GeographyL3";

    /** The Constant GEOGRAPHY_L4. */
    public static final String GEOGRAPHY_L4 = "GeographyL4";

    /** The Constant L1_NAME. */
    public static final String L1_NAME = "L1Name";

    /** The Constant L2_NAME. */
    public static final String L2_NAME = "L2Name";

    /** The Constant L3_NAME. */
    public static final String L3_NAME = "L3Name";
    /** The Constant ID. */
    public static final String ID = "id";
    public static final String CITY_NAME = "cityname";

    /** The Constant DATE. */
    public static final String DATE = "date";

    /** The Constant CIRCLE_ID1. */
    public static final String CIRCLE_ID1 = "circleId";

    /** The Constant MONTH. */
    public static final String MONTH = "month";

    /** The Constant YEAR. */
    public static final String YEAR = "year";

    /** The Constant RATING. */
    public static final String RATING = "rating";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    /** The Constant CUSTOMER_CARE_CLUSTER_LIST. */
    public static final String CUSTOMER_CARE_CLUSTER_LIST = "ClusterList";
    /** The Constant GEOGRAPHY. */
    public static final String GEOGRAPHY = "GEOGRAPHY";
    /** The Constant SECTOR_SMALL. */
    public static final String SECTOR_SMALL = "sector";

    /** The Constant NE_NAME. */
    public static final String NE_NAME = "neName";

    /** The Constant NE_TYPE. */
    public static final String NE_TYPE = "neType";

    /** The Constant NE_STATUS. */
    public static final String NE_STATUS = "neStatus";

    /** The Constant NE_FRFEQUENCY. */
    public static final String NE_FRFEQUENCY = "neFrequency";
    
    public static final String FRIENDLY_NAME="friendlyName";
    
    /** The Constant BAND. */
    public static final String BAND = "band";

    /** The Constant SAPIDS. */
    public static final String SAPIDS = "sapIds";
    /** The Constant SAPID_CAMEL. */
    public static final String SAPID_CAMEL = "sapId";
    /** The Constant PAN_INDIA. */
    public static final String PAN_INDIA = "PAN India";

    /** The Constant PAN_INDIA_NO_SPACE. */
    public static final String PAN_INDIA_NO_SPACE = "PANINDIA";

    /** The Constant TARGET_AREA_TYPE. */
    public static final String TARGET_AREA_TYPE = "PANINDIA";

    /** The Constant PAN_INDIA_CAPITAL. */
    public static final String PAN_INDIA_CAPITAL = "PAN_INDIA";
    /** The Constant CARRIER. */
    public static final String CARRIER = "carrier";
    public static final String ERROR_NO_CELLS_AVAILABLE = "No Cells available";
    public static final String LOG_EXCEPTION = "Exception in {}";
    /** The Constant VENDOR_SMALL. */
    public static final String VENDOR_SMALL = "vendor";

    /** The Constant TECH. */
    public static final String TECH = "technology";

    /** The Constant DOMAIN_SMALL. */
    public static final String DOMAIN_SMALL = "domain";

    /** The Constant CITY_CAMEL. */
    public static final String CITY_CAMEL = "cityName";

    /** The Constant R4G_STATE_CAMEL. */
    public static final String R4G_STATE_CAMEL = "r4gStateName";

    /** The Constant CLUSTERNAME. */
    public static final String CLUSTERNAME = "clusterName";

    /** The Constant JIO_CENTER_CAMEL. */
    public static final String JIO_CENTER_CAMEL = "jioCenter";
    /** The Constant TECHNOLOGY. */
    public static final String TECHNOLOGY = "technology";
    /** The Constant SOUTH_WEST_LONGITUDE. */
    public static final String SOUTH_WEST_LONGITUDE = "southWestLong";

    /** The Constant SOUTH_WEST_LATITUDE. */
    public static final String SOUTH_WEST_LATITUDE = "southWestLat";

    /** The Constant NORTH_EAST_LONGITUDE. */
    public static final String NORTH_EAST_LONGITUDE = "northEastLong";

    /** The Constant NORTH_EAST_LATITUDE. */
    public static final String NORTH_EAST_LATITUDE = "northEastLat";
    /** The Constant SITE_PROGRESS_STATE. */
    public static final String SITE_PROGRESS_STATE = "progressState";

    /** The Constant SITE_TYPE_KEY. */
    public static final String SITE_TYPE_KEY = "siteType";

    /** The Constant CITY_BOUNDARY. */
    public static final String CITY_BOUNDARY = "cityboundary";

    /** The Constant STATE_BOUNDARY. */
    public static final String STATE_BOUNDARY = "stateboundary";
    /** The Constant ASTERISK. */
    public static final String ASTERISK = "*";

    /** The Constant PERCENT. */
    public static final String PERCENT = "%";
    /** The Constant KEY_VENDOR. */
    public static final String KEY_VENDOR = "vendor";
    /** The Constant VENDOR. */
    public static final String VENDOR = "VENDOR";
    /** The Constant CLUSTER. */
    public static final String CLUSTER = "cluster";
    /** The Constant AZIMUTH. */
    public static final String AZIMUTH = "azimuth";

    /** The Constant MACRO. */
    public static final String MACRO = "MACRO";

    /** The Constant MICRO. */
    public static final String MICRO = "MICRO";

    /** The Constant PICO. */
    public static final String PICO = "PICO";

    /** The Constant SITENAME. */
    public static final String SITENAME = "siteName";
    /** The Constant VENDOR_CAPITALISE. */
    public static final String VENDOR_CAPITALISE = "Vendor";
    public static final String ERROR_NO_SMALL_CELLS_AVAILABLE = "No Small Cells available";
    /** The Constant TYPE_LIST. */
    public static final String TYPE_LIST = "typeList";

    /** The Constant NAME LIST. */
    public static final String NAME_LIST = "nameList";

    /** The Constant PARAM PREFIX. */
    public static final String PARAM_PREFIX = "paramPrefix";

    /** The Constant SYSTEM_CONFIGURATION. */
    public static final String SYSTEM_CONFIGURATION = "SystemConfiguration";

    /** The Constant PROCESS_CONFIGURATION. */
    public static final String PROCESS_CONFIGURATION = "ProcessConfiguration";
    /** The Constant TYPE. */
    public static final String TYPE = "type";
    /** The Constant NAME. */
    public static final String CONFIGTYPE = "configType";
    /** The Constant SF_DATA_SEPARATOR. */
    public static final String SF_DATA_SEPARATOR = "\\|";
    /** The Constant TRUE_LOWERCASE. */
    public static final String TRUE_LOWERCASE = "true";

    /** The Constant FALSE_LOWERCASE. */
    public static final String FALSE_LOWERCASE = "false";
    /** The Constant OPEN_BRACKET. */
    public static final String OPEN_BRACKET = "(";
    /** The Constant EXTENSION_CSV. */
    public static final String EXTENSION_CSV = ".csv";

    /** The Constant CLOSED_BRACKET. */
    public static final String CLOSED_BRACKET = ")";

    /** The Constant CLOSING_PARANTHESIS. */
    public static final String CLOSING_PARANTHESIS = ")";

    /** The Constant OPENING_ANGULAR_BRACKET. */
    public static final String OPENING_ANGULAR_BRACKET = "<";

    /** The Constant CLOSING_ANGULAR_BRACKET. */
    public static final String CLOSING_ANGULAR_BRACKET = ">";
    /** The Constant MESSAGE. */
    public static final String MESSAGE = "message";
    /** The Constant PROBLEMTYPE. */
    public static final String PROBLEMTYPE = "problemType";

    /** The Constant PROBLEMSUBTYPE. */
    public static final String PROBLEMSUBTYPE = "problemSubtype";

    /** The Constant USER_CAMEL_CASE. */
    public static final String USER_CAMEL_CASE = "User";

    /** The Constant MODIFIED_VALUE. */
    public static final String MODIFIED_VALUE = "Modified Value";

    /** The Constant MODIFIED_TIME_SPACE. */
    public static final String MODIFIED_TIME_SPACE = "Modified Time";

    /** The Constant MODIFIED_SINCE. */
    public static final String MODIFIED_SINCE = "Modified Since";

    /** The Constant CONSUMERFEEDBACKREPORT. */
    public static final String CONSUMERFEEDBACKREPORT = "ConsumerFeedbackReport";

    /** The Constant CONSUMERFEEDBACKFORMAT. */
    public static final String CONSUMERFEEDBACKFORMAT = "ConsumerFeedbackReport.xlsx";

    /** The Constant CONSUMERFEEDBACKFORMATT. */
    public static final String CONSUMERFEEDBACKFORMATT = "ConsumerFeedbackReportt.csv";

    /** The Constant CONSUMER_FEEDBACK_REPORT_PREFIX. */
    public static final String CONSUMER_FEEDBACK_REPORT_PREFIX = "NV-FE";
    /** ******************* LAYERS CONSTANTS *******************. */
    public static final String SERVICE = "Service";

    /** The Constant DAO. */
    public static final String DAO = "Dao";

    /** The Constant REST. */
    public static final String REST = "Rest";

    /** The Constant GENERIC_MODEL. */
    public static final String GENERIC_MODEL = "999";

    /** The Constant ZOOKEEPER_SLEEP_TIMEOUT. */
    public static final long ZOOKEEPER_SLEEP_TIMEOUT = 5000;

    /** The Constant HEATMAP_IMAGE_NAME. */
    public static final String HEATMAP_IMAGE_NAME = "heatmap.jpeg";

    /** The Constant JPEG_EXTENSION. */
    public static final String JPEG_EXTENSION = ".jpeg";

    /** The Constant JPEG_EXTENSION. */
    public static final String JPG_EXTENSION = ".jpg";

    /** The Constant JPEG. */
    public static final String JPG = "JPG";
    public static final String SESSION_EXPIRED = "SESSION EXPIRED";

    /** The Constant EXCEPTION_NULL_POINTER. */
    public static final String EXCEPTION_NULL_POINTER = "Unexpected null value found somewhere";

    /** The Constant EXCEPTION_UNABLE_TO_PARSE_DATA. */
    public static final String EXCEPTION_UNABLE_TO_PARSE_DATA = "Unable to read data received from server.";

    /** The Constant COORDINATES_18. */
    public static final String COORDINATES_18 = "coordinates18";

    /** The Constant IS_LARGE_AREA. */
    public static final String IS_LARGE_AREA = "isLargeArea";

    /** The Constant R_LOWERCASE. */
    public static final String R_LOWERCASE = "r";

    public static final String L1 = "L1";

    /** The Constant L2. */
    public static final String L2 = "L2";

    /** The Constant L3. */
    public static final String L3 = "L3";

    /** The Constant L4. */
    public static final String L4 = "L4";

    /** The Constant L5. */
    public static final String L5 = "L5";

    public static final String OTHER="Other";
    
    /** The Constant SUCCESS. */
    public static final String SUCCESS = "Success";

    /** The Constant FAILURE. */
    public static final String FAILURE = "Failure";
    /** The Constant FAIL. */
    public static final String FAIL = "Fail";
    /** The Constant ENABLE. */
    public static final String ENABLE = "ENABLE";

    /** The Constant DISABLE. */
    public static final String DISABLE = "DISABLE";

    /** The Constant ACTIVATE. */
    public static final String ACTIVATE = "ACTIVATE";

    /** The Constant NOHUP. */
    public static final String NOHUP = "nohup";

    /** The Constant EXPECT. */
    public static final String EXPECT = "expect";
    /** The Constant DELETED. */
    public static final String DELETED = "DELETED";
    /** The Constant DELETED. */
    public static final String IS_DELETED = "isDeleted";
    /** The Constant FILEPATH. */
    // new constant
    public static final String FILEPATH = "filePath";

    /** The Constant FALSE. */
    public static final Boolean FALSE = false;
    /** The Constant GEOGRAPHY. */
    public static final String GEOGRAPHY_SMALL = "geography";
    /** The Constant BAND_SMALL. */
    public static final String BAND_SMALL = "band";
    /** The Constant INDEX_ZERO. */
    public static final Integer INDEX_ZERO = 0;

    /** The Constant INDEX_TEN. */
    public static final Integer INDEX_TEN = 10;

    /** The Constant INDEX_ONE. */
    public static final Integer INDEX_ONE = 1;

    /** The Constant INDEX_TWO. */
    public static final Integer INDEX_TWO = 2;

    /** The Constant INDEX_THREE. */
    public static final Integer INDEX_THREE = 3;

    /** The Constant INDEX_FOUR. */
    public static final Integer INDEX_FOUR = 4;
    /** The Constant INDEX_FOUR. */
    public static final Integer INDEX_FIVE = 5;
    /** The Constant CLOSE_CURLY_BRACE. */
    public static final String CLOSE_CURLY_BRACE = "}";

    /** The Constant OPEN_CURLY_BRACE. */
    public static final String OPEN_CURLY_BRACE = "{";
    /** The date formate dd mmm yyyy. */
    public static final String DATE_FORMATE_DD_MMM_YYYY = "dd MMM, yyyy";

    /** The time formate hh mm a. */
    public static final String TIME_FORMATE_HH_MM_A = "hh:mm a";
    /** The Constant STRING_1800. */
    public static final String STRING_1800 = "1800";
    /** The Constant FIFTY_THREE. */
    public static final int FIFTY_THREE = 53;
    /** The Constant STRING_2300. */
    public static final String STRING_2300 = "2300";

    /** The Constant STRING_850. */
    public static final String STRING_850 = "850";

    /** The Constant STRING_SITE_LEVEL_ALARM. */
    public static final String STRING_SITE_LEVEL_ALARM = "A";
    /** The Constant CLOSE. */
    public static final String CLOSE = "close";
    /** The Constant ZERO_INTEGER. */
    public static final Integer ZERO_INTEGER = 0;
    /** The Constant STRING_ONE. */
    public static final String STRING_ONE = "1";

    /** The Constant STRING_TWO. */
    public static final String STRING_TWO = "2";

    /** The Constant PLUS_SIGN. */
    public static final String PLUS_SIGN = "+";
    public static final Throwable EXCEPTION_UNABLE_TO_CREATE_REPORT = null;
    public static final String FAILED = null;
    /** The response 200. */
    public static Integer RESPONSE_200 = 200;
    /** The Constant ZERO_IN_STRING. */
    public static final String ZERO_IN_STRING = "0";

    /** The Constant DEFAULT_MAX_PAGINATION_SIZE. */
    public static final Integer DEFAULT_MAX_PAGINATION_SIZE = 10;

    /** The Constant DEFAULT_LOWER_PAGINATION_LIMIT. */
    public static final Integer DEFAULT_LOWER_PAGINATION_LIMIT = 0;
    /** The Constant CONTENT_DISPOSITION. */
    public static final String CONTENT_DISPOSITION = "Content-Disposition";

    /** The Constant SMALL_KEY. */
    public static final String SMALL_KEY = "key";

    /** The Constant COM. */
    public static final String COM = "COM";

    /** The Constant START_DATE. */
    public static final String START_DATE = "startDate";
    /** The Constant END_DATE. */
    public static final String END_DATE = "endDate";

    /** The Constant KML_JOB_HISTORY_KEY. */
    public static final String KML_JOB_HISTORY_KEY = "MITIGATION_COVERAGE_HOLE_";
    /** The Constant DATE_DDMMYYYY. */
    public static final String DATE_DDMMYYYY = "ddMMyyyy";

    /** The Constant MEGA_HZ. */
    public static final String MEGA_HZ = "Mhz";
    /** The Constant KML_EXTENSION. */
    public static final String KML_EXTENSION = ".kml";
    /** The Constant DOWNLOAD_POLYGON. */
    public static final String DOWNLOAD_POLYGON = "DOWNLOAD_POLYGON";
    /** The Constant HDFS_REPO_STORAGE_TYPE. */
    public static final String HDFS_REPO_STORAGE_TYPE = "HDFS";
    /** The Constant HDFS_REPO_STORAGE_TYPE. */
    public static final String HBASE_REPO_STORAGE_TYPE = "HBASE";
    public static final String INVALID_STORAGE_TYPE = "result\":\"Invalid Storage Type \"";
    public static final String DOT_SPLIT_STRING = "\\.";
    public static final int FILE_NAME_WITHOUT_EXTENSION_INDEX = 0;
    
    /** The Constant FILE_PATH_JSON. */
    public static final String FILE_PATH_JSON = "{\"filePath\":\"%s\"}";
    /** The Constant EXCEPTION_DATA_MEASURE_FAILURE. */
    public static final String EXCEPTION_DATA_MEASURE_FAILURE = "Failure - Data not available";
    public static final String CATEGORY = "category";
    public static final String COUNT = "count";
    /** The Constant PROCESS_RUNNER_CREDENTIAL. */
    public static final String PROCESS_RUNNER_CREDENTIAL = "PROCESS_RUNNER_CREDENTIAL";
    /** The Constant PROCESS_RUNNER_GET_ALL_JOBS. */
    public static final String PROCESS_RUNNER_GET_ALL_JOBS = "PROCESS_RUNNER_GET_ALL_JOBS";
    /** The Constant DATEMONTHYEAR_SPACE_SEPRATED. */
    public static final String DATEMONTHYEAR_SPACE_SEPRATED = "dd MM yy";

    /** The Constant DATE_FORMAT_YYYYMMDD. */
    public static final String DATE_FORMAT_YYYYMMDD = "yyyyMMdd";
    /** The Constant LAYER_2. */
    public static final String LEVEL_2 = "GeographyL2";

    /** The Constant NEW_LINE. */
    public static final String NEW_LINE = "\n";

    /** The Constant CSV_EXTENSION. */
    public static final String CSV_EXTENSION = ".csv";
    /** The Constant CONTENT_APPLICATION_JSON. */
    public static final String CONTENT_APPLICATION_JSON = "application/json";

    public static final String SINGLEQUOTE = "'";
    /** The quote. */
    public static final String QUOTE = "\"";
    /** The Constant TILDE. */
    public static final String TILDE = "~";

    /** The Constant SELECTPICKER. */
    public static final String SELECTPICKER = "SELECTPICKER";
    /** The text box. */
    public static final String TEXT_BOX = "TEXT_BOX";

    /** The Constant FILENAME. */
    public static final String FILENAME = "fileName";
    /** The Constant DATE_FORMAT_DDMMYY. */
    public static final String DATE_FORMAT_DDMMYY = "ddMMyy";
    /** The Constant ZERO_VALUE. */
    public static final int ZERO_VALUE = 0;
    /** The Constant GEOGRAPHY_L4fk. */

    /** The Constant MAX_DATE. */
    /** The Constant UNABLE_TO_PROCESS_REQUEST. */
    public static final String UNABLE_TO_PROCESS_REQUEST = "Unable to Process Request";
    /** The Constant UNAUTHORIZED_MOBILE_LOGIN. */
    public static final String UNAUTHORIZED_MOBILE_LOGIN = "User don't have mobile login permission";
    /** The Constant ZERO_INT. */
    public static final int ZERO_INT = 0;
    /** The Constant TRUE_CAPS. */
    public static final String TRUE_CAPS = "TRUE";

    /** The Constant FALSE_CAPS. */
    public static final String FALSE_CAPS = "FALSE";

    /** The Constant ELEVEN. */
    public static final int ELEVEN = 11;
    /** The Constant HUNDRED_VALUE. */
    public static final int HUNDRED_VALUE = 100;
    /** The Constant MINUS_ONE. */
    public static final int MINUS_ONE = -1;
    /** The Constant ONE_INT. */
    public static final int ONE_INT = 1;

    /** The Constant SEVEN. */
    public static final int SEVEN = 7;
    /** The Constant SUPER_ADMIN. */
    public static final String SUPER_ADMIN = "Super Admin";
    /** The Constant SUPER_ADMIN_TEAM. */
    public static final String SUPER_ADMIN_TEAM = "SUPER-ADMIN";

    public static final String ANALYTICAL_REPORT_KEY = "analyticsrepositoryid_pk";

    public static final String GEOGRAPHY_L3_LIST = "GeographyL3List";

    /** The Constant AMPERSAND. */
    public static final String AMPERSAND = "&";

    /** The Constant QUESTIONMARK. */
    public static final String QUESTIONMARK = "?";

    /** The Constant EQUALS. */
    public static final String EQUALS = "=";

    /** The Constant PRIORITYVALUE. */
    public static final String PRIORITYVALUE = "priorityValue";

    /** The Constant GOOGLE_URL_PREFIX. */
    public static final String GOOGLE_URL_PREFIX = "GOOGLE_URL_PREFIX";

    /** The Constant GOOGLE_URL_SUFFIX. */
    public static final String GOOGLE_URL_SUFFIX = "GOOGLE_URL_SUFFIX";

    /** The Constant GOOGLE_KEY_STRING. */
    public static final String GOOGLE_KEY_STRING = "GOOGLE_KEY_STRING";

    /** The Constant GALLERY_STATUS_UPDATED_SUCCESSFULLY. */
    public static final String GALLERY_STATUS_UPDATED_SUCCESSFULLY = "Gallery Status updated successfully";

    /** The Constant DOLLER. */
    public static final String DOLLER = "$";

    /** The Constant BUILDING_SEARCH_KEY. */
    public static final String BUILDING_SEARCH_KEY = "BUILDING";

    /** The Constant GALLERY_DETAIL_SEARCH_KEY. */
    public static final String GALLERY_DETAIL_SEARCH_KEY = "GALLERY";

    /** The Constant SMILE_SEARCH_KEY. */
    public static final String SMILE_SEARCH_KEY = "SMILE";

    /** The Constant OUTLET. */
    public static final String OUTLET = "OUTLET";

    /** The Constant BTS. */
    public static final String BTS = "BTS";

    /** The Constant NENAME_UPPERCASE. */
    public static final String NENAME_UPPERCASE = "NENAME";

    /** The Constant SITE. */
    public static final String SITE = "site";

    /** The Constant IPV4. */
    public static final String IPV4 = "ipv4";

    /** The Constant DRIVER_CLASSNAME. */
    public static final String DRIVER_CLASSNAME = "DRIVER_CLASSNAME";

    /** The Constant STATUS. */
    public static final String STATUS = "status";

    public static final String NOTIFICATIONTYPE = "notificationType";

    public static final String IDS = "ids";

    /** The Constant DOT. */
    public static final String DOT = ".";

    /** The Constant OPEN_SQUARE_BRACKET. */
    public static final String OPEN_SQUARE_BRACKET = "[";

    /** The Constant CLOSED_SQUARE_BRACKET. */
    public static final String CLOSED_SQUARE_BRACKET = "]";

    /** The Constant GEOGRAPHY_L4fk. */
    public static final String GEOGRAPHY_L4fk = "geographyL4";

    /** The Constant GEOGRAPHY_L3fk. */
    public static final String GEOGRAPHY_L3fk = "geographyL3";

    /** The Constant GEOGRAPHY_L2fk. */
    public static final String GEOGRAPHY_L2fk = "geographyL2";

    /** The Constant GEOGRAPHY_L1fk. */
    public static final String GEOGRAPHY_L1fk = "geographyL1";

    /** The Constant totalsamples. */
    public static final String totalsamples = "sampleCount0";

    /** The Constant validsamples. */
    public static final String validsamples = "sampleCount1";

    /** The Constant AVG_VALUE. */
    public static final String SUM_VALUE = "sum";

    /** The Constant RANGE0. */
    public static final String RANGE0 = "range0";

    /** The Constant RANGE1. */
    public static final String RANGE1 = "range1";

    /** The Constant RANGE2. */
    public static final String RANGE2 = "range2";

    /** The Constant ENVIRONMENT. */
    public static final String ENVIRONMENT = "env";

    /** The Constant DATASOURCE. */
    public static final String DATASOURCE = "dataSource";

    /** The Constant KPI_NAME. */
    public static final String KPI_NAME = "kpiname";

    /** The Constant MODULE. */
    public static final String MODULE = "module";

    /** The Constant EMPTYSPACE. */
    public static final String EMPTYSPACE = "";

    /** The Constant COM_ENVIRONMENT. */
    public static final String COM_ENVIRONMENT = "C";

    /** The Constant ISLATEST. */
    public static final String ISLATEST = "isLatest";

    /** The Constant SWLON. */
    public static final String SWLON = "SWLng";

    /** The Constant SWLAT. */
    public static final String SWLAT = "SWLat";

    /** The Constant NELON. */
    public static final String NELON = "NELng";

    /** The Constant NELAT. */
    public static final String NELAT = "NELat";
    /** The Constant GEOGRAPHYTYPE. */
    public static final String GEOGRAPHYTYPE = "geographyType";

    /** The Constant MIN_DATE. */
    public static final String MIN_DATE = "mindate";

    /** The Constant MAX_DATE. */
    public static final String MAX_DATE = "maxdate";

    /** The Constant FREQUENCY_BAND. */
    public static final String FREQUENCY_BAND = "frequencyBand";

    /** The Constant KPIKEY. */
    public static final String KPIKEY = "kpi";

    /** The Constant BAND_HBASE. */
    public static final String BAND_HBASE = "band";

    /** The Constant recordtype. */
    public static final String recordtype = "recordtype";

    /** The yyyy M M dd H H mm ss. */
    public static final String YYYY_MM_DD_HH_MM_SS_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /** The Constant KML_NAME. */
    public static final String KML_NAME = "kmlName";

    /** The Constant ZOOM_LEVEL. */
    public static final String ZOOM_LEVEL = "zoomLevel";

    /** The Constant FILE. */
    public static final String FILE = "file";
    /** The Constant ERRORMSG. */
    public static final String ERRORMSG = "errorMsg";

    /** The Constant COLORCODE. */
    public static final String COLORCODE = "colorCode";

    /** The Constant PLACEMARK. */
    public static final String PLACEMARK = "Placemark";

    /** The Constant CREATEDTIME. */
    public static final String CREATEDTIME = "createdTime";

    /** The Constant HASH. */
    public static final String HASH = "#";

    public static final String TOMCAT_PATH = "catalina.base";

    /** The Constant XLSX. */
    public static final String XLSX = "xlsx";

    /** The Constant XLS. */
    public static final String XLS = "xls";

    /** The Constant PLANNED_UPPERCASE. */
    public static final String PLANNED_UPPERCASE = "PLANNED";

    /** The Constant SITE_POLYGON. */
    public static final String SITE_POLYGON = "sitepolygon";

    /** The Constant LATITUDE_SMALL. */
    public static final String LAT_SMALL = "lat";

    /** The Constant LONGITUDE_SMALL. */
    public static final String LONG_SMALL = "long";

    /** The Constant DASH. */
    public static final String DASH = "-";

    /** The Constant FONTNAME. */
    public static final String FONTNAME = "Calibri";

    /** The Constant DATACELL_KEY. */
    public static final String DATACELL_KEY = "datacell";

    public static final String DATE_FORMAT_yyyy_MM_DD = "yyyy_MM_dd";

    /** The Constant ORANGEHEADER. */
    public static final String ORANGEHEADER = "orangeHeader";

    /** The Constant YELLOWHEADER. */
    public static final String YELLOWHEADER = "yellowHeader";

    /** The Constant BLUEHEADER. */
    public static final String BLUEHEADER = "blueHeader";

    /** The Constant DATE_CAMEL. */
    public static final String DATE_CAMEL = "Date";

    /** The Constant STATUS_CAPS. */
    public static final String STATUS_CAMEL = "Status";

    /** The Constant USER. */
    public static final String USER = "user";

    public static final String EXCEL_REPORT_PATH = "EXCEL_REPORT_PATH";

    /** The Constant COORDINATES. */
    public static final String COORDINATES = "coordinates";

    /** The Constant POLYGON. */
    public static final String POLYGON = "Polygon";

    /** The Constant POINT. */
    public static final String POINT = "Point";

    /** The Constant LINE_STRING. */
    public static final String LINE_STRING = "LineString";

    /** The Constant DEGREE_REF_LNG. */
    public static final String DEGREE_REF_LNG = "DEGREE_REF_LNG";

    /** The Constant DEGREE_REF_LAT. */
    public static final String DEGREE_REF_LAT = "DEGREE_REF_LAT";

    public static final byte[] COLUMN_FAMILY_AMSL = Bytes.toBytes("r");

    public static final byte[] COLUMN_AMSL_IMAGE = Bytes.toBytes("i");

    public static final Integer AMSL_ZOOM_LEVEL = 15;

    public static final String AMSL_ACCURACY_FACTOR_KEY = "AMSL_ACCURACY_FACTOR";

    public static final String ZOOM_MAP_KEY = "TILE_TO_ZONE_MAP";

    public static final String FACEBOOK_POPULATION_TABLE = "Population";

    public static final String AMSL_TABLE = "AMSL";

    public static final String AMSL_LEGENDS_KEY = "AMSLLEGENDS";

    public static final String FACEBOOK_LEGENDS_KEY = "POPULATIONLEGENDS";

    public static final String MIN_COLOR_RANGE = "MinRange";

    public static final String MAX_COLOR_RANGE = "MaxRange";

    public static final String PIXEL_COLOR = "Color";

    public static final byte[] COLUMN_FAMILY_CLUTTER = Bytes.toBytes("r");

    public static final String LOS_FREQUENCY = "los_frequency";

    public static final String FAILED_JSON = "{\"result\":\"failure\",\"messaage\":\"invalid param\"}";

    public static final String TRUE_STRING = "true";

    public static final Integer FOURTEEN = 14;

    public static final String WHITE_SPACE = "\\s";

    /** The Constant PROCESS_CONFIG_UPDATED_SUCCESSFULLY. */
    public static final String PROCESS_CONFIG_UPDATED_SUCCESSFULLY = "Configuration updated successfully";

    /** The Constant MEGA_HERTZ. */
    public static final String MEGA_HERTZ = "MHz";

    public static final String APPROVAL_CYCLE_UPDATED_SUCCESSFULLY = "APPROVAL_CYCLE_UPDATED_SUCCESSFULLY";

    public static final String APPROVAL_CYCLE = "APPROVAL_CYCLE";

    /** The Constant WO_TYPE. */
    public static final String WO_TYPE = "woType";

    /** The Constant RESULT. */
    public static final String RESULT = "result";

    /** The Constant HISTORY. */
    public static final String HISTORY = "HISTORY";

    /** The Constant INDEX_SIX. */
    public static final Integer INDEX_SIX = 6;

    /** The Constant NO_IMPACT. */
    public static final String NO_IMPACT = "NoImpact";

    /** The Constant IMPROVE. */
    public static final String IMPROVE = "Improve";

    /** The Constant DEGRATE. */
    public static final String DEGRADE = "Degrade";

    /** The Constant TWO_DECIMAL_FORMAT. */
    public static final String TWO_DECIMAL_FORMAT = "##.##";

    /** The Constant FAVOURITE_TYPE_SPATIAL. */
    public static final String FAVOURITE_TYPE_SPATIAL = "Spatial";

    /** The Constant FAVOURITE_TYPE_DASHBOARD. */
    public static final String FAVOURITE_TYPE_DASHBOARD = "Dashboard";

    public static final int SIXTY = 60;

    public static final String OPERATOR = "operatorName";

    public static final String OPERATOR_DETAIL = "OperatorDetail";

    /** The Constant OPERATOR_KEY. */
    public static final String OPERATOR_KEY = "operator";

    /** The Constant COUNTRY_KEY. */
    public static final String COUNTRY_KEY = "country";

    /** The Constant COUNTRY_NAME. */
    public static final String COUNTRY_NAME = "countryName";

    /** The Constant SEARCH_TYPE. */
    public static final String SEARCH_TYPE = "searchType";

    public static final String ORGTYPE_OPERATOR = "OPERATOR";

    /** The Constant EMPTY_FILE_JSON. */
    public static final String EMPTY_FILE = "Uploaded File is Empty";;

    /** The Constant AUTHENTICATION_FAILED. */
    public static final String AUTHENTICATION_FAILED = "Authentication Failed";

    /** The Constant NONE. */
    public static final String NONE = "None";

    /** The Constant LATEST. */
    public static final String LATEST = "latest";

    /** The Constant VENDOR_ALL. */
    public static final String VENDOR_ALL = "ALL";

    /** The Constant L1_SALES. */
    public static final String L1_SALES = "L1_SALES";

    /** The Constant L2_SALES. */
    public static final String L2_SALES = "L2_SALES";

    /** The Constant L3_SALES. */
    public static final String L3_SALES = "L3_SALES";

    /** The Constant L4_SALES. */
    public static final String L4_SALES = "L4_SALES";

    /** The Constant RSRQ_PREFIX. */
    public static final Object RSRQ_PREFIX = "RQ";

    /** The Constant UPDATE_SUCCESS. */
    public static final String UPDATE_SUCCESS = "{\"successMsg\":\"Your request has been received,Requested data will be proccessed in first week of next month.\"}";

    /** The Constant UPDATE_FAILURE. */
    public static final String UPDATE_FAILURE = "{\"errorMsg\":\"Records Not Updated\"}";

    /** The Constant REGEX_REMOVE_MULTIPLE_SPACES. */
    public static final String REGEX_REMOVE_MULTIPLE_SPACES = "( )+";

    /** The Constant SALES_BOUNDARY_DATE. */
    public static final String SALES_BOUNDARY_DATE = "SALES_BOUNDARY_DATE";

    /** The Constant SALES_GEOGRAPHY_HISTORY_DATE. */
    public static final String SALES_GEOGRAPHY_HISTORY_DATE = "SALES_GEOGRAPHY_HISTORY_DATE";

    /** The Constant SALES_GEOGRAPHY_TABLE. */
    public static final String SALES_GEOGRAPHY_TABLE = "SalesGeography";

    /** The Constant SALES_BOUNDARY_L1_DATE. */
    public static final String SALES_BOUNDARY_L1_DATE = "SALES_BOUNDARY_L1_DATE";

    /** The Constant SALES_BOUNDARY_L2_DATE. */
    public static final String SALES_BOUNDARY_L2_DATE = "SALES_BOUNDARY_L2_DATE";

    /** The Constant SALES_BOUNDARY_L3_DATE. */
    public static final String SALES_BOUNDARY_L3_DATE = "SALES_BOUNDARY_L3_DATE";

    /** The Constant SALES_BOUNDARY_L4_DATE. */
    public static final String SALES_BOUNDARY_L4_DATE = "SALES_BOUNDARY_L4_DATE";

    public static final Integer MAX_ROWS_WORKSHEET = 1000000;

    /** The Constant MAX_ZOOMLEVEL. */
    public static final Integer MAX_ZOOMLEVEL = 15;

    /** The Constant MIN_ZOOMLEVEL. */
    public static final Integer MIN_ZOOMLEVEL = 5;

    /** The Constant CACHE_SIZE. */
    public static final Integer CACHE_SIZE = 10;

    public static final String SYSTEM_CONFIGURATION_KEY_MISSING_MESSAGE = "System configuration key is missing ";

    public static final Object CONFIGURATION = "configuration";

    public static final double CLUTTER_HEIGHT_CONSTANT = 5.85;

    public static final Integer AZUMITH_MAX = 360;

    public static final String SQL_STATEMENT = "SQL_STATEMENT";
    public static final String PROC_NAME = "PROC_NAME";

    /** The Constant END_ROW_INDEX. */
    public static final int END_ROW_INDEX = 255;

    public static final String ATTACHMENTFILENAME = "attachment; filename=\"";
    public static final String RESULTFAILURE = "{\"Result\" : \"Failure\"}";

    /** The Constant TWELVE. */
    public static final Integer TWELVE = 12;

    /** The Constant EIGHT. */
    public static final int EIGHT = 8;
    public static final String ERRORCODEFIVE = "?errorCode=5";
    public static final String ERRORCODEONE = "?errorCode=1";
    public static final String LOGINJSP = "/login.jsp";

    public static final String LANGUAGETYPE = "languageType";
    public static final String REMARK = "remark";

    public static final String RANDETAIL = "RANDetail";

    public static final String NETWORKELEMENT = "NetworkElement";

    /** The Constant FACEBOOK_POPULATION_COLUMN. */
    public static final String FACEBOOK_POPULATION_COLUMN = "fbpop";

    /** The Constant ZOOM_LEVEL_TEN. */
    public static final int ZOOM_LEVEL_TEN = 10;

    /** The Constant GEOGRAPHY_NAME_COLUMN. */
    public static final String GEOGRAPHY_NAME_COLUMN = "gn";

    /** The Constant HBASE_COLUMN_FAMILY. */
    public static final String HBASE_COLUMN_FAMILY = "r";

    /** The Constant ZOOM_LEVEL_FIFTEEN. */
    public static final int ZOOM_LEVEL_FIFTEEN = 15;
    public static final double FIFTY = 50.00;

    /** The Constant ZOOM_LEVEL_THIRTEEN. */
    public static final Integer ZOOM_LEVEL_THIRTEEN = 13;
    public static final String TO_DELETED_KEY = "deleteKey";

    /** The Constant NETWORK_GEOGRAPHY_HISTORY_DATE. */
    public static final String NETWORK_GEOGRAPHY_HISTORY_DATE = "NETWORK_GEOGRAPHY_HISTORY_DATE";

    /** The Constant NETWORK_DATA_INPROGRESS. */
    public static final String NETWORK_DATA_INPROGRESS = "NETWORK_DATA_INPROGRESS";

    // config Constanst
    public static final String IS_ENCRYPTION_MOBILE = "IS_ENCRYPTION_MOBILE";

    public static final String NETWORK_BOUNDARY_L1_DATE = "NETWORK_BOUNDARY_L1_DATE";

    public static final String NETWORK_BOUNDARY_L2_DATE = "NETWORK_BOUNDARY_L2_DATE";

    public static final String NETWORK_BOUNDARY_L3_DATE = "NETWORK_BOUNDARY_L3_DATE";

    public static final String NETWORK_BOUNDARY_L4_DATE = "NETWORK_BOUNDARY_L4_DATE";

    public static final String PROTECTED_PATH = "/protected";

    public static final String NENAME_LIST = "neNameList";
    public static final String ALERTID = "alertid";

    public static final String EMS_NAME_CAMEL = "emsName";

    public static final String KEY_IP = "ip";

    public static final String KEY_EMS = "EMS";

    public static final String MACRO_CELL = "MACRO_CELL";
    public static final String ROWKEY_PREFIX = "rowkeyPrefix";

    public static final String KEY_VIP = "VIP";

    /* bulkuplaod constant */

    public static final String USER_NAME = "User Name";
    public static final String FIRST_NAME = "First Name";
    public static final String MIDDLE_NAME = "Middle Name";
    public static final String EMAILD_ID = "Email ID";
    public static final String LAST_NAME = "Last Name";
    public static final String PHONE_NUMBER = "Phone Number";
    public static final String ACCESS_LEVEL = "Access Level";
    public static final String BULKUPLOAD_ACCESS_LEVEL = "BULKUPLOAD_ACCESS_LEVEL";
    public static final String SP_FORESIGHT = "SP_FORESIGHT";
    public static final String SP_SITEFORGE = "SP_SITEFORGE";

    public static final String WEB_INTERNET = "web internet";
    public static final String WEB_INTRANET = "web intranet";
    public static final String MOBILE_INTERNET = "mobile internet";
    public static final String MOBILE_INTRANET = "mobile intranet";
    public static final String ORGANIZATION_NAME = "Organization Name";
    public static final String AUTHENTICATION_TYPE = "Authentication Type";
    public static final String ROLE = "Role";
    public static final String WORKSPACE = "Workspace";
    public static final String TEAM = "Team";
    public static final String LEVEL = "Level";
    public static final String BULKUPLOAD_ZONE = "BULKUPLOAD_ZONE";
    public static final String BULKUPLOAD_CIRCLE = "BULKUPLOAD_CIRCLE";
    public static final String BULKUPLOAD_CITY = "BULKUPLOAD_CITY";
    public static final String BULKUPLOAD_NETWORK_CLUSTER = "BULKUPLOAD_NETWORK_CLUSTER";
    public static final String SALES_REGION = "Sales Region";
    public static final String SALES_CLUSTER = "Sales Cluster";
    public static final String SALES = "SALES";
    public static final String SALESL1 = "SALES_L1";
    public static final String SALESL2 = "SALES_L2";
    public static final String SALESL3 = "SALES_L3";
    public static final String SALESL4 = "SALES_L4";
    public static final String SALES_NHQ = "SALES_NHQ";
    public static final String NHQ = "NHQ";
    public static final String USER_STATUS = "User Status";
    public static final String CREATED_USER = "Created_User";
    public static final String CATALINA_BASE = "catalina.base";
    public static final String BULKUPLOAD_ENABLE_DISABLE = "BULKUPLOAD_ENABLE_DISABLE";
    public static final String TOMECAT_TEMP_PATH = "/temp/CREATED_FILE_";
    public static final String PATH = "path";
    public static final String FORESIGHT_ = "FORESIGHT_";
    public static final String ENODE_B_PREFIX = "ENB";
    public static final String MACRO_VDU = "MACRO_VDU";
    public static final String MACRO_ENB = "MACRO_ENB";
    public static final String VDU = "VDU";
    public static final String VCU = "VCU";
    public static final String RIU = "RIU";
    public static final String RRH = "RRH";
    public static final int LONGITUDE_INDEX = 1;
    public static final int LATITUDE_INDEX = 0;
    public static final String KPI_SUMMARY_LIST_SPLIT_SIZE = "KPI_SUMMARY_LIST_SPLIT_SIZE";
    public static final String GEOGRAPHYVALUE = "geographyValue";
    public static final String NEL_ID = "nelId";
    public static final String NEL_TYPE = "nelType";
    public static final String NELOCATION = "neLocation";
    public static final String PARAMETER = "parameter";
	public static final String DATE_TIME_DATEFORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String LOCATION_CODE = "locationCode";
	public static final String PINCODE = "pincode";
	public static final String ADDRESS = "address";
	public static final String ATP_1A="ATP 1A";
	public static final String ATP_1B="ATP 1B";
	public static final String ATP_1C="ATP 1C";
	public  static final String SFTP = "sftp";
	public  static final String NO = "no";
	public  static final String STRICT_HOST_KEY_CHECKING = "StrictHostKeyChecking";
    public  static final String NOT_EXISTS = "Not Exists";
    public  static final String OPERATIONAL_STATUS = "Operational Status";
    public  static final String ADMIN_STATE =  "Administrative State";
    public static final String SPACE_VALIDATION=".*([ \t]).*";
    public  static final String UNIT = "Unit";
	public static final String IN_SERVICE = "INSERVICE";
}

package com.inn.foresight.module.nv.wpt.analytics.constants;

import com.inn.commons.Symbol;
import com.inn.commons.lang.NumberUtils;

/**
 * The Class WPTAnalyticsConstants.
 *
 * @author innoeye
 * date - 28-Oct-2017 1:46:27 PM
 */
public class WPTAnalyticsConstants {

	/** Instantiates a new WPT analytics constants. */
	protected WPTAnalyticsConstants() {
	}
	
	/** The Constant DDMMYY. */
	//Date Constants
	public static final String DDMMYY = "ddMMyy";
	
	/** The Constant DDMMYYYY_SLASH. */
	public static final String DDMMYYYY_SLASH = "dd/MM/yyyy";
	
	/** The Constant YYYY_MM_DD. */
	public static final String YYYY_MM_DD = "yyyy-MM-dd HH:00:00";
	
	/** The Constant DATE_FORMAT_YYYY_MM_DD. */
	public static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd 00:00:00";
	 
	/** The Constant FROMDATE. */
	//Query Param Constants
	public static final String FROMDATE = "fromDate";
	
	/** The Constant TODATE. */
	public static final String TODATE = "toDate";
	
	/** The Constant OPERATOR. */
	public static final String OPERATOR = "operator";
	
	/** The Constant NETWORK. */
	public static final String NETWORK = "network";
	
	/** The Constant OPERATORA. */
	public static final String OPERATORA = "operatorA";
	
	/** The Constant OPERATORB. */
	public static final String OPERATORB = "operatorB";
	
	/** The Constant NETWORKA. */
	public static final String NETWORKA = "networkA";
	
	/** The Constant NETWORKB. */
	public static final String NETWORKB = "networkB";
	
	/** The Constant CONFIGA. */
	public static final String CONFIGA = "configA";
	
	/** The Constant CONFIGB. */
	public static final String CONFIGB = "configB";
	
	/** The Constant CONFIGURATION. */
	public static final String CONFIGURATION = "configuration";
	
	/** The Constant SITENAME. */
	public static final String SITENAME = "siteName";
	
	/** The Constant ISSITEWISE. */
	public static final String ISSITEWISE = "isSiteWise";
	
	/** The Constant CATEGORY. */
	public static final String CATEGORY = "category";
	
	/** The Constant IDENTIFIER. */
	public static final String IDENTIFIER = "identifier";
	
	/** The Constant CATEGORYID. */
	public static final String CATEGORYID = "categoryId";
	
	/** The Constant ULIMIT. */
	public static final String ULIMIT = "ulimit";
	
	/** The Constant LLIMIT. */
	public static final String LLIMIT = "llimit";
	
	/** The Constant GEOGRAPHY. */
	public static final String GEOGRAPHY = "geography";
	
	/** The Constant GEOGRAPHY_ID. */
	public static final String GEOGRAPHY_ID = "geographyId";
	
	/** The Constant TECHNOLOGY. */
	public static final String TECHNOLOGY = "technology";
	
	/** The Constant PROCESS_TYPE. */
	public static final String PROCESS_TYPE = "processType";
	
	/** The Constant GEOGRAPHY_LEVEL. */
	public static final String GEOGRAPHY_LEVEL = "geographyLevel";
	
	/** The Constant DATE. */
	public static final String DATE = "date";
	
	/** The Constant DATE_RANGE_PARAM. */
	public static final String DATE_RANGE_PARAM = "dateRangeParam";
	
	/** The Constant COMPARE_TYPE. */
	public static final String COMPARE_TYPE = "compareType";
	
	/** The Constant LOCATION. */
	public static final String LOCATION = "location";
	
	/** The Constant LOCATION_ID. */
	public static final String LOCATION_ID = "locationId";	
	
	/** The Constant FILTER. */
	public static final String FILTER = "filter";


	/** The Constant EXCEPTION_SOMETHING_WENT_WRONG. */
	//Response Messages
	public static final String EXCEPTION_SOMETHING_WENT_WRONG = "There is some problem in completing the request.";
	
	/** The Constant DATA_NOT_FOUND. */
	public static final String DATA_NOT_FOUND = "Data not found for the request.";
	
	/** The Constant INVALID_PARAMETER_JSON. */
	public static final String INVALID_PARAMETER_JSON = "{\"message\":\"Parameters given are invalid.\"}";
	
	/** The Constant SUCCESS_JSON. */
	public static final String SUCCESS_JSON = "{\"message\":\"Success\"}";
	
	/** The Constant FAILURE_JSON. */
	public static final String FAILURE_JSON = "{\"message\":\"Failure\"}";
	
	/** The Constant INVALID_COMPARE_TYPE. */
	public static final String INVALID_COMPARE_TYPE = "Invalid Comapare Type";
	
	/** The Constant INVALID_GEOGRAPHY_LEVEL. */
	public static final String INVALID_GEOGRAPHY_LEVEL = "Invalid Geography Level";
	
	/** The Constant NO_RESULT_EXCEPTION_LOGGER. */
	//Loggers
	public static final String NO_RESULT_EXCEPTION_LOGGER = "Getting NoResultException, {}";
	
	/** The Constant EXCEPTION_LOGGER. */
	public static final String EXCEPTION_LOGGER = "Getting Exception {}";
	
	/** The Constant RESULT_SIZE_LOGGER. */
	public static final String RESULT_SIZE_LOGGER = "Getting Result Size {}";
		
	/** The Constant WPT_ANALATICS_OPERATOR. */
	//Config Constants
	public static final String WPT_ANALATICS_OPERATOR = "WPT_ANALATICS_OPERATOR";
	
	/** The Constant WPT_ANALATICS_CONFIGURATION. */
	public static final String WPT_ANALATICS_CONFIGURATION = "WPT_ANALATICS_CONFIGURATION";
	
	/** The Constant WPT_ANALATICS_LOG_REPORT_HEADER. */
	public static final String WPT_ANALATICS_LOG_REPORT_HEADER = "WPT_ANALATICS_LOG_REPORT_HEADER";

	/** The Constant CURLY_BRACES_OPEN. */
	//Symbols
	public static final String CURLY_BRACES_OPEN = "{";
	
	/** The Constant CURLY_BRACES_CLOSE. */
	public static final String CURLY_BRACES_CLOSE = "}";
	
	/** The Constant QUOTE. */
	public static final String QUOTE = Symbol.QUOTE_STRING;
	
	/** The Constant COLON. */
	public static final String COLON = Symbol.COLON_STRING;
	
	/** The Constant SPACE. */
	public static final String SPACE = Symbol.SPACE_STRING;
	
	/** The Constant NEW_LINE. */
	public static final String NEW_LINE = "\n";
	
	/** The Constant DASH. */
	public static final String DASH = "-";
	
	/** The Constant BLANK_STRING. */
	public static final String BLANK_STRING = "";
	
	/** The Constant PIE1. */
	//Utility Constants
	public static final String PIE1 = "pie1";
	
	/** The Constant PIE2. */
	public static final String PIE2 = "pie2";
	
	/** The Constant ALL. */
	public static final String ALL = "ALL";
	
	/** The Constant COMMA. */
	public static final String COMMA = Symbol.COMMA_STRING;
	
	/** The Constant UNDERSCORE. */
	public static final String UNDERSCORE = Symbol.UNDERSCORE_STRING;
	
	/** The Constant SQUARE_BRACES_OPEN. */
	public static final String SQUARE_BRACES_OPEN = Symbol.BRACKET_OPEN_STRING;
	
	/** The Constant SQUARE_BRACES_CLOSE. */
	public static final String SQUARE_BRACES_CLOSE = Symbol.BRACKET_CLOSE_STRING;
	
	/** The Constant X_KEY. */
	public static final String X_KEY = "x";
	
	/** The Constant Y_KEY. */
	public static final String Y_KEY = "y";
	
	/** The Constant VALUE_KEY. */
	public static final String VALUE_KEY = "value";
	
	/** The Constant COUNT_KEY. */
	public static final String COUNT_KEY = "count";
	
	/** The Constant X. */
	public static final String X = QUOTE + X_KEY + QUOTE + COLON;
	
	/** The Constant Y. */
	public static final String Y = QUOTE + Y_KEY + QUOTE + COLON;
	
	/** The Constant VALUE. */
	public static final String VALUE = QUOTE + VALUE_KEY + QUOTE + COLON;
	
	/** The Constant COUNT. */
	public static final String COUNT = QUOTE + COUNT_KEY + QUOTE + COLON;
	
	/** The Constant WPT_ANALATICS_PAGINATION_LIMIT. */
	public static final String WPT_ANALATICS_PAGINATION_LIMIT = "WPT_ANALATICS_PAGINATION_LIMIT";
	
	/** The Constant WPT_URL_INDEX. */
	public static final Integer WPT_URL_INDEX = 30;
	
	/** The Constant INDORE. */
	public static final String INDORE = "Indore";
	
	/** The Constant OTHER. */
	public static final String OTHER = "Other";
	
	/** The Constant IPV4. */
	public static final String IPV4 = "IPV4";
	
	/** The Constant IPV6. */
	public static final String IPV6 = "IPV6";
	
	/** The Constant SITE. */
	public static final String SITE = "site";
	
	/** The Constant WPT_ANALATICS_LOG_REPORT_NAME. */
	public static final String WPT_ANALATICS_LOG_REPORT_NAME = "Detailed_Log_Report";
	
	/** The Constant CSV_EXTENSION. */
	public static final String CSV_EXTENSION = ".csv";
	
	/** The Constant DATERANGE. */
	public static final String DATERANGE = "Date Range";
	
	/** The Constant CITY_CAMELCASE. */
	public static final String CITY_CAMELCASE = "City";
	
	/** The Constant HIPHEN. */
	public static final String HIPHEN = Symbol.HYPHEN_STRING;
	
	/** The Constant MASKED_IMSI_PREFIX. */
	public static final String MASKED_IMSI_PREFIX = "XXXXXXXXXXX";
	
	/** The Constant TOTAL. */
	public static final String TOTAL = "total";
	
	/** The Constant ID. */
	public static final String ID = "id";
	
	/** The Constant TIME_CONSTANT_LIST. */
	public static final String TIME_CONSTANT_LIST = "timeConstantList";
	
	/** The Constant CENT. */
	public static final Integer CENT = 100;
	
	/** The Constant PREVIOUS_TIME_CONSTANT. */
	public static final Integer PREVIOUS_TIME_CONSTANT = 7;
	
	/** The Constant ZERO_INT. */
	public static final Integer ZERO_INT = NumberUtils.INTEGER_ZERO;
	
	/** The Constant ONE_INT. */
	public static final Integer ONE_INT = NumberUtils.INTEGER_ONE;
	
	/** The Constant NULL_STRING. */
	public static final String NULL_STRING = "null";
	
	/** The Constant NaN. */
	public static final String NAN = "NaN";
	
	/** The Constant TESTED_URL. */
	public static final String TESTED_URL = "TestedUrl";
	
	/** The Constant PING_URL. */
	public static final String PING_URL = "PingUrl";
	
	/** The Constant TRACE_ROUTE_URL. */
	public static final String TRACE_ROUTE_URL = "TraceRouteUrl";
	
	/** The Constant TOTAL_RECORD. */
	public static final String TOTAL_RECORD = "TotalRecord";
	
	/** The Constant TOTAL_DEVICE. */
	public static final String TOTAL_DEVICE = "TotalDevice";
	
	/** The Constant TOTAL_URL. */
	public static final String TOTAL_URL = "TotalUrl";
	
	/** The Constant ISPINGDATAREQUIRED. */
	public static final String ISPINGDATAREQUIRED = "isPingDataRequired";
	
	/** The Constant DATA. */
	public static final String DATA = "data";
	
	/** The Constant JAVAX_PERSISTENCE_CACHE_STOREMODE. */
	public static final String JAVAX_PERSISTENCE_CACHE_STOREMODE = "javax.persistence.cache.storeMode";
	
	/** The Constant AVERAGE. */
	public static final String AVERAGE = "average";
	
	/** The Constant AVG_HTTP. */
	public static final String AVG_HTTP = "avgHttp";
	
	/** The Constant AVG_HTTPS. */
	public static final String AVG_HTTPS = "avgHttps";
	
	/** The Constant HTTPS. */
	public static final String HTTPS = "https";
	
	/** The Constant KPI. */
	public static final String KPI = "kpi";
	
	/** The Constant IPVERSION. */
	public static final String IPVERSION = "ipVersion";
	
	/** The Constant COMPARE_IPV. */
	public static final String COMPARE_IPV = "compareIPV";
	
	/** The Constant TRUE. */
	public static final String TRUE = "true";
	
	/** The Constant FALSE. */
	public static final String FALSE = "false";
	
	/** The Constant COMPARATOR_TECH_LIST. */
	public static final String COMPARATOR_TECH_LIST = "comparatorTechList";
	
	/** The Constant GEOGRAPHYL1. */
	public static final String GEOGRAPHYL1 = "geographyL1";
	
	/** The Constant GEOGRAPHYL2. */
	public static final String GEOGRAPHYL2 = "geographyL2";
	
	/** The Constant GEOGRAPHYL3. */
	public static final String GEOGRAPHYL3 = "geographyL3";
	
	/** The Constant GEOGRAPHYL4. */
	public static final String GEOGRAPHYL4 = "geographyL4";
	
	/** The Constant L1. */
	public static final String L1 = "L1";
	
	/** The Constant L2. */
	public static final String L2 = "L2";
	
	/** The Constant L3. */
	public static final String L3 = "L3";
	
	/** The Constant L4. */
	public static final String L4 = "L4";
	
	/** The Constant FILTER_NAME. */
	public static final String FILTER_NAME = "filterName";
	
	/** The Constant FILTER_PARAM. */
	public static final String FILTER_PARAM = "filterParam";
	
	/** The Constant FILTER_VALUE. */
	public static final String FILTER_VALUE = "filterValue";
	
	/** The Constant PROCESS_TYPE_DAILY. */
	public static final String PROCESS_TYPE_DAILY = "D";
	
	/** The Constant PROCESS_TYPE_WEEKLY. */
	public static final String PROCESS_TYPE_WEEKLY = "W";
	
	/** The Constant PROCESS_TYPE_MONTHLY. */
	public static final String PROCESS_TYPE_MONTHLY = "M";
	
	/** The Constant ALL_GEOGRAPHY_ID. */
	public static final Integer ALL_GEOGRAPHY_ID = 0;
	
	/** The Constant GEOGRAPHY_ONLY. */
	public static final String GEOGRAPHY_ONLY = "geographyOnly";
	
	/** The Constant WPT_USER_FAVOURITE_FILTERS. */
	public static final String WPT_USER_FAVOURITE_FILTERS = "WPT_USER_FAVOURITE_FILTERS";
	
	/** The Constant GEOGRAPHYL1_FILTER. */
	//Query Filters
	public static final String GEOGRAPHYL1_FILTER = "wptGeographyL1Filter";
	
	/** The Constant GEOGRAPHYL2_FILTER. */
	public static final String GEOGRAPHYL2_FILTER = "wptGeographyL2Filter";
	
	/** The Constant GEOGRAPHYL3_FILTER. */
	public static final String GEOGRAPHYL3_FILTER = "wptGeographyL3Filter";
	
	/** The Constant GEOGRAPHYL4_FILTER. */
	public static final String GEOGRAPHYL4_FILTER = "wptGeographyL4Filter";	
	
	/** The Constant GEOGRAPHYL1_TECHNOLOGY_FILTER. */
	public static final String GEOGRAPHYL1_TECHNOLOGY_FILTER = "wptGeographyL1TechnologyFilter";
	
	/** The Constant GEOGRAPHYL2_TECHNOLOGY_FILTER. */
	public static final String GEOGRAPHYL2_TECHNOLOGY_FILTER = "wptGeographyL2TechnologyFilter";
	
	/** The Constant GEOGRAPHYL3_TECHNOLOGY_FILTER. */
	public static final String GEOGRAPHYL3_TECHNOLOGY_FILTER = "wptGeographyL3TechnologyFilter";
	
	/** The Constant GEOGRAPHYL4_TECHNOLOGY_FILTER. */
	public static final String GEOGRAPHYL4_TECHNOLOGY_FILTER = "wptGeographyL4TechnologyFilter";	
	
	/** The Constant OPERATOR_FILTER. */
	public static final String OPERATOR_FILTER = "wptOperatorFilter";
	
	/** The Constant OPERATOR_TECHNOLOGY_FILTER. */
	public static final String OPERATOR_TECHNOLOGY_FILTER = "wptOperatorTechnologyFilter";
	
	/** The Constant ALL_GEOGRAPHY_FILTER. */
	public static final String ALL_GEOGRAPHY_FILTER = "wptAllGeographyFilter";
	
	/** The Constant GEOGRAPHY_LEVEL_FILTER. */
	public static final String GEOGRAPHY_LEVEL_FILTER = "wptGeographyLevelFilter";


	/** The Constant WPT_TOPIC_NAME. */
	//Kafka Topic Constants
	public static final String WPT_TOPIC_NAME = "TOPIC_WPT";
	
	/** The Constant WPT_TOPIC_PARTITION_COUNT_DRIVE. */
	public static final String WPT_TOPIC_PARTITION_COUNT_DRIVE = "PARTITION_TOPIC_WPT";
	
	/** The Constant NETVELOCITY_TOPIC_ACTIVE. */
	public static final String NETVELOCITY_TOPIC_ACTIVE = "netVelocityTopicActive";

}

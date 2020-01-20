package com.inn.foresight.module.nv.layer3.constants;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.inn.commons.Symbol;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;

/**
 * The Class NVLayer3Constants.
 *
 * @author innoeye
 * date - 29-Dec-2017 1:52:43 PM
 */
public interface NVLayer3Constants extends NVWorkorderConstant{

	/** Instantiates a new NV layer 3 constants. */


	public static final Gson gson=new Gson();

	static Type typeTokenForStealthCSVParsing = new TypeToken<Map<Integer, String>>(){}.getType();

	/** The Constant WO_RECIPE_MAPPING. */
	public static final String  WO_RECIPE_MAPPING = "woRecipeMapping";

	/** The Constant WO_RECIPE_MAPPING. */
	public static final String  WO_TASK_RESULT_ID = "woTaskResultId";

	/** The Constant IS_RETRIED. */
	public static final String  IS_RETRIED = "isRetried";

	/** The Constant QMDL_FILE_EXTENTION. */
	public static final String  QMDL_FILE_EXTENTION = ".qmdl";

	/** The Constant FILE_IS_NOT_OF_QMDL_TYPE. */
	public static final String  FILE_IS_NOT_OF_REQUIRED_TYPE = "{\"message\":\"File is not of QMDL or CSV Type or inputstream is null\"}";

	/** The Constant WO_RECIPE_MAPPING_ID_IS_NULL. */
	public static final String  WO_RECIPE_MAPPING_ID_IS_NULL = "{\"message\":\"WO Recipe Mapping id or file or fileName or fileType is null\"}";

	/** The Constant INVALID_WORECIPEMAPPING_ID. */
	public static final String  INVALID_WORECIPEMAPPING_ID = "{\"message\":\"Invalid WORecipeMapping id : ID\"}";

	/** The Constant COMPLETED_WORECIPEMAPPING_ID. */
	public static final String  COMPLETED_WORECIPEMAPPING_ID = "{\"message\":\"Already Completed Recipe, WORecipeMapping Id : ID\"}";

	/** The Constant ID. */
	public static final String  ID = "ID";

	/** The Constant FILE_DETAIL. */
	public static final String  FILE_DETAIL = "fileDetail";

	/** The Constant HBASE_DATA_SERVICE_DROPWIZARD_URL. */
	public static final String  HBASE_DATA_SERVICE_DROPWIZARD_URL = "";

	/** The Constant CSV_FILE_EXTENTION. */
	public static final String  CSV_FILE_EXTENTION = ".csv";

	/** The Constant ZIP_FILE_EXTENTION. */
	public static final String  ZIP_FILE_EXTENTION = ".zip";

	/** The Constant QMDL_FILE_EXTENTION. */
	public static final String  QMDL_FILE_TYPE = "QMDL";

	/** The Constant ZIP_FILE_EXTENTION. */
	public static final String  ZIP_FILE_TYPE = "ZIP";

	/** The Constant CSV_FILE_TYPE. */
	public static final String  CSV_FILE_TYPE = "CSV";

	/** The Constant IS_RETRIED. */
	public static final String  FILE_TYPE = "fileType";


	/** The Constant QMDL_KPI_STATS_TABLE. */
	public static final String QMDL_KPI_STATS_TABLE="QMDL_KPI_STATS_TABLE";

	/** The Constant FILE_NAME. */
	public static final String FILE_NAME="fileName";

	/** The Constant GET_ALL_LAYER3PRESET_QUERY. */
	public static final String GET_ALL_LAYER3PRESET_QUERY = "getAllLayer3PresetData";

	/** The Constant GET_LAYER3PRESET_BY_USERID. */
	public static final String GET_LAYER3PRESET_BY_USERID = "getlayer3presetdatabyuserid";

	/** The Constant GET_LAYER3VISUALIZATION_BY_ID. */
	public static final String GET_LAYER3VISUALIZATION_BY_ID = "getlayer3visualizationbyid";

	public static final String INVALID_USER = "\"result\":\"Error in getting user from Context\"";

	/** Signal Messages For Bin. */
	public static final String KEY_START_TIME = "startTime";
	public static final String KEY_END_TIME = "endTime";
	public static final String KEY_ROW_PREFIX = "rowPrefix";
	public static final String KEY_IMEI = "imei";



	public static final String EVENT = "event";
	public static final String KPI = "KPI";
	public static final String CALL = "Call";
	public static final String VOLTE_CALL = "VoLTE_Call";

	public static final String MOBILITY = "Mobility";
	public static final String TYPE_PREFIX ="NVLayer3%";
	public static final String IMEI="imei";
	public static final String END_TIME = "eTime";
	public static final String START_DATE = "startDate";
	public static final String KPI_LTE = "LTE";
	public static final String KPI_3G = "3G";
	public static final String KPI_2G = "2G";
	public static final String RRC = "RRC";
	public static final String CSFB = "CSFB";
	public static final String LINK_ADAPTATION = "LINK_ADAPTATION";
	public static final String DOMINANT = "DOMINANT";
	public static final String 	MCS_INFO = "MCS_INFO";
	public static final String 	DATA_THROUGHPUT = "DATA_THROUGHPUT";
	public static final String 	STATE_INFO = "STATE_INFO";

	public static final String 	VOLTE = "VOLTE";

	public static final String END_DATE = "endDate";

	public static final String PRESET_ID = "presetId";

	public static final String COMMON = "common";

	public static final String SMS = "SMS";

	public static final String DOWNLOAD = "Download";



	public static final String SUMMARY_COLUMN_NAME = "summary";

	public static final String SUMMARY_COLUMN_NAME_2G = "summary_2g";

	public static final String SUMMARY_COLUMN_NAME_3G = "summary_3g";

	public static final String TAU = "TAU";

	public static final String KPI_INDEXES = "kpi";

	public static final String KPI_NEIGHBOUR_INDEXES="kpiNeighbour";

	public static final String  INVALID_FILE_TYPE = "{\"message\":\"Invalid file type \"}";
	/** Utiltity Constants. */
	public static final String FORWARD = "forward";
	public static final String BACKWARD = "backward";

	/** Logger Messages. */
	public static final String EXCEPTION_SOMETHING_WENT_WRONG = "There is some problem in completing the request.";
	public static final String DATA_NOT_FOUND = "Data not found for the request.";
	public static final String INVALID_PARAMETER = "Parameters given are invalid.";
	public static final String INVALID_FILE_PATH = "FilePath is invalid.";

	
	public static final String FAILURE = "Failure";
	public static final String SUCCESS = "Success";
	

	/** Response Messages. */
	public static final String MESSAGE_START = "{\"message\":\"";
	public static final String MESSAGE_END = "\"}";
	public static final String RESULT_MESSAGE_START = "{\"result\":\"";	
	public static final String EXCEPTION_SOMETHING_WENT_WRONG_JSON = MESSAGE_START + EXCEPTION_SOMETHING_WENT_WRONG
			+ MESSAGE_END;
	public static final String DATA_NOT_FOUND_JSON = MESSAGE_START + DATA_NOT_FOUND + MESSAGE_END;
	public static final String INVALID_PARAMETER_JSON = MESSAGE_START + INVALID_PARAMETER + MESSAGE_END;
	
	public static final String SUCCESS_JSON = MESSAGE_START + SUCCESS + MESSAGE_END;
	public static final String FAILURE_JSON = MESSAGE_START +FAILURE + MESSAGE_END;
	public static final String INPROGRESS_FILE_JSON = MESSAGE_START + "Already inprogress" + MESSAGE_END;

	public static final String EXCEPTION_INVALLID_FILE_PATH_JSON = MESSAGE_START + INVALID_FILE_PATH
			+ MESSAGE_END;
	
	
	public static final String CONTAINS_ZIP_MESSAGE = "Invallid Zip File, File Contains Zip Inside It";
	public static final String CONTAINS_DIRECTORY_MESSAGE ="Invallid Zip File, File Contains Directory Inside Zip";
	public static final String CORRUPTED_ZIP_MESSAGE = "Zip File Is Corrupted,Please Upload Vallid Zip";
	

	/** Table Name Constants. */
	public static final String QMDL_SIGNALING_DATA_TABLE = "QMDL_SIGNALING_DATA_TABLE";

	public static final String LAYER3_MESSAGES_DATA_TABLE="LAYER3_MESSAGES_DATA_TABLE";
	public static final String QMDL_DATA_TABLE = "QMDL_DATA_TABLE";
	public static final String QMDL_KPI_TABLE = "QMDL_KPI_TABLE";
	public static final String QMDL_ZOOM_LEVEL18_TABLE = "QMDL_ZOOM_LEVEL18_TABLE";
	public static final String LAYER3_REPORT_TABLE="LAYER3_REPORT_TABLE";
	public static final String LAYER3_NEIGHBOUR_TABLE="LAYER3_NEIGHBOUR_TABLE";
	public static final String IS_LAYER3_FRAMEWORK_ENABLED="IS_LAYER3_FRAMEWORK_ENABLED";

	/** PHEONIX Query Constants. */
	public static final String WILDCARD_CHARACTER = "%";
	public static final String LIKE = " like ";
	public static final String GREATER_THAN = " > ";
	public static final String LESS_THAN = " < ";
	public static final String EQUAL_TO = Symbol.SPACE_STRING + Symbol.EQUAL_STRING + Symbol.SPACE_STRING;
	public static final String GREATER_THAN_OR_EQUAL_TO = Symbol.SPACE_STRING + GREATER_THAN + EQUAL_TO
			+ Symbol.SPACE_STRING;
	public static final String LESS_THAN_OR_EQUAL_TO = Symbol.SPACE_STRING + LESS_THAN + EQUAL_TO + Symbol.SPACE_STRING;
	public static final String TO_NUMBER = "TO_NUMBER";
	public static final String OPEN_BRACKET = "(";
	public static final String CLOSE_BRACKET = ")";

	/** REST Constants. */
	public static final String WORKORDER_ID = "workorderId";
	public static final String TASK_ID = "taskId";
	public static final String STARTTIME = "startTime";
	public static final String ENDTIME = "endTime";
	public static final String LASTROWKEY = "lastRowKey";
	public static final String DIRECTION = "direction";
	public static final String LIMIT = "limit";
	public static final String ISTIMEWISE = "isTimeWise";

	public static final String MSG_TYPE = "msgType";
	public static final String RECEIPE_ID = "receipeId";
	public static final String OPERATOR = "operator";

	public static final String MESSAGE = "message";


	/** Report Dump. */
	public static final String REPORT_JSON_START_STRING = "{\"result\":\"[[";
	public static final String REPORT_JSON_ARRAY_START_STRING="[[";
	public static final String REPORT_JSON_ARRAY_END_STRING="]]";
	public static final String REPORT_JSON_ARRAY_LAST_BRACES="],]";
	public static final String REPORT_JSON_ARRAY_LAST_BRACES_STEALTH=",],]";
	public static final String REPORT_JSON_ARRAY_LAST_BRACES_STEALTH_2="],";
	public static final String REPORT_JSON_END_STRING = "]]\"}";
	public static final String REPORT_JSON_LINE_SEPERATOR = "\\],\\[";
	public static final String REPORT_JSON_LINE_SEPERATOR_STEALTH = ",\\],\\[";
	public static final String NEW_LINE_SEPERATOR = "\n";
	public static final String REPORT_NAME_PREFIX = "WO_REPORT_";	
	public static final String PATH="Path";
	public static final String REPORT_FILE_EXTENSION = ".csv";
	public static final Integer FILE_BUFFER_SIZE = 1024;
	public static final Integer OUTPUT_STREAM_START_OFFSET = 0;
	public static final Integer DATA_STREAM_END_VALUE = 0;
	public static final String ENCODING_UTF_8 = "UTF-8";
	public static final String CONFIG_HEADER_SEPERATOR = "_";
	public static final String CSV_HEADER_SEPERATOR = ",";
	public static final String CSV_NO_DATA_IDENTIFIER = "{\"result\":]\"}";
	public static final String WO_REPORT_NO_DATA_AVAILABLE_JSON = "";
	public static final String REPORTINSTANCEID_PK = "analyticsrepositoryId";
	public static final String RECEIPE_MAPPING_ID = "recipeId";
	public static final String HBASE_RECORD_CONSTANT = "HBASE";
	public static final String FILE_NOT_FOUND = "\"result\":\"File Not Found \"";
	public static final String HDFS_RECORD_CONSTANT = "HDFS";
	public static final String ATTACHMENT_FILE = "attachment; filename=\"";
	public static final String INVALID_STORAGE_TYPE = "result\":\"Invalid Storage Type \"";
	public static final String OPERATOR_LIST_SEPERATOR = "_";
	public static final String ASSIGN_TO_JSON_KEY = "assignedTo";
	public static final String WORKORDER_NAME = "workorderName";
	public static final int DUMP_INDEX_TIMESTAMP = 2;
	public static final int DUMP_INDEX_ROWKEY = 64;
	public static final int LIST_INDEX_AND_LENGTH_DIFFERENCE = 1;
	public static final String CSV_COLUMN_SEPERATOR = ",";
	public static final String DATE_FORMAT_FOR_CSV_DUMP = "dd/MM/yyyy HH:mm:ss.SSS z";
	public static final String DATE_FORMAT_FOR_EXCEL_DUMP = "MMM/dd/yyyy HH:mm:ss";
	public static final int GEOHASH_INDEX = 172;

	/** LatLng Aggregation Constant. */
	public static final String DRIVE_START_STRING = "{\"result\":\"[";
	public static final String DRIVE_END_STRING = "]]\"}";
	public static final String DRIVE_FIRST_STRING="\\[";
	public static final String DRIVE_RECORD_SEPERATOR = "\\],\\[";
	public static final String DRIVE_NULL_STRING = "";
	public static final String DRIVE_COMMA_SEPRATOR = ",";
	public static final String DRIVE_BRACKET_SEPRATOR = "\\],";
	public static final String DRIVE_OPENING_BRACKET_SEPRATOR ="[";
	public static final String DRIVE_CLOSING_BRACKET_COMMA_SEPRATOR ="],";
	public static final String DRIVE_CLOSING_BRACKET_PARENTHESI_SEPRATOR ="]\"}";


	public static final String NEIGHBOUR_FIRST_STRING="[[";
	public static final String NEIGHBOUR_LAST_STRING="]]";
	public static final String NEIGHBOUR_RECORD_SEPERATOR = "\\], \\[";



	public static final Integer GRIDLATINDEX = 0;
	public static final Integer GRIDLNGINDEX = 1;

	public static final String LAYER3_GRID_SIZE = "LAYER3_GRID_SIZE";

	public static final String OPERATOR_NAME_BENCHMARK = "BN";


	public static final String SCAN_ROW_SEPERATOR = "_";

	public static final String KEY_SEARCH = "search";

	/**
	 * REGEX to remove single entry from csv
	 * PART_1 + {String to remove} + PART_2 + {String to remove} + PART_3.
	 */

	public static final String REGEX_CSV_ITEM_REMOVE_PART_1 = "\\s*\\b";

	public static final String REGEX_CSV_ITEM_REMOVE_PART_2 = "\\s*\\b,|,\\s*\\b";

	public static final String REGEX_CSV_ITEM_REMOVE_PART_3 = "\\s*\\b$";

	/** For Live Drive Csv Report. */

	public static final String LD_REPORT_NAME_PREFIX = "Live_Drive_Report_";

	/** The name. */
	public static final String NAME = "name";

	/** The name. */
	public static final String VALUE = "value";

	public static final String START_TIMESTAMP = "startTimestamp";
	public static final String END_TIMESTAMP = "endTimestamp";
	public static final String GEOGRAPHY_L3_LIST = "GeographyL3List";

	public static final String DATE = "date";

	public static final String WO_FILE_NAME_PREFIX_FLOOR_PLAN = "floor_plan";

	public static final String STATISTIC = "Statistic";
	public static final String STATISTIC_FORMATTED_DATA = "Statistic Formatted Data";
	public static final String SERIES_FORMATTED_DATA = "Series Formatted Data";
	public static final String HISTOGRAM_FORMATTED_DATA = "Histogram Formatted Data";
	public static final String  EXCELDATA  ="ExcelData";
	public static final String	XLS_EXTENSION  ="xls";
	public static final String COUNT="Count";
	
	
	
	public static final String LAT_LNG_TIMESTAMP ="latlngTimestamp";
	public static final String LAT_LNG_TIMESTAMP_COL ="latlngTimestamp";


	

	
	
	public static final String RANGE ="Range";
	public static final String HEADER_NEIGHBOUR_DETAIL="Neighbour No_RSRP(dBm)_RSRQ(dB)_RSSI(dBm)_PCI_latitude_longitude_RSRPRX0(dBm)_RSRPRX1(dBm)_RSRQRX0(dB)_RSRQRX1(dB)_RSSIRX0(dBm)_RSSIRX1(dBm)";
	public static final String HEADER_NEIGHBOUR_DETAIL_NEW_FRAMEWORK="FRAMEBDRYREFTIME0_FRAMEBDRYREFTIME1_FTLCUMUFREQOFFSET_RSRP(dBm)_RSRPRX0(dBm)_RSRPRX1(dBm)_RSRQ(dB)_RSRQRX0(dB)_RSRQRX1(dB)_RSSI(dBm)_RSSIRX0(dBm)_RSSIRX1(dBm)_PCI_TOTALTIMEADJCIR0_TOTALTIMEADJCIR1_LATITUDE_LONGITUDE";


	public static final String DROPWIZARD_STEALTH_WO_TASK_ID_PDF_PROCESS_URL = "DROPWIZARD_STEALTH_WO_TASK_ID_PDF_PROCESS_URL";
	public static final String DROPWIZARD_STEALTH_WO_TASK_ID_PDF_DOWNLOAD_URL = "DROPWIZARD_STEALTH_WO_TASK_ID_PDF_DOWNLOAD_URL";
	public static final String DROPWIZARD_RECIPE_LOG_FILE_DOWNLOAD_URL = "DROPWIZARD_RECIPE_LOG_FILE_DOWNLOAD_URL";

	public static final int LIST_SIZE_ONE = 1;

	public static final String LOG_FILE_MAIN_ZIP_NAME = "Processed_recipe_log_files.zip";
	
	public static final String LAYER3_PROCESSED_LOG_FILE_DIRECTORY_NAME = "PROCESSED_LOG_FILES";

	public static final int LOG_FILE_ARRAY_PROCESSED_PATH_INDEX = 0;
	public static final int LOG_FILE_ARRAY_FILE_NAME_INDEX = 1;

	public static final String  NO_DATA_FOUND ="NO DATA FOUND";
	public static final int NEIGHBOUR_DATA_PCI_INDEX = 4;
	public static final int NEIGHBOUR_NO_INDEX = 0;
	public static final int NEIGHBOUR_KPI_INDEX = 1;
	public static final int MAX_KPI_OFFSET = 300;
	public static final int HUNDRED = 100;
	public static final int TWO = 2;

	public static final String COLUMNS = "columns";
	public static final String NONE_STRING = "NONE";
	public static final String NULL_STRING = "null";
	public static final Long TIMESTAMP_1JAN_2016=	1451606400000L;
	public static final String NEIGHBOUR_COLUMN = "NeighbourData";
	public static final String NEIGHBOUR_ARRAY_OPENING = "\\[\\[";
	public static final String NEIGHBOUR_ARRAY_CLOSING = "\\]\\]";
	public static final String NEIGHBOUR_ARRAY_OPENING_CLOSING_REPLACEMENT = "\\$\\$";
	public static final String NEIGHBOUR_ARRAY_SEPERATOR = "\\], \\[";
	public static final String NEIGHBOUR_ARRAY_SEPERATOR_REPLACEMENT = "\\$,\\$";

	public static final String LAYER3_DRIVE_DETAIL_TABLE = "LAYER3_DRIVE_DETAIL_TABLE";

	public static final String NEIGHBOUR_DATA_COL="NeighbourData";
	public static final String EMPTY_ARRAY = "[]";


	public static final String LAYER3_NOT_REQURED_HBASE_COLUMNS = "LAYER3_NOT_REQURED_HBASE_COLUMNS";

	public static final String DOWNLOAD_TEST = "DOWNLOAD";

	public static final String UPLOAD_TEST = "UPLOAD";

	public static final String FULL_TEST = "Full Test";

	public static final String QUICK_TEST = "Quick Test";

	public static final String YOUTUBE_TEST = "YOUTUBE_TEST";

	public static final String WPT_TEST = "Web Test";

	public static final String PING_TEST = "PING";

	public static final String IDLE_TEST = "IDLE";
	
	

	public static final String LAYER3_DRIVE_SUMMAARY_TABLE = "LAYER3_DRIVE_SUMMAARY_TABLE";

	public static final String LAYER3_EVENT_TABLE = "LAYER3_EVENT_TABLE";

	public static final String DLF_FILE_TYPE = "dlf";


}
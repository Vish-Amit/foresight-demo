package com.inn.foresight.module.nv.report.utils;

/** The Class ReportConstants. */
public interface ReportConstants {

	Double EARTH_RADIUS = 6371000.0;
	Double ANGLE_180 = 180.0;

	/** The Constant RF_COVERAGE_REPORT_TILE_IMAGE. */
	String RF_COVERAGE_REPORT_TILE_IMAGE = "RF_COVERAGE_REPORT_TILE_IMAGE";

	/** The Constant FINAL_IMAGE_PATH. */
	String FINAL_IMAGE_PATH = "FINAL_IMAGE_PATH";

	/** The Constant MAX_LAT. */
	String MAX_LAT = "MAX_LAT";

	/** The Constant MIN_LAT. */
	String MIN_LAT = "MIN_LAT";

	/** The Constant MAX_LON. */
	String MAX_LON = "MAX_LON";

	/** The Constant MIN_LON. */
	String MIN_LON = "MIN_LON";

	/** The Constant BLANK_STRING. */
	String BLANK_STRING = "";

	/** The Constant KEY_LEGENDS. */
	String KEY_LEGENDS = "legends";

	/** The Constant COMMA. */
	String COMMA = ",";

	/** The Constant DATE_FORMAT_1. */
	String DATE_FORMAT_1 = "yyyy_MM_dd_HH_mm_ss_SSS";

	/** The Constant INDEX_ZER0. */
	// Numeric Constants
	int INDEX_ZER0 = 0;

	/** The Constant INDEX_ONE. */
	int INDEX_ONE = 1;

	/** The Constant INDEX_THREE. */

	/** The Constant PCI_PLOT. */
	int INDEX_THREE = 3;

	String PCI_PLOT = "PCI_PLOT";

	/** The Constant FORWARD_SLASH. */
	String FORWARD_SLASH = "/";

	/** The Constant DOT_JPG. */
	// Extensions
	String DOT_JPG = ".jpg";
	String DOT_PNG = ".png";

	/** The Constant MAP_SATELLITE. */
	// Map View Types
	String MAP_SATELLITE = "satellite";

	/** The Constant MAP_ROAD. */
	String MAP_ROAD = "roadmap";

	/** The Constant GOOGLE_MAP_API_URL. */
	String GOOGLE_MAP_API_URL = "https://maps.googleapis.com/maps/api/staticmap?center=";

	/** The Constant INDEX_THIRTY. */
	int INDEX_THIRTY = 30;

	/** The Constant INDEX_THIRTY. */
	int INDEX_THIRTY_FOUR = 34;

	/** The Constant INDEX_HUNDRED. */
	int INDEX_HUNDRED = 100;

	/** The Constant ZERO_DOT_ZEROZERO. */
	String ZERO_DOT_ZEROZERO = "0.00";

	/** The Constant FIFTEEN. */
	int FIFTEEN = 15;

	/** The Constant EIGHTY. */
	int EIGHTY = 80;

	/** The Constant FIVE_ONE_TWO. */
	int FIVE_ONE_TWO = 512;

	/** The Constant TEN. */
	int TEN = 10;

	/** The Constant TWENTY. */
	int INDEX_FOURTEEN = 14;

	int TWENTY = 20;

	int TWENTY_ONE = 21;

	/** The image rsrp. */
	// Constant For Jasper
	String IMAGE_RSRP = "rsrpimg";
	String IMAGE_RSRQ = "rsrqimg";

	/** The image sinr. */
	String IMAGE_SINR = "sinrimg";

	/** The image dl. */
	String IMAGE_DL = "dlimg";
	String IMAGE_PDSCH = "pdschimg";
	String HTTP_IMAGE_DL = "httpdlimg";
	String FTP_IMAGE_DL = "ftpdlimg";
	/** The image dl legend. */
	String IMAGE_DL_LEGEND = "dllegendimg";
	String IMAGE_PDSCH_LEGEND = "pdschlegendimg";
	String IMAGE_HTTP_DL_LEGEND = "httpdllegend";
	String IMAGE_FTP_DL_LEGEND = "ftpdllegend";
	/** The image ul. */
	String HTTP_IMAGE_UL = "httpulimg";
	String FTP_IMAGE_UL = "ftpulimg";
	String IMAGE_UL = "ulimg";
	String IMAGE_PUSCH = "puschimg";
	String IMAGE_HTTP_UL_LEGEND = "httpUllegend";
	String IMAGE_FTP_UL_LEGEND = "ftpullegend";
	String NHI = "nhiimg";
	/** The image pci. */
	String IMAGE_PCI = "pciimg";
	String CONTENT_TYPE_CSV = "text/csv";
	/** The image site. */
	String IMAGE_SITE = "siteimg";

	/** The image rsrp legend. */
	String IMAGE_RSRP_LEGEND = "rsrplegendimg";
	String IMAGE_RSRQ_LEGEND = "rsrqlegendimg";

	/** The image sinr legend. */
	String IMAGE_SINR_LEGEND = "sinrlegendimg";
	String IMAGE_SINR_HTTP_DL = "httpsinrimgdl";
	String IMAGE_SINR_FTP_DL = "ftpsinrimgdl";
	String IMAGE_RSRP_HTTP_DL = "httprsrpimgdl";
	String IMAGE_RSRP_FTP_DL = "ftprsrprimgdl";
	String IMAGE_SINR_HTTP_UL = "httpsinrimgul";
	String IMAGE_SINR_FTP_UL = "ftpsinrimgul";
	String IMAGE_RSRP_HTTP_UL = "httprsrpimgul";
	String IMAGE_RSRP_FTP_UL = "ftprsrprimgul";
	String IMAGE_MOS = "mosimg";
	String IMAGE_SINR_HTTP_DL_LEGEND = "httpsinrimgdllegend";
	String IMAGE_SINR_FTP_DL_LEGEND = "ftpsinrimgdllegend";
	String IMAGE_RSRP_HTTP_DL_LEGEND = "httprsrpimgdllegend";
	String IMAGE_RSRP_FTP_DL_LEGEND = "ftprsrprimgdllegend";
	String IMAGE_SINR_HTTP_UL_LEGEND = "httpsinrimgullegend";
	String IMAGE_SINR_FTP_UL_LEGEND = "ftpsinrimgullegend";
	String IMAGE_RSRP_HTTP_UL_LEGEND = "httprsrpimgullegend";
	String IMAGE_RSRP_FTP_UL_LEGEND = "ftprsrprimgullegend";
	String IMAGE_MOS_LEGEND = "moslegendimg";

	/** The image pci legend. */
	String IMAGE_PCI_LEGEND = "pcilegendimg";

	/** The image ul legend. */
	String IMAGE_UL_LEGEND = "ullegendimg";
	String IMAGE_PUSCH_LEGEND = "puschlegendimg";
	String IMAGE_CQI_LEGEND = "cqilegendimg";
	String IMAGE_CQI = "cqiimg";
	String IMAGE_RI_LEGEND = "rilegendimg";
	String IMAGE_RI = "riimg";
	String IMAGE_MCS_LEGEND = "mcslegendimg";
	String IMAGE_MCS = "mcsimg";

	String IMAGE_FC_LEGEND = "fclegendimg";
	String IMAGE_FC = "fcimg";
	String IMAGE_IDEL = "idelimg";
	String IMAGE_CGI = "cgiimg";
	String IMAGE_CGI_LEGEND = "cgilegendimg";

	String IMAGE_SERVING_CELL = "servingcellimg";
	String IMAGE_SERVING_CELL_LEGEND = "servingcellegendimg";

	/** The Constant SSVT_REPORT. */
	String SSVT_REPORT = "SSVT REPORT";
	String NV_DASHBOARD_REPORT = "NV_Dashboard";

	/** The Constant SUBREPORT_DIR. */
	String SUBREPORT_DIR = "SUBREPORT_DIR";

	/** The Constant SSVT_REPORT_MAIN_FILENAME. */

	String SSVT_THANK_YOU_PAGE = "main_ThankYou.jasper";

	String MAIN_JASPER = "main_report.jasper";

	String BENCHMARK_JASPER_NAME = "Main_report.jasper";

	/** The Constant LEGEND. */
	String LEGEND = "LEGEND";

	/** The Constant PCI_LEGEND. */
	String PCI_LEGEND = "legends";

	/** The Constant SITE_IMAGE. */
	String SITE_IMAGE = "0";

	/** The Constant UNDERSCORE. */
	String UNDERSCORE = "_";

	/** The Constant HYPHEN. */
	String HYPHEN = "-";

	/** The Constant JPG. */
	String JPG = "jpg";

	/** The Constant THOUSNAND. */
	int THOUSNAND = 1000;

	/** The Constant TWELVE. */
	int TWELVE = 12;

	/** The Constant ONE_HUNDRED_SEVENTY. */
	int ONE_HUNDRED_SEVENTY = 170;

	/** The Constant ONE_HUNDRED_SEVENTY. */
	int ONE_HUNDRED_FOURTY = 140;

	/** The Constant HASH_DOT_HASHHASH. */
	String HASH_DOT_HASHHASH = "#.##";

	/** The Constant TWO_FIVE_SIX. */
	int TWO_FIVE_SIX = 256;
	int TWO_ZERO_FOUR_EIGHT = 2048;
	int THREE_ZERO_FOUR = 304;

	String NV_REPORT_LOGO_CLIENT_KEY = "NV_REPORT_LOGO_CLIENT_KEY";

	String NV_REPORT_LOGO_CLIENT_IMG = "NV_REPORT_LOGO_CLIENT_IMG";

	/** The Constant LOGO_CLIENT_KEY. */
	String LOGO_CLIENT_KEY = "clientLogo";

	/** The Constant LOGO_CLIENT_IMG. */
	String LOGO_CLIENT_IMG = "ClientLogo.png";

	/** The Constant LOGO_NV_KEY. */
	String LOGO_NV_KEY = "netvelocity";

	/** The Constant LOGO_NV_IMG. */
	String LOGO_NV_IMG = "netvelocity.png";

	/** The Constant DATE_FORMAT_YYYYMMDD. */
	String DATE_FORMAT_YYYYMMDD = "yyyy-MM-dd";

	/** The Constant FONT_TIMES_NEW_ROMAN. */
	String FONT_TIMES_NEW_ROMAN = "Times New Roman";

	String CALIBRI = "Calibri";
	/** The Constant TO. */
	String TO = "to";

	/** The Constant GREATER_THAN. */
	String GREATER_THAN = ">";

	/** The Constant N_SLASH_A. */
	String N_SLASH_A = "N/A";

	/** The Constant DT_MOBILE. */
	String DT_MOBILE = "DT Mobile";

	// Summary Page fields Required

	/** The Constant SSVT_SUMMARY_FIELDS. */
	String SSVT_SUMMARY_FIELDS = "SSVT_SUMMARY_FIELDS";

	/** The Constant S_START_TIMESTAMP_INDEX. */
	// Summary Hbase field constants
	String S_START_TIMESTAMP_INDEX = "S_START_TIMESTAMP_INDEX";

	/** The Constant S_END_TIMESTAMP_INDEX. */
	String S_END_TIMESTAMP_INDEX = "S_END_TIMESTAMP_INDEX";

	/** The Constant S_RSSI_INDEX. */
	String S_RSSI_INDEX = "S_RSSI_INDEX";

	/** The Constant S_RSRP_INDEX. */
	String S_RSRP_INDEX = "S_RSRP_INDEX";

	/** The Constant S_RSRQ_INDEX. */
	String S_RSRQ_INDEX = "S_RSRQ_INDEX";

	/** The Constant S_SINR_INDEX. */
	String S_SINR_INDEX = "S_SINR_INDEX";

	/** The Constant S_DL_INDEX. */
	String S_DL_INDEX = "S_DL_INDEX";

	/** The Constant S_UL_INDEX. */
	String S_UL_INDEX = "S_UL_INDEX";

	/** The Constant S_LATENCY_INDEX. */
	String S_LATENCY_INDEX = "S_LATENCY_INDEX";

	/** The Constant S_HANDOVER_INITIATE_INDEX. */
	String S_HANDOVER_INITIATE_INDEX = "S_HANDOVER_INITIATE_INDEX";

	/** The Constant S_HANDOVER_FAILURE_INDEX. */
	String S_HANDOVER_FAILURE_INDEX = "S_HANDOVER_FAILURE_INDEX";

	/** The Constant S_HANDOVER_SUCCESS_INDEX. */
	String S_HANDOVER_SUCCESS_INDEX = "S_HANDOVER_SUCCESS_INDEX";

	/** The Constant S_CALL_INITIATE_INDEX. */
	String S_CALL_INITIATE_INDEX = "S_CALL_INITIATE_INDEX";

	/** The Constant S_CALL_DROP_INDEX. */
	String S_CALL_DROP_INDEX = "S_CALL_DROP_INDEX";

	/** The Constant S_CALL_FAILURE_INDEX. */
	String S_CALL_FAILURE_INDEX = "S_CALL_FAILURE_INDEX";

	/** The Constant S_CALL_SUCCESS_INDEX. */
	String S_CALL_SUCCESS_INDEX = "S_CALL_SUCCESS_INDEX";

	/** The Constant S_CELL_CHANGED_INDEX. */
	String S_CELL_CHANGED_INDEX = "S_CELL_CHANGED_INDEX";

	/** The Constant S_TOTAL_HANDOVER_INDEX. */
	String S_TOTAL_HANDOVER_INDEX = "S_TOTAL_HANDOVER_INDEX";

	/** The Constant S_TOTAL_CALLS_INDEX. */
	String S_TOTAL_CALLS_INDEX = "S_TOTAL_CALLS_INDEX";

	/** The Constant S_ADDRESS_INDEX. */
	String S_ADDRESS_INDEX = "S_ADDRESS_INDEX";

	/** The Constant S_RRC_INITIATE_INDEX. */
	String S_RRC_INITIATE_INDEX = "S_RRC_INITIATE_INDEX";

	/** The Constant S_RRC_SUCCESS_INDEX. */
	String S_RRC_SUCCESS_INDEX = "S_RRC_SUCCESS_INDEX";

	/** The Constant RRC_CONNECTION_SUCCESS_RATE. */
	String RRC_CONNECTION_SUCCESS_RATE = "RRC_CONNECTION_SUCCESS_RATE";

	/** The Constant VOLTE_SETUP_SUCCESS_RATE. */
	String VOLTE_SETUP_SUCCESS_RATE = "VOLTE_SETUP_SUCCESS_RATE";

	/** The Constant VOLTE_ERAB_DROP_RATE. */
	String VOLTE_ERAB_DROP_RATE = "VOLTE_ERAB_DROP_RATE";

	/** The Constant HANDOVER_SUCCESS_RATE. */
	String HANDOVER_SUCCESS_RATE = "HANDOVER_SUCCESS_RATE";

	/** The Constant ERAB_DROP_RATE. */
	String ERAB_DROP_RATE = "ERAB_DROP_RATE";

	String ERAB_DROP = "ERAB_DROP";

	/** The Constant CROSS_FEEDER. */
	String CROSS_FEEDER = "CROSS_FEEDER";

	/** The Constant OVER_SHOOTING. */
	String OVER_SHOOTING = "OVER_SHOOTING";

	/** The Constant PASS. */
	String PASS = "PASS";

	/** The Constant FAIL. */
	String FAIL = "FAIL";

	/** The Constant TWO_HUNDRED_TWENTY. */
	int TWO_HUNDRED_TWENTY = 220;

	/** The Constant PDF. */
	String PDF_EXTENSION = ".pdf";
	String DOCX_EXTENSION = ".docx";

	/** The Constant FOUR. */
	Integer FOUR = 4;

	/** The image param header bg. */
	String IMAGE_PARAM_HEADER_BG = "header-bg";
	String IMAGE_PARAM_HEADER_LOGO = "header-logo";

	/** The image param header log. */
	String IMAGE_PARAM_HEADER_LOG = "header-log";

	/** The image param screen bg. */
	String IMAGE_PARAM_SCREEN_BG = "landing-screen-bg";
	String IMAGE_PARAM_SUB_REPORT_SCREEN_BG = "sub-report-screen-bg";
	String IMAGE_PARAM_THANK_YOU = "thankyou";

	String IMAGE_PARAM_THANK_YOU_BENCHMARK = "thank-you";
	String IMAGE_PARAM_FOOTER_LOGO = "footer-logo";
	String IMAGE_PARAM_THANK_YOU_FOOTER = "thank-you-footer";
	String IMAGE_PARAM_THANK_YOU_FG = "thank-you-fg";

	/** The image param screen log. */
	String IMAGE_PARAM_SCREEN_LOG = "landing-screen-logo";

	/** The image param screen log. */

	/** The image param screen log. */
	String IMAGE_PARAM_SCREEN_LOGO = "landing-logo";
	/** The image param screen log. */

	/** The date. */
	String DATE = "date";

	String HOUR = "hour";

	/** The present date. */
	String PRESENT_DATE = "presentDate";

	/** The previous date. */
	String PREVIOUS_DATE = "previousDate";

	/** The image header bg. */
	String IMAGE_HEADER_BG = "header-bg.png";
	String IMAGE_HEADER_LOGO = "header-logo.png";

	/** The image header log. */
	String IMAGE_HEADER_LOG = "header-log.png";

	String IMAGE_HEADER_LOG_NV = "header-log123.png";

	/** The image screen logo. */
	String IMAGE_SCREEN_LOGO = "landing-screen-logo.jpg";

	String IMAGE_SCREEN_LOGO_STATIONARY = "landing-screen-logo123.png";

	String IMAGE_SCREEN_LOGO_MASTER = "landing-logo.png";

	/** The image screen bg. */
	String IMAGE_SCREEN_BG = "landing-screen-bg.png";
	String IMAGE_SCREEN_BG_JPEG = "landing-screen-bg.jpeg";
	String THANK_YOU_IMG = "Thankyou.png";
	String THANK_YOU_JPEG = "thank-you.jpeg";
	String FOOTER_LOGO_IMG = "footer-logo.png";
	String THANK_YOU_FOOTER_LOGO_IMG = "thank-you-footer.png";
	String THANK_YOU_FG_IMG = "thank-you-fg.png";
	/** The Constant IS_GRIDLINES_VISIBLE_FOR_CHART_DOMAIN. */
	boolean IS_GRIDLINES_VISIBLE_FOR_CHART_DOMAIN = false;

	/** The Constant IS_GRIDLINES_VISIBLE_FOR_CHART_RANGES. */
	boolean IS_GRIDLINES_VISIBLE_FOR_CHART_RANGES = false;

	/** The Constant HASH. */
	String HASH = "#";


	int INDEX_TWO = 2;

	Double HUNDRED = 100.0;

	String CLOSED_BRACKET = ")";
	String AMPERSAND = "&";

	String SIZE = "size";

	String MAPTYPE = "maptype";

	String GEOGRAPHYL4 = "GeographyL4";
	String GEOGRAPHYL3 = "GeographyL3";
	String GEOGRAPHYL2 = "GeographyL2";
	String GEOGRAPHYL1 = "GeographyL1";
	String GEOGRAPHYL0 = "GeographyL0";

	String GEOGRAPHY_TYPE = "geographyType";
	String GEOGRAPHY = "GEOGRAPHY";
	String GEOGRAPHY_ID = "geographyId";
	String COORDINATES = "coordinates";
	String GEOGRAPHY_NAME = "geographyName";
	String GEOGRAPHY_ID_LIST = "geographyIdList";
	String COLON = ":";
	/**  String COORDINATESr:coordinates */

	String ZERO = "0";

	Integer SEVEN = 7;

	String ZOOM = "zoom";

	String KEY = "KEY";

	String API_KEY = "key";

	String DOUBLE_SPACE = "  ";

	String OPEN_BRACKET = "(";

	String SPACE = " ";

	String SSVT_REPORT_PATH = "SSVT_REPORT_PATH";

	String SSVTREPORT_JASPER_FOLDER_PATH = "SSVTREPORT_JASPER_FOLDER_PATH";
	String QUICK_SSVTREPORT_JASPER_FOLDER_PATH = "QUICK_SSVTREPORT_JASPER_FOLDER_PATH";

	String BRTI_QUARTERLY_REPORT_FOLDER_PATH = "BRTI_QUARTERLY_REPORT_FOLDER_PATH";
	String BRTI_QUARTERLY_COMPARISON_REPORT_FOLDER_PATH = "BRTI_QUARTERLY_COMPARISON_REPORT_FOLDER_PATH";

	String BRTI_SMART_TELECOM_REPORT_FOLDER_PATH = "BRTI_SMART_TELECOM_REPORT_FOLDER_PATH";

	String BRTI_CRB_REPORT_FOLDER_PATH = "BRTI_CRB_REPORT_FOLDER_PATH";

	String SSVT_RECIPE_REPORT_JASPER_FOLDER_PATH = "SSVT_RECIPE_REPORT_JASPER_FOLDER_PATH";

	String IMAGE_PATH_FOR_NV_REPORT = "IMAGE_PATH_FOR_NV_REPORT";

	String STATIONARY_SUMMARY_LIST = "STATIONARY_SUMMARY_LIST";

	String NV_DASHBOARD_REPORT_PATH = "NV_DASHBOARD_REPORT_PATH";

	String SSVT_SITE_UPDATE_JASPER_FOLDER_PATH = "SSVT_SITE_UPDATE_JASPER_FOLDER_PATH";

	String SSVT_UPDATED_REPORT_PATH = "SSVT_UPDATED_REPORT_PATH";

	String SSVT = "SSVT";

	String KPICOMPARISON = "KPICOMPARISON";

	String COMPLAINT_TEST_MAIN_JASPER = "COMPLAINT_TEST_MAIN_JASPER";

	String COMPLAINT_TEST_REPORT = "COMPLAINT_TEST_REPORT";

	String STATIONARY = "STATIONARY";

	String NETVELOCITY_SPEED_TEST = "NETVELOCITY_SPEED_TEST";

	String NETVELOCITY = "Netvelocity";

	String PEAK_VALUE = "PEAK_VALUE";

	String HASH_DOT = "#.";

	/** CLOT REPORT SUMMARY CONSTANTS. */
	String RRC_CONNECTION_REQUEST_SUCCESS = "RRC_CONNECTION_REQUEST_SUCCESS";

	String VOLTE_CALL_DROP_RATE = "VOLTE_CALL_DROP_RATE";

	String AVERAGE_HANDOVER_INTERRUPTION_TIME = "AVERAGE_HANDOVER_INTERRUPTION_TIME";

	String AVERAGE_SINR = "AVERAGE_SINR";

	String VOLTE_VOICE_MEAN_OPINION_SOURCE = "VOLTE_VOICE_MEAN_OPINION_SOURCE";

	String WORKORDER_ID = "workorderId";
	String ANALYTICS_REPOSITORY_ID = "analyticsrepositoryid_pk";

	String REPORT_NAME = "reportName";
	String CITY = "city";
	String TECHNOLOGY = "technology";
	String PERIOD_TYPE = "period";
	String QUARTER = "quarter";
	String YEAR = "year";

	String NVREPORT_COLUMN_FAMILY = "r";

	String NVREPORT_TABLE = "NVReport";

	String CONTENT_TYPE = "Content-Type";
	String CONTENT_DISPOSITION = "Content-Disposition";
	String INSTANCE_ID = "instanceId";
	String DROPWIZARD_URL = "DROPWIZARD_POST_NVACTIVEREPORTDATA_URL";
	String CONTENT_TYPE_EXCEL = "application/excel";
	String EXCEPTION_ON_CONNECTION = "Unable to connect to the server!";
	String LIVEDRIVEREPORT = "WorkOrderReport";

	/** The Constant PDF. */
	String PDF = "PDF";

	String CQI = "CQI";
	String MOS = "MOS";
	String RI = "RI";

	String ROUTE = "ROUTE";

	String IMAGE_ROUTE = "imgroute";

	String RSRP = "RSRP";

	String SINR = "SINR";

	String DL = "DL_THROUGHPUT";

	String UL = "UL_THROUGHPUT";

	String MIMO = "MIMO";

	String MCS = "MCS";

	String FREQUENCY = "FREQUENCY";

	String IMAGE_CELL = "cellpciimg";

	String IMAGE_CELL_LEGEND = "cellpcilegendsimg";

	String IMAGE_HANDOVER = "handoverimg";

	String RESULT = "result";

	String SECTOR = "sector";

	String ITERATION = "ITERATION";

	String SATELLITE_VIEW = "SATELLITE_VIEW";

	Object SATELLITE_VIEW3 = "SATELLITE_VIEW3";

	String JUSTFICATION_IMG = "spimg";

	String RACH_ATTEMPT = "RACH_ATTEMPT";
	String RACH_SUCCESS = "RACH_SUCCESS";
	String ATTACH_REQUEST = "ATTACH_REQUEST";
	String ATTACH_COMPLETE = "ATTACH_COMPLETE";
	String DETACH_REQUEST = "DETACH_REQUEST";
	String ATTACH_LATENCY = "ATTACH_LATENCY";
	String CALL_SETUP_SUCCESS = "CALL_SETUP_SUCCESS";
	String IMS_REGISTRATION_TIME = "IMS_REGISTRATION_TIME";
	String MT_CALL_ATTEMPT = "MT_CALL_ATTEMPT";
	String MT_CALL_SUCCESS = "MT_CALL_SUCCESS";
	String MO_CALL_ATTEMPT = "MO_CALL_ATTEMPT";
	String MO_CALL_SUCCESS = "MO_CALL_SUCCESS";
	String RTP_PACKET_LOSS = "RTP_PACKET_LOSS";

	String PIN_IMAGE = "PIN_IMAGE";
	String PIN_IMAGE_PATH = "PIN_IMAGE_PATH";

	String EARFCN = "EARFCN";
	String DL_EARFCN = "DL_EARFCN";
	String UL_EARFCN = "UL_EARFCN";
	String DLEARFCN = "dLEarfcn";

	String IBREPORT_FLOORPLAN_IMAGE_PATH = "IBREPORT_FLOORPLAN_IMAGE_PATH";

	String IB_REPORT_IMAGE_NAME_PREFIX = "FloorPlan_";

	String IMAGE_FILE_EXTENSION = ".jpeg";

	String INBUILDING_REPORT_PLOT = "INBUILDING_REPORT_PLOT";
	String INBUILDING_REPORT_SUMMARY_PATH = "INBUILDING_REPORT_SUMMARY_PATH";
	String NV_REPORTS_PATH = "NV_REPORTS_PATH";
	String NV_REPORTS_PATH_HDFS = "NV_REPORTS_PATH_HDFS";
	String BRTI_REPORTS_PATH = "NV_REPORTS_PATH";

	String CLOT = "CLOT";

	String HDFS = "HDFS";
	String FLOORPLANNAME = "floorplan";
	String NOACCESS = "noaccess";

	String INBUILDING_REPORT_PATH = "INBUILDING_REPORT_PATH";
	String IMAGE_PARAM_FOOTER = "footerImg";
	String IMAGE_FOOTER = "footer-img.png";

	String ACCESSIBILITY = "Accessibility";
	String RETAINABILITY = "Retainability";
	String MOBILITY = "Mobility";
	String INTEGRITY = "Integrity";

	String HIPHEN = "-";

	String TECHNOLOGY_FDD = "FDD";
	String TECHNOLOGY_TDD = "TDD";

	Integer INDEX_FOUR = 4;

	Integer INDEX_FIVE = 5;

	String MBPS = "Mbps";

	String RECIPE_LIST = "RECIPE_LIST";
	String OPERATOR_LIST = "OPERATOR_LIST";

	String INBUILDING = "INBUILDING";
	String FLOOR = "FLOOR";

	String STATIONARY_MAP_IMAGE = "nhiimg";
	String STATIONARY_MAP_INITIAL_IMAGE = "siteImage";
	String STATIONARY_MAIN_JASPER_NAME = "stationary_main.jasper";

	String RSRP_BEFORE = "rsrp_before";
	String SINR_BEFORE = "sinr_before";
	String PCI_BEFORE = "pci_before";
	String EARFCN_BEFORE = "earfcn_before";
	String THP_BEFORE = "thp_before";

	String RSRP_AFTER = "rsrp_after";
	String SINR_AFTER = "sinr_after";
	String PCI_AFTER = "pci_after";
	String EARFCN_AFTER = "earfcn_after";
	String THP_AFTER = "thp_after";

	String RSRP_COMPARISON_LEGEND = "rsrp_legend";
	String SINR_COMPARISON_LEGEND = "sinr_legend";
	String PCI_COMPARISON_LEGEND = "pci_legend";
	String EARFCN_COMPARISON_LEGEND = "earfcn_legend";
	String THP_COMPARISON_LEGEND = "thp_legend";

	String KEY_RECIPE_CUSTOM_GEO_MAP = "recipeCustomGeographyMap";

	String HEADER_NAME_VOICE = "Voice";

	String HEADER_NAME_DATA = "Data";

	String GEOGRAPHY_TYPE_POINT = "POINT";
	String CUSTOMER_COMPLAINT = "CUSTOMER_COMPLAINT";
	String BENCHMARK = "BENCHMARK";

	String RECIPE_CATEGORY_DRIVE = "Drive";

	String RECIPE_CATEGORY_STATIONARY = "Stationary";

	String RECIPEID = "recipeId";
	String OPERATOR = "operator";
	String INBUILDINGID = "inbuildingid";
	/** String WORKORDERID="workorderId";. */
	String RECEPILEVEL = "RECEPILEVEL";
	String WORKORDERLEVEL = "WORKORDERLEVEL";
	String DOT_ZIP = ".zip";
	String FLOORPLANIMG = "FloorPlanImage";
	String BACKGROUNDIMAGE = "Backgroundimage";

	String PEAK_VALUE_SINGLE_SAMPLE = "DL Peak Value (Single Sample)";

	String BENCHMARK_JASPER_PATH = "BENCHMARK_JASPER_PATH";

	String PREV_WORKORDER_ID = "prevWorkorderId";

	String COMPLAINT_REPORT_JASPER_FOLDER_PATH = "COMPLAINT_REPORT_JASPER_FOLDER_PATH";

	/** Sample 05 April 2018. */
	String DATE_FORMAT_DD_SP_MM_SP_YY = "dd MMMM yyyy";

	/** Sample 05-April-2018. */
	String DATE_FORMAT_DD_SP_MM_YY = "dd MMMM yy";

	String DATE_FORMAT_DD_MM_YY = "dd-MMMM-yy";

	String DATE_FORMAT_DAY_NAME = "EEEE";

	String MASTER_REPORT = "MASTER_REPORT";
	String MASTER = "MASTER";

	String IB_BENCHMARK = "IB_BENCHMARK";

	String LIVEDRIVE = "LIVEDRIVE";
	String INITIAL_PAGE = "INITIAL_PAGE";
	String MASTER_INITIAL_PAGE = "cluster_drive_report_front_page.jasper";
	String MASTER_THANK_YOU_PAGE = "cluster_drive_thank_you.jasper";

	String THANK_YOU = "THANK_YOU";

	String DL_THROUGHPUT = "DL_THROUGHPUT";
	String UL_THROUGHPUT = "UL_THROUGHPUT";
	String FTP_DL_THROUGHPUT = "FTP_DL_THROUGHPUT";
	String FTP_UL_THROUGHPUT = "FTP_UL_THROUGHPUT";
	String HTTP_DL_THROUGHPUT = "HTTP_DL_THROUGHPUT";
	String HTTP_UL_THROUGHPUT = "HTTP_UL_THROUGHPUT";
	String PDSCH_THROUGHPUT = "PDSCH_THROUGHPUT";
	String PUSCH_THROUGHPUT = "PUSCH_THROUGHPUT";
	String MAC_DL_THROUGHPUT = "MAC_DL_THROUGHPUT";
	String MAC_UL_THROUGHPUT = "MAC_UL_THROUGHPUT";

	String JITTER = "JITTER";
	String LATENCY = "LATENCY";
	String WEB_DOWNLOAD_DELAY = "WEB_DOWNLOAD_DELAY";
	String WEB_DOWNLOAD_DELAY_IB = "WEB_DOWNLOAD_DELAY_IB";
	String DBM_UNIT_REPORT = " (dBm)";
	String DB_UNIT_REPORT = " (dB)";

	String PCI = "PCI";

	String PILOT = "PILOT";

	String RSRQ = "RSRQ";
	String RSSI = "RSSI";

	String CONNECTION_SETUP_TIME = "CONNECTION_SETUP_TIME";

	String KEY_KPILIST = "KEY_KPILIST";
	String KEY_WRAPPER = "KEY_WRAPPER";

	String FTP_DOWNLOAD = "FTP_DOWNLOAD";
	String HTTP_DOWNLOAD = "HTTP_DOWNLOAD";
	String FTP_UPLOAD = "FTP_UPLOAD";
	String HTTP_UPLOAD = "HTTP_UPLOAD";

	String TRUE = "true";
	String FALSE = "false";

	int ONE_HUNDRED_TEN = 110;
	String HEADER_NAME_COVERAGE = "Coverage";

	String MS_UNIT_REPORT = "ms";

	String MS = " (ms)";

	String CALL_SMALL = "call";

	String JPEG = "jpeg";

	String WORKORDERREPORT = "WorkOrderReport";
	String AVG_RSRP = "AVG_RSRP";
	String AVG_DL = "AVG_DL";
	String AVG_UL = "AVG_UL";

	String STATS = "STATS";

	String CALL_PLOT = "CALL_PLOT";
	String HANDOVER_PLOT = "HANDOVER_PLOT";
	String RESELECTION_PLOT = "RESELECTION_PLOT";
	String ERAB_PLOT = "ERAB_PLOT";
	String CALL_SETUP_PLOT = "CALL_SETUP_PLOT";
	String HANDOVER_FAILURE_PLOT = "HANDOVER_FAILURE_PLOT";

	String CALL_INITIATE_ICON = "call_initiate.png";
	String CALL_FAIL_ICON = "call_fail.png";
	String CALL_DROP_ICON = "call_drop.png";
	String CALL_SUCCESS_ICON = "call_success.png";
	String CELL_RESELECTION="CELL_RESELECTION";

	String STATISTICS = "STATISTICS";

	String MEAN = "Mean";
	String MEDIAN = "Median";
	String MAXIMUM = "Maximum";
	String MINIMUM = "Minimum";
	String STANDARD_DEVIATION = "Standard Deviation";

	String MODE = "Mode";
	String COUNT = "count";

	String MOBILITY_TEST_COUNT = "Mobility test count";
	String MOBILITY_TEST_COUNT_ON_NET = "Mobility test count OnNet";
	String MOBILITY_TEST_COUNT_OFF_NET = "Mobility test count OffNet";
	String STATIONARY_TEST_COUNT = "Stationary test count";
	String STATIONARY_TEST_COUNT_ON_NET = "Stationary test count OnNet";
	String STATIONARY_TEST_COUNT_OFF_NET = "Stationary test count OffNet";

	String CALL_COUNT = "Call Count";
	String CALL_COUNT_ON_NET = "Call Count On Net";
	String CALL_COUNT_OFF_NET = "Call Count Off Net";
	String VARIANCE = "variance";

	String DBM = "dBm";
	String DB = "dB";

	String CALL_ATTEMPT = "Call Attempt";
	String CALL_CONNECTED = "Call Connected";
	String CALL_DROPPED = "Call Dropped";
	String CALL_SETUP_SUCCESS_RATE = "Call Setup Success Rate";
	String CALL_DROPPED_RATE = "Call Dropped Rate";

	String CALL_DROP_REASON = "CALL_DROP_REASON";

	String CALL_FAILURE = "Call Failure";

	/** ----------For InBuilding Benchmark Report----------//. */

	String KEY_RECIPE_OPERATOR_MAP = "recipeOperatorMap";
	
	String IMAGE_FIRST = "first";
	String IMAGE_THANKU = "thankyou";
	String IMAGE_FIRST_IMG = "first.png";
	String IMAGE_THANKU_IMG = "thankyou.png";
	String PIE_CHART = "piechart";
	String SURVEY_PATH = "surveypath";
	String PIE_CHART_IMG = "piechart.png";
	String IB_BENCHMARK_OPERATOR_NAME = "BN";
	String KEY_TECHNOLOGIES = "technologies";
	String IB_BENCHMARK_REPORT_NAME_PREFIX = "IB_BENCHMARK_REPORT_";
	String IB_BENCHMARK_GENERATED_REPORT_PATH = "IB_BENCHMARK_GENERATED_REPORT_PATH";
	String IB_BENCHMARK_JASPER_FILE_PATH = "IB_BENCHMARK_JASPER_FILE_PATH";
	String IB_BENCHMARK_INITIAL_JASPER_FILE_NAME = "main_report_2.jasper";
	String IB_BENCHMARK_THANK_YOU_PAGE_JASPER_FILE_NAME = "main_thank_you_page.jasper";
	String IB_BENCHMARK_REPORT_TEMP_PATH = "IB_BENCHMARK_REPORT_TEMP_PATH";
	String OPERATOR_MAP_KEY_PREFIX = "operator";
	String OPERATOR_IMAGE_MAP_KEY_SUFFIX = "Img";
	String TECHNOLOGY_LTE = "LTE";
	String IB_BENCHMARK_REPORT_FLOOR_PLAN_PATH = "IB_BENCHMARK_REPORT_FLOOR_PLAN_PATH";
	String KEY_ASSIGNED_TO = "assignedTo";
	String KEY_LEGEND_IMAGE_RSRP = "legendRSRP";

	Integer IB_BENCHMARK_LEGEND_IMAGE_WIDTH = 300;
	Integer IB_BENCHMARK_LEGEND_IMAGE_HEIGHT = 300;

	Integer IB_BENCHMARK_LEGEND_X_SPACING = 30;
	Integer IB_BENCHMARK_LEGEND_Y_SPACING = 30;

	int OPERATOR_COUNT_1 = 1;
	int OPERATOR_COUNT_2 = 2;
	int OPERATOR_COUNT_3 = 3;
	int OPERATOR_COUNT_4 = 4;

	Integer RSRP_DL_UL_RANGE_LIMIT = 6;
	Integer RSRP_PDSCH_PUSCH_RANGE_LIMIT = 6;
	Integer SINR_RANGE_LIMIT = 7;

	String TITLE_DL = "Download Throughput";
	String TITLE_UL = "Upload Throughput";

	String TITLE_PDSCH = "PDSCH Throughput";
	String TITLE_PUSCH = "PUSCH Throughput";

	String TITLE_GRAPH = " Graph";

	String RSRP_LABEL_WITH_UNIT = "RSRP (dBm)";
	String SINR_LABEL_WITH_UNIT = "SINR (dB)";
	String RSRQ_LABEL_WITH_UNIT = "RSRQ (dB)";
	String RSSI_LABEL_WITH_UNIT = "RSSI (dBm)";
	String DL_LABEL_WITH_UNIT = "DL Throughput (Mbps)";
	String UL_LABEL_WITH_UNIT = "UL Throughput (Mbps)";

	String PDSCH_LABEL_WITH_UNIT = "PDSCH Throughput (Mbps)";
	String PUSCH_LABEL_WITH_UNIT = "PUSCH Throughput (Mbps)";

	String DEFAULT_RANGE_LABEL = "Ranges";

	String IB_BENCHMARK_PCI_LEGEND = "BM_PCI_LEGEND";

	Integer IB_BM_PCI_LEGEND_MARGIN = 125;

	/** ----------For InBuilding Benchmark Report----------//. */

	String FTP_DL_RSRP = "FTP_DL_RSRP";
	String FTP_UL_RSRP = "FTP_UL_RSRP";
	String HTTP_DL_RSRP = "HTTP_DL_RSRP";
	String HTTP_UL_RSRP = "HTTP_UL_RSRP";

	String FTP_DL_SINR = "FTP_DL_SINR";
	String FTP_UL_SINR = "FTP_UL_SINR";
	String HTTP_DL_SINR = "HTTP_DL_SINR";
	String HTTP_UL_SINR = "HTTP_UL_SINR";

	String BUILDING_ID_KEY = "buildingIdKey";
	String HTTP_DL = "HTTP_DL";
	String FTP_DL = "FTP_DL";
	String HTTP_UL = "HTTP_UL";
	String FTP_UL = "FTP_UL";

	String HANDOVER_LONG_CALL_IN = "handover_long_call_in";
	String HANDOVER_LONG_CALL_OUT = "handover_long_call_out";
	String HANDOVER_DL_IN = "handover_dl_in";
	String HANDOVER_DL_OUT = "handover_dl_out";
	String LONG_CALL = "long_call";
	String LONG_CALL_DL = "long_call_dl";

	String STATIONARY_LONG_DL = "stationary_long_dl";
	String STATIONARY_LONG_UL = "stationary_long_ul";
	String STATIONARY_PING = "stationary_ping";
	String STATIONARY_RRC = "stationary_rrc";
	String STATIONARY_SC = "stationary_sc";

	String LESS_THAN_SYMBOL = "<";
	String DATE_FORMAT_DD_MM_YY_HH_SS = "ddMMMyyHHmmssSSS";
	String DATE_FORMAT_DD_MM_YY_SS_AA = "yyyy-MM-dd HH:mm:ss:SSS a";
	int ONE_HUNDRED_FIFTEEN = 115;

	String DATE_FORMAT_DDMMMYYHH = "ddMMMyyHH";

	String DATE_FORMAT_DD_MM_YYYY_HH_MM_SS = "dd/MM/YYYY | HH:mm:ss";

	String DATE_FORMAT_DD_MM_YYYY = "dd/MM/YYYY";

	String DATE_FORMAT_HH_MM_SS = "HH:mm:ss";

	String IDLE_PLOT = "IDLE_PLOT";

	String IDLE = "IDLE";

	String PIPE = "|";

	int FILE_NAME_WITHOUT_EXTENSION_INDEX = 0;

	String DOT_SPLIT_STRING = "\\.";

	String BRTI = "BRTI";

	String RECIPE = "recipe";

	String DATA = "data";
	String WORKORDER_IDS = "workOrderIds";

	String CHECK_TEMP_DATA = "CHECK_TEMP_DATA";

	String IMAGE_EARFCN = "earfcnimg";
	String IMAGE_EARFCN_LEGEND = "earfcnlegend";

	Integer IB_FLOOR_PLAN_IMAGE_EXTRA_MARGIN = 20;
	String USER_DIR = "user.dir";
	String SOFFICE_COMMAND = "soffice --infilter=" + "writer_pdf_import" + " --convert-to doc ";

	String WORKORDER_STR = "Workorder-";
	String RECIPE_STR = "Recipe-";
	String RECIPE_OPERATOR_MAP = "recipeOperatorMap";
	/**
	 *  String[] BRTI_QOERTLY_REPORT__CALL_HEADER
	 * ={"Area","Service","Day","Date","Hour","Place","Address","Total Call","Fail
	 * Call","Drop Call","ESA Rate(>=90%)","Drop Rate (<=5%)"};.
	 */

	
	
	String PERCENT = "%";

	String CA = "CA";
	String SERVING_SYSTEM = "SERVING_SYSTEM";

	String NETWORKTYPE_GSM = "GSM";
	String NETWORKTYPE_UMTS = "UMTS";

	String WPT_TEST = "WPT Test";

	String YOUTUBE_TEST = "YouTube Test";

	String ACTIVE_TEST = "Active Test";

	String SAUT = "SAUT";

	String TEST_TYPE_YOUTUBE = "YOUTUBE_TEST";

	String TEST_TYPE_WPT_BROWSE = "HTTP Browsing";

	int INDEX_SIX = 6;
	int INDEX_SEVEN = 7;

	String LOGO_YOUTUBE_KEY = "youtube";

	String LOGO_INTERNET_KEY = "internet";

	String LOGO_YOUTUBE_IMG = "youtube.png";

	String LOGO_INTERNET_IMG = "internet-explorer.png";

	String LOGO_TABLE_KEY = "table";

	String LOGO_TABLE_IMG = "table.jpg";

	String LOGO_WHATSAPP_KEY = "whatsapp";

	String LOGO_WHATSAPP_IMG = "whatsapp.png";

	String LOGO_VOLTE_KEY = "volte";

	String LOGO_VOLTE_IMG = "VoLte.png";

	String SMFREPORT_JASPER_FOLDER_PATH = "SMFREPORT_JASPER_FOLDER_PATH";

	String PPTX_EXTENSION = ".pptx";

	String CUSTOMER_EXPERIENCE_REPORT = "Customer Experience Report";
	String SAUT_BENCHMARK_JASPER_PATH = "SAUT_BENCHMARK_JASPER_PATH";

	String CALL_CAPITAL = "CALL";
	String CALL_ON_NET = "CALL_ON_NET";
	String CALL_OFF_NET = "CALL_OFF_NET";

	String NAME = "name";

	Object RECIPE_UNITID_MAP = "recipeUnitIdMap";

	String BRTI_SMS_TEMPLATE_PATH = "BRTI_SMS_TEMPLATE_PATH";

	String BRTI_SMS_SHEET_NAME = "BRTISMSReport.xlsx";

	String BRTI_COMBINE_EXCEL_SHEET_NAME = "BRTI_QOS_QUATERLY_COMBINE_REPORT.xls";

	String REMARK_REPORT = "REMARK_REPORT";

	String REMARK_REPORT_JASPER = "remark_report.jasper";

	String REMARK_JASPER_FOLDER = "REMARK_JASPER";
	String SUMMARY = "summary";

	String NETWORK_PERFORMANNCE = "NETWORK PERFORMANCE";

	String BRTI_YEARLY_TEMPLATE = "BRTIYearlyReport.xlsx";

	String REPORT_TYPE = "reportType";

	String BRTI_YEARLY_REPORT_TYPE = "BRTI Yearly Report";

	String BRTI_REPORT_TYPE_QUATERLY = "BRTI Quarterly Report";

	String BRTI_REPORT_TYPE_QUATERLY_COMBINE = "BRTI Quarterly Combine Report";

	String XLS_EXTENSION = ".xls";

	String ARRIVAL_LIST = "arrivalList";
	String DEPARTURE_LIST = "departureList";
	String INTERMEDIATE_LIST = "intermediateList";
	String BRTI_YEARLY_TEMPLATE_PATH = "BRTI_YEARLY_TEMPLATE_PATH";

	String HEADING_ONE = "A :  Drive Test";
	String HEADING_TWO = "B :  Static Call";

	String HEADING_THREE = "C :  Drive SMS";

	String HEADING_FOUR = "D :  Static SMS";

	String ALL = "All";

	String ON_NET = "On -Net";

	String OFF_NET = "Off-Net";

	String XLSX_EXTENSION = ".xlsx";

	String NATIONAL = "National";
	String TOTAL = "Total";

	String HEADER_CITY = "City";
	String HEADER_SERVICE = "Service";

	String BRTI_EXCEL_SHEET_ONE_NAME = "KPI";
	String BRTI_EXCEL_SHEET_TWO_NAME = "BACKUP";

	String DRIVE_TEST = "DRIVE TEST ";
	String STATIC_CALL_TEST = "STATIC CALL TEST ";
	String END_POINT_SERVICE_AVAILABILITY = "END POINT SERVICE AVAILABILITY ";
	String SMS_TEST = "SMS TEST ";
	String VALUE = "value";

		
	
	
	String BRTI_YEARLY_REPORT_JASPER_PATH = "BRTI_YEARLY_REPORT_JASPER_PATH";

	String IMAGE_PARAM_SCREEN_2 = "02";

	String IMAGE_SECOND_SCREEB = "second_screen";

	String KEY_IS_MOBILITY_TEST_AVAILABLE = "isMobilityTestAvailable";
	String KEY_IS_STATIONARY_TEST_AVAILABLE = "isStationaryTestAvailable";
	String KEY_DO_PRINT_METRICS = "doPrintMetricsPage";
	String KEY_WORKORDER_NAME = "workorderName";
	String KEY_DATE_TIME = "dateTime";
	String KEY_IS_STEALTH_REPORT = "isStealthReport";

	String REPORT_TEMPLATE_PATH = "REPORT_TEMPLATE_PATH";

	String HANDOVER_INITIATE = "Handover Initiate";
	String HANDOVER_SUCCESS = "Handover Success";
	String HANDOVER_FAILURE = "Handover Failure";
	String HANDOVER_INTERRUPTION_TIME = "HANDOVER_INTERRUPTION_TIME";

	String UNLOCK = "Unlock";
	String TDD = "TDD";

	String FDD = "FDD";

	String BRTI_PPT_NAME = "BRTIQuarterly_";

	String PPT = "PPT";

	String IMAGE_VOICE_MOBILE_RSRP = "voiceMobileRsrpImg";

	String SERVING_CELL = "SERVING_CELL";

	String HEALTH_INDEX = "HEALTH_INDEX";

	String IMAGE_VOICE_MOBILE_RSRP_LEGEND = "voiceMobileRsrpLegend";

	Double INVALID_RSRP_VALUE = -150.0;

	String IMAGE_MOBILE_INTERNET_RSRP = "mobileInternetRsrpImg";

	String IMAGE_MOBILE_INTENET_RSRP_LEGEND = "mobileInternetRsrpLegend";

	String STR_GOOD = "Good";
	String STR_FAIR = "Fair";
	String STR_POOR = "Poor";

	Double DL_GOOD_VLAUE = 5d;
	Double DL_POOR_VLAUE = 1d;

	Double UL_GOOD_VLAUE = 3d;
	Double UL_POOR_VLAUE = 0.7d;

	Double YT_DL_GOOD_VLAUE = 1.5d;
	Double YT_DL_POOR_VLAUE = 0.5d;

	Double RESPONSE_TIME_GOOD_VLAUE = 100d;
	Double RESPONSE_TIME_POOR_VLAUE = 300d;

	Double RSRP_GOOD_VLAUE = -100d;
	Double RSRP_POOR_VLAUE = -105d;

	Double PACKET_LOSS_GOOD_VLAUE = 1d;
	Double PACKET_LOSS_POOR_VLAUE = 5d;

	Double LATENCY_GOOD_VLAUE = 100d;
	Double LATENCY_POOR_VLAUE = 33d;

	String PREPOST = "PREPOST";

	Double SQUARE_WIDTH_SIZE = 10d;

	Double RSRP_WORST_VALUE = -140d;

	Double SINR_GOOD_VLAUE = 5d;
	Double SINR_POOR_VLAUE = -2d;
	Double SINR_WORST_VALUE = -20d;

	Double RSRQ_GOOD_VLAUE = -9d;
	Double RSRQ_POOR_VLAUE = -13d;
	Double RSRQ_WORST_VALUE = -20d;
	Double UL_WORST_VALUE = 0d;

	/** For Stealth Report. */

	Integer STEALTH_INDEX_TEST_TYPE = 3;
	Integer STEALTH_INDEX_TIMESTAMP = 4;
	Integer STEALTH_INDEX_RSRP = 26;
	Integer STEALTH_INDEX_SINR = 29;
	Integer STEALTH_INDEX_RSRQ = 27;
	Integer STEALTH_INDEX_ENODEB = 25;
	Integer STEALTH_INDEX_UL = 30;
	String STEALTH_TEST_TYPE_DL = "DOWNLOAD";
	String STEALTH_TEST_TYPE_UL = "UPLOAD";
	String STEALTH_TEST_TYPE_QUICK_TEST = "Quick Test";
	String STEALTH_TEST_TYPE_FULL_TEST = "Full Test";
	String STEALTH_TEST_TYPE_YOUTUBE = "YOUTUBE_TEST";
	String STEALTH_TEST_TYPE_WPT = "Web Test";

	Integer STEALTH_INDEX_DL_DL = 30;
	Integer STEALTH_INDEX_ACTIVE_DL = 31;

	Integer STEALTH_INDEX_YT_DL = 30;

	Integer STEALTH_INDEX_ACTIVE_LATENCY = 32;
	Integer STEALTH_INDEX_WPT_LATENCY = 38;

	Integer STEALTH_INDEX_WPT_DNS = 35;

	Integer STEALTH_INDEX_WPT_RESPONSE_TIME = 37;

	Integer STEALTH_INDEX_WPT_PACKET_LOSS = 53;
	Integer STEALTH_INDEX_ACTIVE_PACKET_LOSS = 35;

	Integer STEALTH_INDEX_YT_BUFFER_TIME = 33;

	Integer STEALTH_INDEX_YT_ITERATION = 37;

	String STEALTH_REPORT_NAME = "Device Analysis Report";

	String STEALTH_TEST_NAME = "Device Analysis";

	String CUSTOMER_EXPERIENCE_TEST_NAME = "Walk Test";

	String ACTIVE = "ACTIVE";
	String PASSIVE = "PASSIVE";

	String START_TIMESTAMP = "startTimestamp";
	String END_TIMESTAMP = "endTimestamp";

	String GEOGRAPHY_COLUMN_GL1 = "gL1";
	String GEOGRAPHY_COLUMN_GL2 = "gL2";
	String GEOGRAPHY_COLUMN_GL3 = "gL3";
	String GEOGRAPHY_COLUMN_GL4 = "gL4";
	String COLUMN_FAMILY_R = "r";

	String ACTIVE_TABLE_NAME = "NVActive";

	String EARFCN_UL = "Earfcn_UL";
	String EARFCN_DL = "Earfcn_DL";
	String PMI = "PMI";
	String BANDWIDTH_UL = "Bandwidth_UL";
	String BANDWIDTH_DL = "Bandwidth_DL";
	String SPATIAL_RANK = "Spatial_Rank";
	String POWER_TX_PUSCH = "Power_Tx_Pusch";
	String CARRIER_INDEX = "Carrier_Index";
	String BLER = "Bler";


	/** For Stealth Workorder Report. */

	String STEALTH = "STEALTH";

	String STEALTH_KEY_USER_NAME = "User";

	String STEALTH_KEY_DATE = "WO Execution Date";

	String STEALTH_KEY_WORKORDER_NAME = "Workorder Name";

	String STEALTH_KEY_WO_SCHEDULE_TYPE = "Schedule Type";

	String STEALTH_KEY_NO_OF_EXECUTIONS = "No. of Executions";

	String STEALTH_KEY_WO_EXECUTION_DAY_WEEKLY = "WO Execution Day";

	String STEALTH_META_FREQUENCY_TYPE_WEEKLY = "WEEKLY";

	String STEALTH_KEY_EXECUTION_TIME = "Execution Time";

	String STEALTH_KEY_EXECUTION_DATE = "Execution Start Date";

	String STEALTH_KEY_END_DATE = "Execution End Date";

	String META_KEY_WO_FREQUENCY = "woFrequency";
	String META_KEY_FREQUENCY = "frequency";

	String ACCEPT = "Accept";
	String REJECT = "Reject";
	String PENDING = "Pending";

	String SUCCESS = "Success";
	String FAILURE = "Failure";
	String INPROGRESS = "InProgress";

	String KEY_WELCOME_IMAGE = "welcomeImage";
	String KEY_HEADER_IMAGE = "headerImage";
	String KEY_TITLE_IMAGE = "titleImage";
	String KEY_WO_NAME = "woName";

	String STEALTH_REPORT = "STEALTH_REPORT_";

	String STEALTH_WO_REPORT_JASPER_FILE_PATH = "STEALTH_WO_REPORT_JASPER_FILE_PATH";

	int STEALTH_JSON_GEOGRAPHY_INDEX = 72;

	int STEALTH_JSON_DL_INDEX = 27;
	int STEALTH_JSON_RSRP_INDEX = 23;
	int STEALTH_JSON_TTL_INDEX = 63;
	int STEALTH_JSON_NETWORK_TYPE_INDEX = 2;

	String R1 = "r1";
	String R2 = "r2";
	String R3 = "r3";
	String R4 = "r4";
	String R5 = "r5";
	String R6 = "r6";
	String R7 = "r7";

	String MIN = "min";
	String MAX = "max";
	String AVG = "avg";
	String SUM = "sum";
	String CNT = "CNT";

	String DNS = "dns";
	String TOTAL_DNS = "totaldns";
	String TTL = "ttl";
	String TTFB = "ttfb";

	String BUFFERING = "bfring";
	String FREEZING_RATIO = "frzratio";
	String LOAD_TIME = "lt";
	String STALLING = "stl";

	String ACCEPTED = "Accepted";

	String REJECTED = "Rejected";
	String CLOSED = "Closed";

	String TIME = "time";

	String KPI_DATA_TABLE = "KPI_DATA_TABLE";
	String GEOGRAPHY_L1_ZOOM = "GeographyL1Zoom";
	String GEOGRAPHY_L2_ZOOM = "GeographyL2Zoom";
	String GEOGRAPHY_L3_ZOOM = "GeographyL3Zoom";
	String GEOGRAPHY_L4_ZOOM = "GeographyL4Zoom";

	String NV_DASHBOARD = "NV_DASHBOARD";

	String NV_DASHBOARD_TEMPLATE_NAME = "NvDashboardTemplate.xlsx";

	String NV_DASHBOARD_RSRP_TITLE = "RSRP Map ";
	String NV_DASHBOARD_DL_TITLE = "Throughput Map ";

	Integer RSRP_GOOD_FOR_DASHBOARD = -105;
	Integer DL_GOOD_FOR_DASHBOARD = 1;

	Integer STEALTH_INDEX_YT_VIDEO_DURATION = 31;

	String KEY_IMEI = "IMEI";
	String KEY_IMSI = "IMSI";
	String KEY_MAKE = "Make";
	String KEY_MODEL = "Model";

	Integer STEALTH_INDEX_HEADER_IMEI = 2;
	Integer STEALTH_INDEX_HEADER_IMSI = 3;
	Integer STEALTH_INDEX_HEADER_MAKE = 6;
	Integer STEALTH_INDEX_HEADER_MODEL = 7;

	String DATE_FORMAT_STEALTH_HBASE = "ddMMyy";

	String STEALTH_HOURLY_DATE_FORMAT = DATE_FORMAT_STEALTH_HBASE + " HH:mm";

	Integer NVDASHBOARD_LEGEND_IMAGE_WIDTH = 300;
	Integer NVDASHBOARD_LEGEND_IMAGE_HEIGHT = 120;

	String AUTO_CELL_RANGE_MAX_WEEK = "AUTO_CELL_RANGE_MAX_WEEK";

	String ATOLL = "Atoll";

	String DEFAULT_MAP_VIEWPORT = "DefaultMapViewPort";

	String ATOLL_RSRP_IMAGE = "atollRSRPImg";

	String CLM_MAX_LAT = "maLat";
	String CLM_MAX_LNG = "maLng";
	String CLM_MIN_LAT = "miLat";
	String CLM_MIN_LNG = "miLng";
	String CLM_GEO_NAME = "gn";

	String R_MAX_LAT = "r:" + CLM_MAX_LAT;
	String R_MAX_LNG = "r:" + CLM_MAX_LNG;
	String R_MIN_LAT = "r:" + CLM_MIN_LAT;
	String R_MIN_LNG = "r:" + CLM_MIN_LNG;
	String R_GEO_NAME = "r:" + CLM_GEO_NAME;

	String ATOLL_LEGEND = "AtollLegend";

	String LEGEND_TECHNOLOGY = "legendTech";

	String ATOLL_RSRP_LEGEND_IMAGE = "atollRSRPLegendImg";

	String STEALTH_DETAIL_DEVICE_ID_COLUMN_KEY = "deviceid";

	String STEALTH_DETAIL_LATITUDE_COLUMN_KEY = "lat";

	String STEALTH_DETAIL_LONGITUDE_COLUMN_KEY = "lng";

	String STEALTH_DETAIL_SCORE_COLUMN_KEY = "score";

	String DATE_FORMAT_DDHMMHYYYY = "dd-MM-yyyy";

	String DATE_FORMAT_DD_MM = "dd/MM";

	String KEY_FILE_PATH = "filePath";

	String DOT_CSV = ".csv";

	String BAND_TYPE = "bandType";

	String BENCHMARK_OPERATOR_STRING = "BN";

	String PING_LATENCY = "pingLatency";
	String PING_JITTER = "pingJitter";
	String PING_PACKET_LOSS = "pingPacketLoss";

	String STEALTH_DEVICES_MAP_IMAGE_NAME = "device_map_image" + DOT_JPG;

	String STEALTH_DEVICES_MAP_IMAGE_KEY = "deviceMapImage";

	String DOT_SVG = ".svg";

	String STEALTH_DATA_RSRP_JSON_COLUMN_NAME = "rsrpJson";
	String STEALTH_DATA_RSRQ_JSON_COLUMN_NAME = "rsrqJson";
	String STEALTH_DATA_SINR_JSON_COLUMN_NAME = "sinrJson";
	String STEALTH_DATA_DL_JSON_COLUMN_NAME = "dlJson";
	String STEALTH_DATA_UL_JSON_COLUMN_NAME = "ulJson";

	String PDF_REPORT = "PDF_REPORT";

	/**
	 * ---------------------------------------------// For Drive Benchmark Report //
	 * ---------------------------------------------//.
	 */

	String DRIVE_BENCHMARK_REPORT_JASPER_DIRECTORY_PATH = "DRIVE_BENCHMARK_REPORT_JASPER_DIRECTORY_PATH";
	String KEY_WELCOME_LOGO = "welcomeLogo";
	String IMAGE_NAME_WELCOME_LOGO = "welcome_logo.png";
	String KEY_FOOTER_LOGO = "footerLogo";
	String IMAGE_NAME_FOOTER_LOGO = "footer.png";
	String KEY_HEADER_LOGO = "headerLogo";
	String IMAGE_NAME_HEADER_LOGO = "header_logo.png";
	String IMAGE_NAME_HEADER_LOGO_JPG = "header-logo.jpg";

	/**
	 * ---------------------------------------------// For Drive Benchmark Report //
	 * ---------------------------------------------//.
	 */

	String JSON_KEY_WHATSAPP_LATENCY = "whatsappLatency";

	String GWO_META_ENTITY_CEMS_DATA = "cemsData";

	String JSON_KEY_VOLTE_MOS_PERCENT = "goodmos";
	String JSON_KEY_RTP_LOSS_PERCENT = "goodpacketloss";

	int DATE_COMPARISON_NUMBER = 0;

	String RECIPE_CATEGORY_CAPTURE_IMAGE = "CaptureImage";

	String IMAGE_CATEGORY_AZIMUTH = "Azimuth";
	String IMAGE_CATEGORY_ELECTRONIC_TILT = "Electrical";
	String IMAGE_CATEGORY_ANTENNA_HEIGHT = "Height";
	String IMAGE_CATEGORY_PANORAMA_VIEW = "Panoram";
	String IMAGE_CATEGORY_MECHNICAL_TILT = "MechanicalTilt";

	String START_DATE = "startDate";
	String END_DATE = "endDate";
	String DOUBLE_SPACE_COLON_DOUBLE_SPACE = "  :  ";
	/** Logger Constants //. */

	String INDEX_LIST = "indexList ";
	String FINAL_CORNER_DATA = "Final Corner Data ";
	String ZOOM_LEVEL = "Zoom Level ";
	String GOOGLE_TILE_MAP_MIN = "GoogleTile Min ";
	String GOOGLE_TILE_MAP_MAX = "GoogleTile Max ";

	String BRTI_QUARTERLY_REPORT_FILE_NAME = "BRTIQuarterlyReport";

	String SHOW_DASHBOARD_PAGE = "showDashboardPage";
	String SHOW_DEVICE_PAGE = "showDevicePage";

	String THROUGHPUT = "THROUGHPUT";
	String THP = "THP";

	String DEFAULT = "Default";
	String LTE_SPACE_PCI = "LTE PCI";

	String DRIVE_ROUTE = "DRIVE_ROUTE";
	String BENCHMARK_REPORT_OPERATOR_LOGO = "BENCHMARK_REPORT_OPERATOR_LOGO";
	Integer TWO_DECIMAL_PLACES = 2;
	String INST_MOS = "INST_MOS";

	Integer BENCHMARK_LEGEND_IMAGE_WIDTH = 300;
	Integer BENCHMARK_LEGEND_IMAGE_HEIGHT = 60;

	Double TOTAL_PERCENT = 100.0D;

	String BENCHMARK_RANGE_COMPARATOR_CHARACTER = "t";

	String STEALTH_REPORT_HOURLY_DATE_REPRESENTATION_FORMAT = "dd-MM-yy HH:mm";

	String SITE_LOCATION_IMG = "sitesLcnImg";

	String ENABLED = "enabled";

	String DISABLED = "disabled";
	String SITE_DATABASE_LEGEND_IMG = "siteDbLegendImg";

	String TOTAL_DISTANCE = "Total Distance";
	String TOTAL_DISTANCE_UNIT = " Meters";
	String ONAIR = "ONAIR";

	String PLANNED = "PLANNED";

	String DECOMMISSIONED = "DECOMMISSIONED";

	/**
	 * ------------------------------------DO NOT MELD FROM
	 * PRODUCTION/STAGING------------------------------------//.
	 */
	String RSRP_LEGEND_REMARK_JSON = "{\"-40.0 to -90.0\" : \"Perfect\", \"-90.0 to -95.0\" : \"Excellent\", \"-95.0 to -100.0\" : \"Very Good\", \"-100.0 to -105.0\" : \"Good\", \"-105.0 to -113.0\" : \"Bad\", \"-113.0 to -140.0\" : \"Worst\"}";
	String SINR_LEGEND_REMARK_JSON = "{\"30.0 to 24.0\" : \"Perfect\", \"24.0 to 18.0\" : \"Excellent\", \"18.0 to 12.0\" : \"Very Good\", \"12.0 to 6.0\" : \"Good\", \"6.0 to 3.0\" : \"Fair\", \"3.0 to 0.0\" : \"Bad\", \"0.0 to -20.0\" : \"Worst\"}";
	String DL_LEGEND_REMARK_JSON = "{\"500.0 to 26.0\" : \"Perfect\", \"26.0 to 20.0\" : \"Excellent\", \"20.0 to 10.0\" : \"Good\", \"10.0 to 6.0\" : \"Fair\", \"6.0 to 2.0\" : \"Bad\", \"2.0 to 0.0\" : \"Worst\"}";
	String UL_LEGEND_REMARK_JSON = "{\"10.0 to 5.0\" : \"Perfect\", \"5.0 to 3.0\" : \"Excellent\", \"3.0 to 1.0\" : \"Very Good\", \"1.0 to 0.512\" : \"Good\", \"0.512 to 0.256\" : \"Fair\", \"0.256 to 0.0\" : \"Bad\"}";
	/**
	 * ------------------------------------DO NOT MELD FROM
	 * PRODUCTION/STAGING------------------------------------//.
	 */
	String CQI_LEGEND_REMARK_JSON = "{\"30.0 to 24.0\" : \"Perfect\", \"24.0 to 20.0\" : \"Excellent\", \"20.0 to 15.0\" : \"Very Good\", \"15.0 to 10.0\" : \"Good\", \"10.0 to 5.0\" : \"Fair\", \"5.0 to 0.0\" : \"Bad\"}";

	String STEALTH_HOURLY_DEFAULT_MINUTES = "00";

	String STEALTH_DATE_FORMAT_FOR_REPORT = "dd-MMM-yy HH:mm";

	Integer STEALTH_HBASE_DATE_STRING_LENGTH = 6;

	int FOUR_FIVE_ZERO = 450;
	int THREE_SIX_ZER0 = 360;

	int FOURTY = 40;

	int INDEX_TWENTY_THREE = 23;

	int INDEX_EIGHTEEN = 18;

	int TWENTY_FIVE = 25;

	int INDEX_NINE = 9;

	int TWENTY_FOUR = 24;

	int INDEX_EIGHTY_FIVE = 85;
	int INDEX_FOURTY_FIVE = 45;
	int INDEX_FIFTY = 50;
	int INDEX_SIXTY = 60;
	int INDEX_SIXTY_FIVE = 65;
	int INDEX_NINTY_FIVE = 95;
	Double ZERO_DOT_SEVEN_SIX_EIGHT = 0.768;
	Double ZERO_DOT_TWO_FIVE_SIX = 0.256;
	Double ZERO_DOT_FIVE_ONE_TWO = 0.512;
	Double ZERO_DOT_FOUR = 0.4;
	Double ZERO_DOT_SIX = 0.6;
	Double ZERO_DOT_THREE = 0.3;
	Double ZERO_DOT_ONE = 0.1;
	Double ZERO_DOT_FIFTEEN = 0.15;

	String WIFI_RSSI = "WIFI_RSSI";
	String WIFI_SNR = "WIFI_SNR";

	String BRTI_SINGLE_TECHNOLOGY = "Single_Operator";
	String BRTI_MULTIPLE_TECHNOLOGY = "Multiple_Operator";

	String TECHNOLOGY_WIFI = "WIFI";

	String CLOT_WO_COMPARISON = "CLOT_WO_COMPARISON";

	String PREV_WORKORDER_IDS = "prevWorkorderIds";

	String TERRRAIN_VIEW = "TERRAIN";

	String CLOTREPORT_JASPER_FOLDER_PATH = "CLOTREPORT_JASPER_FOLDER_PATH";
	String CLOT_ACCEPTANCE_REPORT_JASPER_FOLDER_PATH = "CLOT_ACCEPTANCE_REPORT_JASPER_FOLDER_PATH";

	String CLEAR = "Clear";

	String NOT_CLEAR = "Not Clear";

	String CLUSTER_WO_COMPARISON_REPORT_JASPER_FOLDER_PATH = "CLUSTER_WO_COMPARISON_REPORT_JASPER_FOLDER_PATH";
	String SSVT_WO_COMPARISON_REPORT_JASPER_FOLDER_PATH = "SSVT_WO_COMPARISON_REPORT_JASPER_FOLDER_PATH";
	String IBC_QUICK_REPORT_JASPER_FOLDER_PATH = "IBC_QUICK_REPORT_JASPER_FOLDER_PATH";
	String INBUILDING_ONEC_JASPER_PATH = "INBUILDING_ONEC_JASPER_PATH";

	String SITE_INFO = "siteInfo";
	String SITE_NAME = "siteName";
	String TEST_TYPE = "testType";
	String SSVT_QUICK = "QUICK";
	String REPORT_INSTANCE_ID = "reportInstanceId";
	String REPORT_NOT_GENERATED = "\"result\":\"Report Not Generated\"";
	String REPORT_GENERATED = "\"result\":\"Report Generated\"";

	String YES = "Yes";
	String NO = "No";
	String SITE_ID = "siteId";

	String KPI_SUMMARY = "kpiSummary";

	String GOOGLE_MAP_CLIENT_ID = "GOOGLE_MAP_CLIENT_ID";
	String GOOGLE_MAP_PRIVATE_KEY = "GOOGLE_MAP_PRIVATE_KEY";

	int THREE_THOUSAND = 3000;
	int FOUR_ZERO_NINE_SIX = 4096;

	int NINTY = 90;

	int ONE_TWO_ZER0 = 120;
	int TWO_FIVE_ZER0 = 250;
	int FOUR_INDEX = 4;

	String PEAK_VALUE_FDD = "PEAK_VALUE_FDD";
	String PEAK_VALUE_TDD = "PEAK_VALUE_TDD";

	String NE_ALARM_DATA = "NE_ALARM_DATA";
	String SITE_INFO_LIST = "SITE_INFO_LIST";

	String SF_DONWLOAD_ATTACHMENT_URL = "SF_DONWLOAD_ATTACHMENT_URL";

	String SSVTQUICK = "SSVTQUICK";

	String SSVTFULL = "SSVTFULL";

	String NVSSVT = "NVSSVT";

	String IMG_FILE_PATH = "imgFilePath";

	String UNICODE_8 = "UTF-8";

	String QUICK_WORKORDER_ID = "quickWorkorderId";

	String SSVT_IMAGE_TEMP_PATH = "SSVT_IMAGE_TEMP_PATH";
	String USE_MAP_PRIVATE_KEY = "USE_MAP_PRIVATE_KEY";
	String NOT_FOUND = "NA";
	String LATENCY32 = "LATENCY32";
	String LATENCY1000 = "LATENCY1000";
	String LATENCY1500 = "LATENCY1500";
	String HO_INTERRUPTION_TIME = "HO INTERRUPTION TIME";
	String RESPONSE_TIME = "RESPONSE TIME";

	Integer INDEX_STEALTH_CSV_DUMP_TIMESTAMP = 1;

	String HANDOVER_KEY = "Handover Success Rate";

	String CC_GET_COLOR_MAP_DATA = "CC_GET_COLOR_MAP_DATA";

	String GOOD_OUTDOOR_GOOD_INDOOR = "GOOD_OUTDOOR_GOOD_INDOOR";
	String GOOD_OUTDOOR_WEAK_INDOOR = "GOOD_OUTDOOR_WEAK_INDOOR";
	String WEAK_OUTDOOR_NO_INDOOR = "WEAK_OUTDOOR_NO_INDOOR";
	String NO_OUTDOOR_NO_INDOOR = "NO_OUTDOOR_NO_INDOOR";

	String NV_KPI_COMPARISON_REPORT_PATH = "NV_KPI_COMPARISON_REPORT_PATH";
	String KEY_VOLTE = "VOLTE";
	String KEY_RRC = "RRC";
	public static String NV_SSVT_REPORT_RECIPEID_MAP = "NV_SSVT_REPORT_RECIPEID_MAP";
	public static String SSVT_COMPARISION = "SSVT_COMPARISION";
	public static String IBC_WALKTEST_REPORT="IBC_WALKTEST_REPORT";
	public static String NV_IBC_REPORT_RECIPEID_MAP = "NV_IBC_REPORT_RECIPEID_MAP";


	String IS_PRE_DT = "IS_PRE_DT";

	String LATITUDE = "LATITUDE";
	String LONGITUDE = "LONGITUDE";
	String TIMESTAMP = "TIMESTAMP";
	String DB_TIMESTAMP_KEY = "latlngTimestamp";
	String NETWORK_TYPE = "networkType";
	String BAND = "BAND";
	String DL_BANWIDTH = "BANDWIDTH_DL";
	String CELL_ID = "CELLID";
	String SOURCE_PCI = "SOURCE_PCI";
	String TARGET_PCI = "TARGET_PCI";
	String HANDOVER_LATENCY = "HANDOVER_LATENCY";
	String SSID = "SSID";
	String MCC = "MCC";
	String MNC = "MNC";
	String VOLTE_JITTER = "VOLTE_JITTER";
	String VOLTE_PACKET_LOSS = "VOLTE_PACKET_LOSS";
	String POLQA_MOS = "POLQA_MOS";
	String UL_BANWIDTH = "BANDWIDTH_UL";
	String CALL_INITIATE = "CALL_INITIATE";
	String CALL_DROP = "CALL_DROP";
	String CALL_SUCCESS = "CALL_SUCCESS";
	String RESELECTION_SUCCESS = "RESELECTION_SUCCESS";
	String NV_CLOT = "NVCLOT";
	String PACKET_LOSS = "PACKET_LOSS";
	String RSCP = "RSCP";
	String ECIO = "ECIO";
	String LAYER3_SUMMARY_TABLE = "LAYER3_SUMMARY_TABLE";
	String KPINAME_KPISTATS = "kpiname {} , kpistat {}";
	String TOTAL_HANDOVER = "TOTAL_HANDOVER";
	String SMS_ATTEMPT = "SMS_ATTEMPT";
	String SMS_DELIVERED = "SMS_DELIVERED";
	String SMS_SUCCESS = "SMS_SUCCESS";
	String SMS_FAILURE = "SMS_FAILURE";
	String SMS_ATTEMPT_ON_NET = "SMS_ATTEMPT_ON_NET";
	String SMS_ATTEMPT_OFF_NET = "SMS_ATTEMPT_OFF_NET";
	String SMS_DELIVERED_ON_NET = "SMS_DELIVERED_ON_NET";
	String SMS_DELIVERED_OFF_NET = "SMS_DELIVERED_OFF_NET";
	String SMS_DELIVERED_LESS_THAN_3_MIN = "SMS_DELIVERED_LESS_THAN_3_MIN";
	String SMS_DELIVERED_LESS_THAN_3_MIN_ON_NET = "SMS_DELIVERED_LESS_THAN_3_MIN_ON_NET";
	String SMS_DELIVERED_LESS_THAN_3_MIN_OFF_NET = "SMS_DELIVERED_LESS_THAN_3_MIN_OFF_NET";
	String CALL_SETUP_TIME = "CALL_SETUP_TIME";
	String CALL_ATTEMPT_ON_NET = "CALL_ATTEMPT_ON_NET";
	String CALL_FAILURE_ON_NET = "CALL_FAILURE_ON_NET";
	String CALL_DROP_ON_NET = "CALL_DROP_ON_NET";
	String CALL_SUCCESS_ON_NET = "CALL_SUCCESS_ON_NET";
	String CALL_ATTEMPT_OFF_NET = "CALL_ATTEMPT_OFF_NET";
	String CALL_FAILURE_OFF_NET = "CALL_FAILURE_OFF_NET";
	String CALL_DROP_OFF_NET = "CALL_DROP_OFF_NET";
	String CALL_SUCCESS_OFF_NET = "CALL_SUCCESS_OFF_NET";
	String HTTP_ATTEMPT = "HTTP_ATTEMPT";
	String HTTP_SUCCESS = "HTTP_SUCCESS";
	String DL_HTTP = "DL_HTTP";

	String RRC_INITIATE = "RRC_INITIATE";
	String RRC_SUCCESS = "RRC_SUCCESS";
	String ERAB_SUCCESS_RATE = "ERAB_SUCCESS_RATE";
	String MOS_G711 = "MOS_G711";

	String HANDOVER_IN = "HANDOVER_IN";
	String HANDOVER_OUT = "HANDOVER_OUT";
	String VOLTE_HANDOVER_IN = "VOLTE_HANDOVER_IN";
	String VOLTE_HANDOVER_OUT = "VOLTE_HANDOVER_OUT";

	String PRE_DT = "PRE_DT";
	String POST_DT = "POST_DT";

	String NV_IBC_WALKTEST_REPORT_RECIPEID_MAP = "NV_IBC_WALKTEST_REPORT_RECIPEID_MAP";


	String MIN_UNDERSCORE="MIN_";
	String MAX_UNDERSCORE="MAX_";
	String AVG_UNDERSCORE="AVG_";
	String CNT_UNDERSCORE="CNT_";
	String SUM_UNDERSCORE="SUM_";
	String LATEST_UNDERSCORE="LATEST_";
	String DL_FTP = "DL_FTP";
	String UL_FTP = "UL_FTP";
	String UL_HTTP = "UL_HTTP";
	String WAVG = "wavg";
	String LATEST = "latest";
	String FDD3 = "FDD3";
	String FDD5 = "FDD5";
	String TDD40 = "TDD40";

	String MT_CALL_FAILURE = "MT_CALL_FAILURE";
	String MO_CALL_FAILURE = "MO_CALL_FAILURE";
	String NV_LAYER3_DRIVE_TABLE = "NV_LAYER3_DRIVE_TABLE";
	String ATTACH_ACCEPT = "ATTACH_ACCEPT";
	String MSG1_COUNT = "MSG1_COUNT";
	String MSG3_COUNT = "MSG3_COUNT";
	String NEIGHBOUR_DATA = "NeighbourData";
	String YOUTUBE_BUFFER_TIME = "YOUTUBE_BUFFER_TIME";
	String REMOVE_REJECTED_REPORT_DATA_URL = "REMOVE_REJECTED_REPORT_DATA_URL";
	String START_LATITUDE = "START_LATITUDE";
	String START_LONGITUDE = "START_LONGITUDE";
	String END_LATITUDE = "END_LATITUDE";
	String END_LONGITUDE = "END_LONGITUDE";

	String VIDEO_URL = "VIDEO_URL";
	String VIDEO_RESOLUTION = "VIDEO_RESOLUTION";
	String VIDEO_DURATION = "VIDEO_DURATION";
	String NUM_OF_STALLING = "NUM_OF_STALLING";
	String TOTAL_BUFFER_TIME = "TOTAL_BUFFER_TIME";

	String DL_BLER = "DL_BLER";
	String UL_BLER = "UL_BLER";
	String PDSCH_SINR = "PDSCH_SINR";
	String TAU_ATTEMPT= "TAU_ATTEMPT";
	String TAU_SUCCESS= "TAU_SUCCESS";
	String TAU_ACCEPT= "TAU_ACCEPT";
	String ERAB_REQUEST= "ERAB_REQUEST";
	String ERAB_SUCCESS= "ERAB_SUCCESS";

	String RRC_DROP= "RRC_DROP";
	String RRC_DROPPED = "RRC_DROPPED";
	String INITIAL_ERAB_SUCCESS_COUNT = "INITIAL_ERAB_SUCCESS_COUNT";
	String VOLTE_CODEC = "VOLTE_CODEC";
	String CODEC_AMR_12 = "AMR 12.2 KBIT/S (GSM-EFR)";
	String CODEC_AMR = "AMR SID";
	String CODEC_NO_DATA = "NO DATA";


	String X_POINT = "XPOINT";
	String Y_POINT = "YPOINT";
	String TOTAL_PACKET_COUNT = "TOTAL_PACKET_COUNT";
	String PING_BUFFER_SIZE = "PING_BUFFER_SIZE";

	String CLUTTER_TYPE= "CLUTTER_TYPE";

	String DL_MODULATION_TYPE = "DL_MODULATION_TYPE";
	String DL_MODULATION_QPSK = "QPSK";
	String DL_MODULATION_16_QAM	 = "16 QAM";
	String DL_MODULATION_64_QAM	 = "64 QAM";
	String DL_MODULATION_256_QAM = "256 QAM";

	String MIMO_RANK_0 = "rank 0";
	String MIMO_RANK_1 = "rank 1";
	String MIMO_RANK_2 = "rank 2";
	String MIMO_RANK_3 = "rank 3";
	String MIMO_RANK_4 = "rank 4";

	String DOWNLOAD_TIME_YOUTUBE = "DOWNLOAD_TIME_YOUTUBE";
	String DOWNLOAD_TIME_GOOGLE = "DOWNLOAD_TIME_GOOGLE";
	String DOWNLOAD_TIME_FACEBOOK = "DOWNLOAD_TIME_FACEBOOK";
	String SPEED_TEST_PIN_NUMBER = "SPEED_TEST_PIN_NUMBER";
	String SPEED_TEST_DL_RATE = "SPEED_TEST_DL_RATE";
	String SPEED_TEST_UL_RATE = "SPEED_TEST_UL_RATE";
	String WIFI_BSSID = "WIFI_BSSID";
	String WIFI_LINK_SPEED = "WIFI_LINK_SPEED";
	String WIFI_CHANNEL = "WIFI_CHANNEL";
	String INDOOR_BENCHMARK_JASPER_PATH = "INDOOR_BENCHMARK_JASPER_PATH";
	String STATIC_TEST = "Static Test";
	String MOBILITY_TEST = "Mobility Test";
	String ALPHA = "Alpha";
	String BETA = "Beta";
	String GAMMA = "Gamma";
	String TAG_DATA = "Data";
	String TAG_VOLTE = "VoLTE";
	String TAG_PING = "Ping";
	String STRING_ONE = "1";
	String STRING_NEW = "New";
	String VOLTE_SHORT_CALL = "VoLTE Short Call";
	String ICMP_PING= "ICMP Ping";
	String PROCEDURE_UL_FTP="UL FTP";
	String PROCEDURE_DL_FTP="DL FTP";

	String LAYER3_REPORT_COLUMN_HANDOVER = "handoverData";
	Integer INDEX_HO_DATA_TIMESTAMP = 0;
	Integer INDEX_HO_DATA_LATITUDE = 1;
	Integer INDEX_HO_DATA_LONGITUDE = 2;
	Integer INDEX_HO_DATA_INIT_RSRP = 3;
	Integer INDEX_HO_DATA_COMPLETE_RSRP = 4;
	Integer INDEX_HO_DATA_SOURCE_PCI = 5;
	Integer INDEX_HO_DATA_TARGET_PCI = 6;
	Integer INDEX_HO_DATA_INTERRUPTION_TIME = 7;
	Integer INDEX_HO_DATA_STATUS = 8;
	String TEST_STATUS_PASS = "PASS";
	String TEST_STATUS_FAIL = "FAIL";

	String TAC = "TAC";
	String CALL_FAIL_CAUSE = "CALL_FAIL_CAUSE";
	String CELL_STATUS_MAP= "cellStatusMap";
	String RECIPE_PCI_MAP= "recipePCIMap";
	String PROJECT_ID="projectId";

	String KEY_FLOOR_NAME = "floorName";
	String KEY_TASK_NAME = "taskName";
	String KEY_REPORT_ID = "reportId";
	String KEY_BUILDING_NAME = "buildingName";
	String SF_INTEGRATION_TASK_ATP_1C = "ATP 1C";
	String SF_INTEGRATION_TASK_ATP_1E = "ATP 1E";
	String NV_SSVT_SF_INTEGRATION_URL = "NV_SSVT_SF_INTEGRATION_URL";
	String IS_SITE_ACCEPTANCE = "isSiteAcceptance";
	String HEADER_X_API_KEY = "X-API-KEY";
	String DATE_FORMAT_LMP = "yyyy-MM-dd HH:mm";

	public static String NV_IB1E_REPORT_RECIPEID_MAP = "NV_IB1E_REPORT_RECIPEID_MAP";

	public static String KEY_SPILLAGE_TEST="SPILLAGE_TEST";
	public static String KEY_IN_OUT="IN_OUT";
	public static String KEY_STAIR_CASE="STAIR_CASE";
	public static String KEY_ELEVATOR="ELEVATOR";
	public static String KEY_IDLE="IDLE";
	public static String KEY_DATA="DATA";
	public static String KEY_DATA_UL="DATA_UL";
	public static String KEY_SPILLAGE_TEST_SUMMARY="KEY_SPILLAGE_TEST_SUMMARY";
	String NV_SSVT_SF_INTEGRATION_X_API_KEY = "NV_SSVT_SF_INTEGRATION_X_API_KEY";

	String REPORT_ONLY = "Report Only";
	String INBUILDING_ONEE_JASPER_PATH = "INBUILDING_ONEE_JASPER_PATH";
	String VOLTE_LATENCY = "VOLTE_LATENCY";

	String INBUILDING_TASKNAME_BUILDING_REPORT = "Ibs Building Report";
	String PCI_LIST = "PCI_LIST";
	String MIN_LATENCY_LIST = "MIN_LATENCY_LIST";


	// new constant for Nokia report
	String PA_POWER = "PA_POWER";
	String PB_POWER = "PB_POWER";
	String TCP_DL = "TCP_DL";
	String RA_UPDATION = "RA_UPDATION";
	String INTER_SITE_HO = "INTER_SITE_HO";
	String INTRA_SITE_HO = "INTRA_SITE_HO";
	String UE_CAPABILITY_RAT_CONTAINER = "UE_CAPABILITY_RAT_CONTAINER";
	String CSFB_MO_CALL_SETUP_TIME = "CSFB_MO_CALL_SETUP_TIME";
	String CSFB_MT_CALL_SETUP_TIME = "CSFB_MT_CALL_SETUP_TIME";
	String CSFB_CALL_ATTEMPT = "CSFB_CALL_ATTEMPT";
	String CSFB_MTCALL_ATTEMPT = "CSFB_MTCALL_ATTEMPT";
	String CSFB_MOCALL_ATTEMPT = "CSFB_MOCALL_ATTEMPT";
	String CSFB_CALL_SETUP_SUCCESS = "CSFB_CALL_SETUP_SUCCESS";
	String CSFB_MOCALL_SETUP_SUCCESS = "CSFB_MOCALL_SETUP_SUCCESS";
	String CSFB_MTCALL_SETUP_SUCCESS = "CSFB_MTCALL_SETUP_SUCCESS";
	String CSFB_CALL_SUCCESS = "CSFB_CALL_SUCCESS";
	String CSFB_MTCALL_SUCCESS = "CSFB_MTCALL_SUCCESS";
	String CSFB_MOCALL_SUCCESS = "CSFB_MOCALL_SUCCESS";
	String CSFB_CALL_SETUP_TIME = "CSFB_CALL_SETUP_TIME";
	String CSFB_CALL_DROP = "CSFB_CALL_DROP";
	String CSFB_MTCALL_DROP = "CSFB_MTCALL_DROP";
	String CSFB_MOCALL_DROP = "CSFB_MOCALL_DROP";
	String CSFB_CALL_FAIL = "CSFB_CALL_FAIL";
	String CSFB_MTCALL_FAIL = "CSFB_MTCALL_FAIL";
	String CSFB_MOCALL_FAIL = "CSFB_MOCALL_FAIL";
	String CSFB_CAUSE = "CSFB_CAUSE";
	String LA_UPDATION = "LA_UPDATION";
	String FAST_RETURN_TIME = "FAST_RETURN_TIME";
	
	
	
		
	
	
	
	
	
	
	String SSVT_EXCEL_REPORT_JASPER_PATH = "SSVT_EXCEL_REPORT_JASPER_PATH";
	String DATE_FORMAT_DD_MMM_YY_HH_MM_SSSSS = "ddMMMyyHHmmssSSS";
	String VOLTE_MT_CALL_ATTEMPT = "VOLTE_MT_CALL_ATTEMPT";
	String VOLTE_MT_CALL_SUCCESS = "VOLTE_MT_CALL_SUCCESS";
	String VOLTE_MT_CALL_FAIL = "VOLTE_MT_CALL_FAIL";
	String VOLTE_MT_CALL_DROP = "VOLTE_MT_CALL_DROP";
	String VOLTE_MT_CALL_SETUP_SUCCESS = "VOLTE_MT_CALL_SETUP_SUCCESS";
	String VOLTE_MO_CALL_ATTEMPT = "VOLTE_MO_CALL_ATTEMPT";
	String VOLTE_MO_CALL_SUCCESS = "VOLTE_MO_CALL_SUCCESS";
	String VOLTE_MO_CALL_FAIL = "VOLTE_MO_CALL_FAIL";
	String VOLTE_MO_CALL_DROP = "VOLTE_MO_CALL_DROP";
	String VOLTE_MO_CALL_SETUP_SUCCESS = "VOLTE_MO_CALL_SETUP_SUCCESS";
	String VOLTE_MT_CALL_SETUP = "VOLTE_MT_CALL_SETUP";
	String VOLTE_MO_CALL_SETUP = "VOLTE_MO_CALL_SETUP";
	String VOLTE_MT_CALL_SETUP_TIME = "VOLTE_MT_CALL_SETUP_TIME";
	String VOLTE_MO_CALL_SETUP_SUCCESS_TIME = "VOLTE_MO_CALL_SETUP_SUCCESS_TIME";
	String VOLTE_MO_CALL_SETUP_TIME = "VOLTE_MO_CALL_SETUP_TIME";
	String VOLTE_MT_CALL_SETUP_SUCCESS_TIME = "VOLTE_MT_CALL_SETUP_SUCCESS_TIME";
	String INTRA_HANDOVER_INITIATE = "INTRA_HANDOVER_INITIATE";
	String INTRA_HANDOVER_SUCCESS = "INTRA_HANDOVER_SUCCESS";
	String INTRA_HANDOVER_FAIL = "INTRA_HANDOVER_FAIL";
	String INTER_HANDOVER_INITIATE = "INTER_HANDOVER_INITIATE";
	String INTER_HANDOVER_SUCCESS = "INTER_HANDOVER_SUCCESS";
	String INTER_HANDOVER_FAIL = "INTER_HANDOVER_FAIL";
	String MUTECALL = "MUTECALL";
	String SRVCC_SUCCESS = "SRVCC_SUCCESS";
	String SRVCC_FAIL = "SRVCC_FAIL";
	String SRVCC_TIME = "SRVCC_TIME";
	String SESSION_ESTABLISHMENT_TIME = "SESSION_ESTABLISHMENT_TIME";
	String SRVCC_ATTEMPT = "SRVCC_ATTEMPT";

	String STEALTH_DASHBOARD_REPORT_JASPER_FOLDER_PATH = "STEALTH_DASHBOARD_REPORT_JASPER_FOLDER_PATH";
	String RB_ALLOCATION = "RB_ALLOCATION";

	String EVENT_TYPE = "EVENT_TYPE";
	String LAC = "LAC";
	String FAST_RETURN_PRIORITY = "FAST_RETURN_PRIORITY";
	String RRC_CONNECTION_RELESE412F = "RRC_CONNECTION_RELESE412F";
	String LTE_IRAT_HANDOVER = "LTE_IRAT_HANDOVER";
	String DETACH_TIME = "DETACH_TIME";
	String UL_PRB = "UL_PRB";
	String DL_PRB = "DL_PRB";
	String INTRA_FREQ_CELL_RESELECTION_PRIORITY = "INTRA_FREQ_CELL_RESELECTION_PRIORITY";
	String TDD_TO_FDD_RESELECTION_TIME = "TDD_TO_FDD_RESELECTION_TIME";
	String FDD_TO_TDD_RESELECTION_TIME = "FDD_TO_TDD_RESELECTION_TIME";
	String CELL_RESELECTION_LTE_TO_3G_TIME = "CELL_RESELECTION_LTE_TO_3G_TIME";
	String HO_TDD_TO_FDD = "HO_TDD_TO_FDD";
	String HO_FDD_TO_TDD = "HO_FDD_TO_TDD";
	String CELL_RESELECTION_2G_TO_LTE_TIME = "CELL_RESELECTION_2G_TO_LTE_TIME";
	String CELL_RESELECTION_3G_TO_LTE_TIME = "CELL_RESELECTION_3G_TO_LTE_TIME";

	int BEARING_ANGLE_FOR_DOWN_DIRECTION = 180;
	int BEARING_ANGLE_FOR_RIGHT_DIRECTION = 90;
	String CQI_CW0_MAX = "cqicw0max";
	String CQI_CW1_MAX = "cqicw1max";
	String DL_PRB_MAX = "DL_PRB_MAX";
	String UL_PRB_MAX = "UL_PRB_MAX";
	String DL_MCS_MAX = "DL_MCS_MAX";
	String UL_MCS_MAX = "UL_MCS_MAX";
	String VIDEO_OVER_VOLTE_CALL_SUCCESS = "VIDEO_OVER_VOLTE_CALL_SUCCESS";
	String VIDEO_OVER_VOLTE_CALL_INITIATE = "VIDEO_OVER_VOLTE_CALL_INITIATE";
	String VIDEO_CALL_CONF_SUCCESS = "VIDEO_CALL_CONF_SUCCESS";
	String VIDEO_CALL_CONF_INITIATE = "VIDEO_CALL_CONF_INITIATE";
	String SCELL2_PDSCH_THROUPUT = "SCELL2_PDSCH_THROUPUT";
	String SCELL1_PDSCH_THROUPUT = "SCELL1_PDSCH_THROUPUT";
	String LAYER3_EVENT_TABLE = "LAYER3_EVENT_TABLE";
	String DECODED_MESSAGE = "DECODED_MESSAGE";
	String INTER_FREQ_CELL_RESELECTION_PRIORITY = "INTER_FREQ_CELL_RESELECTION_PRIORITY";
	String IRAT_WCDMA_CELL_RESELECTION_PRIORITY = "IRAT_WCDMA_CELL_RESELECTION_PRIORITY";
	String IRAT_GSM_CELL_RESELECTION_PRIORITY = "IRAT_GSM_CELL_RESELECTION_PRIORITY";
	String RXLEVEL = "RXLEVEL";
	String RXQUAL = "RXQUAL";
	String WROST_CI = "WROST_CI";
	String GSM_CELLID = "GSM_CELLID";
	String SSVT_2G_REPORT_JASPER_PATH = "SSVT_2G_REPORT_JASPER_PATH";
	String CELL_RESELECTION_LTE_TO_2G_TIME = "CELL_RESELECTION_LTE_TO_2G_TIME";
    String KEY_BAND = "band";
	String DOWNLOAD= "DOWNLOAD";
	String PCELL_PDSCH_THROUPUT = "PCELL_PDSCH_THROUPUT";
	
	String SCFT_ST_NK_Combine ="SCFT Combine ST NK 200 ms";
	String FTP_UL_NK_200ms ="FTP UL NK 200ms";
	String FTP_DL_NK_200ms ="FTP DL NK 200ms";
	String SCFT_IDLE_200ms ="SCFT IDLE 200ms";
	String SCFT_IDLE_AT_DT ="SCFT IDLE AT DT";
	String SCFT_CSFB_CALL_MO_200ms = "SCFT CSFB CALL MO 200ms";
	String SCFT_CSFB_MT_CALL_200ms = "SCFT CSFB MT CALL 200ms";
	String FTP_DL_CA_200ms ="FTP DL CA 200ms";
	String ALL_RECIPE = "ALL_RECIPE";
	String VOLTE_MT_CALL_CONNECTED_IDLE_MODE_200MS = "VoLTE MT Call Connected IDLE mode 200ms";
	String ViLTE_AND_CONFERENCE_SHORT_CALL_200MS = "VILTE and Conference Short Call 200ms";
	String VOLTE_LONG_CALL = "Volte Long call";
	String LONG_DRIVE_MT_DR_200MS = "Long Drive MT DR 200ms";
	String LONG_CALL_MO_DR_200MS = "Long Call MO DR 200ms";
}

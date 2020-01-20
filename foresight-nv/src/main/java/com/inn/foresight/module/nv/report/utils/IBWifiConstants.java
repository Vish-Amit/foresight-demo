package com.inn.foresight.module.nv.report.utils;

public final class IBWifiConstants {

	private IBWifiConstants() {
		// Empty Constructor to Prevent Instantiation
	}

	public static final String KEY_FLOOR_PLAN_IMAGE = "floorPlanImage";
	public static final String KEY_SURVEY_PATH_IMAGE = "surveyPathImage";
	public static final String KEY_WIFI_RSSI_IMAGE = "WIFI_RSSI";
	public static final String KEY_WIFI_SNR_IMAGE = "WIFI_SNR";
	public static final String KEY_WIFI_DL_2_GHZ = "WIFI_DL_2GHZ";
	public static final String KEY_WIFI_DL_5_GHZ = "WIFI_DL_5GHZ";
	public static final String KEY_WIFI_UL_2_GHZ = "WIFI_UL_2GHZ";
	public static final String KEY_WIFI_UL_5_GHZ = "WIFI_UL_5GHZ";
	public static final String REPORT_PARAM_KEY_SURVEY_PATH_IMAGE = "surveyPathImg";
	public static final String REPORT_PARAM_KEY_OVERALL_SIGNAL_IMAGE = "overallSignalCoverageImg";
	public static final String REPORT_PARAM_KEY_OVERALL_SIGNAL_LEGEND = "overallSignalCoverageLegend";
	public static final String REPORT_PARAM_KEY_SNR_IMAGE = "snrImg";
	public static final String REPORT_PARAM_KEY_SNR_LEGEND = "snrLegend";
	public static final String REPORT_PARAM_KEY_DL_IMAGE = "dlThroughputImg";
	public static final String REPORT_PARAM_KEY_DL_LEGEND = "dlThroughputLegend";
	public static final String REPORT_PARAM_KEY_UL_IMAGE = "ulThroughputImg";
	public static final String REPORT_PARAM_KEY_UL_LEGEND = "ulThroughputLegend";
	public static final String WIFI_RSSI = "WIFI_RSSI";
	public static final String WIFI_SNR = "WIFI_SNR";
	public static final String KEY_LEGEND_INBUILDING_WIFI = "INBUILDING WIFI";

	public static final String ITEM_LABEL_WORKORDER_NAME = "Workorder Name";
	public static final String ITEM_LABEL_RECIPE_NAME = "Recipe Name";
	public static final String ITEM_LABEL_DATE = "Date";
	public static final String ITEM_LABEL_BUILDING_NAME = "Building Name";
	public static final String ITEM_LABEL_WING_NAME = "Wing Name";
	public static final String ITEM_LABEL_FLOOR_NUMBER = "Floor Number";
	public static final String ITEM_LABEL_UNIT_NAME = "Room Name";

	public static final String DESIRED_SIGNAL_THRESHOLD = "-67";
	public static final String MIN_SNR_THRESHOLD = "25";
	public static final String MIN_DOWNLOAD_THRESHOLD = "0.6";
	public static final String MIN_UPLOAD_THRESHOLD = "0.6";
	public static final String MIN_PHY_DATA_RATE_THRESHOLD = "5.5";

	public static final Integer INDEX_DETAIL_WIFI_RSSI = 41;
	public static final Integer INDEX_DETAIL_WIFI_SNR = 42;
	public static final Integer INDEX_DETAIL_WIFI_SSID = 43;
	public static final Integer INDEX_DETAIL_WIFI_BSSID = 98;
	public static final Integer INDEX_DETAIL_WIFI_CHANNEL = 99;
	public static final Integer INDEX_DETAIL_WIFI_LINK_SPEED = 100;
	public static final Integer INDEX_DETAIL_WIFI_BAND = 101;
	public static final Integer INDEX_DETAIL_ST_PIN_NUMBER = 107;
	public static final Integer INDEX_DETAIL_ST_DL_RATE = 108;
	public static final Integer INDEX_DETAIL_ST_UL_RATE = 109;
	public static final Integer INDEX_DETAIL_DL_TIME_GOOGLE = 110;
	public static final Integer INDEX_DETAIL_DL_TIME_FACEBOOK = 111;
	public static final Integer INDEX_DETAIL_DL_TIME_YOUTUBE = 112;
	public static final Integer INDEX_DETAIL_ST_JITTER = 39;
	public static final Integer INDEX_DETAIL_ST_LATENCY = 23;
	public static final Integer INDEX_DETAIL_ST_PACKET_LOSS = 54;

	public static final Double OVERALL_SIGNAL_PERCENTAGE_THRESHOLD = 95.0;
	public static final Double SNR_PERCENTAGE_THRESHOLD = 100.0;
	public static final Double DL_PERCENTAGE_THRESHOLD = 100.0;
	public static final Double UL_PERCENTAGE_THRESHOLD = 100.0;

	public static final String RESULT_TEXT_PASS = "Pass";
	public static final String RESULT_TEXT_FAIL = "Fail";

	public static final String IB_WIFI_REPORT_NAME_PREFIX = "IB_WIFI_REPORT_";
	public static final String IB_WIFI_GENERATED_REPORT_PATH = "IB_WIFI_GENERATED_REPORT_PATH";
	public static final String IB_WIFI_JASPER_FILE_PATH = "IB_WIFI_JASPER_FILE_PATH";
	public static final String IB_WIFI_MAIN_JASPER_FILE_NAME = "a_survey_report.jasper";

	public static final String REPORT_PARAM_KEY_HEADER_BG = "header-bg";
	public static final String REPORT_PARAM_KEY_HEADER_LOGO = "header-logo";
	public static final String REPORT_PARAM_KEY_FOOTER_IMAGE = "footer-img";
	public static final String REPORT_PARAM_KEY_BEAN_LIST_SIZE = "beanListSize";

	public static final String IMAGE_NAME_HEADER_BG = "header-bg.png";
	public static final String IMAGE_NAME_HEADER_LOGO = "header-logo.png";
	public static final String IMAGE_NAME_FOOTER_IMAGE = "footer-img.png";

	public static final String IB_WIFI_OPERATOR_NAME = "WIFI";

	public static final String WIFI_BAND_VALUE_2_4 = "2.4";
	public static final String WIFI_BAND_VALUE_5 = "5";

	public static final Integer LIST_INDEX_ZERO = 0;
	public static final String KEY_SPEED_TEST_IMAGE = "speedTestImage";

}

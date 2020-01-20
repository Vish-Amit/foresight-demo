package com.inn.foresight.module.nv;

import com.ibm.icu.text.SimpleDateFormat;

public class NVConstant {
	
	private NVConstant() {
		
	}
	public static final String CGI_KEY="cgi";
	public static final String PCI_KEY="pci";
	public static final String MODULE_NAME = "Net Velocity";
	public static final String GET_DEVICEINFO_BY_APKID_AND_IMEI = "getDeviceInfoByApkIdAndImei";
	public static final String GET_DEVICEINFO_BY_DEVICE_ID_LIST = "getDeviceInfoListByDeviceIds";
	public static final String DEVICE_ID_LIST="deviceIdList";
	public static final String MSISDNLIST="msisdnList";
	public static final String ID_KEY = "id";
	public static final String PRESET_ID = "presetId";
	public static final String HOURLY = "HOURLY";
	public static final String WO_FREQUENCY="woFrequency";
	public static final Integer STATUS_CODE_200 = 200;
	public static final String CONTENT_TYPE= "Content-Type";
	public static final String CONTENT_DISPOSITION="Content-Disposition";
	public static final String ATTACHMENT_FILE_NAME="attachment;filename=";
	public static final String REPORT_NOT_FOUND_JSON="{\"result\":\"Report Not Found for json \"}";
	public static final String DATA_NOT_FOUND = "{\"message\":\"Data Not Found.\"}";
	public static final Object INVALID_PARAM = "{\"message\\\":\"Invalid Parameter.\"}";
	public static final String CRC_EXTENSION=".crc";
	public static final String KEY_TECHNOLOGY="technology";
	public static final String RECIPE_CUSTOM_GEOGRAPHYMAP="recipeCustomGeographyMap";
	public static final Double DOUBLE_HUNDRED = 100.0;

	public static final Integer MAX_PARTITION_LIMIT = 900;
	public static final Double DOUBLE_THOUSAND=1000.0;
	public static final int INT_THOUSAND=1000;

	
	public static final String FREQUENCY_ONETIME ="ONETIME";
	public static final String FREQUENCY_DAILY ="DAILY";
	public static final String FREQUENCY_WEEKLY ="WEEKLY";
	

	public static final int MINUS_ONE_INT = -1;
	public static final int ZERO_INT = 0;
	public static final int ONE_INT = 1;
	public static final int TWO_INT = 2;
	public static final int THREE_INT = 3;
	public static final int FOUR_INT = 4;
	public static final int FIVE_INT = 5;
	public static final int SIX_INT = 6;
	public static final int EIGHT=8;
	public static final int SIXTEEN=16;
	
	public static final int THIRTY=30;
	public static final int TWO_HUNDRED=200;
	public static final int TEN_INT = 10;
	public static final int NEGATIVE_TEN=-10;
	public static final String FS_API_USERNAME = "FS_API_USERNAME";
	public static final String FS_API_CHECKSUM = "FS_API_PASSWORD";
	public static final String GET_BCC_EMAILIDS = "GET_BCC_EMAILIDS";
	public static final String GET_CHANGEPARAMETER_URL = "GET_CHANGEPARAMETER_URL";
	public static final String GET_METHOD="GET";
	public static final String GET_SITE_ALARM="/AlarmLayer/isSiteHaveAlarms?queryCriteria=";
	public static final String GET_SITE_ALARM_OLD="/AlarmLayer/isSiteHaveAlarm?categories=OUTAGE";

	public static final String POST_METHOD="POST";
	public static final String SSVT_QUICK_SLA_TIME_DURATION = "SSVT_QUICK_SLA_TIME_DURATION";
	public static final String SSVT_FULL_SLA_TIME_DURATION = "SSVT_FULL_SLA_TIME_DURATION";
	public static final int ONE_KB = 1024;
	public static final int FOUR_KB = 4096;
	public static final String WO_RECIPE_ID = "woRecipeId";
	public static final String LAYER3_EXECUTION_KEY= "isLayer3Execution";
	public static final SimpleDateFormat DATE_FORMAT_WW_YYYY = new SimpleDateFormat("wwYYYY");
	public static final String NV_SSVT_IBC_WALKTEST = "NV_SSVT_IBC_WALKTEST";
	public static final int HUNDRED=100;
	public static final String KEY_PASSIVE="PASSIVE";
	public static final String ONDEMAND_KPI_COUNTER_API="/rest/OnDemandKPINotificationRest/getOnDemandkpisForCell";
	public static final String GET_GEOGRAPHY_WISE_ALARM="/AlarmLayer/getOutageEffectedSiteCount?";
	public static final String LAYER3_IS_PROBE="isProbe";
	public static final String BENCHMARK_DEFAULT_OPERATOR="BENCHMARK_DEFAULT_OPERATOR";
	public static final String INBUILDING_ATP_1C="INBUILDING ATP 1C";
	public static final String INBUILDING_ATP_1E = "INBUILDING ATP 1E";
	public static final String ODSC_ATP_1C="ODSC ATP 1C";
	public static final String ODSC_ATP_1E = "ODSC ATP 1E";
	public static final String NE_LAT= "NELat";
	public static final String NE_LNG= "NELng";
	public static final String SW_LAT= "SWLat";
	public static final String SW_LNG= "SWLng";
	public static final String GEOGRAPHY_LEVEL= "geographyLevel";
	
	public static final String NV_SITE_SUGGESTION_HDFS_BASE_FILE_PATH = "NV_SITE_SUGGESTION_HDFS_BASE_FILE_PATH";
	public static final String GWO_META_FILTER = "GWOMetaFilter";
}

package com.inn.foresight.module.nv.workorder.stealth.constants;

import org.apache.hadoop.hbase.util.Bytes;

/** The Class StealthConstants. */
public class StealthConstants {

	/** The Enum WOTestStatus. */
	public enum WOTestStatus {

		/** The inprogress. */
		INPROGRESS,
		/** The synced. */
		SYNCED,
		/** The processed. */
		PROCESSED,
		
		/** The stopped. */
		STOPPED,
		
		/** The closed. */
		CLOSED,
		
		/** The completed. */
		COMPLETED,
	
		EXPIRE,
		
		FAILURE
	}

	/** The Enum Acknowledgement. */
	public enum Acknowledgement {
		
		/** The pending. */
		PENDING,
/** The accept. */
ACCEPT,
/** The reject. */
REJECT
	}
	
	/** The Enum WOTestResult. */
	public enum WOTestResult {

		/** The success. */
		SUCCESS,
		/** The failure. */
		FAILURE
	}
	
	/** The Enum Status. */
	public enum Status {
		
		/** The inprogress. */
		INPROGRESS,
		
		/** The success. */
		SUCCESS,
		
		/** The fail. */
		FAIL
	}
	
	/** The Enum AssigmentType. */
	public enum AssigmentType{
		
		/** The assign to user. */
		ASSIGN_TO_USER,
		
		/** The assign to device. */
		ASSIGN_TO_DEVICE,
		
		/** The geographical area. */
		GEOGRAPHICAL_AREA
	}
	
	/** The Constant NORTH_EAST_LAT. */
	public static final String NORTH_EAST_LAT = "NELat";

	/** The Constant NORTH_EAST_LONG. */
	public static final String NORTH_EAST_LONG = "NELng";

	/** The Constant SOUTH_WEST_LAT. */
	public static final String SOUTH_WEST_LAT = "SWLat";

	/** The Constant SOUTH_WEST_LONG. */
	public static final String SOUTH_WEST_LONG = "SWLng";

	/** The Constant ZOOMLEVEL. */
	public static final String ZOOMLEVEL = "zoomLevel";

	/** The invalid parameter json. */
	public static final String INVALID_PARAMETER_JSON = "{\"message\":\"Parameters given are invalid.\"}";

	/** The failure json. */
	public static final String FAILURE_JSON = "{\"message\":\"Failure\"}";
	
	/** The success json. */
	public static final String SUCCESS_JSON = "{\"message\":\"Success\"}";


	/** The Constant DEVICE ID. */
	public static final String DEVICE_ID = "deviceId";

	/** The Constant TEST_START_TIME. */
	public static final String TEST_START_TIME = "testStartTime";
	
	/** The Constant TEST_END_TIME. */
	public static final String TEST_END_TIME = "testEndTime";
	
	/** The Constant TEST_ID. */
	public static final String TEST_ID = "testId";
	
	/** The Constant DEVICE GROUP ID. */
	public static final String DEVICE_GROUP_ID = "deviceGroupId";

	/** The Constant WORKORDER RECIPE MAPPING ID. */
	public static final String WORKORDER_RECIPE_MAPPING_ID = "worecipeId";

	/** The Constant GET_BUILDING_BY_VIEWPORT. */
	public static final String GET_DEVICE_GROUP_BY_VIEWPORT = "getDeviceGroupByViewPort";

	/** The Constant GET_BUILDING_COUNT_GROUP_BY_GEOL1. */
	public static final String GET_DEVICE_GROUP_COUNT_GROUP_BY_GEOL1 = "getDeviceGroupCountGroupByGeol1";

	/** The Constant GET_BUILDING_COUNT_GROUP_BY_GEOL2. */
	public static final String GET_DEVICE_GROUP_COUNT_GROUP_BY_GEOL2 = "getDeviceGroupCountGroupByGeol2";

	/** The Constant GET_BUILDING_COUNT_GROUP_BY_GEOL3. */
	public static final String GET_DEVICE_GROUP_COUNT_GROUP_BY_GEOL3 = "getDeviceGroupCountGroupByGeol3";

	/** The Constant GET_BUILDING_COUNT_GROUP_BY_GEOL4. */
	public static final String GET_DEVICE_GROUP_COUNT_GROUP_BY_GEOL4 = "getDeviceGroupCountGroupByGeol4";

	/** The Constant GEOGRAPHY_L1_ZOOM. */
	public static final String GEOGRAPHY_L1_ZOOM = "GeographyL1Zoom";

	/** The Constant GEOGRAPHY_L2_ZOOM. */
	public static final String GEOGRAPHY_L2_ZOOM = "GeographyL2Zoom";

	/** The Constant GEOGRAPHY_L3_ZOOM. */
	public static final String GEOGRAPHY_L3_ZOOM = "GeographyL3Zoom";

	/** The Constant GEOGRAPHY_L4_ZOOM. */
	public static final String GEOGRAPHY_L4_ZOOM = "GeographyL4Zoom";

	/** The Constant DEVICE_GROUP_ZOOM. */
	public static final String DEVICE_GROUP_ZOOM = "DeviceGroupZoom";

	/** The Constant GET_DEVICE_DATA_BY_KPI_AND_THRESHOLD_BETWEEN_STARTDATE_AND_ENDDATE. */
	public static final String GET_DEVICE_DATA_BETWEEN_STARTDATE_AND_ENDDATE = "getDeviceDataBetweenStartDateAndEndDate";

	/** The Constant START_DATE. */
	public static final String START_DATE = "startDate";

	/** The Constant END_DATE. */
	public static final String END_DATE = "endDate";

	/** The Constant KPI. */
	public static final String KPI = "kpi";

	/** The Constant THRESHOLD. */
	public static final String THRESHOLD = "threshold";

	/** The Constant RESULT_TYPE. */
	public static final String RESULT_TYPE = "resultType";

	/** The Constant LLIMIT. */
	public static final String LLIMIT = "lLimit";

	/** The Constant ULIMIT. */
	public static final String ULIMIT = "uLimit";

	/** The Constant GET_FAILURE_DEVICE_DATA_BETWEEN_STARTDATE_AND_ENDDATE. */
	public static final String GET_FAILURE_DEVICE_DATA_BETWEEN_STARTDATE_AND_ENDDATE = "getFailureDeviceDataBetweenStartDateAndEndDate";

	/** The Constant DATATYPE. */
	public static final String DATATYPE = "dataType";

	/** The Constant FILTER_NAME. */
	public static final String FILTER_NAME = "filterName";

	/** The Constant FILTER_PARAM. */
	public static final String FILTER_PARAM = "filterParam";

	/** The Constant FILTER_VALUE. */
	public static final String FILTER_VALUE = "filterValue";

	/** The Constant TTFB_FILTER. */
	public static final String TTFB_FILTER = "TTFBFilter";

	/** The Constant TTL_FILTER. */
	public static final String TTL_FILTER = "TTLFilter";

	/** The Constant TDNS_FILTER. */
	public static final String TDNS_FILTER = "TDNSFilter";

	/** The Constant FDNS_FILTER. */
	public static final String FDNS_FILTER = "FDNSFilter";

	/** The Constant FAILURE_FILTER. */
	public static final String FAILURE_FILTER = "failureFilter";

	/** The Constant TTFB. */
	public static final String TTFB = "TTFB";

	/** The Constant TTL. */
	public static final String TTL = "TTL";

	/** The Constant TDNS. */
	public static final String TDNS = "TDNS";

	/** The Constant FDNS. */
	public static final String FDNS = "FDNS";

	/** The Constant FAILURE. */
	public static final String FAILURE = "FAILURE";

	/** The Constant IS_DEVICEWISE. */
	public static final String IS_DEVICEWISE = "isDeviceWise";

	/** The Constant TTFB_DEVICE_WISE_FILTER. */
	public static final String TTFB_DEVICE_WISE_BAD_FILTER = "TTFBDevicewiseBadFilter";

	/** The Constant TDNS_DEVICE_WISE_FILTER. */
	public static final String TDNS_DEVICE_WISE_BAD_FILTER = "TDNSDevicewiseBadFilter";

	/** The Constant TTL_DEVICE_WISE_FILTER. */
	public static final String TTL_DEVICE_WISE_BAD_FILTER = "TTLDevicewiseBadFilter";

	/** The Constant FDNS_DEVICE_WISE_FILTER. */
	public static final String FDNS_DEVICE_WISE_BAD_FILTER = "FDNSDevicewiseBadFilter";

	/** The Constant IS_FAILURE_DEVICE. */
	public static final String IS_FAILURE_DEVICE = "IS_FAILURE_DEVICE";

	/** The Constant ID. */
	public static final String ID = "id";

	/** The Constant GET_DEVICE_DATA_FOR_DAYWISE. */
	public static final String GET_DEVICE_DATA_FOR_DAYWISE = "GetDeviceDataForDaywise";

	/** The Constant GET_FAILURE_DEVICE_DATA_FOR_DAYWISE. */
	public static final String GET_FAILURE_DEVICE_DATA_FOR_DAYWISE = "GetFailureDeviceDataForDaywise";
	

	/** The Constant IP_VERSION_4. */
	public static final String IP_VERSION_4 = "IPV4";

	/** The Constant IP_VERSION_6. */
	public static final String IP_VERSION_6 = "IPV6";

		/** The Constant WPT_DUMP_REPORT_HEADER. */
	public static final String WPT_DUMP_REPORT_HEADER = "Test performed,Network Type,Date,Test Start Time,Test End Time,Web URL,Iteration,First DNS Resolution Time (ms),Total DNS Resolution Time (ms),Time to First Byte (ms),Total Time to Load (Sec),Target IP,External IP,City,No. of redirections,Redirected URL flow,Route Holder List,Total number of hops,Target IP Version,IP Version,IPV4 List,IPV6 List,Circle,City,Latitude,Longitude,Operator Name,IMSI,IMEI,Manufacturer,Model,Operating System,Location Type,Remark,GPS Status,charger Connected Status,batteryLevel,Voltage,Temperature,Page Size(KB),Baseband,Build Number,Auto Date&Time,MAC,Probe ID,MCC,MNC,Cell ID,PCI,PSC,TAC,LAC,Avg RSRP,Avg SINR,Avg RSRQ,Avg RSSI,Avg RSCP,Avg EC/NO,Avg Rx Level,Avg Rx Quality";
	
	/** The Constant PAYLOAD_FOR_DEVICE_UPDATE_NOTIFICATION. */
	public static final String PAYLOAD_FOR_DEVICE_UPDATE_NOTIFICATION = "{\"message\": \"Hi, Please provide updated status of device.\", \"moduleName\": \"PULSE\"}";

	/** The Constant PAYLOAD_FOR_PULSE. */
	public static final String PAYLOAD_FOR_PULSE = "{\"message\": \"Hi! A new workorder of Open Drive has been assigned to you.\", \"moduleName\": \"PULSE\"}";

	/** The Constant ALL. */
	public static final String ALL = "all";

	/** The Constant BAD. */
	public static final String BAD = "bad";

	/** The Constant GOOD. */
	public static final String GOOD = "good";

	/** The Constant TTFB_DEVICE_WISE_DATA_FILTER. */
	public static final String TTFB_DEVICE_WISE_GOOD_FILTER = "TTFBDevicewiseGoodFilter";

	/** The Constant TTL_DEVICE_WISE_DATA_FILTER. */
	public static final String TTL_DEVICE_WISE_GOOD_FILTER = "TTLDevicewiseGoodFilter";

	/** The Constant TDNS_DEVICE_WISE_DATA_FILTER. */
	public static final String TDNS_DEVICE_WISE_GOOD_FILTER = "TDNSDevicewiseGoodFilter";

	/** The Constant FDNS_DEVICE_WISE_DATA_FILTER. */
	public static final String FDNS_DEVICE_WISE_GOOD_FILTER = "FDNSDevicewiseGoodFilter";

	/** The Constant TTFB_DAY_WISE_FILTER. */
	public static final String TTFB_DAY_WISE_BAD_FILTER = "TTFBDaywiseBadFilter";

	/** The Constant TTL_DAY_WISE_FILTER. */
	public static final String TTL_DAY_WISE_BAD_FILTER = "TTLDaywiseBadFilter";

	/** The Constant TDNS_DAY_WISE_FILTER. */
	public static final String TDNS_DAY_WISE_BAD_FILTER = "TDNSDaywiseBadFilter";

	/** The Constant FDNS_DAY_WISE_FILTER. */
	public static final String FDNS_DAY_WISE_BAD_FILTER = "FDNSDaywiseBadFilter";

	/** The Constant TTFB_DAY_WISE_DATA_FILTER. */
	public static final String TTFB_DAY_WISE_GOOD_FILTER = "TTFBDaywiseGoodFilter";

	/** The Constant TTL_DAY_WISE_DATA_FILTER. */
	public static final String TTL_DAY_WISE_GOOD_FILTER = "TTLDaywiseGoodFilter";

	/** The Constant TDNS_DAY_WISE_DATA_FILTER. */
	public static final String TDNS_DAY_WISE_GOOD_FILTER = "TDNSDaywiseGoodFilter";

	/** The Constant FDNS_DAY_WISE_DATA_FILTER. */
	public static final String FDNS_DAY_WISE_GOOD_FILTER = "FDNSDaywiseGoodFilter";
	
	/** The Constant GET_FAILURE_DEVICE_REMARK. */
	public static final String GET_FAILURE_DEVICE_REMARK = "getFailureDeviceRemark";

	/** The Constant GET_DEVICE_ID_PK_BY_STATUS. */
	public static final String GET_NON_RESPONDING_DEVICES = "getNonRespondDevices";

	/** The Constant DEVICE_NOT_RESPONDED. */
	public static final String DEVICE_NOT_RESPONDED = "Device Not Responed";

	/** The Constant WPT_DUMP_REPORT_FILE_PATH. */
	public static final String WPT_DUMP_REPORT_FILE_PATH = "WPT_DUMP_REPORT_FILE_PATH";

	/** The Constant PULSE. */
	public static final String PULSE = "PULSE";

	/** The Constant GET_DEVICE_DATA_COUNT_BETWEEN_STARTDATE_AND_ENDDATE. */
	public static final String GET_DEVICE_DATA_COUNT_BETWEEN_STARTDATE_AND_ENDDATE = "getDeviceDataCountBetweenStartDateAndEndDate";

	/** The Constant GET_FAILURE_DEVICE_DATA_COUNT_BETWEEN_STARTDATE_AND_ENDDATE. */
	public static final String GET_FAILURE_DEVICE_DATA_COUNT_BETWEEN_STARTDATE_AND_ENDDATE = "getFailureDeviceDataCountBetweenStartDateAndEndDate";

	/** The Constant GET_DEVICE_DATA_COUNT_DAYWISE. */
	public static final String GET_DEVICE_DATA_COUNT_DAYWISE = "getDeviceDataCountDayWise";

	/** The Constant GET_FAILURE_DEVICE_DATA_COUNT_DAYWISE. */
	public static final String GET_FAILURE_DEVICE_DATA_COUNT_DAYWISE = "getFailureDeviceDataCountDayWise";

	/** The Constant GET_CEMS_DATA_BY_STEALTH_TASK_RESULT. */
	public static final String GET_CEMS_DATA_BY_STEALTH_TASK_RESULT = "getCemDataByStealthTaskResult";
	
	/** The Constant JSON. */
	public static final String JSON = "json";

	/** The Constant ITERATION. */
	public static final String ITERATION = "iteration";

	/** The Constant LIST_ITEM. */
	public static final String LIST_ITEM = "listItem";

	/** The Constant URL_LIST. */
	public static final String URL_LIST = "urlList";

	/** The Constant INTERVAL_TIME. */
	public static final String INTERVAL_TIME = "intervalTime";

	/** The Constant DURATION. */
	public static final String DURATION = "duration";

	/** The Constant BROWSE_TEST. */
	public static final String WEB_TEST = "WEB TEST";

	/** The Constant VIDEO_TEST. */
	public static final String YOUTUBE_TEST = "YOUTUBE TEST";

	/** The Constant DL. */
	public static final String DL = "DL";

	/** The Constant UL. */
	public static final String UL = "UL";

	/** The Constant TWO_MINUTE_DELAY_FOR_EACH_URL. */
	public static final int TWO_MINUTE_DELAY_FOR_EACH_URL = 120;

	/** The Constant CONNECTION_ESTABLISHMENT_TIME. */
	public static final int CONNECTION_ESTABLISHMENT_TIME = 30;

	/** The Constant BUFFERING_TIME. */
	public static final int BUFFERING_TIME =2;

	/** The Constant IMEI. */
	public static final String IMEI = "imei";

	/** The Constant START_TIME. */
	public static final String START_TIME = "startTime";

	/** The Constant END_TIME. */
	public static final String END_TIME = "endTime";
	
	/** The Constant TASK_ID. */
	public static final String TASK_ID = "taskId";

	/** The Constant YOU_TUBE_CSV_REPORT_FILE_PATH. */
	public static final String YOU_TUBE_CSV_REPORT_FILE_PATH = "YOU_TUBE_CSV_REPORT_FILE_PATH";

	/** The Constant YOU_TUBE_CSV_REPORT_HEADER. */
	public static final String YOU_TUBE_CSV_REPORT_HEADER = "Date,TimeStamp,NetworkType,networkSubtype,operatorName,NetworkTypeWhenWifi,Device ID,Android ID,GpsStatus,Latitude,Longitude,Zone,Sales Region,Province,Sales Cluster,Location Accuracy,Altitude,Mcc,Mnc,Cellid,CGI,EnodeB,PCI,TAC,PSC,LAC,Band LTE/3G/2G/WiFi,WiFi Frequency ,macAddress,Connected BSSID,SSID,RSRP,RSRQ,RSSI,SINR,WiFiRSSI,WiFiSNR,RSCP,ECNO,RXLevel,RXQuality,DL Speed (Mbps),VideoDuration(ms),ScreenResolution,TotalBufferingTime,VideoStalling,VideoFreezingRatio (%),VideoLoadTime (ms),Channel,LinkSpeed,Internal IP,Base App Version,Module Version,Manufacturer,Model,Operating System,Baseband,Build ID,Battery Level,Battery Voltage,BatteryTemperature,SOC Model,Core Architecture,Fingerprint,Serial Number,chipSet,IsEnterprise,DualSimEnable,AutomaticDate&Time,Charging Status,IsLayer3Enabled,ProfileId";
	
	/** The Constant WORKORDER_ID. */
	public static final String WORKORDER_ID="workorderId";

	/** The Constant GET_DEVICES_BY_WORKORDER_ID. */
	public static final String GET_DEVICES_BY_WORKORDER_ID = "getDevicesByWorkOrderId";
	
	
	public static final String GET_PROBE_DETAIL_BY_KPI="getProbeDetailByKpi";
	
	/** The Constant GET_STEALTH_WO_LIST_COUNT_BY_WO_ID. */
	public static final String GET_STEALTH_WO_LIST_COUNT_BY_WO_ID="getStealthWOListCountByWOId";
	
	/** The Constant GET_STEALTH_TASK_DETAIL_BY_DEVICE_ID. */
	public static final String GET_STEALTH_TASK_DETAIL_BY_DEVICE_ID="getStealthTaskDetailByDeviceId";

	/** The Constant GET_STEALTH_TASK_BY_DEVICE_AND_WO_ID. */
	public static final String GET_STEALTH_TASK_BY_DEVICE_AND_WO_ID = "getStealthTaskByDeviceAndWOId";
	
	
	/** The Constant KEY_ASSIGNED. */
	public static final String KEY_ASSIGNED="Assigned";
	
	/** The Constant KEY_UNASSIGNED. */
	public static final String KEY_UNASSIGNED="Unassigned";
	
	/** The Constant STATUS_PENDING. */
	public static final String STATUS_PENDING="PENDING";
	
	/** The Constant WO_LIST_IS_EMPTY. */
	public static final String WO_LIST_IS_EMPTY = "Stealth workorder list is empty";

	/** The Constant IS_ACKNOWLEDGED. */
	public static final String IS_ACKNOWLEDGED="isAcknowledged";
	
	/** The Constant IS_DETAILS. */
	public static final String IS_DETAILS="isDetails";
	
	/** The Constant WORKORDER_CLOSED. */
	public static final String WORKORDER_CLOSED="Worker Order Closed";
	
	/** The Constant WORKORDER_STOPPED. */
	public static final String WORKORDER_STOPPED="Worker Order Stopped";
	
	/** The Constant ASSIGNMENT_TYPE. */
	public static final String ASSIGNMENT_TYPE="assignmentType";

	/** The Constant KEY_WORKORDER_STATUS. */
	public static final String KEY_WORKORDER_STATUS="workorderStatus";
	
	/** The Constant WO_STATUS_STOPPED. */
	public static final String WO_STATUS_STOPPED="STOPPED";
	
	/** The Constant WORKORDER_EXPIRED_OR_COMPLETED. */
	public static final String WORKORDER_EXPIRED_OR_COMPLETED = "Workorder Expired or Completed";
	
	/** The Constant GET_STEALTH_KPI_URI. */
	public static final String GET_STEALTH_KPI_URI = "getStealthKPISummary?";
	
	/** The Constant GET_KPI_DISTRIBUTION_AND_COUNT_URI. */
	public static final String GET_KPI_DISTRIBUTION_AND_COUNT_URI = "getKPIDistributionAndCount?";
	
	/** The Constant GET_CELL_WISE_DATA_URI. */
	public static final String GET_CELL_WISE_DATA_URI = "getCellWiseData?";
	
	/** The Constant GET_TOP_AND_WORST_ENODEB_URI. */
	public static final String GET_TOP_AND_WORST_ENODEB_URI="getTopEnodeBDetails?";
	
	/** The Constant EXCEPTION_PROBLEM_IN_DROPWIZARD. */
	public static final String EXCEPTION_PROBLEM_IN_DROPWIZARD = "There is some problem in completing the request in dropwizard";
	/** The Constant GET_FAILURE_DEVICE_DATA_FOR_DAYWISE. */

	public static final String GET_WORKORDERS_BY_WOID_AND_DATE = "getWOListByWOIdAndDate";

	public static final String COLUMN_FAMILY = "r";
	public static final String COLUMN_LATITUDE = "lat";
	public static final String COLUMN_LONGITUDE = "lng";
	public static final String COLUMN_DEVICE_ID = "deviceid";
	public static final String COLUMN_SCORE = "score";
	public static final String COLUMN_DATE = "date";
	public static final String NO_DATA_AVAILABLE_JSON = "{\"result\":\"No data available\"}";
	public static final String COLUMN_WORKORDER_ID = "woid";
	public static final String COLUMN_TASK_ID = "taskid";
	public static final String TYPE_GEO_L4="GeographyL4";
	public static final String TYPE_SITE="site";
	
	public static final String AVERAGE_RSRP_KEY="avgRsrp";
	public static final String AVERAGE_RSRQ_KEY="avgRsrq";
	public static final String AVERAGE_DL_KEY="avgDl";
	public static final String AVERAGE_UL_KEY="avgUl";
	public static final String AVERAGE_SINR_KEY="avgSinr";
	public static final String CGI_KEY="cgi";


	public static final String COLUMN_FAIR_COUNT = "fairCount";
	public static final String COLUMN_POOR_COUNT = "poorCount";
	public static final String COLUMN_GOOD_COUNT = "goodCount";
	public static final String COLUMN_SINR_JSON = "sinrJson";
	public static final String COLUMN_RSRP_JSON = "rsrpJson";
	public static final String COLUMN_RSRQ_JSON = "rsrqJson";
	public static final String COLUMN_UL_JSON = "ulJson";
	public static final String COLUMN_DL_JSON = "dlJson";
	public static final String COLUMN_PKT_JSON = "pktlJson";
	public static final String COLUMN_BUFFERING_JSON = "bfring";
	public static final String COLUMN_TTL_JSON="ttl";
	public static final String COLUMN_DNS_JSON="dns";
	public static final String COLUMN_THROUGHPUT_JSON="ytDl";
	public static final String COLUMN_LATENCY_JSON="ltJson";
	public static final String COLUMN_TOP_RSRP="rsrpTop";
	public static final String COLUMN_TOP_RSRQ="rsrqTop";
	public static final String COLUMN_TOP_SINR="sinrTop";
	public static final String COLUMN_TOP_UL="ulTop";
	public static final String COLUMN_TOP_DL="dlTop";
	public static final String COLUMN_BOTTOM_RSRP="rsrpBottom";
	public static final String COLUMN_BOTTOM_RSRQ="rsrqBottom";
	public static final String COLUMN_BOTTOM_SINR="sinrBottom";
	public static final String COLUMN_BOTTOM_UL="ulBottom";
	public static final String COLUMN_BOTTOM_DL="dlBottom";
	public static final String COLUMN_GL4="gL4";
	public static final String COLUMN_GL3="gL3";
	public static final String COLUMN_GL2="gL2";
	public static final String KPI_STRING="kpi";
	
	
	
	public static final String KEY_FAIR_ENODEB_COUNT="fairEnodeBCount";
	public static final String KEY_POOR_ENODEB_COUNT="poorEnodeBCount";
	public static final String KEY_GOOD_ENODEB_COUNT="goodEnodeBCount";
	public static final String KEY_RSRP_DIST="rsrpDist";
	public static final String KEY_SINR_DIST="sinrDist";
	public static final String KEY_RSRQ_DIST="rsrqDist";
	public static final String KEY_RSRP="rsrp";
	public static final String KEY_SINR="sinr";
	public static final String KEY_RSRQ="rsrq";
	public static final String KEY_UL="ul";
	public static final String KEY_DL="dl";
	public static final String KEY_PKT_LOSS="packetLoss";
	public static final String KEY_TTL="ttl";
	public static final String KEY_DNS="dns";
	public static final String KEY_BUFFER_TIME="bufferTime";
	public static final String KEY_THROUGHPUT="thput";
	public static final String KEY_LATENCY="latency";
	public static final String KEY_MIN="min";
	public static final String KEY_MAX="max";
	public static final String KEY_AGV="avg";
	public static final String KEY_ENODEB_ID="enodeBId";
	public static final String KEY_DISTRIBUTION="distribution";
	public static final String KEY_KPI_COUNT="counts";
	public static final String KEY_ENB_COUNT="eNodeB";
	public static final String KEY_FAIR="fair";
	public static final String KEY_GOOD="good";
	public static final String KEY_POOR="poor";
	
	
	public static final String KEY_R1="r1";
	public static final String KEY_R2="r2";
	public static final String KEY_R3="r3";
	public static final String KEY_R4="r4";
	public static final String KEY_R5="r5";
	public static final String KEY_R6="r6";
	public static final String KEY_R7="r7";
	public static final String KEY_R8="r8";
	public static final String KEY_R9="r9";
	public static final String KEY_R10="r10";

	public static final String KEY_TOP = "top";
	public static final String KEY_BOTTOM = "bottom";
	public static final Integer GRID_LENGTH_200=200;
	public static final Integer GRID_LENGTH_100=100;
	public static final Integer GRID_LENGTH_50=50;
	public static final Integer GRID_LENGTH_25=25;
	public static final String REFERENCE_LAT_KEY="CountryLatitude";
	public static final String REFERENCE_LONG_KEY="CountryLongitude";
	public static final Double THRESHOLD_VALUE =90d;
	public static final String CONST_FAIR="Fair";
	public static final String CONST_GOOD="Good";
	public static final String CONST_POOR="Poor";

	public static final String DATE_FORMATE = "ddMMyy";
	public static final String DATE_FORMATE_TIME_STAMP = "ddMMyy";
	
	public static final String DATE_FORMAT_HOUR = "HH-dd-MM-yy";

	public static final String  COLUMN_TIME = "time";

	public static final Integer STEALTH_HEADERS_VERSION_INDEX = 17;
	public static final Integer STEALTH_HEADERS_MAKE_INDEX = 6;
	public static final Integer STEALTH_HEADERS_MODEL_INDEX = 7;
	public static final Integer STEALTH_HEADERS_OPERATOR_INDEX = 8;
	public static final Integer STEALTH_HEADERS_OS_INDEX = 20;
	public static final Integer STEALTH_HEADERS_DUAL_SIM_INDEX = 23;
	public static final Integer STEALTH_HEADERS_AUTO_DATE_TIME_INDEX = 18;
	public static final Integer STEALTH_HEADERS_DEVICE_ID_INDEX = 9;

	public static final long ONE_HOUR_INTERVAL = 60 * 60 * 1000l;
	public static final long FIFTEEN_MINUTE_INTERVAL = 15 * 60 * 1000l;
	public static final long ONE_DAY_INTERVAL = 24 * 60 * 60 * 1000l;
	public static final long ONE_WEEK_INTERVAL = 7 * 24 * 60 * 60 * 1000l;
	
	public static final String REMARK_DELAYED_ACKNOWLEDGEMENT = "Workorder is not accepted till the time of execution";

	public static final String WO_TYPE_ONETIME = "ONETIME";
	
	public static final String GWO_FREQUENCY_KEY= "woFrequency";
	
	public static final String IS_RANDOMIZATION= "isRandomization";
	
	public static final String EXECUTION_TIME= "executionTime";
	
	public static final String SPLIT_KEY= "splits";
	
	public static final String ACCEPTED= "ACCEPTED";
	public static final String REJECTED= "REJECTED";
	public static final String PENDING= "PENDING";

	public static final String IS_PROBE_TEST ="isProbe";
	public static final String PROBE_DETAIL_TABLE = "ProbeDetail";
	
   	public static final String PERSIST_PROBE_DATA_MICROSERVICE_URL="/rest/ms/ProbeDetail/persistData";

	public static final String CONNECTIVITY_KPI = "Connectivity";
	
}

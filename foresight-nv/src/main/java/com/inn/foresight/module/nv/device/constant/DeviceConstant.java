package com.inn.foresight.module.nv.device.constant;

import com.inn.commons.Symbol;
import com.inn.commons.lang.NumberUtils;

/** The Class DeviceConstant. */
public class DeviceConstant {


	/** Instantiates a new device constant. */
	protected DeviceConstant() {
	}


	/** The Constant GET_ALL_NV_DEVICE_LIST. */
	// Query
	public static final String GET_ALL_DEVICE_LIST = "getAllDeviceList";

	/** The Constant GET_ALL_NV_DEVICE_LIST. */
	public static final String GET_ALL_NV_DEVICE_LIST = "getAllNVDeviceList";

	/** The Constant GET_DEVICE_LIST_COUNT. */
	public static final String GET_DEVICE_LIST_COUNT = "getDeviceListCount";

	/** The Constant GET_DEVICE_COUNT_GROUP_BY_L1. */
	public static final String GET_DEVICE_COUNT_GROUP_BY_L1 = "getDeviceCountGroupByL1";

	/** The Constant GET_DEVICE_COUNT_GROUP_BY_L2. */
	public static final String GET_DEVICE_COUNT_GROUP_BY_L2 = "getDeviceCountGroupByL2";

	/** The Constant GET_DEVICE_COUNT_GROUP_BY_L3. */
	public static final String GET_DEVICE_COUNT_GROUP_BY_L3 = "getDeviceCountGroupByL3";

	/** The Constant GET_DEVICE_COUNT_GROUP_BY_L4. */
	public static final String GET_DEVICE_COUNT_GROUP_BY_L4 = "getDeviceCountGroupByL4";

	/** The Constant GET_DEVICE_FOR_VIEW_PORT. */
	public static final String GET_DEVICE_FOR_VIEW_PORT = "getDevicesForViewport";

	/** The Constant GET_DEVICE_COUNT_WRAPPER_BY_L2. */
	public static final String GET_DEVICE_COUNT_WRAPPER_BY_L2 = "getDeviceCountByL2";

	/** The Constant GET_DEVICE_COUNT_WRAPPER_BY_L3. */
	public static final String GET_DEVICE_COUNT_WRAPPER_BY_L3 = "getDeviceCountByL3";

	/** The Constant GET_DEVICE_COUNT_WRAPPER_BY_L4. */
	public static final String GET_DEVICE_COUNT_WRAPPER_BY_L4 = "getDeviceCountByL4";

	/** The Constant GET_DEVICE_LIST_INSIDE_CLUSTER. */
	public static final String GET_DEVICE_LIST_INSIDE_VIEW_PORT = "getDeviceListInsideViewPort";

	/** The Constant GET_DEVICE_SPECIFICATION_BY_MODEL. */
	public static final String GET_DEVICE_SPECIFICATION_BY_MODEL = "getDeviceSpecificationByModel";

	/** The Constant GET_NV_DEVICE_DATA_WRAPPER_BY_DEVICE_ID. */
	public static final String GET_NV_DEVICE_DATA_WRAPPER_BY_DEVICE_ID = "getNVDeviceDataWrapperByDeviceId";

	/** The Constant GET_DEVICE_DATA_BY_DEVICE_ID. */
	public static final String GET_DEVICE_DATA_BY_DEVICE_ID = "getDeviceDataByDeviceId";

	/** The Constant GET_DEVICE_LIST_INSIDE_CLUSTER. */
	public static final String GET_DEVICE_LIST_INSIDE_CLUSTER = "getDeviceListInsideCluster";
	// Query

	/** The Constant NVDEVICE_OPERATOR_FILTER. */
	// Filter Constants
	public static final String NVDEVICE_OPERATOR_FILTER = "nvDeviceOperatorFilter";

	/** The Constant FILTER_NAME. */
	public static final String FILTER_NAME = "filterName";

	/** The Constant FILTER_PARAM. */
	public static final String FILTER_PARAM = "filterParam";

	/** The Constant FILTER_VALUE. */
	public static final String FILTER_VALUE = "filterValue";

	// Filter Constants

	/** The Constant OPERATOR_PLACEHOLDER. */
	// Place holders
	public static final String GEOGRAPHY_PLACE_HOLDER = "geographyName";

	/** The Constant NE_LAT_PLACEHOLDER. */
	public static final String NE_LAT_PLACEHOLDER = "nELat";

	/** The Constant NE_LNG_PLACEHOLDER. */
	public static final String NE_LNG_PLACEHOLDER = "nELng";

	/** The Constant SW_LAT_PLACEHOLDER. */
	public static final String SW_LAT_PLACEHOLDER = "sWLat";

	/** The Constant SW_LNG_PLACEHOLDER. */
	public static final String SW_LNG_PLACEHOLDER = "sWLng";

	/** The Constant MODEL_PLACE_HOLDER. */
	public static final String MODEL_PLACE_HOLDER = "modelName";

	/** The Constant DEVICE_ID_PLACE_HOLDER. */
	public static final String DEVICE_ID_PLACE_HOLDER = "deviceId";

	/** The Constant GEOGRAPHY_ID_PLACE_HOLDER. */
	public static final String GEOGRAPHY_ID_PLACE_HOLDER = "geographyId";

	/** The Constant EXCEPTION_SOMETHING_WENT_WRONG. */
	// Response Message
	public static final String EXCEPTION_SOMETHING_WENT_WRONG = "There is some problem in completing the request.";

	/** The Constant MINIMUM_ZOOM_LEVEL_FOR_L2. */
	public static final short MINIMUM_ZOOM_LEVEL_FOR_L2 = 5;

	/** The Constant MAXIMUM_ZOOM_LEVEL_FOR_L2. */
	public static final short MAXIMUM_ZOOM_LEVEL_FOR_L2 = 8;

	/** The Constant MINIMUM_ZOOM_LEVEL_FOR_L3. */
	public static final short MINIMUM_ZOOM_LEVEL_FOR_L3 = 9;

	/** The Constant MAXIMUM_ZOOM_LEVEL_FOR_L3. */
	public static final short MAXIMUM_ZOOM_LEVEL_FOR_L3 = 10;

	/** The Constant MINIMUM_ZOOM_LEVEL_FOR_L4. */
	public static final short MINIMUM_ZOOM_LEVEL_FOR_L4 = 11;

	/** The Constant MAXIMUM_ZOOM_LEVEL_FOR_L4. */
	public static final short MAXIMUM_ZOOM_LEVEL_FOR_L4 = 18;

	/** The Constant FIRST_INDEX. */
	public static final int FIRST_INDEX = 0;

	/** The Constant SECOND_INDEX. */
	public static final int SECOND_INDEX = 1;

	/** The Constant THIRD_INDEX. */
	public static final int THIRD_INDEX = 2;

	/** The Constant OPERATOR_ALL. */
	public static final String OPERATOR_ALL = "ALL";

	/** The Constant USER_TYPE_ALL. */
	public static final String USER_TYPE_ALL = "ALL";

	/** The Constant INVALID_PARAMETER_JSON. */
	public static final String INVALID_PARAMETER_JSON = "{\"message\":\"Parameters given are invalid.\"}";

	/** The Constant MAX_RESULT. */
	public static final int MAX_RESULT = 1;

	/** The Constant DEVICE_ID_PARAM. */
	public static final String DEVICE_ID_PARAM = "deviceId";

	/** The Constant OPERATOR_PARAM. */
	public static final String OPERATOR_PARAM = "operator";

	/** The Constant USER_TYPE_PARAM. */
	public static final String USER_TYPE_PARAM = "isEnterprise";

	/** The Constant GEOGRAPHYL1. */
	public static final String GEOGRAPHYL1 = "geographyL1";

	/** The Constant GEOGRAPHYL2. */
	public static final String GEOGRAPHYL2 = "geographyL2";

	/** The Constant GEOGRAPHYL3. */
	public static final String GEOGRAPHYL3 = "geographyL3";

	/** The Constant GEOGRAPHYL4. */
	public static final String GEOGRAPHYL4 = "geographyL4";

	/** The Constant DEVICE_ID_FILTER. */
	public static final String DEVICE_ID_FILTER = "nvDeviceIdFilter";

	/** The Constant GEOGRAPHYL1_FILTER. */
	public static final String GEOGRAPHYL1_FILTER = "nvDeviceGeographyL1Filter";

	/** The Constant GEOGRAPHYL2_FILTER. */
	public static final String GEOGRAPHYL2_FILTER = "nvDeviceGeographyL2Filter";

	/** The Constant GEOGRAPHYL3_FILTER. */
	public static final String GEOGRAPHYL3_FILTER = "nvDeviceGeographyL3Filter";

	/** The Constant GEOGRAPHYL4_FILTER. */
	public static final String GEOGRAPHYL4_FILTER = "nvDeviceGeographyL4Filter";

	/** The Constant MAKE_FILTER. */
	public static final String MAKE_FILTER = "nvDeviceMakeFilter";

	/** The Constant MODEL_FILTER. */
	public static final String MODEL_FILTER = "nvDeviceModelFilter";

	/** The Constant OS_FILTER. */
	public static final String OS_FILTER = "nvDeviceOSFilter";

	/** The Constant USER_TYPE_FILTER. */
	public static final String USER_TYPE_FILTER = "nvDeviceUserTypeFilter";

	/** The Constant DEVICE_INFO_IMEI_FILTER. */
	public static final String DEVICE_INFO_IMEI_FILTER = "deviceInfoImeiFilter";

	/** The Constant DEVICE_INFO_IMSI_FILTER. */
	public static final String DEVICE_INFO_IMSI_FILTER = "deviceInfoImsiFilter";

	/** The Constant GET_DEVICE_LIST_BY_DEVICE_INFO_IDS. */
	public static final String GET_DEVICE_LIST_BY_DEVICE_INFO_IDS = "getDeviceListByDeviceInfoID";

	/** The Constant GEOGRAPHY_ID. */
	public static final String GEOGRAPHY_ID = "geographyId";

	/** The Constant MAKE. */
	public static final String MAKE = "make";

	/** The Constant MODEL. */
	public static final String MODEL = "model";

	/** The Constant OS. */
	public static final String OS = "os";

	/** The Constant INVALID_GEOGRAPHY_LEVEL. */
	public static final String INVALID_GEOGRAPHY_LEVEL = "Invalid Geography Level";

	/** The Constant GET_DEVICE_LIST_FOR_WO. */
	public static final String GET_DEVICE_LIST_FOR_WO = "getDeviceListForWO";

	/** The Constant GET_DEVICE_LIST_COUNT. */
	public static final String GET_DEVICE_LIST_COUNT_FOR_WO = "getDeviceListCountForWO";

	/** The Constant GET_DEVICE_LIST_BY_IDS. */
	public static final String GET_DEVICE_LIST_BY_IDS = "getDeviceListByIds";

	/** The Constant FIND_DEVICE_INFO. */
	public static final String FIND_DEVICE_INFO = "findDeviceInfo";

	/** The Constant WO_START_TIME. */
	public static final String WO_START_TIME = "woStartTime";

	/** The Constant FAILURE_JSON. */
	public static final String FAILURE_JSON = "{\"result\":\"failure\"}";

	/** The Constant GEOGRAPHY_LEVEL. */
	public static final String GEOGRAPHY_LEVEL = "geographyLevel";

	/** The Constant LOWER_LIMIT. */
	public static final String LOWER_LIMIT = "lLimit";

	/** The Constant UPPER_LIMIT. */
	public static final String UPPER_LIMIT = "uLimit";

	/** The Constant ID_LIST. */
	public static final String ID_LIST = "idList";

	/** The Constant MAX_DEVICE_COUNT. */
	public static final int MAX_DEVICE_COUNT = 1;

	/** The Constant IMEI_PARAM. */
	public static final String IMEI_PARAM = "imei";

	/** The Constant IMSI_PARAM. */
	public static final String IMSI_PARAM = "imsi";

	/** The Constant MESSAGE_START. */
	public static final String MESSAGE_START = "{message : \"";

	/** The Constant MESSAGE_END. */
	public static final String MESSAGE_END = "\"}";

	/** The Constant FAILURE_JSON. */
	public static final String SUCCESS_JSON = "{\"result\":\"success\"}";

	/** The Constant GET_DEVICES_BY_DEVICE_IDS. */
	public static final String GET_DEVICES_BY_DEVICE_IDS = "getDevicesByDeviceIds";

	/** The Constant GET_DEVICES_BY_DEVICE_IDS_AND_GROUP_ID. */
	public static final String GET_DEVICES_BY_DEVICE_IDS_AND_GROUP_ID = "getDevicesByDeviceIdsAndGroupId";

	/** The Constant DEVICE_GROUP_ID_PREFIX. */
	public static final String DEVICE_GROUP_ID_PREFIX = "DG";

	/** The Constant EARTH_RADIUS. */
	public static final double EARTH_RADIUS = 6371;

	/** The Constant FIND_PROFILE_DATA. */
	public static final String GET_ALL_DEVICE_GROUP = "getAllDeviceGroup";

	/** The Constant GET_DEVICE_GROUP_WITHIN_VIEW_PORT. */
	public static final String GET_DEVICE_GROUP_WITHIN_VIEW_PORT = "getDeviceGroupWithinViewPort";

	/** The Constant NORTH_EAST_LAT. */
	public static final String NORTH_EAST_LAT = "NELat";

	/** The Constant NORTH_EAST_LONG. */
	public static final String NORTH_EAST_LONG = "NELng";

	/** The Constant SOUTH_WEST_LAT. */
	public static final String SOUTH_WEST_LAT = "SWLat";

	/** The Constant SOUTH_WEST_LONG. */
	public static final String SOUTH_WEST_LONG = "SWLng";

	/** The Constant GROUP_ID. */
	public static final String GROUP_ID = "groupId";

	/** The Constant DEVICE_ID. */
	public static final String DEVICE_ID = "deviceId";

	/** The Constant GET_BUILDING_COUNT_GROUP_BY_GEOL1. */
	public static final String GET_ALL_DEVICE_GROUP_COUNT_GROUP_BY_GEOL1 = "getAllDeviceGroupCountGroupByGeol1";

	/** The Constant GET_BUILDING_COUNT_GROUP_BY_GEOL2. */
	public static final String GET_ALL_DEVICE_GROUP_COUNT_GROUP_BY_GEOL2 = "getAllDeviceGroupCountGroupByGeol2";

	/** The Constant GET_BUILDING_COUNT_GROUP_BY_GEOL3. */
	public static final String GET_ALL_DEVICE_GROUP_COUNT_GROUP_BY_GEOL3 = "getAllDeviceGroupCountGroupByGeol3";

	/** The Constant GET_BUILDING_COUNT_GROUP_BY_GEOL4. */
	public static final String GET_ALL_DEVICE_GROUP_COUNT_GROUP_BY_GEOL4 = "getAllDeviceGroupCountGroupByGeol4";

	/** The Constant GET_BUILDING_BY_VIEWPORT. */
	public static final String GET_ALL_DEVICE_GROUP_BY_VIEWPORT = "getAllDeviceGroupByViewPort";

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

	/** The Constant GET_DEVICES_BY_GROUP_ID. */
	public static final String GET_DEVICES_BY_GROUP_ID = "getDevicesByGroupId";

	/** The Constant GET_DEVICE_COUNT_BY_GROUP_ID. */
	public static final String GET_DEVICE_COUNT_BY_GROUP_ID = "getDeviceCountByGroupId";

	/** The Constant GET_DEVICE_DATA_BY_GROUP_ID. */
	public static final String GET_DEVICE_DATA_BY_GROUP_ID = "getDeviceDataByGroupId";

	/** The Constant ULIMIT. */
	public static final String ULIMIT = "ulimit";

	/** The Constant LLIMIT. */
	public static final String LLIMIT = "llimit";

	/** The Constant GET_DEVICE_DETAIL_BY_TACFAC. */
	public static final String GET_DEVICE_DETAIL_BY_MODEL_NAME = "getDeviceDetailByModelName";

	/** The Constant GET_NV_DEVICE_DETAIL_BY_DEVICE_ID. */
	public static final String GET_NV_DEVICE_DETAIL_BY_DEVICE_ID = "getDeviceDetailByDeviceId";

	/** The Constant OPERATOR. */
	public static final String OPERATOR = "OPERATOR";

	/** The Constant NV_DEVICE_DATA. */
	public static final String NV_DEVICE_DATA = "NVDeviceData";

	/** The Constant TECHNOLOGY. */
	public static final Integer INDEX_ZERO = NumberUtils.INTEGER_ZERO;

	/** The Constant PROFILE_MAP_KEY_GEOGRAPHY_INDEX. */
	public static final Integer PROFILE_MAP_KEY_GEOGRAPHY_INDEX = INDEX_ZERO;

	/** The Constant PROFILE_MAP_KEY_DELIMITER. */
	public static final String PROFILE_MAP_KEY_DELIMITER = Symbol.HASH_STRING;


	/** The Constant BLANK_OPERATOR. */
	public static final String BLANK_OPERATOR = "{\"message\":\"Given Operator is blank or null.\"}";

	/** The Constant PAYLOAD_FOR_PROBE_DEVICES_REMOVE_NOTIFICATION. */
	public static final String PAYLOAD_FOR_PROBE_DEVICES_REMOVE_NOTIFICATION = "{\"message\": \"DEVICE DELETED\", \"moduleName\": \"PULSE\"}";

	/** The Constant INVALID_MCC_MNC_MESSAGE. */
	public static final String INVALID_MCC_MNC_MESSAGE = "Invalid mcc & mnc unable to get operator";

	/** The Constant END_DATE. */
	public static final String END_DATE = "endDate";

	/** The Constant ENTERPRISE_USER. */
	public static final String ENTERPRISE_USER = "Enterprise";

	/** The Constant CONSUMER_USER. */
	public static final String CONSUMER_USER = "Consumer";

	/** The Constant ANDROID. */
	public static final String ANDROID = "ANDROID";

	/** The Constant GET_ALL_DEVICE_GROUP_COUNT. */
	public static final String GET_ALL_DEVICE_GROUP_COUNT = "getAllDeviceGroupCount";

	/** The Constant GEOGRAPHYL1_INDEX. */
	public static final Integer GEOGRAPHYL1_INDEX = 0;

	/** The Constant GEOGRAPHYL2_INDEX. */
	public static final Integer GEOGRAPHYL2_INDEX = 1;

	/** The Constant GEOGRAPHYL3_INDEX. */
	public static final Integer GEOGRAPHYL3_INDEX = 2;



	/** The Constant GEOGRAPHYL4_INDEX. */
	public static final Integer GEOGRAPHY_L4_INDEX = 3;

	/** The Constant FILE_PATH. */
	public static final String FILE_NAME = "{\"fileName\":\"%s\"}";

	/** The Constant GET_DEVICE_RECHARGE_NOTIFICATION. */
	public static final String GET_DEVICE_RECHARGE_NOTIFICATION = "getDeviceRechargeNotification";

	/** The Constant GET_DEVICE_RECHARGE_NOTIFICATION_COUNT. */
	public static final String GET_DEVICE_RECHARGE_NOTIFICATION_COUNT = "getDeviceRechargeNotificationCount";

	/** The Constant RECHARGE_DAY_DIFFERENCE. */
	public static final int RECHARGE_DAY_DIFFERENCE = 2;

	/** The Constant DEVICE_RECHARGE_DETAIL_REPORT_PATH. */
	public static final String DEVICE_RECHARGE_DETAIL_REPORT_PATH = "DEVICE_RECHARGE_DETAIL_REPORT_PATH";

	/** The Constant PULSE. */
	public static final String PULSE = "PULSE";

	/** The Constant DEVICE_RECHARGE_DETAIL_REPORT. */
	public static final String DEVICE_RECHARGE_DETAIL_REPORT = "DEVICE_RECHARGE_DETAIL_REPORT";

	/** The Constant PAYLOAD_FOR_PULSE. */
	public static final String PAYLOAD_FOR_DEVICE_UPDATE_NOTIFICATION = "{\"message\": \"DEVICE UPDATED.\", \"moduleName\": \"PULSE\"}";

	/** The Constant WO_START_TIME. */
	public static final String WO_END_TIME = "woEndTime";

	/** The Constant GET_DEVICE_LIST_FOR_WO. */
	public static final String GET_DEVICE_LIST_FOR_WORK_ORDER = "getDeviceListForWorkOrder";

	/** The Constant GET_DEVICE_LIST_COUNT_FOR_WORK_ORDER. */
	public static final String GET_DEVICE_LIST_COUNT_FOR_WORK_ORDER = "getDeviceListCountForWorkOrder";

	/** The Constant WORKORDER_ID. */
	public static final String WORKORDER_ID = "id";


	/** The Constant RESULT_SUCCESS. */
	public static final String RESULT_SUCCESS = "{\"Result\":\"Success\"}";

	/** The Constant RESULT_DEVICE_ID_NULL. */
	public static final String RESULT_DEVICE_ID_NULL = "{\"Result\":\"Failure\",\"Reason\":\"Device id is null\"}";

	/** The Constant RESULT_FAILURE. */
	public static final String RESULT_FAILURE = "{\"Result\":\"Failure\"} ";

	/** The Constant RESULT_PUSHNOTIFICATION_ID_NULL. */
	public static final String RESULT_PUSHNOTIFICATION_ID_NULL = "{\"Result\":\"Failure\",\"Reason\":\"Pushnotification id is null\"}";

	/** The Constant LOCATION_RECIEVED. */
	public static final String LOCATION_RECIEVED = "Location Recieved";

	/** The Constant LOCATION_NOT_RECIEVED. */
	public static final String LOCATION_NOT_RECIEVED = "Location Not Recieved";

	/** The Constant GEOGRAPHY_NOT_FOUND. */
	public static final String GEOGRAPHY_NOT_FOUND = "Geography Not Found";

	/** The Constant GET_DEVICES_BY_DEVICE_WORKORDER_ID. */
	public static final String GET_DEVICES_BY_DEVICE_WORKORDER_ID = "getDevicesByDeviceWorkOrderId";
	public static final String ENTERPRISE_FILTER="nvDeviceEnterpriseFilter";
	public static final String CONSUMER_FILTER="nvDeviceConsumerFilter";

	/** The Constant START_DATE. */
	public static final String START_DATE = "startDate";

	/** The Constant KPI. */
	public static final String KPI = "kpi";

	/** The Constant THRESHOLD. */
	public static final String THRESHOLD = "threshold";

	/** The Constant RESULT_TYPE. */
	public static final String RESULT_TYPE = "resultType";

	/** The Constant ZOOMLEVEL. */
	public static final String ZOOMLEVEL = "zoomLevel";

	/** The Constant DATATYPE. */
	public static final String DATATYPE = "dataType";


	/** The Constant GET_BUILDING_COUNT_GROUP_BY_GEOL1. */
	public static final String GET_DEVICE_GROUP_COUNT_GROUP_BY_GEOL1 = "getDeviceGroupCountGroupByGeol1";

	/** The Constant GET_BUILDING_COUNT_GROUP_BY_GEOL2. */
	public static final String GET_DEVICE_GROUP_COUNT_GROUP_BY_GEOL2 = "getDeviceGroupCountGroupByGeol2";

	/** The Constant GET_BUILDING_COUNT_GROUP_BY_GEOL3. */
	public static final String GET_DEVICE_GROUP_COUNT_GROUP_BY_GEOL3 = "getDeviceGroupCountGroupByGeol3";

	/** The Constant GET_BUILDING_COUNT_GROUP_BY_GEOL4. */
	public static final String GET_DEVICE_GROUP_COUNT_GROUP_BY_GEOL4 = "getDeviceGroupCountGroupByGeol4";

	/** The Constant GET_BUILDING_BY_VIEWPORT. */
	public static final String GET_DEVICE_GROUP_BY_VIEWPORT = "getDeviceGroupByViewPort";


	public static final String GET_NV_INSTALLATION_DETAIL = "getNvInstallationDetail";
	public static final String NV_DURATION_TIME = "NV_DURATION_TIME";
	public static final String NV_CURRENT_DATE = "currentDate";
	public static final String NV_PREVIOUS_DATE = "previousDate";
	public static final String NV_DEVICE_ID = "deviceId";
	public static final String DEFAULT_OPERATOR_FILTER="nvDeviceDefaultOperatorFilter";
	public static final String REFERENCE_LAT_KEY="CountryLatitude";
	public static final String REFERENCE_LONG_KEY="CountryLongitude";
	public static final Integer GRID_LENGHT_FOR_ZOOM16=300;
	public static final Integer GRID_LENGHT_FOR_ZOOM17=100;
	public static final Integer GRID_LENGHT_FOR_ZOOM18=50;
	public static final Integer ZOOM_LEVEL_16=16;
	public static final Integer ZOOM_LEVEL_17=17;
	public static final Integer ZOOM_LEVEL_18=18;
	public static final String OPERATOR_MCC_MNC_MAP = "OPERATOR_MCC_MNC_MAP";
	public static final String NV_FCM_KEY = "registration_ids";
	public static final String DEVICE_OS_IOS = "iOS";



	/**
	 * Contants for creating Device Data
	 */
	public static final String BANDS_PATTERN = "\\\\b850\\\\b|\\\\b900\\\\b|\\\\b1800\\\\b|\\\\b1900\\\\b|\\\\b2100\\\\b|\\\\b2600\\\\b|\\\\b1700\\\\b|\\\\b2300\\\\b|\\\\b2500\\\\b";
	public static final String TABLE_TD_NFO = "td[class=nfo]";
	public static final String TABLE_TD_TTL = "td[class=ttl]";
	public static final String TABLE_ROW = "tr";
	public static final String PHP_EXTENSION = ".php";
	public static final String JPG_EXTENSION = ".jpg";
	public static final String QUICKSEARCH = "quicksearch";
	public static final String MARKET_NAME = "market_name";
	public static final String MODEL_NAME = "ModelName";
	public static final String MODEL_CAMELCASE = "Model";
	public static final String BRAND = "Brand";
	public static final String BANDS_2G = "2G bands";
	public static final String BANDS_3G = "3G bands";
	public static final String BANDS_4G = "4G bands";
	public static final String BATTERY_LIFE = "Battery life";
	public static final String WLAN = "WLAN";
	public static final String SIM = "SIM";
	public static final String SPEED = "Speed";
	public static final String GPRS = "GPRS";
	public static final String EDGE = "EDGE";
	public static final String ANNOUNCED = "Announced";
	public static final String DIMENSIONS = "Dimensions";
	public static final String RESOLUTION = "Resolution";
	public static final String MULTITOUCH = "Multitouch";
	public static final String OS_CAPITAL = "OS";
	public static final String CHIPSET = "Chipset";
	public static final String GPU = "GPU";
	public static final String CARD_SLOT = "Card slot";
	public static final String INTERNAL = "Internal";
	public static final String FEATURES = "Features";
	public static final String SIZE_CAMEL = "Size";
	public static final String ALERT_TYPES = "Alert types";
	public static final String LOUDSPEAKER = "Loudspeaker";
	public static final String BLUETOOTH = "Bluetooth";
	public static final String GPS = "GPS";
	public static final String RADIO = "Radio";
	public static final String USB = "USB";
	public static final String SENSORS = "Sensors";
	public static final String MESSASING = "Messaging";
	public static final String BROWSER = "Browser";
	public static final String JAVA = "Java";
	public static final String COLORS = "Colors";
	public static final String JACK_3_5MM = "3.5mm jack";
	public static final String WEIGHT_CAMEL = "Weight";
	public static final String STAND_BY = "Stand-by";
	public static final String TALK_TIME = "Talk time";
	public static final String MUSIC_PLAY = "Music play";
	public static final String PRICE_GROUP = "Price";
	public static final String SAR_US = "SAR";
	public static final String SAR_EU = "SAR EU";
	public static final String TECHNOLOGY_RAN="Technology";
	public static final String STATUS_CAMEL = "Status";
	public static final String PRIMARY_CAMEL_CASE = "Primary";
	public static final String SECONDARY_CAMEL_CASE = "Secondary";
	public static final String IMAGE_URL= "imageUrl";
	public static final String IOS = "iOS";
	public static final String DUAL_VIDEO_CALL = "dual video call";
	public static final String GET_GEOGRAPHYL4_DATA_URL = "GET_GEOGRAPHYL4_DATA_URL";
	public static final String VIDEO = "Video";
	public static final String YES = "YES";
	public static final String NO = "NO";
	public static final String TYPE_CAPITALISE = "Type";
	public static final String TELEMETRY_CPU = "CPU";
	public static final String GSM_ARENA_URL = "https://www.gsmarena.com/";
	public static final String DEVICE_QUICK_SEARCH = "quicksearch";
	public static final String MODEL_CODE = "ModelCode";
	public static final String INSTALLED_FILTER="installedFilter";
	public static final String PROBE="PROBE";
	public static final String APP_VERSION = "appVersion";
	public static final String SOURCE = "source";
	public static final String MODIFICATION_TIME = "modificationTime";
	public static final String NAME = "name";
	public static final String MODULE = "module";
	public static final String DEVICE_INFO = "deviceInfo";
	public static final String USER = "user";
	public static final String FIRST_NAME = "firstName";
	public static final String LAST_NAME = "lastName";


}


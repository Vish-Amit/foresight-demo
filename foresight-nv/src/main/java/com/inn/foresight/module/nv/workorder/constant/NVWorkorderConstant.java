package com.inn.foresight.module.nv.workorder.constant;

/** The Interface NVWorkorderConstant. */
public interface NVWorkorderConstant {
	
	/** The workorder id prefix. */
	public static final String WORKORDER_ID_PREFIX = "WO";

	/** The recipe id. */
	// Query Constants
	public static final String RECIPE_ID = "recipeId";
	
	
	// Query Constants
	public static final String FILE_ID = "fileId";


	/** The user id. */
	public static final String USER_ID = "userId";

	/** The workorder name. */
	public static final String WORKORDER_NAME = "workorderName";

	/** The exception logger. */
	// Loggers
	public static final String EXCEPTION_LOGGER = "Getting Exception {}";

	/** The result size logger. */
	public static final String RESULT_SIZE_LOGGER = "Getting Result Size {}";

	/** The invalid recipe details. */
	// Utility Constants
	public static final String INVALID_RECIPE_DETAILS = "Invalid Recipe Details.";
	
	/** The invalid recipe details. */
	public static final String RECIPE_ASSIGNED_TO_WO = "Recipe already assigned to workorder.";
	
	/** The Recipe assigned to wo parameter json. */
	public static final String RECIPE_ASSIGNED_TO_WO_JSON = "{\"message\":\"Recipe already assigned to workorder.\"}";
	public static final String NO_DATA_FOUND_JSON = "{\"message\":\"No Data Found For The Request.\"}";

	/** The user name. */
	public static final String USER_NAME = "userName";

	/** The user name. */
	public static final String REMARK = "remark";

	/** The assigned to. */
	public static final String ASSIGNED_TO = "assignedTo";

	/** The status. */
	public static final String STATUS = "status";

	/** The site id. */
	public static final String SITE_ID = "siteId";

	/** The pci azimuth. */
	public static final String PCI_AZIMUTH_MAP = "pciAzimuthMap";
	public static final String PCI_AZIMUTH = "pciAzimuth";

	/** The recipe pci map. */
	public static final String RECIPE_PCI_MAP = "recipePCIMap";

	/** The recipe pci map. */
	public static final String RECIPE_RSRP_MAP = "recipeRSRPMap";
	
	public static final String RECIPE_SINR_MAP = "recipeSINRMap";
	
	public static final String RECIPE_DL_MAP = "recipeDLMap";
	public static final String RECIPE_UL_MAP = "recipeULMap";
	public static final String RECIPE_JITTER_MAP = "recipeJitterMap";
	public static final String RECIPE_LATENCY_MAP = "recipeLatencyMap";
	public static final String RECIPE_BROWSE_MAP = "recipeBrowseMap";
	public static final String RECIPE_DL_CRITERIA_MAP = "recipeDLCriteriaMap";
	public static final String RECIPE_UL_CRITERIA_MAP = "recipeULCriteriaMap";
	public static final String RECIPE_MAX_SINR_MAP = "recipeMaxSINRMap";

	public static final String RECIPE_REMARK_MAP = "recipeRemarkMap";

	/** The recipe floorplan map. */
	public static final String RECIPE_FLOORPLAN_MAP = "recipeFloorplanMap";

	/** The recipe unit id map. */
	public static final String RECIPE_UNIT_ID_MAP = "recipeUnitIdMap";
	
	public static final String RECIPE_CELL_NAME_MAP = "recipeCellNameMap";

	/** The recipe unit id map. */
	public static final String RECIPE_OPERATOR_MAP = "recipeOperatorMap";

	/** The recipe custom geography map. */
	public static final String RECIPE_CUSTOM_GEOGRAPHY_MAP = "recipeCustomGeographyMap";
	public static final String REPORT_INSTACE_ID = "reportInstanceId";

	/** The site info. */
	public static final String SITE_INFO = "siteInfo";
   
	/** The geographyl1. */
	public static final String GEGRAPHY = "GEGRAPHY";
	
	/** The geography1. */
	public static final String GEOGRAPHYL1 = "geographyL1";

	/** The geographyl2. */
	public static final String GEOGRAPHYL2 = "geographyL2";

	/** The geographyl3. */
	public static final String GEOGRAPHYL3 = "geographyL3";

	/** The geographyl4. */
	public static final String GEOGRAPHYL4 = "geographyL4";

	/** The custom geography. */
	public static final String CUSTOM_GEOGRAPHY = "customGeography";

	/** The geographyl4 id. */
	public static final String GEOGRAPHYL4_ID = "geographyL4Id";

	/** The geographyl3 id. */
	public static final String GEOGRAPHYL3_ID = "geographyL3Id";

	/** The wo recipe mapping id. */
	public static final String WO_RECIPE_MAPPING_ID = "woRecipeMappingId";
public static final String SF_IMG_FILE_PATH="imgFilePath";
	
	
	/** The task id. */
	public static final String TASK_ID = "taskId";
	
	public static final String DATE = "date";
	
	public static final String DEVICE_ID = "deviceId";

	/** The close. */
	public static final String CLOSE = "Close";

	/** The workorder id. */
	public static final String WORKORDER_ID = "workorderId";

	/** The recipeList. */
	public static final String RECIPELIST = "recipeList";
	/** The workorder type. */
	public static final String WORKORDER_TYPE = "workorderType";

	/** The modified time. */
	public static final String MODIFIED_TIME = "modifiedTime";
	public static final String MODIFICATION_TIME = "modificationTime";

	/** The custom geography id. */
	public static final String CUSTOM_GEOGRAPHY_ID = "customGeographyId";

	/** The geography name. */
	public static final String GEOGRAPHY_NAME = "geographyName";

	/** The geography type. */
	public static final String GEOGRAPHY_TYPE = "geographyType";

	/** The geography id. */
	public static final String GEOGRAPHY_ID = "geographyId";

	/** The geography id. */
	public static final String ZOOM_LEVEL = "zoom";

	/** The operator name. */
	public static final String OPERATOR_NAME = "operatorName";

	/** The wo source. */
	public static final String WO_SOURCE = "woSource";

	/** The wo source. */
	public static final String BAND_TYPE = "bandType";
	
	/** The Constant BAND. */
	public static final String BAND = "band";

	/** The earfcn. */
	public static final String EARFCN = "earfcn";

	/** The Start Time. */
	public static final String START_TIME = "startTime";

	/** The Duration. */
	public static final String DURATION = "duration";

	/** The template. */
	public static final String TEMPLATE = "template";

	/** The workorder Id Filter. */
	public static final String WORKORDER_ID_FILTER = "workorderIdFilter";

	/** The exception something went wrong. */
	// Response Messages
	public static final String EXCEPTION_SOMETHING_WENT_WRONG = "There is some problem in completing the request.";

	/** The data not found. */
	public static final String DATA_NOT_FOUND = "Data not found for the request.";

	/** The invalid parameter json. */
	public static final String INVALID_PARAMETER_JSON = "{\"message\":\"Parameters given are invalid.\"}";

	/** The invalid limits json. */
	public static final String INVALID_LIMITS_JSON = "{\\\"message\\\":\\\"Invalid Limits.\\\"}";

	/** The success json. */
	public static final String SUCCESS_JSON = "{\"message\":\"Success\"}";

	/** The failure json. */
	public static final String FAILURE_JSON = "{\"message\":\"Failure\"}";

	public static final String FILE_NOT_FOUND_FOR_ID = "{\"message\":\"WO file not found for Recipe \"}";
	/** The Invalid Encryption Json json. */
	public static final String INVALID_ENCRYPTION_JSON = "{\"message\":\"Invalid Encryption.\"}";

	/** The workorder creation msg. */
	public static final String WORKORDER_CREATION_MSG = "{\"message\":\"Success\",\"Result\":\"Workorder Creation Count, Success : %d, Failure : %d\"}";

	/** The failure json. */
	public static final String FILE_IS_EMPTY_JSON = "{\"message\":\"WO Recipe File is empty\"}";

	/** The file is empty. */
	public static final String FILE_IS_EMPTY = "WO Recipe File is empty";

	/** The workorder already completed json. */
	public static final String WORKORDER_COMPLETED_JSON = "{\"message\":\"Workorder is already Completed\"}";
	
	/** The workorder already completed json. */
	public static final String FILE_NOT_AVAILABLE = "{\"message\":\"File not found for Recipe\"}";

	/** The workorder already completed json. */
	public static final String WORKORDER_NOT_AASIGNED_JSON = "{\"message\":\"Workorder is not assigned to you.\"}";

	/** The recipe already completed json. */
	public static final String RECIPE_COMPLETED_JSON = "{\"message\":\"Recipe is already Completed\"}";
	
	public static final String RECIPE_FILE_NOT_FOUND = "{\"message\":\"Recipe File not found\"}";


	/** The name. */
	public static final String NAME = "name";

	/** The workorder name. */
	public static final String WO_NAME = "woName";

	/** The name. */
	public static final String VALUE = "value";

	/** The name. */
	public static final String LANDMARK = "landmark";

	/** The name. */
	public static final String TYPE = "type";

	/** The boundary. */
	public static final String BOUNDARY = "boundary";

	/** The column family r. */
	public static final String COULMN_FAMILY_R = "r";

	/** The table custom boundary. */
	public static final String TABLE_CUSTOM_BOUNDARY = "TABLE_CUSTOM_BOUNDARY";

	/** The underscore. */
	// Character Constatnt
	public static final String UNDERSCORE = "_";

	/** The coulmn family live drive csv. */
	public static final String COULMN_FAMILY_LIVE_DRIVE_CSV = "r";

	/** The extension. */
	public static final String EXTENSION = ".csv";

	/** The csv name. */
	public static final String CSV_NAME = "NVCSV";

	/** The cent. */
	public static final Integer CENT = 100;

	/** The cluster name. */
	// Inbuilding Constants
	public static final String CLUSTER_NAME = "clusterName";

	/** The building id. */
	public static final String BUILDING_ID = "buildingId";

	/** The to use floorplan. */
	public static final String TO_USE_FLOORPLAN = "toUseFloorplan";

	/** The building name. */
	public static final String BUILDING_NAME = "buildingName";

	/** The building type. */
	public static final String BUILDING_TYPE = "buildingType";

	/** The floor id. */
	public static final String FLOOR_ID = "floorId";
	public static final String FLOOR_NAME = "floorName";

	/** The flat id. */
	public static final String FLAT_ID = "flatId";

	/** The flat name. */
	public static final String FLAT_NAME = "flatName";

	/** The wing id. */
	public static final String WING_ID = "wingId";

	/** The wing name. */
	public static final String WING_NAME = "wingName";

	/** The technology. */
	public static final String TECHNOLOGY = "technology";

	/** The description. */
	public static final String DESCRIPTION = "buildingDescription";

	/** The latitude. */
	public static final String LATITUDE = "latitude";

	/** The longitude. */
	public static final String LONGITUDE = "longitude";

	/** The report instance id. */
	public static final String REPORT_INSTANCE_ID = "reportInstanceId";

	/** The invalid parameters. */
	public static final String INVALID_PARAMETERS = "Invalid Parameters";

	/** The recipe category. */
	public static final String RECIPE_CATEGORY = "recipeCategory";

	/** The recipe id pk. */
	public static final String RECIPE_ID_PK = "recipeIdPk";

	/** The recipe id list. */
	public static final String RECIPE_ID_LIST = "recipeIdList";

	/** The permission name. */
	public static final String PERMISSION_NAME = "permissionName";

	/** The user workorder notification message. */
	public static final String USER_WORKORDER_NOTIFICATION_MESSAGE = "Hi! %s a new workorder of %s has been assigned to you.";

	/** The user workorder notification message. */
	public static final String WORKORDER_REASSIGN_NOTIFICATION_MESSAGE = "Hi! Workorder with ID %s has been reassigned.";

	/** The user workorder notification message. */
	public static final String WORKORDER_DELETE_NOTIFICATION_MESSAGE = "Hi! Workorder with ID %s has been deleted.";

	/** The device workorder notification message. */
	public static final String DEVICE_WORKORDER_NOTIFICATION_MESSAGE = "STEALTH";

	/** The stationary. */
	public static final String STATIONARY = "Stationary";

	/** The clot. */
	public static final String CLOT = "Cluster Optimization";

	/** The shakedown. */
	public static final String SHAKEDOWN = "Shakedown";

	/** The live drive. */
	public static final String LIVE_DRIVE = "Live Drive";

	/** The brti. */
	public static final String BRTI = "BRTI";

	/** The open drive. */
	public static final String OPEN_DRIVE = "Open Drive";

	/** The in building. */
	public static final String IN_BUILDING = "In Building";
	
	public static final String IN_BUILDING_WORKFLOW="In-Building Workflow";
	
	public static final String SSVTFULL="SSVT FULL";
	
	public static final String SSVTQUICK="SSVT QUICK";
	
	public static final String NV_CLOT="NV_CLOT";

	/** The mobile. */
	public static final String MOBILE = "Mobile";

	/** The server. */
	public static final String SERVER = "Server";

	/** The nv recipe. */
	public static final String NV_RECIPE = "NV_RECIPE";

	/** The nv default recipe map. */
	public static final String NV_DEFAULT_RECIPE_MAP = "NV_DEFAULT_RECIPE_MAP";

	/** The nv default recipe map. */
	public static final String NV_VALID_MCC_MNC_LIST = "NV_VALID_MCC_MNC_LIST";

	/** The file path. */
	public static final String FILE_PATH = "filePath";

	/** The file path. */
	public static final String FILE_PATH_JSON = "{\"filePath\":\"%s\"}";

	/** The content disposition prefix. */
	public static final String CONTENT_DISPOSITION_PREFIX = "attachment; filename=\"";

	/** The content disposition suffix. */
	public static final String CONTENT_DISPOSITION_SUFFIX = "\"";

	/** The input stream end bytes. */
	public static final Integer INPUT_STREAM_END_BYTES = 0;

	/** The stream buffer size. */
	public static final Integer STREAM_BUFFER_SIZE = 1024;

	/** The input stream start bytes. */
	public static final Integer INPUT_STREAM_START_BYTES = 0;

	/** The file directory seperator. */
	public static final String FILE_DIRECTORY_SEPERATOR = "/";

	/** The recipe mapping id. */
	public static final String RECIPE_MAPPING_ID = "recipeMappingId";

	/** The report instance id json. */
	public static final String REPORT_INSTANCE_ID_JSON = "{\"analyticsrepositoryid_pk\": %d}";

	/** The ssvt prefix. */
	public static final String SSVT_PREFIX = "SSV";
	
	public static final String SF_SSVT_PREFIX = "SFSSVQUICK";
	public static final String SF_SSVT_PREFIX_FULL = "SFSSVFULL";
	public static final String NV_SSVT_IBC_QUICK_PREFIX = "IBC-QUICK";
	public static final String NV_SSVT_IBC_FULL_PREFIX = "IBC-FULL";

	/** The live drive prefix. */
	public static final String LIVE_DRIVE_PREFIX = "LDRIVE";

	/** The clot prefix. */
	public static final String CLOT_PREFIX = "CLOT";

	/** The stationary prefix. */
	public static final String STATIONARY_PREFIX = "SN";

	/** The open drive prefix. */
	public static final String OPEN_DRIVE_PREFIX = "ODRIVE";

	/** The brti prefix. */
	public static final String BRTI_PREFIX = "BRTI";

	/** The benchmark prefix. */
	public static final String BENCHMARK_PREFIX = "BENCHMARK";

	/** The in building prefix. */
	public static final String IN_BUILDING_PREFIX = "INBD";

	/** The customer complaints prefix. */
	public static final String CUSTOMER_COMPLAINTS_PREFIX = "CUSTCOMPL";

	/** The recipe parsing template name. */
	public static final String RECIPE_PARSING_TEMPLATE_NAME = "NVQMDL";

	/** The Constant KEY_MODULE_NAME. */
	public static final String KEY_MODULE_NAME = "moduleName";

	/** The Constant KEY_NOTIFICATION_MESSAGE. */
	public static final String KEY_NOTIFICATION_MESSAGE = "message";

	/** The Constant KEY_NOTIFICATION_DEVICEID. */
	public static final String KEY_NOTIFICATION_DEVICEID = "deviceId";

	/** The Constant KEY_NOTIFICATION_WORKORDERID. */
	public static final String KEY_NOTIFICATION_WORKORDERID = "workOrderId";
	
	public static final String KEY_NOTIFICATION_WORKORDER = "workOrder";

	
	public static final String KEY_WO_COMPLETION_TIME="workorderCompletionTime";
	
	public static final double EARTH_RADIUS = 6371;

	public static final String KEY_LAT_LONG_DISTANCE = "distance";

	public static final String KEY_LATITUDE = "latitude";

	public static final String KEY_LONGITUDE = "longitude";

	public static final String KEY_NORTH_EAST = "northEast";

	public static final String KEY_SOUTH_WEST = "southWest";

	public static final Integer DEFAULT_SITES_ZOOMLEVEL = 11;

	public static final String COMPLAINER_NAME = "complainerName";

	public static final String COMPLAINER_ADDRESS = "complainerAddress";

	public static final String COMPLAINT_DATE = "complaintDate";

	public static final String COMPLAINT_REASON = "complaintReason";

	public static final Integer FILE_BUFFER_SIZE = 1024;

	public static final Integer OUTPUT_STREAM_START_OFFSET = 0;

	public static final Integer DATA_STREAM_END_VALUE = 0;

	public static final String ZOOM_18 = "18";

	public static final String INVALID_USER = "\"result\":\"Error in getting user from Context\"";

	public static final Integer FILE_PART_INDEX = 6;

	public static final String EMPTY_RESULT_JSON = "{\"result\":[]}";

	public static final String RESULT = "result";

	public static final String FILE_NOT_FOUND = "\"result\":\"File Not Found \"";

	/** The result success json. */
	public static final String RESULT_SUCCESS_JSON = "{\"result\":\"Success\"}";

	/** The result failure json. */
	public static final String RESULT_FAILURE_JSON = "{\"result\":\"Failure\"}";

	/** The result invalid parameter json. */
	public static final String RESULT_INVALID_PARAMETER_JSON = "{\"result\":\"Parameters given are invalid.\"}";

	/** The result something went wrong json. */
	public static final String RESULT_START = "{\"result\":\"";
	public static final String RESULT_END = "\"}";
	public static final String RESULT_EXCEPTION_SOMETHING_WENT_WRONG_JSON = RESULT_START + EXCEPTION_SOMETHING_WENT_WRONG + RESULT_END;

	public static final String WIFI = "WIFI";

	public static final String LTE = "LTE";

	public static final String KEY_SEARCH = "search";

	public static final String RECEIPE_ID = "receipeId";

	public static final String OPERATOR = "operator";

	public static final String QUARTER = "quarter";
	public static final String YEAR = "year";

	public static final String START_TIMESTAMP = "startTimestamp";
	public static final String END_TIMESTAMP = "endTimestamp";

	public static final String GEOGRAPHY_ID_LIST = "geographyIdList";

	public static final String REASSIGNED_STATUS = "REASSIGNED";

	public static final String DELETED_STATUS = "DELETED";
	
	public static final String COMPLETED_STATUS = "COMPLETED";

	public static final String WORKORDER_DASHBOARD_HEADER = "Workorder ID,Name,Status,Workorder Type,Assigned To,Creationtime,Completion Percentage,Zone,Sales Region,Province,Network Cluster";

	public static final String NEWL_LINE = "\n";
	public static final String CREATION_TIME = "creationTime";
	public static final String END_TIME = "endTime";

	public static final String SUCCESS = "Success";
	
	public static final String PREDEFINED = "PREDEFINED";

	/** Workorder summary Dashboard constants. */
	public static final String TYPE_INBUILDING = "IN_BUILDING";
	public static final String TYPE_DRIVE = "DRIVE";
	public static final String TEMPLATE_TYPE_ALL = "ALL";
	public static final String ASSIGNED_TYPE_USER = "User";
	public static final String ASSIGNED_TYPE_DEVICE = "Device";
	public static final String DD_MM_YY = "dd-MM-yy";
	public static final String CAPS_DD_MM_YY = "dd-MMM-YYYY";
	public static final String TEMPLATE_TYPE = "templateType";
	public static final String TYPE_ADHOC = "Adhoc";
	public static final String TYPE_WORKORDER = "Workorder";
	public static final String DRIVE_COUNT = "DRIVE_COUNT";
	public static final String IN_BUILDING_COUNT = "IN_BUILDING_COUNT";

	public static final String SSVT = "SSVT";
	public static final String ADHOC_BRTI = "ADHOC BRTI";
	public static final String ADHOC_IN_BUILDING = "ADHOC InBuilding";
	public static final String ADHOC_LIVE_DRIVE = "ADHOC Live Drive";
	public static final String ADHOC_OPEN_DRIVE = "ADHOC Open Drive";
	public static final String BENCHMARK = "Benchmark";
	public static final String CLUSTER_OPTIMIZATION = "Cluster Optimization";
	public static final String CUSTOMER_COMPLAINTS = "Cutomer Complaints";
	public static final String IB_ADHOC_BENCHMARK = "ADHOC InBuilding Benchmark";
	public static final String INBUILDING = "InBuilding";
	public static final String STEALTH = "STEALTH";
	public static final String KEY_NOTIFICATION_STEALTH_TASK="stealthTaskId";
	public static final String DEVICE_NOT_FOUND="Devices Not found";
	public static final String IMSI = "imsi";
	public static final String IMEI = "imei";
	public static final String INVALID_GEOGRAPHY="Invalid Geography";
	/**
	 * SF-SSVT Constants
	 */
	public static final String VENDOR_PM_USERS="vendorPMUsers";
	public static final String RF_MANAGER_USERS="rfManagerUsers";
	public static final String RFE_ADMIN_USERS ="rfeAdminUsers";
	public static final String VENDOR_PM_USER_NAMES="vendorPMUserNames";
	public static final String RF_MANAGER_USER_NAMES="rfManagerUserNames";
	public static final String HQ_USER_NAMES="hqUserNames";
	
	public static final String LEVEL_TYPE="LEVEL_TYPE";
	public static final String SSVT_LEVEL_TYPE="SSVT_LEVEL_TYPE";
	public static final String ROLE_RF_MANAGER = "ROLE_RF_MANAGER";
	public static final String ROLE_VENDOR_PM = "ROLE_VENDOR_PM";
	public static final String ROLE_RFE_ADMIN = "ROLE_RFE_ADMIN";
	public static final String HQ_MANGER_ROLE_NAME="HQ_MANGER_ROLE_NAME";
	public static final String NV_DEFAULT_SF_SSVT_RECIPE_MAP="NV_DEFAULT_SF_SSVT_RECIPE_MAP";
	public static final String ACTION_CREATE="create";
	public static final String ACTION_PERFORM="perform";
	public static final String NHQ_LEVEL="NHQ";
	public static final String IS_FULL_STATISFIED_FROM_QUICK="isFullSatisfied";
	public static final String HQ_USERS_EMAILS="hqUsers";
	public static final String SLA_TIME="slaTime";
	public static final String VENDOR="vendor";
	public static final String REPORT="Report";

	public static final String SITE_INTEGRATION_ACK_URL = "SITE_INTEGRATION_ACK_URL";
	/**
	 * In-Building Automated WO Flow contants
	 */
	public static final String KEY_IS_FLOORPLAN_APPROVED = "isFloorPlanApproved";
	public static final String WORKORDER_NO="workorderNo";
	public static final String KEY_SELECTED_UNIT_IDS="selectedUnitIds";
	public static final String WO_MAP_TYPE="woMapType";
	public static final Object WO_ROUTE_TYPE = "woRouteType";
	public static final String  RECIPE="Recipe";
    public static final String SITELATITUDE="siteLatitude";
    public static final String SITELONGITUDE="siteLongitude";
    public static final String SITENAME="siteName";

    public static final String KPI_RRC_CONNECTION_SUCCESS="RRC Connection Success Rate";
    public static final String KPI_HANDOVER_SUCCESS_RATE="Handover Success Rate";
    public static final String KPI_ERAB_DROP_RATE="ERAB Drop Rate";
    public static final String KPI_VOLTE_SETUP_SUCCESS_RATE="VoLTE Setup Success Rate";
    public static final String KPI_PEAK_VALUE="Peak Value (Single Sample)";
	public static final String QUICKWORKORDER_ID = "quickWorkorderId";
	public static final String QUICKWORKORDER_ANALYTICS_REPOSITORY_ID="quickAnalyticsRepositoryId";
	public static final String BAND_2300 = "2300";
	public static final String BAND_850 = "850";

	public static final String NV_SSVT = "NV_SSVT";

	public static final String SSVT_TEMPLATE_TYPE_JSON = "{'name':'SSVT','value':'NV_SSVT'}";

	public static final String WORKORDER_WRAPPER = "WORKORDER_WRAPPER";
	
	public static final String WOID_JSON_KEY="\"workorderId\": ";
	public static final String RECIPEID_JSON_KEY="\"recipeId\": ";
	public static final String MULTI_USER = "multiUser";

	public static final String SCANNER_REQUEST = "scannerRequest";
	public static final String CHANNEL_LIST = "channelList";

	public static final String IS_SITE_ACCEPTANCE = "isSiteAcceptance";

	public static final String PROJECT_ID = "projectId";

	public static final String TASK_STATUS = "taskStatus";

	public static final String BAND_1800 = "1800";

	public static final String FDD3 = "FDD3";

	public static final String ASSIGNED_BY = "assignedBy";

	public static final String RECIPE_LIST = "recipeList";

	public static final String DUE_DATE = "dueDate";

	public static final Integer DEFAULT_RECIPE_RSRP_THRESHOLD_VALUE = -140;

	public static final Integer DEFAULT_RECIPE_SINR_THRESHOLD_VALUE = -18;

	public static final String FAILURE = "Failure";
	
	public static final String SCANNING_REQUEST_MESSAGE = "Scanning";
	public static final String KEY_GEOGRAPHY_L1_NAME ="gographyL1Name";

	public static final String DESCRIPTION_KEY ="description";
	
	public static final String SSVT_IBC_WORKORDER_DESCRIPTION ="SSVT IBC Automation Workorder";
	

	public static final String OLD_VENDR_ID="oldVendorId";
	
	public static final String RC_PRE_STN_RRC_IBC = "RC-PRE-STN-RRC-IBC";
	
	public static final String RC_PRE_STN_SC_IBC = "RC-PRE-STN-SC-IBC";
	
	public static final String RC_PRE_STN_PING_IBC = "RC-PRE-STN-PING-IBC";
	
	public static final String WALK_TEST_REPORT_INSTACE_ID = "walkTestReportInstanceId";

	public static final String SSVT_1C = "SSVT_1C";
	public static final String SSVT_1E = "SSVT_1E";
	public static final String IB_1C = "IB_1C";
	public static final String IB_1E = "IB_1E";
	public static final String SSVT_SF_DOCUMENT_NAME="documentName";
	public static final String SSVT_SF_DOCUMENT_PATH="documentPath";
	/**
     * END
     */

	public static final String HIBERNATE_ESCAPE_CHAR = "\\";
	
	public static final String BUFFER_SIZE = "bufferSize";
	
	public static final String WO_STATUS = "woStatus";
	public static final String NETYPE="netype";

}

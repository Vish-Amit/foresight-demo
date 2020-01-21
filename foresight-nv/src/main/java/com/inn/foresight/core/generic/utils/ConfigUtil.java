package com.inn.foresight.core.generic.utils;

import com.inn.commons.configuration.ConfigUtils;

/**
 * The Class ConfigUtil.
 */
public class ConfigUtil {

    /** The Constant CONFIG_PROPERTIES. */
    public static final String CONFIG_PROPERTY = "config.properties";

    /** The Constant APP_PROPERTIES. */
    public static final String APPLICATION_PROPERTY = "application.properties";

    public static final String UM_USER_PREFERENCE = "UM_USER_PREFERENCE";

    public static final String UM_APPLICATION_URL = "UM_APPLICATION_URL";

    public static final String UM_CREATE_EMAIL_URL = "UM_CREATE_EMAIL_URL";

    public static final String TAC_EXCEL_MACRO_TEMPLET = "TAC_EXCEL_MACRO_TEMPLET";

    public static final String UM_EMAIL_LOGO = "UM_EMAIL_LOGO";

    public static final String UM_EMAIL_SUBJECT = "UM_EMAIL_SUBJECT";

    public static final String UM_EMAIL_BCC = "UM_EMAIL_BCC";
    public static final String UM_SEND_EMAIL_TO_NEW_USER_ENABLED = "UM_SEND_EMAIL_TO_NEW_USER_ENABLED";

    /** The Constant IS_ENCRYPTION_EANBLED. */
    public static final String IS_ENCRYPTION_EANBLED = "IS_ENCRYPTION_EANBLED";

    /** The Constant APP_ACCESS_URL. */
    public static final String APP_ACCESS_URL = "APP_ACCESS_URL";

    /** The Constant TIMEOUT_VALUE. */
    public static final String TIMEOUT_VALUE = "TIMEOUT_VALUE";

    /** The Constant SANSA_SPECIFIC_DATE. */
    public static final String SANSA_SPECIFIC_DATE = "SANSA_SPECIFIC_DATE";

    public static final String ATTACHMENT_SERVICE_AVAILABLE = "ATTACHMENT_SERVICE_AVAILABLE";
    public static final String DOWNLOAD_AUTH_PATH_AVAILABLE = "DOWNLOAD_AUTH_PATH_AVAILABLE";

    /** The Constant SANSA_DATE. */
    public static final String SANSA_DATE = "SANSA_DATE";

    /** The Constant TRAINEDFILE_LOCATION. */
    public static final String TRAINEDFILE_LOCATION = "TRAINEDFILE_LOCATION";

    /** The Constant NLP_FILE_PATH. */
    public static final String NLP_FILE_PATH = "NLP_FILE_PATH";

    /** The Constant BPMN_SA_WO_TEMPLATE. */
    // SA-NSA
    public static final String BPMN_SA_WO_TEMPLATE = "BPMN_SA_WO_TEMPLATE";

    /** The Constant BPMN_NSA_WO_TEMPLATE. */
    public static final String BPMN_NSA_WO_TEMPLATE = "BPMN_NSA_WO_TEMPLATE";

    /** The Constant CM_BPMN_WO_CATEGORY. */
    public static final String CM_BPMN_WO_CATEGORY = "CM_BPMN_WO_CATEGORY";

    /** The Constant SOMETHING_WENT_WRONG. */
    public static final String SOMETHING_WENT_WRONG = "Something went wrong.";

    /** The Constant DROPWIZARD_PM_URL. */
    public static final String DROPWIZARD_PM_URL = "DROPWIZARD_PM_URL";

    /** The Constant LIVE_DRIVE_TOPIC. */
    public static final String LIVE_DRIVE_TOPIC = "liveDriveTopic";
    /** The Constant SNAP_SHOT_URL. */
    public static final String SNAP_SHOT_URL = "SNAP_SHOT_URL";

    /** The Constant LIVE_DRIVE_REMAINING_TOPIC. */
    public static final String LIVE_DRIVE_REMAINING_TOPIC = "liveDriveRemainingTopic";

    /** The Constant META_BROKER_LIST. */
    public static final String META_BROKER_LIST = "metadataBrokerList";

    /** The Constant CLIENT_ID */
    public static final String CLIENT_ID = "client.id";

    /** The Constant PRODUCER_TYPE. */
    public static final String PRODUCER_TYPE = "producerType";

    /** The Constant SERIALIZER_CLASS_STRING. */
    public static final String KEY_SERIALIZER = "key.serializer";

    /** The Constant Value SERIALIZER CLASS */
    public static final String VALUE_SERIALIZER = "value.serializer";

    /** The Constant PARTITIONER_CLASS. */
    public static final String PARTITIONER_CLASS = "partitionerClass";

    /** The Constant REQUEST_REQUIRED_ACK. */
    public static final String REQUEST_REQUIRED_ACK = "requestRequiredAcks";

    /** The Constant AUDIT_REPORT_PATH. */
    public static final String AUDIT_REPORT_PATH = "AUDIT_REPORT_PATH";

    /** The Constant ATTACHMENT_URL_TEMP. */
    public static final String ATTACHMENT_URL_TEMP = "ATTACHMENT_URL_TEMP";

    public static final String LSR_SEARCH_FOR_IMSI_ANALYTICS = "LSR_SEARCH_FOR_IMSI_ANALYTICS";

    /** The Constant LIVE_DRIVE_ACTIX_BASE_URL. */
    public static final String LIVE_DRIVE_ACTIX_BASE_URL = "LIVE_DRIVE_ACTIX_BASE_URL";

    public static final String TOPIC = "TOPIC";

    /** The Constant LIVE_DRIVE_COLUMN_FAMILY. */
    public static final String LIVE_DRIVE_COLUMN_FAMILY = "liveDriveColumnFamily";

    /** The Constant LIVE_DRIVE_TABLE_NAME. */
    public static final String LIVE_DRIVE_TABLE_NAME = "liveDriveTableName";

    /** The Constant ZOOKEEPER_SLEEP_TIMEOUT. */
    public static final int ZOOKEEPER_SLEEP_TIMEOUT = 5000;

    /** The Constant MITIGATION_VISUALISATION_DATA_URL. */
    public static final String MITIGATION_VISUALISATION_DATA_URL = "MITIGATION_VISUALISATION_DATA_URL";

    public static final String HBASE_DB = "HBASE_DB";

    public static final String COMBINEHOURLYPM = "COMBINEHOURLYPM";
    public static final String COMBINEDAILYPM = "COMBINEDAILYPM";

    public static final String GET_CELL_DATA_FOR_PMDASBORD = "GET_CELL_DATA_FOR_PMDASBORD";
    public static final String GET_KPI_CELL_DATA_FOR_PMDASBORD = "GET_KPI_CELL_DATA_FOR_PMDASBORD";
    public static final String GET_KPI_GEO_LEVEL_DATA_FOR_RANDAILYDASBORD = "GET_KPI_GEO_LEVEL_DATA_FOR_RANDAILYDASBORD";
    public static final String GET_CELL_DATE_FOR_HOURLY = "GET_CELL_DATE_FOR_HOURLY";
    public static final String GET_CELL_DATE_FOR_DAILY = "GET_CELL_DATE_FOR_DAILY";

    public static final String GET_CELL_DATE_FORMATTED_FOR_HOURLY = "GET_CELL_DATE_FORMATTED_FOR_HOURLY";
    public static final String GET_CELL_DATE_FORMATTED_FOR_DAILY = "GET_CELL_DATE_FORMATTED_FOR_DAILY";

    public static final String VENDOR_CODE_SAMSUNG = "VENDOR_CODE_SAMSUNG";
    ///
    public static final String GET_PMDASHBOARD_DATE_FOR_HOURLY = "GET_PMDASHBOARD_DATE_FOR_HOURLY";
    public static final String GET_PMDASHBOARD_DATE_FOR_DAILY = "GET_PMDASHBOARD_DATE_FOR_DAILY";

    public static final String GEOGRAPHYL3_TABLENAME = null;

    public static final String GEOGRAPHYL2_TABLENAME = null;

    /** The Constant IS_DROPWIZARD_DBFCOLORMAP_URL.. */
    public static final String DROPWIZARD_DBFCOLORMAP_URL = "DROPWIZARD_DBFCOLORMAP_URL";

    public static final String GOOGLE_MAP_KEY = "GOOGLE_MAP_KEY";

    public static final String IMAGE_PATH_FOR_NV_REPORT = "IMAGE_PATH_FOR_NV_REPORT";

    public static final String SSVTREPORT_JASPER_FOLDER_PATH = "SSVTREPORT_JASPER_FOLDER_PATH";

    public static final String SENT_NOTIFICATION_RETRY_COUNT = "SENT_NOTIFICATION_RETRY_COUNT";

    public static final String EMAIL_ID = "EMAIL_ID";

    public static final String HOST_NAME = "HOST_NAME";

    public static final String SMTP_PORT = "SMTP_PORT";

    /** Process Runner Constants **/
    public static final String SSVT_REPORT_PATH = "SSVT_REPORT_PATH";

    public static final String CLOTREPORT_JASPER_FOLDER_PATH = "CLOTREPORT_JASPER_FOLDER_PATH";

    /** Process Runner Constants **/
    public static final String PROCESS_RUNNER_GET_ALL_JOBS = "PROCESS_RUNNER_GET_ALL_JOBS";

    public static final String UM_SITEFORGE_GUEST_ROLE_ID = "UM_SITEFORGE_GUEST_ROLE_ID";

    /**
     * FM_ALARM_HISTORY_TEMPLATE_PATH Constant
     */
    public static final String FM_ALARM_HISTORY_TEMPLATE_PATH = "FM_ALARM_HISTORY_TEMPLATE_PATH";
    
    public static final String RAN_OUTAGE_DASHBOARD_TEMPLATE_PATH = "RAN_OUTAGE_DASHBOARD_TEMPLATE_PATH";

    /** The Constant WPT_TABLE_NAME. */
    public static final String WPT_TABLE_NAME = "WPT_TABLE_NAME";

    /** Tribe **/
    public static final String TRIBE_BASE_URL = "TRIBE_BASE_URL";
    public static final String DOC_PATH_TRIBE = "DOC_PATH_TRIBE";
    public static final String FILE_UPLOADS_TRIBE_URL = "FILE_UPLOADS_TRIBE_URL";
    public static final String DELETE_FILE_UPLOADS_TRIBE_URL = "DELETE_FILE_UPLOADS_TRIBE_URL";
    public static final String FILE_SIZE_TRIBE_IN_MB = "FILE_SIZE_TRIBE_IN_MB";

    public static final String BULK_UPLOAD_USER_CREATION_FILE_PATH = "BULK_UPLOAD_USER_CREATION_FILE_PATH";
    public static final String BULK_UPLOAD_LOCAL_PATH = "BULK_UPLOAD_LOCAL_PATH";

    public static final String COUNTER_CATEGORY_REPORT_PATH = "COUNTER_CATEGORY_REPORT_PATH";

    public static final String MICROWAVE_FILE_PATH = "MICROWAVE_FILE_PATH";

    public static final String MICROWAVE_FTS_FILE_PATH = "MICROWAVE_FTS_FILE_PATH";

    /** The Constant ALGO_PROPERTY_MAIL_RECIPIENTS. */
    public static final String ALGO_PROPERTY_MAIL_RECIPIENTS = "ALGO_PROPERTY_RECIPIENTS";
    public static final String DROPWIZARD_BOUNDARY_GEOGRAPHY_URL = "DROPWIZARD_BOUNDARY_GEOGRAPHY_URL";

    public static final String MICRO_SERVICE_BASE_URL = "MICRO_SERVICE_BASE_URL";

    public static final String MD5_KEY = "MD5_KEY";

    public static final String DROPWIZARD_KML_PROCESSOR_URL = "DROPWIZARD_KML_PROCESSOR_URL";

    public static final String KML_FILE_UPLOAD_PATH = "KML_FILE_UPLOAD_PATH";

    public static final String KML_FILE_PATH = "KML_FILE_PATH";

    public static final String KML_FILE_ATTACHMENT_PATH = "KML_FILE_ATTACHMENT_PATH";

    public static final String KML_FILE_DROPWIZARD_URL = "KML_FILE_DROPWIZARD_URL";

    /** The Constant KML_FILE_REPORTING_URL. */
    public static final String KML_FILE_REPORTING_URL = "KML_FILE_REPORTING_URL";

    public static final String GETSITECOLORBYKPIVALUE_URL = "GETSITECOLORBYKPIVALUE";

    public static final String TAC_EXCEL_TEMPLET = "TAC_EXCEL_TEMPLET";

    public static final String TAC_ANALYSIS_EXCEL_PATH = "TAC_ANALYSIS_EXCEL_PATH";

    public static final String PLANNED_SITE_REPORT_PATH = "PLANNED_SITE_REPORT_PATH";

    public static final String SITE_REPORT_DOWNLOAD_PATH = "SITE_REPORT_DOWNLOAD_PATH";

    public static final String BBM_FILE_PATH = "BBM_FILE_PATH";

    private static String googleUrlStringPrefix = "googleUrlStringPrefix";

    private static String googleUrlStringSuffix = "googleUrlStringSuffix";

    private static String googleKeyString = "googleKeyString";

    /** The Constant For AzenQos. */
    public static final String BASE_ZIP_DIRECTORY = "BASE_ZIP_DIRECTORY";

    public static final String HBASE_TABLE_NAMESPACE = "HBASE_TABLE_NAMESPACE";

    /** The Constant For Attachment. */
    public static final String ATTACHMENT_FILE_PATH = "ATTACHMENT_FILE_PATH";

    public static final String ATTACHMENT_SHARED_DRIVE_PATH = "ATTACHMENT_SHARED_DRIVE_PATH";

    public static final String HDFS_HBASE_INDEXING_PATH = "HDFS_HBASE_INDEXING_PATH";

    public static final String HDFS_PATH = "HDFS_PATH";
    public static final String STEALTH_DATA_TABLE = "STEALTH_DATA_TABLE";

    public static final String REFRENCE_LATITUDE_FOR_ADDRESS = "REFRENCE_LATITUDE_FOR_ADDRESS";
    public static final String REFRENCE_LONGTITUDE_FOR_ADDRESS = "REFRENCE_LONGTITUDE_FOR_ADDRESS";
    public static final String GRID_SIZE_FOR_ADDRESS = "GRID_SIZE_FOR_ADDRESS";
    public static final String ZOOM_LEVEL_FOR_ADDRESS = "ZOOM_LEVEL_FOR_ADDRESS";
    public static final String INBUILDING_TEMP_PATH = "INBUILDING_TEMP_PATH";

    public static final String FLOOR_PLAN_TABLE = "FLOOR_PLAN_TABLE";

    public static final String MASTER_LIVE_DRIVE_JASPER = "MASTER_LIVE_DRIVE_JASPER";

    public static final String WATCHMAN_DATA_TABLE = "WATCHMAN_DATA_TABLE";
    public static final String LAYER3_BEAN_TIME = "LAYER3_BEAN_TIME";
    public static final String QMDL_EVENT_TABLE = "QMDL_EVENT_TABLE";
    public static final String ASN_COMPILER_PATH = "ASN_COMPILER_PATH";
    /** The qmdl signaling data table. */
    public static final String QMDL_SIGNALING_DATA_TABLE = "QMDL_SIGNALING_DATA_TABLE";

    /** The qmdl data table. */
    public static final String QMDL_DATA_TABLE = "QMDL_DATA_TABLE";
    public static final String NV_WORKORDER_TAB_FILE_BASE_PATH = "NV_WORKORDER_TAB_FILE_BASE_PATH";
    public static final String NV_WORKORDER_TAB_FILE_PARSE_SCRIPT = "NV_WORKORDER_TAB_FILE_PARSE_SCRIPT";

    public static final String GEOGRAPHYL4_TABLE = "GEOGRAPHYL4_TABLE";
    public static final String GEOGRAPHYL3_TABLE = "GEOGRAPHYL3_TABLE";
    public static final String GEOGRAPHYL2_TABLE = "GEOGRAPHYL2_TABLE";
    public static final String GEOGRAPHYL1_TABLE = "GEOGRAPHYL1_TABLE";

    public static final String ACTUAL_HEADERS_FOR_CREATE_OR_UPDATE_USER = "ACTUAL_HEADERS_FOR_CREATE_OR_UPDATE_USER";

    public static final String ACTUAL_HEADERS_FOR_CREATE_OR_UPDATE_CUSTOMERCARE_USER = "ACTUAL_HEADERS_FOR_CREATE_OR_UPDATE_CUSTOMERCARE_USER";

    /** The qmdl kpi table. */
    public static final String QMDL_KPI_TABLE = "QMDL_KPI_TABLE";

    public static final String IS_SSO_ENABLED = "IS_SSO_ENABLED";

    public static final String IS_TWO_FACTOR_AUTHENTICATION_ENABLED = "IS_TWO_FACTOR_AUTHENTICATION_ENABLED";

    public static final String MVCODE = "MVCODE";

    public static final String FORGOT_PASSWORD_EMAIL_SUBJECT = "FORGOT_PASSWORD_EMAIL_SUBJECT";

    public static final String BULKUPLOAD_TEMPLATE_PATH = "BULKUPLOAD_TEMPLATE_PATH";
    public static final String BULKUPLOAD_TEMPLATE_PATH_UPDATE = "BULKUPLOAD_TEMPLATE_PATH_UPDATE";
    public static final String BULKUPLOAD_TEMPLATE_PATH_CUSTOMERCARE = "BULKUPLOAD_TEMPLATE_PATH_CUSTOMERCARE";
    public static final String BULKUPLOAD_APPLICATION_TYPE = "BULKUPLOAD_APPLICATION_TYPE";
    public static final String IS_ACCESS_LEVE_ENABLED = "IS_ACCESS_LEVE_ENABLED";
    /** ALARM_LIBRARY_TEMPLATE_PATH */
    public static final String ALARM_BULKUPLOAD_TEMPORARY_PATH = "ALARM_BULKUPLOAD_TEMPORARY_PATH";

    
    /** IPAM **/
    public static final String IPAM_SYSTEM_USER = "ipam.system.user";
    
	public static final String IPAM_REQUEST_APPROVER_ROLE = "ipam.role.requestapproval";
	
	public static final String IPAM_REQUESTER_EMAIL_ID = "ipam.requester";
	
	public static final String IPAM_EXTERNAL_USERNAME = "ipam.external.username";
	
	public static final String IPAM_HOST_ADDRESS = "ipam.host.address";
	
	public static final String IPAM_SYSTEM_PASSWORD = "ipam.system.password";

    public static final String IPAM_REPORT_TEMPLATE_PATH = "ipam.path.reporttemplate";

    public static final String IPAM_DATA_BATCH_SIZE = "ipam.size.databatch";
    
    public static final String IP_CONFIG_TEMPLATE_VM_MAIL_PATH = "ipam.path.ipconfig.vm.mail";

   public static final String NETWORK_NEID_MAP="NETWORK_NEID_MAP";
    
    public static String getConfigProp(String key) {
        return ConfigUtils.getString(key);
    }

}

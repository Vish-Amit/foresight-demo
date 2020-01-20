package com.inn.foresight.module.nv.app.constants;

import com.inn.commons.lang.NumberUtils;

/**
 * The Class AppConstants.
 */
public class AppConstants {
	
	/**
	 * Instantiates a new app constants.
	 */
	protected AppConstants () {	}

	/** The Constant MOBILE_AUTH. */
	public static final String MOBILE_AUTH = "mobileAppAuth";

	/** The Constant APK_DETAILS_BY_NAME. */
	public static final String APK_DETAILS_BY_NAME = "apkDetailByName";

	/** The Constant APK_NAME. */
	public static final String APK_NAME = "apkName";

	/** The Constant USERNAME. */
	public static final String USERNAME = "username";

	/** The Constant CHECKSUM. */
	public static final String QUERY_PARAM_PW = "password";

	/** The Constant EMPTY_STRING. */
	public static final String EMPTY_STRING = "";

	/** The Constant SPACE. */
	public static final String SPACE = " ";

	/** The Constant CURLEY_BRACES. */
	public static final String CURLEY_BRACES = "{}";

	/** The Constant COMMA. */
	public static final String COMMA = ",";

	/** The Constant TRUE_STRING. */
	public static final String TRUE_STRING = "true";

	/** The Constant STRING_NULL. */
	public static final String STRING_NULL = null;

	/** The Constant SPRING_SECURITY_CONTEXT. */
	public static final String SPRING_SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";

	/** The Constant CLOSE_CURELY_BRACE. */
	public static final String CLOSE_CURELY_BRACE = "}";

	/** The Constant DEVICE_ID_PREFIX. */
	public static final String DEVICE_ID_PREFIX = "DC";

	/** The Constant LICENCE_ID_PREFIX. */
	public static final String LICENCE_ID_PREFIX = "LC";

	/** The Constant APK_ID_STR. */
	public static final String APK_ID_STR = "apkId";

	/** The Constant APP_OS_STR. */
	public static final String APP_OS_STR = "appOS";

	/** The Constant VERSION_NAME. */
	public static final String VERSION_NAME = "versionName";

	/** The Constant IMEI. */
	public static final String IMEI = "imei";

	/** The Constant IMSI. */
	public static final String IMSI = "imsi";

	/** The Constant DEVICE_OS. */
	public static final String DEVICE_OS = "deviceOS";

	/** The Constant LICENCE_ID. */
	public static final String LICENCE_ID = "licenceId";

	/** The Constant APPNAME. */
	public static final String APPNAME = "appName";

	/** The Constant CLIENTNAME. */
	public static final String CLIENTNAME = "clientName";

	/** The Constant LICENSEMASTER. */
	public static final String LICENSEMASTER = "licenseMaster";

	/** The Constant IS_ENCRYPTION_MOBILE. */
	// config Constanst
	public static final String IS_ENCRYPTION_MOBILE = "IS_ENCRYPTION_MOBILE";

	/** The Constant USERNAME_CHECKSUM_LOGGER. */
	// loggers
	public static final String USERNAME_CHECKSUM_LOGGER = USERNAME + SPACE + CURLEY_BRACES;

	/** The Constant EXCEPTION_LOGGER. */
	public static final String EXCEPTION_LOGGER = "Getting Exception With Trace {}";

	/** The Constant NULL_ERROR_LOGGER. */
	public static final String NULL_ERROR_LOGGER = "Getting NullPointerException With Trace {}";

	/** The Constant RESULT_SIZE_LOGGER. */
	public static final String RESULT_SIZE_LOGGER = "Getting Result Size {}";

	/** The Constant NO_RECORD_FOUND_LOGGER. */
	public static final String NO_RECORD_FOUND_LOGGER = "No record found in table for the given details.";

	/** The Constant DATA_NOT_FOUND. */
	public static final String DATA_NOT_FOUND = "No data found for the given details.";

	/** The Constant SOMETHING_WENT_WRONG. */
	public static final String SOMETHING_WENT_WRONG = "There is some problem on getting data for the request";

	/** The Constant INVALID_ENCRYPTED_VALUE. */
	public static final String INVALID_ENCRYPTED_VALUE = "There is some problem in decrypting the value. Encrypted value is invalid.";

	/** The Constant INVALID_LICENCE_DETAILS. */
	public static final String INVALID_LICENCE_DETAILS = "Invalid Licence Details found for the given Licence Id";

	/** The Constant RESULT_USERNAME_OR_CHECKSUM_IS_EMPTY_OR_NULL. */
	public static final String RESULT_USERNAME_OR_CHECKSUM_IS_EMPTY_OR_NULL = "{\"result\":\"username or password is empty or null\"}";

	/** The Constant ERORR_IN_DECODING_CHECKSUM. */
	public static final String ERORR_IN_DECODING_CHECKSUM = "Error in decoding Password";

	/** The Constant RESULT_SUCCESS_WITH_USER_ID. */
	public static final String RESULT_SUCCESS_WITH_USER_ID = "{\"result\":\"success\",\"userid\":";

	/** The Constant FAILURE_JSON. */
	public static final String FAILURE_JSON = "{\"result\":\"failure\"}";
	
	public static final String FAILURE_JSON_WITH_REASON = "{\"result\":\"failure\",\"reason\":\"{0}\"}";

	/** The Constant LICENSE_IS_EXPIRED. */
	public static final String LICENSE_IS_EXPIRED = "License Is Expired";

	/** The Constant INVALID_APP_DETAILS. */
	public static final String INVALID_APP_DETAILS = "Invalid App Details";

	/** The Constant MAX_USER_LIMIT_REACHED. */
	public static final String MAX_USER_LIMIT_REACHED = "Max User Limit Reached";

	/** The Constant INVALID_DEVICE_DETAIL. */
	public static final String INVALID_DEVICE_DETAIL = "Invalid Device Detail";

	/** The Constant UID. */
	public static final String UID = "id";

	/** The Constant GET_LICENSEINFO_BY_IMEI_APPNAME_CLIENTID. */
	public static final String GET_LICENSEINFO_BY_IMEI_APPNAME_CLIENTID = "getLicenseInfoByIMEIAppNameClientId";

	/** The Constant GET_LICENSEINFO_COUNT_BY_LICENSEMASTER. */
	public static final String GET_LICENSEINFO_COUNT_BY_LICENSEMASTER = "getLicenseInfoCountByLicenseMaster";

	/** The Constant GET_LICENSEINFO_FROM_ID. */
	public static final String GET_LICENSEINFO_FROM_ID = "getLicenseInfoFromId";

	/** The Constant GET_DEVICEINFO_BY_APKID. */
	public static final String GET_DEVICEINFO_BY_APKID = "getDeviceInfoByApkId";

	/** The Constant GET_DEVICE_INFO_BY_USER_ID. */
	public static final String GET_DEVICE_INFO_BY_USER_ID = "getDeviceInfoByUserId";
	/** The Constant GET_DEVICE_INFO_BY_USER_ID. */
	public static final String GET_DEVICE_INFO_BY_USER_NAME = "getDeviceInfoByUserName";
	
	/** The Constant GET_DEVICE_LIST. */
	public static final String GET_DEVICE_LIST = "getDeviceList";
	
	/** The Constant GET_DEVICE_INFO_LIST_COUNT. */
	public static final String GET_DEVICE_INFO_LIST_COUNT = "getDeviceInfoListCount";
	
	
	/** The Constant GET_DEVICE_INFO_LIST_COUNT_BY_USER_NAME. */
	public static final String GET_DEVICE_INFO_LIST_COUNT_BY_USER_NAME = "getDeviceInfoListCountByUserName";


	/** The Constant GET_DEVICEINFO_BY_IMEI_AND_IMSI. */
	public static final String GET_DEVICEINFO_BY_IMEI_AND_IMSI = "getDeviceInfoByImeiAndImsi";
	
	/** The Constant GET_DEVICEINFO_BY_IMSI. */
	public static final String GET_DEVICEINFO_BY_IMSI="getDeviceInfoByImsi";

	/** The Constant GET_LICENSEMASTER_BY_APPNAME_CLIENTID. */
	public static final String GET_LICENSEMASTER_BY_APPNAME_CLIENTID = "getLicenseMasterByAppNameClientId";

	/** The Constant GET_APKDETAIL_BY_NAME. */
	public static final String GET_APKDETAIL_BY_NAME = "getAPKDetailByName";

	/** The Constant GET_APKDETAIL_BY_ID. */
	public static final String GET_APKDETAIL_BY_ID = "getAPKDetailById";
	
	/** The Constant GET_DEVICEINFO_BY_MSISDN. */
	public static final String GET_DEVICEINFO_BY_MSISDN="getDeviceInfoByMSISDN";

	/** The Constant SUCCESS. */
	public static final String SUCCESS = "SUCCESS";

	/** The Constant FAILURE. */
	public static final String FAILURE = "FAILURE";

	/** The Constant DEVICE_ID_LENGTH. */
	public static final Integer DEVICE_ID_LENGTH = 15;

	/** The Constant DEVICE_ID_PADDING_CONSTANT. */
	public static final String DEVICE_ID_PADDING_CONSTANT = String.valueOf(NumberUtils.INTEGER_ZERO);

	/** The Constant DEVICE_INFO. */
	public static final String DEVICE_INFO = "DeviceInfo";
	
	/** The Constant GET_DISTINCT_MAKE. */
	public static final String GET_DISTINCT_MAKE = "getDistinctMake";
	
	/** The Constant GET_DISTINCT_MODEL. */
	public static final String GET_DISTINCT_MODEL = "getDistinctModel";
	
	/** The Constant GET_DISTINCT_OS. */
	public static final String GET_DISTINCT_OS = "getDistinctOS";

	/** The Constant BULK_UPDATE_BATCH_SIZE. */
	public static final Integer BULK_UPDATE_BATCH_SIZE = 1000;
	
	/** The Constant EMPTY_BATCH_SIZE. */
	public static final Integer EMPTY_BATCH_SIZE = 0;
	
	/** The Constant GET_DEVICEINFO_BY_DEVICE_ID. */
	public static final String GET_DEVICEINFO_BY_DEVICE_ID = "getDeviceInfoByDeviceId";
	
	/** The invalid parameter json. */
	public static final String INVALID_PARAMETER_JSON = "{\"message\":\"Parameters given are invalid.\"}";

	/** The Constant DEVICE_ID. */
	public static final String DEVICE_ID = "deviceId";

	/** The Constant GET_DEVICEINFO_LIST_BY_IMEI_AND_IMSI. */
	public static final String GET_DEVICEINFO_LIST_BY_IMEI_AND_IMSI = "getDeviceInfoListByImeiAndImsi";
	
	/** The Constant MESSAGE_START. */
	public static final String MESSAGE_START = "{message : \"";
	
	/** The Constant MESSAGE_END. */
	public static final String MESSAGE_END = "\"}";
	
	/** The Constant FAILURE_JSON. */
	public static final String SUCCESS_JSON = "{\"result\":\"success\"}";
	
	/** The Constant APP_RELEASE_TIME_FILTER. */
	public static final String APP_RELEASE_TIME_FILTER = "apkDetailReleaseTimeFilter";
	
	/** The Constant MSISDN. */
	public static final String MSISDN="msisdn";
    
    /** The Constant UNAUTHORIZED_ACCESS. */
    public static final String UNAUTHORIZED_ACCESS="Unauthorized_Access";
	
	/** The Constant MOBILE_INTERNET. */
	public static final String MOBILE_INTERNET = "mobile_internet";
	
	/** The Constant MOBILE_INTRANET. */
	public static final String MOBILE_INTRANET = "mobile_intranet";

	/** The Constant GET_DEVICE_ID_BY_IMEI. */
	public static final String GET_DEVICE_ID_BY_IMEI = "getDeviceIdByIMEI";

	public static final String GET_DEVICE_INFO_BY_MDN = "getDeviceInfoByMdn";

	public static final String SOURCE = "source";
	public static final String  TFA_PERMISSION_NAME="ROLE_MOBILE_DISABLE_TFA";
	

	public static final String  BLANK_ROLE_JSON="\"roles\":[\"\"]";
	
	public static final String  ROLES_JSON_STRING="\"roles\":";
	
	public static final String  MODIFICATION_TIME="modificationTime";
	
	/** The Constant FIRST_REQUEST. */
	public static final String FIRST_REQUEST = "FIRST_REQUEST";
	
	public static final String RESULT_USERNAME_OR_PW_IS_EMPTY_OR_NULL = "{\"result\":\"username or password is empty or null\"}";
	public static final String RESULT_USERNAME_IS_EMPTY_OR_NULL = "{\"result\":\"username is empty or null\"}";
	public static final String CSRF_TOKEN_MOBILE="CSRF_TOKEN_MOBILE";

	public static final String VERSION_CODE="versionCode";
	
	
	public static final String APK_ID_FILTER="apkIdFilter";
	public static final String APK_CODE_FILTER="apkCodeFilter";
	public static final String APK_VERSION_NAME_FILTER="apkVersionNameFilter";
	public static final String APK_NAME_FILTER="apkNameFilter";
	

	public static final String CSRF_TOKEN_MOBILE_COOKIE_PATH = "CSRF_TOKEN_MOBILE_COOKIE_PATH";

	public static final String CSRF_TOKEN_MOBILE_COOKIE_SECURE = "CSRF_TOKEN_MOBILE_COOKIE_SECURE";

	public static final String CSRF_TOKEN_MOBILE_COOKIE_MAX_AGE = "CSRF_TOKEN_MOBILE_COOKIE_MAX_AGE";
	
	public static final String CHECKSUM = "password";
	
	public static final String APK_KEY ="APKDetails";
	public static final String LICENSE_KEY ="LicenseMaster";
	public static final String PROFILE_KEY ="Profile";
	public static final String BASE_APK_PATH="/data/report/NETVELOCITY/apk/";
	
	public static final String APK_CUSTOM_PATH="apk.custom.path";
	
	public static final String APK_DOWNLOAD_PATH="/nvapp/apk/";

	public static final String ENG_BUILD_ID = "ENG";

	public static final String NA = "NA";
}

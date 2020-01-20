package com.inn.foresight.module.nv.profile.constants;

import com.inn.commons.Symbol;
import com.inn.commons.lang.NumberUtils;

/**
 * The Class NVProfileConstants.
 *
 * @author innoeye
 * date - 26-Feb-2018 2:21:15 PM
 */
public class NVProfileConstants {
	
	/** Instantiates a new NV profile constants. */
	protected NVProfileConstants() { }
	
	/** The Constant FAILURE_JSON. */
	public static final String SUCCESS_JSON = "{\"result\":\"success\"}";
	
	/** The Constant FAILURE_JSON. */
	public static final String FAILURE_JSON = "{\"result\":\"failure\"}";

	/** The Constant NV_DEVICE_DATA. */
	public static final String NV_DEVICE_DATA = "NVDeviceData";
	
	/** The Constant NV_PROFILE_DATA. */
	public static final String NV_PROFILE_DATA = "NVProfileData";
	
	/** The Constant NV_PROFILE_TEMPLATE. */
	public static final String NV_PROFILE_TEMPLATE = "NVProfileTemplate";

	/** The Constant GEOGRAPHYL1. */
	public static final String GEOGRAPHYL1 = "geographyL1";
	
	/** The Constant GEOGRAPHYL2. */
	public static final String GEOGRAPHYL2 = "geographyL2";
	
	/** The Constant GEOGRAPHYL3. */
	public static final String GEOGRAPHYL3 = "geographyL3";
	
	/** The Constant GEOGRAPHYL4. */
	public static final String GEOGRAPHYL4 = "geographyL4";
	
	/** The Constant GEOGRAPHY_ID. */
	public static final String GEOGRAPHY_ID = "geographyId";
	
	/** The Constant MAKE. */
	public static final String MAKE = "make";
	
	/** The Constant MODEL. */
	public static final String MODEL = "model";
	
	/** The Constant OS. */
	public static final String  OS = "os";
	
	/** The Constant INVALID_GEOGRAPHY_LEVEL. */
	public static final String INVALID_GEOGRAPHY_LEVEL = "Invalid Geography Level";
	
	/** The invalid parameter json. */
	public static final String INVALID_PARAMETER_JSON = "{\"message\":\"Parameters given are invalid.\"}";
	

	
	/** The Constant DEVICE_ID. */
	public static final String DEVICE_ID = "deviceId";
	
	/** The Constant DEVICE_ID_LENGTH. */
	public static final Integer PROFILE_ID_LENGTH = 15;

	/** The Constant DEVICE_ID_PADDING_CONSTANT. */
	public static final String PROFILE_ID_PADDING_CONSTANT = String.valueOf(NumberUtils.INTEGER_ZERO);
	
	/** The Constant DEVICE_ID_PADDING_CONSTANT. */
	public static final String PROFILE_ID_PREFIX = "NP";
	
	/** The Constant FIND_PROFILE_DATA. */
	public static final String FIND_PROFILE_DATA = "findProfileData";
	
	/** The Constant FIND_PROFILE_DATA. */
	public static final String GET_ALL_PROFILE = "getAllProfiles";
	
    /** The Constant GET_PROFILE_DATA_BY_PROFILE_ID. */
    public static final String GET_PROFILE_DATA_BY_PROFILE_ID="getProfileDataByProfileId";
    
    public static final String PROFILE_ID_PLACEHOLDER="profileId";
	
	/** The Constant ID. */
	public static final String ID = "id";
	
	/** The Constant OPERATOR. */
	public static final String OPERATOR = "OPERATOR";
	
	/** The Constant TECHNOLOGY. */
	public static final String TECHNOLOGY = "NV_TECHNOLOGY";

	/** The Constant TECHNOLOGY. */
	public static final Integer INDEX_ZERO = NumberUtils.INTEGER_ZERO;
	
	/** The Constant GET_PROFILE_BY_TYPE. */
	public static final String GET_PROFILE_BY_TYPE_AND_MODULE = "getProfileByTypeAndModule";
	
	/** The Constant TYPE. */
	public static final String TYPE = "type";
	
	/** The Constant PRE_DEFINE_PROFILE. */
	public static final String PRE_DEFINE_PROFILE = "PRE-DEFINE";
	
	/** The Constant GET_ALL_VALID_PROFILES. */
	public static final String GET_ALL_VALID_PROFILES = "getAllValidProfiles";
	
	/** The Constant LATITUDE. */
	public static final String LATITUDE = "latitude";
			
	/** The Constant LONGITUDE. */
	public static final String LONGITUDE = "longitude";
	
	/** The Constant MCC. */
	public static final String MCC = "mcc";
	
	/** The Constant MNC. */
	public static final String MNC = "mnc";
	
	/** The Constant CELL_ID. */
	public static final String CELL_ID = "cellId";
	
	/** The Constant module. */
	public static final String MODULE = "module";
	
	/** The Constant PAN_GEOGRAPHY. */
	public static final String PAN_GEOGRAPHY = "panGeography";
	
	/** The Constant BLANK_STRING. */
	public static final String BLANK_STRING = "";
	
	/** The Constant MODULE_IS_NULL_MESSAGE. */
	public static final String MODULE_IS_NULL_MESSAGE = "App module is null";
	
	/** The Constant INVALID_MCC_MNC_MESSAGE. */
	public static final String INVALID_MCC_MNC_MESSAGE = "Invalid mcc & mnc unable to get operator";
	
	/** The Constant GEOGRAPHYL1_INDEX. */
	public static final Integer GEOGRAPHYL1_INDEX = 3;
	
	/** The Constant GEOGRAPHYL2_INDEX. */
	public static final Integer GEOGRAPHYL2_INDEX = 2;
	
	/** The Constant GEOGRAPHYL3_INDEX. */
	public static final Integer GEOGRAPHYL3_INDEX = 1;
	
	/** The Constant GEOGRAPHYL4_INDEX. */
	public static final Integer GEOGRAPHYL4_INDEX = 0;
	
	/** The Constant PROFILE_MAP_KEY_DELIMITER. */
	public static final String PROFILE_MAP_KEY_DELIMITER = Symbol.HASH_STRING;
	
	/** The Constant PROFILE_ID. */
	public static final String PROFILE_ID = "profileId";
	
	/** The Constant STATUS. */
	public static final String STATUS = "status";
	
	/** The Constant MESSAGE_START. */
	public static final String MESSAGE_START = "{message : \"";
	
	/** The Constant MESSAGE_END. */
	public static final String MESSAGE_END = "\"}";

	/** The Constant NV_ENTERPRISE. */
	public static final String NV_ENTERPRISE = "NV_ENTERPRISE";
	
	/** The Constant PROFILE_MAP_KEY_GEOGRAPHY_INDEX. */
	public static final Integer PROFILE_MAP_KEY_GEOGRAPHY_INDEX = INDEX_ZERO;
	
	public static final String OPERATOR_MCC_MNC_MAP="OPERATOR_MCC_MNC_MAP";
	public static final String SYSTEM="SYSTEM";
}

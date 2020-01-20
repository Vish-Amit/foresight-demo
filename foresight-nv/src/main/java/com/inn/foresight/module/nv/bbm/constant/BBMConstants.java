package com.inn.foresight.module.nv.bbm.constant;

public class BBMConstants {
	protected BBMConstants() {
	}

	public static final Double ONE_MB_SIZE = 1048576d;
	public static final int ZERO = 0;
	public static final int MINUS_ONE = -1;
	public static final String CSV = ".csv";
	public static final String CSV_CAPS = ".CSV";
	public static final String KEY_IMEI = "imei";
	public static final String KEY_IMSI = "imsi";
	public static final String KEY_UUID = "uuid";
	public static final String INVALID_DEVICE_DETAIL_JSON = "{\"status\":\"Failure\",\"reason\":\"invalid device detail\"}";
	/** The failure json. */
	public static final String FAILURE_JSON = "{\"Result\":\"Failure\"}";
	public static final String SUCCESS_JSON = "{\"Result\":\"Success\"}";
	public static final String INVALID_PARAMETER_JSON = "{\"Result\":\"Failure\",\"reason\":\"invalid Data\"}";
	/** The exception something went wrong. */
	public static final String EXCEPTION_SOMETHING_WENT_WRONG = "There is some problem in completing the request.";

	/** The Constant GET_UPLOAD_FLOOR_PLAN_DATA_URI. */
	public static final String GET_DROPWIZARD_SYNCHRONIZE_DATA_URI = "synchronizeData";

	public static final String INVALID_FILE_SIZE = "{\"Result\":\"Failure\",\"reason\":\"File Size limit exceeded\"}";
	public static final String ZIP_FILE_EXTENTION = ".zip";
	public static final String CSV_SEPARATOR = ",";
	public static final int FIRST_INDEX = 0;
	public static final int SECOND_INDEX = 1;
	public static final int THIRD_INDEX = 2;
	public static final int FOURTH_INDEX = 3;
	public static final int FIFTH_INDEX = 4;
	public static final int MIN_HEADER_LENGTH = 3;
	public static final String BBM_DEVICE_REG_HEADER = "DeviceId,MDN,BBMID,RegistrationDate";
	public static final String BBM_DEVICE_DE_REG_HEADER = "DeviceId,MDN,BBMID,DeregistrationDate,DeregistrationCause";
	public static final String HEADER_DEVICE_ID="DeviceId";
	public static final String HEADER_MDN="MDN";
	public static final String HEADER_BBMID="BBMID";
	public static final String HEADER_REG_DATE="RegistrationDate";
	public static final String HEADER_DE_REG_DATE="DeregistrationDate";
	public static final String HEADER_DE_REG_CAUSE="DeregistrationCause";
	public static final long MILLISECONDS = 1000;
	

}

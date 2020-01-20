package com.inn.foresight.module.nv.pushnotification.constants;

/** The Class PushNotificationConstants. */
public class PushNotificationConstants {
	
	private PushNotificationConstants() {
		
	}

	/** The Constant FIRST_INDEX. */
	public static final int FIRST_INDEX = 0;
	
	/** The Constant KEY_MODULE_NAME. */
	public static final String KEY_MODULE_NAME = "moduleName";
	
	/** The Constant KEY_DATA. */
	public static final String KEY_DATA = "data";
	
	/** The Constant KEY_NOTIFICATION_MESSAGE. */
	public static final String KEY_NOTIFICATION_MESSAGE="message";
	
	/** The Constant KEY_REGISTRATION_IDS. */
	public static final String KEY_REGISTRATION_IDS="registration_ids";
	
	public static final String KEY_CONTENT_AVAILABLE="content_available";
	
	public static final String KEY_NOTIFICATION="notification";
	
	public static final String KEY_BODY="body";
	
	/** The Constant GET_ALL_NEW_AND_FAILED_NOTIFICATION. */
	public static final String GET_ALL_NEW_AND_FAILED_NOTIFICATION="findAllNewAndFailedNotification";
    
    /** The Constant GET_ALL_NOTIFICATION_FILE_LIST. */
    public static final String GET_ALL_NOTIFICATION_FILE_LIST="getAllNotificationFileList";
    
    /** The Constant GET_ALL_NOTIFICATION_BY_NOTIFICATIONFILE. */
    public static final String GET_ALL_NOTIFICATION_BY_NOTIFICATIONFILE="getAllNotificationByFile";
    
    public static final String GET_ALL_NOTIFICATION_BY_NOTIFICATIONFILE_COUNT="getFeedBackNotificationFileDetailListCount";
    
    /** The Constant GET_FEEDBACK_NOTIFICATION_FILE_COUNT. */
    public static final String GET_FEEDBACK_NOTIFICATION_FILE_COUNT="getFeedbackNotificationFileCount";
	
	/** The Constant RATE_YOUR_EXPERIENCE. */
	public static final String RATE_YOUR_EXPERIENCE = "Rate your experience";
	
	/** The Constant WORKORDER_MSG. */
	public static final String WORKORDER_MSG = "You have been assigned a Workorder";
	
	/** The Constant METHOD_POST. */
	public static final String METHOD_POST="POST";
	
	/** The Constant HEADER_KEY_AUTHORIZATION. */
	public static final String HEADER_KEY_AUTHORIZATION="Authorization";
	
	/** The Constant MIN_UPPER_LIMIT. */
	public static final Integer MIN_UPPER_LIMIT=0;
	
	/** The Constant MIN_LOWER_LIMIT. */
	public static final Integer MIN_LOWER_LIMIT=0;
	
	/** The Constant NOTIFICATION_FILE_PLACE_HOLDER. */
	public static final String NOTIFICATION_FILE_PLACE_HOLDER="notificationFilePlaceHolder";
	
	/** The invalid parameter json. */
	public static final String INVALID_NOTIFICATION_OBJECT_JSON = "{\"message\":\"Invalid Notification Object.\"}";
	
	public static final String INVALID_PARAMETER_JSON = "{\"message\":\"Parameters given are invalid.\"}";
	
	/** The Constant NO_DEVICE_INFO_FOUND. */
	public static final String NO_DEVICE_INFO_FOUND = "No Device info found";
	
	
	/** The Constant USER_NOT_FOUND. */
	public static final String USER_NOT_FOUND="User not found";
	
	/** The success json. */
	public static final String SUCCESS_JSON = "{\"message\":\"Success\"}";

	/** The failure json. */
	public static final String FAILURE_JSON = "{\"message\":\"Failure\"}";
	
	public static final String KEY_PUSHNOTIFICATION_ID="pushnotification_id";

	public static final String IOS_QUICKE_TEST_MESSAGE = "Start Speed Test";

	public static final String LIVE_LOCATION = "LIVE_LOCATION";

	public static final String QUICK_TEST = "QUICKTEST";
	
	public static final String KEY_PRIORITY="priority";
	
	public static final Integer PRIORITY_VALUE=10;
	
	

}

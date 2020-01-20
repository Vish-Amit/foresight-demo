package com.inn.foresight.module.nv.webrtc.constant;

public class WebRTCConstant {
	
	private WebRTCConstant() {
		
	}

	public static final String CALLKEY = "Call";
	public static final String MOSKEY = "MOS";
	public static final String VIDEO_KEY = "Video";
	public static final String AUDIO_KEY = "Audio";
	public static final String SWITCH_KEY = "Switch";
	public static final String GOOD_KEY = "GOOD";
	public static final String AVG_KEY = "AVG";
	public static final String BAD_KEY = "BAD";

	public static final String CALLTYPE = "CallType";
	public static final String CALL = "CALL";
	public static final String DATE_FORMATE = "ddMMM";

	public static final Double DEFAULT_ADD_VALUE = 0.0;

	public static final String AUDIO = "audio";
	/** Hbase columns of webrtc data tables. */
	public static final String COLUMN_AVG_RSRP = "avgRsrp";
	public static final String COLUMN_AVG_SINR = "avgSinr";
	public static final String COLUMN_SNR = "snr";
	public static final String COLUMN_AVG_RSSI = "rssi";

	
	public static final String COLUMN_RSCP = "avgRscp";
	public static final String COLUMN_RXLEVEL = "avgRxLevel";
	
	public static final String COLUMN_DEVICEID = "deviceId";

	public static final String COLUMN_MOS = "mos";
	public static final String COLUMN_LAT = "latitude";
	public static final String COLUMN_LONG = "longitude";
	public static final String COLUMN_CALL_START_TIME = "callStartTime";
	public static final String COLUMN_CALL_END_TIME = "callEndTime";
	public static final String COLUMN_BYTE_SEND_AUDIO_SENDER = "btSentAS";
	public static final String COLUMN_BYTE_SEND_AUDIO_RECIVER = "btSentAR";
	public static final String COLUMN_BYTE_SEND_VIDEO_SENDER = "btSentVS";
	public static final String COLUMN_BYTE_SEND_VIDEO_RECIVER = "btSentVR";
	public static final String COLUMN_BYTE_RECIVE_AUDIO_SENDER = "btrecvAS";
	public static final String COLUMN_BYTE_RECIVE_AUDIO_RECIVER = "btrecvAR";
	public static final String COLUMN_BYTE_RECIVE_VIDEO_SENDER = "btrecvVS";
	public static final String COLUMN_BYTE_RECIVE_VIDEO_RECIVER = "btrecvVR";

	public static final String COLUMN_PACKETLOSS_SEND_AUDIO_SENDER = "pcktSentAS";
	public static final String COLUMN_PACKETLOSS_SEND_AUDIO_RECIVER = "pcktSentAR";
	public static final String COLUMN_PACKETLOSS_SEND_VIDEO_SENDER = "pcktSentVS";
	public static final String COLUMN_PACKETLOSS_SEND_VIDEO_RECIVER = "pcktSentVR";
	public static final String COLUMN_PACKETLOSS_RECIVE_AUDIO_SENDER = "pcktRecvAS";
	public static final String COLUMN_PACKETLOSS_RECIVE_AUDIO_RECIVER = "pcktRecvAR";
	public static final String COLUMN_PACKETLOSS_RECIVE_VIDEO_SENDER = "pcktRecvVS";
	public static final String COLUMN_PACKETLOSS_RECIVE_VIDEO_RECIVER = "pcktRecvVR";

	public static final String COLUMN_PACKETLOSS_AUDIO_SENDER = "pcktLostAS";
	public static final String COLUMN_PACKETLOSS_AUDIO_RECIVER = "pcktLostAR";
	public static final String COLUMN_PACKETLOSS_VIDEO_SENDER = "pcktLostVS";
	public static final String COLUMN_PACKETLOSS_VIDEO_RECIVER = "pcktLostVR";

	public static final String COLUMN_FAMILY = "r";
	public static final String DATE_FORMATE_DDMMYY = "ddMMyy";
	public static final String DC_STRING = "DC";
	public static final String COLUMN_AVG_RTT = "avgRtt";
	public static final String COLUMN_V_CALL_RELTYPE = "vCallRelType";
	public static final String COLUMN_V_MEDIA_TYPE = "vMediaType";
	public static final String COLUMN_AVG_PACKETLOSS = "avgPcktlost";
	public static final String COLUMN_V_EVENT = "vCallEvent";
	public static final String COLUMN_IMSI = "imsi";
	public static final String SENDER_ROW_KEY = "sentRowKey";
	public static final String RECEIVER_ROW_KEY ="recvRowKey";
	public static final String NETWORK_TYPE = "networkType";
	public static final String WIFI_TYPE = "Wifi";
	public static final String TYPE_LTE = "LTE";
	public static final String AVG_MOS = "avgMos";
	public static final String FAIL_CALL = "failedCalls";
	public static final String DROP_CALL = "dropCalls";
	public static final String TOTAL_CALL = "totalCalls";
	public static final String TOTAL_SUBCRIBER = "totalsubscriber";
	public static final String ALL = "ALL";

	public static final String RSRP = "RSRP";
	public static final String SINR = "SINR";
	public static final String RSRQ = "RSRQ";
	public static final String OS = "Os";
	public static final String IOS = "IOS";
	public static final String ANDROID = "Android";
	public static final String DROP_CALL_KEY = "Drop Calls";
	public static final String FAILED_CALL_KEY = "Failed Calls";
	public static final String SUCCESS_CALL_KEY = "Success Calls";
	public static final String USER_KEY = "Subscriber";
	public static final String DATA = "Data";
	
	public static final String PACKET_LOSS_KEY = "Packet Loss";
	public static final String JITTER_KEY = "Jitter";
	public static final String AVERAGE_MOS_KEY = "Avg MOS";
	public static final String RTT_KEY = "RTT";
	public static final String TYPE_3G = "3G";
	public static final String TYPE_2G = "2G";
	public static final String RSSI = "RSSI";
	public static final String RSCP = "RSCP";
	public static final String RX_LEVEL = "RX LEVEL";
	public static final String SNR = "SNR";
	public static final String TOTAL_USER = "totalUser";
	public static final String ZIP_EXTENSION = ".zip";
	public static final double BAD_MOS_VALUE = 3.1;
	public static final double GOOD_MOS_VALUE = 4.00;
	public static final String BAD = "Bad";
	public static final String AVERAGE = "Average";
	public static final String GOOD = "Good";
	public static final String G_CDC_NM_VS = "gCdcNmVS";
	public static final String G_CDC_NM_AS = "gCdcNmAS";
	public static final String V_CALL_TOKEN = "vCallToken";
	public static final String V_MEDIA_TYPE = "vMediaType";
	public static final String V_CALL_REL_TYPE = "vCallRelType";
	public static final String V_CALL_TYPE = "vCallType";
	public static final String CALL_LAYER = "CALL";
	public static final String MOS_LAYER = "MOS";
	public static final String PKTLOSS_LAYER = "PKTLOSS";
	public static final String CGI = "cgi";
	public static final String PCI = "pci";
	public static final String PSC = "psc";
	public static final String CELL_ID = "cellId";
	public static final String M_TYPE = "mType";

	public static final Integer FIVE = 5;
	public static final Integer SIX = 6;
	public static final Integer SEVEN = 7;
	public static final Integer EIGHT = 8;
	public static final Integer NINE = 9;

	public static final Integer TEN = 10;
	public static final Integer ELEVEN = 11;
	public static final Integer TWELVE = 12;
	public static final Integer THIRTEEN = 13;
	public static final Integer FOURTEEN = 14;
	public static final Integer FIFTEEN = 15;
	public static final Integer SIXTEEN = 16;
	public static final Integer SEVENTEEN = 17;
	public static final Integer EIGHTEEN = 18;
	public static final String AVG_RTT = "avgRtt";
	public static final String AVG_PKTLOSS = "avgPcktlost";
	public static final String MOS = "mos";
	public static final String RFACTOR = "rFactor";
	public static final String HANDOVER = "handover";
	private static final String TECHNOLOGY_LTE = "LTE";
	private static final String TECHNOLOGY_3G = "3G";
	private static final String TECHNOLOGY_2G = "2G";
	
}

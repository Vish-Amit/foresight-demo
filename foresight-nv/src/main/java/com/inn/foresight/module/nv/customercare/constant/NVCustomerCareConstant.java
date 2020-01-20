package com.inn.foresight.module.nv.customercare.constant;

public class NVCustomerCareConstant {
	protected NVCustomerCareConstant() {
		
	}
	/** The invalid parameter json. */
	public static final String INVALID_PARAMETER_JSON = "{\"message\":\"Parameters given are invalid.\"}";
	public static final String FAILURE_JSON = "{\"message\":\"Failure\"}";

	/** The success json. */
	public static final String SUCCESS_JSON = "{\"message\":\"Success\"}";


	public static final String GET_NV_LATEST_DATA_URI = "getNVLatestData?";

	public static final String NO_DEVICE_FOUND_FOR = "No device(s) found for ";

	public static final CharSequence COMMA_SEPARATOR = ",";
	public static final String EXCEPTION_PROBLEM_IN_DROPWIZARD = "There is some problem in completing the request in dropwizard";
public static final String GET_DEVICE_INFO_BY_IMEI_OR_IMSI = "getDeviceInfoByImeiOrImsi";
	public static final String IMEI = "imei";
	public static final String IMSI = "imsi";
	public static final String ACTIVE_TYPE="Active";
	public static final String PASSIVE_TYPE="Passive";
	public static final String BOTH_TYPE="both";
	public static final String ACTIVE_COLUMNS="capturedOn,activeTestType,type,userid,date,mcc,mnc,cellId,band,pci,tac,psc,lac,latitude,longitude,gL1,gL2,gL3,gL4,starRating,minRsrp,maxRsrp,avgRsrp,minSinr,maxSinr,avgSinr,minRsrq,maxRsrq,avgRsrq,minRssi,maxRssi,avgRssi,minSnr,maxSnr,avgSnr,minRscp,maxRscp,avgRscp,minEcNo,maxEcNo,avgEcNo,minRxLevel,maxRxLevel,avgRxLevel,minRxquality,maxRxquality,avgRxquality,minDlRate,maxDlRate,avgDlRate,minUlRate,maxUlRate,avgUlRate,minLatency,maxLatency,avgLatency,jitter,pcktLoss,clientOpName,url1Time,url2Time,url3Time,make,model,deviceOS,address,userComment,chargerConnected,batteryLevel,voltage,temperature,nearestCity,nearestIP,destPingIpAddr,versionName,androidId,deviceId,speedTestFailure,dualSimEnable,mobileNumber,isManualSync,buildNumber,cgi,eNodeB,enodeBVendor,enodeBType,bssid,autoDateAndTime,earfcn,uarfcn,arfcn,cpuUsage,memoryUsage,deviceIPV4Status,deviceIPV6Status";
	public static final String PASSIVE_COLUMNS="date,capturedOn,networkType,networkSubtype,operatorName,connectionType,networkTypeWhenWifi,operatorNameWhenWiFi,collectionType,deviceId,androidId,gpsEnabled,latitude,longitude,gL1,gL2,gL3,gL4,locAccuracy,barometerList,altitude,mcc,mnc,cellId,cgi,eNodeB,pci,tac,psc,lac,band,frequency,macAddress,bssid,ssid,avgRsrp,avgRsrq,avgRssi,avgSinr,rssi,snr,3gRscp,3gEcno,2Grxlevel,2gRxQuality,avgUlRate,avgDlRate,linkSpeed,versionName,nvModule,sdkVersion,make,model,deviceOS,baseband,buildNumber,cpuUsage,memoryUsage,batteryLevel,batteryVoltage,batteryTemp,socModel,coreArch,fingerPrint,serialNo,chipSet,isEnterprise,dualSimEnable,autoDateTime,roamingStatus,charging,chargingType,charger,profileId,profileStatus,simType,sim1,sim2,ipV4,ipV6,callDuration,callType,callEstStatus,callReleaseType,isgwEnabled,isDciEnabled,voiceNetworkType,voiceOperatorName,voiceMcc,voiceMnc,voiceCellId,voicePci,voiceTac,voiceLac,voicePsc,voiceRsrp,voiceRsrq,voiceRssi,voiceSinr,voiceRscp,voiceEcno,voiceRxLevel,voiceRxQuality";
	public static final String COLUMN_FAMILY = "r";
	public static final String SEPERATOR=",";

		/** HBASE Columns Name constants. */
	public static final String COLUMN_ACTIVE_TEST_TYPE = "activeTestType";
	public static final String COLUMN_ADDRESS = "address";
	public static final String COLUMN_ANDROID_ID = "androidId";
	public static final String COLUMN_AUTO_DATE_AND_TIME = "autoDateAndTime";
	public static final String COLUMN_AVERAGE_DL_RATE = "avgDlRate";
	public static final String COLUMN_AVERAGE_LATENCY = "avgLatency";
	public static final String COLUMN_AVERAGE_RSRP = "avgRsrp";
	public static final String COLUMN_AVERAGE_RSRQ = "avgRsrq";
	public static final String COLUMN_AVERAGE_RSSI = "avgRssi";

	public static final String COLUMN_AVERAGE_UL_RATE = "avgUlRate";
	public static final String COLUMN_AVERAGE_SINR = "avgSinr";
	public static final String COLUMN_BAND = "band";
	public static final String COLUMN_BASE_BAND = "baseband";
	public static final String COLUMN_BATTERY_LEVEL = "batteryLevel";
	public static final String COLUMN_BUILD_NUMBER = "buildNumber";
	public static final String COLUMN_CAPTURED_ON = "capturedOn";
	public static final String COLUMN_CELL_ID = "cellId";
	public static final String COLUMN_CHARGER_CONNECTED = "chargerConnected";
	public static final String COLUMN_CLIENT_OP_NAME = "clientOpName";

	public static final String COLUMN_CLIENT_PING_IP_ADDR = "clientPingIpAddr";
	public static final String COLUMN_CPU_USAGE = "cpuUsage";
	public static final String COLUMN_DEST_PING_IP_ADDR = "destPingIpAddr";
	public static final String COLUMN_DEVICE_CHIP_SET = "deviceChipSet";
	public static final String COLUMN_DEVICE_CORE_ARCH = "deviceCoreArch";

	public static final String COLUMN_DEVICE_FINGERPRINT = "deviceFingerprint";
	public static final String COLUMN_DEVICE_IPV4_STATUS = "deviceIPV4Status";
	public static final String COLUMN_DEVICE_IPV6_STATUS = "deviceIPV6Status";
	public static final String COLUMN_DEVICE_ID = "deviceId";
	public static final String COLUMN_DEVICE_OS = "deviceOS";

	public static final String COLUMN_DEVICE_SERIAL_NO = "deviceSerialNo";
	public static final String COLUMN_DEVICE_SOC_MODEL = "deviceSocModel";
	public static final String COLUMN_DUAL_SIM_ENABLE = "dualSimEnable";
	public static final String COLUMN_EARFCN = "earfcn";
	public static final String COLUMN_GPS_STATUS = "gpsStatus";
	public static final String COLUMN_DATA_SIM_DETAIL="dataSimDetail";
	public static final String COLUMN_VOICE_SIM_DETAIL="voiceSimDetail";
			
	

	public static final String COLUMN_IS_DCI_ENABLED = "isDciEnabled";
	public static final String COLUMN_IS_ENTERPRISE = "isEnterprise";
	public static final String COLUMN_LATITUDE = "latitude";
	public static final String COLUMN_LOCATION_SOURCE = "locationSource";
	public static final String COLUMN_LONGITUDE = "longitude";

	public static final String COLUMN_MAKE = "make";
	public static final String COLUMN_MAX_DL_RATE = "maxDlRate";
	public static final String COLUMN_MAX_LATENCY = "maxLatency";
	public static final String COLUMN_MAX_RSRP = "maxRsrp";
	public static final String COLUMN_MAX_RSRQ = "maxRsrq";

	public static final String COLUMN_MAX_RSSI = "maxRssi";
	public static final String COLUMN_MAX_SINR = "maxSinr";
	public static final String COLUMN_MAX_UL_RATE = "maxUlRate";
	public static final String COLUMN_MCC = "mcc";
	public static final String COLUMN_MEMORY_USAGE = "memoryUsage";
		public static final String COLUMN_MIN_DL_RATE = "minDlRate";
	public static final String COLUMN_MIN_LATENCY = "minLatency";
	public static final String COLUMN_MIN_RSRP = "minRsrp";
	public static final String COLUMN_MIN_RSRQ = "minRsrq";
	public static final String COLUMN_MIN_RSSI = "minRssi";

	public static final String COLUMN_MIN_SINR = "minSinr";
	public static final String COLUMN_MIN_UL_RATE = "minUlRate";
	public static final String COLUMN_MNC = "mnc";
	public static final String COLUMN_MODEL = "model";
	public static final String COLUMN_NEAREST_CITY = "nearestCity";

	public static final String COLUMN_NEAREST_FETCH_TIME = "nearestFetchTime";
	public static final String COLUMN_NEAREST_IP = "nearestIP";
	public static final String COLUMN_NETWORK_SUB_TYPE = "networkSubtype";
	public static final String COLUMN_NV_MODULE = "nvModule";
	public static final String COLUMN_PCI = "pci";

	public static final String COLUMN_RECORD_TYPE = "recordType";
	public static final String COLUMN_SIM_SLOT = "simSlot";
	public static final String COLUMN_SPEED_TEST_FAIL = "speedTestFail";
	public static final String COLUMN_STAR_RATING = "starRating";
	public static final String COLUMN_TAC = "tac";

	public static final String COLUMN_TEMERATURE = "temperature";
	public static final String COLUMN_TYPE = "type";
	public static final String COLUMN_USER_COMMENT = "userComment";
	public static final String COLUMN_USER_ID = "userid";
	public static final String COLUMN_VERSION_NAME = "versionName";

	public static final String COLUMN_VOLTAGE = "voltage";
	public static final String COLUMN_WIFI_SCAN_LIST = "wifiScanList";

	public static final String COLUMN_GL1 = "gL1";
	public static final String COLUMN_GL2 = "gL2";
	public static final String COLUMN_GL3 = "gL3";
	public static final String COLUMN_GL4 = "gL4";

	public static final String COLUMN_GPS_ACCURACY = "gpsAccuracy";
	public static final String COLUMN_IS_MANUAL_SYNC = "isManualSync";
	public static final String COLUMN_MAC_ADDRESS = "macAddress";
	public static final String COLUMN_MAX_LINK_SPEED = "maxLinkspeed";
	public static final String COLUMN_MIN_LINK_SPEED = "minLinkspeed";
	public static final String COLUMN_NW_TYPE_WHEN_WIFI = "networkTypeWhenWifi";
	public static final String COLUMN_SECURITY_TYPE = "securityType";
	public static final String COLUMN_SPEED_TEST_FAILURE = "speedTestFailure";

	public static final String COLUMN_BSSID = "bssid";
	public static final String COLUMN_CHANNEL = "channel";
	public static final String COLUMN_CHARGING_TYPE = "chargingType";
	public static final String COLUMN_DATE = "date";
	public static final String COLUMN_CGI = "cgi";
	public static final String COLUMN_E_NODE_B = "eNodeB";
	public static final String COLUMN_JITTER = "jitter";

	public static final String COLUMN_PCKT_LOSS = "pcktLoss";
	public static final String COLUMN_PCKT_RECIEVED = "pcktReceived";
	public static final String COLUMN_PCKT_TRANSMITTED = "pcktTransmitted";
	public static final String COLUMN_TIME = "time";

	/** Passive. */

	public static final String COLUMN_BATTERY_TEMP = "batteryTemp";
	public static final String COLUMN_CALL_EST_STATUS = "callEstStatus";
	public static final String COLUMN_CALL_START_INFO = "callStartInfo";
	public static final String COLUMN_CALL_TYPE = "callType";

	public static final String COLUMN_CHARGER = "charger";
	public static final String COLUMN_CHARGING = "charging";
	public static final String COLUMN_CHIP_SET = "chipSet";
	public static final String COLUMN_ACTIVE_CHIP_SET = "deviceChipSet";
	public static final String COLUMN_COLLECTION_TYPE = "collectionType";

	public static final String COLUMN_CONNECTION_TYPE = "connectionType";
	public static final String COLUMN_CORE_ARCH = "coreArch";
	public static final String COLUMN_FLIGHT_MODE = "flightMode";
	public static final String COLUMN_GPS_ENABLED = "gpsEnabled";

	public static final String COLUMN_AVG_EC_NO = "avgEcNo";
	public static final String COLUMN_AVG_RSCP = "avgRscp";
	public static final String COLUMN_IPV4 = "ipV4";
	public static final String COLUMN_IPV6 = "ipV6";
	public static final String COLUMN_ISGW_ENABLED = "isgwEnabled";

	public static final String COLUMN_LAC = "lac";
	public static final String COLUMN_LOC_ACCURACY = "locAccuracy";
	public static final String COLUMN_OPERATOR_NAME = "operatorName";
	public static final String COLUMN_NETWORK_TYPE = "networkType";

	public static final String COLUMN_PSC = "psc";
	public static final String COLUMN_ROAMING_STATUS = "roamingStatus";
	public static final String COLUMN_SDK_VERSION = "sdkVersion";

	public static final String COLUMN_SERIAL_NO = "serialNo";
	public static final String COLUMN_SIM_DETAIL = "simDetail";
	public static final String COLUMN_SOC_MODEL = "socModel";
	
	public static final String COLUMN_2G_RX_LEVEL = "2Grxlevel";
	public static final String COLUMN_2G_RX_QUALITY = "2gRxQuality";
	

	public static final String COLUMN_3G_ECNO = "3gEcno";
	public static final String COLUMN_3G_RSCP = "3gRscp";
	public static final String COLUMN_ALTITUDE = "altitude";
	public static final String COLUMN_ARFCN = "arfcn";
	public static final String COLUMN_AUTO_DATE_TIME = "autoDateTime";
	public static final String COLUMN_AVERAGE_RSCP = "avgRscp";
	public static final String COLUMN_AVERAGE_RX_LEVEL = "avgRxLevel";
	public static final String COLUMN_AVERAGE_RX_QUALITY = "avgRxquality";
	public static final String COLUMN_AVERAGE_SNR = "avgSnr";
	public static final String COLUMN_BAROMETER_LIST = "barometerList";
	public static final String COLUMN_BATTERY_VOLTAGE = "batteryVoltage";
	public static final String COLUMN_CALL_DURATION = "callDuration";
	public static final String COLUMN_CALL_RELEASE_TYPE = "callReleaseType";
	public static final String ENODE_B_TYPE = "enodeBType";
	public static final String COLUMN_ENODE_B_VENDOR = "enodeBVendor";
	public static final String COLUMN_FINGERPRINT = "fingerPrint";
	public static final String COLUMN_FREQUENCY = "frequency";
	public static final String COLUMN_LINK_SPEED = "linkSpeed";
	public static final String COLUMN_MAX_ECNO = "maxEcNo";
	public static final String COLUMN_MAX_RSCP = "maxRscp";
	public static final String COLUMN_MAX_RX_LEVEL = "maxRxLevel";
	public static final String COLUMN_MAX_RX_QUALITY = "maxRxquality";
	public static final String COLUMN_MAX_SNR = "maxSnr";
	public static final String COLUMN_MIN_ECNO = "minEcNo";
	public static final String COLUMN_MIN_RSCP = "minRscp";
	public static final String COLUMN_MIN_RX_LEVEL = "minRxLevel";
	public static final String COLUMN_MIN_RX_QUALITY = "minRxquality";
	public static final String COLUMN_MIN_SNR = "minSnr";
	public static final String COLUMN_MOBILE_NUMBER = "mobileNumber";
	public static final String COLUMN_NETWORK_TYPE_WHEN_WIFI = "networkTypeWhenWifi";
	public static final String COLUMN_CLIENT_OP_NAME_WHEN_WIFI = "operatorNameWhenWiFi";
	public static final String COLUMN_PROFILE_ID = "profileId";
	public static final String COLUMN_PROFILE_STATUS = "profileStatus";
	public static final String COLUMN_RSSI = "rssi";
	public static final String COLUMN_SIM1 = "sim1";
	public static final String COLUMN_SIM2 = "sim2";
	public static final String COLUMN_SIM_TYPE = "simType";
	public static final String COLUMN_SNR = "snr";
	public static final String COLUMN_SSID = "ssid";
	public static final String COLUMN_UARFCN = "uarfcn";
	public static final String COLUMN_URL1_TIME = "url1Time";
	public static final String COLUMN_URL2_TIME = "url2Time";
	public static final String COLUMN_URL3_TIME = "url3Time";
	public static final String COLUMN_VOICE_CELL_ID = "voiceCellId";
	public static final String COLUMN_VOICE_ECNO = "voiceEcno";
	public static final String COLUMN_VOICE_LAC = "voiceLac";
	public static final String COLUMN_VOICE_MCC = "voiceMcc";
	public static final String COLUMN_VOICE_MNC = "voiceMnc";
	public static final String COLUMN_VOICE_NW_TYPE = "voiceNetworkType";
	public static final String COLUMN_VOICE_OPERATOR_NAME = "voiceOperatorName";
	public static final String COLUMN_VOICE_PCI = "voicePci";
	public static final String COLUMN_VOICE_PSC = "voicePsc";
	public static final String COLUMN_VOICE_RSCP = "voiceRscp";
	public static final String COLUMN_VOICE_RSRP = "voiceRsrp";
	public static final String COLUMN_VOICE_RSSI = "voiceRssi";
	public static final String COLUMN_VOICE_RSRQ = "voiceRsrq";
	public static final String COLUMN_VOICE_RX_LEVEL = "voiceRxLevel";
	public static final String COLUMN_VOICE_RX_QUALITY = "voiceRxQuality";
	public static final String COLUMN_VOICE_SINR = "voiceSinr";
	public static final String COLUMN_VOICE_TAC = "voiceTac";
	public static final String NV_ACTIVE_HBASE_TABLE = "NV_ACTIVE_HBASE_TABLE";
	public static final String NV_PASSIVE_HBASE_TABLE = "NV_PASSIVE_HBASE_TABLE";
	public static final String NUMBER_OF_METERS = "NUMBER_OF_METERS";
	
	
	


}

package com.inn.foresight.module.nv.core.generic.utils.hb;

/** The Interface HBaseProperties. */
public interface HBaseProperties {

	/** The hbase zookeeper quorum. */
	String HBASE_ZOOKEEPER_QUORUM = "hbase.zookeeper.quorum";
	
	/** The hbase zookeeper client port. */
	String HBASE_ZOOKEEPER_CLIENT_PORT = "hbase.zookeeper.property.clientPort";
	
	/** The hbase master. */
	String HBASE_MASTER = "hbase.master";
	
	/** The zookeeper znode. */
	String ZOOKEEPER_ZNODE = "zookeeper.znode.parent";
	
	/** The network type unknown id. */
	int NETWORK_TYPE_UNKNOWN_ID = 0;
	
	/** The active network type 2g id. */
	int ACTIVE_NETWORK_TYPE_2G_ID = 1;
	
	/** The active network type 3g id. */
	int ACTIVE_NETWORK_TYPE_3G_ID = 2;
	
	/** The active network type lte id. */
	int ACTIVE_NETWORK_TYPE_LTE_ID = 3;
	
	/** The active network type wifi id. */
	int ACTIVE_NETWORK_TYPE_WIFI_ID = 4;

	/** The passive network type 2g id. */
	int PASSIVE_NETWORK_TYPE_2G_ID = 5;
	
	/** The passive network type 3g id. */
	int PASSIVE_NETWORK_TYPE_3G_ID = 6;
	
	/** The passive network type lte id. */
	int PASSIVE_NETWORK_TYPE_LTE_ID = 7;
	
	/** The passive network type wifi id. */
	int PASSIVE_NETWORK_TYPE_WIFI_ID = 8;
	
	/** The passive service data 2g id. */
	int PASSIVE_SERVICE_DATA_2G_ID = 9;
	
	/** The passive service data 3g id. */
	int PASSIVE_SERVICE_DATA_3G_ID = 10;
	
	/** The passive service data lte id. */
	int PASSIVE_SERVICE_DATA_LTE_ID = 11;
	
	/** The passive service data wifi id. */
	int PASSIVE_SERVICE_DATA_WIFI_ID = 12;
	
	/** The passive app data. */
	int PASSIVE_APP_DATA = 13;
	
	/** The passive network type none id. */
	int PASSIVE_NETWORK_TYPE_NONE_ID = 14;

	/** The active 2g imei index. */
	int ACTIVE_2G_IMEI_INDEX = 45;
	
	/** The active 3g imei index. */
	int ACTIVE_3G_IMEI_INDEX = 46;
	
	/** The active lte imei index. */
	int ACTIVE_LTE_IMEI_INDEX = 52;
	
	/** The active wifi imei index. */
	int ACTIVE_WIFI_IMEI_INDEX = 42;

	/** The passive 2g imei index. */
	int PASSIVE_2G_IMEI_INDEX = 16;
	
	/** The passive 3g imei index. */
	int PASSIVE_3G_IMEI_INDEX = 17;
	
	/** The passive lte imei index. */
	int PASSIVE_LTE_IMEI_INDEX = 19;
	
	/** The passive wifi imei index. */
	int PASSIVE_WIFI_IMEI_INDEX = 9;
	
	/** The passive none imei index. */
	int PASSIVE_NONE_IMEI_INDEX = 4;

	/** The passive service 2g imei index. */
	int PASSIVE_SERVICE_2G_IMEI_INDEX = 17;
	
	/** The passive service 3g imei index. */
	int PASSIVE_SERVICE_3G_IMEI_INDEX = 18;
	
	/** The passive service lte imei index. */
	int PASSIVE_SERVICE_LTE_IMEI_INDEX = 20;
	
	/** The passive service wifi imei index. */
	int PASSIVE_SERVICE_WIFI_IMEI_INDEX = 10;
	
	/** The passive app data imei index. */
	int PASSIVE_APP_DATA_IMEI_INDEX = 1;

	/** The network type 2g. */
	String NETWORK_TYPE_2G = "2G";
	
	/** The network type 3g. */
	String NETWORK_TYPE_3G = "3G";
	
	/** The network type lte. */
	String NETWORK_TYPE_LTE = "LTE";
	
	/** The network type wifi. */
	String NETWORK_TYPE_WIFI = "WIFI";
	
	/** The data type service data. */
	String DATA_TYPE_SERVICE_DATA = "ServiceData";
	
	/** The data type app data. */
	String DATA_TYPE_APP_DATA = "AppData";
	
	/** The test type active. */
	String TEST_TYPE_ACTIVE = "ACTIVE";
	
	/** The test type passive. */
	String TEST_TYPE_PASSIVE = "PASSIVE";
	
	/** The network type none. */
	String NETWORK_TYPE_NONE = "NONE";

	/** The csv seperator. */
	String CSV_SEPERATOR = ",";

	/** The csv network type index. */
	int CSV_NETWORK_TYPE_INDEX = 0;
	
	/** The service data network type index. */
	int SERVICE_DATA_NETWORK_TYPE_INDEX = 1;
	
	/** The bss id. */
	String BSS_ID = "bssId";
	
	/** The client operator name. */
	String CLIENT_OPERATOR_NAME = "clientOperatorName";
	
	/** The operator name. */
	String OPERATOR_NAME = "operatorName";
	
	/** The client ping ip address. */
	String CLIENT_PING_IP_ADDRESS = "clientPingIpAddress";
	
	/** The destination ping ip address. */
	String DESTINATION_PING_IP_ADDRESS = "destinationPingIpAddress";
	
	/** The device os. */
	String DEVICE_OS = "deviceOS";
	
	/** The ended on. */
	String ENDED_ON = "endedOn";
	
	/** The id. */
	String ID = "id";
	
	/** The imsi. */
	String IMSI = "imsi";
	
	/** The model. */
	String MODEL = "model";
	
	/** The make. */
	String MAKE = "make";
	
	/** The imei. */
	String IMEI = "imei";
	
	/** The network subtype. */
	String NETWORK_SUBTYPE = "networkSubtype";
	
	/** The security type. */
	String SECURITY_TYPE = "securityType";
	
	/** The ssid. */
	String SSID = "ssid";
	
	/** The url 1. */
	String URL_1 = "url1";
	
	/** The url 2. */
	String URL_2 = "url2";
	
	/** The url 3. */
	String URL_3 = "url3";
	
	/** The started on. */
	String STARTED_ON = "startedOn";
	
	/** The captured on. */
	String CAPTURED_ON = "capturedOn";
	
	/** The type. */
	String TYPE = "type";
	
	/** The network type. */
	String NETWORK_TYPE = "networkType";
	
	/** The avg browse time. */
	String AVG_BROWSE_TIME = "avgBrowseTime";
	
	/** The avg dl rate. */
	String AVG_DL_RATE = "avgDlRate";
	
	/** The avg ec io. */
	String AVG_EC_IO = "avgEcIo";
	
	/** The avg ec no. */
	String AVG_EC_NO = "avgEcNo";
	
	/** The avg latency. */
	String AVG_LATENCY = "avgLatency";
	
	/** The avg link speed. */
	String AVG_LINK_SPEED = "avgLinkspeed";
	
	/** The avg noise. */
	String AVG_NOISE = "avgNoise";
	
	/** The avg rscp. */
	String AVG_RSCP = "avgRscp";
	
	/** The avg rsrp. */
	String AVG_RSRP = "avgRsrp";
	
	/** The avg rsrq. */
	String AVG_RSRQ = "avgRsrq";
	
	/** The avg rssi. */
	String AVG_RSSI = "avgRssi";
	
	/** The avg rx level. */
	String AVG_RX_LEVEL = "avgRxLevel";
	
	/** The avg rx quality. */
	String AVG_RX_QUALITY = "avgRxquality";
	
	/** The avg signal. */
	String AVG_SIGNAL = "avgSignal";
	
	/** The avg sinr. */
	String AVG_SINR = "avgSinr";
	
	/** The avg snr. */
	String AVG_SNR = "avgSnr";
	
	/** The avg ul rate. */
	String AVG_UL_RATE = "avgUlRate";
	
	/** The cell id. */
	String CELL_ID = "cellId";
	
	/** The channel. */
	String CHANNEL = "channel";
	
	/** The frequency. */
	String FREQUENCY = "frequency";
	
	/** The gps accuracy. */
	String GPS_ACCURACY = "gpsAccuracy";
	
	/** The is uploaded. */
	String IS_UPLOADED = "isUploaded";
	
	/** The jitter. */
	String JITTER = "jitter";
	
	/** The lac. */
	String LAC = "lac";
	
	/** The latitude. */
	String LATITUDE = "latitude";
	
	/** The longitude. */
	String LONGITUDE = "longitude";
	
	/** The max browse time. */
	String MAX_BROWSE_TIME = "maxBrowseTime";
	
	/** The max dl rate. */
	String MAX_DL_RATE = "maxDlRate";
	
	/** The max ec io. */
	String MAX_EC_IO = "maxEcIo";
	
	/** The max ec no. */
	String MAX_EC_NO = "maxEcNo";
	
	/** The max latency. */
	String MAX_LATENCY = "maxLatency";
	
	/** The max link speed. */
	String MAX_LINK_SPEED = "maxLinkspeed";
	
	/** The max noise. */
	String MAX_NOISE = "maxNoise";
	
	/** The max rscp. */
	String MAX_RSCP = "maxRscp";
	
	/** The max rsrp. */
	String MAX_RSRP = "maxRsrp";
	
	/** The max rsrq. */
	String MAX_RSRQ = "maxRsrq";
	
	/** The max rssi. */
	String MAX_RSSI = "maxRssi";
	
	/** The max rx level. */
	String MAX_RX_LEVEL = "maxRxLevel";
	
	/** The max rx quality. */
	String MAX_RX_QUALITY = "maxRxquality";
	
	/** The max signal. */
	String MAX_SIGNAL = "maxSignal";
	
	/** The max sinr. */
	String MAX_SINR = "maxSinr";
	
	/** The max snr. */
	String MAX_SNR = "maxSinr";
	
	/** The max ul rate. */
	String MAX_UL_RATE = "maxUlRate";
	
	/** The mcc. */
	String MCC = "mcc";
	
	/** The mnc. */
	String MNC = "mnc";
	
	/** The pci. */
	String PCI = "pci";
	
	/** The min browse time. */
	String MIN_BROWSE_TIME = "minBrowseTime";
	
	/** The min dl rate. */
	String MIN_DL_RATE = "minDlRate";
	
	/** The min ec io. */
	String MIN_EC_IO = "minEcIo";
	
	/** The min ec no. */
	String MIN_EC_NO = "minEcNo";
	
	/** The min latency. */
	String MIN_LATENCY = "minLatency";
	
	/** The min link speed. */
	String MIN_LINK_SPEED = "minLinkspeed";
	
	/** The min noise. */
	String MIN_NOISE = "minNoise";
	
	/** The min rscp. */
	String MIN_RSCP = "minRscp";
	
	/** The min rsrp. */
	String MIN_RSRP = "minRsrp";
	
	/** The min rsrq. */
	String MIN_RSRQ = "minRsrq";
	
	/** The min rssi. */
	String MIN_RSSI = "minRssi";
	
	/** The min rx level. */
	String MIN_RX_LEVEL = "minRxLevel";
	
	/** The min rx quality. */
	String MIN_RX_QUALITY = "minRxquality";
	
	/** The min signal. */
	String MIN_SIGNAL = "minSignal";
	
	/** The min sinr. */
	String MIN_SINR = "minSinr";
	
	/** The min snr. */
	String MIN_SNR = "minSinr";
	
	/** The min ul rate. */
	String MIN_UL_RATE = "minUlRate";
	
	/** The pckt loss. */
	String PCKT_LOSS = "pcktLoss";
	
	/** The pckt received. */
	String PCKT_RECEIVED = "pcktReceived";
	
	/** The pckt transmitted. */
	String PCKT_TRANSMITTED = "pcktTransmitted";
	
	/** The psc. */
	String PSC = "psc";
	
	/** The tac. */
	String TAC = "tac";
	
	/** The time. */
	String TIME = "time";
	
	/** The url 1 browse time. */
	String URL_1_BROWSE_TIME = "url1BrowseTime";
	
	/** The url 2 browse time. */
	String URL_2_BROWSE_TIME = "url2BrowseTime";
	
	/** The url 3 browse time. */
	String URL_3_BROWSE_TIME = "url3BrowseTime";
	
	/** The url 1 response code. */
	String URL_1_RESPONSE_CODE = "url1ResponseCode";
	
	/** The url 2 response code. */
	String URL_2_RESPONSE_CODE = "url2ResponseCode";
	
	/** The url 3 response code. */
	String URL_3_RESPONSE_CODE = "url3ResponseCode";
	
	/** The user id. */
	String USER_ID = "userId";
	
	/** The gps time. */
	String GPS_TIME = "gpsTime";

	/** The data. */
	String DATA = "data";

	/** The drive data. */
	String DRIVE_DATA = "data";
	
	/** The drive created time. */
	String DRIVE_CREATED_TIME = "ct";
	
	/** The drive started on. */
	String DRIVE_STARTED_ON = "so";
	
	/** The drive end on. */
	String DRIVE_END_ON = "eo";
	
	/** The drive provider name. */
	String DRIVE_PROVIDER_NAME = "pn";

	/** The geo network type. */
	String GEO_NETWORK_TYPE = "networkType";
	
	/** The a started on. */
	String A_STARTED_ON = "aStartedOn";
	
	/** The z end on. */
	String Z_END_ON = "zEndOn";
	
	/** The geo imei. */
	String GEO_IMEI = "imei";
	
	/** The geo imsi. */
	String GEO_IMSI = "imsi";
	
	/** The geo make. */
	String GEO_MAKE = "make";
	
	/** The geo model. */
	String GEO_MODEL = "model";
	
	/** The geo os. */
	String GEO_OS = "os";
	
	/** The geo atest type. */
	String GEO_ATEST_TYPE = "atestType";
	
	/** The geo route id. */
	String GEO_ROUTE_ID = "routeId";
	
	/** The geo user id. */
	String GEO_USER_ID = "userId";
	
	/** The geo total time. */
	String GEO_TOTAL_TIME = "totalTime";
	
	/** The geo total distance. */
	String GEO_TOTAL_DISTANCE = "totalDistance";
	
	/** The geo workoeder id. */
	String GEO_WORKOEDER_ID = "workorderId";
	
	/** The geo task id. */
	String GEO_TASK_ID = "taskId";
	
	/** The geo source file name. */
	String GEO_SOURCE_FILE_NAME = "sourceFileName";

	/** The active lte ntification test type index. */
	int ACTIVE_LTE_NTIFICATION_TEST_TYPE_INDEX = 72;
	
	/** The active 2g ntification test type index. */
	int ACTIVE_2G_NTIFICATION_TEST_TYPE_INDEX = 64;
	
	/** The active 3g ntification test type index. */
	int ACTIVE_3G_NTIFICATION_TEST_TYPE_INDEX = 65;
	
	/** The active wifi ntification test type index. */
	int ACTIVE_WIFI_NTIFICATION_TEST_TYPE_INDEX = 64;

	/** The active notification lte id index. */
	int ACTIVE_NOTIFICATION_LTE_ID_INDEX = 80;
	
	/** The active notification 2g id index. */
	int ACTIVE_NOTIFICATION_2G_ID_INDEX = 72;
	
	/** The active notification 3g id index. */
	int ACTIVE_NOTIFICATION_3G_ID_INDEX = 73;
	
	/** The active notification wifi id index. */
	int ACTIVE_NOTIFICATION_WIFI_ID_INDEX = 87;

	/** The wpt network type index. */
	int WPT_NETWORK_TYPE_INDEX = 1;

	/** The wpt network type 2g id. */
	int WPT_NETWORK_TYPE_2G_ID = 1;
	
	/** The wpt network type 3g id. */
	int WPT_NETWORK_TYPE_3G_ID = 2;
	
	/** The wpt network type lte id. */
	int WPT_NETWORK_TYPE_LTE_ID = 3;
	
	/** The wpt network type wifi id. */
	int WPT_NETWORK_TYPE_WIFI_ID = 4;

	/** The wpt imei index. */
	int WPT_IMEI_INDEX = 6;
	
	/** The wpt test starttime index. */
	int WPT_TEST_STARTTIME_INDEX = 29;

}

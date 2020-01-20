package com.inn.foresight.module.nv.dashboard.wifi.utils.constants;

public class WIFIPerformanceConstants {

	private WIFIPerformanceConstants() {
	}

	public static final String FLOOR_PERFORMANCE_MICROSERVICE_REST_URL = "/rest/ms/WIFIAPDashboard/floorPerformance";

	public static final String FLOOR_STATS_MICROSERVICE_REST_URL = "/rest/ms/WIFIAPDashboard/floorWiseAPData";

	public static final String AP_PERFORMANCE_MICROSERVICE_REST_URL = "/rest/ms/WIFIAPDashboard/apPerformance";

	public static final String WIFI_PERFORMANCE_HBASE_TABLE = "WiFiPerformance";

	public static final String COLUMN_FAMILY = "r";

	public static final String TOTAL_SAMPLES_COLUMN = "ts";

	public static final String TOTAL_USERS_COLUMN = "tu";

	public static final String RSSI_JSON_COLUMN = "rssi";

	public static final String DL_JSON_COLUMN = "dl";
	public static final String SNR_JSON_COLUMN = "snr";
	public static final String UL_JSON_COLUMN = "ul";
	public static final String TTL_JSON_COLUMN = "ttl";
	public static final String DNS_JSON_COLUMN = "dns";
	public static final String BUFFERTIME_JSON_COLUMN = "buffertime";
	public static final String PACKETLOSS_JSON_COLUMN = "packetloss";

	public static final String LATENCY_JSON_COLUMN = "latency";

	public static final String AP_COUNT_COLUMN = "apcnt";

	public static final String AP_DISTRIBUTION_COLUMN = "apdist";

	public static final String PERFORMANCE_COLUMN = "performance";
	public static final String RSSI_PERFORMANCE_COLUMN = "rssiperformance";
	public static final String SNR_PERFORMANCE_COLUMN = "snrperformance";

	public static final String HOURLY_USER_DISTRIBUTION_COLUMN = "hourlyuser";
	public static final String HOURLY_SAMPLE_DISTRIBUTION_COLUMN = "hourlysample";

	public static final String RSSI_HOURLY_JSON_COLUMN = "rssihourly";
	public static final String SNR_HOURLY_JSON_COLUMN = "snrhourly";
	public static final String DL_HOURLY_JSON_COLUMN = "dlhourly";
	public static final String UL_HOURLY_JSON_COLUMN = "ulhourly";

	public static final String DNS_HOURLY_JSON_COLUMN = "dnshourly";
	public static final String BUFFERTIME_HOURLY_JSON_COLUMN = "buffertimehourly";
	public static final String PL_HOURLY_JSON_COLUMN = "packetlosshourly";

	public static final String LATENCY_HOURLY_JSON_COLUMN = "latencyhourly";

	public static final String RSSIP_HOURLY_JSON_COLUMN = "rssiperformancehourly";
	public static final String SNRP_HOURLY_JSON_COLUMN = "snrperformancehourly";
	public static final String TTL_HOURLY_JSON_COLUMN = "ttlhourly";
	
	public static final String DL_SPEED_JSON_COLUMN = "dlspeed";
	
	

	public static final String STARTDATE = "startDate";

	public static final String FLOORID = "floorId";
	public static final String MACADDRESS = "macAddress";
	public static final String HOUR = "hour";
	
	

}

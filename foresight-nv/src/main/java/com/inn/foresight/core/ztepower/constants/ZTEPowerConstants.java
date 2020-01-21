package com.inn.foresight.core.ztepower.constants;

public class ZTEPowerConstants {
	protected ZTEPowerConstants() {
		super();
	}

	public static final String TYPE_AREA = "Area";
	public static final String TYPE_STATION = "Station";
	public static final String TYPE_DEVICE = "Device";
	public static final String COUNT = "count";
	public static final String DATA_MAP = "data";

	public static final String TYPE_AREA_NAME = "areaName";
	public static final String TYPE_STATION_NAME = "stationName";
	public static final String TYPE_DEVICE_NAME = "neName";
	public static final String TYPE_DEVICE_ID = "neId";
	public static final String TYPE_METE_ID = "meteId";
	public static final String TYPE_METE_NAME = "meteName";
	public static final String TYPE_METE_KIND = "meteKind";

	// Columns Keys
	public static final String KEY_NETWORK_ELEMENT_TABLE = "networkElement";
	public static final String KEY_ZTE_POWER_STATION_TABLE = "ztePowerStation";
	public static final String KEY_ZTE_POWER_REGION_TABLE = "ztePowerRegion";
	public static final String KEY_ZTE_POWER_METE_INFO_TABLE = "ztePowerMeteInfo";
	public static final String KEY_GEOGRAPHYL4_TABLE = "geographyL4";
	public static final String KEY_GEOGRAPHYL3_TABLE = "geographyL3";
	public static final String KEY_GEOGRAPHYL2_TABLE = "geographyL2";
	public static final String KEY_GEOGRAPHYL1_TABLE = "geographyL1";
	public static final String KEY_NE_NAME = "neName";
	public static final String KEY_NE_ID = "neId";
	public static final String KEY_AREA_NAME = "areaName";
	public static final String KEY_AREA_ID = "areaId";
	public static final String KEY_LSC_ID = "lscId";
	public static final String KEY_LSC_NAME = "lscName";
	public static final String KEY_NAME = "name";
	public static final String KEY_STATION_NAME = "stationName";
	public static final String KEY_STATION_ID = "stationId";
	public static final String KEY_VENDOR = "vendor";
	public static final String KEY_DOMAIN = "domain";
	public static final String KEY_METE_ID = "meteId";
	public static final String KEY_METE_NAME = "meteName";
	public static final String KEY_METE_KIND = "meteKind";
	public static final String KEY_IPV4 = "ipv4";
	public static final String REMARK_DELETED_FOR_DEVICE = "{\"message\":\"Device Deleted from ZTE Power\",\"deviceName\":\"%s\",\"deviceId\":\"%s\"}";
	public static final String REMARK_DELETED_FOR_STATION = "{\"message\":\"Station Deleted from ZTE Power\",\"stationName\":\"%s\",\"stationId\":\"%s\"}";
}
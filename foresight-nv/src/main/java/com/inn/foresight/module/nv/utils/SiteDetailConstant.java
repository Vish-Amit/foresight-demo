package com.inn.foresight.module.nv.utils;

public final class SiteDetailConstant {

	private SiteDetailConstant() {
		// Empty Constructor to Prevent Instantiation
	}

	public static final String NE_FILTER_NE_TYPE_MACRO_CELL = "{\"DATATYPE\":\"NEType\",\"OPERATION\":\"EQUALS\",\"VALUE\":\"MACRO_CELL\",\"LABEL_TYPE\":\"neType\"}";
	public static final String NE_FILTER_NE_TYPE_MACRO = "{\"DATATYPE\":\"NEType\",\"OPERATION\":\"EQUALS\",\"VALUE\":\"MACRO\",\"LABEL_TYPE\":\"neType\"}";
	public static final String NE_FILTER_NE_STATUS_ON_AIR = "{\"DATATYPE\":\"NEStatus\",\"OPERATION\":\"EQUALS\",\"VALUE\":\"ONAIR\",\"LABEL_TYPE\":\"neStatus\"}";
	public static final String NE_FILTER_NE_STATUS_PLANNED = "{\"DATATYPE\":\"NEStatus\",\"OPERATION\":\"EQUALS\",\"VALUE\":\"PLANNED\",\"LABEL_TYPE\":\"neStatus\"}";
	public static final String NE_FILTER_NE_TYPE_WIFI= "{\"DATATYPE\":\"NEType\",\"OPERATION\":\"EQUALS\",\"VALUE\":\"WIFI\",\"LABEL_TYPE\":\"neType\"}";
	

	public static final String TN_NETWORK_ELEMENT = "NetworkElement";
	public static final String TN_RAN_DETAIL = "RANDetail";
	public static final String TN_GEOGRAPHY_L4 = "GeographyL4";
	public static final String TN_GEOGRAPHY_L3 = "GeographyL3";
	public static final String TN_GEOGRAPHY_L2 = "GeographyL2";
	public static final String TN_GEOGRAPHY_L1 = "GeographyL1";
	
	public static final String NE_PROJECTION_ENBID = "enbid";
	public static final String NE_PROJECTION_ECGI = "ecgi";
	public static final String NE_PROJECTION_NE_TYPE = "neType";
	public static final String NE_PROJECTION_NE_STATUS = "neStatus";
	public static final String NE_PROJECTION_NE_NAME = "neName";
	public static final String NE_PROJECTION_LATITUDE = "latitude";
	public static final String NE_PROJECTION_LONGITUDE = "longitude";
	public static final String NE_PROJECTION_CELL_ID = "cellNum";
	public static final String NE_PROJECTION_NE_FREQUENCY = "neFrequency";
	public static final String NE_PROJECTION_MODIFICATIONTIME = "modifiedTime";
	public static final String RAN_PROJECTION_MODIFICATIONTIME = "modificationTime";
	public static final String NE_PROJECTION_ID = "id";
	public static final String NE_PROJECTION_NE_ID = "neId";
	public static final String NE_PROJECTION_PARENT_NE_NAME = "parentneName";
	public static final String NE_PROJECTION_PARENT_NETYPE = "parentneType";
	public static final String NE_PROJECTION_MCC = "mcc";
	public static final String NE_PROJECTION_MNC = "mnc";

	public static final String NE_PROJECTION_FRIENDLY_NAME = "friendlyname";
	public static final String NE_PROJECTION_PARENT_FRIENDLY_NAME = "parentfriendlyname";

	public static final String NE_PROJECTION_TECHNOLOGY = "technology";
	
	public static final String RAN_PROJECTION_ANT_HEIGHT = "antennaHeight";
	public static final String RAN_PROJECTION_ANT_MODEL = "antennaModel";
	public static final String RAN_PROJECTION_ANT_TYPE = "antennaType";
	public static final String RAN_PROJECTION_PCI = "pci";
	public static final String RAN_PROJECTION_AZIMUTH = "azimuth";
	public static final String RAN_PROJECTION_MECH_TILT = "mechTilt";
	public static final String RAN_PROJECTION_ELEC_TILT = "elecTilt";
	public static final String RAN_PROJECTION_TAC = "trackingArea";
	public static final String RAN_PROJECTION_SECTOR = "sector";
	public static final String RAN_PROJECTION_TXPOWER = "txPower";
	public static final String RAN_PROJECTION_RET_STATUS = "retstatus";
	public static final String RAN_PROJECTION_ADMIN_STATE = "adminState";
	
	public static final String FILTER_COLUMNS_NE_STATUS = "neStatus";
	public static final String FILTER_COLUMNS_NE_TYPE = "neType";
	public static final String FILTER_COLUMNS_DATATYPE_NE_STATUS = "NEStatus";
	public static final String FILTER_COLUMNS_DATATYPE_NE_TYPE = "NEType";
	public static final String RAN_PROJECTION_EARFCN = "earfcn";
	public static final String RAN_PROJECTION_DL_EARFCN = "dlearfcn";
	public static final String RAN_PROJECTION_HORIZONTAL_BEAMWIDTH = "horizontalBeamWidth";
	public static final String RAN_PROJECTION_BANDWIDTH = "bandwidth";
	
	public static final String GEOGRAPHY_PROJECTION_NAME = "name";
	public static final String GEOGRAPHY_PROJECTION_DISPLAY_NAME = "displayname";
	
	public static final String GEOGRAPHY_L4_PROJECTION_NAME = "L4name";
	public static final String GEOGRAPHY_L4_PROJECTION_ID = "L4id";
	public static final String GEOGRAPHY_L3_PROJECTION_NAME = "L3name";
	public static final String GEOGRAPHY_L2_PROJECTION_NAME = "L2name";
	public static final String GEOGRAPHY_L1_PROJECTION_NAME = "L1name";
	
	public static final String GEOGRAPHY_L4_PROJECTION_DISPLAY_NAME = "L4displayname";
	public static final String GEOGRAPHY_L3_PROJECTION_DISPLAY_NAME = "L3displayname";
	public static final String GEOGRAPHY_L2_PROJECTION_DISPLAY_NAME = "L2displayname";
	public static final String GEOGRAPHY_L1_PROJECTION_DISPLAY_NAME = "L1displayname";
	
	public static final String FILTER_COLUMNS_NE_NAME = "neName";
	public static final String FILTER_COLUMNS_NE_FREQUENCY = "neFrequency";
	public static final String FILTER_COLUMNS_NE_TECHNOLOGY = "technology";
	public static final String FILTER_COLUMNS_GEOGRAPHY_NAME = "name";
	public static final String FILTER_COLUMNS_RAN_CGI = "cgi";
	public static final String FILTER_COLUMNS_RAN_PCI = "pci";
	public static final String FILTER_OPERATION_IN = "IN";
	public static final String FILTER_OPERATION_EQUALS = "EQUALS";
	public static final String FILTER_DATA_TYPE_STRING = "String";
	public static final String FILTER_DATA_TYPE_INTEGER = "Integer";
	public static final String RAN_PROJECTION_CGI = "cgi";
	public static final String FILTER_DATA_TYPE_TIMESTAMP = "TimeStamp";
	public static final Object NE_PROJECTION_PARENT_NESTATUS = "parentneStatus";
	public static final String FILTER_DATA_COLUMN_MACADDRESS= "macaddress";
	public static final String RAN_PROJECTION_COLUMN_CHANNEL= "channel";
	public static final String NE_PROJECTION_COLUMN_IPV4="ipv4";
	public static final String NE_PROJECTION_PARENT_LATITUDE = "parentlatitude";
	public static final String NE_PROJECTION_PARENT_LONGITUDE = "parentlongitude";
	public static final String NE_PROJECTION_PARENT_MODIFIEDTIME ="parentmodifiedTime";
	public static final String NE_PROJECTION_SOFTWAREVERSION = "swBuildNumber";
	public static final String NE_PROJECTION_PARENT_SOFTWAREVERSION = "parentswBuildNumber";
	public static final String CUSTOM_NETWORK_ELEMENT = "CustomNEDetail";
	public static final String CUSTOM_NE_PROJECTION_TIREADY = "tiReady";
	public static final String FILTER_DATA_TYPE_BOOLEAN = "boolean";
	public static final String TN_NEBAND_DETAIL = "NEBandDetail";
	public static final String NEBAND_PROJECTION_BANDONAIRDATE = "bandOnairDate";
	public static final String TI_READY_DATE = "tiDate";
	public static final String FLOOR_NAME= "floorName";
	public static final String BUILDING_NAME= "buildingName";
	

	
}

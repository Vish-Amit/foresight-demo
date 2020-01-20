package com.inn.foresight.module.nv.report.utils;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

/** The Class DriveHeaderConstants. */
public interface DriveHeaderConstants {

	/** The Constant INDEX_LAT. */
	Integer INDEX_LAT = 0;

	/** The Constant INDEX_LON. */
	Integer INDEX_LON = 1;

	/** The Constant INDEX_TIMESTAMP. */
	Integer INDEX_TIMESTAMP = 2;

	/** The Constant INDEX_PCI. */
	Integer INDEX_PCI = 3;

	/** The Constant INDEX_RSRP. */
	Integer INDEX_RSRP = 5;

	/** The Constant INDEX_RSRQ. */
	Integer INDEX_RSRQ = 6;

	/** The Constant INDEX_SINR. */
	Integer INDEX_SINR = 7;

	/** The Constant INDEX_DL. */
	Integer INDEX_DL = 21;

	/** The Constant INDEX_UL. */
	Integer INDEX_UL = 22;

	/** The Constant INDEX_CELLID. */
	Integer INDEX_CELLID = 35;
	Integer INDEX_ENBID = 51;

	Integer INDEX_XPOINT = 37;

	Integer INDEX_YPOINT = 38;

	/** The Constant INDEX_JITTER. */
	Integer INDEX_JITTER = 39;

	/** The Constant INDEX_WEBDELAY. */
	Integer INDEX_WEBDELAY = 40;

	/** The Constant SUMMARY_RSRP_INDEX. */
	Integer SUMMARY_RSRP_INDEX = 3;

	Integer SUMMARY_MIN_RSRP_INDEX = 47;

	Integer SUMMARY_MAX_RSRP_INDEX = 60;

	/** The Constant SUMMARY_SINR_INDEX. */
	Integer SUMMARY_SINR_INDEX = 5;

	Integer SUMMARY_MIN_SINR_INDEX = 48;

	Integer SUMMARY_MAX_SINR_INDEX = 61;

	/** The Constant SUMMARY_UL_INDEX. */
	Integer SUMMARY_UL_INDEX = 7;

	Integer SUMMARY_MIN_UL_INDEX = 40;

	Integer SUMMARY_MAX_UL_INDEX = 53;

	/** The Constant SUMMARY_DL_INDEX. */
	Integer SUMMARY_DL_INDEX = 6;

	Integer SUMMARY_MIN_DL_INDEX = 39;

	Integer SUMMARY_MAX_DL_INDEX = 52;

	Integer SUMMARY_LATENCY_INDEX = 8;

	Integer SUMMARY_MIN_LATENCY_INDEX = 38;

	Integer SUMMARY_MAX_LATENCY_INDEX = 51;

	Integer SUMMARY_JITTER_INDEX = 64;

	Integer SUMMARY_MIN_JITTER_INDEX = 45;

	Integer SUMMARY_MAX_JITTER_INDEX = 58;

	Integer SUMMARY_RESPONSE_TIME_INDEX = 65;

	Integer SUMMARY_MIN_RESPONSE_TIME_INDEX = 46;

	Integer SUMMARY_MAX_RESPONSE_TIME_INDEX = 59;

	/** No field available in summary data. */
	Integer SUMMARY_IMEI_INDEX = 54;

	Integer SUMMARY_START_TIME_INDEX = 0;

	Integer SUMMARY_END_TIME_INDEX = 1;

	Integer SUMMARY_CELL_ID_INDEX = 31;

	Integer SUMMARY_LATITUDE_INDEX = 100;  

	Integer SUMMARY_LONGITUDE_INDEX = 101;

	/** The Constant SUMMARY_ADDRESS_INDEX. */
	Integer SUMMARY_ADDRESS_INDEX = 19;
	Integer SUMMARY_TECH_INDEX = 33;
	Integer SUMMARY_EARFCN_INDEX = 37;      
	/** Summary HTTP INDEX. */
	
	Integer SUMMARY_DL_HTTP_AVG=67;
	Integer SUMMARY_HTTP_DOWNLOAD_TIME=110;
	Integer SUMMARY_HTTP_ATTEMPT=111;
	Integer SUMMARY_HTTP_SUCCESS=112;
	Integer SUMMARY_PACKETLOSS=109; 
	Integer INDEX_DL_EARFCN = 8;
	Integer BANDWIDTH_DL_INDEX = 10;
	Integer BANDWIDTH_UL_INDEX = 11;
	Integer TECHNOLOGY_INDEX=34;

	Integer SUMMARY_PDSCH_THP_AVG = 127;
	Integer SUMMARY_PDSCH_THP_MIN = 177;
	Integer SUMMARY_PDSCH_THP_MAX = 178;

	Integer SUMMARY_PUSCH_THP_AVG = 141;
	Integer SUMMARY_PUSCH_THP_MIN = 186;
	Integer SUMMARY_PUSCH_THP_MAX = 187;

	/** New index. */
	Integer INDEXCQI = 17;
	Integer INDEXMCS = 36;

	Integer INDEXRI = 16;
	Integer INDEXROUTE = 1;

	Object INDEX_EARFCN = "";
	
	Integer INDEX_NETWORK_TYPE = 44;

	/** The Constant INDEX_UL. */
	Integer INDEX_LATENCY = 23;

	Integer INDEX_WIFI_RSSI = 41;

	Integer INDEX_WIFI_SNR = 42;
	
	Integer INDEX_INST_MOS = 89;
	
	//Volte KPI Index
	Integer SUMMARY_VOLTE_JITTER_DLF=180;
	Integer SUMMARY_VOLTE_PKT_LOSS=181;
	Integer SUMMARY_IMS_REG_SETUP_TIME=170;
	
	Integer SUMMARY_VOLTE_MT_CALL_ATTEMPTS=173;
	Integer SUMMARY_VOLTE_MT_CALL_SUCCESS=174;
	
	Integer SUMMARY_VOLTE_MO_CALL_ATTEMPTS=175;
	Integer SUMMARY_VOLTE_MO_CALL_SUCCESS=176;

	Integer SUMMARY_RACH_ATTEMPT_INDEX = 201;
	Integer SUMMARY_RACH_SUCCESS_INDEX = 200;

	Integer SUMMARY_CALL_SETUP_SUCCESS_INDEX = 191;


	/**
	 * 17 Gets the kpi index map.
	 *
	 * @return the kpi index map
	 */
	String RSRP="RSRP";
	String MIN_RSRP="MIN_RSRP";
	String MAX_RSRP="MAX_RSRP";

	String SINR="SINR";
	String RSRQ = "RSRQ";
	String MIN_SINR="MIN_SINR";
	String MAX_SINR="MAX_SINR";
	String DL_THROUGHPUT="DL_THROUGHPUT";
	String MIN_DL_THROUGHPUT="MIN_DL_THROUGHPUT";
	String MAX_DL_THROUGHPUT="MAX_DL_THROUGHPUT";
	String BANDWIDTH_DL="BANDWIDTH_DL";

	String UL_THROUGHPUT="UL_THROUGHPUT";
	String MIN_UL_THROUGHPUT="MIN_UL_THROUGHPUT";
	String MAX_UL_THROUGHPUT="MAX_UL_THROUGHPUT";

	String WIFI_RSSI="RSSI";
	String WIFI_SNR="SNR";

	String JITTER="JITTER";
	String CONNECTION_SETUP_TIME="Connection Setup Time";
	String RESPONSE_TIME="RESPONSE_TIME";
	String LATENCY="LATENCY";

	String PCI_PLOT="PCI_PLOT";

	String CQI="CQI";

	String MCS="MCS";
	String ROUTE="ROUTE";

	String MIMO="MIMO";
	String CGI="CGI";
	String DL_EARFCN="DL_EARFCN";
	
	String UL="UL THROUGHPUT (Mbps)";
	String DL="DL THROUGHPUT (Mbps)";
	String DL_FTP="FTP DL THROUGHPUT (Mbps)";
	String DL_HTTP="HTTP DL THROUGHPUT (Mbps)";
	String UL_FTP="FTP UL THROUGHPUT (Mbps)";
	String UL_HTTP="HTTP UL THROUGHPUT (Mbps)";
	
	
	

	Integer SUMMARY_CALL_ATTEMPTS = 12;
	Integer SUMMARY_CALL_DROP=13;
	Integer SUMMARY_CALL_FAILURE = 14;
	Integer SUMMARY_CALL_SUCCESS = 15;
	Integer SUMMARY_CALL_SETUP_TIME = 102;
	Integer SUMMARY_CALL_SETUP_TIME_ON_NET = 103;
	Integer SUMMARY_CALL_SETUP_TIME_OFF_NET = 104;

	Integer SUMMARY_TOTAL_CALL=18;
	Integer SUMMARY_CALL_ATTEMPT_ON_NET = 86;
	Integer SUMMARY_CALL_DROP_ON_NET = 87;
	Integer SUMMARY_CALL_FAILURE_ON_NET = 88;
	Integer SUMMARY_CALL_SUCCESS_ON_NET = 89;
	Integer SUMMARY_CALL_ATTEMPT_OFF_NET = 90;
	Integer SUMMARY_CALL_DROP_OFF_NET = 91;
	Integer SUMMARY_CALL_FAILURE_OFF_NET = 92;
	Integer SUMMARY_CALL_SUCCESS_OFF_NET = 93;


	Integer SUMMARY_SMS_ATTEMPT = 74;
	Integer SUMMARY_SMS_SUCCESS = 75;
	Integer SUMMARY_SMS_DELIVERED_IN_LESS_THAN_3_MIN = 77;
	Integer SUMMARY_SMS_DELIVEREY_AVG_TIME = 76;
	Integer SUMMARY_SMS_DELIVEREY_SUM = 76;

	Integer SUMMARY_SMS_ATTEMPT_ON_NET = 78;
	Integer SUMMARY_SMS_SUCCESFULL_ON_NET = 79;
	Integer SUMMARY_SMS_DELIVERED_SUM_ON_NET = 80;
	Integer SUMMARY_SMS_DELIVERED_LESS_THAN_3_MIN_ON_NET = 81;
	
	Integer SUMMARY_SMS_ATTEMPT_OFF_NET = 82;
	Integer SUMMARY_SMS_SUCCESFULL_OFF_NET = 83;
	Integer SUMMARY_SMS_DELIVERED_SUM_OFF_NET = 84;
	Integer SUMMARY_SMS_DELIVERED_LESS_THAN_3_MIN_OFF_NET = 85;


	Integer INDEX_YT_URL = 0;
	Integer INDEX_YT_RESOLUTION = 1;
	Integer INDEX_YT_DURATION_IN_SECOND = 2;
	Integer INDEX_YT_NO_OF_STALLING = 3;
	Integer INDEX_YT_TOTAL_BUFFERING_TIME = 4;
	Integer INDEX_YT_FREEZING_RATIO = 5;
	Integer INDEX_YT_AVG_RSRP = 6;
	Integer INDEX_YT_AVG_SINR = 7;
	Integer INDEX_HTTP_DL_TOTAL_ATTEMPTS=0;
	Integer INDEX_HTTP_DL_TOTAL_SUCCESS=1;
	Integer INDEX_HTTP_DL_TOTAL_SUCCESS_RATE=2;
	Integer INDEX_HTTP_DL_AVG_TIME=3;
	Integer INDEX_HTTP_DL=4;
	Integer INDEX_PDSCH_THROUGHPUT = 91;
	Integer INDEX_PUSCH_THROUGHPUT = 127;
	Integer INDEX_MAC_DL_THROUGHPUT = 194;
	Integer INDEX_MAC_UL_THROUGHPUT = 154;

	Integer INDEX_QT_DL_THROUGHPUT = 0;
	Integer INDEX_QT_UL_THROUGHPUT = 1;
	Integer INDEX_QT_LATENCY = 2;
	Integer INDEX_QT_AVG_RSRP = 3;
	Integer INDEX_QT_AVG_SINR = 4;
	Integer INDEX_HO_LATENCY=53;
	Integer INDEX_TARGET_PCI=52;
	Integer INDEX_SOURCE_PCI=51;
    Integer INDEX_HANDOVER_SUCCESS=26;
    Integer INDEX_CALL_SETUP_SUCCESS=199;
    Integer INDEX_CALL_DROP=28;
    Integer INDEX_CALL_FAIL=29;
    Integer INDEX_CALL_SUCCESS=30;
	String CALL_ATTEMPTS = "Call Attempts";
	String FAILED_CALL = "Blocked/Failed Call";
	String DROPPED_CALL = "Dropped Call";
	String INITIATE_CALL = "Initiate Call";
	String FAIL_CALL = "Fail Call";
	String SUCCESS_CALL = "Success Call";
	
	String SUCCESS_CALL_PERCENTAGE = "Success Call (%)";
	String CALL_DROPPED_PERCENTAGE = "Dropped Call (%)";
	String CALL_SETUP_TIME = "Call Setup Time (ms)";
	String WEB_PAGE_DOWNLOAD_DELAY = "Web page download delay(s)";
	String SMS_ATTEMPTS = "SMS Attempts";
	String SMS_DELIVERED_AVERAGE_SECOND = "SMS Delivered Average (Second)";
	String SMS_DELIVERED_LESS_THAN_3_MIN = "SMS Delivered (< 3 min)";
	String SMS_DELIVERED_LESS_THAN_3_MIN_PERCENTAGE = "% SMS Delivered (< 3 min)";


	String TOTAL_SMS = "TOTAL SMS";
	String TOTAL_VOICE = "TOTAL VOICE";

	String TARGET_SUCCESS_CALL_PERCENTAGE = ">=90 %";
	String TARGET_DROPPED_CALL_PERCENTAGE = "<=5 %";

	String TARGET_SMS_DELIVERED_PERCENTAGE = ">=90 %";

	String PASS = "Pass";
	String FAIL = "Fail";

	String STATIC_TEST_OR_MOBILITY_TEST = "Static Test/Mobility Test";

	Integer INDEX_TEST_TYPE = 45;

    Integer NO_ACCESS_RADIUS=5;

    Integer SUMMARY_INDEX_OPERATOR = 29;

	Integer INDEX_MIMO = 16;

	Integer INDEX_RSSI = 4;

	Integer INDEX_WPT = 40;
	static Map<String, Integer> getKpiIndexMap() {
		Map<String, Integer> map = new HashMap<>();
		map.put(RSRP, INDEX_RSRP);  //5
		map.put(SINR, INDEX_SINR);  //7
		map.put(DL_THROUGHPUT, INDEX_DL); //21
		map.put(UL_THROUGHPUT, INDEX_UL); //22
		map.put(PCI_PLOT, INDEX_PCI);     //3
		map.put(CGI, INDEX_CELLID);   
		map.put(DL_EARFCN, INDEX_DL_EARFCN);  //8
		map.put(CQI, INDEXCQI);  //17
		map.put(MCS, INDEXMCS);  //36
		map.put(ROUTE, INDEXROUTE);  //1  
		map.put(MIMO, INDEXRI);  //16
		map.put(ReportConstants.FTP_DL_THROUGHPUT, INDEX_DL);  //21
		map.put(ReportConstants.HTTP_DL_THROUGHPUT, INDEX_DL); //21
		map.put(ReportConstants.FTP_UL_THROUGHPUT, INDEX_UL);  //22
		map.put(ReportConstants.HTTP_UL_THROUGHPUT, INDEX_UL);  //22
		map.put(ReportConstants.RSRQ, INDEX_RSRQ);  //6
		map.put(ReportConstants.RSSI, INDEX_RSSI);  //4
		map.put(ReportConstants.LATENCY, INDEX_LATENCY);  //23
		map.put(ReportConstants.JITTER, INDEX_JITTER);   //39
		map.put(ReportConstants.WEB_DOWNLOAD_DELAY, INDEX_WPT);  //40
		map.put(ReportConstants.CALL_PLOT, INDEX_PCI);
		map.put(ReportConstants.FTP_DL_RSRP, INDEX_RSRP);  //5
		map.put(ReportConstants.HTTP_DL_RSRP, INDEX_RSRP); //5
		map.put(ReportConstants.FTP_UL_RSRP, INDEX_RSRP);  //5
		map.put(ReportConstants.HTTP_UL_RSRP, INDEX_RSRP); //5
		map.put(ReportConstants.FTP_DL_SINR, INDEX_SINR);  //7
		map.put(ReportConstants.HTTP_DL_SINR, INDEX_SINR); //7
		map.put(ReportConstants.FTP_UL_SINR, INDEX_SINR);  //7
		map.put(ReportConstants.HTTP_UL_SINR, INDEX_SINR); //7
		map.put(ReportConstants.IDLE_PLOT, INDEX_RSRP); //5
		map.put(ReportConstants.MOS, INDEX_MOS);        //55
		map.put(ReportConstants.CA, INDEX_CA);          //56
		map.put(ReportConstants.SERVING_SYSTEM, SERVING_SYSTEM_INDEX);  //64
		map.put(ReportConstants.HANDOVER_PLOT, INDEX_PCI);
		map.put(ReportConstants.RESELECTION_PLOT, INDEX_PCI);
		map.put(ReportConstants.SERVING_CELL, SERVING_CELL_INDEX);
		map.put(ReportConstants.HEALTH_INDEX, HEALTH_IMAGE_INDEX);
		map.put(ReportConstants.TECHNOLOGY.toUpperCase(), TECHNOLOGY_INDEX); //34
		map.put(ReportConstants.BANDWIDTH_DL.toUpperCase(), BANDWIDTH_DL_INDEX); //10
		map.put(ReportConstants.WIFI_RSSI	, INDEX_WIFI_RSSI); //41
		map.put(ReportConstants.WIFI_SNR, INDEX_WIFI_SNR); //42
		map.put(IBWifiConstants.KEY_WIFI_DL_2_GHZ, INDEX_DL); //21
		map.put(IBWifiConstants.KEY_WIFI_DL_5_GHZ, INDEX_DL); //21
		map.put(IBWifiConstants.KEY_WIFI_UL_2_GHZ, INDEX_UL); //22
		map.put(IBWifiConstants.KEY_WIFI_UL_5_GHZ, INDEX_UL);
		map.put(ReportConstants.PDSCH_THROUGHPUT, INDEX_PDSCH_THROUGHPUT);
		map.put(ReportConstants.PUSCH_THROUGHPUT, INDEX_PUSCH_THROUGHPUT);
		return map;
	}

	static Map<String, Integer> getKpiIndexMapFORSsvT(Map<String, Integer> kpiIndexMap) {
		Map<String, Integer> map = new HashMap<>();
		map.put(RSRP, kpiIndexMap.get(RSRP));
		map.put(SINR, kpiIndexMap.get(SINR));
		map.put(RSRQ,  kpiIndexMap.get(RSRQ));
		map.put(DL_THROUGHPUT,  kpiIndexMap.get(DL_THROUGHPUT));
		map.put(UL_THROUGHPUT, kpiIndexMap.get(UL_THROUGHPUT));
		map.put(PCI_PLOT, kpiIndexMap.get(PCI_PLOT));
		map.put(DL_EARFCN,  kpiIndexMap.get(DL_EARFCN));
		map.put(CGI, kpiIndexMap.get(CGI));
		map.put(ReportConstants.FTP_DL_RSRP, kpiIndexMap.get(ReportConstants.FTP_DL_RSRP));  //5
		map.put(ReportConstants.HTTP_DL_RSRP, kpiIndexMap.get(ReportConstants.HTTP_DL_RSRP)); //5
		map.put(ReportConstants.FTP_UL_RSRP, kpiIndexMap.get(ReportConstants.FTP_UL_RSRP));  //5
		map.put(ReportConstants.HTTP_UL_RSRP, kpiIndexMap.get(ReportConstants.HTTP_UL_RSRP)); //5
		map.put(ReportConstants.FTP_DL_SINR, kpiIndexMap.get(ReportConstants.FTP_DL_SINR));  //7
		map.put(ReportConstants.HTTP_DL_SINR, kpiIndexMap.get(ReportConstants.HTTP_DL_SINR)); //7
		map.put(ReportConstants.FTP_UL_SINR, kpiIndexMap.get(ReportConstants.FTP_UL_SINR));  //7
		map.put(ReportConstants.HTTP_UL_SINR, kpiIndexMap.get(ReportConstants.HTTP_UL_SINR)); //7
		map.put(ReportConstants.TECHNOLOGY.toUpperCase(), kpiIndexMap.get(ReportConstants.TECHNOLOGY)); //34
		map.put(ReportConstants.WIFI_RSSI	, kpiIndexMap.get(ReportConstants.WIFI_RSSI)); //41
		map.put(ReportConstants.WIFI_SNR, kpiIndexMap.get(ReportConstants.WIFI_SNR)); //42
		map.put(ReportConstants.HANDOVER_PLOT, kpiIndexMap.get(ReportConstants.HANDOVER_PLOT));
		map.put(ReportConstants.CALL_PLOT, kpiIndexMap.get(ReportConstants.CALL_PLOT));
		map.put(ReportConstants.PDSCH_THROUGHPUT, kpiIndexMap.get(ReportConstants.PDSCH_THROUGHPUT));
		map.put(ReportConstants.PDSCH_THROUGHPUT, kpiIndexMap.get(ReportConstants.PDSCH_THROUGHPUT));

		return map;
	}


	static Map<String, Integer> getKpiIndexMapFORSsvT() {
		Map<String, Integer> map = new HashMap<>();
		map.put(RSRP, INDEX_RSRP);
		map.put(SINR, INDEX_SINR);
		map.put(RSRQ, INDEX_RSRQ);
		map.put(DL_THROUGHPUT, INDEX_DL);
		map.put(UL_THROUGHPUT, INDEX_UL);
		map.put(PCI_PLOT, INDEX_PCI);
		map.put(DL_EARFCN, INDEX_DL_EARFCN);
		map.put(CGI, INDEX_CELLID);
		map.put(ReportConstants.FTP_DL_RSRP, INDEX_RSRP);  //5
		map.put(ReportConstants.HTTP_DL_RSRP, INDEX_RSRP); //5
		map.put(ReportConstants.FTP_UL_RSRP, INDEX_RSRP);  //5
		map.put(ReportConstants.HTTP_UL_RSRP, INDEX_RSRP); //5
		map.put(ReportConstants.FTP_DL_SINR, INDEX_SINR);  //7
		map.put(ReportConstants.HTTP_DL_SINR, INDEX_SINR); //7
		map.put(ReportConstants.FTP_UL_SINR, INDEX_SINR);  //7
		map.put(ReportConstants.HTTP_UL_SINR, INDEX_SINR); //7
		map.put(ReportConstants.TECHNOLOGY.toUpperCase(), TECHNOLOGY_INDEX); //34
		map.put(ReportConstants.WIFI_RSSI	, INDEX_WIFI_RSSI); //41
		map.put(ReportConstants.WIFI_SNR, INDEX_WIFI_SNR); //42
		map.put(ReportConstants.HANDOVER_PLOT, INDEX_PCI);
		map.put(ReportConstants.CALL_PLOT, INDEX_PCI);
		map.put(ReportConstants.PDSCH_THROUGHPUT, INDEX_PDSCH_THROUGHPUT);
		return map;
	}


	public static Map<String, Integer> getKpiIndexMapForSSVIBCReport() {
		Map<String, Integer> indexMap = new HashMap<>();
		indexMap.put(RRC_CONNECTION_ATTEMPT, INDEX_RRC_CONNECTION_ATTEMPT);
		indexMap.put(RRC_CONNECTION_COMPLETE, INDEX_RRC_CONNECTION_COMPLETE);
		indexMap.put(CALL_INITIATE, INDEX_CALL_INITIATE);
		indexMap.put(CALL_SETUP_SUCCESS, INDEX_CALL_SETUP_SUCCESS);
		indexMap.put(LATENCY_BUFFER_SIZE_32_BYTES, INDEX_LATENCY_32);
		indexMap.put(RRC_REESTABLISHMENT_FAILED, INDEX_RRC_REESTABLISHMENT_FAILED);
		indexMap.put(ATTACH_ACCEPT, INDEX_ATTACH_ACCEPT);
		indexMap.put(PACKET_LOSS, INDEX_PACKET_LOSS);
		indexMap.put(DL_KEY, INDEX_DL);
		indexMap.put(UL_KEY, INDEX_UL);
		indexMap.put(PCI_KEY,INDEX_PCI);
				
		return indexMap;
	}
	
	static Map<String, Integer> getKpiIndexMapForStationary(){
		Map<String, Integer> map = new HashMap<>();
		map.put(RSRP, INDEX_RSRP);
		map.put(SINR, INDEX_SINR);
		map.put(DL_THROUGHPUT, INDEX_DL);
		map.put(UL_THROUGHPUT, INDEX_UL);
		return map;
	}


	static Map<String, Integer[]> getKpiIndexMapForStationarySummary() {
		Map<String, Integer[]> map = new HashMap<>();
		map.put(RSRP, new Integer[]{SUMMARY_RSRP_INDEX,SUMMARY_MIN_RSRP_INDEX,SUMMARY_MAX_RSRP_INDEX});
		map.put(SINR, new Integer[]{SUMMARY_SINR_INDEX,SUMMARY_MIN_SINR_INDEX,SUMMARY_MAX_SINR_INDEX});
		map.put(DL_THROUGHPUT, new Integer[]{SUMMARY_DL_INDEX,SUMMARY_MIN_DL_INDEX,SUMMARY_MAX_DL_INDEX});
		map.put(UL_THROUGHPUT, new Integer[]{SUMMARY_UL_INDEX,SUMMARY_MIN_UL_INDEX,SUMMARY_MAX_UL_INDEX});
		map.put(JITTER, new Integer[]{SUMMARY_JITTER_INDEX,SUMMARY_MIN_JITTER_INDEX,SUMMARY_MAX_JITTER_INDEX});
		map.put(LATENCY, new Integer[]{SUMMARY_LATENCY_INDEX,SUMMARY_MIN_LATENCY_INDEX,SUMMARY_MAX_LATENCY_INDEX});
		map.put(RESPONSE_TIME, new Integer[]{SUMMARY_RESPONSE_TIME_INDEX,SUMMARY_MIN_RESPONSE_TIME_INDEX,SUMMARY_MAX_RESPONSE_TIME_INDEX});

		return map;
	}

	/**
	 * Gets the earfcn color map.
	 *
	 * @return the earfcn color map
	 */
	static Map<String, Color> getEarfcnColorMap() {
		Map<String, Color> map = new HashMap<>();
		map.put("2477", Color.RED);
		map.put("39050", Color.BLUE);
		map.put("39194", Color.YELLOW);
		return map;
	}


	String CA = "CA";
	String RANK_INDICATOR = "RANK_INDICATOR";
	String MEAN_OPINION_SOURCE = "MEAN_OPINION_SOURCE";
	String CARRIER_AGGREGATION = "CARRIER_AGGREGATION";

	String TIME = "TIME";

	Integer FTP_DL_IMAGE_INDEX = 37;
	Integer FTP_UL_IMAGE_INDEX = 38;
	Integer HTTP_DL_IMAGE_INDEX = 41;
	Integer HTTP_UL_IMAGE_INDEX = 42;
	Integer CALL_PLOT_INDEX = 54;
	Integer HANDOVER_PLOT_INDEX = 74;
	Integer RESELECTION_PLOT_INDEX = 174;
	
	Integer SUMMARY_INDEX_MOS_G711=72;
	Integer SUMMARY_INDEX_MOS_ON_NET_G711=105;
	Integer SUMMARY_INDEX_MOS_OFF_NET_G711=106;

	String MEAN_OPINION_SCORE = "Mean Opinion Score(MOS) avg";

	String TARGET_MOS_VALUE = "3.5";
	Integer SUMMARY_CST_INDEX=71;
	
	
	
	

	/** The Constant SUMMARY_RSRQ_INDEX. */
	Integer SUMMARY_RSRQ_INDEX = 4;

	Integer SUMMARY_MIN_RSRQ_INDEX = 49;

	Integer SUMMARY_MAX_RSRQ_INDEX = 62;
	
	
	
	/** The Constant SUMMARY_RSSI_INDEX. */
	Integer SUMMARY_RSSI_INDEX = 2;

	Integer SUMMARY_MIN_RSSI_INDEX = 50;

	Integer SUMMARY_MAX_RSSI_INDEX = 63;

	
	Integer FTP_DL_RSRP_IMAGE_INDEX = 75;
	Integer FTP_UL_RSRP_IMAGE_INDEX = 76;
	Integer HTTP_DL_RSRP_IMAGE_INDEX = 77;
	Integer HTTP_UL_RSRP_IMAGE_INDEX = 78;
	Integer FTP_DL_SINR_IMAGE_INDEX = 79;
	Integer FTP_UL_SINR_IMAGE_INDEX = 80;
	Integer HTTP_DL_SINR_IMAGE_INDEX = 81;
	Integer HTTP_UL_SINR_IMAGE_INDEX = 82;
	Integer HEALTH_IMAGE_INDEX = 83;
	
	
	/** Hand Over summary Index. */
	Integer SUMMARY_HANDOVER_SUCCESS_INDEX=11;
	Integer SUMMARY_HANDOVER_FAIL_INDEX=10;
	Integer SUMMARY_HANDOVER_TOTAL_INDEX=17;
	Integer SUMMARY_HANDOVER_INITIATE_INDEX=9;
	Integer HBASE_SUMMARY_EARFCN_INDEX =44;
	Integer IDLE_PLOT_INDEX = 63;
	Integer SERVING_SYSTEM_INDEX = 64;
	Integer SERVING_CELL_INDEX = 65;
	

	Integer INDEX_MOS = 89;
	Integer INDEX_CA = 56;

	int BAND_INDEX = 33;

	int HBASE_SUMMARY_BAND_INDEX = 39;
	int HBASE_TRANSMISSION_MODE_INDEX = 33;
	
	int SUMMARY_TOTAL_DISTNACE_INDEX=116;

	String MOS = "MOS";

	Integer SUMMARY_MOS_INDEX = 123;
	Integer SUMMARY_MIN_MOS_INDEX = 124;
	Integer SUMMARY_MAX_MOS_INDEX = 125;
	
	Integer INDEX_DRIVE_DETAIL_PIN_NUMBER = 107;
	
	Double TEXT_IN_CIRCLE_X_DIFFERENCE = -9.0;
	Double TEXT_IN_CIRCLE_Y_DIFFERENCE = 11.0;
	
	Integer CIRCLE_RADIUS_IB_WIFI_SPEED_TEST_PIN = 20;
	Integer TEXT_SIZE_IB_WIFI_SPEED_TEST_PIN = 32;

	Integer SUMMARY_DL_HTTP_MIN = 41;

	Integer SUMMARY_DL_HTTP_MAX = 54;

	Integer SUMMARY_UL_HTTP_AVG = 68;

	Integer SUMMARY_UL_HTTP_MIN = 42;

	Integer SUMMARY_UL_HTTP_MAX = 55;
	

	Integer SUMMARY_DT_ATTEMP_COUNT = 147;

	Integer SUMMARY_AT_ATTEMP_COUNT=153;

	Integer SUMMARY_AT_SUCCESS_COUNT = 163;
	Integer SUMMARY_RESELECTION_SUCCESS_COUNT = 151;

	Integer SUMMARY_HO_INTERRUPTION_MIN_TIME = 148;
	Integer SUMMARY_HO_INTERRUPTION_MAX_TIME = 149;

	Integer SUMMARY_HO_INTERRUPTION_AVG_TIME = 150;

	Integer SUMMARY_MIN_LATENCY_32BYTE = 154;
	Integer SUMMARY_MAX_LATENCY_32BYTE = 155;
	Integer SUMMARY_AVG_LATENCY_32BYTE = 156;

	Integer SUMMARY_MIN_LATENCY_1000BYTE = 157;
	Integer SUMMARY_MAX_LATENCY_1000BYTE = 158;
	Integer SUMMARY_AVG_LATENCY_1000BYTE = 159;

	Integer SUMMARY_MIN_LATENCY_1500BYTE = 160;
	Integer SUMMARY_MAX_LATENCY_1500BYTE = 161;
	Integer SUMMARY_AVG_LATENCY_1500BYTE = 162;


	Integer SUMMARY_RTP_TOTAL_PACKET_COUNT = 204;
	Integer SUMMARY_RTP_PACKETS_LOST = 203;
	Integer SUMMARY_PING_BUFFER_SIZE=202;
	
	Integer INDEX_LATENCY_32 = 176;

	Integer INDEX_LATENCY_1000 = 177;

	Integer INDEX_LATENCY_1500 = 178;

	Integer INDEX_HO_INTERRUPTION_TIME = 131;

	Integer INDEX_RESPONSE_TIME = 40;

	Integer INDEX_VOLTE_JITTER = 185;

	Integer INDEX_VOLTE_PACKET_LOSS = 186;

	Integer INDEX_POLQA_MOS = 195;

	String LATITUDE = "LATITUDE";

	String LONGITUDE = "LONGITUDE";

	String PCI = "PCI";

	String NETWORK_TYPE = "networkType";

	String TECHNOLOGY = "TECHNOLOGY";
	String HANDOVER_INITIATE = "HANDOVER_INITIATE";
	String RESELECTION_SUCCESS = "RESELECTION_SUCCESS";

	// SSV IBC Report Constants (KPI && Index)
	public static final String RRC_CONNECTION_ATTEMPT = "RRC Connection Attempt";
	public static final String RRC_CONNECTION_COMPLETE = "RRC Connection Complete";
	public static final String CALL_INITIATE = "callIntiate";
	public static final String CALL_SETUP_SUCCESS = "Call Setup Success";
	public static final String LATENCY_BUFFER_SIZE_32_BYTES = "LatencyBufferSize32Bytes";
	public static final String RRC_REESTABLISHMENT_FAILED ="RRC Reestablishment Failed";
	public static final String RRC_CONNECTION_REESTABLISHMENT_REJECT = "RRC Connection Reestablishment Reject";
	public static final String ATTACH_ACCEPT = "AttachAccept";
	public static final String PACKET_LOSS = "packetLoss";
	public static final String DL_KEY = "DL";
	public static final String UL_KEY = "UL";
	public static final String PCI_KEY = "PCI";
	
	public static final Integer INDEX_RRC_CONNECTION_ATTEMPT = 116;
	public static final Integer INDEX_RRC_CONNECTION_COMPLETE = 117;
	public static final Integer INDEX_RRC_CONNECTION_SETUP_OK = 115;

	public static final Integer INDEX_CALL_INITIATE = 27;
	public static final Integer INDEX_RRC_REESTABLISHMENT_FAILED = 121;
	public static final Integer INDEX_ATTACH_ACCEPT = 159;
	public static final Integer INDEX_PACKET_LOSS = 54;
	public static final Integer INDEX_TOTAL_PACKET_COUNT=208;
	public static final Integer INDEX_RTP_PACKET_LOST=207;
	
	
}

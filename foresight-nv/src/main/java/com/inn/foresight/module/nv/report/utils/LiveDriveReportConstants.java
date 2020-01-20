package com.inn.foresight.module.nv.report.utils;

public interface LiveDriveReportConstants extends ReportConstants {

	String COMMA_SEPARATOR = ",";

	String LIVE_DRIVE_COLUMNS_FAMILY = "LIVE_DRIVE_COLUMNS_FAMILY";

	/** The Constant LIVE_DRIVE_TABLE. */
	String LIVE_DRIVE_TABLE = "LIVE_DRIVE_TABLE";

	String LIVEDRIVEREPORT = "WorkOrderReport";

	String DL_THROUGHPUT = "DL Throughput";

	String UL_THROUGHPUT = "UL Throughput";

	String MBPS = "(Mbps)";

	String MS_UNIT = "(ms)";

	String DBM_UNIT = "(dBm)";

	String DB_UNIT = "(dB)";

	String LATENCY = "Ping Latency";

	String JITTER = "Jitter";

	String MAIN_FILE_NAME = "nv_drive_report.jasper";

	String WEBDELAY = "Web page download delay(s)";

	String CDF_PDF = "CDF/PDF";

	String PASS_UPPER = "PASS";

	String FAIL_UPPER = "FAIL";

	String GRAPH_HEADING_RSRP = "Connected Mode RSRP Statistics";

	String GRAPH_HEADING_SINR = "SINR Statistics";

	String GRAPH_HEADING_WEBDL = "Web page download delay";

	String GRAPH_HEADING_DL = "Downlink Physical Layer Throughput Statistics";
	String GRAPH_HEADING_FTP_DL = "FTP Downlink Physical Layer Throughput Statistics";
	String GRAPH_HEADING_HTTP_DL = "HTTP Downlink Physical Layer Throughput Statistics";
	
	String GRAPH_HEADING_FTP_UL = "FTP Uplink Physical Layer Throughput Statistics";
	String GRAPH_HEADING_HTTP_UL = "HTTP Uplink Physical Layer Throughput Statistics";
	String GRAPH_HEADING_UL = "Uplink Physical Layer Throughput Statistics";

	String GRAPH_HEADING_JITTER = "Jitter";

	String GRAPH_HEADING_LATENCY = "Ping Latency";

	String CHART_TITLE_RSRP = "RSRP";

	String CHART_TITLE_SINR = "SINR";

	String CHART_TITLE_PCI = "Service Cell PCI";

	String CHART_TITLE_WEBDELAY = "Web Page Download Delay";

	String CHART_TITLE_LATENCY = "Latency";

	String CHART_TITLE_JITTER = "Jitter";

	String CHART_TITLE_DL = "DL Throughput";
	String CHART_TITLE_FTP_DL = "FTP DL Throughput";
	String CHART_TITLE_HTTP_DL = "HTTP DL Throughput";

	String CHART_TITLE_UL = "UL Throughput";
	String CHART_TITLE_FTP_UL = "FTP UL Throughput";
	String CHART_TITLE_HTTP_UL = "HTTP UL Throughput";

	String CHART_TYPE_FOR_RSRP = "Signal Strength";

	String CHART_TYPE_FOR_SINR = "Signal Quality";

	String CHART_TYPE_FOR_PCI = "Service Cell PCI";

	String CHART_TYPE_FOR_WEBDELAY = "Web Page Download";

	String CHART_TYPE_FOR_LATENCY = "Ping";

	String CHART_TYPE_FOR_JITTER = "Ping";

	String CHART_TYPE_FOR_DL = "Download";

	String CHART_TYPE_FOR_UL = "Upload";
	String CHART_TYPE_FOR_HTTP_UL = "HTTP Upload";
	String CHART_TYPE_FOR_FTP_UL = "FTP Upload";
	String CHART_TYPE_FOR_HTTP_DL = "HTTP Download";
	String CHART_TYPE_FOR_FTP_DL = "FTP Download";

	String COVERAGE_HEATH_INDEX = "chi";

	String DATA_HEATH_INDEX = "dhi";

	/** * SCORE DEFINITIONS. */
	String SCORE_GOOD = "Good";

	String SCORE_AVG = "Average";

	String SCORE_POOR = "Poor";

	String SCORE_EXCELLENT = "Excellent";

	String NETWORK_4G = "LTE";

	String NETWORK_3G = "WIFI";

	String NETWORK_2G = "2G";

	String NETWORK_CHECK_NOT_REQUIRED = null;

	String THROUGHTPUT = "_THROUGHTPUT";

	Double NINETY = 0.9d;

	Double TEN = 0.1d;

	String TIME = "TIME";

	String CELLID = "CELLID";


	String RSRP_GOOD_RANGE = "Avg > -95";
	String RSRP_AVG_RANGE = "Avg <= -95 && Avg >= -110";
	String RSRP_POOR_RANGE = "Avg < -110";
	
	String SINR_GOOD_RANGE = "Avg > 5";
	String SINR_AVG_RANGE = "Avg <= 5 && Avg >= -2";
	String SINR_POOR_RANGE = "Avg < -2";
	
	String DL_EXCELENT_RANGE = "Avg > 8";
	String DL_GOOD_RANGE = "Avg <= 8 && Avg > 2";
	String DL_AVG_RANGE = "Avg <= 2 && Avg >= 0.512";
	String DL_POOR_RANGE = "Avg < 0.512";	
	
	String UL_EXCELENT_RANGE = "Avg > 3";
	String UL_GOOD_RANGE = "Avg <= 3 && Avg > 0.768";
	String UL_AVG_RANGE = "Avg <= 0.768 && Avg >= 0.256";
	String UL_POOR_RANGE = "Avg < 0.256";

	

	public enum Kpi {
		pci, rssi, rsrp, rsrq, sinr, dlAvg, ulAvg, mcc, mnc, jitter, latency
	}
}

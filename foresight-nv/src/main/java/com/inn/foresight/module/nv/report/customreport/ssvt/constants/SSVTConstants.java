package com.inn.foresight.module.nv.report.customreport.ssvt.constants;

import java.util.HashMap;
import java.util.Map;

import com.inn.foresight.module.nv.report.utils.ReportConstants;

public class SSVTConstants {

	protected SSVTConstants() {
		// Empty Constructor to Prevent Instantiation
	}

	public static final Integer DRIVE_INDEX_HO_INTERRUPTION_TIME = 131;
	public static final Integer DRIVE_INDEX_CALL_INITIATED = 27;
	public static final Integer DRIVE_INDEX_CALL_DROPPED = 28;
	public static final Integer DRIVE_INDEX_CALL_FAILURE = 29;
	public static final Integer DRIVE_INDEX_CALL_SUCCESS = 30;
	public static final String TEST_STATUS_PASS = "PASS";
	public static final String TEST_STATUS_FAIL = "FAIL";
	public static final String TEST_STATUS_OBSERVED = "Observed";
	public static final String TEST_STATUS_NOT_OBSERVED = "Not Observed";
	public static final String PARAM_KEY_PCI_PLOT = "pciPlot";
	public static final String PARAM_KEY_PCI_LEGEND = "pciLegend";
	public static final String PARAM_KEY_JUSTIFICATION_IMAGE = "justificationImage";
	public static final String PARAM_KEY_SECTOR_1_IMAGE = "sector1Image";
	public static final String PARAM_KEY_SECTOR_2_IMAGE = "sector2Image";
	public static final String PARAM_KEY_SECTOR_3_IMAGE = "sector3Image";
	public static final String CALL_KPI_ATTEMPTS = "Call Attempts";
	public static final String CALL_KPI_FAILED_CALLS = "Blocked/Failed Call";
	public static final String CALL_KPI_DROPPED_CALLS = "Dropped Call";
	public static final String CALL_KPI_DROPPED_PERCENT = "Dropped Call (%)";
	public static final String CALL_KPI_SUCCESS_PERCENT = "Success Call (%)";
	public static final String CALL_KPI_CST = "Call Setup Time (ms)";
	public static final String CALL_KPI_MOS = "Mean Opinion Score(MOS)";
	protected static final String[] CALL_ANALYSIS_KPI_ARRAY = new String[] { CALL_KPI_CST, CALL_KPI_MOS };
	public static final String SHORT_CALL = "SHORT_CALL";
	public static final String DL_FTP_CALL = "FTP_DOWNLOAD";
	public static final String DL_HTTP_CALL = "HTTP_DOWNLOAD";
	public static final String UL_FTP_CALL = "FTP_UPLOAD";
	public static final String UL_HTTP_CALL = "HTTP_UPLOAD";
	public static final String LONG_CALL = "LONG_CALL";
	public static final String PING = "PING";
	public static final String ATTACH_DETACH = "ATTACH_DETACH";
	public static final String ATTACH = "ATTACH";
	public static final String DETACH = "DETACH";
	public static final String FDD3 = "FDD3";
	public static final String FDD5 = "FDD5";
	public static final String TDD40 = "TDD40";
	public static final Integer DRIVE_INDEX_HO_INITIATE = 24;
	public static final Integer DRIVE_INDEX_HO_SUCCESS = 26;

	public static final String PARAM_KEY_NV_LOGO = "netvelocityLogo";
	public static final String LAYER3_REPORT_COLUMN_SSVT = "ssvt";
	public static final String LAYER3_REPORT_COLUMN_HANDOVER = "handoverData";

	public static final String LAYER3_EMPTY_DATA_STRING = "[]";

	public static final Integer INDEX_HO_DATA_TIMESTAMP = 0;
	public static final Integer INDEX_HO_DATA_LATITUDE = 1;
	public static final Integer INDEX_HO_DATA_LONGITUDE = 2;
	public static final Integer INDEX_HO_DATA_INIT_RSRP = 3;
	public static final Integer INDEX_HO_DATA_COMPLETE_RSRP = 4;
	public static final Integer INDEX_HO_DATA_SOURCE_PCI = 5;
	public static final Integer INDEX_HO_DATA_TARGET_PCI = 6;
	public static final Integer INDEX_HO_DATA_INTERRUPTION_TIME = 7;
	public static final Integer INDEX_HO_DATA_STATUS = 8;

	public static final String DRIVE_DATA_TYPE_STATIONARY = "Stationary";
	public static final String DRIVE_DATA_TYPE_DRIVE = "Drive";
	public static final String DRIVE_DATA_TYPE_COMBINED = "Combined";

	public static final String IS_SITE_ACCEPTANCE = "isSiteAcceptance";

	public static final Integer SUMMARY_BAND_INDEX = 32;

	public static final String META_KEY_BAND = "band";
	public static final String META_KEY_PROJECT_ID = "projectId";

	public static final String KEY_REPORT_ID = "reportId";
	public static final String KEY_TASK_NAME = "taskName";
	public static final String KEY_SITE_NAME = "siteName";

	public static final String KEY_RECIPE_SCRIPT_NAME = "name";
	public static final String RECIPE_SCRIPT_NAME_DOWNLOAD = "DOWNLOAD";
	public static final String RECIPE_SCRIPT_NAME_UPLOAD = "UPLOAD";
	public static final String RECIPE_SCRIPT_NAME_SHORT_CALL = "SHORT_CALL";
	public static final String RECIPE_SCRIPT_NAME_LONG_CALL = "LONG_CALL";
	public static final String RECIPE_SCRIPT_NAME_IDLE = "IDLE";

	public static final String KEY_RECIPE_MOBILITY_PREFIX = "MOBILITY_";

	public static final Integer RECIPE_SCRIPT_VALID_SIZE = 1;

	public static final String MOBILITY_RSRP = "MOBILITY_RSRP";

	public static final String MOBILITY_SINR = "MOBILITY_SINR";

	public static final String SITE_BANDWIDTH_20  = "20";

	public static final String SITE_BANDWIDTH_5 = "5";

	public static final String PACKET_LOSS = "RTP_PACKET_LOSS";

	public static final String SF_INTEGRATION_TASK_ATP_1E = "ATP 1E";
	public static final String SF_INTEGRATION_TASK_ATP_1C = "ATP 1C";

	public static final String HEADER_X_API_KEY = "X-API-KEY";

	public static final String NV_SSVT_SF_INTEGRATION_URL = "NV_SSVT_SF_INTEGRATION_URL";

	public static final String NV_SSVT_SF_INTEGRATION_X_API_KEY = "NV_SSVT_SF_INTEGRATION_X_API_KEY";

	public static final Integer PEAK_DL_THRESHOLD_20_MHZ = 80;
	public static final Integer PEAK_DL_THRESHOLD_5_MHZ = 20;
	public static final Integer PEAK_UL_THRESHOLD_20_MHZ = 20;
	public static final Integer PEAK_UL_THRESHOLD_5_MHZ = 5;

	public static final String KEY_RECIPE_OTHERS = "OTHERS";


	public static final Map<String, Integer> getKpiIndexMapForMobility(boolean isSiteAceptance,
			Map<String, Integer> kpiIndexMap) {
		Map<String, Integer> map = new HashMap();
		map.put(ReportConstants.PCI_PLOT, kpiIndexMap.get(ReportConstants.PCI_PLOT));
		if (!isSiteAceptance) {
			map.put(ReportConstants.HANDOVER_PLOT, kpiIndexMap.get(ReportConstants.HANDOVER_PLOT));
			map.put(ReportConstants.CALL_PLOT, kpiIndexMap.get(ReportConstants.CALL_PLOT));
		}
		return map;
	}

	public static final Map<String, Integer> getKpiIndexMapForDownload(Map<String, Integer> kpiIndexMap) {
		Map<String, Integer> map = new HashMap();
			map.put("RSRP", kpiIndexMap.get(ReportConstants.RSRP));
			map.put("SINR", kpiIndexMap.get(ReportConstants.SINR));
			map.put("RSRQ", kpiIndexMap.get(ReportConstants.RSRQ));
			map.put("MAC_DL_THROUGHPUT", kpiIndexMap.get(ReportConstants.MAC_DL_THROUGHPUT));
		return map;
	}

	public static final Map<String, Integer> getKpiIndexMapForUpload(Map<String, Integer> kpiIndexMap) {
		Map<String, Integer> map = new HashMap();
		map.put("MAC_UL_THROUGHPUT", kpiIndexMap.get(ReportConstants.MAC_UL_THROUGHPUT));
		return map;
	}

}

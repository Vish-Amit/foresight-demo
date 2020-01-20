package com.inn.foresight.module.nv.report.utils;

import com.google.gson.Gson;
import com.inn.commons.hadoop.hbase.HBaseResult;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.report.wrapper.APReportWrapper;
import org.apache.hadoop.hbase.CompareOperator;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActiveReportUtil {
	
	/** The logger. */
	private static Logger logger = LogManager.getLogger(ActiveReportUtil.class);
	
	private ActiveReportUtil() {
		//Sonar Fixing
	}
	
	public static Scan getFilterList(JSONObject jsonObj,Scan scan) {
		try {
			Filter columnValueFilter = new SingleColumnValueFilter(Bytes
			        .toBytes(ReportConstants.COLUMN_FAMILY_R), Bytes.toBytes(ActiveReportUtil.getGeographyColumnNameInHbase(jsonObj.get(ReportConstants.GEOGRAPHY_TYPE).toString())), 
			        CompareOperator.EQUAL, Bytes.toBytes(jsonObj.get(ReportConstants.GEOGRAPHY_NAME).toString()));
			scan.setFilter(columnValueFilter);
			scan.setTimeRange(Long.parseLong(jsonObj.get(ReportConstants.START_TIMESTAMP).toString()), Long.parseLong(jsonObj.get(ReportConstants.END_TIMESTAMP).toString()));
			scan.addColumn(Bytes.toBytes(ReportConstants.COLUMN_FAMILY_R),Bytes.toBytes(ActiveReportUtil.getGeographyColumnNameInHbase(jsonObj.get(ReportConstants.GEOGRAPHY_TYPE).toString())));
		} catch (NumberFormatException e) {
			logger.error("NumberFormatException occured inside method {} ",Utils.getStackTrace(e));
		} catch (Exception e) {
			logger.error("Unable to get the scan object {} ",Utils.getStackTrace(e));
		}
		return scan;
		
	}
	
	public static String getGeographyColumnNameInHbase(String geographyType) {
		if(geographyType.equalsIgnoreCase(ReportConstants.GEOGRAPHYL1)){
			return ReportConstants.GEOGRAPHY_COLUMN_GL1;
		}else if(geographyType.equalsIgnoreCase(ReportConstants.GEOGRAPHYL2)){
			return ReportConstants.GEOGRAPHY_COLUMN_GL2;
		}else if(geographyType.equalsIgnoreCase(ReportConstants.GEOGRAPHYL3)){
			return ReportConstants.GEOGRAPHY_COLUMN_GL3;
		}else if(geographyType.equalsIgnoreCase(ReportConstants.GEOGRAPHYL4)){
			return ReportConstants.GEOGRAPHY_COLUMN_GL4;
		}
		return null;
	}

	public static List<APReportWrapper> convertHbaseResultListToWrapper(List<HBaseResult> hbaseResultList) {
		List<APReportWrapper> reportWrapperList = new ArrayList<>();
		if(hbaseResultList!=null && hbaseResultList.size()>ReportConstants.INDEX_ZER0){
			for (HBaseResult hbaseresult : hbaseResultList) {
				APReportWrapper wrapper = new APReportWrapper();
				wrapper.setRecordType(hbaseresult.getString(APReportHbaseConstants.RECORD_TYPE.getBytes()));
				wrapper.setTestSubtype(hbaseresult.getString(APReportHbaseConstants.ACTIVE_TEST_TYPE.getBytes()));
				wrapper.setDeviceOs(hbaseresult.getString(APReportHbaseConstants.DEVICE_OS.getBytes()));
				wrapper.setNvModule(hbaseresult.getString(APReportHbaseConstants.NV_MODULE.getBytes()));
				wrapper.setGeographyl1(hbaseresult.getString(APReportHbaseConstants.GEOGRAPHY_L1.getBytes()));
				wrapper.setGeographyl2(hbaseresult.getString(APReportHbaseConstants.GEOGRAPHY_L2.getBytes()));
				wrapper.setGeographyl3(hbaseresult.getString(APReportHbaseConstants.GEOGRAPHY_L3.getBytes()));
				wrapper.setGeographyl4(hbaseresult.getString(APReportHbaseConstants.GEOGRAPHY_L4.getBytes()));
				//logger.info("RSRP 123 {} ",hbaseresult.getStringValue(APReportHbaseConstants.MIN_RSRP.getBytes()));
				wrapper.setMinRsrp(ReportUtil.getDoubleValue(hbaseresult.getString(APReportHbaseConstants.MIN_RSRP.getBytes())));
				wrapper.setMinSinr(ReportUtil.getDoubleValue(hbaseresult.getString(APReportHbaseConstants.MIN_SINR.getBytes())));
				wrapper.setMinRsrq(ReportUtil.getDoubleValue(hbaseresult.getString(APReportHbaseConstants.MIN_RSRQ.getBytes())));
				wrapper.setMinRssi(ReportUtil.getDoubleValue(hbaseresult.getString(APReportHbaseConstants.MIN_RSSI.getBytes())));
				wrapper.setMinUlRate(ReportUtil.getDoubleValue(hbaseresult.getString(APReportHbaseConstants.MIN_UL.getBytes())));
				wrapper.setMinDlRate(ReportUtil.getDoubleValue(hbaseresult.getString(APReportHbaseConstants.MIN_DL.getBytes())));
				// wrapper.setMinWebDlDelay(ReportUtil.getDoubleValue(hbaseresult.getStringValue(APReportHbaseConstants.MIN_.getBytes())));
				wrapper.setMinJitter(ReportUtil.getDoubleValue(hbaseresult.getString(APReportHbaseConstants.MIN_JITTER.getBytes())));
				wrapper.setMinLatency(ReportUtil.getDoubleValue(hbaseresult.getString(APReportHbaseConstants.MIN_LATENCY.getBytes())));

				wrapper.setMaxRsrp(ReportUtil.getDoubleValue(hbaseresult.getString(APReportHbaseConstants.MAX_RSRP.getBytes())));
				wrapper.setMaxSinr(ReportUtil.getDoubleValue(hbaseresult.getString(APReportHbaseConstants.MAX_SINR.getBytes())));
				wrapper.setMaxRsrq(ReportUtil.getDoubleValue(hbaseresult.getString(APReportHbaseConstants.MAX_RSRQ.getBytes())));
				wrapper.setMaxRssi(ReportUtil.getDoubleValue(hbaseresult.getString(APReportHbaseConstants.MAX_RSSI.getBytes())));
				wrapper.setMaxUlRate(ReportUtil.getDoubleValue(hbaseresult.getString(APReportHbaseConstants.MAX_UL.getBytes())));
				wrapper.setMaxDlRate(ReportUtil.getDoubleValue(hbaseresult.getString(APReportHbaseConstants.MAX_DL.getBytes())));
				// wrapper.setMaxWebDlDelay(ReportUtil.getDoubleValue(hbaseresult.getStringValue(APReportHbaseConstants.MAX_.getBytes())));
				wrapper.setMaxJitter(ReportUtil.getDoubleValue(hbaseresult.getString(APReportHbaseConstants.MAX_JITTER.getBytes())));
				wrapper.setMaxLatency(ReportUtil.getDoubleValue(hbaseresult.getString(APReportHbaseConstants.MAX_LATENCY.getBytes())));

				wrapper.setAvgRsrp(ReportUtil.getDoubleValue(hbaseresult.getString(APReportHbaseConstants.AVG_RSRP.getBytes())));
				wrapper.setAvgSinr(ReportUtil.getDoubleValue(hbaseresult.getString(APReportHbaseConstants.AVG_SINR.getBytes())));
				wrapper.setAvgRsrq(ReportUtil.getDoubleValue(hbaseresult.getString(APReportHbaseConstants.AVG_RSRQ.getBytes())));
				wrapper.setAvgRssi(ReportUtil.getDoubleValue(hbaseresult.getString(APReportHbaseConstants.AVG_RSSI.getBytes())));
				wrapper.setAvgUlRate(ReportUtil.getDoubleValue(hbaseresult.getString(APReportHbaseConstants.AVG_UL.getBytes())));
				wrapper.setAvgDlRate(ReportUtil.getDoubleValue(hbaseresult.getString(APReportHbaseConstants.AVG_DL.getBytes())));
				// wrapper.setAvgWebDlDelay(ReportUtil.getDoubleValue(hbaseresult.getStringValue(APReportHbaseConstants.AVG_.getBytes())));
				wrapper.setAvgJitter(ReportUtil.getDoubleValue(hbaseresult.getString(APReportHbaseConstants.AVG_JITTER.getBytes())));
				wrapper.setAvgLatency(ReportUtil.getDoubleValue(hbaseresult.getString(APReportHbaseConstants.AVG_LATENCY.getBytes())));

				reportWrapperList.add(wrapper);
			}
		}
		logger.info("reportWrapperList Data {} ",new Gson().toJson(reportWrapperList));
		return reportWrapperList;
	}
	
	
}

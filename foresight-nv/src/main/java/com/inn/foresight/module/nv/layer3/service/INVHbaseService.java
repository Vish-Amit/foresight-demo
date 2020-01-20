package com.inn.foresight.module.nv.layer3.service;

import java.util.List;
import java.util.Map;

import com.inn.commons.hadoop.hbase.HBaseResult;
import com.inn.foresight.module.nv.report.wrapper.RawDataWrapper;
import com.inn.foresight.module.nv.report.wrapper.stealth.StealthWOKpiAnalysisWrapper;
import com.inn.foresight.module.nv.report.wrapper.stealth.StealthWOPingAnalysisWrapper;
import com.inn.foresight.module.nv.report.wrapper.stealth.StealthWOWPTAnalysisWrapper;
import com.inn.foresight.module.nv.report.wrapper.stealth.StealthWOYoutubeAnalysisWrapper;

public interface INVHbaseService {

	String getLiveDriveReportData(Map<String, Map<String, List<String>>> valueMap);

	List<RawDataWrapper> getHbaseDataForTimeStamp(Long startTime, Long endTime, String imei);

	List<RawDataWrapper> getAggregatedHbaseDataForRowKey(Map<String, Map<String, List<String>>> locationTestTypeKeyMap);

	RawDataWrapper getSautDataWrapper(List<HBaseResult> hbaseResultList, String locationName);

	String getStealthDataByWoIdAndTaskId(String woId, String taskId, String date,String columnName, Integer hour);

	String getStealthDataByWoId(String woId, String taskId, String date, String columnName);

	List<StealthWOWPTAnalysisWrapper> getWptAnalysisDataList(List<HBaseResult> hbaseResultList,String woId, List<Integer> taskIdList, String date, Long startDate, Long endDate, String type);

	List<StealthWOYoutubeAnalysisWrapper> getYoutubeAnalysisData(List<HBaseResult> hbaseResultList ,Integer workorderId, List<Integer> taskIdList, Long startDate, Long endDate, String type);

	Map<String, List<String[]>> getGeographyWiseData(List<HBaseResult> hbaseResultList, String woId, List<Integer> taskIdList, String date, String columnName, Long startDate, Long endDate);

	List<HBaseResult> getHBaseResultListForStealthReport(String woId, List<Integer> taskIdList, String date, String columnName);
	
	List<StealthWOPingAnalysisWrapper> getPingAnalysisDataList(List<HBaseResult> hbaseResultList ,String woId, List<Integer> taskIdList, String date, Long startDate, Long endDate, String type);
	
	List<StealthWOKpiAnalysisWrapper> getKpiAnalysisDataList(List<HBaseResult> hbaseResultList ,String woId, List<Integer> taskIdList, String date, Long startDate, Long endDate, String type);
	
	List<HBaseResult> getHbaseResultListforStealth(String woId, List<Integer> taskIdList, String date,Long startDate, Long endDate);

	Boolean deleteDataUsingRowPrefix(String rowPrefix, String tableName);

	void deletePreviousDataForLayer3(Integer workorderId, Integer recipeId);

}

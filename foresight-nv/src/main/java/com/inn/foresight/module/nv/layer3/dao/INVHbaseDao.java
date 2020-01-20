package com.inn.foresight.module.nv.layer3.dao;

import java.util.List;

import org.apache.hadoop.hbase.client.Get;

import com.inn.commons.hadoop.hbase.HBaseResult;

public interface INVHbaseDao {

	List<HBaseResult> getHbaseDataForTimeStamp(String deviceId, Long startTime, Long endTime, String tableName);

	List<HBaseResult> getReportDataByRowKeys(List<String> list, String configProp);

	List<HBaseResult> getSautReportData(List<Get> getList,String tableName);

	List<HBaseResult> getStealthDataFromHbase(String rowKey, String configProp);
	
	List<HBaseResult> getStealthDataFromHbase(List<String> rowKeyList, String tableName);

	Boolean deleteDataUsingRowPrefix(String rowPreFix, String tableName);

}

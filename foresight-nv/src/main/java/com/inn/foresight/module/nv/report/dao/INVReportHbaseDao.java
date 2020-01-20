package com.inn.foresight.module.nv.report.dao;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Scan;

import com.inn.commons.hadoop.hbase.HBaseResult;
import com.inn.foresight.module.nv.report.workorder.wrapper.DriveDataWrapper;

public interface INVReportHbaseDao {
	DriveDataWrapper getDataFromHbase(String id);

    List<DriveDataWrapper> getNoAcessDataFromLayer3Report(Integer workorderId, String recipeId);

    String getKpiStatsFromHbase(String workOrderId, String kpiName);

	String getSummaryDataFromHbaseforWorkOrderId(String workOrderId);

	void insertFileToHbase(Put put, String nvreportTable);

	File getPDFReportByAnalyticsRepositoryId(Integer reportIntanceId);

	boolean saveFileToHbase(File file, Integer reportInstanceId);

	HBaseResult getResultForDriveResultByTaskId(Integer taskId);

	List<DriveDataWrapper> getImageDataFromHbase(String id, Integer size);

	List<DriveDataWrapper> getSpeedTestDatafromHbase(Integer workorderId, List<String> list, List<String> list2);

	List<DriveDataWrapper> getNoAcessDataFromLayer3Report(Integer workorderId, String operator, String recipeId)
			;

	List<HBaseResult> getActiveReportDataFromHbase(Scan scan, String tableName, String columnFamily);

	boolean delete(String tableName, String rowKey) throws IOException;
	boolean deleteFromRowPreFilter(String tableName,String rowPreFix);
	
	void setDataInDriveDataWrapperList(List<DriveDataWrapper> speedtestDataList, Get get, String tableName)
			throws IOException;
}

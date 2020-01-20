package com.inn.foresight.module.nv.workorder.stealth.dao;

import java.util.List;

import org.apache.hadoop.hbase.client.Scan;

import com.inn.commons.hadoop.hbase.HBaseResult;

public interface IStealthTaskHbaseDao {
	List<HBaseResult> getStealthKPIByWorkorderId(Scan scan);

	List<HBaseResult> getResultForRequest(List<String> columnList, String tableName, String startRow, String endRow,
			Long startTime, Long endTime);

	List<HBaseResult> getResultListForPreFix(List<String> columnList, String tableName, String rowPrefix);


	List<HBaseResult> getResultListForPreFixList(List<String> columnList, String tableName, List<String> rowPrefix);
}

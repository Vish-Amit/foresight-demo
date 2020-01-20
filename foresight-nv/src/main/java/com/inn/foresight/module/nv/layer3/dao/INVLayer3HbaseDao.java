package com.inn.foresight.module.nv.layer3.dao;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Scan;

import com.inn.commons.hadoop.hbase.HBaseResult;

/**
 * @author innoeye
 * date - 09-Jan-2018 6:04:03 PM
 */
public interface INVLayer3HbaseDao {




	List<HBaseResult> getSignalKPIStats(List<Get> getList) throws IOException;

	HBaseResult getQMDLDataFromHBase(Get get, String tableName) throws IOException;

	List<HBaseResult> scanQMDLDataFromHBaseWithPagination(String tableName, String workoderId, String direction, Long timeStamp)
			throws IOException;

	HBaseResult getSignalMessageDetail(String tableName, String msgType, String rowKey) throws IOException;

	HBaseResult getKpiStatsData(String tableName, String rangeStats, String workorderId, String kpi) throws IOException;

	String insertQMDLDataIntoHbase(List<Put> putList, String tableName) throws IOException;

	List<HBaseResult> getQMDLDataFromHBase(List<Get> getList, String table) throws IOException;

	List<HBaseResult> scanQMDLDataFromHBaseWithPagination(String tableName, String prefix, String direction,
			String lastRowKey) throws IOException;

	List<HBaseResult> scanQMDLDataFromHBaseForMSGCsv(String tableName, Set<String> prefixList) throws IOException;
	
	List<HBaseResult> scanQMDLDataFromHbase(String tableName, Scan scan) throws IOException;

	
	List<HBaseResult> scanNeighbourDataFromHBase(String tableName, String prefix) throws IOException;

	List<HBaseResult> scanNeighbourDataFromHBaseForFramework(String tableName, String prefix) throws IOException;

	List<HBaseResult> scanQMDLDataFromHBaseWithPaginationForFramework(String tableName, String prefix, String direction,
			String lastRowKey) throws IOException;
	
}

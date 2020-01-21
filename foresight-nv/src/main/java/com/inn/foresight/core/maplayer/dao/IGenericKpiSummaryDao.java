package com.inn.foresight.core.maplayer.dao;

import java.util.List;

import org.apache.hadoop.hbase.client.Get;

import com.inn.commons.hadoop.hbase.HBaseResult;
import com.inn.foresight.core.maplayer.wrapper.TileImageWrapper;


public interface IGenericKpiSummaryDao {

	public List<TileImageWrapper> getHBaseDataFromGetList(List<Get> getList, String tableName, String column,String columnFamily);
	public List<HBaseResult> getHbaseResultForPrefixList(String tableName,List<String>tileIdPrefixList,List<String> column,String columnFamily);
	
}

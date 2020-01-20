package com.inn.foresight.module.nv.layer.dao;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.hbase.client.Scan;

import com.inn.commons.hadoop.hbase.HBaseResult;

public interface LayerPlotDao {

	public List<HBaseResult> getDataFromHbase(Scan scan, String tableName, String columnFamily) throws IOException;
	
}

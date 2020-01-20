package com.inn.foresight.module.nv.customgeography.dao;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.hbase.client.Put;

import com.inn.commons.hadoop.hbase.HBaseResult;

public interface ICustomGeographyHbaseDao {

	HBaseResult getCustomBoundaryFromHbase(String tableName, String columnFamily, String rowKey)
			throws IOException;

	void insertCustomBoundary(String tableName, List<Put> putList) throws IOException;

}

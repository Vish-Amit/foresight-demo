package com.inn.foresight.module.nv.customercare.dao;

import java.util.List;

import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Scan;

import com.inn.commons.hadoop.hbase.HBaseResult;

/**
 * The Interface IActiveHbaseDao.
 */
public interface IActiveHbaseDao {
	
	/**
	 * Gets the active data.
	 *
	 * @param scanList the scan list
	 * @return the active data
	 */
	List<HBaseResult> getActiveData(List<Scan> scanList);

	/**
	 * Gets the active data by row key.
	 *
	 * @param gets the gets
	 * @return the active data by row key
	 */
	List<HBaseResult> getActiveDataByRowKey(List<Get> gets);
}

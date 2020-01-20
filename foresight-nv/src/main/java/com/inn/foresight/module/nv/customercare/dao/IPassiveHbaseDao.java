package com.inn.foresight.module.nv.customercare.dao;

import java.util.List;

import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Scan;

import com.inn.commons.hadoop.hbase.HBaseResult;

/**
 * The Interface IPassiveHbaseDao.
 */
public interface IPassiveHbaseDao {
	
	/**
	 * Gets the passive data.
	 *
	 * @param scanList the scan list
	 * @return the passive data
	 */
	List<HBaseResult> getPassiveData(List<Scan> scanList);

	/**
	 * Gets the passive data by row key.
	 *
	 * @param rowKeyList the row key list
	 * @return the passive data by row key
	 */
	List<HBaseResult> getPassiveDataByRowKey(List<Get> rowKeyList);

}

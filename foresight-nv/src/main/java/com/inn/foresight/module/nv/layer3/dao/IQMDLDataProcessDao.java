package com.inn.foresight.module.nv.layer3.dao;

import java.util.List;

import org.apache.hadoop.hbase.client.Put;

/** The Interface IQMDLDataProcessDao. */
public interface IQMDLDataProcessDao {

	/**
	 * Insert QMDL data into hbase.
	 *
	 * @param putList the put list
	 * @param string the string
	 * @return the string
	 */
	String insertQMDLDataIntoHbase(List<Put> putList, String string);

}

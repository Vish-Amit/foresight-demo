package com.inn.foresight.module.nv.wpt.analytics.dao;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.filter.Filter;

import com.inn.foresight.module.nv.wpt.analytics.utils.wrapper.WPTRawDataWrapper;


/** The Interface IWPTHbaseDao. */
public interface IWPTHbaseDao {



	/**
	 * Scan data with time range.
	 *
	 * @param minStamp the min stamp
	 * @param maxStamp the max stamp
	 * @return the list
	 * @throws Exception the exception
	 */
	List<WPTRawDataWrapper> scanDataWithTimeRange(Long minStamp, Long maxStamp, Filter filter) throws Exception;


	/**
	 * Insert WPT result list.
	 *
	 * @param putList the put list
	 * @param tableNameWpt the table name wpt
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	void insertWPTResultList(List<Put> putList, String tableNameWpt) throws IOException;

}

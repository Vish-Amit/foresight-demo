package com.inn.foresight.module.nv.customgeography.dao.impl;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.inn.commons.hadoop.hbase.HBaseResult;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.core.generic.dao.impl.hbase.AbstractHBaseDao;
import com.inn.foresight.module.nv.customgeography.dao.ICustomGeographyHbaseDao;

@Service("CustomGeographyHbaseDaoImpl")
public class CustomGeographyHbaseDaoImpl extends AbstractHBaseDao implements ICustomGeographyHbaseDao {

	/** The logger. */
	private static Logger logger = LogManager.getLogger(CustomGeographyHbaseDaoImpl.class);

	/**
	 * Inserts Data In hbase.
	 * @param HBase tableName
	 * @param putList the put list
	 * @return ArrayList of Put Object
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Override
	public void insertCustomBoundary(String tableName, List<Put> putList) throws IOException {
		logger.info("Going to insert Custom Boundary into table {}", tableName);
		insert(tableName, putList);
	}

	/**
	 * Gets CustomBoundaryWrapper for Specified Table name, ColumnFamily and Rowkey.
	 * @param tableName the table name
	 * @param columnFamily the column family
	 * @param rowKey the row key
	 * @return CustomBoundaryWrapper Containing all CustomBoundary Data of Specified rowkey, ColumnFamily and tableName
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws DaoException
	 */
	@Override
	public HBaseResult getCustomBoundaryFromHbase(String tableName, String columnFamily, String rowKey)
			throws IOException {
		logger.info("Going to get Custom Boundary for tableName {}, columnFamily {} , rowKey {}", tableName,
				columnFamily, rowKey);
		try {
			Get get = new Get(Bytes.toBytes(rowKey));
			return getResultByPool(get, tableName, Bytes.toBytes(columnFamily));
		} catch (IOException e) {
			logger.error("Getting Exception in getCustomBoundaryFromHbase", ExceptionUtils.getStackTrace(e));
			throw e;
		}
	}

}

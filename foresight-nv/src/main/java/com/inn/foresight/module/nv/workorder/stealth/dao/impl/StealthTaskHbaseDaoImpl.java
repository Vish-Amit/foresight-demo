package com.inn.foresight.module.nv.workorder.stealth.dao.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.commons.Symbol;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.hadoop.hbase.HBaseResult;
import com.inn.foresight.core.generic.dao.impl.hbase.AbstractHBaseDao;
import com.inn.foresight.core.generic.utils.ConfigUtil;
import com.inn.foresight.core.maplayer.dao.IGenericMapDao;
import com.inn.foresight.module.nv.workorder.stealth.constants.StealthConstants;
import com.inn.foresight.module.nv.workorder.stealth.dao.IStealthTaskHbaseDao;

@Service("StealthTaskHbaseDaoImpl")
public class StealthTaskHbaseDaoImpl extends AbstractHBaseDao implements IStealthTaskHbaseDao {
	/** The logger. */
	private static final Logger logger = LogManager.getLogger(StealthTaskHbaseDaoImpl.class);
	
	@Autowired
	IGenericMapDao iGenericMapDao;

	@Override
	public List<HBaseResult> getStealthKPIByWorkorderId(Scan scan) {
		List<HBaseResult> resultList = null;
		try {
			resultList = scanResultByPool(scan, ConfigUtils.getString(ConfigUtil.STEALTH_DATA_TABLE),
					Bytes.toBytes(StealthConstants.COLUMN_FAMILY));
		} catch (IOException e) {
			logger.error("IOException in getStealthKPIByWorkorderId: {}", ExceptionUtils.getStackTrace(e));
		}
		return resultList;
	}
	@Override
	public List<HBaseResult> getResultForRequest(List<String> columnList,String tableName,String startRow,String endRow,Long startTime,Long endTime) {
		try {
			logger.info("getResultForRequest columnList:  {} tableName: {} startRow: {} endTime:{}",columnList,tableName,startRow,endTime);
			return iGenericMapDao.getHbaseDataByRowOrTimeRange(columnList, startRow, endRow, tableName, startTime, endTime);
		} catch (Exception e) {
			logger.error("Exception in getResultForRequest: {}", ExceptionUtils.getStackTrace(e));
		}
		return Collections.emptyList();
	}
	
	@Override
	public List<HBaseResult> getResultListForPreFix(List<String> columnList,String tableName,String rowPrefix) {
		logger.info("getResultForRequest columnList:  {} tableName: {} rowPrefix: {}", columnList, tableName,
				rowPrefix);
		try {
			Scan scan = new Scan();
			String columnFamily = null;
			scan.setRowPrefixFilter(Bytes.toBytes(rowPrefix));
			logger.info("prefix  is {}", scan.getFilter());
			for (String column : columnList) {
				scan.addColumn(column.split(Symbol.COLON_STRING)[0].getBytes(),
						column.split(Symbol.COLON_STRING)[1].getBytes());
				columnFamily = column.split(Symbol.COLON_STRING)[0];
			}
		
			return iGenericMapDao.getResultListForScan(scan, tableName, columnFamily);
		} catch (Exception e) {
			logger.error("Exception in getResultForRequest: {}", ExceptionUtils.getStackTrace(e));
		}
		return Collections.emptyList();
	}
	
	
	@Override
	public List<HBaseResult> getResultListForPreFixList(List<String> columnList,String tableName,List<String> rowPrefix) {
		logger.info("getResultForRequest columnList:  {} tableName: {} rowPrefix: {}", columnList, tableName,
				rowPrefix);
		try {
			String columnFamily = null;

			List<Scan>scanList=new ArrayList<>();
			for (String prefix : rowPrefix) {
				Scan scan = new Scan();
				scan.setRowPrefixFilter(Bytes.toBytes(prefix));

				logger.info("prefix  is {}", scan.getFilter());
				for (String column : columnList) {
					scan.addColumn(column.split(Symbol.COLON_STRING)[0].getBytes(),
							column.split(Symbol.COLON_STRING)[1].getBytes());
					columnFamily = column.split(Symbol.COLON_STRING)[0];

				}
				scanList.add(scan);

			}
		
			return scanResultByPool(scanList, tableName, Bytes.toBytes(columnFamily));
		} catch (Exception e) {
			logger.error("Exception in getResultForRequest: {}", ExceptionUtils.getStackTrace(e));
		}
		return Collections.emptyList();
	}
}

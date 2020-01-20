package com.inn.foresight.module.nv.layer3.dao.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.inn.commons.Symbol;
import com.inn.commons.hadoop.hbase.HBaseResult;
import com.inn.commons.lang.StringUtils;
import com.inn.core.generic.exceptions.application.BusinessException;
import com.inn.foresight.core.generic.dao.impl.hbase.AbstractHBaseDao;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.layer3.constants.QMDLConstant;
import com.inn.foresight.module.nv.layer3.dao.INVHbaseDao;
import com.inn.foresight.module.nv.layer3.utils.NVLayer3Utils;

@Service("NVHbaseDaoImpl")
public class NVHbaseDaoImpl extends AbstractHBaseDao implements INVHbaseDao {

	/** The logger. */
	private static Logger logger = LogManager.getLogger(NVHbaseDaoImpl.class);
	
	
	@Override
	public List<HBaseResult> getHbaseDataForTimeStamp(String deviceId, Long startTime, Long endTime, String tableName) {
		try {
			String rowPrefix = StringUtils.reverse(deviceId.substring(2, deviceId.length()));
			return scanQMDLDataFromHbase(tableName,
					NVLayer3Utils.getScanForSignalMessageForBin(startTime, endTime, rowPrefix, Symbol.EMPTY_STRING));
		} catch (Exception e) {
			logger.error("Exception in Getting Data {} ", Utils.getStackTrace(e));
		}
		return null;
	}
	
	public List<HBaseResult> scanQMDLDataFromHbase(String tableName, Scan scan) throws IOException {
		logger.info("Inside scanQMDLDataFromHbase : {}",tableName);
		if (scan != null && tableName != null) {
			return scanResultByPool(scan, tableName, QMDLConstant.COLUMN_FAMILY.getBytes());
		}
	
		throw new BusinessException(QMDLConstant.INVALID_ARGUMENT);
	}

	
	
	@Override
	public List<HBaseResult> getReportDataByRowKeys(List<String> rowKeys, String tableName) {
		try {
			return getResultByPool(createGetList(rowKeys),tableName,
					QMDLConstant.COLUMN_FAMILY.getBytes());
		} catch (IOException e) {
			logger.error("Exception in getActiveDataByRowKeys {} ", e.getStackTrace());
		}
		return null;
	}
	
	private List<Get> createGetList(List<String> rowKeys){
		logger.debug("createGetList {} ", rowKeys.size());
		List<Get> listGet = new ArrayList<>();
		for(String key : rowKeys){
			Get get = new Get(key.getBytes());
			listGet.add(get);
		}
		return listGet;
	}
	
	@Override
	public List<HBaseResult> getSautReportData(List<Get> getList,String tableName) {
		try {
			return getResultByPool(getList,tableName,QMDLConstant.COLUMN_FAMILY.getBytes());
		} catch (IOException e) {
			logger.error("Exception in getActiveDataByRowKeys {} ", e.getStackTrace());
		}
		return null;
	}

	@Override
	public List<HBaseResult> getStealthDataFromHbase(String rowKey, String tableName) {
		logger.info("Inside getStealthDataFromHbase with RowKey : {}, TableName : {}",rowKey,tableName);
		try {
			return scanResultByPool(getScanObject(rowKey), tableName, QMDLConstant.COLUMN_FAMILY.getBytes());
		} catch (IOException e) {
			logger.error("Error Inside getStealthDataFromHbase {} ",Utils.getStackTrace(e));
		}
		return null;
	}
	private Scan getScanObject(String rowKey) {
		byte[] startRow = Bytes.toBytes(rowKey);
		byte[] endRow = new byte[startRow.length + 1];
		System.arraycopy(startRow, 0, endRow, 0, startRow.length);
		endRow[startRow.length] = (byte) 255;
		
		Scan scan = new Scan();
		scan.setRowPrefixFilter(Bytes.toBytes(rowKey));
		scan.withStartRow(startRow);
		scan.withStopRow(endRow);
		
		return scan;
	}

	@Override
	public List<HBaseResult> getStealthDataFromHbase(List<String> rowKeyList, String tableName) {
		try {
			return scanResultByPool(getScanList(rowKeyList), tableName, QMDLConstant.COLUMN_FAMILY.getBytes());
		} catch (IOException e) {
			logger.error("Error Inside getStealthDataFromHbase {} ",Utils.getStackTrace(e));
		}
		return null;
	}
	
	private List<Scan> getScanList(List<String> rowKeyList) {
		List<Scan> scanList = new ArrayList<>();
		if(rowKeyList != null && !rowKeyList.isEmpty()) {
			for(String rowKey: rowKeyList) {
				Scan scan = getScanObject(rowKey);
				scanList.add(scan);
			}
		}
		return scanList;
	}

	@Override
	public Boolean deleteDataUsingRowPrefix(String rowPreFix, String tableName) {
		return  deleteFromRowPreFilter(tableName, rowPreFix);
	}

}

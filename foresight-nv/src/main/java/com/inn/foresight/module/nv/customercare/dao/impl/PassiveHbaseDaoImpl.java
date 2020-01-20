package com.inn.foresight.module.nv.customercare.dao.impl;

import java.util.List;


import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.hadoop.hbase.HBaseResult;
import com.inn.foresight.core.generic.dao.impl.hbase.AbstractHBaseDao;
import com.inn.foresight.module.nv.customercare.constant.NVCustomerCareConstant;
import com.inn.foresight.module.nv.customercare.dao.IPassiveHbaseDao;

@Service("PassiveHbaseDaoImpl")
public class PassiveHbaseDaoImpl extends AbstractHBaseDao implements IPassiveHbaseDao{
	private static final Logger logger = LogManager.getLogger(PassiveHbaseDaoImpl.class);
	@Override
	public List<HBaseResult> getPassiveDataByRowKey(List<Get> rowKeyList) {
		List<HBaseResult> resultList=null;
		try {
			resultList=getResultByPool(rowKeyList, ConfigUtils.getString(NVCustomerCareConstant.NV_PASSIVE_HBASE_TABLE),  Bytes.toBytes(NVCustomerCareConstant.COLUMN_FAMILY));
		} catch (Exception e) {
			logger.error("Exception in getPassiveDataByRowKey: {}",ExceptionUtils.getStackTrace(e));
		}
		return resultList;
	}

	@Override
	public List<HBaseResult> getPassiveData(List<Scan> scanList) {
		List<HBaseResult> resultList=null;
		try {
			resultList =scanResultByPool(scanList, ConfigUtils.getString(NVCustomerCareConstant.NV_PASSIVE_HBASE_TABLE),  Bytes.toBytes(NVCustomerCareConstant.COLUMN_FAMILY));
		} catch (Exception e) {
			logger.error("Exception in getPassiveData: {}",ExceptionUtils.getStackTrace(e));
		}
		return resultList;
	}

}

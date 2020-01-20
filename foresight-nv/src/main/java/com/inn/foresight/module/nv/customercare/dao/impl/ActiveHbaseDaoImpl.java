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
import com.inn.foresight.module.nv.customercare.dao.IActiveHbaseDao;

@Service("ActiveHbaseDaoImpl")
public class ActiveHbaseDaoImpl extends AbstractHBaseDao implements IActiveHbaseDao{
	private static final Logger logger = LogManager.getLogger(ActiveHbaseDaoImpl.class);

	@Override
	public List<HBaseResult> getActiveDataByRowKey(List<Get> gets) {
		List<HBaseResult> resultList=null;
		try {
			resultList=getResultByPool(gets, ConfigUtils.getString(NVCustomerCareConstant.NV_ACTIVE_HBASE_TABLE), Bytes.toBytes(NVCustomerCareConstant.COLUMN_FAMILY));
		} catch (Exception e) {
			logger.error("Exception in getActiveDataByRowKey: {}",ExceptionUtils.getStackTrace(e));
		}
		return resultList;
	}


	@Override
	public List<HBaseResult> getActiveData(List<Scan> scanList) {
		List<HBaseResult> resultList=null;
		try {
			resultList =scanResultByPool(scanList, ConfigUtils.getString(NVCustomerCareConstant.NV_ACTIVE_HBASE_TABLE),  Bytes.toBytes(NVCustomerCareConstant.COLUMN_FAMILY));
		} catch (Exception e) {
			logger.error("Exception in getActiveData: {}",ExceptionUtils.getStackTrace(e));
		}
		return resultList;
	}
	
}

package com.inn.foresight.module.nv.inbuilding.dao.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.hadoop.hbase.client.Put;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.inn.commons.configuration.ConfigUtils;
import com.inn.foresight.core.generic.utils.ConfigUtil;
import com.inn.foresight.core.infra.constants.InBuildingConstants;
import com.inn.foresight.module.nv.hbase.NVHbaseGenericDao;

@Service("TestResultHbaseDaoImpl")
public class TestResultHbaseDaoImpl extends NVHbaseGenericDao implements ITestResultHbaseDao {
	public static final Logger logger = LogManager.getLogger(TestResultHbaseDaoImpl.class);
	private  String tableName = ConfigUtils.getString(ConfigUtil.FLOOR_PLAN_TABLE);

	@Override
	public String insertIBTestResultToHbase(Put put) throws IOException {
		try {
			List<Put>list=new ArrayList<>();
			list.add(put);
			insert(tableName,list);
			
			return InBuildingConstants.SUCCESS_JSON;
		} catch (IOException e) {
			logger.error("IOException in insert TestResult : {}", ExceptionUtils.getStackTrace(e));
			return ExceptionUtils.getMessage(e);
		} catch (Exception e) {
			logger.error("Exception in insert TestResult : {}", ExceptionUtils.getStackTrace(e));
			return ExceptionUtils.getMessage(e);
		}
	}

}
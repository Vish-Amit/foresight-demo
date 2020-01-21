package com.inn.foresight.core.report.dao.impl;

import java.io.IOException;

import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.inn.commons.hadoop.hbase.HBaseResult;
import com.inn.foresight.core.generic.dao.impl.hbase.AbstractHBaseDao;
import com.inn.foresight.core.report.dao.IReportGenerationHBASEDao;

@Service("ReportGenerationHBASEDaoImpl")
public class ReportGenerationHBASEDaoImpl extends AbstractHBaseDao implements  IReportGenerationHBASEDao {
	/** The logger. */
	private Logger logger = LogManager.getLogger(ReportGenerationHBASEDaoImpl.class);

	private static String NV_REPORT_HBASE_TABLE_NAME="NVReport";
	private static String NV_REPORT_HBASE_COL_FAMILY = "r";
	
	@Override
	public HBaseResult getReportDataForRowKey(Integer analyticsrepositoryId) throws IOException {

		logger.info("Going to get report data for analyticsrepositoryId {}", analyticsrepositoryId);

		Get get = new Get(Bytes.toBytes(String.valueOf(analyticsrepositoryId)));

		return getResultByPool(get, NV_REPORT_HBASE_TABLE_NAME, Bytes.toBytes(NV_REPORT_HBASE_COL_FAMILY));

	}
}

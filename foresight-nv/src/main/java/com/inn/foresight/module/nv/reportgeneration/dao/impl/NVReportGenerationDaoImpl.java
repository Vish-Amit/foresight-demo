package com.inn.foresight.module.nv.reportgeneration.dao.impl;

import java.io.IOException;

import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.inn.commons.hadoop.hbase.HBaseResult;
import com.inn.foresight.core.generic.dao.impl.hbase.AbstractHBaseDao;
import com.inn.foresight.module.nv.reportgeneration.constants.NVReportGenerationConstants;
import com.inn.foresight.module.nv.reportgeneration.dao.INVReportGenerationDao;

@Service("NVReportGenerationDaoImpl")
public class NVReportGenerationDaoImpl extends AbstractHBaseDao implements INVReportGenerationDao {

	/** The logger. */
	private static Logger logger = LogManager.getLogger(NVReportGenerationDaoImpl.class);

	@Override
	public HBaseResult getReportDataForRowKey(Integer analyticsrepositoryId)
			throws IOException {

			logger.info("Going to get report data for analyticsrepositoryId {}",
				analyticsrepositoryId);

				Get get = new Get(Bytes.toBytes(String.valueOf(analyticsrepositoryId)));

				return getResultByPool(get,NVReportGenerationConstants.NV_REPORT_HBASE_TABLE_NAME,Bytes.toBytes(NVReportGenerationConstants.NV_REPORT_HBASE_COL_FAMILY));



	         }



	      }




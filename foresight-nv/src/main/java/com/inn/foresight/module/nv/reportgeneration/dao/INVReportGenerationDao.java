package com.inn.foresight.module.nv.reportgeneration.dao;

import java.io.IOException;

import com.inn.commons.hadoop.hbase.HBaseResult;

public interface INVReportGenerationDao {

	HBaseResult getReportDataForRowKey(Integer analyticsrepositoryid)
			throws IOException;




}

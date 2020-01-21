package com.inn.foresight.core.report.dao;

import java.io.IOException;

import com.inn.commons.hadoop.hbase.HBaseResult;

public interface IReportGenerationHBASEDao {

	HBaseResult getReportDataForRowKey(Integer analyticsrepositoryId) throws IOException;
	

	
}

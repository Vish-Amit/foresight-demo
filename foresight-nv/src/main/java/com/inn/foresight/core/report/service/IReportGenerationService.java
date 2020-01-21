package com.inn.foresight.core.report.service;

import java.io.IOException;

import javax.ws.rs.core.Response;

public interface IReportGenerationService {

	Response getReportById(Integer rowkey, String extension) throws IOException ;

	Response getReportByPath(String filePath,String downloadFileName, String extension) throws IOException;

}

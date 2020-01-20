package com.inn.foresight.module.nv.report.service;

import java.io.File;

public interface INVDashboardReportService {

	File generateNvDashboardReport(String geographyName, String geographyType, String band, String technology, String operator, String startDate, String endDate) throws Exception;
	
}

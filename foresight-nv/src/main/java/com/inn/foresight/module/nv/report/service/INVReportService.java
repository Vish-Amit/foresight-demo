package com.inn.foresight.module.nv.report.service;

import javax.ws.rs.core.Response;

public interface INVReportService {

	void generateReportForWorkOrderId(Integer workorderId);

	Response updateSiteInfoInSSVTReport(String json);
}

package com.inn.foresight.module.nv.report.service;

import javax.ws.rs.core.Response;

public interface IMasterReportService {

	Response execute(String json);
	
}
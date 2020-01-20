package com.inn.foresight.module.nv.report.service;

import java.util.Map;

import javax.ws.rs.core.Response;

import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;

public interface ISSVT2gReportService {
	
	Response createReport(Integer workorderId, Map<String, Object> jsonMap, Integer analyticsrepoId, GenericWorkorder genericWorkorder);
	
}

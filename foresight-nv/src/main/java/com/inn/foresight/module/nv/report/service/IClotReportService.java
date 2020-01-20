package com.inn.foresight.module.nv.report.service;

import javax.ws.rs.core.Response;

public interface IClotReportService {

	//Response createReportForWorkOrderID(Integer workOrderId, Integer reportIntanceId);

	/**Response createReportForWorkOrderID(Integer workOrderId, Integer reportIntanceId, String username);.*/


	Response execute(String json);
}

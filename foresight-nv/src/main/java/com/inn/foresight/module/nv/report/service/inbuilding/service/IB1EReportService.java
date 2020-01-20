package com.inn.foresight.module.nv.report.service.inbuilding.service;

import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

public interface IB1EReportService {



	Response createFloorLevelReport(List<Integer> workOrderIds, Integer analyticsrepoId, Map<String, Object> jsonMap,
			boolean isBuildingLevel);

}

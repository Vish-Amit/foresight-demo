package com.inn.foresight.module.nv.workorder.stealth.rest;

import java.util.List;

import javax.ws.rs.core.Response;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;

public interface IStealthTaskRest {

	Response getDashboardDataFromHbase(List<String> hbaseColumns, String tableName, String rowkeyPrefix,
			String startDate, String endDate);

	Response getTopEnodeBDetails(String tableName, String type, String name, Long timeStamp, Long endTime);

	Response getStealthWoSummary(Integer workorderId);

	Response getCellWiseData(String tableName, String type, String name, Long timeStamp, Integer enodeBId,
			Long endTime);

	Response getKPIDistributionAndCount(String tableName, String type, String name, Long timeStamp, Integer enodeBId,
			Long endTime);

	@PreAuthorize("hasRole('ROLE_NV_WO_STEALTH_click')")
	@Secured("ROLE_NV_WO_STEALTH_click")
	Response getStealthKPISummary(Integer workorderId, Integer zoomLevel, Long startTime, Long endTime);

	@PreAuthorize("hasRole('ROLE_NV_WO_stopWO')")
	@Secured("ROLE_NV_WO_stopWO")
	Response completeStealthWorkorder(Integer workorderId);

	Response getStealthDeviceListByWOId(Integer workorderId);

	
}

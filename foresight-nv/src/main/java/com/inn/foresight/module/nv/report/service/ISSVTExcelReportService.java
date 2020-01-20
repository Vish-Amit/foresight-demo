package com.inn.foresight.module.nv.report.service;

import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import com.inn.foresight.module.nv.report.wrapper.SSVTReportSubWrapper;
import com.inn.foresight.module.nv.workorder.model.WORecipeMapping;

public interface ISSVTExcelReportService {
	
	Response execute(String json);

	List<String[]> getDriveData(Integer workorderId, List<String> fetchKPIList, Map<String, Integer> kpiIndexMap,
			List<WORecipeMapping> recipeMappings, Map<String, Integer> recipePciMap);


	void setMessageDataForSSVT(Integer workorderId, SSVTReportSubWrapper subwrapper,
			Map<String, List<String>> recipeOperatorListMap, Map<String, Integer> recipePciMap);

}

package com.inn.foresight.module.nv.report.customreport.ssvt.service;

import javax.ws.rs.core.Response;

import com.inn.foresight.module.nv.core.workorder.model.GWOMeta;

import java.util.List;
import java.util.Map;

public interface ISSVTService {
	
	Response execute(String json);

	String[] getSummaryData(Integer workorderId, Map<String, List<String>> map, List<String> fetchSummaryKPIList);

	GWOMeta getRecipeWiseSSVTSummary(Integer recipeId);


}
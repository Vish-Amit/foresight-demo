package com.inn.foresight.module.nv.inbuilding.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.springframework.security.access.prepost.PreAuthorize;

public interface InBuildingLayer3Rest {
	
	@PreAuthorize(value = "hasRole('ROLE_LAYER_RANPLAN_INDOOR_ANALYSIS_view')")
	Response getRelativeChangeForRsrp( Integer buildingId,
			 Integer floorId);

	Response getFloorPlanImage(Integer woId, Integer recipeId, String operatorName, Boolean isDrive,
			HttpServletRequest request);

	Response getBuildingInFoByWoId(Integer woId);

	Response getKpiAvgByBuildingId(Integer buildingId, Integer floorId, Integer band, Long startTime, Long endTime);

	Response getKpiAvgByFloorId(Integer floorId, Integer band, Long startTime, Long endTime);

}

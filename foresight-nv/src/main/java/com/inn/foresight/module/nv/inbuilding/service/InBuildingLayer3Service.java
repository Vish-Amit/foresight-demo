package com.inn.foresight.module.nv.inbuilding.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import com.inn.foresight.module.nv.inbuilding.wrapper.NVIBResultWrapper;

public interface InBuildingLayer3Service {

	Response getFloorPlanImage(Integer woId, Integer recipeId, String operatorName, Boolean isDrive, HttpServletRequest request);


	Response drawFloorPlanImgForLayer3(Integer woId, Integer recipeId, String operator, Boolean isDrive);


	NVIBResultWrapper getBuildingInFoByWoId(Integer woId);


	Response getKpiAvgByBuildingId(Integer buildingId, Integer floorId, Long startTime, Long endTime);

	NVIBResultWrapper getKpiAvgByFloorId(Integer floorId, Integer band, Long startTime, Long endTime);

	List<NVIBResultWrapper> getRelativeChangeForRsrp(Integer buildingId, Integer floorId);

}

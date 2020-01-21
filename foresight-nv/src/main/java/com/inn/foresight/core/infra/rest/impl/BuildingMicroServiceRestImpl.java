package com.inn.foresight.core.infra.rest.impl;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.inn.foresight.core.infra.service.IBuildingDataService;

@Path("/ms/NVInBuilding")
public class BuildingMicroServiceRestImpl {
	
	@Autowired
	IBuildingDataService iBuildingDataService;
	
	/** The logger. */
	private Logger logger = LogManager.getLogger(BuildingMicroServiceRestImpl.class);
	
	@GET
	@Path("getFloorPlan/{unitId}")
	public byte[] getFloorPlanByUnitId(@PathParam("unitId") Integer unitId, @QueryParam("kpi") String kpi) {
		logger.trace("request to get Floor Plan By unitId {}",unitId);
		return iBuildingDataService.getFloorPlanByUnitId(unitId, kpi);
	}
	
	@GET
	@Path("getFloorLegend/{unitId}/{kpi}")
	public String getFloorLegendByKpi(@PathParam("unitId") Integer unitId, @PathParam("kpi") String kpi) {
		logger.trace("request to get legend By kpi {}",kpi);
		return iBuildingDataService.getFloorLegendByKpi(unitId, kpi);
	}
	
	@GET
	@Path("getPredictionKpi/{unitId}")
	public String getPredictionKpiByUnitId(@PathParam("unitId") Integer unitId) {
		logger.trace("request to get prediction kpi By unitId {}", unitId);
		return iBuildingDataService.getPredictionKpiByUnitId(unitId);
	}
	
	@GET
	@Path("getBounds/{unitId}")
	public String getBoundsByUnitId(@PathParam("unitId") Integer unitId) {
		logger.trace("request to get bounds By unitId {}", unitId);
		return iBuildingDataService.getBoundsByUnitId(unitId);
	}

}
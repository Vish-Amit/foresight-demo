package com.inn.foresight.core.infra.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.infra.model.Building;
import com.inn.foresight.core.infra.wrapper.BuildingPolygonWrapper;
import com.inn.foresight.core.infra.wrapper.BuildingWrapper;
import com.inn.foresight.core.infra.wrapper.FloorWrapper;
import com.inn.foresight.core.infra.wrapper.GeographyDetailWrapper;
import com.inn.foresight.core.infra.wrapper.UnitWrapper;

/** The Interface IBuildingDataService. */
public interface IBuildingDataService {

	/**
	 * Creates the building.
	 *
	 * @param building
	 *            the building
	 * @return the building wrapper
	 * @throws RestException
	 *             the rest exception
	 */
	BuildingWrapper createBuilding(BuildingWrapper building) ;

	/**
	 * Gets the building list by cluster.
	 *
	 * @param cluster
	 *            the cluster
	 * @return the building list by cluster
	 * @throws RestException
	 *             the rest exception
	 */
	List<BuildingWrapper> getBuildingListByCluster(String cluster);

	/**
	 * Gets the building details by pk.
	 *
	 * @param id
	 *            the id
	 * @return the building details by pk
	 * @throws RestException
	 *             the rest exception
	 */
	BuildingWrapper getBuildingDetailsByPk(Integer id);

	/**
	 * Get Nearest Building From Location.
	 *
	 * @param latitude
	 *            latitude of the location
	 * @param longitude
	 *            longitude of the location
	 * @return list of BuildingWrapper with nearest six buildings from location
	 * @throws RestException
	 *             the rest exception
	 */
	List<BuildingWrapper> getNearestBuildingsFromLocation(Double latitude, Double longitude);

	/**
	 * Creates the building along with floor plan.
	 *
	 * @param request
	 *            the request
	 * @return the building wrapper
	 * @throws RestException
	 *             the rest exception
	 */
	BuildingWrapper createBuildingAlongWithFloorPlan(HttpServletRequest request);

	/**
	 * Get Building List By Name.
	 *
	 * @param name            name to search buildings
	 * @return list of BuildingWrapper
	 * @throws RestException             the rest exception
	 */
	List<BuildingWrapper> getBuildingListByName(String name);


	/**
	 * Find by pk.
	 *
	 * @param id the id
	 * @return the building
	 */
	Building findByPk(Integer id);


	/**
	 * Creates the building from app.
	 *
	 * @param buildingData
	 *            encrypted building wrapper json
	 * @return the building wrapper
	 * @throws RestException
	 *             the rest exception
	 */
	BuildingWrapper createBuildingData(String buildingData);

	/**
	 * Persist advance search details.
	 *
	 * @param buildingData the building data
	 * @throws RestException the rest exception
	 */
	void persistAdvanceSearchDetails(Building buildingData);
	
	/**
	 * Gets the building details for advance search.
	 *
	 * @param buildingId
	 *            the buildingId
	 * @return Map with building lat, long, address, name.
	 * @throws RestException
	 *             the rest exception
	 */
	
	Map getBuildingDetailsForAdvanceSearch(Integer buildingId);

	String tagBuildingGeographyL4();

	GeographyDetailWrapper getGeographyDetailByBuildingId(String buildingId);

	List<BuildingPolygonWrapper> getBuildingPolygonByViewPort(Double swLat, Double swLng, Double neLat, Double neLng);

	List<FloorWrapper> getFloorDetailsByBuildingId(String buildingId);

	List<UnitWrapper> getUnitDetailsByFloorId(Integer floorId);

	BuildingWrapper getBuildingDetailByBuildingId(String buildingId);

	BuildingWrapper getCoverageDetails(String buildingId, Integer floorId);

	byte[] getFloorPlanByUnitId(Integer unitId, String kpi);

	String getFloorLegendByKpi(Integer unitId, String kpi);

	Long getNumberOfFloorsByBuildingId(String buildingId);

	String getPredictionKpiByUnitId(Integer unitId);

	String getBoundsByUnitId(Integer unitId);

	List<List<List<Double>>> getPolygonAsArrayList(String polygon);

}

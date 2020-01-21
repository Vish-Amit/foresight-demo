package com.inn.foresight.core.infra.dao;

import java.util.List;

import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.core.infra.model.Building;
import com.inn.foresight.core.infra.wrapper.BuildingPolygonWrapper;
import com.inn.foresight.core.infra.wrapper.BuildingWrapper;
import com.inn.foresight.core.infra.wrapper.GeographyDetailWrapper;
import com.inn.foresight.core.infra.wrapper.UnitWrapper;
import com.inn.product.um.geography.model.GeographyL4;

/** The Interface IBuildingDataDao. */
public interface IBuildingDataDao {
	
	/**
	 * Creates the building.
	 *
	 * @param buildingData the building data
	 * @return the building
	 * @throws DaoException the dao exception
	 */
	Building createBuilding(Building buildingData);
	
	/**
	 * Updates the building.
	 *
	 * @param buildingData the building data
	 * @return the building
	 * @throws DaoException the dao exception
	 */
	Building updateBuilding(Building buildingData);

	/**
	 * Find by pk.
	 *
	 * @param id the id
	 * @return the building
	 * @throws DaoException the dao exception
	 */
	Building findByPk(Integer id);
	
	/**
	 * Find building wrapper by pk.
	 *
	 * @param id the id
	 * @return the building
	 * @throws DaoException the dao exception
	 */
	BuildingWrapper findBuildingWrapperByPk(Integer id);
	/**
	 * Gets the building list by cluster.
	 *
	 * @param geographyL4 the geography L 4
	 * @return the building list by cluster
	 * @throws DaoException the dao exception
	 */
	List<BuildingWrapper> getBuildingListByCluster(GeographyL4 geographyL4);
	
	/**
	 * Find All.
	 *
	 * @return List<Building>, all building data from DB.
	 * @throws DaoException
	 *             the dao exception
	 */
	List<BuildingWrapper> getAllBuildings();
	
	/**
	 * Get Buildings By Name.
	 *
	 * @param name
	 *            name of building to search
	 * @return list of buildings
	 * @throws DaoException
	 *             the dao exception
	 */
	List<BuildingWrapper> getBuildingsByName(String name);

	/**
	 * Gets the building by name and address.
	 *
	 * @param buildingName the building name
	 * @param address the address
	 * @return the building by name and address
	 * @throws DaoException 
	 */
	Building getBuildingByNameAndAddress(String buildingName, String address);

	List<Building> tagBuildingGeographyL4();
	
	Building getBuildingByNameAndLatLong(String buildingName,Double latitude, Double longitude);
	Building getBuildingByBuildingId(String buildingId);
	
	Integer getGeographyL4Id(String name);

	GeographyDetailWrapper getGeographyDetailByBuildingId(String buildingId);
		
	List<BuildingPolygonWrapper> getBuildingPolygonByViewPort(Double swLat, Double swLng, Double neLat, Double neLng);

	BuildingWrapper getBuildingDetailByBuildingId(String buildingId);
	
	BuildingWrapper getBuildingCoverage(String buildingId);
}

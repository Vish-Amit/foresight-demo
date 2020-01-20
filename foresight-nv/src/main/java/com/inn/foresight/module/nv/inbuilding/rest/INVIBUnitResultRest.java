package com.inn.foresight.module.nv.inbuilding.rest;

import javax.ws.rs.core.Response;

/**
 * The Interface INVIBUnitResultRest.
 *
 * @author innoeye
 * date - 15-Mar-2018 7:58:44 PM
 */
public interface INVIBUnitResultRest {

	/**
	 * Search NVIB buildings.
	 *
	 * @param swLat the sw lat
	 * @param swLng the sw lng
	 * @param neLat the ne lat
	 * @param neLng the ne lng
	 * @param technology the technology
	 * @param buildingType the building type
	 * @param zoomLevel the zoom level
	 * @return the response
	 */
	Response searchNVIBBuildings(Double swLat, Double swLng,
			Double neLat, Double neLng, String technology, String buildingType,
			Integer zoomLevel);

	/**
	 * Gets the wings.
	 *
	 * @param buildingId the building id
	 * @param technology the technology
	 * @return the wings
	 */
	Response getWings(Integer buildingId, String technology);

	/**
	 * Gets the floors.
	 *
	 * @param wingId the wing id
	 * @param technology the technology
	 * @return the floors
	 */
	Response getFloors(Integer wingId, String technology);

	/**
	 * Gets the units.
	 *
	 * @param floorId the floor id
	 * @param technology the technology
	 * @return the units
	 */
	Response getUnits(Integer floorId, String technology);

	/**
	 * Gets the recipe file name.
	 *
	 * @param unitId the unit id
	 * @param technology the technology
	 * @return the recipe file name
	 */
	Response getRecipeFileName(Integer unitId, String technology);

	Response getUnitWiseWorkorder(Integer unitId, String technology);

	Response getUnitWiseWorkorderCount(Integer unitId, String technology);

	Response getWingLevelWiseIBUnitResult(Integer id, String type,String technology);

}

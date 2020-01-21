package com.inn.foresight.core.infra.dao;

import java.util.List;

import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.core.infra.model.Floor;
import com.inn.foresight.core.infra.model.Wing;
import com.inn.foresight.core.infra.wrapper.BuildingWrapper;
import com.inn.foresight.core.infra.wrapper.FloorWrapper;

/** The Interface IFloorDataDao. */
public interface IFloorDataDao {
	 
 	/**
 	 * Creates the floor.
 	 *
 	 * @param floorData the floor data
 	 * @return the floor
 	 * @throws DaoException the dao exception
 	 */
 	Floor createFloor(Floor floorData );
 	
	/**
	 * Update floor.
	 *
	 * @param floor the floor
	 * @return the floor
	 * @throws DaoException the dao exception
	 */
	Floor updateFloor(Floor floor);
	 
 	/**
 	 * Gets the all floor for wing.
 	 *
 	 * @param wing the wing
 	 * @return the all floor for wing
 	 * @throws DaoException the dao exception
 	 */
 	List<FloorWrapper> getAllFloorForWing(Wing wing);
	
	/**
	 * Find by pk.
	 *
	 * @param id the id
	 * @return the floor
	 * @throws DaoException the dao exception
	 */
	Floor findByPk(Integer id);
	
	List<Floor> getFloorDetailByBuilding(Integer buildingId);

	BuildingWrapper getFloorCoverage(String buildingId, Integer floorId);

	List<FloorWrapper> getFloorDetailsByBuildingId(String buildingId);

	Long getNumberOfFloorsByBuildingId(String buildingId);
}

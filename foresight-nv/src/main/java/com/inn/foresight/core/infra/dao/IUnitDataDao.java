package com.inn.foresight.core.infra.dao;

import java.util.List;

import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.core.infra.model.Floor;
import com.inn.foresight.core.infra.model.Unit;
import com.inn.foresight.core.infra.wrapper.UnitWrapper;

/** The Interface IUnitDataDao. */
public interface IUnitDataDao {
	
	/**
	 * Creates the unit.
	 *
	 * @param unitData the unit data
	 * @return the unit
	 * @throws DaoException the dao exception
	 */
	Unit createUnit(Unit unitData );
	
	/**
	 * Update unit.
	 *
	 * @param unitData the unit data
	 * @return the unit
	 * @throws DaoException the dao exception
	 */
	Unit updateUnit(Unit unitData );
	/**
	 * Gets the all unit for floor.
	 *
	 * @param floor the floor
	 * @return the all unit for floor
	 * @throws DaoException the dao exception
	 */
	List<UnitWrapper> getAllUnitForFloor(Floor floor);
	
	/**
	 * Find by pk.
	 *
	 * @param id the id
	 * @return the Unit
	 * @throws DaoException the dao exception
	 */
	Unit findByPk(Integer id);

	List<UnitWrapper> getAllAdhocUnitForFloor(Floor floor);

	List<UnitWrapper> getUnitDetailsByFloorId(Integer floorId);
}

package com.inn.foresight.core.infra.service;

import java.util.List;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.infra.model.Floor;
import com.inn.foresight.core.infra.model.Unit;
import com.inn.foresight.core.infra.wrapper.FloorWrapper;
import com.inn.foresight.core.infra.wrapper.UnitWrapper;

/** The Interface IUnitDataService. */
public interface IUnitDataService {
	
	/**
	 * Find by pk.
	 *
	 * @param id the id
	 * @return the unit
	 * @throws RestException the rest exception
	 */
	Unit findByPk(Integer id) ;
	
	/**
	 * Creates the unit.
	 *
	 * @param unitData the unit data
	 * @return the unit
	 * @throws RestException the rest exception
	 */
	Unit createUnit(Unit unitData );
	
	/**
	 * Updates the unit.
	 *
	 * @param unitData the unit data
	 * @return the unit
	 * @throws RestException the rest exception
	 */
	Unit updateUnit(Unit unitData );

	/**
	 * Delete units.
	 *
	 * @param unitList the unit list
	 * @throws RestException the rest exception
	 */
	void deleteUnits(List<UnitWrapper> unitList);

	/**
	 * Creates the unit list.
	 *
	 * @param floorData the floor data
	 * @param floorWrapper the floor wrapper
	 * @throws RestException the rest exception
	 */
	void createOrUpdateUnitList(Floor floorData, FloorWrapper floorWrapper);

	/**
	 * Update unit list.
	 *
	 * @param floorWrapper the floor wrapper
	 * @throws RestException the rest exception
	 */
	void updateUnitList(FloorWrapper floorWrapper);

	/**
	 * Gets the all unit wrapper for floor.
	 *
	 * @param floorId the floor id
	 * @param unitWrapperList the unit wrapper list
	 * @return the all unit wrapper for floor
	 * @throws RestException the rest exception
	 */
	List<UnitWrapper> getAllUnitWrapperForFloor(Integer floorId);;
}

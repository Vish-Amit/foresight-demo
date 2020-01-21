package com.inn.foresight.core.infra.service;

import java.util.List;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.infra.model.Floor;
import com.inn.foresight.core.infra.model.Wing;
import com.inn.foresight.core.infra.wrapper.FloorWrapper;
import com.inn.foresight.core.infra.wrapper.WingWrapper;

/** The Interface IFloorDataService. */
public interface IFloorDataService {

	/**
	 * Find by pk.
	 *
	 * @param id
	 *            the id
	 * @return the floor
	 * @throws RestException
	 *             the rest exception
	 */
	Floor findByPk(Integer id);

	/**
	 * Creates the floor.
	 *
	 * @param floorData
	 *            the floor data
	 * @return the floor
	 * @throws RestException
	 *             the rest exception
	 */
	Floor createFloor(Floor floorData);

	/**
	 * Updates the floor.
	 *
	 * @param floorData
	 *            the floor data
	 * @return the floor
	 * @throws RestException
	 *             the rest exception
	 */
	Floor updateFloor(Floor floorData);

	/**
	 * Delete floors.
	 *
	 * @param floorWrappers
	 *            the floor wrappers
	 */
	void deleteFloors(List<FloorWrapper> floorWrappers);

	/**
	 * Creates the floor list.
	 *
	 * @param wingData
	 *            the wing data
	 * @param wingWrapper
	 *            the wing wrapper
	 * @throws RestException
	 *             the rest exception
	 */
	void createOrUpdateFloorList(Wing wingData, WingWrapper wingWrapper);

	/**
	 * Update floor list.
	 *
	 * @param wingWrapper
	 *            the wing wrapper
	 * @throws RestException
	 *             the rest exception
	 */
	void updateFloorList(WingWrapper wingWrapper);

	/**
	 * Gets the all floor wrapper for wing.
	 *
	 * @param id
	 *            the id
	 * @return the all floor wrapper for wing
	 * @throws RestException
	 *             the rest exception
	 */
	List<FloorWrapper> getAllFloorWrapperForWing(Integer id) ;
}

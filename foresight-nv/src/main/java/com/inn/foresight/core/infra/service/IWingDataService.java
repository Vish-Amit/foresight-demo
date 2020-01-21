package com.inn.foresight.core.infra.service;

import java.util.List;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.infra.model.Building;
import com.inn.foresight.core.infra.model.Wing;
import com.inn.foresight.core.infra.wrapper.BuildingWrapper;
import com.inn.foresight.core.infra.wrapper.WingWrapper;

/** The Interface IWingDataService. */
public interface IWingDataService {

	/**
	 * Find by pk.
	 *
	 * @param id
	 *            the id
	 * @return the wing
	 * @throws RestException
	 *             the rest exception
	 */
	Wing findByPk(Integer id);

	/**
	 * Creates the wing.
	 *
	 * @param wingData
	 *            the wing data
	 * @return the wing
	 * @throws RestException
	 *             the rest exception
	 */
	Wing createWing(Wing wingData);

	/**
	 * Creates the wing list.
	 *
	 * @param building
	 *            the building
	 * @param buildingWrapper
	 *            the building wrapper
	 * @throws RestException
	 *             the rest exception
	 */
	void createOrUpdateWingList(Building building, BuildingWrapper buildingWrapper);

	/**
	 * Updates the wing.
	 *
	 * @param wingData
	 *            the wing data
	 * @return the wing
	 * @throws RestException
	 *             the rest exception
	 */
	Wing updateWing(Wing wingData);

	/**
	 * Delete wings.
	 *
	 * @param wingWrappers
	 *            the wing wrappers
	 */
	void deleteWings(List<WingWrapper> wingWrappers);

	/**
	 * Gets the all wing wrapper for building.
	 *
	 * @param id
	 *            the id
	 * @return the all wing wrapper for building
	 * @throws RestException
	 *             the rest exception
	 */
	List<WingWrapper> getAllWingWrapperForBuilding(Integer id);

	/**
	 * Update wing list.
	 *
	 * @param buildingWrapper
	 *            the building wrapper
	 * @throws RestException
	 *             the rest exception
	 */
	void updateWingList(BuildingWrapper buildingWrapper);
}

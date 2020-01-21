package com.inn.foresight.core.infra.dao;

import java.util.List;

import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.core.infra.model.Building;
import com.inn.foresight.core.infra.model.Wing;
import com.inn.foresight.core.infra.wrapper.WingWrapper;

/** The Interface IWingDataDao. */
public interface IWingDataDao {
	  
  	/**
  	 * Creates the wing.
  	 *
  	 * @param wingData the wing data
  	 * @return the wing
  	 * @throws DaoException the dao exception
  	 */
  	Wing createWing(Wing wingData);
  	
	/**
	 * Update wing.
	 *
	 * @param wing the wing
	 * @return the wing
	 * @throws DaoException the dao exception
	 */
	Wing updateWing(Wing wing);

	/**
	 * Gets the all wing for building.
	 *
	 * @param building the building
	 * @return the all wing for building
	 * @throws DaoException the dao exception
	 */
	List<WingWrapper> getAllWingForBuilding(Building building);

	/**
	 * Find by pk.
	 *
	 * @param id the id
	 * @return the wing
	 * @throws DaoException the dao exception
	 */
	Wing findByPk(Integer id);
}

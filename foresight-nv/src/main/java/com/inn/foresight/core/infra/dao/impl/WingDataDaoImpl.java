package com.inn.foresight.core.infra.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.core.infra.constants.InBuildingConstants;
import com.inn.foresight.core.infra.dao.IWingDataDao;
import com.inn.foresight.core.infra.model.Building;
import com.inn.foresight.core.infra.model.Wing;
import com.inn.foresight.core.infra.wrapper.WingWrapper;

/** The Class WingDataDaoImpl. */
@Repository("WingDataDaoImpl")
public class WingDataDaoImpl extends HibernateGenericDao<Integer, Wing> implements IWingDataDao {
	/** The logger. */
	private Logger logger = LogManager.getLogger(WingDataDaoImpl.class);
	
	/** Instantiates a new wing data dao impl. */
	public WingDataDaoImpl() {
		super(Wing.class);
	}

	/**
  	 * Creates the wing.
  	 *
  	 * @param wingData the wing data
  	 * @return the wing
  	 * @throws DaoException the dao exception
  	 */
	@Override
	public Wing createWing(Wing wingData) {
		try {
			return super.create(wingData);
		} catch (Exception e) {
			logger.error(InBuildingConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new DaoException(ExceptionUtils.getMessage(e));
		}
	}

	/**
	 * Gets the all wing for building.
	 *
	 * @param building the building
	 * @return the all wing for building
	 * @throws DaoException the dao exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<WingWrapper> getAllWingForBuilding(Building building) {
		Query query = null;
		try {
			query = getEntityManager()
					.createNamedQuery(InBuildingConstants.GET_ALL_WING_FOR_BUILDING)
					.setParameter(InBuildingConstants.BUILDING_PLACE_HOLDER, building);
		} catch (Exception e) {
			logger.error(InBuildingConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new DaoException(ExceptionUtils.getStackTrace(e));
		}
		
		return query.getResultList();
	}
	
	/**
	 * Find by pk.
	 *
	 * @param id the id
	 * @return the wing
	 * @throws DaoException the dao exception
	 */
	@Override
	public Wing findByPk(Integer id) {
		try {
			return super.findByPk(id);
		} catch (Exception e) {
			logger.error(InBuildingConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new DaoException(ExceptionUtils.getMessage(e));
		}
	}

	/**
	 * Update wing.
	 *
	 * @param wing the wing
	 * @return the wing
	 * @throws DaoException the dao exception
	 */
	@Override
	public Wing updateWing(Wing wing) {
		try {
			return super.update(wing);
		} catch (Exception e) {
			logger.error(InBuildingConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new DaoException(ExceptionUtils.getMessage(e));
		}
	}

}

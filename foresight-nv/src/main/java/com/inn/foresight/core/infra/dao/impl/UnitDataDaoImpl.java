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
import com.inn.foresight.core.infra.dao.IUnitDataDao;
import com.inn.foresight.core.infra.model.Floor;
import com.inn.foresight.core.infra.model.Unit;
import com.inn.foresight.core.infra.wrapper.UnitWrapper;

/** The Class UnitDataDaoImpl. */
@Repository("UnitDataDaoImpl")
public class UnitDataDaoImpl extends HibernateGenericDao<Integer, Unit> implements IUnitDataDao {
	/** The logger. */
	private Logger logger = LogManager.getLogger(UnitDataDaoImpl.class);

	/** Instantiates a new unit data dao impl. */
	public UnitDataDaoImpl() {
		super(Unit.class);
	}

	/**
	 * Creates the unit.
	 *
	 * @param unitData the unit data
	 * @return the unit
	 * @throws DaoException the dao exception
	 */
	@Override
	public Unit createUnit(Unit unitData) {
		try {
			return super.create(unitData);
		} catch (Exception e) {
			logger.error(InBuildingConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new DaoException(ExceptionUtils.getMessage(e));
		}
	}

	/**
	 * Gets the all unit for floor.
	 *
	 * @param floor the floor
	 * @return the all unit for floor
	 * @throws DaoException the dao exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<UnitWrapper> getAllUnitForFloor(Floor floor) {
		Query query = null;
		try {
			query = getEntityManager()
					.createNamedQuery(InBuildingConstants.GET_ALL_UNIT_FOR_FLOOR)
					.setParameter(InBuildingConstants.FLOOR_PLACE_HOLDER, floor);
		} catch (Exception e) {
			logger.error(InBuildingConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new DaoException(ExceptionUtils.getMessage(e));
		}
		return query.getResultList();
	}
	
	/**
	 * Find by pk.
	 *
	 * @param id the id
	 * @return the Unit
	 * @throws DaoException the dao exception
	 */
	@Override
	public Unit findByPk(Integer id) {
		try {
			return super.findByPk(id);
		} catch (Exception e) {
			logger.error(InBuildingConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new DaoException(ExceptionUtils.getMessage(e));
		}
	}


	/**
	 * Update unit.
	 *
	 * @param unitData the unit data
	 * @return the unit
	 * @throws DaoException the dao exception
	 */
	@Override
	public Unit updateUnit(Unit unitData) {
		try {
			return super.update(unitData);
		} catch (Exception e) {
			logger.error(InBuildingConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new DaoException(ExceptionUtils.getMessage(e));
		}	
	}
	/**
	 * Gets the all unit for floor.
	 *
	 * @param floor the floor
	 * @return the all unit for floor
	 * @throws DaoException the dao exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<UnitWrapper> getAllAdhocUnitForFloor(Floor floor) {
		Query query = null;
		try {
			query = getEntityManager()
					.createNamedQuery("getAllAdhocUnitForFloor")
					.setParameter(InBuildingConstants.FLOOR_PLACE_HOLDER, floor);
		} catch (Exception e) {
			logger.error(InBuildingConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new DaoException(ExceptionUtils.getMessage(e));
		}
		return query.getResultList();
	}
	
	@Override
	public List<UnitWrapper> getUnitDetailsByFloorId(Integer floorId) {
		return getEntityManager().createNamedQuery("getUnitDetailsByFloorId", UnitWrapper.class)
				.setParameter("floorId", floorId).getResultList();
	}
	
}

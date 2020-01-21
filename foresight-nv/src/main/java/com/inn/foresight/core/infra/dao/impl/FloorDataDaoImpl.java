package com.inn.foresight.core.infra.dao.impl;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.constants.InBuildingConstants;
import com.inn.foresight.core.infra.dao.IFloorDataDao;
import com.inn.foresight.core.infra.model.Floor;
import com.inn.foresight.core.infra.model.Wing;
import com.inn.foresight.core.infra.wrapper.BuildingWrapper;
import com.inn.foresight.core.infra.wrapper.FloorWrapper;

/** The Class FloorDataDaoImpl. */
@Repository("FloorDataDaoImpl")
public class FloorDataDaoImpl extends HibernateGenericDao<Integer, Floor> implements IFloorDataDao {
	/** The logger. */
	private Logger logger = LogManager.getLogger(FloorDataDaoImpl.class);

	/** Instantiates a new floor data dao impl. */
	public FloorDataDaoImpl() {
		super(Floor.class);
	}

	/**
 	 * Creates the floor.
 	 *
 	 * @param floorData the floor data
 	 * @return the floor
 	 * @throws DaoException the dao exception
 	 */
	@Override
	public Floor createFloor(Floor floorData) {
		try {
			return super.create(floorData);
		} catch (Exception e) {
			logger.error(InBuildingConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new DaoException(ExceptionUtils.getMessage(e));
		}
	}

	/**
 	 * Gets the all floor for wing.
 	 *
 	 * @param wing the wing
 	 * @return the all floor for wing
 	 * @throws DaoException the dao exception
 	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<FloorWrapper> getAllFloorForWing(Wing wing) {
		Query query = null;
		try {
			query = getEntityManager()
					.createNamedQuery(InBuildingConstants.GET_ALL_FLOOR_FOR_WING)
					.setParameter(InBuildingConstants.WING_PLACE_HOLDER, wing);
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
	 * @return the floor
	 * @throws DaoException the dao exception
	 */
	@Override
	public Floor findByPk(Integer id) {
		try {
			return super.findByPk(id);
		} catch (Exception e) {
			logger.error(InBuildingConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new DaoException(ExceptionUtils.getMessage(e));
		}
	}

	/**
	 * Update floor.
	 *
	 * @param floor the floor
	 * @return the floor
	 * @throws DaoException the dao exception
	 */
	@Override
	public Floor updateFloor(Floor floor) {
		try {
			return super.update(floor);
		} catch (Exception e) {
			logger.error(InBuildingConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new DaoException(ExceptionUtils.getMessage(e));
		}
	}

	@Override
	public List<Floor> getFloorDetailByBuilding(Integer buildingId) {
		if(Utils.hasValue(buildingId))
		{
			logger.info("Inside getFloorDetailByBuilding method {}",buildingId);
			List<Floor> floorResult = null;
			Query query = null;
			try {
				query = getEntityManager().createNamedQuery(InBuildingConstants.GET_FLOOR_DETAIL_BUILDINGWISE)
						.setParameter(InBuildingConstants.BUILDING_ID, buildingId);
				return query.getResultList();
			} catch (Exception e) {
				logger.error(InBuildingConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
				throw new DaoException(ExceptionUtils.getMessage(e));
			}
			
		}
		return null;
	}

	@Override
	public BuildingWrapper getFloorCoverage(String buildingId, Integer floorNumber) {
		try {
			return getEntityManager().createNamedQuery("getFloorCoverage", BuildingWrapper.class)
					.setParameter("buildingId", buildingId).setParameter("floorNumber", floorNumber).getSingleResult();
		} catch (NoResultException e) {
			logger.error("No result found for getCoverageDetails by buildingId:{}", buildingId);
			return null;
		}
	}

	@Override
	public List<FloorWrapper> getFloorDetailsByBuildingId(String buildingId) {
		return getEntityManager().createNamedQuery("getFloorDetailsByBuildingId", FloorWrapper.class)
				.setParameter("buildingId", buildingId).getResultList();
	}
	
	@Override
	public Long getNumberOfFloorsByBuildingId(String buildingId) {
		try {
			return getEntityManager().createNamedQuery("getNumberOfFloorsByBuildingId", Long.class)
					.setParameter("buildingId", buildingId).getSingleResult();
		} catch (NoResultException e) {
			logger.error("No result found for buildingId:{}", buildingId);
			return null;
		}
	}
}

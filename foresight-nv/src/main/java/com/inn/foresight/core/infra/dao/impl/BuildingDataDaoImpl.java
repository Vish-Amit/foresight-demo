package com.inn.foresight.core.infra.dao.impl;

import java.util.Collections;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.core.infra.constants.InBuildingConstants;
import com.inn.foresight.core.infra.dao.IBuildingDataDao;
import com.inn.foresight.core.infra.model.Building;
import com.inn.foresight.core.infra.wrapper.BuildingPolygonWrapper;
import com.inn.foresight.core.infra.wrapper.BuildingWrapper;
import com.inn.foresight.core.infra.wrapper.GeographyDetailWrapper;
import com.inn.foresight.core.infra.wrapper.UnitWrapper;
import com.inn.product.um.geography.model.GeographyL4;

/** The Class BuildingDataDaoImpl. */
@Repository("BuildingDataDaoImpl")
public class BuildingDataDaoImpl extends HibernateGenericDao<Integer, Building> implements IBuildingDataDao {
	/** The logger. */
	private Logger logger = LogManager.getLogger(BuildingDataDaoImpl.class);

	/** Instantiates a new building data dao impl. */
	public BuildingDataDaoImpl() {
		super(Building.class);
	}

	/**
	 * Creates the building.
	 *
	 * @param buildingData
	 *            the building data
	 * @return the building
	 * @throws DaoException
	 *             the dao exception
	 */
	@Override
	public Building createBuilding(Building buildingData) {
		try {
			return create(buildingData);
		} catch (Exception e) {
			logger.error(InBuildingConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			Throwable throwable = e.getCause();
			if (throwable instanceof ConstraintViolationException) {
				throw new DaoException(InBuildingConstants.BUILDING_NAME_ADDRESS_ALREADY_EXIST);
			} else {
				throw new DaoException(ExceptionUtils.getMessage(e));
			}
		}
	}

	/**
	 * Find by pk.
	 *
	 * @param id
	 *            the id
	 * @return the building
	 * @throws DaoException
	 *             the dao exception
	 */
	@Override
	public Building findByPk(Integer entityPk) {
		try {
			return super.findByPk(entityPk);
		} catch (Exception e) {
			logger.error(InBuildingConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new DaoException(ExceptionUtils.getMessage(e));
		}
	}

	/**
	 * Find All.
	 *
	 * @return List<Building>, all building data from DB.
	 * @throws DaoException
	 *             the dao exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<BuildingWrapper> getAllBuildings() {
		Query query = null;
		try {
			query = getEntityManager().createNamedQuery(InBuildingConstants.GET_ALL_BUILDINGS);
			return query.getResultList();
		} catch (Exception e) {
			logger.error(InBuildingConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new DaoException(ExceptionUtils.getMessage(e));
		}
		
	}

	/**
	 * Gets the building list by cluster.
	 *
	 * @param geographyL4
	 *            the geography L 4
	 * @return the building list by cluster
	 * @throws DaoException
	 *             the dao exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<BuildingWrapper> getBuildingListByCluster(GeographyL4 geographyL4) {
		Query query = null;
		try {
			query = getEntityManager()	.createNamedQuery(InBuildingConstants.GET_BUILDINGS_BY_CLUSTER)
										.setParameter(InBuildingConstants.GEOGRAPHY_L4_PLACE_HOLDER, geographyL4);
			return query.getResultList();
		} catch (Exception e) {
			logger.error(InBuildingConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
		}
		return Collections.emptyList();
	}

	@Override
	public Building updateBuilding(Building buildingData) {
		try {
			return super.update(buildingData);
		} catch (Exception e) {
			logger.error(InBuildingConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new DaoException(ExceptionUtils.getMessage(e));
		}
	}

	/**
	 * Get Buildings By Name.
	 *
	 * @param name
	 *            name of building to search
	 * @return list of buildings
	 * @throws DaoException
	 *             the dao exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<BuildingWrapper> getBuildingsByName(String name) {
		Query query = null;
		try {
			query = getEntityManager()	.createNamedQuery(InBuildingConstants.GET_BUILDINGS_BY_NAME)
										.setParameter(InBuildingConstants.BUILDING_NAME_PLACEHOLDER, name);
			return query.getResultList();
		} catch (Exception e) {
			logger.error(InBuildingConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new DaoException(ExceptionUtils.getMessage(e));
		}
	}

	/**
	 * Find building wrapper by pk.
	 *
	 * @param id
	 *            the id
	 * @return the building
	 * @throws DaoException
	 *             the dao exception
	 */
	@Override
	public BuildingWrapper findBuildingWrapperByPk(Integer id) {
		Query query = null;
		try {
			query = getEntityManager()	.createNamedQuery(InBuildingConstants.GET_BUILDING_BY_ID)
										.setParameter(InBuildingConstants.BUILDING_ID_PLACEHOLDER, id);
			return (BuildingWrapper) query.getSingleResult();
		} catch (Exception e) {
			logger.error(InBuildingConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new DaoException(ExceptionUtils.getMessage(e));
		}
		
	}

	/**
	 * Gets the building by name and address.
	 *
	 * @param buildingName
	 *            the building name
	 * @param address
	 *            the address
	 * @return the building by name and address
	 */
	@Override
	public Building getBuildingByNameAndAddress(String buildingName, String address) {
		Query query = null;
		Building building=null;
		try {
			query = getEntityManager()	.createNamedQuery(InBuildingConstants.GET_BUILDING_BY_NAME_AND_ADDRESS)
										.setParameter("buildingName", buildingName)
										.setParameter("address", address);
			building=(Building) query.getSingleResult();

		}catch (NoResultException e) {
			logger.info(InBuildingConstants.NO_RECORD_FOUND_LOGGER);
			return null;
		}  catch (Exception e) {
			logger.error(InBuildingConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new DaoException(ExceptionUtils.getMessage(e));
		}
		return building;
	}

	@Override
	public List<Building> tagBuildingGeographyL4() {
		Query query = null;
		try {
			query = getEntityManager()	.createNamedQuery("getBuildingsToTagGeoL4");
			return query.getResultList();
		} catch (Exception e) {
			logger.error(InBuildingConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new DaoException(ExceptionUtils.getMessage(e));
		}
	}
	
	
	@Override
	public Building getBuildingByBuildingId(String buildingId) {
		Query query = null;
		Building building=null;
		try {
			query = getEntityManager()	.createNamedQuery(InBuildingConstants.GET_BUILDING_BY_BUILDING_ID)
										.setParameter("buildingId", buildingId);
			building=(Building) query.getSingleResult();

		}catch (NoResultException e) {
			logger.info(InBuildingConstants.NO_RECORD_FOUND_LOGGER);
			return null;
		}  catch (Exception e) {
			logger.error(InBuildingConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new DaoException(ExceptionUtils.getMessage(e));
		}
		return building;
	}
	
	@Override
	public Building getBuildingByNameAndLatLong(String buildingName,Double latitude, Double longitude) {
		Query query = null;
		Building building=null;
		try {
			query = getEntityManager()	.createNamedQuery(InBuildingConstants.GET_BUILDING_BY_NAME_AND_LATLONG)
										.setParameter("buildingName", buildingName)
										.setParameter("latitude", latitude)
										.setParameter("longitude", longitude);
			building=(Building) query.getSingleResult();

		}catch (NoResultException e) {
			logger.info(InBuildingConstants.NO_RECORD_FOUND_LOGGER);
			return null;
		}  catch (Exception e) {
			logger.error(InBuildingConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new DaoException(ExceptionUtils.getMessage(e));
		}
		return building;
	}

  @Override
  public Integer getGeographyL4Id(String name) {
    try {
      return (Integer) getEntityManager().createNamedQuery("getGeographyL4Id")
          .setParameter("name", name).getSingleResult();
    } catch (NoResultException e) {
      logger.error("No result found for {}", name);
      return null;
    } catch (Exception e) {
      logger.error("Exception inside getGeographyL4Id due to {}", ExceptionUtils.getStackTrace(e));
    }
    return null;
  }

	@Override
	public GeographyDetailWrapper getGeographyDetailByBuildingId(String buildingId) {
		try {
			return  getEntityManager().createNamedQuery("getGeographyDetailByBuildingId", GeographyDetailWrapper.class)
					.setParameter("buildingId", buildingId).getSingleResult();
		} catch (NoResultException e) {
			logger.error("No result found for getGeographyDetailByBuildingId by buildingId:{}", buildingId);
			return null;
		}
	}

	@Override
	public List<BuildingPolygonWrapper> getBuildingPolygonByViewPort(Double swLat, Double swLng, Double neLat,
			Double neLng) {
		return getEntityManager()
				.createNamedQuery("getBuildingPolygonByViewPort", BuildingPolygonWrapper.class)
				.setParameter("swLat", swLat).setParameter("swLng", swLng).setParameter("neLat", neLat)
				.setParameter("neLng", neLng).getResultList();
	}
	
	@Override
	public BuildingWrapper getBuildingDetailByBuildingId(String buildingId) {
		try {
			return getEntityManager().createNamedQuery("getBuildingDetailByBuildingId",BuildingWrapper.class).setParameter("buildingId", buildingId).getSingleResult();
		} catch (NoResultException e) {
			logger.error("No result found for getBuildingDetailByBuildingId by buildingId:{}", buildingId);
			return null;
		}
	}

	@Override
	public BuildingWrapper getBuildingCoverage(String buildingId) {
		try {
			return getEntityManager().createNamedQuery("getBuildingCoverage", BuildingWrapper.class)
					.setParameter("buildingId", buildingId).getSingleResult();
		} catch (NoResultException e) {
			logger.error("No result found for getCoverageDetails by buildingId:{}", buildingId);
			return null;
		}
	}

}

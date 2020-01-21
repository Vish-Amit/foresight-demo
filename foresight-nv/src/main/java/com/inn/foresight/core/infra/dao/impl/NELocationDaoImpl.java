package com.inn.foresight.core.infra.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.BusinessException;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.utils.ExceptionUtil;
import com.inn.core.generic.utils.Utils;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.infra.dao.INELocationDao;
import com.inn.foresight.core.infra.model.NELocation;
import com.inn.foresight.core.infra.model.NELocation.NELStatus;
import com.inn.foresight.core.infra.utils.InfraConstants;
import com.inn.foresight.core.infra.utils.InfraUtils;
import com.inn.foresight.core.infra.utils.enums.NELType;
import com.inn.foresight.core.infra.utils.enums.NEType;
import com.inn.foresight.core.infra.wrapper.NELocationWrapper;
import com.inn.product.um.geography.model.GeographyL1;
import com.inn.product.um.geography.model.GeographyL2;
import com.inn.product.um.geography.model.GeographyL3;
import com.inn.product.um.geography.model.GeographyL4;

/**
 * The Class NELocationDaoImpl.
 */
@Repository("NELocationDaoImpl")
public class NELocationDaoImpl extends HibernateGenericDao<Integer, NELocation> implements INELocationDao {

	/** The logger. */
	private Logger logger = LogManager.getLogger(NELocationDaoImpl.class);

	/**
	 * Instantiates a new NE location dao impl.
	 */
	public NELocationDaoImpl() {
		super(NELocation.class);
	}

	@Override
	public NELocation getNELocationByNEId(String neId) {
		logger.info("inside @getNELocationByNEId method nelocationid :{}", neId);
		try {
			TypedQuery<NELocation> query = getEntityManager().createNamedQuery("checkNELocationByNEId",
					NELocation.class);
			query.setParameter("nelId", neId);
			return query.getSingleResult();
		} catch (NoResultException e) {
			logger.warn("No result found for neId :{}", neId);
			return null;
		} catch (Exception e) {
			logger.error("error while getting NELocation :{}", ExceptionUtils.getStackTrace(e));
			throw new BusinessException(ExceptionUtils.getMessage(e));
		}
	}

	@Override
	public List<NELocation> searchNELocationByNELIdAndNELType(String searchNELId, List<NELType> nelTypeList,
			Integer upperLimit, Integer lowerLimit) {
		List<NELocation> neLocationList = new ArrayList<>();
		try {
			Query query = getEntityManager().createNamedQuery("searchNELocationByNELIdAndNELType")
					.setParameter("nelTypeList", nelTypeList).setParameter("searchNELId", "%" + searchNELId + "%");

			if (ObjectUtils.allNotNull(lowerLimit, upperLimit)) {
				Integer totalPaginationRecord = upperLimit - lowerLimit + 1;
				query.setFirstResult(lowerLimit);
				query.setMaxResults(totalPaginationRecord);
			} else {
				query.setMaxResults(10);
			}
			neLocationList = query.getResultList();
			logger.info("Size of NE Location list is : {}", neLocationList.size());
			return neLocationList;

		} catch (Exception e) {
			logger.error("Error in searchNELocationByNELIdAndNELType : {}", Utils.getStackTrace(e));
		}
		return neLocationList;
	}

	private CriteriaQuery<NELocationWrapper> getNELocationCriteriaBuilderGeographyWise(String geographyLevel,
			CriteriaBuilder builder, Root<NELocation> neLocationRoot, CriteriaQuery<NELocationWrapper> criteriaQuery,
			List<String> distinctGeographyList, String nelType, Map<String, Double> viewMap) {
		try {

			if (geographyLevel.equalsIgnoreCase(InfraConstants.GEOGRAPHYL1)) {

				criteriaQuery
						.select(builder
								.construct(
										NELocationWrapper.class, builder
												.avg(builder
														.<Number>selectCase().when(
																builder.between(
																		neLocationRoot
																				.get(InfraConstants.NE_LATITUDE_KEY),
																		viewMap.get(InfraConstants.SW_LATITUDE),
																		viewMap.get(InfraConstants.NW_LAT)),
																neLocationRoot.get(InfraConstants.NE_LATITUDE_KEY))
														.otherwise(builder.nullLiteral(Number.class)))
												.alias(InfraConstants.NE_LATITUDE_KEY),
										builder.avg(builder.<Number>selectCase().when(
												builder.between(neLocationRoot.get(InfraConstants.NE_LONGITUDE_KEY),
														viewMap.get(InfraConstants.SW_LONGITUDE),
														viewMap.get(InfraConstants.NW_LONG)),
												neLocationRoot.get(InfraConstants.NE_LONGITUDE_KEY))
												.otherwise(builder.nullLiteral(Number.class)))
												.alias(InfraConstants.NE_LONGITUDE_KEY),
										builder.count(neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL4_KEY)
												.get(InfraConstants.NE_GEOGRAPHYL3_KEY)
												.get(InfraConstants.NE_GEOGRAPHYL2_KEY)
												.get(InfraConstants.NE_GEOGRAPHYL1_KEY)
												.get(InfraConstants.BOUNDARY_NAME)),
										neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL4_KEY)
												.get(InfraConstants.NE_GEOGRAPHYL3_KEY)
												.get(InfraConstants.NE_GEOGRAPHYL2_KEY)
												.get(InfraConstants.NE_GEOGRAPHYL1_KEY)
												.get(InfraConstants.BOUNDARY_NAME),
										neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL4_KEY)
												.get(InfraConstants.NE_GEOGRAPHYL3_KEY)
												.get(InfraConstants.NE_GEOGRAPHYL2_KEY)
												.get(InfraConstants.NE_GEOGRAPHYL1_KEY)
												.get(InfraConstants.BOUNDARY_DISPLAY_NAME),
										neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL4_KEY)
												.get(InfraConstants.NE_GEOGRAPHYL3_KEY)
												.get(InfraConstants.NE_GEOGRAPHYL2_KEY)
												.get(InfraConstants.NE_GEOGRAPHYL1_KEY)
												.get(InfraConstants.ID)));
				criteriaQuery.where(
						neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.NE_GEOGRAPHYL3_KEY)
								.get(InfraConstants.NE_GEOGRAPHYL2_KEY).get(InfraConstants.NE_GEOGRAPHYL1_KEY)
								.get(InfraConstants.BOUNDARY_NAME).in(distinctGeographyList),
						builder.and(builder.equal(neLocationRoot.get(InfraConstants.NE_LOCATION_TYPE),
								NELType.valueOf(nelType))));
				criteriaQuery.groupBy(neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL4_KEY)
						.get(InfraConstants.NE_GEOGRAPHYL3_KEY).get(InfraConstants.NE_GEOGRAPHYL2_KEY)
						.get(InfraConstants.NE_GEOGRAPHYL1_KEY).get(InfraConstants.BOUNDARY_NAME));

			} else if (geographyLevel.equalsIgnoreCase(InfraConstants.GEOGRAPHYL2)) {

				criteriaQuery
						.select(builder
								.construct(
										NELocationWrapper.class, builder
												.avg(builder
														.<Number>selectCase().when(
																builder.between(
																		neLocationRoot
																				.get(InfraConstants.NE_LATITUDE_KEY),
																		viewMap.get(InfraConstants.SW_LATITUDE),
																		viewMap.get(InfraConstants.NW_LAT)),
																neLocationRoot.get(InfraConstants.NE_LATITUDE_KEY))
														.otherwise(builder.nullLiteral(Number.class)))
												.alias(InfraConstants.NE_LATITUDE_KEY),
										builder.avg(builder.<Number>selectCase().when(
												builder.between(neLocationRoot.get(InfraConstants.NE_LONGITUDE_KEY),
														viewMap.get(InfraConstants.SW_LONGITUDE),
														viewMap.get(InfraConstants.NW_LONG)),
												neLocationRoot.get(InfraConstants.NE_LONGITUDE_KEY))
												.otherwise(builder.nullLiteral(Number.class)))
												.alias(InfraConstants.NE_LONGITUDE_KEY),
										builder.count(neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL4_KEY)
												.get(InfraConstants.NE_GEOGRAPHYL3_KEY)
												.get(InfraConstants.NE_GEOGRAPHYL2_KEY)
												.get(InfraConstants.BOUNDARY_NAME)),
										neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL4_KEY)
												.get(InfraConstants.NE_GEOGRAPHYL3_KEY)
												.get(InfraConstants.NE_GEOGRAPHYL2_KEY)
												.get(InfraConstants.BOUNDARY_NAME),
										neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL4_KEY)
												.get(InfraConstants.NE_GEOGRAPHYL3_KEY)
												.get(InfraConstants.NE_GEOGRAPHYL2_KEY)
												.get(InfraConstants.BOUNDARY_DISPLAY_NAME),
										neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL4_KEY)
												.get(InfraConstants.NE_GEOGRAPHYL3_KEY)
												.get(InfraConstants.NE_GEOGRAPHYL2_KEY)
												.get(InfraConstants.ID)));

				criteriaQuery.where(
						neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.NE_GEOGRAPHYL3_KEY)
								.get(InfraConstants.NE_GEOGRAPHYL2_KEY).get(InfraConstants.BOUNDARY_NAME)
								.in(distinctGeographyList),
						builder.and(builder.equal(neLocationRoot.get(InfraConstants.NE_LOCATION_TYPE),
								NELType.valueOf(nelType))));
				criteriaQuery.groupBy(
						neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.NE_GEOGRAPHYL3_KEY)
								.get(InfraConstants.NE_GEOGRAPHYL2_KEY).get(InfraConstants.BOUNDARY_NAME));

			} else if (geographyLevel.equalsIgnoreCase(InfraConstants.GEOGRAPHYL3)) {

				criteriaQuery
						.select(builder
								.construct(
										NELocationWrapper.class, builder
												.avg(builder
														.<Number>selectCase().when(
																builder.between(
																		neLocationRoot
																				.get(InfraConstants.NE_LATITUDE_KEY),
																		viewMap.get(InfraConstants.SW_LATITUDE),
																		viewMap.get(InfraConstants.NW_LAT)),
																neLocationRoot.get(InfraConstants.NE_LATITUDE_KEY))
														.otherwise(builder.nullLiteral(Number.class)))
												.alias(InfraConstants.NE_LATITUDE_KEY),
										builder.avg(builder.<Number>selectCase().when(
												builder.between(neLocationRoot.get(InfraConstants.NE_LONGITUDE_KEY),
														viewMap.get(InfraConstants.SW_LONGITUDE),
														viewMap.get(InfraConstants.NW_LONG)),
												neLocationRoot.get(InfraConstants.NE_LONGITUDE_KEY))
												.otherwise(builder.nullLiteral(Number.class)))
												.alias(InfraConstants.NE_LONGITUDE_KEY),
										builder.count(neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL4_KEY)
												.get(InfraConstants.NE_GEOGRAPHYL3_KEY)
												.get(InfraConstants.BOUNDARY_NAME)),
										neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL4_KEY)
												.get(InfraConstants.NE_GEOGRAPHYL3_KEY)
												.get(InfraConstants.BOUNDARY_NAME),
										neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL4_KEY)
												.get(InfraConstants.NE_GEOGRAPHYL3_KEY)
												.get(InfraConstants.BOUNDARY_DISPLAY_NAME),
										neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL4_KEY)
												.get(InfraConstants.NE_GEOGRAPHYL3_KEY)
												.get(InfraConstants.ID)));
				criteriaQuery.where(
						neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.NE_GEOGRAPHYL3_KEY)
								.get(InfraConstants.BOUNDARY_NAME).in(distinctGeographyList),
						builder.and(builder.equal(neLocationRoot.get(InfraConstants.NE_LOCATION_TYPE),
								NELType.valueOf(nelType))));
				criteriaQuery.groupBy(neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL4_KEY)
						.get(InfraConstants.NE_GEOGRAPHYL3_KEY).get(InfraConstants.BOUNDARY_NAME));

			} else if (geographyLevel.equalsIgnoreCase(InfraConstants.GEOGRAPHYL4)) {

				criteriaQuery
						.select(builder
								.construct(
										NELocationWrapper.class, builder
												.avg(builder
														.<Number>selectCase().when(
																builder.between(
																		neLocationRoot
																				.get(InfraConstants.NE_LATITUDE_KEY),
																		viewMap.get(InfraConstants.SW_LATITUDE),
																		viewMap.get(InfraConstants.NW_LAT)),
																neLocationRoot.get(InfraConstants.NE_LATITUDE_KEY))
														.otherwise(builder.nullLiteral(Number.class)))
												.alias(InfraConstants.NE_LATITUDE_KEY),
										builder.avg(builder.<Number>selectCase().when(
												builder.between(neLocationRoot.get(InfraConstants.NE_LONGITUDE_KEY),
														viewMap.get(InfraConstants.SW_LONGITUDE),
														viewMap.get(InfraConstants.NW_LONG)),
												neLocationRoot.get(InfraConstants.NE_LONGITUDE_KEY))
												.otherwise(builder.nullLiteral(Number.class)))
												.alias(InfraConstants.NE_LONGITUDE_KEY),
										builder.count(neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL4_KEY)
												.get(InfraConstants.BOUNDARY_NAME)),
										neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL4_KEY)
												.get(InfraConstants.BOUNDARY_NAME),
										neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL4_KEY)
												.get(InfraConstants.BOUNDARY_DISPLAY_NAME),
										neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL4_KEY)
												.get(InfraConstants.ID)

								));
				criteriaQuery.where(
						neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.BOUNDARY_NAME)
								.in(distinctGeographyList),
						builder.and(builder.equal(neLocationRoot.get(InfraConstants.NE_LOCATION_TYPE),
								NELType.valueOf(nelType))));
				criteriaQuery.groupBy(
						neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.BOUNDARY_NAME));

			}

		} catch (Exception exception) {

			logger.error("Error in getting CriteriaBuilder for InventoryLocation {}", Utils.getStackTrace(exception));
		}
		return criteriaQuery;
	}

	@Override
	public List<NELocationWrapper> getNELocationsDataAndCountGeographyWise(String nelType, Double southWestLong,
			Double southWestLat, Double northEastLong, Double northEastLat, String geographyLevel) {
		List<NELocationWrapper> neLocationWrapperList = new ArrayList<>();
		try {
			List<String> distinctGeographyList = getDistinctGeography(geographyLevel, southWestLong, southWestLat,
					northEastLong, northEastLat);

			if (distinctGeographyList.isEmpty()) {
				return neLocationWrapperList;
			}

			Optional<Map> viewMap = Optional
					.ofNullable(InfraUtils.validateViewPort(southWestLong, southWestLat, northEastLong, northEastLat));

			CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<NELocationWrapper> criteriaQuery = builder.createQuery(NELocationWrapper.class);
			Root<NELocation> neLocationRoot = criteriaQuery.from(NELocation.class);

			CriteriaQuery<NELocationWrapper> criteriaQueryILW = getNELocationCriteriaBuilderGeographyWise(
					geographyLevel, builder, neLocationRoot, criteriaQuery, distinctGeographyList, nelType,
					viewMap.get());

			TypedQuery<NELocationWrapper> createQuery = getEntityManager().createQuery(criteriaQueryILW);
			neLocationWrapperList = createQuery.getResultList();

			logger.info("InventoryLocation geography {} ", neLocationWrapperList.size());
		} catch (NoResultException ex) {
			logger.error("Error while Inventory Location data and count Geography Wise  {}", Utils.getStackTrace(ex));
		} catch (Exception ex) {
			logger.error("Error while getting  distinct geogroahy  {}", Utils.getStackTrace(ex));
			throw new DaoException(ex.getMessage());
		}
		return neLocationWrapperList;
	}

	public List<String> getDistinctGeography(String geograhyLevel, Double southWestLong, Double southWestLat,
			Double northEastLong, Double northEastLat) {
		Map<String, Double> viewportMap = new HashMap<>();
		List<String> geographyList = new ArrayList();
		try {
			CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<String> criteriaQuery = builder.createQuery(String.class);
			Root<NELocation> neLocationRoot = criteriaQuery.from(NELocation.class);
			viewportMap = InfraUtils.getViewPortMap(southWestLong, southWestLat, northEastLong, northEastLat);
			List<Predicate> predicateList = InfraUtils.getPredicatesForViewPortsForNE(builder, neLocationRoot,
					viewportMap);
			criteriaQuery = getGeographyCriteria(geograhyLevel, criteriaQuery, neLocationRoot);
			if (criteriaQuery != null) {
				TypedQuery<String> query = getEntityManager()
						.createQuery(criteriaQuery.where(predicateList.toArray(new Predicate[] {})));
				geographyList = query.getResultList();
			}
		} catch (NoResultException exception) {
			logger.error("Error while getting distinct Geography  {}", Utils.getStackTrace(exception));
		} catch (Exception exception) {
			logger.error("Error while getting  distinct Geography  {}", Utils.getStackTrace(exception));
			throw new DaoException("Unable to get distinct Geography");
		}
		return geographyList;
	}

	@SuppressWarnings("rawtypes")
	private CriteriaQuery getGeographyCriteria(String geograhyLevel, CriteriaQuery criteriaQuery, Root neLocationRoot) {
		if (geograhyLevel.equalsIgnoreCase(InfraConstants.GEOGRAPHYL1_TABLE)) {
			return criteriaQuery.select(neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL4_KEY)
					.get(InfraConstants.NE_GEOGRAPHYL3_KEY).get(InfraConstants.NE_GEOGRAPHYL2_KEY)
					.get(InfraConstants.NE_GEOGRAPHYL1_KEY).get(InfraConstants.BOUNDARY_NAME)).distinct(true);
		} else if (geograhyLevel.equalsIgnoreCase(InfraConstants.GEOGRAPHYL2_TABLE)) {
			return criteriaQuery
					.select(neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.NE_GEOGRAPHYL3_KEY)
							.get(InfraConstants.NE_GEOGRAPHYL2_KEY).get(InfraConstants.BOUNDARY_NAME))
					.distinct(true);
		} else if (geograhyLevel.equalsIgnoreCase(InfraConstants.GEOGRAPHYL3_TABLE)) {
			return criteriaQuery.select(neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL4_KEY)
					.get(InfraConstants.NE_GEOGRAPHYL3_KEY).get(InfraConstants.BOUNDARY_NAME)).distinct(true);
		} else if (geograhyLevel.equalsIgnoreCase(InfraConstants.GEOGRAPHYL4_TABLE)) {
			return criteriaQuery
					.select(neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.BOUNDARY_NAME))
					.distinct(true);
		}
		return null;
	}

	public List<String> getDistinctGeographyListByGeogpraphylevel(String geograhyLevel, Double southWestLong,
			Double southWestLat, Double northEastLong, Double northEastLat) {
		Map<String, Double> viewportMap = null;
		List<String> geographyList = new ArrayList<>();
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		Root neLocationRoot = null;
		CriteriaQuery criteriaQuery = null;
		viewportMap = InfraUtils.getViewPortMap(southWestLong, southWestLat, northEastLong, northEastLat);

		if (geograhyLevel.equalsIgnoreCase(InfraConstants.GEOGRAPHYL1_TABLE)) {
			criteriaQuery = builder.createQuery(GeographyL1.class);
			neLocationRoot = criteriaQuery.from(GeographyL1.class);

		} else if (geograhyLevel.equalsIgnoreCase(InfraConstants.GEOGRAPHYL2_TABLE)) {
			criteriaQuery = builder.createQuery(GeographyL2.class);
			neLocationRoot = criteriaQuery.from(GeographyL2.class);

		} else if (geograhyLevel.equalsIgnoreCase(InfraConstants.GEOGRAPHYL3_TABLE)) {
			criteriaQuery = builder.createQuery(GeographyL3.class);
			neLocationRoot = criteriaQuery.from(GeographyL3.class);

		} else if (geograhyLevel.equalsIgnoreCase(InfraConstants.GEOGRAPHYL4_TABLE)) {
			criteriaQuery = builder.createQuery(GeographyL4.class);
			neLocationRoot = criteriaQuery.from(GeographyL4.class);

		}
		try {
			List<Predicate> predicateList = InfraUtils.getPredicatesForViewPortsForNE(builder, neLocationRoot,
					viewportMap);
			criteriaQuery.select(neLocationRoot.get(InfraConstants.BOUNDARY_NAME)).distinct(true);
			Query query = getEntityManager()
					.createQuery(criteriaQuery.where(predicateList.toArray(new Predicate[] {})));
			geographyList = query.getResultList();

		} catch (NoResultException ex) {
			logger.error("Error while getting distinct geography  {}", Utils.getStackTrace(ex));
		} catch (Exception ex) {
			logger.error("Error while getting  distinct geography  {}", Utils.getStackTrace(ex));
			throw new DaoException(ex.getMessage());
		}
		return geographyList;
	}

	@Override
	public List<NELocationWrapper> getNELocationDataByNELType(Double southWestLong, Double southWestLat,
			Double northEastLong, Double northEastLat, String nelType) {
		List<NELocationWrapper> ilDataList = new ArrayList<>();
		logger.info(
				"going to get Combined Sites Data For GeographyL2 for nelType={} , southWestLong={}, southWestLat={},northEastLong={} ,northEastLat={} ",
				nelType, southWestLong, southWestLat, northEastLong, northEastLat);
		try {
			TypedQuery<NELocationWrapper> query = getEntityManager().createNamedQuery("getNELocationDataList",
					NELocationWrapper.class);
			query.setParameter(ForesightConstants.SOUTH_WEST_LONGITUDE, southWestLong);
			query.setParameter(ForesightConstants.SOUTH_WEST_LATITUDE, southWestLat);
			query.setParameter(ForesightConstants.NORTH_EAST_LONGITUDE, northEastLong);
			query.setParameter(ForesightConstants.NORTH_EAST_LATITUDE, northEastLat);
			query.setParameter(InfraConstants.NE_LOCATION_TYPE, NELType.valueOf(nelType));

			ilDataList = query.getResultList();

		} catch (NoResultException e) {
			logger.error("error in getNELocationDataByLocationType, err = {}", Utils.getStackTrace(e));
		} catch (Exception e) {
			logger.error("error in getNELocationDataByLocationType, err = {}", Utils.getStackTrace(e));
			throw new DaoException(e.getMessage());
		}
		return ilDataList;
	}

	@Override
	public List<NELocationWrapper> getNELocationDataAndCount(String geograhyLevel, String nelType, Double southWestLong,
			Double southWestLat, Double northEastLong, Double northEastLat) {
		Map<String, Double> viewportMap = new HashMap<>();
		List<NELocationWrapper> neLocationWrapperList = new ArrayList<>();
		try {
			CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<NELocationWrapper> criteriaQuery = builder.createQuery(NELocationWrapper.class);
			Root<NELocation> neLocationRoot = criteriaQuery.from(NELocation.class);
			viewportMap = InfraUtils.getViewPortMap(southWestLong, southWestLat, northEastLong, northEastLat);

			List<Predicate> predicateList = InfraUtils.getPredicatesForViewPortsForNE(builder, neLocationRoot,
					viewportMap);
			predicateList.add(builder
					.and(builder.equal(neLocationRoot.get(InfraConstants.NE_LOCATION_TYPE), NELType.valueOf(nelType))));

			CriteriaQuery<NELocationWrapper> criteriaQueryILW = getNELocationDataAndCountCriteriaBuilder(geograhyLevel,
					builder, neLocationRoot, criteriaQuery);

			TypedQuery<NELocationWrapper> createQuery = getEntityManager()
					.createQuery(criteriaQueryILW.where(predicateList.toArray(new Predicate[] {})));
			neLocationWrapperList = createQuery.getResultList();

			logger.info("neLocationWrapperList {}  ", neLocationWrapperList.size());

		} catch (NoResultException ex) {
			logger.error("Error while getting NELocation data on view port {}", Utils.getStackTrace(ex));
		} catch (Exception ex) {
			logger.error("Error while getting NELocation data on view port {}", Utils.getStackTrace(ex));
			throw new DaoException(ex.getMessage());
		}
		return neLocationWrapperList;
	}

	private CriteriaQuery<NELocationWrapper> getNELocationDataAndCountCriteriaBuilder(String geographyLevel,
			CriteriaBuilder builder, Root<NELocation> neLocationRoot, CriteriaQuery<NELocationWrapper> criteriaQuery) {
		try {
			if (geographyLevel.equalsIgnoreCase(InfraConstants.GEOGRAPHYL1)) {

				criteriaQuery.select(builder.construct(NELocationWrapper.class,
						neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.NE_GEOGRAPHYL3_KEY)
								.get(InfraConstants.NE_GEOGRAPHYL2_KEY).get(InfraConstants.NE_GEOGRAPHYL1_KEY)
								.get(InfraConstants.NE_LATITUDE_KEY),
						neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.NE_GEOGRAPHYL3_KEY)
								.get(InfraConstants.NE_GEOGRAPHYL2_KEY).get(InfraConstants.NE_GEOGRAPHYL1_KEY)
								.get(InfraConstants.NE_LONGITUDE_KEY),
						builder.count(neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL4_KEY)
								.get(InfraConstants.NE_GEOGRAPHYL3_KEY).get(InfraConstants.NE_GEOGRAPHYL2_KEY)
								.get(InfraConstants.NE_GEOGRAPHYL1_KEY).get(InfraConstants.BOUNDARY_NAME)),
						neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.NE_GEOGRAPHYL3_KEY)
								.get(InfraConstants.NE_GEOGRAPHYL2_KEY).get(InfraConstants.NE_GEOGRAPHYL1_KEY)
								.get(InfraConstants.BOUNDARY_NAME)));
				criteriaQuery.groupBy(neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL4_KEY)
						.get(InfraConstants.NE_GEOGRAPHYL3_KEY).get(InfraConstants.NE_GEOGRAPHYL2_KEY)
						.get(InfraConstants.NE_GEOGRAPHYL1_KEY).get(InfraConstants.BOUNDARY_NAME));

			} else if (geographyLevel.equalsIgnoreCase(InfraConstants.GEOGRAPHYL2)) {

				criteriaQuery.select(builder.construct(NELocationWrapper.class,
						neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.NE_GEOGRAPHYL3_KEY)
								.get(InfraConstants.NE_GEOGRAPHYL2_KEY).get(InfraConstants.NE_LATITUDE_KEY),
						neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.NE_GEOGRAPHYL3_KEY)
								.get(InfraConstants.NE_GEOGRAPHYL2_KEY).get(InfraConstants.NE_LONGITUDE_KEY),
						builder.count(neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL4_KEY)
								.get(InfraConstants.NE_GEOGRAPHYL3_KEY).get(InfraConstants.NE_GEOGRAPHYL2_KEY)
								.get(InfraConstants.BOUNDARY_NAME)),
						neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.NE_GEOGRAPHYL3_KEY)
								.get(InfraConstants.NE_GEOGRAPHYL2_KEY).get(InfraConstants.BOUNDARY_NAME)));
				criteriaQuery.groupBy(
						neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.NE_GEOGRAPHYL3_KEY)
								.get(InfraConstants.NE_GEOGRAPHYL2_KEY).get(InfraConstants.BOUNDARY_NAME));
			} else if (geographyLevel.equalsIgnoreCase(InfraConstants.GEOGRAPHYL3)) {

				criteriaQuery.select(builder.construct(NELocationWrapper.class,
						neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.NE_GEOGRAPHYL3_KEY)
								.get(InfraConstants.NE_LATITUDE_KEY),
						neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.NE_GEOGRAPHYL3_KEY)
								.get(InfraConstants.NE_LONGITUDE_KEY),
						builder.count(neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL4_KEY)
								.get(InfraConstants.NE_GEOGRAPHYL3_KEY).get(InfraConstants.BOUNDARY_NAME)),
						neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.NE_GEOGRAPHYL3_KEY)
								.get(InfraConstants.BOUNDARY_NAME)));
				criteriaQuery.groupBy(neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL4_KEY)
						.get(InfraConstants.NE_GEOGRAPHYL3_KEY).get(InfraConstants.BOUNDARY_NAME));
			} else if (geographyLevel.equalsIgnoreCase(InfraConstants.GEOGRAPHYL4)) {

				criteriaQuery.select(builder.construct(NELocationWrapper.class,
						neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.NE_LATITUDE_KEY),
						neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.NE_LONGITUDE_KEY),
						builder.count(neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL4_KEY)
								.get(InfraConstants.BOUNDARY_NAME)),
						neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.BOUNDARY_NAME)

				));
				criteriaQuery.groupBy(
						neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.BOUNDARY_NAME));
			}

		} catch (Exception exception) {

			logger.error("Error in getting criteria builder for NELocation availble on view port {}",
					Utils.getStackTrace(exception));
		}
		return criteriaQuery;
	}

	@Override
	public List<Object[]> getNELocationListByNELType(String nelType) {
		List<Object[]> resultList = new ArrayList<>();
		try {
			TypedQuery<Object[]> ilswQuery1 = getEntityManager().createNamedQuery("getNELocationListByLocationType",
					Object[].class);
			ilswQuery1.setParameter(InfraConstants.NE_LOCATION_TYPE, NELType.valueOf(nelType));
			resultList = ilswQuery1.getResultList();
		} catch (NoResultException e) {
			logger.error("error in getNELocationListByLocationType, NoResultException : {}", Utils.getStackTrace(e));
		} catch (Exception e) {
			logger.error("error in getNELocationListByLocationType, err : {}", Utils.getStackTrace(e));
			throw new DaoException(e.getMessage());
		}

		return resultList;

	}

	@Override
	public List<Map<String, Object>> getSearchNELocationName(String locationName, List<String> nelTypeStr) {
		logger.error("going to get Search locationName = {} and locationType ={}", locationName, nelTypeStr);
		List<Object[]> resultList = new ArrayList<>();
		List<Map<String, Object>> locationNameList = new ArrayList<>();
		try {
			List<NELType> nelTypeList = getNelType(nelTypeStr);
			TypedQuery<Object[]> query = getEntityManager().createNamedQuery("getSearchNELocationName", Object[].class);
			query.setParameter(InfraConstants.NE_LOCATION_TYPE, nelTypeList);
			query.setParameter(ForesightConstants.NAME, locationName.toUpperCase());
			query.setMaxResults(10);
			resultList = query.getResultList();
			for (Object[] ilname : resultList) {
				Map<String, Object> locationNameMap = new HashMap<>();
				locationNameMap.put(ForesightConstants.ID, ilname[0]);
				locationNameMap.put("locationName", ilname[1]);
				locationNameList.add(locationNameMap);
			}
		} catch (NoResultException e) {
			logger.error("error in getSearchNELocationName, NoResultException = {}", Utils.getStackTrace(e));
		} catch (Exception e) {
			logger.error("error in getSearchNELocationName, err = {}", Utils.getStackTrace(e));
			throw new DaoException(e.getMessage());
		}

		return locationNameList;

	}

	private List<NELType> getNelType(List<String> nelType) {
		List<NELType> nelTypeList=new ArrayList<NELType>();
		for(String nel:nelType) {
			nelTypeList.add(NELType.valueOf(nel));
		}
		return nelTypeList;
	}

	@Override
	public NELocation getNELocationDetailsByName(String name) {
		NELocation inventoryDetails = null;
		try {
			Query query = getEntityManager().createNamedQuery("getNELocationDetailsByName");
			query.setParameter(ForesightConstants.NAME, name);
			inventoryDetails = (NELocation) query.getSingleResult();
		} catch (NoResultException e) {
			logger.error("error in getNELocationDetailsByName, NoResultException = {}", Utils.getStackTrace(e));
		} catch (Exception e) {
			logger.error("error in getNELocationDetailsByName, err = {}", Utils.getStackTrace(e));
			throw new DaoException(ExceptionUtil.generateExceptionCode("Dao", "InventoryLocation", e));
		}
		return inventoryDetails;
	}

	@Override
	public List<NELocation> getCDCList(String type) {
		logger.info("inside getCDCList method :having type :{}", type);
		List<NELocation> neLocationList = null;
		TypedQuery<NELocation> query;
		try {
			NELType neltype = NELType.valueOf(type);
			CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<NELocation> cbQuery = cb.createQuery(NELocation.class);
			Root<NELocation> root = cbQuery.from(NELocation.class);
			Predicate predicate = cb.equal(root.get(InfraConstants.NE_LOCATION_TYPE), neltype);
			cbQuery.where(predicate);
			query = getEntityManager().createQuery(cbQuery);
			neLocationList = query.getResultList();
		} catch (NoResultException nre) {
			logger.error("error in getCDCList, NoResultException = {}", Utils.getStackTrace(nre));
		} catch (Exception e) {
			logger.error("Error occured while getting CDC List :{}", e.getMessage());
			throw new DaoException("Error occured while getting CDC List");
		}
		return neLocationList;
	}

	@Override
	public NELocation getNELocationDetailsByNameAndType(String name, NELType nelType) {
		NELocation inventoryDetails = null;
		try {
			Query query = getEntityManager().createNamedQuery("getNELocationDetailsByNameAndType");
			query.setParameter(ForesightConstants.NAME, name);
			query.setParameter(ForesightConstants.TYPE, nelType);
			inventoryDetails = (NELocation) query.getSingleResult();
		} catch (NoResultException e) {
			logger.error("error in getNELocationDetailsByNameAndType, NoResultException = {}", Utils.getStackTrace(e));
		} catch (Exception e) {
			logger.error("error in getNELocationDetailsByNameAndType, err = {}", Utils.getStackTrace(e));
			throw new DaoException(ExceptionUtil.generateExceptionCode("Dao", "InventoryLocation", e));
		}
		return inventoryDetails;
	}

	@Override
	public Long checkUniqueLocationCode(String locationCode, Integer nelPk) {
		Long count = 0L;
		try {

			CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
			Root<NELocation> neLocationRoot = criteriaQuery.from(NELocation.class);

			criteriaQuery.select(builder.count(neLocationRoot.get("id")));

			List<Predicate> predicateList = new ArrayList<>();

			if (locationCode != null) {
				predicateList.add(builder.equal(neLocationRoot.<String>get("locationCode"), locationCode));
			}
			if (nelPk != null) {
				predicateList.add(builder.notEqual(neLocationRoot.<String>get("id"), nelPk));
			}

			TypedQuery<Long> query = getEntityManager()
					.createQuery(criteriaQuery.where(predicateList.toArray(new Predicate[] {})));
			count = query.getSingleResult();

		} catch (NoResultException ex) {
			logger.error("Error occured due to NoResultException {}", Utils.getStackTrace(ex));
		} catch (Exception ex) {
			logger.error("Error occured due to Exception {}", Utils.getStackTrace(ex));
			throw new DaoException(ex.getMessage());

		}
		return count;
	}

	@Override
	public Long checkUniqueLocationName(String nelId, Integer nelPk) {
		Long count = 0L;
		try {
			CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
			Root<NELocation> neLocationRoot = criteriaQuery.from(NELocation.class);

			criteriaQuery.select(builder.count(neLocationRoot.get("id")));

			List<Predicate> predicateList = new ArrayList<>();

			if (nelId != null) {
				predicateList.add(builder.equal(neLocationRoot.<String>get("nelId"), nelId));
			}
			if (nelPk != null) {
				predicateList.add(builder.notEqual(neLocationRoot.<String>get("id"), nelPk));
			}

			TypedQuery<Long> query = getEntityManager()
					.createQuery(criteriaQuery.where(predicateList.toArray(new Predicate[] {})));
			count = query.getSingleResult();
		} catch (NoResultException ex) {
			logger.error("Error occured due to NoResultException {}", Utils.getStackTrace(ex));
		} catch (Exception ex) {
			logger.error("Error occured due to Exception {}", Utils.getStackTrace(ex));
			throw new DaoException(ex.getMessage());

		}
		return count;
	}

	@Override
	public List<NELocation> getNeLocationByNelidListAndType(List<String> nelIdList, NELType nelType) {
		List<NELocation> siteGCMappingList;
		try {
			TypedQuery<NELocation> query = getEntityManager().createNamedQuery("getNELocationByNEType",
					NELocation.class);
			query.setParameter("nelIdList", nelIdList);
			query.setParameter("neType", nelType);
			siteGCMappingList = query.getResultList();
			logger.info("siteGCMappingList size {}", siteGCMappingList.size());
		} catch (Exception e) {
			logger.error("Exception occured :{} while getting NELocation:{} Exception {} ", Utils.getStackTrace(e));
			throw new DaoException("Error Occurred while getting NELocation");
		}
		return siteGCMappingList;
	}

	@Override
	public List<NELocation> getNeLocationByLocationCodeListAndType(List<String> locationCodeList, NELType nelType) {
		List<NELocation> siteGCMappingList;
		try {
			TypedQuery<NELocation> query = getEntityManager().createNamedQuery("getNeLocationByLocationCodeListAndType",
					NELocation.class);
			query.setParameter("locationCodeList", locationCodeList);
			query.setParameter("neType", nelType);
			siteGCMappingList = query.getResultList();
			logger.info("siteGCMappingList size {}", siteGCMappingList.size());
		} catch (Exception e) {
			logger.error("Exception occured :{} while getting NELocation:{} Exception {} ", Utils.getStackTrace(e));
			throw new DaoException("Error Occurred while getting NELocation");
		}
		return siteGCMappingList;
	}

	@Override
	public List<Object[]> getNELocationDetailByNELType(String nelType) {
		List<Object[]> resultList = new ArrayList<>();
		try {
			TypedQuery<Object[]> ilswQuery1 = getEntityManager().createNamedQuery("getNELocationDetailByNELType",
					Object[].class);
			ilswQuery1.setParameter(InfraConstants.NE_LOCATION_TYPE, NELType.valueOf(nelType));
			resultList = ilswQuery1.getResultList();
		} catch (NoResultException e) {
			logger.error("error in getNELocationListByLocationType, NoResultException : {}", Utils.getStackTrace(e));
		} catch (Exception e) {
			logger.error("error in getNELocationListByLocationType, err : {}", Utils.getStackTrace(e));
			throw new DaoException(e.getMessage());
		}

		return resultList;
	}
	
	@Override
	public List<Object[]> getNeLocationAndParamterByType(String nelType,Integer llimit,Integer ulimit) {
		logger.info("inside @getNeLocationAndParamterByType @Dao method");
		List<Object[]> resultList = new ArrayList<>();
		try {
			TypedQuery<Object[]> query = getEntityManager().createNamedQuery("getNeLocationAndParamterByType",
					Object[].class);
			query.setParameter(InfraConstants.NE_LOCATION_TYPE, NELType.valueOf(nelType));
			enableLimit(query,llimit,ulimit);
			resultList = query.getResultList();
			return resultList;
		} catch (PersistenceException ex) {
			logger.error("Exception Occured While getting Nelocation and Parameter by Type:{}", ex.getMessage());
		}
		return Collections.emptyList();
	}
	
	public void enableLimit(Query query, Integer llimit, Integer ulimit) {
		if (ulimit != null && llimit != null && ulimit >= 0 && llimit < ulimit) {
			query.setFirstResult(llimit);
			query.setMaxResults(ulimit - llimit + 1);
		}
	}
	
	@Override
	public List<NELocation> filterNELocationByNameAndStatus(String gcName, String nelStatus, String locationCode,
			Integer ulimit, Integer llimit, String orderBy, String orderType, String creationTime,
			String modificationTime,Boolean likeFlag) {
		logger.info("inside @filterNELocationByNameAndStatus method");
		try {
			CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<NELocation> criteriaQuery = builder.createQuery(NELocation.class);
			Root<NELocation> neLocation = criteriaQuery.from(NELocation.class);
			List<Predicate> predicateList = new ArrayList<>();
			predicateList.add(builder.equal(neLocation.get("isDeleted"), false));
			predicateList.add(builder.equal(neLocation.get("nelType"), NELType.valueOf("GC")));
			predicateList.add(neLocation.get("nelStatus").isNotNull());
			if (com.inn.foresight.core.generic.utils.Utils.checkForValueInString(gcName)) {
				if (likeFlag) {
					predicateList.add(builder.like(neLocation.get("nelId"), "%" + gcName + "%"));
				} else {
					predicateList.add(builder.like(neLocation.get("nelId"), gcName));
				}
			}
			if (com.inn.foresight.core.generic.utils.Utils.checkForValueInString(nelStatus)) {
				predicateList.add(builder.equal(neLocation.get("nelStatus"), NELStatus.valueOf(nelStatus)));
			}
			logger.info("predicate List Size is :{}", predicateList.size());
			criteriaQuery.orderBy(addOrderByInNELocationQuery(builder, criteriaQuery, orderBy, orderType, neLocation,
					creationTime, modificationTime));
			criteriaQuery.where(predicateList.toArray(new Predicate[] {}));
			TypedQuery<NELocation> createQuery = getEntityManager().createQuery(criteriaQuery);
			enableLimit(llimit, ulimit, createQuery);
			return createQuery.getResultList();
		} catch (NoResultException noResultException) {
			logger.error("NO record Found for  Name :{} and nelstatus :{} Exception is :{}", gcName, nelStatus,
					noResultException.getMessage());
		} catch (Exception exception) {
			logger.error("Exception Occured while filtering NELocation by Name :{} and nelstatus :{} Exception is :{}",
					gcName, nelStatus, ExceptionUtils.getStackTrace(exception));
			throw new DaoException("Exception Occured while filtering NELocation");
		}
		return Collections.emptyList();
	}

	private Order addOrderByInNELocationQuery(CriteriaBuilder builder, CriteriaQuery<NELocation> query, String orderBy,
			String orderType, Root<NELocation> neLocationRoot, String creationTime, String modificationTime) {
		logger.info("going to get order by clause for orderBy: {} and orderType: {}", orderBy, orderType);
		Path<Object> path = null;
		if (orderBy != null && orderType != null) {
			if (orderBy.equalsIgnoreCase("status")) {
				path = neLocationRoot.get("nelStatus");
			}
			if (orderBy.equalsIgnoreCase("gcCode")) {
				path = neLocationRoot.get("locationCode");
			}
			if (orderBy.equalsIgnoreCase("gcName")) {
				path = neLocationRoot.get("nelId");
			}
			if (orderBy.equalsIgnoreCase("creationTime")) {
				path = neLocationRoot.get("createdTime");
			}
			if (orderBy.equalsIgnoreCase("modificationTime")) {
				path = neLocationRoot.get("modifiedTime");
			}
			if (path != null) {
				if (orderType.equalsIgnoreCase("asc")) {
					return builder.asc(path);
				}
				if (orderType.equalsIgnoreCase("desc")) {
					return builder.desc(path);
				}
			}
		}
		return builder.desc(neLocationRoot.get("modifiedTime"));
	}

	private void enableLimit(Integer llimit, Integer ulimit, TypedQuery<NELocation> query) {
		if (llimit != null && ulimit != null && llimit >= 0 && llimit < ulimit) {
			query.setFirstResult(llimit);
			query.setMaxResults((ulimit - llimit) + 1);
		}
	}

	@Override
	public Long getGCOnAirCount(String nelType, String nelStatus) {
		Long count = 0L;
		try {
			CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
			Root<NELocation> neLocationRoot = criteriaQuery.from(NELocation.class);

			criteriaQuery.select(builder.count(neLocationRoot.get("id")));

			List<Predicate> predicateList = new ArrayList<>();

			if (nelType != null) {
				predicateList.add(builder.equal(neLocationRoot.<String>get("nelType"), NELType.valueOf(nelType)));
			}
			if (nelStatus != null) {
				predicateList.add(builder.equal(neLocationRoot.<String>get("nelStatus"), NELStatus.valueOf(nelStatus)));
			}

			TypedQuery<Long> query = getEntityManager()
					.createQuery(criteriaQuery.where(predicateList.toArray(new Predicate[] {})));
			count = query.getSingleResult();
		} catch (NoResultException ex) {
			logger.error("Error occured due to NoResultException {}", Utils.getStackTrace(ex));
		} catch (Exception ex) {
			logger.error("Error occured due to Exception {}", Utils.getStackTrace(ex));
			throw new DaoException(ex.getMessage());
		}
		return count;
	}

	@Override
	public Long getTotalNelocationCountByType(String neltype) {
		logger.info("Going to get nelocation count by  neltype :{}", neltype);
		Long total = 0L;
		try {
			Query query = getEntityManager().createNamedQuery("getTotalNelocationCountByType");
			query.setParameter(InfraConstants.NE_LOCATION_TYPE, NELType.valueOf(neltype));
			total = (Long) query.getSingleResult();
			return total;
		} catch (PersistenceException e) {
			logger.error("Exception occured while getting total nelocation count for neltype:{}", neltype);
		}
		return total;
	}
	
	@Override
	public NELocation getNELocationByLocationCode(String locationCode) {
		logger.info("inside @getNELocationByLocationCode method nelocationid :{}", locationCode);
		try {
			TypedQuery<NELocation> query = getEntityManager().createNamedQuery("getNELocationByLocationCode",
					NELocation.class);
			query.setParameter("locationCode", locationCode);
			return query.getSingleResult();
		} catch (NoResultException e) {
			logger.warn("No result found for locationCode :{}", locationCode);
			return null;
		} catch (Exception e) {
			logger.error("error while getting NELocation :{}", ExceptionUtils.getStackTrace(e));
			throw new BusinessException(ExceptionUtils.getMessage(e));
		}
	}
	
	@Override
	public List<NELocationWrapper> getGCCountByStatusAndGeography( List<String> nelType, List<String> nelStatus) {
		List<NELocationWrapper> neLocationList;
		try {
			List<NELStatus> nelStatusList = nelStatus.stream().map(NELStatus::valueOf).collect(Collectors.toList());
			List<NELType> nelTypeList = nelType.stream().map(NELType::valueOf).collect(Collectors.toList());
			TypedQuery<NELocationWrapper> query = getEntityManager().createNamedQuery("getGCCountByStatusAndGeography",
					NELocationWrapper.class);
			query.setParameter("nelType", nelTypeList);
			query.setParameter("nelStatus", nelStatusList);
			neLocationList = query.getResultList();
			logger.info("neLocationList size {}", neLocationList.size());
		} catch (Exception e) {
			logger.error("Exception occured :{} while getting NELocation:{} Exception {} ", Utils.getStackTrace(e));
			throw new DaoException("Error Occurred while getting GCCount By Status and Geography");
		}
		return neLocationList;
	}
	
	@Override
	public List<NELocationWrapper> getGCDetailsByStatusAndGeography(List<String> nelType, List<String> nelStatus,String vcuNEType, String vduNEType, String macroNEType, String geographyName, Integer llimit, Integer ulimit) {
		List<NELocationWrapper> neLocationList;
		try {
			List<NELStatus> nelStatusList = nelStatus.stream().map(NELStatus::valueOf).collect(Collectors.toList());
			List<NELType> nelTypeList = nelType.stream().map(NELType::valueOf).collect(Collectors.toList());
			TypedQuery<NELocationWrapper> query = getEntityManager().createNamedQuery("getGCDetailsByStatusAndGeography",
					NELocationWrapper.class);
			query.setParameter("nelType", nelTypeList);
			query.setParameter("nelStatus", nelStatusList);
			query.setParameter("vcuNEType", NEType.valueOf(vcuNEType));
			query.setParameter("vduNEType", NEType.valueOf(vduNEType));
			query.setParameter("macroNEType", NEType.valueOf(macroNEType));
			query.setParameter("geographyName", geographyName);
			if (ulimit != null && llimit != null) {
	          if (ulimit >= 0) {
	            query.setMaxResults(ulimit - llimit + 1);
	          }
	          if (llimit >= 0) {
	            query.setFirstResult(llimit);
	          }
	        }
			neLocationList = query.getResultList();
			logger.info("neLocation detail list size {}", neLocationList.size());
		} catch (Exception e) {
			logger.error("Exception occured :{} while getting NELocation:{} Exception {} ", Utils.getStackTrace(e));
			throw new DaoException("Error Occurred while getting GCCount By Status and Geography");
		}
		return neLocationList;
	}

	@Override
	public List<String> filterByLocationCode(String locationcode) {
		logger.info("Going to get locationcode by  locationcode :{}", locationcode);
		List<String> finalFilter = new ArrayList<>();
		try {
			Query query = getEntityManager().createNamedQuery("filterByLocationCode").setParameter("locationcode",
					locationcode);
			finalFilter = (List<String>) query.setMaxResults(10).getResultList();
		} catch (Exception e) {
			logger.error("Exception caught while getting  list : {}", ExceptionUtils.getStackTrace(e));
		}
		return finalFilter;
	}
	
	@Override
	public List<NELocationWrapper> getNELocationByGeographyId(Integer geoId, String geoType, NELType nelType) {
		logger.info("Going to get location  geographyType :{}, geo Id:{},nelType :{}", geoType, geoId, nelType);
		List<NELocationWrapper> neLocationWrapperList = new ArrayList<>();
		try {
			CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<NELocationWrapper> criteriaQuery = builder.createQuery(NELocationWrapper.class);
			Root<NELocation> neLocationRoot = criteriaQuery.from(NELocation.class);
			Join<NELocation, GeographyL3> l4Join = neLocationRoot.join("geographyL3", JoinType.LEFT);
			List<Predicate> predicates = new ArrayList<>();

			criteriaQuery.select(
					builder.construct(NELocationWrapper.class, neLocationRoot.get(InfraConstants.NE_LATITUDE_KEY),
							neLocationRoot.get(InfraConstants.NE_LONGITUDE_KEY), neLocationRoot.get("nelId"),
							l4Join.get(InfraConstants.BOUNDARY_NAME), l4Join.get(InfraConstants.BOUNDARY_DISPLAY_NAME),
							neLocationRoot.get("address"), neLocationRoot.get("nelType"),
							neLocationRoot.get(InfraConstants.ID), neLocationRoot.get("friendlyName"),
							neLocationRoot.get("nelStatus"), neLocationRoot.get("locationCode")));

			if (geoId != null && !InfraConstants.GEOGRAPHYL0.equalsIgnoreCase(geoType)) {
				predicates
						.add(builder.and(builder.equal(neLocationRoot.get(geoType).get(ForesightConstants.ID), geoId)));
			}
			
			predicates.add(builder.and(builder.equal(neLocationRoot.get(ForesightConstants.NEL_TYPE), nelType)));
			predicates.add(builder.and(builder.equal(neLocationRoot.get(ForesightConstants.IS_DELETED), false)));

			TypedQuery<NELocationWrapper> createQuery = getEntityManager()
					.createQuery(criteriaQuery.where(predicates.toArray(new Predicate[] {})));
			neLocationWrapperList = createQuery.getResultList();

			logger.info("neLocationWrapperList {}  ", neLocationWrapperList.size());
		} catch (NoResultException ex) {
			logger.error("Error while getting NELocation data on for geography {},stacktrace:{}",
					Utils.getStackTrace(ex));
		} catch (Exception ex) {
			logger.error("Error while getting NELocation data on for geography:{},stacktrace: {}",
					Utils.getStackTrace(ex));
			throw new DaoException(ex.getMessage());
		}
		return neLocationWrapperList;
	}
	
	
	@Override
	public List<NELocationWrapper> getGeographyWiseNELocationCountByGeographyId(String nelType, Integer geoId, String geoType) {
		logger.info("Going to get group location count by  geographyType :{}, geo Id:{}", geoType,geoId);
		List<NELocationWrapper> neLocationWrapperList = new ArrayList<>();
		try {
			CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<NELocationWrapper> criteriaQuery = builder.createQuery(NELocationWrapper.class);
			Root<NELocation> neLocationRoot = criteriaQuery.from(NELocation.class);
			
			List<Predicate> predicates = new ArrayList<>();
			predicates.add(builder
					.and(builder.equal(neLocationRoot.get(InfraConstants.NE_LOCATION_TYPE), NELType.valueOf(nelType))));
			predicates.add(builder
					.and(builder.equal(neLocationRoot.get(InfraConstants.ISDELETED_STATUS), false)));
	
			getGeographyWiseNELocationCountByGeographyId(builder, neLocationRoot, criteriaQuery, geoType, geoId, predicates);

			TypedQuery<NELocationWrapper> createQuery = getEntityManager()
					.createQuery(criteriaQuery.where(predicates.toArray(new Predicate[] {})));
			neLocationWrapperList = createQuery.getResultList();

			logger.info("neLocationWrapperList {}  ", neLocationWrapperList.size());

		} catch (NoResultException ex) {
			logger.error("Error while getting geography group count data by  geographyType :{}, geo Id:{}, stacktrace: {}", Utils.getStackTrace(ex));
		} catch (Exception ex) {
			logger.error("Error while getting geography group count data by  geographyType :{}, geo Id:{}, stacktrace:{}", Utils.getStackTrace(ex));
			throw new DaoException(ex.getMessage());
		}
		return neLocationWrapperList;
	}

	private void getGeographyWiseNELocationCountByGeographyId(CriteriaBuilder builder,
			Root<NELocation> neLocationRoot, CriteriaQuery<NELocationWrapper> criteriaQuery, String geoType,Integer geoId,List<Predicate> predicates) {
		if (InfraConstants.GEOGRAPHYL0.equalsIgnoreCase(geoType)) {

			criteriaQuery.select(builder.construct(NELocationWrapper.class,
					neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL1_KEY).get(InfraConstants.NE_LATITUDE_KEY),
					neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL1_KEY).get(InfraConstants.NE_LONGITUDE_KEY),
					builder.count(neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL1_KEY).get(InfraConstants.BOUNDARY_NAME)),
					neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL1_KEY).get(InfraConstants.BOUNDARY_NAME),
					neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL1_KEY).get(InfraConstants.BOUNDARY_DISPLAY_NAME),
					neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL1_KEY).get(InfraConstants.ID)));
			criteriaQuery.groupBy(neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL1_KEY).get(InfraConstants.BOUNDARY_NAME));

		} else if (InfraConstants.GEOGRAPHYL1.equalsIgnoreCase(geoType)) {
			predicates.add(builder
					.and(builder.equal(neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL1_KEY).get(InfraConstants.ID), geoId)));

			criteriaQuery.select(builder.construct(NELocationWrapper.class,
					neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL2_KEY).get(InfraConstants.NE_LATITUDE_KEY),
					neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL2_KEY).get(InfraConstants.NE_LONGITUDE_KEY),
					builder.count(neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL2_KEY).get(InfraConstants.BOUNDARY_NAME)),
					neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL2_KEY).get(InfraConstants.BOUNDARY_NAME),
					neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL2_KEY).get(InfraConstants.BOUNDARY_DISPLAY_NAME),
				neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL2_KEY).get(InfraConstants.ID)));
			criteriaQuery.groupBy(neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL2_KEY).get(InfraConstants.BOUNDARY_NAME));

		} else if (InfraConstants.GEOGRAPHYL2.equalsIgnoreCase(geoType)) {
			predicates.add(builder
					.and(builder.equal(neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL2_KEY).get(InfraConstants.ID), geoId)));

			criteriaQuery.select(builder.construct(NELocationWrapper.class,
					neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL3_KEY).get(InfraConstants.NE_LATITUDE_KEY),
					neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL3_KEY).get(InfraConstants.NE_LONGITUDE_KEY),
					builder.count(neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL3_KEY).get(InfraConstants.BOUNDARY_NAME)),
					neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL3_KEY).get(InfraConstants.BOUNDARY_NAME),
					neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL3_KEY).get(InfraConstants.BOUNDARY_DISPLAY_NAME),
					neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL3_KEY).get(InfraConstants.ID)));
			criteriaQuery.groupBy(neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL3_KEY).get(InfraConstants.BOUNDARY_NAME));
		} else if (InfraConstants.GEOGRAPHYL3.equalsIgnoreCase(geoType)) {

			predicates.add(builder
					.and(builder.equal(neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL3_KEY).get(InfraConstants.ID), geoId)));

			criteriaQuery.select(builder.construct(NELocationWrapper.class,
					neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.NE_LATITUDE_KEY),
					neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.NE_LONGITUDE_KEY),
					builder.count(neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.BOUNDARY_NAME)),
					neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.BOUNDARY_NAME),
			neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.BOUNDARY_DISPLAY_NAME),
			neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.ID)));
	
			criteriaQuery.groupBy(neLocationRoot.get(InfraConstants.NE_GEOGRAPHYL4_KEY).get(InfraConstants.BOUNDARY_NAME));
		}
	}
	
}

package com.inn.foresight.core.infra.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.utils.ExceptionUtil;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.dao.ITowerProviderDao;
import com.inn.foresight.core.infra.model.Tower;
import com.inn.foresight.core.infra.model.TowerProvider;
import com.inn.foresight.core.infra.utils.InfraConstants;
import com.inn.foresight.core.infra.utils.InfraUtils;
import com.inn.foresight.core.infra.wrapper.TowerProviderWrapper;
@Repository("TowerProviderDaoImpl")
public class TowerProviderDaoImpl extends HibernateGenericDao<Integer, TowerProvider> implements ITowerProviderDao  {
	
	private Logger logger = LogManager.getLogger(TowerProviderDaoImpl.class);
	
	public TowerProviderDaoImpl() {
		super(TowerProvider.class);
	}
	
	@Override
	public List<Tower> getTowerDetailsByProvider(List<String> name,Double minLatitude, Double maxLatitude, Double minLongitude, Double maxLongitude) {
		logger.info("Going to get Tower Provider Details by name ");
	    List<Tower> towerProviderWrapper = new ArrayList<>();
		try {
			if (minLatitude != null && maxLatitude != null && minLongitude != null && maxLongitude != null) {
			Query query = getEntityManager().createNamedQuery("getTowerDetailsByProvider");
			query.setParameter(InfraConstants.TP_NAME_KEY, name);
			query.setParameter(InfraConstants.TP_MINLATITUDE_KEY, minLatitude);
			query.setParameter(InfraConstants.TP_MAXLATITUDE_KEY, maxLatitude);
			query.setParameter(InfraConstants.TP_MINLONGITUDE_KEY, minLongitude);
			query.setParameter(InfraConstants.TP_MAXLONGITUDE_KEY, maxLongitude);
			towerProviderWrapper = query.getResultList();
			} else {
				throw new DaoException(InfraConstants.EXCEPTION_INVALID_PARAMS);
			}

		} catch (Exception exception) {
			 logger.error("error in getTowerDetailsByProvider, err = {}", ExceptionUtils.getStackTrace(exception));
			throw new DaoException(ExceptionUtil.generateExceptionCode("Dao", "TowerProvider", exception));
		}
		return towerProviderWrapper;
	}
	@Override
	public List<TowerProviderWrapper> getCountOfTowersByProvider(Double southWestLong, Double southWestLat, Double northEastLong, Double northEastLat, String geographyLevel,
			 List<String> geographyList,List<String> name) {
		List<TowerProviderWrapper> listOfTower = new ArrayList<>();
		try {
			CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<TowerProviderWrapper> criteriaQuery = builder.createQuery(TowerProviderWrapper.class);
			
			Root<Tower> towerDetail = criteriaQuery.from(Tower.class);
			criteriaQuery = getConstructorStatementForTowersCount(geographyLevel, builder, towerDetail, criteriaQuery,southWestLat, northEastLat, southWestLong, northEastLong);
			List<Predicate> predicateList = new ArrayList<>();
			predicateList=getPredicatesForTowers(builder, towerDetail, southWestLat, northEastLat, southWestLong, northEastLong,name,null);
			criteriaQuery=criteriaQuery.where(predicateList.toArray(new Predicate[] {}));
			criteriaQuery=getAggregationInCreateriaQuery(builder, geographyLevel, towerDetail, criteriaQuery);
			listOfTower = getEntityManager().createQuery(criteriaQuery).getResultList();
		} catch (Exception exception) {
			logger.error("Error while getting Aggregate Towers Count {}", Utils.getStackTrace(exception));
			throw new DaoException("Unable to get Data from TowerDetail");
		}
		logger.info("counts of Towers by Providers: {}" + listOfTower.size());
		return listOfTower;
	}
	private static CriteriaQuery<TowerProviderWrapper> getAggregationInCreateriaQuery(CriteriaBuilder criteriaBuilder,String geographyLevel,Root<Tower> root,CriteriaQuery<TowerProviderWrapper> criteriaQuery) {
		if (geographyLevel.equalsIgnoreCase(InfraConstants.GEOGRAPHYL1)) {
			criteriaQuery.groupBy(root.get(ForesightConstants.GEOGRAPHY_L4fk).get(ForesightConstants.GEOGRAPHY_L3fk).get(ForesightConstants.GEOGRAPHY_L2fk).get(ForesightConstants.GEOGRAPHY_L1fk).get(InfraConstants.BOUNDARY_NAME));
		} else if (geographyLevel.equalsIgnoreCase(InfraConstants.GEOGRAPHYL2)) {
			criteriaQuery.groupBy(root.get(ForesightConstants.GEOGRAPHY_L4fk).get(ForesightConstants.GEOGRAPHY_L3fk).get(ForesightConstants.GEOGRAPHY_L2fk).get(InfraConstants.BOUNDARY_NAME));
		} else if (geographyLevel.equalsIgnoreCase(InfraConstants.GEOGRAPHYL3)) {
			criteriaQuery.groupBy(root.get(ForesightConstants.GEOGRAPHY_L4fk).get(ForesightConstants.GEOGRAPHY_L3fk).get(InfraConstants.BOUNDARY_NAME));
		} else if (geographyLevel.equalsIgnoreCase(InfraConstants.GEOGRAPHYL4)) {
			criteriaQuery.groupBy(root.get(ForesightConstants.GEOGRAPHY_L4fk).get(InfraConstants.BOUNDARY_NAME));
		}
		return criteriaQuery;
	}
	private static List<Predicate> getPredicatesForTowers(CriteriaBuilder criteriaBuilder, Root<Tower> root, Double minLatitude, Double maxLatitude, Double minLongitude, Double maxLongitude,List<String> providerList, Map<String, List<String>> geographyNames) {
		List<Predicate> predicates = new ArrayList<>();
		if (minLatitude != null && maxLatitude != null && minLongitude != null && maxLongitude != null) {
			predicates.add(criteriaBuilder.and(criteriaBuilder.gt(root.get(ForesightConstants.LATITUDE), minLatitude), criteriaBuilder.lt(root.get(ForesightConstants.LATITUDE), maxLatitude)));
			predicates.add(criteriaBuilder.and(criteriaBuilder.gt(root.get(ForesightConstants.LONGITUDE), minLongitude), criteriaBuilder.lt(root.get(ForesightConstants.LONGITUDE), maxLongitude)));
		}
		if (providerList != null && !providerList.isEmpty()) {
			predicates.add((criteriaBuilder.upper(root.get(InfraConstants.TP_TOWERPROVIDER_KEY).get(InfraConstants.TP_NAME_KEY)).in(providerList)));
			}
		if (geographyNames != null && !geographyNames.isEmpty()) {
			List<String> geographies = new ArrayList<>();
			if (geographyNames.get(InfraConstants.GEOGRAPHYL4_TABLE) != null) {
				geographies = geographyNames.get(InfraConstants.GEOGRAPHYL4_TABLE);
				predicates.add(criteriaBuilder.upper(root.get(ForesightConstants.GEOGRAPHY_L4fk).get(InfraConstants.BOUNDARY_NAME)).in(geographies));
			}
			if (geographyNames.get(InfraConstants.GEOGRAPHYL3_TABLE) != null) {
				geographies = geographyNames.get(InfraConstants.GEOGRAPHYL3_TABLE);
				predicates.add(criteriaBuilder.upper(root.get(ForesightConstants.GEOGRAPHY_L4fk).get(ForesightConstants.GEOGRAPHY_L3fk).get(InfraConstants.BOUNDARY_NAME)).in(geographies));
			}
			if (geographyNames.get(InfraConstants.GEOGRAPHYL2_TABLE) != null) {
				geographies = geographyNames.get(InfraConstants.GEOGRAPHYL2_TABLE);
				predicates.add(criteriaBuilder.upper(root.get(ForesightConstants.GEOGRAPHY_L4fk).get(ForesightConstants.GEOGRAPHY_L3fk).get(ForesightConstants.GEOGRAPHY_L2fk).get(InfraConstants.BOUNDARY_NAME)).in(geographies));
			}
			if (geographyNames.get(InfraConstants.GEOGRAPHYL1_TABLE) != null) {
				geographies = geographyNames.get(InfraConstants.GEOGRAPHYL1_TABLE);
				predicates.add(criteriaBuilder.upper(root.get(ForesightConstants.GEOGRAPHY_L4fk).get(ForesightConstants.GEOGRAPHY_L3fk).get(ForesightConstants.GEOGRAPHY_L2fk).get(ForesightConstants.GEOGRAPHY_L1fk).get(InfraConstants.BOUNDARY_NAME)).in(geographies));
			}
		}
		return predicates;
	}
	private CriteriaQuery<TowerProviderWrapper> getConstructorStatementForTowersCount(String geographyLevel,CriteriaBuilder builder, Root<Tower> towerDetail, CriteriaQuery<TowerProviderWrapper> criteriaQuery,
			Double smalllat, Double largeLat, Double smallLong, Double largelong) {
		try {
			if (geographyLevel.equalsIgnoreCase(InfraConstants.GEOGRAPHYL1)) {
				criteriaQuery.select(builder.construct(TowerProviderWrapper.class,
						towerDetail.get(ForesightConstants.GEOGRAPHY_L4fk).get(ForesightConstants.GEOGRAPHY_L3fk).get(ForesightConstants.GEOGRAPHY_L2fk).get(ForesightConstants.GEOGRAPHY_L1fk).get(InfraConstants.BOUNDARY_DISPLAY_NAME),
						builder.countDistinct(towerDetail.get(InfraConstants.TP_ID_KEY)),
						builder.avg(builder.<Number>selectCase().when(builder.between(towerDetail.get(ForesightConstants.LATITUDE), smalllat, largeLat),towerDetail.get(ForesightConstants.LATITUDE)).otherwise(builder.nullLiteral(Number.class))),
			         	builder.avg(builder.<Number>selectCase().when(builder.between(towerDetail.get(ForesightConstants.LONGITUDE), smallLong, largelong),towerDetail.get(ForesightConstants.LONGITUDE)).otherwise(builder.nullLiteral(Number.class))),
			         	builder.max(towerDetail.get(InfraConstants.TP_MODIFICATIONTIME_KEY))));
			} else if (geographyLevel.equalsIgnoreCase(InfraConstants.GEOGRAPHYL2)) {
				criteriaQuery.select(builder.construct(TowerProviderWrapper.class,
						towerDetail.get(ForesightConstants.GEOGRAPHY_L4fk).get(ForesightConstants.GEOGRAPHY_L3fk).get(ForesightConstants.GEOGRAPHY_L2fk).get(InfraConstants.BOUNDARY_DISPLAY_NAME),
								builder.countDistinct(towerDetail.get(InfraConstants.TP_ID_KEY)),
								builder.avg(builder.<Number>selectCase().when(builder.between(towerDetail.get(ForesightConstants.LATITUDE), smalllat, largeLat),towerDetail.get(ForesightConstants.LATITUDE)).otherwise(builder.nullLiteral(Number.class))),
					         	builder.avg(builder.<Number>selectCase().when(builder.between(towerDetail.get(ForesightConstants.LONGITUDE), smallLong, largelong),towerDetail.get(ForesightConstants.LONGITUDE)).otherwise(builder.nullLiteral(Number.class))),
					         	builder.max(towerDetail.get(InfraConstants.TP_MODIFICATIONTIME_KEY))));
			} else if (geographyLevel.equalsIgnoreCase(InfraConstants.GEOGRAPHYL3)) {
				criteriaQuery.select(builder.construct(TowerProviderWrapper.class,
						towerDetail.get(ForesightConstants.GEOGRAPHY_L4fk).get(ForesightConstants.GEOGRAPHY_L3fk).get(InfraConstants.BOUNDARY_DISPLAY_NAME),
						builder.countDistinct(towerDetail.get(InfraConstants.TP_ID_KEY)),
						builder.avg(builder.<Number>selectCase().when(builder.between(towerDetail.get(ForesightConstants.LATITUDE), smalllat, largeLat),towerDetail.get(ForesightConstants.LATITUDE)).otherwise(builder.nullLiteral(Number.class))),
			         	builder.avg(builder.<Number>selectCase().when(builder.between(towerDetail.get(ForesightConstants.LONGITUDE), smallLong, largelong),towerDetail.get(ForesightConstants.LONGITUDE)).otherwise(builder.nullLiteral(Number.class))),
			         	builder.max(towerDetail.get(InfraConstants.TP_MODIFICATIONTIME_KEY))));
			} else if (geographyLevel.equalsIgnoreCase(InfraConstants.GEOGRAPHYL4)) {
				criteriaQuery.select(builder.construct(TowerProviderWrapper.class,
						towerDetail.get(ForesightConstants.GEOGRAPHY_L4fk).get(InfraConstants.BOUNDARY_DISPLAY_NAME),
						builder.countDistinct(towerDetail.get(InfraConstants.TP_ID_KEY)),
						builder.avg(builder.<Number>selectCase().when(builder.between(towerDetail.get(ForesightConstants.LATITUDE), smalllat, largeLat),towerDetail.get(ForesightConstants.LATITUDE)).otherwise(builder.nullLiteral(Number.class))),
			         	builder.avg(builder.<Number>selectCase().when(builder.between(towerDetail.get(ForesightConstants.LONGITUDE), smallLong, largelong),towerDetail.get(ForesightConstants.LONGITUDE)).otherwise(builder.nullLiteral(Number.class))),
			         	builder.max(towerDetail.get(InfraConstants.TP_MODIFICATIONTIME_KEY))));
			}
		} catch (Exception exception) {
			logger.error("Error in getting geography for Towers Count {}", Utils.getStackTrace(exception));
		}
		return criteriaQuery;
	}
	@Override
	@SuppressWarnings("unchecked")
	public List<TowerProviderWrapper> getTowerDetailsByGeography( List<String> providersList, Map<String, List<String>> geographyNames) {
		try {
			logger.info("Going to get TowerProvider Details By Geography");
			providersList = InfraUtils.convertListElements(providersList, InfraConstants.LIST_TOUPPERCASE);
			CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<TowerProviderWrapper> criteriaQuery = criteriaBuilder.createQuery(TowerProviderWrapper.class);
			Root<Tower> towerDetail = criteriaQuery.from(Tower.class);
			criteriaQuery = getConstructorStatementForTowers(criteriaBuilder, towerDetail, criteriaQuery);
			
			List<Predicate> predicates = getPredicatesForTowers(criteriaBuilder,towerDetail,null,null,null,null,providersList,geographyNames);
			Query query = getEntityManager().createQuery(criteriaQuery.where(predicates.toArray(new Predicate[] {})));
			return query.getResultList();
		} catch (NullPointerException nullPointerException) {
			logger.error("NullPointerException caught while getting data from TowerDetail Exception : {}", Utils.getStackTrace(nullPointerException));
			throw new DaoException("Unable to get TowerDetail data  for specific parameters.");
		} catch (IllegalArgumentException illegalArgumentException) {
			logger.error("IllegalArgumentException caught while getting data from TowerDetail Exception : {}", Utils.getStackTrace(illegalArgumentException));
			throw new DaoException("Unable to get TowerDetail data for specific parameters. ");
		} catch (NoResultException noResultException) {
			logger.error("NoResultException caught while getting data from TowerDetail Exception : {}", Utils.getStackTrace(noResultException));
			throw new DaoException(" Unable to get TowerDetail data for specific parameters.");
		} catch (PersistenceException persistenceException) {
			logger.error("PersistenceException caught while getting data from TowerDetail Exception : {}", Utils.getStackTrace(persistenceException));
			throw new DaoException("Unable to  get TowerDetail data for specific parameters.");
		} catch (Exception exception) {
			
			logger.error("Exception caught while getting data from TowerDetail Exception : {}", Utils.getStackTrace(exception));
			throw new DaoException("Unable to get TowerDetail data for specific parameters.");
		}
	}
	private CriteriaQuery<TowerProviderWrapper> getConstructorStatementForTowers(CriteriaBuilder builder,Root towerDetail, CriteriaQuery<TowerProviderWrapper> criteriaQuery) {
		try {
			criteriaQuery.select(builder.construct(TowerProviderWrapper.class,
					towerDetail.get(InfraConstants.TP_ID_KEY),towerDetail.get(InfraConstants.TP_TOWERID_KEY),towerDetail.get(InfraConstants.TP_TOWERNAME_KEY),towerDetail.get(ForesightConstants.LATITUDE),
					towerDetail.get(ForesightConstants.LONGITUDE),towerDetail.get(InfraConstants.TP_TOWERHEIGHT_KEY),towerDetail.get(InfraConstants.TP_TOWERPROVIDER_KEY).get(InfraConstants.TP_NAME_KEY),towerDetail.get(InfraConstants.TP_TOWERPROVIDER_KEY).get(InfraConstants.TP_SERVICETYPE_KEY),
					towerDetail.get(InfraConstants.TP_TOWERPROVIDER_KEY).get(InfraConstants.TP_LEGENDCOLOR_KEY),towerDetail.get(InfraConstants.TP_TOWERPROVIDER_KEY).get(InfraConstants.TP_PROVIDERRANK_KEY),towerDetail.get(InfraConstants.TP_CREATIONTIME_KEY),towerDetail.get(InfraConstants.TP_MODIFICATIONTIME_KEY),
					towerDetail.get(ForesightConstants.GEOGRAPHY_L4fk).get(InfraConstants.BOUNDARY_NAME),
					towerDetail.get(ForesightConstants.GEOGRAPHY_L4fk).get(ForesightConstants.GEOGRAPHY_L3fk).get(InfraConstants.BOUNDARY_NAME),
					towerDetail.get(ForesightConstants.GEOGRAPHY_L4fk).get(ForesightConstants.GEOGRAPHY_L3fk).get(ForesightConstants.GEOGRAPHY_L2fk).get(InfraConstants.BOUNDARY_NAME),
					towerDetail.get(ForesightConstants.GEOGRAPHY_L4fk).get(ForesightConstants.GEOGRAPHY_L3fk).get(ForesightConstants.GEOGRAPHY_L2fk).get(ForesightConstants.GEOGRAPHY_L1fk).get(InfraConstants.BOUNDARY_NAME)
					));
			} catch (Exception exception) {
			logger.error("Error in creating constructor for towers {}", Utils.getStackTrace(exception));
		}
		return criteriaQuery;
	}
	@Override
	public List<Tower> getTowerDetailsByTowerId(String towerId) {
		logger.info("Going to get Tower Provider Details by ID ");
	    List<Tower> towerProviderWrapper = new ArrayList<>();
		try {
			Query query = getEntityManager().createNamedQuery("getTowerDetailByTowerId");
			query.setParameter(InfraConstants.TP_TOWERID_KEY, towerId);
			towerProviderWrapper = query.getResultList();
		} catch (Exception exception) {
			 logger.error("error in getTowerDetailsByName, err = {}", ExceptionUtils.getStackTrace(exception));
			throw new DaoException(ExceptionUtil.generateExceptionCode("Dao", "TowerProvider", exception));
		}
		return towerProviderWrapper;
	}
	@Override
	public List<TowerProviderWrapper> getAllTowerProviderDetails() {
		logger.info("Going to get Tower Provider name .");
	    List<TowerProviderWrapper> towerProviderList = new ArrayList<>();
		try {
			Query query = getEntityManager().createNamedQuery("getAllTowerProviderDetails");
			towerProviderList = query.getResultList();
		} catch (Exception exception) {
			 logger.error("error in getAllTowerProviderDetails, err = {}", ExceptionUtils.getStackTrace(exception));
		}
		return towerProviderList;
	}
}

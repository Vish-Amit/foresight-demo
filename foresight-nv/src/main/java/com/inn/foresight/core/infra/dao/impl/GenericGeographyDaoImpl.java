package com.inn.foresight.core.infra.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.QueryTimeoutException;
import javax.persistence.TransactionRequiredException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CompoundSelection;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;

import com.inn.commons.Symbol;
import com.inn.commons.maps.Corner;
import com.inn.commons.maps.LatLng;
import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.utils.ExceptionUtil;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.infra.dao.IGenericGeographyDao;
import com.inn.foresight.core.infra.utils.InfraConstants;
import com.inn.product.um.geography.utils.wrapper.GeographyWrapper;

public class GenericGeographyDaoImpl<T> extends HibernateGenericDao<Integer, T> implements IGenericGeographyDao<T>{

	/** The logger. */
	private Logger logger = LogManager.getLogger(GenericGeographyDaoImpl.class);
	
	private Class<T> clazz;
	
	public GenericGeographyDaoImpl(Class<T> type) {
		super(type);
		this.clazz=type;
	}

	@Override
	public T getGeography(String name) {
		try {
			logger.debug("Going to get  geography names {},{}", name, clazz.getSimpleName());
			CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);
			Root<T> entity = criteriaQuery.from(clazz);
			List<Predicate> predicates = new ArrayList<>();
			predicates.add(criteriaBuilder.equal(entity.get(InfraConstants.NAME), name.toLowerCase()));
			criteriaQuery.select(entity).where(predicates.toArray(new Predicate[] {}));
			TypedQuery<T> query = getEntityManager().createQuery(criteriaQuery);
			return query.getSingleResult();
		} catch (NoResultException exception) {
			logger.error("Error in getGeography,Err:{}", ExceptionUtils.getMessage(exception));
			throw new DaoException(
					ExceptionUtil.generateExceptionCode(InfraConstants.DAO, clazz.getSimpleName(), exception));
		} catch (Exception exception) {
			logger.error("Error in getGeography,Err:{}", ExceptionUtils.getStackTrace(exception));
			throw new DaoException(
					ExceptionUtil.generateExceptionCode(InfraConstants.DAO, clazz.getSimpleName(), exception));
		}
	}

	@Override
	public List<T> getGeography(List<String> names) {
		logger.debug("Going to getGeography by list of name {} ,{}", names.size(), clazz.getSimpleName());
		List<T> geographyList = new ArrayList<>();
		try {
			CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);
			Root<T> entity = criteriaQuery.from(clazz);
			List<Predicate> predicates = new ArrayList<>();
			predicates.add(entity.get(InfraConstants.NAME).in(names));
			criteriaQuery.select(entity).where(predicates.toArray(new Predicate[] {}));
			TypedQuery<T> query = getEntityManager().createQuery(criteriaQuery);
			geographyList = query.getResultList();
		} catch (PersistenceException exception) {
			logger.warn("Exception in getGeography, {}", exception.getMessage());
		} catch (Exception exception) {
			logger.error("Error while getGeography,err msg ={}", ExceptionUtils.getStackTrace(exception));
		}
		return geographyList;
	}

  @Override
  public List<String> getDistinctGeographyNames() {
    try {
      logger.debug("Going to get all Distinct geography names");
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<String> criteriaQuery = criteriaBuilder.createQuery(String.class);
		Root<T> entity = criteriaQuery.from(clazz);
		List<Predicate> predicates = new ArrayList<>();
        predicates.add(entity.get(InfraConstants.NAME).isNotNull());
        Selection<String> selection = entity.get(InfraConstants.NAME);
        criteriaQuery.select(selection).distinct(true).where(predicates.toArray(new Predicate[] {}));
		TypedQuery<String> query = getEntityManager().createQuery(criteriaQuery);
        return query.getResultList();
    } catch (NoResultException exception) {
      logger.error("No geography Name found of {}, err : {}", clazz.getSimpleName(),
          exception.getMessage());
      throw new DaoException(ExceptionUtil.generateExceptionCode(ForesightConstants.DAO,
          clazz.getSimpleName(), exception));
    } catch (HibernateException exception) {
      logger.error("error in finding geography of {} , err: {}", clazz.getSimpleName(),
          exception.getMessage());
      throw new DaoException(ExceptionUtil.generateExceptionCode(ForesightConstants.DAO,
          clazz.getSimpleName(), exception));
    } catch (Exception exception) {
      logger.error("Error while finding geography Exception {} ", exception);
      throw new DaoException(ExceptionUtil.generateExceptionCode(ForesightConstants.DAO,
          clazz.getSimpleName(), exception));
    }
  }

	@Override
	public List<T> getAllGeography() {
	  List<T> geographyList = new ArrayList<>();
	  logger.info("get all geography {}",clazz.getSimpleName());
	  try {
		    CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);
			Root<T> entity = criteriaQuery.from(clazz);
			List<Predicate> predicates = new ArrayList<>();
	        predicates.add(entity.get(InfraConstants.NAME).isNotNull());
	        criteriaQuery.select(entity).where(predicates.toArray(new Predicate[] {}));
			TypedQuery<T> query = getEntityManager().createQuery(criteriaQuery);
			geographyList = query.getResultList();
      } catch (IllegalStateException illegalStateException) {
          logger.error(
                  "Error finding  getAllGeography by Type class {} and message {} "
                          + illegalStateException.getClass(),
                  illegalStateException.getMessage());
      } catch (QueryTimeoutException queryTimeoutException) {
          logger.error(
                  "Error finding getAllGeography by Type class {} and message {} "
                          + queryTimeoutException.getClass(),
                  queryTimeoutException.getMessage());
      } catch (TransactionRequiredException requiredException) {
          logger.error(
                  "Error  finding getAllGeography by Type  class {} and message {} "
                          + requiredException.getClass(),
                  requiredException.getMessage());
      } catch (PersistenceException persistenceException) {
          logger.error(
                  "Error finding getAllGeography by Type  class {} and message {} "
                          + persistenceException.getClass(),
                  persistenceException.getMessage());
      } catch (Exception exception) {
          logger.error(
                  "Error finding getAllGeography by Type class {} and message {} "
                          + exception.getClass(), exception.getMessage());
      }
      return geographyList;
	}

  @Override
  public List<GeographyWrapper> getAllGeographyWrapper() {
    try {
    	CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
   		CriteriaQuery<GeographyWrapper> criteriaQuery = criteriaBuilder.createQuery(GeographyWrapper.class);
   		Root<T> entity = criteriaQuery.from(clazz);
        CompoundSelection<GeographyWrapper> selection = criteriaBuilder.construct(GeographyWrapper.class, entity.get(InfraConstants.ID),entity.get(InfraConstants.NAME),entity.get(InfraConstants.NE_LATITUDE_KEY)
        		,entity.get(InfraConstants.NE_LONGITUDE_KEY));
        criteriaQuery.select(selection);
   		TypedQuery<GeographyWrapper> query = getEntityManager().createQuery(criteriaQuery);
        return query.getResultList();
    } catch (Exception exception) {
      logger.error("Exception in getAllGeographyWrapper  {} ", ExceptionUtils.getStackTrace(exception));
      throw new DaoException(ExceptionUtil.generateExceptionCode(ForesightConstants.DAO,
          clazz.getSimpleName(), exception));
    }
  }

  @Override
  public List<GeographyWrapper> getGeographyWrapper(Corner corner) {
    logger.debug("Going to get all getGeographyWrapper list by corner ");
    try {
    	CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
   		CriteriaQuery<GeographyWrapper> criteriaQuery = criteriaBuilder.createQuery(GeographyWrapper.class);
   		Root<T> entity = criteriaQuery.from(clazz);
   		List<Predicate> predicates = new ArrayList<>();
   		predicates.add(criteriaBuilder.ge(entity.get(InfraConstants.NE_LATITUDE_KEY),corner.getSouthWestLat()));
   		predicates.add(criteriaBuilder.ge(entity.get(InfraConstants.NE_LONGITUDE_KEY), corner.getSouthWestLon()));
   		predicates.add(criteriaBuilder.le(entity.get(InfraConstants.NE_LATITUDE_KEY), corner.getNorthEastLat()));
   		predicates.add(criteriaBuilder.le(entity.get(InfraConstants.NE_LONGITUDE_KEY), corner.getNorthEastLon()));
        CompoundSelection<GeographyWrapper> selection = criteriaBuilder.construct(GeographyWrapper.class, entity.get("id"),entity.get("name"),entity.get("latitude"),entity.get("longitude"));
        criteriaQuery.select(selection).where(predicates.toArray(new Predicate[] {}));
        TypedQuery<GeographyWrapper> query = getEntityManager().createQuery(criteriaQuery);
      return query.getResultList();
    } catch (NoResultException exception) {
      logger.error("Error inside getGeographyWrapper No geography Name found ==> {} ",
          clazz.getSimpleName());
      throw new DaoException(ExceptionUtil.generateExceptionCode(InfraConstants.DAO,
          clazz.getSimpleName(), exception));
    } catch (HibernateException exception) {
      logger.error("Error inside getGeographyWrapper : {}, {}", clazz.getSimpleName(),
          exception.getMessage());
      throw new DaoException(ExceptionUtil.generateExceptionCode(InfraConstants.DAO,
          clazz.getSimpleName(), exception));
    } catch (Exception exception) {
      logger.error("Error while finding geography {} ==> Exception {} ", clazz.getSimpleName(),
          ExceptionUtils.getStackTrace(exception));
      throw new DaoException(ExceptionUtil.generateExceptionCode(InfraConstants.DAO,
          clazz.getSimpleName(), exception));
    }
  }

	@Override
	public T getGeography(Integer id) {
	  logger.debug("Going to getGeography by id={}", id);
     try {
    	 CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
    		CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);
    		Root<T> entity = criteriaQuery.from(clazz);
    		List<Predicate> predicates = new ArrayList<>();
    		predicates.add(criteriaBuilder.equal(entity.get(InfraConstants.ID),id));
    		
    		criteriaQuery.select(entity).where(predicates.toArray(new Predicate[] {}));
            TypedQuery<T> query = getEntityManager().createQuery(criteriaQuery);
          return  query.getSingleResult();
      } catch (PersistenceException exception) {
          logger.warn("Exception in getGeography by id, {}", ExceptionUtils.getStackTrace(exception));
          throw new DaoException(ExceptionUtil.generateExceptionCode(
              ForesightConstants.DAO, clazz.getSimpleName(), exception));

      } catch (Exception exception) {
          logger.error("Error while getGeography by id,err msg ={}", ExceptionUtils.getStackTrace(exception));
          throw new DaoException(ExceptionUtil.generateExceptionCode(
              ForesightConstants.DAO, clazz.getSimpleName(), exception));
      }   
     }
     

  @Override
  public List<T> getGeographyByIdList(List<Integer> ids) {
    logger.debug("Going to getGeographyByIdList by ids {} ", ids.size());

    List<T> geographyList = new ArrayList<>();
    try {
    	CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
 		CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);
 		Root<T> entity = criteriaQuery.from(clazz);
 		List<Predicate> predicates = new ArrayList<>();
 		predicates.add(entity.get(InfraConstants.ID).in(ids));
		criteriaQuery.select(entity).where(predicates.toArray(new Predicate[] {}));
        TypedQuery<T> query = getEntityManager().createQuery(criteriaQuery);
      geographyList = query.getResultList();
    } catch (PersistenceException exception) {
      logger.error("Exception in getGeographyByIdList, {}", exception.getMessage());
    } catch (Exception exception) {
      logger.error("Error while getGeographyByIdList,err msg ={}", ExceptionUtils.getStackTrace(exception));
    }
    return geographyList;
  }

  @Override
  public List<T> getChildByL1Name(String name) {
    logger.debug("Going inside getChildByL1Name : {} ", name);
    try {
      TypedQuery<T> query =
          getEntityManager().createNamedQuery("getChildByL1Name"+clazz.getSimpleName(), clazz)
              .setParameter(ForesightConstants.NAME, name);
      return query.getResultList();
    } catch (Exception exception) {
      logger.error("Exception in getChildByL1Name {}",exception);
      throw new DaoException(ExceptionUtil.generateExceptionCode(ForesightConstants.DAO,
          clazz.getSimpleName(), exception));
    }
  }

	@Override
	public List<T> getChildByL2Name(String name) {
	  logger.debug("Going inside getChildByL2Name : {} ", name);
	    try {
	      TypedQuery<T> query =
	          getEntityManager().createNamedQuery("getChildByL2Name"+clazz.getSimpleName(), clazz)
	              .setParameter(ForesightConstants.NAME, name);
	      return query.getResultList();
	    } catch (Exception exception) {
	      throw new DaoException(ExceptionUtil.generateExceptionCode(ForesightConstants.DAO,
	          clazz.getSimpleName(), exception));
	    }
	  }

  @Override
  public List<T> getChildByL3Name(String name) {
    logger.debug("Going inside getChildByL3Name : {} ,{}", name,clazz.getSimpleName());
    try {
      TypedQuery<T> query =
          getEntityManager().createNamedQuery("getChildByL3Name" + clazz.getSimpleName(), clazz)
              .setParameter(ForesightConstants.NAME, name);
      return query.getResultList();
    } catch (Exception exception) {
      throw new DaoException(ExceptionUtil.generateExceptionCode(ForesightConstants.DAO,
          clazz.getSimpleName(), exception));
    }
  }

  @Override
  public List<T> getChildByL1Id(Integer id) {
    logger.debug("Going inside getChildByL1Id : {},{} ", id, clazz.getSimpleName());
    try {
      TypedQuery<T> query =
          getEntityManager().createNamedQuery("getChildByL1Id" + clazz.getSimpleName(), clazz)
              .setParameter("id", id);
      return query.getResultList();
    } catch (Exception exception) {
      throw new DaoException(ExceptionUtil.generateExceptionCode(ForesightConstants.DAO,
          clazz.getSimpleName(), exception));
    }
  }

	@Override
	public List<T> getChildByL2Id(Integer id) {
	  logger.debug("Going inside getChildByL2Id : {} ,{}", id,clazz.getSimpleName());
	    try {
	      TypedQuery<T> query =
	          getEntityManager().createNamedQuery("getChildByL2Id" + clazz.getSimpleName(), clazz)
	              .setParameter("id", id);
	      return query.getResultList();
	    } catch (Exception exception) {
	      throw new DaoException(ExceptionUtil.generateExceptionCode(ForesightConstants.DAO,
	          clazz.getSimpleName(), exception));
	    }
	 }

	@Override
	public List<T> getChildByL3Id(Integer id) {
	  logger.debug("Going inside getChildByL3Id : {} ,{}", id,clazz.getSimpleName());
      try {
        TypedQuery<T> query =
            getEntityManager().createNamedQuery("getChildByL3Id" + clazz.getSimpleName(), clazz)
                .setParameter("id", id);
        return query.getResultList();
      } catch (Exception exception) {
        throw new DaoException(ExceptionUtil.generateExceptionCode(ForesightConstants.DAO,
            clazz.getSimpleName(), exception));
      }
   }

  @Override
  public List<GeographyWrapper> getChildByParentId(Integer id) {
    logger.debug("Going inside getChildByParentId : {} ==>{}", id, clazz.getSimpleName());
    try {
      TypedQuery<GeographyWrapper> query =
          getEntityManager().createNamedQuery("getChildByParentId" + clazz.getSimpleName(),
              GeographyWrapper.class).setParameter("id", id);
      return query.getResultList();
    } catch (Exception exception) {
      throw new DaoException(ExceptionUtil.generateExceptionCode(ForesightConstants.DAO,
          clazz.getSimpleName(), exception));
    }
  }

  @Override
  public List<GeographyWrapper> getChildByParentIds(List<Integer> ids) {
    logger.debug("Going inside getChildByParentIds : {} ==>{}", ids, clazz.getSimpleName());
    try {
      TypedQuery<GeographyWrapper> query =
          getEntityManager().createNamedQuery("getChildByParentIds" + clazz.getSimpleName(),
              GeographyWrapper.class).setParameter("ids", ids);
      return query.getResultList();
    } catch (Exception exception) {
      throw new DaoException(ExceptionUtil.generateExceptionCode(ForesightConstants.DAO,
          clazz.getSimpleName(), exception));
    }
  }

  @Override
  public List<T> searchByName(String name, Integer uLimit, Integer lLimit) {
    logger.debug("Going inside searchByName : {} ,{},{},{}", name, uLimit, lLimit,
        clazz.getSimpleName());

    try {
    	CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
 		CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);
 		Root<T> entity = criteriaQuery.from(clazz);
 		List<Predicate> predicates = new ArrayList<>();
 		Expression<String> path = entity.get(InfraConstants.NAME);
 		predicates.add(criteriaBuilder.like(path, Symbol.PERCENT_STRING+name+Symbol.PERCENT_STRING));
		criteriaQuery.select(entity).where(predicates.toArray(new Predicate[] {}));
        TypedQuery<T> query = getEntityManager().createQuery(criteriaQuery);
        setUpperLowerLimits(query, uLimit, lLimit);
      return query.getResultList();
    } catch (Exception exception) {
      logger.error("Error in searchByName, name:{} Err:{}", name,
          ExceptionUtils.getStackTrace(exception));
      throw new DaoException(ExceptionUtil.generateExceptionCode(ForesightConstants.DAO,
          clazz.getSimpleName(), exception));
    }
  }

  private void setUpperLowerLimits(Query query, Integer uLimit, Integer lLimit) {
    if (uLimit != null && lLimit != null) {
      query.setFirstResult(lLimit);
      query.setMaxResults(uLimit);
    }
  }

  @Override
  public T getGeographyByNameAndParentId(String name, Integer parentId) {
    try {
      TypedQuery<T> query =
          getEntityManager()
              .createNamedQuery("getGeographyByNameAndParentId" + clazz.getSimpleName(), clazz)
              .setParameter("name", name).setParameter("parentId", parentId);
      return query.getSingleResult();
    } catch (Exception exception) {
      logger.error("Exception in getGeographyByNameAndParentId, {}",
          ExceptionUtils.getStackTrace(exception));
      throw new DaoException(ExceptionUtil.generateExceptionCode(ForesightConstants.DAO,
          clazz.getSimpleName(), exception));
    }
  }

  @Override
  public T getGeographyByLatLng(LatLng latLng) {
    logger.debug("Going to getGeographyBy By LATLONG {},{}", latLng.getLatitude(),
        latLng.getLongitude());
    try {
    	
    	CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
 		CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);
 		Root<T> entity = criteriaQuery.from(clazz);
 		List<Predicate> predicates = new ArrayList<>();
 		predicates.add(criteriaBuilder.equal(entity.get("latitude"), latLng.getLatitude()));
 		predicates.add(criteriaBuilder.equal(entity.get("longitude"), latLng.getLongitude()));
		criteriaQuery.select(entity).where(predicates.toArray(new Predicate[] {}));
        TypedQuery<T> query = getEntityManager().createQuery(criteriaQuery);
      return query.getSingleResult();
    } catch (Exception exception) {
      logger
          .error("Exception in getGeographyByLatLng, {}", ExceptionUtils.getStackTrace(exception));
      throw new DaoException(ExceptionUtil.generateExceptionCode(ForesightConstants.DAO,
          clazz.getSimpleName(), exception));
    }
  }

}

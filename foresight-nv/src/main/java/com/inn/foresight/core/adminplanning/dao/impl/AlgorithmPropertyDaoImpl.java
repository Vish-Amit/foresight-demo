package com.inn.foresight.core.adminplanning.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TransactionRequiredException;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.utils.Preconditions;
import com.inn.foresight.core.adminplanning.dao.IAlgorithmPropertyDao;
import com.inn.foresight.core.adminplanning.model.AlgorithmProperty;
import com.inn.foresight.core.adminplanning.util.AlgorithmPlanningConstant;
import com.inn.foresight.core.adminplanning.wrapper.AlgorithmResponse;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;

/**
 * The Class AlgorithmPropertyDaoImpl.
 */
@Repository("AlgorithmPropertyDaoImpl")
public class AlgorithmPropertyDaoImpl extends HibernateGenericDao<Integer, AlgorithmProperty>
		implements IAlgorithmPropertyDao {

	/** The logger. */
	private Logger logger = LogManager.getLogger(AlgorithmPropertyDaoImpl.class);

	/**
	 * Instantiates a new algorithm property dao impl.
	 */
	public AlgorithmPropertyDaoImpl() {
		super(AlgorithmProperty.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AlgorithmResponse> getExistingExceptions(Integer algoId) {
		List<AlgorithmResponse> list = new ArrayList<>();
		try {
			enableDeletedFilter(false);
			Query queryL1 = this.getEntityManager().createNamedQuery("getExistingL1Exceptions")
					.setParameter("id", algoId);
			Query queryL2 = this.getEntityManager().createNamedQuery("getExistingL2Exceptions")
					.setParameter("id", algoId);
			Query queryL3 = this.getEntityManager().createNamedQuery("getExistingL3Exceptions")
					.setParameter("id", algoId);
			Query queryL4 = this.getEntityManager().createNamedQuery("getExistingL4Exceptions")
					.setParameter("id", algoId);
			list.addAll(queryL1.getResultList());
			list.addAll(queryL2.getResultList());
			list.addAll(queryL3.getResultList());
			list.addAll(queryL4.getResultList());
			return list;
		} catch (TransactionRequiredException tr) {
			throw new DaoException(tr.getLocalizedMessage());
		} catch (Exception e) {
			logger.error("Exception in getExistingExceptions : {}", Utils.getStackTrace(e));
			throw new DaoException(e.getCause());
		}
	}

	/**
	 * Enable deleted filter.
	 *
	 * @param deleted the deleted
	 */
	private void enableDeletedFilter(Boolean deleted) {
		try {
			Preconditions.checkNotNull(deleted);
			Session s = (Session) getEntityManager().getDelegate();
			s.enableFilter("APdeletedFilter").setParameter("deleted", deleted);
		} catch (RestException re) {
			logger.error("Could not enable deleted filter : deleted field is null");
		} catch (Exception e) {
			logger.error("Could not enable deleted filter : {}", e.getCause());
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AlgorithmProperty> getPANProperties(Integer algorithmId) {
		try {
			enablePANFilter();
			enableDeletedFilter(false);
			Query query = this.getEntityManager().createNamedQuery("getProperties").setParameter("algorithmId", algorithmId);
			return query.getResultList();
		} catch (TransactionRequiredException tr) {
			throw new DaoException(tr.getLocalizedMessage());
		} catch (Exception e) {
			logger.error("Exception in getPANProperties : {}", Utils.getStackTrace(e));
			throw new DaoException(e.getCause());
		}
	}

	/**
	 * Enable PAN filter.
	 */
	private void enablePANFilter() {
		try {
			Session s = (Session) getEntityManager().getDelegate();
			s.enableFilter("APPANFilter");
		} catch (Exception e) {
			logger.error("Could not enable PAN filter : {}", e.getMessage());
		}

	}

	/**
	 * Enable geography filter.
	 *
	 * @param type the type
	 * @param id the id
	 */
	private void enableGeographyFilter(String type, Integer id) {
		try {
			Session s = (Session) getEntityManager().getDelegate();
			if (AlgorithmPlanningConstant.L1.equalsIgnoreCase(type)) {
				s.enableFilter("APgeographyL1Filter").setParameter("id", id);
			} else if (AlgorithmPlanningConstant.L2.equalsIgnoreCase(type)) {
				s.enableFilter("APgeographyL2Filter").setParameter("id", id);
			} else if (AlgorithmPlanningConstant.L3.equalsIgnoreCase(type)) {
				s.enableFilter("APgeographyL3Filter").setParameter("id", id);
			} else if (AlgorithmPlanningConstant.L4.equalsIgnoreCase(type)) {
				s.enableFilter("APgeographyL4Filter").setParameter("id", id);
			}
		} catch (Exception e) {
			logger.error("Could not enable geography filter : {}", Utils.getStackTrace(e));
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AlgorithmProperty> getPropertiesForException(Integer algorithmId, Integer exceptionId, String type) {
		try {
			Query query = this.getEntityManager().createNamedQuery("getProperties").setParameter("algorithmId", algorithmId);
			enableDeletedFilter(false);
			enableGeographyFilter(type, exceptionId);
			return query.getResultList();
		} catch (TransactionRequiredException tr) {
			throw new DaoException(tr.getLocalizedMessage());
		} catch (Exception e) {
			logger.error("Exception in getPropertiesForException : {}", Utils.getStackTrace(e));
			throw new DaoException(e);
		}

	}

	/**
	 * @deprecated This method is not required
	 */
	@Deprecated
	@Override
	public List<AlgorithmProperty> getPropertiesForException(List<AlgorithmResponse> algoRequestList,
			Integer algoId) {
		try {
			Map<String, List<Integer>> geoTypeMap = getGeoTypeMap(algoRequestList);
			if (!geoTypeMap.isEmpty()) {
				logger.info("Inside resetAlgorithmPropertyData method geoTypeMap size : {}", geoTypeMap.size());
				Query query = this.getEntityManager().createNamedQuery("resetProperties").setParameter("id", algoId)
						.setParameter("l1List", geoTypeMap.get(AlgorithmPlanningConstant.L1));
				query.setParameter("l2List", geoTypeMap.get(AlgorithmPlanningConstant.L2));
				query.setParameter("l3List", geoTypeMap.get(AlgorithmPlanningConstant.L3));
				query.setParameter("l4List", geoTypeMap.get(AlgorithmPlanningConstant.L4));
				return query.getResultList();
			}
		} catch (Exception e) {
			logger.error(
					"Exception occur in @resetAlgorithmPropertyData method with algoRequestList size : {},algoId : {}  error msg : {}",
					algoRequestList.size(), algoId, Utils.getStackTrace(e));
			throw new DaoException(e.getMessage());
		}
		return new ArrayList<>();

	}

	/**
	 * Gets the geo type map.
	 *
	 * @param algoRequestList the algo request list
	 * @return the geo type map
	 * @throws DaoException the dao exception
	 */
	private Map<String, List<Integer>> getGeoTypeMap(List<AlgorithmResponse> algoRequestList) {
		Map<String, List<Integer>> geoTypeMap = new HashMap<>();
		try {
			geoTypeMap.put(AlgorithmPlanningConstant.L1, getAlgoList(AlgorithmPlanningConstant.L1, algoRequestList));
			geoTypeMap.put(AlgorithmPlanningConstant.L2, getAlgoList(AlgorithmPlanningConstant.L2, algoRequestList));
			geoTypeMap.put(AlgorithmPlanningConstant.L3, getAlgoList(AlgorithmPlanningConstant.L3, algoRequestList));
			geoTypeMap.put(AlgorithmPlanningConstant.L4, getAlgoList(AlgorithmPlanningConstant.L4, algoRequestList));
		} catch (Exception e) {
			logger.error("Exception occur in @getGeoTypeMap method with algoRequestList size : {} error msg : {}",algoRequestList.size(),Utils.getStackTrace(e));
			throw new DaoException(e.getMessage());
		}
		return geoTypeMap;
	}

	/**
	 * Gets the algo list.
	 *
	 * @param type the type
	 * @param exceptionList the exception list
	 * @return the algo list
	 */
	private List<Integer> getAlgoList(String type, List<AlgorithmResponse> exceptionList) {
		return exceptionList.stream().filter(geographyException -> type.equalsIgnoreCase(geographyException.getType()))
				.map(AlgorithmResponse::getId).collect(Collectors.toList());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AlgorithmProperty> getPropertyByName(String propertyName) {
		try {
			
			Query query = getEntityManager().createNamedQuery("getAlgorithPropertyByName").setParameter("name", propertyName);
			return query.getResultList();
		} catch(NoResultException e) {
			logger.error("NoResultException inside getPropertyByName {}",ExceptionUtils.getStackTrace(e));
			return Collections.emptyList();
		}
		catch(Exception e) {
			logger.error("Exception inside getPropertyByName {}",ExceptionUtils.getStackTrace(e));
			throw new DaoException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getPropertyHistory(Integer propertyId) {
		try {
			Query query = getEntityManager().createNativeQuery(
					"select a.previousvalue, a.value, a.reason, CONCAT_WS(' ', u.firstname, u.lastname), a.modificationtime from AlgorithmProperty_AUD a left join User u on (a.lastmodifierid_fk = u.userid_pk) where algorithmpropertyid_pk = :id order by modificationtime desc")
					.setParameter("id", propertyId);

			return query.getResultList();
		} catch (Exception e) {
			logger.error("Exception in getting property history for id : {} {}", propertyId, Utils.getStackTrace(e));
			throw new DaoException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AlgorithmProperty> getPropertyByNameAndId(Integer id , String name){
		try{
			Query query = this.getEntityManager().createNamedQuery("getAlgorithmPropertyByIdAndName").setParameter("id",id).setParameter("name",name);	
		return query.getResultList();
	} catch(NoResultException e) {
		logger.error("NoResultException inside getPropertyByNameAndId {}",ExceptionUtils.getStackTrace(e));
		return Collections.emptyList();
	}
	catch(Exception e) {
		logger.error("Exception inside getPropertyByNameAndId {}",ExceptionUtils.getStackTrace(e));
		throw new DaoException(e);
	}
		}
	

	@SuppressWarnings("unchecked")
	@Override
	public AlgorithmProperty getAlgorithPropertyByNameAndAlgorithmName(String algorithmName, String propertyName){
		try{
			Query query = this.getEntityManager().createNamedQuery("getAlgorithPropertyByNameAndAlgorithmName")
					.setParameter("algorithmPropertyName", propertyName).setParameter("algorithmName", algorithmName);	
			return (AlgorithmProperty) query.getSingleResult();
		} catch(NoResultException e) {
			logger.warn("NoResultException inside getAlgorithPropertyByNameAndAlgorithmName algorithmName:{}, propertName:{}-Exception:{}",
					algorithmName, propertyName, ExceptionUtils.getStackTrace(e));
			return new AlgorithmProperty();
		} catch(Exception e) {
			logger.error("Exception inside getAlgorithPropertyByNameAndAlgorithmName for algorithmName:{},propertyName:{},{}",algorithmName, propertyName,ExceptionUtils.getStackTrace(e));
			throw new DaoException(e);
		}
	}
}
package com.inn.foresight.core.infra.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.inn.commons.lang.StringUtils;
import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.BusinessException;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.dao.INELocationParameterDao;
import com.inn.foresight.core.infra.model.NELocation;
import com.inn.foresight.core.infra.model.NELocationParameter;
import com.inn.foresight.core.infra.utils.enums.NELType;
import com.inn.product.um.geography.model.GeographyL4;

/**
 * The Class NELocationDaoImpl.
 */
@Repository("NELocationParameterDaoImpl")
public class NELocationParameterDaoImpl extends HibernateGenericDao<Integer, NELocationParameter>
		implements INELocationParameterDao {

	/** The logger. */
	private Logger logger = LogManager.getLogger(NELocationParameterDaoImpl.class);

	@Transactional
	@Override
	public NELocationParameter create(NELocationParameter anEntity) {
		return super.create(anEntity);
	}

	@Transactional
	@Override
	public void delete(NELocationParameter anEntity) {
		super.delete(anEntity);
	}

	@Transactional
	@Override
	public void deleteByPk(Integer entityPk) {
		super.deleteByPk(entityPk);
	}

	@Transactional
	@Override
	public List<NELocationParameter> findAll() {
		return super.findAll();
	}

	@Transactional
	@Override
	public NELocationParameter findByPk(Integer entityPk) {
		return super.findByPk(entityPk);
	}

	@Transactional
	@Override
	public NELocationParameter update(NELocationParameter anEntity) {
		return super.update(anEntity);
	}

	/**
	 * Instantiates a new NE location dao impl.
	 */
	public NELocationParameterDaoImpl() {
		super(NELocationParameter.class);
	}

	@Override
	public String getTotalFloorByNeLocationId(Integer nelocationId) {
		String totalFloor = "0";
		logger.info("nelocationid :{}", nelocationId);
		try {
			TypedQuery<String> query = getEntityManager().createNamedQuery("getTotalFloorByNeLocationId", String.class);
			query.setParameter("nelocationId", nelocationId);

			totalFloor = query.getSingleResult();
		} catch (NoResultException e) {
			logger.error("error while getting NELocation :{}", ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			logger.error("error while getting NELocation :{}", ExceptionUtils.getStackTrace(e));
			throw new BusinessException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
		return totalFloor;
	}

	@Override
	public List<Object[]> getParameterAndKeyByNeLocationId(Integer nelocationId) {
		List<Object[]> mapKeyValue = new ArrayList<>();
		logger.info("nelocationid :{}", nelocationId);
		try {
			TypedQuery<Object[]> createNamedQuery = getEntityManager()
					.createNamedQuery("getParameterAndKeyByNeLocationId", Object[].class);
			createNamedQuery.setParameter("nelocationId", nelocationId);

			mapKeyValue = createNamedQuery.getResultList();
		} catch (NoResultException e) {
			logger.error("error while getting NELocation :{}", ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			logger.error("error while getting NELocation :{}", ExceptionUtils.getStackTrace(e));
			throw new BusinessException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
		return mapKeyValue;
	}

	@Override
	public List<NELocationParameter> getNELocationParamByNELId(Integer nelId) {
		// logger.info("inside @getNELocationParamByNELId method nelId: {}", nelId);
		try {
			TypedQuery<NELocationParameter> query = getEntityManager().createNamedQuery("getNELocationParamByNELId",
					NELocationParameter.class);
			query.setParameter("nelId", nelId);
			return query.getResultList();
		} catch (NoResultException nre) {
			logger.error("Exception occured while getting NELocationParameter List :{}",
					ExceptionUtils.getMessage(nre));
			return Collections.emptyList();
		} catch (Exception exception) {
			logger.error("Exception occured while getting NELocationParameter List :{}",
					ExceptionUtils.getStackTrace(exception));
			throw new DaoException(ExceptionUtils.getMessage(exception));
		}
	}

	@Override
	public List<Object[]> getAllGCList(Integer llimit, Integer ulimit) {
		logger.info("inside @getAllGCList");
		try {
			TypedQuery<Object[]> query = getEntityManager().createNamedQuery("getAllGCList", Object[].class);
			if (llimit != null && ulimit != null && ulimit > llimit) {
				query.setFirstResult(llimit);
				query.setMaxResults((ulimit - llimit) + 1);
			}
			return query.getResultList();
		} catch (NoResultException nre) {
			logger.error("Exception occured while getting AllGCList :{}", ExceptionUtils.getMessage(nre));
			return Collections.emptyList();
		} catch (Exception exception) {
			logger.error("Exception occured while getting get All GC List :{}",
					ExceptionUtils.getStackTrace(exception));
			throw new DaoException(ExceptionUtils.getMessage(exception));
		}
	}

	@Override
	public Object getParameterValue(Integer gcId, String parameter) {
		logger.info("inside @getParameterValue");
		try {
			TypedQuery<Object> query = getEntityManager().createNamedQuery("getParameterValue", Object.class);
			query.setParameter("locationId", gcId);
			query.setParameter("paramName", parameter);
			return query.getSingleResult();

		} catch (NoResultException nre) {
			logger.error("Exception occured while getting AllGCList :{}", ExceptionUtils.getMessage(nre));
			return null;
		} catch (Exception exception) {
			logger.error("Exception occured while getting get All GC List :{}",
					ExceptionUtils.getStackTrace(exception));
			throw new DaoException(ExceptionUtils.getMessage(exception));
		}
	}

	@Transactional
	@Override
	public NELocationParameter getNELocationParamByParameter(Integer gcId, String parameter) {
		logger.info("inside @getParameterValue");
		try {
			TypedQuery<NELocationParameter> query = getEntityManager().createNamedQuery("getNELocationParamByParameter",
					NELocationParameter.class);
			query.setParameter("locationId", gcId);
			query.setParameter("paramName", parameter);
			return query.getSingleResult();

		} catch (NoResultException nre) {
			logger.error("Exception occured while getting AllGCList :{}", ExceptionUtils.getMessage(nre));
			return null;
		} catch (Exception exception) {
			logger.error("Exception occured while getting get All GC List :{}",
					ExceptionUtils.getStackTrace(exception));
			throw new DaoException(ExceptionUtils.getMessage(exception));
		}
	}

	@Override
	public Long getAllGCTotalCount() {
		logger.info("inside @getAllGCList");
		Long count = 0l;
		try {
			TypedQuery<Long> query = getEntityManager().createNamedQuery("getAllGCTotalCount", Long.class);

			count = query.getSingleResult();
		} catch (NoResultException nre) {
			logger.error("Exception occured while getting AllGCList :{}", ExceptionUtils.getMessage(nre));

		} catch (Exception exception) {
			logger.error("Exception occured while getting get All GC List :{}",
					ExceptionUtils.getStackTrace(exception));
			throw new DaoException(ExceptionUtils.getMessage(exception));
		}
		return count;
	}

	@Override
	public List<NELocation> getNELocationByParameter(String parameter, String value, String type) {
		logger.info("inside @getAllGCList");
		try {
			TypedQuery<NELocation> query = getEntityManager().createNamedQuery("getNELocationByParameter",
					NELocation.class);
			query.setParameter("paramName", parameter);
			query.setParameter("paramValue", value);
			query.setParameter("nelType", NELType.valueOf(type));
			return query.getResultList();
		} catch (NoResultException nre) {
			logger.error("Exception occured while getting nelocation ids :{}", ExceptionUtils.getMessage(nre));

		} catch (Exception exception) {
			logger.error("Exception occured while getting nelocation ids :{}", ExceptionUtils.getStackTrace(exception));
			throw new DaoException(ExceptionUtils.getMessage(exception));
		}
		return Collections.emptyList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<NELocationParameter> getValuesByIdAndParameterList(Integer id, List<String> parameterList) {
		List<NELocationParameter> neLocationList = null;
		logger.info("inside @getValuesByIdAndParameterList");
		try {
			Query query = getEntityManager().createNamedQuery("getValuesByParametersAndNELocationID");
			query.setParameter("pk", id);
			query.setParameter("parameterList", parameterList);
			neLocationList = query.getResultList();
			return neLocationList;
		} catch (NoResultException nre) {
			logger.error("Exception occured while getting values from id and parameters :{}",
					ExceptionUtils.getStackTrace(nre));
			nre.printStackTrace();
		} catch (Exception exception) {
			logger.error("Exception occured @getValuesByIdAndParameterList :{}",
					ExceptionUtils.getStackTrace(exception));
			exception.printStackTrace();
		}
		return neLocationList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<NELocationParameter> getNELocationParameterByParameterList(Integer id, List<String> parameterList) {
		logger.info("inside @getValuesByIdAndParameterList");
		try {
			Query query = getEntityManager().createNamedQuery("getNELocationParameterByParameterList");
			query.setParameter("pk", id);
			query.setParameter("parameterList", parameterList);
			List<NELocationParameter> neLocationParameterList = query.getResultList();
			return neLocationParameterList;
		} catch (NoResultException nre) {
			logger.error(
					"NoResultException occured while getting values from id : {}and parameters :{}. Exception : {}", id,
					parameterList, Utils.getStackTrace(nre));
			throw new DaoException("Unable to get values for parameters");
		} catch (Exception exception) {
			logger.error("Exception occured while getting values from id : {} and parameters :{}. Exception : {}", id,
					parameterList, Utils.getStackTrace(exception));
			throw new DaoException("Unable to get values for parameters");
		}
	}

	@Override
	public Long getValuesSetByParameterList(List<String> parameterList, String combVal, Integer nelpk) {
		Long count = 0l;
		try {
			String queryString = "select count(*) from(select group_concat(distinct parametername order by parametername), group_concat(value order by parametername) combVal , "
					+ "nelocationid_fk from NELocationParameter where parametername in(:parameterList) ";

			if (nelpk != null) {
				queryString = queryString + " and nelocationid_fk!=:nelpk ";
			}
			queryString = queryString + " group by nelocationid_fk) Temp where combVal=:combVal ";
			Query query = getEntityManager().createNativeQuery(queryString);
			query.setParameter("parameterList", parameterList);
			query.setParameter("combVal", combVal);
			if (nelpk != null) {
				query.setParameter("nelpk", nelpk);
			}
			Object singleResult = query.getSingleResult();
			count = Long.parseLong(String.valueOf(singleResult));
		} catch (NoResultException e) {
			logger.error("error in getInventoryLocationRackPodCount, NoResultException = {}", Utils.getStackTrace(e));
		} catch (Exception e) {
			logger.error("error in getInventoryLocationRackPodCount, err = {}", Utils.getStackTrace(e));
			throw new DaoException(e.getMessage());
		}

		return count;

	}

	@Override
	public List<Object[]> getGCList(String name, String status, Integer llimit, Integer ulimit) {
		List<Object[]> gcList = new ArrayList<>();
		try {
			String queryString = "select nelp1.value as gcType,nel.nelId,nel.technology,nel.vendor,nel.domain,nel.address,nel.pincode,"
					+ "nel.locationcode,nel.friendlyname,nel.nelocationid_pk,nel.nelstatus as status,gl4.name,nel.creationtime,nel.modificationtime,nelp2.value as managedByCdc"
					+ " from NELocation nel left join GeographyL4 gl4 on nel.geographyl4id_fk=gl4.geographyl4id_pk "
					+ " join  NELocationParameter nelp1 on nel.nelocationid_pk=nelp1.nelocationid_fk join  NELocationParameter nelp2 on  "
					+ " nel.nelocationid_pk=nelp2.nelocationid_fk where "
					+ " nel.neltype='GC' and nelp1.parametername='gcType' and nelp2.parametername='managed-by-cdc' ";

			if (StringUtils.isNotBlank(name)) {
				queryString = queryString + " and nel.nelid=:name ";
			}

			if (StringUtils.isNotBlank(status)) {
				queryString = queryString + " and nelp1.value=:statusValue ";
			}

			queryString = queryString + " order by nel.modificationtime desc ";
			Query query = getEntityManager().createNativeQuery(queryString);

			if (StringUtils.isNotBlank(name)) {
				query.setParameter("name", name);
			}

			if (StringUtils.isNotBlank(status)) {
				query.setParameter("statusValue", status);
			}

			if (llimit != null && ulimit != null && ulimit > llimit) {
				query.setFirstResult(llimit);
				query.setMaxResults((ulimit - llimit) + 1);
			}
			gcList = query.getResultList();

		} catch (NoResultException e) {
			logger.error("error in getInventoryLocationRackPodCount, NoResultException = {}", Utils.getStackTrace(e));
		} catch (Exception e) {
			logger.error("error in getInventoryLocationRackPodCount, err = {}", Utils.getStackTrace(e));
			throw new DaoException(e.getMessage());
		}

		return gcList;

	}

	@Override
	public Long getGCCount(String name, String status) {
		Long count = 0l;
		try {
			String queryString = "select count(nel.nelocationid_pk) from "
					+ "NELocation nel left join GeographyL4 gl4 on nel.geographyl4id_fk=gl4.geographyl4id_pk "
					+ "join  NELocationParameter nelp1 on nel.nelocationid_pk=nelp1.nelocationid_fk join  NELocationParameter nelp2 on  "
					+ "nel.nelocationid_pk=nelp2.nelocationid_fk where  "
					+ "nel.neltype='GC' and nelp1.parametername='gcType' and nelp2.parametername='managed-by-cdc'";

			if (StringUtils.isNotBlank(name)) {
				queryString = queryString + " and nel.nelid=:name ";
			}

			if (StringUtils.isNotBlank(status)) {
				queryString = queryString + " and nel.nelstatus=:statusValue ";
			}

			queryString = queryString + " order by nel.modificationtime desc ";
			Query query = getEntityManager().createNativeQuery(queryString);

			if (StringUtils.isNotBlank(name)) {
				query.setParameter("name", name);
			}

			if (StringUtils.isNotBlank(status)) {
				query.setParameter("statusValue", status);
			}

			Object singleResult = query.getSingleResult();
			count = Long.parseLong(String.valueOf(singleResult));
		} catch (NoResultException e) {
			logger.error("error in getInventoryLocationRackPodCount, NoResultException = {}", Utils.getStackTrace(e));
		} catch (Exception e) {
			logger.error("error in getInventoryLocationRackPodCount, err = {}", Utils.getStackTrace(e));
			throw new DaoException(e.getMessage());
		}

		return count;

	}

	public Long getNELocationGcCount(String name, String gcType, String status, String managedByCdc, Integer lLimit,
			Integer uLimit, String orderBy, String orderType) {
		Long count = 0l;
		try {
			CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
			Root<NELocationParameter> neLocationParameterRoot = criteriaQuery.from(NELocationParameter.class);

			Join<NELocationParameter, NELocation> neLocationJoinRoot = neLocationParameterRoot
					.join(ForesightConstants.NELOCATION, JoinType.LEFT);
			Join<NELocation, GeographyL4> geographyL4Root = neLocationJoinRoot.join(ForesightConstants.GEOGRAPHY_L4,
					JoinType.LEFT);
			criteriaQuery.select(builder.count(neLocationJoinRoot.get(ForesightConstants.ID))).distinct(true);

			List<Predicate> predicateList = new ArrayList<>();
			predicateList = getGCParameterPredicateList(name, gcType, status, managedByCdc, builder,
					neLocationParameterRoot, neLocationJoinRoot, predicateList);

			TypedQuery<Long> createQuery = getEntityManager()
					.createQuery(criteriaQuery.where(predicateList.toArray(new Predicate[] {})));
			count = createQuery.getSingleResult();

		} catch (NoResultException e) {
			logger.error("No result found to  get Network Service List occured due to NoResultException {}",
					e.getMessage());
		} catch (Exception e) {
			logger.error("Exception to get Network Service List due to Exception : {}", Utils.getStackTrace(e));
			throw new DaoException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}

		return count;
	}

	@Transactional
	@Override
	public List<Object[]> getNELocationGCList(String name, String gcType, String status, String managedByCdc,
			Integer llimit, Integer ulimit) {
		List<Object[]> neLocationWrapperList = new ArrayList<>();

		try {
			CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<Object[]> criteriaQuery = builder.createQuery(Object[].class);
			Root<NELocationParameter> neLocationParameterRoot = criteriaQuery.from(NELocationParameter.class);
			Join<NELocationParameter, NELocation> neLocationJoinRoot = neLocationParameterRoot.join("nelocation",
					JoinType.LEFT);
			Join<NELocation, GeographyL4> geographyL4Root = neLocationJoinRoot.join("geographyL4", JoinType.LEFT);
			criteriaQuery.multiselect(neLocationParameterRoot.get("value"),
					neLocationJoinRoot.get(ForesightConstants.NEL_ID),
					neLocationJoinRoot.get(ForesightConstants.TECHNOLOGY),
					neLocationJoinRoot.get(ForesightConstants.VENDOR_SMALL),
					neLocationJoinRoot.get(ForesightConstants.DOMAIN),
					neLocationJoinRoot.get(ForesightConstants.ADDRESS),
					neLocationJoinRoot.get(ForesightConstants.PINCODE),
					neLocationJoinRoot.get(ForesightConstants.LOCATION_CODE),
					neLocationJoinRoot.get(ForesightConstants.FRIENDLY_NAME),
					neLocationJoinRoot.get(ForesightConstants.ID), geographyL4Root.get(ForesightConstants.NAME),
					neLocationJoinRoot.get("parameterName"), neLocationJoinRoot.get(ForesightConstants.MODIFIED_TIME),
					neLocationJoinRoot.get(ForesightConstants.CREATEDTIME));

			List<Predicate> predicateList = new ArrayList<>();
			predicateList = getGCParameterPredicateList(name, gcType, status, managedByCdc, builder,
					neLocationParameterRoot, neLocationJoinRoot, predicateList);

			TypedQuery<Object[]> createQuery = getEntityManager()
					.createQuery(criteriaQuery.where(predicateList.toArray(new Predicate[] {})));
			if (llimit != null && ulimit != null && ulimit > llimit) {
				createQuery.setFirstResult(llimit);
				createQuery.setMaxResults((ulimit - llimit) + 1);
			}
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

	private List<Predicate> getGCParameterPredicateList(String name, String gcType, String status, String managedByCdc,
			CriteriaBuilder builder, Root<NELocationParameter> neLocationParameterRoot,
			Join<NELocationParameter, NELocation> neLocationJoinRoot, List<Predicate> predicateList) {
		List<String> parameterNameList = new ArrayList<>();
		parameterNameList.add("status");
		parameterNameList.add("gcType");
		parameterNameList.add("managed-by-cdc");

		predicateList.add(builder.equal(neLocationJoinRoot.get(ForesightConstants.NEL_TYPE), "GC"));
		predicateList.add(neLocationParameterRoot.get("parameterName").in(parameterNameList));
		if (!Utils.isBlank(name)) {
			predicateList.add(builder.like(neLocationJoinRoot.get(ForesightConstants.NEL_ID), "%" + name + "%"));
		}
		/*
		 * if(!Utils.isBlank(gcType)){
		 * //predicateList.add(builder.equal(neLocationParameterRoot.get("parameterName"
		 * ), "gcType"));
		 * predicateList.add(builder.like(neLocationParameterRoot.get("value"), "%" +
		 * gcType + "%")); }
		 * 
		 * if(!Utils.isBlank(status)){
		 * //predicateList.add(builder.equal(neLocationParameterRoot.get("parameterName"
		 * ), "status"));
		 * predicateList.add(builder.like(neLocationParameterRoot.get("value"), "%" +
		 * status + "%")); } if(!Utils.isBlank(managedByCdc)){
		 * //predicateList.add(builder.equal(neLocationParameterRoot.get("parameterName"
		 * ), "managed-by-cdc"));
		 * predicateList.add(builder.like(neLocationParameterRoot.get("value"), "%" +
		 * gcType + "%")); }
		 */

		return predicateList;
	}

}

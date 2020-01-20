package com.inn.foresight.module.nv.workorder.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder.TemplateType;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;
import com.inn.foresight.module.nv.workorder.dao.IWORecipeMappingDao;
import com.inn.foresight.module.nv.workorder.model.WORecipeMapping;
import com.inn.foresight.module.nv.workorder.recipe.wrapper.WORecipeMappingWrapper;
import com.inn.foresight.module.nv.workorder.utils.WorkorderDashboardUtils;
import com.inn.foresight.module.nv.workorder.wrapper.WORecipeWrapper;

/** The Class WORecipeMappingDaoImpl. */
@Repository("WORecipeMappingDaoImpl")
public class WORecipeMappingDaoImpl extends HibernateGenericDao<Integer, WORecipeMapping>
		implements IWORecipeMappingDao {

	/** The logger. */
	private Logger logger = LogManager.getLogger(WORecipeMappingDaoImpl.class);

	/** Instantiates a new WO recipe mapping dao impl. */
	public WORecipeMappingDaoImpl() {
		super(WORecipeMapping.class);
	}

	/**
	 * Gets List of All WORecipeMapping object corresponding to userId.
	 *
	 * @param userId the user id
	 * @return List<WORecipeMapping>
	 * @throws RestException the rest exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<WORecipeMapping> getRecipeWoByUser(Integer userId) {
		logger.info("Going to get WORecipeMapping List from userId {}", userId);

		Query query = getEntityManager().createNamedQuery("getRecipeWOByUser");
		query.setParameter(NVWorkorderConstant.USER_ID, userId);
		try {
			List<WORecipeMapping> list = query.getResultList();
			logger.info(NVWorkorderConstant.RESULT_SIZE_LOGGER, list.size());
			return list;
		} catch (NoResultException e) {
			throw new RestException(NVWorkorderConstant.DATA_NOT_FOUND);
		}
	}

	/**
	 * Gets All the WORecipeMapping objects corresponding to WorkorderName.
	 *
	 * @param workorderIdList the workorder name
	 * @param templateTypes   the template type
	 * @param modifiedTime    the modified time
	 * @param workorderId     the workorder id
	 * @return List<WORecipeMapping> all WORecipeMapping that matches the
	 *         WorkorderNames passed in the list
	 * @throws RestException the rest exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<WORecipeMapping> getRecipeWOByWOIdList(List<String> workorderIdList, List<String> templateTypes,
			Long modifiedTime, Integer id) {
		logger.info(
				"Going to get WORecipeMapping List from workorderIdList size {}, templateType {}, modifiedTime {}, id {}",
				workorderIdList.size(), templateTypes, modifiedTime, id);
		Query query = getEntityManager().createNamedQuery("getRecipeWOByWOName");
		query.setParameter(NVWorkorderConstant.WORKORDER_ID, workorderIdList);
		query.setParameter(NVWorkorderConstant.WORKORDER_TYPE, getListOfTemplateType(templateTypes));
		query.setParameter(NVWorkorderConstant.MODIFIED_TIME, new Date(modifiedTime));
		addWorkorderIdFilter(id);

		try {
			List<WORecipeMapping> list = query.getResultList();
			logger.info(NVWorkorderConstant.RESULT_SIZE_LOGGER, list.size());
			return list;
		} catch (NoResultException e) {
			throw new DaoException(NVWorkorderConstant.DATA_NOT_FOUND);
		}
	}

	private List<TemplateType> getListOfTemplateType(List<String> templateTypes) {
		List<TemplateType> list = new ArrayList<>();
		for (String templateType : templateTypes) {
			list.add(TemplateType.valueOf(templateType));
		}
		return list;
	}

	/**
	 * Getting List of WORecipeMapping from GenericWorkorderId
	 * 
	 * @param workorderId : PK of GenericWorkorder
	 * @return WORecipeMapping List
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<WORecipeMapping> getWORecipeByGWOId(Integer workorderId) {
		logger.info("Going to get WORecipeMapping List from workorderId {}", workorderId);
		Query query = getEntityManager().createNamedQuery("getWORecipeByGWOId");
		query.setParameter(NVWorkorderConstant.WORKORDER_ID, workorderId);
		try {
			List<WORecipeMapping> list = query.getResultList();
			logger.info(NVWorkorderConstant.RESULT_SIZE_LOGGER, list.size());
			return list;
		} catch (NoResultException e) {
			throw new DaoException(NVWorkorderConstant.DATA_NOT_FOUND);
		}
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<WORecipeMapping> getWORecipeByGWOIds(List<Integer> workorderIds) {
		logger.info("Going to get WORecipeMapping List from workorderId {}", workorderIds);
		Query query = getEntityManager().createNamedQuery("getWORecipeByGWOIds");
		query.setParameter(NVWorkorderConstant.WORKORDER_ID, workorderIds);
		try {
			List<WORecipeMapping> list = query.getResultList();
			logger.info(NVWorkorderConstant.RESULT_SIZE_LOGGER, list.size());
			return list;
		} catch (NoResultException e) {
			throw new DaoException(NVWorkorderConstant.DATA_NOT_FOUND);
		}
	}

	/**
	 * Query filter, this Method Enables to filter Data if workorderId is not null.
	 *
	 * @param workorderId the workorder id
	 */
	private void addWorkorderIdFilter(Integer workorderId) {
		if (workorderId != null) {
			Session s = (Session) getEntityManager().getDelegate();
			Filter filter = s.enableFilter(NVWorkorderConstant.WORKORDER_ID_FILTER);
			filter.setParameter(NVWorkorderConstant.WORKORDER_ID, workorderId);
		}
	}

	@Override
	public WORecipeMapping getWORecipeMappingById(Integer woRecipeMappingId) {
		logger.info("Getting WorkOrder Recipe Mapping By Id: {}", woRecipeMappingId);
		try {
			if (woRecipeMappingId != null) {
				Query query = getEntityManager().createNamedQuery("getWORecipeMappingById");
				query.setParameter(NVWorkorderConstant.WO_RECIPE_MAPPING_ID, woRecipeMappingId);
				return (WORecipeMapping) query.getSingleResult();
			} else {
				throw new DaoException(NVWorkorderConstant.EXCEPTION_SOMETHING_WENT_WRONG);
			}
		} catch (Exception e) {
			logger.error("Error while getting WorkOrder Recipe Mapping By Id: {}, {}", woRecipeMappingId,
					ExceptionUtils.getStackTrace(e));
			throw new DaoException(NVWorkorderConstant.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WORecipeMapping> geKpiAndEventString(Integer workorderId, List<Integer> recipeList) {
		logger.info("Going to get geKpiAndEventString List from workorderId {}", workorderId);
		Query query = getEntityManager().createNamedQuery("getWORecipeMappingByGWOIdAndListofId");
		query.setParameter(NVWorkorderConstant.WORKORDER_ID, workorderId).setParameter(NVWorkorderConstant.RECIPELIST,
				recipeList);
		try {
			List<WORecipeMapping> list = query.getResultList();
			logger.info(NVWorkorderConstant.RESULT_SIZE_LOGGER, list.size());
			return list;

		} catch (NoResultException e) {
			throw new RestException(NVWorkorderConstant.DATA_NOT_FOUND);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WORecipeMapping> getWorkOrderDetailByImei(String imei, Long startTime, Long endTime,
			TemplateType templateType) {
		Query query = getEntityManager().createNamedQuery("getWorkOrderDetailByImei").setParameter("imei", imei)
				.setParameter("startTime", new Date(startTime)).setParameter("endTime", new Date(endTime))
				.setParameter("templateType", templateType);
		try {
			List<WORecipeMapping> list = query.getResultList();
			logger.info(NVWorkorderConstant.RESULT_SIZE_LOGGER, list != null ? list.size() : 0);
			return list;
		} catch (NoResultException e) {
			throw new DaoException(NVWorkorderConstant.DATA_NOT_FOUND);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WORecipeMappingWrapper> getWORecipeMappingByGeographyAndDate(String geographyLevel, Integer geographyId,
			Date date) {
		logger.info("Going to get WORecipeMapping List from date {}, geographyId {}, geographyLevel {}",
				WorkorderDashboardUtils.getDateString(date, NVWorkorderConstant.DD_MM_YY), geographyId, geographyLevel);
		List<WORecipeMappingWrapper> list = new ArrayList<>();
		String workorderQuery = null;
		workorderQuery = "getWORecipeListByDateGeography";
		Query query = getEntityManager().createNamedQuery(workorderQuery).setParameter("date",
				WorkorderDashboardUtils.getDateString(date, NVWorkorderConstant.DD_MM_YY));
		try {
			String geographyFilterName = getGeographyFilterName(geographyLevel);
			if (geographyFilterName != null) {
				applyGeographyFilter(geographyFilterName, Arrays.asList(geographyId));
			}
			list = query.getResultList();
			disableFilter(geographyFilterName);
		} catch (Exception e) {
			throw new DaoException(ExceptionUtils.getMessage(e));
		}
		logger.info("Getting List size {}", list.size());
		return list;
	}

	private void applyGeographyFilter(String filterName, List<Integer> geographyId) {
		if (filterName != null && geographyId != null) {
			Session s = (Session) getEntityManager().getDelegate();
			Filter filter = s.enableFilter(filterName);
			filter.setParameterList("geographyId", geographyId);
		}
	}

	private String getGeographyFilterName(String geographyLevel) {
		if (geographyLevel != null) {
			if (geographyLevel.equalsIgnoreCase(ForesightConstants.GEOGRAPHY_L1)) {
				return "GeographyL1Filter";
			} else if (geographyLevel.equalsIgnoreCase(ForesightConstants.GEOGRAPHY_L2)) {
				return "GeographyL2Filter";
			} else if (geographyLevel.equalsIgnoreCase(ForesightConstants.GEOGRAPHY_L3)) {
				return "GeographyL3Filter";
			} else if (geographyLevel.equalsIgnoreCase(ForesightConstants.GEOGRAPHY_L4)) {
				return "GeographyL4Filter";
			}
		}
		return null;
	}

	private void disableFilter(String filterName) {
		Session s = (Session) getEntityManager().getDelegate();
		s.disableFilter(filterName);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WORecipeWrapper> getWoIdsByforExecutedCountofRecipe() {
		logger.info("Going to getWoIdsByforExecutedCountofRecipe ");
		List<WORecipeWrapper> list = new ArrayList<>();
		Query query = getEntityManager().createNamedQuery("getWoidsListByRecipeCount");
		try {
			list = query.getResultList();
		} catch (Exception e) {
			throw new DaoException(ExceptionUtils.getMessage(e));
		}
		logger.info("Getting List size of woIds {}", list.size());
		return list;
	}

	/**
	 * Gets List of All non Deleted WO List corresponding to recipeId.
	 *
	 * @param recipeId
	 * @return List<WORecipeMapping>
	 * @throws RestException the rest exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<WORecipeMapping> getNonDeletedWOByRecipeId(Integer recipeId) {
		logger.info("Going to get getRecipeWoListByRecipeId List from recipeId {}", recipeId);
		try {
			Query query = getEntityManager().createNamedQuery("getNonDeletedWOByRecipeId");
			query.setParameter(NVWorkorderConstant.RECIPE_ID, recipeId);
			List<WORecipeMapping> list = query.getResultList();
			logger.info(NVWorkorderConstant.RESULT_SIZE_LOGGER, list.size());
			return list;
		} catch (Exception e) {
			logger.info("Error while getting RecipeWOList {} " + e.getMessage());
			throw new DaoException(e.getMessage());
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void updateWoRecipeMapping(WORecipeMapping recipeMapping) {
		try {
			update(recipeMapping);
		} catch (NoResultException | DaoException e) {
			logger.error("Error in update object into table {}", Utils.getStackTrace(e));
			throw new RestException(NVWorkorderConstant.DATA_NOT_FOUND);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WORecipeMapping> getWoRecipeMappingByWorkOrderId(Integer workrorderId) {
		try {

			Query query = getEntityManager().createNamedQuery("findWoRecipeMappingByWorkOrderId")
					.setParameter("workrorderId", workrorderId);
			List<WORecipeMapping> list = query.getResultList();
			logger.info(NVWorkorderConstant.RESULT_SIZE_LOGGER, list != null ? list.size() : 0);
			return list;
		} catch (NoResultException e) {
			throw new RestException(NVWorkorderConstant.DATA_NOT_FOUND);
		}
	}

}

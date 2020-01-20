package com.inn.foresight.module.nv.core.workorder.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.cxf.jaxrs.ext.search.SearchContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.utils.ExceptionUtil;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.NVConstant;
import com.inn.foresight.module.nv.core.workorder.constants.GenericWorkorderConstants;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder.Status;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder.TemplateType;
import com.inn.foresight.module.nv.core.workorder.wrapper.WorkorderCountWrapper;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;
import com.inn.foresight.module.nv.workorder.utils.NVWorkorderUtils;
import com.inn.foresight.module.nv.workorder.wrapper.NVWorkorderWrapper;

/** The Class GenericWorkorderDaoImpl. */



@Repository("GenericWorkorderDaoImpl")
public class GenericWorkorderDaoImpl extends HibernateGenericDao<Integer, GenericWorkorder>
		implements IGenericWorkorderDao {

	private static final String STATUS_LIST = "statusList";
	private static final String TEMPLATE_LIST = "templateList";
	private static final String ERROR_INSIDE_THE_METHOD_GET_LIST_OF_WORK_ORDER_ID_BY_BUILDING_ID = "Error inside the method getListOfWorkOrderIdByBuildingId {}";
	/** The logger. */
	private Logger logger = LogManager.getLogger(GenericWorkorderDaoImpl.class);

	/** Instantiates a new generic workorder dao impl. */
	public GenericWorkorderDaoImpl() {
		super(GenericWorkorder.class);
	}

	/**
	 * Find by pk.
	 *
	 * @param entityPk
	 *            the entity pk
	 * @return the generic workorder
	 * @throws DaoException
	 *             the dao exception
	 */
	@Override
	public GenericWorkorder findByPk(Integer entityPk) {
		try {
			return super.findByPk(entityPk);
		} catch (Exception e) {
			throw new DaoException(ExceptionUtil.generateExceptionCode(ForesightConstants.DAO, GenericWorkorderConstants.GENERIC_WORKORDER, e));
		}
	}

	/**
	 * Creates the.
	 *
	 * @param anEntity
	 *            the an entity
	 * @return the generic workorder
	 * @throws DaoException
	 *             the dao exception
	 */
	@Override
	public GenericWorkorder create(GenericWorkorder anEntity) {
		try {
			return super.create(anEntity);
		} catch (DaoException e) {
			throw new DaoException(ExceptionUtil.generateExceptionCode(ForesightConstants.DAO,  GenericWorkorderConstants.GENERIC_WORKORDER, e));
		}
	}

	/**
	 * Search.
	 *
	 * @param ctx
	 *            the ctx
	 * @param maxLimit
	 *            the max limit
	 * @param minLimit
	 *            the min limit
	 * @param orderby
	 *            the orderby
	 * @param orderType
	 *            the order type
	 * @return the list
	 */
	@Override
	public List<GenericWorkorder> search(SearchContext ctx, Integer maxLimit, Integer minLimit, String orderby,
			String orderType) {
		logger.info("Going to call search Context ");
		return super.search(ctx, maxLimit, minLimit, orderby, orderType);
	}

	/**
	 * Update.
	 *
	 * @param anEntity
	 *            the an entity
	 * @return the generic workorder
	 * @throws DaoException
	 *             the dao exception
	 */
	@Override
	public GenericWorkorder update(GenericWorkorder anEntity) {
		try {
			return super.update(anEntity);
		} catch (DaoException e) {
			throw new DaoException(ExceptionUtil.generateExceptionCode(ForesightConstants.DAO,  GenericWorkorderConstants.GENERIC_WORKORDER, e));
		}
	}

	/**
	 * Delete.
	 *
	 * @param anEntity
	 *            the an entity
	 * @throws DaoException
	 *             the dao exception
	 */
	@Override
	public void delete(GenericWorkorder anEntity) {
		try {
			super.delete(anEntity);
		} catch (DaoException e) {
			throw new DaoException(ExceptionUtil.generateExceptionCode(ForesightConstants.DAO, "GenericWorkorder", e));
		}
	}

	/**
	 * Find all.
	 *
	 * @return the list
	 * @throws DaoException
	 *             the dao exception
	 */
	@Override
	public List<GenericWorkorder> findAll() {
		try {
			return super.findAll();
		} catch (DaoException e) {
			throw new DaoException(ExceptionUtil.generateExceptionCode(ForesightConstants.DAO, "GenericWorkorder", e));
		}
	}

	/**
	 * Gets the List of all Generic Workorder Names which contains the passed
	 * workorder name.
	 *
	 * @param workorderName
	 *            the workorder name
	 * @return List<String> Workorder names from Db which contains(workorderName)
	 * @throws DaoException
	 *             the dao exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getWorkorderIdListFromWOId(String workorderId) {
		Query query = getEntityManager().createNamedQuery("getWorkorderIdListFromWOId")
										.setParameter("workorderId", workorderId);
		try {
			return query.getResultList();
		} catch (Exception e) {
			throw new DaoException(
					ExceptionUtil.generateExceptionCode(ForesightConstants.DAO, GenericWorkorder.class.getName(), e));
		}
	}

	/**
	 * Gets GenericWorkorder object in which the workorderName contains the passed
	 * Workorder Name.
	 *
	 * @param workorderId
	 *            the workorder name
	 * @return GenericWorkorder object from Db which contains(workorderName)
	 * @throws DaoException
	 *             the dao exception
	 */
	@Override
	public GenericWorkorder getWorkorderByWOId(String workorderId) {
		Query query = getEntityManager().createNamedQuery("getWorkorderByWOId")
										.setParameter("workorderId", workorderId);
		try {
			return (GenericWorkorder) query.getSingleResult();
		} catch (Exception e) {
			throw new DaoException(
					ExceptionUtil.generateExceptionCode(ForesightConstants.DAO, GenericWorkorder.class.getName(), e));
		}
	}



	private void applyArchiveFilter(Boolean isArchived) {
		if (isArchived != null) {
			Session s = (Session) getEntityManager().getDelegate();
			Filter filter = s.enableFilter(GenericWorkorderConstants.ARCHIVED_FILTER);
			filter.setParameter(GenericWorkorderConstants.IS_ARCHIVED, isArchived);
		}
	}

	private void applyCreatorFilter(Integer creatorId) {
		if (creatorId != null) {
			Session s = (Session) getEntityManager().getDelegate();
			Filter filter = s.enableFilter(GenericWorkorderConstants.GW_CREATOR_FILTER);
			filter.setParameter("creatorId", creatorId);
		}
	}

	private void disableFilter(String filterName) {
		Session s = (Session) getEntityManager().getDelegate();
		s.disableFilter(filterName);
	}

	private void applyGeographyFilter(String filterName, List<Integer> geographyId) {
		if (filterName != null && geographyId != null) {
			Session s = (Session) getEntityManager().getDelegate();
			Filter filter = s.enableFilter(filterName);
			filter.setParameterList("geographyId", geographyId);
		}
	}

	
	
	/**
	 * Gets Total Workorder count for template List and Status List.
	 * 
	 * @return Integer Workorder Count
	 * @throws RestException
	 */
	@Override
	public Long getTotalWorkorderCount(List<TemplateType> templateList, List<Status> statusList,String searchString,Boolean isArchived) {
		logger.info("Going to get Workorder Count for templateList {}, statusList {} ,searchString {} ,isArchived {},", templateList, statusList,searchString,isArchived);
		Long count=0L;
		try {
			Query query = getEntityManager().createNamedQuery("getTotalCountForNVWO").setParameter(GenericWorkorderConstants.TEMPLATE_LIST, templateList).setParameter(GenericWorkorderConstants.STATUS_LIST, statusList);
			applyArchiveFilter(isArchived);
			applySearchFilter(searchString);
			count=(Long) query.getSingleResult();
			disableFilter(GenericWorkorderConstants.SEARCH_FILTER);
			disableFilter(GenericWorkorderConstants.ARCHIVED_FILTER);
		} catch (Exception e) {
			throw new DaoException(e.getMessage());
		}
		return count;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<GenericWorkorder> findAllWorkorder(Integer lLimit, Integer uLimit, List<TemplateType> templateList,
			List<Status> statusList, String searchString, String geographyLevel, Integer geographyId, Integer creatorId,
			Long startTimestamp, Long endTimestamp) {
		logger.info("Getting All Workorder with lLimit {}, uLimit {}, templateList {}, statusList {}, geographyLevel {}, geographyId {}, creatorId {} , startTimestamp {} , endTimestamp {} ",lLimit, uLimit, templateList, statusList, geographyLevel, geographyId, creatorId, startTimestamp,
				endTimestamp);
		Query query = getEntityManager().createNamedQuery(GenericWorkorderConstants.FIND_ALL_WOKRORDER_WITHIN_TIMERANGE).setParameter(GenericWorkorderConstants.TEMPLATE_LIST, templateList).setParameter(GenericWorkorderConstants.STATUS_LIST, statusList).setParameter(GenericWorkorderConstants.START_TIME, new Date(startTimestamp)).setParameter(GenericWorkorderConstants.END_TIME, new Date(endTimestamp));
		if (uLimit != null && lLimit != null) {
			query	.setFirstResult(lLimit)
					.setMaxResults(uLimit - lLimit + 1);
		}
		applySearchFilter(searchString);
		applyCreatorFilter(creatorId);
		String geographyFilterName = getGeographyFilterName(geographyLevel);
		applyGeographyFilter(geographyFilterName, Arrays.asList(geographyId));
		try {
			List<GenericWorkorder> list = query.getResultList();
			disableFilter(GenericWorkorderConstants.ARCHIVED_FILTER);
			disableFilter(GenericWorkorderConstants.GW_CREATOR_FILTER);
			disableFilter(geographyFilterName);
			logger.info(" Inside findAllWorkorder() getting list size {}", list.size());
			return list;
		} catch (Exception e) {
			throw new DaoException(e.getMessage());
		}
	}

	@Override
	public Long getAllWorkorderCount(String geographyLevel, Integer geographyId, Date startDate, Date endDate,
			List<TemplateType> templateList) {
		logger.info(
				"Getting getAllWorkorderCount with geographyLevel {}, geographyId {}, startDate {}, endDate {}, templateList {} ",
				geographyLevel, geographyId, startDate, endDate, templateList);

		Long count = 0L;
		Query query = getEntityManager().createNamedQuery(GenericWorkorderConstants.GET_ALL_WO_COUNT_BY_DATE_QUERY)
										.setParameter(GenericWorkorderConstants.TEMPLATE_LIST, templateList)
										.setParameter(GenericWorkorderConstants.DATE, Utils.getDateString(endDate));
		String geographyFilterName = getGeographyFilterName(geographyLevel);
		applyGeographyFilter(geographyFilterName, Arrays.asList(geographyId));
		try {
			count = (Long) query.getSingleResult();
			disableFilter(geographyFilterName);
		} catch (Exception e) {
			throw new DaoException(ExceptionUtils.getMessage(e));
		}
		return count;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WorkorderCountWrapper> getADHOCWorkorderDayWiseCount(String geographyLevel, Integer geographyId,
			Date startDate, Date endDate, List<TemplateType> templateList) {
		logger.info(
				"Getting getADHOCWorkorderDayWiseCount with geographyLevel {}, geographyId {}, startDate {}, endDate {}, templateList {} ",
				geographyLevel, geographyId, startDate, endDate, templateList);
		List<WorkorderCountWrapper> list;
		Query query = getEntityManager().createNamedQuery(GenericWorkorderConstants.GET_ADHOC_WO_DAYWISE_COUNT_QUERY)
										.setParameter(GenericWorkorderConstants.TEMPLATE_LIST, templateList)
										.setParameter(GenericWorkorderConstants.START_DATE, startDate)
										.setParameter(GenericWorkorderConstants.END_DATE, endDate);
		String geographyFilterName = getGeographyFilterName(geographyLevel);
		if (geographyFilterName != null) {
			applyGeographyFilter(geographyFilterName, Arrays.asList(geographyId));
		}
		try {
			list = query.getResultList();
			disableFilter(geographyFilterName);
		} catch (Exception e) {
			throw new DaoException(ExceptionUtils.getMessage(e));
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WorkorderCountWrapper> getWOCountByTemplateTypeAndStatus(String geographyLevel, Integer geographyId,
			Date startDate, Date endDate, List<TemplateType> woibTemplateList, List<Status> statusList) {
		logger.info(
				"Getting getWOCountByTemplateTypeAndStatus with geographyLevel {}, geographyId {}, startDate {}, endDate {}, templateList {},statusList {} ",
				geographyLevel, geographyId, startDate, endDate, woibTemplateList, statusList);
		List<WorkorderCountWrapper> list;
		Query query = getEntityManager().createNamedQuery(
				GenericWorkorderConstants.GET_WO_COUNT_BY_TEMPLATE_TYPE_AND_STATUS_BY_DATE_QUERY)
										.setParameter(GenericWorkorderConstants.TEMPLATE_LIST, woibTemplateList)
										.setParameter(GenericWorkorderConstants.STATUS_LIST, statusList)
										.setParameter(GenericWorkorderConstants.DATE, Utils.getDateString(endDate));
		String geographyFilterName = getGeographyFilterName(geographyLevel);
		if (geographyFilterName != null) {
			applyGeographyFilter(geographyFilterName, Arrays.asList(geographyId));
		}
		try {
			list = query.getResultList();
			disableFilter(geographyFilterName);
		} catch (Exception e) {
			throw new DaoException(ExceptionUtils.getMessage(e));
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WorkorderCountWrapper> getDayWiseWOCountByStatus(String geographyLevel, Integer geographyId,
			Date startDate, Date endDate, List<TemplateType> templateTypeList, List<Status> statusList) {
		logger.info(
				"Getting getDayWiseWOCountByStatus with geographyLevel {}, geographyId {}, startDate {}, endDate {}, templateList {},statusList {} ",
				geographyLevel, geographyId, startDate, endDate, templateTypeList, statusList);
		List<WorkorderCountWrapper> list;
		Query query = getEntityManager().createNamedQuery(
				GenericWorkorderConstants.GET_DAY_WISE_WO_COUNT_BY_STATUS_QUERY)
										.setParameter(GenericWorkorderConstants.TEMPLATE_LIST, templateTypeList)
										.setParameter(GenericWorkorderConstants.STATUS_LIST, statusList)
										.setParameter(GenericWorkorderConstants.START_DATE, startDate)
										.setParameter(GenericWorkorderConstants.END_DATE, endDate);
		String geographyFilterName = getGeographyFilterName(geographyLevel);
		if (geographyFilterName != null) {
			applyGeographyFilter(geographyFilterName, Arrays.asList(geographyId));
		}
		try {
			list = query.getResultList();
			disableFilter(geographyFilterName);
		} catch (Exception e) {
			throw new DaoException(ExceptionUtils.getMessage(e));
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
		public List<WorkorderCountWrapper> getDayWiseAssignedWOCount(String geographyLevel, Integer geographyId,
			Date startDate, Date endDate, List<TemplateType> woTemplateAssignedToUser) {

		logger.info(
				"Getting getDayWiseAssignedWOCount with geographyLevel {}, geographyId {}, startDate {}, endDate {}, templateList {}",
				geographyLevel, geographyId, startDate, endDate, woTemplateAssignedToUser);
		List<WorkorderCountWrapper> list;
		Query query = getEntityManager().createNamedQuery(
				GenericWorkorderConstants.GET_DAY_WISE_ASSIGNED_WO_COUNT_QUERY)
										.setParameter(GenericWorkorderConstants.TEMPLATE_LIST, woTemplateAssignedToUser)
										.setParameter(GenericWorkorderConstants.START_DATE, startDate)
										.setParameter(GenericWorkorderConstants.END_DATE, endDate);
		String geographyFilterName = getGeographyFilterName(geographyLevel);
		if (geographyFilterName != null) {
			applyGeographyFilter(geographyFilterName, Arrays.asList(geographyId));
		}
		try {
			list = query.getResultList();
			disableFilter(geographyFilterName);
		} catch (Exception e) {
			throw new DaoException(ExceptionUtils.getMessage(e));
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WorkorderCountWrapper> getDueWorkorderDayWiseCount(String geographyLevel, Integer geographyId,
			Date startDate, Date endDate, List<Status> statusList) {
		logger.info(
				"Getting getDueWorkorderDayWiseCount with geographyLevel {}, geographyId {}, startDate {}, endDate {}, statusList {}",
				geographyLevel, geographyId, startDate, endDate, statusList);
		List<WorkorderCountWrapper> list;
		Query query = getEntityManager().createNamedQuery(GenericWorkorderConstants.GET_DUE_WO_DAY_WISE_COUNT_QUERY)
										.setParameter(GenericWorkorderConstants.STATUS_LIST, statusList)
										.setParameter(GenericWorkorderConstants.START_DATE, startDate)
										.setParameter(GenericWorkorderConstants.END_DATE, endDate);
		String geographyFilterName = getGeographyFilterName(geographyLevel);
		if (geographyFilterName != null) {
			applyGeographyFilter(geographyFilterName, Arrays.asList(geographyId));
		}
		try {
			list = query.getResultList();
			disableFilter(geographyFilterName);
		} catch (Exception e) {
			throw new DaoException(ExceptionUtils.getMessage(e));
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WorkorderCountWrapper> getDueWorkorderList(String geographyLevel, Integer geographyId, Date startDate,
			Date endDate, List<Status> statusList, Integer llimit, Integer ulimit) {

		logger.info(
				"Getting getDueWorkorderList with geographyLevel {}, geographyId {}, startDate {}, endDate {}, statusList {},llimit {} ,ulimit {}",
				geographyLevel, geographyId, startDate, endDate, statusList, llimit, ulimit);

		List<WorkorderCountWrapper> list;
		Query query = getEntityManager().createNamedQuery(GenericWorkorderConstants.GET_DUE_WO_LIST_BY_DATE_QUERY)
										.setParameter(GenericWorkorderConstants.STATUS_LIST, statusList)
										.setParameter(GenericWorkorderConstants.DATE, Utils.getDateString(endDate));
		if (llimit != null && ulimit != null) {
			query.setFirstResult(llimit);
			query.setMaxResults(ulimit);
		}
		String geographyFilterName = getGeographyFilterName(geographyLevel);
		if (geographyFilterName != null) {
			applyGeographyFilter(geographyFilterName, Arrays.asList(geographyId));
		}
		try {
			list = query.getResultList();
			disableFilter(geographyFilterName);
		} catch (Exception e) {
			throw new DaoException(ExceptionUtils.getMessage(e));
		}
		return list;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<GenericWorkorder> findAllWorkorder(Integer lLimit, Integer uLimit, List<TemplateType> templateList,
			List<Status> statusList, String searchString, String geographyLevel, List<Integer> geographyIdList,
			Integer creatorId, Long startTimestamp, Long endTimestamp, String entityType, String entityValue) {
		logger.info("Getting All Workorder with lLimit {}, uLimit {}, templateList {}, statusList {}, geographyLevel {}, geographyId {}, creatorId {} , startTimestamp {} , endTimestamp {} ",lLimit, uLimit, templateList, statusList, geographyLevel, geographyIdList, creatorId, startTimestamp,
				endTimestamp);
		Query query = getEntityManager().createNamedQuery("findAllWorkorderWithinTimeRange")
										.setParameter(GenericWorkorderConstants.TEMPLATE_LIST, templateList)
										.setParameter(GenericWorkorderConstants.STATUS_LIST, statusList)
										.setParameter(GenericWorkorderConstants.START_TIME, new Date(startTimestamp))
										.setParameter(GenericWorkorderConstants.END_TIME, new Date(endTimestamp));

		if (uLimit != null && lLimit != null) {
			query	.setFirstResult(lLimit)
					.setMaxResults(uLimit - lLimit + 1);
		}
		applySearchFilter(searchString);
		applyCreatorFilter(creatorId);
		String geographyFilterName = getGeographyFilterName(geographyLevel);
		if (geographyFilterName != null) {
			applyGeographyFilter(geographyFilterName, geographyIdList);
		}
		List<String> entityValueList=null;
		if(entityValue.contains(NVWorkorderConstant.FILE_DIRECTORY_SEPERATOR)){
			String[] entityValues = entityValue.split(NVWorkorderConstant.FILE_DIRECTORY_SEPERATOR);
			entityValueList = Arrays.asList(entityValues);
		}else{
			entityValueList= Arrays.asList(entityValue);
		}
		if (entityType != null) {
			if (!entityValue.equalsIgnoreCase(ForesightConstants.ALL))
				applyGWOMetaFilter(entityType, entityValueList);
			else {
				applyGWOMetaFilter(entityType, Arrays.asList(GenericWorkorderConstants.TDD, GenericWorkorderConstants.FDD));
			}
		}

		try {
			logger.info("Going to execute the query");
			List<GenericWorkorder> list = query.getResultList();
			disableFilter(GenericWorkorderConstants.SEARCH_FILTER);
			disableFilter(GenericWorkorderConstants.GW_CREATOR_FILTER);
			disableFilter(geographyFilterName);
			logger.info(" Inside  findAllWorkorder() getting list size {}", list.size());
			return list;
		} catch (Exception e) {
			throw new DaoException(e.getMessage());
		}
	}

	

	private void applyGWOMetaFilter(String entityType, List<String> entityValue) {
		if (entityType != null && entityValue != null) {
			Session s = (Session) getEntityManager().getDelegate();
			Filter filter = s.enableFilter(NVConstant.GWO_META_FILTER);
			filter.setParameter("entitytype", entityType);
			filter.setParameterList("entityvalue", entityValue);
		}
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<GenericWorkorder> getWorkorderListForReport(String geographyLevel, Integer geographyId, Date date) {
		logger.info("Getting getWorkorderListForReport with geographyLevel {}, geographyId {}, date  {}",
				geographyLevel, geographyId, date);

		List<GenericWorkorder> list;
		Query query = getEntityManager().createNamedQuery(GenericWorkorderConstants.GET_WO_FOR_REPORT_BY_DATE_QUERY)
										.setParameter(GenericWorkorderConstants.DATE, Utils.getDateString(date));
		String geographyFilterName = getGeographyFilterName(geographyLevel);
		if (geographyFilterName != null) {
			applyGeographyFilter(geographyFilterName, Arrays.asList(geographyId));
		}
		try {
			list = query.getResultList();
			disableFilter(geographyFilterName);
		} catch (Exception e) {
			throw new DaoException(ExceptionUtils.getMessage(e));
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<GenericWorkorder> getGenericWOForUser(String userName){
		try {
			logger.info("Going to get Generic Workorder  list for user {} ",userName);
			Query query=getEntityManager().createNamedQuery("getGenericWOForUser").setParameter("userName", userName);
			return query.getResultList();
		}catch(Exception exception) {
			logger.error("Error in getting Generic Workorder which are assigned to User {} : Exception {} ",userName ,Utils.getStackTrace(exception));
			throw new DaoException("Unable To get WO List for User"+userName);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<GenericWorkorder> findByIds(List<Integer>workorderIds) {
		logger.info("Inside method findByIds to get the Workorder {} ",workorderIds);
		try{
			Query query = getEntityManager().createNamedQuery("getWorkorderByIds");
			query.setParameter("workorderIds", workorderIds);
			  return query.getResultList();
				 
		
		} catch(Exception e){
			logger.warn("Exception inside method findById for workorder Id {} , {} ",workorderIds,Utils.getStackTrace(e));
		}

		return Collections.emptyList();
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> getListOfWorkOrderIdByBuildingId(Integer buildingId, String technology) {
		logger.info("inside the method getListOfWorkOrderIdByBuildingId {}", buildingId);
		List<Integer> workOrderIdList = new ArrayList<>();
		try {
			Query nativeQuery = getEntityManager().createNativeQuery(
					"select gwo.genericworkorderid_pk from GenericWorkorder gwo join (select genericworkorderid_fk,group_concat(concat(entitytype,':',entityvalue)) woprop from GWOMeta group by genericworkorderid_fk) meta on gwo.genericworkorderid_pk=meta.genericworkorderid_fk where gwo.status = 'COMPLETED' and meta.woprop like CONCAT('%buildingId:',:buildingId,'%') and meta.woprop like CONCAT('%technology:',:technology,'%')");
			nativeQuery.setParameter("buildingId", buildingId);
			nativeQuery.setParameter("technology", technology);
			workOrderIdList = nativeQuery.getResultList();
			logger.info("workOrderIdList {}", workOrderIdList);
			return workOrderIdList;
		} catch (Exception e) {
			logger.error(ERROR_INSIDE_THE_METHOD_GET_LIST_OF_WORK_ORDER_ID_BY_BUILDING_ID, Utils.getStackTrace(e));
		}
		return workOrderIdList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> getWorkOrderIdListByBuildingId(Integer buildingId) {
		logger.info("inside the method getListOfWorkOrderIdByBuildingId {}", buildingId);
		List<Integer> workOrderIdList = new ArrayList<>();
		try {
			Query nativeQuery = getEntityManager().createNativeQuery(
					"select gwo.genericworkorderid_pk from GenericWorkorder gwo join (select genericworkorderid_fk,group_concat(concat(entitytype,':',entityvalue)) woprop from GWOMeta group by genericworkorderid_fk) meta on gwo.genericworkorderid_pk=meta.genericworkorderid_fk where gwo.status = 'COMPLETED' and meta.woprop like CONCAT('%buildingId:',:buildingId,'%')");
			nativeQuery.setParameter("buildingId", buildingId);
			workOrderIdList = nativeQuery.getResultList();
			logger.info("workOrderIdList {}", workOrderIdList);
			return workOrderIdList;
		} catch (Exception e) {
			logger.error(ERROR_INSIDE_THE_METHOD_GET_LIST_OF_WORK_ORDER_ID_BY_BUILDING_ID, Utils.getStackTrace(e));
		}
		return workOrderIdList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getNVReportConfiguration(String reportType){
		List<Object[]> objcetList = new ArrayList<>();
       logger.info("inside the method getNVReportConfiguration reportType {}",reportType);
       try {
    	   Query nativeQuery = getEntityManager().createNativeQuery(
				"select vendor,operator, kpi,configuration,targetvalue from NVReportConfiguration where reporttype=:reportType");
		nativeQuery.setParameter("reportType", reportType);
		objcetList = nativeQuery.getResultList();
       } catch (Exception e) {
			logger.error(ERROR_INSIDE_THE_METHOD_GET_LIST_OF_WORK_ORDER_ID_BY_BUILDING_ID, Utils.getStackTrace(e));
		}
		return objcetList;
		
	}
	
	private String getGeographyFilterName(String geographyLevel) {
		if (geographyLevel != null) {
			if (geographyLevel.equals(ForesightConstants.GEOGRAPHY_L1)) {
				return "GWGeographyL1Filter";
			} else if (geographyLevel.equals(ForesightConstants.GEOGRAPHY_L2)) {
				return "GWGeographyL2Filter";
			} else if (geographyLevel.equals(ForesightConstants.GEOGRAPHY_L3)) {
				return "GWGeographyL3Filter";
			} else if (geographyLevel.equals(ForesightConstants.GEOGRAPHY_L4) 
					|| geographyLevel.equals(ForesightConstants.GeographyType_L4)) {
				return "GWGeographyL4Filter";
			}else if(geographyLevel.equals(ForesightConstants.GEOGRAPHY_L3_LIST)){
				return "GWGeographyL3FilterList";
			}
		}
		return null;
	}

	private void applySearchFilter(String searchString) {
		if (searchString != null) {
			Session s = (Session) getEntityManager().getDelegate();
			Filter filter = s.enableFilter(GenericWorkorderConstants.SEARCH_FILTER);
			filter.setParameter("searchString", ForesightConstants.MODULUS + searchString + ForesightConstants.MODULUS);
		}
	}
	



	

	private void applyGeographyListFilter(String filterName, List<Integer> geographyIdList) {
		if(filterName != null && geographyIdList != null) {
			Session s = (Session) getEntityManager().getDelegate();
			Filter filter = s.enableFilter(filterName);
			filter.setParameterList("geographyId", geographyIdList);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<GenericWorkorder> findAllWorkorder(Integer lLimit, Integer uLimit, List<TemplateType> templateList,
			List<Status> statusList, String searchString, String geographyLevel, List<Integer> geographyIdList,
			Integer creatorId, Long startTimestamp, Long emdTimestamp) {

		logger.info(
				"Getting All Workorder with lLimit {}, uLimit {}, templateList {}, statusList {}, geographyLevel {}, "
						+ "geographyId {}, creatorId {} , startTimestamp {} , endTimestamp {} ",
						lLimit, uLimit, templateList, statusList, geographyLevel, geographyIdList, creatorId,startTimestamp,emdTimestamp);
		Query query = getEntityManager().createNamedQuery("findAllWorkorderWithinTimeRange")
				.setParameter(TEMPLATE_LIST, templateList)
				.setParameter(STATUS_LIST, statusList)
				.setParameter("startTime", new Date(startTimestamp))
				.setParameter("endTime", new Date(emdTimestamp));
		if (uLimit != null && lLimit != null) {
			query.setFirstResult(lLimit).setMaxResults(uLimit - lLimit + 1);
		}
		applySearchFilter(searchString);
		applyCreatorFilter(creatorId);
		String geographyFilterName = getGeographyFilterName(ForesightConstants.GEOGRAPHY_L3);
		applyGeographyListFilter(geographyFilterName, geographyIdList);
		try {
			List<GenericWorkorder> list = query.getResultList();
			disableFilter("SearchFilter");
			disableFilter("GWCreatorFilter");
			disableFilter(geographyFilterName);
			logger.info("Inside findAllWorkorder() getting list size {}", list.size());
			return list;
		} catch (Exception e) {
			throw new DaoException(e.getMessage());
		}
	
	}
	
	/**
	 * Find by pk with user.
	 *
	 * @param entityPk
	 *            the entity pk
	 * @return the generic workorder object
	 * @throws DaoException
	 *             the dao exception
	 */
	
	@Override
	public GenericWorkorder findByPkWithUser(Integer entityPk) {
		Query query = getEntityManager().createNamedQuery("getWorkorderByWOIdWithUser").setParameter("id",
				entityPk);
		try {
			return (GenericWorkorder) query.getSingleResult();
		} catch (Exception e) {
			logger.error("Error in method findByPkWithUser {}", Utils.getStackTrace(e));
			throw new DaoException(e.getMessage());
		}
	}

	/**
	 * Find by pk with geographyl3.
	 *
	 * @param entityPk
	 *            the entity pk
	 * @return the generic workorder object
	 * @throws DaoException
	 *             the dao exception
	 */

	@Override
	public GenericWorkorder findByPkWithGeographyL3(Integer entityPk) {
		Query query = getEntityManager().createNamedQuery("getWorkorderByWOIdWithGeographyL3").setParameter("id",
				entityPk);
		try {
			return (GenericWorkorder) query.getSingleResult();
		} catch (Exception e) {
			logger.error("Error in method findByPkWithUser {}", Utils.getStackTrace(e));
			throw new DaoException(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<GenericWorkorder> findAllNVWorkorder(Integer lLimit, Integer uLimit, List<TemplateType> templateList,
			List<Status> statusList, String searchString, String geographyLevel, Integer geographyId, Integer creatorId,Boolean isArchived) {
		logger.info(
				"Getting All Workorder with lLimit {}, uLimit {}, templateList {}, statusList {}, geographyLevel {}, "
						+ "geographyId {}, creatorId {}",
				lLimit, uLimit, templateList, statusList, geographyLevel, geographyId, creatorId);
		Query query = getEntityManager().createNamedQuery("findAllNVWorkorders")
										.setParameter(TEMPLATE_LIST, templateList)
										.setParameter(STATUS_LIST, statusList);

		if (uLimit != null && lLimit != null) {
			query	.setFirstResult(lLimit)
					.setMaxResults(uLimit - lLimit + 1);
		}
		applyArchiveFilter(isArchived);
		applySearchFilter(searchString);
		applyCreatorFilter(creatorId);
		String geographyFilterName = getGeographyFilterName(geographyLevel);
		applyGeographyFilter(geographyFilterName, Arrays.asList(geographyId));
		try {
			List<GenericWorkorder> list = query.getResultList();
			disableFilter("SearchFilter");
			disableFilter("GWCreatorFilter");
			disableFilter(geographyFilterName);
			disableFilter("ArchivedFilter");
			logger.info("Inside findAllWorkorder() getting list size {}", list.size());
			return list;
		} catch (Exception e) {
			logger.error("Error in getting all workorders  {} ",ExceptionUtils.getStackTrace(e));
			throw new DaoException(e.getMessage());
		}
	}

	@Override
	public List<GenericWorkorder> findFilteredWOList(NVWorkorderWrapper wrapper, Integer lLimit, Integer uLimit,
			Boolean isArchive) {
		List<Status> statusList = NVWorkorderUtils.getStatusList(wrapper.getStatusList());
		List<TemplateType> templateList = NVWorkorderUtils.getTemplateList(wrapper.getTemplate());
		logger.info("Getting All Workorder with lLimit {}, uLimit {}, templateList {}, statusList {}", lLimit, uLimit,
				wrapper.getTemplate(), wrapper.getStatusList());
		Query query = getEntityManager().createNamedQuery("findAllNVWorkorders")
				.setParameter(TEMPLATE_LIST, templateList).setParameter(STATUS_LIST, statusList);

		applyArchiveFilter(isArchive);
		applyWorkOrderIdFilter(wrapper.getWorkorderId());
		applyWorkOrderNameFilter(wrapper.getWorkorderName());
		applyRemarkFilter(wrapper.getRemark());
		applyAssignedToFilter(wrapper.getAssignedTo());
		applyAssignedByFilter(wrapper.getAssignedBy());
		if(wrapper.getBuildingId()!=null) {
		applyGWOMetaFilter(NVWorkorderConstant.BUILDING_ID, Arrays.asList(String.valueOf(wrapper.getBuildingId())));
		}
		if (lLimit != null) {
			query.setFirstResult(lLimit).setMaxResults(uLimit - lLimit + 1);
		}

		try {
			List<GenericWorkorder> list = query.getResultList();
			disableFilter(GenericWorkorderConstants.ASSIGNED_TO_FILTER);
			disableFilter(GenericWorkorderConstants.ARCHIVED_FILTER);
			disableFilter(GenericWorkorderConstants.WO_ID_FILTER);
			disableFilter(GenericWorkorderConstants.WO_NAME_FILTER);
			disableFilter(GenericWorkorderConstants.REMARK_FILTER);
			disableFilter(GenericWorkorderConstants.ASSIGNED_BY_FILTER);
			if (wrapper.getBuildingId() != null) {
				disableFilter(NVConstant.GWO_META_FILTER);
			}
			logger.info("Inside find Filter Workorder getting list size {}", list.size());
			return list;
		} catch (Exception e) {
			logger.error("Error in getting all workorders  {} ", ExceptionUtils.getStackTrace(e));
			throw new DaoException(e.getMessage());
		}
	}
	
	@Override
	public Long getCountByFilter(NVWorkorderWrapper wrapper, Boolean isArchive) {
		logger.info("Going to get Workorder Count for templateList {}, statusList {}, isArchived {},", wrapper.getTemplate(), wrapper.getStatusList(),isArchive);
		Long count=0L;
		try {
			List<Status> statusList = NVWorkorderUtils.getStatusList(wrapper.getStatusList());
			List<TemplateType> templateList = NVWorkorderUtils.getTemplateList(wrapper.getTemplate());
			Query query = getEntityManager().createNamedQuery("getTotalCountForNVWO")
					.setParameter(GenericWorkorderConstants.TEMPLATE_LIST, templateList)
					.setParameter(GenericWorkorderConstants.STATUS_LIST, statusList);
			applyArchiveFilter(isArchive);
			applyWorkOrderIdFilter(wrapper.getWorkorderId());
			applyWorkOrderNameFilter(wrapper.getWorkorderName());
			applyRemarkFilter(wrapper.getRemark());
			applyAssignedByFilter(wrapper.getAssignedBy());
			applyAssignedToFilter(wrapper.getAssignedTo());
			if (wrapper.getBuildingId() != null) {
				applyGWOMetaFilter(NVWorkorderConstant.BUILDING_ID,
						Arrays.asList(String.valueOf(wrapper.getBuildingId())));
			}
			count = (Long) query.getSingleResult();
			disableFilter(GenericWorkorderConstants.ASSIGNED_TO_FILTER);
			disableFilter(GenericWorkorderConstants.ARCHIVED_FILTER);
			disableFilter(GenericWorkorderConstants.WO_ID_FILTER);
			disableFilter(GenericWorkorderConstants.WO_NAME_FILTER);
			disableFilter(GenericWorkorderConstants.REMARK_FILTER);
			disableFilter(GenericWorkorderConstants.ASSIGNED_TO_FILTER);
			disableFilter(GenericWorkorderConstants.ASSIGNED_BY_FILTER);
			if (wrapper.getBuildingId() != null) {
				disableFilter(NVConstant.GWO_META_FILTER);
			}
		} catch (Exception e) {
			throw new DaoException(e.getMessage());
		}
		return count;
	}

	private void applyRemarkFilter(String remark) {
		if (Utils.hasValidValue(remark)) {
			Session s = (Session) getEntityManager().getDelegate();
			Filter filter = s.enableFilter(GenericWorkorderConstants.REMARK_FILTER);
			filter.setParameter("remark", ForesightConstants.MODULUS + remark + ForesightConstants.MODULUS);
		}
	}

	private void applyWorkOrderNameFilter(String workOrderName) {
		if (Utils.hasValidValue(workOrderName)) {
			Session s = (Session) getEntityManager().getDelegate();
			Filter filter = s.enableFilter(GenericWorkorderConstants.WO_NAME_FILTER);
			filter.setParameter("woName", ForesightConstants.MODULUS + workOrderName + ForesightConstants.MODULUS);
		}
	}

	private void applyWorkOrderIdFilter(String woId) {
		if (Utils.hasValidValue(woId)) {
			Session s = (Session) getEntityManager().getDelegate();
			Filter filter = s.enableFilter(GenericWorkorderConstants.WO_ID_FILTER);
			filter.setParameter("woId", ForesightConstants.MODULUS + woId + ForesightConstants.MODULUS);
		}
	
	}

	private void applyAssignedByFilter(String assignedBy) {
		if (Utils.hasValidValue(assignedBy)) {
			assignedBy=assignedBy.trim().replace(ForesightConstants.SPACE, ForesightConstants.EMPTY);
			Session s = (Session) getEntityManager().getDelegate();
			Filter filter = s.enableFilter(GenericWorkorderConstants.ASSIGNED_BY_FILTER);
			filter.setParameter("assignedBy", ForesightConstants.MODULUS + assignedBy + ForesightConstants.MODULUS);
		}
	}

	private void applyAssignedToFilter(String assignedTo) {
		if (Utils.hasValidValue(assignedTo)) {
			assignedTo=assignedTo.trim().replace(ForesightConstants.SPACE, ForesightConstants.EMPTY);
			Session s = (Session) getEntityManager().getDelegate();
			Filter filter = s.enableFilter(GenericWorkorderConstants.ASSIGNED_TO_FILTER);
			filter.setParameter("assignedTo", ForesightConstants.MODULUS + assignedTo + ForesightConstants.MODULUS);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<GenericWorkorder> getWoIdListForDateRange(Long fromdate,Long todate,TemplateType templateType) {
		logger.info("going to get wolist for given date range {},  {}, {}", todate, fromdate, templateType);
		Query query = getEntityManager().createNamedQuery(GenericWorkorderConstants.FIND_ALL_WOKRORDER_WITHIN_TIMERANGE)
				.setParameter(GenericWorkorderConstants.TEMPLATE_LIST, templateType)
				.setParameter(GenericWorkorderConstants.STATUS_LIST, Status.COMPLETED)
				.setParameter(GenericWorkorderConstants.START_TIME, new Date(fromdate))
				.setParameter(GenericWorkorderConstants.END_TIME, new Date(todate));
		List<GenericWorkorder> resultList = query.getResultList();
		return resultList;
	}
}
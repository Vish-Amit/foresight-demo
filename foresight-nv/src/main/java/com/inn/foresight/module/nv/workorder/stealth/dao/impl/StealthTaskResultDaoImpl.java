package com.inn.foresight.module.nv.workorder.stealth.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.NVConstant;
import com.inn.foresight.module.nv.device.wrapper.NVDeviceDataWrapper;
import com.inn.foresight.module.nv.workorder.stealth.dao.IStealthTaskResultDao;
import com.inn.foresight.module.nv.workorder.stealth.model.StealthTaskResult;
import com.inn.foresight.module.nv.workorder.stealth.wrapper.StealthWOWrapper;

@Repository("StealthTaskResultDaoImpl")
public class StealthTaskResultDaoImpl extends HibernateGenericDao<Integer, StealthTaskResult>
		implements IStealthTaskResultDao {

	/** The logger. */
	private Logger logger = LogManager.getLogger(StealthTaskResultDaoImpl.class);

	public StealthTaskResultDaoImpl() {
		super(StealthTaskResult.class);
	}

	private static final String ERROR_WHILE_GETTING_RESULTLIST_FOR_TASK_ID = "Error while getting resultlist for task id: {}, {}";

	
	@Override
	public StealthTaskResult getStealthTaskResultByWOAndStealthTask(Integer woId, Integer stealthTaskId, Date date) {
		logger.info("Going to get StealthTask result for woId {} Stealth TaskId {} for Time {} ", woId, stealthTaskId,
				date);

		try {
			Query query = getEntityManager().createNamedQuery("getStealthTaskResultByWOIdandTask")
					.setParameter("woId", woId).setParameter("stealthTaskDetail", stealthTaskId)
					.setParameter("date", date);
			return (StealthTaskResult) query.getSingleResult();
		} catch (Exception exception) {
			logger.error("Unable to fetch Stealth Task for for woId {} Stealth TaskId {} for Time {}  Exception {} ",
					woId, stealthTaskId, date, Utils.getStackTrace(exception));

		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public StealthTaskResult getStealthTaskResultByStealthTaskDetailId(Integer stealthTaskDetailId, Date startTime,
			Integer hour) {
		logger.info("Fetching task result from database {} {} {} ", stealthTaskDetailId, startTime, hour);
		try {
			Query query = getEntityManager().createNamedQuery("getStealthTaskResultByStealthTaskDetailId")
					.setParameter("stealthTaskDetailId", stealthTaskDetailId).setParameter("startTime", startTime);
			if (hour != null)
				enableFilter("StealthHourFilter", "hour", hour);
			List<StealthTaskResult> resultList = query.getResultList();
			if (hour != null)
				disableFilter("StealthHourFilter");
			if (resultList != null && !resultList.isEmpty()) {
				return resultList.get(ForesightConstants.ZERO_INT);
			}
		} catch (NoResultException noResultException) {
			logger.error("No task result entry found for task detail id: {}", stealthTaskDetailId);
		} catch (Exception e) {
			logger.error("Error while getting task result by stealthTaskDetailId: {},{}", stealthTaskDetailId,
					ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<NVDeviceDataWrapper> getStealthDeviceWrapperListByWOId(Integer id) {
		try {
			Query query = getEntityManager().createNamedQuery("getStealthDeviceWrapperListByWOId").setParameter("id",
					id);
			return query.getResultList();
		} catch (Exception e) {
			logger.error("Exception in getStealthDeviceListByWOId for woId {} exception {}  ", id,
					Utils.getStackTrace(e));
		}
		return Collections.emptyList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StealthWOWrapper> getStealthWoSummary(Integer workorderId) {
		try {
			Query query = getEntityManager().createNamedQuery("getStealthTaskResultByWorkorderId").setParameter("id",
					workorderId);
			return query.getResultList();
		} catch (Exception e) {
			logger.error("Exception in getStealthWoSummary ", ExceptionUtils.getStackTrace(e));
		}
		return Collections.emptyList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StealthWOWrapper> getStatusSummary(Integer woId, String type) {
		List<StealthWOWrapper> resultList = new ArrayList<>();
		try {
			Query query = null;
			if (NVConstant.HOURLY.equalsIgnoreCase(type)) {
				query = getEntityManager().createNamedQuery("getHourlyStatusSummary").setParameter("woId", woId);
			} else {
				query = getEntityManager().createNamedQuery("getStatusSummary").setParameter("woId", woId);
			}
			return query.getResultList();
		} catch (Exception e) {
			logger.error("Exception in getStealthWoSummary  for woId {}  Exception : {} ", woId,
					ExceptionUtils.getStackTrace(e));
		}
		return resultList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StealthWOWrapper> getStatusSummaryForReport(Integer woId, List<Integer> taskIdList) {
		List<StealthWOWrapper> resultList = new ArrayList<>();
		try {
			Query query = getEntityManager().createNamedQuery("getStatusSummaryForReport").setParameter("woId", woId)
					.setParameter("taskIdList", taskIdList);
			return query.getResultList();
		} catch (Exception e) {
			logger.error("Exception in getStealthWoSummary  for woId {}  Exception : {} ", woId,
					ExceptionUtils.getStackTrace(e));
		}
		return resultList;
	}

	@Override
	public StealthTaskResult getStealthTaskResultForHourlyWO(Integer woId, Integer stealthTaskId, Date date,
			Integer hour) {
		logger.info("Going to get StealthTask result for woId {} Stealth TaskId {} for Time {} hour {} ", woId,
				stealthTaskId, date, hour);

		try {
			Query query = getEntityManager().createNamedQuery("getStealthTaskResultForHourlyWO")
					.setParameter("woId", woId).setParameter("stealthTaskDetail", stealthTaskId)
					.setParameter("date", date).setParameter("hour", hour);

			return (StealthTaskResult) query.getSingleResult();
		} catch (Exception exception) {
			logger.error("Unable to fetch Stealth Task for for woId {} Stealth TaskId {} for Time {}  Exception {} ",
					woId, stealthTaskId, date, Utils.getStackTrace(exception));
		}
		return null;
	}

	private void enableFilter(String filterName, String filterParam, Object filterValue) {
		Session s = (Session) getEntityManager().getDelegate();
		Filter filter = s.enableFilter(filterName);
		if (filterParam != null && filterValue != null) {
			filter.setParameter(filterParam, filterValue);
		}
	}

	/**
	 * Disable filter.
	 *
	 * @param filterName the filter name
	 */
	private void disableFilter(String filterName) {
		Session s = (Session) getEntityManager().getDelegate();
		s.disableFilter(filterName);
	}
	
	@Override
	public List<StealthTaskResult> getStealthResultListByTaskId(Integer taskId){
		logger.info("Going to get result list for task id: {} ",taskId);
		try {
		Query query = getEntityManager().createNamedQuery("getStealthResultListByTaskId");
		query.setParameter("taskId", taskId);
		return query.getResultList();
		}catch(IllegalStateException illegalStateException) {
			logger.error(ERROR_WHILE_GETTING_RESULTLIST_FOR_TASK_ID,taskId, ExceptionUtils.getStackTrace(illegalStateException));
			throw illegalStateException;
		}catch(PersistenceException persistenceException) {
			logger.error(ERROR_WHILE_GETTING_RESULTLIST_FOR_TASK_ID,taskId, ExceptionUtils.getStackTrace(persistenceException));
			throw persistenceException;
		}catch(Exception e) {
			logger.error(ERROR_WHILE_GETTING_RESULTLIST_FOR_TASK_ID,taskId, ExceptionUtils.getStackTrace(e));
			throw e;
		}
	}

	@Override
	public StealthWOWrapper getDeviceCount(Integer workorderId) {
		StealthWOWrapper deviceCount = new StealthWOWrapper();
		try {
			Query query = null;			
			query = getEntityManager().createNamedQuery("getAssignedDeviceCount").setParameter("woId", workorderId);
			return (StealthWOWrapper) query.getSingleResult();
		} catch (Exception e) {
			logger.error("Exception in getStealthWoSummary  for woId {}  Exception : {} ", workorderId,
					ExceptionUtils.getStackTrace(e));
		}
		return deviceCount;
	}
}

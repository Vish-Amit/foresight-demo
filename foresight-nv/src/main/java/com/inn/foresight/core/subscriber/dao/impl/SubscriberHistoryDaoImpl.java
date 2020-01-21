package com.inn.foresight.core.subscriber.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.QueryTimeoutException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.lang.DateUtils;
import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.foresight.core.subscriber.dao.ISubscriberHistoryDao;
import com.inn.foresight.core.subscriber.model.SubscriberHistory;
import com.inn.foresight.core.subscriber.utils.SubscriberHistoryConstants;
import com.inn.foresight.core.subscriber.utils.SubscriberHistoryUtils;
import com.inn.foresight.core.subscriber.utils.wrapper.SubscriberHistoryWrapper;

@Repository("SubscriberHistoryDaoImpl")
public class SubscriberHistoryDaoImpl extends HibernateGenericDao<Integer, SubscriberHistory> implements ISubscriberHistoryDao {

	private Logger logger = LogManager.getLogger(SubscriberHistoryDaoImpl.class);

	public SubscriberHistoryDaoImpl() {
		super(SubscriberHistory.class);
	}

	@Override
	public Long getTotalSubscriberHistoryByMdn(String subscriberSearch) {
		logger.info("Going to search Total Subscriber history for search value {} ", subscriberSearch);
		Long totalSubscriber = null;
		try {
			CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<SubscriberHistoryWrapper> criteriaQuery = criteriaBuilder.createQuery(SubscriberHistoryWrapper.class);
			Root<SubscriberHistory> root = criteriaQuery.from(SubscriberHistory.class);
			SubscriberHistoryUtils.getSelectionInCriteriaQuery(criteriaBuilder, criteriaQuery, root);
			if (subscriberSearch != null) {
				SubscriberHistoryUtils.getWhereClauseInCriteriaQuery(subscriberSearch, criteriaBuilder, criteriaQuery, root);
			}else {
				criteriaQuery.where((criteriaBuilder.between(criteriaBuilder.function(SubscriberHistoryConstants.FUNCTION_DATE, Date.class, root.get(SubscriberHistoryConstants.SUBSCRIBER_SEARCH_DATETIME)), DateUtils.addDays(new Date(), ConfigUtils.getInteger(SubscriberHistoryConstants.TIME_DURATION)),new Date())));
			}
			SubscriberHistoryUtils.getGroupByClauseInCriteriaQuery(criteriaBuilder, criteriaQuery, root);
			Query query = getEntityManager().createQuery(criteriaQuery);
			totalSubscriber = (long) query.getResultList().size();
		} catch (QueryTimeoutException queryTimeoutException) {
			logger.error("Data can't be fetched because of timeout:{}", ExceptionUtils.getStackTrace(queryTimeoutException));
		} catch (NoResultException noResultException) {
			logger.error("Result not found noResultException:{}", ExceptionUtils.getStackTrace(noResultException));
		} catch (Exception exception) {
			logger.error("Unable to get total subscriber search by search value : {}  Exception : {}", subscriberSearch, ExceptionUtils.getStackTrace(exception));
		}
		return totalSubscriber;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SubscriberHistoryWrapper> getSubscriberHistoryDetails(Integer llimit, Integer ulimit) {
		logger.info("Going to get Subscriber History Details");
		List<SubscriberHistoryWrapper> list = new ArrayList<>();
		try {
			CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<SubscriberHistoryWrapper> criteriaQuery = criteriaBuilder.createQuery(SubscriberHistoryWrapper.class);
			Root<SubscriberHistory> root = criteriaQuery.from(SubscriberHistory.class);
			SubscriberHistoryUtils.getSelectionInCriteriaQuery(criteriaBuilder, criteriaQuery, root);
			criteriaQuery.where((criteriaBuilder.between(criteriaBuilder.function(SubscriberHistoryConstants.FUNCTION_DATE, Date.class, root.get(SubscriberHistoryConstants.SUBSCRIBER_SEARCH_DATETIME)), DateUtils.addDays(new Date(), ConfigUtils.getInteger(SubscriberHistoryConstants.TIME_DURATION)),new Date())));
			SubscriberHistoryUtils.getGroupByClauseInCriteriaQuery(criteriaBuilder, criteriaQuery, root);
			SubscriberHistoryUtils.setOrderBySearchBy(criteriaBuilder,criteriaQuery,root);
			Query query = getEntityManager().createQuery(criteriaQuery);
			SubscriberHistoryUtils.setPaginationInQuery(llimit, ulimit, query);
			list = query.getResultList();
			logger.info("RESULT LIST : {}", list);
		}catch (QueryTimeoutException queryTimeoutException) {
			logger.error("Data can't be fetched because of timeout:{}", ExceptionUtils.getStackTrace(queryTimeoutException));
		} catch (NoResultException noResultException) {
			logger.error("Result not found:{}", ExceptionUtils.getStackTrace(noResultException));
		} catch (Exception exception) {
			logger.error("Error in getting subscriber detail : {} ", ExceptionUtils.getStackTrace(exception));
			
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SubscriberHistoryWrapper> searchSubscriberHistoryDetailByMdnAndUser(String subscriberSearch,Integer llimit, Integer ulimit) {
		logger.info("Going to search Subscriber History Details for mdn and user : {}", subscriberSearch);
		List<SubscriberHistoryWrapper> list = new ArrayList<>();
		try {
			CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<SubscriberHistoryWrapper> criteriaQuery = criteriaBuilder.createQuery(SubscriberHistoryWrapper.class);
			Root<SubscriberHistory> root = criteriaQuery.from(SubscriberHistory.class);
			SubscriberHistoryUtils.getSelectionInCriteriaQuery(criteriaBuilder, criteriaQuery, root);
			if (subscriberSearch != null && !subscriberSearch.isEmpty()) {
				SubscriberHistoryUtils.getWhereClauseInCriteriaQuery(subscriberSearch, criteriaBuilder, criteriaQuery, root);
			}
			SubscriberHistoryUtils.getGroupByClauseInCriteriaQuery(criteriaBuilder, criteriaQuery, root);
			SubscriberHistoryUtils.setOrderBySearchBy(criteriaBuilder,criteriaQuery,root);
			Query query = getEntityManager().createQuery(criteriaQuery);
			SubscriberHistoryUtils.setPaginationInQuery(llimit, ulimit, query);
			list = query.getResultList();
			logger.info("RESULT LIST : {}", list);
		} catch (QueryTimeoutException queryTimeoutException) {
			logger.error("Searching can't be performed because of timeout:{}", ExceptionUtils.getStackTrace(queryTimeoutException));
		} catch (NoResultException noResultException) {
			logger.error("Result not found:{}", ExceptionUtils.getStackTrace(noResultException));
		} catch (Exception exception) {
			logger.error("Error in getting Subscriber details by Mdn and User : {} ", ExceptionUtils.getStackTrace(exception));
		}
		return list;
	}	
}
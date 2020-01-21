package com.inn.foresight.core.subscriber.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.QueryTimeoutException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.subscriber.dao.ISubscriberInfoDao;
import com.inn.foresight.core.subscriber.model.SubscriberInfo;
import com.inn.foresight.core.subscriber.utils.SubscriberInfoConstants;
import com.inn.foresight.core.subscriber.utils.SubscriberInfoUtils;
import com.inn.foresight.core.subscriber.utils.wrapper.SubscriberInfoDetailWrapper;

@Repository("SubscriberInfoDaoImpl")
public class SubscriberInfoDaoImpl extends HibernateGenericDao<Integer, SubscriberInfo> implements ISubscriberInfoDao {

	private Logger logger = LogManager.getLogger(SubscriberInfoDaoImpl.class);

	public SubscriberInfoDaoImpl() {
		super(SubscriberInfo.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SubscriberInfoDetailWrapper> searchSubscriberInfo(String searchValue, Integer llimit, Integer ulimit) {
		logger.info("Going to search Subscriber Info for search value : {}", searchValue);
		List<SubscriberInfoDetailWrapper> list = new ArrayList<>();
		try {
			CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<SubscriberInfoDetailWrapper> criteriaQuery = criteriaBuilder.createQuery(SubscriberInfoDetailWrapper.class);
			Root<SubscriberInfo> root = criteriaQuery.from(SubscriberInfo.class);
			criteriaQuery.select(criteriaBuilder.construct(SubscriberInfoDetailWrapper.class, root.get(SubscriberInfoConstants.SUBSCRIBER_INFO_IMSI),
					root.get(SubscriberInfoConstants.SUBSCRIBER_INFO_MSISDN), root.get(SubscriberInfoConstants.SUBSCRIBER_INFO_CATEGORY), root.get(SubscriberInfoConstants.SUBSCRIBER_INFO_TYPE),
					root.get(SubscriberInfoConstants.SUBSCRIBER_INFO_CREATION_TIME), root.get(SubscriberInfoConstants.SUBSCRIBER_INFO_MODIFICATION_TIME),
					root.get(SubscriberInfoConstants.SUBSCRIBER_INFO_IS_ENABLED), root.get(SubscriberInfoConstants.SUBSCRIBER_INFO_CREATED_BY).get(SubscriberInfoConstants.USER_NAME),
					root.get(SubscriberInfoConstants.SUBSCRIBER_INFO_MODIFIERED_BY).get(SubscriberInfoConstants.USER_NAME)));
			if (searchValue != null && !searchValue.isEmpty()) {
				criteriaQuery.where(criteriaBuilder.like(criteriaBuilder.upper(root.get(SubscriberInfoConstants.SUBSCRIBER_INFO_SEARCH_VALUE)),
						ForesightConstants.PERCENT + searchValue.toUpperCase() + ForesightConstants.PERCENT));
			}
			criteriaQuery.groupBy(root.get(SubscriberInfoConstants.SUBSCRIBER_INFO_MSISDN));
			criteriaQuery.orderBy(criteriaBuilder.desc(criteriaBuilder.max(root.get(SubscriberInfoConstants.SUBSCRIBER_INFO_MODIFICATION_TIME))));
			Query query = getEntityManager().createQuery(criteriaQuery);
			SubscriberInfoUtils.setPaginationInQuery(llimit, ulimit, query);
			list = query.getResultList();
			logger.info("Result List Size :{}", list.size());
		} catch (NoResultException noResultException) {
			logger.error("Result not found {}", Utils.getStackTrace(noResultException));
		} catch (QueryTimeoutException queryTimeoutException) {
			logger.error("Data can't be fetched because of timeout {}", Utils.getStackTrace(queryTimeoutException));
		} catch (Exception exception) {
			logger.error("Error while searching the Subscriber Info {}", Utils.getStackTrace(exception));
		}
		return list;
	}

	@Override
	public SubscriberInfo getSubscriberDetailByMdn(String msisdn) {
		logger.info("Going to get subscriber detail by mdn : {} ", msisdn);
		SubscriberInfo subscriberInfo = null;
		try {
			CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<SubscriberInfo> criteriaQuery = criteriaBuilder.createQuery(SubscriberInfo.class);
			Root<SubscriberInfo> root = criteriaQuery.from(SubscriberInfo.class);
			criteriaQuery.where(criteriaBuilder.equal(root.get(SubscriberInfoConstants.SUBSCRIBER_INFO_MSISDN), msisdn));
			Query query = getEntityManager().createQuery(criteriaQuery);
			subscriberInfo = (SubscriberInfo) query.getSingleResult();
			logger.info("Subscriber Info : {}", subscriberInfo.getSearchValue());
		} catch (NoResultException noResultException) {
			logger.error("Error in getting data for msisdn {} noResultException {} ", msisdn, noResultException.getMessage());
		} catch (Exception e) {
			logger.error("Error in getting subscriber detail by msisdn : {} Exception : {}", msisdn, Utils.getStackTrace(e));
		}
		return subscriberInfo;
	}

	@Override
	public Long getTotalSubscriberCount(String searchValue) {
		logger.info("Going to count total Subscribers for search value : {}", searchValue);
		Long totalSubscriber = null;
		try {
			CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
			Root<SubscriberInfo> root = criteriaQuery.from(SubscriberInfo.class);
			criteriaQuery.select(criteriaBuilder.count(root.get(SubscriberInfoConstants.SUBSCRIBER_INFO_ID)));
			if (searchValue != null) {
				criteriaQuery.where(criteriaBuilder.like(criteriaBuilder.upper(root.get(SubscriberInfoConstants.SUBSCRIBER_INFO_SEARCH_VALUE)),
						ForesightConstants.PERCENT + searchValue.toUpperCase() + ForesightConstants.PERCENT));
			}
			Query query = getEntityManager().createQuery(criteriaQuery);
			totalSubscriber = (Long) query.getSingleResult();
			logger.info("Total Count :{}", totalSubscriber);
		} catch (NoResultException noResultException) {
			logger.error("Result not found {}", Utils.getStackTrace(noResultException));
		} catch (QueryTimeoutException queryTimeoutException) {
			logger.error("Data can't be fetched because of timeout {}", Utils.getStackTrace(queryTimeoutException));
		} catch (Exception exception) {
			logger.error("Error while searching the Subscriber Info {}", Utils.getStackTrace(exception));
		}
		return totalSubscriber;
	}

}

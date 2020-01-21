package com.inn.foresight.core.subscriber.utils;

import java.util.Date;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.lang.DateUtils;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.subscriber.model.SubscriberHistory;
import com.inn.foresight.core.subscriber.utils.wrapper.SubscriberHistoryWrapper;

public class SubscriberHistoryUtils {
	
	private SubscriberHistoryUtils() 
	{
		super();
	}
	public static void getGroupByClauseInCriteriaQuery(CriteriaBuilder criteriaBuilder, CriteriaQuery<SubscriberHistoryWrapper> criteriaQuery, Root<SubscriberHistory> root) {
		criteriaQuery.groupBy(root.get(SubscriberHistoryConstants.SUBSCRIBER_SEARCH_MDN), root.get(SubscriberHistoryConstants.SUBSCRIBER_SEARCH_SEARCHBY),
				criteriaBuilder.function(SubscriberHistoryConstants.FUNCTION_DATE, Date.class, root.get(SubscriberHistoryConstants.SUBSCRIBER_SEARCH_DATETIME)));
	}

	public static void getWhereClauseInCriteriaQuery(String subscriberSearch, CriteriaBuilder criteriaBuilder, CriteriaQuery<SubscriberHistoryWrapper> criteriaQuery, Root<SubscriberHistory> root) {
		criteriaQuery.where(criteriaBuilder.like(criteriaBuilder.upper(root.get(SubscriberHistoryConstants.SUBSCRIBER_SEARCH)), ForesightConstants.PERCENT + subscriberSearch.toUpperCase() + ForesightConstants.PERCENT), criteriaBuilder
		.and(criteriaBuilder.between(criteriaBuilder.function(SubscriberHistoryConstants.FUNCTION_DATE, Date.class, root.get(SubscriberHistoryConstants.SUBSCRIBER_SEARCH_DATETIME)), DateUtils.addDays(new Date(), ConfigUtils.getInteger(SubscriberHistoryConstants.TIME_DURATION)),new Date())));
		}

	public static void getSelectionInCriteriaQuery(CriteriaBuilder criteriaBuilder, CriteriaQuery<SubscriberHistoryWrapper> criteriaQuery, Root<SubscriberHistory> root) {
		criteriaQuery.select(
				criteriaBuilder.construct(SubscriberHistoryWrapper.class, root.get(SubscriberHistoryConstants.SUBSCRIBER_SEARCH_MDN), root.get(SubscriberHistoryConstants.SUBSCRIBER_SEARCH_SEARCHBY),
						criteriaBuilder.function(SubscriberHistoryConstants.FUNCTION_DATE, Date.class, root.get(SubscriberHistoryConstants.SUBSCRIBER_SEARCH_DATETIME)),
						criteriaBuilder.count(root.get(SubscriberHistoryConstants.SUBSCRIBER_SEARCH_ID)), root.get(SubscriberHistoryConstants.SUBSCRIBER_SEARCH_EXECUTIVE_ROLE)));
	}

	public static void setPaginationInQuery(Integer llimit, Integer ulimit, Query query) {
		if (llimit != null && ulimit != null && llimit >= 0 && ulimit > 0) {
			query.setMaxResults(ulimit - llimit + 1);
			query.setFirstResult(llimit);
		}
	}
	
	public static void setOrderBySearchBy(CriteriaBuilder criteriaBuilder, CriteriaQuery<SubscriberHistoryWrapper> criteriaQuery, Root<SubscriberHistory> root) {
		criteriaQuery.orderBy(criteriaBuilder.desc(criteriaBuilder.max(root.get(SubscriberHistoryConstants.SUBSCRIBER_SEARCH_DATETIME))));
	}
}

package com.inn.foresight.core.subscriber.dao;

import java.util.List;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.foresight.core.subscriber.model.SubscriberHistory;
import com.inn.foresight.core.subscriber.utils.wrapper.SubscriberHistoryWrapper;

public interface ISubscriberHistoryDao extends IGenericDao<Integer, SubscriberHistory> {

	Long getTotalSubscriberHistoryByMdn(String subscriberSearch);

	List<SubscriberHistoryWrapper> getSubscriberHistoryDetails(Integer llimit,Integer ulimit);

	List<SubscriberHistoryWrapper> searchSubscriberHistoryDetailByMdnAndUser(String subscriberSearch,Integer llimit,Integer ulimit);

}

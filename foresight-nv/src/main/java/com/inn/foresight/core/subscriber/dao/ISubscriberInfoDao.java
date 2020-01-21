package com.inn.foresight.core.subscriber.dao;

import java.util.List;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.foresight.core.subscriber.model.SubscriberInfo;
import com.inn.foresight.core.subscriber.utils.wrapper.SubscriberInfoDetailWrapper;

public interface ISubscriberInfoDao extends IGenericDao<Integer, SubscriberInfo> {
	
	List<SubscriberInfoDetailWrapper> searchSubscriberInfo(String searchValue, Integer llimit , Integer ulimit);

	SubscriberInfo getSubscriberDetailByMdn(String msisdn);

	Long getTotalSubscriberCount(String searchValue);

}

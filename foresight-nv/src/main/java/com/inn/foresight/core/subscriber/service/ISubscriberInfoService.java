package com.inn.foresight.core.subscriber.service;

import java.util.List;
import java.util.Map;

import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.core.subscriber.model.SubscriberInfo;
import com.inn.foresight.core.subscriber.utils.wrapper.SubscriberInfoDetailWrapper;

public interface ISubscriberInfoService extends IGenericService<Integer, SubscriberInfo>{

	Map<String, String> createSubscriberInfoData(SubscriberInfo subscriberInfo);

	List<SubscriberInfoDetailWrapper> searchSubscriberInfo(String searchValue, Integer llimit, Integer ulimit);

	Long getTotalSubscriberInfoCount(String searchValue);

	SubscriberInfo getSubscriberDetailByMdn(String msisdn);

	List<String> getSubscriberInfoType();

	Map<String, String> getSubscriberCategoryByMdn(String msisdn, String imsi);

	Map<String, String> updateSubscriberInfoByMdn(SubscriberInfoDetailWrapper subscriberInfoWrapper);
	
}

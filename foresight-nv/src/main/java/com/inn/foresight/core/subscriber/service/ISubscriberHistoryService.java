package com.inn.foresight.core.subscriber.service;

import java.util.List;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;

import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.core.subscriber.model.SubscriberHistory;
import com.inn.foresight.core.subscriber.utils.wrapper.SubscriberHistoryWrapper;

public interface ISubscriberHistoryService  extends IGenericService<Integer, SubscriberHistory> {

	Map<String,String> createSubscriberHistory(SubscriberHistory subscriberSearch);

	Long getTotalSubscriberHistoryByMdn(String subscriberSearch);

	@PreAuthorize("hasRole('ROLE_ADMIN_MSISDN_HISTORY_view')")
	List<SubscriberHistoryWrapper> getSubscriberHistoryDetails(Integer llimit,Integer ulimit);

	List<SubscriberHistoryWrapper> searchSubscriberHistoryByMdnAndUser(String subscriberSearch,Integer llimit,Integer ulimit);

}

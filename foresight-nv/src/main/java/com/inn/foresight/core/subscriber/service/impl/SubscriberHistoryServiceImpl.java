package com.inn.foresight.core.subscriber.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.QueryTimeoutException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.service.impl.AbstractService;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.subscriber.dao.ISubscriberHistoryDao;
import com.inn.foresight.core.subscriber.model.SubscriberHistory;
import com.inn.foresight.core.subscriber.service.ISubscriberHistoryService;
import com.inn.foresight.core.subscriber.utils.SubscriberHistoryConstants;
import com.inn.foresight.core.subscriber.utils.wrapper.SubscriberHistoryWrapper;
import com.inn.product.um.user.dao.UserDao;
import com.inn.product.um.user.service.UserContextService;

@Service("SubscriberHistoryServiceImpl")
public class SubscriberHistoryServiceImpl extends AbstractService<Integer, SubscriberHistory> implements ISubscriberHistoryService {

	private Logger logger = LogManager.getLogger(SubscriberHistoryServiceImpl.class);

	@Autowired
	private ISubscriberHistoryDao iSubscriberHistoryDao;

	@Autowired
	private UserDao iUserDao;

	@Autowired
	private UserContextService userInContext;

	@Autowired
	public void setDao(ISubscriberHistoryDao dao) {
		super.setDao(dao);
		this.iSubscriberHistoryDao = dao;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Map<String, String> createSubscriberHistory(SubscriberHistory subscriberSearch) {
		logger.info("Going to create Subscriber History ");
		Map<String, String> map = new HashMap<>();
		String message = ForesightConstants.BLANK_STRING;
		try {
			if (validateStringForSubscriber(subscriberSearch.getSubscriberNo()) && validateStringForSubscriber(subscriberSearch.getSearchBy())) {
				subscriberSearch.setSearchDateTime(new Date());
				subscriberSearch.setSubscriberSearch();
				String userName = userInContext.getUserInContextnew().getUserName();
				setExecutiveRoleOfSubscriber(subscriberSearch, userName);
				subscriberSearch = iSubscriberHistoryDao.create(subscriberSearch);
				message = setSubscriberMessage(subscriberSearch);
				logger.info("MESSAGE : {}", message);
			} else {
				message = SubscriberHistoryConstants.SUBSCRIBER_NOT_INSERTED;
			}
		} catch (QueryTimeoutException queryTimeoutException) {
			logger.error("Creation can't be done because of timeout:{}", ExceptionUtils.getStackTrace(queryTimeoutException));
		} catch (Exception e) {
			logger.error("Error in creating subscriber history : {}", ExceptionUtils.getStackTrace(e));
		}
		map.put(ForesightConstants.MESSAGE, message);
		logger.info("LAST MESSAGE : {}", message);
		return map;
	}

	private void setExecutiveRoleOfSubscriber(SubscriberHistory subscriberSearch, String userName) {
		if ((userName != null && !userName.isEmpty() && subscriberSearch.getSearchBy().equalsIgnoreCase(userName))
				&& (iUserDao.findByUserName(userName).getActiveRole() != null && iUserDao.findByUserName(userName).getActiveRole().getRoleName() != null)) {
			subscriberSearch.setExecutiveRole(iUserDao.findByUserName(userName).getActiveRole().getRoleName());
		}
	}

	private String setSubscriberMessage(SubscriberHistory subscriberSearch) {
		String message;
		if (validateStringForSubscriber(subscriberSearch.getId())) {
			message = SubscriberHistoryConstants.SUBSCRIBER_INSERTED_SUCCESSSFULLY;
		} else {
			message = SubscriberHistoryConstants.SUBSCRIBER_NOT_INSERTED;
		}
		return message;
	}

	private Boolean validateStringForSubscriber(Object validateString) {
		logger.info("Inside validateStringForSubscriber method");
		Boolean isValidate = false;
		if (validateString instanceof Integer || (validateString instanceof String && !validateString.equals(ForesightConstants.NULL_STRING))) {
			return !isValidate;
		}
		return isValidate;
	}

	@Override
	public Long getTotalSubscriberHistoryByMdn(String subscriberSearch) {
		logger.info("Going to search Total Subscriber for search value {} ", subscriberSearch);
		Long totalSubscriber = null;
		try {
			totalSubscriber = iSubscriberHistoryDao.getTotalSubscriberHistoryByMdn(subscriberSearch);
		} catch (DaoException daoException) {
			logger.info("Exception in DAO occured:{}", ExceptionUtils.getStackTrace(daoException));
		} catch (Exception exception) {
			logger.info("Error in getting total subscriber searches: {}", ExceptionUtils.getStackTrace(exception));
		}
		return totalSubscriber;
	}

	@Override
	public List<SubscriberHistoryWrapper> getSubscriberHistoryDetails(Integer llimit, Integer ulimit) {
		logger.info("Going to get details of all subscriber history");
		List<SubscriberHistoryWrapper> searchWrapperList = new ArrayList<>();
		try {
			searchWrapperList = iSubscriberHistoryDao.getSubscriberHistoryDetails(llimit, ulimit);
		} catch (Exception e) {
			logger.info("Error in getting subscriber details:{}", ExceptionUtils.getStackTrace(e));
		}
		return searchWrapperList;
	}

	@Override
	public List<SubscriberHistoryWrapper> searchSubscriberHistoryByMdnAndUser(String subscriberSearch, Integer llimit, Integer ulimit) {
		logger.info("Going to search Subscriber Details by MDN & User : {}", subscriberSearch);
		List<SubscriberHistoryWrapper> searchWrapperList = new ArrayList<>();
		try {
			if (subscriberSearch != null && !subscriberSearch.isEmpty()) {
				searchWrapperList = iSubscriberHistoryDao.searchSubscriberHistoryDetailByMdnAndUser(subscriberSearch, llimit, ulimit);
			}
		} catch (Exception e) {
			logger.info("Error in searching the subscriber details by Mdn and User:{}", ExceptionUtils.getStackTrace(e));
		}
		return searchWrapperList;
	}

}

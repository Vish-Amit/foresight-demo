package com.inn.foresight.core.subscriber.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inn.commons.configuration.ConfigUtils;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.service.impl.AbstractService;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.subscriber.dao.ISubscriberInfoDao;
import com.inn.foresight.core.subscriber.model.SubscriberInfo;
import com.inn.foresight.core.subscriber.service.ISubscriberInfoService;
import com.inn.foresight.core.subscriber.service.ISubscriberSearch;
import com.inn.foresight.core.subscriber.utils.SubscriberInfoConstants;
import com.inn.foresight.core.subscriber.utils.wrapper.SubscriberInfoDetailWrapper;
import com.inn.product.um.user.model.User;
import com.inn.product.um.user.service.UserContextService;
import com.inn.product.um.user.utils.UmConstants;
import com.inn.product.um.user.utils.UmUtils;

@Service("SubscriberInfoServiceImpl")
public class SubscriberInfoServiceImpl extends AbstractService<Integer, SubscriberInfo> implements ISubscriberInfoService, ISubscriberSearch {

	private Logger logger = LogManager.getLogger(SubscriberInfoServiceImpl.class);

	@Autowired
	ISubscriberInfoDao iSubscriberInfoDao;

	@Autowired
	private UserContextService userInContext;

	@Autowired
	public void setDao(ISubscriberInfoDao subscriberInfoDao) {
		super.setDao(subscriberInfoDao);
		this.iSubscriberInfoDao = subscriberInfoDao;
	}

	@Override
	@Transactional
	public Map<String, String> createSubscriberInfoData(SubscriberInfo subscriberInfo) {
		logger.info("Going to create subscriber info detail");
		Map<String, String> map = new HashMap<>();
		String status = ForesightConstants.BLANK_STRING;
		try {
			User user = userInContext.getUserInContextnew();
			subscriberInfo.setCreator(user);
			subscriberInfo.setLastModifier(user);
			subscriberInfo.setCreationTime(new Date());
			subscriberInfo.setModificationTime(new Date());
			String searchValue = subscriberInfo.getMsisdn() + subscriberInfo.getCategory() + user.getUserName();
			subscriberInfo.setSearchValue(searchValue);
			subscriberInfo = iSubscriberInfoDao.create(subscriberInfo);
			status = setOutputMsg(subscriberInfo);
		} catch (DataIntegrityViolationException dataIntegrityViolationException) {
			logger.error("Error in creating subscriber info detail by data integrity Exception : {}", Utils.getStackTrace(dataIntegrityViolationException));
			throw new RestException("Record already exist");
		} catch (Exception e) {
			logger.error("Error in creating subscriber info detail  {}", Utils.getStackTrace(e));
			throw new RestException("Record already exist");
		}
		map.put(ForesightConstants.MESSAGE, status);
		logger.info("Subscriber info map : {}", map);
		return map;
	}

	private String setOutputMsg(SubscriberInfo subscriberInfo) {
		String status;
		if (subscriberInfo != null) {
			status = SubscriberInfoConstants.SUBSCRIBER_INFO_INSERTED_SUCCESSFULLY;
		} else {
			status = SubscriberInfoConstants.SUBSCRIBER_INFO_NOT_INSERTED;
		}
		return status;
	}

	@Override
	@Transactional
	public Map<String, String> updateSubscriberInfoByMdn(SubscriberInfoDetailWrapper subscriberInfo) {
		logger.info("Going to update subscriber info for mdn : {}", subscriberInfo.getMsisdn());
		Map<String, String> map = new HashMap<>();
		String status = ForesightConstants.BLANK_STRING;
		try {
			if (subscriberInfo.getMsisdn() != null && subscriberInfo.getCategory() != null) {
				SubscriberInfo persistedSubscriberInfo = iSubscriberInfoDao.getSubscriberDetailByMdn(subscriberInfo.getMsisdn());
				if (persistedSubscriberInfo != null) {
					User user = userInContext.getUserInContextnew();
					persistedSubscriberInfo.setImsi(subscriberInfo.getImsi());
					persistedSubscriberInfo.setCategory(subscriberInfo.getCategory());
					persistedSubscriberInfo.setIsEnabled(subscriberInfo.getIsEnabled());
					persistedSubscriberInfo.setModificationTime(new Date());
					persistedSubscriberInfo.setLastModifier(user);
					String appendString = null;
					appendString = appendStringOfCreatorName(persistedSubscriberInfo, user);
					String searchValue = persistedSubscriberInfo.getMsisdn() + subscriberInfo.getCategory() + persistedSubscriberInfo.getCreator().getUserName() + appendString;
					persistedSubscriberInfo.setSearchValue(searchValue);
					status = setSubscriberInfoMsg(persistedSubscriberInfo);
				}
			}
		} catch (Exception e) {
			status = SubscriberInfoConstants.SUBSCRIBER_INFO_NOT_UPDATED;
			logger.error("Error in updating subscriber info by mdn : {} Exception : {}", subscriberInfo.getMsisdn(), Utils.getStackTrace(e));
		}
		logger.info("MESSAGE : {}", status);
		map.put(ForesightConstants.MESSAGE, status);
		return map;
	}

	private String setSubscriberInfoMsg(SubscriberInfo persistedSubscriberInfo) {
		String status;
		if (iSubscriberInfoDao.update(persistedSubscriberInfo) != null) {
			status = SubscriberInfoConstants.SUBSCRIBER_INFO_UPDATED_SUCCESSFULLY;
		} else {
			status = SubscriberInfoConstants.SUBSCRIBER_INFO_NOT_UPDATED;
		}
		return status;
	}

	private String appendStringOfCreatorName(SubscriberInfo persistedSubscriberInfo, User user) {
		String appendString;
		if (persistedSubscriberInfo.getCreator().getUserName().equalsIgnoreCase(user.getUserName())) {
			appendString = ForesightConstants.BLANK_STRING;
		} else {
			appendString = user.getUserName();
		}
		return appendString;
	}

	@Override
	public List<SubscriberInfoDetailWrapper> searchSubscriberInfo(String searchValue, Integer llimit, Integer ulimit) {
		logger.info("Going to search Subscriber Info for seach value : {}", searchValue);
		List<SubscriberInfoDetailWrapper> list = new ArrayList<>();
		try {
			list = iSubscriberInfoDao.searchSubscriberInfo(searchValue, llimit, ulimit);
		} catch (Exception e) {
			logger.error("Error in searching subscriber info by search value : {} Exception :  {}", searchValue, Utils.getStackTrace(e));
		}
		return list;
	}

	@Override
	public Long getTotalSubscriberInfoCount(String searchValue) {
		logger.info("Going to count total subscriber info for search value : {}", searchValue);
		Long totalSubscriber = null;
		try {
			totalSubscriber = iSubscriberInfoDao.getTotalSubscriberCount(searchValue);
		} catch (Exception e) {
			logger.error("Error in getting total count of subscriber info by search value : {} Exception : {}", searchValue, Utils.getStackTrace(e));
		}
		return totalSubscriber;
	}

	@Override
	public SubscriberInfo getSubscriberDetailByMdn(String msisdn) {
		logger.info("Going to get subscriber detail by mdn : {} ", msisdn);
		SubscriberInfo subscriberInfo = null;
		try {
			subscriberInfo = iSubscriberInfoDao.getSubscriberDetailByMdn(msisdn);
		} catch (Exception e) {
			logger.error("Error in getting subscriber detail by msisdn : {} Exception : {}", msisdn, Utils.getStackTrace(e));
		}
		return subscriberInfo;
	}

	@Override
	public List<String> getSubscriberInfoType() {
		logger.info("Going to get subscriber info type");
		List<String> list = new ArrayList<>();
		try {
			list = ConfigUtils.getStringList(SubscriberInfoConstants.SUBSCRIBER_USERS);
			logger.info("TYPE LIST : {}", list);
		} catch (Exception e) {
			logger.error("Error in getting subscriber info type : {}", Utils.getStackTrace(e));
		}
		return list;
	}

	@Override
	public Map<String, String> getIMSIByMSISDN(String msisdn) {
		logger.info("Going to get imsi by msisdn : {}", msisdn);
		Map<String, String> imsiMap = new HashMap<>();
		try {
			SubscriberInfo subscriberInfo = iSubscriberInfoDao.getSubscriberDetailByMdn(msisdn);
			if (subscriberInfo != null && Utils.checkForValueInString(subscriberInfo.getImsi())) {
				imsiMap = getSubscriberCategoryByMdn(msisdn, subscriberInfo.getImsi());
			}
		} catch (Exception e) {
			logger.error("Error in getting imsi by msisdn : {}, Exception : {}", msisdn, Utils.getStackTrace(e));
		}
		return imsiMap;
	}

	@Override
	public Map<String, String> getSubscriberCategoryByMdn(String msisdn, String imsi) {
		logger.info("Going to get imsi by msisdn : {}", msisdn);
		Map<String, String> imsiMap = new HashMap<>();
		try {
			User user = userInContext.getUserInContextnew();
			if (user != null) {
				Set<String> permissionSet = UmUtils.getPermissionList(UmUtils.getPermissionOfRoles(user, UmConstants.WORKSPACE_ROLE));
				logger.info("PERMISSON LIST : {}", permissionSet.size());
				String category = getSubscriberInfoCategoryByMdn(msisdn);
				logger.info("MDN CATEGORY : {}", category);
				if (category != null && !category.isEmpty()) {
					populateImsiMap(imsiMap, permissionSet, category, imsi);
				} else {
					putDataToMap(imsiMap, null);
				}
			}
		} catch (Exception exception) {
			logger.error("Error while getting Imsi by mdn {} Exception {} ", msisdn, exception.getMessage());
		}
		logger.info("MSISDN MAP : {}", imsiMap);
		return imsiMap;
	}

	private void populateImsiMap(Map<String, String> imsiMap, Set<String> permissionSet, String category, String imsi) {
		if (category.equalsIgnoreCase(SubscriberInfoConstants.SUBSCRIBER_VVIP)) {
			if (permissionSet.contains(SubscriberInfoConstants.ROLE_CC_VVIP_MDN_QUERY_VIEW)) {
				setImsiAndCategory(imsiMap, category, imsi);
			} else {
				putDataToMap(imsiMap, SubscriberInfoConstants.SUBSCRIBER_VVIP_ERROR_MESSAGE);
			}
		} else if (category.equalsIgnoreCase(SubscriberInfoConstants.SUBSCRIBER_VIP)) {
			if (permissionSet.contains(SubscriberInfoConstants.ROLE_CC_VIP_MDN_QUERY_VIEW)) {
				setImsiAndCategory(imsiMap, category, imsi);
			} else {
				putDataToMap(imsiMap, SubscriberInfoConstants.SUBSCRIBER_VIP_ERROR_MESSAGE);
			}
		} else if (category.equalsIgnoreCase(SubscriberInfoConstants.SUBSCRIBER_REGULAR)) {
			setImsiAndCategory(imsiMap, category, imsi);
		}
	}

	private void putDataToMap(Map<String, String> imsiMap, String msg) {
		imsiMap.put(SubscriberInfoConstants.IMSI_LOWER_CARE, null);
		imsiMap.put(SubscriberInfoConstants.CATEGORY, null);
		imsiMap.put(SubscriberInfoConstants.SUBSCRIBER_ERROR_MSG, msg);
	}

	private void setImsiAndCategory(Map<String, String> imsiMap, String category, String imsi) {
		if (Utils.checkForValueInString(imsi)) {
			imsiMap.put(SubscriberInfoConstants.IMSI_LOWER_CARE, imsi);
			imsiMap.put(SubscriberInfoConstants.CATEGORY, category.toUpperCase());
			imsiMap.put(ForesightConstants.ERRORMSG, null);
		} else {
			imsiMap.put(SubscriberInfoConstants.IMSI_LOWER_CARE, null);
			imsiMap.put(SubscriberInfoConstants.CATEGORY, category.toUpperCase());
			imsiMap.put(ForesightConstants.ERRORMSG, null);
		}
	}

	private String getSubscriberInfoCategoryByMdn(String msisdn) {
		logger.info("Going to get subscriber info category by mdn : {}", msisdn);
		String category = null;
		SubscriberInfo subscriberInfo = null;
		try {
			subscriberInfo = iSubscriberInfoDao.getSubscriberDetailByMdn(msisdn);
		} catch (Exception e) {
			logger.error("Error in getting subscriber info category by mdn : {} Exception {}", msisdn, e.getMessage());
		}
		category = setSubscriberCategory(subscriberInfo);
		logger.info("CATEGORY  : {}", category);
		return category;
	}

	private String setSubscriberCategory(SubscriberInfo subscriberInfo) {
		String category;
		if (subscriberInfo != null && subscriberInfo.getCategory() != null && !subscriberInfo.getCategory().isEmpty()) {
			category = subscriberInfo.getCategory();
		} else {
			category = SubscriberInfoConstants.SUBSCRIBER_REGULAR;
		}
		return category;
	}

}
package com.inn.foresight.core.infra.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inn.core.generic.service.impl.AbstractService;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.dao.NEDetailDao;
import com.inn.foresight.core.infra.model.NEDetail;
import com.inn.foresight.core.infra.service.NEDetailService;
import com.inn.foresight.core.infra.utils.InfraConstants;
import com.inn.foresight.core.infra.utils.enums.Vendor;
import com.inn.foresight.core.infra.wrapper.NEDetailDataWrapper;

@Service("NEDetailServiceImpl")
public class NEDetailServiceImpl extends AbstractService<Integer, NEDetail> implements NEDetailService {

	/** The logger. */
	private Logger logger = LogManager.getLogger(NEDetailServiceImpl.class);

	@Autowired
	private NEDetailDao neDetailDao;

	@Override
	public List<String> getDistinctVIPCategories() {
		List<String> vipCategories = neDetailDao.getDistinctVIPCategories();
		return vipCategories;
	}

	@Override
	public List<String> getListOfVIPNetworkElement(List<String> neNameList, String domain, String vendor) {
		logger.info("inside getListOfVIPNetworkElement with domain:{},vendor:{},Size of Nenamelist:{}", domain, vendor,
				neNameList.size());
		try {
			List<Vendor> vendorList = new ArrayList<>();
			vendorList.add(Vendor.valueOf(vendor));
			return neDetailDao.getListOfVIPNetworkElement(neNameList, domain, vendorList);
		} catch (Exception e) {
			logger.error("Exception while getting ListOfVIP Nenames Error:{}", ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}
	
	@Override
	public List<NEDetailDataWrapper> getSiteParameterList(String searchValue,Integer llimit, Integer ulimit) {
		logger.info("Going to get site paramerter list : {}", searchValue);
		return neDetailDao.getSiteParameterList(searchValue, llimit, ulimit);
	}
	
	@Override
	public NEDetailDataWrapper getSiteDetailByNeName(String neName) {
		logger.info("Going to get site detail by site name  : {}", neName);
		return neDetailDao.getSiteDetailByNeName(neName);
	}
	
	@Override
	public Map<String, Integer> getSiteParameterCount(String searchValue) {
		logger.info("Going to count site paramerter list : {}", searchValue);
		Integer count = 0;
		Map<String, Integer> countMap = new HashMap<>();
		List<NEDetailDataWrapper> siteList = neDetailDao.getSiteParameterList(searchValue, null, null);
		if (siteList != null && !siteList.isEmpty()) {
			count = siteList.size();
			logger.info("Found site count = {} for search value = {}", count, searchValue);
		}
		countMap.put(ForesightConstants.COUNT, count);
		return countMap;
	}
	
	@Override
	@Transactional
	public Map<String, String> updateSiteParameterByNeName(NEDetailDataWrapper dataWrapper) {
		logger.info("Going to update site class by site name : {} and site class : {}", dataWrapper.getNeName(), dataWrapper.getSiteClass());
		Map<String, String> updateMap = new HashMap<>();
		try {
			if (neDetailDao.updateSiteClassByNeName(dataWrapper.getNeName(), dataWrapper.getSiteClass())) {
				updateMap.put(ForesightConstants.MESSAGE, ForesightConstants.SUCCESS_FOR_FILTER);
			} else {
				updateMap.put(ForesightConstants.MESSAGE, InfraConstants.FAILED_MESSAGE);
			}
		} catch (Exception e) {
			logger.error("Error in updating site class by site name : {}", Utils.getStackTrace(e));
		}
		return updateMap;
	}
	
	
	@Override
	public List<Object []> getClutterAndRadiusByNeName(String neName) {
		logger.info("Going to get Radius And  Clutter by site name  : {}", neName);
		try {
			if (neName != null) {
				return neDetailDao.getClutterAndRadiusByNeName(neName);
			}
		} catch (Exception e) {
			logger.error("Error in getting ClutterAndRadius by site name :{}",
					Utils.getStackTrace(e));
		}
		return null;
	}

}

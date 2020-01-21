package com.inn.foresight.core.generic.service.impl;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.core.generic.service.impl.AbstractService;
import com.inn.foresight.core.generic.dao.IOperatorDetailDao;
import com.inn.foresight.core.generic.model.OperatorDetail;
import com.inn.foresight.core.generic.service.IOperatorDetailService;
import com.inn.foresight.core.generic.utils.ForesightConstants;


@Service("OperatorDetailServiceImpl")
public class OperatorDetailServiceImpl extends AbstractService<Integer, OperatorDetail> implements IOperatorDetailService{

	/** The logger. */
	Logger logger = LogManager.getLogger(OperatorDetailServiceImpl.class);
	
	@Autowired
	IOperatorDetailDao iOpeatorDao;
	
	@Override
	public Set<String> getOperatorData(String searchType, String countryName, String module) {
		logger.info("Going to Search For Opeator Detail");
		List<OperatorDetail> dataBasedByType = iOpeatorDao.getSearchWiseData(searchType, countryName, module);
		 Set<String> response = null;
		if (searchType.equalsIgnoreCase(ForesightConstants.COUNTRY_KEY)) {
			response = dataBasedByType.stream().filter(x -> x.getCountry() != null).map(OperatorDetail::getCountry)
					.collect(Collectors.toSet());
		} else if (searchType.equalsIgnoreCase(ForesightConstants.OPERATOR_KEY)) {
			response = dataBasedByType.stream().filter(x -> x.getOperator() != null).map(OperatorDetail::getOperator)
					.collect(Collectors.toSet());
			Set<String> myTreeSet = new TreeSet<>();
			myTreeSet.addAll(response);
			response = myTreeSet;
		}
		logger.info("Response :: {}",	response!=null? response.toString():null); 
		return response;
	}

	@Override
	public OperatorDetail getOperatorByMCCAndMNC(Integer mcc, Integer mnc) {
		return iOpeatorDao.getOperatorByMCCAndMNC(mcc, mnc);
	}
}

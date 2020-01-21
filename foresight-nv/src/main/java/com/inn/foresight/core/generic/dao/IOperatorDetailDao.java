package com.inn.foresight.core.generic.dao;

import java.util.List;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.foresight.core.generic.model.OperatorDetail;

public interface IOperatorDetailDao  extends IGenericDao<Integer, OperatorDetail>{

	public List<OperatorDetail> getSearchWiseData(String searchType, String countryName, String module);
	
	public OperatorDetail getOperatorDetailListByOperatorName(String operatorName);
	
	public OperatorDetail getOperatorByMCCAndMNC(Integer mcc,Integer mnc);

}

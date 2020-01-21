package com.inn.foresight.core.generic.service;

import java.util.Set;

import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.core.generic.model.OperatorDetail;

public interface IOperatorDetailService  extends IGenericService<Integer, OperatorDetail>{

	public Set<String> getOperatorData(String searchType, String countryName, String module);
	
	public OperatorDetail getOperatorByMCCAndMNC(Integer mcc, Integer mnc);

}

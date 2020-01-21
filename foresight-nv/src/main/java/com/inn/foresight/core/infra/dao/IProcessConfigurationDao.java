package com.inn.foresight.core.infra.dao;

import java.util.List;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.foresight.core.infra.model.ProcessConfiguration;

public interface IProcessConfigurationDao  extends IGenericDao<Integer,ProcessConfiguration>{

	Long getProcessConfigurationCount();

	List<ProcessConfiguration> getProcessConfigurationByType(String type);

	List<ProcessConfiguration> getProcessConfiguration(Integer upperLimit, Integer lowerLimit);
	List<ProcessConfiguration> getProcessConfigurationByNameList(List<String> nameList);

	List<ProcessConfiguration> getAllProcessConfiguration();





}

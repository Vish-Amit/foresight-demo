package com.inn.foresight.core.infra.service;

import java.util.List;
import java.util.Map;

import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.core.infra.model.ProcessConfiguration;

public interface IProcessConfigurationService  extends IGenericService<Integer,ProcessConfiguration>{

	List<ProcessConfiguration> getProcessConfigurationByType(String type);

	Long getProcessConfigurationCount();

	List<ProcessConfiguration> getProcessConfigurationByNameList(List<String> nameList);

	Map<String, String> updateProcessConfiguration(ProcessConfiguration processconfiguration);

	List<ProcessConfiguration> getProcessConfiguration(Integer upperLimit, Integer lowerLimit);

	List<ProcessConfiguration> getAllProcessConfiguration();

	ProcessConfiguration createProcessConf(ProcessConfiguration processconfiguration);

}

package com.inn.foresight.core.infra.service;

import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.core.infra.model.AdvanceSearchConfiguration;

public interface IAdvanceSearchConfigurationService extends IGenericService<Integer, AdvanceSearchConfiguration> {

	String getBeanNameByType(String type);

}

package com.inn.foresight.core.infra.dao;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.foresight.core.infra.model.AdvanceSearchConfiguration;

public interface IAdvanceSearchConfigurationDao extends IGenericDao<Integer, AdvanceSearchConfiguration>{

	String getBeanNameByType(String type);

	AdvanceSearchConfiguration getAdvanceSearchConfigurationByType(String searchFieldType);

}

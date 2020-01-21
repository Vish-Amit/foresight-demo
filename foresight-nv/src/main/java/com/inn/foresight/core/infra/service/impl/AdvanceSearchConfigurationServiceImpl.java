package com.inn.foresight.core.infra.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.core.generic.service.impl.AbstractService;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.dao.IAdvanceSearchConfigurationDao;
import com.inn.foresight.core.infra.model.AdvanceSearchConfiguration;
import com.inn.foresight.core.infra.service.IAdvanceSearchConfigurationService;

@Service("AdvanceSearchConfigurationServiceImpl")
public class AdvanceSearchConfigurationServiceImpl extends AbstractService<Integer, AdvanceSearchConfiguration> implements IAdvanceSearchConfigurationService
{

	private Logger logger = LogManager.getLogger(AdvanceSearchConfigurationServiceImpl.class);

	
	
	@Autowired
	private IAdvanceSearchConfigurationDao iAdvanceSearchConfigurationDao;

	
	@Autowired
	public void setDao(IAdvanceSearchConfigurationDao advanceSearchConfigurationDao) {
		super.setDao(advanceSearchConfigurationDao);
		this.iAdvanceSearchConfigurationDao = advanceSearchConfigurationDao;
	}
	
	
	@Override
	public String getBeanNameByType(String type) 
	{
		logger.info("Inside getBeanNameByType : {}", type);
		String beanName = ForesightConstants.BLANK_STRING;
		try {
			if(type!=null && !type.trim().isEmpty()) {
				beanName=iAdvanceSearchConfigurationDao.getBeanNameByType(type);
			}
		}
		catch(Exception excepion) {
			logger.error("Error while getting bean name by type :{} ",Utils.getStackTrace(excepion));
		}
		return beanName;

	}

}

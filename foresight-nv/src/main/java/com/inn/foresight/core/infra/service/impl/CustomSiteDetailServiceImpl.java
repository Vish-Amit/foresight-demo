package com.inn.foresight.core.infra.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.inn.core.generic.service.impl.AbstractService;
import com.inn.foresight.core.infra.service.ICustomSiteDetailService;

@Service("CustomSiteDetailServiceImpl")
public class CustomSiteDetailServiceImpl extends AbstractService<Integer, Object> implements ICustomSiteDetailService	{

	private Logger logger = LogManager.getLogger(BuildingDataServiceImpl.class);
		
}

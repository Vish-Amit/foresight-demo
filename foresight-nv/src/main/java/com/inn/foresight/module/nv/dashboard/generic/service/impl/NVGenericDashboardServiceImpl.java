package com.inn.foresight.module.nv.dashboard.generic.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.foresight.module.nv.dashboard.generic.service.NVGenericDashboardService;
import com.inn.product.legends.dao.ILegendRangeDao;
import com.inn.product.legends.utils.LegendWrapper;

@Service("NVGenericDashboardServiceImpl")
public class NVGenericDashboardServiceImpl implements NVGenericDashboardService{

	private Logger logger = LogManager.getLogger(NVGenericDashboardServiceImpl.class);
	
	@Autowired
	ILegendRangeDao legendRangeDao;
	
	@Override
	public List<LegendWrapper> getLegendRangesDetail(String appliedTo) {
		logger.info("Going to get Legend Ranges from appliedTo {}",appliedTo); 
		return legendRangeDao.findAllLegendRangesByAppliedTo(appliedTo);
	}

}

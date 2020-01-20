package com.inn.foresight.module.nv.workorder.stealth.service;

import static org.junit.Assert.assertNull;

import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.inn.foresight.module.nv.workorder.stealth.service.impl.StealthTaskDetailServiceImpl;
import com.inn.foresight.module.nv.workorder.stealth.wrapper.StealthWOWrapper;
import com.inn.foresight.test.SpringJUnitRunner;

public class TestStealthTaskMicroServiceImpl extends SpringJUnitRunner {
	private  Logger logger = LogManager.getLogger(StealthTaskDetailServiceImpl.class);
	@Autowired IStealthTaskService iStealthTaskService;
	
	//Map Visualization
	@org.junit.Test
	public void testGetStealthKPISummary() {
		List<StealthWOWrapper> result = null;
		try {
			assertNull(iStealthTaskService.getStealthKPISummary(null, null, null, null));
			result = iStealthTaskService.getStealthKPISummary(5791,17,1531213844l, 1531213844l);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Exception : {} ",ExceptionUtils.getStackTrace(e));
		}
		logger.info("result : {}",result);
	}
	
}

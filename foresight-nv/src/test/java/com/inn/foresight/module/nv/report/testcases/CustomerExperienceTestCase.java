package com.inn.foresight.module.nv.report.testcases;

import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.inn.foresight.module.nv.report.service.ICustomerExperienceReportService;
import com.inn.foresight.test.SpringJUnitRunner;

public class CustomerExperienceTestCase  extends SpringJUnitRunner{
	
	private  Logger logger = LogManager.getLogger(CustomerExperienceTestCase.class);
	
	@Autowired
	ICustomerExperienceReportService cReportService;
	
	//@Test
	public void callExecute(){
		String json="{\"assignTo\": \"admin admin\", \"mobility\": {}, \"stationary\": [{\"location\": \"Floor1\", \"workorderId\": 6748}, {\"location\": \"Floor 2\", \"workorderId\": 6750}, {\"location\": \"Floor 3\", \"workorderId\": 6751}]}";
		Response response = cReportService.execute(json);
		logger.info("response {} ",response);
	}
	

}

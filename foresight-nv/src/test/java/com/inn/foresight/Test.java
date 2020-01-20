package com.inn.foresight;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.hbase.thirdparty.com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.customercare.rest.ICustomerPlannedEventRest;
import com.inn.foresight.module.nv.webrtc.dao.impl.WebRTCDaoImpl;
import com.inn.foresight.test.SpringJUnitRunner;

/**
 * Class to test code base.
 */
public class Test extends SpringJUnitRunner {
	private Logger logger = LogManager.getLogger(WebRTCDaoImpl.class);
	
	@Autowired
	ICustomerPlannedEventRest rest;
	

	@org.junit.Test
	@Transactional
	public void data() throws DaoException {
		try {

       	 String startDateString="2019-06-18";  
       	    Date startDate=new SimpleDateFormat("dd/MM/yyyy").parse(startDateString);  
       	    
       	    String endDateString="2019-06-22";  
       	    Date endDate=new SimpleDateFormat("dd/MM/yyyy").parse(endDateString);  
       	
       	
       	System.out.println(new Gson().toJson(rest.getAllPlannedEventForSite("AB1184600684",startDate ,endDate )));
		} catch (Exception e) {
			logger.error(" {}", Utils.getStackTrace(e));
		}
}}
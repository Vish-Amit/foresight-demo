package com.inn.foresight;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.inn.foresight.core.infra.dao.IJobHistoryDao;
import com.inn.foresight.core.infra.service.IJobHistoryService;
import com.inn.foresight.test.SpringJUnitRunner;

/**
 * Class to test code base.
 */
public class TestApp extends SpringJUnitRunner {

	
	@Autowired
	private IJobHistoryService jobService;
	
	
	
	@Autowired
	private IJobHistoryDao jobDao;
	
    
    @Test
    public void test() {
        System.out.println("Context up successfully");
       // jobService.excelRW();
       // jobService.updateExcel();
       // System.out.println("done");
       // jobService.writeToExcel();
        //jobDao.selectAllFromNE();
        jobDao.selectAllFromNAD();
        
       // jobService.callMethodWithInteration();
        
        
    }

}

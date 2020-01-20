package com.inn.foresight.module.nv.report.testcases;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.inn.foresight.module.coverage.service.ICoverageService;
import com.inn.foresight.module.nv.layer3.constants.QMDLConstant;
import com.inn.foresight.module.nv.layer3.service.INVLayer3DashboardService;
import com.inn.foresight.module.nv.report.dao.INVReportHbaseDao;
import com.inn.foresight.module.nv.report.service.ISSVTReportService;
import com.inn.foresight.module.nv.report.workorder.wrapper.DriveDataWrapper;
import com.inn.foresight.module.nv.report.wrapper.SiteInformationWrapper;
import com.inn.foresight.test.SpringJUnitRunner;

public class SSVTTestCase extends SpringJUnitRunner{

	private  Logger logger = LogManager.getLogger(SSVTTestCase.class);

	@Autowired
	ISSVTReportService ssvtReportService;
	@Autowired
	INVLayer3DashboardService nvLayer3Service;
	@Autowired
	ICoverageService coverageService;
	@Autowired
	INVReportHbaseDao nvReportHbaseDao;

	// Windows-->Preferences--Java--Editor--typing--check tick (Escape when pasting to string literal)

	Map<String, List<String>> driveRecipeMap =null; 

	//@Test
	public void getAtollImage(){
//		BufferedImage atollRsrpImg =coverageService.getAtollCoverageImageforKpiInViewPort(22.71700858288426, 75.86646266189854, 22.721589990832975, 
//				75.8721180988489, 17, "RSRP","com", "OnAir");
		try{
//			ImageIO.write(atollRsrpImg, "PNG", new File("/home/ist/atoll.png"));
		}catch(Exception e){
			logger.error("Exception inside method getAtollImage {} ",e.getMessage());	
		}
	}

	//@Test
	public void getDriveRecipeMap(){
		driveRecipeMap = nvLayer3Service.getDriveRecipeDetail(7131);
		logger.info("driveRecipeMap  Data {} ",driveRecipeMap);
	}

	//@Test
	public void callExecuteMethod(){
		String json= "{\"assignedTo\": \"Sachin Choudhary\", \"workorderId\": 7131}";
		ssvtReportService.execute(json);
	}

	

	//@Test
	public void getNeighbourSites(){
		List<SiteInformationWrapper> listOfSiteInfo = ssvtReportService.getFirstTierNieghbourSites(new ArrayList<>());
		logger.info("listOfSiteInfo Data {} ",listOfSiteInfo.toString());
	}

	//@Test
	public void getSpeedTestData(){
		Integer workorderId=7131;
		List<DriveDataWrapper> speedTestDataList = nvReportHbaseDao.getSpeedTestDatafromHbase(workorderId, driveRecipeMap.get(QMDLConstant.RECIPE), driveRecipeMap.get(QMDLConstant.OPERATOR));
		logger.info("speedTestDataList {} ",speedTestDataList);
	}

}

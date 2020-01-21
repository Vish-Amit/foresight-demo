package com.inn.foresight.core.infra.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.service.impl.AbstractService;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.dao.IJobHistoryDao;
import com.inn.foresight.core.infra.model.JobHistory;
import com.inn.foresight.core.infra.service.IJobHistoryService;
import com.inn.product.systemconfiguration.dao.SystemConfigurationDao;
import com.inn.product.um.user.model.User;


@Service("JobHistoryServiceImpl")
public class JobHistoryServiceImpl extends AbstractService<Integer, JobHistory> implements IJobHistoryService  {

	
	/** The logger. */
	private Logger logger = LogManager.getLogger(JobHistoryServiceImpl.class);
	

	
	/** The dao. */
	@Autowired
	private IJobHistoryDao dao;
	
	
	@Autowired
	private SystemConfigurationDao sysDao;

	
	@Autowired
	public void setDao(IJobHistoryDao dao) {
		super.setDao(dao);
		this.dao=dao;
	}

	
	
	/**
	 * Gets the Job History by name
	 * @param name the name
	 * @return the Job History by name
	 * @return the Job History by max date
	 * 
	 */
	@Override
	public String getvalueByName(String name){
		if(name != null)
		{
			return dao.getvalueByName(name);
		}
	return null;
		
	}

	/**
	 * Gets the Job History by name and date
	 * @param name the key
	 * @param value the date
	 * @return the date < givendate
	 * @throws RestException the rest exception
	 */
	@Override
	public String getLastAvailableTileDateByDateAndKey(String key, String date) {
		logger.info("Inside  @getLastAvailableTileDateByDateAndKey Service with key {} and date {}",key,date);
		try {
			String tileDate = dao.getLastAvailableTileDateByDateAndKey(key, date);
			logger.info("tileDate {} :"+tileDate);
			return new Gson().toJson(tileDate);
		} catch (Exception e) {
			logger.info("Error in Service getLastAvailableTileDateByDateAndKey :{}",ExceptionUtils.getStackTrace(e));
			return null;
		}
	}
	/**
	 * Gets the latest available tile date.
	 *
	 * @param startDate the start date
	 * @param endDate the end date
	 * @param name the key
	 * @return the latest available tile date
	 * @throws RestException the rest exception
	 */
	@Override
	public String getLatestAvailableTileDate(String startDate, String endDate ,String key) {
		logger.info("Inside  @getLatestAvailableTileDate Service with startDate {},endDate {} and date {}",startDate,endDate,key);
		try {
			String tileDate = dao.getLatestAvailableTileDate(startDate, endDate, key);
			logger.info("tileDate{}:" + tileDate);
			return new Gson().toJson(tileDate);
		} catch (Exception e) {
			logger.info("Error in Service getLatestAvailableTileDate :{}", ExceptionUtils.getStackTrace(e));
			return null;
		}
	}



	@Override
	public String validateParametersResult(String name, String date, String band) {
		logger.info("Inside validateParametersResult in class: {} with date: {} name {} and band: {}",this.getClass().getSimpleName(),date,name,band);
		try {
			String weekno = Utils.getWeekNumberWithYear(Utils.parseStringToDate(date, ForesightConstants.DATE_FORMAT_YYYYMMDD));
			String key = (band != null && !band.isEmpty())?name+ForesightConstants.UNDERSCORE+band:name;
			logger.info("going to get data for week: {} key: {}",weekno,key);
			String result = dao.getvalueByNameAndWeekNo(key, weekno);
			logger.info("result: {}",result);
			return (result!=null && !result.isEmpty())?ForesightConstants.SUCCESS_JSON:ForesightConstants.FAILURE_JSON;
			 
		} catch (Exception e) {
			logger.error("Error while getting result for parameter: {}",ExceptionUtils.getStackTrace(e));
			throw new RestException(ForesightConstants.FAILURE_JSON);
		}
	}
	 
	@Override
	public Map<String, String> getMaxValueByKeyAndDate(String name, String band) {
		logger.info("Inside getMaxValueByKeyAndDate in class: {}",this.getClass().getSimpleName());
		Map<String, String> map = new HashMap<>();
		try {
			String key = name + ForesightConstants.UNDERSCORE + band;
			String value = dao.getMaxValueByKeyAndDate(key);
			logger.info("result: {}", value);
		    
		    if(value!=null) {
		    	Date date = Utils.parseStringToDate(value, ForesightConstants.DATE_FORMAT_yyyy_MM_dd);
			    Date[] dates = Utils.getStartAndEndDay(date);
			    
			String minValue = Utils.parseDateToString(dates[0], ForesightConstants.DATE_FORMAT_yyyy_MM_dd);
			String maxValue = Utils.parseDateToString(dates[1], ForesightConstants.DATE_FORMAT_yyyy_MM_dd);
				map.put("minDate", minValue);
				map.put("maxDate", maxValue);
				map.put("format",ForesightConstants.DATE_FORMAT_YYYY_MM_DD);
			}
			return map;
		} catch (Exception e) {
			logger.error("Error in Service getMaxValueByKeyAndDate :{}", ExceptionUtils.getStackTrace(e));
			throw new RestException(e);
		}
	}



	@Override
	public List<String> getvalueListByName(String name, String limit) {
		if(name != null && limit != null)
		{			
			return dao.getvalueListByName(name,limit);
		}
	    return null;
	}
	
	
	@Override
	public void callMethodWithInteration() {
		
	
	
		logger.info("callMethodWithInteration method calling ");
		try {
			Integer loopIteration=0;
			Integer loopInterval=0;
			for(int i =1 ; i <= (loopIteration = Integer.parseInt(sysDao.getValueByName("CC_JSI_SCHEDULING_ITERATION"))) ; i++) {
				int totalIter=0;
				totalIter=i;
				logger.info("total iteration {}",totalIter);
				logger.info("Inside loop iteration : {}", loopIteration);
				loopInterval = Integer.parseInt(sysDao.getValueByName("CC_JSI_SCHEDULING_INTERVAL"));
				logger.info("interval {}" , loopInterval);
				Thread.sleep(loopInterval);
			}
					
			logger.info("loop break here");
					
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error ", Utils.getStackTrace(e));
		}
	}
	
	
public static void main(String[] args) {
		try {

			
			String stringToParse ="{\"interation\":23, \"interval\":3000}";
			
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(stringToParse);
			
			System.out.println(json);
			
			
			System.out.println("=========================end loop");
		} catch (Exception e) {
		e.printStackTrace();
		}
		
		
}
	
	
	
	@Override
	public void selectAllFromNE(){
		List<User> listNE= new ArrayList<>();
//	logger.info("list  : {}", listNE.size());
		/*
		for(User list: listNE){
			logger.info("result {}",list );
		
	}	*/
	}
}

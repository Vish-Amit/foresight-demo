package com.inn.foresight.core.infra.rest.impl;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.cxf.jaxrs.ext.search.SearchContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.rest.AbstractCXFRestService;
import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.infra.model.JobHistory;
import com.inn.foresight.core.infra.service.IJobHistoryService;

@Path("/JobHistory")
@Produces("application/json")
@Consumes("application/json")
@Service("JobHistoryRestImpl")
public class JobHistoryRestImpl extends AbstractCXFRestService<Integer, JobHistory>{
	/** The logger. */
	private Logger logger = LogManager.getLogger(JobHistoryRestImpl.class);
	
	
	@Autowired
	private IJobHistoryService service;

	/**
	 * Instantiates a new advance search rest impl.
	 */
	public JobHistoryRestImpl() {
		super(JobHistory.class);
	}

	@Override
	public List<JobHistory> search(JobHistory entity) {
		// TODO Auto-generated method stub
		return service.search(entity);
	}

	@Override
	public JobHistory findById(@NotNull Integer primaryKey) {
		// TODO Auto-generated method stub
		return service.findById(primaryKey);
	}

	@Override
	public List<JobHistory> findAll() {
		// TODO Auto-generated method stub
		return service.findAll();
	}

	@Override
	public JobHistory create(@Valid JobHistory anEntity) {
		// TODO Auto-generated method stub
		return service.create(anEntity);
	}

	@Override
	public JobHistory update(@Valid JobHistory anEntity) {
		// TODO Auto-generated method stub
		return service.update(anEntity);
	}

	@Override
	public boolean remove(@Valid JobHistory anEntity) {
		// TODO Auto-generated method stub
		service.remove(anEntity);
		return true;
	}

	@Override
	public void removeById(@NotNull Integer primaryKey) {
		// TODO Auto-generated method stub
		service.removeById(primaryKey);
		
	}

	@Override
	public IGenericService<Integer, JobHistory> getService() {
		// TODO Auto-generated method stub
		return service;
	}

	@Override
	public SearchContext getSearchContext() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Gets the value by name 
	 * @param name the name
	 * @return the value by name
	 * @return the value by max date
	 * 
	 */
	@GET
	@Path("getvalueByName/{name}")
	public String getvalueByName(@PathParam("name") String name){
		return service.getvalueByName(name);
	}
	
	/**
	 * Gets the Job History by key and date
	 * @param name the key
	 * @param value the date
	 * @return the Job History by key
	 * @return the Job History by date
	 * @return the Job History by date < given date
	 * @throws RestException 
	 * 
	 */
	@GET
	@Path("getLastSncDateByDateAndKey/{key}/{date}")
	public String getLastAvailableTileDateByDateAndKey(
			@PathParam(ForesightConstants.SMALL_KEY) String key,@PathParam(ForesightConstants.DATE) String date){
		logger.info("Going to getLastSncDateByDateAndKey by date {} and key {} ",date,key);
		try{
			if (date != null && key != null) {			
				return service.getLastAvailableTileDateByDateAndKey(key, date);
			}
		 }catch(Exception e){
				logger.error("Exception while getting current Snc date "+ExceptionUtils.getStackTrace(e));
				
			}
		return null;
		
	}
	
	
	/**
	 * Gets the latest available tile date.
	 *
	 * @param startDate the start date
	 * @param endDate the end date
	 * @param key the key
	 * @return the latest available tile date
	 * @throws RestException the rest exception
	 */
	@GET
	@Path("getLatestAvailableTileDate/{startDate}/{endDate}/{key}")
	public String getLatestAvailableTileDate(@PathParam(ForesightConstants.START_DATE) String startDate,@PathParam(ForesightConstants.END_DATE) String endDate , @PathParam(ForesightConstants.SMALL_KEY) String key) {
		logger.info("Going to getLastSncDateByDateAndKey by startDate {} ,endDate{}, key {}  ",startDate,endDate,key);
		try{		
				return service.getLatestAvailableTileDate(startDate,endDate,key);
				
		}catch(Exception e){
			logger.warn("NO ENTITY FOUND in getLatestAvailableTileDate");
		}
		 return null;
		 
	}
	
	@GET
	@Path("validateParametersResult")
	public String validateParametersResult(@QueryParam("name")String name,@QueryParam("date")String date,@QueryParam("band")String band) {
		try {
			logger.info("Inside validateParametersResult with param name: {} date: {} band: {}",name,date,band);
			return service.validateParametersResult(name,date,band);
		} catch (Exception e) {
			logger.error("Error while validating results for param: {}",ExceptionUtils.getStackTrace(e));
		}
		throw new RestException(ForesightConstants.FAILURE_JSON);
	}
	
	@GET
	@Path("getMaxValueByKeyAndDate")
	public Map<String, String> getMaxValueByKeyAndDate(@QueryParam("name") String name, @QueryParam("band") String band){
		logger.info("Inside getMaxValueByKeyAndDate with name: {}, band {} ", name, band);
		try {
			return service.getMaxValueByKeyAndDate(name, band);
		} catch (Exception e) {
			logger.error("Error in getMaxValueByKeyAndDate {}",ExceptionUtils.getStackTrace(e));
		} 
		throw new RestException(ForesightConstants.FAILURE_JSON);
	}
	

	@GET
	@Path("getvalueListByName/{name}/{limit}")
	public List<String> getvalueListByName(@PathParam("name") String name,@PathParam("limit") String limit){
		logger.info("Inside getvalueListByName with name: {}, limit {} ", name, limit);
		try {
			return service.getvalueListByName(name,limit);
		} catch (Exception e) {
			logger.error("Error in getvalueListByName  {}",ExceptionUtils.getStackTrace(e));
		}
		throw new RestException(ForesightConstants.FAILURE_JSON);
	}
	
	
	
}

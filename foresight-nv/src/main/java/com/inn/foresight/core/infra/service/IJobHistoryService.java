package com.inn.foresight.core.infra.service;

import java.util.List;
import java.util.Map;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.core.infra.model.JobHistory;

public interface IJobHistoryService extends IGenericService<Integer, JobHistory>{

	/**
	 * Gets the Job History by name .
	 *
	 * @param name the name
	 * @return the Job History by name
	 */
	String getvalueByName(String name);
	
	/**
	 * Gets the Job History by name and date.
	 *
	 * @param name the name
	 * @param date the date
	 * @return the Job History by name
	 * @return the Job History by value
	 * @throws RestException 
	 */
	String getLastAvailableTileDateByDateAndKey(String name, String date);

	String getLatestAvailableTileDate(String startDate, String endDate, String key);

	String validateParametersResult(String name, String date, String band);

	Map<String, String> getMaxValueByKeyAndDate(String name, String band);

	List<String> getvalueListByName(String name, String limit);

   public void selectAllFromNE();

void callMethodWithInteration();

	
}

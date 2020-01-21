package com.inn.foresight.core.infra.dao;

import java.util.List;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.infra.model.JobHistory;
import com.inn.foresight.core.infra.model.NetworkElement;
import com.inn.product.um.user.model.User;

public interface IJobHistoryDao extends IGenericDao<Integer, JobHistory>{
	
	
	
	public String getvalueByNameAndWeekNo(String name,String weekno);

	/**
	 * Gets the Job History by name and type.
	 *
	 * @param name the name
	 * @return the job history by name
	 */
	public String getvalueByName(String name);
	
	/**
	 * Gets the Job History by name and value.
	 *
	 * @param name the name
	 * @param value the date
	 * @return the job history by name
	 * @return the job history by value
	 * @throws RestException 
	 */
	public String getLastAvailableTileDateByDateAndKey(String key, String date);

	public String getLatestAvailableTileDate(String startDate, String endDate, String key);

	public String getMaxValueByKeyAndDate(String key);


	public List<JobHistory> getListOfDateByName(String name);

	List<Object[]> getWeekNoAndValueByName(String name);

	public List<String> getvalueListByName(String name, String limit);

	JobHistory getJobhistoryByNameandValue(String name, String value);

	void selectAllFromNE();

	void selectAllFromNAD();



}

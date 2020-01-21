package com.inn.foresight.core.generic.dao;

import java.util.List;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.foresight.core.generic.model.JobStatus;

/**
 * The Interface IJobStatusDao.
 */
public interface IJobStatusDao extends IGenericDao<Long, JobStatus> {

	/**
	 * Gets the latest record for type.
	 *
	 * @param type the type
	 * @return the latest record for type
	 */
	public JobStatus getLatestRecordForType(String type);

	/**
	 * Gets the constants detail.
	 *
	 * @param constantList the constant list
	 * @return the constants detail
	 */
	public List<JobStatus> getConstantsDetail(List<String> constantList);

}

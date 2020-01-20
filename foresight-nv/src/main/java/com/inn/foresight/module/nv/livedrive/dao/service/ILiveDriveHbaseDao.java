package com.inn.foresight.module.nv.livedrive.dao.service;

import com.inn.commons.hadoop.hbase.HBaseResult;
import com.inn.core.generic.exceptions.application.RestException;

/** The Interface ILiveDriveHbaseDao. */
public interface ILiveDriveHbaseDao {

	/**
	 * Gets the result for drive result by task id.
	 *
	 * @param taskId the task id
	 * @return the result for drive result by task id
	 * @throws RestException the rest exception
	 * @throws RestException the rest exception
	 */
	HBaseResult getResultForDriveResultByTaskId(Integer taskId);

}

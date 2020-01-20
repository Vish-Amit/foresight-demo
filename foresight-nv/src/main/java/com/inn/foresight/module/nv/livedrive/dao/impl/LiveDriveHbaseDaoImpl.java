package com.inn.foresight.module.nv.livedrive.dao.impl;

import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.hadoop.hbase.HBaseResult;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.dao.impl.hbase.AbstractHBaseDao;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.module.nv.livedrive.constants.LiveDriveConstant;
import com.inn.foresight.module.nv.livedrive.dao.service.ILiveDriveHbaseDao;

/** The Class LiveDriveHbaseDaoImpl. */
@Service("LiveDriveHbaseDaoImpl")
public class LiveDriveHbaseDaoImpl extends AbstractHBaseDao implements
		ILiveDriveHbaseDao {

	/** The logger. */
	private static final Logger logger = LogManager
			.getLogger(LiveDriveHbaseDaoImpl.class);

	/**
	 * Gets the result for drive result by task id.
	 *
	 * @param taskId
	 *            the task id
	 * @return List of Result
	 * @throws RestException
	 *             the rest exception
	 */
	@Override
	public HBaseResult getResultForDriveResultByTaskId(Integer taskId) {
		logger.info("inside getResultForDriveResultByTaskId taskId : {}",taskId);
		String taskIdValue = ForesightConstants.BLANK_STRING;

		if (taskId != null) {
			taskIdValue = String.valueOf(taskId);
		}
		try {
			Get liveDriveData = new Get(Bytes.toBytes(taskIdValue));
			HBaseResult result = getResultByPool(liveDriveData,
					ConfigUtils
					.getString(LiveDriveConstant.LIVE_DRIVE_TABLE),
			null);
			logger.info("inside getResultForDriveResultByTaskId result : {}",result);
			return result;
		} catch (Exception e) {
			logger.error("unable to get data {}", e.getMessage());
			throw new RestException("unable to get data  " + e.getMessage());
		}
	}

}

package com.inn.foresight.module.nv.inbuilding.dao.impl;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.inn.commons.configuration.ConfigUtils;
import com.inn.foresight.core.generic.utils.ConfigUtil;
import com.inn.foresight.core.infra.constants.InBuildingConstants;
import com.inn.foresight.module.nv.hbase.NVHbaseGenericDao;
import com.inn.foresight.module.nv.inbuilding.dao.IFloorPlanHbaseDao;
import com.inn.foresight.module.nv.inbuilding.utils.NVBuildingUtils;

/**
 * @author Innoeye
 */
@Service("FloorPlanHbaseDaoImpl")
public class FloorPlanHbaseDaoImpl extends NVHbaseGenericDao implements IFloorPlanHbaseDao {

	/** The logger. */
	private static final Logger logger = LogManager.getLogger(FloorPlanHbaseDaoImpl.class);

	/**
	 * Update IB floor plan data to hbase.
	 *
	 * @param tableName
	 *            the table name
	 * @param floorPlanPut
	 *            the floor plan put
	 * @return the string
	 */
	@Override
	public String insertIBFloorPlanDataToHbase(Put floorPlanPut) {
		if (floorPlanPut != null) {
			try {
				insert(floorPlanPut, ConfigUtils.getString(ConfigUtil.FLOOR_PLAN_TABLE));
				logger.info("Floor plan data inserted successfully");
				return InBuildingConstants.SUCCESS_JSON;
			} catch (Exception e) {
				logger.error("Exception in uploadFloorPlan : {}", ExceptionUtils.getStackTrace(e));
				return ExceptionUtils.getMessage(e);
			}
		} else {
			return InBuildingConstants.FAILURE_JSON;
		}
	}

	/**
	 * Gets the IB floor plan image.
	 *
	 * @param rowKey
	 *            the row key
	 * @return the IB floor plan image
	 */
	@Override
	public Result getIBFloorPlan(String rowKey) {
		logger.info("Going to downloadFloorPlan  for rowKey: {}", rowKey);
		Get get = NVBuildingUtils.getFloorPlanGetForRowKey(rowKey);
		Result result = getResultForRowkey(get, ConfigUtils.getString(ConfigUtil.FLOOR_PLAN_TABLE));
        if(result.isEmpty()) {
        	return null;
        }
		logger.info("getIBFloorPlanImage success");
		return result;
	}

	@Override
	public boolean isFloorPlanAvailable(String rowKey) {
		Get get = NVBuildingUtils.getFloorPlanGetForRowKey(rowKey);
		Result result = getResultForRowkey(get, ConfigUtils.getString(ConfigUtil.FLOOR_PLAN_TABLE));
        return result.isEmpty();
	}

}
package com.inn.foresight.module.nv.inbuilding.dao;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;

/** The Interface IFloorPlanHbaseDao. */
public interface IFloorPlanHbaseDao {

	/**
	 * Update IB floor plan data to hbase.
	 *
	 * @param floorPlanPut the floor plan put
	 * @return the string
	 */
	String insertIBFloorPlanDataToHbase( Put floorPlanPut);

	/**
	 * Gets the IB floor plan image.
	 *
	 * @param rowKey the row key
	 * @return the IB floor plan image
	 */
	Result getIBFloorPlan(String rowKey);

	/**
	 * Checks if is floor plan available.
	 *
	 * @param rowKey the rowkey
	 * @return true, if is floor plan available
	 */
	boolean isFloorPlanAvailable(String rowKey);
}
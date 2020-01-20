package com.inn.foresight.module.nv.customercare.rest;

import javax.ws.rs.core.Response;

public interface INVCustomerCareRest {
	/**
	 * Gets the NV latest data.
	 *
	 * @param type the type
	 * @param imei the imei
	 * @param imsi the imsi
	 * @param deviceId the device id
	 * @param minTimeStamp the min time stamp
	 * @param maxTimeStmap the max time stmap
	 * @param lLimit the l limit
	 * @param noOfRecords the no of records
	 * @param rowKey the row key
	 * @return the NV latest data
	 */
	Response getNVLatestData(String type,  String deviceId,
			 String imei, String imsi,Long minTimeStamp, Long maxTimeStmap, String lastRowKey, Long noOfRecords,String rowKey);
}

package com.inn.foresight.module.nv.customercare.service;

import java.util.List;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.module.nv.customercare.wrapper.NVCustomerCareDataWrapper;

/** The Interface IActivePassiveService. */
public interface INVCustomerCareService {

	/**
	 * Gets the NV latest data.
	 *
	 * @param type the type
	 * @param deviceId the device id
	 * @param imei the imei
	 * @param imsi the imsi
	 * @param minTimeStamp the min time stamp
	 * @param maxTimeStmap the max time stmap
	 * @param lastRowKey the last row key
	 * @param noOfRecords the no of records
	 * @param rowKey the row key
	 * @return the NV latest data
	 * @throws RestException the rest exception
	 */
	String getNVLatestData(String type, String deviceId, String imei, String imsi, Long minTimeStamp, Long maxTimeStmap,
			String lastRowKey, Long noOfRecords, String rowKey);

	List<NVCustomerCareDataWrapper> getNVLatestData(String type, String deviceIds, Long minTimeStamp, Long maxTimeStmap,
			String lastRowKey, Long noOfRecords, String rowKey);

	NVCustomerCareDataWrapper getLatestSpeedTestDataByDeviceId(String deviceId, Double latitude, Double longitude);

}

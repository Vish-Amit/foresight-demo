package com.inn.foresight.module.nv.customercare.rest.impl;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.foresight.module.nv.customercare.constant.NVCustomerCareConstant;
import com.inn.foresight.module.nv.customercare.rest.INVCustomerCareRest;
import com.inn.foresight.module.nv.customercare.service.INVCustomerCareService;

@Path("/NVData")
@Service("NVCustomerCareRestImpl")
public class NVCustomerCareRestImpl implements INVCustomerCareRest {

	@Autowired
	private INVCustomerCareService invCustomerCareService;

	/**
	 * Gets the NV latest data.
	 *
	 * @param type
	 *            the type
	 * @param imei
	 *            the imei
	 * @param imsi
	 *            the imsi
	 * @param deviceId
	 *            the device id
	 * @param minTimeStamp
	 *            the min time stamp
	 * @param maxTimeStmap
	 *            the max time stmap
	 * @param lLimit
	 *            the l limit
	 * @param noOfRecords
	 *            the no of records
	 * @param rowKey
	 *            the row key
	 * @return the NV latest data
	 */
	@Override
	@Produces(MediaType.TEXT_PLAIN)
	@Path("getNVLatestData")
	@GET
	public Response getNVLatestData(@QueryParam("type") String type, @QueryParam("deviceId") String deviceId,
			@QueryParam("imei") String imei, @QueryParam("imsi") String imsi,
			@QueryParam("minTimeStamp") Long minTimeStamp, @QueryParam("maxTimeStmap") Long maxTimeStmap,
			@QueryParam("lastRowKey") String lastRowKey, @QueryParam("noOfRecords") Long noOfRecords,
			@QueryParam("rowKey") String rowKey) {
		Response responseToReturn = null;
		if (type != null && ((deviceId != null || rowKey != null) || (imei != null || imsi != null))) {
			responseToReturn = Response	.ok(invCustomerCareService.getNVLatestData(type, deviceId, imei, imsi,
					minTimeStamp, maxTimeStmap, lastRowKey, noOfRecords, rowKey))
										.build();
		} else {
			responseToReturn = Response	.ok(NVCustomerCareConstant.INVALID_PARAMETER_JSON)
										.build();
		}
		return responseToReturn;
	}

}

package com.inn.foresight.module.nv.customercare.rest.impl;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.inn.foresight.module.nv.customercare.constant.NVCustomerCareConstant;
import com.inn.foresight.module.nv.customercare.service.INVCustomerCareService;
import com.inn.foresight.module.nv.customercare.wrapper.NVCustomerCareDataWrapper;

@Path("/ms/NVData")
public class NVCustomerCareMicroServiceRestImpl   {

	/** The logger. */
	private static final Logger logger = LogManager.getLogger(NVCustomerCareRestImpl.class);
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
	@GET
	@Path("/getNVLatestData")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNVLatestData(@QueryParam("type")String type,@QueryParam("deviceIds")String deviceIds,@QueryParam("minTimeStamp") Long minTimeStamp,@QueryParam("maxTimeStmap") Long maxTimeStmap,
			@QueryParam("lastRowKey")String lastRowKey,@QueryParam("noOfRecords") Long noOfRecords, @QueryParam("rowKey")String rowKey) {
		logger.info("Going to getNVLatestData type:{}", type);
		Response responseToReturn;
		if (type != null && (deviceIds != null || rowKey != null)) {
			List<NVCustomerCareDataWrapper> activePassiveDataWrappers = invCustomerCareService.getNVLatestData(type,
					deviceIds, minTimeStamp, maxTimeStmap, lastRowKey, noOfRecords, rowKey);
			if (activePassiveDataWrappers != null) {
				responseToReturn = Response	.ok(new Gson().toJson(activePassiveDataWrappers))
											.build();
			} else {
				responseToReturn = Response	.ok(NVCustomerCareConstant.FAILURE_JSON)
											.build();
			}
		} else {
			responseToReturn = Response	.ok(NVCustomerCareConstant.INVALID_PARAMETER_JSON)
										.build();

		}
		return responseToReturn;
	}

	@GET
	@Produces("application/json")
	@Consumes("application/json")
	@Path("getLatestSpeedTestDataByDeviceId")
	public NVCustomerCareDataWrapper getLatestSpeedTestDataByDeviceId(@QueryParam("deviceId") String deviceId, @QueryParam("latitude") Double latitude, @QueryParam("longitude") Double longitude) {
		return invCustomerCareService.getLatestSpeedTestDataByDeviceId(deviceId, latitude, longitude);
	}

}

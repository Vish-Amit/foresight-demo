package com.inn.foresight.module.nv.workorder.stealth.kpi.rest.impl;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.inn.foresight.module.nv.workorder.stealth.constants.StealthConstants;
import com.inn.foresight.module.nv.workorder.stealth.kpi.rest.IDriveTestKPIRest;
import com.inn.foresight.module.nv.workorder.stealth.kpi.service.IDriveTestKPIService;

/**
 * The Class DriveTestKPIRestImpl.
 * 
 * @author ist
 */
@Path("/DriveTestKPI")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)

public class DriveTestKPIRestImpl extends StealthConstants implements IDriveTestKPIRest {

	/** The logger. */
	private Logger logger = LogManager.getLogger(DriveTestKPIRestImpl.class);

	/** The i drive test KPI service. */
	@Autowired
	IDriveTestKPIService iDriveTestKPIService;

	/**
	 * Gets the KPI data of devices, performed web performance test.
	 *
	 * @param startDate
	 *            the start date
	 * @param endDate
	 *            the end date
	 * @param dataType
	 *            the data type
	 * @param kpi
	 *            the kpi
	 * @param threshold
	 *            the threshold
	 * @param uLimit
	 *            the u limit
	 * @param lLimit
	 *            the l limit
	 * @param resultType
	 *            the result type
	 * @return the device data
	 */
	@GET
	@Path("getKPIData")
	@Override
	public Response getKPIData(@QueryParam(START_DATE) Long startDate, @QueryParam(END_DATE) Long endDate,
			@QueryParam(DATATYPE) String dataType, @QueryParam(KPI) String kpi, @QueryParam(THRESHOLD) Double threshold,
			@QueryParam(ULIMIT) Integer uLimit, @QueryParam(LLIMIT) Integer lLimit,
			@QueryParam(RESULT_TYPE) String resultType) {
		try {
			logger.info(
					"Going to getDeviceData for startDate {},endDate {},dataType {},kpi {}, threshold {},uLimit {},lLimit {} and resultType {}",
					startDate, endDate, kpi, dataType, threshold, uLimit, lLimit, resultType);
			if (startDate == null || endDate == null || kpi == null || threshold == null || dataType == null
					|| uLimit == null || lLimit == null) {
				logger.info(
						"Parameters are null, startDate {},endDate {},dataType {},kpi {}, threshold {},uLimit {},lLimit {} and resultType {}",
						startDate, endDate, kpi, dataType, threshold, uLimit, lLimit, resultType);
				return Response.ok(INVALID_PARAMETER_JSON).build();
			} else
				return Response.ok(iDriveTestKPIService.getKPIData(startDate, endDate, dataType, kpi, threshold, uLimit,
						lLimit, resultType)).build();
		} catch (Exception e) {
			logger.error("Exception in getDeviceData : {} ", ExceptionUtils.getStackTrace(e));
			return Response.ok(FAILURE_JSON).build();
		}
	}

	/**
	 * Gets the KPI data for day wise.
	 *
	 * @param startDate
	 *            the start date
	 * @param endDate
	 *            the end date
	 * @param dataType
	 *            the data type
	 * @param deviceid
	 *            the deviceid
	 * @param kpi
	 *            the kpi
	 * @param threshold
	 *            the threshold
	 * @param uLimit
	 *            the u limit
	 * @param lLimit
	 *            the l limit
	 * @param resultType
	 *            the result type
	 * @return the KPI data for day wise
	 */
	@GET
	@Path("getKPIDataForDayWise")
	@Override
	public Response getKPIDataForDayWise(@QueryParam(START_DATE) Long startDate, @QueryParam(END_DATE) Long endDate,
			@QueryParam(DATATYPE) String dataType, @QueryParam(DEVICE_ID) String deviceid, @QueryParam(KPI) String kpi,
			@QueryParam(THRESHOLD) Double threshold, @QueryParam(ULIMIT) Integer uLimit,
			@QueryParam(LLIMIT) Integer lLimit, @QueryParam(RESULT_TYPE) String resultType) {
		try {
			logger.info(
					"Going to searchProbes for startDate {},endDate {},dataType {},deviceid {},kpi {}, threshold {}, uLimit {} ,uLimit {}, resultType {}  ",
					startDate, endDate, dataType, deviceid, kpi, threshold, uLimit, lLimit, resultType);
			if (startDate == null || endDate == null || dataType == null || deviceid == null) {
				logger.info(
						"Parameters are null, startDate {},endDate {},dataType {},deviceid {} ,kpi {}, threshold {}, uLimit {} , uLimit {},resultType {} ",
						startDate, endDate, dataType, deviceid, kpi, threshold, uLimit, lLimit, resultType);
				return Response.ok(INVALID_PARAMETER_JSON).build();
			} else
				return Response.ok(iDriveTestKPIService.getKPIDataForDayWise(startDate, endDate, dataType, deviceid,
						kpi, threshold, uLimit, lLimit, resultType)).build();
		} catch (Exception e) {
			logger.error("Exception in getDeviceData : {} ", ExceptionUtils.getStackTrace(e));
			return Response.ok(FAILURE_JSON).build();
		}
	}

	/**
	 * Gets the KPI data count.
	 *
	 * @param startDate
	 *            the start date
	 * @param endDate
	 *            the end date
	 * @param dataType
	 *            the data type
	 * @param kpi
	 *            the kpi
	 * @param threshold
	 *            the threshold
	 * @param resultType
	 *            the result type
	 * @return the device data count
	 */
	@GET
	@Path("getKPIDataCount")
	@Override
	public Response getKPIDataCount(@QueryParam(START_DATE) Long startDate, @QueryParam(END_DATE) Long endDate,
			@QueryParam(DATATYPE) String dataType, @QueryParam(KPI) String kpi, @QueryParam(THRESHOLD) Double threshold,
			@QueryParam(RESULT_TYPE) String resultType) {
		try {
			logger.info(
					"Going to getDeviceDataCount for startDate {},endDate {},dataType {}, KPI {}, Threshold {}, resultType {}",
					startDate, endDate, dataType, kpi, threshold, resultType);
			if (startDate == null || endDate == null || dataType == null) {
				logger.info("Parameters are null, startDate {},endDate {},dataType {}", startDate, endDate, dataType);
				return Response.ok(INVALID_PARAMETER_JSON).build();
			} else
				return Response.ok(
						iDriveTestKPIService.getKPIDataCount(startDate, endDate, dataType, kpi, threshold, resultType))
						.build();
		} catch (Exception e) {
			logger.error("Exception in getDeviceDataCount : {} ", ExceptionUtils.getStackTrace(e));
			return Response.ok(FAILURE_JSON).build();
		}
	}

	/**
	 * Gets the KPI data count for day wise.
	 *
	 * @param startDate
	 *            the start date
	 * @param endDate
	 *            the end date
	 * @param dataType
	 *            the data type
	 * @param deviceId
	 *            the device id
	 * @param kpi
	 *            the kpi
	 * @param threshold
	 *            the threshold
	 * @param resultType
	 *            the result type
	 * @return the device data count for day wise
	 */
	@GET
	@Path("getKPIDataCountForDayWise")
	@Override
	public Response getKPIDataCountForDayWise(@QueryParam(START_DATE) Long startDate,
			@QueryParam(END_DATE) Long endDate, @QueryParam(DATATYPE) String dataType,
			@QueryParam(DEVICE_ID) String deviceId, @QueryParam(KPI) String kpi,
			@QueryParam(THRESHOLD) Double threshold, @QueryParam(RESULT_TYPE) String resultType) {
		try {
			logger.info("Going to getDeviceDataCountForDayWise for startDate {},endDate {},dataType {}, deviceId {}",
					startDate, endDate, dataType, deviceId);
			if (startDate == null || endDate == null || dataType == null) {
				logger.info("Parameters are null, startDate {},endDate {},dataType {}, deviceId {}", startDate, endDate,
						dataType, deviceId);
				return Response.ok(INVALID_PARAMETER_JSON).build();
			} else
				return Response.ok(iDriveTestKPIService.getKPIDataCountForDayWise(startDate, endDate, dataType,
						deviceId, kpi, threshold, resultType)).build();
		} catch (Exception e) {
			logger.error("Exception in getDeviceDataCountForDayWise : {} ", ExceptionUtils.getStackTrace(e));
			return Response.ok(FAILURE_JSON).build();
		}
	}

}
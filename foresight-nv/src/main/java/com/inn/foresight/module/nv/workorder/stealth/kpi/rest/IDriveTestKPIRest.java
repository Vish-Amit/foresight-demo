package com.inn.foresight.module.nv.workorder.stealth.kpi.rest;

import javax.ws.rs.core.Response;

/**
 * The Interface IDriveTestKPIRest.
 *
 * @author ist
 */
public interface IDriveTestKPIRest {

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
	Response getKPIData(Long startDate, Long endDate, String dataType, String kpi, Double threshold,
			Integer uLimit, Integer lLimit, String resultType);

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
	Response getKPIDataForDayWise(Long startDate, Long endDate, String dataType, String deviceid, String kpi,
			Double threshold, Integer uLimit, Integer lLimit, String resultType);

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
	Response getKPIDataCount(Long startDate, Long endDate, String dataType, String kpi, Double threshold,
			String resultType);

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
	Response getKPIDataCountForDayWise(Long startDate, Long endDate, String dataType, String deviceId,
			String kpi, Double threshold, String resultType);

}
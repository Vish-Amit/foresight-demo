package com.inn.foresight.module.nv.workorder.stealth.kpi.service;

import java.util.List;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.module.nv.device.wrapper.NVDeviceDataWrapper;


/**
 * The Interface IDriveTestKPIService.
 *
 * @author ist
 */
public interface IDriveTestKPIService {

	/**
	 * Gets the KPI data.
	 *
	 * @param startDate the start date
	 * @param endDate the end date
	 * @param dataType the data type
	 * @param kpi the kpi
	 * @param threshold the threshold
	 * @param uLimit the u limit
	 * @param lLimit the l limit
	 * @param resultType the result type
	 * @return the KPI data
	 * @throws RestException the rest exception
	 */
	List<NVDeviceDataWrapper> getKPIData(Long startDate, Long endDate, String dataType, String kpi, Double threshold, Integer uLimit, Integer lLimit, String resultType);


	/**
	 * Gets the KPI data count.
	 *
	 * @param startDate the start date
	 * @param endDate the end date
	 * @param dataType the data type
	 * @param kpi the kpi
	 * @param threshold the threshold
	 * @param resultType the result type
	 * @return the KPI data count
	 * @throws RestException the rest exception
	 */
	Long getKPIDataCount(Long startDate, Long endDate, String dataType, String kpi, Double threshold, String resultType);

	/**
	 * Gets the KPI data count for day wise.
	 *
	 * @param startDate the start date
	 * @param endDate the end date
	 * @param dataType the data type
	 * @param deviceId the device id
	 * @param kpi the kpi
	 * @param threshold the threshold
	 * @param resultType the result type
	 * @return the KPI data count for day wise
	 * @throws RestException the rest exception
	 */
	Long getKPIDataCountForDayWise(Long startDate, Long endDate, String dataType, String deviceId, String kpi,
			Double threshold, String resultType);

	/**
	 * Gets the KPI data for day wise.
	 *
	 * @param startDate the start date
	 * @param endDate the end date
	 * @param dataType the data type
	 * @param deviceid the deviceid
	 * @param kpi the kpi
	 * @param threshold the threshold
	 * @param uLimit the u limit
	 * @param lLimit the l limit
	 * @param resultType the result type
	 * @return the KPI data for day wise
	 * @throws RestException 
	 */
	List<NVDeviceDataWrapper> getKPIDataForDayWise(Long startDate, Long endDate, String dataType, String deviceid, String kpi,
			Double threshold, Integer uLimit, Integer lLimit, String resultType);

	
}
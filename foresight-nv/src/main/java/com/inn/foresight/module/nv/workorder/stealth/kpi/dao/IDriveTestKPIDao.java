package com.inn.foresight.module.nv.workorder.stealth.kpi.dao;

import java.util.List;
import java.util.Map;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.module.nv.device.model.NVDeviceData;
import com.inn.foresight.module.nv.device.wrapper.NVDeviceDataWrapper;
import com.inn.foresight.module.nv.device.wrapper.NVDeviceGroupWrapper;
import com.inn.foresight.module.nv.workorder.stealth.kpi.model.DriveTestKPI;

/** The Interface IDriveTestKPIDao. */
public interface IDriveTestKPIDao extends IGenericDao<Integer, DriveTestKPI> {

	/**
	 * Gets the KPI data.
	 *
	 * @param startDate the start date
	 * @param endDate the end date
	 * @param kpi the kpi
	 * @param threshold the threshold
	 * @param uLimit the u limit
	 * @param lLimit the l limit
	 * @param filterAndValueMap the filter and value map
	 * @return the KPI data
	 * @throws DaoException the dao exception
	 */
	List<NVDeviceDataWrapper> getKPIData(Long startDate, Long endDate, String kpi, Double threshold, Integer uLimit,
			Integer lLimit, Map<String, List> filterAndValueMap);

	/**
	 * Gets the failure data.
	 *
	 * @param startDate the start date
	 * @param endDate the end date
	 * @param kpi the kpi
	 * @param threshold the threshold
	 * @param uLimit the u limit
	 * @param lLimit the l limit
	 * @return the failure data
	 * @throws DaoException the dao exception
	 */
	List<NVDeviceDataWrapper> getFailureData(Long startDate, Long endDate, String kpi, Double threshold, Integer uLimit, Integer lLimit);


	/**
	 * Gets the failure device remark.
	 *
	 * @param startDate the start date
	 * @param endDate the end date
	 * @param deviceId the device id
	 * @return the failure device remark
	 */
	NVDeviceDataWrapper getFailureDeviceRemark(Long startDate, Long endDate,String deviceId);

	/**
	 * Gets the non respond devices.
	 *
	 * @return the non respond devices
	 */
	List<NVDeviceData> getNonRespondDevices();


	/**
	 * Gets the KPI data count.
	 *
	 * @param startDate the start date
	 * @param endDate the end date
	 * @param map the map
	 * @return the KPI data count
	 */
	Long getKPIDataCount(Long startDate, Long endDate, Map<String, List> map);

	/**
	 * Gets the failure device data count.
	 *
	 * @param startDate the start date
	 * @param endDate the end date
	 * @return the failure device data count
	 */
	Long getFailureDataCount(Long startDate, Long endDate);

	/**
	 * Gets the KPI data count for day wise.
	 *
	 * @param startDate the start date
	 * @param endDate the end date
	 * @param deviceId the device id
	 * @param map the map
	 * @return the KPI data count for day wise
	 */
	Long getKPIDataCountForDayWise(Long startDate, Long endDate, String deviceId, Map<String, List> map);

	/**
	 * Gets the failure device data count for day wise.
	 *
	 * @param startDate the start date
	 * @param endDate the end date
	 * @param deviceId the device id
	 * @return the failure device data count for day wise
	 */
	Long getFailureDataCountForDayWise(Long startDate, Long endDate, String deviceId);

	/**
	 * Gets the KPI data for day wise.
	 *
	 * @param startDate the start date
	 * @param endDate the end date
	 * @param deviceid the deviceid
	 * @param uLimit the u limit
	 * @param lLimit the l limit
	 * @param filterForDayWise the filter for day wise
	 * @return the KPI data for day wise
	 */
	List<NVDeviceDataWrapper> getKPIDataForDayWise(Long startDate, Long endDate, String deviceid, Integer uLimit,
			Integer lLimit, Map<String, List> filterForDayWise);

	/**
	 * Gets the failure data for day wise.
	 *
	 * @param startDate the start date
	 * @param endDate the end date
	 * @param deviceid the deviceid
	 * @param uLimit the u limit
	 * @param lLimit the l limit
	 * @return the failure data for day wise
	 */
	List<NVDeviceDataWrapper> getFailureDataForDayWise(Long startDate, Long endDate, String deviceid,
			Integer uLimit, Integer lLimit);
	

	/**
	 * Search NV device groups.
	 *
	 * @param startDate the start date
	 * @param endDate the end date
	 * @param swLat the sw lat
	 * @param swLng the sw lng
	 * @param neLat the ne lat
	 * @param neLng the ne lng
	 * @param zoomLevel the zoom level
	 * @param filterMap the filter map
	 * @return the list
	 * @throws DaoException the dao exception
	 */
	List<NVDeviceGroupWrapper> searchNVDeviceGroups(Long startDate, Long endDate, Double swLat, Double swLng,
			Double neLat, Double neLng, Integer zoomLevel, Map<String, List> filterMap);

	/**
	 * Gets Cems Data from DriveTestKPI table by Stealth Task Detail id and start time.
	 *
	 * @param startDate
	 *            the start date
	 * @param stealthTaskDetailId
	 *            primary key for Stealth Task Detail table
	 * @return Cems data json from DriveTestKPI table
	 */
	String getCemsDataByStealthTaskIdAndDate(Long startDate, Integer stealthTaskDetailId);
	
}
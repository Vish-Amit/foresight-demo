package com.inn.foresight.module.nv.device.rest;

import java.util.List;

import javax.ws.rs.core.Response;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;

import com.inn.foresight.module.nv.device.model.DeviceDashboard;
import com.inn.foresight.module.nv.device.wrapper.DeviceDashboardWrapper;
import com.inn.foresight.module.nv.device.wrapper.NVDeviceDataWrapper;
import com.inn.foresight.module.nv.device.wrapper.NVDeviceWrapper;

/** The Interface INVDeviceDataRest. */
public interface INVDeviceDataRest {

	/**
	 * Gets the all device list.
	 *
	 * @param lLimit            the l limit
	 * @param uLimit            the u limit
	 * @param operator the operator
	 * @return the all device list
	 */
	Response getAllDeviceList(Integer lLimit, Integer uLimit,String operator);

	/**
	 * Gets the device list count.
	 *
	 * @param operator the operator
	 * @return the device list count
	 */
	Response getDeviceListCount(String operator);

	/**
	 * Gets the device count for view port.
	 *
	 * @param northEastLong the north east long
	 * @param northEastLat the north east lat
	 * @param southWestLong the south west long
	 * @param southWestLat the south west lat
	 * @param zoomLevel the zoom level
	 * @param operator the operator
	 * @return the device count for view port
	 */
	Response getDeviceCountForViewPort( Double northEastLong, Double northEastLat, Double southWestLong,
			Double southWestLat, Integer zoomLevel,String operator,String userType);
	
	/**
	 * Search NV devices.
	 *
	 * @param northEastLong the north east long
	 * @param northEastLat the north east lat
	 * @param southWestLong the south west long
	 * @param southWestLat the south west lat
	 * @param zoomLevel the zoom level
	 * @param operator the operator
	 * @return the response
	 */
	Response searchNVDevices( Double northEastLong, Double northEastLat, Double southWestLong,
			Double southWestLat, Integer zoomLevel,String operator,String userType);
	/**
	 * Gets the device details by device id.
	 *
	 * @param deviceId the device id
	 * @return the device details by device id
	 */
	Response getDeviceDetailsByDeviceId(String deviceId);
	
	/**
	 * Gets the device inside cluster.
	 *
	 * @param northEastLong the north east long
	 * @param northEastLat the north east lat
	 * @param southWestLong the south west long
	 * @param southWestLat the south west lat
	 * @param operator the operator
	 * @param geographyId the geography id
	 * @return the device inside cluster
	 */
	Response getDeviceInsideViewPort( Double northEastLong, Double northEastLat, Double southWestLong,
			Double southWestLat,String operator,String geographyId,String userType);
	
	/**
	 * Gets the device count for table view.
	 *
	 * @param northEastLong the north east long
	 * @param northEastLat the north east lat
	 * @param southWestLong the south west long
	 * @param southWestLat the south west lat
	 * @param operator the operator
	 * @param geography the geography
	 * @param geographyId the geography id
	 * @return the device count for table view
	 */
	Response getDeviceCountForTableView( Double northEastLong, Double northEastLat, Double southWestLong,
			Double southWestLat,String operator,String geography,String geographyId);
	

	/**
	 * Gets the device list inside cluster.
	 *
	 * @param cluster the cluster
	 * @param operator the operator
	 * @return the device list inside cluster
	 */
	Response getDeviceListInsideCluster(String cluster, String operator,String userType);

	/**
	 * Gets the device list for WO.
	 *
	 * @param woStartTime the wo start time
	 * @param geographyLevel the geography level
	 * @param geographyId the geography id
	 * @param lLimit the l limit
	 * @param uLimit the u limit
	 * @return the device list for WO
	 */
	Response getDeviceListForWO(Long woStartTime, String geographyLevel,
			Integer geographyId, Integer lLimit, Integer uLimit);

	/**
	 * Gets the device list count.
	 *
	 * @param woStartTime the wo start time
	 * @param geographyLevel the geography level
	 * @param geographyId the geography id
	 * @return the device list count
	 */
	Response getDeviceListCountForWO(Long woStartTime, String geographyLevel,
			Integer geographyId);

	/**
	 * Refresh device data.
	 *
	 * @param deviceId the device id
	 * @return the response
	 */
	Response refreshDeviceData(String deviceId);

	
	/**
	 * Update recharge status.
	 *
	 * @param deviceDataWrappper the device data wrappper
	 * @return the response
	 */
	Response updateRechargeStatus(NVDeviceDataWrapper deviceDataWrappper);

	/**
	 * Gets the device recharge detail.
	 *
	 * @return the device recharge detail
	 * @throws Exception the exception
	 */
	Response getDeviceRechargeDetail() throws Exception;

	/**
	 * Gets the device recharge detail count.
	 *
	 * @return the device recharge detail count
	 * @throws Exception the exception
	 */
	Response getDeviceRechargeDetailCount() throws Exception;

	/**
	 * Gets the device list for work order.
	 *
	 * @param woStartTime the wo start time
	 * @param woEndTime the wo end time
	 * @param geographyLevel the geography level
	 * @param geographyId the geography id
	 * @param lLimit the l limit
	 * @param uLimit the u limit
	 * @return the device list for work order
	 */
	Response getDeviceListForWorkOrder(Long woStartTime, Long woEndTime, String geographyLevel, Integer geographyId,
			Integer lLimit, Integer uLimit);

	/**
	 * Gets the device list count for work order.
	 *
	 * @param woStartTime the wo start time
	 * @param woEndTime the wo end time
	 * @param geographyLevel the geography level
	 * @param geographyId the geography id
	 * @return the device list count for work order
	 */
	Response getDeviceListCountForWorkOrder(Long woStartTime, Long woEndTime, String geographyLevel,
			Integer geographyId);

	Response getStealthDeviceListForGeography( String geographyLevel, Integer geographyId, Integer lLimit,
			Integer uLimit);

	Response getStealthDeviceListCountForGeography(String geographyLevel, Integer geographyId);

	@PreAuthorize("hasRole('ROLE_NV_DEVICES_MANAGER_view')")
	@Secured("ROLE_NV_DEVICES_MANAGER_view")
	Response getDeviceDataCount(NVDeviceWrapper wrapper);

	@PreAuthorize("hasRole('ROLE_NV_DEVICES_MANAGER_view')")
	@Secured("ROLE_NV_DEVICES_MANAGER_view")
	Response getDeviceData(Integer lLimit, Integer uLimit, NVDeviceWrapper wrapper);

	@PreAuthorize("hasRole('ROLE_NV_DEVICES_MANAGER_view')")
	@Secured("ROLE_NV_DEVICES_MANAGER_view")
	Response getFilters();

	@PreAuthorize("hasRole('ROLE_NV_Devices_FUT_Manager_view')")
	@Secured("ROLE_NV_Devices_FUT_Manager_view")
	List<DeviceDashboard> getDeviceDashboardData(Integer llimit, Integer ulimit, DeviceDashboardWrapper wrapper);

	@PreAuthorize("hasRole('ROLE_NV_Devices_FUT_Manager_view')")
	@Secured("ROLE_NV_Devices_FUT_Manager_view")
	Response getDeviceDashboardCount(DeviceDashboardWrapper wrapper);

}

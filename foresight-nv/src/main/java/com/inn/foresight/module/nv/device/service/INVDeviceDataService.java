package com.inn.foresight.module.nv.device.service;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.module.nv.device.model.NVDeviceData;
import com.inn.foresight.module.nv.device.wrapper.NVDeviceCountWrapper;
import com.inn.foresight.module.nv.device.wrapper.NVDeviceDataWrapper;
import com.inn.foresight.module.nv.device.wrapper.NVDeviceWrapper;
import com.inn.foresight.module.nv.pushnotification.wrapper.PushNotificationWrapper;
import com.inn.foresight.module.nv.reportgeneration.wrapper.NVReportWrapper;

import java.util.List;
import java.util.Map;

/** The Interface INVDeviceDataService. */
public interface INVDeviceDataService {

	/**
	 * Gets the all device list.
	 *
	 * @param lLimit the l limit
	 * @param uLimit the u limit
	 * @param operator the operator
	 * @return the all device list
	 * @throws RestException the rest exception
	 */
	List<NVDeviceDataWrapper> getAllDeviceList(Integer lLimit,Integer uLimit,String operator);
	
	/**
	 * Gets the device count.
	 *
	 * @param operator the operator
	 * @return the device count
	 * @throws RestException the rest exception
	 */
	long getDeviceCount(String operator);
	
	/**
	 * Gets the device count for view port.
	 *
	 * @param northEastLat the north east lat
	 * @param northEastLong the north east long
	 * @param southWestLat the south west lat
	 * @param southWestLong the south west long
	 * @param zoomLevel the zoom level
	 * @param operator the operator
	 * @return the device count for view port
	 * @throws RestException the rest exception
	 */
	List<NVDeviceCountWrapper> getDeviceCountForViewPort( Double northEastLat, Double northEastLong,Double southWestLat,
			Double southWestLong, Integer zoomLevel,String operator,String userType);
	
	/**
	 * Gets the device count for table view.
	 *
	 * @param northEastLat the north east lat
	 * @param northEastLong the north east long
	 * @param southWestLat the south west lat
	 * @param southWestLong the south west long
	 * @param operator the operator
	 * @param geography the geography
	 * @param geographyId the geography id
	 * @return the device count for table view
	 * @throws RestException the rest exception
	 */
	List<NVDeviceCountWrapper> getDeviceCountForTableView( Double northEastLat, Double northEastLong,Double southWestLat,
			Double southWestLong, String operator,String geography, String geographyId);

	/**
	 * Gets the device details by device id.
	 *
	 * @param deviceId the device id
	 * @return the device details by device id
	 * @throws RestException the rest exception
	 */
	NVDeviceDataWrapper getDeviceDetailsByDeviceId(String deviceId);
	
	/**
	 * Gets the device inside cluster.
	 *
	 * @param northEastLat the north east lat
	 * @param northEastLong the north east long
	 * @param southWestLat the south west lat
	 * @param southWestLong the south west long
	 * @param operator the operator
	 * @param geographyId the geography id
	 * @return the device inside cluster
	 * @throws RestException the rest exception
	 */
	List<NVDeviceDataWrapper> getDeviceInsideViewPort( Double northEastLat, Double northEastLong,Double southWestLat,
			Double southWestLong,String operator, String geographyId,String userType);
	
	/**
	 * Creates the NV device data.
	 *
	 * @param nvDeviceData the nv device data
	 * @return the NV device data
	 * @throws RestException the rest exception
	 */
	NVDeviceData createNVDeviceData(NVDeviceData nvDeviceData);
	
	/**
	 * Find NV device data by device id.
	 *
	 * @param deviceId the device id
	 * @return the NV device data
	 * @throws RestException the rest exception
	 */
	NVDeviceData findNVDeviceDataByDeviceId(String deviceId);

	/**
	 * Update NV device data.
	 *
	 * @param nvDeviceData the nv device data
	 * @return the NV device data
	 * @throws RestException the rest exception
	 */
	NVDeviceData updateNVDeviceData(NVDeviceData nvDeviceData);

	/**
	 * Gets the device list inside cluster.
	 *
	 * @param cluster the cluster
	 * @param operator the operator
	 * @return the device list inside cluster
	 * @throws RestException the rest exception
	 */
	List<NVDeviceDataWrapper> getDeviceListInsideCluster(String cluster, String operator,String userType);

	/**
	 * Gets the device list for WO.
	 *
	 * @param woStartTime the wo start time
	 * @param geographyLevel the geography level
	 * @param geographyId the geography id
	 * @param lLimit the l limit
	 * @param uLimit the u limit
	 * @return the device list for WO
	 * @throws RestException the rest exception
	 */
	List<NVDeviceDataWrapper> getDeviceListForWO(Long woStartTime,
			String geographyLevel, Integer geographyId, Integer lLimit, Integer uLimit);

	/**
	 * Gets the NV device data.
	 *
	 * @param nvDeviceDataIds the nv device data ids
	 * @return the NV device data
	 * @throws RestException the rest exception
	 */
	List<NVDeviceData> getNVDeviceData(List<Integer> nvDeviceDataIds);

	/**
	 * Gets the device count list for WO.
	 *
	 * @param woStartTime the wo start time
	 * @param geographyLevel the geography level
	 * @param geographyId the geography id
	 * @return the device count list for WO
	 * @throws RestException the rest exception
	 */
	Long getDeviceCountListForWO(Long woStartTime, String geographyLevel,
			Integer geographyId);

	/**
	 * Search NV devices.
	 *
	 * @param northEastLat the north east lat
	 * @param northEastLong the north east long
	 * @param southWestLat the south west lat
	 * @param southWestLong the south west long
	 * @param operator the operator
	 * @param zoomLevel the zoom level
	 * @return the list
	 * @throws RestException the rest exception
	 */
	List<NVDeviceDataWrapper> searchNVDevices(String operator,Integer zoomLevel,String userType);

	/**
	 * Creates the.
	 *
	 * @param deviceId the device id
	 * @return the push notification
	 * @throws RestException the rest exception
	 */
	PushNotificationWrapper refreshDeviceData(String deviceId);

	/**
	 * Update recharge status.
	 *
	 * @param deviceDataWrappper the device data wrappper
	 * @return the string
	 */
	String updateRechargeStatus(NVDeviceDataWrapper deviceDataWrappper);

	/**
	 * Update device data.
	 *
	 * @param requestWrapper the request wrapper
	 * @return the string
	 */
	String updateDeviceData(String requestWrapper);

	/**
	 * Gets the device recharge detail.
	 *
	 * @return the device recharge detail
	 * @throws RestException the rest exception
	 */
	NVReportWrapper getDeviceRechargeDetail();

	/**
	 * Gets the device recharge detail count.
	 *
	 * @return the device recharge detail count
	 * @throws RestException the rest exception
	 */
	Long getDeviceRechargeDetailCount();

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
	 * @throws RestException the rest exception
	 * @throws Exception 
	 */
	List<NVDeviceDataWrapper> getDeviceListForWorkOrder(Long woStartTime, Long woEndTime, String geographyLevel,
			Integer geographyId, Integer lLimit, Integer uLimit) throws Exception;

	/**
	 * Gets the device list count for work order.
	 *
	 * @param woStartTime the wo start time
	 * @param woEndTime the wo end time
	 * @param geographyLevel the geography level
	 * @param geographyId the geography id
	 * @return the device list count for work order
	 */
	Long getDeviceListCountForWorkOrder(Long woStartTime, Long woEndTime,
			String geographyLevel, Integer geographyId) throws Exception;



	/**
	 * Gets the devices by work order id.
	 *
	 * @param id the id
	 * @return the devices by work order id
	 * @throws RestException 
	 */
	

	List<NVDeviceData> getNVDeviceDataByDeviceInfo(List<Integer> deviceInfoIds);
	
	String updateNVDeviceData(String wrapper);
	
	NVDeviceData getDeviceByDeviceId(String deviceId);

	List<Integer> getDeviceListForUser(Integer userId);

	List<NVDeviceDataWrapper> getStealthDeviceListForGeography( String geographyLevel,
			Integer geographyId, Integer lLimit, Integer uLimit);

	Long getStealthDeviceListCountForGeography(String geographyLevel, Integer geographyId);

	NVDeviceData persistNVDeviceData(NVDeviceDataWrapper wrapper, String[] geographies, String operator);

	String[] getGeographiesForDeviceRequest(NVDeviceDataWrapper wrapper);

	 List<NVDeviceDataWrapper> searchNVDeviceDetail(Double northEastLat, Double northEastLong, Double southWestLat, Double southWestLong,
			String operator, Integer zoomLevel, String userType);
	
    	Map<String, List<String>> getFilters();

	List<NVDeviceDataWrapper> getDeviceData(NVDeviceWrapper wrapper, Integer lLimit, Integer uLimit);

	Long getDeviceDataCount(NVDeviceWrapper wrapper);
}

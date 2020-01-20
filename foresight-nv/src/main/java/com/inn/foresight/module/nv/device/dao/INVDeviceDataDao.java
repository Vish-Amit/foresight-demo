package com.inn.foresight.module.nv.device.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.module.nv.device.model.NVDeviceData;
import com.inn.foresight.module.nv.device.wrapper.NVDeviceCountWrapper;
import com.inn.foresight.module.nv.device.wrapper.NVDeviceDataWrapper;
import com.inn.foresight.module.nv.device.wrapper.NVDeviceWrapper;


/**
 * The Interface INVDeviceDataDao.
 *
 * @author innoeye
 * date - 27-Feb-2018 2:16:01 PM
 */
public interface INVDeviceDataDao extends IGenericDao<Integer,NVDeviceData> {
	
	/**
	 * Gets the device profile by device id.
	 *
	 * @param deviceId the device id
	 * @return the device profile by device id
	 * @throws DaoException the dao exception
	 */
	NVDeviceData getDeviceByDeviceId(String deviceId);
	
	
	/**
	 * Gets the all device list.
	 *
	 * @param lLimit the l limit
	 * @param uLimit the u limit
	 * @param operator the operator
	 * @param filters the filters
	 * @return the all device list
	 * @throws DaoException the dao exception
	 */
	@SuppressWarnings("rawtypes")
	List<NVDeviceDataWrapper> getAllDeviceList(Integer lLimit,Integer uLimit,String operator, Map<String, List> filters);
	
	/**
	 * Gets the device count.
	 *
	 * @param operator the operator
	 * @param filters the filters
	 * @return the device count
	 * @throws DaoException the dao exception
	 */
	@SuppressWarnings("rawtypes")
	long getDeviceCount(String operator,Map<String, List> filters);
	
	
	/**
	 * Gets the device count wrapper for zoom level.
	 *
	 * @param zoomLevel the zoom level
	 * @param operator the operator
	 * @param northEastLat the north east lat
	 * @param northEastLong the north east long
	 * @param southWestLat the south west lat
	 * @param southWestLong the south west long
	 * @param filters the filters
	 * @return the device count wrapper for zoom level
	 * @throws DaoException the dao exception
	 */
	@SuppressWarnings("rawtypes") List<NVDeviceCountWrapper> getDeviceCountWrapperForZoomLevel(Integer zoomLevel, String operator, Double northEastLat,
			Double northEastLong, Double southWestLat, Double southWestLong, Map<String, List> filters);
	
	/**
	 * Gets the device inside cluster.
	 *
	 * @param operator the operator
	 * @param northEastLat the north east lat
	 * @param northEastLong the north east long
	 * @param southWestLat the south west lat
	 * @param southWestLong the south west long
	 * @param geographyId the geography id
	 * @param filters the filters
	 * @return the device inside cluster
	 * @throws DaoException the dao exception
	 */
	@SuppressWarnings("rawtypes") List<NVDeviceDataWrapper> getDeviceInsideViewPort( String operator, Double northEastLat,
			Double northEastLong, Double southWestLat, Double southWestLong, String geographyId,Map<String, List> filters);
	
	/**
	 * Gets the NV device data wrapper by device id.
	 *
	 * @param deviceId the device id
	 * @return the NV device data wrapper by device id
	 * @throws DaoException the dao exception
	 */
	NVDeviceDataWrapper getNVDeviceDataWrapperByDeviceId(String deviceId);


	/**
	 * Gets the device list inside cluster.
	 *
	 * @param cluster the cluster
	 * @param operator the operator
	 * @param filterAndValueMap the filter and value map
	 * @return the device list inside cluster
	 * @throws DaoException the dao exception
	 */
	@SuppressWarnings("rawtypes") List<NVDeviceDataWrapper> getDeviceListInsideCluster(String cluster, String operator,
			Map<String, List> filterAndValueMap);


	/**
	 * Gets the device list for WO.
	 *
	 * @param woStartTime the wo start time
	 * @param filters the filters
	 * @param lLimit the l limit
	 * @param uLimit the u limit
	 * @return the device list for WO
	 * @throws DaoException the dao exception
	 */
	@SuppressWarnings("rawtypes") List<NVDeviceDataWrapper> getDeviceListForWO(Date woStartTime,
			Map<String, List> filters,Integer lLimit,Integer uLimit);


	/**
	 * Gets the NV device data.
	 *
	 * @param nvDeviceDataIds the nv device data ids
	 * @return the NV device data
	 * @throws DaoException the dao exception
	 */
	List<NVDeviceData> getNVDeviceData(List<Integer> nvDeviceDataIds);
	
	/**
	 * Gets the device count list for WO.
	 *
	 * @param woStartTime the wo start time
	 * @param filters the filters
	 * @return the device count list for WO
	 * @throws DaoException the dao exception
	 */
	@SuppressWarnings("rawtypes") Long getDeviceCountListForWO(Date woStartTime,
			Map<String, List> filters);


	/**
	 * Gets the device count for table view.
	 *
	 * @param northEastLat the north east lat
	 * @param northEastLong the north east long
	 * @param southWestLat the south west lat
	 * @param southWestLong the south west long
	 * @param filterAndValueMap the filter and value map
	 * @param geography the geography
	 * @return the device count for table view
	 * @throws DaoException the dao exception
	 */
	@SuppressWarnings("rawtypes")
	List<NVDeviceCountWrapper> getDeviceCountForTableView(Double northEastLat, Double northEastLong,
			Double southWestLat, Double southWestLong, Map<String, List> filterAndValueMap, String geography);



	/**
	 * Search NV devices.
	 *
	 * @param operator the operator
	 * @param northEastLat the north east lat
	 * @param northEastLong the north east long
	 * @param southWestLat the south west lat
	 * @param southWestLong the south west long
	 * @param filterValueMap the filter value map
	 * @param zoomLevel the zoom level
	 * @return the list
	 * @throws DaoException the dao exception
	 */
	@SuppressWarnings("rawtypes")
	List<NVDeviceDataWrapper> searchNVDevices(String operator, Double northEastLat, Double northEastLong,
			Double southWestLat, Double southWestLong, Map<String, List> filterValueMap,Integer zoomLevel);

	/**
	 * Gets the devices by device ids.
	 *
	 * @param deviceId the device id
	 * @return the devices by device ids
	 * @throws DaoException the dao exception
	 */
	List<NVDeviceData> getDevicesByDeviceIds(List<String> deviceId);
	
	/**
	 * Gets the devices by device ids and group id.
	 *
	 * @param groupId the group id
	 * @param deviceId the device id
	 * @return the devices by device ids and group id
	 * @throws DaoException the dao exception
	 */
	List<NVDeviceData> getDevicesByDeviceIdsAndGroupId(Integer groupId,
			List<String> deviceId);


	/**
	 * Gets the devices by group id.
	 *
	 * @param groupId the group id
	 * @return the devices by group id
	 * @throws DaoException the dao exception
	 */
	List<NVDeviceDataWrapper> getDevicesByGroupId(Integer groupId);


	/**
	 * Gets the device count by group id.
	 *
	 * @param groupId the group id
	 * @return the device count by group id
	 * @throws DaoException the dao exception
	 */
	Long getDeviceCountByGroupId(Integer groupId);


	/**
	 * Gets the device data by group id.
	 *
	 * @param groupId the group id
	 * @param filters the filters
	 * @return the device data by group id
	 * @throws DaoException the dao exception
	 */
	@SuppressWarnings("rawtypes")
	List<NVDeviceData> getDeviceDataByGroupId(Integer groupId , Map<String, List> filters );

	/**
	 * Gets the device recharge detail.
	 *
	 * @param daysOfDifference the days of difference
	 * @return the device recharge detail
	 * @throws DaoException the dao exception
	 */
	List<NVDeviceDataWrapper> getDeviceRechargeDetail(Integer daysOfDifference);


	/**
	 * Gets the device recharge detail count.
	 *
	 * @param i the i
	 * @return the device recharge detail count
	 * @throws DaoException the dao exception
	 */
	Long getDeviceRechargeDetailCount(int daysOfDifference);


	List<NVDeviceData> getNVDeviceDataByDeviceInfo(List<Integer> deviceInfoIDs);


	List<Integer> getDeviceListForUser(Integer userId);


	String getNvInstallationDetail(String deviceId);

	

    Map<String, List<String>> getFilters();

    List<NVDeviceDataWrapper> getDeviceData(NVDeviceWrapper wrapper, Integer llimit, Integer ulimit);

    Long getDeviceDataCount(NVDeviceWrapper wrapper);
}

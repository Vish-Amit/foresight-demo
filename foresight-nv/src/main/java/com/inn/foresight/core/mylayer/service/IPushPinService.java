package com.inn.foresight.core.mylayer.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.core.mylayer.model.PushPin;
import com.inn.foresight.core.mylayer.wrapper.DataWrapper;
import com.inn.product.um.user.model.User;


/**
 * The Interface IPushPinService.
 */
public interface IPushPinService extends IGenericService<Integer, PushPin>
{

	boolean deleteGroupName(Integer userid, String groupName);

	boolean deletePinFromGroup(Integer userid, Integer id);

	void sharedPushPinGroup(User user, String groupName, String email);

	boolean deletePinsByPinStatus(Integer userid, String groupName, String pinStatus);

	String getDataInExcel(List<DataWrapper> wrappers);

	List<DataWrapper> getPinGroupDetails(Integer userid, Integer lowerLimit, Integer upperLimit);

	Long getPinGroupListCountBySearchterm(Integer userid, String searchTerm);

	Long getPinGroupListCount(Integer userid);

	List<DataWrapper> getPinGroupData(Integer userid, String groupName);

	List<String> getPinGroupNameList(Integer userid);

	List<String> getPinNamesForGroupNames(List<String> groupNames, Integer userid);

	byte[] getFileByPath(String fileName);

	Map<String, String> insertPinsData(User user, List<DataWrapper> pins);

	Map uploadFile(InputStream inputStream, String fileName, User user);

	List<String> getPinNameByGroupName(Integer userid, List<String> groupName);

	List<DataWrapper> searchPinDetails(Integer userid, List<String> groupName, String pinName);

	byte[] exportPinsInKML(Integer userId, String groupName);

	Long getCountsOfPinsByGroupName(Integer userid, String groupName, String searchTerm);

	Long getCountsOfPinsByGroupName(Integer userid, String groupName);

	List<DataWrapper> getPinGroupData(Integer userid, String groupName, Integer lowerLimit, Integer upperLimit);

	List<DataWrapper> getPinsDetailsBySearchTerm(Integer userid,Integer lowerLimit, Integer upperLimit, String pinName,String groupName);

	List<DataWrapper> getPinGroupDetailsBySearchTerm(Integer userid, Integer lowerLimit, Integer upperLimit,
			String groupName);

	PushPin getPinDetailsForGroupName(Integer userid, String groupName, Integer id);

	String getPushPinFileName(String groupName);

	boolean isGroupExistForUser(String groupName, Integer userid);

	List<String> getPinStatusList(Integer userid);

	List<DataWrapper> getPinStatusByGroupName(Integer userid, String groupName);

	boolean updateGroupName(Integer userid, List<DataWrapper> dataWrapperList);

}

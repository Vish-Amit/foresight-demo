package com.inn.foresight.core.mylayer.dao;

import java.util.List;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.foresight.core.mylayer.model.PushPin;
import com.inn.foresight.core.mylayer.wrapper.DataWrapper;


/**
 * The Interface IPushPinDao.
 */
public interface IPushPinDao extends IGenericDao<Integer, PushPin>
{
	List<String> getGroupNamesByUserId(Integer userid);

	boolean deleteGroupName(Integer userid, String groupName);

	boolean deletePinFromGroup(Integer userid, Integer id);

	List<String> getGroupNames(Integer userid);

	List<String> getPinNamesForGroupNames(List<String> groupNames, Integer userid);

	boolean deletePinsByPinStatus(Integer userid, String groupName, String pinStatus);

	PushPin getPushPinById(Integer id);

	Long getPinGroupListCount(Integer userid, String searchTerm);

	List<String> getPinGroupNameList(Integer userid);

	List<String> getPinNameByGroupName(Integer userid, List<String> groupName);

	List<DataWrapper> searchPinDetails(Integer userid, List<String> groupName, String pinName);

	Long getCountsOfPinsByGroupName(Integer userid, String groupName, String searchTerm);

	List<PushPin> getPinGroupData(Integer userid, String groupName, Integer lowerLimit, Integer upperLimit);

	List<DataWrapper> getListOfPinBySearchTerm(Integer userid, Integer lowerLimit, Integer upperLimit, String pinName,
			String groupName);

	List<DataWrapper> getListOfPinGroupsBySearchTerm(Integer userid, Integer lowerLimit, Integer upperLimit,
			String groupName);

	List<DataWrapper> getPinGroupDetails(String groupName, Integer lowerLimit, Integer upperLimit, Integer userid);

	PushPin getPinDetailsForGroupName(Integer userid, String groupName, Integer id);

	boolean isPinExistForGroupName(Integer userid, String pinName, String groupName, Integer id);

	boolean updateGroupName(Integer userid, String colorCode, DataWrapper dataWrapper);

	boolean isPinExistInGroup(String pinName, String groupName, Integer userid);

	List<String> getAllGroupNames(Integer userid, String groupName);

	boolean isGroupExistForUser(String groupName, Integer userid);

	List<String> getPinStatusList(Integer userid);

	List<DataWrapper> getPinStatusByGroupName(Integer userid, String groupName);

	Long getNumberOfPinsInGroup(String groupName, Integer userid);

	PushPin getPinData(Integer userid, String groupName, String pinName);

	List<DataWrapper> getColorCodeAndImageNameByPinStatus(Integer userid, String groupName);

	Long getNumberOfPinStatusInGroup(Integer userid, String groupName);

	List<String> getPinStatusByGroupNameAndUserid(Integer userid, String groupName);

}

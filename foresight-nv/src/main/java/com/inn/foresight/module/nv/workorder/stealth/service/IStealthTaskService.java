package com.inn.foresight.module.nv.workorder.stealth.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.inn.foresight.module.nv.workorder.stealth.wrapper.StealthWOWrapper;
import com.inn.foresight.module.nv.workorder.wrapper.EnodeBWrapper;

public interface IStealthTaskService {

	List<StealthWOWrapper> getStealthKPISummary(Integer workorderId, Integer zoomLevel, Long startTime, Long endTime);

	Map<String,Object> getKPIDistributionAndCount(String tableName,String type, String name, Long timeStamp,Integer enodeBID, Long endTime);

	List<EnodeBWrapper> getCellWiseData(String tableName, String type, String name, Integer enodeBId, Long timeStamp, Long endTime);

	Map<String, Object> getTopEnodeBDetails(String tableName, String type, String name,Long timeStamp);

	Map<Long, List<String>> getDashboardDataFromHbase(List<String> columnList, String tableName, String startDate, String rowkeyPrefix, String endDate) throws IOException;

	Map<String, Object> getTopEnodeBDetailsByDateRang(String tableName, String type, String name, Long startTime,
			Long endTime);

}

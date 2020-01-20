package com.inn.foresight.module.nv.workorder.stealth.service;

import java.util.List;

import com.inn.commons.http.HttpException;
import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.module.nv.workorder.stealth.model.StealthTaskDetail;

public interface IStealthTaskDetailService extends IGenericService<Integer, StealthTaskDetail> {

	String updateStealthTaskDetails(String encryptedWOTestJson);

	String fetchStealthWOTaskId(String encryptedDeviceId, String encryptedWoRecipeId);

	String updateAcknowledgement(String encryptedTaskJson);

	String checkWOStatus(String encryptedTaskJson);

	String completeStealthWorkorder(Integer workorderId);

	String getStealthKPISummary(Integer workorderId, Long startTime,Long endTime, Integer zoomLevel);
	
	String fetchStealthTaskIdForDeviceReport(String deviceId, String workorderId);

	String updateStealthTaskDetailList(String encryptedTaskJsonList);

	/*String getKPIDistributionAndCount(String tableName, String type, String name, Long timeStamp,
			Integer enodeBId) ;

String getCellWiseData(String tableName, String type, String name, Long timeStamp, Integer enodeBId);

	String getTopEnodeBDetails(String tableName, String type, String name, Long timeStamp);
*/
	String getDashboardDataFromHbase(List<String> hbaseColumns, String tableName, String rowkeyPrefix, String startDate,String endDate) throws HttpException;


	String getKPIDistributionAndCount(String tableName, String type, String name, Long timeStamp, Integer enodeBId,
			Long endTime);

	String getCellWiseData(String tableName, String type, String name, Long timeStamp, Integer enodeBId, Long endTime);

	String getTopEnodeBDetails(String tableName, String type, String name, Long timeStamp, Long endTime);

}

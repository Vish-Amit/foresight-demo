package com.inn.foresight.module.nv.workorder.stealth.dao;

import java.util.Date;
import java.util.List;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.foresight.module.nv.device.wrapper.NVDeviceDataWrapper;
import com.inn.foresight.module.nv.workorder.stealth.model.StealthTaskResult;
import com.inn.foresight.module.nv.workorder.stealth.wrapper.StealthWOWrapper;

public interface IStealthTaskResultDao extends IGenericDao<Integer, StealthTaskResult>{

	List<NVDeviceDataWrapper> getStealthDeviceWrapperListByWOId(Integer id);

	List<StealthWOWrapper> getStealthWoSummary(Integer workorderId);

	StealthTaskResult getStealthTaskResultByWOAndStealthTask(Integer woId, Integer stealthTaskId, Date date);

	StealthTaskResult getStealthTaskResultByStealthTaskDetailId(Integer stealthTaskDetailId, Date startTime,Integer hour);

	List<StealthWOWrapper> getStatusSummary(Integer woId,String type);

	List<StealthWOWrapper> getStatusSummaryForReport(Integer woId, List<Integer> taskIdList);

	StealthTaskResult getStealthTaskResultForHourlyWO(Integer woId, Integer stealthTaskId, Date date, Integer hour);

	List<StealthTaskResult> getStealthResultListByTaskId(Integer taskId);

	StealthWOWrapper getDeviceCount(Integer workorderId);

}

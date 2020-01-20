package com.inn.foresight.module.nv.workorder.stealth.service;

import java.util.List;
import java.util.Map;

import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.module.nv.device.wrapper.NVDeviceDataWrapper;
import com.inn.foresight.module.nv.workorder.stealth.model.StealthTaskResult;

public interface IStealthTaskResultService extends IGenericService<Integer, StealthTaskResult>{
	List<NVDeviceDataWrapper>getStealthDeviceListByWOId(Integer workorderId);
	List<Map<String, Long>> getStealthWoSummary(Integer workorderId);
	StealthTaskResult getStealthTaskResultByStealthTaskDetailId(Integer stealthTaskDetailId, Long startTime);

}

package com.inn.foresight.module.nv.dashboard.passive.dao;

import java.util.List;
import java.util.Map;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.foresight.module.nv.dashboard.passive.model.NVPassiveDevice;
import com.inn.foresight.module.nv.dashboard.passive.wrapper.NVPassiveDeviceWrapper;

public interface INVPassiveDeviceDao extends IGenericDao<Integer, NVPassiveDevice> {

	@SuppressWarnings("rawtypes")
	List<NVPassiveDeviceWrapper> getMakeWisePassiveDeviceCounts(Map<String, List> filterMetaData,
			List<String> top5Make);

	@SuppressWarnings("rawtypes")
	List<String> getTop5Make(Map<String, List> filterMetaData);

	@SuppressWarnings("rawtypes")
	List<NVPassiveDeviceWrapper> getMakeModelWisePassiveDeviceCounts(Map<String, List> filterMetaData,
			List<String> top5Make);

	@SuppressWarnings("rawtypes")
	List<NVPassiveDeviceWrapper> getMakeModelOsWisePassiveDeviceCounts(Map<String, List> filterMetaData,
			List<String> top5Make);

}

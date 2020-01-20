package com.inn.foresight.module.nv.device.dao;

import java.util.List;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.foresight.module.nv.device.model.DeviceDashboard;
import com.inn.foresight.module.nv.device.wrapper.DeviceDashboardWrapper;

public interface DeviceDashboardDao extends IGenericDao<Integer, DeviceDashboard>{
	
	DeviceDashboard persistDashboardData(DeviceDashboard dashboard);

	DeviceDashboard findDeviceDataByDeviceId(String deviceId);

	List<DeviceDashboard> findDeviceList(DeviceDashboardWrapper wrapper, Integer llimit, Integer ulimit);

	Long findDevicesCount(DeviceDashboardWrapper wrapper);


	List<DeviceDashboardWrapper> getTop10DeviceWithRank(String deviceId);
}

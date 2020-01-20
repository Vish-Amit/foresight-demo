package com.inn.foresight.module.nv.device.service;

import java.util.List;

import com.inn.foresight.module.nv.device.model.DeviceDashboard;
import com.inn.foresight.module.nv.device.wrapper.DeviceDashboardWrapper;

public interface DeviceDashboardService {



	String persistDeviceDashboardData(String encryptedWrapper);

	List<DeviceDashboard> getDeviceDashbordData(Integer llimit, Integer ulimit, DeviceDashboardWrapper wrapper);

	Long getDeviceDashbordDataCount(DeviceDashboardWrapper wrapper);

	List<DeviceDashboardWrapper> getTop10DeviceWithRank(String deviceId);
}

package com.inn.foresight.module.nv.dashboard.wifi.service;

import java.util.List;
import java.util.Map;

import com.inn.foresight.module.nv.dashboard.wifi.wrapper.APDetailWrapper;
import com.inn.foresight.module.nv.dashboard.wifi.wrapper.WIFIAPDashboardWrapper;

public interface IWIFIAPDetailService {
	public List<APDetailWrapper> getFloorDataForBuilding(Integer buildingId);

	public List<WIFIAPDashboardWrapper> getFloorWiseAPDataFromHbase(Integer floorId, String date,
			Map<String,String> macAddressMap);

	public Object getFloorStats(Integer floorId, String startDate);

	public WIFIAPDashboardWrapper getFloorPerformanceDataFromHbase(Integer floorId, String startDate);

	public String getFloorPerformance(Integer floorId, String startDate);

	public String getAPPerformance(Integer floorId, String macAddress, String startDate, String hour);

	public WIFIAPDashboardWrapper getAPPerformanceFromHbase(Integer floorId, String macAddress, String startDate,
			String hour);

	public APDetailWrapper getAPDetail(String macAddress);

}

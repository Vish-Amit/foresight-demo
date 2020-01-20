package com.inn.foresight.module.nv.dashboard.wifi.dao;

import com.inn.foresight.module.nv.dashboard.wifi.wrapper.WIFIAPDashboardWrapper;

public interface IWIFIAPDetailHbaseDao {

	public WIFIAPDashboardWrapper getPerformanceDataFromHbase(Integer floorId, String macAddress, String startDate,
			String hour, Boolean isFloorWise);

}

package com.inn.foresight.module.nv.dashboard.passive.rest;

import javax.ws.rs.core.Response;

public interface INVPassiveDashboardRest {
	
	Response getPassiveDashboardData(String date, String level,Integer geographyId,String duplexType,String tagTye ,String appName);

	Response getDeviceDistribution(String date, String level, Integer geographyId,String duplexType,String tagTye, String appName);

}

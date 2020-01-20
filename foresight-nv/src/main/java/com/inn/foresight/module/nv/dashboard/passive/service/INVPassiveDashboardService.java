package com.inn.foresight.module.nv.dashboard.passive.service;

import java.util.List;
import java.util.Map;

import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.module.nv.dashboard.passive.model.NVPassiveDashboard;

public interface INVPassiveDashboardService extends IGenericService<Integer, NVPassiveDashboard>{

	Map<String, Map<String, ?>> getPassiveDashboardData(String date, String level, Integer geographyId,String duplexType,String tagTye ,String appName);
	
	String getDeviceDistributionData(String date, String level, Integer geographyId,String duplexType,String tagTye ,String appName);

	String getTop5MakeAndModel(Map<String, Map<String, Long>> deviceDistribution);

    	Map<String, Map<String,?>> computeCombineResultData(List<NVPassiveDashboard> passiveRecords, List<NVPassiveDashboard> lastSevenDaysRecords);
}

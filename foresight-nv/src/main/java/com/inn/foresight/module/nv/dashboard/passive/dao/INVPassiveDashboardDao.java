package com.inn.foresight.module.nv.dashboard.passive.dao;

import java.util.List;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.foresight.module.nv.dashboard.passive.model.NVPassiveDashboard;

public interface INVPassiveDashboardDao extends IGenericDao<Integer, NVPassiveDashboard> {

	List<NVPassiveDashboard> getCombinePassiveRecords(String date, String level, Integer geographyId,String duplexType,String tagType,String appName);

	List<NVPassiveDashboard> getLastSevenDaysData(String date, String level, Integer geographyId,String duplexType,String tagType,String appName);
}

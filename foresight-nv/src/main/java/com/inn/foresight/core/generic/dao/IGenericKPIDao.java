package com.inn.foresight.core.generic.dao;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.inn.foresight.core.generic.wrapper.GenericCvFtWrapper;


public interface IGenericKPIDao {

	public List<GenericCvFtWrapper> getDataForClusterviewAndFloatingTable(String band, String environment, String datasource, List<String> daterange, String module, boolean isLatest, String kpiName,
			String geographyType, Set<String> geographyList,String technology, String operatorName);

  List<Object[]> getKPIWeeklyData(String geographyLevel, Integer geographyId, Date latestMondayDate,
      Date startDate, String band, String kpi, String module, String competitor);
}

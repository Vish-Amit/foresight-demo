package com.inn.foresight.core.generic.service;

import java.util.List;

import com.inn.foresight.core.generic.wrapper.GenericCvFtWrapper;

public interface IGenericKPIService {

	public List<GenericCvFtWrapper> getDataForClusterviewAndFloatingtable(Double southWestLat, Double southWestLong, Double northEastLat,
			Double northEastLong, String geographyType, String kpi, String band, String datasource,String mindate, String maxdate,
			String recordtype, String module, boolean isLatest,String technology,String operatorName);	
}

package com.inn.foresight.core.infra.dao;

public interface BuildingHBaseDao {

	byte[] getFloorPlanByRowkey(String rowkey, String kpi);

	String getFloorLegendByKpi(String rowkey, String kpi);

	String getPredictionKpiByRowkey(String rowkey);

	String getBoundsByRowkey(String rowkey);

}
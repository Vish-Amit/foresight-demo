package com.inn.foresight.module.nv.report.wrapper.kpicomparison;

import java.util.List;

public class KPIComparisonInfoWrapper {

	private String kpiName;

	List<KPIComparisonDataWrapper> kpiDataWrapperList;

	public String getKpiName() {
		return kpiName;
	}

	public void setKpiName(String kpiName) {
		this.kpiName = kpiName;
	}

	public List<KPIComparisonDataWrapper> getKpiDataWrapperList() {
		return kpiDataWrapperList;
	}

	public void setKpiDataWrapperList(List<KPIComparisonDataWrapper> kpiDataWrapperList) {
		this.kpiDataWrapperList = kpiDataWrapperList;
	}

	@Override
	public String toString() {
		return "KPIComparisonInfoWrapper [kpiName=" + kpiName + ", kpiDataWrapperList=" + kpiDataWrapperList + "]";
	}

}

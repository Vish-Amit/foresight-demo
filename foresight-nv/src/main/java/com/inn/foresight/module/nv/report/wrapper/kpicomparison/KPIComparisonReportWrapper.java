package com.inn.foresight.module.nv.report.wrapper.kpicomparison;

import java.util.List;

public class KPIComparisonReportWrapper {

	List<KPIComparisonReportSubWrapper> subWrapperList;

	public List<KPIComparisonReportSubWrapper> getSubWrapperList() {
		return subWrapperList;
	}

	public void setSubWrapperList(List<KPIComparisonReportSubWrapper> subWrapperList) {
		this.subWrapperList = subWrapperList;
	}

	@Override
	public String toString() {
		return "KPIComparisonReportWrapper [subWrapperList=" + subWrapperList + "]";
	}

}

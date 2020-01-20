package com.inn.foresight.module.nv.report.wrapper.kpicomparison;

import java.util.List;

public class KPIComparisonReportSubWrapper {

	List<KPIComparisonInfoWrapper> kpiInfoWrapperlist;

	public List<KPIComparisonInfoWrapper> getKpiInfoWrapperlist() {
		return kpiInfoWrapperlist;
	}

	public void setKpiInforWrapperlist(List<KPIComparisonInfoWrapper> kpiInforWrapperlist) {
		this.kpiInfoWrapperlist = kpiInforWrapperlist;
	}

	@Override
	public String toString() {
		return "KPIComparisonReportSubWrapper [kpiInforWrapperlist=" + kpiInfoWrapperlist + "]";
	}

}

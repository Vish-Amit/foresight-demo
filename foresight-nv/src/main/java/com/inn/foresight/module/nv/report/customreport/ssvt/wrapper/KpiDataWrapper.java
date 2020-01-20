package com.inn.foresight.module.nv.report.customreport.ssvt.wrapper;

import java.util.List;

public class KpiDataWrapper {

	List<KpiMapWrapper> kpiPlotList;
	List<KpiGraphWrapper> kpiGraphList;

	public List<KpiMapWrapper> getKpiPlotList() {
		return kpiPlotList;
	}

	public void setKpiPlotList(List<KpiMapWrapper> kpiPlotList) {
		this.kpiPlotList = kpiPlotList;
	}

	public List<KpiGraphWrapper> getKpiGraphList() {
		return kpiGraphList;
	}

	public void setKpiGraphList(List<KpiGraphWrapper> kpiGraphList) {
		this.kpiGraphList = kpiGraphList;
	}

	
}

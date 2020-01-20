package com.inn.foresight.module.nv.report.customreport.reports.master.wrapper;

import java.util.List;

import com.inn.core.generic.wrapper.JpaWrapper;
@JpaWrapper
public class GraphPlotDataWrapper {

	List<MasterGraphDataWrapper> kpiPlotList;

	public List<MasterGraphDataWrapper> getKpiPlotList() {
		return kpiPlotList;
	}

	public void setKpiPlotList(List<MasterGraphDataWrapper> kpiPlotList) {
		this.kpiPlotList = kpiPlotList;
	}

	@Override public String toString() {
		return "GraphPlotDataWrapper{" + "kpiPlotList=" + kpiPlotList + '}';
	}
}

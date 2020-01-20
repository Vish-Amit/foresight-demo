package com.inn.foresight.module.nv.report.wrapper.inbuilding.benchmark;

import java.util.List;

public class IBBenchmarkGraphPlotWrapper {

	String title;
	List<IBBenchmarkGraphDataWrapper> kpiChartList;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<IBBenchmarkGraphDataWrapper> getKpiChartList() {
		return kpiChartList;
	}
	public void setKpiChartList(List<IBBenchmarkGraphDataWrapper> kpiChartList) {
		this.kpiChartList = kpiChartList;
	}
	
}

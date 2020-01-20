package com.inn.foresight.module.nv.report.wrapper.inbuilding.benchmark;

import java.util.List;

public class IBBenchmarkSummaryWrapper {

	List<IBBenchmarkSummaryKpiWrapper> kpiSummaryList;
	List<IBBenchmarkSummaryKpiWrapper> throughputSummaryList;
	
	public List<IBBenchmarkSummaryKpiWrapper> getKpiSummaryList() {
		return kpiSummaryList;
	}
	public void setKpiSummaryList(List<IBBenchmarkSummaryKpiWrapper> kpiSummaryList) {
		this.kpiSummaryList = kpiSummaryList;
	}
	public List<IBBenchmarkSummaryKpiWrapper> getThroughputSummaryList() {
		return throughputSummaryList;
	}
	public void setThroughputSummaryList(List<IBBenchmarkSummaryKpiWrapper> throughputSummaryList) {
		this.throughputSummaryList = throughputSummaryList;
	}
	
}
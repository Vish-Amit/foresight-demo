package com.inn.foresight.module.nv.report.customreport.ssvt.wrapper;

public class KpiMapWrapper {

	String kpiName;
	String kpiPlot;
	String kpiLegend;

	public String getKpiName() {
		return kpiName;
	}

	public void setKpiName(String kpiName) {
		this.kpiName = kpiName;
	}

	public String getKpiPlot() {
		return kpiPlot;
	}

	public void setKpiPlot(String kpiPlot) {
		this.kpiPlot = kpiPlot;
	}

	public String getKpiLegend() {
		return kpiLegend;
	}

	public void setKpiLegend(String kpiLegend) {
		this.kpiLegend = kpiLegend;
	}

	@Override public String toString() {
		return "KpiMapWrapper{" + "kpiName='" + kpiName + '\'' + ", kpiPlot='" + kpiPlot + '\'' + ", kpiLegend='"
				+ kpiLegend + '\'' + '}';
	}
}

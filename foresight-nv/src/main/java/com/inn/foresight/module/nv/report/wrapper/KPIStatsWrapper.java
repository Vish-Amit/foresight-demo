package com.inn.foresight.module.nv.report.wrapper;

import com.inn.core.generic.wrapper.JpaWrapper;

@JpaWrapper
public class KPIStatsWrapper {

	private Double min;
	private Double max;
	private Double mean;
	private Double avg;
	private Double per90;
	private Double per10;
	private Double per5;
	private Double stdev;
	private String kpi;
	private String tableHeader;

	public Double getPer5() {
		return per5;
	}

	public void setPer5(Double per5) {
		this.per5 = per5;
	}

	public Double getMin() {
		return min;
	}

	public void setMin(Double min) {
		this.min = min;
	}

	public Double getMax() {
		return max;
	}

	public void setMax(Double max) {
		this.max = max;
	}

	public Double getMean() {
		return mean;
	}

	public void setMean(Double mean) {
		this.mean = mean;
	}

	public Double getPer90() {
		return per90;
	}

	public void setPer90(Double per90) {
		this.per90 = per90;
	}

	public Double getStdev() {
		return stdev;
	}

	public void setStdev(Double stdev) {
		this.stdev = stdev;
	}

	public String getKPI() {
		return kpi;
	}

	public void setKPI(String kPI) {
		kpi = kPI;
	}

	public String getTableHeader() {
		return tableHeader;
	}

	public void setTableHeader(String tableHeader) {
		this.tableHeader = tableHeader;
	}

	public Double getAvg() {
		return avg;
	}

	public void setAvg(Double avg) {
		this.avg = avg;
	}

	public Double getPer10() {
		return per10;
	}

	public void setPer10(Double per10) {
		this.per10 = per10;
	}

	@Override
	public String toString() {
		return "KPIStatsWrapper [min=" + min + ", max=" + max + ", mean=" + mean + ", avg=" + avg + ", per90=" + per90 + ", per10=" + per10 + ", per5=" + per5 + ", stdev=" + stdev + ", KPI=" + kpi
				+ ", tableHeader=" + tableHeader + "]";
	}



}

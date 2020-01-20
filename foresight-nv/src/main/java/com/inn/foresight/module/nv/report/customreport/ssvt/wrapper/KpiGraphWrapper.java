package com.inn.foresight.module.nv.report.customreport.ssvt.wrapper;

import com.inn.foresight.module.nv.report.wrapper.GraphDataWrapper;

import java.util.List;

public class KpiGraphWrapper {

	String kpiName;
	Double mean;
	Double min;
	Double max;
	Integer count;
	String unit;
	List<GraphDataWrapper> graphDataList;

	public String getKpiName() {
		return kpiName;
	}

	public void setKpiName(String kpiName) {
		this.kpiName = kpiName;
	}

	public Double getMean() {
		return mean;
	}

	public void setMean(Double mean) {
		this.mean = mean;
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

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public List<GraphDataWrapper> getGraphDataList() {
		return graphDataList;
	}

	public void setGraphDataList(List<GraphDataWrapper> graphDataList) {
		this.graphDataList = graphDataList;
	}

	@Override public String toString() {
		return "KpiGraphWrapper{" + "kpiName='" + kpiName + '\'' + ", mean=" + mean + ", min=" + min + ", max=" + max
				+ ", count=" + count + ", unit='" + unit + '\'' + ", graphDataList=" + graphDataList + '}';
	}
}

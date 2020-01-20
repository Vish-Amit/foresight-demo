package com.inn.foresight.module.nv.report.workorder.wrapper;

public class KPIDataWrapper {

	Integer kpiId;
	String kpiName;
	Double minValue;
	Double maxValue;
	Double avgValue;

	public Integer getKpiId() {
		return kpiId;
	}
	public void setKpiId(Integer kpiId) {
		this.kpiId = kpiId;
	}
	public String getKpiName() {
		return kpiName;
	}
	public void setKpiName(String kpiName) {
		this.kpiName = kpiName;
	}
	public Double getMinValue() {
		return minValue;
	}
	public void setMinValue(Double minValue) {
		this.minValue = minValue;
	}
	public Double getMaxValue() {
		return maxValue;
	}
	public void setMaxValue(Double maxValue) {
		this.maxValue = maxValue;
	}
	public Double getAvgValue() {
		return avgValue;
	}
	public void setAvgValue(Double avgValue) {
		this.avgValue = avgValue;
	}

	@Override
	public String toString() {
		return "KPIDataWrapper [kpiId=" + kpiId + ", kpiName=" + kpiName + ", minValue=" + minValue + ", maxValue="
				+ maxValue + ", avgValue=" + avgValue + "]";
	}


}

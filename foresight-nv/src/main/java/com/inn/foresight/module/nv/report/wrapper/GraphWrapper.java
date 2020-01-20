package com.inn.foresight.module.nv.report.wrapper;

import java.util.List;

public class GraphWrapper {
	private String kpiName;
	private Double min;
	private Double max;
	private Double mean;
	private Double stDev;
	private Double variance;
	private Double threshold;
	private Integer count;
	private Double targetValue;
	private String kpiPlotImg;
	private String kpiLegendImg;
	private String eventLegendImg;
	private List<GraphDataWrapper> graphDataList;
	private String operator1;
	private String operator2;
	private String kpiScore;
	private String technology;
	private String unit;

	public Double getTargetValue() {
		return targetValue;
	}

	public void setTargetValue(Double targetValue) {
		this.targetValue = targetValue;
	}

	public Double getStDev() {
		return stDev;
	}

	public void setStDev(Double stDev) {
		this.stDev = stDev;
	}

	public Double getVariance() {
		return variance;
	}

	public void setVariance(Double variance) {
		this.variance = variance;
	}

	public Double getThreshold() {
		return threshold;
	}

	public void setThreshold(Double threshold) {
		this.threshold = threshold;
	}

	public String getTechnology() {
		return technology;
	}

	public void setTechnology(String technology) {
		this.technology = technology;
	}

	public String getKpiName() {
		return kpiName;
	}

	public void setKpiName(String kpiName) {
		this.kpiName = kpiName;
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

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getKpiPlotImg() {
		return kpiPlotImg;
	}

	public void setKpiPlotImg(String kpiPlotImg) {
		this.kpiPlotImg = kpiPlotImg;
	}

	public String getKpiLegendImg() {
		return kpiLegendImg;
	}

	public void setKpiLegendImg(String kpiLegendImg) {
		this.kpiLegendImg = kpiLegendImg;
	}

	public List<GraphDataWrapper> getGraphDataList() {
		return graphDataList;
	}

	public void setGraphDataList(List<GraphDataWrapper> graphDataList) {
		this.graphDataList = graphDataList;
	}

	public String getOperator1() {
		return operator1;
	}

	public void setOperator1(String operator1) {
		this.operator1 = operator1;
	}

	public String getOperator2() {
		return operator2;
	}

	public void setOperator2(String operator2) {
		this.operator2 = operator2;
	}

	public String getKpiScore() {
		return kpiScore;
	}

	public void setKpiScore(String kpiScore) {
		this.kpiScore = kpiScore;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getEventLegendImg() {
		return eventLegendImg;
	}

	public void setEventLegendImg(String eventLegendImg) {
		this.eventLegendImg = eventLegendImg;
	}

	@Override
	public String toString() {
		return "GraphWrapper [kpiName=" + kpiName + ", min=" + min + ", max=" + max + ", mean=" + mean + ", stDev="
				+ stDev + ", variance=" + variance + ", threshold=" + threshold + ", count=" + count + ", targetValue="
				+ targetValue + ", kpiPlotImg=" + kpiPlotImg + ", kpiLegendImg=" + kpiLegendImg + ", graphDataList="
				+ graphDataList + ", operator1=" + operator1 + ", operator2=" + operator2 + ", kpiScore=" + kpiScore
				+ ", technology=" + technology + "]";
	}

}

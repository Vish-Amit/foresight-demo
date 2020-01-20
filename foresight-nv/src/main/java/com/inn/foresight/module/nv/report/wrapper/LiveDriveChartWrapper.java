package com.inn.foresight.module.nv.report.wrapper;

import java.util.List;

import com.inn.core.generic.wrapper.JpaWrapper;

@JpaWrapper
public class LiveDriveChartWrapper {
	private String chartTitle;
	private String categoryAxisLabel;
	private String valueAxisLabel;
	private Double minKPI;
	private Double maxKPI;
	private Double medKPI;
	private Double per10KPI;
	private Double per90KPI;
	private Double per5KPI;
	private Double mean;
	private Double avg;
	private Double stDev;
	private String score;
	private String chartType;
	private String kpiUnit;
	private String kpi;
	private String graphHeading;
	private String pci;
	private String cause;
	private List<RangeScoreWrapper> rangeScoreWrapperList;
	
	

	public String getPci() {
		return pci;
	}

	public void setPci(String pci) {
		this.pci = pci;
	}

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}

	private List<ChartBeanWrapper> chartList;

	public String getChartTitle() {
		return chartTitle;
	}

	public void setChartTitle(String chartTitle) {
		this.chartTitle = chartTitle;
	}

	public String getCategoryAxisLabel() {
		return categoryAxisLabel;
	}

	public void setCategoryAxisLabel(String categoryAxisLabel) {
		this.categoryAxisLabel = categoryAxisLabel;
	}

	public String getValueAxisLabel() {
		return valueAxisLabel;
	}

	public void setValueAxisLabel(String valueAxisLabel) {
		this.valueAxisLabel = valueAxisLabel;
	}

	public Double getMinKPI() {
		return minKPI;
	}

	public void setMinKPI(Double minKPI) {
		this.minKPI = minKPI;
	}

	public Double getMaxKPI() {
		return maxKPI;
	}

	public void setMaxKPI(Double maxKPI) {
		this.maxKPI = maxKPI;
	}

	public Double getMedKPI() {
		return medKPI;
	}

	public void setMedKPI(Double medKPI) {
		this.medKPI = medKPI;
	}

	public Double getPer10KPI() {
		return per10KPI;
	}

	public void setPer10KPI(Double per10kpi) {
		per10KPI = per10kpi;
	}

	public Double getPer90KPI() {
		return per90KPI;
	}

	public void setPer90KPI(Double per90kpi) {
		per90KPI = per90kpi;
	}

	public Double getPer5KPI() {
		return per5KPI;
	}

	public void setPer5KPI(Double per5kpi) {
		per5KPI = per5kpi;
	}

	public Double getMean() {
		return mean;
	}

	public void setMean(Double mean) {
		this.mean = mean;
	}

	public Double getAvg() {
		return avg;
	}

	public void setAvg(Double avg) {
		this.avg = avg;
	}

	public Double getStDev() {
		return stDev;
	}

	public void setStDev(Double stDev) {
		this.stDev = stDev;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getChartType() {
		return chartType;
	}

	public void setChartType(String chartType) {
		this.chartType = chartType;
	}

	public String getKPIUnit() {
		return kpiUnit;
	}

	public void setKPIUnit(String kPIUnit) {
		kpiUnit = kPIUnit;
	}

	public String getKpi() {
		return kpi;
	}

	public void setKpi(String kpi) {
		this.kpi = kpi;
	}

	public String getGraphHeading() {
		return graphHeading;
	}

	public void setGraphHeading(String graphHeading) {
		this.graphHeading = graphHeading;
	}

	public List<ChartBeanWrapper> getChartList() {
		return chartList;
	}

	public void setChartList(List<ChartBeanWrapper> chartList) {
		this.chartList = chartList;
	}

	public List<RangeScoreWrapper> getRangeScoreWrapperList() {
		return rangeScoreWrapperList;
	}

	public void setRangeScoreWrapperList(List<RangeScoreWrapper> rangeScoreWrapperList) {
		this.rangeScoreWrapperList = rangeScoreWrapperList;
	}

	@Override
	public String toString() {
		return "LiveDriveChartWrapper [chartTitle=" + chartTitle + ", categoryAxisLabel=" + categoryAxisLabel
				+ ", valueAxisLabel=" + valueAxisLabel + ", minKPI=" + minKPI + ", maxKPI=" + maxKPI + ", medKPI="
				+ medKPI + ", per10KPI=" + per10KPI + ", per90KPI=" + per90KPI + ", per5KPI=" + per5KPI + ", mean="
				+ mean + ", avg=" + avg + ", stDev=" + stDev + ", score=" + score + ", chartType=" + chartType
				+ ", KPIUnit=" + kpiUnit + ", kpi=" + kpi + ", graphHeading=" + graphHeading + ", pci=" + pci
				+ ", cause=" + cause + ", rangeScoreWrapperList=" + rangeScoreWrapperList + ", chartList=" + chartList
				+ "]";
	}



}

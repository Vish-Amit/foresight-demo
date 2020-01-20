package com.inn.foresight.module.nv.report;

import java.util.Arrays;
import java.util.List;

public class KPIWrapper {

	Integer indexKPI;
	String kpiName;
	List<RangeSlab> rangeSlabs;
	
	String[] kpiStats;
	Double minValue;
	Double maxValue;
	Integer totalCount;
	Double targetValue;
	
	public boolean isValidPlot() {
		return isValidPlot;
	}

	public void setValidPlot(boolean isValidPlot) {
		this.isValidPlot = isValidPlot;
	}

	boolean isValidPlot;
	
	public Double getTargetValue() {
		return targetValue;
	}

	public void setTargetValue(Double targetValue) {
		this.targetValue = targetValue;
	}

	public void setIndexKPI(Integer indexKPI) {
		this.indexKPI = indexKPI;
	}

	public Double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(Double maxValue) {
		this.maxValue = maxValue;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}


	public Double getMinValue() {
		return minValue;
	}

	public void setMinValue(Double minValue) {
		this.minValue = minValue;
	}

	public String[] getKpiStats() {
		return kpiStats;
	}

	public void setKpiStats(String[] kpiStats) {
		this.kpiStats = kpiStats;
	}

	public String getKpiName() {
		return kpiName;
	}

	public void setKpiName(String kpiName) {
		this.kpiName = kpiName;
	}

	public Integer getIndexKPI() {
		return indexKPI;
	}

	public void setIndexKPI(int indexKPI) {
		this.indexKPI = indexKPI;
	}

	public List<RangeSlab> getRangeSlabs() {
		return rangeSlabs;
	}

	public void setRangeSlabs(List<RangeSlab> rangeSlabs) {
		this.rangeSlabs = rangeSlabs;
	}

	@Override
	public String toString() {
		return "KPIWrapper [indexKPI=" + indexKPI + ", kpiName=" + kpiName + ", rangeSlabs=" + rangeSlabs
				+ ", kpiStats=" + Arrays.toString(kpiStats) + ", minValue=" + minValue + ", maxValue=" + maxValue
				+ ", totalCount=" + totalCount + ", targetValue=" + targetValue + ", isValidPlot=" + isValidPlot + "]";
	}

}

package com.inn.foresight.module.nv.report.workorder.wrapper;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.inn.foresight.module.nv.report.RangeSlab;

public class KPIWrapper {

	Integer indexKPI;
	String kpiName;
	List<RangeSlab> rangeSlabs;

	String[] kpiStats;
	Double minValue;
	Double maxValue;
	Integer totalCount;
	Double targetValue;
	Double averageValue;

	Map<String, Integer> kpiStatMap;

	String operatorName;
	boolean isValidPlot;
	boolean isGraphRequired;
	
	public Double getAverageValue() {
		return averageValue;
	}

	public void setAverageValue(Double averageValue) {
		this.averageValue = averageValue;
	}

	public KPIWrapper() {
		super();
	}

	public KPIWrapper(Integer indexKPI, String kpiName) {
		super();
		this.indexKPI = indexKPI;
		this.kpiName = kpiName;
	}

	public boolean isValidPlot() {
		return isValidPlot;
	}

	public void setValidPlot(boolean isValidPlot) {
		this.isValidPlot = isValidPlot;
	}

	

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

	
	public List<RangeSlab> getRangeSlabs() {
		return rangeSlabs;
	}

	public void setRangeSlabs(List<RangeSlab> rangeSlabs) {
		this.rangeSlabs = rangeSlabs;
	}

	public Map<String, Integer> getKpiStatMap() {
		return kpiStatMap;
	}

	public void setKpiStatMap(Map<String, Integer> kpiStatMap) {
		this.kpiStatMap = kpiStatMap;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	@Override
	public String toString() {
		return "KPIWrapper [indexKPI=" + indexKPI + ", kpiName=" + kpiName + ", rangeSlabs=" + rangeSlabs
				+ ", kpiStats=" + Arrays.toString(kpiStats) + ", minValue=" + minValue + ", maxValue=" + maxValue
				+ ", totalCount=" + totalCount + ", targetValue=" + targetValue + ", averageValue=" + averageValue
				+ ", kpiStatMap=" + kpiStatMap + ", operatorName=" + operatorName + ", isValidPlot=" + isValidPlot
				+ "]";
	}

	

}

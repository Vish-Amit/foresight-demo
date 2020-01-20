package com.inn.foresight.module.nv.report.apassive.wrapper;

import java.awt.Color;

public class ClusterWiseKpiInfo {
	
	String geographyName;
	String kpiName;
	Double avg;
	Double min;
	Double max;
	int count;
	Color color;
	
	
	public String getGeographyName() {
		return geographyName;
	}
	public void setGeographyName(String geographyName) {
		this.geographyName = geographyName;
	}
	public String getKpiName() {
		return kpiName;
	}
	public void setKpiName(String kpiName) {
		this.kpiName = kpiName;
	}
	public Double getAvg() {
		return avg;
	}
	public void setAvg(Double avg) {
		this.avg = avg;
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
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	
	@Override
	public String toString() {
		return "ClusterWiseKpiInfo [geographyName=" + geographyName + ", kpiName=" + kpiName + ", avg=" + avg + ", min="
				+ min + ", max=" + max + ", count=" + count + ", color=" + color + "]";
	}

}

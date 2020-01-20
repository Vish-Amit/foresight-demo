package com.inn.foresight.module.nv.report.wrapper;

import java.io.InputStream;

public class KPIImgDataWrapper {

	private String kpiName;
	private Double avgBeforeKpi;
	private InputStream beforImg;
	private InputStream beforLegendImg;
	private InputStream beforStatImg;
	private Double avgAfterKpi;
	private Double improvement;
	

	private InputStream afterImg;
	private InputStream afterLegendImg;
	private InputStream afterStatImg;
	private String kpiUnit;
	
	public Double getImprovement() {
		return improvement;
	}
	public void setImprovement(Double improvement) {
		this.improvement = improvement;
	}
	public InputStream getAfterStatImg() {
		return afterStatImg;
	}
	public void setAfterStatImg(InputStream afterStatImg) {
		this.afterStatImg = afterStatImg;
	}
	public String getKpiName() {
		return kpiName;
	}
	public void setKpiName(String kpiName) {
		this.kpiName = kpiName;
	}

	public InputStream getBeforStatImg() {
		return beforStatImg;
	}
	public void setBeforStatImg(InputStream beforStatImg) {
		this.beforStatImg = beforStatImg;
	}
	public InputStream getBeforImg() {
		return beforImg;
	}
	public void setBeforImg(InputStream beforImg) {
		this.beforImg = beforImg;
	}
	public InputStream getBeforLegendImg() {
		return beforLegendImg;
	}
	public void setBeforLegendImg(InputStream beforLegendImg) {
		this.beforLegendImg = beforLegendImg;
	}
	public InputStream getAfterImg() {
		return afterImg;
	}
	public void setAfterImg(InputStream afterImg) {
		this.afterImg = afterImg;
	}
	public InputStream getAfterLegendImg() {
		return afterLegendImg;
	}
	public void setAfterLegendImg(InputStream afterLegendImg) {
		this.afterLegendImg = afterLegendImg;
	}
	public Double getAvgBeforeKpi() {
		return avgBeforeKpi;
	}
	public void setAvgBeforeKpi(Double avgBeforeKpi) {
		this.avgBeforeKpi = avgBeforeKpi;
	}
	public Double getAvgAfterKpi() {
		return avgAfterKpi;
	}
	public void setAvgAfterKpi(Double avgAfterKpi) {
		this.avgAfterKpi = avgAfterKpi;
	}
	
	
	public String getKpiUnit() {
		return kpiUnit;
	}
	public void setKpiUnit(String kpiUnit) {
		this.kpiUnit = kpiUnit;
	}
	

}

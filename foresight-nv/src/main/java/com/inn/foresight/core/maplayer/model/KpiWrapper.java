package com.inn.foresight.core.maplayer.model;

public class KpiWrapper {
	Integer legendId;
	String fromDate;
	String toDate;
	String siteStatus;
	String band;
	String kpi;
	String legendConfigType;
	String legendConfigValue;
	
	
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public Integer getLegendId() {
		return legendId;
	}
	public void setLegendId(Integer legendId) {
		this.legendId = legendId;
	}
	public String getBand() {
		return band;
	}
	public void setBand(String band) {
		this.band = band;
	}
	public String getKpi() {
		return kpi;
	}
	public void setKpi(String kpi) {
		this.kpi = kpi;
	}
	public String getSiteStatus() {
		return siteStatus;
	}
	public void setSiteStatus(String siteStatus) {
		this.siteStatus = siteStatus;
	}
	
	public String getLegendConfigType() {
		return legendConfigType;
	}
	
	public void setLegendConfigType(String legendConfigType) {
		this.legendConfigType = legendConfigType;
	}
	
	public String getLegendConfigValue() {
		return legendConfigValue;
	}
	
	public void setLegendConfigValue(String legendConfigValue) {
		this.legendConfigValue = legendConfigValue;
	}
	
	@Override
	public String toString() {
		return "KpiWrapper [legendId=" + legendId + ", fromDate=" + fromDate + ", toDate=" + toDate + ", siteStatus="
				+ siteStatus + ", band=" + band + ", kpi=" + kpi + "]";
	}
}

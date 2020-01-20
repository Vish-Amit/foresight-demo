package com.inn.foresight.module.nv.layer3.qmdlParser.wrappers;

import java.util.Set;

public class NV3GSummaryWrapper {

	private Long tempTimeStamp;
	private Long timeStamp;
	private Long etimeStamp;
	private Double startLat;
	private Double startLong;
	private Double endLat;
	private Double endLong;
	
	/** Wcdma : 3g data */
	private Double[] rscp;
	private Double[] ecio;
	private Double[] rssi;
	private String technologyName;
	private Set<String> kpi3GList;
	
	public Set<String> getKpi3GList() {
		return kpi3GList;
	}
	public void setKpi3GList(Set<String> kpi3gList) {
		kpi3GList = kpi3gList;
	}

	public String getTechnologyName() {
		return technologyName;
	}
	public void setTechnologyName(String technologyName) {
		this.technologyName = technologyName;
	}
	public Double[] getRscp() {
		return rscp;
	}
	public void setRscp(Double[] rscp) {
		this.rscp = rscp;
	}
	public Double[] getEcio() {
		return ecio;
	}
	public void setEcio(Double[] ecio) {
		this.ecio = ecio;
	}
	public Double[] getRssi() {
		return rssi;
	}
	public void setRssi(Double[] rssi) {
		this.rssi = rssi;
	}
	public Long getTempTimeStamp() {
		return tempTimeStamp;
	}
	public void setTempTimeStamp(Long tempTimeStamp) {
		this.tempTimeStamp = tempTimeStamp;
	}
	public Long getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}
	public Long getEtimeStamp() {
		return etimeStamp;
	}
	public void setEtimeStamp(Long etimeStamp) {
		this.etimeStamp = etimeStamp;
	}
	public Double getStartLat() {
		return startLat;
	}
	public void setStartLat(Double startLat) {
		this.startLat = startLat;
	}
	public Double getStartLong() {
		return startLong;
	}
	public void setStartLong(Double startLong) {
		this.startLong = startLong;
	}
	public Double getEndLat() {
		return endLat;
	}
	public void setEndLat(Double endLat) {
		this.endLat = endLat;
	}
	public Double getEndLong() {
		return endLong;
	}
	public void setEndLong(Double endLong) {
		this.endLong = endLong;
	}
}

package com.inn.foresight.module.nv.layer3.qmdlParser.wrappers;

import java.util.Set;

public class NV2GSummaryWrapper {

	private Long tempTimeStamp;
	private Long timeStamp;
	private Long etimeStamp;
	private Double startLat;
	private Double startLong;
	private Double endLat;
	private Double endLong;
	
	/** GSM: 2g data */
	private Double[]  rxLev;
	private Double[]  rxQual;
	private Integer  bCCHChannel;
	private Integer  bSIC;
	private Set<String> kpi2GList;
	
	public Set<String> getKpi2GList() {
		return kpi2GList;
	}
	public void setKpi2GList(Set<String> kpi2gList) {
		kpi2GList = kpi2gList;
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
	public Double[] getRxLev() {
		return rxLev;
	}
	public void setRxLev(Double[] rxLev) {
		this.rxLev = rxLev;
	}
	public Double[] getRxQual() {
		return rxQual;
	}
	public void setRxQual(Double[] rxQual) {
		this.rxQual = rxQual;
	}
	public Integer getbCCHChannel() {
		return bCCHChannel;
	}
	public void setbCCHChannel(Integer bCCHChannel) {
		this.bCCHChannel = bCCHChannel;
	}
	public Integer getbSIC() {
		return bSIC;
	}
	public void setbSIC(Integer bSIC) {
		this.bSIC = bSIC;
	}
	
}

package com.inn.foresight.module.nv.layer3.qmdlParser.wrappers;

public class HandoverDetailWrapper {


	private Long timeStamp;
	private Double lat;
	private Double lon;
	private Double rsrpatHOInit;
	private Double rsrpatHOSuccess;
	private Integer sourcePci;
	private Integer targetPci;
	private Long handoverInterruption;
	private String status;
	
	
	
	public Long getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}
	public Double getLat() {
		return lat;
	}
	public void setLat(Double lat) {
		this.lat = lat;
	}
	public Double getLon() {
		return lon;
	}
	public void setLon(Double lon) {
		this.lon = lon;
	}
	public Double getRsrpatHOInit() {
		return rsrpatHOInit;
	}
	public void setRsrpatHOInit(Double rsrpatHOInit) {
		this.rsrpatHOInit = rsrpatHOInit;
	}
	public Double getRsrpatHOSuccess() {
		return rsrpatHOSuccess;
	}
	public void setRsrpatHOSuccess(Double rsrpatHOSuccess) {
		this.rsrpatHOSuccess = rsrpatHOSuccess;
	}
	public Integer getSourcePci() {
		return sourcePci;
	}
	public void setSourcePci(Integer sourcePci) {
		this.sourcePci = sourcePci;
	}
	public Integer getTargetPci() {
		return targetPci;
	}
	public void setTargetPci(Integer targetPci) {
		this.targetPci = targetPci;
	}
	public Long getHandoverInterruption() {
		return handoverInterruption;
	}
	public void setHandoverInterruption(Long handoverInterruption) {
		this.handoverInterruption = handoverInterruption;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	


}

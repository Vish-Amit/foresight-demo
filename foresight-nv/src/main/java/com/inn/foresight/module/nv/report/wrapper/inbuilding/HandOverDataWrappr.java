package com.inn.foresight.module.nv.report.wrapper.inbuilding;

import com.inn.core.generic.wrapper.JpaWrapper;

@JpaWrapper
public class HandOverDataWrappr {
	private String captureTime;
	private String status;
	private Integer sourcePci;
	private Integer targetPci;
	private String hoLatency; 
	private Double latitude;
	private Double longitude;
	private Integer totalHO;
	private Integer noOfSucessHO;
	private Integer noOfFailHO;


	private String successPercent;
	private String failPercent;

	private String cause;
	private String pcis;


	private Integer enbid;
	private Integer cgi;
	public String getCaptureTime() {
		return captureTime;
	}
	public void setCaptureTime(String captureTime) {
		this.captureTime = captureTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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


	public String getHoLatency() {
		return hoLatency;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setHoLatency(String hoLatency) {
		this.hoLatency = hoLatency;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Integer getTotalHO() {
		return totalHO;
	}
	public void setTotalHO(Integer totalHO) {
		this.totalHO = totalHO;
	}
	public Integer getNoOfSucessHO() {
		return noOfSucessHO;
	}
	public void setNoOfSucessHO(Integer noOfSucessHO) {
		this.noOfSucessHO = noOfSucessHO;
	}
	public Integer getNoOfFailHO() {
		return noOfFailHO;
	}
	public void setNoOfFailHO(Integer noOfFailHO) {
		this.noOfFailHO = noOfFailHO;
	}
	public String getSuccessPercent() {
		return successPercent;
	}
	public void setSuccessPercent(String successPercent) {
		this.successPercent = successPercent;
	}
	public String getFailPercent() {
		return failPercent;
	}
	public void setFailPercent(String failPercent) {
		this.failPercent = failPercent;
	}

	public String getCause() {
		return cause;
	}
	public void setCause(String cause) {
		this.cause = cause;
	}
	public String getPcis() {
		return pcis;
	}
	public void setPcis(String pcis) {
		this.pcis = pcis;
	}
	public Integer getEnbid() {
		return enbid;
	}
	public void setEnbid(Integer enbid) {
		this.enbid = enbid;
	}
	public Integer getCgi() {
		return cgi;
	}
	public void setCgi(Integer cgi) {
		this.cgi = cgi;
	}

	


}

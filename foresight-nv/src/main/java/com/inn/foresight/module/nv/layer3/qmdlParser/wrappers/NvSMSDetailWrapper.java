package com.inn.foresight.module.nv.layer3.qmdlParser.wrappers;

public class NvSMSDetailWrapper {

	private Long sendingTime;
	private Long deliveredTime;
	private Long totalTimeTaken;
	private String status;
	private String neNtatus;
	private String technology;
	private String dailNumber;
	private String mobileNumber;
	private String success;
	private String failureCause;
	
	

	public String getFailureCause() {
		return failureCause;
	}
	public void setFailureCause(String failureCause) {
		this.failureCause = failureCause;
	}
	public Long getSendingTime() {
		return sendingTime;
	}
	public void setSendingTime(Long sendingTime) {
		this.sendingTime = sendingTime;
	}
	public Long getDeliveredTime() {
		return deliveredTime;
	}
	public void setDeliveredTime(Long deliveredTime) {
		this.deliveredTime = deliveredTime;
	}
	public Long getTotalTimeTaken() {
		return totalTimeTaken;
	}
	public void setTotalTimeTaken(Long totalTimeTaken) {
		this.totalTimeTaken = totalTimeTaken;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getNeNtatus() {
		return neNtatus;
	}
	public void setNeNtatus(String neNtatus) {
		this.neNtatus = neNtatus;
	}
	public String getTechnology() {
		return technology;
	}
	public void setTechnology(String technology) {
		this.technology = technology;
	}
	public String getDailNumber() {
		return dailNumber;
	}
	public void setDailNumber(String dailNumber) {
		this.dailNumber = dailNumber;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}



}

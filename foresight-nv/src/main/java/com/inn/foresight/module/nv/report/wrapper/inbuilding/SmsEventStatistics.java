package com.inn.foresight.module.nv.report.wrapper.inbuilding;

public class SmsEventStatistics {

	private String operator;
	private String dateTime;
	private String serialNo;
	private String senderContactNumber;
	private String receiverContactNumber;
	private String totalTime;
	private String status;
	private String result;
	private Double totalCount;
	private Double deliveredCount;
	private Double deliveredRate;
	private String cityName;
	
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public Double getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(Double totalCount) {
		this.totalCount = totalCount;
	}
	public Double getDeliveredCount() {
		return deliveredCount;
	}
	public void setDeliveredCount(Double deliveredCount) {
		this.deliveredCount = deliveredCount;
	}
	public Double getDeliveredRate() {
		return deliveredRate;
	}
	public void setDeliveredRate(Double deliveredRate) {
		this.deliveredRate = deliveredRate;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	public String getSenderContactNumber() {
		return senderContactNumber;
	}
	public void setSenderContactNumber(String senderContactNumber) {
		this.senderContactNumber = senderContactNumber;
	}
	public String getReceiverContactNumber() {
		return receiverContactNumber;
	}
	public void setReceiverContactNumber(String receiverContactNumber) {
		this.receiverContactNumber = receiverContactNumber;
	}
	public String getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(String totalTime) {
		this.totalTime = totalTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	
	
}

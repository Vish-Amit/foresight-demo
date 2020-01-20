package com.inn.foresight.module.nv.workorder.stealth.wrapper;

import com.inn.core.generic.wrapper.RestWrapper;

@RestWrapper
public class StealthWOParameters {

	Integer woId;
	
	Integer stealthTaskId;
	
	Integer stealthTaskResultId;
	
	Integer userId;
	
	String deviceId;
	
	String message;
	
	Long timestamp;

	String acknowledgement;
	
	String status;
	
	String assignmentType;
	
	String operator;
	
	String technology;
	
	Integer totalIteration;
	
	Integer completedIteration;

	String response;
	
	String remark;
	
	Double latitude;
	
	Double longitude;

	public Integer getWoId() {
		return woId;
	}

	public void setWoId(Integer woId) {
		this.woId = woId;
	}

	public Integer getStealthTaskId() {
		return stealthTaskId;
	}

	public void setStealthTaskId(Integer stealthTaskId) {
		this.stealthTaskId = stealthTaskId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getAcknowledgement() {
		return acknowledgement;
	}

	public void setAcknowledgement(String acknowledgement) {
		this.acknowledgement = acknowledgement;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAssignmentType() {
		return assignmentType;
	}

	public void setAssignmentType(String assignmentType) {
		this.assignmentType = assignmentType;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getTechnology() {
		return technology;
	}

	public void setTechnology(String technology) {
		this.technology = technology;
	}

	public Integer getTotalIteration() {
		return totalIteration;
	}

	public void setTotalIteration(Integer totalIteration) {
		this.totalIteration = totalIteration;
	}

	public Integer getCompletedIteration() {
		return completedIteration;
	}

	public void setCompletedIteration(Integer completedIteration) {
		this.completedIteration = completedIteration;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Double getLatitude() {
		return latitude;
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
	
	public Integer getStealthTaskResultId() {
		return stealthTaskResultId;
	}

	public void setStealthTaskResultId(Integer stealthTaskResultId) {
		this.stealthTaskResultId = stealthTaskResultId;
	}

	@Override
	public String toString() {
		return "StealthWOParameters [woId=" + woId + ", stealthTaskId=" + stealthTaskId + ", stealthTaskResultId=" + stealthTaskResultId + ", userId="
				+ userId + ", deviceId=" + deviceId + ", message=" + message + ", timestamp=" + timestamp + ", acknowledgement=" + acknowledgement
				+ ", status=" + status + ", assignmentType=" + assignmentType + ", operator=" + operator + ", technology=" + technology
				+ ", totalIteration=" + totalIteration + ", completedIteration=" + completedIteration + ", response=" + response + ", remark="
				+ remark + ", latitude=" + latitude + ", longitude=" + longitude + "]";
	}

}

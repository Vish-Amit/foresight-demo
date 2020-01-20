package com.inn.foresight.module.nv.workorder.stealth.wrapper;

import com.inn.core.generic.wrapper.RestWrapper;

@RestWrapper
public class StealthTaskDetailWrapper {

private Integer id;	

private Integer woRecipeMappingId;

private Long startTime;

private Long endTime;
	
private String acknowledgement;

private Integer workorderId;

private Integer deviceId;

private Boolean isAssigned;

private String status;

private String remark;

/** The total iteration. */
private Integer totalIteration;

/** The completed iteration. */
private Integer completedIteration;

/** The operator. */
private String operator;

/** The technology. */
private String technology;


public Integer getId() {
	return id;
}

public void setId(Integer id) {
	this.id = id;
}

public Long getStartTime() {
	return startTime;
}

public void setStartTime(Long startTime) {
	this.startTime = startTime;
}

public Long getEndTime() {
	return endTime;
}

public void setEndTime(Long endTime) {
	this.endTime = endTime;
}

public String getAcknowledgement() {
	return acknowledgement;
}

public void setAcknowledgement(String acknowledgement) {
	this.acknowledgement = acknowledgement;
}

public Integer getWorkorderId() {
	return workorderId;
}

public void setWorkorderId(Integer workorderId) {
	this.workorderId = workorderId;
}

public Integer getDeviceId() {
	return deviceId;
}

public void setDeviceId(Integer deviceId) {
	this.deviceId = deviceId;
}

public Boolean getIsAssigned() {
	return isAssigned;
}

public void setIsAssigned(Boolean assigned) {
	this.isAssigned = assigned;
}

public String getStatus() {
	return status;
}

public void setStatus(String status) {
	this.status = status;
}

public String getRemark() {
	return remark;
}

public void setRemark(String remark) {
	this.remark = remark;
}

public Integer getWoRecipeMappingId() {
	return woRecipeMappingId;
}

public void setWoRecipeMappingId(Integer woRecipeMappingId) {
	this.woRecipeMappingId = woRecipeMappingId;
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

@Override
public String toString() {
	return "StealthTaskDetailWrapper [id=" + id + ", woRecipeMappingId=" + woRecipeMappingId + ", startTime=" + startTime + ", endTime=" + endTime
			+ ", acknowledgement=" + acknowledgement + ", workorderId=" + workorderId + ", deviceId=" + deviceId + ", isAssigned=" + isAssigned
			+ ", status=" + status + ", remark=" + remark + ", totalIteration=" + totalIteration + ", completedIteration=" + completedIteration
			+ ", operator=" + operator + ", technology=" + technology + "]";
}

}

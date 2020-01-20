package com.inn.foresight.module.nv.report.wrapper;

public class MessageKpiWrapper {
	
	String startMessageDetail;
	String endMessageDetail;
	String result;
	String isSuccess;
	Double duration;
	String startTime;
	String endTime;
	Long starttimestamp;
	
	
	public String getStartMessageDetail() {
		return startMessageDetail;
	}
	public void setStartMessageDetail(String startMessageDetail) {
		this.startMessageDetail = startMessageDetail;
	}
	public String getEndMessageDetail() {
		return endMessageDetail;
	}
	public void setEndMessageDetail(String endMessageDetail) {
		this.endMessageDetail = endMessageDetail;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getIsSuccess() {
		return isSuccess;
	}
	public void setIsSuccess(String isSuccess) {
		this.isSuccess = isSuccess;
	}
	public Double getDuration() {
		return duration;
	}
	public void setDuration(Double duration) {
		this.duration = duration;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	@Override
	public String toString() {
		return "MessageKpiWrapper [startMessageDetail=" + startMessageDetail + ", endMessageDetail=" + endMessageDetail
				+ ", result=" + result + ", isSuccess=" + isSuccess + ", duration=" + duration + ", startTime="
				+ startTime + ", endTime=" + endTime + "]";
	}
	public Long getStarttimestamp() {
		return starttimestamp;
	}
	public void setStarttimestamp(Long starttimestamp) {
		this.starttimestamp = starttimestamp;
	}
	
}

package com.inn.foresight.module.nv.report.smf.wrapper;

public class MobilityLocationWrapper {
	
	String location;
	Long startTime;
	Long endTime;
	
	
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
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
	@Override
	public String toString() {
		return "MobilityLocationWrapper [location=" + location + ", startTime=" + startTime + ", endTime=" + endTime
				+ "]";
	}
	
	
}

package com.inn.foresight.module.nv.report.smf.wrapper;

public class SmfStationaryWidget {
	
	Integer workorderId;
	String location;
	
	public Integer getWorkorderId() {
		return workorderId;
	}
	public void setWorkorderId(Integer workorderId) {
		this.workorderId = workorderId;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	
	@Override
	public String toString() {
		return "SmfStationaryWidget [workorderId=" + workorderId + ", location=" + location + "]";
	}
	
	
}

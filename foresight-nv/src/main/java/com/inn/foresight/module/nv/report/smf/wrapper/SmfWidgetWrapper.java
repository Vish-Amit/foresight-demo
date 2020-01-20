package com.inn.foresight.module.nv.report.smf.wrapper;

import java.util.List;

public class SmfWidgetWrapper {
	
	String assignTo;
	Integer analyticsrepositoryid_pk;
	
	List<SmfStationaryWidget> stationary;
	SmfMobilityWidgetWrapper mobility;
	
	
	public String getAssignTo() {
		return assignTo;
	}
	public void setAssignTo(String assignTo) {
		this.assignTo = assignTo;
	}
	public List<SmfStationaryWidget> getStationary() {
		return stationary;
	}
	public void setStationary(List<SmfStationaryWidget> stationary) {
		this.stationary = stationary;
	}
	public SmfMobilityWidgetWrapper getMobility() {
		return mobility;
	}
	public void setMobility(SmfMobilityWidgetWrapper mobility) {
		this.mobility = mobility;
	}
	public Integer getAnalyticsrepositoryid_pk() {
		return analyticsrepositoryid_pk;
	}
	public void setAnalyticsrepositoryid_pk(Integer analyticsrepositoryid_pk) {
		this.analyticsrepositoryid_pk = analyticsrepositoryid_pk;
	}
	@Override
	public String toString() {
		return "SmfWidgetWrapper [assignTo=" + assignTo + ", analyticsrepositoryid_pk=" + analyticsrepositoryid_pk
				+ ", stationary=" + stationary + ", mobility=" + mobility + "]";
	}
	
}

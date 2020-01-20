package com.inn.foresight.module.nv.report.stealthdashboard.wrapper;

import java.util.List;

public class StealthDashboardSummaryWrapper {

	String totalDevices;
	String activeDevices;
	String totalSamples;
	String executedTests;
	List<SDGeographyDataWrapper> geographyDevicesList;

	public String getTotalDevices() {
		return totalDevices;
	}

	public void setTotalDevices(String totalDevices) {
		this.totalDevices = totalDevices;
	}

	public String getActiveDevices() {
		return activeDevices;
	}

	public void setActiveDevices(String activeDevices) {
		this.activeDevices = activeDevices;
	}

	public String getTotalSamples() {
		return totalSamples;
	}

	public void setTotalSamples(String totalSamples) {
		this.totalSamples = totalSamples;
	}

	public String getExecutedTests() {
		return executedTests;
	}

	public void setExecutedTests(String executedTests) {
		this.executedTests = executedTests;
	}

	public List<SDGeographyDataWrapper> getGeographyDevicesList() {
		return geographyDevicesList;
	}

	public void setGeographyDevicesList(List<SDGeographyDataWrapper> geographyDevicesList) {
		this.geographyDevicesList = geographyDevicesList;
	}
}

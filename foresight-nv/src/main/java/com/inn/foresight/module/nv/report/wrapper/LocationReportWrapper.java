package com.inn.foresight.module.nv.report.wrapper;

public class LocationReportWrapper {
	
	
	String locationType;
	String locationName;
	Double dlThroughput;
	Double ulThroughput;
	Double latency;
	Double pcktLoss;
	
	Double ytThroughput;
	Double ytBufferingTime;
	Double webPerformance;
	Double responseTime;
	
	

	public String getLocationType() {
		return locationType;
	}

	public Double getYtThroughput() {
		return ytThroughput;
	}

	public void setYtThroughput(Double ytThroughput) {
		this.ytThroughput = ytThroughput;
	}

	public Double getYtBufferingTime() {
		return ytBufferingTime;
	}

	public void setYtBufferingTime(Double ytBufferingTime) {
		this.ytBufferingTime = ytBufferingTime;
	}

	public Double getWebPerformance() {
		return webPerformance;
	}

	public void setWebPerformance(Double webPerformance) {
		this.webPerformance = webPerformance;
	}

	public Double getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(Double responseTime) {
		this.responseTime = responseTime;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public Double getDlThroughput() {
		return dlThroughput;
	}

	public void setDlThroughput(Double dlThroughput) {
		this.dlThroughput = dlThroughput;
	}

	public Double getUlThroughput() {
		return ulThroughput;
	}

	public void setUlThroughput(Double ulThroughput) {
		this.ulThroughput = ulThroughput;
	}

	public Double getLatency() {
		return latency;
	}

	public void setLatency(Double latency) {
		this.latency = latency;
	}

	public Double getPcktLoss() {
		return pcktLoss;
	}

	public void setPcktLoss(Double pcktLoss) {
		this.pcktLoss = pcktLoss;
	}

	@Override
	public String toString() {
		return "LocationReportWrapper [locationType=" + locationType + ", locationName=" + locationName
				+ ", dlThroughput=" + dlThroughput + ", ulThroughput=" + ulThroughput + ", latency=" + latency
				+ ", pcktLoss=" + pcktLoss + ", ytThroughput=" + ytThroughput + ", ytBufferingTime=" + ytBufferingTime
				+ ", webPerformance=" + webPerformance + ", responseTime=" + responseTime + "]";
	}

}

package com.inn.foresight.module.nv.report.wrapper;

public class RawDataWrapper {

	private Double dlThroughput;
	private Double ytDlThroughput;
	private Double ulThroughput;
	private Double latency;
	private Double bufferTime;
	private Double packetLoss;
	private Double dns;
	private String url;
	private String testType;
	private String key;
	private Long capturedTime;
	private String locationName;
	private Double urlResponseTime;
	private String overallKpiScore;
	private String operatorName;
	private Double whatsappLatency;
	private Double volteMosPercent;
	private Double rtpLossPercent;
	

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getOverallKpiScore() {
		return overallKpiScore;
	}

	public void setOverallKpiScore(String overallKpiScore) {
		this.overallKpiScore = overallKpiScore;
	}

	public Double getUrlResponseTime() {
		return urlResponseTime;
	}

	public void setUrlResponseTime(Double urlResponseTime) {
		this.urlResponseTime = urlResponseTime;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public Long getCapturedTime() {
		return capturedTime;
	}

	public void setCapturedTime(Long capturedTime) {
		this.capturedTime = capturedTime;
	}

	public Double getYtDlThroughput() {
		return ytDlThroughput;
	}

	public void setYtDlThroughput(Double ytDlThroughput) {
		this.ytDlThroughput = ytDlThroughput;
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

	public Double getBufferTime() {
		return bufferTime;
	}

	public void setBufferTime(Double bufferTime) {
		this.bufferTime = bufferTime;
	}

	public Double getPacketLoss() {
		return packetLoss;
	}

	public void setPacketLoss(Double packetLoss) {
		this.packetLoss = packetLoss;
	}

	public Double getDns() {
		return dns;
	}

	public void setDns(Double dns) {
		this.dns = dns;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTestType() {
		return testType;
	}

	public void setTestType(String testType) {
		this.testType = testType;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Double getWhatsappLatency() {
		return whatsappLatency;
	}

	public void setWhatsappLatency(Double whatsappLatency) {
		this.whatsappLatency = whatsappLatency;
	}
	
	public Double getVolteMosPercent() {
		return volteMosPercent;
	}

	public void setVolteMosPercent(Double volteMosPercent) {
		this.volteMosPercent = volteMosPercent;
	}

	public Double getRtpLossPercent() {
		return rtpLossPercent;
	}

	public void setRtpLossPercent(Double rtpLossPercent) {
		this.rtpLossPercent = rtpLossPercent;
	}

	@Override
	public String toString() {
		return "RawDataWrapper [dlThroughput=" + dlThroughput + ", ytDlThroughput=" + ytDlThroughput + ", ulThroughput="
				+ ulThroughput + ", latency=" + latency + ", bufferTime=" + bufferTime + ", packetLoss=" + packetLoss
				+ ", dns=" + dns + ", url=" + url + ", testType=" + testType + ", key=" + key + ", capturedTime="
				+ capturedTime + ", locationName=" + locationName + ", urlResponseTime=" + urlResponseTime
				+ ", overallKpiScore=" + overallKpiScore + ", operatorName=" + operatorName + ", whatsappLatency="
				+ whatsappLatency + ", volteMosPercent=" + volteMosPercent + ", rtpLossPercent=" + rtpLossPercent + "]";
	}

}

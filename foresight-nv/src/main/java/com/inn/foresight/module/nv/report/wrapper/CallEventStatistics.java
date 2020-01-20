package com.inn.foresight.module.nv.report.wrapper;

public class CallEventStatistics {
	
	private String woCategory;
	private String kpiName;
	private String testType;
	private String callAttempt;
	private String callDrop;
	private String callSuccess;
	private String callFailure;
	private Double esaRate;
	private Double dropRate;
	private String cityName;
	
	
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getWoCategory() {
		return woCategory;
	}
	public void setWoCategory(String woCategory) {
		this.woCategory = woCategory;
	}
	public String getKpiName() {
		return kpiName;
	}
	public void setKpiName(String kpiName) {
		this.kpiName = kpiName;
	}
	public String getTestType() {
		return testType;
	}
	public void setTestType(String testType) {
		this.testType = testType;
	}
	public String getCallAttempt() {
		return callAttempt;
	}
	public void setCallAttempt(String callAttempt) {
		this.callAttempt = callAttempt;
	}
	public String getCallDrop() {
		return callDrop;
	}
	public void setCallDrop(String callDrop) {
		this.callDrop = callDrop;
	}
	public String getCallSuccess() {
		return callSuccess;
	}
	public void setCallSuccess(String callSuccess) {
		this.callSuccess = callSuccess;
	}
	public String getCallFailure() {
		return callFailure;
	}
	public void setCallFailure(String callFailure) {
		this.callFailure = callFailure;
	}
	public Double getEsaRate() {
		return esaRate;
	}
	public void setEsaRate(Double esaRate) {
		this.esaRate = esaRate;
	}
	public Double getDropRate() {
		return dropRate;
	}
	public void setDropRate(Double dropRate) {
		this.dropRate = dropRate;
	}
	@Override
	public String toString() {
		return "CallEventStatistics [woCategory=" + woCategory + ", kpiName=" + kpiName + ", testType=" + testType
				+ ", callAttempt=" + callAttempt + ", callDrop=" + callDrop + ", callSuccess=" + callSuccess
				+ ", callFailure=" + callFailure + ", esaRate=" + esaRate + ", dropRate=" + dropRate + ", cityName="
				+ cityName + "]";
	}
	
		
}

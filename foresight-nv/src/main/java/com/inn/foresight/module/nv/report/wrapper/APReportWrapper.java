package com.inn.foresight.module.nv.report.wrapper;

public class APReportWrapper {

	/** QuickTest/FullTest. */
	private String testType;
	/** Active/passive. */
	private String recordType;
	/** QuickTest / FullTest (Discarded Field). */
	private String testSubtype;

	/** Android /Ios. */
	private String deviceOs;
	/** NV_ENTERPRISE / Netvelocity. */
	private String nvModule;

	private String geographyl1;
	private String geographyl2;
	private String geographyl3;
	private String geographyl4;

	private String band;
	private String operator;

	/** Min kpi variables. */
	private Double minRsrp;
	private Double minSinr;
	private Double minRsrq;
	private Double minRssi;
	private Double minUlRate;
	private Double minDlRate;
	private Double minWebDlDelay;
	private Double minJitter;
	private Double minLatency;

	/** Max kpi variables. */
	private Double maxRsrp;
	private Double maxSinr;
	private Double maxRsrq;
	private Double maxRssi;
	private Double maxUlRate;
	private Double maxDlRate;
	private Double maxWebDlDelay;
	private Double maxJitter;
	private Double maxLatency;

	/** Avg kpi variables. */
	private Double avgRsrp;
	private Double avgSinr;
	private Double avgRsrq;
	private Double avgRssi;
	private Double avgUlRate;
	private Double avgDlRate;
	private Double avgWebDlDelay;
	private Double avgJitter;
	private Double avgLatency;



	public Double getMinRsrp() {
		return minRsrp;
	}
	public void setMinRsrp(Double minRsrp) {
		this.minRsrp = minRsrp;
	}
	public Double getMinSinr() {
		return minSinr;
	}
	public void setMinSinr(Double minSinr) {
		this.minSinr = minSinr;
	}
	public Double getMinRsrq() {
		return minRsrq;
	}
	public void setMinRsrq(Double minRsrq) {
		this.minRsrq = minRsrq;
	}
	public Double getMinRssi() {
		return minRssi;
	}
	public void setMinRssi(Double minRssi) {
		this.minRssi = minRssi;
	}
	public Double getMinUlRate() {
		return minUlRate;
	}
	public void setMinUlRate(Double minUlRate) {
		this.minUlRate = minUlRate;
	}
	public Double getMinDlRate() {
		return minDlRate;
	}
	public void setMinDlRate(Double minDlRate) {
		this.minDlRate = minDlRate;
	}
	public Double getMinLatency() {
		return minLatency;
	}
	public void setMinLatency(Double minLatency) {
		this.minLatency = minLatency;
	}
	public String getTestType() {
		return testType;
	}
	public void setTestType(String testType) {
		this.testType = testType;
	}
	public String getRecordType() {
		return recordType;
	}
	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}
	public String getTestSubtype() {
		return testSubtype;
	}
	public void setTestSubtype(String testSubtype) {
		this.testSubtype = testSubtype;
	}
	public String getDeviceOs() {
		return deviceOs;
	}
	public void setDeviceOs(String deviceOs) {
		this.deviceOs = deviceOs;
	}
	public String getNvModule() {
		return nvModule;
	}
	public void setNvModule(String nvModule) {
		this.nvModule = nvModule;
	}
	public String getGeographyl1() {
		return geographyl1;
	}
	public void setGeographyl1(String geographyl1) {
		this.geographyl1 = geographyl1;
	}
	public String getGeographyl2() {
		return geographyl2;
	}
	public void setGeographyl2(String geographyl2) {
		this.geographyl2 = geographyl2;
	}
	public String getGeographyl3() {
		return geographyl3;
	}
	public void setGeographyl3(String geographyl3) {
		this.geographyl3 = geographyl3;
	}
	public String getGeographyl4() {
		return geographyl4;
	}
	public void setGeographyl4(String geographyl4) {
		this.geographyl4 = geographyl4;
	}
	public String getBand() {
		return band;
	}
	public void setBand(String band) {
		this.band = band;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public Double getMinWebDlDelay() {
		return minWebDlDelay;
	}
	public void setMinWebDlDelay(Double minWebDlDelay) {
		this.minWebDlDelay = minWebDlDelay;
	}
	public Double getMinJitter() {
		return minJitter;
	}
	public void setMinJitter(Double minJitter) {
		this.minJitter = minJitter;
	}
	public Double getMaxRsrp() {
		return maxRsrp;
	}
	public void setMaxRsrp(Double maxRsrp) {
		this.maxRsrp = maxRsrp;
	}
	public Double getMaxSinr() {
		return maxSinr;
	}
	public void setMaxSinr(Double maxSinr) {
		this.maxSinr = maxSinr;
	}
	public Double getMaxRsrq() {
		return maxRsrq;
	}
	public void setMaxRsrq(Double maxRsrq) {
		this.maxRsrq = maxRsrq;
	}
	public Double getMaxRssi() {
		return maxRssi;
	}
	public void setMaxRssi(Double maxRssi) {
		this.maxRssi = maxRssi;
	}
	public Double getMaxUlRate() {
		return maxUlRate;
	}
	public void setMaxUlRate(Double maxUlRate) {
		this.maxUlRate = maxUlRate;
	}
	public Double getMaxDlRate() {
		return maxDlRate;
	}
	public void setMaxDlRate(Double maxDlRate) {
		this.maxDlRate = maxDlRate;
	}
	public Double getMaxWebDlDelay() {
		return maxWebDlDelay;
	}
	public void setMaxWebDlDelay(Double maxWebDlDelay) {
		this.maxWebDlDelay = maxWebDlDelay;
	}
	public Double getMaxJitter() {
		return maxJitter;
	}
	public void setMaxJitter(Double maxJitter) {
		this.maxJitter = maxJitter;
	}
	public Double getMaxLatency() {
		return maxLatency;
	}
	public void setMaxLatency(Double maxLatency) {
		this.maxLatency = maxLatency;
	}
	public Double getAvgRsrp() {
		return avgRsrp;
	}
	public void setAvgRsrp(Double avgRsrp) {
		this.avgRsrp = avgRsrp;
	}
	public Double getAvgSinr() {
		return avgSinr;
	}
	public void setAvgSinr(Double avgSinr) {
		this.avgSinr = avgSinr;
	}
	public Double getAvgRsrq() {
		return avgRsrq;
	}
	public void setAvgRsrq(Double avgRsrq) {
		this.avgRsrq = avgRsrq;
	}
	public Double getAvgRssi() {
		return avgRssi;
	}
	public void setAvgRssi(Double avgRssi) {
		this.avgRssi = avgRssi;
	}
	public Double getAvgUlRate() {
		return avgUlRate;
	}
	public void setAvgUlRate(Double avgUlRate) {
		this.avgUlRate = avgUlRate;
	}
	public Double getAvgDlRate() {
		return avgDlRate;
	}
	public void setAvgDlRate(Double avgDlRate) {
		this.avgDlRate = avgDlRate;
	}
	public Double getAvgWebDlDelay() {
		return avgWebDlDelay;
	}
	public void setAvgWebDlDelay(Double avgWebDlDelay) {
		this.avgWebDlDelay = avgWebDlDelay;
	}
	public Double getAvgJitter() {
		return avgJitter;
	}
	public void setAvgJitter(Double avgJitter) {
		this.avgJitter = avgJitter;
	}
	public Double getAvgLatency() {
		return avgLatency;
	}
	public void setAvgLatency(Double avgLatency) {
		this.avgLatency = avgLatency;
	}
	@Override
	public String toString() {
		return "APReportWrapper [testType=" + testType + ", recordType=" + recordType + ", testSubtype=" + testSubtype
				+ ", deviceOs=" + deviceOs + ", nvModule=" + nvModule + ", geographyl1=" + geographyl1
				+ ", geographyl2=" + geographyl2 + ", geographyl3=" + geographyl3 + ", geographyl4=" + geographyl4
				+ ", band=" + band + ", operator=" + operator + ", minRsrp=" + minRsrp + ", minSinr=" + minSinr
				+ ", minRsrq=" + minRsrq + ", minRssi=" + minRssi + ", minUlRate=" + minUlRate + ", minDlRate="
				+ minDlRate + ", minWebDlDelay=" + minWebDlDelay + ", minJitter=" + minJitter + ", minLatency="
				+ minLatency + ", maxRsrp=" + maxRsrp + ", maxSinr=" + maxSinr + ", maxRsrq=" + maxRsrq + ", maxRssi="
				+ maxRssi + ", maxUlRate=" + maxUlRate + ", maxDlRate=" + maxDlRate + ", maxWebDlDelay=" + maxWebDlDelay
				+ ", maxJitter=" + maxJitter + ", maxLatency=" + maxLatency + ", avgRsrp=" + avgRsrp + ", avgSinr="
				+ avgSinr + ", avgRsrq=" + avgRsrq + ", avgRssi=" + avgRssi + ", avgUlRate=" + avgUlRate
				+ ", avgDlRate=" + avgDlRate + ", avgWebDlDelay=" + avgWebDlDelay + ", avgJitter=" + avgJitter
				+ ", avgLatency=" + avgLatency + "]";
	}

}

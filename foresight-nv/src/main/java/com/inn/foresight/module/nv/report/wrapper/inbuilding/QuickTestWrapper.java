package com.inn.foresight.module.nv.report.wrapper.inbuilding;

import com.inn.core.generic.wrapper.JpaWrapper;

@JpaWrapper
public class QuickTestWrapper {

	private Integer testNo;
	private Double dlThoughput;
	private Double ulThoughput;
	private Double latency;
	private Double avgRSRP;
	private Double avgSINR;
	public Integer getTestNo() {
		return testNo;
	}
	public void setTestNo(Integer testNo) {
		this.testNo = testNo;
	}
	public Double getDlThoughput() {
		return dlThoughput;
	}
	public void setDlThoughput(Double dlThoughput) {
		this.dlThoughput = dlThoughput;
	}
	public Double getUlThoughput() {
		return ulThoughput;
	}
	public void setUlThoughput(Double ulThoughput) {
		this.ulThoughput = ulThoughput;
	}
	public Double getLatency() {
		return latency;
	}
	public void setLatency(Double latency) {
		this.latency = latency;
	}
	public Double getAvgRSRP() {
		return avgRSRP;
	}
	public void setAvgRSRP(Double avgRSRP) {
		this.avgRSRP = avgRSRP;
	}
	public Double getAvgSINR() {
		return avgSINR;
	}
	public void setAvgSINR(Double avgSINR) {
		this.avgSINR = avgSINR;
	}
	

}
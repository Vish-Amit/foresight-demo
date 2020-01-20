package com.inn.foresight.module.nv.report.wrapper.stealth;

public class StealthWOBestWorstItemWrapper {

	String clusterName;
	Double rsrp;
	String ttl;
	String dlThroughput;

	public String getClusterName() {
		return clusterName;
	}

	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}


	public Double getRsrp() {
		return rsrp;
	}

	public void setRsrp(Double rsrp) {
		this.rsrp = rsrp;
	}

	public String getTtl() {
		return ttl;
	}

	public void setTtl(String ttl) {
		this.ttl = ttl;
	}

	public String getDlThroughput() {
		return dlThroughput;
	}

	public void setDlThroughput(String dlThroughput) {
		this.dlThroughput = dlThroughput;
	}

	@Override
	public String toString() {
		return "StealthWOBestWorstItemWrapper [clusterName=" + clusterName + ", rsrp=" + rsrp + ", ttl=" + ttl
				+ ", dlThroughput=" + dlThroughput + "]";
	}

}

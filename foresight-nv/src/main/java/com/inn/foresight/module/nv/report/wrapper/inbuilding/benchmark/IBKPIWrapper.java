package com.inn.foresight.module.nv.report.wrapper.inbuilding.benchmark;

public class IBKPIWrapper {

	private Double rsrp;
	private Double sinr;
	private Double dlRate;
	private Double ulRate;
	
	public Double getRsrp() {
		return rsrp;
	}
	public void setRsrp(Double rsrp) {
		this.rsrp = rsrp;
	}
	public Double getSinr() {
		return sinr;
	}
	public void setSinr(Double sinr) {
		this.sinr = sinr;
	}
	public Double getDlRate() {
		return dlRate;
	}
	public void setDlRate(Double dlRate) {
		this.dlRate = dlRate;
	}
	public Double getUlRate() {
		return ulRate;
	}
	public void setUlRate(Double ulRate) {
		this.ulRate = ulRate;
	}

	@Override
	public String toString() {
		return "IBKPIWrapper [rsrp=" + rsrp + ", sinr=" + sinr + ", dlRate=" + dlRate + ", ulRate=" + ulRate + "]";
	}
	
}

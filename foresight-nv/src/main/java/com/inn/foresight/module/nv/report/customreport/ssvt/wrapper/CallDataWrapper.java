package com.inn.foresight.module.nv.report.customreport.ssvt.wrapper;

public class CallDataWrapper {
	
	String testingKPI;
	String target;
	String status;
	String sector1;
	String sector2;
	String sector3;
	
	public String getTestingKPI() {
		return testingKPI;
	}
	public void setTestingKPI(String testingKPI) {
		this.testingKPI = testingKPI;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSector1() {
		return sector1;
	}
	public void setSector1(String sector1) {
		this.sector1 = sector1;
	}
	public String getSector2() {
		return sector2;
	}
	public void setSector2(String sector2) {
		this.sector2 = sector2;
	}
	public String getSector3() {
		return sector3;
	}
	public void setSector3(String sector3) {
		this.sector3 = sector3;
	}
	@Override
	public String toString() {
		return "CallDataWrapper [testingKPI=" + testingKPI + ", target=" + target + ", status=" + status + ", sector1="
				+ sector1 + ", sector2=" + sector2 + ", sector3=" + sector3 + "]";
	}
}

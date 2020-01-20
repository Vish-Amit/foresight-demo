package com.inn.foresight.module.nv.report.wrapper;

public class AvrageKpiWrapper {
private Double avgRsrp;
private Double avgSinr;
private Double avgDl;
private Double avgUl;
private String startDate;
private String workorderName;


public String getWorkorderName() {
	return workorderName;
}
public void setWorkorderName(String workorderName) {
	this.workorderName = workorderName;
}
public String getStartDate() {
	return startDate;
}
public void setStartDate(String startDate) {
	this.startDate = startDate;
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
public Double getAvgDl() {
	return avgDl;
}
public void setAvgDl(Double avgDl) {
	this.avgDl = avgDl;
}
public Double getAvgUl() {
	return avgUl;
}
public void setAvgUl(Double avgUl) {
	this.avgUl = avgUl;
}
@Override
public String toString() {
	return "AvrageKpiWrapper [avgRsrp=" + avgRsrp + ", avgSinr=" + avgSinr + ", avgDl=" + avgDl + ", avgUl=" + avgUl
			+ ", startDate=" + startDate + "]";
}



}

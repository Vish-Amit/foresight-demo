package com.inn.foresight.module.nv.report.wrapper;

public class IBCTableDataHolder {
private Integer attemptCount;
private Integer successCount;

private Integer cellID;
private Integer pci;
private Integer cgi;
private String status;
private Double peakValue;

private Double rate;
private Double avg;
private Double avgRsrp;
private Double avgSinr;
public Integer getAttemptCount() {
	return attemptCount;
}
public void setAttemptCount(Integer attemptCount) {
	this.attemptCount = attemptCount;
}
public Integer getSuccessCount() {
	return successCount;
}
public void setSuccessCount(Integer successCount) {
	this.successCount = successCount;
}
public Integer getCellID() {
	return cellID;
}
public void setCellID(Integer cellID) {
	this.cellID = cellID;
}
public Integer getPci() {
	return pci;
}
public void setPci(Integer pci) {
	this.pci = pci;
}
public Integer getCgi() {
	return cgi;
}
public void setCgi(Integer cgi) {
	this.cgi = cgi;
}
public String getStatus() {
	return status;
}
public void setStatus(String status) {
	this.status = status;
}
public Double getPeakValue() {
	return peakValue;
}
public void setPeakValue(Double peakValue) {
	this.peakValue = peakValue;
}
public Double getRate() {
	return rate;
}
public void setRate(Double rate) {
	this.rate = rate;
}
public Double getAvg() {
	return avg;
}
public void setAvg(Double avg) {
	this.avg = avg;
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



}

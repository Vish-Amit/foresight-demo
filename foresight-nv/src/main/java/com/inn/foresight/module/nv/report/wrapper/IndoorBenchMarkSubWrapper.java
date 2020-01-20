package com.inn.foresight.module.nv.report.wrapper;

public class IndoorBenchMarkSubWrapper {
private String operatorName;
private String band;
private String frequency;
private String cellIds;
private String technologies;
private String bandwidth;
private Double httpDlThp;
private Double dlThreshold;
private Double httpUlThp;
private Double ulThreshold;
private Double browseTimeDelay;
private Double browseTimeDelyIn3Sites;
private Double latency;
private Double intialBuffering;
private Double latencyGoogle;
private Double meanCst;
private Double maxCst;
private Double minCst;

private Integer callIntitate;
private Integer callSuccess;
private Integer callDrop;
private Integer callFail;
private Double callSucessRate;
private Double callDropRate;

private Double mosPloqaMean;

private String name;
private String details;

private Double mosPercent;
private Integer mosCount;

private String callFailCause;



public String getCallFailCause() {
	return callFailCause;
}
public void setCallFailCause(String callFailCause) {
	this.callFailCause = callFailCause;
}
public String getOperatorName() {
	return operatorName;
}
public void setOperatorName(String operatorName) {
	this.operatorName = operatorName;
}
public String getBand() {
	return band;
}
public void setBand(String band) {
	this.band = band;
}
public String getFrequency() {
	return frequency;
}
public void setFrequency(String frequency) {
	this.frequency = frequency;
}
public String getCellIds() {
	return cellIds;
}
public void setCellIds(String cellIds) {
	this.cellIds = cellIds;
}
public String getTechnologies() {
	return technologies;
}
public void setTechnologies(String technologies) {
	this.technologies = technologies;
}
public String getBandwidth() {
	return bandwidth;
}
public void setBandwidth(String bandwidth) {
	this.bandwidth = bandwidth;
}

public Double getHttpDlThp() {
	return httpDlThp;
}
public void setHttpDlThp(Double httpDlThp) {
	this.httpDlThp = httpDlThp;
}
public Double getDlThreshold() {
	return dlThreshold;
}
public void setDlThreshold(Double dlThreshold) {
	this.dlThreshold = dlThreshold;
}
public Double getHttpUlThp() {
	return httpUlThp;
}
public void setHttpUlThp(Double httpUlThp) {
	this.httpUlThp = httpUlThp;
}
public Double getUlThreshold() {
	return ulThreshold;
}
public void setUlThreshold(Double ulThreshold) {
	this.ulThreshold = ulThreshold;
}

public Double getBrowseTimeDelay() {
	return browseTimeDelay;
}
public void setBrowseTimeDelay(Double browseTimeDelay) {
	this.browseTimeDelay = browseTimeDelay;
}
public Double getBrowseTimeDelyIn3Sites() {
	return browseTimeDelyIn3Sites;
}
public void setBrowseTimeDelyIn3Sites(Double browseTimeDelyIn3Sites) {
	this.browseTimeDelyIn3Sites = browseTimeDelyIn3Sites;
}
public Double getLatency() {
	return latency;
}
public void setLatency(Double latency) {
	this.latency = latency;
}

public Double getIntialBuffering() {
	return intialBuffering;
}
public void setIntialBuffering(Double intialBuffering) {
	this.intialBuffering = intialBuffering;
}

public Double getLatencyGoogle() {
	return latencyGoogle;
}
public void setLatencyGoogle(Double latencyGoogle) {
	this.latencyGoogle = latencyGoogle;
}

public Double getMeanCst() {
	return meanCst;
}
public void setMeanCst(Double meanCst) {
	this.meanCst = meanCst;
}
public Double getMaxCst() {
	return maxCst;
}
public void setMaxCst(Double maxCst) {
	this.maxCst = maxCst;
}
public Double getMinCst() {
	return minCst;
}
public void setMinCst(Double minCst) {
	this.minCst = minCst;
}
public Integer getCallIntitate() {
	return callIntitate;
}
public void setCallIntitate(Integer callIntitate) {
	this.callIntitate = callIntitate;
}
public Integer getCallSuccess() {
	return callSuccess;
}
public void setCallSuccess(Integer callSuccess) {
	this.callSuccess = callSuccess;
}
public Integer getCallDrop() {
	return callDrop;
}
public void setCallDrop(Integer callDrop) {
	this.callDrop = callDrop;
}
public Integer getCallFail() {
	return callFail;
}
public void setCallFail(Integer callFail) {
	this.callFail = callFail;
}
public Double getCallSucessRate() {
	return callSucessRate;
}
public void setCallSucessRate(Double callSucessRate) {
	this.callSucessRate = callSucessRate;
}

public Double getCallDropRate() {
	return callDropRate;
}
public void setCallDropRate(Double callDropRate) {
	this.callDropRate = callDropRate;
}
public Double getMosPloqaMean() {
	return mosPloqaMean;
}
public void setMosPloqaMean(Double mosPloqaMean) {
	this.mosPloqaMean = mosPloqaMean;
}

public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getDetails() {
	return details;
}
public void setDetails(String details) {
	this.details = details;
}

public Double getMosPercent() {
	return mosPercent;
}
public void setMosPercent(Double mosPercent) {
	this.mosPercent = mosPercent;
}
public Integer getMosCount() {
	return mosCount;
}
public void setMosCount(Integer mosCount) {
	this.mosCount = mosCount;
}
@Override
public String toString() {
	return "IndoorBenchMarkSubWrapper [operatorName=" + operatorName + ", band=" + band + ", frequency=" + frequency
			+ ", cellIds=" + cellIds + ", technologies=" + technologies + ", bandwidth=" + bandwidth + ", httpDlThp="
			+ httpDlThp + ", dlThreshold=" + dlThreshold + ", httpUlThp=" + httpUlThp + ", ulThreshold=" + ulThreshold
			+ ", browseTimeDelay=" + browseTimeDelay + ", browseTimeDelyIn3Sites=" + browseTimeDelyIn3Sites
			+ ", latency=" + latency + ", intialBuffering=" + intialBuffering + ", latencyGoogle=" + latencyGoogle
			+ ", meanCst=" + meanCst + ", maxCst=" + maxCst + ", minCst=" + minCst + ", callIntitate=" + callIntitate
			+ ", callSuccess=" + callSuccess + ", callDrop=" + callDrop + ", callFail=" + callFail + ", callSucessRate="
			+ callSucessRate + ", callDropRate=" + callDropRate + ", mosPloqaMean=" + mosPloqaMean + ", name=" + name
			+ ", details=" + details + ", mosPercent=" + mosPercent + ", mosCount=" + mosCount + ", callFailCause="
			+ callFailCause + "]";
}





	
}

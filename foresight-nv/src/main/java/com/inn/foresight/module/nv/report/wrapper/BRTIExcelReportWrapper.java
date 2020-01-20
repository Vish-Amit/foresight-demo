package com.inn.foresight.module.nv.report.wrapper;

public class BRTIExcelReportWrapper {
private String frequency;
private String day;
private String date;
private String hour;
private String place;
private String address;
private String inforamtion;
private Integer noOfSms;


private String sentIn3min;
private String smsInfo;
private String serivce;
private String cityName;

public String getCityName() {
	return cityName;
}
public void setCityName(String cityName) {
	this.cityName = cityName;
}
private Integer totalCall;
private Integer failCall;
private Integer dropcall;
private Double callEsaRate;
private Double callDropRate;

private Integer totalSms;
private Integer smsDeliivered;
private Double smsDeliveryRate;

private Integer totalCallOnnet;
private Integer failCallOnnet;
private Integer dropcallOnnet;
private Double callEsaRateOnnet;
private Double callDropRateOnnet;

private Integer totalSmsOnnet;
private Double smsDeliveryRateOnnet;

private Integer totalCallOffnet;
private Integer failCallOffnet;
private Integer dropcallOffnet;
private Double callEsaRateOffnet;
private Double callDropRateOffnet;
private Integer totalSmsOffnet;
private Integer smsDeliveredOnnetIn3Min;

private Integer smsDeliveredOffnetIn3min;
private Integer totalSmsDeliveredIn3Min;
private Integer smsDeliiveredOnnet;

private Integer smsDeliiveredOffnet;
private Double smsDeliveryRateOffnet;
private String startDate;
private String endDate;


private Integer successCall;
private Integer blockedCall;
private Double callSetupTime;
private Double meanOpinionScore;
private boolean isStationaryData;
private Double  smsOnNetRateIn3Min;
private Double  smsoffNetRateIn3Min;
private Double  smsRateIn3Min;


private Integer successCallOnNet;
private Integer blockedCallOnNet;
private Double callSetupTimeOnNet;
private Double meanOpinionScoreOnNet;


private Integer successCallOffNet;
private Integer blockedCallOffNet;
private Double callSetupTimeOffNet;
private Double meanOpinionScoreOffNet;

private Integer httpDownloadAttempt;
private Integer httpDownloadSuccess;
private Double httpDownloadTimeAvg;
private Double httpDlSuccessRate;

private Double httpThroughputAvg;
private Double networkLatency;
private Double packetLoss;
private Double smsDeliveredAvgTime;
private Double smsDeliverdOnNetAvgTime;
private Double smsDeliverdOffNetAvgTime;
private Integer smsDeliveredSum;
private Integer smsDeliveredSumOnNet;
private Integer smsDeliveredSumOffNet;
private String transmissionMode;




public String getTransmissionMode() {
	return transmissionMode;
}
public void setTransmissionMode(String transmissionMode) {
	this.transmissionMode = transmissionMode;
}
public Integer getSmsDeliveredSum() {
	return smsDeliveredSum;
}
public void setSmsDeliveredSum(Integer smsDeliveredSum) {
	this.smsDeliveredSum = smsDeliveredSum;
}
public Integer getSmsDeliveredSumOnNet() {
	return smsDeliveredSumOnNet;
}
public void setSmsDeliveredSumOnNet(Integer smsDeliveredSumOnNet) {
	this.smsDeliveredSumOnNet = smsDeliveredSumOnNet;
}
public Integer getSmsDeliveredSumOffNet() {
	return smsDeliveredSumOffNet;
}
public void setSmsDeliveredSumOffNet(Integer smsDeliveredSumOffNet) {
	this.smsDeliveredSumOffNet = smsDeliveredSumOffNet;
}
public Double getSmsDeliveredAvgTime() {
	return smsDeliveredAvgTime;
}
public void setSmsDeliveredAvgTime(Double smsDeliveredAvgTime) {
	this.smsDeliveredAvgTime = smsDeliveredAvgTime;
}
public Double getSmsDeliverdOnNetAvgTime() {
	return smsDeliverdOnNetAvgTime;
}
public void setSmsDeliverdOnNetAvgTime(Double smsDeliverdOnNetAvgTime) {
	this.smsDeliverdOnNetAvgTime = smsDeliverdOnNetAvgTime;
}
public Double getSmsDeliverdOffNetAvgTime() {
	return smsDeliverdOffNetAvgTime;
}
public void setSmsDeliverdOffNetAvgTime(Double smsDeliverdOffNetAvgTime) {
	this.smsDeliverdOffNetAvgTime = smsDeliverdOffNetAvgTime;
}
public boolean isStationaryData() {
	return isStationaryData;
}
public void setStationaryData(boolean isStationaryData) {
	this.isStationaryData = isStationaryData;
}
public Integer getSmsDeliveredOnnetIn3Min() {
	return smsDeliveredOnnetIn3Min;
}
public void setSmsDeliveredOnnetIn3Min(Integer smsDeliveredOnnetIn3Min) {
	this.smsDeliveredOnnetIn3Min = smsDeliveredOnnetIn3Min;
}
public Integer getSmsDeliveredOffnetIn3min() {
	return smsDeliveredOffnetIn3min;
}
public void setSmsDeliveredOffnetIn3min(Integer smsDeliveredOffnetIn3min) {
	this.smsDeliveredOffnetIn3min = smsDeliveredOffnetIn3min;
}
public Integer getTotalSmsDeliveredIn3Min() {
	return totalSmsDeliveredIn3Min;
}
public void setTotalSmsDeliveredIn3Min(Integer totalSmsDeliveredIn3Min) {
	this.totalSmsDeliveredIn3Min = totalSmsDeliveredIn3Min;
}

public Double getHttpDlSuccessRate() {
	return httpDlSuccessRate;
}
public void setHttpDlSuccessRate(Double httpDlSuccessRate) {
	this.httpDlSuccessRate = httpDlSuccessRate;
}
public Integer getHttpDownloadAttempt() {
	return httpDownloadAttempt;
}
public void setHttpDownloadAttempt(Integer httpDownloadAttempt) {
	this.httpDownloadAttempt = httpDownloadAttempt;
}
public Integer getHttpDownloadSuccess() {
	return httpDownloadSuccess;
}
public void setHttpDownloadSuccess(Integer httpDownloadSuccess) {
	this.httpDownloadSuccess = httpDownloadSuccess;
}
public Double getHttpDownloadTimeAvg() {
	return httpDownloadTimeAvg;
}
public void setHttpDownloadTimeAvg(Double httpDownloadTimeAvg) {
	this.httpDownloadTimeAvg = httpDownloadTimeAvg;
}
public Double getHttpThroughputAvg() {
	return httpThroughputAvg;
}
public void setHttpThroughputAvg(Double httpThroughputAvg) {
	this.httpThroughputAvg = httpThroughputAvg;
}
public Double getNetworkLatency() {
	return networkLatency;
}
public void setNetworkLatency(Double networkLatency) {
	this.networkLatency = networkLatency;
}
public Double getPacketLoss() {
	return packetLoss;
}
public void setPacketLoss(Double packetLoss) {
	this.packetLoss = packetLoss;
}
public Integer getSuccessCall() {
	return successCall;
}
public void setSuccessCall(Integer successCall) {
	this.successCall = successCall;
}
public Integer getBlockedCall() {
	return blockedCall;
}
public void setBlockedCall(Integer blockedCall) {
	this.blockedCall = blockedCall;
}
public Double getCallSetupTime() {
	return callSetupTime;
}
public void setCallSetupTime(Double callSetupTime) {
	this.callSetupTime = callSetupTime;
}
public Double getMeanOpinionScore() {
	return meanOpinionScore;
}
public void setMeanOpinionScore(Double meanOpinionScore) {
	this.meanOpinionScore = meanOpinionScore;
}


public Double getCallSetupTimeOnNet() {
	return callSetupTimeOnNet;
}
public void setCallSetupTimeOnNet(Double callSetupTimeOnNet) {
	this.callSetupTimeOnNet = callSetupTimeOnNet;
}
public Double getMeanOpinionScoreOnNet() {
	return meanOpinionScoreOnNet;
}
public void setMeanOpinionScoreOnNet(Double meanOpinionScoreOnNet) {
	this.meanOpinionScoreOnNet = meanOpinionScoreOnNet;
}
public Integer getSuccessCallOffNet() {
	return successCallOffNet;
}
public void setSuccessCallOffNet(Integer successCallOffNet) {
	this.successCallOffNet = successCallOffNet;
}
public Integer getBlockedCallOffNet() {
	return blockedCallOffNet;
}
public void setBlockedCallOffNet(Integer blockedCallOffNet) {
	this.blockedCallOffNet = blockedCallOffNet;
}
public Double getCallSetupTimeOffNet() {
	return callSetupTimeOffNet;
}
public void setCallSetupTimeOffNet(Double callSetupTimeOffNet) {
	this.callSetupTimeOffNet = callSetupTimeOffNet;
}
public Double getMeanOpinionScoreOffNet() {
	return meanOpinionScoreOffNet;
}
public void setMeanOpinionScoreOffNet(Double meanOpinionScoreOffNet) {
	this.meanOpinionScoreOffNet = meanOpinionScoreOffNet;
}
public Integer getSuccessCallOnNet() {
	return successCallOnNet;
}
public void setSuccessCallOnNet(Integer successCallOnNet) {
	this.successCallOnNet = successCallOnNet;
}
public Integer getBlockedCallOnNet() {
	return blockedCallOnNet;
}
public void setBlockedCallOnNet(Integer blockedCallOnNet) {
	this.blockedCallOnNet = blockedCallOnNet;
}

public String getFrequency() {
	return frequency;
}
public void setFrequency(String frequency) {
	this.frequency = frequency;
}
public String getDay() {
	return day;
}
public void setDay(String day) {
	this.day = day;
}
public String getDate() {
	return date;
}
public void setDate(String date) {
	this.date = date;
}
public String getHour() {
	return hour;
}
public void setHour(String hour) {
	this.hour = hour;
}
public String getPlace() {
	return place;
}
public void setPlace(String place) {
	this.place = place;
}
public String getAddress() {
	return address;
}
public void setAddress(String address) {
	this.address = address;
}
public String getInforamtion() {
	return inforamtion;
}
public void setInforamtion(String inforamtion) {
	this.inforamtion = inforamtion;
}
public Integer getNoOfSms() {
	return noOfSms;
}
public void setNoOfSms(Integer noOfSms) {
	this.noOfSms = noOfSms;
}
public String getSentIn3min() {
	return sentIn3min;
}
public void setSentIn3min(String sentIn3min) {
	this.sentIn3min = sentIn3min;
}
public String getSmsInfo() {
	return smsInfo;
}
public void setSmsInfo(String smsInfo) {
	this.smsInfo = smsInfo;
}
public String getSerivce() {
	return serivce;
}
public void setSerivce(String serivce) {
	this.serivce = serivce;
}
public Integer getTotalCall() {
	return totalCall;
}
public void setTotalCall(Integer totalCall) {
	this.totalCall = totalCall;
}
public Integer getFailCall() {
	return failCall;
}
public void setFailCall(Integer failCall) {
	this.failCall = failCall;
}
public Integer getDropcall() {
	return dropcall;
}
public void setDropcall(Integer dropcall) {
	this.dropcall = dropcall;
}
public Double getCallEsaRate() {
	return callEsaRate;
}
public void setCallEsaRate(Double callEsaRate) {
	this.callEsaRate = callEsaRate;
}
public Double getCallDropRate() {
	return callDropRate;
}
public void setCallDropRate(Double callDropRate) {
	this.callDropRate = callDropRate;
}
public Integer getTotalSms() {
	return totalSms;
}
public void setTotalSms(Integer totalSms) {
	this.totalSms = totalSms;
}
public Integer getSmsDeliivered() {
	return smsDeliivered;
}
public void setSmsDeliivered(Integer smsDeliivered) {
	this.smsDeliivered = smsDeliivered;
}
public Double getSmsDeliveryRate() {
	return smsDeliveryRate;
}
public void setSmsDeliveryRate(Double smsDeliveryRate) {
	this.smsDeliveryRate = smsDeliveryRate;
}
public Integer getTotalCallOnnet() {
	return totalCallOnnet;
}
public void setTotalCallOnnet(Integer totalCallOnnet) {
	this.totalCallOnnet = totalCallOnnet;
}
public Integer getFailCallOnnet() {
	return failCallOnnet;
}
public void setFailCallOnnet(Integer failCallOnnet) {
	this.failCallOnnet = failCallOnnet;
}
public Integer getDropcallOnnet() {
	return dropcallOnnet;
}
public void setDropcallOnnet(Integer dropcallOnnet) {
	this.dropcallOnnet = dropcallOnnet;
}
public Double getCallEsaRateOnnet() {
	return callEsaRateOnnet;
}
public void setCallEsaRateOnnet(Double callEsaRateOnnet) {
	this.callEsaRateOnnet = callEsaRateOnnet;
}
public Double getCallDropRateOnnet() {
	return callDropRateOnnet;
}
public void setCallDropRateOnnet(Double callDropRateOnnet) {
	this.callDropRateOnnet = callDropRateOnnet;
}
public Integer getTotalSmsOnnet() {
	return totalSmsOnnet;
}
public void setTotalSmsOnnet(Integer totalSmsOnnet) {
	this.totalSmsOnnet = totalSmsOnnet;
}
public Integer getSmsDeliiveredOnnet() {
	return smsDeliiveredOnnet;
}
public void setSmsDeliiveredOnnet(Integer smsDeliiveredOnnet) {
	this.smsDeliiveredOnnet = smsDeliiveredOnnet;
}
public Double getSmsDeliveryRateOnnet() {
	return smsDeliveryRateOnnet;
}
public void setSmsDeliveryRateOnnet(Double smsDeliveryRateOnnet) {
	this.smsDeliveryRateOnnet = smsDeliveryRateOnnet;
}
public Integer getTotalCallOffnet() {
	return totalCallOffnet;
}
public void setTotalCallOffnet(Integer totalCallOffnet) {
	this.totalCallOffnet = totalCallOffnet;
}
public Integer getFailCallOffnet() {
	return failCallOffnet;
}
public void setFailCallOffnet(Integer failCallOffnet) {
	this.failCallOffnet = failCallOffnet;
}
public Integer getDropcallOffnet() {
	return dropcallOffnet;
}
public void setDropcallOffnet(Integer dropcallOffnet) {
	this.dropcallOffnet = dropcallOffnet;
}
public Double getCallEsaRateOffnet() {
	return callEsaRateOffnet;
}
public void setCallEsaRateOffnet(Double callEsaRateOffnet) {
	this.callEsaRateOffnet = callEsaRateOffnet;
}
public Double getCallDropRateOffnet() {
	return callDropRateOffnet;
}
public void setCallDropRateOffnet(Double callDropRateOffnet) {
	this.callDropRateOffnet = callDropRateOffnet;
}
public Integer getTotalSmsOffnet() {
	return totalSmsOffnet;
}
public void setTotalSmsOffnet(Integer totalSmsOffnet) {
	this.totalSmsOffnet = totalSmsOffnet;
}
public Integer getSmsDeliiveredOffnet() {
	return smsDeliiveredOffnet;
}
public void setSmsDeliiveredOffnet(Integer smsDeliiveredOffnet) {
	this.smsDeliiveredOffnet = smsDeliiveredOffnet;
}
public Double getSmsDeliveryRateOffnet() {
	return smsDeliveryRateOffnet;
}
public void setSmsDeliveryRateOffnet(Double smsDeliveryRateOffnet) {
	this.smsDeliveryRateOffnet = smsDeliveryRateOffnet;
}
public String getStartDate() {
	return startDate;
}
public void setStartDate(String startDate) {
	this.startDate = startDate;
}
public String getEndDate() {
	return endDate;
}
public void setEndDate(String endDate) {
	this.endDate = endDate;
}

public Double getSmsOnNetRateIn3Min() {
	return smsOnNetRateIn3Min;
}
public void setSmsOnNetRateIn3Min(Double smsOnNetRateIn3Min) {
	this.smsOnNetRateIn3Min = smsOnNetRateIn3Min;
}
public Double getSmsoffNetRateIn3Min() {
	return smsoffNetRateIn3Min;
}
public void setSmsoffNetRateIn3Min(Double smsoffNetRateIn3Min) {
	this.smsoffNetRateIn3Min = smsoffNetRateIn3Min;
}
public Double getSmsRateIn3Min() {
	return smsRateIn3Min;
}
public void setSmsRateIn3Min(Double smsRateIn3Min) {
	this.smsRateIn3Min = smsRateIn3Min;
}
@Override
public String toString() {
	return "BRTIExcelReportWrapper [frequency=" + frequency + ", day=" + day + ", date=" + date + ", hour=" + hour
			+ ", place=" + place + ", address=" + address + ", inforamtion=" + inforamtion + ", noOfSms=" + noOfSms
			+ ", sentIn3min=" + sentIn3min + ", smsInfo=" + smsInfo + ", serivce=" + serivce + ", cityName=" + cityName
			+ ", totalCall=" + totalCall + ", failCall=" + failCall + ", dropcall=" + dropcall + ", callEsaRate="
			+ callEsaRate + ", callDropRate=" + callDropRate + ", totalSms=" + totalSms + ", smsDeliivered="
			+ smsDeliivered + ", smsDeliveryRate=" + smsDeliveryRate + ", totalCallOnnet=" + totalCallOnnet
			+ ", failCallOnnet=" + failCallOnnet + ", dropcallOnnet=" + dropcallOnnet + ", callEsaRateOnnet="
			+ callEsaRateOnnet + ", callDropRateOnnet=" + callDropRateOnnet + ", totalSmsOnnet=" + totalSmsOnnet
			+ ", smsDeliveryRateOnnet=" + smsDeliveryRateOnnet + ", totalCallOffnet=" + totalCallOffnet
			+ ", failCallOffnet=" + failCallOffnet + ", dropcallOffnet=" + dropcallOffnet + ", callEsaRateOffnet="
			+ callEsaRateOffnet + ", callDropRateOffnet=" + callDropRateOffnet + ", totalSmsOffnet=" + totalSmsOffnet
			+ ", smsDeliveredOnnetIn3Min=" + smsDeliveredOnnetIn3Min + ", smsDeliveredOffnetIn3min="
			+ smsDeliveredOffnetIn3min + ", totalSmsDeliveredIn3Min=" + totalSmsDeliveredIn3Min
			+ ", smsDeliiveredOnnet=" + smsDeliiveredOnnet + ", smsDeliiveredOffnet=" + smsDeliiveredOffnet
			+ ", smsDeliveryRateOffnet=" + smsDeliveryRateOffnet + ", startDate=" + startDate + ", endDate=" + endDate
			+ ", successCall=" + successCall + ", blockedCall=" + blockedCall + ", callSetupTime=" + callSetupTime
			+ ", meanOpinionScore=" + meanOpinionScore + ", isStationaryData=" + isStationaryData
			+ ", smsOnNetRateIn3Min=" + smsOnNetRateIn3Min + ", smsoffNetRateIn3Min=" + smsoffNetRateIn3Min
			+ ", smsRateIn3Min=" + smsRateIn3Min + ", successCallOnNet=" + successCallOnNet + ", blockedCallOnNet="
			+ blockedCallOnNet + ", callSetupTimeOnNet=" + callSetupTimeOnNet + ", meanOpinionScoreOnNet="
			+ meanOpinionScoreOnNet + ", successCallOffNet=" + successCallOffNet + ", blockedCallOffNet="
			+ blockedCallOffNet + ", callSetupTimeOffNet=" + callSetupTimeOffNet + ", meanOpinionScoreOffNet="
			+ meanOpinionScoreOffNet + ", httpDownloadAttempt=" + httpDownloadAttempt + ", httpDownloadSuccess="
			+ httpDownloadSuccess + ", httpDownloadTimeAvg=" + httpDownloadTimeAvg + ", httpDlSuccessRate="
			+ httpDlSuccessRate + ", httpThroughputAvg=" + httpThroughputAvg + ", networkLatency=" + networkLatency
			+ ", packetLoss=" + packetLoss + ", smsDeliveredAvgTime=" + smsDeliveredAvgTime
			+ ", smsDeliverdOnNetAvgTime=" + smsDeliverdOnNetAvgTime + ", smsDeliverdOffNetAvgTime="
			+ smsDeliverdOffNetAvgTime + ", smsDeliveredSum=" + smsDeliveredSum + ", smsDeliveredSumOnNet="
			+ smsDeliveredSumOnNet + ", smsDeliveredSumOffNet=" + smsDeliveredSumOffNet + ", transmissionMode="
			+ transmissionMode + "]";
}

}

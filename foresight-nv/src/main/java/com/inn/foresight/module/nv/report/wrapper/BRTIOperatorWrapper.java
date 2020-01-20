package com.inn.foresight.module.nv.report.wrapper;

import java.util.List;

import com.inn.foresight.module.nv.report.wrapper.inbuilding.SmsEventStatistics;

public class BRTIOperatorWrapper {
	private String cityName;
	private List<CallEventStatistics> callEventStatsDrive;
	private List<CallEventStatistics> callEventStatsStationary;
	private List<CallEventStatistics> callEvenCityWise;
	private List<SmsEventStatistics> smsEvenCityWise;
	private List<SmsEventStatistics> callEvenCityWiseOnNetOffNet;



	public String getCityName() {
		return cityName;
	}



	public void setCityName(String cityName) {
		this.cityName = cityName;
	}



	public List<CallEventStatistics> getCallEventStatsDrive() {
		return callEventStatsDrive;
	}



	public void setCallEventStatsDrive(List<CallEventStatistics> callEventStatsDrive) {
		this.callEventStatsDrive = callEventStatsDrive;
	}



	public List<CallEventStatistics> getCallEventStatsStationary() {
		return callEventStatsStationary;
	}



	public void setCallEventStatsStationary(List<CallEventStatistics> callEventStatsStationary) {
		this.callEventStatsStationary = callEventStatsStationary;
	}



	public List<CallEventStatistics> getCallEvenCityWise() {
		return callEvenCityWise;
	}



	public void setCallEvenCityWise(List<CallEventStatistics> callEvenCityWise) {
		this.callEvenCityWise = callEvenCityWise;
	}



	public List<SmsEventStatistics> getSmsEvenCityWise() {
		return smsEvenCityWise;
	}



	public void setSmsEvenCityWise(List<SmsEventStatistics> smsEvenCityWise) {
		this.smsEvenCityWise = smsEvenCityWise;
	}



	public List<SmsEventStatistics> getCallEvenCityWiseOnNetOffNet() {
		return callEvenCityWiseOnNetOffNet;
	}



	public void setCallEvenCityWiseOnNetOffNet(List<SmsEventStatistics> callEvenCityWiseOnNetOffNet) {
		this.callEvenCityWiseOnNetOffNet = callEvenCityWiseOnNetOffNet;
	}



	@Override
	public String toString() {
		return "BRTIOperatorWrapper [cityName=" + cityName + ", callEventStatsDrive=" + callEventStatsDrive
				+ ", callEventStatsStationary=" + callEventStatsStationary + ", callEvenCityWise=" + callEvenCityWise
				+ ", smsEvenCityWise=" + smsEvenCityWise + ", callEvenCityWiseOnNetOffNet="
				+ callEvenCityWiseOnNetOffNet + "]";
	}


}

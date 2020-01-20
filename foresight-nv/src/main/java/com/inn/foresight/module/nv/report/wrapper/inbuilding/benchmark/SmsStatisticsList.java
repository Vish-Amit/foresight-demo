package com.inn.foresight.module.nv.report.wrapper.inbuilding.benchmark;

import java.util.List;

import com.inn.foresight.module.nv.report.wrapper.inbuilding.SmsEventStatistics;

public class SmsStatisticsList {

	List<SmsEventStatistics> smsEventStatisticsStationary;

	@Override
	public String toString() {
		return "SmsStatisticsList [smsEventStatisticsStationary=" + smsEventStatisticsStationary + "]";
	}

	public List<SmsEventStatistics> getSmsEventStatisticsStationary() {
		return smsEventStatisticsStationary;
	}

	public void setSmsEventStatisticsStationary(List<SmsEventStatistics> smsEventStatisticsStationary) {
		this.smsEventStatisticsStationary = smsEventStatisticsStationary;
	}
	
}

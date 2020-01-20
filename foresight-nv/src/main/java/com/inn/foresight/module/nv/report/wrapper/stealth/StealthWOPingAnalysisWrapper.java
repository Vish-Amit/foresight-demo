package com.inn.foresight.module.nv.report.wrapper.stealth;

import java.util.List;

public class StealthWOPingAnalysisWrapper {

	List<StealthWOPingItemWrapper> pingAnalysisDataList;
	String daysCount;
	String avgPingLatency;
	String avgPingJitter;
	String avgPingPacketLoss;

	public List<StealthWOPingItemWrapper> getPingAnalysisDataList() {
		return pingAnalysisDataList;
	}

	public void setPingAnalysisDataList(List<StealthWOPingItemWrapper> pingAnalysisDataList) {
		this.pingAnalysisDataList = pingAnalysisDataList;
	}

	public String getDaysCount() {
		return daysCount;
	}

	public void setDaysCount(String daysCount) {
		this.daysCount = daysCount;
	}

	public String getAvgPingLatency() {
		return avgPingLatency;
	}

	public void setAvgPingLatency(String avgPingLatency) {
		this.avgPingLatency = avgPingLatency;
	}

	public String getAvgPingJitter() {
		return avgPingJitter;
	}

	public void setAvgPingJitter(String avgPingJitter) {
		this.avgPingJitter = avgPingJitter;
	}

	public String getAvgPingPacketLoss() {
		return avgPingPacketLoss;
	}

	public void setAvgPingPacketLoss(String avgPingPacketLoss) {
		this.avgPingPacketLoss = avgPingPacketLoss;
	}

	@Override
	public String toString() {
		return "StealthWOPingAnalysisWrapper [pingAnalysisDataList=" + pingAnalysisDataList + ", daysCount=" + daysCount
				+ ", avgPingLatency=" + avgPingLatency + ", avgPingJitter=" + avgPingJitter + ", avgPingPacketLoss="
				+ avgPingPacketLoss + "]";
	}

}

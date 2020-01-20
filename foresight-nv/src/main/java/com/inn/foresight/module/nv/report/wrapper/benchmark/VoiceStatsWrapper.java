package com.inn.foresight.module.nv.report.wrapper.benchmark;

import com.inn.core.generic.wrapper.JpaWrapper;

@JpaWrapper
public class VoiceStatsWrapper {

	private String operator;
	private String callAttemptCount;
	private String callConnectedCount;
	private String callDroppedCount;
	private String callSetupSucessRate;
	private String callFailCount;

	private String callDropRate;

	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getCallAttemptCount() {
		return callAttemptCount;
	}
	public void setCallAttemptCount(String callAttemptCount) {
		this.callAttemptCount = callAttemptCount;
	}
	public String getCallConnectedCount() {
		return callConnectedCount;
	}
	public void setCallConnectedCount(String callConnectedCount) {
		this.callConnectedCount = callConnectedCount;
	}
	public String getCallDroppedCount() {
		return callDroppedCount;
	}
	public void setCallDroppedCount(String callDroppedCount) {
		this.callDroppedCount = callDroppedCount;
	}
	public String getCallSetupSucessRate() {
		return callSetupSucessRate;
	}
	public void setCallSetupSucessRate(String callSetupSucessRate) {
		this.callSetupSucessRate = callSetupSucessRate;
	}
	public String getCallDropRate() {
		return callDropRate;
	}
	public void setCallDropRate(String callDropRate) {
		this.callDropRate = callDropRate;
	}
	
	public String getCallFailCount() {
		return callFailCount;
	}
	public void setCallFailCount(String callFailCount) {
		this.callFailCount = callFailCount;
	}
	@Override
	public String toString() {
		return "VoceStatsWrapper [operator=" + operator + ", callAttemptCount=" + callAttemptCount
				+ ", callConnectedCount=" + callConnectedCount + ", callDroppedCount=" + callDroppedCount
				+ ", callSetupSucessRate=" + callSetupSucessRate + ", callDropRate=" + callDropRate + "]";
	}





}

package com.inn.foresight.module.nv.report.customreport.reports.master.wrapper;

import com.inn.core.generic.wrapper.JpaWrapper;

@JpaWrapper
public class VolteCallData {

	String callEndTime;
	String callDuration;
	Double initRsrp;
	Integer initPci;
	String callSetupTime;
	Double avgPktLossRate;
	Double jitter;
	Double avgMOS;
	String callStartTime;
	String callReleaseReason;

	/**
	 * @return the callEndTime
	 */
	public String getCallEndTime() {
		return callEndTime;
	}

	/**
	 * @param callEndTime the callEndTime to set
	 */
	public void setCallEndTime(String callEndTime) {
		this.callEndTime = callEndTime;
	}

	/**
	 * @return the callDuration
	 */
	public String getCallDuration() {
		return callDuration;
	}

	/**
	 * @param callDuration the callDuration to set
	 */
	public void setCallDuration(String callDuration) {
		this.callDuration = callDuration;
	}

	/**
	 * @return the initRsrp
	 */
	public Double getInitRsrp() {
		return initRsrp;
	}

	/**
	 * @param initRsrp the initRsrp to set
	 */
	public void setInitRsrp(Double initRsrp) {
		this.initRsrp = initRsrp;
	}

	/**
	 * @return the initPci
	 */
	public Integer getInitPci() {
		return initPci;
	}

	/**
	 * @param initPci the initPci to set
	 */
	public void setInitPci(Integer initPci) {
		this.initPci = initPci;
	}

	/**
	 * @return the callSetupTime
	 */
	public String getCallSetupTime() {
		return callSetupTime;
	}

	/**
	 * @param callSetupTime the callSetupTime to set
	 */
	public void setCallSetupTime(String callSetupTime) {
		this.callSetupTime = callSetupTime;
	}

	/**
	 * @return the avgPktLossRate
	 */
	public Double getAvgPktLossRate() {
		return avgPktLossRate;
	}

	/**
	 * @param avgPktLossRate the avgPktLossRate to set
	 */
	public void setAvgPktLossRate(Double avgPktLossRate) {
		this.avgPktLossRate = avgPktLossRate;
	}

	/**
	 * @return the jitter
	 */
	public Double getJitter() {
		return jitter;
	}

	/**
	 * @param jitter the jitter to set
	 */
	public void setJitter(Double jitter) {
		this.jitter = jitter;
	}

	/**
	 * @return the avgMOS
	 */
	public Double getAvgMOS() {
		return avgMOS;
	}

	/**
	 * @param avgMOS the avgMOS to set
	 */
	public void setAvgMOS(Double avgMOS) {
		this.avgMOS = avgMOS;
	}

	/**
	 * @return the callStartTime
	 */
	public String getCallStartTime() {
		return callStartTime;
	}

	/**
	 * @param callStartTime the callStartTime to set
	 */
	public void setCallStartTime(String callStartTime) {
		this.callStartTime = callStartTime;
	}

	/**
	 * @return the callReleaseReason
	 */
	public String getCallReleaseReason() {
		return callReleaseReason;
	}

	/**
	 * @param callReleaseReason the callReleaseReason to set
	 */
	public void setCallReleaseReason(String callReleaseReason) {
		this.callReleaseReason = callReleaseReason;
	}

	@Override
	public String toString() {
		return "VolteCallDataWrapper [callEndTime=" + callEndTime + ", callDuration=" + callDuration + ", initRsrp="
				+ initRsrp + ", initPci=" + initPci + ", callSetupTime=" + callSetupTime + ", avgPktLossRate="
				+ avgPktLossRate + ", jitter=" + jitter + ", avgMOS=" + avgMOS + ", callStartTime=" + callStartTime
				+ ", callReleaseReason=" + callReleaseReason + "]";
	}

}

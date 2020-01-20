package com.inn.foresight.module.nv.report.customreport.ssvt.wrapper;

import com.inn.core.generic.wrapper.JpaWrapper;

@JpaWrapper
public class HOItemWrapper {

	String timestamp;
	String latitude;
	String longitude;
	String initRsrp;
	String sourcePci;
	String completeRsrp;
	String targetPCI;
	String testStatus;
	String interruptionTime;

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getInitRsrp() {
		return initRsrp;
	}

	public void setInitRsrp(String initRsrp) {
		this.initRsrp = initRsrp;
	}

	public String getSourcePci() {
		return sourcePci;
	}

	public void setSourcePci(String sourcePci) {
		this.sourcePci = sourcePci;
	}

	public String getCompleteRsrp() {
		return completeRsrp;
	}

	public void setCompleteRsrp(String completeRsrp) {
		this.completeRsrp = completeRsrp;
	}

	public String getTargetPCI() {
		return targetPCI;
	}

	public void setTargetPCI(String targetPCI) {
		this.targetPCI = targetPCI;
	}

	public String getTestStatus() {
		return testStatus;
	}

	public void setTestStatus(String testStatus) {
		this.testStatus = testStatus;
	}

	public String getInterruptionTime() {
		return interruptionTime;
	}

	public void setInterruptionTime(String interruptionTime) {
		this.interruptionTime = interruptionTime;
	}

	@Override
	public String toString() {
		return "HOItemWrapper [timestamp=" + timestamp + ", latitude=" + latitude + ", longitude=" + longitude
				+ ", initRsrp=" + initRsrp + ", sourcePci=" + sourcePci + ", completeRsrp=" + completeRsrp
				+ ", targetPCI=" + targetPCI + ", testStatus=" + testStatus + ", interruptionTime=" + interruptionTime
				+ "]";
	}

}

package com.inn.foresight.module.nv.report.wrapper.stealth;

import java.util.List;

public class StealthWOSummaryWrapper {

	String assigned;
	String accepted;
	String rejected;
	String pending;
	List<StealthDateWiseStatusWrapper> dateWiseStatusList;

	public String getAssigned() {
		return assigned;
	}

	public void setAssigned(String assigned) {
		this.assigned = assigned;
	}

	public String getAccepted() {
		return accepted;
	}

	public void setAccepted(String accepted) {
		this.accepted = accepted;
	}

	public String getRejected() {
		return rejected;
	}

	public void setRejected(String rejected) {
		this.rejected = rejected;
	}

	public String getPending() {
		return pending;
	}

	public void setPending(String pending) {
		this.pending = pending;
	}

	public List<StealthDateWiseStatusWrapper> getDateWiseStatusList() {
		return dateWiseStatusList;
	}

	public void setDateWiseStatusList(List<StealthDateWiseStatusWrapper> dateWiseStatusList) {
		this.dateWiseStatusList = dateWiseStatusList;
	}

	@Override
	public String toString() {
		return "StealthWOSummaryWrapper [assigned=" + assigned + ", accepted=" + accepted + ", rejected=" + rejected
				+ ", pending=" + pending + ", dateWiseStatusList=" + dateWiseStatusList + "]";
	}

}

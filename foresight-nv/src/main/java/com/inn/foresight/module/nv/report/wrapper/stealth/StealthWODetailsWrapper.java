package com.inn.foresight.module.nv.report.wrapper.stealth;

import java.util.List;

public class StealthWODetailsWrapper {

	List<StealthWODetailItemWrapper> woDetailsList;

	public List<StealthWODetailItemWrapper> getWoDetailsList() {
		return woDetailsList;
	}

	public void setWoDetailsList(List<StealthWODetailItemWrapper> woDetailsList) {
		this.woDetailsList = woDetailsList;
	}

	@Override
	public String toString() {
		return "StealthWODetailsWrapper [woDetailsList=" + woDetailsList + "]";
	}

}

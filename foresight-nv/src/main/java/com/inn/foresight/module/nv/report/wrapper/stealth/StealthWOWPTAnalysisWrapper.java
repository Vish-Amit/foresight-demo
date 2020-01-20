package com.inn.foresight.module.nv.report.wrapper.stealth;

import java.util.List;

public class StealthWOWPTAnalysisWrapper {

	List<StealthWOWPTItemWrapper> wptAnalysisDataList;
	String daysCount;
	String avgTtl;
	String avgTtfb;
	String avgDns;
	String avgTotalDns;

	public List<StealthWOWPTItemWrapper> getWptAnalysisDataList() {
		return wptAnalysisDataList;
	}

	public void setWptAnalysisDataList(List<StealthWOWPTItemWrapper> wptAnalysisDataList) {
		this.wptAnalysisDataList = wptAnalysisDataList;
	}

	public String getDaysCount() {
		return daysCount;
	}

	public void setDaysCount(String daysCount) {
		this.daysCount = daysCount;
	}

	public String getAvgTtl() {
		return avgTtl;
	}

	public void setAvgTtl(String avgTtl) {
		this.avgTtl = avgTtl;
	}

	public String getAvgTtfb() {
		return avgTtfb;
	}

	public void setAvgTtfb(String avgTtfb) {
		this.avgTtfb = avgTtfb;
	}

	public String getAvgDns() {
		return avgDns;
	}

	public void setAvgDns(String avgDns) {
		this.avgDns = avgDns;
	}

	public String getAvgTotalDns() {
		return avgTotalDns;
	}

	public void setAvgTotalDns(String avgTotalDns) {
		this.avgTotalDns = avgTotalDns;
	}

	@Override
	public String toString() {
		return "StealthWOWPTAnalysisWrapper [wptAnalysisDataList=" + wptAnalysisDataList + ", daysCount=" + daysCount
				+ ", avgTtl=" + avgTtl + ", avgTtfb=" + avgTtfb + ", avgDns=" + avgDns + ", avgTotalDns=" + avgTotalDns
				+ "]";
	}

}

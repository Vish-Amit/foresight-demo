package com.inn.foresight.module.nv.report.wrapper.stealth;

import java.util.List;

public class StealthWOKpiAnalysisWrapper {

	List<StealthWOKpiItemWrapper> kpiAnalysisDataList;
	String daysCount;
	String avgRsrp;
	String avgRsrq;
	String avgSinr;
	String avgDl;
	String avgUl;

	public List<StealthWOKpiItemWrapper> getKpiAnalysisDataList() {
		return kpiAnalysisDataList;
	}

	public void setKpiAnalysisDataList(List<StealthWOKpiItemWrapper> kpiAnalysisDataList) {
		this.kpiAnalysisDataList = kpiAnalysisDataList;
	}

	public String getDaysCount() {
		return daysCount;
	}

	public void setDaysCount(String daysCount) {
		this.daysCount = daysCount;
	}

	public String getAvgRsrp() {
		return avgRsrp;
	}

	public void setAvgRsrp(String avgRsrp) {
		this.avgRsrp = avgRsrp;
	}

	public String getAvgRsrq() {
		return avgRsrq;
	}

	public void setAvgRsrq(String avgRsrq) {
		this.avgRsrq = avgRsrq;
	}

	public String getAvgSinr() {
		return avgSinr;
	}

	public void setAvgSinr(String avgSinr) {
		this.avgSinr = avgSinr;
	}

	public String getAvgDl() {
		return avgDl;
	}

	public void setAvgDl(String avgDl) {
		this.avgDl = avgDl;
	}

	public String getAvgUl() {
		return avgUl;
	}

	public void setAvgUl(String avgUl) {
		this.avgUl = avgUl;
	}

	@Override
	public String toString() {
		return "StealthWOKpiAnalysisWrapper [kpiAnalysisDataList=" + kpiAnalysisDataList + ", daysCount=" + daysCount
				+ ", avgRsrp=" + avgRsrp + ", avgRsrq=" + avgRsrq + ", avgSinr=" + avgSinr + ", avgDl=" + avgDl
				+ ", avgUl=" + avgUl + "]";
	}

}

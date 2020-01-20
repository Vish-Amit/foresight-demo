package com.inn.foresight.module.nv.report.wrapper;

import java.util.List;

public class BRTIReportWrapper {
	private String reportName;
	private List<BRTIReportSubWrapper> subList;

	public List<BRTIReportSubWrapper> getSubList() {
		return subList;
	}

	public void setSubList(List<BRTIReportSubWrapper> subList) {
		this.subList = subList;
	}


	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	@Override
	public String toString() {
		return "BRTIReportWrapper [reportName=" + reportName + ", subList=" + subList + "]";
	}

}

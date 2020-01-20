package com.inn.foresight.module.nv.report.wrapper.inbuilding.wifi;

import java.util.List;

public class NVIBWifiReportWrapper {

	List<NVIBWifiSubReportWrapper> reportDataList;

	public List<NVIBWifiSubReportWrapper> getReportDataList() {
		return reportDataList;
	}

	public void setReportDataList(List<NVIBWifiSubReportWrapper> reportDataList) {
		this.reportDataList = reportDataList;
	}

	@Override
	public String toString() {
		return "NVIBWifiReportWrapper [reportDataList=" + reportDataList + "]";
	}

}
package com.inn.foresight.module.nv.report.smf.wrapper;

import java.util.List;

public class SmfWrapper {
	
	List<SmfWidgetWrapper> reportData;
	Integer analyticsrepositoryid_pk;

	public Integer getAnalyticsrepositoryid_pk() {
		return analyticsrepositoryid_pk;
	}

	public void setAnalyticsrepositoryid_pk(Integer analyticsrepositoryid_pk) {
		this.analyticsrepositoryid_pk = analyticsrepositoryid_pk;
	}

	public List<SmfWidgetWrapper> getReportData() {
		return reportData;
	}

	public void setReportData(List<SmfWidgetWrapper> reportData) {
		this.reportData = reportData;
	}

	@Override
	public String toString() {
		return "SmfWrapper [reportData=" + reportData + ", analyticsrepositoryid_pk=" + analyticsrepositoryid_pk + "]";
	}

	
	

}

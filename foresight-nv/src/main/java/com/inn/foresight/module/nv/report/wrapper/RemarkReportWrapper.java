package com.inn.foresight.module.nv.report.wrapper;

import java.util.List;

public class RemarkReportWrapper {
    private List<ReportSubWrapper>remarkDataList;
    private String isremarkData;
    private String istestSkip;
	private String siteName;
	private String testDate;

	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public String getTestDate() {
		return testDate;
	}
	public void setTestDate(String testDate) {
		this.testDate = testDate;
	}
	public List<ReportSubWrapper> getRemarkDataList() {
		return remarkDataList;
	}
	public void setRemarkDataList(List<ReportSubWrapper> remarkDataList) {
		this.remarkDataList = remarkDataList;
	}
	public String getIsremarkData() {
		return isremarkData;
	}
	public void setIsremarkData(String isremarkData) {
		this.isremarkData = isremarkData;
	}
	public String getIstestSkip() {
		return istestSkip;
	}
	public void setIstestSkip(String istestSkip) {
		this.istestSkip = istestSkip;
	}
	
	@Override
	public String toString() {
		return "RemarkReportWrapper [remarkDataList=" + remarkDataList + ", isremarkData=" + isremarkData
				+ ", istestSkip=" + istestSkip + ", siteName=" + siteName + ", testDate=" + testDate + "]";
	}
    
}

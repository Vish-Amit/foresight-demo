package com.inn.foresight.module.nv.report.wrapper;

import java.util.List;

public class SMFReportWrapper {
    private List<ReportSubWrapper>subList;
	private String reportName;
	private String operatorName1;
	private String operatorName2;
	
	private boolean isMobilityDataAvailable;
	private boolean isStationaryDataAvailable;
	
	public boolean isMobilityDataAvailable() {
		return isMobilityDataAvailable;
	}
	public void setMobilityDataAvailable(boolean isMobilityDataAvailable) {
		this.isMobilityDataAvailable = isMobilityDataAvailable;
	}
	public boolean isStationaryDataAvailable() {
		return isStationaryDataAvailable;
	}
	public void setStationaryDataAvailable(boolean isStationaryDataAvailable) {
		this.isStationaryDataAvailable = isStationaryDataAvailable;
	}
	public List<ReportSubWrapper> getSubList() {
		return subList;
	}
	public void setSubList(List<ReportSubWrapper> subList) {
		this.subList = subList;
	}
	public String getReportName() {
		return reportName;
	}
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	
	public String getOperatorName1() {
		return operatorName1;
	}
	public void setOperatorName1(String operatorName1) {
		this.operatorName1 = operatorName1;
	}
	public String getOperatorName2() {
		return operatorName2;
	}
	public void setOperatorName2(String operatorName2) {
		this.operatorName2 = operatorName2;
	}
	
	@Override
	public String toString() {
		return "SMFReportWrapper [subList=" + subList + ", reportName=" + reportName + ", operatorName1="
				+ operatorName1 + ", operatorName2=" + operatorName2 + "]";
	}
	
}

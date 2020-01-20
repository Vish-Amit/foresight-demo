package com.inn.foresight.module.nv.reportgeneration.wrapper;

import java.util.Arrays;

import com.inn.core.generic.wrapper.RestWrapper;

@RestWrapper
public class NVReportWrapper {
	
	private String reportName;
	private String reportType;
	private byte[] file;
	public String getReportName() {
		return reportName;
	}
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	public String getReportType() {
		return reportType;
	}
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	public byte[] getFile() {
		return file;
	}
	public void setFile(byte[] file) {
		this.file = file;
	}
	@Override
	public String toString() {
		return "ReportWrapper [reportName=" + reportName + ", reportType=" + reportType + ", file="
				+ Arrays.toString(file) + "]";
	}

}

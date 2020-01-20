package com.inn.foresight.module.nv.report.wrapper.clot;

import java.util.List;

import com.inn.foresight.module.nv.report.wrapper.PSDataWrapper;
import com.inn.foresight.module.nv.report.wrapper.ReportSubWrapper;

public class ClotReportWrapper {
	private String startDate;
	private String endDate;
	private String fileName;
    private List<ClotReportSubWrapper>subWrapperList;
    private List<ReportSubWrapper>remarkDataList;
    private String isremarkData;
    private String istestSkip;
    private List<PSDataWrapper> listPsDataWrapper;


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
 
public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

public String getStartDate() {
	return startDate;
}

public void setStartDate(String startDate) {
	this.startDate = startDate;
}

public String getEndDate() {
	return endDate;
}

public void setEndDate(String endDate) {
	this.endDate = endDate;
}

public List<ClotReportSubWrapper> getSubWrapperList() {
	return subWrapperList;
}

public void setSubWrapperList(List<ClotReportSubWrapper> subWrapperList) {
	this.subWrapperList = subWrapperList;
}

public List<PSDataWrapper> getListPsDataWrapper() {
	return listPsDataWrapper;
}

public void setListPsDataWrapper(List<PSDataWrapper> listPsDataWrapper) {
	this.listPsDataWrapper = listPsDataWrapper;
}

@Override
public String toString() {
	return "ClotReportWrapper [startDate=" + startDate + ", endDate=" + endDate + ", fileName=" + fileName
			+ ", subWrapperList=" + subWrapperList + ", remarkDataList=" + remarkDataList + ", isremarkData="
			+ isremarkData + ", istestSkip=" + istestSkip + ", listPsDataWrapper=" + listPsDataWrapper + "]";
}

}

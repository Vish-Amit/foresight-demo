package com.inn.foresight.module.nv.report.wrapper;

import java.util.List;

import com.inn.foresight.module.nv.dashboard.model.NVDashboard;

public class NvReportWrapper {
	private List<NVDashboardReportWrapper> nvDashboardReportWrapperList;

	private List<NVDashboard> dashboardDataList;

	private List<NVDashboardPlotWrapper> parentGoogleMapPlotPath;

	private List<NVDashboardPlotWrapper> childGoogleMapPlotPath;

	public List<NVDashboardReportWrapper> getNvDashboardReportWrapperList() {
		return nvDashboardReportWrapperList;
	}

	public void setNvDashboardReportWrapperList(List<NVDashboardReportWrapper> nvDashboardReportWrapperList) {
		this.nvDashboardReportWrapperList = nvDashboardReportWrapperList;
	}

	public List<NVDashboard> getDashboardDataList() {
		return dashboardDataList;
	}

	public void setDashboardDataList(List<NVDashboard> dashboardDataList) {
		this.dashboardDataList = dashboardDataList;
	}

	public List<NVDashboardPlotWrapper> getParentGoogleMapPlotPath() {
		return parentGoogleMapPlotPath;
	}

	public void setParentGoogleMapPlotPath(List<NVDashboardPlotWrapper> parentGoogleMapPlotPath) {
		this.parentGoogleMapPlotPath = parentGoogleMapPlotPath;
	}

	public List<NVDashboardPlotWrapper> getChildGoogleMapPlotPath() {
		return childGoogleMapPlotPath;
	}

	public void setChildGoogleMapPlotPath(List<NVDashboardPlotWrapper> childGoogleMapPlotPath) {
		this.childGoogleMapPlotPath = childGoogleMapPlotPath;
	}

	@Override
	public String toString() {
		return "NvReportWrapper [nvDashboardReportWrapperList=" + nvDashboardReportWrapperList + ", dashboardDataList="
				+ dashboardDataList + ", parentGoogleMapPlotPath=" + parentGoogleMapPlotPath
				+ ", childGoogleMapPlotPath=" + childGoogleMapPlotPath + "]";
	}



}

package com.inn.foresight.module.nv.report.wrapper.stealth;

import java.util.List;

import com.inn.foresight.module.nv.report.wrapper.GraphDataWrapper;

public class StealthWOCoverageGraphDataWrapper {

	String pageTitle;
	List<GraphDataWrapper> graphDataList;
	String kpiName;
	
	public String getPageTitle() {
		return pageTitle;
	}
	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}
	public List<GraphDataWrapper> getGraphDataList() {
		return graphDataList;
	}
	public void setGraphDataList(List<GraphDataWrapper> graphDataList) {
		this.graphDataList = graphDataList;
	}
	public String getKpiName() {
		return kpiName;
	}
	public void setKpiName(String kpiName) {
		this.kpiName = kpiName;
	}
	
	@Override
	public String toString() {
		return "StealthWOCoverageGraphDataWrapper [pageTitle=" + pageTitle + ", graphDataList=" + graphDataList
				+ ", kpiName=" + kpiName + "]";
	}
	
	
	

}

package com.inn.foresight.module.nv.report.stealthdashboard.wrapper;

import java.util.List;

public class StealthDashboardReportWrapper {

	List<StealthDashboardSummaryWrapper> summaryDataList;
	List<SDGraphMapDataWrapper> graphDataList;
	List<SDGraphMapDataWrapper> criteriaGraphDataList;
	List<SDGraphMapDataWrapper> mapPlotList;

	public List<StealthDashboardSummaryWrapper> getSummaryDataList() {
		return summaryDataList;
	}

	public void setSummaryDataList(List<StealthDashboardSummaryWrapper> summaryDataList) {
		this.summaryDataList = summaryDataList;
	}

	public List<SDGraphMapDataWrapper> getGraphDataList() {
		return graphDataList;
	}

	public void setGraphDataList(List<SDGraphMapDataWrapper> graphDataList) {
		this.graphDataList = graphDataList;
	}

	public List<SDGraphMapDataWrapper> getMapPlotList() {
		return mapPlotList;
	}

	public void setMapPlotList(List<SDGraphMapDataWrapper> mapPlotList) {
		this.mapPlotList = mapPlotList;
	}

	public List<SDGraphMapDataWrapper> getCriteriaGraphDataList() {
		return criteriaGraphDataList;
	}

	public void setCriteriaGraphDataList(List<SDGraphMapDataWrapper> criteriaGraphDataList) {
		this.criteriaGraphDataList = criteriaGraphDataList;
	}
}

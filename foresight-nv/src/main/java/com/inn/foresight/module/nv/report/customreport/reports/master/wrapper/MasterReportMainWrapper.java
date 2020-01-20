package com.inn.foresight.module.nv.report.customreport.reports.master.wrapper;

import java.util.List;

import com.inn.core.generic.wrapper.JpaWrapper;
import com.inn.foresight.module.nv.report.customreport.ssvt.wrapper.HandoverDataWrapper;

@JpaWrapper
public class MasterReportMainWrapper {

	List<MasterSummaryWrapper> summaryDataList;

	List<MasterPlotDataWrapper> stationaryMapList;

	List<MasterGraphPlotContainerWrapper> graphPlotDataList;

	List<HandoverDataWrapper> handoverDataList;

	List<MasterPlotDataWrapper> reselectionDataList;
	
	List<VolteCallDataWrapper>volteCallSummaryList;

	public List<MasterSummaryWrapper> getSummaryDataList() {
		return summaryDataList;
	}

	public void setSummaryDataList(List<MasterSummaryWrapper> summaryDataList) {
		this.summaryDataList = summaryDataList;
	}

	public List<MasterPlotDataWrapper> getStationaryMapList() {
		return stationaryMapList;
	}

	public void setStationaryMapList(List<MasterPlotDataWrapper> stationaryMapList) {
		this.stationaryMapList = stationaryMapList;
	}

	public List<MasterGraphPlotContainerWrapper> getGraphPlotDataList() {
		return graphPlotDataList;
	}

	public void setGraphPlotDataList(List<MasterGraphPlotContainerWrapper> graphPlotDataList) {
		this.graphPlotDataList = graphPlotDataList;
	}

	public List<HandoverDataWrapper> getHandoverDataList() {
		return handoverDataList;
	}

	public void setHandoverDataList(List<HandoverDataWrapper> handoverDataList) {
		this.handoverDataList = handoverDataList;
	}

	public List<MasterPlotDataWrapper> getReselectionDataList() {
		return reselectionDataList;
	}

	public void setReselectionDataList(List<MasterPlotDataWrapper> reselectionDataList) {
		this.reselectionDataList = reselectionDataList;
	}

	public List<VolteCallDataWrapper> getVolteCallSummaryList() {
		return volteCallSummaryList;
	}

	public void setVolteCallSummaryList(List<VolteCallDataWrapper> volteCallSummaryList) {
		this.volteCallSummaryList = volteCallSummaryList;
	}

	@Override
	public String toString() {
		return "MasterReportMainWrapper [summaryDataList=" + summaryDataList + ", stationaryMapList="
				+ stationaryMapList + ", graphPlotDataList=" + graphPlotDataList + ", handoverDataList="
				+ handoverDataList + ", reselectionDataList=" + reselectionDataList + ", volteCallSummaryList="
				+ volteCallSummaryList + "]";
	}

}

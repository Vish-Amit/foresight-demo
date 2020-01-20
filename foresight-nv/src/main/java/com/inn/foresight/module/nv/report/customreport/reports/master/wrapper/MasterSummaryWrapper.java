package com.inn.foresight.module.nv.report.customreport.reports.master.wrapper;

import java.util.List;

import com.inn.core.generic.wrapper.JpaWrapper;

@JpaWrapper
public class MasterSummaryWrapper {

	List<MasterSummaryDataWrapper> stationaryDataList;
	List<MasterSummaryDataWrapper> mobilityDataList;

	public List<MasterSummaryDataWrapper> getStationaryDataList() {
		return stationaryDataList;
	}

	public void setStationaryDataList(List<MasterSummaryDataWrapper> stationaryDataList) {
		this.stationaryDataList = stationaryDataList;
	}

	public List<MasterSummaryDataWrapper> getMobilityDataList() {
		return mobilityDataList;
	}

	public void setMobilityDataList(List<MasterSummaryDataWrapper> mobilityDataList) {
		this.mobilityDataList = mobilityDataList;
	}

	@Override public String toString() {
		return "MasterSummaryWrapper{" + "stationaryDataList=" + stationaryDataList + ", mobilityDataList="
				+ mobilityDataList + '}';
	}
}

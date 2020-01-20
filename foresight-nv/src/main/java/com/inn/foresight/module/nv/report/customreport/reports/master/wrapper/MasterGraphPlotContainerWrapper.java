package com.inn.foresight.module.nv.report.customreport.reports.master.wrapper;

import java.util.List;

import com.inn.core.generic.wrapper.JpaWrapper;

@JpaWrapper
public class MasterGraphPlotContainerWrapper {

	List<MasterPlotDataWrapper> mobilityMapList;
	List<MasterPlotDataWrapper> stationaryGraphList;
	List<MasterPlotDataWrapper> mobilityGraphList;

	public List<MasterPlotDataWrapper> getStationaryGraphList() {
		return stationaryGraphList;
	}

	public void setStationaryGraphList(List<MasterPlotDataWrapper> stationaryGraphList) {
		this.stationaryGraphList = stationaryGraphList;
	}

	public List<MasterPlotDataWrapper> getMobilityGraphList() {
		return mobilityGraphList;
	}

	public void setMobilityGraphList(List<MasterPlotDataWrapper> mobilityGraphList) {
		this.mobilityGraphList = mobilityGraphList;
	}

	public List<MasterPlotDataWrapper> getMobilityMapList() {
		return mobilityMapList;
	}

	public void setMobilityMapList(List<MasterPlotDataWrapper> mobilityMapList) {
		this.mobilityMapList = mobilityMapList;
	}

	@Override public String toString() {
		return "MasterGraphPlotContainerWrapper{" + "mobilityMapList=" + mobilityMapList + ", stationaryGraphList="
				+ stationaryGraphList + ", mobilityGraphList=" + mobilityGraphList + '}';
	}
}

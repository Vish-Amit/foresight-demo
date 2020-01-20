package com.inn.foresight.module.nv.report.customreport.reports.master.wrapper;

import java.util.List;

import com.inn.core.generic.wrapper.JpaWrapper;

@JpaWrapper
public class MasterPlotDataWrapper {

	String plotTitle;
	String mapPlot;
	String legendImage;
	
	List<GraphPlotDataWrapper> kpiPlotList;

	public String getPlotTitle() {
		return plotTitle;
	}

	public void setPlotTitle(String plotTitle) {
		this.plotTitle = plotTitle;
	}

	public String getMapPlot() {
		return mapPlot;
	}

	public void setMapPlot(String mapPlot) {
		this.mapPlot = mapPlot;
	}

	public String getLegendImage() {
		return legendImage;
	}

	public void setLegendImage(String legendImage) {
		this.legendImage = legendImage;
	}

	public List<GraphPlotDataWrapper> getKpiPlotList() {
		return kpiPlotList;
	}

	public void setKpiPlotList(List<GraphPlotDataWrapper> kpiPlotList) {
		this.kpiPlotList = kpiPlotList;
	}

	@Override public String toString() {
		return "MasterPlotDataWrapper{" + "plotTitle='" + plotTitle + '\'' + ", mapPlot='" + mapPlot + '\''
				+ ", legendImage='" + legendImage + '\'' + ", kpiPlotList=" + kpiPlotList + '}';
	}
}

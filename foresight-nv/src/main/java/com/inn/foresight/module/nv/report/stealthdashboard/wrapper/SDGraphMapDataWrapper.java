package com.inn.foresight.module.nv.report.stealthdashboard.wrapper;

import java.util.List;

public class SDGraphMapDataWrapper {

	List<SDChartDataWrapper> chart1List;
	List<SDChartDataWrapper> chart2List;
	String geographyName;
	Double avgRsrp;
	Double avgSinr;
	Double avgDl;
	Double avgUl;
	String coverageMapPlot;
	String throughputMapPlot;
	String coverageMapLegend;
	String throughputMapLegend;
	List<SDGeographyDataWrapper> topCoverageList;
	List<SDGeographyDataWrapper> worstCoverageList;
	List<SDGeographyDataWrapper> coverageNCList;
	List<SDGeographyDataWrapper> topDlList;
	List<SDGeographyDataWrapper> worstDlList;
	String geographyNames;

	List<SDGraphMapDataWrapper> mapPlotDataList;

	public List<SDChartDataWrapper> getChart1List() {
		return chart1List;
	}

	public void setChart1List(List<SDChartDataWrapper> chart1List) {
		this.chart1List = chart1List;
	}

	public List<SDChartDataWrapper> getChart2List() {
		return chart2List;
	}

	public void setChart2List(List<SDChartDataWrapper> chart2List) {
		this.chart2List = chart2List;
	}

	public String getGeographyName() {
		return geographyName;
	}

	public void setGeographyName(String geographyName) {
		this.geographyName = geographyName;
	}

	public Double getAvgRsrp() {
		return avgRsrp;
	}

	public void setAvgRsrp(Double avgRsrp) {
		this.avgRsrp = avgRsrp;
	}

	public Double getAvgSinr() {
		return avgSinr;
	}

	public void setAvgSinr(Double avgSinr) {
		this.avgSinr = avgSinr;
	}

	public Double getAvgDl() {
		return avgDl;
	}

	public void setAvgDl(Double avgDl) {
		this.avgDl = avgDl;
	}

	public Double getAvgUl() {
		return avgUl;
	}

	public void setAvgUl(Double avgUl) {
		this.avgUl = avgUl;
	}

	public List<SDGeographyDataWrapper> getTopCoverageList() {
		return topCoverageList;
	}

	public void setTopCoverageList(List<SDGeographyDataWrapper> topCoverageList) {
		this.topCoverageList = topCoverageList;
	}

	public List<SDGeographyDataWrapper> getWorstCoverageList() {
		return worstCoverageList;
	}

	public void setWorstCoverageList(List<SDGeographyDataWrapper> worstCoverageList) {
		this.worstCoverageList = worstCoverageList;
	}

	public List<SDGeographyDataWrapper> getCoverageNCList() {
		return coverageNCList;
	}

	public void setCoverageNCList(List<SDGeographyDataWrapper> coverageNCList) {
		this.coverageNCList = coverageNCList;
	}

	public List<SDGeographyDataWrapper> getTopDlList() {
		return topDlList;
	}

	public void setTopDlList(List<SDGeographyDataWrapper> topDlList) {
		this.topDlList = topDlList;
	}

	public List<SDGeographyDataWrapper> getWorstDlList() {
		return worstDlList;
	}

	public void setWorstDlList(List<SDGeographyDataWrapper> worstDlList) {
		this.worstDlList = worstDlList;
	}

	public String getCoverageMapPlot() {
		return coverageMapPlot;
	}

	public void setCoverageMapPlot(String coverageMapPlot) {
		this.coverageMapPlot = coverageMapPlot;
	}

	public String getThroughputMapPlot() {
		return throughputMapPlot;
	}

	public void setThroughputMapPlot(String throughputMapPlot) {
		this.throughputMapPlot = throughputMapPlot;
	}

	public List<SDGraphMapDataWrapper> getMapPlotDataList() {
		return mapPlotDataList;
	}

	public void setMapPlotDataList(List<SDGraphMapDataWrapper> mapPlotDataList) {
		this.mapPlotDataList = mapPlotDataList;
	}

	public String getCoverageMapLegend() {
		return coverageMapLegend;
	}

	public void setCoverageMapLegend(String coverageMapLegend) {
		this.coverageMapLegend = coverageMapLegend;
	}

	public String getThroughputMapLegend() {
		return throughputMapLegend;
	}

	public void setThroughputMapLegend(String throughputMapLegend) {
		this.throughputMapLegend = throughputMapLegend;
	}

	public String getGeographyNames() {
		return geographyNames;
	}

	public void setGeographyNames(String geographyNames) {
		this.geographyNames = geographyNames;
	}
}

package com.inn.foresight.module.nv.report.wrapper.inbuilding.benchmark;

import java.util.List;

import com.inn.foresight.module.nv.report.wrapper.inbuilding.NoAccessWrapper;

public class IBBenchMarkReportWrapper {
	
	List<IBBenchmarkImagePlotWrapper> rsrpPlotList;
	List<IBBenchmarkGraphPlotWrapper> rsrpGraphList;
	List<IBBenchmarkImagePlotWrapper> sinrPlotList;
	List<IBBenchmarkGraphPlotWrapper> sinrGraphList;
	List<IBBenchmarkImagePlotWrapper> dlPlotList;
	List<IBBenchmarkGraphPlotWrapper> dlGraphList;
	List<IBBenchmarkImagePlotWrapper> ulPlotList;
	List<IBBenchmarkGraphPlotWrapper> ulGraphList;
	List<IBBenchmarkImagePlotWrapper> pciPlotList;
	List<IBBenchmarkImagePlotWrapper> earfcnPlotList;
	List<IBBenchmarkImagePlotWrapper> cqiPlotList;
	List<IBBenchmarkImagePlotWrapper> riPlotList;
	List<IBBenchmarkSummaryWrapper> summaryWrapperList;
	List<IBBenchmarkOverallDataWrapper> overallScoreList;
	List<IBBenchmarkKPIAnalysisWrapper> kpiAnalysisList;
	List<NoAccessWrapper> noAccessimglist;
	
	public List<IBBenchmarkKPIAnalysisWrapper> getKpiAnalysisList() {
		return kpiAnalysisList;
	}
	public void setKpiAnalysisList(List<IBBenchmarkKPIAnalysisWrapper> kpiAnalysisList) {
		this.kpiAnalysisList = kpiAnalysisList;
	}
	public List<IBBenchmarkImagePlotWrapper> getRsrpPlotList() {
		return rsrpPlotList;
	}
	public void setRsrpPlotList(List<IBBenchmarkImagePlotWrapper> rsrpPlotList) {
		this.rsrpPlotList = rsrpPlotList;
	}
	public List<IBBenchmarkGraphPlotWrapper> getRsrpGraphList() {
		return rsrpGraphList;
	}
	public void setRsrpGraphList(List<IBBenchmarkGraphPlotWrapper> rsrpGraphList) {
		this.rsrpGraphList = rsrpGraphList;
	}
	public List<IBBenchmarkImagePlotWrapper> getSinrPlotList() {
		return sinrPlotList;
	}
	public void setSinrPlotList(List<IBBenchmarkImagePlotWrapper> sinrPlotList) {
		this.sinrPlotList = sinrPlotList;
	}
	public List<IBBenchmarkGraphPlotWrapper> getSinrGraphList() {
		return sinrGraphList;
	}
	public void setSinrGraphList(List<IBBenchmarkGraphPlotWrapper> sinrGraphList) {
		this.sinrGraphList = sinrGraphList;
	}
	public List<IBBenchmarkImagePlotWrapper> getPciPlotList() {
		return pciPlotList;
	}
	public void setPciPlotList(List<IBBenchmarkImagePlotWrapper> pciPlotList) {
		this.pciPlotList = pciPlotList;
	}
	public List<IBBenchmarkImagePlotWrapper> getEarfcnPlotList() {
		return earfcnPlotList;
	}
	public void setEarfcnPlotList(List<IBBenchmarkImagePlotWrapper> earfcnPlotList) {
		this.earfcnPlotList = earfcnPlotList;
	}
	public List<IBBenchmarkImagePlotWrapper> getCqiPlotList() {
		return cqiPlotList;
	}
	public void setCqiPlotList(List<IBBenchmarkImagePlotWrapper> cqiPlotList) {
		this.cqiPlotList = cqiPlotList;
	}
	public List<IBBenchmarkImagePlotWrapper> getRiPlotList() {
		return riPlotList;
	}
	public void setRiPlotList(List<IBBenchmarkImagePlotWrapper> riPlotList) {
		this.riPlotList = riPlotList;
	}
	public List<IBBenchmarkSummaryWrapper> getSummaryWrapperList() {
		return summaryWrapperList;
	}
	public void setSummaryWrapperList(List<IBBenchmarkSummaryWrapper> summaryWrapperList) {
		this.summaryWrapperList = summaryWrapperList;
	}
	public List<IBBenchmarkOverallDataWrapper> getOverallScoreList() {
		return overallScoreList;
	}
	public void setOverallScoreList(List<IBBenchmarkOverallDataWrapper> overallScoreList) {
		this.overallScoreList = overallScoreList;
	}
	public List<IBBenchmarkImagePlotWrapper> getDlPlotList() {
		return dlPlotList;
	}
	public void setDlPlotList(List<IBBenchmarkImagePlotWrapper> dlPlotList) {
		this.dlPlotList = dlPlotList;
	}
	public List<IBBenchmarkGraphPlotWrapper> getDlGraphList() {
		return dlGraphList;
	}
	public void setDlGraphList(List<IBBenchmarkGraphPlotWrapper> dlGraphList) {
		this.dlGraphList = dlGraphList;
	}
	public List<IBBenchmarkImagePlotWrapper> getUlPlotList() {
		return ulPlotList;
	}
	public void setUlPlotList(List<IBBenchmarkImagePlotWrapper> ulPlotList) {
		this.ulPlotList = ulPlotList;
	}
	public List<IBBenchmarkGraphPlotWrapper> getUlGraphList() {
		return ulGraphList;
	}
	public void setUlGraphList(List<IBBenchmarkGraphPlotWrapper> ulGraphList) {
		this.ulGraphList = ulGraphList;
	}
	public List<NoAccessWrapper> getNoAccessimglist() {
		return noAccessimglist;
	}
	public void setNoAccessimglist(List<NoAccessWrapper> noAccessimglist) {
		this.noAccessimglist = noAccessimglist;
	}
	
}
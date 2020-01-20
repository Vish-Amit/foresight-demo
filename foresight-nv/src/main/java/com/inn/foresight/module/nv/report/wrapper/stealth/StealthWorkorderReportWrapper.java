package com.inn.foresight.module.nv.report.wrapper.stealth;

import java.util.List;

public class StealthWorkorderReportWrapper {

	List<StealthWODetailsWrapper> woDetails;

	List<StealthWOSummaryWrapper> woSummary;

	List<StealthWOCoverageAnalysisWrapper> coverageDataAnalysis;

	List<StealthWOWPTAnalysisWrapper> wptDataAnalysisList;

	List<StealthWOYoutubeAnalysisWrapper> youtubeDataAnalysisList;

	List<StealthWOBestWorstDataWrapper> bestWorstDataList;

	List<StealthWOPingAnalysisWrapper> pingDataAnalysisList;

	List<StealthWOKpiAnalysisWrapper> kpiDataAnalysisList;

	public List<StealthWODetailsWrapper> getWoDetails() {
		return woDetails;
	}

	public void setWoDetails(List<StealthWODetailsWrapper> woDetails) {
		this.woDetails = woDetails;
	}

	public List<StealthWOSummaryWrapper> getWoSummary() {
		return woSummary;
	}

	public void setWoSummary(List<StealthWOSummaryWrapper> woSummary) {
		this.woSummary = woSummary;
	}

	public List<StealthWOCoverageAnalysisWrapper> getCoverageDataAnalysis() {
		return coverageDataAnalysis;
	}

	public void setCoverageDataAnalysis(List<StealthWOCoverageAnalysisWrapper> coverageDataAnalysis) {
		this.coverageDataAnalysis = coverageDataAnalysis;
	}

	public List<StealthWOWPTAnalysisWrapper> getWptDataAnalysisList() {
		return wptDataAnalysisList;
	}

	public void setWptDataAnalysisList(List<StealthWOWPTAnalysisWrapper> wptDataAnalysisList) {
		this.wptDataAnalysisList = wptDataAnalysisList;
	}

	public List<StealthWOYoutubeAnalysisWrapper> getYoutubeDataAnalysisList() {
		return youtubeDataAnalysisList;
	}

	public void setYoutubeDataAnalysisList(List<StealthWOYoutubeAnalysisWrapper> youtubeDataAnalysisList) {
		this.youtubeDataAnalysisList = youtubeDataAnalysisList;
	}

	public List<StealthWOBestWorstDataWrapper> getBestWorstDataList() {
		return bestWorstDataList;
	}

	public void setBestWorstDataList(List<StealthWOBestWorstDataWrapper> bestWorstDataList) {
		this.bestWorstDataList = bestWorstDataList;
	}

	public List<StealthWOPingAnalysisWrapper> getPingDataAnalysisList() {
		return pingDataAnalysisList;
	}

	public void setPingDataAnalysisList(List<StealthWOPingAnalysisWrapper> pingDataAnalysisList) {
		this.pingDataAnalysisList = pingDataAnalysisList;
	}

	public List<StealthWOKpiAnalysisWrapper> getKpiDataAnalysisList() {
		return kpiDataAnalysisList;
	}

	public void setKpiDataAnalysisList(List<StealthWOKpiAnalysisWrapper> kpiDataAnalysisList) {
		this.kpiDataAnalysisList = kpiDataAnalysisList;
	}

	@Override
	public String toString() {
		return "StealthWorkorderReportWrapper [woDetails=" + woDetails + ", woSummary=" + woSummary
				+ ", coverageDataAnalysis=" + coverageDataAnalysis + ", wptDataAnalysisList=" + wptDataAnalysisList
				+ ", youtubeDataAnalysisList=" + youtubeDataAnalysisList + ", bestWorstDataList=" + bestWorstDataList
				+ ", pingDataAnalysisList=" + pingDataAnalysisList + ", kpiDataAnalysisList=" + kpiDataAnalysisList
				+ "]";
	}

}

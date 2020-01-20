package com.inn.foresight.module.nv.report.wrapper.stealth;

import java.util.List;

public class StealthWOCoverageAnalysisWrapper {

	List<StealthWOCoverageGraphDataWrapper> coverageDataList;

	public List<StealthWOCoverageGraphDataWrapper> getCoverageDataList() {
		return coverageDataList;
	}

	public void setCoverageDataList(List<StealthWOCoverageGraphDataWrapper> coverageDataList) {
		this.coverageDataList = coverageDataList;
	}

	@Override
	public String toString() {
		return "StealthWOCoverageAnalysisWrapper [coverageDataList=" + coverageDataList + "]";
	}

}

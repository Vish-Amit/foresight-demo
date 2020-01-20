package com.inn.foresight.module.nv.report.wrapper.stealth;

import java.util.List;

public class StealthWOBestWorstDataWrapper {

	List<StealthWOBestWorstItemWrapper> topDataList;

	List<StealthWOBestWorstItemWrapper> bottomDataList;

	public List<StealthWOBestWorstItemWrapper> getTopDataList() {
		return topDataList;
	}

	public void setTopDataList(List<StealthWOBestWorstItemWrapper> topDataList) {
		this.topDataList = topDataList;
	}

	public List<StealthWOBestWorstItemWrapper> getBottomDataList() {
		return bottomDataList;
	}

	public void setBottomDataList(List<StealthWOBestWorstItemWrapper> bottomDataList) {
		this.bottomDataList = bottomDataList;
	}

	@Override
	public String toString() {
		return "StealthWOBestWorstDataWrapper [topDataList=" + topDataList + ", bottomDataList=" + bottomDataList + "]";
	}

}

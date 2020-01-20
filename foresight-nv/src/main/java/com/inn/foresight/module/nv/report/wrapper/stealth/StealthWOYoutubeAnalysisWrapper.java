package com.inn.foresight.module.nv.report.wrapper.stealth;

import java.util.List;

public class StealthWOYoutubeAnalysisWrapper {

	List<StealthWOYoutubeItemWrapper> youtubeDataAnalysisList;
	String daysCount;
	String avgStalling;
	String avgLoadTime;
	String avgBuffering;
	String avgFreezingRatio;

	public List<StealthWOYoutubeItemWrapper> getYoutubeDataAnalysisList() {
		return youtubeDataAnalysisList;
	}

	public void setYoutubeDataAnalysisList(List<StealthWOYoutubeItemWrapper> youtubeDataAnalysisList) {
		this.youtubeDataAnalysisList = youtubeDataAnalysisList;
	}

	public String getDaysCount() {
		return daysCount;
	}

	public void setDaysCount(String daysCount) {
		this.daysCount = daysCount;
	}

	public String getAvgStalling() {
		return avgStalling;
	}

	public void setAvgStalling(String avgStalling) {
		this.avgStalling = avgStalling;
	}

	public String getAvgLoadTime() {
		return avgLoadTime;
	}

	public void setAvgLoadTime(String avgLoadTime) {
		this.avgLoadTime = avgLoadTime;
	}

	public String getAvgBuffering() {
		return avgBuffering;
	}

	public void setAvgBuffering(String avgBuffering) {
		this.avgBuffering = avgBuffering;
	}

	public String getAvgFreezingRatio() {
		return avgFreezingRatio;
	}

	public void setAvgFreezingRatio(String avgFreezingRatio) {
		this.avgFreezingRatio = avgFreezingRatio;
	}

	@Override
	public String toString() {
		return "StealthWOYoutubeAnalysisWrapper [youtubeDataAnalysisList=" + youtubeDataAnalysisList + ", daysCount="
				+ daysCount + ", avgStalling=" + avgStalling + ", avgLoadTime=" + avgLoadTime + ", avgbuffering="
				+ avgBuffering + ", avgFreezingRatio=" + avgFreezingRatio + "]";
	}

}

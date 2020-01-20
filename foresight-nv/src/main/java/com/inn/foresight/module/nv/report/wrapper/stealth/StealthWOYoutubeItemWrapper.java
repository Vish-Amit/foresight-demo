package com.inn.foresight.module.nv.report.wrapper.stealth;

public class StealthWOYoutubeItemWrapper {

	String date;
	String stalling;
	String buffering;
	String loadTime;
	String freezingRatio;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getStalling() {
		return stalling;
	}

	public void setStalling(String stalling) {
		this.stalling = stalling;
	}

	public String getBuffering() {
		return buffering;
	}

	public void setBuffering(String buffering) {
		this.buffering = buffering;
	}

	public String getLoadTime() {
		return loadTime;
	}

	public void setLoadTime(String loadTime) {
		this.loadTime = loadTime;
	}

	public String getFreezingRatio() {
		return freezingRatio;
	}

	public void setFreezingRatio(String freezingRatio) {
		this.freezingRatio = freezingRatio;
	}

	@Override
	public String toString() {
		return "StealthWOYoutubeItemWrapper [date=" + date + ", stalling=" + stalling + ", buffering=" + buffering
				+ ", loadTime=" + loadTime + ", freezingRatio=" + freezingRatio + "]";
	}

}

package com.inn.foresight.module.nv.report.wrapper.stealth;

public class StealthWOMapDataWrapper {

	Double latitude;
	Double longitude;
	String scoreImagePath;

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getScoreImagePath() {
		return scoreImagePath;
	}

	public void setScoreImagePath(String scoreImagePath) {
		this.scoreImagePath = scoreImagePath;
	}

	@Override
	public String toString() {
		return "StealthWOMapDataWrapper [latitude=" + latitude + ", longitude=" + longitude + ", scoreImagePath="
				+ scoreImagePath + "]";
	}

}

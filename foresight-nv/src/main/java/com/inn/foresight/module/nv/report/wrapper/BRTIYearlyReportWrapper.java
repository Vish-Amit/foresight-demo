package com.inn.foresight.module.nv.report.wrapper;

import java.io.InputStream;
import java.util.List;

public class BRTIYearlyReportWrapper {
	private String cityName;
	List<BRTIExcelReportWrapper> driveSummaryList;
    List<BRTIStaionaryDataWrapper>stationaryList;
	private InputStream voiceRsrpImg;
	private InputStream mobileRsrpImg;
	private InputStream voiceRsrpLegendImg;
	private InputStream mobileRsrpImgLegendImg;
	public InputStream getVoiceRsrpImg() {
		return voiceRsrpImg;
	}
	public void setVoiceRsrpImg(InputStream voiceRsrpImg) {
		this.voiceRsrpImg = voiceRsrpImg;
	}
	public InputStream getMobileRsrpImg() {
		return mobileRsrpImg;
	}
	public void setMobileRsrpImg(InputStream mobileRsrpImg) {
		this.mobileRsrpImg = mobileRsrpImg;
	}
	public InputStream getVoiceRsrpLegendImg() {
		return voiceRsrpLegendImg;
	}
	public void setVoiceRsrpLegendImg(InputStream voiceRsrpLegendImg) {
		this.voiceRsrpLegendImg = voiceRsrpLegendImg;
	}
	public InputStream getMobileRsrpImgLegendImg() {
		return mobileRsrpImgLegendImg;
	}
	public void setMobileRsrpImgLegendImg(InputStream mobileRsrpImgLegendImg) {
		this.mobileRsrpImgLegendImg = mobileRsrpImgLegendImg;
	}
	public List<BRTIExcelReportWrapper> getDriveSummaryList() {
		return driveSummaryList;
	}
	public void setDriveSummaryList(List<BRTIExcelReportWrapper> driveSummaryList) {
		this.driveSummaryList = driveSummaryList;
	}
	
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	
	public List<BRTIStaionaryDataWrapper> getStationaryList() {
		return stationaryList;
	}
	public void setStationaryList(List<BRTIStaionaryDataWrapper> stationaryList) {
		this.stationaryList = stationaryList;
	}
	
	@Override
	public String toString() {
		return "BRTIYearlyReportWrapper [cityName=" + cityName + ", driveSummaryList=" + driveSummaryList
				+ ", stationaryList=" + stationaryList + ", voiceRsrpImg=" + voiceRsrpImg + ", mobileRsrpImg="
				+ mobileRsrpImg + ", voiceRsrpLegendImg=" + voiceRsrpLegendImg + ", mobileRsrpImgLegendImg="
				+ mobileRsrpImgLegendImg + "]";
	}
	
	
}

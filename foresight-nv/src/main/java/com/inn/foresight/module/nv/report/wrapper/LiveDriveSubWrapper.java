package com.inn.foresight.module.nv.report.wrapper;

import java.util.ArrayList;
import java.util.List;

import com.inn.core.generic.wrapper.JpaWrapper;
import com.inn.foresight.module.nv.livedrive.wrapper.DeviceDataWapper;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.HandOverDataWrappr;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.LiveDriveVoiceAndSmsWrapper;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.QuickTestWrapper;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.YoutubeTestWrapper;

@JpaWrapper
public class LiveDriveSubWrapper {
	private Double distance;
	private Integer uniqueCells;
	private String chi;
	private String dhi;
	private Integer noOfLocation;
	private List<KPIStatsWrapper> voiceList = new ArrayList<>();
	private List<KPIStatsWrapper> dataList = new ArrayList<>();
	private List<LiveDriveChartWrapper> chartDataListLeft;
	private List<LiveDriveChartWrapper> chartDataListRight;
	private List<LiveDriveChartWrapper> idleTestList;
	private List<LiveDriveVoiceAndSmsWrapper> smsDataList;
	private List<LiveDriveVoiceAndSmsWrapper> callDataList;
	private List<LiveDriveVoiceAndSmsWrapper> callPlotDataList;
	private List<LiveDriveVoiceAndSmsWrapper> handoverPlotDataList;
	private List<YoutubeTestWrapper> youtubeTestWrapperList;
	private List<QuickTestWrapper> speedTestWrapperList;
	private List<HandOverDataWrappr>handOverList;
	private List<HandOverDataWrappr>handSummaryList;
	private List<GeographyWrapper> geoWrapperList;
	private List<YoutubeTestWrapper> httpLinkDlList;
	private List<HandOverDataWrappr>handOverIdelList;
	
	/** The site info list. */
	private List<SiteInformationWrapper>siteInfoList;


    private String recipeNo;
	private List<DeviceDataWapper> deviceList;
	String geographyL4;
	Double latitude;
	Double longitude;
	String startTime;
	String endTime;
	String geographyL3;
	String geographyL2;
	
	String geographyL1;

	
	

	public List<HandOverDataWrappr> getHandOverIdelList() {
		return handOverIdelList;
	}
	public void setHandOverIdelList(List<HandOverDataWrappr> handOverIdelList) {
		this.handOverIdelList = handOverIdelList;
	}
	public List<YoutubeTestWrapper> getHttpLinkDlList() {
		return httpLinkDlList;
	}
	public void setHttpLinkDlList(List<YoutubeTestWrapper> httpLinkDlList) {
		this.httpLinkDlList = httpLinkDlList;
	}
	public List<GeographyWrapper> getGeoWrapperList() {
		return geoWrapperList;
	}
	public void setGeoWrapperList(List<GeographyWrapper> geoWrapperList) {
		this.geoWrapperList = geoWrapperList;
	}
	public List<HandOverDataWrappr> getHandSummaryList() {
		return handSummaryList;
	}
	public void setHandSummaryList(List<HandOverDataWrappr> handSummaryList) {
		this.handSummaryList = handSummaryList;
	}
	public List<HandOverDataWrappr> getHandOverList() {
		return handOverList;
	}
	public void setHandOverList(List<HandOverDataWrappr> handOverList) {
		this.handOverList = handOverList;
	}
	public List<LiveDriveVoiceAndSmsWrapper> getCallPlotDataList() {
		return callPlotDataList;
	}
	public void setCallPlotDataList(List<LiveDriveVoiceAndSmsWrapper> callPlotDataList) {
		this.callPlotDataList = callPlotDataList;
	}
	public String getGeographyL1() {
		return geographyL1;
	}
	public void setGeographyL1(String geographyL1) {
		this.geographyL1 = geographyL1;
	}
	public List<LiveDriveChartWrapper> getIdleTestList() {
		return idleTestList;
	}
	public void setIdleTestList(List<LiveDriveChartWrapper> idleTestList) {
		this.idleTestList = idleTestList;
	}
	public String getRecipeNo() {
		return recipeNo;
	}
	public void setRecipeNo(String recipeNo) {
		this.recipeNo = recipeNo;
	}
	public String getGeographyL4() {
		return geographyL4;
	}
	public void setGeographyL4(String geographyL4) {
		this.geographyL4 = geographyL4;
	}
	public String getGeographyL3() {
		return geographyL3;
	}
	public void setGeographyL3(String geographyL3) {
		this.geographyL3 = geographyL3;
	}
	public String getGeographyL2() {
		return geographyL2;
	}
	public void setGeographyL2(String geographyL2) {
		this.geographyL2 = geographyL2;
	}
	public List<DeviceDataWapper> getDeviceList() {
		return deviceList;
	}
	public void setDeviceList(List<DeviceDataWapper> deviceList) {
		this.deviceList = deviceList;
	}
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
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public Integer getNoOfLocation() {
		return noOfLocation;
	}
	public void setNoOfLocation(Integer noOfLocation) {
		this.noOfLocation = noOfLocation;
	}
	public Double getDistance() {
		return distance;
	}
	public void setDistance(Double distance) {
		this.distance = distance;
	}
	public Integer getUniqueCells() {
		return uniqueCells;
	}
	public void setUniqueCells(Integer uniqueCells) {
		this.uniqueCells = uniqueCells;
	}
	public String getChi() {
		return chi;
	}
	public void setChi(String chi) {
		this.chi = chi;
	}
	public String getDhi() {
		return dhi;
	}
	public void setDhi(String dhi) {
		this.dhi = dhi;
	}
	public List<KPIStatsWrapper> getVoiceList() {
		return voiceList;
	}
	public void setVoiceList(List<KPIStatsWrapper> voiceList) {
		this.voiceList = voiceList;
	}
	public List<KPIStatsWrapper> getDataList() {
		return dataList;
	}
	public void setDataList(List<KPIStatsWrapper> dataList) {
		this.dataList = dataList;
	}
	public List<LiveDriveChartWrapper> getChartDataListLeft() {
		return chartDataListLeft;
	}
	public void setChartDataListLeft(List<LiveDriveChartWrapper> chartDataListLeft) {
		this.chartDataListLeft = chartDataListLeft;
	}
	public List<LiveDriveChartWrapper> getChartDataListRight() {
		return chartDataListRight;
	}
	public void setChartDataListRight(List<LiveDriveChartWrapper> chartDataListRight) {
		this.chartDataListRight = chartDataListRight;
	}
	
	public List<LiveDriveVoiceAndSmsWrapper> getSmsDataList() {
		return smsDataList;
	}
	public void setSmsDataList(List<LiveDriveVoiceAndSmsWrapper> smsDataList) {
		this.smsDataList = smsDataList;
	}
	
	public List<YoutubeTestWrapper> getYoutubeTestWrapperList() {
		return youtubeTestWrapperList;
	}
	public void setYoutubeTestWrapperList(List<YoutubeTestWrapper> youtubeTestWrapperList) {
		this.youtubeTestWrapperList = youtubeTestWrapperList;
	}
	public List<QuickTestWrapper> getSpeedTestWrapperList() {
		return speedTestWrapperList;
	}
	public void setSpeedTestWrapperList(List<QuickTestWrapper> speedTestWrapperList) {
		this.speedTestWrapperList = speedTestWrapperList;
	}
	public List<LiveDriveVoiceAndSmsWrapper> getCallDataList() {
		return callDataList;
	}
	public void setCallDataList(List<LiveDriveVoiceAndSmsWrapper> callDataList) {
		this.callDataList = callDataList;
	}
	public List<LiveDriveVoiceAndSmsWrapper> getHandoverPlotDataList() {
		return handoverPlotDataList;
	}
	public void setHandoverPlotDataList(List<LiveDriveVoiceAndSmsWrapper> handoverPlotDataList) {
		this.handoverPlotDataList = handoverPlotDataList;
	}
	public List<SiteInformationWrapper> getSiteInfoList() {
		return siteInfoList;
	}
	public void setSiteInfoList(List<SiteInformationWrapper> siteInfoList) {
		this.siteInfoList = siteInfoList;
	}
	@Override
	public String toString() {
		return "LiveDriveSubWrapper [distance=" + distance + ", uniqueCells=" + uniqueCells + ", chi=" + chi + ", dhi="
				+ dhi + ", noOfLocation=" + noOfLocation + ", voiceList=" + voiceList + ", dataList=" + dataList
				+ ", chartDataListLeft=" + chartDataListLeft + ", chartDataListRight=" + chartDataListRight
				+ ", idleTestList=" + idleTestList + ", smsDataList=" + smsDataList + ", callDataList=" + callDataList
				+ ", callPlotDataList=" + callPlotDataList + ", handoverPlotDataList=" + handoverPlotDataList
				+ ", youtubeTestWrapperList=" + youtubeTestWrapperList + ", speedTestWrapperList="
				+ speedTestWrapperList + ", handOverList=" + handOverList + ", handSummaryList=" + handSummaryList
				+ ", geoWrapperList=" + geoWrapperList + ", httpLinkDlList=" + httpLinkDlList + ", handOverIdelList="
				+ handOverIdelList + ", siteInfoList=" + siteInfoList + ", recipeNo=" + recipeNo + ", deviceList="
				+ deviceList + ", geographyL4=" + geographyL4 + ", latitude=" + latitude + ", longitude=" + longitude
				+ ", startTime=" + startTime + ", endTime=" + endTime + ", geographyL3=" + geographyL3
				+ ", geographyL2=" + geographyL2 + ", geographyL1=" + geographyL1 + "]";
	}
	

}

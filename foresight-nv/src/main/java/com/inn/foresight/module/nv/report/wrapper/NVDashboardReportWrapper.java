package com.inn.foresight.module.nv.report.wrapper;

import java.util.List;

import com.inn.foresight.module.nv.dashboard.model.NVDashboard;
import com.inn.product.um.geography.model.GeographyL1;
import com.inn.product.um.geography.model.GeographyL2;
import com.inn.product.um.geography.model.GeographyL3;
import com.inn.product.um.geography.model.GeographyL4;

public class NVDashboardReportWrapper {

	private Double avgDlRate;

	private Double avgUlRate;

	private Double latency;

	private Double jitter;

	private Double packetLoss;

	private Double url1BrowseTime;

	private Double url2BrowseTime;

	private Double url3BrowseTime;

	private Double starRating;

	private Integer weekNumber;

	private Integer monthNumber;

	private String band;

	private String technology;

	private Double signalStrength;

	private Double quality;

	private Double sinr;

	private String operator;

	private String name;

	private String type;

	private GeographyL1 geographyL1;

	private GeographyL2 geographyL2;

	private GeographyL3 geographyL3;

	private GeographyL4 geographyL4;
	
	private Long numberOfTests;
	
	private Long numberOfUsers;

	private Long androidUsers;
	
	private Long iosUsers;

	private Long androidTests;
	
	private Long iosTests;
	
	private Double healthIndex;
	
	private List<NVDashboard> dashboardDataList;
	
	public Double getAvgDlRate() {
		return avgDlRate;
	}

	public void setAvgDlRate(Double avgDlRate) {
		this.avgDlRate = avgDlRate;
	}

	public Double getAvgUlRate() {
		return avgUlRate;
	}

	public void setAvgUlRate(Double avgUlRate) {
		this.avgUlRate = avgUlRate;
	}

	public Double getLatency() {
		return latency;
	}

	public void setLatency(Double latency) {
		this.latency = latency;
	}

	public Double getJitter() {
		return jitter;
	}

	public void setJitter(Double jitter) {
		this.jitter = jitter;
	}

	public Double getPacketLoss() {
		return packetLoss;
	}

	public void setPacketLoss(Double packetLoss) {
		this.packetLoss = packetLoss;
	}

	public Double getUrl1BrowseTime() {
		return url1BrowseTime;
	}

	public void setUrl1BrowseTime(Double url1BrowseTime) {
		this.url1BrowseTime = url1BrowseTime;
	}

	public Double getUrl2BrowseTime() {
		return url2BrowseTime;
	}

	public void setUrl2BrowseTime(Double url2BrowseTime) {
		this.url2BrowseTime = url2BrowseTime;
	}

	public Double getUrl3BrowseTime() {
		return url3BrowseTime;
	}

	public void setUrl3BrowseTime(Double url3BrowseTime) {
		this.url3BrowseTime = url3BrowseTime;
	}

	public Double getStarRating() {
		return starRating;
	}

	public void setStarRating(Double starRating) {
		this.starRating = starRating;
	}

	public Integer getWeekNumber() {
		return weekNumber;
	}

	public void setWeekNumber(Integer weekNumber) {
		this.weekNumber = weekNumber;
	}

	public Integer getMonthNumber() {
		return monthNumber;
	}

	public void setMonthNumber(Integer monthNumber) {
		this.monthNumber = monthNumber;
	}

	public String getBand() {
		return band;
	}

	public void setBand(String band) {
		this.band = band;
	}

	public String getTechnology() {
		return technology;
	}

	public void setTechnology(String technology) {
		this.technology = technology;
	}

	public Double getSignalStrength() {
		return signalStrength;
	}

	public void setSignalStrength(Double signalStrength) {
		this.signalStrength = signalStrength;
	}

	public Double getQuality() {
		return quality;
	}

	public void setQuality(Double quality) {
		this.quality = quality;
	}

	public Double getSinr() {
		return sinr;
	}

	public void setSinr(Double sinr) {
		this.sinr = sinr;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public GeographyL1 getGeographyL1() {
		return geographyL1;
	}

	public void setGeographyL1(GeographyL1 geographyL1) {
		this.geographyL1 = geographyL1;
	}

	public GeographyL2 getGeographyL2() {
		return geographyL2;
	}

	public void setGeographyL2(GeographyL2 geographyL2) {
		this.geographyL2 = geographyL2;
	}

	public GeographyL3 getGeographyL3() {
		return geographyL3;
	}

	public void setGeographyL3(GeographyL3 geographyL3) {
		this.geographyL3 = geographyL3;
	}

	public GeographyL4 getGeographyL4() {
		return geographyL4;
	}

	public void setGeographyL4(GeographyL4 geographyL4) {
		this.geographyL4 = geographyL4;
	}

	public Long getNumberOfTests() {
		return numberOfTests;
	}

	public void setNumberOfTests(Long numberOfTests) {
		this.numberOfTests = numberOfTests;
	}

	public Long getNumberOfUsers() {
		return numberOfUsers;
	}

	public void setNumberOfUsers(Long numberOfUsers) {
		this.numberOfUsers = numberOfUsers;
	}

	public Long getAndroidUsers() {
		return androidUsers;
	}

	public void setAndroidUsers(Long androidUsers) {
		this.androidUsers = androidUsers;
	}

	public Long getIosUsers() {
		return iosUsers;
	}

	public void setIosUsers(Long iosUsers) {
		this.iosUsers = iosUsers;
	}

	public Long getAndroidTests() {
		return androidTests;
	}

	public void setAndroidTests(Long androidTests) {
		this.androidTests = androidTests;
	}

	public Long getIosTests() {
		return iosTests;
	}

	public void setIosTests(Long iosTests) {
		this.iosTests = iosTests;
	}

	public Double getHealthIndex() {
		return healthIndex;
	}

	public void setHealthIndex(Double healthIndex) {
		this.healthIndex = healthIndex;
	}

	public List<NVDashboard> getDashboardDataList() {
		return dashboardDataList;
	}

	public void setDashboardDataList(List<NVDashboard> dashboardDataList) {
		this.dashboardDataList = dashboardDataList;
	}

	@Override
	public String toString() {
		return "NVDashboardReportWrapper [avgDlRate=" + avgDlRate + ", avgUlRate=" + avgUlRate + ", latency=" + latency
				+ ", jitter=" + jitter + ", packetLoss=" + packetLoss + ", url1BrowseTime=" + url1BrowseTime
				+ ", url2BrowseTime=" + url2BrowseTime + ", url3BrowseTime=" + url3BrowseTime + ", starRating="
				+ starRating + ", weekNumber=" + weekNumber + ", monthNumber=" + monthNumber + ", band=" + band
				+ ", technology=" + technology + ", signalStrength=" + signalStrength + ", quality=" + quality
				+ ", sinr=" + sinr + ", operator=" + operator + ", name=" + name + ", type=" + type + ", geographyL1="
				+ geographyL1 + ", geographyL2=" + geographyL2 + ", geographyL3=" + geographyL3 + ", geographyL4="
				+ geographyL4 + ", numberOfTests=" + numberOfTests + ", numberOfUsers=" + numberOfUsers
				+ ", androidUsers=" + androidUsers + ", iosUsers=" + iosUsers + ", androidTests=" + androidTests
				+ ", iosTests=" + iosTests + ", healthIndex=" + healthIndex + ", dashboardDataList=" + dashboardDataList
				+ "]";
	}

	


	
	
}

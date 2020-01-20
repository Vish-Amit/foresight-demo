package com.inn.foresight.module.nv.report.wrapper;

import java.util.List;
import java.util.Map;

import com.inn.foresight.module.nv.report.smf.wrapper.MobilityLocationWrapper;
import com.inn.foresight.module.nv.report.workorder.wrapper.KPIWrapper;
import com.inn.foresight.module.nv.report.wrapper.benchmark.KpiRankWrapper;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.SmsEventStatistics;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.benchmark.SmsStatisticsList;
import com.inn.foresight.module.nv.report.wrapper.stealth.StealthWODetailItemWrapper;

/** The Class ReportSubWrapper. */
public class ReportSubWrapper {

	/** The site info list. */
	private List<SiteInformationWrapper> siteInfoList;

	/** The kpi info list. */
	private List<KPISummaryDataWrapper> kpiInfoList;
	private List<GraphWrapper> rsrpList;
	private List<GraphWrapper> sinrList;
	private List<GraphWrapper> dlList;
	private List<GraphWrapper> ulList;
	private List<GraphWrapper> httpRsrpDlList;
	private List<GraphWrapper> ftpRsrpDlList;
	private List<GraphWrapper> httpRsrpUlList;
	private List<GraphWrapper> ftpRsrpUlList;
	private List<GraphWrapper> ftpSinrDlList;
	private List<GraphWrapper> httpSinrDlList;
	private List<GraphWrapper> ftpSinrUlList;
	private List<GraphWrapper> httpSinrUlList;
	private List<GraphWrapper> earfcnList;
	private List<GraphWrapper> riList;
	private List<GraphWrapper> cqiList;
	private List<GraphWrapper> mosList;
	List<KPIWrapper> kpiList;
	
	
	private String key1;
	private String key2;
	private String key3;
	private String key4;
	
	private List<KpiRankWrapper> rsrpRankList;
	private List<KpiRankWrapper> sinrRankList;
	private List<KpiRankWrapper> dlRankList;
	private List<KpiRankWrapper> ulRankList;
	private List<String[]> jsonData;
	
	

	public List<String[]> getJsonData() {
		return jsonData;
	}

	public void setJsonData(List<String[]> jsonData) {
		this.jsonData = jsonData;
	}

	public List<KpiRankWrapper> getRsrpRankList() {
		return rsrpRankList;
	}

	public void setRsrpRankList(List<KpiRankWrapper> rsrpRankList) {
		this.rsrpRankList = rsrpRankList;
	}

	public List<KpiRankWrapper> getSinrRankList() {
		return sinrRankList;
	}

	public void setSinrRankList(List<KpiRankWrapper> sinrRankList) {
		this.sinrRankList = sinrRankList;
	}

	public List<KpiRankWrapper> getDlRankList() {
		return dlRankList;
	}

	public void setDlRankList(List<KpiRankWrapper> dlRankList) {
		this.dlRankList = dlRankList;
	}

	public List<KpiRankWrapper> getUlRankList() {
		return ulRankList;
	}

	public void setUlRankList(List<KpiRankWrapper> ulRankList) {
		this.ulRankList = ulRankList;
	}

	public String getKey1() {
		return key1;
	}

	public void setKey1(String key1) {
		this.key1 = key1;
	}

	public String getKey2() {
		return key2;
	}

	public void setKey2(String key2) {
		this.key2 = key2;
	}

	public String getKey3() {
		return key3;
	}

	public void setKey3(String key3) {
		this.key3 = key3;
	}

	public String getKey4() {
		return key4;
	}

	public void setKey4(String key4) {
		this.key4 = key4;
	}

	public List<KPIWrapper> getKpiList() {
		return kpiList;
	}

	public void setKpiList(List<KPIWrapper> kpiList) {
		this.kpiList = kpiList;
	}

	public List<GraphWrapper> getRiList() {
		return riList;
	}

	public void setRiList(List<GraphWrapper> riList) {
		this.riList = riList;
	}

	public List<GraphWrapper> getCqiList() {
		return cqiList;
	}

	public void setCqiList(List<GraphWrapper> cqiList) {
		this.cqiList = cqiList;
	}

	public List<GraphWrapper> getMosList() {
		return mosList;
	}

	public void setMosList(List<GraphWrapper> mosList) {
		this.mosList = mosList;
	}

	private List<CallEventStatistics> callEventOnNetStatsStationary;
	private List<CallEventStatistics> callEventOffNetStatsStationary;
	private List<CallEventStatistics> callEventStatsStationary;
	private List<SmsEventStatistics> smsEventOnNetStatisticsStationary;
	private List<SmsEventStatistics> smsEventOffNetStatisticsStationary;
	private List<SmsStatisticsList> smsStatisticsList;

	private List<CallEventStatistics> callEventOnNetStatsDrive;
	private List<CallEventStatistics> callEventOffNetStatsDrive;
	private List<CallEventStatistics> callEventStatsDrive;
	private List<SmsEventStatistics> smsEventOnNetStatisticsDrive;
	private List<SmsEventStatistics> smsEventOffNetStatisticsDrive;
	private List<SmsEventStatistics> smsEventStatisticsDrive;

	private List<GraphWrapper> graphList;
	private List<MobilityLocationWrapper> locationInfo;
	private List<RawDataWrapper> locationDataList;
	private List<RawDataWrapper> locationDataListRight;

	private List<RemarkDataWrapper> remarkDataList;
	private List<RemarkDataWrapper> testSkipDataList;

	private List<StealthWODetailItemWrapper> deviceDetailList;

	Map<String,String> imageMap;
	
	public Map<String, String> getImageMap() {
		return imageMap;
	}

	public void setImageMap(Map<String, String> imageMap) {
		this.imageMap = imageMap;
	}

	/** The avg rsrp. */
	private Double avgRsrp;

	/** The avg sinr. */
	private Double avgSinr;

	/** The avg dl. */
	private Double avgDl;

	/** The avg ul. */
	private Double avgUl;

	/** The test date. */
	private String testDate;

	private String cityName;

	private String quarter;

	private String testName;

	private String monthAndYear;

	private String geoLevel;

	private String technologyType;

	private String callCountStationary;
	private String callCountStationaryOnNet;
	private String callCountStationaryOffNet;

	private String callCountDrive;
	private String callCountDriveOnNet;
	private String callCountDriveOffNet;

	private String smsCount;

	private String finalScore;
	private String walktestScore;

	public String getFinalScore() {
		return finalScore;
	}

	public void setFinalScore(String finalScore) {
		this.finalScore = finalScore;
	}

	public String getWalktestScore() {
		return walktestScore;
	}

	public void setWalktestScore(String walktestScore) {
		this.walktestScore = walktestScore;
	}

	public List<SmsStatisticsList> getSmsStatisticsList() {
		return smsStatisticsList;
	}

	public void setSmsStatisticsList(List<SmsStatisticsList> smsStatisticsList) {
		this.smsStatisticsList = smsStatisticsList;
	}

	public String getCallCountStationary() {
		return callCountStationary;
	}

	public void setCallCountStationary(String callCountStationary) {
		this.callCountStationary = callCountStationary;
	}

	public String getCallCountStationaryOnNet() {
		return callCountStationaryOnNet;
	}

	public void setCallCountStationaryOnNet(String callCountStationaryOnNet) {
		this.callCountStationaryOnNet = callCountStationaryOnNet;
	}

	public String getCallCountStationaryOffNet() {
		return callCountStationaryOffNet;
	}

	public void setCallCountStationaryOffNet(String callCountStationaryOffNet) {
		this.callCountStationaryOffNet = callCountStationaryOffNet;
	}

	public String getCallCountDrive() {
		return callCountDrive;
	}

	public void setCallCountDrive(String callCountDrive) {
		this.callCountDrive = callCountDrive;
	}

	public String getCallCountDriveOnNet() {
		return callCountDriveOnNet;
	}

	public void setCallCountDriveOnNet(String callCountDriveOnNet) {
		this.callCountDriveOnNet = callCountDriveOnNet;
	}

	public String getCallCountDriveOffNet() {
		return callCountDriveOffNet;
	}

	public void setCallCountDriveOffNet(String callCountDriveOffNet) {
		this.callCountDriveOffNet = callCountDriveOffNet;
	}

	public String getSmsCount() {
		return smsCount;
	}

	public void setSmsCount(String smsCount) {
		this.smsCount = smsCount;
	}

	public List<SiteInformationWrapper> getSiteInfoList() {
		return siteInfoList;
	}

	public void setSiteInfoList(List<SiteInformationWrapper> siteInfoList) {
		this.siteInfoList = siteInfoList;
	}

	public List<KPISummaryDataWrapper> getKpiInfoList() {
		return kpiInfoList;
	}

	public void setKpiInfoList(List<KPISummaryDataWrapper> kpiInfoList) {
		this.kpiInfoList = kpiInfoList;
	}

	public List<GraphWrapper> getRsrpList() {
		return rsrpList;
	}

	public void setRsrpList(List<GraphWrapper> rsrpList) {
		this.rsrpList = rsrpList;
	}

	public List<GraphWrapper> getSinrList() {
		return sinrList;
	}

	public void setSinrList(List<GraphWrapper> sinrList) {
		this.sinrList = sinrList;
	}

	public List<GraphWrapper> getDlList() {
		return dlList;
	}

	public void setDlList(List<GraphWrapper> dlList) {
		this.dlList = dlList;
	}

	public List<GraphWrapper> getUlList() {
		return ulList;
	}

	public void setUlList(List<GraphWrapper> ulList) {
		this.ulList = ulList;
	}

	public List<GraphWrapper> getHttpRsrpDlList() {
		return httpRsrpDlList;
	}

	public void setHttpRsrpDlList(List<GraphWrapper> httpRsrpDlList) {
		this.httpRsrpDlList = httpRsrpDlList;
	}

	public List<GraphWrapper> getFtpRsrpDlList() {
		return ftpRsrpDlList;
	}

	public void setFtpRsrpDlList(List<GraphWrapper> ftpRsrpDlList) {
		this.ftpRsrpDlList = ftpRsrpDlList;
	}

	public List<GraphWrapper> getHttpRsrpUlList() {
		return httpRsrpUlList;
	}

	public void setHttpRsrpUlList(List<GraphWrapper> httpRsrpUlList) {
		this.httpRsrpUlList = httpRsrpUlList;
	}

	public List<GraphWrapper> getFtpRsrpUlList() {
		return ftpRsrpUlList;
	}

	public void setFtpRsrpUlList(List<GraphWrapper> ftpRsrpUlList) {
		this.ftpRsrpUlList = ftpRsrpUlList;
	}

	public List<GraphWrapper> getFtpSinrDlList() {
		return ftpSinrDlList;
	}

	public void setFtpSinrDlList(List<GraphWrapper> ftpSinrDlList) {
		this.ftpSinrDlList = ftpSinrDlList;
	}

	public List<GraphWrapper> getHttpSinrDlList() {
		return httpSinrDlList;
	}

	public void setHttpSinrDlList(List<GraphWrapper> httpSinrDlList) {
		this.httpSinrDlList = httpSinrDlList;
	}

	public List<GraphWrapper> getFtpSinrUlList() {
		return ftpSinrUlList;
	}

	public void setFtpSinrUlList(List<GraphWrapper> ftpSinrUlList) {
		this.ftpSinrUlList = ftpSinrUlList;
	}

	public List<GraphWrapper> getHttpSinrUlList() {
		return httpSinrUlList;
	}

	public void setHttpSinrUlList(List<GraphWrapper> httpSinrUlList) {
		this.httpSinrUlList = httpSinrUlList;
	}

	public List<GraphWrapper> getEarfcnList() {
		return earfcnList;
	}

	public void setEarfcnList(List<GraphWrapper> earfcnList) {
		this.earfcnList = earfcnList;
	}

	public List<CallEventStatistics> getCallEventOnNetStatsStationary() {
		return callEventOnNetStatsStationary;
	}

	public void setCallEventOnNetStatsStationary(List<CallEventStatistics> callEventOnNetStatsStationary) {
		this.callEventOnNetStatsStationary = callEventOnNetStatsStationary;
	}

	public List<CallEventStatistics> getCallEventOffNetStatsStationary() {
		return callEventOffNetStatsStationary;
	}

	public void setCallEventOffNetStatsStationary(List<CallEventStatistics> callEventOffNetStatsStationary) {
		this.callEventOffNetStatsStationary = callEventOffNetStatsStationary;
	}

	public List<CallEventStatistics> getCallEventStatsStationary() {
		return callEventStatsStationary;
	}

	public void setCallEventStatsStationary(List<CallEventStatistics> callEventStatsStationary) {
		this.callEventStatsStationary = callEventStatsStationary;
	}

	public List<SmsEventStatistics> getSmsEventOnNetStatisticsStationary() {
		return smsEventOnNetStatisticsStationary;
	}

	public void setSmsEventOnNetStatisticsStationary(List<SmsEventStatistics> smsEventOnNetStatisticsStationary) {
		this.smsEventOnNetStatisticsStationary = smsEventOnNetStatisticsStationary;
	}

	public List<SmsEventStatistics> getSmsEventOffNetStatisticsStationary() {
		return smsEventOffNetStatisticsStationary;
	}

	public void setSmsEventOffNetStatisticsStationary(List<SmsEventStatistics> smsEventOffNetStatisticsStationary) {
		this.smsEventOffNetStatisticsStationary = smsEventOffNetStatisticsStationary;
	}

	public List<CallEventStatistics> getCallEventOnNetStatsDrive() {
		return callEventOnNetStatsDrive;
	}

	public void setCallEventOnNetStatsDrive(List<CallEventStatistics> callEventOnNetStatsDrive) {
		this.callEventOnNetStatsDrive = callEventOnNetStatsDrive;
	}

	public List<CallEventStatistics> getCallEventOffNetStatsDrive() {
		return callEventOffNetStatsDrive;
	}

	public void setCallEventOffNetStatsDrive(List<CallEventStatistics> callEventOffNetStatsDrive) {
		this.callEventOffNetStatsDrive = callEventOffNetStatsDrive;
	}

	public List<CallEventStatistics> getCallEventStatsDrive() {
		return callEventStatsDrive;
	}

	public void setCallEventStatsDrive(List<CallEventStatistics> callEventStatsDrive) {
		this.callEventStatsDrive = callEventStatsDrive;
	}

	public List<SmsEventStatistics> getSmsEventOnNetStatisticsDrive() {
		return smsEventOnNetStatisticsDrive;
	}

	public void setSmsEventOnNetStatisticsDrive(List<SmsEventStatistics> smsEventOnNetStatisticsDrive) {
		this.smsEventOnNetStatisticsDrive = smsEventOnNetStatisticsDrive;
	}

	public List<SmsEventStatistics> getSmsEventOffNetStatisticsDrive() {
		return smsEventOffNetStatisticsDrive;
	}

	public void setSmsEventOffNetStatisticsDrive(List<SmsEventStatistics> smsEventOffNetStatisticsDrive) {
		this.smsEventOffNetStatisticsDrive = smsEventOffNetStatisticsDrive;
	}

	public List<SmsEventStatistics> getSmsEventStatisticsDrive() {
		return smsEventStatisticsDrive;
	}

	public void setSmsEventStatisticsDrive(List<SmsEventStatistics> smsEventStatisticsDrive) {
		this.smsEventStatisticsDrive = smsEventStatisticsDrive;
	}

	public List<GraphWrapper> getGraphList() {
		return graphList;
	}

	public void setGraphList(List<GraphWrapper> graphList) {
		this.graphList = graphList;
	}

	public List<MobilityLocationWrapper> getLocationInfo() {
		return locationInfo;
	}

	public void setLocationInfo(List<MobilityLocationWrapper> locationInfo) {
		this.locationInfo = locationInfo;
	}

	public List<RawDataWrapper> getLocationDataList() {
		return locationDataList;
	}

	public void setLocationDataList(List<RawDataWrapper> locationDataList) {
		this.locationDataList = locationDataList;
	}

	public List<RawDataWrapper> getLocationDataListRight() {
		return locationDataListRight;
	}

	public void setLocationDataListRight(List<RawDataWrapper> locationDataListRight) {
		this.locationDataListRight = locationDataListRight;
	}

	public Double getAvgRsrp() {
		return avgRsrp;
	}

	public void setAvgRsrp(Double avgRsrp) {
		this.avgRsrp = avgRsrp;
	}

	public Double getAvgSinr() {
		return avgSinr;
	}

	public void setAvgSinr(Double avgSinr) {
		this.avgSinr = avgSinr;
	}

	public Double getAvgDl() {
		return avgDl;
	}

	public void setAvgDl(Double avgDl) {
		this.avgDl = avgDl;
	}

	public Double getAvgUl() {
		return avgUl;
	}

	public void setAvgUl(Double avgUl) {
		this.avgUl = avgUl;
	}

	public String getTestDate() {
		return testDate;
	}

	public void setTestDate(String testDate) {
		this.testDate = testDate;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getQuarter() {
		return quarter;
	}

	public void setQuarter(String quarter) {
		this.quarter = quarter;
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	public String getMonthAndYear() {
		return monthAndYear;
	}

	public void setMonthAndYear(String monthAndYear) {
		this.monthAndYear = monthAndYear;
	}

	public String getGeoLevel() {
		return geoLevel;
	}

	public void setGeoLevel(String geoLevel) {
		this.geoLevel = geoLevel;
	}

	public String getTechnologyType() {
		return technologyType;
	}

	public void setTechnologyType(String technologyType) {
		this.technologyType = technologyType;
	}

	public List<RemarkDataWrapper> getRemarkDataList() {
		return remarkDataList;
	}

	public void setRemarkDataList(List<RemarkDataWrapper> remarkDataList) {
		this.remarkDataList = remarkDataList;
	}

	public List<RemarkDataWrapper> getTestSkipDataList() {
		return testSkipDataList;
	}

	public void setTestSkipDataList(List<RemarkDataWrapper> testSkipDataList) {
		this.testSkipDataList = testSkipDataList;
	}

	public List<StealthWODetailItemWrapper> getDeviceDetailList() {
		return deviceDetailList;
	}

	public void setDeviceDetailList(List<StealthWODetailItemWrapper> deviceDetailList) {
		this.deviceDetailList = deviceDetailList;
	}

	@Override
	public String toString() {
		return "ReportSubWrapper [siteInfoList=" + siteInfoList + ", kpiInfoList=" + kpiInfoList + ", rsrpList="
				+ rsrpList + ", sinrList=" + sinrList + ", dlList=" + dlList + ", ulList=" + ulList
				+ ", httpRsrpDlList=" + httpRsrpDlList + ", ftpRsrpDlList=" + ftpRsrpDlList + ", httpRsrpUlList="
				+ httpRsrpUlList + ", ftpRsrpUlList=" + ftpRsrpUlList + ", ftpSinrDlList=" + ftpSinrDlList
				+ ", httpSinrDlList=" + httpSinrDlList + ", ftpSinrUlList=" + ftpSinrUlList + ", httpSinrUlList="
				+ httpSinrUlList + ", earfcnList=" + earfcnList + ", riList=" + riList + ", cqiList=" + cqiList
				+ ", mosList=" + mosList + ", callEventOnNetStatsStationary=" + callEventOnNetStatsStationary
				+ ", callEventOffNetStatsStationary=" + callEventOffNetStatsStationary + ", callEventStatsStationary="
				+ callEventStatsStationary + ", smsEventOnNetStatisticsStationary=" + smsEventOnNetStatisticsStationary
				+ ", smsEventOffNetStatisticsStationary=" + smsEventOffNetStatisticsStationary + ", smsStatisticsList="
				+ smsStatisticsList + ", callEventOnNetStatsDrive=" + callEventOnNetStatsDrive
				+ ", callEventOffNetStatsDrive=" + callEventOffNetStatsDrive + ", callEventStatsDrive="
				+ callEventStatsDrive + ", smsEventOnNetStatisticsDrive=" + smsEventOnNetStatisticsDrive
				+ ", smsEventOffNetStatisticsDrive=" + smsEventOffNetStatisticsDrive + ", smsEventStatisticsDrive="
				+ smsEventStatisticsDrive + ", graphList=" + graphList + ", locationInfo=" + locationInfo
				+ ", locationDataList=" + locationDataList + ", locationDataListRight=" + locationDataListRight
				+ ", remarkDataList=" + remarkDataList + ", testSkipDataList=" + testSkipDataList
				+ ", deviceDetailList=" + deviceDetailList + ", avgRsrp=" + avgRsrp + ", avgSinr=" + avgSinr
				+ ", avgDl=" + avgDl + ", avgUl=" + avgUl + ", testDate=" + testDate + ", cityName=" + cityName
				+ ", quarter=" + quarter + ", testName=" + testName + ", monthAndYear=" + monthAndYear + ", geoLevel="
				+ geoLevel + ", technologyType=" + technologyType + ", callCountStationary=" + callCountStationary
				+ ", callCountStationaryOnNet=" + callCountStationaryOnNet + ", callCountStationaryOffNet="
				+ callCountStationaryOffNet + ", callCountDrive=" + callCountDrive + ", callCountDriveOnNet="
				+ callCountDriveOnNet + ", callCountDriveOffNet=" + callCountDriveOffNet + ", smsCount=" + smsCount
				+ ", finalScore=" + finalScore + ", walktestScore=" + walktestScore + "]";
	}

}

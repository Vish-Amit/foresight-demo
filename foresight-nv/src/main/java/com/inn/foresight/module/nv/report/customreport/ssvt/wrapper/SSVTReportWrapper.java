package com.inn.foresight.module.nv.report.customreport.ssvt.wrapper;

import com.inn.foresight.module.nv.report.wrapper.SectorSwapWrapper;

import java.util.List;

public class SSVTReportWrapper {

	String cityName;
	String siteName;
	String testEngineer;
	String technology;
	String testDate;
	String siteId;

	List<SiteInfoWrapper> siteInfo;

	List<HandoverDataWrapper> handoverDataList;

	List<CallAnalysisDataWrapper> callAnalysisList;

	List<SectorSwapWrapper> sectorSwapList;

	List<SiteInfoWrapper> siteDataList;

	List<KpiDataWrapper> kpiDataList;

	public List<SectorSwapWrapper> getSectorSwapList() {
		return sectorSwapList;
	}

	public void setSectorSwapList(List<SectorSwapWrapper> sectorSwapList) {
		this.sectorSwapList = sectorSwapList;
	}

	public List<SiteInfoWrapper> getSiteInfo() {
		return siteInfo;
	}

	public void setSiteInfo(List<SiteInfoWrapper> siteInfo) {
		this.siteInfo = siteInfo;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String city) {
		this.cityName = city;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getTestEngineer() {
		return testEngineer;
	}

	public void setTestEngineer(String testEngineer) {
		this.testEngineer = testEngineer;
	}

	public String getTechnology() {
		return technology;
	}

	public void setTechnology(String technology) {
		this.technology = technology;
	}

	public String getTestDate() {
		return testDate;
	}

	public void setTestDate(String testDate) {
		this.testDate = testDate;
	}

	public List<HandoverDataWrapper> getHandoverDataList() {
		return handoverDataList;
	}

	public void setHandoverDataList(List<HandoverDataWrapper> handoverDataList) {
		this.handoverDataList = handoverDataList;
	}

	public List<CallAnalysisDataWrapper> getCallAnalysisList() {
		return callAnalysisList;
	}

	public void setCallAnalysisList(List<CallAnalysisDataWrapper> callAnalysisList) {
		this.callAnalysisList = callAnalysisList;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public List<SiteInfoWrapper> getSiteDataList() {
		return siteDataList;
	}

	public void setSiteDataList(List<SiteInfoWrapper> siteDataList) {
		this.siteDataList = siteDataList;
	}

	public List<KpiDataWrapper> getKpiDataList() {
		return kpiDataList;
	}

	public void setKpiDataList(List<KpiDataWrapper> kpiDataList) {
		this.kpiDataList = kpiDataList;
	}

}

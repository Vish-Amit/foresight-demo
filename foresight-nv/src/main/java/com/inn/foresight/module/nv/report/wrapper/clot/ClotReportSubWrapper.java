package com.inn.foresight.module.nv.report.wrapper.clot;

import java.util.List;

import com.inn.foresight.module.nv.report.wrapper.GraphDataWrapper;
import com.inn.foresight.module.nv.report.wrapper.GraphWrapper;
import com.inn.foresight.module.nv.report.wrapper.KPISummaryDataWrapper;
import com.inn.foresight.module.nv.report.wrapper.SiteInformationWrapper;

public class ClotReportSubWrapper {
	private	String clusterName;
	private String testerName;
	private String address;
	private String technology;
	private String  frequency;
	private Integer noOfSites;
	private String city;
    private List<GraphWrapper>rsrpList;
    private List<GraphWrapper>sinrList;
    private List<GraphWrapper>dlList;
    private List<GraphWrapper>ulList;
    private List<GraphWrapper>mimoList;
    private List<GraphWrapper>cqiList;
    private List<GraphWrapper>mcsList;
    private List<GraphDataWrapper>earfcnList;

/** The site info list. */
private List<SiteInformationWrapper>siteInfoList;

/** The kpi info list. */
private List<KPISummaryDataWrapper>kpiInfoList;



public String getCity() {
	return city;
}
public void setCity(String city) {
	this.city = city;
}
public Integer getNoOfSites() {
	return noOfSites;
}
public void setNoOfSites(Integer noOfSites) {
	this.noOfSites = noOfSites;
}
public String getClusterName() {
	return clusterName;
}
public void setClusterName(String clusterName) {
	this.clusterName = clusterName;
}
public String getTesterName() {
	return testerName;
}
public void setTesterName(String testerName) {
	this.testerName = testerName;
}
public String getAddress() {
	return address;
}
public void setAddress(String address) {
	this.address = address;
}
public String getTechnology() {
	return technology;
}
public void setTechnology(String technology) {
	this.technology = technology;
}
public String getFrequency() {
	return frequency;
}
public void setFrequency(String frequency) {
	this.frequency = frequency;
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
public List<GraphWrapper> getMimoList() {
	return mimoList;
}
public void setMimoList(List<GraphWrapper> mimoList) {
	this.mimoList = mimoList;
}
public List<GraphWrapper> getCqiList() {
	return cqiList;
}
public void setCqiList(List<GraphWrapper> cqiList) {
	this.cqiList = cqiList;
}
public List<GraphWrapper> getMcsList() {
	return mcsList;
}
public void setMcsList(List<GraphWrapper> mcsList) {
	this.mcsList = mcsList;
}


public List<GraphDataWrapper> getEarfcnList() {
	return earfcnList;
}
public void setEarfcnList(List<GraphDataWrapper> earfcnList) {
	this.earfcnList = earfcnList;
}

@Override
public String toString() {
	return "ClotReportSubWrapper [clusterName=" + clusterName + ", testerName=" + testerName + ", address=" + address
			+ ", technology=" + technology + ", frequency=" + frequency + ", noOfSites=" + noOfSites + ", city=" + city
			+ ", rsrpList=" + rsrpList + ", sinrList=" + sinrList + ", dlList=" + dlList + ", ulList=" + ulList
			+ ", mimoList=" + mimoList + ", cqiList=" + cqiList + ", mcsList=" + mcsList + ", earfcnList="
			+ earfcnList + ", siteInfoList=" + siteInfoList + ", kpiInfoList=" + kpiInfoList + "]";
}






}

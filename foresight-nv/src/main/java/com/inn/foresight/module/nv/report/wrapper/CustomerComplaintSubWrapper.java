package com.inn.foresight.module.nv.report.wrapper;

import java.util.List;

public class CustomerComplaintSubWrapper {
   
	private String testDate;
	private String testType;
	private String complaintDate;

	private String complainerName;
	private String complainerAddress;
	private Double latitude;
	private Double longitude;
	private String dateOfComplaint;
	private String reasonToComplaint;
	private List<KPIImgDataWrapper>imgList;
    private List<AvrageKpiWrapper>preList;
    private List<AvrageKpiWrapper>postList;
    private List<SiteInformationWrapper> siteList;
    private Double onAirSitePercentage;
    private Double onAirCellPercentage;
    private Integer noOfSites;
    private Integer noOfCells;
    private String clusterName;
    private String description;
    private List<SiteInformationWrapper> cellWiseSiteList;
    
	public String getComplaintDate() {
		return complaintDate;
	}
	public void setComplaintDate(String complaintDate) {
		this.complaintDate = complaintDate;
	}
	public String getComplainerName() {
		return complainerName;
	}
	public void setComplainerName(String complainerName) {
		this.complainerName = complainerName;
	}
	public String getComplainerAddress() {
		return complainerAddress;
	}
	public void setComplainerAddress(String complainerAddress) {
		this.complainerAddress = complainerAddress;
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
	public String getDateOfComplaint() {
		return dateOfComplaint;
	}
	public void setDateOfComplaint(String dateOfComplaint) {
		this.dateOfComplaint = dateOfComplaint;
	}
	public String getReasonToComplaint() {
		return reasonToComplaint;
	}
	public void setReasonToComplaint(String reasonToComplaint) {
		this.reasonToComplaint = reasonToComplaint;
	}
	public String getTestDate() {
		return testDate;
	}
	public void setTestDate(String testDate) {
		this.testDate = testDate;
	}
	public String getTestType() {
		return testType;
	}
	public void setTestType(String testType) {
		this.testType = testType;
	}
	
	public List<KPIImgDataWrapper> getImgList() {
		return imgList;
	}
	public void setImgList(List<KPIImgDataWrapper> imgList) {
		this.imgList = imgList;
	}
	
	
	public List<AvrageKpiWrapper> getPreList() {
		return preList;
	}
	public void setPreList(List<AvrageKpiWrapper> preList) {
		this.preList = preList;
	}
	public List<AvrageKpiWrapper> getPostList() {
		return postList;
	}
	public void setPostList(List<AvrageKpiWrapper> postList) {
		this.postList = postList;
	}
	
	public List<SiteInformationWrapper> getSiteList() {
		return siteList;
	}
	public void setSiteList(List<SiteInformationWrapper> siteList) {
		this.siteList = siteList;
	}
	
	public Double getOnAirSitePercentage() {
		return onAirSitePercentage;
	}
	public void setOnAirSitePercentage(Double onAirSitePercentage) {
		this.onAirSitePercentage = onAirSitePercentage;
	}
	public Double getOnAirCellPercentage() {
		return onAirCellPercentage;
	}
	public void setOnAirCellPercentage(Double onAirCellPercentage) {
		this.onAirCellPercentage = onAirCellPercentage;
	}
	public Integer getNoOfSites() {
		return noOfSites;
	}
	public void setNoOfSites(Integer noOfSites) {
		this.noOfSites = noOfSites;
	}
	public Integer getNoOfCells() {
		return noOfCells;
	}
	public void setNoOfCells(Integer noOfCells) {
		this.noOfCells = noOfCells;
	}
	
	
	
	public String getClusterName() {
		return clusterName;
	}
	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	public List<SiteInformationWrapper> getCellWiseSiteList() {
		return cellWiseSiteList;
	}
	public void setCellWiseSiteList(List<SiteInformationWrapper> cellWiseSiteList) {
		this.cellWiseSiteList = cellWiseSiteList;
	}
	@Override
	public String toString() {
		return "CustomerComplaintSubWrapper [testDate=" + testDate + ", testType=" + testType + ", complaintDate="
				+ complaintDate + ", complainerName=" + complainerName + ", complainerAddress=" + complainerAddress
				+ ", latitude=" + latitude + ", longitude=" + longitude + ", dateOfComplaint=" + dateOfComplaint
				+ ", reasonToComplaint=" + reasonToComplaint + ", imgList=" + imgList + ", preList=" + preList
				+ ", postList=" + postList + ", siteList=" + siteList + ", onAirSitePercentage=" + onAirSitePercentage
				+ ", onAirCellPercentage=" + onAirCellPercentage + ", noOfSites=" + noOfSites + ", noOfCells="
				+ noOfCells + ", clusterName=" + clusterName + ", description=" + description + ", cellWiseSiteList="
				+ cellWiseSiteList + "]";
	}
	
}

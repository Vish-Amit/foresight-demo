package com.inn.foresight.module.nv.report.wrapper;

import java.util.List;

import com.inn.core.generic.wrapper.JpaWrapper;

@JpaWrapper
public class PSDataWrapper {

	private Integer httpDownloadAttempt;
	private Integer httpDownloadSuccess;
	private Double httpDownloadTimeAvg;
	private Double httpDlSuccessRate;
	private Double httpThroughputAvg;
	private Double networkLatency;
	private Double packetLoss;

	private List<CapturedImagesWrapper> listOfAzimuthImages;
	private List<CapturedImagesWrapper> listOfETiltImages;
	private List<CapturedImagesWrapper> listOfMTiltImages;
	private List<CapturedImagesWrapper> listOfPanoramaImages;
	private List<CapturedImagesWrapper> listOfAntennaHeightImages;
	private List<SiteInformationWrapper> siteInfoList;
	private String sfId;
	private String siteId;
	private String region;
	private String city;
	private String cluster;
	private Double latitude;
	private Double longitude;
	private String siteType;
	private String dateOfVisit;
	private String nameAndContactNumber;

	public String getSfId() {
		return sfId;
	}

	public void setSfId(String sfId) {
		this.sfId = sfId;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCluster() {
		return cluster;
	}

	public void setCluster(String cluster) {
		this.cluster = cluster;
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

	public String getSiteType() {
		return siteType;
	}

	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}

	public String getDateOfVisit() {
		return dateOfVisit;
	}

	public void setDateOfVisit(String dateOfVisit) {
		this.dateOfVisit = dateOfVisit;
	}

	public String getNameAndContactNumber() {
		return nameAndContactNumber;
	}

	public void setNameAndContactNumber(String nameAndContactNumber) {
		this.nameAndContactNumber = nameAndContactNumber;
	}

	public Integer getHttpDownloadAttempt() {
		return httpDownloadAttempt;
	}

	public void setHttpDownloadAttempt(Integer httpDownloadAttempt) {
		this.httpDownloadAttempt = httpDownloadAttempt;
	}

	public Integer getHttpDownloadSuccess() {
		return httpDownloadSuccess;
	}

	public void setHttpDownloadSuccess(Integer httpDownloadSuccess) {
		this.httpDownloadSuccess = httpDownloadSuccess;
	}

	public Double getHttpDownloadTimeAvg() {
		return httpDownloadTimeAvg;
	}

	public void setHttpDownloadTimeAvg(Double httpDownloadTimeAvg) {
		this.httpDownloadTimeAvg = httpDownloadTimeAvg;
	}

	public Double getHttpDlSuccessRate() {
		return httpDlSuccessRate;
	}

	public void setHttpDlSuccessRate(Double httpDlSuccessRate) {
		this.httpDlSuccessRate = httpDlSuccessRate;
	}

	public Double getHttpThroughputAvg() {
		return httpThroughputAvg;
	}

	public void setHttpThroughputAvg(Double httpThroughputAvg) {
		this.httpThroughputAvg = httpThroughputAvg;
	}

	public Double getNetworkLatency() {
		return networkLatency;
	}

	public void setNetworkLatency(Double networkLatency) {
		this.networkLatency = networkLatency;
	}

	public Double getPacketLoss() {
		return packetLoss;
	}

	public void setPacketLoss(Double packetLoss) {
		this.packetLoss = packetLoss;
	}

	public List<CapturedImagesWrapper> getListOfAzimuthImages() {
		return listOfAzimuthImages;
	}

	public void setListOfAzimuthImages(List<CapturedImagesWrapper> listOfAzimuthImages) {
		this.listOfAzimuthImages = listOfAzimuthImages;
	}

	public List<CapturedImagesWrapper> getListOfETiltImages() {
		return listOfETiltImages;
	}

	public void setListOfETiltImages(List<CapturedImagesWrapper> listOfETiltImages) {
		this.listOfETiltImages = listOfETiltImages;
	}

	public List<CapturedImagesWrapper> getListOfPanoramaImages() {
		return listOfPanoramaImages;
	}

	public void setListOfPanoramaImages(List<CapturedImagesWrapper> listOfPanoramaImages) {
		this.listOfPanoramaImages = listOfPanoramaImages;
	}

	public List<CapturedImagesWrapper> getListOfAntennaHeightImages() {
		return listOfAntennaHeightImages;
	}

	public void setListOfAntennaHeightImages(List<CapturedImagesWrapper> listOfAntennaHeightImages) {
		this.listOfAntennaHeightImages = listOfAntennaHeightImages;
	}

	public List<SiteInformationWrapper> getSiteInfoList() {
		return siteInfoList;
	}

	public void setSiteInfoList(List<SiteInformationWrapper> siteInfoList) {
		this.siteInfoList = siteInfoList;
	}

	public List<CapturedImagesWrapper> getListOfMTiltImages() {
		return listOfMTiltImages;
	}

	public void setListOfMTiltImages(List<CapturedImagesWrapper> listOfMTiltImages) {
		this.listOfMTiltImages = listOfMTiltImages;
	}

	
}

package com.inn.foresight.module.nv.sitesuggestion.wrapper;

import java.util.Date;

import javax.persistence.Column;

import com.inn.core.generic.wrapper.RestWrapper;

@RestWrapper
public class FriendlySiteSuggestionWrapper {
	
	private String gridId;
	private String deviceId;
	private Double latitude;
	private Double longitude;
	private String buildingName;
	private String siteType;
	private String buildingType;
	private String address;
	private String contactPersonName;
	private String contactPersonNumber;
	private Date creationTime;
	private Date modificationTime;	
    private String refferalName;	
	private String refferalContactNumber;	
	private String refferalEmailId;
	
	
	public String getGridId() {
		return gridId;
	}
	public void setGridId(String gridId) {
		this.gridId = gridId;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
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
	public String getBuildingName() {
		return buildingName;
	}
	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}
	public String getSiteType() {
		return siteType;
	}
	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}
	public String getBuildingType() {
		return buildingType;
	}
	public void setBuildingType(String buildingType) {
		this.buildingType = buildingType;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getContactPersonName() {
		return contactPersonName;
	}
	public void setContactPersonName(String contactPersonName) {
		this.contactPersonName = contactPersonName;
	}
	public String getContactPersonNumber() {
		return contactPersonNumber;
	}
	public void setContactPersonNumber(String contactPersonNumber) {
		this.contactPersonNumber = contactPersonNumber;
	}	
	
	public Date getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	public Date getModificationTime() {
		return modificationTime;
	}
	public void setModificationTime(Date modificationTime) {
		this.modificationTime = modificationTime;
	}	
	
	public String getRefferalName() {
		return refferalName;
	}
	public void setRefferalName(String refferalName) {
		this.refferalName = refferalName;
	}
	public String getRefferalContactNumber() {
		return refferalContactNumber;
	}
	public void setRefferalContactNumber(String refferalContactNumber) {
		this.refferalContactNumber = refferalContactNumber;
	}
	public String getRefferalEmailId() {
		return refferalEmailId;
	}
	public void setRefferalEmailId(String refferalEmailId) {
		this.refferalEmailId = refferalEmailId;
	}
	@Override
	public String toString() {
		return "FriendlySiteSuggestionWrapper [gridId=" + gridId + ", deviceId=" + deviceId + ", latitude=" + latitude
				+ ", longitude=" + longitude + ", buildingName=" + buildingName + ", siteType=" + siteType
				+ ", buildingType=" + buildingType + ", address=" + address + ", contactPersonName=" + contactPersonName
				+ ", contactPersonNumber=" + contactPersonNumber + ", creationTime=" + creationTime
				+ ", modificationTime=" + modificationTime + ", refferalName=" + refferalName
				+ ", refferalContactNumber=" + refferalContactNumber + ", refferalEmailId=" + refferalEmailId + "]";
	}	

}

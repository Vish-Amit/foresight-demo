package com.inn.foresight.module.nv.report.wrapper.inbuilding;

import java.util.List;

public class InBuildingReportWrapper {
    private  String fileName;
     private String date;
     private Double latitude;
     private Double longitude;
     private String address;
     private String isFloor;
     private boolean pdschStatus;
     private boolean puschStatus;
	List<INBuildingSubWrapper>subList;

	
	public String getIsFloor() {
		return isFloor;
	}
	public void setIsFloor(String isFloor) {
		this.isFloor = isFloor;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public List<INBuildingSubWrapper> getSubList() {
		return subList;
	}
	public void setSubList(List<INBuildingSubWrapper> subList) {
		this.subList = subList;
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public boolean isPdschStatus() {
		return pdschStatus;
	}
	public void setPdschStatus(boolean pdschStatus) {
		this.pdschStatus = pdschStatus;
	}
	public boolean isPuschStatus() {
		return puschStatus;
	}
	public void setPuschStatus(boolean puschStatus) {
		this.puschStatus = puschStatus;
	}
	
}

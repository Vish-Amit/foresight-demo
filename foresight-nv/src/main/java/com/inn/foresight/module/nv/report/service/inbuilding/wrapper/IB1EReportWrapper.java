package com.inn.foresight.module.nv.report.service.inbuilding.wrapper;

import java.util.List;

import com.inn.foresight.module.nv.report.service.Inbuilding.wrapper.InbuildingWrapper;

public class IB1EReportWrapper {
	private String city;
	private String buildingId;
	private String buildingName;
	private String floorName;
	private String technology;
	private String testDate;
	private String latitude;
	private String longitude;
	private String mccmnc;
	private String earfcn;
	private String tac;
private List<InbuildingWrapper>floorWiseList;

public List<InbuildingWrapper> getFloorWiseList() {
	return floorWiseList;
}

public void setFloorWiseList(List<InbuildingWrapper> floorWiseList) {
	this.floorWiseList = floorWiseList;
}

public String getCity() {
	return city;
}

public void setCity(String city) {
	this.city = city;
}

public String getBuildingId() {
	return buildingId;
}

public void setBuildingId(String buildingId) {
	this.buildingId = buildingId;
}

public String getBuildingName() {
	return buildingName;
}

public void setBuildingName(String buildingName) {
	this.buildingName = buildingName;
}

public String getFloorName() {
	return floorName;
}

public void setFloorName(String floorName) {
	this.floorName = floorName;
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


public String getLatitude() {
	return latitude;
}

public void setLatitude(String latitude) {
	this.latitude = latitude;
}

public String getLongitude() {
	return longitude;
}

public void setLongitude(String longitude) {
	this.longitude = longitude;
}

public String getMccmnc() {
	return mccmnc;
}

public void setMccmnc(String mccmnc) {
	this.mccmnc = mccmnc;
}

public String getEarfcn() {
	return earfcn;
}

public void setEarfcn(String earfcn) {
	this.earfcn = earfcn;
}

public String getTac() {
	return tac;
}

public void setTac(String tac) {
	this.tac = tac;
}

@Override
public String toString() {
	return "IB1EReportWrapper [city=" + city + ", buildingId=" + buildingId + ", buildingName=" + buildingName
			+ ", floorName=" + floorName + ", technology=" + technology + ", testDate=" + testDate + ", floorWiseList="
			+ floorWiseList + "]";
}


}

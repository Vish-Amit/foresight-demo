package com.inn.foresight.module.nv.device.wrapper;

import java.util.List;

import com.inn.core.generic.wrapper.RestWrapper;
import com.inn.foresight.module.nv.app.model.APKDetail.APP_OS;

@RestWrapper
public class DeviceDashboardWrapper {

	private String deviceId;
	private Integer totalTest;
	private Double consumption;
	private String badge;
	private Double dlThroughput;
	private Double ulThroughput;
	private String make;
	private String model;
	private APP_OS deviceOS;
	private Long timeStamp;
	private String apkId;
	private String versionName;
	private Double latitude;
	private Double longitude;
	private List<APP_OS> deviceOSList;
	private List<String> badgeList;

	private Integer mcc;
	private Integer mnc;
	private String operator;
	private Integer cellServed;
	private String badgeFormula;
	private Integer rank;
	
	
	
	
	public DeviceDashboardWrapper() {
	}
	
	
	public DeviceDashboardWrapper(String deviceId, Double consumption,Integer totalTest, String badge,
			Integer cellServed, Integer rank) {
		super();
		this.deviceId = deviceId;
		this.consumption = consumption;
		this.totalTest = totalTest;
		this.badge = badge;
		this.cellServed = cellServed;
		this.rank = rank;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public String getBadgeFormula() {
		return badgeFormula;
	}

	public void setBadgeFormula(String badgeFormula) {
		this.badgeFormula = badgeFormula;
	}
	
	public Integer getCellServed() {
		return cellServed;
	}

	public void setCellServed(Integer cellServed) {
		this.cellServed = cellServed;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Integer getMcc() {
		return mcc;
	}

	public void setMcc(Integer mcc) {
		this.mcc = mcc;
	}

	public Integer getMnc() {
		return mnc;
	}

	public void setMnc(Integer mnc) {
		this.mnc = mnc;
	}

	public List<APP_OS> getDeviceOSList() {
		return deviceOSList;
	}

	public void setDeviceOSList(List<APP_OS> deviceOSList) {
		this.deviceOSList = deviceOSList;
	}

	public List<String> getBadgeList() {
		return badgeList;
	}

	public void setBadgeList(List<String> badgeList) {
		this.badgeList = badgeList;
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

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}


	public String getApkId() {
		return apkId;
	}

	public void setApkId(String apkId) {
		this.apkId = apkId;
	}

	public Long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Integer getTotalTest() {
		return totalTest;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	
	public APP_OS getDeviceOS() {
		return deviceOS;
	}

	public void setDeviceOS(APP_OS deviceOS) {
		this.deviceOS = deviceOS;
	}

	public void setTotalTest(Integer totalTest) {
		this.totalTest = totalTest;
	}

	public Double getConsumption() {
		return consumption;
	}

	public void setConsumption(Double consumption) {
		this.consumption = consumption;
	}

	public String getBadge() {
		return badge;
	}

	public void setBadge(String badge) {
		this.badge = badge;
	}

	public Double getDlThroughput() {
		return dlThroughput;
	}

	public void setDlThroughput(Double dlThroughput) {
		this.dlThroughput = dlThroughput;
	}

	public Double getUlThroughput() {
		return ulThroughput;
	}

	public void setUlThroughput(Double ulThroughput) {
		this.ulThroughput = ulThroughput;
	}

	@Override
	public String toString() {
		return "DeviceDashboardWrapper [deviceId=" + deviceId + ", totalTest=" + totalTest + ", consumption="
				+ consumption + ", badge=" + badge + ", dlThroughput=" + dlThroughput + ", ulThroughput=" + ulThroughput
				+ ", make=" + make + ", model=" + model + ", deviceOS=" + deviceOS + ", timeStamp=" + timeStamp
				+ ", apkId=" + apkId + ", versionName=" + versionName + ", latitude=" + latitude + ", longitude="
				+ longitude + ", deviceOSList=" + deviceOSList + ", badgeList=" + badgeList + ", mcc=" + mcc + ", mnc="
				+ mnc + ", operator=" + operator + ", cellServed=" + cellServed + ", badgeFormula=" + badgeFormula
				+ ", rank=" + rank + "]";
	}

	

	
	
	

	
	

}

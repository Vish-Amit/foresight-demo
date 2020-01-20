package com.inn.foresight.module.nv.dashboard.wifi.wrapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.inn.core.generic.wrapper.RestWrapper;

@JsonInclude(JsonInclude.Include.NON_NULL)
@RestWrapper
public class APDetailWrapper {

	private String floorNumber;

	private Integer floorId;

	private String apName;

	

	private String wingName;

	private Long apCount;

	private String ipAddress;

	private String apStatus;

	private String serialNumber;

	private Double latitude;
	private Double longitude;
	
	private Integer channel;
	
	public String getApName() {
		return apName;
	}

	public Integer getChannel() {
		return channel;
	}

	public void setChannel(Integer channel) {
		this.channel = channel;
	}

	public void setApName(String apName) {
		this.apName = apName;
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

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	private String macAddress;

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getApStatus() {
		return apStatus;
	}

	public void setApStatus(String apStatus) {
		this.apStatus = apStatus;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public String getFloorNumber() {
		return floorNumber;
	}

	public void setFloorNumber(String floorNumber) {
		this.floorNumber = floorNumber;
	}

	public Integer getFloorId() {
		return floorId;
	}

	public void setFloorId(Integer floorId) {
		this.floorId = floorId;
	}

	public String getWingName() {
		return wingName;
	}

	public void setWingName(String wingName) {
		this.wingName = wingName;
	}

	public Long getApCount() {
		return apCount;
	}

	public void setApCount(Long apCount) {
		this.apCount = apCount;
	}

	@Override
	public String toString() {
		return "APDetailWrapper [floorNumber=" + floorNumber + ", floorId=" + floorId + ", apName=" + apName
				+ ", wingName=" + wingName + ", apCount=" + apCount + ", ipAddress=" + ipAddress + ", apStatus="
				+ apStatus + ", serialNumber=" + serialNumber + ", latitude=" + latitude + ", longitude=" + longitude
				+ ", channel=" + channel + ", macAddress=" + macAddress + "]";
	}

	

	

}

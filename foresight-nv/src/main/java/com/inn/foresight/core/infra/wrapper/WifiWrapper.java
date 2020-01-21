package com.inn.foresight.core.infra.wrapper;

import com.inn.commons.lang.CommonUtils;
import com.inn.core.generic.wrapper.JpaWrapper;
import com.inn.core.generic.wrapper.RestWrapper;
import com.inn.foresight.core.infra.utils.enums.NEStatus;

/**
 * The Class WifiWrapper.
 */
@RestWrapper
@JpaWrapper
public class WifiWrapper {
	
	private String neId;
	private String deviceName;
	private String status;
	private Double latitude;
	private Double longitude;
	private String channel;
	private String serial;
	private String ip;
	private String port;
	private String address;
	
	public WifiWrapper() {
		super();
	}
	
	

	/** getWifiDetailByNEId **/
	public WifiWrapper(String neId, String deviceName, String port, String ip, String channel, String serial, NEStatus status, Double latitude,
			Double longitude,String address) {
		super();
		this.neId = neId;
		this.port = port;
		this.ip = ip;
		this.serial = serial;
		this.deviceName = deviceName;
		this.channel = channel;
		if (CommonUtils.isNoneNull(status)) {
			this.status = status.displayName();
		} 
		this.latitude = latitude;
		this.longitude = longitude;
		this.address = address;
	}
	
 

	public WifiWrapper(String neId, String deviceName, NEStatus status, Double latitude, Double longitude, Integer channel, String ip) {
		super();
		this.neId = neId;
		this.deviceName = deviceName;
		if (CommonUtils.isNoneNull(status)) {
			this.status = status.displayName();
		} 
		this.latitude = latitude;
		this.longitude = longitude;
		if(channel!=null) {
		this.channel = String.valueOf(channel);
		}
		this.ip = ip;
	}



	public String getNeId() {
		return neId;
	}

	public void setNeId(String neId) {
		this.neId = neId;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("WifiWrapper [neId=");
		builder.append(neId);
		builder.append(", deviceName=");
		builder.append(deviceName);
		builder.append(", status=");
		builder.append(status);
		builder.append(", latitude=");
		builder.append(latitude);
		builder.append(", longitude=");
		builder.append(longitude);
		builder.append(", channel=");
		builder.append(channel);
		builder.append(", serial=");
		builder.append(serial);
		builder.append(", ip=");
		builder.append(ip);
		builder.append(", port=");
		builder.append(port);
		builder.append(", address=");
		builder.append(address);
		builder.append("]");
		return builder.toString();
	}
	
}

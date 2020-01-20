package com.inn.foresight.module.nv.webrtc.wrapper;

public class WebRTCViberLayerWrapper {

	String grid;
	String deviceId;
	String callToken;
	String networkType;
	Double packetLoss;
	Double mosValue;
	String osName;
	String releaseType;
	String callDirection;
	String mediaType;
	long startTime;
	long endTime;
	String mosType;

	public String getGrid() {
		return grid;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public String getMosType() {
		return mosType;
	}

	public void setMosType(String mosType) {
		this.mosType = mosType;
	}

	public void setGrid(String grid) {
		this.grid = grid;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getCallToken() {
		return callToken;
	}

	public void setCallToken(String callToken) {
		this.callToken = callToken;
	}

	public String getNetworkType() {
		return networkType;
	}

	public void setNetworkType(String networkType) {
		this.networkType = networkType;
	}

	public Double getPacketLoss() {
		return packetLoss;
	}

	public void setPacketLoss(Double packetLoss) {
		this.packetLoss = packetLoss;
	}

	public Double getMosValue() {
		return mosValue;
	}

	public void setMosValue(Double mosValue) {
		this.mosValue = mosValue;
	}

	public String getOsName() {
		return osName;
	}

	public void setOsName(String osName) {
		this.osName = osName;
	}

	public String getReleaseType() {
		return releaseType;
	}

	public void setReleaseType(String releaseType) {
		this.releaseType = releaseType;
	}

	public String getCallDirection() {
		return callDirection;
	}

	public void setCallDirection(String callDirection) {
		this.callDirection = callDirection;
	}

	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

}

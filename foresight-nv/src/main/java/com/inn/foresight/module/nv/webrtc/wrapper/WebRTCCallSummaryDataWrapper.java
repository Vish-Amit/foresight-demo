package com.inn.foresight.module.nv.webrtc.wrapper;

public class WebRTCCallSummaryDataWrapper {

	String callType;

	String date;

	String mosType;

	String mediaType;
	
	String callReleaseType;

	String codecType;

	String networkType;

	Double avgMos;

	Double latitude;

	Double longitude;

	Long callStartTime;

	Long callEndTime;

	Double avgRtt;

	String mType;

	String callToken;
	
	
	public String getmType() {
		return mType;
	}

	public void setmType(String mType) {
		this.mType = mType;
	}

	public String getCallType() {
		return callType;
	}

	public void setCallType(String callType) {
		this.callType = callType;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getMosType() {
		return mosType;
	}

	public void setMosType(String mosType) {
		this.mosType = mosType;
	}


	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	public String getCodecType() {
		return codecType;
	}

	public void setCodecType(String codecType) {
		this.codecType = codecType;
	}

	public String getNetworkType() {
		return networkType;
	}

	public void setNetworkType(String networkType) {
		this.networkType = networkType;
	}

	public Double getAvgMos() {
		return avgMos;
	}

	public void setAvgMos(Double avgMos) {
		this.avgMos = avgMos;
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

	public Long getCallStartTime() {
		return callStartTime;
	}

	public void setCallStartTime(Long callStartTime) {
		this.callStartTime = callStartTime;
	}

	public Long getCallEndTime() {
		return callEndTime;
	}

	public void setCallEndTime(Long callEndTime) {
		this.callEndTime = callEndTime;
	}

	public Double getAvgRtt() {
		return avgRtt;
	}

	public void setAvgRtt(Double avgRtt) {
		this.avgRtt = avgRtt;
	}

	public String getCallReleaseType() {
		return callReleaseType;
	}

	public void setCallReleaseType(String callReleaseType) {
		this.callReleaseType = callReleaseType;
	}

	public String getCallToken() {
		return callToken;
	}

	public void setCallToken(String callToken) {
		this.callToken = callToken;
	}

	@Override
	public String toString() {
		return "WebRTCCallSummaryDataWrapper [callType=" + callType + ", date=" + date + ", mosType=" + mosType
				+ ", mediaType=" + mediaType + ", callReleaseType=" + callReleaseType + ", codecType=" + codecType
				+ ", networkType=" + networkType + ", avgMos=" + avgMos + ", latitude=" + latitude + ", longitude="
				+ longitude + ", callStartTime=" + callStartTime + ", callEndTime=" + callEndTime + ", avgRtt=" + avgRtt
				+ ", mType=" + mType + ", callToken=" + callToken + "]";
	}

	
}

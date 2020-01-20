package com.inn.foresight.module.nv.bbm.utils.wrapper;

public class BBMDetailWrapper {

	private String make;
	private String model;
	private String time;
	private String deviceOs;
	/** Not exist. */
	private String bbmAppVersion;
	private String deviceId;
	private String networkType;
	private String operatorName;
	private String mcc;
	private String mnc;
	private String pci;
	/** Not exist. */
	private String cgi;
	private String rsrp;
	private String rsrq;
	private String rscp;
	private String sinr;
	private String rxLevel;
	private String jitter;
	private String latency;
	private String latitude;
	private String longitude;
	/** Not exist. */
	private String callStartTime;
	/** Not exist. */
	private String callEndTime;
	/** Not exist. */
	private String callDuration;
	/** Not exist. */
	private String releaseCause;
	/** Not exist. */
	private String callType;
	/** Not exist. */
	private String sessionThroughput;
	
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
	public String getDeviceOs() {
		return deviceOs;
	}
	public void setDeviceOs(String deviceOs) {
		this.deviceOs = deviceOs;
	}
	public String getBbmAppVersion() {
		return bbmAppVersion;
	}
	public void setBbmAppVersion(String bbmAppVersion) {
		this.bbmAppVersion = bbmAppVersion;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getNetworkType() {
		return networkType;
	}
	public void setNetworkType(String networkType) {
		this.networkType = networkType;
	}
	public String getOperatorName() {
		return operatorName;
	}
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	public String getMcc() {
		return mcc;
	}
	public void setMcc(String mcc) {
		this.mcc = mcc;
	}
	public String getMnc() {
		return mnc;
	}
	public void setMnc(String mnc) {
		this.mnc = mnc;
	}
	public String getPci() {
		return pci;
	}
	public void setPci(String pci) {
		this.pci = pci;
	}
	public String getCgi() {
		return cgi;
	}
	public void setCgi(String cgi) {
		this.cgi = cgi;
	}
	public String getRsrp() {
		return rsrp;
	}
	public void setRsrp(String rsrp) {
		this.rsrp = rsrp;
	}
	public String getRsrq() {
		return rsrq;
	}
	public void setRsrq(String rsrq) {
		this.rsrq = rsrq;
	}
	public String getRscp() {
		return rscp;
	}
	public void setRscp(String rscp) {
		this.rscp = rscp;
	}
	public String getSinr() {
		return sinr;
	}
	public void setSinr(String sinr) {
		this.sinr = sinr;
	}
	public String getRxLevel() {
		return rxLevel;
	}
	public void setRxLevel(String rxLevel) {
		this.rxLevel = rxLevel;
	}
	public String getJitter() {
		return jitter;
	}
	public void setJitter(String jitter) {
		this.jitter = jitter;
	}
	public String getLatency() {
		return latency;
	}
	public void setLatency(String latency) {
		this.latency = latency;
	}
	public String getCallStartTime() {
		return callStartTime;
	}
	public void setCallStartTime(String callStartTime) {
		this.callStartTime = callStartTime;
	}
	public String getCallEndTime() {
		return callEndTime;
	}
	public void setCallEndTime(String callEndTime) {
		this.callEndTime = callEndTime;
	}
	public String getCallDuration() {
		return callDuration;
	}
	public void setCallDuration(String callDuration) {
		this.callDuration = callDuration;
	}
	public String getReleaseCause() {
		return releaseCause;
	}
	public void setReleaseCause(String releaseCause) {
		this.releaseCause = releaseCause;
	}
	public String getCallType() {
		return callType;
	}
	public void setCallType(String callType) {
		this.callType = callType;
	}
	public String getSessionThroughput() {
		return sessionThroughput;
	}
	public void setSessionThroughput(String sessionThroughput) {
		this.sessionThroughput = sessionThroughput;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "BBMDetailWrapper [make=" + make + ", model=" + model + ", time=" + time + ", deviceOs=" + deviceOs + ", bbmAppVersion="
				+ bbmAppVersion + ", deviceId=" + deviceId + ", networkType=" + networkType + ", operatorName=" + operatorName + ", mcc=" + mcc
				+ ", mnc=" + mnc + ", pci=" + pci + ", cgi=" + cgi + ", rsrp=" + rsrp + ", rsrq=" + rsrq + ", rscp=" + rscp + ", sinr=" + sinr
				+ ", rxLevel=" + rxLevel + ", jitter=" + jitter + ", latency=" + latency + ", latitude=" + latitude + ", longitude=" + longitude
				+ ", callStartTime=" + callStartTime + ", callEndTime=" + callEndTime + ", callDuration=" + callDuration + ", releaseCause="
				+ releaseCause + ", callType=" + callType + ", sessionThroughput=" + sessionThroughput + "]";
	}
}
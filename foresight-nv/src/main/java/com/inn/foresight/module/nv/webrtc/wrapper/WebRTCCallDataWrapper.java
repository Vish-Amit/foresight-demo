package com.inn.foresight.module.nv.webrtc.wrapper;

import com.inn.core.generic.wrapper.RestWrapper;

@RestWrapper
public class WebRTCCallDataWrapper {

  	private Double rsrp;
	private Double sinr;
	private Double snr;
	private Double rssi;
	private Double rscp;
	private Double rxlevel;
	private Double bytesReceived;
	private Double bytesSent;
	private Double packetsRecieved;
	private Double packetsSent;
	private Double packetsLost;
	private Double latitude;
	private Double longitude;
	private String callEvent;
	private Long timestamp;

	private Double btrecvAS;
	private Double btrecvAR;
	private Double btrecvVR;
	private Double btrecvVS;

	private Double btSentAS;
	private Double btSentAR;
	private Double btSentVS;
	private Double btSentVR;
	
	private Double pcktRecvAS;
	private Double pcktRecvAR;
	private Double pcktRecvVS;
	private Double pcktRecvVR;
	
	private Double pcktSentAS; 
	private Double pcktSentAR;
	private Double pcktSentVS;
	private Double pcktSentVR;
	
	private Double avgPcktlost;
	private String vCallEvent;
	
	private Double mos;
	private Double avgRtt;
	private String vCallRelType;
	private String deviceId;
	private Long startTime;
	private Long endTime;
	private String mediaType;
	private String imsi;
	
	private String networkType;
	
	private Double pcktSentDiff;
	private Double pcktRecvDiff;
	private Double pcktLostDiff;
	private Double btSentDiff;
	private Double btRecvDiff;

	
	private Integer cgi;
	private Integer pci;
	private Integer psc;
	private Integer cellId;

	private Double rFactor;
	private String nvModule;
	
	public Integer getPsc() {
		return psc;
	}

	public void setPsc(Integer psc) {
		this.psc = psc;
	}

	public Integer getCellId() {
		return cellId;
	}

	public void setCellId(Integer cellId) {
		this.cellId = cellId;
	}

	public Integer getCgi() {
		return cgi;
	}

	public void setCgi(Integer cgi) {
		this.cgi = cgi;
	}

	public Integer getPci() {
		return pci;
	}

	public void setPci(Integer pci) {
		this.pci = pci;
	}
	
	public String getNetworkType() {
		return networkType;
	}

	public void setNetworkType(String networkType) {
		this.networkType = networkType;
	}

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Double getRsrp() {
		return rsrp;
	}

	public void setRsrp(Double rsrp) {
		this.rsrp = rsrp;
	}

	public Double getSinr() {
		return sinr;
	}

	public void setSinr(Double sinr) {
		this.sinr = sinr;
	}
	
	public Double getSnr() {
		return snr;
	}

	public void setSnr(Double snr) {
		this.snr = snr;
	}

	public Double getRssi() {
		return rssi;
	}

	public void setRssi(Double rssi) {
		this.rssi = rssi;
	}

	public Double getRscp() {
		return rscp;
	}

	public void setRscp(Double rscp) {
		this.rscp = rscp;
	}

	public Double getRxlevel() {
		return rxlevel;
	}

	public void setRxlevel(Double rxlevel) {
		this.rxlevel = rxlevel;
	}

	public Double getBytesReceived() {
		return bytesReceived;
	}

	public void setBytesReceived(Double bytesReceived) {
		this.bytesReceived = bytesReceived;
	}

	public Double getBytesSent() {
		return bytesSent;
	}

	public void setBytesSent(Double bytesSent) {
		this.bytesSent = bytesSent;
	}

	public Double getPacketsRecieved() {
		return packetsRecieved;
	}

	public void setPacketsRecieved(Double packetsRecieved) {
		this.packetsRecieved = packetsRecieved;
	}

	public Double getPacketsSent() {
		return packetsSent;
	}

	public void setPacketsSent(Double packetsSent) {
		this.packetsSent = packetsSent;
	}

	public Double getPacketsLost() {
		return packetsLost;
	}

	public void setPacketsLost(Double packetsLost) {
		this.packetsLost = packetsLost;
	}

	public String getCallEvent() {
		return callEvent;
	}

	public void setCallEvent(String string) { this.callEvent = string; }

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public Double getBtrecvAS() {
		return btrecvAS;
	}

	public void setBtrecvAS(Double btrecvAS) {
		this.btrecvAS = btrecvAS;
	}

	public Double getBtrecvAR() {
		return btrecvAR;
	}

	public void setBtrecvAR(Double btrecvAR) {
		this.btrecvAR = btrecvAR;
	}

	public Double getBtrecvVR() {
		return btrecvVR;
	}

	public void setBtrecvVR(Double btrecvVR) {
		this.btrecvVR = btrecvVR;
	}

	public Double getBtrecvVS() {
		return btrecvVS;
	}

	public void setBtrecvVS(Double btrecvVS) {
		this.btrecvVS = btrecvVS;
	}

	public Double getBtSentAS() {
		return btSentAS;
	}

	public void setBtSentAS(Double btSentAS) {
		this.btSentAS = btSentAS;
	}

	public Double getBtSentAR() {
		return btSentAR;
	}

	public void setBtSentAR(Double btSentAR) {
		this.btSentAR = btSentAR;
	}

	public Double getBtSentVS() {
		return btSentVS;
	}

	public void setBtSentVS(Double btSentVS) {
		this.btSentVS = btSentVS;
	}

	public Double getBtSentVR() {
		return btSentVR;
	}

	public void setBtSentVR(Double btSentVR) {
		this.btSentVR = btSentVR;
	}

	public Double getPcktRecvAS() {
		return pcktRecvAS;
	}

	public void setPcktRecvAS(Double pcktRecvAS) {
		this.pcktRecvAS = pcktRecvAS;
	}

	public Double getPcktRecvAR() {
		return pcktRecvAR;
	}

	public void setPcktRecvAR(Double pcktRecvAR) {
		this.pcktRecvAR = pcktRecvAR;
	}

	public Double getPcktRecvVS() {
		return pcktRecvVS;
	}

	public void setPcktRecvVS(Double pcktRecvVS) {
		this.pcktRecvVS = pcktRecvVS;
	}

	public Double getPcktRecvVR() {
		return pcktRecvVR;
	}

	public void setPcktRecvVR(Double pcktRecvVR) {
		this.pcktRecvVR = pcktRecvVR;
	}

	public Double getPcktSentAS() {
		return pcktSentAS;
	}

	public void setPcktSentAS(Double pcktSentAS) {
		this.pcktSentAS = pcktSentAS;
	}

	public Double getPcktSentAR() {
		return pcktSentAR;
	}

	public void setPcktSentAR(Double pcktSentAR) {
		this.pcktSentAR = pcktSentAR;
	}

	public Double getPcktSentVS() {
		return pcktSentVS;
	}

	public void setPcktSentVS(Double pcktSentVS) {
		this.pcktSentVS = pcktSentVS;
	}

	public Double getPcktSentVR() {
		return pcktSentVR;
	}

	public void setPcktSentVR(Double pcktSentVR) {
		this.pcktSentVR = pcktSentVR;
	}

	public Double getAvgPcktlost() {
		return avgPcktlost;
	}

	public void setAvgPcktlost(Double avgPcktlost) {
		this.avgPcktlost = avgPcktlost;
	}

	public String getvCallEvent() {
		return vCallEvent;
	}

	public void setvCallEvent(String vCallEvent) {
		this.vCallEvent = vCallEvent;
	}

	public Double getMos() {
		return mos;
	}

	public void setMos(Double mos) {
		this.mos = mos;
	}

	public Double getAvgRtt() {
		return avgRtt;
	}

	public void setAvgRtt(Double avgRtt) {
		this.avgRtt = avgRtt;
	}

	public String getvCallRelType() {
		return vCallRelType;
	}

	public void setvCallRelType(String vCallRelType) {
		this.vCallRelType = vCallRelType;
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

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}
	

	public Double getPcktSentDiff() {
		return pcktSentDiff;
	}

	public void setPcktSentDiff(Double pcktSentDiff) {
		this.pcktSentDiff = pcktSentDiff;
	}

	public Double getPcktRecvDiff() {
		return pcktRecvDiff;
	}

	public void setPcktRecvDiff(Double pcktRecvDiff) {
		this.pcktRecvDiff = pcktRecvDiff;
	}

	public Double getPcktLostDiff() {
		return pcktLostDiff;
	}

	public void setPcktLostDiff(Double pcktLostDiff) {
		this.pcktLostDiff = pcktLostDiff;
	}

	public Double getBtSentDiff() {
		return btSentDiff;
	}

	public void setBtSentDiff(Double btSentDiff) {
		this.btSentDiff = btSentDiff;
	}

	public Double getBtRecvDiff() {
		return btRecvDiff;
	}

	public void setBtRecvDiff(Double btRecvDiff) {
		this.btRecvDiff = btRecvDiff;
	}

	
	public Double getrFactor() {
		return rFactor;
	}

	public void setrFactor(Double rFactor) {
		this.rFactor = rFactor;
	}

	public String getNvModule() {
		return nvModule;
	}

	public void setNvModule(String nvModule) {
		this.nvModule = nvModule;
	}

	@Override
	public String toString() {
		return "WebRTCCallDataWrapper{" +
				"rsrp=" + rsrp +
				", sinr=" + sinr +
				", snr=" + snr +
				", rssi=" + rssi +
				", rscp=" + rscp +
				", rxlevel=" + rxlevel +
				", bytesReceived=" + bytesReceived +
				", bytesSent=" + bytesSent +
				", packetsRecieved=" + packetsRecieved +
				", packetsSent=" + packetsSent +
				", packetsLost=" + packetsLost +
				", latitude=" + latitude +
				", longitude=" + longitude +
				", callEvent='" + callEvent + '\'' +
				", timestamp=" + timestamp +
				", btrecvAS=" + btrecvAS +
				", btrecvAR=" + btrecvAR +
				", btrecvVR=" + btrecvVR +
				", btrecvVS=" + btrecvVS +
				", btSentAS=" + btSentAS +
				", btSentAR=" + btSentAR +
				", btSentVS=" + btSentVS +
				", btSentVR=" + btSentVR +
				", pcktRecvAS=" + pcktRecvAS +
				", pcktRecvAR=" + pcktRecvAR +
				", pcktRecvVS=" + pcktRecvVS +
				", pcktRecvVR=" + pcktRecvVR +
				", pcktSentAS=" + pcktSentAS +
				", pcktSentAR=" + pcktSentAR +
				", pcktSentVS=" + pcktSentVS +
				", pcktSentVR=" + pcktSentVR +
				", avgPcktlost=" + avgPcktlost +
				", vCallEvent='" + vCallEvent + '\'' +
				", mos=" + mos +
				", avgRtt=" + avgRtt +
				", vCallRelType='" + vCallRelType + '\'' +
				", deviceId='" + deviceId + '\'' +
				", startTime=" + startTime +
				", endTime=" + endTime +
				", mediaType='" + mediaType + '\'' +
				", imsi='" + imsi + '\'' +
				", networkType='" + networkType + '\'' +
				", pcktSentDiff=" + pcktSentDiff +
				", pcktRecvDiff=" + pcktRecvDiff +
				", pcktLostDiff=" + pcktLostDiff +
				", btSentDiff=" + btSentDiff +
				", btRecvDiff=" + btRecvDiff +
				", cgi=" + cgi +
				", pci=" + pci +
				", psc=" + psc +
				", cellId=" + cellId +
				", rFactor=" + rFactor +
				", nvModule='" + nvModule + '\'' +
				'}';
	}
}

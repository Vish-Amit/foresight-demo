package com.inn.foresight.module.nv.webrtc.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ViberLayer implements Serializable {

	private static final long serialVersionUID = 8881554147821659844L;

	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "viberlayerid_pk")
	private Integer id;

	@Column(name = "latitude")
	Double latitude;
	@Column(name = "longitude")
	Double longitude;

	@Column(name = "tileidgeographyl1")
	String tileIdGeographyL1;

	@Column(name = "tileidgeographyl2")
	String tileIdGeographyL2;

	@Column(name = "tileidgeographyl3")
	String tileIdGeographyL3;

	@Column(name = "tileidgeographyl4")
	String tileIdGeographyL4;

	@Column(name = "deviceid")
	String deviceId;

	@Column(name = "calltoken")
	String callToken;

	@Column(name = "networktype")
	String networkType;

	@Column(name = "packetloss")
	Double packetLoss;

	@Column(name = "mos")
	Double mosValue;

	@Column(name = "operatingsystem")
	String osName;
	/** Normal drop. */
	@Column(name = "releasetype")
	String releaseType;
	/** Incoming or Outgoing. */
	@Column(name = "calltype")
	String callDirection;

	/** Audio or video. */
	@Column(name = "mediatype")
	String mediaType;

	@Column(name = "starttime")
	Long startTime;

	@Column(name = "endtime")
	Long endTime;

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getTileIdGeographyL1() {
		return tileIdGeographyL1;
	}

	public void setTileIdGeographyL1(String tileIdGeographyL1) {
		this.tileIdGeographyL1 = tileIdGeographyL1;
	}

	public String getTileIdGeographyL2() {
		return tileIdGeographyL2;
	}

	public void setTileIdGeographyL2(String tileIdGeographyL2) {
		this.tileIdGeographyL2 = tileIdGeographyL2;
	}

	public String getTileIdGeographyL3() {
		return tileIdGeographyL3;
	}

	public void setTileIdGeographyL3(String tileIdGeographyL3) {
		this.tileIdGeographyL3 = tileIdGeographyL3;
	}

	public String getTileIdGeographyL4() {
		return tileIdGeographyL4;
	}

	public void setTileIdGeographyL4(String tileIdGeographyL4) {
		this.tileIdGeographyL4 = tileIdGeographyL4;
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

	public void setCallDirection(String callType) {
		this.callDirection = callType;
	}

	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}

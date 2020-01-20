package com.inn.foresight.module.nv.webrtc.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ViberSubscriber implements Serializable{
	
	private static final long serialVersionUID = 1478404786116799058L;
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "vibersubscriberid_pk")
	private Integer id;

	@Column(name = "audiouc")
	Long audioUserCount;
	
	@Column(name = "videouc")
	Long videoUserCount;
	
	@Column(name = "switchuc")
	Long mixedUserCount;
	
	@Column(name = "totaluc")
	Long totalUserCount;
	
	@Column(name = "country")
	String countryName;
	
	@Column(name = "technology")
	String technology;
	
	@Column(name = "operator")
	String operatorName;
	
	@Column(name = "creationdate")
	Date creationDate;

	@Column(name = "nvmodule")
	String nvModule;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Long getAudioUserCount() {
		return audioUserCount;
	}

	public void setAudioUserCount(Long audioUserCount) {
		this.audioUserCount = audioUserCount;
	}

	public Long getVideoUserCount() {
		return videoUserCount;
	}

	public void setVideoUserCount(Long videoUserCount) {
		this.videoUserCount = videoUserCount;
	}

	public Long getMixedUserCount() {
		return mixedUserCount;
	}

	public void setMixedUserCount(Long mixedUserCount) {
		this.mixedUserCount = mixedUserCount;
	}

	public Long getTotalUserCount() {
		return totalUserCount;
	}

	public void setTotalUserCount(Long totalUserCount) {
		this.totalUserCount = totalUserCount;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getTechnology() {
		return technology;
	}

	public void setTechnology(String technology) {
		this.technology = technology;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getNvModule() { return nvModule;	}

	public void setNvModule(String nvModule) { this.nvModule = nvModule; }

	@Override
	public String toString() {
		return "ViberSubscriber{" +
				"id=" + id +
				", audioUserCount=" + audioUserCount +
				", videoUserCount=" + videoUserCount +
				", mixedUserCount=" + mixedUserCount +
				", totalUserCount=" + totalUserCount +
				", countryName='" + countryName + '\'' +
				", technology='" + technology + '\'' +
				", operatorName='" + operatorName + '\'' +
				", creationDate=" + creationDate +
				", nvModule='" + nvModule + '\'' +
				'}';
	}
}

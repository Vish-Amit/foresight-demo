package com.inn.foresight.module.nv.webrtc.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ViberDashboard implements Serializable{
	private static final long serialVersionUID = 1088089440029039646L;
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "viberdashboardid_pk")
	private Integer id;
	
	@Column(name = "audiouc")
	Long audioUc;
	@Column(name = "videouc")
	Long videoUc;
	@Column(name = "switchuc")
	Long mixedUc;
	@Column(name = "audiocall")
	Long audioCall;
	@Column(name = "videocall")
	Long videoCall;
	@Column(name = "switchcall")
	Long mixedCall;
	@Column(name = "audiodropcall")
	Long audioDropCall;
	@Column(name = "videodropcall")
	Long videoDropCall;
	@Column(name = "switchdropcall")
	Long mixedDropCall;
	@Column(name = "audiosuccesscall")
	Long audioSuccessCall;
	@Column(name = "videosuccesscall")
	Long videoSuccessCall;
	@Column(name = "switchsuccesscall")
	Long mixedSuccessCall;
	@Column(name = "avgmosaudio")
	Double avgMosAudio;
	@Column(name = "avgmosvideo")
	Double avgMosVideo;
	@Column(name = "avgmosswitch")
	Double avgMosMixed;
	@Column(name = "technology")
	String technology;
	@Column(name = "nvmodule")
	String nvModule;
	@Column(name = "creationdate")
	Date creationDate;
	@Column(name = "devicedistribution")
	String deviceDistribution;
	@Column(name = "avgpacketloss")
	Double avgPacketLoss;
	@Column(name = "avgjitter")
	Double avgJitter;
	@Column(name = "avgrtt")
	Double avgRtt;
	@Column(name = "countryname")
	String countryName;
	@Column(name = "avgtotalmos")
	Double avgTotalMos;
	@Column(name = "operatorname")
	String operatorName;
	@Column(name = "rsrp")
	String rsrp;
	@Column(name = "rssi")
	String rssi;
	@Column(name = "sinr")
	String sinr;
	@Column(name = "snr")
	String snr;
	@Column(name = "rscp")
	String rscp;
	@Column(name = "rxlevel")
	String rxlevel;
	@Column(name = "audioioscnt")
	Long audioIosCount;
	@Column(name = "videoioscnt")
	Long videoIosCount;
	@Column(name = "switchioscnt")
	Long mixedIosCount;
	@Column(name = "audioandroidcnt")
	Long audioAndroidCount;
	@Column(name = "videoandroidcnt")
	Long videoAndroidCount;
	@Column(name = "switchandroidcnt")
	Long mixedAndroidCount;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Long getAudioUc() {
		return audioUc;
	}
	public void setAudioUc(Long audioUc) {
		this.audioUc = audioUc;
	}
	public Long getVideoUc() {
		return videoUc;
	}
	public void setVideoUc(Long videoUc) {
		this.videoUc = videoUc;
	}
	public Long getMixedUc() {
		return mixedUc;
	}
	public void setMixedUc(Long mixedUc) {
		this.mixedUc = mixedUc;
	}
	public Long getAudioCall() {
		return audioCall;
	}
	public void setAudioCall(Long audioCall) {
		this.audioCall = audioCall;
	}
	public Long getVideoCall() {
		return videoCall;
	}
	public void setVideoCall(Long videoCall) {
		this.videoCall = videoCall;
	}
	public Long getMixedCall() {
		return mixedCall;
	}
	public void setMixedCall(Long mixedCall) {
		this.mixedCall = mixedCall;
	}
	public Long getAudioDropCall() {
		return audioDropCall;
	}
	public void setAudioDropCall(Long audioDropCall) {
		this.audioDropCall = audioDropCall;
	}
	public Long getVideoDropCall() {
		return videoDropCall;
	}
	public void setVideoDropCall(Long videoDropCall) {
		this.videoDropCall = videoDropCall;
	}
	public Long getMixedDropCall() {
		return mixedDropCall;
	}
	public void setMixedDropCall(Long mixedDropCall) {
		this.mixedDropCall = mixedDropCall;
	}
	public Long getAudioSuccessCall() {
		return audioSuccessCall;
	}
	public void setAudioSuccessCall(Long audioSuccessCall) {
		this.audioSuccessCall = audioSuccessCall;
	}
	public Long getVideoSuccessCall() {
		return videoSuccessCall;
	}
	public void setVideoSuccessCall(Long videoSuccessCall) {
		this.videoSuccessCall = videoSuccessCall;
	}
	public Long getMixedSuccessCall() {
		return mixedSuccessCall;
	}
	public void setMixedSuccessCall(Long mixedSuccessCall) {
		this.mixedSuccessCall = mixedSuccessCall;
	}
	public Double getAvgMosAudio() {
		return avgMosAudio;
	}
	public void setAvgMosAudio(Double avgMosAudio) {
		this.avgMosAudio = avgMosAudio;
	}
	public Double getAvgMosVideo() {
		return avgMosVideo;
	}
	public void setAvgMosVideo(Double avgMosVideo) {
		this.avgMosVideo = avgMosVideo;
	}
	public Double getAvgMosMixed() {
		return avgMosMixed;
	}
	public void setAvgMosMixed(Double avgMosMixed) {
		this.avgMosMixed = avgMosMixed;
	}
	public String getTechnology() {
		return technology;
	}
	public void setTechnology(String technology) {
		this.technology = technology;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public String getDeviceDistribution() {
		return deviceDistribution;
	}
	public void setDeviceDistribution(String deviceDistribution) {
		this.deviceDistribution = deviceDistribution;
	}
	public Double getAvgPacketLoss() {
		return avgPacketLoss;
	}
	public void setAvgPacketLoss(Double avgPacketLoss) {
		this.avgPacketLoss = avgPacketLoss;
	}
	public Double getAvgJitter() {
		return avgJitter;
	}
	public void setAvgJitter(Double avgJitter) {
		this.avgJitter = avgJitter;
	}
	public Double getAvgRtt() {
		return avgRtt;
	}
	public void setAvgRtt(Double avgRtt) {
		this.avgRtt = avgRtt;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public Double getAvgTotalMos() {
		return avgTotalMos;
	}
	public void setAvgTotalMos(Double avgTotalMos) {
		this.avgTotalMos = avgTotalMos;
	}
	public String getOperatorName() {
		return operatorName;
	}
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	public String getRsrp() {
		return rsrp;
	}
	public void setRsrp(String rsrp) {
		this.rsrp = rsrp;
	}
	public String getRssi() {
		return rssi;
	}
	public void setRssi(String rssi) {
		this.rssi = rssi;
	}
	public String getSinr() {
		return sinr;
	}
	public void setSinr(String sinr) {
		this.sinr = sinr;
	}
	public String getSnr() {
		return snr;
	}
	public void setSnr(String snr) {
		this.snr = snr;
	}
	public String getRscp() {
		return rscp;
	}
	public void setRscp(String rscp) {
		this.rscp = rscp;
	}
	public String getRxlevel() {
		return rxlevel;
	}
	public void setRxlevel(String rxlevel) {
		this.rxlevel = rxlevel;
	}
	public Long getAudioIosCount() {
		return audioIosCount;
	}
	public void setAudioIosCount(Long audioIosCount) {
		this.audioIosCount = audioIosCount;
	}
	public Long getVideoIosCount() {
		return videoIosCount;
	}
	public void setVideoIosCount(Long videoIosCount) {
		this.videoIosCount = videoIosCount;
	}
	public Long getMixedIosCount() {
		return mixedIosCount;
	}
	public void setMixedIosCount(Long mixedIosCount) {
		this.mixedIosCount = mixedIosCount;
	}
	public Long getAudioAndroidCount() {
		return audioAndroidCount;
	}
	public void setAudioAndroidCount(Long audioAndroidCount) {
		this.audioAndroidCount = audioAndroidCount;
	}
	public Long getVideoAndroidCount() {
		return videoAndroidCount;
	}
	public void setVideoAndroidCount(Long videoAndroidCount) {
		this.videoAndroidCount = videoAndroidCount;
	}
	public Long getMixedAndroidCount() {
		return mixedAndroidCount;
	}
	public void setMixedAndroidCount(Long mixedAndroidCount) {
		this.mixedAndroidCount = mixedAndroidCount;
	}
	public String getNvModule() { return nvModule; }
	public void setNvModule(String nvModule) { this.nvModule = nvModule; }
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "ViberDashboard{" + "id=" + id +	", audioUc=" + audioUc + ", videoUc=" + videoUc + ", mixedUc=" + mixedUc +
				", audioCall=" + audioCall + ", videoCall=" + videoCall + ", mixedCall=" + mixedCall + ", audioDropCall=" + audioDropCall +
				", videoDropCall=" + videoDropCall + ", mixedDropCall=" + mixedDropCall + ", audioSuccessCall=" + audioSuccessCall +
				", videoSuccessCall=" + videoSuccessCall + ", mixedSuccessCall=" + mixedSuccessCall + ", avgMosAudio=" + avgMosAudio +
				", avgMosVideo=" + avgMosVideo + ", avgMosMixed=" + avgMosMixed + ", technology='" + technology + '\'' +
				", nvModule='" + nvModule + '\'' + ", creationDate=" + creationDate + ", deviceDistribution='" + deviceDistribution + '\'' +
				", avgPacketLoss=" + avgPacketLoss + ", avgJitter=" + avgJitter + ", avgRtt=" + avgRtt +
				", countryName='" + countryName + '\'' + ", avgTotalMos=" + avgTotalMos + ", operatorName='" + operatorName + '\'' +
				", rsrp='" + rsrp + '\'' + ", rssi='" + rssi + '\'' + ", sinr='" + sinr + '\'' + ", snr='" + snr + '\'' +
				", rscp='" + rscp + '\'' + ", rxlevel='" + rxlevel + '\'' + ", audioIosCount=" + audioIosCount +
				", videoIosCount=" + videoIosCount + ", mixedIosCount=" + mixedIosCount + ", audioAndroidCount=" + audioAndroidCount +
				", videoAndroidCount=" + videoAndroidCount + ", mixedAndroidCount=" + mixedAndroidCount + '}';
	}
}

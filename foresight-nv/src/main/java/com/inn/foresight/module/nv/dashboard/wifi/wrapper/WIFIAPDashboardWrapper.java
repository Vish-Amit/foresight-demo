package com.inn.foresight.module.nv.dashboard.wifi.wrapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.inn.core.generic.wrapper.RestWrapper;

@JsonInclude(JsonInclude.Include.NON_NULL)
@RestWrapper
public class WIFIAPDashboardWrapper {

	private Long totalUser;
	private Long totalSamples;
	private String rssi;
	private String snr;
	private String dl;
	private String ul;
	private String bufferTime;
	private String ttl;
	private String dns;
	private String latency;
	private String packetLoss;
	private Long apCount;
	private String apDist;
	private Integer networkElementid;
	private String performance;
	private String rssiPerformance;
	private String snrPerformance;
	private String apDistribution;
	private String hourlyUserDistribution;
	private String dlSpeed;
	private String hourlySampleDistribution;
	private String macAddress;
	private String apName;
	
	
	
	
	public String getApName() {
		return apName;
	}

	public void setApName(String apName) {
		this.apName = apName;
	}

	public String getDlSpeed() {
		return dlSpeed;
	}

	public void setDlSpeed(String dlSpeed) {
		this.dlSpeed = dlSpeed;
	}

	

	public String getHourlySampleDistribution() {
		return hourlySampleDistribution;
	}

	public void setHourlySampleDistribution(String hourlySampleDistribution) {
		this.hourlySampleDistribution = hourlySampleDistribution;
	}

	public String getHourlyUserDistribution() {
		return hourlyUserDistribution;
	}

	public void setHourlyUserDistribution(String hourlyUserDistribution) {
		this.hourlyUserDistribution = hourlyUserDistribution;
	}

	public String getRssiPerformance() {
		return rssiPerformance;
	}

	public void setRssiPerformance(String rssiPerformance) {
		this.rssiPerformance = rssiPerformance;
	}

	public String getSnrPerformance() {
		return snrPerformance;
	}

	public void setSnrPerformance(String snrPerformance) {
		this.snrPerformance = snrPerformance;
	}

	public String getApDistribution() {
		return apDistribution;
	}

	public void setApDistribution(String apDistribution) {
		this.apDistribution = apDistribution;
	}

	public String getPerformance() {
		return performance;
	}

	public void setPerformance(String performance) {
		this.performance = performance;
	}

	
	public Integer getNetworkElementid() {
		return networkElementid;
	}

	public void setNetworkElementid(Integer networkElementid) {
		this.networkElementid = networkElementid;
	}

	

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public Long getTotalUser() {
		return totalUser;
	}

	public void setTotalUser(Long totalUser) {
		this.totalUser = totalUser;
	}

	public Long getTotalSamples() {
		return totalSamples;
	}

	public void setTotalSamples(Long totalSamples) {
		this.totalSamples = totalSamples;
	}

	public String getRssi() {
		return rssi;
	}

	public void setRssi(String rssi) {
		this.rssi = rssi;
	}

	public String getSnr() {
		return snr;
	}

	public void setSnr(String snr) {
		this.snr = snr;
	}

	public String getDl() {
		return dl;
	}

	public void setDl(String dl) {
		this.dl = dl;
	}

	public String getUl() {
		return ul;
	}

	public void setUl(String ul) {
		this.ul = ul;
	}

	public String getBufferTime() {
		return bufferTime;
	}

	public void setBufferTime(String bufferTime) {
		this.bufferTime = bufferTime;
	}

	public String getTtl() {
		return ttl;
	}

	public void setTtl(String ttl) {
		this.ttl = ttl;
	}

	public String getDns() {
		return dns;
	}

	public void setDns(String dns) {
		this.dns = dns;
	}

	public String getLatency() {
		return latency;
	}

	public void setLatency(String latency) {
		this.latency = latency;
	}

	public String getPacketLoss() {
		return packetLoss;
	}

	public void setPacketLoss(String packetLoss) {
		this.packetLoss = packetLoss;
	}

	public Long getApCount() {
		return apCount;
	}

	public void setApCount(Long apCount) {
		this.apCount = apCount;
	}

	public String getApDist() {
		return apDist;
	}

	public void setApDist(String apDist) {
		this.apDist = apDist;
	}

	@Override
	public String toString() {
		return "WIFIAPDashboardWrapper [totalUser=" + totalUser + ", totalSamples=" + totalSamples + ", rssi=" + rssi
				+ ", snr=" + snr + ", dl=" + dl + ", ul=" + ul + ", bufferTime=" + bufferTime + ", ttl=" + ttl
				+ ", dns=" + dns + ", latency=" + latency + ", packetLoss=" + packetLoss + ", apCount=" + apCount
				+ ", apDist=" + apDist + ", networkElementid=" + networkElementid + ", performance=" + performance
				+ ", rssiPerformance=" + rssiPerformance + ", snrPerformance=" + snrPerformance + ", apDistribution="
				+ apDistribution + ", hourlyUserDistribution=" + hourlyUserDistribution + ", hourlySampleDistribution="
				+ hourlySampleDistribution + ", macAddress=" + macAddress + "]";
	}

}

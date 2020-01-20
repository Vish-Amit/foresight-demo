package com.inn.foresight.module.nv.dashboard.wrapper;

import com.inn.core.generic.wrapper.JpaWrapper;

/** The Class NVDashboardWrapper. */
@JpaWrapper
public class NVDashboardWrapper {

	/** The date. */
	private String date;

	/** The avg dl rate. */
	private Double avgDlRate;

	/** The max dl rate. */
	private Double maxDlRate;

	/** The avg ul rate. */
	private Double avgUlRate;

	/** The max ul rate. */
	private Double maxUlRate;

	/** The latency. */
	private Double latency;

	/** The jitter. */
	private Double jitter;

	/** The packet loss. */
	private Double packetLoss;

	/** The url 1 browse time. */
	private Double url1BrowseTime;

	/** The url 2 browse time. */
	private Double url2BrowseTime;

	/** The url 3 browse time. */
	private Double url3BrowseTime;

	/** The star rating. */
	private Double starRating;

	/** The coverage. */
	private Double coverage;

	/** The quality. */
	private Double quality;

	/** The sinr. */
	private Double sinr;

	/** The enterprise UC. */
	private Long enterpriseUC;

	/** The android UC. */
	private Long androidUC;

	/** The ios UC. */
	private Long iosUC;

	/** The active UC. */
	private Long activeUC;

	/** The passive UC. */
	private Long passiveUC;

	/** The consumer UC. */
	private Long consumerUC;

	/** The total UC. */
	private Long totalUC;

	/** Instantiates a new NV dashboard wrapper. */
	public NVDashboardWrapper() {

	}

	public NVDashboardWrapper(Long totaluc, Long androiduc, Long iosuc) {
		this.totalUC = totaluc;
		this.androidUC = androiduc;
		this.iosUC = iosuc;
	}

	/**
	 * Instantiates a new NV dashboard wrapper.
	 *
	 * @param enterpriseUC
	 *            the enterprise UC
	 * @param androidUC
	 *            the android UC
	 * @param iosUC
	 *            the ios UC
	 * @param activeUC
	 *            the active UC
	 * @param passiveUC
	 *            the passive UC
	 * @param consumerUC
	 *            the consumer UC
	 * @param totalUC
	 *            the total UC
	 */
	public NVDashboardWrapper(Long enterpriseUC, Long androidUC, Long iosUC, Long activeUC, Long passiveUC,
			Long consumerUC, Long totalUC) {
		super();
		this.enterpriseUC = enterpriseUC;
		this.androidUC = androidUC;
		this.iosUC = iosUC;
		this.activeUC = activeUC;
		this.passiveUC = passiveUC;
		this.consumerUC = consumerUC;
		this.totalUC = totalUC;
	}

	/**
	 * Instantiates a new NV dashboard wrapper.
	 *
	 * @param date
	 *            the date
	 * @param avgDlRate
	 *            the avg dl rate
	 * @param maxDlRate
	 *            the max dl rate
	 * @param avgUlRate
	 *            the avg ul rate
	 * @param maxUlRate
	 *            the max ul rate
	 * @param latency
	 *            the latency
	 * @param jitter
	 *            the jitter
	 * @param packetLoss
	 *            the packet loss
	 * @param url1BrowseTime
	 *            the url 1 browse time
	 * @param url2BrowseTime
	 *            the url 2 browse time
	 * @param url3BrowseTime
	 *            the url 3 browse time
	 * @param starRating
	 *            the star rating
	 * @param coverage
	 *            the coverage
	 * @param quality
	 *            the quality
	 * @param sinr
	 *            the sinr
	 * @param enterpriseUC
	 *            the enterprise UC
	 * @param androidUC
	 *            the android UC
	 * @param iosUC
	 *            the ios UC
	 * @param activeUC
	 *            the active UC
	 * @param passiveUC
	 *            the passive UC
	 * @param consumerUC
	 *            the consumer UC
	 * @param totalUC
	 *            the total UC
	 */
	public NVDashboardWrapper(String date, Double avgDlRate, Double maxDlRate, Double avgUlRate, Double maxUlRate,
			Double latency, Double jitter, Double packetLoss, Double url1BrowseTime, Double url2BrowseTime,
			Double url3BrowseTime, Double starRating, Double coverage, Double quality, Double sinr, Long enterpriseUC,
			Long androidUC, Long iosUC, Long activeUC, Long passiveUC, Long consumerUC, Long totalUC) {
		super();
		this.date = date;
		this.avgDlRate = avgDlRate;
		this.maxDlRate = maxDlRate;
		this.avgUlRate = avgUlRate;
		this.maxUlRate = maxUlRate;
		this.latency = latency;
		this.jitter = jitter;
		this.packetLoss = packetLoss;
		this.url1BrowseTime = url1BrowseTime;
		this.url2BrowseTime = url2BrowseTime;
		this.url3BrowseTime = url3BrowseTime;
		this.starRating = starRating;
		this.coverage = coverage;
		this.quality = quality;
		this.sinr = sinr;
		this.enterpriseUC = enterpriseUC;
		this.androidUC = androidUC;
		this.iosUC = iosUC;
		this.activeUC = activeUC;
		this.passiveUC = passiveUC;
		this.consumerUC = consumerUC;
		this.totalUC = totalUC;
	}

	/**
	 * Gets the date.
	 *
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * Sets the date.
	 *
	 * @param date
	 *            the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * Gets the avg dl rate.
	 *
	 * @return the avgDlRate
	 */
	public Double getAvgDlRate() {
		return avgDlRate;
	}

	/**
	 * Sets the avg dl rate.
	 *
	 * @param avgDlRate
	 *            the avgDlRate to set
	 */
	public void setAvgDlRate(Double avgDlRate) {
		this.avgDlRate = avgDlRate;
	}

	/**
	 * Gets the max dl rate.
	 *
	 * @return the maxDlRate
	 */
	public Double getMaxDlRate() {
		return maxDlRate;
	}

	/**
	 * Sets the max dl rate.
	 *
	 * @param maxDlRate
	 *            the maxDlRate to set
	 */
	public void setMaxDlRate(Double maxDlRate) {
		this.maxDlRate = maxDlRate;
	}

	/**
	 * Gets the avg ul rate.
	 *
	 * @return the avgUlRate
	 */
	public Double getAvgUlRate() {
		return avgUlRate;
	}

	/**
	 * Sets the avg ul rate.
	 *
	 * @param avgUlRate
	 *            the avgUlRate to set
	 */
	public void setAvgUlRate(Double avgUlRate) {
		this.avgUlRate = avgUlRate;
	}

	/**
	 * Gets the max ul rate.
	 *
	 * @return the maxUlRate
	 */
	public Double getMaxUlRate() {
		return maxUlRate;
	}

	/**
	 * Sets the max ul rate.
	 *
	 * @param maxUlRate
	 *            the maxUlRate to set
	 */
	public void setMaxUlRate(Double maxUlRate) {
		this.maxUlRate = maxUlRate;
	}

	/**
	 * Gets the latency.
	 *
	 * @return the latency
	 */
	public Double getLatency() {
		return latency;
	}

	/**
	 * Sets the latency.
	 *
	 * @param latency
	 *            the latency to set
	 */
	public void setLatency(Double latency) {
		this.latency = latency;
	}

	/**
	 * Gets the jitter.
	 *
	 * @return the jitter
	 */
	public Double getJitter() {
		return jitter;
	}

	/**
	 * Sets the jitter.
	 *
	 * @param jitter
	 *            the jitter to set
	 */
	public void setJitter(Double jitter) {
		this.jitter = jitter;
	}

	/**
	 * Gets the packet loss.
	 *
	 * @return the packetLoss
	 */
	public Double getPacketLoss() {
		return packetLoss;
	}

	/**
	 * Sets the packet loss.
	 *
	 * @param packetLoss
	 *            the packetLoss to set
	 */
	public void setPacketLoss(Double packetLoss) {
		this.packetLoss = packetLoss;
	}

	/**
	 * Gets the url 1 browse time.
	 *
	 * @return the url1BrowseTime
	 */
	public Double getUrl1BrowseTime() {
		return url1BrowseTime;
	}

	/**
	 * Sets the url 1 browse time.
	 *
	 * @param url1BrowseTime
	 *            the url1BrowseTime to set
	 */
	public void setUrl1BrowseTime(Double url1BrowseTime) {
		this.url1BrowseTime = url1BrowseTime;
	}

	/**
	 * Gets the url 2 browse time.
	 *
	 * @return the url2BrowseTime
	 */
	public Double getUrl2BrowseTime() {
		return url2BrowseTime;
	}

	/**
	 * Sets the url 2 browse time.
	 *
	 * @param url2BrowseTime
	 *            the url2BrowseTime to set
	 */
	public void setUrl2BrowseTime(Double url2BrowseTime) {
		this.url2BrowseTime = url2BrowseTime;
	}

	/**
	 * Gets the url 3 browse time.
	 *
	 * @return the url3BrowseTime
	 */
	public Double getUrl3BrowseTime() {
		return url3BrowseTime;
	}

	/**
	 * Sets the url 3 browse time.
	 *
	 * @param url3BrowseTime
	 *            the url3BrowseTime to set
	 */
	public void setUrl3BrowseTime(Double url3BrowseTime) {
		this.url3BrowseTime = url3BrowseTime;
	}

	/**
	 * Gets the star rating.
	 *
	 * @return the starRating
	 */
	public Double getStarRating() {
		return starRating;
	}

	/**
	 * Sets the star rating.
	 *
	 * @param starRating
	 *            the starRating to set
	 */
	public void setStarRating(Double starRating) {
		this.starRating = starRating;
	}

	/**
	 * Gets the enterprise UC.
	 *
	 * @return the enterpriseUC
	 */
	public Long getEnterpriseUC() {
		return enterpriseUC;
	}

	/**
	 * Sets the enterprise UC.
	 *
	 * @param enterpriseUC
	 *            the enterpriseUC to set
	 */
	public void setEnterpriseUC(Long enterpriseUC) {
		this.enterpriseUC = enterpriseUC;
	}

	/**
	 * Gets the android UC.
	 *
	 * @return the androidUC
	 */
	public Long getAndroidUC() {
		return androidUC;
	}

	/**
	 * Sets the android UC.
	 *
	 * @param androidUC
	 *            the androidUC to set
	 */
	public void setAndroidUC(Long androidUC) {
		this.androidUC = androidUC;
	}

	/**
	 * Gets the ios UC.
	 *
	 * @return the iosUC
	 */
	public Long getIosUC() {
		return iosUC;
	}

	/**
	 * Sets the ios UC.
	 *
	 * @param iosUC
	 *            the iosUC to set
	 */
	public void setIosUC(Long iosUC) {
		this.iosUC = iosUC;
	}

	/**
	 * Gets the active UC.
	 *
	 * @return the activeUC
	 */
	public Long getActiveUC() {
		return activeUC;
	}

	/**
	 * Sets the active UC.
	 *
	 * @param activeUC
	 *            the activeUC to set
	 */
	public void setActiveUC(Long activeUC) {
		this.activeUC = activeUC;
	}

	/**
	 * Gets the passive UC.
	 *
	 * @return the passiveUC
	 */
	public Long getPassiveUC() {
		return passiveUC;
	}

	/**
	 * Sets the passive UC.
	 *
	 * @param passiveUC
	 *            the passiveUC to set
	 */
	public void setPassiveUC(Long passiveUC) {
		this.passiveUC = passiveUC;
	}

	/**
	 * Gets the consumer UC.
	 *
	 * @return the consumerUC
	 */
	public Long getConsumerUC() {
		return consumerUC;
	}

	/**
	 * Sets the consumer UC.
	 *
	 * @param consumerUC
	 *            the consumerUC to set
	 */
	public void setConsumerUC(Long consumerUC) {
		this.consumerUC = consumerUC;
	}

	/**
	 * Gets the total UC.
	 *
	 * @return the totalUC
	 */
	public Long getTotalUC() {
		return totalUC;
	}

	/**
	 * Sets the total UC.
	 *
	 * @param totalUC
	 *            the totalUC to set
	 */
	public void setTotalUC(Long totalUC) {
		this.totalUC = totalUC;
	}

	/**
	 * Gets the coverage.
	 *
	 * @return the coverage
	 */
	public Double getCoverage() {
		return coverage;
	}

	/**
	 * Sets the coverage.
	 *
	 * @param coverage
	 *            the coverage to set
	 */
	public void setCoverage(Double coverage) {
		this.coverage = coverage;
	}

	/**
	 * Gets the quality.
	 *
	 * @return the quality
	 */
	public Double getQuality() {
		return quality;
	}

	/**
	 * Sets the quality.
	 *
	 * @param quality
	 *            the quality to set
	 */
	public void setQuality(Double quality) {
		this.quality = quality;
	}

	/**
	 * Gets the sinr.
	 *
	 * @return the sinr
	 */
	public Double getSinr() {
		return sinr;
	}

	/**
	 * Sets the sinr.
	 *
	 * @param sinr
	 *            the sinr to set
	 */
	public void setSinr(Double sinr) {
		this.sinr = sinr;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "NVDashboardWrapper [date=" + date + ", avgDlRate=" + avgDlRate + ", maxDlRate=" + maxDlRate
				+ ", avgUlRate=" + avgUlRate + ", maxUlRate=" + maxUlRate + ", latency=" + latency + ", jitter="
				+ jitter + ", packetLoss=" + packetLoss + ", url1BrowseTime=" + url1BrowseTime + ", url2BrowseTime="
				+ url2BrowseTime + ", url3BrowseTime=" + url3BrowseTime + ", starRating=" + starRating + ", coverage="
				+ coverage + ", quality=" + quality + ", sinr=" + sinr + ", enterpriseUC=" + enterpriseUC
				+ ", androidUC=" + androidUC + ", iosUC=" + iosUC + ", activeUC=" + activeUC + ", passiveUC="
				+ passiveUC + ", consumerUC=" + consumerUC + ", totalUC=" + totalUC + "]";
	}

}

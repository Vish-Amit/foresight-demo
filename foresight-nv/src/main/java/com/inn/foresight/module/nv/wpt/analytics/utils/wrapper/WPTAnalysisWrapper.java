package com.inn.foresight.module.nv.wpt.analytics.utils.wrapper;

/** The Class WPTAnalysisWrapper. */
public class WPTAnalysisWrapper {
	
	/** The date. */
	private String date;
	
	/** The avg TTFB. */
	private Double avgTTFB;
	
	/** The avg TDNS. */
	private Double avgTDNS;
	
	/** The avg FDNS. */
	private Double avgFDNS;

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
	 * @param date the new date
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * Gets the avg TTFB.
	 *
	 * @return the avg TTFB
	 */
	public Double getAvgTTFB() {
		return avgTTFB;
	}

	/**
	 * Sets the avg TTFB.
	 *
	 * @param avgTTFB the new avg TTFB
	 */
	public void setAvgTTFB(Double avgTTFB) {
		this.avgTTFB = avgTTFB;
	}

	/**
	 * Gets the avg TDNS.
	 *
	 * @return the avg TDNS
	 */
	public Double getAvgTDNS() {
		return avgTDNS;
	}

	/**
	 * Sets the avg TDNS.
	 *
	 * @param avgTDNS the new avg TDNS
	 */
	public void setAvgTDNS(Double avgTDNS) {
		this.avgTDNS = avgTDNS;
	}

	/**
	 * Gets the avg FDNS.
	 *
	 * @return the avg FDNS
	 */
	public Double getAvgFDNS() {
		return avgFDNS;
	}

	/**
	 * Sets the avg FDNS.
	 *
	 * @param avgFDNS the new avg FDNS
	 */
	public void setAvgFDNS(Double avgFDNS) {
		this.avgFDNS = avgFDNS;
	}
	
}

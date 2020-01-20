package com.inn.foresight.module.nv.wpt.analytics.utils.wrapper;

import com.inn.core.generic.wrapper.JpaWrapper;

/**
 * The Class WPTHTTPStatsWrapper.
 *
 * @author innoeye
 * date - 13-Dec-2017 6:24:58 PM
 */
@JpaWrapper
public class WPTHTTPStatsWrapper {
	
	/** The date. */
	private String date;
	
	/** The count. */
	private Long count;
	
	/** The is https. */
	private Boolean isHttps;	
	
	/**
	 * Instantiates a new WPTHTTP stats wrapper.
	 *
	 * @param date the date
	 * @param count the count
	 * @param isHttps the is https
	 */
	public WPTHTTPStatsWrapper(String date, Long count, Boolean isHttps) {
		super();
		this.date = date;
		this.count = count;
		this.isHttps = isHttps;
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
	 * @param date the new date
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * Gets the count.
	 *
	 * @return the count
	 */
	public Long getCount() {
		return count;
	}

	/**
	 * Sets the count.
	 *
	 * @param count the new count
	 */
	public void setCount(Long count) {
		this.count = count;
	}

	/**
	 * Gets the checks if is https.
	 *
	 * @return the checks if is https
	 */
	public Boolean getIsHttps() {
		return isHttps;
	}

	/**
	 * Sets the checks if is https.
	 *
	 * @param isHttps the new checks if is https
	 */
	public void setIsHttps(Boolean isHttps) {
		this.isHttps = isHttps;
	}

}

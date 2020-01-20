package com.inn.foresight.module.nv.wpt.analytics.utils.wrapper;

import com.inn.core.generic.wrapper.JpaWrapper;

/** The Class WPTDayWiseCountWrapper. */
@JpaWrapper
public class WPTDayWiseCountWrapper {

	/** The date. */
	private String date;
	
	/** The count. */
	private Long count;
	
	/**
	 * Instantiates a new WPT day wise count wrapper.
	 *
	 * @param date the date
	 * @param count the count
	 */
	public WPTDayWiseCountWrapper(String date, Long count) {
		this.date = date;
		this.count = count;
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
	
}

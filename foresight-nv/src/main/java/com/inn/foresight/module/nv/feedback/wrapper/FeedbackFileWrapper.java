package com.inn.foresight.module.nv.feedback.wrapper;

/** The Class FeedbackFileWrapper. */
public class FeedbackFileWrapper {
	

	/** The imei. */
	private String imei;
	
	/** The imsi. */
	private String imsi;
	
	/** The status. */
	private String status;
	
	/**
	 * Instantiates a new feedback file wrapper.
	 *
	 * @param imei the imei
	 * @param imsi the imsi
	 * @param status the status
	 */
	public FeedbackFileWrapper(String imei, String imsi, String status) {
		this.imei = imei;
		this.imsi = imsi;
		this.status = status;
	}
	
	/**
	 * Gets the imei.
	 *
	 * @return the imei
	 */
	public String getImei() {
		return imei;
	}

	/**
	 * Sets the imei.
	 *
	 * @param imei            the imei to set
	 */
	public void setImei(String imei) {
		this.imei = imei;
	}

	/**
	 * Gets the imsi.
	 *
	 * @return the imsi
	 */
	public String getImsi() {
		return imsi;
	}

	/**
	 * Sets the imsi.
	 *
	 * @param imsi            the imsi to set
	 */
	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Sets the status.
	 *
	 * @param status            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
}

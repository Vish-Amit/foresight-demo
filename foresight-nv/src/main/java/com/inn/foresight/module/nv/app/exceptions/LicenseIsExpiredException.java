package com.inn.foresight.module.nv.app.exceptions;


/**
 * The Class LicenseIsExpiredException.
 *
 * @author innoeye
 * date - 16-Nov-2017 6:32:13 PM
 */
public class LicenseIsExpiredException extends Exception {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new license is expired exception.
	 *
	 * @param e the e
	 */
	public LicenseIsExpiredException(Exception e) {
		super(e.getMessage(), e);
	}

	/**
	 * Instantiates a new license is expired exception.
	 *
	 * @param message the message
	 */
	public LicenseIsExpiredException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new license is expired exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public LicenseIsExpiredException(String message, Throwable cause) {
		super(message, cause);
	}
}

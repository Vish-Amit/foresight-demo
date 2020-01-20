package com.inn.foresight.module.nv.app.exceptions;


/**
 * The Class InvalidDeviceDetailException.
 *
 * @author innoeye
 * date - 17-Nov-2017 4:25:54 PM
 */
public class InvalidDeviceDetailException extends Exception {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new invalid device detail exception.
	 *
	 * @param e the e
	 */
	public InvalidDeviceDetailException(Exception e) {
		super(e.getMessage(), e);
	}

	/**
	 * Instantiates a new invalid device detail exception.
	 *
	 * @param message the message
	 */
	public InvalidDeviceDetailException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new invalid device detail exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public InvalidDeviceDetailException(String message, Throwable cause) {
		super(message, cause);
	}

}

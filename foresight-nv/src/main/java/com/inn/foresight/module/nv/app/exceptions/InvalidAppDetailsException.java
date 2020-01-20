package com.inn.foresight.module.nv.app.exceptions;


/**
 * The Class InvalidAppDetailsException.
 *
 * @author innoeye
 * date - 16-Nov-2017 6:27:17 PM
 */
public class InvalidAppDetailsException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new invalid app details exception.
	 *
	 * @param e the e
	 */
	public InvalidAppDetailsException(Exception e) {
		super(e.getMessage(), e);
	}

	/**
	 * Instantiates a new invalid app details exception.
	 *
	 * @param message the message
	 */
	public InvalidAppDetailsException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new invalid app details exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public InvalidAppDetailsException(String message, Throwable cause) {
		super(message, cause);
	}
}

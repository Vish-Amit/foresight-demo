package com.inn.foresight.module.nv.app.exceptions;


/**
 * The Class MaxUserLimitReachedException.
 *
 * @author innoeye
 * date - 16-Nov-2017 6:22:05 PM
 */
public class MaxUserLimitReachedException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Instantiates a new max user limit reached exception.
	 *
	 * @param e the e
	 */
	public MaxUserLimitReachedException(Exception e) {
		super(e.getMessage(), e);
	}
	
	/**
	 * Instantiates a new max user limit reached exception.
	 *
	 * @param message the message
	 */
	public MaxUserLimitReachedException(String message) {
		super(message);
	}
	
	/**
	 * Instantiates a new max user limit reached exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public MaxUserLimitReachedException(String message, Throwable cause) {
		super(message, cause);
	}

}

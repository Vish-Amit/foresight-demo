package com.inn.foresight.core.generic.exceptions;

/** The Class InvalidEncryptionException. */
public class InvalidEncryptionException extends Exception{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new invalid encryption exception.
	 *
	 * @param e the e
	 */
	public InvalidEncryptionException(Exception e) {
		super(e.getMessage(), e);
	}

	/**
	 * Instantiates a new invalid encryption exception.
	 *
	 * @param message the message
	 */
	public InvalidEncryptionException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new invalid encryption exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public InvalidEncryptionException(String message, Throwable cause) {
		super(message, cause);
	}
	
}

package com.inn.foresight.core.generic.exceptions;

/**
 * The Class ValueNotFoundException.
 */
public class ValueNotFoundException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3892116817542890495L;

	/**
	 * Instantiates a new value not found exception.
	 *
	 * @param e            Exception
	 */
	public ValueNotFoundException(Exception e) {
		super(e.getMessage(), e);
	}

	/**
	 * Instantiates a new value not found exception.
	 *
	 * @param string the string
	 */
	public ValueNotFoundException(String string) {
		super(string);
	}

	/**
	 * Instantiates a new value not found exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public ValueNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new value not found exception.
	 *
	 * @param string the string
	 * @param guimasaage the guimasaage
	 */
	public ValueNotFoundException(String string, String guimasaage) {
		super(guimasaage);

	}
}

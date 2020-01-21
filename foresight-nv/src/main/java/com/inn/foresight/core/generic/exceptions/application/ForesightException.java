package com.inn.foresight.core.generic.exceptions.application;

/**
 * The Class ForesightException.
 *
 * @author Autogenerated by Headstart
 * @version 1.0
 */
public class ForesightException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7866616924378144581L;
	
	/** The gui message. */
	private String guiMessage;

	/**
	 * Gets the gui message.
	 *
	 * @return String
	 */
	

	/**
	 * Sets the gui message.
	 *
	 * @param message the new gui message
	 */
	public void setGuiMessage(String message) {
		guiMessage = "{\"errorMsg\": \"" + message + "\"}";
	}

	public String getGuiMessage() {
		return guiMessage;
	}

	/**
	 * Instantiates a new foresight exception.
	 */
	public ForesightException() {
		super();
	}

	/**
	 * Constructor.
	 * 
	 * @param message
	 *            exception message
	 * @param cause
	 *            the cause of the exception
	 */
	public ForesightException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new foresight exception.
	 *
	 * @param cause the cause
	 */
	public ForesightException(Throwable cause) {
		super(cause.getMessage(), cause);
	}

	/**
	 * Constructor.
	 *
	 * @param message the message
	 */
	public ForesightException(String message) {
		super(message);
		setGuiMessage(message);
	}

	/**
	 * Instantiates a new foresight exception.
	 *
	 * @param message the message
	 * @param guiMessage the gui message
	 */
	public ForesightException(String message, String guiMessage) {
		super(message);
		setGuiMessage(guiMessage);
	}
}

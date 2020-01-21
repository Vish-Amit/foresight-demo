package com.inn.foresight.core.generic.exceptions.application;

/**
 * The Class ReportNotGeneratedException.
 */
public class ReportNotGeneratedException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7185419481385391952L;
	
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
	 * Instantiates a new report not generated exception.
	 */
	public ReportNotGeneratedException() {
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
	public ReportNotGeneratedException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new report not generated exception.
	 *
	 * @param cause the cause
	 */
	public ReportNotGeneratedException(Throwable cause) {
		super(cause.getMessage(), cause);
	}

	/**
	 * Constructor.
	 *
	 * @param message the message
	 */
	public ReportNotGeneratedException(String message) {
		super(message);
		setGuiMessage(message);
	}

	/**
	 * Instantiates a new report not generated exception.
	 *
	 * @param message the message
	 * @param guiMessage the gui message
	 */
	public ReportNotGeneratedException(String message, String guiMessage) {
		super(message);
		setGuiMessage(guiMessage);
	}
}

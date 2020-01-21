package com.inn.foresight.core.generic.exceptions.application;

import com.inn.core.generic.exceptions.application.RestException;

/**
 * The Class AddressNotFoundException.
 */
public class AddressNotFoundException extends RestException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3892116817542890495L;

	/**
	 * Instantiates a new address not found exception.
	 *
	 * @param e            Exception
	 */
	public AddressNotFoundException(Exception e) {
		super(e);
	}

	/**
	 * Instantiates a new address not found exception.
	 *
	 * @param string the string
	 */
	public AddressNotFoundException(String string) {
		super(string);

	}

	/**
	 * Instantiates a new address not found exception.
	 *
	 * @param string the string
	 * @param guimasaage the guimasaage
	 */
	public AddressNotFoundException(String string, String guimasaage) {
		super(guimasaage);

	}
}

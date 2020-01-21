package com.inn.foresight.core.generic.exceptions.application;

import com.inn.core.generic.exceptions.application.RestException;

/**
 * The Class AddressAlreadyExistException.
 */
public class AddressAlreadyExistException extends RestException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3892116817542890495L;

	/**
	 * Instantiates a new address already exist exception.
	 *
	 * @param e            Exception
	 */
	public AddressAlreadyExistException(Exception e) {
		super(e);
	}

	/**
	 * Instantiates a new address already exist exception.
	 *
	 * @param string the string
	 */
	public AddressAlreadyExistException(String string) {
		super(string);

	}

	/**
	 * Instantiates a new address already exist exception.
	 *
	 * @param string the string
	 * @param guimasaage the guimasaage
	 */
	public AddressAlreadyExistException(String string, String guimasaage) {
		super(guimasaage);

	}
}

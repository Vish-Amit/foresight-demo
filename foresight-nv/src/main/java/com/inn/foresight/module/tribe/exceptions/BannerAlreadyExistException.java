package com.inn.foresight.module.tribe.exceptions;

import com.inn.core.generic.exceptions.application.RestException;

/**
 * The Class BannerAlreadyExistException.
 */
public class BannerAlreadyExistException extends RestException {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3892116817542890495L;

	/**
	 * Instantiates a new banner already exist exception.
	 *
	 * @param e            Exception
	 */
	public BannerAlreadyExistException(Exception e) {
		super(e);
	}

	/**
	 * Instantiates a new banner already exist exception.
	 *
	 * @param string the string
	 */
	public BannerAlreadyExistException(String string) {
		super(string);

	}

	/**
	 * Instantiates a new banner already exist exception.
	 *
	 * @param string the string
	 * @param guimasaage the guimasaage
	 */
	public BannerAlreadyExistException(String string, String guimasaage) {
		super(guimasaage);

	}
}

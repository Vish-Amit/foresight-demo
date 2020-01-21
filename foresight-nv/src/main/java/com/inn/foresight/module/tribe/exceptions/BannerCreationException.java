package com.inn.foresight.module.tribe.exceptions;

import com.inn.core.generic.exceptions.application.RestException;

/**
 * The Class BannerCreationException.
 */
public class BannerCreationException extends RestException {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3892116817542890495L;

	/**
	 * Instantiates a new banner creation exception.
	 *
	 * @param e            Exception
	 */
	public BannerCreationException(Exception e) {
		super(e);
	}

	/**
	 * Instantiates a new banner creation exception.
	 *
	 * @param string the string
	 */
	public BannerCreationException(String string) {
		super(string);

	}

	/**
	 * Instantiates a new banner creation exception.
	 *
	 * @param string the string
	 * @param guimasaage the guimasaage
	 */
	public BannerCreationException(String string, String guimasaage) {
		super(guimasaage);

	}
}


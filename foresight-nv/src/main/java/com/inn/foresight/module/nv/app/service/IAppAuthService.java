package com.inn.foresight.module.nv.app.service;

/** The Interface IAppAuthService. */
public interface IAppAuthService {

	/**
	 * Check authentication.
	 *
	 * @param username the username
	 * @param password the password
	 * @param ip the ip
	 * @param verificationcode the verificationcode
	 * @return the string
	 * @throws Exception the exception
	 */
	String checkAuthentication(String username, String password, String ip, String verificationcode);
	
	String checkAuthentication1(String username, String password);


	/**
	 * Checks if is user TF authorised.
	 *
	 * @param username the username
	 * @return the string
	 */
	String isUserTFAuthorised(String username);

	
}

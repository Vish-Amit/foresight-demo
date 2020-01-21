package com.inn.foresight.core.infra.service;

import java.util.Map;

/**
 * The Interface IRequestScopeDataService.
 */
public interface IRequestScopeDataService {

	/**
	 * Gets the exception list.
	 *
	 * @return the exception list
	 */
	Map<String, Throwable> getExceptionList();

	/**
	 * Sets the exception list.
	 *
	 * @param exceptionList the exception list
	 */
	void setExceptionList(Map<String, Throwable> exceptionList);

	/**
	 * Sets the exception in list.
	 *
	 * @param code the code
	 * @param exception the exception
	 */
	void setExceptionInList(String code, Throwable exception);

	/**
	 * Initialize list.
	 */
	void InitializeList();

	/**
	 * Sets the exception in list.
	 *
	 * @param code the code
	 * @param exception the exception
	 * @param row the row
	 */
	void setExceptionInList(String code, Throwable exception, Integer row);

	/**
	 * Sets the exception in list.
	 *
	 * @param exceptionList the exception list
	 */
	void setExceptionInList(Map<String, Throwable> exceptionList);

}

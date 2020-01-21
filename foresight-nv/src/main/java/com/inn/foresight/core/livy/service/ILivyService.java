package com.inn.foresight.core.livy.service;

import java.util.Map;

/**
 * The Interface ILivyService.
 * 
 * @author Zafar
 */
public interface ILivyService {

	/**
	 * Submit spark sql.
	 *
	 * @param query the query
	 * @param show the show
	 * @param tempTable the temp table
	 * @param caching the caching
	 * @param appName the app name
	 * @return the string
	 */
	String submitSparkSql(String query, boolean show, String tempTable, Boolean caching, String appName);

	/**
	 * Delete session.
	 *
	 * @param appName the app name
	 * @return the string
	 */
	String deleteSession(String appName);

	/**
	 * Submit code template.
	 *
	 * @param code the code
	 * @param appName the app name
	 * @return the string
	 */
	String submitCodeTemplate(String code, String appName);

	/**
	 * Gets the application name by module.
	 *
	 * @param vendor the vendor
	 * @param domain the domain
	 * @param technology the technology
	 * @param module the module
	 * @return the application name by module
	 */
	Map<String, String> getApplicationNameByModule(String vendor, String domain, String technology, String module);
}
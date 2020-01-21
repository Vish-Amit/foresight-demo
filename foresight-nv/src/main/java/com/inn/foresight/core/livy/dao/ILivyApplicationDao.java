package com.inn.foresight.core.livy.dao;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.foresight.core.livy.model.LivyApplication;

/**
 * The Interface ILivyApplicationDao.
 * 
 *  @author Zafar
 */
public interface ILivyApplicationDao extends IGenericDao<Integer, LivyApplication> {
	
	/**
	 * Gets the livy application.
	 *
	 * @param appName the app name
	 * @return the livy application
	 */
	public LivyApplication getLivyApplication(String appName);

	/**
	 * Update app.
	 *
	 * @param app the app
	 */
	public void updateApp(LivyApplication app);

	/**
	 * Gets the application name by module.
	 *
	 * @param vendor the vendor
	 * @param domain the domain
	 * @param technology the technology
	 * @param module the module
	 * @return the application name by module
	 */
	public String getApplicationNameByModule(String vendor, String domain, String technology, String module);
}
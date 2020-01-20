package com.inn.foresight.module.nv.wpt.analytics.dao;

import java.util.Map;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.foresight.module.nv.wpt.analytics.model.WebCategory;

/** The Interface IWebCategoryDao. */
public interface IWebCategoryDao extends IGenericDao<Integer,WebCategory> {

	/**
	 * Gets the web category map.
	 *
	 * @return the web category map
	 */
	Map<String, WebCategory> getWebCategoryMap();

	/**
	 * Gets the web category id map.
	 *
	 * @return the web category id map
	 */
	Map<Integer, WebCategory> getWebCategoryIdMap();

}

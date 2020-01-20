package com.inn.foresight.module.nv.wpt.analytics.dao;

import java.util.Map;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.foresight.module.nv.wpt.analytics.model.WebCategory;
import com.inn.foresight.module.nv.wpt.analytics.model.Website;

/** The Interface IWebsiteDao. */
public interface IWebsiteDao extends IGenericDao<Integer, Website> {

	/**
	 * Gets the site category map.
	 *
	 * @return the site category map
	 */
	Map<String, String> getSiteCategoryMap();

	/**
	 * Update site table.
	 *
	 * @param url the url
	 * @param webCategoryMap the web category map
	 * @param siteMap the site map
	 * @return the string
	 */
	String updateSiteTable(String url, Map<String, WebCategory> webCategoryMap, Map<String, String> siteMap);

}

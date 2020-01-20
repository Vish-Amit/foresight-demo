package com.inn.foresight.module.nv.wpt.analytics.dao.impl.mysql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.module.nv.wpt.analytics.dao.IWebCategoryDao;
import com.inn.foresight.module.nv.wpt.analytics.model.WebCategory;

/** The Class WebCategoryDaoImpl. */
@Repository("WebCategoryDaoImpl")
public class WebCategoryDaoImpl extends HibernateGenericDao<Integer, WebCategory> implements IWebCategoryDao{

	/** The logger. */
	private Logger logger = LogManager.getLogger(WebCategoryDaoImpl.class);
	
	/** Instantiates a new web category dao impl. */
	public WebCategoryDaoImpl() {
		super(WebCategory.class);
	}
	
	/**
	 * Gets the web category map.
	 *
	 * @return the web category map
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Map<String, WebCategory> getWebCategoryMap() {
		Map<String, WebCategory> webCategoryMap = new HashMap<>();
		try {
			List<WebCategory> webCategory = findAll();
			for (WebCategory tableData : webCategory) {
				webCategoryMap.put(tableData.getCategoryName(), tableData);
			}
		} catch (DaoException e) {
			logger.error("DBException found in @getWebCategoryMap: err={}", e.getMessage());
		} catch (Exception e) {
			logger.error("No WebCategory found for error={}  ", e.getMessage());
		}
		return webCategoryMap;
	}
	
	/**
	 * Gets the web category id map.
	 *
	 * @return the web category id map
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Map<Integer, WebCategory> getWebCategoryIdMap() {
		Map<Integer, WebCategory> webCategoryMap = new HashMap<>();
		try {
			List<WebCategory> webCategory = findAll();
			for (WebCategory tableData : webCategory) {
				webCategoryMap.put(tableData.getId(), tableData);
			}
		} catch (DaoException e) {
			logger.error("DBException found in @getWebCategoryIdMap: err={}", e.getMessage());
		} catch (Exception e) {
			logger.error("No WebCategory found for error={}  ", e.getMessage());
		}
		return webCategoryMap;
	}

}

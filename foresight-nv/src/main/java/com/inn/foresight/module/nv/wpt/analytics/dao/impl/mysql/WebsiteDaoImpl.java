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
import com.inn.core.generic.utils.QueryObject.SortOrder;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.wpt.analytics.constants.WPTAnalyticsConstants;
import com.inn.foresight.module.nv.wpt.analytics.dao.IWebsiteDao;
import com.inn.foresight.module.nv.wpt.analytics.model.WebCategory;
import com.inn.foresight.module.nv.wpt.analytics.model.Website;
import com.inn.foresight.module.nv.wpt.analytics.utils.WPTAnalyticsUtils;

/** The Class WebsiteDaoImpl. */
@Repository("WebsiteDaoImpl")
public class WebsiteDaoImpl extends HibernateGenericDao<Integer, Website> implements IWebsiteDao {

	/** The logger. */
	private Logger logger = LogManager.getLogger(WebsiteDaoImpl.class);

	/** Instantiates a new website dao impl. */
	public WebsiteDaoImpl() {
		super(Website.class);
	}

	/**
	 * Find all with pagination.
	 *
	 * @param lowerLimit the lower limit
	 * @param upperLimit the upper limit
	 * @param order the order
	 * @param sort the sort
	 * @return the list
	 */
	@Override
	public List<Website> findAllWithPagination(int lowerLimit, int upperLimit, String order, SortOrder sort) {
		logger.info("Going to get All records for lowerLimit {}, upperLimit {}, order {}, SortOrder {}", lowerLimit,
				upperLimit, order, sort);
		return super.findAllWithPagination(lowerLimit, upperLimit, order, sort);
	}

	/**
	 * Gets the site category map.
	 *
	 * @return the site category map
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public Map<String, String> getSiteCategoryMap() {
		Map<String, String> categoryMap = new HashMap<>();
		List<Website> websites = null;
		try {
			websites = findAll();
		} catch (DaoException e) {
			logger.error("DBException found in @getSiteCategoryMap: err={}", e.getMessage());
		} catch (Exception e) {
			logger.error("No SiteCategoryRecord found for error={}  ", e.getMessage());
		}
		if(websites!=null && !websites.isEmpty()) {
			logger.info("website size : {}",websites.size());
		}else {
			logger.info("websites are null");
		}
		for (Website website : websites) {
			if (website != null ) {
				String siteName = website.getSiteName();
				String categoryName = website.getCategory().getCategoryName();
				logger.info(" siteName : {}, categoryName : {}",siteName,categoryName);
				if(Utils.hasValidValue(siteName) && Utils.hasValidValue(categoryName)) {
					categoryMap.put(siteName.toLowerCase(),categoryName.toLowerCase());
				}
			}
		}
		return categoryMap;
	}
	
	/**
	 * Update site table.
	 *
	 * @param url the url
	 * @param webCategoryMap the web category map
	 * @param siteMap the site map
	 * @return the string
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String updateSiteTable(String url, Map<String, WebCategory> webCategoryMap,
			Map<String, String> siteMap) {
		try {
			Website website = new Website();
			WebCategory webCategory = webCategoryMap.get(WPTAnalyticsConstants.OTHER);
			website.setSiteName(url);
			website.setCategory(webCategory);
			website.setIsHttps(WPTAnalyticsUtils.getIsHttps(url));
			website = create(website);
			logger.info("success create a new record");
			String category = WPTAnalyticsConstants.OTHER;
			siteMap.put(website.getSiteName(), website.getCategory().getCategoryName());
			return category;
		} catch (DaoException e) {
			logger.error("DBException found in @updateSiteTable: err={}", e.getMessage());
		} catch (Exception e) {
			logger.error("Unable to create Website Record for error={}  ", e.getMessage());
		}
		return null;
	}

}

package com.inn.foresight.module.tribe.dao;

import java.util.List;
import java.util.Map;

import org.apache.cxf.jaxrs.ext.search.SearchContext;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.foresight.module.tribe.model.TRBBanner;

/**
 * The Interface ITRBBannerDao.
 */
public interface ITRBBannerDao extends IGenericDao<Integer, TRBBanner> {

	/**
	 * Find by banner title.
	 *
	 * @param title the title
	 * @return the TRB banner
	 */
	TRBBanner findByBannerTitle(String title);
	
	/**
	 * Gets the search record count.
	 *
	 * @param context the context
	 * @return the search record count
	 */
	Integer getSearchRecordCount(SearchContext context);
	
	/**
	 * Gets the existed banner ids.
	 *
	 * @return the existed banner ids
	 */
	List<Integer> getExistedBannerIds();

	Map<String, Integer> getBannerCountByStatus();

	void markExpiredBanners();

}

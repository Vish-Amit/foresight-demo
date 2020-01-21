package com.inn.foresight.module.tribe.dao;

import org.apache.cxf.jaxrs.ext.search.SearchContext;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.foresight.module.tribe.model.TRBBannerView;

/**
 * The Interface ITRBBannerViewDao.
 */
public interface ITRBBannerViewDao extends IGenericDao<Integer, TRBBannerView> {

	/**
	 * Gets the banner view by banner id and user id.
	 *
	 * @param bannerId the banner id
	 * @param userId the user id
	 * @return the banner view by banner id and user id
	 */
	TRBBannerView getBannerViewByBannerIdAndUserId(Integer bannerId, Integer userId);

	/**
	 * Delete banner views by banner id.
	 *
	 * @param bannerId the banner id
	 */
	void deleteBannerViewsByBannerId(Integer bannerId);

	/**
	 * Gets the search record count.
	 *
	 * @param context the context
	 * @return the search record count
	 */
	Integer getSearchRecordCount(SearchContext context);

	/**
	 * Gets the banner count by banner id.
	 *
	 * @param bannerId the banner id
	 * @return the banner count by banner id
	 */
	Long getBannerCountByBannerId(Integer bannerId);


	Integer activeBannerCount();

	Integer inactiveBannerCount();

	Integer draftBannerCount();

}

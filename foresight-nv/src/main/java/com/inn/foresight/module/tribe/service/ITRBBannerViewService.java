package com.inn.foresight.module.tribe.service;

import org.apache.cxf.jaxrs.ext.search.SearchContext;

import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.module.tribe.model.TRBBannerView;

/**
 * The Interface ITRBBannerViewService.
 */
public interface ITRBBannerViewService extends IGenericService<Integer, TRBBannerView> {
	
	/**
	 * Creates the banner view.
	 *
	 * @param bannerId the banner id
	 * @return the string
	 */
	String createBannerView(Integer bannerId);

	/**
	 * Gets the search record count.
	 *
	 * @param context the context
	 * @return the search record count
	 */
	Integer getSearchRecordCount(SearchContext context);

	Integer activeBannerCount();

	Integer inactiveBannerCount();

	Integer draftBannerCount();
}

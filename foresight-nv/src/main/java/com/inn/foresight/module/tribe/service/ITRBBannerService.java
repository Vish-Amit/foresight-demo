package com.inn.foresight.module.tribe.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.cxf.jaxrs.ext.search.SearchContext;

import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.module.tribe.model.TRBBanner;
import com.inn.foresight.module.tribe.model.TRBBanner.BannerStatus;
import com.inn.foresight.module.tribe.utils.BannerContentWrapper;

/**
 * The Interface ITRBBannerService.
 */
public interface ITRBBannerService extends IGenericService<Integer, TRBBanner> {

	/**
	 * Creates the banner.
	 *
	 * @param bannerStream the banner stream
	 * @param fileStream the file stream
	 * @param banner the banner
	 * @return the TRB banner
	 * @throws RestException the rest exception
	 * @throws DaoException 
	 */
	TRBBanner createBanner(InputStream bannerStream, InputStream fileStream, String banner) throws RestException, DaoException;

	/**
	 * Update banner.
	 *
	 * @param bannerStream the banner stream
	 * @param fileStream the file stream
	 * @param banner the banner
	 * @return the TRB banner
	 * @throws RestException the rest exception
	 * @throws DaoException 
	 */
	TRBBanner updateBanner(InputStream bannerStream, InputStream fileStream, String banner) throws RestException, DaoException;

	/**
	 * Update banner status.
	 *
	 * @param bannerId the banner id
	 * @param status the status
	 * @return the string
	 * @throws RestException the rest exception
	 * @throws DaoException 
	 */
	String updateBannerStatus(Integer bannerId, BannerStatus status) throws RestException, DaoException;

	/**
	 * Delete banner.
	 *
	 * @param bannerId the banner id
	 * @return the string
	 * @throws RestException the rest exception
	 */
	String deleteBanner(Integer bannerId) throws RestException;

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

	Map<String,Integer> getBannerCountByStatus();

	List<TRBBanner> updateBannerContent(List<BannerContentWrapper> bannerContentWrapper);
	
	public void markExpiredBanners();

}

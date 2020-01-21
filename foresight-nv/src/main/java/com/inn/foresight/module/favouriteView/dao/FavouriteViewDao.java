/**
 * 
 */
package com.inn.foresight.module.favouriteView.dao;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.module.favouriteView.model.FavouriteView;
import com.inn.foresight.module.favouriteView.wrapper.FavouriteViewWrapper;


/**
 * @author root
 *
 */

public interface FavouriteViewDao  extends IGenericDao<Integer, FavouriteView>{
	


	/**
	 * Save favourite view.
	 *
	 * @param favouriteView the favourite view
	 * @return the favourite view
	 * @throws RestException the rest exception
	 * @throws Exception the exception
	 */
	public FavouriteView saveFavouriteView(FavouriteView favouriteView) throws Exception;

	/**
	 * Gets the view by ID.
	 *
	 * @param FavouriteID the favourite ID
	 * @return the view by ID
	 * @throws RestException the rest exception
	 * @throws Exception the exception
	 */
	public FavouriteView getViewByID(Integer FavouriteID) throws Exception;

	/**
	 * Gets the all favourites for logged in user.
	 *
	 * @return the all favourites for logged in user
	 * @throws RestException the rest exception
	 * @throws Exception the exception
	 */
	public List<FavouriteViewWrapper> getAllFavouritesForLoggedInUser() throws Exception;

	/**
	 * Gets the default favourite.
	 *
	 * @return the default favourite
	 * @throws NoResultException the no result exception
	 * @throws NonUniqueResultException the non unique result exception
	 * @throws RestException the rest exception
	 * @throws Exception the exception
	 */
	public FavouriteView getDefaultFavourite() throws Exception;

    FavouriteView getFavouriteViewByName(String viewName, Integer userid) throws Exception;


}

/**
 * 
 */
package com.inn.foresight.module.favouriteView.service;

import java.util.List;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.module.favouriteView.model.FavouriteView;
import com.inn.foresight.module.favouriteView.wrapper.FavouriteViewWrapper;

/**
 * @author root
 *
 */
public interface FavouriteViewService extends IGenericService<Integer, FavouriteView>{



	/**
	 * Save favourite view.
	 *
	 * @param favouriteViewWrapper the favourite view wrapper
	 * @return the favourite view wrapper
	 * @throws RestException the rest exception
	 */
	public FavouriteViewWrapper saveFavouriteView(FavouriteViewWrapper favouriteViewWrapper);

	/**
	 * Gets the view by ID.
	 *
	 * @param favouriteID the favourite ID
	 * @return the view by ID
	 * @throws RestException the rest exception
	 */
	public FavouriteViewWrapper getViewByID(Integer favouriteID);

	/**
	 * Gets the all favourites for logged in user.
	 *
	 * @return the all favourites for logged in user
	 * @throws RestException the rest exception
	 */
	public List<FavouriteViewWrapper> getAllFavouritesForLoggedInUser();

	/**
	 * Gets the default favourite.
	 *
	 * @return the default favourite
	 * @throws RestException the rest exception
	 */
	public FavouriteViewWrapper getDefaultFavourite();

	/**
	 * Delete favourite view by ID.
	 *
	 * @param favouriteID the favourite ID
	 * @return the boolean
	 * @throws RestException the rest exception
	 */
	public Boolean deleteFavouriteViewByID(Integer favouriteID);

	/**
	 * Sets the default favourite.
	 *
	 * @param favouriteViewID the favourite view ID
	 * @return the boolean
	 * @throws RestException the rest exception
	 */
	public Boolean setDefaultFavourite(Integer favouriteViewID);

	/**
	 * Removes the default from favourite view.
	 *
	 * @param favouriteViewID the favourite view ID
	 * @return the boolean
	 * @throws RestException the rest exception
	 */
	public Boolean removeDefaultFromFavouriteView(Integer favouriteViewID);
   
	/**
     * Share favourite view to Another User.
     *
     * @param favouriteViewID the favourite view ID
     * $param email the User's emailId 
     * @return the boolean
     * @throws RestException the rest exception
     */
    public Boolean shareFavouriteView(Integer favouriteViewId, String email); 
}

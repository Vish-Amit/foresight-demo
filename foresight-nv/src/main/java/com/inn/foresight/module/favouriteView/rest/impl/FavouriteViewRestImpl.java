/**
 * 
 */
package com.inn.foresight.module.favouriteView.rest.impl;


import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.module.favouriteView.model.FavouriteView;
import com.inn.foresight.module.favouriteView.service.FavouriteViewService;
import com.inn.foresight.module.favouriteView.wrapper.FavouriteViewWrapper;


/**
 * The Class FavouriteViewRestImpl.
 *
 * @author innoeye
 */

@Path("/FavouriteView")
@Produces("application/json")
@Consumes("application/json")
@Service("FavouriteViewRestImpl")
public class FavouriteViewRestImpl  {

	/** The logger. */
	private Logger logger = LogManager.getLogger(FavouriteViewRestImpl.class);

	/**
	 * Instantiates a new favourite view rest impl.
	 *//*
	public FavouriteViewRestImpl() {
		super(FavouriteView.class);
	}*/

	/** The ifavourite view service. */
	@Autowired
	FavouriteViewService ifavouriteViewService;

	/**
	 * Save favourite view.
	 *
	 * @param favouriteViewWrapper the favourite view wrapper
	 * @return the favourite view wrapper
	 * @throws RestException the rest exception
	 */
	@POST
	@Path("saveFavouriteView")
	@Consumes("application/json")
	@Produces("application/json")
	public FavouriteViewWrapper saveFavouriteView(FavouriteViewWrapper favouriteViewWrapper) {
		logger.error("Going to start FavouriteViewRestImpl saveFavouriteView. favouriteViewWrapper: ");
		return ifavouriteViewService.saveFavouriteView(favouriteViewWrapper);
	}

	/**
	 * Gets the view by ID.
	 *
	 * @param favouriteViewID the favourite view ID
	 * @return the view by ID
	 * @throws RestException the rest exception
	 */
	@GET
	@Path("getViewByID")
	@Produces("application/json")
	public FavouriteViewWrapper getViewByID(@QueryParam("favouriteID") Integer favouriteViewID) {
		logger.info("Going to start FavouriteViewRestImpl getViewByID, favouriteViewID : " + favouriteViewID);
		return ifavouriteViewService.getViewByID(favouriteViewID);
	}

	/**
	 * Gets the all favourites for logged in user.
	 *
	 * @return the all favourites for logged in user
	 * @throws RestException the rest exception
	 */
	@GET
	@Path("getAllFavouritesForLoggedInUser")
	public List<FavouriteViewWrapper> getAllFavouritesForLoggedInUser() {
		logger.info("Going to start FavouriteViewRestImpl getAllFavouritesForLoggedInUser");
		return ifavouriteViewService.getAllFavouritesForLoggedInUser();
	}

	/**
	 * Gets the default favourite.
	 *
	 * @return the default favourite
	 * @throws RestException the rest exception
	 */
	@GET
	@Path("getDefaultFavourite")
	@Produces("application/json")
	public FavouriteViewWrapper getDefaultFavourite() {
		logger.info("Going to start  FavouriteViewRestImpl getDefaultFavourite");
		return ifavouriteViewService.getDefaultFavourite();
	}

	/**
	 * Gets the deafult favourite view ID.
	 *
	 * @return the deafult favourite view ID
	 * @throws RestException the rest exception
	 */
	@GET
	@Path("getDeafultFavouriteViewID")
	@Produces("application/json")
	public FavouriteViewWrapper getDeafultFavouriteViewID() {
		try {
			logger.info("Going to start  FavouriteViewRestImpl.getDeafultFavouriteViewID");
			return  ifavouriteViewService.getDefaultFavourite();
			
		} catch (Exception e) {
			logger.error("Exception in getDeafultFavouriteViewID {}",e.getMessage());
			throw new RestException(ForesightConstants.UNABLE_TO_PROCESS_REQUEST);
		}

	}

	/**
	 * Sets the default favourite.
	 *
	 * @param favouriteViewID the favourite view ID
	 * @return the boolean
	 * @throws RestException the rest exception
	 */
	@GET
	@Path("setDefaultFavourite/{favouriteViewID}")
	public Boolean setDefaultFavourite(@PathParam("favouriteViewID") Integer favouriteViewID) {
		logger.info("Going to start  FavouriteViewRestImpl getDefaultFavourite");
		return ifavouriteViewService.setDefaultFavourite(favouriteViewID);
	}

	/**
	 * Delete favourite view by ID.
	 *
	 * @param favouriteViewID the favourite view ID
	 * @return the boolean
	 * @throws RestException the rest exception
	 */
	@GET
	@Path("deleteFavouriteViewByID")
	public Boolean deleteFavouriteViewByID(@QueryParam("favouriteID") Integer favouriteViewID) {
		logger.info("Going to start FavouriteViewRestImpl deleteViewByID, favouriteViewID : " + favouriteViewID);
		return ifavouriteViewService.deleteFavouriteViewByID(favouriteViewID);
	}

	/**
	 * Removes the default from favourite view.
	 *
	 * @param favouriteViewID the favourite view ID
	 * @return the boolean
	 * @throws RestException the rest exception
	 */
	@GET
	@Path("removeDefaultFromFavouriteView")
	public Boolean removeDefaultFromFavouriteView(@QueryParam("favouriteID") Integer favouriteViewID) {
		logger.info("Going to start FavouriteViewRestImpl removeDefaultFromFavouriteView, favouriteViewID : " + favouriteViewID);
		return ifavouriteViewService.removeDefaultFromFavouriteView(favouriteViewID);
	}

		/**
	 * Creates the.
	 *
	 * @param anEntity the an entity
	 * @return the favourite view
	 * @throws RestException the rest exception
	 */
	
	public FavouriteView create(FavouriteView anEntity) {
		logger.info("Going to start create");
		return ifavouriteViewService.create(anEntity);
	}
	
    /**
     * Share favourite view to Another User.
     *
     * @param favouriteViewID the favourite view ID
     * $param email the User's emailId 
     * @return the boolean
     * @throws RestException the rest exception
     */
    @GET
    @Path("shareFavouriteView/{favouriteViewId}/{email}")
	public Boolean shareFavouriteView(@PathParam("favouriteViewId") Integer favouriteViewId,@PathParam("email") String email) {
        logger.info("Going to share favouriteView id: {} email: {}",favouriteViewId,email);
	    return ifavouriteViewService.shareFavouriteView(favouriteViewId,email);
	}
}


/**
 * 
 */
package com.inn.foresight.module.favouriteView.dao.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.module.favouriteView.dao.FavouriteViewDao;
import com.inn.foresight.module.favouriteView.model.FavouriteView;
import com.inn.foresight.module.favouriteView.wrapper.FavouriteViewWrapper;
import com.inn.product.um.user.model.User;
import com.inn.product.um.user.service.impl.UserContextServiceImpl;


/**
 * The Class FavouriteViewDaoImpl.
 *
 * @author innoeye
 */

@Repository("FavouriteViewDaoImpl")
@SuppressWarnings("unchecked")
public class FavouriteViewDaoImpl extends HibernateGenericDao<Integer, FavouriteView> implements FavouriteViewDao {
	
	/** The logger. */
	private Logger logger = LogManager.getLogger(FavouriteViewDaoImpl.class);

	/**
	 * Instantiates a new favourite view dao impl.
	 */
	public FavouriteViewDaoImpl() {
		super(FavouriteView.class);
	}

	/**
	 * Save favourite view.
	 *
	 * @param favouriteView the favourite view
	 * @return the favourite view
	 * @throws RestException the rest exception
	 * @throws Exception the exception
	 */
	@Override
	public FavouriteView saveFavouriteView(FavouriteView favouriteView) throws Exception {
		logger.info("Going to save favouriteView in FavouriteViewDaoImpl");
		FavouriteView newFavouriteView = create(favouriteView);
		if (newFavouriteView != null) {
			logger.info("Favourite view Successfully Created.");
		} else {
			throw new RestException("Can't create favourite view.");
		}
		return newFavouriteView;
	}

	/**
	 * Gets the view by ID.
	 *
	 * @param favouriteID the favourite ID
	 * @return the view by ID
	 * @throws RestException the rest exception
	 * @throws Exception the exception
	 */
	@Override
	public FavouriteView getViewByID(Integer favouriteID) throws Exception {
		logger.info("Going to find getViewByID for UserInContext in FavouriteViewDaoImpl");
		FavouriteView favouriteView = null;
		try {
			User user = UserContextServiceImpl.getUserInContext();
			Query query = getEntityManager().createNamedQuery("findByFavouriteViewId").setParameter("userID", user.getUserid()).setParameter("viewid", favouriteID);
			favouriteView = (FavouriteView) query.getSingleResult();

		} catch (NoResultException e) {
			logger.info("Error in geting getViewByID for UserInContext error message {}" + e.getMessage());
			throw new RestException("Exception in FavouriteViewDaoImpl.getViewByID {} " + e.getMessage());
		} catch (Exception e) {
			logger.error("Error in geting getViewByID for UserInContext error message {}", e.getMessage());
			throw new RestException("Exception in FavouriteViewDaoImpl.getViewByID {} " + e.getMessage());
		}
		return favouriteView;
	}

	/**
	 * Gets the all favourites for logged in user.
	 *
	 * @return the all favourites for logged in user
	 * @throws RestException the rest exception
	 * @throws Exception the exception
	 */
	@Override
	public List<FavouriteViewWrapper> getAllFavouritesForLoggedInUser() throws Exception {
		logger.info("Going to find all Favourite View for UserInContext in FavouriteViewDaoImpl");
		List<FavouriteViewWrapper> favouriteViewList = new ArrayList<>();
		try {
			User user = UserContextServiceImpl.getUserInContext();
			Query query = getEntityManager().createNamedQuery("getAllFavouritesForLoggedInUser").setParameter("userID", user.getUserid());
			favouriteViewList = query.getResultList();
			if(favouriteViewList != null) {
			     return favouriteViewList.stream().filter(favourite -> StringUtils.isNotEmpty(favourite.getSharedBy())).map(favourite -> {
			        if(StringUtils.equals(favourite.getSharedBy(), user.getEmail())) {
			            favourite.setSharedBy(null);
			        }return favourite;
			    } ).collect(Collectors.toCollection(ArrayList::new));
			}
		} catch (NoResultException e) {
			logger.error("Error in geting getAllFavouritesForLoggedInUser error message {}" + e.getMessage());
			throw new RestException("Exception in FavouriteViewDaoImpl.getAllFavouriteViewForLoggedInUser {} " + e.getMessage());
		} catch (Exception e) {
			logger.error("Error in geting getAllFavouritesForLoggedInUser error message {}" + e.getMessage());
			throw new RestException("Exception in FavouriteViewDaoImpl.getAllFavouriteViewForLoggedInUser  {}" + e.getMessage());
		}
		return favouriteViewList;
	}

	/**
	 * Gets the default favourite.
	 *
	 * @return the default favourite
	 * @throws RestException the rest exception
	 * @throws Exception the exception
	 */
	@Override
	public FavouriteView getDefaultFavourite() throws Exception {
		logger.info("Going to find all Favourite View for UserInContext in FavouriteViewDaoImpl");
		FavouriteView favouriteView = null;
		try {
			User user = UserContextServiceImpl.getUserInContext();

			Query query = getEntityManager().createNamedQuery("getDefaultFavourite").setParameter("userID", user.getUserid());
			favouriteView = (FavouriteView) query.getSingleResult();

		} catch (NoResultException e) {
			throw new NoResultException("Exception in FavouriteViewDaoImpl.getDefaultFavourite {} " + e.getMessage());
		} catch (NonUniqueResultException e) {
			logger.error("Error in geting getDefaultFavourite for UserInContext error message {} " + e.getMessage());
			throw new RestException("Exception in FavouriteViewDaoImpl.getDefaultFavourite {} " + e.getMessage());

		} catch (Exception e) {
			logger.error("Error in geting find default Favourite View for UserInContext error message {}" + e.getMessage());
			throw new RestException("Exception in FavouriteViewDaoImpl.getDefaultFavourite {} " + e.getMessage());
		}
		return favouriteView;
	}
	
	
	@Override
	public FavouriteView getFavouriteViewByName(String  viewName, Integer userid) throws Exception {
		logger.info("Going to find getViewByID for UserInContext in FavouriteViewDaoImpl");
		FavouriteView favouriteView = null;
		try {
            Query query = getEntityManager().createNamedQuery("getFavouriteViewByName").setParameter("userID", userid)
					.setParameter("name", viewName);
			favouriteView = (FavouriteView) query.getSingleResult();
		} catch (NoResultException | NonUniqueResultException e) {
			throw e;
		} catch (Exception e) {
			logger.error("Error in geting getViewByID for UserInContext error message {}", e.getMessage());
			throw new RestException("Exception in FavouriteViewDaoImpl.getViewByID {} " + e.getMessage());
		}
		return favouriteView;
	}
}

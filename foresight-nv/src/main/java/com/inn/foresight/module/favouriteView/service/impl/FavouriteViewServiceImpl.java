/**
 * 
 */
package com.inn.foresight.module.favouriteView.service.impl;


import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import org.apache.commons.collections.ListUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.service.impl.AbstractService;
import com.inn.core.generic.utils.Preconditions;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.module.favouriteView.dao.FavouriteViewDao;
import com.inn.foresight.module.favouriteView.model.FavouriteView;
import com.inn.foresight.module.favouriteView.model.FavouriteView.FavouriteViewType;
import com.inn.foresight.module.favouriteView.service.FavouriteViewService;
import com.inn.foresight.module.favouriteView.wrapper.FavouriteViewWrapper;
import com.inn.product.um.user.dao.UserDao;
import com.inn.product.um.user.model.User;
import com.inn.product.um.user.service.impl.UserContextServiceImpl;

/**
 * The Class FavouriteViewServiceImpl.
 *
 * @author innoeye
 */

@Service("FavouriteViewServiceImpl")
@Transactional
public class FavouriteViewServiceImpl extends AbstractService<Integer, FavouriteView> implements FavouriteViewService {

	/** The ifavourite view dao. */
	@Autowired
	private FavouriteViewDao ifavouriteViewDao;
	
    @Autowired
    private UserDao  userDao;

	/**
	 * Sets the dao.
	 *
	 * @param dao the new dao
	 */
	@Autowired
	public void setDao(FavouriteViewDao dao) {
		super.setDao(dao);
		ifavouriteViewDao = dao;
	}

	/** The logger. */
	private Logger logger = LogManager.getLogger(FavouriteViewServiceImpl.class);

	/**
	 * Save favourite view.
	 *
	 * @param favouriteViewWrapper the favourite view wrapper
	 * @return the favourite view wrapper
	 * @throws RestException the rest exception
	 */
	@Override
	public FavouriteViewWrapper saveFavouriteView(FavouriteViewWrapper favouriteViewWrapper) {
		FavouriteView favouriteView = null;
		try {
			if (favouriteViewWrapper != null) {
				if(!isFavouriteViewAlreadyAvail(favouriteViewWrapper)){
					throw new RestException("View name is already available.");
				}
				User user = UserContextServiceImpl.getUserInContext();
				favouriteView = setFavouriteViewObject(favouriteViewWrapper);
                favouriteView.setCreatedBy(user);
                favouriteView.setSharedBy(user);
                favouriteView.setCreatedTime(new Date());
				favouriteView.setModifiedBy(user);
				favouriteView.setModifiedTime(new Date());
				favouriteView.setIsDeleted(false);
				if (favouriteView.getIsDefault()) {
					Boolean flag = makePreviuosDefaultToFalse();
					if (!flag) {
						throw new RestException("Can't create favourite view.");
					}
				}
				favouriteView = ifavouriteViewDao.saveFavouriteView(favouriteView);
				FavouriteViewWrapper newFavouriteView = new FavouriteViewWrapper();
				newFavouriteView.setId(favouriteView.getId());
				newFavouriteView.setIsDefault(favouriteView.getIsDefault());
				newFavouriteView.setIsDeleted(favouriteView.getIsDeleted());
				newFavouriteView.setName(favouriteView.getName());
				return newFavouriteView;
			}
		} catch (RestException e) {
			throw e;
		}catch (Exception e) {
			logger.error("Error in geting saveFavouriteView for UserInContext error message,FavouriteViewServiceImpl.saveFavouriteView {}" + e.getMessage());
			throw new RestException("Can't create favourite view.");
		}
		return new FavouriteViewWrapper();
	}

	private boolean isFavouriteViewAlreadyAvail(FavouriteViewWrapper favouriteViewWrapper) throws Exception {
		try {
            Integer userid = UserContextServiceImpl.getUserInContext().getUserid();
			FavouriteView view =ifavouriteViewDao.getFavouriteViewByName(favouriteViewWrapper.getName(),userid);
			if(view !=null)
				return true;
		} catch (NoResultException | NonUniqueResultException e) {
			return true;
		}catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return false;
	}

	/**
	 * Make previuos default to false.
	 *
	 * @return the boolean
	 * @throws RestException the rest exception
	 * @throws Exception the exception
	 */
	private Boolean makePreviuosDefaultToFalse() throws Exception {
		logger.info("Going to remove previous default view.");
		Boolean returnFlag = false;
		try {
			User user = UserContextServiceImpl.getUserInContext();
			FavouriteView favouriteView = ifavouriteViewDao.getDefaultFavourite();
			if (favouriteView != null) {
				favouriteView.setIsDefault(false);
				favouriteView.setModifiedTime(new Date());
				favouriteView.setModifiedBy(user);
				favouriteView = ifavouriteViewDao.update(favouriteView);
				returnFlag = true;
			}
		} catch (NoResultException e) {
			logger.info("No Default View found for user." + UserContextServiceImpl.getUserInContext());
			return true;
		} catch (Exception e) {
			logger.error("Error in geting FavouriteViewServiceImpl.makePreviuosDefaultToFalse for UserInContext error message {} " + e.getMessage());
			throw new RestException("Can't create favourite view.");
		}
		return returnFlag;
	}

	/**
	 * Gets the view by ID.
	 *
	 * @param favouriteID the favourite ID
	 * @return the view by ID
	 * @throws RestException the rest exception
	 */
	@Override
	public FavouriteViewWrapper getViewByID(Integer favouriteID) {
		FavouriteView favouriteView;
		FavouriteViewWrapper wrapperObj = new FavouriteViewWrapper();
		try {
			favouriteView = ifavouriteViewDao.getViewByID(favouriteID);
			wrapperObj.setConfiguration(favouriteView.getConfiguration());
			wrapperObj.setFavouriteType(favouriteView.getFavouriteType().name());
			wrapperObj.setId(favouriteView.getId());
			wrapperObj.setIsDefault(favouriteView.getIsDefault());
			wrapperObj.setIsDeleted(favouriteView.getIsDeleted());
			wrapperObj.setName(favouriteView.getName());
		} catch (Exception e) {
			logger.error("Error in geting getViewByID for UserInContext error message, FavouriteViewServiceImpl.getViewByID {} " + e.getMessage());
			throw new RestException("No View Found.");
		}
		return wrapperObj;
	}

	/**
	 * Gets the all favourites for logged in user.
	 *
	 * @return the all favourites for logged in user
	 * @throws RestException the rest exception
	 */
	@Override
	public List<FavouriteViewWrapper> getAllFavouritesForLoggedInUser() {
		List<FavouriteViewWrapper> favouriteViewList;
		try {
			favouriteViewList = ifavouriteViewDao.getAllFavouritesForLoggedInUser();
		} catch (Exception e) {
			logger.error("Error in geting FavouriteViewServiceImpl.getAllFavouritesForLoggedInUser for UserInContext error message,FavouriteViewServiceImpl.getAllFavouritesForLoggedInUser {} "
					+ e.getMessage());
			return ListUtils.EMPTY_LIST;
		}
		return favouriteViewList;
	}

	/**
	 * Gets the default favourite.
	 *
	 * @return the default favourite
	 * @throws RestException the rest exception
	 */
	@Override
	public FavouriteViewWrapper getDefaultFavourite() {
		FavouriteView favouriteView = null;
		FavouriteViewWrapper wrapperObj = new FavouriteViewWrapper();
		try {
			favouriteView = ifavouriteViewDao.getDefaultFavourite();
			wrapperObj.setConfiguration(favouriteView.getConfiguration());
			wrapperObj.setFavouriteType(favouriteView.getFavouriteType().name());
			wrapperObj.setId(favouriteView.getId());
			wrapperObj.setIsDefault(favouriteView.getIsDefault());
			wrapperObj.setIsDeleted(favouriteView.getIsDeleted());
			wrapperObj.setName(favouriteView.getName());
		} catch (NoResultException e) {
			logger.info("Error in geting getDefaultFavourite for UserInContext error message {}" + e.getMessage());
		} catch (NonUniqueResultException e) {
			logger.error("Error in geting getDefaultFavourite for UserInContext error message ,FavouriteViewServiceImpl.getDefaultFavourite {} " + e.getMessage());
		} catch (RestException e) {
			logger.error("Error in geting getDefaultFavourite for UserInContext error message,FavouriteViewServiceImpl.getDefaultFavourite {} " + e.getMessage());			
		} catch (Exception e) {
			logger.error("Error in geting getDefaultFavourite for UserInContext error message , FavouriteViewServiceImpl.getDefaultFavourite {} " + e.getMessage());
		}
		return wrapperObj;
	}

	/**
	 * Sets the favourite view object.
	 *
	 * @param favouriteViewWrapper the favourite view wrapper
	 * @return the favourite view
	 */
	private FavouriteView setFavouriteViewObject(FavouriteViewWrapper favouriteViewWrapper) {
		FavouriteView favouriteView = new FavouriteView();
		if (favouriteViewWrapper != null) {
			logger.info("inside setFavouriteViewObject {}",favouriteViewWrapper);
			favouriteView.setConfiguration(favouriteViewWrapper.getConfiguration());
			if (ForesightConstants.FAVOURITE_TYPE_SPATIAL.equalsIgnoreCase(favouriteViewWrapper.getFavouriteType())) {
				favouriteView.setFavouriteType(FavouriteViewType.Spatial);
			} else if (ForesightConstants.FAVOURITE_TYPE_DASHBOARD.equalsIgnoreCase(favouriteViewWrapper.getFavouriteType())) {
				favouriteView.setFavouriteType(FavouriteViewType.Dashboard);
			}
			favouriteView.setIsDefault(favouriteViewWrapper.getIsDefault());
			favouriteView.setName(favouriteViewWrapper.getName());
			if (favouriteViewWrapper.getIsDefault()) {
				favouriteView.setIsDefault(true);
			} else {
				favouriteView.setIsDefault(false);
			}
		}
		return favouriteView;
	}

	/**
	 * Delete favourite view by ID.
	 *
	 * @param favouriteID the favourite ID
	 * @return the boolean
	 * @throws RestException the rest exception
	 */
	@Override
	public Boolean deleteFavouriteViewByID(Integer favouriteID) {
		Boolean isSuccess = true;
		FavouriteView favouriteView;
		try {
			User user = UserContextServiceImpl.getUserInContext();
			favouriteView = ifavouriteViewDao.getViewByID(favouriteID);
			favouriteView.setIsDefault(false);
			favouriteView.setIsDeleted(true);
			favouriteView.setModifiedTime(new Date());
			favouriteView.setModifiedBy(user);
			ifavouriteViewDao.update(favouriteView);
		} catch (Exception e) {
			logger.error("Error in deleteFavouriteViewByID for UserInContext error message {} " + e.getMessage());
			isSuccess = false;
			throw new RestException("Can't able to delete requested view.");
		}
		return isSuccess;
	}

	/**
	 * Sets the default favourite.
	 *
	 * @param favouriteViewId the favourite view id
	 * @return the boolean
	 * @throws RestException the rest exception
	 */
	@Override
	public Boolean setDefaultFavourite(Integer favouriteViewId) {

		FavouriteView favouriteView;
		Boolean flag = false;
		try {
			makePreviuosDefaultToFalse();
			favouriteView = ifavouriteViewDao.getViewByID(favouriteViewId);
			if (favouriteView == null) {
				throw new RestException("View Not found." + favouriteViewId);
			} else {
				User user = UserContextServiceImpl.getUserInContext();
				favouriteView.setIsDefault(true);
				favouriteView.setModifiedBy(user);
				favouriteView.setModifiedTime(new Date());
				ifavouriteViewDao.update(favouriteView);
				flag = true;
			}
		} catch (NonUniqueResultException e) {
			logger.error("Error in setDefaultFavourite for UserInContext error message , FavouriteViewServiceImpl.setDefaultFavourite {} " + e.getMessage());
			throw new RestException("Unable to set view as Default");
		} catch (RestException e) {
			logger.error("Error in setDefaultFavourite for UserInContext error message,  FavouriteViewServiceImpl.setDefaultFavourite {} " + e.getMessage());
			throw new RestException("Unable to set view as Default");
		} catch (Exception e) {

			logger.error("Error in setDefaultFavourite for UserInContext error message, FavouriteViewServiceImpl.setDefaultFavourite {} " + e.getMessage());
			throw new RestException("Unable to set view as Default");
		}
		return flag;
	}

	/**
	 * Removes the default from favourite view.
	 *
	 * @param favouriteViewID the favourite view ID
	 * @return the boolean
	 * @throws RestException the rest exception
	 */
	@Override
	public Boolean removeDefaultFromFavouriteView(Integer favouriteViewID) {

		FavouriteView favouriteView;
		Boolean flag = false;
		try {
			logger.info("Going to remove default from favourite view.");
			favouriteView = ifavouriteViewDao.getViewByID(favouriteViewID);
			if (favouriteView == null) {
				throw new RestException("View Not found." + favouriteViewID);
			} else {
				User user = UserContextServiceImpl.getUserInContext();
				favouriteView.setIsDefault(false);
				favouriteView.setModifiedBy(user);
				favouriteView.setModifiedTime(new Date());
				ifavouriteViewDao.update(favouriteView);
				flag = true;
			}
		} catch (Exception e) {

			logger.error("Error in removeDefaultFromFavouriteView for UserInContext error message,FavouriteViewServiceImpl.removeDefaultFromFavouriteView {} " + e.getMessage());
			throw new RestException("Unable to remove Default.");
		}
		return flag;
	}


    /**
     * Share favourite view to Another User.
     *
     * @param favouriteViewID the favourite view ID
     * $param email the User's emailId 
     * @return the boolean
     * @throws RestException the rest exception
     */
	   @Override
	   public Boolean shareFavouriteView(Integer favouriteViewId, String email) {
	       User sharedBy = null;
	       User creator = null;
	       FavouriteView favoriteView  = null;
	       try {
	           creator = UserContextServiceImpl.getUserInContext();
	           sharedBy = userDao.findUserByEmail(email);
	           favoriteView = ifavouriteViewDao.findByPk(favouriteViewId);
	           Preconditions.checkNotNull(favoriteView, "Favourite View Not Exist");
               Preconditions.checkNotNull(sharedBy, "User Does Not Exist");

               if(getFavouriteViewByName(sharedBy, favoriteView) != null){
                   throw new RestException("Favourite View Name Already Exist ");
               }
	           if(creator.getUserid().intValue() == sharedBy.getUserid().intValue()) {
                   throw new RestException("User cannot share file to the same user");
	           }

	           FavouriteView favorite = new FavouriteView();
	           favorite.setSharedBy(creator);
	           favorite.setCreatedBy(sharedBy);
	           favorite.setModifiedBy(sharedBy);
	           favorite.setModifiedTime(new Date());
               
	           favorite.setName(favoriteView.getName());
	           favorite.setConfiguration(favoriteView.getConfiguration());
	           favorite.setFavouriteType(favoriteView.getFavouriteType());
	           favorite.setIsDeleted(false);
	           favorite.setIsDefault(false);
	           try {
	               ifavouriteViewDao.saveFavouriteView(favorite);
	           } catch (Exception e) {
	               logger.error("Error in geting shareFavouriteView for UserInContext error message,FavouriteViewServiceImpl.shareFavouriteView {}" + e.getMessage());
	               throw new RestException("Can't Share favourite view.");
	           }
	           return true;
	       }catch (DaoException e) {
	           logger.error("Error in sharing FavouriteView for UserInContext error message {}" + e.getMessage());
	           throw new RestException("Unable to fetch data for shareFavouriteView");
	       }
	   }

    private FavouriteView getFavouriteViewByName(User sharedBy, FavouriteView favoriteView) {
        try {
            return ifavouriteViewDao.getFavouriteViewByName(favoriteView.getName(), sharedBy.getUserid());
        } catch (Exception e) {
            logger.error("Error in getting FavouriteView by name error message {}" + e.getMessage());
            return null;
        }
    }

}

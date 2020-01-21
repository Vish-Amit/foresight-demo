package com.inn.foresight.module.tribe.service.impl;

import java.util.Date;

import javax.validation.Valid;

import org.apache.cxf.jaxrs.ext.search.SearchContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.service.impl.AbstractService;
import com.inn.foresight.core.generic.exceptions.application.ValidationFailedException;
import com.inn.foresight.module.tribe.dao.ITRBBannerDao;
import com.inn.foresight.module.tribe.dao.ITRBBannerViewDao;
import com.inn.foresight.module.tribe.model.TRBBanner;
import com.inn.foresight.module.tribe.model.TRBBannerView;
import com.inn.foresight.module.tribe.service.ITRBBannerViewService;
import com.inn.foresight.module.tribe.utils.TRBConstants;
import com.inn.product.um.user.dao.UserDao;
import com.inn.product.um.user.service.impl.UserContextServiceImpl;

/**
 * The Class TRBBannerViewServiceImpl.
 */
@Service("TRBBannerViewServiceImpl")
public class TRBBannerViewServiceImpl extends AbstractService<Integer, TRBBannerView> implements ITRBBannerViewService {

	/** The logger. */
	private Logger logger = LogManager.getLogger(TRBBannerViewServiceImpl.class);

	/** The TRBViewBannerDao dao. */
	private ITRBBannerViewDao tRBBannerViewDao;

	/**
	 * Sets the dao.
	 *
	 * @param dao the new dao
	 */
	@Autowired
	public void setDao(ITRBBannerViewDao dao) {
		super.setDao(dao);
		logger.info("Inside @class TRBBannerViewServiceImpl inside @Method setDao");

		this.tRBBannerViewDao = dao;
	}

	/** The t RB banner dao. */
	@Autowired
	private ITRBBannerDao tRBBannerDao;

	/** The users dao. */
	@Autowired
	private UserDao usersDao;

	/**
	 * Creates the.
	 *
	 * @param trbbannerview the trbbannerview
	 * @return the TRB banner view
	 * @throws RestException the rest exception
	 */
	@Transactional
	public TRBBannerView create(@Valid TRBBannerView trbbannerview) throws RestException {
		logger.info("Create record by TRBViewBanner :" + trbbannerview);
		try {
			trbbannerview.setCreator(UserContextServiceImpl.getUserInContext());
			trbbannerview.setLastModifier(UserContextServiceImpl.getUserInContext());
			trbbannerview.setCreatedTime(new Date());
			trbbannerview.setModifiedTime(new Date());
			return super.create(trbbannerview);
		} catch (DataIntegrityViolationException ex) {
			logger.info("Inside @class TRBBannerViewServiceImpl inside @Method create");

			throw ex;
		} catch (ConstraintViolationException ex) {
			logger.info("Inside @class TRBBannerViewServiceImpl inside @Method create");
			throw new ValidationFailedException(ex);
		}
	}

	/**
	 * Update.
	 *
	 * @param trbbannerview the trbbannerview
	 * @return the TRB banner view
	 * @throws RestException the rest exception
	 */
	@Transactional
	public TRBBannerView update(@Valid TRBBannerView trbbannerview) throws RestException {
		logger.info("Update record by TRBViewBanner :" + trbbannerview);
		try {
			trbbannerview.setLastModifier(UserContextServiceImpl.getUserInContext());
			trbbannerview.setModifiedTime(new Date());
			return super.update(trbbannerview);
		} catch (DataIntegrityViolationException ex) {
			logger.error("Error occored inside @class TRBBannerViewServiceImpl @Method update");
			throw ex;

		} catch (ConstraintViolationException ex) {
			logger.error("Error occored inside @classTRBBannerViewServiceImpl @Method update");
			throw new ValidationFailedException(ex);
		}
	}

	/**
	 * Removes the.
	 *
	 * @param trbbanner the trbbanner
	 * @throws RestException the rest exception
	 */
	@Transactional
	public void remove(TRBBannerView trbbanner) throws RestException {
		logger.info("Remove record by TRBViewBanner :" + trbbanner);
		try {
			super.remove(trbbanner);
		} catch (DataIntegrityViolationException ex) {
			logger.error("Error occored inside @classTRBBannerViewServiceImpl @Method remove");
			throw new ValidationFailedException(ex);

		} catch (ConstraintViolationException ex) {
			logger.error("Error occored inside @class TRBBannerViewServiceImpl @Method remove");
			throw ex;
		}

	}

	/**
	 * Removes the by id.
	 *
	 * @param primaryKey the primary key
	 * @throws RestException the rest exception
	 */
	@Override
	@Transactional
	public void removeById(Integer primaryKey) throws RestException {
		logger.info("Remove record by primaryKey :" + primaryKey);
		try {
			super.removeById(primaryKey);
		} catch (DataIntegrityViolationException ex) {
			logger.error("Error occored inside @class TRBBannerViewServiceImpl @Method removeById");
			throw new ValidationFailedException(ex);

		} catch (ConstraintViolationException ex) {
			logger.error("Error occored inside @class TRBBannerViewServiceImpl @Method removeById");
			throw ex;
		}
	}

	/**
	 * Find by id.
	 *
	 * @param primaryKey the primary key
	 * @return the TRB banner view
	 * @throws RestException the rest exception
	 */
	@Transactional
	public TRBBannerView findById(Integer primaryKey) throws RestException {
		logger.info("find record by primaryKey :" + primaryKey);
		try {
			return super.findById(primaryKey);
		} catch (DataIntegrityViolationException ex) {
			logger.error("Error occored inside @classTRBBannerViewServiceImpl @Method findById");
			throw new ValidationFailedException(ex);

		} catch (ConstraintViolationException ex) {
			throw ex;
		}
	}

	/**
	 * Creates the banner view.
	 *
	 * @param bannerId the banner id
	 * @return the string
	 */
	@Transactional
	public String createBannerView(Integer bannerId) {
		try {
			Integer userId = UserContextServiceImpl.getUserInContext().getUserid();
			TRBBannerView oldBannerView = tRBBannerViewDao.getBannerViewByBannerIdAndUserId(bannerId, userId);
			if (oldBannerView != null) {
				// no action
			} else {
				TRBBannerView bannerView = new TRBBannerView();
				TRBBanner banner = tRBBannerDao.findByPk(bannerId);
				bannerView.setBanner(banner);
				bannerView.setViewer(usersDao.findByPk(userId));
				tRBBannerViewDao.create(bannerView);
				Long count = tRBBannerViewDao.getBannerCountByBannerId(bannerId);
				if (count != null) {
					banner.setViewsCount(count);
					return "{\"count\":\"" + tRBBannerDao.update(banner).getViewsCount() + "\"}";
				}
			}
		} catch (Exception e) {
			logger.info("unable to create banner view.");
		}
		return TRBConstants.FAILURE_JSON;
	}

	/**
	 * Gets the search record count.
	 *
	 * @param context the context
	 * @return the search record count
	 */
	@Transactional
	public Integer getSearchRecordCount(SearchContext context) {
		logger.info("TRBBannerViewServiceImpl" + " @method getSearchRecordCount");
		try {
			return tRBBannerViewDao.getSearchRecordCount(context);
		} catch (Exception ex) {
			logger.error("Error  occurred  @class TRBBannerViewServiceImpl" + "TRBBannerViewServiceImpl", ex);
		}
		return 0;
	}
	
	
	@Override
	public Integer activeBannerCount() {		
		return tRBBannerViewDao.activeBannerCount();
	}

	@Override
	public Integer inactiveBannerCount() {
		
		return tRBBannerViewDao.inactiveBannerCount();
	}

	@Override
	public Integer draftBannerCount() {
		
		return tRBBannerViewDao.draftBannerCount();
	}
	
}

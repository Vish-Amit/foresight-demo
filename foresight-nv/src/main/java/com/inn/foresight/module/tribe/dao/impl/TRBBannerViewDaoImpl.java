package com.inn.foresight.module.tribe.dao.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.cxf.jaxrs.ext.search.SearchCondition;
import org.apache.cxf.jaxrs.ext.search.SearchConditionVisitor;
import org.apache.cxf.jaxrs.ext.search.SearchContext;
import org.apache.cxf.jaxrs.ext.search.jpa.JPATypedQueryVisitor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.module.tribe.dao.ITRBBannerViewDao;
import com.inn.foresight.module.tribe.model.TRBBannerView;
import com.inn.product.um.user.service.impl.UserContextServiceImpl;

/**
 * Dao .
 */
@Repository("TRBBannerViewDaoImpl")
public class TRBBannerViewDaoImpl extends HibernateGenericDao<Integer, TRBBannerView> implements ITRBBannerViewDao {

	/** The logger. */
	private Logger logger = LogManager.getLogger(TRBBannerViewDaoImpl.class);

	/**
	 * Instantiates a new cellTRBBannerView dao impl.
	 */
	public TRBBannerViewDaoImpl() {
		super(TRBBannerView.class);

	}

	/**
	 * Returns the new TRBBannerView record.
	 *
	 * @param trbbannerview the trbbannerview
	 * @return the TRBBannerView
	 * @parameter TRBBannerView of type TRBBannerView
	 * @returns a new TRBBannerView
	 */

	/**
	 * 
	 * Returns the new TRBBannerView record
	 * 
	 * @parameter trbbanner of type TRBBannerView
	 * @returns a new TRBBannerView
	 * 
	 */
	
	public TRBBannerView create(@Valid TRBBannerView trbbannerview) {
		logger.info("@class"+this.getClass().getName()+"@Method create Create record by an entity :" + trbbannerview.getId());
		trbbannerview.setCreator(UserContextServiceImpl.getUserInContext());
		trbbannerview.setLastModifier(UserContextServiceImpl.getUserInContext());
		trbbannerview.setCreatedTime(new Date());
		trbbannerview.setModifiedTime(new Date());
		try {
			return super.create(trbbannerview);
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * Returns the updated TRBBannerView record.
	 *
	 * @param trbbannerview the trbbannerview
	 * @return the TRB banner view
	 * @throws DaoException 
	 * @parameter anEntity of type TRBBannerView
	 * @returns a updated TRBBannerView record
	 */
	
	public TRBBannerView update(@Valid TRBBannerView trbbannerview) throws DaoException {
		logger.info("@class"+this.getClass().getName()+"@Method update record by an entity of Id:"+trbbannerview.getId());
		trbbannerview.setLastModifier(UserContextServiceImpl.getUserInContext());
		trbbannerview.setModifiedTime(new Date());
		return super.update(trbbannerview);
	}

	/**
	 * Method to remove TRBBannerView record.
	 *
	 * @param trbbanner the trbbanner
	 * @throws DaoException 
	 * @parameter trbbanner of type TRBBannerView
	 */
	
	public void delete(@Valid TRBBannerView trbbanner) throws DaoException {
		logger.info("@class"+this.getClass().getName()+"@Method delete() Deleting record by an entity :"+trbbanner.getId());
		super.delete(trbbanner);

	}

	/**
	 * Method to remove TRBBannerView record by primary key.
	 *
	 * @param integerPk the integer pk
	 * @throws DaoException 
	 * @parameter primary key of type Integer
	 */
	
	public void deleteByPk(@NotNull Integer integerPk) throws DaoException {
		logger.info("@class"+this.getClass().getName()+"@Method Deleting record by primary key :@param" + integerPk);
		super.deleteByPk(integerPk);

	}

	/**
	 * Returns the list of TRBBannerView record.
	 *
	 * @return the list
	 * @throws DaoException 
	 * @returns TRBBannerView record
	 */
	
	public List<TRBBannerView> findAll() throws DaoException {
		logger.info("@class"+this.getClass().getName()+"@Method Inside class TRBBennerViewDaoImpl @Method List");
		return super.findAll();

	}

	/**
	 * Returns the record of TRBBannerView finding by primary key.
	 *
	 * @param integerPk the integer pk
	 * @return the TRB banner view
	 * @throws DaoException 
	 * @parameter primary key of type Integer
	 * @returns a TRBBannerView record
	 */
	
	public TRBBannerView findByPk(@NotNull Integer integerPk) throws DaoException {
		logger.info("@class"+this.getClass().getName()+"@Method Find record by Primary Key :" + integerPk);
		return super.findByPk(integerPk);

	}

	/**
	 * Gets the banner view by banner id and user id.
	 *
	 * @param bannerId the banner id
	 * @param userId the user id
	 * @return the banner view by banner id and user id
	 */
	public TRBBannerView getBannerViewByBannerIdAndUserId(Integer bannerId, Integer userId) {
		try {
			logger.info("@class"+this.getClass().getName()+"@Method getBannerViewByBannerIdAndUserId");
			return (TRBBannerView) getEntityManager().createNamedQuery("getBannerViewByBannerIdAndUserId")
					.setParameter("bannerId", bannerId).setParameter("userId", userId).getSingleResult();
		} catch (NoResultException nre) {
			logger.debug(
					" error @class"+this.getClass().getName()+"@Method getBannerViewByBannerIdAndUserId no result found corresponding to" + bannerId + " and " + userId + "@TRBBannerViewDemoImpl");
			return null;
		}
	}

	/**
	 * Delete banner views by banner id.
	 *
	 * @param bannerId the banner id
	 */
	public void deleteBannerViewsByBannerId(Integer bannerId) {
		logger.info(" @class"+this.getClass().getName()+"@Method deleteBannerViewsByBannerId");
		try {
			getEntityManager().createNamedQuery("deleteBannerViewsByBannerId").setParameter("bannerId", bannerId)
					.executeUpdate();
		} catch (Exception e) {
			logger.debug("@class"+this.getClass().getName()+"@Method some problem in deleting banner views : " + e.getMessage());
			logger.debug("some problem in deleting banner views : " + e.getMessage());
		}
	}
	
	/**
	 * Gets the total count.
	 *
	 * @return the total count
	 */
	public Long getTotalCount() {
		logger.info("@class"+this.getClass().getName()+"@Method getTotalCount");
		return (Long) this.getEntityManager().createQuery("select count(x) from TRBBannerView x").getSingleResult();
	}
	
	/**
	 * Gets the search record count.
	 *
	 * @param context the context
	 * @return the search record count
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Integer getSearchRecordCount(SearchContext context) {
		Integer count = 0;
		try {
			logger.info("@class"+this.getClass().getName()+"@Method getSearchRecordCount");
			SearchCondition sc = null;
			if (context != null) {
				sc = context.getCondition((Class) TRBBannerView.class);
				logger.info("@class"+this.getClass().getName()+"@Method getSearchRecordCount if (context != null)");

				if (sc != null) {
					logger.info("@class"+this.getClass().getName()+"@Method getSearchRecordCount if (context != null) and if (sc != null)");

					JPATypedQueryVisitor visitor = new JPATypedQueryVisitor(this.getEntityManager(), this.getType());
					sc.accept((SearchConditionVisitor) visitor);
					visitor.visit(sc);
					TypedQuery typedQuery = visitor.getTypedQuery();
					count = typedQuery.getResultList().size();
				} else {
					logger.info("@class"+this.getClass().getName()+"@Method getSearchRecordCount if (context != null) and if (sc == null)");
					Long l = this.getTotalCount();
					count = l.intValue();
				}
			}
		} catch (Exception ex) {
			logger.error("Error   @class"+this.getClass().getName()+"@Method getSearchRecordCount ", ex);
			logger.error("Error  occurred  @class" + this.getClass().getName(), ex);
		}
		return count;
	}
	
	/**
	 * Gets the banner count by banner id.
	 *
	 * @param bannerId the banner id
	 * @return the banner count by banner id
	 */
	public Long getBannerCountByBannerId(Integer bannerId) {
		try {
			logger.info("@class TRBBannerDaoImpl @Method getBannerCountByBannerId @Param bannerId "+bannerId);
			return (Long) getEntityManager().createNamedQuery("getBannerCountByBannerId")
					.setParameter("bannerId", bannerId).getSingleResult();
		} catch (Exception e) {
			logger.error("error in @class TRBBannerDaoImpl @Method getBannerCountByBannerId to:"+e.getMessage());
			return null;
		}
	}
	@Override
	public Integer activeBannerCount() {
		try {
			logger.info("@class TRBBannerDaoImpl @Method activeBannerCount ");
			Long count =(Long)getEntityManager().createNamedQuery("activeBannerCount").getSingleResult();
			Integer activeBannerCount=count.intValue();
			return activeBannerCount ;
		} catch (NoResultException nre) {
			nre.printStackTrace();			
			logger.error("Error @class TRBBannerDaoImpl @Method activeBannerCount", nre.getMessage());
			return 0;
		}
	}

	@Override
	public Integer inactiveBannerCount() {
		try {
			logger.info("@class TRBBannerDaoImpl @Method inactiveBannerCount ");
			Long count =(Long) getEntityManager().createNamedQuery("inactiveBannerCount").getSingleResult();
			Integer inactiveBannerCount=count.intValue();
			return inactiveBannerCount ;
		} catch (NoResultException nre) {
			nre.printStackTrace();			
			logger.error("Error @class TRBBannerDaoImpl @Method inactiveBannerCount", nre.getMessage());
			return 0;
		}
	}

	@Override
	public Integer draftBannerCount() {
		try {
			logger.info("@class TRBBannerDaoImpl @Method draftBannerCount ");

			Long count =(Long) getEntityManager().createNamedQuery("draftBannerCount").getSingleResult();
			Integer draftBannerCount=count.intValue();
			return draftBannerCount ;
		} catch (NoResultException nre) {
			nre.printStackTrace();			
			logger.error("Error @class TRBBannerDaoImpl @Method draftBannerCount", nre.getMessage());
			return 0;
		}
	}
}

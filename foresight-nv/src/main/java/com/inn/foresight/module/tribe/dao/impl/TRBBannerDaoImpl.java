package com.inn.foresight.module.tribe.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.inn.foresight.module.tribe.dao.ITRBBannerDao;
import com.inn.foresight.module.tribe.model.TRBBanner;
import com.inn.product.um.user.service.impl.UserContextServiceImpl;

/**
 * Dao .
 */
@Repository("TRBBannerDaoImpl")
public class TRBBannerDaoImpl extends HibernateGenericDao<Integer, TRBBanner> implements ITRBBannerDao {

	/** The logger. */
	private Logger logger = LogManager.getLogger(TRBBannerDaoImpl.class);

	/**
	 * Instantiates a new cellTRBBanner dao impl.
	 */
	public TRBBannerDaoImpl() {
		super(TRBBanner.class);
	}

	/**
	 * Returns the new TRBBanner record.
	 *
	 * @param trbbanner the trbbanner
	 * @return the TRBBanner
	 * @parameter TRBBanner of type TRBBanner
	 * @returns a new TRBBanner
	 */

	/**
	 * 
	 * Returns the new TRBBanner record
	 * @throws DaoException 
	 * 
	 * @parameter trbbanner of type TRBBanner
	 * @returns a new TRBBanner
	 * 
	 */

	public TRBBanner create(@Valid TRBBanner trbbanner) throws DaoException {
		logger.info("Create record by an entity :" + trbbanner);
		trbbanner.setCreator(UserContextServiceImpl.getUserInContext());
		trbbanner.setLastModifier(UserContextServiceImpl.getUserInContext());
		trbbanner.setCreatedTime(new Date());
		trbbanner.setModifiedTime(new Date());
		return super.create(trbbanner);

	}

	/**
	 * Returns the updated TRBBanner record.
	 *
	 * @param trbbanner the trbbanner
	 * @return the TRB banner
	 * @throws DaoException 
	 * @parameter anEntity of type TRBBanner
	 * @returns a updated TRBBanner record
	 */

	public TRBBanner update(@Valid TRBBanner trbbanner) throws DaoException {
		logger.info("@class TRBBannerDaoImpl @Method update record by an entity of Id:" + trbbanner.getId());
		trbbanner.setLastModifier(UserContextServiceImpl.getUserInContext());
		trbbanner.setModifiedTime(new Date());
		return super.update(trbbanner);
	}

	/**
	 * Method to remove TRBBanner record.
	 *
	 * @param trbbanner the trbbanner
	 * @throws DaoException 
	 * @parameter trbbanner of type TRBBanner
	 */

	public void delete(@Valid TRBBanner trbbanner) throws DaoException {
		logger.info("@class TRBBannerDaoImpl @Method Deleting record by an entity : trbbanner Id" + trbbanner.getId());
		super.delete(trbbanner);

	}

	/**
	 * Method to remove TRBBanner record by primary key.
	 *
	 * @param integerPk the integer pk
	 * @throws DaoException 
	 * @parameter primary key of type Integer
	 */

	public void deleteByPk(@NotNull Integer integerPk) throws DaoException {
		logger.info("@class TRBBannerDaoImpl @Method  Deleting record by primary key @param:" + integerPk);
		super.deleteByPk(integerPk);

	}

	/**
	 * Returns the list of TRBBanner record.
	 *
	 * @return the list
	 * @throws DaoException 
	 * @returns TRBBanner record
	 */

	public List<TRBBanner> findAll() throws DaoException {
		logger.info("@Method Inside class TRBBannerImpl @Method findAll");
		return super.findAll();

	}

	/**
	 * Returns the record of TRBBanner finding by primary key.
	 *
	 * @param integerPk the integer pk
	 * @return the TRB banner
	 * @throws DaoException 
	 * @parameter primary key of type Integer
	 * @returns a TRBBanner record
	 */

	public TRBBanner findByPk(@NotNull Integer integerPk) throws DaoException {
		logger.info("@class TRBBannerDaoImpl @Method  Find record by Primary Key :@param" + integerPk);
		return super.findByPk(integerPk);

	}

	/**
	 * Find by banner title.
	 *
	 * @param title the title
	 * @return the TRB banner
	 */
	public TRBBanner findByBannerTitle(String title) {
		try {
			logger.info("@class TRBBannerDaoImpl @Method findBannerTitle @param title :" + title);
			return (TRBBanner) getEntityManager().createNamedQuery("findByBannerTitle").setParameter("title", title)
					.getSingleResult();
		} catch (NoResultException nre) {
			logger.debug("Error Occured @class TRBBannerDaoImpl  @Method findBannerTitle @param title :" + title
					+ " @Exception :", nre);
			return null;
		}
	}

	/**
	 * Gets the total count.
	 *
	 * @return the total count
	 */
	public Long getTotalCount() {
		logger.info("@class TRBBannerDaoImpl @Method getTotalCount");
		return (Long) this.getEntityManager().createQuery("select count(x) from TRBBanner x").getSingleResult();
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
			logger.info("@class TRBBannerDaoImpl @Method getSearchRecordCount");

			SearchCondition sc = null;
			if (context != null) {
				sc = context.getCondition((Class) TRBBanner.class);
				logger.info("@class TRBBannerDaoImpl @Method getSearchRecordCount if(context != null)");

				if (sc != null) {
					logger.info(
							"@class TRBBannerDaoImpl @Method getSearchRecordCount if(context != null) and if (sc != null) ");
					JPATypedQueryVisitor visitor = new JPATypedQueryVisitor(this.getEntityManager(), this.getType());
					sc.accept((SearchConditionVisitor) visitor);
					visitor.visit(sc);
					TypedQuery typedQuery = visitor.getTypedQuery();
					count = typedQuery.getResultList().size();
				} else {
					logger.info(
							"@class TRBBannerDaoImpl @Method getSearchRecordCount if(context != null) and if (sc == null) ");
					Long l = this.getTotalCount();
					count = l.intValue();
				}
			}
		} catch (Exception ex) {
			logger.error("Error  occurred @class TRBBannerDaoImpl  @Method getBannerCountByBannerId", ex);
		}
		return count;
	}

	/**
	 * Gets the existed banner ids.
	 *
	 * @return the existed banner ids
	 */
	@SuppressWarnings("unchecked")
	public List<Integer> getExistedBannerIds() {
		try {
			logger.info("@class TRBBannerDaoImpl @Method getExistedBannerIds ");

			return getEntityManager().createNamedQuery("getExistedBannerIds").getResultList();
		} catch (NoResultException nre) {
			logger.error("Error @class TRBBannerDaoImpl @Method getExistedBannerIds", nre.getMessage());
		}
		return new ArrayList<Integer>();
	}
	
	@Override
	public Map<String,Integer> getBannerCountByStatus() {
		logger.info("@class" + this.getClass().getName() + "@Method getBannerCountByStatus");
		Map<String,Integer> res= new HashMap<String, Integer>();
		
		try {
			List<Object[]> list = getEntityManager().createNativeQuery("select status, count(*) from Banner GROUP BY status").getResultList();
			for (Object[] obj : list) {
				res.put(obj[0].toString(), Integer.parseInt(obj[1].toString()));
			}
		} catch (Exception e) {
			logger.error("error in @class" + this.getClass().getName() + "@Method getBannerCountByStatus ",e.getMessage());
		}
		
		return res;
	}

	@Override
	public void markExpiredBanners() {
		try {
			logger.info("@class TRBBannerDaoImpl @Method markExpiredBanners");
			getEntityManager().createNativeQuery("update Banner b set b.status='INACTIVE' where b.status='ACTIVE' and b.endtime<=now()").executeUpdate();
		} catch (Exception nre) {
			logger.debug("Error Occured @class TRBBannerDaoImpl  @Method markExpiredBanners  @Exception :", nre);
		}
		
	}
	
	
}

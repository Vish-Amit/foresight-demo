package com.inn.foresight.core.adminplanning.dao.impl;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.TransactionRequiredException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.utils.Preconditions;
import com.inn.foresight.core.adminplanning.dao.IAlgorithmDao;
import com.inn.foresight.core.adminplanning.model.Algorithm;

/**
 * The Class AlgorithmDaoImpl.
 */
@Repository("AlgorithmDaoImpl")
public class AlgorithmDaoImpl extends HibernateGenericDao<Integer, Algorithm> implements IAlgorithmDao {

	/** The logger. */
	private Logger logger = LogManager.getLogger(AlgorithmDaoImpl.class);

	/**
	 * Instantiates a new algorithm dao impl.
	 */
	public AlgorithmDaoImpl() {
		super(Algorithm.class);
	}

	/* (non-Javadoc)
	 * @see com.inn.foresight.module.algoplanning.dao.IAlgorithmDao#getAllAlgorithms(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public List<Algorithm> getAllAlgorithms() {
		try {
			Query query = this.getEntityManager().createNamedQuery("getAllAlgorithms");
			enableParentFilter();
			enableDeletedFilter(false);
			enableChildDeletedFilter();
			return query.getResultList();
		} catch (TransactionRequiredException tr) {
			throw new DaoException(tr.getLocalizedMessage());
		} catch (Exception e) {
			logger.error("Exception in getAllAlgorithms : {}", com.inn.foresight.core.generic.utils.Utils.getStackTrace(e));
			throw new DaoException(e.getMessage());
		}
	}

	/**
	 * Enable parent filter.
	 */
	private void enableParentFilter() {
		try {
			Session s = (Session) getEntityManager().getDelegate();
			s.enableFilter("topParentFilter");
		} catch (Exception e) {
			logger.error("Could not enable parent filter : {}", com.inn.foresight.core.generic.utils.Utils.getStackTrace(e));
		}

	}

	/**
	 * Enable deleted filter.
	 *
	 * @param deleted the deleted
	 */
	private void enableDeletedFilter(Boolean deleted) {
		try {
			Preconditions.checkNotNull(deleted);
			Session s = (Session) getEntityManager().getDelegate();
			s.enableFilter("deletedFilter").setParameter("deleted", deleted);
		} catch (RestException re) {
			logger.error("Could not enable deleted filter : deleted field is null :{}",com.inn.foresight.core.generic.utils.Utils.getStackTrace(re));
		} catch (Exception e) {
			logger.error("Could not enable deleted filter : {}", com.inn.foresight.core.generic.utils.Utils.getStackTrace(e));
		}

	}
	
	/**
	 * Enable child deleted filter.
	 */
	private void enableChildDeletedFilter() {
		try {
			Session s = (Session) getEntityManager().getDelegate();
			s.enableFilter("childDeleteFilter");
		} catch (Exception e) {
			logger.error("Could not enable deleted filter : {}", com.inn.foresight.core.generic.utils.Utils.getStackTrace(e));
		}

	}

	/* (non-Javadoc)
	 * @see com.inn.foresight.module.algoplanning.dao.IAlgorithmDao#searchAlgorithmData(java.lang.String)
	 */
	@Override
	public List<Algorithm> searchAlgorithmData(String displayName) {
		try {
			Query query = this.getEntityManager().createNamedQuery("searchAlgorithmByDisplayName");
			query.setParameter("displayName", displayName);
			enableParentFilter();
			enableDeletedFilter(false);
			return query.getResultList();
		} catch (Exception e) {
			logger.error("Excption in @searchAlgorithmData method wirth error : {}", com.inn.foresight.core.generic.utils.Utils.getStackTrace(e));
			throw new DaoException(e.getCause());
		}
	}

}
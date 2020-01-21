package com.inn.foresight.core.infra.dao.impl;

import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.core.infra.dao.IWifiSiteDetailDao;
import com.inn.foresight.core.infra.model.WifiSiteDetail;
import com.inn.foresight.core.infra.wrapper.WifiWrapper;

/**
 * The Class WifiSiteDetailDaoImpl.
 */
@Repository("WifiSiteDetailDaoImpl")
public class WifiSiteDetailDaoImpl extends HibernateGenericDao<Integer, WifiSiteDetail> implements IWifiSiteDetailDao {
	
	/**
	 * Instantiates a new wifi site detail dao impl.
	 */
	public WifiSiteDetailDaoImpl() {
		super(WifiSiteDetail.class);
	}

	/** The logger. */
	private Logger logger = LogManager.getLogger(WifiSiteDetailDaoImpl.class);

	/**
	 * Creates the.
	 *
	 * @param wifiSiteDetail the wifi site detail
	 * @return the wifi site detail
	 * @throws DaoException the dao exception
	 */
	@Override
	public WifiSiteDetail create(WifiSiteDetail wifiSiteDetail) {
		return super.create(wifiSiteDetail);
	}
	
	@Override
	public WifiWrapper getWifiDetailByNEId(String neId) {
		logger.info("Going to get Wifi detail by neId {}",neId);
		WifiWrapper wifiWrapper=null;
		try {
			Query query = getEntityManager().createNamedQuery("getWifiDetailByNEId");
			query.setParameter("neId", neId);
			wifiWrapper = (WifiWrapper)query.getSingleResult();
		} catch (PersistenceException e) {
			logger.warn("Exception while getting wifi detail err msg {}",e);
		}
		logger.info("Return wifi detail {}",wifiWrapper);
		return wifiWrapper;
	}

}
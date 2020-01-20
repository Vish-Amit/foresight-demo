package com.inn.foresight.module.nv.wpt.analytics.dao.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.module.nv.wpt.analytics.dao.IWPTLocationDao;
import com.inn.foresight.module.nv.wpt.analytics.model.WPTLocation;

/** The Class WPTLocationDaoImpl. */
@Repository("WPTLocationDaoImpl")
public class WPTLocationDaoImpl  extends HibernateGenericDao<Integer, WPTLocation> implements IWPTLocationDao{

	/** The logger. */
	private Logger logger = LogManager.getLogger(WPTLocationDaoImpl.class);
	
	/** Construct WPTLocationDaoImpl object/instance. */
	public WPTLocationDaoImpl() {
		super(WPTLocation.class);
	}

	/**
	 * Finding All records from WPTLocation.
	 *
	 * @return the list
	 * @throws DaoException the dao exception
	 */
	@Override
	public List<WPTLocation> findAll() {
		logger.info("Finding All records");
		return super.findAll();
	}
	
}

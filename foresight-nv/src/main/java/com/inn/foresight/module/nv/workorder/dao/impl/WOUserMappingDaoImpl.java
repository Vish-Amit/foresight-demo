package com.inn.foresight.module.nv.workorder.dao.impl;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;
import com.inn.foresight.module.nv.workorder.dao.IWOUserMappingDao;
import com.inn.foresight.module.nv.workorder.model.WOUserMapping;

/**
 * The Class WOUserMappingDaoImpl.
 */
@Repository("WOUserMappingDaoImpl")
public class WOUserMappingDaoImpl extends HibernateGenericDao<Integer, WOUserMapping> implements IWOUserMappingDao {

	/**
	 * Instantiates a new WO user mapping dao impl.
	 */
	public WOUserMappingDaoImpl() {
		super(WOUserMapping.class);
	}	

	/** The logger. */
	private Logger logger = LogManager.getLogger(WOUserMappingDaoImpl.class);


	@SuppressWarnings("unchecked")
	@Override
	public List<WOUserMapping> findByWorkorderId(Integer woId) {
		logger.info("Going to get WOUserMapping List from workorderId {}", woId);
		Query query = getEntityManager().createNamedQuery("getWOUserByGWOId");
		query.setParameter(NVWorkorderConstant.WORKORDER_ID, woId);
		try {
			List<WOUserMapping> list = query.getResultList();
			logger.info(NVWorkorderConstant.RESULT_SIZE_LOGGER, list.size());
			return list;
		} catch (NoResultException e) {
			throw new DaoException(NVWorkorderConstant.DATA_NOT_FOUND);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WOUserMapping> findByUserId(Integer userid) {
		logger.info("Going to get WOUserMapping List from workorderId {}", userid);
		Query query = getEntityManager().createNamedQuery("getWOUseByUserId");
		query.setParameter(NVWorkorderConstant.USER_ID, userid);
		try {
			List<WOUserMapping> list = query.getResultList();
			logger.info(NVWorkorderConstant.RESULT_SIZE_LOGGER, list.size());
			return list;
		} catch (NoResultException e) {
			throw new DaoException(NVWorkorderConstant.DATA_NOT_FOUND);
		}
	}

}

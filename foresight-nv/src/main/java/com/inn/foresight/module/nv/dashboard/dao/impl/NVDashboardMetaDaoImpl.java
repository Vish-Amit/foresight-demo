package com.inn.foresight.module.nv.dashboard.dao.impl;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.dashboard.dao.INVDashboardMetaDao;
import com.inn.foresight.module.nv.dashboard.model.NVDashboardMeta;
import com.inn.foresight.module.nv.dashboard.utils.NVDashboardConstants;

/**
 * The Class NVDashboardMetaDaoImpl.
 *
 * @author innoeye
 */
@Repository("NVDashboardMetaDaoImpl")
public class NVDashboardMetaDaoImpl extends HibernateGenericDao<Integer, NVDashboardMeta> implements INVDashboardMetaDao {

	/** The logger. */
	private Logger logger = LogManager.getLogger(NVDashboardMetaDaoImpl.class);

	/** Instantiates a new NV dashboard meta dao impl. */
	public NVDashboardMetaDaoImpl() {
		super(NVDashboardMeta.class);
	}
	
	/**
	 * Gets the all meta data.
	 *
	 * @return the all meta data
	 * @throws DaoException the dao exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<NVDashboardMeta> getAllMetaData(){
		logger.info("In getAllMetaData");

		List<NVDashboardMeta> nvDashboardMetaList = null;
		Query query = null;

		try{
			query = getEntityManager().createNamedQuery(NVDashboardConstants.GET_ALL_NVDASHBOARDMETA_QUERY);
			nvDashboardMetaList = query.getResultList();
		}catch(NoResultException e){
			logger.error("Exception while fetching user count data by date :{}",e.getMessage());
			throw new DaoException(NVDashboardConstants.NO_DATA_MESSAGE);
		}catch (Exception e){
			logger.error("Exception while fetching user count data by date :{}",Utils.getStackTrace(e));
			throw new DaoException(e.getMessage());
		}
		return nvDashboardMetaList;
	}

}

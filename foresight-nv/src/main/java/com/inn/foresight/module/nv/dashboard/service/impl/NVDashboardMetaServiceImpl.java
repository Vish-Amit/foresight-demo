package com.inn.foresight.module.nv.dashboard.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.service.impl.AbstractService;
import com.inn.foresight.module.nv.dashboard.dao.INVDashboardMetaDao;
import com.inn.foresight.module.nv.dashboard.model.NVDashboardMeta;
import com.inn.foresight.module.nv.dashboard.service.INVDashboardMetaService;

/** The Class NVDashboardMetaServiceImpl. */
@Service("NVDashboardMetaServiceImpl")
@Transactional
public class NVDashboardMetaServiceImpl extends AbstractService<Integer, NVDashboardMeta> implements INVDashboardMetaService {

	/** The logger. */
	private Logger logger = LogManager.getLogger(NVDashboardMetaServiceImpl.class);

	/** The inv dashboard meta dao. */
	@Autowired
	private INVDashboardMetaDao invDashboardMetaDao;

	/**
	 * Sets the dao.
	 *
	 * @param dao the new dao
	 */
	@Autowired
	public void setDao(INVDashboardMetaDao dao) {
		super.setDao(dao);
		invDashboardMetaDao = dao;
	}

	/**
	 * Gets the all nv dashboard meta data.
	 *
	 * @return the all nv dashboard meta data
	 * @throws RestException the rest exception
	 */
	@Override
	public List<NVDashboardMeta> getAllNvDashboardMetaData() {
		try {
			logger.info("In getAllNvDashboardMetaData");
			return invDashboardMetaDao.getAllMetaData();
		} catch (Exception e) {
			throw new RestException(e.getMessage());
		}
	}
}

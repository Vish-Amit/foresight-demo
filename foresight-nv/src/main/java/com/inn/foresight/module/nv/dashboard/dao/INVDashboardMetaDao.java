package com.inn.foresight.module.nv.dashboard.dao;

import java.util.List;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.module.nv.dashboard.model.NVDashboardMeta;

/** The Interface INVDashboardMetaDao. */
public interface INVDashboardMetaDao extends IGenericDao<Integer, NVDashboardMeta> {

/**
 * Gets the all meta data.
 *
 * @return the all meta data
 * @throws DaoException the dao exception
 */
List<NVDashboardMeta> getAllMetaData();

}

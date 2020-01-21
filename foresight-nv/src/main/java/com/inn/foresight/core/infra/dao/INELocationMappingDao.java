package com.inn.foresight.core.infra.dao;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.foresight.core.infra.model.NELocation;
import com.inn.foresight.core.infra.model.NELocationMapping;

/**
 * The Interface INELocationMappingDao.
 */
public interface INELocationMappingDao extends IGenericDao<Integer, NELocationMapping> {


	NELocation getParentNELocationByNELType(Integer nelocationid_fk, String neltype);

}

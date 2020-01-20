package com.inn.foresight.module.nv.core.workorder.dao;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.foresight.module.nv.core.workorder.model.GWOMeta;

public interface IGWOMetaDao extends IGenericDao<Integer, GWOMeta> {


	GWOMeta getGwoMetaDataByGenericWorkorderId(Integer genericWorkorderId, String entityType);

	GWOMeta getGwoMetaDataByGenericWorkorderIdforReport(Integer genericWorkorderId, String entityType);

}

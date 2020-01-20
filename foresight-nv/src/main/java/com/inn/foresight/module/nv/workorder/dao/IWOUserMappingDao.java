package com.inn.foresight.module.nv.workorder.dao;

import java.util.List;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.foresight.module.nv.workorder.model.WOUserMapping;

/**
 * The Interface IWOUserMappingDao.
 */
public interface IWOUserMappingDao extends IGenericDao<Integer, WOUserMapping> {

	List<WOUserMapping> findByWorkorderId(Integer woId);

	List<WOUserMapping> findByUserId(Integer userid);

}

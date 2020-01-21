package com.inn.foresight.core.infra.dao;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.foresight.core.infra.model.WifiSiteDetail;
import com.inn.foresight.core.infra.wrapper.WifiWrapper;

/**
 * The Interface IWifiSiteDetailDao.
 */
public interface IWifiSiteDetailDao extends IGenericDao<Integer, WifiSiteDetail> {

	WifiWrapper getWifiDetailByNEId(String neId);

}
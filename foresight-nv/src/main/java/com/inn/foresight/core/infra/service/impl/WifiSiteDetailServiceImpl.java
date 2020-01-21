package com.inn.foresight.core.infra.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.core.generic.service.impl.AbstractService;
import com.inn.foresight.core.infra.dao.IWifiSiteDetailDao;
import com.inn.foresight.core.infra.model.WifiSiteDetail;
import com.inn.foresight.core.infra.service.IWifiSiteDetailService;

/**
 * The Class WifiSiteDetailServiceImpl.
 */
@Service("WifiSiteDetailServiceImpl")
public class WifiSiteDetailServiceImpl extends AbstractService<Integer, WifiSiteDetail> implements IWifiSiteDetailService {

	/** The logger. */
	private Logger logger = LogManager.getLogger(WifiSiteDetailServiceImpl.class);

	/** The wifi site detail dao. */
	@Autowired
	private IWifiSiteDetailDao wifiSiteDetailDao;

	/**
	 * Sets the dao.
	 *
	 * @param wifiSiteDetailDao the new dao
	 */
	public void setDao(IWifiSiteDetailDao wifiSiteDetailDao) {
		super.setDao(wifiSiteDetailDao);
		this.wifiSiteDetailDao = wifiSiteDetailDao;
	}

}
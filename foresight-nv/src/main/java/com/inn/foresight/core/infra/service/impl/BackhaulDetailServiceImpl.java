package com.inn.foresight.core.infra.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.service.impl.AbstractService;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.infra.dao.IBackhaulDetailDao;
import com.inn.foresight.core.infra.dao.INetworkElementDao;
import com.inn.foresight.core.infra.model.AdvanceSearch;
import com.inn.foresight.core.infra.model.BackhaulDetail;
import com.inn.foresight.core.infra.model.NetworkElement;
import com.inn.foresight.core.infra.service.IAdvanceSearchProvider;
import com.inn.foresight.core.infra.service.IBackhaulDetailService;
import com.inn.foresight.core.infra.utils.InfraConstants;


@Service("BackhaulDetailServiceImpl")
public class BackhaulDetailServiceImpl extends AbstractService<Integer, BackhaulDetail>
		implements IBackhaulDetailService, IAdvanceSearchProvider {

	/** The logger. */
	private Logger logger = LogManager.getLogger(BackhaulDetailServiceImpl.class);

	/** The i element detail dao. */
	@Autowired
	IBackhaulDetailDao iBackhaulDetailDao;

	/** The network element dao. */
	@Autowired
	INetworkElementDao networkElementDao;

	/**
	 * Sets the dao.
	 *
	 * @param dao
	 *            the new dao
	 */
	@Autowired
	public void setDao(IBackhaulDetailDao dao) {
		super.setDao(dao);
		this.iBackhaulDetailDao = dao;
	}

	 @Override
		public Object getSearchData(Map<String, Object> map) {
		  Map<String, Object> siteDetailMap = new HashMap<>();
			try {
				AdvanceSearch search = (AdvanceSearch) map.get(InfraConstants.ADVANCESEARCH);
				NetworkElement networkElementDetail = networkElementDao.getNetworkElementByIPV4(search.getName());
			if (networkElementDetail != null && networkElementDetail.getNeType() != null) {
					siteDetailMap.put(ForesightConstants.NE_NAME, networkElementDetail.getNeName());
					siteDetailMap.put(ForesightConstants.NE_TYPE, networkElementDetail.getNeType().name());
					siteDetailMap.put(ForesightConstants.NE_STATUS, networkElementDetail.getNeStatus());
					siteDetailMap.put(ForesightConstants.LATITUDE, networkElementDetail.getLatitude());
					siteDetailMap.put(ForesightConstants.LONGITUDE, networkElementDetail.getLongitude());
				} else {
					throw new RestException(ForesightConstants.EXCEPTION_NO_RECORD_FOUND);
				}
			} catch (Exception exception) {
				logger.error("Error while getting network element detail. Exception : {}", ExceptionUtils.getStackTrace(exception));
				throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
			}

			return siteDetailMap;
		}

}

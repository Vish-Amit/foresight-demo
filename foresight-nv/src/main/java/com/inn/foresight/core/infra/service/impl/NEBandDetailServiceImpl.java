package com.inn.foresight.core.infra.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.service.impl.AbstractService;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.infra.dao.INEBandDetailDao;
import com.inn.foresight.core.infra.dao.INetworkElementDao;
import com.inn.foresight.core.infra.model.AdvanceSearch;
import com.inn.foresight.core.infra.model.NEBandDetail;
import com.inn.foresight.core.infra.model.NetworkElement;
import com.inn.foresight.core.infra.service.IAdvanceSearchProvider;
import com.inn.foresight.core.infra.service.INEBandDetailService;
import com.inn.foresight.core.infra.utils.enums.NEType;
import com.inn.foresight.core.infra.wrapper.PBServiceInfoWrapper;

@Service("NEBandDetailServiceImpl")
public class NEBandDetailServiceImpl extends AbstractService<Integer, NEBandDetail>
		implements INEBandDetailService, IAdvanceSearchProvider {

	private Logger logger = LogManager.getLogger(NEBandDetailServiceImpl.class);

	@Autowired
	private INEBandDetailDao iNEBandDetailDao;

	@Autowired
	private INetworkElementDao networkElementDao;

	@Override
	@Transactional
	public PBServiceInfoWrapper getMacroSitesForPBService(List<NEType> neTypeList, List<String> neFrequencyList) {
		logger.info("Going to get MacroSites for PBService");
		PBServiceInfoWrapper pbServiceInfoWrapper = new PBServiceInfoWrapper();
		try {
			List<NEBandDetail> neBandDetailList = iNEBandDetailDao.getNEBandDetails(neTypeList, null, neFrequencyList,
					null, null, null, null, null, null);
			if (neBandDetailList != null && neBandDetailList.size() > 0) {
				List<String> neNames = neBandDetailList.stream().map(n -> n.getNetworkElement().getNeName())
						.collect(Collectors.toList());
				Integer totalCount = neNames.size();
				pbServiceInfoWrapper.setServiceData(neNames);
				pbServiceInfoWrapper.setCount(totalCount);
			}
			return pbServiceInfoWrapper;
		} catch (DaoException daoException) {
			logger.info("DaoException caught while getting MacroSites for PBService Message : {}",
					daoException.getMessage());
		} catch (Exception exception) {
			logger.info("Error in getting MacroSites for PBService Message : {}", exception.getMessage());
		}
		return pbServiceInfoWrapper;
	}

	public String getMorphologyByCode(String btsName) {
		try {
			return iNEBandDetailDao.getMorphologyByCode(btsName);
		} catch (DaoException daoException) {
			logger.error("DaoException caught while getting distinct Morphology {}", daoException.getStackTrace());
		} catch (Exception exception) {
			logger.error("Error in getting distinct Morphology {} ", exception.getStackTrace());
		}
		return null;
	}

	@Transactional
	public Map<String, Long> getSitesCountNestatusWiseAndGeographyWise(List<NEType> neType, String domain,
			String vendor, List<String> neStatus, String geographyType, String geographyValue) {
		return iNEBandDetailDao.getSitesCountNestatusWiseAndGeographyWise(neType, domain, vendor, neStatus,
				geographyType, geographyValue);
	}

	@Override
	public Object getSearchData(Map<String, Object> map) {
		Map<String, Object> siteDetailMap = new HashMap<>();
		AdvanceSearch search = (AdvanceSearch) map.get("advanceSearch");
		NetworkElement networkElement = null;
		if (search != null) {
			networkElement = networkElementDao.searchSiteByFriendlyName(search.getName(),
					NEType.valueOf(getSiteValue(search.getAdvanceSearchConfiguration().getTypeCategory())));
		}
		if (networkElement != null && networkElement.getLatitude() != null && networkElement.getLongitude() != null
				&& networkElement.getNeType() != null) {
			logger.info("newtwork element data found by its name : {} ", networkElement.getNeId());
			siteDetailMap.put(ForesightConstants.NE_NAME, networkElement.getNeName());
			siteDetailMap.put(ForesightConstants.NE_TYPE, networkElement.getNeType().name());
			siteDetailMap.put(ForesightConstants.NE_STATUS, networkElement.getNeStatus());
			siteDetailMap.put(ForesightConstants.LATITUDE, networkElement.getLatitude());
			siteDetailMap.put(ForesightConstants.LONGITUDE, networkElement.getLongitude());
			siteDetailMap.put(ForesightConstants.FRIENDLY_NAME, networkElement.getFriendlyname());

		} else {
			throw new RestException(ForesightConstants.EXCEPTION_NO_RECORD_FOUND);
		}
		return siteDetailMap;
	}

	private String getSiteValue(String value) {
		if (value != null) {
			if (value.equalsIgnoreCase("MACRO_FN")) {
				return value.split("_")[0];
			} else {
				return value.split("_")[0].concat("_SITE");
			}
		}
		return value;
	}
}

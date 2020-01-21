package com.inn.foresight.core.infra.service.impl;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.commons.maps.LatLng;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.service.impl.AbstractService;
import com.inn.core.generic.utils.Utils;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.infra.dao.IMacroSiteDetailDao;
import com.inn.foresight.core.infra.model.MacroSiteDetail;
import com.inn.foresight.core.infra.service.IMacroSiteDetailService;
import com.inn.foresight.core.infra.wrapper.MacroSitesDetailWrapper;

/**
 * The Class NetworkElementServiceImpl.
 */
@Service("MacroSiteDetailServiceImpl")
public class MacroSiteDetailServiceImpl extends AbstractService<Integer, MacroSiteDetail> implements IMacroSiteDetailService {

	/** The logger. */
	private Logger logger = LogManager.getLogger(NetworkElementServiceImpl.class);

	@Autowired
	private IMacroSiteDetailDao macroSiteDetailDao;

	@Override
	public Map getEcgiLocation() {
		try {
			List<MacroSitesDetailWrapper> macroSiteDetailWrappers = macroSiteDetailDao.getEcgiLocation();
			logger.info("Data {}",macroSiteDetailWrappers.toString());
			return macroSiteDetailWrappers.stream().collect(Collectors.toMap(MacroSitesDetailWrapper::getEcgi,MacroSitesDetailWrapper::getLocationDetailWrapper,(wrapper1, wrapper2) -> wrapper1));
		} catch (Exception e){
			logger.error(ForesightConstants.LOG_EXCEPTION,e);
			throw new RestException(e);
		}
	}

	@Override
	public LatLng getLocationByCGIAndPci(Integer cgi, Integer pci) {
		try {
			logger.info("Going to get Location by CGi {} and pci {} ", cgi, pci);
			return macroSiteDetailDao.getLocationByCGIAndPci(cgi, pci);
		} catch (DaoException exception) {
			logger.info("Unable to get Location by Cgi {} and pci {} Exception {}", cgi, pci,
					Utils.getStackTrace(exception));
			throw new RestException(exception.getMessage());
		} catch (Exception exception) {
			logger.info("Unable to get Location by Cgi {} and pci {} Exception {}", cgi, pci,
					Utils.getStackTrace(exception));
			throw new RestException("Unable to get Location");
		}
	}
	
	@Override
	public List<MacroSiteDetail> getMacroSiteDetailByListNename(List<String> nename) {
		try {
			logger.info("Going to data @method  getMacroSiteDetailByListNename by nename {} ",  nename);
			 return macroSiteDetailDao.getMacroSiteDetailByListNename(nename);
			
		} catch (DaoException exception) {
			logger.info("Unable to get Location by Cgi {} and pci {} Exception {}",
					Utils.getStackTrace(exception));
			throw new RestException(exception.getMessage());
		} catch (Exception exception) {
			logger.info("Unable to get Location by Cgi {} and pci {} Exception {}",
					Utils.getStackTrace(exception));
			throw new RestException("Unable to get Location");
		}
	}

	@Override
	@SuppressWarnings("unused")
	public String validateEnbid(Integer enbId) {

		List<Integer> check = new ArrayList<>();
		try {
			logger.info("Going to get data for enbid {}", enbId);
			check = macroSiteDetailDao.validateEnbid(enbId);
			if (check != null && !check.isEmpty()) {
				return "Success";
			} else {
				throw new RestException("Invalid enodebid");
			}
		} catch (DaoException exception) {
			logger.info("Unable to get data for enbid {}", Utils.getStackTrace(exception));
		} catch (Exception exception) {
			logger.info("Unable to get data for enbid {} ", Utils.getStackTrace(exception));

		}
		return null;
	}

}


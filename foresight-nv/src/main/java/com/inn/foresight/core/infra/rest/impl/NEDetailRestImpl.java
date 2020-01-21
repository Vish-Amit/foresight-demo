package com.inn.foresight.core.infra.rest.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.service.INETaskDetailService;
import com.inn.foresight.core.infra.service.NEDetailService;
import com.inn.foresight.core.infra.wrapper.NEDetailDataWrapper;
import com.inn.foresight.core.infra.wrapper.NETaskDetailWrapper;

@Path("NEDetail")
@Produces("application/json")
@Consumes("application/json")
public class NEDetailRestImpl {
	/** The logger. */
	private Logger logger = LogManager.getLogger(NEDetailRestImpl.class);
	@Autowired
	private NEDetailService neDetailService;
	@Autowired
	INETaskDetailService neTaskDetailService;

	@GET
	@Path("getDistinctVIPCategories")
	public List<String> getDistinctVIPCategories() {
		logger.info("going to get distinct VIP categories");
		return neDetailService.getDistinctVIPCategories();
	}

	@POST
	@Path("getListOfVIPNetworkElement")
	public List<String> getListOfVIPNetworkElement(List<String> neNameList, @QueryParam("domain") String domain,
			@QueryParam("vendor") String vendor) {
		try {
			return neDetailService.getListOfVIPNetworkElement(neNameList, domain, vendor);
		} catch (Exception e) {
			logger.error("Exception while getting ListOfVIP Nenames Error:{}", ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	@GET
	@Path("getSiteParameterList")
	public List<NEDetailDataWrapper> getSiteParameterList(@QueryParam("searchValue") String searchValue,
			@QueryParam("llimit") Integer llimit, @QueryParam("ulimit") Integer ulimit) {
		logger.info("Going to get site parameter list searchValue : {} llimit : {} ulimit : {}", searchValue, llimit,
				ulimit);
		return neDetailService.getSiteParameterList(searchValue, llimit, ulimit);
	}

	@GET
	@Path("getSiteParameterCount")
	public Map<String, Integer> getSiteParameterCount(@QueryParam("searchValue") String searchValue) {
		logger.info("Gng to get count of site parameter : {}", searchValue);
		return neDetailService.getSiteParameterCount(searchValue);
	}

	@GET
	@Path("getSiteDetailByNeName/{neName}")
	public NEDetailDataWrapper getSiteDetailByNeName(@PathParam("neName") String neName) {
		logger.info("Going to get site detail by nename : {}", neName);
		if (Utils.checkForValueInString(neName)) {
			return neDetailService.getSiteDetailByNeName(neName);
		} else {
			throw new RestException(ForesightConstants.INVALID_PARAMETER);
		}
	}

	@POST
	@Path("updateSiteParameterByNeName")
	public Map<String, String> updateSiteParameterByNeName(NEDetailDataWrapper dataWrapper) {
		logger.info("Going to update site parameter by nename");
		if (dataWrapper != null && Utils.checkForValueInString(dataWrapper.getNeName())) {
			return neDetailService.updateSiteParameterByNeName(dataWrapper);
		} else {
			throw new RestException(ForesightConstants.INVALID_PARAMETER);
		}
	}

	@POST
	@Path("updateNeTaskDetailBySiteAndFrequency")
	public Map<String, String> updateNeTaskDetailBySiteAndFrequency(NETaskDetailWrapper neDetailWrapper) {
		logger.info("Going to update site detail using  neDetailWrapper");
		if (Utils.checkForValueInString(neDetailWrapper.getSiteId())
				&& Utils.checkForValueInString(neDetailWrapper.getTaskName())
				&& Utils.checkForValueInString(neDetailWrapper.getTaskStatus())) {
			try {
                return neTaskDetailService.updateNeTaskDetailBySiteAndFrequency(neDetailWrapper);
			} catch (Exception e) {
				throw new RestException(ForesightConstants.FAILURE);
			}
		} else {
			throw new RestException(ForesightConstants.INVALID_PARAMETER);
		}
	}
}

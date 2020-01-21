package com.inn.foresight.core.subscriber.rest.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.cxf.jaxrs.ext.search.SearchContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.rest.AbstractCXFRestService;
import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.subscriber.model.SubscriberInfo;
import com.inn.foresight.core.subscriber.service.ISubscriberInfoService;
import com.inn.foresight.core.subscriber.service.ISubscriberSearch;
import com.inn.foresight.core.subscriber.utils.wrapper.SubscriberInfoDetailWrapper;

@Path("SubscriberInfo")
@Produces("application/json")
@Consumes("application/json")
@Service("SubscriberInfoRestImpl")
public class SubscriberInfoRestImpl extends AbstractCXFRestService<Integer, SubscriberInfo> {

	private Logger logger = LogManager.getLogger(SubscriberInfoRestImpl.class);

	@Autowired
	private ISubscriberInfoService iSubscriberInfoService;
	
	@Autowired
	@Qualifier("SubscriberInfoServiceImpl")
	private ISubscriberSearch iSubscriberSearch;
	
	public SubscriberInfoRestImpl() {
		super(SubscriberInfo.class);
	}

	@Override
	public SubscriberInfo create(@Valid SubscriberInfo arg0) {
		return null;
	}

	@Override
	public List<SubscriberInfo> findAll() {
		return new ArrayList<>();
	}

	@Override
	public SubscriberInfo findById(@NotNull Integer arg0) {
		return null;
	}

	@Override
	public boolean remove(@Valid SubscriberInfo arg0) {
		return false;
	}

	@Override
	public void removeById(@NotNull Integer arg0) {
		// empty
	}

	@Override
	public List<SubscriberInfo> search(SubscriberInfo arg0) {
		return new ArrayList<>();
	}

	@Override
	public SubscriberInfo update(@Valid SubscriberInfo arg0) {
		return null;
	}

	@Override
	public SearchContext getSearchContext() {
		return null;
	}

	@Override
	public IGenericService<Integer, SubscriberInfo> getService() {
		return null;
	}
	
	@POST
	@Path("createSubscriberInfoData")
	public Map<String, String> createSubscriberInfoData(SubscriberInfo subscriberInfo) {
		logger.info("Going to create subscriber info data");
		if (subscriberInfo != null && subscriberInfo.getMsisdn() != null) {
			return iSubscriberInfoService.createSubscriberInfoData(subscriberInfo);
		} else {
			throw new RestException(ForesightConstants.INVALID_PARAMETER);
		}
	}

	@POST
	@Path("updateSubscriberInfoByMdn")
	public Map<String, String> updateSubscriberInfoByMdn(SubscriberInfoDetailWrapper subscriberInfo) {
		if (subscriberInfo != null && subscriberInfo.getMsisdn() != null) {
			logger.info("Going to update subscriber category for msisdn : {}", subscriberInfo.getMsisdn());
			return iSubscriberInfoService.updateSubscriberInfoByMdn(subscriberInfo);
		} else {
			throw new RestException(ForesightConstants.INVALID_PARAMETER);
		}
	}

	@GET
	@Path("searchSubscriberInfo")
	public List<SubscriberInfoDetailWrapper> searchSubscriberInfo(@QueryParam("searchValue") String searchValue, @QueryParam("llimit") Integer llimit, @QueryParam("ulimit") Integer ulimit) {
		logger.info("Going to search Subscriber Info for search value : {}", searchValue);
		return iSubscriberInfoService.searchSubscriberInfo(searchValue, llimit, ulimit);
	}

	@GET
	@Path("getTotalSubscriberInfoCount")
	public Long getTotalSubscriberInfoCount(@QueryParam("searchValue")String searchValue) {
		logger.info("Going to count total subscriber info for search value : {}", searchValue);
		return iSubscriberInfoService.getTotalSubscriberInfoCount(searchValue);
	}
	
	@GET
	@Path("getSubscriberDetailByMdn/{msisdn}")
	public SubscriberInfo getSubscriberDetailByMdn(@PathParam("msisdn") String msisdn) {
		logger.info("Going to get subscriber info detail for msisdn : {}", msisdn);
		if (msisdn != null && !msisdn.isEmpty()) {
			return iSubscriberInfoService.getSubscriberDetailByMdn(msisdn);
		} else {
			throw new RestException(ForesightConstants.INVALID_PARAMETER);
		}
	}

	@GET
	@Path("getSubscriberInfoType")
	public List<String> getSubscriberInfoType() {
		return iSubscriberInfoService.getSubscriberInfoType();
	}
	
	@GET
	@Path("getIMSIByMSISDN")
	public Map<String, String> getIMSIByMSISDN(@QueryParam("msisdn") String msisdn) {
		logger.info("Going to get imsi by msisdn : {}", msisdn);
		if (Utils.checkForValueInString(msisdn)) {
			return iSubscriberSearch.getIMSIByMSISDN(msisdn);
		} else {
			throw new RestException(ForesightConstants.INVALID_PARAMETER);
		}
	}
	
}
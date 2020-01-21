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
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.cxf.jaxrs.ext.search.SearchContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.rest.AbstractCXFRestService;
import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.subscriber.model.SubscriberHistory;
import com.inn.foresight.core.subscriber.service.ISubscriberHistoryService;
import com.inn.foresight.core.subscriber.utils.wrapper.SubscriberHistoryWrapper;

@Path("SubscriberHistory")
@Produces("application/json")
@Consumes("application/json")
@Service("SubscriberHistoryRestImpl")
public class SubscriberHistoryRestImpl extends AbstractCXFRestService<Integer, SubscriberHistory> {

	private Logger logger = LogManager.getLogger(SubscriberHistoryRestImpl.class);

	@Autowired
	private ISubscriberHistoryService iSubscriberHistoryService;

	public SubscriberHistoryRestImpl() {
		super(SubscriberHistory.class);
	}

	@Override
	public List<SubscriberHistory> search(SubscriberHistory entity) {
		return new ArrayList<>();
	}

	@Override
	public SubscriberHistory findById(@NotNull Integer primaryKey) {
		return null;
	}

	@Override
	public List<SubscriberHistory> findAll() {
		return new ArrayList<>();
	}

	@Override
	public SubscriberHistory create(@Valid SubscriberHistory anEntity) {
		return null;
	}

	@Override
	public SubscriberHistory update(@Valid SubscriberHistory anEntity) {
		return null;
	}

	@Override
	public boolean remove(@Valid SubscriberHistory anEntity) {
		return false;
	}

	@Override
	public void removeById(@NotNull Integer primaryKey) {
		// Empty Block
	}

	@Override
	public IGenericService<Integer, SubscriberHistory> getService() {
		return null;
	}

	@Override
	public SearchContext getSearchContext() {
		return null;
	}

	@POST
	@Path("createSubscriberHistory")
	public Map<String, String> createSubscriberHistory(SubscriberHistory subscriberSearch) {
		logger.info("Inside createSubscriberHistory method ");
		if (subscriberSearch != null) {
			return iSubscriberHistoryService.createSubscriberHistory(subscriberSearch);
		} else {
			throw new RestException(ForesightConstants.INVALID_PARAMETER);
		}
	}

	@GET
	@Path("getTotalSubscriberHistoryByMdn")
	public Long getTotalSubscriberHistoryByMdn(@QueryParam("subscriberSearch") String subscriberSearch) {
		logger.info("Going to count total subscriber searches by value {} ", subscriberSearch);
		return iSubscriberHistoryService.getTotalSubscriberHistoryByMdn(subscriberSearch);
	}

	@GET
	@Path("getSubscriberHistoryDetails")
	public List<SubscriberHistoryWrapper> getSubscriberHistoryDetails(@QueryParam("llimit") Integer llimit, @QueryParam("ulimit") Integer ulimit) {
		logger.info("Going to get Subscriber Details for subscriberSearch llimit {} ulimit {} ", llimit, ulimit);
		return iSubscriberHistoryService.getSubscriberHistoryDetails(llimit, ulimit);
	}

	@GET
	@Path("searchSubscriberHistoryByMdnAndUser")
	public List<SubscriberHistoryWrapper> searchSubscriberHistoryByMdnAndUser(@QueryParam("subscriberSearch") String subscriberSearch, @QueryParam("llimit") Integer llimit,
			@QueryParam("ulimit") Integer ulimit) {
		logger.info("Going to search Subscriber Detail for value {} ", subscriberSearch);
		return iSubscriberHistoryService.searchSubscriberHistoryByMdnAndUser(subscriberSearch, llimit, ulimit);
	}

}

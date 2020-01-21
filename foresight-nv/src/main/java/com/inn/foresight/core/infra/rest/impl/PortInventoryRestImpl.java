package com.inn.foresight.core.infra.rest.impl;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.infra.service.IPortInventoryService;

@Path("/PortInventory")
@Produces("application/json")
@Consumes("application/json")
@Service("PortInventoryRestImpl")
public class PortInventoryRestImpl {

	/** The logger. */
	private Logger logger = LogManager.getLogger(PortInventoryRestImpl.class);
	@Autowired
	private IPortInventoryService iPortInventoryService;
	
	@GET
	@Path("getAllPortsByRouter")
	public List<String> getAllPortsByRouter(@QueryParam("neid") String neName) {
		logger.trace("request to get Ports");
		try {
			return iPortInventoryService.getPortsByRouter(neName);
		} catch (Exception e) {
			logger.error("Error in getting ports : {}", ExceptionUtils.getStackTrace(e));
			throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}
	@GET
	@Path("getPhysicalAndLogicalPortsByRouter")
	public List<String> getPhysicalAndLogicalPortsByRouter(@QueryParam("neid") String neName) {
		logger.trace("request to get Ports");
		try {
			return iPortInventoryService.getPhysicalAndLogicalPortsByRouter(neName);
		} catch (Exception e) {
			logger.error("Error in getPhysicalAndLogicalPortsByRouter : {}", ExceptionUtils.getStackTrace(e));
			throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}
	

}

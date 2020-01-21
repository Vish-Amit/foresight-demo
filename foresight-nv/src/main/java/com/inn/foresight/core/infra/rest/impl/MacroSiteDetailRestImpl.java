package com.inn.foresight.core.infra.rest.impl;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.infra.service.IMacroSiteDetailService;

@Path("MacroSiteDetail")
@Produces("application/json")
@Consumes("application/json")
public class MacroSiteDetailRestImpl {

	/** The logger. */
	private Logger logger = LogManager.getLogger(MacroSiteDetailRestImpl.class);

	@Autowired
	private IMacroSiteDetailService macroSiteDetailService;

	@GET
	@Path("getEcgiLocation")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEcgiLocation() {
		try {
			logger.info("Inside getData");
			return Response.ok(macroSiteDetailService.getEcgiLocation()).build();
		} catch (Exception e) {
			logger.error(ForesightConstants.LOG_EXCEPTION, e);
			return Response.ok(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG).build();
		}
	}
}

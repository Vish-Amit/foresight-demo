package com.inn.foresight.module.nv.workorder.rest.impl;


import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.module.nv.workorder.service.IKMLParserService;

@Path("/ms/KMLParser")
public class KMLParserMicroServiceRestImpl {


	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(KMLParserRestImpl.class);
	
	/** The i KML parser service. */
	@Autowired
	private IKMLParserService iKMLParserService;

	/**
	 * Instantiates a new KML parser rest impl.
	 *
	 * @param iKMLParserService the i KML parser service
	 */

	/**
	 * Parses the KML and get boundary.
	 *
	 * @param request the request
	 * @return the response
	 * @throws RestException 
	 */
	@POST
	@Path("parseKMLAndGetBoundary")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response parseKMLAndGetBoundary(@Context HttpServletRequest request) {
		logger.info("Going to parse and get boundary from KML");
		return Response	.ok(iKMLParserService.parseTabAndKMLAndGetBoundary(request))
						.build();
	}

}

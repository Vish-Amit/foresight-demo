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
import org.springframework.stereotype.Service;

import com.inn.foresight.module.nv.workorder.service.IKMLParserService;


@Path("/KMLParser")
@Service("KMLParserRestImpl")
public class KMLParserRestImpl {
	
	private Logger logger = LogManager.getLogger(KMLParserRestImpl.class);
	
	@Autowired
	private IKMLParserService iKmlParserService;

	@POST
	@Path("parseKMLAndGetBoundary")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response parseKMLAndGetBoundary(@Context HttpServletRequest request) {
		logger.info("Going to upload and parse  KML to get boundary");
		return Response	.ok(iKmlParserService.parseKMLAndGetBoundary(request))
						.build();
	}

}

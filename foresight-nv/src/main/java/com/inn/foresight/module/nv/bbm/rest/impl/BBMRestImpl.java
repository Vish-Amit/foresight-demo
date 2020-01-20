package com.inn.foresight.module.nv.bbm.rest.impl;

import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.module.nv.bbm.constant.BBMConstants;
import com.inn.foresight.module.nv.bbm.service.IBBMService;

@Path("/BBM")
@Service("BBMRestImpl")
public class BBMRestImpl extends BBMConstants {

	private Logger logger = LogManager.getLogger(BBMRestImpl.class);

	@Autowired
	private IBBMService iBBMService;

	@POST
	@Path("deviceRegistration")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response deviceRegistration(@Multipart("file") InputStream inputFile,
			@Multipart("fileName") String fileName) {
		logger.info("Going for deviceRegistration file name :{}", fileName);
		try {
			return Response	.ok(iBBMService.deviceRegistration(fileName, inputFile))
							.build();
		} catch (RestException e) {
			return Response	.ok(FAILURE_JSON)
							.build();
		}
	}
	
	@POST
	@Path("deviceDeregistration")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response deviceDeregistration(@Multipart("file") InputStream inputFile,
			@Multipart("fileName") String fileName) {
		logger.info("Going for deviceDeregistration file name :{}", fileName);
		try {
			return Response	.ok(iBBMService.deviceDeregistration(fileName, inputFile))
							.build();
		} catch (RestException e) {
			return Response	.ok(FAILURE_JSON)
							.build();
		}
	}

}

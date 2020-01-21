package com.inn.foresight.core.generic.rest;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.foresight.core.generic.service.IMD5GenerateService;

@Path("/MD5Generate")
@Produces("application/json")
@Consumes("application/json")
@Service("MD5HashGenerateRestImpl")
public class MD5HashGenerateRestImpl {
	Logger logger = LogManager.getLogger(MD5HashGenerateRestImpl.class);
	
	/** The service. */
	@Autowired
	private IMD5GenerateService md5service;

	@GET
	@Path("/getUriWithHash")
	public Map<String,String> getMd5HashForUri(@QueryParam("uri") String uri){
		return md5service.getMd5HashForUri(uri);
	}
	
	
}

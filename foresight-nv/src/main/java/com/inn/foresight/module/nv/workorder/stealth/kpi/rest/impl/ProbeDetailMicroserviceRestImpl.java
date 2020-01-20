package com.inn.foresight.module.nv.workorder.stealth.kpi.rest.impl;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.module.nv.workorder.stealth.kpi.service.ProbeDetailService;
import com.inn.foresight.module.nv.workorder.stealth.kpi.wrapper.ProbeDetailWrapper;

@Path("/ms/ProbeDetail")
@Produces(MediaType.APPLICATION_JSON)
public class ProbeDetailMicroserviceRestImpl {
	
@Autowired
ProbeDetailService probeDetailService;
	
private static final Logger logger = LogManager.getLogger(ProbeDetailMicroserviceRestImpl.class);

/**
 * @param wrapper
 * @return
 */
@POST	
@Path("persistData")
	public String persistDataIntoHbase(ProbeDetailWrapper wrapper) {
		if (wrapper != null) {
			return probeDetailService.persistDataIntoHbaseMicroService(wrapper);
		} else {
			return ForesightConstants.INVALID_PARAMETERS_JSON;
		}
	}
}

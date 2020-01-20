package com.inn.foresight.module.nv.app.rest.impl;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.module.nv.app.constants.AppConstants;
import com.inn.foresight.module.nv.app.service.ILicenseInfoService;
import com.inn.product.security.utils.AuthenticationCommonUtil;

/**
 * The Class LicenseInfoRestImpl.
 *
 * @author innoeye
 * date - 16-Nov-2017 6:39:03 PM
 */
@Path("/LicenseInfo")
@Service("LicenseInfoRestImpl")
public class LicenseInfoRestImpl extends AppConstants{
	
	/** The logger. */
	private Logger logger = LogManager.getLogger(LicenseInfoRestImpl.class);
	
	/** The service. */
	@Autowired
	ILicenseInfoService service;
	
	/**
	 * Cheking for Licence validity .
	 *
	 * @param licenceId : unique id related to Licence issued
	 * @return true if Licence is valid, false otherwise or error message if anything unexpected occurs
	 */
	@GET
	@Path("checkLicenseValidityById/{"+LICENCE_ID+"}")
	public Response checkLicenseValidityById(@PathParam(LICENCE_ID) String licenceId) {
		logger.info("Cheking For Licence Validation by licenceId {}", licenceId);
		try {
			if(licenceId != null) {
				return Response.ok(service.checkLicenseValidityById(licenceId)).build();
			}else {
				return Response.ok(ForesightConstants.INVALID_PARAMETERS).build();
			}
		} catch (RestException e) {
			logger.error(AppConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			return Response.ok(AuthenticationCommonUtil.checkForValueEncryption(e.getMessage(),null)).build();
		}
	}

}

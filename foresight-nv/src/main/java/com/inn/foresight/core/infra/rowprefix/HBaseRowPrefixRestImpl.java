package com.inn.foresight.core.infra.rowprefix;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.infra.rest.impl.BoundaryDataRestImpl;

@Path("/HBaseRowPrefix")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HBaseRowPrefixRestImpl {

	/** The logger. */
    private Logger logger = LogManager.getLogger(BoundaryDataRestImpl.class);

    @Autowired
    private HBaseRowPrefixService hBaseRowPrefixService; 
    
    @GET
    @Path("generateHBaseRowPrefix")
    public String generateHBaseRowPrefix(@Context HttpServletRequest request) {
        try {
        	hBaseRowPrefixService.generateCodeForMissingGeo();
        	return ForesightConstants.SUCCESS_JSON;
		}catch (RestException e) {
			throw e;
		}catch (Exception e) {
            logger.error("Exception inside generateHBaseRowPrefix {} ", ExceptionUtils.getStackTrace(e));
            throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
        }
    }
}

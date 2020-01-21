
package com.inn.foresight.core.infra.rest.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.inn.commons.http.HttpGetRequest;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.utils.InfraConstants;

@Path("/BoundaryData")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BoundaryDataRestImpl {

    /** The logger. */
    private Logger logger = LogManager.getLogger(BoundaryDataRestImpl.class);

    @GET
    @Path("findGeographyNameByLatLong")
    public String findGeographyNameByLatLong(@QueryParam("lat") Double lat, @QueryParam("lng") Double lng,
            @QueryParam("zoomLevel") Integer zoomLevel, @Context HttpServletRequest request) {
        logger.info("going to find Geography Name By LatLong for lat:{},lng:{}, zoomLevel:{}", lat, lng, zoomLevel);
		try {
			if (lat == null && lng == null && zoomLevel == null) {
				logger.info("provide values for lat: {} ,lng : {} ,zoomLevel: {}", lat, lng, zoomLevel);
				throw new RestException(InfraConstants.EXCEPTION_INVALID_PARAMS);
			}
			String dropwizardUrl = Utils.getDropwizardUrlWithPrefix(request);
			logger.info("going to hit the request for findGeographyNameByLatLong {}", dropwizardUrl);
			return new HttpGetRequest(dropwizardUrl).getString();
		} catch (Exception e) {
            logger.error("Exception inside findGeographyNameByLatLong {} ", ExceptionUtils.getStackTrace(e));
            throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
        }
    }
    
    @GET
    @Path("findGeographyLocation")
    public String findGeographyLocation(@QueryParam("lat") Double lat, @QueryParam("lng") Double lng,
            @QueryParam("zoomLevel") Integer zoomLevel, @Context HttpServletRequest request) {
        logger.info("Inside findGeographyLocation for lat: {},lng: {}, zoomLevel: {}", lat, lng, zoomLevel);
		try {
			if (lat == null && lng == null && zoomLevel == null) {
				logger.info("provide values for lat: {} ,lng : {} ,zoomLevel: {}", lat, lng, zoomLevel);
				throw new RestException(InfraConstants.EXCEPTION_INVALID_PARAMS);
			}
			String dropwizardUrl = Utils.getDropwizardUrlWithPrefix(request);
			logger.info("going to hit the request for findGeographyLocation {}", dropwizardUrl);
			return new HttpGetRequest(dropwizardUrl).getString();
		} catch (Exception e) {
            logger.error("Exception inside findGeographyLocation {} ", ExceptionUtils.getStackTrace(e));
            throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
        }
    }
}
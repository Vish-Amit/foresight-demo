package com.inn.foresight.core.infra.rest.impl;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.apache.cxf.jaxrs.ext.search.SearchContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.utils.ExceptionUtil;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.product.um.geography.service.GeographyL2Service;
import com.inn.product.um.geography.utils.wrapper.GeographyCollectionWrapper;
import com.inn.product.um.geography.utils.wrapper.GeographyWrapper;

@Path("/Geography")
@Produces("application/json")
@Consumes("application/json")
public class GeographyRestImpl {
	/** The context. */
	@Context
	private SearchContext context;

	@Autowired
	private GeographyL2Service geographyL2Service;

	/** The logger. */
	private Logger logger = LogManager.getLogger(GeographyRestImpl.class);

	@POST
	@Path("getGeographyById")
	public List<GeographyWrapper> getGeographyById(GeographyCollectionWrapper collectionWrapper) {
		logger.info("Going to get {} geography details By Id for {}", collectionWrapper.getResponseType(), collectionWrapper.getRequestType());
		try {
			return geographyL2Service.getGeographyByLevel(collectionWrapper);
		} catch (RestException e) {
			throw new RestException(e.getGuiMessage());
		} catch (Exception exception) {
			throw new RestException(ExceptionUtil.generateExceptionCode(ForesightConstants.REST, ForesightConstants.GEOGRAPHY_L2, exception));
		}
	}
	
	@POST
	@Path("getGeographyByName")
	public List<GeographyWrapper> getGeographyByName(GeographyCollectionWrapper collectionWrapper) {
		logger.info("Going to get {} geography details By Name for {}", collectionWrapper.getResponseType(), collectionWrapper.getRequestType());
		try {
			return geographyL2Service.getGeographyByLevel(collectionWrapper);
		} catch (RestException e) {
			throw new RestException(e.getGuiMessage());
		} catch (Exception exception) {
			throw new RestException(ExceptionUtil.generateExceptionCode(ForesightConstants.REST, ForesightConstants.GEOGRAPHY_L2, exception));
		}
	}
}
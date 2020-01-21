package com.inn.foresight.core.adminplanning.rest.impl;

import java.security.InvalidParameterException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.cxf.jaxrs.ext.search.SearchContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.rest.AbstractCXFRestService;
import com.inn.core.generic.service.IGenericService;
import com.inn.core.generic.utils.Preconditions;
import com.inn.foresight.core.adminplanning.model.AlgorithmProperty;
import com.inn.foresight.core.adminplanning.service.IAlgorithmPropertyService;
import com.inn.foresight.core.adminplanning.wrapper.AlgorithmResponse;
import com.inn.foresight.core.generic.utils.ForesightConstants;



/**
 * The Class AlgorithmPropertyRestImpl.
 */
@Path("/AlgorithmProperty")
@Produces("application/json")
@Consumes("application/json")
@Service("AlgorithmPropertyRest")
public class AlgorithmPropertyRest extends AbstractCXFRestService<Integer, AlgorithmProperty> {

	/** The logger. */
	private Logger logger = LogManager.getLogger(AlgorithmPropertyRest.class);

	/** The service. */
	@Autowired
	private IAlgorithmPropertyService service;

	/** The context. */
	@Context
	private SearchContext context;

	/**
	 * Instantiates a new algorithm property rest impl.
	 */
	public AlgorithmPropertyRest() {
		super(AlgorithmProperty.class);
	}

	/**
	 * Search.
	 *
	 * @param entity
	 *            the entity
	 * @return the list
	 * @throws RestException
	 *             the rest exception
	 */
	@GET
	@Override
	@Path("search")
	public List<AlgorithmProperty> search(@QueryParam("") AlgorithmProperty entity) {
		logger.info("Searching AlgorithmProperty entity :" + entity);
		try {
			return service.search(entity);
		} catch (RestException e) {
			throw new RestException(e);
		}
	}

	/**
	 * Find by id.
	 *
	 * @param id
	 *            the id
	 * @return the algorithm property
	 * @throws RestException
	 *             the rest exception
	 */
	@GET
	@Override
	@Path("findById")
	public AlgorithmProperty findById(@QueryParam("id") Integer id) {
		logger.info("Finding AlgorithmProperty by id :" + id);
		try {
			return service.findById(id);
		} catch (RestException e) {
			throw new RestException(e);
		}

	}

	/**
	 * Find all.
	 *
	 * @return the list
	 * @throws RestException
	 *             the rest exception
	 */
	@GET
	@Override
	@Path("findAll")
	public List<AlgorithmProperty> findAll() {
		try {
			return service.findAll();
		} catch (RestException e) {
			throw new RestException(e);
		}
	}

	/**
	 * Creates the.
	 *
	 * @param algorithmProperty
	 *            the algorithm property
	 * @return the algorithm property
	 * @throws RestException
	 *             the rest exception
	 */
	@Override
	@POST
	@Path("create")
	public AlgorithmProperty create(AlgorithmProperty algorithmProperty) {

		try {
			return service.create(algorithmProperty);
		} catch (RestException e) {
			throw new RestException(e);
		}
	}

	/**
	 * Update.
	 *
	 * @param algorithmProperty
	 *            the algorithm property
	 * @return the algorithm property
	 * @throws RestException
	 *             the rest exception
	 */
	@Override
	@POST
	@Path("update")
	public AlgorithmProperty update(AlgorithmProperty algorithmProperty) {
		logger.info("inside @method update Updating AlgorithmProperty by an entity :" + algorithmProperty);
		try {
			return service.update(algorithmProperty);
		} catch (RestException e) {
			throw new RestException(e);
		}
	}

	/**
	 * Removes the.
	 *
	 * @param algorithmProperty
	 *            the algorithm property
	 * @return true, if successful
	 * @throws RestException
	 *             the rest exception
	 */
	@Override
	public boolean remove(AlgorithmProperty algorithmProperty) {
		logger.info("Removing AlgorithmProperty record by an entity :" + algorithmProperty);
		try {
			service.remove(algorithmProperty);
		} catch (RestException e) {
			throw new RestException(e);
		}
		return true;
	}

	/**
	 * Removes the by id.
	 *
	 * @param id
	 *            the id
	 * @throws RestException
	 *             the rest exception
	 */
	@Override
	public void removeById(@PathParam("id") Integer id) {
		if (id != null) {
			logger.info("Removing AlgorithmProperty by primaryKey :" + id);
			try {
				service.removeById(id);
			} catch (RestException e) {
				throw new RestException(e);
			}
		} else {
			logger.info("Parameters id : " + id);
			throw new InvalidParameterException("Invalid parametres");
		}

	}

	/**
	 * Gets the service.
	 *
	 * @return the service
	 */
	@Override
	public IGenericService<Integer, AlgorithmProperty> getService() {
		return service;
	}

	/**
	 * Gets the search context.
	 *
	 * @return the search context
	 */
	@Override
	public SearchContext getSearchContext() {
		return context;
	}

	/**
	 * Gets the existing exceptions.
	 *
	 * @param algoId
	 *            the algorithm name
	 * @return the existing exceptions
	 * @throws RestException
	 *             the rest exception
	 */
	@GET
	@Path("getExistingExceptions/{id}")
	public List<AlgorithmResponse> getExistingExceptions(@PathParam("id") Integer algoId) {
		logger.info("going to get existing exceptions for {}", algoId);
		if (algoId != null) {
			return service.getExistingExceptions(algoId);
		}
		throw new RestException(ForesightConstants.INVALID_PARAMETER);
	}

	/**
	 * Gets the algorithm properties.
	 *
	 * @param algoId            the algorithm name
	 * @param type the type
	 * @param exceptionId the exception id
	 * @return the properties
	 * @throws RestException             the rest exception
	 */
	@GET
	@Path("getProperties/{algorithmId}")
	public List<AlgorithmProperty> getProperties(@PathParam("algorithmId") Integer algoId,
			@QueryParam("type") String type, @QueryParam("exceptionId") Integer exceptionId) {
		Preconditions.checkNoneNull(algoId);
		return service.getProperties(algoId, type, exceptionId);
	}

	
	/**
	 * Reset algorithm property.
	 *
	 * @param type the type
	 * @param exceptionId the exception id
	 * @param algoId the algo id
	 * @return the response
	 * @throws RestException the rest exception
	 * @throws DaoException the dao exception
	 */
	@POST
	@Path("resetAlgorithmProperty/{id}")
	public Response resetAlgorithmProperty(@QueryParam("type") String type,
			@QueryParam("exceptionId") Integer exceptionId, @PathParam("id") Integer algoId, @QueryParam("reason") String reason) {
		logger.info("Going to reset properties : {}, type : {}, id : {} ", algoId, type, exceptionId);
		Preconditions.checkNoneNull(algoId);
		service.resetAlgorithmPropertyData(algoId, type, exceptionId, reason);
		return Response.ok("{\"Result\":\"Success\"}").build();
	}


	
	/**
	 * Creates the properties.
	 *
	 * @param algorithmId the algorithm id
	 * @param newExceptions the new exceptions
	 * @return the response
	 * @throws RestException the rest exception
	 */
	@POST
	@Path("createProperties/{algorithmId}")
	public Response createProperties(@PathParam("algorithmId") Integer algorithmId,
			List<AlgorithmProperty> newExceptions) {
		Preconditions.checkNoneNull(algorithmId, newExceptions);
		logger.info("Going to create exception for algorithm : {}, exception : {}", algorithmId,
				newExceptions.size());
		service.createProperties(algorithmId, newExceptions);
		return Response.ok("{\"Result\":\"Success\"}").build();
	}


	/**
	 * Update properties.
	 *
	 * @param propertyList the property list
	 * @param id the id
	 * @return the response
	 * @throws RestException the rest exception
	 */
	@POST
	@Path("updateProperties/{algorithmId}")
	public Response updateProperties(List<AlgorithmProperty> propertyList,@PathParam("algorithmId")  Integer id) {
		logger.info("going to update following properties: {} ", propertyList);
		Preconditions.checkNotEmpty(propertyList);
		service.updateProperties(propertyList,id);
		return Response.ok("{\"Result\":\"Success\"}").build();
	}
	
	/**
	 * Gets the property by name.
	 *
	 * @param propertyName the property name
	 * @return the property by name
	 * @throws RestException the rest exception
	 */
	@GET
	@Path("getPropertyByName/{propertyName}")
	public List<AlgorithmProperty> getPropertyByName(@PathParam("propertyName") String propertyName){
		try {
			logger.info("Going to get AlgorithmProperty by propertyName : {}",propertyName);
			return service.getPropertyByName(propertyName);
		}catch(Exception e) {
			logger.error("Exception inside getPropertyByName {}",ExceptionUtils.getStackTrace(e));
			throw new RestException(e);
		}
	}

	/**
	 * Delete property.
	 *
	 * @param propertyId the property id
	 * @return the response
	 * @throws RestException the rest exception
	 */
	@POST
	@Path("deleteProperty/{propertyId}")
	public Response deleteProperty(@PathParam("propertyId") Integer propertyId) {
		logger.info("Going to delete property : {}", propertyId);
		Preconditions.checkNotNull(propertyId);
		service.deleteProperty(propertyId);
		return Response.ok("{\"Result\":\"Success\"}").build();
	}
	
	/**
	 * Gets the property modification history.
	 *
	 * @param propertyId the property id
	 * @return the property history
	 * @throws RestException the rest exception
	 */
	@GET
	@Path("getPropertyHistory/{propertyId}")
	public List<Object[]> getPropertyHistory(@PathParam("propertyId") Integer propertyId) {
		logger.info("Going to get property history : {}", propertyId);
		Preconditions.checkNotNull(propertyId);
		return service.getPropertyHistory(propertyId);
	}
	
	@POST
	@Path("addProperties/{algorithmId}")
	public Response addProperties(@PathParam("algorithmId") Integer algorithmId,
			List<AlgorithmProperty> properties) {
		Preconditions.checkNoneNull(algorithmId, properties);
		logger.info("Going to add properties for algorithm : {}, exception : {}", algorithmId,
				properties.size());
		service.addProperties(algorithmId, properties);
		return Response.ok("{\"Result\":\"Success\"}").build();
	}

	@POST
	@Path("updatePropertiesConfiguration/{algorithmId}")
	public Response updatePropertiesConfiguration(List<AlgorithmProperty> propertyList,
			@PathParam("algorithmId") Integer algorithmId) {
		logger.info("going to update following properties configuration : {} ", propertyList);
		Preconditions.checkNotEmpty(propertyList);
		service.updatePropertiesConfiguration(propertyList, algorithmId);
		return Response.ok("{\"Result\":\"Success\"}").build();
	}
		
	
	
}
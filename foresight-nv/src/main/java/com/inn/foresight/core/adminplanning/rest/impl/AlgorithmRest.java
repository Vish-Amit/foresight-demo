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

import org.apache.cxf.jaxrs.ext.search.SearchContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.rest.AbstractCXFRestService;
import com.inn.core.generic.service.IGenericService;
import com.inn.core.generic.utils.Preconditions;
import com.inn.foresight.core.adminplanning.model.Algorithm;
import com.inn.foresight.core.adminplanning.service.IAlgorithmService;

/**
 * The Class AlgorithmRestImpl.
 */
@Path("/Algorithm")
@Produces("application/json")
@Consumes("application/json")
@Service("AlgorithmRest")
public class AlgorithmRest extends AbstractCXFRestService<Integer, Algorithm> {
	/** The logger. */
	private Logger logger = LogManager.getLogger(AlgorithmRest.class);

	/** The service. */
	@Autowired
	private IAlgorithmService service;

	/** The context. */
	@Context
	private SearchContext context;

	/**
	 * Instantiates a new algorithm rest impl.
	 */
	public AlgorithmRest() {
		super(Algorithm.class);
	}

	/* (non-Javadoc)
	 * @see com.inn.foresight.core.generic.rest.IGenericRest#search(java.lang.Object)
	 */
	@GET
	@Override
	@Path("search")
	public List<Algorithm> search(@QueryParam("") Algorithm entity) {
		logger.info("Searching AlgorithmProperty entity :" + entity);
		try {
			return service.search(entity);
		} catch (RestException e) {
			throw new RestException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.inn.foresight.core.generic.rest.IGenericRest#findById(java.lang.Object)
	 */
	@GET
	@Override
	@Path("findById")
	public Algorithm findById(@QueryParam("id") Integer id) {
		logger.info("Finding AlgorithmProperty by id :" + id);
		try {
			return service.findById(id);
		} catch (RestException e) {
			throw new RestException(e);
		}

	}

	/* (non-Javadoc)
	 * @see com.inn.foresight.core.generic.rest.IGenericRest#findAll()
	 */
	@GET
	@Override
	@Path("findAll")
	public List<Algorithm> findAll() {
		try {
			return service.findAll();
		} catch (RestException e) {
			throw new RestException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.inn.foresight.core.generic.rest.IGenericRest#create(java.lang.Object)
	 */
	@Override
	@POST
	@Path("create")
	public Algorithm create(Algorithm algorithmProperty) {

		try {
			return service.create(algorithmProperty);
		} catch (RestException e) {
			throw new RestException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.inn.foresight.core.generic.rest.IGenericRest#update(java.lang.Object)
	 */
	@Override
	@POST
	@Path("update")
	public Algorithm update(Algorithm algorithmProperty) {
		logger.info("Updating AlgorithmProperty by an entity :" + algorithmProperty);
		try {
			return service.update(algorithmProperty);
		} catch (RestException e) {
			throw new RestException(e);
		}
	}
	
	/**
	 * Search algorithm.
	 *
	 * @param displayName the display name
	 * @return the list
	 * @throws RestException the rest exception
	 */
	@GET
	@Path("searchAlgorithm")
	public List<Algorithm> searchAlgorithm(@QueryParam("displayName") String displayName) {
		Preconditions.checkNoneNull(displayName);
		logger.info("Going to search searchAlgorithmProperty data @displayName : {}",displayName);
		return service.searchAlgorithmData(displayName);
	}
	

	/* (non-Javadoc)
	 * @see com.inn.foresight.core.generic.rest.IGenericRest#remove(java.lang.Object)
	 */
	@Override
	public boolean remove(Algorithm algorithmProperty) {
		logger.info("Removing AlgorithmProperty record by an entity :" + algorithmProperty);
		try {
			service.remove(algorithmProperty);
		} catch (RestException e) {
			throw new RestException(e);
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.inn.foresight.core.generic.rest.IGenericRest#removeById(java.lang.Object)
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

	/* (non-Javadoc)
	 * @see com.inn.foresight.core.generic.rest.AbstractCXFRestService#getService()
	 */
	@Override
	public IGenericService<Integer, Algorithm> getService() {
		return service;
	}

	/* (non-Javadoc)
	 * @see com.inn.foresight.core.generic.rest.AbstractCXFRestService#getSearchContext()
	 */
	@Override
	public SearchContext getSearchContext() {
		return context;
	}

	/**
	 * Gets the all algorithms.
	 *
	 * @param llimit the pagination lower limit
	 * @param ulimit the pagination upper limit
	 * @return the all algorithms
	 * @throws RestException the rest exception
	 */
	@GET
	@Path("getAllAlgorithm")
	public List<Algorithm> getAllAlgorithms() {
		logger.info("Going to get all algorithms data");
		return service.getAllAlgorithms();
	}
	
	
	/**
	 * Creates the algorithm.
	 *
	 * @param algorithm the algorithm property
	 * @return the response
	 * @throws RestException the rest exception
	 */
	@POST
	@Path("createAlgorithm")
	public Response createAlgorithm(Algorithm algorithm) {
		logger.info("Updating AlgorithmProperty by an entity :" + algorithm);
		Preconditions.checkNotNull(algorithm);
		service.createAlgorithm(algorithm);
		return Response.ok("{\"Result\":\"Success\"}").build();
	}
	
	@POST
	@Path("deleteAlgorithm/{algorithmId}")
	public Response deleteAlgorithm(@PathParam("algorithmId") Integer algorithmId) {
		logger.info("Going to delete algorithm and it's properties : {}", algorithmId);
		Preconditions.checkNotNull(algorithmId);
		service.deleteAlgorithm(algorithmId);
		return Response.ok("{\"Result\":\"Success\"}").build();
	}
	
	@POST
	@Path("updateAlgorithm")
	public Response updateAlgorithm(Algorithm algorithm) {
		logger.info("Updating AlgorithmProperty by an entity :" + algorithm);
		Preconditions.checkNotNull(algorithm);
		Preconditions.checkArgument(algorithm.getId() != null, "Algorithm ID is missing.");
		service.updateAlgorithm(algorithm);
		return Response.ok("{\"Result\":\"Success\"}").build();
	}

}
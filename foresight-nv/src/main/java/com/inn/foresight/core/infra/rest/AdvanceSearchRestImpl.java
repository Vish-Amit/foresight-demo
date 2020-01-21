package com.inn.foresight.core.infra.rest;

import java.util.List;
import java.util.Map;

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
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.infra.model.AdvanceSearch;
import com.inn.foresight.core.infra.service.IAdvanceSearchService;

/**
 * The Class AdvanceSearchRestImpl.
 */
@Path("/AdvanceSearch")
@Produces("application/json")
@Consumes("application/json")
@Service("AdvanceSearchRestImpl")
public class AdvanceSearchRestImpl extends AbstractCXFRestService<Integer, AdvanceSearch> {

	/** The logger. */
	private Logger logger = LogManager.getLogger(AdvanceSearchRestImpl.class);

	/**
	 * Instantiates a new advance search rest impl.
	 */
	public AdvanceSearchRestImpl() {
		super(AdvanceSearch.class);
	}

	/** The advance search service. */
	@Autowired
	private IAdvanceSearchService advanceSearchService;

	/** The context. */
	@Context
	private SearchContext context;

	/**
	 * Search.
	 *
	 * @param entity the entity
	 * @return the list
	 * @throws RestException the rest exception
	 */
	@GET
	public List<AdvanceSearch> search(@QueryParam("") AdvanceSearch entity) {
		try {
			return advanceSearchService.search(entity);
		} catch (RestException e) {
			logger.error(e.getMessage());
			throw new RestException(e.getMessage());
		}
	}

	/**
	 * Search.
	 *
	 * @param lowerLimit the lower limit
	 * @param upperLimit the upper limit
	 * @return the list
	 * @throws RestException the rest exception
	 */
	@GET
	@Path("search")
	@Produces("application/json")
	public List<AdvanceSearch> search(@QueryParam("llimit") Integer lowerLimit,
			@QueryParam("ulimit") Integer upperLimit) {
		try {
			transform(context);
			return advanceSearchService.searchWithLimit(context, upperLimit, lowerLimit);
		} catch (RestException e) {
			logger.error(e.getMessage());
			throw new RestException(e.getMessage());
		}
	}

	/**
	 * Find by id.
	 *
	 * @param primaryKey the primary key
	 * @return the advance search
	 * @throws RestException the rest exception
	 */
	@Path("findById")
	public AdvanceSearch findById(@QueryParam("id") Integer primaryKey) {
		try {
			logger.info("Finding advance search by Id " + primaryKey);
			return advanceSearchService.findById(primaryKey);
		} catch (RestException e) {
			logger.error(e.getMessage());
			throw new RestException(e.getMessage());
		}
	}

	/**
	 * Find all.
	 *
	 * @return the list
	 * @throws RestException the rest exception
	 */
	@Path("findAll")
	@Override
	public List<AdvanceSearch> findAll() {
		try {
			return advanceSearchService.findAll();
		} catch (RestException e) {
			logger.error(e.getMessage());
			throw new RestException(e.getMessage());
		}
	}

	/**
	 * Creates the.
	 *
	 * @param anEntity the an entity
	 * @return the advance search
	 * @throws RestException the rest exception
	 */
	@Override
	@POST
	@Path("create")
	public AdvanceSearch create(AdvanceSearch anEntity) {
		try {
			return advanceSearchService.create(anEntity);
		} catch (RestException e) {
			logger.error(e.getMessage());
			throw new RestException(e.getMessage());
		}
	}

	/**
	 * Update.
	 *
	 * @param anEntity the an entity
	 * @return the advance search
	 * @throws RestException the rest exception
	 */
	@Override
	@POST
	@Path("update")
	public AdvanceSearch update(AdvanceSearch anEntity) {
		try {
			return advanceSearchService.update(anEntity);
		} catch (RestException e) {
			logger.error(e.getMessage());
			throw new RestException(e.getMessage());
		}
	}

	/**
	 * Removes the.
	 *
	 * @param anEntity the an entity
	 * @return true, if successful
	 * @throws RestException the rest exception
	 */
	@Override
	@Path("delete")
	public boolean remove(AdvanceSearch anEntity) {
		try {
			advanceSearchService.remove(anEntity);
			return true;
		} catch (RestException e) {
			logger.error(e.getMessage());
			throw new RestException(e.getMessage());
		}
	}

	/**
	 * Removes the by id.
	 *
	 * @param primaryKey the primary key
	 * @throws RestException the rest exception
	 */
	@Override
	@Path("deleteById")
	public void removeById(@PathParam("id") Integer primaryKey) {
		try {
			advanceSearchService.removeById(primaryKey);
		} catch (RestException e) {
			logger.error(e.getMessage());
			throw new RestException(e.getMessage());
		}
	}

	/**
	 * Gets the service.
	 *
	 * @return the service
	 */
	@Override
	public IGenericService<Integer, AdvanceSearch> getService() {
		return advanceSearchService;
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
	 * Search for data.
	 *
	 * @param id the id
	 * @return the map
	 * @throws RestException the rest exception
	 */
	@GET
	@Path("searchdata/{id}")
	public Response searchForData(@PathParam("id") Integer id) {
		logger.debug("Inside searchForData, id: " + id);
		return Response.ok(advanceSearchService.searchForMapData(id, null)).build();
	}

	@GET
	@Path("searchdata/{id}/{zoom}")
	public Response searchForDataBoundary(@PathParam("id") Integer id, @PathParam("zoom") Integer zoom) {
		logger.debug("Inside searchForData, id: {}, zoom: {}", id, zoom);
		return Response.ok(advanceSearchService.searchForMapData(id, zoom)).build();
	}

	@POST
	@Path("/searchDetails")
	public Response searchDetails(Map<String, Object> map) {
		logger.debug("Inside searchForData, Map : {}", map);
		return Response.ok(advanceSearchService.searchDetails(map)).build();
	}

	/**
	 * Gets the advance search by name.
	 *
	 * @param name the name
	 * @return the advance search by name
	 * @throws RestException the rest exception
	 */
	@GET
	@Path("getAdvanceSearchByName/{name}")
	public List<AdvanceSearch> getAdvanceSearchByName(@PathParam("name") String name) {
		try {
			return advanceSearchService.getAdvanceSearchByName(name);
		} catch (RestException e) {
			logger.error("RestException in getAdvanceSearchByName: err={} ", e.getMessage());
			throw new RestException(e.getMessage());
		}
	}

	/**
	 * Gets the advance search by type list.
	 *
	 * @param name the name
	 * @param type the type
	 * @return the advance search by type list
	 * @throws RestException the rest exception
	 */
	@POST
	@Path("getAdvanceSearchByTypeList/{name}")
	public List<AdvanceSearch> getAdvanceSearchByTypeList(@PathParam("name") String name, List<String> type) {
		logger.info("Inside getAdvanceSearchByTypeList");
		if (name != null && type != null && !type.isEmpty())
			return advanceSearchService.getAdvanceSearchByTypeList(name, type);
		else
			throw new RestException(ForesightConstants.INVALID_PARAMETER);

	}

	/**
	 * Gets the address from google.
	 *
	 * @param searchStr the search str
	 * @return the address from google
	 */
	@GET
	@Path("getAddressFromGoogle")
	@Produces("application/json")
	public Map getAddressFromGoogle(@QueryParam("address") String address) {
		logger.info("Going to get address from google searched string = {}", address);
		return advanceSearchService.getAddressFromGoogle(address);

	}

	/**
	 * Search to map data.
	 *
	 * @param id the id
	 * @return the object
	 * @throws RestException the rest exception
	 */
	@GET
	@Path("searchToMapData/{id}")
	public Object searchToMapData(@PathParam("id") Integer id) {
		logger.info("Going to search To MapData by AdvanceSearch id {}", id);
		if (id == null) {
			throw new RestException(ForesightConstants.INVALID_PARAMETERS);
		}
		return advanceSearchService.searchToMapData(id);
	}

	/**
	 * Gets the advance search for IP topology.
	 *
	 * @param name the name
	 * @return the advance search for IP topology
	 * @throws RestException the rest exception
	 */
	@GET
	@Path("getAdvanceSearchForIPTopology/{name}")
	public List<AdvanceSearch> getAdvanceSearchForIPTopology(@PathParam("name") String name) {
		try {
			return advanceSearchService.getAdvanceSearchForIPTopology(name);
		} catch (RestException e) {
			logger.error("RestException in getAdvanceSearchForIPTopology: err={} ", e.getMessage());
			throw new RestException(e.getMessage());
		}
	}

	@GET
	@Path("searchNeIdByType")
	public List<String> searchNeIdByNeType(@QueryParam("neId") String neId, @QueryParam("neType") String neType) {
		logger.info("Inside @Class : " + this.getClass().getName() + " searchNeIdByNeType " + "neId {} neType {}", neId,
				neType);
		if (neId == null || neType == null) {
			throw new RestException(ForesightConstants.EXCEPTION_INVALID_PARAMS);
		}
		return advanceSearchService.searchNeIdByNeType(neId, neType);
	}

	@POST
	@Path("getAdvanceSearchByTypeListAndVendor/{name}")
	public List<AdvanceSearch> getAdvanceSearchByTypeListAndVendor(@PathParam("name") String name,
			Map<String, List<String>> map) {
		logger.info("Inside @Class : " + this.getClass().getName());
		if (name != null && map != null && !map.isEmpty())
			return advanceSearchService.getAdvanceSearchByTypeListAndVendor(name, map);
		else
			throw new RestException(ForesightConstants.INVALID_PARAMETER);

	}

}
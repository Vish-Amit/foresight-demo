package com.inn.foresight.module.nv.dashboard.rest.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
import com.inn.foresight.module.nv.dashboard.model.NVDashboardMeta;
import com.inn.foresight.module.nv.dashboard.service.INVDashboardMetaService;


/** The Class NVDashboardMetaRestImpl. */
@Path("/NVDashboardMeta")
@Produces(ForesightConstants.APPLICATION_SLASH_JSON)
@Consumes(ForesightConstants.APPLICATION_SLASH_JSON)
@Service("NVDashboardMetaRestImpl")
public class NVDashboardMetaRestImpl extends
		AbstractCXFRestService<Integer, NVDashboardMeta> {
	
	/** The logger. */
	private Logger logger = LogManager.getLogger(NVDashboardMetaRestImpl.class);

	/** The i NV dashboard meta service. */
	@Autowired
	private INVDashboardMetaService iNVDashboardMetaService;

	/** The context. */
	@Context
	private SearchContext context;
	
	/** Instantiates a new NV dashboard meta rest impl. */
	public NVDashboardMetaRestImpl() {
		super(NVDashboardMeta.class);
	}

	/**
	 * Search.
	 *
	 * @param entity the entity
	 * @return the list
	 * @throws RestException the rest exception
	 */
	@Override
	public List<NVDashboardMeta> search(NVDashboardMeta entity) {
		try {
			return iNVDashboardMetaService.search(entity);
		} catch (RestException e) {
			logger.info("Exception in search method");
		}
		return Collections.emptyList();
	}

	/**
	 * Find by id.
	 *
	 * @param primaryKey the primary key
	 * @return the NV dashboard meta
	 * @throws RestException the rest exception
	 */
	@Override
	public NVDashboardMeta findById(Integer primaryKey) {
		try {
			return iNVDashboardMetaService.findById(primaryKey);
		} catch (RestException e) {
			logger.info("Exception in findById method");
		}
		return null;
	}

	/**
	 * Find all.
	 *
	 * @return the list
	 * @throws RestException the rest exception
	 */
	@Override
	public List<NVDashboardMeta> findAll() {
		try {
			return iNVDashboardMetaService.findAll();
		} catch (RestException e) {
			logger.info("Exception in findAll method");
		}
		return Collections.emptyList();
	}

	/**
	 * Creates the.
	 *
	 * @param anEntity the an entity
	 * @return the NV dashboard meta
	 * @throws RestException the rest exception
	 */
	@Override
	public NVDashboardMeta create(NVDashboardMeta anEntity) {
		try {
			return iNVDashboardMetaService.create(anEntity);
		} catch (RestException e) {
			logger.info("Exception in create method");
		}
		return null;
	}

	/**
	 * Update.
	 *
	 * @param anEntity the an entity
	 * @return the NV dashboard meta
	 * @throws RestException the rest exception
	 */
	@Override
	public NVDashboardMeta update(NVDashboardMeta anEntity) {
		try {
			return iNVDashboardMetaService.update(anEntity);
		} catch (RestException e) {
			logger.info("Exception in update method");
		}
		return null;
	}

	/**
	 * Removes the.
	 *
	 * @param anEntity the an entity
	 * @return true, if successful
	 * @throws RestException the rest exception
	 */
	@Override
	public boolean remove(NVDashboardMeta anEntity) {
		try {
			iNVDashboardMetaService.remove(anEntity);
			return true;
		} catch (RestException e) {
			logger.info("Exception in remove method");
			return false;
		}

	}

	/**
	 * Removes the by id.
	 *
	 * @param primaryKey the primary key
	 * @throws RestException the rest exception
	 */
	@Override
	public void removeById(Integer primaryKey) {
		try {
			iNVDashboardMetaService.removeById(primaryKey);
		} catch (RestException e) {
			logger.info("Exception in removeById method");
		}
	}

	/**
	 * Gets the service.
	 *
	 * @return the service
	 */
	@Override
	public IGenericService<Integer, NVDashboardMeta> getService() {
		return iNVDashboardMetaService;
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
	 * Gets the all NV dashboard meta.
	 *
	 * @return the all NV dashboard meta
	 */
	@GET
	@Produces(ForesightConstants.APPLICATION_SLASH_JSON)
	@Path("getAllNVDashboardMeta")
	public Response getAllNVDashboardMeta() {
		logger.info("In getAllNVDashboardMeta");
		
		List<NVDashboardMeta> nvDashboardMetas = new ArrayList<>();
		try {
			nvDashboardMetas = iNVDashboardMetaService.getAllNvDashboardMetaData();
		} catch (RestException e) {
			logger.error("Error while getting usercount data data and message is {}", e.getMessage());
		}
		return Response.ok(nvDashboardMetas).build();
	}
	
}

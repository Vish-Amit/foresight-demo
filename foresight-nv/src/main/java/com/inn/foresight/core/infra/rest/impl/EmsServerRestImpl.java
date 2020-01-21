package com.inn.foresight.core.infra.rest.impl;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.apache.cxf.jaxrs.ext.search.SearchContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.rest.AbstractCXFRestService;
import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.model.EmsServer;
import com.inn.foresight.core.infra.service.IEmsServerService;
import com.inn.foresight.core.infra.utils.InfraConstants;

/**
 * The Class EmsServerRestImpl.
 */
/**
 * @author ist
 *
 */
/**
 * @author ist
 *
 */
@Path("EmsServer")
@Produces("application/json")
@Consumes("application/json")
public class EmsServerRestImpl extends AbstractCXFRestService<Integer, EmsServer> {

	/** The logger. */
	private Logger logger = LogManager.getLogger(EmsServerRestImpl.class);

	/**
	 * Instantiates a new EmsServer rest impl.
	 */
	public EmsServerRestImpl() {
		super(EmsServer.class);
	}

	/** The ems server service. */
	@Autowired
	IEmsServerService emsServerService;

	/** The context. */
	@Context
	private SearchContext context;

	/**
	 * Find all.
	 *
	 * @return the list
	 * @throws RestException the rest exception
	 */
	@Override
	public List<EmsServer> findAll() {
		return null;
	}

	/**
	 * Returns the EmsServer finding by id.
	 * 
	 * @param id
	 *            the id
	 * @return the EmsServer
	 * @throws RestException
	 *             the business exception
	 * @parameter id of type Integer
	 * @returns a EmsServer record
	 */
	@Override
	public EmsServer findById(@QueryParam("") Integer id) {
		logger.info("Inside  @class :" + this.getClass().getName() + " @Method :findById @Param: id " + id);
		return null;
	}

	/**
	 * Returns the record by searching EmsServer name.
	 *
	 * @param EmsServerInformation the ems server information
	 * @return the list
	 * @throws RestException             the business exception
	 * @parameter EmsServer of typecellSite
	 * @returns a list of EmsServer record
	 */
	@Override
	@GET
	public List<EmsServer> search(@QueryParam("") EmsServer EmsServerInformation) {
		logger.info("Inside  @class :" + this.getClass().getName() + " @Method :search @Param: EmsServerInformation " + EmsServerInformation);
		return null;
	}

	/**
	 * Creates the.
	 *
	 * @param EmsServerInformation the ems server information
	 * @return the ems server
	 * @throws RestException the rest exception
	 */
	@Override
	@POST
	@Path("create")
	public EmsServer create(@Valid EmsServer EmsServerInformation) {
		logger.info("Inside  @class :" + this.getClass().getName() + " @Method :create @Param: EmsServerInformation " + EmsServerInformation);
		try {
			EmsServerInformation = emsServerService.create(EmsServerInformation);
		} catch (Exception e) {
			logger.error("Error occurred  @class" + this.getClass().getName() + " @Method :create", e);
		}
		logger.info("Inside  @class :" + this.getClass().getName() + " @Method :create @Return: " + EmsServerInformation);
		return EmsServerInformation;

	}

	/**
	 * Update.
	 *
	 * @param EmsServerInformation the ems server information
	 * @return the ems server
	 * @throws RestException the rest exception
	 */
	@Override
	@POST
	@Path("update")
	public EmsServer update(@Valid EmsServer EmsServerInformation) {
		logger.info("Inside  @class :" + this.getClass().getName() + " @Method :update @Param: EmsServerInformation " + EmsServerInformation);
		try {
			EmsServerInformation = emsServerService.update(EmsServerInformation);
		} catch (Exception e) {
			logger.error("Error occurred  @class" + this.getClass().getName() + " @Method :update", e);
		}
		logger.info("Inside  @class :" + this.getClass().getName() + " @Method :update @Return: " + EmsServerInformation);
		return EmsServerInformation;
	}

	/**
	 * Returns the removed EmsServerInformation record get path and delete
	 * EmsServerInformation record .
	 *
	 * @param asitetaskInformation the asitetask information
	 * @return true, if successful
	 * @throws RestException             the business exception
	 * @parameter valid EmsServerInformation entity
	 * @returns a removed EmsServerInformation record
	 */

	@Override
	@Path("delete")
	public boolean remove(EmsServer asitetaskInformation) {
		logger.info("Inside  @class :" + this.getClass().getName() + " @Method :remove @Param: asitetaskInformation " + asitetaskInformation);
		try {
			emsServerService.remove(asitetaskInformation);
		} catch (Exception e) {
			logger.error("Error occurred  @class" + this.getClass().getName() + " @Method :remove", e);
		}
		logger.info("Inside  @class :" + this.getClass().getName() + " @Method :remove @Return: " + true);
		return true;
	}

	/**
	 * method remove audit action get path to remove audit action.
	 * 
	 * @param primaryKey
	 *            the primary key
	 * @throws RestException
	 *             the business exception
	 * @parameter id of type Integer in path param
	 */
	@DELETE
	@Override
	@Path("delete/{id}")
	public void removeById(@PathParam("id") Integer primaryKey) {
		logger.info("Inside  @class :" + this.getClass().getName() + " @Method :removeById @Param: primaryKey " + primaryKey);
		try {
			emsServerService.removeById(primaryKey);
		} catch (Exception e) {
			logger.error("Error occurred  @class" + this.getClass().getName() + " @Method :removeById", e);

		}

	}

	/**
	 * Gets the service.
	 *
	 * @return the service
	 */
	@Override
	public IGenericService<Integer, EmsServer> getService() {
		logger.info("Inside  @class :" + this.getClass().getName() + " @Method :getService");
		return emsServerService;
	}

	/**
	 * Gets the search context.
	 *
	 * @return the search context
	 */
	@Override
	public SearchContext getSearchContext() {
		logger.info("Inside  @class :" + this.getClass().getName() + " @Method :getSearchContext");
		return context;
	}
	
	@GET
	@Path("getEmsServer/{geographyId}/{emsType}/{geoType}")
	public List<Map<String, Object>> getEmsServer(@PathParam("geographyId") Integer geographyId,@PathParam("emsType") String emsType,@PathParam("geoType") String geoType,
			@QueryParam("domain") String domain, @QueryParam("vendor") String vendor, @QueryParam("technology") String technology) {
		logger.info("Going to get ip traceport ip for geoType {}, geographyId {}, domain {}, vendor {} and emsType {}", geoType,geographyId, domain,vendor,emsType);
		if (!Utils.isValidParameter(geographyId, emsType,geoType,domain, vendor)) {
			throw new RestException(InfraConstants.INVALID_PARAMETERS);
		}
		try {
			return emsServerService.getEmsServer(geographyId, emsType,geoType,domain, vendor,technology);
		} catch (Exception e) {
			logger.error("Error in getting getIpListFromEmsServer error {}", Utils.getStackTrace(e));
			throw new RestException(e);
		}
	}

	@GET
	@Path("getEMSServerNameAndIP")
	public List<Map<String, String>> getEMSServerNameAndIP(@QueryParam("domain") String domain,
			@QueryParam("vendor") String vendor, @QueryParam("technology") String technology) {
		logger.info("inside getEMSServerNameAndIP with domain:{},vendor:{},technology:{}", domain, vendor, technology);
		return emsServerService.getEmsNameAndIpByDomainAndVendor(domain, vendor, technology);
	}

	@GET
	@Path("getDistinctEmsName")
	public List<String> getDistinctEmsName() {
		logger.info("Going to get getDistincEmsName {}");
		return emsServerService.getDistinctEmsName();
	
	}
	
	
	@GET
	@Path("getDistinctEmsIP")
	public List<String> getDistinctEmsIP() {
		logger.info("Going to get getDistincEmsIP {}");
		return	emsServerService.getDistinctEmsIP();
		
	}
}
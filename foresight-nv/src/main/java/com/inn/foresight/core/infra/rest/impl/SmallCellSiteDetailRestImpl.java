package com.inn.foresight.core.infra.rest.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import org.apache.cxf.jaxrs.ext.search.SearchContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;

import com.inn.commons.jaxrs.ResponseUtils;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.rest.AbstractCXFRestService;
import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.infra.model.SmallCellSiteDetail;
import com.inn.foresight.core.infra.service.ISmallCellSiteDetailService;
import com.inn.foresight.core.infra.utils.InfraConstants;
import com.inn.foresight.core.infra.wrapper.SmallCellSiteDetailWrapper;

/**
 * The Class SmallCellSiteDetailRestImpl.
 */
@Path("/SmallCellSiteDetail")
@Produces("application/json")
@Consumes("application/json")
public class SmallCellSiteDetailRestImpl extends AbstractCXFRestService<Integer, SmallCellSiteDetail> {

	/** The logger. */
	private Logger logger = LogManager.getLogger(SmallCellSiteDetailRestImpl.class);

	/**
	 * Instantiates a new SmallCellSiteDetail rest impl.
	 */
	public SmallCellSiteDetailRestImpl() {
		super(SmallCellSiteDetail.class);
	}

	/** The Small cell site detail service. */
	@Autowired
	ISmallCellSiteDetailService smallCellSiteDetailService;

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
	public List<SmallCellSiteDetail> findAll() {
		return null;
	}

	/**
	 * Returns the SmallCellSiteDetail finding by id.
	 * 
	 * @param id
	 *            the id
	 * @return the SmallCellSiteDetail
	 * @throws RestException
	 *             the business exception
	 * @parameter id of type Integer
	 * @returns a SmallCellSiteDetail record
	 */
	@Override
	public SmallCellSiteDetail findById(@QueryParam("") Integer id) {
		logger.info("Inside  @class :" + this.getClass().getName() + " @Method :findById @Param: id " + id);
		return null;
	}

	/**
	 * Returns the record by searching SmallCellSiteDetail name.
	 *
	 * @param SmallCellSiteDetailInformation the small cell site detail information
	 * @return the list
	 * @throws RestException             the business exception
	 * @parameter SmallCellSiteDetail of typecellSite
	 * @returns a list of SmallCellSiteDetail record
	 */
	@Override
	@GET
	public List<SmallCellSiteDetail> search(@QueryParam("") SmallCellSiteDetail SmallCellSiteDetailInformation) {
		logger.info("Inside  @class :" + this.getClass().getName() + " @Method :search @Param: SmallCellSiteDetailInformation " + SmallCellSiteDetailInformation);
		return null;
	}

	/**
	 * Creates the.
	 *
	 * @param SmallCellSiteDetailInformation the small cell site detail information
	 * @return the small cell site detail
	 * @throws RestException the rest exception
	 */
	@Override
	@POST
	@Path("create")
	public SmallCellSiteDetail create(@Valid SmallCellSiteDetail SmallCellSiteDetailInformation) {
		logger.info("Inside  @class :" + this.getClass().getName() + " @Method :create @Param: SmallCellSiteDetailInformation " + SmallCellSiteDetailInformation);
		try {
			SmallCellSiteDetailInformation = smallCellSiteDetailService.create(SmallCellSiteDetailInformation);
		} catch (Exception e) {
			logger.error("Error occurred  @class" + this.getClass().getName() + " @Method :create", e);
		}
		logger.info("Inside  @class :" + this.getClass().getName() + " @Method :create @Return: " + SmallCellSiteDetailInformation);
		return SmallCellSiteDetailInformation;

	}

	/**
	 * Update.
	 *
	 * @param SmallCellSiteDetailInformation the small cell site detail information
	 * @return the small cell site detail
	 * @throws RestException the rest exception
	 */
	@Override
	@POST
	@Path("update")
	public SmallCellSiteDetail update(@Valid SmallCellSiteDetail SmallCellSiteDetailInformation) {
		logger.info("Inside  @class :" + this.getClass().getName() + " @Method :update @Param: SmallCellSiteDetailInformation " + SmallCellSiteDetailInformation);
		try {
			SmallCellSiteDetailInformation = smallCellSiteDetailService.update(SmallCellSiteDetailInformation);
		} catch (Exception e) {
			logger.error("Error occurred  @class" + this.getClass().getName() + " @Method :update", e);
		}
		logger.info("Inside  @class :" + this.getClass().getName() + " @Method :update @Return: " + SmallCellSiteDetailInformation);
		return SmallCellSiteDetailInformation;
	}

	/**
	 * Returns the removed SmallCellSiteDetailInformation record get path and delete
	 * SmallCellSiteDetailInformation record .
	 *
	 * @param asitetaskInformation the asitetask information
	 * @return true, if successful
	 * @throws RestException             the business exception
	 * @parameter valid SmallCellSiteDetailInformation entity
	 * @returns a removed SmallCellSiteDetailInformation record
	 */

	@Override
	@Path("delete")
	public boolean remove(SmallCellSiteDetail asitetaskInformation) {
		logger.info("Inside  @class :" + this.getClass().getName() + " @Method :remove @Param: asitetaskInformation " + asitetaskInformation);
		try {
			smallCellSiteDetailService.remove(asitetaskInformation);
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
			smallCellSiteDetailService.removeById(primaryKey);
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
	public IGenericService<Integer, SmallCellSiteDetail> getService() {
		logger.info("Inside  @class :" + this.getClass().getName() + " @Method :getService");
		return smallCellSiteDetailService;
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
	@Path("getSmallCellSiteDetail")
	public SmallCellSiteDetailWrapper getSmallCellSiteDetail(@QueryParam("neName") String neName) {
		logger.info("going to get onair outdoor site details by neName {} ", neName);
		return smallCellSiteDetailService.getSmallCellSiteDetails(neName);
	}
	
	@GET
	@Path("getSmallCellSiteDetailSummaryReport")
	@Produces("application/excel")
	public Response getSmallCellSiteDetailSummaryReport(@QueryParam("neName") String neName) {
		logger.info("Going to Download Summary Report for neName {} ", neName);
		if(neName != null) {
			String DATE_FORMAT = "ddMMyyyy";
			DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
			String fDate = dateFormat.format(new Date());
			String fileName = ForesightConstants.SMALLCELL_SITE + ForesightConstants.UNDERSCORE + neName + ForesightConstants.UNDERSCORE + fDate + ForesightConstants.FILE_EXTENSION_EXCEL;
			ResponseBuilder response = Response.ok(smallCellSiteDetailService.getReportForSmallCellSiteSummary(neName,fileName));
			response.header("fileName",fileName);
			return response.build();
		}
		return null;
	}
	
	@GET
	@Path("getReportForSmallCellSiteParameter")
	@Produces("application/excel")
	public Response getReportForSmallCellSiteParameter(@QueryParam("neName") String neName) {
		logger.info("Going to Download Parameter Details Report for neName {} ", neName);
		if(neName != null) {
		String DATE_FORMAT = "ddMMyyyy";
		DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
		String fDate = dateFormat.format(new Date());
		String fileName = InfraConstants.SMALLCELL_PARAMETERS + ForesightConstants.UNDERSCORE + neName + ForesightConstants.UNDERSCORE +  fDate + ForesightConstants.FILE_EXTENSION_EXCEL;
		ResponseBuilder response = Response.ok(smallCellSiteDetailService.getReportForSmallCellSiteParameter(neName,fileName));
		response.header("fileName",fileName);
		return response.build();
		}
		return null;
	}
}
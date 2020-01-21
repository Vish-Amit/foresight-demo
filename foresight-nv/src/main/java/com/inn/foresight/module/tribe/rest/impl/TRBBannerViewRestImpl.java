package com.inn.foresight.module.tribe.rest.impl;

import java.util.List;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
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
import org.springframework.stereotype.Service;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.rest.AbstractCXFRestService;
import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.module.tribe.model.TRBBannerView;
import com.inn.foresight.module.tribe.service.ITRBBannerViewService;

/**
 * Rest.
 */

@Path("/TRBBannerView")
@Produces("application/json")
@Consumes("application/json")
@Service("TRBBannerViewRestImpl")
public class TRBBannerViewRestImpl extends AbstractCXFRestService<Integer, TRBBannerView> {
	
	/** The logger. */
	private Logger logger = LogManager.getLogger(TRBBannerViewRestImpl.class);

	/**
	 * Instantiates a new TRB banner view rest impl.
	 */
	public TRBBannerViewRestImpl() {
		super(TRBBannerView.class);
	}

	/** The trbbanner view service. */
	@Autowired
	private ITRBBannerViewService trbbannerViewService;

	/** The context. */
	@Context
	private SearchContext context;

	/**
	 * Find by id.
	 *
	 * @param id the id
	 * @return the TRB banner view
	 * @throws RestException the rest exception
	 */
	@Path("findById/{id}")
	@GET
	public TRBBannerView findById(@QueryParam("id") Integer id) throws RestException {
		logger.info("Inside  @class"+this.getClass().getName()+"@Method :findById @Param: id "+id);
		return trbbannerViewService.findById(id);

	}

	/**
	 * Search.
	 *
	 * @param lowerLimit the lower limit
	 * @param upperLimit the upper limit
	 * @param orderBy the order by
	 * @param orderType the order type
	 * @return the list
	 * @throws RestException the rest exception
	 */
	@GET
	@Path("search")
	@Produces("application/json")
	public List<TRBBannerView> search(@QueryParam("llimit") Integer lowerLimit,
			@QueryParam("ulimit") Integer upperLimit, @QueryParam("orderBy") String orderBy,
			@QueryParam("orderType") String orderType) throws RestException {
		logger.info("Inside  @class"+this.getClass().getName()+"@Method :search @Param: lowerLimit " + lowerLimit
				+ " ,upperLimit " + upperLimit + " ,orderBy " + orderBy + " ,orderType " + orderType);
		return trbbannerViewService.searchWithLimitAndOrderBy(context, upperLimit, lowerLimit, orderBy, orderType);

	}
	
	/**
	 * Gets the search record count.
	 *
	 * @return the search record count
	 */
	@GET
	@Path("getSearchRecordCount")
	@Produces("application/json")
	public Integer getSearchRecordCount() {
		return trbbannerViewService.getSearchRecordCount(context);
	}

	/**
	 * Creates the.
	 *
	 * @param trbbanner the trbbanner
	 * @return the TRB banner view
	 * @throws RestException the rest exception
	 */
	@POST
	@Path("create")
	public TRBBannerView create(@Valid TRBBannerView trbbanner) throws RestException {
		logger.info("Inside  @class"+this.getClass().getName()+"@Method :create @Param: trbbanner " + trbbanner.getId());
		TRBBannerView newDocComment = trbbannerViewService.create(trbbanner);
		return newDocComment;
	}

	/**
	 * Update.
	 *
	 * @param trbbanner the trbbanner
	 * @return the TRB banner view
	 * @throws RestException the rest exception
	 */
	@POST
	@Path("update")
	public TRBBannerView update(@Valid TRBBannerView trbbanner) throws RestException {
		logger.info("Inside  @class"+this.getClass().getName()+"@Method :update @Param: trbbanner " + trbbanner.getId());
		TRBBannerView newDocComment = trbbannerViewService.update(trbbanner);
		return newDocComment;
	}

	
	/**
	 * Removes the.
	 *
	 * @param trbbanner the trbbanner
	 * @return true, if successful
	 * @throws RestException the rest exception
	 */
	@Path("delete")
	public boolean remove(TRBBannerView trbbanner) throws RestException {
		logger.info("Inside  @class"+this.getClass().getName()+"@Method :remove @Param:  "+trbbanner.getId());
		trbbannerViewService.remove(trbbanner);
		return true;
	}

	
	/**
	 * Removes the by id.
	 *
	 * @param primaryKey the primary key
	 * @throws RestException the rest exception
	 */
	@POST
	@Path("delete/{id}")
	public void removeById(@PathParam("id") Integer primaryKey) throws RestException {
		logger.info("Inside  @class"+this.getClass().getName()+"@Method :removeById @Param: primaryKey " + primaryKey);
		TRBBannerView trbbanner = trbbannerViewService.findById(primaryKey);
		if (trbbanner != null) {
			trbbannerViewService.removeById(primaryKey);
		}
	}


	/**
	 * Gets the service.
	 *
	 * @return the service
	 */
	public IGenericService<Integer, TRBBannerView> getService() {
		logger.info("Inside  @class"+this.getClass().getName()+"@Method :getService ");
		return trbbannerViewService;
	}

	
	/**
	 * Gets the search context.
	 *
	 * @return the search context
	 */
	public SearchContext getSearchContext() {
		logger.info("Inside  @class"+this.getClass().getName()+"@Method :getSearchContext ");
		return context;
	}

	
	/**
	 * Search.
	 *
	 * @param entity the entity
	 * @return the list
	 */
	public List<TRBBannerView> search(TRBBannerView entity) {
		logger.info("Inside  @class"+this.getClass().getName()+"@Method :search ");

		return null;
	}

	
	/**
	 * Find all.
	 *
	 * @return the list
	 */
	public List<TRBBannerView> findAll() {
		logger.info("Inside  @class"+this.getClass().getName()+"@Method :findAll ");
		return null;
	}

	/**
	 * Creates the banner view.
	 *
	 * @param bannerId the banner id
	 * @return the string
	 */
	@POST
	@Path("/createBannerView/{bannerId}")
	public String createBannerView(@PathParam("bannerId") Integer bannerId) {
		logger.info("Inside  @class"+this.getClass().getName()+"@Method :createBannerView() @param"+bannerId);
		return trbbannerViewService.createBannerView(bannerId);
	}

	/**
	 * Creates the banner view from tab.
	 *
	 * @param bannerId the banner id
	 * @return the string
	 */
	@POST
	@Path("/createBannerViewFromTab/{bannerId}")
	public String createBannerViewFromTab(@PathParam("bannerId") Integer bannerId) {
		logger.info("Inside  @class"+this.getClass().getName()+"@Method :createBannerViewFromTab() @param"+bannerId);
		return trbbannerViewService.createBannerView(bannerId);
	}
	
	@GET
	@Path("/activeBannerCount")
	public Integer activeBannerCount() {
		logger.info("Inside  @class"+this.getClass().getName()+"@Method :activeBannerCount()");
		return trbbannerViewService.activeBannerCount();
	}
	
	@GET
	@Path("/inactiveBannerCount")
	public Integer inactiveBannerCount() {
		logger.info("Inside  @class"+this.getClass().getName()+"@Method :inactiveBannerCount()");
		return trbbannerViewService.inactiveBannerCount();
	}
	
	@GET
	@Path("/draftBannerCount")
	public Integer draftBannerCount() {
		logger.info("Inside  @class"+this.getClass().getName()+"@Method :draftBannerCount()");
		return trbbannerViewService.draftBannerCount();
	}
	
	
}

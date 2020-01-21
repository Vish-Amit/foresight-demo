package com.inn.foresight.module.tribe.rest.impl;

import java.io.InputStream;
import java.util.ArrayList;
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
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.apache.cxf.jaxrs.ext.search.SearchContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonView;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.rest.AbstractCXFRestService;
import com.inn.core.generic.service.IGenericService;
import com.inn.core.generic.utils.Views;
import com.inn.foresight.module.tribe.model.TRBBanner;
import com.inn.foresight.module.tribe.model.TRBBanner.BannerStatus;
import com.inn.foresight.module.tribe.service.ITRBBannerService;
import com.inn.foresight.module.tribe.utils.BannerContentWrapper;

/**
 * Rest.
 */

@Path("/TRBBanner")
@Produces("application/json")
@Consumes("application/json")
@Service("TRBBannerRestImpl")
public class TRBBannerRestImpl extends AbstractCXFRestService<Integer, TRBBanner> {
	
	/** The logger. */
	private Logger logger = LogManager.getLogger(TRBBannerRestImpl.class);

	/**
	 * Instantiates a new TRB banner rest impl.
	 */
	public TRBBannerRestImpl() {
		super(TRBBanner.class);
	}

	/** The trbbanner service. */
	@Autowired
	private ITRBBannerService trbbannerService;

	/** The context. */
	@Context
	private SearchContext context;

	/**
	 * Find by id.
	 *
	 * @param id the id
	 * @return the TRB banner
	 * @throws RestException the rest exception
	 */
	@Path("findById/{id}")
	@GET
	public TRBBanner findById(@QueryParam("id") Integer id) throws RestException {
		logger.info("Inside  @class"+this.getClass().getName()+"@Method :findById @Param: id "+id);
		return trbbannerService.findById(id);
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
	@JsonView(value = { Views.BasicView.class })
	public List<TRBBanner> search(@Context SearchContext ctx,@QueryParam("llimit") Integer lowerLimit, @QueryParam("ulimit") Integer upperLimit,
			@QueryParam("orderBy") String orderBy, @QueryParam("orderType") String orderType) throws RestException {
		logger.info("Inside  @class"+this.getClass().getName()+"@Method :search @Param: lowerLimit " + lowerLimit
				+ " ,upperLimit " + upperLimit + " ,orderBy " + orderBy + " ,orderType " + orderType);
		return trbbannerService.searchWithLimitAndOrderBy(ctx, upperLimit, lowerLimit, orderBy, orderType);
	}

	/**
	 * Search for tab.
	 *
	 * @param lowerLimit the lower limit
	 * @param upperLimit the upper limit
	 * @param orderBy the order by
	 * @param orderType the order type
	 * @return the list
	 * @throws RestException the rest exception
	 */
	@GET
	@Path("searchForTab")
	@Produces("application/json")
	public List<TRBBanner> searchForTab(@QueryParam("llimit") Integer lowerLimit,
			@QueryParam("ulimit") Integer upperLimit, @QueryParam("orderBy") String orderBy,
			@QueryParam("orderType") String orderType) throws RestException {
		logger.info("Inside  @class"+this.getClass().getName()+"@Method :searchForTab @Param: lowerLimit " + lowerLimit
				+ " ,upperLimit " + upperLimit + " ,orderBy " + orderBy + " ,orderType " + orderType);
		return trbbannerService.searchWithLimitAndOrderBy(context, upperLimit, lowerLimit, orderBy, orderType);
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
		return trbbannerService.getSearchRecordCount(context);
	}

	/**
	 * Creates the.
	 *
	 * @param trbbanner the trbbanner
	 * @return the TRB banner
	 * @throws RestException the rest exception
	 */
	@POST
	@Path("create")
	public TRBBanner create(@Valid TRBBanner trbbanner) throws RestException {
		logger.info("Inside  @class"+this.getClass().getName()+"@Method :create @Param: trbbanner " + trbbanner.getId());
		TRBBanner newDocComment = trbbannerService.create(trbbanner);
		return newDocComment;
	}

	/**
	 * Update.
	 *
	 * @param trbbanner the trbbanner
	 * @return the TRB banner
	 * @throws RestException the rest exception
	 */
	@Override
	@POST
	@Path("update")
	public TRBBanner update(@Valid TRBBanner trbbanner) throws RestException {
		logger.info("Inside  @class"+this.getClass().getName()+"@Method :update @Param: trbbanner " + trbbanner.getId());
		TRBBanner newDocComment = trbbannerService.update(trbbanner);
		return newDocComment;
	}

	
	@POST
	@Path("updateBannerContent")
	public List<TRBBanner> updateBannerContent(List<BannerContentWrapper>  bannerContentWrapper) throws RestException {
		logger.info("Inside  @class"+this.getClass().getName()+"@Method :updateBannerContent @Param: updateBannerContent " + bannerContentWrapper.toString());
		return trbbannerService.updateBannerContent(bannerContentWrapper);
	}
	
	@POST
	@Path("updateBannerContentById")
	public TRBBanner updateBannerContentById(BannerContentWrapper  bannerContentWrapper) throws RestException {
		logger.info("Inside  @class"+this.getClass().getName()+"@Method :updateBannerContent @Param: updateBannerContent " + bannerContentWrapper.toString());
		List<BannerContentWrapper> bannerWrapper = new ArrayList<BannerContentWrapper>();
		bannerWrapper.add(bannerContentWrapper);
		List<TRBBanner> updateBannerContent = trbbannerService.updateBannerContent(bannerWrapper);
		if(updateBannerContent !=null && updateBannerContent.isEmpty())
			 return updateBannerContent.get(0);
		 else
			 return null;
	}
	
	/**
	 * Removes the.
	 *
	 * @param trbbanner the trbbanner
	 * @return true, if successful
	 * @throws RestException the rest exception
	 */
	@Override
	@Path("delete")
	public boolean remove(TRBBanner trbbanner) throws RestException {
		logger.info("Inside  @class"+this.getClass().getName()+"@Method :remove @Param:  "+trbbanner.getId());
		trbbannerService.remove(trbbanner);
		return true;
	}

	/**
	 * Removes the by id.
	 *
	 * @param primaryKey the primary key
	 * @throws RestException the rest exception
	 */
	@Override
	@POST
	@Path("delete/{id}")
	public void removeById(@PathParam("id") Integer primaryKey) throws RestException {
		logger.info("Inside  @class"+this.getClass().getName()+"@Method :removeById @Param: primaryKey " + primaryKey);
		TRBBanner trbbanner = trbbannerService.findById(primaryKey);
		if (trbbanner != null) {
			trbbannerService.removeById(primaryKey);
		}

	}

	/**
	 * Gets the service.
	 *
	 * @return the service
	 */
	@Override
	public IGenericService<Integer, TRBBanner> getService() {
		logger.info("Inside  @class"+this.getClass().getName()+"@Method :getService ");
		return trbbannerService;
	}

	/**
	 * Gets the search context.
	 *
	 * @return the search context
	 */
	@Override
	public SearchContext getSearchContext() {
		logger.info("Inside  @class"+this.getClass().getName()+"@Method :getSearchContext ");
		return this.context;
	}

	/**
	 * Search.
	 *
	 * @param entity the entity
	 * @return the list
	 */
	@Override
	public List<TRBBanner> search(TRBBanner entity) {
		logger.info("Inside  @class"+this.getClass().getName()+"@Method search");
		return null;
	}

	/**
	 * Find all.
	 *
	 * @return the list
	 */
	@Override
	public List<TRBBanner> findAll() {
		logger.info("Inside  @class"+this.getClass().getName()+"@Method findall()");
		return null;
	}

	/**
	 * Creates the banner.
	 *
	 * @param bannerStream the banner stream
	 * @param fileStream the file stream
	 * @param banner the banner
	 * @return the TRB banner
	 * @throws RestException the rest exception
	 * @throws DaoException 
	 */
	@POST
	@Consumes("multipart/form-data")
	@Path("/createBanner")
	public TRBBanner createBanner(@Multipart(value = "bannerImage") InputStream bannerStream,
			@Multipart(value = "bannerFile") InputStream fileStream, @Multipart(value = "banner") String banner)
			throws RestException, DaoException {
		logger.info("Inside  @class"+this.getClass().getName()+"@Method :createBanner()");
		return trbbannerService.createBanner(bannerStream, fileStream, banner);
	}

	/**
	 * Creates the banner from tab.
	 *
	 * @param bannerStream the banner stream
	 * @param fileStream the file stream
	 * @param banner the banner
	 * @return the TRB banner
	 * @throws RestException the rest exception
	 * @throws DaoException 
	 */
	@POST
	@Consumes("multipart/form-data")
	@Path("/createBannerFromTab")
	public TRBBanner createBannerFromTab(@Multipart(value = "bannerImage") InputStream bannerStream,
			@Multipart(value = "bannerFile") InputStream fileStream, @Multipart(value = "banner") String banner)
			throws RestException, DaoException {
		logger.info("Inside  @class"+this.getClass().getName()+"@Method :createBannerFromTab()");
		return trbbannerService.createBanner(bannerStream, fileStream, banner);
	}

	/**
	 * Update banner.
	 *
	 * @param bannerStream the banner stream
	 * @param fileStream the file stream
	 * @param banner the banner
	 * @return the TRB banner
	 * @throws RestException the rest exception
	 * @throws DaoException 
	 */
	@POST
	@Consumes("multipart/form-data")
	@Path("/updateBanner")
	public TRBBanner updateBanner(@Multipart(value = "bannerImage") InputStream bannerStream,
			@Multipart(value = "bannerFile") InputStream fileStream, @Multipart(value = "banner") String banner)
			throws RestException, DaoException {
		logger.info("Inside  @class"+this.getClass().getName()+"@Method :updateBanner()");
		return trbbannerService.updateBanner(bannerStream, fileStream, banner);
	}

	/**
	 * Update banner from tab.
	 *
	 * @param bannerStream the banner stream
	 * @param fileStream the file stream
	 * @param banner the banner
	 * @return the TRB banner
	 * @throws RestException the rest exception
	 * @throws DaoException 
	 */
	@POST
	@Consumes("multipart/form-data")
	@Path("/updateBannerFromTab")
	public TRBBanner updateBannerFromTab(@Multipart(value = "bannerImage") InputStream bannerStream,
			@Multipart(value = "bannerFile") InputStream fileStream, @Multipart(value = "banner") String banner)
			throws RestException, DaoException {
		logger.info("Inside  @class"+this.getClass().getName()+"@Method :updateBannerFromTab()");
		return trbbannerService.updateBanner(bannerStream, fileStream, banner);
	}

	/**
	 * Update banner status.
	 *
	 * @param bannerId the banner id
	 * @param status the status
	 * @return the string
	 * @throws RestException the rest exception
	 * @throws DaoException 
	 */
	@POST
	@Path("/updateBannerStatus/{bannerId}/{status}")
	public String updateBannerStatus(@PathParam("bannerId") Integer bannerId, @PathParam("status") BannerStatus status)
			throws RestException, DaoException {
		logger.info("Inside  @class"+this.getClass().getName()+"@Method :updateBannerStatus() @param");
		return trbbannerService.updateBannerStatus(bannerId, status);
	}

	/**
	 * Update banner status for tab.
	 *
	 * @param bannerId the banner id
	 * @param status the status
	 * @return the string
	 * @throws RestException the rest exception
	 * @throws DaoException 
	 */
	@POST
	@Path("/updateBannerStatusForTab/{bannerId}/{status}")
	public String updateBannerStatusForTab(@PathParam("bannerId") Integer bannerId,
			@PathParam("status") BannerStatus status) throws RestException, DaoException {
		logger.info("Inside  @class"+this.getClass().getName()+"@Method :updateBannerStatusForTab() @param");
		return trbbannerService.updateBannerStatus(bannerId, status);
	}

	/**
	 * Delete banner.
	 *
	 * @param bannerId the banner id
	 * @return the string
	 * @throws RestException the rest exception
	 */
	@POST
	@Path("/deleteBanner/{bannerId}")
	public String deleteBanner(@PathParam("bannerId") Integer bannerId) throws RestException {
		logger.info("Inside  @class"+this.getClass().getName()+"@Method :deleteBanner()");
		return trbbannerService.deleteBanner(bannerId);
	}

	/**
	 * Delete banner from tab.
	 *
	 * @param bannerId the banner id
	 * @return the string
	 * @throws RestException the rest exception
	 */
	@POST
	@Path("/deleteBannerFromTab/{bannerId}")
	public String deleteBannerFromTab(@PathParam("bannerId") Integer bannerId) throws RestException {
		logger.info("Inside  @class"+this.getClass().getName()+"@Method :deleteBannerFromTab()");
		return trbbannerService.deleteBanner(bannerId);
	}

	/**
	 * Gets the existed banner ids.
	 *
	 * @return the existed banner ids
	 */
	@GET
	@Path("/getExistedBannerIds")
	public List<Integer> getExistedBannerIds() {
		logger.info("Inside  @class"+this.getClass().getName()+"@Method :getExistedBannerIds()");
		return trbbannerService.getExistedBannerIds();
	}
	
	@GET
	@Path("/getBannerCountByStatus")
	public Response getBannerCountByStatus() throws JSONException {
		logger.info("Inside  @class"+this.getClass().getName()+"@Method :getBannerCountByStatus()");
		return Response.ok(trbbannerService.getBannerCountByStatus()).build();
	}
	
}

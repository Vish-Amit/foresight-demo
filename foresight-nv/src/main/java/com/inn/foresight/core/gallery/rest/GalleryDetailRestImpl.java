package com.inn.foresight.core.gallery.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
import com.inn.foresight.core.gallery.model.GalleryDetail;
import com.inn.foresight.core.gallery.service.IGalleryDetailService;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.product.audit.utils.AuditActionName;
import com.inn.product.audit.utils.AuditActionType;
import com.inn.product.audit.utils.Auditable;

@Path("GalleryDetail")
@Produces("application/json")
@Consumes("application/json")
@Service("GalleryDetailRestImpl")
public class GalleryDetailRestImpl extends AbstractCXFRestService<Integer, GalleryDetail> {

	private Logger logger = LogManager.getLogger(GalleryDetailRestImpl.class);

	@Autowired
	private IGalleryDetailService iGalleryDetailService;

	/** The context. */
	@Context
	private SearchContext context;

	public GalleryDetailRestImpl() {
		super(GalleryDetail.class);
	}

	@Override
	public List<GalleryDetail> search(GalleryDetail entity) {
		return new ArrayList<>();

	}

	@Override
	public GalleryDetail findById(@NotNull Integer primaryKey) {
		return null;
	}

	@Override
	public List<GalleryDetail> findAll() {
		return new ArrayList<>();
	}

	@Override
	public GalleryDetail create(@Valid GalleryDetail anEntity) {
		return null;
	}

	@Override
	public GalleryDetail update(@Valid GalleryDetail anEntity) {
		return null;
	}

	@Override
	public boolean remove(@Valid GalleryDetail anEntity) {
		return false;
	}

	@Override
	public void removeById(@NotNull Integer primaryKey) {
		// blank method
	}

	@Override
	public IGenericService<Integer, GalleryDetail> getService() {
		return null;
	}

	@Override
	public SearchContext getSearchContext() {
		return null;
	}

	/**
	 * Search.
	 *
	 * @param lowerLimit the lower limit
	 * @param upperLimit the upper limit
	 * @return the list
	 * @throws RestException the rest exception
	 * @throws RestException the rest exception
	 */
	@GET
	@Path("searchwithinlimit")
	@Produces("application/json")
	public List<GalleryDetail> search(@QueryParam("llimit") Integer lowerLimit,
			@QueryParam("ulimit") Integer upperLimit) {
		logger.info("Inside search with limit method");
		return iGalleryDetailService.searchWithLimit(context, upperLimit, lowerLimit);
	}

	@GET
	@Path("getGalleryDetailDataList")
	@Produces("application/json")
	public List<GalleryDetail> getGallerySmileDataList(@QueryParam("llimit") Integer llimit,
			@QueryParam("ulimit") Integer ulimit) {
		logger.info("Going to fetch Gallery and Smile Data List");
		return iGalleryDetailService.getGallerySmileDataList(llimit, ulimit);
	}

	@POST
	@Path("createGalleryDetail")
	@Produces("application/json")
	@Auditable(actionType = AuditActionType.CREATE, actionName = AuditActionName.CREATE_GALLERY)
	public Map<String, String> createGallerySmile(GalleryDetail gallery) {
		logger.info("Going to create Gallery and Smile");
		if (gallery != null) {
			return iGalleryDetailService.createGallerySmile(gallery);
		} else {
			throw new RestException(ForesightConstants.INVALID_PARAMETER);
		}
	}

	@POST
	@Path("updateGalleryDetail")
	@Produces("application/json")
	@Auditable(actionType = AuditActionType.UPDATE, actionName = AuditActionName.UPDATE_GALLERY)
	public Map<String, String> updateGallerySmile(GalleryDetail gallery) {
		logger.info("Going to update Gallery and Smile Data List");
		if (gallery != null) {
			return iGalleryDetailService.updateGallerySmile(gallery);
		} else {
			throw new RestException(ForesightConstants.INVALID_PARAMETER);
		}
	}

	@GET
	@Path("getGalleryDetailCount")
	@Produces("application/json")
	public Map<String, Long> getGallerySmileCount() {
		logger.info("Going to count Gallery and Smile");
		return iGalleryDetailService.getGallerySmileCount();
	}

	@GET
	@Path("getsearchByNameGalleryCount")
	@Produces("application/json")
	public Map<String, Long> getGallerySmileCountBySearchName(@QueryParam("name") String name) {
		logger.info("Going to count Gallery and Smile by searching through name");
		return iGalleryDetailService.getGallerySmileCountBySearchName(name);
	}

	@GET
	@Path("getGalleryDetailById")
	@Produces("application/json")
	public GalleryDetail getGallerySmileById(@QueryParam("id") Integer id) {
		logger.info("Going to fetch Gallery and Smile Data by Id");
		if (id != null) {
			return iGalleryDetailService.getGallerySmileById(id);
		} else {
			throw new RestException(ForesightConstants.INVALID_PARAMETER);
		}
	}

	@GET
	@Path("searchByName")
	@Produces("application/json")
	public List<GalleryDetail> searchByName(@QueryParam("name") String name, @QueryParam("llimit") Integer llimit,
			@QueryParam("ulimit") Integer ulimit) {
		logger.info("Inside searchByname method");
		if (name != null) {
			return iGalleryDetailService.searchByName(name, llimit, ulimit);
		} else {
			throw new RestException(ForesightConstants.INVALID_PARAMETER);
		}
	}

	@GET
	@Path("enable/disable gallery status")
	@Auditable(actionType = AuditActionType.ENABLE, actionName = AuditActionName.UPDATE_GALLERY)
	public Map<String, String> enable(@QueryParam("id") GalleryDetail id) {
		logger.info("Inside enable method");
		if (id == null)
			throw new RestException(ForesightConstants.INVALID_PARAMETER);
		else
			return updateGallerySmile(id);
	}

	@POST
	@Path("updateGalleryStatus")
	public Map<String, String> updateGallerySmileStatus(@QueryParam("enabled") Boolean enabled, List<Integer> id) {
		logger.info("Going to update Gallery and Smile status for id {} ", id, enabled);
		if (id != null && enabled != null) {
			return iGalleryDetailService.updateGalleryStatus(id, enabled);
		} else {
			throw new RestException("Id can not be null");
		}
	}

	@GET
	@Path("getGalleryDetailForVisulisation")
	public List<GalleryDetail> getGallerySmileForVisualization(@QueryParam("SWLng") Double swLon,
			@QueryParam("SWLat") Double swLat, @QueryParam("NELng") Double neLon, @QueryParam("NELat") Double neLat) {
		logger.info("Going to fetch Gallery and Smile for visualization SWLat {} SWLon {} NELat {} NELon {}", swLat,
				swLon, neLat, neLon);
		return iGalleryDetailService.getGallerySmileForVisualization(swLon, swLat, neLon, neLat);
	}

	@GET
	@Path("getGalleryManagerByManagerType/{managerType}")
	public String getGallerySmileManagerByManagerType(@PathParam("managerType") String managerType) {
		logger.info("Going to fetch Gallery and Smile Manager By Manager Type {} ", managerType);
		return iGalleryDetailService.getGallerySmileManagerByManagerType(managerType);
	}
}

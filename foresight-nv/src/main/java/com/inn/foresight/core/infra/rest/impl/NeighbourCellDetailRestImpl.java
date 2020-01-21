package com.inn.foresight.core.infra.rest.impl;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.apache.commons.collections.CollectionUtils;
import org.apache.cxf.jaxrs.ext.search.SearchContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.rest.AbstractCXFRestService;
import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.model.NeighbourCellDetail;
import com.inn.foresight.core.infra.service.INeighbourCellDetailService;

@Path("NeighbourCellDetail")
@Produces("application/json")
@Consumes("application/json")
@Service("NeighbourCellDetailRestImpl")
public class NeighbourCellDetailRestImpl extends AbstractCXFRestService<Integer, NeighbourCellDetail> {

	/** The logger. */
	private Logger logger = LogManager.getLogger(NeighbourCellDetailRestImpl.class);
	
	public NeighbourCellDetailRestImpl() {
		super(NeighbourCellDetail.class);
	}
	
	@Autowired
	private INeighbourCellDetailService neighbourCellDetail;

	/** The context. */
	@Context
	private SearchContext context;

	@Override
	public SearchContext getSearchContext() {
		// TODO Auto-generated method stub
		return context;
	}

	
	@Override
	public List<NeighbourCellDetail> search(NeighbourCellDetail entity) {
		// TODO Auto-generated method stub
		return neighbourCellDetail.search(entity);
	}

	@GET
	@Path("findById")
	@Override
	public NeighbourCellDetail findById(@NotNull Integer primaryKey) {
		// TODO Auto-generated method stub
		return neighbourCellDetail.findById(primaryKey);
	}

	@GET
	@Path("findAll")
	@Override
	public List<NeighbourCellDetail> findAll() {
		// TODO Auto-generated method stub
		return neighbourCellDetail.findAll();
	}

	@Override
	public NeighbourCellDetail create(@Valid NeighbourCellDetail anEntity) {
		// TODO Auto-generated method stub
		return neighbourCellDetail.create(anEntity);
	}

	@Override
	public NeighbourCellDetail update(@Valid NeighbourCellDetail anEntity) {
		// TODO Auto-generated method stub
		return neighbourCellDetail.update(anEntity);
	}

	
	@Override
	public IGenericService<Integer, NeighbourCellDetail> getService() {
		return neighbourCellDetail;
	}


	@Override
	public boolean remove(@Valid NeighbourCellDetail anEntity) {
		// TODO Auto-generated method stub
		neighbourCellDetail.remove(anEntity);
		return true;
	}


	@Override
	public void removeById(@NotNull Integer primaryKey) {
		// TODO Auto-generated method stub
		neighbourCellDetail.removeById(primaryKey);
	}
	
	/**
	 * Search NeighbourCell.
	 *
	 * @param lowerLimit the lower limit
	 * @param upperLimit the upper limit
	 * @param orderBy the order by
	 * @param orderType the order type
	 * @return the list
	 * @throws RestException the rest exception
	 */
	@GET
	@Path("searchNeighbourCell")
	public List<NeighbourCellDetail> searchNeighbourCell(@QueryParam("llimit") Integer lowerLimit, @QueryParam("ulimit") Integer upperLimit, @QueryParam("orderBy") String orderBy,
			@QueryParam("orderType") String orderType) {
		if (!Utils.isValidParameter(orderBy,orderType)) {
			throw new RestException(ForesightConstants.INVALID_PARAMETERS);
		}
		logger.info("Going to search NeighbourCell @lowerLimit: {} @upperLimit: {} @orderBy: {} @orderType: {} ",lowerLimit,upperLimit,orderBy,orderType);
		return neighbourCellDetail.searchWithLimitAndOrderBy(context, upperLimit, lowerLimit, orderBy, orderType);
	}
	

	
	@POST
	@Path("getNeighbourCellDetailsForSourceCells")
	public Map<String, List<NeighbourCellDetail>> getNeighbourCellDetailsForSourceCells(List<String> cellName, @QueryParam("weekNo") Integer weekNo) {
		logger.info("@Method searchNeighbourCell, Input parameter : cellNames {}, weekno {}",cellName, weekNo);
		if (CollectionUtils.isEmpty(cellName) || weekNo==null) {
			throw new RestException(ForesightConstants.INVALID_PARAMETERS);
		}
		return neighbourCellDetail.getNeighbourCellDetailsForSourceCells(cellName, weekNo);
	}
	@GET
	@Path("getNeighbourCellDataForSourceCell")
	public List<Map> getNeighbourCellDataForSourceCell(@QueryParam("sourceSiteId") String sourceSiteId, @QueryParam("sourceCellId") Integer sourceCellId, @QueryParam("sourceFrequencyBand") String sourceFrequencyBand,@QueryParam("vendor") String vendor, @QueryParam("domain") String domain) {
		logger.info("going to get Neighbour Cells Detail Data for SiteID: {} and CellNum: {} ",sourceSiteId, sourceCellId);
        if(vendor != null && domain != null) {
        if(sourceSiteId != null) {
		if(sourceFrequencyBand != null) {
			return neighbourCellDetail.getNeighbourCellDetails(sourceSiteId, sourceCellId,sourceFrequencyBand,vendor,domain);
		}else
			throw new RestException("Invalid Frequency.");
       
	    }else
			throw new RestException("Invalid SiteID.");
        }else
			throw new RestException("Invalid Vendor and Domain");
	}
}

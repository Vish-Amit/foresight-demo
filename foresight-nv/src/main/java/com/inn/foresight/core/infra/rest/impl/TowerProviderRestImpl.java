package com.inn.foresight.core.infra.rest.impl;


import java.util.ArrayList;
import java.util.HashMap;
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
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.ext.search.SearchContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.rest.AbstractCXFRestService;
import com.inn.core.generic.service.IGenericService;
import com.inn.core.generic.utils.ExceptionUtil;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.service.ITowerProviderService;
import com.inn.foresight.core.infra.wrapper.KPISummaryDataWrapper;
import com.inn.foresight.core.infra.wrapper.TowerProviderWrapper;

@Path("TowerProvider")
@Produces("application/json")
@Consumes("application/json")
public class TowerProviderRestImpl extends AbstractCXFRestService<Integer, Object> {

	public TowerProviderRestImpl() {
		super(Object.class);
	}

	private Logger logger = LogManager.getLogger(TowerProviderRestImpl.class);
	
	private static final String TOWER_PROVIDER_REST_IMPL = "TowerProviderRestImpl";

	@Autowired
	private ITowerProviderService iTowerProviderService;

	@Override
	public List<Object> search(Object entity) {
		return null;
	}

	@Override
	public Object findById(@NotNull Integer primaryKey) {
		return null;
	}

	@Override
	public List<Object> findAll() {
		return null;
	}

	@Override
	public Object create(@Valid Object anEntity) {
		return null;
	}

	@Override
	public Object update(@Valid Object anEntity) {
		return null;
	}

	@Override
	public boolean remove(@Valid Object anEntity) {
		return false;
	}

	@Override
	public void removeById(@NotNull Integer primaryKey) {
		//
	}

	@Override
	public IGenericService<Integer, Object> getService() {
		return null;
	}

	@Override
	public SearchContext getSearchContext() {
		return null;
	}

	@POST
	@Path("getTowerDetailsForProviders")
	public Response getTowerDetailsForProviders(@QueryParam("SWLng") Double southWestLong, @QueryParam("SWLat") Double southWestLat, @QueryParam("NELng") Double northEastLong,
			@QueryParam("NELat") Double northEastLat, @QueryParam("zoomLevel") Integer zoomLevel, List<String> name, @QueryParam("displayTowers") Integer displayTowers) {
		logger.info("Going to get Tower Provider Details For Providers.");
		try {
			if (southWestLong != null && southWestLat != null && northEastLong != null && northEastLat != null) {
				return Response.ok(new Gson().toJson(iTowerProviderService.getTowerDetailsForProviders(southWestLong, southWestLat, northEastLong, northEastLat, name, displayTowers, zoomLevel))).build();
			} else {
				throw new RestException(ForesightConstants.INVALID_PARAMETERS);
			}
		} catch (RestException restException) {
			throw new RestException(ExceptionUtil.generateExceptionCode("Rest", TOWER_PROVIDER_REST_IMPL, restException));
		} catch (Exception exception) {
			logger.error("Unable to get TowerDetails For Providers Exception {} ", Utils.getStackTrace(exception));
			throw new RestException(exception.getMessage());
		}
	}

	@POST
	@Path("getTowerCountsForProviders")
	public Map<String, Long> getTowersCountsForKPI(KPISummaryDataWrapper filterConfiguration, @QueryParam("SWLng") Double southWestLong, @QueryParam("SWLat") Double southWestLat,
			@QueryParam("NELng") Double northEastLong, @QueryParam("NELat") Double northEastLat, @QueryParam("zoomLevel") Integer zoomLevel) {
		logger.info("Going to get Tower Counts for KPI .");
		try {
			if (filterConfiguration != null && filterConfiguration.getPolyList() != null && !filterConfiguration.getPolyList().isEmpty()) {
				return iTowerProviderService.getTowerCountsInsidePolygon(filterConfiguration, zoomLevel);
			}
			if (filterConfiguration != null && filterConfiguration.getGeographyList() != null && !filterConfiguration.getGeographyList().isEmpty()) {
				return iTowerProviderService.getTowerCountsForGeographies(filterConfiguration);
			}
			if (southWestLong != null && southWestLat != null && northEastLong != null && northEastLat != null && zoomLevel != null && filterConfiguration != null) {
				return iTowerProviderService.getTowerCountsForProviders(filterConfiguration, southWestLong, southWestLat, northEastLong, northEastLat, zoomLevel);
			} else {
				throw new RestException(ForesightConstants.INVALID_PARAMETERS);
			}
		} catch (RestException restException) {
			throw new RestException(ExceptionUtil.generateExceptionCode("Rest", TOWER_PROVIDER_REST_IMPL, restException));
		} catch (Exception exception) {
			logger.error("Unable to get getTowersCountsForKPI Exception {} ", Utils.getStackTrace(exception));
			throw new RestException(exception.getMessage());
		}
	}
	
	@GET
	@Path("getTowerDetailReport")
	public Map<String, String> getTowerDetailReport(@QueryParam("towerId") String towerId) {
		logger.info("Into the method for creating the report for Tower {}",towerId);
		Map<String, String> fileMap = new HashMap<>();
		try {
			if(towerId != null) {
				fileMap = iTowerProviderService.getTowerDetailReport(towerId);
			}
			else {
				throw new RestException("Invalid Towername.");
			}
		} catch (RestException e) {
			throw new RestException(ExceptionUtil.generateExceptionCode("Rest", TOWER_PROVIDER_REST_IMPL, e));
		} catch (Exception e) {
			logger.error("Error in creating the report {} ", e.getStackTrace());
		}
		return fileMap;
	}
	
	@GET
	@Path("getAllTowerProviderDetails")
	public List<TowerProviderWrapper> getAllTowerProviderDetails() {
		logger.info("Into the method for getting tower provider list.");
		List<TowerProviderWrapper> towerProviderList = new ArrayList<>();
		try {
			towerProviderList = iTowerProviderService.getAllTowerProviderDetails();
		} catch (RestException e) {
			throw new RestException(ExceptionUtil.generateExceptionCode("Rest", TOWER_PROVIDER_REST_IMPL, e));
		} catch (Exception e) {
			logger.error("Error in getting name of tower provider.Exception: {} ", e.getStackTrace());
		}
		return towerProviderList;
	}
}

package com.inn.foresight.module.nv.dashboard.rest.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.inn.foresight.module.nv.NVConstant;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.cxf.jaxrs.ext.search.SearchContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.rest.AbstractCXFRestService;
import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.module.nv.NVConstant;
import com.inn.foresight.module.nv.dashboard.model.NVDashboard;
import com.inn.foresight.module.nv.dashboard.service.INVDashboardService;
import com.inn.foresight.module.nv.dashboard.utils.NVDashboardConstants;
import com.inn.foresight.module.nv.dashboard.wrapper.NVDistributionWrapper;
import com.inn.foresight.module.nv.dashboard.wrapper.TopGeographyWrapper;
import com.inn.foresight.module.nv.dashboard.wrapper.UserCountWrapper;


/** The Class NVDashboardRestImpl. */
@Path("/NVDashboard")
@Produces(ForesightConstants.APPLICATION_SLASH_JSON)
@Consumes(ForesightConstants.APPLICATION_SLASH_JSON)
@Service("NVDashboardRestImpl")
public class NVDashboardRestImpl extends
		AbstractCXFRestService<Integer, NVDashboard> {

	/** The logger. */
	private Logger logger = LogManager.getLogger(NVDashboardRestImpl.class);

	/** The i NV dashboardservice. */
	@Autowired
	private INVDashboardService iNVDashboardservice;

	/** The context. */
	@Context
	private SearchContext context;

	/** Instantiates a new NV dashboard rest impl. */
	public NVDashboardRestImpl() {
		super(NVDashboard.class);
	}

	/**
	 * Search.
	 *
	 * @param entity the entity
	 * @return the list
	 * @throws RestException the rest exception
	 */
	@Override
	public List<NVDashboard> search(NVDashboard entity) {
		try {
			return iNVDashboardservice.search(entity);
		} catch (RestException e) {
			logger.info("Exception in search method");
		}
		return Collections.emptyList();
	}

	/**
	 * Find by id.
	 *
	 * @param primaryKey the primary key
	 * @return the NV dashboard
	 * @throws RestException the rest exception
	 */
	@Override
	public NVDashboard findById(Integer primaryKey) {
		try {
			return iNVDashboardservice.findById(primaryKey);
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
	public List<NVDashboard> findAll() {
		try {
			return iNVDashboardservice.findAll();
		} catch (RestException e) {
			logger.info("Exception in findAll method");
		}
		return Collections.emptyList();
	}

	/**
	 * Creates the.
	 *
	 * @param anEntity the an entity
	 * @return the NV dashboard
	 * @throws RestException the rest exception
	 */
	@Override
	public NVDashboard create(NVDashboard anEntity) {
		try {
			return iNVDashboardservice.create(anEntity);
		} catch (RestException e) {
			logger.info("Exception in create method");
		}
		return null;
	}

	/**
	 * Update.
	 *
	 * @param anEntity the an entity
	 * @return the NV dashboard
	 * @throws RestException the rest exception
	 */
	@Override
	public NVDashboard update(NVDashboard anEntity) {
		try {
			return iNVDashboardservice.update(anEntity);
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
	public boolean remove(NVDashboard anEntity) {
		try {
			iNVDashboardservice.remove(anEntity);
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
			iNVDashboardservice.removeById(primaryKey);
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
	public IGenericService<Integer, NVDashboard> getService() {
		return iNVDashboardservice;
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
	 * This method is used to fetch data for different chart.
	 *
	 * @param kpiList the kpi list
	 * @param advancedSearchId the advanced search id
	 * @param endDate the end date
	 * @param callType the call type
	 * @param band the band
	 * @param technology the technology
	 * @param operator the operator
	 * @param type the type
	 * @param name the name
	 * @return Map<String,Map<String,String>>
	 */
	@POST
	@Produces(ForesightConstants.APPLICATION_SLASH_JSON)
	@Path("getNvDashboardDataByDateAndLocation")
	public Response getNvDashboardDataByDateAndLocation(List<String> kpiList,
			@QueryParam(NVDashboardConstants.ADVANCESEARCH_ID) Integer advancedSearchId,
			@QueryParam(NVDashboardConstants.END_DATE) String endDate,
			@QueryParam(NVDashboardConstants.CALL_TYPE) String callType,
			@QueryParam(NVDashboardConstants.BAND) String band,
			@QueryParam(NVDashboardConstants.TECHNOLOGY) String technology,
			@QueryParam(NVDashboardConstants.OPERATOR) String operator,
			@QueryParam(NVDashboardConstants.TYPE) String type,
			@QueryParam(NVDashboardConstants.NAME) String name,
            @QueryParam(NVDashboardConstants.COUNTRY) String country) {
		logger.info("In getNvDashboardDataByDateAndLocation advancedSearchId {} ,endDate {},callType {},band {},technology {},operator {}",advancedSearchId, endDate, callType, band, technology, operator);
		Map<String, Map<String, String>> nvDashboardDataByDateAndLocation = null;
		try {
			nvDashboardDataByDateAndLocation = iNVDashboardservice.getNvDashboardDataByDateAndLocation(advancedSearchId,endDate, callType, band, technology, operator, kpiList, type, name,country);
		} catch (Exception e) {
			logger.error("Error while getting dashboard data and message is {}", e.getMessage());
		}
		if (MapUtils.isNotEmpty(nvDashboardDataByDateAndLocation)) {
			return Response.ok(nvDashboardDataByDateAndLocation).build();
		} else {
			return Response.ok(NVConstant.DATA_NOT_FOUND).build();
		}
	}

	/**
	 * This method is used to fetch unique user count data.
	 *
	 * @param advancedSearchId the advanced search id
	 * @param endDate the end date
	 * @param callType the call type
	 * @param band the band
	 * @param technology the technology
	 * @param operator the operator
	 * @return the nv dashboard user count by date and location
	 */
	@GET
	@Produces(ForesightConstants.APPLICATION_SLASH_JSON)
	@Path("getNvDashboardUserCountByDateAndLocation")
	public Response getNvDashboardUserCountByDateAndLocation(
			@QueryParam(NVDashboardConstants.ADVANCESEARCH_ID) Integer advancedSearchId,
			@QueryParam(NVDashboardConstants.END_DATE) String endDate,
			@QueryParam(NVDashboardConstants.CALL_TYPE) String callType,
			@QueryParam(NVDashboardConstants.BAND) String band,
			@QueryParam(NVDashboardConstants.TECHNOLOGY) String technology,
			@QueryParam(NVDashboardConstants.OPERATOR) String operator,
			@QueryParam(NVDashboardConstants.COUNTRY) String country) {
		logger.info("In getAllUserCount advancedSearchId {} ,endDate {},callType {},band {},technology {},operator {},country{}",
				advancedSearchId, endDate, callType, band, technology, operator, country);

		Map<String, UserCountWrapper> userCountMap = null;
		try {
			userCountMap = iNVDashboardservice.getNvDashboardUserCountByDateAndLocation(advancedSearchId, endDate,
					callType, band, technology, operator,country);
		} catch (RestException e) {
			logger.error("Error while getting usercount data and message is {}", e.getMessage());
		}
		if (MapUtils.isNotEmpty(userCountMap)) {
			return Response.ok(userCountMap).build();
		} else {
			return Response.ok(NVConstant.DATA_NOT_FOUND).build();
		}
	}


	/**
	 * This method is used to get distribution of all operator or iOS or Android or Devices depends on
	 * distribution type(operator,iOS,Android,Device) and call type (Daily,Weekly,Monthly).
	 *
	 * @param advancedSearchId the advanced search id
	 * @param endDate the end date
	 * @param callType the call type
	 * @param band the band
	 * @param technology the technology
	 * @param operator the operator
	 * @param distType the dist type
	 * @return the NV distribution by date and location
	 */
	@GET
	@Produces(ForesightConstants.APPLICATION_SLASH_JSON)
	@Path("getNVDistributionByDateAndLocation")
	public Response getNVDistributionByDateAndLocation(
			@QueryParam(NVDashboardConstants.KPI) String kpi,
			@QueryParam(NVDashboardConstants.ADVANCESEARCH_ID) Integer advancedSearchId,
			@QueryParam(NVDashboardConstants.END_DATE) String endDate,
			@QueryParam(NVDashboardConstants.CALL_TYPE) String callType,
			@QueryParam(NVDashboardConstants.BAND) String band,
			@QueryParam(NVDashboardConstants.TECHNOLOGY) String technology,
			@QueryParam(NVDashboardConstants.OPERATOR) String operator,
			@QueryParam(NVDashboardConstants.DISTRIBUTION_TYPE) String distType,
            @QueryParam(NVDashboardConstants.COUNTRY) String country) {
		logger.info("In getAllUserCount advancedSearchId {},endDate {},callType {},band {},technology {},operator {}",advancedSearchId, endDate, callType, band, technology, operator);

		List<NVDistributionWrapper> distributionWrappers = null;
		try {
			distributionWrappers = iNVDashboardservice.getNvDistributionDataByType(advancedSearchId, endDate, callType, band, technology, operator, distType,kpi,country);
		} catch (RestException e) {
			logger.error("Error while getting usercount data data and message is {}", e.getMessage());
		}
		if (CollectionUtils.isNotEmpty(distributionWrappers)) {
			return Response.ok(distributionWrappers).build();
		} else {
			return Response.ok(NVConstant.DATA_NOT_FOUND).build();
		}
	}

	/**
	 * This method is used to get top 7 locations based on given kpi .
	 *
	 * @param advancedSearchId the advanced search id
	 * @param endDate the end date
	 * @param callType the call type
	 * @param band the band
	 * @param technology the technology
	 * @param operator the operator
	 * @param kpi the kpi
	 * @return the top seven data by location
	 */
	@GET
	@Produces(ForesightConstants.APPLICATION_SLASH_JSON)
	@Path("getTopSevenDataByLocation")
	public Response getTopSevenDataByLocation(
			@QueryParam(NVDashboardConstants.ADVANCESEARCH_ID) Integer advancedSearchId,
			@QueryParam(NVDashboardConstants.END_DATE) String endDate,
			@QueryParam(NVDashboardConstants.CALL_TYPE) String callType,
			@QueryParam(NVDashboardConstants.BAND) String band,
			@QueryParam(NVDashboardConstants.TECHNOLOGY) String technology,
			@QueryParam(NVDashboardConstants.OPERATOR) String operator,
			@QueryParam(NVDashboardConstants.KPI) String kpi,
            @QueryParam(NVDashboardConstants.COUNTRY) String country) {
		List<TopGeographyWrapper> topGeographyByKpi = null;
		try {
			topGeographyByKpi = iNVDashboardservice.getTopGeographyByKpi(advancedSearchId, callType, band, operator,technology, kpi, endDate,country);
		} catch (RestException e) {
			logger.error("Error while getting top locations data and message is {}", e.getMessage());
		}
		if (CollectionUtils.isNotEmpty(topGeographyByKpi)) {
			return Response.ok(topGeographyByKpi).build();
		} else {
			return Response.ok(NVConstant.DATA_NOT_FOUND).build();
		}
	}
	@GET
	@Produces(ForesightConstants.APPLICATION_SLASH_JSON)
	@Path("getOperatorRoamingCountries")
	public Response getOperatorRoamingCountries() {
		Map<String, List<String>> countryOperatorMap=null;
		try {
			 countryOperatorMap = iNVDashboardservice.getRoamingCountries();
		} catch (Exception e) {
			logger.error("Error while getting all technologies,operator and band data and message is {}", e.getMessage());
		}
		if (MapUtils.isNotEmpty(countryOperatorMap)) {
			return Response.ok(countryOperatorMap).build();
		} else {
			return Response.ok(NVConstant.DATA_NOT_FOUND).build();
		}
	}
}

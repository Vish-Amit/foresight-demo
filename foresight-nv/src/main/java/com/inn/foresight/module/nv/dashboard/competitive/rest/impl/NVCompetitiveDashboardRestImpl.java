package com.inn.foresight.module.nv.dashboard.competitive.rest.impl;

import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.commons.collections.MapUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.NVConstant;
import com.inn.foresight.module.nv.dashboard.competitive.constant.NVCompetitiveConstant;
import com.inn.foresight.module.nv.dashboard.competitive.rest.INVCompetitiveDashboardRest;
import com.inn.foresight.module.nv.dashboard.competitive.service.INVCompetitiveDashboardService;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;

/**
 * The Class NVCompetitiveDashboardRestImpl.
 *
 * @author innoeye
 */
@Path("/NVCompetitiveDashboard")
@Produces(ForesightConstants.APPLICATION_SLASH_JSON)
@Consumes(ForesightConstants.APPLICATION_SLASH_JSON)
@Service("NVCompetitiveDashboardRestImpl")
public class NVCompetitiveDashboardRestImpl implements INVCompetitiveDashboardRest {

	/** The logger. */
	private Logger logger = LogManager.getLogger(NVCompetitiveDashboardRestImpl.class);

	/** Autowired INVCompetitiveDashboardService. */
	@Autowired
	INVCompetitiveDashboardService iNVCompetitiveDashboardService;

	/**
	 * Get Competitive User count by date operator and geographies.
	 *
	 * @param date
	 *            the date
	 * @param geographyL1
	 *            the geography L 1
	 * @param geographyL2
	 *            the geography L 2
	 * @param geographyL3
	 *            the geography L 3
	 * @param geographyL4
	 *            the geography L 4
	 * @param operators
	 *            the operators
	 * @return the competitive user
	 */
	@POST
	@Path("/getCompetitiveUser")
	@Override
	public Response getCompetitiveUser(@QueryParam(NVCompetitiveConstant.DATE) String date,
			@QueryParam(NVCompetitiveConstant.GEOGRAPHY_L1) String geographyL1,
			@QueryParam(NVCompetitiveConstant.GEOGRAPHY_L2) String geographyL2,
			@QueryParam(NVCompetitiveConstant.GEOGRAPHY_L3) String geographyL3,
			@QueryParam(NVCompetitiveConstant.GEOGRAPHY_L4) String geographyL4, List<String> operators,
			@QueryParam(NVCompetitiveConstant.APPLICATION_TYPE) String appName) {

		logger.info("going to get Competitive dashboard  user count with date :[{}], operator :[{}] ,applicationType : {}", date, operators,appName);
		try {
			if (date != null && operators != null && appName!= null) {
				Map<String, Object> competitiveUser = iNVCompetitiveDashboardService.getCompetitiveUser(date,
						geographyL1, geographyL2, geographyL3, geographyL4, operators,appName);
				if (MapUtils.isNotEmpty(competitiveUser)) {
					return Response.ok(competitiveUser).build();
				} else {
					return Response.ok(NVConstant.DATA_NOT_FOUND).build();
				}
			}
		} catch (Exception e) {
			logger.error(
					"Exception inside the method getCompetitiveUser for date {} geographyL1 {} geographyL2 {} geographyL3 {}  geographyL4  {} and operator {}"
					+ " ,applicationType : {} ",
					date, geographyL1, geographyL2, geographyL3, geographyL4, operators,appName, Utils.getStackTrace(e));
		}
		return Response.ok(NVConstant.INVALID_PARAM).build();
	}

	/**
	 * Get Competitive Sample count by date operator and geographies.
	 *
	 * @param date
	 *            the date
	 * @param geographyL1
	 *            the geography L 1
	 * @param geographyL2
	 *            the geography L 2
	 * @param geographyL3
	 *            the geography L 3
	 * @param geographyL4
	 *            the geography L 4
	 * @param operators
	 *            the operators
	 * @return the sample count
	 */
	@POST
	@Path("/getSampleCount")
	@Override
	public Response getSampleCount(@QueryParam(NVCompetitiveConstant.DATE) String date,
			@QueryParam(NVCompetitiveConstant.GEOGRAPHY_L1) String geographyL1,
			@QueryParam(NVCompetitiveConstant.GEOGRAPHY_L2) String geographyL2,
			@QueryParam(NVCompetitiveConstant.GEOGRAPHY_L3) String geographyL3,
			@QueryParam(NVCompetitiveConstant.GEOGRAPHY_L4) String geographyL4, List<String> operators,
			@QueryParam(NVCompetitiveConstant.APPLICATION_TYPE) String appType) {

		logger.info("going to get sample count with date :[{}], operator :[{}],applicationType : {}", date, operators,appType);
		try {
			if (date != null && operators != null) {
				Map<String, Map<String, Long>> sampleCount = iNVCompetitiveDashboardService.getSampleCount(date,
						geographyL1, geographyL2, geographyL3, geographyL4, operators,appType);
				if (MapUtils.isNotEmpty(sampleCount)) {
					return Response.ok(sampleCount).build();
				} else {
					return Response.ok(NVConstant.DATA_NOT_FOUND).build();
				}
			}
		} catch (Exception e) {
			logger.error(
					"Exception inside the method getSampleCount for date {} geographyL1 {} geographyL2 {} geographyL3 {}  geographyL4  {} and operator {} ",
					date, geographyL1, geographyL2, geographyL3, geographyL4, operators, Utils.getStackTrace(e));
		}
		return Response.ok(NVConstant.INVALID_PARAM).build();
	}

	/**
	 * Get Competitive KPI distribution by date operator and geographies.
	 *
	 * @param date
	 *            the date
	 * @param geographyL1
	 *            the geography L 1
	 * @param geographyL2
	 *            the geography L 2
	 * @param geographyL3
	 *            the geography L 3
	 * @param geographyL4
	 *            the geography L 4
	 * @param operators
	 *            the operators
	 * @return the KPI distibution
	 */
	@POST
	@Path("/getKPIDistribution")
	@Override
	public Response getKPIDistribution(@QueryParam(NVCompetitiveConstant.DATE) String date,
			@QueryParam(NVCompetitiveConstant.GEOGRAPHY_L1) String geographyL1,
			@QueryParam(NVCompetitiveConstant.GEOGRAPHY_L2) String geographyL2,
			@QueryParam(NVCompetitiveConstant.GEOGRAPHY_L3) String geographyL3,
			@QueryParam(NVCompetitiveConstant.GEOGRAPHY_L4) String geographyL4, List<String> operators,
			@QueryParam(NVCompetitiveConstant.APPLICATION_TYPE) String appType) {

		logger.info("going to get KPI distribution count with date :[{}], operator :[{}],applicationType : {}", date, operators,appType);
		try {
			if (date != null && operators != null) {
				Map<String, Map<String, Map<String, String>>> kpiDistribution = iNVCompetitiveDashboardService
						.getKPIDistribution(date, geographyL1, geographyL2, geographyL3, geographyL4, operators,appType);
				if (MapUtils.isNotEmpty(kpiDistribution)) {
					return Response.ok(kpiDistribution).build();
				} else {
					return Response.ok(NVConstant.DATA_NOT_FOUND).build();
				}

			}
		} catch (Exception e) {
			logger.error(
					"Exception inside the method getKPIDistribution for date {} geographyL1 {} geographyL2 {} geographyL3 {}  geographyL4  {} and operator {} ",
					date, geographyL1, geographyL2, geographyL3, geographyL4, operators, Utils.getStackTrace(e));
		}
		return Response.ok(NVWorkorderConstant.FAILURE_JSON).build();
	}

	/**
	 * Get Operator distribution count by date operator and geographies.
	 *
	 * @param date
	 *            the date
	 * @param geographyL1
	 *            the geography L 1
	 * @param geographyL2
	 *            the geography L 2
	 * @param geographyL3
	 *            the geography L 3
	 * @param geographyL4
	 *            the geography L 4
	 * @param operators
	 *            the operators
	 * @return the operator distribution
	 */
	@POST
	@Path("/getOperatorDistribution")
	@Override
	public Response getOperatorDistribution(@QueryParam(NVCompetitiveConstant.DATE) String date,
			@QueryParam(NVCompetitiveConstant.GEOGRAPHY_L1) String geographyL1,
			@QueryParam(NVCompetitiveConstant.GEOGRAPHY_L2) String geographyL2,
			@QueryParam(NVCompetitiveConstant.GEOGRAPHY_L3) String geographyL3,
			@QueryParam(NVCompetitiveConstant.GEOGRAPHY_L4) String geographyL4, List<String> operators,
			@QueryParam(NVCompetitiveConstant.APPLICATION_TYPE) String appType) {

		logger.info("going to get Operator distribution count with date :[{}], operator :[{}],applicationType : {}", date, operators,appType);
		try {
			if (date != null && operators != null) {
				String operatorDistribution = iNVCompetitiveDashboardService.getOperatorDistribution(date, geographyL1,
						geographyL2, geographyL3, geographyL4, operators,appType);
				if (operatorDistribution != null) {

					return Response.ok(operatorDistribution).build();

				} else {
					return Response.ok(NVConstant.DATA_NOT_FOUND).build();
				}

			}
		} catch (Exception e) {
			logger.error(
					"Exception inside the method getOperatorDistribution for date {} geographyL1 {} geographyL2 {} geographyL3 {}  geographyL4  {} and operator {} ",
					date, geographyL1, geographyL2, geographyL3, geographyL4, operators, Utils.getStackTrace(e));
		}
		return Response.ok(NVWorkorderConstant.FAILURE_JSON).build();
	}

	/**
	 * Get KPI ranges by date, operator, geographies and KPI type.
	 *
	 * @param date
	 *            the date
	 * @param kpi
	 *            the kpi
	 * @param geographyL1
	 *            the geography L 1
	 * @param geographyL2
	 *            the geography L 2
	 * @param geographyL3
	 *            the geography L 3
	 * @param geographyL4
	 *            the geography L 4
	 * @param operators
	 *            the operators
	 * @return the KPI ranges
	 */
	@POST
	@Path("/getKPIRanges")
	@Override
	public Response getKPIRanges(@QueryParam(NVCompetitiveConstant.DATE) String date,
			@QueryParam(NVCompetitiveConstant.KPI) String kpi,
			@QueryParam(NVCompetitiveConstant.GEOGRAPHY_L1) String geographyL1,
			@QueryParam(NVCompetitiveConstant.GEOGRAPHY_L2) String geographyL2,
			@QueryParam(NVCompetitiveConstant.GEOGRAPHY_L3) String geographyL3,
			@QueryParam(NVCompetitiveConstant.GEOGRAPHY_L4) String geographyL4, List<String> operators,
			@QueryParam(NVCompetitiveConstant.APPLICATION_TYPE) String appType) {

		logger.info("going to get KPI Ranges with date: {}, operator: {},applicationType : {}", date, operators,appType);
		try {
			if (date != null && operators != null) {
				Map<String, Map<String, Long>> kpiRanges = iNVCompetitiveDashboardService.getKPIRanges(date, kpi,
						geographyL1, geographyL2, geographyL3, geographyL4, operators,appType);
				if (MapUtils.isNotEmpty(kpiRanges)) {
					return Response.ok(kpiRanges).build();
				} else {
					return Response.ok(NVConstant.DATA_NOT_FOUND).build();
				}
			}
		} catch (Exception e) {
			logger.error(
					"Exception inside the method getKPIRanges for date {} geographyL1 {} geographyL2 {} geographyL3 {}  geographyL4  {} and operator {} ",
					date, geographyL1, geographyL2, geographyL3, geographyL4, operators, Utils.getStackTrace(e));
		}
		return Response.ok(NVWorkorderConstant.FAILURE_JSON).build();
	}

	/**
	 * Get DL+UL distribution count by date operator and geographies.
	 *
	 * @param date
	 *            the date
	 * @param geographyL1
	 *            the geography L 1
	 * @param geographyL2
	 *            the geography L 2
	 * @param geographyL3
	 *            the geography L 3
	 * @param geographyL4
	 *            the geography L 4
	 * @param operators
	 *            the operators
	 * @return the dl ul distribution
	 */
	@POST
	@Path("/getDlUlDistribution/")
	@Override
	public Response getDlUlDistribution(@QueryParam(NVCompetitiveConstant.DATE) String date,
			@QueryParam(NVCompetitiveConstant.GEOGRAPHY_L1) String geographyL1,
			@QueryParam(NVCompetitiveConstant.GEOGRAPHY_L2) String geographyL2,
			@QueryParam(NVCompetitiveConstant.GEOGRAPHY_L3) String geographyL3,
			@QueryParam(NVCompetitiveConstant.GEOGRAPHY_L4) String geographyL4, List<String> operators,
			@QueryParam(NVCompetitiveConstant.APPLICATION_TYPE) String appType) {

		logger.info("going to get KPI Ranges with date :{}, operator :{},applicationType : {}", date, operators,appType);
		try {
			if (date != null && operators != null) {
				Map<String, Map<String, Map<String, Map<String, String>>>> dlUlDistribution = iNVCompetitiveDashboardService
						.getDlUlDistribution(date, geographyL1, geographyL2, geographyL3, geographyL4, operators,appType);
				if (MapUtils.isNotEmpty(dlUlDistribution)) {
					return Response.ok(dlUlDistribution).build();
				} else {
					return Response.ok(NVConstant.DATA_NOT_FOUND).build();
				}
			}
		} catch (Exception e) {
			logger.error(
					"Exception inside the method getDlUlDistribution for date {} geographyL1 {} geographyL2 {} geographyL3 {}  geographyL4  {} and operator {} ",
					date, geographyL1, geographyL2, geographyL3, geographyL4, operators, Utils.getStackTrace(e));
		}
		return Response.ok(NVWorkorderConstant.FAILURE_JSON).build();
	}

	/**
	 * Get DL/UL Ranges by date, operator, geographies and DL/UL type.
	 *
	 * @param date
	 *            the date
	 * @param kpi
	 *            the kpi
	 * @param geographyL1
	 *            the geography L 1
	 * @param geographyL2
	 *            the geography L 2
	 * @param geographyL3
	 *            the geography L 3
	 * @param geographyL4
	 *            the geography L 4
	 * @param operators
	 *            the operators
	 * @return the dl ul ranges
	 */
	@POST
	@Path("/getDlUlRanges/")
	@Override
	public Response getDlUlRanges(@QueryParam(NVCompetitiveConstant.DATE) String date,
			@QueryParam(NVCompetitiveConstant.KPI) String kpi,
			@QueryParam(NVCompetitiveConstant.GEOGRAPHY_L1) String geographyL1,
			@QueryParam(NVCompetitiveConstant.GEOGRAPHY_L2) String geographyL2,
			@QueryParam(NVCompetitiveConstant.GEOGRAPHY_L3) String geographyL3,
			@QueryParam(NVCompetitiveConstant.GEOGRAPHY_L4) String geographyL4, List<String> operators,
			@QueryParam(NVCompetitiveConstant.APPLICATION_TYPE) String appType) {

		logger.info("going to get KPI Ranges with date :[{}], operator :[{}],,applicationType : {}", date, operators,appType);
		try {
			if (date != null && operators != null) {
				Map<String, Map<String, Map<String, Integer>>> dlUlRanges = iNVCompetitiveDashboardService
						.getDlUlRanges(date, kpi, geographyL1, geographyL2, geographyL3, geographyL4, operators,appType);
				if (MapUtils.isNotEmpty(dlUlRanges)) {
					return Response.ok(dlUlRanges).build();
				} else {
					return Response.ok(NVConstant.DATA_NOT_FOUND).build();
				}
			}
		} catch (Exception e) {
			logger.error(
					"Exception inside the method getDlUlRanges for date {} geographyL1 {} geographyL2 {} geographyL3 {}  geographyL4  {} and operator {} ",
					date, geographyL1, geographyL2, geographyL3, geographyL4, operators, Utils.getStackTrace(e));
		}
		return Response.ok(NVWorkorderConstant.FAILURE_JSON).build();
	}

	/**
	 * Gets the NPS data.
	 *
	 * @param date
	 *            the date
	 * @param geographyL1
	 *            the geography L 1
	 * @param geographyL2
	 *            the geography L 2
	 * @param geographyL3
	 *            the geography L 3
	 * @param geographyL4
	 *            the geography L 4
	 * @param operators
	 *            the operators
	 * @return the NPS data
	 */
	@POST
	@Path("/getNPSData/")
	@Override
	public Response getNPSData(@QueryParam(NVCompetitiveConstant.DATE) String date,
			@QueryParam(NVCompetitiveConstant.GEOGRAPHY_L1) String geographyL1,
			@QueryParam(NVCompetitiveConstant.GEOGRAPHY_L2) String geographyL2,
			@QueryParam(NVCompetitiveConstant.GEOGRAPHY_L3) String geographyL3,
			@QueryParam(NVCompetitiveConstant.GEOGRAPHY_L4) String geographyL4, List<String> operators) {
		logger.info("going to getNPSData with date :{}, operator :{}", date, operators);
		try {
			if (date != null && operators != null) {
				Map<String, Double> npsData = iNVCompetitiveDashboardService.getNPSData(date, geographyL1, geographyL2,
						geographyL3, geographyL4, operators);
				if (MapUtils.isNotEmpty(npsData)) {

					return Response.ok(npsData).build();

				} else {
					return Response.ok(NVConstant.DATA_NOT_FOUND).build();
				}
			}
		} catch (Exception e) {
			logger.error(
					"Exception inside the method getNPSData for date {} geographyL1 {} geographyL2 {} geographyL3 {}  geographyL4  {} and operator {} ",
					date, geographyL1, geographyL2, geographyL3, geographyL4, operators, Utils.getStackTrace(e));
		}
		return Response.ok(NVWorkorderConstant.FAILURE_JSON).build();
	}

	/**
	 * @param date
	 * @param kpi
	 * @param geographyL1
	 * @param geographyL2
	 * @param geographyL3
	 * @param geographyL4
	 * @param operators
	 * @return
	 */
	@POST
	@Path("/getPerformanceKPI/")
	@Override
	public Response getPerformanceKPIData(@QueryParam(NVCompetitiveConstant.DATE) String date,
			@QueryParam(NVCompetitiveConstant.KPI) String kpi,
			@QueryParam(NVCompetitiveConstant.GEOGRAPHY_L1) String geographyL1,
			@QueryParam(NVCompetitiveConstant.GEOGRAPHY_L2) String geographyL2,
			@QueryParam(NVCompetitiveConstant.GEOGRAPHY_L3) String geographyL3,
			@QueryParam(NVCompetitiveConstant.GEOGRAPHY_L4) String geographyL4, List<String> operators,
			@QueryParam(NVCompetitiveConstant.TECHNOLOGY) String technology,
			@QueryParam(NVCompetitiveConstant.APPLICATION_TYPE) String appType) {

		logger.info("going to get KPI Performance Data with kpi {} date :[{}], operator :[{}] ,technology {},applicationType : {}", kpi,
				date, operators, technology,appType);
		try {
			if (date != null && operators != null) {
				Object performanceKPIData = iNVCompetitiveDashboardService.getPerformanceKPIData(date, kpi, geographyL1,
						geographyL2, geographyL3, geographyL4, operators, technology,appType);
				if (performanceKPIData != null) {

					return Response.ok(performanceKPIData).build();

				} else {
					return Response.ok(NVConstant.DATA_NOT_FOUND).build();
				}
			}
		} catch (Exception e) {
			logger.error(
					"Exception inside the method getPerformanceKPIData for date {} geographyL1 {} geographyL2 {} geographyL3 {}  geographyL4  {} and operator {} ",
					date, geographyL1, geographyL2, geographyL3, geographyL4, operators, Utils.getStackTrace(e));
		}
		return Response.ok(NVWorkorderConstant.FAILURE_JSON).build();
	}

}

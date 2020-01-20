package com.inn.foresight.module.nv.nps.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.inn.foresight.module.nv.nps.constants.NetPromoterConstant;
import com.inn.foresight.module.nv.nps.service.INetPromoterService;

/** The Class NetPromoterRest. */
@Path("/NetPromoter")
@Produces(NetPromoterConstant.APPLICATION_SLASH_JSON)
@Consumes(NetPromoterConstant.APPLICATION_SLASH_JSON)

public class NetPromoterRest {

	/** The logger. */
	private Logger logger = LogManager.getLogger(NetPromoterRest.class);

	/** The i net promoter service. */
	@Autowired
	private INetPromoterService iNetPromoterService;

	/**
	 * Gets the NPS event score.
	 *
	 * @param geographyId
	 *            the geography id
	 * @param geographyType
	 *            the geography type
	 * @param startDate
	 *            the start date
	 * @param endDate
	 *            the end date
	 * @param operator
	 *            the operator
	 * @param technology
	 *            the technology
	 * @return the NPS event score
	 */
	@GET
	@Produces(NetPromoterConstant.APPLICATION_SLASH_JSON)
	@Path("getNPSEventScore")
	public Response getNPSEventScore(@QueryParam(NetPromoterConstant.GEOGRAPHY_ID) Integer geographyId,
			@QueryParam(NetPromoterConstant.GEOGRAPHY_TYPE) String geographyType,
			@QueryParam(NetPromoterConstant.START_DATE) String startDate,
			@QueryParam(NetPromoterConstant.END_DATE) String endDate,
			@QueryParam(NetPromoterConstant.OPERATOR) String operator,
			@QueryParam(NetPromoterConstant.TECHNOLOGY) String technology) {
		logger.info(
				"In getNPSEventScore geographyId, geographyType {} startdate {} enddate {} operator {} technology {} dateType {}",
				geographyId, geographyType, startDate, endDate, operator, technology);

		return Response.ok(iNetPromoterService.getNPSEventScore(geographyId, geographyType, startDate, endDate,
				operator, technology)).build();

	}

	/**
	 * Gets the NPS score analysis.
	 *
	 * @param geographyId
	 *            the geography id
	 * @param geographyType
	 *            the geography type
	 * @param startDate
	 *            the start date
	 * @param operator
	 *            the operator
	 * @param technology
	 *            the technology
	 * @return the NPS score analysis
	 */
	@GET
	@Produces(NetPromoterConstant.APPLICATION_SLASH_JSON)
	@Path("getNPSMonthlyAnalysisDetail")
	public Response getNPSScoreAnalysis(@QueryParam(NetPromoterConstant.GEOGRAPHY_ID) Integer geographyId,
			@QueryParam(NetPromoterConstant.GEOGRAPHY_TYPE) String geographyType,
			@QueryParam(NetPromoterConstant.START_DATE) String startDate,
			@QueryParam(NetPromoterConstant.OPERATOR) String operator,
			@QueryParam(NetPromoterConstant.TECHNOLOGY) String technology) {
		logger.info(
				"In getNPSMonthlyAnalysisDetail geographyId, geographyType {} startdate {} operator {} technology {} dateType {}",
				geographyId, geographyType, startDate, operator, technology);

		return Response.ok(iNetPromoterService.getNPSMonthlyAnalysisDetail(geographyId, geographyType, startDate,
				operator, technology)).build();

	}

	@GET
	@Produces(NetPromoterConstant.APPLICATION_SLASH_JSON)
	@Path("getNPSData")
	public Response getNPSData(@QueryParam(NetPromoterConstant.GEOGRAPHY_ID) Integer geographyId,
			@QueryParam(NetPromoterConstant.GEOGRAPHY_TYPE) String geographyType,
			@QueryParam(NetPromoterConstant.START_DATE) String startDate,
			@QueryParam(NetPromoterConstant.CALL_TYPE) String callType,
			@QueryParam(NetPromoterConstant.OPERATOR) String operator,
			@QueryParam(NetPromoterConstant.TECHNOLOGY) String technology) {
		logger.info(
				"In getNPSData geographyId, geographyType {} startdate {} enddate {} operator {} technology {} dateType {}",
				geographyId, geographyType, startDate, callType, operator, technology);

		return Response.ok(
				iNetPromoterService.getNPSData(geographyId, geographyType, startDate, callType, operator, technology))
				.build();

	}

	@GET
	@Produces(NetPromoterConstant.APPLICATION_SLASH_JSON)
	@Path("getNPSKpiWiseData")
	public Response getNPSKpiWiseData(@QueryParam(NetPromoterConstant.GEOGRAPHY_ID) Integer geographyId,
			@QueryParam(NetPromoterConstant.GEOGRAPHY_TYPE) String geographyType,
			@QueryParam(NetPromoterConstant.START_DATE) String startDate,
			@QueryParam(NetPromoterConstant.END_DATE) String endDate,
			@QueryParam(NetPromoterConstant.OPERATOR) String operator,
			@QueryParam(NetPromoterConstant.TECHNOLOGY) String technology,
			@QueryParam(NetPromoterConstant.KPI) String kpi) {
		logger.info(
				"In getNPSData geographyId, geographyType {} startdate {} enddate {} operator {} technology {} dateType {}, kpi {}",
				geographyId, geographyType, startDate, endDate, operator, technology, kpi);
		String json = "[]";
		try {
			json = iNetPromoterService.getNPSKpiWiseData(geographyId, 
					geographyType, startDate, endDate, operator,
					technology, kpi);
			logger.info("json  {}",json);
		} catch (Exception e) {
			logger.error("Error while getting getNPSKpiWiseData and message is {}", e.getMessage());
		}
		return Response.ok(json).build();
	}

	
	
}

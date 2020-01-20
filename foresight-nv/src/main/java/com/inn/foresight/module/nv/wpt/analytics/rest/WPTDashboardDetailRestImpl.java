package com.inn.foresight.module.nv.wpt.analytics.rest;

import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.foresight.module.nv.wpt.analytics.constants.WPTAnalyticsConstants;
import com.inn.foresight.module.nv.wpt.analytics.service.IWPTDashboardDetailService;
import com.inn.foresight.module.nv.wpt.analytics.utils.WPTDashboardUtils;

/** The Class WPTDashboardDetailRestImpl. */
@Path("/wptDashboard")
@Service("WPTDashboardDetailRestImpl")
public class WPTDashboardDetailRestImpl extends WPTDashboardUtils {

	/** The logger. */
	private Logger logger = LogManager.getLogger(WPTDashboardDetailRestImpl.class);

	/** Instance of WPTDashboardDetailServiceImpl. */
	@Autowired
	private IWPTDashboardDetailService service;

	/**
	 * Loading List of All filter for Dashboard Selctions i.e. Geography List based on given Geography Level,
	 * Locations List, Operator and Technology List and Category List
	 * @param geographyLevel : Define Geography Level i.e. GeographyL1, GeographyL2, GeographyL3, GeographyL4
	 * @return Map contain all the Filter Lists
	 */
	@GET
	@Path("getAllFilters")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFilterList(@QueryParam(WPTAnalyticsConstants.GEOGRAPHY_LEVEL) String geographyLevel) {
		logger.info("@method : getFilterList(), Going to all filters for");
		return Response.ok(service.getFilterList(geographyLevel)).build();
	}
	
	/**
	 * Save and get filters.
	 *
	 * @param filterJSON the filter JSON
	 * @return the response
	 */
	@POST
	@Path("saveAndGetFilters")
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveAndGetFilters(String filterJSON) {
		logger.info("@method : getFilterList(), Going to save filters for User, filter JSON {}", filterJSON);
		return Response.ok(service.saveAndGetFilters(filterJSON)).build();
	}

	/**
	 * Getting JSON for Target IP Version Chart contain percentage of Ipv4 and Ipv4 Counts
	 * for the selection .
	 *
	 * @param date : Date Selected
	 * @param processType : Aggregation Process Type i.e. Daily, Weekly or Monthly
	 * @param geographyLevel : Define Geography Level i.e. GeographyL1, GeographyL2, GeographyL3, GeographyL4
	 * @param geographyId : Id of the Geography Selected
	 * @param operator : Operator Selected
	 * @param technology : Technology Selected
	 * @return Map contain Ipv4 and Ipv6 Percentage
	 */
	@GET
	@Path("getTargetIpVersionPercent")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTargetIpVersionPercent(@QueryParam(WPTAnalyticsConstants.DATE) String date,
			@QueryParam(WPTAnalyticsConstants.PROCESS_TYPE) String processType,
			@QueryParam(WPTAnalyticsConstants.GEOGRAPHY_LEVEL) String geographyLevel,
			@QueryParam(WPTAnalyticsConstants.GEOGRAPHY_ID) Integer geographyId,
			@QueryParam(WPTAnalyticsConstants.OPERATOR) String operator,
			@QueryParam(WPTAnalyticsConstants.TECHNOLOGY) String technology) {
		logger.info(
				"Going to get Target Ip Version Json for fromDate {}, processType {}, geographyLevel {}, geographyId {},operator {}, technology {}",
				date, processType, geographyLevel, geographyId, operator, technology);
		if (date != null && processType != null && geographyId != null && operator != null && technology != null
				&& geographyLevel != null) {
			return Response.ok(service.getTargetIpVersionPercent(date, processType, geographyLevel, geographyId,
					operator, technology)).build();
		}
		return Response.ok(WPTAnalyticsConstants.INVALID_PARAMETER_JSON).build();
	}

	/**
	 * Getting JSON For the Previous Seven Days, or Weeks or Months Count depends on the 'processType' Selected
	 * For Total Records, Top Tested URLs and Total Devices Bar Chart.
	 *
	 * @param date : Date Selected
	 * @param processType : Aggregation Process Type i.e. Daily, Weekly or Monthly
	 * @param geographyLevel : Define Geography Level i.e. GeographyL1, GeographyL2, GeographyL3, GeographyL4
	 * @param geographyId : Id of the Geography Selected
	 * @param operator : Operator Selected
	 * @param technology : Technology Selected
	 * @return JSON contains Previous Seven Days, or Weeks or Months Count for Total Records, Top Tested URLs and Total Devices
	 */
	@GET
	@Path("getDayWiseCounts")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDayWiseCounts(@QueryParam(WPTAnalyticsConstants.DATE) String date,
			@QueryParam(WPTAnalyticsConstants.PROCESS_TYPE) String processType,
			@QueryParam(WPTAnalyticsConstants.GEOGRAPHY_LEVEL) String geographyLevel,
			@QueryParam(WPTAnalyticsConstants.GEOGRAPHY_ID) Integer geographyId,
			@QueryParam(WPTAnalyticsConstants.OPERATOR) String operator,
			@QueryParam(WPTAnalyticsConstants.TECHNOLOGY) String technology) {
		logger.info(
				"Going to get Day Wise Counts for date {}, processType {}, geographyLevel {}, geographyId {},operator {}, technology {}",
				date, processType, geographyLevel, geographyId, operator, technology);
		if (date != null && processType != null && geographyLevel != null && geographyId != null && operator != null
				&& technology != null) {
			return Response
					.ok(service.getDayWiseCounts(date, processType, geographyLevel, geographyId, operator, technology))
					.build();
		}
		return Response.ok(WPTAnalyticsConstants.INVALID_PARAMETER_JSON).build();
	}

	/**
	 * Getting Top URLs (Maximum 4) based on their respective KPI Counts i.e. Total Tested URLs, Ping and Trace Route
	 * @param date : Date Selected
	 * @param processType : Aggregation Process Type i.e. Daily, Weekly or Monthly
	 * @param geographyLevel : Define Geography Level i.e. GeographyL1, GeographyL2, GeographyL3, GeographyL4
	 * @param geographyId : Id of the Geography Selected
	 * @param operator : Operator Selected
	 * @param technology : Technology Selected
	 * @return Map contains Top URLs Details
	 */
	@GET
	@Path("getTopUrlCounts")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTopUrlCounts(@QueryParam(WPTAnalyticsConstants.DATE) String date,
			@QueryParam(WPTAnalyticsConstants.PROCESS_TYPE) String processType,
			@QueryParam(WPTAnalyticsConstants.GEOGRAPHY_LEVEL) String geographyLevel,
			@QueryParam(WPTAnalyticsConstants.GEOGRAPHY_ID) Integer geographyId,
			@QueryParam(WPTAnalyticsConstants.OPERATOR) String operator,
			@QueryParam(WPTAnalyticsConstants.TECHNOLOGY) String technology) {

		logger.info(
				"Going to get all Top Url Counts for geographyLevel {}, date {}, processType {}, geographyId {}, operator {}, technology {}",
				geographyLevel, date, processType, geographyId, operator, technology);

		if (date != null && processType != null && geographyId != null && operator != null && technology != null) {

			return Response
					.ok(service.getTopUrlCounts(date, processType, geographyLevel, geographyId, operator, technology))
					.build();
		}
		return Response.ok(WPTAnalyticsConstants.INVALID_PARAMETER_JSON).build();
	}

	/**
	 * Getting URL Details based on their respective KPIs i.e. Total Tested URLs, Ping and Trace Route
	 * @param date : Date Selected
	 * @param processType : Aggregation Process Type i.e. Daily, Weekly or Monthly
	 * @param geographyLevel : Define Geography Level i.e. GeographyL1, GeographyL2, GeographyL3, GeographyL4
	 * @param geographyId : Id of the Geography Selected
	 * @param operator : Operator Selected
	 * @param technology : Technology Selected
	 * @param isPingDataRequired : true if Getting Detail for Ping Kpi, false otherwise
	 * @param ulimit : Upper Limit of URL Counts For Pagination
	 * @param llimit : Lower Limit of URL Counts For Pagination
	 * @return List contains All URL Details
	 */
	@GET
	@Path("getURLDetails")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getURLDetails(@QueryParam(WPTAnalyticsConstants.DATE) String date,
			@QueryParam(WPTAnalyticsConstants.PROCESS_TYPE) String processType,
			@QueryParam(WPTAnalyticsConstants.GEOGRAPHY_LEVEL) String geographyLevel,
			@QueryParam(WPTAnalyticsConstants.GEOGRAPHY_ID) Integer geographyId,
			@QueryParam(WPTAnalyticsConstants.OPERATOR) String operator,
			@QueryParam(WPTAnalyticsConstants.TECHNOLOGY) String technology,
			@QueryParam(WPTAnalyticsConstants.ISPINGDATAREQUIRED) Boolean isPingDataRequired,
			@QueryParam(WPTAnalyticsConstants.ULIMIT) Integer ulimit,
			@QueryParam(WPTAnalyticsConstants.LLIMIT) Integer llimit) {

		logger.info("Going to get all Top Url Details for geographyLevel {}, date {}, processType {}, geographyId {}, operator {}, technology {}, ulimit {}, llimit {}, isPingDataRequired {}",
				geographyLevel, date, processType, geographyId, operator, technology, ulimit, llimit,
				isPingDataRequired);

		if (date != null && processType != null && geographyId != null && operator != null && technology != null) {

			return Response.ok(service.getURLDetails(date, processType, geographyLevel, geographyId, operator,
					technology, isPingDataRequired != null ? isPingDataRequired : Boolean.FALSE, ulimit, llimit))
					.build();
		}
		return Response.ok(WPTAnalyticsConstants.INVALID_PARAMETER_JSON).build();
	}

	/**
	 * Getting Time to First Byte Aggregated Data For the Previous Seven Days, or Weeks or Months Count 
	 * depends on the 'processType' Selected.
	 *
	 * @param date : Date Selected
	 * @param processType : Aggregation Process Type i.e. Daily, Weekly or Monthly
	 * @param geographyLevel : Define Geography Level i.e. GeographyL1, GeographyL2, GeographyL3, GeographyL4
	 * @param geographyId : Id of the Geography Selected
	 * @param operator : Operator Selected
	 * @param technology : Technology Selected
	 * @return JSON contains Previous Seven Days, or Weeks or Months Time to First Byte value
	 */
	@GET
	@Path("getAvgTTFBDetail")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAvgTTFBDetail(@QueryParam(WPTAnalyticsConstants.DATE) String date,
			@QueryParam(WPTAnalyticsConstants.PROCESS_TYPE) String processType,
			@QueryParam(WPTAnalyticsConstants.GEOGRAPHY_LEVEL) String geographyLevel,
			@QueryParam(WPTAnalyticsConstants.GEOGRAPHY_ID) Integer geographyId,
			@QueryParam(WPTAnalyticsConstants.OPERATOR) String operator,
			@QueryParam(WPTAnalyticsConstants.TECHNOLOGY) String technology) {

		logger.info(
				"Going to get avgTTFB for geographyLevel {}, date {}, processType {}, geographyId {}, "
						+ "operator {}, technology {}",
				geographyLevel, date, processType, geographyId, operator, technology);

		if (date != null && processType != null && geographyId != null && operator != null && technology != null) {
			return Response
					.ok(service.getAvgTTFBDetail(date, processType, geographyLevel, geographyId, operator, technology))
					.build();
		}

		return Response.ok(WPTAnalyticsConstants.INVALID_PARAMETER_JSON).build();
	}

	/**
	 * Getting Time to First Byte Aggregated Data For the Previous Seven Days, or Weeks or Months Count 
	 * depends on the 'processType' Selected.
	 *
	 * @param date : Date Selected
	 * @param processType : Aggregation Process Type i.e. Daily, Weekly or Monthly
	 * @param geographyLevel : Define Geography Level i.e. GeographyL1, GeographyL2, GeographyL3, GeographyL4
	 * @param geographyId : Id of the Geography Selected
	 * @param operator : Operator Selected
	 * @param technology : Technology Selected
	 * @return the HTTP stats data
	 */
	@GET
	@Path("getHTTPStatsData")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getHTTPStatsData(@QueryParam(WPTAnalyticsConstants.DATE) String date,
			@QueryParam(WPTAnalyticsConstants.PROCESS_TYPE) String processType,
			@QueryParam(WPTAnalyticsConstants.GEOGRAPHY_LEVEL) String geographyLevel,
			@QueryParam(WPTAnalyticsConstants.GEOGRAPHY_ID) Integer geographyId,
			@QueryParam(WPTAnalyticsConstants.OPERATOR) String operator,
			@QueryParam(WPTAnalyticsConstants.TECHNOLOGY) String technology) {

		logger.info(
				"Going to get HTTPStas for geographyLevel {}, date {}, processType {}, geographyId {}, "
						+ "operator {}, technology {}",
				geographyLevel, date, processType, geographyId, operator, technology);

		if (date != null && processType != null && geographyId != null && operator != null && technology != null) {
			return Response
					.ok(service.getHTTPAndHTTPSCount(date, processType, geographyLevel, geographyId, operator, technology))
					.build();
		}

		return Response.ok(WPTAnalyticsConstants.INVALID_PARAMETER_JSON).build();
	}

	/**
	 * Getting Data for Comparision Graph between Trace Route Time and Time to first Byte.
	 *
	 * @param map contain comparator i.e. either geography and operator with Ip Version and list of Location and Category Id
	 * @param compareType either GEOGRAPHY if  multiple geography or OPERATOR if multiple operator selected
	 * @param date : Date Selected
	 * @param processType : Aggregation Process Type i.e. Daily, Weekly or Monthly
	 * @param geographyLevel : Define Geography Level i.e. GeographyL1, GeographyL2, GeographyL3, GeographyL4
	 * @param geographyId : Id of the Geography Selected
	 * @param operator : Operator Selected
	 * @param technology : Technology Selected
	 * @return JSON contains Day wise data of Trace Route Time and Time to first Byte
	 */
	@POST
	@Path("getTraceRouteAndTTFBData")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTraceRouteAndTTFBData(Map<String, List<Object>> map,
			@QueryParam(WPTAnalyticsConstants.COMPARE_TYPE) String compareType,
			@QueryParam(WPTAnalyticsConstants.DATE) String date,
			@QueryParam(WPTAnalyticsConstants.PROCESS_TYPE) String processType,
			@QueryParam(WPTAnalyticsConstants.GEOGRAPHY_LEVEL) String geographyLevel,
			@QueryParam(WPTAnalyticsConstants.GEOGRAPHY_ID) Integer geographyId,
			@QueryParam(WPTAnalyticsConstants.OPERATOR) String operator,
			@QueryParam(WPTAnalyticsConstants.TECHNOLOGY) String technology) {

		logger.info(
				"Going to get Trace Route and First DNS Resolution data for date {}, processType {}, geographyLevel {}, geographyId {}, operator {}, map {}, compareType {}, technology {}",
				geographyLevel, date, processType, geographyId, operator, map, compareType, technology);

		if (date != null && processType != null && (geographyId != null || operator != null) && map != null && compareType != null
				&& technology != null) {
			
			return Response.ok(service.getTraceRouteAndTTFBData(map, compareType, date, processType, geographyLevel, geographyId, operator,
					technology)).build();
		}

		return Response.ok(WPTAnalyticsConstants.INVALID_PARAMETER_JSON).build();
	}

	/**
	 * Gets the TTFB and TDNS data.
	 *
	 * @param map contain comparator i.e. either geography and operator with Ip Version and list of Location and Category Id
	 * @param compareType either GEOGRAPHY if  multiple geography or OPERATOR if multiple operator selected
	 * @param date : Date Selected
	 * @param processType : Aggregation Process Type i.e. Daily, Weekly or Monthly
	 * @param geographyLevel : Define Geography Level i.e. GeographyL1, GeographyL2, GeographyL3, GeographyL4
	 * @param geographyId : Id of the Geography Selected
	 * @param operator : Operator Selected
	 * @param technology : Technology Selected
	 * @return the TTFB and TDNS data
	 */
	@POST
	@Path("getTTFBAndTDNSData")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTTFBAndTDNSData(Map<String, List<Object>> map,
			@QueryParam(WPTAnalyticsConstants.COMPARE_TYPE) String compareType,
			@QueryParam(WPTAnalyticsConstants.DATE) String date,
			@QueryParam(WPTAnalyticsConstants.PROCESS_TYPE) String processType,
			@QueryParam(WPTAnalyticsConstants.GEOGRAPHY_LEVEL) String geographyLevel,
			@QueryParam(WPTAnalyticsConstants.GEOGRAPHY_ID) Integer geographyId,
			@QueryParam(WPTAnalyticsConstants.OPERATOR) String operator,
			@QueryParam(WPTAnalyticsConstants.TECHNOLOGY) String technology) {

		logger.info(
				"Going to get Trace Route and First DNS Resolution data for date {}, processType {}, geographyLevel {}, geographyId {}, operator {}, map {}, compareType {}, technology {}",
				geographyLevel, date, processType, geographyId, operator, map, compareType, technology);
		
		if (date != null && processType != null && (geographyId != null || operator != null) && map != null && compareType != null
				&& technology != null) {
			
			return Response.ok(service.getTTFBAndTDNSData(map, compareType, date, processType, geographyLevel, geographyId, operator,
					technology)).build();

		}

		return Response.ok(WPTAnalyticsConstants.INVALID_PARAMETER_JSON).build();
	}

	/**
	 * Gets the TTFB category wise data.
	 *
	 * @param map contain comparator i.e. either geography and operator with Ip Version and list of Location and Category Id
	 * @param compareType either GEOGRAPHY if  multiple geography or OPERATOR if multiple operator selected
	 * @param date : Date Selected
	 * @param processType : Aggregation Process Type i.e. Daily, Weekly or Monthly
	 * @param geographyLevel : Define Geography Level i.e. GeographyL1, GeographyL2, GeographyL3, GeographyL4
	 * @param geographyId : Id of the Geography Selected
	 * @param operator : Operator Selected
	 * @param technology : Technology Selected
	 * @return the TTFB category wise data
	 */
	@POST
	@Path("getTTFBCategoryWiseData")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTTFBCategoryWiseData(Map<String, List<Object>> map,
			@QueryParam(WPTAnalyticsConstants.COMPARE_TYPE) String compareType,
			@QueryParam(WPTAnalyticsConstants.DATE) String date,
			@QueryParam(WPTAnalyticsConstants.PROCESS_TYPE) String processType,
			@QueryParam(WPTAnalyticsConstants.GEOGRAPHY_LEVEL) String geographyLevel,
			@QueryParam(WPTAnalyticsConstants.GEOGRAPHY_ID) Integer geographyId,
			@QueryParam(WPTAnalyticsConstants.OPERATOR) String operator,
			@QueryParam(WPTAnalyticsConstants.TECHNOLOGY) String technology) {

		logger.info(
				"Going to get Trace Route and First DNS Resolution data for date {}, processType {}, geographyLevel {}, geographyId {}, operator {}, map {}, compareType {}, technology {}",
				geographyLevel, date, processType, geographyId, operator, map, compareType, technology);

		if (date != null && processType != null && (geographyId != null || operator != null) && map != null
				&& compareType != null && technology != null) {

			return Response.ok(service.getTTFBCategoryWiseData(map, compareType, date, processType, geographyLevel,
					geographyId, operator, technology)).build();
		}

		return Response.ok(WPTAnalyticsConstants.INVALID_PARAMETER_JSON).build();
	}

	/**
	 * Gets the geography analysis data.
	 *
	 * @param map contain comparator i.e. either geography and operator with Ip Version and list of Location and Category Id
	 * @param compareType either GEOGRAPHY if  multiple geography or OPERATOR if multiple operator selected
	 * @param date : Date Selected
	 * @param processType : Aggregation Process Type i.e. Daily, Weekly or Monthly
	 * @param geographyLevel : Define Geography Level i.e. GeographyL1, GeographyL2, GeographyL3, GeographyL4
	 * @param geographyId : Id of the Geography Selected
	 * @param operator : Operator Selected
	 * @param technology : Technology Selected
	 * @return the geography analysis data
	 */
	@POST
	@Path("getGeographyAnalysisData")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getGeographyAnalysisData(Map<String, List<Object>> map,
			@QueryParam(WPTAnalyticsConstants.COMPARE_TYPE) String compareType,
			@QueryParam(WPTAnalyticsConstants.DATE) String date,
			@QueryParam(WPTAnalyticsConstants.PROCESS_TYPE) String processType,
			@QueryParam(WPTAnalyticsConstants.GEOGRAPHY_LEVEL) String geographyLevel,
			@QueryParam(WPTAnalyticsConstants.GEOGRAPHY_ID) Integer geographyId,
			@QueryParam(WPTAnalyticsConstants.OPERATOR) String operator,
			@QueryParam(WPTAnalyticsConstants.TECHNOLOGY) String technology) {
		List<Object> compareTech = map.get(WPTAnalyticsConstants.COMPARE_IPV);
		List<Object> categories = map.get(WPTAnalyticsConstants.CATEGORY);
		List<Object> locations = map.get(WPTAnalyticsConstants.LOCATION);
		logger.info(
				"Going to get Geography Analysis Data for geographyLevel {}, date {}, processType {}, geographyId {}, compareTech {}, operator {}, categories {}, technology {},compareType {},locations{}",
				geographyLevel, date, processType, geographyId,compareTech, operator, categories, technology, compareType,locations);

		if (date != null && processType != null && geographyLevel!= null && (geographyId != null || operator != null)
				&& CollectionUtils.isNotEmpty(compareTech) && compareType != null && CollectionUtils.isNotEmpty(categories) 
				&& CollectionUtils.isNotEmpty(locations) && technology != null) {
			return Response.ok(service.getAnalysisGraphData(compareType, date, processType, geographyLevel, compareTech,
					geographyId, operator, categories, locations, technology)).build();
		}

		return Response.ok(WPTAnalyticsConstants.INVALID_PARAMETER_JSON).build();
	}

}

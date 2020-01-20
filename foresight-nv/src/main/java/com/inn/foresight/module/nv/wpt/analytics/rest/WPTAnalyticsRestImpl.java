package com.inn.foresight.module.nv.wpt.analytics.rest;

import java.util.Collections;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.report.utils.ReportConstants;
import com.inn.foresight.module.nv.reportgeneration.wrapper.NVReportWrapper;
import com.inn.foresight.module.nv.workorder.stealth.constants.StealthConstants;
import com.inn.foresight.module.nv.wpt.analytics.constants.WPTAnalyticsConstants;
import com.inn.foresight.module.nv.wpt.analytics.service.IWPTAnalyticsService;
import com.inn.foresight.module.nv.wpt.analytics.utils.WPTAnalyticsUtils;

/** The Class WPTAnalyticsRestImpl. */
@Path("/wptAnalytics")
@Service("WPTAnalyticsRestImpl")
public class WPTAnalyticsRestImpl {
	/** The logger. */
	private Logger logger = LogManager.getLogger(WPTAnalyticsRestImpl.class);

	/** Instance of WPTAnalyticsService. */
	@Autowired
	private IWPTAnalyticsService service;

	/**
	 * Getting list of all filters i.e. geography, operators, network and ipversions
	 *
	 * @param operator the operator
	 * @return Map of filterList
	 */
	@GET
	@Path("getFilterList")
	public Response getFilterList(@QueryParam(WPTAnalyticsConstants.OPERATOR) String operator) {
		try {
			logger.info("@method : getFilterList(), Going to filter for operator {}", operator);
			return Response.ok(service.getFilterList(operator)).build();
		} catch (Exception e) {
			logger.error(WPTAnalyticsConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			return Response.ok(Collections.emptyMap()).build();
		}
	}

	/**
	 * This method is used to WPT Pie Chart Data.
	 *
	 * @param geographyL3 - name of geographyL3 to search
	 * @param fromDate    - startTime
	 * @param toDate      - enndTime
	 * @param operatorA   - First Operator
	 * @param operatorB   - Second Operator
	 * @param networkA    - First NetworkType
	 * @param networkB    - Second NetworkType
	 * @param configA     - First Configuration
	 * @param configB     - Second Configuration
	 * @return Map of statistic JSON & categories average response time
	 */
	@GET
	@Path("getWPTPieChartData")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getWPTPieChartData(@QueryParam(WPTAnalyticsConstants.GEOGRAPHYL3) String geographyL3,
			@QueryParam(WPTAnalyticsConstants.FROMDATE) String fromDate,
			@QueryParam(WPTAnalyticsConstants.TODATE) String toDate,
			@QueryParam(WPTAnalyticsConstants.OPERATORA) String operatorA,
			@QueryParam(WPTAnalyticsConstants.OPERATORB) String operatorB,
			@QueryParam(WPTAnalyticsConstants.NETWORKA) String networkA,
			@QueryParam(WPTAnalyticsConstants.NETWORKB) String networkB,
			@QueryParam(WPTAnalyticsConstants.CONFIGA) String configA,
			@QueryParam(WPTAnalyticsConstants.CONFIGB) String configB) {
		logger.info(
				"@method : getWPTPieChartData(),  Going to get data for geographyL3 {}, fromDate {}, toDate {}, operatorA {}, operatorB {}, networkA {},  networkB {}, configA {}, configB {}",
				geographyL3, fromDate, toDate, operatorA, operatorB, networkA, networkB, configA, configB);

		try {
			if (geographyL3 != null && fromDate != null && toDate != null && operatorA != null && operatorB != null
					&& networkA != null && networkB != null && configA != null && configB != null) {
				return Response.ok(service.getWPTPieChartData(geographyL3, fromDate, toDate, operatorA, operatorB,
						networkA, networkB, configA, configB)).build();
			}
			return Response.ok(Collections.emptyMap()).build();
		} catch (Exception e) {
			logger.error(WPTAnalyticsConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			return Response.ok(Collections.emptyMap()).build();
		}
	}

	/**
	 * Getting Detailed Log Report showing Raw Data.
	 *
	 * @param geographyL3 - name of geographyL3 to search
	 * @param fromDate    - startTime
	 * @param toDate      - enndTime
	 * @param operatorA   - First Operator
	 * @param operatorB   - Second Operator
	 * @param networkA    - First NetworkType
	 * @param networkB    - Second NetworkType
	 * @param configA     - First Configuration
	 * @param configB     - Second Configuration
	 * @return Detailed Log Report CSV in bytes[]
	 */
	@GET
	@Path("getWPTDetailedLogReport")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getWPTDetailedLogReport(@QueryParam(WPTAnalyticsConstants.GEOGRAPHYL3) String geographyL3,
			@QueryParam(WPTAnalyticsConstants.FROMDATE) String fromDate,
			@QueryParam(WPTAnalyticsConstants.TODATE) String toDate,
			@QueryParam(WPTAnalyticsConstants.OPERATORA) String operatorA,
			@QueryParam(WPTAnalyticsConstants.OPERATORB) String operatorB,
			@QueryParam(WPTAnalyticsConstants.NETWORKA) String networkA,
			@QueryParam(WPTAnalyticsConstants.NETWORKB) String networkB,
			@QueryParam(WPTAnalyticsConstants.CONFIGA) String configA,
			@QueryParam(WPTAnalyticsConstants.CONFIGB) String configB) {
		logger.info(
				"@method : getWPTDetailedLogReport(),  Going to get data for geographyL3 {}, fromDate {}, toDate {}, operatorA {}, operatorB {},networkA {},  networkB {}, configA {}, configB {}",
				geographyL3, fromDate, toDate, operatorA, operatorB, networkA, networkB, configA, configB);

		try {
			if (geographyL3 != null && fromDate != null && toDate != null && operatorA != null && operatorB != null
					&& networkA != null && networkB != null && configA != null && configB != null) {

				return Response
						.ok(service.getWPTDetailedLogReport(geographyL3, fromDate, toDate, operatorA, operatorB,
								networkA, networkB, configA, configB))
						.header("Content-Type", "application/csv")
						.header("Content-Disposition",
								"attachment; filename=\""
										+ WPTAnalyticsUtils.getFileNameForReport(geographyL3, fromDate, toDate) + "\"")
						.build();
			}
			return Response.ok(WPTAnalyticsConstants.INVALID_PARAMETER_JSON).build();
		} catch (Exception e) {
			logger.error(WPTAnalyticsConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			return Response.ok(WPTAnalyticsConstants.EXCEPTION_SOMETHING_WENT_WRONG).build();
		}
	}

	/**
	 * This method is used to get WPT Detailed View Data.
	 *
	 * @param geographyL3   - name of geographyL3 to search
	 * @param fromDate      - startTime
	 * @param toDate        - enndTime
	 * @param operator      - Operator
	 * @param network       - NetworkType
	 * @param configuration - Configuration
	 * @param isSiteWise    - Site wise records require or category wise
	 * @param ulimit        the ulimit
	 * @param llimit        the llimit
	 * @return list Detailed View Wrapper
	 */
	@GET
	@Path("getWPTDetailedViewData")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getWPTDetailedViewData(@QueryParam(WPTAnalyticsConstants.GEOGRAPHYL3) String geographyL3,
			@QueryParam(WPTAnalyticsConstants.FROMDATE) String fromDate,
			@QueryParam(WPTAnalyticsConstants.TODATE) String toDate,
			@QueryParam(WPTAnalyticsConstants.OPERATOR) String operator,
			@QueryParam(WPTAnalyticsConstants.NETWORK) String network,
			@QueryParam(WPTAnalyticsConstants.CONFIGURATION) String configuration,
			@QueryParam(WPTAnalyticsConstants.ISSITEWISE) Boolean isSiteWise,
			@QueryParam(WPTAnalyticsConstants.ULIMIT) Integer ulimit,
			@QueryParam(WPTAnalyticsConstants.LLIMIT) Integer llimit) {
		logger.info(
				"@method : getWPTDetailedViewData(),  Going to get data for geographyL3 {}, fromDate {}, toDate {}, operator {}, network {}, configuration {},  isSiteWise {}, ulimit {}, llimit {}",
				geographyL3, fromDate, toDate, operator, network, configuration, isSiteWise, ulimit, llimit);

		try {
			if (geographyL3 != null && fromDate != null && toDate != null && operator != null && network != null
					&& configuration != null && isSiteWise != null) {

				return Response.ok(service.getWPTDetailedViewData(operator, network, configuration, fromDate, toDate,
						isSiteWise, geographyL3, ulimit, llimit)).build();
			}
			return Response.ok(Collections.emptyList()).build();
		} catch (Exception e) {
			logger.error(WPTAnalyticsConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			return Response.ok(Collections.emptyList()).build();
		}
	}

	/**
	 * This method is used to WPT Histogram Chart Data.
	 *
	 * @param geographyL3 - name of geographyL3 to search
	 * @param fromDate    - startTime
	 * @param toDate      - enndTime
	 * @param operatorA   - First Operator
	 * @param operatorB   - Second Operator
	 * @param networkA    - First NetworkType
	 * @param networkB    - Second NetworkType
	 * @param configA     - First Configuration
	 * @param configB     - Second Configuration
	 * @return Map of scatter plot JSON
	 */
	@GET
	@Path("getWPTHistogramData")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getWPTHistogramData(@QueryParam(WPTAnalyticsConstants.GEOGRAPHYL3) String geographyL3,
			@QueryParam(WPTAnalyticsConstants.FROMDATE) String fromDate,
			@QueryParam(WPTAnalyticsConstants.TODATE) String toDate,
			@QueryParam(WPTAnalyticsConstants.OPERATORA) String operatorA,
			@QueryParam(WPTAnalyticsConstants.OPERATORB) String operatorB,
			@QueryParam(WPTAnalyticsConstants.NETWORKA) String networkA,
			@QueryParam(WPTAnalyticsConstants.NETWORKB) String networkB,
			@QueryParam(WPTAnalyticsConstants.CONFIGA) String configA,
			@QueryParam(WPTAnalyticsConstants.CONFIGB) String configB) {
		logger.info(
				"@method : getWPTHistogramData(),  Going to get data for geographyL3 {}, fromDate {}, toDate {}, operatorA {}, operatorB {}, networkA {},  networkB {}, configA {}, configB {}",
				geographyL3, fromDate, toDate, operatorA, operatorB, networkA, networkB, configA, configB);

		try {
			if (geographyL3 != null && fromDate != null && toDate != null && operatorA != null && operatorB != null
					&& networkA != null && networkB != null && configA != null && configB != null) {

				return Response.ok(service.getWPTHistogramData(geographyL3, fromDate, toDate, operatorA, operatorB,
						networkA, networkB, configA, configB)).build();
			}
			return Response.ok(Collections.emptyMap()).build();
		} catch (Exception e) {
			logger.error(WPTAnalyticsConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			return Response.ok(Collections.emptyMap()).build();
		}
	}

	/**
	 * This method is used to get WPT Scatter Plot Data.
	 *
	 * @param geographyL3 - name of geographyL3 to search
	 * @param fromDate    - startTime
	 * @param toDate      - enndTime
	 * @param operatorA   - First Operator
	 * @param operatorB   - Second Operator
	 * @param networkA    - First NetworkType
	 * @param networkB    - Second NetworkType
	 * @param configA     - First Configuration
	 * @param configB     - Second Configuration
	 * @return Map of statistic plot JSON
	 */
	@GET
	@Path("getWPTScatterPlotData")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getWPTScatterPlotData(@QueryParam(WPTAnalyticsConstants.GEOGRAPHYL3) String geographyL3,
			@QueryParam(WPTAnalyticsConstants.FROMDATE) String fromDate,
			@QueryParam(WPTAnalyticsConstants.TODATE) String toDate,
			@QueryParam(WPTAnalyticsConstants.OPERATORA) String operatorA,
			@QueryParam(WPTAnalyticsConstants.OPERATORB) String operatorB,
			@QueryParam(WPTAnalyticsConstants.NETWORKA) String networkA,
			@QueryParam(WPTAnalyticsConstants.NETWORKB) String networkB,
			@QueryParam(WPTAnalyticsConstants.CONFIGA) String configA,
			@QueryParam(WPTAnalyticsConstants.CONFIGB) String configB) {
		logger.info(
				"@method : getWPTScatterPlotData(),  Going to get data for geographyL3 {}, fromDate {}, toDate {}, operatorA {}, operatorB {}, networkA {},  networkB {}, configA {}, configB {}",
				geographyL3, fromDate, toDate, operatorA, operatorB, networkA, networkB, configA, configB);

		try {
			if (geographyL3 != null && fromDate != null && toDate != null && operatorA != null && operatorB != null
					&& networkA != null && networkB != null && configA != null && configB != null) {

				return Response.ok(service.getWPTScatterPlotData(geographyL3, fromDate, toDate, operatorA, operatorB,
						networkA, networkB, configA, configB)).build();
			}
			return Response.ok(Collections.emptyMap()).build();
		} catch (Exception e) {
			logger.error(WPTAnalyticsConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			return Response.ok(Collections.emptyMap()).build();
		}
	}

	/**
	 * This method is use to get WPT Summary Data.
	 *
	 * @param fromDate - startTime
	 * @param toDate   - enndTime
	 * @param siteName - name of site to search optional
	 * @param ulimit   the ulimit
	 * @param llimit   the llimit
	 * @return List of WPTSummaryWrapper
	 */
	@GET
	@Path("getWPTResultSummaryData")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getWPTResultSummaryData(@QueryParam(WPTAnalyticsConstants.FROMDATE) String fromDate,
			@QueryParam(WPTAnalyticsConstants.TODATE) String toDate,
			@QueryParam(WPTAnalyticsConstants.SITENAME) String siteName,
			@QueryParam(WPTAnalyticsConstants.ULIMIT) Integer ulimit,
			@QueryParam(WPTAnalyticsConstants.LLIMIT) Integer llimit) {
		logger.info(
				"@method : getWPTResultSummaryData(), Going to get Data for fromDate {}, toDate {},siteName {}, ulimit {}, llimit {}",
				fromDate, toDate, siteName, ulimit, llimit);

		try {
			if (fromDate != null && toDate != null) {

				return Response.ok(service.getWPTResultSummaryData(fromDate, toDate, siteName, ulimit, llimit)).build();
			}
			return Response.ok(Collections.emptyList()).build();
		} catch (Exception e) {
			logger.error(WPTAnalyticsConstants.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			return Response.ok(Collections.emptyList()).build();
		}
	}

	/**
	 * Gets the WPT dump report from hbase.
	 * 
	 * @param woTestWrapper the wo test wrapper
	 * @return the WPT dump report from hbase
	 * @throws Exception the exception
	 */
	@GET
	@Path("getWPTDumpReportFromHbase")
	public Response getWPTDumpReportFromHbase(@QueryParam(StealthConstants.DEVICE_ID) String deviceId,
			@QueryParam(StealthConstants.TEST_ID) Integer testId,
			@QueryParam(StealthConstants.TEST_START_TIME) Long testStartTime,
			@QueryParam(StealthConstants.TEST_END_TIME) Long testEndTime) throws Exception {
		logger.info("Going to create WPT Dump Report");
		try {
			Response.ResponseBuilder builder = Response.status(200);
			NVReportWrapper reportWrapper = service.getWPTDumpReportFromHbase(deviceId, testId, testStartTime,
					testEndTime);

			builder = builder.entity(reportWrapper.getFile())
					.header(ReportConstants.CONTENT_TYPE, ReportConstants.CONTENT_TYPE_CSV)
					.header(ReportConstants.CONTENT_DISPOSITION,
							"attachment; filename=\"" + reportWrapper.getReportName() + "\"")
					.header("filename", reportWrapper.getReportName());

			return builder.build();
		} catch (Exception e) {
			logger.error("Error while creating WPT Dump Report: {}", Utils.getStackTrace(e));
			return Response.ok(ForesightConstants.FAILURE_JSON).build();
		}

	}

	@GET
	@Path("categoryWiseSites")
	public Response getCategoryWiseSites() {

		try {
		return Response.ok(service.getCategoryWiseSites()).build();
		}
		catch(Exception e) {
			return Response.ok(ForesightConstants.FAILURE_JSON).build();
		}
	}
}

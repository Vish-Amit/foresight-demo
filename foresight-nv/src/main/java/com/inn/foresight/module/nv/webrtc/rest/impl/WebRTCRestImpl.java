package com.inn.foresight.module.nv.webrtc.rest.impl;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.commons.configuration.ConfigUtils;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.NVConfigUtil;
import com.inn.foresight.module.nv.NVConstant;
import com.inn.foresight.module.nv.layer3.constants.NVLayer3Constants;
import com.inn.foresight.module.nv.webrtc.constant.WebRTCConstant;
import com.inn.foresight.module.nv.webrtc.rest.IWebRTCRest;
import com.inn.foresight.module.nv.webrtc.service.IWebRTCService;
import com.inn.foresight.module.nv.webrtc.wrapper.CallStatsWrapper;
import com.inn.foresight.module.nv.webrtc.wrapper.WebRTCViberDashBoardDataWrapper;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;

@Path("/WebRTC")
@Service("WebRTCRestImpl")
@Produces(MediaType.APPLICATION_JSON)
public class WebRTCRestImpl implements IWebRTCRest {
	private Logger logger = LogManager.getLogger(WebRTCRestImpl.class);

	@Autowired
	IWebRTCService iWebRTCService;

	@GET
	@Path("getDataByIMSI")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response getDataByIMSI(@QueryParam("deviceId") String deviceId, @QueryParam("startDate") String startDate,
			@QueryParam("endDate") String endDate) throws IOException {
		logger.info("inside the method getDataByIMSI imsi {}", deviceId);
		try {
			if (Utils.hasValidValue(deviceId)) {
				String summaryDataWrapper = iWebRTCService.getDataByIMSI(deviceId, startDate, endDate);
				if (summaryDataWrapper != null) {
					return Response.ok(summaryDataWrapper).build();
				} else {
					return Response.ok(NVWorkorderConstant.NO_DATA_FOUND_JSON).build();
				}
			} else {
				return Response.ok(NVWorkorderConstant.INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error("Exception inside the method getDataByIMSI imsi {} trace {}", deviceId,
					Utils.getStackTrace(e));
		}
		return Response.ok(NVWorkorderConstant.FAILURE_JSON).build();
	}

	@GET
	@Path("getCallDataByCallToken")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response getCallDataByCallToken(@QueryParam("callToken") String callToken,
			@Context HttpServletRequest request) {
		logger.info("Inside the method getCallDataByCallToken callToken{}", callToken);
		try {
			if (Utils.hasValidValue(callToken)) {
				String callData = iWebRTCService.getCallDataByCallToken(callToken, request);
				if (callData != null) {
					return Response.ok(callData).build();
				} else {
					return Response.ok(NVWorkorderConstant.NO_DATA_FOUND_JSON).build();
				}
			} else {
				return Response.ok(NVWorkorderConstant.INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error("Error Inside the method getCallDataByCallToken callToken {},Exception ", callToken,
					Utils.getStackTrace(e));
		}
		return Response.ok(NVWorkorderConstant.FAILURE_JSON).build();

	}

	@GET
	@Path("getViberDashboardCallData")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response getViberDashboardData(@QueryParam("startDate") String startDate,
										  @QueryParam("endDate") String endDate, @QueryParam("technology") String technology,
										  @QueryParam("operator") String operator, @QueryParam("country") String country, @QueryParam("nvModule") String nvModule){
		logger.info(
				"Inside the method getViberDashboardData startDate {} ,endDate {}, technology {}, operator {} ,country {}, nvModule {}",
				startDate, endDate, technology, operator, country, nvModule);
		try {
			if (Utils.hasValidValue(startDate) && Utils.hasValidValue(endDate) && Utils.hasValidValue(technology)
					&& Utils.hasValidValue(operator) && Utils.hasValidValue(country)) {
				WebRTCViberDashBoardDataWrapper callData = iWebRTCService.getViberDashboardCallData(startDate, endDate,
						technology, operator, country, nvModule);
				if (callData != null) {
					return Response.ok(callData).build();
				} else {
					return Response.ok(NVWorkorderConstant.NO_DATA_FOUND_JSON).build();
				}
			} else {
				return Response.ok(NVWorkorderConstant.INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error(
					"Error Inside the method getViberDashboardData date {}, date {} , technology {}, operator {} ,country {} Exception ",
					startDate, technology, operator, country, Utils.getStackTrace(e));
		}
		return Response.ok(NVWorkorderConstant.FAILURE_JSON).build();

	}

	@GET
	@Path("getkpiDistributionData")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response getKpiDistribution(@QueryParam("startDate") String startDate, @QueryParam("endDate") String endDate,
			@QueryParam("technology") String technology, @QueryParam("operator") String operator,
			@QueryParam("country") String country, @QueryParam("nvModule") String nvModule) {
		logger.info(
				"Inside the method getKpiDistribution startDate {} ,endDate {}, technology {}, operator {} ,country {}",
				startDate, endDate, technology, operator, country);
		try {
			if (Utils.hasValidValue(startDate) && Utils.hasValidValue(endDate) && Utils.hasValidValue(technology)
					&& Utils.hasValidValue(operator) && Utils.hasValidValue(country)) {
				WebRTCViberDashBoardDataWrapper kpiData = iWebRTCService.getKpiDistribution(startDate, endDate,
						technology, operator, country, nvModule);
				if (kpiData != null) {
					return Response.ok(kpiData).build();
				} else {
					return Response.ok(NVWorkorderConstant.NO_DATA_FOUND_JSON).build();
				}
			} else {
				return Response.ok(NVWorkorderConstant.INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error(
					"Error Inside the method getKpiDistribution date {}, date {} , technology {}, operator {} ,country {} Exception ",
					startDate, technology, operator, country, Utils.getStackTrace(e));
		}
		return Response.ok(NVWorkorderConstant.FAILURE_JSON).build();

	}

	@GET
	@Path("getLastSevenDaysData")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response getLastSevenDaysData(@QueryParam("startDate") String startDate,
			@QueryParam("endDate") String endDate, @QueryParam("technology") String technology,
			@QueryParam("operator") String operator, @QueryParam("country") String country, @QueryParam("nvModule") String nvModule) {
		logger.info(
				"Inside the method getLastSevenDaysData startDate {} ,endDate {}, technology {}, operator {} ,country {}",
				startDate, endDate, technology, operator, country);
		try {
			if (Utils.hasValidValue(startDate) && Utils.hasValidValue(endDate) && Utils.hasValidValue(technology)
					&& Utils.hasValidValue(operator) && Utils.hasValidValue(country)) {
				Map<String, CallStatsWrapper> kpiData = iWebRTCService.processLastSevenDaysData(startDate, endDate,
						technology, operator, country, nvModule);
				if (kpiData != null) {
					return Response.ok(kpiData).build();
				} else {
					return Response.ok(NVWorkorderConstant.NO_DATA_FOUND_JSON).build();
				}
			} else {
				return Response.ok(NVWorkorderConstant.INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error(
					"Error Inside the method getLastSevenDaysData date {}, date {} , technology {}, operator {} ,country {} Exception ",
					startDate, technology, operator, country, Utils.getStackTrace(e));
		}
		return Response.ok(NVWorkorderConstant.FAILURE_JSON).build();
	}

	@GET
	@Path("/getWRTCZipFile")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Override
	public Response downloadFile(@QueryParam("callToken") String callToken, @QueryParam("callType") String callType, @QueryParam("isRCS") Boolean isRCS)
			throws IOException {
		try {
			byte[] fileFromHDFS = iWebRTCService.getFileFromHDFS(callToken, callType, isRCS);

			String outputFileName = ConfigUtils.getString(NVConfigUtil.WEBRTC_DOWNLOAD_FILE_NAME);
			outputFileName = outputFileName + callType + WebRTCConstant.ZIP_EXTENSION;

			logger.info("ouput file name  {}", outputFileName);

			Response.ResponseBuilder builder = Response.status(200);

			builder = builder.entity(fileFromHDFS)
					.header(ForesightConstants.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM)
					.header(ForesightConstants.CONTENT_DISPOSITION,
							NVLayer3Constants.ATTACHMENT_FILE + outputFileName + "\"");
			return builder.build();

		} catch (Exception e) {
			logger.error("Error while returning " + e.getClass() + " | " + e.getMessage());
		}

		return Response.ok("File Not Found").build();
	}

	@GET
	@Path("/getViberLayer")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response getViberLayer(@QueryParam("NELat") Double nELat, @QueryParam("NELng") Double nELng,
			@QueryParam("SWLat") Double sWLat, @QueryParam("SWLng") Double sWLng, @QueryParam("zoomLevel") Integer zoom,
			@QueryParam("mediaType") String mediaType, @QueryParam("callDirection") String callDirection,
			@QueryParam("releaseType") String releaseType, @QueryParam("os") String os,
			@QueryParam("startTime") Long startTime, @QueryParam("endTime") Long endTime,
			@QueryParam("minValue") String minValue, @QueryParam("maxValue") String maxValue,
			@QueryParam("layerType") String layerType) throws ParseException {

		if (Utils.hasValue(nELat) && Utils.hasValue(nELng) && Utils.hasValue(sWLat) && Utils.hasValue(sWLng)
				&& zoom != null) {
			Map<String, ?> layerData = iWebRTCService.getLayerData(nELat, nELng, sWLat, sWLng, zoom, mediaType,
					callDirection, releaseType, os, startTime, endTime, minValue, maxValue, layerType);

			if (layerData != null) {
				return Response.ok(layerData).build();
			} else {
				return Response.ok(NVConstant.DATA_NOT_FOUND).build();
			}
		} else {
			return Response.ok(NVConstant.INVALID_PARAM).build();
		}

	}

}

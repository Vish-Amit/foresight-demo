package com.inn.foresight.module.nv.webrtc.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.webrtc.service.IWebRTCService;
import com.inn.foresight.module.nv.webrtc.wrapper.WebRTCCallStatsWrapper;
import com.inn.foresight.module.nv.webrtc.wrapper.WebRTCDeviceSummaryWrapper;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;

@Path("/ms/WebRTC")
@Service("WebRTCMicroServiceRestImpl")
@Produces(MediaType.APPLICATION_JSON)
public class WebRTCMicroServiceRestImpl {
	private Logger logger = LogManager.getLogger(WebRTCMicroServiceRestImpl.class);
	@Autowired
	IWebRTCService iWebRTCService;

	@GET
	@Path("getDataByDeviceId")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDataByDeviceId(@QueryParam("deviceId") String deviceId,
			@QueryParam("startDate") String startDate, @QueryParam("endDate") String endDate) {
		logger.info("inside the method getDataByDeviceId deviceId {} startDate {},endDate{}", deviceId, startDate,
				endDate);
		if (Utils.hasValidValue(deviceId)) {
			WebRTCDeviceSummaryWrapper summaryDataWrapper = iWebRTCService
					.getDeviceSummaryDataByDeviceId(deviceId, startDate, endDate);
			if (summaryDataWrapper != null) {
				return Response.ok(summaryDataWrapper).build();
			} else {
				return Response.ok(NVWorkorderConstant.NO_DATA_FOUND_JSON).build();
			}
		} else {
			return Response.ok(NVWorkorderConstant.INVALID_PARAMETER_JSON).build();
		}

	}

	@GET
	@Path("getCallDataByCallToken")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCallDataByCallToken(@QueryParam("callToken") String callToken) {
		logger.info("inside the method getCallDataByCallToken callToken {}", callToken);
		if (Utils.hasValidValue(callToken)) {
			WebRTCCallStatsWrapper callData = iWebRTCService.getWebRtcDataByCallToken(callToken);
			if (callData != null) {
				return Response.ok(callData).build();
			} else {
				return Response.ok(NVWorkorderConstant.NO_DATA_FOUND_JSON).build();
			}
		} else {
			return Response.ok(NVWorkorderConstant.INVALID_PARAMETER_JSON).build();
		}

	}

}

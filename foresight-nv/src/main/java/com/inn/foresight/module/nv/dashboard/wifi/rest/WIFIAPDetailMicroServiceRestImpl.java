package com.inn.foresight.module.nv.dashboard.wifi.rest;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.dashboard.wifi.service.IWIFIAPDetailService;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;

@Path("/ms/WIFIAPDashboard")
@Produces(ForesightConstants.APPLICATION_SLASH_JSON)
@Consumes(ForesightConstants.APPLICATION_SLASH_JSON)
public class WIFIAPDetailMicroServiceRestImpl {

	private Logger logger = LogManager.getLogger(WIFIAPDetailMicroServiceRestImpl.class);

	@Autowired
	IWIFIAPDetailService iWifiAPSerivce;

	@GET
	@Path("/floorPerformance")
	public Response getFloorPerformanceData(@QueryParam("floorId") Integer floorId,
			@QueryParam("startDate") String startDate) {
		logger.info("Found the floorId {} ", floorId);
		if (Utils.hasValue(floorId) && Utils.hasValidValue(startDate)) {
			return Response.ok(iWifiAPSerivce.getFloorPerformanceDataFromHbase(floorId, startDate)).build();
		} else {
			return Response.ok(NVWorkorderConstant.INVALID_PARAMETER_JSON).build();
		}
	}

	@POST
	@Path("/floorWiseAPData")
	public Response getFloorWiseAPData(@QueryParam("floorId") Integer floorId,
			@QueryParam("startDate") String startDate, Map<String,String> macAddressMap) {
		logger.info("Found the floorId {} startDate {} mac Address List Size {}", floorId, startDate,
				macAddressMap.size());
		if (Utils.hasValue(floorId)) {
			return Response.ok(iWifiAPSerivce.getFloorWiseAPDataFromHbase(floorId, startDate, macAddressMap)).build();
		} else {
			return Response.ok(NVWorkorderConstant.INVALID_PARAMETER_JSON).build();
		}
	}

	@GET
	@Path("/apPerformance")
	public Response getAPPerformanceData(@QueryParam("floorId") Integer floorId,
			@QueryParam("macAddress") String macAddress, @QueryParam("startDate") String startDate,
			@QueryParam("hour") String hour) {

		return Response.ok(iWifiAPSerivce.getAPPerformanceFromHbase(floorId, macAddress, startDate, hour)).build();

	}

}

package com.inn.foresight.module.nv.dashboard.wifi.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.dashboard.wifi.service.IWIFIAPDetailService;
import com.inn.foresight.module.nv.dashboard.wifi.wrapper.APDetailWrapper;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;

@Path("/WIFIAPDashboard")
@Produces(ForesightConstants.APPLICATION_SLASH_JSON)
@Consumes(ForesightConstants.APPLICATION_SLASH_JSON)
@Service("WIFIAPDetailRestImpl")
public class WIFIAPDashboardRestImpl {

	private Logger logger = LogManager.getLogger(WIFIAPDashboardRestImpl.class);

	@Autowired
	IWIFIAPDetailService iWifiAPSerivce;

	/**
	 * @param buildingId
	 * @return
	 */
	@GET
	@Path("/floorByBuilding")
	public Response getFloorWiseData(@QueryParam("buildingId") Integer buildingId) {
		logger.info("Found the buildingId {}", buildingId);
		if (Utils.hasValue(buildingId)) {
			return Response.ok(iWifiAPSerivce.getFloorDataForBuilding(buildingId)).build();
		} else {
			return Response.ok(NVWorkorderConstant.INVALID_PARAMETER_JSON).build();
		}
	}

	@GET
	@Path("/apDetail")
	public APDetailWrapper getAPDetail(@QueryParam("macAddress") String macAddress) {
		logger.info("mac Address {}", macAddress);
		if (Utils.hasValidValue(macAddress)) {
			return iWifiAPSerivce.getAPDetail(macAddress);
		}
		return new APDetailWrapper();

	}

	/**
	 * @param floorId
	 * @param startDate
	 * @return
	 */
	@GET
	@Path("/floorPerformance")
	public Response getFloorPerformanceDashboardData(@QueryParam("floorId") Integer floorId,
			@QueryParam("startDate") String startDate) {
		logger.info("Found the floorId {} and startDate {}", floorId, startDate);
		if (Utils.hasValue(floorId) && Utils.hasValidValue(startDate)) {
			return Response.ok(iWifiAPSerivce.getFloorPerformance(floorId, startDate)).build();
		} else {
			return Response.ok(NVWorkorderConstant.INVALID_PARAMETER_JSON).build();
		}
	}

	@GET
	@Path("/floorStats")
	public Response getFloorStats(@QueryParam("floorId") Integer floorId, @QueryParam("startDate") String startDate) {
		logger.info("Found the floorId {} and startDate {}", floorId, startDate);
		if (Utils.hasValue(floorId) && Utils.hasValidValue(startDate)) {
			return Response.ok(iWifiAPSerivce.getFloorStats(floorId, startDate)).build();
		} else {
			return Response.ok(NVWorkorderConstant.INVALID_PARAMETER_JSON).build();
		}
	}

	/**
	 * @param macAddress
	 * @param startDate
	 * @return
	 */

	@GET
	@Path("/apPerformance")
	public Response getAPPerformanceDashboardData(@QueryParam("floorId") Integer floorId,
			@QueryParam("macAddress") String macAddress, @QueryParam("startDate") String startDate,
			@QueryParam("hour") String hour) {
		logger.info("Found the FloorId {} Mac address {} and  date {} and hour {}", floorId, macAddress, startDate,
				hour);
		if (Utils.hasValue(macAddress) && Utils.hasValue(floorId) && Utils.hasValue(startDate)) {
			return Response.ok(iWifiAPSerivce.getAPPerformance(floorId, macAddress, startDate, hour)).build();
		} else {
			return Response.ok(NVWorkorderConstant.INVALID_PARAMETER_JSON).build();
		}
	}

}

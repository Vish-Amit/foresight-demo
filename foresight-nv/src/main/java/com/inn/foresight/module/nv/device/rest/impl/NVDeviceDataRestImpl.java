package com.inn.foresight.module.nv.device.rest.impl;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.commons.lang.NumberUtils;
import com.inn.commons.lang.StringUtils;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.device.constant.DeviceConstant;
import com.inn.foresight.module.nv.device.model.DeviceDashboard;
import com.inn.foresight.module.nv.device.rest.INVDeviceDataRest;
import com.inn.foresight.module.nv.device.service.DeviceDashboardService;
import com.inn.foresight.module.nv.device.service.INVDeviceDataService;
import com.inn.foresight.module.nv.device.util.NVDeviceUtil;
import com.inn.foresight.module.nv.device.wrapper.DeviceDashboardWrapper;
import com.inn.foresight.module.nv.device.wrapper.NVDeviceDataWrapper;
import com.inn.foresight.module.nv.device.wrapper.NVDeviceWrapper;
import com.inn.foresight.module.nv.report.utils.ReportConstants;
import com.inn.foresight.module.nv.reportgeneration.wrapper.NVReportWrapper;

/** The Class NVDeviceDataRestImpl. */
@Path("/NVDeviceManager")
@Service("NVDeviceDataRestImpl")
public class NVDeviceDataRestImpl extends NVDeviceUtil implements INVDeviceDataRest {

	/** The logger. */
	private Logger logger = LogManager.getLogger(NVDeviceDataRestImpl.class);

	/** The i NV device data service. */
	@Autowired
	INVDeviceDataService iNVDeviceDataService;

	@Autowired
	DeviceDashboardService deviceDashboardService;

	/**
	 * Gets the all device list.
	 *
	 * @param lLimit      the l limit
	 * @param noOfRecords the no of records
	 * @param operator    the operator
	 * @return the all device list
	 */
	@GET
	@Path("getAllDeviceList")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response getAllDeviceList(@QueryParam("lLimit") Integer lLimit,
			@QueryParam("noOfRecords") Integer noOfRecords, @QueryParam("operator") String operator) {
		List<NVDeviceDataWrapper> nvDeviceList = null;
		Response responseToReturn;
		logger.info("Going to getAllDeviceList lLimit{},noOfRecords{},operator{}", lLimit, noOfRecords, operator);
		try {
			nvDeviceList = iNVDeviceDataService.getAllDeviceList(lLimit, noOfRecords, operator);
			responseToReturn = Response.ok(nvDeviceList).build();
		} catch (RestException e) {
			logger.error("Error while getting NVdevice list  :{} ", ExceptionUtils.getMessage(e));
			responseToReturn = Response.ok(DeviceConstant.EXCEPTION_SOMETHING_WENT_WRONG).build();
		}
		return responseToReturn;
	}

	/**
	 * Gets the device list count.
	 *
	 * @param operator the operator
	 * @return the device list count
	 */
	@GET
	@Path("getDeviceListCount")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response getDeviceListCount(@QueryParam("operator") String operator) {
		long deviceCount;
		Response responseToReturn;
		try {
			if (StringUtils.isNotBlank(operator)) {
				deviceCount = iNVDeviceDataService.getDeviceCount(operator);
				responseToReturn = Response.ok(deviceCount).build();
			} else {
				logger.warn("Warning in getDeviceListCount operator {} ", operator);
				responseToReturn = Response.ok(DeviceConstant.INVALID_PARAMETER_JSON).build();
			}
		} catch (RestException e) {
			logger.error("Error while getting device list count :{} ", ExceptionUtils.getMessage(e));
			responseToReturn = Response.ok(DeviceConstant.EXCEPTION_SOMETHING_WENT_WRONG).build();
		}
		return responseToReturn;
	}

	/**
	 * Gets the device details by device id.
	 *
	 * @param deviceId the device id
	 * @return the device details by device id
	 */
	@GET
	@Path("getDeviceDetailsByDeviceId/{deviceId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response getDeviceDetailsByDeviceId(@PathParam("deviceId") String deviceId) {
		Response responseToReturn;
		logger.info("Going to getDeviceDetailsByDeviceId deviceId {} ", deviceId);
		try {
			if (!StringUtils.isBlank(deviceId) && Utils.isNotNull(deviceId)) {
				responseToReturn = Response.ok(iNVDeviceDataService.getDeviceDetailsByDeviceId(deviceId)).build();
			} else {
				responseToReturn = Response.ok(DeviceConstant.INVALID_PARAMETER_JSON).build();
			}
		} catch (RestException e) {
			logger.error("Error while getting device Details by device id:{} ", ExceptionUtils.getMessage(e));
			responseToReturn = Response.ok(DeviceConstant.EXCEPTION_SOMETHING_WENT_WRONG).build();
		}
		return responseToReturn;
	}

	/**
	 * Gets the device inside cluster.
	 *
	 * @param northEastLat  the north east lat
	 * @param northEastLong the north east long
	 * @param southWestLat  the south west lat
	 * @param southWestLong the south west long
	 * @param operator      the operator
	 * @param geographyId   the geography id
	 * @return the device inside cluster
	 */
	@GET
	@Path("getDeviceInsideViewPort")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response getDeviceInsideViewPort(@QueryParam("NELat") Double northEastLat,
			@QueryParam("NELng") Double northEastLong, @QueryParam("SWLat") Double southWestLat,
			@QueryParam("SWLng") Double southWestLong, @QueryParam("operator") String operator,
			@QueryParam(GEOGRAPHY_ID) String geographyId, @QueryParam("userType") String userType) {
		Response responseToReturn;
		logger.info(
				"Going to getDeviceInsideViewPort NELat {} ,NELng {} ,SWLat {} ,SWLng {} ,operator{} ,geographyId{},userType{}",
				northEastLat, northEastLong, southWestLat, southWestLong, operator, geographyId, userType);
		try {
			if ((NumberUtils.isValidNumber(northEastLat) && NumberUtils.isValidNumber(northEastLong))
					&& (NumberUtils.isValidNumber(southWestLat) && NumberUtils.isValidNumber(southWestLong))
					&& (StringUtils.isNotBlank(operator))) {
				responseToReturn = Response.ok(iNVDeviceDataService.getDeviceInsideViewPort(northEastLat, northEastLong,
						southWestLat, southWestLong, operator, geographyId, userType)).build();
			} else {
				responseToReturn = Response.ok(DeviceConstant.INVALID_PARAMETER_JSON).build();
			}
		} catch (RestException e) {
			logger.error("Error while getting devices inside viewport:{} ", ExceptionUtils.getMessage(e));
			responseToReturn = Response.ok(DeviceConstant.EXCEPTION_SOMETHING_WENT_WRONG).build();
		}
		return responseToReturn;
	}

	/**
	 * Gets the device list inside cluster.
	 *
	 * @param cluster  the cluster
	 * @param operator the operator
	 * @return the device list inside cluster
	 */
	@GET
	@Path("getDeviceListInsideCluster")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response getDeviceListInsideCluster(@QueryParam("cluster") String cluster,
			@QueryParam("operator") String operator, @QueryParam("userType") String userType) {
		Response responseToReturn;
		logger.warn("Going to getDeviceListInsideCluster cluster {} ,operator {} ,userType {}", cluster, operator,
				userType);
		try {
			if (StringUtils.isNotBlank(operator) && StringUtils.isNotBlank(cluster)) {
				responseToReturn = Response
						.ok(iNVDeviceDataService.getDeviceListInsideCluster(cluster, operator, userType)).build();
			} else {
				responseToReturn = Response.ok(DeviceConstant.INVALID_PARAMETER_JSON).build();
			}
		} catch (RestException e) {
			logger.error("Error while getting devices inside cluster:{} ", ExceptionUtils.getMessage(e));
			responseToReturn = Response.ok(DeviceConstant.EXCEPTION_SOMETHING_WENT_WRONG).build();
		}
		return responseToReturn;
	}

	/**
	 * Gets the device list for WO.
	 *
	 * @param woStartTime    the wo start time
	 * @param geographyLevel the geography level
	 * @param geographyId    the geography id
	 * @param lLimit         the l limit
	 * @param uLimit         the u limit
	 * @return the device list for WO
	 */
	@GET
	@Path("getDeviceListForWO")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response getDeviceListForWO(@QueryParam(WO_START_TIME) Long woStartTime,
			@QueryParam(GEOGRAPHY_LEVEL) String geographyLevel, @QueryParam(GEOGRAPHY_ID) Integer geographyId,
			@QueryParam(LOWER_LIMIT) Integer lLimit, @QueryParam(UPPER_LIMIT) Integer uLimit) {
		ResponseBuilder builder = null;
		logger.info(
				"Going to getDeviceListForWO startTime : {} geographyLevel : {} geographyId : {} uLimit : {} lLimit : {}",
				woStartTime, geographyLevel, geographyId, uLimit, lLimit);
		try {
			if (StringUtils.isNotBlank(geographyLevel) && woStartTime != null && geographyId != null
					&& StringUtils.isNotEmpty(geographyLevel)) {
				List<NVDeviceDataWrapper> deviceDatas = iNVDeviceDataService.getDeviceListForWO(woStartTime,
						geographyLevel, geographyId, lLimit, uLimit);
				logger.info("Done getDeviceListForWO");
				builder = Response.ok(deviceDatas);
			} else {
				logger.info(INVALID_PARAMETER_JSON);
				builder = Response.ok(INVALID_PARAMETER_JSON).status(Status.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			logger.error("Exception in getDeviceListForWO : {} ", ExceptionUtils.getStackTrace(e));
			builder = Response.ok(FAILURE_JSON).status(Status.INTERNAL_SERVER_ERROR);
		}
		return builder.build();
	}

	/**
	 * Gets the device list count.
	 *
	 * @param woStartTime    the wo start time
	 * @param geographyLevel the geography level
	 * @param geographyId    the geography id
	 * @return the device list count
	 */
	@GET
	@Path("getDeviceListCountForWO")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response getDeviceListCountForWO(@QueryParam(WO_START_TIME) Long woStartTime,
			@QueryParam(GEOGRAPHY_LEVEL) String geographyLevel, @QueryParam(GEOGRAPHY_ID) Integer geographyId) {
		Long deviceCount;
		Response responseToReturn;
		try {
			deviceCount = iNVDeviceDataService.getDeviceCountListForWO(woStartTime, geographyLevel, geographyId);
			responseToReturn = Response.ok(deviceCount).build();
		} catch (RestException e) {
			logger.error("Error while getting device list count for WO :{} ", ExceptionUtils.getMessage(e));
			responseToReturn = Response.ok(DeviceConstant.EXCEPTION_SOMETHING_WENT_WRONG).build();
		}
		return responseToReturn;
	}

	/**
	 * Search NV devices.
	 *
	 * @param northEastLong the north east long
	 * @param northEastLat  the north east lat
	 * @param southWestLong the south west long
	 * @param southWestLat  the south west lat
	 * @param zoomLevel     the zoom level
	 * @param operator      the operator
	 * @return the response
	 */
	@GET
	@Path("searchNVDevices")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response searchNVDevices(@QueryParam("NELat") Double northEastLat, @QueryParam("NELng") Double northEastLong,
			@QueryParam("SWLat") Double southWestLat, @QueryParam("SWLng") Double southWestLong,
			@QueryParam("zoomLevel") Integer zoomLevel, @QueryParam("operator") String operator,
			@QueryParam("userType") String userType) {
		logger.info(
				"Going to searchNVDevices NELat {} ,NELng {} ,SWLat {} ,SWLng {} ,operator{} ,zoomLevel{},userType {}",
				northEastLat, northEastLong, southWestLat, southWestLong, operator, zoomLevel, userType);
		Response responseToReturn;
		try {

			if (zoomLevel >= DeviceConstant.ZOOM_LEVEL_16 && zoomLevel <= DeviceConstant.ZOOM_LEVEL_18) {
				if ((NumberUtils.isValidNumber(northEastLat) && NumberUtils.isValidNumber(northEastLong))
						&& (NumberUtils.isValidNumber(southWestLat) && NumberUtils.isValidNumber(southWestLong))
						&& (StringUtils.isNotBlank(operator) && Utils.isNotNull(zoomLevel))) {
					responseToReturn = Response.ok(iNVDeviceDataService.searchNVDeviceDetail(northEastLat,
							northEastLong, southWestLat, southWestLong, operator, zoomLevel, userType)).build();
				} else {
					responseToReturn = Response.ok(DeviceConstant.INVALID_PARAMETER_JSON).build();
				}
			} else {
				responseToReturn = Response.ok(iNVDeviceDataService.searchNVDevices(operator, zoomLevel, userType))
						.build();
			}

		} catch (RestException e) {
			logger.error("Error while searchNVDevices:{} ", ExceptionUtils.getMessage(e));
			responseToReturn = Response.ok(DeviceConstant.EXCEPTION_SOMETHING_WENT_WRONG).build();
		}
		return responseToReturn;
	}

//Unused FIXME
	/**
	 * Gets the device count for view port.
	 *
	 * @param northEastLat  the north east lat
	 * @param northEastLong the north east long
	 * @param southWestLat  the south west lat
	 * @param southWestLong the south west long
	 * @param zoomLevel     the zoom level
	 * @param operator      the operator
	 * @return the device count for view port
	 */
	@GET
	@Path("getDeviceCountForViewPort")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response getDeviceCountForViewPort(@QueryParam("NELat") Double northEastLat,
			@QueryParam("NELng") Double northEastLong, @QueryParam("SWLat") Double southWestLat,
			@QueryParam("SWLng") Double southWestLong, @QueryParam("zoomLevel") Integer zoomLevel,
			@QueryParam("operator") String operator, @QueryParam("userType") String userType) {
		Response responseToReturn;
		logger.info(
				"Going to getDeviceCountForViewPort NELat {} ,NELng {} ,SWLat {} ,SWLng {} ,operator {} ,zoomLevel {}  ",
				northEastLat, northEastLong, southWestLat, southWestLong, operator, zoomLevel);
		try {
			if ((Utils.isNotNull(zoomLevel) && NumberUtils.isValidNumber(northEastLat))
					&& (NumberUtils.isValidNumber(northEastLong) && NumberUtils.isValidNumber(southWestLat))
					&& (NumberUtils.isValidNumber(southWestLong) && StringUtils.isNotBlank(operator))) {
				responseToReturn = Response.ok(iNVDeviceDataService.getDeviceCountForViewPort(northEastLat,
						northEastLong, southWestLat, southWestLong, zoomLevel, operator, userType)).build();
			} else {

				responseToReturn = Response.ok(DeviceConstant.INVALID_PARAMETER_JSON).build();
			}
		} catch (RestException e) {
			logger.error("Error while getting device count for view port :{} ", ExceptionUtils.getMessage(e));
			responseToReturn = Response.ok(DeviceConstant.EXCEPTION_SOMETHING_WENT_WRONG).build();
		}
		return responseToReturn;
	}

	/**
	 * Gets the device count for table view.
	 *
	 * @param northEastLat   the north east lat
	 * @param northEastLong  the north east long
	 * @param southWestLat   the south west lat
	 * @param southWestLong  the south west long
	 * @param operator       the operator
	 * @param geographyLevel the geography level
	 * @param geographyId    the geography id
	 * @return the device count for table view
	 */
	@GET
	@Path("getDeviceCountForTableView")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response getDeviceCountForTableView(@QueryParam("NELat") Double northEastLat,
			@QueryParam("NELng") Double northEastLong, @QueryParam("SWLat") Double southWestLat,
			@QueryParam("SWLng") Double southWestLong, @QueryParam("operator") String operator,
			@QueryParam("geographyLevel") String geographyLevel, @QueryParam("geographyId") String geographyId) {
		Response responseToReturn;
		logger.info(
				"Going to getDeviceInsideViewPort NELat {} ,NELng {} ,SWLat {} ,SWLng {} ,operator{},geographyLevel {} ,geographyId{} ",
				northEastLat, northEastLong, southWestLat, southWestLong, operator, geographyLevel, geographyId);
		try {
			if ((NumberUtils.isValidNumber(northEastLat) && NumberUtils.isValidNumber(northEastLong))
					&& (NumberUtils.isValidNumber(southWestLat) && NumberUtils.isValidNumber(southWestLong))
					&& (StringUtils.isNotBlank(operator) && StringUtils.isNotBlank(geographyLevel))) {
				responseToReturn = Response.ok(iNVDeviceDataService.getDeviceCountForTableView(northEastLat,
						northEastLong, southWestLat, southWestLong, operator, geographyLevel, geographyId)).build();
			} else {
				responseToReturn = Response.ok(DeviceConstant.INVALID_PARAMETER_JSON).build();
			}
		} catch (RestException e) {
			logger.error("Error while getting devices for table view:{} ", ExceptionUtils.getMessage(e));
			responseToReturn = Response.ok(DeviceConstant.EXCEPTION_SOMETHING_WENT_WRONG).build();
		}
		return responseToReturn;
	}

	/**
	 * Refresh device data.
	 *
	 * @param deviceId the device id
	 * @return the response
	 */
	@GET
	@Path("refreshNVDeviceStatus")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response refreshDeviceData(@QueryParam("deviceId") String deviceId) {
		Response responseToReturn;
		logger.info("Going to refreshNVDeviceStatus deviceId {} ", deviceId);
		try {
			if (StringUtils.isNotBlank(deviceId)) {
				responseToReturn = Response.ok(iNVDeviceDataService.refreshDeviceData(deviceId)).build();
			} else {
				responseToReturn = Response.ok(DeviceConstant.INVALID_PARAMETER_JSON).build();
			}
		} catch (RestException e) {
			logger.error("Error while execute refreshNVDeviceStatus for table view:{} ", ExceptionUtils.getMessage(e));
			responseToReturn = Response.ok(DeviceConstant.EXCEPTION_SOMETHING_WENT_WRONG).build();
		}
		return responseToReturn;
	}

	/**
	 * Update recharge status.
	 *
	 * @param deviceDataWrappper the device data wrappper
	 * @return the response
	 */
	@POST
	@Path("updateRechargeStatus")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response updateRechargeStatus(NVDeviceDataWrapper deviceDataWrappper) {
		Response responseToReturn;
		try {
			logger.info("Going to update Recharge Status");
			if (deviceDataWrappper != null && deviceDataWrappper.getId() != null) {
				responseToReturn = Response.ok(iNVDeviceDataService.updateRechargeStatus(deviceDataWrappper)).build();
			} else {
				responseToReturn = Response.ok(DeviceConstant.INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error("Error while updating recharge status: {}", Utils.getStackTrace(e));
			responseToReturn = Response.ok(DeviceConstant.EXCEPTION_SOMETHING_WENT_WRONG).build();
		}
		return responseToReturn;
	}

	/**
	 * Gets the device recharge detail.
	 *
	 * @return the device recharge detail
	 * @throws Exception the exception
	 */
	@GET
	@Path("getDeviceRechargeDetail")
	@Override
	public Response getDeviceRechargeDetail() throws Exception {
		logger.info("Going to get Device Recharge Notification Report");
		try {
			Response.ResponseBuilder builder = Response.status(200);
			NVReportWrapper reportWrapper = iNVDeviceDataService.getDeviceRechargeDetail();
			builder = builder.entity(reportWrapper.getFile()).header("Content-Type", ReportConstants.CONTENT_TYPE_CSV)
					.header("Content-Disposition", "attachment; filename=\"" + reportWrapper.getReportName() + "\"");

			return builder.build();
		} catch (Exception e) {
			logger.error("Error while get Device Recharge Notification Report: {}", Utils.getStackTrace(e));
			return Response.ok(ForesightConstants.FAILURE_JSON).build();
		}

	}

	/**
	 * Gets the device recharges detail count.
	 *
	 * @return the device recharges detail count
	 * @throws Exception the exception
	 */
	@GET
	@Path("getDeviceRechargeDetailCount")
	@Override
	public Response getDeviceRechargeDetailCount() throws Exception {
		logger.info("Inside getDeviceRechargeDetailCount");
		try {
			Long count = iNVDeviceDataService.getDeviceRechargeDetailCount();
			logger.info("Done getAllDeviceGroups");
			return Response.ok(count).build();
		} catch (Exception e) {
			logger.error("Exception in get Device Recharge Detail Count : {} ", ExceptionUtils.getStackTrace(e));
			return Response.ok(FAILURE_JSON).build();
		}
	}

	/**
	 * Gets the device list for work order.
	 *
	 * @param woStartTime    the wo start time
	 * @param woEndTime      the wo end time
	 * @param geographyLevel the geography level
	 * @param geographyId    the geography id
	 * @param lLimit         the l limit
	 * @param uLimit         the u limit
	 * @return the device list for work order
	 */
	@GET
	@Path("getDeviceListForWorkOrder")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response getDeviceListForWorkOrder(@QueryParam(WO_START_TIME) Long woStartTime,
			@QueryParam(WO_END_TIME) Long woEndTime, @QueryParam(GEOGRAPHY_LEVEL) String geographyLevel,
			@QueryParam(GEOGRAPHY_ID) Integer geographyId, @QueryParam(LOWER_LIMIT) Integer lLimit,
			@QueryParam(UPPER_LIMIT) Integer uLimit) {
		ResponseBuilder builder = null;
		logger.info(
				"Going to getDeviceListForWorkOrder startTime : {} endTime : {} geographyLevel : {} geographyId : {} uLimit : {} lLimit : {}",
				woStartTime, woEndTime, geographyLevel, geographyId, uLimit, lLimit);
		try {
			if (StringUtils.isNotBlank(geographyLevel) && woStartTime != null && geographyId != null
					&& StringUtils.isNotEmpty(geographyLevel)) {
				List<NVDeviceDataWrapper> deviceDatas = iNVDeviceDataService.getDeviceListForWorkOrder(woStartTime,
						woEndTime, geographyLevel, geographyId, lLimit, uLimit);
				logger.info("Done getDeviceListForWorkOrder");
				builder = Response.ok(deviceDatas);
			} else {
				logger.info(INVALID_PARAMETER_JSON);
				builder = Response.ok(INVALID_PARAMETER_JSON).status(Status.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			logger.error("Exception in getDeviceListForWorkOrder : {} ", ExceptionUtils.getStackTrace(e));
			builder = Response.ok(FAILURE_JSON).status(Status.INTERNAL_SERVER_ERROR);
		}
		return builder.build();
	}

	/**
	 * Gets the device list count for work order.
	 *
	 * @param woStartTime    the wo start time
	 * @param woEndTime      the wo end time
	 * @param geographyLevel the geography level
	 * @param geographyId    the geography id
	 * @return the device list count for work order
	 */
	@GET
	@Path("getDeviceListCountForWorkOrder")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response getDeviceListCountForWorkOrder(@QueryParam(WO_START_TIME) Long woStartTime,
			@QueryParam(WO_END_TIME) Long woEndTime, @QueryParam(GEOGRAPHY_LEVEL) String geographyLevel,
			@QueryParam(GEOGRAPHY_ID) Integer geographyId) {
		ResponseBuilder builder = null;
		logger.info(
				"Going to getDeviceListCountForWorkOrder startTime : {} endTime : {} geographyLevel : {} geographyId : {} ",
				woStartTime, woEndTime, geographyLevel, geographyId);
		try {
			if (StringUtils.isNotBlank(geographyLevel) && woStartTime != null && geographyId != null
					&& StringUtils.isNotEmpty(geographyLevel)) {
				Long deviceDataCount = iNVDeviceDataService.getDeviceListCountForWorkOrder(woStartTime, woEndTime,
						geographyLevel, geographyId);
				logger.info("Done getDeviceListCountForWorkOrder");
				builder = Response.ok(deviceDataCount);
			} else {
				logger.info(INVALID_PARAMETER_JSON);
				builder = Response.ok(INVALID_PARAMETER_JSON).status(Status.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			logger.error("Exception in getDeviceListCountForWorkOrder : {} ", ExceptionUtils.getStackTrace(e));
			builder = Response.ok(FAILURE_JSON).status(Status.INTERNAL_SERVER_ERROR);
		}
		return builder.build();
	}

	/**
	 * Gets the device list for WO.
	 *
	 * @param woStartTime    the wo start time
	 * @param geographyLevel the geography level
	 * @param geographyId    the geography id
	 * @param lLimit         the l limit
	 * @param uLimit         the u limit
	 * @return the device list for WO
	 */
	@GET
	@Path("getStealthDeviceListForGeography")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response getStealthDeviceListForGeography(@QueryParam(GEOGRAPHY_LEVEL) String geographyLevel,
			@QueryParam(GEOGRAPHY_ID) Integer geographyId, @QueryParam(LOWER_LIMIT) Integer lLimit,
			@QueryParam(UPPER_LIMIT) Integer uLimit) {
		ResponseBuilder builder = null;
		logger.info(
				"Going to getStealthDeviceListForGeography  geographyLevel : {} geographyId : {} uLimit : {} lLimit : {}",
				geographyLevel, geographyId, uLimit, lLimit);
		try {
			if (StringUtils.isNotBlank(geographyLevel) && geographyId != null
					&& StringUtils.isNotEmpty(geographyLevel)) {
				List<NVDeviceDataWrapper> deviceDatas = iNVDeviceDataService
						.getStealthDeviceListForGeography(geographyLevel, geographyId, lLimit, uLimit);
				logger.info("Done getStealthDeviceListForGeography");
				builder = Response.ok(deviceDatas);
			} else {
				logger.info(INVALID_PARAMETER_JSON);
				builder = Response.ok(INVALID_PARAMETER_JSON).status(Status.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			logger.error("Exception in getStealthDeviceListForGeography : {} ", ExceptionUtils.getStackTrace(e));
			builder = Response.ok(FAILURE_JSON).status(Status.INTERNAL_SERVER_ERROR);
		}
		return builder.build();
	}

	@GET
	@Path("getStealthDeviceListCountForGeography")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response getStealthDeviceListCountForGeography(@QueryParam(GEOGRAPHY_LEVEL) String geographyLevel,
			@QueryParam(GEOGRAPHY_ID) Integer geographyId) {
		ResponseBuilder builder = null;
		logger.info("Going to getStealthDeviceListCountForGeography  geographyLevel : {} geographyId : {}",
				geographyLevel, geographyId);
		try {
			if (StringUtils.isNotBlank(geographyLevel) && geographyId != null
					&& StringUtils.isNotEmpty(geographyLevel)) {
				Long deviceCount = iNVDeviceDataService.getStealthDeviceListCountForGeography(geographyLevel,
						geographyId);
				logger.info("Done getStealthDeviceListCountForGeography");
				builder = Response.ok(deviceCount);
			} else {
				logger.info(INVALID_PARAMETER_JSON);
				builder = Response.ok(INVALID_PARAMETER_JSON).status(Status.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			logger.error("Exception in getStealthDeviceListCountForGeography : {} ", ExceptionUtils.getStackTrace(e));
			builder = Response.ok(FAILURE_JSON).status(Status.INTERNAL_SERVER_ERROR);
		}
		return builder.build();
	}

	@POST
	@Path("deviceDashboardData")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public List<DeviceDashboard> getDeviceDashboardData(@QueryParam("llimit") Integer llimit,
			@QueryParam("ulimit") Integer ulimit, DeviceDashboardWrapper wrapper) {
		logger.info("Inside deviceDashboardData ");
		return deviceDashboardService.getDeviceDashbordData(llimit, ulimit, wrapper);

	}

	@POST
	@Path("deviceDashboardDataCount")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public Response getDeviceDashboardCount(DeviceDashboardWrapper wrapper) {
		logger.info("Inside deviceDashboardData ");
		return Response.ok(deviceDashboardService.getDeviceDashbordDataCount(wrapper)).build();

	}

	/**
	 * get filters for device data
	 * 
	 * @return
	 */
	@GET
	@Path("getFilters")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response getFilters() {
		logger.info("Inside getFilters ");
		return Response.ok(iNVDeviceDataService.getFilters()).build();
	}

	/**
	 * get device data using different filters added in wrapper
	 * 
	 * @param lLimit
	 * @param uLimit
	 * @param wrapper
	 * @return
	 */
	@POST
	@Path("getDeviceData")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response getDeviceData(@QueryParam(LOWER_LIMIT) Integer lLimit, @QueryParam(UPPER_LIMIT) Integer uLimit,
			NVDeviceWrapper wrapper) {
		logger.info("Inside getDeviceData");
		return Response.ok(iNVDeviceDataService.getDeviceData(wrapper, lLimit, uLimit)).build();
	}

	/**
	 * get device data count using different filters added in wrapper
	 * 
	 * @param wrapper
	 * @return
	 */
	@POST
	@Path("getDeviceDataCount")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response getDeviceDataCount(NVDeviceWrapper wrapper) {
		logger.info("Inside getDeviceData");
		return Response.ok(iNVDeviceDataService.getDeviceDataCount(wrapper)).build();
	}
}

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

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.device.constant.DeviceConstant;
import com.inn.foresight.module.nv.device.model.NVDeviceGroup;
import com.inn.foresight.module.nv.device.rest.INVDeviceGroupRest;
import com.inn.foresight.module.nv.device.service.INVDeviceGroupService;
import com.inn.foresight.module.nv.device.wrapper.NVDeviceGroupWrapper;
import com.inn.product.um.user.model.User;
import com.inn.product.um.user.service.impl.UserContextServiceImpl;

/**
 * The Class NVDeviceGroupRestImpl.
 *
 * @author innoeye
 * date - 24-Apr-2018 12:41:24 PM
 */
@Path("/NVDeviceGroup")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class NVDeviceGroupRestImpl extends DeviceConstant
		implements INVDeviceGroupRest {

	/** The logger. */
	private Logger logger = LogManager.getLogger(NVDeviceGroupRestImpl.class);
	
	/** The device group service. */
	@Autowired
	private INVDeviceGroupService deviceGroupService;
	
	
	/**
	 * Gets the all device groups.
	 *
	 * @param ulimit the ulimit
	 * @param llimit the llimit
	 * @return the all device groups
	 */
	@POST
	@Override
	@Path("create")
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(String groupJSON) {
		logger.info("Inside create json : {}",groupJSON);
		try {
			if (StringUtils.isNotBlank(groupJSON) && StringUtils.isNotEmpty(groupJSON)) {
				NVDeviceGroup deviceGroup = new ObjectMapper().readValue(groupJSON, NVDeviceGroup.class);
				User creator = UserContextServiceImpl.getUserInContext();
				NVDeviceGroup nvDeviceGroup = deviceGroupService.create(deviceGroup, creator);
				logger.info("Done create");
				return Response.ok(nvDeviceGroup).build();
			} else {
				return Response.ok(INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error("Exception in create : {} ", ExceptionUtils.getStackTrace(e));
			return Response.ok(MESSAGE_START+e.getMessage()+MESSAGE_END).build();
		}
	}
	
	/**
	 * Creates the.
	 *
	 * @param probeJSON the probe JSON
	 * @return the response
	 */
	@GET
	@Override
	@Path("getAllDeviceGroups")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllDeviceGroups(@QueryParam(ULIMIT) Integer ulimit,
			@QueryParam(LLIMIT) Integer llimit) {
		logger.info("Inside getAllDeviceGroups");
		try {
			List<NVDeviceGroupWrapper> profileDatas = deviceGroupService.getAllDeviceGroups(ulimit,llimit);
			logger.info("Done getAllDeviceGroups");
			return Response.ok(profileDatas).build();
		} catch (Exception e) {
			logger.error("Exception in getAllDeviceGroups : {} ", ExceptionUtils.getStackTrace(e));
			return Response.ok(FAILURE_JSON).build();
		}
	}
	

	/**
	 * Update device group.
	 *
	 * @param deviceGroup the device group
	 * @return the response
	 */
	@POST
	@Override
	@Path("updateDeviceGroup")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateDeviceGroup(NVDeviceGroupWrapper deviceGroup) {
		logger.info("Inside updateDeviceGroup: {}",deviceGroup);
		try {
			if (deviceGroup != null) {
				NVDeviceGroupWrapper updatedNVDeviceGroup = deviceGroupService.updateDeviceGroup(deviceGroup);
				logger.info("Done update");
				return Response.ok(updatedNVDeviceGroup).build();
			} else {
				return Response.ok(INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error("Exception in update : {} ", ExceptionUtils.getStackTrace(e));
			return Response.ok(MESSAGE_START+e.getMessage()+MESSAGE_END).build();
		}	
	}
	
	/**
	 * Adds the device.
	 *
	 * @param id the id
	 * @param devices the devices
	 * @return the response
	 */
	@POST
	@Override
	@Path("addDevice/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addDevice(@PathParam("id")  String id,String devices) {
		logger.info("Inside addDevice id :{} devices : {}",id,devices);
		try {
			if (StringUtils.isNoneBlank(id,devices) && StringUtils.isNoneEmpty(id,devices)) {
				String response = deviceGroupService.addDevice(id, devices, Boolean.FALSE);
				logger.info("Done addDevice");
				return Response.ok(response).build();
			} else {
				return Response.ok(INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error("Exception in addDevice : {} ", ExceptionUtils.getStackTrace(e));
			return Response.ok(MESSAGE_START+e.getMessage()+MESSAGE_END).build();
		}
	}

	/**
	 * Removes the device.
	 *
	 * @param id the id
	 * @param devices the devices
	 * @return the response
	 */
	@POST
	@Override
	@Path("removeDevice/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeDevice(@PathParam("id")  String id,String devices) {
		logger.info("Inside removeDevice id :{} devices : {}",id,devices);
		try {
			if (StringUtils.isNoneBlank(id,devices) && StringUtils.isNoneEmpty(id,devices)) {
				String response = deviceGroupService.removeDevice(id, devices, Boolean.FALSE);
				logger.info("Done removeDevice");
				return Response.ok(response).build();
			} else {
				return Response.ok(INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error("Exception in removeDevice : {} ", ExceptionUtils.getStackTrace(e));
			return Response.ok(MESSAGE_START+e.getMessage()+MESSAGE_END).build();
		}
	}
	
	/**
	 * Gets the device group.
	 *
	 * @param id the id
	 * @return the device group
	 */
	@GET
	@Override
	@Path("getDeviceGroup/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDeviceGroup(@PathParam("id") String id) {
		logger.info("Inside getDeviceGroup id : {}", id);
		try {
			if (id != null) {
				String deviceGroupWrapper = deviceGroupService.getDeviceGroup(id,Boolean.FALSE);
				logger.info("Done getDeviceGroup");
				return Response.ok(deviceGroupWrapper).build();
			} else {
				return Response.ok(INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error("Exception in getDeviceGroup : {} ",ExceptionUtils.getStackTrace(e));
			return Response.ok(FAILURE_JSON).build();
		}
	}
	
	/**
	 * Removes the.
	 *
	 * @param id the id
	 * @return the response
	 */
	@GET
	@Override
	@Path("remove/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response remove(@PathParam("id") Integer id) {
		logger.info("Inside remove id : {}", id);
		try {
			if (id != null) {
				deviceGroupService.removeDevice(String.valueOf(id), null, Boolean.FALSE);
				logger.info("Done remove");
				return Response.ok(SUCCESS_JSON).build();
			} else {
				return Response.ok(INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error("Exception in remove : {} ",ExceptionUtils.getStackTrace(e));
			return Response.ok(FAILURE_JSON).build();
		}
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
		logger.info("Going to get device Details by device id: {} ", deviceId);
		try {
			if (!StringUtils.isBlank(deviceId) && Utils.isNotNull(deviceId)) {
				responseToReturn = Response	.ok(deviceGroupService.getDeviceDetailsByDeviceId(deviceId))
											.build();
			} else {
				responseToReturn = Response	.ok(DeviceConstant.INVALID_PARAMETER_JSON)
											.build();
			}
		} catch (RestException e) {
			logger.error("Error: {} while getting device Details by device id: {} ", ExceptionUtils.getMessage(e), deviceId);
			responseToReturn = Response	.ok(DeviceConstant.EXCEPTION_SOMETHING_WENT_WRONG)
										.build();
		}
		return responseToReturn;
	}

	/**
	 * Gets the all device group count.
	 *
	 * @return the all device group count
	 */
	@GET
	@Override
	@Path("getAllDeviceGroupCount")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllDeviceGroupCount() {
		logger.info("Inside getAllDeviceGroups");
		try {
			Long count = deviceGroupService.getAllDeviceGroupsCount();
			logger.info("Done getAllDeviceGroups");
			return Response.ok(count).build();
		} catch (Exception e) {
			logger.error("Exception in getAllDeviceGroups : {} ", ExceptionUtils.getStackTrace(e));
			return Response.ok(FAILURE_JSON).build();
		}
	}

	/**
	 * Search NV device groups.
	 *
	 * @param startDate the start date
	 * @param endDate the end date
	 * @param swLat the sw lat
	 * @param swLng the sw lng
	 * @param neLat the ne lat
	 * @param neLng the ne lng
	 * @param zoomLevel the zoom level
	 * @param dataType the data type
	 * @param kpi the kpi
	 * @param threshold the threshold
	 * @param resultType the result type
	 * @return the response
	 */
	@GET
	@Path("searchNVDeviceGroups")
	@Override
	public Response searchNVDeviceGroups(
			@QueryParam(START_DATE) Long startDate, 
			@QueryParam(END_DATE) Long endDate,
			@QueryParam(SOUTH_WEST_LAT) Double swLat, 
			@QueryParam(SOUTH_WEST_LONG) Double swLng,
			@QueryParam(NORTH_EAST_LAT) Double neLat, 
			@QueryParam(NORTH_EAST_LONG) Double neLng,
			@QueryParam(ZOOMLEVEL) Integer zoomLevel, 
			@QueryParam(DATATYPE) String dataType,
			@QueryParam(KPI) String kpi, 
			@QueryParam(THRESHOLD) Double threshold,
			@QueryParam(RESULT_TYPE) String resultType) {

		try {
			logger.info(
					"Going to searchProbes for sWLat {},sWLong {},sWLong {},nELong {}, zoomLevel {}, dataType {}, kpi {} and threshold {}",
					swLat, swLng, neLat, neLng, zoomLevel, dataType, kpi, threshold);
			if ((swLat == null || swLng == null) || (neLat == null || neLng == null) || (zoomLevel == null
					|| dataType == null)) {
				logger.info("Parameters are null, SWLat {},SWLng {},NELat {}, NELng {} and zoomLevel {}", swLat, swLng,
						neLat, neLng, zoomLevel);
				return Response.ok(INVALID_PARAMETER_JSON).build();
			} else {
				return Response.ok(deviceGroupService.searchNVDeviceGroups(startDate, endDate, swLat, swLng,
						neLat, neLng, zoomLevel, dataType, kpi, threshold, resultType)).build();
			}
		} catch (Exception e) {
			logger.error("Exception in searchNVDeviceGroups : {} ", ExceptionUtils.getStackTrace(e));
			return Response.ok(FAILURE_JSON).build();
		}
	}

}
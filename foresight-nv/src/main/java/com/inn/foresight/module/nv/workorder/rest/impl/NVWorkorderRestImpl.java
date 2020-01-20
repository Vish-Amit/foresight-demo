package com.inn.foresight.module.nv.workorder.rest.impl;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonSyntaxException;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.lang.StringUtils;
import com.inn.commons.unit.Duration;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ConfigEnum;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.constants.InBuildingConstants;
import com.inn.foresight.core.report.model.AnalyticsRepository;
import com.inn.foresight.module.nv.app.exceptions.InvalidEncryptionException;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder.Status;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder.TemplateType;
import com.inn.foresight.module.nv.layer3.constants.NVLayer3Constants;
import com.inn.foresight.module.nv.layer3.utils.NVLayer3Utils;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;
import com.inn.foresight.module.nv.workorder.rest.NVWorkorderRest;
import com.inn.foresight.module.nv.workorder.service.INVWorkorderService;
import com.inn.foresight.module.nv.workorder.utils.NVWorkorderUtils;
import com.inn.foresight.module.nv.workorder.wrapper.NVWorkorderWrapper;
import com.inn.product.security.utils.AuthenticationCommonUtil;

/** The Class NVWorkorderRestImpl. */
@Path("/NVWorkorder")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Service("NVWorkorderRestImpl")
public class NVWorkorderRestImpl implements NVWorkorderRest {

	/** The logger. */
	private Logger logger = LogManager.getLogger(NVWorkorderRestImpl.class);

	/** Instance of NVWorkorderServiceImpl. */
	@Autowired
	private INVWorkorderService nvWorkorderService;

	/**
	 * Creates NV Workorder for given Template Type and Recipe details from Web
	 * assigned to Users.
	 * 
	 * @param NVWOWraper: wrapper contains workorder details
	 * @return Success Message if Workorder created, Failure Message otherwise
	 */
	@POST
	@Path("createWorkorder")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response createWorkorder(NVWorkorderWrapper wrapper) {
		logger.info("Going to Create Workorder From Web User {}  ", wrapper);
		if (wrapper != null && NVWorkorderUtils.verifyWorkorder(wrapper)) {
			return Response.ok(nvWorkorderService.createWorkorderFromWeb(wrapper)).build();
		}
		return Response.ok(NVWorkorderConstant.INVALID_PARAMETER_JSON).build();
	}

	/**
	 * Creates NV Workorder for given Template Type and Recipe details from Web
	 * assigned to Device.
	 * 
	 * @param NVWOWraper: wrapper contains workorder details
	 * @return Success Message if Workorder created, Failure Message otherwise
	 */
	@POST
	@Path("createWorkorderForDevice")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response createWorkorderForDevice(NVWorkorderWrapper wrapper) {
		logger.info("Going to create Workorder With Given Recipe {}", wrapper);
		if (wrapper != null) {
			return Response.ok(nvWorkorderService.createWorkorderForDevice(wrapper)).build();
		}
		return Response.ok(NVWorkorderConstant.INVALID_PARAMETER_JSON).build();
	}

	/**
	 * Gets All Workorder Created For Provided User or workorder Id.
	 * 
	 * @param userId       the user id
	 * @param workorderId  the workorder id
	 * @param templateType the template type
	 * @param modifiedTime the modified time
	 * @return Collection<WORecipeWrapper>
	 */
	@POST
	@Path("getWorkorderDetails")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.TEXT_PLAIN)
	@Override
	public Response getWorkorderDetails(String mapStr) {
		try {
			if (Utils.hasValidValue(mapStr)) {
				Map<String, String> map = NVWorkorderUtils.castMapFromEncryptedString(mapStr);
				String workorderId = map.get(NVWorkorderConstant.WORKORDER_ID);
				String templateType = map.get(NVWorkorderConstant.WORKORDER_TYPE);
				String modifiedTime = map.get(NVWorkorderConstant.MODIFIED_TIME);
				logger.info("Going to get Recipe and Workorder from workorderId {}, templateType {}, modifiedTime {}",
						workorderId, templateType, modifiedTime);
				if (templateType != null && modifiedTime != null) {
					return Response.ok(AuthenticationCommonUtil.checkForValueEncryption(
							nvWorkorderService.getWorkorderDetailsByUser(workorderId, templateType, modifiedTime),null))
							.build();

				}
			}
			return Response.ok(AuthenticationCommonUtil.checkForValueEncryption(NVWorkorderConstant.INVALID_PARAMETER_JSON,null)).build();
		} catch (JsonSyntaxException | InvalidEncryptionException e) {
			return Response.ok(AuthenticationCommonUtil.checkForValueEncryption(NVWorkorderConstant.INVALID_ENCRYPTION_JSON,null)).build();
		} catch (Exception e) {
			return Response.ok(AuthenticationCommonUtil.checkForValueEncryption(NVWorkorderConstant.FAILURE_JSON,null)).build();
		}
	}

	/**
	 * Gets All Workorder Created For Provided User or workorder Id.
	 * 
	 * @param userId       the user id
	 * @param workorderId  the workorder id
	 * @param templateType the template type
	 * @param modifiedTime the modified time
	 * @return Collection<WORecipeWrapper>
	 */
	@GET
	@Path("getWorkorderDetailsByWOId")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public Response getWorkorderDetailsByWOId(@QueryParam(NVWorkorderConstant.WORKORDER_ID) Integer workorderId,@QueryParam(NVWorkorderConstant.TASK_ID) Integer taskId) {
		try {
			if (Utils.hasValue(workorderId)) {
				logger.info("Going to get Recipe and Workorder from workorderId {} , taskID {}", workorderId,taskId);
				return Response.ok(nvWorkorderService.getWorkorderDetailsByWOId(workorderId,taskId)).build();
			}
			return Response.ok(AuthenticationCommonUtil.checkForValueEncryption(NVWorkorderConstant.INVALID_PARAMETER_JSON,null)).build();
		} catch (Exception e) {
			return Response.ok(AuthenticationCommonUtil.checkForValueEncryption(NVWorkorderConstant.FAILURE_JSON,null)).build();
		}
	}

	@GET
	@Path("updateWorkorderArchivedStatus")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public Response updateWorkorderArchivedStatus(@QueryParam(NVWorkorderConstant.WORKORDER_ID) Integer workorderId,
			@QueryParam("isArchive") Boolean isArchive) {
		try {
			if (Utils.hasValue(workorderId)) {
				logger.info("Going to update archived status by workorderId {}", workorderId);
				return Response.ok(nvWorkorderService.updateWorkorderArchivedStatus(workorderId, isArchive)).build();
			}
			return Response.ok(AuthenticationCommonUtil.checkForValueEncryption(NVWorkorderConstant.INVALID_PARAMETER_JSON,null)).build();
		} catch (Exception e) {
			return Response.ok(AuthenticationCommonUtil.checkForValueEncryption(NVWorkorderConstant.FAILURE_JSON,null)).build();
		}
	}

	@GET
	@Path("getWorkorderDetailsByWOIdAndDeviceId")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public Response getWorkorderDetailsByWOIdAndDeviceId(
			@QueryParam(NVWorkorderConstant.WORKORDER_ID) Integer workorderId, @QueryParam("deviceId") String deviceId,
			@QueryParam("date") Long date) {
		try {
			if (Utils.hasValue(workorderId)) {
				logger.info("Going to get Recipe and Workorder from workorderId {}", workorderId);
				Date woDate = null;
				if (date != null) {
					woDate = new Date(date);
				}
				return Response.ok(nvWorkorderService.getWorkorderDetailsByDeviceId(workorderId, deviceId, woDate))
						.build();
			}
			return Response.ok(AuthenticationCommonUtil.checkForValueEncryption(NVWorkorderConstant.INVALID_PARAMETER_JSON,null)).build();
		} catch (Exception e) {
			return Response.ok(AuthenticationCommonUtil.checkForValueEncryption(NVWorkorderConstant.FAILURE_JSON,null)).build();
		}
	}

	/**
	 * Completed Workorder by Completing Task by given TaskId.
	 *
	 * @param taskId : BPMNTask Id corresponds to BPMNWorkorder
	 * @return Success Message if Task Completed, Failure Message otherwise
	 */
	@GET
	@Path("completeWOTask")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public Response completeWOTask(@QueryParam(NVWorkorderConstant.TASK_ID) Integer taskId) {
		logger.info("Going to complete Workorder for Task Id {}", taskId);
		if (taskId != null) {
			Object result = nvWorkorderService.completeWorkorderByTaskId(taskId);
			if (result instanceof AnalyticsRepository) {
				try {
					//nvWorkorderService.generateReport((AnalyticsRepository) result);
				} catch (Exception e) {
					logger.error("Getting Exception in generating report, @method completeWOTask(), {}",
							ExceptionUtils.getStackTrace(e));
				}
				return Response.ok(NVWorkorderConstant.SUCCESS_JSON).build();
			}
			return Response.ok(result).build();
		}
		return Response.ok(NVWorkorderConstant.INVALID_PARAMETER_JSON).build();
	}

	/**
	 * Accepts llimit and ulimit and returns List of NVWorkorderWrapper Objects if
	 * currently logged in user has permissions .
	 * 
	 * @param lLimit
	 * @param uLimit
	 * @return List of NVWorkorderWrapper Objects
	 */
	@POST
	@Path("findAllWorkorder/{llimit}/{ulimit}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response findAllWorkorder(Map<String, List<String>> filterMap, @PathParam("llimit") Integer lLimit,
			@PathParam("ulimit") Integer uLimit, @QueryParam("searchString") String searchString,
			@QueryParam("isArchive") Boolean isArchive) {
		logger.info("Inside method  findAllWorkorder ");
		if (lLimit != null && uLimit != null && uLimit > lLimit && filterMap != null) {
			try {
				List<Status> statusList = NVWorkorderUtils.getStatusList(filterMap.get(NVWorkorderConstant.STATUS));
				List<TemplateType> templateList = NVWorkorderUtils
						.getTemplateList(filterMap.get(NVWorkorderConstant.TEMPLATE));
				if (Utils.isValidList(statusList) && Utils.isValidList(templateList)) {
					return Response.ok(nvWorkorderService.findAllWorkorder(lLimit, uLimit, statusList, templateList,
							searchString, isArchive)).build();
				}
			} catch (Exception e) {
				logger.error("Exception in findAllWorkorder : {}", ExceptionUtils.getStackTrace(e));
				return Response.ok(NVWorkorderConstant.FAILURE_JSON).build();
			}
		}
		return Response.ok(NVWorkorderConstant.INVALID_PARAMETER_JSON).build();
	}

	/**
	 * Gets Total Workorder count .
	 * 
	 * @return Integer Workorder Count for of logged in Permission
	 * @throws RestException
	 */
	@POST
	@Path("getTotalWorkorderCount")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response getTotalWorkorderCount(@QueryParam("searchString") String searchString,
			@QueryParam("isArchived") Boolean isArchived, Map<String, List<String>> filterMap) {
		try {
			if (filterMap != null) {
				List<Status> statusList = NVWorkorderUtils.getStatusList(filterMap.get(NVWorkorderConstant.STATUS));
				List<TemplateType> templateList = NVWorkorderUtils
						.getTemplateList(filterMap.get(NVWorkorderConstant.TEMPLATE));
				if (Utils.isValidList(statusList) && Utils.isValidList(templateList)) {
					return Response.ok(nvWorkorderService.getTotalWorkorderCount(statusList, templateList, searchString,
							isArchived)).build();
				}
			}
			return Response.ok(NVWorkorderConstant.INVALID_PARAMETER_JSON).build();
		} catch (Exception e) {
			logger.error("Exception in getTotalWorkorderCount== : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(NVWorkorderConstant.FAILURE_JSON).build();
		}
	}

	/**
	 * @param wrapper
	 * @param lLimit
	 * @param uLimit
	 * @param isArchive
	 * @return
	 */
	@POST
	@Path("findFilteredWO/{llimit}/{ulimit}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response findFilteredWO(NVWorkorderWrapper wrapper, @PathParam("llimit") Integer lLimit,
			@PathParam("ulimit") Integer uLimit, @QueryParam("isArchive") Boolean isArchive) {
		logger.info("Inside method  findAllWorkorder");
		if (wrapper != null) {
			try {
				return Response.ok(nvWorkorderService.findFilteredWOList(wrapper, lLimit, uLimit, isArchive)).build();
			} catch (Exception e) {
				logger.error("Exception in findAllWorkorder : {}", ExceptionUtils.getStackTrace(e));
				return Response.ok(NVWorkorderConstant.FAILURE_JSON).build();
			}
		}
		return Response.ok(NVWorkorderConstant.INVALID_PARAMETER_JSON).build();
	}
	
	/**
	 * This
	 * 
	 * @param request
	 * @param Json    Data of [["Geography Name"],["r:centroid","r:coordinates"]]
	 * @return Json Boundary data got after Hitting NVHbase DropWizad
	 * @throws RestException
	 */
	@POST
	@Path("getGeographyBoundryByLevel")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response getGeographyBoundryByLevel(@QueryParam("tableName") String tableName, String strGeoData) {
		try {
			String url = ConfigUtils.getString(ConfigEnum.DROPWIZARD_HBASE_GEOGRAPHY_BOUNDARY, Boolean.TRUE)
					+ tableName;
			Duration duration = Duration
					.minutes(Integer.parseInt(ConfigUtils.getString(ConfigEnum.TIMEOUT_VALUE.getValue())));
			StringEntity entity = new StringEntity(AuthenticationCommonUtil.checkForValueDecryption(strGeoData),
					ContentType.APPLICATION_JSON);
			String result = NVLayer3Utils.sendHttpPostRequest(url, entity, true, duration).getString();
			logger.info("Done at getGeting Data From Hbase");
			return Response.ok(result).build();
		} catch (Exception e) {
			logger.error("Exception in getWorkorderBoundryByLevel : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(NVWorkorderConstant.EXCEPTION_SOMETHING_WENT_WRONG).build();
		}
	}

	@POST
	@Path("getWorkorderListByGeography")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response getWorkorderListByGeography(@QueryParam("geographyLevel") String geographyLevel,
			@QueryParam("geographyId") Integer geographyId, @QueryParam("remark") String remark,
			Map<String, List<String>> filterMap) {
		try {
			if (((geographyLevel != null && geographyId != null) || remark != null) && filterMap != null) {
				List<Status> statusList = NVWorkorderUtils.getStatusList(filterMap.get(NVWorkorderConstant.STATUS));
				List<TemplateType> templateList = NVWorkorderUtils
						.getTemplateList(filterMap.get(NVWorkorderConstant.TEMPLATE));
				if (Utils.isValidList(statusList) && Utils.isValidList(templateList)) {
					return Response.ok(nvWorkorderService.getWorkorderListByGeography(geographyLevel, geographyId,
							statusList, templateList, remark)).build();
				}
			}
			return Response.ok(NVWorkorderConstant.INVALID_PARAMETER_JSON).build();
		} catch (Exception e) {
			logger.error("Exception in getWorkorderListByGeography {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(NVWorkorderConstant.FAILURE_JSON).build();
		}
	}

	@POST
	@Path("/uploadKML")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Override
	public Response uploadKML(@Multipart(value = ForesightConstants.FILE) InputStream inputStream,
			@Multipart(value = ForesightConstants.FILENAME) String fileName) {
		try {
			if (StringUtils.isNotBlank(fileName) && inputStream != null) {
				String filePath = ConfigUtils.getString(ConfigEnum.NV_KML_BASE_PATH.getValue()) + fileName;
				logger.info("Getting KML file path {}", filePath);
				NVWorkorderUtils.copyFileToLocalPath(filePath, inputStream);
				return Response.ok(String.format(NVWorkorderConstant.FILE_PATH_JSON, filePath)).build();
			}
			return Response.ok(NVWorkorderConstant.INVALID_PARAMETER_JSON).build();
		} catch (Exception e) {
			logger.error("Error while uploading KML : {}", ExceptionUtils.getMessage(e));
			return Response.ok(InBuildingConstants.FAILURE_JSON).build();
		}
	}

	@GET
	@Path("/getWorkOrderDetailByImei")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response getWorkOrderDetailByImei(@QueryParam(NVLayer3Constants.IMEI) String imei,
			@QueryParam(NVLayer3Constants.START_TIME) Long startTime,
			@QueryParam(NVLayer3Constants.END_TIME) Long endTime,
			@QueryParam(NVLayer3Constants.TEMPLATE) GenericWorkorder.TemplateType templateType,
			@Context HttpServletRequest request) {
		try {
			logger.info("Getting parameter imei {}  startTime {}   endTime {}  templateType {}  ", imei, startTime,
					endTime, templateType);
			if (imei != null && startTime != null && endTime != null && templateType != null) {
				return Response.ok(nvWorkorderService.getWorkOrderDetailByImei(imei, startTime, endTime, templateType))
						.build();
			}
			return Response.ok(NVWorkorderConstant.INVALID_PARAMETER_JSON).build();
		} catch (Exception e) {
			logger.error("Error In getting getWorkOrderDetailByImei  : {}", ExceptionUtils.getMessage(e));
			return Response.ok(InBuildingConstants.FAILURE_JSON).build();
		}
	}

	@POST
	@Path("getWOListByGeographyOfPeriod")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response getWorkorderListByGeographyOfPeriod(@QueryParam("geographyLevel") String geographyLevel,
			@QueryParam("geographyId") Integer geographyId, Map<String, List<String>> filterMap,
			@QueryParam("quarter") Integer quarter, @QueryParam("year") Integer year) {
		logger.info("Inside method getWorkorderListByGeographyOfPeriod for quarter {} , year {} ", quarter, year);
		try {
			if (geographyLevel != null && geographyId != null && filterMap != null) {
				List<Status> statusList = NVWorkorderUtils.getStatusList(filterMap.get(NVWorkorderConstant.STATUS));
				List<TemplateType> templateList = NVWorkorderUtils
						.getTemplateList(filterMap.get(NVWorkorderConstant.TEMPLATE));
				List<Integer> quarterList = quarter != null ? Arrays.asList(quarter) : null;
				List<Integer> yearList = year != null ? Arrays.asList(year) : null;
				if (Utils.isValidList(statusList) && Utils.isValidList(templateList) && Utils.isValidList(yearList)) {
					return Response.ok(nvWorkorderService.getWorkorderListByGeographyOfPeriod(geographyLevel,
							geographyId, statusList, templateList, quarterList, yearList)).build();
				}
			}
			return Response.ok(NVWorkorderConstant.INVALID_PARAMETER_JSON).build();
		} catch (Exception e) {
			logger.error("Exception in getWOListByGeographyOfPeriod {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(NVWorkorderConstant.FAILURE_JSON).build();
		}
	}

	@POST
	@Path("updateWorkorder")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response updateWorkorder(NVWorkorderWrapper wrapper) {
		try {
			if (wrapper != null) {
				logger.info("Going to update workorder with details {} ", wrapper);
				return Response.ok(nvWorkorderService.updateWorkorderDetails(wrapper)).build();
			}
			return Response.ok(NVWorkorderConstant.INVALID_PARAMETER_JSON).build();
		} catch (Exception e) {
			logger.error("Exception in updateWorkorder : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(NVWorkorderConstant.FAILURE_JSON).build();
		}
	}

	@POST
	@Path("getWOListByGeographyListOfPeriod")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response getWOListByGeographyListOfPeriod(@QueryParam("geographyLevel") String geographyLevel,
			Map<String, List<String>> filterMap, @QueryParam("quarter") Integer quarter,
			@QueryParam("year") Integer year, @QueryParam("technology") String technology) {
		logger.info("Inside method getWorkorderListByGeographyOfPeriod for quarter {} , year {} ", quarter, year);
		try {
			if (geographyLevel != null && filterMap!=null&&filterMap.get(NVWorkorderConstant.GEOGRAPHY_ID_LIST) != null) {
				List<Status> statusList = NVWorkorderUtils.getStatusList(filterMap.get(NVWorkorderConstant.STATUS));
				List<TemplateType> templateList = NVWorkorderUtils
						.getTemplateList(filterMap.get(NVWorkorderConstant.TEMPLATE));
				List<Integer> geographyIdList = NVWorkorderUtils
						.getgeographyIdList(filterMap.get(NVWorkorderConstant.GEOGRAPHY_ID_LIST));
				List<Integer> quarterList = quarter != null ? Arrays.asList(quarter) : null;
				List<Integer> yearList = year != null ? Arrays.asList(year) : null;
				if (Utils.isValidList(statusList) && Utils.isValidList(templateList) && Utils.isValidList(yearList)) {
					logger.info("geographyIdList for woList  {} ", geographyIdList);
					return Response.ok(nvWorkorderService.getWorkorderListByGeographyOfPeriod(geographyLevel,
							geographyIdList, statusList, templateList, quarterList, yearList, technology)).build();
				}
			}
			return Response.ok(NVWorkorderConstant.INVALID_PARAMETER_JSON).build();
		} catch (Exception e) {
			logger.error("Exception in getWOListByGeographyListOfPeriod === : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(NVWorkorderConstant.FAILURE_JSON).build();
		}
	}

	@GET
	@Path("reassignWorkorder")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response reassignWorkorder(@QueryParam(NVWorkorderConstant.TASK_ID) Integer taskId,
			@QueryParam(NVWorkorderConstant.ASSIGNED_TO) String userName,
			@QueryParam(NVWorkorderConstant.REMARK) String remark) {
		try {
			if (Utils.isNotNull(taskId) && Utils.hasValidValue(userName)) {
				logger.info("Going to reassign workorder with details taskId {}, username {}, remark {} ", taskId,
						userName, remark);
				return Response.ok(nvWorkorderService.reassignWorkorder(taskId, userName, remark)).build();
			}
			return Response.ok(NVWorkorderConstant.INVALID_PARAMETER_JSON).build();
		} catch (Exception e) {
			logger.error("Exception in getTotalWorkorderCount : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(NVWorkorderConstant.FAILURE_JSON).build();
		}
	}

	@GET
	@Path("deleteWorkorder")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response deleteWorkorder(@QueryParam(NVWorkorderConstant.TASK_ID) Integer taskId) {
		try {
			if (Utils.isNotNull(taskId)) {
				logger.info("Going to reassign workorder with details taskId {}", taskId);
				return Response.ok(nvWorkorderService.deleteWorkorder(taskId)).build();
			}
			return Response.ok(NVWorkorderConstant.INVALID_PARAMETER_JSON).build();
		} catch (Exception e) {
			logger.error("Exception in getTotalWorkorderCount : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(NVWorkorderConstant.FAILURE_JSON).build();
		}
	}

	@POST
	@Path("deleteFiles")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response deleteFiles(List<Integer> idList) {
		logger.trace("inside the method deleteFiles idList {}", idList);
		try {
			if (idList != null && !idList.isEmpty()) {
				return nvWorkorderService.deleteFilesFromWoFileDetail(idList);
			}
			return Response.ok(NVWorkorderConstant.INVALID_PARAMETER_JSON).build();
		} catch (Exception e) {
			logger.error("Exception inside the method deleteFiles idList{},msg {}", idList, e.getMessage());
		}
		return Response.ok(NVWorkorderConstant.FAILURE_JSON).build();

	}

	/**
	 * @GET @Path("/reopenWorkOrder/{workOrderId}")
	 * @Produces(MediaType.APPLICATION_JSON) public Response
	 *                                       reopenWorkOrderById(@PathParam("workOrderId")
	 *                                       Integer workOrderId) throws
	 *                                       RestException { Response response; try
	 *                                       { if
	 *                                       (Utils.hasValidValue(String.valueOf(workOrderId)))
	 *                                       { response =
	 *                                       genricWorkOrderService.reopenWorkOrderById(workOrderId);
	 *                                       if (response == null) { response =
	 *                                       Response.ok(InBuildingConstants.FAILURE_JSON).build();
	 *                                       } } else { response =
	 *                                       Response.ok(InBuildingConstants.INVALID_PARAMETER_JSON).build();
	 *                                       } } catch (RestException e) {
	 *                                       logger.error("Error while downloading
	 *                                       floor plan image: {}",
	 *                                       ExceptionUtils.getMessage(e)); response
	 *                                       =
	 *                                       Response.ok(InBuildingConstants.EXCEPTION_SOMETHING_WENT_WRONG).build();
	 *                                       } return response; }
	 */

	@POST
	@Path("updatePresetIdForWorkorder")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response updatePresetIdForWorkorder(@QueryParam("presetId") Integer presetId,
			@QueryParam("workorderId") Integer workorderId) {

		logger.info("Going to update Preset id: {} for workorderId: {}", presetId, workorderId);
		Response response;
		try {
			if (Utils.isNotNull(presetId) && Utils.isNotNull(workorderId)) {
				response = nvWorkorderService.updatePresetIdForWorkorder(presetId, workorderId);
			} else {
				response = Response.ok(InBuildingConstants.INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.info("Error while updating preset id for workorder: {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(InBuildingConstants.FAILURE_JSON).build();
		}
		return response;
	}

	@GET
	@Path("checkIfReportAvailabel/{workorderId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response checkIfReportAvailabel(@PathParam("workorderId") String workorderId) {
		logger.info("inside the method checkStatusForPerform workorderId {} ", workorderId);
		if (Utils.hasValidValue(workorderId)) {
			return nvWorkorderService.checkIfReportAvailabel(workorderId);
		} else {
			return Response.ok(NVWorkorderConstant.INVALID_PARAMETER_JSON).build();
		}

	}
	@GET
	@Path("WOReopen")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response reopenRecipeById(@QueryParam("workorderId") String workorderId) {
		try {
			if (Utils.hasValidValue(String.valueOf(workorderId))) {
				return  nvWorkorderService.WOReopenByWOId(workorderId);
				
			} else {
				return Response.ok(InBuildingConstants.INVALID_PARAMETER_JSON).build();
			}
		} catch (RestException e) {
			logger.error("Error while reopen workorder  {}: {}",workorderId ,ExceptionUtils.getMessage(e));
			return Response.ok(InBuildingConstants.EXCEPTION_SOMETHING_WENT_WRONG).build();
		}
	}
}
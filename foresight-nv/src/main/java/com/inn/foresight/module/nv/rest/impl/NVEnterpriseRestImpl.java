package com.inn.foresight.module.nv.rest.impl;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.inn.commons.Symbol;
import com.inn.commons.Validate;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.http.HttpGetRequest;
import com.inn.commons.http.HttpRequest;
import com.inn.commons.lang.NumberUtils;
import com.inn.commons.lang.StringUtils;
import com.inn.commons.unit.Duration;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ConfigEnum;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.constants.InBuildingConstants;
import com.inn.foresight.core.infra.service.IBuildingDataService;
import com.inn.foresight.core.report.model.AnalyticsRepository;
import com.inn.foresight.core.report.utils.ReportUtils;
import com.inn.foresight.module.nv.NVConfigUtil;
import com.inn.foresight.module.nv.app.exceptions.InvalidEncryptionException;
import com.inn.foresight.module.nv.app.service.IDeviceInfoService;
import com.inn.foresight.module.nv.core.workorder.model.GWOMeta;
import com.inn.foresight.module.nv.inbuilding.result.service.IInBuildingResultService;
import com.inn.foresight.module.nv.layer3.constants.NVLayer3Constants;
import com.inn.foresight.module.nv.layer3.constants.QMDLConstant;
import com.inn.foresight.module.nv.layer3.service.INVLayer3Service;
import com.inn.foresight.module.nv.layer3.utils.NVLayer3Utils;
import com.inn.foresight.module.nv.livedrive.constants.LiveDriveConstant;
import com.inn.foresight.module.nv.livedrive.service.ILiveDriveService;
import com.inn.foresight.module.nv.livedrive.wrapper.TrackSpanWrapper;
import com.inn.foresight.module.nv.report.utils.ReportConstants;
import com.inn.foresight.module.nv.reportgeneration.service.INVReportGenerationService;
import com.inn.foresight.module.nv.rest.NVEnterpriseRest;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;
import com.inn.foresight.module.nv.workorder.recipe.service.IRecipeService;
import com.inn.foresight.module.nv.workorder.service.INVWorkorderService;
import com.inn.foresight.module.nv.workorder.stealth.kpi.service.ProbeDetailService;
import com.inn.foresight.module.nv.workorder.stealth.kpi.wrapper.ProbeDetailWrapper;
import com.inn.foresight.module.nv.workorder.stealth.service.IStealthTaskDetailService;
import com.inn.foresight.module.nv.workorder.utils.NVWorkorderUtils;
import com.inn.foresight.module.nv.workorder.wrapper.NVWorkorderWrapper;
import com.inn.product.security.utils.AuthenticationCommonUtil;
import com.inn.product.um.user.service.UserService;

@Path("/NVEnterprise")
@Produces("application/json")
@Consumes("application/json")
@Service("NVEnterpriseRestImpl")
public class NVEnterpriseRestImpl extends NVLayer3Utils implements NVEnterpriseRest {

	/** The logger. */
	Logger logger = LogManager.getLogger(NVEnterpriseRestImpl.class);

	@Autowired
	private INVWorkorderService nvWorkorderService;

	@Autowired
	private IRecipeService recipeService;

	/** The nv layer 3 service. */
	@Autowired
	private INVLayer3Service nvLayer3Service;

	@Autowired
	private ILiveDriveService iLiveDriveService;

	@Autowired
	private IBuildingDataService iBuildingDataService;

	/** The users service. */
	@Autowired
	private UserService usersService;

	@Autowired
	private IInBuildingResultService iInBuildingResultService;

	/** Instance of DeviceInfoServiceImpl. */
	@Autowired
	private IDeviceInfoService service;

	@Autowired
	private IStealthTaskDetailService stealthTaskDetailService;

	@Autowired
	private ProbeDetailService probeDetailService;
	@Autowired
	private INVReportGenerationService reportGenerationService;

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
							nvWorkorderService.getWorkorderDetailsByUser(workorderId, templateType, modifiedTime),
							null)).build();

				}
			}
			return Response.ok(
					AuthenticationCommonUtil.checkForValueEncryption(NVWorkorderConstant.INVALID_PARAMETER_JSON, null))
					.build();
		} catch (JsonSyntaxException | InvalidEncryptionException e) {
			return Response.ok(
					AuthenticationCommonUtil.checkForValueEncryption(NVWorkorderConstant.INVALID_ENCRYPTION_JSON, null))
					.build();
		} catch (Exception e) {
			logger.error("Exception inside the method getWorkorderDetails {}", Utils.getStackTrace(e));
			return Response.ok(AuthenticationCommonUtil.checkForValueEncryption(NVWorkorderConstant.FAILURE_JSON, null))
					.build();
		}
	}

	/**
	 * Completed Workorder by Completing Task by given TaskId.
	 * 
	 * @param taskId : BPMNTask Id corresponds to BPMNWorkorder
	 * @return Success Message if Task Completed, Failure Message otherwise
	 */
	@POST
	@Path("completeWOTask")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.TEXT_PLAIN)
	@Override
	public Response completeWOTask(String strTaskId) {
		logger.info("Going to complete Workorder for Task Id {}", strTaskId);
		if (strTaskId != null) {
			try {
				Integer taskId = Integer.parseInt(AuthenticationCommonUtil.checkForValueDecryption(strTaskId));
				Object analyticsRepository = nvWorkorderService.completeWorkorderByTaskId(taskId);
				if (analyticsRepository instanceof AnalyticsRepository) {
					String generateReport = ConfigUtils.getString(ConfigEnum.NV_TO_GENERATE_REPORT.getValue());
					if (generateReport != null && generateReport.equals(ForesightConstants.TRUE_LOWERCASE)) {
						try {
							// nvWorkorderService.generateReport((AnalyticsRepository) analyticsRepository);
						} catch (Exception e) {
							logger.error("Getting Exception in generating report, @method completeWOTask(), {}",
									ExceptionUtils.getStackTrace(e));
						}
					}
					logger.info("Workorder Completed Successfully. {}",
							AuthenticationCommonUtil.checkForValueEncryption(NVWorkorderConstant.SUCCESS_JSON, null));
					return Response.ok(
							AuthenticationCommonUtil.checkForValueEncryption(NVWorkorderConstant.SUCCESS_JSON, null))
							.build();
				}
				return Response.ok(AuthenticationCommonUtil.checkForValueEncryption(analyticsRepository, null)).build();
			} catch (Exception e) {
				logger.error(EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
				return Response
						.ok(AuthenticationCommonUtil.checkForValueEncryption(NVWorkorderConstant.FAILURE_JSON, null))
						.build();
			}
		}
		return Response
				.ok(AuthenticationCommonUtil.checkForValueEncryption(NVWorkorderConstant.INVALID_PARAMETER_JSON, null))
				.build();
	}

	/**
	 * Fetch the boundary of Any Geography Type.
	 * 
	 * @param geographyType
	 * @param geographyId
	 * @param request
	 * @return
	 * @throws RestException
	 */
	@POST
	@Path("getGeographyBoundryForMobile")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.TEXT_PLAIN)
	@Override
	public Response getGeographyBoundryForMobile(String strGeoData) {
		if (StringUtils.isNotEmpty(strGeoData)) {
			logger.info("Going to get Boundary for Mobile for data {}", strGeoData);
			return nvWorkorderService.getGeographyBoundryForMobile(strGeoData);
		} else {
			throw new RestException(NVWorkorderConstant.INVALID_PARAMETERS);
		}
	}

	/**
	 * Creates NV Workorder for given Template Type and Recipe details.
	 * 
	 * @param wrapper the wrapper
	 * @return Success Message if Workorder created, Failure Message otherwise
	 */
	@POST
	@Path("createWorkorderForMobile")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.TEXT_PLAIN)
	@Override
	public Response createWorkorderForMobile(String strWorkorder) {
		logger.info("Going to create Workorder With Given Recipe {}", strWorkorder);
		try {
			if (StringUtils.isNotBlank(strWorkorder)) {
				return Response.ok(AuthenticationCommonUtil
						.checkForValueEncryption(nvWorkorderService.createWorkorderFromMobile(strWorkorder), null))
						.build();
			}
		} catch (Exception e) {
			logger.error("Exception in createWorkorderForMobile : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(AuthenticationCommonUtil.checkForValueEncryption(NVWorkorderConstant.FAILURE_JSON, null))
					.build();
		}
		return Response
				.ok(AuthenticationCommonUtil.checkForValueEncryption(NVWorkorderConstant.INVALID_PARAMETER_JSON, null))
				.build();
	}

	@POST
	@Path("getSitesFromLatLong")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.TEXT_PLAIN)
	@Override
	public Response getSitesFromLatLong(String encLatLongMap) {
		logger.info("inside method getSitesFromLatLong {}", encLatLongMap);
		try {
			if (StringUtils.isNotBlank(encLatLongMap)) {
				return Response
						.ok(AuthenticationCommonUtil.checkForValueEncryption(
								nvWorkorderService.getSitesFromLatLong(
										AuthenticationCommonUtil.checkForValueDecryption(encLatLongMap), null, null),
								null))
						.build();
			}
		} catch (Exception e) {
			logger.error("Exception in getSitesFromLatLong : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(AuthenticationCommonUtil.checkForValueEncryption(NVWorkorderConstant.FAILURE_JSON, null))
					.build();
		}
		return Response
				.ok(AuthenticationCommonUtil.checkForValueEncryption(NVWorkorderConstant.INVALID_PARAMETER_JSON, null))
				.build();
	}

	/**
	 * Update Status to COMPLETED of Recipe corresponds to provided WORecipeMapping
	 * Id.
	 * 
	 * @param woRecipeMappingId : WORecipeMapping Id
	 * @return Success Message if Status updated, Failure Message otherwise
	 */
	@POST
	@Path("completeRecipe")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.TEXT_PLAIN)
	@Override
	public Response completeRecipe(String strWoRecipeMappingId) {
		logger.info("Going to complete Recipe for WORecipeMapping Id {}", strWoRecipeMappingId);

		if (strWoRecipeMappingId != null) {
			try {
				return Response.ok(AuthenticationCommonUtil.checkForValueEncryption(
						recipeService.completeRecipe(Integer
								.parseInt(AuthenticationCommonUtil.checkForValueDecryption(strWoRecipeMappingId))),
						null)).build();
			} catch (Exception e) {
				return Response
						.ok(AuthenticationCommonUtil.checkForValueEncryption(NVWorkorderConstant.FAILURE_JSON, null))
						.build();
			}
		}
		return Response
				.ok(AuthenticationCommonUtil.checkForValueEncryption(NVWorkorderConstant.INVALID_PARAMETER_JSON, null))
				.build();
	}

	/**
	 * Update Status to INPROGRESS of Recipe corresponds to provided WORecipeMapping
	 * Id.
	 * 
	 * @param woRecipeMappingId : WORecipeMapping Id
	 * @return Success Message if Status updated, Failure Message otherwise
	 */
	@POST
	@Path("startRecipe")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.TEXT_PLAIN)
	@Override
	public Response startRecipe(String strWoRecipeMappingId, @QueryParam(TASK_ID) String taskId) {
		logger.info("Going to start Recipe for WORecipeMapping Id {}", strWoRecipeMappingId);

		if (strWoRecipeMappingId != null) {
			try {
				return Response.ok(AuthenticationCommonUtil.checkForValueEncryption(recipeService.startRecipe(
						Integer.parseInt(AuthenticationCommonUtil.checkForValueDecryption(strWoRecipeMappingId)), false,
						taskId), null)).build();
			} catch (Exception e) {
				return Response
						.ok(AuthenticationCommonUtil.checkForValueEncryption(NVWorkorderConstant.FAILURE_JSON, null))
						.build();
			}
		}
		return Response
				.ok(AuthenticationCommonUtil.checkForValueEncryption(NVWorkorderConstant.INVALID_PARAMETER_JSON, null))
				.build();
	}

	/**
	 * Sync WO recipe QMDL/CSV files in HDFS and also insert WOFileDetail instance
	 * in DB.
	 *
	 * @param woRecipeMappingId the wo recipe mapping id whose file is to sync
	 * @param isRetried         the flag to acknowledge that recipe is retried again
	 * @param fileType          represents type of files present in the zip
	 * @param inputFile         the inputStream of zip file to sync
	 * @param fileName          the file name
	 * @return the status of sync call
	 */
	@POST
	@Path("/syncWorkOrderFile")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Override
	public String syncRecipeFiles(@Multipart(value = WO_RECIPE_MAPPING_ID) String encrWORecipeMappingId,
			@QueryParam(IS_RETRIED) Boolean isRetried, @QueryParam(FILE_TYPE) String fileType,
			@Multipart(value = ForesightConstants.FILE) InputStream inputFile,
			@Multipart(value = ForesightConstants.FILENAME) String fileName, @QueryParam(TASK_ID) String taskId) {
		logger.info("Going to persit Recipe Files");
		String localFilePath=null;
		try {
			Validate.checkNoneNull(encrWORecipeMappingId, inputFile, fileName, fileType);
			logger.info("fileName {} fileType {}", fileName, fileType);
			if ((fileName.endsWith(ZIP_FILE_EXTENTION))
					&& (StringUtils.equalsIgnoreCaseAny(fileType, ZIP_FILE_TYPE, CSV_FILE_TYPE, QMDL_FILE_TYPE))
					&& inputFile != null) {
				Integer woRecipeMappingId = Integer
						.parseInt(AuthenticationCommonUtil.checkForValueDecryption(encrWORecipeMappingId));

				localFilePath = ConfigUtils.getString(NVConfigUtil.LAYER3_DUMP_FILE_PATH)
						+ String.valueOf(new Date().getTime()) + ForesightConstants.FORWARD_SLASH;

				String jsonMsg = nvLayer3Service.syncWorkorderFile(woRecipeMappingId, isRetried, inputFile, fileName,
						fileType, taskId, localFilePath);

				logger.info("Done persit Recipe Files");
				
				return AuthenticationCommonUtil
						.checkForValueEncryption(QMDLConstant.MESSAGE_START + jsonMsg + QMDLConstant.MESSAGE_END, null);

			} else {
				logger.error(
						"File is not of QMDL or CSV Type or inputstream is null fileName or fileType or inputFile");
				throw new RestException(FILE_IS_NOT_OF_REQUIRED_TYPE);
			}
			// return AuthenticationCommonUtil.checkForValueEncryption(SUCCESS_JSON, null);
		} catch (NullPointerException e) {
			logger.error("NullPointerException in syncRecipeFiles : {}", ExceptionUtils.getStackTrace(e));
			return AuthenticationCommonUtil.checkForValueEncryption(WO_RECIPE_MAPPING_ID_IS_NULL, null);
		} catch (RestException e) {
			logger.error("RestException in syncRecipeFiles : {}", ExceptionUtils.getStackTrace(e));
			return AuthenticationCommonUtil.checkForValueEncryption(e.getMessage(), null);
		} catch (IllegalArgumentException e) {
			logger.error("Exception in syncRecipeFiles === : {}", ExceptionUtils.getStackTrace(e));
			return AuthenticationCommonUtil.checkForValueEncryption(FILE_IS_EMPTY_JSON, null);
		} catch (Exception e) {
			logger.error("Exception in syncRecipeFiles : {}", ExceptionUtils.getStackTrace(e));
			return AuthenticationCommonUtil.checkForValueEncryption(FAILURE_JSON, null);
		}finally {
			try {
				FileUtils.deleteDirectory(new File(localFilePath));
			} catch (Exception e) {
				logger.error("Exception inside the method syncWorkorderFile while deletingDirectory {}",
						e.getMessage());
			}
		}
	}

	/**
	 * Sync WO recipe QMDL/CSV files in HDFS and also insert WOFileDetail instance
	 * in DB.
	 *
	 * @param woRecipeMappingId the wo recipe mapping id whose file is to sync
	 * @param isRetried         the flag to acknowledge that recipe is retried again
	 * @param fileType          represents type of files present in the zip
	 * @param inputFile         the inputStream of zip file to sync
	 * @param fileName          the file name
	 * @return the status of sync call
	 */
	@POST
	@Path("/syncWorkOrderFileForBenchMarking")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Override
	public String syncWorkOrderFileForBenchMarking(
			@Multipart(value = WO_RECIPE_MAPPING_ID) String encrWORecipeMappingId,
			@QueryParam(IS_RETRIED) Boolean isRetried, @QueryParam(FILE_TYPE) String fileType,
			@Multipart(value = ForesightConstants.FILE) InputStream inputFile,
			@Multipart(value = ForesightConstants.FILENAME) String fileName) {
		logger.info("Going to persit syncWorkOrderFileForBenchMarking Files {} {}  {} ", encrWORecipeMappingId,
				fileType, fileName);
		try {
			Validate.checkNoneNull(encrWORecipeMappingId, inputFile, fileName, fileType);
			if ((fileName.endsWith(ZIP_FILE_EXTENTION))
					&& (StringUtils.equalsIgnoreCaseAny(fileType, ZIP_FILE_TYPE, CSV_FILE_TYPE, QMDL_FILE_TYPE))
					&& inputFile != null) {
				String woRecipeMappingIdList = AuthenticationCommonUtil.checkForValueDecryption(encrWORecipeMappingId);
				nvLayer3Service.syncWorkOrderFileForBenchMarking(woRecipeMappingIdList, isRetried, inputFile, fileName,
						fileType);
			} else {
				logger.error(
						"File is not of QMDL or CSV Type or inputstream is null fileName or fileType or inputFile");
				throw new RestException(FILE_IS_NOT_OF_REQUIRED_TYPE);
			}
			logger.info("Done persit Recipe Files");
			return AuthenticationCommonUtil.checkForValueEncryption(SUCCESS_JSON, null);
		} catch (NullPointerException e) {
			logger.error("NullPointerException in syncRecipeFiles : {}", ExceptionUtils.getStackTrace(e));
			return AuthenticationCommonUtil.checkForValueEncryption(WO_RECIPE_MAPPING_ID_IS_NULL, null);
		} catch (RestException e) {
			logger.error("RestException in syncRecipeFiles : {}", ExceptionUtils.getStackTrace(e));
			return AuthenticationCommonUtil.checkForValueEncryption(e.getMessage(), null);
		} catch (IllegalArgumentException e) {
			logger.error("Exception in syncRecipeFiles : {}", ExceptionUtils.getStackTrace(e));
			return AuthenticationCommonUtil.checkForValueEncryption(FILE_IS_EMPTY_JSON, null);
		} catch (Exception e) {
			logger.error("Exception in syncRecipeFiles : {}", ExceptionUtils.getStackTrace(e));
			return AuthenticationCommonUtil.checkForValueEncryption(FAILURE_JSON, null);
		}
	}

	/**
	 * Sync live drive json in kafka every 10 sec.
	 * 
	 * @param trackSpanWrapper
	 * @return application/json
	 * @throws RestException
	 */
	@POST
	@Path("/syncLiveDriveJson")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public String syncLiveDriveJsonFileToKafka(TrackSpanWrapper trackSpanWrapper) {
		logger.info("In side syncLiveDriveJsonFileToKafka method");
		if (trackSpanWrapper == null)
			throw new RestException(
					AuthenticationCommonUtil.checkForValueEncryption(LiveDriveConstant.INVALID_PARAMETER, null));
		if (trackSpanWrapper.getDriveid() == null)
			throw new RestException(
					AuthenticationCommonUtil.checkForValueEncryption(LiveDriveConstant.INVALID_PARAMETER, null));
		try {
			iLiveDriveService.syncJsonDataToKafka(trackSpanWrapper);
			return AuthenticationCommonUtil.checkForValueEncryption(ForesightConstants.SUCCESS_JSON, null);
		} catch (Exception e) {
			return AuthenticationCommonUtil.checkForValueEncryption(ForesightConstants.FAILURE_JSON, null);
		}
	}

	/**
	 * Live Drive Remaining data sync.
	 *
	 * @param inputFile the input file
	 * @param fileName  the file name
	 * @return the string
	 * @throws RestException the rest exception
	 */

	@POST
	@Path("syncRemainingLiveDriveData")
	@Consumes("multipart/form-data")
	@Produces("text/html")
	@Override
	public String syncRemainingLiveDriveData(@Multipart("file") InputStream inputFile,
			@Multipart("fileName") String fileName) {
		logger.info("Going  to synch Live drive remaining  result data : {}", fileName);
		if (inputFile == null || fileName == null) {
			throw new RestException(
					AuthenticationCommonUtil.checkForValueEncryption(LiveDriveConstant.INVALID_ZIP, null));
		}
		return iLiveDriveService.recordAllLocation(inputFile);
	}

	/**
	 * Get Nearest Building From Location.
	 *
	 * @return the response, nearest six building's data
	 * @throws RestException the rest exception
	 * @param latitude  latitude of the location
	 * @param longitude longitude of the location
	 */
	@GET
	@Path("getNearestBuildingsFromLocation/{latitude}/{longitude}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response getNearestBuildingsFromLocation(@PathParam("latitude") Double latitude,
			@PathParam("longitude") Double longitude) {
		Response response = null;
		try {
			if (NumberUtils.isValidNumber(latitude) && NumberUtils.isValidNumber(longitude)) {
				response = Response.ok(iBuildingDataService.getNearestBuildingsFromLocation(latitude, longitude))
						.build();
			} else {
				response = Response.ok(InBuildingConstants.INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}

		return response;
	}

	/**
	 * Gets the building list by name.
	 *
	 * @param name name to search buildings.
	 * @return the building list by name
	 * @throws RestException the rest exception
	 */
	@GET
	@Path("getBuildingListByName/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response getBuildingListByName(@PathParam("name") String name) {
		Response response = null;
		try {
			if (StringUtils.isNotBlank(name)) {
				response = Response.ok(iBuildingDataService.getBuildingListByName(name)).build();
			} else {
				response = Response.ok(InBuildingConstants.INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
		return response;
	}

	/**
	 * Creates the building from app.
	 *
	 * @param buildingData encrypted building wrapper json
	 * @return the response
	 * @throws RestException the rest exception
	 */
	@POST
	@Path("createBuildingData")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.TEXT_PLAIN)
	@Override
	public Response createBuildingData(String buildingData) {
		Response response = null;
		try {
			if (StringUtils.isNotBlank(buildingData)) {
				response = Response
						.ok(AuthenticationCommonUtil
								.checkForValueEncryption(iBuildingDataService.createBuildingData(buildingData), null))
						.build();
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}

		return response;
	}

	@POST
	@Path("/uploadFloorPlanData/{unitId}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Override
	public Response uploadFloorPlanData(@PathParam("unitId") Integer unitId,
			@Multipart(value = ForesightConstants.FILE) InputStream inputStream,
			@Multipart(value = ForesightConstants.FILENAME) String fileName) {
		Response response = Response.ok(InBuildingConstants.EXCEPTION_SOMETHING_WENT_WRONG).build();
		try {
			if (!Utils.isNotNull(unitId) || !StringUtils.isNotBlank(fileName) || inputStream == null) {
				response = Response.ok(InBuildingConstants.INVALID_PARAMETER_JSON).build();
			} else {
				response = Response.ok(iInBuildingResultService.uploadFloorPlanData(unitId, inputStream, fileName))
						.build();
			}
		} catch (RestException e) {
			logger.error("Error while uploading floor plan data: {}", ExceptionUtils.getMessage(e));
			response = Response.ok(InBuildingConstants.EXCEPTION_SOMETHING_WENT_WRONG).build();
		}
		return response;
	}

	/**
	 * App logout.
	 *
	 * @param request  the request
	 * @param response the response
	 * @return the string
	 */

	@POST
	@Path("appLogout")
	@Consumes(MediaType.TEXT_PLAIN)
	@Override
	public String appLogout(@Context HttpServletRequest request, @Context HttpServletResponse response) {
		logger.info("Inside  appLogout Query string :{}", request.getQueryString());
		String deviceId = request.getParameter("deviceId");
		logger.info("Going to get deviceId parameter{}", deviceId);
		try {
			String result = usersService.appLogout(request, response);

			if (AuthenticationCommonUtil.checkForValueDecryption(result).equalsIgnoreCase("{\"result\":\"success\"}")
					&& deviceId != null) {
				service.updateUserForDevice(deviceId);
			}
			return result;

		} catch (Exception e) {
			logger.error("exception class is: {}  and message : {}  and exception : {}", e.getClass(), e.getMessage(),
					e);
			return AuthenticationCommonUtil.checkForValueEncryption("{\"result\":\"failure\"}", null);
		}
	}

	@GET
	@Path("/downloadFloorPlanData/{unitId}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Override
	public Response downloadFloorPlanData(@PathParam("unitId") Integer unitId) {
		Response response = Response.ok(InBuildingConstants.INVALID_PARAMETER_JSON).build();
		try {
			if (Utils.isNotNull(unitId)) {
				Response.ResponseBuilder builder = Response.status(200);
				response = builder.entity(iInBuildingResultService.downloadFloorPlanData(unitId))
						.header("Content-Type", MediaType.APPLICATION_OCTET_STREAM)
						.header("Content-Disposition",
								"attachment;filename=" + InBuildingConstants.FLOOR_PLAN_FILE_NAME + unitId
								+ InBuildingConstants.FLOOR_PLAN_FILE_SEPERATOR + System.currentTimeMillis()
								+ InBuildingConstants.FLOOR_PLAN_FILE_EXTENSION)
						.build();
			}
		} catch (RestException e) {
			logger.error("Error while preparing download for floor plan data: {}", ExceptionUtils.getMessage(e));
			response = Response.ok(InBuildingConstants.EXCEPTION_SOMETHING_WENT_WRONG).build();
		}
		return response;
	}

	@GET
	@Path("/getReportById/{analyticsrepositoryId}")
	@Override
	public Response getReportById(@PathParam("analyticsrepositoryId") Integer analyticsRepositoryId
			,@Context HttpServletRequest request) {
		logger.info("Inside method getReportById with input analyticsRepositoryId {} ", analyticsRepositoryId);
		try {
			HttpRequest http = new HttpGetRequest(getDropwizardUrl(analyticsRepositoryId, request.getQueryString()));
			Response.ResponseBuilder builder = Response.ok(http.getByteArray())
					.header(ReportConstants.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM)
					//				.header(HttpHeaders.ACCEPT, MediaType.APPLICATION_OCTET_STREAM)
					.header(ReportConstants.CONTENT_DISPOSITION,NVLayer3Constants.ATTACHMENT_FILE +
							reportGenerationService.getDownloadFileName(analyticsRepositoryId, 	request.getParameter("extension"))+ "\"");

			return builder.build();


			/*	HttpResponse response = ReportUtils
					.sendGetRequestWithoutTimeOut(getDropwizardUrl(analyticsRepositoryId, request.getQueryString()));
			Response.ResponseBuilder builder = Response.status(200);
			logger.info("response.getEntity().getContentType()====={}",response.getEntity().getContentType());
			builder = builder.entity(response.getEntity().getContent())
					.header(ReportConstants.CONTENT_TYPE, response.getEntity().getContentType())
					.header(ReportConstants.CONTENT_DISPOSITION,
							response.getFirstHeader(ReportConstants.CONTENT_DISPOSITION).getValue());
			return builder.build();*/

		} catch (Exception e) {
			logger.error("Exception in getReportById : {}", ExceptionUtils.getStackTrace(e));
		}
		return Response.ok(ForesightConstants.FAILURE_JSON).build();

	}

	private String getDropwizardUrl(Integer rowkey, String queryString) {
		String finalUrl = ConfigUtils.getString(ConfigEnum.MICRO_SERVICE_BASE_URL.getValue())
				.concat(ConfigUtils.getString(ConfigEnum.DROPWIZARD_URL.getValue())).concat(rowkey.toString());
		if (StringUtils.isNotEmpty(queryString)) {
			finalUrl += Symbol.QUESTION_MARK_STRING + queryString;
		}
		return finalUrl;
	}

	/**
	 * Download floor plan.
	 *
	 * @param unitId     the unit id
	 * @param Technology the technology
	 * @return the response
	 */
	@GET
	@Path("/downloadFloorPlan/{unitId}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Override
	public Response downloadFloorPlan(@PathParam("unitId") Integer unitId) {
		Response response;
		try {
			if (Utils.hasValidValue(String.valueOf(unitId))) {
				response = iInBuildingResultService.downloadFloorPlan(unitId);
				if (response == null) {
					response = Response.ok(InBuildingConstants.FAILURE_JSON).build();
				}
			} else {
				response = Response.ok(InBuildingConstants.INVALID_PARAMETER_JSON).build();
			}
		} catch (RestException e) {
			logger.error("Error while downloading floor plan image: {}", ExceptionUtils.getMessage(e));
			response = Response.ok(InBuildingConstants.EXCEPTION_SOMETHING_WENT_WRONG).build();
		}
		return response;
	}

	@GET
	@Path("getInBuildingReportFloorWise")
	@Produces(MediaType.APPLICATION_JSON)	
	@Override
	public Response getInBuildingReportFloorWise(@QueryParam("floorId") Integer floorId,
			@QueryParam("inbuildingid") Integer inbuildingid, @QueryParam("technology") String technology,
			@Context HttpServletRequest request) {
		logger.info("Inside method getInBuildingReportFloorWise with input json floorId {}   inbuildingid {}", floorId,
				inbuildingid);
		try {
			Duration duration = Duration
					.minutes(Integer.parseInt(ConfigUtils.getString(ConfigEnum.TIMEOUT_VALUE.getValue())));

			return Response.ok(NVLayer3Utils.sendHttpGetRequest(getDropwizardUrlForWoReport(request,
					ConfigUtils.getString(ConfigEnum.MICRO_SERVICE_BASE_URL.getValue(), Boolean.TRUE).concat(
							ConfigUtils.getString(ConfigEnum.CREATE_INBUILDING_FLOOR_REPORT_URL, Boolean.TRUE))),
					false, duration).getString()).build();

		} catch (Exception e) {
			logger.error("Exception in getInBuildingReportFloorWise : {}", ExceptionUtils.getStackTrace(e));

		}
		return null;

	}

	@GET
	@Path("getInbuildingReportPdf/")
	@Produces(MediaType.APPLICATION_JSON)	
	@Override
	public Response getInbuildingReportPdf(@QueryParam("recipeId") Integer recipeId,
			@QueryParam("operator") String operator, @QueryParam("inbuildingid") Integer inbuildingid,
			@QueryParam("workorderId") Integer workorderId, @QueryParam("technology") String technology,
			@Context HttpServletRequest request) {
		try {
			Duration duration = Duration
					.minutes(Integer.parseInt(ConfigUtils.getString(ConfigEnum.TIMEOUT_VALUE.getValue())));

			return Response.ok(NVLayer3Utils.sendHttpGetRequest(
					getDropwizardUrlForWoReport(request,
							ConfigUtils.getString(ConfigEnum.MICRO_SERVICE_BASE_URL.getValue(), Boolean.TRUE).concat(
									ConfigUtils.getString(ConfigEnum.CREATE_INBUILDING_REPORT_URL, Boolean.TRUE))),
					false, duration).getString()).build();

		} catch (Exception e) {
			logger.error("Exception in getInbuildingReportPdf : {}", ExceptionUtils.getStackTrace(e));

		}
		return null;
	}

	private static String getQueryParamsInUrl(Integer recipeId, String operator, Integer inbuildingid,
			Integer workorderId, Integer floorId, String technology) {
		String finalUrl = "";
		String extraCharacter = "?";
		if (recipeId != null) {
			finalUrl = finalUrl + extraCharacter + "recipeId=" + recipeId;
			extraCharacter = "&";
		}
		if (operator != null) {
			finalUrl = finalUrl + extraCharacter + "operator=" + operator;
			extraCharacter = "&";
		}
		if (inbuildingid != null) {
			finalUrl = finalUrl + extraCharacter + "inbuildingid=" + inbuildingid;
			extraCharacter = "&";
		}
		if (workorderId != null) {
			finalUrl = finalUrl + extraCharacter + "workorderId=" + workorderId;
			extraCharacter = "&";
		}
		if (floorId != null) {
			finalUrl = finalUrl + extraCharacter + "floorId=" + floorId;
		}
		if (technology != null) {
			finalUrl = finalUrl + extraCharacter + "technology=" + technology;
		}
		return finalUrl;
	}

	@POST
	@Path("/processWOReportDump")
	@Override
	public Response processWOReportDump(@QueryParam(WORKORDER_ID) Integer workorderId, Map<String, List<String>> map,
			@Context HttpServletRequest request) {
		try {
			if (workorderId != null && map != null) {
				String json = new Gson().toJson(map);
				StringEntity httpEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
				Duration duration = Duration
						.minutes(Integer.parseInt(ConfigUtils.getString(ConfigEnum.TIMEOUT_VALUE.getValue())));

				return Response
						.ok(NVLayer3Utils
								.sendHttpPostRequest(getDropwizardUrlForWoReport(request,
										ConfigUtils
										.getString(ConfigEnum.MICRO_SERVICE_BASE_URL.getValue(), Boolean.TRUE)
										.concat(ConfigUtils.getString(ConfigEnum.DROPWIZARD_WO_DUMP_FILE_URL,
												Boolean.TRUE))),
										httpEntity, true, duration)
								.getString())
						.build();

			} else {
				logger.error(QMDLConstant.INVALID_PARAMETER);
				return Response.ok(INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error("Exception in processWOReportDump : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(QMDLConstant.EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
		}
	}

	@POST
	@Path("/addRecipeIntoWO")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	@Override
	public Response addRecipeIntoWO(String encryptedString) {
		try {
			if (Utils.hasValidValue(encryptedString)) {
				NVWorkorderWrapper wrapper = new Gson().fromJson(
						AuthenticationCommonUtil.checkForValueDecryption(encryptedString), NVWorkorderWrapper.class);
				return Response.ok(AuthenticationCommonUtil
						.checkForValueEncryption(nvWorkorderService.addRecipeIntoWO(wrapper), null)).build();
			} else {
				return Response.ok(AuthenticationCommonUtil.checkForValueEncryption(INVALID_PARAMETER_JSON, null))
						.build();
			}
		} catch (Exception e) {
			logger.error("Exception in addRecipe : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(AuthenticationCommonUtil.checkForValueEncryption(NVWorkorderConstant.FAILURE_JSON, null))
					.build();
		}
	}


	@GET
	@Path("/getWOReportDump")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Override
	public Response getWOReportDump(@QueryParam(FILE_NAME) String fileName, @Context HttpServletRequest request) {
		try {
			/*HttpResponse response = ReportUtils.sendGetRequestWithoutTimeOut(getDropwizardUrlForWoReport(request,
					ConfigUtils.getString(ConfigEnum.MICRO_SERVICE_BASE_URL.getValue(), Boolean.TRUE).concat(
							ConfigUtils.getString(ConfigEnum.DROPWIZARD_WO_DUMP_DOWNLOAD_FILE_URL, Boolean.TRUE))));
			Response.ResponseBuilder builder = Response.status(200);
			builder = builder.entity(response.getEntity().getContent())
					.header(ReportConstants.CONTENT_TYPE, response.getEntity().getContentType())
					.header(ReportConstants.CONTENT_DISPOSITION,
							response.getFirstHeader(ReportConstants.CONTENT_DISPOSITION).getValue());*/
			HttpRequest http = new HttpGetRequest(getDropwizardUrlForWoReport(request,
					ConfigUtils.getString(ConfigEnum.MICRO_SERVICE_BASE_URL.getValue(), Boolean.TRUE).concat(
							ConfigUtils.getString(ConfigEnum.DROPWIZARD_WO_DUMP_DOWNLOAD_FILE_URL, Boolean.TRUE))));
			Response.ResponseBuilder builder = Response.ok(http.getByteArray())
					.header(ReportConstants.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM)
					.header(ReportConstants.CONTENT_DISPOSITION,NVLayer3Constants.ATTACHMENT_FILE +
							fileName+ "\"");

			return builder.build();
		} catch (Exception e) {
			logger.error("Exception in getDriveDetailReceipeWise : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(QMDLConstant.EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
		}
	}

	private String getDropwizardUrlForWoReport(HttpServletRequest request, String url) {
		String queryString = request.getQueryString();
		logger.info("Getting url {}  query {} ", url, queryString);
		if (StringUtils.isNotEmpty(queryString)) {
			url += Symbol.QUESTION_MARK_STRING + queryString;
		}
		logger.info("Getting url {}", url);
		return url;
	}

	@GET
	@Path("/getReportByAnalyticsrepositoryId/{analyticsrepositoryId}")
	@Override
	public Response getReportByAnalyticsrepositoryId(
			@PathParam("analyticsrepositoryId") Integer analyticsRepositoryId) {
		logger.info("Inside method getReportById with input analyticsRepositoryId {} ", analyticsRepositoryId);
		try {
			HttpResponse response = ReportUtils
					.sendGetRequestWithoutTimeOut(getDropwizardReportUrl(analyticsRepositoryId));
			Response.ResponseBuilder builder = Response.status(200);
			builder = builder.entity(response.getEntity().getContent())
					.header(ReportConstants.CONTENT_TYPE, response.getEntity().getContentType())
					.header(ReportConstants.CONTENT_DISPOSITION,
							response.getFirstHeader(ReportConstants.CONTENT_DISPOSITION).getValue());
			return builder.build();

		} catch (Exception e) {
			logger.error("Exception in getReportByAnalyticsrepositoryId : {}", ExceptionUtils.getStackTrace(e));

		}
		return Response.ok(ForesightConstants.FAILURE_JSON).build();

	}

	private String getDropwizardReportUrl(Integer rowkey) {
		return ConfigUtils.getString(ConfigEnum.DROPWIZARD_REPORT_URL.getValue()).concat(rowkey.toString());

	}

	/**
	 * Gets the workorder part in set.
	 *
	 * @return the workorder part set
	 * @throws RestException the rest exception
	 */
	@GET
	@Path("/geFilePartByRecipMappingId")
	@Override
	public Response geFilePartByRecipMappingId(@QueryParam("woRecipeMappingId") String woRecipeMappingId) {
		Response response = null;
		try {
			if (woRecipeMappingId != null) {
				response = Response.ok(AuthenticationCommonUtil.checkForValueEncryption(
						nvWorkorderService.findFilePartByRecipeMappingId(woRecipeMappingId), null)).build();
			} else {
				logger.warn("invalid paramter geFilePartByRecipMappingId {}", woRecipeMappingId);
				response = Response
						.ok(AuthenticationCommonUtil.checkForValueEncryption(RESULT_INVALID_PARAMETER_JSON, null))
						.build();
			}
		} catch (Exception e) {
			logger.error("Exception in geFilePartByRecipMappingId : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(
					AuthenticationCommonUtil.checkForValueEncryption(RESULT_EXCEPTION_SOMETHING_WENT_WRONG_JSON, null))
					.build();

		}
		return response;
	}

	/**
	 * Mark as Deletes files in workorder by part.
	 * 
	 * @return the workorder part set
	 * @throws RestException the rest exception
	 */
	@POST
	@Path("/deleteFileByRecipeMappingId")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.TEXT_PLAIN)
	@Override
	public Response deleteFileByRecipeMappingId(String woRecipeMappingId) {
		Response response = null;
		try {
			if (woRecipeMappingId != null) {
				response = Response.ok(AuthenticationCommonUtil.checkForValueEncryption(
						nvWorkorderService.deleteFileByRecipeMappingId(woRecipeMappingId), null)).build();
			} else {
				logger.warn("invalid paramter deleteFileByRecipeMappingId {} ", woRecipeMappingId);
				response = Response
						.ok(AuthenticationCommonUtil.checkForValueEncryption(RESULT_INVALID_PARAMETER_JSON, null))
						.build();
			}
		} catch (Exception e) {
			logger.error("Exception in deleteFilePartByRecipeMappingId : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(
					AuthenticationCommonUtil.checkForValueEncryption(RESULT_EXCEPTION_SOMETHING_WENT_WRONG_JSON, null))
					.build();
		}
		return response;
	}

	/**
	 * Mark as Deletes files in workorder by part.
	 * 
	 * @return the workorder part set
	 * @throws RestException the rest exception
	 */
	@POST
	@Path("/getWorkorderStatus")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.TEXT_PLAIN)
	@Override
	public Response getWorkorderStatusByTaskIdList(String encrTaskIdList) {
		Response response = null;
		try {
			if (encrTaskIdList != null) {
				logger.info("Going to get workorder status for encrypted taskIdList {}", encrTaskIdList);
				response = Response.ok(AuthenticationCommonUtil.checkForValueEncryption(
						nvWorkorderService.getWorkorderStatusByTaskIdList(encrTaskIdList), null)).build();
			} else {
				response = Response
						.ok(AuthenticationCommonUtil.checkForValueEncryption(RESULT_INVALID_PARAMETER_JSON, null))
						.build();
			}
		} catch (Exception e) {
			logger.error("Exception in getWorkorderStatusByTaskIdList : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(AuthenticationCommonUtil.checkForValueEncryption(FAILURE_JSON, null)).build();
		}
		return response;
	}

	@POST
	@Path("getDeviceListByUserName")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response getDeviceListByUserName(List<String> userList, @QueryParam("lLimit") Integer lLimit,
			@QueryParam("uLimit") Integer uLimit, @QueryParam("isInstalled") Boolean isInstalled) {
		logger.info("Going to getDeviceIdListByUser: {} , isInstalled {}", userList, isInstalled);
		try {
			if (userList != null) {
				return Response.ok(service.getDeviceListByUserName(userList, lLimit, uLimit, isInstalled)).build();
			} else {
				return Response.ok(INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error("Exception in getDeviceListByUserName : {} ", ExceptionUtils.getStackTrace(e));
			return Response.ok(FAILURE_JSON).build();
		}
	}

	@POST
	@Path("getDeviceListCountByUserName")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response getDeviceListCountByUserName(List<String> userList,
			@QueryParam("isInstalled") Boolean isInstalled) {
		logger.info("Going to getDeviceListCountByUserName: {} , isInstalled {}", userList, isInstalled);
		try {
			if (userList != null) {
				return Response.ok(service.getDeviceListCountByUserName(userList, isInstalled)).build();

			} else {
				return Response.ok(INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error("Exception in getDeviceListCountByUserName : {} ", ExceptionUtils.getStackTrace(e));
			return Response.ok(FAILURE_JSON).build();
		}
	}

	@GET
	@Path("/getWOReportDumpForStealth")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Override
	public Response getWOReportDumpForStealth(@QueryParam(WORKORDER_ID) String workorderId,
			@QueryParam(TASK_ID) String taskId, @QueryParam(DATE) String date, @Context HttpServletRequest request) {
		try {
			if (workorderId != null && taskId != null) {
				HttpResponse response = ReportUtils.sendGetRequestWithoutTimeOut(getDropwizardUrlForWoReport(request,
						ConfigUtils.getString(ConfigEnum.MICRO_SERVICE_BASE_URL) + ConfigUtils
						.getString(ConfigEnum.DROPWIZARD_STEALTH_WO_DUMP_DOWNLOAD_FILE_URL, Boolean.TRUE)));
				Response.ResponseBuilder builder = Response.status(200);
				builder = builder.entity(response.getEntity().getContent())
						.header(ReportConstants.CONTENT_TYPE, response.getEntity().getContentType())
						.header(ReportConstants.CONTENT_DISPOSITION,
								response.getFirstHeader(ReportConstants.CONTENT_DISPOSITION).getValue());
				return builder.build();
			} else {
				return Response.ok(INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error("Exception in getDriveDetailReceipeWise : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(QMDLConstant.EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
		}
	}

	@GET
	@Path("/getDeviceReportForStealth")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Override
	public Response getDeviceReportForStealth(@QueryParam(WORKORDER_ID) String workorderId,
			@QueryParam(TASK_ID) String taskId, @QueryParam(DATE) String date, @Context HttpServletRequest request) {
		try {
			HttpResponse response = ReportUtils.sendGetRequestWithoutTimeOut(getDropwizardUrlForWoReport(request,
					ConfigUtils.getString(ConfigEnum.MICRO_SERVICE_BASE_URL) + ConfigUtils
					.getString(ConfigEnum.DROPWIZARD_STEALTH_PDF_DOWNLOAD_FILE_URL, Boolean.TRUE)));
			Response.ResponseBuilder builder = Response.status(200);
			builder = builder.entity(response.getEntity().getContent())
					.header(ReportConstants.CONTENT_TYPE, response.getEntity().getContentType())
					.header(ReportConstants.CONTENT_DISPOSITION,
							response.getFirstHeader(ReportConstants.CONTENT_DISPOSITION).getValue());
			return builder.build();
		} catch (Exception e) {
			logger.error("Exception in getDeviceReportForStealth : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(QMDLConstant.EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
		}
	}

	@GET
	@Path("getDeviceList")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response getDeviceList(@QueryParam("lLimit") Integer lLimit, @QueryParam("uLimit") Integer uLimit) {
		logger.info("Going to getDeviceList: ");
		try {
			return Response.ok(service.getDeviceList(lLimit, uLimit)).build();

		} catch (Exception e) {
			logger.error("Exception in getDeviceList: {} ", ExceptionUtils.getStackTrace(e));
			return Response.ok(FAILURE_JSON).build();
		}
	}

	@GET
	@Path("getDeviceListCount")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response getDeviceListCount() {
		logger.info("Going to getDeviceListCount:  ");
		try {
			return Response.ok(service.getDeviceListCount()).build();

		} catch (Exception e) {
			logger.error("Exception in getDeviceListCount: {} ", ExceptionUtils.getStackTrace(e));
			return Response.ok(FAILURE_JSON).build();
		}
	}

	@GET
	@Path("/getPDFReportForStealthWO")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Override
	public Response getPDFReportForStealthWO(@QueryParam(WORKORDER_ID) String workorderId,
			@Context HttpServletRequest request) {
		try {
			HttpResponse response = ReportUtils.sendGetRequestWithoutTimeOut(
					getDropwizardUrlForWoReport(request, ConfigUtils.getString(ConfigEnum.MICRO_SERVICE_BASE_URL)
							+ ConfigUtils.getString(ConfigEnum.DROPWIZARD_STEALTH_WO_PDF_REPORT_URL, Boolean.TRUE)));
			Response.ResponseBuilder builder = Response.status(200);
			builder = builder.entity(response.getEntity().getContent())
					.header(ReportConstants.CONTENT_TYPE, response.getEntity().getContentType())
					.header(ReportConstants.CONTENT_DISPOSITION,
							response.getFirstHeader(ReportConstants.CONTENT_DISPOSITION).getValue());
			return builder.build();
		} catch (Exception e) {
			logger.error("Exception in getPDFReportForStealthWO : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(QMDLConstant.EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
		}
	}

	@GET
	@Path("/getStealthReportForLayerVisualization")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Override
	public Response getStealthReportForLayerVisualization(@QueryParam(WORKORDER_ID) String workorderId,
			@QueryParam(DEVICE_ID) String deviceId, @QueryParam(DATE) String date,
			@Context HttpServletRequest request) {
		try {
			Response response = null;
			if (workorderId != null && deviceId != null && date != null) {
				String taskId = stealthTaskDetailService.fetchStealthTaskIdForDeviceReport(deviceId, workorderId);
				if (taskId != null) {
					HttpResponse httpResponse = ReportUtils.sendGetRequestWithoutTimeOut(getDropwizardUrlForWoReport(
							request, ConfigUtils.getString(ConfigEnum.MICRO_SERVICE_BASE_URL) + ConfigUtils
							.getString(ConfigEnum.DROPWIZARD_STEALTH_PDF_DOWNLOAD_FILE_URL, Boolean.TRUE)));
					Response.ResponseBuilder builder = Response.status(200);
					builder = builder.entity(httpResponse.getEntity().getContent())
							.header(ReportConstants.CONTENT_TYPE, httpResponse.getEntity().getContentType())
							.header(ReportConstants.CONTENT_DISPOSITION,
									httpResponse.getFirstHeader(ReportConstants.CONTENT_DISPOSITION).getValue());
					return builder.build();
				} else {
					response = Response.ok(QMDLConstant.EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
				}
			} else {
				response = Response.ok(QMDLConstant.EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
			}
			return response;
		} catch (Exception e) {
			logger.error("Exception in getDeviceReportForStealth : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(QMDLConstant.EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
		}
	}

	@GET
	@Path("/generateNvDashboardReport")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Override
	public Response generateNvDashboardReport(@QueryParam(GEOGRAPHY_NAME) String geographyName,
			@QueryParam(GEOGRAPHY_TYPE) String geographyType, @QueryParam(BAND) String band,
			@QueryParam(TECHNOLOGY) String technology, @QueryParam(OPERATOR) String operator,
			@QueryParam(START_DATE) String startTime, @QueryParam(END_DATE) String endTiime,
			@Context HttpServletRequest request) {
		logger.info(
				"Inside method generateNvDashboardReport, geographyName: {}, geographyType: {}, band : {}, technology : {}, operator : {}, startTime : {}, endTiime : {}  ",
				geographyName, geographyType, band, technology, operator, startTime, endTiime);
		Response.ResponseBuilder builder = Response.status(200);
		try {
			if (geographyName != null && geographyType != null && band != null && technology != null
					&& operator != null) {
				HttpResponse httpResponse = ReportUtils.sendGetRequestWithoutTimeOut(getDropwizardUrlForWoReport(
						request, ConfigUtils.getString(ConfigEnum.MICRO_SERVICE_BASE_URL) + ConfigUtils
						.getString(ConfigEnum.DROPWIZARD_NV_DASHBOARD_REPORT_DOWNLOAD_PATH, Boolean.TRUE)));
				builder = Response.status(200);
				builder = builder.entity(httpResponse.getEntity().getContent())
						.header(ReportConstants.CONTENT_TYPE, httpResponse.getEntity().getContentType())
						.header(ReportConstants.CONTENT_DISPOSITION,
								httpResponse.getFirstHeader(ReportConstants.CONTENT_DISPOSITION).getValue());

			} else {
				builder = Response.status(200);
				builder = builder.entity(QMDLConstant.INVALID_PARAMETER)
						.header("Content-Type", MediaType.APPLICATION_OCTET_STREAM).header("Content-Disposition",
								"attachment; filename=\"" + "Unable_To_Generate_Report.txt" + "\"");
			}
		} catch (Exception e) {
			logger.error("Exception inside the method generateNvDashboardReport {}", Utils.getStackTrace(e));
			builder = Response.status(200);
			builder = builder.entity(QMDLConstant.EXCEPTION_SOMETHING_WENT_WRONG)
					.header("Content-Type", MediaType.APPLICATION_OCTET_STREAM)
					.header("Content-Disposition", "attachment; filename=\"" + "Unable_To_Generate_Report.txt" + "\"");
		}
		return builder.build();
	}

	@POST
	@Path("syncMultipleWorkorderFiles")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Override
	public Response syncMultipleWorkorderFiles(@Context HttpServletRequest request) {
		Response response;
		logger.info("Going to syncMultipleWorkorderFiles ");
		try {
			response = Response.ok(nvLayer3Service.syncMultipleWorkorderFiles(request)).build();
		} catch (RestException e) {
			logger.error("RestException in syncMultipleWorkorderFiles{} ", ExceptionUtils.getStackTrace(e));
			throw new RestException(QMDLConstant.EXCEPTION_SOMETHING_WENT_WRONG_JSON);
		}
		return response;
	}

	@GET
	@Path("/processStealthWOCsvDump")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response processStealthWOCsvDump(@QueryParam(WORKORDER_ID) String workorderId,
			@Context HttpServletRequest request) {
		try {
			if (!StringUtils.isBlank(workorderId)) {
				Duration duration = Duration
						.minutes(Integer.parseInt(ConfigUtils.getString(ConfigEnum.TIMEOUT_VALUE.getValue())));

				return Response.ok(NVLayer3Utils.sendHttpGetRequest(getDropwizardUrlForWoReport(request,
						ConfigUtils.getString(ConfigEnum.MICRO_SERVICE_BASE_URL.getValue(), Boolean.TRUE).concat(
								ConfigUtils.getString(ConfigEnum.DROPWIZARD_STEALTH_WO_CSV_PROCESS_URL, Boolean.TRUE))),
						false, duration).getString()).build();
			} else {
				logger.error("Exception in processStealthWOCsvDump, workorderId invalid");
				return Response.ok(QMDLConstant.INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error("Exception in processStealthWOCsvDump : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(QMDLConstant.EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
		}
	}

	@GET
	@Path("/getStealthWOCsvDump")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Override
	public Response getStealthWOCsvDump(@QueryParam(FILE_PATH) String filePath, @Context HttpServletRequest request) {
		try {
			HttpResponse response = ReportUtils.sendGetRequestWithoutTimeOut(
					getDropwizardUrlForWoReport(request, ConfigUtils.getString(ConfigEnum.MICRO_SERVICE_BASE_URL)
							+ ConfigUtils.getString(ConfigEnum.DROPWIZARD_STEALTH_WO_CSV_DOWNLOAD_URL, Boolean.TRUE)));
			Response.ResponseBuilder builder = Response.status(200);
			builder = builder.entity(response.getEntity().getContent())
					.header(ReportConstants.CONTENT_TYPE, response.getEntity().getContentType())
					.header(ReportConstants.CONTENT_DISPOSITION,
							response.getFirstHeader(ReportConstants.CONTENT_DISPOSITION).getValue());
			return builder.build();
		} catch (Exception e) {
			logger.error("Exception in getStealthWOCsvDump : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(QMDLConstant.EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
		}
	}

	@POST
	@Path("/processStealthWOPdfForTaskId")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response processStealthWOPdfForTaskId(@QueryParam(WORKORDER_ID) String workorderId,
			@QueryParam(START_DATE) Long startDate, @QueryParam(END_DATE) Long endDate, List<Integer> taskIdList,
			@Context HttpServletRequest request) {
		try {
			if (!StringUtils.isBlank(workorderId) && taskIdList != null) {
				String json = new Gson().toJson(taskIdList);
				StringEntity httpEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
				Duration duration = Duration
						.minutes(Integer.parseInt(ConfigUtils.getString(ConfigEnum.TIMEOUT_VALUE.getValue())));

				return Response
						.ok(NVLayer3Utils.sendHttpPostRequest(
								getDropwizardUrlForWoReport(request,
										ConfigUtils
										.getString(ConfigEnum.MICRO_SERVICE_BASE_URL.getValue(), Boolean.TRUE)
										.concat(ConfigUtils.getString(
												DROPWIZARD_STEALTH_WO_TASK_ID_PDF_PROCESS_URL, Boolean.TRUE))),
								httpEntity, true, duration).getString())
						.build();
			} else {
				logger.error("Exception in processStealthWOPdfForTaskId, workorderId invalid");
				return Response.ok(QMDLConstant.INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error("Exception in processStealthWOPdfForTaskId : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(QMDLConstant.EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
		}
	}

	@GET
	@Path("/getStealthWOPdfForTaskId")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Override
	public Response getStealthWOPdfForTaskId(@QueryParam(FILE_PATH) String filePath,
			@Context HttpServletRequest request) {
		try {
			HttpResponse response = ReportUtils.sendGetRequestWithoutTimeOut(
					getDropwizardUrlForWoReport(request, ConfigUtils.getString(ConfigEnum.MICRO_SERVICE_BASE_URL)
							+ ConfigUtils.getString(DROPWIZARD_STEALTH_WO_TASK_ID_PDF_DOWNLOAD_URL, Boolean.TRUE)));
			Response.ResponseBuilder builder = Response.status(200);
			builder = builder.entity(response.getEntity().getContent())
					.header(ReportConstants.CONTENT_TYPE, response.getEntity().getContentType())
					.header(ReportConstants.CONTENT_DISPOSITION,
							response.getFirstHeader(ReportConstants.CONTENT_DISPOSITION).getValue());
			return builder.build();
		} catch (Exception e) {
			logger.error("Exception in getStealthWOPdfForTaskId : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(QMDLConstant.EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
		}
	}

	@GET
	@Path("/getLogFileForRecipe")
	@Produces(MediaType.APPLICATION_JSON)	
	@Override
	public Response getLogFileForRecipe(@QueryParam(FILE_PATH) String recipeId, @QueryParam(FILE_ID) String fileId,
			@Context HttpServletRequest request) {
		try {
			Duration duration = Duration
					.minutes(Integer.parseInt(ConfigUtils.getString(ConfigEnum.TIMEOUT_VALUE.getValue())));

			return Response.ok(NVLayer3Utils.sendHttpGetRequest(
					getDropwizardUrlForWoReport(request,
							ConfigUtils.getString(ConfigEnum.MICRO_SERVICE_BASE_URL.getValue(), Boolean.TRUE).concat(
									ConfigUtils.getString(DROPWIZARD_RECIPE_LOG_FILE_DOWNLOAD_URL, Boolean.TRUE))),
					false, duration).getString()).build();

		} catch (Exception e) {
			logger.error("Exception in getLogFileForRecipe : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(QMDLConstant.EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
		}
	}




	@POST
	@Path("/updateWOStatusOnHold")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Override
	public String updateWOStatusOnHold(@Multipart(value = WORKORDER_ID) String encrWOId,
			@Multipart(value = ForesightConstants.REMARK) String encrRemark,
			@Multipart(value = ForesightConstants.FILENAME) String encrFilename) {
		logger.info("Going to persit Recipe Files");
		Boolean isGCMAlgo = null;
		try {
			logger.info("Getting encrypted details from mobile WOId {} remark {} filename", encrWOId, encrRemark,
					encrFilename);
			if (encrWOId != null && encrRemark != null) {
				isGCMAlgo = AuthenticationCommonUtil.isGCMAlgorithm(encrWOId);
				String woRecipeMappingId = AuthenticationCommonUtil.checkForValueDecryption(encrWOId);
				String remark = AuthenticationCommonUtil.checkForValueDecryption(encrRemark);
				Boolean updateWorkorderStatus = nvWorkorderService.updateHoldStatusInGenericWorkorder(woRecipeMappingId,
						remark);
				if (updateWorkorderStatus) {
					return AuthenticationCommonUtil.checkForValueEncryption(SUCCESS_JSON, isGCMAlgo);
				}
			}
		} catch (Exception e) {
			logger.error("Exception in syncRecipeFiles : {}", ExceptionUtils.getStackTrace(e));
			return AuthenticationCommonUtil.checkForValueEncryption(FAILURE_JSON, isGCMAlgo);
		}
		return AuthenticationCommonUtil.checkForValueEncryption(FAILURE_JSON, isGCMAlgo);
	}

	@POST
	@Path("/syncStealthWOFile")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Override
	public String syncStealthWOFile(@Multipart(value = WO_TASK_RESULT_ID) String woTaskResultId,
			@QueryParam(FILE_TYPE) String fileType, @Multipart(value = ForesightConstants.FILE) InputStream inputFile,
			@Multipart(value = ForesightConstants.FILENAME) String fileName) {
		logger.info("Going to persit Recipe Files");
		try {
			Validate.checkNoneNull(woTaskResultId, inputFile, fileName, fileType);
			logger.info("fileName {} fileType {}", fileName, fileType);
			if ((fileName.endsWith(ZIP_FILE_EXTENTION))
					&& (StringUtils.equalsIgnoreCaseAny(fileType, ZIP_FILE_TYPE, CSV_FILE_TYPE, QMDL_FILE_TYPE))
					&& inputFile != null) {
				Integer taskResultId = Integer
						.parseInt(AuthenticationCommonUtil.checkForValueDecryption(woTaskResultId));

				nvLayer3Service.syncStealthWorkorderFile(taskResultId, inputFile, fileName, fileType);
			} else {
				logger.error(
						"File is not of QMDL or CSV Type or inputstream is null fileName or fileType or inputFile");
				throw new RestException(FILE_IS_NOT_OF_REQUIRED_TYPE);
			}
			logger.info("Done persit Recipe Files");
			return AuthenticationCommonUtil.checkForValueEncryption(SUCCESS_JSON, null);
		} catch (NullPointerException e) {
			logger.error("NullPointerException in syncStealthWOFile : {}", ExceptionUtils.getStackTrace(e));
			return AuthenticationCommonUtil.checkForValueEncryption(WO_RECIPE_MAPPING_ID_IS_NULL, null);
		} catch (RestException e) {
			logger.error("RestException in syncStealthWOFile : {}", ExceptionUtils.getStackTrace(e));
			return AuthenticationCommonUtil.checkForValueEncryption(e.getMessage(), null);
		} catch (IllegalArgumentException e) {
			logger.error("Exception in syncStealthWOFile === : {}", ExceptionUtils.getStackTrace(e));
			return AuthenticationCommonUtil.checkForValueEncryption(FILE_IS_EMPTY_JSON, null);
		} catch (Exception e) {
			logger.error("Exception in syncStealthWOFile : {}", ExceptionUtils.getStackTrace(e));
			return AuthenticationCommonUtil.checkForValueEncryption(FAILURE_JSON, null);
		}
	}

	@POST
	@Path("updateOccuranceOfEvent")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.TEXT_PLAIN)
	@Override
	public String updateOccuranceOfEvent(String encryptedJson) {
		String response=null;
		try {
			ProbeDetailWrapper wrapper = new Gson().fromJson(
					AuthenticationCommonUtil.checkForValueDecryption(encryptedJson), ProbeDetailWrapper.class);
			response=probeDetailService.updateEventOccurance(wrapper);
		} catch (Exception e) {
			logger.error("Exception in updateOccuranceOfEvent {}", Utils.getStackTrace(e));
			return AuthenticationCommonUtil.checkForValueEncryption(ForesightConstants.FAILURE_JSON, false);

		}
		return AuthenticationCommonUtil.checkForValueEncryption(response,null);
	}

	@GET
	@Path("/getRecipeWiseSummary")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response getRecipeWiseSummary(@QueryParam(RECIPE_ID) String encryptedRecipeId) {
		try {
			Response response;
			logger.info("Going to getRecipeWiseSummary ");
			try {
				String recipeId = AuthenticationCommonUtil.checkForValueDecryption(encryptedRecipeId);

				if (recipeId != null) {
					GWOMeta gwoMeta = nvWorkorderService.getGWOMetaByRecipeId(Integer.valueOf(recipeId));
					if (gwoMeta != null && gwoMeta.getEntityValue() != null) {
						response = Response.ok(AuthenticationCommonUtil.checkForValueEncryption(gwoMeta.getEntityValue(), null)).build();
					} else {
						response = Response.ok(AuthenticationCommonUtil.checkForValueEncryption(NO_DATA_FOUND, null)).build();
					} 
				} else {
					response = Response.ok(AuthenticationCommonUtil.checkForValueEncryption(QMDLConstant.INVALID_ARGUMENT, null)).build();
				}
			} catch (RestException e) {
				logger.error("RestException in getRecipeWiseSummary{} ", ExceptionUtils.getStackTrace(e));
				return Response.ok(AuthenticationCommonUtil.checkForValueEncryption(FAILURE_JSON, null)).build();
			}
			return response;

		} catch (Exception e) {
			logger.error("Exception in getRecipeWiseSummary : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(AuthenticationCommonUtil.checkForValueEncryption(QMDLConstant.EXCEPTION_SOMETHING_WENT_WRONG_JSON, null)).build();
		}
	}

	@GET
	@Path("/reopenRecipe/{woRecipeId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response reopenRecipeById(@PathParam("woRecipeId") String encryptedRecipeId,@QueryParam("workOrderId") String encryptedWorkOrderId) {
		try {
			Integer woRecipeId = Integer.valueOf(AuthenticationCommonUtil.checkForValueDecryption(encryptedRecipeId));
			Integer workOrderId = Integer.valueOf(AuthenticationCommonUtil.checkForValueDecryption(encryptedWorkOrderId));

			if (Utils.hasValidValue(String.valueOf(woRecipeId))&&Utils.hasValidValue(String.valueOf(workOrderId))) {
				return  recipeService.reopenRecipeById(woRecipeId,workOrderId,false);

			} else {
				return Response.ok(AuthenticationCommonUtil.checkForValueEncryption(InBuildingConstants.INVALID_PARAMETER_JSON,null)).build();
			}
		} catch (Exception e) {
			logger.error("Error while dreopenRecipeById woRecipeId {}" ,ExceptionUtils.getMessage(e));
			return Response.ok(AuthenticationCommonUtil.checkForValueEncryption(InBuildingConstants.EXCEPTION_SOMETHING_WENT_WRONG,null)).build();
		} 
	}



}

package com.inn.foresight.module.nv.inbuilding.result.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.http.HttpResponse;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.inn.commons.Symbol;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.http.HttpException;
import com.inn.commons.io.FileUtils;
import com.inn.commons.io.IOUtils;
import com.inn.commons.unit.Duration;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ConfigEnum;
import com.inn.foresight.core.generic.utils.ConfigUtil;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.constants.InBuildingConstants;
import com.inn.foresight.core.infra.dao.IFloorPlanMappingDao;
import com.inn.foresight.core.infra.model.FloorPlanMapping;
import com.inn.foresight.core.infra.model.Unit;
import com.inn.foresight.core.infra.service.IFloorPlanMappingService;
import com.inn.foresight.core.infra.service.IUnitDataService;
import com.inn.foresight.core.infra.wrapper.BuildingWrapper;
import com.inn.foresight.core.infra.wrapper.UnitInfoWrapper;
import com.inn.foresight.module.nv.inbuilding.dao.IFloorPlanHbaseDao;
import com.inn.foresight.module.nv.inbuilding.dao.impl.ITestResultHbaseDao;
import com.inn.foresight.module.nv.inbuilding.result.service.IInBuildingResultService;
import com.inn.foresight.module.nv.inbuilding.utils.NVBuildingUtils;
import com.inn.foresight.module.nv.inbuilding.wrapper.IBTestResultWrapper;

@Service("InBuildingResultServiceImpl")
public class InBuildingResultServiceImpl implements IInBuildingResultService {
	/** The logger. */
	private Logger logger = LogManager.getLogger(InBuildingResultServiceImpl.class);
	@Autowired
	private IUnitDataService iUnitDataService;

	@Autowired
	private IFloorPlanHbaseDao floorPlanHbaseDao;

	/** The i test result hbase dao. */
	@Autowired
	private ITestResultHbaseDao iTestResultHbaseDao;

	@Autowired
	private IFloorPlanMappingService iFloorPlanMappingService;

	@Autowired
	private IFloorPlanMappingDao iFloorPlanMappingDao;

	/**
	 * Download floor plan.
	 *
	 * @param unitId     the unit id
	 * @param technology the technology
	 * @return the string
	 * @throws RestException the rest exception
	 */
	@Override
	public Response downloadFloorPlan(Integer unitId) {
		logger.info("Going to downloadFloorPlan URL for unit id :{}", unitId);
		HttpResponse response = NVBuildingUtils.sendGetRequestWithoutTimeOut(NVBuildingUtils
				.getDropwizardUrl(InBuildingConstants.GET_DOWNLOAD_FLOOR_PLAN_URI.concat(String.valueOf(unitId))));
		Response responseToReturn = null;
		try {
			InputStream stream = response.getEntity().getContent();
			String imageName = NVBuildingUtils.getFileNameFromResponse(response);
			Response.ResponseBuilder builder = Response.status(200);
			responseToReturn = builder.entity(stream).header("Content-Type", MediaType.APPLICATION_OCTET_STREAM)
					.header("Content-Disposition", "attachment;filename=" + imageName).build();

		} catch (Exception e) {
			logger.error("Error while floor plan download:{}", ExceptionUtils.getStackTrace(e));
		}
		return responseToReturn;
	}

	/**
	 * Upload floor plan.
	 *
	 * @param unitId      the unit id
	 * @param inputStream the input stream
	 * @param fileName    the file name
	 * @return the string
	 * @throws RestException the rest exception
	 */
	@Transactional
	@Override
	public String uploadFloorPlanData(Integer unitId, InputStream inputStream, String fileName) {
		String response = null;
		try {
			ContentBody content = new InputStreamBody(inputStream, ForesightConstants.FILE);
			MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();
			reqEntity.addPart(ForesightConstants.FILE, content);
			reqEntity.addTextBody(ForesightConstants.FILENAME, fileName);

			Duration duration = Duration
					.minutes(Integer.parseInt(ConfigUtils.getString(ConfigEnum.TIMEOUT_VALUE.getValue())));
			String url = NVBuildingUtils.getDropwizardUrl(InBuildingConstants.GET_UPLOAD_FLOOR_PLAN_DATA_URI)
					.concat(String.valueOf(unitId));
			logger.info("uploadFloorPlan :{} ", url);
			response = NVBuildingUtils.sendHttpPostRequest(url, reqEntity.build(), true, duration).getString();
			updateFloorPlanAvailabilityStatus(unitId, response);
		} catch (Exception e) {
			logger.error("Error while uploading floor plan data :{} ", ExceptionUtils.getMessage(e));
			response = InBuildingConstants.EXCEPTION_SOMETHING_WENT_WRONG;
		}
		return response;
	}

	private void updateFloorPlanAvailabilityStatus(Integer unitId, String response) {
		if (response != null && InBuildingConstants.SUCCESS_JSON.equalsIgnoreCase(response)) {
			Unit unitData = iUnitDataService.findByPk(unitId);
			unitData.setFloorPlanAvailable(true);
			iUnitDataService.updateUnit(unitData);
			iFloorPlanMappingService.updateFloorPlanMapping(unitId, InBuildingConstants.TEMPLATE_ADHOC);

		}
	}

	/**
	 * Download floor plan.
	 *
	 * @param unitId the unit id
	 * @return the string
	 * @throws RestException the rest exception
	 */
	@Override
	public byte[] downloadFloorPlanData(Integer unitId) {
		byte[] response = null;
		try {
			Duration duration = Duration
					.minutes(Integer.parseInt(ConfigUtils.getString(ConfigEnum.TIMEOUT_VALUE.getValue())));
			response = NVBuildingUtils.sendHttpGetRequest(
					NVBuildingUtils.getDropwizardUrl(
							InBuildingConstants.GET_DOWNLOAD_FLOOR_PLAN_DATA_URI.concat(String.valueOf(unitId))),
					true, duration).getByteArray();
		} catch (HttpException e) {
			logger.error("Error while downloadin floor plan data :{} ", ExceptionUtils.getMessage(e));
			throw new RestException("Error while preparing floor plan download: " + ExceptionUtils.getMessage(e));
		}
		return response;
	}

	/**
	 * Process IB floor plan and test result data.
	 *
	 * @param dataStream the data stream
	 * @return the string
	 */
	@Override
	public String processIBFloorPlanAndTestResultData(InputStream dataStream) {
		String unitInfo = "";
		String floorPlanJson = "";
		String testResultJson = "";
		UnitInfoWrapper unitInfoWrapper = new Gson().fromJson(unitInfo, UnitInfoWrapper.class);
		InputStream floorPlanImageStream = null;
		InputStream backgroundImageStream = null;
		String result = processFloorPlan(unitInfoWrapper, floorPlanJson, floorPlanImageStream, backgroundImageStream);
		if (InBuildingConstants.SUCCESS_JSON.equalsIgnoreCase(result)) {
			return processTestResult(unitInfoWrapper, testResultJson);
		}
		return result;
	}

	/**
	 * Process test result.
	 *
	 * @param unitInfoWrapper the unit info wrapper
	 * @param testResultJson  the test result json
	 * @return the string
	 */
	private String processTestResult(UnitInfoWrapper unitInfoWrapper, String testResultJson) {
		try {
			IBTestResultWrapper ibTestResultWrapper = new Gson().fromJson(testResultJson, IBTestResultWrapper.class);
			return iTestResultHbaseDao
					.insertIBTestResultToHbase(NVBuildingUtils.getTestResultPut(unitInfoWrapper, ibTestResultWrapper));
		} catch (IOException e) {
			logger.error("Exception in processTestResult : {}", ExceptionUtils.getStackTrace(e));
			return ExceptionUtils.getMessage(e);
		}
	}

	/**
	 * Process floor plan.
	 *
	 * @param unitInfoWrapper       the unit info wrapper
	 * @param floorPlanJson         the floor plan json
	 * @param floorPlanImageStream  the floor plan image stream
	 * @param backgroundImageStream the background image stream
	 * @return the string
	 */
	private String processFloorPlan(UnitInfoWrapper unitInfoWrapper, String floorPlanJson,
			InputStream floorPlanImageStream, InputStream backgroundImageStream) {
		try {
			logger.info("Goining to processFloorPlan  for unit id: {}", unitInfoWrapper.getUnitId());
			return floorPlanHbaseDao.insertIBFloorPlanDataToHbase(NVBuildingUtils.getFloorPlanPut(unitInfoWrapper,
					floorPlanImageStream, backgroundImageStream, floorPlanJson, null));
		} catch (Exception e) {
			logger.error("Exception in processFloorPlan : {}", ExceptionUtils.getStackTrace(e));
			return ExceptionUtils.getMessage(e);
		}
	}

	/**
	 * Upload floor plan.
	 * 
	 * @param string
	 *
	 * @param unitId      the unit id
	 * @param technology  the technology
	 * @param inputStream the input stream
	 * @param fileName    the file name
	 * @return the string
	 */
	@Override
	public String uploadFloorPlan(String templateType, Integer unitId, String technology, InputStream inputStream,
			String fileName) {
		try {
			logger.info("Going to uploadFloorPlan  for unit id: {}", unitId);
			UnitInfoWrapper unitInfoWrapper = new UnitInfoWrapper();
			unitInfoWrapper.setTechnology(technology);
			unitInfoWrapper.setUnitId(unitId);
			unitInfoWrapper.setTemplateType(InBuildingConstants.TEMPLATE_WORKORDER);
			return floorPlanHbaseDao.insertIBFloorPlanDataToHbase(
					NVBuildingUtils.getFloorPlanPut(unitInfoWrapper, null, inputStream, null, fileName));
		} catch (Exception e) {
			logger.error("Exception in uploadFloorPlan : {}", ExceptionUtils.getStackTrace(e));
			return ExceptionUtils.getMessage(e);
		}
	}

	/**
	 * Download floor plan.
	 *
	 * @param unitId     the unit id
	 * @param technology the technology
	 * @return the byte[]
	 */
	@Override
	public Response downloadFloorPlanImage(Integer unitId) {
		Response response = null;
		List<FloorPlanMapping> floorPlanMappings = null;
		String imageName = "FloorPlanImage_" + unitId + ".jpeg";
		logger.info("Going to downloadFloorPlan  for unit id: {}", unitId);
		try {
			floorPlanMappings = iFloorPlanMappingDao.getFloorplanMappingByUnitId(unitId);
		} catch (NoResultException e) {
			logger.error(
					"NoResultException inside downloadFloorPlanImage when going to getFloorplanMappingByUnitId {} ",
					Utils.getStackTrace(e));
		}
		try {
			Result result = floorPlanHbaseDao.getIBFloorPlan(NVBuildingUtils.getFloorPlanRowKeyWithTemplateType(unitId,
					getTemplateFromMapping(floorPlanMappings)));
			if (result == null) {
				logger.error("result is null ,now going to get floorplan result without template {} ,", result);
				result = floorPlanHbaseDao
						.getIBFloorPlan(NVBuildingUtils.getFloorPlanRowKeyWithTemplateType(unitId, null));
			}
			if (result != null) {
				logger.info("Result is NOT NULL ");
				byte[] byteArray = result.getValue(InBuildingConstants.COLUMN_FAMILY.getBytes(),
						Bytes.toBytes(InBuildingConstants.COLUMN_BG_IMAGE));
				if (byteArray == null) {
					logger.info("The INSIDE THE BYTE ARRAY CONDITION.");

					byteArray = result.getValue(InBuildingConstants.COLUMN_FAMILY.getBytes(),
							Bytes.toBytes(InBuildingConstants.COLUMN_FLOOR_PLAN_IMAGE));
				}
				byte[] imageNameByte = result.getValue(InBuildingConstants.COLUMN_FAMILY.getBytes(),
						Bytes.toBytes(InBuildingConstants.COLUMN_BG_IMAGE_NAME));
				if (imageNameByte != null) {
					imageName = new String(imageNameByte);
				}
				File fileToReturn = new File(imageName);
				logger.info("fileToReturn == {}",fileToReturn.getAbsolutePath());
				FileUtils.writeByteArrayToFile(fileToReturn, byteArray);

				Response.ResponseBuilder builder = Response.status(200);
				response = builder.entity(fileToReturn).header("Content-Type", MediaType.APPLICATION_OCTET_STREAM)
						.header(InBuildingConstants.KEY_HEADER_FILENAME, imageName).build();

			} else {
				Response.ResponseBuilder builder = Response.status(200);
				response = builder
						.entity(InBuildingConstants.FLOOR_PLAN_DOES_NOT_EXIST_FOR
								.concat("unit id = ".concat(String.valueOf(unitId))))

						.build();
			}
		} catch (Exception e) {
			logger.error("Getting Error in downloadFloorPlan{}", Utils.getStackTrace(e));
		}
		return response;
	}

	private String getTemplateFromMapping(List<FloorPlanMapping> floorPlanMappings) {
		if (!Utils.isValidList(floorPlanMappings)) {
			logger.info("floorPlanMappings is empty");
			return null;
		} else {
			Optional<FloorPlanMapping> approvedOptional = floorPlanMappings.stream()
					.filter(f -> f.getIsApproved().equals(Boolean.TRUE)).findFirst();
			if (approvedOptional.isPresent()) {
				logger.info("Approved floorplan available");
				return String.valueOf(approvedOptional.get().getTemplateType().charAt(InBuildingConstants.FIRST_INDEX));
			} else {
				Optional<FloorPlanMapping> optional = floorPlanMappings.stream()
						.max(Comparator.comparing(FloorPlanMapping::getModificationTime));
				if (optional.isPresent()) {
					logger.info("Approved floorplan not available getting latest floorplan");
					return String.valueOf(optional.get().getTemplateType().charAt(InBuildingConstants.FIRST_INDEX));

				}
				return null;
			}

		}
	}

	/**
	 * Upload floor plan data.
	 *
	 * @param unitId      the unit id
	 * @param inputStream the input stream
	 * @param fileName    the file name
	 * @return the response
	 */
	@Override
	public String putFloorPlanData(Integer unitId, InputStream inputStream, String fileName) {
		logger.info("Going to process floor plan file to hbase fileName {}", fileName);
		String filePath = ConfigUtils.getString(ConfigUtil.INBUILDING_TEMP_PATH);
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		NVBuildingUtils.copyFileToLocalPath(filePath + fileName, inputStream);
		return processZipForFloorPlanData(unitId, filePath + fileName);
	}

	/**
	 * Process Zip Content For Floor Plan Data.
	 *
	 * @param unitId   the unit id
	 * @param filePath local file path
	 * @return the response
	 */
	private String processZipForFloorPlanData(Integer unitId, String filePath) {
		logger.info("Processing zip data filePath: {}", filePath);
		String floorPlanJson = null;
		byte[] floorPlanImage = null;
		byte[] floorPlanBackground = null;
		String bgFileName = null;
		String fpFileName = null;

		UnitInfoWrapper unitInfoWrapper = new UnitInfoWrapper();
		unitInfoWrapper.setTechnology(Symbol.EMPTY_STRING);
		unitInfoWrapper.setTemplateType(InBuildingConstants.TEMPLATE_ADHOC);
		unitInfoWrapper.setUnitId(unitId);

		try (ZipFile zipFile = new ZipFile(filePath)) {
			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				if (entry.getName().contains(InBuildingConstants.FLOOR_PLAN_JSON_NAME_IDENTIFIER)) {
					floorPlanJson = NVBuildingUtils.getJsonFromInputStream(zipFile.getInputStream(entry));
					logger.info("Getting Floor plan json file name:  {}", entry.getName());
				} else if (entry.getName().contains(InBuildingConstants.FLOOR_PLAN_IMAGE_NAME_IDENTIFIER)) {
					floorPlanImage = IOUtils.toByteArray(zipFile.getInputStream(entry));
					fpFileName = entry.getName();
					logger.info("Getting  Floorplan image name :{}  , size: {}", entry.getName(),
							floorPlanImage.length);
				} else if (entry.getName().contains(InBuildingConstants.FLOOR_PLAN_BACKGROUND_NAME_IDENTIFIER)) {
					bgFileName = entry.getName();
					floorPlanBackground = IOUtils.toByteArray(zipFile.getInputStream(entry));
					logger.info("Getting file  background image name : {} , size:  {} ", entry.getName(),
							floorPlanBackground.length);

				}
			}
		} catch (IOException e) {
			logger.error("Error! while processing zip file: {}", Utils.getStackTrace(e));
		}
		if (bgFileName == null) {
			bgFileName = fpFileName;
		}
		logger.info("Going to insert floor plan data in hbase filePath {}", filePath);
		return floorPlanHbaseDao.insertIBFloorPlanDataToHbase(NVBuildingUtils.getFloorPlanPut(unitInfoWrapper,
				floorPlanImage, floorPlanBackground, floorPlanJson, bgFileName));
	}

	/**
	 * Upload multiple floor plan.
	 *
	 * @param request the request
	 * @return the building wrapper
	 */
	@Override
	public BuildingWrapper uploadMultipleFloorPlan(HttpServletRequest request) {
		BuildingWrapper buildingWrapper = null;
		try {
			HashMap<Integer, Boolean> unitIds = new HashMap<>();
			List<FileItem> fileItem = NVBuildingUtils.extractFileItemsFromHttpRequest(request);
			buildingWrapper = NVBuildingUtils.extractBuildingWrapper(fileItem);
			logger.info("Going to uploadMultipleFloorPlan Building wrapper : {}", buildingWrapper);
			for (FileItem response : fileItem) {
				if (!InBuildingConstants.KEY_DATA.equalsIgnoreCase(response.getFieldName())) {
					Integer unitId = NVBuildingUtils.getUnitId(buildingWrapper, response.getFieldName());
					if (unitId != null) {
						String result = uploadFloorPlan(InBuildingConstants.TEMPLATE_WORKORDER, unitId, "",
								response.getInputStream(), response.getFieldName());
						NVBuildingUtils.prepareFloorPlanUploadStatus(unitIds, unitId, result);

					} else {
						logger.info("Unit Id not Present for {}", response.getFieldName());
					}
				}
			}
			return getUpdatedBuildingWrapper(buildingWrapper, unitIds);
		} catch (FileUploadException e) {
			logger.error("FileUploadException on uploading floorplan {}", ExceptionUtils.getStackTrace(e));
		} catch (Exception e) {
			logger.error("Exception on uploading floorplan {}", ExceptionUtils.getStackTrace(e));
		}
		return buildingWrapper;
	}

	/**
	 * Prepare building wrapper.
	 *
	 * @param buildingWrapper the building wrapper
	 * @param unitIds         the unit ids
	 * @return the building wrapper
	 */
	private BuildingWrapper getUpdatedBuildingWrapper(BuildingWrapper buildingWrapper,
			HashMap<Integer, Boolean> unitIds) {
		logger.info("unit id list status:  {}", unitIds);
		buildingWrapper.getWingList().parallelStream().forEach(wing -> {
			wing.getFloorList().parallelStream().forEach(floor -> {
				floor.getUnitList().parallelStream().forEach(unit -> {
					Boolean isUploaded = unitIds.get(unit.getUnitId());
					if (isUploaded != null) {
						unit.setFloorPlanUploaded(isUploaded);
						unit.setFloorPlanAvailable(isUploaded);
					}
				});
			});
		});
		return buildingWrapper;
	}

	/**
	 * Checks if is floor plan available.
	 *
	 * @param unitId the unit id
	 * @return true, if is floor plan available
	 */
	public boolean isFloorPlanAvailable(Integer unitId) {
		List<FloorPlanMapping> floorPlanMappings = new ArrayList<>();
		try {
			floorPlanMappings = iFloorPlanMappingDao.getFloorplanMappingByUnitId(unitId);
		} catch (NoResultException e) {
			logger.error("NoResultException inside isFloorPlanAvailable when going to getFloorplanMappingByUnitId {}",
					Utils.getStackTrace(e));
		}
		return floorPlanHbaseDao.isFloorPlanAvailable(
				NVBuildingUtils.getFloorPlanRowKeyWithTemplateType(unitId, getTemplateFromMapping(floorPlanMappings)));
	}

	/**
	 * Download floor plan data.
	 *
	 * @param unitId the unit id
	 * @return the byte[]
	 */
	@Override
	public byte[] getFloorPlanData(Integer unitId) {
		List<FloorPlanMapping> floorPlanMappings = new ArrayList<>();
		try {
			floorPlanMappings = iFloorPlanMappingDao.getFloorplanMappingByUnitId(unitId);
		} catch (NoResultException e) {
			logger.error("NoResultException inside getFloorPlanData when going to getFloorplanMappingByUnitId {}",
					Utils.getStackTrace(e));
		}
		Result result = floorPlanHbaseDao.getIBFloorPlan(
				NVBuildingUtils.getFloorPlanRowKeyWithTemplateType(unitId, getTemplateFromMapping(floorPlanMappings)));

		if (result == null) {
			logger.error("result is null ,now going to get floorplan result without template {} ,", result);
			result = floorPlanHbaseDao.getIBFloorPlan(NVBuildingUtils.getFloorPlanRowKeyWithTemplateType(unitId, null));
		}
		return processFloorPlanDataToZipFile(result, unitId);
	}

	/**
	 * Process floor plan data to zip file.
	 *
	 * @param unitId the unit id
	 * @return the byte[]
	 */
	private byte[] processFloorPlanDataToZipFile(Result result, Integer unitId) {
		byte[] data = null;
		File file = new File(ConfigUtils.getString(ConfigUtil.INBUILDING_TEMP_PATH)
				+ InBuildingConstants.FLOOR_PLAN_ZIP_NAME + unitId + InBuildingConstants.FILE_EXTENSION_ZIP);
		try {
			String bgZipFileName;

			if (result.getValue(InBuildingConstants.COLUMN_FAMILY.getBytes(),
					Bytes.toBytes(InBuildingConstants.COLUMN_BG_IMAGE_NAME)) != null) {
				bgZipFileName = new String(
						result.getValue(InBuildingConstants.COLUMN_FAMILY.getBytes(),
								Bytes.toBytes(InBuildingConstants.COLUMN_BG_IMAGE_NAME)),
						StandardCharsets.UTF_8);
				
				
			} else {
				bgZipFileName = InBuildingConstants.DEFAULT_BG_IMAGE_FILE_NAME;
			}

			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(file));

			if (result.getValue(InBuildingConstants.COLUMN_FAMILY.getBytes(),
					Bytes.toBytes(InBuildingConstants.COLUMN_BG_IMAGE)) != null) {
				logger.info("writeg  the bgimage plan image ");

				out = NVBuildingUtils.addEntryToZipFile(result.getValue(InBuildingConstants.COLUMN_FAMILY.getBytes(),
						Bytes.toBytes(InBuildingConstants.COLUMN_BG_IMAGE)), bgZipFileName, out);
			}
		
			if (result.getValue(InBuildingConstants.COLUMN_FAMILY.getBytes(),
					Bytes.toBytes(InBuildingConstants.COLUMN_BG_IMAGE)) == null&&result.getValue(InBuildingConstants.COLUMN_FAMILY.getBytes(),
							Bytes.toBytes(InBuildingConstants.COLUMN_FLOOR_PLAN_IMAGE)) != null) {
				logger.info("writeg  the floor plan image ");
				out = NVBuildingUtils.addEntryToZipFile(result.getValue(InBuildingConstants.COLUMN_FAMILY.getBytes(),
						Bytes.toBytes(InBuildingConstants.COLUMN_FLOOR_PLAN_IMAGE)), bgZipFileName, out);
			}
			
			if (result.getValue(InBuildingConstants.COLUMN_FAMILY.getBytes(),
					Bytes.toBytes(InBuildingConstants.COLUMN_FLOOR_PLAN_JSON)) != null) {
				out = NVBuildingUtils.addEntryToZipFile(
						result.getValue(InBuildingConstants.COLUMN_FAMILY.getBytes(),
								Bytes.toBytes(InBuildingConstants.COLUMN_FLOOR_PLAN_JSON)),
						InBuildingConstants.FLOOR_PLAN_JSON_FILE_NAME, out);
			}
			out.close();
			FileInputStream inputStream = new FileInputStream(file);
			data = IOUtils.toByteArray(inputStream);
		} catch (IOException e) {
			logger.error("Error while preparing zip file: {}", Utils.getStackTrace(e));
		}
		return data;
	}

}
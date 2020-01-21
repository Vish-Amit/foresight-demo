
package com.inn.foresight.core.infra.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.inn.commons.Symbol;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.http.HttpGetRequest;
import com.inn.commons.http.HttpPostRequest;
import com.inn.commons.io.IOUtils;
import com.inn.commons.unit.Duration;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ConfigEnum;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.constants.InBuildingConstants;
import com.inn.foresight.core.infra.model.Building;
import com.inn.foresight.core.infra.model.Floor;
import com.inn.foresight.core.infra.model.Unit;
import com.inn.foresight.core.infra.model.Wing;
import com.inn.foresight.core.infra.wrapper.BuildingWrapper;
import com.inn.foresight.core.infra.wrapper.FloorWrapper;
import com.inn.foresight.core.infra.wrapper.UnitInfoWrapper;
import com.inn.foresight.core.infra.wrapper.UnitWrapper;
import com.inn.foresight.core.infra.wrapper.WingWrapper;

/** The Class InBuildingUtils. */
public class InBuildingUtils {

	/** The logger. */
	private static Logger logger = LogManager.getLogger(InBuildingUtils.class);

	protected InBuildingUtils() {
	}

	/**
	 * Prepare building data.
	 *
	 * @param buildingWrapper
	 *            the building
	 * @param buildingData
	 *            the building data
	 * @return the building
	 */
	public static Building getBuildingData(BuildingWrapper buildingWrapper, Building buildingData) {
		buildingData.setAddress(buildingWrapper.getAddress());
		buildingData.setBuildingName(buildingWrapper.getBuildingName());
		Calendar calendar = Calendar.getInstance();
		if (buildingData.getCreationTime() == null) {
			buildingData.setCreationTime(calendar.getTime());
		}
		buildingData.setModificationTime(calendar.getTime());
		buildingData.setBuildingType(buildingWrapper.getBuildingType());
		buildingData.setLatitude(buildingWrapper.getLatitude());
		buildingData.setLongitude(buildingWrapper.getLongitude());
		return buildingData;
	}

	/**
	 * Prepare wing data.
	 *
	 * @param wingData
	 *            the wing data
	 * @param wingWrapper
	 *            the wing
	 * @param buildingData
	 *            the building data
	 * @return the wing
	 */
	public static Wing getWingData(Wing wingData, WingWrapper wingWrapper, Building buildingData) {
		if (buildingData != null) {
			wingData.setBuilding(buildingData);
		}
		wingData.setWingName(wingWrapper.getWingName());
		Calendar calendar = Calendar.getInstance();
		if (wingData.getCreationTime() == null) {
			wingData.setCreationTime(calendar.getTime());
		}
		wingData.setModificationTime(calendar.getTime());
		return wingData;
	}

	/**
	 * Prepare floor data.
	 *
	 * @param floorData
	 *            the floor data
	 * @param floorWrapper
	 *            the floor wrapper
	 * @param wingData
	 *            the wing data
	 * @return the floor
	 */
	public static Floor getFloorData(Floor floorData, FloorWrapper floorWrapper, Wing wingData) {
		if (wingData != null)
			floorData.setWing(wingData);
		floorData.setFloorName(floorWrapper.getFloorNumber());
		Calendar calendar = Calendar.getInstance();
		if (floorData.getCreationTime() == null) {
			floorData.setCreationTime(calendar.getTime());
		}
		floorData.setModificationTime(calendar.getTime());
		return floorData;
	}

	/**
	 * Prepare unit data.
	 *
	 * @param unitData
	 *            the unit data
	 * @param unitWrapper
	 *            the unit
	 * @param floorData
	 *            the floor data
	 * @return the unit
	 */
	public static Unit getUnitData(Unit unitData, UnitWrapper unitWrapper, Floor floorData) {
		if (floorData != null) {
			unitData.setFloor(floorData);
		}
		unitData.setUnitName(unitWrapper.getUnitName());
		unitData.setUnitType(unitWrapper.getUnitType());
		Calendar calendar = Calendar.getInstance();
		if (unitData.getCreationTime() == null) {
			unitData.setCreationTime(calendar.getTime());
		}
		unitData.setModificationTime(calendar.getTime());
		return unitData;
	}

	/**
	 * Gets the column list for query.
	 *
	 * @return the column list for query
	 */
	public static List<String> getColumnListForQuery() {
		List<String> columnList = new ArrayList<>();
		columnList.add("r:geographyName");
		return columnList;
	}

	/**
	 * Prepare wing wrapper.
	 *
	 * @param wing
	 *            the wing
	 * @param wingWrapper
	 *            the wing wrapper
	 */
	public static WingWrapper getWingWrapper(Wing wing) {
		return new WingWrapper(wing.getId(), wing.getWingName(), wing.getCreationTime(), wing.getModificationTime());
	}

	/**
	 * Prepare floor wrapper.
	 *
	 * @param floor
	 *            the floor
	 * @param floorWrapper
	 *            the floor wrapper
	 */
	public static FloorWrapper getFloorWrapper(Floor floor) {
		FloorWrapper floorWrapper = new FloorWrapper();
		floorWrapper.setFloorId(floor.getId());
		floorWrapper.setFloorNumber(floor.getFloorName());
		return floorWrapper;
	}

	
	/**
	 * Prepare unit wrapper.
	 *
	 * @param unit
	 *            the unit
	 * @param unitWrapper
	 *            the unit wrapper
	 */
	public static UnitWrapper getUnitWrapper(Unit unit) {
		UnitWrapper unitWrapper = new UnitWrapper();
		unitWrapper.setUnitId(unit.getId());
		unitWrapper.setUnitName(unit.getUnitName());
		unitWrapper.setUnitType(unit.getUnitType());
		unitWrapper.setFloorPlanAvailable(unit.isFloorPlanAvailable());
		return unitWrapper;
	}

	/**
	 * Send http post request.
	 *
	 * @param url
	 *            the url
	 * @param reqEntity
	 *            the http entity
	 * @param isToEnableTimeOut
	 *            the is to enable time out
	 * @param duration
	 *            the duration
	 * @return the http post request
	 */
	public static HttpPostRequest sendHttpPostRequest(String url, HttpEntity reqEntity, boolean isToEnableTimeOut,
			Duration duration) {
		HttpPostRequest httpPostRequest = new HttpPostRequest(url, reqEntity);
		if (duration != null) {
			httpPostRequest.setConnectionTimeout(duration);
			httpPostRequest.setEnableTimeout(isToEnableTimeOut);
		}
		return httpPostRequest;
	}

	/**
	 * Gets the dropwizard url.
	 *
	 * @param path
	 *            the path
	 * @return the dropwizard url
	 */
	public static String getDropwizardUrl(String path) {

		return ConfigUtils.getString(ConfigEnum.MICRO_SERVICE_BASE_URL.getValue())
				+ ConfigUtils	.getString(ConfigEnum.DROPWIZARD_URL_FOR_INBUILDING_RESULT, Boolean.TRUE)
								.concat(path);
	}

	/**
	 * Send http get request.
	 *
	 * @param url
	 *            the url
	 * @param isToEnableTimeOut
	 *            the is to enable time out
	 * @param duration
	 *            the duration
	 * @return HttpGetRequest
	 */

	public static HttpGetRequest sendHttpGetRequest(String url, boolean isToEnableTimeOut, Duration duration) {
		HttpGetRequest httpGetRequest = new HttpGetRequest(url);
		if (duration != null) {
			httpGetRequest.setConnectionTimeout(duration);
			httpGetRequest.setEnableTimeout(isToEnableTimeOut);
		}
		return httpGetRequest;
	}

	/**
	 * Gets the multi part entity.
	 *
	 * @param buildingWrapper
	 *            the building wrapper
	 * @param fileItemList
	 *            the file item
	 * @return the multi part entity
	 */
	public static MultipartEntityBuilder getMultiPartEntity(BuildingWrapper buildingWrapper, List<FileItem> fileItemList) {
		MultipartEntityBuilder multipartEntity =MultipartEntityBuilder.create();
		try {
			multipartEntity.addTextBody(InBuildingConstants.KEY_DATA, new Gson().toJson(buildingWrapper));

			fileItemList.stream()
						.filter(e -> !InBuildingConstants.KEY_DATA.equalsIgnoreCase(e.getFieldName()))
						.forEach(e -> {
							try {
								multipartEntity.addPart(e.getFieldName(),
										new InputStreamBody(e.getInputStream(), ForesightConstants.FILE));
							} catch (IOException e1) {
								logger.error("IOException in getMultiPartEntity... {}",
										ExceptionUtils.getStackTrace(e1));
							}
						});
		} catch (Exception e1) {
			logger.error("UnsupportedEncodingException in getMultiPartEntity... {}", ExceptionUtils.getMessage(e1));
		}

		return multipartEntity;
	}

	/**
	 * Extract file items from http request.
	 * 
	 * @param request
	 *            the HttpServletRequest
	 * @return the list of FileItem
	 * @throws FileUploadException
	 *             the file upload exception
	 */
	@SuppressWarnings("unchecked")
	public static List<FileItem> extractFileItemsFromHttpRequest(HttpServletRequest request)
			throws FileUploadException {
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload fileUpload = new ServletFileUpload(factory);
		return fileUpload.parseRequest(request);
	}

	public static HttpResponse sendGetRequestWithoutTimeOut(String uri) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(uri);
		try {
			logger.info("Going to hit URL : {}", uri);
			CloseableHttpResponse response = httpClient.execute(httpGet);
			int statusCode = response	.getStatusLine()
										.getStatusCode();

			if (statusCode != HttpStatus.SC_OK) {
				throw new RestException("Failed with HTTP error code : " + statusCode);
			}
			logger.info("Return Response Code : {}", statusCode);
			return response;
		} catch (Exception e) {
			throw new RestException(ExceptionUtils.getStackTrace(e));
		} finally {
			// httpGet.releaseConnection();
			// IOUtils.closeQuietly(httpClient);
		}
	}

	public static String getFileNameFromResponse(HttpResponse response) {
		Header[] headers = response.getHeaders(InBuildingConstants.KEY_HEADER_FILENAME);
		return headers[0].getValue();
	}
	/**
	 * Gets the floor plan get for row key.
	 *
	 * @param rowKey
	 *            the row key
	 * @return the floor plan get for row key
	 */
	public static Get getFloorPlanGetForRowKey(String rowKey) {
		String columnFamily = InBuildingConstants.COLUMN_FAMILY;
		List<String> columns = new ArrayList<>();
		columns.add(InBuildingConstants.COLUMN_FLOOR_PLAN_IMAGE);
		columns.add(InBuildingConstants.COLUMN_BG_IMAGE);
		columns.add(InBuildingConstants.COLUMN_FLOOR_PLAN_JSON);
		columns.add(InBuildingConstants.COLUMN_BG_IMAGE_NAME);
		return getHbaseGet(rowKey, columnFamily, columns);
	}
	
	/**
	 * Gets the bytes from input stream.
	 *
	 * @param inputStream
	 *            the input stream
	 * @return the bytes from input stream
	 */
	public static byte[] getBytesFromInputStream(InputStream inputStream) {
		byte[] byteArray = null;
		try {
			byteArray = IOUtils.toByteArray(inputStream);
		} catch (IOException e) {
			logger.error("IOException {}: ", ExceptionUtils.getStackTrace(e));
		}
		return byteArray;
	}

	/**
	 * Adds the string to put.
	 *
	 * @param put
	 *            object in which add qualifier
	 * @param value
	 *            value of qualifier
	 * @param qualifierName
	 *            qualier name
	 * @param family
	 *            column family of table
	 */
	public static void addStringToPut(Put put, Object value, String qualifierName, String family) {
		if (value != null) {
			String valueStr = String.valueOf(value);
			put.addColumn(family.getBytes(), qualifierName.getBytes(), valueStr.getBytes());
		}
	}

	/**
	 * Gets the floor plan row key.
	 *
	 * @param unitId
	 *            the unit id
	 * @return the floor plan row key
	 */
	public static String getFloorPlanRowKey(Integer unitId) {
		return StringUtils.reverse(StringUtils.leftPad(String.valueOf(unitId), InBuildingConstants.ROW_KEY_LENGTH,
				InBuildingConstants.PADDING_CHAR));

	}
	

	/**
	 * Gets the floor plan put.
	 *
	 * @param unitInfoWrapper
	 *            the unit info wrapper
	 * @param floorPlanStream
	 *            the floor plan stream
	 * @param backgroundImageStream
	 *            the background image stream
	 * @param floorplanJson
	 *            the floorplan json
	 * @param bgImageName
	 *            the bg image name
	 * @return the floor plan put
	 */
	public static Put getFloorPlanPut(UnitInfoWrapper unitInfoWrapper, InputStream floorPlanStream,
			InputStream backgroundImageStream, String floorplanJson, String bgImageName) {
		Put floorPlanPut = new Put(Bytes.toBytes(InBuildingUtils.getFloorPlanRowKey(unitInfoWrapper.getUnitId())));
		if (floorPlanStream != null) {
			floorPlanPut.addColumn(InBuildingConstants.COLUMN_FAMILY.getBytes(),
					InBuildingConstants.COLUMN_FLOOR_PLAN_IMAGE.getBytes(),
					InBuildingUtils.getBytesFromInputStream(floorPlanStream));
		}

		if (backgroundImageStream != null) {
			floorPlanPut.addColumn(InBuildingConstants.COLUMN_FAMILY.getBytes(),
					InBuildingConstants.COLUMN_BG_IMAGE.getBytes(),
					InBuildingUtils.getBytesFromInputStream(backgroundImageStream));
		}
		if (floorplanJson != null) {
			floorPlanPut.addColumn(InBuildingConstants.COLUMN_FAMILY.getBytes(),
					InBuildingConstants.COLUMN_FLOOR_PLAN_JSON.getBytes(), floorplanJson.getBytes());
		}

		floorPlanPut.addColumn(InBuildingConstants.COLUMN_FAMILY.getBytes(),
				InBuildingConstants.COLUMN_BG_IMAGE_NAME.getBytes(), bgImageName.getBytes());

		return floorPlanPut;
	}

	/**
	 * Gets the floor plan put.
	 *
	 * @param unitInfoWrapper
	 *            the unit info wrapper
	 * @param floorPlanStream
	 *            the floor plan stream
	 * @param backgroundImageStream
	 *            the background image stream
	 * @param floorplanJson
	 *            the floorplan json
	 * @param bgImageName
	 *            the bg image name
	 * @return the floor plan put
	 */
	public static Put getFloorPlanPut(UnitInfoWrapper unitInfoWrapper, byte[] floorPlanStream,
			byte[] backgroundImageStream, String floorplanJson, String bgImageName) {
		Put floorPlanPut = new Put(
				Bytes.toBytes(InBuildingUtils.getFloorPlanRowKey(unitInfoWrapper.getUnitId())));
		if (floorPlanStream != null && floorPlanStream.length > 0) {
			floorPlanPut.addColumn(InBuildingConstants.COLUMN_FAMILY.getBytes(),
					InBuildingConstants.COLUMN_FLOOR_PLAN_IMAGE.getBytes(), floorPlanStream);
		}
		if (backgroundImageStream != null && backgroundImageStream.length > 0) {
			floorPlanPut.addColumn(InBuildingConstants.COLUMN_FAMILY.getBytes(),
					InBuildingConstants.COLUMN_BG_IMAGE.getBytes(), backgroundImageStream);
		}
		if (floorplanJson != null) {
			floorPlanPut.addColumn(InBuildingConstants.COLUMN_FAMILY.getBytes(),
					InBuildingConstants.COLUMN_FLOOR_PLAN_JSON.getBytes(), floorplanJson.getBytes());
		}
		if (bgImageName != null) {
			floorPlanPut.addColumn(InBuildingConstants.COLUMN_FAMILY.getBytes(),
					InBuildingConstants.COLUMN_BG_IMAGE_NAME.getBytes(), bgImageName.getBytes());
		}

		return floorPlanPut;
	}

	

	/**
	 * Gets the test result get for row key.
	 *
	 * @param rowKey
	 *            the row key
	 * @return the test result get for row key
	 */
	public static Get getTestResultGetForRowKey(String rowKey) {
		String columnFamily = InBuildingConstants.COLUMN_FAMILY;
		List<String> columns = new ArrayList<>();
		columns.add(InBuildingConstants.COLUMN_TEST_RESULT_JSON);
		columns.add(InBuildingConstants.COLUMN_TEST_START_TIME);
		columns.add(InBuildingConstants.COLUMN_TEST_END_TIME);
		columns.add(InBuildingConstants.COLUMN_TEST_TYPE);
		return getHbaseGet(rowKey, columnFamily, columns);
	}

	/**
	 * Gets the hbase get.
	 *
	 * @param rowkey
	 *            the rowkey
	 * @param columnFamily
	 *            the column family
	 * @param columns
	 *            the columns
	 * @return the hbase get
	 */
	public static Get getHbaseGet(String rowkey, String columnFamily, List<String> columns) {
		Get get = new Get(Bytes.toBytes(rowkey));
		if (columnFamily != null) {
			get.addFamily(Bytes.toBytes(columnFamily));
		}
		if (columns != null && !columns.isEmpty()) {
			for (String column : columns) {
				get.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(column));
			}
		}
		return get;
	}


	/**
	 * Copy File to Local Path.
	 *
	 * @param filePath
	 *            the File path
	 * @param dataStream
	 *            the input stream for the file
	 */
	public static void copyFileToLocalPath(String filePath, InputStream dataStream) {
		logger.info("Copying file to local filePath  {}:", filePath);
		File file = new File(filePath);
		try (FileOutputStream outputStream = new FileOutputStream(file)) {
			int bufferSize;
			byte[] buffer = new byte[InBuildingConstants.FILE_BUFFER_SIZE];
			while ((bufferSize = dataStream.read(buffer)) > InBuildingConstants.DATA_STREAM_END_VALUE) {
				outputStream.write(buffer, InBuildingConstants.OUTPUT_STREAM_START_OFFSET, bufferSize);
			}
		} catch (IOException e) {
			logger.error("Error! Exception while writing file to local: {}", Utils.getStackTrace(e));
		}
	}



	/**
	 * Prepare floor plan upload status.
	 *
	 * @param unitIds
	 *            the unit ids
	 * @param unitId
	 *            the unit id
	 * @param result
	 *            the result
	 */
	public static void prepareFloorPlanUploadStatus(Map<Integer, Boolean> unitIds, Integer unitId, String result) {
		logger.info("Result from hbase : {}", result);
		if (InBuildingConstants.SUCCESS_JSON.equals(result)) {
			unitIds.put(unitId, true);
		} else {
			unitIds.put(unitId, false);
		}
	}

	/**
	 * Extract building wrapper from request.
	 *
	 * @param response
	 *            the response
	 * @return the building wrapper
	 */
	public static BuildingWrapper extractBuildingWrapper(List<FileItem> fileItemList) {
		BuildingWrapper buildingWrapper = null;
		try {
			Optional<FileItem> optional = fileItemList	.stream()
														.filter(fileItem -> InBuildingConstants.KEY_DATA.equalsIgnoreCase(
																fileItem.getFieldName()))
														.findAny();
			if (optional.isPresent()) {
				buildingWrapper = new Gson().fromJson(optional	.get()
																.getString(),
						BuildingWrapper.class);
			}
		} catch (Exception e) {
			logger.error("Error in ExtractBuildingWrapperFromRequest{}", ExceptionUtils.getStackTrace(e));
		}
		return buildingWrapper;
	}


	/**
	 * Add Entry To Zip File.
	 *
	 * @param data
	 *            data to write in zip file
	 *
	 * @param fileName
	 *            fileName
	 *
	 * @param out
	 *            zip output stream
	 *
	 * @return out zip output stream with added entry
	 */
	public static ZipOutputStream addEntryToZipFile(byte[] data, String fileName, ZipOutputStream out) {
		ZipEntry entry = new ZipEntry(fileName);
		try {
			out.putNextEntry(entry);
			out.write(data, 0, data.length);
			out.closeEntry();
		} catch (IOException e) {
			logger.error("Error while creating entry in zip file: {}", Utils.getStackTrace(e));
		}

		return out;
	}

	/**
	 * GetJson From Input Stream.
	 *
	 * @param dataStream
	 *            the input stream
	 * @return json string
	 */
	public static String getJsonFromInputStream(InputStream dataStream) {
		String json = null;
		try {
			StringWriter writer = new StringWriter();
			IOUtils.copy(dataStream, writer, InBuildingConstants.ENCODING_UTF_8);
			json = writer.toString();
		} catch (IOException e) {
			logger.error("Error while processing InputStream to Json: {}", Utils.getStackTrace(e));
		}
		return json;
	}

	public static Integer getUnitId(BuildingWrapper buildingWrapper, String fieldName) {
		logger.info("file name from request header:{}", fieldName);
		Integer unitId = null;
		String[] key = fieldName.split("\\.");
		String[] name = key[InBuildingConstants.FIRST_INDEX].split(Symbol.UNDERSCORE_STRING);
		Optional<WingWrapper> wingWrapperOptional = buildingWrapper	.getWingList()
																	.stream()
																	.filter(wing -> wing.getWingName()
																						.equalsIgnoreCase(
																								name[InBuildingConstants.FIRST_INDEX]))
																	.findFirst();

		if (wingWrapperOptional.isPresent()) {
			Optional<FloorWrapper> floorWrapperOptional = wingWrapperOptional	.get()
																				.getFloorList()
																				.stream()
																				.filter(floor -> floor.getFloorNumber()
																									  .equalsIgnoreCase(
																												name[InBuildingConstants.SECOND_INDEX]))
																				.findFirst();
			if (floorWrapperOptional.isPresent()) {
				Optional<UnitWrapper> unitWrapperOptional = floorWrapperOptional.get()
																				.getUnitList()
																				.stream()
																				.filter(unit -> unit.getUnitName()
																									.equalsIgnoreCase(
																											name[InBuildingConstants.THIRD_INDEX]))
																				.findFirst();
				if (unitWrapperOptional.isPresent()) {
					unitId = unitWrapperOptional.get()
												.getUnitId();
				}
			}
		}
		return unitId;
	}
}

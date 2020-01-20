package com.inn.foresight.module.nv.report.customreport.ssvt.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.stream.Collectors;

import com.inn.foresight.module.nv.report.customreport.ssvt.wrapper.SiteInfoWrapper;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.inn.commons.Symbol;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.hadoop.hbase.HBaseResult;
import com.inn.commons.lang.StringUtils;
import com.inn.commons.maps.LatLng;
import com.inn.commons.maps.geometry.GeometryUtils;
import com.inn.commons.maps.geometry.MensurationUtils;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.report.utils.ReportUtils;
import com.inn.foresight.module.nv.layer3.constants.NVLayer3Constants;
import com.inn.foresight.module.nv.layer3.constants.QMDLConstant;
import com.inn.foresight.module.nv.layer3.utils.NVLayer3Utils;
import com.inn.foresight.module.nv.report.utils.DriveHeaderConstants;
import com.inn.foresight.module.nv.report.utils.ReportConstants;
import com.inn.foresight.module.nv.report.utils.ReportUtil;
import com.inn.foresight.module.nv.report.wrapper.SectorSwapWrapper;
import com.inn.foresight.module.nv.report.wrapper.SiteInformationWrapper;
import com.inn.foresight.module.nv.report.customreport.ssvt.constants.SSVTConstants;
import com.inn.foresight.module.nv.workorder.model.WORecipeMapping;

public class SSVTReportUtils {

	/**
	 * The logger.
	 */
	private static Logger logger = LogManager.getLogger(SSVTReportUtils.class);

	public static <K, V> boolean validateMap(Map<K, V> dataMap, String key) {
		if (key != null) {
			return dataMap != null && !dataMap.isEmpty() && dataMap.containsKey(key) && dataMap.get(key) != null;
		} else {
			return dataMap != null && !dataMap.isEmpty();
		}
	}

	public static String findUserName(Map<String, Object> jsonMap) {
		return jsonMap.get(NVLayer3Constants.ASSIGN_TO_JSON_KEY) != null ?
				jsonMap.get(NVLayer3Constants.ASSIGN_TO_JSON_KEY).toString() :
				null;
	}

	public static InputStream getLegendColorImage(Color color) {
		BufferedImage image = new BufferedImage(ReportConstants.BENCHMARK_LEGEND_IMAGE_WIDTH,
				ReportConstants.BENCHMARK_LEGEND_IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = (Graphics2D) image.getGraphics();
		graphics.setColor(color);
		graphics.fillRect(ReportConstants.INDEX_ZER0, ReportConstants.INDEX_ZER0,
				ReportConstants.BENCHMARK_LEGEND_IMAGE_WIDTH, ReportConstants.BENCHMARK_LEGEND_IMAGE_HEIGHT);
		return ReportUtil.getInputStreamFromBufferedImage(image);
	}

	public static Set<String> getAllPossibleIntraHOCombinations(List<SiteInformationWrapper> siteInfoList) {
		Set<Integer> pciFilteredSet = siteInfoList.stream()
												  .filter(data -> data.getPci() != null)
												  .map(SiteInformationWrapper::getPci)
												  .collect(Collectors.toSet());
		return getAllPairsFromSet(pciFilteredSet);
	}

	private static Set<String> getAllPairsFromSet(Set<Integer> set) {
		Set<String> resultSet = new HashSet<>();
		List<Integer> list = new ArrayList<>(set);
		for (int i = 0; i < list.size(); i++) {
			for (int j = 0; j < list.size(); j++) {
				if (i != j) {
					resultSet.add(list.get(i) + Symbol.SLASH_FORWARD_STRING + list.get(j));
				}
			}
		}
		return resultSet;
	}

	public static String getFileNameFromFilePath(String filePath) {
		if (filePath != null && (filePath.contains(ReportConstants.PDF_EXTENSION) || filePath.contains(
				ReportConstants.PPTX_EXTENSION))) {
			try {
				String fileName = filePath.substring(filePath.lastIndexOf(ReportConstants.FORWARD_SLASH) + 1,
						filePath.length());
				logger.info("Going to return the downloadFileName {} ", fileName);
				return fileName;
			} catch (Exception e) {
				logger.error("Exception inside method getFileNameFromFilePath {} ", Utils.getStackTrace(e));
			}
		}
		return null;
	}

	public static List<SectorSwapWrapper> getSectorSwapInfo(List<String[]> csvDataArray,
			Map<String, Object> siteDataMap, Map<String, Integer> kpiIndexMap) {
		Integer latitudeIndex = kpiIndexMap.get(ReportConstants.LATITUDE);
		Integer longitudeIndex = kpiIndexMap.get(ReportConstants.LONGITUDE);
		Integer pciIndex = kpiIndexMap.get(ReportConstants.PCI_PLOT);
		List<SectorSwapWrapper> swapList = new ArrayList<>();
		if(siteDataMap != null && Utils.isValidList(csvDataArray) && pciIndex != null && latitudeIndex != null && longitudeIndex != null) {
			logger.info("Site INFO Wrapper total Wrapper {}", siteDataMap);
			Map<String, List<String[]>> expectedPCIMap = null;

			List<String[]> totalDataList = csvDataArray.stream()
													   .filter(x -> x != null && x.length > pciIndex && x[pciIndex] != null)
													   .collect(Collectors.toList());

			List<SiteInformationWrapper> list = (List<SiteInformationWrapper>) siteDataMap.get(ReportConstants.SITE_INFO_LIST);

			List<SiteInformationWrapper> filterList = list.stream().filter(f -> !f.getIsNeighbourSite()).collect(Collectors.toList());

			if (Utils.isValidList(filterList)) {
				LatLng siteLatLng = new LatLng(filterList.get(ForesightConstants.ZERO).getLat(),
						filterList.get(ForesightConstants.ZERO).getLon());

				Map<Integer, List<SiteInformationWrapper>> sectorPciMap = filterList.stream()
																					.collect(Collectors.groupingBy(
																							SiteInformationWrapper::getSector));

				logger.info("Sector PCI Map {}", sectorPciMap.toString());
				for (Entry<Integer, List<SiteInformationWrapper>> entry : sectorPciMap.entrySet()) {

					List<List<Double>> sitePolygon = getSitePolygon(entry.getValue());
					if (pciIndex != null && latitudeIndex != null && longitudeIndex != null && sitePolygon != null) {
						expectedPCIMap = totalDataList.stream()
													  .filter(x -> x != null && x.length > pciIndex && !x[pciIndex].equalsIgnoreCase(
															  entry.getValue().toString()) && GeometryUtils.contains(
															  sitePolygon,
															  new LatLng(Double.valueOf(x[latitudeIndex]), Double.valueOf(x[longitudeIndex]))) &&
															  MensurationUtils.distanceBetweenPoints(siteLatLng, new LatLng(Double.valueOf(x[latitudeIndex]),
																	  Double.valueOf(x[longitudeIndex]))) > ForesightConstants.HUNDRED)
													  .collect(Collectors.groupingBy(x -> x[pciIndex]));
						for (Entry<String, List<String[]>> pciEntry : expectedPCIMap.entrySet()) {
							swapList = getSectorSwapData(entry, pciEntry);
						}
					}
				}
			}
		}
		return swapList;
	}

	private static double getAverageValue(int totolCount, int sampleCount) {
		Double avgValue = (sampleCount * ForesightConstants.HUNDRED_DOUBLE) / totolCount;
		return avgValue;
	}

	private static List<SectorSwapWrapper> getSectorSwapData(Entry<Integer, List<SiteInformationWrapper>> entry,
			Entry<String, List<String[]>> pciEntry) {
		List<SectorSwapWrapper> swapList;
		double averageValue = getAverageValue(entry.getValue().size(), pciEntry.getValue().size());
		SectorSwapWrapper sectorSwapWrapper = new SectorSwapWrapper();
		String isSwap = "FAIL";
		swapList = new ArrayList<>();
		if (averageValue >= ForesightConstants.FIFTY) {
			isSwap = "SUCCESS";
			sectorSwapWrapper.setSiteId(entry.getValue().get(ForesightConstants.ZERO).getSector().toString());
			sectorSwapWrapper.setSectorId(entry.getKey().toString());
			sectorSwapWrapper.setMeasuredPci(entry.getValue().get(ForesightConstants.ZERO).getPci().toString());
			sectorSwapWrapper.setExpectedPci(pciEntry.getKey());
			sectorSwapWrapper.setStatus(isSwap);
		} else {
			sectorSwapWrapper.setSiteId(entry.getValue().get(ForesightConstants.ZERO).getSector().toString());
			sectorSwapWrapper.setSectorId(entry.getKey().toString());
			sectorSwapWrapper.setMeasuredPci(entry.getValue().get(ForesightConstants.ZERO).getPci().toString());
			sectorSwapWrapper.setExpectedPci(pciEntry.getKey());
			sectorSwapWrapper.setStatus(isSwap);
		}
		swapList.add(sectorSwapWrapper);
		return swapList;
	}

	private static List<List<Double>> getSitePolygon(List<SiteInformationWrapper> filterList) {
		logger.debug("going to get site polygon for Sites Detail {}", filterList);
		for (SiteInformationWrapper siteInfo : filterList) {
			if (filterList != null) {
				return getSitePolygon(siteInfo.getTxPower(), siteInfo.getAntHight(), siteInfo.getLat(),
						siteInfo.getLon(), siteInfo.getHorizontalBeamWidth(), siteInfo.getActualAzimuth(),
						siteInfo.getNeFrequency());
			}
		}
		return null;
	}

	public static List<List<Double>> getSitePolygon(Double txPower, Double antennaHeight, Double latitude,
			Double longitude, Double horizontalBeamWidth, Integer azimuth, String frequency) {
		List<List<Double>> polygon = null;
		logger.info(
				"Going to get polygon here for tx power {} , AH {} , Lat {},long {}, and HBW {},azimuth {}, frequency {}",
				txPower, antennaHeight, latitude, longitude, horizontalBeamWidth, azimuth, frequency);
		Double radius = null;
		Double distance = null;
		if (txPower != null && antennaHeight != null && latitude != null && longitude != null
				&& horizontalBeamWidth != null && azimuth != null && frequency != null) {
			radius = ReportUtils.getRadius(txPower, Integer.parseInt(frequency));
			logger.info("Found the radius {}", radius);
			distance = Math.sqrt((radius * radius) - (antennaHeight * antennaHeight));
			polygon = getPolygonFromAzimuth(latitude, longitude, horizontalBeamWidth, azimuth, distance,
					2.0d);
		}
		logger.info("Found the polygon {}", polygon);
		return polygon;
	}

	private static List<List<Double>> getPolygonFromAzimuth(Double latitude, Double longitude, Double bandWidth,
			Integer azimuth, Double radius, double stepSize) {
		List<List<Double>> polygon = new ArrayList<>();
		List<Double> siteLocation = Arrays.asList(new Double[] { latitude, longitude });
		polygon.add(siteLocation);
		Double startAngle = azimuth - (bandWidth / 2);
		Double endAngle = azimuth + (bandWidth / 2);
		for (Double i = startAngle; i <= endAngle; i += stepSize) {
			polygon.add(getBearingDistance(radius, i, latitude, longitude));
		}
		polygon.add(siteLocation);
		return polygon;
	}

	public static List<Double> getBearingDistance(Double distance, Double bearing, Double latitude, Double longitude) {
		Double dist = distance / ReportConstants.EARTH_RADIUS;
		Double bearer = bearing * Math.PI / ReportConstants.ANGLE_180;
		Double latitude1 = latitude * Math.PI / ReportConstants.ANGLE_180;
		Double longitude1 = longitude * Math.PI / ReportConstants.ANGLE_180;
		Double latitude2 = Math.asin(
				Math.sin(latitude1) * Math.cos(dist) + Math.cos(latitude1) * Math.sin(dist) * Math.cos(bearer));
		Double longitude2 = longitude1 + Math.atan2(Math.sin(bearer) * Math.sin(dist) * Math.cos(latitude1),
				Math.cos(dist) - Math.sin(latitude1) * Math.sin(latitude2));
		longitude2 = (longitude2 + 3 * Math.PI) % (2 * Math.PI) - Math.PI;
		Double newLatitude = latitude2 * (ReportConstants.ANGLE_180 / Math.PI);
		Double newLongitude = longitude2 * (ReportConstants.ANGLE_180 / Math.PI);
		return Arrays.asList(new Double[] { newLatitude, newLongitude });
	}

	public static List<Get> getHandoverQueryList(Integer workorderId, List<String> recipeList,
			List<String> operatorList) {
		List<Get> getList = new ArrayList<>();
		for (String recipeId : recipeList) {
			String rowkey = NVLayer3Utils.getRowKeyForLayer3PPE(workorderId, recipeId);
			logger.info("rowkey =====" + rowkey);
			Get get = new Get(Bytes.toBytes(rowkey));
			get.addColumn(QMDLConstant.COLUMN_FAMILY.getBytes(),
					SSVTConstants.LAYER3_REPORT_COLUMN_HANDOVER.getBytes());
			getList.add(get);

		}
		return getList;
	}

	private static String getRowKeyForLayer3(Integer workorderId, String operatorName, String receipeId) {
		return NVLayer3Utils.getRowkeyFromWorkOrderId(workorderId) + operatorName
				+ NVLayer3Utils.getRowkeyFromWorkOrderId(receipeId);
	}

	public static List<String[]> getHandoverDataListFromHBaseResult(List<HBaseResult> resultList) {
		logger.info("Inside Method getHandoverDataListFromHBaseResult with data resultList size: {}",
				resultList.size());
		List<String[]> handoverDataList = new ArrayList<>();
		if (Utils.isValidList(resultList)) {
			for (HBaseResult result : resultList) {
				if (result != null) {
					String data = result.getString(SSVTConstants.LAYER3_REPORT_COLUMN_HANDOVER);
					if (!StringUtils.isBlank(data) && !SSVTConstants.LAYER3_REPORT_COLUMN_HANDOVER.equalsIgnoreCase(data)) {
						logger.info("Single String result: {}", data);
						List<String[]> convertedData = convertCSVStringToDataList(data);
						logger.info("Converted HO Data: {}", new Gson().toJson(convertedData));
						handoverDataList.addAll(convertCSVStringToDataList(data));
					}
				}
			}
		}
		logger.info("Returning Final HO data List: {}", new Gson().toJson(handoverDataList));
		return handoverDataList;
	}

	private static List<String[]> convertCSVStringToDataList(String data) {
		logger.info("inside the method convertCSVStringToDataList");
		List<String[]> arList = new ArrayList<>();
		try {
			if (data != null) {
				String[] ar = data.split("], \\[", -1);
				List<String> list = Arrays.asList(ar);
				for (String string : list) {
					String[] arr = string.replaceAll("]]", "").replace("//]", "").replaceAll("\\[", "").split(",", -1);
					arList.add(arr);
				}
			}
		} catch (Exception e) {
			logger.error("errorConvertingStringToDataList", Utils.getStackTrace(e));
		}
		return arList;
	}

	public static Map<String, List<WORecipeMapping>> getDriveTypeWiseRecipeMapping(List<WORecipeMapping> recipeMappingList) {
		return recipeMappingList.stream()
								.collect(Collectors.groupingBy(recipeMapping -> recipeMapping.getRecipe().getCategory(),
										Collectors.toList()));
	}

	public static void setMobilityDataToWrapper(SiteInfoWrapper siteInfo, Map<String, List<String[]>> csvDataMap,
			Map<String, Integer> kpiIndexMap) {
	//	logger.info("inside the method csvDataMap setMobilityDataToWrapper {} kpiIndexMap {}",csvDataMap,kpiIndexMap);
		Integer hoInitiate = 0;
		Integer hoSuccess = 0;
		Integer indexHOInitiate = kpiIndexMap.get(ReportConstants.HANDOVER_INITIATE);
		Integer indexHOSuccess = kpiIndexMap.get(ReportConstants.HANDOVER_SUCCESS);
		Integer indexHOInterruptionTime = kpiIndexMap.get(ReportConstants.HO_INTERRUPTION_TIME);
		if (validateMap(csvDataMap, SSVTConstants.DRIVE_DATA_TYPE_DRIVE)) {
			logger.info("inside valid Drive condtion ");
			for (String[] row : csvDataMap.get(SSVTConstants.DRIVE_DATA_TYPE_DRIVE)) {
				if (indexHOInitiate != null && checkValidation(indexHOInitiate, row)
						&& NumberUtils.isCreatable(row[indexHOInitiate])
						&& Integer.parseInt(row[indexHOInitiate]) != 0) {
					hoInitiate += Integer.parseInt(row[indexHOInitiate]);
				}
				if (indexHOSuccess != null && checkValidation(indexHOSuccess, row)
						&& NumberUtils.isCreatable(row[indexHOSuccess]) && Integer.parseInt(row[indexHOSuccess]) != 0) {
					hoSuccess += Integer.parseInt(row[indexHOSuccess]);
				}
			}
			String hoStatus = getHOSuccessStatus(hoInitiate, hoSuccess);
			if(hoStatus.contains(SSVTConstants.TEST_STATUS_FAIL)){
				siteInfo.setOverallStatus(SSVTConstants.TEST_STATUS_FAIL);
			}
			siteInfo.setHandoverSuccessRate(
					getHOReportText(hoInitiate, hoSuccess, hoStatus));
			if (indexHOInterruptionTime != null) {
				List<Double> hoInterruptionList = ReportUtil.convetArrayToList(
						csvDataMap.get(SSVTConstants.DRIVE_DATA_TYPE_DRIVE), indexHOInterruptionTime);
				String hoInterruptionTime = getHOInterruptionTimeInCriteria(hoInterruptionList);
				if(hoInterruptionTime != null && hoInterruptionTime.contains(SSVTConstants.TEST_STATUS_FAIL)){
					siteInfo.setOverallStatus(SSVTConstants.TEST_STATUS_FAIL);
				}
				siteInfo.setHandoverInterruptionTime(hoInterruptionTime);
			}
		}

	}

	public static String getHOInterruptionTimeInCriteria(List<Double> hoInterruptionList) {
		if (Utils.isValidList(hoInterruptionList)) {
			OptionalDouble avgHOInterruption = hoInterruptionList.stream()
																 .filter(x -> x != null)
																 .mapToDouble(x -> x)
																 .average();
			if (avgHOInterruption.isPresent()) {
				Double avgHOIntTime = avgHOInterruption.getAsDouble();
				return avgHOIntTime <= 75 ?
						SSVTConstants.TEST_STATUS_PASS + Symbol.PARENTHESIS_OPEN_STRING
								+ ReportUtil.round(avgHOIntTime, ReportConstants.TWO_DECIMAL_PLACES) + Symbol.PARENTHESIS_CLOSE_STRING :
						SSVTConstants.TEST_STATUS_FAIL + Symbol.PARENTHESIS_OPEN_STRING
								+ ReportUtil.round(avgHOIntTime, ReportConstants.TWO_DECIMAL_PLACES) + Symbol.PARENTHESIS_CLOSE_STRING;
			} else{
				return SSVTConstants.TEST_STATUS_FAIL + Symbol.PARENTHESIS_OPEN_STRING
						+ Symbol.HYPHEN_STRING + Symbol.PARENTHESIS_CLOSE_STRING;
			}
		}
		return null;
	}

	public static boolean checkValidation(int index, String[] record) {
		Boolean flag = false;
		if (record != null && index < record.length && Utils.hasValidValue(record[index])&& !record[index].isEmpty() && !record[index].contains(
				";@")) {
			flag = true;
		}
		return flag;
	}

	public static String getHOSuccessStatus(Integer hoInitiated, Integer hoSuccess) {
		return hoInitiated != null && hoSuccess != null && hoInitiated != ReportConstants.INDEX_ZER0
				&& hoSuccess >= hoInitiated ? SSVTConstants.TEST_STATUS_PASS : SSVTConstants.TEST_STATUS_FAIL;
	}

	public static String getHOReportText(Integer initiate, Integer success, String status) {
		return status + Symbol.PARENTHESIS_OPEN_STRING + success + Symbol.SLASH_FORWARD_STRING + initiate
				+ Symbol.PARENTHESIS_CLOSE_STRING;
	}

	public static void deleteAllFilesWithDirectory(List<String> filePathList) {
		for(String filePath : filePathList) {
			File file = new File(filePath);
			if (file.exists() && file.isDirectory()) {
				File[] filesList = file.listFiles();
				for (File singleFile : filesList) {
					if (singleFile.isDirectory() && singleFile.listFiles().length > ReportConstants.INDEX_ZER0) {
						deleteAllFilesWithDirectory(Arrays.asList(singleFile.getAbsolutePath()));
					} else {
						boolean delete = singleFile.delete();
						logger.info("Inner File Delete {}",delete);
					}
				}
				boolean delete = file.delete();
				logger.info("Outer File Delete {}",delete);
			}
		}
	}

	public static void addPciInEmptyFields(List<String[]> driveData, Map<String, Integer> kpiIndexMap){
		Integer previousPci = null;
		Integer indexPCI = kpiIndexMap.get(ReportConstants.PCI_PLOT);
		if(Utils.isValidList(driveData)) {
			for (String[] rowData : driveData) {
				if (rowData != null && indexPCI != null) {
					if (rowData.length > indexPCI && !StringUtils.isBlank(rowData[indexPCI]) && NumberUtils.isCreatable(
							rowData[indexPCI])) {
						previousPci = Integer.parseInt(rowData[indexPCI]);
					} else if (previousPci != null) {
						rowData[indexPCI] = previousPci.toString();
					}
				}
			}
		}
	}
	
	
	public static void addTestTypeInEmptyFields(List<String[]> driveData, Map<String, Integer> kpiIndexMap){
		String previousTestType = null;
		Integer indexTestType = kpiIndexMap.get(ReportConstants.TEST_TYPE);
		if(Utils.isValidList(driveData)) {
			for (String[] rowData : driveData) {
				if (rowData != null && indexTestType != null) {
					if (rowData.length > indexTestType && !StringUtils.isBlank(rowData[indexTestType])
							&& Utils.hasValidValue(rowData[indexTestType])) {
						previousTestType = rowData[indexTestType];
					} else if (previousTestType != null) {
						rowData[indexTestType] = previousTestType;
					}
				}
			}
		}
	}

	public static Map<String, List<String[]>> tagRecordsWithTestType(Map<String, List<String[]>> csvDataMap,
			Map<String, Integer> kpiIndexMap) {
		try {
			Map<String, List<String[]>> newCsvDataMap = new HashMap<>();
			for (Entry<String, List<String[]>> entry : csvDataMap.entrySet()) {
				List<String[]> dataList = entry.getValue();
				SSVTReportUtils.addTestTypeInEmptyFields(dataList, kpiIndexMap);
				newCsvDataMap.put(entry.getKey(), dataList);
			}
			return newCsvDataMap;
		} catch (Exception e) {
			logger.error("Exception in tagTestTypeInMissingPlace {}", Utils.getStackTrace(e));
		}
		return csvDataMap;
	}


	public static List<String[]> addNeighbourDataToDriveData(List<String[]> csvData, Map<String, String> neighbourData) {
		List<String[]> newCsvData = new ArrayList<>();
		for(String[] csvRow : csvData){
			String timestamp = csvRow[DriveHeaderConstants.INDEX_TIMESTAMP];
			if(SSVTReportUtils.validateMap(neighbourData, timestamp)){
				String[] newCsvRow = ArrayUtils.add(csvRow, String.valueOf(ReportUtil.convertCSVStringToDataList(neighbourData.get(timestamp)).size()));
				newCsvData.add(newCsvRow);
			} else{
				newCsvData.add(csvRow);
			}
		}
		return newCsvData;
	}

	public static HttpResponse sendPostRequestWithoutTimeOut(String uri, StringEntity httpEntity) throws IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(uri);
		try {
			logger.info("Going to hit URL : {}",uri);
			httpPost.setEntity(httpEntity);
			httpPost.addHeader(SSVTConstants.HEADER_X_API_KEY, ConfigUtils.getString(SSVTConstants.NV_SSVT_SF_INTEGRATION_X_API_KEY));
			CloseableHttpResponse response = httpClient.execute(httpPost);

			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				throw new RestException("Failed with HTTP error code : " + statusCode);
			}
			logger.info("Return Response Code : {}",statusCode);
			return response;
		} catch (Exception e) {
			throw new RestException(ReportConstants.EXCEPTION_ON_CONNECTION);
		}finally {
			httpClient.close();
		}
	}

	public static long getStartOfDayInMillis() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTimeInMillis();
	}

}

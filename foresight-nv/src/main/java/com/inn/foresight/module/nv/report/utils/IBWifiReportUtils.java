package com.inn.foresight.module.nv.report.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.lang.NumberUtils;
import com.inn.commons.lang.StringUtils;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.model.Unit;
import com.inn.foresight.core.report.model.AnalyticsRepository;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.wifi.NVIBWifiReportDataWrapper;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.wifi.NVIBWifiReportWrapper;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.wifi.NVIBWifiSubReportWrapper;
import com.inn.foresight.module.nv.workorder.model.WORecipeMapping;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class IBWifiReportUtils {

	/** The logger. */
	private static Logger logger = LogManager.getLogger(IBWifiReportUtils.class);

	public IBWifiReportUtils() {
		// Empty Constructor
	}

	public Integer getWorkorderIdFromAnalyticsRepo(AnalyticsRepository analyticrepositoryObj) {
		try {
			if (analyticrepositoryObj != null) {
				Map<String, Object> configMap = new JsonMapParser<String, Object>().convertJsonToMap(
						analyticrepositoryObj	.getReportConfig()
												.replaceAll("'", "\""));
				logger.info("AnalyticsRepository jsonObject {} ", configMap);
				if (!configMap.isEmpty()) {
					Integer workorderId = (Integer) configMap.get(ReportConstants.WORKORDER_ID);
					logger.info("going to invoke report generation method for workOrderIds size {} ", workorderId);
					return workorderId;
				}
			}
		} catch (Exception e) {
			logger.error("Error inside getWorkorderIdFromJson method in NVIBBenchmarkReportServiceImpl{} ",
					Utils.getStackTrace(e));
		}
		return null;
	}

	public static Map<Integer, List<String>> getUnitWiseRecipeListMap(List<String> recipeList,
			Map<String, Integer> recipeUnitIdMap) {
		Map<Integer, List<String>> unitRecipeMap = new HashMap<>();
		for (String recipeId : recipeList) {
			if (recipeUnitIdMap.containsKey(recipeId)) {
				if (unitRecipeMap.containsKey(recipeUnitIdMap.get(recipeId))) {
					List<String> existingRecipeList = unitRecipeMap.get(recipeUnitIdMap.get(recipeId));
					existingRecipeList.add(recipeId);
				} else {
					List<String> newRecipeList = new ArrayList<>();
					newRecipeList.add(recipeId);
					unitRecipeMap.put(recipeUnitIdMap.get(recipeId), newRecipeList);
				}
			}
		}
		return unitRecipeMap;
	}

	public static List<NVIBWifiReportDataWrapper> getSurveyDetailsPageData(Unit unit, WORecipeMapping woRecipeMapping) {
		List<NVIBWifiReportDataWrapper> surveyDetailsList = new ArrayList<>();
		addSurveyDetailToList(IBWifiConstants.ITEM_LABEL_BUILDING_NAME, unit.getFloor()
																			.getWing()
																			.getBuilding()
																			.getBuildingName(),
				surveyDetailsList);
		addSurveyDetailToList(IBWifiConstants.ITEM_LABEL_WING_NAME, unit.getFloor()
																		.getWing()
																		.getWingName(),
				surveyDetailsList);
		addSurveyDetailToList(IBWifiConstants.ITEM_LABEL_FLOOR_NUMBER, unit	.getFloor()
																			.getFloorName(),
				surveyDetailsList);
		addSurveyDetailToList(IBWifiConstants.ITEM_LABEL_UNIT_NAME, unit.getUnitName(), surveyDetailsList);
		addSurveyDetailToList(IBWifiConstants.ITEM_LABEL_WORKORDER_NAME, woRecipeMapping.getGenericWorkorder()
																						.getWorkorderName(),
				surveyDetailsList);
		addSurveyDetailToList(IBWifiConstants.ITEM_LABEL_RECIPE_NAME, woRecipeMapping	.getRecipe()
																						.getName(),
				surveyDetailsList);
		addSurveyDetailToList(IBWifiConstants.ITEM_LABEL_DATE,
				Utils.parseDateToString(woRecipeMapping	.getGenericWorkorder()
														.getCreationTime(),
						ReportConstants.DATE_FORMAT_DD_MM_YYYY_HH_MM_SS),
				surveyDetailsList);
		return surveyDetailsList;
	}

	private static void addSurveyDetailToList(String itemLabel, String itemValue,
			List<NVIBWifiReportDataWrapper> surveyDetailsList) {
		if (!StringUtils.isBlank(itemLabel) && !StringUtils.isBlank(itemValue)) {
			NVIBWifiReportDataWrapper surveyDataWrapper = new NVIBWifiReportDataWrapper();
			surveyDataWrapper.setItemLabel(itemLabel);
			surveyDataWrapper.setItemValue(itemValue);
			surveyDetailsList.add(surveyDataWrapper);
		}
	}

	public static List<NVIBWifiReportDataWrapper> getFloorPlanOverviewData(String floorPlanImage, String x, String y) {
		List<NVIBWifiReportDataWrapper> surveyDetailsList = new ArrayList<>();
		if (!StringUtils.isBlank(floorPlanImage)) {
			NVIBWifiReportDataWrapper dataWrapper = new NVIBWifiReportDataWrapper();
			dataWrapper.setFloorPlanImg(floorPlanImage);
			dataWrapper.setX(x);
			dataWrapper.setY(y);
			surveyDetailsList.add(dataWrapper);
		}
		return surveyDetailsList;
	}

	public static List<NVIBWifiReportDataWrapper> getAPConfigList() {
		List<NVIBWifiReportDataWrapper> surveyDetailsList = new ArrayList<>();
		NVIBWifiReportDataWrapper dataWrapper = new NVIBWifiReportDataWrapper();
		dataWrapper.setDesiredSignal(IBWifiConstants.DESIRED_SIGNAL_THRESHOLD);
		dataWrapper.setMinSNR(IBWifiConstants.MIN_SNR_THRESHOLD);
		dataWrapper.setMinDl(IBWifiConstants.MIN_DOWNLOAD_THRESHOLD);
		dataWrapper.setMinUl(IBWifiConstants.MIN_UPLOAD_THRESHOLD);
		dataWrapper.setMinPHYRate(IBWifiConstants.MIN_PHY_DATA_RATE_THRESHOLD);
		surveyDetailsList.add(dataWrapper);
		return surveyDetailsList;
	}

	public static List<NVIBWifiReportDataWrapper> getAPConfigTableListData(List<String[]> recipeDataList) {
		List<NVIBWifiReportDataWrapper> surveyDetailsList = new ArrayList<>();
		if (recipeDataList != null && !recipeDataList.isEmpty()) {
			Map<String, List<String[]>> bssidWiseRecipeDataMap = getBSSIDWiseMapFromRecipeData(recipeDataList);
			logger.debug("bssidWiseDataMap: {}", new Gson().toJson(bssidWiseRecipeDataMap));
			if (bssidWiseRecipeDataMap != null && !bssidWiseRecipeDataMap.isEmpty()) {
				for (Entry<String, List<String[]>> recipeDataEntry : bssidWiseRecipeDataMap.entrySet()) {
					NVIBWifiReportDataWrapper dataWrapper = new NVIBWifiReportDataWrapper();
					dataWrapper.setMacAddress(recipeDataEntry.getKey());
					dataWrapper.setApName(recipeDataEntry.getKey());
					setAPConfigDataToWrapper(recipeDataEntry, dataWrapper);
					surveyDetailsList.add(dataWrapper);
				}
			}
		}
		return surveyDetailsList;
	}

	public static List<NVIBWifiReportDataWrapper> getAPConfigTableListData(List<String[]> recipeDataList, Map<String, Integer> kpiIndexMap) {
		List<NVIBWifiReportDataWrapper> surveyDetailsList = new ArrayList<>();
		if (recipeDataList != null && !recipeDataList.isEmpty() && kpiIndexMap.get(ReportConstants.WIFI_BSSID) != null) {
			Map<String, List<String[]>> bssidWiseRecipeDataMap = getBSSIDWiseMapFromRecipeData(recipeDataList, kpiIndexMap.get(ReportConstants.WIFI_BSSID));
//			logger.debug("bssidWiseDataMap: {}", new Gson().toJson(bssidWiseRecipeDataMap));
			if (bssidWiseRecipeDataMap != null && !bssidWiseRecipeDataMap.isEmpty()) {
				for (Entry<String, List<String[]>> recipeDataEntry : bssidWiseRecipeDataMap.entrySet()) {
					if(!StringUtils.isBlank(recipeDataEntry.getKey())) {
						NVIBWifiReportDataWrapper dataWrapper = new NVIBWifiReportDataWrapper();
						dataWrapper.setMacAddress(recipeDataEntry.getKey());
						dataWrapper.setApName(recipeDataEntry.getKey());
						setAPConfigDataToWrapper(recipeDataEntry, dataWrapper, kpiIndexMap);
						surveyDetailsList.add(dataWrapper);
					}
				}
			}
		}
		return surveyDetailsList;
	}

	private static void setAPConfigDataToWrapper(Entry<String, List<String[]>> recipeDataEntry,
			NVIBWifiReportDataWrapper dataWrapper) {
		Double maxSignal = null;
		for (String[] recipeData : recipeDataEntry.getValue()) {
			if (recipeData != null && recipeData.length > IBWifiConstants.INDEX_DETAIL_WIFI_LINK_SPEED) {
				if (NumberUtils.isParsable(recipeData[IBWifiConstants.INDEX_DETAIL_WIFI_CHANNEL])) {
					dataWrapper.setChannel(Integer.parseInt(recipeData[IBWifiConstants.INDEX_DETAIL_WIFI_CHANNEL]));
				}
				if (NumberUtils.isParsable(recipeData[IBWifiConstants.INDEX_DETAIL_WIFI_RSSI])) {
					Double currentSignal = Double.parseDouble(recipeData[IBWifiConstants.INDEX_DETAIL_WIFI_RSSI]);
					if (maxSignal == null || maxSignal < currentSignal) {
						maxSignal = currentSignal;
					}
					dataWrapper.setMaxSignal(maxSignal);
				}
				dataWrapper.setSsid(recipeData[IBWifiConstants.INDEX_DETAIL_WIFI_SSID]);
				dataWrapper.setChannel(Integer.parseInt(recipeData[IBWifiConstants.INDEX_DETAIL_WIFI_CHANNEL]));
			}
		}
	}

	private static void setAPConfigDataToWrapper(Entry<String, List<String[]>> recipeDataEntry,
												 NVIBWifiReportDataWrapper dataWrapper, Map<String, Integer> kpiIndexMap) {
		Double maxSignal = null;
		for (String[] recipeData : recipeDataEntry.getValue()) {
			if (recipeData != null && kpiIndexMap.get(ReportConstants.WIFI_LINK_SPEED) != null
					&& recipeData.length > kpiIndexMap.get(ReportConstants.WIFI_LINK_SPEED)) {
				if (kpiIndexMap.get(ReportConstants.WIFI_CHANNEL) != null
						&& NumberUtils.isParsable(recipeData[kpiIndexMap.get(ReportConstants.WIFI_CHANNEL)])) {
					dataWrapper.setChannel(Integer.parseInt(recipeData[kpiIndexMap.get(ReportConstants.WIFI_CHANNEL)]));
				}
				if (kpiIndexMap.get(ReportConstants.WIFI_RSSI) != null
						&& NumberUtils.isParsable(recipeData[kpiIndexMap.get(ReportConstants.WIFI_RSSI)])) {
					Double currentSignal = Double.parseDouble(recipeData[kpiIndexMap.get(ReportConstants.WIFI_RSSI)]);
					if (maxSignal == null || maxSignal < currentSignal) {
						maxSignal = currentSignal;
					}
					dataWrapper.setMaxSignal(maxSignal);
				}

				if (kpiIndexMap.get(ReportConstants.SSID) != null) {
					dataWrapper.setSsid(recipeData[kpiIndexMap.get(ReportConstants.SSID)]);
				}
			}
		}
	}

	private static Map<String, List<String[]>> getBSSIDWiseMapFromRecipeData(List<String[]> recipeDataList) {
		return recipeDataList	.stream()
								.filter(array -> array != null
										&& array.length > IBWifiConstants.INDEX_DETAIL_WIFI_BSSID)
								.collect(
										Collectors.groupingBy(array -> array[IBWifiConstants.INDEX_DETAIL_WIFI_BSSID]));
	}

	private static Map<String, List<String[]>> getBSSIDWiseMapFromRecipeData(List<String[]> recipeDataList, Integer indexWifiBSSID) {
		return recipeDataList	.stream()
								.filter(array -> array != null && indexWifiBSSID != null
										&& array.length > indexWifiBSSID)
								.collect(
										Collectors.groupingBy(array -> array[indexWifiBSSID]));
	}

	public static List<NVIBWifiReportDataWrapper> getKpiDataList(List<String[]> recipeDataList, Integer kpiIndex,
			Double percentageThreshold, String desiredSignalThreshold) {
		List<NVIBWifiReportDataWrapper> surveyDetailsList = new ArrayList<>();
		NVIBWifiReportDataWrapper dataWrapper = new NVIBWifiReportDataWrapper();
		dataWrapper.setDesiredSignal(desiredSignalThreshold);
		setNoOfSamplesInCriteriaToWrapper(recipeDataList, kpiIndex, Double.parseDouble(desiredSignalThreshold),
				dataWrapper, percentageThreshold);
		surveyDetailsList.add(dataWrapper);
		return surveyDetailsList;
	}

	private static void setNoOfSamplesInCriteriaToWrapper(List<String[]> recipeDataList, Integer kpiIndex,
			Double threshold, NVIBWifiReportDataWrapper dataWrapper, Double percentageThreshold) {
		Integer totalSamples = recipeDataList	.stream()
												.filter(array -> array != null && kpiIndex != null && array.length > kpiIndex)
												.collect(Collectors.toList())
												.size();
		Integer samplesInCriteria = recipeDataList	.stream()
													.filter(array -> array != null && kpiIndex != null && array.length > kpiIndex
															&& NumberUtils.isParsable(array[kpiIndex])
															&& Double.parseDouble(array[kpiIndex]) >= threshold)
													.collect(Collectors.toList())
													.size();
		Double percentage = ReportUtil.getPercentage(samplesInCriteria, totalSamples);
		if (percentage != null) {
			dataWrapper.setPercentageSignal(percentage.toString());
			dataWrapper.setResult(percentage >= percentageThreshold ? IBWifiConstants.RESULT_TEXT_PASS
					: IBWifiConstants.RESULT_TEXT_FAIL);
		}
	}

	public static void setPlotImagesToReportWrapper(Map<String, String> plotImagesMap,
			NVIBWifiSubReportWrapper reportSubWrapper) {
		reportSubWrapper.setSurveyPathImg(
				getImagePathFromPlotImagesMap(plotImagesMap, IBWifiConstants.KEY_SURVEY_PATH_IMAGE));
		reportSubWrapper.setOverallSignalCoverageImg(
				getImagePathFromPlotImagesMap(plotImagesMap, IBWifiConstants.KEY_WIFI_RSSI_IMAGE));
		reportSubWrapper.setOverallSignalCoverageLegend(getImagePathFromPlotImagesMap(plotImagesMap,
				ReportConstants.LEGEND + ReportConstants.UNDERSCORE + IBWifiConstants.KEY_WIFI_RSSI_IMAGE));
		reportSubWrapper.setSnrImg(getImagePathFromPlotImagesMap(plotImagesMap, IBWifiConstants.KEY_WIFI_SNR_IMAGE));
		reportSubWrapper.setSnrLegend(getImagePathFromPlotImagesMap(plotImagesMap,
				ReportConstants.LEGEND + ReportConstants.UNDERSCORE + IBWifiConstants.KEY_WIFI_SNR_IMAGE));
		reportSubWrapper.setSpeedTestImg(
				getImagePathFromPlotImagesMap(plotImagesMap, IBWifiConstants.KEY_SPEED_TEST_IMAGE));
		if (reportSubWrapper.getBand() != null && reportSubWrapper	.getBand()
																	.contains(IBWifiConstants.WIFI_BAND_VALUE_5)) {
			reportSubWrapper.setDlThroughputImg(
					getImagePathFromPlotImagesMap(plotImagesMap, IBWifiConstants.KEY_WIFI_DL_5_GHZ));
			reportSubWrapper.setDlThroughputLegend(getImagePathFromPlotImagesMap(plotImagesMap,
					ReportConstants.LEGEND + ReportConstants.UNDERSCORE + IBWifiConstants.KEY_WIFI_DL_5_GHZ));
			reportSubWrapper.setUlThroughputImg(
					getImagePathFromPlotImagesMap(plotImagesMap, IBWifiConstants.KEY_WIFI_UL_5_GHZ));
			reportSubWrapper.setUlThroughputLegend(getImagePathFromPlotImagesMap(plotImagesMap,
					ReportConstants.LEGEND + ReportConstants.UNDERSCORE + IBWifiConstants.KEY_WIFI_UL_5_GHZ));
		} else {
			reportSubWrapper.setDlThroughputImg(
					getImagePathFromPlotImagesMap(plotImagesMap, IBWifiConstants.KEY_WIFI_DL_2_GHZ));
			reportSubWrapper.setDlThroughputLegend(getImagePathFromPlotImagesMap(plotImagesMap,
					ReportConstants.LEGEND + ReportConstants.UNDERSCORE + IBWifiConstants.KEY_WIFI_DL_2_GHZ));
			reportSubWrapper.setUlThroughputImg(
					getImagePathFromPlotImagesMap(plotImagesMap, IBWifiConstants.KEY_WIFI_UL_2_GHZ));
			reportSubWrapper.setUlThroughputLegend(getImagePathFromPlotImagesMap(plotImagesMap,
					ReportConstants.LEGEND + ReportConstants.UNDERSCORE + IBWifiConstants.KEY_WIFI_UL_2_GHZ));
		}
	}

	private static String getImagePathFromPlotImagesMap(Map<String, String> plotImagesMap, String key) {
		return plotImagesMap.containsKey(key) ? plotImagesMap.get(key) : null;
	}

	public static File proceedToCreateReport(List<NVIBWifiReportWrapper> dataSourceList, String fileName) {
		try {
			logger.info("Inside method generateInbuildingWifiReportAndReturnResponse {}", fileName);
			JRBeanCollectionDataSource rfbeanColDataSource = new JRBeanCollectionDataSource(dataSourceList);
			String destinationFileName = ConfigUtils.getString(IBWifiConstants.IB_WIFI_GENERATED_REPORT_PATH)
					+ fileName;
			String reportAssetsDirectory = ConfigUtils.getString(IBWifiConstants.IB_WIFI_JASPER_FILE_PATH);
			Map<String, Object> parameterMap = new HashMap<>();
			parameterMap.put(ReportConstants.SUBREPORT_DIR, reportAssetsDirectory);
			parameterMap.put(IBWifiConstants.REPORT_PARAM_KEY_HEADER_BG,
					reportAssetsDirectory + IBWifiConstants.IMAGE_NAME_HEADER_BG);
			parameterMap.put(IBWifiConstants.REPORT_PARAM_KEY_HEADER_LOGO,
					reportAssetsDirectory + IBWifiConstants.IMAGE_NAME_HEADER_LOGO);
			parameterMap.put(IBWifiConstants.REPORT_PARAM_KEY_FOOTER_IMAGE,
					reportAssetsDirectory + IBWifiConstants.IMAGE_NAME_FOOTER_IMAGE);
			parameterMap.put(IBWifiConstants.REPORT_PARAM_KEY_BEAN_LIST_SIZE, dataSourceList.size());

			JasperRunManager.runReportToPdfFile(reportAssetsDirectory + IBWifiConstants.IB_WIFI_MAIN_JASPER_FILE_NAME,
					destinationFileName, parameterMap, rfbeanColDataSource);

			return ReportUtil.getIfFileExists(destinationFileName);

		} catch (JRException e) {
			logger.error("Error inside proceedToCreateReport method in NVIBBenchmarkReportServiceImpl{} ",
					Utils.getStackTrace(e));
		}

		return null;

	}

	public static void removeJunkFilesFromDisk(List<String> junkFiles) {
		if (junkFiles != null && !junkFiles.isEmpty()) {
			for (String filePath : junkFiles) {
				File file = new File(filePath);
				if (file.exists() && file.isFile()) {
					boolean delete = file.delete();
					if(delete) {
						logger.info("File Deleted Successfully"); 
					}
				}
			}
		}
	}

	public static String getBandForRecipeData(List<String[]> recipeDataList) {
		if (recipeDataList != null && !recipeDataList.isEmpty()) {
			for (String[] singleRowData : recipeDataList) {
				if (singleRowData != null && singleRowData.length > DriveHeaderConstants.BAND_INDEX
						&& !StringUtils.isBlank(singleRowData[DriveHeaderConstants.BAND_INDEX])) {
					return singleRowData[DriveHeaderConstants.BAND_INDEX];
				}
			}
		}
		return null;
	}

	public static String getBandForRecipeData(List<String[]> recipeDataList, Integer bandIndex) {
		if (recipeDataList != null && !recipeDataList.isEmpty() && bandIndex != null) {
			for (String[] singleRowData : recipeDataList) {
				if (singleRowData != null && singleRowData.length > bandIndex
						&& !StringUtils.isBlank(singleRowData[bandIndex])) {
					return singleRowData[bandIndex];
				}
			}
		}
		return null;
	}

	public List<NVIBWifiReportDataWrapper> getSpeedTestDataList(List<String[]> recipeDataList) {
		List<NVIBWifiReportDataWrapper> speedTestDataList = new ArrayList<>();
		if (recipeDataList != null && !recipeDataList.isEmpty()) {
			List<String[]> speedTestList = recipeDataList	.stream()
															.filter(data -> data != null
																	&& data.length > IBWifiConstants.INDEX_DETAIL_DL_TIME_YOUTUBE
																	&& !StringUtils.isBlank(
																			data[IBWifiConstants.INDEX_DETAIL_ST_PIN_NUMBER]))
															.collect(Collectors.toList());


			for (String[] speedTestRow : speedTestList) {
				NVIBWifiReportDataWrapper speedTestDataWrapper = new NVIBWifiReportDataWrapper();
				speedTestDataWrapper.setPin(speedTestRow[IBWifiConstants.INDEX_DETAIL_ST_PIN_NUMBER]);
				speedTestDataWrapper.setRssi(speedTestRow[IBWifiConstants.INDEX_DETAIL_WIFI_RSSI]);
				speedTestDataWrapper.setSnr(speedTestRow[IBWifiConstants.INDEX_DETAIL_WIFI_SNR]);
				speedTestDataWrapper.setDlThroughput(speedTestRow[IBWifiConstants.INDEX_DETAIL_ST_DL_RATE]);
				speedTestDataWrapper.setUlThroughput(speedTestRow[IBWifiConstants.INDEX_DETAIL_ST_UL_RATE]);
				speedTestDataWrapper.setJitter(speedTestRow[IBWifiConstants.INDEX_DETAIL_ST_JITTER]);
				speedTestDataWrapper.setLatency(speedTestRow[IBWifiConstants.INDEX_DETAIL_ST_LATENCY]);
				speedTestDataWrapper.setPacketLoss(speedTestRow[IBWifiConstants.INDEX_DETAIL_ST_PACKET_LOSS]);
				speedTestDataWrapper.setDlTimeGoogle(speedTestRow[IBWifiConstants.INDEX_DETAIL_DL_TIME_GOOGLE]);
				speedTestDataWrapper.setDlTimeFacebook(speedTestRow[IBWifiConstants.INDEX_DETAIL_DL_TIME_FACEBOOK]);
				speedTestDataWrapper.setDlTimeYoutube(speedTestRow[IBWifiConstants.INDEX_DETAIL_DL_TIME_YOUTUBE]);
				speedTestDataList.add(speedTestDataWrapper);
			}


		}
		return speedTestDataList;
	}

	public List<NVIBWifiReportDataWrapper> getSpeedTestDataListForReport(List<String[]> recipeDataList, Map<String, Integer> kpiIndexMap) {
		logger.info("inside getSpeedTestDataListForReport");
		List<NVIBWifiReportDataWrapper> speedTestDataList = new ArrayList<>();
		if (recipeDataList != null && !recipeDataList.isEmpty()) {
			List<String[]> speedTestList = recipeDataList	.stream()
					.filter(data -> data != null 
							&& kpiIndexMap.get(ReportConstants.SPEED_TEST_PIN_NUMBER) != null
							&& !StringUtils.isBlank(
							data[kpiIndexMap.get(ReportConstants.SPEED_TEST_PIN_NUMBER)]))
					.collect(Collectors.toList());


			for (String[] speedTestRow : speedTestList) {
				NVIBWifiReportDataWrapper speedTestDataWrapper = new NVIBWifiReportDataWrapper();
				if (kpiIndexMap.get(ReportConstants.SPEED_TEST_PIN_NUMBER) != null) {
					speedTestDataWrapper.setPin(speedTestRow[kpiIndexMap.get(ReportConstants.SPEED_TEST_PIN_NUMBER)]);
				}
				if (kpiIndexMap.get(ReportConstants.WIFI_RSSI) != null) {
					speedTestDataWrapper.setRssi(speedTestRow[kpiIndexMap.get(ReportConstants.WIFI_RSSI)]);
				}
				if (kpiIndexMap.get(ReportConstants.WIFI_SNR) != null) {
					speedTestDataWrapper.setSnr(speedTestRow[kpiIndexMap.get(ReportConstants.WIFI_SNR)]);
				}
				if (kpiIndexMap.get(ReportConstants.SPEED_TEST_DL_RATE) != null) {
					speedTestDataWrapper.setDlThroughput(speedTestRow[kpiIndexMap.get(ReportConstants.SPEED_TEST_DL_RATE)]);
				}
				if (kpiIndexMap.get(ReportConstants.SPEED_TEST_UL_RATE) != null) {
					speedTestDataWrapper.setUlThroughput(speedTestRow[kpiIndexMap.get(ReportConstants.SPEED_TEST_UL_RATE)]);
				}
				if (kpiIndexMap.get(ReportConstants.JITTER) != null) {
					speedTestDataWrapper.setJitter(speedTestRow[kpiIndexMap.get(ReportConstants.JITTER)]);
				}
				if (kpiIndexMap.get(ReportConstants.LATENCY) != null) {
					speedTestDataWrapper.setLatency(speedTestRow[kpiIndexMap.get(ReportConstants.LATENCY)]);
				}
				if (kpiIndexMap.get(ReportConstants.PACKET_LOSS) != null) {
					speedTestDataWrapper.setPacketLoss(speedTestRow[kpiIndexMap.get(ReportConstants.PACKET_LOSS)]);
				}
				if (kpiIndexMap.get(ReportConstants.DOWNLOAD_TIME_GOOGLE) != null) {
					speedTestDataWrapper.setDlTimeGoogle(speedTestRow[kpiIndexMap.get(ReportConstants.DOWNLOAD_TIME_GOOGLE)]);
				}
				if (kpiIndexMap.get(ReportConstants.DOWNLOAD_TIME_FACEBOOK) != null) {
					speedTestDataWrapper.setDlTimeFacebook(speedTestRow[kpiIndexMap.get(ReportConstants.DOWNLOAD_TIME_FACEBOOK)]);
				}
				if (kpiIndexMap.get(ReportConstants.DOWNLOAD_TIME_YOUTUBE) != null) {
					speedTestDataWrapper.setDlTimeYoutube(speedTestRow[kpiIndexMap.get(ReportConstants.DOWNLOAD_TIME_YOUTUBE)]);
				}
				speedTestDataList.add(speedTestDataWrapper);
			}


		}
		return speedTestDataList;
	}

	public List<String[]> removeSpeedTestDataFromRecipeData(List<String[]> recipeDataList) {
		List<String[]> filteredDataList = new ArrayList<>();
		for (String[] singleRow : recipeDataList) {
			if (singleRow != null) {
				if (singleRow.length > IBWifiConstants.INDEX_DETAIL_ST_PIN_NUMBER) {
					if (StringUtils.isBlank(singleRow[IBWifiConstants.INDEX_DETAIL_ST_PIN_NUMBER])) {
						filteredDataList.add(singleRow);
					}
				} else {
					filteredDataList.add(singleRow);
				}
			}
		}
		return filteredDataList;
	}

	public List<String[]> removeSpeedTestDataFromRecipeData(List<String[]> recipeDataList, Integer pinNumberIndex) {
		List<String[]> filteredDataList = new ArrayList<>();
		for (String[] singleRow : recipeDataList) {
			if (singleRow != null) {
				if (pinNumberIndex != null && singleRow.length > pinNumberIndex) {
					if (StringUtils.isBlank(singleRow[pinNumberIndex])) {
						filteredDataList.add(singleRow);
					}
				} else {
					filteredDataList.add(singleRow);
				}
			}
		}
		return filteredDataList;
	}

}

package com.inn.foresight.module.nv.report.stealthdashboard.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.inn.commons.Symbol;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.encoder.AESUtils;
import com.inn.commons.hadoop.hbase.HBaseResult;
import com.inn.commons.lang.NumberUtils;
import com.inn.commons.lang.StringUtils;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.maplayer.service.IGenericMapService;
import com.inn.foresight.core.maplayer.utils.GenericMapUtils;
import com.inn.foresight.core.report.dao.IAnalyticsRepositoryDao;
import com.inn.foresight.core.report.model.AnalyticsRepository;
import com.inn.foresight.module.nv.report.parse.wrapper.Geography;
import com.inn.foresight.module.nv.report.parse.wrapper.GeographyParser;
import com.inn.foresight.module.nv.report.service.IMapImagesService;
import com.inn.foresight.module.nv.report.service.IReportService;
import com.inn.foresight.module.nv.report.stealthdashboard.service.IStealthDashboardReportService;
import com.inn.foresight.module.nv.report.stealthdashboard.wrapper.*;
import com.inn.foresight.module.nv.report.utils.LegendUtil;
import com.inn.foresight.module.nv.report.utils.ReportConstants;
import com.inn.foresight.module.nv.report.utils.ReportUtil;
import com.inn.foresight.module.nv.report.utils.StealthUtils;
import com.inn.foresight.module.nv.workorder.stealth.constants.StealthConstants;
import com.inn.foresight.module.nv.workorder.stealth.dao.IStealthTaskHbaseDao;
import com.inn.product.um.geography.dao.GeographyL4Dao;
import com.inn.product.um.geography.model.GeographyL4;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.ws.rs.core.Response;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Service("StealthDashboardReportServiceImpl") public class StealthDashboardReportServiceImpl
		implements IStealthDashboardReportService {

	private Logger logger = LogManager.getLogger(StealthDashboardReportServiceImpl.class);

	@Autowired
	private IAnalyticsRepositoryDao analyticsRepositoryDao;

	@Autowired
	private IReportService reportService;

	@Autowired
	private GeographyL4Dao geographyL4Dao;

	@Autowired
	private IStealthTaskHbaseDao stealthTaskHbaseDao;

	@Autowired
	private IGenericMapService iGenericMapService;

	@Autowired
	private IMapImagesService mapImagesService;

	@Override
	public Response execute(String json) {
		try {
			Map<String, Object> jsonMap = reportService.getJsonDataMap(json);
			Integer analyticsRepositoryId = (Integer) jsonMap.get(ForesightConstants.ANALYTICAL_REPORT_KEY);
			AnalyticsRepository analyticsRepository = analyticsRepositoryDao.findByPk(analyticsRepositoryId);
			String geographyConfig = analyticsRepository.getGeographyConfig() != null ?
					analyticsRepository.getGeographyConfig().replaceAll("\'", "\"") :
					null;
			String reportConfig = analyticsRepository.getGeographyConfig() != null ?
					analyticsRepository.getReportConfig().replaceAll("\'", "\"") :
					null;
			logger.info("Geography config is: {}", geographyConfig);
			logger.info("report config is: {}", reportConfig);
			Map<String, Object> configMap = new ObjectMapper().readValue(reportConfig, HashMap.class);
			Long timestamp = (Long) configMap.get("timestamp");
			GeographyParser geographyParser = new Gson().fromJson(geographyConfig, GeographyParser.class);
			List<Geography> geography = getGeographyList(geographyParser);
			Map<String, List<GeographyL4>> geographyL4Map = getGeographyL4CorrespondingToGeography(geography);
			logger.info("Geography map is: {}", new Gson().toJson(geographyL4Map));
			Date startTime = getStartDateFromEndTimestamp(timestamp);
			List<String> dateList = StealthUtils.getDateRange(startTime, new Date(timestamp),
					StealthConstants.DATE_FORMATE);
			Map<String, Map<String, List<HBaseResult>>> dataMapForStealthDashboard = getDataMapForStealthDashboard(
					geographyL4Map, dateList);
//			dataMapForStealthDashboard.put("TOMAKOMAI-SHI", dataMapForStealthDashboard.get("TOKYO"));
//			dataMapForStealthDashboard.put("SETAGAYA-KU", dataMapForStealthDashboard.get("TOKYO"));
//			geographyL4Map.put("TOMAKOMAI-SHI", geographyL4Map.get("TOKYO"));
//			geographyL4Map.put("SETAGAYA-KU", geographyL4Map.get("TOKYO"));
			logger.info("Hbase Data Map is: {}", new Gson().toJson(dataMapForStealthDashboard));
			StealthDashboardReportWrapper mainWrapper = new StealthDashboardReportWrapper();
			addGraphDataToMainWrapper(mainWrapper, dataMapForStealthDashboard, dateList);
			addPlotDataToMainWrapper(mainWrapper, dataMapForStealthDashboard, geographyL4Map);
			logger.info("Main Wrapper is: {}", new Gson().toJson(mainWrapper));
			String filePath = ConfigUtils.getString(ReportConstants.NV_REPORTS_PATH) + ReportConstants.STEALTH
					+ ReportConstants.FORWARD_SLASH;
			File file = new File(filePath);
			if (!file.exists()) {
				file.mkdirs();
			}
			String destinationFilePath = ReportUtil.getFileName("STEALTH_EXPRESS_REPORT", analyticsRepositoryId,
					filePath);
			File pdfFile = proceedToCreateReport(mainWrapper, destinationFilePath, ReportUtil.getFormattedDate(new Date(timestamp), "dd MMM"), ReportUtil.getFormattedDate(startTime, "dd MMM"));
			String hdfsFilePath = ConfigUtils.getString(ReportConstants.NV_REPORTS_PATH_HDFS) + ReportConstants.STEALTH
					+ ReportConstants.FORWARD_SLASH;
			return reportService.saveFile(analyticsRepositoryId, hdfsFilePath, pdfFile);
		} catch (Exception e) {
			logger.error("Exception while preparing stealth dashboard report: {}", Utils.getStackTrace(e));
		}
		return null;
	}

	private File proceedToCreateReport(StealthDashboardReportWrapper mainWrapper, String destinationFileName,
			String formattedDate, String formattedStartDate) {
		logger.info("proceedToCreateClusterAcceptanceReport");
		try {
			String reportAssetRepo = ConfigUtils.getString(ReportConstants.STEALTH_DASHBOARD_REPORT_JASPER_FOLDER_PATH);
			List<StealthDashboardReportWrapper> dataSourceList = new ArrayList<>();
			dataSourceList.add(mainWrapper);
			JRBeanCollectionDataSource rfbeanColDataSource = new JRBeanCollectionDataSource(dataSourceList);
			HashMap<String, Object> imageMap = new HashMap<>();
			imageMap.put("endDate", formattedDate);
			imageMap.put("startDate", formattedStartDate);
			imageMap.put(ReportConstants.SUBREPORT_DIR, reportAssetRepo);
			imageMap.put(ReportConstants.IMAGE_PARAM_SCREEN_BG, reportAssetRepo + ReportConstants.IMAGE_FIRST_IMG);
			imageMap.put(ReportConstants.IMAGE_PARAM_SCREEN_LOG, reportAssetRepo + ReportConstants.FOOTER_LOGO_IMG);
			imageMap.put(ReportConstants.IMAGE_PARAM_FOOTER, reportAssetRepo + ReportConstants.IMAGE_FOOTER);
			imageMap.put(ReportConstants.IMAGE_PARAM_HEADER_LOGO,
					reportAssetRepo + ReportConstants.IMAGE_NAME_HEADER_LOGO_JPG);
			JasperRunManager.runReportToPdfFile(reportAssetRepo + ReportConstants.MAIN_JASPER, destinationFileName,
					imageMap, rfbeanColDataSource);
			logger.info("Report Created successfully ");

			return ReportUtil.getIfFileExists(destinationFileName);
		} catch (Exception e) {
			logger.info("proceedToCreateClusterAcceptanceReport getting err={}", Utils.getStackTrace(e));
		}
		logger.info(
				"proceedToCreateClusterAcceptanceReport going to return null as there has been some problem in generating report");
		return null;
	}

	private void addPlotDataToMainWrapper(StealthDashboardReportWrapper mainWrapper,
			Map<String, Map<String, List<HBaseResult>>> dataMapForStealthDashboard,
			Map<String, List<GeographyL4>> geographyL4Map) throws IOException {
		SDGraphMapDataWrapper mapPlotWrapper = new SDGraphMapDataWrapper();
		List<SDGraphMapDataWrapper> mapPlotDataWrapperList = new ArrayList<>();
		StealthDashboardSummaryWrapper summaryWrapper = new StealthDashboardSummaryWrapper();
		List<SDGeographyDataWrapper> summaryGeographyList = new ArrayList<>();
		for (Map.Entry<String, Map<String, List<HBaseResult>>> dataEntry : dataMapForStealthDashboard.entrySet()) {
			SDGeographyDataWrapper summary = new SDGeographyDataWrapper();
			summary.setGeographyName(dataEntry.getKey());
			SDGraphMapDataWrapper geographyDataWrapper = new SDGraphMapDataWrapper();
			geographyDataWrapper.setGeographyName(dataEntry.getKey());
			Map<String, List<HBaseResult>> listMap = dataEntry.getValue();
			addCoverageDataToWrapper(geographyDataWrapper, listMap, geographyL4Map.get(dataEntry.getKey()), summary);
			mapPlotDataWrapperList.add(geographyDataWrapper);
			summaryGeographyList.add(summary);
		}
		summaryWrapper.setGeographyDevicesList(summaryGeographyList);
		mapPlotWrapper.setMapPlotDataList(mapPlotDataWrapperList);
		mainWrapper.setMapPlotList(Arrays.asList(mapPlotWrapper));
		mainWrapper.setSummaryDataList(Arrays.asList(summaryWrapper));
	}

	private void addCoverageDataToWrapper(SDGraphMapDataWrapper geographyDataWrapper,
			Map<String, List<HBaseResult>> listMap, List<GeographyL4> geographyL4s, SDGeographyDataWrapper summary) throws IOException {
		List<StealthGeographyDataWrapper> geographyWiseDataWrapperList = new ArrayList<>();
		Double totalSamples = 0.0;
		if(Utils.isValidList(geographyL4s)) {
			for (GeographyL4 geographyL4 : geographyL4s) {
				if (listMap.containsKey(geographyL4.getDisplayName())) {
					List<HBaseResult> dataList = listMap.get(geographyL4.getDisplayName());
				if (Utils.isValidList(dataList)) {
					StealthGeographyDataWrapper geographyWrapper = new StealthGeographyDataWrapper();
					geographyWrapper.setLatitude(geographyL4.getLatitude());
					geographyWrapper.setLongitude(geographyL4.getLongitude());
					geographyWrapper.setGeographyName(geographyL4.getDisplayName());
					geographyWrapper.setBoundaryData(getBoundaryData(geographyL4.getName()));
					addKpiDataToGeographyWrapper(geographyWrapper, dataList);
					if (geographyWrapper.getRsrpCount() != null) {
						totalSamples += geographyWrapper.getRsrpCount();
					}
					setMapPlotDataToWrapper(geographyWrapper);
					geographyWiseDataWrapperList.add(geographyWrapper);
				}
			}
			}
			if (totalSamples != null) {
				summary.setNoOfSamples(String.valueOf(totalSamples.longValue()));
			}
			setMapImagesToWrapper(geographyDataWrapper, geographyWiseDataWrapperList);
			setOverallKpiAverageForGeography(geographyDataWrapper, geographyWiseDataWrapperList);
			setTopWorstGeographyDataList(geographyWiseDataWrapperList, geographyDataWrapper);
		}
	}

	private void setOverallKpiAverageForGeography(SDGraphMapDataWrapper geographyDataWrapper,
			List<StealthGeographyDataWrapper> geographyWiseDataWrapperList) {
		Double rsrpSum = geographyWiseDataWrapperList.stream()
													 .filter(x -> x.getRsrpSum() != null)
													 .mapToDouble(x -> x.getRsrpSum())
													 .sum();
		Double rsrpCount = geographyWiseDataWrapperList.stream()
													   .filter(x -> x.getRsrpCount() != null)
													   .mapToDouble(x -> x.getRsrpCount())
													   .sum();
		Double sinrSum = geographyWiseDataWrapperList.stream()
													 .filter(x -> x.getSinrSum() != null)
													 .mapToDouble(x -> x.getSinrSum())
													 .sum();
		Double sinrCount = geographyWiseDataWrapperList.stream()
													   .filter(x -> x.getSinrCount() != null)
													   .mapToDouble(x -> x.getSinrCount())
													   .sum();
		Double dlSum = geographyWiseDataWrapperList.stream()
												   .filter(x -> x.getDlSum() != null)
												   .mapToDouble(x -> x.getDlSum())
												   .sum();
		Double dlCount = geographyWiseDataWrapperList.stream()
													 .filter(x -> x.getDlCount() != null)
													 .mapToDouble(x -> x.getDlCount())
													 .sum();
		Double ulSum = geographyWiseDataWrapperList.stream()
												   .filter(x -> x.getUlSum() != null)
												   .mapToDouble(x -> x.getUlSum())
												   .sum();
		Double ulCount = geographyWiseDataWrapperList.stream()
													 .filter(x -> x.getUlCount() != null)
													 .mapToDouble(x -> x.getUlCount())
													 .sum();

		geographyDataWrapper.setAvgRsrp(ReportUtil.round((rsrpSum / rsrpCount), ReportConstants.TWO_DECIMAL_PLACES));
		geographyDataWrapper.setAvgSinr(ReportUtil.round((sinrSum / sinrCount), ReportConstants.TWO_DECIMAL_PLACES));
		geographyDataWrapper.setAvgDl(ReportUtil.round((dlSum / dlCount), ReportConstants.TWO_DECIMAL_PLACES));
		geographyDataWrapper.setAvgUl(ReportUtil.round((ulSum / ulCount), ReportConstants.TWO_DECIMAL_PLACES));

	}

	private void setMapImagesToWrapper(SDGraphMapDataWrapper geographyDataWrapper,
			List<StealthGeographyDataWrapper> geographyWiseDataWrapperList) {
		HashMap<String, String> clusterBoundaryImage = mapImagesService.getClusterBoundaryImage(
				geographyWiseDataWrapperList);
		String saveImagePath = ConfigUtils.getString(ReportConstants.IMAGE_PATH_FOR_NV_REPORT)
				+ ReportConstants.STEALTH + ReportConstants.FORWARD_SLASH;
		File file = new File(saveImagePath);
		if(!file.exists()){
			file.mkdirs();
		}
		geographyDataWrapper.setCoverageMapPlot(clusterBoundaryImage.get("RSRP_IMAGE"));
		geographyDataWrapper.setCoverageMapLegend(getLegendImageForMapPlot("COVERAGE", saveImagePath));
		geographyDataWrapper.setThroughputMapPlot(clusterBoundaryImage.get("DL_IMAGE"));
		geographyDataWrapper.setThroughputMapLegend(getLegendImageForMapPlot("THROUGHPUT", saveImagePath));
	}

	private String getLegendImageForMapPlot(String type, String path) {
		String finalPath = path + type + "_legend.jpg";
		try {
			Map<String, Color> colorMap = new HashMap<>();
			if (type.equalsIgnoreCase("COVERAGE")) {
				colorMap.putAll(getColorValueMapForCoverage());
			} else{
				colorMap.putAll(getColorValueMapForThroughput());
			}
			ImageIO.write(LegendUtil.getLegendStripForValueAndColor(colorMap), "jpg",
					new File(finalPath));
		} catch (Exception e) {
			logger.error("Error while writing Legend Image: {}", Utils.getStackTrace(e));
		}
		return finalPath;
	}

	private Map<String, Color> getColorValueMapForCoverage() {
		Map<String, Color> map = new HashMap<>();
		map.put("-140", Color.decode("#FA403C"));
		map.put("-125", Color.decode("#FA6D3C"));
		map.put("-115", Color.decode("#F6E936"));
		map.put("-110", Color.decode("#5FE427"));
		map.put("-105", Color.decode("#368706"));
		map.put("-80", Color.decode("#0ED5FA"));
		map.put("-60", Color.decode("#3275FB"));
		map.put("-40", null);
		return map;
	}

	private Map<String, Color> getColorValueMapForThroughput() {
		Map<String, Color> map = new HashMap<>();
		map.put("0", Color.decode("#FA403C"));
		map.put("1", Color.decode("#FA6D3C"));
		map.put("2", Color.decode("#F6E936"));
		map.put("6", Color.decode("#5FE427"));
		map.put("10", Color.decode("#368706"));
		map.put("20", Color.decode("#0ED5FA"));
		map.put("30", Color.decode("#3275FB"));
		map.put("200", null);
		return map;
	}

	private void setMapPlotDataToWrapper(StealthGeographyDataWrapper geographyWrapper) {
		Double dlAverage = null;
		if (geographyWrapper.getDlSum() != null && geographyWrapper.getDlCount() != null) {
			dlAverage = ReportUtil.round((geographyWrapper.getDlSum() / geographyWrapper.getDlCount()),
					ReportConstants.TWO_DECIMAL_PLACES);
			logger.info("dl average is: {}", dlAverage);
			geographyWrapper.setAvgDl(dlAverage);
		}
		Double rsrpAverage = null;
		if (geographyWrapper.getRsrpSum() != null && geographyWrapper.getRsrpCount() != null) {
			rsrpAverage = ReportUtil.round((geographyWrapper.getRsrpSum() / geographyWrapper.getRsrpCount()),
					ReportConstants.TWO_DECIMAL_PLACES);
			logger.info("rsrp average is: {}", rsrpAverage);
			geographyWrapper.setAvgRSRP(rsrpAverage);
		}
		setKpiColorForAverageValue(geographyWrapper, dlAverage, rsrpAverage);

	}

	private void setKpiColorForAverageValue(StealthGeographyDataWrapper geographyWrapper, Double dlAverage,
			Double rsrpAverage) {
		if (rsrpAverage != null) {
			if (rsrpAverage >= -140 && rsrpAverage < -125) {
				geographyWrapper.setRsrpPlotColor(Color.decode("#FA403C"));
			} else if (rsrpAverage >= -125 && rsrpAverage < -115) {
				geographyWrapper.setRsrpPlotColor(Color.decode("#FA6D3C"));
			} else if (rsrpAverage >= -115 && rsrpAverage < -110) {
				geographyWrapper.setRsrpPlotColor(Color.decode("#F6E936"));
			} else if (rsrpAverage >= -110 && rsrpAverage < -105) {
				geographyWrapper.setRsrpPlotColor(Color.decode("#5FE427"));
			} else if (rsrpAverage >= -105 && rsrpAverage < -80) {
				geographyWrapper.setRsrpPlotColor(Color.decode("#368706"));
			} else if (rsrpAverage >= -80 && rsrpAverage < -60) {
				geographyWrapper.setRsrpPlotColor(Color.decode("#0ED5FA"));
			} else if (rsrpAverage >= -60 && rsrpAverage <= -40) {
				geographyWrapper.setRsrpPlotColor(Color.decode("#3275FB"));
			}
		}
		if (dlAverage != null) {
			if (dlAverage >= 0 && dlAverage < 1) {
				geographyWrapper.setDlPlotColor(Color.decode("#FA403C"));
			} else if (dlAverage >= 1 && dlAverage < 2) {
				geographyWrapper.setDlPlotColor(Color.decode("#FA6D3C"));
			} else if (dlAverage >= 2 && dlAverage < 6) {
				geographyWrapper.setDlPlotColor(Color.decode("#F6E936"));
			} else if (dlAverage >= 6 && dlAverage < 10) {
				geographyWrapper.setDlPlotColor(Color.decode("#5FE427"));
			} else if (dlAverage >= 10 && dlAverage < 20) {
				geographyWrapper.setDlPlotColor(Color.decode("#368706"));
			} else if (dlAverage >= 20 && dlAverage < 30) {
				geographyWrapper.setDlPlotColor(Color.decode("#0ED5FA"));
			} else if (dlAverage >= 30 && dlAverage <= 200) {
				geographyWrapper.setDlPlotColor(Color.decode("#3275FB"));
			}
		}
	}

	private void setTopWorstGeographyDataList(List<StealthGeographyDataWrapper> geographyWiseDataWrapperList,
			SDGraphMapDataWrapper geographyDataWrapper) {
		List<StealthGeographyDataWrapper> filteredTopRsrpData = geographyWiseDataWrapperList.stream()
																							.filter(x ->
																									x.getRsrpCount()
																											!= null &&
																											x.getRsrpCount()
																													> 1000)
																							.collect(
																									Collectors.toList());
		Comparator<StealthGeographyDataWrapper> rsrpComparator = getBestWorstRSRPComparator();
		List<StealthGeographyDataWrapper> filteredBottomRsrpData = new ArrayList<>();
		filteredBottomRsrpData.addAll(filteredTopRsrpData);
		Collections.sort(filteredTopRsrpData, rsrpComparator);
		Collections.sort(filteredBottomRsrpData, rsrpComparator);
		Collections.reverse(filteredTopRsrpData);
		logger.info("RSRP Top List: {}",new Gson().toJson(filteredTopRsrpData));
		logger.info("RSRP bottom List: {}",new Gson().toJson(filteredBottomRsrpData));
		List<StealthGeographyDataWrapper> topRsrpData = new ArrayList<>();
		if (Utils.isValidList(filteredTopRsrpData) && filteredTopRsrpData.size() > 5) {
			topRsrpData.addAll(filteredTopRsrpData.subList(0, 5));
		} else if (Utils.isValidList(filteredTopRsrpData)) {
			topRsrpData.addAll(filteredTopRsrpData.subList(0, filteredTopRsrpData.size()));
		}
		List<StealthGeographyDataWrapper> bottomRsrpData = new ArrayList<>();
		if (Utils.isValidList(filteredBottomRsrpData) && filteredBottomRsrpData.size() > 5) {
			bottomRsrpData.addAll(filteredBottomRsrpData.subList(0, 5));
		} else if (Utils.isValidList(filteredBottomRsrpData)) {
			bottomRsrpData.addAll(filteredBottomRsrpData.subList(0, filteredBottomRsrpData.size()));
		}
		double rsrpTotalSample = filteredTopRsrpData.stream().mapToDouble(x -> x.getRsrpCount()).sum();
		geographyDataWrapper.setTopCoverageList(
				addKpiDataToWrapper(topRsrpData, ReportConstants.RSRP, rsrpTotalSample));
		geographyDataWrapper.setWorstCoverageList(
				addKpiDataToWrapper(bottomRsrpData, ReportConstants.RSRP, rsrpTotalSample));

		geographyWiseDataWrapperList.forEach(x -> logger.info("Total dl samples in geography: {} is: {}", x.getGeographyName(), x.getDlCount()));

		List<StealthGeographyDataWrapper> filteredTopDlData = geographyWiseDataWrapperList.stream()
																						  .filter(x ->
																								  x.getDlCount() != null
																										  &&
																										  x.getDlCount()
																												  > 1000)
																						  .collect(Collectors.toList());
		Comparator<StealthGeographyDataWrapper> dlComparator = getBestWorstDlComparator();
		List<StealthGeographyDataWrapper> filteredBottomDlData = new ArrayList<>();
		filteredBottomDlData.addAll(filteredTopDlData);
		Collections.sort(filteredTopDlData, dlComparator);
		Collections.sort(filteredBottomDlData, dlComparator);
		Collections.reverse(filteredTopDlData);
		List<StealthGeographyDataWrapper> topDlData = new ArrayList<>();
		if (Utils.isValidList(filteredTopDlData) && filteredTopDlData.size() > 5) {
			topDlData.addAll(filteredTopDlData.subList(0, 5));
		} else if (Utils.isValidList(filteredTopDlData)) {
			topDlData.addAll(filteredTopDlData.subList(0, filteredTopDlData.size()));
		}
		List<StealthGeographyDataWrapper> bottomDlData = new ArrayList<>();
		if (Utils.isValidList(filteredBottomDlData) && filteredBottomDlData.size() > 5) {
			bottomDlData.addAll(filteredBottomDlData.subList(0, 5));
		} else if (Utils.isValidList(filteredBottomDlData)) {
			bottomDlData.addAll(filteredBottomDlData.subList(0, filteredBottomDlData.size()));
		}
		double dlTotalSample = filteredTopDlData.stream().mapToDouble(x -> x.getDlCount()).sum();
		geographyDataWrapper.setTopDlList(addKpiDataToWrapper(topDlData, ReportConstants.DL, dlTotalSample));
		geographyDataWrapper.setWorstDlList(addKpiDataToWrapper(bottomDlData, ReportConstants.DL, dlTotalSample));

		List<StealthGeographyDataWrapper> filteredTopNoCoverageData = geographyWiseDataWrapperList.stream()
																								  .filter(x ->
																										  x.getNocoverageCount()
																												  != null
																												  &&
																												  x.getNocoverageCount()
																														  > 1000)
																								  .collect(
																										  Collectors.toList());
		Comparator<StealthGeographyDataWrapper> noCoverageComparator = getBestWorstNoCoverageComparator();
		Collections.sort(filteredTopNoCoverageData, noCoverageComparator);
		Collections.reverse(filteredTopNoCoverageData);
		List<StealthGeographyDataWrapper> topNoCoverageData = new ArrayList<>();
		if (Utils.isValidList(filteredTopNoCoverageData) && filteredTopNoCoverageData.size() > 5) {
			topNoCoverageData.addAll(filteredTopNoCoverageData.subList(0, 5));
		} else if (Utils.isValidList(filteredTopNoCoverageData)) {
			topNoCoverageData.addAll(filteredTopNoCoverageData.subList(0, filteredTopNoCoverageData.size()));
		}
		double noCoverageTotalSample = filteredTopNoCoverageData.stream()
																.mapToDouble(x -> x.getNocoverageCount())
																.sum();
		geographyDataWrapper.setCoverageNCList(
				addKpiDataToWrapper(topNoCoverageData, ReportConstants.NO, noCoverageTotalSample));
	}

	private List<SDGeographyDataWrapper> addKpiDataToWrapper(List<StealthGeographyDataWrapper> kpiData, String kpi,
			Double totalSamples) {
		logger.info("kpi Data is: {}", new Gson().toJson(kpiData));
		List<SDGeographyDataWrapper> dataList = new ArrayList<>();
		for (StealthGeographyDataWrapper wrapper : kpiData) {
			SDGeographyDataWrapper dataWrapper = new SDGeographyDataWrapper();
			dataWrapper.setGeographyName(wrapper.getGeographyName());
			if (kpi.equalsIgnoreCase(ReportConstants.RSRP)) {
				dataWrapper.setTotalSample(wrapper.getRsrpCount().longValue() + Symbol.EMPTY_STRING);
				dataWrapper.setAvgValue(ReportUtil.round((wrapper.getRsrpSum() / wrapper.getRsrpCount()),
						ReportConstants.TWO_DECIMAL_PLACES).toString());
				dataWrapper.setPercentage(
						ReportUtil.round(ReportUtil.getPercentage(wrapper.getRsrpCount(), totalSamples),
								ReportConstants.TWO_DECIMAL_PLACES).toString() + Symbol.SPACE_STRING + Symbol.PERCENT_STRING);
			} else if (kpi.equalsIgnoreCase(ReportConstants.DL)) {
				dataWrapper.setTotalSample(wrapper.getDlCount().longValue() + Symbol.EMPTY_STRING);
				dataWrapper.setAvgValue(ReportUtil.round((wrapper.getDlSum() / wrapper.getDlCount()),
						ReportConstants.TWO_DECIMAL_PLACES).toString());
				dataWrapper.setPercentage(ReportUtil.round(ReportUtil.getPercentage(wrapper.getDlCount(), totalSamples),
						ReportConstants.TWO_DECIMAL_PLACES).toString() + Symbol.SPACE_STRING + Symbol.PERCENT_STRING);
			} else if (kpi.equalsIgnoreCase(ReportConstants.NO)) {
				dataWrapper.setTotalSample(wrapper.getNocoverageCount().longValue() + Symbol.EMPTY_STRING);
				dataWrapper.setPercentage(
						ReportUtil.round(ReportUtil.getPercentage(wrapper.getNocoverageCount(), totalSamples),
								ReportConstants.TWO_DECIMAL_PLACES).toString() + Symbol.SPACE_STRING + Symbol.PERCENT_STRING);
			}

			dataList.add(dataWrapper);
		}
		return dataList;
	}

	private Comparator<StealthGeographyDataWrapper> getBestWorstRSRPComparator() {
		return new Comparator<StealthGeographyDataWrapper>() {
			@Override
			public int compare(StealthGeographyDataWrapper o1, StealthGeographyDataWrapper o2) {
				if (o1 != null && o1.getAvgRSRP() != null && o2 != null && o2.getAvgRSRP() != null) {
					return o1.getAvgRSRP().compareTo(o2.getAvgRSRP());
				} else {
					return -1;
				}
			}
		};
	}

	private Comparator<String> getDateStringComparator() {
		return new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				if (!StringUtils.isBlank(o1) && !StringUtils.isBlank(o2)) {
					SimpleDateFormat df = new SimpleDateFormat("ddMMyy");
					try {
						Date d1 = df.parse(o1);
						Date d2 = df.parse(o2);
						return d1.compareTo(d2);
					} catch (ParseException e) {
						logger.error("Error while comparing date: {}", Utils.getStackTrace(e));
					}
				}
				return -1;
			}
		};
	}

	private Comparator<StealthGeographyDataWrapper> getBestWorstDlComparator() {
		return new Comparator<StealthGeographyDataWrapper>() {
			@Override
			public int compare(StealthGeographyDataWrapper o1, StealthGeographyDataWrapper o2) {
				if (o1 != null && o1.getAvgDl() != null && o2 != null && o2.getAvgDl() != null) {
					return o1.getAvgDl().compareTo(o2.getAvgDl());
				} else {
					return -1;
				}
			}
		};
	}

	private Comparator<StealthGeographyDataWrapper> getBestWorstNoCoverageComparator() {
		return new Comparator<StealthGeographyDataWrapper>() {
			@Override
			public int compare(StealthGeographyDataWrapper o1, StealthGeographyDataWrapper o2) {
				if (o1 != null && o1.getNocoverageCount() != null && o2 != null && o2.getNocoverageCount() != null) {
					return o1.getNocoverageCount().compareTo(o2.getNocoverageCount());
				} else {
					return -1;
				}
			}
		};
	}

	private void addKpiDataToGeographyWrapper(StealthGeographyDataWrapper geographyWrapper,
			List<HBaseResult> dataList) {
		double[] rsrpSumCount = new double[2];
		double[] sinrSumCount = new double[2];
		double[] dlSumCount = new double[2];
		double[] ulSumCount = new double[2];
		Long noCoverageCount = 0l;
		for (HBaseResult result : dataList) {
			Map<String, Object> rsrpJson = new Gson().fromJson(result.getString("rsrpJson"), HashMap.class);
			if (rsrpJson != null) {
				Double[] rsrpSC = getSumCountForKpi(rsrpJson);
				if (rsrpSC != null && rsrpSC[0] != null && rsrpSC[1] != null) {
					rsrpSumCount[0] += rsrpSC[0];
					rsrpSumCount[1] += rsrpSC[1];
				}
			}

			Map<String, Object> sinrJson = new Gson().fromJson(result.getString("sinrJson"), HashMap.class);
			if (sinrJson != null) {
				Double[] sinrSC = getSumCountForKpi(sinrJson);
				if (sinrSC != null && sinrSC[0] != null && sinrSC[1] != null) {
					sinrSumCount[0] += sinrSC[0];
					sinrSumCount[1] += sinrSC[1];
				}
			}

			Map<String, Object> dlJson = new Gson().fromJson(result.getString("dlJson"), HashMap.class);
			if (dlJson != null) {
				Double[] dlSC = getSumCountForKpi(dlJson);
				if (dlSC != null && dlSC[0] != null && dlSC[1] != null) {
					dlSumCount[0] += dlSC[0];
					dlSumCount[1] += dlSC[1];
				}
			}

			Map<String, Object> ulJson = new Gson().fromJson(result.getString("ulJson"), HashMap.class);
			if (ulJson != null) {
				Double[] ulSC = getSumCountForKpi(ulJson);
				if (ulSC != null && ulSC[0] != null && ulSC[1] != null) {
					ulSumCount[0] += ulSC[0];
					ulSumCount[1] += ulSC[1];
				}
			}
			String noCovData = result.getString(Bytes.toBytes("noCov"));
			logger.info("Got No coverage data {}", noCovData);
			if (noCovData != null && NumberUtils.isParsable(noCovData)) {
				Long noCovCount = Long.parseLong(noCovData);
				noCoverageCount += noCovCount;
			}
		}
		geographyWrapper.setRsrpSum(rsrpSumCount[0]);
		geographyWrapper.setRsrpCount(rsrpSumCount[1]);
		geographyWrapper.setSinrSum(sinrSumCount[0]);
		geographyWrapper.setSinrCount(sinrSumCount[1]);
		geographyWrapper.setDlSum(dlSumCount[0]);
		geographyWrapper.setDlCount(dlSumCount[1]);
		geographyWrapper.setUlSum(ulSumCount[0]);
		geographyWrapper.setUlCount(ulSumCount[1]);
		geographyWrapper.setNocoverageCount(noCoverageCount.doubleValue());
	}

	private Double[] getSumCountForKpi(Map<String, Object> json) {
		Double[] sumCountArray = new Double[2];
		if (json.containsKey("count")) {
			Double totalRsrpCount = (Double) json.get("count");
			sumCountArray[1] = totalRsrpCount;
			Double totalRsrpSum = (Double) json.get("sum");
			sumCountArray[0] = totalRsrpSum;
		}
		return sumCountArray;
	}

	private List<List<List<List<Double>>>> getBoundaryData(String name) throws IOException {
		String boundaryData = getL4BoundaryByName(name);
		List<List<List<Double>>> boundarie = null;
		List<List<List<List<Double>>>> boundaries = new ArrayList<>();
		if (boundaryData != null) {
			boundarie = new ObjectMapper().readValue(boundaryData, new TypeReference<List<List<List<Double>>>>() {
			});
			logger.info("boundaries{}", boundarie.toString());
			boundaries.add(boundarie);
		}
		return boundaries;
	}

	private String getL4BoundaryByName(String name) {
		List<Map<String, String>> boundarMap;
		boundarMap = iGenericMapService.getBoundaryDataByGeographyNamesMS(Arrays.asList(name),
				GenericMapUtils.GEOGRAPHY_TABLE_NAME, GenericMapUtils.getColumnListForQuery(), null,
				GenericMapUtils.L4_TYPE);
		if (boundarMap != null && !boundarMap.isEmpty()) {
			return boundarMap.get(ReportConstants.INDEX_ZER0).get(GenericMapUtils.COORDINATES);
		}
		return null;
	}

	private void addGraphDataToMainWrapper(StealthDashboardReportWrapper mainWrapper,
			Map<String, Map<String, List<HBaseResult>>> dataMapForStealthDashboard, List<String> dateList) {
		try {
			Map<String, List<SDChartDataWrapper>> dateWiseDataWrapperMap = new HashMap<>();
			int geographyCounter = 1;
			StringBuilder geographyNamesBuilder = new StringBuilder();
			for (Map.Entry<String, Map<String, List<HBaseResult>>> dataEntry : dataMapForStealthDashboard.entrySet()) {
				TreeMap<String, List<HBaseResult>> dateWiseDataForGeography = getDateWiseDataForGeography(
						dataEntry.getValue());
				logger.info("Date wise data: {}", new Gson().toJson(dateWiseDataForGeography));
				for (String date : dateList) {
					List<HBaseResult> dataList = dateWiseDataForGeography.get(date);
					SDChartDataWrapper totalSampleData;
					SDChartDataWrapper noCoverageSampleData;
					SDChartDataWrapper rsrpCriteriaData;
					SDChartDataWrapper dlCriteriaData;
					if (dateWiseDataWrapperMap.containsKey(date)) {
						totalSampleData = dateWiseDataWrapperMap.get(date).get(0);
						noCoverageSampleData = dateWiseDataWrapperMap.get(date).get(1);
						rsrpCriteriaData = dateWiseDataWrapperMap.get(date).get(2);
						dlCriteriaData = dateWiseDataWrapperMap.get(date).get(3);
					} else {
						totalSampleData = new SDChartDataWrapper();
						noCoverageSampleData = new SDChartDataWrapper();
						rsrpCriteriaData = new SDChartDataWrapper();
						dlCriteriaData = new SDChartDataWrapper();
					}
					Double[] doubles = calculateSampleCounts(dataList);
					Double totalSamples = doubles[0];
					Double noCoverageSamples = doubles[1];
					Double rsrpCriteriaSamples = doubles[2];
					Double dlCriteriaSamples = doubles[3];
					String formattedDate = ReportUtil.getFormattedDate(
							ReportUtil.getDateFromString(date, "ddMMyy"), "dd-MMM");
					totalSampleData.setDate(formattedDate);
					noCoverageSampleData.setDate(formattedDate);
					rsrpCriteriaData.setDate(formattedDate);
					dlCriteriaData.setDate(formattedDate);
					addGraphDataToWrapper(totalSamples, totalSampleData, geographyCounter, dataEntry.getKey());
					addGraphDataToWrapper(noCoverageSamples, noCoverageSampleData, geographyCounter,
							dataEntry.getKey());
					addGraphDataToWrapper(rsrpCriteriaSamples, rsrpCriteriaData, geographyCounter, dataEntry.getKey());
					addGraphDataToWrapper(dlCriteriaSamples, dlCriteriaData, geographyCounter, dataEntry.getKey());
					List<SDChartDataWrapper> chartDataWrapperList = new ArrayList<>();
					chartDataWrapperList.add(0, totalSampleData);
					chartDataWrapperList.add(1, noCoverageSampleData);
					chartDataWrapperList.add(2, rsrpCriteriaData);
					chartDataWrapperList.add(3, dlCriteriaData);
					if (dateWiseDataWrapperMap.containsKey(date)) {
						dateWiseDataWrapperMap.replace(date, chartDataWrapperList);
					} else {
						dateWiseDataWrapperMap.put(date, chartDataWrapperList);
					}
				}
				geographyNamesBuilder.append(dataEntry.getKey() + Symbol.COMMA_STRING + Symbol.SPACE_STRING);
				geographyCounter++;
			}
			List<SDChartDataWrapper> totalSampleDataList = new ArrayList<>();
			List<SDChartDataWrapper> noCoverageDataList = new ArrayList<>();
			List<SDChartDataWrapper> rsrpCriteriaDataList = new ArrayList<>();
			List<SDChartDataWrapper> dlCriteriaDataList = new ArrayList<>();

			for (Map.Entry<String, List<SDChartDataWrapper>> dateWiseEntry : dateWiseDataWrapperMap.entrySet()) {
				SDChartDataWrapper totalSampleWrapper = dateWiseEntry.getValue().get(0);
				addTotalSamplesToWrapper(totalSampleWrapper);
				totalSampleDataList.add(totalSampleWrapper);
				SDChartDataWrapper ncSampleWrapper = dateWiseEntry.getValue().get(1);
				addTotalSamplesToWrapper(ncSampleWrapper);
				noCoverageDataList.add(ncSampleWrapper);
				SDChartDataWrapper rsrpSampleWrapper = dateWiseEntry.getValue().get(2);
				addTotalSamplesToWrapper(rsrpSampleWrapper);
				rsrpCriteriaDataList.add(rsrpSampleWrapper);
				SDChartDataWrapper dlSampleWrapper = dateWiseEntry.getValue().get(3);
				addTotalSamplesToWrapper(dlSampleWrapper);
				dlCriteriaDataList.add(dlSampleWrapper);
			}

			SDGraphMapDataWrapper graphDataWrapper = new SDGraphMapDataWrapper();
			sortListByDate(totalSampleDataList);
			sortListByDate(noCoverageDataList);
			String geographyNames = geographyNamesBuilder.toString();
			geographyNames = geographyNames.replaceAll(", $", Symbol.EMPTY_STRING);
			graphDataWrapper.setGeographyNames(geographyNames);
			graphDataWrapper.setChart1List(totalSampleDataList);
			graphDataWrapper.setChart2List(noCoverageDataList);
			logger.info("Graph data Wrapper is: {}", new Gson().toJson(graphDataWrapper));
			mainWrapper.setGraphDataList(Arrays.asList(graphDataWrapper));

			SDGraphMapDataWrapper criteriaGraphDataWrapper = new SDGraphMapDataWrapper();
			sortListByDate(rsrpCriteriaDataList);
			sortListByDate(dlCriteriaDataList);
			criteriaGraphDataWrapper.setGeographyNames(geographyNames);
			criteriaGraphDataWrapper.setChart1List(rsrpCriteriaDataList);
			criteriaGraphDataWrapper.setChart2List(dlCriteriaDataList);
			logger.info("Criteria Graph data Wrapper is: {}", new Gson().toJson(criteriaGraphDataWrapper));
			mainWrapper.setCriteriaGraphDataList(Arrays.asList(criteriaGraphDataWrapper));

		} catch (Exception e) {
			logger.error("Error while calculating graph data: {}", Utils.getStackTrace(e));
		}
	}

	private void addTotalSamplesToWrapper(SDChartDataWrapper totalSampleWrapper) {
		Double totalCount = 0.0;
		if(totalSampleWrapper.getGeography1Line() != null){
			totalCount += totalSampleWrapper.getGeography1Line();
		}
		if(totalSampleWrapper.getGeography2Line() != null){
			totalCount += totalSampleWrapper.getGeography2Line();
		}
		if(totalSampleWrapper.getGeography3Line() != null){
			totalCount += totalSampleWrapper.getGeography3Line();
		}
		totalSampleWrapper.setTotalLineCount(totalCount);
	}

	private void sortListByDate(List<SDChartDataWrapper> dataList) {
		Comparator<SDChartDataWrapper> dateComparator = getDateComparator();
		Collections.sort(dataList, dateComparator);
	}

	private Comparator<SDChartDataWrapper> getDateComparator() {
		return new Comparator<SDChartDataWrapper>() {
			@Override
			public int compare(SDChartDataWrapper o1, SDChartDataWrapper o2) {
				if (o1 != null && o1.getDate() != null && o2 != null && o2.getDate() != null) {
					SimpleDateFormat df = new SimpleDateFormat("dd-MMM");
					try {
						Date d1 = df.parse(o1.getDate());
						Date d2 = df.parse(o2.getDate());
						return d1.compareTo(d2);
					} catch (ParseException e) {
						logger.error("Error while comparing date: {}", Utils.getStackTrace(e));
					}
				}
				return -1;
			}
		};
	}

	private void addGraphDataToWrapper(Double totalSamples, SDChartDataWrapper totalSampleData, int geographyCounter,
			String geographyName) {
		switch (geographyCounter) {
		case 1:
			totalSampleData.setGeography1(geographyName);
			if (totalSamples != null) {
				totalSampleData.setGeography1Bar(totalSamples);
				Double samplesInMillions = ReportUtil.round(Double.valueOf(totalSamples / 1000000),
						ReportConstants.TWO_DECIMAL_PLACES);
				totalSampleData.setGeography1Line(samplesInMillions != null ? samplesInMillions : 0.0);
			}
			break;
		case 2:
			totalSampleData.setGeography2(geographyName);
			if (totalSamples != null) {
				totalSampleData.setGeography2Bar(totalSamples);
				Double samplesInMillions = ReportUtil.round(Double.valueOf(totalSamples / 1000000),
						ReportConstants.TWO_DECIMAL_PLACES);
				totalSampleData.setGeography2Line(samplesInMillions != null ? samplesInMillions : 0.0);
			}
			break;
		case 3:
			totalSampleData.setGeography3(geographyName);
			if (totalSamples != null) {
				totalSampleData.setGeography3Bar(totalSamples);
				Double samplesInMillions = ReportUtil.round(Double.valueOf(totalSamples / 1000000),
						ReportConstants.TWO_DECIMAL_PLACES);
				totalSampleData.setGeography3Line(samplesInMillions != null ? samplesInMillions : 0.0);
			}
			break;
		}
	}

	private Double[] calculateSampleCounts(List<HBaseResult> dataList) {
		logger.info("Hbase result list size: {}", dataList != null ? dataList.size() : "List is Empty");
		Double[] data = new Double[4];
		if(Utils.isValidList(dataList)) {
			Double totalSamples = 0.0;
			Double rsrpCriteriaSamples = 0.0;
			Double noCoverageSamples = 0.0;
			Double dlCriteriaSamples = 0.0;
			for (HBaseResult result : dataList) {
				Map<String, Object> rsrpJson = new Gson().fromJson(result.getString("rsrpJson"), HashMap.class);
				if (rsrpJson != null && rsrpJson.containsKey("count")) {
					Double totalRsrpCount = (Double) rsrpJson.get("count");
					logger.info("Rsrp Count is: {}", totalRsrpCount);
					totalSamples += totalRsrpCount;
					logger.info("TOTAL Count is: {}", totalSamples);
				}
				if (rsrpJson != null && rsrpJson.containsKey("kpi")) {
					Map<String, Object> kpiMap = (Map<String, Object>) rsrpJson.get("kpi");
					logger.info("KPI in Rsrp Json is: {}", new Gson().toJson(kpiMap));
					if (kpiMap != null) {
						if (kpiMap != null && kpiMap.containsKey("-140 to -125") && kpiMap.containsKey("-125 to -115")) {
							Double criteriaRsrpCount = (Double) kpiMap.get("-140 to -125");
							rsrpCriteriaSamples += criteriaRsrpCount;
							criteriaRsrpCount = (Double) kpiMap.get("-125 to -115");
							rsrpCriteriaSamples += criteriaRsrpCount;
						}
					}
				}
				String noCovData = result.getString(Bytes.toBytes("noCov"));
				if (noCovData != null && NumberUtils.isParsable(noCovData)) {
					Long noCovCount = Long.parseLong(noCovData);
					totalSamples += noCovCount;
					noCoverageSamples += noCovCount;
				}

				Map<String, Object> dlJson = new Gson().fromJson(result.getString("dlJson"), HashMap.class);
				if (dlJson != null && dlJson.containsKey("kpi")) {
					Map<String, Object> kpiMap = (Map<String, Object>) dlJson.get("kpi");
					logger.info("KPI in DL Json is: {}", new Gson().toJson(kpiMap));
					if (kpiMap != null) {
						if (kpiMap != null && kpiMap.containsKey("0 to 1")) {
							Double criteriaDlCount = (Double) kpiMap.get("0 to 1");
							dlCriteriaSamples += criteriaDlCount;
						}
					}
				}
			}
			data[0] = totalSamples;
			data[1] = noCoverageSamples;
			data[2] = rsrpCriteriaSamples;
			data[3] = dlCriteriaSamples;
		}
		return data;
	}

	private TreeMap<String, List<HBaseResult>> getDateWiseDataForGeography(Map<String, List<HBaseResult>> l4WiseData) {
		Map<String, List<HBaseResult>> dateWiseData = new HashMap<>();
		if(l4WiseData != null && !l4WiseData.isEmpty()) {
			for (Map.Entry<String, List<HBaseResult>> l4WiseEntry : l4WiseData.entrySet()) {
				for (HBaseResult result : l4WiseEntry.getValue()) {
					String date = getDateFromHbaseEntry(result);
					logger.info("Date from Entry: {}", date);
					if (dateWiseData.containsKey(date)) {
						List<HBaseResult> hBaseResults = dateWiseData.get(date);
						hBaseResults.add(result);
					} else {
						List<HBaseResult> hBaseResults = new ArrayList<>();
						hBaseResults.add(result);
						dateWiseData.put(date, hBaseResults);
					}
				}
			}
		}
		TreeMap<String, List<HBaseResult>> dataMap = new TreeMap<>(getDateStringComparator());
		dataMap.putAll(dateWiseData);
		return dataMap;
	}

	private String getDateFromHbaseEntry(HBaseResult result) {
		String rowkey = result.getRowKey();
		return rowkey.substring(0, 6);
	}

	private Map<String, Map<String, List<HBaseResult>>> getDataMapForStealthDashboard(
			Map<String, List<GeographyL4>> geographyL4Map, List<String> dateList) {
		Map<String, Map<String, List<HBaseResult>>> geographyWiseDataMap = new HashMap<>();
		if (Utils.isValidList(dateList)) {
			logger.info("Date list is: {}", new Gson().toJson(dateList));
			for (Map.Entry<String, List<GeographyL4>> geographyEntry : geographyL4Map.entrySet()) {
				List<GeographyL4> geographyL4List = geographyEntry.getValue();
				Map<String, List<HBaseResult>> l4WiseDataMap = new HashMap<>();
				for (GeographyL4 geographyL4 : geographyL4List) {
					List<String> prefixList = getPrefixForReport(geographyL4.getId(), dateList);
					logger.info("prefix list is: {}", new Gson().toJson(prefixList));
					List<HBaseResult> list = stealthTaskHbaseDao.getResultListForPreFixList(getColumnListForReport(),
							"StealthDashboard", prefixList);
					logger.info("Hbase result is: {}", new Gson().toJson(list));
					if (Utils.isValidList(list)) {
						l4WiseDataMap.put(geographyL4.getDisplayName(), list);
					}
				}
				geographyWiseDataMap.put(geographyEntry.getKey(), l4WiseDataMap);
			}
		}
		return geographyWiseDataMap;
	}

	public static List<String> getColumnListForReport() {
		List<String> columnList = new ArrayList<>();
		columnList.add("r:rsrpJson");
		columnList.add("r:sinrJson");
		columnList.add("r:ulJson");
		columnList.add("r:dlJson");
		columnList.add("r:noCov");
		return columnList;
	}

	private List<String> getPrefixForReport(Integer l4Id, List<String> dateList) {
		List<String> preFixList = new ArrayList<>();
		for (String date : dateList) {
			preFixList.add(date + StringUtils.reverse(StringUtils.leftPad(String.valueOf(l4Id), 7, "0")));
		}
		return preFixList;
	}

	private Map<String, List<GeographyL4>> getGeographyL4CorrespondingToGeography(List<Geography> geographyList) {
		Map<String, List<GeographyL4>> geographyMap = new HashMap<>();
		for (Geography geography : geographyList) {
			String geographyType = geography.getGeographyType();
			if (geographyType.equalsIgnoreCase(ForesightConstants.GEOGRAPHY_L2)) {
				List<GeographyL4> l4byL2Id = geographyL4Dao.getL4byL2Id(geography.getId());
				geographyMap.put(geography.getName(), l4byL2Id);
			} else if (geographyType.equalsIgnoreCase(ForesightConstants.GEOGRAPHY_L3)) {
				List<GeographyL4> l4byL3Name = geographyL4Dao.getL4ByL3Name(geography.getName());
				geographyMap.put(geography.getName(), l4byL3Name);
			} else if (geographyType.equalsIgnoreCase(ForesightConstants.GEOGRAPHY_L4)) {
				List<GeographyL4> geographyL4ListByIds = geographyL4Dao.getGeographyL4ListByIds(
						Arrays.asList(geography.getId()));
				geographyMap.put(geography.getName(), geographyL4ListByIds);
			}
		}
		return geographyMap;
	}

	public List<Geography> getGeographyList(GeographyParser geographyParser) {
		try {
			if (geographyParser != null) {
				if (geographyParser.getGeographyL4() != null && !geographyParser.getGeographyL4().isEmpty()) {
					geographyParser.getGeographyL4()
								   .forEach(geography -> geography.setGeographyType(ForesightConstants.GEOGRAPHY_L4));
					return geographyParser.getGeographyL4();
				} else if (geographyParser.getGeographyL3() != null && !geographyParser.getGeographyL3().isEmpty()) {
					geographyParser.getGeographyL3()
								   .forEach(geography -> geography.setGeographyType(ForesightConstants.GEOGRAPHY_L3));
					return geographyParser.getGeographyL3();
				} else if (geographyParser.getGeographyL2() != null && !geographyParser.getGeographyL2().isEmpty()) {
					geographyParser.getGeographyL2()
								   .forEach(geography -> geography.setGeographyType(ForesightConstants.GEOGRAPHY_L2));
					return geographyParser.getGeographyL2();
				} else if (geographyParser.getGeographyL1() != null && !geographyParser.getGeographyL1().isEmpty()) {
					geographyParser.getGeographyL1()
								   .forEach(geography -> geography.setGeographyType(ForesightConstants.GEOGRAPHY_L1));
					return geographyParser.getGeographyL1();
				}
			}
		} catch (Exception e) {
			logger.error("Exception in finding the getGeography Object {} ", Utils.getStackTrace(e));
		}
		return null;
	}

	private Date getStartDateFromEndTimestamp(Long timestamp) {
		if (timestamp != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date(timestamp));
			calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 6);
			return calendar.getTime();
		}
		return null;
	}

	public static void main(String[] args) {
//		System.out.println(AESUtils.decrypt("fBwhDMTn5D1F\\/LaU+rH2G8Qpauqc\\/9XhNuzkXULa0tBACr+fI1ljSk7uFpB6fksuS0\\/\\/fS\\/7wKgzxT74rlmIAw+hOv+UIMJ88Mv66DPHiVaUp\\/iH1YDGcUA8I7WiQmMyAYFIPVnP\\/2rijLJrHB9kYcjV3BMbhjf4ErE6rKEfn4v8ptxnO8W1EE4EIHs5\\/vN8cMHFRHzRVrZjs9x\\/Fyg5MpbUhPIz+tl0luqL7KqCcFD7VdBC2GVrEY09vFABhT86Q2i52IGVH13bj+oqHEzZK2lqZ00gVjoV3fLVgu9yMSqQoGiUXKxT9\\/hIBoSTcij\\/1NgDmmkL7KQsMEZTTQrACARzC653viZtJ7vEZZLcbTiK5y\\/u3wOGW+\\/Q8PrGUYAWDpzoX0HxuxCYDFPOWvHHD3RVqyS1dKfXC4foU21PLUCHrQVB7QiZWLXhT5Eg4hAFntXIsZ7nR6cfX0tyknq2G6owRCbtzq64NXDuC62rRn8hVDhyZU7HeWTPTL0QudZPvmFj7bGdksW8IOcOaMqYTaVPA9ephdAj+IT74ggyYAzZWr8NBmCVtsRl8nvgqj9yVuzE4fbITyNMNACidG1897YJ3Z4wkDVxKDHisxBnbUYvc2D+1DurqsPueH\\/xAXb0vF0awyKz5\\/4s0Zy3KkAoc8htgZr9UT29QFgx6Rlcv7Bwbjs30qPK\\/1Dj+AN+QmGX5462\\/aWlomJUML2+suH1q1qjtreaYUgkfDiIlgTQxf9gNlvnrGY2pq7EjlRhajoMH4q7WtsL4ahanHkX8\\/m0IOGUistwPJKTdZrHMy3cXrxgCr4r3sFMzL7LPQfNojifPc1cd2TbouVvdM731j2SAtmIgpNaxRr6BhTHtLmEyrrAdYmgRXLUTJj5QeCFU+ToCSxgFiKoD97\\/Wri\\/naAwxwjSYtvTn1CWtor\\/Lr+2uWG4rAu2+MisSMLZZUl80SN+Pc7vCYoODkZjMiMz9TlWIEEaplBx+Qgnvlm024fGruicA4HAkP8rLBzlJxUg\\/+bpkd\\/Mr9xyRfTs63Jo+bxZmBnYyR\\/vAMGLSQ0Yma4omdxTc8hqn2sNSRM0SMTalbzzMjAw+82zKnUrIE1kMHRn2F\\/ZRVm5iQ7fs0NEkfJpnZF3Y9JW3GfF8Vb0w8ahEql7MEeYWwJ3yyF68S0ssW2B9hsuMDvM3FFkAKdeExSMJoFpfUTMEijLkT6kkuS3bVBMsC7ObbqEuHtsxzroFVeO2E1BG615P94kzLLp460yacTlnbtwEH40PCQNXv2BrCcJ8g\\/7awmgpuCNOqn7YMA28cSVOGGYq\\/a+6CnHuwX2WiRuh1fzjLyYi4FOcV7ZFsdf1FAd4ohCJPSkrd\\/7N4mGjhx7ynvKmkj40Xp1ouHFwtsK5e8vkcQ3I4AiOKtlyyynUrBKb+uAZuwMeGiM2\\/3LK28LXAkzq5A6fhaRYvnaZGBKBWm0jKehur6SZ3RP54PdmbtznrZSHuurwQzfyH1T2Gy0DxWw5u\\/ySgUhr2pp\\/fh95gxfpwTAKQm4PhQj7l7EvPE\\/opcSp1uF1Ac\\/xewzTzDUIvu1fn+\\/ckGAmnHGh94credIZRhN64pgIfplOr5Eu9VaSm1iYy2iWSm9IgSFe+tceL8B9hUjZGaYPC4YClKUpWEzcIqPzc9ob\\/KStmCRtDmEdSdi28dFMGHlgceY5a8fJ3SDuhzlFlZGCb2M\\/WwFUhJ1EHkPOVKVTTN5wOGm+oB30TARoJDNW3P8NL6xe8qw0SpTzvrHRae2Quxrq+PQkSDOnK6I5871IE3k9\\/WGI2lGz2PhnCEPR6hycuqyijLaKWve0w\\/JYHE8KiROpcqb3JC3kLATGYGcM33zDtD7UtOEMvrb6LUiFqkMuGSAUQ=="));
//		TreeMap<String, List<HBaseResult>> dataMap = new TreeMap<>(getDateStringComparator());
//
//		dataMap.put("011219", null);
//		dataMap.put("031219", null);
//		dataMap.put("021219", null);
//		dataMap.put("281119", null);
//		dataMap.put("291119", null);
//		dataMap.put("301119", null);
//		dataMap.put("271119", null);
//
//		System.out.println(dataMap);

	}

}

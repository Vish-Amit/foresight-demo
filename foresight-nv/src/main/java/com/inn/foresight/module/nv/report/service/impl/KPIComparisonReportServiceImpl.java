package com.inn.foresight.module.nv.report.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.io.image.ImageUtils;
import com.inn.commons.maps.LatLng;
import com.inn.commons.maps.tiles.Tile;
import com.inn.commons.maps.tiles.TileUtils;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.maplayer.service.IGenericMapService;
import com.inn.foresight.core.report.dao.IAnalyticsRepositoryDao;
import com.inn.foresight.core.report.model.AnalyticsRepository;
import com.inn.foresight.core.report.model.AnalyticsRepository.progress;
import com.inn.foresight.module.coverage.service.ICoverageService;
import com.inn.foresight.module.nv.core.workorder.dao.impl.IGenericWorkorderDao;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;
import com.inn.foresight.module.nv.layer3.constants.Layer3PPEConstant;
import com.inn.foresight.module.nv.layer3.constants.QMDLConstant;
import com.inn.foresight.module.nv.layer3.service.ILayer3PPEService;
import com.inn.foresight.module.nv.layer3.service.INVLayer3DashboardService;
import com.inn.foresight.module.nv.layer3.service.parse.INVL3ParsingService;
import com.inn.foresight.module.nv.report.constant.ReportIndexWrapper;
import com.inn.foresight.module.nv.report.service.IKPIComparsionReportService;
import com.inn.foresight.module.nv.report.service.IReportService;
import com.inn.foresight.module.nv.report.utils.DriveHeaderConstants;
import com.inn.foresight.module.nv.report.utils.ReportConstants;
import com.inn.foresight.module.nv.report.utils.ReportUtil;

import com.inn.foresight.module.nv.report.wrapper.kpicomparison.KPIComparisonDataWrapper;
import com.inn.foresight.module.nv.report.wrapper.kpicomparison.KPIComparisonInfoWrapper;
import com.inn.foresight.module.nv.report.wrapper.kpicomparison.KPIComparisonReportSubWrapper;
import com.inn.foresight.module.nv.report.wrapper.kpicomparison.KPIComparisonReportWrapper;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;
import com.inn.product.systemconfiguration.dao.SystemConfigurationDao;
import com.inn.product.systemconfiguration.model.SystemConfiguration;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * The Class APIComparisonReportServiceImpl.
 */
@Service("KPIComparisonReportServiceImpl")
public class KPIComparisonReportServiceImpl implements IKPIComparsionReportService {

	/** The logger. */
	private Logger logger = LogManager.getLogger(KPIComparisonReportServiceImpl.class);

	@Autowired
	private IGenericWorkorderDao iGenericWorkorderDao;

	@Autowired
	private INVL3ParsingService nvL3Parsing;

	@Autowired
	private IAnalyticsRepositoryDao analyticsrepositoryDao;
	@Autowired
	private INVLayer3DashboardService nvLayer3DashboardService;
	@Autowired
	private IReportService reportService;

	@Autowired
	private IGenericMapService iGenericMapService;

	@Autowired
	private SystemConfigurationDao iSystemConfigurationDao;
	
	@Autowired
	private ILayer3PPEService iLayer3PPEService;

	@Autowired
	private ICoverageService iCoverageService;

	public KPIComparisonReportServiceImpl() {
		super();
	}

	static Map<Integer, Double> loadColorMap;

	@Override
	@Transactional
	public Response execute(String json) {
		logger.info("Inside execute method to create KPIComparison Report with json {} ", json);
		Integer analyticsrepoId = null;
		try {
			Map<String, Object> jsonMap = reportService.getJsonDataMap(json);
			Integer workorderId = (Integer) jsonMap.get(ReportConstants.WORKORDER_ID);
			analyticsrepoId = (Integer) jsonMap.get(ReportConstants.ANALYTICS_REPOSITORY_ID);
			Integer recipeId = (Integer) jsonMap.get(ReportConstants.RECIPEID);
			boolean isFilesProcessed = reportService.getFileProcessedStatusForRecipeAndWorkorder(workorderId, recipeId);
			if (isFilesProcessed) {
				generateReport(jsonMap, analyticsrepoId, workorderId, recipeId);
			}
		} catch (IOException | ForbiddenException e) {
			logger.error("Error inside the method createReportForWorkOrderID for json {} , {} ", json,
					Utils.getStackTrace(e));
			analyticsrepositoryDao.updateStatusInAnalyticsRepository(analyticsrepoId, null, "Something Went Wrong",
					progress.Failed, null);
		} catch (Exception e1) {
			logger.error("Error nside the method createReportForWorkOrderID for json {} , {} ", json,
					Utils.getStackTrace(e1));
			analyticsrepositoryDao.updateStatusInAnalyticsRepository(analyticsrepoId, null, "Something Went Wrong",
					progress.Failed, null);
		}
		return Response.ok(ForesightConstants.FAILURE_JSON).build();
	}

	private Response generateReport(Map<String, Object> jsonDataMap, Integer analyticsrepoId, Integer workorderId, Integer recipeId)
			throws IOException {
		try {
			GenericWorkorder workorderObj = iGenericWorkorderDao.findByPk(workorderId);
//			String filePath = ConfigUtils.getString(ReportConstants.NV_REPORTS_PATH) + ReportConstants.KPICOMPARISON
//					+ ReportConstants.FORWARD_SLASH;
			AnalyticsRepository analyticsRepository = analyticsrepositoryDao.findByPk(analyticsrepoId);
			Map<String, List<String>> driveRecipeDetailMap = nvLayer3DashboardService
					.getDriveRecipeDetail(workorderObj.getId());
			if (driveRecipeDetailMap != null && !driveRecipeDetailMap.get(QMDLConstant.RECIPE).isEmpty()) {
				KPIComparisonReportWrapper mainWrapper = new KPIComparisonReportWrapper();
				List<KPIComparisonReportSubWrapper> subWrapperList = new ArrayList<>();

				Set<String> dynamicKpis = reportService.getDynamicKpiName(Arrays.asList(workorderId), null, Layer3PPEConstant.ADVANCE);
				List<String> fetchKPIList = ReportIndexWrapper.getLiveDriveKPIs().stream()
						.filter(k -> dynamicKpis.contains(k)).collect(Collectors.toList());

			    Map<String, Integer> kpiIndexMap = ReportIndexWrapper.getLiveDriveKPIIndexMap(fetchKPIList);
		
				List<String[]> csvDataArray = reportService.getDriveDataForReport(workorderId,Arrays.asList(recipeId.toString()),fetchKPIList);
				List<KPIComparisonInfoWrapper> kpiInfoList = getPlannedvsActualStats(csvDataArray,kpiIndexMap);
				List<KPIComparisonInfoWrapper> kpiGraphDataList = getGraphPredictionData(kpiInfoList);
				KPIComparisonReportSubWrapper graphSubWrapper = new KPIComparisonReportSubWrapper();
				graphSubWrapper.setKpiInforWrapperlist(kpiGraphDataList);
				KPIComparisonReportSubWrapper subWrapper = new KPIComparisonReportSubWrapper();
				subWrapper.setKpiInforWrapperlist(kpiInfoList);
				subWrapperList.add(0, graphSubWrapper);
				subWrapperList.add(1, subWrapper);
				mainWrapper.setSubWrapperList(subWrapperList);
				String filePath = proceedToCreateReport(mainWrapper, workorderObj, jsonDataMap, new HashMap<>());
				if (filePath != null) {
					Response response = saveFileToHdfsAndUpdateStatus(analyticsRepository, filePath);
					return response;
				} else {
					return Response.ok(ForesightConstants.FAILURE_JSON).build();
				}
			} else {
				analyticsrepositoryDao.updateStatusInAnalyticsRepository(analyticsrepoId, null, "Data is Not Available",
						progress.Failed, null);
			}

		} catch (Exception e) {
			logger.error("Exception In generateReport {} ", Utils.getStackTrace(e));
		}
		return Response.ok(ForesightConstants.FAILURE_JSON).build();
	}

	private List<KPIComparisonInfoWrapper> getGraphPredictionData(List<KPIComparisonInfoWrapper> kpiInfoList) {
		List<KPIComparisonInfoWrapper> kpiGraphDataList = new ArrayList<>();
		List<String> kpiRangeList = Arrays.asList(new String[]{"<-10", "-10 to -6", "-6 to -3", "-3 to 0", "0 to 3", "3 to 6", "6 to 10", ">=10", "N/A"});
		for (KPIComparisonInfoWrapper kpiInfo : kpiInfoList){
			KPIComparisonInfoWrapper kpiGraphWrapper = new KPIComparisonInfoWrapper();
			kpiGraphWrapper.setKpiName(kpiInfo.getKpiName());
			List<KPIComparisonDataWrapper> kpiDataWrapperList = new ArrayList<>();
			for (String range : kpiRangeList){
				KPIComparisonDataWrapper kpiDataWrapper = new KPIComparisonDataWrapper();
				kpiDataWrapper.setKpiTitle(kpiInfo.getKpiName() + " Difference (Prediction - Drive Test)");
				kpiDataWrapper.setKpiRange(!range.equalsIgnoreCase("N/A") ? range + " " + ReportUtil.getUnitByKPiName(kpiInfo.getKpiName()) : range);
				kpiDataWrapper.setKpiValue(calculateGraphDataForRange(kpiInfo.getKpiDataWrapperList(), range));
				kpiDataWrapperList.add(kpiDataWrapper);
			}
			kpiGraphWrapper.setKpiDataWrapperList(kpiDataWrapperList);
			kpiGraphDataList.add(kpiGraphWrapper);
		}
		return kpiGraphDataList;
	}

	private Double calculateGraphDataForRange(List<KPIComparisonDataWrapper> kpiDataList, String range){
		Integer totalCount = null;
		Integer criteriaCount = null;
		switch (range){
		case "<-10":
			totalCount = kpiDataList.size();
			criteriaCount = kpiDataList.stream().filter(x -> x != null && x.getDelta() != null && x.getDelta() < -10).collect(Collectors.toList()).size();
			break;
		case "-10 to -6":
			totalCount = kpiDataList.size();
			criteriaCount = kpiDataList.stream().filter(x -> x != null && x.getDelta() != null && x.getDelta() >= -10 && x.getDelta() < -6).collect(Collectors.toList()).size();
			break;
		case "-6 to -3":
			totalCount = kpiDataList.size();
			criteriaCount = kpiDataList.stream().filter(x -> x != null && x.getDelta() != null && x.getDelta() >= -6 && x.getDelta() < -3).collect(Collectors.toList()).size();
			break;
		case "-3 to 0":
			totalCount = kpiDataList.size();
			criteriaCount = kpiDataList.stream().filter(x -> x != null && x.getDelta() != null && x.getDelta() >= -3 && x.getDelta() < 0).collect(Collectors.toList()).size();
			break;
		case "0 to 3":
			totalCount = kpiDataList.size();
			criteriaCount = kpiDataList.stream().filter(x -> x != null && x.getDelta() != null && x.getDelta() >= 0 && x.getDelta() < 3).collect(Collectors.toList()).size();
			break;
		case "3 to 6":
			totalCount = kpiDataList.size();
			criteriaCount = kpiDataList.stream().filter(x -> x != null && x.getDelta() != null && x.getDelta() >= 3 && x.getDelta() < 6).collect(Collectors.toList()).size();
			break;
		case "6 to 10":
			totalCount = kpiDataList.size();
			criteriaCount = kpiDataList.stream().filter(x -> x != null && x.getDelta() != null && x.getDelta() >= 6 && x.getDelta() < 10).collect(Collectors.toList()).size();
			break;
		case ">=10":
			totalCount = kpiDataList.size();
			criteriaCount = kpiDataList.stream().filter(x -> x != null && x.getDelta() != null && x.getDelta() >= 10).collect(Collectors.toList()).size();
			break;
		case "N/A":
			totalCount = kpiDataList.size();
			criteriaCount = kpiDataList.stream().filter(x -> x != null && x.getDelta() == null).collect(Collectors.toList()).size();
			break;
		}
		return ReportUtil.getPercentage(criteriaCount, totalCount);
	}

	private List<KPIComparisonInfoWrapper> getPlannedvsActualStats(List<String[]> csvDataArray, Map<String, Integer> kpiIndexMap) {
		List<KPIComparisonInfoWrapper> kpiInfoList = new ArrayList<>();
		List<String> kpiList = Arrays.asList(new String[] { ReportConstants.RSRP, ReportConstants.RSRQ, ReportConstants.SINR });
		for (String kpi : kpiList) {
			KPIComparisonInfoWrapper kpiComparisonInfoWrapper = new KPIComparisonInfoWrapper();
			Map<String, BufferedImage> imageMap = new HashMap<>();
			List<KPIComparisonDataWrapper> kpiDataWrapperList = new ArrayList<>();
			kpiComparisonInfoWrapper.setKpiName(kpi);
			for (String[] dataPoint : csvDataArray) {
				KPIComparisonDataWrapper kpiComparisonDataWrapper = new KPIComparisonDataWrapper();
				LatLng location = getLocationForDataPoint(dataPoint,kpiIndexMap);
				if (location!=null && location.getLatitude() != null && location.getLongitude()!=null) {
					kpiComparisonDataWrapper.setLat(location.getLatitude());
				kpiComparisonDataWrapper.setLon(location.getLongitude());
				}
				Double actualValue = getKpiForDataPoint(dataPoint, kpi, kpiIndexMap);
				if(actualValue == null){
					continue;
				}
				kpiComparisonDataWrapper.setDtKpi(actualValue);
				String date = getTestExecutionDate(dataPoint, kpiIndexMap);
				Tile tile = new Tile(location, 15);
				BufferedImage bufferedImage = null;
				if (!imageMap.containsKey(tile.getIdWithZoom())) {
					bufferedImage = getImageForTileIdForPlanningData(tile, kpi, date);
					imageMap.put(tile.getIdWithZoom(), bufferedImage);
				}
				if(imageMap.get(tile.getIdWithZoom()) != null){
					Double plannedValue = getKpiValueFromBufferedImage(tile, location, imageMap.get(tile.getIdWithZoom()), kpi);
					if(plannedValue != null && actualValue != null) {
						kpiComparisonDataWrapper.setPredictionKpi(plannedValue);
						kpiComparisonDataWrapper.setDelta(plannedValue - actualValue);
					}
				}
				kpiDataWrapperList.add(kpiComparisonDataWrapper);
			}
			kpiComparisonInfoWrapper.setKpiDataWrapperList(kpiDataWrapperList);
			kpiInfoList.add(kpiComparisonInfoWrapper);
		}
		return kpiInfoList;
	}

	private String getTestExecutionDate(String[] dataPoint, Map<String, Integer> kpiIndexMap) {
		String formattedDate = null;
		if (dataPoint != null && dataPoint.length > kpiIndexMap.get(ReportConstants.TIMESTAMP) && NumberUtils.isNumber(
				dataPoint[kpiIndexMap.get(ReportConstants.TIMESTAMP)])) {
			Long timestamp = Long.parseLong(dataPoint[kpiIndexMap.get(ReportConstants.TIMESTAMP)]);
			formattedDate = ReportUtil.getFormattedDate(new Date(timestamp), ReportConstants.DATE_FORMAT_STEALTH_HBASE);
		}
		return formattedDate;
	}

	private Double getKpiForDataPoint(String[] dataPoint, String kpiName, Map<String, Integer> kpiIndexMap) {
		Integer indexKpi = getKpiIndexFromKpiName(kpiName,kpiIndexMap);
		if (dataPoint != null && indexKpi != null && dataPoint.length > indexKpi && NumberUtils.isNumber(dataPoint[indexKpi])) {
			return Double.parseDouble(dataPoint[indexKpi]);
		}
		return null;
	}

	private Integer getKpiIndexFromKpiName(String kpiName, Map<String, Integer> kpiIndexMap) {
		switch (kpiName) {
		case ReportConstants.RSRP:
			return kpiIndexMap.get(ReportConstants.RSRP);
		case ReportConstants.RSRQ:
			return kpiIndexMap.get(ReportConstants.RSRQ);
		case ReportConstants.SINR:
			return kpiIndexMap.get(ReportConstants.SINR);
		}
		return null;
	}

	private LatLng getLocationForDataPoint(String[] dataPoint, Map<String, Integer> kpiIndexMap) {
		if (dataPoint != null && dataPoint.length > kpiIndexMap.get(ReportConstants.LATITUDE) && NumberUtils.isNumber(
				dataPoint[kpiIndexMap.get(ReportConstants.LATITUDE)]) && NumberUtils.isNumber(
				dataPoint[kpiIndexMap.get(ReportConstants.LONGITUDE)])) {
			
			return new LatLng(Double.parseDouble(dataPoint[kpiIndexMap.get(ReportConstants.LATITUDE)]), Double.parseDouble(dataPoint[kpiIndexMap.get(ReportConstants.LONGITUDE)]));
		}
		return null;
	}

	private BufferedImage getImageForTileIdForPlanningData(Tile tile, String kpiName, String date) {
		logger.info("Inside method getImageForTileIdForPlanningData {}, {}, {}", tile, kpiName, date);
		BufferedImage bufferedImage = null;
		try {
			String coverageDate = iCoverageService.getNearestLayerDate(kpiName, "B", "OA", date).get("date");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date dateObj = sdf.parse(coverageDate);

			byte[] imageData = iGenericMapService.getImageForKpiAndZone("PredictiveCoverage", tile.getIdWithZoom(),
					"r:i", kpiName, ReportUtil.getFormattedDate(dateObj, ReportConstants.DATE_FORMAT_STEALTH_HBASE),
					"onair", null, "B");
			if (imageData != null && imageData.length > 0) {
				bufferedImage = ImageUtils.toBufferedImage(imageData);
			}
		} catch (Exception e) {
			logger.error("Error in getting buffered image: {}", Utils.getStackTrace(e));
		}
		return bufferedImage;
	}

	private Double getKpiValueFromBufferedImage(Tile tile, LatLng location, BufferedImage bufferedImage,
			String kpiName) {
//		logger.info("Inside method getKpiValueFromBufferedImage {}, {}, {}, {}", tile, location, kpiName, bufferedImage != null);
		Double kpicolorvalue = null;
		int[] tileImagePixel = TileUtils.getTileImagePixel(location, tile);
		if (bufferedImage != null) {
			Integer colorVal = bufferedImage.getRGB(tileImagePixel[NumberUtils.INTEGER_ZERO],
					tileImagePixel[NumberUtils.INTEGER_ONE]);
			kpicolorvalue = getKpicolorValue(colorVal, kpiName);
		}
		return kpicolorvalue;
	}

     private Double getKpicolorValue(Integer colorVal, String kpiName) {
		Double kpiValue = null;
        if (colorVal != null) {
			kpiValue = getDbfColorLegendMap(kpiName, colorVal);
		}
		return kpiValue;
	}

	

	@SuppressWarnings("serial")
	private static Map<String, Map<Double, List<Integer>>> getDBFColorMapFromSysConf(
			List<SystemConfiguration> systemConfigurationByName) {
		String dbfColorAsString = systemConfigurationByName.get(NumberUtils.INTEGER_ZERO).getValue();
		return new Gson().fromJson(dbfColorAsString, new TypeToken<Map<String, Map<Double, List<Integer>>>>() {
		}.getType());
	}

	private Double getDbfColorLegendMap(String kpi, Integer color) {
		Double kpiValue = null;
		try {
			List<SystemConfiguration> systemConfigurationByName = iSystemConfigurationDao
					.getSystemConfigurationByName("TILE_RANGES");
			Map<String, Map<Double, List<Integer>>> loadcolorMap = getDBFColorMapFromSysConf(systemConfigurationByName);
			Map<Double, List<Integer>> colorWithLegends = loadcolorMap.get(kpi.toUpperCase());
			TreeMap<Integer, Double> colorWithLegendsInt = new TreeMap<>();
			for (Map.Entry<Double, List<Integer>> entry : colorWithLegends.entrySet()) {
				List<Integer> colorList = entry.getValue();
				int red = (color & 0x00ff0000) >> 16;
				int green = (color & 0x0000ff00) >> 8;
				int blue = color & 0x000000ff;
				if (colorList.get(0) == red && colorList.get(1) == green && colorList.get(2) == blue) {
					kpiValue = entry.getKey();
				}
			}
		} catch (Exception e) {
			logger.error("Error inside getDbfColorLegendMap method Io exception: {}", Utils.getStackTrace(e));
		}
		return kpiValue;
	}

	private String proceedToCreateReport(KPIComparisonReportWrapper mainWrapper, GenericWorkorder workorderObj,
			Map<String, Object> jsonMap, Map<String, Object> imageMap) {
		logger.info("Going to create KPI COMPARISON Report");
		String reportAssetRepo = ConfigUtils.getString(ReportConstants.NV_KPI_COMPARISON_REPORT_PATH);
		List<KPIComparisonReportWrapper> dataSourceList = new ArrayList<>();
		dataSourceList.add(mainWrapper);
		JRBeanCollectionDataSource rfbeanColDataSource = new JRBeanCollectionDataSource(dataSourceList);
		imageMap.put(ReportConstants.SUBREPORT_DIR, reportAssetRepo);
		imageMap.put(ReportConstants.LOGO_CLIENT_KEY, reportAssetRepo + ReportConstants.LOGO_CLIENT_IMG);
//		imageMap.put(NVReportConstants.PARAM_KEY_NV_LOGO, reportAssetRepo + ReportConstants.LOGO_NV_IMG);

		logger.info("Found Parameter map: {}", new Gson().toJson(imageMap));

		String filePath = ConfigUtils.getString(ReportConstants.NV_REPORTS_PATH) + "KPI_COMPARISON/";
		String fileName = ReportUtil.getFileName(workorderObj.getWorkorderId(),
				(Integer) jsonMap.get(ReportConstants.ANALYTICS_REPOSITORY_ID), filePath);
		File file = new File(filePath);
		if(!file.exists()){
			file.mkdirs();
		}
		try {
//			String fileName = filePath + "WO_KPI_COM_TEST.pdf";
			logger.info("Going to save report on path {}", fileName);
			fileName = fileName.replace(".pdf", ".xls");
			String[] sheetnames = {"Summary","Raw Data"};;
			ReportUtil.fillDataInXlsxExporter(imageMap, reportAssetRepo + ReportConstants.MAIN_JASPER,
					rfbeanColDataSource, fileName,sheetnames);
			return fileName;
		} catch (JRException e) {
			logger.info("Exception while processing Jasper on path {} trace ==> {}", reportAssetRepo,
					Utils.getStackTrace(e));
		}
		return null;
	}

	private Response saveFileToHdfsAndUpdateStatus(AnalyticsRepository analyticObj, String filePath) {
		String hdfsFilePath = ConfigUtils.getString(ReportConstants.NV_REPORTS_PATH_HDFS) + "KPI_COMPARISON"
				+ ReportConstants.FORWARD_SLASH;
		Response responseReturn = reportService.saveFileAndUpdateStatus(analyticObj.getId(), hdfsFilePath, null,
				new File(filePath), ReportUtil.getFileNameFromFilePath(filePath),NVWorkorderConstant.REPORT_INSTACE_ID);
		//			ReportUtil.deleteAllFilesFromDirectory(ConfigUtils.getString(NVReportConstants.OPEN_DRIVE_REPORT_PATH));
		return responseReturn;
	}
}

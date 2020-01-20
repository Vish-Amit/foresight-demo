package com.inn.foresight.module.nv.report.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.inn.commons.Symbol;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.core.generic.exceptions.application.BusinessException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ConfigUtil;
import com.inn.foresight.core.generic.utils.DateUtil;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.maplayer.service.IGenericMapService;
import com.inn.foresight.core.maplayer.utils.GenericMapUtils;
import com.inn.foresight.module.nv.dashboard.model.NVDashboard;
import com.inn.foresight.module.nv.dashboard.service.INVDashboardService;
import com.inn.foresight.module.nv.dashboard.utils.NVDashboardConstants;
import com.inn.foresight.module.nv.report.RangeSlab;
import com.inn.foresight.module.nv.report.constant.ReportIndexWrapper;
import com.inn.foresight.module.nv.report.dao.INVReportHdfsDao;
import com.inn.foresight.module.nv.report.service.IMapImagesService;
import com.inn.foresight.module.nv.report.service.INVDashboardReportService;
import com.inn.foresight.module.nv.report.service.IReportService;
import com.inn.foresight.module.nv.report.utils.DriveHeaderConstants;
import com.inn.foresight.module.nv.report.utils.IBWifiReportUtils;
import com.inn.foresight.module.nv.report.utils.LegendUtil;
import com.inn.foresight.module.nv.report.utils.MergePDF;
import com.inn.foresight.module.nv.report.utils.ReportConstants;
import com.inn.foresight.module.nv.report.utils.ReportUtil;
import com.inn.foresight.module.nv.report.workorder.wrapper.DriveImageWrapper;
import com.inn.foresight.module.nv.report.workorder.wrapper.KPIWrapper;
import com.inn.foresight.module.nv.report.wrapper.NVDashboardPlotWrapper;
import com.inn.foresight.module.nv.report.wrapper.NVDashboardReportWrapper;
import com.inn.foresight.module.nv.report.wrapper.NvReportWrapper;
import com.inn.product.legends.dao.ILegendRangeDao;
import com.inn.product.legends.utils.LegendWrapper;
import com.inn.product.systemconfiguration.dao.SystemConfigurationDao;
import com.inn.product.systemconfiguration.model.SystemConfiguration;
import com.inn.product.um.geography.dao.GeographyL1Dao;
import com.inn.product.um.geography.dao.GeographyL2Dao;
import com.inn.product.um.geography.dao.GeographyL3Dao;
import com.inn.product.um.geography.dao.GeographyL4Dao;
import com.inn.product.um.geography.model.GeographyL1;
import com.inn.product.um.geography.model.GeographyL2;
import com.inn.product.um.geography.model.GeographyL3;
import com.inn.product.um.geography.model.GeographyL4;

import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service("NVDashboardReportServiceImpl")
public class NVDashboardReportServiceImpl implements INVDashboardReportService {

	Logger logger = LogManager.getLogger(NVDashboardReportServiceImpl.class);

	@Autowired
	private INVDashboardService iNvDashboardService;

	@Autowired
	private IGenericMapService iGenericMapService;

	@Autowired
	private GeographyL2Dao iGeographyL2Dao;

	@Autowired
	private GeographyL3Dao iGeographyL3Dao;

	@Autowired
	private GeographyL4Dao iGeographyL4Dao;

	@Autowired
	private GeographyL1Dao iGeographyL1Dao;
	
	@Autowired
	private IReportService iReportService;
	
	@Autowired
	private IMapImagesService iMapImageService;
	
	@Autowired
	private SystemConfigurationDao systemConfigurationDao;
	
	@Autowired
	private ILegendRangeDao legendRangeDao;
	
	@Autowired
	private INVReportHdfsDao nvReportHdfsDao;
	
	private List<String> filesToDeleteAfterExecution = new ArrayList<>();
	
	@Override
	public File generateNvDashboardReport(String geographyName, String geographyType, String band, String technology, String operator, String startDate, String endDate) throws Exception {
		logger.info("Inside Method generateNvDashboardReport In Service");
		try {
			List<File> nvDashboardReportFiles = new ArrayList<>();
			nvDashboardReportFiles.add(getIntroPage(geographyName,startDate));
			Map<String, String> zoomLevelMap = systemConfigurationDao.getSystemConfigurationByNameList(Arrays.asList(ReportConstants.GEOGRAPHY_L1_ZOOM, ReportConstants.GEOGRAPHY_L2_ZOOM, ReportConstants.GEOGRAPHY_L3_ZOOM, ReportConstants.GEOGRAPHY_L4_ZOOM)).stream().collect(Collectors.toMap(SystemConfiguration::getName, SystemConfiguration::getValue));
			List<KPIWrapper> kpiWrapperList = getLegendData();
			Map<String, Object> map = getImageLegendImageMapForDeviceTable(kpiWrapperList);
			if(geographyType.equalsIgnoreCase(ReportConstants.GEOGRAPHYL0)) {
				List<GeographyL1> geoL1List = iGeographyL1Dao.getGeographyL1List();
					
				List<NVDashboard> nvDashboardList = getNvDashBoardReportData(geographyName, geographyType, band, technology, operator, startDate, endDate);
				logger.info("Parent data Size of Mysql {}",(!nvDashboardList.isEmpty())?nvDashboardList.size():"list is null");
				if(!nvDashboardList.isEmpty()) {
					nvDashboardReportFiles.add(generateNvDashboardReportGeographyWise(zoomLevelMap.get(ReportConstants.GEOGRAPHY_L1_ZOOM), geographyName, ReportConstants.GEOGRAPHYL0, startDate, endDate ,kpiWrapperList,nvDashboardList,map));
					logger.info("GeographyL1 List Size{} ",geoL1List!=null);
					for(GeographyL1 geoL1 : geoL1List){
						logger.info("Looping L1");
						nvDashboardReportFiles.add(generateNvDashboardReportGeographyWise(zoomLevelMap.get(ReportConstants.GEOGRAPHY_L1_ZOOM), geoL1.getName(), ReportConstants.GEOGRAPHYL1, startDate, endDate ,kpiWrapperList,nvDashboardList,map));
					}
				}
			}else if(geographyType.equalsIgnoreCase(ReportConstants.GEOGRAPHYL1)) {
				List<GeographyL2> geoL2List = iGeographyL2Dao.getL2ByL1Name(geographyName);
				List<NVDashboard> nvDashboardList = getNvDashBoardReportData(geographyName, geographyType, band, technology, operator, startDate, endDate);
				if(!nvDashboardList.isEmpty()) {
					nvDashboardReportFiles.add(generateNvDashboardReportGeographyWise(zoomLevelMap.get(ReportConstants.GEOGRAPHY_L1_ZOOM), geographyName, ReportConstants.GEOGRAPHYL1, startDate, endDate ,kpiWrapperList,nvDashboardList,map));
					logger.info("GeographyL2 List Size{} ",geoL2List.size());
					for(GeographyL2 geoL2 : geoL2List){
						logger.info("Looping L2");
						nvDashboardReportFiles.add(generateNvDashboardReportGeographyWise(zoomLevelMap.get(ReportConstants.GEOGRAPHY_L2_ZOOM), geoL2.getName(), ReportConstants.GEOGRAPHYL2, startDate, endDate ,kpiWrapperList,nvDashboardList,map));
					}
				}
			}else if(geographyType.equalsIgnoreCase(ReportConstants.GEOGRAPHYL2)) {
				List<GeographyL3> geoL3List = iGeographyL3Dao.getL3ByL2Name(geographyName);
				List<NVDashboard> nvDashboardList = getNvDashBoardReportData(geographyName, geographyType, band, technology, operator, startDate, endDate);
				if(!nvDashboardList.isEmpty()) {
					nvDashboardReportFiles.add(generateNvDashboardReportGeographyWise(zoomLevelMap.get(ReportConstants.GEOGRAPHY_L2_ZOOM), geographyName, ReportConstants.GEOGRAPHYL2, startDate, endDate ,kpiWrapperList,nvDashboardList,map));
					logger.info("GeographyL3 List Size{} ",geoL3List.size());
					for(GeographyL3 geoL3 : geoL3List){
						logger.info("Looping L3");
						nvDashboardReportFiles.add(generateNvDashboardReportGeographyWise(zoomLevelMap.get(ReportConstants.GEOGRAPHY_L3_ZOOM), geoL3.getName(), ReportConstants.GEOGRAPHYL3, startDate, endDate ,kpiWrapperList,nvDashboardList,map));
					}
				}
			}else if(geographyType.equalsIgnoreCase(ReportConstants.GEOGRAPHYL3)) {
				List<GeographyL4> geoL4List = iGeographyL4Dao.getL4ByL3Name(geographyName);
				List<NVDashboard> nvDashboardList = getNvDashBoardReportData(geographyName, geographyType, band, technology, operator, startDate, endDate);
				if(!nvDashboardList.isEmpty()) {
					nvDashboardReportFiles.add(generateNvDashboardReportGeographyWise(zoomLevelMap.get(ReportConstants.GEOGRAPHY_L3_ZOOM), geographyName, ReportConstants.GEOGRAPHYL3, startDate, endDate ,kpiWrapperList,nvDashboardList,map));
					logger.info("GeographyL4 List Size{} ",geoL4List.size());
					for(GeographyL4 geoL4 : geoL4List){
						logger.info("Looping L4");
						nvDashboardReportFiles.add(generateNvDashboardReportGeographyWise(zoomLevelMap.get(ReportConstants.GEOGRAPHY_L4_ZOOM), geoL4.getName(), ReportConstants.GEOGRAPHYL4, startDate, endDate ,kpiWrapperList,nvDashboardList,map));
					}
				}
			}else if(geographyType.equalsIgnoreCase(ReportConstants.GEOGRAPHYL4)) {
				List<NVDashboard> nvDashboardList = getNvDashBoardReportData(geographyName, geographyType, band, technology, operator, startDate, endDate);
				if(!nvDashboardList.isEmpty()) {
					nvDashboardReportFiles.add(generateNvDashboardReportGeographyWise(zoomLevelMap.get(ReportConstants.GEOGRAPHY_L4_ZOOM),geographyName, ReportConstants.GEOGRAPHYL4, startDate, endDate ,kpiWrapperList,nvDashboardList,map));
				}
			}
			
			return getSingleZipFileForDashboardReport(nvDashboardReportFiles,geographyName,startDate);
		} catch (Exception e) {
			logger.error("Exception inside generateNvDashboardReport {} ",ExceptionUtils.getStackTrace(e));
			throw new BusinessException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}


	private File getSingleZipFileForDashboardReport(List<File> nvDashboardReportFiles, String geographyName,String strDate) throws ParseException {
		if(nvDashboardReportFiles!=null&&nvDashboardReportFiles.size()>1){
		String destinationpath = ConfigUtils.getString(ReportConstants.NV_REPORTS_PATH) + ReportConstants.NV_DASHBOARD + Symbol.SLASH_FORWARD;
		String destinationFilePDF = destinationpath+ geographyName+Symbol.UNDERSCORE+DateUtil.parseDateToString(ReportConstants.DATE_FORMAT_DD_SP_MM_YY,DateUtil.parseStringToDate(ReportConstants.DATE_FORMAT_YYYYMMDD,strDate))+ReportConstants.PDF_EXTENSION;
		nvDashboardReportFiles = nvDashboardReportFiles.stream().filter(Objects::nonNull).collect(Collectors.toList());
		MergePDF.mergeFiles(destinationFilePDF, nvDashboardReportFiles);
		nvDashboardReportFiles.stream().forEach(File::delete);
		return new File(destinationFilePDF);
		} else {
			throw new BusinessException(ForesightConstants.EXCEPTION_NO_RECORD_FOUND);
		}
	}
	

	private File getIntroPage(String geographyName, String strDate) {
		File nvDashboardReport = null;
		try {
			Map<String, Object> map =new HashMap<>();
			String jasperPath = ConfigUtils.getString(ReportConstants.NV_DASHBOARD_REPORT_PATH);
			map.put(ReportConstants.SUBREPORT_DIR, jasperPath);
			map.put(ReportConstants.GEOGRAPHY_NAME, geographyName);
			map.put(ReportConstants.DATE, DateUtil.parseDateToString(ReportConstants.DATE_FORMAT_DD_SP_MM_YY,DateUtil.parseStringToDate(ReportConstants.DATE_FORMAT_YYYYMMDD,strDate)));
			
			
			List<NvReportWrapper> list = new ArrayList<>();
			JRBeanCollectionDataSource rfbeanColDataSource = new JRBeanCollectionDataSource(list);
			String destinationpath = ConfigUtils.getString(ReportConstants.NV_REPORTS_PATH) + ReportConstants.NV_DASHBOARD + Symbol.SLASH_FORWARD;
			ReportUtil.createDirectory(destinationpath);
			String  destinationFile = destinationpath +"Intro_"+geographyName+Symbol.UNDERSCORE+new Date().getTime()+ReportConstants.PDF_EXTENSION;
			
			JasperRunManager.runReportToPdfFile(jasperPath + "Intro_page.jasper",
					destinationFile, map, rfbeanColDataSource);
			return new File(destinationFile);
		} catch (Exception e) {
			logger.error("Error in getIntroPage {} ", ExceptionUtils.getStackTrace(e));
		}
		return nvDashboardReport;
	}
	
	public File generateNvDashboardReportGeographyWise(String zoomLevel, String geographyName, String geographyType, String startDate, String endDate, List<KPIWrapper> kpiWrapperList, List<NVDashboard> dashboards, Map<String, Object> map) {
		logger.info("=============================Creating Data for GeographyLevel {}, GeographyName {} =======================================",geographyType, geographyName);
		try {
			String strGeoName = geographyName;
			NvReportWrapper reportWrapper = new NvReportWrapper();
			 List<NVDashboard> listNvDashboard = getDashboarDataForGeography(geographyType, geographyName, dashboards);
			 NVDashboardReportWrapper dashboardWrapper = prepareNvDashboardReportWrapper(listNvDashboard!=null && !listNvDashboard.isEmpty()?listNvDashboard.get(0):null);
			if(dashboardWrapper!=null){
				map.put(ReportConstants.SHOW_DASHBOARD_PAGE,ReportConstants.TRUE);
				List<NVDashboard> nvDashboardSummary = getSummaryDataForGeography(geographyType, geographyName, dashboards);
				logger.info("nvDashboardSummary size for {} and {} is {}",geographyType, geographyName,nvDashboardSummary!=null?nvDashboardSummary.size():"nvDashboardSummary is null");
				dashboardWrapper.setDashboardDataList(nvDashboardSummary);
				map.put(ReportConstants.SHOW_DEVICE_PAGE,dashboardWrapper.getDashboardDataList()!=null&&!dashboardWrapper.getDashboardDataList().isEmpty()?ReportConstants.TRUE:ReportConstants.FALSE);
				getNvDashboardImageMap(zoomLevel, geographyType, geographyName,startDate, endDate, reportWrapper, kpiWrapperList,map);
				logger.info("showDashboardPage : {},  showDevicePage : {}",map.get(ReportConstants.SHOW_DASHBOARD_PAGE),map.get(ReportConstants.SHOW_DEVICE_PAGE));
				reportWrapper.setNvDashboardReportWrapperList(Arrays.asList(dashboardWrapper));
				if(map.get(ReportConstants.SHOW_DASHBOARD_PAGE)!=null && map.get(ReportConstants.SHOW_DASHBOARD_PAGE).equals(ReportConstants.TRUE)
						&& map.get(ReportConstants.SHOW_DEVICE_PAGE)!=null && map.get(ReportConstants.SHOW_DEVICE_PAGE).equals(ReportConstants.TRUE)){
					return createNvDashboardReport(reportWrapper, map, strGeoName);
				}
			}
		} catch (Exception e) {
			logger.error("Exception inside generateNvDashboardReport {} ",ExceptionUtils.getStackTrace(e));
			throw new BusinessException(e.getMessage());
		}
		return null;
	}

	private void getNvDashboardImageMap(String zoomLevel, String geographyType, String geographyName, String startDate, String endDate, NvReportWrapper reportWrapper, List<KPIWrapper> kpiWrapperList, Map<String, Object> map) {
		try{
		List<NVDashboardPlotWrapper> parentGoogleMapPlotPath = new ArrayList<>();
		setDataForImageMap(zoomLevel, geographyName, geographyType, startDate, endDate, kpiWrapperList,parentGoogleMapPlotPath,map);
		reportWrapper.setParentGoogleMapPlotPath(parentGoogleMapPlotPath);
		}catch (Exception e){
				logger.error("Exception Inside getNvDashboardImageMap {} ",ExceptionUtils.getStackTrace(e));
			}
		}
	
	private void setDataForImageMap(String zoomLevel, String geoLName, String geographyType, String startDate, String endDate,
			List<KPIWrapper> kpiWrapperList, List<NVDashboardPlotWrapper> mapPlotPathList, Map<String, Object> map){
		logger.info("setDataForImageMap zoomLevel {} , geoLName {} , kpiWrapperList Size {} , mapPlotPathList {} ",
				zoomLevel, geoLName, kpiWrapperList!=null?kpiWrapperList.size():"kpiWrapperList is Null", mapPlotPathList!=null?mapPlotPathList.size():"mapPlotPathList is Null");
		try{ 
		List columnList = Arrays.asList(ReportConstants.R_MAX_LAT, ReportConstants.R_MAX_LNG, ReportConstants.R_MIN_LAT, ReportConstants.R_MIN_LNG, ReportConstants.R_GEO_NAME);
		List<Map<String, String>> dataList;
		dataList = getViewPortOfGeography(geoLName, geographyType, columnList);
		Integer zoomLevelInt = Integer.parseInt(zoomLevel);
		if(dataList==null) throw new BusinessException("Exception in finding ViewPort Of Geography ");
		Double nWLat = dataList.stream().mapToDouble(item -> ReportUtil.getDoubleValue(item.get(ReportConstants.CLM_MAX_LAT))).max().getAsDouble();
		Double nWLng = dataList.stream().mapToDouble(item -> ReportUtil.getDoubleValue(item.get(ReportConstants.CLM_MAX_LNG))).max().getAsDouble();
		Double sELat = dataList.stream().mapToDouble(item -> ReportUtil.getDoubleValue(item.get(ReportConstants.CLM_MIN_LAT))).min().getAsDouble();
		Double sELng = dataList.stream().mapToDouble(item -> ReportUtil.getDoubleValue(item.get(ReportConstants.CLM_MIN_LNG))).min().getAsDouble();
		
		List<String> columnListoGetFromHbase = Arrays.asList("r:glat","r:glon","r:COM0RS","r:COM0RSCNT","r:COM0DL","r:COM0DLCNT");
		List<List<String>> dataForMap = getDataForMap(zoomLevelInt, columnListoGetFromHbase, nWLat, nWLng, sELat, sELng,  getFormattedDateForHbase(startDate), getFormattedDateForHbase(endDate), null, null);
		logger.info("Data of dataForMap Size {} ", dataForMap!=null?dataForMap.size():"dataForMap List is Null");
		SystemConfiguration sysConfig= null;
		if(geographyType.equalsIgnoreCase(ReportConstants.GEOGRAPHYL0)){
			sysConfig = systemConfigurationDao.getConfigurationByName(ReportConstants.GEOGRAPHYL0);
			sysConfig.setValue(null);
		} else {
			sysConfig = systemConfigurationDao.getConfigurationByName(geographyType);
		}
		map.put(ReportConstants.GEOGRAPHY_TYPE, sysConfig.getValue());
		map.put(ReportConstants.GEOGRAPHY_NAME, geoLName);
		addImageWrappers(dataForMap, kpiWrapperList, geoLName, sysConfig.getValue(), mapPlotPathList);
		logger.info("setDataForImageMap mapPlotPathList Size {} ",mapPlotPathList!=null?mapPlotPathList.size():"DL List is Null");
		} catch (Exception e) {
			logger.error("Exception inside setDataForImageMap {} ",ExceptionUtils.getStackTrace(e));
		}
	}


	private void addImageWrappers(List<List<String>> dataForMap, List<KPIWrapper> kpiWrapperList, String geoLName,
			String geographyType, List<NVDashboardPlotWrapper> mapPlotPathList) {
		if (dataForMap != null && kpiWrapperList != null && !dataForMap.isEmpty()) {
			try {
				kpiWrapperList = kpiWrapperList	.stream()
												.filter(kpiWrapper -> kpiWrapper.getKpiName() != null
														&& (kpiWrapper	.getKpiName()
																		.equals(DriveHeaderConstants.DL_THROUGHPUT) || (kpiWrapper	.getKpiName()
																				.equals(DriveHeaderConstants.RSRP))))
												.collect(Collectors.toList());
				if (kpiWrapperList != null && !kpiWrapperList.isEmpty()) {
					NVDashboardPlotWrapper imageWrapperDL = new NVDashboardPlotWrapper();
					NVDashboardPlotWrapper imageWrapperRsrp = new NVDashboardPlotWrapper();
					HashMap<String, String> legendMap = getLegendImage(dataForMap, kpiWrapperList);
					HashMap<String, String> googleMapImage = getGoogleMapPlotImage(dataForMap, kpiWrapperList);
					
						prepareImageWrapper(geoLName, geographyType, imageWrapperDL, imageWrapperRsrp, legendMap,
								googleMapImage);
						logger.info("Inside addImageWrappers imageWrapperDL{} ", imageWrapperDL.toString());
						logger.info("Inside addImageWrappers imageWrapperRsrp{} ", imageWrapperRsrp.toString());
						mapPlotPathList.add(imageWrapperDL);
						mapPlotPathList.add(imageWrapperRsrp);
				}
			} catch (Exception e) {
				logger.error("Exception in getImageWrapperDL {} ", ExceptionUtils.getStackTrace(e));
			}
		}
		
	}


	private void prepareImageWrapper(String geoLName, String geographyType, NVDashboardPlotWrapper imageWrapperDL,
			NVDashboardPlotWrapper imageWrapperRsrp, HashMap<String, String> legendMap,
			HashMap<String, String> googleMapImage) {
		imageWrapperDL.setKpiLegend(legendMap.get(
				ReportConstants.
				LEGEND + ReportConstants.UNDERSCORE + DriveHeaderConstants.INDEX_DL));
		imageWrapperRsrp.setKpiLegend(legendMap.get(
				ReportConstants.LEGEND + ReportConstants.UNDERSCORE + DriveHeaderConstants.INDEX_RSRP));

		imageWrapperDL.setKpiImage(googleMapImage.get(DriveHeaderConstants.INDEX_DL.toString()));
		imageWrapperRsrp.setKpiImage(googleMapImage.get(DriveHeaderConstants.INDEX_RSRP.toString()));
imageWrapperDL.setTitle(ReportConstants.NV_DASHBOARD_DL_TITLE + Symbol.PARENTHESIS_OPEN + (geographyType!=null?geographyType:Symbol.EMPTY_STRING)+ (geographyType!=null&&geoLName!=null?Symbol.COLON:Symbol.EMPTY_STRING) +  (geoLName!=null?geoLName:Symbol.EMPTY_STRING)
					+ Symbol.PARENTHESIS_CLOSE +Symbol.SPACE+ legendMap.get("dlPercentage") + " % in Good Speed (>= 1Mbps)");

imageWrapperRsrp.setTitle(ReportConstants.NV_DASHBOARD_RSRP_TITLE + Symbol.PARENTHESIS_OPEN + (geographyType!=null?geographyType:Symbol.EMPTY_STRING)+ (geographyType!=null&&geoLName!=null?Symbol.COLON:Symbol.EMPTY_STRING) +  (geoLName!=null?geoLName:Symbol.EMPTY_STRING)
			+ Symbol.PARENTHESIS_CLOSE +Symbol.SPACE+ legendMap.get("rsrpPercentage") + "% in Good Rsrp (>= -105 dbm)");
	}


	private List<Map<String, String>> getViewPortOfGeography(String geoLName, String geographyType, List columnList) {
		logger.info("Inside getViewPortOfGeography with geoLName : {}, geographyType : {}, columnList : {} ", geoLName, geographyType,columnList);
		List<Map<String, String>> dataList;
		if(geographyType.equalsIgnoreCase(ReportConstants.GEOGRAPHYL0)){
			SystemConfiguration systemConfiguration = systemConfigurationDao.getConfigurationByName(ReportConstants.DEFAULT_MAP_VIEWPORT);
			Map<String, String> viewPortMap = new Gson().fromJson(systemConfiguration.getValue(),Map.class);
			dataList = new ArrayList<>();
			dataList.add(viewPortMap);
		} else {
		dataList = iGenericMapService.getBoundaryDataByGeographyNamesMS(Arrays.asList(geoLName), 
				GenericMapUtils.GEOGRAPHY_TABLE_NAME, columnList, null, geographyType.toUpperCase());
		}
		logger.info("Corners Lat long List {} ", dataList!=null&&!dataList.isEmpty()?dataList.size():"List is Null");
		return dataList;
	}
	
	private HashMap<String,String> getGoogleMapPlotImage(List<List<String>> dataListForMap, List<KPIWrapper> kpiWrapperList) throws IOException {
		logger.info("Inside getGoogleMapPlotImage dataListForMap Size {} ",dataListForMap!=null?dataListForMap.size():"List is Null");
		HashMap<String,String> googleMapImage = new HashMap<>();
		try {
			DriveImageWrapper driveImageWrapper = new DriveImageWrapper();
			driveImageWrapper.setKpiWrappers(kpiWrapperList);
			List<String[]> dataKPIs = new ArrayList<>();
			
			for(List<String> dataList :dataListForMap){
					String[] str = new String[100];
				str[DriveHeaderConstants.INDEX_LAT]=dataList.get(0);
				str[DriveHeaderConstants.INDEX_LON]=dataList.get(1);
				str[DriveHeaderConstants.INDEX_RSRP]=dataList.get(2);
				str[DriveHeaderConstants.INDEX_DL]=dataList.get(4);
				dataKPIs.add(str);
			}
			driveImageWrapper.setDataKPIs(dataKPIs);
			driveImageWrapper.setIndexLatitude(DriveHeaderConstants.INDEX_LAT);
			driveImageWrapper.setIndexLongitude(DriveHeaderConstants.INDEX_LON);
			
			String folder =System.currentTimeMillis()+ReportConstants.FORWARD_SLASH;
			googleMapImage = iMapImageService.saveDriveImages(iMapImageService.getDriveImagesForReport(driveImageWrapper, null, null), ConfigUtils.getString(ReportConstants.IMAGE_PATH_FOR_NV_REPORT)+ReportConstants.NV_DASHBOARD+ReportConstants.FORWARD_SLASH+folder, false);
			logger.info("getGoogleMapPlotImage map Size {} ",googleMapImage.size());
			filesToDeleteAfterExecution.addAll(googleMapImage.values());
		} catch(Exception e) {
			logger.error("Error in Creating Map Image : {}",ExceptionUtils.getStackTrace(e));
		}
		return googleMapImage;
	}
	
	private HashMap<String,String> getLegendImage(List<List<String>> dataListMap, List<KPIWrapper> kpiWrapperList) {
		logger.info("Inside getLegendImage Kpi Wrapper {} ",new Gson().toJson(kpiWrapperList));
		HashMap<String,String> legendMap;
		Double goodPercentageRsrp = null;
		Double goodPercentageDl = null;
		Integer totalRsrpCount = null;
		Integer totalDlCount = null;
		Integer goodCountDl = 0;
		Integer goodCountRsrp = 0;
			for (KPIWrapper wrapper : kpiWrapperList) {
				if(wrapper.getKpiName().equals(DriveHeaderConstants.RSRP)) {
					for (RangeSlab rangeslab : wrapper.getRangeSlabs()) {
						rangeslab.setCount(getKpiCountBetweenRange(dataListMap, rangeslab,ReportConstants.INDEX_TWO, ReportConstants.INDEX_THREE));
					}
					totalRsrpCount = wrapper.getRangeSlabs()
							.stream().filter(rangeSlab -> rangeSlab.getCount()!=null)
							.mapToInt(RangeSlab::getCount)
							.sum();
					  wrapper.setTotalCount(totalRsrpCount);
					  goodCountRsrp += wrapper.getRangeSlabs().stream().filter(rangeSlab -> rangeSlab.getCount()!=null && rangeSlab.getLowerLimit()>=ReportConstants.RSRP_GOOD_FOR_DASHBOARD).mapToInt(RangeSlab::getCount)
								.sum();
				} else 	if(wrapper.getKpiName().equals(DriveHeaderConstants.DL_THROUGHPUT)) {
					for (RangeSlab rangeslab : wrapper.getRangeSlabs()) {
						rangeslab.setCount(getKpiCountBetweenRange(dataListMap, rangeslab,ReportConstants.INDEX_FOUR,ReportConstants.INDEX_FIVE));
					}
					totalDlCount = wrapper.getRangeSlabs()
							.stream().filter(rangeSlab -> rangeSlab.getCount()!=null)
							.mapToInt(RangeSlab::getCount)
							.sum();
					  wrapper.setTotalCount(totalDlCount);
					  goodCountDl += wrapper.getRangeSlabs().stream().filter(rangeSlab -> rangeSlab.getCount()!=null && rangeSlab.getLowerLimit()>=ReportConstants.DL_GOOD_FOR_DASHBOARD).mapToInt(RangeSlab::getCount)
								.sum();
				}
			}
			 goodPercentageRsrp = ReportUtil.getPercentage(goodCountRsrp, totalRsrpCount);
             goodPercentageDl = ReportUtil.getPercentage(goodCountDl, totalDlCount);
             logger.info("totalRsrpCount: {}, totalDLCount: {}, goodPercentageRsrp: {}, goodPercentageDl: {}",totalRsrpCount, totalDlCount, goodPercentageRsrp,goodPercentageDl);
			String folder =ReportUtil.getFormattedDate(new Date(), ReportConstants.DATE_FORMAT_DD_MM_YY_HH_SS)+ReportConstants.FORWARD_SLASH;
			legendMap = iMapImageService.saveDriveImages(iReportService.getLegendImages(kpiWrapperList, null, DriveHeaderConstants.INDEX_TEST_TYPE), ConfigUtils.getString(ConfigUtil.IMAGE_PATH_FOR_NV_REPORT)+ReportConstants.NV_DASHBOARD+ReportConstants.FORWARD_SLASH+folder, false);
			legendMap.put("dlPercentage", goodPercentageDl!=null?goodPercentageDl.toString():"-");
			legendMap.put("rsrpPercentage", goodPercentageRsrp!=null?goodPercentageRsrp.toString():"-");
			logger.info("Inside getLegendImage Returning size {} ",legendMap.size());	
			filesToDeleteAfterExecution.addAll(legendMap.values());
		return legendMap;
	}
	private Integer getKpiCountBetweenRange(List<List<String>> dataListMap, RangeSlab rangeslab, Integer kpiIndex, Integer countIndex) {
		logger.info("Inside getKpiCountBetweenRange with data size {} ",dataListMap!=null?dataListMap.size():" dataListMap is null");
		Integer count = 0;
		for(List<String> dataList : dataListMap){
			if(dataList!=null && !dataList.isEmpty() && dataList.get(kpiIndex)!=null && !dataList.get(kpiIndex).isEmpty()){
				Double kpiValue = Double.parseDouble(dataList.get(kpiIndex));
				if(kpiValue!=null && kpiValue>=rangeslab.getLowerLimit() && kpiValue<rangeslab.getUpperLimit() && dataList.get(countIndex)!=null && !dataList.isEmpty()){
						count+=Integer.parseInt(dataList.get(countIndex));
				}
			}
		}
		return count;
	}

	private File createNvDashboardReport(NvReportWrapper reportWrapper, Map<String, Object> map, String geographyName) {
		try {
			logger.info("createNvDashboardReport reportWrapper {} ",reportWrapper==null?"wrapper is NUll":"Wrapper is NOT Null");
			logger.info("createNvDashboardReport map size {} ",map.size());
			File nvDashboardReport = null;
			String jasperPath = ConfigUtils.getString(ReportConstants.NV_DASHBOARD_REPORT_PATH);
			map.put(ReportConstants.SUBREPORT_DIR, jasperPath);
			
			List<NvReportWrapper> list = new ArrayList<>();
			list.add(reportWrapper);
			JRBeanCollectionDataSource rfbeanColDataSource = new JRBeanCollectionDataSource(list);
			String destinationpath = ConfigUtils.getString(ReportConstants.NV_REPORTS_PATH) + ReportConstants.NV_DASHBOARD + Symbol.SLASH_FORWARD + new Date().getTime() + Symbol.SLASH_FORWARD;
			ReportUtil.createDirectory(destinationpath);
			String  destinationFile = destinationpath +geographyName+Symbol.UNDERSCORE+ DateUtil.parseDateToString(ReportConstants.DATE_FORMAT_DD_MM_YY, new Date())+ReportConstants.PDF_EXTENSION;
			
			JasperRunManager.runReportToPdfFile(jasperPath + ReportConstants.MAIN_JASPER,
					destinationFile, map, rfbeanColDataSource);
			IBWifiReportUtils.removeJunkFilesFromDisk(filesToDeleteAfterExecution);
			return new File(destinationFile);
		} catch (Exception e) {
			logger.error("Error in createNvDashboardReport {} ", ExceptionUtils.getStackTrace(e));
			throw new BusinessException(e.getMessage());
		}
	}

	private List<NVDashboard> getNvDashBoardReportData(String geoName, String geographyType, String band, String technology, String operator, String startDate, String endDate) {
		List<NVDashboard> dashboardDataList = null;
		logger.info("Inside getNvDashBoardReportData Getting Data");
		try {
			DateFormat format = new SimpleDateFormat(ReportConstants.DATE_FORMAT_YYYYMMDD);
			dashboardDataList = iNvDashboardService.getNVDashboardDataByDateForReport(geographyType, geoName,
					format.parse(startDate), format.parse(endDate), band, technology, operator, NVDashboardConstants.DEVICE, null,null);
			if (dashboardDataList == null || dashboardDataList.isEmpty()) {
					throw new BusinessException("List from DataBase is Empty Or Null");
			}
		} catch (Exception e) {
			logger.error("Error Inside getNvDashBoardReportWapper {} ", ExceptionUtils.getStackTrace(e));
			throw new BusinessException(ForesightConstants.ERROR_RESPONSE);
		}
		logger.info("Inside getNvDashBoardReportData Returning Data Size {} ",dashboardDataList.size());
		return dashboardDataList;
	}

	private NVDashboardReportWrapper prepareNvDashboardReportWrapper(NVDashboard nvDashboard) {
		logger.info("Preparing Nv NVDashboardReportWrapper from NVDashboard {} ",nvDashboard!=null?nvDashboard.toString():"dashboard Object is null");
		NVDashboardReportWrapper dashboardWrapper = null;
		if (nvDashboard != null) {
			dashboardWrapper = new NVDashboardReportWrapper();
			dashboardWrapper.setAvgDlRate(nvDashboard.getAvgDlRate());
			dashboardWrapper.setAvgUlRate(nvDashboard.getAvgUlRate());
			dashboardWrapper.setLatency(nvDashboard.getLatency());
			dashboardWrapper.setSignalStrength(nvDashboard.getSignalStrength());
			dashboardWrapper.setQuality(nvDashboard.getQuality());
			dashboardWrapper.setHealthIndex(nvDashboard.getStarRating());
			dashboardWrapper.setUrl1BrowseTime(nvDashboard.getUrl1BrowseTime());
			dashboardWrapper.setUrl2BrowseTime(nvDashboard.getUrl2BrowseTime());
			dashboardWrapper.setUrl3BrowseTime(nvDashboard.getUrl3BrowseTime());
			dashboardWrapper.setNumberOfUsers(nvDashboard.getTotalUC());
			dashboardWrapper.setAndroidUsers(nvDashboard.getAndroidUC());
			dashboardWrapper.setIosUsers(nvDashboard.getIosUC());
			if (nvDashboard.getSampleAndroid() != null && nvDashboard.getSampleIos() != null) {
				dashboardWrapper.setNumberOfTests(nvDashboard.getSampleIos() + nvDashboard.getSampleAndroid());
			} else if (nvDashboard.getSampleAndroid() != null) {
				dashboardWrapper.setNumberOfTests(nvDashboard.getSampleAndroid());
			} else if (nvDashboard.getSampleIos() != null) {
				dashboardWrapper.setNumberOfTests(nvDashboard.getSampleIos());
			}

			dashboardWrapper.setAndroidTests(nvDashboard.getSampleAndroid());
			dashboardWrapper.setIosTests(nvDashboard.getSampleIos());
		}
		logger.info("Inside getDashboarDataForGeography Returning {}",dashboardWrapper!=null?dashboardWrapper.toString():"Object is Null");
		return dashboardWrapper;

	}
	
	private List<List<String>> getDataForMap(Integer zoom, List<String> columnList, Double nWLat, Double nWLng,
			Double sELat, Double sELng, String minDate, String maxDate, Long latestDate, String postFix) {
		logger.info("Inside getLegendData zoom {} , columnList {}, nWLat {}, nWLng {},sELat {} , sELng {} , minDate {} , maxDate {} , latestDate {} ,  pstFix {} ",zoom,columnList,nWLat,nWLng,sELat,sELng,minDate,maxDate,latestDate,postFix);
		List<List<String>> dataFrMap= null;
		try {
			dataFrMap = iGenericMapService.getDataForTable(ConfigUtils.getString(ReportConstants.KPI_DATA_TABLE), zoom,
					columnList, nWLat, nWLng, sELat, sELng, minDate, maxDate, latestDate, postFix,null,null);
		} catch (IOException e) {
			logger.error("Inside getDataForMap {} ",e.getStackTrace());
		}
		return dataFrMap;

	}
	
	
	private List<KPIWrapper> getLegendData() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException{
		logger.info("Inside getLegendData");
		List<KPIWrapper> kpiWrapperList = null; 
		try {
			List<LegendWrapper> legendList = legendRangeDao.findAllLegendRangesAppliedTo(ReportConstants.NV_DASHBOARD_REPORT);
			logger.info("getLegendData legendList size {} ",legendList!=null?legendList.size():"legendList is Null");

			kpiWrapperList = ReportUtil.convertLegendsListToKpiWrapperList(legendList,DriveHeaderConstants.getKpiIndexMap());
//			kpiWrapperList = ReportUtil.convertLegendsListToKpiWrapperList(legendList, ReportIndexWrapper.getLiveDriveKPIIndexMap(ReportIndexWrapper.getLiveDriveKPIs()));
			logger.info("getLegendData kpiWrappetList size {} ",kpiWrapperList!=null?kpiWrapperList.size():"kpiWrappetList is Null");
			return kpiWrapperList;
		} catch (RestException e) {
			logger.error("Exception ingetting Legend Data form DB {} ",ExceptionUtils.getStackTrace(e));
		}
		return kpiWrapperList;
	}
	

	private Map<String, Object> getImageLegendImageMapForDeviceTable(List<KPIWrapper> kpiWrapperList) {
		HashMap<String, BufferedImage> map= new HashMap<>();
		HashMap<String, Object> finalMap= new HashMap<>();
		for(KPIWrapper wrapper : kpiWrapperList){			
			map.put(wrapper.getKpiName(),LegendUtil.getLegendImageForNVDashboard(wrapper.getKpiName(), wrapper.getRangeSlabs()));
		}
		finalMap.putAll(iMapImageService.saveDriveImages(map, ConfigUtils.getString(ReportConstants.IMAGE_PATH_FOR_NV_REPORT)+ReportConstants.NV_DASHBOARD_REPORT+Symbol.SLASH_FORWARD, false));
		logger.info("saveDriveImages Legend Dash Map {}",finalMap);
		
		return finalMap;
	}
	
	private String getFormattedDateForHbase(String strDate) {
		try {
			SimpleDateFormat format = new SimpleDateFormat(ForesightConstants.DDMMYY);
			SimpleDateFormat format1 = new SimpleDateFormat(ReportConstants.DATE_FORMAT_YYYYMMDD);
			Date parsedDate;
			parsedDate = format1.parse(strDate);
			return format.format(parsedDate);
		} catch (ParseException e) {
			logger.error("Inside getFormattedDateForHbase Date for Hbase data ({}) not Parsed Exception is {} ",strDate,ExceptionUtils.getStackTrace(e));
		}
		return null;
	}	
	
	private List<NVDashboard> getSummaryDataForGeography(String geographyType, String geographyName, List<NVDashboard> dashboards) {
		List<NVDashboard> dashboardList = null;
		Predicate<NVDashboard> predicate = null;
		try {
			if(dashboards!=null && !dashboards.isEmpty()) {
				if (geographyType.equalsIgnoreCase(ReportConstants.GEOGRAPHYL0)) {
					predicate = data -> (data.getGeographyL1() == null && data.getType() != null);
				} else if (geographyType.equalsIgnoreCase(ReportConstants.GEOGRAPHYL1)) {
					predicate = data -> (data.getGeographyL1() != null && data.getGeographyL1().getName().equalsIgnoreCase(geographyName) && data.getType() != null);
				} else if (geographyType.equalsIgnoreCase(ReportConstants.GEOGRAPHYL2)) {
					predicate = data -> (data.getGeographyL2() != null && data.getGeographyL2().getName().equalsIgnoreCase(geographyName) && data.getType() != null);
				} else if (geographyType.equalsIgnoreCase(ReportConstants.GEOGRAPHYL3)) {
					predicate = data -> (data.getGeographyL3() != null && data.getGeographyL3().getName().equalsIgnoreCase(geographyName) && data.getType() != null);
				} else if (geographyType.equalsIgnoreCase(ReportConstants.GEOGRAPHYL4)) {
					predicate = data -> (data.getGeographyL4() != null && data.getGeographyL4().getName().equalsIgnoreCase(geographyName) && data.getType() != null);
				}
			dashboardList = getFilteredData(dashboards, predicate);
			}
		} catch (Exception e) {
			logger.info("Exception Inside getSummaryData {}", ExceptionUtils.getStackTrace(e));
		}
		logger.info("Inside getDashboarDataForGeography Returning {} {} {}",geographyName,geographyType,(dashboardList!=null&&!dashboardList.isEmpty())?dashboardList.size():"List is Null Or Empty");
		return dashboardList;
	}

	private List<NVDashboard> getDashboarDataForGeography(String geographyType, String geographyName, List<NVDashboard> dashboards) {
		List<NVDashboard> dashboardList = null;
		try {
			if(dashboards!=null && !dashboards.isEmpty()) {
			if (geographyType.equalsIgnoreCase(ReportConstants.GEOGRAPHYL0)) {
				dashboardList =	dashboards.parallelStream()
									.filter(data -> (data.getGeographyL1() == null && data.getType() == null && data.getName() == null))
									.collect(Collectors.toList());
			} else if (geographyType.equalsIgnoreCase(ReportConstants.GEOGRAPHYL1)) {
				dashboardList =	dashboards	.parallelStream()
									.filter(data -> (data.getGeographyL1() != null && data.getGeographyL1().getName().equalsIgnoreCase(geographyName) && data.getGeographyL2() == null && data.getType() == null && data.getName() == null))
									.collect(Collectors.toList());
			} else if (geographyType.equalsIgnoreCase(ReportConstants.GEOGRAPHYL2)) {
				dashboardList =	dashboards	.parallelStream()
									.filter(data -> (data.getGeographyL2() != null && data.getGeographyL2().getName().equalsIgnoreCase(geographyName) && data.getGeographyL3() == null && data.getType() == null && data.getName() == null))
									.collect(Collectors.toList());
			} else if (geographyType.equalsIgnoreCase(ReportConstants.GEOGRAPHYL3)) {
				dashboardList =	dashboards	.parallelStream()
									.filter(data -> (data.getGeographyL3() != null && data.getGeographyL3().getName().equalsIgnoreCase(geographyName) && data.getGeographyL4() == null && data.getType() == null && data.getName() == null))
									.collect(Collectors.toList());
			} else if (geographyType.equalsIgnoreCase(ReportConstants.GEOGRAPHYL4)) {
				dashboardList =	dashboards	.parallelStream()
									.filter(data -> (data.getGeographyL4() != null && data.getGeographyL4().getName().equalsIgnoreCase(geographyName) && data.getType() == null && data.getName() == null))
									.collect(Collectors.toList());
			}
		}
		} catch (Exception e) {
			logger.info("Exception Inside getDashboarData {}", ExceptionUtils.getStackTrace(e));
		}
		logger.info("Inside getDashboarDataForGeography Returning {} {} {}",geographyName,geographyType,(dashboardList!=null&&!dashboardList.isEmpty())?dashboardList.size():"List is Null Or Empty");
		return dashboardList;
	}
	
	List<NVDashboard> getFilteredData(List<NVDashboard> dashboardList, Predicate<NVDashboard> predicate) {
		Set names = new HashSet<>();
		List<NVDashboard> finalList = new ArrayList<>();
		dashboardList = dashboardList	.parallelStream()
										.filter(predicate)
										.collect(Collectors.toList());

		for (NVDashboard nvDashboard : dashboardList) {
			if (!names.contains(nvDashboard.getName())) {
				finalList.add(nvDashboard);
				names.add(nvDashboard.getName());
			}
		}
		return finalList.parallelStream()
						.limit(10)
						.collect(Collectors.toList());
	}
	
}

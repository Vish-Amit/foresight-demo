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
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.lang.NumberUtils;
import com.inn.commons.lang.StringUtils;
import com.inn.commons.maps.rowkey.RowKeyUtils.KPI;
import com.inn.core.generic.exceptions.application.BusinessException;
import com.inn.foresight.core.generic.utils.ConfigUtil;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.NVConfigUtil;
import com.inn.foresight.module.nv.layer3.constants.Layer3PPEConstant;
import com.inn.foresight.module.nv.layer3.constants.QMDLConstant;
import com.inn.foresight.module.nv.layer3.dao.INVLayer3HbaseDao;
import com.inn.foresight.module.nv.layer3.service.INVLayer3DashboardService;
import com.inn.foresight.module.nv.report.LiveDriveWrapper;
import com.inn.foresight.module.nv.report.constant.ReportIndexWrapper;
import com.inn.foresight.module.nv.report.constant.ReportSummaryIndexWrapper;
import com.inn.foresight.module.nv.report.service.ILiveDriveReportService;
import com.inn.foresight.module.nv.report.service.IMapImagesService;
import com.inn.foresight.module.nv.report.service.IReportService;
import com.inn.foresight.module.nv.report.utils.DriveHeaderConstants;
import com.inn.foresight.module.nv.report.utils.LegendUtil;
import com.inn.foresight.module.nv.report.utils.LiveDriveReportConstants;
import com.inn.foresight.module.nv.report.utils.LiveDriveReportUtil;
import com.inn.foresight.module.nv.report.utils.ReportConstants;
import com.inn.foresight.module.nv.report.utils.ReportUtil;
import com.inn.foresight.module.nv.report.workorder.wrapper.DriveImageWrapper;
import com.inn.foresight.module.nv.report.workorder.wrapper.KPIWrapper;
import com.inn.foresight.module.nv.report.wrapper.KPIStatsWrapper;
import com.inn.foresight.module.nv.report.wrapper.LiveDriveChartWrapper;
import com.inn.foresight.module.nv.report.wrapper.LiveDriveReportWrapper;
import com.inn.foresight.module.nv.report.wrapper.LiveDriveSubWrapper;
import com.inn.foresight.module.nv.report.wrapper.SiteInformationWrapper;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.HandOverDataWrappr;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.LiveDriveVoiceAndSmsWrapper;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.YoutubeTestWrapper;
import com.inn.foresight.module.nv.workorder.dao.IWORecipeMappingDao;
import com.inn.foresight.module.nv.workorder.model.WORecipeMapping;
import com.inn.product.legends.dao.ILegendRangeDao;
import com.inn.product.legends.utils.LegendWrapper;

import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service("LiveDriveReportServiceImpl")
public class LiveDriveReportServiceImpl implements ILiveDriveReportService, LiveDriveReportConstants {

	/** The logger. */
	private static final Logger logger = LogManager.getLogger(LiveDriveReportServiceImpl.class);

	@Autowired
	private ILegendRangeDao legendRangeDao;

	/** The map image service. */

	@Autowired
	private IMapImagesService mapImageService;

	@Autowired
	private INVLayer3DashboardService nvLayer3DashboardService;

	@Autowired
	private IReportService reportService;
	@Autowired
	IWORecipeMappingDao recipeMappingDao ;
	
	@Autowired
	private INVLayer3HbaseDao nvLayer3HbaseDao;

	public LiveDriveReportServiceImpl() {
		super();
	}

	@Override
	public File getLiveDriveReportForMasterReport(List<Integer> workorderIds, Map<String, List<String>> driveRecipedetailsMap,
			boolean isMasterReport) throws IOException {
		logger.info("Inside method getLiveDriveReportForMasterReport for workOrderId {} , isMasterReport {} ",
				workorderIds, isMasterReport);
		File file = null;
		try {
			String filePath = ConfigUtils.getString(NV_REPORTS_PATH) + LIVEDRIVE
					+ FORWARD_SLASH;
			
			/** Drive Data Fetch */
			Set<String> dynamicKpis = reportService.getDynamicKpiName(workorderIds, null, Layer3PPEConstant.ADVANCE);
			
			List<String> fetchKPIList = ReportIndexWrapper.getLiveDriveKPIs().stream().filter(k -> dynamicKpis.contains(k)).collect(Collectors.toList());
			
			Map<String, Integer> kpiIndexMap = ReportIndexWrapper.getLiveDriveKPIIndexMap(fetchKPIList);
//			logger.info("generated KPIIndexMap : {}",kpiIndexMap);
			kpiIndexMap.put(DL_EARFCN, kpiIndexMap.get(EARFCN));     
			
			List<String[]> listArray = reportService.getDriveDataRecipeWiseTaggedForReport(workorderIds,driveRecipedetailsMap.get(ReportConstants.RECIPE), fetchKPIList, kpiIndexMap);
			ReportUtil.addCustomIndexDataToDriveDataArray(listArray, getCustomIndexMapForDriveData(kpiIndexMap));

			/** Summary Data Fetch */
			List<String> fetchSummaryKPIList = ReportSummaryIndexWrapper.getLiveDriveKPIs();
//			logger.info(" workorderId ,fetchSummaryKPIList   ==== {} {}",workorderId,fetchSummaryKPIList);
			Map<String, Integer> summaryKpiIndexMap = ReportSummaryIndexWrapper.getLiveDriveKPIIndexMap(fetchSummaryKPIList);
//			logger.info("generated summaryKpiIndexMap : {}",summaryKpiIndexMap);
			
			String[] summaryDataArray = reportService.getSummaryDataForReport(workorderIds, driveRecipedetailsMap.get(ReportConstants.RECIPE), fetchSummaryKPIList);
//			logger.info("workorderId {},listOfDriveSummaryData is  {} ",workorderId,  Arrays.toString(summaryDataArray));
		
			List<String[]> handoverdata = reportService.getHandoverDataFromHbase(workorderIds.get(0),
					driveRecipedetailsMap.get(ReportConstants.RECIPE));
			
			LiveDriveReportWrapper driveReportWrapper = new LiveDriveReportWrapper();
			Map<String, Object> map = getKpiWrapperListStatData(listArray, summaryDataArray, workorderIds,
						driveRecipedetailsMap, driveReportWrapper, kpiIndexMap, summaryKpiIndexMap,handoverdata);

			
			
			List<KPIWrapper> kpiList = map.get(KEY_KPILIST) != null
					? (List<KPIWrapper>) map.get(KEY_KPILIST) : null;
			driveReportWrapper = map.get(KEY_WRAPPER) != null
						? (LiveDriveReportWrapper) map.get(KEY_WRAPPER) : null;

//			logger.info("kpiList  {}",kpiList);
				HashMap<String, String> imagemap = getDriveImagesMap(listArray, kpiList, kpiIndexMap);
				if (driveReportWrapper != null) {
					driveReportWrapper.setFileName(ReportUtil.getFileName(LIVEDRIVEREPORT, workorderIds.get(ReportConstants.INDEX_ZER0), filePath));
					logger.debug("getLiveDriveReportForMasterReport {} ", new Gson().toJson(driveReportWrapper));
					driveReportWrapper.setListPsDataWrapper(reportService.getPsDataWrapperFromSummaryDataForReport(
							summaryDataArray, summaryKpiIndexMap));
					file = proceedToCreateLiveDriveReport(prepareImageMap(imagemap, kpiIndexMap), driveReportWrapper, isMasterReport,
							imagemap);
				}
		} catch (Exception e) {
			logger.error("Error inside the method getLiveDriveReportForMasterReport {} ", Utils.getStackTrace(e));
		}
		return file;
	}

	private Map<Integer, Integer> getCustomIndexMapForDriveData(Map<String, Integer> kpiIndexMap){
		Map<Integer, Integer> customIndexMap = new HashMap<>();
		if(kpiIndexMap != null && kpiIndexMap.containsKey(FTP_DL_THROUGHPUT) && kpiIndexMap.containsKey(PDSCH_THROUGHPUT)){
			customIndexMap.put(kpiIndexMap.get(FTP_DL_THROUGHPUT), kpiIndexMap.get(PDSCH_THROUGHPUT));
		}
		if(kpiIndexMap != null && kpiIndexMap.containsKey(HTTP_DL_THROUGHPUT) && kpiIndexMap.containsKey(PDSCH_THROUGHPUT)){
			customIndexMap.put(kpiIndexMap.get(HTTP_DL_THROUGHPUT), kpiIndexMap.get(PDSCH_THROUGHPUT));
		}
		if(kpiIndexMap != null && kpiIndexMap.containsKey(FTP_UL_THROUGHPUT) && kpiIndexMap.containsKey(PUSCH_THROUGHPUT)){
			customIndexMap.put(kpiIndexMap.get(FTP_UL_THROUGHPUT), kpiIndexMap.get(PUSCH_THROUGHPUT));
		}
		if(kpiIndexMap != null && kpiIndexMap.containsKey(HTTP_UL_THROUGHPUT) && kpiIndexMap.containsKey(PUSCH_THROUGHPUT)){
			customIndexMap.put(kpiIndexMap.get(HTTP_UL_THROUGHPUT), kpiIndexMap.get(PUSCH_THROUGHPUT));
		}
		return customIndexMap;
	}


	private Map<String, Object> getKpiWrapperListStatData(List<String[]> listArray, String[] summaryData,
			List<Integer> workorderIds, Map<String, List<String>> driveRecipedetailsMap,
			LiveDriveReportWrapper driveReportWrapper, Map<String, Integer> kpiIndexMap, Map<String, Integer> summaryKpiIndexMap, List<String[]> handoverdata) {
		logger.info("Inside method getKpiWrapperListStatData for workOrderId {} ", workorderIds);
		Map<String, Object> map = new HashMap<>();
		List<LegendWrapper> legendList;
		List<KPIWrapper> kpiList = null;
		try {
			legendList = legendRangeDao.findAllLegendRangesAppliedTo(LIVEDRIVE);
			
			/** update in kpi's index map */
			kpiList = ReportUtil.convertLegendsListToKpiWrapperList(legendList, kpiIndexMap);
//			kpiList = reportService.modifyIndexOfCustomKpisForReport(kpiList, kpiIndexMap);
			kpiList = LegendUtil.populateLegendData(kpiList, listArray, kpiIndexMap.get(TEST_TYPE));
			Map<String, List<Double>> liveDriveKpiMap = LiveDriveReportUtil.getKPiWiseValueList(listArray, kpiList,
					kpiIndexMap.get(TEST_TYPE), kpiIndexMap.get(TIMESTAMP));
			logger.debug("workorderId {}, liveDriveKpiMap data {} ",workorderIds , liveDriveKpiMap);
			getCellIdList(listArray, liveDriveKpiMap, kpiIndexMap);
			setConnectionSetupTime(summaryData, liveDriveKpiMap, summaryKpiIndexMap);
			LiveDriveSubWrapper driveSubWrapper = new LiveDriveSubWrapper();
			goingToSetHttpYoutubeHandoverData(workorderIds, driveRecipedetailsMap, listArray, driveSubWrapper,
					driveReportWrapper, summaryData, kpiIndexMap, summaryKpiIndexMap,handoverdata);
			setDataForLiveDriveReport(liveDriveKpiMap, kpiList, driveReportWrapper, driveSubWrapper);
			logger.debug("workorderId {}, driveReportWrapper Data {}  ", workorderIds, driveReportWrapper.toString());
			map.put(KEY_KPILIST, kpiList);
			map.put(KEY_WRAPPER, driveReportWrapper);
			return map;
		} catch (Exception e) {
			logger.error("Exception inside method getKpiWrapperListStatData {} ", Utils.getStackTrace(e));
		}
		return map;
	}

	private LiveDriveSubWrapper goingToSetHttpYoutubeHandoverData(List<Integer> workorderIds,
			Map<String, List<String>> driveRecipedetailsMap, List<String[]> listArray,
			LiveDriveSubWrapper driveSubWrapper, LiveDriveReportWrapper driveReportWrapper, String[] summaryData, Map<String, Integer> kpiIndexMap, Map<String, Integer> summaryKpiIndexMap, List<String[]> handoverdata) {
		logger.info("Inside metho goingToSetHttpYoutubeHandoverData");
		try {
			
			List<HandOverDataWrappr> handOverList = getHandOverDataListFromDriveData(handoverdata, kpiIndexMap);
			List<WORecipeMapping> mappings=recipeMappingDao.getWORecipeByGWOIds(workorderIds);
			List<HandOverDataWrappr> handOverIdelList = getHandoverCauseData(mappings, driveRecipedetailsMap);
			goingToSetHandoverData(driveSubWrapper, summaryData, driveReportWrapper, handOverList, handOverIdelList, summaryKpiIndexMap);
			goingToSetSmsAndCallData(driveSubWrapper, summaryData, driveReportWrapper, summaryKpiIndexMap);
			List<YoutubeTestWrapper> youtubeDataList = reportService.getYouTubeDataForReport(listArray, kpiIndexMap);
			List<YoutubeTestWrapper> listOfHttpDl = getHttpDownLinkData(mappings, driveRecipedetailsMap);
			goinToSetHttpLinkAndYoutubeData(driveSubWrapper, youtubeDataList, driveReportWrapper, listOfHttpDl);
			return driveSubWrapper;
		} catch (Exception e) {
			logger.error("Exception inside method goingToSetHttpYoutubeHandoverData {} ", e.getMessage());
		}
		return driveSubWrapper;
	}

	private List<YoutubeTestWrapper> getHttpDownLinkData(List<WORecipeMapping> mappings, Map<String, List<String>> map) {
		List<String> listRecipe = map.get(QMDLConstant.RECIPE);
		String httpDlLinkData = null;
		List<YoutubeTestWrapper> listYoutubeData = new ArrayList<>();
		logger.info("getHttpDownLinkData Recipe List Details : Recipes {} ", listRecipe.toArray());
		for (WORecipeMapping recipeMapping : mappings) {
			try {
				if(listRecipe.contains(String.valueOf(recipeMapping.getId()))) {
				httpDlLinkData = nvLayer3DashboardService.getHttpDownLinkDataFromLayer3Report(recipeMapping.getGenericWorkorder().getId(),
						recipeMapping.getOperator(), String.valueOf(recipeMapping.getId()));
				}
				
				if (httpDlLinkData != null && !httpDlLinkData.isEmpty()) {
					YoutubeTestWrapper wrapper = ReportUtil.getHttpDownLinkData(httpDlLinkData);
					if (wrapper != null) {
						listYoutubeData.add(wrapper);
					}
				}
			} catch (BusinessException e) {
				logger.error("Exception in getYouTubeDataRecipeWise : {}", Utils.getStackTrace(e));
			}
//			logger.debug("List of listYoutubeData  {} ", listYoutubeData);
		}
		logger.info("listYoutubeData Data Size for workOrderId {}  , data {} ", mappings,
				listYoutubeData.size());
		return listYoutubeData;
	}

	private List<HandOverDataWrappr> getHandOverDataListFromDriveData(List<String[]> listArray, Map<String, Integer> kpiIndexMap) {
		logger.info("inside method getHandOverDataListFromDriveData ");
		List<HandOverDataWrappr> handOverList = new ArrayList<>();
		if (listArray != null && !listArray.isEmpty()) {
			for (String[] array : listArray) {
				setHandOverWrapperIntoList(handOverList, array, kpiIndexMap);
			}
		}
		logger.info("going to return HandOverDataList size {} ", handOverList.size());

		return handOverList;
	}

	private void setHandOverWrapperIntoList(List<HandOverDataWrappr> handOverList, String[] array, Map<String, Integer> kpiIndexMap) {
		try {
			if (array != null && array.length > INDEX_HO_DATA_STATUS) {
				HandOverDataWrappr wrapper = new HandOverDataWrappr();
				wrapper.setCaptureTime(ReportUtil.getFormattedDate(
						new Date(Long.parseLong(array[INDEX_HO_DATA_TIMESTAMP])),
						ReportConstants.DATE_FORMAT_DD_MM_YY_SS_AA));
				wrapper.setLatitude(array[INDEX_HO_DATA_LATITUDE]!=null?Double.parseDouble(array[INDEX_HO_DATA_LATITUDE]):0.0);
				wrapper.setLongitude(array[INDEX_HO_DATA_LONGITUDE]!=null?Double.parseDouble(array[INDEX_HO_DATA_LONGITUDE]):0.0);
				wrapper.setSourcePci(array[INDEX_HO_DATA_SOURCE_PCI]!=null?Integer.parseInt(array[INDEX_HO_DATA_SOURCE_PCI]):0);
				wrapper.setTargetPci(array[INDEX_HO_DATA_TARGET_PCI]!=null?Integer.parseInt(array[INDEX_HO_DATA_TARGET_PCI]):0);
				wrapper.setHoLatency(array[INDEX_HO_DATA_INTERRUPTION_TIME]);
				if (!StringUtils.isBlank(array[INDEX_HO_DATA_STATUS])
						&& TEST_STATUS_PASS
								.equalsIgnoreCase(array[INDEX_HO_DATA_STATUS])) {
					wrapper.setStatus(TEST_STATUS_PASS);
				} else {
					wrapper.setStatus(TEST_STATUS_FAIL);
					
				}
				handOverList.add(wrapper);
			}
		} catch (Exception e) {
			logger.error("Exception inside the method setHandOverWrapperIntoList {}", Utils.getStackTrace(e));
		}
	}

	private boolean getValidHandOver(String[] array, Map<String, Integer> kpiIndexMap) {
		return kpiIndexMap.get(TARGET_PCI) != null 
				&& kpiIndexMap.get(SOURCE_PCI) != null
				&& array.length >= kpiIndexMap.get(TARGET_PCI)
				&& array[kpiIndexMap.get(SOURCE_PCI)] != null
				&& array[kpiIndexMap.get(TARGET_PCI)] != null
				&& !array[kpiIndexMap.get(TARGET_PCI)].isEmpty()
				&& !array[kpiIndexMap.get(SOURCE_PCI)].isEmpty();
	}

	private void setConnectionSetupTime(String[] summaryData, Map<String, List<Double>> liveDriveKpiMap, Map<String, Integer> summaryKpiIndexMap) {
		List<Double> connectionSetupList = new ArrayList<>();
		try {
			Integer cstIndex = summaryKpiIndexMap.get(CONNECTION_SETUP_TIME);
			if(cstIndex != null) {
				logger.info("CST VALUE FOR {} ", (cstIndex));
				if (summaryData[cstIndex] != null) {
					connectionSetupList.add(NumberUtils.toDouble((summaryData[cstIndex])));
				}
			}
		} catch (Exception e) {
			logger.warn("Excaeption inside the method getCellIdList {}", e.getMessage());
		}
		logger.info("connectiosetupList data {}  ", connectionSetupList);
		liveDriveKpiMap.put(CONNECTION_SETUP_TIME, connectionSetupList);
	}

	private void getCellIdList(List<String[]> listArray, Map<String, List<Double>> liveDriveKpiMap, Map<String, Integer> kpiIndexMap) {
		List<Double> cellIdList = new ArrayList<>();
		try {
			for (String[] arr : listArray) {
				if (kpiIndexMap.get(CELL_ID) != null && arr[kpiIndexMap.get(CELL_ID)] != null) {
					cellIdList.add(NumberUtils.toDouble((arr[kpiIndexMap.get(CELL_ID)])));
				}
			}
		} catch (Exception e) {
			logger.warn("Exception inside the method getCellIdList {}", e.getMessage());
		}
		liveDriveKpiMap.put(CELLID, cellIdList);
	}

	private HashMap<String, String> getDriveImagesMap(List<String[]> listArray, List<KPIWrapper> kpiList, Map<String, Integer> kpiIndexMap)
			throws IOException {
		HashMap<String, String> imagemap = new HashMap<>();
		
		try {
			logger.info("Inside method getDriveImagesMap =============");
			List<SiteInformationWrapper> siteInfoList = reportService.getSiteDataForReportByDataList(listArray, kpiIndexMap);
			DriveImageWrapper driveImageWrapper = new DriveImageWrapper(listArray, kpiIndexMap.get(LATITUDE),
					kpiIndexMap.get(LONGITUDE), kpiIndexMap.get(PCI_PLOT), kpiList, siteInfoList, null);
			
			HashMap<String, BufferedImage> bufferdImageMap = reportService.getLegendImages(kpiList,
					driveImageWrapper.getDataKPIs(), kpiIndexMap.get(TEST_TYPE));
			
			bufferdImageMap.putAll(mapImageService.getDriveImagesForReport(driveImageWrapper, null, kpiIndexMap));
			String folder = ReportUtil.getFormattedDate(new Date(), DATE_FORMAT_DD_MM_YY_HH_SS)
					+ FORWARD_SLASH;
			imagemap = mapImageService.saveDriveImages(bufferdImageMap,
					ConfigUtils.getString(ConfigUtil.IMAGE_PATH_FOR_NV_REPORT) + LIVEDRIVE
							+ FORWARD_SLASH + folder,
					false);
			folder = ConfigUtils.getString(ConfigUtil.IMAGE_PATH_FOR_NV_REPORT) + LIVEDRIVE
					+ FORWARD_SLASH + folder;
			imagemap.put(ForesightConstants.TO_DELETED_KEY, folder);
		//	logger.info("image map is {}", new Gson().toJson(imagemap));

		} catch (Exception e) {
			logger.error("Exceoption inside method getDriveImagesMap {} ", Utils.getStackTrace(e));
		}
		return imagemap;

	}

	private LiveDriveReportWrapper setDataForLiveDriveReport(Map<String, List<Double>> liveDriveKpiMap,
			List<KPIWrapper> kpiList, LiveDriveReportWrapper driveReportWrapper,
			LiveDriveSubWrapper driveSubWrapper) {
		if (liveDriveKpiMap != null && liveDriveKpiMap.size() > 0) {
			String testDate = null;
			if(liveDriveKpiMap.containsKey(TIME) && Utils.isValidList(liveDriveKpiMap.get(TIME))) {
				long timeStamp = liveDriveKpiMap.get(TIME).get(0).longValue();
				Date date = new Date(timeStamp);
				SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_DD_MM_YY,
						Locale.ENGLISH);
				testDate = format.format(date);
			}
			Map<String, LiveDriveWrapper> livedrivewrappers = new LiveDriveReportUtil().setDataOnMap(kpiList,
					liveDriveKpiMap);
		//	logger.debug("livedrivewrappers : {}", new Gson().toJson(livedrivewrappers));
			setVoiceAndDataList(testDate, livedrivewrappers, driveSubWrapper, driveReportWrapper);
			driveSubWrapper.setUniqueCells((int) LiveDriveReportUtil.getUniqueCellId(liveDriveKpiMap));
			setRsrpAndSinrPlotData(driveReportWrapper, livedrivewrappers);
			setHttpDlAndUlPlotData(driveReportWrapper, livedrivewrappers);
			setFtpDlAndUlPlotData(driveReportWrapper, livedrivewrappers);
			setDlAndUlPlotData(driveReportWrapper, livedrivewrappers);
		//	logger.debug("driveReportWrapper data from method setDataForLiveDriveReport {}",
		//			new Gson().toJson(driveReportWrapper));
		}
		return driveReportWrapper;
	}

	private LiveDriveReportWrapper setVoiceAndDataList(String testDate, Map<String, LiveDriveWrapper> livedrivewrappers,
			LiveDriveSubWrapper driveSubWrapper, LiveDriveReportWrapper driveReportWrapper) {
		List<LiveDriveSubWrapper> driveSubWrappers = new ArrayList<>();
		try {
			driveReportWrapper.setTestDate(testDate);
			setVoiceDataList(driveSubWrapper, livedrivewrappers);
			if (setCHIData(livedrivewrappers) != null) {
				driveSubWrapper.setChi(String.valueOf(setCHIData(livedrivewrappers)));
			}
			if (setDHIData(livedrivewrappers) != null) {
				driveSubWrapper.setDhi(String.valueOf(setDHIData(livedrivewrappers)));
			}
			driveSubWrappers.add(driveSubWrapper);
			driveReportWrapper.setNhi2DataList(driveSubWrappers);
		} catch (Exception e) {
			logger.error("Exception inside method setVoiceAndDataList {} ", Utils.getStackTrace(e));
		}
		return driveReportWrapper;
	}

	private void goinToSetHttpLinkAndYoutubeData(LiveDriveSubWrapper driveSubWrapper,
			List<YoutubeTestWrapper> youtubeDataList, LiveDriveReportWrapper driveReportWrapper,
			List<YoutubeTestWrapper> listOfHttpDl) {
		logger.info("inside method goinToSetHttpLinkAndYoutubeData with youtubedatasize {} ",youtubeDataList.size());
		if (youtubeDataList != null && !youtubeDataList.isEmpty()) {
			driveSubWrapper.setYoutubeTestWrapperList(youtubeDataList);
			driveReportWrapper.setYouTubePage(TRUE);
		}
		if (listOfHttpDl != null && !listOfHttpDl.isEmpty()) {
			driveSubWrapper.setHttpLinkDlList(listOfHttpDl);
			driveReportWrapper.setHttpLinkDlPage(TRUE);
		}
	}

	private void goingToSetSmsAndCallData(LiveDriveSubWrapper driveSubWrapper, String[] summaryData,
			LiveDriveReportWrapper driveReportWrapper, Map<String, Integer> summaryKpiIndexMap) {
		List<LiveDriveVoiceAndSmsWrapper> smsList = ReportUtil.smsDataListForReport(summaryData, summaryKpiIndexMap);
		if (!smsList.isEmpty()) {
			driveReportWrapper.setSmsPage(TRUE);
			driveSubWrapper.setSmsDataList(smsList);
		}
		List<LiveDriveVoiceAndSmsWrapper> callList = ReportUtil.callDataListForReport(summaryData, summaryKpiIndexMap);
		if (!callList.isEmpty()) {
			driveReportWrapper.setCallPage(TRUE);
			driveSubWrapper.setCallDataList(callList);
		}
		List<LiveDriveVoiceAndSmsWrapper> callDataList = ReportUtil.getCallPlotDataListForReport(summaryData, summaryKpiIndexMap);
		driveSubWrapper.setCallPlotDataList(callDataList);
	}

	private void goingToSetHandoverData(LiveDriveSubWrapper driveSubWrapper, String[] summaryData,
			LiveDriveReportWrapper driveReportWrapper, List<HandOverDataWrappr> handOverList,
			List<HandOverDataWrappr> handOverIdelList, Map<String, Integer> summaryKpiIndexMap) {
		if (handOverIdelList != null && !handOverIdelList.isEmpty()) {
			driveSubWrapper.setHandOverIdelList(handOverIdelList);
		}
		List<LiveDriveVoiceAndSmsWrapper> handoverPlotDataList = ReportUtil.getHandoverPlotDataListForReport(summaryData, summaryKpiIndexMap);
//		logger.info("handover data list: {}", new Gson().toJson(handoverPlotDataList));
		driveSubWrapper.setHandoverPlotDataList(handoverPlotDataList);
		if (handOverList != null && !handOverList.isEmpty()) {
			driveSubWrapper.setHandOverList(handOverList);
			driveReportWrapper.setHandOverPage(TRUE);
			driveSubWrapper.setHandSummaryList(setHandOverSummary(summaryData, summaryKpiIndexMap));
		}
	}

	private List<HandOverDataWrappr> setHandOverSummary(String[] summaryData, Map<String, Integer> summaryKpiIndexMap) {
		List<HandOverDataWrappr> summaryList = new ArrayList<>();
		HandOverDataWrappr wrapper = new HandOverDataWrappr();
		try {
			if (summaryKpiIndexMap.get(SUM_UNDERSCORE+HANDOVER_INITIATE) != null  
					&& summaryKpiIndexMap.get(SUM_UNDERSCORE+HANDOVER_FAILURE) != null 
					&& summaryKpiIndexMap.get(SUM_UNDERSCORE+HANDOVER_SUCCESS) != null
					&& summaryData.length >= summaryKpiIndexMap.get(SUM_UNDERSCORE+HANDOVER_INITIATE)) {
				Integer total = NumberUtils.toInteger(summaryData[summaryKpiIndexMap.get(SUM_UNDERSCORE+HANDOVER_INITIATE)]);
				Integer fail = NumberUtils.toInteger(summaryData[summaryKpiIndexMap.get(SUM_UNDERSCORE+HANDOVER_FAILURE)]);
				Integer success = NumberUtils
						.toInteger(summaryData[summaryKpiIndexMap.get(SUM_UNDERSCORE+HANDOVER_SUCCESS)]);
				wrapper.setTotalHO(total);
				wrapper.setNoOfFailHO(fail);
				wrapper.setNoOfSucessHO(success);
				wrapper.setSuccessPercent(
						ReportUtil.getPercentage(success, total) + SPACE + PERCENT);
				wrapper.setFailPercent(
						ReportUtil.getPercentage(fail, total) + SPACE + PERCENT);
				summaryList.add(wrapper);
			}
		} catch (Exception e) {
			logger.error("Exception to setHandOverSummary {} ", e.getMessage());
		}

		return summaryList;
	}

	private LiveDriveSubWrapper setVoiceDataList(LiveDriveSubWrapper driveSubWrapper,
			Map<String, LiveDriveWrapper> livedrivewrappers) {
		List<Object> dataKPisList = Arrays.asList(
				/* UL_THROUGHPUT, */FTP_UL_THROUGHPUT,
				HTTP_UL_THROUGHPUT,
				/* DL_THROUGHPUT, */FTP_DL_THROUGHPUT,
				HTTP_DL_THROUGHPUT, WEB_DOWNLOAD_DELAY, JITTER,
				LATENCY, CONNECTION_SETUP_TIME);
		List<Object> voiceKpilist = Arrays.asList(RSRP, SINR, RSRQ,
				RSSI);
		driveSubWrapper
				.setDataList(setDataVoiceList(livedrivewrappers, dataKPisList, HEADER_NAME_DATA));
		driveSubWrapper
				.setVoiceList(setDataVoiceList(livedrivewrappers, voiceKpilist, HEADER_NAME_COVERAGE));
		return driveSubWrapper;
	}

	private List<KPIStatsWrapper> setDataVoiceList(Map<String, LiveDriveWrapper> livedrivewrappers,
			List<Object> listOfKPi, String headerName) {
		logger.debug("Inside method setDataList for KPiList {} ", listOfKPi);
		List<KPIStatsWrapper> dataList = new ArrayList<>();
		KPIStatsWrapper kpistatswrapper = null;
		LiveDriveWrapper driveWrapper = null;
		List<String> kpiNamelist = listOfKPi.stream().map(String::valueOf).collect(Collectors.toList());
		for (String kpiName : kpiNamelist) {
			try {
				driveWrapper = livedrivewrappers.get(kpiName);
				if (driveWrapper != null) {
					kpistatswrapper = new KPIStatsWrapper();
					kpistatswrapper.setMin(driveWrapper.getMin());
					kpistatswrapper.setMax(driveWrapper.getMax());
					kpistatswrapper.setMean(driveWrapper.getMean());
					kpistatswrapper
							.setKPI(driveWrapper.getKPI().replace(UNDERSCORE, SPACE));
					kpistatswrapper.setPer90(driveWrapper.getPer90());
					kpistatswrapper.setStdev(driveWrapper.getStdev());
					kpistatswrapper.setTableHeader(headerName);
					dataList.add(kpistatswrapper);
				}
			} catch (Exception e) {
				logger.error("Exception Inside method setDataList {} ", Utils.getStackTrace(e));
			}
		}
		logger.info("Going to return the voice list for above kpi list {} ", dataList);
		return dataList;
	}

	private LiveDriveChartWrapper setChartDataListLeftForRSRP(Map<String, LiveDriveWrapper> livedrivewrappers) {
		LiveDriveChartWrapper driveChartWrapper = new LiveDriveChartWrapper();
		try {
			LiveDriveWrapper driveWrapper = livedrivewrappers.get(KPI.RSRP.toString());
			driveChartWrapper.setChartTitle(driveWrapper.getChartTitle());
			driveChartWrapper.setChartType(driveWrapper.getChartType());
			driveChartWrapper.setKpi(driveWrapper.getKPI());
			driveChartWrapper.setGraphHeading(driveWrapper.getGraphHeading());
			driveChartWrapper.setMinKPI(driveWrapper.getMin());
			driveChartWrapper.setMaxKPI(driveWrapper.getMax());
			driveChartWrapper.setMean(driveWrapper.getMean());
			driveChartWrapper.setMedKPI(driveWrapper.getMedKPI());
			driveChartWrapper.setStDev(driveWrapper.getStdev());
			driveChartWrapper.setPer90KPI(driveWrapper.getPer90());
			driveChartWrapper.setPer10KPI(driveWrapper.getPer10());
			driveChartWrapper.setChartList(driveWrapper.getChartList());
			driveChartWrapper.setScore(driveWrapper.getScore());
			driveChartWrapper.setRangeScoreWrapperList(driveWrapper.getRangeScoreWrapperList());
			driveChartWrapper.setKPIUnit(driveWrapper.getKPI());
			driveChartWrapper.setCategoryAxisLabel(driveWrapper.getKPI());
			driveChartWrapper.setValueAxisLabel(CDF_PDF);
		} catch (Exception e) {
			logger.error("error inside the method setChartDataListLeftForRSRP {}", e.getMessage());
		}
		return driveChartWrapper;
	}

	private LiveDriveChartWrapper setChartDataListRightForSINR(Map<String, LiveDriveWrapper> livedrivewrappers) {
		LiveDriveChartWrapper driveChartWrapper = new LiveDriveChartWrapper();

		try {
			LiveDriveWrapper driveWrapper = livedrivewrappers.get(KPI.SINR.toString());
			if (driveWrapper != null) {
				driveChartWrapper.setChartTitle(driveWrapper.getChartTitle());
				driveChartWrapper.setChartType(driveWrapper.getChartType());
				driveChartWrapper.setKpi(driveWrapper.getKPI());
				driveChartWrapper.setGraphHeading(driveWrapper.getGraphHeading());
				driveChartWrapper.setMinKPI(driveWrapper.getMin());
				driveChartWrapper.setMaxKPI(driveWrapper.getMax());
				driveChartWrapper.setMean(driveWrapper.getMean());
				driveChartWrapper.setMedKPI(driveWrapper.getMedKPI());
				driveChartWrapper.setStDev(driveWrapper.getStdev());
				driveChartWrapper.setPer90KPI(driveWrapper.getPer90());
				driveChartWrapper.setPer10KPI(driveWrapper.getPer10());
				driveChartWrapper.setChartList(driveWrapper.getChartList());
				driveChartWrapper.setScore(driveWrapper.getScore());
				driveChartWrapper.setRangeScoreWrapperList(driveWrapper.getRangeScoreWrapperList());
				driveChartWrapper.setKPIUnit(driveWrapper.getKPI());
				driveChartWrapper.setCategoryAxisLabel(driveWrapper.getKPI());
				driveChartWrapper.setValueAxisLabel(CDF_PDF);
			}
		} catch (Exception e) {
			logger.error("Exception inside the method setChartDataListRightForSINR {}", Utils.getStackTrace(e));

		}

		return driveChartWrapper;
	}

	private LiveDriveChartWrapper setChartDataListLeftFORDL(Map<String, LiveDriveWrapper> livedrivewrappers) {
		LiveDriveChartWrapper driveChartWrapper = new LiveDriveChartWrapper();
		try {
			LiveDriveWrapper driveWrapper = livedrivewrappers.get(KPI.DL.toString());
			driveChartWrapper = new LiveDriveChartWrapper();
			driveChartWrapper.setChartTitle(driveWrapper.getChartTitle());
			driveChartWrapper.setChartType(driveWrapper.getChartType());
			driveChartWrapper.setKpi(driveWrapper.getKPI());
			driveChartWrapper.setGraphHeading(driveWrapper.getGraphHeading());
			driveChartWrapper.setMinKPI(driveWrapper.getMin());
			driveChartWrapper.setMaxKPI(driveWrapper.getMax());
			driveChartWrapper.setMean(driveWrapper.getMean());
			driveChartWrapper.setMedKPI(driveWrapper.getMedKPI());
			driveChartWrapper.setStDev(driveWrapper.getStdev());
			driveChartWrapper.setPer90KPI(driveWrapper.getPer90());
			driveChartWrapper.setPer10KPI(driveWrapper.getPer10());
			driveChartWrapper.setChartList(driveWrapper.getChartList());
			driveChartWrapper.setScore(driveWrapper.getScore());
			driveChartWrapper.setRangeScoreWrapperList(driveWrapper.getRangeScoreWrapperList());
			driveChartWrapper.setKPIUnit(DL_THROUGHPUT + MBPS);
			driveChartWrapper.setCategoryAxisLabel(DL_THROUGHPUT + MBPS);
			driveChartWrapper.setValueAxisLabel(CDF_PDF);
		} catch (Exception e) {
			logger.error("Exception inside them method setChartDataListLeftFORDL{}", e.getMessage());
		}
		return driveChartWrapper;
	}

	private LiveDriveChartWrapper setChartDataListLeftFORHTTPDL(Map<String, LiveDriveWrapper> livedrivewrappers) {
		LiveDriveChartWrapper driveChartWrapper = new LiveDriveChartWrapper();
		try {
			LiveDriveWrapper driveWrapper = livedrivewrappers.get(HTTP_DL_THROUGHPUT);
			if (driveWrapper != null) {
				driveChartWrapper.setChartTitle(driveWrapper.getChartTitle());
				driveChartWrapper.setChartType(driveWrapper.getChartType());
				driveChartWrapper.setKpi(driveWrapper.getKPI());
				driveChartWrapper.setGraphHeading(driveWrapper.getGraphHeading());
				driveChartWrapper.setMinKPI(driveWrapper.getMin());
				driveChartWrapper.setMaxKPI(driveWrapper.getMax());
				driveChartWrapper.setMean(driveWrapper.getMean());
				driveChartWrapper.setMedKPI(driveWrapper.getMedKPI());
				driveChartWrapper.setStDev(driveWrapper.getStdev());
				driveChartWrapper.setPer90KPI(driveWrapper.getPer90());
				driveChartWrapper.setPer10KPI(driveWrapper.getPer10());
				driveChartWrapper.setChartList(driveWrapper.getChartList());
				driveChartWrapper.setScore(driveWrapper.getScore());
				driveChartWrapper.setRangeScoreWrapperList(driveWrapper.getRangeScoreWrapperList());
				driveChartWrapper.setKPIUnit(DL_THROUGHPUT + MBPS);
				driveChartWrapper.setCategoryAxisLabel(DL_THROUGHPUT + MBPS);
				driveChartWrapper.setValueAxisLabel(CDF_PDF);
			}
		} catch (Exception e) {
			logger.error("Exception inside the method {}", Utils.getStackTrace(e));
		}
		return driveChartWrapper;
	}

	private LiveDriveChartWrapper setChartDataListLeftFORFTPDL(Map<String, LiveDriveWrapper> livedrivewrappers) {
		LiveDriveChartWrapper driveChartWrapper = new LiveDriveChartWrapper();

		try {
			LiveDriveWrapper driveWrapper = livedrivewrappers.get(FTP_DL_THROUGHPUT);
			if (driveWrapper != null) {
				driveChartWrapper.setChartTitle(driveWrapper.getChartTitle());
				driveChartWrapper.setChartType(driveWrapper.getChartType());
				driveChartWrapper.setKpi(driveWrapper.getKPI());
				driveChartWrapper.setGraphHeading(driveWrapper.getGraphHeading());
				driveChartWrapper.setMinKPI(driveWrapper.getMin());
				driveChartWrapper.setMaxKPI(driveWrapper.getMax());
				driveChartWrapper.setMean(driveWrapper.getMean());
				driveChartWrapper.setMedKPI(driveWrapper.getMedKPI());
				driveChartWrapper.setStDev(driveWrapper.getStdev());
				driveChartWrapper.setPer90KPI(driveWrapper.getPer90());
				driveChartWrapper.setPer10KPI(driveWrapper.getPer10());
				driveChartWrapper.setChartList(driveWrapper.getChartList());
				driveChartWrapper.setScore(driveWrapper.getScore());
				driveChartWrapper.setRangeScoreWrapperList(driveWrapper.getRangeScoreWrapperList());
				driveChartWrapper.setKPIUnit(DL_THROUGHPUT + MBPS);
				driveChartWrapper.setCategoryAxisLabel(DL_THROUGHPUT + MBPS);
				driveChartWrapper.setValueAxisLabel(CDF_PDF);
			}
		} catch (Exception e) {
			logger.error("Exception inside the method {}", Utils.getStackTrace(e));
		}
		return driveChartWrapper;
	}

	private LiveDriveChartWrapper setChartDataListRightFORUL(Map<String, LiveDriveWrapper> livedrivewrappers) {

		LiveDriveWrapper driveWrapper = livedrivewrappers.get(KPI.UL.toString());
		LiveDriveChartWrapper driveChartWrapper = new LiveDriveChartWrapper();
		if (driveWrapper != null) {
			driveChartWrapper = new LiveDriveChartWrapper();
			driveChartWrapper.setChartTitle(driveWrapper.getChartTitle());
			driveChartWrapper.setChartType(driveWrapper.getChartType());
			driveChartWrapper.setKpi(driveWrapper.getKPI());
			driveChartWrapper.setGraphHeading(driveWrapper.getGraphHeading());
			driveChartWrapper.setMinKPI(driveWrapper.getMin());
			driveChartWrapper.setMaxKPI(driveWrapper.getMax());
			driveChartWrapper.setMean(driveWrapper.getMean());
			driveChartWrapper.setMedKPI(driveWrapper.getMedKPI());
			driveChartWrapper.setStDev(driveWrapper.getStdev());
			driveChartWrapper.setPer90KPI(driveWrapper.getPer90());
			driveChartWrapper.setPer10KPI(driveWrapper.getPer10());
			driveChartWrapper.setChartList(driveWrapper.getChartList());
			driveChartWrapper.setScore(driveWrapper.getScore());
			driveChartWrapper.setRangeScoreWrapperList(driveWrapper.getRangeScoreWrapperList());
			driveChartWrapper.setKPIUnit(UL_THROUGHPUT + MBPS);
			driveChartWrapper.setCategoryAxisLabel(UL_THROUGHPUT + MBPS);
			driveChartWrapper.setValueAxisLabel(CDF_PDF);
		}
		return driveChartWrapper;
	}

	private LiveDriveChartWrapper setChartDataListRightFORHTTPUL(Map<String, LiveDriveWrapper> livedrivewrappers) {
		LiveDriveChartWrapper driveChartWrapper = new LiveDriveChartWrapper();

		try {
			LiveDriveWrapper driveWrapper = livedrivewrappers.get(HTTP_UL_THROUGHPUT);
			if (driveWrapper != null) {
				driveChartWrapper = new LiveDriveChartWrapper();
				driveChartWrapper.setChartTitle(driveWrapper.getChartTitle());
				driveChartWrapper.setChartType(driveWrapper.getChartType());
				driveChartWrapper.setKpi(driveWrapper.getKPI());
				driveChartWrapper.setGraphHeading(driveWrapper.getGraphHeading());
				driveChartWrapper.setMinKPI(driveWrapper.getMin());
				driveChartWrapper.setMaxKPI(driveWrapper.getMax());
				driveChartWrapper.setMean(driveWrapper.getMean());
				driveChartWrapper.setMedKPI(driveWrapper.getMedKPI());
				driveChartWrapper.setStDev(driveWrapper.getStdev());
				driveChartWrapper.setPer90KPI(driveWrapper.getPer90());
				driveChartWrapper.setPer10KPI(driveWrapper.getPer10());
				driveChartWrapper.setChartList(driveWrapper.getChartList());
				driveChartWrapper.setScore(driveWrapper.getScore());
				driveChartWrapper.setRangeScoreWrapperList(driveWrapper.getRangeScoreWrapperList());
				driveChartWrapper.setKPIUnit(UL_THROUGHPUT + MBPS);
				driveChartWrapper.setCategoryAxisLabel(UL_THROUGHPUT + MBPS);
				driveChartWrapper.setValueAxisLabel(CDF_PDF);
			}
			return driveChartWrapper;
		} catch (Exception e) {
			logger.info("Error inside the method setChartDataListRightFORHTTPUL {}", Utils.getStackTrace(e));
		}
		return driveChartWrapper;
	}

	private LiveDriveChartWrapper setChartDataListRightFORFTPUL(Map<String, LiveDriveWrapper> livedrivewrappers) {
		LiveDriveChartWrapper driveChartWrapper = new LiveDriveChartWrapper();

		try {
			LiveDriveWrapper driveWrapper = livedrivewrappers.get(FTP_UL_THROUGHPUT);
			if (driveWrapper != null) {
				driveChartWrapper = new LiveDriveChartWrapper();
				driveChartWrapper.setChartTitle(driveWrapper.getChartTitle());
				driveChartWrapper.setChartType(driveWrapper.getChartType());
				driveChartWrapper.setKpi(driveWrapper.getKPI());
				driveChartWrapper.setGraphHeading(driveWrapper.getGraphHeading());
				driveChartWrapper.setMinKPI(driveWrapper.getMin());
				driveChartWrapper.setMaxKPI(driveWrapper.getMax());
				driveChartWrapper.setMean(driveWrapper.getMean());
				driveChartWrapper.setMedKPI(driveWrapper.getMedKPI());
				driveChartWrapper.setStDev(driveWrapper.getStdev());
				driveChartWrapper.setPer90KPI(driveWrapper.getPer90());
				driveChartWrapper.setPer10KPI(driveWrapper.getPer10());
				driveChartWrapper.setChartList(driveWrapper.getChartList());
				driveChartWrapper.setScore(driveWrapper.getScore());
				driveChartWrapper.setRangeScoreWrapperList(driveWrapper.getRangeScoreWrapperList());
				driveChartWrapper.setKPIUnit(UL_THROUGHPUT + MBPS);
				driveChartWrapper.setCategoryAxisLabel(UL_THROUGHPUT + MBPS);
				driveChartWrapper.setValueAxisLabel(CDF_PDF);
				return driveChartWrapper;
			}
		} catch (Exception e) {
			logger.error("Error inside the method setChartDataListRightFORHTTPUL {}", Utils.getStackTrace(e));
		}
		return driveChartWrapper;
	}

	private void setDlAndUlPlotData(LiveDriveReportWrapper driveReportWrapper,
			Map<String, LiveDriveWrapper> livedrivewrappers) {
		try {
			List<LiveDriveSubWrapper> dlUlChartBeanList = new ArrayList<>();
			LiveDriveSubWrapper driveSubWrapper = new LiveDriveSubWrapper();
			List<LiveDriveChartWrapper> chartDataListLeftForDL = new ArrayList<>();
			chartDataListLeftForDL.add(setChartDataListLeftFORDL(livedrivewrappers));
			driveSubWrapper.setChartDataListLeft(chartDataListLeftForDL);

			List<LiveDriveChartWrapper> chartDataListRightForUL = new ArrayList<>();
			chartDataListRightForUL.add(setChartDataListRightFORUL(livedrivewrappers));
			driveSubWrapper.setChartDataListRight(chartDataListRightForUL);
			dlUlChartBeanList.add(driveSubWrapper);
			driveReportWrapper.setDlUlChartBeanList(dlUlChartBeanList);
		} catch (Exception e) {
			logger.error("Exception inside the Method  setDlAndUlPlotData {}", Utils.getStackTrace(e));
		}
	}

	private void setHttpDlAndUlPlotData(LiveDriveReportWrapper driveReportWrapper,
			Map<String, LiveDriveWrapper> livedrivewrappers) {
		try {
			List<LiveDriveSubWrapper> dlUlChartBeanList = new ArrayList<>();
			LiveDriveSubWrapper driveSubWrapper = new LiveDriveSubWrapper();
			List<LiveDriveChartWrapper> chartDataListLeftForDL = new ArrayList<>();
			chartDataListLeftForDL.add(setChartDataListLeftFORHTTPDL(livedrivewrappers));
			driveSubWrapper.setChartDataListLeft(chartDataListLeftForDL);

			List<LiveDriveChartWrapper> chartDataListRightForUL = new ArrayList<>();
			chartDataListRightForUL.add(setChartDataListRightFORHTTPUL(livedrivewrappers));
			driveSubWrapper.setChartDataListRight(chartDataListRightForUL);
			dlUlChartBeanList.add(driveSubWrapper);
			driveReportWrapper.setDlUlHttpChartBeanList(dlUlChartBeanList);
		} catch (Exception e) {
			logger.error("Exception inside them metheo setHttpDlAndUlPlotData{}", Utils.getStackTrace(e));
		}
	}

	private void setFtpDlAndUlPlotData(LiveDriveReportWrapper driveReportWrapper,
			Map<String, LiveDriveWrapper> livedrivewrappers) {
		try {
			List<LiveDriveSubWrapper> dlUlChartBeanList = new ArrayList<>();
			LiveDriveSubWrapper driveSubWrapper = new LiveDriveSubWrapper();
			List<LiveDriveChartWrapper> chartDataListLeftForDL = new ArrayList<>();
			chartDataListLeftForDL.add(setChartDataListLeftFORFTPDL(livedrivewrappers));
			driveSubWrapper.setChartDataListLeft(chartDataListLeftForDL);

			List<LiveDriveChartWrapper> chartDataListRightForUL = new ArrayList<>();
			chartDataListRightForUL.add(setChartDataListRightFORFTPUL(livedrivewrappers));
			driveSubWrapper.setChartDataListRight(chartDataListRightForUL);
			dlUlChartBeanList.add(driveSubWrapper);
			driveReportWrapper.setDlUlFtpChartBeanList(dlUlChartBeanList);
		} catch (Exception e) {
			logger.error("Exception inside them metheo setHttpDlAndUlPlotData{}", Utils.getStackTrace(e));
		}
	}

	private void setRsrpAndSinrPlotData(LiveDriveReportWrapper driveReportWrapper,
			Map<String, LiveDriveWrapper> livedrivewrappers) {
		try {
			logger.debug("INside setRsrpAndSinrPlotData : {},\n\n\n\n  {}", driveReportWrapper, livedrivewrappers);
			LiveDriveSubWrapper driveSubWrapper = new LiveDriveSubWrapper();
			List<LiveDriveSubWrapper> rsrpSinrChartBeanList = new ArrayList<>();
			List<LiveDriveChartWrapper> chartDataListLeftForRSRP = new ArrayList<>();
			chartDataListLeftForRSRP.add(setChartDataListLeftForRSRP(livedrivewrappers));
			driveSubWrapper.setChartDataListLeft(chartDataListLeftForRSRP);

			List<LiveDriveChartWrapper> chartDataListRightForSINR = new ArrayList<>();
			chartDataListRightForSINR.add(setChartDataListRightForSINR(livedrivewrappers));
			driveSubWrapper.setChartDataListRight(chartDataListRightForSINR);
			rsrpSinrChartBeanList.add(driveSubWrapper);
			driveReportWrapper.setRsrpSinrChartBeanList(rsrpSinrChartBeanList);
		} catch (Exception e) {
			logger.error("Exception inside the method setRsrpAndSinrPlotData {}", Utils.getStackTrace(e));
		}
	}

	private Double setCHIData(Map<String, LiveDriveWrapper> livedrivewrappers) {
		return LiveDriveReportUtil.calculateCHI(livedrivewrappers, NETWORK_4G);
	}

	private Double setDHIData(Map<String, LiveDriveWrapper> livedrivewrappers) {
		return LiveDriveReportUtil.calculateDHI(livedrivewrappers);
	}

	private File proceedToCreateLiveDriveReport(Map<String, Object> imageMap, LiveDriveReportWrapper mainWrapper,
			boolean isMasterResport, HashMap<String, String> imagemap) {
		logger.info("inside the methid proceedToCreateLiveDriveReport ");
		try {
			String reportAssetRepo = null;

			if (isMasterResport) {
				reportAssetRepo = ConfigUtils.getString(NVConfigUtil.GET_LIVE_DRIVE_JASPER_FOR_MASTER);

			} else {
				reportAssetRepo = ConfigUtils.getString(NVConfigUtil.GET_LIVE_DRIVE_JASPER);
			}
			logger.info("REPORT_ASSET_REPO {}", reportAssetRepo);
			List<LiveDriveReportWrapper> dataSourceList = new ArrayList<>();
			dataSourceList.add(mainWrapper);
			JRBeanCollectionDataSource rfbeanColDataSource = new JRBeanCollectionDataSource(dataSourceList);
			imageMap.put(SUBREPORT_DIR, reportAssetRepo);
			imageMap.put(IMAGE_PARAM_HEADER_LOGO, reportAssetRepo +IMAGE_HEADER_LOGO);
			imageMap.put(IMAGE_PARAM_HEADER_BG, reportAssetRepo + IMAGE_HEADER_BG);
			imageMap.put(IMAGE_PARAM_HEADER_LOG, reportAssetRepo + IMAGE_HEADER_LOG);
			imageMap.put(IMAGE_PARAM_SCREEN_LOG, reportAssetRepo + IMAGE_SCREEN_LOGO);
			imageMap.put(IMAGE_PARAM_SCREEN_BG, reportAssetRepo + IMAGE_SCREEN_BG);
			String destinationFileName = mainWrapper.getFileName();
			logger.info("imageMap" + new Gson().toJson(imageMap));

			logger.info("DATA TO POST ON JASPER LIVEDRIVE {} ", new Gson().toJson(dataSourceList));
			JasperRunManager.runReportToPdfFile(reportAssetRepo + MAIN_FILE_NAME, destinationFileName, imageMap,
					rfbeanColDataSource);

			logger.info("Finally Report is created for fileName {} ", mainWrapper.getFileName());

//			if (imagemap.get(ForesightConstants.TO_DELETED_KEY) != null) {
//				ReportUtils.deleteGeneratedFile(imagemap.get(ForesightConstants.TO_DELETED_KEY));
//			} else {
//				logger.info("Nothing to delete");
//			}

			return ReportUtil.getIfFileExists(destinationFileName);
		} catch (Exception e) {
			logger.error("@proceedToCreateLiveDriveReport getting err={}" + Utils.getStackTrace(e));
		}
		logger.info(
				"@proceedToCreateLiveDriveReport going to return null as there has been some problem in generating report");
		return null;
	}

	private HashMap<String, Object> prepareImageMap(HashMap<String, String> imagemap, Map<String, Integer> kpiIndexMap) {
		logger.info("inside the method prepareImageMap");
		Map<String, Object> map = new HashMap<>();
		try {
			map.put(IMAGE_RSRP, imagemap.get(kpiIndexMap.get(RSRP) + ""));
			map.put(IMAGE_RSRP_LEGEND, imagemap
					.get(LEGEND + UNDERSCORE + kpiIndexMap.get(RSRP)));

			map.put(IMAGE_SINR, imagemap.get(kpiIndexMap.get(SINR) + ""));
			map.put(IMAGE_SINR_LEGEND, imagemap
					.get(LEGEND + UNDERSCORE + kpiIndexMap.get(SINR)));
			map.put(IMAGE_DL, imagemap.get(DL_THROUGHPUT + ""));
			map.put(HTTP_IMAGE_DL, imagemap.get(kpiIndexMap.get(HTTP_DL_THROUGHPUT) + ""));
			map.put(FTP_IMAGE_DL, imagemap.get(kpiIndexMap.get(FTP_DL_THROUGHPUT) + ""));

			map.put(IMAGE_FTP_DL_LEGEND, imagemap.get(
					LEGEND + UNDERSCORE + kpiIndexMap.get(FTP_DL_THROUGHPUT)));
			map.put(IMAGE_HTTP_DL_LEGEND, imagemap.get(
					LEGEND + UNDERSCORE + kpiIndexMap.get(HTTP_DL_THROUGHPUT)));
			map.put(IMAGE_DL_LEGEND,
					imagemap.get(LEGEND + UNDERSCORE + kpiIndexMap.get(DL_THROUGHPUT)));

			map.put(IMAGE_UL, imagemap.get(kpiIndexMap.get(UL_THROUGHPUT) + ""));
			map.put(HTTP_IMAGE_UL, imagemap.get(kpiIndexMap.get(HTTP_UL_THROUGHPUT) + ""));
			map.put(FTP_IMAGE_UL, imagemap.get(kpiIndexMap.get(FTP_UL_THROUGHPUT) + ""));

			map.put(IMAGE_UL_LEGEND,
					imagemap.get(LEGEND + UNDERSCORE + kpiIndexMap.get(UL_THROUGHPUT)));
			map.put(IMAGE_HTTP_UL_LEGEND, imagemap.get(
					LEGEND + UNDERSCORE + kpiIndexMap.get(HTTP_UL_THROUGHPUT)));

			map.put(IMAGE_FTP_UL_LEGEND, imagemap.get(
					LEGEND + UNDERSCORE + kpiIndexMap.get(FTP_UL_THROUGHPUT)));

			map.put(IMAGE_PCI, imagemap.get(kpiIndexMap.get(PCI_PLOT) + ""));
			map.put(NHI, imagemap.get(SATELLITE_VIEW3));
			map.put(IMAGE_PCI_LEGEND, imagemap.get(PCI_LEGEND));
			map.put(IMAGE_CELL, imagemap.get(kpiIndexMap.get(PCI_PLOT) + ""));
			map.put(IMAGE_CELL_LEGEND, imagemap.get(PCI_LEGEND));
			map.put(IMAGE_HANDOVER, imagemap.get(kpiIndexMap.get(PCI_PLOT) + ""));
			map.put(CALL_PLOT, imagemap.get(kpiIndexMap.get(CALL_PLOT) + ""));
			map.put(HANDOVER_PLOT, imagemap.get(kpiIndexMap.get(HANDOVER_PLOT) + ""));
			map.put(IMAGE_CQI, imagemap.get(kpiIndexMap.get(CQI) + ""));
			map.put(IMAGE_CQI_LEGEND,
					imagemap.get(LEGEND + UNDERSCORE + kpiIndexMap.get(CQI)));
			map.put(IMAGE_RI, imagemap.get(kpiIndexMap.get(RI) + ""));
			map.put(IMAGE_RI_LEGEND,
					imagemap.get(LEGEND + UNDERSCORE + kpiIndexMap.get(RI)));
			map.put(IMAGE_FC, imagemap.get(kpiIndexMap.get(DL_EARFCN) + ""));
			map.put(IMAGE_FC_LEGEND, imagemap.get(DL_EARFCN));
			map.put(IMAGE_IDEL, imagemap.get(kpiIndexMap.get(IDLE_PLOT).toString()));

			map.put(IMAGE_SERVING_CELL,
					imagemap.get(kpiIndexMap.get(SERVING_CELL).toString()));
			map.put(IMAGE_SERVING_CELL_LEGEND, imagemap.get(
					LEGEND + UNDERSCORE + kpiIndexMap.get(SERVING_CELL)));
			map.put(LEGEND_TECHNOLOGY, imagemap.get(LEGEND_TECHNOLOGY));
			map.put(DriveHeaderConstants.TECHNOLOGY, imagemap.get(kpiIndexMap.get(DriveHeaderConstants.TECHNOLOGY)));

		} catch (Exception e) {
			logger.error("Error inside the method prepareImageMap{}", Utils.getStackTrace(e));
		}
//		logger.info("prepareImageMap {}", new Gson().toJson(map));
		return (HashMap<String, Object>) map;
	}

	private List<YoutubeTestWrapper> getYouTubeData(List<WORecipeMapping> mappings, Map<String, List<String>> map) {
		List<String> listRecipe = map.get(QMDLConstant.RECIPE);
		String youTubeData = null;
		List<YoutubeTestWrapper> listYoutubeData = new ArrayList<>();
		logger.info("getHttpDownLinkData Recipe List Details : Recipes {} ", listRecipe.toArray());
		for (WORecipeMapping recipeMapping : mappings) {
			try {
				if(listRecipe.contains(String.valueOf(recipeMapping.getId()))){
				youTubeData = nvLayer3DashboardService.getYoutubeDataFromLayer3Report(recipeMapping.getGenericWorkorder().getId(),
						recipeMapping.getOperator(), String.valueOf(recipeMapping.getId()));
				}
			
				if (youTubeData != null && !youTubeData.isEmpty()) {
					listYoutubeData.addAll(ReportUtil.getYouTubeTestDataWrapper(youTubeData));
				}
			} catch (BusinessException e) {
				logger.error("Exception in getYouTubeDataRecipeWise : {}", Utils.getStackTrace(e));
			}
		}
		logger.info("listYoutubeData Data Size for workOrderId {} , is  {} , data {} ", mappings,
				listYoutubeData.size(), listYoutubeData);
		return listYoutubeData;
	}
	
	
	

	@Override
	public List<HandOverDataWrappr> getHandoverCauseData(List<WORecipeMapping> mappings, Map<String, List<String>> map) {
		List<String> listRecipe = map.get(RECIPE);
		String handoverData = null;
		List<HandOverDataWrappr> handoverCauseList = new ArrayList<>();
		logger.info("getHttpDownLinkData Recipe List Details : Recipes {} ", listRecipe.toArray());
		for (WORecipeMapping recipeMapping : mappings) {
			try {
				if(listRecipe.contains(String.valueOf(recipeMapping.getId()))){
				handoverData = nvLayer3DashboardService.getHandoverDataFromHbase(recipeMapping.getGenericWorkorder().getId(),recipeMapping.getOperator(),
						String.valueOf(recipeMapping.getId()));
				}
				
				if (handoverData != null && !handoverData.isEmpty()) {
					handoverCauseList.addAll(getHandoverData(handoverData));
				}
			} catch (BusinessException e) {
				logger.error("Exception in getHandoverCauseData : {}", Utils.getStackTrace(e));
			}
		}
		logger.info("handoverCauseList Data Size for workOrderId {} , is  {} , data {} ", mappings,
				handoverCauseList.size(), handoverCauseList);
		return handoverCauseList;
	}

	private List<HandOverDataWrappr> getHandoverData(String handoverData) {
//		logger.info("handoverData {}", handoverData);
		List<HandOverDataWrappr> list = new ArrayList<>();
		List<String[]> data = ReportUtil.convertCSVStringToDataList(handoverData);
		data.stream().filter(
				d -> (d != null && d[INDEX_ZER0] != null && d[INDEX_ONE] != null))
				.forEach(arr -> {
					HandOverDataWrappr wrapper = new HandOverDataWrappr();
					wrapper.setPcis(
							arr[INDEX_ZER0].replace(UNDERSCORE, HIPHEN)
									.replace(PIPE, HIPHEN));
					wrapper.setCause(arr[INDEX_ONE]);
					list.add(wrapper);
				});
		return list;
	}
}

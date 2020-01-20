package com.inn.foresight.module.nv.report.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inn.commons.Symbol;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.io.FileUtils;
import com.inn.foresight.core.generic.utils.DateUtil;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.maplayer.service.IGenericMapService;
import com.inn.foresight.core.maplayer.utils.GenericMapUtils;
import com.inn.foresight.core.report.dao.IAnalyticsRepositoryDao;
import com.inn.foresight.core.report.model.AnalyticsRepository.progress;
import com.inn.foresight.module.nv.core.workorder.dao.impl.IGenericWorkorderDao;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;
import com.inn.foresight.module.nv.layer3.constants.NVLayer3Constants;
import com.inn.foresight.module.nv.layer3.service.INVLayer3DashboardService;
import com.inn.foresight.module.nv.layer3.utils.NVLayer3Utils;
import com.inn.foresight.module.nv.report.constant.ReportIndexWrapper;
import com.inn.foresight.module.nv.report.constant.ReportSummaryIndexWrapper;
import com.inn.foresight.module.nv.report.service.IClotReportService;
import com.inn.foresight.module.nv.report.service.IMapImagesService;
import com.inn.foresight.module.nv.report.service.IReportService;
import com.inn.foresight.module.nv.report.utils.ReportConstants;
import com.inn.foresight.module.nv.report.utils.ReportUtil;
import com.inn.foresight.module.nv.report.workorder.wrapper.DriveImageWrapper;
import com.inn.foresight.module.nv.report.workorder.wrapper.KPIWrapper;
import com.inn.foresight.module.nv.report.wrapper.GraphDataWrapper;
import com.inn.foresight.module.nv.report.wrapper.KPISummaryDataWrapper;
import com.inn.foresight.module.nv.report.wrapper.ReportSubWrapper;
import com.inn.foresight.module.nv.report.wrapper.clot.ClotReportSubWrapper;
import com.inn.foresight.module.nv.report.wrapper.clot.ClotReportWrapper;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;
import com.inn.product.um.geography.dao.GeographyL3Dao;
import com.inn.product.um.geography.dao.GeographyL4Dao;
import com.inn.product.um.geography.model.GeographyL4;

import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service("ClotReportServiceImpl")
public class ClotReportServiceImpl implements IClotReportService, ReportConstants {
	private static final Logger logger = LogManager.getLogger(ClotReportServiceImpl.class);


	@Autowired
	private IMapImagesService mapImageService;
	@Autowired
	private IGenericWorkorderDao iGenericWorkorderDao;
	@Autowired
	private IGenericMapService iGenericMapService;
	@Autowired
	private GeographyL3Dao geographyL3Dao;
	@Autowired
	private GeographyL4Dao geographyL4Dao;
	@Autowired
	private IAnalyticsRepositoryDao analyticsrepositoryDao;
	@Autowired
	private INVLayer3DashboardService nvLayer3DashboardService;
	@Autowired
	private IReportService reportService;

	public ClotReportServiceImpl() {
		super();

	}

	ObjectMapper mapper = new ObjectMapper();
	
	@Override
	@Transactional
	public Response execute(String json) {
		ClotReportSubWrapper subWrapper = new ClotReportSubWrapper();
		List<ClotReportSubWrapper> subWrapperList = new ArrayList<>();
		String localDirPath = ConfigUtils.getString(NV_REPORTS_PATH) +new Date().getTime() + FORWARD_SLASH;
		ReportUtil.createDirectory(localDirPath);
		Integer analyticsrepoId = null;
		try {
			Map<String, Object> jsonMap = reportService.getJsonDataMap(json);
			Integer workOrderId = (Integer) jsonMap.get(WORKORDER_ID);
			String username = (String) jsonMap.get(NVLayer3Constants.ASSIGN_TO_JSON_KEY);
			analyticsrepoId = (Integer) jsonMap.get(ForesightConstants.ANALYTICAL_REPORT_KEY);
			logger.info("going to invoke report generation method for workOrderId {} ", workOrderId);
			if (reportService.getFileProcessedStatusForWorkorders(Arrays.asList(workOrderId))) {
				subWrapper.setTesterName(username);

				generateClotReport(subWrapper, subWrapperList, localDirPath, workOrderId,analyticsrepoId);

			}
		} catch (Exception e1) {
			logger.error("IOException insdie method createReportForWorkOrderID {} ", Utils.getStackTrace(e1));
			analyticsrepositoryDao.updateStatusInAnalyticsRepository(analyticsrepoId, null, "Something Went Wrong",
					progress.Failed, null);
			return Response.ok("{\"result\":\"" + "IOException for json " + e1.getLocalizedMessage() + " \"}").build();
		}
		finally {
			logger.info("localDirPath {}",localDirPath);
			try {
				FileUtils.deleteDirectory(new File(localDirPath));
			} catch (IOException e) {
				logger.error("Error in deleting directory localDirPath {} message {}",localDirPath,e.getMessage());
			}catch(Exception e) {
				logger.error("Error occuring to delte the directory {}",e.getMessage());
			}
		}
		return null;
	}


	private Response generateClotReport(ClotReportSubWrapper subWrapper,List<ClotReportSubWrapper> subWrapperList, String localDirPath, 
			Integer workOrderId,Integer analyticsrepoId) {
			try {

			logger.info("inside the method createReportForWorkOrderID");
			ClotReportWrapper mainWrapper = new ClotReportWrapper();
			Map<String, List<String>> recipeCategoryOperatorMap = nvLayer3DashboardService
					.getDriveRecipeDetail(workOrderId);
	
			if (recipeCategoryOperatorMap!=null) {
				GenericWorkorder genricWorkOrder = iGenericWorkorderDao.findByPk(workOrderId);
				Map<String, String> gwoMap = genricWorkOrder.getGwoMeta();
				String name = getGeographyName(gwoMap, subWrapper);
				String fileName = ReportUtil.getFileName(genricWorkOrder.getWorkorderId(), analyticsrepoId,
						localDirPath);


				List<String> fetchKPIList = ReportIndexWrapper.getLiveDriveKPIs();
				Map<String, Integer> kpiIndexMap = ReportIndexWrapper.getLiveDriveKPIIndexMap(fetchKPIList);
			
				logger.info("kpiIndexMap: {}", kpiIndexMap);
				
				List<KPIWrapper> kpiList = reportService.getKPiStatsDataList(workOrderId,recipeCategoryOperatorMap, kpiIndexMap, CLOT);
				logger.info("kpilist from reportutil {}", kpiList);
				List<String[]> arlist = reportService.getDriveDataRecipeWiseTaggedForReport(Arrays.asList(workOrderId),recipeCategoryOperatorMap.get(ReportConstants.RECIPE), fetchKPIList, kpiIndexMap);
                logger.info("kpi list in clot {}",kpiList );
				//List<String[]> arlist = reportService.getDriveDataForReport(workOrderId, recipeCategoryOperatorMap.get(RECIPE), fetchKPIList);
     			getDriveDataByTestType(arlist,kpiIndexMap);

				subWrapper.setSiteInfoList(reportService.getSiteDataForReportByDataList(arlist, kpiIndexMap));
				List<List<List<List<Double>>>> boundaries = getBoundaryData(mapper, name, gwoMap);
				DriveImageWrapper driveImageWrapper = new DriveImageWrapper(arlist,	kpiIndexMap.get(LATITUDE), kpiIndexMap.get(LONGITUDE),
						kpiIndexMap.get(PCI_PLOT), kpiList, subWrapper.getSiteInfoList(), boundaries);
				HashMap<String, String> imagemap = getImageMapForReport(kpiList, driveImageWrapper,localDirPath, kpiIndexMap);

				List<String> fetchSummaryKPIList = ReportSummaryIndexWrapper.getLiveDriveKPIs();
				String[] summaryData = reportService.getSummaryDataForReport(workOrderId, recipeCategoryOperatorMap.get(RECIPE), fetchSummaryKPIList);
                Map<String, Integer> summaryKpiIndexMap = ReportSummaryIndexWrapper.getLiveDriveKPIIndexMap(fetchSummaryKPIList);
				
				List<KPISummaryDataWrapper> kpisummarList = reportService.getKPISummaryDataForWorkOrderId(
						workOrderId, driveImageWrapper.getDataKPIs(), driveImageWrapper.getKpiWrappers(),
						summaryData, NV_CLOT, null, null, null, kpiIndexMap, summaryKpiIndexMap);
				setDataForEarfcn(subWrapper, driveImageWrapper,kpiIndexMap);
				setDataForReport(subWrapper, subWrapperList, arlist, kpiList,kpisummarList, name,kpiIndexMap);
				setFileNameStartEndDate(mainWrapper,fileName,genricWorkOrder,subWrapperList);
				
				getRemarkDataForReport(mainWrapper, workOrderId, recipeCategoryOperatorMap);
				HashMap<String, Object> images = prepareImageMap(imagemap, kpiIndexMap);
				mainWrapper.setListPsDataWrapper(reportService.getPsDataWrapperFromSummaryDataForReport(summaryData, summaryKpiIndexMap));
				
				String hdfsFilePath = ConfigUtils.getString(NV_REPORTS_PATH_HDFS) + MASTER + FORWARD_SLASH;
				File reportFile = proceedToCreateCLOTReport(images, mainWrapper);
				reportService.genrateDocxReport(hdfsFilePath, fileName,
						ReportUtil.getFileNameForDoc(genricWorkOrder.getWorkorderId(), analyticsrepoId));
				
				return reportService.saveFileAndUpdateStatus(analyticsrepoId, hdfsFilePath, genricWorkOrder,
						reportFile, null,NVWorkorderConstant.REPORT_INSTACE_ID);
			}else{
				analyticsrepositoryDao.updateStatusInAnalyticsRepository(analyticsrepoId,null,null,progress.Failed,null);
				return Response.ok(ForesightConstants.FAILURE_JSON).build();
			}
		}catch (Exception e) {
			analyticsrepositoryDao.updateStatusInAnalyticsRepository(analyticsrepoId,null,e.getMessage(),progress.Failed,null);
			logger.error("Exception inside the method createClotReportForWorkOrderID {}",Utils.getStackTrace(e));
		}
		return Response.ok(ForesightConstants.FAILURE_JSON).build();
	}

	
	private void getDriveDataByTestType(List<String[]> listArray, Map<String, Integer> kpiIndexMap) {
         logger.info("inside method getDriveDataByTestType");
		if (listArray != null && !listArray.isEmpty()) {
			for (String[] data : listArray) {
				if (ReportUtil.checkValidString(kpiIndexMap.get(TEST_TYPE), data)
						&& (data[kpiIndexMap.get(TEST_TYPE)].equalsIgnoreCase("HTTP_DOWNLOAD")
								|| data[kpiIndexMap.get(TEST_TYPE)].equalsIgnoreCase("FTP_DOWNLOAD"))) {
					// NOTHING TO DO
				} else {
					data[kpiIndexMap.get(PDSCH_THROUGHPUT)] = "";
				}
           	}
		}
   }
	
	private void setFileNameStartEndDate(ClotReportWrapper mainWrapper, String fileName, GenericWorkorder genricWorkOrder, List<ClotReportSubWrapper> subWrapperList) {
		mainWrapper.setFileName(fileName);
		mainWrapper.setEndDate(DateUtil.parseDateToString(DATE_FORMAT_YYYYMMDD, genricWorkOrder.getModificationTime()));
		mainWrapper.setStartDate((DateUtil.parseDateToString(DATE_FORMAT_YYYYMMDD, genricWorkOrder.getCreationTime())));
		mainWrapper.setSubWrapperList(subWrapperList);
	}
	
	private Map<Integer, Integer> getCustomIndexMapForDriveData(Map<String, Integer> kpiIndexMap){
		Map<Integer, Integer> customIndexMap = new HashMap<>();
		if(kpiIndexMap != null && kpiIndexMap.containsKey(FTP_DL_THROUGHPUT) && kpiIndexMap.containsKey(PDSCH_THROUGHPUT)){
			customIndexMap.put(kpiIndexMap.get(FTP_DL_THROUGHPUT), kpiIndexMap.get(PDSCH_THROUGHPUT));
		}
		if(kpiIndexMap != null && kpiIndexMap.containsKey(HTTP_DL_THROUGHPUT) && kpiIndexMap.containsKey(PDSCH_THROUGHPUT)){
			customIndexMap.put(kpiIndexMap.get(HTTP_DL_THROUGHPUT), kpiIndexMap.get(PDSCH_THROUGHPUT));
		}
		return customIndexMap;
	}

	private void getRemarkDataForReport(ClotReportWrapper mainWrapper, Integer workOrderId,
			Map<String, List<String>> recipeCategoryOperatorMap) {
		logger.info("inside the method getRemarkDataForReport");
		try {
			List<ReportSubWrapper> remarkDataList = new ArrayList<>();
			ReportSubWrapper reportSubWrapper = reportService.getRemarkDataForReport(workOrderId,
					recipeCategoryOperatorMap);
			if (reportSubWrapper.getRemarkDataList() != null
					&& reportSubWrapper.getRemarkDataList().size() > INDEX_ZER0) {
				mainWrapper.setIsremarkData(TRUE);
			}
			if (reportSubWrapper.getTestSkipDataList() != null
					&& reportSubWrapper.getTestSkipDataList().size() > INDEX_ZER0) {
				mainWrapper.setIstestSkip(TRUE);
			}
			remarkDataList.add(reportSubWrapper);
			mainWrapper.setRemarkDataList(remarkDataList);
		}
		catch(Exception e) {
			logger.error("unable to set remark data {}",e.getMessage());
		}
	}

	private void setDataForEarfcn(ClotReportSubWrapper subWrapper, DriveImageWrapper driveImageWrapper, Map<String, Integer> kpiIndexMap) {
		logger.info("inside the method  setDataForEarfcn");
		List<GraphDataWrapper> earfcnList = new ArrayList<>();
		try {
			
			Double cdf = 0.0;
//			String json = nvLayer3DashboardService.getKpiStatsRecipeData(workOrderId, DLEARFCN,
//					recipiMap.get(RECIPE), recipiMap.get(OPERATOR));
//			JSONObject jsonObject = ReportUtil.convertStringToJsonObject(json);
//			Map<String, Integer> earfcnCountMap = mapper.readValue(jsonObject.get(RESULT).toString(),
//					new TypeReference<Map<String, Integer>>() {
//			});
			Map<String, Long> earfcnCountMap = mapImageService.getEarfcnCountMap(driveImageWrapper, kpiIndexMap);
			Integer totalCount = earfcnCountMap.values().stream().mapToInt(Number::intValue).sum();
			for (Entry<String, Long> map : earfcnCountMap.entrySet()) {
				if(map.getKey()!=null &&!map.getKey().isEmpty()) {
				GraphDataWrapper graphData = new GraphDataWrapper();
				Double pdf = Utils.getPercentage(map.getValue()!=null?map.getValue().intValue():0, totalCount);
				cdf += pdf;
				graphData.setPdfValue(pdf);
				graphData.setCdfValue(cdf);
				graphData.setCount(map.getValue()!=null?map.getValue().intValue():0);
				graphData.setKpiName(map.getKey());
				earfcnList.add(graphData);
				}
			}
			logger.info("earfcnList is " + earfcnList);
			subWrapper.setEarfcnList(earfcnList);
		}catch (Exception e) {
			logger.error("Error inside the method setDataForEarfcn {}", Utils.getStackTrace(e));
		}
	}

	private HashMap<String, String> getImageMapForReport(List<KPIWrapper> kpiList, DriveImageWrapper driveImageWrapper,
			String localDirectoryPath, Map<String, Integer> kpiIndexMap) throws IOException {
		HashMap<String, BufferedImage> bufferdImageMap = reportService.getLegendImages(kpiList,
				driveImageWrapper.getDataKPIs(), kpiIndexMap.get(TEST_TYPE));
		bufferdImageMap.putAll(mapImageService.getDriveImagesForReport(driveImageWrapper, null, kpiIndexMap));
		return  mapImageService.saveDriveImages(bufferdImageMap,
				localDirectoryPath, false);
	}

	private List<List<List<List<Double>>>> getBoundaryData(ObjectMapper mapper, String name, Map<String, String> gwoMap)
			throws IOException {
		String boundaryData = getGeographyBoundaryByLevelAndName(name, gwoMap.get(GEOGRAPHY_TYPE));
		List<List<List<Double>>> boundarie = null;
		List<List<List<List<Double>>>> boundaries = new ArrayList<>();
		if (boundaryData != null) {
			boundarie = mapper.readValue(boundaryData, new TypeReference<List<List<List<Double>>>>() {
			});
			logger.info("boundaries{}", boundarie.toString());
			boundaries.add(boundarie);
		}
		return boundaries;
	}

//	private void setSiteDataForReport(ClotReportSubWrapper subWrapper, List<String[]> arlist, Map<String, Integer> kpiIndexMap) {
//		try {
//			Corner corner = ReportUtil.getminmaxLatlOnDriveList(arlist, kpiIndexMap.get(LATITUDE), kpiIndexMap.get(LONGITUDE));
//			Map<String, Double> viewportMap = ReportUtil.getViewPortMap(corner);
//			subWrapper.setSiteInfoList(siteDetailService.getMacroSiteDetailsForCellLevelForReport(viewportMap, null, null, null, false, true, true));
//		} catch (Exception e) {
//			logger.error("Exception inside the method setSiteDataForReport {}", Utils.getStackTrace(e));
//		}
//	}

	private List<ClotReportSubWrapper> setDataForReport(ClotReportSubWrapper subWrapper,
			List<ClotReportSubWrapper> subWrapperList, List<String[]> arlist, List<KPIWrapper> kpiList,
			List<KPISummaryDataWrapper> kpisummaryList, String clusterName, Map<String, Integer> kpiIndexMap) {
		subWrapper.setKpiInfoList(kpisummaryList);
		subWrapper.setClusterName(clusterName);
		setTechnologyEarfcnData(kpiIndexMap, arlist,subWrapper);
		populateClotGraphData(subWrapper, kpiList, arlist);
		subWrapperList.add(subWrapper);
		return subWrapperList;
	}

	private String getGeographyName(Map<String, String> gwoMap, ClotReportSubWrapper subWrapper) {
		String name = null;
		try {
			if (GEOGRAPHYL4.equalsIgnoreCase(gwoMap.get(GEOGRAPHY_TYPE))) {
				GeographyL4 geographyL4 = geographyL4Dao.findByPk(Integer.parseInt(gwoMap.get(GEOGRAPHY_ID)));
				subWrapper.setCity(geographyL4.getGeographyL3().getDisplayName());
				name = geographyL4.getDisplayName();
			} else if (GEOGRAPHYL3.equalsIgnoreCase(gwoMap.get(GEOGRAPHY_TYPE))) {
				name = geographyL3Dao.findByPk(Integer.parseInt(gwoMap.get(GEOGRAPHY_ID))).getDisplayName();
				subWrapper.setCity(name);
			}
		} catch (Exception e) {
			logger.error("Error inside the Method getGeographyName{}", Utils.getStackTrace(e));
		}
		return name;
	}

	private ClotReportSubWrapper populateClotGraphData(ClotReportSubWrapper subWrapper, List<KPIWrapper> kpiList,
			List<String[]> arlist) {
		for (KPIWrapper kpiWrapper : kpiList) {
			populateGraphDataForkpi(subWrapper, kpiWrapper, arlist);
		}
		return subWrapper;
	}

	private ClotReportSubWrapper populateGraphDataForkpi(ClotReportSubWrapper subWrapper, KPIWrapper kpiWrapper,
			List<String[]> arlist) {
		logger.info("inside the method populateGraphDataForkpi");
		try {
			if (RSRP.equalsIgnoreCase(kpiWrapper.getKpiName())) {
				subWrapper.setRsrpList(ReportUtil.setGraphDataForKpi(kpiWrapper,
						ReportUtil.convetArrayToList(arlist, kpiWrapper.getIndexKPI())));
			} else if (SINR.equalsIgnoreCase(kpiWrapper.getKpiName())) {
				subWrapper.setSinrList(ReportUtil.setGraphDataForKpi(kpiWrapper,
						ReportUtil.convetArrayToList(arlist, kpiWrapper.getIndexKPI())));
			} else if (PDSCH_THROUGHPUT.equalsIgnoreCase(kpiWrapper.getKpiName())) {
				subWrapper.setDlList(ReportUtil.setGraphDataForKpi(kpiWrapper,
						ReportUtil.convetArrayToList(arlist, kpiWrapper.getIndexKPI())));
			} else if (UL.equalsIgnoreCase(kpiWrapper.getKpiName())) {
				subWrapper.setUlList(ReportUtil.setGraphDataForKpi(kpiWrapper,
						ReportUtil.convetArrayToList(arlist, kpiWrapper.getIndexKPI())));

			} else if (CQI.equalsIgnoreCase(kpiWrapper.getKpiName())) {
				subWrapper.setCqiList(ReportUtil.setGraphDataForKpi(kpiWrapper,
						ReportUtil.convetArrayToList(arlist, kpiWrapper.getIndexKPI())));
			} else if (MIMO.equalsIgnoreCase(kpiWrapper.getKpiName())) {
				subWrapper.setMimoList(ReportUtil.setGraphDataForKpi(kpiWrapper,
						ReportUtil.convetArrayToList(arlist, kpiWrapper.getIndexKPI())));
			} else if (MCS.equalsIgnoreCase(kpiWrapper.getKpiName())) {
				subWrapper.setMcsList(ReportUtil.setGraphDataForKpi(kpiWrapper,
						ReportUtil.convetArrayToList(arlist, kpiWrapper.getIndexKPI())));
			}

		} catch (Exception e) {
			logger.error("Exception inside populateGraphDataForkpi {}", Utils.getStackTrace(e));
		}
		return subWrapper;
	}

	private String getGeographyBoundaryByLevelAndName(String name, String level) {
		logger.info("inside the method getGeographyNameFromWorkOrder name and level {} {}", name, level);
		if (GEOGRAPHYL4.equalsIgnoreCase(level)) {
			return getL4BoundaryByName(name);
		} else if (GEOGRAPHYL3.equalsIgnoreCase(level)) {
			return getL3BoundaryByName(name);
		}
		return null;
	}

	private String getL3BoundaryByName(String name) {
		List<Map<String, String>> boundarMap;
		boundarMap = iGenericMapService.getBoundaryDataByGeographyNamesMS(Arrays.asList(name),
				GenericMapUtils.GEOGRAPHY_TABLE_NAME, GenericMapUtils.getColumnListForQuery(), null,GenericMapUtils.L3_TYPE);
		if (boundarMap != null && !boundarMap.isEmpty()) {
			return boundarMap.get(ReportConstants.INDEX_ZER0).get(GenericMapUtils.COORDINATES);
		}
		return null;
	}

	private String getL4BoundaryByName(String name) {
		List<Map<String, String>> boundarMap;
		boundarMap = iGenericMapService.getBoundaryDataByGeographyNamesMS(Arrays.asList(name),
				GenericMapUtils.GEOGRAPHY_TABLE_NAME, GenericMapUtils.getColumnListForQuery(), null,GenericMapUtils.L4_TYPE);
		if (boundarMap != null && !boundarMap.isEmpty()) {
			return boundarMap.get(ReportConstants.INDEX_ZER0).get(GenericMapUtils.COORDINATES);
		}
		return null;
	}


	private HashMap<String, Object> prepareImageMap(HashMap<String, String> imagemap, Map<String, Integer> kpiIndexMap) {
		logger.info("inside the method prepareImageMap");
		Map<String, Object> map = new HashMap<>();
		try {
			map.put(IMAGE_RSRP, imagemap.get(kpiIndexMap.get(RSRP).toString()));
			map.put(IMAGE_RSRP_LEGEND, imagemap.get(LEGEND + UNDERSCORE + kpiIndexMap.get(RSRP)));

			map.put(IMAGE_SINR, imagemap.get(kpiIndexMap.get(SINR).toString()));
			map.put(IMAGE_SINR_LEGEND, imagemap.get(LEGEND + UNDERSCORE + kpiIndexMap.get(SINR)));
			map.put(IMAGE_DL, imagemap.get(kpiIndexMap.get(PDSCH_THROUGHPUT) + ""));
			map.put(IMAGE_DL_LEGEND, imagemap.get(LEGEND + UNDERSCORE + kpiIndexMap.get(PDSCH_THROUGHPUT)));

			map.put(IMAGE_UL, imagemap.get(kpiIndexMap.get(UL_THROUGHPUT) + ""));
			map.put(IMAGE_UL_LEGEND, imagemap.get(LEGEND + UNDERSCORE + kpiIndexMap.get(UL_THROUGHPUT)));
			map.put(IMAGE_PCI, imagemap.get(kpiIndexMap.get(PCI_PLOT) + ""));
			map.put(IMAGE_SITE, imagemap.get(SITE_IMAGE));
			map.put(IMAGE_PCI_LEGEND, imagemap.get(PCI_LEGEND));
			map.put(IMAGE_CQI, imagemap.get(kpiIndexMap.get(CQI) + ""));
			map.put(IMAGE_CQI_LEGEND, imagemap.get(LEGEND + UNDERSCORE + kpiIndexMap.get(CQI)));
			map.put(IMAGE_RI, imagemap.get(kpiIndexMap.get(RI) + ""));
			map.put(IMAGE_RI_LEGEND,
					imagemap.get(LEGEND + UNDERSCORE + kpiIndexMap.get(RI)));

			map.put(IMAGE_MCS, imagemap.get(kpiIndexMap.get(MCS) + ""));
			map.put(IMAGE_MCS_LEGEND,
					imagemap.get(LEGEND + UNDERSCORE + kpiIndexMap.get(MCS)));
			map.put(IMAGE_FC, imagemap.get(kpiIndexMap.get(DL_EARFCN) + ""));
			map.put(IMAGE_FC_LEGEND, imagemap.get(DL_EARFCN));

			map.put(IMAGE_ROUTE, imagemap.get(kpiIndexMap.get(ROUTE) + ""));

		} catch (Exception e) {
			logger.error("Error inside the method prepareImageMap{}", e.getMessage());
		}
		return (HashMap<String, Object>) map;
	}

	private File proceedToCreateCLOTReport(Map<String, Object> imageMap, ClotReportWrapper mainWrapper) {
		logger.info("inside the methid proceedToCreateCLOTReport ");
		try {
			String reportAssetRepo = ConfigUtils.getString(ReportConstants.CLOTREPORT_JASPER_FOLDER_PATH);
			List<ClotReportWrapper> dataSourceList = new ArrayList<>();
			dataSourceList.add(mainWrapper);
			JRBeanCollectionDataSource rfbeanColDataSource = new JRBeanCollectionDataSource(dataSourceList);
			imageMap.put(SUBREPORT_DIR, reportAssetRepo);
			imageMap.put(LOGO_CLIENT_KEY, reportAssetRepo + LOGO_CLIENT_IMG);
			imageMap.put(IMAGE_PARAM_HEADER_LOGO, reportAssetRepo + IMAGE_HEADER_LOGO);
			imageMap.put(LOGO_NV_KEY, reportAssetRepo + LOGO_NV_IMG);
			String destinationFileName = mainWrapper.getFileName();
			JasperRunManager.runReportToPdfFile(reportAssetRepo + MAIN_JASPER,
					destinationFileName, imageMap, rfbeanColDataSource);
			logger.info("Report Created successfully  ");

			return ReportUtil.getIfFileExists(destinationFileName);
		} catch (Exception e) {
			logger.error("@proceedToCreateCLOTReport getting err={}", Utils.getStackTrace(e));
		}
		logger.info(
				"@proceedToCreateCLOTReport going to return null as there has been some problem in generating report");
		return null;
	}
	
	
	public void setTechnologyEarfcnData(Map<String, Integer> kpiIndexMap, List<String[]> arlist, ClotReportSubWrapper subWrapper) {
		Set<String> technology = new HashSet<>();
		Set<String> earfcn = new HashSet<>();
		for (String[] data : arlist) {
			if (kpiIndexMap.get(TECHNOLOGY) != null && data.length > kpiIndexMap.get(TECHNOLOGY)
					&& !data[kpiIndexMap.get(TECHNOLOGY)].isEmpty()) {
				technology.add(data[kpiIndexMap.get(TECHNOLOGY)]);
			}
			if (ReportUtil.checkIndexValue(kpiIndexMap.get(DL_EARFCN), data)) {
				earfcn.add(data[kpiIndexMap.get(ReportConstants.DL_EARFCN)]);
			}
			if (ReportUtil.checkIndexValue(kpiIndexMap.get(UL_EARFCN), data)) {
				earfcn.add(data[kpiIndexMap.get(ReportConstants.UL_EARFCN)]);
			}
		}
        if(technology!=null && ! technology.isEmpty())  {
		subWrapper.setTechnology(NVLayer3Utils.getStringFromSetValues(technology).replace(Symbol.UNDERSCORE, Symbol.HYPHEN));
	     }
        if(earfcn!=null && ! earfcn.isEmpty())  {
		subWrapper.setFrequency(NVLayer3Utils.getStringFromSetValues(earfcn).replace(Symbol.UNDERSCORE, Symbol.HYPHEN));
	}}

}

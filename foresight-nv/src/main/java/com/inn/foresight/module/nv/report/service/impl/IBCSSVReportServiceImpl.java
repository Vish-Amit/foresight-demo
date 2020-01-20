package com.inn.foresight.module.nv.report.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.inn.commons.Symbol;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.hadoop.hbase.HBaseResult;
import com.inn.commons.lang.NumberUtils;
import com.inn.commons.lang.StringUtils;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.dao.IBuildingDataDao;
import com.inn.foresight.core.infra.model.Building;
import com.inn.foresight.core.report.dao.IAnalyticsRepositoryDao;
import com.inn.foresight.core.report.model.AnalyticsRepository.progress;
import com.inn.foresight.module.nv.core.workorder.dao.impl.IGenericWorkorderDao;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder.TemplateType;
import com.inn.foresight.module.nv.layer3.constants.NVLayer3Constants;
import com.inn.foresight.module.nv.layer3.constants.QMDLConstant;
import com.inn.foresight.module.nv.layer3.service.INVLayer3DashboardService;
import com.inn.foresight.module.nv.report.constant.ReportIndexWrapper;
import com.inn.foresight.module.nv.report.constant.ReportSummaryIndexWrapper;
import com.inn.foresight.module.nv.report.customreport.ssvt.constants.SSVTConstants;
import com.inn.foresight.module.nv.report.ib.utils.IBReportUtils;
import com.inn.foresight.module.nv.report.service.IIBCSSVReportService;
import com.inn.foresight.module.nv.report.service.IMapImagesService;
import com.inn.foresight.module.nv.report.service.INVInBuildingReportService;
import com.inn.foresight.module.nv.report.service.IReportService;
import com.inn.foresight.module.nv.report.service.ISSVTReportService;
import com.inn.foresight.module.nv.report.utils.DriveHeaderConstants;
import com.inn.foresight.module.nv.report.utils.LegendUtil;
import com.inn.foresight.module.nv.report.utils.ReportConstants;
import com.inn.foresight.module.nv.report.utils.ReportUtil;
import com.inn.foresight.module.nv.report.workorder.wrapper.DriveDataWrapper;
import com.inn.foresight.module.nv.report.workorder.wrapper.DriveImageWrapper;
import com.inn.foresight.module.nv.report.workorder.wrapper.KPIWrapper;
import com.inn.foresight.module.nv.report.wrapper.ComparisoGraphWrapper;
import com.inn.foresight.module.nv.report.wrapper.IBCReportWrapper;
import com.inn.foresight.module.nv.report.wrapper.IBCTableDataHolder;
import com.inn.foresight.module.nv.report.wrapper.KPISummaryDataWrapper;
import com.inn.foresight.module.nv.report.wrapper.NVReportConfigurationWrapper;
import com.inn.foresight.module.nv.report.wrapper.RecipeMappingWrapper;
import com.inn.foresight.module.nv.report.wrapper.ReportDataHolder;
import com.inn.foresight.module.nv.report.wrapper.SSVTReportSubWrapper;
import com.inn.foresight.module.nv.report.wrapper.SSVTReportWrapper;
import com.inn.foresight.module.nv.report.wrapper.SiteInformationWrapper;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.HandOverDataWrappr;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.LiveDriveVoiceAndSmsWrapper;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;
import com.inn.product.legends.dao.ILegendRangeDao;
import com.inn.product.legends.utils.LegendWrapper;
import com.inn.product.systemconfiguration.dao.SystemConfigurationDao;

import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service("IBCSSVReportServiceImpl")
public class IBCSSVReportServiceImpl implements IIBCSSVReportService, DriveHeaderConstants {
	/** The logger. */
	private Logger logger = LogManager.getLogger(IBCSSVReportServiceImpl.class);
	@Autowired
	private IReportService reportService;

	@Autowired
	private IAnalyticsRepositoryDao analyticsrepositoryDao;
	@Autowired
	private IGenericWorkorderDao iGenericWorkorderDao;
	@Autowired
	private INVLayer3DashboardService nvLayer3DashboardService;
	@Autowired
	private SystemConfigurationDao iSystemConfigurationDao;
	@Autowired
	private ISSVTReportService iSSVTReportService;
	@Autowired
	private ILegendRangeDao legendRangeDao;

	@Autowired
	private IMapImagesService mapImageService;
	@Autowired
	INVInBuildingReportService nvInBuildingReportService;

	private static final Double CRITERIA_99 = 99.0;
	private static final Double CRITERIA_1 = 1.0;
	private static final Double CRITERIA_30 = 30.0;
	private static final Double CRITERIA_65 = 65.0;
	private static final Double CRITERIA_7 = 7.0;
	private static final Double CRITERIA_15 = 15.0;

	@Autowired
	IBuildingDataDao buildingDataDaoImpl;
	

	@Override
	public Response execute(String json) {

		logger.info("Inside execute method to create SSVT Report with json {} ", json);
		Integer analyticsrepoId = null;
		try {

			Map<String, Object> jsonMap = reportService.getJsonDataMap(json);
			Integer workorderId = (Integer) jsonMap.get(ReportConstants.WORKORDER_ID);
			analyticsrepoId = (Integer) jsonMap.get(ReportConstants.ANALYTICS_REPOSITORY_ID);
			Integer quickWorkorderId = (Integer) jsonMap.get(ReportConstants.QUICK_WORKORDER_ID);

			Integer woIdForData = workorderId;
			if (quickWorkorderId != null) {
				woIdForData = quickWorkorderId;
			}
			String username = jsonMap.get(NVLayer3Constants.ASSIGN_TO_JSON_KEY) != null
					? jsonMap.get(NVLayer3Constants.ASSIGN_TO_JSON_KEY).toString()
					: null;

			generateReport(jsonMap, analyticsrepoId, workorderId, username, woIdForData);

		} catch (IOException | ForbiddenException e) {
			logger.error("Error nside the method createReportForWorkOrderID for json {} , {} ", json,
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

	private Response generateReport(Map<String, Object> jsonDataMap, Integer analyticsrepoId, Integer workorderId,
			String username, Integer woIdForData) throws IOException {
		logger.info("Inside method generate Report with workorderId {} ", workorderId);
		try {
			GenericWorkorder workorderObj = iGenericWorkorderDao.findByPk(workorderId);

			String filePath = ConfigUtils.getString(ReportConstants.NV_REPORTS_PATH) + ReportConstants.SSVT
					+ ReportConstants.FORWARD_SLASH + workorderObj.getTemplateType().name()
					+ ReportConstants.FORWARD_SLASH + workorderObj.getGwoMeta().get(ReportConstants.SITE_ID)
					+ ReportConstants.FORWARD_SLASH;
			jsonDataMap.put(ReportConstants.TEST_TYPE, workorderObj.getTemplateType().name());

			ReportUtil.createDirectory(filePath);
			boolean isFilesProcessed = reportService.getFileProcessedStatusForWorkorders(Arrays.asList(woIdForData));

			if (isFilesProcessed) {
				Map<String, RecipeMappingWrapper> map = nvLayer3DashboardService
						.getDriveRecipeDetailForMasterReport(Arrays.asList(woIdForData));
				Map<String, RecipeMappingWrapper> finalMap = ReportUtil.getCategoryWiseRecipeMappinWrappermap(map);
				return generateSSVTReport(filePath, analyticsrepoId, username, workorderObj, finalMap,
						woIdForData);
			}
		} catch (Exception e) {
			logger.error("Exception In generateReport {} ", Utils.getStackTrace(e));
			analyticsrepositoryDao.updateStatusInAnalyticsRepository(analyticsrepoId, null, "Data is Not Available",
					progress.Failed, null);
		}
		return Response.ok(ForesightConstants.FAILURE_JSON).build();
	}

	/**
	 * @param json
	 * @param filePath
	 * @param workorderId
	 * @param analyticsrepoId
	 * @param username
	 * @param workorderObj
	 * @param woIdForData
	 * @param band
	 * @param workorderObjForUpdate
	 * @param summaryData
	 * @param driveRecipeDetailMap2
	 * @param subWrapperList
	 * @param subWrapper
	 * @param mainWrapper
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Response generateSSVTReport( String filePath, Integer analyticsrepoId,
			String username, GenericWorkorder woObjToUpdate, Map<String, RecipeMappingWrapper> finalMap,
			Integer woIdForData) throws IOException {

		try {
			GenericWorkorder workorderObj = iGenericWorkorderDao.findByPk(woIdForData);
			SSVTReportSubWrapper subWrapper = new SSVTReportSubWrapper();
			SSVTReportWrapper mainWrapper = new SSVTReportWrapper();
			List<String[]> stationaryDataArray = null;
			List<KPISummaryDataWrapper> kpiSummaryDataList = null;
			subWrapper.setTempFilePath(ReportUtil.getTempFilePath());
			ReportUtil.createDirectory(subWrapper.getTempFilePath());
			Map<String, Object> imageMap = new HashMap<>();
			ReportUtil.setTechnologyFromBand(woObjToUpdate, mainWrapper);
			if (woObjToUpdate.getTemplateType().equals(TemplateType.NV_SSVT_IBC_FULL)) {
				mainWrapper.setIsIBCFull(ReportConstants.TRUE);
			}
			List<String> fetchSummaryKPIList = ReportSummaryIndexWrapper.getLiveDriveKPIs();
			Map<String, Integer> summaryKpiIndexMap = ReportSummaryIndexWrapper
					.getLiveDriveKPIIndexMap(fetchSummaryKPIList);
			List<String> fetchKPIList = ReportIndexWrapper.getLiveDriveKPIs();
			Map<String, Integer> kpiIndexMap = ReportIndexWrapper.getLiveDriveKPIIndexMap(fetchKPIList);
		

			Map<String, Object> siteDataMap = reportService.getSiteDataForReport(workorderObj, null, null, null, false,
					false);

			List<KPIWrapper> kpiList = getKPIList(kpiIndexMap);
			List<SiteInformationWrapper> siteDataList = new ArrayList<>();
			logger.error("inside the filter data  for quick and full {}", finalMap);
			if (finalMap != null && !finalMap.isEmpty()) {
				Map<String, List<String>> recipeOperatorMap = getRecipeOperatorMapFromRecipeMappingWrapper(
						finalMap.get(ReportConstants.RECIPE_CATEGORY_STATIONARY));
				if (recipeOperatorMap != null) {
				
					stationaryDataArray=reportService.getDriveDataRecipeWiseTaggedForReport(Arrays.asList(workorderObj.getId()), recipeOperatorMap.get(QMDLConstant.RECIPE), fetchKPIList, kpiIndexMap);
					if (stationaryDataArray != null) {
						stationaryDataArray = stationaryDataArray.stream()
								.filter(x -> Utils.hasValidValue(x[kpiIndexMap.get(ReportConstants.PCI_PLOT)]))
								.collect(Collectors.toList());
						Map<String, List<String[]>> pciWiseMapDataList = stationaryDataArray.stream()
								.collect(Collectors.groupingBy(x -> x[kpiIndexMap.get(ReportConstants.PCI_PLOT)]));
						logger.error("pciWiseMapDataList {}", pciWiseMapDataList.keySet());
						siteDataList = ReportUtil.getSiteFromMap(siteDataMap);
						Map<Integer, List<SiteInformationWrapper>> pciSiteDataMap = getPciSiteDataListMap(subWrapper,
								siteDataList);
						kpiSummaryDataList = setSummaryDataForReport(subWrapper, mainWrapper, pciWiseMapDataList,
								pciSiteDataMap,kpiIndexMap);
						papulateGraphDataWrapper(kpiList, pciWiseMapDataList, pciSiteDataMap, subWrapper, mainWrapper,kpiIndexMap);

					}
				}
				ReportDataHolder dataHolder = getReportHolder(stationaryDataArray, fetchSummaryKPIList,
						summaryKpiIndexMap, fetchKPIList, kpiIndexMap, kpiList);
				return createReport(filePath, analyticsrepoId, username, woObjToUpdate, workorderObj, subWrapper,
						mainWrapper, imageMap, kpiSummaryDataList, siteDataList,dataHolder);

			}

		} catch (Exception e) {
			logger.error("Unable to generate the SSVT Report due to exception {} ", Utils.getStackTrace(e));
		}
		return Response.ok(ForesightConstants.FAILURE_JSON).build();
	}

	private ReportDataHolder getReportHolder(List<String[]> stationaryDataArray, List<String> fetchSummaryKPIList,
			Map<String, Integer> summaryKpiIndexMap, List<String> fetchKPIList, Map<String, Integer> kpiIndexMap,
			List<KPIWrapper> kpiList) {
		ReportDataHolder dataHolder=new ReportDataHolder();
		dataHolder.setDataList(stationaryDataArray);
		dataHolder.setSummaryKpiIndexMap(summaryKpiIndexMap);
		dataHolder.setKpiIndexMap(kpiIndexMap);
		dataHolder.setKpiList(kpiList);
		dataHolder.setFetchKPIList(fetchKPIList);
		dataHolder.setFetchSummaryKPIList(fetchSummaryKPIList);
		return dataHolder;
	}

	private Response createReport(String filePath, Integer analyticsrepoId, String username,
			GenericWorkorder woObjToUpdate, GenericWorkorder workorderObj, SSVTReportSubWrapper subWrapper,
			SSVTReportWrapper mainWrapper, Map<String, Object> imageMap,
			List<KPISummaryDataWrapper> kpiSummaryDataList, 
			List<SiteInformationWrapper> siteDataList, ReportDataHolder dataHolder) throws IOException {
		setKpiSummaryForMobility(workorderObj, kpiSummaryDataList, subWrapper, imageMap,dataHolder);
		getSiteImage(subWrapper, imageMap, siteDataList, dataHolder);
		iSSVTReportService.setSiteAuditImageInWrapper(woObjToUpdate, mainWrapper, imageMap);
		mainWrapper = iSSVTReportService.preparedWrapperForJasper(workorderObj, siteDataList, kpiSummaryDataList,
				subWrapper, mainWrapper, username, null);
		Double buildingLat = null;
		Double buildingLong = null;
		String buildingId = workorderObj.getGwoMeta().get(NVWorkorderConstant.BUILDING_ID);
		if (buildingId != null && !buildingId.equals("")) {
			Building buildingObj = buildingDataDaoImpl.findByPk(Integer.parseInt(buildingId));
			if (null != buildingObj) {
				buildingLat = buildingObj.getLatitude();
				buildingLong = buildingObj.getLongitude();
				mainWrapper.setBuildingLatitude(buildingLat);
				mainWrapper.setBuildingLongitude(buildingLong);
			}

		}

		mainWrapper.setFileName(ReportUtil.getFileName(woObjToUpdate.getWorkorderId(), analyticsrepoId, filePath));
		logger.info("mainWrapper: {}", mainWrapper.toString());
		File file = proceedToIBCReport(imageMap, mainWrapper);
		if (file != null) {
			logger.info("file == {}", file.getAbsolutePath());

			reportService.saveFileAndUpdateStatus(analyticsrepoId, filePath, woObjToUpdate, file, file.getName(),
					NVWorkorderConstant.REPORT_INSTACE_ID);

		}
		return Response.ok(ForesightConstants.SUCCESS_JSON).build();

	}

	private List<KPISummaryDataWrapper> setSummaryDataForReport(SSVTReportSubWrapper subWrapper,
			SSVTReportWrapper mainWrapper, Map<String, List<String[]>> pciWiseMapDataList,
			Map<Integer, List<SiteInformationWrapper>> pciSiteDataMap, Map<String, Integer> kpiIndexMap) {
		setTableViewData(pciSiteDataMap, pciWiseMapDataList, subWrapper, mainWrapper,kpiIndexMap);
		return getKpiSummaryListForStationary(pciWiseMapDataList, pciSiteDataMap, mainWrapper.getTechnology(),kpiIndexMap);

	}

	private List<KPIWrapper> getKPIList(Map<String, Integer> kpiIndexMap) {
		List<LegendWrapper> legendList = legendRangeDao.findAllLegendRangesAppliedTo(ReportConstants.SSVT_REPORT);
		return ReportUtil.convertLegendsListToKpiWrapperList(legendList, getKpiIndexMap(kpiIndexMap));

	}

	private void getSiteImage(SSVTReportSubWrapper subWrapper, Map<String, Object> imageMap, List<SiteInformationWrapper> siteDataList, ReportDataHolder dataHolder)
			throws IOException {
		try {
			DriveImageWrapper driveImageWrapper = new DriveImageWrapper(dataHolder.getDataList(),
					dataHolder.getKpiIndexMap().get(ReportConstants.LATITUDE), dataHolder.getKpiIndexMap().get(ReportConstants.LONGITUDE), dataHolder.getKpiIndexMap().get(ReportConstants.PCI_PLOT),
					dataHolder.getKpiList(), siteDataList, null, null, null);

			HashMap<String, BufferedImage> bufferdImageMap = mapImageService.getLegendImages(dataHolder.getKpiList(),
					driveImageWrapper.getDataKPIs());
			bufferdImageMap.putAll(mapImageService.getDriveImagesForReport(driveImageWrapper, null,dataHolder.getKpiIndexMap()));
			HashMap<String, String> map = mapImageService.saveDriveImages(bufferdImageMap, subWrapper.getTempFilePath(),
					false);
			imageMap.put(ReportConstants.IMAGE_SITE, map.get(ReportConstants.SITE_IMAGE));
		} catch (Exception e) {
			logger.error("Exception inside the method getSiteImage {}", Utils.getStackTrace(e));
		}
	}

	private Map<Integer, List<SiteInformationWrapper>> getPciSiteDataListMap(SSVTReportSubWrapper subWrapper,
			List<SiteInformationWrapper> siteDataList) {
		siteDataList.sort(Comparator.comparing(SiteInformationWrapper::getCellId));

		List<Integer> cellIDList = siteDataList.stream().map(SiteInformationWrapper::getCellId)
				.collect(Collectors.toList());
		List<String> cellIDS = cellIDList.stream().map(String::valueOf).collect(Collectors.toList());
		subWrapper.setHeading(String.join(Symbol.SLASH_FORWARD_STRING, cellIDS));
		logger.info("Heading is {}", subWrapper.getHeading());
		Map<Integer, List<SiteInformationWrapper>> pciSiteDataMap = siteDataList.stream()
				.collect(Collectors.groupingBy(SiteInformationWrapper::getPci));
		logger.error("pciSiteDataMap ==={}", pciSiteDataMap.keySet());
		return pciSiteDataMap;
	}

	private void papulateGraphDataWrapper(List<KPIWrapper> kpiList, Map<String, List<String[]>> pciWiseMapDataList,
			Map<Integer, List<SiteInformationWrapper>> pciSiteDataMap, SSVTReportSubWrapper subWrapper,
			SSVTReportWrapper mainWrapper, Map<String, Integer> kpiIndexMap) {
		List<ComparisoGraphWrapper> graphDataList = new ArrayList<>();
		for (Entry<Integer, List<SiteInformationWrapper>> entry : pciSiteDataMap.entrySet()) {
			SiteInformationWrapper siteWrapper = pciSiteDataMap.get(entry.getKey()).get(ReportConstants.INDEX_ZER0);
			List<String[]> dataKPIs = pciWiseMapDataList.get(String.valueOf(entry.getKey()));
			List<KPIWrapper> newKpilist = LegendUtil.populateLegendData(kpiList, dataKPIs,
					kpiIndexMap.get(ReportConstants.TEST_TYPE));
			for (KPIWrapper kpiWrapper : newKpilist) {
				ComparisoGraphWrapper graphWrapper = new ComparisoGraphWrapper();
				if (!kpiWrapper.getKpiName().equalsIgnoreCase(ReportConstants.RSRP)) {
					graphWrapper.setUnit(ReportUtil.getUnitByKPiName(kpiWrapper.getKpiName()));
					List<Double> dataList = ReportUtil.convetArrayToList(dataKPIs, kpiWrapper.getIndexKPI());
					if (Utils.isValidList(dataList)) {
						logger.info("siteWrapper {} {}", siteWrapper.getCellId(), siteWrapper.getPci());

						String kpiname = Symbol.PARENTHESIS_OPEN_STRING + Symbol.SPACE_STRING + "CellID"
								+ Symbol.UNDERSCORE_STRING + siteWrapper.getCellId() + Symbol.SLASH_FORWARD_STRING
								+ "PCI" + Symbol.UNDERSCORE_STRING + siteWrapper.getPci()
								+ Symbol.PARENTHESIS_CLOSE_STRING + Symbol.SPACE_STRING
								+ kpiWrapper.getKpiName().replaceAll(ReportConstants.UNDERSCORE, ReportConstants.SPACE);

						logger.info("kpiWrapper key is {}" + kpiWrapper.getKpiName());
						graphWrapper.setPreDriveList(ReportUtil.setGraphDataForKpi(kpiWrapper, dataList, kpiname));
						graphDataList.add(graphWrapper);
						mainWrapper.setIsVaildGraph(ReportConstants.TRUE);
					}
				}
			}
		}

		logger.info("size === graphList  {}", graphDataList.size());
		subWrapper.setGraphDataList(graphDataList);

	}



	private List<IBCReportWrapper> getcellAvgandMaxDataList(Map<String, List<String[]>> pciWiseDataMap,
			Map<Integer, List<SiteInformationWrapper>> pciSiteDataMap, Integer index, Double criteria, String heading,
			String symbol, Map<String, Integer> kpiIndexMap) {
		List<IBCReportWrapper> ibcDataList = new ArrayList<>();
		List<IBCTableDataHolder> list = new ArrayList<>();
		IBCReportWrapper ibcWrapper = new IBCReportWrapper();
		ibcWrapper.setHeading(heading);
		for (Entry<Integer, List<SiteInformationWrapper>> entry : pciSiteDataMap.entrySet()) {
			SiteInformationWrapper siteInfo = entry.getValue().get(ReportConstants.INDEX_ZER0);
			IBCTableDataHolder holder = new IBCTableDataHolder();

			List<String[]> dataList = pciWiseDataMap.get(String.valueOf(entry.getKey()));
			if (Utils.isValidList(dataList)) {
				logger.info("dataList === index ======{}", dataList.size());
				List<Double> attemptList = ReportUtil.convetArrayToList(dataList, index);
				Double avg = getAverageFromList(attemptList);
				holder.setAvg(avg);
				if (Symbol.GRETER_THAN_STRING.equalsIgnoreCase(symbol)) {
					if (avg != null) {
						holder.setStatus((avg >= criteria) ? ReportConstants.PASS : ReportConstants.FAIL);
					}
				} else {
					if (avg != null) {
						holder.setStatus((avg <= criteria) ? ReportConstants.PASS : ReportConstants.FAIL);
					}
				}
				holder.setPeakValue(getMaxFromList(attemptList));

			}
			holder.setPci(entry.getKey());
			holder.setCellID(siteInfo.getCellId());
			setCgiFromData(holder, dataList,kpiIndexMap);
			list.add(holder);

		}
		ibcWrapper.setTable(list);
		ibcDataList.add(ibcWrapper);
		logger.info("ibcDataList ======={}", ibcDataList);

		return ibcDataList;

	}

	private List<IBCReportWrapper> getcellIDWiseDataList(Map<String, List<String[]>> pciWiseMapDataMap,
			Map<Integer, List<SiteInformationWrapper>> pciSiteDataMap, Integer attemptIndex, Integer successIndex,
			String heading, Map<String, Integer> kpiIndexMap) {
		List<IBCReportWrapper> ibcDataList = new ArrayList<>();
		List<IBCTableDataHolder> list = new ArrayList<>();
		IBCReportWrapper ibcWrapper = new IBCReportWrapper();
		ibcWrapper.setHeading(heading);
		for (Entry<Integer, List<SiteInformationWrapper>> entry : pciSiteDataMap.entrySet()) {
			SiteInformationWrapper siteInfo = entry.getValue().get(ReportConstants.INDEX_ZER0);
			IBCTableDataHolder holder = new IBCTableDataHolder();
			List<String[]> dataList = pciWiseMapDataMap.get(String.valueOf(entry.getKey()));
			List<Double> attemptList = ReportUtil.convetArrayToList(dataList, attemptIndex);
			List<Double> successList = ReportUtil.convetArrayToList(dataList, successIndex);
			if (Utils.isValidList(attemptList)) {
				Integer attemptCount = attemptList.stream().mapToInt(Double::intValue).sum();
				Integer successCount=0;
				if (Utils.isValidList(successList)) {
					successCount = successList.stream().mapToInt(Double::intValue).sum();
					holder.setSuccessCount(successCount);
				}
				else {
					holder.setSuccessCount(successCount);
				}
				holder.setAttemptCount(attemptCount);
				holder.setRate(ReportUtil.getPercentage(successCount, attemptCount));
			}
			holder.setPci(entry.getKey());
			holder.setCellID(siteInfo.getCellId());
			setCgiFromData(holder, dataList,kpiIndexMap);
			list.add(holder);

		}
		ibcWrapper.setTable(list);
		ibcDataList.add(ibcWrapper);
		return ibcDataList;
	}

	private List<IBCReportWrapper> getRSRPList(Map<Integer, List<SiteInformationWrapper>> pciSiteDataMap,
			Map<String, List<String[]>> pciWiseMapDataMap, Map<String, Integer> kpiIndexMap) {
		List<IBCReportWrapper> ibcRsrpList = new ArrayList<>();
		List<IBCTableDataHolder> list = new ArrayList<>();
		IBCReportWrapper ibcWrapper = new IBCReportWrapper();
		ibcWrapper.setHeading("RF Condition Static Test Below the Antenna (average)");
		for (Entry<Integer, List<SiteInformationWrapper>> entry : pciSiteDataMap.entrySet()) {
			SiteInformationWrapper siteInfo = entry.getValue().get(ReportConstants.INDEX_ZER0);
			IBCTableDataHolder holder = new IBCTableDataHolder();
			List<String[]> dataList = pciWiseMapDataMap.get(String.valueOf(entry.getKey()));
			List<Double> rsrpDataList = ReportUtil.convetArrayToList(dataList, kpiIndexMap.get(ReportConstants.RSRP));
			List<Double> sinrDataList = ReportUtil.convetArrayToList(dataList, kpiIndexMap.get(ReportConstants.SINR));

			holder.setAvgRsrp(getAverageFromList(rsrpDataList));
			holder.setAvgSinr(getAverageFromList(sinrDataList));
			holder.setPci(entry.getKey());
			holder.setCellID(siteInfo.getCellId());
			setCgiFromData(holder, dataList,kpiIndexMap);
			list.add(holder);

		}
		ibcWrapper.setTable(list);
		ibcRsrpList.add(ibcWrapper);
		return ibcRsrpList;
	}

	private void setCgiFromData(IBCTableDataHolder holder, List<String[]> dataList, Map<String, Integer> kpiIndexMap) {
		List<Double> cgiList = ReportUtil.convetArrayToList(dataList, kpiIndexMap.get(DriveHeaderConstants.CGI));

		if (Utils.isValidList(cgiList)) {
			holder.setCgi(cgiList.get(ReportConstants.INDEX_ZER0).intValue());
		}
	}

	private void setKpiSummaryForMobility(GenericWorkorder workorderObj, List<KPISummaryDataWrapper> kpiSummaryDataList,
			SSVTReportSubWrapper subWrapper, Map<String, Object> imageMap, ReportDataHolder dataHolder) {
		try {
			String mapJson = iSystemConfigurationDao
					.getSystemConfigurationByType(ReportConstants.NV_IBC_REPORT_RECIPEID_MAP)
					.get(ReportConstants.INDEX_ZER0).getValue();
			logger.info("mapJson ==={}", mapJson);
			Map<String, String> recipeIDMap = ReportUtil.convertCSVStringDataToMap(mapJson);
			Map<String, Map<String, List<String>>> recipeWiseIDMap = iSSVTReportService
					.getRecipeMapForRecipeId(workorderObj.getId(), recipeIDMap);

			if (!recipeWiseIDMap.isEmpty()) {
				Map<String, String[]> recipeWiseSummaryMap =iSSVTReportService.getRecipeWiseSummary(recipeWiseIDMap,
						workorderObj.getId(),dataHolder.getFetchSummaryKPIList());
				logger.error("recipeWiseSummaryMap {}",recipeWiseSummaryMap);
				setHandoverSummary(kpiSummaryDataList, recipeWiseSummaryMap, ReportConstants.HANDOVER_IN,dataHolder);
				setHandoverSummary(kpiSummaryDataList, recipeWiseSummaryMap, ReportConstants.HANDOVER_OUT,dataHolder);
				setHandoverSummary(kpiSummaryDataList, recipeWiseSummaryMap, ReportConstants.VOLTE_HANDOVER_IN,dataHolder);
				setHandoverSummary(kpiSummaryDataList, recipeWiseSummaryMap, ReportConstants.VOLTE_HANDOVER_OUT,dataHolder);
				getHandoverImages(workorderObj, subWrapper, recipeWiseIDMap, imageMap,dataHolder);

				setHandOverSummaryList(subWrapper, recipeWiseSummaryMap,dataHolder);
			}
		} catch (Exception e) {
			logger.error("Exception inside the method setKpiSummaryForMobility {}", Utils.getStackTrace(e));
		}

	}

	private void setHandOverSummaryList(SSVTReportSubWrapper subWrapper, Map<String, String[]> recipeWiseSummaryMap,ReportDataHolder dataHolder) {
		List<LiveDriveVoiceAndSmsWrapper> handoverInDataList = ReportUtil.getHandoverPlotDataListForReport(recipeWiseSummaryMap.get(ReportConstants.HANDOVER_IN),dataHolder.getSummaryKpiIndexMap());
		subWrapper.setHandoverPlotDataList(handoverInDataList);

		List<LiveDriveVoiceAndSmsWrapper> handOverOutDataList = ReportUtil.getHandoverPlotDataListForReport(recipeWiseSummaryMap.get(ReportConstants.HANDOVER_OUT),dataHolder.getSummaryKpiIndexMap());
		subWrapper.setPostHandoverPlotDataList(handOverOutDataList);

		List<LiveDriveVoiceAndSmsWrapper> volteInDataList = ReportUtil.getHandoverPlotDataListForReport(recipeWiseSummaryMap.get(ReportConstants.VOLTE_HANDOVER_IN),dataHolder.getSummaryKpiIndexMap());
		subWrapper.setCallDataList(volteInDataList);

		List<LiveDriveVoiceAndSmsWrapper> volteOutDataList = ReportUtil.getHandoverPlotDataListForReport(recipeWiseSummaryMap.get(ReportConstants.VOLTE_HANDOVER_OUT),dataHolder.getSummaryKpiIndexMap());
		subWrapper.setCallPlotDataList(volteOutDataList);
	}

	private Map<String, String> getHandoverImages(GenericWorkorder workorderObj, SSVTReportSubWrapper subWrapper,
			Map<String, Map<String, List<String>>> recipeWiseIDMap, Map<String, Object> imageMap, ReportDataHolder dataHolder) throws IOException {
		logger.info("recipeWiseIDMap {}", recipeWiseIDMap);
		try {
			for (Entry<String, Map<String, List<String>>> entry : recipeWiseIDMap.entrySet()) {
				Map<String, List<String>> map = entry.getValue();
				if ((map.get(QMDLConstant.RECIPE) != null
						&& map.get(QMDLConstant.RECIPE).size() > ReportConstants.INDEX_ZER0)
						&& (map.get(QMDLConstant.OPERATOR) != null
								&& map.get(QMDLConstant.OPERATOR).size() > ReportConstants.INDEX_ZER0)) {
					
					String operator = map.get(QMDLConstant.OPERATOR).get(ReportConstants.INDEX_ZER0);
					String recipeId = map.get(QMDLConstant.RECIPE).get(ReportConstants.INDEX_ZER0);
					DriveDataWrapper driveDataWrapper = nvLayer3DashboardService.getFloorplanDataFromLayer3ReportForFramework(
							workorderObj.getId(), recipeId);
					String imgFloorPlan = nvInBuildingReportService.getFloorplanImg(Integer.valueOf(recipeId), operator,
							driveDataWrapper, driveDataWrapper.getJson(), subWrapper.getTempFilePath());
					
					List<String[]> dataList = reportService.getDriveDataRecipeWiseTaggedForReport(Arrays.asList(workorderObj.getId()),
							map.get(QMDLConstant.RECIPE), dataHolder.getFetchKPIList(), dataHolder.getKpiIndexMap());
					if (Utils.isValidList(dataList)) {
						logger.info("dataList for ho {}", dataList);
						IBReportUtils.getInstance().drawFloorPlan(imgFloorPlan, driveDataWrapper.getJson(),
								ReportConstants.FLOORPLANNAME, dataList);
						Map<String, String> images = InBuildingHeatMapGenerator.getInstance()
								.generateHeatMapsForHandover(dataList, subWrapper.getTempFilePath(), imgFloorPlan,
										dataHolder.getKpiIndexMap(), entry.getKey());
						if (images != null) {
							imageMap.putAll(images);
						}
						setHandOverTableData(workorderObj, subWrapper, entry);

					}

				}
			}
		} catch (Exception e) {

			logger.error("Exception inside the method getHandoverImages{} ", Utils.getStackTrace(e));
		}
		return null;
	}

	private void setHandOverTableData(GenericWorkorder workorderObj, SSVTReportSubWrapper subWrapper,
			Entry<String, Map<String, List<String>>> entry) {
		try {
			if (Utils.isValidList(entry.getValue().get(QMDLConstant.RECIPE))) {
				List<String[]> csvDataArray = reportService.getHandoverDataFromHbase(workorderObj.getId(),
						entry.getValue().get(QMDLConstant.RECIPE));
				setHandOverDetailToWrapper(subWrapper, entry.getKey(), csvDataArray);

			}
		} catch (Exception e) {
			logger.error("Exception inside the method setHandOverTableData {}", e.getMessage());
		}
	}

	public List<String[]> getHandoverDataListFromHBaseResult(List<HBaseResult> resultList) {
		logger.info("Inside Method getHandoverDataListFromHBaseResult with data resultList size: {}",
				resultList.size());
		List<String[]> handoverDataList = new ArrayList<>();
		if (Utils.isValidList(resultList)) {
			for (HBaseResult result : resultList) {
				if (result != null) {
					String data = result.getString(SSVTConstants.LAYER3_REPORT_COLUMN_SSVT);
					if (!StringUtils.isBlank(data) && !SSVTConstants.LAYER3_REPORT_COLUMN_SSVT.equalsIgnoreCase(data)) {
						logger.info("Single String result: {}", data);
						List<String[]> convertedData = ReportUtil.convertCSVStringToDataList(data);
						logger.info("Converted HO Data: {}", new Gson().toJson(convertedData));
						handoverDataList.addAll(ReportUtil.convertCSVStringToDataList(data));
					}
				}
			}
		}
		logger.info("Returning Final HO data List: {}", new Gson().toJson(handoverDataList));
		return handoverDataList;
	}

	private void setHandOverDetailToWrapper(SSVTReportSubWrapper subWrapper, String key, List<String[]> dataList) {
		if (ReportConstants.HANDOVER_IN.equalsIgnoreCase(key)) {
			subWrapper.setHandoverInList(getHandOverDataListFromDriveData(dataList));
		}

		else if (ReportConstants.HANDOVER_OUT.equalsIgnoreCase(key)) {
			subWrapper.setHandoverOutList(getHandOverDataListFromDriveData(dataList));

		}

		else if (ReportConstants.VOLTE_HANDOVER_IN.equalsIgnoreCase(key)) {
			subWrapper.setVolteHOInList(getHandOverDataListFromDriveData(dataList));

		}

		else if (ReportConstants.VOLTE_HANDOVER_OUT.equalsIgnoreCase(key)) {
			subWrapper.setVoletHOOutList(getHandOverDataListFromDriveData(dataList));

		}
	}

	private List<HandOverDataWrappr> getHandOverDataListFromDriveData(List<String[]> listArray) {
		logger.info("inside method getHandOverDataListFromDriveData ");
		List<HandOverDataWrappr> handOverList = new ArrayList<>();
		if (listArray != null && !listArray.isEmpty()) {
			for (String[] array : listArray) {
				setHandOverWrapperIntoList(handOverList, array);
			}
		}
		logger.info("going to return HandOverDataList size {} ", handOverList.size());

		return handOverList;
	}

	private void setHandOverWrapperIntoList(List<HandOverDataWrappr> handOverList, String[] csvData) {
		if (csvData != null && csvData.length > SSVTConstants.INDEX_HO_DATA_STATUS) {
			HandOverDataWrappr itemWrapper = new HandOverDataWrappr();
			itemWrapper.setCaptureTime(ReportUtil.getFormattedDate(
					new Date(Long.parseLong(csvData[SSVTConstants.INDEX_HO_DATA_TIMESTAMP])),
					ReportConstants.DATE_FORMAT_DD_MM_YY_SS_AA));
			itemWrapper.setLatitude(NumberUtils.toDouble(csvData[SSVTConstants.INDEX_HO_DATA_LATITUDE]));
			itemWrapper.setLongitude(NumberUtils.toDouble(csvData[SSVTConstants.INDEX_HO_DATA_LONGITUDE]));
			itemWrapper.setSourcePci(NumberUtils.toInteger(csvData[SSVTConstants.INDEX_HO_DATA_SOURCE_PCI]));
			itemWrapper.setTargetPci(NumberUtils.toInteger(csvData[SSVTConstants.INDEX_HO_DATA_TARGET_PCI]));
			itemWrapper.setHoLatency(csvData[SSVTConstants.INDEX_HO_DATA_INTERRUPTION_TIME]);
			if (!StringUtils.isBlank(csvData[SSVTConstants.INDEX_HO_DATA_STATUS])
					&& SSVTConstants.TEST_STATUS_PASS.equalsIgnoreCase(csvData[SSVTConstants.INDEX_HO_DATA_STATUS])) {
				itemWrapper.setStatus(SSVTConstants.TEST_STATUS_PASS);
			} else {
				itemWrapper.setStatus(SSVTConstants.TEST_STATUS_FAIL);
			}
			handOverList.add(itemWrapper);
		}

	}

	

	private void setHandoverSummary(List<KPISummaryDataWrapper> kpiSummaryDataList,
			Map<String, String[]> recipeWiseSummaryMap, String key, ReportDataHolder dataHolder) {
		KPISummaryDataWrapper wrapper = new KPISummaryDataWrapper();
		NVReportConfigurationWrapper nvWrapper = new NVReportConfigurationWrapper();

		wrapper.setTarget(String.valueOf(99.0));
		nvWrapper.setTargetvalue(String.valueOf(99.0));
		wrapper = ReportUtil.getHandoverSuccessRate(recipeWiseSummaryMap.get(key),
				wrapper, nvWrapper,dataHolder.getSummaryKpiIndexMap().get(ReportConstants.HANDOVER_SUCCESS),
				dataHolder.getSummaryKpiIndexMap().get(ReportConstants.HANDOVER_INITIATE));
		wrapper.setTestName(key.replace(Symbol.UNDERSCORE_STRING, Symbol.SPACE_STRING));
		kpiSummaryDataList.add(wrapper);
	}

	public Map<String, Integer> getKpiIndexMap(Map<String, Integer> kpiIndexMap) {
		Map<String, Integer> map = new HashMap<>();
		map.put(DriveHeaderConstants.RSRP, kpiIndexMap.get(ReportConstants.RSRP));
		map.put(DL_THROUGHPUT, kpiIndexMap.get(ReportConstants.DL_THROUGHPUT));
		map.put(UL_THROUGHPUT, kpiIndexMap.get(ReportConstants.UL_THROUGHPUT));
		map.put(ReportConstants.LATENCY, kpiIndexMap.get(ReportConstants.LATENCY32)); // 23
		return map;
	}

	private List<KPISummaryDataWrapper> getKpiSummaryListForStationary(Map<String, List<String[]>> pciWiseDataMap,
			Map<Integer, List<SiteInformationWrapper>> pciSiteDataMap, String technology, Map<String, Integer> kpiIndexMap) {
		List<KPISummaryDataWrapper> list = new ArrayList<>();
		List<String> keyList = Arrays.asList("RRC Connection Request Success", "VoLTE Setup Success Rate", "Latency",
				"Packet Loss", "ERAB Drop Rate", "Peak Downlink Throughput", "Peak Uplink Throughput");

		for (String key : keyList) {
			KPISummaryDataWrapper wrapper = new KPISummaryDataWrapper();
			wrapper.setSource(ReportConstants.DT_MOBILE);

			switch (key) {
			case "Handover Success Rate":
				wrapper = setCellWiseKpiSummaryRate(pciWiseDataMap, pciSiteDataMap, wrapper, kpiIndexMap.get(ReportConstants.HANDOVER_INITIATE), kpiIndexMap.get(ReportConstants.HANDOVER_SUCCESS), CRITERIA_99,
						Symbol.GRETER_THAN_STRING);
				wrapper.setItem(ReportConstants.ACCESSIBILITY);
				wrapper.setTestName(key);
				list.add(wrapper);
				break;
			case "RRC Connection Request Success":
				wrapper = setCellWiseKpiSummaryRate(pciWiseDataMap, pciSiteDataMap, wrapper,
						kpiIndexMap.get(ReportConstants.RRC_INITIATE),
						kpiIndexMap.get(ReportConstants.RRC_SUCCESS), CRITERIA_99,
						Symbol.GRETER_THAN_STRING);
				wrapper.setItem(ReportConstants.ACCESSIBILITY);
				wrapper.setTestName(key);
				list.add(wrapper);
				break;
			case "VoLTE Setup Success Rate":
				wrapper = setCellWiseKpiSummaryRate(pciWiseDataMap, pciSiteDataMap, wrapper,  kpiIndexMap.get(ReportConstants.CALL_INITIATE),
						kpiIndexMap.get(ReportConstants.CALL_SETUP_SUCCESS), CRITERIA_99, Symbol.GRETER_THAN_STRING);
				wrapper.setItem(ReportConstants.ACCESSIBILITY);
				wrapper.setTestName(key);
				list.add(wrapper);
				break;
			case "ERAB Drop Rate":
				wrapper = setCellWiseKpiSummaryRate(pciWiseDataMap, pciSiteDataMap, wrapper, 
						kpiIndexMap.get(ReportConstants.INITIAL_ERAB_SUCCESS_COUNT),kpiIndexMap.get(ReportConstants.RRC_DROPPED), CRITERIA_1, Symbol.LESS_THAN_STRING);
				wrapper.setItem(ReportConstants.RETAINABILITY);
				wrapper.setTestName(key);
				list.add(wrapper);
				break;
			case "Latency":
				wrapper = setCellWiseKpiSummaryAvgOrMax(pciWiseDataMap, pciSiteDataMap, wrapper, kpiIndexMap.get(ReportConstants.LATENCY32),
						CRITERIA_30, Symbol.LESS_THAN_STRING, true, " ms");
				wrapper.setItem(ReportConstants.INTEGRITY);
				wrapper.setTestName(key);
				list.add(wrapper);
				break;
			case "Packet Loss":
				wrapper = setCellWiseKpiSummaryRate(pciWiseDataMap, pciSiteDataMap, wrapper,kpiIndexMap.get(ReportConstants.TOTAL_PACKET_COUNT),
						kpiIndexMap.get(ReportConstants.RTP_PACKET_LOSS), 0.25, Symbol.LESS_THAN_STRING);
				wrapper.setItem(ReportConstants.RETAINABILITY);
				wrapper.setTestName(key);
				list.add(wrapper);
				break;
			case "Peak Downlink Throughput":

				if (technology.equalsIgnoreCase(ReportConstants.TDD)) {
					wrapper = setCellWiseKpiSummaryAvgOrMax(pciWiseDataMap, pciSiteDataMap, wrapper, kpiIndexMap.get(ReportConstants.DL_THROUGHPUT),
							CRITERIA_65, Symbol.GRETER_THAN_STRING, false, Symbol.SPACE_STRING + ReportConstants.MBPS);
				} else {
					wrapper = setCellWiseKpiSummaryAvgOrMax(pciWiseDataMap, pciSiteDataMap, wrapper, kpiIndexMap.get(ReportConstants.DL_THROUGHPUT),
							CRITERIA_30, Symbol.GRETER_THAN_STRING, false, Symbol.SPACE_STRING + ReportConstants.MBPS);

				}
				wrapper.setItem(ReportConstants.INTEGRITY);
				wrapper.setTestName(key);
				list.add(wrapper);
				break;
			case "Peak Uplink Throughput":

				if (technology.equalsIgnoreCase(ReportConstants.TDD)) {
					wrapper = setCellWiseKpiSummaryAvgOrMax(pciWiseDataMap, pciSiteDataMap, wrapper, kpiIndexMap.get(ReportConstants.UL_THROUGHPUT),
							CRITERIA_7, Symbol.GRETER_THAN_STRING, false, Symbol.SPACE_STRING + ReportConstants.MBPS);
				} else {
					wrapper = setCellWiseKpiSummaryAvgOrMax(pciWiseDataMap, pciSiteDataMap, wrapper, kpiIndexMap.get(ReportConstants.UL_THROUGHPUT),
							CRITERIA_15, Symbol.GRETER_THAN_STRING, false, Symbol.SPACE_STRING + ReportConstants.MBPS);

				}
				wrapper.setItem(ReportConstants.INTEGRITY);
				wrapper.setTestName(key);
				list.add(wrapper);
				break;
			}
		}
		return list;
	}

	private void setTableViewData(Map<Integer, List<SiteInformationWrapper>> pciSiteDataMap,
			Map<String, List<String[]>> pciWiseMapDataMap, SSVTReportSubWrapper subWrapper,
			SSVTReportWrapper mainWrapper, Map<String, Integer> kpiIndexMap) {
		subWrapper.setIbcRsrpList(getRSRPList(pciSiteDataMap, pciWiseMapDataMap,kpiIndexMap));
		subWrapper.setIbcErabList(getcellIDWiseDataList(pciWiseMapDataMap, pciSiteDataMap, 
				kpiIndexMap.get(ReportConstants.INITIAL_ERAB_SUCCESS_COUNT),kpiIndexMap.get(ReportConstants.RRC_DROPPED), "ERAB Drop Rate (Target <1%)",kpiIndexMap));
		
		subWrapper.setIbcRRCList(getcellIDWiseDataList(pciWiseMapDataMap, pciSiteDataMap, kpiIndexMap.get(ReportConstants.RRC_INITIATE),
				 kpiIndexMap.get(ReportConstants.RRC_SUCCESS), "RRC Connection Request Success Rate (Target 99%)",kpiIndexMap));
		
		subWrapper.setIbcVolteList(getcellIDWiseDataList(pciWiseMapDataMap, pciSiteDataMap, kpiIndexMap.get(ReportConstants.CALL_INITIATE),
				kpiIndexMap.get(ReportConstants.CALL_SETUP_SUCCESS), "VoLTE Setup Success Rate (Target 99%)",kpiIndexMap));
		if (ReportConstants.TDD.equalsIgnoreCase(mainWrapper.getTechnology())) {
			subWrapper.setIbcDLList(
					getcellAvgandMaxDataList(pciWiseMapDataMap, pciSiteDataMap, kpiIndexMap.get(ReportConstants.DL_THROUGHPUT), 65.0,
							"FTP Download 30Mhz (Target Peak ≥ 65 Mbps)", Symbol.GRETER_THAN_STRING,kpiIndexMap));
		} else {
			subWrapper.setIbcDLList(
					getcellAvgandMaxDataList(pciWiseMapDataMap, pciSiteDataMap, kpiIndexMap.get(ReportConstants.DL_THROUGHPUT), 30.0,
							"FTP Download 10Mhz (Target Peak ≥ 30 Mbps)", Symbol.GRETER_THAN_STRING,kpiIndexMap));

		}
		if (ReportConstants.TDD.equalsIgnoreCase(mainWrapper.getTechnology())) {
			subWrapper.setIbcULList(
					getcellAvgandMaxDataList(pciWiseMapDataMap, pciSiteDataMap, kpiIndexMap.get(ReportConstants.UL_THROUGHPUT), 7.0,
							"FTP Upload 10Mhz (Target Peak ≥ 7 Mbps)", Symbol.GRETER_THAN_STRING,kpiIndexMap));
		} else {
			subWrapper.setIbcULList(
					getcellAvgandMaxDataList(pciWiseMapDataMap, pciSiteDataMap, kpiIndexMap.get(ReportConstants.UL_THROUGHPUT), 15.0,
							"FTP Upload 10Mhz (Target Peak ≥ 15 Mbps)", Symbol.GRETER_THAN_STRING,kpiIndexMap));

		}
		subWrapper.setIbcPingList(
				getcellAvgandMaxDataList(pciWiseMapDataMap, pciSiteDataMap, kpiIndexMap.get(ReportConstants.LATENCY32), 30.0,
						"Ping Test 32 bytes (Target <= 30ms)", Symbol.LESS_THAN_STRING,kpiIndexMap));

		subWrapper.setIbcPacketLossList(getcellAvgandMaxDataList(pciWiseMapDataMap, pciSiteDataMap, kpiIndexMap.get(ReportConstants.RTP_PACKET_LOSS),
				0.25, "Ping Packet Loss Test 32 bytes (Target <= 0.25%)", Symbol.LESS_THAN_STRING,kpiIndexMap));

	}
	private KPISummaryDataWrapper setCellWiseKpiSummaryRate(Map<String, List<String[]>> pciWiseDataMap,
			Map<Integer, List<SiteInformationWrapper>> pciSiteDataMap, KPISummaryDataWrapper wrapper,
			Integer indexAttempt, Integer indexSuccess, Double criteria, String symbol) {
		logger.info("inside the method setCellWiseKpiSummaryRate {}", indexAttempt);
		for (Entry<Integer, List<SiteInformationWrapper>> entry : pciSiteDataMap.entrySet()) {
			try {
				List<String[]> dataList = pciWiseDataMap.get(String.valueOf(entry.getKey()));
				logger.info("key of dataList ==={}", entry.getKey());

				if (Utils.isValidList(dataList)) {
					logger.info("dataList ==={}, indexAttempt {}", dataList.size(), indexAttempt);
					Double value = calculateRateForKpi(dataList, indexAttempt, indexSuccess, wrapper);
					if (value != null) {
						if (wrapper.getAchived() == null) {
							wrapper.setAchived(value + Symbol.PERCENT_STRING);
						} else {
							wrapper.setAchived(
									wrapper.getAchived() + Symbol.SLASH_FORWARD_STRING + value + Symbol.PERCENT_STRING);
						}
						if (symbol.equalsIgnoreCase(Symbol.GRETER_THAN_STRING)) {
							if (value >= criteria) {
								if (wrapper.getStatus() == null) {
									wrapper.setStatus(ReportConstants.PASS);
								}
							} else {
								wrapper.setStatus(ReportConstants.FAIL);
							}
						} else {

							if (value <= criteria) {
								if (wrapper.getStatus() == null) {
									wrapper.setStatus(ReportConstants.PASS);
								}
							} else {
								wrapper.setStatus(ReportConstants.FAIL);
							}

						}
					} else {
						if (wrapper.getAchived() == null) {
							wrapper.setAchived(Symbol.HYPHEN_STRING);
						} else {
							wrapper.setAchived(
									wrapper.getAchived() + Symbol.SLASH_FORWARD_STRING + Symbol.HYPHEN_STRING);
						}
					}
				} else {
					if (wrapper.getAchived() == null) {
						wrapper.setAchived(Symbol.HYPHEN_STRING);
					} else {
						wrapper.setAchived(wrapper.getAchived() + Symbol.SLASH_FORWARD_STRING + Symbol.HYPHEN_STRING);
					}
				}

				wrapper.setTarget(symbol + Symbol.EQUAL_STRING + criteria + Symbol.PERCENT_STRING);
			} catch (Exception e) {

				logger.error("Exception inside the methdo setCellWiseKpiSummaryRate {}", Utils.getStackTrace(e));
			}

		}
		return wrapper;
	}

	private KPISummaryDataWrapper setCellWiseKpiSummaryAvgOrMax(Map<String, List<String[]>> pciWiseDataMap,
			Map<Integer, List<SiteInformationWrapper>> pciSiteDataMap, KPISummaryDataWrapper wrapper, Integer index,
			Double criteria, String symbol, Boolean isAvg, String unit) {
		for (Entry<Integer, List<SiteInformationWrapper>> entry : pciSiteDataMap.entrySet()) {
			List<String[]> dataList = pciWiseDataMap.get(String.valueOf(entry.getKey()));
			logger.info("dataList ==={},entry.getKey() {}", dataList, entry.getKey());

			if (Utils.isValidList(dataList)) {
				logger.info("dataList ==={}", dataList.size());
				List<Double> attemptList = ReportUtil.convetArrayToList(dataList, index);

				Double value = getValueFromList(isAvg, attemptList);
				if (value != null) {
					if (wrapper.getAchived() == null) {
						wrapper.setAchived(value + "");
					} else {
						wrapper.setAchived(wrapper.getAchived() + Symbol.SLASH_FORWARD_STRING + value);
					}
					if (symbol.equalsIgnoreCase(Symbol.GRETER_THAN_STRING)) {
						if (value >= criteria) {
							if (wrapper.getStatus() == null) {
								wrapper.setStatus(ReportConstants.PASS);
							}
						} else {
							wrapper.setStatus(ReportConstants.FAIL);
						}
					} else {

						if (value <= criteria) {
							if (wrapper.getStatus() == null) {
								wrapper.setStatus(ReportConstants.PASS);
							}
						} else {
							wrapper.setStatus(ReportConstants.FAIL);
						}

					}
				} else {
					if (wrapper.getAchived() == null) {
						wrapper.setAchived(Symbol.HYPHEN_STRING);
					} else {
						wrapper.setAchived(wrapper.getAchived() + Symbol.SLASH_FORWARD_STRING + Symbol.HYPHEN_STRING);
					}
				}
			}

			else {
				if (wrapper.getAchived() == null) {
					wrapper.setAchived(Symbol.HYPHEN_STRING);
				} else {
					wrapper.setAchived(wrapper.getAchived() + Symbol.SLASH_FORWARD_STRING + Symbol.HYPHEN_STRING);
				}
			}
		}
		wrapper.setTarget(symbol + criteria + unit);

		return wrapper;
	}

	private Double getValueFromList(Boolean isAvg, List<Double> attemptList) {
		if (isAvg) {

			return getAverageFromList(attemptList);
		} else {

			return getMaxFromList(attemptList);

		}
	}

	private Double getMaxFromList(List<Double> attemptList) {
		OptionalDouble optional;
		optional = attemptList.stream().mapToDouble(Double::valueOf).max();
		if (optional.isPresent())
			return ReportUtil.parseToFixedDecimalPlace(optional.getAsDouble(),2);
		return null;
	}

	private Double getAverageFromList(List<Double> dataList) {
		if (Utils.isValidList(dataList)) {
			OptionalDouble optional;
			optional = dataList.stream().mapToDouble(Double::valueOf).average();
			if (optional.isPresent()) {
				return ReportUtil.parseToFixedDecimalPlace(optional.getAsDouble(), 2);
			}
		}
		return null;

	}

	private Double calculateRateForKpi(List<String[]> dataList, Integer attemptIndex, Integer successIndex,
			KPISummaryDataWrapper wrapper) {
		try {
			List<Double> attemptList = ReportUtil.convetArrayToList(dataList, attemptIndex);
			logger.info("attemptList{}, attemptIndex{}", attemptList, attemptIndex);
			List<Double> successList = ReportUtil.convetArrayToList(dataList, successIndex);
			logger.info("successList{}, successIndex{}", attemptIndex, successList);
			if (Utils.isValidList(attemptList)) {
				Integer attemptCount = attemptList.stream().mapToInt(Double::intValue).sum();
				Integer successCount = null;
				if (Utils.isValidList(successList)) {
					successCount = successList.stream().mapToInt(Double::intValue).sum();
				}
				if (attemptCount != null && successCount == null) {
					successCount = 0;

				}
				if (wrapper.getTotal() != null) {
					wrapper.setTotal(attemptCount + wrapper.getTotal());
				} else {
					wrapper.setTotal(attemptCount);
				}
				if (wrapper.getSuccess() != null) {
					wrapper.setSuccess(successCount + wrapper.getSuccess());
				} else {
					wrapper.setSuccess(successCount);

				}
				return ReportUtil.getPercentage(successCount, attemptCount);
			}

		} catch (Exception e) {

			logger.error("Excpetion insoidw the method {}", Utils.getStackTrace(e));
		}
		return null;

	}

	private Map<String, List<String>> getRecipeOperatorMapFromRecipeMappingWrapper(
			RecipeMappingWrapper recipeMappingWrapper) {
		Map<String, List<String>> recipeOperatorMap = null;
		if (recipeMappingWrapper != null && recipeMappingWrapper.getRecpiList() != null
				&& recipeMappingWrapper.getOperatorList() != null) {
			recipeOperatorMap = new HashMap<>();
			recipeOperatorMap.put(QMDLConstant.RECIPE, recipeMappingWrapper.getRecpiList());
			recipeOperatorMap.put(QMDLConstant.OPERATOR, recipeMappingWrapper.getOperatorList());
		}
		return recipeOperatorMap;
	}

	private File proceedToIBCReport(Map<String, Object> imageMap, SSVTReportWrapper mainWrapper) {
		try {

			String reportAssetRepo = ConfigUtils.getString(ReportConstants.IBC_QUICK_REPORT_JASPER_FOLDER_PATH);
			List<SSVTReportWrapper> dataSourceList = new ArrayList<>();
			dataSourceList.add(mainWrapper);
			JRBeanCollectionDataSource rfbeanColDataSource = new JRBeanCollectionDataSource(dataSourceList);
			imageMap.put(ReportConstants.SUBREPORT_DIR, reportAssetRepo);
			imageMap.put(ReportConstants.LOGO_CLIENT_KEY, reportAssetRepo + ReportConstants.LOGO_CLIENT_IMG);
			imageMap.put(ConfigUtils.getString(ReportConstants.NV_REPORT_LOGO_CLIENT_KEY), reportAssetRepo + ReportConstants.LOGO_CLIENT_IMG);
			imageMap.put(ReportConstants.LOGO_NV_KEY, reportAssetRepo + ReportConstants.LOGO_NV_IMG);
			String destinationFileName = mainWrapper.getFileName();
			logger.info("destinationFileName: {}", destinationFileName);
			JasperRunManager.runReportToPdfFile(reportAssetRepo + ReportConstants.MAIN_JASPER, destinationFileName,
					imageMap, rfbeanColDataSource);

			logger.info("Report Created successfully  ");
			return ReportUtil.getIfFileExists(destinationFileName);
		} catch (Exception e) {
			logger.error("@proceedToCreateComplaintReport getting err={}", Utils.getStackTrace(e));
		}
		logger.info(
				"@proceedToCreateComplaintReport going to return null as there has been some problem in generating report");
		return null;
	}
}

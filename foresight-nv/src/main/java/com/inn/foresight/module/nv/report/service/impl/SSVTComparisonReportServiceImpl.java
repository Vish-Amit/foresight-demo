package com.inn.foresight.module.nv.report.service.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inn.commons.Symbol;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.io.FileUtils;
import com.inn.commons.lang.NumberUtils;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.report.dao.IAnalyticsRepositoryDao;
import com.inn.foresight.core.report.model.AnalyticsRepository.progress;
import com.inn.foresight.module.nv.core.workorder.dao.impl.IGenericWorkorderDao;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder.TemplateType;
import com.inn.foresight.module.nv.layer3.constants.NVLayer3Constants;
import com.inn.foresight.module.nv.layer3.constants.QMDLConstant;
import com.inn.foresight.module.nv.report.constant.ReportIndexWrapper;
import com.inn.foresight.module.nv.report.constant.ReportSummaryIndexWrapper;
import com.inn.foresight.module.nv.report.dao.INVReportHbaseDao;
import com.inn.foresight.module.nv.report.service.IMapImagesService;
import com.inn.foresight.module.nv.report.service.IReportService;
import com.inn.foresight.module.nv.report.service.ISSVTComparisonReportService;
import com.inn.foresight.module.nv.report.service.ISSVTReportService;
import com.inn.foresight.module.nv.report.utils.DriveHeaderConstants;
import com.inn.foresight.module.nv.report.utils.QMdlIndexConstants;
import com.inn.foresight.module.nv.report.utils.ReportConstants;
import com.inn.foresight.module.nv.report.utils.ReportUtil;
import com.inn.foresight.module.nv.report.workorder.wrapper.DriveDataWrapper;
import com.inn.foresight.module.nv.report.workorder.wrapper.DriveImageWrapper;
import com.inn.foresight.module.nv.report.workorder.wrapper.KPIWrapper;
import com.inn.foresight.module.nv.report.wrapper.ComparisoGraphWrapper;
import com.inn.foresight.module.nv.report.wrapper.KPIImgDataWrapper;
import com.inn.foresight.module.nv.report.wrapper.KPISummaryDataWrapper;
import com.inn.foresight.module.nv.report.wrapper.NVReportConfigurationWrapper;
import com.inn.foresight.module.nv.report.wrapper.ReportDataHolder;
import com.inn.foresight.module.nv.report.wrapper.SSVTReportSubWrapper;
import com.inn.foresight.module.nv.report.wrapper.SSVTReportWrapper;
import com.inn.foresight.module.nv.report.wrapper.SiteInformationWrapper;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.LiveDriveVoiceAndSmsWrapper;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;
import com.inn.product.systemconfiguration.dao.SystemConfigurationDao;

import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service("SSVTComparisonReportServiceImpl")
public class SSVTComparisonReportServiceImpl implements ISSVTComparisonReportService {
	/** The logger. */
	private Logger logger = LogManager.getLogger(SSVTComparisonReportServiceImpl.class);

	/** The map image service. */

	@Autowired
	private IMapImagesService mapImageService;

	/** The report service. */
	@Autowired
	private IReportService reportService;

	@Autowired
	private ISSVTReportService ssvtReportService;

	@Autowired
	private IGenericWorkorderDao genericWorkorderDao;
	@Autowired
	private SystemConfigurationDao iSystemConfigurationDao;
	@Autowired
	private INVReportHbaseDao nVReportHbaseDao;
	@Autowired
	private IAnalyticsRepositoryDao analyticsrepositoryDao;
	/** The mapper. */
	ObjectMapper mapper = new ObjectMapper();

	@Override
	public Response execute(String json) {
		logger.info("Going to execute the method with json {} ", json);

		try {
			Map<String, Object> jsonMap = reportService.getJsonDataMap(json);
			logger.info("jsonMap {} ", jsonMap);
			Integer previousWoId = (Integer) jsonMap.get(ReportConstants.PREV_WORKORDER_ID);
			Integer currentWoId = (Integer) jsonMap.get(ReportConstants.WORKORDER_ID);
			String assignTo = (String) jsonMap.get(NVLayer3Constants.ASSIGN_TO_JSON_KEY);
			Integer analyticsrepoId = (Integer) jsonMap.get(ForesightConstants.ANALYTICAL_REPORT_KEY);
			List<Integer> allWOIds = new ArrayList<>();
			allWOIds.add(previousWoId);
			allWOIds.add(currentWoId);
			GenericWorkorder workorderObj = genericWorkorderDao.findByPk(currentWoId);

			boolean qmdlParsingStatus = reportService.getFileProcessedStatusForWorkorders(allWOIds);

			if (qmdlParsingStatus) {
				generateReport(assignTo, allWOIds, workorderObj, analyticsrepoId);
			} else {
				analyticsrepositoryDao.updateStatusInAnalyticsRepository(analyticsrepoId, null, "Data is Not Available",
						progress.Failed, null);
			}
		} catch (Exception e1) {
			logger.error("Error inside the method createReportForWorkOrderID for json {} , {} ", json,
					Utils.getStackTrace(e1));
			return Response.ok(ForesightConstants.FAILURE_JSON).build();
		}
		return Response.ok(ForesightConstants.FAILURE_JSON).build();
	}


	private void generateReport(String assignTo, List<Integer> allWOIds, GenericWorkorder workorderObj,
			Integer analyticsrepoId) {
		Map<String, Object> imageMap = new HashMap<>();
		SSVTReportWrapper mainWrapper = new SSVTReportWrapper();
		List<KPIImgDataWrapper> lsitofKpiImg = new ArrayList<>();
		SSVTReportSubWrapper subWrapper = new SSVTReportSubWrapper();
		try {
			subWrapper.setTempFilePath(ReportUtil.getTempFilePath());
			String filePath = getFilePath(workorderObj);
			List<List<List<Double>>> driveRoute = reportService.getAllCustomRoutesOfPrePost(
					Arrays.asList(allWOIds.get(ReportConstants.INDEX_ZER0)),
					Arrays.asList(allWOIds.get(ReportConstants.INDEX_ONE)));
			Boolean isPre = getIsPreDefaultValue(allWOIds);
			List<ComparisoGraphWrapper> graphDataList = new ArrayList<>();
			Map<String, ReportDataHolder> dataMap = new HashMap<>();
			ReportDataHolder dataHolder=getReportDataHolder();
			prepareDataMapAndSetImages(imageMap, allWOIds, lsitofKpiImg, subWrapper, driveRoute, isPre,
					graphDataList, dataMap,dataHolder);
			getKPISumarryList(dataMap, subWrapper,dataHolder);
			setHandOverSummaryData(dataMap, subWrapper,dataHolder);
			subWrapper.setImgList(lsitofKpiImg);
			mainWrapper.setFileName(ReportUtil.getFileName(workorderObj.getWorkorderId(), analyticsrepoId, filePath));
			ReportUtil.setTechnologyFromBand(workorderObj, mainWrapper);
			ssvtReportService.preparedWrapperForJasper(workorderObj,
					ReportUtil.getSiteFromMap(getSiteForReport(workorderObj, getListFromDataMap(dataMap),dataHolder.getKpiIndexMap())),
					subWrapper.getKpiInfoList(), subWrapper, mainWrapper, assignTo, null);
			ssvtReportService.setSiteAuditImageInWrapper(workorderObj, mainWrapper, imageMap);
			logger.info("final image map is {}", imageMap);
			File file = proceedToSSVTWOCompareReport(imageMap, mainWrapper);

			if (file != null) {
				reportService.saveFileAndUpdateStatus(analyticsrepoId, filePath, workorderObj, file, file.getName(),NVWorkorderConstant.REPORT_INSTACE_ID);
			} else {
				analyticsrepositoryDao.updateStatusInAnalyticsRepository(analyticsrepoId, null, "Something Went Wrong",
						progress.Failed, null);
			}

		} catch (Exception e) {
			logger.error("Exception to genrate Report {} ", Utils.getStackTrace(e));
			analyticsrepositoryDao.updateStatusInAnalyticsRepository(analyticsrepoId, null, "Something Went Wrong",
					progress.Failed, null);
		} finally {

			if (subWrapper.getTempFilePath() != null) {
				try {
					FileUtils.deleteDirectory(new File(subWrapper.getTempFilePath()));
				} catch (IOException e) {
					logger.error("Excpetion to Deleting directory {}", subWrapper.getTempFilePath());
				}
			}

		}
	}


	private ReportDataHolder getReportDataHolder() throws IllegalAccessException, NoSuchFieldException {
		List<String> fetchSummaryKPIList = ReportSummaryIndexWrapper.getLiveDriveKPIs();
		Map<String, Integer> summaryKpiIndexMap = ReportSummaryIndexWrapper
				.getLiveDriveKPIIndexMap(fetchSummaryKPIList);
		List<String> fetchKPIList = ReportIndexWrapper.getLiveDriveKPIs();
		Map<String, Integer> kpiIndexMap = ReportIndexWrapper.getLiveDriveKPIIndexMap(fetchKPIList);
		ReportDataHolder dataHolder=new ReportDataHolder();
		dataHolder.setFetchKPIList(fetchKPIList);
		dataHolder.setFetchSummaryKPIList(fetchSummaryKPIList);
		dataHolder.setKpiIndexMap(kpiIndexMap);
		dataHolder.setSummaryKpiIndexMap(summaryKpiIndexMap);
		return dataHolder;
	}

	private void setHandOverSummaryData(Map<String, ReportDataHolder> dataMap, SSVTReportSubWrapper subWrapper, ReportDataHolder dataHolder
			) {
		ReportDataHolder preDTHolder = dataMap.get(ReportConstants.PRE_DT);
		ReportDataHolder postDTHolder = dataMap.get(ReportConstants.POST_DT);
		if (preDTHolder != null) {
			List<LiveDriveVoiceAndSmsWrapper> handoverPlotDataList = ReportUtil.getHandoverPlotDataListForReport(preDTHolder.getRecipeWiseSummaryMap().get(ReportConstants.FTP_DL),dataHolder.getSummaryKpiIndexMap());
			subWrapper.setHandoverPlotDataList(handoverPlotDataList);
			setRRCInformation(
					preDTHolder.getRecipeWiseSummaryMap().get(ReportConstants.KEY_RRC), subWrapper, true,null);
		}
		if (postDTHolder != null) {
			List<LiveDriveVoiceAndSmsWrapper> handoverPlotDataList = ReportUtil.getHandoverPlotDataListForReport(postDTHolder.getRecipeWiseSummaryMap().get(ReportConstants.FTP_DL),dataHolder.getSummaryKpiIndexMap());
			subWrapper.setPostHandoverPlotDataList(handoverPlotDataList);
			setRRCInformation(
					postDTHolder.getRecipeWiseSummaryMap().get(ReportConstants.KEY_RRC), subWrapper, false,null);
		}

	}

	private void setRRCInformation(String[] smmryData, SSVTReportSubWrapper subWrapper, Boolean isPre,Map<String, Integer> summaryKpiIndexMap) {
		try {
			if (smmryData != null && !smmryData[summaryKpiIndexMap.get(ReportConstants.RRC_INITIATE)].isEmpty()
					&& !smmryData[summaryKpiIndexMap.get(ReportConstants.RRC_SUCCESS)].isEmpty()
					&& NumberUtils.isValidNumber(
							Double.parseDouble(smmryData[summaryKpiIndexMap.get(ReportConstants.RRC_INITIATE)]))
					&& NumberUtils.isValidNumber(
							Double.parseDouble(smmryData[summaryKpiIndexMap.get(ReportConstants.RRC_SUCCESS)]))) {
				Double rrcAttempt = Double.parseDouble(smmryData[summaryKpiIndexMap.get(ReportConstants.RRC_INITIATE)]);
				Double rrcSuccess = Double.parseDouble(smmryData[summaryKpiIndexMap.get(ReportConstants.RRC_SUCCESS)]);
				Double val = (rrcSuccess * ReportConstants.INDEX_HUNDRED) / rrcAttempt;
				val = ReportUtil.parseToFixedDecimalPlace(val, ReportConstants.INDEX_TWO);
				if (isPre) {
					subWrapper.setRrcAttempt(rrcAttempt);
					subWrapper.setRrcConnect(rrcSuccess);
					subWrapper.setRrcReqSuccessRate(val);
				} else {

					subWrapper.setPostRrcAttempt(rrcAttempt);
					subWrapper.setPostRrcConnect(rrcSuccess);
					subWrapper.setPostRrcReqSuccessRate(val);

				}
			}
		} catch (Exception e) {
			logger.error("setRRCInformation=== {}", Utils.getStackTrace(e));
		}
	}

	private Boolean prepareDataMapAndSetImages(Map<String, Object> imageMap, List<Integer> allWOIds,
			List<KPIImgDataWrapper> lsitofKpiImg, SSVTReportSubWrapper subWrapper,
			List<List<List<Double>>> driveRoute, Boolean isPre, List<ComparisoGraphWrapper> graphDataList,
			Map<String, ReportDataHolder> dataMap, ReportDataHolder reportDataKpi) throws  IllegalAccessException, NoSuchFieldException {
		for (Integer woid : allWOIds) {
			GenericWorkorder workorderObj =genericWorkorderDao.findByPk(woid);
			ReportDataHolder holder = new ReportDataHolder();
			String mapJson = iSystemConfigurationDao
					.getSystemConfigurationByType(ReportConstants.NV_SSVT_REPORT_RECIPEID_MAP).get(ReportConstants.INDEX_ZER0).getValue();
			Map<String, String> recipeIDMap = ReportUtil.convertCSVStringToMap(mapJson);
			Map<String, Map<String, List<String>>> recipeWiseIDMap = ssvtReportService.getRecipeMapForRecipeId(woid,
					recipeIDMap);

			List<String[]> csvDataArray = reportService.getDriveDataRecipeWiseTaggedForReport(Arrays.asList(workorderObj.getId()),
					recipeWiseIDMap.get(ReportConstants.FTP_DL).get(QMDLConstant.RECIPE), reportDataKpi.getFetchKPIList(),
					reportDataKpi.getKpiIndexMap());
			setDataHolder(driveRoute, woid, holder, recipeWiseIDMap, csvDataArray);
			logger.info("csvDataArray ====== {} woid =={}", csvDataArray.size(), woid);
			populateImageDataInWrapper(lsitofKpiImg, driveRoute, isPre, imageMap, subWrapper, graphDataList, holder,reportDataKpi.getKpiIndexMap());
			setImageSpeedTestImageData(imageMap, workorderObj, subWrapper, isPre, holder, recipeWiseIDMap);
			Map<String, String[]> recipeWiseSummaryMap = ssvtReportService.getRecipeWiseSummary(recipeWiseIDMap,
					woid,reportDataKpi.getFetchSummaryKPIList());
			logger.info("recipeWiseSummaryMap is {}  woid {}",recipeWiseSummaryMap ,woid);
			holder.setRecipeWiseSummaryMap(recipeWiseSummaryMap);
			holder.setKpiIndexMap(reportDataKpi.getKpiIndexMap());
			holder.setSummaryKpiIndexMap(reportDataKpi.getSummaryKpiIndexMap());
			holder.setDataList(csvDataArray);
			setDataMap(isPre, dataMap, holder);
			if (isPre != null) {
				isPre = true;
			}
		}
		return isPre;
	}

	private void setImageSpeedTestImageData(Map<String, Object> imageMap, GenericWorkorder workorderObj,
			SSVTReportSubWrapper subWrapper, Boolean isPre, ReportDataHolder holder,
			Map<String, Map<String, List<String>>> recipeWiseIDMap) {
		try {
			if(recipeWiseIDMap.get(ReportConstants.STATIONARY)!=null) {
			List<DriveDataWrapper> imageDataList = reportService.getSpeedTestDatafromHbase(workorderObj.getId(),
					recipeWiseIDMap.get(ReportConstants.STATIONARY).get(QMDLConstant.RECIPE));
			holder.setImageDataList(imageDataList);
			imageMap.putAll(getSectorWiseImageFromList(holder.getImageDataList(), isPre, subWrapper.getTempFilePath()));

			}
		} catch (Exception e) {
			logger.error("Exception setImageSpeedTestImageData {}",Utils.getStackTrace(e));
		}
	}

	private String getFilePath(GenericWorkorder workorderObj) {
		return ConfigUtils.getString(ReportConstants.NV_REPORTS_PATH) + ReportConstants.SSVT
				+ ReportConstants.FORWARD_SLASH + "NV_SSVT_POST_DT" + ReportConstants.FORWARD_SLASH
				+ workorderObj.getGwoMeta().get(ReportConstants.SITE_ID) + ReportConstants.FORWARD_SLASH;
	}

	private void getKPISumarryList(Map<String, ReportDataHolder> dataMap, SSVTReportSubWrapper subWrapper, ReportDataHolder dataHolder) {
		List<KPISummaryDataWrapper> kpiSummary = new ArrayList<>();
		ReportDataHolder preDTHolder = dataMap.get(ReportConstants.PRE_DT);
		ReportDataHolder postDTHolder = dataMap.get(ReportConstants.POST_DT);
		try {
			for (KPIWrapper kpiWrapper : preDTHolder.getKpiList()) {
				if (ReportConstants.RSRP.equalsIgnoreCase(kpiWrapper.getKpiName())) {
					KPISummaryDataWrapper kpiSummaryWrapper = new KPISummaryDataWrapper();

					kpiSummary.add(setKpiSummaryFromRawData(preDTHolder, postDTHolder, kpiWrapper, kpiSummaryWrapper,
							-105.0, "RSRP > -105 (%)", Symbol.PERCENT_STRING));

				} else if (ReportConstants.SINR.equalsIgnoreCase(kpiWrapper.getKpiName())) {
					KPISummaryDataWrapper kpiSummaryWrapper = new KPISummaryDataWrapper();

					kpiSummary.add(setKpiSummaryFromRawData(preDTHolder, postDTHolder, kpiWrapper, kpiSummaryWrapper,
							3.0, "SINR > 3 (%)", Symbol.PERCENT_STRING));

				} else if (ReportConstants.DL.equalsIgnoreCase(kpiWrapper.getKpiName())) {
					setDlSummary(kpiSummary, preDTHolder, postDTHolder, kpiWrapper);

				}
			}
			getSummaryDataForLaye3Parameter(kpiSummary, preDTHolder, postDTHolder,dataHolder);
		} catch (Exception e) {
			logger.error("Exception inside the method getKPISumarryList {}", Utils.getStackTrace(e));
		}

		subWrapper.setKpiInfoList(kpiSummary);

	}

	private void setDlSummary(List<KPISummaryDataWrapper> kpiSummary, ReportDataHolder preDTHolder,
			ReportDataHolder postDTHolder, KPIWrapper kpiWrapper) {
		try {
			Double prePeakValue = null;
			Double postPeakValue = null;
			KPISummaryDataWrapper kpiSummaryWrapper = new KPISummaryDataWrapper();

			kpiSummary.add(setKpiSummaryFromRawData(preDTHolder, postDTHolder, kpiWrapper, kpiSummaryWrapper, 5.0,
					"DL THP > 5 Mbps (%)", Symbol.PERCENT_STRING));
			KPISummaryDataWrapper kpiSummaryWrapper1 = new KPISummaryDataWrapper();
			Optional<Double> preOptional = ReportUtil
					.convetArrayToList(preDTHolder.getDataList(), kpiWrapper.getIndexKPI()).stream()
					.max(Comparator.comparing(Double::valueOf));
			Optional<Double> postOptional = ReportUtil
					.convetArrayToList(postDTHolder.getDataList(), kpiWrapper.getIndexKPI()).stream()
					.max(Comparator.comparing(Double::valueOf));
			if (preOptional.isPresent()) {
				prePeakValue = preOptional.get();
			}
			if (postOptional.isPresent()) {
				postPeakValue = postOptional.get();
			}
			setKpiSummaryWrapper(kpiSummaryWrapper1, "Peak Value (Single Sample)", prePeakValue, postPeakValue,
					ReportConstants.MBPS);
			kpiSummary.add(kpiSummaryWrapper1);
		} catch (Exception e) {

			logger.error("Exception inside the method {}", Utils.getStackTrace(e));
		}
	}

	private void getSummaryDataForLaye3Parameter(List<KPISummaryDataWrapper> kpiSummary, ReportDataHolder preDTHolder,
			ReportDataHolder postDTHolder, ReportDataHolder dataHolder) {
		try {
			List<String> summaryFeildArrayList = new ArrayList<>(Arrays.asList(ReportConstants.ERAB_DROP_RATE,
					ReportConstants.HANDOVER_SUCCESS_RATE, ReportConstants.VOLTE_SETUP_SUCCESS_RATE,
					ReportConstants.VOLTE_ERAB_DROP_RATE, ReportConstants.RRC_CONNECTION_REQUEST_SUCCESS));
			List<Object[]> objectList = genericWorkorderDao
					.getNVReportConfiguration(TemplateType.NV_SSVT_FULL.toString());
			List<NVReportConfigurationWrapper> nvWrapperList = ReportUtil.getNVReportconfiguratioinList(objectList);
			for (String key : summaryFeildArrayList) {
				List<NVReportConfigurationWrapper> filterList = nvWrapperList.stream()
						.filter(wrapper -> wrapper.getKpi().equalsIgnoreCase(key)).collect(Collectors.toList());
				KPISummaryDataWrapper wrapper = getKPiSummaryDataWrapperForSVVTAutomationReport(key, filterList,
						preDTHolder, postDTHolder,dataHolder);
				if (wrapper != null) {
					kpiSummary.add(wrapper);
				}
			}
		} catch (Exception e) {

			logger.error("Exception inside the method getSummaryDataForLaye3Parameter {}", Utils.getStackTrace(e));
		}
	}

	public KPISummaryDataWrapper getKPiSummaryDataWrapperForSVVTAutomationReport(String key,
			List<NVReportConfigurationWrapper> filterList, ReportDataHolder preDTHolder,
			ReportDataHolder postDTHolder, ReportDataHolder dataHolder) {
		KPISummaryDataWrapper wrapper = new KPISummaryDataWrapper();
		try {
			KPISummaryDataWrapper preKpiWrapper = null;
			KPISummaryDataWrapper postKpiWrapper = null;
			wrapper.setTestName(key);
			wrapper.setTarget(
					(filterList != null && !filterList.isEmpty()) ? filterList.get(0).getTargetvalue() : null);
			NVReportConfigurationWrapper nvWrapper = (filterList != null && !filterList.isEmpty()) ? filterList.get(0)
					: null;
			switch (key) {

			case ReportConstants.NETVELOCITY_SPEED_TEST:
				return setNetvelocitySpeedTest(preDTHolder, postDTHolder, wrapper);

			case ReportConstants.RRC_CONNECTION_REQUEST_SUCCESS:
			case ReportConstants.RRC_CONNECTION_SUCCESS_RATE:
				preKpiWrapper =  ReportUtil.getRRCSuccessRate(preDTHolder.getRecipeWiseSummaryMap().get(ReportConstants.KEY_RRC), wrapper, nvWrapper,
						preDTHolder.getSummaryKpiIndexMap().get(ReportConstants.RRC_INITIATE),
						preDTHolder.getSummaryKpiIndexMap().get(ReportConstants.RRC_SUCCESS));
				postKpiWrapper = ReportUtil.getRRCSuccessRate(postDTHolder.getRecipeWiseSummaryMap().get(ReportConstants.KEY_RRC), wrapper, nvWrapper,
						postDTHolder.getSummaryKpiIndexMap().get(ReportConstants.RRC_INITIATE),
						postDTHolder.getSummaryKpiIndexMap().get(ReportConstants.RRC_SUCCESS));
				return getFinalSummaryWrapper(preKpiWrapper, postKpiWrapper);

			case ReportConstants.VOLTE_ERAB_DROP_RATE:
					preKpiWrapper=ReportUtil.getVolteErabDropRate(key,preDTHolder.getRecipeWiseSummaryMap().get(ReportConstants.KEY_VOLTE), wrapper, nvWrapper,
						preDTHolder.getSummaryKpiIndexMap().get(ReportConstants.CALL_INITIATE),
						preDTHolder.getSummaryKpiIndexMap().get(ReportConstants.CALL_DROP));
					postKpiWrapper=ReportUtil.getVolteErabDropRate(key,postDTHolder.getRecipeWiseSummaryMap().get(ReportConstants.KEY_VOLTE), wrapper, nvWrapper,
							postDTHolder.getSummaryKpiIndexMap().get(ReportConstants.CALL_INITIATE),
							postDTHolder.getSummaryKpiIndexMap().get(ReportConstants.CALL_DROP));
				
				return getFinalSummaryWrapper(preKpiWrapper, postKpiWrapper);

			case ReportConstants.ERAB_DROP_RATE:
				preKpiWrapper=ReportUtil.getErabDropRate1(key, preDTHolder.getRecipeWiseSummaryMap().get(ReportConstants.KEY_RRC), wrapper, nvWrapper,
						preDTHolder.getSummaryKpiIndexMap().get(ReportConstants.INITIAL_ERAB_SUCCESS_COUNT),
						preDTHolder.getSummaryKpiIndexMap().get(ReportConstants.RRC_DROPPED));
				postKpiWrapper = ReportUtil.getErabDropRate1(key, postDTHolder.getRecipeWiseSummaryMap().get(ReportConstants.KEY_RRC), wrapper, nvWrapper,
						postDTHolder.getSummaryKpiIndexMap().get(ReportConstants.INITIAL_ERAB_SUCCESS_COUNT),
						postDTHolder.getSummaryKpiIndexMap().get(ReportConstants.RRC_DROPPED));
				return getFinalSummaryWrapper(preKpiWrapper, postKpiWrapper);

			case ReportConstants.HANDOVER_SUCCESS_RATE:
				preKpiWrapper = ReportUtil. getHandoverSuccessRate(preDTHolder.getRecipeWiseSummaryMap().get(ReportConstants.FTP_DL), wrapper, nvWrapper,
						preDTHolder.getSummaryKpiIndexMap().get(ReportConstants.HANDOVER_SUCCESS),
						preDTHolder.getSummaryKpiIndexMap().get(ReportConstants.HANDOVER_INITIATE));
				
				postKpiWrapper=	ReportUtil. getHandoverSuccessRate(postDTHolder.getRecipeWiseSummaryMap().get(ReportConstants.FTP_DL), wrapper, nvWrapper,
						postDTHolder.getSummaryKpiIndexMap().get(ReportConstants.HANDOVER_SUCCESS),
						postDTHolder.getSummaryKpiIndexMap().get(ReportConstants.HANDOVER_INITIATE));
				return getFinalSummaryWrapper(preKpiWrapper, postKpiWrapper);

			case ReportConstants.VOLTE_SETUP_SUCCESS_RATE:
				preKpiWrapper = ReportUtil.getVolteSetupSucessRate1(preDTHolder.getRecipeWiseSummaryMap().get(ReportConstants.KEY_VOLTE), wrapper, nvWrapper,
						preDTHolder.getSummaryKpiIndexMap().get(ReportConstants.CALL_INITIATE),
						preDTHolder.getSummaryKpiIndexMap().get(ReportConstants.CALL_SETUP_SUCCESS));
				
				postKpiWrapper = ReportUtil.getVolteSetupSucessRate1(postDTHolder.getRecipeWiseSummaryMap().get(ReportConstants.KEY_VOLTE), wrapper, nvWrapper,
						postDTHolder.getSummaryKpiIndexMap().get(ReportConstants.CALL_INITIATE),
						postDTHolder.getSummaryKpiIndexMap().get(ReportConstants.CALL_SETUP_SUCCESS));
				return getFinalSummaryWrapper(preKpiWrapper, postKpiWrapper);

			default:
				break;
			}
		} catch (Exception e) {
			logger.error("Exception inside method getKPiSummaryDataWrapperForKey for key {}  , {} ", key,
					Utils.getStackTrace(e));
		}
		return wrapper;
	}

	private KPISummaryDataWrapper setNetvelocitySpeedTest(ReportDataHolder preDTHolder, ReportDataHolder postDTHolder,
			KPISummaryDataWrapper wrapper) {
		try {
			List<Double> dlSpeedtestList = null;
			List<Double> postDlSpeed = null;
			if (Utils.isValidList(preDTHolder.getImageDataList())) {
				dlSpeedtestList = preDTHolder.getImageDataList().stream().map(DriveDataWrapper::getDltpt)
						.collect(Collectors.toList());
				dlSpeedtestList = dlSpeedtestList != null
						? dlSpeedtestList.stream().distinct().collect(Collectors.toList())
						: dlSpeedtestList;

			}
			if (Utils.isValidList(postDTHolder.getImageDataList())) {
				postDlSpeed = postDTHolder.getImageDataList().stream().map(DriveDataWrapper::getDltpt)
						.collect(Collectors.toList());
				postDlSpeed = postDlSpeed != null ? postDlSpeed.stream().distinct().collect(Collectors.toList())
						: postDlSpeed;

			}
			if (dlSpeedtestList != null && postDlSpeed != null && Utils.isValidList(postDlSpeed) && Utils.isValidList(dlSpeedtestList)) {
				Optional<Double> postOptional = postDlSpeed.stream().max(Comparator.comparing(Double::valueOf));
				Optional<Double> preOptional=	dlSpeedtestList.stream().max(Comparator.comparing(Double::valueOf));
				if (postOptional.isPresent() && preOptional.isPresent()) {
					Double postValue = postOptional.get();
					Double preValue = preOptional.get();
					wrapper.setStatus(postValue >= preValue ? ReportConstants.PASS : ReportConstants.FAIL);
				}

				setPreDlSpeedValue(wrapper, dlSpeedtestList, true);
				setPreDlSpeedValue(wrapper, postDlSpeed, false);

			}

			else if (Utils.isValidList(postDlSpeed)) {
				wrapper.setStatus("NA");

				setPreDlSpeedValue(wrapper, postDlSpeed, false);

			} else if (Utils.isValidList(dlSpeedtestList)) {
				wrapper.setStatus("NA");

				setPreDlSpeedValue(wrapper, dlSpeedtestList, true);

			}

			wrapper.setTestName("Netvelocity DownLink Speed Test");
			wrapper.setSource(ReportConstants.DT_MOBILE);
			return wrapper;

		} catch (Exception e) {
			logger.error("Exception inside the method setNetvelocitySpeedTest {}", Utils.getStackTrace(e));
		}

		return null;
	}

	private void setPreDlSpeedValue(KPISummaryDataWrapper wrapper, List<Double> dlSpeedtestList, boolean b) {
		for (Double dlvalue : dlSpeedtestList) {
			if (dlvalue != null) {
				dlvalue = ReportUtil.parseToFixedDecimalPlace(dlvalue, 2);
				if (b) {
					if (wrapper.getAchived() != null && !wrapper.getAchived().isEmpty()) {
						wrapper.setAchived(wrapper.getAchived() + dlvalue + ReportConstants.FORWARD_SLASH);
					} else {
						wrapper.setAchived(dlvalue + ReportConstants.FORWARD_SLASH);
					}
				} else {
					if (wrapper.getPostAchived() != null && !wrapper.getPostAchived().isEmpty()) {
						wrapper.setPostAchived(wrapper.getPostAchived() + dlvalue + ReportConstants.FORWARD_SLASH);
					} else {
						wrapper.setPostAchived(dlvalue + ReportConstants.FORWARD_SLASH);
					}
				}

			}
		}
	}

	private KPISummaryDataWrapper getFinalSummaryWrapper(KPISummaryDataWrapper preKpiWrapper,
			KPISummaryDataWrapper postKpiWrapper) {

		String preString = extractPercentageAndUnit(preKpiWrapper.getAchived());
		String poString = extractPercentageAndUnit(postKpiWrapper.getAchived());

		if (preString != null && poString != null) {
			preKpiWrapper.setAchived(preKpiWrapper.getAchived());
			preKpiWrapper.setPostAchived(postKpiWrapper.getAchived());
			preKpiWrapper.setStatus(Double.parseDouble(poString) >= Double.parseDouble(preString) ? ReportConstants.PASS
					: ReportConstants.FAIL);
			return preKpiWrapper;
		} else if (preString == null) {
			if (poString != null) {
				postKpiWrapper.setPostAchived(postKpiWrapper.getAchived());
				postKpiWrapper.setAchived(Symbol.HASH_STRING);
				return postKpiWrapper;
			}
		}

		else {
			return preKpiWrapper;
		}
		return preKpiWrapper;

	}

	private KPISummaryDataWrapper setKpiSummaryFromRawData(ReportDataHolder preDTHolder, ReportDataHolder postDTHolder,
			KPIWrapper wrapper, KPISummaryDataWrapper kpiSummaryWrapper, Double criteria, String testName,
			String unit) {

		try {
			List<Double> prersrpList = ReportUtil.convetArrayToList(preDTHolder.getDataList(), wrapper.getIndexKPI());
			Long preFilterCount = prersrpList.stream().filter(x -> x > criteria).collect(Collectors.counting());
			Double preValue = ReportUtil.getPercentage(preFilterCount.intValue(), prersrpList.size());

			List<Double> postrsrpList = ReportUtil.convetArrayToList(postDTHolder.getDataList(), wrapper.getIndexKPI());
			Long postFilterCount = postrsrpList.stream().filter(x -> x > criteria).collect(Collectors.counting());
			Double postValue = ReportUtil.getPercentage(postFilterCount.intValue(), postrsrpList.size());
			setKpiSummaryWrapper(kpiSummaryWrapper, testName, preValue, postValue, unit);
			return kpiSummaryWrapper;
		} catch (Exception e) {
			logger.error("Exception inside the method setKpiSummaryFromRawData {}", Utils.getStackTrace(e));
		}
		return kpiSummaryWrapper;
	}

	private void setKpiSummaryWrapper(KPISummaryDataWrapper kpiSummaryWrapper, String testName, Double preValue,
			Double postValue, String unit) {
		kpiSummaryWrapper.setTestName(testName);
		kpiSummaryWrapper.setAchived(String.valueOf(preValue) + unit);
		kpiSummaryWrapper.setPostAchived(postValue!=null?String.valueOf(postValue) + unit:Symbol.HYPHEN_STRING);
		kpiSummaryWrapper.setStatus((postValue!=null &&postValue >= preValue ? ReportConstants.PASS : ReportConstants.FAIL));
		kpiSummaryWrapper.setSource(ReportConstants.DT_MOBILE);
	}

	private String extractPercentageAndUnit(String value) {
		if (value != null && (value.contains(Symbol.PERCENT_STRING) || value.contains(ReportConstants.MBPS))) {
			value = value.replace(Symbol.PERCENT_STRING, Symbol.EMPTY_STRING).replace(ReportConstants.MBPS, Symbol.EMPTY_STRING).trim();
		} else if (value != null  &&(value.contains(Symbol.HYPHEN_STRING) || value.contains(Symbol.SLASH_FORWARD_STRING))) {
			return value;
		}
		
		return value;
	}

	private List<String[]> getListFromDataMap(Map<String, ReportDataHolder> dataMap) {
		List<String[]> list = new ArrayList<>();
		Collection<ReportDataHolder> dataList = dataMap.values();
		for (ReportDataHolder reportDataHolder : dataList) {
			list.addAll(reportDataHolder.getDataList());
		}
		return list;
	}

	private void setDataMap(Boolean isPre, Map<String, ReportDataHolder> dataMap, ReportDataHolder holder) {
		if (!isPre) {
			dataMap.put(ReportConstants.PRE_DT, holder);
		} else {
			dataMap.put(ReportConstants.POST_DT, holder);

		}
	}

	private void setDataHolder(List<List<List<Double>>> driveRoute, Integer woid, ReportDataHolder holder,
			Map<String, Map<String, List<String>>> recipeWiseIDMap, List<String[]> csvDataArray) {
		holder.setDataList(csvDataArray);
		holder.setRecipeWiseIDMap(recipeWiseIDMap);
		holder.setWoId(woid);
		holder.setDriveRoute(driveRoute);
	}

	private File proceedToSSVTWOCompareReport(Map<String, Object> imageMap, SSVTReportWrapper mainWrapper) {
		try {
			String reportAssetRepo = ConfigUtils
					.getString(ReportConstants.SSVT_WO_COMPARISON_REPORT_JASPER_FOLDER_PATH);

			List<SSVTReportWrapper> dataSourceList = new ArrayList<>();
			dataSourceList.add(mainWrapper);
			JRBeanCollectionDataSource rfbeanColDataSource = new JRBeanCollectionDataSource(dataSourceList);
			imageMap.put(ReportConstants.SUBREPORT_DIR, reportAssetRepo);
			imageMap.put(ReportConstants.LOGO_CLIENT_KEY, reportAssetRepo + ReportConstants.LOGO_CLIENT_IMG);
			imageMap.put(ConfigUtils.getString(ReportConstants.NV_REPORT_LOGO_CLIENT_KEY), reportAssetRepo + ReportConstants.LOGO_CLIENT_IMG);
			imageMap.put(ReportConstants.LOGO_NV_KEY, reportAssetRepo + ReportConstants.LOGO_NV_IMG);
			String destinationFileName = mainWrapper.getFileName();
			JasperRunManager.runReportToPdfFile(reportAssetRepo + ReportConstants.MAIN_JASPER, destinationFileName,
					imageMap, rfbeanColDataSource);
			logger.info("Report Created successfully  ");
			return ReportUtil.getIfFileExists(destinationFileName);
		} catch (Exception e) {
			logger.error("@proceedToSSVTWOCompareReport getting err={}", Utils.getStackTrace(e));
		}
		logger.info(
				"@proceedToCreateComplaintReport going to return null as there has been some problem in generating report");
		return null;
	}

	private void populateImageDataInWrapper(List<KPIImgDataWrapper> lsitofKpiImg, List<List<List<Double>>> driveRoute,
			Boolean isPre, Map<String, Object> imageMap, SSVTReportSubWrapper subWrapper,
			List<ComparisoGraphWrapper> graphDataList, ReportDataHolder holder, Map<String, Integer> kpiIndexMap) {
		GenericWorkorder workorderObj = genericWorkorderDao.findByPk(holder.getWoId());
		List<KPIWrapper> kpiList = ssvtReportService.getKpiStatsList(workorderObj,
				holder.getRecipeWiseIDMap().get(ReportConstants.FTP_DL), getKpiIndexMap(kpiIndexMap));
		DriveImageWrapper driveImageWrapper = getDriveImageWrapperDataForImageGenearation(driveRoute, kpiList,
				workorderObj, holder.getDataList(),kpiIndexMap);
		getListofImages(driveImageWrapper, kpiList, lsitofKpiImg, isPre, imageMap, subWrapper,kpiIndexMap);
		setPredictionCoverageImge(ssvtReportService.getCoveragePredictionImage(driveImageWrapper), imageMap, isPre);
		populateGraphData(graphDataList, kpiList, holder.getDataList(), isPre, subWrapper);
		subWrapper.setGraphDataList(graphDataList);
		holder.setKpiList(kpiList);
	}

	private List<ComparisoGraphWrapper> populateGraphData(List<ComparisoGraphWrapper> graphDataList,
			List<KPIWrapper> kpiList, List<String[]> arlist, Boolean isPre, SSVTReportSubWrapper subWrapper) {
		for (KPIWrapper kpiWrapper : kpiList) {
			if (!kpiWrapper.getIndexKPI().toString().equalsIgnoreCase(DriveHeaderConstants.INDEX_PCI.toString())
					&& !kpiWrapper.getIndexKPI().toString()
							.equalsIgnoreCase(DriveHeaderConstants.INDEX_CELLID.toString())
					&& !kpiWrapper.getIndexKPI().toString()
							.equalsIgnoreCase(DriveHeaderConstants.HANDOVER_PLOT_INDEX.toString())) {
				if (!isPre) {
					if (kpiWrapper.getIndexKPI().toString()
							.equalsIgnoreCase(DriveHeaderConstants.INDEX_DL_EARFCN.toString())) {
						subWrapper.setEarfcnList(reportService.setDataForEarfcnGraph(arlist, null));

					} else {
						ComparisoGraphWrapper wrapper = new ComparisoGraphWrapper();
						wrapper.setKpiName(
								kpiWrapper.getKpiName().replaceAll(ReportConstants.UNDERSCORE, ReportConstants.SPACE));
						List<Double> dataList = ReportUtil.convetArrayToList(arlist, kpiWrapper.getIndexKPI());
						if (Utils.isValidList(dataList)) {
							wrapper.setPreDriveList(ReportUtil.setGraphDataForKpi(kpiWrapper, dataList));
							graphDataList.add(wrapper);
						}
					}
				} else {
					if (kpiWrapper.getIndexKPI().toString()
							.equalsIgnoreCase(DriveHeaderConstants.INDEX_DL_EARFCN.toString())) {
						subWrapper.setEarfcnPostDataList(reportService.setDataForEarfcnGraph(arlist, null));

					} else {
						List<ComparisoGraphWrapper> kpiImgWrapperList = graphDataList.stream()
								.filter(obj -> obj.getKpiName().equalsIgnoreCase(kpiWrapper.getKpiName()
										.replaceAll(ReportConstants.UNDERSCORE, ReportConstants.SPACE)))
								.collect(Collectors.toList());
						if (kpiImgWrapperList != null && !kpiImgWrapperList.isEmpty()) {
							logger.info("inside the else to set image for kpi {}", kpiWrapper.getKpiName());
							ComparisoGraphWrapper wrapper = kpiImgWrapperList.get(ReportConstants.INDEX_ZER0);
							wrapper.setPostDriveList(ReportUtil.setGraphDataForKpi(kpiWrapper,
									ReportUtil.convetArrayToList(arlist, kpiWrapper.getIndexKPI())));

						}

					}
				}
			}
		}
		return graphDataList;

	}

	private void setPredictionCoverageImge(HashMap<String, String> coveragePredictionImage,
			Map<String, Object> imageMap, Boolean isPre) {
		if (coveragePredictionImage != null) {
			if (!isPre) {
				imageMap.put("Pre" + ReportConstants.ATOLL_RSRP_IMAGE, coveragePredictionImage.get(ReportConstants.ATOLL
						+ ReportConstants.UNDERSCORE + DriveHeaderConstants.INDEX_RSRP.toString()));
				imageMap.put("Pre" + ReportConstants.ATOLL_RSRP_LEGEND_IMAGE,
						coveragePredictionImage.get(ReportConstants.ATOLL_LEGEND + ReportConstants.UNDERSCORE
								+ DriveHeaderConstants.INDEX_RSRP.toString()));

			} else {
				imageMap.put("Post" + ReportConstants.ATOLL_RSRP_IMAGE,
						coveragePredictionImage.get(ReportConstants.ATOLL + ReportConstants.UNDERSCORE
								+ DriveHeaderConstants.INDEX_RSRP.toString()));
				imageMap.put("Post" + ReportConstants.ATOLL_RSRP_LEGEND_IMAGE,
						coveragePredictionImage.get(ReportConstants.ATOLL_LEGEND + ReportConstants.UNDERSCORE
								+ DriveHeaderConstants.INDEX_RSRP.toString()));

			}
		}

	}

	private DriveImageWrapper getDriveImageWrapperDataForImageGenearation(List<List<List<Double>>> driveRoute,
			List<KPIWrapper> kpiList, GenericWorkorder workorderObj, List<String[]> csvDataArray, Map<String, Integer> kpiIndexMap) {

		Map<String, Object> siteDataMap = getSiteForReport(workorderObj, csvDataArray,kpiIndexMap);
		List<SiteInformationWrapper> siteInfoList= ReportUtil.filterNEDataByBand(ReportUtil.getSiteFromMap(siteDataMap), ReportUtil.findBandDetailByGWOMetaData(workorderObj));

		return new DriveImageWrapper(csvDataArray, kpiIndexMap.get(ReportConstants.LATITUDE), kpiIndexMap.get(ReportConstants.LONGITUDE),
				kpiIndexMap.get(ReportConstants.PCI_PLOT), kpiList, siteInfoList, null, driveRoute);
	}

	private Map<String, Object> getSiteForReport(GenericWorkorder workorderObj, List<String[]> csvDataArray, Map<String, Integer> kpiIndexMap) {
		
		try {
			Map<String, Long> driveTimeStampMap = ReportUtil.getDriveTimeStampMap(csvDataArray,kpiIndexMap);

			return reportService.getSiteDataForReport(workorderObj, driveTimeStampMap.get(ReportConstants.START_TIMESTAMP),
					driveTimeStampMap.get(ReportConstants.END_TIMESTAMP), csvDataArray, false, false);
		} catch (Exception e) {
			logger.error("getSiteForReport ===={}",Utils.getStackTrace(e));
		}
		return null;
	}

	private Boolean getIsPreDefaultValue(List<Integer> list) {
		if (list != null && list.size() == ReportConstants.INDEX_TWO) {
			return false;
		}
		return null;

	}

	private List<KPIImgDataWrapper> getListofImages(DriveImageWrapper driveImageWrapper, List<KPIWrapper> kpiList,
			List<KPIImgDataWrapper> listofKpiImg, Boolean isPre, Map<String, Object> imageMap,
			SSVTReportSubWrapper subWrapper, Map<String, Integer> kpiIndexMap) {
		logger.info("Inside method getListofImages for workOrderIds");
		try {
			HashMap<String, BufferedImage> driveImageMap = mapImageService.getDriveImages(driveImageWrapper, null);
			HashMap<String, BufferedImage> legendImageMap = mapImageService.getLegendImages(kpiList,
					driveImageWrapper.getDataKPIs());
			if (isPre) {
				setSiteImage(driveImageMap, imageMap);
			}
			kpiList.stream().forEach(kpiWrapperObj -> {
				try {
					if ((kpiIndexMap.containsKey(ReportConstants.PCI_PLOT)&&kpiWrapperObj.getIndexKPI().toString()
							.equalsIgnoreCase(kpiIndexMap.get(ReportConstants.PCI_PLOT).toString()))
							|| (kpiIndexMap.containsKey(ReportConstants.EARFCN)&&kpiWrapperObj.getIndexKPI().toString()
									.equalsIgnoreCase(kpiIndexMap.get(ReportConstants.EARFCN).toString()))
							|| (kpiIndexMap.containsKey(DriveHeaderConstants.CGI)&&kpiWrapperObj.getIndexKPI().toString()
									.equalsIgnoreCase(kpiIndexMap.get(DriveHeaderConstants.CGI).toString()))) {
						setCustomizedData(isPre, kpiWrapperObj, listofKpiImg, driveImageMap);
					} else if (ReportConstants.HANDOVER_PLOT.equalsIgnoreCase(kpiWrapperObj.getKpiName())) {
						logger.info("HANDOVER_PLOT ====");
						setHandOverPlot(imageMap, driveImageMap, isPre, subWrapper);
					} else {
						setDataInWrapper(isPre, kpiWrapperObj, listofKpiImg, driveImageMap, legendImageMap);
					}
				} catch (Exception e) {
					logger.error("Exception inside method getListofImages {} ", Utils.getStackTrace(e));
				}
			});
			return listofKpiImg;
		} catch (Exception e) {
			logger.error("Exception inside method getListofImages {} ", Utils.getStackTrace(e));
		}
		return listofKpiImg;
	}

	private void setHandOverPlot(Map<String, Object> imageMap, HashMap<String, BufferedImage> driveImageMap,
			Boolean isPre, SSVTReportSubWrapper subWrapper) {
		logger.info("subWrapper.getTempFilePath() {}", subWrapper.getTempFilePath());
		ReportUtil.createDirectory(subWrapper.getTempFilePath());
		HashMap<String, BufferedImage> newdriveImageMap = new HashMap<>();
		if (!isPre) {
			newdriveImageMap.put("Pre" + DriveHeaderConstants.HANDOVER_PLOT_INDEX,
					driveImageMap.get(DriveHeaderConstants.HANDOVER_PLOT_INDEX + ""));
			HashMap<String, String> map = mapImageService.saveDriveImages(newdriveImageMap,
					subWrapper.getTempFilePath(), false);
			imageMap.put("Pre" + ReportConstants.HANDOVER_PLOT,
					map.get("Pre" + DriveHeaderConstants.HANDOVER_PLOT_INDEX));
		} else {
			newdriveImageMap.put("Post" + DriveHeaderConstants.HANDOVER_PLOT_INDEX,
					driveImageMap.get(DriveHeaderConstants.HANDOVER_PLOT_INDEX + ""));
			HashMap<String, String> map = mapImageService.saveDriveImages(newdriveImageMap,
					subWrapper.getTempFilePath(), false);
			imageMap.put("Post" + ReportConstants.HANDOVER_PLOT,
					map.get("Post" + DriveHeaderConstants.HANDOVER_PLOT_INDEX));

		}
		logger.info("imageMap ===={}", imageMap);
	}

	private void setSiteImage(HashMap<String, BufferedImage> driveImageMap, Map<String, Object> map) {
		HashMap<String, BufferedImage> newdriveImageMap = new HashMap<>();
		newdriveImageMap.put(ReportConstants.SITE_IMAGE, driveImageMap.get(ReportConstants.SITE_IMAGE));
		HashMap<String, String> imageMap = mapImageService.saveDriveImages(newdriveImageMap,
				ConfigUtils.getString(ReportConstants.IMAGE_PATH_FOR_NV_REPORT) + ReportConstants.SSVT
						+ ReportConstants.FORWARD_SLASH + ReportConstants.ATOLL.toUpperCase()
						+ ReportConstants.FORWARD_SLASH
						+ ReportUtil.getFormattedDate(new Date(), ReportConstants.DATE_FORMAT_DD_MMM_YY_HH_MM_SSSSS)
						+ ReportConstants.FORWARD_SLASH,
				false);
		map.put(ReportConstants.IMAGE_SITE, imageMap.get(ReportConstants.SITE_IMAGE));

	}

	private void setCustomizedData(Boolean isPre, KPIWrapper kpiWrapperObj, List<KPIImgDataWrapper> listofKpiImg,
			Map<String, BufferedImage> driveImageMap) {
		logger.info("Inside method setCustomizedData  is Pre {} , kpiwrapperObj {} ", isPre, kpiWrapperObj);
		try {
			if (!isPre) {
				KPIImgDataWrapper kpiImgWrapper = new KPIImgDataWrapper();
				kpiImgWrapper.setKpiName(
						kpiWrapperObj.getKpiName().replaceAll(ReportConstants.UNDERSCORE, ReportConstants.SPACE));
				kpiImgWrapper.setBeforImg(ReportUtil.getInputStreamFromBufferedImage(
						driveImageMap.get(kpiWrapperObj.getIndexKPI() + ReportConstants.BLANK_STRING)));
				if (kpiWrapperObj.getKpiName().equalsIgnoreCase(ReportConstants.PCI_PLOT)) {
					kpiImgWrapper.setBeforLegendImg(
							ReportUtil.getInputStreamFromBufferedImage(driveImageMap.get(ReportConstants.KEY_LEGENDS)));
				} else if (kpiWrapperObj.getKpiName().equalsIgnoreCase(DriveHeaderConstants.CGI)) {
					kpiImgWrapper.setBeforLegendImg(
							ReportUtil.getInputStreamFromBufferedImage(driveImageMap.get(DriveHeaderConstants.CGI)));

				}

				else {
					kpiImgWrapper.setBeforLegendImg(
							ReportUtil.getInputStreamFromBufferedImage(driveImageMap.get(kpiWrapperObj.getKpiName())));
				}
				kpiImgWrapper.setAvgBeforeKpi(kpiWrapperObj.getAverageValue());
				listofKpiImg.add(kpiImgWrapper);
			} else {
				List<KPIImgDataWrapper> kpiImgWrapperList = listofKpiImg.stream()
						.filter(obj -> obj.getKpiName().equalsIgnoreCase(kpiWrapperObj.getKpiName()
								.replaceAll(ReportConstants.UNDERSCORE, ReportConstants.SPACE)))
						.collect(Collectors.toList());
				logger.info("kpiImgWrapperList {}", kpiImgWrapperList);
				if (kpiImgWrapperList != null && !kpiImgWrapperList.isEmpty()) {
					KPIImgDataWrapper kpiImgWrapper = kpiImgWrapperList.get(ReportConstants.INDEX_ZER0);
					kpiImgWrapper.setAfterImg(ReportUtil.getInputStreamFromBufferedImage(
							driveImageMap.get(kpiWrapperObj.getIndexKPI() + ReportConstants.BLANK_STRING)));
					if (kpiWrapperObj.getKpiName().equalsIgnoreCase(ReportConstants.PCI_PLOT)) {
						kpiImgWrapper.setAfterLegendImg(ReportUtil
								.getInputStreamFromBufferedImage(driveImageMap.get(ReportConstants.KEY_LEGENDS)));
					} else if (kpiWrapperObj.getKpiName().equalsIgnoreCase(DriveHeaderConstants.CGI)) {
						kpiImgWrapper.setAfterLegendImg(ReportUtil
								.getInputStreamFromBufferedImage(driveImageMap.get(DriveHeaderConstants.CGI)));

					} else {
						kpiImgWrapper.setAfterLegendImg(ReportUtil
								.getInputStreamFromBufferedImage(driveImageMap.get(kpiWrapperObj.getKpiName())));
					}
					kpiImgWrapper.setAvgAfterKpi(kpiWrapperObj.getAverageValue());
				}
			}
		} catch (Exception e) {
			logger.info("Exception inside method setDataInWrapper {} ", e.getMessage());
		}
	}

	private void setDataInWrapper(Boolean isPre, KPIWrapper kpiWrapperObj, List<KPIImgDataWrapper> listofKpiImg,
			Map<String, BufferedImage> driveImageMap, Map<String, BufferedImage> legendImageMap) {
		logger.info("Inside method setDataInWrapper is Pre {} , kpiwrapperObj {} ", isPre, kpiWrapperObj);
		try {
			if (!isPre) {
				logger.info("inside the if to set image of kpiName {}", kpiWrapperObj.getKpiName());
				InputStream is = ReportUtil.getInputStreamFromBufferedImage(
						driveImageMap.get(kpiWrapperObj.getIndexKPI() + ReportConstants.BLANK_STRING));
				if (is != null) {
					KPIImgDataWrapper kpiImgWrapper = new KPIImgDataWrapper();
					kpiImgWrapper.setKpiUnit(ReportUtil.getUnitByKPiName(kpiWrapperObj.getKpiName()));
					kpiImgWrapper.setKpiName(
							kpiWrapperObj.getKpiName().replaceAll(ReportConstants.UNDERSCORE, ReportConstants.SPACE));
					kpiImgWrapper.setBeforImg(is);
					kpiImgWrapper.setBeforLegendImg(ReportUtil.getInputStreamFromBufferedImage(legendImageMap
							.get(ReportConstants.LEGEND + ReportConstants.UNDERSCORE + kpiWrapperObj.getIndexKPI())));
					if (kpiWrapperObj.getKpiName().equalsIgnoreCase(ReportConstants.ROUTE)) {
						logger.info("Setting Route info");
						kpiImgWrapper
								.setBeforImg(ReportUtil.getInputStreamFromBufferedImage(driveImageMap.get("TERRAIN0")));
					}
					listofKpiImg.add(kpiImgWrapper);
				} else {
					logger.info("Input Stream is null for isPre {} , kpi Name {} ", isPre, kpiWrapperObj.getKpiName());
				}
			} else {
				List<KPIImgDataWrapper> kpiImgWrapperList = listofKpiImg.stream()
						.filter(obj -> obj.getKpiName().equalsIgnoreCase(kpiWrapperObj.getKpiName()
								.replaceAll(ReportConstants.UNDERSCORE, ReportConstants.SPACE)))
						.collect(Collectors.toList());
				logger.info("kpiImgWrapperList {}", kpiImgWrapperList);
				if (kpiImgWrapperList != null && !kpiImgWrapperList.isEmpty()) {
					logger.info("inside the else to set image for kpi {}", kpiWrapperObj.getKpiName());
					KPIImgDataWrapper kpiImgWrapper = kpiImgWrapperList.get(ReportConstants.INDEX_ZER0);
					kpiImgWrapper.setAfterImg(ReportUtil.getInputStreamFromBufferedImage(
							driveImageMap.get(kpiWrapperObj.getIndexKPI() + ReportConstants.BLANK_STRING)));
					kpiImgWrapper.setAfterLegendImg(ReportUtil.getInputStreamFromBufferedImage(legendImageMap
							.get(ReportConstants.LEGEND + ReportConstants.UNDERSCORE + kpiWrapperObj.getIndexKPI())));
					if (kpiWrapperObj.getKpiName().equalsIgnoreCase(ReportConstants.ROUTE)) {
						logger.info("Setting Route info");
						kpiImgWrapper
								.setBeforImg(ReportUtil.getInputStreamFromBufferedImage(driveImageMap.get("TERRAIN0")));
					}
				}
			}
		} catch (Exception e) {
			logger.info("Exception inside method setDataInWrapper {} ", e.getMessage());
		}
	}

	public Map<String, Integer> getKpiIndexMap(Map<String, Integer> kpiIndexMap) {
		Map<String, Integer> map = new HashMap<>();
		map.put(DriveHeaderConstants.RSRP, kpiIndexMap.get(ReportConstants.RSRP));
		map.put(DriveHeaderConstants.SINR, kpiIndexMap.get(ReportConstants.SINR));
		map.put(DriveHeaderConstants.DL_THROUGHPUT, kpiIndexMap.get(ReportConstants.DL_THROUGHPUT));
		map.put(DriveHeaderConstants.UL_THROUGHPUT,  kpiIndexMap.get(ReportConstants.UL_THROUGHPUT));
		map.put(DriveHeaderConstants.PCI_PLOT,  kpiIndexMap.get(ReportConstants.PCI_PLOT));
		map.put(DriveHeaderConstants.DL_EARFCN, kpiIndexMap.get(ReportConstants.EARFCN));
		map.put(DriveHeaderConstants.CGI, kpiIndexMap.get(DriveHeaderConstants.CGI));
		map.put(ReportConstants.HANDOVER_PLOT, kpiIndexMap.get(ReportConstants.HANDOVER_PLOT));

		return map;
	}

	private Map<String, Object> getSectorWiseImageFromList(List<DriveDataWrapper> imageDataList, boolean isPre,
			String filePath) {
		Map<String, Object> sectorImageMap = new HashMap<>();

		if(imageDataList!=null) {
		logger.info("inside method getSectorWiseImageFromList {} ", imageDataList.size());
		Integer i = 1;
		for (DriveDataWrapper driveData : imageDataList) {
			try {
				InputStream in = new ByteArrayInputStream(driveData.getImg());
				BufferedImage buffredImage = ImageIO.read(in);
				ImageIO.write(buffredImage, ReportConstants.JPG, new File(
						filePath + (isPre ? "Post" : "Pre") + ReportConstants.SECTOR + i + ReportConstants.DOT_JPG));
				sectorImageMap.put(ReportConstants.SECTOR + i, ConfigUtils.getString(ReportConstants.SSVT_REPORT_PATH)
						+ ReportConstants.SECTOR + i + ReportConstants.DOT_JPG);
				i++;
			} catch (Exception e) {
				logger.warn("Inside method getSectorWiseImageFromList {}", e.getMessage());
			}
		}
		logger.debug("sectorImageMap data {}", sectorImageMap);
		return sectorImageMap;
		}
		return null;
	}

	


}

package com.inn.foresight.module.nv.report.service.impl;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.imageio.ImageIO;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.inn.commons.Symbol;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.lang.DateUtils;
import com.inn.commons.lang.StringUtils;
import com.inn.commons.unit.Duration;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.report.dao.IAnalyticsRepositoryDao;
import com.inn.foresight.core.report.model.AnalyticsRepository;
import com.inn.foresight.core.report.model.AnalyticsRepository.progress;
import com.inn.foresight.module.nv.core.workorder.dao.impl.IGenericWorkorderDao;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;
import com.inn.foresight.module.nv.layer3.constants.QMDLConstant;
import com.inn.foresight.module.nv.layer3.utils.NVLayer3Utils;
import com.inn.foresight.module.nv.layer3.utils.NetworkDataFormats;
import com.inn.foresight.module.nv.report.constant.ReportIndexWrapper;
import com.inn.foresight.module.nv.report.customreport.ssvt.constants.SSVTConstants;
import com.inn.foresight.module.nv.report.customreport.ssvt.wrapper.SSVTCellWiseWrapper;
import com.inn.foresight.module.nv.report.service.IMapImagesService;
import com.inn.foresight.module.nv.report.service.IReportService;
import com.inn.foresight.module.nv.report.service.ISSVT2gReportService;
import com.inn.foresight.module.nv.report.service.ISSVTExcelReportService;
import com.inn.foresight.module.nv.report.service.ISSVTReportService;
import com.inn.foresight.module.nv.report.service.Inbuilding.Utils.InbuildingReportUtil;
import com.inn.foresight.module.nv.report.utils.DriveHeaderConstants;
import com.inn.foresight.module.nv.report.utils.ReportConstants;
import com.inn.foresight.module.nv.report.utils.ReportUtil;
import com.inn.foresight.module.nv.report.utils.Statistics;
import com.inn.foresight.module.nv.report.workorder.wrapper.DriveImageWrapper;
import com.inn.foresight.module.nv.report.workorder.wrapper.KPIWrapper;
import com.inn.foresight.module.nv.report.wrapper.GraphDataWrapper;
import com.inn.foresight.module.nv.report.wrapper.GraphWrapper;
import com.inn.foresight.module.nv.report.wrapper.MessageDetailWrapper;
import com.inn.foresight.module.nv.report.wrapper.MessageKpiWrapper;
import com.inn.foresight.module.nv.report.wrapper.SSVTReportSubWrapper;
import com.inn.foresight.module.nv.report.wrapper.SSVTReportWrapper;
import com.inn.foresight.module.nv.report.wrapper.SectorSwapWrapper;
import com.inn.foresight.module.nv.report.wrapper.SiteInformationWrapper;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;
import com.inn.foresight.module.nv.workorder.dao.IWORecipeMappingDao;
import com.inn.foresight.module.nv.workorder.model.WORecipeMapping;
import com.inn.foresight.module.nv.workorder.model.WORecipeMapping.Status;
import com.inn.product.legends.dao.ILegendRangeDao;
import com.inn.product.legends.utils.LegendWrapper;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service("SSVTExcelReportServiceImpl")
public class SSVTExcelReportServiceImpl implements ISSVTExcelReportService {

	private static final String CALIBRI = "Calibri";

	private static final String SUCCESS = "Success";

	private Logger logger = LogManager.getLogger(SSVTExcelReportServiceImpl.class);

	@Autowired
	private IGenericWorkorderDao genericWorkorderDao;
	@Autowired
	private IReportService reportService;
	@Autowired
	private IWORecipeMappingDao recipeMappingDao;
	@Autowired
	private IAnalyticsRepositoryDao analyticsrepositoryDao;
	@Autowired
	private IMapImagesService mapImageService;

	private ObjectMapper mapper = new ObjectMapper();
	@Autowired
	private ILegendRangeDao legendRangeDao;

	@Autowired
	private ISSVTReportService iSSVTReportService;

	@Autowired
	private ISSVT2gReportService ssvt2gReportService;

	@Transactional
	public Response execute(String json) {
		logger.info("Inside execute method to create KPIComparison Report with json {} ", json);
		Integer analyticsrepoId = null;
		try {
			Map<String, Object> jsonMap = reportService.getJsonDataMap(json);
			if (jsonMap!=null) {
				logger.info(" jsonamap {}", new Gson().toJson(jsonMap));
				Integer workorderId = (Integer) jsonMap.get(ReportConstants.WORKORDER_ID);
				logger.info("workorderId >>>>>>>>>> {} ", workorderId);
				analyticsrepoId = (Integer) jsonMap.get(ReportConstants.ANALYTICS_REPOSITORY_ID);
				//			Integer recipeId = (Integer) jsonMap.get(ReportConstants.RECIPEID);
				GenericWorkorder genericWorkorder = genericWorkorderDao.findByPk(workorderId);
				//			boolean isFilesProcessed = reportService.getFileProcessedStatusForRecipeAndWorkorder(workorderId, recipeId);
				boolean iSiteAcceptance = false;
				if (InbuildingReportUtil.validateMap(genericWorkorder.getGwoMeta(), SSVTConstants.IS_SITE_ACCEPTANCE)) {
					iSiteAcceptance = Boolean
							.parseBoolean(genericWorkorder.getGwoMeta().get(SSVTConstants.IS_SITE_ACCEPTANCE));
				}
				if (iSiteAcceptance) {
					ssvt2gReportService.createReport(workorderId, jsonMap, analyticsrepoId, genericWorkorder);
				} else {
					return createReport(workorderId, jsonMap, analyticsrepoId, genericWorkorder);
				} 
			}

		} catch (Exception e1) {
			logger.error("Error nside the method createReportForWorkOrderID for json {} , {} ", json,
					Utils.getStackTrace(e1));
			analyticsrepositoryDao.updateStatusInAnalyticsRepository(analyticsrepoId, null, "Something Went Wrong",
					progress.Failed, null);
		}
		return Response.ok(ForesightConstants.FAILURE_JSON).build();
	}

	public Response createReport(Integer workorderId, Map<String, Object> jsonMap, Integer analyticsrepoId,
			GenericWorkorder genericWorkorder) throws IllegalAccessException, NoSuchFieldException {
		logger.info("inside the method  createReport workorderId {}", workorderId);

		if (genericWorkorder != null && genericWorkorder.getGwoMeta().containsKey(NVWorkorderConstant.RECIPE_PCI_MAP)) {
			
			SSVTReportWrapper mainWrapper = new SSVTReportWrapper();
			SSVTReportSubWrapper subwrapper = new SSVTReportSubWrapper();
			AnalyticsRepository analyticsRepository = analyticsrepositoryDao.findByPk(analyticsrepoId);
			String band = InbuildingReportUtil.getBandFromMap(genericWorkorder);
			Map<String, Object> imagemap = new HashMap<>();
			List<String> fetchKPIList = ReportIndexWrapper.getLiveDriveKPIs();
			Map<String, Integer> kpiIndexMap = ReportIndexWrapper.getLiveDriveKPIIndexMap(fetchKPIList);
			Map<String, Integer> recipePciMap = getRecipePCIMap(genericWorkorder);
			List<WORecipeMapping> recipeMappings = recipeMappingDao.getWORecipeByGWOId(workorderId);
			List<SiteInformationWrapper> siteInfoList = reportService.getSiteDataForSSVTReport(genericWorkorder);
			
			Map<Integer, Map<String, List<String[]>>> pciWiseDriveRecipeDataMap = getPciWiseDriveData(workorderId,
					fetchKPIList, kpiIndexMap, recipePciMap, recipeMappings);
			Map<Integer, Map<String, List<String[]>>> pciWiseStRecipeDataMap = getPciWiseStData(workorderId,
					fetchKPIList, kpiIndexMap, recipePciMap, recipeMappings);
			
			iSSVTReportService.setSiteAuditImageInWrapper(genericWorkorder, mainWrapper, imagemap);
			setSiteDataToWrapper(siteInfoList, subwrapper, genericWorkorder);
			setMessageDataForSSVTExcel(workorderId, subwrapper, recipeMappings, recipePciMap);
			List<String[]> driveData = getDriveData(workorderId, fetchKPIList, kpiIndexMap, recipeMappings,recipePciMap);
			ReportUtil.addPciInEmptyFields(driveData, kpiIndexMap);
			Map<Integer, List<String[]>> pciWiseHandoverDataMap = getPciWiseHandoverData(workorderId, recipePciMap, recipeMappings);
			Map<Integer, List<String[]>> pciWiseSTDataMap = getPciWiseStationaryData(workorderId, fetchKPIList,kpiIndexMap, recipePciMap, recipeMappings);
			List<SSVTCellWiseWrapper> volteDataList = setVolteTestData(kpiIndexMap,pciWiseStRecipeDataMap, pciWiseDriveRecipeDataMap);
			List<SSVTCellWiseWrapper> cellWiseList = prepareCellWiseDataWrapper(kpiIndexMap, pciWiseDriveRecipeDataMap,
					pciWiseHandoverDataMap, recipePciMap, band, pciWiseStRecipeDataMap);
			setSubWrapperData(subwrapper, genericWorkorder, siteInfoList);
			
			List<GraphWrapper> graphjPlotList = getPlotsDataForDlUl(driveData, kpiIndexMap, siteInfoList);
			List<GraphWrapper> voltePlotList = getPlotsDataForLongCall(driveData, kpiIndexMap, siteInfoList);
			List<GraphWrapper> stGraphjPlotList = getGraphAndPlotsForSt(pciWiseSTDataMap, kpiIndexMap, siteInfoList);
			List<GraphWrapper> screenshotImgList = getScreenShotForDriveData(pciWiseSTDataMap, kpiIndexMap);
			
			subwrapper.setVolteDataList(volteDataList);
			subwrapper.setCellList(cellWiseList);
			subwrapper.setGraphplotList(graphjPlotList);
			subwrapper.setVoltePlotList(voltePlotList);
			subwrapper.setStationaryDataList(stGraphjPlotList);
			subwrapper.setRsrpList(screenshotImgList);
			mainWrapper.setSubWrapperList(Arrays.asList(subwrapper));
			
			String filePath = proceedToCreateReport(mainWrapper, genericWorkorder, jsonMap, imagemap);
			if (filePath != null) {
				return saveFileToHdfsAndUpdateStatus(analyticsRepository, filePath, genericWorkorder);
			} else {
				return Response.ok(ForesightConstants.FAILURE_JSON).build();
			}
		}
		return null;
	}

	@Override
	public void setMessageDataForSSVT(Integer workorderId, SSVTReportSubWrapper subwrapper,
			Map<String, List<String>> recipeOperatorListMap, Map<String, Integer> recipePciMap) {
		
		List<MessageDetailWrapper> messageDataList = reportService.getLayer3MessagesDataForReport(workorderId,
				recipeOperatorListMap.get(QMDLConstant.RECIPE),null);
		List<MessageDetailWrapper> pciTaggedList = carryForwardPciInWrapper(messageDataList);
		Map<Integer, List<MessageDetailWrapper>> pciWiseMessageMap = getPciWiseMessageDataList(pciTaggedList,
				recipePciMap);
		setAttachMessageDataToWrapper(subwrapper, pciWiseMessageMap);
		setDetachMessageDataToWrapper(subwrapper, pciWiseMessageMap);
		setCSMOMessageDataToWrapper(subwrapper, pciWiseMessageMap);
		setCSMTMessageDataToWrapper(subwrapper, pciWiseMessageMap);
		setVoLTEMTMessageDataToWrapper(subwrapper, pciWiseMessageMap);
		setVoLTEMOMessageDataToWrapper(subwrapper, pciWiseMessageMap);
		setFastReturnMessageDataToWrapper(subwrapper, pciWiseMessageMap);
		setRRCMessageDataToWrapper(subwrapper, pciWiseMessageMap);
	}

	private List<GraphWrapper> getGraphAndPlotsForSt(Map<Integer, List<String[]>> pciWiseSTDataMap,
			Map<String, Integer> kpiIndexMap, List<SiteInformationWrapper> siteInfoList) {
		List<GraphWrapper> graphList = new ArrayList<>();
		try {

			for (Entry<Integer, List<String[]>> entry : pciWiseSTDataMap.entrySet()) {
				List<String[]> dlDataList = ReportUtil.filterDataByTestType(entry.getValue(),
						kpiIndexMap.get(ReportConstants.TEST_TYPE), NetworkDataFormats.TEST_TYPE_HTTP_DOWNLOAD,
						NetworkDataFormats.TEST_TYPE_FTP_DOWNLOAD);
				GraphWrapper wrapper = getPdschThroughputGraphData(kpiIndexMap, dlDataList, siteInfoList);
				if (wrapper != null) {

					wrapper.setEventLegendImg(getGraphkpiImage(kpiIndexMap, dlDataList, entry.getKey(), "dL"));
					wrapper.setTechnology(setKpiStringForImage(kpiIndexMap, dlDataList, entry.getKey(), "dL"));
					graphList.add(wrapper);
				}
				GraphWrapper puschWrapper = getPuschThroughputGraphData(kpiIndexMap, entry, siteInfoList);
				graphList.add(puschWrapper);

			}
			// logger.info(" graphList for stationary {}", new Gson().toJson(graphList));
		} catch (Exception e) {
			logger.info(" error getGraphAndPlotsForSt {}", Utils.getStackTrace(e));
		}

		return graphList;
	}

	private GraphWrapper getPdschThroughputGraphData(Map<String, Integer> kpiIndexMap, List<String[]> dlDataList,
			List<SiteInformationWrapper> siteInfoList) {
		GraphWrapper wrapper = new GraphWrapper();
		if (Utils.isValidList(dlDataList)) {
			List<GraphDataWrapper> graphDataList = new ArrayList<>();
			DriveImageWrapper driveImageWrapper = new DriveImageWrapper(dlDataList,
					kpiIndexMap.get(ReportConstants.LATITUDE), kpiIndexMap.get(ReportConstants.LONGITUDE),
					kpiIndexMap.get(ReportConstants.PCI_PLOT), null, siteInfoList);
			HashMap<String, String> map = getStationaryImage(kpiIndexMap, dlDataList, driveImageWrapper,
					ReportConstants.PDSCH_THROUGHPUT);
			Integer graphpci = 0;
			if (kpiIndexMap.containsKey(ReportConstants.PCI_PLOT)) {

				IntStream pci = dlDataList.stream()
						.filter(x -> (x[kpiIndexMap.get(ReportConstants.PCI_PLOT)] != null
								&& !x[kpiIndexMap.get(ReportConstants.PCI_PLOT)].isEmpty()))
						.map(x -> x[kpiIndexMap.get(ReportConstants.PCI_PLOT)]).collect(Collectors.toList()).stream()
						.mapToInt(i -> Integer.parseInt(i));
				if (pci != null) {
					graphpci = pci.findFirst().getAsInt();
				}
			}
			wrapper.setKpiPlotImg(map.get(ReportConstants.PDSCH_THROUGHPUT));
			wrapper.setMax(getMaxValueFromIndex(kpiIndexMap.get(ReportConstants.PDSCH_THROUGHPUT), dlDataList));
			wrapper.setMean(getAvgOfIndexData(kpiIndexMap.get(ReportConstants.PDSCH_THROUGHPUT), dlDataList));
			wrapper.setMin(getMinValueFromIndex(kpiIndexMap.get(ReportConstants.PDSCH_THROUGHPUT), dlDataList));
			wrapper.setKpiName(Symbol.PARENTHESIS_OPEN_STRING
					+ ReportConstants.DL.replace(Symbol.UNDERSCORE_STRING, Symbol.SPACE_STRING) + Symbol.SPACE_STRING
					+ ReportConstants.PCI + Symbol.UNDERSCORE_STRING + graphpci + Symbol.PARENTHESIS_CLOSE_STRING);

			for (String[] arr : dlDataList) {
				if (arr != null && ReportUtil.checkIndexValue(kpiIndexMap.get(ReportConstants.TIMESTAMP), arr)
						&& ReportUtil.checkIndexValue(kpiIndexMap.get(ReportConstants.PDSCH_THROUGHPUT), arr)) {
					GraphDataWrapper gaphWrapper = new GraphDataWrapper();
					gaphWrapper.setTime(new Date(Long.parseLong(arr[kpiIndexMap.get(ReportConstants.TIMESTAMP)])));
					gaphWrapper.setValue(Double.parseDouble(arr[kpiIndexMap.get(ReportConstants.PDSCH_THROUGHPUT)]));
					graphDataList.add(gaphWrapper);
				}
			}
			wrapper.setGraphDataList(graphDataList);
			return wrapper;

		}
		return null;
	}

	private GraphWrapper getPuschThroughputGraphData(Map<String, Integer> kpiIndexMap,
			Entry<Integer, List<String[]>> entry, List<SiteInformationWrapper> siteInfoList) {
		GraphWrapper wrapper = new GraphWrapper();
		List<String[]> ulDataList = ReportUtil.filterDataByTestType(entry.getValue(),
				kpiIndexMap.get(ReportConstants.TEST_TYPE), NetworkDataFormats.TEST_TYPE_HTTP_UPLOAD,
				NetworkDataFormats.TEST_TYPE_FTP_UPLOAD);
		if (Utils.isValidList(ulDataList)) {

			DriveImageWrapper driveImageWrapper = new DriveImageWrapper(ulDataList,
					kpiIndexMap.get(ReportConstants.LATITUDE), kpiIndexMap.get(ReportConstants.LONGITUDE),
					kpiIndexMap.get(ReportConstants.PCI_PLOT), null, siteInfoList);
			HashMap<String, String> map = getStationaryImage(kpiIndexMap, ulDataList, driveImageWrapper,
					ReportConstants.PUSCH_THROUGHPUT);
			List<GraphDataWrapper> graphDataList = new ArrayList<>();
			Integer graphpci = 0;
			if (kpiIndexMap.containsKey(ReportConstants.PCI_PLOT)) {

				IntStream pci = ulDataList.stream()
						.filter(x -> (x[kpiIndexMap.get(ReportConstants.PCI_PLOT)] != null
								&& !x[kpiIndexMap.get(ReportConstants.PCI_PLOT)].isEmpty()))
						.map(x -> x[kpiIndexMap.get(ReportConstants.PCI_PLOT)]).collect(Collectors.toList()).stream()
						.mapToInt(i -> Integer.parseInt(i));
				if (pci != null) {
					graphpci = pci.findFirst().getAsInt();
				}
			}
			wrapper.setKpiPlotImg(map.get(ReportConstants.PUSCH_THROUGHPUT));
			wrapper.setMax(getMaxValueFromIndex(kpiIndexMap.get(ReportConstants.PUSCH_THROUGHPUT), ulDataList));
			wrapper.setMean(getAvgOfIndexData(kpiIndexMap.get(ReportConstants.PUSCH_THROUGHPUT), ulDataList));
			wrapper.setMin(getMinValueFromIndex(kpiIndexMap.get(ReportConstants.PUSCH_THROUGHPUT), ulDataList));
			wrapper.setKpiName(Symbol.PARENTHESIS_OPEN_STRING
					+ ReportConstants.UL.replace(Symbol.UNDERSCORE_STRING, Symbol.SPACE_STRING) + Symbol.SPACE_STRING
					+ ReportConstants.PCI + Symbol.UNDERSCORE_STRING + graphpci + Symbol.PARENTHESIS_CLOSE_STRING);
			for (String[] arr : ulDataList) {
				if (arr != null && ReportUtil.checkIndexValue(kpiIndexMap.get(ReportConstants.TIMESTAMP), arr)
						&& ReportUtil.checkIndexValue(kpiIndexMap.get(ReportConstants.PUSCH_THROUGHPUT), arr)) {
					GraphDataWrapper gaphWrapper = new GraphDataWrapper();
					gaphWrapper.setTime(new Date(Long.parseLong(arr[kpiIndexMap.get(ReportConstants.TIMESTAMP)])));
					gaphWrapper.setValue(Double.parseDouble(arr[kpiIndexMap.get(ReportConstants.PUSCH_THROUGHPUT)]));
					graphDataList.add(gaphWrapper);
				}
			}
			wrapper.setGraphDataList(graphDataList);
		}
		return wrapper;
	}

	private HashMap<String, String> getStationaryImage(Map<String, Integer> kpiIndexMap, List<String[]> ulDataList,
			DriveImageWrapper driveImageWrapper, String key) {
		List<Double[]> pinLonLatList = getPinLatLongList(ulDataList, kpiIndexMap);

		HashMap<String, BufferedImage> map = mapImageService.getStationaryImages(driveImageWrapper, pinLonLatList, key);
		String saveImagePath = (ConfigUtils.getString(ReportConstants.IMAGE_PATH_FOR_NV_REPORT) + ReportConstants.SSVT
				+ ReportConstants.FORWARD_SLASH
				+ ReportUtil.getFormattedDate(new Date(), ReportConstants.DATE_FORMAT_DD_MM_YY_HH_SS)
				+ ReportConstants.FORWARD_SLASH).toString();
		return mapImageService.saveDriveImages(map, saveImagePath, false);
	}

	private List<Double[]> getPinLatLongList(List<String[]> ulDataList, Map<String, Integer> kpiIndexMap) {
		for (String[] arr : ulDataList) {
			if (ReportUtil.checkIndexValue(kpiIndexMap.get(ReportConstants.LATITUDE), arr)
					&& ReportUtil.checkIndexValue(kpiIndexMap.get(ReportConstants.LONGITUDE), arr)) {
				List<Double[]> list = new ArrayList<>();
				Double[] ar = new Double[2];
				ar[0] = Double.parseDouble(arr[kpiIndexMap.get(ReportConstants.LONGITUDE)]);
				ar[1] = Double.parseDouble(arr[kpiIndexMap.get(ReportConstants.LATITUDE)]);
				list.add(ar);
				return list;
			}
		}
		return null;
	}

	private void setGraphDataAndPlotImages(Map<String, Integer> kpiIndexMap, List<GraphWrapper> graphList,
			List<LegendWrapper> legendList, List<String[]> dataList, Map<String, Integer> filterKpiMap, String category,
			List<SiteInformationWrapper> siteInfoList) {
		List<KPIWrapper> kpiList = ReportUtil.convertLegendsListToKpiWrapperList(legendList, filterKpiMap);
		List<SiteInformationWrapper> siteList = reportService.getSiteDataForReportByDataList(dataList, kpiIndexMap);
		siteList.addAll(siteInfoList);

		DriveImageWrapper driveImageWrapper = new DriveImageWrapper(dataList, kpiIndexMap.get(ReportConstants.LATITUDE),
				kpiIndexMap.get(ReportConstants.LONGITUDE), kpiIndexMap.get(ReportConstants.PCI_PLOT), kpiList,
				siteList);
		try {
			HashMap<String, String> imageMap = getImagesForReport(kpiList, driveImageWrapper, kpiIndexMap);

			for (KPIWrapper kpiWrapper : kpiList) {
				if (ReportConstants.PCI_PLOT.equalsIgnoreCase(kpiWrapper.getKpiName())) {
					GraphWrapper graphWrapper = new GraphWrapper();
					graphWrapper.setKpiName(
							ReportConstants.PCI_PLOT.replace(Symbol.UNDERSCORE_STRING, Symbol.SPACE_STRING));
					graphWrapper.setKpiPlotImg(
							imageMap.get(kpiIndexMap.get(kpiWrapper.getKpiName()) + Symbol.EMPTY_STRING));
					graphWrapper.setKpiLegendImg(imageMap.get(ReportConstants.PCI_LEGEND));
					graphList.add(graphWrapper);

				} else if (ReportConstants.VOLTE_CODEC.equalsIgnoreCase(kpiWrapper.getKpiName())) {
					logger.info("inside the method VOLTE_CODEC ");
					GraphWrapper graphWrapper = setDataForCodecGraph(dataList,kpiIndexMap);
					graphWrapper.setKpiName(
							ReportConstants.VOLTE_CODEC.replace(Symbol.UNDERSCORE_STRING, Symbol.SPACE_STRING));
					graphWrapper.setKpiPlotImg(
							imageMap.get(kpiIndexMap.get(kpiWrapper.getKpiName()) + Symbol.EMPTY_STRING));
					graphWrapper.setKpiLegendImg(imageMap.get(ReportConstants.VOLTE_CODEC));
					graphList.add(graphWrapper);
				} else if (ReportConstants.DL_EARFCN.equalsIgnoreCase(kpiWrapper.getKpiName())) {
					logger.info("inside the method DL_EARFCN ");
					GraphWrapper graphWrapper = setGraphDataForKpi(kpiWrapper,
							ReportUtil.convetArrayToList(dataList, kpiWrapper.getIndexKPI()));
					graphWrapper.setKpiName(ReportConstants.EARFCN);
					graphWrapper.setKpiPlotImg(
							imageMap.get(kpiIndexMap.get(kpiWrapper.getKpiName()) + Symbol.EMPTY_STRING));
					graphWrapper.setKpiLegendImg(imageMap.get(ReportConstants.DL_EARFCN));
					graphList.add(graphWrapper);
				} else if (DriveHeaderConstants.ROUTE.equalsIgnoreCase(kpiWrapper.getKpiName())) {
					logger.info("inside the method route ");
					GraphWrapper graphWrapper = setGraphDataForKpi(kpiWrapper,
							ReportUtil.convetArrayToList(dataList, kpiWrapper.getIndexKPI()));
					graphWrapper.setKpiName(ReportConstants.ROUTE);
					graphWrapper.setKpiPlotImg(
							imageMap.get(kpiIndexMap.get(kpiWrapper.getKpiName()) + Symbol.EMPTY_STRING));
					// graphWrapper.setKpiLegendImg(imageMap.get(ReportConstants.ROUTE));
					graphList.add(graphWrapper);
				} else if (ReportConstants.TECHNOLOGY.equalsIgnoreCase(kpiWrapper.getKpiName())) {
					logger.info("inside the method TECHNOLOGY ");
					GraphWrapper graphWrapper = setGraphDataForKpi(kpiWrapper,
							ReportUtil.convetArrayToList(dataList, kpiWrapper.getIndexKPI()));
					graphWrapper.setKpiPlotImg(
							imageMap.get(kpiIndexMap.get(DriveHeaderConstants.TECHNOLOGY) + Symbol.EMPTY_STRING));
					graphWrapper.setKpiLegendImg(imageMap.get(ReportConstants.LEGEND_TECHNOLOGY));
					graphList.add(graphWrapper);
				} else {
					GraphWrapper graphWrapper = setGraphDataForKpi(kpiWrapper,
							ReportUtil.convetArrayToList(dataList, kpiWrapper.getIndexKPI()));
					graphWrapper.setKpiPlotImg(
							imageMap.get(kpiIndexMap.get(kpiWrapper.getKpiName()) + Symbol.EMPTY_STRING));
					graphWrapper.setKpiLegendImg(imageMap.get(ReportConstants.LEGEND + ReportConstants.UNDERSCORE
							+ kpiIndexMap.get(kpiWrapper.getKpiName())));
					if (category != null) {
						graphWrapper.setKpiName(
								"VoLTE " + "(" + category.replace(Symbol.UNDERSCORE_STRING, Symbol.SPACE_STRING) + ")"
										+ Symbol.SPACE_STRING + graphWrapper.getKpiName());
					}
					graphList.add(graphWrapper);
				}
			}
		} catch (IOException e) {
			logger.error("Exception inside the mehthod getGraphAndPlotsData {}", Utils.getStackTrace(e));
		}
	}

	private List<SSVTCellWiseWrapper> setVolteTestData(
			Map<String, Integer> kpiIndexMap, Map<Integer, Map<String, List<String[]>>> pciWiseStRecipeDataMap, Map<Integer, Map<String, List<String[]>>> pciWiseDriveRecipeDataMap) {
		List<SSVTCellWiseWrapper> list = new ArrayList<>();
		for (Entry<Integer, Map<String, List<String[]>>> entry : pciWiseStRecipeDataMap.entrySet()) {
			if (Utils.isValidMap(entry.getValue())) {
				Map<String,List<String[]>> dataMap = entry.getValue();
				Map<String,List<String[]>> driveDataMap = pciWiseDriveRecipeDataMap.get(entry.getKey());
				SSVTCellWiseWrapper cellWiseWrapper = new SSVTCellWiseWrapper();
				if (kpiIndexMap.containsKey(ReportConstants.VOLTE_MO_CALL_SETUP_TIME)) {
					Double result = getAvgOfIndexData(kpiIndexMap.get(ReportConstants.VOLTE_MO_CALL_SETUP_TIME),
							dataMap.get(ReportConstants.SCFT_ST_NK_Combine));
					if (result != null) {
						cellWiseWrapper.setVolteMoSetupTime(
								ReportUtil.round(result / 1000, ReportConstants.TWO_DECIMAL_PLACES));
					}
				}
				if (kpiIndexMap.containsKey(ReportConstants.VOLTE_MT_CALL_SETUP_TIME)) {
					Double result = getAvgOfIndexData(kpiIndexMap.get(ReportConstants.VOLTE_MT_CALL_SETUP_TIME),
							dataMap.get(ReportConstants.VOLTE_MT_CALL_CONNECTED_IDLE_MODE_200MS));
					if (result != null) {
						cellWiseWrapper.setVolteMtSetupTime(
								ReportUtil.round(result / 1000, ReportConstants.TWO_DECIMAL_PLACES));
					}
				}
				if (kpiIndexMap.containsKey(ReportConstants.CALL_INITIATE)
						&& kpiIndexMap.containsKey(ReportConstants.MUTECALL)) {
					cellWiseWrapper.setOneWayAudioPercent(getKpiSuccessRate(dataMap.get(ReportConstants.SCFT_ST_NK_Combine),
							kpiIndexMap.get(ReportConstants.CALL_INITIATE), kpiIndexMap.get(ReportConstants.MUTECALL)));
				}
				if (kpiIndexMap.containsKey(ReportConstants.CALL_INITIATE)
						&& kpiIndexMap.containsKey(ReportConstants.CALL_SETUP_SUCCESS)) {
					cellWiseWrapper.setVolteSetupSuccessRate(
							getKpiSuccessRate(dataMap.get(ReportConstants.SCFT_ST_NK_Combine), kpiIndexMap.get(ReportConstants.CALL_INITIATE),
									kpiIndexMap.get(ReportConstants.CALL_SETUP_SUCCESS)));
				}
				if (kpiIndexMap.containsKey(ReportConstants.CALL_INITIATE)
						&& kpiIndexMap.containsKey(ReportConstants.CALL_DROP)) {
					cellWiseWrapper.setVolteDropRate(
							getKpiSuccessRate(dataMap.get(ReportConstants.SCFT_ST_NK_Combine), kpiIndexMap.get(ReportConstants.CALL_INITIATE),
									kpiIndexMap.get(ReportConstants.CALL_DROP)));
				}
				if (kpiIndexMap.containsKey(ReportConstants.CALL_INITIATE)
						&& kpiIndexMap.containsKey(ReportConstants.CALL_FAILURE)) {
					cellWiseWrapper.setVolteAbnormalRelaseRate(
							getKpiSuccessRate(dataMap.get(ReportConstants.SCFT_ST_NK_Combine), kpiIndexMap.get(ReportConstants.CALL_INITIATE),
									kpiIndexMap.get(ReportConstants.CALL_FAILURE)));
				}
				if (kpiIndexMap.containsKey(ReportConstants.VIDEO_OVER_VOLTE_CALL_INITIATE)
						&& kpiIndexMap.containsKey(ReportConstants.VIDEO_OVER_VOLTE_CALL_SUCCESS)) {
					Double successRate = getKpiSuccessRate(dataMap.get(ReportConstants.ViLTE_AND_CONFERENCE_SHORT_CALL_200MS),
							kpiIndexMap.get(ReportConstants.VIDEO_OVER_VOLTE_CALL_INITIATE),
							kpiIndexMap.get(ReportConstants.VIDEO_OVER_VOLTE_CALL_SUCCESS));

					if (successRate != null && successRate > 0.0) {
						cellWiseWrapper.setVideocallRate("YES");
					} else {
						cellWiseWrapper.setVideocallRate("NO");
					}
				}
				if (kpiIndexMap.containsKey(ReportConstants.VIDEO_CALL_CONF_INITIATE)
						&& kpiIndexMap.containsKey(ReportConstants.VIDEO_CALL_CONF_SUCCESS)) {
					Double successRate = getKpiSuccessRate(dataMap.get(ReportConstants.ViLTE_AND_CONFERENCE_SHORT_CALL_200MS),
							kpiIndexMap.get(ReportConstants.VIDEO_CALL_CONF_INITIATE),
							kpiIndexMap.get(ReportConstants.VIDEO_CALL_CONF_SUCCESS));
					if (successRate != null && successRate > 0.0) {
						cellWiseWrapper.setCallConferencingRate("YES");
					} else {
						cellWiseWrapper.setCallConferencingRate("NO");
					}

				}
				if (kpiIndexMap.containsKey(ReportConstants.TOTAL_PACKET_COUNT)
						&& kpiIndexMap.containsKey(ReportConstants.RTP_PACKET_LOSS)) {
					Double successRate = getKpiSuccessRate(dataMap.get(ReportConstants.SCFT_ST_NK_Combine),
							kpiIndexMap.get(ReportConstants.RTP_PACKET_LOSS),
							kpiIndexMap.get(ReportConstants.TOTAL_PACKET_COUNT));
					cellWiseWrapper.setRtpPacketLossRate(successRate);
				}
				
				cellWiseWrapper.setInterhoInterruptionTime(getInterAndIntraHoTime(
						kpiIndexMap.get(ReportConstants.INTER_HANDOVER_SUCCESS), driveDataMap.get(ReportConstants.LONG_CALL_MO_DR_200MS), kpiIndexMap));
				cellWiseWrapper.setIntrahoInterruptionTime(getInterAndIntraHoTime(
						kpiIndexMap.get(ReportConstants.INTRA_HANDOVER_SUCCESS), driveDataMap.get(ReportConstants.LONG_CALL_MO_DR_200MS), kpiIndexMap));
				
				if (kpiIndexMap.containsKey(ReportConstants.SRVCC_ATTEMPT)
						&& kpiIndexMap.containsKey(ReportConstants.SRVCC_SUCCESS)) {
					cellWiseWrapper.setSrvccSuccessRate(
							getKpiSuccessRate(driveDataMap.get(ReportConstants.LONG_CALL_MO_DR_200MS), kpiIndexMap.get(ReportConstants.SRVCC_ATTEMPT),
									kpiIndexMap.get(ReportConstants.SRVCC_SUCCESS)));
				}
//				if (kpiIndexMap.containsKey(ReportConstants.SESSION_ESTABLISHMENT_TIME)) {
//					cellWiseWrapper.setSetupEstTime(getAvgOfIndexData(
//							kpiIndexMap.get(ReportConstants.SESSION_ESTABLISHMENT_TIME), entry.getValue()));
//				}
				if (kpiIndexMap.containsKey(ReportConstants.SRVCC_TIME)) {
					cellWiseWrapper.setSrvccInterruptionTime(
							getAvgOfIndexData(kpiIndexMap.get(ReportConstants.SRVCC_TIME), driveDataMap.get(ReportConstants.LONG_CALL_MO_DR_200MS)));
				}

				list.add(cellWiseWrapper);
			}
		}

		return list;
	}

	private Double getInterAndIntraHoTime(Integer index, List<String[]> list, Map<String, Integer> kpiIndexMap) {
		Double sum = 0.0;
		int count = 0;
		if (Utils.isValidList(list)) {
			for (String[] arr : list) {
				if (ReportUtil.checkIndexValue(index, arr)
						&& ReportUtil.checkIndexValue(kpiIndexMap.get(ReportConstants.HO_INTERRUPTION_TIME), arr)) {
					sum = sum + Double.parseDouble(arr[kpiIndexMap.get(ReportConstants.HO_INTERRUPTION_TIME)]);
					count = count + 1;
				}

			} 
		}
		if (count != 0) {
			return ReportUtil.getAverage(sum, count);
		}
		return null;
	}

	private void setSubWrapperData(SSVTReportSubWrapper subwrapper, GenericWorkorder genericWorkorder,
			List<SiteInformationWrapper> siteInfoList) {
		try {
			String json = genericWorkorder.getGwoMeta().get(ReportConstants.RECIPE_PCI_MAP);
			Map<String, Integer> recipePciMap = new Gson().fromJson(json, new TypeToken<Map<String, Integer>>() {
			}.getType());
			StringBuilder cellId = new StringBuilder();
			if (Utils.isValidList(siteInfoList)) {
				for (SiteInformationWrapper siteWrapper : siteInfoList) {
					if (siteWrapper.getSiteName() != null && !siteWrapper.getSiteName().isEmpty()) {
						subwrapper.setSiteName(siteWrapper.getSiteName());
						subwrapper.setSiteId(siteWrapper.getSiteName());
					}
					if (siteWrapper.getPci() != null && recipePciMap.containsValue(siteWrapper.getPci())
							&& siteWrapper.getCgi() != null) {
						cellId.append(Symbol.COMMA).append(siteWrapper.getCgi().toString());
					}
				}
			}
			if (cellId != null) {
				subwrapper.setCellId(cellId.toString().replaceFirst(Symbol.COMMA_STRING, ""));
				logger.info("cell id  {}", subwrapper.getCellId());
			}
		} catch (Exception e) {
			logger.info("Exception inside method setSubWrapperData {} ", Utils.getStackTrace(e));
		}
	}

	private List<SSVTCellWiseWrapper> prepareCellWiseDataWrapper(Map<String, Integer> kpiIndexMap, Map<Integer, Map<String, List<String[]>>> pciWiseDriveRecipeDataMap,
			Map<Integer, List<String[]>> pciWiseHandoverDataMap, Map<String, Integer> recipePciMap, String band, Map<Integer, Map<String, List<String[]>>> pciWiseStRecipeDataMap) {
		List<SSVTCellWiseWrapper> list = new ArrayList<>();
		for (Entry<Integer, Map<String, List<String[]>>> entry : pciWiseStRecipeDataMap.entrySet()) {
			if (Utils.isValidMap(entry.getValue())) {
				Map<String,List<String[]>> dataMap = entry.getValue();
				SSVTCellWiseWrapper cellWiseWrapper = new SSVTCellWiseWrapper();
				
				setStationaryBasicKpi(kpiIndexMap, dataMap.get(ReportConstants.SCFT_ST_NK_Combine), cellWiseWrapper,entry.getKey(),band);
				setRsrpSinrData(kpiIndexMap, dataMap.get(ReportConstants.SCFT_ST_NK_Combine), cellWiseWrapper,entry.getKey(),band);
				setSessionKpi(kpiIndexMap, dataMap.get(ReportConstants.ALL_RECIPE), cellWiseWrapper,entry.getKey(),band);
				setCAKpiData(kpiIndexMap,dataMap.get(ReportConstants.FTP_DL_CA_200ms), cellWiseWrapper);
				setMcsCqIAndDlUl(kpiIndexMap, dataMap.get(ReportConstants.SCFT_ST_NK_Combine), cellWiseWrapper,entry.getKey(),band);
				setReselectionKpiData(kpiIndexMap, dataMap.get(ReportConstants.SCFT_IDLE_200ms), cellWiseWrapper,entry.getKey(),band);
				setNetworkLatencyKpi(kpiIndexMap, dataMap.get(ReportConstants.SCFT_IDLE_AT_DT),dataMap.get(ReportConstants.SCFT_IDLE_200ms), cellWiseWrapper, entry.getKey());
				setCsbKpis(kpiIndexMap, dataMap.get(ReportConstants.SCFT_CSFB_CALL_MO_200ms),dataMap.get(ReportConstants.SCFT_CSFB_MT_CALL_200ms), cellWiseWrapper);
				setFastReturnRaLaUpdationAndTauSuccess(kpiIndexMap, dataMap.get(ReportConstants.SCFT_CSFB_CALL_MO_200ms),dataMap.get(ReportConstants.SCFT_CSFB_MT_CALL_200ms),dataMap.get(ReportConstants.ALL_RECIPE), cellWiseWrapper,band);
				setTacData(kpiIndexMap, dataMap.get(ReportConstants.SCFT_IDLE_200ms), dataMap.get(ReportConstants.SCFT_CSFB_CALL_MO_200ms),
						 cellWiseWrapper,  entry.getKey(),  band);
				Map<String,List<String[]>> driveDataMap = pciWiseDriveRecipeDataMap.get(entry.getKey());
				List<String[]> handoverList = pciWiseHandoverDataMap.get(entry.getKey());
				if (Utils.isValidMap(driveDataMap)) {
					setDriveDataForStationarySection(kpiIndexMap, cellWiseWrapper, driveDataMap, handoverList,
							entry.getKey(), recipePciMap);
				}
				list.add(cellWiseWrapper);
			}
		}
		return list;
	}

	private void setFastReturnRaLaUpdationAndTauSuccess(Map<String, Integer> kpiIndexMap, List<String[]> molist,
			List<String[]> mtlist, List<String[]> alldatalist, SSVTCellWiseWrapper cellWiseWrapper, String band) {
		List<String[]> dataList = new ArrayList<>();
		if (Utils.isValidList(molist)) {
			dataList.addAll(molist);
		}
		if (Utils.isValidList(mtlist)) {
			dataList.addAll(mtlist);
		}

		if (Utils.isValidList(dataList)) {

			if (kpiIndexMap.containsKey(ReportConstants.TAU_SUCCESS)
					&& kpiIndexMap.containsKey(ReportConstants.RRC_CONNECTION_RELESE412F)) {
				Double totalTau = getSumFromIndex(kpiIndexMap.get(ReportConstants.TAU_SUCCESS), dataList);
				Double dlDcchSum = getSumFromIndex(kpiIndexMap.get(ReportConstants.TAU_SUCCESS), dataList);
				if (dlDcchSum != 0) {
					cellWiseWrapper.setTauSuccessRatio(totalTau / dlDcchSum);
				}
			}

			if (kpiIndexMap.containsKey(ReportConstants.RA_UPDATION)
					|| kpiIndexMap.containsKey(ReportConstants.LA_UPDATION)) {
				Double raUpdation = getMaxValueFromIndex(kpiIndexMap.get(ReportConstants.RA_UPDATION), dataList);
				if (raUpdation != null) {
					cellWiseWrapper.setIsRaLaUpdates(ReportConstants.YES);
				} else {
					Double laUpdation = getMaxValueFromIndex(kpiIndexMap.get(ReportConstants.LA_UPDATION), dataList);
					if (laUpdation != null) {
						cellWiseWrapper.setIsRaLaUpdates(ReportConstants.YES);
					} else {
						cellWiseWrapper.setIsRaLaUpdates(ReportConstants.NO);

					}
				}
			}
			if (kpiIndexMap.containsKey(ReportConstants.FAST_RETURN_TIME)) {
				Double result = getAvgOfIndexData(kpiIndexMap.get(ReportConstants.FAST_RETURN_TIME), dataList);
				if (result != null) {
					cellWiseWrapper
							.setFastReturnTime(ReportUtil.round(result / 1000, ReportConstants.TWO_DECIMAL_PLACES));
				}
			}
		}
		if (kpiIndexMap.containsKey(ReportConstants.RACH_ATTEMPT)) {
			Double value = getAvgOfIndexData(kpiIndexMap.get(ReportConstants.RACH_ATTEMPT), alldatalist);
			if (value != null) {
				cellWiseWrapper.setRachAttemptCount(value.intValue());
			}
		}
		if (kpiIndexMap.containsKey(ReportConstants.FAST_RETURN_PRIORITY)) {
			if (band != null && !band.isEmpty() && band.contains(ReportConstants.FDD)) {
				cellWiseWrapper.setFastReturnTechnolgy(ReportConstants.FDD);
			}
			if (band != null && !band.isEmpty() && band.contains(ReportConstants.TDD)) {
				cellWiseWrapper.setFastReturnTechnolgy(ReportConstants.TDD);
			}
		}
	}

	private void setCsbKpis(Map<String, Integer> kpiIndexMap, List<String[]> moList, List<String[]> mtList,
			SSVTCellWiseWrapper cellWiseWrapper) {
		if (Utils.isValidList(moList)) {
			if (kpiIndexMap.containsKey(ReportConstants.CSFB_MO_CALL_SETUP_TIME)) {
				Double result = getAvgOfIndexData(kpiIndexMap.get(ReportConstants.CSFB_MO_CALL_SETUP_TIME), moList);
				if (result != null) {
					cellWiseWrapper.setCsfbMoCallSetupTime(
							ReportUtil.round(result / 1000, ReportConstants.TWO_DECIMAL_PLACES));
				}
			}
			if (kpiIndexMap.containsKey(ReportConstants.CSFB_CALL_ATTEMPT)
					&& kpiIndexMap.containsKey(ReportConstants.CSFB_CALL_SETUP_SUCCESS)) {
				cellWiseWrapper.setCsfbSuccessRate(
						getKpiSuccessRate(moList, kpiIndexMap.get(ReportConstants.CSFB_CALL_ATTEMPT),
								kpiIndexMap.get(ReportConstants.CSFB_CALL_SETUP_SUCCESS)));
			}
			if (kpiIndexMap.containsKey(ReportConstants.CSFB_CALL_ATTEMPT)
					&& kpiIndexMap.containsKey(ReportConstants.CSFB_CALL_DROP)) {
				cellWiseWrapper
						.setCsfbDropRate(getKpiSuccessRate(moList, kpiIndexMap.get(ReportConstants.CSFB_CALL_ATTEMPT),
								kpiIndexMap.get(ReportConstants.CSFB_CALL_DROP)));
			}
			if (kpiIndexMap.containsKey(ReportConstants.CSFB_CALL_ATTEMPT)
					&& kpiIndexMap.containsKey(ReportConstants.CSFB_CALL_FAIL)) {
				cellWiseWrapper.setCsfbAbnormalRate(
						getKpiSuccessRate(moList, kpiIndexMap.get(ReportConstants.CSFB_CALL_ATTEMPT),
								kpiIndexMap.get(ReportConstants.CSFB_CALL_FAIL)));

			}
			if (kpiIndexMap.containsKey(ReportConstants.CSFB_CAUSE)) {
				cellWiseWrapper.setCsfbCause(
						InbuildingReportUtil.getDistinctKpiValues(moList, kpiIndexMap.get(ReportConstants.CSFB_CAUSE)));

			}
			if (kpiIndexMap.containsKey(ReportConstants.LA_UPDATION)) {
				Double maxlaupdate = getMaxValueFromIndex(kpiIndexMap.get(ReportConstants.LA_UPDATION),moList);
				if(maxlaupdate!=null) {
					cellWiseWrapper.setLaUpdateDuringCall("CSFB WITH LAU");
				}else {
					cellWiseWrapper.setLaUpdateDuringCall("CSFB WITHOUT LAU");
				}
			}
		}
		if (Utils.isValidList(mtList)) {
			if (kpiIndexMap.containsKey(ReportConstants.CSFB_MT_CALL_SETUP_TIME)) {
				Double result = getAvgOfIndexData(kpiIndexMap.get(ReportConstants.CSFB_MT_CALL_SETUP_TIME), mtList);
				if (result != null) {
					cellWiseWrapper.setCsfbMtCallSetupTime(
							ReportUtil.round(result / 1000, ReportConstants.TWO_DECIMAL_PLACES));
				}
			}
		}
	}

	private void setNetworkLatencyKpi(Map<String, Integer> kpiIndexMap, List<String[]> atdtList,
			List<String[]> idle200List, SSVTCellWiseWrapper cellWiseWrapper, Integer pci) {

		if (Utils.isValidList(atdtList)) {
			List<String[]> dataList = atdtList.stream()
					.filter(x -> (x[kpiIndexMap.get(ReportConstants.PCI_PLOT)] != null
							&& !x[kpiIndexMap.get(ReportConstants.PCI_PLOT)].isEmpty())
							&& pci.toString().equalsIgnoreCase(x[kpiIndexMap.get(ReportConstants.PCI_PLOT)]))
					.collect(Collectors.toList());

			if (Utils.isValidList(dataList)) {
				if (kpiIndexMap.containsKey(ReportConstants.ATTACH_LATENCY)) {
					cellWiseWrapper.setAttachedTime(
							getAvgOfIndexData(kpiIndexMap.get(ReportConstants.ATTACH_LATENCY), dataList));
				}
				if (kpiIndexMap.containsKey(ReportConstants.LATENCY)) {
					cellWiseWrapper.setLatency(getAvgOfIndexData(kpiIndexMap.get(ReportConstants.LATENCY), dataList));
				}
				if (kpiIndexMap.containsKey(ReportConstants.ATTACH_REQUEST)
						&& kpiIndexMap.containsKey(ReportConstants.ATTACH_COMPLETE)) {
					Double result = getKpiSuccessRate(dataList, kpiIndexMap.get(ReportConstants.ATTACH_REQUEST),
							kpiIndexMap.get(ReportConstants.ATTACH_COMPLETE));
					if (result != null && result > 100.0) {
						cellWiseWrapper.setAttachedSuccessRate(100.0);
					} else {
						cellWiseWrapper.setAttachedSuccessRate(result);
					}
				}
				if (kpiIndexMap.containsKey(ReportConstants.DETACH_REQUEST)) {
					Double dTRate = getKpiSuccessRate(dataList, kpiIndexMap.get(ReportConstants.DETACH_REQUEST),
							kpiIndexMap.get(ReportConstants.DETACH_REQUEST));
					if (dTRate != null && dTRate > 100.0) {
						cellWiseWrapper.setDetachedSuccessRate(100.0);
					} else {
						cellWiseWrapper.setDetachedSuccessRate(dTRate);
					}
				}
				if (kpiIndexMap.containsKey(ReportConstants.DETACH_TIME)) {
					cellWiseWrapper
							.setDetachedTime(getAvgOfIndexData(kpiIndexMap.get(ReportConstants.DETACH_TIME), dataList));
				}
			}
		}

		if (Utils.isValidList(idle200List)) {
			List<String[]> dataList = idle200List.stream()
					.filter(x -> (x[kpiIndexMap.get(ReportConstants.PCI_PLOT)] != null
							&& !x[kpiIndexMap.get(ReportConstants.PCI_PLOT)].isEmpty())
							&& pci.toString().equalsIgnoreCase(x[kpiIndexMap.get(ReportConstants.PCI_PLOT)]))
					.collect(Collectors.toList());

			if (Utils.isValidList(dataList)) {
				if (kpiIndexMap.containsKey(ReportConstants.UE_CAPABILITY_RAT_CONTAINER)) {
					Double fgi = getMaxValueFromIndex(kpiIndexMap.get(ReportConstants.UE_CAPABILITY_RAT_CONTAINER),
							dataList);
					if (fgi != null && fgi >= 1.0) {
						cellWiseWrapper.setMsgFgiBits(ReportConstants.YES);
					} else {
						cellWiseWrapper.setMsgFgiBits(ReportConstants.NO);
					}
				}
			}
		}
	}

	private void setMcsCqIAndDlUl(Map<String, Integer> kpiIndexMap, List<String[]> dataList,
			SSVTCellWiseWrapper cellWiseWrapper,Integer pci,String band) {

		if (Utils.isValidList(dataList)) {
			List<String[]> list = dataList.stream()
					.filter(x -> (x[kpiIndexMap.get(ReportConstants.PCI_PLOT)] != null
							&& x[kpiIndexMap.get(ReportConstants.TECHNOLOGY)] != null
							&& !x[kpiIndexMap.get(ReportConstants.PCI_PLOT)].isEmpty()
							&& !x[kpiIndexMap.get(ReportConstants.TECHNOLOGY)].isEmpty())
							&& pci.toString().equalsIgnoreCase(x[kpiIndexMap.get(ReportConstants.PCI_PLOT)])
							&& band.contains(x[kpiIndexMap.get(ReportConstants.TECHNOLOGY)]))
					.collect(Collectors.toList());
			if (Utils.isValidList(list)) {

				List<String[]> dlDataList = ReportUtil.filterDataByTestType(list,
						kpiIndexMap.get(ReportConstants.TEST_TYPE), NetworkDataFormats.TEST_TYPE_HTTP_DOWNLOAD,
						NetworkDataFormats.TEST_TYPE_FTP_DOWNLOAD);

				if (Utils.isValidList(dlDataList)) {
					setDownloadDataForStationary(kpiIndexMap, cellWiseWrapper, dlDataList);
				}

				List<String[]> ulDataList = ReportUtil.filterDataByTestType(list,
						kpiIndexMap.get(ReportConstants.TEST_TYPE), NetworkDataFormats.TEST_TYPE_HTTP_UPLOAD,
						NetworkDataFormats.TEST_TYPE_FTP_UPLOAD);

				if (Utils.isValidList(ulDataList)) {
					setUploadDataForStationary(kpiIndexMap, cellWiseWrapper, ulDataList);
				}
			} 
		}
	}

	private void setUploadDataForStationary(Map<String, Integer> kpiIndexMap, SSVTCellWiseWrapper cellWiseWrapper,
			List<String[]> ulDataList) {
		if (kpiIndexMap.containsKey(ReportConstants.PUSCH_THROUGHPUT)) {
			cellWiseWrapper.setTcpUl(
					getMaxValueFromIndex(kpiIndexMap.get(ReportConstants.PUSCH_THROUGHPUT), ulDataList));
		}

		List<Double> data = new ArrayList<>();
		if (kpiIndexMap.containsKey(ReportConstants.CQI_CW0_MAX)) {
			data.addAll(ReportUtil.convetArrayToList(ulDataList, kpiIndexMap.get(ReportConstants.CQI_CW0_MAX)));
		}
		if (kpiIndexMap.containsKey(ReportConstants.CQI_CW1_MAX)) {
			data.addAll(ReportUtil.convetArrayToList(ulDataList, kpiIndexMap.get(ReportConstants.CQI_CW1_MAX)));
		}
		if (Utils.isValidList(data)) {
			Optional<Double> max = data.stream().max(Comparator.naturalOrder());
			if (max.isPresent()) {
				cellWiseWrapper.setUlMaxCqi(max.get().intValue());
			}
		}

		if (kpiIndexMap.containsKey(ReportConstants.UL_MCS_MAX)) {
			Double mcs = getMaxValueFromIndex(kpiIndexMap.get(ReportConstants.UL_MCS_MAX), ulDataList);
			if (mcs != null) {
				cellWiseWrapper.setUlPeakMcs(mcs.intValue());
			}
		}
		if (kpiIndexMap.containsKey(ReportConstants.UL_PRB_MAX)) {
			cellWiseWrapper.setUlPeakRbAllocation(
					getMaxValueFromIndex(kpiIndexMap.get(ReportConstants.UL_PRB_MAX), ulDataList));
		}
	}

	private void setDownloadDataForStationary(Map<String, Integer> kpiIndexMap, SSVTCellWiseWrapper cellWiseWrapper,
			List<String[]> dlDataList) {
		if (kpiIndexMap.containsKey(ReportConstants.PDSCH_THROUGHPUT)) {
			cellWiseWrapper.setTcpDl(
					getMaxValueFromIndex(kpiIndexMap.get(ReportConstants.PDSCH_THROUGHPUT), dlDataList));
		}

		List<Double> data = new ArrayList<>();
		if (kpiIndexMap.containsKey(ReportConstants.CQI_CW0_MAX)) {
			data.addAll(ReportUtil.convetArrayToList(dlDataList, kpiIndexMap.get(ReportConstants.CQI_CW0_MAX)));
		}
		if (kpiIndexMap.containsKey(ReportConstants.CQI_CW1_MAX)) {
			data.addAll(ReportUtil.convetArrayToList(dlDataList, kpiIndexMap.get(ReportConstants.CQI_CW1_MAX)));
		}
		if (Utils.isValidList(data)) {
			Optional<Double> max = data.stream().max(Comparator.naturalOrder());
			if (max.isPresent()) {
				cellWiseWrapper.setDlMaxCqi(max.get().intValue());
			}
		}
		if (kpiIndexMap.containsKey(ReportConstants.DL_MCS_MAX)) {
			Double mcs = getMaxValueFromIndex(kpiIndexMap.get(ReportConstants.DL_MCS_MAX), dlDataList);
			if (mcs != null) {
				cellWiseWrapper.setDlPeakMcs(mcs.intValue());
			}
		}
		if (kpiIndexMap.containsKey(ReportConstants.DL_PRB_MAX)) {
			cellWiseWrapper.setDlPeakRbAllocation(
					getMaxValueFromIndex(kpiIndexMap.get(ReportConstants.DL_PRB_MAX), dlDataList));
		}
	}

	private void setSessionKpi(Map<String, Integer> kpiIndexMap, List<String[]> list,
			SSVTCellWiseWrapper cellWiseWrapper,Integer pci,String band) {
		if (Utils.isValidList(list)) {
		List<String[]> dataList = list.stream()
				.filter(x -> (x[kpiIndexMap.get(ReportConstants.PCI_PLOT)] != null && x[kpiIndexMap.get(ReportConstants.TECHNOLOGY)] != null
				&& !x[kpiIndexMap.get(ReportConstants.PCI_PLOT)].isEmpty() && !x[kpiIndexMap.get(ReportConstants.TECHNOLOGY)].isEmpty())
				&& pci.toString().equalsIgnoreCase(x[kpiIndexMap.get(ReportConstants.PCI_PLOT)]) && band.contains(x[kpiIndexMap.get(ReportConstants.TECHNOLOGY)]))
				.collect(Collectors.toList());

		if (Utils.isValidList(dataList)) {
			if (kpiIndexMap.containsKey(ReportConstants.RRC_SUCCESS)) {
				logger.info("going to set erab success rate ");
				Double result = getKpiSuccessRate(dataList, kpiIndexMap.get(ReportConstants.RRC_SUCCESS),
						kpiIndexMap.get(ReportConstants.RRC_INITIATE));
				if (result != null && result > 100.0) {
					cellWiseWrapper.setSetupSuccessRate(100.0);
				} else {
					cellWiseWrapper.setSetupSuccessRate(result);

				}
			}
			if (kpiIndexMap.containsKey(ReportConstants.RRC_DROP)) {
				logger.info("going to set erab success rate ");
				Double result = getKpiSuccessRate(dataList, kpiIndexMap.get(ReportConstants.RRC_DROP),
						kpiIndexMap.get(ReportConstants.RRC_INITIATE));
				if (result != null && result > 100.0) {
					cellWiseWrapper.setSetupAbnormalRelaseRate(100.0);
				} else {
					cellWiseWrapper.setSetupAbnormalRelaseRate(result);

				}
			}
			if (kpiIndexMap.containsKey(ReportConstants.SESSION_ESTABLISHMENT_TIME)) {
				cellWiseWrapper.setSetupEstTime(
						getAvgOfIndexData(kpiIndexMap.get(ReportConstants.SESSION_ESTABLISHMENT_TIME), dataList));
			}
		}
		}
	}

	private void setStationaryBasicKpi(Map<String, Integer> kpiIndexMap, List<String[]> list,
			SSVTCellWiseWrapper cellWiseWrapper, Integer pci, String band) {
		if (Utils.isValidList(list)) {			
			List<String[]> dataList = list.stream() 
					.filter(x -> (x[kpiIndexMap.get(ReportConstants.PCI_PLOT)] != null && x[kpiIndexMap.get(ReportConstants.TECHNOLOGY)] != null
							&& !x[kpiIndexMap.get(ReportConstants.PCI_PLOT)].isEmpty() && !x[kpiIndexMap.get(ReportConstants.TECHNOLOGY)].isEmpty())
							&& pci.toString().equalsIgnoreCase(x[kpiIndexMap.get(ReportConstants.PCI_PLOT)]) && band.contains(x[kpiIndexMap.get(ReportConstants.TECHNOLOGY)]))
					.collect(Collectors.toList());
						
			if (Utils.isValidList(dataList)) {
				if (kpiIndexMap.containsKey(ReportConstants.TAC)) {
					cellWiseWrapper.setTac(
							InbuildingReportUtil.getDistinctKpiValues(dataList, kpiIndexMap.get(ReportConstants.TAC)));
				}
				if (kpiIndexMap.containsKey(ReportConstants.MCC)) {
					cellWiseWrapper.setMcc(
							InbuildingReportUtil.getDistinctKpiValues(dataList, kpiIndexMap.get(ReportConstants.MCC)));
				}
				if (kpiIndexMap.containsKey(ReportConstants.MNC)) {
					cellWiseWrapper.setMnc(
							InbuildingReportUtil.getDistinctKpiValues(dataList, kpiIndexMap.get(ReportConstants.MNC)));
				}
				if (kpiIndexMap.containsKey(ReportConstants.DL_EARFCN)) {
					cellWiseWrapper.setEurfcn(InbuildingReportUtil.getDistinctKpiValues(dataList,
							kpiIndexMap.get(ReportConstants.DL_EARFCN)));
				}
				if (kpiIndexMap.containsKey(ReportConstants.PA_POWER)) {
					cellWiseWrapper.setRsPowerPa(InbuildingReportUtil.getDistinctKpiValues(dataList,
							kpiIndexMap.get(ReportConstants.PA_POWER)));
				}
				if (kpiIndexMap.containsKey(ReportConstants.PB_POWER)) {
					cellWiseWrapper.setRsPowerPb(InbuildingReportUtil.getDistinctKpiValues(dataList,
							kpiIndexMap.get(ReportConstants.PB_POWER)));
				}
				if (kpiIndexMap.containsKey(ReportConstants.PCI_PLOT)) {
					cellWiseWrapper.setPci(pci);
				}
			}
		}
	}

	private void setRsrpSinrData(Map<String, Integer> kpiIndexMap, List<String[]> list,
			SSVTCellWiseWrapper cellWiseWrapper, Integer pci,String band) {
		if (Utils.isValidList(list)) {
			List<String[]> dataList = list.stream()
					.filter(x -> (x[kpiIndexMap.get(ReportConstants.PCI_PLOT)] != null && x[kpiIndexMap.get(ReportConstants.TECHNOLOGY)] != null
					&& !x[kpiIndexMap.get(ReportConstants.PCI_PLOT)].isEmpty() && !x[kpiIndexMap.get(ReportConstants.TECHNOLOGY)].isEmpty())
					&& pci.toString().equalsIgnoreCase(x[kpiIndexMap.get(ReportConstants.PCI_PLOT)]) && band.contains(x[kpiIndexMap.get(ReportConstants.TECHNOLOGY)]))
					.collect(Collectors.toList());
			
			if (Utils.isValidList(list)) {
				if (kpiIndexMap.containsKey(ReportConstants.RSRP)) {
					cellWiseWrapper.setAvgRsrp(getAvgOfIndexData(kpiIndexMap.get(ReportConstants.RSRP), dataList));
				}
				if (kpiIndexMap.containsKey(ReportConstants.SINR)) {
					cellWiseWrapper.setAvgSinr(getAvgOfIndexData(kpiIndexMap.get(ReportConstants.SINR), dataList));
				}
			}
		}
	}
	
	private void setTacData(Map<String, Integer> kpiIndexMap, List<String[]> idlelist, List<String[]> csfblist,
			SSVTCellWiseWrapper cellWiseWrapper, Integer pci, String band) {
		List<String[]> networkWiseList = new ArrayList<>();
		String lac = null;
		if (Utils.isValidList(idlelist)) {
			networkWiseList.addAll(idlelist);
		}
		if (Utils.isValidList(csfblist)) {
			networkWiseList.addAll(csfblist);
		}
		if (Utils.isValidList(networkWiseList)) {
			List<String[]> twoGList = networkWiseList.stream()
					.filter(x -> (x[kpiIndexMap.get(ReportConstants.NETWORK_TYPE)] != null
							&& !x[kpiIndexMap.get(ReportConstants.NETWORK_TYPE)].isEmpty()
							&& x[kpiIndexMap.get(ReportConstants.NETWORK_TYPE)].equalsIgnoreCase("2g")))
					.collect(Collectors.toList());

			if (Utils.isValidList(twoGList) && kpiIndexMap.containsKey(ReportConstants.LAC)) {
				lac = InbuildingReportUtil.getDistinctKpiValues(twoGList, kpiIndexMap.get(ReportConstants.LAC));
				cellWiseWrapper.setLac(lac);
			}
			if (lac == null) {
				List<String[]> threeGList = networkWiseList.stream()
						.filter(x -> (x[kpiIndexMap.get(ReportConstants.NETWORK_TYPE)] != null
								&& !x[kpiIndexMap.get(ReportConstants.NETWORK_TYPE)].isEmpty()
								&& x[kpiIndexMap.get(ReportConstants.NETWORK_TYPE)].equalsIgnoreCase("3g")))
						.collect(Collectors.toList());
				if (Utils.isValidList(threeGList) && kpiIndexMap.containsKey(ReportConstants.LAC)) {
					lac = InbuildingReportUtil.getDistinctKpiValues(twoGList, kpiIndexMap.get(ReportConstants.LAC));
					cellWiseWrapper.setLac(lac);
				}
			}
		}
	}

	private void setDriveDataForStationarySection(Map<String, Integer> kpiIndexMap, SSVTCellWiseWrapper cellWiseWrapper,
			Map<String, List<String[]>> driveDataMap, List<String[]> handoverDataList, Integer pci,
			Map<String, Integer> recipePciMap) {
		logger.info("inside method setDriveDataForStationarySection with datalist :{} ",driveDataMap.size());
		List<String[]> driveDatalist = new ArrayList<>();
		if(Utils.isValidList(driveDataMap.get(ReportConstants.FTP_DL_NK_200ms))) {
			driveDatalist.addAll(driveDataMap.get(ReportConstants.FTP_DL_NK_200ms));
		}
		if(Utils.isValidList(driveDataMap.get(ReportConstants.FTP_UL_NK_200ms))) {
			driveDatalist.addAll(driveDataMap.get(ReportConstants.FTP_UL_NK_200ms));
		}
		
		if (Utils.isValidList(handoverDataList)) {
			if (checkintrasite(handoverDataList, recipePciMap)) {
				cellWiseWrapper.setIntraSite(ReportConstants.YES);
			} else {
				cellWiseWrapper.setIntraSite(ReportConstants.NO);
			}
		}
		if (Utils.isValidList(handoverDataList)) {
			if (checkintersite(handoverDataList, recipePciMap)) {
				cellWiseWrapper.setInterSite(ReportConstants.YES);
			} else {
				cellWiseWrapper.setInterSite(ReportConstants.NO);
			}
		}

		if (Utils.isValidList(driveDatalist)) {
			setFDDToTdd(kpiIndexMap, cellWiseWrapper, driveDatalist);
			if (kpiIndexMap.containsKey(ReportConstants.LTE_IRAT_HANDOVER)) {
				Double iratHo = getMaxValueFromIndex(kpiIndexMap.get(ReportConstants.LTE_IRAT_HANDOVER), driveDatalist);
				if (iratHo != null && iratHo >= 1.0) {
					cellWiseWrapper.setFddTddTo3g2g(ReportConstants.YES);
				} else {
					cellWiseWrapper.setFddTddTo3g2g(ReportConstants.NO);

				}
			}
			if (kpiIndexMap.containsKey(ReportConstants.EVENT_TYPE)) {
				String event = InbuildingReportUtil.getDistinctKpiValues(driveDatalist,
						kpiIndexMap.get(ReportConstants.EVENT_TYPE));

				if (event != null) {
					cellWiseWrapper.setRrcConfiguration(event);
				}
			}
			if (kpiIndexMap.containsKey(ReportConstants.HO_INTERRUPTION_TIME)
					&& kpiIndexMap.containsKey(ReportConstants.SOURCE_PCI)) {
				cellWiseWrapper.setHoInterruptionTime(getHandoverInterruptionTime(driveDatalist, kpiIndexMap, pci));
			}
			if (kpiIndexMap.containsKey(ReportConstants.CELL_RESELECTION_LTE_TO_3G_TIME)) {
				cellWiseWrapper.setLteTo3gTime(getAvgOfIndexData(
						kpiIndexMap.get(ReportConstants.CELL_RESELECTION_LTE_TO_3G_TIME), driveDatalist));
			}
			if (kpiIndexMap.containsKey(ReportConstants.CELL_RESELECTION_LTE_TO_2G_TIME)) {
				cellWiseWrapper.setLteTo2gTime(getAvgOfIndexData(
						kpiIndexMap.get(ReportConstants.CELL_RESELECTION_LTE_TO_2G_TIME), driveDatalist));
			} 
		}

	}

	private void setReselectionKpiData(Map<String, Integer> kpiIndexMap, List<String[]> entry,
			SSVTCellWiseWrapper cellWiseWrapper,Integer pci,String band) {
		
		if (Utils.isValidList(entry)) {

			if (kpiIndexMap.containsKey(ReportConstants.TDD_TO_FDD_RESELECTION_TIME)) {
				cellWiseWrapper.setTddToFddTime(
						getAvgOfIndexData(kpiIndexMap.get(ReportConstants.TDD_TO_FDD_RESELECTION_TIME), entry));
			}
			if (kpiIndexMap.containsKey(ReportConstants.FDD_TO_TDD_RESELECTION_TIME)) {
				cellWiseWrapper.setFddToTddTime(
						getAvgOfIndexData(kpiIndexMap.get(ReportConstants.FDD_TO_TDD_RESELECTION_TIME), entry));
			}
			if (kpiIndexMap.containsKey(ReportConstants.CELL_RESELECTION_3G_TO_LTE_TIME)) {
				cellWiseWrapper.setThreegToLteTime(
						getAvgOfIndexData(kpiIndexMap.get(ReportConstants.CELL_RESELECTION_3G_TO_LTE_TIME), entry));
			}
			if (kpiIndexMap.containsKey(ReportConstants.CELL_RESELECTION_2G_TO_LTE_TIME)) {
				cellWiseWrapper.setTwogToLteTime(
						getAvgOfIndexData(kpiIndexMap.get(ReportConstants.CELL_RESELECTION_2G_TO_LTE_TIME), entry));
			}
			StringBuilder builder = new StringBuilder();
			if (kpiIndexMap.containsKey(ReportConstants.INTRA_FREQ_CELL_RESELECTION_PRIORITY)) {
				String data = InbuildingReportUtil.getDistinctKpiValues(entry,
						kpiIndexMap.get(ReportConstants.INTRA_FREQ_CELL_RESELECTION_PRIORITY));
				if (data != null) {
					builder.append("LTE INTRA:" + data);
				}
			}
			if (kpiIndexMap.containsKey(ReportConstants.INTER_FREQ_CELL_RESELECTION_PRIORITY)) {
				String data = InbuildingReportUtil.getDistinctKpiValues(entry,
						kpiIndexMap.get(ReportConstants.INTER_FREQ_CELL_RESELECTION_PRIORITY));
				if (data != null) {
					builder.append(" LTE INTER:" + data);
				}
			}
			if (kpiIndexMap.containsKey(ReportConstants.IRAT_WCDMA_CELL_RESELECTION_PRIORITY)) {
				String data = InbuildingReportUtil.getDistinctKpiValues(entry,
						kpiIndexMap.get(ReportConstants.IRAT_WCDMA_CELL_RESELECTION_PRIORITY));
				if (data != null) {
					builder.append(" WCDMA:" + data);
				}
			}
			if (kpiIndexMap.containsKey(ReportConstants.IRAT_GSM_CELL_RESELECTION_PRIORITY)) {
				String data = InbuildingReportUtil.getDistinctKpiValues(entry,
						kpiIndexMap.get(ReportConstants.IRAT_GSM_CELL_RESELECTION_PRIORITY));
				if (data != null) {
					builder.append(" GSM:" + data);
				}
			}
			logger.info("setReSelectionPriorites  {}", builder.toString());
			cellWiseWrapper.setReSelectionPriorites(builder.toString());
		}

	}

	private void setFDDToTdd(Map<String, Integer> kpiIndexMap, SSVTCellWiseWrapper cellWiseWrapper,
			List<String[]> driveDataList) {
		if (kpiIndexMap.containsKey(ReportConstants.HO_TDD_TO_FDD)
				|| kpiIndexMap.containsKey(ReportConstants.HO_FDD_TO_TDD)) {

			Double hoFddToTdd = 0.0;
			Double hoTddToFdd = 0.0;

			if (kpiIndexMap.containsKey(ReportConstants.HO_TDD_TO_FDD)) {
				Double value = getMaxValueFromIndex(kpiIndexMap.get(ReportConstants.HO_TDD_TO_FDD), driveDataList);
				if (value != null) {
					hoTddToFdd = value;
				}
			}
			if (kpiIndexMap.containsKey(ReportConstants.HO_FDD_TO_TDD)) {
				Double value = getMaxValueFromIndex(kpiIndexMap.get(ReportConstants.HO_FDD_TO_TDD), driveDataList);
				if (value != null) {
					hoFddToTdd = value;
				}
			}

			if (hoFddToTdd >= 1.0 || hoTddToFdd >= 1.0) {
				cellWiseWrapper.setFddTdd(ReportConstants.YES);

			} else {
				cellWiseWrapper.setFddTdd(ReportConstants.NO);
			}
		}

		else {
			cellWiseWrapper.setFddTdd(ReportConstants.NO);

		}
	}

	private Double getMaxValueFromIndex(Integer index, List<String[]> value) {
		if (Utils.isValidList(value)) {
			List<Double> data = ReportUtil.convetArrayToList(value, index);
			Optional<Double> max = data.stream().max(Comparator.naturalOrder());
			if (max.isPresent()) {
				return ReportUtil.parseToFixedDecimalPlace(max.get(), ForesightConstants.INDEX_TWO);
			}
		}
		return null;
	}

	private Double getMinValueFromIndex(Integer index, List<String[]> value) {
		List<Double> data = ReportUtil.convetArrayToList(value, index);
		Optional<Double> min = data.stream().min(Comparator.naturalOrder());
		if (min.isPresent()) {
			return ReportUtil.parseToFixedDecimalPlace(min.get(), ForesightConstants.INDEX_TWO);
		}
		return null;
	}

	public List<String[]> getDriveData(Integer workorderId, List<String> fetchKPIList, Map<String, Integer> kpiIndexMap,
			List<WORecipeMapping> recipeMappings, Map<String, Integer> recipePciMap) {

		try {
			List<WORecipeMapping> filterMappings = recipeMappings.stream()
					.filter(mapping -> !(recipePciMap.keySet().contains(mapping.getId().toString())))
					.collect(Collectors.toList());

			List<String> recipeList = transform(
					filterMappings.stream().map(WORecipeMapping::getId).collect(Collectors.toList()), String::valueOf);
			return reportService.getDriveDataRecipeWiseTaggedForReport(Arrays.asList(workorderId), recipeList,
					fetchKPIList, kpiIndexMap);
		} catch (Exception e) {
			logger.error(" getDriveData {}", Utils.getStackTrace(e));
		}
		return null;
	}

	private Map<Integer, List<String[]>> getPciWiseStationaryData(Integer workorderId, List<String> fetchKPIList,
			Map<String, Integer> kpiIndexMap, Map<String, Integer> recipePciMap, List<WORecipeMapping> recipeMappings) {
		Map<Integer, List<String[]>> pciWiseDataMap = new HashMap<>();
		for (WORecipeMapping woRecipeMapping : recipeMappings) {
			if (Status.COMPLETED.equals(woRecipeMapping.getStatus()) && recipePciMap != null
					&& recipePciMap.containsKey(woRecipeMapping.getId().toString())) {
				List<String[]> data = reportService.getDriveDataRecipeWiseTaggedForReport(Arrays.asList(workorderId),
						Arrays.asList(woRecipeMapping.getId().toString()), fetchKPIList, kpiIndexMap);

				ReportUtil.addPciInEmptyFields(data, kpiIndexMap);
				if (pciWiseDataMap.containsKey(recipePciMap.get(woRecipeMapping.getId().toString()))) {
					List<String[]> dataList = pciWiseDataMap.get(recipePciMap.get(woRecipeMapping.getId().toString()));
					dataList.addAll(data);
					pciWiseDataMap.put(recipePciMap.get(woRecipeMapping.getId().toString()), dataList);
				} else {
					pciWiseDataMap.put(recipePciMap.get(woRecipeMapping.getId().toString()), data);
				}
			}
		}
		return pciWiseDataMap;
	}

	private Map<String, Integer> getRecipePCIMap(GenericWorkorder genericWorkorder) {
		try {
			return mapper.readValue(genericWorkorder.getGwoMeta().get(NVWorkorderConstant.RECIPE_PCI_MAP),
					new TypeReference<Map<String, Integer>>() {
					});
		} catch (JsonProcessingException e) {
			logger.error("JsonProcessingException inside the method createReport {}", Utils.getStackTrace(e));
		}
		return null;
	}

	public static <T, U> List<U> transform(List<T> list, Function<T, U> function) {
		return list.stream().map(function).collect(Collectors.toList());
	}

	private Double getAvgOfIndexData(Integer index, List<String[]> dataList) {
		if (Utils.isValidList(dataList)) {
			List<Double> data = ReportUtil.convetArrayToList(dataList, index);
			OptionalDouble avg = data.stream().mapToDouble(x -> x).average();
			if (avg.isPresent()) {
				return ReportUtil.parseToFixedDecimalPlace(avg.getAsDouble(), ForesightConstants.INDEX_TWO);
			}
		}
		return null;
	}

	private double getKpiSuccessRate(List<String[]> driveDataForWorkorders, int attemptindex, Integer sucessindex) {
		double successrate = 0.0;
		if (Utils.isValidList(driveDataForWorkorders)) {
			List<String> attemptSumList = driveDataForWorkorders.stream()
					.filter(x -> (x[attemptindex] != null && !x[attemptindex].isEmpty())).map(x -> x[attemptindex])
					.collect(Collectors.toList());
			int attemptSum = 0;
			if (Utils.isValidList(attemptSumList)) {
				attemptSum = attemptSumList.stream().mapToInt(i -> Integer.parseInt(i)).sum();
			}
			int successSum = 0;
			if (sucessindex != null) {
				List<String> successSumList = driveDataForWorkorders.stream()
						.filter(x -> (x[sucessindex] != null && !x[sucessindex].isEmpty())).map(x -> x[sucessindex])
						.collect(Collectors.toList());

				if (Utils.isValidList(successSumList)) {
					successSum = successSumList.stream().mapToInt(i -> Integer.parseInt(i)).sum();
				}
			}
			logger.info("sum of attempts {}", attemptSum);
			logger.info("sum of success {}", successSum);
			if (attemptSum > 0) {
				return ReportUtil.getPercentage(successSum, attemptSum);
			} 
		}
		return successrate;
	}

	private Double getSumFromIndex(Integer index, List<String[]> dataList) {
		List<Double> data = ReportUtil.convetArrayToList(dataList, index);
		Double value = data.stream().mapToDouble(Double::valueOf).sum();
		return value != null ? value : Double.valueOf(ForesightConstants.INDEX_ZERO);
	}

	private String proceedToCreateReport(SSVTReportWrapper mainWrapper, GenericWorkorder workorderObj,
			Map<String, Object> jsonMap, Map<String, Object> imageMap) {
		logger.info("Going to create SSVT Excel Report");
		String reportAssetRepo = ConfigUtils.getString(ReportConstants.SSVT_EXCEL_REPORT_JASPER_PATH);
		List<SSVTReportWrapper> dataSourceList = new ArrayList<>();
		dataSourceList.add(mainWrapper);
		JRBeanCollectionDataSource rfbeanColDataSource = new JRBeanCollectionDataSource(dataSourceList);
		imageMap.put(ReportConstants.SUBREPORT_DIR, reportAssetRepo);
		imageMap.put(ReportConstants.LOGO_CLIENT_KEY, reportAssetRepo + ReportConstants.LOGO_CLIENT_IMG);
		logger.info("Found Parameter map: {}", new Gson().toJson(imageMap));

		String filePath = ConfigUtils.getString(ReportConstants.NV_REPORTS_PATH) + "SSVT_EXCEL/";
		String fileName = ReportUtil.getFileName(workorderObj.getWorkorderId(),
				(Integer) jsonMap.get(ReportConstants.ANALYTICS_REPOSITORY_ID), filePath);
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		try {
			logger.info("Going to save report on path {}", fileName);
			fileName = fileName.replace(".pdf", ".xls");
			String[] sheetNames = { "Telecom", "SiteInfo", "SCFT", "SCFT Plots", "Session",
					"Attach", "Detach", "CSFB MO Setup Time", "CSFB MT Setup Time", "Fast Return Time", "Volte SCFT",
					"Volte Plots", "Volte MO Call Setup", "Volte MT Call Setup", "Static Test",
					"Site Audit", "CA KPI" };
			ReportUtil.fillDataInXlsxExporter(imageMap, reportAssetRepo + ReportConstants.MAIN_JASPER,
					rfbeanColDataSource, fileName, sheetNames);
			return fileName;
		} catch (JRException e) {
			logger.error("Exception while processing Jasper on path {} trace ==> {}", reportAssetRepo,
					Utils.getStackTrace(e));
		}
		return null;
	}

	private Response saveFileToHdfsAndUpdateStatus(AnalyticsRepository analyticObj, String filePath,
			GenericWorkorder genericWorkorder) {
		String hdfsFilePath = ConfigUtils.getString(ReportConstants.NV_REPORTS_PATH_HDFS) + "SSVT_EXCEL"
				+ ReportConstants.FORWARD_SLASH;
		return reportService.saveFileAndUpdateStatus(analyticObj.getId(), hdfsFilePath, genericWorkorder,
				new File(filePath), ReportUtil.getFileNameFromFilePath(filePath),
				NVWorkorderConstant.REPORT_INSTACE_ID);
	}

	private HashMap<String, String> getImagesForReport(List<KPIWrapper> kpiList, DriveImageWrapper driveImageWrapper,
			Map<String, Integer> kpiIndexMap) throws IOException {
		HashMap<String, BufferedImage> bufferdImageMap = mapImageService.getLegendImages(kpiList,
				driveImageWrapper.getDataKPIs());
		bufferdImageMap.putAll(mapImageService.getDriveImagesForReport(driveImageWrapper, null, kpiIndexMap));
		String saveImagePath = (ConfigUtils.getString(ReportConstants.IMAGE_PATH_FOR_NV_REPORT) + ReportConstants.SSVT
				+ ReportConstants.FORWARD_SLASH
				+ ReportUtil.getFormattedDate(new Date(), ReportConstants.DATE_FORMAT_DD_MM_YY_HH_SS)
				+ ReportConstants.FORWARD_SLASH).toString();
		HashMap<String, String> imagemap = mapImageService.saveDriveImages(bufferdImageMap, saveImagePath, false);
		logger.info("Returning images Map with Data: {}", new Gson().toJson(imagemap));
		return imagemap;
	}

	/*
	 * public static final Map<String, Integer> getKpiIndexMapMobility(Map<String,
	 * Integer> kpiIndexMap) { Map<String, Integer> map = new HashMap();
	 * 
	 * map.put(ReportConstants.RSRP, kpiIndexMap.get(ReportConstants.RSRP));
	 * map.put(ReportConstants.SINR, kpiIndexMap.get(ReportConstants.SINR));
	 * map.put(ReportConstants.RSRQ, kpiIndexMap.get(ReportConstants.RSRQ));
	 * map.put(ReportConstants.PCI_PLOT, kpiIndexMap.get(ReportConstants.PCI_PLOT));
	 * 
	 * map.put(ReportConstants.VOLTE_CODEC,
	 * kpiIndexMap.get(ReportConstants.VOLTE_CODEC)); return map; }
	 */

	public static final Map<String, Integer> getKpiIndexMapForCall(Map<String, Integer> kpiIndexMap) {
		Map<String, Integer> map = new HashMap<>();
		map.put(ReportConstants.RSRP, kpiIndexMap.get(ReportConstants.RSRP));
		map.put(ReportConstants.SINR, kpiIndexMap.get(ReportConstants.SINR));
		map.put(ReportConstants.RSRQ, kpiIndexMap.get(ReportConstants.RSRQ));
		map.put(ReportConstants.VOLTE_CODEC, kpiIndexMap.get(ReportConstants.VOLTE_CODEC));
		map.put(ReportConstants.PCI_PLOT, kpiIndexMap.get(ReportConstants.PCI_PLOT));
		map.put(DriveHeaderConstants.ROUTE, kpiIndexMap.get(DriveHeaderConstants.ROUTE));
		map.put(DriveHeaderConstants.CQI, kpiIndexMap.get(DriveHeaderConstants.CQI));
		map.put(ReportConstants.DL_EARFCN, kpiIndexMap.get(ReportConstants.DL_EARFCN));
		return map;
	}

	public static final Map<String, Integer> getKpiIndexMapForUpload(Map<String, Integer> kpiIndexMap) {
		Map<String, Integer> map = new HashMap<>();
		map.put(ReportConstants.PUSCH_THROUGHPUT, kpiIndexMap.get(ReportConstants.PUSCH_THROUGHPUT));
		return map;
	}

	public static final Map<String, Integer> getKpiIndexMapForDownloadload(Map<String, Integer> kpiIndexMap) {
		Map<String, Integer> map = new HashMap<>();
		map.put(ReportConstants.RSRP, kpiIndexMap.get(ReportConstants.RSRP));
		map.put(ReportConstants.SINR, kpiIndexMap.get(ReportConstants.SINR));
		map.put(ReportConstants.RSRQ, kpiIndexMap.get(ReportConstants.RSRQ));
		map.put(ReportConstants.PCI_PLOT, kpiIndexMap.get(ReportConstants.PCI_PLOT));
		map.put(ReportConstants.PDSCH_THROUGHPUT, kpiIndexMap.get(ReportConstants.PDSCH_THROUGHPUT));
		map.put(ReportConstants.DL_EARFCN, kpiIndexMap.get(ReportConstants.DL_EARFCN));
		return map;
	}

	public GraphWrapper setGraphDataForKpi(KPIWrapper kpiWrapper, List<Double> dataList) {
		logger.info("inside the method setGraphDataForKpi  {} ,totalcount {}", kpiWrapper.getKpiName(),
				kpiWrapper.getTotalCount());
		GraphWrapper graphWrapper = new GraphWrapper();

		if (kpiWrapper.getTotalCount() != null && kpiWrapper.getTotalCount() > ForesightConstants.ZERO) {
			List<GraphDataWrapper> graphDataList = new ArrayList<>();
			Statistics staticstic = new Statistics(dataList);
			try {
				graphWrapper.setMax(staticstic.getMax());
				graphWrapper.setMin(staticstic.getMin());
				graphWrapper.setStDev(staticstic.getStdDev());
				graphWrapper.setVariance(staticstic.getVariance());
				graphWrapper.setMean(staticstic.getMean());
				graphWrapper.setKpiName(kpiWrapper.getKpiName().replace(Symbol.UNDERSCORE_STRING, Symbol.SPACE_STRING));
				graphWrapper.setUnit(ReportUtil.getUnitByKPiName(kpiWrapper.getKpiName()));
				ReportUtil.getThresholdValue(graphWrapper, dataList, kpiWrapper);
				graphWrapper.setCount(kpiWrapper.getTotalCount());
				graphWrapper.setTargetValue(kpiWrapper.getTargetValue());
				ReportUtil.setGraphDataToList(kpiWrapper, graphDataList, kpiWrapper.getRangeSlabs());
				graphDataList.sort(Comparator.comparing(GraphDataWrapper::getFrom).reversed());
				graphWrapper.setGraphDataList(graphDataList);
			} catch (Exception e) {
				logger.error("Exception inside the method setGraphDataForKpi {}", Utils.getStackTrace(e));
			}
		}
		return graphWrapper;
	}

	private void setSiteDataToWrapper(List<SiteInformationWrapper> siteInfoList, SSVTReportSubWrapper subwrapper,
			GenericWorkorder genericWorkorder) {
		List<SiteInformationWrapper> list = new ArrayList<>();
		Map<String, Integer> recipePciMap = getRecipePCIMap(genericWorkorder);
		logger.info("inside method  setsitedata");
		if (Utils.isValidList(siteInfoList)) {
			for (SiteInformationWrapper siteData : siteInfoList) {
				logger.info("inside loop  setsitedata");
				if (siteData.getPci() != null && recipePciMap!=null && recipePciMap.containsValue(siteData.getPci())) {
					list.add(siteData);
				}
				subwrapper.setSiteName(siteData.getSiteName());
			}
		}
		logger.info(" Size of SiteDataList {}", list.size());
		subwrapper.setSiteInfoList(list);
	}

	private List<GraphWrapper> getPlotsDataForLongCall(List<String[]> driveData, Map<String, Integer> kpiIndexMap,
			List<SiteInformationWrapper> siteInfoList) {
		List<GraphWrapper> graphList = new ArrayList<>();
		List<LegendWrapper> legendList = legendRangeDao.findAllLegendRangesAppliedTo(ReportConstants.SSVT_REPORT);
		List<String[]> longCallList = ReportUtil.filterDataByTestType(driveData,
				kpiIndexMap.get(ReportConstants.TEST_TYPE), NetworkDataFormats.LONG_CALL_TEST);
		if (Utils.isValidList(longCallList)) {
			setGraphDataAndPlotImages(kpiIndexMap, graphList, legendList, longCallList,
					getKpiIndexMapForCall(kpiIndexMap), NetworkDataFormats.LONG_CALL_TEST, siteInfoList);
		}

		return graphList;
	}

	private List<GraphWrapper> getPlotsDataForDlUl(List<String[]> driveData, Map<String, Integer> kpiIndexMap,
			List<SiteInformationWrapper> siteInfoList) {
		List<GraphWrapper> graphList = new ArrayList<>();
		List<LegendWrapper> legendList = legendRangeDao.findAllLegendRangesAppliedTo(ReportConstants.SSVT_REPORT);
		List<String[]> dlTestDataList = ReportUtil.filterDataByTestType(driveData,
				kpiIndexMap.get(ReportConstants.TEST_TYPE), NetworkDataFormats.TEST_TYPE_FTP_DOWNLOAD,
				NetworkDataFormats.TEST_TYPE_HTTP_DOWNLOAD);
		List<String[]> ulTestDataList = ReportUtil.filterDataByTestType(driveData,
				kpiIndexMap.get(ReportConstants.TEST_TYPE), NetworkDataFormats.TEST_TYPE_FTP_UPLOAD,
				NetworkDataFormats.TEST_TYPE_HTTP_UPLOAD);
		if (Utils.isValidList(dlTestDataList)) {
			setGraphDataAndPlotImages(kpiIndexMap, graphList, legendList, dlTestDataList,
					getKpiIndexMapForDownloadload(kpiIndexMap), null, siteInfoList);
		}
		if (Utils.isValidList(ulTestDataList)) {
			setGraphDataAndPlotImages(kpiIndexMap, graphList, legendList, ulTestDataList,
					getKpiIndexMapForUpload(kpiIndexMap), null, siteInfoList);
		}
		return graphList;
	}

	private Map<Integer, List<String[]>> getPciWiseHandoverData(Integer workorderId, Map<String, Integer> recipePciMap,
			List<WORecipeMapping> recipeMappings) {
		Map<Integer,List<String[]>> pciWiseDataMap = new HashMap<Integer,List<String[]>>();
				
		List<String[]> dataList = new ArrayList<String[]>();
		List<WORecipeMapping> mobilityMappings = getFilteredMappingsForDrive(recipeMappings,recipePciMap);

		logger.info("filterMappings for handover {}", mobilityMappings.size());
		for (WORecipeMapping woRecipeMapping : mobilityMappings) {
			if (Status.COMPLETED.equals(woRecipeMapping.getStatus()) && 
					(woRecipeMapping.getRecipe().getName().equalsIgnoreCase(ReportConstants.FTP_UL_NK_200ms) || 
					 woRecipeMapping.getRecipe().getName().equalsIgnoreCase(ReportConstants.FTP_DL_NK_200ms))) {
				dataList.addAll(reportService.getHandoverDataFromHbase(workorderId,
						Arrays.asList(woRecipeMapping.getId().toString())));
			}
		}
		
		if (Utils.isValidList(dataList)) {
			for (String[] data : dataList) {
				if (ReportUtil.checkIndexValue(ReportConstants.INDEX_HO_DATA_SOURCE_PCI, data)) {
					Integer sourcePci = Integer.parseInt(data[ReportConstants.INDEX_HO_DATA_SOURCE_PCI]);
					if (pciWiseDataMap.containsKey(sourcePci)) {
						List<String[]> existingDataList =pciWiseDataMap.get(sourcePci);
						existingDataList.add(data);
						pciWiseDataMap.put(sourcePci, existingDataList);
					} else {
						List<String[]> newList = new ArrayList<>();
						newList.add(data);
						pciWiseDataMap.put(sourcePci,newList);
					}	
				}
			}
		}
		
	    logger.info("pciWiseHandoverDataMap : {}", dataList.size());
		return pciWiseDataMap;
	}
	private Boolean checkintrasite(List<String[]> handoverDataList, Map<String, Integer> recipePciMap) {
		for (String[] data : handoverDataList) {
			if (data != null && data.length > ReportConstants.INDEX_HO_DATA_STATUS) {
				if (!StringUtils.isBlank(data[ReportConstants.INDEX_HO_DATA_STATUS]) && ReportConstants.TEST_STATUS_PASS
						.equalsIgnoreCase(data[ReportConstants.INDEX_HO_DATA_STATUS])) {
					// logger.info(" status is pass {} :", Pci);
					if (ReportUtil.checkIndexValue(ReportConstants.INDEX_HO_DATA_SOURCE_PCI, data)
							&& ReportUtil.checkIndexValue(ReportConstants.INDEX_HO_DATA_TARGET_PCI, data)) {
						if (recipePciMap.containsValue(Integer.parseInt(data[ReportConstants.INDEX_HO_DATA_SOURCE_PCI]))
								&& recipePciMap.containsValue(
										Integer.parseInt(data[ReportConstants.INDEX_HO_DATA_TARGET_PCI]))) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	private Boolean checkintersite(List<String[]> handoverDataList, Map<String, Integer> recipePciMap) {
		for (String[] data : handoverDataList) {
			if (data != null && data.length > ReportConstants.INDEX_HO_DATA_STATUS) {
				if (!StringUtils.isBlank(data[ReportConstants.INDEX_HO_DATA_STATUS]) && ReportConstants.TEST_STATUS_PASS
						.equalsIgnoreCase(data[ReportConstants.INDEX_HO_DATA_STATUS])) {
					// logger.info(" status is pass {} :", Pci);
					if (ReportUtil.checkIndexValue(ReportConstants.INDEX_HO_DATA_SOURCE_PCI, data)
							&& ReportUtil.checkIndexValue(ReportConstants.INDEX_HO_DATA_TARGET_PCI, data)) {
						if ((!recipePciMap
								.containsValue(Integer.parseInt(data[ReportConstants.INDEX_HO_DATA_SOURCE_PCI])))
								||( !recipePciMap.containsValue(
										Integer.parseInt(data[ReportConstants.INDEX_HO_DATA_TARGET_PCI])))) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	private Double getHandoverInterruptionTime(List<String[]> dataList, Map<String, Integer> kpiIndeMap, Integer pci) {
		try {
			logger.info("inside method getHandoverInterruptionTime");
			List<String> hoList = dataList.stream()
					.filter(x -> (x[kpiIndeMap.get(ReportConstants.SOURCE_PCI)] != null
							&& !x[kpiIndeMap.get(ReportConstants.SOURCE_PCI)].isEmpty()
							&& Integer.parseInt(x[kpiIndeMap.get(ReportConstants.SOURCE_PCI)]) == pci))
					.map(x -> x[kpiIndeMap.get(ReportConstants.HO_INTERRUPTION_TIME)]).collect(Collectors.toList());

			logger.info(" hoList {} :", hoList);
			if (Utils.isValidList(hoList)) {
				OptionalDouble avgHIT = hoList.stream().filter(x -> (x != null && !x.isEmpty()))
						.mapToDouble(x -> Double.parseDouble(x)).average();
				if (avgHIT != null) {
					return avgHIT.getAsDouble();
				}
			}
		} catch (Exception e) {
			logger.error("Exception in getHandoverInterruptionTime {} ", Utils.getStackTrace(e));
		}
		return null;
	}

	private void setCAKpiData(Map<String, Integer> kpiIndexMap, List<String[]> entry,
			SSVTCellWiseWrapper cellWiseWrapper) {
		try {
			if (Utils.isValidList(entry)) {
				List<String[]> dlDataList = ReportUtil.filterDataByTestType(entry,
						kpiIndexMap.get(ReportConstants.TEST_TYPE), NetworkDataFormats.TEST_TYPE_HTTP_DOWNLOAD,
						NetworkDataFormats.TEST_TYPE_FTP_DOWNLOAD);
				if (Utils.isValidList(dlDataList)) {
					if (kpiIndexMap.containsKey(ReportConstants.PDSCH_THROUGHPUT)) {
						cellWiseWrapper.setMaxPdschThroughput(
								getMaxValueFromIndex(kpiIndexMap.get(ReportConstants.PDSCH_THROUGHPUT), dlDataList));
					}
					setAvgCaValues(kpiIndexMap, cellWiseWrapper, dlDataList);
					setMaxCaValues(kpiIndexMap, cellWiseWrapper, dlDataList);
				} 
			}
		} catch (Exception e) {
			logger.error("Exception inside method setCAKpi {}", Utils.getStackTrace(e));
		}
	}

	private void setAvgCaValues(Map<String, Integer> kpiIndexMap, SSVTCellWiseWrapper cellWiseWrapper,
			List<String[]> dlDataList) {
		Double scellthp = null;
		if (kpiIndexMap.containsKey(ReportConstants.PCELL_PDSCH_THROUPUT)) {
			cellWiseWrapper
					.setAvgDl(getAvgOfIndexData(kpiIndexMap.get(ReportConstants.PCELL_PDSCH_THROUPUT), dlDataList));
		}
		if (kpiIndexMap.containsKey(ReportConstants.SCELL1_PDSCH_THROUPUT)) {
			scellthp = getAvgOfIndexData(kpiIndexMap.get(ReportConstants.SCELL1_PDSCH_THROUPUT), dlDataList);
		}
		if (kpiIndexMap.containsKey(ReportConstants.SCELL2_PDSCH_THROUPUT)) {
			if (scellthp != null
					&& getAvgOfIndexData(kpiIndexMap.get(ReportConstants.SCELL2_PDSCH_THROUPUT), dlDataList) != null) {
				scellthp += getAvgOfIndexData(kpiIndexMap.get(ReportConstants.SCELL2_PDSCH_THROUPUT), dlDataList);
			}
		}
		cellWiseWrapper.setsCellPdschThroughput(scellthp);
	}

	private void setMaxCaValues(Map<String, Integer> kpiIndexMap, SSVTCellWiseWrapper cellWiseWrapper,
			List<String[]> dlDataList) {
		Double scellthp = null;
		Double maxPdsch = 0.0;
		if (Utils.isValidList(dlDataList) && kpiIndexMap.containsKey(ReportConstants.PDSCH_THROUGHPUT)) {
			for (String[] data : dlDataList) {

				if (ReportUtil.checkIndexValue(kpiIndexMap.get(ReportConstants.PDSCH_THROUGHPUT), data)
						&& ReportUtil.parseToFixedDecimalPlace(
								Double.parseDouble(data[kpiIndexMap.get(ReportConstants.PDSCH_THROUGHPUT)]),
								ReportConstants.TWO_DECIMAL_PLACES) > maxPdsch) {

					maxPdsch = ReportUtil.parseToFixedDecimalPlace(
							Double.parseDouble(data[kpiIndexMap.get(ReportConstants.PDSCH_THROUGHPUT)]),
							ReportConstants.TWO_DECIMAL_PLACES);

					Double pcell = NVLayer3Utils.getDoubleFromCsv(data,
							kpiIndexMap.get(ReportConstants.PCELL_PDSCH_THROUPUT));
					if (pcell != null) {
						cellWiseWrapper.setMaxPCellPdschThroughput(
								ReportUtil.parseToFixedDecimalPlace(pcell, ReportConstants.TWO_DECIMAL_PLACES));
					}

					scellthp = NVLayer3Utils.getDoubleFromCsv(data,
							kpiIndexMap.get(ReportConstants.SCELL1_PDSCH_THROUPUT));

					if (scellthp != null && NVLayer3Utils.getDoubleFromCsv(data,
							kpiIndexMap.get(ReportConstants.SCELL2_PDSCH_THROUPUT)) != null) {
						scellthp += NVLayer3Utils.getDoubleFromCsv(data,
								kpiIndexMap.get(ReportConstants.SCELL2_PDSCH_THROUPUT));
					}
					if (scellthp != null) {
						cellWiseWrapper.setMaxSCellPdschThroughput(
								ReportUtil.parseToFixedDecimalPlace(scellthp, ReportConstants.TWO_DECIMAL_PLACES));
					}

				}
			}
		}
	}
	
	private String getGraphkpiImage(Map<String, Integer> kpiIndexMap, List<String[]> data, Integer sector,
			String testype) {
		try {
			List<GraphDataWrapper> kpiDataList = getKpiDataListForImage(kpiIndexMap, data);
			if (Utils.isValidList(kpiDataList)) {
				BufferedImage img = getKpiImage(kpiDataList);
				ImageIO.write(img, "PNG",
						new File(ConfigUtils.getString(ReportConstants.FINAL_IMAGE_PATH) + testype
								+ ReportConstants.UNDERSCORE + ReportConstants.SECTOR + ReportConstants.UNDERSCORE
								+ sector + ReportConstants.DOT_PNG));
				return ConfigUtils.getString(ReportConstants.FINAL_IMAGE_PATH) + testype + ReportConstants.UNDERSCORE
						+ ReportConstants.SECTOR + ReportConstants.UNDERSCORE + sector + ReportConstants.DOT_PNG;
			}
		} catch (Exception e) {
			logger.error("Exception getGraphkpiImage {}", Utils.getStackTrace(e));
		}
		return null;
	}

	private List<GraphDataWrapper> getKpiDataListForImage(Map<String, Integer> kpiIndexMap, List<String[]> data) {
		List<GraphDataWrapper> list = new ArrayList<>();
		if (kpiIndexMap.containsKey(ReportConstants.PDSCH_THROUGHPUT)) {
			Double value = getAvgOfIndexData(kpiIndexMap.get(ReportConstants.PDSCH_THROUGHPUT), data);
			if (value != null) {
				GraphDataWrapper wrapper = new GraphDataWrapper();
				wrapper.setKpiName("Avg" + ReportConstants.SPACE + ReportConstants.PDSCH_THROUGHPUT);
				wrapper.setValue(value);
				list.add(wrapper);
			}
		}
		if (kpiIndexMap.containsKey(ReportConstants.PDSCH_THROUGHPUT)) {
			Double value = getMaxValueFromIndex(kpiIndexMap.get(ReportConstants.PDSCH_THROUGHPUT), data);
			if (value != null) {
				GraphDataWrapper wrapper = new GraphDataWrapper();
				wrapper.setKpiName("Max" + ReportConstants.SPACE + ReportConstants.PDSCH_THROUGHPUT);
				wrapper.setValue(value);
				list.add(wrapper);
			}
		}
		if (kpiIndexMap.containsKey(ReportConstants.DL_THROUGHPUT)) {
			Double value = getAvgOfIndexData(kpiIndexMap.get(ReportConstants.DL_THROUGHPUT), data);
			if (value != null) {
				GraphDataWrapper wrapper = new GraphDataWrapper();
				wrapper.setKpiName("Avg" + ReportConstants.SPACE + ReportConstants.DL_THROUGHPUT + "   ");
				wrapper.setValue(value);
				list.add(wrapper);
			}
		}
		if (kpiIndexMap.containsKey(ReportConstants.DL_THROUGHPUT)) {
			Double value = getMaxValueFromIndex(kpiIndexMap.get(ReportConstants.DL_THROUGHPUT), data);
			if (value != null) {
				GraphDataWrapper wrapper = new GraphDataWrapper();
				wrapper.setKpiName("Max" + ReportConstants.SPACE + ReportConstants.DL_THROUGHPUT + "   ");
				wrapper.setValue(value);
				list.add(wrapper);
			}
		}
		if (kpiIndexMap.containsKey(ReportConstants.PUSCH_THROUGHPUT)) {
			Double value = getAvgOfIndexData(kpiIndexMap.get(ReportConstants.PUSCH_THROUGHPUT), data);
			if (value != null) {
				GraphDataWrapper wrapper = new GraphDataWrapper();
				wrapper.setKpiName("Avg" + ReportConstants.SPACE + ReportConstants.PUSCH_THROUGHPUT);
				wrapper.setValue(value);
				list.add(wrapper);
			}
		}
		if (kpiIndexMap.containsKey(ReportConstants.PUSCH_THROUGHPUT)) {
			Double value = getMaxValueFromIndex(kpiIndexMap.get(ReportConstants.PUSCH_THROUGHPUT), data);
			if (value != null) {
				GraphDataWrapper wrapper = new GraphDataWrapper();
				wrapper.setKpiName("Max" + ReportConstants.SPACE + ReportConstants.PUSCH_THROUGHPUT);
				wrapper.setValue(value);
				list.add(wrapper);
			}
		}
		if (kpiIndexMap.containsKey(ReportConstants.DL_PRB)) {
			Double value = getAvgOfIndexData(kpiIndexMap.get(ReportConstants.DL_PRB), data);
			if (value != null) {
				GraphDataWrapper wrapper = new GraphDataWrapper();
				wrapper.setKpiName("Avg" + ReportConstants.SPACE + ReportConstants.DL_PRB);
				wrapper.setValue(value);
				list.add(wrapper);
			}
		}

		if (kpiIndexMap.containsKey(ReportConstants.DL_PRB_MAX)) {
			Double value = getMaxValueFromIndex(kpiIndexMap.get(ReportConstants.DL_PRB_MAX), data);
			if (value != null) {
				GraphDataWrapper wrapper = new GraphDataWrapper();
				wrapper.setKpiName("Max" + ReportConstants.SPACE + ReportConstants.DL_PRB_MAX);
				wrapper.setValue(value);
				list.add(wrapper);
			}
		}
		if (kpiIndexMap.containsKey(ReportConstants.MCS)) {
			Double value = getAvgOfIndexData(kpiIndexMap.get(ReportConstants.MCS), data);
			if (value != null) {
				GraphDataWrapper wrapper = new GraphDataWrapper();
				wrapper.setKpiName("Avg" + ReportConstants.SPACE + "LTE" + ReportConstants.SPACE + ReportConstants.MCS);
				wrapper.setValue(value);
				list.add(wrapper);
			}
		}

		if (kpiIndexMap.containsKey(ReportConstants.DL_MCS_MAX)) {
			Double value = getMaxValueFromIndex(kpiIndexMap.get(ReportConstants.DL_MCS_MAX), data);
			if (value != null) {
				GraphDataWrapper wrapper = new GraphDataWrapper();
				wrapper.setKpiName(
						"Max" + ReportConstants.SPACE + "LTE" + ReportConstants.SPACE + ReportConstants.DL_MCS_MAX);
				wrapper.setValue(value);
				list.add(wrapper);
			}
		}

		if (kpiIndexMap.containsKey(ReportConstants.UL_MCS_MAX)) {
			Double value = getMaxValueFromIndex(kpiIndexMap.get(ReportConstants.UL_MCS_MAX), data);
			if (value != null) {
				GraphDataWrapper wrapper = new GraphDataWrapper();
				wrapper.setKpiName(
						"Max" + ReportConstants.SPACE + "LTE" + ReportConstants.SPACE + ReportConstants.UL_MCS_MAX);
				wrapper.setValue(value);
				list.add(wrapper);
			}
		}

		return list;
	}

	public  BufferedImage getKpiImage(List<GraphDataWrapper> list) {
		// logger.info("Inside method getLegendImage for kpi {} ");
		BufferedImage image = new BufferedImage(235, 180, BufferedImage.TYPE_INT_RGB);
		try {
			Graphics2D graphics = (Graphics2D) image.getGraphics();
			int j = ReportConstants.TEN;
			int i = ReportConstants.TEN;
			writeDataKpiWise(list, graphics, j, i);
		} catch (Exception e) {
			 logger.error("Exception inside method getLegendImage {} ",
			 e.getMessage());
		}
		return image;
	}

	
	private  void writeDataKpiWise(List<GraphDataWrapper> list, Graphics2D graphics, int j, int i) {
		try {
			for (GraphDataWrapper graphDataWrapper : list) {
				if (graphDataWrapper != null) {
					j = writeDataInImage(graphDataWrapper, i, j, graphics);
				}
			}
		} catch (Exception e) {
			 logger.error("Error in method writeDataRangeSlabwise {} ",
					 e.getMessage());
		}
	}

	private  int writeDataInImage(GraphDataWrapper graphDataWrapper, int i, int j, Graphics2D graphics) {
		String range = null;
		j = j + ReportConstants.FIFTEEN;
		try {

			graphics.setColor(Color.WHITE);
			graphics.setBackground(Color.BLACK);
			graphics.setFont(new Font(ReportConstants.CALIBRI, Font.PLAIN, 10));

			range = graphDataWrapper.getKpiName() + ReportConstants.DOUBLE_SPACE + ReportConstants.DOUBLE_SPACE
					+ ReportConstants.DOUBLE_SPACE + ReportConstants.DOUBLE_SPACE + graphDataWrapper.getValue();

				graphics.drawString(range, i, j);
			
		} catch (Exception e) {
			logger.error("Exception inside the method writeDataInImage {}",Utils.getStackTrace(e));
		}
		return j;
	}

	private String setKpiStringForImage(Map<String, Integer> kpiIndexMap, List<String[]> datalist, Integer sector,
			String testype) {
		BufferedImage image = new BufferedImage(235, 50, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = (Graphics2D) image.getGraphics();
		int j = ReportConstants.TEN;
		int i = ReportConstants.TEN;

		//j = j + ReportConstants.FIFTEEN;
		graphics.setColor(Color.WHITE);
		graphics.setBackground(Color.BLACK);
		graphics.setFont(new Font(CALIBRI, Font.BOLD, 10));
		String range = null;
		if (kpiIndexMap.containsKey(ReportConstants.RSRP) && kpiIndexMap.containsKey(ReportConstants.SINR)
				&& kpiIndexMap.containsKey(ReportConstants.RSRQ)) {

			Double rsrp = getAvgOfIndexData(kpiIndexMap.get(ReportConstants.RSRP), datalist);
			Double sinr = getAvgOfIndexData(kpiIndexMap.get(ReportConstants.SINR), datalist);
			Double rsrq = getAvgOfIndexData(kpiIndexMap.get(ReportConstants.RSRQ), datalist);

			range = ReportConstants.RSRP + Symbol.COLON + rsrp + ReportConstants.SPACE + ReportConstants.SINR
					+ Symbol.COLON + sinr + ReportConstants.SPACE + ReportConstants.RSRQ + Symbol.COLON + rsrq
					+ ReportConstants.SPACE;

		}
		if (range != null && !range.isEmpty()) {
			graphics.drawString(range, i, j);
		}

		range = null;
		j = j + ReportConstants.FIFTEEN;
		graphics.setColor(Color.WHITE);
		graphics.setBackground(Color.BLACK);
		graphics.setFont(new Font(CALIBRI, Font.PLAIN, 8));
		if (kpiIndexMap.containsKey(ReportConstants.PCI_PLOT) && kpiIndexMap.containsKey(ReportConstants.EARFCN)
				&& kpiIndexMap.containsKey(ReportConstants.BAND)) {

			String earfcn = InbuildingReportUtil.getDistinctKpiValues(datalist,
					kpiIndexMap.get(ReportConstants.EARFCN));
			String band = InbuildingReportUtil.getDistinctKpiValues(datalist, kpiIndexMap.get(ReportConstants.BAND));

			range = ReportConstants.EARFCN + Symbol.COLON + earfcn + ReportConstants.SPACE + ReportConstants.BAND
					+ Symbol.COLON + band + ReportConstants.SPACE + ReportConstants.PCI + Symbol.COLON + sector
					+ ReportConstants.SPACE;

		}
		if (range != null && !range.isEmpty()) {
			graphics.drawString(range, i, j);
		}
		
		range = null;
		j = j + ReportConstants.FIFTEEN;
		graphics.setColor(Color.WHITE);
		graphics.setBackground(Color.BLACK);
		graphics.setFont(new Font(CALIBRI, Font.PLAIN, 8));
		if (kpiIndexMap.containsKey(ReportConstants.LATITUDE) && kpiIndexMap.containsKey(ReportConstants.LONGITUDE)) {
			Map<String, String> latlonmap = getFirstLocation(datalist, kpiIndexMap.get(ReportConstants.LATITUDE),
					kpiIndexMap.get(ReportConstants.LONGITUDE));

			range = "Location" + Symbol.COLON + latlonmap.get(ReportConstants.LATITUDE) + ","
					+ latlonmap.get(ReportConstants.LONGITUDE);
		}
		if (range != null && !range.isEmpty()) {
			graphics.drawString(range, i, j);
		}

		try {
			ImageIO.write(image, "PNG", new File(ConfigUtils.getString(ReportConstants.FINAL_IMAGE_PATH) + testype
					+ ReportConstants.UNDERSCORE + ReportConstants.SECTOR + ReportConstants.UNDERSCORE
					+ ReportConstants.STATIC_TEST + ReportConstants.UNDERSCORE + sector + ReportConstants.DOT_PNG));
		} catch (IOException e) {
			logger.error("Exception inside method setKpiStringForImage {} ", Utils.getStackTrace(e));
			
		}
		return ConfigUtils.getString(ReportConstants.FINAL_IMAGE_PATH) + testype + ReportConstants.UNDERSCORE
				+ ReportConstants.SECTOR + ReportConstants.UNDERSCORE + ReportConstants.STATIC_TEST
				+ ReportConstants.UNDERSCORE + sector + ReportConstants.DOT_PNG;
	}

	private List<MessageDetailWrapper> carryForwardPciInWrapper(List<MessageDetailWrapper> list) {
		String pci = null;
		String firstValue = null;
		List<MessageDetailWrapper> backwordList = new ArrayList<>();
		List<MessageDetailWrapper> newList = new ArrayList<>();
		for (MessageDetailWrapper wrapper : list) {
			if (wrapper != null) {
				if (wrapper.getPCI() != null && !wrapper.getPCI().isEmpty()) {
					pci = wrapper.getPCI();
					if (firstValue == null) {
						firstValue = pci;
					}
					newList.add(wrapper);

				} else if (pci != null) {
					wrapper.setPCI(pci);
					newList.add(wrapper);

				} else {
					backwordList.add(wrapper);

				}

			}

		}
		if (Utils.isValidList(backwordList)) {
			for (MessageDetailWrapper backwordWrapper : backwordList) {
				backwordWrapper.setPCI(firstValue);
			}
			backwordList.addAll(newList);
			logger.info("inside method addKPIInEmptyFields going to return backwordList {}", backwordList.size());
			return backwordList;
		}
		return newList;
	}

	private Map<Integer, List<MessageDetailWrapper>> getPciWiseMessageDataList(List<MessageDetailWrapper> list, Map<String, Integer> recipePciMap) {
		Map<Integer, List<MessageDetailWrapper>> map = new HashMap<>();
		logger.info("inside method getPciWiseMessageDataList {} ", list.stream().map(MessageDetailWrapper::getPCI).collect(Collectors.toSet()));
		for (MessageDetailWrapper wrapper : list) {
			if (wrapper != null && wrapper.getPCI() != null && !wrapper.getPCI().isEmpty()) {
				if (wrapper.getPCI().contains("!#!#")) {
					String[] pci = wrapper.getPCI().split("!#!#");
					wrapper.setPCI(pci[0]);
				}
				if (recipePciMap.containsValue(Integer.parseInt(wrapper.getPCI()))) {
					if (map.containsKey(Integer.parseInt(wrapper.getPCI()))) {
						List<MessageDetailWrapper> dataList = map.get(Integer.parseInt(wrapper.getPCI()));
						dataList.add(wrapper);
						map.put(Integer.parseInt(wrapper.getPCI()), dataList);
					} else {
						List<MessageDetailWrapper> newList = new ArrayList<>();
						newList.add(wrapper);
						map.put(Integer.parseInt(wrapper.getPCI()), newList);
					}}
			}
		}
		return map;
	}

	private void setAttachMessageDataToWrapper(SSVTReportSubWrapper subwrapper,
			Map<Integer, List<MessageDetailWrapper>> pciWiseMessageData) {
		List<SectorSwapWrapper> list = new ArrayList<>();
		logger.info("inside method getattachmessagdata to wrapper ===={}", pciWiseMessageData.size());
		if (Utils.isValidMap(pciWiseMessageData)) {
			for (Entry<Integer, List<MessageDetailWrapper>> map : pciWiseMessageData.entrySet()) {
				List<MessageKpiWrapper> messageKpiWrapperList = new ArrayList<>();
				SectorSwapWrapper sectorWrapper = new SectorSwapWrapper();
				if (map.getValue() != null) {
					
					List<MessageDetailWrapper> filteredList =	map.getValue().stream()
							.filter(x -> (x.getPCI() != null && !x.getPCI().isEmpty())
									&& map.getKey().toString().equalsIgnoreCase(x.getPCI()))
				
							.collect(Collectors.toList());
					setATData(filteredList, messageKpiWrapperList);
					sectorWrapper.setMessageList(messageKpiWrapperList);
					sectorWrapper.setSectorId(map.getKey().toString());

					list.add(sectorWrapper);
				}
			}
		}
		subwrapper.setAtSectorWiseMessagList(list);
	}

	
	private void setATData(List<MessageDetailWrapper> list, List<MessageKpiWrapper> messageKpiWrapperList) {
		MessageKpiWrapper messageKpiWrapper = null;
		boolean onlyAtRequest = false;
		for (MessageDetailWrapper messageDetailWrapper : list) {
			logger.info(" onlyAtRequest  =={}", onlyAtRequest);
			if (onlyAtRequest && messageKpiWrapper != null) {
				if (messageDetailWrapper.getAttachComplete() != null
						&& !messageDetailWrapper.getAttachComplete().isEmpty()) {
					if (messageDetailWrapper.getTimeStamp() != null) {
						messageKpiWrapper.setEndTime(
								NVLayer3Utils.getDateFromTimestamp(messageDetailWrapper.getTimeStamp().toString()));
						Duration duration = DateUtils.getDiffrence(new Date(messageKpiWrapper.getStarttimestamp()),
								new Date(messageDetailWrapper.getTimeStamp()));
						messageKpiWrapper.setDuration(duration.toMilliseconds());

					}
					if (messageDetailWrapper.getDecodedMsg()!=null && ! StringUtils.isBlank(messageDetailWrapper.getDecodedMsg())) {
						String msg = messageDetailWrapper.getDecodedMsg().replaceAll("\\$", "\\\n");
						messageKpiWrapper.setEndMessageDetail(msg);
					}
					messageKpiWrapper.setIsSuccess("1");
					messageKpiWrapper.setResult(ReportConstants.SUCCESS);
					messageKpiWrapperList.add(messageKpiWrapper);

					messageKpiWrapper = new MessageKpiWrapper();
					onlyAtRequest = false;
					continue;
				} else if (messageDetailWrapper.getAttachRequest() != null
						&& !messageDetailWrapper.getAttachRequest().isEmpty()) {

					messageKpiWrapper.setIsSuccess("0");
					messageKpiWrapper.setResult(ReportConstants.FAILURE);
					messageKpiWrapperList.add(messageKpiWrapper);
					messageKpiWrapper = new MessageKpiWrapper();
					if (messageDetailWrapper.getDecodedMsg()!=null && !StringUtils.isBlank(messageDetailWrapper.getDecodedMsg())) {
						String msg = messageDetailWrapper.getDecodedMsg().replaceAll("\\$", "\\\n");
						messageKpiWrapper.setStartMessageDetail(msg);
					}
					if (messageDetailWrapper.getTimeStamp() != null) {
						messageKpiWrapper.setStartTime(
								NVLayer3Utils.getDateFromTimestamp(messageDetailWrapper.getTimeStamp().toString()));
						messageKpiWrapper.setStarttimestamp(messageDetailWrapper.getTimeStamp());
					}
					onlyAtRequest = true;

					continue;
				}
			} else if (messageDetailWrapper.getAttachRequest() != null
					&& !messageDetailWrapper.getAttachRequest().isEmpty()
					&& messageDetailWrapper.getAttachComplete() != null
					&& !messageDetailWrapper.getAttachComplete().isEmpty()) {
				messageKpiWrapper = new MessageKpiWrapper();
				messageKpiWrapper.setIsSuccess("1");
				messageKpiWrapper.setResult(ReportConstants.SUCCESS);
				if (messageDetailWrapper.getDecodedMsg()!=null&& !StringUtils.isBlank(messageDetailWrapper.getDecodedMsg())) {
					String msg = messageDetailWrapper.getDecodedMsg().replaceAll("\\$", "\\\n");
					messageKpiWrapper.setStartMessageDetail(msg);
					messageKpiWrapper.setEndMessageDetail(msg);
				}
				if (messageDetailWrapper.getTimeStamp() != null) {
					messageKpiWrapper.setStartTime(
							NVLayer3Utils.getDateFromTimestamp(messageDetailWrapper.getTimeStamp().toString()));
					messageKpiWrapper.setEndTime(
							NVLayer3Utils.getDateFromTimestamp(messageDetailWrapper.getTimeStamp().toString()));
					messageKpiWrapper.setStarttimestamp(messageDetailWrapper.getTimeStamp());

					Duration duration = DateUtils.getDiffrence(new Date(messageDetailWrapper.getTimeStamp()),
							new Date(messageDetailWrapper.getTimeStamp()));
					messageKpiWrapper.setDuration(duration.toMilliseconds());
				}

				messageKpiWrapperList.add(messageKpiWrapper);
				messageKpiWrapper = new MessageKpiWrapper();
				continue;
			} else if (messageDetailWrapper.getAttachRequest() != null
					&& !messageDetailWrapper.getAttachRequest().isEmpty()
					&& (messageDetailWrapper.getAttachComplete() == null
							|| messageDetailWrapper.getAttachComplete().isEmpty())) {
				messageKpiWrapper = new MessageKpiWrapper();

				if (messageDetailWrapper.getDecodedMsg()!=null && !StringUtils.isBlank(messageDetailWrapper.getDecodedMsg())) {
					String msg = messageDetailWrapper.getDecodedMsg().replaceAll("\\$", "\\n");
					messageKpiWrapper.setStartMessageDetail(msg);
				}
				if (messageDetailWrapper.getTimeStamp() != null) {
					messageKpiWrapper.setStartTime(
							NVLayer3Utils.getDateFromTimestamp(messageDetailWrapper.getTimeStamp().toString()));
					messageKpiWrapper.setStarttimestamp(messageDetailWrapper.getTimeStamp());
				}
				onlyAtRequest = true;
			}
		}
	}

	private void setDetachMessageDataToWrapper(SSVTReportSubWrapper subwrapper,
			Map<Integer, List<MessageDetailWrapper>> pciWiseMessageData) {
		List<SectorSwapWrapper> list = new ArrayList<>();
		logger.info("inside method setDetachMessageDataToWrapper to wrapper {}", pciWiseMessageData.size());
		if (Utils.isValidMap(pciWiseMessageData)) {
			for (Entry<Integer, List<MessageDetailWrapper>> map : pciWiseMessageData.entrySet()) {
				List<MessageKpiWrapper> messageKpiWrapperList = new ArrayList<>();
				SectorSwapWrapper sectorWrapper = new SectorSwapWrapper();
				if (map.getValue() != null) {
					
					List<MessageDetailWrapper> filteredList =	map.getValue().stream()
							.filter(x -> (x.getPCI() != null && !x.getPCI().isEmpty())
									&& map.getKey().toString().equalsIgnoreCase(x.getPCI()))
							.collect(Collectors.toList());
					setDTData(filteredList, messageKpiWrapperList);
					sectorWrapper.setMessageList(messageKpiWrapperList);
					sectorWrapper.setSectorId(map.getKey().toString());
					list.add(sectorWrapper);
				}
			}
		}
		subwrapper.setDtSectorWiseMessagList(list);
	}

	private void setDTData(List<MessageDetailWrapper> list, List<MessageKpiWrapper> messageKpiWrapperList) {

		boolean onlyAtRequest = false;
		MessageKpiWrapper messageKpiWrapper = null;
		for (MessageDetailWrapper messageDetailWrapper : list) {
			if (onlyAtRequest) {
				if (messageDetailWrapper.getDetachAccept() != null
						&& !messageDetailWrapper.getDetachAccept().isEmpty()||(messageDetailWrapper.getRrcConnectionRelease() != null
								&& !messageDetailWrapper.getRrcConnectionRelease().isEmpty())) {
					if (messageDetailWrapper.getTimeStamp() != null) {
						messageKpiWrapper.setEndTime(
								NVLayer3Utils.getDateFromTimestamp(messageDetailWrapper.getTimeStamp().toString()));
						Duration duration = DateUtils.getDiffrence(new Date(messageKpiWrapper.getStarttimestamp()),
								new Date(messageDetailWrapper.getTimeStamp()));
						messageKpiWrapper.setDuration(duration.toMilliseconds());
					}
					if (messageDetailWrapper.getDecodedMsg()!=null && ! StringUtils.isBlank(messageDetailWrapper.getDecodedMsg())) {
						String msg = messageDetailWrapper.getDecodedMsg();
						messageKpiWrapper.setEndMessageDetail(msg);
					}

					messageKpiWrapper.setIsSuccess("1");
					messageKpiWrapper.setResult(ReportConstants.SUCCESS);
					messageKpiWrapperList.add(messageKpiWrapper);

					messageKpiWrapper = new MessageKpiWrapper();
					onlyAtRequest = false;
					continue;
				} else if (messageDetailWrapper.getDetachRequest() != null
						&& !messageDetailWrapper.getDetachRequest().isEmpty()) {
					messageKpiWrapper.setIsSuccess("0");
					messageKpiWrapper.setResult(ReportConstants.FAILURE);
					messageKpiWrapperList.add(messageKpiWrapper);
					messageKpiWrapper = new MessageKpiWrapper();
					if (messageDetailWrapper.getDecodedMsg()!=null && !StringUtils.isBlank(messageDetailWrapper.getDecodedMsg())) {
						String msg = messageDetailWrapper.getDecodedMsg().replaceAll("\\$", "\\\n");
						messageKpiWrapper.setStartMessageDetail(msg);
					}
					if (messageDetailWrapper.getTimeStamp() != null) {
						messageKpiWrapper.setStartTime(
								NVLayer3Utils.getDateFromTimestamp(messageDetailWrapper.getTimeStamp().toString()));
						messageKpiWrapper.setStarttimestamp(messageDetailWrapper.getTimeStamp());
					}
					onlyAtRequest = true;

					continue;
				}
			} else if (messageKpiWrapper != null && messageDetailWrapper.getDetachRequest() != null
					&& !messageDetailWrapper.getDetachRequest().isEmpty()
					&& (messageDetailWrapper.getDetachAccept() != null
							&& !messageDetailWrapper.getDetachAccept().isEmpty()
							|| messageDetailWrapper.getRrcConnectionRelease() != null
									&& !messageDetailWrapper.getRrcConnectionRelease().isEmpty())) {
				messageKpiWrapper.setIsSuccess("1");
				messageKpiWrapper.setResult(ReportConstants.SUCCESS);
				if (messageDetailWrapper.getDecodedMsg() !=null && !StringUtils.isBlank(messageDetailWrapper.getDecodedMsg())) {
					String msg = messageDetailWrapper.getDecodedMsg().replaceAll("\\$", "\\\n");
					messageKpiWrapper.setStartMessageDetail(msg);
					messageKpiWrapper.setEndMessageDetail(msg);
				}
				if (messageDetailWrapper.getTimeStamp() != null) {
					messageKpiWrapper.setStartTime(
							NVLayer3Utils.getDateFromTimestamp(messageDetailWrapper.getTimeStamp().toString()));
					messageKpiWrapper.setEndTime(
							NVLayer3Utils.getDateFromTimestamp(messageDetailWrapper.getTimeStamp().toString()));
					messageKpiWrapper.setStarttimestamp(messageDetailWrapper.getTimeStamp());

					Duration duration = DateUtils.getDiffrence(new Date(messageDetailWrapper.getTimeStamp()),
							new Date(messageDetailWrapper.getTimeStamp()));
					messageKpiWrapper.setDuration(duration.toMilliseconds());
				}
				messageKpiWrapperList.add(messageKpiWrapper);
				messageKpiWrapper = new MessageKpiWrapper();
				continue;

			} else if (messageDetailWrapper.getDetachRequest() != null
					&& !messageDetailWrapper.getDetachRequest().isEmpty()
					&& (messageDetailWrapper.getDetachAccept() == null
							|| messageDetailWrapper.getDetachAccept().isEmpty())) {
				messageKpiWrapper = new MessageKpiWrapper();

				if (messageDetailWrapper.getDecodedMsg() !=null && !StringUtils.isBlank(messageDetailWrapper.getDecodedMsg())) {
					String msg = messageDetailWrapper.getDecodedMsg().replaceAll("\\$", "\\\n");
					messageKpiWrapper.setStartMessageDetail(msg);
				}
				if (messageDetailWrapper.getTimeStamp() != null) {
					messageKpiWrapper.setStartTime(
							NVLayer3Utils.getDateFromTimestamp(messageDetailWrapper.getTimeStamp().toString()));
					messageKpiWrapper.setStarttimestamp(messageDetailWrapper.getTimeStamp());

				}
				onlyAtRequest = true;
			}
		}

	}

	private void setCSMOMessageDataToWrapper(SSVTReportSubWrapper subwrapper,
			Map<Integer, List<MessageDetailWrapper>> pciWiseMessageData) {
		List<SectorSwapWrapper> list = new ArrayList<>();
		logger.info("inside method setCSMOMessageDataToWrapper to wrapper ==={}", pciWiseMessageData.size());
		if (Utils.isValidMap(pciWiseMessageData)) {
			for (Entry<Integer, List<MessageDetailWrapper>> map : pciWiseMessageData.entrySet()) {
				List<MessageKpiWrapper> messageKpiWrapperList = new ArrayList<>();
				SectorSwapWrapper sectorWrapper = new SectorSwapWrapper();
				if (map.getValue() != null) {
					
					List<MessageDetailWrapper> filteredList =	map.getValue().stream()
							.filter(x -> (x.getPCI() != null && !x.getPCI().isEmpty())
									&& map.getKey().toString().equalsIgnoreCase(x.getPCI()))
							.collect(Collectors.toList());

					setCSMOData(filteredList, messageKpiWrapperList);
					sectorWrapper.setMessageList(messageKpiWrapperList);
					sectorWrapper.setSectorId(map.getKey().toString());

					list.add(sectorWrapper);
				}
			}
		}
		subwrapper.setCsfbMOSectorWiseMessageList(list);
	}

	private void setCSMOData(List<MessageDetailWrapper> list, List<MessageKpiWrapper> messageKpiWrapperList) {
		MessageKpiWrapper messageKpiWrapper = null;
		boolean onlyAtRequest = false;
		for (MessageDetailWrapper messageDetailWrapper : list) {
			if (onlyAtRequest && messageKpiWrapper != null) {
				if (messageDetailWrapper.getCsfbMoCallSetupSuccess() != null
						&& !messageDetailWrapper.getCsfbMoCallSetupSuccess().isEmpty()) {
					if (messageDetailWrapper.getTimeStamp() != null) {
						messageKpiWrapper.setEndTime(
								NVLayer3Utils.getDateFromTimestamp(messageDetailWrapper.getTimeStamp().toString()));
					}
					if (messageDetailWrapper.getDecodedMsg()!=null && !StringUtils.isBlank(messageDetailWrapper.getDecodedMsg())) {
						String msg = messageDetailWrapper.getDecodedMsg().replaceAll("\\$", "\\n");
						messageKpiWrapper.setEndMessageDetail(msg);
					}
					if (messageDetailWrapper.getCsfbMoCallSetupTime() != null) {
						Double time = Double.parseDouble(messageDetailWrapper.getCsfbMoCallSetupTime());
						messageKpiWrapper.setDuration(time);
					}
					messageKpiWrapper.setIsSuccess("1");
					messageKpiWrapper.setResult(ReportConstants.SUCCESS);
					messageKpiWrapperList.add(messageKpiWrapper);

					messageKpiWrapper = new MessageKpiWrapper();
					onlyAtRequest = false;
					continue;
				} else if (messageDetailWrapper.getCsfbMoCallAttempt() != null
						&& !messageDetailWrapper.getCsfbMoCallAttempt().isEmpty()) {
					messageKpiWrapper.setIsSuccess("0");
					messageKpiWrapper.setResult(ReportConstants.FAILURE);
					messageKpiWrapperList.add(messageKpiWrapper);
					messageKpiWrapper = new MessageKpiWrapper();
					if (messageDetailWrapper.getDecodedMsg()!=null && !StringUtils.isBlank(messageDetailWrapper.getDecodedMsg())) {
						String msg = messageDetailWrapper.getDecodedMsg().replaceAll("\\$", "\\\n");
						messageKpiWrapper.setStartMessageDetail(msg);
					}
					if (messageDetailWrapper.getTimeStamp() != null) {
						messageKpiWrapper.setStartTime(
								NVLayer3Utils.getDateFromTimestamp(messageDetailWrapper.getTimeStamp().toString()));
						messageKpiWrapper.setStarttimestamp(messageDetailWrapper.getTimeStamp());
					}
					onlyAtRequest = true;

					continue;
				}
			} else if (messageKpiWrapper != null && messageDetailWrapper.getCsfbMoCallAttempt() != null
					&& !messageDetailWrapper.getCsfbMoCallAttempt().isEmpty()
					&& messageDetailWrapper.getCsfbMoCallSetupSuccess() != null
					&& !messageDetailWrapper.getCsfbMoCallSetupSuccess().isEmpty()) {
				messageKpiWrapper.setIsSuccess("1");
				messageKpiWrapper.setResult(ReportConstants.SUCCESS);
				if (messageDetailWrapper.getDecodedMsg()!=null && !StringUtils.isBlank(messageDetailWrapper.getDecodedMsg())) {
					String msg = messageDetailWrapper.getDecodedMsg().replaceAll("\\$", "\\\n");
					messageKpiWrapper.setStartMessageDetail(msg);
					messageKpiWrapper.setEndMessageDetail(msg);
				}
				if (messageDetailWrapper.getTimeStamp() != null) {
					messageKpiWrapper.setStartTime(
							NVLayer3Utils.getDateFromTimestamp(messageDetailWrapper.getTimeStamp().toString()));
					messageKpiWrapper.setEndTime(
							NVLayer3Utils.getDateFromTimestamp(messageDetailWrapper.getTimeStamp().toString()));
				}
				if (messageDetailWrapper.getCsfbMoCallSetupTime() != null) {
					Double time = Double.parseDouble(messageDetailWrapper.getCsfbMoCallSetupTime());
					messageKpiWrapper.setDuration(time);
				}
				messageKpiWrapperList.add(messageKpiWrapper);
				messageKpiWrapper = new MessageKpiWrapper();
				continue;
			} else if (messageDetailWrapper.getCsfbMoCallAttempt() != null
					&& !messageDetailWrapper.getCsfbMoCallAttempt().isEmpty()
					&& (messageDetailWrapper.getCsfbMoCallSetupSuccess() == null
							|| messageDetailWrapper.getCsfbMoCallSetupSuccess().isEmpty())) {
				messageKpiWrapper = new MessageKpiWrapper();

				if (messageDetailWrapper.getDecodedMsg()!=null && !StringUtils.isBlank(messageDetailWrapper.getDecodedMsg())) {
					String msg = messageDetailWrapper.getDecodedMsg().replaceAll("\\$", "\\\n");
					messageKpiWrapper.setStartMessageDetail(msg);
				}
				if (messageDetailWrapper.getTimeStamp() != null) {
					messageKpiWrapper.setStartTime(
							NVLayer3Utils.getDateFromTimestamp(messageDetailWrapper.getTimeStamp().toString()));
				}
				onlyAtRequest = true;
			}
		}

	}

	private void setCSMTMessageDataToWrapper(SSVTReportSubWrapper subwrapper,
			Map<Integer, List<MessageDetailWrapper>> pciWiseMessageData) {
		List<SectorSwapWrapper> list = new ArrayList<>();
		logger.info("inside method getattachmessagdata to wrapper {}", pciWiseMessageData.size());
		if (Utils.isValidMap(pciWiseMessageData)) {
			for (Entry<Integer, List<MessageDetailWrapper>> map : pciWiseMessageData.entrySet()) {
				List<MessageKpiWrapper> messageKpiWrapperList = new ArrayList<>();
				SectorSwapWrapper sectorWrapper = new SectorSwapWrapper();
				if (map.getValue() != null) {
					
					List<MessageDetailWrapper> filteredList =	map.getValue().stream()
							.filter(x -> (x.getPCI() != null && !x.getPCI().isEmpty())
									&& map.getKey().toString().equalsIgnoreCase(x.getPCI()))
							.collect(Collectors.toList());
					
					setCSMTData(filteredList, messageKpiWrapperList);
					sectorWrapper.setMessageList(messageKpiWrapperList);
					sectorWrapper.setSectorId(map.getKey().toString());
				}
				list.add(sectorWrapper);
			}

		}
		subwrapper.setCsfbMTSectorWiseMessageList(list);
	}

	private void setCSMTData(List<MessageDetailWrapper> list, List<MessageKpiWrapper> messageKpiWrapperList) {
		MessageKpiWrapper messageKpiWrapper = null;
		boolean onlyAtRequest = false;
		for (MessageDetailWrapper messageDetailWrapper : list) {
			if (onlyAtRequest && messageKpiWrapper != null) {
				if (messageDetailWrapper.getCsfbMtCallSetupSuccess() != null
						&& !messageDetailWrapper.getCsfbMtCallSetupSuccess().isEmpty()) {
					if (messageDetailWrapper.getTimeStamp() != null) {
						messageKpiWrapper.setEndTime(
								NVLayer3Utils.getDateFromTimestamp(messageDetailWrapper.getTimeStamp().toString()));
					}
					if (messageDetailWrapper.getDecodedMsg()!=null && !StringUtils.isBlank(messageDetailWrapper.getDecodedMsg())) {
						String msg = messageDetailWrapper.getDecodedMsg().replaceAll("\\$", "\\\n");
						messageKpiWrapper.setEndMessageDetail(msg);
					}
					if (messageDetailWrapper.getCsfbMtCallSetupTime() != null
							&& !messageDetailWrapper.getCsfbMtCallSetupTime().isEmpty()) {
						Double time = Double.parseDouble(messageDetailWrapper.getCsfbMtCallSetupTime());
						messageKpiWrapper.setDuration(time);
					}
					messageKpiWrapper.setIsSuccess("1");
					messageKpiWrapper.setResult(ReportConstants.SUCCESS);
					messageKpiWrapperList.add(messageKpiWrapper);

					messageKpiWrapper = new MessageKpiWrapper();
					onlyAtRequest = false;
					continue;
				} else if (messageDetailWrapper.getCsfbMtCallAttempt() != null
						&& !messageDetailWrapper.getCsfbMtCallAttempt().isEmpty()) {
					messageKpiWrapper.setIsSuccess("0");
					messageKpiWrapper.setResult(ReportConstants.FAILURE);
					messageKpiWrapperList.add(messageKpiWrapper);
					messageKpiWrapper = new MessageKpiWrapper();
					if (messageDetailWrapper.getDecodedMsg()!=null && !StringUtils.isBlank(messageDetailWrapper.getDecodedMsg())) {
						String msg = messageDetailWrapper.getDecodedMsg().replaceAll("\\$", "\\\n");
						messageKpiWrapper.setStartMessageDetail(msg);
					}
					if (messageDetailWrapper.getTimeStamp() != null) {
						messageKpiWrapper.setStartTime(
								NVLayer3Utils.getDateFromTimestamp(messageDetailWrapper.getTimeStamp().toString()));
						messageKpiWrapper.setStarttimestamp(messageDetailWrapper.getTimeStamp());
					}
					onlyAtRequest = true;

					continue;
				}
			} else if (messageKpiWrapper != null && messageDetailWrapper.getCsfbMtCallAttempt() != null
					&& !messageDetailWrapper.getCsfbMtCallAttempt().isEmpty()
					&& messageDetailWrapper.getCsfbMoCallSetupSuccess() != null
					&& !messageDetailWrapper.getCsfbMoCallSetupSuccess().isEmpty()) {
				messageKpiWrapper.setIsSuccess("1");
				messageKpiWrapper.setResult(SUCCESS);
				if (messageDetailWrapper.getDecodedMsg()!=null && !StringUtils.isBlank(messageDetailWrapper.getDecodedMsg())) {
					String msg = messageDetailWrapper.getDecodedMsg().replaceAll("\\$", "\\\n");
					messageKpiWrapper.setStartMessageDetail(msg);
					messageKpiWrapper.setEndMessageDetail(msg);
				}
				if (messageDetailWrapper.getTimeStamp() != null) {
					messageKpiWrapper.setStartTime(
							NVLayer3Utils.getDateFromTimestamp(messageDetailWrapper.getTimeStamp().toString()));
					messageKpiWrapper.setEndTime(
							NVLayer3Utils.getDateFromTimestamp(messageDetailWrapper.getTimeStamp().toString()));
				}
				if (messageDetailWrapper.getCsfbMtCallSetupTime() != null) {
					Double time = Double.parseDouble(messageDetailWrapper.getCsfbMtCallSetupTime());
					messageKpiWrapper.setDuration(time);
				}
				messageKpiWrapperList.add(messageKpiWrapper);
				messageKpiWrapper = new MessageKpiWrapper();
				continue;
			} else if (messageDetailWrapper.getCsfbMtCallAttempt() != null
					&& !messageDetailWrapper.getCsfbMtCallAttempt().isEmpty()
					&& (messageDetailWrapper.getCsfbMtCallSetupSuccess() == null
							|| messageDetailWrapper.getCsfbMtCallSetupSuccess().isEmpty())) {
				messageKpiWrapper = new MessageKpiWrapper();

				if (messageDetailWrapper.getDecodedMsg()!=null && !StringUtils.isBlank(messageDetailWrapper.getDecodedMsg())) {
					String msg = messageDetailWrapper.getDecodedMsg().replaceAll("\\$", "\\\n");
					messageKpiWrapper.setStartMessageDetail(msg);
				}
				if (messageDetailWrapper.getTimeStamp() != null) {
					messageKpiWrapper.setStartTime(
							NVLayer3Utils.getDateFromTimestamp(messageDetailWrapper.getTimeStamp().toString()));
				}
				onlyAtRequest = true;
			}
		}
	}

	private void setVoLTEMTMessageDataToWrapper(SSVTReportSubWrapper subwrapper,
			Map<Integer, List<MessageDetailWrapper>> pciWiseMessageData) {
		List<SectorSwapWrapper> list = new ArrayList<SectorSwapWrapper>();
		logger.info("inside method getattachmessagdata to wrapper {}", pciWiseMessageData.size());
		if (Utils.isValidMap(pciWiseMessageData)) {
			for (Entry<Integer, List<MessageDetailWrapper>> map : pciWiseMessageData.entrySet()) {
				List<MessageKpiWrapper> messageKpiWrapperList = new ArrayList<MessageKpiWrapper>();
				SectorSwapWrapper sectorWrapper = new SectorSwapWrapper();
				if (map.getValue() != null) {
					
					List<MessageDetailWrapper> filteredList =	map.getValue().stream()
							.filter(x -> (x.getPCI() != null && !x.getPCI().isEmpty())
									&& map.getKey().toString().equalsIgnoreCase(x.getPCI()))
							.collect(Collectors.toList());
					
					setVoLTEMtData(filteredList, messageKpiWrapperList);
					sectorWrapper.setMessageList(messageKpiWrapperList);
					sectorWrapper.setSectorId(map.getKey().toString());
				}
				list.add(sectorWrapper);
			}
		}

		subwrapper.setVolteMtSectorWiseMessagList(list);
	}

	private void setVoLTEMtData(List<MessageDetailWrapper> list, List<MessageKpiWrapper> messageKpiWrapperList) {
		MessageKpiWrapper messageKpiWrapper = null;
		boolean onlyAtRequest = false;
		for (MessageDetailWrapper messageDetailWrapper : list) {
			if (onlyAtRequest && messageKpiWrapper != null) {
				if (messageDetailWrapper.getVolteMTCallSetup() != null
						&& !messageDetailWrapper.getVolteMTCallSetup().isEmpty()) {
					if (messageDetailWrapper.getTimeStamp() != null) {
						messageKpiWrapper.setEndTime(
								NVLayer3Utils.getDateFromTimestamp(messageDetailWrapper.getTimeStamp().toString()));
					}
					if (messageDetailWrapper.getDecodedMsg()!=null && !StringUtils.isBlank(messageDetailWrapper.getSipMessage())) {
						String msg = messageDetailWrapper.getSipMessage().replaceAll("\\$", "\\\n");
						messageKpiWrapper.setEndMessageDetail(msg);
					}
					if (messageDetailWrapper.getMtCallConnectionSetupTime() != null) {
						Double time = Double.parseDouble(messageDetailWrapper.getMtCallConnectionSetupTime());
						messageKpiWrapper.setDuration(time);
					}
					messageKpiWrapper.setIsSuccess("1");
					messageKpiWrapper.setResult("Suceess");

					messageKpiWrapperList.add(messageKpiWrapper);

					messageKpiWrapper = new MessageKpiWrapper();
					onlyAtRequest = false;
					continue;
				} else if (messageDetailWrapper.getVolteMTCallAttempts() != null
						&& !messageDetailWrapper.getVolteMTCallAttempts().isEmpty()) {
					messageKpiWrapper.setIsSuccess("0");
					messageKpiWrapper.setResult("Failure");
					messageKpiWrapperList.add(messageKpiWrapper);
					messageKpiWrapper = new MessageKpiWrapper();
					if (messageDetailWrapper.getDecodedMsg()!=null && !StringUtils.isBlank(messageDetailWrapper.getSipMessage())) {
						String msg = messageDetailWrapper.getSipMessage().replaceAll("\\$", "\\\n");
						messageKpiWrapper.setStartMessageDetail(msg);
					}
					if (messageDetailWrapper.getTimeStamp() != null) {
						messageKpiWrapper.setStartTime(
								NVLayer3Utils.getDateFromTimestamp(messageDetailWrapper.getTimeStamp().toString()));
						messageKpiWrapper.setStarttimestamp(messageDetailWrapper.getTimeStamp());
					}
					onlyAtRequest = true;

					continue;
				}
			} else if (messageKpiWrapper != null && messageDetailWrapper.getVolteMTCallAttempts() != null
					&& !messageDetailWrapper.getVolteMTCallAttempts().isEmpty()
					&& messageDetailWrapper.getVolteMTCallSetup() != null
					&& !messageDetailWrapper.getVolteMTCallSetup().isEmpty()) {
				messageKpiWrapper.setIsSuccess("1");
				messageKpiWrapper.setResult(SUCCESS);
				if (messageDetailWrapper.getSipMessage()!=null && !StringUtils.isBlank(messageDetailWrapper.getSipMessage())) {
					String msg = messageDetailWrapper.getSipMessage().replaceAll("\\$", "\\\n");
					messageKpiWrapper.setStartMessageDetail(msg);
					messageKpiWrapper.setEndMessageDetail(msg);
				}
				if (messageDetailWrapper.getTimeStamp() != null) {
					messageKpiWrapper.setStartTime(
							NVLayer3Utils.getDateFromTimestamp(messageDetailWrapper.getTimeStamp().toString()));
					messageKpiWrapper.setEndTime(
							NVLayer3Utils.getDateFromTimestamp(messageDetailWrapper.getTimeStamp().toString()));
				}
				if (messageDetailWrapper.getMtCallConnectionSetupTime() != null) {
					Double time = Double.parseDouble(messageDetailWrapper.getMtCallConnectionSetupTime());
					messageKpiWrapper.setDuration(time);
				}
				messageKpiWrapperList.add(messageKpiWrapper);
				messageKpiWrapper = new MessageKpiWrapper();
				continue;
			} else if (messageDetailWrapper.getVolteMTCallAttempts() != null
					&& !messageDetailWrapper.getVolteMTCallAttempts().isEmpty()
					&& (messageDetailWrapper.getVolteMTCallSetup() == null
							|| messageDetailWrapper.getVolteMTCallSetup().isEmpty())) {
				messageKpiWrapper = new MessageKpiWrapper();
				if (messageDetailWrapper.getSipMessage()!=null && !StringUtils.isBlank(messageDetailWrapper.getSipMessage())) {
					String msg = messageDetailWrapper.getSipMessage().replaceAll("\\$", "\\\n");
					messageKpiWrapper.setStartMessageDetail(msg);
				}
				if (messageDetailWrapper.getTimeStamp() != null) {
					messageKpiWrapper.setStartTime(
							NVLayer3Utils.getDateFromTimestamp(messageDetailWrapper.getTimeStamp().toString()));
				}
				onlyAtRequest = true;
			}
		}
	}

	private void setVoLTEMOMessageDataToWrapper(SSVTReportSubWrapper subwrapper,
			Map<Integer, List<MessageDetailWrapper>> pciWiseMessageData) {
		List<SectorSwapWrapper> list = new ArrayList<>();
		logger.info("inside method getattachmessagdata to wrapper {}", pciWiseMessageData.size());
		if (Utils.isValidMap(pciWiseMessageData)) {
			for (Entry<Integer, List<MessageDetailWrapper>> map : pciWiseMessageData.entrySet()) {
				List<MessageKpiWrapper> messageKpiWrapperList = new ArrayList<>();
				SectorSwapWrapper sectorWrapper = new SectorSwapWrapper();
				if (map.getValue() != null) {

					List<MessageDetailWrapper> filteredList =	map.getValue().stream()
							.filter(x -> (x.getPCI() != null && !x.getPCI().isEmpty())
									&& map.getKey().toString().equalsIgnoreCase(x.getPCI()))
							.collect(Collectors.toList());
					setVoLTEMOData(filteredList, messageKpiWrapperList);
					sectorWrapper.setMessageList(messageKpiWrapperList);
					sectorWrapper.setSectorId(map.getKey().toString());

					list.add(sectorWrapper);
				}
			}
		}
		subwrapper.setVolteMoSectorWiseMessagList(list);
	}

	private void setVoLTEMOData(List<MessageDetailWrapper> list, List<MessageKpiWrapper> messageKpiWrapperList) {
		MessageKpiWrapper messageKpiWrapper = null;
		boolean onlyAtRequest = false;
		for (MessageDetailWrapper messageDetailWrapper : list) {
			if (onlyAtRequest && messageKpiWrapper != null) {
				if (messageDetailWrapper.getVolteMOCallSetup() != null
						&& !messageDetailWrapper.getVolteMOCallSetup().isEmpty()) {
					if (messageDetailWrapper.getTimeStamp() != null) {
						messageKpiWrapper.setEndTime(
								NVLayer3Utils.getDateFromTimestamp(messageDetailWrapper.getTimeStamp().toString()));
					}
					if (messageDetailWrapper.getSipMessage()!=null && !StringUtils.isBlank(messageDetailWrapper.getSipMessage())) {
						String msg = messageDetailWrapper.getSipMessage().replaceAll("\\$", "\\\n");
						messageKpiWrapper.setEndMessageDetail(msg);
					}
					if (messageDetailWrapper.getMoCallConnectionSetupTime() != null) {
						Double time = Double.parseDouble(messageDetailWrapper.getMoCallConnectionSetupTime());
						messageKpiWrapper.setDuration(time);
					}
					messageKpiWrapper.setIsSuccess("1");
					messageKpiWrapper.setResult("Suceess");
					messageKpiWrapperList.add(messageKpiWrapper);

					messageKpiWrapper = new MessageKpiWrapper();
					onlyAtRequest = false;
					continue;
				} else if (messageDetailWrapper.getVolteMOCallAttempts() != null
						&& !messageDetailWrapper.getVolteMOCallAttempts().isEmpty()) {
					messageKpiWrapper.setIsSuccess("0");
					messageKpiWrapper.setResult("Failure");
					messageKpiWrapperList.add(messageKpiWrapper);
					messageKpiWrapper = new MessageKpiWrapper();
					if (messageDetailWrapper.getSipMessage()!=null && !StringUtils.isBlank(messageDetailWrapper.getSipMessage())) {
						String msg = messageDetailWrapper.getSipMessage().replaceAll("\\$", "\\\n");
						messageKpiWrapper.setStartMessageDetail(msg);
					}
					if (messageDetailWrapper.getTimeStamp() != null) {
						messageKpiWrapper.setStartTime(
								NVLayer3Utils.getDateFromTimestamp(messageDetailWrapper.getTimeStamp().toString()));
						messageKpiWrapper.setStarttimestamp(messageDetailWrapper.getTimeStamp());
					}
					onlyAtRequest = true;

					continue;
				}
			} else if (messageKpiWrapper != null && messageDetailWrapper.getVolteMOCallAttempts() != null
					&& !messageDetailWrapper.getVolteMOCallAttempts().isEmpty()
					&& messageDetailWrapper.getVolteMOCallSetup() != null
					&& !messageDetailWrapper.getVolteMOCallSetup().isEmpty()) {
				messageKpiWrapper.setIsSuccess("1");
				messageKpiWrapper.setResult(SUCCESS);
				if (messageDetailWrapper.getSipMessage()!=null && !StringUtils.isBlank(messageDetailWrapper.getSipMessage())) {
					String msg = messageDetailWrapper.getSipMessage().replaceAll("\\$", "\\\n");
					messageKpiWrapper.setStartMessageDetail(msg);
					messageKpiWrapper.setEndMessageDetail(msg);
				}
				if (messageDetailWrapper.getTimeStamp() != null) {
					messageKpiWrapper.setStartTime(
							NVLayer3Utils.getDateFromTimestamp(messageDetailWrapper.getTimeStamp().toString()));
					messageKpiWrapper.setEndTime(
							NVLayer3Utils.getDateFromTimestamp(messageDetailWrapper.getTimeStamp().toString()));
				}
				if (messageDetailWrapper.getMoCallConnectionSetupTime() != null) {
					Double time = Double.parseDouble(messageDetailWrapper.getMoCallConnectionSetupTime());
					messageKpiWrapper.setDuration(time);
				}
				messageKpiWrapperList.add(messageKpiWrapper);
				messageKpiWrapper = new MessageKpiWrapper();
				continue;
			} else if (messageDetailWrapper.getVolteMOCallAttempts() != null
					&& !messageDetailWrapper.getVolteMOCallAttempts().isEmpty()
					&& (messageDetailWrapper.getVolteMOCallSetup() == null
							|| messageDetailWrapper.getVolteMOCallSetup().isEmpty())) {
				messageKpiWrapper = new MessageKpiWrapper();

				if (messageDetailWrapper.getSipMessage()!=null && !StringUtils.isBlank(messageDetailWrapper.getSipMessage())) {
					String msg = messageDetailWrapper.getSipMessage().replaceAll("\\$", "\\\n");
					messageKpiWrapper.setStartMessageDetail(msg);
				}
				if (messageDetailWrapper.getTimeStamp() != null) {
					messageKpiWrapper.setStartTime(
							NVLayer3Utils.getDateFromTimestamp(messageDetailWrapper.getTimeStamp().toString()));
				}
				onlyAtRequest = true;
			}
		}
	}

	private void setFastReturnMessageDataToWrapper(SSVTReportSubWrapper subwrapper,
			Map<Integer, List<MessageDetailWrapper>> pciWiseMessageData) {
		List<SectorSwapWrapper> list = new ArrayList<>();
		logger.info("inside method getattachmessagdata to wrapper {}", pciWiseMessageData.size());
		if (Utils.isValidMap(pciWiseMessageData)) {
			for (Entry<Integer, List<MessageDetailWrapper>> map : pciWiseMessageData.entrySet()) {
				List<MessageKpiWrapper> messageKpiWrapperList = new ArrayList<>();
				SectorSwapWrapper sectorWrapper = new SectorSwapWrapper();
				if (map.getValue() != null) {

					List<MessageDetailWrapper> filteredList =	map.getValue().stream()
							.filter(x -> (x.getPCI() != null && !x.getPCI().isEmpty())
									&& map.getKey().toString().equalsIgnoreCase(x.getPCI()))
							.collect(Collectors.toList());
					
					setFastReturnData(filteredList, messageKpiWrapperList);
					sectorWrapper.setMessageList(messageKpiWrapperList);
					sectorWrapper.setSectorId(map.getKey().toString());

					list.add(sectorWrapper);
				}
			}
		}
		subwrapper.setfRTSectorWiseMessagList(list);
	}

	private void setFastReturnData(List<MessageDetailWrapper> list, List<MessageKpiWrapper> messageKpiWrapperList) {
		MessageKpiWrapper messageKpiWrapper = null;
		boolean onlyAtRequest = false;
		for (MessageDetailWrapper messageDetailWrapper : list) {
			if (onlyAtRequest && messageKpiWrapper != null) {
				if (messageDetailWrapper.getFastReturnTime() != null
						&& !messageDetailWrapper.getFastReturnTime().isEmpty()) {
					if (messageDetailWrapper.getTimeStamp() != null) {
						messageKpiWrapper.setEndTime(
								NVLayer3Utils.getDateFromTimestamp(messageDetailWrapper.getTimeStamp().toString()));
					}
					if (messageDetailWrapper.getDecodedMsg()!=null && !StringUtils.isBlank(messageDetailWrapper.getDecodedMsg())) {
						String msg = messageDetailWrapper.getDecodedMsg().replaceAll("\\$", "\\\n");
						messageKpiWrapper.setEndMessageDetail(msg);
					}
					if (messageDetailWrapper.getFastReturnTime() != null) {
						Double time = Double.parseDouble(messageDetailWrapper.getFastReturnTime());
						messageKpiWrapper.setDuration(time);
					}
					messageKpiWrapper.setIsSuccess("1");
					messageKpiWrapper.setResult("Suceess");
					messageKpiWrapperList.add(messageKpiWrapper);

					messageKpiWrapper = new MessageKpiWrapper();
					onlyAtRequest = false;
					continue;
				} else if (messageDetailWrapper.getChannelRelease() != null
						&& !messageDetailWrapper.getChannelRelease().isEmpty()) {
					messageKpiWrapper.setIsSuccess("0");
					messageKpiWrapper.setResult("Failure");
					messageKpiWrapper = new MessageKpiWrapper();
					if (messageDetailWrapper.getDecodedMsg()!=null && !StringUtils.isBlank(messageDetailWrapper.getDecodedMsg())) {
						String msg = messageDetailWrapper.getDecodedMsg().replaceAll("\\$", "\\\n");
						messageKpiWrapper.setStartMessageDetail(msg);
					}
					if (messageDetailWrapper.getTimeStamp() != null) {
						messageKpiWrapper.setStartTime(
								NVLayer3Utils.getDateFromTimestamp(messageDetailWrapper.getTimeStamp().toString()));
						messageKpiWrapper.setStarttimestamp(messageDetailWrapper.getTimeStamp());
					}
					onlyAtRequest = true;

					continue;
				}
			} else if (messageKpiWrapper != null && messageDetailWrapper.getChannelRelease() != null
					&& !messageDetailWrapper.getChannelRelease().isEmpty()
					&& messageDetailWrapper.getFastReturnTime() != null
					&& !messageDetailWrapper.getFastReturnTime().isEmpty()) {
				messageKpiWrapper.setIsSuccess("1");
				messageKpiWrapper.setResult(SUCCESS);
				if (messageDetailWrapper.getDecodedMsg()!=null && !StringUtils.isBlank(messageDetailWrapper.getDecodedMsg())) {
					String msg = messageDetailWrapper.getDecodedMsg().replaceAll("\\$", "\\\n");
					messageKpiWrapper.setStartMessageDetail(msg);
					messageKpiWrapper.setEndMessageDetail(msg);
				}
				if (messageDetailWrapper.getTimeStamp() != null) {
					messageKpiWrapper.setStartTime(
							NVLayer3Utils.getDateFromTimestamp(messageDetailWrapper.getTimeStamp().toString()));
					messageKpiWrapper.setEndTime(
							NVLayer3Utils.getDateFromTimestamp(messageDetailWrapper.getTimeStamp().toString()));
				}
				if (messageDetailWrapper.getFastReturnTime() != null) {
					Double time = Double.parseDouble(messageDetailWrapper.getFastReturnTime());
					messageKpiWrapper.setDuration(time);
				}
				messageKpiWrapperList.add(messageKpiWrapper);
				messageKpiWrapper = new MessageKpiWrapper();
				continue;
			} else if (messageDetailWrapper.getChannelRelease() != null
					&& !messageDetailWrapper.getChannelRelease().isEmpty()
					&& (messageDetailWrapper.getFastReturnTime() == null
							|| messageDetailWrapper.getFastReturnTime().isEmpty())) {
				messageKpiWrapper = new MessageKpiWrapper();

				if (messageDetailWrapper.getDecodedMsg()!=null && !StringUtils.isBlank(messageDetailWrapper.getDecodedMsg())) {
					String msg = messageDetailWrapper.getDecodedMsg().replaceAll("\\$", "\\n");
					messageKpiWrapper.setStartMessageDetail(msg);
				}
				if (messageDetailWrapper.getTimeStamp() != null) {
					messageKpiWrapper.setStartTime(
							NVLayer3Utils.getDateFromTimestamp(messageDetailWrapper.getTimeStamp().toString()));
				}
				onlyAtRequest = true;
			}
		}
	}

	private void setRRCMessageDataToWrapper(SSVTReportSubWrapper subwrapper,
			Map<Integer, List<MessageDetailWrapper>> pciWiseMessageData) {
		List<SectorSwapWrapper> list = new ArrayList<>();
		logger.info("inside method rrcmessagedata to wrapper {}", pciWiseMessageData.size());
		if (Utils.isValidMap(pciWiseMessageData)) {
			for (Entry<Integer, List<MessageDetailWrapper>> map : pciWiseMessageData.entrySet()) {
				List<MessageKpiWrapper> messageKpiWrapperList = new ArrayList<>();
				SectorSwapWrapper sectorWrapper = new SectorSwapWrapper();
				if (map.getValue() != null) {
					
					List<MessageDetailWrapper> filteredList =	map.getValue().stream()
							.filter(x -> (x.getPCI() != null && !x.getPCI().isEmpty())
									&& map.getKey().toString().equalsIgnoreCase(x.getPCI()))
							.collect(Collectors.toList());

					filteredList.sort(Comparator.comparing(MessageDetailWrapper::getTimeStamp));
					setRRCData(filteredList, messageKpiWrapperList);
					sectorWrapper.setMessageList(messageKpiWrapperList);
					sectorWrapper.setSectorId(map.getKey().toString());

					list.add(sectorWrapper);
				}
			}
		}
		subwrapper.setRrcWiseMessagList(list);
		logger.info("size of rrc list {}", new Gson().toJson(list));
	}

	private void setRRCData(List<MessageDetailWrapper> list, List<MessageKpiWrapper> messageKpiWrapperList) {
		logger.info("inside method setrrcData");
		for (MessageDetailWrapper messageDetailWrapper : list) {
			
			if (messageDetailWrapper.getRrcConnectionRequest() != null
					&& !messageDetailWrapper.getRrcConnectionRequest().isEmpty()) {
				MessageKpiWrapper messageKpiWrapper = new MessageKpiWrapper();

				messageKpiWrapper.setStartMessageDetail("RRCConnectionRequest (UL-CCCH)");
				if (messageDetailWrapper.getTimeStamp() != null) {
					messageKpiWrapper.setStartTime(
							NVLayer3Utils.getDateFromTimestamp(messageDetailWrapper.getTimeStamp().toString()));
					messageKpiWrapper.setStarttimestamp(messageDetailWrapper.getTimeStamp());
				}
				messageKpiWrapperList.add(messageKpiWrapper);
			}
		 if (messageDetailWrapper.getRrcConnectionSetup() != null
					&& !messageDetailWrapper.getRrcConnectionSetup().isEmpty()) {
				MessageKpiWrapper messageKpiWrapper = new MessageKpiWrapper();

				messageKpiWrapper.setStartMessageDetail("RRCConnectionSetupComplete (UL-DCCH)");
				if (messageDetailWrapper.getTimeStamp() != null) {
					messageKpiWrapper.setStartTime(
							NVLayer3Utils.getDateFromTimestamp(messageDetailWrapper.getTimeStamp().toString()));
					messageKpiWrapper.setEndTime(
							NVLayer3Utils.getDateFromTimestamp(messageDetailWrapper.getTimeStamp().toString()));
				}
				if (messageDetailWrapper.getRrcConnectionSetupTime() != null) {
					Double time = Double.parseDouble(messageDetailWrapper.getRrcConnectionSetupTime());
					messageKpiWrapper.setDuration(time);
				}
				messageKpiWrapperList.add(messageKpiWrapper);

			}
		}
	}

	private List<GraphWrapper> getScreenShotForDriveData(Map<Integer, List<String[]>> pciWiseDriveDataMap,
			Map<String, Integer> kpiIndexMap) {
		List<GraphWrapper> graphList = new ArrayList<>();
		try {

			for (Entry<Integer, List<String[]>> entry : pciWiseDriveDataMap.entrySet()) {
				List<String[]> dlDataList = ReportUtil.filterDataByTestType(entry.getValue(),
						kpiIndexMap.get(ReportConstants.TEST_TYPE), NetworkDataFormats.TEST_TYPE_HTTP_DOWNLOAD,
						NetworkDataFormats.TEST_TYPE_FTP_DOWNLOAD);
				GraphWrapper wrapper = getPdschThroughputScreenshotData(kpiIndexMap, dlDataList);
				if (wrapper != null) {
					wrapper.setEventLegendImg(getGraphkpiImage(kpiIndexMap, dlDataList, entry.getKey(), "DL"));
					wrapper.setTechnology(setKpiStringForImage(kpiIndexMap, dlDataList, entry.getKey(), "DL"));

					graphList.add(wrapper);
				}
				List<String[]> ulDataList = ReportUtil.filterDataByTestType(entry.getValue(),
						kpiIndexMap.get(ReportConstants.TEST_TYPE), NetworkDataFormats.TEST_TYPE_HTTP_UPLOAD,
						NetworkDataFormats.TEST_TYPE_FTP_UPLOAD);

				GraphWrapper puschWrapper = getPuschThroughputScreenshotData(kpiIndexMap, ulDataList);
				puschWrapper.setEventLegendImg(getGraphkpiImage(kpiIndexMap, ulDataList, entry.getKey(), "UL"));
				puschWrapper.setTechnology(setKpiStringForImage(kpiIndexMap, ulDataList, entry.getKey(), "UL"));
				graphList.add(puschWrapper);

			}
			logger.info(" screenshot for drivedata {}", new Gson().toJson(graphList));
		} catch (Exception e) {
			logger.error(" error screenshot for drivedata {}", Utils.getStackTrace(e));
		}
		return graphList;
	}

	private GraphWrapper getPdschThroughputScreenshotData(Map<String, Integer> kpiIndexMap, List<String[]> dlDataList) {
		GraphWrapper wrapper = new GraphWrapper();
		try {
			if (Utils.isValidList(dlDataList)) {
				List<GraphDataWrapper> graphDataList = new ArrayList<>();
				Integer graphpci = 0;
				if (kpiIndexMap.containsKey(ReportConstants.PCI_PLOT)) {

					IntStream pci = dlDataList.stream()
							.filter(x -> (x[kpiIndexMap.get(ReportConstants.PCI_PLOT)] != null
									&& !x[kpiIndexMap.get(ReportConstants.PCI_PLOT)].isEmpty()))
							.map(x -> x[kpiIndexMap.get(ReportConstants.PCI_PLOT)]).collect(Collectors.toList())
							.stream().mapToInt(i -> Integer.parseInt(i));
					if (pci != null) {
						graphpci = pci.findFirst().getAsInt();
					}
				}
				wrapper.setMax(getMaxValueFromIndex(kpiIndexMap.get(ReportConstants.PDSCH_THROUGHPUT), dlDataList));
				wrapper.setMean(getAvgOfIndexData(kpiIndexMap.get(ReportConstants.PDSCH_THROUGHPUT), dlDataList));
				wrapper.setMin(getMinValueFromIndex(kpiIndexMap.get(ReportConstants.PDSCH_THROUGHPUT), dlDataList));
				wrapper.setKpiName(Symbol.PARENTHESIS_OPEN_STRING
						+ ReportConstants.DL.replace(Symbol.UNDERSCORE_STRING, Symbol.SPACE_STRING)
						+ Symbol.SPACE_STRING + ReportConstants.PCI + Symbol.UNDERSCORE_STRING + graphpci
						+ Symbol.PARENTHESIS_CLOSE_STRING);

				for (String[] arr : dlDataList) {
					if (arr != null && ReportUtil.checkIndexValue(kpiIndexMap.get(ReportConstants.TIMESTAMP), arr)
							&& ReportUtil.checkIndexValue(kpiIndexMap.get(ReportConstants.PDSCH_THROUGHPUT), arr)) {
						GraphDataWrapper gaphWrapper = new GraphDataWrapper();
						gaphWrapper.setTime(new Date(Long.parseLong(arr[kpiIndexMap.get(ReportConstants.TIMESTAMP)])));
						gaphWrapper
								.setValue(Double.parseDouble(arr[kpiIndexMap.get(ReportConstants.PDSCH_THROUGHPUT)]));
						graphDataList.add(gaphWrapper);
					}
				}
				wrapper.setGraphDataList(graphDataList);
				return wrapper;

			}
		} catch (NumberFormatException e) {
			logger.error("Exception inside method getPdschThroughputScreenshotData {} ", Utils.getStackTrace(e));
		}
		return null;
	}

	private GraphWrapper getPuschThroughputScreenshotData(Map<String, Integer> kpiIndexMap, List<String[]> ulDataList) {
		GraphWrapper wrapper = new GraphWrapper();
		if (Utils.isValidList(ulDataList)) {
			List<GraphDataWrapper> graphDataList = new ArrayList<>();
			Integer graphpci = 0;
			if (kpiIndexMap.containsKey(ReportConstants.PCI_PLOT)) {
				IntStream pci = ulDataList.stream()
						.filter(x -> (x[kpiIndexMap.get(ReportConstants.PCI_PLOT)] != null
								&& !x[kpiIndexMap.get(ReportConstants.PCI_PLOT)].isEmpty()))
						.map(x -> x[kpiIndexMap.get(ReportConstants.PCI_PLOT)]).collect(Collectors.toList()).stream()
						.mapToInt(i -> Integer.parseInt(i));
				if (pci != null) {
					graphpci = pci.findFirst().getAsInt();
				}
			}
			wrapper.setMax(getMaxValueFromIndex(kpiIndexMap.get(ReportConstants.PUSCH_THROUGHPUT), ulDataList));
			wrapper.setMean(getAvgOfIndexData(kpiIndexMap.get(ReportConstants.PUSCH_THROUGHPUT), ulDataList));
			wrapper.setMin(getMinValueFromIndex(kpiIndexMap.get(ReportConstants.PUSCH_THROUGHPUT), ulDataList));
			wrapper.setKpiName(Symbol.PARENTHESIS_OPEN_STRING
					+ ReportConstants.UL.replace(Symbol.UNDERSCORE_STRING, Symbol.SPACE_STRING) + Symbol.SPACE_STRING
					+ ReportConstants.PCI + Symbol.UNDERSCORE_STRING + graphpci + Symbol.PARENTHESIS_CLOSE_STRING);
			for (String[] arr : ulDataList) {
				if (arr != null && ReportUtil.checkIndexValue(kpiIndexMap.get(ReportConstants.TIMESTAMP), arr)
						&& ReportUtil.checkIndexValue(kpiIndexMap.get(ReportConstants.PUSCH_THROUGHPUT), arr)) {
					GraphDataWrapper gaphWrapper = new GraphDataWrapper();
					gaphWrapper.setTime(new Date(Long.parseLong(arr[kpiIndexMap.get(ReportConstants.TIMESTAMP)])));
					gaphWrapper.setValue(Double.parseDouble(arr[kpiIndexMap.get(ReportConstants.PUSCH_THROUGHPUT)]));
					graphDataList.add(gaphWrapper);
				}
			}
			wrapper.setGraphDataList(graphDataList);
		}
		return wrapper;
	}

	public GraphWrapper setDataForCodecGraph(List<String[]> arlist, Map<String, Integer> kpiIndexMap) {
		logger.info("inside the method  setDataForCodecGraph");
		GraphWrapper graphWrapper = new GraphWrapper();
		List<GraphDataWrapper> graphDataList = new ArrayList<>();
		try {
			Double cdf = 0.0;
			List<String> dataList = arlist.stream()
					.filter(x -> !StringUtils.isBlank(x[kpiIndexMap.get(ReportConstants.VOLTE_CODEC)]))
					.map(x -> x[kpiIndexMap.get(ReportConstants.VOLTE_CODEC)]).collect(Collectors.toList());

			Map<Object, Long> kpiCountMap = dataList.stream()
					.collect(Collectors.groupingBy(x -> x, Collectors.counting()));

			Integer totalCount = dataList.size();
			for (Entry<Object, Long> map : kpiCountMap.entrySet()) {
				GraphDataWrapper graphData = new GraphDataWrapper();
				Double pdf = Utils.getPercentage(map.getValue().intValue(), totalCount);
				cdf += pdf;
				graphData.setPdfValue(pdf);
				graphData.setCdfValue(cdf);
				graphData.setCount(map.getValue().intValue());
				graphData.setKpiName(map.getKey().toString());
				graphDataList.add(graphData);
			}
			graphWrapper.setGraphDataList(graphDataList);
			logger.info("volte code list is :: {} ",graphDataList);
			return graphWrapper;
		} catch (Exception e) {
			logger.error("Error inside the method setDataForEarfcn {}", Utils.getStackTrace(e));
		}
		return graphWrapper;
	}
	
	private Map<String, String> getFirstLocation(List<String[]> dataList, Integer indexlat, Integer indexlon) {
		Map<String, String> map = new HashMap<>();
		try {
			if (Utils.isValidList(dataList)) {
				for (String[] data : dataList) {
					if (ReportUtil.checkIndexValue(indexlat, data)) {
						map.put(ReportConstants.LATITUDE, data[indexlat]);
					}
					if (ReportUtil.checkIndexValue(indexlat, data)) {
						map.put(ReportConstants.LONGITUDE, data[indexlon]);
					}
					if (map.get(ReportConstants.LATITUDE) != null && map.get(ReportConstants.LONGITUDE) != null) {
						break;
					}
				}
			}
		} catch (Exception e) {
			logger.info("Error inside getFirstLocation {} ", Utils.getStackTrace(e));
		}
		return map;
	}
	
	private List<WORecipeMapping> getFilteredMappingsForDrive(List<WORecipeMapping> recipeMappings,
			Map<String, Integer> recipePciMap) {
		return recipeMappings.stream().filter(mapping -> !(recipePciMap.keySet().contains(mapping.getId().toString())))
				.collect(Collectors.toList());
	}
	
	private List<WORecipeMapping> getFilteredMappingsForStationary(List<WORecipeMapping> recipeMappings,
			Map<String, Integer> recipePciMap) {
		return recipeMappings.stream().filter(mapping -> (recipePciMap.keySet().contains(mapping.getId().toString())))
				.collect(Collectors.toList());
	} 
	
	private Map<Integer, Map<String, List<String[]>>> getPciWiseStData(Integer workorderId, List<String> fetchKPIList,
			Map<String, Integer> kpiIndexMap, Map<String, Integer> recipePciMap, List<WORecipeMapping> recipeMappings) {
		List<WORecipeMapping> stationaryRecipeMappings = getFilteredMappingsForStationary(recipeMappings, recipePciMap);
		Map<Integer, Map<String, List<String[]>>> pciWiseStMap = new HashMap<>();
		Set<Integer> pciList = getSetOfPci(recipePciMap);
		try {
			for (Integer pci : pciList) {
				List<String[]> allRecipeDataList = new ArrayList<>();
				Map<String, List<String[]>> recipeNameWiseMap = new HashMap<>();
				for (WORecipeMapping woRecipeMapping : stationaryRecipeMappings) {
					if (Status.COMPLETED.equals(woRecipeMapping.getStatus())
							&& recipePciMap.get(woRecipeMapping.getId().toString()).equals(pci)) {
						List<String[]> data = reportService.getDriveDataRecipeWiseTaggedForReport(
								Arrays.asList(workorderId), Arrays.asList(woRecipeMapping.getId().toString()),
								fetchKPIList, kpiIndexMap);

						ReportUtil.addKPIInEmptyFields(data, kpiIndexMap.get(ReportConstants.PCI_PLOT));
						ReportUtil.addKPIInEmptyFields(data, kpiIndexMap.get(ReportConstants.TECHNOLOGY));
						recipeNameWiseMap.put(woRecipeMapping.getRecipe().getName(), data);
						allRecipeDataList.addAll(data);
					}
				}
				recipeNameWiseMap.put(ReportConstants.ALL_RECIPE, allRecipeDataList);
				pciWiseStMap.put(pci, recipeNameWiseMap);
			}
		} catch (Exception e) {
			logger.error("Exception inside method getPciWiseStData {} ", Utils.getStackTrace(e));
		}
		return pciWiseStMap;
	}
	
	private Map<Integer, Map<String, List<String[]>>> getPciWiseDriveData(Integer workorderId,
			List<String> fetchKPIList, Map<String, Integer> kpiIndexMap, Map<String, Integer> recipePciMap,
			List<WORecipeMapping> recipeMappings) {
		Map<Integer, Map<String, List<String[]>>> pciWiseDriveMap = new HashMap<>();
		List<WORecipeMapping> driveRecipeMappings = getFilteredMappingsForDrive(recipeMappings,recipePciMap);
		Collection<Integer> pciList = recipePciMap.values();
		for (Integer pci : pciList) {
			Map<String, List<String[]>> map = new HashMap<>();
			pciWiseDriveMap.put(pci, map);
		}

		if (Utils.isValidList(driveRecipeMappings)) {
			for (WORecipeMapping woRecipeMapping : recipeMappings) {
				if (Status.COMPLETED.equals(woRecipeMapping.getStatus())) {
					List<String[]> dataList = reportService.getDriveDataRecipeWiseTaggedForReport(
							Arrays.asList(workorderId), Arrays.asList(woRecipeMapping.getId().toString()), fetchKPIList,
							kpiIndexMap);

					ReportUtil.addPciInEmptyFields(dataList, kpiIndexMap);
					ReportUtil.addTechnologyInEmptyFields(dataList, kpiIndexMap);
					for (Integer pci : pciList) {
						List<String[]> newList = new ArrayList<>();
						for (String[] data : dataList) {
							if (ReportUtil.checkIndexValue(kpiIndexMap.get(ReportConstants.PCI_PLOT), data)
									&& (data[kpiIndexMap.get(ReportConstants.PCI_PLOT)])
											.equalsIgnoreCase(pci.toString())) {
								newList.add(data);
							}
						}
						if (Utils.isValidList(newList)) {
							Map<String, List<String[]>> recipeNameMap = pciWiseDriveMap.get(pci);
							recipeNameMap.put(woRecipeMapping.getRecipe().getName(), newList);
						}
					}
				}
			} 
		}
		return pciWiseDriveMap;
	}	
	
	public void setMessageDataForSSVTExcel(Integer workorderId, SSVTReportSubWrapper subwrapper,
			List<WORecipeMapping> woRecipeMapping, Map<String, Integer> recipePciMap) {
		try {
			if (Utils.isValidMap(recipePciMap)) {
				List<WORecipeMapping> woSTRecipeMapping = getFilteredMappingsForStationary(woRecipeMapping,
						recipePciMap);
				Map<Integer, List<MessageDetailWrapper>> pciWiseMessageMapATDT = getPciWiseMessageDataReciepNameWise(workorderId,
						recipePciMap, woSTRecipeMapping, ReportConstants.SCFT_IDLE_AT_DT);
				Map<Integer, List<MessageDetailWrapper>> pciWiseMessageMapCSMO = getPciWiseMessageDataReciepNameWise(workorderId,
						recipePciMap, woSTRecipeMapping, ReportConstants.SCFT_CSFB_CALL_MO_200ms);
				Map<Integer, List<MessageDetailWrapper>> pciWiseMessageMapCSMT = getPciWiseMessageDataReciepNameWise(workorderId,
						recipePciMap, woSTRecipeMapping, ReportConstants.SCFT_CSFB_MT_CALL_200ms);
				Map<Integer, List<MessageDetailWrapper>> pciWiseMessageMapSCCombine = getPciWiseMessageDataReciepNameWise(workorderId,
						recipePciMap, woSTRecipeMapping, ReportConstants.SCFT_ST_NK_Combine);
				Map<Integer, List<MessageDetailWrapper>> pciWiseMessageMapVolteMt = getPciWiseMessageDataReciepNameWise(workorderId,
						recipePciMap, woSTRecipeMapping, ReportConstants.VOLTE_MT_CALL_CONNECTED_IDLE_MODE_200MS);
				setAttachMessageDataToWrapper(subwrapper, pciWiseMessageMapATDT);
				setDetachMessageDataToWrapper(subwrapper, pciWiseMessageMapATDT);
				setCSMOMessageDataToWrapper(subwrapper, pciWiseMessageMapCSMO);
				setCSMTMessageDataToWrapper(subwrapper, pciWiseMessageMapCSMT);
				setVoLTEMTMessageDataToWrapper(subwrapper, pciWiseMessageMapVolteMt);
				setVoLTEMOMessageDataToWrapper(subwrapper, pciWiseMessageMapSCCombine);
				setFastReturnMessageDataToWrapper(subwrapper, pciWiseMessageMapCSMO);
				
				Map<Integer, List<MessageDetailWrapper>> pciWiseMessageMap = getPciWiseMessageDataForRRC(workorderId,
						recipePciMap, woSTRecipeMapping);
				setRRCMessageDataToWrapper(subwrapper, pciWiseMessageMap);
			}
		} catch (Exception e) {
			logger.error("Exception inside method setMessageDataForSSVTExcel {}", Utils.getStackTrace(e));
		}
	}

	private Map<Integer, List<MessageDetailWrapper>> getPciWiseMessageDataReciepNameWise(Integer workorderId,
			Map<String, Integer> recipePciMap, List<WORecipeMapping> recipeMappings, String recipeName) {

		List<WORecipeMapping> nameWiseMapping =  getWORecipeMappingFilteredByName(recipeMappings,recipeName);
		Map<Integer, List<MessageDetailWrapper>> pciWiseMessageMap = new HashMap<>();
		try {
			for (WORecipeMapping woRecipeMapping : nameWiseMapping) {
				if (Status.COMPLETED.equals(woRecipeMapping.getStatus()) && recipePciMap != null
						&& recipePciMap.containsKey(woRecipeMapping.getId().toString())) {

					List<MessageDetailWrapper> data = reportService.getLayer3MessagesDataForReport(workorderId,
							Arrays.asList(woRecipeMapping.getId().toString()), null);

					List<MessageDetailWrapper> messageDataList = carryForwardPciInWrapper(data);

					String recipeId = woRecipeMapping.getId().toString();
					pciWiseMessageMap.put(recipePciMap.get(recipeId), messageDataList);

				}
			}
		} catch (Exception e) {
			logger.error("Exception inside method getPciWiseMessageDataReciepNameWise {}", Utils.getStackTrace(e));
		}
		return pciWiseMessageMap;
	}
	
	private List<WORecipeMapping> getWORecipeMappingFilteredByName(List<WORecipeMapping> woSTRecipeMappings, String recipeName) {
		List<WORecipeMapping> recipeList = new ArrayList<>();
		for (WORecipeMapping woRecipeMapping : woSTRecipeMappings) {
			if (Status.COMPLETED.equals(woRecipeMapping.getStatus())
					&& woRecipeMapping.getRecipe().getName().equalsIgnoreCase(recipeName)) {
				recipeList.add(woRecipeMapping);
			}
		}
		return recipeList;
	}

	private Map<Integer, List<MessageDetailWrapper>> getPciWiseMessageDataForRRC(Integer workorderId, Map<String, Integer> recipePciMap,
			List<WORecipeMapping> recipeMappings) {
		Map<Integer, List<MessageDetailWrapper>> pciWiseMessageMap = new HashMap<>();
		try {
			for (WORecipeMapping woRecipeMapping : recipeMappings) {
				if (Status.COMPLETED.equals(woRecipeMapping.getStatus()) && recipePciMap != null
						&& recipePciMap.containsKey(woRecipeMapping.getId().toString())) {

					List<MessageDetailWrapper> data = reportService.getLayer3MessagesDataForReport(workorderId,
							Arrays.asList(woRecipeMapping.getId().toString()), null);
					List<MessageDetailWrapper> messageDataList = carryForwardPciInWrapper(data);
					String recipeId = woRecipeMapping.getId().toString();
					if(pciWiseMessageMap.containsKey(recipePciMap.get(recipeId))) {
						List<MessageDetailWrapper> msgList = pciWiseMessageMap.get(recipePciMap.get(recipeId));
						msgList.addAll(messageDataList);
						pciWiseMessageMap.put(recipePciMap.get(recipeId), msgList);
					}
					else {
					pciWiseMessageMap.put(recipePciMap.get(recipeId), messageDataList);
					}
				}
			}
		} catch (Exception e) {
			logger.error("Exception inside method getPciWiseMessageDataForRRC {} ", Utils.getStackTrace(e));
		}
		return pciWiseMessageMap;
	}
	

	private Set<Integer> getSetOfPci(Map<String, Integer> recipePciMap) {
		Set<Integer> pciSet = new HashSet();
		if (recipePciMap != null) {
			for (Entry<String, Integer> map : recipePciMap.entrySet()) {
				pciSet.add(map.getValue());
			}
		}
		return pciSet;
	}
}
package com.inn.foresight.module.nv.report.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.inn.commons.Symbol;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.io.FileUtils;
import com.inn.commons.lang.DateUtils;
import com.inn.commons.lang.NumberUtils;
import com.inn.foresight.core.generic.utils.DateUtil;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.report.dao.IAnalyticsRepositoryDao;
import com.inn.foresight.core.report.model.AnalyticsRepository.progress;
import com.inn.foresight.module.nv.NVConstant;
import com.inn.foresight.module.nv.core.workorder.dao.impl.IGenericWorkorderDao;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;
import com.inn.foresight.module.nv.layer3.constants.Layer3PPEConstant;
import com.inn.foresight.module.nv.layer3.utils.NVLayer3Utils;
import com.inn.foresight.module.nv.layer3.utils.NetworkDataFormats;
import com.inn.foresight.module.nv.report.RangeSlab;
import com.inn.foresight.module.nv.report.constant.ReportIndexWrapper;
import com.inn.foresight.module.nv.report.ib.utils.IBReportUtils;
import com.inn.foresight.module.nv.report.service.IIndoorBenchMarkReportService;
import com.inn.foresight.module.nv.report.service.IMapImagesService;
import com.inn.foresight.module.nv.report.service.INVInBuildingReportService;
import com.inn.foresight.module.nv.report.service.IReportService;
import com.inn.foresight.module.nv.report.utils.DriveHeaderConstants;
import com.inn.foresight.module.nv.report.utils.ReportConstants;
import com.inn.foresight.module.nv.report.utils.ReportUtil;
import com.inn.foresight.module.nv.report.workorder.wrapper.DriveDataWrapper;
import com.inn.foresight.module.nv.report.workorder.wrapper.KPIWrapper;
import com.inn.foresight.module.nv.report.wrapper.IndoorBenchMarkReportWrapper;
import com.inn.foresight.module.nv.report.wrapper.IndoorBenchMarkSubWrapper;
import com.inn.foresight.module.nv.report.wrapper.benchmark.BenchMarkOperatorInfo;
import com.inn.foresight.module.nv.report.wrapper.benchmark.BenchMarksubwrapper;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.InbuildingPointWrapper;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.benchmark.IBBenchmarkGraphDataWrapper;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;
import com.inn.foresight.module.nv.workorder.dao.IWORecipeMappingDao;
import com.inn.foresight.module.nv.workorder.model.WORecipeMapping;
import com.inn.foresight.module.nv.workorder.model.WORecipeMapping.Status;
import com.inn.foresight.module.nv.workorder.recipe.model.Recipe;
import com.inn.product.legends.dao.ILegendRangeDao;
import com.inn.product.legends.utils.LegendWrapper;

import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class IndoorBenchMarkReportServiceImpl implements IIndoorBenchMarkReportService {
	private Logger logger = LogManager.getLogger(IndoorBenchMarkReportServiceImpl.class);

	@Autowired
	private IReportService reportService;

	@Autowired
	private IWORecipeMappingDao recipeMappingDao;
	@Autowired
	private INVInBuildingReportService nvInBuildingReportService;
	@Autowired
	private ILegendRangeDao legendRangeDao;
	@Autowired
	private IMapImagesService mapImageService;
	@Autowired
	private IAnalyticsRepositoryDao analyticsrepositoryDao;
	@Autowired
	private IGenericWorkorderDao iGenericWorkorderDao;

	@Override
	public Response execute(String json) {

		logger.info("inside the method  execute json is {}", json);
		String localDirPath = ConfigUtils.getString(ReportConstants.INBUILDING_REPORT_PATH) + new Date().getTime();
		String imgFloorPlanPath = null;
		Map<String, Object> jsonMap = reportService.getJsonDataMap(json);
		Integer analyticsrepoId = (Integer) jsonMap.get(ReportConstants.ANALYTICS_REPOSITORY_ID);

		Integer workorderId = (Integer) jsonMap.get(ReportConstants.WORKORDER_ID);
		IndoorBenchMarkReportWrapper mainWrapper = new IndoorBenchMarkReportWrapper();
		BenchMarksubwrapper subWrapper = new BenchMarksubwrapper();
		List<BenchMarksubwrapper> subList = new ArrayList<>();
		Map<String, Object> imageMap = new HashMap<>();
		GenericWorkorder workorderObj = iGenericWorkorderDao.findByPk(workorderId);
		try {
			Map<String, List<String[]>> operatorWiseDataMap = new HashMap<>();
			if (reportService.getFileProcessedStatusForWorkorders(Arrays.asList(workorderId))) {
				List<String> fetchKPIList = getDynmicKpiList(workorderId);
				Map<String, Integer> kpiIndexMap = ReportIndexWrapper.getLiveDriveKPIIndexMap(fetchKPIList);
				List<KPIWrapper> kpiList = getKPIList(kpiIndexMap);
				imgFloorPlanPath = setOperatorWiseDataMaps(localDirPath, workorderId, operatorWiseDataMap, fetchKPIList,
						kpiIndexMap, kpiList, subWrapper);
				subWrapper.setMeasurementList(getMeasurementDataList(operatorWiseDataMap, kpiIndexMap));
				subWrapper.setServingDataList(getServingDataList(operatorWiseDataMap, kpiIndexMap));
				subWrapper.setCallFailDetail(prepareCallFailDataList(kpiIndexMap, operatorWiseDataMap));
				subWrapper.setCallDropDetail(prepareCallDropDataList(kpiIndexMap, operatorWiseDataMap));
			     setBandWiseMosData(operatorWiseDataMap,kpiIndexMap,subWrapper);
				File routeImg = getRouteImage(localDirPath, imgFloorPlanPath, operatorWiseDataMap, kpiIndexMap);
				if (routeImg != null) {
					imageMap.put(ReportConstants.ROUTE, routeImg.getAbsolutePath());
				}
				prepareGraphData(kpiIndexMap, operatorWiseDataMap, kpiList, subWrapper);
				prepareDataWrapper(mainWrapper, subWrapper, subList, operatorWiseDataMap, kpiIndexMap);

				return createAndSaveFile(localDirPath, analyticsrepoId, mainWrapper, imageMap, workorderObj);
			}
		} catch (Exception e) {
			logger.error("Exception inside the methd {}", Utils.getStackTrace(e));
			analyticsrepositoryDao.updateStatusInAnalyticsRepository(analyticsrepoId, null, "Something Went Wrong",
					progress.Failed, null);
		}

		finally {
			try {
				FileUtils.deleteDirectory(new File(localDirPath));
			} catch (IOException e) {
				logger.error("Exception to delete file {}", Utils.getStackTrace(e));

			}
		}
		return null;

	}

	private void setBandWiseMosData(Map<String, List<String[]>> operatorWiseDataMap, Map<String, Integer> kpiIndexMap,
			BenchMarksubwrapper subWrapper) {
		try {
			for (Entry<String, List<String[]>> entry : operatorWiseDataMap.entrySet()) {

				if (entry.getKey().equalsIgnoreCase(ConfigUtils.getString(NVConstant.BENCHMARK_DEFAULT_OPERATOR))) {
					List<IndoorBenchMarkSubWrapper> mosDataList = new ArrayList<>();

					List<String[]> volteCallDataList = ReportUtil.filterDataByTestType(entry.getValue(),
							kpiIndexMap.get(ReportConstants.TEST_TYPE), NetworkDataFormats.SHORT_CALL_TEST);
					List<String[]> filterList = volteCallDataList.stream()
							.filter(x -> x != null && (x.length > kpiIndexMap.get(ReportConstants.MOS))
									&& !x[kpiIndexMap.get(ReportConstants.MOS)].isEmpty()
									&& (Double.valueOf(x[kpiIndexMap.get(ReportConstants.MOS)]) < 2.0))
							.collect(Collectors.toList());
					Map<String, List<String[]>> bandWiseDataMap = getMapOfIndexKpi(
							kpiIndexMap.get(ReportConstants.BAND), filterList);
					for (Entry<String, List<String[]>> bandMap : bandWiseDataMap.entrySet()) {
						if (NumberUtils.isParsable(bandMap.getKey())) {
							IndoorBenchMarkSubWrapper wrapper = new IndoorBenchMarkSubWrapper();
							wrapper.setBand(
									NVLayer3Utils.getFrequencyFromBand(Integer.valueOf(bandMap.getKey()))+ " MHz");
							wrapper.setMosCount(bandMap.getValue().size());
							wrapper.setMosPercent(
									ReportUtil.getPercentage(bandMap.getValue().size(), filterList.size()));
							mosDataList.add(wrapper);
						}
					}
					if (!mosDataList.isEmpty()) {
						IndoorBenchMarkSubWrapper wrapper = new IndoorBenchMarkSubWrapper();

						wrapper.setMosCount(
								mosDataList.stream().mapToInt(IndoorBenchMarkSubWrapper::getMosCount).sum());
						wrapper.setBand(ReportConstants.TOTAL);
						wrapper.setMosPercent(
								mosDataList.stream().mapToDouble(IndoorBenchMarkSubWrapper::getMosPercent).sum());
						mosDataList.add(wrapper);
					}
					subWrapper.setMosDataList(mosDataList);
				}
			}
		}

		catch (Exception e) {
			logger.error("Exception inside the method {}", e.getMessage());
		}
	}

	private void prepareDataWrapper(IndoorBenchMarkReportWrapper mainWrapper, BenchMarksubwrapper subWrapper,
			List<BenchMarksubwrapper> subList, Map<String, List<String[]>> operatorWiseDataMap,
			Map<String, Integer> kpiIndexMap) {
		setDateInfo(mainWrapper, operatorWiseDataMap, kpiIndexMap);
		subList.add(subWrapper);
		mainWrapper.setSubList(subList);
	}

	private void setDateInfo(IndoorBenchMarkReportWrapper mainWrapper, Map<String, List<String[]>> operatorWiseDataMap,
			Map<String, Integer> kpiIndexMap) {
		try {
			List<String[]> dataList = getAllDataList(operatorWiseDataMap);
			Collections.sort(dataList, ReportUtil.getTimestampComparator(kpiIndexMap.get(ReportConstants.TIMESTAMP)));
			String[] startRow = dataList.get(ForesightConstants.INDEX_ZERO);
			String[] endRow = dataList.get(dataList.size() - ForesightConstants.INDEX_ONE);
			String statTime = startRow[kpiIndexMap.get(ReportConstants.TIMESTAMP)];
			String endTime = endRow[kpiIndexMap.get(ReportConstants.TIMESTAMP)];
			int i = DateUtil.getDifference(new Date(Long.parseLong(statTime)), new Date(Long.parseLong(statTime)));

			if (i > ForesightConstants.INDEX_ZERO) {
				Date startDate = new Date(Long.parseLong(statTime));
				Date endDate = new Date(Long.parseLong(statTime));
				Integer dayNo = DateUtils.getDayOfWeek(new Date(Long.parseLong(statTime)));
				Integer endDayNo = DateUtils.getDayOfWeek(new Date(Long.parseLong(endTime)));
				mainWrapper.setDaysOfMesurment(ReportUtil.getDayFromDayNo(dayNo) + Symbol.SPACE_STRING
						+ ReportConstants.TO + Symbol.SPACE_STRING + ReportUtil.getDayFromDayNo(endDayNo));
				mainWrapper.setDateOfMesurment(
						ReportUtil.getFormattedDate(startDate, "dd/MM/yyyy") + Symbol.SPACE_STRING + ReportConstants.TO
								+ Symbol.SPACE_STRING + ReportUtil.getFormattedDate(endDate, "dd/MM/yyyy"));
			} else {
				Date startDate = new Date(Long.parseLong(statTime));
				Integer dayNo = DateUtils.getDayOfWeek(new Date(Long.parseLong(statTime)));
				mainWrapper.setDaysOfMesurment(ReportUtil.getDayFromDayNo(dayNo));
				mainWrapper.setDateOfMesurment(ReportUtil.getFormattedDate(startDate, "dd/MM/yyyy"));

			}
		} catch (Exception e) {
			logger.error("Exception inside the method {}", Utils.getStackTrace(e));
		}
	}

	private Response createAndSaveFile(String localDirPath, Integer analyticsrepoId,
			IndoorBenchMarkReportWrapper mainWrapper, Map<String, Object> imageMap, GenericWorkorder workorderObj) {
		setFileName(localDirPath, analyticsrepoId, workorderObj, mainWrapper);

		File file = createIndoorBenchMarkReport(mainWrapper, analyticsrepoId, localDirPath, imageMap);
		if (file != null) {
			String hdfsFilePath = ConfigUtils.getString(ReportConstants.NV_REPORTS_PATH_HDFS)
					+ ReportConstants.INBUILDING + ReportConstants.FORWARD_SLASH;
			return reportService.saveFileAndUpdateStatus(analyticsrepoId, hdfsFilePath, workorderObj, file,
					file.getName(), NVWorkorderConstant.REPORT_INSTACE_ID);
		}
		return null;
	}


	private void setFileName(String filePath, Integer analyticsrepoId, GenericWorkorder workorderObj,
			IndoorBenchMarkReportWrapper mainWrapper) {
		mainWrapper.setFileName(filePath+Symbol.SLASH_FORWARD_STRING + ReportConstants.BENCHMARK + Symbol.HYPHEN + workorderObj.getWorkorderId()
				+Symbol.UNDERSCORE_STRING+analyticsrepoId+ ReportConstants.PDF_EXTENSION);

	}

	private List<IndoorBenchMarkSubWrapper> getServingDataList(Map<String, List<String[]>> operatorWiseDataMap,
			Map<String, Integer> kpiIndexMap) {
		List<IndoorBenchMarkSubWrapper> list = new ArrayList<>();
		for (Entry<String, List<String[]>> entry : operatorWiseDataMap.entrySet()) {
			IndoorBenchMarkSubWrapper wrapper = new IndoorBenchMarkSubWrapper();
			wrapper.setOperatorName(entry.getKey());
			List<String[]> dataList = entry.getValue();
			if (Utils.isValidList(dataList)) {
				setHttpDlThp(kpiIndexMap, wrapper, dataList);
				setHttpUlThp(kpiIndexMap, wrapper, dataList);
				getAvgOfIndexData(kpiIndexMap.get(ReportConstants.WEB_DOWNLOAD_DELAY_IB), dataList);
				wrapper.setBrowseTimeDelay(
						getAvgOfIndexData(kpiIndexMap.get(ReportConstants.WEB_DOWNLOAD_DELAY_IB), dataList));
				wrapper.setLatency(getAvgOfIndexData(kpiIndexMap.get(ReportConstants.LATENCY), dataList));
				setConnectionSetupTimeInfo(kpiIndexMap.get(ReportConstants.CALL_SETUP_TIME), dataList, wrapper);
				setCallInfo(kpiIndexMap, dataList, wrapper);
				list.add(wrapper);
			}
		}
		return list;

	}

	private List<IndoorBenchMarkSubWrapper> prepareCallFailDataList(Map<String, Integer> kpiIndexMap,
			Map<String, List<String[]>> operatorWiseDataMap) {
		List<IndoorBenchMarkSubWrapper> list = new ArrayList<>();
		try {
			for (Entry<String, List<String[]>> entry : operatorWiseDataMap.entrySet()) {
				Map<String, List<String[]>> causeMap = getMapOfIndexKpi(kpiIndexMap.get(ReportConstants.CALL_FAIL_CAUSE),
						entry.getValue());
				if(causeMap!=null && !causeMap.isEmpty()) {
				for (Entry<String, List<String[]>> cause : causeMap.entrySet()) {
					IndoorBenchMarkSubWrapper wrapper = new IndoorBenchMarkSubWrapper();
					wrapper.setOperatorName(entry.getKey());
					wrapper.setCallFailCause(cause.getKey());
					wrapper.setCallFail(getSumFromIndex(kpiIndexMap.get(ReportConstants.CALL_FAILURE), cause.getValue()));
					Set<String> technlogyList = getListFromIndex(cause.getValue(),
							kpiIndexMap.get(ReportConstants.NETWORK_TYPE), true);
					wrapper.setTechnologies(String.join(Symbol.SLASH_FORWARD_STRING, technlogyList));
					list.add(wrapper);

				}
			  }
			}
		} catch (Exception e) {
			logger.error("Exception inside the method prepareCallFailDataList {}",Utils.getStackTrace(e));
		}
		logger.info("call fail list {}", list);
		return list;
	}

	private List<IndoorBenchMarkSubWrapper> prepareCallDropDataList(Map<String, Integer> kpiIndexMap,
			Map<String, List<String[]>> operatorWiseDataMap) {
		List<IndoorBenchMarkSubWrapper> list = new ArrayList<>();
		try {
			for (Entry<String, List<String[]>> entry : operatorWiseDataMap.entrySet()) {
				Map<String, List<String[]>> causeMap = getMapOfIndexKpi(kpiIndexMap.get(ReportConstants.CALL_DROP_REASON),
						entry.getValue());
				for (Entry<String, List<String[]>> cause : causeMap.entrySet()) {
					IndoorBenchMarkSubWrapper wrapper = new IndoorBenchMarkSubWrapper();
					wrapper.setOperatorName(entry.getKey());
					wrapper.setCallFailCause(cause.getKey());
					wrapper.setCallFail(getSumFromIndex(kpiIndexMap.get(ReportConstants.CALL_DROP), cause.getValue()));
					Set<String> technlogyList = getListFromIndex(cause.getValue(),
							kpiIndexMap.get(ReportConstants.NETWORK_TYPE), true);
					wrapper.setTechnologies(String.join(Symbol.SLASH_FORWARD_STRING, technlogyList));
					list.add(wrapper);

				}
			}
		} catch (Exception e) {
			logger.error("Exception inside the method prepareCallFailDataList {}",Utils.getStackTrace(e));
		}
		return list;
	}

	private void setCallInfo(Map<String, Integer> kpiIndexMap, List<String[]> dataList,
			IndoorBenchMarkSubWrapper wrapper) {
		wrapper.setCallIntitate(getSumFromIndex(kpiIndexMap.get(ReportConstants.CALL_INITIATE), dataList));
		wrapper.setCallDrop(getSumFromIndex(kpiIndexMap.get(ReportConstants.CALL_DROP), dataList));
		wrapper.setCallFail(getSumFromIndex(kpiIndexMap.get(ReportConstants.CALL_FAILURE), dataList));
		wrapper.setCallSuccess(getSumFromIndex(kpiIndexMap.get(ReportConstants.CALL_SUCCESS), dataList));
		wrapper.setMosPloqaMean(ReportUtil.parseToFixedDecimalPlace(
				getAvgOfIndexData(kpiIndexMap.get(ReportConstants.MOS), dataList), ForesightConstants.INDEX_TWO));
		wrapper.setCallSucessRate(ReportUtil.parseToFixedDecimalPlace(
				ReportUtil.getPercentage(wrapper.getCallSuccess(), wrapper.getCallIntitate()),
				ForesightConstants.INDEX_TWO));
		wrapper.setCallDropRate(ReportUtil.parseToFixedDecimalPlace(
				ReportUtil.getPercentage(wrapper.getCallDrop(), wrapper.getCallIntitate()),
				ForesightConstants.INDEX_TWO));

	}

	private int getSumFromIndex(Integer index, List<String[]> dataList) {
		List<Double> data = ReportUtil.convetArrayToList(dataList, index);
		Double value = data.stream().mapToDouble(Double::valueOf).sum();
		return value != null ? value.intValue() : ForesightConstants.INDEX_ZERO;
	}

	private void setConnectionSetupTimeInfo(Integer index, List<String[]> dataList, IndoorBenchMarkSubWrapper wrapper) {
		List<Double> data = ReportUtil.convetArrayToList(dataList, index);
		OptionalDouble avg = data.stream().mapToDouble(x -> x).average();
		if (avg.isPresent()) {
			wrapper.setMeanCst(ReportUtil.parseToFixedDecimalPlace(avg.getAsDouble(), ForesightConstants.INDEX_TWO));
		}
		Optional<Double> max = data.stream().max(Comparator.naturalOrder());
		if (max.isPresent()) {
			wrapper.setMaxCst(ReportUtil.parseToFixedDecimalPlace(max.get(), ForesightConstants.INDEX_TWO));
		}
		Optional<Double> min = data.stream().min(Comparator.naturalOrder());
		if (min.isPresent()) {
			wrapper.setMinCst(ReportUtil.parseToFixedDecimalPlace(min.get(), ForesightConstants.INDEX_TWO));
		}

	}

	private Double getAvgOfIndexData(Integer index, List<String[]> dataList) {
		List<Double> data = ReportUtil.convetArrayToList(dataList, index);
		OptionalDouble avg = data.stream().mapToDouble(x -> x).average();
		if (avg.isPresent()) {
			return ReportUtil.parseToFixedDecimalPlace(avg.getAsDouble(), ForesightConstants.INDEX_TWO);
		}
		return null;
	}

	private void setHttpDlThp(Map<String, Integer> kpiIndexMap, IndoorBenchMarkSubWrapper wrapper,
			List<String[]> dataList) {
		try {
			List<String[]> filterList = ReportUtil.filterDataByTestType(dataList,
					kpiIndexMap.get(ReportConstants.TEST_TYPE), NetworkDataFormats.TEST_TYPE_HTTP_DOWNLOAD);
			List<Double> data = ReportUtil.convetArrayToList(filterList,
					kpiIndexMap.get(ReportConstants.MAC_DL_THROUGHPUT));

			List<Double> filterCriteria = data.stream().filter(x -> (x < 2.0)).collect(Collectors.toList());

			wrapper.setDlThreshold(ReportUtil.parseToFixedDecimalPlace(
					ReportUtil.getPercentage(Double.valueOf(filterCriteria.size()), Double.valueOf(data.size())),
					ForesightConstants.INDEX_TWO));

			OptionalDouble httpDlOptional = data.stream().mapToDouble(x -> x).average();

			if (httpDlOptional.isPresent()) {
				wrapper.setHttpDlThp(ReportUtil.parseToFixedDecimalPlace(httpDlOptional.getAsDouble(),
						ForesightConstants.INDEX_TWO));
			}
		} catch (Exception e) {
			logger.error("Excption inside method setHttpDlThp {}", Utils.getStackTrace(e));
		}
	}

	private void setHttpUlThp(Map<String, Integer> kpiIndexMap, IndoorBenchMarkSubWrapper wrapper,
			List<String[]> dataList) {
		try {
			List<String[]> filterList = ReportUtil.filterDataByTestType(dataList,
					kpiIndexMap.get(ReportConstants.TEST_TYPE), NetworkDataFormats.TEST_TYPE_HTTP_UPLOAD);
			List<Double> data = ReportUtil.convetArrayToList(filterList,
					kpiIndexMap.get(ReportConstants.MAC_UL_THROUGHPUT));
			List<Double> filterCriteria = data.stream().filter(x -> (x < 0.5)).collect(Collectors.toList());
			wrapper.setUlThreshold(ReportUtil.parseToFixedDecimalPlace(
					ReportUtil.getPercentage(Double.valueOf(filterCriteria.size()), Double.valueOf(data.size())),
					ForesightConstants.INDEX_TWO));
			OptionalDouble httpDlOptional = data.stream().mapToDouble(x -> x).average();
			if (httpDlOptional.isPresent()) {
				wrapper.setHttpUlThp(ReportUtil.parseToFixedDecimalPlace(httpDlOptional.getAsDouble(),
						ForesightConstants.INDEX_TWO));
			}
		} catch (Exception e) {
			logger.error("Excption inside method setHttpDlThp {}", Utils.getStackTrace(e));
		}
	}

	private File createIndoorBenchMarkReport(IndoorBenchMarkReportWrapper mainWrapper, Integer analyticsRespositoryId,
			String filePath, Map<String, Object> imageMap) {
		logger.info("Inside createBenchMarkReport : {} ", mainWrapper);
		try {
			ArrayList<IndoorBenchMarkReportWrapper> indoorReportWrapperList = new ArrayList<>();
			indoorReportWrapperList.add(mainWrapper);
			String jasperReportSourcePath = ConfigUtils.getString(ReportConstants.INDOOR_BENCHMARK_JASPER_PATH);

			imageMap.put(ReportConstants.SUBREPORT_DIR, jasperReportSourcePath);
			imageMap.put(ReportConstants.IMAGE_PARAM_HEADER_BG,
					jasperReportSourcePath + ReportConstants.IMAGE_HEADER_BG);
			imageMap.put(ReportConstants.IMAGE_PARAM_HEADER_LOG,
					jasperReportSourcePath + ReportConstants.IMAGE_HEADER_LOG_NV);
			imageMap.put(ReportConstants.IMAGE_PARAM_SCREEN_LOG,
					jasperReportSourcePath + ReportConstants.IMAGE_SCREEN_LOGO_STATIONARY);
			imageMap.put(ReportConstants.IMAGE_PARAM_SCREEN_BG,
					jasperReportSourcePath + ReportConstants.IMAGE_SCREEN_BG_JPEG);
			imageMap.put(ReportConstants.IMAGE_PARAM_THANK_YOU_BENCHMARK,
					jasperReportSourcePath + ReportConstants.THANK_YOU_JPEG);
			JRBeanCollectionDataSource rfbeanColDataSource = new JRBeanCollectionDataSource(indoorReportWrapperList);
			String finalFilePath = mainWrapper.getFileName();
			JasperRunManager.runReportToPdfFile(jasperReportSourcePath + ReportConstants.MAIN_JASPER, finalFilePath,
					imageMap, rfbeanColDataSource);
			logger.info("Report Created");
			return ReportUtil.getIfFileExists(finalFilePath);
		} catch (Exception e) {
			logger.error("Exception In createBenchMarkReport : {} ", Utils.getStackTrace(e));
		}
		return null;
	}

	private File getRouteImage(String localDirPath, String imgFloorPlanPath,
			Map<String, List<String[]>> operatorWiseDataMap, Map<String, Integer> kpiIndexMap) {
		List<String[]> dataList = null;
		String defaultOperator = ConfigUtils.getString(NVConstant.BENCHMARK_DEFAULT_OPERATOR);
		if (operatorWiseDataMap.containsKey(defaultOperator)) {
			dataList = operatorWiseDataMap.get(defaultOperator);
		} else {
			dataList = getAllDataList(operatorWiseDataMap);
		}
		dataList = ReportUtil.filterDataByTestType(dataList, kpiIndexMap.get(ReportConstants.TEST_TYPE),
				NetworkDataFormats.TEST_TYPE_HTTP_DOWNLOAD, NetworkDataFormats.TEST_TYPE_FTP_DOWNLOAD);
		if (dataList != null && !dataList.isEmpty()) {
			logger.info("imgFloorPlanPath   ===={}", imgFloorPlanPath);

			return InBuildingHeatMapGenerator.getInstance().getRouteImage(dataList, imgFloorPlanPath, localDirPath,
					kpiIndexMap);
		}
		return null;
	}

	private List<String[]> getAllDataList(Map<String, List<String[]>> operatorWiseDataMap) {
		List<String[]> dataList = new ArrayList<>();

		for (Entry<String, List<String[]>> entry : operatorWiseDataMap.entrySet()) {
			dataList.addAll(entry.getValue());
		}

		return dataList;
	}

	private String setOperatorWiseDataMaps(String localDirPath, Integer workorderId,
			Map<String, List<String[]>> operatorWiseDataMap, List<String> fetchKPIList,
			Map<String, Integer> kpiIndexMap, List<KPIWrapper> kpiList, BenchMarksubwrapper subWrapper)
			throws IOException {
		String imageFilePath = null;
		List<WORecipeMapping> recipeMappings = recipeMappingDao.getWORecipeByGWOId(workorderId);
		if (Utils.isValidList(recipeMappings)) {
			Map<String, List<WORecipeMapping>> operatorWiseRecipe = getOperatorWiseRecipeMap(recipeMappings);
			for (Entry<String, List<WORecipeMapping>> map : operatorWiseRecipe.entrySet()) {
				List<String> recipeList = transform(
						map.getValue().stream().map(WORecipeMapping::getId).collect(Collectors.toList()),
						String::valueOf);
				List<String[]> data = reportService.getDriveDataRecipeWiseTaggedForReport(Arrays.asList(workorderId),
						recipeList, fetchKPIList, kpiIndexMap);
				convertCSTInToSecond(data, kpiIndexMap.get(ReportConstants.CALL_SETUP_TIME));
				operatorWiseDataMap.put(map.getKey(), data);
				DriveDataWrapper driveDataWrapper = nvInBuildingReportService.getDataWrapperForRecipeId(workorderId,
						map.getValue().get(ReportConstants.INDEX_ZER0).getId());
				logger.info("kpiIndexMap {}", new Gson().toJson(kpiIndexMap));
				if (driveDataWrapper.getJson() != null) {
					imageFilePath = setKpIWiseImageData(localDirPath, kpiIndexMap, subWrapper, map, data,
							driveDataWrapper, kpiList);

				}

			}

			setRecipeDetails(recipeMappings, subWrapper);
		}
		return imageFilePath;
	}

	private void convertCSTInToSecond(List<String[]> data, Integer index) {
		for (String[] driveData : data) {
			try {
				if (index != null && driveData != null && driveData.length > index
						&& !StringUtils.isBlank(driveData[index]) && NumberUtils.isParsable(driveData[index])) {

					driveData[index] = String.valueOf(Double.parseDouble(driveData[index]) / 1000);
				}
			} catch (Exception e) {
				logger.error("Exception inside the method convertCSTInToSecond {}", Utils.getStackTrace(e));
			}

		}
	}

	private String setKpIWiseImageData(String localDirPath, Map<String, Integer> kpiIndexMap,
			BenchMarksubwrapper subWrapper, Entry<String, List<WORecipeMapping>> map, List<String[]> data,
			DriveDataWrapper driveDataWrapper, List<KPIWrapper> kpiList) throws IOException {
		String tempFilePathLTE = localDirPath + Symbol.SLASH_FORWARD_STRING + map.getKey() + Symbol.UNDERSCORE + "LTE";
		String tempFilePathVOLTE = localDirPath + Symbol.SLASH_FORWARD_STRING + map.getKey() + Symbol.UNDERSCORE
				+ "VOLTE";
		ReportUtil.createDirectory(tempFilePathLTE);
		ReportUtil.createDirectory(tempFilePathVOLTE);
		String imgFloorPlanPath = nvInBuildingReportService.getFloorplanImg(
				map.getValue().get(ReportConstants.INDEX_ZER0).getId(), map.getKey(), driveDataWrapper,
				driveDataWrapper.getJson(), tempFilePathLTE);
		InbuildingPointWrapper pointWrapper = IBReportUtils.getInstance().drawFloorPlan(imgFloorPlanPath,
				driveDataWrapper.getJson(), data,
				kpiIndexMap.get(ReportConstants.X_POINT), kpiIndexMap.get(ReportConstants.Y_POINT));
		List<String[]> lteDataList = ReportUtil.filterDataByTestType(pointWrapper.getArlist(),
				kpiIndexMap.get(ReportConstants.TEST_TYPE), NetworkDataFormats.TEST_TYPE_HTTP_DOWNLOAD,
				NetworkDataFormats.TEST_TYPE_FTP_DOWNLOAD);

		List<String[]> volteCallDataList = ReportUtil.filterDataByTestType(pointWrapper.getArlist(),
				kpiIndexMap.get(ReportConstants.TEST_TYPE), NetworkDataFormats.SHORT_CALL_TEST,
				NetworkDataFormats.LONG_CALL_TEST);
		for (KPIWrapper kpiWrapper : kpiList) {
			if (ReportConstants.RSRP.equalsIgnoreCase(kpiWrapper.getKpiName())) {
				BenchMarkOperatorInfo wrapper = getOPeratorImgInfo(kpiIndexMap, tempFilePathLTE, map.getKey(),
						lteDataList, kpiWrapper, imgFloorPlanPath);
				subWrapper.setRsrpImgList(addWrapperToList(subWrapper.getRsrpImgList(), wrapper));
				BenchMarkOperatorInfo volteData = getOPeratorImgInfo(kpiIndexMap, tempFilePathVOLTE, map.getKey(),
						volteCallDataList, kpiWrapper, imgFloorPlanPath);
				subWrapper.setVoltersrpImgList(addWrapperToList(subWrapper.getVoltersrpImgList(), volteData));
				if (map.getKey().equalsIgnoreCase(ConfigUtils.getString(NVConstant.BENCHMARK_DEFAULT_OPERATOR))) {

					subWrapper.setBandWisersrpImgList(getBandWiseImageList(kpiIndexMap, map, tempFilePathLTE,
							imgFloorPlanPath, lteDataList, kpiWrapper));
				}
			} else if (ReportConstants.SINR.equalsIgnoreCase(kpiWrapper.getKpiName())) {
				BenchMarkOperatorInfo wrapper = getOPeratorImgInfo(kpiIndexMap, tempFilePathLTE, map.getKey(),
						lteDataList, kpiWrapper, imgFloorPlanPath);
				subWrapper.setSinrImgList(addWrapperToList(subWrapper.getSinrImgList(), wrapper));
				BenchMarkOperatorInfo volteData = getOPeratorImgInfo(kpiIndexMap, tempFilePathVOLTE, map.getKey(),
						volteCallDataList, kpiWrapper, imgFloorPlanPath);
				subWrapper.setVoltesinrImgList(addWrapperToList(subWrapper.getVoltesinrImgList(), volteData));
				if (map.getKey().equalsIgnoreCase(ConfigUtils.getString(NVConstant.BENCHMARK_DEFAULT_OPERATOR))) {
					subWrapper.setBandWisesinrImgList(getBandWiseImageList(kpiIndexMap, map, tempFilePathLTE,
							imgFloorPlanPath, lteDataList, kpiWrapper));
				}

			} else if (ReportConstants.RSRQ.equalsIgnoreCase(kpiWrapper.getKpiName())) {
				BenchMarkOperatorInfo wrapper = getOPeratorImgInfo(kpiIndexMap, tempFilePathLTE, map.getKey(),
						lteDataList, kpiWrapper, imgFloorPlanPath);
				subWrapper.setRsrqImgList(addWrapperToList(subWrapper.getRsrqImgList(), wrapper));
				BenchMarkOperatorInfo volteData = getOPeratorImgInfo(kpiIndexMap, tempFilePathVOLTE, map.getKey(),
						volteCallDataList, kpiWrapper, imgFloorPlanPath);
				subWrapper.setVoltersrqImgList(addWrapperToList(subWrapper.getVoltersrqImgList(), volteData));
			}

			else if (ReportConstants.MAC_UL_THROUGHPUT.equalsIgnoreCase(kpiWrapper.getKpiName())) {
				List<String[]> ulDataList = ReportUtil.filterDataByTestType(pointWrapper.getArlist(),
						kpiIndexMap.get(ReportConstants.TEST_TYPE), NetworkDataFormats.TEST_TYPE_FTP_UPLOAD,
						NetworkDataFormats.TEST_TYPE_HTTP_UPLOAD);
				BenchMarkOperatorInfo wrapper = getOPeratorImgInfo(kpiIndexMap, tempFilePathLTE, map.getKey(),
						ulDataList, kpiWrapper, imgFloorPlanPath);
				subWrapper.setUlImgList(addWrapperToList(subWrapper.getUlImgList(), wrapper));

			} else if (ReportConstants.MAC_DL_THROUGHPUT.equalsIgnoreCase(kpiWrapper.getKpiName())) {
				BenchMarkOperatorInfo wrapper = getOPeratorImgInfo(kpiIndexMap, tempFilePathLTE, map.getKey(),
						lteDataList, kpiWrapper, imgFloorPlanPath);
				subWrapper.setDlImgList(addWrapperToList(subWrapper.getDlImgList(), wrapper));

			}

			else if (ReportConstants.MOS.equalsIgnoreCase(kpiWrapper.getKpiName())
					&& map.getKey().equalsIgnoreCase(ConfigUtils.getString(NVConstant.BENCHMARK_DEFAULT_OPERATOR))) {

				BenchMarkOperatorInfo wrapper = getOPeratorImgInfo(kpiIndexMap, tempFilePathVOLTE, map.getKey(),
						volteCallDataList, kpiWrapper, imgFloorPlanPath);
				wrapper.setOperatorName(wrapper.getOperatorName()+ Symbol.HYPHEN_STRING+DriveHeaderConstants.MOS);
				subWrapper.setMosImgList(addWrapperToList(subWrapper.getMosImgList(), wrapper));
				
				List<String[]> filterList = volteCallDataList.stream()
						.filter(x -> x != null && (x.length > kpiIndexMap.get(ReportConstants.MOS))
								&& !x[kpiIndexMap.get(ReportConstants.MOS)].isEmpty()
								&& (Double.valueOf(x[kpiIndexMap.get(ReportConstants.MOS)]) < 2.0))
						.collect(Collectors.toList());
				BenchMarkOperatorInfo mosData = getOPeratorImgInfo(kpiIndexMap, tempFilePathLTE, map.getKey(),
						filterList, kpiWrapper, imgFloorPlanPath);
				mosData.setOperatorName(mosData.getOperatorName()+ Symbol.HYPHEN_STRING+"MOS Less than 2");

				subWrapper.setMosImgList(addWrapperToList(subWrapper.getMosImgList(), mosData));
			}

		}
		if (map.getKey().equalsIgnoreCase(ConfigUtils.getString(NVConstant.BENCHMARK_DEFAULT_OPERATOR))) {
			setCallImageInfo(kpiIndexMap, subWrapper, map, tempFilePathVOLTE, imgFloorPlanPath, volteCallDataList);
		}
		return imgFloorPlanPath;

	}

	private void setCallImageInfo(Map<String, Integer> kpiIndexMap, BenchMarksubwrapper subWrapper,
			Entry<String, List<WORecipeMapping>> map, String tempFilePathVOLTE, String imgFloorPlanPath,
			List<String[]> volteCallDataList) {
		try {
			if (kpiIndexMap.containsKey(ReportConstants.CALL_DROP)) {
				Map<String, String> heatMaps = new HashMap<>();
				heatMaps = InBuildingHeatMapGenerator.getInstance().generateHeatMapsForCallData(volteCallDataList,
						tempFilePathVOLTE, imgFloorPlanPath, ReportConstants.CALL_DROP, heatMaps, null,
						kpiIndexMap.get(ReportConstants.CALL_DROP), null, null,
						kpiIndexMap.get(ReportConstants.X_POINT), kpiIndexMap.get(ReportConstants.Y_POINT));
				BenchMarkOperatorInfo dropCallData = new BenchMarkOperatorInfo();
				dropCallData.setOperatorName(map.getKey());
				dropCallData.setKpiName("Call Drop");
				dropCallData.setKpiImg(heatMaps.get(ReportConstants.CALL_DROP));
				subWrapper.setCallDropImgList(addWrapperToList(subWrapper.getCallDropImgList(), dropCallData));
			}
			if (kpiIndexMap.containsKey(ReportConstants.CALL_FAILURE)) {
				Map<String, String> heatMaps = new HashMap<>();
				heatMaps = InBuildingHeatMapGenerator.getInstance().generateHeatMapsForCallData(volteCallDataList,
						tempFilePathVOLTE, imgFloorPlanPath, ReportConstants.CALL_FAILURE, heatMaps, null,
						kpiIndexMap.get(ReportConstants.CALL_FAILURE), null, null,
						kpiIndexMap.get(ReportConstants.X_POINT), kpiIndexMap.get(ReportConstants.Y_POINT));
				BenchMarkOperatorInfo dropCallData = new BenchMarkOperatorInfo();
				dropCallData.setOperatorName(map.getKey());
				dropCallData.setKpiImg(heatMaps.get(ReportConstants.CALL_FAILURE));
				dropCallData.setKpiName("Block Call");
				subWrapper.setCallFailImgList(addWrapperToList(subWrapper.getCallFailImgList(), dropCallData));
			}
		} catch (Exception e) {
			logger.error("Exception inside the method setCallImageInfo {}", e.getMessage());
		}
	}

	private List<BenchMarkOperatorInfo> getBandWiseImageList(Map<String, Integer> kpiIndexMap,
			Entry<String, List<WORecipeMapping>> map, String tempFilePathLTE, String imgFloorPlanPath,
			List<String[]> lteDataList, KPIWrapper kpiWrapper) {
		List<BenchMarkOperatorInfo> list = new ArrayList<>();

		try {
			Map<String, List<String[]>> bandWiseDataMap = getMapOfIndexKpi(kpiIndexMap.get(ReportConstants.BAND),
					lteDataList);

			for (Entry<String, List<String[]>> entry : bandWiseDataMap.entrySet()) {
				String directory = tempFilePathLTE + Symbol.SLASH_FORWARD_STRING + entry.getKey();
				ReportUtil.createDirectory(directory);
				BenchMarkOperatorInfo bandWrapper = getOPeratorImgInfo(kpiIndexMap, directory, map.getKey(),
						entry.getValue(), kpiWrapper, imgFloorPlanPath);
				if (NumberUtils.isParsable(entry.getKey())) {
					Integer band = NVLayer3Utils.getFrequencyFromBand(Integer.valueOf(entry.getKey()));
					String technology = ReportUtil.getTechnologyForBand(entry.getKey());
					bandWrapper.setKpiName(bandWrapper.getOperatorName() + Symbol.SPACE_STRING
							+ Symbol.PARENTHESIS_OPEN_STRING + Symbol.SPACE_STRING + technology
							+ Symbol.UNDERSCORE_STRING + band + Symbol.PARENTHESIS_CLOSE_STRING);

				} else {
					bandWrapper.setKpiName(
							bandWrapper.getOperatorName() + Symbol.SPACE_STRING + Symbol.PARENTHESIS_OPEN_STRING
									+ Symbol.SPACE_STRING + entry.getKey() + Symbol.PARENTHESIS_CLOSE_STRING);
				}
				bandWrapper.setOperatorName(bandWrapper.getKpiName());

				list.add(bandWrapper);

			}
			return list;
		} catch (Exception e) {
			logger.error("Exception inside the method getBandWiseImageList {}", Utils.getStackTrace(e));
		}
		return list;

	}

	private Map<String, List<String[]>> getMapOfIndexKpi(Integer index, List<String[]> lteDataList) {
		Map<String, List<String[]>> map = new HashMap<>();
		if (Utils.isValidList(lteDataList)) {
			List<String[]> filterDataList = lteDataList.stream()
					.filter(x -> x != null && x.length > index && !x[index].isEmpty()).collect(Collectors.toList());
			map = filterDataList.stream().collect(Collectors.groupingBy(x -> x[index]));
		}
		return map;
	}

	private List<KPIWrapper> getKPIList(Map<String, Integer> kpiIndexMap) {
		List<LegendWrapper> legendList = legendRangeDao.findAllLegendRangesAppliedTo(ReportConstants.SSVT_REPORT);
		List<KPIWrapper> kpiList = ReportUtil.convertLegendsListToKpiWrapperList(legendList, kpiIndexMap);
		kpiList = reportService.modifyIndexOfCustomKpisForReport(kpiList, kpiIndexMap);
		return kpiList;
	}

	private List<IndoorBenchMarkSubWrapper> getMeasurementDataList(Map<String, List<String[]>> operatorWiseDataMap,
			Map<String, Integer> kpiIndexMap) {
		List<IndoorBenchMarkSubWrapper> list = new ArrayList<>();
		for (Entry<String, List<String[]>> entry : operatorWiseDataMap.entrySet()) {
			try {
				IndoorBenchMarkSubWrapper wrapper = new IndoorBenchMarkSubWrapper();
				wrapper.setOperatorName(entry.getKey());
				List<String[]> dataList = entry.getValue();
				Set<String> bandList = getListFromIndex(dataList, kpiIndexMap.get(ReportConstants.BAND), false);
				wrapper.setBand(String.join(Symbol.SLASH_FORWARD_STRING, bandList));
				List<String> frequencyList = getFrequencyListFromband(bandList);
				wrapper.setFrequency(String.join(Symbol.SLASH_FORWARD_STRING, frequencyList));
				Set<String> cgiList = getListFromIndex(dataList, kpiIndexMap.get(DriveHeaderConstants.CGI), false);
				wrapper.setCellIds(String.join(Symbol.SLASH_FORWARD_STRING, cgiList));
				Set<String> technlogyList = getListFromIndex(dataList, kpiIndexMap.get(ReportConstants.NETWORK_TYPE),
						true);

				wrapper.setTechnologies(String.join(Symbol.SLASH_FORWARD_STRING, technlogyList));
				logger.info("technlogyList === {}", technlogyList);

				Set<String> bandWidth = getListFromIndex(dataList, kpiIndexMap.get(ReportConstants.DL_BANWIDTH), false);
				logger.info("frequencyList === {}", bandWidth);
				wrapper.setBandwidth(String.join(Symbol.SLASH_FORWARD_STRING, bandWidth));

				list.add(wrapper);
			} catch (Exception e) {
				logger.error("Error inside the  getMeasurementDataList {}", e.getMessage());
			}
		}
		logger.info("mesurmentList ========{}", new Gson().toJson(list));

		return list;

	}

	private List<String> getFrequencyListFromband(Set<String> bandList) {
		List<String> frequencyList = new ArrayList<>();
		for (String band : bandList) {
			if (band != null && !band.isEmpty() && NumberUtils.isParsable(band)) {
				frequencyList.add(String.valueOf(NVLayer3Utils.getFrequencyFromBand(Integer.valueOf(band))));
			}

		}
		return frequencyList;
	}

	private Set<String> getListFromIndex(List<String[]> dataList, Integer index, boolean checkValue) {
		HashSet<String> set = new HashSet();
		if (index != null) {
			for (String[] arr : dataList) {
				if (arr != null && arr.length > index && arr[index] != null && !arr[index].isEmpty()) {
					if (checkValue && arr[index].equalsIgnoreCase(NetworkDataFormats.NONE)) {
						set.add("NO Service");

					} else {
						set.add(arr[index]);

					}
				}
			}
		}
		return set;
	}

	private List<String> getDynmicKpiList(Integer workorderId) throws IllegalAccessException {
		Set<String> dynamicKpis = reportService.getDynamicKpiName(Arrays.asList(workorderId), null,
				Layer3PPEConstant.ADVANCE);
		return ReportIndexWrapper.getLiveDriveKPIs().stream().filter(k -> dynamicKpis.contains(k))
				.collect(Collectors.toList());
	}

	private Map<String, List<WORecipeMapping>> getOperatorWiseRecipeMap(List<WORecipeMapping> recipeMappings) {
		recipeMappings = recipeMappings.stream().filter(mapping -> !mapping.getStatus().equals(Status.NOT_STARTED))
				.collect(Collectors.toList());
		return recipeMappings.stream().collect(Collectors.groupingBy(WORecipeMapping::getOperator));
	}

	public static <T, U> List<U> transform(List<T> list, Function<T, U> function) {
		return list.stream().map(function).collect(Collectors.toList());
	}

	private void prepareGraphData(Map<String, Integer> kpiIndexMap, Map<String, List<String[]>> operatorWiseDataMap,
			List<KPIWrapper> kpiList, BenchMarksubwrapper subWrapper) {
		for (KPIWrapper kpiWrapper : kpiList) {
			if (ReportConstants.RSRP.equalsIgnoreCase(kpiWrapper.getKpiName())) {
				subWrapper.setRsrpGraphList(getGraphDataForKpI(kpiIndexMap, operatorWiseDataMap, kpiWrapper, true,
						NetworkDataFormats.TEST_TYPE_HTTP_DOWNLOAD, NetworkDataFormats.TEST_TYPE_FTP_DOWNLOAD));
			}
			if (ReportConstants.RSRQ.equalsIgnoreCase(kpiWrapper.getKpiName())) {
				subWrapper.setRsrqGraphList(getGraphDataForKpI(kpiIndexMap, operatorWiseDataMap, kpiWrapper, true,
						NetworkDataFormats.TEST_TYPE_HTTP_DOWNLOAD, NetworkDataFormats.TEST_TYPE_FTP_DOWNLOAD));
			}
			if (ReportConstants.SINR.equalsIgnoreCase(kpiWrapper.getKpiName())) {
				subWrapper.setSinrGraphList(getGraphDataForKpI(kpiIndexMap, operatorWiseDataMap, kpiWrapper, true,
						NetworkDataFormats.TEST_TYPE_HTTP_DOWNLOAD, NetworkDataFormats.TEST_TYPE_FTP_DOWNLOAD));
			}
			if (ReportConstants.MAC_DL_THROUGHPUT.equalsIgnoreCase(kpiWrapper.getKpiName())) {
				subWrapper.setDlGraphList(getGraphDataForKpI(kpiIndexMap, operatorWiseDataMap, kpiWrapper, true,
						NetworkDataFormats.TEST_TYPE_HTTP_DOWNLOAD, NetworkDataFormats.TEST_TYPE_FTP_DOWNLOAD));
			}
			if (ReportConstants.MAC_UL_THROUGHPUT.equalsIgnoreCase(kpiWrapper.getKpiName())) {
				subWrapper.setUlGraphList(getGraphDataForKpI(kpiIndexMap, operatorWiseDataMap, kpiWrapper, true,
						NetworkDataFormats.TEST_TYPE_HTTP_UPLOAD, NetworkDataFormats.TEST_TYPE_FTP_UPLOAD));
			}
			if (ReportConstants.MOS.equalsIgnoreCase(kpiWrapper.getKpiName())) {
				subWrapper.setMosGraphList(getGraphDataForKpI(kpiIndexMap, operatorWiseDataMap, kpiWrapper, true,
						NetworkDataFormats.SHORT_CALL_TEST, NetworkDataFormats.LONG_CALL_TEST));
			}

			if (ReportConstants.CALL_SETUP_TIME.equalsIgnoreCase(kpiWrapper.getKpiName())) {
				subWrapper.setCstGraphList(getGraphDataForKpI(kpiIndexMap, operatorWiseDataMap, kpiWrapper, false));
			}

		}
		subWrapper.setDataTechnolgyGraphList(getTechnlogyGraphData(operatorWiseDataMap, kpiIndexMap,
				NetworkDataFormats.TEST_TYPE_HTTP_DOWNLOAD, NetworkDataFormats.TEST_TYPE_FTP_DOWNLOAD));
		subWrapper.setVoiceTechnolgyGraphList(getTechnlogyGraphData(operatorWiseDataMap, kpiIndexMap,
				NetworkDataFormats.SHORT_CALL_TEST, NetworkDataFormats.LONG_CALL_TEST));
		subWrapper.setTechVsDlGraphList(getTechnlogyVsSpeedGraphData(operatorWiseDataMap, kpiIndexMap));
		subWrapper.setBandVsDlGraphList(getBandVsSpeedGraphData(operatorWiseDataMap, kpiIndexMap));
		subWrapper.setFrequencyBandGraphList(getFrequencyBandGraphData(operatorWiseDataMap, kpiIndexMap));
	}

	private List<IBBenchmarkGraphDataWrapper> getTechnlogyVsSpeedGraphData(
			Map<String, List<String[]>> operatorWiseDataMap, Map<String, Integer> kpiIndexMap) {
		List<IBBenchmarkGraphDataWrapper> list = new ArrayList<>();
		try {
			for (Entry<String, List<String[]>> entry : operatorWiseDataMap.entrySet()) {
				List<String[]> dataList = entry.getValue();
				dataList = ReportUtil.filterDataByTestType(dataList, kpiIndexMap.get(ReportConstants.TEST_TYPE),
						NetworkDataFormats.TEST_TYPE_FTP_DOWNLOAD, NetworkDataFormats.TEST_TYPE_HTTP_DOWNLOAD);
				if (Utils.isValidList(dataList)) {
					List<String[]> filterDataList = dataList.stream()
							.filter(x -> x != null && x.length > kpiIndexMap.get(ReportConstants.NETWORK_TYPE)
									&& !x[kpiIndexMap.get(ReportConstants.NETWORK_TYPE)].isEmpty())
							.collect(Collectors.toList());
					Map<String, List<String[]>> technologyMap = filterDataList.stream()
							.collect(Collectors.groupingBy(x -> x[kpiIndexMap.get(ReportConstants.NETWORK_TYPE)]));
					technologyMap.remove(NetworkDataFormats.NETWORK_TYPE_NONE);
					for (Entry<String, List<String[]>> map : technologyMap.entrySet()) {
						IBBenchmarkGraphDataWrapper wrapper = new IBBenchmarkGraphDataWrapper();
						wrapper.setKpiName(map.getKey());
						wrapper.setOperator1(entry.getKey());
						wrapper.setOperator1PDF(
								getAvgOfIndexData(kpiIndexMap.get(ReportConstants.MAC_DL_THROUGHPUT), map.getValue()));
						list.add(wrapper);
					}

				}
			}
		} catch (Exception e) {
			logger.error("exception inside the method  getTechnlogyVsSpeedGraphData=== {}", Utils.getStackTrace(e));
		}
		return list;

	}

	private List<IBBenchmarkGraphDataWrapper> getBandVsSpeedGraphData(Map<String, List<String[]>> operatorWiseDataMap,
			Map<String, Integer> kpiIndexMap) {
		List<IBBenchmarkGraphDataWrapper> list = new ArrayList<>();
		try {
			for (Entry<String, List<String[]>> entry : operatorWiseDataMap.entrySet()) {
				List<String[]> dataList = entry.getValue();
				dataList = ReportUtil.filterDataByTestType(dataList, kpiIndexMap.get(ReportConstants.TEST_TYPE),
						NetworkDataFormats.TEST_TYPE_FTP_DOWNLOAD, NetworkDataFormats.TEST_TYPE_HTTP_DOWNLOAD);
				if (Utils.isValidList(dataList)) {
					List<String[]> filterDataList = dataList.stream()
							.filter(x -> x != null && x.length > kpiIndexMap.get(ReportConstants.BAND)
									&& !x[kpiIndexMap.get(ReportConstants.BAND)].isEmpty())
							.collect(Collectors.toList());
					Map<String, List<String[]>> technologyMap = filterDataList.stream()
							.collect(Collectors.groupingBy(x -> x[kpiIndexMap.get(ReportConstants.BAND)]));
					for (Entry<String, List<String[]>> map : technologyMap.entrySet()) {
						IBBenchmarkGraphDataWrapper wrapper = new IBBenchmarkGraphDataWrapper();
						wrapper.setKpiName(NVLayer3Utils.getFrequencyFromBand(Integer.valueOf(map.getKey()))
								+ Symbol.SPACE_STRING + "MHz");
						wrapper.setOperator1(entry.getKey());
						wrapper.setOperator1PDF(
								getAvgOfIndexData(kpiIndexMap.get(ReportConstants.MAC_DL_THROUGHPUT), map.getValue()));
						list.add(wrapper);
					}

				}
			}
		} catch (Exception e) {
			logger.error("exception inside the method  getBandVsSpeedGraphData{}", Utils.getStackTrace(e));
		}
		return list;

	}

	private List<IBBenchmarkGraphDataWrapper> getFrequencyBandGraphData(Map<String, List<String[]>> operatorWiseDataMap,
			Map<String, Integer> kpiIndexMap) {
		List<IBBenchmarkGraphDataWrapper> list = new ArrayList<>();
		try {
			for (Entry<String, List<String[]>> entry : operatorWiseDataMap.entrySet()) {
				List<String[]> dataList = entry.getValue();
				dataList = ReportUtil.filterDataByTestType(dataList, kpiIndexMap.get(ReportConstants.TEST_TYPE),
						NetworkDataFormats.TEST_TYPE_FTP_DOWNLOAD, NetworkDataFormats.TEST_TYPE_HTTP_DOWNLOAD);
				if (Utils.isValidList(dataList)) {
					List<String[]> filterDataList = dataList.stream()
							.filter(x -> x != null && x.length > kpiIndexMap.get(ReportConstants.BAND)
									&& !x[kpiIndexMap.get(ReportConstants.BAND)].isEmpty())
							.collect(Collectors.toList());
					Map<String, List<String[]>> technologyMap = filterDataList.stream()
							.collect(Collectors.groupingBy(x -> x[kpiIndexMap.get(ReportConstants.BAND)]));
					for (Entry<String, List<String[]>> map : technologyMap.entrySet()) {
						IBBenchmarkGraphDataWrapper wrapper = new IBBenchmarkGraphDataWrapper();
						wrapper.setKpiName(NVLayer3Utils.getFrequencyFromBand(Integer.valueOf(map.getKey()))
								+ Symbol.SPACE_STRING + "MHz");
						wrapper.setOperator1(entry.getKey());
						wrapper.setOperator1PDF(Utils.getPercentage(map.getValue().size(), filterDataList.size()));
						list.add(wrapper);
					}

				}
			}
		} catch (Exception e) {
			logger.error("exception inside the method  getTechnlogyGraphData{}", Utils.getStackTrace(e));
		}
		return list;

	}

	private List<IBBenchmarkGraphDataWrapper> getTechnlogyGraphData(Map<String, List<String[]>> operatorWiseDataMap,
			Map<String, Integer> kpiIndexMap, String... testTypes) {
		List<IBBenchmarkGraphDataWrapper> list = new ArrayList<>();
		try {
			for (Entry<String, List<String[]>> entry : operatorWiseDataMap.entrySet()) {
				List<String[]> dataList = entry.getValue();
				dataList = ReportUtil.filterDataByTestType(dataList, kpiIndexMap.get(ReportConstants.TEST_TYPE),
						testTypes);
				if (Utils.isValidList(dataList)) {
					List<String[]> filterDataList = dataList.stream()
							.filter(x -> x != null && x.length > kpiIndexMap.get(ReportConstants.NETWORK_TYPE)
									&& !x[kpiIndexMap.get(ReportConstants.NETWORK_TYPE)].isEmpty())
							.collect(Collectors.toList());
					Map<String, List<String[]>> technologyMap = filterDataList.stream()
							.collect(Collectors.groupingBy(x -> x[kpiIndexMap.get(ReportConstants.NETWORK_TYPE)]));
					for (Entry<String, List<String[]>> map : technologyMap.entrySet()) {
						IBBenchmarkGraphDataWrapper wrapper = new IBBenchmarkGraphDataWrapper();
						if (map.getKey().equalsIgnoreCase(NetworkDataFormats.NETWORK_TYPE_NONE)) {
							wrapper.setKpiName("NO Service");
						} else {
							wrapper.setKpiName(map.getKey());

						}
						wrapper.setOperator1(entry.getKey());
						wrapper.setOperator1PDF(ReportUtil.parseToFixedDecimalPlace(
								ReportUtil.getPercentage(map.getValue().size(), filterDataList.size()),
								ForesightConstants.INDEX_TWO));

						list.add(wrapper);
					}

				}
			}
			list = list.stream()
					.sorted(Comparator.comparing(IBBenchmarkGraphDataWrapper::getKpiName, Comparator.naturalOrder()))
					.collect(Collectors.toList());
		} catch (Exception e) {
			logger.error("exception inside the method  getTechnlogyGraphData{}", Utils.getStackTrace(e));
		}

		return list;

	}

	private List<IBBenchmarkGraphDataWrapper> getGraphDataForKpI(Map<String, Integer> kpiIndexMap,
			Map<String, List<String[]>> operatorWiseDataMap, KPIWrapper kpiWrapper, boolean isToApplyFilter,
			String... testType) {
		List<IBBenchmarkGraphDataWrapper> kpiGraphList = new ArrayList<>();
		List<RangeSlab> rangeList = kpiWrapper.getRangeSlabs();
		rangeList.sort(Comparator.comparing(RangeSlab::getLowerLimit));
		for (RangeSlab rangeSlab : rangeList) {
			try {
				IBBenchmarkGraphDataWrapper graphData = new IBBenchmarkGraphDataWrapper();
				int i = ForesightConstants.INDEX_ONE;
				for (Entry<String, List<String[]>> entry : operatorWiseDataMap.entrySet()) {
					List<String[]> dataList = entry.getValue();
					if (isToApplyFilter) {
						dataList = ReportUtil.filterDataByTestType(dataList, kpiIndexMap.get(ReportConstants.TEST_TYPE),
								testType);
					}
					List<Double> data = ReportUtil.convetArrayToList(dataList,
							kpiIndexMap.get(kpiWrapper.getKpiName()));
					if (Utils.isValidList(data)) {
						kpiGraphList.add(getGraphDataWrapper(rangeSlab, graphData, i, entry, data, kpiWrapper));
					}
					i = i + ForesightConstants.INDEX_ONE;
				}

			} catch (Exception e) {
				logger.error("Exception during calculatio of graph data ", Utils.getStackTrace(e));
			}
		}

		return kpiGraphList;
	}

	private static IBBenchmarkGraphDataWrapper getGraphDataWrapper(RangeSlab rangeSlab,
			IBBenchmarkGraphDataWrapper graphData, int i, Entry<String, List<String[]>> entry, List<Double> rsrpList,
			KPIWrapper kpiWrapper) {
		List<Double> filterList = rsrpList.stream()
				.filter(val -> (rangeSlab.getUpperLimit() != null
						? (val >= rangeSlab.getLowerLimit() && val <= rangeSlab.getUpperLimit())
						: val >= rangeSlab.getLowerLimit()))
				.collect(Collectors.toList());
		Double parcent = Utils.getPercentage(filterList.size(), rsrpList.size());
		graphData.setCount(filterList.size());
		if (i == ForesightConstants.INDEX_ONE) {
			graphData.setOperator1PDF(ReportUtil.parseToFixedDecimalPlace(parcent, ReportConstants.INDEX_TWO));
			graphData.setOperator1CDF(Double.valueOf(filterList.size()));
			graphData.setOperator1(entry.getKey());
		}
		if (i == ForesightConstants.INDEX_TWO) {
			graphData.setOperator2PDF(ReportUtil.parseToFixedDecimalPlace(parcent, ReportConstants.INDEX_TWO));
			graphData.setOperator2CDF(Double.valueOf(filterList.size()));
			graphData.setOperator2(entry.getKey());
		}
		if (i == ForesightConstants.INDEX_THREE) {
			graphData.setOperator3PDF(ReportUtil.parseToFixedDecimalPlace(parcent, ReportConstants.INDEX_TWO));
			graphData.setOperator3CDF(Double.valueOf(filterList.size()));
			graphData.setOperator3(entry.getKey());
		}
		if (i == ForesightConstants.INDEX_FOUR) {
			graphData.setOperator4PDF(ReportUtil.parseToFixedDecimalPlace(parcent, ReportConstants.INDEX_TWO));
			graphData.setOperator4CDF(Double.valueOf(filterList.size()));
			graphData.setOperator4(entry.getKey());
		}
		graphData.setFrom(rangeSlab.getLowerLimit());
		if (rangeSlab.getUpperLimit() != null) {
			graphData.setTo(rangeSlab.getUpperLimit());
		}
		graphData.setKpiName(kpiWrapper.getKpiName() + Symbol.SPACE + ReportConstants.OPEN_BRACKET
				+ ReportUtil.getUnitByKPiName(kpiWrapper.getKpiName().trim()) + ReportConstants.CLOSED_BRACKET);
		return graphData;
	}

	private List<BenchMarkOperatorInfo> addWrapperToList(List<BenchMarkOperatorInfo> list,
			BenchMarkOperatorInfo wrapper) {
		if (Utils.isValidList(list)) {
			list.add(wrapper);
		} else {
			list = new ArrayList<>();
			list.add(wrapper);
		}
		return list;
	}

	private BenchMarkOperatorInfo getOPeratorImgInfo(Map<String, Integer> kpiIndexMap, String localDirPath,
			String opName, List<String[]> data, KPIWrapper kpiWrapper, String imgFloorPlan) {
		BenchMarkOperatorInfo wrapper = new BenchMarkOperatorInfo();
		Map<String, String> kpiImageMap = InBuildingHeatMapGenerator.getInstance().generateHeatMapsForKpi(data,
				localDirPath, imgFloorPlan, kpiWrapper, kpiIndexMap);
		wrapper.setOperatorName(opName);
		wrapper.setKpiImg(kpiImageMap.get(kpiWrapper.getKpiName()));
		List<KPIWrapper> legenDList = new ArrayList<>();
		legenDList.add(kpiWrapper);
		HashMap<String, BufferedImage> bufferdImageMap = reportService.getLegendImages(legenDList, data,
				kpiIndexMap.get(ReportConstants.TEST_TYPE));
		HashMap<String, String> legendImgMap = mapImageService.saveDriveImages(bufferdImageMap, localDirPath, false);
		wrapper.setKpiLegendImg(legendImgMap
				.get(ReportConstants.LEGEND + ReportConstants.UNDERSCORE + kpiIndexMap.get(kpiWrapper.getKpiName())));
		return wrapper;
	}

	private void setRecipeDetails(List<WORecipeMapping> recipeMappings, BenchMarksubwrapper subWrapper) {
		List<JSONArray> jsonDataList = getScrpitJsonData(recipeMappings);
		List<IndoorBenchMarkSubWrapper> dataList = new ArrayList<>();
		List<IndoorBenchMarkSubWrapper> voiceList = new ArrayList<>();

		for (JSONArray jsonArray : jsonDataList) {
			try {
				JSONObject object = (JSONObject) jsonArray.get(ForesightConstants.INDEX_ZERO);
				if (object.has("name") && object.get("name").toString().equalsIgnoreCase("DOWNLOAD")) {
					IndoorBenchMarkSubWrapper wrapper = new IndoorBenchMarkSubWrapper();
					wrapper.setName(object.get("protocol").toString().toUpperCase() + Symbol.SPACE_STRING + "DOWNLOAD");
					wrapper.setDetails(object.get("fileSize").toString() + Symbol.SPACE_STRING + ReportConstants.MBPS
							+ "( " + object.get("duration") + " Sec timeout )");
					dataList.add(wrapper);
				}
				if (object.has("name") && object.get("name").toString().equalsIgnoreCase("UPLOAD")) {
					IndoorBenchMarkSubWrapper wrapper = new IndoorBenchMarkSubWrapper();
					wrapper.setName(object.get("protocol").toString().toUpperCase() + Symbol.SPACE_STRING + "UPLOAD");
					wrapper.setDetails(object.get("fileSize").toString() + Symbol.SPACE_STRING + ReportConstants.MBPS
							+ "( " + object.get("duration") + " Sec timeout )");
					dataList.add(wrapper);

				}
				if (object.has("name") && object.get("name").toString().equalsIgnoreCase("PING")) {
					IndoorBenchMarkSubWrapper wrapper = new IndoorBenchMarkSubWrapper();
					wrapper.setName(object.get("name").toString().toUpperCase() + Symbol.HYPHEN_STRING
							+ object.get("protocol"));
					if (object.has("urlList")) {
						if (object.get("urlList").toString().contains(Symbol.COMMA_STRING)) {
							wrapper.setDetails(object.get("urlList").toString().split(Symbol.COMMA_STRING).length
									+ Symbol.SPACE_STRING + "live websites");
						} else {
							wrapper.setDetails(ForesightConstants.INDEX_ONE + Symbol.SPACE_STRING + "live websites");

						}
						dataList.add(wrapper);
					}

				}

				if (object.has("name") && object.get("name").toString().equalsIgnoreCase("LONG_CALL")) {
					IndoorBenchMarkSubWrapper wrapper = new IndoorBenchMarkSubWrapper();
					wrapper.setName(object.get("name").toString().toUpperCase().replace(Symbol.UNDERSCORE_STRING,
							Symbol.SPACE_STRING) + Symbol.HYPHEN_STRING + object.get("callType"));
					wrapper.setDetails(object.get("duration") + " Second ");
					voiceList.add(wrapper);
					IndoorBenchMarkSubWrapper wrapper1 = new IndoorBenchMarkSubWrapper();
					wrapper1.setName("Wait Time");
					wrapper1.setDetails(object.get("idleTime") + " Second ");
					voiceList.add(wrapper1);

				}

				if (object.has("name") && object.get("name").toString().equalsIgnoreCase("SHORT_CALL")) {
					IndoorBenchMarkSubWrapper wrapper = new IndoorBenchMarkSubWrapper();
					wrapper.setName(object.get("name").toString().toUpperCase().replace(Symbol.UNDERSCORE_STRING,
							Symbol.SPACE_STRING) + Symbol.HYPHEN_STRING + object.get("callType"));
					wrapper.setDetails(object.get("duration") + " Second ");
					voiceList.add(wrapper);
					IndoorBenchMarkSubWrapper wrapper1 = new IndoorBenchMarkSubWrapper();
					wrapper1.setName("Wait Time");
					wrapper1.setDetails(object.get("idleTime") + " Second ");
					voiceList.add(wrapper1);

				}
			} catch (JSONException e) {
				logger.error("JSONException inside the method setRecipeDetails {}", Utils.getStackTrace(e));
			}

			catch (Exception e) {
				logger.error("Exception inside the method setRecipeDetails {}", Utils.getStackTrace(e));
			}
		}
		subWrapper.setDataList(dataList);
		subWrapper.setVoiceList(voiceList);
	}

	private List<JSONArray> getScrpitJsonData(List<WORecipeMapping> recipeMappings) {
		Map<Recipe, List<WORecipeMapping>> recipeMap = recipeMappings.stream()
				.collect(Collectors.groupingBy(WORecipeMapping::getRecipe, Collectors.toList()));
		List<JSONArray> list = new ArrayList<>();
		List<Integer> recipeIds = new ArrayList<>();

		for (Entry<Recipe, List<WORecipeMapping>> map : recipeMap.entrySet()) {
			map.getKey().getScriptJson();
			JSONArray newJArray = new JSONArray(map.getKey().getScriptJson());
			if (!recipeIds.contains(map.getKey().getId())) {
				list.add(newJArray);
				recipeIds.add(map.getKey().getId());
			}

		}
		return list;
	}

}

package com.inn.foresight.module.nv.report.service.impl;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import com.inn.foresight.module.nv.report.wrapper.stealth.StealthWODetailItemWrapper;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.lang.StringUtils;
import com.inn.core.generic.exceptions.application.BusinessException;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.report.dao.IAnalyticsRepositoryDao;
import com.inn.foresight.core.report.model.AnalyticsRepository;
import com.inn.foresight.module.nv.core.workorder.dao.impl.IGenericWorkorderDao;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;
import com.inn.foresight.module.nv.layer3.constants.QMDLConstant;
import com.inn.foresight.module.nv.layer3.qmdlParser.wrappers.Layer3SummaryWrapper;
import com.inn.foresight.module.nv.layer3.qmdlParser.wrappers.NVL3CsvDataWrapper;
import com.inn.foresight.module.nv.layer3.service.INVHbaseService;
import com.inn.foresight.module.nv.layer3.service.parse.IAdhocLiveDriveReportGenerationService;
import com.inn.foresight.module.nv.layer3.service.parse.INVAdhocDriveProcessingService;
import com.inn.foresight.module.nv.layer3.utils.NVLayer3Utils;
import com.inn.foresight.module.nv.layer3.wrapper.Layer3ReportWrapper;
import com.inn.foresight.module.nv.report.service.ICustomerExperienceReportService;
import com.inn.foresight.module.nv.report.service.IReportService;
import com.inn.foresight.module.nv.report.smf.wrapper.MobilityLocationWrapper;
import com.inn.foresight.module.nv.report.smf.wrapper.SmfStationaryWidget;
import com.inn.foresight.module.nv.report.smf.wrapper.SmfWidgetWrapper;
import com.inn.foresight.module.nv.report.utils.ReportConstants;
import com.inn.foresight.module.nv.report.utils.ReportUtil;
import com.inn.foresight.module.nv.report.utils.StealthUtils;
import com.inn.foresight.module.nv.report.utils.ZipUtils;
import com.inn.foresight.module.nv.report.wrapper.GraphDataWrapper;
import com.inn.foresight.module.nv.report.wrapper.GraphWrapper;
import com.inn.foresight.module.nv.report.wrapper.RawDataWrapper;
import com.inn.foresight.module.nv.report.wrapper.ReportSubWrapper;
import com.inn.foresight.module.nv.report.wrapper.SMFReportWrapper;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;
import com.inn.foresight.module.nv.workorder.stealth.kpi.dao.IDriveTestKPIDao;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRPptxExporter;

@Service("CustomerExperienceReportServiceImpl")
public class CustomerExperienceReportServiceImpl implements ICustomerExperienceReportService {

	/** The logger. */
	private static final Logger logger = LogManager.getLogger(CustomerExperienceReportServiceImpl.class);

	/** The mapper. */
	static ObjectMapper mapper = new ObjectMapper();
	@Autowired
	private IReportService reportService;
	@Autowired
	private IAnalyticsRepositoryDao analyticsRepositoryDao;
	@Autowired
	private IGenericWorkorderDao iGenericWorkorderDao;
	@Autowired
	private INVAdhocDriveProcessingService nvAdhocDriveProcessingService;
	@Autowired
	private IAdhocLiveDriveReportGenerationService adhocDriveReportGeneration;
	@Autowired
	private INVHbaseService nvHbaseService;

	@Autowired
	private IDriveTestKPIDao driveTestKPIDao;


	@Override
	@SuppressWarnings({"unused" })
	public Response execute(String json) {
		logger.info("Going to execute method for json {} ", json);
		String filePath=ConfigUtils.getString(ReportConstants.NV_REPORTS_PATH)+ReportConstants.SAUT+ReportConstants.FORWARD_SLASH+new Date().getTime()+ReportConstants.FORWARD_SLASH;
		ReportUtil.createDirectory(filePath);
		SMFReportWrapper mainWrapper=new SMFReportWrapper();
		List<ReportSubWrapper>subList=new ArrayList<>();
		try {
			SmfWidgetWrapper wrapper = 	mapper.readValue(json, SmfWidgetWrapper.class);
			logger.info("wrapper {} ",wrapper.toString());
		    return generateCETReport(filePath, mainWrapper, subList, wrapper);
		} catch (Exception e) {
			logger.error("Unable to Genearate the report reason is {} ",e.getMessage());
			return Response.ok(ForesightConstants.FAILURE_JSON).build();
		}
	}

	private Response generateCETReport(String filePath, SMFReportWrapper mainWrapper, List<ReportSubWrapper> subList,
			SmfWidgetWrapper wrapper) throws IOException {
		if((wrapper.getMobility()!=null && wrapper.getMobility().getWorkorderIds()!=null) || wrapper.getStationary()!=null){
			List<Integer> listOfWoIds = wrapper.getMobility().getWorkorderIds()!=null?wrapper.getMobility().getWorkorderIds():new ArrayList<>();
			List<NVL3CsvDataWrapper> nvL3CsvList =new ArrayList<>();
			Layer3SummaryWrapper summary=	new Layer3SummaryWrapper();  
			if (reportService.getFileProcessedStatusForWorkorders(listOfWoIds)) {
				for (Integer workorderId : listOfWoIds){
					GenericWorkorder genericWorkorder=	setStatusOfMobilityDataAvailability(workorderId,mainWrapper);
					if(genericWorkorder!=null){
							nvL3CsvList = getMobilityDataAndGeneratecsv(filePath, wrapper, genericWorkorder,summary,nvL3CsvList);
					}
				  }
				}
				setDataToWrapper(filePath, mainWrapper, subList, wrapper, summary, nvL3CsvList);
		        String destFileName = filePath+mainWrapper.getReportName();
				File destDir = new File(filePath);
		        mainWrapper.setSubList(subList);
				proceedToCreateReport(mainWrapper,destFileName, true, getParamMapForReport(mainWrapper.isMobilityDataAvailable(), mainWrapper.isStationaryDataAvailable(), true, false));
				String zipFilePath=ConfigUtils.getString(ReportConstants.NV_REPORTS_PATH)+ReportConstants.FORWARD_SLASH+mainWrapper.getReportName()+ReportConstants.UNDERSCORE+wrapper.getAnalyticsrepositoryid_pk()+ReportConstants.DOT_ZIP;
				logger.info("zipFilePath {}",zipFilePath);
				ZipUtils.zip(destDir.listFiles(), zipFilePath);
				String hdfsFilePath = ConfigUtils.getString(ReportConstants.NV_REPORTS_PATH_HDFS) + ReportConstants.MASTER + ReportConstants.FORWARD_SLASH;
				reportService.saveFileAndUpdateStatus(wrapper.getAnalyticsrepositoryid_pk(), hdfsFilePath, null, new File(zipFilePath),mainWrapper.getReportName()+ReportConstants.DOT_ZIP,NVWorkorderConstant.REPORT_INSTACE_ID);
				return Response.ok(ForesightConstants.SUCCESS_JSON).build();
		}
		return Response.ok(ForesightConstants.FAILURE_JSON).build();
	}
	
	private GenericWorkorder setStatusOfMobilityDataAvailability(Integer workorderId, SMFReportWrapper mainWrapper) {
		GenericWorkorder genericWorkorder=null;
		if(workorderId!=null) {
			try {
				genericWorkorder = iGenericWorkorderDao.findByPk(workorderId);
				 mainWrapper.setMobilityDataAvailable(true);
			} catch (DaoException e) {
				logger.error("Unable to find the workorder data for Id {} , rsn {} ",workorderId,e.getMessage());
			}
			 return genericWorkorder;
		}
		return genericWorkorder;
	}
	
	private List<NVL3CsvDataWrapper> getMobilityDataAndGeneratecsv(String filePath, SmfWidgetWrapper wrapper,
			GenericWorkorder genericWorkorder, Layer3SummaryWrapper summary, List<NVL3CsvDataWrapper> finalNvl3CsvList) throws IOException {
		List<NVL3CsvDataWrapper> nvL3CsvList = nvAdhocDriveProcessingService
				.processAdhocFileForReport(genericWorkorder.getId(), summary);
		if(nvL3CsvList!=null){
			finalNvl3CsvList.addAll(nvL3CsvList);
			try {
				if (wrapper.getMobility() != null && genericWorkorder.getId() != null) {
					adhocDriveReportGeneration.generateCSVReportForLiveDrive(genericWorkorder.getId(),
							nvL3CsvList, filePath, genericWorkorder.getWorkorderId());
				}
			} catch (BusinessException e) {
				logger.error("Unable to write csv file for workOrderId {} , error msg {} ",genericWorkorder.getWorkorderId(),e.getMessage());
			}
		}
		logger.info("Going to return the finalNvl3CsvList of Size {} ",finalNvl3CsvList!=null?finalNvl3CsvList.size():null);
		return finalNvl3CsvList;
	}
	
	private void setDataToWrapper(String filePath, SMFReportWrapper mainWrapper, List<ReportSubWrapper> subList,
			SmfWidgetWrapper wrapper, Layer3SummaryWrapper summary,
			List<NVL3CsvDataWrapper> nvL3CsvList) {
		List<String> kpiList = Arrays.asList(ReportConstants.RSRP, ReportConstants.SINR, ReportConstants.RSRQ,
				ReportConstants.UL_THROUGHPUT/* , ReportConstants.DL_THROUGHPUT */);
		ReportSubWrapper subWrapper=  getGraphDataFromList(nvL3CsvList, kpiList);
		getEnodbWisegraphDataFromList(nvL3CsvList,subWrapper);
		subWrapper.setTestName(ReportConstants.CUSTOMER_EXPERIENCE_TEST_NAME);
		subWrapper.setLocationInfo(wrapper.getMobility()!=null?wrapper.getMobility().getFloorPlan():null);
		if(wrapper.getStationary() != null && !wrapper.getStationary().isEmpty()) {
			mainWrapper.setStationaryDataAvailable(true);
			List<RawDataWrapper>locationDataList=  getLocationWiseDataForStationry(wrapper.getStationary(),filePath,summary.getOperatorName());
			subWrapper.setLocationDataList(locationDataList);
		} 
		subWrapper.setFinalScore(getFinalScore(subWrapper.getWalktestScore(), subWrapper.getLocationDataList()));
		subList.add(subWrapper);
		mainWrapper.setReportName(getReportName(wrapper));
	}
	
	@Override
	public Layer3ReportWrapper getDeviceReportForStealth(String taskId, String workorderId, String date) {
		logger.info("inside method getDeviceReportForStealth");
		try {
			String filePath = ConfigUtils.getString(ReportConstants.NV_REPORTS_PATH) + ReportConstants.SAUT
					+ ReportConstants.FORWARD_SLASH + new Date().getTime() + ReportConstants.FORWARD_SLASH;
			ReportUtil.createDirectory(filePath);
			SimpleDateFormat sdf = new SimpleDateFormat(ReportConstants.DATE_FORMAT_STEALTH_HBASE);
			Date testDate = new Date(Long.parseLong(date));
			String formattedDate = sdf.format(testDate);
			 String rawData = nvHbaseService.getStealthDataByWoIdAndTaskId(workorderId, taskId, formattedDate, QMDLConstant.RAW_DATA, StealthUtils.getHourFromTimestamp(testDate.getTime()));
			List<String[]> dataList = ReportUtil.convertCSVStringToDataListStealth(rawData);
			logger.info("DataList: {}", new Gson().toJson(dataList));
			SMFReportWrapper mainWrapper = new SMFReportWrapper();
			mainWrapper.setReportName(ReportConstants.STEALTH_REPORT_NAME);
			List<ReportSubWrapper> subList = new ArrayList<>();
			List<NVL3CsvDataWrapper> csvDataList = getGraphDataForStealth(dataList);
			List<String> kpiList = Arrays.asList(ReportConstants.RSRP, ReportConstants.SINR, ReportConstants.RSRQ,
					ReportConstants.UL_THROUGHPUT, ReportConstants.DL_THROUGHPUT);
			ReportSubWrapper subWrapper = getGraphDataFromList(csvDataList, kpiList);
			List<StealthWODetailItemWrapper> deviceDetailList = getDeviceDetailWrapperFromHeaders(ReportUtil.getHeadersFromStealthRawData(rawData));
			subWrapper.setDeviceDetailList(deviceDetailList);
			subWrapper.setTestName(ReportConstants.STEALTH_TEST_NAME);
			subWrapper = getEnodbWisegraphDataFromList(csvDataList, subWrapper);
			subWrapper.setLocationInfo(Arrays.asList(new MobilityLocationWrapper()));
			List<RawDataWrapper> locationDataList = getStationaryDataForStealth(dataList, taskId, date);
			subWrapper.setLocationDataList(locationDataList);
			subWrapper.setFinalScore(getFinalScore(subWrapper.getWalktestScore(), subWrapper.getLocationDataList()));
			subList.add(subWrapper);
			mainWrapper.setSubList(subList);
			String destFileName = filePath + ReportConstants.STEALTH_REPORT_NAME + ReportConstants.UNDERSCORE + workorderId + ReportConstants.UNDERSCORE + taskId + ReportConstants.UNDERSCORE + date;
			File file = proceedToCreateReport(mainWrapper, destFileName, true, getParamMapForReport(true, true, true, true));
			try(FileInputStream fileInputStream = new FileInputStream(file)){
				Layer3ReportWrapper layer3Wrapper = new Layer3ReportWrapper();
				if(file!=null){layer3Wrapper.setFileName(file.getName());}
				layer3Wrapper.setFile(IOUtils.toByteArray(fileInputStream));
				return layer3Wrapper;
			}
		} catch (Exception e) {
			logger.error("Error inside method getDeviceReportForStealth {}", ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	
	private List<StealthWODetailItemWrapper> getDeviceDetailWrapperFromHeaders(String[] headers) {
		List<StealthWODetailItemWrapper> deviceDetailList = new ArrayList<>();
		deviceDetailList.add(getDeviceDetailWrapper(ReportConstants.KEY_IMEI, headers[ReportConstants.STEALTH_INDEX_HEADER_IMEI]));
		deviceDetailList.add(getDeviceDetailWrapper(ReportConstants.KEY_IMSI, headers[ReportConstants.STEALTH_INDEX_HEADER_IMSI]));
		deviceDetailList.add(getDeviceDetailWrapper(ReportConstants.KEY_MAKE, headers[ReportConstants.STEALTH_INDEX_HEADER_MAKE]));
		deviceDetailList.add(getDeviceDetailWrapper(ReportConstants.KEY_MODEL, headers[ReportConstants.STEALTH_INDEX_HEADER_MODEL]));
		return deviceDetailList;
	}
	
	private StealthWODetailItemWrapper getDeviceDetailWrapper(String itemLabel, String itemValue) {
		StealthWODetailItemWrapper wrapper = new StealthWODetailItemWrapper();
		wrapper.setItemLabel(itemLabel);
		wrapper.setItemValue(itemValue);
		return wrapper;
	}
	
	private Map<String, Object> getParamMapForReport(Boolean isMobilityTestAvailable, Boolean isStationaryTestAvailable, Boolean doPrintMetrics, Boolean isStealthReport){
		Map<String, Object> paramsMap = new HashMap<>();
		paramsMap.put(ReportConstants.KEY_IS_MOBILITY_TEST_AVAILABLE, isMobilityTestAvailable);
		paramsMap.put(ReportConstants.KEY_IS_STATIONARY_TEST_AVAILABLE, isStationaryTestAvailable);
		paramsMap.put(ReportConstants.KEY_DO_PRINT_METRICS, doPrintMetrics);
		paramsMap.put(ReportConstants.KEY_IS_STEALTH_REPORT, isStealthReport);
		String reportAssetRepo = ConfigUtils.getString(ReportConstants.SMFREPORT_JASPER_FOLDER_PATH);
		paramsMap.put(ReportConstants.LOGO_CLIENT_KEY, reportAssetRepo + ReportConstants.LOGO_CLIENT_IMG);
		return paramsMap;
	}


	private String getFinalScore(String walktestScore, List<RawDataWrapper> locationDataList) {
		logger.info("Inside getFinalScore");
		String finalScore = null;
		try {
			if (walktestScore != null && locationDataList != null && !locationDataList.isEmpty()) {
				finalScore = getwalkTestFinalScore(walktestScore, locationDataList);
			} else if (walktestScore != null) {
				return walktestScore;
			} else if (locationDataList != null && !locationDataList.isEmpty()) {
				finalScore = getLocationDataFinalScore(locationDataList);
			}
		} catch (Exception e) {
			logger.error("Exception Inside getFinalScore {} ", e.getMessage());
		}
		logger.info("Inside getFinalScore {} ", finalScore);
		return finalScore;
	}

	private String getwalkTestFinalScore(String walktestScore, List<RawDataWrapper> locationDataList) {
		String finalScore;
		List<RawDataWrapper> kpiScoreList = locationDataList.stream()
															.filter(rawData -> (rawData != null
																	&& rawData.getOverallKpiScore() != null))
															.collect(Collectors.toList());
		if (walktestScore.equals(ReportConstants.STR_POOR) || kpiScoreList.stream().anyMatch(
				rawData -> rawData.getOverallKpiScore().equals(ReportConstants.STR_POOR))) {
			finalScore = ReportConstants.STR_POOR;
		} else if (walktestScore.equals(ReportConstants.STR_GOOD) && kpiScoreList.stream().allMatch(
				rawData -> rawData.getOverallKpiScore().equals(ReportConstants.STR_GOOD))) {
			finalScore = ReportConstants.STR_GOOD;
		} else {
			finalScore = ReportConstants.STR_FAIR;
		}
		return finalScore;
	}

	private String getLocationDataFinalScore(List<RawDataWrapper> locationDataList) {
		String finalScore;
		List<RawDataWrapper> kpiScoreList = locationDataList.stream()
															.filter(rawData -> (rawData != null
																	&& rawData.getOverallKpiScore() != null))
															.collect(Collectors.toList());
		if (kpiScoreList.stream().anyMatch(
				rawData -> rawData.getOverallKpiScore().equals(ReportConstants.STR_POOR))) {
			finalScore = ReportConstants.STR_POOR;
		} else if (kpiScoreList.stream().allMatch(
				rawData -> rawData.getOverallKpiScore().equals(ReportConstants.STR_GOOD))) {
			finalScore = ReportConstants.STR_GOOD;
		} else {
			finalScore = ReportConstants.STR_FAIR;
		}
		return finalScore;
	}


   private ReportSubWrapper getEnodbWisegraphDataFromList(List<NVL3CsvDataWrapper> nvL3CsvList,
			ReportSubWrapper subWrapper) {

		List<GraphWrapper> rsrpList = new ArrayList<>();
		GraphWrapper graphWrapper = new GraphWrapper();
		List<GraphDataWrapper> graphList = new ArrayList<>();
		logger.info("inside the method getEnodbWisegraphDataFromList  and List size is  {}",
				nvL3CsvList != null ? nvL3CsvList.size() : null);
		try {
			if (nvL3CsvList != null && !nvL3CsvList.isEmpty()) {
				Map<Integer, Long> countMap = getCountMapByEnbId(nvL3CsvList);
				setDataToGraphList(graphList, nvL3CsvList.get(0));
				for (int i = 1; i < nvL3CsvList.size(); i++) {
					NVL3CsvDataWrapper currentWrapper = nvL3CsvList.get(i);
					NVL3CsvDataWrapper previous = nvL3CsvList.get(i - 1);
					mergeNvLCsvDataWrapper(graphList, countMap, currentWrapper, previous);
				}
			}
		} catch (Exception e) {
			logger.error("Exception inside the method getEnodbWisegraphDataFromList {}", Utils.getStackTrace(e));

		}
		graphWrapper.setGraphDataList(graphList);
		rsrpList.add(graphWrapper);
		subWrapper.setRsrpList(rsrpList);
		return subWrapper;
	}

private void mergeNvLCsvDataWrapper(List<GraphDataWrapper> graphList, Map<Integer, Long> countMap,
		NVL3CsvDataWrapper currentWrapper, NVL3CsvDataWrapper previous) {
	if (currentWrapper.geteNodeBId() != null && previous.geteNodeBId() != null && countMap.get(currentWrapper.geteNodeBId()) > ReportConstants.INDEX_ONE) {
			if (currentWrapper.geteNodeBId().equals(previous.geteNodeBId())) {
				setDataToGraphList(graphList, currentWrapper);
			} else {
				if (countMap.get(previous.geteNodeBId()) > ReportConstants.INDEX_ONE) {
					GraphDataWrapper graphDataWrapper = new GraphDataWrapper();
					graphDataWrapper.setValue(null);
					if (previous.getUlThroughPut() != null)
						graphDataWrapper.setValue2(previous.getUlThroughPut());
					graphDataWrapper.setCount(previous.geteNodeBId());
					graphDataWrapper.setTime(new Date(previous.getTimeStamp() + 1000));
					graphList.add(graphDataWrapper);
					setDataToGraphList(graphList, currentWrapper);

				}
			}
	}
}

	private Map<Integer, Long> getCountMapByEnbId(List<NVL3CsvDataWrapper> nvL3CsvList) {
		 nvL3CsvList=nvL3CsvList.stream().filter(x->x.geteNodeBId()!=null).collect(Collectors.toList());
        return nvL3CsvList.stream().collect(Collectors.groupingBy(NVL3CsvDataWrapper::geteNodeBId,Collectors.counting()));
}

	private void setDataToGraphList(List<GraphDataWrapper> graphList, NVL3CsvDataWrapper currentWrapper) {
		GraphDataWrapper wrapper = new GraphDataWrapper();
		wrapper.setValue(currentWrapper.getRsrp());
		if (currentWrapper.getUlThroughPut() != null)
			wrapper.setValue2(currentWrapper.getUlThroughPut());
		wrapper.setCount(currentWrapper.geteNodeBId());
		wrapper.setTime(new Date(currentWrapper.getTimeStamp()));
		graphList.add(wrapper);
	}

	@Override
	public List<RawDataWrapper> getLocationWiseDataForStationry(List<SmfStationaryWidget> locationWiseList,
			String filePath, String opName) {
		logger.info("Inside method getLocationWiseData ");
		List<RawDataWrapper> list = new ArrayList<>();
		try {
			for (SmfStationaryWidget widgetWrapper : locationWiseList) {
				Layer3SummaryWrapper summaryWrapper = new Layer3SummaryWrapper();
				List<NVL3CsvDataWrapper> nvL3CsvList = nvAdhocDriveProcessingService
						.processAdhocFileForReport(widgetWrapper.getWorkorderId(), summaryWrapper);
				logger.info("summaryWrapper is {}", summaryWrapper);
				NVLayer3Utils.getAvgFromArray(summaryWrapper.getYoutubeThroughtPut());
				RawDataWrapper rawDataWrapper = getSautDataWrapperFromList(summaryWrapper, widgetWrapper.getLocation());
				rawDataWrapper = addCemsDataFieldsToRawDataWrapper(rawDataWrapper, widgetWrapper.getWorkorderId());
				list.add(rawDataWrapper);
				opName = opName != null ? opName : ReportConstants.UNDERSCORE;
				adhocDriveReportGeneration.generateCSVReportForLiveDrive(widgetWrapper.getWorkorderId(), nvL3CsvList,
						filePath, widgetWrapper.getLocation() + ReportConstants.UNDERSCORE + opName);
				
			}
			return list;
		} catch (Exception e) {
			logger.error("Exception occured inside method getLocationWiseData of stationary ", Utils.getStackTrace(e));
		}
		return list;

	}
	
	private RawDataWrapper addCemsDataFieldsToRawDataWrapper(RawDataWrapper rawDataWrapper, Integer workorderId) {
		if(rawDataWrapper != null) {
			try {
				GenericWorkorder genericWorkorder = iGenericWorkorderDao.findByPk(workorderId);
				Map<String, String> gwoMeta = genericWorkorder.getGwoMeta();
				if (gwoMeta.containsKey(ReportConstants.GWO_META_ENTITY_CEMS_DATA)) {
					return setCemsFieldsToRawData(gwoMeta.get(ReportConstants.GWO_META_ENTITY_CEMS_DATA), rawDataWrapper);
				}
			} catch (Exception e) {
				logger.error("Exception occured inside method addWhatsappLatencyToRawDataWrapper for Workorder Id: {} Error: ()",workorderId, Utils.getStackTrace(e));
			}
			
		}
		return rawDataWrapper;
	}

	private RawDataWrapper getSautDataWrapperFromList(Layer3SummaryWrapper summaryWrapper, String locationName) {
		RawDataWrapper rawData = new RawDataWrapper();
		logger.info("Inside  method get Saut Data Wrappper for location{} Data {} ", locationName,
				new Gson().toJson(summaryWrapper));
		try {
			rawData.setDlThroughput(NVLayer3Utils.getAvgFromArray(summaryWrapper.getDlThroughPutAdhoc()));
			rawData.setUlThroughput(NVLayer3Utils.getAvgFromArray(summaryWrapper.getUlThroughPutAdhoc()));
			rawData.setLatency(NVLayer3Utils.getAvgFromArray(summaryWrapper.getLatencyAdhoc()));
			rawData.setPacketLoss(NVLayer3Utils.getAvgFromArray(summaryWrapper.getPingPacketLoss()));
			rawData.setYtDlThroughput(NVLayer3Utils.getAvgFromArray(summaryWrapper.getYoutubeThroughtPut()));
			rawData.setBufferTime(summaryWrapper.getYoutubeBufferTime());
			rawData.setDns(NVLayer3Utils.getAvgFromArray(summaryWrapper.getWptDns()));
			rawData.setUrlResponseTime(NVLayer3Utils.getAvgFromArray(summaryWrapper.getWptUrl()));
			rawData.setLocationName(locationName);
			rawData.setOverallKpiScore(getScoreinRawData(summaryWrapper));
			return rawData;
		} catch (Exception e) {
			logger.error("unable to Set Data in Wrapper {} ", Utils.getStackTrace(e));
		}
		return rawData;

	}


	private ReportSubWrapper getGraphDataFromList(List<NVL3CsvDataWrapper> nvL3CsvList, List<String> kpiList) {
		logger.info("Inside method getGraphDataFromList");
		ReportSubWrapper subWrapper  = new ReportSubWrapper();
		try {
			String kpiScore = null;
			List<GraphWrapper> graphWrapperList = null;
			if (nvL3CsvList != null && !nvL3CsvList.isEmpty()) {
				graphWrapperList = new ArrayList<>();
				for (String kpi : kpiList) {
					String kpiUnit = ReportUtil.getUnitByKPiName(kpi);
					kpiUnit = (kpiUnit != null && !kpiUnit.isEmpty()) ? (ReportConstants.SPACE
							+ ReportConstants.OPEN_BRACKET + kpiUnit + ReportConstants.CLOSED_BRACKET)
							: ReportConstants.BLANK_STRING;
					GraphWrapper graphData = new GraphWrapper();
					graphData.setKpiName(kpi + kpiUnit);
					List<GraphDataWrapper> graphDataList = new ArrayList<>();
					Double rsrpGreaterThan100 = getRSRPGreaterThan100Percentage(nvL3CsvList);
					nvL3CsvList = aggreagteDataByTimestamp(nvL3CsvList);
					for (NVL3CsvDataWrapper nvCsvDataWrapper : nvL3CsvList) {
						GraphDataWrapper wrapper = new GraphDataWrapper();
						if (nvCsvDataWrapper.getTimeStamp() != null) {
							wrapper.setKpiName(kpi);
							wrapper.setTime(new Date(nvCsvDataWrapper.getTimeStamp()));
							setValueByKpiName(kpi, nvCsvDataWrapper, wrapper);
							if (ReportConstants.RSRP.equalsIgnoreCase(kpi)) {
								wrapper.setCdfValue(rsrpGreaterThan100);
							}
							logger.info("GraphDataWrapper: {}", wrapper.toString());
							graphDataList.add(wrapper);
						}
					}
					List<Double> kpiPeakValueList = StealthUtils.getMinMaxvalueListBYkpi(kpi);
					List<Double> list = graphDataList.stream().filter(Objects::nonNull)
							.map(GraphDataWrapper::getValue).collect(Collectors.toList());
					if(ReportConstants.RSRP.equalsIgnoreCase(kpi)) {
						kpiScore = StealthUtils.getCoverageKpiScoreForRSRP(list, kpiPeakValueList);
					} else {
						kpiScore = StealthUtils.getCoverageKpiScore(list, kpiPeakValueList);
					}
					
					graphData.setKpiScore(kpiScore);
					graphData.setGraphDataList(graphDataList);
					graphWrapperList.add(graphData);
				}
				logger.info("GraphWrapperList: {}", new Gson().toJson(graphWrapperList));
			}
			subWrapper.setGraphList(graphWrapperList);
			subWrapper.setWalktestScore(StealthUtils.getFinalCoverageScore(graphWrapperList));
			logger.info("Finally going to return the subWrapper with data {} ", new Gson().toJson(subWrapper));
		} catch (Exception e) {
			logger.error("Error inside method getGraphDataFromList {}", Utils.getStackTrace(e));
		}
		return subWrapper;
	
	}
	
	private Double getRSRPGreaterThan100Percentage(List<NVL3CsvDataWrapper> nvL3CsvList) {
		if (nvL3CsvList != null && !nvL3CsvList.isEmpty()) {
			Integer rsrpGreaterThan100 = 0;
			Integer validRsrpCount = 0;
			for (NVL3CsvDataWrapper layer3DataWrapper : nvL3CsvList) {
				if (layer3DataWrapper != null && layer3DataWrapper.getRsrp() != null) {
					validRsrpCount++;
					if (layer3DataWrapper.getRsrp() > ReportConstants.RSRP_GOOD_VLAUE) {
						rsrpGreaterThan100++;
					}
				}
			}
			return ReportUtil.getPercentage(rsrpGreaterThan100, validRsrpCount);
		}
		return null;
	}

	
	private static List<NVL3CsvDataWrapper> aggreagteDataByTimestamp(List<NVL3CsvDataWrapper> nvL3CsvList) {
		try {
			Map<Long, List<NVL3CsvDataWrapper>> timewiseWrapperMap = nvL3CsvList.stream().collect(
					Collectors.groupingBy(NVL3CsvDataWrapper::getTimeStamp, Collectors.toList()));
			logger.info("timewiseWrapperMap  size {} , data {} ",
					timewiseWrapperMap != null ? timewiseWrapperMap.size() : null,
					new Gson().toJson(timewiseWrapperMap));
			List<NVL3CsvDataWrapper> finalCsvList = new ArrayList<>();
			if (timewiseWrapperMap != null) {
				timewiseWrapperMap.forEach((key, value) -> aggregatingSametimeStampdata(value, finalCsvList));
			}
			logger.info("finalCsvList  Size {} , Data {} ", finalCsvList.size(), new Gson().toJson(finalCsvList));
			return finalCsvList;
		} catch (Exception e) {
			logger.error("Unable to aggreagteDataByTimestamp {} ", e.getMessage());
			return nvL3CsvList;
		}
	}

	private static void aggregatingSametimeStampdata(List<NVL3CsvDataWrapper> listOfWrappr, List<NVL3CsvDataWrapper> finalCsvList) {
		Optional<NVL3CsvDataWrapper> csvWrapper  =listOfWrappr.stream().reduce((obj1,obj2)->aggregateDataByTimeStamp(obj1,obj2));
		NVL3CsvDataWrapper wrapper  = (csvWrapper.isPresent())?csvWrapper.get():null;
		if(wrapper!=null){
			finalCsvList.add(wrapper);
		}
	}

	private static  NVL3CsvDataWrapper aggregateDataByTimeStamp(NVL3CsvDataWrapper obj1, NVL3CsvDataWrapper obj2) {
		if(obj1.getTimeStamp().equals(obj2.getTimeStamp())){
			try {
				if(obj1.getRsrp()!=null && obj1.getRsrp()>obj2.getRsrp()){
					obj2.setRsrp(obj1.getRsrp());
				}
				if(obj1.getSinr()!=null && obj1.getSinr()>obj2.getSinr()){
					obj2.setSinr(obj1.getSinr());
				}
				if(obj1.getUlThroughPut()!=null && obj1.getUlThroughPut()>obj2.getUlThroughPut()){
					obj2.setUlThroughPut(obj1.getUlThroughPut());
				}
				if(obj1.getDlThroughPut()!=null && obj1.getDlThroughPut()>obj2.getDlThroughPut()){
					obj2.setDlThroughPut(obj1.getDlThroughPut());
				}
				return obj2;
			} catch (Exception e) {
				logger.error("Error in aggregateDataByTimeStamp",Utils.getStackTrace(e));
			}
		}
		return obj2;
	}

	private void setValueByKpiName(String kpi, NVL3CsvDataWrapper nvCsvDataWrapper, GraphDataWrapper wrapper) {
		try {
			if (kpi.equalsIgnoreCase(ReportConstants.RSRP)) {
				wrapper.setValue(nvCsvDataWrapper.getRsrp());
			} else if (kpi.equalsIgnoreCase(ReportConstants.RSRQ)) {
				wrapper.setValue(nvCsvDataWrapper.getRsrq());
			} else if (kpi.equalsIgnoreCase(ReportConstants.SINR)) {
				wrapper.setValue(nvCsvDataWrapper.getSinr());
			} else if (kpi.equalsIgnoreCase(ReportConstants.UL_THROUGHPUT)) {
				wrapper.setValue(nvCsvDataWrapper.getUlThroughPut());
			} else if (kpi.equalsIgnoreCase(ReportConstants.DL_THROUGHPUT)) {
				wrapper.setValue(nvCsvDataWrapper.getDlThroughPut());
			}
		}
		catch(Exception e) {
			logger.error("Error inside the method setValueByKpiName {}",e.getMessage());
		}
	}
	
    @Override
	public String getReportName(SmfWidgetWrapper wrapper ) {
		try {
			
			AnalyticsRepository analyticsRepository = analyticsRepositoryDao.findByPk(wrapper.getAnalyticsrepositoryid_pk());
			if(analyticsRepository!=null && analyticsRepository.getName()!=null){
				return analyticsRepository.getName();
			}else{
				return ReportConstants.CUSTOMER_EXPERIENCE_REPORT;
			}
		} catch (Exception e) {
			logger.error("Unable to find the Saut report Name {} ",Utils.getStackTrace(e));
		}
		return null;
	}

	private File proceedToCreateReport(SMFReportWrapper mainWrapper, String destFileName, Boolean needPPTX, Map<String, Object> imageMap) {
		logger.info("inside the method proceedToCreateReport");
		try {
			String reportAssetRepo = ConfigUtils.getString(ReportConstants.SMFREPORT_JASPER_FOLDER_PATH);
			List<SMFReportWrapper> dataSourceList = new ArrayList<>();
			dataSourceList.add(mainWrapper);
			JRBeanCollectionDataSource rfbeanColDataSource = new JRBeanCollectionDataSource(dataSourceList);
			imageMap.put(ReportConstants.SUBREPORT_DIR, reportAssetRepo);
			imageMap.put(ReportConstants.LOGO_YOUTUBE_KEY, reportAssetRepo + ReportConstants.LOGO_YOUTUBE_IMG);
			imageMap.put(ReportConstants.LOGO_INTERNET_KEY, reportAssetRepo + ReportConstants.LOGO_INTERNET_IMG);
			imageMap.put(ReportConstants.LOGO_TABLE_KEY, reportAssetRepo + ReportConstants.LOGO_TABLE_IMG);
			imageMap.put(ConfigUtils.getString(ReportConstants.NV_REPORT_LOGO_CLIENT_KEY), reportAssetRepo + ConfigUtils.getString(ReportConstants.NV_REPORT_LOGO_CLIENT_IMG));
			imageMap.put(ReportConstants.LOGO_WHATSAPP_KEY, reportAssetRepo + ReportConstants.LOGO_WHATSAPP_IMG);
			imageMap.put(ReportConstants.LOGO_VOLTE_KEY, reportAssetRepo + ReportConstants.LOGO_VOLTE_IMG);
			imageMap.put(ReportConstants.IMAGE_PARAM_SCREEN_BG, reportAssetRepo + ReportConstants.IMAGE_SCREEN_BG);
			String destinationFileName = null;
			String fileName = JasperFillManager.fillReportToFile(
					reportAssetRepo + ReportConstants.MAIN_JASPER, imageMap, rfbeanColDataSource);
			if (needPPTX) {
				destinationFileName = destFileName + ReportConstants.PPTX_EXTENSION;
				getPPTXFromReport(fileName, destinationFileName);
			} else {
				destinationFileName = destFileName + ReportConstants.PDF_EXTENSION;
				getPdfFromReport(fileName, destinationFileName);
			}
			return ReportUtil.getIfFileExists(destinationFileName);
		} catch (Exception e) {
			logger.error("@proceedToCreateReport getting err={}", Utils.getStackTrace(e));
		}
		logger.info(
				"@proceedToCreateReport going to return null as there has been some problem in generating report");
		return null;
	}
	
	private boolean getPPTXFromReport(String inputFileName, String outputFileName) throws JRException {
		if(!StringUtils.isBlank(inputFileName)) {
			JRPptxExporter exporter = new JRPptxExporter();
			exporter.setExporterInput(new SimpleExporterInput(new File(inputFileName)));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(new File(outputFileName)));
//			exporter.setParameter(JRExporterParameter.INPUT_FILE, new File(inputFileName));
//			exporter.setParameter(JRExporterParameter.OUTPUT_FILE, new File(outputFileName));
			exporter.exportReport();
			return true;
		}
		return false;
	}
	
	private boolean getPdfFromReport(String inputFileName, String outputFileName) throws JRException {
		if(!StringUtils.isBlank(inputFileName)) {
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setExporterInput(new SimpleExporterInput(new File(inputFileName)));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(new File(outputFileName)));
//			exporter.setParameter(JRExporterParameter.INPUT_FILE, new File(inputFileName));
//			exporter.setParameter(JRExporterParameter.OUTPUT_FILE, new File(outputFileName));
			exporter.exportReport();
			return true;
		}
		return false;
	}

	private String getScoreinRawData(Layer3SummaryWrapper summaryWrapper) {
		logger.info("Inside getScoreinRawData {} ",new Gson().toJson(summaryWrapper));
		String p1Score = getP1Score(summaryWrapper);
		String p2Score = getP2Score(summaryWrapper);
		logger.info("P1 Score p1Score");
		if(p1Score!=null && p2Score!=null){
			if(p1Score.equals(ReportConstants.STR_GOOD)&&(p2Score.equals(ReportConstants.STR_GOOD)||p2Score.equals(ReportConstants.STR_FAIR))){
				return ReportConstants.STR_GOOD;
			} else if(p1Score.equals(ReportConstants.STR_FAIR) && (p2Score.equals(ReportConstants.STR_GOOD)||p2Score.equals(ReportConstants.STR_FAIR))){
				return ReportConstants.STR_FAIR;
			} else {
				return ReportConstants.STR_POOR;
			}
		}else if(p1Score!=null ){
			return p1Score;
		}else if(p2Score!=null){
			return p2Score;
		}
		return null;
	}
	
	private String getP2Score(Layer3SummaryWrapper summaryWrapper) {
		String finalP2Score = null;
		String scoreUl = null;
		String scoreResponseTime = null;
		try{
		if (summaryWrapper.getUlThroughPutAdhoc() != null) {
			scoreUl = StealthUtils.getUlScoreForP2(Arrays.asList(summaryWrapper.getUlThroughPutAdhoc()));
		}
		if(summaryWrapper.getResponseTime()!=null){
			scoreResponseTime = StealthUtils.getResponseTimeScore(Arrays.asList(summaryWrapper.getResponseTime()));
		}
		if(scoreUl!=null || scoreResponseTime!=null){
			if((scoreUl!=null && ReportConstants.STR_POOR.equals(scoreUl)) || (scoreResponseTime!=null && ReportConstants.STR_POOR.equals(scoreResponseTime))){
				finalP2Score = ReportConstants.STR_POOR;
			} if((scoreUl!=null && ReportConstants.STR_GOOD.equals(scoreUl)) && (scoreResponseTime!=null && ReportConstants.STR_GOOD.equals(scoreResponseTime))){
				finalP2Score = ReportConstants.STR_GOOD;
			} else {
				finalP2Score = ReportConstants.STR_FAIR;
			}
		}else{
		   return finalP2Score;	
		 }
		}catch (Exception e){
			logger.info("Exception in getP2Score {} ",e.getMessage());
		}
		return finalP2Score;
	}

	private String getP1Score(Layer3SummaryWrapper summaryWrapper) {
		logger.info("Inside getP1Score");
		String finalP1Score=null;
		try{
		String scoreDL = null;
		String scoreLatency = null;
		String scorepacketLoss = null;
		String scoreDnsResponseTime = null;
		if(summaryWrapper.getDlThroughPutAdhoc()!=null) {
			scoreDL = StealthUtils.getDlScore(Arrays.asList(summaryWrapper.getDlThroughPutAdhoc()));
		}
		if(summaryWrapper.getLatencyAdhoc()!=null){
			scoreLatency = StealthUtils.getLatencyScore(Arrays.asList(summaryWrapper.getLatencyAdhoc()));
		}
		if(summaryWrapper.getPingPacketLoss()!=null){
			scorepacketLoss = StealthUtils.getPacketLossScore(Arrays.asList(summaryWrapper.getPingPacketLoss()));
		}
		if(summaryWrapper.getWptDns()!=null){
			scoreDnsResponseTime = StealthUtils.getResponseTimeScore(Arrays.asList(summaryWrapper.getWptDns()));
		}
		logger.info("scoreDL scoreDL {} ",scoreDL );
		logger.info("scoreLatency scoreLatency {} ",scoreLatency );
		logger.info("scorepacketLoss scorepacketLoss {} ",scorepacketLoss );
		finalP1Score = getFinal1Score(finalP1Score, scoreDL, scoreLatency, scorepacketLoss, scoreDnsResponseTime);
		}catch (Exception e) {
			logger.info("Exception Inside getP1Score {} ",e.getMessage());
		}
		return finalP1Score;
		
	}

	private String getFinal1Score(String finalP1Score, String scoreDL, String scoreLatency, String scorepacketLoss,
			String scoreDnsResponseTime) {
		if(scoreDL!=null || scoreLatency!=null || scorepacketLoss!=null || scoreDnsResponseTime!=null){
			if((scoreDL!=null && ReportConstants.STR_POOR.equals(scoreDL))||(scoreLatency!=null && ReportConstants.STR_POOR.equals(scoreLatency))
					||(scorepacketLoss!=null && ReportConstants.STR_POOR.equals(scorepacketLoss))
					||(scoreDnsResponseTime!=null && ReportConstants.STR_POOR.equals(scoreDnsResponseTime))){
				finalP1Score = ReportConstants.STR_POOR;
			}else if((scoreDL!=null && ReportConstants.STR_GOOD.equals(scoreDL)) && (scoreLatency!=null && ReportConstants.STR_GOOD.equals(scoreLatency))
					&&(scorepacketLoss!=null && ReportConstants.STR_GOOD.equals(scorepacketLoss))
					&& (scoreDnsResponseTime!=null && ReportConstants.STR_GOOD.equals(scoreDnsResponseTime))){
				finalP1Score = ReportConstants.STR_GOOD;
			}else {
				finalP1Score = ReportConstants.STR_FAIR;
			}
		}
		return finalP1Score;
	}
	
	private List<NVL3CsvDataWrapper> getGraphDataForStealth(List<String[]> rawdataList) {

		List<NVL3CsvDataWrapper> csvDataList = new ArrayList<>();

		try {

			for (String[] rawData : rawdataList) {
				NVL3CsvDataWrapper csvDataWrapper = new NVL3CsvDataWrapper();
				if (ReportUtil.checkIndexValue(ReportConstants.STEALTH_INDEX_TIMESTAMP, rawData)) {
					csvDataWrapper.setTimeStamp(Long.parseLong(rawData[ReportConstants.STEALTH_INDEX_TIMESTAMP]));
				}
				if (ReportUtil.checkIndexValue(ReportConstants.STEALTH_INDEX_RSRP, rawData)) {
					csvDataWrapper.setRsrp(Double.parseDouble(rawData[ReportConstants.STEALTH_INDEX_RSRP]));
				}
				if (ReportUtil.checkIndexValue(ReportConstants.STEALTH_INDEX_RSRQ, rawData)) {
					csvDataWrapper.setRsrq(Double.parseDouble(rawData[ReportConstants.STEALTH_INDEX_RSRQ]));
				}
				if (ReportUtil.checkIndexValue(ReportConstants.STEALTH_INDEX_SINR, rawData)) {
					csvDataWrapper.setSinr(Double.parseDouble(rawData[ReportConstants.STEALTH_INDEX_SINR]));
				}
				if (ReportUtil.checkIndexValue(ReportConstants.STEALTH_INDEX_ENODEB, rawData)) {
					csvDataWrapper.seteNodeBId(Integer.parseInt(rawData[ReportConstants.STEALTH_INDEX_ENODEB]));
				}
				setCommonDataInCsvWrapper(rawData, csvDataWrapper);
				logger.info("csvDataWrapper: {}", new Gson().toJson(csvDataWrapper));
				csvDataList.add(csvDataWrapper);
			}
		} catch (Exception e) {
			logger.error("Error inside method getGraphDataForStealth {}", ExceptionUtils.getStackTrace(e));
		}

		return csvDataList;

	}

	private void setCommonDataInCsvWrapper(String[] rawData, NVL3CsvDataWrapper csvDataWrapper) {
		String testType = getValueFromArray(ReportConstants.STEALTH_INDEX_TEST_TYPE, rawData);
		if (testType != null && (testType.equalsIgnoreCase(ReportConstants.STEALTH_TEST_TYPE_QUICK_TEST)
				|| testType.equalsIgnoreCase(ReportConstants.STEALTH_TEST_TYPE_FULL_TEST)
				|| testType.equalsIgnoreCase(ReportConstants.STEALTH_TEST_TYPE_UL))
				&& ReportUtil.checkIndexValue(ReportConstants.STEALTH_INDEX_UL, rawData)) {
				csvDataWrapper.setUlThroughPut(Double.parseDouble(rawData[ReportConstants.STEALTH_INDEX_UL]));
		}
		if (testType != null && (testType.equalsIgnoreCase(ReportConstants.STEALTH_TEST_TYPE_QUICK_TEST)
				|| testType.equalsIgnoreCase(ReportConstants.STEALTH_TEST_TYPE_FULL_TEST)) &&
				ReportUtil.checkIndexValue(ReportConstants.STEALTH_INDEX_ACTIVE_DL, rawData) ) {
				csvDataWrapper.setDlThroughPut(Double.parseDouble(rawData[ReportConstants.STEALTH_INDEX_ACTIVE_DL]));
		}
		if(testType != null && testType.equalsIgnoreCase(ReportConstants.STEALTH_TEST_TYPE_DL) && 
				ReportUtil.checkIndexValue(ReportConstants.STEALTH_INDEX_DL_DL, rawData)){
				csvDataWrapper.setDlThroughPut(Double.parseDouble(rawData[ReportConstants.STEALTH_INDEX_DL_DL]));
		}
	}
	
	private List<RawDataWrapper> getStationaryDataForStealth(List<String[]> rawDataList, String taskId, String date){
		logger.info("inside getStationaryDataForStealth");
		RawDataWrapper rawDataWrapper = new RawDataWrapper();
		try {
			Layer3SummaryWrapper layer3SummaryWrapper = getSummaryWrapperForStealthStationary(rawDataList);
			rawDataWrapper.setDlThroughput(NVLayer3Utils.getAvgFromArray(layer3SummaryWrapper.getDlThroughPutAdhoc()));
			rawDataWrapper.setUlThroughput(NVLayer3Utils.getAvgFromArray(layer3SummaryWrapper.getUlThroughPutAdhoc()));
			rawDataWrapper.setYtDlThroughput(NVLayer3Utils.getAvgFromArray(layer3SummaryWrapper.getYoutubeThroughtPut()));
			rawDataWrapper.setLatency(NVLayer3Utils.getAvgFromArray(layer3SummaryWrapper.getLatencyAdhoc()));
			rawDataWrapper.setPacketLoss(NVLayer3Utils.getAvgFromArray(layer3SummaryWrapper.getPingPacketLoss()));
			rawDataWrapper.setBufferTime(layer3SummaryWrapper.getYoutubeBufferTime());
			rawDataWrapper.setDns(NVLayer3Utils.getAvgFromArray(layer3SummaryWrapper.getWptDns()));
			rawDataWrapper.setUrlResponseTime(NVLayer3Utils.getAvgFromArray(layer3SummaryWrapper.getWptUrl()));
			rawDataWrapper.setLocationName(ReportConstants.STEALTH_TEST_NAME);
			rawDataWrapper.setOverallKpiScore(getScoreinRawData(layer3SummaryWrapper));
			if (NumberUtils.isCreatable(taskId) && NumberUtils.isCreatable(date)) {
				setCemsFieldsToRawData(driveTestKPIDao.getCemsDataByStealthTaskIdAndDate(
						Long.parseLong(date), Integer.parseInt(taskId)), rawDataWrapper);
			}
		} catch (Exception e) {
			logger.error("Error inside method getStationaryDataForStealth {}", ExceptionUtils.getStackTrace(e));
		}
		return Arrays.asList(rawDataWrapper);
	}
	
	private static RawDataWrapper setCemsFieldsToRawData(String cemsData, RawDataWrapper rawDataWrapper) throws JSONException{
		if (!StringUtils.isBlank(cemsData)) {
				JSONObject jsonObj = new JSONObject(cemsData);
				rawDataWrapper
						.setWhatsappLatency(jsonObj.has(ReportConstants.JSON_KEY_WHATSAPP_LATENCY)?jsonObj.getDouble(ReportConstants.JSON_KEY_WHATSAPP_LATENCY):null);
				rawDataWrapper.setVolteMosPercent(jsonObj.has(ReportConstants.JSON_KEY_VOLTE_MOS_PERCENT)?jsonObj.getDouble(ReportConstants.JSON_KEY_VOLTE_MOS_PERCENT):null);
				rawDataWrapper.setRtpLossPercent(jsonObj.has(ReportConstants.JSON_KEY_RTP_LOSS_PERCENT)?jsonObj.getDouble(ReportConstants.JSON_KEY_RTP_LOSS_PERCENT):null);
		}
		logger.info("Going to check cems data {} ",new Gson().toJson(rawDataWrapper));
		return rawDataWrapper;
	}
	
	private Layer3SummaryWrapper getSummaryWrapperForStealthStationary(List<String[]> rawData) {
		Layer3SummaryWrapper layer3SummaryWrapper = new Layer3SummaryWrapper();
		layer3SummaryWrapper.setDlThroughPutAdhoc(getDLThroughputFromRawDataList(rawData));
		layer3SummaryWrapper.setUlThroughPutAdhoc(getULThroughputFromRawDataList(rawData));
		layer3SummaryWrapper.setYoutubeThroughtPut(getYTDLThroughputFromRawDataList(rawData));
		layer3SummaryWrapper.setLatencyAdhoc(getLatencyFromRawDataList(rawData));
		layer3SummaryWrapper.setWptDns(getDNSFromRawDataList(rawData));
		layer3SummaryWrapper.setPingPacketLoss(getPacketLossFromRawDataList(rawData));
		layer3SummaryWrapper.setWptUrl(getResponseTimeFromRawDataList(rawData));
		layer3SummaryWrapper.setYoutubeBufferTime(getBufferTimeFromRawDataList(rawData));
		return layer3SummaryWrapper;
	}
	
	private Double[] getDLThroughputFromRawDataList(List<String[]> rawDataList) {
		int count = 0;
		double sum = 0.0;
		Double[] dataArray = null;
		for (String[] rawData : rawDataList) {
			String testType = getValueFromArray(ReportConstants.STEALTH_INDEX_TEST_TYPE, rawData);
			if (ReportConstants.STEALTH_TEST_TYPE_DL.equalsIgnoreCase(testType)
					&& ReportUtil.checkIndexValue(ReportConstants.STEALTH_INDEX_DL_DL, rawData)) {
				String dlValue = getValueFromArray(ReportConstants.STEALTH_INDEX_DL_DL, rawData);
				if (dlValue != null) {
					dataArray = NVLayer3Utils.getDoubleArrayValue(dataArray, Double.parseDouble(dlValue));
					sum = sum + Double.parseDouble(dlValue);
					count++;
				}
			} else if ((ReportConstants.STEALTH_TEST_TYPE_QUICK_TEST.equalsIgnoreCase(testType)
					|| ReportConstants.STEALTH_TEST_TYPE_FULL_TEST.equalsIgnoreCase(testType))
					&& ReportUtil.checkIndexValue(ReportConstants.STEALTH_INDEX_ACTIVE_DL, rawData)) {
				String dlValue = getValueFromArray(ReportConstants.STEALTH_INDEX_ACTIVE_DL, rawData);
				if (dlValue != null) {
					dataArray = NVLayer3Utils.getDoubleArrayValue(dataArray, Double.parseDouble(dlValue));
					sum = sum + Double.parseDouble(dlValue);
					count++;
				}
			}
		}
		
		logger.info("dl throughput count: {}, sum: {}", count, sum);
		
		return dataArray;
	}
	
	private Double[] getULThroughputFromRawDataList(List<String[]> rawDataList) {
		int count = 0;
		double sum = 0.0;
		Double[] dataArray = null;
		for (String[] rawData : rawDataList) {
			String testType = getValueFromArray(ReportConstants.STEALTH_INDEX_TEST_TYPE, rawData);
				if (testType != null && (testType.equalsIgnoreCase(ReportConstants.STEALTH_TEST_TYPE_QUICK_TEST)
						|| testType.equalsIgnoreCase(ReportConstants.STEALTH_TEST_TYPE_FULL_TEST)
						|| testType.equalsIgnoreCase(ReportConstants.STEALTH_TEST_TYPE_UL))
						&& ReportUtil.checkIndexValue(ReportConstants.STEALTH_INDEX_UL, rawData)) {
						String ulValue = getValueFromArray(ReportConstants.STEALTH_INDEX_UL, rawData);
						if (ulValue != null) {
							dataArray = NVLayer3Utils.getDoubleArrayValue(dataArray, Double.parseDouble(ulValue));
							sum = sum + Double.parseDouble(ulValue);
							count++;
						}
				}
		}
		logger.info("ul throughput count: {}, sum: {}", count, sum);
		return dataArray;
	}
	
	private Double[] getYTDLThroughputFromRawDataList(List<String[]> rawDataList) {
		int count = 0;
		double sum = 0.0;
		Double[] dataArray = null;
		for(String[] rawData:rawDataList) {
			String testType = getValueFromArray(ReportConstants.STEALTH_INDEX_TEST_TYPE, rawData);
			if (testType != null && testType.equalsIgnoreCase(ReportConstants.STEALTH_TEST_TYPE_YOUTUBE) 
				&& ReportUtil.checkIndexValue(ReportConstants.STEALTH_INDEX_YT_DL, rawData)) {
				String dlValue = getValueFromArray(ReportConstants.STEALTH_INDEX_YT_DL, rawData);
				if (dlValue != null) {
					dataArray = NVLayer3Utils.getDoubleArrayValue(dataArray, Double.parseDouble(dlValue));
					sum = sum + Double.parseDouble(dlValue);
					count++;
				}
			}
		}
		logger.info("yt dl throughput count: {}, sum: {}", count, sum);
		return dataArray;
	}
	
	private Double[] getLatencyFromRawDataList(List<String[]> rawDataList) {
		int count = 0;
		double sum = 0.0;
		Double[] dataArray = null;
		for(String[] rawData:rawDataList) {
			String testType = getValueFromArray(ReportConstants.STEALTH_INDEX_TEST_TYPE, rawData);
			if (testType != null && (testType.equalsIgnoreCase(ReportConstants.STEALTH_TEST_TYPE_QUICK_TEST)
					|| testType.equalsIgnoreCase(ReportConstants.STEALTH_TEST_TYPE_FULL_TEST))
					&& ReportUtil.checkIndexValue(ReportConstants.STEALTH_INDEX_ACTIVE_LATENCY, rawData)) {
						String latency = getValueFromArray(ReportConstants.STEALTH_INDEX_ACTIVE_LATENCY, rawData);
				if (latency != null) {
					dataArray = NVLayer3Utils.getDoubleArrayValue(dataArray, Double.parseDouble(latency));
					sum = sum + Double.parseDouble(latency);
					count++;
				}
			}
		}
		logger.info("latency count: {}, sum: {}", count, sum);
		return dataArray;
	}
	
	private Double[] getDNSFromRawDataList(List<String[]> rawDataList) {
		int count = 0;
		double sum = 0.0;
		Double[] dataArray = null;
		for(String[] rawData:rawDataList) {
			String testType = getValueFromArray(ReportConstants.STEALTH_INDEX_TEST_TYPE, rawData);
			if (testType != null && testType.equalsIgnoreCase(ReportConstants.STEALTH_TEST_TYPE_WPT)
					&& ReportUtil.checkIndexValue(ReportConstants.STEALTH_INDEX_WPT_DNS, rawData)) {
				String dns = getValueFromArray(ReportConstants.STEALTH_INDEX_WPT_DNS, rawData);
				if (dns != null) {
					dataArray = NVLayer3Utils.getDoubleArrayValue(dataArray, Double.parseDouble(dns));
					sum = sum + Double.parseDouble(dns);
					count++;
				}
			}
		}
		logger.info("dns count: {}, sum: {}", count, sum);
		return dataArray;
	}
	
	private Double[] getPacketLossFromRawDataList(List<String[]> rawDataList) {
		int count = 0;
		double sum = 0.0;
		Double[] dataArray = null;
		for(String[] rawData:rawDataList) {
			String testType = getValueFromArray(ReportConstants.STEALTH_INDEX_TEST_TYPE, rawData);
			if (testType != null && (testType.equalsIgnoreCase(ReportConstants.STEALTH_TEST_TYPE_QUICK_TEST)
					|| testType.equalsIgnoreCase(ReportConstants.STEALTH_TEST_TYPE_FULL_TEST))
					&& ReportUtil.checkIndexValue(ReportConstants.STEALTH_INDEX_ACTIVE_PACKET_LOSS, rawData)) {
				String packetLoss = getValueFromArray(ReportConstants.STEALTH_INDEX_ACTIVE_PACKET_LOSS,rawData);
				if (packetLoss != null) {
					dataArray = NVLayer3Utils.getDoubleArrayValue(dataArray, Double.parseDouble(packetLoss));
					sum = sum + Double.parseDouble(packetLoss);
					count++;
				}
			}
		}
		logger.info("packet loss count: {}, sum: {}", count, sum);
		return dataArray;
	}
	
	private Double getBufferTimeFromRawDataList(List<String[]> rawDataList) {
		List<Double> youtubeBufferTimeList = new ArrayList<>();
		try {
			for (String[] rawData : rawDataList) {
				String testType = getValueFromArray(ReportConstants.STEALTH_INDEX_TEST_TYPE, rawData);
				if (testType != null && testType.equalsIgnoreCase(ReportConstants.STEALTH_TEST_TYPE_YOUTUBE)
						&& ReportUtil.checkIndexValue(ReportConstants.STEALTH_INDEX_YT_BUFFER_TIME, rawData)) {
					String bufferTime = getValueFromArray(ReportConstants.STEALTH_INDEX_YT_BUFFER_TIME,
							rawData);
					String duration = getValueFromArray(ReportConstants.STEALTH_INDEX_YT_VIDEO_DURATION,
							rawData);
					if (bufferTime != null && duration != null && !bufferTime.equalsIgnoreCase("null") && !duration.equalsIgnoreCase("null")) {
						youtubeBufferTimeList.add(
								NVLayer3Utils.roundOffDouble(QMDLConstant.DECIMAL_PATTERN_2DIGIT,
										(Double.parseDouble(bufferTime) / Double.parseDouble(duration)) * 100));
					}
				}
			}
		} catch (Exception e) {
			logger.error("Error inside method getBufferTimeFromRawDataList {}", ExceptionUtils.getStackTrace(e));
		}

		return getBufferTimeFromYoutubeDataList(youtubeBufferTimeList);
	}
	
	private Double getBufferTimeFromYoutubeDataList(List<Double> youtubeDataList) {
		if (youtubeDataList != null && !youtubeDataList.isEmpty()) {
			return youtubeDataList.get(youtubeDataList.size() - 1);
		} else {
			return null;
		}
	}
	
	private Double[] getResponseTimeFromRawDataList(List<String[]> rawDataList) {
		int count = 0;
		double sum = 0.0;
		Double[] dataArray = null;
		for(String[] rawData:rawDataList) {
			String testType = getValueFromArray(ReportConstants.STEALTH_INDEX_TEST_TYPE, rawData);
			if (testType != null && testType.equalsIgnoreCase(ReportConstants.STEALTH_TEST_TYPE_WPT)
					&& ReportUtil.checkIndexValue(ReportConstants.STEALTH_INDEX_WPT_RESPONSE_TIME, rawData)) {
				String dns = getValueFromArray(ReportConstants.STEALTH_INDEX_WPT_RESPONSE_TIME, rawData);
				if (dns != null) {
					dataArray = NVLayer3Utils.getDoubleArrayValue(dataArray, Double.parseDouble(dns));
					sum = sum + Double.parseDouble(dns);
					count++;
				}
			}
		}
		logger.info("response time count: {}, sum: {}", count, sum);
		return dataArray;
	}
	
	private String getValueFromArray(Integer index, String[] data) {
		if(data != null && data.length > index) {
			return data[index];
			
		}
		return null;
	}
	
	
	
	public void multiValueMapExample(){
		MultiValuedMap<String, String> map = new HashSetValuedHashMap<>();
		map.put("key1", "value1");
		map.put("key1", "value2");
		map.put("key1", "value3");
		map.put("key1", "value4");
		for (Entry<String, String> mapdata : map.entries()) {
			logger.info("mapdata key: {}, value: {} ",mapdata.getKey(),mapdata.getValue());
			
		}
	}
	
	public static void main(String[] args) {
		String cemsdata = "{\"Goodmos\":100.0,\"goodpacketloss\":100.00000000000000000,\"whatsappLatency1\":107754,\"youtube_throughput\":441.49515546291866}";
		try {
			setCemsFieldsToRawData(cemsdata, new RawDataWrapper());
		} catch (Exception e) {
			logger.error(e.getStackTrace());
		}
	}

}

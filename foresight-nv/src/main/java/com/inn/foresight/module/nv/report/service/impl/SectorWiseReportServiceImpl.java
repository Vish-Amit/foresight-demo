package com.inn.foresight.module.nv.report.service.impl;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.report.dao.IAnalyticsRepositoryDao;
import com.inn.foresight.core.report.model.AnalyticsRepository;
import com.inn.foresight.core.report.model.AnalyticsRepository.progress;
import com.inn.foresight.module.nv.core.workorder.dao.impl.IGenericWorkorderDao;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder.TemplateType;
import com.inn.foresight.module.nv.feedback.dao.IConsumerFeedbackDao;
import com.inn.foresight.module.nv.feedback.model.ConsumerFeedback;
import com.inn.foresight.module.nv.layer3.constants.QMDLConstant;
import com.inn.foresight.module.nv.report.customreport.ssvt.wrapper.SectorWiseWrapper;
import com.inn.foresight.module.nv.report.customreport.ssvt.wrapper.SiteInfoWrapper;
import com.inn.foresight.module.nv.report.service.IReportService;
import com.inn.foresight.module.nv.report.service.ISectorWiseReportService;
import com.inn.foresight.module.nv.report.utils.ExcelReportUtils;
import com.inn.foresight.module.nv.report.utils.ReportConstants;
import com.inn.foresight.module.nv.report.utils.ReportUtil;
import com.inn.foresight.module.nv.report.wrapper.SectorWiseSummaryWrapper;
import com.inn.product.um.user.utils.UmConstants;

@Service("SectorWiseReportServiceImpl")
public class SectorWiseReportServiceImpl implements ISectorWiseReportService, ReportConstants {
	private Logger logger = LogManager.getLogger(SectorWiseReportServiceImpl.class);

	@Autowired
	private IGenericWorkorderDao iGenericWorkorderDao;

	@Autowired
	private IReportService reportService;

	@Autowired
	private IConsumerFeedbackDao consumerFeedbackDao;

	@Autowired
	private IAnalyticsRepositoryDao analyticsRepositoryDao;

	public SectorWiseReportServiceImpl() {
		super();
	}

	@Override
	@Transactional
	public Response execute(String json) {
		logger.info("Inside execute method to create SSVT Report with {} ", json);
		Integer analyticsrepoId = null;
		List<SectorWiseSummaryWrapper> finalWrapperList = null;
		try {
			Map<String, Object> jsonMap = getJsonDataMap(json);
			Long startDate = (Long) jsonMap.get(START_DATE);
			Long endDate = (Long) jsonMap.get(END_DATE);
			List<GenericWorkorder> workorderdata = iGenericWorkorderDao.getWoIdListForDateRange(startDate, endDate,
					TemplateType.NV_SSVT);
			List<Integer> workorderIds = getwoidListFromWoData(workorderdata);
			logger.info("size of workorderlist {}", workorderIds);
			analyticsrepoId = (Integer) jsonMap.get(ANALYTICS_REPOSITORY_ID);
			AnalyticsRepository analyticsRepository = analyticsRepositoryDao.findByPk(analyticsrepoId);
			Map<Integer, String> woWiseSummaryMap = getSummaryDataFromGWOMeta(workorderIds);
			finalWrapperList = generateSectorWiseReport(woWiseSummaryMap);
			createSectorWiseExcelReport(finalWrapperList, analyticsRepository);
		} catch (Exception e) {
			logger.error("Error inside the method createReportForWorkOrderID for json {} , {} ", json,
					Utils.getStackTrace(e));
			analyticsRepositoryDao.updateStatusInAnalyticsRepository(analyticsrepoId, null, "Something Went Wrong",
					progress.Failed, null);
		}
		return Response.ok(ForesightConstants.FAILURE_JSON).build();
	}

	public Map<String, Object> getJsonDataMap(String json) {
		Map<String, Object> jsonMap = null;
		try {
			jsonMap = new ObjectMapper().readValue(json, HashMap.class);
			Integer analyticsrepoId = (Integer) jsonMap.get(ForesightConstants.ANALYTICAL_REPORT_KEY);
			AnalyticsRepository analyticrepositoryObj = analyticsRepositoryDao.findByPk(analyticsrepoId);
			logger.info("analyticsrepositoryId {} , {}  ", analyticsrepoId, analyticrepositoryObj.getReportConfig());
			String reportConfig = analyticrepositoryObj.getReportConfig() != null
					? analyticrepositoryObj.getReportConfig().replaceAll("\'", "\"")
					: null;
			Map<String, Object> configMap = new ObjectMapper().readValue(reportConfig, HashMap.class);
			logger.info("AnalyticsRepository jsonObject {} ", configMap);
			Long startdate = (Long) configMap.get(START_DATE);
			Long endDate = (Long) configMap.get(END_DATE);
			jsonMap.put(START_DATE, startdate);
			jsonMap.put(END_DATE, endDate);
			jsonMap.put(ForesightConstants.ANALYTICAL_REPORT_KEY, analyticsrepoId);
		} catch (Exception e) {
			logger.info("exception inside method getJsonDataMap {}", Utils.getStackTrace(e));
		}
		return jsonMap;
	}

	private List<Integer> getwoidListFromWoData(List<GenericWorkorder> data) {
		List<Integer> woidList = new ArrayList<>();
		if (Utils.isValidList(data)) {
			for (GenericWorkorder wodata : data) {
				woidList.add(wodata.getId());
			}
		}
		return woidList;
	}

	private Response createSectorWiseExcelReport(List<SectorWiseSummaryWrapper> list,
			AnalyticsRepository analyticsRepository) {
		logger.info("Inside method createSectorWiseExcelReport");
		    String filePath = ConfigUtils.getString(ReportConstants.NV_REPORTS_PATH);
			String reportFilePath = writeData(list, filePath);
			logger.info("reportFilePath {}", reportFilePath);
			if (reportFilePath != null) {
				File file = ReportUtil.getIfFileExists(reportFilePath);
				String hdfsFilePath = ConfigUtils.getString(ReportConstants.NV_REPORTS_PATH_HDFS)
						+ ReportConstants.MASTER + ReportConstants.FORWARD_SLASH;
				return reportService.saveFile(analyticsRepository.getId(), hdfsFilePath, file);
			} else {
				analyticsRepositoryDao.updateStatusInAnalyticsRepository(analyticsRepository.getId(), null,
						"Data is Not Available", progress.Failed, null);
				return Response.ok(ForesightConstants.FAILURE_JSON).build();
			}		
	}

	private Map<Integer, String> getSummaryDataFromGWOMeta(List<Integer> workorderIds) {
		logger.info("Inside method getSummaryDataFromGWOMeta");
		Map<Integer, String> woWiseSummaryMap = new HashMap<Integer, String>();
		try {
			for (int woid : workorderIds) {
				GenericWorkorder workOrder = iGenericWorkorderDao.findByPk(woid);
				Map<String, String> gwoMeta = workOrder.getGwoMeta();
				woWiseSummaryMap.put(woid,
						gwoMeta.get(QMDLConstant.SECTORWISE_SUMMARY_JSON) != null
								? gwoMeta.get(QMDLConstant.SECTORWISE_SUMMARY_JSON)
								: null);
			}
		} catch (Exception e) {
			logger.info("exception inside method getSummaryDataFromGWOMeta {}", Utils.getStackTrace(e));
		}
		return woWiseSummaryMap;
	}

	private List<SectorWiseSummaryWrapper> generateSectorWiseReport(Map<Integer, String> woWiseSummaryMap) {
		logger.info("Inside method generateSectorWiseReport");
		List<SectorWiseSummaryWrapper> list = new ArrayList<>();
		if (woWiseSummaryMap != null && !woWiseSummaryMap.isEmpty()) {
			try {
				for (Entry<Integer, String> summaryWiseMap : woWiseSummaryMap.entrySet()) {
					String json = summaryWiseMap.getValue();
					if (json != null && !json.isEmpty()) {
						List<SiteInfoWrapper> summarydata = new Gson().fromJson(json,
								new TypeToken<List<SiteInfoWrapper>>() {
								}.getType());
						GenericWorkorder workOrder = iGenericWorkorderDao.findByPk(summaryWiseMap.getKey());
						list.addAll(addSummaryDataToWrapper(summarydata, workOrder));
					}
				}
			} catch (Exception e) {
				logger.info("exception inside method generateSectorWiseReport {}", Utils.getStackTrace(e));
			}
		}
		return list;
	}

	private List<SectorWiseSummaryWrapper> addSummaryDataToWrapper(List<SiteInfoWrapper> summarydata, GenericWorkorder workOrder) {
		logger.info("Inside method addSummaryDataToWrapper");
		List<SectorWiseSummaryWrapper> listOfSummaryWrapper = new ArrayList<SectorWiseSummaryWrapper>();
		for (SiteInfoWrapper summary : summarydata) {
			try {
				if (Utils.isValidList(summary.getModelAlpha()) && Utils.isValidList(summary.getAlpha())) {
					addAllKpisToWrapper(summary.getModelAlpha(), summary, listOfSummaryWrapper, ALPHA,
							workOrder);
					addFeebackDataToWrapper(summary.getAlpha(), summary, listOfSummaryWrapper, ALPHA, workOrder);
				}
				if (Utils.isValidList(summary.getModelBeta()) && Utils.isValidList(summary.getBeta())) {
					addAllKpisToWrapper(summary.getModelBeta(), summary, listOfSummaryWrapper, BETA, workOrder);
					addFeebackDataToWrapper(summary.getBeta(), summary, listOfSummaryWrapper, BETA, workOrder);
				}
				if (Utils.isValidList(summary.getModelGamma()) && Utils.isValidList(summary.getGamma())) {
					addAllKpisToWrapper(summary.getModelGamma(), summary, listOfSummaryWrapper, GAMMA,
							workOrder);
					addFeebackDataToWrapper(summary.getGamma(), summary, listOfSummaryWrapper, GAMMA, workOrder);
				}
				setOverAllStatus(listOfSummaryWrapper,summary);
			} catch (Exception e) {
				logger.info("exception Inside method addSummaryDataToWrapper {} ", Utils.getStackTrace(e));
			}
		}
		return listOfSummaryWrapper;
	}

	private void addFeebackDataToWrapper(List<SectorWiseWrapper> summaryData, SiteInfoWrapper siteWrapper,
			List<SectorWiseSummaryWrapper> listOfSummaryWrapper, String discriptior, GenericWorkorder workOrder) {
		logger.info("Inside method addFeebackDataToWrapper");
		try {
			for (SectorWiseWrapper data : summaryData) {
				ConsumerFeedback feedBack = getFeedbackData(data);
				setVoiceFeedbackToList(siteWrapper, listOfSummaryWrapper, discriptior, workOrder, feedBack);
				setVideoFeedbackToList(siteWrapper, listOfSummaryWrapper, discriptior, workOrder, feedBack);
				setMessagingFeedbackToList(siteWrapper, listOfSummaryWrapper, discriptior, workOrder, feedBack);
			}
		} catch (Exception e) {
			logger.info("Exception inside method setfeedbackdatatowrapper {}", Utils.getStackTrace(e));
		}
	}

	private void setMessagingFeedbackToList(SiteInfoWrapper siteWrapper,
			List<SectorWiseSummaryWrapper> listOfSummaryWrapper, String discriptior, GenericWorkorder workOrder,
			ConsumerFeedback feedBack) {
		if (feedBack.getStarRatingMessagingRcs() != null) {
			SectorWiseSummaryWrapper wrapper = new SectorWiseSummaryWrapper();
			logger.info("for messagfb {}", feedBack.getStarRatingMessagingRcs());
			wrapper.setResultSummary(feedBack.getStarRatingMessagingRcs().toString());
			setSectorWiseDataWrapper(siteWrapper, wrapper, "RCS", "5", "Static Tests", workOrder,
					discriptior, "Messaging RCS", "FEEDBACK RCS", 3.0, "");
			listOfSummaryWrapper.add(wrapper);
		}
	}

	private void setVideoFeedbackToList(SiteInfoWrapper siteWrapper,
			List<SectorWiseSummaryWrapper> listOfSummaryWrapper, String discriptior, GenericWorkorder workOrder,
			ConsumerFeedback feedBack) {
		if (feedBack.getStarRatingVideoRcs() != null) {
			SectorWiseSummaryWrapper wrapper = new SectorWiseSummaryWrapper();
			logger.info("for videofb {}", feedBack.getStarRatingVideoRcs());
			wrapper.setResultSummary(feedBack.getStarRatingVideoRcs().toString());
			setSectorWiseDataWrapper(siteWrapper, wrapper, "RCS", "5", "Static Tests", workOrder,
					discriptior, "Video RCS", "FEEDBACK RCS", 3.0, "");
			listOfSummaryWrapper.add(wrapper);
		}
	}

	private void setVoiceFeedbackToList(SiteInfoWrapper siteWrapper,
			List<SectorWiseSummaryWrapper> listOfSummaryWrapper, String discriptior, GenericWorkorder workOrder,
			ConsumerFeedback feedBack) {
		if (feedBack.getStarRatingVoiceRcs() != null) {
			SectorWiseSummaryWrapper wrapper = new SectorWiseSummaryWrapper();
			logger.info("for voicefb {}", feedBack.getStarRatingVoiceRcs());
			wrapper.setResultSummary(feedBack.getStarRatingVoiceRcs().toString());
			setSectorWiseDataWrapper(siteWrapper, wrapper, "RCS", "5", "Static Tests", workOrder,
					discriptior, "Voice RCS", "FEEDBACK RCS", 3.0, "");
			listOfSummaryWrapper.add(wrapper);
		}
	}

	private void addAllKpisToWrapper(List<SectorWiseWrapper> summaryData, SiteInfoWrapper siteWrapper,
			List<SectorWiseSummaryWrapper> listOfSummaryWrapper, String discriptior, GenericWorkorder workOrder) {
		logger.info("Inside method addAllKpisToWrapper");
		for (SectorWiseWrapper data : summaryData) {
			try {
				setRSRPToList(siteWrapper, listOfSummaryWrapper, discriptior, workOrder, data);
				setSINRToList(siteWrapper, listOfSummaryWrapper, discriptior, workOrder, data);

				setOverlappingServerToList(siteWrapper, listOfSummaryWrapper, discriptior, workOrder, data);
				setAvgMacDlToList(siteWrapper, listOfSummaryWrapper, discriptior, workOrder, data);
				setMinMacDlToList(siteWrapper, listOfSummaryWrapper, discriptior, workOrder, data);
				setMaxMacDlToList(siteWrapper, listOfSummaryWrapper, discriptior, workOrder, data);
				setMacDlPercentageToList(siteWrapper, listOfSummaryWrapper, discriptior, workOrder, data);
				
				setHandOverSusscessRateToList(siteWrapper,listOfSummaryWrapper,discriptior,workOrder);
				setHandOverInterruptionTimeToList(siteWrapper,listOfSummaryWrapper,discriptior,workOrder);

				setAvgMacUlToList(siteWrapper, listOfSummaryWrapper, discriptior, workOrder, data);
				setMinMacUlToList(siteWrapper, listOfSummaryWrapper, discriptior, workOrder, data);
				setMaxMacUlToList(siteWrapper, listOfSummaryWrapper, discriptior, workOrder, data);
				setMacUlPercentageToList(siteWrapper, listOfSummaryWrapper, discriptior, workOrder, data);

				setPingLatency32ToList(siteWrapper, listOfSummaryWrapper, discriptior, workOrder, data);
				setPingLatency1000ToList(siteWrapper, listOfSummaryWrapper, discriptior, workOrder, data);
				setPingLatency1500ToList(siteWrapper, listOfSummaryWrapper, discriptior, workOrder, data);

				setImsTimeToList(siteWrapper, listOfSummaryWrapper, discriptior, workOrder, data);
				setVolteCallTimeToList(siteWrapper, listOfSummaryWrapper, discriptior, workOrder, data);
				setVolteCallSetupTimeInterOperator(siteWrapper, listOfSummaryWrapper, discriptior, workOrder, data);
				setVolteCallSetupSuccessRateToList(siteWrapper, listOfSummaryWrapper, discriptior, workOrder, data);
				setVolteCallSetupSuccessRateInterOperator(siteWrapper, listOfSummaryWrapper, discriptior, workOrder,data);
				setPLMNCallSetupSuccessRateToList(siteWrapper, listOfSummaryWrapper, discriptior, workOrder, data);
				
				setAttachSuccessRateToList(siteWrapper, listOfSummaryWrapper, discriptior, workOrder, data);
				setRachSuccessRateToList(siteWrapper, listOfSummaryWrapper, discriptior, workOrder, data);
				setDetachSuccessRateToList(siteWrapper, listOfSummaryWrapper, discriptior, workOrder, data);
				setAttachLatencyToList(siteWrapper, listOfSummaryWrapper, discriptior, workOrder, data);
				setRTPPacketLossToList(siteWrapper, listOfSummaryWrapper, discriptior, workOrder, data);
				setPageLoadTimeToList(siteWrapper, listOfSummaryWrapper, discriptior, workOrder, data);
				setYoutubeStallingToList(siteWrapper, listOfSummaryWrapper, discriptior, workOrder, data);
			} catch (Exception e) {
				logger.info("exception inside method addAllKpisToWrapper {} ", Utils.getStackTrace(e));
			}
		}
	}

	private void setHandOverSusscessRateToList(SiteInfoWrapper siteWrapper,
			List<SectorWiseSummaryWrapper> listOfSummaryWrapper, String discriptior, GenericWorkorder workOrder) {
		try {
		if(siteWrapper!=null) {
			if(siteWrapper.getHandoverSuccessRate()!=null && !siteWrapper.getHandoverSuccessRate().isEmpty()) {
				SectorWiseSummaryWrapper wrapper = new SectorWiseSummaryWrapper();
				wrapper.setResultSummary(siteWrapper.getHandoverSuccessRate());
				setSectorWiseDataWrapper(siteWrapper, wrapper, TAG_DATA, "Info Only", MOBILITY_TEST, workOrder,
						discriptior, "Handover Success Rate", PROCEDURE_DL_FTP+"+"+PROCEDURE_UL_FTP, 0.0,
						null);
				listOfSummaryWrapper.add(wrapper);
				
			}
		}}catch(Exception e) {
			logger.info("Exception in setting handoverdata {}",Utils.getStackTrace(e));
		}
	}
	
	private void setHandOverInterruptionTimeToList(SiteInfoWrapper siteWrapper,
			List<SectorWiseSummaryWrapper> listOfSummaryWrapper, String discriptior, GenericWorkorder workOrder) {
		try {
		if(siteWrapper!=null) {
			if(siteWrapper.getHandoverInterruptionTime()!=null && !siteWrapper.getHandoverInterruptionTime().isEmpty()) {
				SectorWiseSummaryWrapper wrapper = new SectorWiseSummaryWrapper();
				wrapper.setResultSummary(siteWrapper.getHandoverInterruptionTime());
				setSectorWiseDataWrapper(siteWrapper, wrapper, TAG_DATA, "<=75 ms", MOBILITY_TEST, workOrder,
						discriptior, "Handover Interruption Time (ms)", PROCEDURE_DL_FTP+"+"+PROCEDURE_UL_FTP, 0.0,
						null);
				listOfSummaryWrapper.add(wrapper);
				
			}
		}}catch(Exception e) {
			logger.info("Exception in setting handoverdata {}",Utils.getStackTrace(e));
		}
		
	}

	private void setYoutubeStallingToList(SiteInfoWrapper siteWrapper,
			List<SectorWiseSummaryWrapper> listOfSummaryWrapper, String discriptior, GenericWorkorder workOrder,
			SectorWiseWrapper data) {
		if (data.getYoutubeStalling() != null && !data.getYoutubeStalling().isEmpty()) {
			SectorWiseSummaryWrapper wrapper = new SectorWiseSummaryWrapper();
			wrapper.setResultSummary(data.getYoutubeStalling());
			setSectorWiseDataWrapper(siteWrapper, wrapper, "video", "0", STATIC_TEST, workOrder,
					discriptior, "Stalling Count", "Youtube Streaming", 0.0, "");
			listOfSummaryWrapper.add(wrapper);
		}
	}

	private void setPageLoadTimeToList(SiteInfoWrapper siteWrapper, List<SectorWiseSummaryWrapper> listOfSummaryWrapper,
			String discriptior, GenericWorkorder workOrder, SectorWiseWrapper data) {
		if (data.getPageLoadTime() != null && !data.getPageLoadTime().isEmpty()) {
			SectorWiseSummaryWrapper wrapper = new SectorWiseSummaryWrapper();
			wrapper.setResultSummary(data.getPageLoadTime());
			setSectorWiseDataWrapper(siteWrapper, wrapper, "Web Browsing", "<=10", STATIC_TEST,
					workOrder, discriptior, "Page Load Time (sec)", "HTTP Browsing", 0.0, "");
			listOfSummaryWrapper.add(wrapper);
		}
	}

	private void setRTPPacketLossToList(SiteInfoWrapper siteWrapper,
			List<SectorWiseSummaryWrapper> listOfSummaryWrapper, String discriptior, GenericWorkorder workOrder,
			SectorWiseWrapper data) {
		if (data.getVoLTERTRPktLoss() != null && !data.getVoLTERTRPktLoss().isEmpty()) {
			SectorWiseSummaryWrapper wrapper = new SectorWiseSummaryWrapper();
			wrapper.setResultSummary(data.getVoLTERTRPktLoss());
			setSectorWiseDataWrapper(siteWrapper, wrapper, TAG_VOLTE, "Info Only", STATIC_TEST, workOrder,
					discriptior, "VoLTE RTP Packet loss Rate <1%", VOLTE_SHORT_CALL, 1.0,
					ReportConstants.LESS_THAN_SYMBOL);
			listOfSummaryWrapper.add(wrapper);
		}
	}

	private void setAttachLatencyToList(SiteInfoWrapper siteWrapper,
			List<SectorWiseSummaryWrapper> listOfSummaryWrapper, String discriptior, GenericWorkorder workOrder,
			SectorWiseWrapper data) {
		if (data.getAttachLatency() != null && !data.getAttachLatency().isEmpty()) {
			SectorWiseSummaryWrapper wrapper = new SectorWiseSummaryWrapper();
			wrapper.setResultSummary(data.getAttachLatency());
			setSectorWiseDataWrapper(siteWrapper, wrapper, TAG_VOLTE, "Info Only", STATIC_TEST, workOrder,
					discriptior, "Attach Latency (ms)", VOLTE_SHORT_CALL, 0.0, null);
			listOfSummaryWrapper.add(wrapper);
		}
	}

	private void setDetachSuccessRateToList(SiteInfoWrapper siteWrapper,
			List<SectorWiseSummaryWrapper> listOfSummaryWrapper, String discriptior, GenericWorkorder workOrder,
			SectorWiseWrapper data) {
		if (data.getDetachSuccessRate() != null && !data.getDetachSuccessRate().isEmpty()) {
			SectorWiseSummaryWrapper wrapper = new SectorWiseSummaryWrapper();
			wrapper.setResultSummary(data.getDetachSuccessRate());
			setSectorWiseDataWrapper(siteWrapper, wrapper, TAG_VOLTE, "", STATIC_TEST, workOrder,
					discriptior, "Detach Success Rate (Success/Attempt)", VOLTE_SHORT_CALL, 0.0, "");
			listOfSummaryWrapper.add(wrapper);
		}
	}

	private void setRachSuccessRateToList(SiteInfoWrapper siteWrapper,
			List<SectorWiseSummaryWrapper> listOfSummaryWrapper, String discriptior, GenericWorkorder workOrder,
			SectorWiseWrapper data) {
		if (data.getRachSuccessRate() != null && !data.getRachSuccessRate().isEmpty()) {
			SectorWiseSummaryWrapper wrapper = new SectorWiseSummaryWrapper();
			wrapper.setResultSummary(data.getRachSuccessRate());
			setSectorWiseDataWrapper(siteWrapper, wrapper, TAG_VOLTE, "",STATIC_TEST, workOrder,
					discriptior, "RACH Success Rate (Success/Attempt)", VOLTE_SHORT_CALL, 0.0, "");
			listOfSummaryWrapper.add(wrapper);
		}
	}

	private void setAttachSuccessRateToList(SiteInfoWrapper siteWrapper,
			List<SectorWiseSummaryWrapper> listOfSummaryWrapper, String discriptior, GenericWorkorder workOrder,
			SectorWiseWrapper data) {
		if (data.getAttachSuccessRate() != null && !data.getAttachSuccessRate().isEmpty()) {
			SectorWiseSummaryWrapper wrapper = new SectorWiseSummaryWrapper();
			wrapper.setResultSummary(data.getAttachSuccessRate());
			setSectorWiseDataWrapper(siteWrapper, wrapper, TAG_VOLTE, "", STATIC_TEST, workOrder,
					discriptior, "Attach Success Rate (Success/Attempt)", VOLTE_SHORT_CALL, 0.0, "");
			listOfSummaryWrapper.add(wrapper);
		}
	}

	private void setVolteCallSetupSuccessRateToList(SiteInfoWrapper siteWrapper,
			List<SectorWiseSummaryWrapper> listOfSummaryWrapper, String discriptior, GenericWorkorder workOrder,
			SectorWiseWrapper data) {
		if (data.getVoLTECallSetup() != null && !data.getVoLTECallSetup().isEmpty()) {
			SectorWiseSummaryWrapper wrapper = new SectorWiseSummaryWrapper();
			logger.info("for voltecallsetup {}", data.getVoLTECallSetup());
			wrapper.setResultSummary(data.getVoLTECallSetup());
			setSectorWiseDataWrapper(siteWrapper, wrapper, TAG_VOLTE, "Info Only",STATIC_TEST, workOrder,
					discriptior, "VoLTE Call Setup Success Rate", VOLTE_SHORT_CALL, 0.0, null);
			listOfSummaryWrapper.add(wrapper);
		}
	}
	
	private void setVolteCallTimeToList(SiteInfoWrapper siteWrapper,
			List<SectorWiseSummaryWrapper> listOfSummaryWrapper, String discriptior, GenericWorkorder workOrder,
			SectorWiseWrapper data) {
		if (data.getCallSetupTime() != null && !data.getCallSetupTime().isEmpty()) {
			SectorWiseSummaryWrapper wrapper = new SectorWiseSummaryWrapper();
			logger.info("for voltecallsetup {}", data.getCallSetupTime());
			wrapper.setResultSummary(data.getCallSetupTime());
			setSectorWiseDataWrapper(siteWrapper, wrapper, TAG_VOLTE, "<=4.8 sec",STATIC_TEST, workOrder,
					discriptior, "VoLTE Call Setup Time (Sec)", VOLTE_SHORT_CALL, 0.0, null);
			listOfSummaryWrapper.add(wrapper);
		}
	}

	private void setPLMNCallSetupSuccessRateToList(SiteInfoWrapper siteWrapper,
			List<SectorWiseSummaryWrapper> listOfSummaryWrapper, String discriptior, GenericWorkorder workOrder,
			SectorWiseWrapper data) {
		if (data.getPlmnCallSetupSuccessRate() != null && !data.getPlmnCallSetupSuccessRate().isEmpty()) {
			SectorWiseSummaryWrapper wrapper = new SectorWiseSummaryWrapper();
			logger.info("for PlmnCallSetupSuccessRate {}", data.getPlmnCallSetupSuccessRate());
			wrapper.setResultSummary(data.getPlmnCallSetupSuccessRate());
			setSectorWiseDataWrapper(siteWrapper, wrapper, TAG_VOLTE, "Info Only", STATIC_TEST, workOrder,
					discriptior, " VoLTE Call Set up Success Rate(VoLTE-PLMN)", VOLTE_SHORT_CALL, 0.0, null);
			listOfSummaryWrapper.add(wrapper);
		}
	}

	private void setVolteCallSetupSuccessRateInterOperator(SiteInfoWrapper siteWrapper,
			List<SectorWiseSummaryWrapper> listOfSummaryWrapper, String discriptior, GenericWorkorder workOrder,
			SectorWiseWrapper data) {
		if (data.getInterCallSetupSuccessRate() != null && !data.getInterCallSetupSuccessRate().isEmpty()) {
			SectorWiseSummaryWrapper wrapper = new SectorWiseSummaryWrapper();
			logger.info("for intercallsetup {}", data.getInterCallSetupSuccessRate());
			wrapper.setResultSummary(data.getInterCallSetupSuccessRate());
			setSectorWiseDataWrapper(siteWrapper, wrapper, TAG_VOLTE, "Info Only", STATIC_TEST, workOrder,
					discriptior, "VoLTE Call Set up Success Rate (Inter-Operator)", VOLTE_SHORT_CALL, 0.0,
					null);
			listOfSummaryWrapper.add(wrapper);
		}
	}
	
	private void setVolteCallSetupTimeInterOperator(SiteInfoWrapper siteWrapper,
			List<SectorWiseSummaryWrapper> listOfSummaryWrapper, String discriptior, GenericWorkorder workOrder,
			SectorWiseWrapper data) {
		if (data.getInterCallSetupTime() != null && !data.getInterCallSetupTime().isEmpty()) {
			SectorWiseSummaryWrapper wrapper = new SectorWiseSummaryWrapper();
			logger.info("for intercallsetup time {}", data.getInterCallSetupTime());
			wrapper.setResultSummary(data.getInterCallSetupTime());
			setSectorWiseDataWrapper(siteWrapper, wrapper, TAG_VOLTE, "Info Only", STATIC_TEST, workOrder,
					discriptior, "VoLTE Call Setup Time (Inter-Operator)", VOLTE_SHORT_CALL, 0.0,
					null);
			listOfSummaryWrapper.add(wrapper);
		}
	}

	private void setImsTimeToList(SiteInfoWrapper siteWrapper, List<SectorWiseSummaryWrapper> listOfSummaryWrapper,
			String discriptior, GenericWorkorder workOrder, SectorWiseWrapper data) {
		if (data.getImsRegistration() != null && !data.getImsRegistration().isEmpty()) {
			SectorWiseSummaryWrapper wrapper = new SectorWiseSummaryWrapper();
			wrapper.setResultSummary(data.getImsRegistration());
			setSectorWiseDataWrapper(siteWrapper, wrapper, TAG_VOLTE, "", STATIC_TEST, workOrder,
					discriptior, " IMS Registration Setup Time (Sec) <1.5", VOLTE_SHORT_CALL, 0.0, null);
			listOfSummaryWrapper.add(wrapper);
		}
	}

	private void setPingLatency1500ToList(SiteInfoWrapper siteWrapper,
			List<SectorWiseSummaryWrapper> listOfSummaryWrapper, String discriptior, GenericWorkorder workOrder,
			SectorWiseWrapper data) {
		if (data.getLatency1500Bytes() != null && !data.getLatency1500Bytes().isEmpty()) {
			SectorWiseSummaryWrapper wrapper = new SectorWiseSummaryWrapper();
			wrapper.setResultSummary(data.getLatency1500Bytes());
			setSectorWiseDataWrapper(siteWrapper, wrapper, TAG_PING, "<=100 ms", STATIC_TEST, workOrder,
					discriptior, "Ping Latency(ms) 1500 bytes", ICMP_PING, 100.0,
					ReportConstants.LESS_THAN_SYMBOL);
			listOfSummaryWrapper.add(wrapper);
		}
	}

	private void setPingLatency1000ToList(SiteInfoWrapper siteWrapper,
			List<SectorWiseSummaryWrapper> listOfSummaryWrapper, String discriptior, GenericWorkorder workOrder,
			SectorWiseWrapper data) {
		if (data.getLatency1000Bytes() != null && !data.getLatency1000Bytes().isEmpty()) {
			SectorWiseSummaryWrapper wrapper = new SectorWiseSummaryWrapper();
			wrapper.setResultSummary(data.getLatency1000Bytes());
			setSectorWiseDataWrapper(siteWrapper, wrapper, TAG_PING, "<=100 ms", STATIC_TEST, workOrder,
					discriptior, "Ping Latency(ms) 1000 bytes", ICMP_PING, 100.0,
					ReportConstants.LESS_THAN_SYMBOL);
			listOfSummaryWrapper.add(wrapper);
		}
	}

	private void setPingLatency32ToList(SiteInfoWrapper siteWrapper,
			List<SectorWiseSummaryWrapper> listOfSummaryWrapper, String discriptior, GenericWorkorder workOrder,
			SectorWiseWrapper data) {
		if (data.getLatency32Bytes() != null && !data.getLatency32Bytes().isEmpty()) {
			SectorWiseSummaryWrapper wrapper = new SectorWiseSummaryWrapper();
			wrapper.setResultSummary(data.getLatency32Bytes());
			setSectorWiseDataWrapper(siteWrapper, wrapper, TAG_PING, "<=100 ms", STATIC_TEST, workOrder,
					discriptior, "Ping Latency(ms) 32 bytes", ICMP_PING, 100.0,
					ReportConstants.LESS_THAN_SYMBOL);
			listOfSummaryWrapper.add(wrapper);
		}
	}

	private void setMacUlPercentageToList(SiteInfoWrapper siteWrapper,
			List<SectorWiseSummaryWrapper> listOfSummaryWrapper, String discriptior, GenericWorkorder workOrder,
			SectorWiseWrapper data) {
		if (data.getMacUlPercentage() != null && !data.getMacUlPercentage().isEmpty()) {
			SectorWiseSummaryWrapper wrapper = new SectorWiseSummaryWrapper();
			wrapper.setResultSummary(data.getMacUlPercentage());
			setSectorWiseDataWrapper(siteWrapper, wrapper, TAG_DATA, ">=95% Samples",MOBILITY_TEST,
					workOrder, discriptior, " MAC UL Thp. >= 512(20 MHz)/128 (5 MHz)\n Kbps", PROCEDURE_UL_FTP, 95.0,
					ReportConstants.GREATER_THAN);
			listOfSummaryWrapper.add(wrapper);
		}
	}

	private void setMaxMacUlToList(SiteInfoWrapper siteWrapper, List<SectorWiseSummaryWrapper> listOfSummaryWrapper,
			String discriptior, GenericWorkorder workOrder, SectorWiseWrapper data) {
		if (data.getMaxMacUl() != null && !data.getMaxMacUl().isEmpty()) {
			SectorWiseSummaryWrapper wrapper = new SectorWiseSummaryWrapper();
			wrapper.setResultSummary(data.getMaxMacUl());
			setSectorWiseDataWrapper(siteWrapper, wrapper, TAG_DATA, "Info Only", MOBILITY_TEST,
					workOrder, discriptior, "MAX MAC UL THROUGHPUT (mpbs)", PROCEDURE_UL_FTP, 0.0, null);
			listOfSummaryWrapper.add(wrapper);
		}
	}

	private void setMinMacUlToList(SiteInfoWrapper siteWrapper, List<SectorWiseSummaryWrapper> listOfSummaryWrapper,
			String discriptior, GenericWorkorder workOrder, SectorWiseWrapper data) {
		if (data.getMinMacUl() != null && !data.getMinMacUl().isEmpty()) {
			SectorWiseSummaryWrapper wrapper = new SectorWiseSummaryWrapper();
			wrapper.setResultSummary(data.getMinMacUl());
			setSectorWiseDataWrapper(siteWrapper, wrapper, TAG_DATA, "Info Only", MOBILITY_TEST,
					workOrder, discriptior, "MIN MAC UL THROUGHPUT (mpbs)", PROCEDURE_UL_FTP, 0.0, null);
			listOfSummaryWrapper.add(wrapper);
		}
	}

	private void setAvgMacUlToList(SiteInfoWrapper siteWrapper, List<SectorWiseSummaryWrapper> listOfSummaryWrapper,
			String discriptior, GenericWorkorder workOrder, SectorWiseWrapper data) {
		if (data.getAvgMacUl() != null && !data.getAvgMacUl().isEmpty()) {
			SectorWiseSummaryWrapper wrapper = new SectorWiseSummaryWrapper();
			wrapper.setResultSummary(data.getAvgMacUl());
			setSectorWiseDataWrapper(siteWrapper, wrapper, TAG_DATA, "Info Only", MOBILITY_TEST,
					workOrder, discriptior, "AVG MAC UL THROUGHPUT (mpbs)", PROCEDURE_UL_FTP, 0.0, null);
			listOfSummaryWrapper.add(wrapper);
		}
	}

	private void setMacDlPercentageToList(SiteInfoWrapper siteWrapper,
			List<SectorWiseSummaryWrapper> listOfSummaryWrapper, String discriptior, GenericWorkorder workOrder,
			SectorWiseWrapper data) {
		if (data.getMacDlPercentage() != null && !data.getMacDlPercentage().isEmpty()) {
			SectorWiseSummaryWrapper wrapper = new SectorWiseSummaryWrapper();
			wrapper.setResultSummary(data.getMacDlPercentage());
			setSectorWiseDataWrapper(siteWrapper, wrapper, TAG_DATA, ">=95% Samples", MOBILITY_TEST,
					workOrder, discriptior, " MAC DL Thp. >= 4.5(20 MHz)/1.5 (5 MHz) Mbps", PROCEDURE_DL_FTP, 95.0,
					ReportConstants.GREATER_THAN);
			listOfSummaryWrapper.add(wrapper);
		}
	}

	private void setMaxMacDlToList(SiteInfoWrapper siteWrapper, List<SectorWiseSummaryWrapper> listOfSummaryWrapper,
			String discriptior, GenericWorkorder workOrder, SectorWiseWrapper data) {
		if (data.getMaxMacDl() != null && !data.getMaxMacDl().isEmpty()) {
			SectorWiseSummaryWrapper wrapper = new SectorWiseSummaryWrapper();
			wrapper.setResultSummary(data.getMaxMacDl());
			setSectorWiseDataWrapper(siteWrapper, wrapper, TAG_DATA, "Info Only",MOBILITY_TEST,
					workOrder, discriptior, "MAX MAC DL THROUGHPUT (mpbs)", PROCEDURE_DL_FTP, 0.0, null);
			listOfSummaryWrapper.add(wrapper);
		}
	}

	private void setMinMacDlToList(SiteInfoWrapper siteWrapper, List<SectorWiseSummaryWrapper> listOfSummaryWrapper,
			String discriptior, GenericWorkorder workOrder, SectorWiseWrapper data) {
		if (data.getMinMacDl() != null && !data.getMinMacDl().isEmpty()) {
			SectorWiseSummaryWrapper wrapper = new SectorWiseSummaryWrapper();
			wrapper.setResultSummary(data.getMinMacDl());
			setSectorWiseDataWrapper(siteWrapper, wrapper, TAG_DATA, "Info Only", MOBILITY_TEST,
					workOrder, discriptior, "MIN MAC DL THROUGHPUT (mpbs)", PROCEDURE_DL_FTP, 0.0, null);
			listOfSummaryWrapper.add(wrapper);
		}
	}

	private void setAvgMacDlToList(SiteInfoWrapper siteWrapper, List<SectorWiseSummaryWrapper> listOfSummaryWrapper,
			String discriptior, GenericWorkorder workOrder, SectorWiseWrapper data) {
		if (data.getAvgMacDl() != null && !data.getAvgMacDl().isEmpty()) {
			SectorWiseSummaryWrapper wrapper = new SectorWiseSummaryWrapper();
			wrapper.setResultSummary(data.getAvgMacDl());
			setSectorWiseDataWrapper(siteWrapper, wrapper, TAG_DATA, "Info Only", MOBILITY_TEST,
					workOrder, discriptior, "AVG MAC DL THROUGHPUT (mpbs)", PROCEDURE_DL_FTP, 0.0, null);
			listOfSummaryWrapper.add(wrapper);
		}
	}

	private void setOverlappingServerToList(SiteInfoWrapper siteWrapper,
			List<SectorWiseSummaryWrapper> listOfSummaryWrapper, String discriptior, GenericWorkorder workOrder,
			SectorWiseWrapper data) {
		if (data.getOverlappingServers() != null && !data.getOverlappingServers().isEmpty()) {
			SectorWiseSummaryWrapper wrapper = new SectorWiseSummaryWrapper();
			wrapper.setResultSummary(data.getOverlappingServers());
			if (wrapper.getResultSummary().contains("%")) {
				String percent = wrapper.getResultSummary().replace("%", "");
				wrapper.setResultSummary(percent);
			}
			setSectorWiseDataWrapper(siteWrapper, wrapper, TAG_DATA, "Info Only", MOBILITY_TEST,
					workOrder, discriptior, "Overlapping Servers <=3 & for >= 95% samples", PROCEDURE_DL_FTP, 95.0,
					ReportConstants.GREATER_THAN);
			listOfSummaryWrapper.add(wrapper);
		}
	}

	private void setSINRToList(SiteInfoWrapper siteWrapper, List<SectorWiseSummaryWrapper> listOfSummaryWrapper,
			String discriptior, GenericWorkorder workOrder, SectorWiseWrapper data) {
		if (data.getMobilitySinr() != null && !data.getMobilitySinr().isEmpty()) {
			SectorWiseSummaryWrapper wrapper = new SectorWiseSummaryWrapper();
			wrapper.setResultSummary(data.getMobilitySinr());
			setSectorWiseDataWrapper(siteWrapper, wrapper, TAG_DATA, ">= 2 dB",MOBILITY_TEST, workOrder,
					discriptior, "SINR (dB)", PROCEDURE_DL_FTP, 2.0, null);
			listOfSummaryWrapper.add(wrapper);
		}
	}

	private void setRSRPToList(SiteInfoWrapper siteWrapper, List<SectorWiseSummaryWrapper> listOfSummaryWrapper,
			String discriptior, GenericWorkorder workOrder, SectorWiseWrapper data) {
		if (data.getMobilityRsrp() != null && !data.getMobilityRsrp().isEmpty()) {
			SectorWiseSummaryWrapper wrapper = new SectorWiseSummaryWrapper();
			wrapper.setResultSummary(data.getMobilityRsrp());
			setSectorWiseDataWrapper(siteWrapper, wrapper, TAG_DATA, ">= -102 dBm", MOBILITY_TEST,
					workOrder, discriptior, "RSRP (dBm)", PROCEDURE_DL_FTP, -102.0, null);
			listOfSummaryWrapper.add(wrapper);
		}
	}

	private ConsumerFeedback getFeedbackData(SectorWiseWrapper data) {
		logger.info("Inside method getFeedbackData");
		List<ConsumerFeedback> list = new ArrayList<>();
		ConsumerFeedback feedback = new ConsumerFeedback();
		try {
			if (data.getPci() != null && data.getCellId() != null) {
				logger.info("pci  {}, cellid {}", data.getPci(), data.getCellId());
				list = consumerFeedbackDao.getPciWiseRating(Integer.parseInt(data.getPci()),
						Integer.parseInt(data.getCellId()));
			}
			if (list != null && list.isEmpty() && list.size() > 0) {
				feedback = list.get(0);
				logger.info("feedback data {} ", feedback);
			}
		} catch (Exception e) {
			logger.info("exception in fetching feedback data {}", Utils.getStackTrace(e));
		}
		return feedback;
	}

	private String addPassFailStatusToResult(Double result, Double theresholdvalue, String check) {
		logger.info("Inside method addPassFailStatusToResult");

		String status = "";
		if (result != null) {
			if (check != null && !check.isEmpty() && check.equalsIgnoreCase(ReportConstants.GREATER_THAN)) {
				if (result > theresholdvalue) {
					status = "PASS";
					return status;
				} else {
					status = "FAIL";
					return status;
				}
			}
			if (check != null && !check.isEmpty() && check.equalsIgnoreCase(ReportConstants.LESS_THAN_SYMBOL)) {
				if (result < theresholdvalue) {
					status = "PASS";
					return status;
				} else {
					status = "FAIL";
					return status;
				}
			}
			if (check == null)
				status = "BLOCKED";
			return status;
		}
		return status;
	}

	private void setSectorWiseDataWrapper(SiteInfoWrapper siteWrapper, SectorWiseSummaryWrapper wrapper, String tag, String criteria, String suite, GenericWorkorder workOrder, String discriptor, String kpiName,
			String procedure, Double threshold, String check) {
		try {
		wrapper.setWoId(workOrder.getWorkorderId()!=null?workOrder.getWorkorderId().toString():null);
		wrapper.setActive("TRUE");
		wrapper.setStatus(STRING_NEW);
		wrapper.setSiteId(siteWrapper.getSiteId());
		wrapper.setSiteName(siteWrapper.getSiteName());
		wrapper.setDiscription(discriptor + " " + kpiName);
		setTesterName(wrapper, workOrder);
		wrapper.setTags(tag);
		wrapper.setProcedure(procedure);
		wrapper.setSuite(suite);
		wrapper.setTargetValue(criteria);
		wrapper.setKpiName(kpiName);
		if (wrapper.getResultSummary() != null && !wrapper.getResultSummary().isEmpty()
				&& wrapper.getResultSummary().contains("PASS")) {
			wrapper.setTestCaseStatus("PASSED");
		} else if (wrapper.getResultSummary() != null && !wrapper.getResultSummary().isEmpty()
				&& wrapper.getResultSummary().contains("FAIL")) {
			wrapper.setTestCaseStatus("FAILED");
		} else if (wrapper.getResultSummary() != null && !wrapper.getResultSummary().isEmpty()) {
			String status = addPassFailStatusToResult(Double.parseDouble(wrapper.getResultSummary()), threshold, check);
			wrapper.setTestCaseStatus(status);
		}}catch(Exception e) {
			logger.info("exception inside method setSectorWiseDataWrapper {} ", Utils.getStackTrace(e));
		}
	}

	private void setTesterName(SectorWiseSummaryWrapper wrapper, GenericWorkorder workOrder) {
		String name = "-";
		if (workOrder.getAssignedTo() != null) {
			if (workOrder.getAssignedTo().getFirstName() != null
					&& !workOrder.getAssignedTo().getFirstName().isEmpty()) {
				name = workOrder.getAssignedTo().getFirstName().toString();
				if (workOrder.getAssignedTo().getLastName() != null) {
					name = name + " " + workOrder.getAssignedTo().getLastName().toString();
				}
			}
		}
		wrapper.setTesterName(name);
	}
	
	private void setOverAllStatus(List<SectorWiseSummaryWrapper> list,SiteInfoWrapper summary) {
		SectorWiseSummaryWrapper wrapper = new SectorWiseSummaryWrapper();
		Map<String,String> woStatusMap = new HashMap<>();
		try {
		if(Utils.isValidList(list)) {
			for(SectorWiseSummaryWrapper sectorwisewrapper :list) {
			if(sectorwisewrapper.getTestCaseStatus()!=null);
			woStatusMap.put(sectorwisewrapper.getTestCaseStatus(), sectorwisewrapper.getWoId());
			}
		}
		if(woStatusMap!=null &&! woStatusMap.isEmpty()) {
			if(woStatusMap.containsKey("FAILED")) {
				setWoIdStatus("FAILED",woStatusMap,wrapper,summary,list);	
			}else {
				setWoIdStatus("PASSED",woStatusMap,wrapper,summary,list);
			}
		}}catch(Exception e) {
			logger.info("exception in setting overall status {}", Utils.getStackTrace(e));
		}
	}
	
	private void setWoIdStatus(String Status,Map<String,String> woStatusMap,SectorWiseSummaryWrapper wrapper,SiteInfoWrapper summary, List<SectorWiseSummaryWrapper> list) {
		wrapper.setDiscription("Overall Status");
		wrapper.setProcedure("Overall Status");
		wrapper.setTestCaseStatus(Status);
		wrapper.setActive("1");
		wrapper.setSuite("New");
		wrapper.setKpiName("Overall Status");
		wrapper.setTags("Overall");
		wrapper.setSiteId(summary.getSiteId());
		for(Entry<String,String> map : woStatusMap.entrySet()) {
			wrapper.setWoId(map.getValue());
		}
		list.add(wrapper);
	}
	

	public String writeData(List<SectorWiseSummaryWrapper> usersList, String filePath) throws JSONException {
		logger.info("Inside writeData");
		String date = new SimpleDateFormat(UmConstants.YYYY_MM_DD_HH_MM).format(new Date());
		String path = "SectorWiseReport" + date + ".xlsx";
		String targetPath = filePath + path;
		XSSFWorkbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Created_User");
		int rowNum = 1;
		final String[] header = { "active ", "description", "identifier", "owner", "passfail_criteria", "phase",
				"procedure", "result", "result_summary", "setup", "suite", "technical_notes", "title", "priority",
				"external_identifier", "complexity", "project_name", "status", "testcase_state_enum_name", "tags" };
		Row headerrow = sheet.createRow(0);
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontName("TIMES_ROMAN");
		font.setFontHeight(10);
		XSSFCellStyle rowStyle = ExcelReportUtils.getCellStyle(workbook, Color.BLACK, Color.YELLOW, BorderStyle.THIN,
				font, true, true, true, true, HorizontalAlignment .CENTER);
		for (int i = 0; i < header.length; i++) {
			sheet.setColumnWidth(i, 12 * 256);
			Cell cell = headerrow.createCell(i);
			cell.setCellValue(header[i]);
			cell.setCellStyle(rowStyle);
		}
		setDataInExcel(usersList, sheet, rowNum);
		writeExcelFile(filePath, path, targetPath, workbook);
		return targetPath;
	}

	private void writeExcelFile(String filePath, String path, String targetPath, XSSFWorkbook workbook) {
		FileOutputStream out;
		try {
			if (filePath != null) {
				out = writeFileIntoFolder(filePath, path);
			} else {
				out = new FileOutputStream(new File(targetPath));
			}
			workbook.write(out);
			out.close();
			logger.info("filepath {}", filePath);
		} catch (Exception e) {
			logger.error("Error while  creating  user request : {}", Utils.getStackTrace(e));
		}
	}

	private void setDataInExcel(List<SectorWiseSummaryWrapper> usersList, Sheet sheet, int rowNum) {
		Row row = null;
		if(usersList!=null && !usersList.isEmpty()) {
		for (SectorWiseSummaryWrapper wrapper : usersList) {
			row = sheet.createRow(rowNum);
			row.createCell(0).setCellValue(wrapper.getActive());
			row.createCell(1).setCellValue(wrapper.getDiscription());
			row.createCell(2).setCellValue(wrapper.getSiteId());
			row.createCell(3).setCellValue(wrapper.getTesterName());
			row.createCell(4).setCellValue(wrapper.getTargetValue());
			row.createCell(5).setCellValue(wrapper.getWoId());
			row.createCell(6).setCellValue(wrapper.getProcedure());
			row.createCell(7).setCellValue(wrapper.getResult());
			row.createCell(8).setCellValue(wrapper.getResultSummary());
			row.createCell(9).setCellValue("");
			row.createCell(10).setCellValue(wrapper.getSuite());
			row.createCell(11).setCellValue("");
			row.createCell(12).setCellValue(wrapper.getKpiName());
			row.createCell(13).setCellValue("");
			row.createCell(14).setCellValue(wrapper.getSiteName());
			row.createCell(15).setCellValue("");
			row.createCell(16).setCellValue("");
			row.createCell(17).setCellValue(wrapper.getStatus());
			row.createCell(18).setCellValue(wrapper.getTestCaseStatus());
			row.createCell(19).setCellValue(wrapper.getTags());
			rowNum++;
		}}
	}

	public static FileOutputStream writeFileIntoFolder(String filePath, String path) throws FileNotFoundException {
		FileOutputStream out;
		File directory = new File(filePath + "/CREATED_FILES_" + new SimpleDateFormat("ddMMyyyy").format(new Date()));
		if (!directory.exists()) {
			directory.mkdir();
			out = new FileOutputStream(filePath + "/" + path);
		} else {
			out = new FileOutputStream(filePath + "/" + path);
		}
		return out;
	}
}
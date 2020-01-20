package com.inn.foresight.module.nv.report.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.core.generic.exceptions.application.BusinessException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.report.dao.IAnalyticsRepositoryDao;
import com.inn.foresight.core.report.model.AnalyticsRepository;
import com.inn.foresight.module.nv.layer3.qmdlParser.wrappers.Layer3SummaryWrapper;
import com.inn.foresight.module.nv.layer3.qmdlParser.wrappers.NVL3CsvDataWrapper;
import com.inn.foresight.module.nv.layer3.service.parse.IAdhocLiveDriveReportGenerationService;
import com.inn.foresight.module.nv.layer3.service.parse.INVAdhocDriveProcessingService;
import com.inn.foresight.module.nv.report.service.ICustomerExperienceReportService;
import com.inn.foresight.module.nv.report.service.ICustomerExperinceBenchmarkReportService;
import com.inn.foresight.module.nv.report.service.IReportService;
import com.inn.foresight.module.nv.report.smf.wrapper.SmfWidgetWrapper;
import com.inn.foresight.module.nv.report.smf.wrapper.SmfWrapper;
import com.inn.foresight.module.nv.report.utils.ReportConstants;
import com.inn.foresight.module.nv.report.utils.ReportUtil;
import com.inn.foresight.module.nv.report.utils.StealthUtils;
import com.inn.foresight.module.nv.report.utils.ZipUtils;
import com.inn.foresight.module.nv.report.wrapper.GraphDataWrapper;
import com.inn.foresight.module.nv.report.wrapper.GraphWrapper;
import com.inn.foresight.module.nv.report.wrapper.ReportSubWrapper;
import com.inn.foresight.module.nv.report.wrapper.SMFReportWrapper;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRPptxExporter;

@Service("CustomerExperinceBenchmarkServiceImpl")
public class CustomerExperinceBenchmarkServiceImpl implements ICustomerExperinceBenchmarkReportService {
	/** The logger. */
	private static final Logger logger = LogManager.getLogger(CustomerExperinceBenchmarkServiceImpl.class);

	/** The mapper. */
	static ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private IReportService reportService;
	@Autowired
	private IAnalyticsRepositoryDao analyticsRepositoryDao;
	@Autowired
	private INVAdhocDriveProcessingService nvAdhocDriveProcessingService;
	@Autowired
	private ICustomerExperienceReportService customerExperienceService;
	@Autowired
	private IAdhocLiveDriveReportGenerationService adhocDriveReportGeneration;

	@Override
	public Response execute(String json){
		logger.info("Going to execute method for json {} ", json);
			String filePath=ConfigUtils.getString(ReportConstants.NV_REPORTS_PATH)+ReportConstants.SAUT+ReportConstants.FORWARD_SLASH+new Date().getTime()+ReportConstants.FORWARD_SLASH;
		ReportUtil.createDirectory(filePath);
	
		SMFReportWrapper mainWrapper = new SMFReportWrapper();
		List<ReportSubWrapper> subList = new ArrayList<>();
		try {
			SmfWrapper wrapperMain = mapper.readValue(json, SmfWrapper.class);
			logger.debug("wrapperMain {} ", wrapperMain.toString());
			List<SmfWidgetWrapper> smfWidgetWrapperList = wrapperMain.getReportData();

			if (smfWidgetWrapperList != null && smfWidgetWrapperList.size() > 1) {
				SmfWidgetWrapper wrapper = smfWidgetWrapperList.get(ReportConstants.INDEX_ONE);
				if (wrapper != null) {
					Boolean isMobilityTestAvailable = false;
					Boolean isStationaryTestAvailable = false;
					if (reportService.getFileProcessedStatusForWorkorders(Arrays.asList(wrapper.getMobility().getWorkorderId()))) {
						Layer3SummaryWrapper summary1 = new Layer3SummaryWrapper();
						Layer3SummaryWrapper summary2 = new Layer3SummaryWrapper();
						List<NVL3CsvDataWrapper> nvL3CsvList1 = nvAdhocDriveProcessingService
								.processAdhocFileForReport(wrapper.getMobility().getWorkorderId(), summary2);
						List<NVL3CsvDataWrapper> nvL3CsvList = nvAdhocDriveProcessingService.processAdhocFileForReport(
								smfWidgetWrapperList.get(ReportConstants.INDEX_ZER0).getMobility().getWorkorderId(),
								summary1);
						generateCsvFilesFromDriveData(filePath, smfWidgetWrapperList, wrapper, summary1, summary2,
								nvL3CsvList1, nvL3CsvList);
						ReportSubWrapper subWrapper = getGraphDataForDrive(summary1, summary2, nvL3CsvList1,
								nvL3CsvList);
						if(wrapper.getMobility() != null && wrapper.getMobility().getWorkorderId() != null && !wrapper.getMobility().getFloorPlan().isEmpty()) {
							logger.info("Mobility is not null");
							isMobilityTestAvailable = true;
						}
						if(wrapper.getStationary() != null && !wrapper.getStationary().isEmpty()) {
							logger.info("Stationary is not null");
							isStationaryTestAvailable = true;
						}
						setDataInWrapperForReport(filePath, mainWrapper, subList, wrapperMain, smfWidgetWrapperList,
								wrapper, summary1, summary2, subWrapper);
						createReportAndSavezipFileToHDFS(filePath, mainWrapper, wrapperMain, isMobilityTestAvailable, isStationaryTestAvailable);
					}
				}
			}
		} catch (Exception e) {
			logger.info("Unable to Genearate the report reason is {} ", Utils.getStackTrace(e));
			Response.ok(ForesightConstants.FAILURE_JSON).build();
		}
		return Response.ok(ForesightConstants.FAILURE_JSON).build();
	}

	private void createReportAndSavezipFileToHDFS(String filePath, SMFReportWrapper mainWrapper, SmfWrapper wrapperMain, Boolean isMobilityTestAvailable, Boolean isStationaryTestAvailable)
			throws IOException {
		String destFileName = filePath+mainWrapper.getReportName();
		File destDir = new File(filePath);
		proceedToCreateReport(mainWrapper,destFileName, isMobilityTestAvailable, isStationaryTestAvailable);
		String zipFilePath=ConfigUtils.getString(ReportConstants.NV_REPORTS_PATH)+ReportConstants.FORWARD_SLASH+mainWrapper.getReportName()+ReportConstants.UNDERSCORE+wrapperMain.getAnalyticsrepositoryid_pk()+ReportConstants.DOT_ZIP;
		logger.info("zipFilePath {}",zipFilePath);
		ZipUtils.zip(destDir.listFiles(), zipFilePath);
		String hdfsFilePath = ConfigUtils.getString(ReportConstants.NV_REPORTS_PATH_HDFS) + ReportConstants.MASTER + ReportConstants.FORWARD_SLASH;
		reportService.saveFileAndUpdateStatus(wrapperMain.getAnalyticsrepositoryid_pk(), hdfsFilePath, null, new File(zipFilePath),mainWrapper.getReportName()+ReportConstants.DOT_ZIP,NVWorkorderConstant.REPORT_INSTACE_ID);
	}

	private void setDataInWrapperForReport(String filePath, SMFReportWrapper mainWrapper,
			List<ReportSubWrapper> subList, SmfWrapper wrapperMain, List<SmfWidgetWrapper> smfWidgetWrapperList,
			SmfWidgetWrapper wrapper, Layer3SummaryWrapper summary1, Layer3SummaryWrapper summary2,
			ReportSubWrapper subWrapper) {
		subWrapper.setLocationInfo(wrapper.getMobility() != null ? wrapper.getMobility().getFloorPlan() : null);
		
		mainWrapper.setReportName(getReportName(wrapperMain));
		mainWrapper.setOperatorName1(summary1.getOperatorName());
		mainWrapper.setOperatorName2(summary2.getOperatorName());
		
		if (smfWidgetWrapperList.get(ReportConstants.INDEX_ZER0) != null
				&& smfWidgetWrapperList.get(ReportConstants.INDEX_ZER0).getStationary() != null) {
			subWrapper.setLocationDataList(customerExperienceService.getLocationWiseDataForStationry(
					smfWidgetWrapperList.get(ReportConstants.INDEX_ZER0).getStationary(), filePath,
					summary1.getOperatorName()));
		}
		if (smfWidgetWrapperList.get(ReportConstants.INDEX_ONE) != null
				&& smfWidgetWrapperList.get(ReportConstants.INDEX_ONE).getStationary() != null) {
			subWrapper.setLocationDataListRight(customerExperienceService.getLocationWiseDataForStationry(
					smfWidgetWrapperList.get(ReportConstants.INDEX_ONE).getStationary(), filePath,
					summary2.getOperatorName()));
		}
		subList.add(subWrapper);
		logger.info("subWrapper to getlist size {}", subWrapper.getGraphList().size());
		mainWrapper.setSubList(subList);
	}

	private ReportSubWrapper getGraphDataForDrive(Layer3SummaryWrapper summary1, Layer3SummaryWrapper summary2,
			List<NVL3CsvDataWrapper> nvL3CsvList1, List<NVL3CsvDataWrapper> nvL3CsvList) {
		TreeMap<Long, NVL3CsvDataWrapper> op1map = getTimeStampWiseWrapper(nvL3CsvList) ;
		TreeMap<Long, NVL3CsvDataWrapper> op2map = getTimeStampWiseWrapper(nvL3CsvList1) ;
		  nvL3CsvList.addAll(nvL3CsvList1);
		  ReportSubWrapper subWrapper= getGraphDataFromList(nvL3CsvList,summary1,summary2,op1map,op2map);
		subWrapper.setFinalScore(getFinalScore(subWrapper.getWalktestScore(), subWrapper.getGraphList()));
		
		return subWrapper;
	}
	
	   private String getFinalScore(String walktestScore, List<GraphWrapper> graphWarpperList) {
			if(walktestScore!=null && graphWarpperList!=null && !graphWarpperList.isEmpty()){
				List<GraphWrapper> kpiScoreList = graphWarpperList.stream().filter(graphData -> (graphData!=null && graphData.getOperator1()!=null)).collect(Collectors.toList());
				if(ReportConstants.STR_POOR.equals(walktestScore) || kpiScoreList.stream().anyMatch(rawData -> ReportConstants.STR_POOR.equals(rawData.getOperator1()))){
					return ReportConstants.STR_POOR;
				} else if(ReportConstants.STR_GOOD.equals(walktestScore) && kpiScoreList.stream().allMatch(rawData -> ReportConstants.STR_GOOD.equals(rawData.getOperator1()))){
					return ReportConstants.STR_GOOD;		
				} else {
					return ReportConstants.STR_FAIR;
				}
			}
			return null;
		}

	private void generateCsvFilesFromDriveData(String filePath, List<SmfWidgetWrapper> smfWidgetWrapperList,
			SmfWidgetWrapper wrapper, Layer3SummaryWrapper summary1, Layer3SummaryWrapper summary2,
			List<NVL3CsvDataWrapper> nvL3CsvList1, List<NVL3CsvDataWrapper> nvL3CsvList) {
		try {
			if(nvL3CsvList1!=null && !nvL3CsvList1.isEmpty() && summary2.getOperatorName()!=null) {
		      adhocDriveReportGeneration.generateCSVReportForLiveDrive(wrapper.getMobility().getWorkorderId(), nvL3CsvList1, filePath,summary2.getOperatorName());
			}
			if(nvL3CsvList!=null && !nvL3CsvList.isEmpty() && summary1.getOperatorName()!=null) {
		      adhocDriveReportGeneration.generateCSVReportForLiveDrive(smfWidgetWrapperList.get(ReportConstants.INDEX_ZER0).getMobility().getWorkorderId(), nvL3CsvList, filePath,summary1.getOperatorName());
			}
		  }catch(BusinessException e) {
			  logger.error("unable to write csv file {}",e.getMessage());
		  }
	}

	private ReportSubWrapper getGraphDataFromList(List<NVL3CsvDataWrapper> nvL3CsvList, Layer3SummaryWrapper summary1,
			Layer3SummaryWrapper summary2, TreeMap<Long, NVL3CsvDataWrapper> op1map,
			TreeMap<Long, NVL3CsvDataWrapper> op2map) {
		List<GraphWrapper> graphWrapperList = new ArrayList<>();
		String kpiScore = null;
		logger.info("Inside method getGraphDataforWoIds");
		ReportSubWrapper subWrapper = new ReportSubWrapper();
		List<String> kpiList = Arrays.asList(ReportConstants.RSRP, ReportConstants.SINR, ReportConstants.RSRQ,
				ReportConstants.UL_THROUGHPUT);
		for (String kpi : kpiList) {
			String kpiUnit = ReportUtil.getUnitByKPiName(kpi);
			kpiUnit = (kpiUnit != null && !kpiUnit.isEmpty())
					? (ReportConstants.SPACE + ReportConstants.OPEN_BRACKET + kpiUnit + ReportConstants.CLOSED_BRACKET)
					: ReportConstants.BLANK_STRING;
			GraphWrapper graphData = new GraphWrapper();
			graphData.setKpiName(kpi + kpiUnit);
			logger.info("summary1.getOperatorName() {}{}",summary1.getOperatorName() ,summary2.getOperatorName());
			graphData.setOperator1(summary1.getOperatorName()!=null?"( "+summary1.getOperatorName()+")":"");
			graphData.setOperator2(summary2.getOperatorName()!=null?"( "+summary2.getOperatorName()+")":"");
			List<GraphDataWrapper> graphDataList = new ArrayList<>();
			for (NVL3CsvDataWrapper nvL3CsvWrapper : nvL3CsvList) {
				GraphDataWrapper wrapper = new GraphDataWrapper();
				wrapper.setKpiName(kpi);
				wrapper.setTime(new Date(nvL3CsvWrapper.getTimeStamp()));
				setValueByKpiName(kpi, nvL3CsvWrapper, wrapper, op1map, op2map);
				graphDataList.add(wrapper);
			}
			List<Double> kpiPeakValueList = StealthUtils.getMinMaxvalueListBYkpi(kpi);
			List<Double> list = graphDataList	.stream()
												.filter(Objects::nonNull)
												.map(GraphDataWrapper::getValue)
												.collect(Collectors.toList());
			kpiScore = StealthUtils.getCoverageKpiScore(list, kpiPeakValueList);
			graphData.setKpiScore(kpiScore);
		    graphData.setGraphDataList(graphDataList);
		    graphWrapperList.add(graphData);
		}
		subWrapper.setGraphList(graphWrapperList);
		subWrapper.setWalktestScore(StealthUtils.getFinalCoverageScore(graphWrapperList));
		logger.info("Finally going to return the subWrapper with data {} ",new Gson().toJson(subWrapper));
		return subWrapper;
	}

	private void setValueByKpiName(String kpi, NVL3CsvDataWrapper nvL3CsvWrapper, GraphDataWrapper wrapper, TreeMap<Long, NVL3CsvDataWrapper> op1map,
			TreeMap<Long, NVL3CsvDataWrapper> op2map) {
		if (nvL3CsvWrapper.getTimeStamp() != null) {
			if (ReportConstants.RSRP.equalsIgnoreCase(kpi)) {
				if (op1map.containsKey(nvL3CsvWrapper.getTimeStamp() / 1000)) {
					wrapper.setValue(op1map.get(nvL3CsvWrapper.getTimeStamp() / 1000).getRsrp());
				}
				if (op2map.containsKey(nvL3CsvWrapper.getTimeStamp() / 1000)) {
					wrapper.setValue2(op2map.get(nvL3CsvWrapper.getTimeStamp() / 1000).getRsrp());
				}
			} else if (ReportConstants.RSRQ.equalsIgnoreCase(kpi)) {
				if (op1map.containsKey(nvL3CsvWrapper.getTimeStamp() / 1000)) {
					wrapper.setValue(op1map.get(nvL3CsvWrapper.getTimeStamp() / 1000).getRsrq());
				}
				if (op2map.containsKey(nvL3CsvWrapper.getTimeStamp() / 1000)) {
					wrapper.setValue2(op2map.get(nvL3CsvWrapper.getTimeStamp() / 1000).getRsrq());
				}
			} else if (ReportConstants.SINR.equalsIgnoreCase(kpi)) {
				if (op1map.containsKey(nvL3CsvWrapper.getTimeStamp() / 1000)) {
					wrapper.setValue(op1map.get(nvL3CsvWrapper.getTimeStamp() / 1000).getSinr());
				}
				if (op2map.containsKey(nvL3CsvWrapper.getTimeStamp() / 1000)) {
					wrapper.setValue2(op2map.get(nvL3CsvWrapper.getTimeStamp() / 1000).getSinr());
				}
			} else if (ReportConstants.UL_THROUGHPUT.equalsIgnoreCase(kpi)) {
				if (op1map.containsKey(nvL3CsvWrapper.getTimeStamp() / 1000)) {
					wrapper.setValue(op1map.get(nvL3CsvWrapper.getTimeStamp() / 1000).getUlThroughPut());
				}
				if (op2map.containsKey(nvL3CsvWrapper.getTimeStamp() / 1000)) {
					wrapper.setValue2(op2map.get(nvL3CsvWrapper.getTimeStamp() / 1000).getUlThroughPut());
				}
			}
		}
	}
		
  private TreeMap<Long, NVL3CsvDataWrapper> getTimeStampWiseWrapper(List<NVL3CsvDataWrapper> nvL3CsvList) {
		TreeMap<Long,NVL3CsvDataWrapper> timeStampWiseMap = new TreeMap<>();

		for (NVL3CsvDataWrapper nvl3CsvDataWrapper : nvL3CsvList) {
			if(nvl3CsvDataWrapper!=null&&nvl3CsvDataWrapper.getTimeStamp()!=null) {
				timeStampWiseMap.put(nvl3CsvDataWrapper.getTimeStamp()/1000, nvl3CsvDataWrapper);
			}
		}
		return timeStampWiseMap;
	}

	private String getReportName(SmfWrapper wrapperMain) {
        try {
			AnalyticsRepository analyticsRepository = analyticsRepositoryDao.findByPk(wrapperMain.getAnalyticsrepositoryid_pk());
			if(analyticsRepository!=null && analyticsRepository.getName()!=null){
				return analyticsRepository.getName();
			}else{
				return ReportConstants.CUSTOMER_EXPERIENCE_REPORT;
			}
		} catch (Exception e) {
			logger.info("Unable to find the Saut report Name {} ",Utils.getStackTrace(e));
		}
		return null;
	}

	private  File proceedToCreateReport( SMFReportWrapper mainWrapper, String destFileName, Boolean isMobilityTestAvailable, Boolean isStationaryTestAvailable) {
		logger.info("inside the method proceedToCreateReport ");
		try {Map<String, Object> imageMap=new HashMap<>();
		String reportAssetRepo = ConfigUtils.getString(ReportConstants.SAUT_BENCHMARK_JASPER_PATH);
		List<SMFReportWrapper> dataSourceList = new ArrayList<>();
		dataSourceList.add(mainWrapper);
		JRBeanCollectionDataSource rfbeanColDataSource = new JRBeanCollectionDataSource(dataSourceList);
		imageMap.put(ReportConstants.SUBREPORT_DIR, reportAssetRepo);
		imageMap.put(ReportConstants.LOGO_CLIENT_KEY, reportAssetRepo + ReportConstants.LOGO_CLIENT_IMG);
		imageMap.put(ReportConstants.LOGO_YOUTUBE_KEY, reportAssetRepo + ReportConstants.LOGO_YOUTUBE_IMG);
		imageMap.put(ReportConstants.LOGO_INTERNET_KEY, reportAssetRepo + ReportConstants.LOGO_INTERNET_IMG);
		imageMap.put(ReportConstants.LOGO_TABLE_KEY, reportAssetRepo + ReportConstants.LOGO_TABLE_IMG);
		imageMap.put(ReportConstants.LOGO_WHATSAPP_KEY, reportAssetRepo + ReportConstants.LOGO_WHATSAPP_IMG);
		imageMap.put(ReportConstants.LOGO_VOLTE_KEY, reportAssetRepo + ReportConstants.LOGO_VOLTE_IMG);
		imageMap.put(ReportConstants.KEY_IS_MOBILITY_TEST_AVAILABLE, isMobilityTestAvailable);
		imageMap.put(ReportConstants.KEY_IS_STATIONARY_TEST_AVAILABLE, isStationaryTestAvailable);
		
		String destinationFileName = destFileName+ReportConstants.PPTX_EXTENSION;
		String fileName = JasperFillManager.fillReportToFile(reportAssetRepo + ReportConstants.MAIN_JASPER, imageMap, rfbeanColDataSource);
	 if (fileName !=null){
			JRPptxExporter exporter = new JRPptxExporter();
		 exporter.setExporterInput(new SimpleExporterInput(new File(fileName)));
		 exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(new File(destinationFileName)));
//			exporter.setParameter(JRExporterParameter.INPUT_FILE, new File(fileName));
//			exporter.setParameter(JRExporterParameter.OUTPUT_FILE,new File(destinationFileName));
			exporter.exportReport();
		}
		logger.info("Report Created successfully DESTINATION_FILE_NAME {} ",destinationFileName);

		return ReportUtil.getIfFileExists(destinationFileName);
		} catch (Exception e) {
			logger.error("@proceedToCreateReport getting err= {} ",e.getMessage() );
		}
		logger.info(
				"@proceedToCreateReport going to return null as there has been some problem in generating report");
		return null;
	}
}

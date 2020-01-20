package com.inn.foresight.module.nv.layer3.service.parse.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.inn.commons.Symbol;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.io.IOUtils;
import com.inn.commons.lang.StringUtils;
import com.inn.commons.maps.LatLng;
import com.inn.commons.maps.geohash.GeoHashUtils;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.model.OperatorDetail;
import com.inn.foresight.core.generic.service.IOperatorDetailService;
import com.inn.foresight.core.generic.utils.ConfigUtil;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.maplayer.service.IGenericMapService;
import com.inn.foresight.core.maplayer.utils.GenericMapUtils;
import com.inn.foresight.core.report.TemplateType;
import com.inn.foresight.module.nv.NVConfigUtil;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;
import com.inn.foresight.module.nv.inbuilding.service.INVIBUnitResultService;
import com.inn.foresight.module.nv.layer3.constants.NVLayer3Constants;
import com.inn.foresight.module.nv.layer3.constants.QMDLConstant;
import com.inn.foresight.module.nv.layer3.dao.INVLayer3HDFSDao;
import com.inn.foresight.module.nv.layer3.dao.INVLayer3HbaseDao;
import com.inn.foresight.module.nv.layer3.qmdlParser.util.ParserConstant;
import com.inn.foresight.module.nv.layer3.qmdlParser.wrappers.HandoverDetailWrapper;
import com.inn.foresight.module.nv.layer3.qmdlParser.wrappers.Layer3SummaryWrapper;
import com.inn.foresight.module.nv.layer3.qmdlParser.wrappers.NV2GSummaryWrapper;
import com.inn.foresight.module.nv.layer3.qmdlParser.wrappers.NV3GSummaryWrapper;
import com.inn.foresight.module.nv.layer3.qmdlParser.wrappers.NeighbourDetailWrapper;
import com.inn.foresight.module.nv.layer3.qmdlParser.wrappers.NvSMSDetailWrapper;
import com.inn.foresight.module.nv.layer3.qmdlParser.wrappers.QMDLLogCodeWrapper;
import com.inn.foresight.module.nv.layer3.service.parse.INVCsvProcessService;
import com.inn.foresight.module.nv.layer3.service.parse.INVL3BuildingJsonParserService;
import com.inn.foresight.module.nv.layer3.service.parse.INVL3CsvProcessService;
import com.inn.foresight.module.nv.layer3.service.parse.INVL3ParsingService;
import com.inn.foresight.module.nv.layer3.service.parse.INVL3UpdationService;
import com.inn.foresight.module.nv.layer3.service.parse.INVLayer3StatsService;
import com.inn.foresight.module.nv.layer3.service.parse.INVLiveJsonProcessService;
import com.inn.foresight.module.nv.layer3.utils.NVLayer3AggregationUtils;
import com.inn.foresight.module.nv.layer3.utils.NVLayer3Utils;
import com.inn.foresight.module.nv.layer3.wrapper.YoutubeWrapper;
import com.inn.foresight.module.nv.report.utils.ReportConstants;
import com.inn.foresight.module.nv.report.utils.ReportUtil;
import com.inn.foresight.module.nv.report.utils.ZipUtils;
import com.inn.foresight.module.nv.workorder.dao.IWOFileDetailDao;
import com.inn.foresight.module.nv.workorder.model.WOFileDetail;
import com.inn.foresight.module.nv.workorder.service.IWOFileDetailService;
import com.inn.foresight.module.nv.workorder.utils.NVWorkorderUtils;


@Service("NVL3ParsingServiceImpl")
public class NVL3ParsingServiceImpl implements INVL3ParsingService{

	private static final  Logger logger = LogManager.getLogger(NVL3ParsingServiceImpl.class);

	
	
	private static List<Integer>requestWorkOrder=new ArrayList<>();
	private static List<Integer>requestRecipe=new ArrayList<>();
	private static ThreadLocal<String> threadLocal = new ThreadLocal<>();

	 public void unload() {
		 threadLocal.remove(); // Compliant
	 }

	/** The wo file detail service. */
	@Autowired
	private IWOFileDetailService woFileDetailService;


	@Autowired
	private INVLayer3StatsService nvLayer3StatsGeneratorService;

	@Autowired
	private INVLayer3HDFSDao nvHdfsDao;

	/** The layer 3 dao. */
	@Autowired
	private INVLayer3HbaseDao layer3Dao;

	@Autowired
	@Qualifier("GenericMapServiceImpl")
	private IGenericMapService genericService;

	@Autowired
	private INVL3CsvProcessService nvL3CsvProcessService;

	@Autowired
	private INVCsvProcessService nvCsvService;

	@Autowired
	private INVLiveJsonProcessService liveJsonService;

	@Autowired
	private INVL3BuildingJsonParserService nvBuildingService;

	@Autowired
	private INVIBUnitResultService nvInBuildingResultService;

	@Autowired
	private INVLayer3HDFSDao nvLayer3hdfsDao;

	@Autowired
	private INVL3UpdationService nvl3Updation;
	
	@Autowired
	private IOperatorDetailService operatorService;
	
	
	@Autowired
	private IWOFileDetailDao woFileDetailDao;

	
	public static ThreadLocal<String> getThreadLocal() {
		return threadLocal;
	}
	private void processUnprocessQMDLFiles(Integer woId, Integer recipeId, String assignTo) throws IOException {
		try {
			addEntry(woId, recipeId);
			logger.info("Going to process file for woId {}  recipeId {}", woId, recipeId);
			List<WOFileDetail> woList = woFileDetailService.getUnProcessFileDetailByWorkOrderId(woId, recipeId);
			if (woList == null || woList.isEmpty()) {
				logger.info("All file Process SuccessFully");
				removeEntry(woId, recipeId);
				return;
			}
			Map<String, List<WOFileDetail>> workOrderFileMap = getWorkOrderMapfromList(woList);
			if(woId!=null && recipeId!=null) {
			threadLocal.set(String.valueOf(woId) + String.valueOf(recipeId) + String.valueOf(System.currentTimeMillis()));	
			}else {
				threadLocal.set(String.valueOf(woId) + String.valueOf(System.currentTimeMillis()));	
			}
			processUnProcessFiles(workOrderFileMap, assignTo);
			
			removeEntry(woId, recipeId);
			logger.info("All files Processed SuccessFully");
		} catch (DaoException e) {
			logger.warn("Getting Error {}  ", Utils.getStackTrace(e));
			removeEntry(woId, recipeId);
		} catch (IOException e) {
			removeEntry(woId, recipeId);
			throw new IOException(e);
		}
	}

	private void addEntry(Integer woId, Integer recipeId) {
		if (woId != null) {
			requestWorkOrder.add(woId);
		}
		if (recipeId != null) {
			requestRecipe.add(recipeId);
		}
	}

	private void removeEntry(Integer woId, Integer recipeId) {
		if (woId != null) {
			requestWorkOrder.remove(woId);
		}
		if (recipeId != null) {
			requestRecipe.remove(recipeId);
		}
	}

	private void processUnProcessFiles(Map<String, List<WOFileDetail>> workOrderFileMap, String assignTo)
			throws IOException {
		for (Entry<String, List<WOFileDetail>> woFileDetail : workOrderFileMap.entrySet()) {
			try {
				processWoWiseFiles(woFileDetail, assignTo);
			} catch (Exception e) {
				logger.error("Getting Error in processing Data for Key  {}  {}", woFileDetail.getKey(),Utils.getStackTrace(e));
			}
		}
	}

	private void processWoWiseFiles(Entry<String, List<WOFileDetail>> woFileDetail, String assignTo) throws IOException {
		try {
			TreeMap<Long, QMDLLogCodeWrapper> woParsedMap = new TreeMap<>();
			Map<String, TreeMap<Long, Object>> kpiValuesMap = new HashMap<>();
			Map<String, Put> reportPutList = new HashMap<>();
			Layer3SummaryWrapper aggrigateWrapperData = new Layer3SummaryWrapper();
			NV3GSummaryWrapper aggrigate3gWrapperData = new NV3GSummaryWrapper();
			NV2GSummaryWrapper aggrigate2gWrapperData = new NV2GSummaryWrapper();
			
			List<WOFileDetail> woFileList = woFileDetail.getValue();		
			processZipFileRecipeWise(woFileDetail, woParsedMap, kpiValuesMap,  reportPutList,
					aggrigateWrapperData, woFileList);
			setRecipeIntoAggrigationData(aggrigateWrapperData,woFileList);
			
			if(!reportPutList.isEmpty()){
				insertImageFilesIntoHbase(reportPutList);
			}
			
			if (!woParsedMap.isEmpty()) {
				processQMDLDataAndInsertIntoHbase(woFileDetail, woParsedMap, kpiValuesMap, aggrigateWrapperData,assignTo,aggrigate3gWrapperData,aggrigate2gWrapperData);
				nvl3Updation.updateReportStatusIntoGWOMeta(aggrigateWrapperData,woFileDetail);
				nvl3Updation.updateImeiIntoGeoMetaData(aggrigateWrapperData,woFileDetail);
				nvl3Updation.updateDeviceIdAndCemsdataintoGeoMetaData(aggrigateWrapperData,woFileDetail);
			}else{
				logger.warn("Getting empty Data for Key  {}",woFileDetail.getKey());
			}
		} catch (Exception e) {
			logger.error("Exception inside method processWoWiseFiles {} ",Utils.getStackTrace(e));
		}
	}
	
	private void setRecipeIntoAggrigationData(Layer3SummaryWrapper aggrigateWrapperData,
			List<WOFileDetail> woFileList) {
		if (!woFileList.isEmpty()) {
			WOFileDetail woFileDetail = woFileList.get(QMDLConstant.FIRST_RECORD_INDEX);
			aggrigateWrapperData.setScript(NVLayer3Utils.setValuesIntoSet(aggrigateWrapperData.getScript(),
					woFileDetail.getWoRecipeMapping().getRecipe().getName()));
		}
	}


	private void processZipFileRecipeWise(Entry<String, List<WOFileDetail>> woFileDetail,
			TreeMap<Long, QMDLLogCodeWrapper> woParsedMap, Map<String, TreeMap<Long, Object>> kpiValuesMap,
			 Map<String, Put> reportPutList,
			Layer3SummaryWrapper aggrigateWrapperData, List<WOFileDetail> woFileList) {
		List<WOFileDetail> fileStatusList=new ArrayList<>();		
		processFileRecipeWise(woFileDetail, woParsedMap, kpiValuesMap, reportPutList, aggrigateWrapperData, woFileList,
				fileStatusList);		
	}



	private void processFileRecipeWise(Entry<String, List<WOFileDetail>> woFileDetail,
			TreeMap<Long, QMDLLogCodeWrapper> woParsedMap, Map<String, TreeMap<Long, Object>> kpiValuesMap,
			Map<String, Put> reportPutList, Layer3SummaryWrapper aggrigateWrapperData, List<WOFileDetail> woFileList,
			List<WOFileDetail> fileStatusList) {

		List<String> processedFiles = new ArrayList<>();
		String localFilePath = getLocalTempDirectoryPathRecipeWise(woFileList);

		for (WOFileDetail woFile : woFileList) {
			try {
				processTypeWiseFile(woParsedMap, woFile, kpiValuesMap, reportPutList, aggrigateWrapperData,
						woFileDetail.getKey(), processedFiles, localFilePath);
				fileStatusList.add(woFile);
			} catch (Exception e) {
				logger.error("Error in processing file for key {}  fileName  {}   error {}", woFileDetail.getKey(),
						woFile.getFileName(), ExceptionUtils.getStackTrace(e));

			}
		}

		insertProcessedFileToHdfs(woFileList, processedFiles, localFilePath);
		updateStatusOfFileProcessing(fileStatusList);

		if (QMDLConstant.TRUE.equalsIgnoreCase(ConfigUtils.getString(QMDLConstant.DELETE_LAYER3_TEMP_FILES))) {
			try {
				FileUtils.deleteDirectory(new File(localFilePath));

				File files = new File(ConfigUtils.getString(ParserConstant.ASN_COMPILER_PATH));
				File[] listFiles = files.listFiles();
				Arrays.asList(listFiles).stream()
						.filter(p -> p.getName().startsWith(QMDLConstant.PCAP_STRING)
								|| p.getName().startsWith(QMDLConstant.MSG_TEXT_STRING))
						.collect(Collectors.toList()).forEach(File::delete);
			} catch (Exception e) {
				logger.error("Exception inside the method processTypeWiseFile while deletingDirectory {}", e.getMessage());
			}
		}
	}

	public String getLocalTempDirectoryPathRecipeWise(List<WOFileDetail> woFileList) {
		return ConfigUtils.getString(NVConfigUtil.QMDL_TEMP_PATH)
				+ woFileList.get(QMDLConstant.ZERO_INDEX).getWoRecipeMapping().getGenericWorkorder().getId()
				+ Symbol.UNDERSCORE_STRING + woFileList.get(QMDLConstant.ZERO_INDEX).getWoRecipeMapping().getId()
				+ Symbol.UNDERSCORE_STRING + +new Date().getTime() + File.separator;

	}


	private void updateStatusOfFileProcessing(List<WOFileDetail> fileStatusList) {
		logger.info("Going to set WOFileDetail Entries  Which  are Processed ..");
		for (WOFileDetail woFile : fileStatusList) {
			String response = woFileDetailService.updateStatusOfFileProcessed(woFile);
			if (response.equals(QMDLConstant.FAILURE)) {
				logger.error("Problem in updating status for file {} ", woFile.getFilePath());
				break;
			}
		}
	}

	
	private void processQMDLDataAndInsertIntoHbase(Entry<String, List<WOFileDetail>> woFileDetail,
			TreeMap<Long, QMDLLogCodeWrapper> woParsedMap, Map<String, TreeMap<Long, Object>> kpiValuesMap,
			 Layer3SummaryWrapper aggrigateWrapperData, String assignTo, NV3GSummaryWrapper aggrigate3gWrapperData, NV2GSummaryWrapper aggrigate2gWrapperData) throws IOException {
		processDataForSSVTReport(woFileDetail.getKey(),woFileDetail,aggrigateWrapperData);
		processDataForCallNetstats(woParsedMap,aggrigateWrapperData);
		SortedMap<Long, QMDLLogCodeWrapper> woAggrigateMap = NVLayer3AggregationUtils.aggregateDataByLatLong(woParsedMap, woFileDetail,aggrigateWrapperData);
		logger.debug("Getting woParsedMap after aggrigation {}    kpiValuesMap {}",woAggrigateMap.size(),kpiValuesMap.size());
		nvLayer3StatsGeneratorService.generateStatsDataFromAggrigatedData((TreeMap<Long, QMDLLogCodeWrapper>) woAggrigateMap,kpiValuesMap);
		processDataToinsertIntoHbase(woAggrigateMap,kpiValuesMap,woFileDetail.getKey(),aggrigateWrapperData,aggrigate3gWrapperData,aggrigate2gWrapperData);
		setOperatorFromMccAndMnc(woFileDetail.getValue(), aggrigateWrapperData);
		setDynamicKpiAndEventListIntoSummary(aggrigateWrapperData,aggrigate3gWrapperData,aggrigate2gWrapperData);
		
		processDataForSummary(aggrigateWrapperData,woFileDetail.getKey(),aggrigate3gWrapperData,aggrigate2gWrapperData);
		processDataForInBuilding(woFileDetail,aggrigateWrapperData,assignTo);
		processDataForYoutubeAndQuickTestAndHTTPLinkData(woFileDetail.getKey(),aggrigateWrapperData);
		insertPeakValueForReport(woFileDetail.getKey(),aggrigateWrapperData);
		processDataForAdohocReport(woFileDetail.getKey(),aggrigateWrapperData);
		processNeighbourDataForDrive(woFileDetail.getKey(),woParsedMap);
		
	}


	private void setOperatorFromMccAndMnc(List<WOFileDetail> woFileDetailList,Layer3SummaryWrapper aggrigateWrapperData){
		try {

			if(woFileDetailList.size()>1) {
				woFileDetailList = woFileDetailList.stream()
						.filter(fileDetail -> !(fileDetail.getFileName().contains(NVLayer3Constants.WO_FILE_NAME_PREFIX_FLOOR_PLAN)))
						.collect(Collectors.toList());
			}
			
			String fileName= woFileDetailList.get(0).getFileName();		

			if(fileName.contains(NVLayer3Constants.NULL_STRING)||fileName.contains(NVLayer3Constants.NONE_STRING)) {
				if(aggrigateWrapperData.getOperatorName()==null&&aggrigateWrapperData.getMCC()!=null&&aggrigateWrapperData.getMNC()!=null) {
					logger.info("Going to set operator from mcc {} & mnc {}", aggrigateWrapperData.getMCC(),aggrigateWrapperData.getMNC());
					OperatorDetail operatorDetail = operatorService.getOperatorByMCCAndMNC(aggrigateWrapperData.getMCC(),aggrigateWrapperData.getMNC());
					aggrigateWrapperData.setOperatorName(operatorDetail.getOperator());
				}
			}

			else {
				aggrigateWrapperData.setOperatorName(NVWorkorderUtils.getOperatorNameFromFileName(fileName));
			}
		}catch(Exception e) {
			logger.error("Exception in setOperatorForNone {} ", Utils.getStackTrace(e));
		}

	}

	private void processDataForSSVTReport(String key, Entry<String, List<WOFileDetail>> woFileDetailEntry, Layer3SummaryWrapper aggrigateWrapperData) {
		try {

			List<WOFileDetail> wofileDetailList = woFileDetailEntry.getValue();
			if(!wofileDetailList.isEmpty()){
				GenericWorkorder genericWorkOrderId = wofileDetailList.get(QMDLConstant.FIRST_RECORD_INDEX).getWoRecipeMapping().getGenericWorkorder();
				if(genericWorkOrderId.getTemplateType().name().equalsIgnoreCase(TemplateType.NV_SSVT.name()) ||
						genericWorkOrderId.getTemplateType().name().equals(TemplateType.NV_ADHOC_OD.name())  ||
						(genericWorkOrderId.getTemplateType().name().equals(TemplateType.NV_OPENDRIVE.name()))
						||NVLayer3Utils.isInBuidlingRecord(genericWorkOrderId)){

					Put put = new Put(key.getBytes());
					String json = getJsonForSSVTReport(aggrigateWrapperData);					
					logger.info("Getting json for ssvt report: {}",json);
					NVLayer3Utils.addStringToPut(put, json, QMDLConstant.SSVT_REPORT_DATA, QMDLConstant.COLUMN_FAMILY);
					List<Put> putlist = new ArrayList<>();
					putlist.add(put);
					try {
						layer3Dao.insertQMDLDataIntoHbase(putlist, ConfigUtils.getString(QMDLConstant.LAYER3_REPORT_TABLE));
					} catch (IOException e) {
						logger.error("Getting Error while inserting data for ssvt Report{}",Utils.getStackTrace(e));
					}		
				}
			}
		}catch (Exception e) {
			logger.error("Getting Error inside method processDataForSSVTReport{}",Utils.getStackTrace(e));
		}	
	}

	
	private String getJsonForSSVTReport(Layer3SummaryWrapper aggrigateWrapperData) {
		StringBuilder json = getIntialStringForJson();
		TreeMap<Long, HandoverDetailWrapper> hoDetailWrapperMap = aggrigateWrapperData.getHodetailWrapperMap();

		if (aggrigateWrapperData.getHodetailWrapperMap() != null
				&& !aggrigateWrapperData.getHodetailWrapperMap().isEmpty()) {

			for (Entry<Long, HandoverDetailWrapper> entry : hoDetailWrapperMap.entrySet()) {
				HandoverDetailWrapper handoverDetailWrapper = entry.getValue();
				if(handoverDetailWrapper.getStatus()!=null) {
					json.append(Symbol.BRACKET_OPEN_STRING);
					NVLayer3Utils.addValueToStringBuilder(json, handoverDetailWrapper.getTimeStamp());
					NVLayer3Utils.addValueToStringBuilder(json, handoverDetailWrapper.getLat());
					NVLayer3Utils.addValueToStringBuilder(json, handoverDetailWrapper.getLon());
					NVLayer3Utils.addValueToStringBuilder(json, handoverDetailWrapper.getRsrpatHOInit());
					NVLayer3Utils.addValueToStringBuilder(json, handoverDetailWrapper.getRsrpatHOSuccess());
					NVLayer3Utils.addValueToStringBuilder(json, handoverDetailWrapper.getSourcePci());
					NVLayer3Utils.addValueToStringBuilder(json, handoverDetailWrapper.getTargetPci());
					NVLayer3Utils.addValueToStringBuilder(json, handoverDetailWrapper.getHandoverInterruption());
					NVLayer3Utils.addValueToStringBuilder(json, handoverDetailWrapper.getStatus());
					getEndingStringJson(json);
					json.append(Symbol.COMMA);
				}

			}
		}
		getEndingStringJson(json);
		return json.toString();
	}
	

	private void processNeighbourDataForDrive(String key, TreeMap<Long, QMDLLogCodeWrapper> woParsedMap) throws IOException {
		List<Put> putList = new ArrayList<>();
		for (Entry<Long, QMDLLogCodeWrapper> woEntry : woParsedMap.entrySet()) {
			QMDLLogCodeWrapper qmdlData = woEntry.getValue();
			if (qmdlData.getNeighbourDataList() != null && !qmdlData.getNeighbourDataList().isEmpty()) {
				Put put = new Put((key + qmdlData.getTimeStamp()).getBytes());
				NVLayer3Utils.addStringToPut(put, getJsonForNeighbourData(qmdlData.getNeighbourDataList()),QMDLConstant.NEIGHBOUR, QMDLConstant.COLUMN_FAMILY);
				putList.add(put);
			}
		}
		layer3Dao.insertQMDLDataIntoHbase(putList, ConfigUtils.getString(QMDLConstant.LAYER3_NEIGHBOUR_TABLE));		
	}



	private String getJsonForNeighbourData(List<NeighbourDetailWrapper> neighbourDataList) {
		StringBuilder json = getIntialStringForJson();
		int count = 1;			
		Collections.sort(neighbourDataList);	
		
		for (NeighbourDetailWrapper neighbour : neighbourDataList) {
			json.append(Symbol.BRACKET_OPEN_STRING);
			NVLayer3Utils.addValueToStringBuilder(json,QMDLConstant.NEIGHBOUR_CONSTANT +(count++));
			NVLayer3Utils.addValueToStringBuilder(json, neighbour.getInstRsrp());
			NVLayer3Utils.addValueToStringBuilder(json, neighbour.getInstRsrq());
			NVLayer3Utils.addValueToStringBuilder(json, neighbour.getInstRssi());
			NVLayer3Utils.addValueToStringBuilder(json, neighbour.getPci());
			NVLayer3Utils.addValueToStringBuilder(json, neighbour.getLat());
			NVLayer3Utils.addValueToStringBuilder(json, neighbour.getLng());
			NVLayer3Utils.addValueToStringBuilder(json, neighbour.getInstRsrpRx0());
			NVLayer3Utils.addValueToStringBuilder(json, neighbour.getInstRsrpRx1());
			NVLayer3Utils.addValueToStringBuilder(json, neighbour.getInstRsrqRx0());
			NVLayer3Utils.addValueToStringBuilder(json, neighbour.getInstRsrqRx1());
			NVLayer3Utils.addValueToStringBuilder(json, neighbour.getInstRssiRx0());
			NVLayer3Utils.addValueToStringBuilder(json, neighbour.getInstRssiRx1());
			getEndingStringJson(json);
			json.append(Symbol.COMMA);
		}
		getEndingStringJson(json);
		return json.toString();
	}



	public void addJSonEndingInRemarkAndTestSkip(Layer3SummaryWrapper aggrigateWrapperData) {
		if(aggrigateWrapperData.getTestSkipJson()!=null) {
			aggrigateWrapperData.setTestSkipJson(getEndingStringJson(aggrigateWrapperData.getTestSkipJson()));
			logger.debug("aggrigateWrapperData.getTestSkipJson(){}",aggrigateWrapperData.getTestSkipJson());

		}
		if(aggrigateWrapperData.getRemarkJson()!=null) {
			aggrigateWrapperData.setRemarkJson(getEndingStringJson(aggrigateWrapperData.getRemarkJson()));
			logger.debug("aggrigateWrapperData.getRemarkJson(){}",aggrigateWrapperData.getRemarkJson());
		}
	}

	private  StringBuilder getEndingStringJson(StringBuilder jsonstring) {
		jsonstring.setLength(jsonstring.length()-QMDLConstant.STRING_EXTRA_LENTH);
		jsonstring.append(Symbol.BRACKET_CLOSE_STRING);
		return jsonstring;
	}

	
	

	private void processDataForAdohocReport(String key, Layer3SummaryWrapper aggrigateWrapperData) throws IOException {
		Put put = new Put(key.getBytes());
		StringBuilder json = getJsonForAdhocReport(aggrigateWrapperData);
		NVLayer3Utils.addStringToPut(put, json.toString(), QMDLConstant.ADHOC_REPORT_DATA, QMDLConstant.COLUMN_FAMILY);
		List<Put> putlist = new ArrayList<>();
		putlist.add(put);
			layer3Dao.insertQMDLDataIntoHbase(putlist, ConfigUtils.getString(QMDLConstant.LAYER3_REPORT_TABLE));		
	}



	private StringBuilder getJsonForAdhocReport(Layer3SummaryWrapper aggrigateWrapperData) {
		StringBuilder json=new StringBuilder();
		json.append(Symbol.BRACKET_OPEN_STRING);
		NVLayer3Utils.addValueToStringBuilder(json,NVLayer3Utils.getAvgFromArray(aggrigateWrapperData.getDlThroughPut()));
		NVLayer3Utils.addValueToStringBuilder(json,NVLayer3Utils.getAvgFromArray(aggrigateWrapperData.getUlThroughPut()));
		NVLayer3Utils.addValueToStringBuilder(json,NVLayer3Utils.getAvgFromArray(aggrigateWrapperData.getLatency()));
		NVLayer3Utils.addValueToStringBuilder(json,NVLayer3Utils.getAvgFromArray(aggrigateWrapperData.getPingPacketLoss()));
		NVLayer3Utils.addValueToStringBuilder(json,NVLayer3Utils.getAvgFromArray(aggrigateWrapperData.getYoutubeThroughtPut()));
		NVLayer3Utils.addValueToStringBuilder(json,aggrigateWrapperData.getYoutubeBufferTime());
		NVLayer3Utils.addValueToStringBuilder(json,NVLayer3Utils.getAvgFromArray(aggrigateWrapperData.getWptDns()));
		NVLayer3Utils.addValueToStringBuilder(json,NVLayer3Utils.getAvgFromArray(aggrigateWrapperData.getWptUrl()));
		json.setLength(json.length()-QMDLConstant.STRING_EXTRA_LENTH);
		json.append(Symbol.BRACKET_CLOSE_STRING);
		return json;
	}

	private void setDynamicKpiAndEventListIntoSummary(Layer3SummaryWrapper aggrigateWrapperData, NV3GSummaryWrapper aggrigate3gWrapperData, NV2GSummaryWrapper aggrigate2gWrapperData) {
		addIntoKpiListByAggrigratedWrapperData(aggrigateWrapperData);	
		
		addIntoMobilityListByAggrigratedWrapperData(aggrigateWrapperData);		
		addIntoCellListByAggrigratedWrapperData(aggrigateWrapperData);	
		addIntoDownloadListByAggrigratedWrapperData(aggrigateWrapperData);	
		addIntoSMSListByAggrigratedWrapperData(aggrigateWrapperData);	
		addIntoTAUListByAggrigratedWrapperData(aggrigateWrapperData);		
		
		addIntoKpi3GListByAggrigratedWrapperData(aggrigate3gWrapperData);
		addIntoKpi2GListByAggrigratedWrapperData(aggrigate2gWrapperData);
	}

	private void addIntoKpi3GListByAggrigratedWrapperData(NV3GSummaryWrapper aggrigate3gWrapperData) {
		Set<String> kpi3GList=new HashSet<>();
		if(aggrigate3gWrapperData.getRscp()!=null)
			kpi3GList.add(QMDLConstant.RSCP);
		if(aggrigate3gWrapperData.getEcio()!=null)
			kpi3GList.add(QMDLConstant.ECIO);
		if(aggrigate3gWrapperData.getRssi()!=null)
			kpi3GList.add(QMDLConstant.WCDMA_RSSI);
		
		aggrigate3gWrapperData.setKpi3GList(kpi3GList);
	}

	private void addIntoKpi2GListByAggrigratedWrapperData(NV2GSummaryWrapper aggrigate2gWrapperData) {
		Set<String> kpi2GList=new HashSet<>();
		if(aggrigate2gWrapperData.getRxLev() != null)
			kpi2GList.add(QMDLConstant.RXLEV);
		if(aggrigate2gWrapperData.getRxQual() != null)
			kpi2GList.add(QMDLConstant.RXQUAL);
		if(aggrigate2gWrapperData.getbSIC() != null && aggrigate2gWrapperData.getbSIC() > 0)
			kpi2GList.add(QMDLConstant.BSIC);
		if(aggrigate2gWrapperData.getbCCHChannel() != null && aggrigate2gWrapperData.getbCCHChannel() > 0)
			kpi2GList.add(QMDLConstant.BCCH);	
		
		aggrigate2gWrapperData.setKpi2GList(kpi2GList);
	}

	private void addIntoTAUListByAggrigratedWrapperData(Layer3SummaryWrapper aggrigateWrapperData) {
		Set<String> tauEventList = new HashSet<>();

		if (aggrigateWrapperData.getTauAttempt() != null && aggrigateWrapperData.getTauAttempt() > 0) {
			tauEventList.add(QMDLConstant.TAU_ATTEMPT);
		}
		if (aggrigateWrapperData.getTauComplete() != null && aggrigateWrapperData.getTauComplete() > 0) {
			tauEventList.add(QMDLConstant.TAU_SUCCESS);
		}
		if (aggrigateWrapperData.getTauFailure() != null && aggrigateWrapperData.getTauFailure() > 0) {
			tauEventList.add(QMDLConstant.TAU_FAILURE);
		}
		aggrigateWrapperData.setTauList(tauEventList);

	}

	private void addIntoSMSListByAggrigratedWrapperData(Layer3SummaryWrapper aggrigateWrapperData) {
		Set<String> smsList=new HashSet<>();
		if(aggrigateWrapperData.getSmsData()!=null&&aggrigateWrapperData.getSmsData().size()>0) {
			Map<Integer, NvSMSDetailWrapper> nvSMSDetailWrapper=aggrigateWrapperData.getSmsData();
			for (Map.Entry<Integer, NvSMSDetailWrapper> entry : nvSMSDetailWrapper.entrySet()) {

				if(entry.getValue().getSendingTime()!=null) {
					smsList.add(QMDLConstant.SMS_ATTEMPT);
				}
				if (entry.getValue().getStatus()!=null && entry.getValue().getStatus().equals(QMDLConstant.SMS_DELIVERED_STATUS)) {
					smsList.add(QMDLConstant.SMS_SUCCESS);	
				}else if(entry.getValue().getStatus()!=null) {
					smsList.add(QMDLConstant.SMS_FAILURE);
				}
			}
		}

		aggrigateWrapperData.setSmsList(smsList);	
	}

	
	private void addIntoDownloadListByAggrigratedWrapperData(Layer3SummaryWrapper aggrigateWrapperData) {
		Set<String> downloadList=new HashSet<>();

		if(aggrigateWrapperData.getHttpAttempt()!=null && aggrigateWrapperData.getHttpAttempt()>0) {
			downloadList.add(QMDLConstant.DOWNLOAD_ATTEMPT);
		}

		if(aggrigateWrapperData.getHttpFailure()!=null && aggrigateWrapperData.getHttpFailure()>0) {
			downloadList.add(QMDLConstant.DOWNLOAD_FAILURE);
		}
		if(aggrigateWrapperData.getHttpSucess()!=null && aggrigateWrapperData.getHttpSucess()>0) {
			downloadList.add(QMDLConstant.DOWNLOAD_SUCCESS);		

		}
		aggrigateWrapperData.setDownloadList(downloadList);

	}


	public void addIntoCellListByAggrigratedWrapperData(Layer3SummaryWrapper aggrigateWrapperData) {
		Set<String> callList=new HashSet<>();
		if(aggrigateWrapperData.getCallInitiateCount()!=null && aggrigateWrapperData.getCallInitiateCount()>0) {
			callList.add(QMDLConstant.LTE_EVENT_VOLTE_CALLINITIATE);
		}
		if(aggrigateWrapperData.getCallDropCount()!=null && aggrigateWrapperData.getCallDropCount()>0) {
			callList.add(QMDLConstant.LTE_EVENT_VOLTE_CALLDROP);
		}
		if(aggrigateWrapperData.getCallFailureCount()!=null && aggrigateWrapperData.getCallFailureCount()>0) {
			callList.add(QMDLConstant.LTE_EVENT_VOLTE_CALLFAILURE);
		}
		if(aggrigateWrapperData.getCallSuccessCount()!=null && aggrigateWrapperData.getCallSuccessCount()>0) {
			callList.add(QMDLConstant.LTE_UE_VOLTE_CALLSUCCESS);
		}		
		if(aggrigateWrapperData.getCallSetupSuccessCount()!=null && aggrigateWrapperData.getCallSetupSuccessCount()>0) {
			callList.add(QMDLConstant.LTE_EVENT_VOLTE_CALLSETUPSUCCESS);
		}
		if(aggrigateWrapperData.getCallSetupCount()!=null && aggrigateWrapperData.getCallSetupCount()>0) {
			callList.add(QMDLConstant.LTE_EVENT_VOLTE_CALLSETUP);
		}
		addLTECallSetupEventIntoLTEEvents(aggrigateWrapperData, callList);
		aggrigateWrapperData.setCallList(callList);
	}

	
	private void addLTECallSetupEventIntoLTEEvents(Layer3SummaryWrapper aggrigateWrapperData, Set<String> callList) {
		if(aggrigateWrapperData.getPagingMessageCount() != null && aggrigateWrapperData.getPagingMessageCount() > 0) {
			callList.add(QMDLConstant.LTE_PAGING_COUNT);
		}
		if(aggrigateWrapperData.getServiceReject() != null && aggrigateWrapperData.getServiceReject() > 0) {
			callList.add(QMDLConstant.LTE_SERVICE_REJECT);
		}
		if(aggrigateWrapperData.getServiceRequest() != null && aggrigateWrapperData.getServiceRequest() > 0) {
			callList.add(QMDLConstant.LTE_SERVICE_REQUEST);
		}
		if(aggrigateWrapperData.getRrcConnectionSetup() != null && aggrigateWrapperData.getRrcConnectionSetup() > 0) {
			callList.add(QMDLConstant.LTE_RRC_CONNECTION_SETUP);
		}
		if(aggrigateWrapperData.getSecurityModeComplete() != null && aggrigateWrapperData.getSecurityModeComplete() > 0) {
			callList.add(QMDLConstant.LTE_SECURITY_MODE_COMPLETE);
		}
		if(aggrigateWrapperData.getAuthenticationResponse() != null && aggrigateWrapperData.getAuthenticationResponse() > 0) {
			callList.add(QMDLConstant.LTE_AUTHENTICATION_RESPONSE);
		}
		if(aggrigateWrapperData.getAuthenticationReject() != null && aggrigateWrapperData.getAuthenticationReject() > 0) {
			callList.add(QMDLConstant.LTE_AUTHENTICATION_REJECT);
		}
		if(aggrigateWrapperData.getAuthenticationFailure() != null && aggrigateWrapperData.getAuthenticationFailure() > 0) {
			callList.add(QMDLConstant.LTE_AUTHENTICATION_FAILURE);
		}
		if(aggrigateWrapperData.getAuthenticationFailure() != null && aggrigateWrapperData.getAuthenticationFailure() > 0) {
			callList.add(QMDLConstant.LTE_AUTHENTICATION_REQUEST);
		}
		if(aggrigateWrapperData.getServiceFailure() != null && aggrigateWrapperData.getServiceFailure() > 0) {
			callList.add(QMDLConstant.LTE_SERVICE_REQUEST_FAILURE);
		}
		if(aggrigateWrapperData.getMoSignalling() != null && aggrigateWrapperData.getMoSignalling() > 0) {
			callList.add(QMDLConstant.LTE_MO_RRC_CONNECTION_REQUEST);
		}
		if(aggrigateWrapperData.getMtAccess() != null && aggrigateWrapperData.getMtAccess() > 0) {
			callList.add(QMDLConstant.LTE_MT_RRC_CONNECTION_REQUEST);
		}
		if(aggrigateWrapperData.getRrcSetupFailure() != null && aggrigateWrapperData.getRrcSetupFailure() > 0) {
			callList.add(QMDLConstant.LTE_RRC_CONNECTION_SETUP_FAILURE);
		}
		if(aggrigateWrapperData.getRrcConnReconfiguration() != null && aggrigateWrapperData.getRrcConnReconfiguration() > 0) {
			callList.add(QMDLConstant.LTE_RRC_CONNECTION_RECONFIGURATION);
		}
		if(aggrigateWrapperData.getReestablishmentFailure() != null && aggrigateWrapperData.getReestablishmentFailure() > 0) {
			callList.add(QMDLConstant.LTE_RRC_REESTABLISHMENT_FAILURE);
		}
		if(aggrigateWrapperData.getRrcConnReconfSuccess() != null && aggrigateWrapperData.getRrcConnReconfSuccess() > 0) {
			callList.add(QMDLConstant.LTE_RRC_CONNECTION_RECONFIGURATION_SUCCESS);
		}
		if(aggrigateWrapperData.getErabSetup() != null && aggrigateWrapperData.getErabSetup() > 0) {
			callList.add(QMDLConstant.LTE_ERAB_SETUP);
		}
		if(aggrigateWrapperData.getErabSetupSuccess() != null && aggrigateWrapperData.getErabSetupSuccess() > 0) {
			callList.add(QMDLConstant.LTE_ERAB_SETUP_SUCCESS);
		}
		if(aggrigateWrapperData.getRrcConnectionDropped() != null && aggrigateWrapperData.getRrcConnectionDropped() > 0) {
			callList.add(QMDLConstant.LTE_RRC_CONNECTION_DROPPED);
		}
		if(aggrigateWrapperData.getServiceRequestSuccess() != null && aggrigateWrapperData.getServiceRequestSuccess() > 0) {
			callList.add(QMDLConstant.LTE_SERVICE_REQUEST_SUCCESS);
		}		
	}


	public void addIntoMobilityListByAggrigratedWrapperData(Layer3SummaryWrapper aggrigateWrapperData) {
		Set<String> mobilityList=new HashSet<>();
		if(aggrigateWrapperData.getNewHandOverIntiateCount()!=null && aggrigateWrapperData.getNewHandOverIntiateCount()>0) {
			mobilityList.add(QMDLConstant.LTE_RRCCONNECTION_HO_INIT);
		}
		if(aggrigateWrapperData.getNewHandOverSuccessCount()!=null && aggrigateWrapperData.getNewHandOverSuccessCount()>0) {
			mobilityList.add(QMDLConstant.LTE_RRCCONNECTION_HO_OK);
		}
		if(aggrigateWrapperData.getNewHandOverFailureCount()!=null && aggrigateWrapperData.getNewHandOverFailureCount()>0) {
			mobilityList.add(QMDLConstant.LTE_RRCCONNECTION_HO_FAIL);
		}
				
		if(aggrigateWrapperData.getNewHandOverIntiateInterCount()!=null && aggrigateWrapperData.getNewHandOverIntiateInterCount()>0) {
			mobilityList.add(QMDLConstant.INTER_HANDOVER_INITIATE);
		}
		if(aggrigateWrapperData.getNewHandOverSuccessInterCount()!=null && aggrigateWrapperData.getNewHandOverSuccessInterCount()>0) {
			mobilityList.add(QMDLConstant.INTER_HANDOVER_SUCCESS);
		}
		if(aggrigateWrapperData.getNewHandOverFailureInterCount()!=null && aggrigateWrapperData.getNewHandOverFailureInterCount()>0) {
			mobilityList.add(QMDLConstant.INTER_HANDOVER_FAILURE);
		}
		
		
		if(aggrigateWrapperData.getNewHandOverIntiateIntraCount()!=null && aggrigateWrapperData.getNewHandOverIntiateIntraCount()>0) {
			mobilityList.add(QMDLConstant.INTRA_HANDOVER_INITIATE);
		}
		if(aggrigateWrapperData.getNewHandOverSuccessIntraCount()!=null && aggrigateWrapperData.getNewHandOverSuccessIntraCount()>0) {
			mobilityList.add(QMDLConstant.INTRA_HANDOVER_SUCCESS);
		}
		if(aggrigateWrapperData.getNewHandOverFailureIntraCount()!=null && aggrigateWrapperData.getNewHandOverFailureIntraCount()>0) {
			mobilityList.add(QMDLConstant.INTRA_HANDOVER_FAILURE);
		}
		addLTEHandoverEventsToMobilityList(aggrigateWrapperData,mobilityList);
		aggrigateWrapperData.setMobilityList(mobilityList);
	}

	private void addLTEHandoverEventsToMobilityList(Layer3SummaryWrapper aggrigateWrapperData, Set<String> mobilityList) {
		if(aggrigateWrapperData.getRrcEutra() != null && aggrigateWrapperData.getRrcEutra() > 0) {
			mobilityList.add(QMDLConstant.LTE_NON_OPTIMIZED_IRAT_HANDOVER_EUTRA);
		}
		if(aggrigateWrapperData.getRrcGeran() != null && aggrigateWrapperData.getRrcGeran() > 0) {
			mobilityList.add(QMDLConstant.LTE_NON_OPTIMIZED_IRAT_HANDOVER_GERAN);
		}
		if(aggrigateWrapperData.getRrcUtraFdd() != null && aggrigateWrapperData.getRrcUtraFdd() > 0) {
			mobilityList.add(QMDLConstant.LTE_NON_OPTIMIZED_IRAT_HANDOVER_UTRAFDD);
		}
		if(aggrigateWrapperData.getRrcUtraTdd() != null && aggrigateWrapperData.getRrcUtraTdd() > 0) {
			mobilityList.add(QMDLConstant.LTE_NON_OPTIMIZED_IRAT_HANDOVER_UTRATDD);
		}
		if(aggrigateWrapperData.getLteUmtsRequest() != null && aggrigateWrapperData.getLteUmtsRequest() > 0) {
			mobilityList.add(QMDLConstant.IDLE_MODE_LTE_UMTS_REQUEST);
		}
		if(aggrigateWrapperData.getLteUmtsSuccess() != null && aggrigateWrapperData.getLteUmtsSuccess() > 0) {
			mobilityList.add(QMDLConstant.IDLE_MODE_LTE_UMTS_SUCCESS);
		}
		if(aggrigateWrapperData.getUmtsLteRequest() != null && aggrigateWrapperData.getUmtsLteRequest() > 0) {
			mobilityList.add(QMDLConstant.IDLE_MODE_UMTS_LTE_REQUEST);
		}
		if(aggrigateWrapperData.getUmtsLteSuccess() != null && aggrigateWrapperData.getUmtsLteSuccess() > 0) {
			mobilityList.add(QMDLConstant.IDLE_MODE_UMTS_LTE_SUCCESS);
		}
		if(aggrigateWrapperData.getMoCsfbRequest() != null && aggrigateWrapperData.getMoCsfbRequest() > 0) {
			mobilityList.add(QMDLConstant.LTE_MO_CSFB_REQUEST);
		}
		if(aggrigateWrapperData.getMtCsfbRequest() != null && aggrigateWrapperData.getMtCsfbRequest() > 0) {
			mobilityList.add(QMDLConstant.LTE_MT_CSFB_REQUEST);
		}
		if(aggrigateWrapperData.getMoCsfbSuccess() != null && aggrigateWrapperData.getMoCsfbSuccess() > 0) {
			mobilityList.add(QMDLConstant.LTE_MO_CSFB_SUCCESS);
		}
		if(aggrigateWrapperData.getMtCsfbSuccess() != null && aggrigateWrapperData.getMtCsfbSuccess() > 0) {
			mobilityList.add(QMDLConstant.LTE_MT_CSFB_SUCCESS);
		}
		if(aggrigateWrapperData.getRrcReconfigCompleteMissing() != null && aggrigateWrapperData.getRrcReconfigCompleteMissing() > 0) {
			mobilityList.add(QMDLConstant.LTE_RRC_CONNECTION_RECONFIGURATION_COMPLETE_MISSING);
		}
		if(aggrigateWrapperData.getRadioLinkFailure() != null && aggrigateWrapperData.getRadioLinkFailure() > 0) {
			mobilityList.add(QMDLConstant.LTE_RADIO_LINK_FAILURE);
		}
		if(aggrigateWrapperData.getRrcConnReconfFailure() != null && aggrigateWrapperData.getRrcConnReconfFailure() > 0) {
			mobilityList.add(QMDLConstant.LTE_RRC_CONNECTIONRECONFIGURATION_FAILURE);
		}	
		if(aggrigateWrapperData.getCellChangeCount() != null && aggrigateWrapperData.getCellChangeCount() > 0) {
			mobilityList.add(QMDLConstant.LTE_IDLE_MODE_RESELECTION);
		}
		if(aggrigateWrapperData.getReturnCSCallEnd() != null && aggrigateWrapperData.getReturnCSCallEnd() > 0) {
			mobilityList.add(QMDLConstant.LTE_RETURN_AFTER_CS_CALL_END);
		}
	}

	public void addIntoKpiListByAggrigratedWrapperData(Layer3SummaryWrapper aggrigateWrapperData) {
		Set<String> kpiList=new HashSet<>();
		if(aggrigateWrapperData.getMeasureRSRPData()!=null) {
			kpiList.add(QMDLConstant.KPI_RSRP);
		}
		if(aggrigateWrapperData.getsINRData()!=null) {
			kpiList.add(QMDLConstant.KPI_SINR);
		}
		if(aggrigateWrapperData.getrSSIData()!=null) {
			kpiList.add(QMDLConstant.KPI_RSSI);
		}
		if(aggrigateWrapperData.getMeasureRSRQData()!=null) {
			kpiList.add(QMDLConstant.KPI_RSRQ);
		}
		if(aggrigateWrapperData.getPciMap()!=null&&!aggrigateWrapperData.getPciMap().isEmpty()) {
			kpiList.add(QMDLConstant.KPI_PCI);
		}
		if(aggrigateWrapperData.getDlThroughPut()!=null) {
			kpiList.add(QMDLConstant.KPI_DL_THROUGHPUT);
		}
		if(aggrigateWrapperData.getUlThroughPut()!=null) {
			kpiList.add(QMDLConstant.KPI_UL_THROUGHPUT);
		}
		if(aggrigateWrapperData.getLatency()!=null) {
			kpiList.add(QMDLConstant.KPI_LATENCY);
		}
		if(aggrigateWrapperData.isDlEarfcn()) {
			kpiList.add(QMDLConstant.KPI_EARFCN_DL);
		}
		if(aggrigateWrapperData.isUlEarfcn()) {
			kpiList.add(QMDLConstant.KPI_EARFCN_UL);
		}
		if(aggrigateWrapperData.isPmi()) {
			kpiList.add(QMDLConstant.KPI_PMI);
		}
		if(aggrigateWrapperData.isRI()) {
			kpiList.add(QMDLConstant.KPI_RI);
		}
		if(aggrigateWrapperData.isBandwidthUL()) {
			kpiList.add(QMDLConstant.KPI_BANDWIDTH_UL);
		}
		if(aggrigateWrapperData.isBandwidthDL()) {
			kpiList.add(QMDLConstant.KPI_BANDWIDTH_DL);
		}
		if(aggrigateWrapperData.isSpatialIndex()) {
			kpiList.add(QMDLConstant.KPI_SPATIAL_RANK);
		}
		if(aggrigateWrapperData.isPowerTxPusch()) {
			kpiList.add(QMDLConstant.KPI_POWER_TX_PUSCH);
		}		
		if(aggrigateWrapperData.getCqiCwo()!=null) {
			kpiList.add(QMDLConstant.KPI_CQI);
		}
		if(aggrigateWrapperData.isCarrierIndex()) {
			kpiList.add(QMDLConstant.KPI_CARRIER_INDEX);
		}
		if(aggrigateWrapperData.isTimingAdvance()) {
			kpiList.add(QMDLConstant.KPI_TIMING_ADVANCE);
		}	
		if(aggrigateWrapperData.isBler()) {
			kpiList.add(QMDLConstant.KPI_BLER);
		}	
		if(aggrigateWrapperData.isMcs()) {
			kpiList.add(QMDLConstant.KPI_MCS);
		}
		if(aggrigateWrapperData.getrSRPRx0Data()!=null) {
			kpiList.add(QMDLConstant.KPI_RSRP_RX0);
		}
		if(aggrigateWrapperData.getsINRRx0Data()!=null) {
			kpiList.add(QMDLConstant.KPI_SINR_RX0);
		}
		if(aggrigateWrapperData.getrSSIRx0Data()!=null) {
			kpiList.add(QMDLConstant.KPI_RSSI_RX0);
		}
		if(aggrigateWrapperData.getrSRQRx0Data()!=null) {
			kpiList.add(QMDLConstant.KPI_RSRQ_RX0);
		}
		if(aggrigateWrapperData.getrSRPRx1Data()!=null) {
			kpiList.add(QMDLConstant.KPI_RSRP_RX1);
		}
		if(aggrigateWrapperData.getsINRRx1Data()!=null) {
			kpiList.add(QMDLConstant.KPI_SINR_RX1);
		}
		if(aggrigateWrapperData.getrSSIRx1Data()!=null) {
			kpiList.add(QMDLConstant.KPI_RSSI_RX1);
		}
		if(aggrigateWrapperData.getrSRQRx1Data()!=null) {
			kpiList.add(QMDLConstant.KPI_RSRQ_RX1);
		}
		if(aggrigateWrapperData.getInstantaneousMos()!=null){
			kpiList.add(QMDLConstant.INST_MOS);
		}
		
		if(aggrigateWrapperData.getpMos()!=null){
			kpiList.add(QMDLConstant.POLQA_MOS);
		}
		addStateInfoKpiIntoKpiList(aggrigateWrapperData,kpiList);
		addRRCStatesIntoKpiList(aggrigateWrapperData,kpiList);
		addLinkAdaptationIntoKpiList(aggrigateWrapperData,kpiList);
		addSchedulingGrantInfoList(aggrigateWrapperData,kpiList);
		
		if(aggrigateWrapperData.isMNC()) {
			kpiList.add(QMDLConstant.KPI_MNC);
		}
		if(aggrigateWrapperData.isMCC()) {
			kpiList.add(QMDLConstant.KPI_MCC);
		}
		if(aggrigateWrapperData.isTrackingAraCode()) {
			kpiList.add(QMDLConstant.KPI_TRACKING_AREA_CODE);
		}
		if(aggrigateWrapperData.getDlCp() != null && !aggrigateWrapperData.getDlCp().isEmpty()) {
			kpiList.add(QMDLConstant.LTE_CELL_DL_CP_TYPE);
		}
		if(aggrigateWrapperData.getUlCp() != null && !aggrigateWrapperData.getUlCp().isEmpty()) {
			kpiList.add(QMDLConstant.LTE_CELL_UL_CP_TYPE);
		}
		
		addHandoverInterrruptionTimeUEIntoList(aggrigateWrapperData, kpiList);		
		addDominantKpisIntoList(aggrigateWrapperData, kpiList);
		addMcsInfoKpisIntoList(aggrigateWrapperData, kpiList);
		addDataThroughputIntoList(aggrigateWrapperData, kpiList);
		addRachInfoIntoList(aggrigateWrapperData, kpiList);
		addDlUlModulationTypeAndDlPRBIntoDynamicKpiList(aggrigateWrapperData, kpiList);
		addReselectionRRCRequestJitterPacketLossIntoDynamicKpiList(aggrigateWrapperData, kpiList);
		addThroughputIntoList(aggrigateWrapperData, kpiList);
		aggrigateWrapperData.setKpiList(kpiList);
//		logger.info("KPI LIST in aggregated wrapper is {} ",aggrigateWrapperData.getKpiList());
	}

	public void addHandoverInterrruptionTimeUEIntoList(Layer3SummaryWrapper aggrigateWrapperData, Set<String> kpiList) {
		if(aggrigateWrapperData.getHandoverInterruptionTimeOfQCI1() != null) {
			kpiList.add(QMDLConstant.HO_INTRPT_TIME_UE);
		}
		
		if(aggrigateWrapperData.getHandoverInterruptionTimeOfQCI9DL() != null) {
			kpiList.add(QMDLConstant.HO_INTRPT_TIME_UE_DL);
		}
		
		if(aggrigateWrapperData.getHandoverInterruptionTimeOfQCI9UL() != null) {
			kpiList.add(QMDLConstant.HO_INTRPT_TIME_UE_UL);
		}
	}
	
	private void addThroughputIntoList(Layer3SummaryWrapper aggrigateWrapperData, Set<String> kpiList) {
		if(aggrigateWrapperData.getMacDlThroughPut() != null) {
			kpiList.add(QMDLConstant.MAC_DL_THROUGHPUT);
		}
		if(aggrigateWrapperData.getPdcpThroughput() != null) {
			kpiList.add(QMDLConstant.PDCP_THROUGHPUT);
		}
		
		if(aggrigateWrapperData.getRlcThroughput() != null) {
			kpiList.add(QMDLConstant.RLC_THROUGHPUT);
		}
	}

	private void addReselectionRRCRequestJitterPacketLossIntoDynamicKpiList(
			Layer3SummaryWrapper aggrigateWrapperData, Set<String> kpiList) {
		if(aggrigateWrapperData.getReselectionSuccess() != null) {
			kpiList.add(QMDLConstant.RESELECTION_SUCCESS);
		}
		if(aggrigateWrapperData.getRrcConnectionReestablishmentRequest() != null) {
			kpiList.add(QMDLConstant.RRC_CONNECTION_REESTABLISHMENT_REQUEST);
		}
		if(aggrigateWrapperData.getJitterAggregated() != null) {
			kpiList.add(QMDLConstant.JITTER_DLF);
		}
		if(aggrigateWrapperData.getPacketLossValueAndCount() != null) {
			kpiList.add(QMDLConstant.PACKET_LOSS_DLF);
		}
	}

	private void addDlUlModulationTypeAndDlPRBIntoDynamicKpiList(Layer3SummaryWrapper aggrigateWrapperData,
		Set<String> kpiList) {
		if(aggrigateWrapperData.getDlModulationType() != null && !aggrigateWrapperData.getDlModulationType().isEmpty()) {
			kpiList.add(QMDLConstant.DL_MODULATION_TYPE);
		}
		if(aggrigateWrapperData.getUlModulationType() != null && !aggrigateWrapperData.getUlModulationType().isEmpty()) {
			kpiList.add(QMDLConstant.UL_MODULATION_TYPE);
		}
		if(aggrigateWrapperData.getB173DlPRB() != null) {
			kpiList.add(QMDLConstant.DL_PRB);
		}
	}
	
	private void addRachInfoIntoList(Layer3SummaryWrapper aggrigateWrapperData, Set<String> kpiList) {
		if(aggrigateWrapperData.getPreambleCountMax()!=null) {
			kpiList.add(QMDLConstant.PREAMBLE_COUNT_MAX);
		}
		if(aggrigateWrapperData.getPreambleInitialTxPower() != null) {
			kpiList.add(QMDLConstant.PREAMBLE_INITIAL_TX_POWER);
		}
		if(aggrigateWrapperData.getRaRNTI() != null) {
			kpiList.add(QMDLConstant.RA_RNTI);
		}
		if(aggrigateWrapperData.getRaTimingAdvance() != null) {
			kpiList.add(QMDLConstant.RA_TIMING_ADVANCE);
		}
	}

	private void addDataThroughputIntoList(Layer3SummaryWrapper aggrigateWrapperData, Set<String> kpiList) {
		if(aggrigateWrapperData.getPDSCHThroughput()!=null) {
			kpiList.add(QMDLConstant.KPI_PDSCH_THROUGHPUT);
		}
		if(aggrigateWrapperData.getPuschThroughput()!=null) {
			kpiList.add(QMDLConstant.KPI_PUSCH_THROUGHPUT);
		}
		if(aggrigateWrapperData.getPdschBLER()!=null) {
			kpiList.add(QMDLConstant.KPI_PDSCH_BLER);
		}
		if(aggrigateWrapperData.getMacUlThroughput()!=null) {
			kpiList.add(QMDLConstant.MAC_UL_THROUGHPUT);
		}
	}

	private void addMcsInfoKpisIntoList(Layer3SummaryWrapper aggrigateWrapperData, Set<String> kpiList) {
		if(aggrigateWrapperData.getTb0McsIndex()!=null) {
			kpiList.add(QMDLConstant.TB0_MCS);
		}
		if(aggrigateWrapperData.getTb1McsIndex()!=null) {
			kpiList.add(QMDLConstant.TB1_MCS);
		}
		if(aggrigateWrapperData.getTb0ModulationType()!=null && !aggrigateWrapperData.getTb0ModulationType().isEmpty()) {
			kpiList.add(QMDLConstant.TB0_MODULATION_TYPE);
		}
		if(aggrigateWrapperData.getTb1ModulationType()!=null && !aggrigateWrapperData.getTb1ModulationType().isEmpty()) {
			kpiList.add(QMDLConstant.TB1_MODULATION_TYPE);
		}
	}

	private void addDominantKpisIntoList(Layer3SummaryWrapper aggrigateWrapperData, Set<String> kpiList) {
		if (aggrigateWrapperData.getDominantChannelPCI() != null) {
			kpiList.add(QMDLConstant.LTE_DOMINANT_PCI);
		}
		if (aggrigateWrapperData.getDominantChannelRSRP() != null) {
			kpiList.add(QMDLConstant.LTE_DOMINANT_RSRP);
		}
		if (aggrigateWrapperData.getDominantChannelRSRPRx0() != null) {
			kpiList.add(QMDLConstant.LTE_DOMINANT_RSRP_TX1RX1);
		}
		if (aggrigateWrapperData.getDominantChannelRSRPRx1() != null) {
			kpiList.add(QMDLConstant.LTE_DOMINANT_RSRP_TX2RX1);
		}
		if (aggrigateWrapperData.getDominantChannelRSSI() != null) {
			kpiList.add(QMDLConstant.LTE_DOMINANT_RSSI);
		}
		if (aggrigateWrapperData.getDominantChannelRSSIRx0() != null) {
			kpiList.add(QMDLConstant.LTE_DOMINANT_RSSI_TX1RX1);
		}
		if (aggrigateWrapperData.getDominantChannelRSSIRx1() != null) {
			kpiList.add(QMDLConstant.LTE_DOMINANT_RSSI_TX2RX1);
		}
		if (aggrigateWrapperData.getDominantChannelRSRQ() != null) {
			kpiList.add(QMDLConstant.LTE_DOMINANT_RSRQ);
		}
		if (aggrigateWrapperData.getDominantChannelRSRQRx0() != null) {
			kpiList.add(QMDLConstant.LTE_DOMINANT_RSRQ_TX1RX1);
		}
		if (aggrigateWrapperData.getDominantChannelRSRQRx1() != null) {
			kpiList.add(QMDLConstant.LTE_DOMINANT_RSRQ_TX2RX1);
		}
		if (aggrigateWrapperData.getLteDominantFrequency() != null) {
			kpiList.add(QMDLConstant.LTE_DOMINANT_EARFCN);
		}
		if (aggrigateWrapperData.getPowerHeadroomdata() != null) {
			kpiList.add(QMDLConstant.TX_POWER_HEADROOM);
		}
		if (aggrigateWrapperData.getPrachTxPower() != null) {
			kpiList.add(QMDLConstant.PRACH_TX_POWER);
		}
		if (aggrigateWrapperData.getUeTxPower() != null) {
			kpiList.add(QMDLConstant.UE_TX_POWER_AGG_WRAPPER);
		}
	}
	
	private void addStateInfoKpiIntoKpiList(Layer3SummaryWrapper aggrigateWrapperData, Set<String> kpiList) {
		if(aggrigateWrapperData.getEmmState() != null) {
			kpiList.add(QMDLConstant.EMM_STATE);
		}
		if(aggrigateWrapperData.getEmmSubState() != null) {
			kpiList.add(QMDLConstant.EMM_SUBSTATE);
		}
		if(aggrigateWrapperData.getRrcState() != null) {
			kpiList.add(QMDLConstant.RRC_STATE);
		}
	}

	private void addSchedulingGrantInfoList(Layer3SummaryWrapper aggrigateWrapperData, Set<String> kpiList) {
		if(aggrigateWrapperData.getAvgDLTb0Size() != null) {
			kpiList.add(QMDLConstant.AVG_DL_TB0_SIZE);
		}
		if(aggrigateWrapperData.getAvgDLTb1Size() != null) {
			kpiList.add(QMDLConstant.AVG_DL_TB1_SIZE);
		}
		if(aggrigateWrapperData.getAvgDLTbSize() != null) {
			kpiList.add(QMDLConstant.AVG_DL_TB_SIZE);
		}
		if(aggrigateWrapperData.getAvgULTBSize() != null) {
			kpiList.add(QMDLConstant.AVG_UL_TB_SIZE);
		}
		if(aggrigateWrapperData.getAvgULPRB() != null) {
			kpiList.add(QMDLConstant.AVG_UL_PRB);
		}
	}

	private void addLinkAdaptationIntoKpiList(Layer3SummaryWrapper aggrigateWrapperData,Set<String> kpiList) {
		if(aggrigateWrapperData.getCqiCwo() != null) {
			kpiList.add(QMDLConstant.LINK_ADAPTATION_CQIO);
		}
		if(aggrigateWrapperData.getCqiCw1() != null) {
			kpiList.add(QMDLConstant.LINK_ADAPTATION_CQI1);
		}
		if(aggrigateWrapperData.getDlPRBUtilization() != null) {
			kpiList.add(QMDLConstant.LINK_ADAPTATION_DL_PRB);
		}
		if(aggrigateWrapperData.getPdschnumRb() != null) {
			kpiList.add(QMDLConstant.LINK_ADAPTATION_PDSCH_NUMRB);
		}
		if(aggrigateWrapperData.getPdschCwoModulation() != null) {
			kpiList.add(QMDLConstant.LINK_ADAPTATION_PDSCH_MODULATION);
		}
		if(aggrigateWrapperData.getPdschCwoMcs() != null) {
			kpiList.add(QMDLConstant.LINK_ADAPTATION_PDSCH_MCS);
		}
		if(aggrigateWrapperData.getUlPRBUtilization() != null) {
			kpiList.add(QMDLConstant.LINK_ADAPTATION_NUMRB);
		}
		if(aggrigateWrapperData.getPuschMcsIndex()!=null) {
			kpiList.add(QMDLConstant.KPI_PUSCH_MCS_INDEX);
		}
		if(aggrigateWrapperData.getPuschModulationType()!=null) {
			kpiList.add(QMDLConstant.KPI_PUSCH_MODULATION_TYPE);
		}
		if(aggrigateWrapperData.getTransmissionMode()!=null && !aggrigateWrapperData.getTransmissionMode().isEmpty()) {
			kpiList.add(QMDLConstant.LINK_ADAPTATION_TRANSMISSION_MODE);
		}
	}

	private void addRRCStatesIntoKpiList(Layer3SummaryWrapper aggrigateWrapperData, Set<String> kpiList) {
		if(aggrigateWrapperData.getRrcConnectionComplete() != null && aggrigateWrapperData.getRrcConnectionComplete() > 0) {
			kpiList.add(QMDLConstant.RRC_CONNECTION_FINISHED);
		}
		if(aggrigateWrapperData.getRrcConnectionSetupOk() != null && aggrigateWrapperData.getRrcConnectionSetupOk() > 0) {
			kpiList.add(QMDLConstant.RRC_CONNECTION_SETUPOK);
		}
		if(aggrigateWrapperData.getRrcConnectionAttempt() != null && aggrigateWrapperData.getRrcConnectionAttempt() > 0) {
			kpiList.add(QMDLConstant.RRC_CONNECTION_ATTEMPT);
		}
		if(aggrigateWrapperData.getRrcConnectionRelease() != null && aggrigateWrapperData.getRrcConnectionRelease() > 0) {
			kpiList.add(QMDLConstant.RRC_CONNECTION_COMPLETE);
		}
		if(aggrigateWrapperData.getRrcConnectionFailed() != null && aggrigateWrapperData.getRrcConnectionFailed() > 0) {
			kpiList.add(QMDLConstant.RRC_CONNECTION_ATTEMPT_FAILURE);
		}
		if(aggrigateWrapperData.getRrcReestablishmentSuccess() != null && aggrigateWrapperData.getRrcReestablishmentSuccess() > 0) {
			kpiList.add(QMDLConstant.RRC_REESTABLISHMENT_SUCCESS);
		}
		if(aggrigateWrapperData.getRrcReestablishmentFailed() != null && aggrigateWrapperData.getRrcReestablishmentFailed() > 0) {
			kpiList.add(QMDLConstant.RRC_REESTABLISHMENT_FAILED);
		}
	}

	private void insertPeakValueForReport(String key, Layer3SummaryWrapper aggrigateWrapperData) throws IOException {
		if(aggrigateWrapperData.getDlPeakValueMap()!=null){
			Put put = new Put(key.getBytes());
			NVLayer3Utils.addStringToPut(put, new Gson().toJson(aggrigateWrapperData.getDlPeakValueMap()), QMDLConstant.DL_TECHNOLOGY_WISE_PEAK_VALUE, QMDLConstant.COLUMN_FAMILY);
			List<Put> putlist = new ArrayList<>();
			putlist.add(put);
			layer3Dao.insertQMDLDataIntoHbase(putlist, ConfigUtils.getString(QMDLConstant.LAYER3_REPORT_TABLE));
		}
	}

	private void processDataForYoutubeAndQuickTestAndHTTPLinkData(String key, Layer3SummaryWrapper aggrigateWrapperData)
			throws IOException {
		Put put = new Put(key.getBytes());
		addQuickTestIntoPut(aggrigateWrapperData, put);
		addYoutubeTestIntoPut(aggrigateWrapperData, put);
		addHTTPTestIntoPut(aggrigateWrapperData, put);
		addIdlePciChangedData(aggrigateWrapperData,put);
		addRemarkAndTestSkipIntoPut(aggrigateWrapperData,put);
		addSmsDetailDataIntoPut(aggrigateWrapperData,put);
		List<Put> putlist = new ArrayList<>();
		putlist.add(put);
			layer3Dao.insertQMDLDataIntoHbase(putlist, ConfigUtils.getString(QMDLConstant.LAYER3_REPORT_TABLE));
	}


	private void addSmsDetailDataIntoPut(Layer3SummaryWrapper aggrigateWrapperData, Put put) {
		String smsDetailJson= getSmsJsonFromAggregatedWrapper(aggrigateWrapperData);
		if(smsDetailJson!=null){
			logger.debug("Getting sms json {}",smsDetailJson);
			NVLayer3Utils.addStringToPut(put, smsDetailJson, QMDLConstant.SMS, QMDLConstant.COLUMN_FAMILY);
		}
	}

	private String getSmsJsonFromAggregatedWrapper(Layer3SummaryWrapper aggrigateWrapperData) {
		
		if (aggrigateWrapperData.getSmsData() != null) {
			String technology="";
			if(aggrigateWrapperData.getTechnologyBandSet()!=null){
				technology=Symbol.PARENTHESIS_OPEN+ NVLayer3Utils.getStringFromSetValues(aggrigateWrapperData.getTechnologyBandSet(),Symbol.SLASH_FORWARD_STRING)+ Symbol.PARENTHESIS_CLOSE;
			}
			StringBuilder json = getIntialStringForJson();
			for (Entry<Integer, NvSMSDetailWrapper> entry : aggrigateWrapperData.getSmsData().entrySet()) {
				json.append(Symbol.BRACKET_OPEN_STRING);
				addSmsValueIntoJson(aggrigateWrapperData, json, entry,technology);
				getEndingStringJson(json);
				json.append(Symbol.COMMA);
			}
			getEndingStringJson(json);
			return json.toString();
		}
		return null;
	}


	private void addSmsValueIntoJson(Layer3SummaryWrapper aggrigateWrapperData, StringBuilder json,
			Entry<Integer, NvSMSDetailWrapper> entry, String technology) {	
		NvSMSDetailWrapper smsData = entry.getValue();
		NVLayer3Utils.addValueToStringBuilder(json,	aggrigateWrapperData.getOperatorName() + technology);
		NVLayer3Utils.addValueToStringBuilder(json,	NVLayer3Utils.getDateFromTimestamp(smsData.getSendingTime()));
		NVLayer3Utils.addValueToStringBuilder(json, entry.getKey());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getMobileNumber());
		NVLayer3Utils.addValueToStringBuilder(json, smsData.getDailNumber());
		if(smsData.getTotalTimeTaken()!=null){
			NVLayer3Utils.addValueToStringBuilder(json, (smsData.getTotalTimeTaken()/1000));
		}
		NVLayer3Utils.addValueToStringBuilder(json, getSucessFromTotalTime(smsData));
		NVLayer3Utils.addValueToStringBuilder(json,smsData.getFailureCause());
	}

	private String getSucessFromTotalTime(NvSMSDetailWrapper smsData) {
		if (smsData.getTotalTimeTaken() != null ) {
			if (smsData.getTotalTimeTaken() > QMDLConstant.SMS_THRESOLD_VALUE ||(smsData.getStatus()!=null&& !smsData.getStatus().equalsIgnoreCase(QMDLConstant.SMS_DELIVERED_STATUS))) {
				return QMDLConstant.NOK;
			} else {
				return QMDLConstant.OK;
			}
		}
		return null;
	}



	private void addRemarkAndTestSkipIntoPut(Layer3SummaryWrapper aggrigateWrapperData, Put put) {
		if(aggrigateWrapperData.getTestSkipJson()!=null){
			NVLayer3Utils.addStringToPut(put, getEndingStringJson(aggrigateWrapperData.getTestSkipJson()), QMDLConstant.TEST_SKIP, QMDLConstant.COLUMN_FAMILY);
		}
		if(aggrigateWrapperData.getRemarkJson()!=null){
			NVLayer3Utils.addStringToPut(put, getEndingStringJson(aggrigateWrapperData.getRemarkJson()), QMDLConstant.REMARK, QMDLConstant.COLUMN_FAMILY);
		}
	}
	
	private void addIdlePciChangedData(Layer3SummaryWrapper aggrigateWrapperData, Put put) {
		StringBuilder json = aggrigateWrapperData.getHandoverJson();
		if(json!=null){
		addPostFixOfcsvIntoJson(json);
		NVLayer3Utils.addStringToPut(put, json.toString(), QMDLConstant.IDLE_HANDOVER_JSON,
				QMDLConstant.COLUMN_FAMILY);
		}
	}


	private void addHTTPTestIntoPut(Layer3SummaryWrapper aggrigateWrapperData, Put put) {
		StringBuilder json = getJsonForHttpLinkDownload(aggrigateWrapperData);
		NVLayer3Utils.addStringToPut(put, json.toString(), QMDLConstant.HTTP_LINK_DOWNLOAD, QMDLConstant.COLUMN_FAMILY);
	}

	private StringBuilder getJsonForHttpLinkDownload(Layer3SummaryWrapper aggrigateWrapperData) {
		StringBuilder json=new StringBuilder();
		json.append(Symbol.BRACKET_OPEN_STRING);
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getTotalHTTPAttempt());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getTotalHTTPSuccess());
		calculateSuccessRateAndSetIntpJSon(aggrigateWrapperData, json);
		calculateHttpAvgTimeAndSetJson(aggrigateWrapperData, json);
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArray(aggrigateWrapperData.getTotalHTTPthroughPut()));
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getTotalHTTPFailure());		
		json.setLength(json.length()-QMDLConstant.STRING_EXTRA_LENTH);
		json.append(Symbol.BRACKET_CLOSE_STRING);
		return json;
	}

	private void calculateHttpAvgTimeAndSetJson(Layer3SummaryWrapper aggrigateWrapperData, StringBuilder json) {
		if(aggrigateWrapperData.getTotalHTTPSuccess()!=null &&aggrigateWrapperData.getTotalHTTPTimeTaken()!=null ){
			NVLayer3Utils.addValueToStringBuilder(json, (aggrigateWrapperData.getTotalHTTPTimeTaken()/aggrigateWrapperData.getTotalHTTPSuccess()));
		}else{
		NVLayer3Utils.addValueToStringBuilder(json, QMDLConstant.EMPTY_JSON_KEY);
		}
	}

	private void calculateSuccessRateAndSetIntpJSon(Layer3SummaryWrapper aggrigateWrapperData, StringBuilder json) {
		if(aggrigateWrapperData.getTotalHTTPAttempt()!=null &&aggrigateWrapperData.getTotalHTTPSuccess()!=null ){
			NVLayer3Utils.addValueToStringBuilder(json, ((aggrigateWrapperData.getTotalHTTPSuccess()*QMDLConstant.PERCENTAGE_MULTIPLIER)/aggrigateWrapperData.getTotalHTTPAttempt()));
		}else{
		NVLayer3Utils.addValueToStringBuilder(json, QMDLConstant.EMPTY_JSON_KEY);
		}
	}

	private void addYoutubeTestIntoPut(Layer3SummaryWrapper aggrigateWrapperData, Put put) {
		 Map<String, YoutubeWrapper> youtubeTest = aggrigateWrapperData.getYouTubeTest();
		if (youtubeTest != null) {
			StringBuilder youtubeJson=addYoutbeDataToJsonString(youtubeTest);
			NVLayer3Utils.addStringToPut(put, youtubeJson.toString(), QMDLConstant.YOUTUBE_TEST,
					QMDLConstant.COLUMN_FAMILY);
		}else{
			logger.debug("Getting Empty Youtube Data");
		}
	}


	private StringBuilder addYoutbeDataToJsonString(Map<String, YoutubeWrapper> youtubeTest) {
		StringBuilder json=new StringBuilder();
		json.append(Symbol.BRACKET_OPEN_STRING);
		for(Entry<String, YoutubeWrapper> entry:youtubeTest.entrySet()){
			json.append(Symbol.BRACKET_OPEN_STRING);
			YoutubeWrapper wrapper = entry.getValue();
			NVLayer3Utils.addValueToStringBuilder(json, wrapper.getVideoURL());
			NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getStringFromSetValues(wrapper.getVideoResolution()));
			NVLayer3Utils.addValueToStringBuilder(json,wrapper.getVideoDuration());
			NVLayer3Utils.addValueToStringBuilder(json,wrapper.getNoOfStalling());
			NVLayer3Utils.addValueToStringBuilder(json,wrapper.getTotalBufferTime());
			NVLayer3Utils.addValueToStringBuilder(json,wrapper.getFreezingRatio());
			NVLayer3Utils.addValueToStringBuilder(json,NVLayer3Utils.getAvgFromArray(wrapper.getAvgRSRP()));
			NVLayer3Utils.addValueToStringBuilder(json,NVLayer3Utils.getAvgFromArray(wrapper.getAvgSINR()));
			json.setLength(json.length()-QMDLConstant.STRING_EXTRA_LENTH);
			json.append(Symbol.BRACKET_CLOSE_STRING).append(Symbol.COMMA);
		}
		json.setLength(json.length()-QMDLConstant.STRING_EXTRA_LENTH);
		json.append(Symbol.BRACKET_CLOSE_STRING);
		return json;
	}

	private void addQuickTestIntoPut(Layer3SummaryWrapper aggrigateWrapperData, Put put) {
		StringBuilder quickTest = aggrigateWrapperData.getQuickTestJson();
		if (quickTest != null) {
			addPostFixOfcsvIntoJson(quickTest);
			NVLayer3Utils.addStringToPut(put, quickTest.toString(), QMDLConstant.QUICK_TEST,
					QMDLConstant.COLUMN_FAMILY);		
		}
	}

	private void processDataForInBuilding(Entry<String, List<WOFileDetail>> woFileDetailEntry,
			Layer3SummaryWrapper aggrigateWrapperData, String assignTo) {
		List<WOFileDetail> fileDetailList = woFileDetailEntry.getValue();
		if(fileDetailList!=null&&!fileDetailList.isEmpty()){
			WOFileDetail woFileDetailObj = fileDetailList.get(QMDLConstant.FIRST_RECORD_INDEX);
			if(NVLayer3Utils.isInBuidlingRecord(woFileDetailObj)){
				nvInBuildingResultService.updateRecordIntoInbuilding(woFileDetailObj.getWoRecipeMapping(),aggrigateWrapperData,assignTo);
			}
		}

	}

	private void insertImageFilesIntoHbase(Map<String,Put> imagePutList) throws IOException {
		layer3Dao.insertQMDLDataIntoHbase(new ArrayList<>(imagePutList.values()), ConfigUtils.getString(QMDLConstant.LAYER3_REPORT_TABLE));
	}

	
	private void processDataForSummary(Layer3SummaryWrapper aggrigateWrapperData, String rowKey, NV3GSummaryWrapper aggrigate3gWrapperData, NV2GSummaryWrapper aggrigate2gWrapperData) throws IOException  {
		StringBuilder json =getIntialStringForJson();
		addAggrigateKpiIntoJson(aggrigateWrapperData, json);
		addEventOccuredIntoJson(aggrigateWrapperData, json);
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getAddress());
		addRrcEventsIntoJson(aggrigateWrapperData,json);
		addVoLteCallDropCountToJson(json, aggrigateWrapperData);
		addErabDropRateIntoJson(json,aggrigateWrapperData);
		addDeviceAndDriveInfo(json,aggrigateWrapperData);
		addTechnologyIntoJson(json,aggrigateWrapperData);
		addScriptIntoJson(json,aggrigateWrapperData);
		addGeographyFromLatLong(json,aggrigateWrapperData);
		addUlDlEarfcnIntoJson(json,aggrigateWrapperData);
		addStationaryReport(json,aggrigateWrapperData);
		addBRTIReportParameter(json,aggrigateWrapperData);
		addDeviceInfoAndTotalDistanceToJson(aggrigateWrapperData, json);
		addTauCountToJson(aggrigateWrapperData, json);
		addInstantaneousMosToJson(aggrigateWrapperData, json);		
		addBRTIReportParameterForPacketSizeDownload(aggrigateWrapperData, json);
		addPDSCHThroughputToJson(aggrigateWrapperData, json);
		addCSFBCAllCountToJson(aggrigateWrapperData, json);
		addKPIMessageCountIntoJson(aggrigateWrapperData,json);
		addIntegrityCountIntoJson(aggrigateWrapperData,json);
		addCqiCwoAndCqiCw1IntoJson(aggrigateWrapperData, json);
		addSSVTReportParameterIntoJson(json, aggrigateWrapperData);
		addDetachRequestIntoJson(aggrigateWrapperData, json);
		addHandoverInteruptionMinMaxAvgtoJson(aggrigateWrapperData, json);
		addReselectionSuccessIntoSummaryJson(aggrigateWrapperData, json);
		addAttachAcceptAndRequestIntoJson(aggrigateWrapperData, json);
		addPingTestLatencyMinMaxAvgtoJson(aggrigateWrapperData, json);
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getAttachComplete());
		addDlUlModulationTypeAndDlPRBIntoSummaryJson(aggrigateWrapperData, json);
		addRrcConnectionReestablishmentRequestCompleteRejectIntoSummaryJson(aggrigateWrapperData, json);
		addImsRegistrationSetupTimeIntoJson(aggrigateWrapperData, json);
		addVoltePagingEventsIntoJson(aggrigateWrapperData, json);		
		addVolteCallEventsIntoJson(aggrigateWrapperData, json);				
		addPdschThroughPutMinMaxIntoJson(aggrigateWrapperData, json);		
		addThroughputDataIntoJson(aggrigateWrapperData, json);
		addJitterAndPacketLossIntoAggregateJson(aggrigateWrapperData, json);
		addUlPRBDlPRBUtilizationUlPRBUtilization(aggrigateWrapperData, json);
		addPmosIntoJson(aggrigateWrapperData, json);
		addPuschThroughPutMinMaxIntoJson(aggrigateWrapperData, json);		
		addHandoverInterruptionTimeOfQCIIntoJson(aggrigateWrapperData, json);		
		addNewCallEventsIntoJson(aggrigateWrapperData, json);
		addPDCPThroughputIntoJson(aggrigateWrapperData, json);
		addRLCThroughputIntoJson(aggrigateWrapperData, json);
		addMsg1AndMsg3CountToSummary(aggrigateWrapperData, json);		
		addPingBufferSizeIntoSummaryJson(aggrigateWrapperData, json);
		addNumberOfRtpPacketLossandTotalPacketCountIntoSummaryJson(aggrigateWrapperData, json);
		addVolteCallSetupEventsIntoSummaryJson(aggrigateWrapperData, json);				
			
		
		addPostFixOfcsvIntoJson(json);		
		Put put=new Put(Bytes.toBytes(rowKey));
		NVLayer3Utils.addStringToPut(put, json.toString(), QMDLConstant.SUMMARY_JSON, QMDLConstant.COLUMN_FAMILY);
		NVLayer3Utils.addStringToPut(put, processSummaryDataFor3G(aggrigate3gWrapperData), QMDLConstant.SUMMARY_JSON_3G, QMDLConstant.COLUMN_FAMILY);
		NVLayer3Utils.addStringToPut(put, processSummaryDataFor2G(aggrigate2gWrapperData), QMDLConstant.SUMMARY_JSON_2G, QMDLConstant.COLUMN_FAMILY);
		List<Put> putlist=new ArrayList<>();
		putlist.add(put);		
		layer3Dao.insertQMDLDataIntoHbase(putlist,ConfigUtils.getString(ConfigUtil.QMDL_DATA_TABLE));
	}
	private void addVolteCallSetupEventsIntoSummaryJson(Layer3SummaryWrapper aggrigateWrapperData, StringBuilder json) {
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getCallSetupCount());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getVolteMTCallSetup());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getVolteMOCallSetup());
		addDoubleArrayIntoJson(json, aggrigateWrapperData.getCallSetupSuccessTime());		
	}
	
	public void addNumberOfRtpPacketLossandTotalPacketCountIntoSummaryJson(Layer3SummaryWrapper aggrigateWrapperData,
			StringBuilder json) {
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getNumberOfRtpPacketsLost());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getTotalPacketCount());
	}
	public void addPingBufferSizeIntoSummaryJson(Layer3SummaryWrapper aggrigateWrapperData, StringBuilder json) {
		NVLayer3Utils.addValueToStringBuilder(json,NVLayer3Utils.getStringFromIntegerSetValues(aggrigateWrapperData.getPingBufferSize()));
	}

	private void addMsg1AndMsg3CountToSummary(Layer3SummaryWrapper aggrigateWrapperData, StringBuilder json) {
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getMessage3Count());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getMessage1Count());

	}
	private void addRLCThroughputIntoJson(Layer3SummaryWrapper aggrigateWrapperData, StringBuilder json) {
		addDoubleArrayIntoJson(json, aggrigateWrapperData.getRlcThroughput());
	
	}
	private void addPDCPThroughputIntoJson(Layer3SummaryWrapper aggrigateWrapperData, StringBuilder json) {
		addDoubleArrayIntoJson(json, aggrigateWrapperData.getPdcpThroughput());
	}

	public void addNewCallEventsIntoJson(Layer3SummaryWrapper aggrigateWrapperData, StringBuilder json) {

		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getCallSetupSuccessCount());

		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getVolteMTCallFailure());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getVolteMTCallDrop());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getVolteMTCallSetupSuccess());

		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getVolteMOCallFailure());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getVolteMOCallDrop());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getVolteMOCallSetupSuccess());

	}

	public void addHandoverInterruptionTimeOfQCIIntoJson(Layer3SummaryWrapper aggrigateWrapperData,
			StringBuilder json) {
		addDoubleArrayIntoJson(json, aggrigateWrapperData.getHandoverInterruptionTimeOfQCI1());		
		addDoubleArrayIntoJson(json, aggrigateWrapperData.getHandoverInterruptionTimeOfQCI9DL());
		addDoubleArrayIntoJson(json, aggrigateWrapperData.getHandoverInterruptionTimeOfQCI9UL());
	}
	
	

	public void addPuschThroughPutMinMaxIntoJson(Layer3SummaryWrapper aggrigateWrapperData, StringBuilder json) {
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getMinPuschThroughput());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getMaxPuschThroughput());
	}

	private void addPmosIntoJson(Layer3SummaryWrapper aggrigateWrapperData, StringBuilder json) {
		addDoubleArrayIntoJson(json, aggrigateWrapperData.getpMos());				
	}

	private void addUlPRBDlPRBUtilizationUlPRBUtilization(Layer3SummaryWrapper aggrigateWrapperData,
			StringBuilder json) {
		addDoubleArrayIntoJson(json, aggrigateWrapperData.getAvgULPRB());
		addDoubleArrayIntoJson(json, aggrigateWrapperData.getDlPRBUtilization());
		addDoubleArrayIntoJson(json, aggrigateWrapperData.getUlPRBUtilization());
	}

	private void addJitterAndPacketLossIntoAggregateJson(Layer3SummaryWrapper aggrigateWrapperData,
			StringBuilder json) {
		addDoubleArrayIntoJson(json, aggrigateWrapperData.getJitterAggregated());
		addDoubleArrayIntoJson(json, aggrigateWrapperData.getPacketLossValueAndCount());
	}

	private void addThroughputDataIntoJson(Layer3SummaryWrapper aggrigateWrapperData, StringBuilder json) {
		addDoubleArrayIntoJson(json, aggrigateWrapperData.getMacDlThroughPut());
	}

	

	public void addPdschThroughPutMinMaxIntoJson(Layer3SummaryWrapper aggrigateWrapperData, StringBuilder json) {
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getMinPdschThroughput());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getMaxPdschThroughput());
	}

	
	private void addVolteCallEventsIntoJson(Layer3SummaryWrapper aggrigateWrapperData, StringBuilder json) {
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getVolteMTCallAttempts());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getVolteMTCallSuccess());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getVolteMOCallAttempts());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getVolteMOCallSuccess());

		
	}

	public void addVoltePagingEventsIntoJson(Layer3SummaryWrapper aggrigateWrapperData, StringBuilder json) {
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getVoltePagingAttempts());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getVoltePagingSuccess());
	}

	public void addImsRegistrationSetupTimeIntoJson(Layer3SummaryWrapper aggrigateWrapperData, StringBuilder json) {
		addDoubleArrayIntoJson(json, aggrigateWrapperData.getImsRegistrationSetupTime());
	}

	private void addRrcConnectionReestablishmentRequestCompleteRejectIntoSummaryJson(Layer3SummaryWrapper aggrigateWrapperData,
			StringBuilder json) {
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getRrcConnectionReestablishmentRequest());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getRrcConnectionReestablishmentComplete());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getRrcConnectionReestablishmentReject());
	}

	private void addDlUlModulationTypeAndDlPRBIntoSummaryJson(Layer3SummaryWrapper aggrigateWrapperData,
			StringBuilder json) {
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getDlModulationType());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getUlModulationType());
		addDoubleArrayIntoJson(json, aggrigateWrapperData.getB173DlPRB());
	}
	
	private void addAttachAcceptAndRequestIntoJson(Layer3SummaryWrapper aggrigateWrapperData, StringBuilder json) {
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getAttachAccept());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getAttachRequest());
	}
	
	public void addPingTestLatencyMinMaxAvgtoJson(Layer3SummaryWrapper aggrigateWrapperData, StringBuilder json) {
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getMinLatencyBufferSize32Bytes());
        NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getMaxLatencyBufferSize32Bytes());
    	addDoubleArrayIntoJson(json, aggrigateWrapperData.getAvgLatencyBufferSize32Bytes());
    	   
        
        NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getMinLatencyBufferSize1000Bytes());
        NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getMaxLatencyBufferSize1000Bytes());
    	addDoubleArrayIntoJson(json, aggrigateWrapperData.getAvgLatencyBufferSize1000Bytes());
          
        NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getMinLatencyBufferSize1500Bytes());
        NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getMaxLatencyBufferSize1500Bytes());
    	addDoubleArrayIntoJson(json, aggrigateWrapperData.getAvgLatencyBufferSize1500Bytes());
        

	}
	
	public void addHandoverInteruptionMinMaxAvgtoJson(Layer3SummaryWrapper aggrigateWrapperData, StringBuilder json) {
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getMinHandoverInterruption());
        NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getMaxHandoverInterruption());
    	addDoubleArrayIntoJson(json, aggrigateWrapperData.getHandoverInterruption());
    	
	}
	
	private void addReselectionSuccessIntoSummaryJson(Layer3SummaryWrapper aggrigateWrapperData, StringBuilder json) {
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getReselectionSuccess());
	}
	
	private void addDetachRequestIntoJson(Layer3SummaryWrapper aggrigateWrapperData, StringBuilder json) {
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getDetachRequest());
	}
	
	private void addSSVTReportParameterIntoJson(StringBuilder json, Layer3SummaryWrapper aggrigateWrapperData) {
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getThreshholdCause());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getRsrpThresholdValue());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getSinrThresholdValue());		
	}

	private void addIntegrityCountIntoJson(Layer3SummaryWrapper aggrigateWrapperData, StringBuilder json) {
		addDoubleArrayIntoJson(json, aggrigateWrapperData.getMacUlThroughput());
		addDoubleArrayIntoJson(json, aggrigateWrapperData.getPuschThroughput());
	}

	public void addCqiCwoAndCqiCw1IntoJson(Layer3SummaryWrapper aggrigateWrapperData, StringBuilder json) {
		addDoubleArrayIntoJson(json, aggrigateWrapperData.getCqiCwo());
		addDoubleArrayIntoJson(json, aggrigateWrapperData.getCqiCw1());
	}

	private void addKPIMessageCountIntoJson(Layer3SummaryWrapper aggrigateWrapperData, StringBuilder json) {
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getRrcComplete());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getRrcRequest());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getRrcSetupComplete());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getRrcReestablishmentRequest());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getMessage3Count());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getMessage1Count());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getAttachAccept());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getAttachRequest());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getTauAccept());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getTauRequest());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getB169Count());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getB0C0Count());
	}

	private void addPDSCHThroughputToJson(Layer3SummaryWrapper aggrigateWrapperData, StringBuilder json) {
		addDoubleArrayIntoJson(json, aggrigateWrapperData.getPDSCHThroughput());
	}

	private void addCSFBCAllCountToJson(Layer3SummaryWrapper aggrigateWrapperData, StringBuilder json) {
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getCsfbCallAttempt());
		NVLayer3Utils.addValueToStringBuilder(json,aggrigateWrapperData.getCsfbCallSetupSuccess());
		NVLayer3Utils.addValueToStringBuilder(json,aggrigateWrapperData.getCsfbCallSuccess());
		NVLayer3Utils.addValueToStringBuilder(json,aggrigateWrapperData.getCsfbCallSetupFailed());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getCsfbCallDrop());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getCsfbCallFailed());
	}

	public void addBRTIReportParameterForPacketSizeDownload(Layer3SummaryWrapper aggrigateWrapperData,
			StringBuilder json) {
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getHttpDrop());
	}

	private void addInstantaneousMosToJson(Layer3SummaryWrapper aggrigateWrapperData, StringBuilder json) {
		addDoubleArrayIntoJson(json, aggrigateWrapperData.getInstantaneousMos());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getMinInstantaneousMos());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getMaxInstantaneousMos());		
	}

	private String processSummaryDataFor2G(NV2GSummaryWrapper aggrigate2gWrapperData) {
		StringBuilder json =getIntialStringForJson();
		addDoubleArrayIntoJson(json, aggrigate2gWrapperData.getRxLev());
		addDoubleArrayIntoJson(json, aggrigate2gWrapperData.getRxQual());
		addPostFixOfcsvIntoJson(json);
		return json.toString();
	}

	private String processSummaryDataFor3G(NV3GSummaryWrapper aggrigate3gWrapperData) {
		StringBuilder json =getIntialStringForJson();
		addDoubleArrayIntoJson(json, aggrigate3gWrapperData.getRscp());
		addDoubleArrayIntoJson(json, aggrigate3gWrapperData.getEcio());
		addDoubleArrayIntoJson(json, aggrigate3gWrapperData.getRssi());
		addPostFixOfcsvIntoJson(json);
		return json.toString();
	}

	private void addTauCountToJson(Layer3SummaryWrapper aggrigateWrapperData, StringBuilder json) {
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getTauAttempt());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getTauComplete());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getTauFailure());
	}

	private void addDeviceInfoAndTotalDistanceToJson(Layer3SummaryWrapper aggrigateWrapperData, StringBuilder json) {
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getTotalDistance());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getBattaryInfo());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getDriveVersion());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getChipsetInfo());
	}

	private void addBRTIReportParameter(StringBuilder json, Layer3SummaryWrapper aggrigateWrapperData) {
		addDoubleArrayIntoJson(json,aggrigateWrapperData.getPingPacketLoss());	
		addDoubleArrayIntoJson(json,aggrigateWrapperData.getHttpDownLoadTime());	
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getHttpAttempt());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getHttpSucess());
		NVLayer3Utils.addValueToStringBuilder(json,NVLayer3Utils.getStringFromSetValues(aggrigateWrapperData.getBandSet()));		
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getHttpFailure());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getSmsFailure());

	}

	private void addStationaryReport(StringBuilder json, Layer3SummaryWrapper aggrigateWrapperData) {
		addKpiMinMaxIntoSummary(json, aggrigateWrapperData);
		addKpiAvgForStationarySummary(json, aggrigateWrapperData);
		addSmsInformationIntoSummary(json,aggrigateWrapperData);
		addCallInformationIntoSummary(json,aggrigateWrapperData);
		addHandoverInformationIntoSummary(json,aggrigateWrapperData);
		addStartLatlongIntoSummary(json,aggrigateWrapperData);
		addCallConnectionSetupTime(json,aggrigateWrapperData);
		addMosOnnetAndOffnet(json,aggrigateWrapperData);
	}


	private void addMosOnnetAndOffnet(StringBuilder json, Layer3SummaryWrapper aggrigateWrapperData) {
		addDoubleArrayIntoJson(json,aggrigateWrapperData.getFinalMosG711Onnet());	
		addDoubleArrayIntoJson(json,aggrigateWrapperData.getFinalMosG711Offnet());	
		addDoubleArrayIntoJson(json,aggrigateWrapperData.getFinalMosILbcOnnet());	
		addDoubleArrayIntoJson(json,aggrigateWrapperData.getFinalMosILbcOffnet());			
	}



	private void addCallConnectionSetupTime(StringBuilder json, Layer3SummaryWrapper aggrigateWrapperData) {
		addDoubleArrayIntoJson(json,aggrigateWrapperData.getCallConnectionSetupTime());	
		addDoubleArrayIntoJson(json,aggrigateWrapperData.getCallConnectionSetupTimeOnnet());
		addDoubleArrayIntoJson(json,aggrigateWrapperData.getCallConnectionSetupTimeOffnet());	
		
	}



	private void addStartLatlongIntoSummary(StringBuilder json, Layer3SummaryWrapper aggrigateWrapperData) {
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getStartLat());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getStartLong());
	}

	private void addHandoverInformationIntoSummary(StringBuilder json, Layer3SummaryWrapper aggrigateWrapperData) {
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getNewHandOverIntiateInterCount());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getNewHandOverFailureInterCount());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getNewHandOverSuccessInterCount());

		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getNewHandOverIntiateIntraCount());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getNewHandOverFailureIntraCount());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getNewHandOverSuccessIntraCount());		
	}

	private void addCallInformationIntoSummary(StringBuilder json, Layer3SummaryWrapper aggrigateWrapperData) {
		addOnnetCallIntoSummary(json, aggrigateWrapperData);		
		addOffnetCallIntoSummary(json, aggrigateWrapperData);
	}

	private void addOffnetCallIntoSummary(StringBuilder json, Layer3SummaryWrapper aggrigateWrapperData) {
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getCallInitiateOffNetCount());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getCallDropOffNetCount());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getCallFailureOffNetCount());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getCallSuccessOffNetCount());
	}

	private void addOnnetCallIntoSummary(StringBuilder json, Layer3SummaryWrapper aggrigateWrapperData) {
		logger.debug("Getting call initiate {}",aggrigateWrapperData.getCallInitiateOnNetCount());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getCallInitiateOnNetCount());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getCallDropOnNetCount());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getCallFailureOnNetCount());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getCallSuccessOnNetCount());
	}

	private void addKpiAvgForStationarySummary(StringBuilder json, Layer3SummaryWrapper aggrigateWrapperData) {
		addDoubleArrayIntoJson(json,aggrigateWrapperData.getJitter());
		addDoubleArrayIntoJson(json,aggrigateWrapperData.getResponseTime());
		addDoubleArrayIntoJson(json, aggrigateWrapperData.getHandoverLatencyTime());
		addDoubleArrayIntoJson(json, aggrigateWrapperData.getHttpDl());		
		addDoubleArrayIntoJson(json, aggrigateWrapperData.getHttpUl());		
		addDoubleArrayIntoJson(json, aggrigateWrapperData.getFtpDl());		
		addDoubleArrayIntoJson(json, aggrigateWrapperData.getFtpUl());
		addDoubleArrayIntoJson(json, aggrigateWrapperData.getConnectionSetupTime());
		addDoubleArrayIntoJson(json, aggrigateWrapperData.getFinalMosG711());
		addDoubleArrayIntoJson(json, aggrigateWrapperData.getFinalMosILbc());
	}

	private void addKpiMinMaxIntoSummary(StringBuilder json, Layer3SummaryWrapper aggrigateWrapperData) {
		addMinValuesIntoJson(json, aggrigateWrapperData);
		addMaxValuesIntoJson(json, aggrigateWrapperData);
	}

	private void addMaxValuesIntoJson(StringBuilder json, Layer3SummaryWrapper aggrigateWrapperData) {
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getMaxLatency());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getMaxDl());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getMaxUl());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getMaxHttpDl());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getMaxHttpUl());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getMaxFtpDl());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getMaxFtpUl());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getMaxJitter());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getMaxresponseTime());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getMaxRSRP());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getMaxSINR());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getMaxRsrq());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getMaxRssi());
	}

	private void addMinValuesIntoJson(StringBuilder json, Layer3SummaryWrapper aggrigateWrapperData) {
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getMinLatency());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getMinDl());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getMinUl());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getMinHttpDl());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getMinHttpUl());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getMinFtpDl());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getMinFtpUl());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getMinJitter());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getMinresponseTime());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getMinRSRP());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getMinSINR());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getMinRsrq());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getMinRssi());
	}


	private void addSmsInformationIntoSummary(StringBuilder json, Layer3SummaryWrapper aggrigateWrapperData) {
		if(aggrigateWrapperData.getSmsData()!=null){
			Map<Integer, NvSMSDetailWrapper> smsDetailMap = aggrigateWrapperData.getSmsData();
			addSMSDeliveredIntoJson(json,smsDetailMap);
		}else{
			for(int i=QMDLConstant.SMS_FIELD_COUNT;i>0;i--){
				NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getSmsData());
			}
		}

	}

	private void addSMSDeliveredIntoJson(StringBuilder json, Map<Integer, NvSMSDetailWrapper> smsDetailMap) {

		Long[] allSms = new Long[4];
		Long[] onNetSms = new Long[4];
		Long[] offNetSms = new Long[4];
		List<NvSMSDetailWrapper> smsDetailList = new ArrayList<>(smsDetailMap.values());
		logger.debug("Getting sms json {}  ",new Gson().toJson(smsDetailList));
		for (NvSMSDetailWrapper smsWrap : smsDetailList) {
			if (smsWrap.getNeNtatus() != null && smsWrap.getNeNtatus().equals(QMDLConstant.ON_NET_STATUS)) {
				setONNetValues(smsWrap, onNetSms, allSms);
			} else {
				setOffNetValues(smsWrap, offNetSms, allSms);
			}
		}
		addValuesIntoJson(allSms,onNetSms,offNetSms,json);
	}

	private void addValuesIntoJson(Long[] allSms, Long[] onNetSms, Long[] offNetSms, StringBuilder json) {
	addAllSMSDetail(allSms, json);
	addAllSMSDetail(onNetSms, json);
	addAllSMSDetail(offNetSms, json);	
	}



	private void addAllSMSDetail(Long[] allSms, StringBuilder json) {
		for(Long smsDetail:allSms){
			if(smsDetail==null){
				NVLayer3Utils.addValueToStringBuilder(json, QMDLConstant.INITIAL_EVENT_COUNT);
			}else{
			NVLayer3Utils.addValueToStringBuilder(json, smsDetail);
			}
		}
	}

	private void setONNetValues(NvSMSDetailWrapper smsWrap, Long[] onNetSms, Long[] allSms) {
		setSMSAttempt(onNetSms, allSms);
		if (smsWrap.getStatus()!=null&&smsWrap.getStatus().equals(QMDLConstant.SMS_DELIVERED_STATUS)) {
			setAllASMSDeliveredField(smsWrap, allSms);
			setAllASMSDeliveredField(smsWrap, onNetSms);
			if (smsWrap.getTotalTimeTaken() < QMDLConstant.SMS_THRESHOLD_TIME) {
				setMorethan3MinCountIntoArray(onNetSms, allSms);
			}
		}
	}

	private void setAllASMSDeliveredField(NvSMSDetailWrapper smsWrap, Long[] sms) {
		sms[QMDLConstant.SMS_SUCESSFULL_INDEX] = sms[QMDLConstant.SMS_SUCESSFULL_INDEX]!=null?sms[QMDLConstant.SMS_SUCESSFULL_INDEX]+ QMDLConstant.RECORD_COUNT:QMDLConstant.RECORD_COUNT;
		sms[QMDLConstant.SMS_DELIVERED_TIME_INDEX] = sms[QMDLConstant.SMS_DELIVERED_TIME_INDEX]!=null?sms[QMDLConstant.SMS_DELIVERED_TIME_INDEX]+ smsWrap.getTotalTimeTaken(): smsWrap.getTotalTimeTaken();
	}

	private void setOffNetValues(NvSMSDetailWrapper smsWrap, Long[] offNetSms, Long[] allSms) {
		setSMSAttempt(offNetSms, allSms);
		if (smsWrap.getStatus()!=null && smsWrap.getStatus().equals(QMDLConstant.SMS_DELIVERED_STATUS)) {
			setAllASMSDeliveredField(smsWrap, allSms);
			setAllASMSDeliveredField(smsWrap, offNetSms);

			if (smsWrap.getTotalTimeTaken() < QMDLConstant.SMS_THRESHOLD_TIME) {
				setMorethan3MinCountIntoArray(offNetSms, allSms);
			}
		}		
	}

	private void setMorethan3MinCountIntoArray(Long[] offNetSms, Long[] allSms) {
		allSms[QMDLConstant.SMS_DELIVERED_LESSTHAN_3_TIME_INDEX] =allSms[QMDLConstant.SMS_DELIVERED_LESSTHAN_3_TIME_INDEX]!=null? allSms[QMDLConstant.SMS_DELIVERED_LESSTHAN_3_TIME_INDEX]	+ QMDLConstant.RECORD_COUNT:QMDLConstant.RECORD_COUNT;
		offNetSms[QMDLConstant.SMS_DELIVERED_LESSTHAN_3_TIME_INDEX] =offNetSms[QMDLConstant.SMS_DELIVERED_LESSTHAN_3_TIME_INDEX]	!=null? offNetSms[QMDLConstant.SMS_DELIVERED_LESSTHAN_3_TIME_INDEX]	+ QMDLConstant.RECORD_COUNT:QMDLConstant.RECORD_COUNT;
	}

	private void setSMSAttempt(Long[] offNetSms, Long[] allSms) {
		allSms[QMDLConstant.SMS_ATTEMPT_INDEX] =allSms[QMDLConstant.SMS_ATTEMPT_INDEX]!=null? allSms[QMDLConstant.SMS_ATTEMPT_INDEX] + QMDLConstant.RECORD_COUNT:QMDLConstant.RECORD_COUNT;
		offNetSms[QMDLConstant.SMS_ATTEMPT_INDEX] = offNetSms[QMDLConstant.SMS_ATTEMPT_INDEX] !=null?offNetSms[QMDLConstant.SMS_ATTEMPT_INDEX] + QMDLConstant.RECORD_COUNT:QMDLConstant.RECORD_COUNT;
	}

	private void addUlDlEarfcnIntoJson(StringBuilder json, Layer3SummaryWrapper aggrigateWrapperData) {
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getStringFromSetValues(aggrigateWrapperData.getUlDlEarfcn()));
	}

	private void addGeographyFromLatLong(StringBuilder json, Layer3SummaryWrapper aggrigateWrapperData) {
		String geographyWithLevel = getGeographyByStartEndLatLong(ConfigUtils.getString(ConfigUtil.GEOGRAPHYL4_TABLE),QMDLConstant.GEOGRAPHY_LEVEL_4,aggrigateWrapperData);
		if(geographyWithLevel!=null){
			NVLayer3Utils.addValueToStringBuilder(json,geographyWithLevel );
		}else {
			NVLayer3Utils.addValueToStringBuilder(json,null);
			NVLayer3Utils.addValueToStringBuilder(json,null);
		}
	}


	private String getGeographyByStartEndLatLong(String tableName,Integer geographyLevel,Layer3SummaryWrapper aggrigateWrapperData) {
		String startgeography= getGeographyByLatLong(tableName,geographyLevel,aggrigateWrapperData.getStartLat(),aggrigateWrapperData.getStartLong());
		String  endgeography= getGeographyByLatLong(tableName,geographyLevel,aggrigateWrapperData.getEndLat(),aggrigateWrapperData.getEndLat());
		if(startgeography==null){
			return endgeography;
		}else if(endgeography==null){
			return startgeography;
		}else if(startgeography.equals(endgeography)){
			return endgeography;
		}else{
			if(geographyLevel>0){
				tableName=tableName.replace(geographyLevel.toString(), String.valueOf(geographyLevel-QMDLConstant.GEOGRAPHY_LOWER_LEVEL_CONSTANT));
				return getGeographyByStartEndLatLong(tableName,geographyLevel-QMDLConstant.GEOGRAPHY_LOWER_LEVEL_CONSTANT,aggrigateWrapperData);
			}else{
				return QMDLConstant.GEOGRAPHY_GLOBAL;
			}
		}

	}

	private String getGeographyByLatLong(String tableName, Integer geographyLevel, Double lat, Double lon) {
		try {
			List<List<String>> result = genericService.getGeographyDataByPoint(
					GenericMapUtils.GEOGRAPHY_TABLE_NAME, 
					GenericMapUtils.getGeoColumnList(),
					lat,lon,true,tableName.toUpperCase());
			if (result != null && !result.isEmpty()) {
				List<String> list = result.get(QMDLConstant.FIRST_RECORD_INDEX);
				if (list != null && !list.isEmpty()) {
					return list.get(QMDLConstant.FIRST_RECORD_INDEX)+","+geographyLevel;
				}
			}
		} catch (Exception e) {			
			logger.error("Error while getGeographyByLatLong  {} ", e.getMessage());
			return null;
		}
		return null;
	}

	private void addScriptIntoJson(StringBuilder json, Layer3SummaryWrapper aggrigateWrapperData) {
		NVLayer3Utils.addValueToStringBuilder(json,NVLayer3Utils.getStringFromSetValues(aggrigateWrapperData.getScript()));
	}

	private void addTechnologyIntoJson(StringBuilder json, Layer3SummaryWrapper aggrigateWrapperData) {
		NVLayer3Utils.addValueToStringBuilder(json,NVLayer3Utils.getStringFromSetValues(aggrigateWrapperData.getTechnologyBandSet()));
	}



	private void addDeviceAndDriveInfo(StringBuilder json, Layer3SummaryWrapper aggrigateWrapperData) {
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getAppVersion());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getMake());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getModel());
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getStringFromSetValues(aggrigateWrapperData.getOperator()));
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getOperatorName());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getPlatform());
		NVLayer3Utils.addValueToStringBuilder(json,NVLayer3Utils.getStringFromIntegerSetValues(aggrigateWrapperData.getCellidSet()));
		NVLayer3Utils.addValueToStringBuilder(json,NVLayer3Utils.getStringFromSetValues(aggrigateWrapperData.getBandSet()));
	}

	private void addErabDropRateIntoJson(StringBuilder json, Layer3SummaryWrapper aggrigateWrapperData) {
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getErabDrop());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getTotalErab());
	}

	private void addRrcEventsIntoJson(Layer3SummaryWrapper aggrigateWrapperData, StringBuilder json) {
		NVLayer3Utils.addValueToStringBuilder(json,aggrigateWrapperData.getRrcInitiate());
		NVLayer3Utils.addValueToStringBuilder(json,aggrigateWrapperData.getRrcSucess());

	}

	private void addEventOccuredIntoJson(Layer3SummaryWrapper eventAggrigateData, StringBuilder json) {
		logger.debug("Getting handover ini {} fail {}  succ {}   ",eventAggrigateData.getNewHandOverIntiateCount(), eventAggrigateData.getNewHandOverFailureCount(),eventAggrigateData.getNewHandOverSuccessCount());
		
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.addIntegerValue(eventAggrigateData.getNewHandOverIntiateInterCount(),eventAggrigateData.getNewHandOverIntiateIntraCount()));
		NVLayer3Utils.addValueToStringBuilder(json, eventAggrigateData.getNewHandOverFailureCount());
		NVLayer3Utils.addValueToStringBuilder(json, eventAggrigateData.getNewHandOverSuccessCount());
		
		logger.debug("Getting call Events  ini {} setup{}  drop {}  fail {}  succ {}", eventAggrigateData.getCallInitiateCount(),eventAggrigateData.getCallSetupSuccessCount(),eventAggrigateData.getCallDropCount(),eventAggrigateData.getCallFailureCount(),eventAggrigateData.getCallSuccessCount());

		
		NVLayer3Utils.addValueToStringBuilder(json, eventAggrigateData.getCallInitiateCount());		
		NVLayer3Utils.addValueToStringBuilder(json, eventAggrigateData.getCallDropCount());
		NVLayer3Utils.addValueToStringBuilder(json, eventAggrigateData.getCallFailureCount());
		NVLayer3Utils.addValueToStringBuilder(json, eventAggrigateData.getCallSuccessCount());
		NVLayer3Utils.addValueToStringBuilder(json, eventAggrigateData.getCellChangeCount());
		//ForTotalCount
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.addIntegerValue(eventAggrigateData.getNewHandOverIntiateInterCount(),eventAggrigateData.getNewHandOverIntiateIntraCount()));
		NVLayer3Utils.addValueToStringBuilder(json, eventAggrigateData.getCallInitiateCount());
		
	}

	private void addAggrigateKpiIntoJson(Layer3SummaryWrapper  aggrigateWrapperData, StringBuilder json) {
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getTimeStamp());
		NVLayer3Utils.addValueToStringBuilder(json, aggrigateWrapperData.getEtimeStamp());
		addDoubleArrayIntoJson(json, aggrigateWrapperData.getrSSIData());
		addDoubleArrayIntoJson(json, aggrigateWrapperData.getMeasureRSRPData());
		addDoubleArrayIntoJson(json, aggrigateWrapperData.getMeasureRSRQData());
		addDoubleArrayIntoJson(json, aggrigateWrapperData.getsINRData());
		addDoubleArrayIntoJson(json, aggrigateWrapperData.getDlThroughPut());
		addDoubleArrayIntoJson(json, aggrigateWrapperData.getUlThroughPut());
		addDoubleArrayIntoJson(json, aggrigateWrapperData.getLatency());
	}

	private void addDoubleArrayIntoJson(StringBuilder json, Double [] array) {
		if(array!=null){
			NVLayer3Utils.addValueToStringBuilder(json,array[QMDLConstant.VALUE_INDEX]);
			NVLayer3Utils.addValueToStringBuilder(json, array[QMDLConstant.COUNT_INDEX]);
		}else {
			NVLayer3Utils.addValueToStringBuilder(json,null);
			NVLayer3Utils.addValueToStringBuilder(json,null);
		}
	}

	private void processDataToinsertIntoHbase(SortedMap<Long, QMDLLogCodeWrapper> woAggrigateMap, Map<String, TreeMap<Long, Object>> kpiValuesMap, String woId, Layer3SummaryWrapper aggrigateWrapperData, NV3GSummaryWrapper aggrigate3gWrapperData,NV2GSummaryWrapper aggrigate2gWrapperData) throws IOException  {
		processKpiDataAndInsertIntoHbase(woAggrigateMap,woId,aggrigateWrapperData,aggrigate3gWrapperData,aggrigate2gWrapperData);
		processStatsDataAndInsertIntoHbase(kpiValuesMap,woId);
	}

	private void processDataForCallNetstats(TreeMap<Long, QMDLLogCodeWrapper> woParsedMap,
			Layer3SummaryWrapper aggrigateWrapperData) {
		if(aggrigateWrapperData.getCallNetStatusOnSuccessMap()!=null){
			TreeMap<Long, String> getCallNetStatusOnSuccessMap = aggrigateWrapperData.getCallNetStatusOnSuccessMap();
			Boolean flag = Boolean.FALSE;
			for(Entry<Long, String> callEvent:getCallNetStatusOnSuccessMap.entrySet()){
				Long key = NVLayer3Utils.getKeyForBean(callEvent.getKey());
				flag=tagMosOnNetOffNetIntoData(key,woParsedMap,callEvent.getValue(),flag);
				flag = Boolean.FALSE;
			}
		}
		
		
		if(aggrigateWrapperData.getCallNetStatusMap()!=null){
			TreeMap<Long, String> callNetstatsMap = aggrigateWrapperData.getCallNetStatusMap();
			for(Entry<Long, String> callEvent:callNetstatsMap.entrySet()){
				Long key = NVLayer3Utils.getKeyForBean(callEvent.getKey());
				tagCallOnNetOffNetIntoRawData(key,woParsedMap,callEvent.getValue());
			}			
		}
	}

	private Boolean tagMosOnNetOffNetIntoData(Long key, TreeMap<Long, QMDLLogCodeWrapper> woParsedMap, String status, Boolean flag) {
		Map.Entry<Long, QMDLLogCodeWrapper> lowerEntry = woParsedMap.floorEntry(key);
		
		while (lowerEntry!=null) {
			key=lowerEntry.getKey();
			QMDLLogCodeWrapper wrapper = lowerEntry.getValue();
				if(wrapper.getMosStatus()!=null){
					break;
				}
				wrapper.setMosStatus(status);
				if (wrapper.getFinalMosG711()!=null&& !flag) {
					flag=true;
					setMosInformationIntoWrapper(wrapper, status);
				}else{
					wrapper.setFinalMosG711(null);
					wrapper.setFinalMosILbc(null);
				}
			lowerEntry = woParsedMap.floorEntry(key-1);
		}
		return flag;
	}

	private void setMosInformationIntoWrapper(QMDLLogCodeWrapper wrapper, String value) {
		if(value.equals(QMDLConstant.ON_NET_STATUS)){
		wrapper.setFinalMosG711Onnet(wrapper.getFinalMosG711());
		wrapper.setFinalMosILbcOnnet(wrapper.getFinalMosILbc());
		}else {
			wrapper.setFinalMosG711Offnet(wrapper.getFinalMosG711());
			wrapper.setFinalMosILbcOffnet(wrapper.getFinalMosILbc());
		}
	}

	private void setcallInformationIntoWrapper(QMDLLogCodeWrapper wrapper, String value) {
		if(value.equals(QMDLConstant.ON_NET_STATUS)){
			wrapper.setCallInitiateOnNetCount(wrapper.getCallInitiateCount());
			wrapper.setCallSuccessOnNetCount(wrapper.getCallSuccessCount());
			wrapper.setCallFailureOnNetCount(wrapper.getCallFailureCount());
			wrapper.setCallDropOnNetCount(wrapper.getCallDropCount());	
			wrapper.setCallConnectionSetupTimeOnnet(wrapper.getCallConnectionSetupTime());
			
		}else {
			wrapper.setCallInitiateOffNetCount(wrapper.getCallInitiateCount());
			wrapper.setCallSuccessOffNetCount(wrapper.getCallSuccessCount());
			wrapper.setCallFailureOffNetCount(wrapper.getCallFailureCount());
			wrapper.setCallDropOffNetCount(wrapper.getCallDropCount());
			wrapper.setCallConnectionSetupTimeOffnet(wrapper.getCallConnectionSetupTime());
		}
	}



	private void tagCallOnNetOffNetIntoRawData(Long key, TreeMap<Long, QMDLLogCodeWrapper> woParsedMap,
			 String status) {
		Map.Entry<Long, QMDLLogCodeWrapper> lowerEntry = woParsedMap.floorEntry(key);
		while (lowerEntry!=null) {
			key=lowerEntry.getKey();
			QMDLLogCodeWrapper wrapper = lowerEntry.getValue();
				if(wrapper.getCallStatus()!=null){
					break;
				}
				wrapper.setCallStatus(status);
				if (isValidForCallEvent(wrapper)) {
					setcallInformationIntoWrapper(wrapper, status);
					logger.debug("Getting value  key  {}", key);
				}
			lowerEntry = woParsedMap.floorEntry(key-1);
		}
	}

	private boolean isValidForCallEvent(QMDLLogCodeWrapper wrapper) {
		return (wrapper.getCallSuccessCount() != null
				&& wrapper.getCallSuccessCount() > QMDLConstant.INITIAL_EVENT_COUNT)
				|| (wrapper.getCallFailureCount() != null
						&& wrapper.getCallFailureCount() > QMDLConstant.INITIAL_EVENT_COUNT)
				|| (wrapper.getCallInitiateCount() != null
						&& wrapper.getCallInitiateCount() > QMDLConstant.INITIAL_EVENT_COUNT)
				|| (wrapper.getCallDropCount() != null && wrapper.getCallDropCount() > QMDLConstant.INITIAL_EVENT_COUNT)
				|| (wrapper.getConnectionSetupTime() != null);
	}


	private void processStatsDataAndInsertIntoHbase(Map<String, TreeMap<Long, Object>> kpiValuesMap, String woId) throws IOException {
		 List<Put> putlist = nvLayer3StatsGeneratorService.createStatsPutListForKpiMap(kpiValuesMap,woId);
		 layer3Dao.insertQMDLDataIntoHbase(putlist,ConfigUtils.getString(ConfigUtil.QMDL_KPI_TABLE));
	}

	private void processKpiDataAndInsertIntoHbase(SortedMap<Long, QMDLLogCodeWrapper> woAggrigateMap, String woId, Layer3SummaryWrapper aggrigateWrapperData, NV3GSummaryWrapper aggrigate3gWrapperData, NV2GSummaryWrapper aggrigate2gWrapperData) throws IOException  {
		StringBuilder finalJson = convertKpiDataToCsvJson(woAggrigateMap,aggrigateWrapperData,woId,aggrigate3gWrapperData,aggrigate2gWrapperData);
		Put put=new Put(Bytes.toBytes(woId));
		
		NVLayer3Utils.addStringToPut(put, finalJson.toString(), QMDLConstant.JSONSTRING, QMDLConstant.COLUMN_FAMILY);
		NVLayer3Utils.addStringToPut(put, aggrigateWrapperData.getTimeStamp(), QMDLConstant.TIMESTAMP, QMDLConstant.COLUMN_FAMILY);
		NVLayer3Utils.addStringToPut(put, aggrigateWrapperData.getDeviceId(), QMDLConstant.DEVICE_ID, QMDLConstant.COLUMN_FAMILY);		

		
		List<Put> putlist=new ArrayList<>();
		putlist.add(put);
		layer3Dao.insertQMDLDataIntoHbase(putlist,ConfigUtils.getString(ConfigUtil.QMDL_DATA_TABLE));
	}

	private void processTypeWiseFile(TreeMap<Long, QMDLLogCodeWrapper> woParsedMap, WOFileDetail woFileDetail,
			Map<String, TreeMap<Long, Object>> kpiValuesMap,Map<String,Put> reportPutList, Layer3SummaryWrapper aggrigateWrapperData, String rowKey, List<String> processedFiles, String localFilePath) throws Exception {
		ReportUtil.createDirectory(localFilePath);
		String file = copyFileFromHdfsToLocal(woFileDetail,localFilePath);
		Boolean isQmdl = processZipFileToProcessReportDataAndQmdl(file, reportPutList,woFileDetail,woParsedMap, kpiValuesMap,rowKey,processedFiles,aggrigateWrapperData,localFilePath);
		setRecipeNameIntoAggrigateWrapper(woFileDetail,aggrigateWrapperData);
		ZipFile zipFile = null;
		try {
			zipFile = new ZipFile(file);
			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				String fileName = entry.getName();
				logger.info("Going to process file {} , {}", fileName,this.hashCode());
				if (fileName.contains(QMDLConstant.CSV_FILE_TYPE) && isQmdl) {
					
						nvL3CsvProcessService.processCsvFile(zipFile, entry,  woParsedMap, kpiValuesMap,
							aggrigateWrapperData,reportPutList, processedFiles,localFilePath);
					
					
				} else if (!isQmdl && fileName.contains(QMDLConstant.CSV_FILE_TYPE)) {
					nvCsvService.processNonQMDLDataFromCsv(zipFile, entry, woParsedMap, kpiValuesMap,
							aggrigateWrapperData,reportPutList,processedFiles,localFilePath);
				}else if(fileName.contains(QMDLConstant.LIVE_DRIVE_PREFIX)&&!fileName.contains(Symbol.DOT_STRING)){
					liveJsonService.processJsonFile(zipFile,entry,woParsedMap,kpiValuesMap,aggrigateWrapperData,isQmdl);
				}else if(fileName.contains(QMDLConstant.INBUILDING_FILE_PREFIX)){
					logger.info("going to process json for Inbuilding {}  ",fileName);
					nvBuildingService.processInBuildingFileData(zipFile, entry, woParsedMap, isQmdl, reportPutList,aggrigateWrapperData,kpiValuesMap,processedFiles,localFilePath);
				}

			}
		} finally {
			if (zipFile != null) {
				zipFile.close();

			}			
		}
	}

	private void setRecipeNameIntoAggrigateWrapper(WOFileDetail woFileDetail,
			Layer3SummaryWrapper aggrigateWrapperData) {
		aggrigateWrapperData.setRecipeName(woFileDetail.getWoRecipeMapping().getRecipe().getName());	
		aggrigateWrapperData.setRecipeId(woFileDetail.getWoRecipeMapping().getRecipe().getRecipeId());
	}



	private Boolean processZipFileToProcessReportDataAndQmdl(String file, Map<String, Put> reportPutMap,
			WOFileDetail woFileDetail, TreeMap<Long, QMDLLogCodeWrapper> woParsedMap, Map<String, TreeMap<Long, Object>> kpiValuesMap, String rowKey, List<String> processedFiles,Layer3SummaryWrapper aggrigateWrapperData, String localFilePath) throws IOException {
		Boolean isRootedDevice = Boolean.FALSE;
		ZipFile zipFile = null;
		logger.info("Going to process zip file {}  ", file);
		try {
			zipFile = new ZipFile(file);
			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			List<String> qmdlFiles = new ArrayList<>();
			while (entries.hasMoreElements()) {
				
				ZipEntry entry = entries.nextElement();
				logger.info("Going to process zip file {}  ", entry.getName());
				if (entry.getName().contains(QMDLConstant.QMDL_FILE_TYPE) || entry.getName().contains(NVLayer3Constants.DLF_FILE_TYPE)) {
					isRootedDevice = true;
//					nvQmdlProcessService.processQMDLFileData(zipFile, entry, woParsedMap, kpiValuesMap, rowKey,woFileDetail, qmdlFiles,processedFiles,aggrigateWrapperData,localFilePath); 
				} else if (entry.getName().contains(QMDLConstant.IMAGE_FILE_TYPE)||entry.getName().contains(QMDLConstant.IMAGE_FILE_JPG_TYPE)||entry.getName().contains(QMDLConstant.IMAGE_FILE_PNG_TYPE)) {
					putimageFileIntoMap(reportPutMap,  woFileDetail, zipFile, entry);
				}else if(entry.getName().contains(QMDLConstant.FLOOR_PLAN_JSON)){
					putJsonIntoMap(reportPutMap, woFileDetail, zipFile, entry);
				}

			}		
			
		} catch (Exception e) {
			logger.error("Error in processig zip file because of reason {} ", e.getMessage());
		} finally {
			if (zipFile != null) {
                zipFile.close();
            }
		}
		return isRootedDevice;
	}

	public void insertProcessedFileToHdfs(List<WOFileDetail> woFileList, List<String> processedFiles,
			String localFilePath) {
		if (processedFiles != null && !processedFiles.isEmpty()) {
			List<File> processedFileList = getFileListFromProcessedFile(processedFiles);

			logger.info("localFilePath {}", localFilePath);
			logger.info("processedFileList size {}", processedFileList.size());

			woFileList = woFileList.stream().filter(fileDetail -> !(fileDetail.getFileName().contains("floor_plan")))
					.collect(Collectors.toList());
			logger.info("woFileList {}", woFileList.size());
			try {
				if (!woFileList.isEmpty()) {

					String zipFilePath = localFilePath + woFileList.get(QMDLConstant.ZERO_INDEX).getFileName();

					if (zipFilePath.contains(Symbol.UNDERSCORE_STRING)) {
						zipFilePath = zipFilePath.substring(QMDLConstant.ZERO_INDEX,
								zipFilePath.lastIndexOf(Symbol.UNDERSCORE_STRING));
						zipFilePath = zipFilePath + Symbol.UNDERSCORE_STRING + new Date().getTime()
								+ ReportConstants.DOT_ZIP;
					} else if (zipFilePath.contains(Symbol.DOT_STRING)) {
						zipFilePath = zipFilePath.substring(QMDLConstant.ZERO_INDEX,
								zipFilePath.lastIndexOf(Symbol.DOT_STRING));
						zipFilePath = zipFilePath + Symbol.UNDERSCORE_STRING + new Date().getTime()
								+ ReportConstants.DOT_ZIP;
					}

					createZipFromFileList(processedFileList, zipFilePath);
					logger.info("zip file name is:{} ", zipFilePath);

					insertProcessedFileZipToHdfs(woFileList, zipFilePath);
				}
			} catch (Exception e) {
				logger.error("Problem in insertProcessedFileToHdfs {} ", Utils.getStackTrace(e));
			}
		}

	}

	public void createZipFromFileList(List<File> processedFileList, String zipFilePath) {
		try {
			ZipUtils.zip(processedFileList, zipFilePath);
		} catch (IOException e) {
			logger.info("IOException inside the method insertProcessedFileToHdfs{}", Utils.getStackTrace(e));
		}
	}


	public void insertProcessedFileZipToHdfs(List<WOFileDetail> woFileList, String zipFilePath) {
		try {
			File file = new File(zipFilePath);
			if (file.exists()) {
				String hdfsFileDir = ConfigUtils.getString(NVConfigUtil.NV_LAYER3_WO_FILE_HDFS_BASE_FILE_PATH)
						+ woFileList.get(QMDLConstant.ZERO_INDEX).getWoRecipeMapping().getId() + File.separator
						+ QMDLConstant.PROCESSED_FILES + File.separator;
				logger.info("inside the method insertProcessedFilesToHDFS filePathLocal {} hdfsFilePath  {} ",
						zipFilePath, hdfsFileDir);

				nvLayer3hdfsDao.deleteFileFromHDFSRecursively(hdfsFileDir);
				nvLayer3hdfsDao.copyFileFromLocalToHDFS(zipFilePath, hdfsFileDir.trim());
				updatePathofProcessedFile(woFileList, hdfsFileDir.trim());
			} else {
				logger.error("filedoes not exist at path filePathLocal {}", zipFilePath);
			}

		} catch (Exception e) {
			logger.info("Exception inside the method insertProcessedFileZipToHdfs {}", Utils.getStackTrace(e));
		}
	}

	public List<File> getFileListFromProcessedFile(List<String> processedFiles) {
		Set<String> processedFileSet = new HashSet<>();
		processedFileSet.addAll(processedFiles);

		List<File> processedFileList = new ArrayList<>();
		for (String filePath : processedFileSet) {
			File file = new File(filePath);
			if (file.exists()) {
				processedFileList.add(file);
			}
		}
		return processedFileList;
	}



	
	private void updatePathofProcessedFile(List<WOFileDetail> woFileList,String filepath) {	
		for (WOFileDetail woFile : woFileList) {
		String response = woFileDetailService.updatePathofFileProcessed(woFile, filepath);			
		if (response.equals(QMDLConstant.FAILURE)) {
			logger.error("Problem in updating Path  for processed file {} ", woFile.getFilePath());				
		}		
		}
	}

	
	
	private void putJsonIntoMap(Map<String, Put> reportPutMap, WOFileDetail woFileDetail, ZipFile zipFile,
			ZipEntry entry) throws IOException, ParseException {
			Put put= new Put(Bytes.toBytes(getKeyFromFileName(woFileDetail)));
			InputStream stream = zipFile.getInputStream(entry);
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader(stream, "UTF-8"));
			put.addColumn(QMDLConstant.COLUMN_FAMILY.getBytes(), Bytes.toBytes(QMDLConstant.JSON),jsonObject.toJSONString().getBytes());
			reportPutMap.put(entry.getName(), put);
	}

	private void putimageFileIntoMap(Map<String, Put> imagePutMap, WOFileDetail woFileDetail, ZipFile zipFile,
			ZipEntry entry) throws IOException {
		byte[] bytes = IOUtils.toByteArray(zipFile.getInputStream(entry));
		if (bytes != null && bytes.length > 0) {
			Put put = getRowKeyForImageFileWoWise(imagePutMap, woFileDetail, entry.getName());
			if (isInbuildinImageFile(entry)) {
				put.addColumn(QMDLConstant.COLUMN_FAMILY.getBytes(), Bytes.toBytes(QMDLConstant.IMAGE_FILE),entry.getName().getBytes());
				put.addColumn(QMDLConstant.COLUMN_FAMILY.getBytes(), Bytes.toBytes(QMDLConstant.FILE_PATH),woFileDetail.getFilePath().getBytes());
			}else if(skipSSVTCaptureImageRecipeFile(entry)){
			}else if(entry.getName().toUpperCase().contains(QMDLConstant.RSRP_THRESHOLD_IMG)){
				logger.info("Getting rsrp image");
				put.addColumn(QMDLConstant.COLUMN_FAMILY.getBytes(), Bytes.toBytes(QMDLConstant.RSRP_THRESHOLD_IMG),bytes);
			}else if(entry.getName().toUpperCase().contains(QMDLConstant.SINR_THRESHOLD_IMG)){
				logger.info("Getting sinr image");
				put.addColumn(QMDLConstant.COLUMN_FAMILY.getBytes(), Bytes.toBytes(QMDLConstant.SINR_THRESHOLD_IMG),bytes);
			}else{
				put.addColumn(QMDLConstant.COLUMN_FAMILY.getBytes(), Bytes.toBytes(QMDLConstant.IMAGE_FILE), bytes);
			}
			imagePutMap.put(entry.getName(), put);
		}
	}


	private boolean skipSSVTCaptureImageRecipeFile(ZipEntry entry) {
		return entry.getName().contains(ReportConstants.IMAGE_CATEGORY_AZIMUTH)||entry.getName().contains(ReportConstants.IMAGE_CATEGORY_ELECTRONIC_TILT)
				||entry.getName().contains(ReportConstants.IMAGE_CATEGORY_ANTENNA_HEIGHT) || entry.getName().contains(ReportConstants.IMAGE_CATEGORY_PANORAMA_VIEW);
	}

	private boolean isInbuildinImageFile(ZipEntry entry) {
		return entry.getName().contains(QMDLConstant.NO_ACCESS_IMAGE_PREFIX)||entry.getName().contains(QMDLConstant.FLOORPLAN_IMAGE_PREFIX)||entry.getName().contains(QMDLConstant.BACKGROUND_IMAGE_PREFIX);
	}

	private Put getRowKeyForImageFileWoWise(Map<String, Put> imagePutMap, WOFileDetail woFileDetail,
			String imageName) {
		if (NVLayer3Utils.isInBuidlingRecord(woFileDetail)) {
			if (imageName.contains(QMDLConstant.FLOORPLAN_IMAGE_PREFIX)||imageName.contains(QMDLConstant.BACKGROUND_IMAGE_PREFIX)) {
				return new Put(Bytes.toBytes(getKeyFromFileName(woFileDetail)));
			}
		}
			return new Put(Bytes.toBytes(getKeyFromFileName(woFileDetail)+ String.valueOf(imagePutMap.size())));
	}

	private String copyFileFromHdfsToLocal(WOFileDetail woFileDetail,String localFilePath)  {
		try {
			return nvHdfsDao.copyFileFromHdfsToLocalPath(woFileDetail.getFilePath(), localFilePath, woFileDetail.getFileName());
		} catch (DaoException e) {
		logger.error("Error inside the method copyFileFromHdfsToLocal {}",e.getMessage());
		}
		return null;
	}

	private Map<String, List<WOFileDetail>> getWorkOrderMapfromList(List<WOFileDetail> woList) {
		Map<String, List<WOFileDetail>> workOrderMap = new HashMap<>();
		for (WOFileDetail woFile : woList) {
			if (isValidFileEntry(woFile)) {
				logger.info("Going to process {}   ",woFile.getFileName());
				String fileKey=getKeyFromFileName(woFile);
				if (workOrderMap.containsKey(fileKey)) {
					workOrderMap.get(fileKey).add(woFile);
				} else {
					List<WOFileDetail> list = new ArrayList<>();
					list.add(woFile);
					workOrderMap.put(fileKey, list);
				}
			}
		}
		return workOrderMap;
	}

	
	private String getKeyFromFileName(WOFileDetail woFile) {
		if (woFile.getWoRecipeMapping().getGenericWorkorder().getTemplateType().name()
				.equals(TemplateType.NV_ADHOC_IB.name())) {
			logger.info("Getting rowkey as:{}", getRowKeyForInbuildingAdhocTest(woFile));
			return getRowKeyForInbuildingAdhocTest(woFile);

		} else {
			return NVLayer3Utils.getRowkeyFromWorkOrderId(woFile.getWoRecipeMapping().getGenericWorkorder().getId())
					+ NVWorkorderUtils.getOperatorNameFromFileName(woFile.getFileName())
					+ NVLayer3Utils.getRowkeyFromWorkOrderId(woFile.getWoRecipeMapping().getId());
		}
	}
	
	
	public String getRowKeyForInbuildingAdhocTest(WOFileDetail woFile) {
		List<WOFileDetail> woFileDetailList = woFileDetailDao
				.findFileByRecipeMappingId(woFile.getWoRecipeMapping().getId());
         
		if(woFileDetailList.size()>1) {
			woFileDetailList = woFileDetailList.stream()
					.filter(fileDetail -> !(fileDetail.getFileName().contains(NVLayer3Constants.WO_FILE_NAME_PREFIX_FLOOR_PLAN)))
					.collect(Collectors.toList());
		}

		logger.info("woFileList {}", woFileDetailList.size());
		return NVLayer3Utils.getRowkeyFromWorkOrderId(woFile.getWoRecipeMapping().getGenericWorkorder().getId())
				+ NVWorkorderUtils.getOperatorNameFromFileName(woFileDetailList.get(0).getFileName())
				+ NVLayer3Utils.getRowkeyFromWorkOrderId(woFile.getWoRecipeMapping().getId());
	}

	private boolean isValidFileEntry(WOFileDetail woFile) {
		return woFile.getWoRecipeMapping()!=null&&woFile.getFileName()!=null &&woFile.getFiletype()!=null&&woFile.getFilePath()!=null;
	}


	/**
	 * Gets the unprocess file from data base.
	 *
	 * @return the unprocess file from data base
	 * @throws IOException
	 */
	@Override
	@Transactional
    public List<WOFileDetail> getUnprocessFileFromDataBase() throws IOException {
		try {
			 List<Integer>woList= woFileDetailService.getUnprocessedWorkOrder();
			 for(Integer wo:woList){
				 processUnprocessQMDLFiles(wo,null,null);
			 }
		} catch (RestException e) {
			logger.error("Getting error in fetching un process file list {} ",Utils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	/**
	 * Creates the and insert put for raw data.
	 *
	 * @param woAggrigateMap the qmdl time wise data map
	 * @param aggrigateWrapperData
	 * @param woId 
	 * @param aggrigate3gWrapperData 
	 * @param aggrigate2gWrapperData 
	 * @param rowPrefix the row prefix
	 * @throws RestException the rest exception
	 */
	private StringBuilder convertKpiDataToCsvJson(SortedMap<Long, QMDLLogCodeWrapper> woAggrigateMap, Layer3SummaryWrapper aggrigateWrapperData, String woId, NV3GSummaryWrapper aggrigate3gWrapperData, NV2GSummaryWrapper aggrigate2gWrapperData) {
		StringBuilder finalJsonString=getIntialStringForJson();
		String band=null;
		String  bandwidth =null;
		Double dlMaxValue=null;
		Integer previousPCI = null;
		Integer previousCellId = null;
		Integer cgi=null;
		Integer eNodeBId=null;
		Integer previousMNC = aggrigateWrapperData.getMNC()!=null?aggrigateWrapperData.getMNC():null;
		Integer previousMCC = aggrigateWrapperData.getMCC()!=null?aggrigateWrapperData.getMCC():null;
		Double pmosValue=aggrigateWrapperData.getpMosFirstDetail()!=null?aggrigateWrapperData.getpMosFirstDetail():null;
		String rrcState=aggrigateWrapperData.getRrcStateFirstValue()!=null?aggrigateWrapperData.getRrcStateFirstValue():null;
		Boolean iscallsetup = false;
		
		logger.info("Getting mnc mcc from summary mcc {} mnc {}",previousMNC,previousMCC);


		String testType = null;
		for(SortedMap.Entry<Long, QMDLLogCodeWrapper> wrapperEntry:woAggrigateMap.entrySet()){

			StringBuilder json=new StringBuilder();
			QMDLLogCodeWrapper wrapper = setValuesIntoAggrigatedWrapper(aggrigateWrapperData, wrapperEntry,band,bandwidth,dlMaxValue);

			if (previousPCI == null || !previousPCI.equals(wrapper.getPci())) {
				previousPCI = wrapper.getPci();
				previousCellId = wrapper.getCellid();
				cgi=wrapper.getCgi();
				eNodeBId=wrapper.geteNodebId();
			}else{
				previousCellId = wrapper.getCellid() != null ? wrapper.getCellid() : previousCellId;
				cgi=wrapper.getCgi()!=null?wrapper.getCgi():cgi;
				eNodeBId=wrapper.geteNodebId()!=null ?wrapper.geteNodebId():eNodeBId;
				wrapper.setCellid(previousCellId);
				wrapper.setCgi(cgi);
				wrapper.seteNodebId(eNodeBId);
				wrapper.setSectorId(previousCellId);
			}

			
			
			if(wrapper.getCallSetupSuccessCount()!=null && wrapper.getCallSetupSuccessCount()>0) {
				wrapper.setIsCallSetup(true);
			}

			if ((wrapper.getCallSuccessCount() != null && wrapper.getCallSuccessCount() > 0)
					|| (wrapper.getCallDropCount() != null && wrapper.getCallDropCount() > 0)) {
				wrapper.setIsCallSetup(false);
			}

			if (wrapper.getIsCallSetup() != null) {
				iscallsetup = wrapper.getIsCallSetup();
			} else {
				wrapper.setIsCallSetup(iscallsetup);
			}


			



			if(wrapper.getMnc()!=null) {
				previousMNC=wrapper.getMnc();
			}else {
				wrapper.setMnc(previousMNC);
			}

			if(wrapper.getMcc()!=null) {
				previousMCC=wrapper.getMcc();
			}else {
				wrapper.setMcc(previousMCC);
			}

			if(wrapper.getTestType()==null) {
				wrapper.setTestType(testType);				
			}
			else {
				testType=wrapper.getTestType();
			}

			if(wrapper.getpMos()!=null) {
				pmosValue=NVLayer3Utils.getAvgFromArrayUptoFourDecimals(wrapper.getpMos());
			}else {
				wrapper.setpMos(NVLayer3Utils.getDoubleArrayValue(pmosValue));
			}
			
			if(wrapper.getRrcState()!=null) {
				rrcState=wrapper.getRrcState();
			}else {
				wrapper.setRrcState(rrcState);
			}


			processGeoHashforWrapperLatLon(wrapper);
			dlMaxValue=getDlPeakValue(dlMaxValue,wrapperEntry.getValue(),band,bandwidth);
			processDominantKpi(wrapper, wrapperEntry);
			String recordString = addValuesIntoJson(aggrigateWrapperData, woId, json, wrapper,aggrigate3gWrapperData,aggrigate2gWrapperData);
			//Temporary
			band=wrapper.getBand()!=null?wrapper.getBand():band;
			bandwidth=wrapper.getDl_Bandwidth()!=null?wrapper.getDl_Bandwidth():bandwidth;
			finalJsonString.append(recordString);
		}
		addPostFixOfcsvIntoJson(finalJsonString);
		return finalJsonString;
	}
	
	private void processGeoHashforWrapperLatLon(QMDLLogCodeWrapper wrapper) {
		try {
			if(wrapper.getLat() !=null && wrapper.getLon() !=null) {
				LatLng latlon = new LatLng();
				latlon.setLatitude(wrapper.getLat());
				latlon.setLongitude(wrapper.getLon());
				wrapper.setGeoHash(GeoHashUtils.getGeoHashForLocation(latlon));
			}
		} catch (Exception e) {
			logger.info("Error inside method processGeoHashforWrapperLatLon{}",Utils.getStackTrace(e));
		}
	}

	private void processDominantKpi(QMDLLogCodeWrapper wrapper, Entry<Long, QMDLLogCodeWrapper> wrapperEntry) {
		
		NeighbourDetailWrapper neighbourDetail = null;
		NeighbourDetailWrapper neighbourDetailRx1 = null;
		NeighbourDetailWrapper neighbourDetailRx0 = null;
		if(wrapperEntry.getValue().getNeighbourDataList() != null) {
		neighbourDetail = wrapperEntry.getValue().getNeighbourDataList().stream().filter(p -> p.getInstRsrp() != null)
				.max(Comparator.comparing(NeighbourDetailWrapper::getInstRsrp))
				.orElse(null);
		neighbourDetailRx1 = wrapperEntry.getValue().getNeighbourDataList().stream().filter(p -> p.getInstRsrpRx1() != null)
				.max(Comparator.comparing(NeighbourDetailWrapper::getInstRsrpRx1))
				.orElse(null);
		neighbourDetailRx0 = wrapperEntry.getValue().getNeighbourDataList().stream().filter(p -> p.getInstRsrpRx0() != null)
				.max(Comparator.comparing(NeighbourDetailWrapper::getInstRsrpRx0))
				.orElse(null);
		}
		
		if(neighbourDetail != null) {
			if (NVLayer3Utils.getAvgFromArray(wrapper.getMeasureRSRPData()) != null && neighbourDetail.getInstRsrp() > NVLayer3Utils.getAvgFromArray(wrapper.getMeasureRSRPData())) {
				wrapper.setDominantChannelRSSI(NVLayer3Utils.getDoubleArrayValue(wrapper.getDominantChannelRSSI(), neighbourDetail.getInstRssi()));
				wrapper.setDominantChannelRSRQ(NVLayer3Utils.getDoubleArrayValue(wrapper.getDominantChannelRSRQ(), neighbourDetail.getInstRsrq()));
				wrapper.setDominantChannelRSRP(NVLayer3Utils.getDoubleArrayValue(wrapper.getDominantChannelRSRP(), neighbourDetail.getInstRsrp()));
				if (neighbourDetail.getPci() != null) {
					wrapper.setDominantChannelPCI(NVLayer3Utils.getLatestValue(wrapper.getDominantChannelPCI(), neighbourDetail.getPci()));
				}
			}
			else {
				wrapper.setDominantChannelRSSI(wrapper.getrSSIData());
				wrapper.setDominantChannelRSRQ(wrapper.getMeasureRSRQData());
				wrapper.setDominantChannelRSRP(wrapper.getMeasureRSRPData());
				wrapper.setDominantChannelPCI(wrapper.getPci());
			}
		}
		else {
			wrapper.setDominantChannelRSSI(wrapper.getrSSIData());
			wrapper.setDominantChannelRSRQ(wrapper.getMeasureRSRQData());
			wrapper.setDominantChannelRSRP(wrapper.getMeasureRSRPData());
			wrapper.setDominantChannelPCI(wrapper.getPci());
		}
		
		setDominantChannelKPIRx0(wrapper, neighbourDetailRx0);
		setDominantChannelKPIRx1(wrapper, neighbourDetailRx1);
	}

	private void setDominantChannelKPIRx1(QMDLLogCodeWrapper wrapper, NeighbourDetailWrapper neighbourDetailRx1) {
		
		if(neighbourDetailRx1 != null) {
			if (NVLayer3Utils.getAvgFromArray(wrapper.getrSRPRx1Data()) != null && neighbourDetailRx1.getInstRsrpRx1() > NVLayer3Utils.getAvgFromArray(wrapper.getrSRPRx1Data())) {
				wrapper.setDominantChannelRSSIRx1(NVLayer3Utils.getDoubleArrayValue(wrapper.getDominantChannelRSSIRx1(), neighbourDetailRx1.getInstRssiRx1()));
				wrapper.setDominantChannelRSRQRx1(NVLayer3Utils.getDoubleArrayValue(wrapper.getDominantChannelRSRQRx1(), neighbourDetailRx1.getInstRsrqRx1()));
				wrapper.setDominantChannelRSRPRx1(NVLayer3Utils.getDoubleArrayValue(wrapper.getDominantChannelRSRPRx1(), neighbourDetailRx1.getInstRsrpRx1()));
			}
			else {
				wrapper.setDominantChannelRSSIRx1(wrapper.getrSSIRx1Data());
				wrapper.setDominantChannelRSRQRx1(wrapper.getrSRQRx1Data());
				wrapper.setDominantChannelRSRPRx1(wrapper.getrSRPRx1Data());
			}
		}
		else {
			wrapper.setDominantChannelRSSIRx1(wrapper.getrSSIRx1Data());
			wrapper.setDominantChannelRSRQRx1(wrapper.getrSRQRx1Data());
			wrapper.setDominantChannelRSRPRx1(wrapper.getrSRPRx1Data());
		}
	}

	private void setDominantChannelKPIRx0(QMDLLogCodeWrapper wrapper, NeighbourDetailWrapper neighbourDetailRx0) {
		
		if(neighbourDetailRx0 != null) {
			if (NVLayer3Utils.getAvgFromArray(wrapper.getrSRPRx0Data()) != null && neighbourDetailRx0.getInstRsrpRx0() > NVLayer3Utils.getAvgFromArray(wrapper.getrSRPRx0Data())) {
				wrapper.setDominantChannelRSSIRx0(NVLayer3Utils.getDoubleArrayValue(wrapper.getDominantChannelRSSIRx0(), neighbourDetailRx0.getInstRssiRx0()));
				wrapper.setDominantChannelRSRQRx0(NVLayer3Utils.getDoubleArrayValue(wrapper.getDominantChannelRSRQRx0(), neighbourDetailRx0.getInstRsrqRx0()));
				wrapper.setDominantChannelRSRPRx0(NVLayer3Utils.getDoubleArrayValue(wrapper.getDominantChannelRSRPRx0(), neighbourDetailRx0.getInstRsrpRx0()));
			}
			else {
				wrapper.setDominantChannelRSSIRx0(wrapper.getrSSIRx0Data());
				wrapper.setDominantChannelRSRQRx0(wrapper.getrSRQRx0Data());
				wrapper.setDominantChannelRSRPRx0(wrapper.getrSRPRx0Data());
			}
		}
		else {
			wrapper.setDominantChannelRSSIRx0(wrapper.getrSSIRx0Data());
			wrapper.setDominantChannelRSRQRx0(wrapper.getrSRQRx0Data());
			wrapper.setDominantChannelRSRPRx0(wrapper.getrSRPRx0Data());
		}
	}

	private String addValuesIntoJson(Layer3SummaryWrapper aggrigateWrapperData, String woId, StringBuilder json,
			QMDLLogCodeWrapper wrapper, NV3GSummaryWrapper aggrigate3gWrapperData, NV2GSummaryWrapper aggrigate2gWrapperData) {
		addlatLongintoString(wrapper.getLat(), wrapper.getLon(), json);
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getTimeStamp());
		addLTEKPIIntoJson(aggrigateWrapperData, woId, json, wrapper);
		add3G2GKPIIntoJson(json, wrapper, aggrigate3gWrapperData, aggrigate2gWrapperData);
		addAdvanceKPIandEventsIntoJson(aggrigateWrapperData, json, wrapper);
		addThroughputIntoJson(aggrigateWrapperData, json, wrapper);
		addPmosIntoJson(aggrigateWrapperData, json, wrapper);
		addHandoverInterruptionTimeIntoJson(aggrigateWrapperData,json, wrapper);		
		addNewVolteCallEventsIntoJson(json, wrapper);
		addPDCPThroughputIntoJson(aggrigateWrapperData,json,wrapper);
		addRtpPktLossAndTotalPktCntIntoJson(json,wrapper,aggrigateWrapperData);
		addPingHostvalueIntoJson(json,wrapper);
		addRLCThroughputIntoJson(aggrigateWrapperData,json,wrapper);
		addNewVolteCallSetupEventsIntoJson(json, wrapper);		
		return getFinalJsonForRecord(json.toString());
	}
	
	
	private void addRLCThroughputIntoJson(Layer3SummaryWrapper aggrigateWrapperData, StringBuilder json,
			QMDLLogCodeWrapper wrapper) {
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArray(wrapper.getRlcThroughput()));
		aggrigateWrapperData.setRlcThroughput(NVLayer3Utils.getDoubleAggrigateArray(aggrigateWrapperData.getRlcThroughput(), wrapper.getRlcThroughput()));
	}
	private void addPingHostvalueIntoJson(StringBuilder json, QMDLLogCodeWrapper wrapper) {
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getHost());				
	}
	private void addRtpPktLossAndTotalPktCntIntoJson(StringBuilder json, QMDLLogCodeWrapper wrapper, Layer3SummaryWrapper aggrigateWrapperData) {
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getNumberOfRtpPacketsLost());
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getTotalPacketCount());		
		
		aggrigateWrapperData.setNumberOfRtpPacketsLost(NVLayer3Utils.addDoubleValue(aggrigateWrapperData.getNumberOfRtpPacketsLost(),wrapper.getNumberOfRtpPacketsLost()));
		aggrigateWrapperData.setTotalPacketCount(NVLayer3Utils.addDoubleValue(aggrigateWrapperData.getTotalPacketCount(),wrapper.getTotalPacketCount()));
	}

	private void addPDCPThroughputIntoJson(Layer3SummaryWrapper aggrigateWrapperData, StringBuilder json, QMDLLogCodeWrapper wrapper) {
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArray(wrapper.getPdcpThroughput()));
		aggrigateWrapperData.setPdcpThroughput(NVLayer3Utils.getDoubleAggrigateArray(aggrigateWrapperData.getPdcpThroughput(), wrapper.getPdcpThroughput()));
	}

	public void addNewVolteCallEventsIntoJson(StringBuilder json, QMDLLogCodeWrapper wrapper) {

		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getCallSetupSuccessCount());

		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getVolteMTCallFailure());
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getVolteMTCallDrop());
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getVolteMTCallSetupSuccess());

		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getVolteMOCallFailure());
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getVolteMOCallDrop());
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getVolteMOCallSetupSuccess());

	}
	
	public void addNewVolteCallSetupEventsIntoJson(StringBuilder json, QMDLLogCodeWrapper wrapper) {
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getCallSetupCount());
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getVolteMTCallSetup());
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getVolteMOCallSetup());
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArray(wrapper.getCallSetupSuccessTime()));
	}

	public void addHandoverInterruptionTimeIntoJson(Layer3SummaryWrapper aggrigateWrapperData, StringBuilder json, QMDLLogCodeWrapper wrapper) {
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArray(wrapper.getHandoverInterruptionTimeOfQCI1()));		
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArray(wrapper.getHandoverInterruptionTimeOfQCI9DL()));		
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArray(wrapper.getHandoverInterruptionTimeOfQCI9UL()));		


		aggrigateWrapperData.setHandoverInterruptionTimeOfQCI1(NVLayer3Utils.getDoubleAggrigateArray(aggrigateWrapperData.getHandoverInterruptionTimeOfQCI1(), wrapper.getHandoverInterruptionTimeOfQCI1()));
		aggrigateWrapperData.setHandoverInterruptionTimeOfQCI9DL(NVLayer3Utils.getDoubleAggrigateArray(aggrigateWrapperData.getHandoverInterruptionTimeOfQCI9DL(), wrapper.getHandoverInterruptionTimeOfQCI9DL()));
		aggrigateWrapperData.setHandoverInterruptionTimeOfQCI9UL(NVLayer3Utils.getDoubleAggrigateArray(aggrigateWrapperData.getHandoverInterruptionTimeOfQCI9UL(), wrapper.getHandoverInterruptionTimeOfQCI9UL()));

	}

	private void addPmosIntoJson(Layer3SummaryWrapper aggrigateWrapperData, StringBuilder json,
			QMDLLogCodeWrapper wrapper) {
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArrayUptoFourDecimals(wrapper.getpMos()));		
	}

	private void addThroughputIntoJson(Layer3SummaryWrapper aggrigateWrapperData, StringBuilder json,
			QMDLLogCodeWrapper wrapper) {
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArray(wrapper.getMacDlThroughput()));
		setThroughputIntoAggrigatedWrapper(aggrigateWrapperData, wrapper);
	}

	private void setThroughputIntoAggrigatedWrapper(Layer3SummaryWrapper aggrigateWrapperData,
			QMDLLogCodeWrapper wrapper) {
		aggrigateWrapperData.setMacDlThroughPut(NVLayer3Utils.getDoubleAggrigateArray(aggrigateWrapperData.getMacDlThroughPut(), wrapper.getMacDlThroughput()));
	}

	private void addAdvanceKPIandEventsIntoJson(Layer3SummaryWrapper aggrigateWrapperData, StringBuilder json,
			QMDLLogCodeWrapper wrapper) {
		addInstantaneousMosIntoJson(json,wrapper,aggrigateWrapperData);		
		addHttpDropParameterIntoJson(json, wrapper);
		addPDSCHThroughput(aggrigateWrapperData, json, wrapper);
		addCSFBCallDataToJson(json,wrapper);
		addWifiParamsToJson(json,wrapper);
		addPDSCHPCellSCellThroughput(json, wrapper);			
		addSpeedTestInbuildinParameterToJson(json, wrapper);		
		addTauTimeDetail(json,wrapper);
		addRRCConnectionStatesToJson(json, wrapper);
		addLinkAdaptationKPItoJson(json, wrapper,aggrigateWrapperData);
		addPUSCHThroughput(aggrigateWrapperData, json, wrapper);
		addPUSCHModulationType(aggrigateWrapperData, json, wrapper);
		addPUSCHMcsIndex(aggrigateWrapperData, json, wrapper);	
		addTransmissionMode(aggrigateWrapperData, json, wrapper);
		addHandOverInterruption(aggrigateWrapperData,json,wrapper);
		addDominantKPI(aggrigateWrapperData, json, wrapper);
		addMCSInfo(aggrigateWrapperData, json, wrapper);
		addSchedulingGrantInfo(aggrigateWrapperData, json, wrapper);
		addPDSCHBler(aggrigateWrapperData, json, wrapper);
		addMacThroughput(aggrigateWrapperData, json, wrapper);
		addTACDetail(json,wrapper);
		addKPICountIntoJson(json,wrapper);
		addUETxPower(aggrigateWrapperData, json, wrapper);
		addRACHInfo(aggrigateWrapperData, json, wrapper);
		addLteCellCPType(aggrigateWrapperData, json, wrapper);
		addStateInfoKpiIntoJson(json,wrapper);
		addAvgULPRBIntoJson(aggrigateWrapperData, json, wrapper);
		addGeoHashIntoJson(json,wrapper);
		addAttachLatencyIntoJson(json, wrapper);
		addReselectionSuccessIntoJson(json, wrapper, aggrigateWrapperData);
		addLatencyAndPingBufferSizeIntoJson(aggrigateWrapperData, json, wrapper);		
		addAttachCompleteDetachRequestIntoJson(json, wrapper);	
		addDlUlModulationTypeAndDlPRBIntoJson(json, wrapper, aggrigateWrapperData);		
		addReestablishmentRequestJitterPacketLossIntoJson(json, wrapper, aggrigateWrapperData);
		addImsRegistrationSetupTimeFromWrapper(aggrigateWrapperData, json, wrapper);
		addVoltePaginEventIntoJson(json, wrapper, aggrigateWrapperData);
		addVolteMoMtCallEventIntoJson(json, wrapper, aggrigateWrapperData);
	}

	

	private void addVolteMoMtCallEventIntoJson(StringBuilder json, QMDLLogCodeWrapper wrapper,
			Layer3SummaryWrapper aggrigateWrapperData) {
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getVolteMTCallAttempts());
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getVolteMTCallSuccess());		
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getVolteMOCallAttempts());
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getVolteMOCallSuccess());		
	
		aggrigateWrapperData.setVolteMTCallAttempts(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getVolteMTCallAttempts(), wrapper.getVolteMTCallAttempts()));
		aggrigateWrapperData.setVolteMTCallSuccess(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getVolteMTCallSuccess(), wrapper.getVolteMTCallSuccess()));
		aggrigateWrapperData.setVolteMTCallDrop(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getVolteMTCallDrop(), wrapper.getVolteMTCallDrop()));
		aggrigateWrapperData.setVolteMTCallFailure(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getVolteMTCallFailure(), wrapper.getVolteMTCallFailure()));
		aggrigateWrapperData.setVolteMTCallSetupSuccess(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getVolteMTCallSetupSuccess(), wrapper.getVolteMTCallSetupSuccess()));
		aggrigateWrapperData.setVolteMTCallSetup(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getVolteMTCallSetup(), wrapper.getVolteMTCallSetup()));
			
		
		aggrigateWrapperData.setVolteMOCallAttempts(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getVolteMOCallAttempts(), wrapper.getVolteMOCallAttempts()));
		aggrigateWrapperData.setVolteMOCallSuccess(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getVolteMOCallSuccess(), wrapper.getVolteMOCallSuccess()));
		aggrigateWrapperData.setVolteMOCallDrop(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getVolteMOCallDrop(), wrapper.getVolteMOCallDrop()));
		aggrigateWrapperData.setVolteMOCallFailure(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getVolteMOCallFailure(), wrapper.getVolteMOCallFailure()));
		aggrigateWrapperData.setVolteMOCallSetupSuccess(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getVolteMOCallSetupSuccess(), wrapper.getVolteMOCallSetupSuccess()));
		aggrigateWrapperData.setVolteMOCallSetup(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getVolteMOCallSetup(), wrapper.getVolteMOCallSetup()));

	}

	private void addVoltePaginEventIntoJson(StringBuilder json, QMDLLogCodeWrapper wrapper,
			Layer3SummaryWrapper aggrigateWrapperData) {
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getVoltePagingAttempts());
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getVoltePagingSuccess());		
		aggrigateWrapperData.setVoltePagingAttempts(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getVoltePagingAttempts(), wrapper.getVoltePagingAttempts()));
		aggrigateWrapperData.setVoltePagingSuccess(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getVoltePagingSuccess(), wrapper.getVoltePagingSuccess()));
	
		
	}

	public void addImsRegistrationSetupTimeFromWrapper(Layer3SummaryWrapper aggrigateWrapperData, StringBuilder json,
			QMDLLogCodeWrapper wrapper) {
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArray(wrapper.getImsRegistrationSetupTime()));
//		aggrigateWrapperData.setImsRegistrationSetupTime(NVLayer3Utils.getDoubleAggrigateArray(aggrigateWrapperData.getImsRegistrationSetupTime(), wrapper.getImsRegistrationSetupTime()));
		aggrigateWrapperData.setImsRegistrationSetupTime(NVLayer3Utils.getDoubleArrayValue(aggrigateWrapperData.getImsRegistrationSetupTime(), NVLayer3Utils.getAvgFromArray(wrapper.getImsRegistrationSetupTime())));
	}
	
	private void addReestablishmentRequestJitterPacketLossIntoJson(StringBuilder json,
			QMDLLogCodeWrapper wrapper, Layer3SummaryWrapper aggrigateWrapperData) {
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getRrcConnectionReestablishmentRequest());
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArray(wrapper.getJitter()));
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArray(wrapper.getPacketLossValueAndCount()));
		
		aggrigateWrapperData.setRrcConnectionReestablishmentRequest(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getRrcConnectionReestablishmentRequest(), wrapper.getRrcConnectionReestablishmentRequest()));
		aggrigateWrapperData.setJitterAggregated(NVLayer3Utils.getDoubleArrayValue(aggrigateWrapperData.getJitterAggregated(),NVLayer3Utils.getAvgFromArray( wrapper.getJitter())));
		aggrigateWrapperData.setPacketLossValueAndCount(NVLayer3Utils.getDoubleArrayValue(aggrigateWrapperData.getPacketLossValueAndCount(), NVLayer3Utils.getAvgFromArray(wrapper.getPacketLossValueAndCount())));
	
	}

	private void addDlUlModulationTypeAndDlPRBIntoJson(StringBuilder json, QMDLLogCodeWrapper wrapper, Layer3SummaryWrapper aggrigateWrapperData) {
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getDlModulationType());
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getUlModulationType());
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArray(wrapper.getB173DlPRB()));
		
		aggrigateWrapperData.setDlModulationType(wrapper.getDlModulationType());
		aggrigateWrapperData.setUlModulationType(wrapper.getUlModulationType());
		aggrigateWrapperData.setB173DlPRB(NVLayer3Utils.getDoubleAggrigateArray(aggrigateWrapperData.getB173DlPRB(), wrapper.getB173DlPRB()));
	}
	
	public void addAttachCompleteDetachRequestIntoJson(StringBuilder json, QMDLLogCodeWrapper wrapper) {
		NVLayer3Utils.addValueToStringBuilder(json,wrapper.getAttachComplete());
		NVLayer3Utils.addValueToStringBuilder(json,wrapper.getDetachRequest());
	}

	public void addLatencyAndPingBufferSizeIntoJson(Layer3SummaryWrapper aggrigateWrapperData, StringBuilder json,
			QMDLLogCodeWrapper wrapper) {
		NVLayer3Utils.addValueToStringBuilder(json,wrapper.getPingBufferSize());
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArray(wrapper.getLatencyBufferSize32Bytes()));
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArray(wrapper.getLatencyBufferSize1000Bytes()));
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArray(wrapper.getLatencyBufferSize1500Bytes()));

		
		aggrigateWrapperData.setPingBufferSize(NVLayer3Utils.setValuesIntoSet(aggrigateWrapperData.getPingBufferSize(),wrapper.getPingBufferSize()));
		aggrigateWrapperData.setAvgLatencyBufferSize32Bytes(NVLayer3Utils.getDoubleAggrigateArray(aggrigateWrapperData.getAvgLatencyBufferSize32Bytes(), wrapper.getLatencyBufferSize32Bytes()));
		aggrigateWrapperData.setMinLatencyBufferSize32Bytes(NVLayer3Utils.getMinValue(NVLayer3Utils.getAvgFromArray(wrapper.getLatencyBufferSize32Bytes()),aggrigateWrapperData.getMinLatencyBufferSize32Bytes()));
		aggrigateWrapperData.setMaxLatencyBufferSize32Bytes(NVLayer3Utils.getMaxValue(NVLayer3Utils.getAvgFromArray(wrapper.getLatencyBufferSize32Bytes()),aggrigateWrapperData.getMaxLatencyBufferSize32Bytes()));

		aggrigateWrapperData.setAvgLatencyBufferSize1000Bytes(NVLayer3Utils.getDoubleAggrigateArray(aggrigateWrapperData.getAvgLatencyBufferSize1000Bytes(), wrapper.getLatencyBufferSize1000Bytes()));
		aggrigateWrapperData.setMinLatencyBufferSize1000Bytes(NVLayer3Utils.getMinValue(NVLayer3Utils.getAvgFromArray(wrapper.getLatencyBufferSize1000Bytes()),aggrigateWrapperData.getMinLatencyBufferSize1000Bytes()));
		aggrigateWrapperData.setMaxLatencyBufferSize1000Bytes(NVLayer3Utils.getMaxValue(NVLayer3Utils.getAvgFromArray(wrapper.getLatencyBufferSize1000Bytes()),aggrigateWrapperData.getMaxLatencyBufferSize1000Bytes()));

		aggrigateWrapperData.setAvgLatencyBufferSize1500Bytes(NVLayer3Utils.getDoubleAggrigateArray(aggrigateWrapperData.getAvgLatencyBufferSize1500Bytes(), wrapper.getLatencyBufferSize1500Bytes()));
		aggrigateWrapperData.setMinLatencyBufferSize1500Bytes(NVLayer3Utils.getMinValue(NVLayer3Utils.getAvgFromArray(wrapper.getLatencyBufferSize1500Bytes()),aggrigateWrapperData.getMinLatencyBufferSize1500Bytes()));
		aggrigateWrapperData.setMaxLatencyBufferSize1500Bytes(NVLayer3Utils.getMaxValue(NVLayer3Utils.getAvgFromArray(wrapper.getLatencyBufferSize1500Bytes()),aggrigateWrapperData.getMaxLatencyBufferSize1500Bytes()));

	}
	
	private void addReselectionSuccessIntoJson(StringBuilder json, QMDLLogCodeWrapper wrapper, Layer3SummaryWrapper aggrigateWrapperData) {
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getReselectionSuccess());
		if(wrapper.getReselectionSuccess() != null) {
			aggrigateWrapperData.setReselectionSuccess(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getReselectionSuccess(),wrapper.getReselectionSuccess()));
		}
	}
	
	private void addAttachLatencyIntoJson(StringBuilder json, QMDLLogCodeWrapper wrapper) {
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArray(wrapper.getConnectionSetupTime()));
	}

	
	
	
	private void addGeoHashIntoJson(StringBuilder json, QMDLLogCodeWrapper wrapper) {
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getGeoHash());
	}

	private void add3G2GKPIIntoJson(StringBuilder json, QMDLLogCodeWrapper wrapper,
			NV3GSummaryWrapper aggrigate3gWrapperData, NV2GSummaryWrapper aggrigate2gWrapperData) {
		add3GDataIntoJson(json,wrapper,aggrigate3gWrapperData);
		add2GDataIntoJson(json,wrapper,aggrigate2gWrapperData);
	}

	private void addLTEKPIIntoJson(Layer3SummaryWrapper aggrigateWrapperData, String woId, StringBuilder json,
			QMDLLogCodeWrapper wrapper) {
		addLogCodeWiseDataIntoJson(aggrigateWrapperData, json, wrapper);
		addEventsAndSiteInformationIntoJson(aggrigateWrapperData, json, wrapper);
		addB169ToJsonLte(json, wrapper);
		addXpointAndYPointIntoJson(json, wrapper);
		addLiveParamForReport(json, wrapper,aggrigateWrapperData);
		addWIFIParamIntoJson(json, wrapper,aggrigateWrapperData);
		addCoverageAndDownloadTypeParamIntoJson(json, wrapper);
		addMncMccEnobeIdAndHandoverDetailIntoJson(json, wrapper);
		addHandoverDetailAndBenchMarkReportDetailIntoJson(json, wrapper,woId);
		addSmsDetail(json, wrapper);
		addHttpDetail(json, wrapper);
		addTauDetail(json, wrapper);
		addExtraKpiDataIntoJson(json,wrapper,aggrigateWrapperData);
	}
	
	
	


	
	
	private void addAvgULPRBIntoJson(Layer3SummaryWrapper aggrigateWrapperData, StringBuilder json,
			QMDLLogCodeWrapper wrapper) {
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArray(wrapper.getAvgULPRB()));	
		if(wrapper.getAvgULPRB() != null) {
			aggrigateWrapperData.setAvgULPRB(NVLayer3Utils.getDoubleAggrigateArray(aggrigateWrapperData.getAvgULPRB(), wrapper.getAvgULPRB()));
		}
	}

	private void addStateInfoKpiIntoJson(StringBuilder json, QMDLLogCodeWrapper wrapper) {
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getEmmState());
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getEmmSubState());
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getRrcState());
	}

	private void addLteCellCPType(Layer3SummaryWrapper aggrigateWrapperData, StringBuilder json,
			QMDLLogCodeWrapper wrapper) {
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getDlCp());
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getUlCp());
		
		if(wrapper.getDlCp() != null && !wrapper.getDlCp().isEmpty()) {
			aggrigateWrapperData.setDlCp(wrapper.getDlCp());
		}
		if(wrapper.getUlCp() != null && !wrapper.getUlCp().isEmpty()) {
			aggrigateWrapperData.setUlCp(wrapper.getUlCp());
		}
	}

	private void addRACHInfo(Layer3SummaryWrapper aggrigateWrapperData, StringBuilder json,
			QMDLLogCodeWrapper wrapper) {

		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getPreambleCountMax());
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getPreambleInitialTxPower());
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getRaRNTI());
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getRaTimingAdvance());
		
		addRACHInfoIntoAggregateWrapper(aggrigateWrapperData, wrapper);
	}

	private void addRACHInfoIntoAggregateWrapper(Layer3SummaryWrapper aggrigateWrapperData,
			QMDLLogCodeWrapper wrapper) {
		if(wrapper.getPreambleCountMax() != null) {
			aggrigateWrapperData.setPreambleCountMax(wrapper.getPreambleCountMax());
		}
		if(wrapper.getPreambleInitialTxPower() != null) {
			aggrigateWrapperData.setPreambleInitialTxPower(wrapper.getPreambleInitialTxPower());
		}
		if(wrapper.getRaRNTI() != null) {
			aggrigateWrapperData.setRaRNTI(wrapper.getRaRNTI());
		}
		if(wrapper.getRaTimingAdvance() != null) {
			aggrigateWrapperData.setRaTimingAdvance(wrapper.getRaTimingAdvance());
		}
	}

	private void addUETxPower(Layer3SummaryWrapper aggrigateWrapperData, StringBuilder json,
			QMDLLogCodeWrapper wrapper) {
			NVLayer3Utils.addValueToStringBuilder(json, wrapper.getUeTxPower());
			if(wrapper.getUeTxPower()!=null) {
			aggrigateWrapperData.setUeTxPower(wrapper.getUeTxPower());
		}
	}

	private void addKPICountIntoJson(StringBuilder json, QMDLLogCodeWrapper wrapper) {
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getMessage3Count());
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getMessage1Count());
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getB0C0Count());
		NVLayer3Utils.addValueToStringBuilder(json,wrapper.getAttachAccept());
		NVLayer3Utils.addValueToStringBuilder(json,wrapper.getAttachRequest());
	}

	private void addMacThroughput(Layer3SummaryWrapper aggrigateWrapperData, StringBuilder json,
			QMDLLogCodeWrapper wrapper) {
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArray(wrapper.getMacUlThroughput()));
		aggrigateWrapperData.setMacUlThroughput(NVLayer3Utils.getDoubleArrayValue(aggrigateWrapperData.getMacUlThroughput(),NVLayer3Utils.getAvgFromArray(wrapper.getMacUlThroughput())));
	}

	private void addPDSCHBler(Layer3SummaryWrapper aggrigateWrapperData, StringBuilder json,
			QMDLLogCodeWrapper wrapper) {
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArray(wrapper.getPdschBLER()));
		aggrigateWrapperData.setPdschBLER(NVLayer3Utils.getDoubleArrayValue(aggrigateWrapperData.getPdschBLER(),NVLayer3Utils.getAvgFromArray(wrapper.getPdschBLER())));
	}

	private void addSchedulingGrantInfo(Layer3SummaryWrapper aggrigateWrapperData, StringBuilder json,
			QMDLLogCodeWrapper wrapper) {
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getDoubleValueUptoTwoDecimal(wrapper.getAvgDLTb0Size()));
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getDoubleValueUptoTwoDecimal(wrapper.getAvgDLTb1Size()));
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getDoubleValueUptoTwoDecimal(wrapper.getAvgDLTbSize()));
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getDoubleValueUptoTwoDecimal(wrapper.getAvgULTBSize()));
		
		addSchedulingGrantInfoIntoAggregateWrapper(aggrigateWrapperData, wrapper);
	}

	private void addSchedulingGrantInfoIntoAggregateWrapper(Layer3SummaryWrapper aggrigateWrapperData,
			QMDLLogCodeWrapper wrapper) {
		if(wrapper.getAvgDLTb0Size() != null) {
			aggrigateWrapperData.setAvgDLTb0Size(wrapper.getAvgDLTb0Size());
		}
		if(wrapper.getAvgDLTb1Size() != null) {
			aggrigateWrapperData.setAvgDLTb1Size(wrapper.getAvgDLTb1Size());
		}
		if(wrapper.getAvgDLTbSize() != null) {
			aggrigateWrapperData.setAvgDLTbSize(wrapper.getAvgDLTbSize());
		}
		if(wrapper.getAvgULTBSize() != null) {
			aggrigateWrapperData.setAvgULTBSize(wrapper.getAvgULTBSize());
		}
	}

	private void addMCSInfo(Layer3SummaryWrapper aggrigateWrapperData, StringBuilder json, QMDLLogCodeWrapper wrapper) {
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getTb0McsIndex());
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getTb1McsIndex());
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getTb0ModulationType());
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getTb1ModulationType());
		
		addMCSInfoIntoAggrigateWrapper(aggrigateWrapperData, wrapper);
	}

	private void addMCSInfoIntoAggrigateWrapper(Layer3SummaryWrapper aggrigateWrapperData, QMDLLogCodeWrapper wrapper) {
		if(wrapper.getTb0McsIndex() != null) {
			aggrigateWrapperData.setTb0McsIndex(wrapper.getTb0McsIndex());
		}
		if(wrapper.getTb1McsIndex() != null) {
			aggrigateWrapperData.setTb1McsIndex(wrapper.getTb1McsIndex());
		}
		if(wrapper.getTb0ModulationType() != null && !wrapper.getTb0ModulationType().isEmpty()) {
			aggrigateWrapperData.setTb0ModulationType(wrapper.getTb0ModulationType());
		}
		if(wrapper.getTb1ModulationType() != null && !wrapper.getTb1ModulationType().isEmpty()) {
			aggrigateWrapperData.setTb1ModulationType(wrapper.getTb1ModulationType());
		}
	}

	private void addTACDetail(StringBuilder json, QMDLLogCodeWrapper wrapper) {
		NVLayer3Utils.addValueToStringBuilder(json,wrapper.getTracking_Area_Code());	
	}

	private void addDominantKPI(Layer3SummaryWrapper aggrigateWrapperData, StringBuilder json,
			QMDLLogCodeWrapper wrapper) {
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArray(wrapper.getDominantChannelRSSI()));
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArray(wrapper.getDominantChannelRSSIRx0()));
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArray(wrapper.getDominantChannelRSSIRx1()));
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArray(wrapper.getDominantChannelRSRP()));
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArray(wrapper.getDominantChannelRSRPRx0()));
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArray(wrapper.getDominantChannelRSRPRx1()));
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArray(wrapper.getDominantChannelRSRQ()));
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArray(wrapper.getDominantChannelRSRQRx0()));
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArray(wrapper.getDominantChannelRSRQRx1()));
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getDominantChannelPCI());
		
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getLteDominantFrequency());
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getPowerHeadroomdata());
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getPrachTxPower());
		setDominantKPIIntoAggrigateWrapper(aggrigateWrapperData, wrapper);
	}

	private void setDominantKPIIntoAggrigateWrapper(Layer3SummaryWrapper aggrigateWrapperData,
			QMDLLogCodeWrapper wrapper) {
		aggrigateWrapperData.setDominantChannelRSSI(NVLayer3Utils.getDoubleArrayValue(aggrigateWrapperData.getDominantChannelRSSI(),NVLayer3Utils.getAvgFromArray(wrapper.getDominantChannelRSSI())));
		aggrigateWrapperData.setDominantChannelRSSIRx0(NVLayer3Utils.getDoubleArrayValue(aggrigateWrapperData.getDominantChannelRSSIRx0(),NVLayer3Utils.getAvgFromArray(wrapper.getDominantChannelRSSIRx0())));
		aggrigateWrapperData.setDominantChannelRSSIRx1(NVLayer3Utils.getDoubleArrayValue(aggrigateWrapperData.getDominantChannelRSSIRx1(),NVLayer3Utils.getAvgFromArray(wrapper.getDominantChannelRSSIRx1())));
		aggrigateWrapperData.setDominantChannelRSRQ(NVLayer3Utils.getDoubleArrayValue(aggrigateWrapperData.getDominantChannelRSRQ(),NVLayer3Utils.getAvgFromArray(wrapper.getDominantChannelRSRQ())));
		aggrigateWrapperData.setDominantChannelRSRQRx0(NVLayer3Utils.getDoubleArrayValue(aggrigateWrapperData.getDominantChannelRSRQRx0(),NVLayer3Utils.getAvgFromArray(wrapper.getDominantChannelRSRQRx0())));
		aggrigateWrapperData.setDominantChannelRSRQRx1(NVLayer3Utils.getDoubleArrayValue(aggrigateWrapperData.getDominantChannelRSRQRx1(),NVLayer3Utils.getAvgFromArray(wrapper.getDominantChannelRSRQRx1())));
		aggrigateWrapperData.setDominantChannelRSRP(NVLayer3Utils.getDoubleArrayValue(aggrigateWrapperData.getDominantChannelRSRP(),NVLayer3Utils.getAvgFromArray(wrapper.getDominantChannelRSRP())));
		aggrigateWrapperData.setDominantChannelRSRPRx0(NVLayer3Utils.getDoubleArrayValue(aggrigateWrapperData.getDominantChannelRSRPRx0(),NVLayer3Utils.getAvgFromArray(wrapper.getDominantChannelRSRPRx0())));
		aggrigateWrapperData.setDominantChannelRSRPRx1(NVLayer3Utils.getDoubleArrayValue(aggrigateWrapperData.getDominantChannelRSRPRx1(),NVLayer3Utils.getAvgFromArray(wrapper.getDominantChannelRSRPRx1())));
		if (wrapper.getDominantChannelPCI() != null) {
			aggrigateWrapperData.setDominantChannelPCI(wrapper.getDominantChannelPCI());
		}
		if (wrapper.getLteDominantFrequency() != null) {
			aggrigateWrapperData.setLteDominantFrequency(wrapper.getLteDominantFrequency());
		}
		if(wrapper.getPowerHeadroomdata() != null) {
			aggrigateWrapperData.setPowerHeadroomdata(wrapper.getPowerHeadroomdata());
		}
		if(wrapper.getPrachTxPower() != null) {
			aggrigateWrapperData.setPrachTxPower(wrapper.getPrachTxPower());
		}
	}

	private void addHandOverInterruption(Layer3SummaryWrapper aggrigateWrapperData, StringBuilder json,
			QMDLLogCodeWrapper wrapper) {

		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArray(wrapper.getHandoverInterruption()));

		aggrigateWrapperData.setHandoverInterruption(NVLayer3Utils.getDoubleAggrigateArray(aggrigateWrapperData.getHandoverInterruption(), wrapper.getHandoverInterruption()));
		aggrigateWrapperData.setMinHandoverInterruption(NVLayer3Utils.getMinValue(NVLayer3Utils.getAvgFromArray(wrapper.getHandoverInterruption()),aggrigateWrapperData.getMinHandoverInterruption()));
		aggrigateWrapperData.setMaxHandoverInterruption(NVLayer3Utils.getMaxValue(NVLayer3Utils.getAvgFromArray(wrapper.getHandoverInterruption()),aggrigateWrapperData.getMaxHandoverInterruption()));

	}

	private void addTransmissionMode(Layer3SummaryWrapper aggrigateWrapperData, StringBuilder json,
			QMDLLogCodeWrapper wrapper) {
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getTransmissionMode());
		setTransmissionModeIntoAggrigateWrapper(wrapper, aggrigateWrapperData);
	}

	private void setTransmissionModeIntoAggrigateWrapper(QMDLLogCodeWrapper wrapper,
			Layer3SummaryWrapper aggrigateWrapperData) {
		if (wrapper.getTransmissionMode() != null && !wrapper.getTransmissionMode().isEmpty()) {
			aggrigateWrapperData.setTransmissionMode(wrapper.getTransmissionMode());
		}
	}

	private void addPUSCHModulationType(Layer3SummaryWrapper aggrigateWrapperData, StringBuilder json,
			QMDLLogCodeWrapper wrapper) {
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getPuschModulationType());
		setPUSCHModulationTypeIntoAggrigateWrapper(wrapper, aggrigateWrapperData);
	}

	private void setPUSCHModulationTypeIntoAggrigateWrapper(QMDLLogCodeWrapper wrapper,
			Layer3SummaryWrapper aggrigateWrapperData) {
		aggrigateWrapperData.setPuschModulationType(wrapper.getPuschModulationType());
	}

	private void addPUSCHMcsIndex(Layer3SummaryWrapper aggrigateWrapperData, StringBuilder json,
			QMDLLogCodeWrapper wrapper) {
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArrayAndReturnIntegerValue(wrapper.getPuschMcsIndex()));
		setPUSCHMcsIndexIntoAggrigateWrapper(wrapper, aggrigateWrapperData);		
	}

	private void setPUSCHMcsIndexIntoAggrigateWrapper(QMDLLogCodeWrapper wrapper,
			Layer3SummaryWrapper aggrigateWrapperData) {
		if (wrapper.getPuschMcsIndex() != null) {
			aggrigateWrapperData.setPuschMcsIndex(Boolean.TRUE);
		}
	}

	private void addPUSCHThroughput(Layer3SummaryWrapper aggrigateWrapperData, StringBuilder json, QMDLLogCodeWrapper wrapper) {
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArrayUptoFourDecimals(wrapper.getPuschThroughput()));
		setPUSCHThroughputIntoAggrigateWrapper(wrapper, aggrigateWrapperData);
	}

	private void setPUSCHThroughputIntoAggrigateWrapper(QMDLLogCodeWrapper wrapper,
			Layer3SummaryWrapper aggrigateWrapperData) {
		aggrigateWrapperData.setPuschThroughput(NVLayer3Utils.getDoubleArrayValue(aggrigateWrapperData.getPuschThroughput(),NVLayer3Utils.getAvgFromArray(wrapper.getPuschThroughput())));
	
		aggrigateWrapperData.setMinPuschThroughput(NVLayer3Utils.getMinValue(NVLayer3Utils.getAvgFromArray(wrapper.getPuschThroughput()),aggrigateWrapperData.getMinPuschThroughput()));
		aggrigateWrapperData.setMaxPuschThroughput(NVLayer3Utils.getMaxValue(NVLayer3Utils.getAvgFromArray(wrapper.getPuschThroughput()),aggrigateWrapperData.getMaxPuschThroughput()));

	}

	private void addLinkAdaptationKPItoJson(StringBuilder json, QMDLLogCodeWrapper wrapper, Layer3SummaryWrapper aggrigateWrapperData) {
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArrayAndReturnIntegerValue(wrapper.getCqiCw1()));
		NVLayer3Utils.addValueToStringBuilder(json, getAvgFromArrayForDLPRBUtilization(wrapper));
		aggrigateWrapperData.setDlPRBUtilization(NVLayer3Utils.getDoubleAggrigateArray(aggrigateWrapperData.getDlPRBUtilization(), wrapper.getDlPRBUtilization()));
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArray(wrapper.getPdschnumRb()));
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getPdschCwoModulation());
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getPdschCwoMcs());
	}

	private Double getAvgFromArrayForDLPRBUtilization(QMDLLogCodeWrapper wrapper) {
		Double dlPRBUtilization = null;
		if(NVLayer3Utils.getAvgFromArray(wrapper.getDlPRBUtilization())!=null)
			dlPRBUtilization = (NVLayer3Utils.getAvgFromArray(wrapper.getDlPRBUtilization()))*QMDLConstant.PERCENTAGE_MULTIPLIER;
		return dlPRBUtilization;
	}

	private void addRRCConnectionStatesToJson(StringBuilder json, QMDLLogCodeWrapper wrapper) {
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getRrcConnectionComplete());
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getRrcConnectionSetupOk());
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getRrcConnectionAttempt());
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getRrcConnectionRelease());
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getRrcConnectionFailed());
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArray(wrapper.getRrcConnectionSetupTime()));
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getRrcReestablishmentSuccess());
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getRrcReestablishmentFailed());
	}

	private void addTauTimeDetail(StringBuilder json, QMDLLogCodeWrapper wrapper) {
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArray(wrapper.getTauTime()));	
	}
	public void addSpeedTestInbuildinParameterToJson(StringBuilder json, QMDLLogCodeWrapper wrapper) {
		NVLayer3Utils.addValueToStringBuilder(json,wrapper.getSpeedTestPinNumber());		
		NVLayer3Utils.addValueToStringBuilder(json,wrapper.getSpeedTestDlRate());
		NVLayer3Utils.addValueToStringBuilder(json,wrapper.getSpeedTestUlRate());
		NVLayer3Utils.addValueToStringBuilder(json,wrapper.getDownloadTimeGoogle());
		NVLayer3Utils.addValueToStringBuilder(json,wrapper.getDownloadTimeFacebook());
		NVLayer3Utils.addValueToStringBuilder(json,wrapper.getDownloadTimeFacebook());
	}

	public void addHttpDropParameterIntoJson(StringBuilder json, QMDLLogCodeWrapper wrapper) {
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getHttpDrop());
	}


	private void addPDSCHPCellSCellThroughput(StringBuilder json, QMDLLogCodeWrapper wrapper) {
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArrayUptoFourDecimals(wrapper.getPdschThroughputPriCell()));
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArrayUptoFourDecimals(wrapper.getPdschThroughputSecCell1()));
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArrayUptoFourDecimals(wrapper.getPdschThroughputSecCell2()));
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArrayUptoFourDecimals(wrapper.getPdschThroughputSecCell3()));
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArrayUptoFourDecimals(wrapper.getPdschThroughputSecCell4()));
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArrayUptoFourDecimals(wrapper.getPdschThroughputSecCell5()));
	}

	private void addWifiParamsToJson(StringBuilder json, QMDLLogCodeWrapper wrapper) {
		NVLayer3Utils.addValueToStringBuilder(json,wrapper.getBssid());
		NVLayer3Utils.addValueToStringBuilder(json,wrapper.getChannel());
		NVLayer3Utils.addValueToStringBuilder(json,wrapper.getLinkSpeed());
	}

	private void addPDSCHThroughput(Layer3SummaryWrapper aggrigateWrapperData, StringBuilder json,
			QMDLLogCodeWrapper wrapper) {

		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArrayUptoFourDecimals(wrapper.getPdschThroughput()));
		setPDSCHThroughputIntoAggrigateWrapper(wrapper, aggrigateWrapperData);
		
	}

	private void setPDSCHThroughputIntoAggrigateWrapper(QMDLLogCodeWrapper wrapper,
			Layer3SummaryWrapper aggrigateWrapperData) {
		
		aggrigateWrapperData.setPDSCHThroughput(NVLayer3Utils.getDoubleArrayValue(aggrigateWrapperData.getPDSCHThroughput(),NVLayer3Utils.getAvgFromArray(wrapper.getPdschThroughput())));
	
		aggrigateWrapperData.setMinPdschThroughput(NVLayer3Utils.getMinValue(NVLayer3Utils.getAvgFromArray(wrapper.getPdschThroughput()),aggrigateWrapperData.getMinPdschThroughput()));
		aggrigateWrapperData.setMaxPdschThroughput(NVLayer3Utils.getMaxValue(NVLayer3Utils.getAvgFromArray(wrapper.getPdschThroughput()),aggrigateWrapperData.getMaxPdschThroughput()));
	}

	private void addCSFBCallDataToJson(StringBuilder json, QMDLLogCodeWrapper wrapper) {
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getCsfbCallAttempt());
		NVLayer3Utils.addValueToStringBuilder(json,wrapper.getCsfbCallSetupSuccess());
		NVLayer3Utils.addValueToStringBuilder(json,wrapper.getCsfbCallSuccess());
		NVLayer3Utils.addValueToStringBuilder(json,wrapper.getCsfbCallSetupFailed());
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getCsfbCallDrop());
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getCsfbCallFailed());


	}

	private void addInstantaneousMosIntoJson(StringBuilder json, QMDLLogCodeWrapper wrapper,
			Layer3SummaryWrapper aggrigateWrapperData) {
		if ((wrapper.getIsCallSetup() != null && wrapper.getIsCallSetup())
				|| (wrapper.getCallSuccessCount() != null && wrapper.getCallSuccessCount() > 0)) {
			NVLayer3Utils.addValueToStringBuilder(json,
					NVLayer3Utils.getAvgFromArrayUptoFourDecimals(wrapper.getInstantaneousMos()));
			setInstantaneousMosIntoAggrigationWrapper(aggrigateWrapperData, wrapper);
		} else {
			NVLayer3Utils.addValueToStringBuilder(json, null);

		}

	}

	private void add2GDataIntoJson(StringBuilder json, QMDLLogCodeWrapper wrapper,
			NV2GSummaryWrapper aggrigate2gWrapperData) {
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArray(wrapper.getRxLev()));
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArray(wrapper.getRxQual()));
		
		NVLayer3Utils.addValueToStringBuilder(json,wrapper.getbCCHChannel());
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getbSIC());		
		set2GDataIntoAggrigateWrapper(wrapper,aggrigate2gWrapperData);
	}

	private void set2GDataIntoAggrigateWrapper(QMDLLogCodeWrapper wrapper, NV2GSummaryWrapper aggrigate2gWrapperData) {

		aggrigate2gWrapperData.setRxLev(NVLayer3Utils.getDoubleArrayValue(aggrigate2gWrapperData.getRxLev(),NVLayer3Utils.getAvgFromArray(wrapper.getRxLev())));
		aggrigate2gWrapperData.setRxQual(NVLayer3Utils.getDoubleArrayValue(aggrigate2gWrapperData.getRxQual(),NVLayer3Utils.getAvgFromArray(wrapper.getRxQual())));
		
		aggrigate2gWrapperData.setbSIC(wrapper.getbSIC());
		aggrigate2gWrapperData.setbCCHChannel(wrapper.getbCCHChannel());
	}

	private void addExtraKpiDataIntoJson(StringBuilder json, QMDLLogCodeWrapper wrapper, Layer3SummaryWrapper aggrigateWrapperData) {
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArray(wrapper.getrSRPRx0Data()));
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArray(wrapper.getrSRPRx1Data()));
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArray(wrapper.getrSRQRx0Data()));
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArray(wrapper.getrSRQRx1Data()));
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArray(wrapper.getrSSIRx0Data()));
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArray(wrapper.getrSSIRx1Data()));
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArray(wrapper.getsINRRx1Data()));
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArray(wrapper.getsINRRx0Data()));
		setExtraKpiDataIntoAggrigateWrapper(wrapper,aggrigateWrapperData);
	}

	private void setExtraKpiDataIntoAggrigateWrapper(QMDLLogCodeWrapper wrapperData,
			Layer3SummaryWrapper aggrigateWrapperData) {
		aggrigateWrapperData.setrSSIRx0Data(NVLayer3Utils.getDoubleArrayValue(aggrigateWrapperData.getrSSIRx0Data(),NVLayer3Utils.getAvgFromArray(wrapperData.getrSSIRx0Data())));
		aggrigateWrapperData.setrSRPRx0Data(NVLayer3Utils.getDoubleArrayValue(aggrigateWrapperData.getrSRPRx0Data(),NVLayer3Utils.getAvgFromArray(wrapperData.getrSRPRx0Data())));
		aggrigateWrapperData.setrSRQRx0Data(NVLayer3Utils.getDoubleArrayValue(aggrigateWrapperData.getrSRQRx0Data(),NVLayer3Utils.getAvgFromArray(wrapperData.getrSRQRx0Data())));
		aggrigateWrapperData.setsINRRx0Data(NVLayer3Utils.getDoubleArrayValue(aggrigateWrapperData.getsINRRx0Data(),NVLayer3Utils.getAvgFromArray(wrapperData.getsINRRx0Data())));
		aggrigateWrapperData.setrSSIRx1Data(NVLayer3Utils.getDoubleArrayValue(aggrigateWrapperData.getrSSIRx1Data(), NVLayer3Utils.getAvgFromArray(wrapperData.getrSSIRx1Data())));
		aggrigateWrapperData.setrSRPRx1Data(NVLayer3Utils.getDoubleArrayValue(aggrigateWrapperData.getrSRPRx1Data(),NVLayer3Utils.getAvgFromArray(wrapperData.getrSRPRx1Data())));
		aggrigateWrapperData.setrSRQRx1Data(NVLayer3Utils.getDoubleArrayValue(aggrigateWrapperData.getrSRQRx1Data(),NVLayer3Utils.getAvgFromArray(wrapperData.getrSRQRx1Data())));
		aggrigateWrapperData.setsINRRx1Data(NVLayer3Utils.getDoubleArrayValue(aggrigateWrapperData.getsINRRx1Data(),NVLayer3Utils.getAvgFromArray(wrapperData.getsINRRx1Data())));
	}

	private void add3GDataIntoJson(StringBuilder json, QMDLLogCodeWrapper wrapper, NV3GSummaryWrapper aggrigate3gWrapperData) {
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArray(wrapper.getRscp()));
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArray(wrapper.getEcio()));
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArray(wrapper.getRssi()));
		set3GDataIntoAggrigateWrapper(wrapper, aggrigate3gWrapperData);
	}
	
	private void set3GDataIntoAggrigateWrapper(QMDLLogCodeWrapper wrapper, NV3GSummaryWrapper aggrigate3gWrapperData) {
		aggrigate3gWrapperData.setRscp(NVLayer3Utils.getDoubleArrayValue(aggrigate3gWrapperData.getRscp(),NVLayer3Utils.getAvgFromArray(wrapper.getRscp())));
		aggrigate3gWrapperData.setEcio(NVLayer3Utils.getDoubleArrayValue(aggrigate3gWrapperData.getEcio(),NVLayer3Utils.getAvgFromArray(wrapper.getEcio())));
		aggrigate3gWrapperData.setRssi(NVLayer3Utils.getDoubleArrayValue(aggrigate3gWrapperData.getRssi(),NVLayer3Utils.getAvgFromArray(wrapper.getRssi())));
	}

	private void addTauDetail(StringBuilder json, QMDLLogCodeWrapper wrapper) {
		NVLayer3Utils.addValueToStringBuilder(json,wrapper.getTauAttempt());
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getTauComplete());		
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getTauFailure());		
	}

	private void addHttpDetail(StringBuilder json, QMDLLogCodeWrapper wrapper) {
		NVLayer3Utils.addValueToStringBuilder(json,wrapper.getHttpAttempt());
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getHttpSucess());
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getHttpFailure());

	}

	private void addSmsDetail(StringBuilder json, QMDLLogCodeWrapper wrapper) {
		NVLayer3Utils.addValueToStringBuilder(json,wrapper.getSmsAttempt());
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getSmsSucess());
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getSmsFailure());
	}

	private void addHandoverDetailAndBenchMarkReportDetailIntoJson(StringBuilder json, QMDLLogCodeWrapper wrapper, String woId) {
		NVLayer3Utils.addValueToStringBuilder(json,NVLayer3Utils.getAvgFromArray( wrapper.getFinalMosG711()));
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getCaType());
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getEtimeStamp());
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getNewHandOverIntiateInterCount());
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getNewHandOverFailureInterCount());
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getNewHandOverSuccessInterCount());
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getNewHandOverIntiateIntraCount());
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getNewHandOverFailureIntraCount());
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getNewHandOverSuccessIntraCount());
		NVLayer3Utils.addValueToStringBuilder(json,woId);
	}


	private Double getDlPeakValue(Double dlValue, QMDLLogCodeWrapper wrapper, String band, String bandwidth) {
		if(wrapper.getDlThroughPut()!=null &&dlValue != null&&dlValue < NVLayer3Utils.getAvgFromArray(wrapper.getDlThroughPut()) ){
			dlValue = NVLayer3Utils.getAvgFromArray(wrapper.getDlThroughPut());
		}
		if (dlValue==null||(wrapper.getBand()!=null&&!wrapper.getBand().equals(band))||(wrapper.getDl_Bandwidth()!=null&&!wrapper.getDl_Bandwidth().equals(bandwidth))){
			dlValue = NVLayer3Utils.getAvgFromArray(wrapper.getDlThroughPut());
		}
		return dlValue;
	}

	private void addMncMccEnobeIdAndHandoverDetailIntoJson(StringBuilder json, QMDLLogCodeWrapper wrapper) {
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getMnc());
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getMcc());
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getCellid());
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getCellid());//SectorId
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.geteNodebId());
		NVLayer3Utils.addValueToStringBuilder(json,wrapper.getSourcePci());
		NVLayer3Utils.addValueToStringBuilder(json,wrapper.getTargetPci());
		NVLayer3Utils.addValueToStringBuilder(json,NVLayer3Utils.getAvgFromArray(wrapper.getHandoverLatencyTime()));
		NVLayer3Utils.addValueToStringBuilder(json,NVLayer3Utils.getAvgFromArray(wrapper.getPacketLoss()));
	}


	private void addCoverageAndDownloadTypeParamIntoJson(StringBuilder json, QMDLLogCodeWrapper wrapper) {
		if(wrapper.getCoverage() != null)
		{
			NVLayer3Utils.addValueToStringBuilder(json, wrapper.getCoverage());
		}
		else{
			Double measuredRSRP = NVLayer3Utils.getAvgFromArray(wrapper.getMeasureRSRPData());
			Double rscp = NVLayer3Utils.getAvgFromArray(wrapper.getRscp());
			Double rxLev = NVLayer3Utils.getAvgFromArray(wrapper.getRxLev());
			if(measuredRSRP != null)
				NVLayer3Utils.addValueToStringBuilder(json, QMDLConstant.NETWORK_TYPE_LTE);
			else if(rscp != null)
				NVLayer3Utils.addValueToStringBuilder(json, QMDLConstant.NETWORK_TYPE_3G);
			else if(rxLev != null)
				NVLayer3Utils.addValueToStringBuilder(json, QMDLConstant.NETWORK_TYPE_2G);
			else {
				NVLayer3Utils.addValueToStringBuilder(json, QMDLConstant.NETWORK_TYPE_NONE);
			}
		}
		
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getTestType());
	}


	private QMDLLogCodeWrapper setValuesIntoAggrigatedWrapper(Layer3SummaryWrapper aggrigateWrapperData,
			Entry<Long, QMDLLogCodeWrapper> wrapperEntry, String band, String bandwidth, Double dlMaxValue) {
		QMDLLogCodeWrapper wrapper = wrapperEntry.getValue();
		setStartAndEndTime(aggrigateWrapperData,wrapper);
		addVoLteCallDropCountToAggregatedData(aggrigateWrapperData, wrapper);
		setTechnologyWisePeakValueIntoAggregationWrapper(aggrigateWrapperData,band,bandwidth,dlMaxValue);
		setHandoverLatencyAndCSTIntoAggregateWrapper(aggrigateWrapperData,wrapper);
		setErabDropValuesIntoAggregateWrapper(aggrigateWrapperData,wrapper);		
//		setInterIntraHandoverIntoAggregationWrapper(aggrigateWrapperData,wrapper);
		
		setMosDropRateIntoAggrigationWrapper(aggrigateWrapperData,wrapper);	
		setCallOnNetOffNetIntoAggrigationWrapper(aggrigateWrapperData,wrapper);
		setBRTIReportParameterIntoAggrigatedWrapper(aggrigateWrapperData,wrapper);
		checkValuesinAggigratedWrapper(aggrigateWrapperData,wrapper);
		setTauValuesIntoAggrigationWrapper(aggrigateWrapperData, wrapper);
	    addCSFBCALLCountToAggregatedData(aggrigateWrapperData, wrapper);
        setRRCStatesToAggrigationWrapper(aggrigateWrapperData,wrapper);
        addLinkAdaptationToAggregatedWrapper(aggrigateWrapperData,wrapper);
        addStateInfoToAggrigatedWrapper(aggrigateWrapperData,wrapper);
        addLTEEventsIntoAggrigatedWrapper(aggrigateWrapperData, wrapper);        
    	setNewInterIntraHandoverIntoAggregationWrapper(aggrigateWrapperData,wrapper);    	
    	addPMosIntoAggregatedWrapper(aggrigateWrapperData, wrapper);		
		
    	
    	
		return wrapper;
	}

	public void addPMosIntoAggregatedWrapper(Layer3SummaryWrapper aggrigateWrapperData, QMDLLogCodeWrapper wrapper) {
		aggrigateWrapperData.setpMos(NVLayer3Utils.getDoubleAggrigateArray(aggrigateWrapperData.getpMos(), wrapper.getpMos()));
	}

	private void setNewInterIntraHandoverIntoAggregationWrapper(Layer3SummaryWrapper aggrigateWrapperData,
			QMDLLogCodeWrapper wrapper) {
		aggrigateWrapperData.setNewHandOverIntiateCount(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getNewHandOverIntiateCount(),NVLayer3Utils.addIntegerValue(wrapper.getNewHandOverIntiateInterCount(),wrapper.getNewHandOverIntiateIntraCount())));
		aggrigateWrapperData.setNewHandOverSuccessCount(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getNewHandOverSuccessCount(), wrapper.getNewHandOverSuccessCount()));
		aggrigateWrapperData.setNewHandOverFailureCount(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getNewHandOverFailureCount(), wrapper.getNewHandOverFailureCount()));

		aggrigateWrapperData.setNewHandOverIntiateInterCount(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getNewHandOverIntiateInterCount(), wrapper.getNewHandOverIntiateInterCount()));
		aggrigateWrapperData.setNewHandOverSuccessInterCount(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getNewHandOverSuccessInterCount(), wrapper.getNewHandOverSuccessInterCount()));
		aggrigateWrapperData.setNewHandOverFailureInterCount(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getNewHandOverFailureInterCount(), wrapper.getNewHandOverFailureInterCount()));

		aggrigateWrapperData.setNewHandOverIntiateIntraCount(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getNewHandOverIntiateIntraCount(), wrapper.getNewHandOverIntiateIntraCount()));
		aggrigateWrapperData.setNewHandOverSuccessIntraCount(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getNewHandOverSuccessIntraCount(), wrapper.getNewHandOverSuccessIntraCount()));
		aggrigateWrapperData.setNewHandOverFailureIntraCount(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getNewHandOverFailureIntraCount(), wrapper.getNewHandOverFailureIntraCount()));
	}

	private void addLTEEventsIntoAggrigatedWrapper(Layer3SummaryWrapper aggrigateWrapperData,
			QMDLLogCodeWrapper wrapper) {
		addVOLTEEventsToAggrigatedWrapper(aggrigateWrapperData, wrapper);
        addLTERegistrationEventToAggrigatedWrapper(aggrigateWrapperData, wrapper);
        addLTERandomAccessEventToAggrigatedWrapper(aggrigateWrapperData, wrapper);
        addLTECallEventsToAggrigatedWrapper(aggrigateWrapperData,wrapper);
        addLTEHandoverEventsToAggrigatedWrapper(aggrigateWrapperData,wrapper);
	}
	
	private void addLTEHandoverEventsToAggrigatedWrapper(Layer3SummaryWrapper aggrigateWrapperData,QMDLLogCodeWrapper wrapper) {
		aggrigateWrapperData.setRrcEutra(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getRrcEutra(), wrapper.getRrcEutra()));
		aggrigateWrapperData.setRrcGeran(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getRrcGeran(), wrapper.getRrcGeran()));
		aggrigateWrapperData.setRrcUtraFdd(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getRrcUtraFdd(), wrapper.getRrcUtraFdd()));
		aggrigateWrapperData.setRrcUtraTdd(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getRrcUtraTdd(), wrapper.getRrcUtraTdd()));
		aggrigateWrapperData.setLteUmtsRequest(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getLteUmtsRequest(), wrapper.getLteUmtsRequest()));
		aggrigateWrapperData.setLteUmtsSuccess(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getLteUmtsSuccess(), wrapper.getLteUmtsSuccess()));
		aggrigateWrapperData.setUmtsLteRequest(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getUmtsLteRequest(), wrapper.getUmtsLteRequest()));
		aggrigateWrapperData.setUmtsLteSuccess(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getUmtsLteSuccess(), wrapper.getUmtsLteSuccess()));
		aggrigateWrapperData.setMoCsfbRequest(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getMoCsfbRequest(), wrapper.getMoCsfbRequest()));
		aggrigateWrapperData.setMtCsfbRequest(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getMtCsfbRequest(), wrapper.getMtCsfbRequest()));
		aggrigateWrapperData.setMoCsfbSuccess(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getMoCsfbSuccess(), wrapper.getMoCsfbSuccess()));
		aggrigateWrapperData.setMtCsfbSuccess(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getMtCsfbSuccess(), wrapper.getMtCsfbSuccess()));
		aggrigateWrapperData.setRrcReconfigCompleteMissing(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getRrcReconfigCompleteMissing(), wrapper.getRrcReconfigCompleteMissing()));

		aggrigateWrapperData.setRadioLinkFailure(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getRadioLinkFailure(), wrapper.getRadioLinkFailure()));
		aggrigateWrapperData.setRrcConnReconfFailure(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getRrcConnReconfFailure(), wrapper.getRrcConnReconfFailure()));
		aggrigateWrapperData.setMoCsfbFailure(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getMoCsfbFailure(), wrapper.getMoCsfbFailure()));
		aggrigateWrapperData.setMtCsfbFailure(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getMtCsfbFailure(), wrapper.getMtCsfbFailure()));
		aggrigateWrapperData.setCellChangeCount(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getCellChangeCount() , wrapper.getCellChangeCount()));
		aggrigateWrapperData.setReturnCSCallEnd(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getReturnCSCallEnd(), wrapper.getReturnCSCallEnd()));
	}

	private void addLTECallEventsToAggrigatedWrapper(Layer3SummaryWrapper aggrigateWrapperData,QMDLLogCodeWrapper wrapper) {
		aggrigateWrapperData.setPagingMessageCount(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getPagingMessageCount(), wrapper.getPagingMessageCount()));
		aggrigateWrapperData.setServiceReject(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getServiceReject(), wrapper.getServiceReject()));
		aggrigateWrapperData.setServiceRequest(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getServiceRequest(), wrapper.getServiceRequest()));
		aggrigateWrapperData.setRrcConnectionSetup(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getRrcConnectionSetup(), wrapper.getRrcConnectionSetup()));
		aggrigateWrapperData.setSecurityModeComplete(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getSecurityModeComplete(), wrapper.getSecurityModeComplete()));
		aggrigateWrapperData.setAuthenticationResponse(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getAuthenticationResponse(), wrapper.getAuthenticationResponse()));
		aggrigateWrapperData.setAuthenticationReject(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getAuthenticationReject(), wrapper.getAuthenticationReject()));
		aggrigateWrapperData.setAuthenticationFailure(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getAuthenticationFailure(), wrapper.getAuthenticationFailure()));
		aggrigateWrapperData.setAuthenticationRequest(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getAuthenticationRequest(), wrapper.getAuthenticationRequest()));
		aggrigateWrapperData.setServiceFailure(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getServiceFailure(), wrapper.getServiceFailure()));
		aggrigateWrapperData.setMoSignalling(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getMoSignalling(), wrapper.getMoSignalling()));
		aggrigateWrapperData.setMtAccess(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getMtAccess(), wrapper.getMtAccess()));
		aggrigateWrapperData.setRrcSetupFailure(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getRrcSetupFailure(), wrapper.getRrcSetupFailure()));
		aggrigateWrapperData.setRrcConnReconfiguration(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getRrcConnReconfiguration(), wrapper.getRrcConnReconfiguration()));
		aggrigateWrapperData.setReestablishmentFailure(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getReestablishmentFailure(), wrapper.getReestablishmentFailure()));
		aggrigateWrapperData.setRrcConnReconfSuccess(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getRrcConnReconfSuccess(), wrapper.getRrcConnReconfSuccess()));
		aggrigateWrapperData.setErabSetup(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getErabSetup(), wrapper.getErabSetup()));
		aggrigateWrapperData.setErabSetupSuccess(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getErabSetupSuccess(), wrapper.getErabSetupSuccess()));
		aggrigateWrapperData.setRrcConnectionDropped(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getRrcConnectionDropped(), wrapper.getRrcConnectionDropped()));
		aggrigateWrapperData.setServiceRequestSuccess(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getServiceRequestSuccess(), wrapper.getServiceRequestSuccess()));
	}

	private void addLTERandomAccessEventToAggrigatedWrapper(Layer3SummaryWrapper aggrigateWrapperData,QMDLLogCodeWrapper wrapper) {
		aggrigateWrapperData.setLteRACHPreambleSuccessCount(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getLteRACHPreambleSuccessCount(),wrapper.getLteRACHPreambleSuccess()));
        aggrigateWrapperData.setLteRACHPreambleFailureCount(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getLteRACHPreambleFailureCount(),wrapper.getLteRACHPreambleFailure()));        
        aggrigateWrapperData.setLteRACHProcedureSuccessCount(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getLteRACHProcedureSuccessCount(),wrapper.getLteRACHProcedureSuccess()));
        aggrigateWrapperData.setLteRACHProcedureFailureCount(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getLteRACHProcedureFailureCount(),wrapper.getLteRACHProcedureFailure()));
	}
	
	private void addLTERegistrationEventToAggrigatedWrapper(Layer3SummaryWrapper aggrigateWrapperData,QMDLLogCodeWrapper wrapper) {
		aggrigateWrapperData.setAttachComplete(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getAttachComplete(),wrapper.getAttachComplete()));
		aggrigateWrapperData.setLteEMMRegisteredEvent(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getLteEMMRegisteredEvent(),wrapper.getLteEMMRegisteredEvent()));
        aggrigateWrapperData.setLtePDNConnectionRequest(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getLtePDNConnectionRequest(),wrapper.getLtePDNConnectionRequest()));
	}
	
	public void addVOLTEEventsToAggrigatedWrapper(Layer3SummaryWrapper aggrigateWrapperData,QMDLLogCodeWrapper wrapper) {
		aggrigateWrapperData.setVoiceBearerActivationRequestCount(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getVoiceBearerActivationRequestCount(),wrapper.getVoiceBearerActivationRequest()));
        aggrigateWrapperData.setVoiceBearerActivationSuccessCount(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getVoiceBearerActivationSuccessCount(),wrapper.getVoiceBearerActivationSuccess()));
        aggrigateWrapperData.setVoiceBearerDeactivationRequestCount(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getVoiceBearerDeactivationRequestCount(),wrapper.getVoiceBearerDeactivationRequest()));
        aggrigateWrapperData.setInitialIMSRegistrationSuccessCount(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getInitialIMSRegistrationSuccessCount(),wrapper.getInitialIMSRegistrationSuccess()));
        aggrigateWrapperData.setInitialIMSRegistrationFailureCount(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getInitialIMSRegistrationFailureCount(),wrapper.getInitialIMSRegistrationFailure()));
	}
	
	private void addStateInfoToAggrigatedWrapper(Layer3SummaryWrapper aggrigateWrapperData,QMDLLogCodeWrapper wrapper) {
		aggrigateWrapperData.setEmmState(wrapper.getEmmState());
		aggrigateWrapperData.setEmmSubState(wrapper.getEmmSubState());
		if (wrapper.getRrcState() != null && !wrapper.getRrcState().isEmpty()) {
		aggrigateWrapperData.setRrcState(wrapper.getRrcState());
		}
	}

	private void addLinkAdaptationToAggregatedWrapper(Layer3SummaryWrapper aggrigateWrapperData,QMDLLogCodeWrapper wrapper) {
		aggrigateWrapperData.setDlPrb(NVLayer3Utils.getDoubleAggrigateArray(aggrigateWrapperData.getDlPrb(), wrapper.getDlPrb()));
		aggrigateWrapperData.setCqiCw1(NVLayer3Utils.getDoubleAggrigateArray(aggrigateWrapperData.getCqiCw1(), wrapper.getCqiCw1()));
		aggrigateWrapperData.setPdschnumRb(NVLayer3Utils.getDoubleAggrigateArray(aggrigateWrapperData.getPdschnumRb(), wrapper.getPdschnumRb()));
		aggrigateWrapperData.setPdschCwoModulation(wrapper.getPdschCwoModulation());
		aggrigateWrapperData.setPdschCwoMcs(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getPdschCwoMcs(), wrapper.getPdschCwoMcs()));
		aggrigateWrapperData.setNumRB(NVLayer3Utils.getDoubleAggrigateArray(aggrigateWrapperData.getNumRB(), wrapper.getNumRB()));
	}

	private void setRRCStatesToAggrigationWrapper(Layer3SummaryWrapper aggrigateWrapperData, QMDLLogCodeWrapper wrapper) {
		aggrigateWrapperData.setRrcConnectionComplete(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getRrcConnectionComplete(), wrapper.getRrcConnectionComplete()));
		aggrigateWrapperData.setRrcConnectionSetupOk(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getRrcConnectionSetupOk(), wrapper.getRrcConnectionSetupOk()));
		aggrigateWrapperData.setRrcConnectionAttempt(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getRrcConnectionAttempt(), wrapper.getRrcConnectionAttempt()));
		aggrigateWrapperData.setRrcConnectionRelease(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getRrcConnectionRelease(), wrapper.getRrcConnectionRelease()));
		aggrigateWrapperData.setRrcConnectionFailed(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getRrcConnectionFailed(), wrapper.getRrcConnectionFailed()));
		aggrigateWrapperData.setRrcConnectionSetupTime(NVLayer3Utils.getDoubleAggrigateArray(aggrigateWrapperData.getRrcConnectionSetupTime(), wrapper.getRrcConnectionSetupTime()));
		aggrigateWrapperData.setRrcReestablishmentSuccess(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getRrcReestablishmentSuccess(), wrapper.getRrcReestablishmentSuccess()));
		aggrigateWrapperData.setRrcReestablishmentFailed(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getRrcReestablishmentFailed(), wrapper.getRrcReestablishmentFailed()));
	}

	private void addCSFBCALLCountToAggregatedData(Layer3SummaryWrapper aggrigateWrapperData,
			QMDLLogCodeWrapper wrapper) {
		aggrigateWrapperData.setCsfbCallAttempt((NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getCsfbCallAttempt(),wrapper.getCsfbCallAttempt())));
		aggrigateWrapperData.setCsfbCallDrop((NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getCsfbCallDrop(),wrapper.getCsfbCallDrop())));
		aggrigateWrapperData.setCsfbCallFailed((NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getCsfbCallFailed(),wrapper.getCsfbCallFailed())));
		aggrigateWrapperData.setCsfbCallSetupFailed((NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getCsfbCallSetupFailed(),wrapper.getCsfbCallSetupFailed())));
		aggrigateWrapperData.setCsfbCallSetupSuccess((NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getCsfbCallSetupSuccess(),wrapper.getCsfbCallSetupSuccess())));
		aggrigateWrapperData.setCsfbCallSuccess((NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getCsfbCallSuccess(),wrapper.getCsfbCallSuccess())));

		
	}

	private void setInstantaneousMosIntoAggrigationWrapper(Layer3SummaryWrapper aggrigateWrapperData,
			QMDLLogCodeWrapper wrapper) {
		aggrigateWrapperData.setInstantaneousMos(NVLayer3Utils.getDoubleAggrigateArray(aggrigateWrapperData.getInstantaneousMos(), wrapper.getInstantaneousMos()));
		aggrigateWrapperData.setMinInstantaneousMos(NVLayer3Utils.getMinValue(NVLayer3Utils.getAvgFromArray(wrapper.getInstantaneousMos()),aggrigateWrapperData.getMinInstantaneousMos()));
		aggrigateWrapperData.setMaxInstantaneousMos(NVLayer3Utils.getMaxValue(NVLayer3Utils.getAvgFromArray(wrapper.getInstantaneousMos()),aggrigateWrapperData.getMaxInstantaneousMos()));

	}

	private void setBRTIReportParameterIntoAggrigatedWrapper(Layer3SummaryWrapper aggrigateWrapperData,
			QMDLLogCodeWrapper wrapper) {
		aggrigateWrapperData.setHttpAttempt(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getHttpAttempt(), wrapper.getHttpAttempt()));
		aggrigateWrapperData.setHttpSucess(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getHttpSucess(), wrapper.getHttpSucess()));
		aggrigateWrapperData.setHttpFailure(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getHttpFailure(), wrapper.getHttpFailure()));		
		aggrigateWrapperData.setHttpDrop(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getHttpDrop(), wrapper.getHttpDrop()));		
				
		aggrigateWrapperData.setHttpDownLoadTime(NVLayer3Utils.getDoubleAggrigateArray(aggrigateWrapperData.getHttpDownLoadTime(), wrapper.getHttpDownLoadTime()));		
		aggrigateWrapperData.setSmsFailure(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getSmsFailure(),wrapper.getSmsFailure()));		
	}

	public void setTauValuesIntoAggrigationWrapper(Layer3SummaryWrapper aggrigateWrapperData,
			QMDLLogCodeWrapper wrapper) {
		aggrigateWrapperData.setTauAttempt(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getTauAttempt(),wrapper.getTauAttempt()));
		aggrigateWrapperData.setTauComplete(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getTauComplete(),wrapper.getTauComplete()));
		aggrigateWrapperData.setTauFailure(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getTauFailure(),wrapper.getTauFailure()));
		aggrigateWrapperData.setTauTime(NVLayer3Utils.getDoubleAggrigateArray(aggrigateWrapperData.getTauTime(), wrapper.getTauTime()));
	}

	private void checkValuesinAggigratedWrapper(Layer3SummaryWrapper aggrigateWrapperData, QMDLLogCodeWrapper wrapper) {
		if(wrapper.getIndexPMI()!=null){
			aggrigateWrapperData.setPmi(Boolean.TRUE);
		}
		if(wrapper.getUl_Bandwidth()!=null) {
			aggrigateWrapperData.setBandwidthUL(Boolean.TRUE);
		}
		if(wrapper.getDl_Bandwidth()!=null) {
			aggrigateWrapperData.setBandwidthDL(Boolean.TRUE);
		}
		if(wrapper.getSpatialRank()!=null) {
			aggrigateWrapperData.setSpatialIndex(Boolean.TRUE);
		}
		if(wrapper.getPuschTxPower()!=null) {
			aggrigateWrapperData.setPowerTxPusch(Boolean.TRUE);
		}
		if(wrapper.getTimingAdvance()!=null) {
			aggrigateWrapperData.setTimingAdvance(Boolean.TRUE);
		}
		if(wrapper.getOutOfSyncBler()!=null) {
			aggrigateWrapperData.setBler(Boolean.TRUE);
		}
		if(wrapper.getMcs()!=null) {
			aggrigateWrapperData.setMcs(Boolean.TRUE);
		}
		if(wrapper.getDlEarfcn()!=null) {
			aggrigateWrapperData.setDlEarfcn(Boolean.TRUE);
		}
		if(wrapper.getUlEarfcn()!=null) {
			aggrigateWrapperData.setUlEarfcn(Boolean.TRUE);
		}
		if(wrapper.getRankIndex()!=null) {
			aggrigateWrapperData.setRI(Boolean.TRUE);
		}
		if(wrapper.getCarrierIndex()!=null) {
			aggrigateWrapperData.setCarrierIndex(Boolean.TRUE);
		}
		
		if(wrapper.getMnc()!=null) {
			aggrigateWrapperData.setMNC(Boolean.TRUE);
		}
		if(wrapper.getMcc()!=null) {
			aggrigateWrapperData.setMCC(Boolean.TRUE);
		}
		if(wrapper.getTracking_Area_Code()!=null) {
			aggrigateWrapperData.setTrackingAraCode(Boolean.TRUE);
		}

	}



	private void setCallOnNetOffNetIntoAggrigationWrapper(Layer3SummaryWrapper aggrigateWrapperData,
			QMDLLogCodeWrapper wrapper) {
		setCallConnectionSetupTime(aggrigateWrapperData,wrapper);
		aggrigateWrapperData.setCallInitiateOnNetCount(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getCallInitiateOnNetCount(), wrapper.getCallInitiateOnNetCount()));
		aggrigateWrapperData.setCallInitiateOffNetCount(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getCallInitiateOffNetCount(),wrapper.getCallInitiateOffNetCount()));
		
		aggrigateWrapperData.setCallSuccessOnNetCount(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getCallSuccessOnNetCount(), wrapper.getCallSuccessOnNetCount()));
		aggrigateWrapperData.setCallSuccessOffNetCount(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getCallSuccessOffNetCount(),wrapper.getCallSuccessOffNetCount()));
		
		aggrigateWrapperData.setCallFailureOnNetCount(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getCallFailureOnNetCount(), wrapper.getCallFailureOnNetCount()));
		aggrigateWrapperData.setCallFailureOffNetCount(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getCallFailureOffNetCount(),wrapper.getCallFailureOffNetCount()));
		
		aggrigateWrapperData.setCallDropOnNetCount(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getCallDropOnNetCount(), wrapper.getCallDropOnNetCount()));
		aggrigateWrapperData.setCallDropOffNetCount(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getCallDropOffNetCount(),wrapper.getCallDropOffNetCount()));
		aggrigateWrapperData.setCallConnectionSetupTimeOffnet(NVLayer3Utils.getDoubleAggrigateArray(aggrigateWrapperData.getCallConnectionSetupTimeOffnet(),wrapper.getCallConnectionSetupTimeOffnet()));
		
		aggrigateWrapperData.setCallConnectionSetupTimeOnnet(NVLayer3Utils.getDoubleAggrigateArray(aggrigateWrapperData.getCallConnectionSetupTimeOnnet(),wrapper.getCallConnectionSetupTimeOnnet()));
	
		
		aggrigateWrapperData.setFinalMosG711Onnet(NVLayer3Utils.getDoubleAggrigateArray(aggrigateWrapperData.getFinalMosG711Onnet(),wrapper.getFinalMosG711Onnet()));
		aggrigateWrapperData.setFinalMosG711Offnet(NVLayer3Utils.getDoubleAggrigateArray(aggrigateWrapperData.getFinalMosG711Offnet(),wrapper.getFinalMosG711Offnet()));
		
		aggrigateWrapperData.setFinalMosILbcOnnet(NVLayer3Utils.getDoubleAggrigateArray(aggrigateWrapperData.getFinalMosILbcOnnet(),wrapper.getFinalMosILbcOnnet()));
		aggrigateWrapperData.setFinalMosILbcOffnet(NVLayer3Utils.getDoubleAggrigateArray(aggrigateWrapperData.getFinalMosILbcOffnet(),wrapper.getFinalMosILbcOffnet()));
		
	}



	private void setMosDropRateIntoAggrigationWrapper(Layer3SummaryWrapper aggrigateWrapperData,
			QMDLLogCodeWrapper wrapper) {
		aggrigateWrapperData.setFinalMosG711(NVLayer3Utils.getDoubleAggrigateArray(aggrigateWrapperData.getFinalMosG711(), wrapper.getFinalMosG711()));		
		aggrigateWrapperData.setFinalMosILbc(NVLayer3Utils.getDoubleAggrigateArray(aggrigateWrapperData.getFinalMosILbc(), wrapper.getFinalMosILbc()));				
	}



	/*private void setInterIntraHandoverIntoAggregationWrapper(Layer3SummaryWrapper aggrigateWrapperData,
			QMDLLogCodeWrapper wrapper) {
		aggrigateWrapperData.setHandOverIntiateInterCount(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getHandOverIntiateInterCount(), wrapper.getHandOverIntiateInterCount()));
		aggrigateWrapperData.setHandOverIntiateIntraCount(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getHandOverIntiateIntraCount(), wrapper.getHandOverIntiateIntraCount()));
		aggrigateWrapperData.setHandOverSuccessInterCount(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getHandOverSuccessInterCount(), wrapper.getHandOverSuccessInterCount()));
		aggrigateWrapperData.setHandOverSuccessIntraCount(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getHandOverSuccessIntraCount(), wrapper.getHandOverSuccessIntraCount()));
		aggrigateWrapperData.setHandOverFailureInterCount(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getHandOverFailureInterCount(), wrapper.getHandOverFailureInterCount()));
		aggrigateWrapperData.setHandOverFailureIntraCount(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getHandOverFailureIntraCount(), wrapper.getHandOverFailureIntraCount()));
	}*/

	private void setCallConnectionSetupTime(Layer3SummaryWrapper aggrigateWrapperData, QMDLLogCodeWrapper wrapper) {
		aggrigateWrapperData.setCallConnectionSetupTime(NVLayer3Utils.getDoubleAggrigateArray(aggrigateWrapperData.getCallConnectionSetupTime(), wrapper.getCallConnectionSetupTime()));		
		aggrigateWrapperData.setCallSetupSuccessTime(NVLayer3Utils.getDoubleAggrigateArray(aggrigateWrapperData.getCallSetupSuccessTime(), wrapper.getCallSetupSuccessTime()));		
	}



	private void setErabDropValuesIntoAggregateWrapper(Layer3SummaryWrapper aggrigateWrapperData,
			QMDLLogCodeWrapper wrapper) {
		aggrigateWrapperData.setTotalErab(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getTotalErab(), wrapper.getTotalErab()));
		aggrigateWrapperData.setErabDrop(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getErabDrop(), wrapper.getErabDrop()));
	}



	private void setHandoverLatencyAndCSTIntoAggregateWrapper(Layer3SummaryWrapper aggrigateWrapperData,
			QMDLLogCodeWrapper wrapper) {
		aggrigateWrapperData.setHandoverLatencyTime(NVLayer3Utils.getDoubleAggrigateArray(aggrigateWrapperData.getHandoverLatencyTime(), wrapper.getHandoverLatencyTime()));
		aggrigateWrapperData.setConnectionSetupTime(NVLayer3Utils.getDoubleArrayValue(aggrigateWrapperData.getConnectionSetupTime(),NVLayer3Utils.getAvgFromArray(wrapper.getConnectionSetupTime())));
    	}



	private void setTechnologyWisePeakValueIntoAggregationWrapper(Layer3SummaryWrapper aggrigateWrapperData,
			 String band, String bandwidth, Double dlMaxValue) {
			if(band!=null && bandwidth!=null && dlMaxValue!=null){
			String key = NVLayer3Utils.getTechnologyFromBand(band) + Symbol.COMMA+bandwidth;
			Map<String, Double> map = aggrigateWrapperData.getDlPeakValueMap() != null? aggrigateWrapperData.getDlPeakValueMap() : new HashMap<>();
			if (map.containsKey(key)) {
				if (map.get(key) <dlMaxValue) {
					map.put(key, dlMaxValue);
				}
			} else {
				map.put(key, dlMaxValue);
			}
			aggrigateWrapperData.setDlPeakValueMap(map);
			}	
	}



	private void addEventsAndSiteInformationIntoJson(Layer3SummaryWrapper aggrigateWrapperData, StringBuilder json,
			QMDLLogCodeWrapper wrapper) {
		addEventDataIntoJson(aggrigateWrapperData,json,wrapper);
		addAddressIntoJson(json,wrapper);
		addSiteRelatedInfo(aggrigateWrapperData, json, wrapper);
	}

	private void addWIFIParamIntoJson(StringBuilder json, QMDLLogCodeWrapper wrapper,
			Layer3SummaryWrapper aggrigateWrapperData) {
	NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArray(wrapper.getWifiRssi()));
	NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArray(wrapper.getWifiSnr()));
	NVLayer3Utils.addValueToStringBuilder(json,wrapper.getSsid());
	setWifiValuesIntoAggrigateWrapper(wrapper,aggrigateWrapperData);
	}


	private void setWifiValuesIntoAggrigateWrapper(QMDLLogCodeWrapper wrapper,
			Layer3SummaryWrapper aggrigateWrapperData) {
		aggrigateWrapperData.setWifiRssi(NVLayer3Utils.getDoubleArrayValue(aggrigateWrapperData.getWifiRssi(),NVLayer3Utils.getAvgFromArray(wrapper.getWifiRssi())));
		aggrigateWrapperData.setWifiSnr(NVLayer3Utils.getDoubleArrayValue(aggrigateWrapperData.getWifiSnr(), NVLayer3Utils.getAvgFromArray(wrapper.getWifiSnr())));
		aggrigateWrapperData.setSsid(NVLayer3Utils.setValuesIntoSet(aggrigateWrapperData.getSsid(),wrapper.getSsid()));
		Double rssi = NVLayer3Utils.getAvgFromArray(wrapper.getWifiRssi());
		if(rssi!=null && rssi>QMDLConstant.WIFI_RSSI_THRESOLD){
			aggrigateWrapperData.setCntrssigrtthn90dbm(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getCntrssigrtthn90dbm(), QMDLConstant.RECORD_COUNT));
		}
		Double snr = NVLayer3Utils.getAvgFromArray(wrapper.getWifiSnr());
		if(snr!=null && snr>QMDLConstant.WIFI_SNR_THRESOLD){
			aggrigateWrapperData.setCntsnrgrtthn25dbm(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getCntsnrgrtthn25dbm(), QMDLConstant.RECORD_COUNT));
		}
	}


	private void addLiveParamForReport(StringBuilder json, QMDLLogCodeWrapper wrapper, Layer3SummaryWrapper aggrigateWrapperData) {
		NVLayer3Utils.addValueToStringBuilder(json,NVLayer3Utils.getAvgFromArray(wrapper.getAvgJitter()));
		NVLayer3Utils.addValueToStringBuilder(json,NVLayer3Utils.getAvgFromArray(wrapper.getAvgReponseTime()));
		addJiiterAndReponseTimeIntoAggrigateWrapper(wrapper, aggrigateWrapperData);
	}


	private void addJiiterAndReponseTimeIntoAggrigateWrapper(QMDLLogCodeWrapper wrapper,
			Layer3SummaryWrapper aggrigateWrapperData) {
		aggrigateWrapperData.setMinJitter(NVLayer3Utils.getMinValue(NVLayer3Utils.getAvgFromArray(wrapper.getAvgJitter()),aggrigateWrapperData.getMinJitter()));
		aggrigateWrapperData.setMaxJitter(NVLayer3Utils.getMaxValue(NVLayer3Utils.getAvgFromArray(wrapper.getAvgJitter()),aggrigateWrapperData.getMaxJitter()));

		aggrigateWrapperData.setMinresponseTime(NVLayer3Utils.getMinValue(NVLayer3Utils.getAvgFromArray(wrapper.getAvgReponseTime()),aggrigateWrapperData.getMinresponseTime()));
		aggrigateWrapperData.setMaxresponseTime(NVLayer3Utils.getMaxValue(NVLayer3Utils.getAvgFromArray(wrapper.getAvgReponseTime()),aggrigateWrapperData.getMaxresponseTime()));

		aggrigateWrapperData.setJitter(NVLayer3Utils.getDoubleArrayValue(aggrigateWrapperData.getJitter(), NVLayer3Utils.getAvgFromArray(wrapper.getAvgJitter())));
		aggrigateWrapperData.setResponseTime(NVLayer3Utils.getDoubleArrayValue(aggrigateWrapperData.getResponseTime(), NVLayer3Utils.getAvgFromArray(wrapper.getAvgReponseTime())));
	}

	private void addXpointAndYPointIntoJson(StringBuilder json, QMDLLogCodeWrapper wrapper) {
		NVLayer3Utils.addValueToStringBuilder(json,NVLayer3Utils.getAvgFromArray(wrapper.getXpoint()));
		NVLayer3Utils.addValueToStringBuilder(json,NVLayer3Utils.getAvgFromArray(wrapper.getYpoint()));

	}

	private void addSiteRelatedInfo(Layer3SummaryWrapper aggrigateWrapperData, StringBuilder json,
			QMDLLogCodeWrapper wrapper) {
		addBandIntoJson(aggrigateWrapperData,json,wrapper);
		addTechnologyToJson(aggrigateWrapperData,json,wrapper);
		addCGIIdToJson(aggrigateWrapperData,json,wrapper);
	}

	private void addTechnologyToJson(Layer3SummaryWrapper aggrigateWrapperData, StringBuilder json,
			QMDLLogCodeWrapper wrapper) {
		String technology= NVLayer3Utils.getTechnologyFromBand(wrapper.getBand());
		NVLayer3Utils.addValueToStringBuilder(json,technology);
		addTechnologyIntoAggrigateWrapper(aggrigateWrapperData,technology);

	}

	private void addTechnologyIntoAggrigateWrapper(Layer3SummaryWrapper aggrigateWrapperData,
			String technology) {
		if (technology != null) {
			Set<String> technologySet = aggrigateWrapperData.getTechnologyBandSet() != null ? aggrigateWrapperData.getTechnologyBandSet(): new HashSet<>();
			technologySet.add(technology);
			aggrigateWrapperData.setTechnologyBandSet(technologySet);

			if(technology.equals(QMDLConstant.TDD_TECHNOLOGY)){
				aggrigateWrapperData.setTddCount(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getTddCount(), QMDLConstant.RECORD_COUNT));
			}else{
				aggrigateWrapperData.setFddCount(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getFddCount(), QMDLConstant.RECORD_COUNT));
			}
		}
	}

	private void addCGIIdToJson(Layer3SummaryWrapper aggrigateWrapperData, StringBuilder json,
			QMDLLogCodeWrapper wrapper) {
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getCgi());
		addSiteInfoIntoAggrigatewrapper(aggrigateWrapperData, wrapper);
	}

	private void addSiteInfoIntoAggrigatewrapper(Layer3SummaryWrapper aggrigateWrapperData, QMDLLogCodeWrapper wrapper) {
		setCgiValuesIntoMap(aggrigateWrapperData, wrapper);
		setEnodeBIdValuesIntoMap(aggrigateWrapperData, wrapper);
		addCellIdIntoSetAndMap(aggrigateWrapperData, wrapper);
	}

	private void setEnodeBIdValuesIntoMap(Layer3SummaryWrapper aggrigateWrapperData, QMDLLogCodeWrapper wrapper) {
		Map<Integer,Integer> enodeBMap= aggrigateWrapperData.getEnodeBMap()!= null ? aggrigateWrapperData.getEnodeBMap(): new HashMap<>();
		if (enodeBMap.containsKey( wrapper.geteNodebId())) {
			enodeBMap.put(wrapper.geteNodebId(), enodeBMap.get(wrapper.geteNodebId()) + QMDLConstant.RECORD_COUNT);
		} else {
			enodeBMap.put(wrapper.geteNodebId(), QMDLConstant.RECORD_COUNT);
		}
		aggrigateWrapperData.setEnodeBMap(enodeBMap);
	}



	private void addCellIdIntoSetAndMap(Layer3SummaryWrapper aggrigateWrapperData, QMDLLogCodeWrapper wrapper) {
		if (wrapper.getCellid() != null) {
			Set<Integer> cellSet = aggrigateWrapperData.getCellidSet() != null ? aggrigateWrapperData.getCellidSet(): new HashSet<>();
			cellSet.add(wrapper.getCellid());
			aggrigateWrapperData.setCellidSet(cellSet);

			Map<Integer,Integer> cellMap= aggrigateWrapperData.getCellIdMap()!= null ? aggrigateWrapperData.getCellIdMap(): new HashMap<>();
			if (cellMap.containsKey( wrapper.getCellid())) {
				cellMap.put(wrapper.getCellid(), cellMap.get(wrapper.getCellid()) + QMDLConstant.RECORD_COUNT);
			} else {
				cellMap.put(wrapper.getCellid(), QMDLConstant.RECORD_COUNT);
			}
			aggrigateWrapperData.setCellIdMap(cellMap);
		}
	}



	private void setCgiValuesIntoMap(Layer3SummaryWrapper aggrigateWrapperData, QMDLLogCodeWrapper wrapper) {
		Map<Integer,Integer> cgiMap= aggrigateWrapperData.getCgiMap()!= null ? aggrigateWrapperData.getCgiMap(): new HashMap<>();
		if (cgiMap.containsKey( wrapper.getCgi())) {
			cgiMap.put(wrapper.getCgi(), cgiMap.get(wrapper.getCgi()) + QMDLConstant.RECORD_COUNT);
		} else {
			cgiMap.put(wrapper.getCgi(), QMDLConstant.RECORD_COUNT);
		}
		aggrigateWrapperData.setCgiMap(cgiMap);
	}

	private void addBandIntoJson(Layer3SummaryWrapper aggrigateWrapperData, StringBuilder json,
			QMDLLogCodeWrapper wrapper) {
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getBand());
		if (wrapper.getBand() != null) {
			Set<String> bandSet = aggrigateWrapperData.getBandSet() != null ? aggrigateWrapperData.getBandSet(): new HashSet<>();
			bandSet.add(wrapper.getBand());
			aggrigateWrapperData.setBandSet(bandSet);
		}
	}

	private void addAddressIntoJson(StringBuilder json, QMDLLogCodeWrapper wrapper) {
		NVLayer3Utils.addValueToStringBuilder(json,getAddressByLatLong(wrapper));
	}

	private void addEventDataIntoJson(Layer3SummaryWrapper aggrData, StringBuilder json,
			QMDLLogCodeWrapper wrapper) {		
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.addIntegerValue(wrapper.getNewHandOverIntiateInterCount(),wrapper.getNewHandOverIntiateIntraCount()));
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getNewHandOverFailureCount());
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getNewHandOverSuccessCount());
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getCallInitiateCount());
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getCallDropCount());
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getCallFailureCount());
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getCallSuccessCount());
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getCellChangeCount());
		setEventInformationIntoSummaryWrapper(aggrData, wrapper);
	}



	private void setEventInformationIntoSummaryWrapper(Layer3SummaryWrapper aggrData, QMDLLogCodeWrapper wrapper) {

		aggrData.setRrcInitiate(NVLayer3Utils.addIntegerValue(aggrData.getRrcInitiate(),wrapper.getRrcInitiate()));
		aggrData.setRrcSucess(NVLayer3Utils.addIntegerValue(aggrData.getRrcSucess(),wrapper.getRrcSucess()));

		aggrData.setCallInitiateCount(NVLayer3Utils.addIntegerValue(aggrData.getCallInitiateCount(),wrapper.getCallInitiateCount()));
		aggrData.setCallDropCount(NVLayer3Utils.addIntegerValue(aggrData.getCallDropCount(),wrapper.getCallDropCount()));
		aggrData.setCallFailureCount(NVLayer3Utils.addIntegerValue(aggrData.getCallFailureCount(),wrapper.getCallFailureCount()));
		aggrData.setCallSuccessCount(NVLayer3Utils.addIntegerValue(aggrData.getCallSuccessCount(),wrapper.getCallSuccessCount()));		
		aggrData.setCallSetupSuccessCount(NVLayer3Utils.addIntegerValue(aggrData.getCallSetupSuccessCount(),wrapper.getCallSetupSuccessCount()));
		aggrData.setCallSetupCount(NVLayer3Utils.addIntegerValue(aggrData.getCallSetupCount(),wrapper.getCallSetupCount()));

	}

	private void addLogCodeWiseDataIntoJson(Layer3SummaryWrapper aggrigateWrapperData, StringBuilder json,
			QMDLLogCodeWrapper wrapper) {
		addB193BeanDataToJsonList(json, wrapper,aggrigateWrapperData);
		addB1B9DataToJsonLte(json, wrapper,aggrigateWrapperData);
		addB0C2ToJsonLte(json, wrapper);
		addB126ToJsonLte(json, wrapper);
		addB139ToJsonLte(json, wrapper,aggrigateWrapperData);
		addB14DToJsonLte(json, wrapper,aggrigateWrapperData);
		addB168ToJsonLte(json, wrapper);
		addB18AToJsonLte(json, wrapper);
		addCsvDataIntoJsonLte(json,wrapper,aggrigateWrapperData);

	}

	private void setStartAndEndTime(Layer3SummaryWrapper aggrigateWrapperData, QMDLLogCodeWrapper wrapper) {
		if (aggrigateWrapperData.getTimeStamp() == null) {
			setStartDriveParam(aggrigateWrapperData, wrapper);
			setEndDriveParam(aggrigateWrapperData, wrapper);
		} else {
			if (aggrigateWrapperData.getTimeStamp() > wrapper.getTimeStamp()) {
				setStartDriveParam(aggrigateWrapperData, wrapper);

			}else if (aggrigateWrapperData.getEtimeStamp() < wrapper.getTimeStamp()) {
				setEndDriveParam(aggrigateWrapperData, wrapper);

			}
		}
	}

	private void setEndDriveParam(Layer3SummaryWrapper aggrigateWrapperData, QMDLLogCodeWrapper wrapper) {
		aggrigateWrapperData.setEtimeStamp(wrapper.getTimeStamp());
		aggrigateWrapperData.setEndLat(wrapper.getLat());
		aggrigateWrapperData.setEndLong(wrapper.getLon());
	}

	private void setStartDriveParam(Layer3SummaryWrapper aggrigateWrapperData, QMDLLogCodeWrapper wrapper) {
		aggrigateWrapperData.setTimeStamp(wrapper.getTimeStamp());
		aggrigateWrapperData.setAddress(getAddressByLatLong(wrapper));
		aggrigateWrapperData.setStartLat(wrapper.getLat());
		aggrigateWrapperData.setStartLong(wrapper.getLon());
	}

	private String getAddressByLatLong(QMDLLogCodeWrapper wrapper) {
		try {
			if (wrapper.getLat() != null && wrapper.getLon() != null) {
				String address = NVLayer3Utils.getAddressByLatLong(wrapper.getLat(), wrapper.getLon());
				return address.replace(Symbol.COMMA, Symbol.UNDERSCORE);
			}
		} catch (IOException e) {
			logger.error("Error in Getting address {}", Utils.getStackTrace(e));
		}
		return null;
	}

	private void addCsvDataIntoJsonLte(StringBuilder json, QMDLLogCodeWrapper wrapper, Layer3SummaryWrapper aggrigateWrapperData) {
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArray(wrapper.getDlThroughPut()));
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArray(wrapper.getUlThroughPut()));
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArray(wrapper.getLatency()));
		setCsvValuesIntoAggrigateWrapper(wrapper, aggrigateWrapperData);

	}

	private void setCsvValuesIntoAggrigateWrapper(QMDLLogCodeWrapper wrapper,
			Layer3SummaryWrapper aggrigateWrapperData) {


		setAvgValueOfCsvField(wrapper, aggrigateWrapperData);
		setMinMaxOfCsvField(wrapper, aggrigateWrapperData);
		Double dlAvg = NVLayer3Utils.getAvgFromArray(wrapper.getDlThroughPut());
		if (dlAvg!=null&&dlAvg > QMDLConstant.IN_BUILDING_DL) {
			aggrigateWrapperData.setCountDlGreaterThan5Mbps(NVLayer3Utils
					.addIntegerValue(aggrigateWrapperData.getCountDlGreaterThan5Mbps(), QMDLConstant.RECORD_COUNT));
		}
	}



	private void setMinMaxOfCsvField(QMDLLogCodeWrapper wrapper, Layer3SummaryWrapper aggrigateWrapperData) {

		aggrigateWrapperData.setMinDl(NVLayer3Utils.getMinValue(NVLayer3Utils.getAvgFromArray(wrapper.getDlThroughPut()),aggrigateWrapperData.getMinDl()));
		aggrigateWrapperData.setMaxDl(NVLayer3Utils.getMaxValue(NVLayer3Utils.getAvgFromArray(wrapper.getDlThroughPut()),aggrigateWrapperData.getMaxDl()));

		aggrigateWrapperData.setMinUl(NVLayer3Utils.getMinValue(NVLayer3Utils.getAvgFromArray(wrapper.getUlThroughPut()),aggrigateWrapperData.getMinUl()));
		aggrigateWrapperData.setMaxUl(NVLayer3Utils.getMaxValue(NVLayer3Utils.getAvgFromArray(wrapper.getUlThroughPut()),aggrigateWrapperData.getMaxUl()));

		aggrigateWrapperData.setMinLatency(NVLayer3Utils.getMinValue(NVLayer3Utils.getAvgFromArray(wrapper.getLatency()),aggrigateWrapperData.getMinLatency()));
		aggrigateWrapperData.setMaxLatency(NVLayer3Utils.getMaxValue(NVLayer3Utils.getAvgFromArray(wrapper.getLatency()),aggrigateWrapperData.getMaxLatency()));

		aggrigateWrapperData.setMinFtpDl(NVLayer3Utils.getMinValue(NVLayer3Utils.getAvgFromArray(wrapper.getFtpDl()),aggrigateWrapperData.getMinFtpDl()));
		aggrigateWrapperData.setMaxFtpDl(NVLayer3Utils.getMaxValue(NVLayer3Utils.getAvgFromArray(wrapper.getFtpDl()),aggrigateWrapperData.getMaxFtpDl()));
		
		aggrigateWrapperData.setMinFtpUl(NVLayer3Utils.getMinValue(NVLayer3Utils.getAvgFromArray(wrapper.getFtpUl()),aggrigateWrapperData.getMinFtpUl()));
		aggrigateWrapperData.setMaxFtpUl(NVLayer3Utils.getMaxValue(NVLayer3Utils.getAvgFromArray(wrapper.getFtpUl()),aggrigateWrapperData.getMaxFtpUl()));
		
		aggrigateWrapperData.setMinHttpDl(NVLayer3Utils.getMinValue(NVLayer3Utils.getAvgFromArray(wrapper.getHttpDl()),aggrigateWrapperData.getMinHttpDl()));
		aggrigateWrapperData.setMaxHttpDl(NVLayer3Utils.getMaxValue(NVLayer3Utils.getAvgFromArray(wrapper.getHttpDl()),aggrigateWrapperData.getMaxHttpDl()));
		
		aggrigateWrapperData.setMinHttpUl(NVLayer3Utils.getMinValue(NVLayer3Utils.getAvgFromArray(wrapper.getHttpUl()),aggrigateWrapperData.getMinHttpUl()));
		aggrigateWrapperData.setMaxHttpUl(NVLayer3Utils.getMaxValue(NVLayer3Utils.getAvgFromArray(wrapper.getHttpUl()),aggrigateWrapperData.getMaxHttpUl()));
	}



	private void setAvgValueOfCsvField(QMDLLogCodeWrapper wrapper, Layer3SummaryWrapper aggrigateWrapperData) {
//		aggrigateWrapperData.setDlThroughPut(NVLayer3Utils.getDoubleArrayValue(aggrigateWrapperData.getDlThroughPut(), NVLayer3Utils.getAvgFromArray(wrapper.getDlThroughPut())));
//		aggrigateWrapperData.setUlThroughPut(NVLayer3Utils.getDoubleArrayValue(aggrigateWrapperData.getUlThroughPut(),NVLayer3Utils.getAvgFromArray( wrapper.getUlThroughPut())));
//		aggrigateWrapperData.setLatency(NVLayer3Utils.getDoubleAggrigateArray(aggrigateWrapperData.getLatency(), wrapper.getLatency()));
	
		aggrigateWrapperData.setDlThroughPut(NVLayer3Utils.getDoubleAggrigateArray(aggrigateWrapperData.getDlThroughPut(), wrapper.getDlThroughPut()));
		aggrigateWrapperData.setUlThroughPut(NVLayer3Utils.getDoubleAggrigateArray(aggrigateWrapperData.getUlThroughPut(), wrapper.getUlThroughPut()));
		aggrigateWrapperData.setLatency(NVLayer3Utils.getDoubleArrayValue(aggrigateWrapperData.getLatency(), NVLayer3Utils.getAvgFromArray(wrapper.getLatency())));
		
		
		aggrigateWrapperData.setFtpDl(NVLayer3Utils.getDoubleArrayValue(aggrigateWrapperData.getFtpDl(), NVLayer3Utils.getAvgFromArray(wrapper.getFtpDl())));
		aggrigateWrapperData.setFtpUl(NVLayer3Utils.getDoubleArrayValue(aggrigateWrapperData.getFtpUl(), NVLayer3Utils.getAvgFromArray(wrapper.getFtpUl())));
		aggrigateWrapperData.setHttpDl(NVLayer3Utils.getDoubleArrayValue(aggrigateWrapperData.getHttpDl(), NVLayer3Utils.getAvgFromArray(wrapper.getHttpDl())));
		aggrigateWrapperData.setHttpUl(NVLayer3Utils.getDoubleArrayValue(aggrigateWrapperData.getHttpUl(), NVLayer3Utils.getAvgFromArray(wrapper.getHttpUl())));
	}

	private void addPostFixOfcsvIntoJson(StringBuilder finalJsonString) {
		if(finalJsonString.length()>QMDLConstant.STRING_EXTRA_LENTH){
			finalJsonString.setLength(finalJsonString.length()-QMDLConstant.STRING_EXTRA_LENTH);
		}
		finalJsonString.append(Symbol.BRACKET_CLOSE_STRING);
	}

	private StringBuilder getIntialStringForJson() {
		StringBuilder jsonString=new StringBuilder();
		jsonString.append(Symbol.BRACKET_OPEN_STRING);
		return jsonString;
	}

	private String getFinalJsonForRecord(String stringValue) {
		stringValue=stringValue.substring(QMDLConstant.STRING_START_INDEX,stringValue.length()-QMDLConstant.STRING_EXTRA_LENTH);
		return Symbol.BRACKET_OPEN_STRING+stringValue+Symbol.BRACKET_CLOSE_STRING+QMDLConstant.COMMA;
	}

	/**
	 * Add b193 data field into put.
	 * @param put put object
	 * @param wrapperData bean data
	 * @param aggrigateWrapperData
	 * @return
	 */
	private void addB193BeanDataToJsonList(StringBuilder json, QMDLLogCodeWrapper wrapperData, Layer3SummaryWrapper aggrigateWrapperData) {
		NVLayer3Utils.addValueToStringBuilder(json, wrapperData.getPci());
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArray(wrapperData.getrSSIData()));
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArray(wrapperData.getMeasureRSRPData()));
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArray(wrapperData.getMeasureRSRQData()));
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArray(wrapperData.getsINRData()));
		setB193DataIntoAggrigateWrapper(wrapperData, aggrigateWrapperData);
		NVLayer3Utils.addValueToStringBuilder(json, wrapperData.getEarfcn());
		/*
		NVLayer3Utils.addValueToStringBuilder(json, wrapperData.getServingCellIndex());
		NVLayer3Utils.addValueToStringBuilder(json, wrapperData.getCurrentSFN());
		NVLayer3Utils.addValueToStringBuilder(json, wrapperData.getCellTiming0());
		NVLayer3Utils.addValueToStringBuilder(json, wrapperData.getCellTiming1());
		NVLayer3Utils.addValueToStringBuilder(json, wrapperData.getCellTimingSFN0());
		NVLayer3Utils.addValueToStringBuilder(json, wrapperData.getCellTimingSFN1());*/
		/*NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArray(wrapperData.getrSRPRx0Data()));
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArray(wrapperData.getrSRPRx1Data()));
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArray(wrapperData.getrSRQRx0Data()));
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArray(wrapperData.getrSRQRx1Data()));*/
		/*NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArray(wrapperData.getrSSIRx0Data()));
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArray(wrapperData.getrSSIRx1Data()));*/
	}

	private void setB193DataIntoAggrigateWrapper(QMDLLogCodeWrapper wrapperData,
			Layer3SummaryWrapper aggrigateWrapperData) {

		setB193AvgValueIntoAggrigatedWrapper(wrapperData, aggrigateWrapperData);
		setB193MinMaxIntoAggrigateWrapper(wrapperData, aggrigateWrapperData);
		aggrigateWrapperData.setEarfcn(NVLayer3Utils.setValuesIntoSet(aggrigateWrapperData.getEarfcn(), wrapperData.getEarfcn()));
		setInBuildingReportKpiCount(wrapperData,aggrigateWrapperData);
		setPciValuesIntoAggrigateWrapper(wrapperData, aggrigateWrapperData);
	}



	private void setB193MinMaxIntoAggrigateWrapper(QMDLLogCodeWrapper wrapperData,
			Layer3SummaryWrapper aggrigateWrapperData) {
//		aggrigateWrapperData.setrSSIData(NVLayer3Utils.getDoubleAggrigateArray(aggrigateWrapperData.getrSSIData(), wrapperData.getrSSIData()));
//		aggrigateWrapperData.setMeasureRSRPData(NVLayer3Utils.getDoubleAggrigateArray(aggrigateWrapperData.getMeasureRSRPData(),wrapperData.getMeasureRSRPData()));
//		aggrigateWrapperData.setMeasureRSRQData(NVLayer3Utils.getDoubleAggrigateArray(aggrigateWrapperData.getMeasureRSRQData(),wrapperData.getMeasureRSRQData()));
//		aggrigateWrapperData.setsINRData(NVLayer3Utils.getDoubleAggrigateArray(aggrigateWrapperData.getsINRData(),wrapperData.getsINRData()));

		aggrigateWrapperData.setMinRSRP(NVLayer3Utils.getMinValue(NVLayer3Utils.getAvgFromArray(wrapperData.getMeasureRSRPData()),aggrigateWrapperData.getMinRSRP()));
		aggrigateWrapperData.setMaxRSRP(NVLayer3Utils.getMaxValue(NVLayer3Utils.getAvgFromArray(wrapperData.getMeasureRSRPData()),aggrigateWrapperData.getMaxRSRP()));

		aggrigateWrapperData.setMinSINR(NVLayer3Utils.getMinValue(NVLayer3Utils.getAvgFromArray(wrapperData.getsINRData()),aggrigateWrapperData.getMinSINR()));
		aggrigateWrapperData.setMaxSINR(NVLayer3Utils.getMaxValue(NVLayer3Utils.getAvgFromArray(wrapperData.getsINRData()),aggrigateWrapperData.getMaxSINR()));

		aggrigateWrapperData.setMinRsrq(NVLayer3Utils.getMinValue(NVLayer3Utils.getAvgFromArray(wrapperData.getMeasureRSRQData()),aggrigateWrapperData.getMinRsrq()));
		aggrigateWrapperData.setMaxRsrq(NVLayer3Utils.getMaxValue(NVLayer3Utils.getAvgFromArray(wrapperData.getMeasureRSRQData()),aggrigateWrapperData.getMaxRsrq()));
	
		aggrigateWrapperData.setMinRssi(NVLayer3Utils.getMinValue(NVLayer3Utils.getAvgFromArray(wrapperData.getrSSIData()),aggrigateWrapperData.getMinRssi()));
		aggrigateWrapperData.setMaxRssi(NVLayer3Utils.getMaxValue(NVLayer3Utils.getAvgFromArray(wrapperData.getrSSIData()),aggrigateWrapperData.getMaxRssi()));
	}



	private void setB193AvgValueIntoAggrigatedWrapper(QMDLLogCodeWrapper wrapperData,
			Layer3SummaryWrapper aggrigateWrapperData) {
		aggrigateWrapperData.setrSSIData(NVLayer3Utils.getDoubleArrayValue(aggrigateWrapperData.getrSSIData(),NVLayer3Utils.getAvgFromArray(wrapperData.getrSSIData())));
		aggrigateWrapperData.setMeasureRSRPData(NVLayer3Utils.getDoubleArrayValue(aggrigateWrapperData.getMeasureRSRPData(),NVLayer3Utils.getAvgFromArray(wrapperData.getMeasureRSRPData())));
		aggrigateWrapperData.setMeasureRSRQData(NVLayer3Utils.getDoubleArrayValue(aggrigateWrapperData.getMeasureRSRQData(),NVLayer3Utils.getAvgFromArray(wrapperData.getMeasureRSRQData())));
		aggrigateWrapperData.setsINRData(NVLayer3Utils.getDoubleArrayValue(aggrigateWrapperData.getsINRData(),NVLayer3Utils.getAvgFromArray(wrapperData.getsINRData())));
	}

	private void setPciValuesIntoAggrigateWrapper(QMDLLogCodeWrapper wrapperData,
			Layer3SummaryWrapper aggrigateWrapperData) {
		if(wrapperData.getPci()!=null){
		Map<Integer,Integer> pciMap = aggrigateWrapperData.getPciMap()!=null?aggrigateWrapperData.getPciMap():new HashMap<>() ;
		if (pciMap.containsKey(wrapperData.getPci())) {
			pciMap.put(wrapperData.getPci(), pciMap.get(wrapperData.getPci()) + QMDLConstant.RECORD_COUNT);
		} else {
			pciMap.put(wrapperData.getPci(), QMDLConstant.RECORD_COUNT);
		}
		aggrigateWrapperData.setPciMap(pciMap);
		}
	}



	private void setInBuildingReportKpiCount(QMDLLogCodeWrapper wrapperData,
			Layer3SummaryWrapper aggrigateWrapperData) {
		Double rsrp = NVLayer3Utils.getAvgFromArray(wrapperData.getMeasureRSRPData());
		if(rsrp!=null&&rsrp>QMDLConstant.IN_BUILDING_RSRP){
			aggrigateWrapperData.setCountRsrpGreaterThan100Dbm(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getCountRsrpGreaterThan100Dbm(), QMDLConstant.RECORD_COUNT));
		}

		Double sinr = NVLayer3Utils.getAvgFromArray(wrapperData.getsINRData());
		if(sinr!=null&&sinr>QMDLConstant.IN_BUILDING_SINR){
			aggrigateWrapperData.setCountSinrGreaterThan12Db(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getCountSinrGreaterThan12Db(), QMDLConstant.RECORD_COUNT));
		}

	}

	/**
	 * Add b1b9 data field into put.
	 * @param put put object
	 * @param wrapperData bean data
	 * @param aggrigateWrapperData
	 * @param aggrigateWrapperData
	 */
	private void addB1B9DataToJsonLte(StringBuilder json, QMDLLogCodeWrapper wrapperData, Layer3SummaryWrapper aggrigateWrapperData) {
		//NVLayer3Utils.addValueToStringBuilder(json, wrapperData.getDlEarfcn());
		//logger.debug("The dl earfcn value inside B1B9 {}",wrapperData.getDlEarfcn());
		NVLayer3Utils.addValueToStringBuilder(json, wrapperData.getUlEarfcn());
		setUlDlValuesIntoAggrigateWrapper(wrapperData,aggrigateWrapperData);
		/*NVLayer3Utils.addValueToStringBuilder(json, wrapperData.getTddConfig());
		NVLayer3Utils.addValueToStringBuilder(json, wrapperData.getSpecialSubframeConfiguration());
		NVLayer3Utils.addValueToStringBuilder(json, wrapperData.getDlCp());
		NVLayer3Utils.addValueToStringBuilder(json, wrapperData.getUlCp());*/
	}

	private void setUlDlValuesIntoAggrigateWrapper(QMDLLogCodeWrapper wrapperData,
			Layer3SummaryWrapper aggrigateWrapperData) {
		if (wrapperData.getUlEarfcn() != null && wrapperData.getDlEarfcn() != null) {
			Set<String> ulDlEarfcnSet = aggrigateWrapperData.getUlDlEarfcn() != null? aggrigateWrapperData.getUlDlEarfcn() : new HashSet<>();
			ulDlEarfcnSet.add(wrapperData.getUlEarfcn() + Symbol.SLASH_FORWARD_STRING + wrapperData.getDlEarfcn());
			aggrigateWrapperData.setUlDlEarfcn(ulDlEarfcnSet);
		}
	}

	/**
	 * Add B0C2 data field into put.
	 * @param put  put object
	 * @param wrapperData bean data
	 */
	private void addB0C2ToJsonLte(StringBuilder json, QMDLLogCodeWrapper wrapperData) {
		//NVLayer3Utils.addValueToStringBuilder(json, wrapperData.getB0c2Pci());
		NVLayer3Utils.addValueToStringBuilder(json, wrapperData.getDl_Bandwidth());
		NVLayer3Utils.addValueToStringBuilder(json, wrapperData.getUl_Bandwidth());
		/*NVLayer3Utils.addValueToStringBuilder(json, wrapperData.getTracking_Area_Code());*/
		/*NVLayer3Utils.addValueToStringBuilder(json, wrapperData.getMcc());
		NVLayer3Utils.addValueToStringBuilder(json, wrapperData.getMnc());*/
	}


	/**
	 * Add b18A data field into put.
	 * @param put put object
	 * @param wrapperData bean data
	 */
	private void addB18AToJsonLte(StringBuilder json, QMDLLogCodeWrapper wrapperData) {
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArray(wrapperData.getOutOfSyncBler()));
	}

	/**
	 * Add b126 data field into put.
	 * @param put
	 * @param wrapperData bean data
	 */
	private void addB126ToJsonLte(StringBuilder json, QMDLLogCodeWrapper wrapperData) {
		/*NVLayer3Utils.addValueToStringBuilder(json, wrapperData.getNumberRxAntennas());
		NVLayer3Utils.addValueToStringBuilder(json, wrapperData.getNumberTxAntennas());
		NVLayer3Utils.addValueToStringBuilder(json, wrapperData.getrBAllocationSlot0());
		NVLayer3Utils.addValueToStringBuilder(json, wrapperData.getrBAllocationSlot1());*/
		NVLayer3Utils.addValueToStringBuilder(json, wrapperData.getSpatialRank());
		NVLayer3Utils.addValueToStringBuilder(json, wrapperData.getIndexPMI());
	}



	/**
	 * Add b139 data field into put.
	 * @param put
	 * @param wrapperData
	 * @param aggrigateWrapperData 
	 */
	private void addB139ToJsonLte(StringBuilder json, QMDLLogCodeWrapper wrapperData, Layer3SummaryWrapper aggrigateWrapperData) {
		NVLayer3Utils.addValueToStringBuilder(json, getAvgFromArrayForULPRBUtilization(wrapperData));
		aggrigateWrapperData.setUlPRBUtilization(NVLayer3Utils.getDoubleAggrigateArray(aggrigateWrapperData.getUlPRBUtilization(), wrapperData.getUlPRBUtilization()));
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArray(wrapperData.getPuschTxPower()));
	}

	private Double getAvgFromArrayForULPRBUtilization(QMDLLogCodeWrapper wrapperData) {
		Double ulPRBUtilization = null; 
		if((NVLayer3Utils.getAvgFromArray(wrapperData.getUlPRBUtilization()))!=null) {
			ulPRBUtilization = (NVLayer3Utils.getAvgFromArray(wrapperData.getUlPRBUtilization()))*QMDLConstant.PERCENTAGE_MULTIPLIER;
		}
		return ulPRBUtilization;
		
	}

	/**
	 * Add b14D data field into put.
	 * @param put put object
	 * @param wrapperData bean data
	 * @param aggrigateWrapperData
	 */
	private void addB14DToJsonLte(StringBuilder json, QMDLLogCodeWrapper wrapperData, Layer3SummaryWrapper aggrigateWrapperData) {
		NVLayer3Utils.addValueToStringBuilder(json,  wrapperData.getRankIndex());
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArrayAndReturnIntegerValue(wrapperData.getCqiCwo()));
		NVLayer3Utils.addValueToStringBuilder(json,  wrapperData.getCarrierIndex());
		aggrigateWrapperData.setCqiCwo(NVLayer3Utils.getDoubleArrayValue(aggrigateWrapperData.getCqiCwo(), NVLayer3Utils.getAvgFromArray(wrapperData.getCqiCwo())));
		if (wrapperData.getRankIndex() != null) {
			if (wrapperData.getRankIndex().equals(QMDLConstant.RANGE_2)) {
				aggrigateWrapperData.setMimoCount(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getMimoCount(), QMDLConstant.RECORD_COUNT));
			}
			aggrigateWrapperData.setMimoTotalCount(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getMimoTotalCount(), QMDLConstant.RECORD_COUNT));
		}
	}
	/**
	 * Add b169 data field into put.
	 * @param put put object
	 * @param wrapperData bean data
	 */
	private void addB169ToJsonLte(StringBuilder json, QMDLLogCodeWrapper wrapperData) {
		NVLayer3Utils.addValueToStringBuilder(json,  wrapperData.getMcs());
		}

	/**
	 * Add B168 data field into put.
	 * @param wrapperData bean data Wrapper
	 * @param put the put map data
	 */
	private void addB168ToJsonLte(StringBuilder json, QMDLLogCodeWrapper wrapperData) {
		NVLayer3Utils.addValueToStringBuilder(json,  wrapperData.getTimingAdvance());
	}

	/**
	 * Add B173 data field into put.
	 * @param put put object
	 * @param wrapperData data bean
	 */
	private void addB173ToJsonLte(StringBuilder json, QMDLLogCodeWrapper wrapperData) {
		NVLayer3Utils.addValueToStringBuilder(json, NVLayer3Utils.getAvgFromArray( wrapperData.getB173numRbs()));
	}

	
	private void addlatLongintoString(Double lat, Double lon, StringBuilder json) {
		NVLayer3Utils.addValueToStringBuilder(json, lat);
		NVLayer3Utils.addValueToStringBuilder(json, lon);
	}


	private void addVoLteCallDropCountToJson(StringBuilder json, Layer3SummaryWrapper wrapper) {
		NVLayer3Utils.addValueToStringBuilder(json, wrapper.getVoLTECallDropCount());
	}

	private void addVoLteCallDropCountToAggregatedData(Layer3SummaryWrapper aggregatedWrapperData,
			QMDLLogCodeWrapper wrapper) {
		aggregatedWrapperData.setVoLTECallDropCount(NVLayer3Utils.addIntegerValue(aggregatedWrapperData.getVoLTECallDropCount(),wrapper.getVoLTECallDropCount()));
	}

	@Override
	//@Transactional
	public String fileProcessWorkorderWise(Integer workorderId,Integer recipeId,String assignTo) throws IOException {
		if(workorderId!=null && recipeId!=null){
			return processRecipeWiseFile(workorderId, recipeId,assignTo);
		}else if(workorderId!=null){
			return processWorkorderWiseFile(workorderId, recipeId,assignTo);
		}else{
			return ForesightConstants.EXCEPTION_INVALID_PARAMS;
		}

	}

	private String processWorkorderWiseFile(Integer workorderId, Integer recipeId, String assignTo) throws IOException {
		if (requestWorkOrder.contains(workorderId)) {
			logger.info("File processing  already in  queue  {} assignTo {} ",workorderId,assignTo);
			return QMDLConstant.IN_PROGRESS_RESPONSE_JSON;
		} else {
			processUnprocessQMDLFiles(workorderId, recipeId,assignTo);
			return ForesightConstants.SUCCESS_JSON;
		}
	}

	private String processRecipeWiseFile(Integer workorderId, Integer recipeId, String assignTo) throws IOException {
		if (requestWorkOrder.contains(workorderId)&&requestRecipe.contains(recipeId)) {
			logger.info("File processing  already in  queue {} recipeId {} assignTo {} ",workorderId,recipeId,assignTo);
			return QMDLConstant.IN_PROGRESS_RESPONSE_JSON;
		} else {
			processUnprocessQMDLFiles(workorderId, recipeId,assignTo);
			return ForesightConstants.SUCCESS_JSON;
		}
	}
	
	private void deleteTempFilesFromLocal(List<String> tempFiles) {
		for(String tempFilePath:tempFiles) {
			if(!StringUtils.isBlank(tempFilePath)) {
				File tempFile = new File(tempFilePath);
				if(tempFile.exists()) {
					boolean delete = tempFile.delete();
					logger.info("File Deleted {}",delete);
				}
			}
		}
	}
}

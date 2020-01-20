package com.inn.foresight.module.nv.layer3.service.parse.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.hadoop.hbase.client.Put;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.inn.commons.Symbol;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.NVConfigUtil;
import com.inn.foresight.module.nv.layer3.constants.QMDLConstant;
import com.inn.foresight.module.nv.layer3.qmdlParser.wrappers.Layer3SummaryWrapper;
import com.inn.foresight.module.nv.layer3.qmdlParser.wrappers.NVL3CsvDataWrapper;
import com.inn.foresight.module.nv.layer3.qmdlParser.wrappers.QMDLLogCodeWrapper;
import com.inn.foresight.module.nv.layer3.service.parse.INVL3CsvProcessService;
import com.inn.foresight.module.nv.layer3.service.parse.INVLayer3StatsService;
import com.inn.foresight.module.nv.layer3.utils.NVLayer3Utils;
import com.inn.foresight.module.nv.layer3.utils.NetworkDataFormats;
import com.inn.foresight.module.nv.layer3.utils.NvCsvParsingCommonUtils;

/** The Class NVLayer3CsvParseServiceImpl. */
@Service("NVL3CsvProcessServiceImpl")
public class NVL3CsvProcessServiceImpl implements INVL3CsvProcessService {

	/** The logger. */
	private static final Logger logger = LogManager.getLogger(NVL3CsvProcessServiceImpl.class);

	/** The nv layer 3 stats generator service. */
	@Autowired
	private INVLayer3StatsService nvLayer3StatsGeneratorService;

	@Override
	public void processCsvFile(ZipFile zipFile,ZipEntry entry,TreeMap<Long, QMDLLogCodeWrapper> woParsedMap,Map<String, TreeMap<Long, Object>> kpiValuesMap,Layer3SummaryWrapper aggrigateWrapperData,Map<String,Put> imagePutMap, List<String> processedFiles,String localFilePath) throws IOException{
		 List<NVL3CsvDataWrapper> csvDataHolderList=new ArrayList<>();
        processCsvFile(zipFile,entry, csvDataHolderList,aggrigateWrapperData,imagePutMap, processedFiles,localFilePath);
        if(!csvDataHolderList.isEmpty()){
        	processCsvDataForLayer3(csvDataHolderList,woParsedMap,kpiValuesMap,aggrigateWrapperData);
        }
	}

	/**
	 * Process csv data for layer 3.
	 *
	 * @param csvDataHolderList the csv data holder list
	 * @param rowPrefix the row prefix
	 * @param woParsedMap
	 * @param kpiValuesMap
	 * @throws RestException the rest exception
	 */
	@Override
	public void processCsvDataForLayer3(List<NVL3CsvDataWrapper> csvDataHolderList, TreeMap<Long, QMDLLogCodeWrapper> woParsedMap, Map<String, TreeMap<Long, Object>> kpiValuesMap,Layer3SummaryWrapper aggrigateWrapperData) {
		processRawData(csvDataHolderList,woParsedMap,aggrigateWrapperData);
		//processDataForStats(csvDataHolderList,kpiValuesMap);
	}

	/**
	 * Process data for stats.
	 *
	 * @param csvDataHolderList the csv data holder list
	 * @param rowPrefix the row prefix
	 * @param kpiMap
	 * @throws RestException the rest exception
	 */
	@Override
	public void processDataForStats(List<NVL3CsvDataWrapper> csvDataHolderList, Map<String, TreeMap<Long, Object>> kpiMap){
		TreeMap<Long,Object> ulDataMap=kpiMap.containsKey(QMDLConstant.UL_THROUGHTPUT)?kpiMap.get(QMDLConstant.UL_THROUGHTPUT):new TreeMap<>();
		TreeMap<Long,Object> dlDataMap=kpiMap.containsKey(QMDLConstant.DL_THROUGHTPUT)?kpiMap.get(QMDLConstant.DL_THROUGHTPUT):new TreeMap<>();
		TreeMap<Long,Object> latency=kpiMap.containsKey(QMDLConstant.LATENCY)?kpiMap.get(QMDLConstant.LATENCY):new TreeMap<>();
		TreeMap<Long,Object> jitter=kpiMap.containsKey(QMDLConstant.JITTER)?kpiMap.get(QMDLConstant.JITTER):new TreeMap<>();
		TreeMap<Long,Object> responseTime=kpiMap.containsKey(QMDLConstant.RESPONSE_TIME)?kpiMap.get(QMDLConstant.RESPONSE_TIME):new TreeMap<>();

		
		
		for(NVL3CsvDataWrapper csvDataHolder:csvDataHolderList){
			updatekpiMapFromData(ulDataMap,dlDataMap,latency,csvDataHolder,jitter,responseTime);
		}
		
		kpiMap.put(QMDLConstant.UL_THROUGHTPUT, ulDataMap);
		kpiMap.put(QMDLConstant.DL_THROUGHTPUT, dlDataMap);
		kpiMap.put(QMDLConstant.LATENCY, latency);
		kpiMap.put(QMDLConstant.JITTER,jitter);
		kpiMap.put(QMDLConstant.RESPONSE_TIME,responseTime);

	}

	/**
	 * Updatekpi map from data.
	 *
	 * @param ulDataMap the ul data map
	 * @param dlDataMap the dl data map
	 * @param latency the latency
	 * @param csvDataHolder the csv data holder
	 * @param responseTime 
	 * @param jitter 
	 */
	private void updatekpiMapFromData(TreeMap<Long, Object> ulDataMap, TreeMap<Long, Object> dlDataMap,
			TreeMap<Long, Object> latency, NVL3CsvDataWrapper csvDataHolder, TreeMap<Long, Object> jitter, TreeMap<Long, Object> responseTime) {
	if(csvDataHolder.getTimeStamp()!=null){
		if(csvDataHolder.getDlThroughPut()!=null){
			nvLayer3StatsGeneratorService.aggSignalParam(csvDataHolder.getTimeStamp(), csvDataHolder.getDlThroughPut(), dlDataMap);
		}
		if(csvDataHolder.getUlThroughPut()!=null){
			nvLayer3StatsGeneratorService.aggSignalParam(csvDataHolder.getTimeStamp(), csvDataHolder.getUlThroughPut(), ulDataMap);
		}
		if(csvDataHolder.getLatency()!=null){
			nvLayer3StatsGeneratorService.aggSignalParam(csvDataHolder.getTimeStamp(), csvDataHolder.getLatency(), latency);
		}
		if(csvDataHolder.getJitter()!=null){
			nvLayer3StatsGeneratorService.aggSignalParam(csvDataHolder.getTimeStamp(), csvDataHolder.getJitter(), jitter);
		}
		if(csvDataHolder.getResponseTime()!=null){
			nvLayer3StatsGeneratorService.aggSignalParam(csvDataHolder.getTimeStamp(), csvDataHolder.getResponseTime(), responseTime);
		}
	}
	}



	private void processRawData(List<NVL3CsvDataWrapper> csvDataHolderList, TreeMap<Long, QMDLLogCodeWrapper> fileDataMap, Layer3SummaryWrapper aggrigateWrapperData) {

		for(NVL3CsvDataWrapper wrapper:csvDataHolderList){
			if(wrapper.getTimeStamp()==null || wrapper.getLat()==null|| wrapper.getLon()==null){
				continue;
			}
			setValuesIntoAggrigatedWrapper(aggrigateWrapperData, wrapper);
			Long key = NVLayer3Utils.getKeyForBean(wrapper.getTimeStamp());
			QMDLLogCodeWrapper qmdlWrapper = NVLayer3Utils.searchNearestRecordWrapper(fileDataMap,
					NVLayer3Utils.getKeyForBean(key));
			if(qmdlWrapper!=null){
				aggregateCsvRecordData(wrapper,qmdlWrapper);
			}else{
				QMDLLogCodeWrapper qmdlData = aggregateCsvRecordData(wrapper,new QMDLLogCodeWrapper());
				qmdlData.setTimeStamp(wrapper.getTimeStamp());
				qmdlData.setLat(wrapper.getLat());
				qmdlData.setLon(wrapper.getLon());
				fileDataMap.put(key,qmdlData);
			}
		}
		
		logger.debug("getting aggrigated wrapper summary data{}",new Gson().toJson(aggrigateWrapperData));
	}

	private void setValuesIntoAggrigatedWrapper(Layer3SummaryWrapper aggrigateWrapperData, NVL3CsvDataWrapper wrapper) {
		NvCsvParsingCommonUtils.addRemarkAndTestSkipIntoAggrigateWrapper(aggrigateWrapperData, wrapper);
		addPingValuesToAggrigateWrapper(aggrigateWrapperData,wrapper);
		addMNCAndMCCValuesToAggrigateWrapper(aggrigateWrapperData,wrapper);
		addPmosValuesToAggrigateWrapper(aggrigateWrapperData,wrapper);

		
	}


	private void addPmosValuesToAggrigateWrapper(Layer3SummaryWrapper aggrigateWrapperData,
			NVL3CsvDataWrapper wrapper) {
		if(aggrigateWrapperData.getpMosFirstDetail()==null) {
		aggrigateWrapperData.setpMosFirstDetail(NVLayer3Utils.getLatestValue(null,wrapper.getpMos()));
		}
	}

	private void addMNCAndMCCValuesToAggrigateWrapper(Layer3SummaryWrapper aggrigateWrapperData,
			NVL3CsvDataWrapper wrapper) {
		aggrigateWrapperData.setMNC(NVLayer3Utils.getLatestValue(aggrigateWrapperData.getMNC(), wrapper.getMnc()));
		aggrigateWrapperData.setMCC(NVLayer3Utils.getLatestValue(aggrigateWrapperData.getMCC(), wrapper.getMcc()));		
	}

	private QMDLLogCodeWrapper aggregateCsvRecordData(NVL3CsvDataWrapper newWrapper, QMDLLogCodeWrapper oldData) {
		oldData.setLatency(NVLayer3Utils.getDoubleArrayValue(oldData.getLatency(), newWrapper.getLatency()));
		oldData.setDlThroughPut(NVLayer3Utils.getDoubleArrayValue(oldData.getDlThroughPut(),newWrapper.getDlThroughPut()));
		oldData.setUlThroughPut(NVLayer3Utils.getDoubleArrayValue(oldData.getUlThroughPut(), newWrapper.getUlThroughPut()));
		oldData.setAvgJitter(NVLayer3Utils.getDoubleArrayValue(oldData.getAvgJitter(),newWrapper.getJitter()));
		oldData.setPacketLoss(NVLayer3Utils.getDoubleArrayValue(oldData.getPacketLoss(),newWrapper.getPacketLoss()));
		oldData.setPingPacketLoss(NVLayer3Utils.getDoubleArrayValue(oldData.getPingPacketLoss(), newWrapper.getPingPacketLoss()));
		oldData.setAvgReponseTime(NVLayer3Utils.getDoubleArrayValue(oldData.getAvgReponseTime(), newWrapper.getResponseTime()));
		oldData.setCoverage(NvCsvParsingCommonUtils.getCoverageValue(newWrapper.getNetworkType(),oldData.getCoverage()));
		oldData.setCgi(newWrapper.getCi());
		oldData.setCellid(newWrapper.getCellId());
		oldData.seteNodebId(newWrapper.geteNodeBId());
		oldData.setSectorId(newWrapper.getCellId());
		
		oldData.setFtpDl(NVLayer3Utils.getDoubleArrayValue(oldData.getFtpDl(), newWrapper.getFtpDl()));
		oldData.setFtpUl(NVLayer3Utils.getDoubleArrayValue(oldData.getFtpUl(), newWrapper.getFtpUl()));
		oldData.setHttpDl(NVLayer3Utils.getDoubleArrayValue(oldData.getHttpDl(),newWrapper.getHttpDl()));
		oldData.setHttpUl(NVLayer3Utils.getDoubleArrayValue(oldData.getHttpUl(), newWrapper.getHttpUl()));
		
		oldData.setHttpAttempt(NVLayer3Utils.addIntegerValue(oldData.getHttpAttempt(), newWrapper.getHttpAttempt()));
		oldData.setHttpSucess(NVLayer3Utils.addIntegerValue(oldData.getHttpSucess(), newWrapper.getHttpSucess()));
		oldData.setHttpFailure(NVLayer3Utils.addIntegerValue(oldData.getHttpFailure(), newWrapper.getHttpFailure()));
		oldData.setHttpDrop(NVLayer3Utils.addIntegerValue(oldData.getHttpDrop(), newWrapper.getHttpDrop()));
		
		oldData.setHttpDownLoadTime(NVLayer3Utils.getDoubleArrayValue(oldData.getHttpDownLoadTime(), newWrapper.getHttpDownLoadTime()));
		
		oldData.setSmsAttempt(NVLayer3Utils.addIntegerValue(oldData.getSmsAttempt(), newWrapper.getSmsAttempt()));
		oldData.setSmsSucess(NVLayer3Utils.addIntegerValue(oldData.getSmsSucess(), newWrapper.getSmsSucess()));
		oldData.setSmsFailure(NVLayer3Utils.addIntegerValue(oldData.getSmsFailure(), newWrapper.getSmsFailure()));
		
		oldData.setPingBufferSize(newWrapper.getPingBufferSize());		

		if(newWrapper.getPingBufferSize()!=null) {
			if(newWrapper.getPingBufferSize().equals(QMDLConstant.THIRTY_TWO)) {
				oldData.setLatencyBufferSize32Bytes(NVLayer3Utils.getDoubleArrayValue(oldData.getLatencyBufferSize32Bytes(), newWrapper.getLatency()));
			
			}else if(newWrapper.getPingBufferSize().equals(QMDLConstant.THOUSAND)) {
				oldData.setLatencyBufferSize1000Bytes(NVLayer3Utils.getDoubleArrayValue(oldData.getLatencyBufferSize1000Bytes(), newWrapper.getLatency()));
			
			}else if(newWrapper.getPingBufferSize().equals(QMDLConstant.FIFTEEN_HUNDRED)) {
				oldData.setLatencyBufferSize1500Bytes(NVLayer3Utils.getDoubleArrayValue(oldData.getLatencyBufferSize1500Bytes(), newWrapper.getLatency()));
			}
		}
	
		//oldData.setCellid(newWrapper.getCi());
		oldData.setpMos(NVLayer3Utils.getDoubleArrayValue(oldData.getpMos(), newWrapper.getpMos()));
		
		
		oldData.setTestType(NvCsvParsingCommonUtils.getCoverageValue(newWrapper.getTestType(),oldData.getTestType()));
		logger.debug("Getting testType  {}",oldData.getTestType());
		return oldData;
	}


	private void addPingValuesToAggrigateWrapper(Layer3SummaryWrapper aggrigateWrapperData,
			NVL3CsvDataWrapper wrapper) {
		aggrigateWrapperData.setPingPacketLoss(NVLayer3Utils.getDoubleArrayValue(aggrigateWrapperData.getPingPacketLoss(), wrapper.getPingPacketLoss()));
	}



	/**
	 * Process csv file.
	 *
	 * @param csvFile the csv file
	 * @param csvDataHolderList the csv data holder list
	 * @param aggrigateWrapperData
	 * @param imagePutMap
	 * @param localFilePath 
	 * @return the buffered reader
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private List<NVL3CsvDataWrapper> processCsvFile(ZipFile zipFile,ZipEntry entry, List<NVL3CsvDataWrapper> csvDataHolderList, Layer3SummaryWrapper aggrigateWrapperData, Map<String, Put> imagePutMap, List<String> processedFiles, String localFilePath)
			throws IOException {
				processCsvFileFromZip(csvDataHolderList, zipFile, entry,aggrigateWrapperData,imagePutMap, processedFiles,localFilePath);
		return csvDataHolderList;
	}

	private void processCsvFileFromZip(List<NVL3CsvDataWrapper> csvDataHolderList, ZipFile zipFile,
			ZipEntry entry, Layer3SummaryWrapper aggrigateWrapperData, Map<String, Put> imagePutMap, List<String> processedFiles, String localFilePath) throws IOException {
		String row;
		InputStream stream = zipFile.getInputStream(entry);
		InputStreamReader isr = new InputStreamReader(stream);
		String csvFilePath = localFilePath + entry.getName();
		boolean isCsvCopiedToLocal = NVLayer3Utils.copyFileToLocalPath(csvFilePath, zipFile.getInputStream(entry));
		if(isCsvCopiedToLocal) {
			processedFiles.add(csvFilePath);
		}
		BufferedReader br = new BufferedReader(isr);
		try {
			if ((row = br.readLine()) != null) {
				NvCsvParsingCommonUtils.setHeaderValuesIntoWrapper(aggrigateWrapperData,row);
			}
			while ((row = br.readLine()) != null) {
				processCsvRow(csvDataHolderList, row,imagePutMap,aggrigateWrapperData);
			}
		} catch (IOException e) {
			throw new IOException("Error In Reading csv " + e.getMessage());
		}finally{
			stream.close();
		}
	}



	private void processCsvRow(List<NVL3CsvDataWrapper> csvDataHolderList, String row, Map<String, Put> imagePutMap, Layer3SummaryWrapper aggrigateWrapperData) {
		try{
			NVL3CsvDataWrapper csvWrapper = getCsvDataHolderForString(row,imagePutMap,aggrigateWrapperData);
			if (csvWrapper != null && csvWrapper.getTestType()!=null) {
				csvDataHolderList.add(csvWrapper);
			}
		}catch (Exception e) {
			logger.warn("Error in Processing line {}  Error {}", row,Utils.getStackTrace(e) );
		}
	}

	/**
	 * Gets the csv data holder for string.
	 *
	 * @param row the row
	 * @param imagePutMap
	 * @param aggrigateWrapperData
	 * @return the csv data holder for string
	 */
	public NVL3CsvDataWrapper getCsvDataHolderForString(String row, Map<String, Put> imagePutMap, Layer3SummaryWrapper aggrigateWrapperData) {
		if (row == null || row.trim().length() <=QMDLConstant.MINIMUM_LENTH_OF_CSV_LINE||row.split(Symbol.COMMA_STRING).length<QMDLConstant.MINIMUM_LENGTH_CSV_FIELD){
			logger.warn("Getting wrong row {}  ",row);
			return null;
		}

		String[] lineData = row.split(Symbol.COMMA_STRING,-1);
		NVL3CsvDataWrapper csvDataHolder =new NVL3CsvDataWrapper();
		NvCsvParsingCommonUtils.addNetworkTypeIntoAggrigateWrapper(aggrigateWrapperData,lineData);
		NvCsvParsingCommonUtils.setCommonsParameterOfCsv(lineData, csvDataHolder);
		NvCsvParsingCommonUtils.setCommonValueByNetworkType(lineData, csvDataHolder);
		int networkType = getStringTypeByNetworkAndTest(lineData[NetworkDataFormats.TESTTYPE_INDEX],lineData[NetworkDataFormats.NETWORK_TYPE_INDEX]);

		switch (networkType) {
		case NetworkDataFormats.DOWNLOAD_4G_ID:
			NvCsvParsingCommonUtils.setLTEDLParameterOfCsv(lineData, csvDataHolder);
			break;
		case NetworkDataFormats.UPLOAD_4G_ID:
			NvCsvParsingCommonUtils.setLTEULParameterOfCsv(lineData, csvDataHolder);
			break;
		case NetworkDataFormats.BROWSE_4G_ID:
			NvCsvParsingCommonUtils.setLTEBROWSEParameterOfCsv(lineData, csvDataHolder);
			break;
		case NetworkDataFormats.PING_4G_ID:
			NvCsvParsingCommonUtils.setLTEPingParameterOfCsv(lineData, csvDataHolder);
			break;
		case NetworkDataFormats.JITTER_4G_ID:
			NvCsvParsingCommonUtils.setLTEJitterParameterOfCsv(lineData, csvDataHolder);
			break;
		case NetworkDataFormats.QUICK_TEST_4G_ID:
			NvCsvParsingCommonUtils.addQuickTestParameterOfCsv(lineData,csvDataHolder);
			NvCsvParsingCommonUtils.addImageInformationFor4GIntoPut(lineData,imagePutMap,aggrigateWrapperData);
			break;
		case NetworkDataFormats.YOUTUBE_TEST_4G_ID:
			NvCsvParsingCommonUtils.addYouTubeTestParameterOfCsv(lineData,csvDataHolder);
			NvCsvParsingCommonUtils.addYoutubeInformationFor4GIntoPut(lineData,aggrigateWrapperData);
			break;
		case NetworkDataFormats.SMS_TEST_4G_ID:
			NvCsvParsingCommonUtils.setSMSInformationIntoCsvWrapper(lineData,csvDataHolder);
			NvCsvParsingCommonUtils.addSMSInformationFor4GIntoAggrigateWrapper(lineData,aggrigateWrapperData);
			break;
		case NetworkDataFormats.HTTP_LINK_DOWNLOAD_4G_ID:
			NvCsvParsingCommonUtils.setLTEHTTPDLParameterOfCsv(lineData, csvDataHolder);
			NvCsvParsingCommonUtils.addHTTPLinkInformationFor4GIntoAggrigateWrapper(lineData,aggrigateWrapperData);
			break;
		case NetworkDataFormats.CALL_TEST_4G_ID:
			NvCsvParsingCommonUtils.setCALLInformationIntoCsvWrapper(lineData,csvDataHolder);
			NvCsvParsingCommonUtils.addCALLInformationFor4GIntoAggrigateWrapper(lineData,aggrigateWrapperData);
			break;
		case NetworkDataFormats.IDLE_4G_ID:
			NvCsvParsingCommonUtils.setIDLEInformationIntoCsvWrapper(lineData,csvDataHolder);
			NvCsvParsingCommonUtils.addHandoverCauseInformationFor4G(lineData,aggrigateWrapperData);
			break;
		case NetworkDataFormats.ATTACH_DETACH_TEST_4G_ID:
			NvCsvParsingCommonUtils.setAttachDetachInformationIntoCsvWrapper(lineData,csvDataHolder);
			break;



		case NetworkDataFormats.DOWNLOAD_3G_ID:
			NvCsvParsingCommonUtils.set3GDLParameterOfCsv(lineData, csvDataHolder);
			break;
		case NetworkDataFormats.UPLOAD_3G_ID:
			NvCsvParsingCommonUtils.set3GULParameterOfCsv(lineData, csvDataHolder);
			break;
		case NetworkDataFormats.BROWSE_3G_ID:
			NvCsvParsingCommonUtils.set3GBROWSEParameterOfCsv(lineData, csvDataHolder);
			break;
		case NetworkDataFormats.PING_3G_ID:
			NvCsvParsingCommonUtils.set3GPingParameterOfCsv(lineData, csvDataHolder);
			break;
		case NetworkDataFormats.JITTER_3G_ID:
			NvCsvParsingCommonUtils.set3GJitterParameterOfCsv(lineData, csvDataHolder);
			break;			
		case NetworkDataFormats.QUICK_TEST_3G_ID:
			NvCsvParsingCommonUtils.add3GQuickTestParameterOfCsv(lineData,csvDataHolder);
			NvCsvParsingCommonUtils.addImageInformationFor3GIntoPut(lineData,imagePutMap,aggrigateWrapperData);
			break;
		case NetworkDataFormats.YOUTUBE_TEST_3G_ID:
			NvCsvParsingCommonUtils.add3GYouTubeTestParameterOfCsv(lineData,csvDataHolder);
			NvCsvParsingCommonUtils.addYoutubeInformationFor3GIntoPut(lineData,aggrigateWrapperData);
			break;				
		case NetworkDataFormats.SMS_TEST_3G_ID:
			NvCsvParsingCommonUtils.set3GSMSInformationIntoCsvWrapper(lineData,csvDataHolder);
			NvCsvParsingCommonUtils.addSMSInformationFor3GIntoAggrigateWrapper(lineData,aggrigateWrapperData);
			break;					
		case NetworkDataFormats.HTTP_LINK_DOWNLOAD_3G_ID:
			NvCsvParsingCommonUtils.set3GHTTPDLParameterOfCsv(lineData, csvDataHolder);
			NvCsvParsingCommonUtils.addHTTPLinkInformationFor3GIntoAggrigateWrapper(lineData,aggrigateWrapperData);
			break;			
		case NetworkDataFormats.CALL_TEST_3G_ID:
			NvCsvParsingCommonUtils.set3GCALLInformationIntoCsvWrapper(lineData,csvDataHolder);
			NvCsvParsingCommonUtils.addCALLInformationFor3GIntoAggrigateWrapper(lineData,aggrigateWrapperData);
			break;		
		case NetworkDataFormats.IDLE_3G_ID:
			NvCsvParsingCommonUtils.set3GIDLEInformationIntoCsvWrapper(lineData,csvDataHolder);
			//NvCsvParsingCommonUtils.addHandoverCauseInformationFor4G(lineData,aggrigateWrapperData);
			break;
		case NetworkDataFormats.ATTACH_DETACH_TEST_3G_ID:
			NvCsvParsingCommonUtils.set3GAttachDetachInformationIntoCsvWrapper(lineData,csvDataHolder);
			break;		



		case NetworkDataFormats.DOWNLOAD_2G_ID:
			NvCsvParsingCommonUtils.set2GDLParameterOfCsv(lineData, csvDataHolder);
			break;
		case NetworkDataFormats.UPLOAD_2G_ID:
			NvCsvParsingCommonUtils.set2GULParameterOfCsv(lineData, csvDataHolder);
			break;

		case NetworkDataFormats.BROWSE_2G_ID:
			NvCsvParsingCommonUtils.set2GBROWSEParameterOfCsv(lineData, csvDataHolder);
			break;
		case NetworkDataFormats.PING_2G_ID:
			NvCsvParsingCommonUtils.set2GPingParameterOfCsv(lineData, csvDataHolder);
			break;
		case NetworkDataFormats.JITTER_2G_ID:
			NvCsvParsingCommonUtils.set2GJitterParameterOfCsv(lineData, csvDataHolder);
			break;			
		case NetworkDataFormats.QUICK_TEST_2G_ID:
			NvCsvParsingCommonUtils.add2GQuickTestParameterOfCsv(lineData,csvDataHolder);
			NvCsvParsingCommonUtils.addImageInformationFor3GIntoPut(lineData,imagePutMap,aggrigateWrapperData);
			break;
		case NetworkDataFormats.YOUTUBE_TEST_2G_ID:
			NvCsvParsingCommonUtils.add2GYouTubeTestParameterOfCsv(lineData,csvDataHolder);
			NvCsvParsingCommonUtils.addYoutubeInformationFor2GIntoPut(lineData,aggrigateWrapperData);
			break;				
		case NetworkDataFormats.SMS_TEST_2G_ID:
			NvCsvParsingCommonUtils.set2GSMSInformationIntoCsvWrapper(lineData,csvDataHolder);
			NvCsvParsingCommonUtils.addSMSInformationFor2GIntoAggrigateWrapper(lineData,aggrigateWrapperData);
			break;						
		case NetworkDataFormats.HTTP_LINK_DOWNLOAD_2G_ID:
			NvCsvParsingCommonUtils.set2GHTTPDLParameterOfCsv(lineData, csvDataHolder);
			NvCsvParsingCommonUtils.addHTTPLinkInformationFor2GIntoAggrigateWrapper(lineData,aggrigateWrapperData);
			break;			
		case NetworkDataFormats.CALL_TEST_2G_ID:
			NvCsvParsingCommonUtils.set2GCALLInformationIntoCsvWrapper(lineData,csvDataHolder);
			NvCsvParsingCommonUtils.addCALLInformationFor2GIntoAggrigateWrapper(lineData,aggrigateWrapperData);
			break;		
		case NetworkDataFormats.IDLE_2G_ID:
			NvCsvParsingCommonUtils.set2GIDLEInformationIntoCsvWrapper(lineData,csvDataHolder);
			//NvCsvParsingCommonUtils.addHandoverCauseInformationFor4G(lineData,aggrigateWrapperData);
			break;
		case NetworkDataFormats.ATTACH_DETACH_TEST_2G_ID:
			NvCsvParsingCommonUtils.set2GAttachDetachInformationIntoCsvWrapper(lineData,csvDataHolder);
			break;



		case NetworkDataFormats.NETWORK_TYPE_NONE_ID:
			NvCsvParsingCommonUtils.setAttemptParameterforPacketDLTest(lineData, csvDataHolder);
			break;


		default:
			logger.debug("invaild network type {} {}", csvDataHolder.getNetworkType(),networkType);
		}
		return csvDataHolder;
	}


	/**
	 * Gets the string type by network and test.
	 *
	 * @param testType  Ping,Download,Upload
	 * @param networkType 3G,4G,2G
	 * @return String format Type contant
	 */
	@Override
	public int getStringTypeByNetworkAndTest(String testType, String networkType) {
		if (testType != null && networkType != null) {
			networkType = networkType.toUpperCase();
			testType = testType.toUpperCase();

			switch (networkType) {

			case QMDLConstant.NETWORK_TYPE_4G:
				return lteStringTypeIdForCsv(testType);

			case QMDLConstant.NETWORK_TYPE_3G:
				return get3GTypeIdForCsv(testType);

			case QMDLConstant.NETWORK_TYPE_2G:
				return get2GStringTypeIdForCsv(testType);
			default:
				if (isValidRecordForNone(testType, networkType)) {
					return NetworkDataFormats.NETWORK_TYPE_NONE_ID;
				}
			}
		}
		return QMDLConstant.NETWORK_TYPE_UNKNOWN_ID;
	}

	private boolean isValidRecordForNone(String testType, String networkType) {
		return (
		 NetworkDataFormats.TEST_TYPE_FTP_DOWNLOAD.equals(testType)
				|| NetworkDataFormats.TEST_TYPE_HTTP_DOWNLOAD.equals(testType)
				|| NetworkDataFormats.HTTP_LINK_DOWNLOAD.equals(testType)
				|| NetworkDataFormats.TEST_TYPE_FTP_UPLOAD.equalsIgnoreCase(testType)
				|| NetworkDataFormats.TEST_TYPE_HTTP_UPLOAD.equalsIgnoreCase(testType)
				|| NetworkDataFormats.HTTP_LINK_UPLOAD.equalsIgnoreCase(testType)
				|| NetworkDataFormats.TEST_TYPE_PING.equalsIgnoreCase(testType)
				|| NetworkDataFormats.TEST_TYPE_BROWSE.equalsIgnoreCase(testType)
				|| NetworkDataFormats.TEST_TYPE_IDLE.equalsIgnoreCase(testType)
				|| NetworkDataFormats.QUICK_TEST.equalsIgnoreCase(testType)
				|| NetworkDataFormats.JITTER.equalsIgnoreCase(testType)
				|| NetworkDataFormats.TEST_TYPE_HTTP_PACKET_DOWNLOAD.equalsIgnoreCase(testType)
				|| NetworkDataFormats.TEST_TYPE_FTP_PACKET_DOWNLOAD.equalsIgnoreCase(testType))
				&& NetworkDataFormats.NETWORK_TYPE_NONE.equalsIgnoreCase(networkType);
	}

	/**
	 * Process 2G type of String.
	 *
	 * @param testType  Ping,Download,Upload
	 * @return format String constant
	 */
	private int get2GStringTypeIdForCsv(String testType) {
		if (isValidRecordForDLthroughPut(testType)) {
			return NetworkDataFormats.DOWNLOAD_2G_ID;
		} else if (isValidRecordForULThroughPut(testType)) {
			return NetworkDataFormats.UPLOAD_2G_ID;
		} else if (NetworkDataFormats.TEST_TYPE_PING.equalsIgnoreCase(testType)) {
			return NetworkDataFormats.PING_2G_ID;
		}else if(NetworkDataFormats.TEST_TYPE_BROWSE.equalsIgnoreCase(testType)){
			return NetworkDataFormats.BROWSE_2G_ID;
		}else if(NetworkDataFormats.TEST_TYPE_IDLE.equalsIgnoreCase(testType) ){
			return NetworkDataFormats.IDLE_2G_ID;
		}else if(NetworkDataFormats.QUICK_TEST.equalsIgnoreCase(testType)){
			return NetworkDataFormats.QUICK_TEST_2G_ID;
		}else if(NetworkDataFormats.JITTER.equalsIgnoreCase(testType)){
			return NetworkDataFormats.JITTER_2G_ID;
		}
		
		else if(NetworkDataFormats.HTTP_LINK_DOWNLOAD.equalsIgnoreCase(testType)){
			return NetworkDataFormats.HTTP_LINK_DOWNLOAD_2G_ID;
		}else if(NetworkDataFormats.SMS_TEST.equalsIgnoreCase(testType)){
			return NetworkDataFormats.SMS_TEST_2G_ID;
		}else if(NetworkDataFormats.YOUTUBE_TEST.equalsIgnoreCase(testType)){
			return NetworkDataFormats.YOUTUBE_TEST_2G_ID;
		}else if(NetworkDataFormats.ATTACH_DETACH_TEST.equalsIgnoreCase(testType)){
			return NetworkDataFormats.ATTACH_DETACH_TEST_2G_ID;
		}else if(NetworkDataFormats.ATTACH_TEST.equalsIgnoreCase(testType)){
			return NetworkDataFormats.ATTACH_DETACH_TEST_2G_ID;
		}else if(NetworkDataFormats.DETACH_TEST.equalsIgnoreCase(testType)){
			return NetworkDataFormats.ATTACH_DETACH_TEST_2G_ID;
		}else if(NetworkDataFormats.SHORT_CALL_TEST.equalsIgnoreCase(testType)||NetworkDataFormats.LONG_CALL_TEST.equalsIgnoreCase(testType)){
			return NetworkDataFormats.CALL_TEST_2G_ID;
		}
		return QMDLConstant.NETWORK_TYPE_UNKNOWN_ID;
	}

	/**
	 * Process 3G type of String.
	 *
	 * @param testType  Ping,Download,Upload
	 * @return format String constant
	 */
	private int get3GTypeIdForCsv(String testType) {
		if (isValidRecordForDLthroughPut(testType)) {
			return NetworkDataFormats.DOWNLOAD_3G_ID;
		} else if (isValidRecordForULThroughPut(testType)) {
			return NetworkDataFormats.UPLOAD_3G_ID;
		} else if (NetworkDataFormats.TEST_TYPE_PING.equalsIgnoreCase(testType)) {
			return NetworkDataFormats.PING_3G_ID;
		}else if(NetworkDataFormats.TEST_TYPE_BROWSE.equalsIgnoreCase(testType)){
			return NetworkDataFormats.BROWSE_3G_ID;
		}else if(NetworkDataFormats.TEST_TYPE_IDLE.equalsIgnoreCase(testType) ){
			return NetworkDataFormats.IDLE_3G_ID;
		}else if(NetworkDataFormats.QUICK_TEST.equalsIgnoreCase(testType)){
			return NetworkDataFormats.QUICK_TEST_3G_ID;
		}else if(NetworkDataFormats.JITTER.equalsIgnoreCase(testType)){
			return NetworkDataFormats.JITTER_3G_ID;
	
		
		}else if(NetworkDataFormats.HTTP_LINK_DOWNLOAD.equalsIgnoreCase(testType)){
			return NetworkDataFormats.HTTP_LINK_DOWNLOAD_3G_ID;
		}else if(NetworkDataFormats.SMS_TEST.equalsIgnoreCase(testType)){
			return NetworkDataFormats.SMS_TEST_3G_ID;
		}else if(NetworkDataFormats.YOUTUBE_TEST.equalsIgnoreCase(testType)){
			return NetworkDataFormats.YOUTUBE_TEST_3G_ID;
		}else if(NetworkDataFormats.ATTACH_DETACH_TEST.equalsIgnoreCase(testType)){
			return NetworkDataFormats.ATTACH_DETACH_TEST_3G_ID;
		}else if(NetworkDataFormats.ATTACH_TEST.equalsIgnoreCase(testType)){
			return NetworkDataFormats.ATTACH_DETACH_TEST_3G_ID;
		}else if(NetworkDataFormats.DETACH_TEST.equalsIgnoreCase(testType)){
			return NetworkDataFormats.ATTACH_DETACH_TEST_3G_ID;
		}else if(NetworkDataFormats.SHORT_CALL_TEST.equalsIgnoreCase(testType)||NetworkDataFormats.LONG_CALL_TEST.equalsIgnoreCase(testType)){
			return NetworkDataFormats.CALL_TEST_3G_ID;
		}

		return NetworkDataFormats.NETWORK_TYPE_UNKNOWN_ID;
	}

	/**
	 * Process 4G type of String.
	 *
	 * @param testType  Ping,Download,Upload
	 * @return format String constant
	 */
	private Integer lteStringTypeIdForCsv(String testType) {
		if (isValidRecordForDLthroughPut(testType)) {
			return NetworkDataFormats.DOWNLOAD_4G_ID;
		} else if (isValidRecordForULThroughPut(testType)) {
			return NetworkDataFormats.UPLOAD_4G_ID;
		} else if (NetworkDataFormats.TEST_TYPE_PING.equalsIgnoreCase(testType)) {
			return NetworkDataFormats.PING_4G_ID;
		}else if(NetworkDataFormats.TEST_TYPE_BROWSE.equalsIgnoreCase(testType)){
			return NetworkDataFormats.BROWSE_4G_ID;
		}else if(NetworkDataFormats.TEST_TYPE_IDLE.equalsIgnoreCase(testType) ){
			return NetworkDataFormats.IDLE_4G_ID;
		}else if(NetworkDataFormats.QUICK_TEST.equalsIgnoreCase(testType)){
			return NetworkDataFormats.QUICK_TEST_4G_ID;
		}else if(NetworkDataFormats.JITTER.equalsIgnoreCase(testType)){
			return NetworkDataFormats.JITTER_4G_ID;
		}else if(NetworkDataFormats.YOUTUBE_TEST.equalsIgnoreCase(testType)){
			return NetworkDataFormats.YOUTUBE_TEST_4G_ID;
		}else if(NetworkDataFormats.SMS_TEST.equalsIgnoreCase(testType)){
			return NetworkDataFormats.SMS_TEST_4G_ID;
		}else if(NetworkDataFormats.HTTP_LINK_DOWNLOAD.equalsIgnoreCase(testType)){
			return NetworkDataFormats.HTTP_LINK_DOWNLOAD_4G_ID;
		}else if(NetworkDataFormats.SHORT_CALL_TEST.equalsIgnoreCase(testType)||NetworkDataFormats.LONG_CALL_TEST.equalsIgnoreCase(testType)){
			return NetworkDataFormats.CALL_TEST_4G_ID;
		}else if(NetworkDataFormats.ATTACH_DETACH_TEST.equalsIgnoreCase(testType)){
			return NetworkDataFormats.ATTACH_DETACH_TEST_4G_ID;
		}else if(NetworkDataFormats.ATTACH_TEST.equalsIgnoreCase(testType)){
			return NetworkDataFormats.ATTACH_DETACH_TEST_4G_ID;
		}else if(NetworkDataFormats.DETACH_TEST.equalsIgnoreCase(testType)){
			return NetworkDataFormats.ATTACH_DETACH_TEST_4G_ID;
		}
		


		return NetworkDataFormats.NETWORK_TYPE_UNKNOWN_ID;
	}

	/**
	 * Checks if is valid record for UL through put.
	 *
	 * @param testType the test type
	 * @return  is valid for UL throughput
	 */
	private boolean isValidRecordForULThroughPut(String testType) {
		return NetworkDataFormats.TEST_TYPE_FTP_UPLOAD.equalsIgnoreCase(testType) ||
				NetworkDataFormats.TEST_TYPE_HTTP_UPLOAD.equalsIgnoreCase(testType)||
				NetworkDataFormats.HTTP_LINK_UPLOAD.equalsIgnoreCase(testType);
	}

	/**
	 * Checks if is valid record for D lthrough put.
	 *
	 * @param testType the test type
	 * @return  is valid for DL throughput
	 */
	private boolean isValidRecordForDLthroughPut(String testType) {
		return  NetworkDataFormats.TEST_TYPE_FTP_DOWNLOAD.equals(testType) || 
				NetworkDataFormats.TEST_TYPE_HTTP_DOWNLOAD.equals(testType)||
				NetworkDataFormats.TEST_TYPE_HTTP_PACKET_DOWNLOAD.equals(testType)||
				NetworkDataFormats.TEST_TYPE_FTP_PACKET_DOWNLOAD.equals(testType);
		
	}

	
	

}

package com.inn.foresight.module.nv.layer3.service.parse.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
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

import com.inn.commons.Symbol;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.NVConfigUtil;
import com.inn.foresight.module.nv.layer3.constants.QMDLConstant;
import com.inn.foresight.module.nv.layer3.qmdlParser.wrappers.Layer3SummaryWrapper;
import com.inn.foresight.module.nv.layer3.qmdlParser.wrappers.NVL3CsvDataWrapper;
import com.inn.foresight.module.nv.layer3.qmdlParser.wrappers.QMDLLogCodeWrapper;
import com.inn.foresight.module.nv.layer3.service.parse.INVCsvProcessService;
import com.inn.foresight.module.nv.layer3.service.parse.INVL3CsvProcessService;
import com.inn.foresight.module.nv.layer3.service.parse.INVLayer3StatsService;
import com.inn.foresight.module.nv.layer3.utils.NVLayer3Utils;
import com.inn.foresight.module.nv.layer3.utils.NetworkDataFormats;
import com.inn.foresight.module.nv.layer3.utils.NvCsvParsingCommonUtils;


/** The Class NVCsvProcessServiceImpl. */
@Service("NVCsvProcessServiceImpl")
public class NVCsvProcessServiceImpl implements INVCsvProcessService {

	/** The logger. */
	private static Logger logger = LogManager.getLogger(NVCsvProcessServiceImpl.class);



	/** The nv layer 3 stats generator service. */
	@Autowired
	private INVLayer3StatsService nvLayer3StatsGeneratorService;

	@Autowired
	private INVL3CsvProcessService nvL3csvService;


	@Override
	public void processNonQMDLDataFromCsv(ZipFile zipFile,ZipEntry entry,TreeMap<Long, QMDLLogCodeWrapper> woParsedMap, Map<String, TreeMap<Long, Object>> kpiValuesMap,Layer3SummaryWrapper aggrigateWrapperData,Map<String, Put> imagePutMap,List<String> processedFiles,String localFilePath) throws IOException {

		List<NVL3CsvDataWrapper> csvDataHolderList=	processCsvFileFromZip(zipFile,entry,aggrigateWrapperData,imagePutMap,processedFiles,localFilePath);
		processCsvDataForLayer3(csvDataHolderList,woParsedMap,kpiValuesMap,aggrigateWrapperData);
	}


	/**
	 * Process csv data for layer 3.
	 *
	 * @param csvDataHolderList the csv data holder list
	 * @param rowPrefix the row prefix
	 * @param woParsedMap the wo parsed map
	 * @param kpiValuesMap the kpi values map
	 * @param aggrigateWrapperData 
	 * @throws RestException the rest exception
	 */
	@Override
	public void processCsvDataForLayer3(List<NVL3CsvDataWrapper> csvDataHolderList,TreeMap<Long, QMDLLogCodeWrapper> woParsedMap, Map<String, TreeMap<Long, Object>> kpiValuesMap, Layer3SummaryWrapper aggrigateWrapperData) {
		processRawData(csvDataHolderList,woParsedMap,aggrigateWrapperData);
		//processDataForStats(csvDataHolderList,kpiValuesMap);
	}


	/**
	 * Process raw data.
	 *
	 * @param csvDataHolderList the csv data holder list
	 * @param fileDataMap the file data map
	 * @param aggrigateWrapperData 
	 */

	private void processRawData(List<NVL3CsvDataWrapper> csvDataHolderList, TreeMap<Long, QMDLLogCodeWrapper> fileDataMap, Layer3SummaryWrapper aggrigateWrapperData) {

		for(NVL3CsvDataWrapper wrapper:csvDataHolderList){
			if(wrapper.getTimeStamp()==null || wrapper.getLat()==null|| wrapper.getLon()==null){
				continue;
			}
			Long key = NVLayer3Utils.getKeyForBean(wrapper.getTimeStamp());
			setValuesIntoAggrigatedWrapper(aggrigateWrapperData, wrapper);
			
			if(fileDataMap.containsKey(key)){
				aggregateCsvRecordData(wrapper,fileDataMap.get(key));
			}else{
				QMDLLogCodeWrapper qmdlData = aggregateCsvRecordData(wrapper,new QMDLLogCodeWrapper());
				qmdlData.setTimeStamp(wrapper.getTimeStamp());
				qmdlData.setEtimeStamp(wrapper.getTimeStamp());
				qmdlData.setLat(wrapper.getLat());
				qmdlData.setLon(wrapper.getLon());
				fileDataMap.put(key,qmdlData);
			}
		}
	}

	private void setValuesIntoAggrigatedWrapper(Layer3SummaryWrapper aggrigateWrapperData, NVL3CsvDataWrapper wrapper) {
		NvCsvParsingCommonUtils.addRemarkAndTestSkipIntoAggrigateWrapper(aggrigateWrapperData, wrapper);
		addPingValuesToAggrigateWrapper(aggrigateWrapperData,wrapper);
		addPmosValuesToAggrigateWrapper(aggrigateWrapperData,wrapper);
	}
	

	private void addPmosValuesToAggrigateWrapper(Layer3SummaryWrapper aggrigateWrapperData,
			NVL3CsvDataWrapper wrapper) {
		if(aggrigateWrapperData.getpMosFirstDetail()==null) {
		aggrigateWrapperData.setpMosFirstDetail(NVLayer3Utils.getLatestValue(null,wrapper.getpMos()));
		}
	}
	
	private void addPingValuesToAggrigateWrapper(Layer3SummaryWrapper aggrigateWrapperData,
			NVL3CsvDataWrapper wrapper) {
		aggrigateWrapperData.setPingPacketLoss(NVLayer3Utils.getDoubleArrayValue(aggrigateWrapperData.getPingPacketLoss(), wrapper.getPingPacketLoss()));
		aggrigateWrapperData.setDlThroughPutAdhoc(NVLayer3Utils.getDoubleArrayValue(aggrigateWrapperData.getDlThroughPutAdhoc(), wrapper.getDlThroughPut()));
		aggrigateWrapperData.setUlThroughPutAdhoc(NVLayer3Utils.getDoubleArrayValue(aggrigateWrapperData.getUlThroughPutAdhoc(), wrapper.getUlThroughPut()));
		aggrigateWrapperData.setLatencyAdhoc(NVLayer3Utils.getDoubleArrayValue(aggrigateWrapperData.getLatencyAdhoc(),wrapper.getLatency()));
		aggrigateWrapperData.setWptDns(NVLayer3Utils.getDoubleArrayValue(aggrigateWrapperData.getWptDns(),wrapper.getWptDns()));
		aggrigateWrapperData.setWptUrl(NVLayer3Utils.getDoubleArrayValue(aggrigateWrapperData.getWptUrl(),wrapper.getWptUrl() ));	
		setBufferPercentageForAdhocReport(wrapper, aggrigateWrapperData);
		aggrigateWrapperData.setYoutubeThroughtPut(NVLayer3Utils.getDoubleArrayValue(aggrigateWrapperData.getYoutubeThroughtPut(),wrapper.getYoutubeThroughPut()));
		
	}

	
	private static void setBufferPercentageForAdhocReport(NVL3CsvDataWrapper csvDataHolder,
			Layer3SummaryWrapper aggrigateWrapperData) {
		if(csvDataHolder.getTotalBufferTime()!=null && csvDataHolder.getVideoDuration()!=null && csvDataHolder.getVideoDuration()>0){
			try {
				aggrigateWrapperData.setYoutubeBufferTime(NVLayer3Utils.roundOffDouble(QMDLConstant.DECIMAL_PATTERN_2DIGIT, (csvDataHolder.getTotalBufferTime()/csvDataHolder.getVideoDuration())));
			} catch (ParseException e) {
				logger.error("Error in getting buffer time ratio {}",Utils.getStackTrace(e));
			}
		}
	}

	/**
	 * Aggregate csv record data.
	 *
	 * @param newWrapper the new wrapper
	 * @param oldData the old data
	 * @return the QMDL log code wrapper
	 */
	private QMDLLogCodeWrapper aggregateCsvRecordData(NVL3CsvDataWrapper newWrapper, QMDLLogCodeWrapper oldData) {
		oldData.setLatency(NVLayer3Utils.getDoubleArrayValue(oldData.getLatency(), newWrapper.getLatency()));
		oldData.setDlThroughPut(NVLayer3Utils.getDoubleArrayValue(oldData.getDlThroughPut(),newWrapper.getDlThroughPut()));
		oldData.setUlThroughPut(NVLayer3Utils.getDoubleArrayValue(oldData.getUlThroughPut(),newWrapper.getUlThroughPut()));
		oldData.setFtpDl(NVLayer3Utils.getDoubleArrayValue(oldData.getFtpDl(),newWrapper.getFtpDl()));
		oldData.setFtpUl(NVLayer3Utils.getDoubleArrayValue(oldData.getFtpUl(), newWrapper.getFtpUl()));
		oldData.setHttpDl(NVLayer3Utils.getDoubleArrayValue(oldData.getHttpDl(), newWrapper.getHttpDl()));
		oldData.setHttpUl(NVLayer3Utils.getDoubleArrayValue(oldData.getHttpUl(), newWrapper.getHttpUl()));
		
		oldData.setsINRData(NVLayer3Utils.getDoubleArrayValue(oldData.getsINRData(),newWrapper.getSinr()));
		oldData.setMeasureRSRQData(NVLayer3Utils.getDoubleArrayValue(oldData.getMeasureRSRQData(), newWrapper.getRsrq()));
		oldData.setMeasureRSRPData(NVLayer3Utils.getDoubleArrayValue(oldData.getMeasureRSRPData(), newWrapper.getRsrp()));
		oldData.setrSSIData(NVLayer3Utils.getDoubleArrayValue(oldData.getrSSIData(), newWrapper.getRssi()));
		oldData.setAvgJitter(NVLayer3Utils.getDoubleArrayValue(oldData.getAvgJitter(), newWrapper.getJitter()));
		oldData.setAvgReponseTime(NVLayer3Utils.getDoubleArrayValue(oldData.getAvgReponseTime(),newWrapper.getResponseTime()));

		oldData.setPacketLoss(NVLayer3Utils.getDoubleArrayValue(oldData.getPacketLoss(), newWrapper.getPacketLoss()));
		oldData.setCoverage(NvCsvParsingCommonUtils.getCoverageValue(newWrapper.getNetworkType(),oldData.getCoverage()));
		oldData.setEtimeStamp(newWrapper.getTimeStamp());
		setNetworkInformationIntoQMDLWrapper(newWrapper, oldData);
		oldData.setTestType(NvCsvParsingCommonUtils.getCoverageValue(newWrapper.getTestType(),oldData.getTestType()));
		setHttpInformationIntoQMDLWrapper(newWrapper, oldData);
		setSmsInformationIntoQMDLWrapper(newWrapper, oldData);
		oldData.setpMos(NVLayer3Utils.getDoubleArrayValue(oldData.getpMos(), newWrapper.getpMos()));
		
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
		

		oldData.setTestType(NvCsvParsingCommonUtils.getCoverageValue(newWrapper.getTestType(),oldData.getTestType()));
		logger.debug("Getting testType  {}",oldData.getTestType());
		return oldData;	
		
	}


	private void setSmsInformationIntoQMDLWrapper(NVL3CsvDataWrapper newWrapper, QMDLLogCodeWrapper oldData) {
		oldData.setSmsAttempt(NVLayer3Utils.addIntegerValue(oldData.getSmsAttempt(),newWrapper.getSmsAttempt()));
		oldData.setSmsFailure(NVLayer3Utils.addIntegerValue(oldData.getSmsFailure(),newWrapper.getSmsFailure()));
		oldData.setSmsSucess(NVLayer3Utils.addIntegerValue(oldData.getSmsSucess(),newWrapper.getSmsSucess()));
	}


	private void setHttpInformationIntoQMDLWrapper(NVL3CsvDataWrapper newWrapper, QMDLLogCodeWrapper oldData) {
		oldData.setHttpAttempt(NVLayer3Utils.addIntegerValue(oldData.getHttpAttempt(), newWrapper.getHttpAttempt()));
		oldData.setHttpSucess(NVLayer3Utils.addIntegerValue(oldData.getHttpSucess(), newWrapper.getHttpSucess()));
		oldData.setHttpFailure(NVLayer3Utils.addIntegerValue(oldData.getHttpFailure(), newWrapper.getHttpFailure()));
		oldData.setHttpDownLoadTime(NVLayer3Utils.getDoubleArrayValue(oldData.getHttpDownLoadTime(), newWrapper.getHttpDownLoadTime()));
		
	}


	private void setNetworkInformationIntoQMDLWrapper(NVL3CsvDataWrapper newWrapper, QMDLLogCodeWrapper oldData) {
		oldData.setPci(newWrapper.getPci());
		oldData.setMnc(newWrapper.getMnc());
		oldData.setMcc(newWrapper.getMcc());
		oldData.setBand(NVLayer3Utils.getStringFromJson(newWrapper.getBand()));
		oldData.setCgi(newWrapper.getCi());
		oldData.setCellid(newWrapper.getCellId());
		oldData.seteNodebId(newWrapper.geteNodeBId());
		oldData.setSectorId(newWrapper.getCellId());
		oldData.setEarfcn(newWrapper.getEarfcn());
		if(newWrapper.getEarfcn()!=null) {
			oldData.setDlEarfcn(newWrapper.getEarfcn().longValue());
		}
		oldData.setUlEarfcn(newWrapper.getUlEarfcn());
	}




	/**
	 * Process data for stats.
	 *
	 * @param csvDataHolderList the csv data holder list
	 * @param kpiMap the kpi map
	 * @throws RestException the rest exception
	 */
	@Override
	public void processDataForStats(List<NVL3CsvDataWrapper> csvDataHolderList, Map<String, TreeMap<Long, Object>> kpiMap) {
		TreeMap<Long,Object> ulDataMap=kpiMap.get(QMDLConstant.UL_THROUGHTPUT)!=null?kpiMap.get(QMDLConstant.UL_THROUGHTPUT):new TreeMap<>();
		TreeMap<Long,Object> dlDataMap=kpiMap.get(QMDLConstant.DL_THROUGHTPUT)!=null?kpiMap.get(QMDLConstant.DL_THROUGHTPUT):new TreeMap<>();
		TreeMap<Long,Object> latency=kpiMap.get(QMDLConstant.LATENCY)!=null?kpiMap.get(QMDLConstant.LATENCY):new TreeMap<>();
		TreeMap<Long,Object> sinr=kpiMap.get(QMDLConstant.SINR)!=null?kpiMap.get(QMDLConstant.SINR):new TreeMap<>();
		TreeMap<Long,Object> rsrp=kpiMap.get(QMDLConstant.RSRP)!=null?kpiMap.get(QMDLConstant.RSRP):new TreeMap<>();
		TreeMap<Long,Object> rsrq=kpiMap.get(QMDLConstant.RSRQ)!=null?kpiMap.get(QMDLConstant.RSRQ):new TreeMap<>();
		TreeMap<Long,Object> rssi=kpiMap.get(QMDLConstant.RSSI)!=null?kpiMap.get(QMDLConstant.RSSI):new TreeMap<>();
		TreeMap<Long,Object> pci=kpiMap.get(QMDLConstant.PHYSICAL_CELL_ID)!=null?kpiMap.get(QMDLConstant.PHYSICAL_CELL_ID):new TreeMap<>();
		TreeMap<Long,Object> jitter=kpiMap.containsKey(QMDLConstant.JITTER)?kpiMap.get(QMDLConstant.JITTER):new TreeMap<>();
		TreeMap<Long,Object> responseTime=kpiMap.containsKey(QMDLConstant.RESPONSE_TIME)?kpiMap.get(QMDLConstant.RESPONSE_TIME):new TreeMap<>();

		for(NVL3CsvDataWrapper csvDataHolder:csvDataHolderList){
			updatekpiMapFromData(ulDataMap,dlDataMap,latency,csvDataHolder,sinr,rsrp,rsrq,rssi,pci,jitter,responseTime);
		}
		putMapsIntoKpiMap(kpiMap, ulDataMap, dlDataMap, latency, sinr, rsrp, rsrq, rssi, pci,jitter,responseTime);
	}


	/**
	 * Put maps into kpi map.
	 *
	 * @param kpiMap the kpi map
	 * @param ulDataMap the ul data map
	 * @param dlDataMap the dl data map
	 * @param latency the latency
	 * @param sinr the sinr
	 * @param rsrp the rsrp
	 * @param rsrq the rsrq
	 * @param rssi the rssi
	 * @param pci the pci
	 * @param responseTime 
	 * @param jitter 
	 */
	private void putMapsIntoKpiMap(Map<String, TreeMap<Long, Object>> kpiMap, TreeMap<Long, Object> ulDataMap,
			TreeMap<Long, Object> dlDataMap, TreeMap<Long, Object> latency, TreeMap<Long, Object> sinr,
			TreeMap<Long, Object> rsrp, TreeMap<Long, Object> rsrq, TreeMap<Long, Object> rssi,
			TreeMap<Long, Object> pci, TreeMap<Long, Object> jitter, TreeMap<Long, Object> responseTime) {
		kpiMap.put(QMDLConstant.UL_THROUGHTPUT, ulDataMap);
		kpiMap.put(QMDLConstant.DL_THROUGHTPUT, dlDataMap);
		kpiMap.put(QMDLConstant.LATENCY, latency);
		kpiMap.put(QMDLConstant.SINR,sinr);
		kpiMap.put(QMDLConstant.RSRP,rsrp);
		kpiMap.put(QMDLConstant.RSRQ,rsrq);
		kpiMap.put(QMDLConstant.RSSI,rssi);
		kpiMap.put(QMDLConstant.PHYSICAL_CELL_ID,pci);
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
	 * @param sinr the sinr
	 * @param rsrp the rsrp
	 * @param rsrq the rsrq
	 * @param rssi the rssi
	 * @param pci the pci
	 * @param responseTime 
	 * @param jitter 
	 */
	private void updatekpiMapFromData(TreeMap<Long, Object> ulDataMap, TreeMap<Long, Object> dlDataMap,
			TreeMap<Long, Object> latency, NVL3CsvDataWrapper csvDataHolder, TreeMap<Long, Object> sinr, TreeMap<Long, Object> rsrp, TreeMap<Long, Object> rsrq, TreeMap<Long, Object> rssi, TreeMap<Long, Object> pci, TreeMap<Long, Object> jitter, TreeMap<Long, Object> responseTime) {
		if(csvDataHolder.getTimeStamp()==null) {
            return;
        }
		if(csvDataHolder.getDlThroughPut()!=null){
			nvLayer3StatsGeneratorService.aggSignalParam(csvDataHolder.getTimeStamp(), csvDataHolder.getDlThroughPut(), dlDataMap);
		}
		if(csvDataHolder.getUlThroughPut()!=null){
			nvLayer3StatsGeneratorService.aggSignalParam(csvDataHolder.getTimeStamp(), csvDataHolder.getUlThroughPut(), ulDataMap);
		}
		if(csvDataHolder.getLatency()!=null){
			nvLayer3StatsGeneratorService.aggSignalParam(csvDataHolder.getTimeStamp(), csvDataHolder.getLatency(), latency);
		}
		if(csvDataHolder.getSinr()!=null){
			nvLayer3StatsGeneratorService.aggSignalParam(csvDataHolder.getTimeStamp(), csvDataHolder.getSinr(), sinr);
		}
		if(csvDataHolder.getRsrp()!=null){
			nvLayer3StatsGeneratorService.aggSignalParam(csvDataHolder.getTimeStamp(), csvDataHolder.getRsrp(), rsrp);
		}
		if(csvDataHolder.getRsrq()!=null){
			nvLayer3StatsGeneratorService.aggSignalParam(csvDataHolder.getTimeStamp(), csvDataHolder.getRsrq(), rsrq);
		}
		if(csvDataHolder.getRssi()!=null){
			nvLayer3StatsGeneratorService.aggSignalParam(csvDataHolder.getTimeStamp(), csvDataHolder.getRssi(), rssi);
		}
		if(csvDataHolder.getPci()!=null){
			pci.put(csvDataHolder.getTimeStamp(), csvDataHolder.getPci());
		}
		if(csvDataHolder.getJitter()!=null){
			nvLayer3StatsGeneratorService.aggSignalParam(csvDataHolder.getTimeStamp(), csvDataHolder.getJitter(), jitter);
		}
		if(csvDataHolder.getResponseTime()!=null){
			nvLayer3StatsGeneratorService.aggSignalParam(csvDataHolder.getTimeStamp(), csvDataHolder.getResponseTime(), responseTime);
		}
	}


	/**
	 * Process csv file from zip.
	 *
	 * @param zipFile the zip file
	 * @param entry the entry
	 * @param aggrigateWrapperData
	 * @param imagePutMap
	 * @param processedFiles 
	 * @param localFilePath 
	 * @return the list
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Override
	public List<NVL3CsvDataWrapper> processCsvFileFromZip(ZipFile zipFile,
			ZipEntry entry, Layer3SummaryWrapper aggrigateWrapperData, Map<String, Put> imagePutMap, List<String> processedFiles, String localFilePath) throws IOException {
		List<NVL3CsvDataWrapper> csvDataHolderList=new ArrayList<>();
		String row;
		InputStream stream= zipFile.getInputStream(entry);
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
				 NVL3CsvDataWrapper csvDataHolder = processCsvRow( row,imagePutMap,aggrigateWrapperData);
				 if(csvDataHolder!=null &&csvDataHolder.getTestType()!=null){
					 csvDataHolderList.add(csvDataHolder);
				 }
			}
		} catch (IOException e) {
			throw new IOException("Error In Reading csv " + e.getMessage());
		}finally{
			stream.close();
		}
		return csvDataHolderList;
	}

	/**
	 * Process csv row.
	 *
	 * @param row the row
	 * @param imagePutMap
	 * @param aggrigateWrapperData
	 * @return the NVL 3 csv data wrapper
	 */
	public NVL3CsvDataWrapper processCsvRow(String row, Map<String, Put> imagePutMap, Layer3SummaryWrapper aggrigateWrapperData) {

		if (row == null || row.trim().length() <=QMDLConstant.MINIMUM_LENTH_OF_CSV_LINE||row.split(Symbol.COMMA_STRING).length<QMDLConstant.MINIMUM_LENGTH_CSV_FIELD){
			logger.warn("Getting wrong row {}  ",row);
			return null;
		}

		String[] lineData = row.split(Symbol.COMMA_STRING,-1);
		NVL3CsvDataWrapper csvDataHolder = setCommonValuesIntoWrapper(aggrigateWrapperData, lineData);

		int networkType = nvL3csvService.getStringTypeByNetworkAndTest(lineData[NetworkDataFormats.TESTTYPE_INDEX],lineData[NetworkDataFormats.NETWORK_TYPE_INDEX]);

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
			logger.debug("invaild network type {}", csvDataHolder.getNetworkType());
		}
		
		//logger.info("csvDataHolder wrapper is {}", new Gson().toJson(csvDataHolder));
		
		return csvDataHolder;
	}


	private NVL3CsvDataWrapper setCommonValuesIntoWrapper(Layer3SummaryWrapper aggrigateWrapperData,
			String[] lineData) {
		NVL3CsvDataWrapper csvDataHolder =new NVL3CsvDataWrapper();
		NvCsvParsingCommonUtils.addNetworkTypeIntoAggrigateWrapper(aggrigateWrapperData,lineData);
		NvCsvParsingCommonUtils.setCommonsParameterOfCsv(lineData, csvDataHolder);
		NvCsvParsingCommonUtils.setCommonValueByNetworkType(lineData, csvDataHolder);
		return csvDataHolder;
	}


	
}

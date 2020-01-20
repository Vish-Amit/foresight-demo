package com.inn.foresight.module.nv.layer3.service.parse.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.hadoop.hbase.client.Put;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.inn.commons.Symbol;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.lang.StringUtils;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.module.nv.NVConfigUtil;
import com.inn.foresight.module.nv.layer3.constants.QMDLConstant;
import com.inn.foresight.module.nv.layer3.qmdlParser.wrappers.Layer3SummaryWrapper;
import com.inn.foresight.module.nv.layer3.qmdlParser.wrappers.NVL3CsvDataWrapper;
import com.inn.foresight.module.nv.layer3.qmdlParser.wrappers.QMDLLogCodeWrapper;
import com.inn.foresight.module.nv.layer3.service.parse.INVCsvProcessService;
import com.inn.foresight.module.nv.layer3.service.parse.INVL3BuildingJsonParserService;
import com.inn.foresight.module.nv.layer3.service.parse.INVL3CsvProcessService;
import com.inn.foresight.module.nv.layer3.utils.NVLayer3Utils;

@Service("NVL3BuildingJsonParserServiceImpl")
public class NVL3BuildingJsonParserServiceImpl implements INVL3BuildingJsonParserService{

	@Autowired
	private INVCsvProcessService nVCsvProcessService;
	@Autowired
	private INVL3CsvProcessService nvcsvL3CsvProcessService;

	private static final  Logger logger = LogManager.getLogger(NVL3BuildingJsonParserServiceImpl.class);

	@Override
	public void processInBuildingFileData(ZipFile zipFile, ZipEntry fileEntry, TreeMap<Long, QMDLLogCodeWrapper> woParsedMap,Boolean isQMDL,Map<String,Put> reportPutMap, Layer3SummaryWrapper aggrigateWrapperData, 
			Map<String, TreeMap<Long, Object>> kpiMap,List<String> processedFiles,String localFilePath) throws Exception{
		/*List<NVL3CsvDataWrapper> responseresult=processJsonAndCovertIntoWrapper(zipFile,fileEntry,isQMDL,reportPutMap,aggrigateWrapperData);
		*/
		//Temporary Solution
		List<NVL3CsvDataWrapper> responseresult=processJsonAndCovertIntoWrapper(zipFile,fileEntry,false,reportPutMap,aggrigateWrapperData,processedFiles,localFilePath);
		processRawData( responseresult, woParsedMap,isQMDL,kpiMap);

	}


	/**
	 * Process raw data.
	 *
	 * @param csvDataHolderList the csv data holder list
	 * @param fileDataMap the file data map
	 * @param isQMDL
	 * @param kpiMap
	 * @throws RestException
	 */
	private void processRawData(List<NVL3CsvDataWrapper> csvDataHolderList, TreeMap<Long, QMDLLogCodeWrapper> fileDataMap, Boolean isQMDL, Map<String, TreeMap<Long, Object>> kpiMap) {
		csvDataHolderList=	processInbuildingDataForBackgroundPushPin(csvDataHolderList);
		//Temporary Solution need to uncomment it
		/*if(isQMDL){
			processJsonforQMDL(csvDataHolderList, fileDataMap);
			nvcsvL3CsvProcessService.processDataForStats(csvDataHolderList, kpiMap);
		}else{*/
			processJsonforNonQMDL(csvDataHolderList, fileDataMap);
//			nVCsvProcessService.processDataForStats(csvDataHolderList, kpiMap);
		//}
	}



	private List<NVL3CsvDataWrapper> processInbuildingDataForBackgroundPushPin(
			List<NVL3CsvDataWrapper> csvDataHolderList) {
		Long oldTimeStamp = null;
		List<NVL3CsvDataWrapper> csvTempData=new ArrayList<>();
		for(NVL3CsvDataWrapper inbuildingData:csvDataHolderList){
			if(inbuildingData.getTimeStamp()!=null && oldTimeStamp==null){
				oldTimeStamp=inbuildingData.getTimeStamp();
			}else if(inbuildingData.getIsBackgroundPin()!=null &&inbuildingData.getIsBackgroundPin().equals(QMDLConstant.TRUE)){
				csvTempData.add(inbuildingData);
			}else  if(inbuildingData.getTimeStamp()!=null && oldTimeStamp!=null){
				updateTimestampIntocsvDataWrapper(csvTempData,inbuildingData.getTimeStamp(),oldTimeStamp);
				csvTempData=new ArrayList<>();
				oldTimeStamp=inbuildingData.getTimeStamp();
			}
		}
		
		return csvDataHolderList;
	}

	private void updateTimestampIntocsvDataWrapper(List<NVL3CsvDataWrapper> csvTempData, Long newTimeStamp,
			Long oldTimeStamp) {
		if (!csvTempData.isEmpty()) {
			long paddingValue = (newTimeStamp - oldTimeStamp) / (csvTempData.size()+1);
			for (NVL3CsvDataWrapper csvData : csvTempData) {
				oldTimeStamp=oldTimeStamp + paddingValue;
				csvData.setTimeStamp(oldTimeStamp);
			}
		}
	}

	private void processJsonforQMDL(List<NVL3CsvDataWrapper> csvDataHolderList,
			TreeMap<Long, QMDLLogCodeWrapper> fileDataMap) {
		for(NVL3CsvDataWrapper wrapper:csvDataHolderList){
			if(wrapper.getTimeStamp()==null || wrapper.getXpoint()==null|| wrapper.getYpoint()==null){
				continue;
			}
			Long key = NVLayer3Utils.getKeyForBean(wrapper.getTimeStamp());
			if(fileDataMap.containsKey(key)){
				aggregateJsonRecordForQMDL(wrapper,fileDataMap.get(key));
			}else{
				QMDLLogCodeWrapper qmdlData = aggregateJsonRecordForQMDL(wrapper,new QMDLLogCodeWrapper());
				intialiseRecordFromJson(fileDataMap, wrapper, key, qmdlData);
			}
		}
	}


	private void intialiseRecordFromJson(TreeMap<Long, QMDLLogCodeWrapper> fileDataMap, NVL3CsvDataWrapper wrapper,
			Long key, QMDLLogCodeWrapper qmdlData) {
		qmdlData.setTimeStamp(wrapper.getTimeStamp());
		qmdlData.setXpoint(NVLayer3Utils.getDoubleArrayValue(null, wrapper.getXpoint()));
		qmdlData.setYpoint(NVLayer3Utils.getDoubleArrayValue(null, wrapper.getYpoint()));
		qmdlData.setEtimeStamp(wrapper.getTimeStamp());
		fileDataMap.put(key,qmdlData);
	}


	private void processJsonforNonQMDL(List<NVL3CsvDataWrapper> csvDataHolderList,
			TreeMap<Long, QMDLLogCodeWrapper> fileDataMap) {
		Double oldYpoint = null;
		Double oldXpoint = null;
		Long oldtimeStamp = null;

		for (NVL3CsvDataWrapper wrapper : csvDataHolderList) {

			if (wrapper.getTimeStamp() == null) {
				continue;
			}

			Long key = NVLayer3Utils.getKeyForBean(wrapper.getTimeStamp());

			if (wrapper.getXpoint() != null && wrapper.getYpoint() != null && wrapper.getXpoint().equals(oldXpoint)
					&& wrapper.getYpoint().equals(oldYpoint) && fileDataMap.containsKey(oldtimeStamp)) {
				QMDLLogCodeWrapper qmdlLogCodeWrapper = aggregateJsonRecordForNonQMDL(wrapper,
						fileDataMap.get(oldtimeStamp));
				fileDataMap.remove(oldtimeStamp);
				fileDataMap.put(wrapper.getTimeStamp(), qmdlLogCodeWrapper);
			} else if (fileDataMap.containsKey(key)) {
				QMDLLogCodeWrapper qmdlLogCodeWrapper = aggregateJsonRecordForNonQMDL(wrapper, fileDataMap.get(key));
				fileDataMap.remove(key);// Temporary solution
				fileDataMap.put(wrapper.getTimeStamp(), qmdlLogCodeWrapper);// Temporary solution

			} else {
				QMDLLogCodeWrapper qmdlData = aggregateJsonRecordForNonQMDL(wrapper, new QMDLLogCodeWrapper());
				intialiseRecordFromJson(fileDataMap, wrapper, wrapper.getTimeStamp(), qmdlData);
			}

			if (wrapper.getXpoint() != null && wrapper.getYpoint() != null) {
				oldXpoint = wrapper.getXpoint();
				oldYpoint = wrapper.getYpoint();
				oldtimeStamp = wrapper.getTimeStamp();
			}
		}
	}

	private QMDLLogCodeWrapper aggregateJsonRecordForNonQMDL(NVL3CsvDataWrapper wrapper, QMDLLogCodeWrapper qmdlLogCodeWrapper) {
		aggregateJsonRecordForQMDL(wrapper,qmdlLogCodeWrapper);
		aggregateJsonRecordForNonQMDLParam(wrapper,qmdlLogCodeWrapper);
		return qmdlLogCodeWrapper;
	}


	private void aggregateJsonRecordForNonQMDLParam(NVL3CsvDataWrapper newWrapper, QMDLLogCodeWrapper oldData) {
		/*oldData.setsINRData(NVLayer3Utils.getDoubleArrayValueFromArrays(oldData.getsINRData(),NVLayer3Utils.getDoubleArrayValue(null, newWrapper.getSinr())));
		oldData.setMeasureRSRQData(NVLayer3Utils.getDoubleArrayValueFromArrays(oldData.getMeasureRSRQData(),NVLayer3Utils.getDoubleArrayValue(null, newWrapper.getRsrq())));
		oldData.setMeasureRSRPData(NVLayer3Utils.getDoubleArrayValueFromArrays(oldData.getMeasureRSRPData(),NVLayer3Utils.getDoubleArrayValue(null, newWrapper.getRsrp())));
		oldData.setrSSIData(NVLayer3Utils.getDoubleArrayValueFromArrays(oldData.getrSSIData(),NVLayer3Utils.getDoubleArrayValue(null, newWrapper.getRssi())));
	*/
		//Temporary solution---------------------uncomment above
		oldData.setsINRData(NVLayer3Utils.getDoubleArrayValue(null, newWrapper.getSinr()));
		oldData.setMeasureRSRQData(NVLayer3Utils.getDoubleArrayValue(null, newWrapper.getRsrq()));
		oldData.setMeasureRSRPData(NVLayer3Utils.getDoubleArrayValue(null, newWrapper.getRsrp()));
		oldData.setrSSIData(NVLayer3Utils.getDoubleArrayValue(null, newWrapper.getRssi()));
		//Temporary solution---------------
		
		oldData.setPci(newWrapper.getPci());
		oldData.setMnc(newWrapper.getMnc());
		oldData.setMcc(newWrapper.getMcc());
		oldData.setBand(getBandFromJson(newWrapper.getBand()));
		
		oldData.setAvgJitter(NVLayer3Utils.getDoubleAggrigateArray(oldData.getAvgJitter(),NVLayer3Utils.getDoubleArrayValue(null, newWrapper.getJitter())));
		oldData.setPacketLoss(NVLayer3Utils.getDoubleAggrigateArray(oldData.getPacketLoss(),NVLayer3Utils.getDoubleArrayValue(null, newWrapper.getPacketLoss())));

		
		
		oldData.setSpeedTestPinNumber(newWrapper.getSpeedTestPinNumber());
		oldData.setSpeedTestDlRate(NVLayer3Utils.getDoubleValueUptoTwoDecimal(newWrapper.getSpeedTestDlRate()));
		oldData.setSpeedTestUlRate(NVLayer3Utils.getDoubleValueUptoTwoDecimal(newWrapper.getSpeedTestUlRate()));		
		oldData.setDownloadTimeGoogle(NVLayer3Utils.getDoubleValueUptoTwoDecimal(newWrapper.getDownloadTimeGoogle()));
		oldData.setDownloadTimeFacebook(NVLayer3Utils.getDoubleValueUptoTwoDecimal(newWrapper.getDownloadTimeFacebook()));
		oldData.setDownloadTimeYoutube(NVLayer3Utils.getDoubleValueUptoTwoDecimal(newWrapper.getDownloadTimeYoutube()));		
		oldData.setHost(newWrapper.getHost());		
		
		
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
		
		

	}


	private String getBandFromJson(String band) {
		try {
			if (!StringUtils.isEmpty(band) && !band.equals(Symbol.HYPHEN_STRING)) {
				return NVLayer3Utils.getStringFromJson(band.replaceAll("[^\\d.]", ""));				
			}
		} catch (Exception e) {
			logger.error("Exception inside getBandFromJson {}",ExceptionUtils.getStackTrace(e));
			return null;
		}
		return null;
	}


	private QMDLLogCodeWrapper aggregateJsonRecordForQMDL(NVL3CsvDataWrapper newWrapper, QMDLLogCodeWrapper oldData) {
		oldData.setLatency(NVLayer3Utils.getDoubleAggrigateArray(oldData.getLatency(),NVLayer3Utils.getDoubleArrayValue(null, newWrapper.getLatency())));
		oldData.setDlThroughPut(NVLayer3Utils.getDoubleAggrigateArray(oldData.getDlThroughPut(),NVLayer3Utils.getDoubleArrayValue(null, newWrapper.getDlThroughPut())));
		oldData.setUlThroughPut(NVLayer3Utils.getDoubleAggrigateArray(oldData.getUlThroughPut(),NVLayer3Utils.getDoubleArrayValue(null, newWrapper.getUlThroughPut())));
		oldData.setXpoint(NVLayer3Utils.getDoubleAggrigateArray(oldData.getXpoint(),NVLayer3Utils.getDoubleArrayValue(null, newWrapper.getXpoint())));
		oldData.setYpoint(NVLayer3Utils.getDoubleAggrigateArray(oldData.getYpoint(),NVLayer3Utils.getDoubleArrayValue(null, newWrapper.getYpoint())));
		oldData.setWifiRssi(NVLayer3Utils.getDoubleAggrigateArray(oldData.getWifiRssi(),NVLayer3Utils.getDoubleArrayValue(null, newWrapper.getWifiRssi())));
		oldData.setWifiSnr(NVLayer3Utils.getDoubleAggrigateArray(oldData.getWifiSnr(),NVLayer3Utils.getDoubleArrayValue(null, newWrapper.getWifiSnr())));
		oldData.setSsid(NVLayer3Utils.getLatestValue(newWrapper.getTimeStamp(), newWrapper.getSsid(), oldData.getSsid(), oldData.getTempTimeStamp()));
		oldData.setTempTimeStamp(newWrapper.getTimeStamp());
		oldData.setSectorId(newWrapper.getCellId());//Both are same
		oldData.setCellid(newWrapper.getCellId());
		oldData.seteNodebId(newWrapper.geteNodeBId());
		oldData.setCgi(newWrapper.getCgi());
		oldData.setEarfcn(newWrapper.getEarfcn());
		oldData.setUlEarfcn(newWrapper.getUlEarfcn());
		oldData.setBssid(NVLayer3Utils.getLatestValue(newWrapper.getTimeStamp(),newWrapper.getBssid(),oldData.getBssid(),oldData.getTempTimeStamp()));
		oldData.setChannel(newWrapper.getChannel());
		oldData.setLinkSpeed(newWrapper.getLinkSpeed());
		//logger.info("Got earfcn as{}",newWrapper.getEarfcn());
		oldData.setEtimeStamp(newWrapper.getTimeStamp());
		return oldData;
	}

	private List<NVL3CsvDataWrapper> processJsonAndCovertIntoWrapper(ZipFile zipFile, ZipEntry entry, Boolean isQMDL, Map<String, Put> reportPutMap, Layer3SummaryWrapper aggrigateWrapperData, List<String> processedFiles, String localFilePath)
			throws IOException, ParseException {
		List<NVL3CsvDataWrapper> wrapperList = new ArrayList<>();
		InputStream stream = zipFile.getInputStream(entry);
		String csvFilePath = localFilePath + entry.getName();
		boolean isCsvCopiedToLocal = NVLayer3Utils.copyFileToLocalPath(csvFilePath, zipFile.getInputStream(entry));
		if(isCsvCopiedToLocal) {
			processedFiles.add(csvFilePath);
		}
		
		processInbuildingJson(isQMDL, reportPutMap, wrapperList, stream,aggrigateWrapperData);
		return wrapperList;
	}


	private void processInbuildingJson(Boolean isQMDL, Map<String, Put> reportPutMap,
			List<NVL3CsvDataWrapper> wrapperList, InputStream stream, Layer3SummaryWrapper aggrigateWrapperData)
					throws IOException, ParseException, UnsupportedEncodingException {
		JSONParser jsonParser = new JSONParser();
		JSONArray jsonArray = (JSONArray) jsonParser.parse(new InputStreamReader(stream, "UTF-8"));
		ListIterator jsonIterator = jsonArray.listIterator();
		while (jsonIterator.hasNext()) {
			JSONObject jsonObject = (JSONObject)jsonIterator.next();
			processRawJsonForSummary(aggrigateWrapperData,jsonObject);
			if (jsonObject.containsKey(QMDLConstant.INBUILDING_RAW_DATA_JSON_KEY)) {
				processRawlistJson(wrapperList, jsonObject,isQMDL,aggrigateWrapperData);
			}
			processRawJsonForImage(reportPutMap,jsonObject);
		}
		logger.debug("wrapperList After Lock set{}",new Gson().toJson(wrapperList));  			
		logger.debug("aggrigateWrapperData After Lock set{}",new Gson().toJson(aggrigateWrapperData));  

	}


	private void processRawJsonForSummary(Layer3SummaryWrapper aggrigateWrapperData, JSONObject jsonObject) {
		setOperatorNameIntoAggrigatedWrapper(aggrigateWrapperData, jsonObject);
		setImeiIntoAggrigatedWrapper(aggrigateWrapperData, jsonObject);
		setDeviceidIntoAggrigrateedWrapper(aggrigateWrapperData, jsonObject);
		setLockValuesIntoAggrigatedWrapper(aggrigateWrapperData, jsonObject);
		setDeviceInfoIntoAggrigatedWrapper(aggrigateWrapperData, jsonObject);
		setBandForWiFiIntoAggrigatedWrapper(aggrigateWrapperData, jsonObject);		  			
		}


	public void setBandForWiFiIntoAggrigatedWrapper(Layer3SummaryWrapper aggrigateWrapperData, JSONObject jsonObject) {
		if(jsonObject.containsKey(QMDLConstant.BAND)){		
			aggrigateWrapperData.setWifiBand(NVLayer3Utils.getStringFromJson(jsonObject.get(QMDLConstant.BAND)));
			}
	}


	
	private void setDeviceInfoIntoAggrigatedWrapper(Layer3SummaryWrapper aggrigateWrapperData, JSONObject jsonObject) {
		if(jsonObject.containsKey(QMDLConstant.HANDSET_MANUFACTURE)){
			aggrigateWrapperData.setMake(NVLayer3Utils.getStringFromJson(jsonObject.get(QMDLConstant.HANDSET_MANUFACTURE)));
		}
		if(jsonObject.containsKey(QMDLConstant.MODEL_JSON)){
			aggrigateWrapperData.setModel(NVLayer3Utils.getStringFromJson(jsonObject.get(QMDLConstant.MODEL_JSON)));
		}
		if(jsonObject.containsKey(QMDLConstant.PLATFORM)){
			aggrigateWrapperData.setPlatform(NVLayer3Utils.getStringFromJson(jsonObject.get(QMDLConstant.PLATFORM)));
		}
		if(jsonObject.containsKey(QMDLConstant.BATTERY_INFORMATION)){
			aggrigateWrapperData.setBattaryInfo(NVLayer3Utils.getStringFromJson(jsonObject.get(QMDLConstant.BATTERY_INFORMATION)));
		}
		if(jsonObject.containsKey(QMDLConstant.CHIPSET_INFORMATION)){
			aggrigateWrapperData.setChipsetInfo(NVLayer3Utils.getStringFromJson(jsonObject.get(QMDLConstant.CHIPSET_INFORMATION)));
		}
		if(jsonObject.containsKey(QMDLConstant.NV_APP_VERSION)){
			aggrigateWrapperData.setAppVersion(NVLayer3Utils.getStringFromJson(jsonObject.get(QMDLConstant.NV_APP_VERSION)));
		}
		if(jsonObject.containsKey(QMDLConstant.VERSION_NAME)){
			aggrigateWrapperData.setDriveVersion(NVLayer3Utils.getStringFromJson(jsonObject.get(QMDLConstant.VERSION_NAME)));
		}
	}

	public void setDeviceidIntoAggrigrateedWrapper(Layer3SummaryWrapper aggrigateWrapperData, JSONObject jsonObject) {
		if(jsonObject.containsKey(QMDLConstant.DEVICEID_JSON_KEY)){
			aggrigateWrapperData.setDeviceId(NVLayer3Utils.getStringFromJson(jsonObject.get(QMDLConstant.DEVICEID_JSON_KEY)));
		}
	}


	public void setImeiIntoAggrigatedWrapper(Layer3SummaryWrapper aggrigateWrapperData, JSONObject jsonObject) {
		if(jsonObject.containsKey(QMDLConstant.IMEI_JSON)){
			aggrigateWrapperData.setImei(NVLayer3Utils.setValuesIntoSet(aggrigateWrapperData.getImei(),
					NVLayer3Utils.getStringFromJson(jsonObject.get(QMDLConstant.IMEI_JSON))));
		}
	}


	public void setOperatorNameIntoAggrigatedWrapper(Layer3SummaryWrapper aggrigateWrapperData,
			JSONObject jsonObject) {
		if(jsonObject.containsKey(QMDLConstant.OPERATOR_NAME_JSON)){
			aggrigateWrapperData.setOperatorName(NVLayer3Utils.getStringFromJson(jsonObject.get(QMDLConstant.OPERATOR_NAME_JSON)));
		}
	}


	public void setLockValuesIntoAggrigatedWrapper(Layer3SummaryWrapper aggrigateWrapperData, JSONObject jsonObject) {
		if(jsonObject.containsKey(QMDLConstant.BANDLOCK_JSON_KEY)){
			aggrigateWrapperData.setlBand(NVLayer3Utils.getIntegerFromJson(jsonObject.get(QMDLConstant.BANDLOCK_JSON_KEY)));
		}
		if(jsonObject.containsKey(QMDLConstant.EARFCNLOCK_JSON_KEY)){
			aggrigateWrapperData.setlEarfcn(NVLayer3Utils.getIntegerFromJson(jsonObject.get(QMDLConstant.EARFCNLOCK_JSON_KEY)));
		}
		if(jsonObject.containsKey(QMDLConstant.RATLOCK_JSON_KEY)){
			aggrigateWrapperData.setlRat(NVLayer3Utils.getStringFromJson(jsonObject.get(QMDLConstant.RATLOCK_JSON_KEY)));
		}
		if(jsonObject.containsKey(QMDLConstant.PCILOCK_JSON_KEY)){
			aggrigateWrapperData.setlPci(NVLayer3Utils.getIntegerFromJson(jsonObject.get(QMDLConstant.PCILOCK_JSON_KEY)));
		}
	}


	private void processRawJsonForImage(Map<String, Put> reportPutMap, JSONObject jsonObject) {
		if(jsonObject.containsKey(QMDLConstant.NO_ACCESS_AREA_IMAGE_KEY)){
			JSONArray rawDataArray = (JSONArray) jsonObject.get(QMDLConstant.NO_ACCESS_AREA_IMAGE_KEY);
			ListIterator jsonIterator = rawDataArray.listIterator();
			while (jsonIterator.hasNext()) {
				addParamToImagePutObject(reportPutMap, jsonIterator);
			}
		}
	}


	private void addParamToImagePutObject(Map<String, Put> reportPutMap, ListIterator jsonIterator) {
		JSONObject json = (JSONObject) jsonIterator.next();
		if(json.containsKey(QMDLConstant.IMAGE_FILE_JSON_KEY)){
		String filename = String.valueOf(json.get(QMDLConstant.IMAGE_FILE_JSON_KEY));
			if(reportPutMap.containsKey(filename)){
				Put put= reportPutMap.get(filename);
				NVLayer3Utils.addStringToPut(put, json.get(QMDLConstant.XPOINT), QMDLConstant.XPOINT, QMDLConstant.COLUMN_FAMILY);
				NVLayer3Utils.addStringToPut(put, json.get(QMDLConstant.YPOINT), QMDLConstant.YPOINT, QMDLConstant.COLUMN_FAMILY);
				NVLayer3Utils.addStringToPut(put, json.get(QMDLConstant.IS_PORTRAIT_IMAGE), QMDLConstant.IS_PORTRAIT_IMAGE, QMDLConstant.COLUMN_FAMILY);
				NVLayer3Utils.addStringToPut(put, json.get(QMDLConstant.IMAGE_FILE_JSON_KEY), QMDLConstant.IMAGE_FILE_JSON_KEY, QMDLConstant.COLUMN_FAMILY);
			}
		}
	}


	private void processRawlistJson(List<NVL3CsvDataWrapper> wrapperList, JSONObject jsonObject, Boolean isQMDL, Layer3SummaryWrapper aggrigateWrapperData) {
		JSONArray rawDataArray = (JSONArray) jsonObject.get(QMDLConstant.INBUILDING_RAW_DATA_JSON_KEY);
		ListIterator jsonIterator = rawDataArray.listIterator();
		if (isQMDL) {
			processRawDataForQMDL(jsonIterator, wrapperList,aggrigateWrapperData);
		} else {
			processRawDataForNonQMDL(jsonIterator, wrapperList,aggrigateWrapperData);
		}
	}

	private void processRawDataForQMDL(ListIterator jsonIterator, List<NVL3CsvDataWrapper> wrapperList, Layer3SummaryWrapper aggrigateWrapperData) {
		while (jsonIterator.hasNext()) {
			JSONObject json = (JSONObject) jsonIterator.next();
			NVL3CsvDataWrapper wrapper = new NVL3CsvDataWrapper();
			addCommonParamsIntoWrapper(json, wrapper,aggrigateWrapperData);
			wrapperList.add(wrapper);
		}
	}


	private void processRawDataForNonQMDL(ListIterator jsonIterator, List<NVL3CsvDataWrapper> wrapperList, Layer3SummaryWrapper aggrigateWrapperData) {
		while (jsonIterator.hasNext()) {
			JSONObject json = (JSONObject) jsonIterator.next();
			NVL3CsvDataWrapper wrapper = new NVL3CsvDataWrapper();
			addCommonParamsIntoWrapper(json, wrapper,aggrigateWrapperData);
			setKpiWiseDataFromJson(json, wrapper,aggrigateWrapperData);
			wrapperList.add(wrapper);
		}
	}


	private void addCommonParamsIntoWrapper(JSONObject json, NVL3CsvDataWrapper wrapper, Layer3SummaryWrapper aggrigateWrapperData) {
		if(json.containsKey(QMDLConstant.XPOINT)){
		wrapper.setXpoint(NVLayer3Utils.getDoubleFromJson(json.get(QMDLConstant.XPOINT)));
		}
		if(json.containsKey(QMDLConstant.YPOINT)){	
		wrapper.setYpoint(NVLayer3Utils.getDoubleFromJson(json.get(QMDLConstant.YPOINT)));
		}
		if(json.containsKey(QMDLConstant.TIMESTAMP_INBUILDING_JSON_KEY)){		
		wrapper.setTimeStamp(NVLayer3Utils.getLongValueFromJson(json.get(QMDLConstant.TIMESTAMP_INBUILDING_JSON_KEY)));
		}
		if (json.containsKey(QMDLConstant.ISBACKGROUNDPIN_INBUILDING_JSON_KEY)) {
			wrapper.setIsBackgroundPin(
					NVLayer3Utils.getStringFromJson(json.get(QMDLConstant.ISBACKGROUNDPIN_INBUILDING_JSON_KEY)));
		}	
		addLTEParamValue(json, wrapper);
		setWifiParamValues(json, wrapper);
		if(json.containsKey(QMDLConstant.NETWORK_TYPE)){
			aggrigateWrapperData.setOperator(NVLayer3Utils.setValuesIntoSet(aggrigateWrapperData.getOperator(), NVLayer3Utils.getStringFromJson(json.get(QMDLConstant.NETWORK_TYPE))));
			wrapper.setNetworkType(NVLayer3Utils.getStringFromJson(json.get(QMDLConstant.NETWORK_TYPE)));			
		}
		setCgiIntoAggrigatedWrapper(json, wrapper);
		setEnodeBIntoAggrigatedWrapper(json, wrapper);
		setCellIdIntoAggrigatedWrapper(json, wrapper);
		
		
		if (json.containsKey(QMDLConstant.EARFCN_JSON_KEY)) {
			wrapper.setEarfcn(
					NVLayer3Utils.getIntegerFromJson(json.get(QMDLConstant.EARFCN_JSON_KEY)));
		}	
		
		if (json.containsKey(QMDLConstant.UL_EARFCN_JSON_KEY)) {
			wrapper.setUlEarfcn(
					NVLayer3Utils.getLongValueFromJson(json.get(QMDLConstant.UL_EARFCN_JSON_KEY)));
		}	
		
		
		if (json.containsKey(QMDLConstant.PING_VALUE_HOLDER_JSON_KEY)) {
			setLatencyAndJitterValueFromBrowseJson(json.get(QMDLConstant.PING_VALUE_HOLDER_JSON_KEY),wrapper);
		}	
	}
	
	
	public void setLatencyAndJitterValueFromBrowseJson(Object browseObject, NVL3CsvDataWrapper wrapper) {
		JSONObject pingHolderJson = (JSONObject) browseObject;
		if(pingHolderJson.containsKey(QMDLConstant.JITTER_JSON_KEY)){
			wrapper.setJitter(NVLayer3Utils.getDoubleFromJson(pingHolderJson.get(QMDLConstant.JITTER_JSON_KEY)));
		}
		if(pingHolderJson.containsKey(QMDLConstant.PACKET_LOSS_IB_JSON_KEY)){
			wrapper.setPacketLoss(NVLayer3Utils.getDoubleFromJson(pingHolderJson.get(QMDLConstant.PACKET_LOSS_IB_JSON_KEY)));
		}
		
		if(pingHolderJson.containsKey(QMDLConstant.LATENCY_JSON_KEY)){
			wrapper.setLatency(NVLayer3Utils.getDoubleFromJson(pingHolderJson.get(QMDLConstant.LATENCY_JSON_KEY)));
		}
		
		if(pingHolderJson.containsKey(QMDLConstant.BUFFERSIZE_IB_JSON_KEY)){
			wrapper.setPingBufferSize(NVLayer3Utils.getIntegerFromJson(pingHolderJson.get(QMDLConstant.BUFFERSIZE_IB_JSON_KEY)));
		}
		if(pingHolderJson.containsKey(QMDLConstant.HOST_IB_JSON_KEY)){
			wrapper.setHost(NVLayer3Utils.getStringFromJson(pingHolderJson.get(QMDLConstant.HOST_IB_JSON_KEY)));
		}
	}

	

	public void setCellIdIntoAggrigatedWrapper(JSONObject json, NVL3CsvDataWrapper wrapper) {
		if(json.containsKey(QMDLConstant.CELL_ID_JSON)){
			wrapper.setCellId(NVLayer3Utils.getIntegerFromJson(json.get(QMDLConstant.CELL_ID_JSON)));
		}
	}


	public void setEnodeBIntoAggrigatedWrapper(JSONObject json, NVL3CsvDataWrapper wrapper) {
		if(json.containsKey(QMDLConstant.ENODEB_JSON_KEY)){
			wrapper.seteNodeBId(NVLayer3Utils.getIntegerFromJson(json.get(QMDLConstant.ENODEB_JSON_KEY)));
		}
	}


	public void setCgiIntoAggrigatedWrapper(JSONObject json, NVL3CsvDataWrapper wrapper) {
		if(json.containsKey(QMDLConstant.CGI_JSON_KEY)){
			wrapper.setCgi(NVLayer3Utils.getIntegerFromJson(json.get(QMDLConstant.CGI_JSON_KEY)));
		}
	}


	
	private void addLTEParamValue(JSONObject json, NVL3CsvDataWrapper wrapper) {
		if(json.containsKey(QMDLConstant.TEST_TYPE_JSON_KEY)){
			wrapper.setTestType(NVLayer3Utils.getStringFromJson(json.get(QMDLConstant.TEST_TYPE_JSON_KEY)));
		}
		if(json.containsKey(QMDLConstant.LATENCY_JSON_KEY)){
			wrapper.setLatency(NVLayer3Utils.getDoubleFromJson(json.get(QMDLConstant.LATENCY_JSON_KEY)));
		}
		if(json.containsKey(QMDLConstant.AVG_DL_JSON_KEY)){
			wrapper.setDlThroughPut(NVLayer3Utils.getDoubleFromJson(json.get(QMDLConstant.AVG_DL_JSON_KEY)));
		}
		if(json.containsKey(QMDLConstant.AVG_UL_JSON_KEY)){	
			wrapper.setUlThroughPut(NVLayer3Utils.getDoubleFromJson(json.get(QMDLConstant.AVG_UL_JSON_KEY)));
		}



		if(json.containsKey(QMDLConstant.JITTER_IB_JSON_KEY)){	
			wrapper.setJitter(NVLayer3Utils.getDoubleFromJson(json.get(QMDLConstant.JITTER_IB_JSON_KEY)));
		}
		if(json.containsKey(QMDLConstant.PACKET_LOSS_IB_JSON_KEY)){	
			wrapper.setPacketLoss(NVLayer3Utils.getDoubleFromJson(json.get(QMDLConstant.PACKET_LOSS_IB_JSON_KEY)));
		}
		if(json.containsKey(QMDLConstant.SPEED_TEST_DL_RATE_JSON_KEY)){	
			wrapper.setSpeedTestDlRate(NVLayer3Utils.getDoubleFromJson(json.get(QMDLConstant.SPEED_TEST_DL_RATE_JSON_KEY)));			
		}
		if(json.containsKey(QMDLConstant.SPEED_TEST_UL_RATE_JSON_KEY)){	
			wrapper.setSpeedTestUlRate(NVLayer3Utils.getDoubleFromJson(json.get(QMDLConstant.SPEED_TEST_UL_RATE_JSON_KEY)));			
		}
		if(json.containsKey(QMDLConstant.GOOGLE_BROWSE_TIME_JSON_KEY)){	
			wrapper.setDownloadTimeGoogle(NVLayer3Utils.getDoubleFromJson(json.get(QMDLConstant.GOOGLE_BROWSE_TIME_JSON_KEY)));		
		}
		if(json.containsKey(QMDLConstant.FACEBOOK_BROWSE_TIME_JSON_KEY)){	
			wrapper.setDownloadTimeFacebook(NVLayer3Utils.getDoubleFromJson(json.get(QMDLConstant.FACEBOOK_BROWSE_TIME_JSON_KEY)));
		}
		if(json.containsKey(QMDLConstant.YOUTUBE_BROWSE_TIME_JSON_KEY)){	
			wrapper.setDownloadTimeYoutube(NVLayer3Utils.getDoubleFromJson(json.get(QMDLConstant.YOUTUBE_BROWSE_TIME_JSON_KEY)));
		}
		if(json.containsKey(QMDLConstant.SPEED_TEST_PIN_NUMBER_JSON_KEY)){	
			wrapper.setSpeedTestPinNumber(NVLayer3Utils.getIntegerFromJson(json.get(QMDLConstant.SPEED_TEST_PIN_NUMBER_JSON_KEY)));

		}		
	}


	private void setWifiParamValues(JSONObject json, NVL3CsvDataWrapper wrapper) {
		if(json.containsKey(QMDLConstant.WIFI_RSSI_JSON)){
		wrapper.setWifiRssi(NVLayer3Utils.getDoubleFromJson(json.get(QMDLConstant.WIFI_RSSI_JSON)));
		}
		if(json.containsKey(QMDLConstant.WIFI_SNR_JSON)){
		wrapper.setWifiSnr(NVLayer3Utils.getDoubleFromJson(json.get(QMDLConstant.WIFI_SNR_JSON)));
		}
		if(json.containsKey(QMDLConstant.WIFI_SSID_JSON)){	
		wrapper.setSsid(NVLayer3Utils.getStringFromJson(json.get(QMDLConstant.WIFI_SSID_JSON)));
		}
		if(json.containsKey(QMDLConstant.WIFI_BSSID_JSON) && json.get(QMDLConstant.WIFI_BSSID_JSON)!=null) {
		wrapper.setBssid(NVLayer3Utils.getStringFromJson(json.get(QMDLConstant.WIFI_BSSID_JSON)).replaceAll(Symbol.COMMA_STRING, QMDLConstant.SPACE));
		}
		if(json.containsKey(QMDLConstant.WIFI_CHANNEL_JSON)) {
			wrapper.setChannel(NVLayer3Utils.getIntegerFromJson(json.get(QMDLConstant.WIFI_CHANNEL_JSON)));
		}
		if(json.containsKey(QMDLConstant.WIFI_LINKSPEED_JSON)) {
			wrapper.setLinkSpeed(NVLayer3Utils.getIntegerFromJson(json.get(QMDLConstant.WIFI_LINKSPEED_JSON)));
		}
	}


	private void setKpiWiseDataFromJson(JSONObject json, NVL3CsvDataWrapper wrapper, Layer3SummaryWrapper aggrigateWrapperData) {
		if(json.containsKey(QMDLConstant.RSRP)){	
		wrapper.setRsrp(NVLayer3Utils.getDoubleFromJson(json.get(QMDLConstant.RSRP)));
		}
		if(json.containsKey(QMDLConstant.RSRQ)){	
			wrapper.setRsrq(NVLayer3Utils.getDoubleFromJson(json.get(QMDLConstant.RSRQ)));
		}
		if(json.containsKey(QMDLConstant.RSSI)){	
			wrapper.setRssi(NVLayer3Utils.getDoubleFromJson(json.get(QMDLConstant.RSSI)));
		}
		if(json.containsKey(QMDLConstant.SINR)){	
			wrapper.setSinr(NVLayer3Utils.getDoubleFromJson(json.get(QMDLConstant.SINR)));
		}
		if(json.containsKey(QMDLConstant.PCI_JSON_KEY)){	
		wrapper.setPci(NVLayer3Utils.getIntegerFromJson(json.get(QMDLConstant.PCI_JSON_KEY)));
		}
		if(json.containsKey(QMDLConstant.MCC)){		
		wrapper.setMcc(NVLayer3Utils.getIntegerFromJson(json.get(QMDLConstant.MCC)));
		}
		if(json.containsKey(QMDLConstant.MNC)){		
		wrapper.setMnc(NVLayer3Utils.getIntegerFromJson(json.get(QMDLConstant.MNC)));
		}

		if(json.containsKey(QMDLConstant.TECHNOLOGY)){		
			if(NVLayer3Utils.getStringFromJson(json.get(QMDLConstant.TECHNOLOGY)).equalsIgnoreCase(QMDLConstant.WIFI)
					&& aggrigateWrapperData.getWifiBand()!=null) {				
				wrapper.setBand(aggrigateWrapperData.getWifiBand());
				
			}
		}
			
		if(json.containsKey(QMDLConstant.BAND)){		
		wrapper.setBand(NVLayer3Utils.getStringFromJson(json.get(QMDLConstant.BAND)));
		}
	}

}





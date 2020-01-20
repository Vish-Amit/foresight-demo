package com.inn.foresight.module.nv.layer3.service.parse.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.layer3.constants.QMDLConstant;
import com.inn.foresight.module.nv.layer3.qmdlParser.wrappers.Layer3SummaryWrapper;
import com.inn.foresight.module.nv.layer3.qmdlParser.wrappers.NVL3CsvDataWrapper;
import com.inn.foresight.module.nv.layer3.qmdlParser.wrappers.NvSMSDetailWrapper;
import com.inn.foresight.module.nv.layer3.qmdlParser.wrappers.QMDLLogCodeWrapper;
import com.inn.foresight.module.nv.layer3.service.parse.INVCsvProcessService;
import com.inn.foresight.module.nv.layer3.service.parse.INVL3CsvProcessService;
import com.inn.foresight.module.nv.layer3.service.parse.INVLiveJsonProcessService;
import com.inn.foresight.module.nv.layer3.utils.NVLayer3Utils;
import com.inn.foresight.module.nv.layer3.utils.NetworkDataFormats;
import com.inn.foresight.module.nv.layer3.wrapper.YoutubeWrapper;

@Service("NVLiveJsonProcessServiceImpl")
public class NVLiveJsonProcessServiceImpl implements INVLiveJsonProcessService {

	/** The logger. */
	private static final Logger logger = LogManager.getLogger(NVLiveJsonProcessServiceImpl.class);


	@Autowired
	private INVL3CsvProcessService nvLayer3Service;
	@Autowired
	private INVCsvProcessService nvcsvService;


	@Override
	public void processJsonFile(ZipFile zipFile, ZipEntry entry,
			TreeMap<Long, QMDLLogCodeWrapper> woParsedMap, Map<String, TreeMap<Long, Object>> kpiValuesMap,
			Layer3SummaryWrapper aggrigateWrapperData,Boolean isQmdl) throws IOException {
		List<NVL3CsvDataWrapper> wrapperList = processJsonAndCovertIntoWrapper(zipFile, entry,aggrigateWrapperData,isQmdl);
		logger.info("Getting live drive json  {}",wrapperList.size());
		if(!isQmdl){
			nvcsvService.processCsvDataForLayer3(wrapperList, woParsedMap, kpiValuesMap, aggrigateWrapperData);
		}else{
			nvLayer3Service.processCsvDataForLayer3(wrapperList, woParsedMap, kpiValuesMap,aggrigateWrapperData);
		}
	}

	private List<NVL3CsvDataWrapper> processJsonAndCovertIntoWrapper(ZipFile zipFile, ZipEntry entry,
			Layer3SummaryWrapper aggrigateWrapperData, Boolean isQmdl) throws IOException {
		List<NVL3CsvDataWrapper> wrapperList = new ArrayList<>();
		InputStream stream = zipFile.getInputStream(entry);
		JSONParser jsonParser = new JSONParser();
		try {
			JSONObject jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader(stream, "UTF-8"));
			if (jsonObject.containsKey(QMDLConstant.OUTER_JSON_LIST_KEY)) {
				processSpanlistJson(wrapperList, jsonObject, isQmdl,aggrigateWrapperData);
			}
			processJsonForSummary(aggrigateWrapperData, jsonObject);
		} catch (Exception e) {
			logger.error("Error in Parsing json {}   ", Utils.getStackTrace(e));
		}
		return wrapperList;
	}

	private void processJsonForSummary(Layer3SummaryWrapper aggrigateWrapperData, JSONObject jsonObject) {
		if(jsonObject.containsKey(QMDLConstant.NV_APP_VERSION)){
			aggrigateWrapperData.setAppVersion(NVLayer3Utils.getStringFromJson(jsonObject.get(QMDLConstant.NV_APP_VERSION)));
		}
		if(jsonObject.containsKey(QMDLConstant.MANUFACTURE_JSON)){
			aggrigateWrapperData.setMake(NVLayer3Utils.getStringFromJson(jsonObject.get(QMDLConstant.MANUFACTURE_JSON)));
		}

		if(jsonObject.containsKey(QMDLConstant.MODEL_JSON)){
			aggrigateWrapperData.setModel(NVLayer3Utils.getStringFromJson(jsonObject.get(QMDLConstant.MODEL_JSON)));
		}

		if(jsonObject.containsKey(QMDLConstant.ANDROID_VERSION_JSON)){
			aggrigateWrapperData.setPlatform(NVLayer3Utils.getStringFromJson(jsonObject.get(QMDLConstant.ANDROID_VERSION_JSON)));

		}
		if(jsonObject.containsKey(QMDLConstant.IMEI_JSON)){
			aggrigateWrapperData.setImei(NVLayer3Utils.setValuesIntoSet(aggrigateWrapperData.getImei(),
					NVLayer3Utils.getStringFromJson(jsonObject.get(QMDLConstant.IMEI_JSON))));
		}
		
	}

	private void processSpanlistJson(List<NVL3CsvDataWrapper> wrapperList, JSONObject jsonObject, Boolean isQmdl, Layer3SummaryWrapper aggrigateWrapperData) {
		JSONArray jsonArray = (JSONArray) jsonObject.get(QMDLConstant.OUTER_JSON_LIST_KEY);
		ListIterator jsonIterator = jsonArray.listIterator();
		while (jsonIterator.hasNext()) {
			JSONObject json = (JSONObject) jsonIterator.next();
			if (json.containsKey(QMDLConstant.INNER_JSON_LIST_KEY)) {
				processPositionJsonList(json, wrapperList,isQmdl,aggrigateWrapperData);
			}
		}
	}

	private void processPositionJsonList(JSONObject json, List<NVL3CsvDataWrapper> wrapperList, Boolean isQmdl, Layer3SummaryWrapper aggrigateWrapperData) {
		JSONArray innerJsonArray = (JSONArray) json.get(QMDLConstant.INNER_JSON_LIST_KEY);

		for (Object innnerJson : innerJsonArray.toArray()) {
			NVL3CsvDataWrapper wrapper = new NVL3CsvDataWrapper();
			JSONObject record = (JSONObject) innnerJson;
			setCommonParams(aggrigateWrapperData, wrapper, record);
			if(!isQmdl){
				addNonQMDLDataIntoWrapper(record,wrapper);
			}

			wrapperList.add(wrapper);
		}
	}

	private void setCommonParams(Layer3SummaryWrapper aggrigateWrapperData, NVL3CsvDataWrapper wrapper,
			JSONObject record) {
		addDlUlIntoWrapperFromJson(wrapper, record);
		addDriveInfoRelatedParamIntoWrapper(wrapper, record);
		if (record.containsKey(QMDLConstant.PING_VALUE_HOLDER_JSON_KEY)) {
			setLatencyAndJitterValueFromBrowseJson(record.get(QMDLConstant.PING_VALUE_HOLDER_JSON_KEY),wrapper);
		}
		if(record.containsKey(QMDLConstant.BROWSE_HOLDER_JSON_KEY)){
			setResposnseWaitTimeFromJson(record.get(QMDLConstant.BROWSE_HOLDER_JSON_KEY),wrapper);
		}
		if(record.containsKey(QMDLConstant.NETWORK_TYPE_JSON)){//LTE
			aggrigateWrapperData.setOperator(NVLayer3Utils.setValuesIntoSet(aggrigateWrapperData.getOperator(), NVLayer3Utils.getStringFromJson(record.get(QMDLConstant.NETWORK_TYPE_JSON))));
			wrapper.setNetworkType(NVLayer3Utils.getStringFromJson(record.get(QMDLConstant.NETWORK_TYPE_JSON)));
		}
		if(record.containsKey(QMDLConstant.NETWORK_PROVIDER_JSON)){
			aggrigateWrapperData.setOperatorName(NVLayer3Utils.getStringFromJson(record.get(QMDLConstant.NETWORK_PROVIDER_JSON)));
		}
		if (record.containsKey(QMDLConstant.CGI_JSON)) {
			wrapper.setCi(NVLayer3Utils.getIntegerFromJson(record.get(QMDLConstant.CGI_JSON)));
		}
		if(!record.containsKey(QMDLConstant.CGI_JSON) && record.containsKey(QMDLConstant.CELL_ID_JSON)){
			wrapper.setCi(NVLayer3Utils.getIntegerFromJson(record.get(QMDLConstant.CELL_ID_JSON)));
		}
		addReportDataFromLiveJson(aggrigateWrapperData, wrapper, record);
		}

	private void addDriveInfoRelatedParamIntoWrapper(NVL3CsvDataWrapper wrapper, JSONObject record) {
		if (record.containsKey(QMDLConstant.LATITUDE_JSON_KEY)) {
			wrapper.setLat(NVLayer3Utils.getDoubleFromJson(record.get(QMDLConstant.LATITUDE_JSON_KEY)));
		}
		if (record.containsKey(QMDLConstant.LONGITUDE_JSON_KEY)) {
			wrapper.setLon(NVLayer3Utils.getDoubleFromJson(record.get(QMDLConstant.LONGITUDE_JSON_KEY)));
		}
		if (record.containsKey(QMDLConstant.TIMESTAMP_JSON_KEY)) {
			wrapper.setTimeStamp(NVLayer3Utils.getLongValueFromJson(record.get(QMDLConstant.TIMESTAMP_JSON_KEY)));
		}
		if(record.containsKey(QMDLConstant.SCRIPT_JSON)){
			wrapper.setTestType(NVLayer3Utils.getStringFromJson(record.get(QMDLConstant.SCRIPT_JSON)).toUpperCase());
		}
	}

	private void addDlUlIntoWrapperFromJson(NVL3CsvDataWrapper wrapper, JSONObject record) {
		if (record.containsKey(QMDLConstant.AVG_DL_JSON_KEY)) {
			wrapper.setDlThroughPut(NVLayer3Utils.getDoubleFromJson(record.get(QMDLConstant.AVG_DL_JSON_KEY)));
		}
		if (record.containsKey(QMDLConstant.AVG_UL_JSON_KEY)) {
			wrapper.setUlThroughPut(NVLayer3Utils.getDoubleFromJson(record.get(QMDLConstant.AVG_UL_JSON_KEY)));
		}
		if (record.containsKey(QMDLConstant.DL_LINK_AVG_JSON)) {
			wrapper.setDlThroughPut(NVLayer3Utils.getDoubleFromJson(record.get(QMDLConstant.DL_LINK_AVG_JSON)));
		}
		if (record.containsKey(QMDLConstant.UL_LINK_AVG_JSON)) {
			wrapper.setUlThroughPut(NVLayer3Utils.getDoubleFromJson(record.get(QMDLConstant.UL_LINK_AVG_JSON)));
		}
	}

	private void addReportDataFromLiveJson(Layer3SummaryWrapper aggrigateWrapperData, NVL3CsvDataWrapper wrapper,
			JSONObject record) {
		
		if (wrapper.getTestType() != null
				&& wrapper.getTestType().equalsIgnoreCase(NetworkDataFormats.HTTP_LINK_DOWNLOAD)) {
			addHTTPLinkInformationFor4GIntoAggrigateWrapper(record, aggrigateWrapperData);
			}
		
		if (wrapper.getTestType() != null
				&& wrapper.getTestType().equalsIgnoreCase(NetworkDataFormats.SMS_TEST)) {
			addSMSInformationFor4GIntoAggrigateWrapper(record, aggrigateWrapperData);
			}
		

		if (wrapper.getTestType() != null
				&& wrapper.getTestType().equalsIgnoreCase(NetworkDataFormats.YOUTUBE_TEST)) {
			addYouTubeInformationFor4GIntoAggrigateWrapper(record, aggrigateWrapperData);
			}
		
		if (wrapper.getTestType() != null
				&& (wrapper.getTestType().equalsIgnoreCase(NetworkDataFormats.SHORT_CALL_TEST)||wrapper.getTestType().equalsIgnoreCase(NetworkDataFormats.LONG_CALL_TEST))) {
			addCallInformationFor4GIntoAggrigateWrapper(record, aggrigateWrapperData,wrapper.getTimeStamp());
			}
		
		
		if(wrapper.getTestType().equalsIgnoreCase(NetworkDataFormats.TEST_TYPE_FTP_UPLOAD)){
			wrapper.setFtpUl(wrapper.getUlThroughPut());
		}else{
			wrapper.setHttpUl(wrapper.getUlThroughPut());
		}
		
		if(wrapper.getTestType().equalsIgnoreCase(NetworkDataFormats.TEST_TYPE_FTP_DOWNLOAD)){
			wrapper.setFtpDl(wrapper.getDlThroughPut());
		}else{
			wrapper.setHttpDl(wrapper.getDlThroughPut());
		}
	}
	
	

	private static NVL3CsvDataWrapper setYouTube4GValues(JSONObject record) {
		NVL3CsvDataWrapper csvDataHolder =new NVL3CsvDataWrapper();
		if(record.containsKey(QMDLConstant.VIDEO_LINK_JSON)){
			csvDataHolder.setVideoURL(NVLayer3Utils.getStringFromJson(record.get(QMDLConstant.VIDEO_LINK_JSON)));
		}
		if(record.containsKey(QMDLConstant.VIDEO_RESOLUTION_JSON)){
		csvDataHolder.setVideoResolution(NVLayer3Utils.getStringFromJson(record.get(QMDLConstant.VIDEO_RESOLUTION_JSON)));
		}
		if(record.containsKey(QMDLConstant.VIDEO_DURATION_JSON)){
			csvDataHolder.setVideoDuration(NVLayer3Utils.getLongValueFromJson(record.get(QMDLConstant.VIDEO_DURATION_JSON)));
		}
		if(record.containsKey(QMDLConstant.NO_OF_STALLING_JSON)){	
		csvDataHolder.setNoOfStalling(NVLayer3Utils.getDoubleFromJson(record.get(QMDLConstant.NO_OF_STALLING_JSON)));
		}
		if(record.containsKey(QMDLConstant.TOTAL_BUFFERING_TIME_JSON)){	
		csvDataHolder.setTotalBufferTime(NVLayer3Utils.getDoubleFromJson(record.get(QMDLConstant.TOTAL_BUFFERING_TIME_JSON)));
		}
		if(record.containsKey(QMDLConstant.VIDEO_FREEZING_RATIO_JSON)){	
			csvDataHolder.setFreezingRatio(NVLayer3Utils.getStringFromJson(record.get(QMDLConstant.VIDEO_FREEZING_RATIO_JSON)));
		}
		if(record.containsKey(QMDLConstant.RSRP)){	
			csvDataHolder.setRsrp(NVLayer3Utils.getDoubleFromJson(record.get(QMDLConstant.RSRP)));
		}
		if(record.containsKey(QMDLConstant.SINR)){	
			csvDataHolder.setSinr(NVLayer3Utils.getDoubleFromJson(record.get(QMDLConstant.SINR)));
		}
		if(record.containsKey(QMDLConstant.ITERATION_JSON_KEY)){	
			csvDataHolder.setIteration(NVLayer3Utils.getIntegerFromJson(record.get(QMDLConstant.ITERATION_JSON_KEY)));
		}
		return csvDataHolder;
	}
	

	public static void addYouTubeInformationFor4GIntoAggrigateWrapper( JSONObject record, Layer3SummaryWrapper aggrigateWrapperData) {
		NVL3CsvDataWrapper csvDataHolder = setYouTube4GValues(record);
		if (csvDataHolder.getVideoURL() != null) {
			Map<String, YoutubeWrapper> youtubeMap = aggrigateWrapperData.getYouTubeTest() != null
					? aggrigateWrapperData.getYouTubeTest() : new HashMap<>();
			if (youtubeMap.containsKey(csvDataHolder.getVideoURL()+csvDataHolder.getIteration())) {
				YoutubeWrapper youtube = youtubeMap.get(csvDataHolder.getVideoURL()+csvDataHolder.getIteration());
				aggrigateYoutubeData(youtube, csvDataHolder);
			} else {
				YoutubeWrapper wrapper = aggrigateYoutubeData(new YoutubeWrapper(), csvDataHolder);
				youtubeMap.put(csvDataHolder.getVideoURL()+csvDataHolder.getIteration(), wrapper);
				aggrigateWrapperData.setYouTubeTest(youtubeMap);
			}
		}
	}

	private static YoutubeWrapper aggrigateYoutubeData(YoutubeWrapper youtube, NVL3CsvDataWrapper csvDataHolder) {
		youtube.setVideoURL(csvDataHolder.getVideoURL());
		youtube.setVideoDuration(csvDataHolder.getVideoDuration());
		youtube.setFreezingRatio(csvDataHolder.getFreezingRatio());
		youtube.setAvgRSRP(NVLayer3Utils.getDoubleArrayValue(youtube.getAvgRSRP(), csvDataHolder.getRsrp()));
		youtube.setAvgSINR(NVLayer3Utils.getDoubleArrayValue(youtube.getAvgSINR(), csvDataHolder.getSinr()));
		youtube.setNoOfStalling( csvDataHolder.getNoOfStalling());
		youtube.setTotalBufferTime(csvDataHolder.getTotalBufferTime());
		youtube.setVideoResolution(NVLayer3Utils.setValuesIntoSet(youtube.getVideoResolution(), csvDataHolder.getVideoResolution()));
		return youtube;
	}
	

	public static void addSMSInformationFor4GIntoAggrigateWrapper(
			JSONObject record, Layer3SummaryWrapper aggrigateWrapperData) {
		if (record.containsKey(QMDLConstant.SMS_ID_JSON)) {
			
			Integer smsId = NVLayer3Utils.getIntegerFromJson(record.get(QMDLConstant.SMS_ID_JSON));
			logger.debug("Getting msg {}",smsId);
			Map<Integer, NvSMSDetailWrapper> smsDetailMap = aggrigateWrapperData.getSmsData() != null ? aggrigateWrapperData.getSmsData() : new HashMap<>();
			if (smsDetailMap.containsKey(smsId)) {
				setValuesIntoSmsWrapper(smsDetailMap.get(smsId), record);
			} else {
				NvSMSDetailWrapper wrapper = new NvSMSDetailWrapper();
				setValuesIntoSmsWrapper(wrapper, record);
				smsDetailMap.put(smsId, wrapper);
			}
			aggrigateWrapperData.setSmsData(smsDetailMap);
		}
	}

	
	
	private static void setValuesIntoSmsWrapper(
			NvSMSDetailWrapper nvSMSDetailWrapper,JSONObject record) {
		if (record.containsKey(QMDLConstant.SMS_SENT_TIME_JSON)) {
			nvSMSDetailWrapper.setSendingTime(NVLayer3Utils.getLongValueFromJson(record.get(QMDLConstant.SMS_SENT_TIME_JSON)));
		}
		if (record.containsKey(QMDLConstant.SMS_DELIVERED_TIME_JSON)) {
			nvSMSDetailWrapper.setDeliveredTime(NVLayer3Utils.getLongValueFromJson(record.get(QMDLConstant.SMS_DELIVERED_TIME_JSON)));
		}
		if (record.containsKey(QMDLConstant.SMS_TOTAL_TIME_JSON)) {
			nvSMSDetailWrapper.setTotalTimeTaken(NVLayer3Utils.getLongValueFromJson(record.get(QMDLConstant.SMS_TOTAL_TIME_JSON)));
		}
		if (record.containsKey(QMDLConstant.SMS_STATUS_JSON)) {
			nvSMSDetailWrapper.setStatus(NVLayer3Utils.getStringFromJson(record.get(QMDLConstant.SMS_STATUS_JSON)));
		}
		if (record.containsKey(QMDLConstant.SMS_NE_STATUS_JSON)) {
			nvSMSDetailWrapper.setNeNtatus(NVLayer3Utils.getStringFromJson(record.get(QMDLConstant.SMS_NE_STATUS_JSON)));
		}
	}
	

	public static void addHTTPLinkInformationFor4GIntoAggrigateWrapper(
			JSONObject record, Layer3SummaryWrapper aggrigateWrapperData) {
	if(record.containsKey(QMDLConstant.TEST_STATUS_JSON)){
		logger.debug("Getting status {}  ",record.get(QMDLConstant.TEST_STATUS_JSON));
		aggrigateWrapperData.setTotalHTTPAttempt(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getTotalHTTPAttempt(),QMDLConstant.RECORD_COUNT));	
		if (record.containsKey(QMDLConstant.DL_LINK_AVG_JSON)) {
			aggrigateWrapperData.setTotalHTTPthroughPut(NVLayer3Utils.getDoubleArrayValue(aggrigateWrapperData.getTotalHTTPthroughPut(), NVLayer3Utils.getDoubleFromJson(record.get(QMDLConstant.DL_LINK_AVG_JSON))));
			
		}
		if(record.get(QMDLConstant.TEST_STATUS_JSON).toString().equalsIgnoreCase(QMDLConstant.SUCCESS)){		
			aggrigateWrapperData.setTotalHTTPSuccess(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getTotalHTTPSuccess(), QMDLConstant.RECORD_COUNT));
			if(record.containsKey(QMDLConstant.TOTAL_DLUL_TIME_JSON))
			aggrigateWrapperData.setTotalHTTPTimeTaken(NVLayer3Utils.addLongValue(aggrigateWrapperData.getTotalHTTPTimeTaken(), NVLayer3Utils.getLongValueFromJson(record.get(QMDLConstant.TOTAL_DLUL_TIME_JSON))));
			
		}
	}
	}


	private void setResposnseWaitTimeFromJson(Object object, NVL3CsvDataWrapper wrapper) {
		JSONObject browseJson = (JSONObject) object;
		if(browseJson.containsKey(QMDLConstant.RESPONSE_TIME_JSON_KEY)){
			wrapper.setResponseTime(NVLayer3Utils.getDoubleFromJson(browseJson.get(QMDLConstant.RESPONSE_TIME_JSON_KEY)));
		}
	}

	private void addNonQMDLDataIntoWrapper(JSONObject record, NVL3CsvDataWrapper wrapper) {
		if (record.containsKey(QMDLConstant.SINR)) {
			wrapper.setSinr(NVLayer3Utils.getDoubleFromJson(record.get(QMDLConstant.SINR)));
		}
		if (record.containsKey(QMDLConstant.RSRP)) {
			wrapper.setRsrp(NVLayer3Utils.getDoubleFromJson(record.get(QMDLConstant.RSRP)));
		}
		if (record.containsKey(QMDLConstant.RSRQ)) {
			wrapper.setRsrq(NVLayer3Utils.getDoubleFromJson(record.get(QMDLConstant.RSRQ)));
		}
		if (record.containsKey(QMDLConstant.RSSI)) {
			wrapper.setRssi(NVLayer3Utils.getDoubleFromJson(record.get(QMDLConstant.RSSI)));
		}
		if (record.containsKey(QMDLConstant.MNC)) {
			wrapper.setMnc(NVLayer3Utils.getIntegerFromJson(record.get(QMDLConstant.MNC)));
		}
		if (record.containsKey(QMDLConstant.MCC)) {
			wrapper.setMcc(NVLayer3Utils.getIntegerFromJson(record.get(QMDLConstant.MCC)));
		}
		if (record.containsKey(QMDLConstant.PCI_JSON_KEY)) {
			wrapper.setPci(NVLayer3Utils.getIntegerFromJson(record.get(QMDLConstant.PCI_JSON_KEY)));
		}
		if (record.containsKey(QMDLConstant.BAND)) {
			wrapper.setBand(NVLayer3Utils.getStringFromJson(record.get(QMDLConstant.BAND)));
		}

	}

	private void setLatencyAndJitterValueFromBrowseJson(Object browseObject, NVL3CsvDataWrapper wrapper) {
		JSONObject browseJson = (JSONObject) browseObject;
		if(browseJson.containsKey(QMDLConstant.LATENCY_JSON_KEY)){
			wrapper.setLatency(NVLayer3Utils.getDoubleFromJson(browseJson.get(QMDLConstant.LATENCY_JSON_KEY)));
		}
		if(browseJson.containsKey(QMDLConstant.JITTER_JSON_KEY)){
			wrapper.setJitter(NVLayer3Utils.getDoubleFromJson(browseJson.get(QMDLConstant.JITTER_JSON_KEY)));
		}
		if(browseJson.containsKey(QMDLConstant.PACKET_LOSS_LIVE_JSON_KEY)){
			wrapper.setPacketLoss(NVLayer3Utils.getDoubleFromJson(browseJson.get(QMDLConstant.PACKET_LOSS_LIVE_JSON_KEY)));
		}
	}


	public static void addCallInformationFor4GIntoAggrigateWrapper(
			JSONObject record, Layer3SummaryWrapper aggrigateWrapperData, Long timestamp) {
		if(record.containsKey(QMDLConstant.CALL_STATUS_JSON_KEY)&&timestamp!=null){				
		TreeMap<Long,String> callNetstatsMap = aggrigateWrapperData.getCallNetStatusMap()!=null?aggrigateWrapperData.getCallNetStatusMap():new TreeMap<>();
		callNetstatsMap.put(getKeyForCallMap(timestamp), NVLayer3Utils.getStringFromJson(record.get(QMDLConstant.CALL_STATUS_JSON_KEY)));
		aggrigateWrapperData.setCallNetStatusMap(callNetstatsMap);
		}	
	}

	private static Long getKeyForCallMap(Long timeStamp) {
		return NVLayer3Utils.getKeyForBean(timeStamp);
	}
	
}

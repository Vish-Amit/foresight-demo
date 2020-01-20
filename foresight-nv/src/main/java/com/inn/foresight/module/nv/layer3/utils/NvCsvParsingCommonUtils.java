package com.inn.foresight.module.nv.layer3.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.hadoop.hbase.client.Put;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.inn.commons.Symbol;
import com.inn.commons.lang.StringUtils;
import com.inn.foresight.module.nv.layer3.constants.QMDLConstant;
import com.inn.foresight.module.nv.layer3.qmdlParser.wrappers.Layer3SummaryWrapper;
import com.inn.foresight.module.nv.layer3.qmdlParser.wrappers.NVL3CsvDataWrapper;
import com.inn.foresight.module.nv.layer3.qmdlParser.wrappers.NvSMSDetailWrapper;
import com.inn.foresight.module.nv.layer3.wrapper.YoutubeWrapper;

public class NvCsvParsingCommonUtils {

	/** The logger. */
	private static Logger logger = LogManager.getLogger(NvCsvParsingCommonUtils.class);


	public static void addYoutubeInformationFor4GIntoPut(String[] lineData, Layer3SummaryWrapper aggrigateWrapperData) {
		NVL3CsvDataWrapper csvDataHolder = setYoutubeInfoIntoWrapper(lineData);
		if (csvDataHolder.getVideoURL() != null&& csvDataHolder.getIteration()!=null&&!csvDataHolder.getVideoURL().equals(QMDLConstant.FETCHING_VIDEO))  {
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
	
	public static void addYoutubeInformationFor3GIntoPut(String[] lineData, Layer3SummaryWrapper aggrigateWrapperData) {
		NVL3CsvDataWrapper csvDataHolder = set3GYoutubeInfoIntoWrapper(lineData);
		if (csvDataHolder.getVideoURL() != null&& csvDataHolder.getIteration()!=null&&!csvDataHolder.getVideoURL().equals(QMDLConstant.FETCHING_VIDEO))  {
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
	
	public static void addYoutubeInformationFor2GIntoPut(String[] lineData, Layer3SummaryWrapper aggrigateWrapperData) {
		NVL3CsvDataWrapper csvDataHolder = set2GYoutubeInfoIntoWrapper(lineData);
		if (csvDataHolder.getVideoURL() != null&& csvDataHolder.getIteration()!=null&&!csvDataHolder.getVideoURL().equals(QMDLConstant.FETCHING_VIDEO))  {
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

	private static NVL3CsvDataWrapper setYoutubeInfoIntoWrapper(String[] lineData) {
		NVL3CsvDataWrapper csvDataHolder=new NVL3CsvDataWrapper();
		setCommonsParameterOfCsv(lineData, csvDataHolder);
		setCommonValueByNetworkType(lineData, csvDataHolder);
		setYouTube4GValues(lineData, csvDataHolder);
		return csvDataHolder;
	}
	
	private static NVL3CsvDataWrapper set3GYoutubeInfoIntoWrapper(String[] lineData) {
		NVL3CsvDataWrapper csvDataHolder=new NVL3CsvDataWrapper();
		setCommonsParameterOfCsv(lineData, csvDataHolder);
		setCommonValueByNetworkType(lineData, csvDataHolder);
		setYouTube3GValues(lineData, csvDataHolder);
		return csvDataHolder;
	}
	
	
	private static NVL3CsvDataWrapper set2GYoutubeInfoIntoWrapper(String[] lineData) {
		NVL3CsvDataWrapper csvDataHolder=new NVL3CsvDataWrapper();
		setCommonsParameterOfCsv(lineData, csvDataHolder);
		setCommonValueByNetworkType(lineData, csvDataHolder);
		setYouTube2GValues(lineData, csvDataHolder);
		return csvDataHolder;
	}

	private static void setYouTube4GValues(String[] lineData, NVL3CsvDataWrapper csvDataHolder) {
		csvDataHolder.setVideoURL(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.YOUTUBE_URL_4G_INDEX));
		csvDataHolder.setVideoResolution(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.RESOLUTION_4G_INDEX));
		csvDataHolder.setVideoDuration(NVLayer3Utils.getLongFromCsv(lineData, NetworkDataFormats.DURATION_4G_INDEX));
		csvDataHolder.setNoOfStalling(NVLayer3Utils.getDoubleFromCsv(lineData, NetworkDataFormats.STALLING_4G_INDEX));
		csvDataHolder.setTotalBufferTime(NVLayer3Utils.getDoubleFromCsv(lineData, NetworkDataFormats.TOTAL_BUFFERING_4G_INDEX));
		csvDataHolder.setFreezingRatio(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.FREEZING_RATIO_4G_INDEX));
		csvDataHolder.setIteration(NVLayer3Utils.getInteger(lineData, NetworkDataFormats.YOUTUBE_ITERATION_INDEX));
		csvDataHolder.setYoutubeThroughPut(NVLayer3Utils.getDoubleFromCsv(lineData, NetworkDataFormats.YOUTUBE_DL_4G_INDEX));
		csvDataHolder.setVideoLoadTime(NVLayer3Utils.getLongFromCsv(lineData, NetworkDataFormats.YOUTUBE_LOAD_TIME_DL_4G_INDEX));
		
		if(NVLayer3Utils.getInteger(lineData, NetworkDataFormats.YOUTUBE_ITERATION_INDEX)!=null){
			csvDataHolder.setIteration(NVLayer3Utils.getInteger(lineData, NetworkDataFormats.YOUTUBE_ITERATION_INDEX));
			}else{
				csvDataHolder.setIteration(1);
			}
	}

	private static void setYouTube3GValues(String[] lineData, NVL3CsvDataWrapper csvDataHolder) {
		csvDataHolder.setVideoURL(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.YOUTUBE_URL_3G_INDEX));
		csvDataHolder.setVideoResolution(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.RESOLUTION_3G_INDEX));
		csvDataHolder.setVideoDuration(NVLayer3Utils.getLongFromCsv(lineData, NetworkDataFormats.DURATION_3G_INDEX));
		csvDataHolder.setNoOfStalling(NVLayer3Utils.getDoubleFromCsv(lineData, NetworkDataFormats.STALLING_3G_INDEX));
		csvDataHolder.setTotalBufferTime(NVLayer3Utils.getDoubleFromCsv(lineData, NetworkDataFormats.TOTAL_BUFFERING_3G_INDEX));
		csvDataHolder.setFreezingRatio(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.FREEZING_RATIO_3G_INDEX));
		csvDataHolder.setIteration(NVLayer3Utils.getInteger(lineData, NetworkDataFormats.YOUTUBE_ITERATION_3G_INDEX));
		csvDataHolder.setYoutubeThroughPut(NVLayer3Utils.getDoubleFromCsv(lineData, NetworkDataFormats.YOUTUBE_DL_3G_INDEX));
		csvDataHolder.setVideoLoadTime(NVLayer3Utils.getLongFromCsv(lineData, NetworkDataFormats.YOUTUBE_LOAD_TIME_DL_3G_INDEX));
		
		if(NVLayer3Utils.getInteger(lineData, NetworkDataFormats.YOUTUBE_ITERATION_3G_INDEX)!=null){
			csvDataHolder.setIteration(NVLayer3Utils.getInteger(lineData, NetworkDataFormats.YOUTUBE_ITERATION_3G_INDEX));
			}else{
				csvDataHolder.setIteration(1);
			}
	}
	
	
	private static void setYouTube2GValues(String[] lineData, NVL3CsvDataWrapper csvDataHolder) {
		csvDataHolder.setVideoURL(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.YOUTUBE_URL_2G_INDEX));
		csvDataHolder.setVideoResolution(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.RESOLUTION_2G_INDEX));
		csvDataHolder.setVideoDuration(NVLayer3Utils.getLongFromCsv(lineData, NetworkDataFormats.DURATION_2G_INDEX));
		csvDataHolder.setNoOfStalling(NVLayer3Utils.getDoubleFromCsv(lineData, NetworkDataFormats.STALLING_2G_INDEX));
		csvDataHolder.setTotalBufferTime(NVLayer3Utils.getDoubleFromCsv(lineData, NetworkDataFormats.TOTAL_BUFFERING_2G_INDEX));
		csvDataHolder.setFreezingRatio(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.FREEZING_RATIO_2G_INDEX));
		csvDataHolder.setIteration(NVLayer3Utils.getInteger(lineData, NetworkDataFormats.YOUTUBE_ITERATION_2G_INDEX));
		csvDataHolder.setYoutubeThroughPut(NVLayer3Utils.getDoubleFromCsv(lineData, NetworkDataFormats.YOUTUBE_DL_2G_INDEX));
		csvDataHolder.setVideoLoadTime(NVLayer3Utils.getLongFromCsv(lineData, NetworkDataFormats.YOUTUBE_LOAD_TIME_DL_2G_INDEX));
		
		if(NVLayer3Utils.getInteger(lineData, NetworkDataFormats.YOUTUBE_ITERATION_2G_INDEX)!=null){
			csvDataHolder.setIteration(NVLayer3Utils.getInteger(lineData, NetworkDataFormats.YOUTUBE_ITERATION_2G_INDEX));
			}else{
				csvDataHolder.setIteration(1);
			}
	}


	public static void addSMSInformationFor4GIntoAggrigateWrapper(String[] lineData,
			Layer3SummaryWrapper aggrigateWrapperData) {
		
		if (NVLayer3Utils.getInteger(lineData, NetworkDataFormats.SMS_ID_INDEX) != null) {
			Integer smsId = NVLayer3Utils.getInteger(lineData, NetworkDataFormats.SMS_ID_INDEX);
			Map<Integer, NvSMSDetailWrapper> smsDetailMap = aggrigateWrapperData.getSmsData() != null ? aggrigateWrapperData.getSmsData() : new HashMap<>();
			if (smsDetailMap.containsKey(smsId)) {
				setValuesIntoSmsWrapper(smsDetailMap.get(smsId), lineData);
			} else {
				NvSMSDetailWrapper wrapper = new NvSMSDetailWrapper();
				setValuesIntoSmsWrapper(wrapper, lineData);
				smsDetailMap.put(smsId, wrapper);
			}
			aggrigateWrapperData.setSmsData(smsDetailMap);
		}
	}
	
	public static void addSMSInformationFor3GIntoAggrigateWrapper(String[] lineData,
			Layer3SummaryWrapper aggrigateWrapperData) {
		
		if (NVLayer3Utils.getInteger(lineData, NetworkDataFormats.SMS_ID_3G_INDEX) != null) {
			Integer smsId = NVLayer3Utils.getInteger(lineData, NetworkDataFormats.SMS_ID_3G_INDEX);
			Map<Integer, NvSMSDetailWrapper> smsDetailMap = aggrigateWrapperData.getSmsData() != null ? aggrigateWrapperData.getSmsData() : new HashMap<>();
			if (smsDetailMap.containsKey(smsId)) {
				set3GValuesIntoSmsWrapper(smsDetailMap.get(smsId), lineData);
			} else {
				NvSMSDetailWrapper wrapper = new NvSMSDetailWrapper();
				set3GValuesIntoSmsWrapper(wrapper, lineData);
				smsDetailMap.put(smsId, wrapper);
			}
			aggrigateWrapperData.setSmsData(smsDetailMap);
		}
	}
	
	
	
	public static void addSMSInformationFor2GIntoAggrigateWrapper(String[] lineData,
			Layer3SummaryWrapper aggrigateWrapperData) {
		
		if (NVLayer3Utils.getInteger(lineData, NetworkDataFormats.SMS_ID_2G_INDEX) != null) {
			Integer smsId = NVLayer3Utils.getInteger(lineData, NetworkDataFormats.SMS_ID_2G_INDEX);
			Map<Integer, NvSMSDetailWrapper> smsDetailMap = aggrigateWrapperData.getSmsData() != null ? aggrigateWrapperData.getSmsData() : new HashMap<>();
			if (smsDetailMap.containsKey(smsId)) {
				set2GValuesIntoSmsWrapper(smsDetailMap.get(smsId), lineData);
			} else {
				NvSMSDetailWrapper wrapper = new NvSMSDetailWrapper();
				set2GValuesIntoSmsWrapper(wrapper, lineData);
				smsDetailMap.put(smsId, wrapper);
			}
			aggrigateWrapperData.setSmsData(smsDetailMap);
		}
	}

	public static void setSMSInformationIntoCsvWrapper(String[] lineData, NVL3CsvDataWrapper csvDataHolder) {
		NvSMSDetailWrapper nvSMSDetailWrapper=new NvSMSDetailWrapper();
		setValuesIntoSmsWrapper(nvSMSDetailWrapper, lineData);
		setSmsDetailIntoCsvWrapper(nvSMSDetailWrapper,csvDataHolder);
		String testStatus = NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.LTE_SMS_TEST_STATUS_INDEX);

		if (testStatus != null && testStatus.equals(QMDLConstant.TEST_SKIPPED)) {
			csvDataHolder.setTestStatus(testStatus);
			csvDataHolder.setFailureCause(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.LTE_SMS_FAILURE_CAUSE_INDEX));
		}
		csvDataHolder.setRemark(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.LTE_SMS_REMARK_INDEX));
		csvDataHolder.setNavigationStartMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.LTE_SMS_NAVIGATION_START_MESSAGE_INDEX));
		csvDataHolder.setNavigationStopMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.LTE_SMS_NAVIGATION_STOP_MESSAGE_INDEX));
	
	}

	private static void setSmsDetailIntoCsvWrapper(NvSMSDetailWrapper nvSMSDetailWrapper,
			NVL3CsvDataWrapper csvDataHolder) {
		if(nvSMSDetailWrapper.getSendingTime()!=null){
			csvDataHolder.setSmsAttempt(QMDLConstant.RECORD_COUNT);
		}
		if(nvSMSDetailWrapper.getStatus()!=null && nvSMSDetailWrapper.getStatus().equals(QMDLConstant.SMS_DELIVERED_STATUS)){
			csvDataHolder.setSmsSucess(QMDLConstant.RECORD_COUNT);
		}else if(nvSMSDetailWrapper.getStatus()!=null ){
			csvDataHolder.setSmsFailure(QMDLConstant.RECORD_COUNT);
		}

	}

	public static void set3GSMSInformationIntoCsvWrapper(String[] lineData, NVL3CsvDataWrapper csvDataHolder) {
		String testStatus = NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.SMS_TEST_STATUS_3G_INDEX);
		if (testStatus != null && testStatus.equals(QMDLConstant.TEST_SKIPPED)) {
			csvDataHolder.setTestStatus(testStatus);
			csvDataHolder.setFailureCause(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.SMS_FAILURE_CAUSE_3G_INDEX));
		}
		csvDataHolder.setRemark(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.SMS_REMARK_3G_INDEX));
		
		csvDataHolder.setNavigationStartMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.SMS_NAVIGATION_START_MESSAGE_3G_INDEX));
		csvDataHolder.setNavigationStopMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.SMS_NAVIGATION_STOP_MESSAGE_3G_INDEX));
	
	}
	
	
	public static void set2GSMSInformationIntoCsvWrapper(String[] lineData, NVL3CsvDataWrapper csvDataHolder) {
		String testStatus = NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.SMS_TEST_STATUS_2G_INDEX);
		if (testStatus != null && testStatus.equals(QMDLConstant.TEST_SKIPPED)) {
			csvDataHolder.setTestStatus(testStatus);
			csvDataHolder.setFailureCause(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.SMS_FAILURE_CAUSE_2G_INDEX));
		}
		csvDataHolder.setRemark(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.SMS_REMARK_2G_INDEX));
		csvDataHolder.setNavigationStartMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.SMS_NAVIGATION_START_MESSAGE_2G_INDEX));
		csvDataHolder.setNavigationStopMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.SMS_NAVIGATION_STOP_MESSAGE_2G_INDEX));
	
	}

	private static void setValuesIntoSmsWrapper(NvSMSDetailWrapper nvSMSDetailWrapper, String[] lineData) {
		
		if (NVLayer3Utils.getLongFromCsv(lineData, NetworkDataFormats.SEND_TIME_INDEX) != null) {
			nvSMSDetailWrapper.setSendingTime(NVLayer3Utils.getLongFromCsv(lineData, NetworkDataFormats.SEND_TIME_INDEX));
		}
		if (NVLayer3Utils.getLongFromCsv(lineData, NetworkDataFormats.DELIVERED_TIME_INDEX) != null) {
			nvSMSDetailWrapper.setDeliveredTime(NVLayer3Utils.getLongFromCsv(lineData, NetworkDataFormats.DELIVERED_TIME_INDEX));
		}
		if (NVLayer3Utils.getLongFromCsv(lineData, NetworkDataFormats.TOTAL_TIME_INDEX) != null) {
			nvSMSDetailWrapper.setTotalTimeTaken(NVLayer3Utils.getLongFromCsv(lineData, NetworkDataFormats.TOTAL_TIME_INDEX));
		}
		if (NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.SMS_STATUS_INDEX) != null) {
			nvSMSDetailWrapper.setStatus(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.SMS_STATUS_INDEX));
		}
		if (NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.SMS_NE_STATUS_INDEX) != null) {
			nvSMSDetailWrapper.setNeNtatus(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.SMS_NE_STATUS_INDEX));
		}
		if (NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.SMS_DIAL_NUMBER_INDEX) != null) {
			nvSMSDetailWrapper.setDailNumber(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.SMS_DIAL_NUMBER_INDEX));
			logger.debug("value of dial number in sms wrapper is{}",nvSMSDetailWrapper.getDailNumber());
		}
		String testStatus = NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.LTE_SMS_TEST_STATUS_INDEX);		
		if (testStatus != null && !testStatus.equals(QMDLConstant.TEST_SKIPPED)) {
			nvSMSDetailWrapper.setFailureCause(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.LTE_SMS_FAILURE_CAUSE_INDEX));
		}
		
	}
	
private static void set3GValuesIntoSmsWrapper(NvSMSDetailWrapper nvSMSDetailWrapper, String[] lineData) {
		
		if (NVLayer3Utils.getLongFromCsv(lineData, NetworkDataFormats.SEND_TIME_3G_INDEX) != null) {
			nvSMSDetailWrapper.setSendingTime(NVLayer3Utils.getLongFromCsv(lineData, NetworkDataFormats.SEND_TIME_3G_INDEX));
		}
		if (NVLayer3Utils.getLongFromCsv(lineData, NetworkDataFormats.DELIVERED_TIME_3G_INDEX) != null) {
			nvSMSDetailWrapper.setDeliveredTime(NVLayer3Utils.getLongFromCsv(lineData, NetworkDataFormats.DELIVERED_TIME_3G_INDEX));
		}
		if (NVLayer3Utils.getLongFromCsv(lineData, NetworkDataFormats.TOTAL_TIME_3G_INDEX) != null) {
			nvSMSDetailWrapper.setTotalTimeTaken(NVLayer3Utils.getLongFromCsv(lineData, NetworkDataFormats.TOTAL_TIME_3G_INDEX));
		}
		if (NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.SMS_STATUS_3G_INDEX) != null) {
			nvSMSDetailWrapper.setStatus(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.SMS_STATUS_3G_INDEX));
		}
		if (NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.SMS_NE_STATUS_3G_INDEX) != null) {
			nvSMSDetailWrapper.setNeNtatus(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.SMS_NE_STATUS_3G_INDEX));
		}
		if (NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.SMS_DIAL_NUMBER_3G_INDEX) != null) {
			nvSMSDetailWrapper.setDailNumber(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.SMS_DIAL_NUMBER_3G_INDEX));
			logger.debug("value of dial number in sms wrapper is{}",nvSMSDetailWrapper.getDailNumber());
		}
		String testStatus = NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.SMS_TEST_STATUS_3G_INDEX);		
		if (testStatus != null && !testStatus.equals(QMDLConstant.TEST_SKIPPED)) {
			nvSMSDetailWrapper.setFailureCause(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.SMS_FAILURE_CAUSE_3G_INDEX));
		}
		
	}
	
private static void set2GValuesIntoSmsWrapper(NvSMSDetailWrapper nvSMSDetailWrapper, String[] lineData) {
	
	if (NVLayer3Utils.getLongFromCsv(lineData, NetworkDataFormats.SEND_TIME_2G_INDEX) != null) {
		nvSMSDetailWrapper.setSendingTime(NVLayer3Utils.getLongFromCsv(lineData, NetworkDataFormats.SEND_TIME_2G_INDEX));
	}
	if (NVLayer3Utils.getLongFromCsv(lineData, NetworkDataFormats.DELIVERED_TIME_2G_INDEX) != null) {
		nvSMSDetailWrapper.setDeliveredTime(NVLayer3Utils.getLongFromCsv(lineData, NetworkDataFormats.DELIVERED_TIME_2G_INDEX));
	}
	if (NVLayer3Utils.getLongFromCsv(lineData, NetworkDataFormats.TOTAL_TIME_2G_INDEX) != null) {
		nvSMSDetailWrapper.setTotalTimeTaken(NVLayer3Utils.getLongFromCsv(lineData, NetworkDataFormats.TOTAL_TIME_2G_INDEX));
	}
	if (NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.SMS_STATUS_2G_INDEX) != null) {
		nvSMSDetailWrapper.setStatus(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.SMS_STATUS_2G_INDEX));
	}
	if (NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.SMS_NE_STATUS_2G_INDEX) != null) {
		nvSMSDetailWrapper.setNeNtatus(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.SMS_NE_STATUS_2G_INDEX));
	}
	if (NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.SMS_DIAL_NUMBER_2G_INDEX) != null) {
		nvSMSDetailWrapper.setDailNumber(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.SMS_DIAL_NUMBER_2G_INDEX));
		logger.debug("value of dial number in sms wrapper is{}",nvSMSDetailWrapper.getDailNumber());
	}
	String testStatus = NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.SMS_TEST_STATUS_2G_INDEX);		
	if (testStatus != null && !testStatus.equals(QMDLConstant.TEST_SKIPPED)) {
		nvSMSDetailWrapper.setFailureCause(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.SMS_FAILURE_CAUSE_2G_INDEX));
	}
	
}


	public static  String getCoverageValue(String newCoverage, String oldCoverage) {
		if(!StringUtils.isBlank(newCoverage)){
			return newCoverage;
		}
		return oldCoverage;
	}



	public static void setHeaderValuesIntoWrapper(Layer3SummaryWrapper aggrigateWrapperData, String row) {
		String[] rowValues = row.split(QMDLConstant.COMMA);
		if (rowValues.length > NetworkDataFormats.EARFCN_LOCK_INDEX) {
			aggrigateWrapperData.setImei(NVLayer3Utils.setValuesIntoSet(aggrigateWrapperData.getImei(),
					NVLayer3Utils.getStringFromCsv(rowValues, NetworkDataFormats.IMEI_INDEX)));
			aggrigateWrapperData.setMake(NVLayer3Utils.getStringFromCsv(rowValues,NetworkDataFormats.MAKE_INDEX));
			aggrigateWrapperData.setModel(NVLayer3Utils.getStringFromCsv(rowValues,NetworkDataFormats.MODEL_INDEX));
//			aggrigateWrapperData.setOperatorName(NVLayer3Utils.getStringFromCsv(rowValues,NetworkDataFormats.OPERATORNAME_INDEX));
			aggrigateWrapperData.setAppVersion(NVLayer3Utils.getStringFromCsv(rowValues,NetworkDataFormats.APPVERSION_INDEX));
			aggrigateWrapperData.setPlatform(NVLayer3Utils.getStringFromCsv(rowValues,NetworkDataFormats.ANDROID_VERSION_INDEX));
			aggrigateWrapperData.setMobileNumber(NVLayer3Utils.getStringFromCsv(rowValues,NetworkDataFormats.MOBILE_NUMBER_INDEX));
			aggrigateWrapperData.setPci(getBooleanValueFromCsv(NVLayer3Utils.getStringFromCsv(rowValues,NetworkDataFormats.PCI_LOCK_INDEX)));
			aggrigateWrapperData.setIsband(getBooleanValueFromCsv(NVLayer3Utils.getStringFromCsv(rowValues,NetworkDataFormats.BAND_LOCK_INDEX)));
			aggrigateWrapperData.setIsrat(getBooleanValueFromCsv(NVLayer3Utils.getStringFromCsv(rowValues,NetworkDataFormats.RAT_LOCK_INDEX)));
			aggrigateWrapperData.setIsearfcn(getBooleanValueFromCsv(NVLayer3Utils.getStringFromCsv(rowValues,NetworkDataFormats.EARFCN_LOCK_INDEX)));
			aggrigateWrapperData.setDeviceId(NVLayer3Utils.getStringFromCsv(rowValues,NetworkDataFormats.DEVICE_ID_INDEX));
			
			aggrigateWrapperData.setlPci(NVLayer3Utils.getIntegerFromCsv(rowValues,NetworkDataFormats.PCI_LOCK_VALUE_INDEX));
			aggrigateWrapperData.setlBand(NVLayer3Utils.getIntegerFromCsv(rowValues,NetworkDataFormats.BAND_LOCK_VALUE_INDEX));
			aggrigateWrapperData.setlRat(NVLayer3Utils.getStringFromCsv(rowValues,NetworkDataFormats.RAT_LOCK_VALUE_INDEX));
			aggrigateWrapperData.setlEarfcn(NVLayer3Utils.getIntegerFromCsv(rowValues,NetworkDataFormats.EARFCN_LOCK_VALUE_INDEX));	
	       
			aggrigateWrapperData.setTotalDistance(NVLayer3Utils.addDoubleValue(aggrigateWrapperData.getTotalDistance(),NVLayer3Utils.getDoubleFromCsv(rowValues,NetworkDataFormats.TOTAL_DISTANCE_INDEX)));
			aggrigateWrapperData.setBattaryInfo(NVLayer3Utils.getStringFromCsv(rowValues, NetworkDataFormats.BATTARY_INFO_INDEX));
			aggrigateWrapperData.setDriveVersion(NVLayer3Utils.getStringFromCsv(rowValues, NetworkDataFormats.DRIVE_VERSION_INDEX));
			aggrigateWrapperData.setChipsetInfo(NVLayer3Utils.getStringFromCsv(rowValues, NetworkDataFormats.CHIPSET_INFO_INDEX));
			
			aggrigateWrapperData.setThreshholdCause(NVLayer3Utils.getStringFromCsv(rowValues, NetworkDataFormats.THRESHHOLD_CAUSE_INDEX));
			aggrigateWrapperData.setRsrpThresholdValue(NVLayer3Utils.getStringFromCsv(rowValues, NetworkDataFormats.RSRP_THRESHOLD_VALUE_INDEX));
			aggrigateWrapperData.setSinrThresholdValue(NVLayer3Utils.getStringFromCsv(rowValues, NetworkDataFormats.SINR_THRESHOLD_VALUE_INDEX));
		}
		logger.debug("After setHeaderValuesIntoWrapper aggrigateWrapperData{}",new Gson().toJson(aggrigateWrapperData));
		
	}

	private static boolean getBooleanValueFromCsv(String value) {
		if(!StringUtils.isBlank(value)&&value.trim().equalsIgnoreCase(QMDLConstant.TRUE)){
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}



	public static void addImageInformationFor4GIntoPut(String[] lineData, Map<String, Put> imagePutMap, Layer3SummaryWrapper aggrigateWrapperData) {
	if(lineData.length>NetworkDataFormats.LTE_IMAGE_INDEX){
		String imageName = lineData[NetworkDataFormats.LTE_IMAGE_INDEX];
		if (imagePutMap.containsKey(imageName)) {
			NVL3CsvDataWrapper csvDataHolder = getCsvHolderForQuickTest(lineData);
			addQuickTestInfoForReport(aggrigateWrapperData,csvDataHolder);
			addQuickTestParamIntoPut(imagePutMap, csvDataHolder, imageName);
		}
	}
	}


	public static void addImageInformationFor3GIntoPut(String[] lineData, Map<String, Put> imagePutMap, Layer3SummaryWrapper aggrigateWrapperData) {
		if(lineData.length>NetworkDataFormats.QUICKTEST_IMAGE_3G_INDEX){
			String imageName = lineData[NetworkDataFormats.QUICKTEST_IMAGE_3G_INDEX];
			if (imagePutMap.containsKey(imageName)) {
				NVL3CsvDataWrapper csvDataHolder = getCsvHolderForQuickTest(lineData);
				addQuickTestInfoForReport(aggrigateWrapperData,csvDataHolder);
				addQuickTestParamIntoPut(imagePutMap, csvDataHolder, imageName);
			}
		}
		}
	
	public static void addImageInformationFor2GIntoPut(String[] lineData, Map<String, Put> imagePutMap, Layer3SummaryWrapper aggrigateWrapperData) {
		if(lineData.length>NetworkDataFormats.QUICKTEST_IMAGE_2G_INDEX){
			String imageName = lineData[NetworkDataFormats.QUICKTEST_IMAGE_2G_INDEX];
			if (imagePutMap.containsKey(imageName)) {
				NVL3CsvDataWrapper csvDataHolder = getCsvHolderForQuickTest(lineData);
				addQuickTestInfoForReport(aggrigateWrapperData,csvDataHolder);
				addQuickTestParamIntoPut(imagePutMap, csvDataHolder, imageName);
			}
		}
		}
	private static NVL3CsvDataWrapper getCsvHolderForQuickTest(String[] lineData) {
		NVL3CsvDataWrapper csvDataHolder=new NVL3CsvDataWrapper();
		setCommonsParameterOfCsv(lineData, csvDataHolder);
		setCommonValueByNetworkType(lineData, csvDataHolder);
		addQuickTestParam(lineData, csvDataHolder);
		return csvDataHolder;
	}


	private static void addQuickTestParam(String[] lineData, NVL3CsvDataWrapper csvDataHolder) {
		csvDataHolder.setDlThroughPut(NVLayer3Utils.getDoubleFromCsv(lineData, NetworkDataFormats.LTE_QUICKTEST_DL_INDEX));
		csvDataHolder.setUlThroughPut(NVLayer3Utils.getDoubleFromCsv(lineData, NetworkDataFormats.LTE_QUICKTEST_UL_INDEX));
		csvDataHolder.setLatency(NVLayer3Utils.getDoubleFromCsv(lineData, NetworkDataFormats.LTE_QUICKTEST_LATENCY_INDEX));
	}

	private static void addQuickTestParamIntoPut(Map<String, Put> imagePutMap, NVL3CsvDataWrapper csvDataHolder,
			String imageName) {
		Put put = imagePutMap.get(imageName);
		NVLayer3Utils.addStringToPut(put,csvDataHolder.getDlThroughPut() , QMDLConstant.DL_THROUGHTPUT,QMDLConstant.COLUMN_FAMILY);
		NVLayer3Utils.addStringToPut(put, csvDataHolder.getUlThroughPut(), QMDLConstant.UL_THROUGHTPUT,QMDLConstant.COLUMN_FAMILY);
		NVLayer3Utils.addStringToPut(put, csvDataHolder.getCi(), QMDLConstant.CELL_ID,QMDLConstant.COLUMN_FAMILY);
		NVLayer3Utils.addStringToPut(put, csvDataHolder.getPci(), QMDLConstant.PHYSICAL_CELL_ID,QMDLConstant.COLUMN_FAMILY);
		NVLayer3Utils.addStringToPut(put, csvDataHolder.getLat(), QMDLConstant.LATITUDE,	QMDLConstant.COLUMN_FAMILY);
		NVLayer3Utils.addStringToPut(put, csvDataHolder.getLon(), QMDLConstant.LONGITUDE,QMDLConstant.COLUMN_FAMILY);
		NVLayer3Utils.addStringToPut(put, csvDataHolder.getTimeStamp(), QMDLConstant.TIMESTAMP,QMDLConstant.COLUMN_FAMILY);
	}

	private static void addQuickTestInfoForReport(Layer3SummaryWrapper aggrigateWrapperData, NVL3CsvDataWrapper csvDataHolder) {
		StringBuilder quickTestJson = aggrigateWrapperData.getQuickTestJson()!=null?aggrigateWrapperData.getQuickTestJson().append(Symbol.BRACKET_OPEN_STRING):getInitialStringForQuickTest();
		NVLayer3Utils.addValueToStringBuilder(quickTestJson,csvDataHolder.getDlThroughPut());
		NVLayer3Utils.addValueToStringBuilder(quickTestJson, csvDataHolder.getUlThroughPut());
		NVLayer3Utils.addValueToStringBuilder(quickTestJson,csvDataHolder.getLatency());
		NVLayer3Utils.addValueToStringBuilder(quickTestJson, csvDataHolder.getRsrp());
		NVLayer3Utils.addValueToStringBuilder(quickTestJson, csvDataHolder.getSinr());
		quickTestJson.setLength(quickTestJson.length()-QMDLConstant.STRING_EXTRA_LENTH);
		quickTestJson.append(Symbol.BRACKET_CLOSE_STRING).append(Symbol.COMMA);
		aggrigateWrapperData.setQuickTestJson(quickTestJson);
	}

	private static StringBuilder getInitialStringForQuickTest() {
		StringBuilder json=new StringBuilder();
		json.append(Symbol.BRACKET_OPEN_STRING).append(Symbol.BRACKET_OPEN_STRING);
		return json;
	}

	public static void addNetworkTypeIntoAggrigateWrapper(Layer3SummaryWrapper aggrigateWrapperData,
			String[] lineData) {
		if (NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.NETWORK_TYPE_INDEX) != null
				&& !NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.NETWORK_TYPE_INDEX)
						.equalsIgnoreCase(NetworkDataFormats.NETWORK_TYPE_NONE)) {
			Set<String> operatorSet = NVLayer3Utils.setValuesIntoSet(aggrigateWrapperData.getOperator(),
					NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.NETWORK_TYPE_INDEX));
			aggrigateWrapperData.setOperator(operatorSet);
		}
	}

	public static void setCommonsParameterOfCsv(String[] lineData, NVL3CsvDataWrapper csvDataHolder) {
		csvDataHolder.setTimeStamp(NVLayer3Utils.getLongFromCsv(lineData, NetworkDataFormats.TIMESTAMP_INDEX));
		csvDataHolder.setTestType(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.TESTTYPE_INDEX));
		csvDataHolder.setNetworkType(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.NETWORK_TYPE_INDEX));
		csvDataHolder.setLat(NVLayer3Utils.getDoubleFromCsv(lineData, NetworkDataFormats.LATITUDE_INDEX));
		csvDataHolder.setLon(NVLayer3Utils.getDoubleFromCsv(lineData, NetworkDataFormats.LONGITUDE_INDEX));
		
		logger.debug("Getting common Params {}   {}",csvDataHolder.getNetworkType(),csvDataHolder.getTestType());
	}

	public static void setNetworkCommonsParameterOfCsv(String[] lineData, NVL3CsvDataWrapper csvDataHolder) {
		csvDataHolder.setMnc(NVLayer3Utils.getInteger(lineData, NetworkDataFormats.MNC_INDEX));
		csvDataHolder.setMcc(NVLayer3Utils.getInteger(lineData, NetworkDataFormats.MCC_INDEX));
		csvDataHolder.setCi(NVLayer3Utils.getInteger(lineData, NetworkDataFormats.CGI_ID_INDEX));
		csvDataHolder.setCellId(NVLayer3Utils.getInteger(lineData, NetworkDataFormats.CELL_ID_LTE_INDEX));
		csvDataHolder.seteNodeBId(NVLayer3Utils.getInteger(lineData, NetworkDataFormats.ENODEB_ID_INDEX));
	}

	public static void setLTECommonsParameterOfCsv(String[] lineData, NVL3CsvDataWrapper csvDataHolder) {
		setNetworkCommonsParameterOfCsv(lineData, csvDataHolder);
		csvDataHolder.setTac(NVLayer3Utils.getInteger(lineData, NetworkDataFormats.TAC_INDEX));
		csvDataHolder.setPci(NVLayer3Utils.getInteger(lineData, NetworkDataFormats.PCI_INDEX));
		csvDataHolder.setSinr(NVLayer3Utils.getDoubleFromCsv(lineData, NetworkDataFormats.SINR_INDEX));
		csvDataHolder.setRsrp(NVLayer3Utils.getDoubleFromCsv(lineData, NetworkDataFormats.RSRP_INDEX));
		csvDataHolder.setRsrq(NVLayer3Utils.getDoubleFromCsv(lineData, NetworkDataFormats.RSRQ_INDEX));
		csvDataHolder.setRssi(NVLayer3Utils.getDoubleFromCsv(lineData, NetworkDataFormats.RSSI_INDEX));
		csvDataHolder.setBand(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.BAND_INDEX));
		csvDataHolder.setEarfcn(NVLayer3Utils.getInteger(lineData, NetworkDataFormats.DLEARFCN_INDEX));
		csvDataHolder.setUlEarfcn(NVLayer3Utils.getLongFromCsv(lineData, NetworkDataFormats.ULEARFCN_INDEX));		
	}
	

	public static void set2G3GNetworkCommonsParameterOfCsv(String[] lineData, NVL3CsvDataWrapper csvDataHolder) {
		csvDataHolder.setMnc(NVLayer3Utils.getInteger(lineData, NetworkDataFormats.MNC_INDEX));
		csvDataHolder.setMcc(NVLayer3Utils.getInteger(lineData, NetworkDataFormats.MCC_INDEX));
		csvDataHolder.setCellId(NVLayer3Utils.getInteger(lineData, NetworkDataFormats.CELL_ID_INDEX));
	}

	private static void set3GCommonsParameterOfCsv(String[] lineData, NVL3CsvDataWrapper csvDataHolder) {
		set2G3GNetworkCommonsParameterOfCsv(lineData, csvDataHolder);
		csvDataHolder.setPsc(NVLayer3Utils.getIntegerFromCsv(lineData, NetworkDataFormats.PSC_3G_INDEX));
		csvDataHolder.setLac(NVLayer3Utils.getIntegerFromCsv(lineData, NetworkDataFormats.LAC_3G_INDEX));
		csvDataHolder.setRscp(NVLayer3Utils.getDoubleFromCsv(lineData, NetworkDataFormats.RSCP_3G_INDEX));
		csvDataHolder.setEcNo(NVLayer3Utils.getIntegerFromCsv(lineData, NetworkDataFormats.ECNO_3G_INDEX));
		csvDataHolder.setEcIO(NVLayer3Utils.getIntegerFromCsv(lineData, NetworkDataFormats.ECIO_3G_INDEX));

	}

	private static void set2GCommonsParameterOfCsv(String[] lineData, NVL3CsvDataWrapper csvDataHolder) {
		set2G3GNetworkCommonsParameterOfCsv(lineData, csvDataHolder);
		csvDataHolder.setLac(NVLayer3Utils.getIntegerFromCsv(lineData, NetworkDataFormats.LAC_2G_INDEX));
		csvDataHolder.setRxQuality(NVLayer3Utils.getIntegerFromCsv(lineData, NetworkDataFormats.RX_QUALITY_2G_INDEX));
		csvDataHolder.setRxLevel(NVLayer3Utils.getIntegerFromCsv(lineData, NetworkDataFormats.RX_LEVEL_2G_INDEX));

	}

	public static void setLTEDLParameterOfCsv(String[] lineData, NVL3CsvDataWrapper csvDataHolder) {
		csvDataHolder.setDlThroughPut(NVLayer3Utils.getDoubleFromCsv(lineData, NetworkDataFormats.DL_4G_INDEX));
		csvDataHolder.setRemark(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.REMARK_DL_4G_INDEX));
		csvDataHolder.setNavigationStartMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.NAVIGATION_START_MESSAGE_DL_4G_INDEX));
		csvDataHolder.setNavigationStopMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.NAVIGATION_STOP_MESSAGE_DL_4G_INDEX));
		
		csvDataHolder.setTestStatus(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.TESTSTATUS_DL_4G_INDEX));
		csvDataHolder.setFailureCause(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.FAILURE_CAUSE_DL_4G_INDEX));
		if(csvDataHolder.getTestStatus()!=null) {  
		
		if(QMDLConstant.SUCCESS.equalsIgnoreCase(csvDataHolder.getTestStatus())||
				QMDLConstant.FAIL.equalsIgnoreCase(csvDataHolder.getTestStatus())||
				QMDLConstant.REJECT.equalsIgnoreCase(csvDataHolder.getTestStatus())){
			csvDataHolder.setHttpAttempt(NVLayer3Utils.getInteger(lineData, NetworkDataFormats.HTTP_DOWNLOAD_ATTEMPT_INDEX));
		}
		

		csvDataHolder.setHttpSucess(NVLayer3Utils.getInteger(lineData, NetworkDataFormats.HTTP_DOWNLOAD_SUCCESS_INDEX));

		if(QMDLConstant.REJECT.equalsIgnoreCase(csvDataHolder.getTestStatus())){			
			csvDataHolder.setHttpDrop(NVLayer3Utils.getInteger(lineData, NetworkDataFormats.HTTP_DOWNLOAD_FAILURE_INDEX));
		}

		if(QMDLConstant.FAIL.equalsIgnoreCase(csvDataHolder.getTestStatus())){			
			csvDataHolder.setHttpFailure(NVLayer3Utils.getInteger(lineData, NetworkDataFormats.HTTP_DOWNLOAD_FAILURE_INDEX));
		}

		}
		csvDataHolder.setHttpDownLoadTime(NVLayer3Utils.getDoubleFromCsv(lineData, NetworkDataFormats.HTTP_DOWNLOAD_TIME_INDEX));

		if(NetworkDataFormats.TEST_TYPE_FTP_DOWNLOAD.equalsIgnoreCase(csvDataHolder.getTestType())){
			csvDataHolder.setFtpDl(csvDataHolder.getDlThroughPut());
		}else{
			csvDataHolder.setHttpDl(csvDataHolder.getDlThroughPut());


		}
	}
	
	public static void set3GDLParameterOfCsv(String[] lineData, NVL3CsvDataWrapper csvDataHolder) {
		csvDataHolder.setDlThroughPut(NVLayer3Utils.getDoubleFromCsv(lineData, NetworkDataFormats.DL_3G_INDEX));

		csvDataHolder.setRemark(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.REMARK_DL_3G_INDEX));
		csvDataHolder.setTestStatus(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.TESTSTATUS_DL_3G_INDEX));
		csvDataHolder.setFailureCause(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.FAILURE_CAUSE_DL_3G_INDEX));
		csvDataHolder.setNavigationStartMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.NAVIGATION_START_MESSAGE_DL_3G_INDEX));
		csvDataHolder.setNavigationStopMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.NAVIGATION_STOP_MESSAGE_DL_3G_INDEX));
		
		
		if(csvDataHolder.getTestStatus()!=null) {  

			if(QMDLConstant.SUCCESS.equalsIgnoreCase(csvDataHolder.getTestStatus())||
					QMDLConstant.FAIL.equalsIgnoreCase(csvDataHolder.getTestStatus())||
					QMDLConstant.REJECT.equalsIgnoreCase(csvDataHolder.getTestStatus())){		

				csvDataHolder.setHttpAttempt(NVLayer3Utils.getInteger(lineData, NetworkDataFormats.HTTP_DOWNLOAD_ATTEMPT_3G_INDEX));
			}
			csvDataHolder.setHttpSucess(NVLayer3Utils.getInteger(lineData, NetworkDataFormats.HTTP_DOWNLOAD_SUCCESS_3G_INDEX));

			if(QMDLConstant.REJECT.equalsIgnoreCase(csvDataHolder.getTestStatus())){			
				csvDataHolder.setHttpDrop(NVLayer3Utils.getInteger(lineData, NetworkDataFormats.HTTP_DOWNLOAD_FAILURE_3G_INDEX));
			}

			if(QMDLConstant.FAIL.equalsIgnoreCase(csvDataHolder.getTestStatus())){			
				csvDataHolder.setHttpFailure(NVLayer3Utils.getInteger(lineData, NetworkDataFormats.HTTP_DOWNLOAD_FAILURE_3G_INDEX));
			}

		}		
		
		csvDataHolder.setHttpDownLoadTime(NVLayer3Utils.getDoubleFromCsv(lineData, NetworkDataFormats.HTTP_DOWNLOAD_TIME_3G_INDEX));
		if(csvDataHolder.getTestType().equalsIgnoreCase(NetworkDataFormats.TEST_TYPE_FTP_DOWNLOAD)){
			csvDataHolder.setFtpDl(csvDataHolder.getDlThroughPut());
		}else{
			csvDataHolder.setHttpDl(csvDataHolder.getDlThroughPut());


		}
	}
	
	
	
	
	
	public static void set2GDLParameterOfCsv(String[] lineData, NVL3CsvDataWrapper csvDataHolder) {
		csvDataHolder.setDlThroughPut(NVLayer3Utils.getDoubleFromCsv(lineData, NetworkDataFormats.DL_2G_INDEX));
		logger.info("getting dl thrpt as: {}",csvDataHolder.getDlThroughPut());
		
		csvDataHolder.setRemark(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.REMARK_DL_2G_INDEX));
		csvDataHolder.setNavigationStartMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.NAVIGATION_START_MESSAGE_DL_2G_INDEX));
		csvDataHolder.setNavigationStopMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.NAVIGATION_STOP_MESSAGE_DL_2G_INDEX));
		
		csvDataHolder.setTestStatus(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.TESTSTATUS_DL_2G_INDEX));
		csvDataHolder.setFailureCause(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.FAILURE_CAUSE_DL_2G_INDEX));
		if(csvDataHolder.getTestStatus()!=null) {  
		
		if(QMDLConstant.SUCCESS.equalsIgnoreCase(csvDataHolder.getTestStatus())||
				QMDLConstant.FAIL.equalsIgnoreCase(csvDataHolder.getTestStatus())||
				QMDLConstant.REJECT.equalsIgnoreCase(csvDataHolder.getTestStatus())){
			csvDataHolder.setHttpAttempt(NVLayer3Utils.getInteger(lineData, NetworkDataFormats.HTTP_DOWNLOAD_ATTEMPT_2G_INDEX));
		}
		

		csvDataHolder.setHttpSucess(NVLayer3Utils.getInteger(lineData, NetworkDataFormats.HTTP_DOWNLOAD_SUCCESS_2G_INDEX));

		if(QMDLConstant.REJECT.equalsIgnoreCase(csvDataHolder.getTestStatus())){			
			csvDataHolder.setHttpDrop(NVLayer3Utils.getInteger(lineData, NetworkDataFormats.HTTP_DOWNLOAD_FAILURE_2G_INDEX));
		}

		if(QMDLConstant.FAIL.equalsIgnoreCase(csvDataHolder.getTestStatus())){			
			csvDataHolder.setHttpFailure(NVLayer3Utils.getInteger(lineData, NetworkDataFormats.HTTP_DOWNLOAD_FAILURE_2G_INDEX));
		}

		}
		csvDataHolder.setHttpDownLoadTime(NVLayer3Utils.getDoubleFromCsv(lineData, NetworkDataFormats.HTTP_DOWNLOAD_TIME_2G_INDEX));

		if(NetworkDataFormats.TEST_TYPE_FTP_DOWNLOAD.equalsIgnoreCase(csvDataHolder.getTestType())){
			csvDataHolder.setFtpDl(csvDataHolder.getDlThroughPut());
		}else{
			csvDataHolder.setHttpDl(csvDataHolder.getDlThroughPut());
		}
	}

	public static void setLTEULParameterOfCsv(String[] lineData, NVL3CsvDataWrapper csvDataHolder) {
		csvDataHolder.setUlThroughPut(NVLayer3Utils.getDoubleFromCsv(lineData, NetworkDataFormats.UL_4G_INDEX));
		csvDataHolder.setRemark(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.REMARK_UL_4G_INDEX));
		csvDataHolder.setTestStatus(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.TESTSTATUS_UL_4G_INDEX));
		csvDataHolder.setFailureCause(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.FAILURE_CAUSE_UL_4G_INDEX));
		csvDataHolder.setNavigationStartMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.NAVIGATION_START_MESSAGE_UL_4G_INDEX));
		csvDataHolder.setNavigationStopMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.NAVIGATION_STOP_MESSAGE_UL_4G_INDEX));
		
		csvDataHolder.setHttpAttempt(NVLayer3Utils.getInteger(lineData, NetworkDataFormats.HTTP_DOWNLOAD_ATTEMPT_INDEX));
		csvDataHolder.setHttpFailure(NVLayer3Utils.getInteger(lineData, NetworkDataFormats.HTTP_DOWNLOAD_FAILURE_INDEX));		
		csvDataHolder.setHttpSucess(NVLayer3Utils.getInteger(lineData, NetworkDataFormats.HTTP_DOWNLOAD_SUCCESS_INDEX));
		csvDataHolder.setHttpDownLoadTime(NVLayer3Utils.getDoubleFromCsv(lineData, NetworkDataFormats.HTTP_DOWNLOAD_TIME_INDEX));
		if(csvDataHolder.getTestType().equalsIgnoreCase(NetworkDataFormats.TEST_TYPE_FTP_UPLOAD)){
			csvDataHolder.setFtpUl(csvDataHolder.getUlThroughPut());
		}else{
			csvDataHolder.setHttpUl(csvDataHolder.getUlThroughPut());
		}
	}
	
	
	public static void set3GULParameterOfCsv(String[] lineData, NVL3CsvDataWrapper csvDataHolder) {
		csvDataHolder.setUlThroughPut(NVLayer3Utils.getDoubleFromCsv(lineData, NetworkDataFormats.DL_3G_INDEX));
		csvDataHolder.setRemark(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.REMARK_DL_3G_INDEX));
		csvDataHolder.setTestStatus(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.TESTSTATUS_DL_3G_INDEX));
		csvDataHolder.setFailureCause(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.FAILURE_CAUSE_DL_3G_INDEX));
		csvDataHolder.setNavigationStartMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.NAVIGATION_START_MESSAGE_DL_3G_INDEX));
		csvDataHolder.setNavigationStopMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.NAVIGATION_STOP_MESSAGE_DL_3G_INDEX));
		
		
		
		csvDataHolder.setHttpAttempt(NVLayer3Utils.getInteger(lineData, NetworkDataFormats.HTTP_UPLOAD_ATTEMPT_3G_INDEX));
		csvDataHolder.setHttpSucess(NVLayer3Utils.getInteger(lineData, NetworkDataFormats.HTTP_UPLOAD_SUCCESS_3G_INDEX));
		csvDataHolder.setHttpFailure(NVLayer3Utils.getInteger(lineData, NetworkDataFormats.HTTP_DOWNLOAD_FAILURE_3G_INDEX));		
		
		csvDataHolder.setHttpDownLoadTime(NVLayer3Utils.getDoubleFromCsv(lineData, NetworkDataFormats.HTTP_DOWNLOAD_TIME_3G_INDEX));
		if(csvDataHolder.getTestType().equalsIgnoreCase(NetworkDataFormats.TEST_TYPE_FTP_UPLOAD)){
			csvDataHolder.setFtpUl(csvDataHolder.getUlThroughPut());
		}else{
			csvDataHolder.setHttpUl(csvDataHolder.getUlThroughPut());
		}
	}
	
	
	public static void set2GULParameterOfCsv(String[] lineData, NVL3CsvDataWrapper csvDataHolder) {
		csvDataHolder.setUlThroughPut(NVLayer3Utils.getDoubleFromCsv(lineData, NetworkDataFormats.DL_2G_INDEX));
		csvDataHolder.setRemark(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.REMARK_DL_2G_INDEX));
		csvDataHolder.setTestStatus(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.TESTSTATUS_DL_2G_INDEX));
		csvDataHolder.setFailureCause(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.FAILURE_CAUSE_DL_2G_INDEX));
		
		csvDataHolder.setNavigationStartMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.NAVIGATION_START_MESSAGE_DL_2G_INDEX));
		csvDataHolder.setNavigationStopMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.NAVIGATION_STOP_MESSAGE_DL_2G_INDEX));
	
		csvDataHolder.setHttpAttempt(NVLayer3Utils.getInteger(lineData, NetworkDataFormats.HTTP_UPLOAD_ATTEMPT_2G_INDEX));
		csvDataHolder.setHttpSucess(NVLayer3Utils.getInteger(lineData, NetworkDataFormats.HTTP_UPLOAD_SUCCESS_2G_INDEX));
		csvDataHolder.setHttpFailure(NVLayer3Utils.getInteger(lineData, NetworkDataFormats.HTTP_DOWNLOAD_FAILURE_2G_INDEX));		
		
		csvDataHolder.setHttpDownLoadTime(NVLayer3Utils.getDoubleFromCsv(lineData, NetworkDataFormats.HTTP_DOWNLOAD_TIME_2G_INDEX));
		if(csvDataHolder.getTestType().equalsIgnoreCase(NetworkDataFormats.TEST_TYPE_FTP_UPLOAD)){
			csvDataHolder.setFtpUl(csvDataHolder.getUlThroughPut());
		}else{
			csvDataHolder.setHttpUl(csvDataHolder.getUlThroughPut());
		}
	}

	public static void setLTEBROWSEParameterOfCsv(String[] lineData, NVL3CsvDataWrapper csvDataHolder) {
		csvDataHolder.setResponseTime(NVLayer3Utils.getDoubleFromCsv(lineData, NetworkDataFormats.LTE_BROWSE_INDEX));
		csvDataHolder.setRemark(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.LTE_BROWSE_REMARK_INDEX));
		csvDataHolder.setTestStatus(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.LTE_BROWSE_TEST_STATUS_INDEX));
		csvDataHolder.setFailureCause(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.LTE_BROWSE_FAILURE_CAUSE_INDEX));
		
		csvDataHolder.setWptDns( NVLayer3Utils.getDoubleFromCsv(lineData, NetworkDataFormats.WPT_DNS_RESOLVE_TIME));
		csvDataHolder.setWptUrl(NVLayer3Utils.getDoubleFromCsv(lineData, NetworkDataFormats.WPT_URL_TIME));
		csvDataHolder.setBrowseUrl(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.BROWSE_URL_LTE_INDEX));
		
		csvDataHolder.setNavigationStartMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.NAVIGATION_START_MESSAGE_BROWSE_4G_INDEX));
		csvDataHolder.setNavigationStopMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.NAVIGATION_STOP_MESSAGE_BROWSE_4G_INDEX));
		
		
		
	}
	
	public static void set3GBROWSEParameterOfCsv(String[] lineData, NVL3CsvDataWrapper csvDataHolder) {
		csvDataHolder.setResponseTime(NVLayer3Utils.getDoubleFromCsv(lineData, NetworkDataFormats.BROWSE_3G_INDEX));
		csvDataHolder.setRemark(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.BROWSE_REMARK_3G_INDEX));
		csvDataHolder.setTestStatus(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.BROWSE_TEST_STATUS_3G_INDEX));
		csvDataHolder.setFailureCause(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.BROWSE_FAILURE_CAUSE_3G_INDEX));
		
		csvDataHolder.setWptDns( NVLayer3Utils.getDoubleFromCsv(lineData, NetworkDataFormats.WPT_DNS_RESOLVE_TIME_3G_INDEX));
		csvDataHolder.setWptUrl(NVLayer3Utils.getDoubleFromCsv(lineData, NetworkDataFormats.WPT_URL_TIME_3G_INDEX));
		csvDataHolder.setBrowseUrl(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.BROWSE_URL_3G_INDEX));
		
		csvDataHolder.setNavigationStartMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.NAVIGATION_START_MESSAGE_BROWSE_3G_INDEX));
		csvDataHolder.setNavigationStopMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.NAVIGATION_STOP_MESSAGE_BROWSE_3G_INDEX));
		

	}
	
	
	public static void set2GBROWSEParameterOfCsv(String[] lineData, NVL3CsvDataWrapper csvDataHolder) {
		csvDataHolder.setResponseTime(NVLayer3Utils.getDoubleFromCsv(lineData, NetworkDataFormats.BROWSE_2G_INDEX));
		csvDataHolder.setRemark(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.BROWSE_REMARK_2G_INDEX));
		csvDataHolder.setTestStatus(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.BROWSE_TEST_STATUS_2G_INDEX));
		csvDataHolder.setFailureCause(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.BROWSE_FAILURE_CAUSE_2G_INDEX));
		
		csvDataHolder.setNavigationStartMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.NAVIGATION_START_MESSAGE_BROWSE_2G_INDEX));
		csvDataHolder.setNavigationStopMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.NAVIGATION_STOP_MESSAGE_BROWSE_2G_INDEX));
	
		csvDataHolder.setWptDns( NVLayer3Utils.getDoubleFromCsv(lineData, NetworkDataFormats.WPT_DNS_RESOLVE_TIME_2G_INDEX));
		csvDataHolder.setWptUrl(NVLayer3Utils.getDoubleFromCsv(lineData, NetworkDataFormats.WPT_URL_TIME_2G_INDEX));
		csvDataHolder.setBrowseUrl(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.BROWSE_URL_2G_INDEX));

	}

	public static void setLTEPingParameterOfCsv(String[] lineData, NVL3CsvDataWrapper csvDataHolder) {
		csvDataHolder.setTestStatus(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.LTE_PING_TEST_STATUS_INDEX));
		csvDataHolder.setFailureCause(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.LTE_PING_FAILURE_CAUSE_INDEX));
		csvDataHolder.setRemark(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.LTE_PING_REMARK_INDEX));
		
		csvDataHolder.setNavigationStartMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.LTE_PING_NAVIGATION_START_MESSAGE_INDEX));
		csvDataHolder.setNavigationStopMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.LTE_PING_NAVIGATION_STOP_MESSAGE_INDEX));
		
		csvDataHolder.setLatency(NVLayer3Utils.getDoubleFromCsv(lineData, NetworkDataFormats.LTE_LATENCY_INDEX));
		csvDataHolder.setPingPacketLoss(NVLayer3Utils.getDoubleFromCsv(lineData, NetworkDataFormats.LTE_PING_PACKETLOSS_INDEX));
		csvDataHolder.setPingBufferSize(NVLayer3Utils.getIntegerFromCsv(lineData, NetworkDataFormats.BUFFER_SIZE_MESSAGE_PING_4G_INDEX));
		csvDataHolder.setJitter(NVLayer3Utils.getDoubleFromCsv(lineData, NetworkDataFormats.LTE_JITTER_INDEX));
		
	}
	
	public static void set3GPingParameterOfCsv(String[] lineData, NVL3CsvDataWrapper csvDataHolder) {
		csvDataHolder.setTestStatus(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.PING_TEST_STATUS_3G_INDEX));
		csvDataHolder.setFailureCause(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.PING_FAILURE_CAUSE_3G_INDEX));
		csvDataHolder.setRemark(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.PING_REMARK_3G_INDEX));
		
		csvDataHolder.setNavigationStartMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.PING_NAVIGATION_START_MESSAGE__3G_INDEX));
		csvDataHolder.setNavigationStopMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.PING_NAVIGATION_STOP_MESSAGE_3G_INDEX));
		
		csvDataHolder.setLatency(NVLayer3Utils.getDoubleFromCsv(lineData, NetworkDataFormats.LATENCY_3G_INDEX));
		csvDataHolder.setPingPacketLoss(NVLayer3Utils.getDoubleFromCsv(lineData, NetworkDataFormats.PING_PACKETLOSS_3G_INDEX));
		csvDataHolder.setPingBufferSize(NVLayer3Utils.getIntegerFromCsv(lineData, NetworkDataFormats.BUFFER_SIZE_MESSAGE_PING_3G_INDEX));
		csvDataHolder.setJitter(NVLayer3Utils.getDoubleFromCsv(lineData, NetworkDataFormats.JITTER_3G_INDEX));	
		
	}

	
	public static void set2GPingParameterOfCsv(String[] lineData, NVL3CsvDataWrapper csvDataHolder) {
		csvDataHolder.setTestStatus(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.PING_TEST_STATUS_2G_INDEX));
		csvDataHolder.setFailureCause(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.PING_FAILURE_CAUSE_2G_INDEX));
		csvDataHolder.setRemark(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.PING_REMARK_2G_INDEX));
		
		csvDataHolder.setNavigationStartMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.PING_NAVIGATION_START_MESSAGE__2G_INDEX));
		csvDataHolder.setNavigationStopMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.PING_NAVIGATION_STOP_MESSAGE_2G_INDEX));
		
		
		csvDataHolder.setLatency(NVLayer3Utils.getDoubleFromCsv(lineData, NetworkDataFormats.LATENCY_2G_INDEX));
		csvDataHolder.setPingPacketLoss(NVLayer3Utils.getDoubleFromCsv(lineData, NetworkDataFormats.PING_PACKETLOSS_2G_INDEX));
		csvDataHolder.setPingBufferSize(NVLayer3Utils.getIntegerFromCsv(lineData, NetworkDataFormats.BUFFER_SIZE_MESSAGE_PING_2G_INDEX));
		csvDataHolder.setJitter(NVLayer3Utils.getDoubleFromCsv(lineData, NetworkDataFormats.JITTER_2G_INDEX));
		
	}
	
	public static void setLTEJitterParameterOfCsv(String[] lineData, NVL3CsvDataWrapper csvDataHolder) {
		csvDataHolder.setJitter(NVLayer3Utils.getDoubleFromCsv(lineData, NetworkDataFormats.LTE_JITTER_INDEX));
		csvDataHolder.setPacketLoss(NVLayer3Utils.getDoubleFromCsv(lineData, NetworkDataFormats.LTE_PACKETLOSS_INDEX));
		
		csvDataHolder.setTestStatus(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.LTE_PING_TEST_STATUS_INDEX));
		csvDataHolder.setFailureCause(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.LTE_PING_FAILURE_CAUSE_INDEX));
		csvDataHolder.setRemark(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.LTE_PING_REMARK_INDEX));
		
		csvDataHolder.setNavigationStartMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.LTE_JITTER_NAVIGATION_START_MESSAGE_INDEX));
		csvDataHolder.setNavigationStopMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.LTE_JITTER_NAVIGATION_STOP_MESSAGE_INDEX));
	
	}
	
	public static void set3GJitterParameterOfCsv(String[] lineData, NVL3CsvDataWrapper csvDataHolder) {
		csvDataHolder.setJitter(NVLayer3Utils.getDoubleFromCsv(lineData, NetworkDataFormats.JITTER_3G_INDEX));
		csvDataHolder.setPacketLoss(NVLayer3Utils.getDoubleFromCsv(lineData, NetworkDataFormats.JITTER_PACKETLOSS_3G_INDEX));
		
		csvDataHolder.setTestStatus(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.JITTER_TEST_STATUS_3G_INDEX));
		csvDataHolder.setFailureCause(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.JITTER_FAILURE_CAUSE_3G_INDEX));
		csvDataHolder.setRemark(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.JITTER_REMARK_3G_INDEX));
		
		csvDataHolder.setNavigationStartMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.JITTER_NAVIGATION_START_MESSAGE__3G_INDEX));
		csvDataHolder.setNavigationStopMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.JITTER_NAVIGATION_STOP_MESSAGE_3G_INDEX));
	
	}
	
	public static void set2GJitterParameterOfCsv(String[] lineData, NVL3CsvDataWrapper csvDataHolder) {
		csvDataHolder.setJitter(NVLayer3Utils.getDoubleFromCsv(lineData, NetworkDataFormats.JITTER_2G_INDEX));
		csvDataHolder.setPacketLoss(NVLayer3Utils.getDoubleFromCsv(lineData, NetworkDataFormats.JITTER_PACKETLOSS_2G_INDEX));
		csvDataHolder.setTestStatus(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.JITTER_TEST_STATUS_2G_INDEX));
		csvDataHolder.setFailureCause(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.JITTER_FAILURE_CAUSE_2G_INDEX));
		csvDataHolder.setRemark(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.JITTER_REMARK_2G_INDEX));
		csvDataHolder.setNavigationStartMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.JITTER_NAVIGATION_START_MESSAGE__2G_INDEX));
		csvDataHolder.setNavigationStopMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.JITTER_NAVIGATION_STOP_MESSAGE_2G_INDEX));
		
	}

	public static void setCommonValueByNetworkType(String[] lineData, NVL3CsvDataWrapper csvDataHolder) {
		if (csvDataHolder.getNetworkType() == null) {
			return;
		}
		String networkType = csvDataHolder.getNetworkType().toUpperCase();
		csvDataHolder.setCoverage(networkType);
		switch (networkType) {
		case NetworkDataFormats.LTE:
			NvCsvParsingCommonUtils.setLTECommonsParameterOfCsv(lineData, csvDataHolder);
			break;
		case NetworkDataFormats._3G:
			NvCsvParsingCommonUtils.set3GCommonsParameterOfCsv(lineData, csvDataHolder);
			break;	
		case NetworkDataFormats._2G:
			NvCsvParsingCommonUtils.set2GCommonsParameterOfCsv(lineData, csvDataHolder);
			break;	
	
		case NetworkDataFormats.NONE:
			break;
		default:
			logger.debug("Getting Wrong operator Type {}",Arrays.toString(lineData));
		}
	}

	

	public static void addHTTPLinkInformationFor4GIntoAggrigateWrapper(String[] lineData,
			Layer3SummaryWrapper aggrigateWrapperData) {
		String status = NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.HTTP_LINK_STATUS_INDEX);
		if(status!=null) {
			//if(status.equals(QMDLConstant.TEST_SKIPPED)){
				aggrigateWrapperData.setTotalHTTPAttempt(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getTotalHTTPAttempt(), QMDLConstant.RECORD_COUNT));
				aggrigateWrapperData.setTotalHTTPthroughPut(NVLayer3Utils.getDoubleArrayValue(aggrigateWrapperData.getTotalHTTPthroughPut(), NVLayer3Utils.getDoubleFromCsv(lineData,NetworkDataFormats.DL_4G_INDEX)));
			//}
			if(status.equalsIgnoreCase(QMDLConstant.SUCCESS)){				
				aggrigateWrapperData.setTotalHTTPSuccess(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getTotalHTTPSuccess(), QMDLConstant.RECORD_COUNT));
				aggrigateWrapperData.setTotalHTTPTimeTaken(NVLayer3Utils.addLongValue(aggrigateWrapperData.getTotalHTTPTimeTaken(), NVLayer3Utils.getLongFromCsv(lineData, QMDLConstant.HTTP_TOTAL_TIME_TAKEN)));
			}
		}
	}
	

	public static void addHTTPLinkInformationFor3GIntoAggrigateWrapper(String[] lineData,
			Layer3SummaryWrapper aggrigateWrapperData) {
		String status = NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.HTTP_LINK_STATUS_3G_INDEX);
		if(status!=null && status.equals(QMDLConstant.TEST_SKIPPED)){
			aggrigateWrapperData.setTotalHTTPAttempt(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getTotalHTTPAttempt(), QMDLConstant.RECORD_COUNT));
			aggrigateWrapperData.setTotalHTTPthroughPut(NVLayer3Utils.getDoubleArrayValue(aggrigateWrapperData.getTotalHTTPthroughPut(), NVLayer3Utils.getDoubleFromCsv(lineData,NetworkDataFormats.DL_3G_INDEX)));
			if(status.equalsIgnoreCase(QMDLConstant.SUCCESS)){
				aggrigateWrapperData.setTotalHTTPSuccess(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getTotalHTTPSuccess(), QMDLConstant.RECORD_COUNT));
				aggrigateWrapperData.setTotalHTTPTimeTaken(NVLayer3Utils.addLongValue(aggrigateWrapperData.getTotalHTTPTimeTaken(), NVLayer3Utils.getLongFromCsv(lineData, NetworkDataFormats.HTTP_TOTAL_TIME_TAKEN_FOR_3G)));
			}
		}
	}

	
	
	public static void addHTTPLinkInformationFor2GIntoAggrigateWrapper(String[] lineData,
			Layer3SummaryWrapper aggrigateWrapperData) {
		String status = NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.HTTP_LINK_STATUS_2G_INDEX);
		if(status!=null && status.equals(QMDLConstant.TEST_SKIPPED)){
			aggrigateWrapperData.setTotalHTTPAttempt(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getTotalHTTPAttempt(), QMDLConstant.RECORD_COUNT));
			aggrigateWrapperData.setTotalHTTPthroughPut(NVLayer3Utils.getDoubleArrayValue(aggrigateWrapperData.getTotalHTTPthroughPut(), NVLayer3Utils.getDoubleFromCsv(lineData,NetworkDataFormats.DL_2G_INDEX)));
			if(status.equalsIgnoreCase(QMDLConstant.SUCCESS)){
				aggrigateWrapperData.setTotalHTTPSuccess(NVLayer3Utils.addIntegerValue(aggrigateWrapperData.getTotalHTTPSuccess(), QMDLConstant.RECORD_COUNT));
				aggrigateWrapperData.setTotalHTTPTimeTaken(NVLayer3Utils.addLongValue(aggrigateWrapperData.getTotalHTTPTimeTaken(), NVLayer3Utils.getLongFromCsv(lineData, NetworkDataFormats.HTTP_TOTAL_TIME_TAKEN_FOR_2G)));
			}
		}
	}

	public static void addCALLInformationFor4GIntoAggrigateWrapper(String[] lineData,
			Layer3SummaryWrapper aggrigateWrapperData) {
		if(NVLayer3Utils.getStringFromCsv(lineData, QMDLConstant.CALL_NE_STATUS_INDEX)!=null&&NVLayer3Utils.getLongFromCsv(lineData, NetworkDataFormats.TIMESTAMP_INDEX)!=null){
		TreeMap<Long,String> callNetstatsMap = aggrigateWrapperData.getCallNetStatusMap()!=null?aggrigateWrapperData.getCallNetStatusMap():new TreeMap<>();
		TreeMap<Long,String> callNetstatsSuccessMap = aggrigateWrapperData.getCallNetStatusOnSuccessMap()!=null?aggrigateWrapperData.getCallNetStatusOnSuccessMap():new TreeMap<>();
			if (NVLayer3Utils.getStringFromCsv(lineData, QMDLConstant.CALL_STATUS_INDEX) != null && NVLayer3Utils
					.getStringFromCsv(lineData, QMDLConstant.CALL_STATUS_INDEX).equals(QMDLConstant.CALL_END)) {
				callNetstatsSuccessMap.put(getKeyForCallMap(lineData),NVLayer3Utils.getStringFromCsv(lineData, QMDLConstant.CALL_NE_STATUS_INDEX));
			}
		callNetstatsMap.put(getKeyForCallMap(lineData), NVLayer3Utils.getStringFromCsv(lineData, QMDLConstant.CALL_NE_STATUS_INDEX));
		aggrigateWrapperData.setCallNetStatusMap(callNetstatsMap);
		aggrigateWrapperData.setCallNetStatusOnSuccessMap(callNetstatsSuccessMap);
		}	
	}
	
	
	public static void addCALLInformationFor3GIntoAggrigateWrapper(String[] lineData,
			Layer3SummaryWrapper aggrigateWrapperData) {
		if(NVLayer3Utils.getStringFromCsv(lineData, QMDLConstant.CALL_NE_STATUS_3G_INDEX)!=null&&NVLayer3Utils.getLongFromCsv(lineData, NetworkDataFormats.TIMESTAMP_INDEX)!=null){
			TreeMap<Long,String> callNetstatsMap = aggrigateWrapperData.getCallNetStatusMap()!=null?aggrigateWrapperData.getCallNetStatusMap():new TreeMap<>();
			TreeMap<Long,String> callNetstatsSuccessMap = aggrigateWrapperData.getCallNetStatusOnSuccessMap()!=null?aggrigateWrapperData.getCallNetStatusOnSuccessMap():new TreeMap<>();
			if (NVLayer3Utils.getStringFromCsv(lineData, QMDLConstant.CALL_NE_STATUS_3G_INDEX) != null 
					&& NVLayer3Utils.getStringFromCsv(lineData, QMDLConstant.CALL_STATUS_3G_INDEX)!=null && 
					NVLayer3Utils.getStringFromCsv(lineData, QMDLConstant.CALL_STATUS_3G_INDEX).equals(QMDLConstant.CALL_END)) {
				callNetstatsSuccessMap.put(getKeyForCallMap(lineData),NVLayer3Utils.getStringFromCsv(lineData, QMDLConstant.CALL_NE_STATUS_3G_INDEX));
			}
			callNetstatsMap.put(getKeyForCallMap(lineData), NVLayer3Utils.getStringFromCsv(lineData, QMDLConstant.CALL_NE_STATUS_3G_INDEX));
			aggrigateWrapperData.setCallNetStatusMap(callNetstatsMap);
			aggrigateWrapperData.setCallNetStatusOnSuccessMap(callNetstatsSuccessMap);
		}	
	}
	
	
	
	public static void addCALLInformationFor2GIntoAggrigateWrapper(String[] lineData,
			Layer3SummaryWrapper aggrigateWrapperData) {
		if(NVLayer3Utils.getStringFromCsv(lineData, QMDLConstant.CALL_NE_STATUS_2G_INDEX)!=null&&NVLayer3Utils.getLongFromCsv(lineData, NetworkDataFormats.TIMESTAMP_INDEX)!=null){
		TreeMap<Long,String> callNetstatsMap = aggrigateWrapperData.getCallNetStatusMap()!=null?aggrigateWrapperData.getCallNetStatusMap():new TreeMap<>();
		TreeMap<Long,String> callNetstatsSuccessMap = aggrigateWrapperData.getCallNetStatusOnSuccessMap()!=null?aggrigateWrapperData.getCallNetStatusOnSuccessMap():new TreeMap<>();
		if (NVLayer3Utils.getStringFromCsv(lineData, QMDLConstant.CALL_NE_STATUS_2G_INDEX) != null 
				&& NVLayer3Utils.getStringFromCsv(lineData, QMDLConstant.CALL_STATUS_2G_INDEX)!=null && 
				NVLayer3Utils.getStringFromCsv(lineData, QMDLConstant.CALL_STATUS_2G_INDEX).equals(QMDLConstant.CALL_END)) {
			callNetstatsSuccessMap.put(getKeyForCallMap(lineData),NVLayer3Utils.getStringFromCsv(lineData, QMDLConstant.CALL_NE_STATUS_2G_INDEX));
		}
		callNetstatsMap.put(getKeyForCallMap(lineData), NVLayer3Utils.getStringFromCsv(lineData, QMDLConstant.CALL_NE_STATUS_2G_INDEX));
		aggrigateWrapperData.setCallNetStatusMap(callNetstatsMap);
		aggrigateWrapperData.setCallNetStatusOnSuccessMap(callNetstatsSuccessMap);
		}	
	}

	private static Long getKeyForCallMap(String[] lineData) {
		return NVLayer3Utils.getKeyForBean(NVLayer3Utils.getLongFromCsv(lineData, NetworkDataFormats.TIMESTAMP_INDEX));
	}
	
	public static void addHandoverCauseInformationFor4G(String[] lineData,Layer3SummaryWrapper aggrigateWrapperData){
		if(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.OLD_PCI_4G_INDEX)!=null&&NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.EVENT_CAUSE_4G_INDEX)!=null){
			StringBuilder handoverJson=aggrigateWrapperData.getHandoverJson()!=null?aggrigateWrapperData.getHandoverJson().append(Symbol.BRACKET_OPEN_STRING):getInitialStringForQuickTest();
			NVLayer3Utils.addValueToStringBuilder(handoverJson, NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.OLD_PCI_4G_INDEX));
			NVLayer3Utils.addValueToStringBuilder(handoverJson, NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.EVENT_CAUSE_4G_INDEX));
			handoverJson.setLength(handoverJson.length()-QMDLConstant.STRING_EXTRA_LENTH);
			handoverJson.append(Symbol.BRACKET_CLOSE_STRING).append(Symbol.COMMA);
			aggrigateWrapperData.setHandoverJson(handoverJson);
		}
	}

	public static void addQuickTestParameterOfCsv(String[] lineData, NVL3CsvDataWrapper csvDataHolder) {
		csvDataHolder.setTestStatus(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.LTE_QUICKTEST_TEST_STATUS_INDEX));
		csvDataHolder.setFailureCause(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.LTE_QUICKTEST_FAILURE_CAUSE_INDEX));
		csvDataHolder.setRemark(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.LTE_QUICKTEST_REMARK_INDEX));	
		csvDataHolder.setNavigationStartMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.LTE_QUICKTEST_NAVIGATION_START_MESSAGE_INDEX));
		csvDataHolder.setNavigationStopMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.LTE_QUICKTEST_NAVIGATION_STOP_MESSAGE_INDEX));
	
	}
	
	public static void add3GQuickTestParameterOfCsv(String[] lineData, NVL3CsvDataWrapper csvDataHolder) {
		csvDataHolder.setTestStatus(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.QUICKTEST_TEST_STATUS_3G_INDEX));
		csvDataHolder.setFailureCause(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.QUICKTEST_FAILURE_CAUSE_3G_INDEX));
		csvDataHolder.setRemark(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.QUICKTEST_REMARK_3G_INDEX));	
		
		csvDataHolder.setNavigationStartMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.QUICKTEST_NAVIGATION_START_MESSAGE_3G_INDEX));
		csvDataHolder.setNavigationStopMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.QUICKTEST_NAVIGATION_STOP_MESSAGE_3G_INDEX));

	}
	
	public static void add2GQuickTestParameterOfCsv(String[] lineData, NVL3CsvDataWrapper csvDataHolder) {
		csvDataHolder.setTestStatus(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.QUICKTEST_TEST_STATUS_2G_INDEX));
		csvDataHolder.setFailureCause(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.QUICKTEST_FAILURE_CAUSE_2G_INDEX));
		csvDataHolder.setRemark(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.QUICKTEST_REMARK_2G_INDEX));
		
		csvDataHolder.setNavigationStartMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.QUICKTEST_NAVIGATION_START_MESSAGE_2G_INDEX));
		csvDataHolder.setNavigationStopMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.QUICKTEST_NAVIGATION_STOP_MESSAGE_2G_INDEX));

	}

	public static void addYouTubeTestParameterOfCsv(String[] lineData, NVL3CsvDataWrapper csvDataHolder) {
		csvDataHolder.setTestStatus(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.LTE_YOUTUBE_TEST_STATUS_INDEX));
		csvDataHolder.setFailureCause(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.LTE_YOUTUBE_FAILURE_CAUSE_INDEX));
		csvDataHolder.setRemark(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.LTE_YOUTUBE_REMARK_INDEX));
		csvDataHolder.setNavigationStartMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.LTE_YOUTUBE_NAVIGATION_START_MESSAGE_INDEX));
		csvDataHolder.setNavigationStopMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.LTE_YOUTUBE_NAVIGATION_STOP_MESSAGE_INDEX));
	
		setYouTube4GValues(lineData, csvDataHolder);
		}
	public static void add3GYouTubeTestParameterOfCsv(String[] lineData, NVL3CsvDataWrapper csvDataHolder) {
		csvDataHolder.setTestStatus(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.YOUTUBE_TEST_STATUS_3G_INDEX));
		csvDataHolder.setFailureCause(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.YOUTUBE_FAILURE_CAUSE_3G_INDEX));
		csvDataHolder.setRemark(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.YOUTUBE_REMARK_3G_INDEX));
		csvDataHolder.setNavigationStartMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.YOUTUBE_NAVIGATION_START_MESSAGE_3G_INDEX));
		csvDataHolder.setNavigationStopMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.YOUTUBE_NAVIGATION_STOP_MESSAGE_3G_INDEX));
	
		setYouTube3GValues(lineData, csvDataHolder);
		}
	
	
	public static void add2GYouTubeTestParameterOfCsv(String[] lineData, NVL3CsvDataWrapper csvDataHolder) {
		csvDataHolder.setTestStatus(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.YOUTUBE_TEST_STATUS_2G_INDEX));
		csvDataHolder.setFailureCause(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.YOUTUBE_FAILURE_CAUSE_2G_INDEX));
		csvDataHolder.setRemark(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.YOUTUBE_REMARK_2G_INDEX));
		csvDataHolder.setNavigationStartMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.YOUTUBE_NAVIGATION_START_MESSAGE_2G_INDEX));
		csvDataHolder.setNavigationStopMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.YOUTUBE_NAVIGATION_STOP_MESSAGE_2G_INDEX));
	
		setYouTube2GValues(lineData, csvDataHolder);
		}

	public static void setLTEHTTPDLParameterOfCsv(String[] lineData, NVL3CsvDataWrapper csvDataHolder) {
		String testStatus = NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.HTTP_LINK_STATUS_INDEX);
		if (testStatus != null && testStatus.equals(QMDLConstant.TEST_SKIPPED)) {
			csvDataHolder.setTestStatus(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.HTTP_LINK_STATUS_INDEX));
			csvDataHolder.setFailureCause(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.HTTP_LINK_FAILURE_CAUSE_4G_INDEX));
		}
		csvDataHolder.setDlThroughPut(NVLayer3Utils.getDoubleFromCsv(lineData, NetworkDataFormats.DL_4G_INDEX));
		csvDataHolder.setRemark(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.REMARK_HTTP_DL_4G_INDEX));
		csvDataHolder.setNavigationStartMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.LTE_HTTP_DL_NAVIGATION_START_MESSAGE_INDEX));
		csvDataHolder.setNavigationStopMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.LTE_HTTP_DL_NAVIGATION_STOP_MESSAGE_INDEX));
	
		if (csvDataHolder.getTestType().equalsIgnoreCase(NetworkDataFormats.TEST_TYPE_FTP_DOWNLOAD)) {
			csvDataHolder.setFtpDl(csvDataHolder.getDlThroughPut());
		} else {
			csvDataHolder.setHttpDl(csvDataHolder.getDlThroughPut());
		}

	}
	
	public static void set3GHTTPDLParameterOfCsv(String[] lineData, NVL3CsvDataWrapper csvDataHolder) {
		String testStatus = NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.HTTP_LINK_STATUS_3G_INDEX);
		if (testStatus != null && testStatus.equals(QMDLConstant.TEST_SKIPPED)) {
			csvDataHolder.setTestStatus(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.HTTP_LINK_STATUS_3G_INDEX));
			csvDataHolder.setFailureCause(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.HTTP_LINK_FAILURE_CAUSE_3G_INDEX));
		}
		csvDataHolder.setDlThroughPut(NVLayer3Utils.getDoubleFromCsv(lineData, NetworkDataFormats.DL_3G_INDEX));
		csvDataHolder.setRemark(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.REMARK_HTTP_DL_3G_INDEX));
		if (csvDataHolder.getTestType().equalsIgnoreCase(NetworkDataFormats.TEST_TYPE_FTP_DOWNLOAD)) {
			csvDataHolder.setFtpDl(csvDataHolder.getDlThroughPut());
		} else {
			csvDataHolder.setHttpDl(csvDataHolder.getDlThroughPut());
		}

		csvDataHolder.setNavigationStartMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.HTTP_DL_NAVIGATION_START_MESSAGE_3G_INDEX));
		csvDataHolder.setNavigationStopMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.HTTP_DL_NAVIGATION_STOP_MESSAGE_3G_INDEX));
	
	}

	
	public static void set2GHTTPDLParameterOfCsv(String[] lineData, NVL3CsvDataWrapper csvDataHolder) {
		String testStatus = NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.HTTP_LINK_STATUS_2G_INDEX);
		if (testStatus != null && testStatus.equals(QMDLConstant.TEST_SKIPPED)) {
			csvDataHolder.setTestStatus(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.HTTP_LINK_STATUS_2G_INDEX));
			csvDataHolder.setFailureCause(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.HTTP_LINK_FAILURE_CAUSE_2G_INDEX));
		}
		csvDataHolder.setDlThroughPut(NVLayer3Utils.getDoubleFromCsv(lineData, NetworkDataFormats.DL_2G_INDEX));
		csvDataHolder.setRemark(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.REMARK_HTTP_DL_2G_INDEX));
		if (csvDataHolder.getTestType().equalsIgnoreCase(NetworkDataFormats.TEST_TYPE_FTP_DOWNLOAD)) {
			csvDataHolder.setFtpDl(csvDataHolder.getDlThroughPut());
		} else {
			csvDataHolder.setHttpDl(csvDataHolder.getDlThroughPut());
		}

		csvDataHolder.setNavigationStartMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.HTTP_DL_NAVIGATION_START_MESSAGE_2G_INDEX));
		csvDataHolder.setNavigationStopMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.HTTP_DL_NAVIGATION_STOP_MESSAGE_2G_INDEX));
	}

	public static void setCALLInformationIntoCsvWrapper(String[] lineData, NVL3CsvDataWrapper csvDataHolder) {
		csvDataHolder.setTestStatus(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.LTE_CALL_TEST_STATUS_INDEX));
		csvDataHolder.setFailureCause(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.LTE_CALL_FAILURE_CAUSE_INDEX));
		csvDataHolder.setRemark(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.LTE_CALL_REMARK_INDEX));		
		csvDataHolder.setNavigationStartMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.LTE_CALL_NAVIGATION_START_MESSAGE_INDEX));
		csvDataHolder.setNavigationStopMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.LTE_CALL_NAVIGATION_STOP_MESSAGE_INDEX));
		csvDataHolder.setpMos(NVLayer3Utils.getDoubleFromCsv(lineData, NetworkDataFormats.LTE_CALL_PMOS_INDEX));
		
	}

	public static void set3GCALLInformationIntoCsvWrapper(String[] lineData, NVL3CsvDataWrapper csvDataHolder) {
		csvDataHolder.setTestStatus(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.CALL_TEST_STATUS_3G_INDEX));
		csvDataHolder.setFailureCause(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.CALL_FAILURE_CAUSE_3G_INDEX));
		csvDataHolder.setRemark(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.CALL_REMARK_3G_INDEX));		
		csvDataHolder.setNavigationStartMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.CALL_NAVIGATION_START_MESSAGE_3G_INDEX));
		csvDataHolder.setNavigationStopMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.CALL_NAVIGATION_STOP_MESSAGE_3G_INDEX));
		csvDataHolder.setpMos(NVLayer3Utils.getDoubleFromCsv(lineData, NetworkDataFormats.CALL_PMOS_3G_INDEX));
		
	}
	
	public static void set2GCALLInformationIntoCsvWrapper(String[] lineData, NVL3CsvDataWrapper csvDataHolder) {
		csvDataHolder.setTestStatus(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.CALL_TEST_STATUS_2G_INDEX));
		csvDataHolder.setFailureCause(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.CALL_FAILURE_CAUSE_2G_INDEX));
		csvDataHolder.setRemark(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.CALL_REMARK_2G_INDEX));		
		csvDataHolder.setNavigationStartMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.CALL_NAVIGATION_START_MESSAGE_2G_INDEX));
		csvDataHolder.setNavigationStopMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.CALL_NAVIGATION_STOP_MESSAGE_2G_INDEX));
		csvDataHolder.setpMos(NVLayer3Utils.getDoubleFromCsv(lineData, NetworkDataFormats.CALL_PMOS_2G_INDEX));
		
	}

	public static void setIDLEInformationIntoCsvWrapper(String[] lineData, NVL3CsvDataWrapper csvDataHolder) {
		csvDataHolder.setRemark(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.LTE_IDLE_REMARK_INDEX));
		csvDataHolder.setNavigationStartMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.LTE_IDLE_NAVIGATION_START_MESSAGE_INDEX));
		csvDataHolder.setNavigationStopMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.LTE_IDLE_NAVIGATION_STOP_MESSAGE_INDEX));
	
	}
	
	public static void set3GIDLEInformationIntoCsvWrapper(String[] lineData, NVL3CsvDataWrapper csvDataHolder) {
		csvDataHolder.setRemark(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.IDLE_REMARK_3G_INDEX));		
		csvDataHolder.setNavigationStartMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.IDLE_NAVIGATION_START_MESSAGE_3G_INDEX));
		csvDataHolder.setNavigationStopMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.IDLE_NAVIGATION_STOP_MESSAGE_3G_INDEX));
	
	}
	
	public static void set2GIDLEInformationIntoCsvWrapper(String[] lineData, NVL3CsvDataWrapper csvDataHolder) {
		csvDataHolder.setRemark(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.IDLE_REMARK_2G_INDEX));
		csvDataHolder.setNavigationStartMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.IDLE_NAVIGATION_START_MESSAGE_2G_INDEX));
		csvDataHolder.setNavigationStopMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.IDLE_NAVIGATION_STOP_MESSAGE_2G_INDEX));
	
	}

	public static void setAttachDetachInformationIntoCsvWrapper(String[] lineData, NVL3CsvDataWrapper csvDataHolder) {
		csvDataHolder.setTestStatus(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.LTE_ATTACH_TEST_STATUS_INDEX));
		csvDataHolder.setFailureCause(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.LTE_ATTACH_FAILURE_CAUSE_INDEX));
		csvDataHolder.setRemark(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.LTE_ATTACH_REMARK_INDEX));
		csvDataHolder.setNavigationStartMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.LTE_ATTACH_NAVIGATION_START_MESSAGE_INDEX));
		csvDataHolder.setNavigationStopMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.LTE_ATTACH_NAVIGATION_STOP_MESSAGE_INDEX));
	
	}
	
	public static void set3GAttachDetachInformationIntoCsvWrapper(String[] lineData, NVL3CsvDataWrapper csvDataHolder) {
		csvDataHolder.setTestStatus(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.ATTACH_TEST_STATUS_3G_INDEX));
		csvDataHolder.setFailureCause(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.ATTACH_FAILURE_CAUSE_3G_INDEX));
		csvDataHolder.setRemark(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.ATTACH_REMARK_3G_INDEX));		
		csvDataHolder.setNavigationStartMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.ATTACH_NAVIGATION_START_MESSAGE_3G_INDEX));
		csvDataHolder.setNavigationStopMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.ATTACH_NAVIGATION_STOP_MESSAGE_3G_INDEX));
	
	}
	
	public static void set2GAttachDetachInformationIntoCsvWrapper(String[] lineData, NVL3CsvDataWrapper csvDataHolder) {
		csvDataHolder.setTestStatus(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.ATTACH_TEST_STATUS_2G_INDEX));
		csvDataHolder.setFailureCause(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.ATTACH_FAILURE_CAUSE_2G_INDEX));
		csvDataHolder.setRemark(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.ATTACH_REMARK_2G_INDEX));		
		csvDataHolder.setNavigationStartMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.ATTACH_NAVIGATION_START_MESSAGE_2G_INDEX));
		csvDataHolder.setNavigationStopMessage(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.ATTACH_NAVIGATION_STOP_MESSAGE_2G_INDEX));
	
	}
	

	public static void  addRemarkAndTestSkipIntoAggrigateWrapper(Layer3SummaryWrapper aggrigateWrapperData,
			NVL3CsvDataWrapper wrapper) {
		addRemarkIntoAggrigateWrapper(aggrigateWrapperData, wrapper);
		addTestSkipIntoAggrigratedWrapper(aggrigateWrapperData, wrapper);
	}

	public static void addTestSkipIntoAggrigratedWrapper(Layer3SummaryWrapper aggrigateWrapperData,
			NVL3CsvDataWrapper wrapper) {		
		if(wrapper.getTestType()!=null && wrapper.getFailureCause()!=null) {
			StringBuilder testSkipJson=aggrigateWrapperData.getTestSkipJson()!=null?aggrigateWrapperData.getTestSkipJson().append(Symbol.BRACKET_OPEN_STRING):getInitialStringForQuickTest();
			NVLayer3Utils.addValueToStringBuilder(testSkipJson,aggrigateWrapperData.getRecipeName());
			NVLayer3Utils.addValueToStringBuilder(testSkipJson,wrapper.getTestStatus());
			NVLayer3Utils.addValueToStringBuilder(testSkipJson,wrapper.getTestType());
			NVLayer3Utils.addValueToStringBuilder(testSkipJson,wrapper.getFailureCause());	
			NVLayer3Utils.addValueToStringBuilder(testSkipJson,aggrigateWrapperData.getRecipeId());
			
			testSkipJson.setLength(testSkipJson.length()-QMDLConstant.STRING_EXTRA_LENTH);
			testSkipJson.append(Symbol.BRACKET_CLOSE_STRING).append(Symbol.COMMA);
			aggrigateWrapperData.setTestSkipJson(testSkipJson);		
		}
	}

	public static void addRemarkIntoAggrigateWrapper(Layer3SummaryWrapper aggrigateWrapperData, NVL3CsvDataWrapper wrapper) {
		if(wrapper.getRemark()!=null) {				
			StringBuilder remarkJson=aggrigateWrapperData.getRemarkJson()!=null?aggrigateWrapperData.getRemarkJson().append(Symbol.BRACKET_OPEN_STRING):getInitialStringForQuickTest();
			NVLayer3Utils.addValueToStringBuilder(remarkJson,aggrigateWrapperData.getRecipeName());
			NVLayer3Utils.addValueToStringBuilder(remarkJson,wrapper.getTestType());
			NVLayer3Utils.addValueToStringBuilder(remarkJson,wrapper.getRemark());
			NVLayer3Utils.addValueToStringBuilder(remarkJson,aggrigateWrapperData.getRecipeId());
			
			remarkJson.setLength(remarkJson.length()-QMDLConstant.STRING_EXTRA_LENTH);
			remarkJson.append(Symbol.BRACKET_CLOSE_STRING).append(Symbol.COMMA);
			aggrigateWrapperData.setRemarkJson(remarkJson);				
		}
	}

	public static void setAttemptParameterforPacketDLTest(String[] lineData, NVL3CsvDataWrapper csvDataHolder) {		

		if(isValidRecordForNonePacketSizeDownload(csvDataHolder.getTestType(),csvDataHolder.getNetworkType())){
			csvDataHolder.setTestStatus(NVLayer3Utils.getStringFromCsv(lineData, NetworkDataFormats.HTTP_DOWNLOAD_TEST_NONE_TESTSTATUS_INDEX));
//           logger.info("Inside the test attempt in none case:{}",csvDataHolder.getTestStatus());
			if(csvDataHolder.getTestStatus()!=null) {  
				if(csvDataHolder.getTestStatus().equalsIgnoreCase(QMDLConstant.SUCCESS)||
						csvDataHolder.getTestStatus().equalsIgnoreCase(QMDLConstant.FAIL)
						){
//					logger.info("inside the attempt :{}",csvDataHolder.getTestStatus());
					csvDataHolder.setHttpAttempt(NVLayer3Utils.getInteger(lineData, NetworkDataFormats.HTTP_DOWNLOAD_ATTEMPT_INDEX_NONE));
				}
				if(	csvDataHolder.getTestStatus().equalsIgnoreCase(QMDLConstant.REJECT)){
//					logger.info("inside the Reject :{}",csvDataHolder.getTestStatus());
					csvDataHolder.setHttpAttempt(NVLayer3Utils.getInteger(lineData, NetworkDataFormats.HTTP_DOWNLOAD_ATTEMPT_INDEX_NONE));
					csvDataHolder.setHttpDrop(NVLayer3Utils.getInteger(lineData, NetworkDataFormats.HTTP_DOWNLOAD_ATTEMPT_INDEX_NONE));
				}
			}
		}
	}

	private static boolean isValidRecordForNonePacketSizeDownload(String testType, String networkType) {
		return (
				NetworkDataFormats.TEST_TYPE_FTP_DOWNLOAD.equals(testType)
				|| NetworkDataFormats.TEST_TYPE_HTTP_DOWNLOAD.equals(testType)
				|| NetworkDataFormats.TEST_TYPE_FTP_UPLOAD.equalsIgnoreCase(testType)
				|| NetworkDataFormats.TEST_TYPE_HTTP_UPLOAD.equalsIgnoreCase(testType)				
				|| NetworkDataFormats.TEST_TYPE_HTTP_PACKET_DOWNLOAD.equalsIgnoreCase(testType)
				|| NetworkDataFormats.TEST_TYPE_FTP_PACKET_DOWNLOAD.equalsIgnoreCase(testType)
				&& NetworkDataFormats.NETWORK_TYPE_NONE.equalsIgnoreCase(networkType));
	}

	
	
	
}

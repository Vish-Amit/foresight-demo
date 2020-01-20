package com.inn.foresight.module.nv.report.constant;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.inn.commons.lang.StringUtils;
import com.inn.core.generic.wrapper.JpaWrapper;
import com.inn.foresight.module.nv.report.utils.DriveHeaderConstants;
import com.inn.foresight.module.nv.report.utils.ReportConstants;

@JpaWrapper
public class ReportSummaryIndexWrapper {

	private String latlngTimestamp = ReportConstants.TIMESTAMP;
	private String latitude = ReportConstants.LATITUDE;
	private String longitude = ReportConstants.LONGITUDE;
	private String startTimestamp = ReportConstants.START_TIMESTAMP;
	private String endTimestamp = ReportConstants.END_TIMESTAMP;
	private String DlThroughput = ReportConstants.DL_THROUGHPUT;
	private String UlThroughput= ReportConstants.UL_THROUGHPUT;
	private String Technology = ReportConstants.TECHNOLOGY;
	private String attachLatency = ReportConstants.CONNECTION_SETUP_TIME;  // Attach Latency is Connection Setup Time
	private String responseTime = ReportConstants.WEB_DOWNLOAD_DELAY;
	private String DLFTP = ReportConstants.DL_FTP;
	private String ULFTP = ReportConstants.UL_FTP;
	private String DLHTTP = ReportConstants.DL_HTTP;
	private String ULHTTP = ReportConstants.UL_HTTP;
	private String RSRP = ReportConstants.RSRP;
	private String SINR = ReportConstants.SINR;
	private String RSSI = ReportConstants.RSSI;	
	private String RSRQ = ReportConstants.RSRQ;
	private String RSCP = ReportConstants.RSCP;
	private String ECIO = ReportConstants.ECIO;
	private String EARFCN = ReportConstants.EARFCN;
	private String Jitter = ReportConstants.JITTER;
	private String Latency = ReportConstants.LATENCY;
	private String mos = ReportConstants.MOS;
	private String MosG711 = ReportConstants.MOS_G711;
	private String WifiSnr = ReportConstants.WIFI_SNR;
	private String WifiRssi = ReportConstants.WIFI_RSSI;
	private String PacketLoss = ReportConstants.PACKET_LOSS;
	private String VoLTEJitter = ReportConstants.VOLTE_JITTER;
	private String VolteCallDrop = ReportConstants.VOLTE_CALL_DROP_RATE;
	private String VoLTEPacketLoss =  ReportConstants.VOLTE_PACKET_LOSS;
	private String handoverInitiate = ReportConstants.HANDOVER_INITIATE;
	private String handoverFailure = ReportConstants.HANDOVER_FAILURE;
	private String handoverSuccess = ReportConstants.HANDOVER_SUCCESS;
	private String HandOverLatency = ReportConstants.HANDOVER_LATENCY;
	private String totalHandover = ReportConstants.TOTAL_HANDOVER;
	private String pdschThroughput = ReportConstants.PDSCH_THROUGHPUT;
	private String puschThroughput = ReportConstants.PUSCH_THROUGHPUT;
	private String macDlThroughput = ReportConstants.MAC_DL_THROUGHPUT;
	private String macUlThroughput = ReportConstants.MAC_UL_THROUGHPUT;
	private String httpAttempt = ReportConstants.HTTP_ATTEMPT;
	private String httpSucess = ReportConstants.HTTP_SUCCESS;
	private String HttpDownloadTime = ReportConstants.HTTP_DOWNLOAD;
	private String rrcConnectionRequest = ReportConstants.RRC_INITIATE;
	private String rrcConnectionSetupComplete = ReportConstants.RRC_SUCCESS;
	private String ErabSuccess = ReportConstants.ERAB_SUCCESS_RATE;
	private String ErabDrop = ReportConstants.ERAB_DROP_RATE;
	private String callAttempt = ReportConstants.CALL_ATTEMPT;
	private String callInitiate = ReportConstants.CALL_INITIATE;
	private String callSuccess = ReportConstants.CALL_SUCCESS;
	private String callFail = ReportConstants.CALL_FAILURE;
	private String callDrop = ReportConstants.CALL_DROP;
	private String callConnectionSetupTime = ReportConstants.CALL_SETUP_TIME;
	private String callSetupSuccess = ReportConstants.CALL_SETUP_SUCCESS;
	private String callAttemptOnnet = ReportConstants.CALL_ATTEMPT_ON_NET;
	private String callFailureOnnet = ReportConstants.CALL_FAILURE_ON_NET;
	private String callDropOnnet = ReportConstants.CALL_DROP_ON_NET;
	private String callSuccessOnnet = ReportConstants.CALL_SUCCESS_ON_NET;
	private String callAttemptOffnet = ReportConstants.CALL_ATTEMPT_OFF_NET;
	private String callFailureOffnet = ReportConstants.CALL_FAILURE_OFF_NET;
	private String callDropOffnet = ReportConstants.CALL_DROP_OFF_NET;
	private String callSuccessOffnet = ReportConstants.CALL_SUCCESS_OFF_NET;	
	private String SmsAttempt = ReportConstants.SMS_ATTEMPT;
	private String SmsSucess = ReportConstants.SMS_SUCCESS;
	private String SmsFailure = ReportConstants.SMS_FAILURE;
	private String SmsDelivered = ReportConstants.SMS_DELIVERED;
	private String SmsDeliveredOnnet = ReportConstants.SMS_DELIVERED_ON_NET;
	private String SmsDeliveredOffnet = ReportConstants.SMS_DELIVERED_OFF_NET;
	private String SmsDeliveredInLessThan3min = ReportConstants.SMS_DELIVERED_LESS_THAN_3_MIN;
	private String SmsDeliveredInLessThan3minOnnet = ReportConstants.SMS_DELIVERED_LESS_THAN_3_MIN_ON_NET;
	private String SmsDeliveredInLessThan3minOffnet = ReportConstants.SMS_DELIVERED_LESS_THAN_3_MIN_OFF_NET;
	private String BAND = ReportConstants.BAND;
	private String handoverInterruptionTime = ReportConstants.HO_INTERRUPTION_TIME;
	private String latencyBufferSize32Bytes = ReportConstants.LATENCY32;
	private String latencyBufferSize1000Bytes = ReportConstants.LATENCY1000;
	private String latencyBufferSize1500Bytes = ReportConstants.LATENCY1500;
	private String volteMTCallAttempts = ReportConstants.MT_CALL_ATTEMPT;
	private String volteMTCallSuccess = ReportConstants.MT_CALL_SUCCESS;
	private String volteMTCallFailure = ReportConstants.MT_CALL_FAILURE	;
	private String volteMOCallAttempts = ReportConstants.MO_CALL_ATTEMPT;
	private String volteMOCallSuccess = ReportConstants.MO_CALL_SUCCESS;
	private String volteMOCallFailure = ReportConstants.MO_CALL_FAILURE;
	private String imsRegistrationSetupTime = ReportConstants.IMS_REGISTRATION_TIME;
	private String attachAccept = ReportConstants.ATTACH_ACCEPT;
	private String attachRequest = ReportConstants.ATTACH_REQUEST;
	private String attachComplete = ReportConstants.ATTACH_COMPLETE;
	private String detachRequest = ReportConstants.DETACH_REQUEST;
	private String youtubeBufferTime = ReportConstants.YOUTUBE_BUFFER_TIME;
	private String totalDistance =  ReportConstants.TOTAL_DISTANCE;
	
	private String startLatitude = ReportConstants.START_LATITUDE;
	private String startLongitude = ReportConstants.START_LONGITUDE;
	private String endLatitude = ReportConstants.END_LATITUDE;
	private String endLongitude = ReportConstants.END_LONGITUDE;
	private String Msg1Count = ReportConstants.MSG1_COUNT;
	private String Msg3Count = ReportConstants.MSG3_COUNT;
	private String TotalPacketCount = ReportConstants.TOTAL_PACKET_COUNT;
	private String NumberOfRtpPacketsLost = ReportConstants.RTP_PACKET_LOSS;
	private String pingBufferSize = ReportConstants.PING_BUFFER_SIZE;
	private String PCI = ReportConstants.PCI;
	private String pciList=ReportConstants.PCI_LIST;
	private String jsonMinLatencyList = ReportConstants.MIN_LATENCY_LIST;
private String rrcDrop = ReportConstants.RRC_DROPPED;
	private String initialERABSuccessCount = ReportConstants.INITIAL_ERAB_SUCCESS_COUNT;

	private static List<String> getList() {
		return Arrays.asList(ReportConstants.MIN, ReportConstants.MAX, ReportConstants.SUM, ReportConstants.COUNT, ReportConstants.AVG, ReportConstants.WAVG, ReportConstants.LATEST, ReportConstants.BLANK_STRING);
	}

	
	public static List<String> getLiveDriveKPIs() throws IllegalArgumentException, IllegalAccessException{
		List<String> response = new ArrayList<>();
		ReportSummaryIndexWrapper obj = new ReportSummaryIndexWrapper();
		Field[] fields = obj.getClass().getDeclaredFields();
		
		for (Field field : fields) {
        	   field.setAccessible(true);
        	   for(String element : getList()) {
					switch(element) {
						case ReportConstants.MIN:
							response.add(field.getName() +  element + ReportConstants.COLON + element.toUpperCase());
							break;
						case ReportConstants.MAX:

							response.add(field.getName() +  element + ReportConstants.COLON + element.toUpperCase());
							break;
						case ReportConstants.COUNT:

							response.add(field.getName() +  ReportConstants.CNT + ReportConstants.COLON + ReportConstants.SUM.toUpperCase());
							
							break;
						default:
							response.add(StringUtils.isNotBlank(element) ? field.getName() + ReportConstants.COLON + element.toUpperCase() : field.getName());
					}
        	   }
		}
		return response;
		
	}
	public static Map<String, Integer> getLiveDriveKPIIndexMap(List<String> fetchKPIList) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		List<String> list = new ArrayList<>();

		ReportSummaryIndexWrapper obj = new ReportSummaryIndexWrapper();
		Field[] fields = obj.getClass().getDeclaredFields();
		
		for (Field field : fields) {
    	   field.setAccessible(true);
    	   for(String element : getList()) {
			   if(fetchKPIList.contains(field.getName() + ReportConstants.COLON + element.toUpperCase())
					   || fetchKPIList.contains(field.getName() + element + ReportConstants.COLON + element.toUpperCase())) {
    			   list.add(StringUtils.isNotBlank(element) ? element.toUpperCase() + ReportConstants.UNDERSCORE + (String)field.get(obj) : (String)field.get(obj));
			   } else if(fetchKPIList.contains(field.getName() + ReportConstants.CNT + ReportConstants.COLON + ReportConstants.SUM.toUpperCase())){
				   list.add(StringUtils.isNotBlank(element) ? ReportConstants.CNT + ReportConstants.UNDERSCORE + (String)field.get(obj) : (String)field.get(obj));
			   } else if(fetchKPIList.contains(field.getName())) {
    			   list.add((String)field.get(obj));   
        	   }
    	   }
        }
		return list.stream().distinct().collect(Collectors.toMap(i -> i, i -> list.indexOf(i)));
	}


}

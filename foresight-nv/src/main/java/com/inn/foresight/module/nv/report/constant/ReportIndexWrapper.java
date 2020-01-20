package com.inn.foresight.module.nv.report.constant;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.inn.core.generic.wrapper.JpaWrapper;
import com.inn.foresight.module.nv.report.utils.DriveHeaderConstants;
import com.inn.foresight.module.nv.report.utils.IBWifiConstants;
import com.inn.foresight.module.nv.report.utils.ReportConstants;

@JpaWrapper
public class ReportIndexWrapper {

	private String latitude = ReportConstants.LATITUDE;
	private String longitude = ReportConstants.LONGITUDE;
	private String latlngTimestamp = ReportConstants.TIMESTAMP;
	private String DlThroughput = ReportConstants.DL_THROUGHPUT;
	private String UlThroughput= ReportConstants.UL_THROUGHPUT;
	private String RSRP = ReportConstants.RSRP;
	private String SINR = ReportConstants.SINR;
	private String RSSI = ReportConstants.RSSI;	
	private String RSRQ = ReportConstants.RSRQ;
	private String CQI = ReportConstants.CQI;
	private String PCI = ReportConstants.PCI_PLOT;
	private String MCS = ReportConstants.MCS;
	private String MCC = ReportConstants.MCC;
	private String MNC = ReportConstants.MNC;
	private String MIMO = ReportConstants.MIMO;
	private String SSID = ReportConstants.SSID;
	private String BAND = ReportConstants.BAND;
	private String RankIndex = ReportConstants.RI;
	private String DLFrequency = ReportConstants.DL_EARFCN;
	private String ULFrequency = ReportConstants.UL_EARFCN;
	private String bssid = ReportConstants.WIFI_BSSID;
	private String WifiSnr = ReportConstants.WIFI_SNR;
	private String WifiRssi = ReportConstants.WIFI_RSSI;
	private String TestType = ReportConstants.TEST_TYPE;
	private String Latency = ReportConstants.LATENCY;
	private String Jitter = ReportConstants.JITTER;
	private String CellId = ReportConstants.CELL_ID;
	private String responseTime = ReportConstants.WEB_DOWNLOAD_DELAY;
	private String Technology = ReportConstants.TECHNOLOGY;
	private String pdschThroughput = ReportConstants.PDSCH_THROUGHPUT;
	private String puschThroughput = ReportConstants.PUSCH_THROUGHPUT;
	private String macDlThroughput = ReportConstants.MAC_DL_THROUGHPUT;
	private String macUlThroughput = ReportConstants.MAC_UL_THROUGHPUT;
	private String VoLTEJitter = ReportConstants.VOLTE_JITTER;
	private String VoLTEPacketLoss =  ReportConstants.VOLTE_PACKET_LOSS;
	private String volteLatency = ReportConstants.VOLTE_LATENCY;
    private String CellReselection=ReportConstants.CELL_RESELECTION;
	private String pMos = ReportConstants.POLQA_MOS;	
	private String NetworkType = ReportConstants.NETWORK_TYPE;
	private String DLBandwidth = ReportConstants.DL_BANWIDTH;
	private String ULBandwidth = ReportConstants.UL_BANWIDTH;
	private String callInitiate = ReportConstants.CALL_INITIATE;
	private String callDrop = ReportConstants.CALL_DROP;
	private String callFail = ReportConstants.CALL_FAILURE;
	private String callSuccess = ReportConstants.CALL_SUCCESS;
	private String callConnectionSetupTime = ReportConstants.CALL_SETUP_TIME;
	private String callSetupSuccess = ReportConstants.CALL_SETUP_SUCCESS;
	private String handoverInitiate = ReportConstants.HANDOVER_INITIATE;
	private String handoverFailure = ReportConstants.HANDOVER_FAILURE;
	private String handoverSuccess = ReportConstants.HANDOVER_SUCCESS;
	private String reselectionSuccess = DriveHeaderConstants.RESELECTION_SUCCESS;
	private String CAType = ReportConstants.CA;
	private String sourcePCI = ReportConstants.SOURCE_PCI;
	private String targetPCI = ReportConstants.TARGET_PCI;
//	private String HandOverLatency = ReportConstants.HANDOVER_LATENCY;
	private String attachRequest = ReportConstants.ATTACH_REQUEST;
	private String attachComplete = ReportConstants.ATTACH_COMPLETE;
	private String detachRequest = ReportConstants.DETACH_REQUEST;
	private String attachLatency = ReportConstants.ATTACH_LATENCY;
	private String imsRegistrationSetupTime = ReportConstants.IMS_REGISTRATION_TIME;
	private String mtCallAttempt = ReportConstants.MT_CALL_ATTEMPT;
	private String mtCallSuccess = ReportConstants.MT_CALL_SUCCESS;
	private String handoverInterruptionTime = ReportConstants.HO_INTERRUPTION_TIME;
	private String mos = ReportConstants.MOS;
	private String Msg1Count = ReportConstants.MSG1_COUNT;
	private String Msg3Count = ReportConstants.MSG3_COUNT;
	private String videoURL = ReportConstants.VIDEO_URL;
    private String videoResolution = ReportConstants.VIDEO_RESOLUTION;
	private String videoDuration = ReportConstants.VIDEO_DURATION;
	private String freezingRatio = ReportConstants.FREEZING_RATIO;
	private String noOfStalling = ReportConstants.NUM_OF_STALLING;
	private String totalBufferTime = ReportConstants.TOTAL_BUFFER_TIME;
	private String EARFCN = ReportConstants.EARFCN;

	private String xpoint = ReportConstants.X_POINT;
	private String ypoint = ReportConstants.Y_POINT;
	private String latencyBufferSize32Bytes = ReportConstants.LATENCY32;
	private String latencyBufferSize1000Bytes = ReportConstants.LATENCY1000;
	private String latencyBufferSize1500Bytes = ReportConstants.LATENCY1500;
	private String iteration = ReportConstants.ITERATION;
	private String pdschBler = ReportConstants.DL_BLER;
	private String puschBler = ReportConstants.UL_BLER;
	private String pdschSinr = ReportConstants.PDSCH_SINR;
	private String tauRequest = ReportConstants.TAU_ATTEMPT;
	private String tauComplete = ReportConstants.TAU_SUCCESS;
	private String tauAccept = ReportConstants.TAU_ACCEPT;
	private String rrcConnectionRequest = ReportConstants.RRC_INITIATE;
	private String rrcConnectionSetupComplete = ReportConstants.RRC_SUCCESS;
	private String erabSetupRequest = ReportConstants.ERAB_REQUEST;
	private String erabSetupSuccess = ReportConstants.ERAB_SUCCESS;
	private String rrcConnectionReestablishmentReject = ReportConstants.RRC_DROP;
	private String rtpAudioFt = ReportConstants.VOLTE_CODEC;
	private String dlModulationType = ReportConstants.DL_MODULATION_TYPE;
//	private String NeighbourData = ReportConstants.NEIGHBOUR_DATA;
	private String TrackingAreaCode = ReportConstants.TAC;
	private String TotalPacketCount = ReportConstants.TOTAL_PACKET_COUNT;
	private String NumberOfRtpPacketsLost = ReportConstants.RTP_PACKET_LOSS;
    private String downloadTimeYoutube = ReportConstants.DOWNLOAD_TIME_YOUTUBE;
	private String speedTestPinNumber = ReportConstants.SPEED_TEST_PIN_NUMBER;
	private String speedTestDlRate = ReportConstants.SPEED_TEST_DL_RATE;
	private String speedTestUlRate = ReportConstants.SPEED_TEST_UL_RATE;
	private String downloadTimeGoogle = ReportConstants.DOWNLOAD_TIME_GOOGLE;
	private String downloadTimeFacebook = ReportConstants.DOWNLOAD_TIME_FACEBOOK;
	private String channel = ReportConstants.WIFI_CHANNEL;
	private String linkSpeed = ReportConstants.WIFI_LINK_SPEED;
	private String CGI=DriveHeaderConstants.CGI;
	private String responseCode=ReportConstants.CALL_FAIL_CAUSE;
	private String rrcDrop = ReportConstants.RRC_DROPPED;
	private String initialERABSuccessCount = ReportConstants.INITIAL_ERAB_SUCCESS_COUNT;
	private String  paPower=ReportConstants.PA_POWER;
	private String  pbPower=ReportConstants.PB_POWER;
	private String  pdcpThroughput=ReportConstants.TCP_DL;
	private String interSiteHandover =ReportConstants.INTER_SITE_HO;
	private String intraSiteHandover =ReportConstants.INTRA_SITE_HO;
	private String ueCapabilityRatContainer=ReportConstants.UE_CAPABILITY_RAT_CONTAINER;
	// Csfb kpi
	private String csfbCallAttempt =ReportConstants.CSFB_CALL_ATTEMPT;
	private String csfbMtCallAttempt=ReportConstants.CSFB_MTCALL_ATTEMPT;
	private String csfbMoCallAttempt=ReportConstants.CSFB_MOCALL_ATTEMPT;
	private String csfbCallSetupSuccess=ReportConstants.CSFB_CALL_SETUP_SUCCESS;
	private String csfbMtCallSetupSuccess=ReportConstants.CSFB_MOCALL_SETUP_SUCCESS;
	private String csfbMoCallSetupSuccess=ReportConstants.CSFB_MTCALL_SETUP_SUCCESS;
	private String csfbCallSuccess=ReportConstants.CSFB_CALL_SUCCESS;
	private String csfbMtCallSuccess=ReportConstants.CSFB_MTCALL_SUCCESS;
	private String csfbMoCallSuccess=ReportConstants.CSFB_MOCALL_SUCCESS;
	private String csfbCallSetupTime=ReportConstants.CSFB_MO_CALL_SETUP_TIME;
	private String csfbMtCallSetupTime=ReportConstants.CSFB_MT_CALL_SETUP_TIME;
	private String csfbMoCallSetupTime=ReportConstants.CSFB_CALL_SETUP_TIME;
	private String csfbCallDrop=ReportConstants.CSFB_CALL_DROP;
	private String csfbMtCallDrop=ReportConstants.CSFB_MTCALL_DROP;
	private String csfbMoCallDrop=ReportConstants.CSFB_MOCALL_DROP;
	private String csfbCallFail=ReportConstants.CSFB_CALL_FAIL;
	private String csfbMtCallFail=ReportConstants.CSFB_MTCALL_FAIL;
	private String csfbMoCallFail=ReportConstants.CSFB_MOCALL_FAIL;
	private String csfbCause=ReportConstants.CSFB_CAUSE;
	private String raUpdateDuringCall =ReportConstants.RA_UPDATION;
	private String laUpdateDuringCall=ReportConstants.LA_UPDATION;
	private String fastReturnTime2G=ReportConstants.FAST_RETURN_TIME;	
	
	private String volteMTCallAttempts= ReportConstants.VOLTE_MT_CALL_ATTEMPT;
	private String volteMTCallSuccess=ReportConstants.VOLTE_MT_CALL_SUCCESS;
	private String volteMTCallFailure=ReportConstants.VOLTE_MT_CALL_FAIL;
	private String volteMTCallDrop=ReportConstants.VOLTE_MT_CALL_DROP;
	private String volteMTCallSetupSuccess=ReportConstants.VOLTE_MT_CALL_SETUP_SUCCESS;
	private String volteMOCallAttempts=ReportConstants.VOLTE_MO_CALL_ATTEMPT;
	private String volteMOCallSuccess=ReportConstants.VOLTE_MO_CALL_SUCCESS;
	private String volteMOCallFailure=ReportConstants.VOLTE_MO_CALL_FAIL;
	private String volteMOCallDrop=ReportConstants.VOLTE_MO_CALL_DROP;
	private String volteMTCallSetup=ReportConstants.VOLTE_MT_CALL_SETUP;
	private String volteMOCallSetup=ReportConstants.VOLTE_MO_CALL_SETUP;
	private String  mtCallConnectionSetupTime=ReportConstants.VOLTE_MT_CALL_SETUP_TIME;
	private String  mtCallSetupSuccessTime=ReportConstants.VOLTE_MT_CALL_SETUP_SUCCESS_TIME;
	private String  moCallConnectionSetupTime=ReportConstants.VOLTE_MO_CALL_SETUP_TIME;
	private String 	moCallSetupSuccessTime=ReportConstants.VOLTE_MO_CALL_SETUP_SUCCESS_TIME;
	private String 	volteMOCallSetupSuccess=ReportConstants.VOLTE_MO_CALL_SETUP_SUCCESS;
	private String intraHandoverInitiate=ReportConstants.INTRA_HANDOVER_INITIATE;
	private String intraHandoverSuccess=ReportConstants.INTRA_HANDOVER_SUCCESS;
	private String intraHandoverFailure=ReportConstants.INTRA_HANDOVER_FAIL;
	private String interhandoverInitiate=ReportConstants.INTER_HANDOVER_INITIATE;
	private String interhandoverSuccess=ReportConstants.INTER_HANDOVER_SUCCESS;
	private String interhandoverFailure=ReportConstants.INTER_HANDOVER_FAIL;
	private String muteCall=ReportConstants.MUTECALL;
	private String decodedMsg= ReportConstants.DECODED_MESSAGE;
	private String  srvccSuccess=ReportConstants.SRVCC_SUCCESS;
	private String  srvccAttempt=ReportConstants.SRVCC_ATTEMPT;
	private String  srvccFailure=ReportConstants.SRVCC_FAIL;
	private String srvccTime=ReportConstants.SRVCC_TIME;
	private String sessionEstablishmentTime=ReportConstants.SESSION_ESTABLISHMENT_TIME;
	private String dlPRB=ReportConstants.DL_PRB;
	private String ulPRB=ReportConstants.UL_PRB;
	private String eventType=ReportConstants.EVENT_TYPE;
	private String locationAreaCode=ReportConstants.LAC;
	private String fastReturnPriorities=ReportConstants.FAST_RETURN_PRIORITY;
	private String rachAttempt=ReportConstants.RACH_ATTEMPT;
    private String rrcConnectionRelease412f=ReportConstants.RRC_CONNECTION_RELESE412F;
    private String lteIRATHandover =ReportConstants.LTE_IRAT_HANDOVER;
    private String  DetachTime=ReportConstants.DETACH_TIME;
    private String intraFreqCellReselectionPriority=ReportConstants.INTRA_FREQ_CELL_RESELECTION_PRIORITY;
    private String tddToFDDCellReselTime=ReportConstants.TDD_TO_FDD_RESELECTION_TIME;
    private String  fddToTDDCellReselTime=ReportConstants.FDD_TO_TDD_RESELECTION_TIME;
    private String cellReselectionLTETo3GTime=ReportConstants.CELL_RESELECTION_LTE_TO_3G_TIME;
    private String cellReselectionLTETo2GTime = ReportConstants.CELL_RESELECTION_LTE_TO_2G_TIME;
    private String handoverTddToFdd =ReportConstants.HO_TDD_TO_FDD;
    private String handoverFddToTdd=ReportConstants.HO_FDD_TO_TDD;
    private String cellReselectionTime2GToLTE=ReportConstants.CELL_RESELECTION_2G_TO_LTE_TIME;
    private String cellReselectionTime3GToLTE=ReportConstants.CELL_RESELECTION_3G_TO_LTE_TIME;
    private String callDropReason = ReportConstants.CALL_DROP_REASON;
    private String cqiCwoMax=ReportConstants.CQI_CW0_MAX;
	private String cqiCw1Max=ReportConstants.CQI_CW1_MAX;
	private String dlPRBMax = ReportConstants.DL_PRB_MAX;
	private String ulPRBMax = ReportConstants.UL_PRB_MAX;
	private String mcsB173Max = ReportConstants.DL_MCS_MAX;
	private String puschMcsIndexMax = ReportConstants.UL_MCS_MAX;
	private String videoOverVolteCallSuccess = ReportConstants.VIDEO_OVER_VOLTE_CALL_SUCCESS;
	private String videoOverVolteCallInitiate = ReportConstants.VIDEO_OVER_VOLTE_CALL_INITIATE;
	private String volteCallConferencingSuccess = ReportConstants.VIDEO_CALL_CONF_SUCCESS;
	private String volteCallConferencingInitiate = ReportConstants.VIDEO_CALL_CONF_INITIATE;
	private String sCell2PdschThroughput = ReportConstants.SCELL2_PDSCH_THROUPUT;
	private String sCell1PdschThroughput = ReportConstants.SCELL1_PDSCH_THROUPUT;
	private String pCellPdschThroughput = ReportConstants.PCELL_PDSCH_THROUPUT;
	private String interFreqCellReselectionPriority = ReportConstants.INTER_FREQ_CELL_RESELECTION_PRIORITY;
	private String iratWCDMACellReselectionPriority = ReportConstants.IRAT_WCDMA_CELL_RESELECTION_PRIORITY;
	private String iratGSMCellReselectionPriority = ReportConstants.IRAT_GSM_CELL_RESELECTION_PRIORITY;
	
	private String RxLev =ReportConstants.RXLEVEL;
	private String RxQual=ReportConstants.RXQUAL;
	private String snr=ReportConstants.WROST_CI;
	private String gsmCellId=ReportConstants.GSM_CELLID;

	private String responseTimeIB = ReportConstants.WEB_DOWNLOAD_DELAY_IB;
	
      public static List<String> getLiveDriveKPIs() throws IllegalArgumentException, IllegalAccessException{
		List<String> response = new ArrayList<>();
		ReportIndexWrapper obj = new ReportIndexWrapper();
		Field[] fields = obj.getClass().getDeclaredFields();
		
		for (Field field : fields) {
        	   field.setAccessible(true);
        	   response.add(field.getName());
        }
		return response;
		
	}
	
	public static Map<String, Integer> getLiveDriveKPIIndexMap(List<String> fetchKPIList) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {

		List<String> list = new ArrayList<>();

		ReportIndexWrapper obj = new ReportIndexWrapper();
		Field[] fields = obj.getClass().getDeclaredFields();
		
		for (Field field : fields) {
        	   field.setAccessible(true);
        	   if(fetchKPIList.contains(field.getName())) {
				   list.add((String)field.get(obj));
        	   }
        }
		addCustomKpiToList(fetchKPIList);
		addCustomKpiToList(list);
		return list.stream().collect(Collectors.toMap(i -> i, i -> list.indexOf(i)));
	}

	private static void addCustomKpiToList(List<String> kpiList){
		kpiList.add(ReportConstants.SERVING_CELL);
		kpiList.add(ReportConstants.SERVING_SYSTEM);
		kpiList.add(DriveHeaderConstants.ROUTE);
		kpiList.add(ReportConstants.FTP_UL_THROUGHPUT);
		kpiList.add(ReportConstants.FTP_DL_THROUGHPUT);
		kpiList.add(ReportConstants.HTTP_UL_THROUGHPUT);
		kpiList.add(ReportConstants.HTTP_DL_THROUGHPUT);
		kpiList.add(ReportConstants.FTP_UL_RSRP);
		kpiList.add(ReportConstants.FTP_DL_RSRP);
		kpiList.add(ReportConstants.HTTP_UL_RSRP);
		kpiList.add(ReportConstants.HTTP_DL_RSRP);
		kpiList.add(ReportConstants.FTP_UL_SINR);
		kpiList.add(ReportConstants.FTP_DL_SINR);
		kpiList.add(ReportConstants.HTTP_UL_SINR);
		kpiList.add(ReportConstants.HTTP_DL_SINR);
		kpiList.add(ReportConstants.CALL_PLOT);
		kpiList.add(ReportConstants.IDLE_PLOT);
		kpiList.add(ReportConstants.HANDOVER_PLOT);
		kpiList.add(ReportConstants.RESELECTION_PLOT);
		kpiList.add(ReportConstants.ERAB_PLOT);
		kpiList.add(ReportConstants.CALL_SETUP_PLOT);
		kpiList.add(IBWifiConstants.KEY_WIFI_DL_2_GHZ);
		kpiList.add(IBWifiConstants.KEY_WIFI_DL_5_GHZ);
		kpiList.add(IBWifiConstants.KEY_WIFI_UL_2_GHZ);
		kpiList.add(IBWifiConstants.KEY_WIFI_UL_5_GHZ);
		kpiList.add(ReportConstants.HANDOVER_FAILURE_PLOT);
	}

}

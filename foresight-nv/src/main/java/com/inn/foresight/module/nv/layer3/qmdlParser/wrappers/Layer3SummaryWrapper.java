package com.inn.foresight.module.nv.layer3.qmdlParser.wrappers;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.inn.foresight.module.nv.layer3.wrapper.YoutubeWrapper;

public class Layer3SummaryWrapper {

	private Long tempTimeStamp;
	private Long timeStamp;
	private Long etimeStamp;
	private Double startLat;
	private Double startLong;
	private Double endLat;
	private Double endLong;
	
	
	
	private Double[] rSRPRx0Data;
	private Double[] rSRPRx1Data;
	private Double[] rSRPRx2Data;
	private Double[] rSRPRx3Data;
	private Double[] measureRSRPData;

	private Double[] rSRQRx0Data;
	private Double[] rSRQRx1Data;
	private Double[] rSRQRx2Data;
	private Double[] rSRQRx3Data;
	private Double[] measureRSRQData;

	private Double[] rSSIRx0Data;
	private Double[] rSSIRx1Data;
	private Double[] rSSIRx2Data;
	private Double[] rSSIRx3Data;
	private Double[] rSSIData;

	private Double[] sINRRx0Data;
	private Double[] sINRRx1Data;
	private Double[] sINRRx2Data;
	private Double[] sINRRx3Data;
	private Double[] sINRData;
		
	private String rankIndex;
	private Double[] cqiCwo;
	private String carrierIndex;
	private Double[] ulThroughPut;
	private Double[] dlThroughPut;
	private Double[] latency;

	private Integer callInitiateCount;
	private Integer callDropCount;
	private Integer callFailureCount;
	private Integer callSuccessCount;
	private Integer cellChangeCount;
	private String address;
	private Integer rrcInitiate;
	private Integer rrcSucess;
	private Set<Integer> cellidSet;
	private Set<String> bandSet;
	private Set<String> technologyBandSet;
	private Integer voLTECallDropCount;
	private Integer erabDrop;
	private Integer totalErab;
	private String operatorName;
	private Set<String> script;
	private String make;
	private String model;
	private String platform;
	private Set<String> operator;
	private String appVersion;
	private Set<String> ulDlEarfcn;
	private Integer mimoCount;
	private Integer mimoTotalCount;

	private Integer tddCount;
	private Integer fddCount;
	private Integer countRsrpGreaterThan100Dbm;
	private Integer countSinrGreaterThan12Db;

	private Integer countDlGreaterThan5Mbps;
	private Map<Integer, Integer> pciMap;
	private Map<Integer, Integer> cellIdMap;
	private Map<Integer, Integer> enodeBMap;
	private Map<Integer, Integer> cgiMap;
	private Double[] wifiRssi;
	private String wifiBand;

	private Double[] wifiSnr;
	private Set<String> ssid;
	private Integer cntsnrgrtthn25dbm;
	private Integer cntrssigrtthn90dbm;
	private Double minLatency;
	private Double maxLatency;
	private Double minDl;
	private Double maxDl;
	private Double minUl;
	private Double maxUl;
	private Double minJitter;
	private Double maxJitter;
	private Double minresponseTime;
	private Double maxresponseTime;
	private Set<String> imei;
	private Double[] responseTime;
	private Double[] jitter;
	private Double minRSRP;
	private Double maxRSRP;
	private Double minSINR;
	private Double maxSINR;
	private StringBuilder quickTestJson;
	private StringBuilder handoverJson;

	private Map<Integer, NvSMSDetailWrapper> smsData;
	private boolean isPci;
	private boolean isearfcn;
	private boolean isband;
	private boolean israt;

	private Integer lPci;
	private Integer lEarfcn;
	private Integer lBand;
	private String lRat;
	private String recipeName;

	private Set<Integer> earfcn;
	private Map<String, Double> dlPeakValueMap;
	private Double[] handoverLatencyTime;
	private Double minHttpDl;
	private Double maxHttpDl;
	private Double minHttpUl;
	private Double maxHttpUl;
	private Double minFtpDl;
	private Double maxFtpDl;
	private Double minFtpUl;
	private Double maxFtpUl;
	private Double[] httpDl;
	private Double[] httpUl;
	private Double[] ftpDl;
	private Double[] ftpUl;
	private Double minRsrq;
	private Double maxRsrq;
	private Double minRssi;
	private Double maxRssi;
	private Integer callInitiateOnNetCount;
	private Integer callDropOnNetCount;
	private Integer callFailureOnNetCount;
	private Integer callSuccessOnNetCount;
	private Integer callInitiateOffNetCount;
	private Integer callDropOffNetCount;
	private Integer callFailureOffNetCount;
	private Integer callSuccessOffNetCount;

	private Double[] finalMosG711;
	private Double[] finalMosILbc;
	private Double[] finalMosG711Onnet;
	private Double[] finalMosILbcOnnet;
	private Double[] finalMosG711Offnet;
	private Double[] finalMosILbcOffnet;
	private Double[] callConnectionSetupTimeOnnet;
	private Double[] callConnectionSetupTimeOffnet;

	private Double[] connectionSetupTime;
	private Integer totalHTTPAttempt;
	private Integer totalHTTPSuccess;
	private Integer totalHTTPFailure;
	private Long totalHTTPTimeTaken;
	private Double[] totalHTTPthroughPut;
	private TreeMap<Long, String> callNetStatusMap;
	private TreeMap<Long, String> callNetStatusOnSuccessMap;
	private Map<String, YoutubeWrapper> youTubeTest;
	private Double[] callConnectionSetupTime;
	private boolean isPmi;
	private boolean isBandwidthUL;
	private boolean isBandwidthDL;
	private boolean isSpatialIndex;
	private boolean isPowerTxPusch;
	private boolean isTimingAdvance;
	private boolean isBler;
	private boolean isMcs;
	private boolean isDlEarfcn;
	private boolean isUlEarfcn;
	private boolean isRI;
	private boolean isCarrierIndex;
	private boolean isMNC;
	private boolean isMCC;

	private Integer MNC;
	private Integer MCC;
	private boolean isTrackingAraCode;

	private Set<String> kpiList;
	private Set<String> mobilityList;
	private Set<String> callList;
	private Set<String> downloadList;
	private Set<String> smsList;
	private Set<String> tauList;

	private String deviceId;
	private Double pMosFirstDetail;
	private Double[] pMos;

	private Double[] handoverInterruptionTimeOfQCI9DL;
	private Double[] handoverInterruptionTimeOfQCI9UL;

	private Integer callSetupSuccessCount;

	private Integer callSetupCount;

	private Double[] pdcpThroughput;
	
    private Double[] rlcThroughput;
	

    private Double numberOfRtpPacketsLost;
	private Double totalPacketCount;
	private Set<Integer>  pingBufferSize;
	
	
	
	
    
	public Double[] getrSRPRx2Data() {
		return rSRPRx2Data;
	}

	public void setrSRPRx2Data(Double[] rSRPRx2Data) {
		this.rSRPRx2Data = rSRPRx2Data;
	}

	public Double[] getrSRPRx3Data() {
		return rSRPRx3Data;
	}

	public void setrSRPRx3Data(Double[] rSRPRx3Data) {
		this.rSRPRx3Data = rSRPRx3Data;
	}

	public Double[] getrSRQRx2Data() {
		return rSRQRx2Data;
	}

	public void setrSRQRx2Data(Double[] rSRQRx2Data) {
		this.rSRQRx2Data = rSRQRx2Data;
	}

	public Double[] getrSRQRx3Data() {
		return rSRQRx3Data;
	}

	public void setrSRQRx3Data(Double[] rSRQRx3Data) {
		this.rSRQRx3Data = rSRQRx3Data;
	}

	public Double[] getrSSIRx2Data() {
		return rSSIRx2Data;
	}

	public void setrSSIRx2Data(Double[] rSSIRx2Data) {
		this.rSSIRx2Data = rSSIRx2Data;
	}

	public Double[] getrSSIRx3Data() {
		return rSSIRx3Data;
	}

	public void setrSSIRx3Data(Double[] rSSIRx3Data) {
		this.rSSIRx3Data = rSSIRx3Data;
	}

	public Double[] getsINRRx2Data() {
		return sINRRx2Data;
	}

	public void setsINRRx2Data(Double[] sINRRx2Data) {
		this.sINRRx2Data = sINRRx2Data;
	}

	public Double[] getsINRRx3Data() {
		return sINRRx3Data;
	}

	public void setsINRRx3Data(Double[] sINRRx3Data) {
		this.sINRRx3Data = sINRRx3Data;
	}

	public Double getNumberOfRtpPacketsLost() {
		return numberOfRtpPacketsLost;
	}

	public void setNumberOfRtpPacketsLost(Double numberOfRtpPacketsLost) {
		this.numberOfRtpPacketsLost = numberOfRtpPacketsLost;
	}

	public Double getTotalPacketCount() {
		return totalPacketCount;
	}

	public void setTotalPacketCount(Double totalPacketCount) {
		this.totalPacketCount = totalPacketCount;
	}

	public Set<Integer>  getPingBufferSize() {
		return pingBufferSize;
	}

	public void setPingBufferSize(Set<Integer>  pingBufferSize) {
		this.pingBufferSize = pingBufferSize;
	}

	public Double[] getRlcThroughput() {
		return rlcThroughput;
	}

	public void setRlcThroughput(Double[] rlcThroughput) {
		this.rlcThroughput = rlcThroughput;
	}
	

	public Double[] getPdcpThroughput() {
		return pdcpThroughput;
	}

	public void setPdcpThroughput(Double[] pdcpThroughput) {
		this.pdcpThroughput = pdcpThroughput;
	}

	public Integer getCallSetupSuccessCount() {
		return callSetupSuccessCount;
	}

	public void setCallSetupSuccessCount(Integer callSetupSuccessCount) {
		this.callSetupSuccessCount = callSetupSuccessCount;
	}

	/**
	 * @return the handoverInterruptionTimeOfQCI9DL
	 */
	public Double[] getHandoverInterruptionTimeOfQCI9DL() {
		return handoverInterruptionTimeOfQCI9DL;
	}

	/**
	 * @param handoverInterruptionTimeOfQCI9DL
	 *            the handoverInterruptionTimeOfQCI9DL to set
	 */
	public void setHandoverInterruptionTimeOfQCI9DL(Double[] handoverInterruptionTimeOfQCI9DL) {
		this.handoverInterruptionTimeOfQCI9DL = handoverInterruptionTimeOfQCI9DL;
	}

	/**
	 * @return the handoverInterruptionTimeOfQCI9UL
	 */
	public Double[] getHandoverInterruptionTimeOfQCI9UL() {
		return handoverInterruptionTimeOfQCI9UL;
	}

	/**
	 * @param handoverInterruptionTimeOfQCI9UL
	 *            the handoverInterruptionTimeOfQCI9UL to set
	 */
	public void setHandoverInterruptionTimeOfQCI9UL(Double[] handoverInterruptionTimeOfQCI9UL) {
		this.handoverInterruptionTimeOfQCI9UL = handoverInterruptionTimeOfQCI9UL;
	}

	public Double[] getpMos() {
		return pMos;
	}

	public void setpMos(Double[] pMos) {
		this.pMos = pMos;
	}

	public Double getpMosFirstDetail() {
		return pMosFirstDetail;
	}

	public void setpMosFirstDetail(Double pMosFirstDetail) {
		this.pMosFirstDetail = pMosFirstDetail;
	}

	private TreeMap<Long, HandoverDetailWrapper> hodetailWrapperMap;

	public TreeMap<Long, HandoverDetailWrapper> getHodetailWrapperMap() {
		return hodetailWrapperMap;
	}

	public void setHodetailWrapperMap(TreeMap<Long, HandoverDetailWrapper> hodetailWrapperMap) {
		this.hodetailWrapperMap = hodetailWrapperMap;
	}

	private Double[] youtubeThroughtPut;
	private Double youtubeBufferTime;
	private Double[] pingPacketLoss;
	private Double[] wptDns;
	private Double[] wptUrl;
	private Double[] ulThroughPutAdhoc;
	private Double[] dlThroughPutAdhoc;
	private Double[] latencyAdhoc;
	private StringBuilder remarkJson;
	private StringBuilder testSkipJson;
	private String mobileNumber;
	private String recipeId;
	private Integer httpAttempt;
	private Integer httpSucess;
	private Integer httpFailure;
	private Integer httpDrop;
	private Double[] httpDownLoadTime;
	private Integer smsFailure;

	private Double totalDistance;
	private String battaryInfo;
	private String chipsetInfo;
	private String driveVersion;
	private Integer tauAttempt;
	private Integer tauComplete;
	private Integer tauFailure;
	private Double[] tauTime;
	private Double[] instantaneousMos;
	private Double minInstantaneousMos;
	private Double maxInstantaneousMos;

	/** Csfb call data. */
	private Integer csfbCallAttempt;
	private Integer csfbCallSuccess;
	private Integer csfbCallDrop;
	private Integer csfbCallFailed;
	private Integer csfbCallSetupSuccess;
	private Integer csfbCallSetupFailed;

	private Double[] pdschThroughput;
	private Double[] puschThroughput;

	private Boolean puschMcsIndex;
	private String puschModulationType;

	// rrc connection states
	private Integer rrcConnectionComplete;
	private Integer rrcConnectionSetupOk;
	private Integer rrcConnectionAttempt;
	private Integer rrcConnectionRelease;
	private Integer rrcConnectionFailed;
	private Double[] rrcConnectionSetupTime;
	private Integer rrcReestablishmentSuccess;
	private Integer rrcReestablishmentFailed;
	private Integer rrcConnectionSetup;

	// LTE LA KPI
	private Double[] cqiCw1;
	private Double[] dlPrb;
	private Double[] pdschnumRb;
	private String pdschCwoModulation;
	private Integer pdschCwoMcs;
	private Double[] numRB;

	private String transmissionMode;
	private Double tb016QAMUtilization;

	// dominant
	private Double[] dominantChannelRSSI;
	private Double[] dominantChannelRSSIRx0;
	private Double[] dominantChannelRSSIRx1;
	private Double[] dominantChannelRSRQ;
	private Double[] dominantChannelRSRQRx0;
	private Double[] dominantChannelRSRQRx1;
	private Double[] dominantChannelRSRP;
	private Double[] dominantChannelRSRPRx0;
	private Double[] dominantChannelRSRPRx1;
	private Integer dominantChannelPCI;
	private Long lteDominantFrequency;
	private Double prachTxPower;
	private Integer powerHeadroomdata;

	// KPI Rate
	private Double[] handoverInterruption;
	private Double minHandoverInterruption;
	private Double maxHandoverInterruption;
	private Double[] handoverInterruptionTimeOfQCI1;

	private Integer rrcConnectionReestablishmentRequest;
	private Integer rrcConnectionReestablishmentReject;
	private Integer rrcConnectionReestablishmentComplete;

	private Double minPdschThroughput;
	private Double maxPdschThroughput;
	private Double[] macDlThroughPut;

	private Double[] jitterAggregated;
	private Double[] packetLossValueAndCount;

	private Double minPuschThroughput;
	private Double maxPuschThroughput;

	public Double[] getHandoverInterruptionTimeOfQCI1() {
		return handoverInterruptionTimeOfQCI1;
	}

	public void setHandoverInterruptionTimeOfQCI1(Double[] handoverInterruptionTimeOfQCI1) {
		this.handoverInterruptionTimeOfQCI1 = handoverInterruptionTimeOfQCI1;
	}

	public Double getMinPuschThroughput() {
		return minPuschThroughput;
	}

	public void setMinPuschThroughput(Double minPuschThroughput) {
		this.minPuschThroughput = minPuschThroughput;
	}

	public Double getMaxPuschThroughput() {
		return maxPuschThroughput;
	}

	public void setMaxPuschThroughput(Double maxPuschThroughput) {
		this.maxPuschThroughput = maxPuschThroughput;
	}

	public Double[] getMacDlThroughPut() {
		return macDlThroughPut;
	}

	public void setMacDlThroughPut(Double[] macDlThroughPut) {
		this.macDlThroughPut = macDlThroughPut;
	}

	public Double getMinPdschThroughput() {
		return minPdschThroughput;
	}

	public void setMinPdschThroughput(Double minPdschThroughput) {
		this.minPdschThroughput = minPdschThroughput;
	}

	public Double getMaxPdschThroughput() {
		return maxPdschThroughput;
	}

	public void setMaxPdschThroughput(Double maxPdschThroughput) {
		this.maxPdschThroughput = maxPdschThroughput;
	}

	public Double getMinHandoverInterruption() {
		return minHandoverInterruption;
	}

	public void setMinHandoverInterruption(Double minHandoverInterruption) {
		this.minHandoverInterruption = minHandoverInterruption;
	}

	public Double getMaxHandoverInterruption() {
		return maxHandoverInterruption;
	}

	public void setMaxHandoverInterruption(Double maxHandoverInterruption) {
		this.maxHandoverInterruption = maxHandoverInterruption;
	}

	private Integer rrcComplete;
	private Integer rrcRequest;
	private Integer rrcSetupComplete;
	private Integer rrcReestablishmentRequest;
	private Integer message3Count;
	private Integer message1Count;
	private Integer attachAccept;
	private Integer attachRequest;
	private Integer attachComplete;
	private Integer detachRequest;

	private Integer tauAccept;
	private Integer tauRequest;
	private Integer b169Count;
	private Integer b0C0Count;

	// Event new
	private Integer voiceBearerActivationRequestCount;
	private Integer voiceBearerActivationSuccessCount;
	private Integer voiceBearerActivationFailureCount;
	private Integer voiceBearerDeactivationRequestCount;
	private Integer initialIMSRegistrationSuccessCount;
	private Integer initialIMSRegistrationFailureCount;

	private Integer pagingMessageCount;
	private Integer radioLinkFailure;
	private Integer serviceRequest;
	private Integer rrcConnReconfiguration;
	private Integer rrcConnReconfSuccess;
	private Integer rrcConnectionDropped;
	private Integer serviceReject;
	private Integer serviceFailure;
	private Integer authenticationResponse;
	private Integer authenticationFailure;
	private Integer authenticationReject;
	private Integer authenticationRequest;
	private Integer mtAccess;
	private Integer moSignalling;
	private Integer securityModeComplete;
	private Integer reestablishmentFailure;
	private Integer rrcSetupFailure;
	private Integer rrcConnReconfFailure;
	private Integer rrcEutra;
	private Integer rrcGeran;
	private Integer rrcUtraFdd;
	private Integer rrcUtraTdd;
	private Integer lteUmtsRequest;
	private Integer lteUmtsSuccess;

	// mcs info
	private Integer tb0McsIndex;
	private Integer tb1McsIndex;
	private String tb0ModulationType;
	private String tb1ModulationType;

	// scheduling grant info
	private Double avgDLTb0Size;
	private Double avgDLTb1Size;
	private Double avgDLTbSize;
	private Double avgULTBSize;

	private Double[] pdschBLER;
	private Double[] macUlThroughput;

	private Double ueTxPower;

	private Integer preambleCountMax;
	private Integer preambleInitialTxPower;
	private Integer raRNTI;
	private Integer raTimingAdvance;

	private String dlCp;
	private String ulCp;

	private String emmState;
	private String rrcState;
	private String emmSubState;

	private Double[] avgULPRB;

	private String threshholdCause;
	private String rsrpThresholdValue;
	private String sinrThresholdValue;

	private Integer erabSetup;
	private Integer erabSetupSuccess;
	private Integer lteEMMRegisteredEvent;
	private Integer ltePDNConnectionRequest;

	private Integer lteRACHPreambleSuccessCount;
	private Integer lteRACHProcedureSuccessCount;
	private Integer lteRACHPreambleFailureCount;
	private Integer lteRACHProcedureFailureCount;

	private Integer serviceRequestSuccess;

	private Integer umtsLteRequest;
	private Integer umtsLteSuccess;
	private Integer moCsfbRequest;
	private Integer mtCsfbRequest;
	private Integer moCsfbSuccess;
	private Integer mtCsfbSuccess;
	private Integer rrcReconfigCompleteMissing;

	private Integer moCsfbFailure;
	private Integer mtCsfbFailure;
	private Integer returnCSCallEnd;

	private Integer activateDedicatedEpsBearerContextAcceptCount;
	private Integer lteMACRACHReasonRadioLinkFailureCount;

	private Integer reselectionSuccess;

	private Double minLatencyBufferSize32Bytes;
	private Double minLatencyBufferSize1000Bytes;
	private Double minLatencyBufferSize1500Bytes;

	private Double maxLatencyBufferSize32Bytes;
	private Double maxLatencyBufferSize1000Bytes;
	private Double maxLatencyBufferSize1500Bytes;

	private Double[] avgLatencyBufferSize32Bytes;
	private Double[] avgLatencyBufferSize1000Bytes;
	private Double[] avgLatencyBufferSize1500Bytes;

	private String dlModulationType;
	private String ulModulationType;
	private Double[] b173DlPRB;

	private Double[] imsRegistrationSetupTime;

	private Integer voltePagingAttempts;
	private Integer voltePagingSuccess;

	private Integer volteMTCallAttempts;
	private Integer volteMTCallSuccess;
	private Integer volteMTCallFailure;
	private Integer volteMTCallDrop;
	private Integer volteMTCallSetupSuccess;
	private Integer volteMTCallSetup;


	private Integer volteMOCallAttempts;
	private Integer volteMOCallSuccess;
	private Integer volteMOCallFailure;
	private Integer volteMOCallDrop;
	private Integer volteMOCallSetupSuccess;
	private Integer volteMOCallSetup;


	private Integer newHandOverIntiateCount;
	private Integer newHandOverFailureCount;
	private Integer newHandOverSuccessCount;
	private Integer newHandOverIntiateInterCount;
	private Integer newHandOverFailureInterCount;
	private Integer newHandOverSuccessInterCount;
	private Integer newHandOverIntiateIntraCount;
	private Integer newHandOverFailureIntraCount;
	private Integer newHandOverSuccessIntraCount;

	private Double[] dlPRBUtilization;
	private Double[] ulPRBUtilization;

	private String rrcStateFirstValue;

	
	private Double[] callSetupSuccessTime;
	
	
	
	
	
	
	public Integer getCallSetupCount() {
		return callSetupCount;
	}

	public void setCallSetupCount(Integer callSetupCount) {
		this.callSetupCount = callSetupCount;
	}

	public Integer getVolteMTCallSetup() {
		return volteMTCallSetup;
	}

	public void setVolteMTCallSetup(Integer volteMTCallSetup) {
		this.volteMTCallSetup = volteMTCallSetup;
	}

	public Integer getVolteMOCallSetup() {
		return volteMOCallSetup;
	}

	public void setVolteMOCallSetup(Integer volteMOCallSetup) {
		this.volteMOCallSetup = volteMOCallSetup;
	}

	public Double[] getCallSetupSuccessTime() {
		return callSetupSuccessTime;
	}

	public void setCallSetupSuccessTime(Double[] callSetupSuccessTime) {
		this.callSetupSuccessTime = callSetupSuccessTime;
	}

	public Integer getVolteMTCallSetupSuccess() {
		return volteMTCallSetupSuccess;
	}

	public void setVolteMTCallSetupSuccess(Integer volteMTCallSetupSuccess) {
		this.volteMTCallSetupSuccess = volteMTCallSetupSuccess;
	}

	public Integer getVolteMOCallSetupSuccess() {
		return volteMOCallSetupSuccess;
	}

	public void setVolteMOCallSetupSuccess(Integer volteMOCallSetupSuccess) {
		this.volteMOCallSetupSuccess = volteMOCallSetupSuccess;
	}

	/**
	 * @return the volteMTCallFailure
	 */
	public Integer getVolteMTCallFailure() {
		return volteMTCallFailure;
	}

	/**
	 * @param volteMTCallFailure
	 *            the volteMTCallFailure to set
	 */
	public void setVolteMTCallFailure(Integer volteMTCallFailure) {
		this.volteMTCallFailure = volteMTCallFailure;
	}

	/**
	 * @return the volteMTCallDrop
	 */
	public Integer getVolteMTCallDrop() {
		return volteMTCallDrop;
	}

	/**
	 * @param volteMTCallDrop
	 *            the volteMTCallDrop to set
	 */
	public void setVolteMTCallDrop(Integer volteMTCallDrop) {
		this.volteMTCallDrop = volteMTCallDrop;
	}

	/**
	 * @return the volteMOCallFailure
	 */
	public Integer getVolteMOCallFailure() {
		return volteMOCallFailure;
	}

	/**
	 * @param volteMOCallFailure
	 *            the volteMOCallFailure to set
	 */
	public void setVolteMOCallFailure(Integer volteMOCallFailure) {
		this.volteMOCallFailure = volteMOCallFailure;
	}

	/**
	 * @return the volteMOCallDrop
	 */
	public Integer getVolteMOCallDrop() {
		return volteMOCallDrop;
	}

	/**
	 * @param volteMOCallDrop
	 *            the volteMOCallDrop to set
	 */
	public void setVolteMOCallDrop(Integer volteMOCallDrop) {
		this.volteMOCallDrop = volteMOCallDrop;
	}

	public String getRrcStateFirstValue() {
		return rrcStateFirstValue;
	}

	public void setRrcStateFirstValue(String rrcStateFirstValue) {
		this.rrcStateFirstValue = rrcStateFirstValue;
	}

	public Integer getNewHandOverIntiateCount() {
		return newHandOverIntiateCount;
	}

	public void setNewHandOverIntiateCount(Integer newHandOverIntiateCount) {
		this.newHandOverIntiateCount = newHandOverIntiateCount;
	}

	public Integer getNewHandOverFailureCount() {
		return newHandOverFailureCount;
	}

	public void setNewHandOverFailureCount(Integer newHandOverFailureCount) {
		this.newHandOverFailureCount = newHandOverFailureCount;
	}

	public Integer getNewHandOverSuccessCount() {
		return newHandOverSuccessCount;
	}

	public void setNewHandOverSuccessCount(Integer newHandOverSuccessCount) {
		this.newHandOverSuccessCount = newHandOverSuccessCount;
	}

	public Integer getNewHandOverIntiateInterCount() {
		return newHandOverIntiateInterCount;
	}

	public void setNewHandOverIntiateInterCount(Integer newHandOverIntiateInterCount) {
		this.newHandOverIntiateInterCount = newHandOverIntiateInterCount;
	}

	public Integer getNewHandOverFailureInterCount() {
		return newHandOverFailureInterCount;
	}

	public void setNewHandOverFailureInterCount(Integer newHandOverFailureInterCount) {
		this.newHandOverFailureInterCount = newHandOverFailureInterCount;
	}

	public Integer getNewHandOverSuccessInterCount() {
		return newHandOverSuccessInterCount;
	}

	public void setNewHandOverSuccessInterCount(Integer newHandOverSuccessInterCount) {
		this.newHandOverSuccessInterCount = newHandOverSuccessInterCount;
	}

	public Integer getNewHandOverIntiateIntraCount() {
		return newHandOverIntiateIntraCount;
	}

	public void setNewHandOverIntiateIntraCount(Integer newHandOverIntiateIntraCount) {
		this.newHandOverIntiateIntraCount = newHandOverIntiateIntraCount;
	}

	public Integer getNewHandOverFailureIntraCount() {
		return newHandOverFailureIntraCount;
	}

	public void setNewHandOverFailureIntraCount(Integer newHandOverFailureIntraCount) {
		this.newHandOverFailureIntraCount = newHandOverFailureIntraCount;
	}

	public Integer getNewHandOverSuccessIntraCount() {
		return newHandOverSuccessIntraCount;
	}

	public void setNewHandOverSuccessIntraCount(Integer newHandOverSuccessIntraCount) {
		this.newHandOverSuccessIntraCount = newHandOverSuccessIntraCount;
	}

	public Integer getVolteMTCallAttempts() {
		return volteMTCallAttempts;
	}

	public void setVolteMTCallAttempts(Integer volteMTCallAttempts) {
		this.volteMTCallAttempts = volteMTCallAttempts;
	}

	public Integer getVolteMTCallSuccess() {
		return volteMTCallSuccess;
	}

	public void setVolteMTCallSuccess(Integer volteMTCallSuccess) {
		this.volteMTCallSuccess = volteMTCallSuccess;
	}

	public Integer getVolteMOCallAttempts() {
		return volteMOCallAttempts;
	}

	public void setVolteMOCallAttempts(Integer volteMOCallAttempts) {
		this.volteMOCallAttempts = volteMOCallAttempts;
	}

	public Integer getVolteMOCallSuccess() {
		return volteMOCallSuccess;
	}

	public void setVolteMOCallSuccess(Integer volteMOCallSuccess) {
		this.volteMOCallSuccess = volteMOCallSuccess;
	}

	public Integer getVoltePagingAttempts() {
		return voltePagingAttempts;
	}

	public void setVoltePagingAttempts(Integer voltePagingAttempts) {
		this.voltePagingAttempts = voltePagingAttempts;
	}

	public Integer getVoltePagingSuccess() {
		return voltePagingSuccess;
	}

	public void setVoltePagingSuccess(Integer voltePagingSuccess) {
		this.voltePagingSuccess = voltePagingSuccess;
	}

	public Double[] getImsRegistrationSetupTime() {
		return imsRegistrationSetupTime;
	}

	public void setImsRegistrationSetupTime(Double[] imsRegistrationSetupTime) {
		this.imsRegistrationSetupTime = imsRegistrationSetupTime;
	}

	public Double getMinLatencyBufferSize32Bytes() {
		return minLatencyBufferSize32Bytes;
	}

	public void setMinLatencyBufferSize32Bytes(Double minLatencyBufferSize32Bytes) {
		this.minLatencyBufferSize32Bytes = minLatencyBufferSize32Bytes;
	}

	public Double getMinLatencyBufferSize1000Bytes() {
		return minLatencyBufferSize1000Bytes;
	}

	public void setMinLatencyBufferSize1000Bytes(Double minLatencyBufferSize1000Bytes) {
		this.minLatencyBufferSize1000Bytes = minLatencyBufferSize1000Bytes;
	}

	public Double getMinLatencyBufferSize1500Bytes() {
		return minLatencyBufferSize1500Bytes;
	}

	public void setMinLatencyBufferSize1500Bytes(Double minLatencyBufferSize1500Bytes) {
		this.minLatencyBufferSize1500Bytes = minLatencyBufferSize1500Bytes;
	}

	public Double getMaxLatencyBufferSize32Bytes() {
		return maxLatencyBufferSize32Bytes;
	}

	public void setMaxLatencyBufferSize32Bytes(Double maxLatencyBufferSize32Bytes) {
		this.maxLatencyBufferSize32Bytes = maxLatencyBufferSize32Bytes;
	}

	public Double getMaxLatencyBufferSize1000Bytes() {
		return maxLatencyBufferSize1000Bytes;
	}

	public void setMaxLatencyBufferSize1000Bytes(Double maxLatencyBufferSize1000Bytes) {
		this.maxLatencyBufferSize1000Bytes = maxLatencyBufferSize1000Bytes;
	}

	public Double getMaxLatencyBufferSize1500Bytes() {
		return maxLatencyBufferSize1500Bytes;
	}

	public void setMaxLatencyBufferSize1500Bytes(Double maxLatencyBufferSize1500Bytes) {
		this.maxLatencyBufferSize1500Bytes = maxLatencyBufferSize1500Bytes;
	}

	public Double[] getAvgLatencyBufferSize32Bytes() {
		return avgLatencyBufferSize32Bytes;
	}

	public void setAvgLatencyBufferSize32Bytes(Double[] avgLatencyBufferSize32Bytes) {
		this.avgLatencyBufferSize32Bytes = avgLatencyBufferSize32Bytes;
	}

	public Double[] getAvgLatencyBufferSize1000Bytes() {
		return avgLatencyBufferSize1000Bytes;
	}

	public void setAvgLatencyBufferSize1000Bytes(Double[] avgLatencyBufferSize1000Bytes) {
		this.avgLatencyBufferSize1000Bytes = avgLatencyBufferSize1000Bytes;
	}

	public Double[] getAvgLatencyBufferSize1500Bytes() {
		return avgLatencyBufferSize1500Bytes;
	}

	public void setAvgLatencyBufferSize1500Bytes(Double[] avgLatencyBufferSize1500Bytes) {
		this.avgLatencyBufferSize1500Bytes = avgLatencyBufferSize1500Bytes;
	}

	public Integer getLteMACRACHReasonRadioLinkFailureCount() {
		return lteMACRACHReasonRadioLinkFailureCount;
	}

	public void setLteMACRACHReasonRadioLinkFailureCount(Integer lteMACRACHReasonRadioLinkFailureCount) {
		this.lteMACRACHReasonRadioLinkFailureCount = lteMACRACHReasonRadioLinkFailureCount;
	}

	public Integer getActivateDedicatedEpsBearerContextAcceptCount() {
		return activateDedicatedEpsBearerContextAcceptCount;
	}

	public void setActivateDedicatedEpsBearerContextAcceptCount(Integer activateDedicatedEpsBearerContextAcceptCount) {
		this.activateDedicatedEpsBearerContextAcceptCount = activateDedicatedEpsBearerContextAcceptCount;
	}

	public Integer getReturnCSCallEnd() {
		return returnCSCallEnd;
	}

	public void setReturnCSCallEnd(Integer returnCSCallEnd) {
		this.returnCSCallEnd = returnCSCallEnd;
	}

	public Integer getMoCsfbFailure() {
		return moCsfbFailure;
	}

	public void setMoCsfbFailure(Integer moCsfbFailure) {
		this.moCsfbFailure = moCsfbFailure;
	}

	public Integer getMtCsfbFailure() {
		return mtCsfbFailure;
	}

	public void setMtCsfbFailure(Integer mtCsfbFailure) {
		this.mtCsfbFailure = mtCsfbFailure;
	}

	public Integer getServiceRequestSuccess() {
		return serviceRequestSuccess;
	}

	public void setServiceRequestSuccess(Integer serviceRequestSuccess) {
		this.serviceRequestSuccess = serviceRequestSuccess;
	}

	public Integer getRrcReconfigCompleteMissing() {
		return rrcReconfigCompleteMissing;
	}

	public void setRrcReconfigCompleteMissing(Integer rrcReconfigCompleteMissing) {
		this.rrcReconfigCompleteMissing = rrcReconfigCompleteMissing;
	}

	public Integer getMoCsfbRequest() {
		return moCsfbRequest;
	}

	public void setMoCsfbRequest(Integer moCsfbRequest) {
		this.moCsfbRequest = moCsfbRequest;
	}

	public Integer getMtCsfbRequest() {
		return mtCsfbRequest;
	}

	public void setMtCsfbRequest(Integer mtCsfbRequest) {
		this.mtCsfbRequest = mtCsfbRequest;
	}

	public Integer getMoCsfbSuccess() {
		return moCsfbSuccess;
	}

	public void setMoCsfbSuccess(Integer moCsfbSuccess) {
		this.moCsfbSuccess = moCsfbSuccess;
	}

	public Integer getMtCsfbSuccess() {
		return mtCsfbSuccess;
	}

	public void setMtCsfbSuccess(Integer mtCsfbSuccess) {
		this.mtCsfbSuccess = mtCsfbSuccess;
	}

	public Integer getUmtsLteRequest() {
		return umtsLteRequest;
	}

	public void setUmtsLteRequest(Integer umtsLteRequest) {
		this.umtsLteRequest = umtsLteRequest;
	}

	public Integer getUmtsLteSuccess() {
		return umtsLteSuccess;
	}

	public void setUmtsLteSuccess(Integer umtsLteSuccess) {
		this.umtsLteSuccess = umtsLteSuccess;
	}

	public Integer getErabSetup() {
		return erabSetup;
	}

	public void setErabSetup(Integer erabSetup) {
		this.erabSetup = erabSetup;
	}

	public Integer getErabSetupSuccess() {
		return erabSetupSuccess;
	}

	public void setErabSetupSuccess(Integer erabSetupSuccess) {
		this.erabSetupSuccess = erabSetupSuccess;
	}

	public Integer getAttachComplete() {
		return attachComplete;
	}

	public void setAttachComplete(Integer attachComplete) {
		this.attachComplete = attachComplete;
	}

	public Integer getDetachRequest() {
		return detachRequest;
	}

	public void setDetachRequest(Integer detachRequest) {
		this.detachRequest = detachRequest;
	}

	public Integer getVoiceBearerActivationRequestCount() {
		return voiceBearerActivationRequestCount;
	}

	public void setVoiceBearerActivationRequestCount(Integer voiceBearerActivationRequestCount) {
		this.voiceBearerActivationRequestCount = voiceBearerActivationRequestCount;
	}

	public Integer getVoiceBearerActivationSuccessCount() {
		return voiceBearerActivationSuccessCount;
	}

	public void setVoiceBearerActivationSuccessCount(Integer voiceBearerActivationSuccessCount) {
		this.voiceBearerActivationSuccessCount = voiceBearerActivationSuccessCount;
	}

	public Integer getVoiceBearerActivationFailureCount() {
		return voiceBearerActivationFailureCount;
	}

	public void setVoiceBearerActivationFailureCount(Integer voiceBearerActivationFailureCount) {
		this.voiceBearerActivationFailureCount = voiceBearerActivationFailureCount;
	}

	public Integer getVoiceBearerDeactivationRequestCount() {
		return voiceBearerDeactivationRequestCount;
	}

	public void setVoiceBearerDeactivationRequestCount(Integer voiceBearerDeactivationRequestCount) {
		this.voiceBearerDeactivationRequestCount = voiceBearerDeactivationRequestCount;
	}

	public Integer getInitialIMSRegistrationSuccessCount() {
		return initialIMSRegistrationSuccessCount;
	}

	public void setInitialIMSRegistrationSuccessCount(Integer initialIMSRegistrationSuccessCount) {
		this.initialIMSRegistrationSuccessCount = initialIMSRegistrationSuccessCount;
	}

	public Integer getInitialIMSRegistrationFailureCount() {
		return initialIMSRegistrationFailureCount;
	}

	public void setInitialIMSRegistrationFailureCount(Integer initialIMSRegistrationFailureCount) {
		this.initialIMSRegistrationFailureCount = initialIMSRegistrationFailureCount;
	}

	public Integer getLteEMMRegisteredEvent() {
		return lteEMMRegisteredEvent;
	}

	public void setLteEMMRegisteredEvent(Integer lteEMMRegisteredEvent) {
		this.lteEMMRegisteredEvent = lteEMMRegisteredEvent;
	}

	public Integer getLtePDNConnectionRequest() {
		return ltePDNConnectionRequest;
	}

	public void setLtePDNConnectionRequest(Integer ltePDNConnectionRequest) {
		this.ltePDNConnectionRequest = ltePDNConnectionRequest;
	}

	public Integer getLteRACHPreambleSuccessCount() {
		return lteRACHPreambleSuccessCount;
	}

	public void setLteRACHPreambleSuccessCount(Integer lteRACHPreambleSuccessCount) {
		this.lteRACHPreambleSuccessCount = lteRACHPreambleSuccessCount;
	}

	public Integer getLteRACHProcedureSuccessCount() {
		return lteRACHProcedureSuccessCount;
	}

	public void setLteRACHProcedureSuccessCount(Integer lteRACHProcedureSuccessCount) {
		this.lteRACHProcedureSuccessCount = lteRACHProcedureSuccessCount;
	}

	public Integer getLteRACHPreambleFailureCount() {
		return lteRACHPreambleFailureCount;
	}

	public void setLteRACHPreambleFailureCount(Integer lteRACHPreambleFailureCount) {
		this.lteRACHPreambleFailureCount = lteRACHPreambleFailureCount;
	}

	public Integer getLteRACHProcedureFailureCount() {
		return lteRACHProcedureFailureCount;
	}

	public void setLteRACHProcedureFailureCount(Integer lteRACHProcedureFailureCount) {
		this.lteRACHProcedureFailureCount = lteRACHProcedureFailureCount;
	}

	public Integer getLteUmtsRequest() {
		return lteUmtsRequest;
	}

	public void setLteUmtsRequest(Integer lteUmtsRequest) {
		this.lteUmtsRequest = lteUmtsRequest;
	}

	public Integer getLteUmtsSuccess() {
		return lteUmtsSuccess;
	}

	public void setLteUmtsSuccess(Integer lteUmtsSuccess) {
		this.lteUmtsSuccess = lteUmtsSuccess;
	}

	public Integer getRrcEutra() {
		return rrcEutra;
	}

	public void setRrcEutra(Integer rrcEutra) {
		this.rrcEutra = rrcEutra;
	}

	public Integer getRrcGeran() {
		return rrcGeran;
	}

	public void setRrcGeran(Integer rrcGeran) {
		this.rrcGeran = rrcGeran;
	}

	public Integer getRrcUtraFdd() {
		return rrcUtraFdd;
	}

	public void setRrcUtraFdd(Integer rrcUtraFdd) {
		this.rrcUtraFdd = rrcUtraFdd;
	}

	public Integer getRrcUtraTdd() {
		return rrcUtraTdd;
	}

	public void setRrcUtraTdd(Integer rrcUtraTdd) {
		this.rrcUtraTdd = rrcUtraTdd;
	}

	public Integer getRrcConnReconfFailure() {
		return rrcConnReconfFailure;
	}

	public void setRrcConnReconfFailure(Integer rrcConnReconfFailure) {
		this.rrcConnReconfFailure = rrcConnReconfFailure;
	}

	public Integer getReestablishmentFailure() {
		return reestablishmentFailure;
	}

	public void setReestablishmentFailure(Integer reestablishmentFailure) {
		this.reestablishmentFailure = reestablishmentFailure;
	}

	public Integer getRrcSetupFailure() {
		return rrcSetupFailure;
	}

	public void setRrcSetupFailure(Integer rrcSetupFailure) {
		this.rrcSetupFailure = rrcSetupFailure;
	}

	public Integer getMtAccess() {
		return mtAccess;
	}

	public void setMtAccess(Integer mtAccess) {
		this.mtAccess = mtAccess;
	}

	public Integer getMoSignalling() {
		return moSignalling;
	}

	public void setMoSignalling(Integer moSignalling) {
		this.moSignalling = moSignalling;
	}

	public Integer getSecurityModeComplete() {
		return securityModeComplete;
	}

	public void setSecurityModeComplete(Integer securityModeComplete) {
		this.securityModeComplete = securityModeComplete;
	}

	public Integer getServiceReject() {
		return serviceReject;
	}

	public void setServiceReject(Integer serviceReject) {
		this.serviceReject = serviceReject;
	}

	public Integer getServiceFailure() {
		return serviceFailure;
	}

	public void setServiceFailure(Integer serviceFailure) {
		this.serviceFailure = serviceFailure;
	}

	public Integer getAuthenticationResponse() {
		return authenticationResponse;
	}

	public void setAuthenticationResponse(Integer authenticationResponse) {
		this.authenticationResponse = authenticationResponse;
	}

	public Integer getAuthenticationFailure() {
		return authenticationFailure;
	}

	public void setAuthenticationFailure(Integer authenticationFailure) {
		this.authenticationFailure = authenticationFailure;
	}

	public Integer getAuthenticationReject() {
		return authenticationReject;
	}

	public void setAuthenticationReject(Integer authenticationReject) {
		this.authenticationReject = authenticationReject;
	}

	public Integer getAuthenticationRequest() {
		return authenticationRequest;
	}

	public void setAuthenticationRequest(Integer authenticationRequest) {
		this.authenticationRequest = authenticationRequest;
	}

	public Integer getRadioLinkFailure() {
		return radioLinkFailure;
	}

	public void setRadioLinkFailure(Integer radioLinkFailure) {
		this.radioLinkFailure = radioLinkFailure;
	}

	public Integer getRrcConnectionDropped() {
		return rrcConnectionDropped;
	}

	public void setRrcConnectionDropped(Integer rrcConnectionDropped) {
		this.rrcConnectionDropped = rrcConnectionDropped;
	}

	public Integer getRrcConnReconfiguration() {
		return rrcConnReconfiguration;
	}

	public void setRrcConnReconfiguration(Integer rrcConnReconfiguration) {
		this.rrcConnReconfiguration = rrcConnReconfiguration;
	}

	public Integer getRrcConnReconfSuccess() {
		return rrcConnReconfSuccess;
	}

	public void setRrcConnReconfSuccess(Integer rrcConnReconfSuccess) {
		this.rrcConnReconfSuccess = rrcConnReconfSuccess;
	}

	public Integer getServiceRequest() {
		return serviceRequest;
	}

	public void setServiceRequest(Integer serviceRequest) {
		this.serviceRequest = serviceRequest;
	}

	public Integer getRrcConnectionSetup() {
		return rrcConnectionSetup;
	}

	public void setRrcConnectionSetup(Integer rrcConnectionSetup) {
		this.rrcConnectionSetup = rrcConnectionSetup;
	}

	public Integer getPagingMessageCount() {
		return pagingMessageCount;
	}

	public void setPagingMessageCount(Integer pagingMessageCount) {
		this.pagingMessageCount = pagingMessageCount;
	}

	public String getEmmState() {
		return emmState;
	}

	public void setEmmState(String emmState) {
		this.emmState = emmState;
	}

	public String getRrcState() {
		return rrcState;
	}

	public void setRrcState(String rrcState) {
		this.rrcState = rrcState;
	}

	public String getEmmSubState() {
		return emmSubState;
	}

	public void setEmmSubState(String emmSubState) {
		this.emmSubState = emmSubState;
	}

	public Integer getB0C0Count() {
		return b0C0Count;
	}

	public void setB0C0Count(Integer b0c0Count) {
		b0C0Count = b0c0Count;
	}

	public Integer getB169Count() {
		return b169Count;
	}

	public void setB169Count(Integer b169Count) {
		this.b169Count = b169Count;
	}

	public Integer getTauAccept() {
		return tauAccept;
	}

	public void setTauAccept(Integer tauAccept) {
		this.tauAccept = tauAccept;
	}

	public Integer getTauRequest() {
		return tauRequest;
	}

	public void setTauRequest(Integer tauRequest) {
		this.tauRequest = tauRequest;
	}

	public Integer getAttachAccept() {
		return attachAccept;
	}

	public void setAttachAccept(Integer attachAccept) {
		this.attachAccept = attachAccept;
	}

	public Integer getAttachRequest() {
		return attachRequest;
	}

	public void setAttachRequest(Integer attachRequest) {
		this.attachRequest = attachRequest;
	}

	public Integer getMessage3Count() {
		return message3Count;
	}

	public void setMessage3Count(Integer message3Count) {
		this.message3Count = message3Count;
	}

	public Integer getMessage1Count() {
		return message1Count;
	}

	public void setMessage1Count(Integer message1Count) {
		this.message1Count = message1Count;
	}

	public Integer getRrcSetupComplete() {
		return rrcSetupComplete;
	}

	public void setRrcSetupComplete(Integer rrcSetupComplete) {
		this.rrcSetupComplete = rrcSetupComplete;
	}

	public Integer getRrcReestablishmentRequest() {
		return rrcReestablishmentRequest;
	}

	public void setRrcReestablishmentRequest(Integer rrcReestablishmentRequest) {
		this.rrcReestablishmentRequest = rrcReestablishmentRequest;
	}

	public Integer getRrcComplete() {
		return rrcComplete;
	}

	public void setRrcComplete(Integer rrcComplete) {
		this.rrcComplete = rrcComplete;
	}

	public Integer getRrcRequest() {
		return rrcRequest;
	}

	public void setRrcRequest(Integer rrcRequest) {
		this.rrcRequest = rrcRequest;
	}

	public Double[] getHandoverInterruption() {
		return handoverInterruption;
	}

	public void setHandoverInterruption(Double[] handoverInterruption) {
		this.handoverInterruption = handoverInterruption;
	}

	public Long getTempTimeStamp() {
		return tempTimeStamp;
	}

	public void setTempTimeStamp(Long tempTimeStamp) {
		this.tempTimeStamp = tempTimeStamp;
	}

	public Long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public Long getEtimeStamp() {
		return etimeStamp;
	}

	public void setEtimeStamp(Long etimeStamp) {
		this.etimeStamp = etimeStamp;
	}

	public Double getStartLat() {
		return startLat;
	}

	public void setStartLat(Double startLat) {
		this.startLat = startLat;
	}

	public Double getStartLong() {
		return startLong;
	}

	public void setStartLong(Double startLong) {
		this.startLong = startLong;
	}

	public Double getEndLat() {
		return endLat;
	}

	public void setEndLat(Double endLat) {
		this.endLat = endLat;
	}

	public Double getEndLong() {
		return endLong;
	}

	public void setEndLong(Double endLong) {
		this.endLong = endLong;
	}

	public Double[] getMeasureRSRPData() {
		return measureRSRPData;
	}

	public void setMeasureRSRPData(Double[] measureRSRPData) {
		this.measureRSRPData = measureRSRPData;
	}

	public Double[] getrSRPRx0Data() {
		return rSRPRx0Data;
	}

	public void setrSRPRx0Data(Double[] rSRPRx0Data) {
		this.rSRPRx0Data = rSRPRx0Data;
	}

	public Double[] getrSRPRx1Data() {
		return rSRPRx1Data;
	}

	public void setrSRPRx1Data(Double[] rSRPRx1Data) {
		this.rSRPRx1Data = rSRPRx1Data;
	}

	public Double[] getMeasureRSRQData() {
		return measureRSRQData;
	}

	public void setMeasureRSRQData(Double[] measureRSRQData) {
		this.measureRSRQData = measureRSRQData;
	}

	public Double[] getrSRQRx0Data() {
		return rSRQRx0Data;
	}

	public void setrSRQRx0Data(Double[] rSRQRx0Data) {
		this.rSRQRx0Data = rSRQRx0Data;
	}

	public Double[] getrSRQRx1Data() {
		return rSRQRx1Data;
	}

	public void setrSRQRx1Data(Double[] rSRQRx1Data) {
		this.rSRQRx1Data = rSRQRx1Data;
	}

	public Double[] getrSSIData() {
		return rSSIData;
	}

	public void setrSSIData(Double[] rSSIData) {
		this.rSSIData = rSSIData;
	}

	public Double[] getrSSIRx0Data() {
		return rSSIRx0Data;
	}

	public void setrSSIRx0Data(Double[] rSSIRx0Data) {
		this.rSSIRx0Data = rSSIRx0Data;
	}

	public Double[] getrSSIRx1Data() {
		return rSSIRx1Data;
	}

	public void setrSSIRx1Data(Double[] rSSIRx1Data) {
		this.rSSIRx1Data = rSSIRx1Data;
	}

	public Double[] getsINRData() {
		return sINRData;
	}

	public void setsINRData(Double[] sINRData) {
		this.sINRData = sINRData;
	}

	public Double[] getsINRRx0Data() {
		return sINRRx0Data;
	}

	public void setsINRRx0Data(Double[] sINRRx0Data) {
		this.sINRRx0Data = sINRRx0Data;
	}

	public Double[] getsINRRx1Data() {
		return sINRRx1Data;
	}

	public void setsINRRx1Data(Double[] sINRRx1Data) {
		this.sINRRx1Data = sINRRx1Data;
	}

	public String getRankIndex() {
		return rankIndex;
	}

	public void setRankIndex(String rankIndex) {
		this.rankIndex = rankIndex;
	}

	public Double[] getCqiCwo() {
		return cqiCwo;
	}

	public void setCqiCwo(Double[] cqiCwo) {
		this.cqiCwo = cqiCwo;
	}

	public String getCarrierIndex() {
		return carrierIndex;
	}

	public void setCarrierIndex(String carrierIndex) {
		this.carrierIndex = carrierIndex;
	}

	public Double[] getUlThroughPut() {
		return ulThroughPut;
	}

	public void setUlThroughPut(Double[] ulThroughPut) {
		this.ulThroughPut = ulThroughPut;
	}

	public Double[] getDlThroughPut() {
		return dlThroughPut;
	}

	public void setDlThroughPut(Double[] dlThroughPut) {
		this.dlThroughPut = dlThroughPut;
	}

	public Double[] getLatency() {
		return latency;
	}

	public void setLatency(Double[] latency) {
		this.latency = latency;
	}

	/*
	 * public Integer getHandOverIntiateCount() { return handOverIntiateCount; }
	 * 
	 * public void setHandOverIntiateCount(Integer handOverIntiateCount) {
	 * this.handOverIntiateCount = handOverIntiateCount; }
	 * 
	 * public Integer getHandOverFailureCount() { return handOverFailureCount; }
	 * 
	 * public void setHandOverFailureCount(Integer handOverFailureCount) {
	 * this.handOverFailureCount = handOverFailureCount; }
	 * 
	 * public Integer getHandOverSuccessCount() { return handOverSuccessCount; }
	 * 
	 * public void setHandOverSuccessCount(Integer handOverSuccessCount) {
	 * this.handOverSuccessCount = handOverSuccessCount; }
	 */

	public Integer getCallInitiateCount() {
		return callInitiateCount;
	}

	public void setCallInitiateCount(Integer callInitiateCount) {
		this.callInitiateCount = callInitiateCount;
	}

	public Integer getCallDropCount() {
		return callDropCount;
	}

	public void setCallDropCount(Integer callDropCount) {
		this.callDropCount = callDropCount;
	}

	public Integer getCallFailureCount() {
		return callFailureCount;
	}

	public void setCallFailureCount(Integer callFailureCount) {
		this.callFailureCount = callFailureCount;
	}

	public Integer getCallSuccessCount() {
		return callSuccessCount;
	}

	public void setCallSuccessCount(Integer callSuccessCount) {
		this.callSuccessCount = callSuccessCount;
	}

	public Integer getCellChangeCount() {
		return cellChangeCount;
	}

	public void setCellChangeCount(Integer cellChangeCount) {
		this.cellChangeCount = cellChangeCount;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getRrcInitiate() {
		return rrcInitiate;
	}

	public void setRrcInitiate(Integer rrcInitiate) {
		this.rrcInitiate = rrcInitiate;
	}

	public Integer getRrcSucess() {
		return rrcSucess;
	}

	public void setRrcSucess(Integer rrcSucess) {
		this.rrcSucess = rrcSucess;
	}

	public Set<Integer> getCellidSet() {
		return cellidSet;
	}

	public void setCellidSet(Set<Integer> cellidSet) {
		this.cellidSet = cellidSet;
	}

	public Set<String> getBandSet() {
		return bandSet;
	}

	public void setBandSet(Set<String> bandSet) {
		this.bandSet = bandSet;
	}

	public Set<String> getTechnologyBandSet() {
		return technologyBandSet;
	}

	public void setTechnologyBandSet(Set<String> technologyBandSet) {
		this.technologyBandSet = technologyBandSet;
	}

	public Integer getVoLTECallDropCount() {
		return voLTECallDropCount;
	}

	public void setVoLTECallDropCount(Integer voLTECallDropCount) {
		this.voLTECallDropCount = voLTECallDropCount;
	}

	public Integer getErabDrop() {
		return erabDrop;
	}

	public void setErabDrop(Integer erabDrop) {
		this.erabDrop = erabDrop;
	}

	public Integer getTotalErab() {
		return totalErab;
	}

	public void setTotalErab(Integer totalErab) {
		this.totalErab = totalErab;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public Set<String> getScript() {
		return script;
	}

	public void setScript(Set<String> script) {
		this.script = script;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public Set<String> getOperator() {
		return operator;
	}

	public void setOperator(Set<String> operator) {
		this.operator = operator;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public Set<String> getUlDlEarfcn() {
		return ulDlEarfcn;
	}

	public void setUlDlEarfcn(Set<String> ulDlEarfcn) {
		this.ulDlEarfcn = ulDlEarfcn;
	}

	public Integer getMimoCount() {
		return mimoCount;
	}

	public void setMimoCount(Integer mimoCount) {
		this.mimoCount = mimoCount;
	}

	public Integer getMimoTotalCount() {
		return mimoTotalCount;
	}

	public void setMimoTotalCount(Integer mimoTotalCount) {
		this.mimoTotalCount = mimoTotalCount;
	}

	public Integer getTddCount() {
		return tddCount;
	}

	public void setTddCount(Integer tddCount) {
		this.tddCount = tddCount;
	}

	public Integer getFddCount() {
		return fddCount;
	}

	public void setFddCount(Integer fddCount) {
		this.fddCount = fddCount;
	}

	public Integer getCountRsrpGreaterThan100Dbm() {
		return countRsrpGreaterThan100Dbm;
	}

	public void setCountRsrpGreaterThan100Dbm(Integer countRsrpGreaterThan100Dbm) {
		this.countRsrpGreaterThan100Dbm = countRsrpGreaterThan100Dbm;
	}

	public Integer getCountSinrGreaterThan12Db() {
		return countSinrGreaterThan12Db;
	}

	public void setCountSinrGreaterThan12Db(Integer countSinrGreaterThan12Db) {
		this.countSinrGreaterThan12Db = countSinrGreaterThan12Db;
	}

	public Integer getCountDlGreaterThan5Mbps() {
		return countDlGreaterThan5Mbps;
	}

	public void setCountDlGreaterThan5Mbps(Integer countDlGreaterThan5Mbps) {
		this.countDlGreaterThan5Mbps = countDlGreaterThan5Mbps;
	}

	public Map<Integer, Integer> getPciMap() {
		return pciMap;
	}

	public void setPciMap(Map<Integer, Integer> pciMap) {
		this.pciMap = pciMap;
	}

	public Map<Integer, Integer> getCellIdMap() {
		return cellIdMap;
	}

	public void setCellIdMap(Map<Integer, Integer> cellIdMap) {
		this.cellIdMap = cellIdMap;
	}

	public Map<Integer, Integer> getEnodeBMap() {
		return enodeBMap;
	}

	public void setEnodeBMap(Map<Integer, Integer> enodeBMap) {
		this.enodeBMap = enodeBMap;
	}

	public Map<Integer, Integer> getCgiMap() {
		return cgiMap;
	}

	public void setCgiMap(Map<Integer, Integer> cgiMap) {
		this.cgiMap = cgiMap;
	}

	public Double[] getWifiRssi() {
		return wifiRssi;
	}

	public void setWifiRssi(Double[] wifiRssi) {
		this.wifiRssi = wifiRssi;
	}

	public String getWifiBand() {
		return wifiBand;
	}

	public void setWifiBand(String wifiBand) {
		this.wifiBand = wifiBand;
	}

	public Double[] getWifiSnr() {
		return wifiSnr;
	}

	public void setWifiSnr(Double[] wifiSnr) {
		this.wifiSnr = wifiSnr;
	}

	public Set<String> getSsid() {
		return ssid;
	}

	public void setSsid(Set<String> ssid) {
		this.ssid = ssid;
	}

	public Integer getCntsnrgrtthn25dbm() {
		return cntsnrgrtthn25dbm;
	}

	public void setCntsnrgrtthn25dbm(Integer cntsnrgrtthn25dbm) {
		this.cntsnrgrtthn25dbm = cntsnrgrtthn25dbm;
	}

	public Integer getCntrssigrtthn90dbm() {
		return cntrssigrtthn90dbm;
	}

	public void setCntrssigrtthn90dbm(Integer cntrssigrtthn90dbm) {
		this.cntrssigrtthn90dbm = cntrssigrtthn90dbm;
	}

	public Double getMinLatency() {
		return minLatency;
	}

	public void setMinLatency(Double minLatency) {
		this.minLatency = minLatency;
	}

	public Double getMaxLatency() {
		return maxLatency;
	}

	public void setMaxLatency(Double maxLatency) {
		this.maxLatency = maxLatency;
	}

	public Double getMinDl() {
		return minDl;
	}

	public void setMinDl(Double minDl) {
		this.minDl = minDl;
	}

	public Double getMaxDl() {
		return maxDl;
	}

	public void setMaxDl(Double maxDl) {
		this.maxDl = maxDl;
	}

	public Double getMinUl() {
		return minUl;
	}

	public void setMinUl(Double minUl) {
		this.minUl = minUl;
	}

	public Double getMaxUl() {
		return maxUl;
	}

	public void setMaxUl(Double maxUl) {
		this.maxUl = maxUl;
	}

	public Double getMinJitter() {
		return minJitter;
	}

	public void setMinJitter(Double minJitter) {
		this.minJitter = minJitter;
	}

	public Double getMaxJitter() {
		return maxJitter;
	}

	public void setMaxJitter(Double maxJitter) {
		this.maxJitter = maxJitter;
	}

	public Double getMinresponseTime() {
		return minresponseTime;
	}

	public void setMinresponseTime(Double minresponseTime) {
		this.minresponseTime = minresponseTime;
	}

	public Double getMaxresponseTime() {
		return maxresponseTime;
	}

	public void setMaxresponseTime(Double maxresponseTime) {
		this.maxresponseTime = maxresponseTime;
	}

	public Set<String> getImei() {
		return imei;
	}

	public void setImei(Set<String> imei) {
		this.imei = imei;
	}

	public Double[] getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(Double[] responseTime) {
		this.responseTime = responseTime;
	}

	public Double[] getJitter() {
		return jitter;
	}

	public void setJitter(Double[] jitter) {
		this.jitter = jitter;
	}

	public Double getMinRSRP() {
		return minRSRP;
	}

	public void setMinRSRP(Double minRSRP) {
		this.minRSRP = minRSRP;
	}

	public Double getMaxRSRP() {
		return maxRSRP;
	}

	public void setMaxRSRP(Double maxRSRP) {
		this.maxRSRP = maxRSRP;
	}

	public Double getMinSINR() {
		return minSINR;
	}

	public void setMinSINR(Double minSINR) {
		this.minSINR = minSINR;
	}

	public Double getMaxSINR() {
		return maxSINR;
	}

	public void setMaxSINR(Double maxSINR) {
		this.maxSINR = maxSINR;
	}

	public StringBuilder getQuickTestJson() {
		return quickTestJson;
	}

	public void setQuickTestJson(StringBuilder quickTestJson) {
		this.quickTestJson = quickTestJson;
	}

	public StringBuilder getHandoverJson() {
		return handoverJson;
	}

	public void setHandoverJson(StringBuilder handoverJson) {
		this.handoverJson = handoverJson;
	}

	public Map<Integer, NvSMSDetailWrapper> getSmsData() {
		return smsData;
	}

	public void setSmsData(Map<Integer, NvSMSDetailWrapper> smsData) {
		this.smsData = smsData;
	}

	public boolean isPci() {
		return isPci;
	}

	public void setPci(boolean isPci) {
		this.isPci = isPci;
	}

	public boolean isIsearfcn() {
		return isearfcn;
	}

	public void setIsearfcn(boolean isearfcn) {
		this.isearfcn = isearfcn;
	}

	public boolean isIsband() {
		return isband;
	}

	public void setIsband(boolean isband) {
		this.isband = isband;
	}

	public boolean isIsrat() {
		return israt;
	}

	public void setIsrat(boolean israt) {
		this.israt = israt;
	}

	public Integer getlPci() {
		return lPci;
	}

	public void setlPci(Integer lPci) {
		this.lPci = lPci;
	}

	public Integer getlEarfcn() {
		return lEarfcn;
	}

	public void setlEarfcn(Integer lEarfcn) {
		this.lEarfcn = lEarfcn;
	}

	public Integer getlBand() {
		return lBand;
	}

	public void setlBand(Integer lBand) {
		this.lBand = lBand;
	}

	public String getlRat() {
		return lRat;
	}

	public void setlRat(String lRat) {
		this.lRat = lRat;
	}

	public String getRecipeName() {
		return recipeName;
	}

	public void setRecipeName(String recipeName) {
		this.recipeName = recipeName;
	}

	public Set<Integer> getEarfcn() {
		return earfcn;
	}

	public void setEarfcn(Set<Integer> earfcn) {
		this.earfcn = earfcn;
	}

	public Map<String, Double> getDlPeakValueMap() {
		return dlPeakValueMap;
	}

	public void setDlPeakValueMap(Map<String, Double> dlPeakValueMap) {
		this.dlPeakValueMap = dlPeakValueMap;
	}

	public Double[] getHandoverLatencyTime() {
		return handoverLatencyTime;
	}

	public void setHandoverLatencyTime(Double[] handoverLatencyTime) {
		this.handoverLatencyTime = handoverLatencyTime;
	}

	public Double getMinHttpDl() {
		return minHttpDl;
	}

	public void setMinHttpDl(Double minHttpDl) {
		this.minHttpDl = minHttpDl;
	}

	public Double getMaxHttpDl() {
		return maxHttpDl;
	}

	public void setMaxHttpDl(Double maxHttpDl) {
		this.maxHttpDl = maxHttpDl;
	}

	public Double getMinHttpUl() {
		return minHttpUl;
	}

	public void setMinHttpUl(Double minHttpUl) {
		this.minHttpUl = minHttpUl;
	}

	public Double getMaxHttpUl() {
		return maxHttpUl;
	}

	public void setMaxHttpUl(Double maxHttpUl) {
		this.maxHttpUl = maxHttpUl;
	}

	public Double getMinFtpDl() {
		return minFtpDl;
	}

	public void setMinFtpDl(Double minFtpDl) {
		this.minFtpDl = minFtpDl;
	}

	public Double getMaxFtpDl() {
		return maxFtpDl;
	}

	public void setMaxFtpDl(Double maxFtpDl) {
		this.maxFtpDl = maxFtpDl;
	}

	public Double getMinFtpUl() {
		return minFtpUl;
	}

	public void setMinFtpUl(Double minFtpUl) {
		this.minFtpUl = minFtpUl;
	}

	public Double getMaxFtpUl() {
		return maxFtpUl;
	}

	public void setMaxFtpUl(Double maxFtpUl) {
		this.maxFtpUl = maxFtpUl;
	}

	public Double[] getHttpDl() {
		return httpDl;
	}

	public void setHttpDl(Double[] httpDl) {
		this.httpDl = httpDl;
	}

	public Double[] getHttpUl() {
		return httpUl;
	}

	public void setHttpUl(Double[] httpUl) {
		this.httpUl = httpUl;
	}

	public Double[] getFtpDl() {
		return ftpDl;
	}

	public void setFtpDl(Double[] ftpDl) {
		this.ftpDl = ftpDl;
	}

	public Double[] getFtpUl() {
		return ftpUl;
	}

	public void setFtpUl(Double[] ftpUl) {
		this.ftpUl = ftpUl;
	}

	public Double getMinRsrq() {
		return minRsrq;
	}

	public void setMinRsrq(Double minRsrq) {
		this.minRsrq = minRsrq;
	}

	public Double getMaxRsrq() {
		return maxRsrq;
	}

	public void setMaxRsrq(Double maxRsrq) {
		this.maxRsrq = maxRsrq;
	}

	public Double getMinRssi() {
		return minRssi;
	}

	public void setMinRssi(Double minRssi) {
		this.minRssi = minRssi;
	}

	public Double getMaxRssi() {
		return maxRssi;
	}

	public void setMaxRssi(Double maxRssi) {
		this.maxRssi = maxRssi;
	}

	public Integer getCallInitiateOnNetCount() {
		return callInitiateOnNetCount;
	}

	public void setCallInitiateOnNetCount(Integer callInitiateOnNetCount) {
		this.callInitiateOnNetCount = callInitiateOnNetCount;
	}

	public Integer getCallDropOnNetCount() {
		return callDropOnNetCount;
	}

	public void setCallDropOnNetCount(Integer callDropOnNetCount) {
		this.callDropOnNetCount = callDropOnNetCount;
	}

	public Integer getCallFailureOnNetCount() {
		return callFailureOnNetCount;
	}

	public void setCallFailureOnNetCount(Integer callFailureOnNetCount) {
		this.callFailureOnNetCount = callFailureOnNetCount;
	}

	public Integer getCallSuccessOnNetCount() {
		return callSuccessOnNetCount;
	}

	public void setCallSuccessOnNetCount(Integer callSuccessOnNetCount) {
		this.callSuccessOnNetCount = callSuccessOnNetCount;
	}

	public Integer getCallInitiateOffNetCount() {
		return callInitiateOffNetCount;
	}

	public void setCallInitiateOffNetCount(Integer callInitiateOffNetCount) {
		this.callInitiateOffNetCount = callInitiateOffNetCount;
	}

	public Integer getCallDropOffNetCount() {
		return callDropOffNetCount;
	}

	public void setCallDropOffNetCount(Integer callDropOffNetCount) {
		this.callDropOffNetCount = callDropOffNetCount;
	}

	public Integer getCallFailureOffNetCount() {
		return callFailureOffNetCount;
	}

	public void setCallFailureOffNetCount(Integer callFailureOffNetCount) {
		this.callFailureOffNetCount = callFailureOffNetCount;
	}

	public Integer getCallSuccessOffNetCount() {
		return callSuccessOffNetCount;
	}

	public void setCallSuccessOffNetCount(Integer callSuccessOffNetCount) {
		this.callSuccessOffNetCount = callSuccessOffNetCount;
	}

	public Double[] getFinalMosG711() {
		return finalMosG711;
	}

	public void setFinalMosG711(Double[] finalMosG711) {
		this.finalMosG711 = finalMosG711;
	}

	public Double[] getFinalMosILbc() {
		return finalMosILbc;
	}

	public void setFinalMosILbc(Double[] finalMosILbc) {
		this.finalMosILbc = finalMosILbc;
	}

	public Double[] getFinalMosG711Onnet() {
		return finalMosG711Onnet;
	}

	public void setFinalMosG711Onnet(Double[] finalMosG711Onnet) {
		this.finalMosG711Onnet = finalMosG711Onnet;
	}

	public Double[] getFinalMosILbcOnnet() {
		return finalMosILbcOnnet;
	}

	public void setFinalMosILbcOnnet(Double[] finalMosILbcOnnet) {
		this.finalMosILbcOnnet = finalMosILbcOnnet;
	}

	public Double[] getFinalMosG711Offnet() {
		return finalMosG711Offnet;
	}

	public void setFinalMosG711Offnet(Double[] finalMosG711Offnet) {
		this.finalMosG711Offnet = finalMosG711Offnet;
	}

	public Double[] getFinalMosILbcOffnet() {
		return finalMosILbcOffnet;
	}

	public void setFinalMosILbcOffnet(Double[] finalMosILbcOffnet) {
		this.finalMosILbcOffnet = finalMosILbcOffnet;
	}

	public Double[] getCallConnectionSetupTimeOnnet() {
		return callConnectionSetupTimeOnnet;
	}

	public void setCallConnectionSetupTimeOnnet(Double[] callConnectionSetupTimeOnnet) {
		this.callConnectionSetupTimeOnnet = callConnectionSetupTimeOnnet;
	}

	public Double[] getCallConnectionSetupTimeOffnet() {
		return callConnectionSetupTimeOffnet;
	}

	public void setCallConnectionSetupTimeOffnet(Double[] callConnectionSetupTimeOffnet) {
		this.callConnectionSetupTimeOffnet = callConnectionSetupTimeOffnet;
	}

	public Double[] getConnectionSetupTime() {
		return connectionSetupTime;
	}

	public void setConnectionSetupTime(Double[] connectionSetupTime) {
		this.connectionSetupTime = connectionSetupTime;
	}

	public Integer getTotalHTTPAttempt() {
		return totalHTTPAttempt;
	}

	public void setTotalHTTPAttempt(Integer totalHTTPAttempt) {
		this.totalHTTPAttempt = totalHTTPAttempt;
	}

	public Integer getTotalHTTPSuccess() {
		return totalHTTPSuccess;
	}

	public void setTotalHTTPSuccess(Integer totalHTTPSuccess) {
		this.totalHTTPSuccess = totalHTTPSuccess;
	}

	public Integer getTotalHTTPFailure() {
		return totalHTTPFailure;
	}

	public void setTotalHTTPFailure(Integer totalHTTPFailure) {
		this.totalHTTPFailure = totalHTTPFailure;
	}

	public Long getTotalHTTPTimeTaken() {
		return totalHTTPTimeTaken;
	}

	public void setTotalHTTPTimeTaken(Long totalHTTPTimeTaken) {
		this.totalHTTPTimeTaken = totalHTTPTimeTaken;
	}

	public Double[] getTotalHTTPthroughPut() {
		return totalHTTPthroughPut;
	}

	public void setTotalHTTPthroughPut(Double[] totalHTTPthroughPut) {
		this.totalHTTPthroughPut = totalHTTPthroughPut;
	}

	public TreeMap<Long, String> getCallNetStatusMap() {
		return callNetStatusMap;
	}

	public void setCallNetStatusMap(TreeMap<Long, String> callNetStatusMap) {
		this.callNetStatusMap = callNetStatusMap;
	}

	public TreeMap<Long, String> getCallNetStatusOnSuccessMap() {
		return callNetStatusOnSuccessMap;
	}

	public void setCallNetStatusOnSuccessMap(TreeMap<Long, String> callNetStatusOnSuccessMap) {
		this.callNetStatusOnSuccessMap = callNetStatusOnSuccessMap;
	}

	public Map<String, YoutubeWrapper> getYouTubeTest() {
		return youTubeTest;
	}

	public void setYouTubeTest(Map<String, YoutubeWrapper> youTubeTest) {
		this.youTubeTest = youTubeTest;
	}

	public Double[] getCallConnectionSetupTime() {
		return callConnectionSetupTime;
	}

	public void setCallConnectionSetupTime(Double[] callConnectionSetupTime) {
		this.callConnectionSetupTime = callConnectionSetupTime;
	}

	public boolean isPmi() {
		return isPmi;
	}

	public void setPmi(boolean isPmi) {
		this.isPmi = isPmi;
	}

	public boolean isBandwidthUL() {
		return isBandwidthUL;
	}

	public void setBandwidthUL(boolean isBandwidthUL) {
		this.isBandwidthUL = isBandwidthUL;
	}

	public boolean isBandwidthDL() {
		return isBandwidthDL;
	}

	public void setBandwidthDL(boolean isBandwidthDL) {
		this.isBandwidthDL = isBandwidthDL;
	}

	public boolean isSpatialIndex() {
		return isSpatialIndex;
	}

	public void setSpatialIndex(boolean isSpatialIndex) {
		this.isSpatialIndex = isSpatialIndex;
	}

	public boolean isPowerTxPusch() {
		return isPowerTxPusch;
	}

	public void setPowerTxPusch(boolean isPowerTxPusch) {
		this.isPowerTxPusch = isPowerTxPusch;
	}

	public boolean isTimingAdvance() {
		return isTimingAdvance;
	}

	public void setTimingAdvance(boolean isTimingAdvance) {
		this.isTimingAdvance = isTimingAdvance;
	}

	public boolean isBler() {
		return isBler;
	}

	public void setBler(boolean isBler) {
		this.isBler = isBler;
	}

	public boolean isMcs() {
		return isMcs;
	}

	public void setMcs(boolean isMcs) {
		this.isMcs = isMcs;
	}

	public boolean isDlEarfcn() {
		return isDlEarfcn;
	}

	public void setDlEarfcn(boolean isDlEarfcn) {
		this.isDlEarfcn = isDlEarfcn;
	}

	public boolean isUlEarfcn() {
		return isUlEarfcn;
	}

	public void setUlEarfcn(boolean isUlEarfcn) {
		this.isUlEarfcn = isUlEarfcn;
	}

	public boolean isRI() {
		return isRI;
	}

	public void setRI(boolean isRI) {
		this.isRI = isRI;
	}

	public boolean isCarrierIndex() {
		return isCarrierIndex;
	}

	public void setCarrierIndex(boolean isCarrierIndex) {
		this.isCarrierIndex = isCarrierIndex;
	}

	public Set<String> getKpiList() {
		return kpiList;
	}

	public void setKpiList(Set<String> kpiList) {
		this.kpiList = kpiList;
	}

	public Set<String> getMobilityList() {
		return mobilityList;
	}

	public void setMobilityList(Set<String> mobilityList) {
		this.mobilityList = mobilityList;
	}

	public Set<String> getCallList() {
		return callList;
	}

	public void setCallList(Set<String> callList) {
		this.callList = callList;
	}

	public Set<String> getDownloadList() {
		return downloadList;
	}

	public void setDownloadList(Set<String> downloadList) {
		this.downloadList = downloadList;
	}

	public Set<String> getSmsList() {
		return smsList;
	}

	public void setSmsList(Set<String> smsList) {
		this.smsList = smsList;
	}

	public Set<String> getTauList() {
		return tauList;
	}

	public void setTauList(Set<String> tauList) {
		this.tauList = tauList;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Double[] getYoutubeThroughtPut() {
		return youtubeThroughtPut;
	}

	public void setYoutubeThroughtPut(Double[] youtubeThroughtPut) {
		this.youtubeThroughtPut = youtubeThroughtPut;
	}

	public Double getYoutubeBufferTime() {
		return youtubeBufferTime;
	}

	public void setYoutubeBufferTime(Double youtubeBufferTime) {
		this.youtubeBufferTime = youtubeBufferTime;
	}

	public Double[] getPingPacketLoss() {
		return pingPacketLoss;
	}

	public void setPingPacketLoss(Double[] pingPacketLoss) {
		this.pingPacketLoss = pingPacketLoss;
	}

	public Double[] getWptDns() {
		return wptDns;
	}

	public void setWptDns(Double[] wptDns) {
		this.wptDns = wptDns;
	}

	public Double[] getWptUrl() {
		return wptUrl;
	}

	public void setWptUrl(Double[] wptUrl) {
		this.wptUrl = wptUrl;
	}

	public Double[] getUlThroughPutAdhoc() {
		return ulThroughPutAdhoc;
	}

	public void setUlThroughPutAdhoc(Double[] ulThroughPutAdhoc) {
		this.ulThroughPutAdhoc = ulThroughPutAdhoc;
	}

	public Double[] getDlThroughPutAdhoc() {
		return dlThroughPutAdhoc;
	}

	public void setDlThroughPutAdhoc(Double[] dlThroughPutAdhoc) {
		this.dlThroughPutAdhoc = dlThroughPutAdhoc;
	}

	public Double[] getLatencyAdhoc() {
		return latencyAdhoc;
	}

	public void setLatencyAdhoc(Double[] latencyAdhoc) {
		this.latencyAdhoc = latencyAdhoc;
	}

	public StringBuilder getRemarkJson() {
		return remarkJson;
	}

	public void setRemarkJson(StringBuilder remarkJson) {
		this.remarkJson = remarkJson;
	}

	public StringBuilder getTestSkipJson() {
		return testSkipJson;
	}

	public void setTestSkipJson(StringBuilder testSkipJson) {
		this.testSkipJson = testSkipJson;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getRecipeId() {
		return recipeId;
	}

	public void setRecipeId(String recipeId) {
		this.recipeId = recipeId;
	}

	public Integer getHttpAttempt() {
		return httpAttempt;
	}

	public void setHttpAttempt(Integer httpAttempt) {
		this.httpAttempt = httpAttempt;
	}

	public Integer getHttpSucess() {
		return httpSucess;
	}

	public void setHttpSucess(Integer httpSucess) {
		this.httpSucess = httpSucess;
	}

	public Integer getHttpFailure() {
		return httpFailure;
	}

	public void setHttpFailure(Integer httpFailure) {
		this.httpFailure = httpFailure;
	}

	public Integer getHttpDrop() {
		return httpDrop;
	}

	public void setHttpDrop(Integer httpDrop) {
		this.httpDrop = httpDrop;
	}

	public Double[] getHttpDownLoadTime() {
		return httpDownLoadTime;
	}

	public void setHttpDownLoadTime(Double[] httpDownLoadTime) {
		this.httpDownLoadTime = httpDownLoadTime;
	}

	public Integer getSmsFailure() {
		return smsFailure;
	}

	public void setSmsFailure(Integer smsFailure) {
		this.smsFailure = smsFailure;
	}

	public Double getTotalDistance() {
		return totalDistance;
	}

	public void setTotalDistance(Double totalDistance) {
		this.totalDistance = totalDistance;
	}

	public String getBattaryInfo() {
		return battaryInfo;
	}

	public void setBattaryInfo(String battaryInfo) {
		this.battaryInfo = battaryInfo;
	}

	public String getChipsetInfo() {
		return chipsetInfo;
	}

	public void setChipsetInfo(String chipsetInfo) {
		this.chipsetInfo = chipsetInfo;
	}

	public String getDriveVersion() {
		return driveVersion;
	}

	public void setDriveVersion(String driveVersion) {
		this.driveVersion = driveVersion;
	}

	public Integer getTauAttempt() {
		return tauAttempt;
	}

	public void setTauAttempt(Integer tauAttempt) {
		this.tauAttempt = tauAttempt;
	}

	public Integer getTauComplete() {
		return tauComplete;
	}

	public void setTauComplete(Integer tauComplete) {
		this.tauComplete = tauComplete;
	}

	public Integer getTauFailure() {
		return tauFailure;
	}

	public void setTauFailure(Integer tauFailure) {
		this.tauFailure = tauFailure;
	}

	public Double[] getTauTime() {
		return tauTime;
	}

	public void setTauTime(Double[] tauTime) {
		this.tauTime = tauTime;
	}

	public Double[] getInstantaneousMos() {
		return instantaneousMos;
	}

	public void setInstantaneousMos(Double[] instantaneousMos) {
		this.instantaneousMos = instantaneousMos;
	}

	public Double getMinInstantaneousMos() {
		return minInstantaneousMos;
	}

	public void setMinInstantaneousMos(Double minInstantaneousMos) {
		this.minInstantaneousMos = minInstantaneousMos;
	}

	public Double getMaxInstantaneousMos() {
		return maxInstantaneousMos;
	}

	public void setMaxInstantaneousMos(Double maxInstantaneousMos) {
		this.maxInstantaneousMos = maxInstantaneousMos;
	}

	public Integer getCsfbCallAttempt() {
		return csfbCallAttempt;
	}

	public void setCsfbCallAttempt(Integer csfbCallAttempt) {
		this.csfbCallAttempt = csfbCallAttempt;
	}

	public Integer getCsfbCallSuccess() {
		return csfbCallSuccess;
	}

	public void setCsfbCallSuccess(Integer csfbCallSuccess) {
		this.csfbCallSuccess = csfbCallSuccess;
	}

	public Integer getCsfbCallDrop() {
		return csfbCallDrop;
	}

	public void setCsfbCallDrop(Integer csfbCallDrop) {
		this.csfbCallDrop = csfbCallDrop;
	}

	public Integer getCsfbCallFailed() {
		return csfbCallFailed;
	}

	public void setCsfbCallFailed(Integer csfbCallFailed) {
		this.csfbCallFailed = csfbCallFailed;
	}

	public Integer getCsfbCallSetupSuccess() {
		return csfbCallSetupSuccess;
	}

	public void setCsfbCallSetupSuccess(Integer csfbCallSetupSuccess) {
		this.csfbCallSetupSuccess = csfbCallSetupSuccess;
	}

	public Integer getCsfbCallSetupFailed() {
		return csfbCallSetupFailed;
	}

	public void setCsfbCallSetupFailed(Integer csfbCallSetupFailed) {
		this.csfbCallSetupFailed = csfbCallSetupFailed;
	}

	public Double[] getPDSCHThroughput() {
		return pdschThroughput;
	}

	public void setPDSCHThroughput(Double[] pdschThroughput) {
		this.pdschThroughput = pdschThroughput;
	}

	public Double[] getPuschThroughput() {
		return puschThroughput;
	}

	public void setPuschThroughput(Double[] puschThroughput) {
		this.puschThroughput = puschThroughput;
	}

	public Boolean getPuschMcsIndex() {
		return puschMcsIndex;
	}

	public void setPuschMcsIndex(Boolean puschMcsIndex) {
		this.puschMcsIndex = puschMcsIndex;
	}

	public String getPuschModulationType() {
		return puschModulationType;
	}

	public void setPuschModulationType(String puschModulationType) {
		this.puschModulationType = puschModulationType;
	}

	public Integer getRrcConnectionComplete() {
		return rrcConnectionComplete;
	}

	public void setRrcConnectionComplete(Integer rrcConnectionComplete) {
		this.rrcConnectionComplete = rrcConnectionComplete;
	}

	public Integer getRrcConnectionSetupOk() {
		return rrcConnectionSetupOk;
	}

	public void setRrcConnectionSetupOk(Integer rrcConnectionSetupOk) {
		this.rrcConnectionSetupOk = rrcConnectionSetupOk;
	}

	public Integer getRrcConnectionAttempt() {
		return rrcConnectionAttempt;
	}

	public void setRrcConnectionAttempt(Integer rrcConnectionAttempt) {
		this.rrcConnectionAttempt = rrcConnectionAttempt;
	}

	public Integer getRrcConnectionRelease() {
		return rrcConnectionRelease;
	}

	public void setRrcConnectionRelease(Integer rrcConnectionRelease) {
		this.rrcConnectionRelease = rrcConnectionRelease;
	}

	public Integer getRrcConnectionFailed() {
		return rrcConnectionFailed;
	}

	public void setRrcConnectionFailed(Integer rrcConnectionFailed) {
		this.rrcConnectionFailed = rrcConnectionFailed;
	}

	public Double[] getRrcConnectionSetupTime() {
		return rrcConnectionSetupTime;
	}

	public void setRrcConnectionSetupTime(Double[] rrcConnectionSetupTime) {
		this.rrcConnectionSetupTime = rrcConnectionSetupTime;
	}

	public Integer getRrcReestablishmentSuccess() {
		return rrcReestablishmentSuccess;
	}

	public void setRrcReestablishmentSuccess(Integer rrcReestablishmentSuccess) {
		this.rrcReestablishmentSuccess = rrcReestablishmentSuccess;
	}

	public Integer getRrcReestablishmentFailed() {
		return rrcReestablishmentFailed;
	}

	public void setRrcReestablishmentFailed(Integer rrcReestablishmentFailed) {
		this.rrcReestablishmentFailed = rrcReestablishmentFailed;
	}

	public Double[] getCqiCw1() {
		return cqiCw1;
	}

	public void setCqiCw1(Double[] cqiCw1) {
		this.cqiCw1 = cqiCw1;
	}

	public Double[] getDlPrb() {
		return dlPrb;
	}

	public void setDlPrb(Double[] dlPrb) {
		this.dlPrb = dlPrb;
	}

	public Double[] getPdschnumRb() {
		return pdschnumRb;
	}

	public void setPdschnumRb(Double[] pdschnumRb) {
		this.pdschnumRb = pdschnumRb;
	}

	public String getPdschCwoModulation() {
		return pdschCwoModulation;
	}

	public void setPdschCwoModulation(String pdschCwoModulation) {
		this.pdschCwoModulation = pdschCwoModulation;
	}

	public Integer getPdschCwoMcs() {
		return pdschCwoMcs;
	}

	public void setPdschCwoMcs(Integer pdschCwoMcs) {
		this.pdschCwoMcs = pdschCwoMcs;
	}

	public Double[] getNumRB() {
		return numRB;
	}

	public void setNumRB(Double[] numRB) {
		this.numRB = numRB;
	}

	public String getTransmissionMode() {
		return transmissionMode;
	}

	public void setTransmissionMode(String transmissionMode) {
		this.transmissionMode = transmissionMode;
	}

	/**
	 * @return the tb016QAMUtilization
	 */
	public Double getTb016QAMUtilization() {
		return tb016QAMUtilization;
	}

	/**
	 * @param tb016qamUtilization
	 *            the tb016QAMUtilization to set
	 */
	public void setTb016QAMUtilization(Double tb016qamUtilization) {
		this.tb016QAMUtilization = tb016qamUtilization;
	}

	public Double[] getDominantChannelRSSI() {
		return dominantChannelRSSI;
	}

	public void setDominantChannelRSSI(Double[] dominantChannelRSSI) {
		this.dominantChannelRSSI = dominantChannelRSSI;
	}

	public Double[] getDominantChannelRSSIRx0() {
		return dominantChannelRSSIRx0;
	}

	public void setDominantChannelRSSIRx0(Double[] dominantChannelRSSIRx0) {
		this.dominantChannelRSSIRx0 = dominantChannelRSSIRx0;
	}

	public Double[] getDominantChannelRSSIRx1() {
		return dominantChannelRSSIRx1;
	}

	public void setDominantChannelRSSIRx1(Double[] dominantChannelRSSIRx1) {
		this.dominantChannelRSSIRx1 = dominantChannelRSSIRx1;
	}

	public Double[] getDominantChannelRSRQ() {
		return dominantChannelRSRQ;
	}

	public void setDominantChannelRSRQ(Double[] dominantChannelRSRQ) {
		this.dominantChannelRSRQ = dominantChannelRSRQ;
	}

	public Double[] getDominantChannelRSRQRx0() {
		return dominantChannelRSRQRx0;
	}

	public void setDominantChannelRSRQRx0(Double[] dominantChannelRSRQRx0) {
		this.dominantChannelRSRQRx0 = dominantChannelRSRQRx0;
	}

	public Double[] getDominantChannelRSRQRx1() {
		return dominantChannelRSRQRx1;
	}

	public void setDominantChannelRSRQRx1(Double[] dominantChannelRSRQRx1) {
		this.dominantChannelRSRQRx1 = dominantChannelRSRQRx1;
	}

	public Double[] getDominantChannelRSRP() {
		return dominantChannelRSRP;
	}

	public void setDominantChannelRSRP(Double[] dominantChannelRSRP) {
		this.dominantChannelRSRP = dominantChannelRSRP;
	}

	public Double[] getDominantChannelRSRPRx0() {
		return dominantChannelRSRPRx0;
	}

	public void setDominantChannelRSRPRx0(Double[] dominantChannelRSRPRx0) {
		this.dominantChannelRSRPRx0 = dominantChannelRSRPRx0;
	}

	public Double[] getDominantChannelRSRPRx1() {
		return dominantChannelRSRPRx1;
	}

	public void setDominantChannelRSRPRx1(Double[] dominantChannelRSRPRx1) {
		this.dominantChannelRSRPRx1 = dominantChannelRSRPRx1;
	}

	public Integer getDominantChannelPCI() {
		return dominantChannelPCI;
	}

	public void setDominantChannelPCI(Integer dominantChannelPCI) {
		this.dominantChannelPCI = dominantChannelPCI;
	}

	public Long getLteDominantFrequency() {
		return lteDominantFrequency;
	}

	public void setLteDominantFrequency(Long lteDominantFrequency) {
		this.lteDominantFrequency = lteDominantFrequency;
	}

	/**
	 * @return the tb0McsIndex
	 */
	public Integer getTb0McsIndex() {
		return tb0McsIndex;
	}

	/**
	 * @param tb0McsIndex
	 *            the tb0McsIndex to set
	 */
	public void setTb0McsIndex(Integer tb0McsIndex) {
		this.tb0McsIndex = tb0McsIndex;
	}

	/**
	 * @return the tb1McsIndex
	 */
	public Integer getTb1McsIndex() {
		return tb1McsIndex;
	}

	/**
	 * @param tb1McsIndex
	 *            the tb1McsIndex to set
	 */
	public void setTb1McsIndex(Integer tb1McsIndex) {
		this.tb1McsIndex = tb1McsIndex;
	}

	/**
	 * @return the tb0ModulationType
	 */
	public String getTb0ModulationType() {
		return tb0ModulationType;
	}

	/**
	 * @param tb0ModulationType
	 *            the tb0ModulationType to set
	 */
	public void setTb0ModulationType(String tb0ModulationType) {
		this.tb0ModulationType = tb0ModulationType;
	}

	/**
	 * @return the tb1ModulationType
	 */
	public String getTb1ModulationType() {
		return tb1ModulationType;
	}

	/**
	 * @param tb1ModulationType
	 *            the tb1ModulationType to set
	 */
	public void setTb1ModulationType(String tb1ModulationType) {
		this.tb1ModulationType = tb1ModulationType;
	}

	/**
	 * @return the prachTxPower
	 */
	public Double getPrachTxPower() {
		return prachTxPower;
	}

	/**
	 * @param prachTxPower
	 *            the prachTxPower to set
	 */
	public void setPrachTxPower(Double prachTxPower) {
		this.prachTxPower = prachTxPower;
	}

	/**
	 * @return the powerHeadroomdata
	 */
	public Integer getPowerHeadroomdata() {
		return powerHeadroomdata;
	}

	/**
	 * @param powerHeadroomdata
	 *            the powerHeadroomdata to set
	 */
	public void setPowerHeadroomdata(Integer powerHeadroomdata) {
		this.powerHeadroomdata = powerHeadroomdata;
	}

	/**
	 * @return the avgDLTb0Size
	 */
	public Double getAvgDLTb0Size() {
		return avgDLTb0Size;
	}

	/**
	 * @param avgDLTb0Size
	 *            the avgDLTb0Size to set
	 */
	public void setAvgDLTb0Size(Double avgDLTb0Size) {
		this.avgDLTb0Size = avgDLTb0Size;
	}

	/**
	 * @return the avgDLTb1Size
	 */
	public Double getAvgDLTb1Size() {
		return avgDLTb1Size;
	}

	/**
	 * @param avgDLTb1Size
	 *            the avgDLTb1Size to set
	 */
	public void setAvgDLTb1Size(Double avgDLTb1Size) {
		this.avgDLTb1Size = avgDLTb1Size;
	}

	/**
	 * @return the avgDLTbSize
	 */
	public Double getAvgDLTbSize() {
		return avgDLTbSize;
	}

	/**
	 * @param avgDLTbSize
	 *            the avgDLTbSize to set
	 */
	public void setAvgDLTbSize(Double avgDLTbSize) {
		this.avgDLTbSize = avgDLTbSize;
	}

	/**
	 * @return the avgULTBSize
	 */
	public Double getAvgULTBSize() {
		return avgULTBSize;
	}

	/**
	 * @param avgULTBSize
	 *            the avgULTBSize to set
	 */
	public void setAvgULTBSize(Double avgULTBSize) {
		this.avgULTBSize = avgULTBSize;
	}

	/**
	 * @return the pdschBLER
	 */
	public Double[] getPdschBLER() {
		return pdschBLER;
	}

	/**
	 * @param pdschBLER
	 *            the pdschBLER to set
	 */
	public void setPdschBLER(Double[] pdschBLER) {
		this.pdschBLER = pdschBLER;
	}

	/**
	 * @return the macUlThroughput
	 */
	public Double[] getMacUlThroughput() {
		return macUlThroughput;
	}

	/**
	 * @param macUlThroughput
	 *            the macUlThroughput to set
	 */
	public void setMacUlThroughput(Double[] macUlThroughput) {
		this.macUlThroughput = macUlThroughput;
	}

	public Integer getMNC() {
		return MNC;
	}

	public void setMNC(Integer mNC) {
		MNC = mNC;
	}

	public Integer getMCC() {
		return MCC;
	}

	public void setMCC(Integer mCC) {
		MCC = mCC;
	}

	public boolean isMNC() {
		return isMNC;
	}

	public void setMNC(boolean isMNC) {
		this.isMNC = isMNC;
	}

	public boolean isMCC() {
		return isMCC;
	}

	public void setMCC(boolean isMCC) {
		this.isMCC = isMCC;
	}

	public boolean isTrackingAraCode() {
		return isTrackingAraCode;
	}

	public void setTrackingAraCode(boolean isTrackingAraCode) {
		this.isTrackingAraCode = isTrackingAraCode;
	}

	/**
	 * @return the ueTxPower
	 */
	public Double getUeTxPower() {
		return ueTxPower;
	}

	/**
	 * @param ueTxPower
	 *            the ueTxPower to set
	 */
	public void setUeTxPower(Double ueTxPower) {
		this.ueTxPower = ueTxPower;
	}

	/**
	 * @return the preambleCountMax
	 */
	public Integer getPreambleCountMax() {
		return preambleCountMax;
	}

	/**
	 * @param preambleCountMax
	 *            the preambleCountMax to set
	 */
	public void setPreambleCountMax(Integer preambleCountMax) {
		this.preambleCountMax = preambleCountMax;
	}

	/**
	 * @return the preambleInitialTxPower
	 */
	public Integer getPreambleInitialTxPower() {
		return preambleInitialTxPower;
	}

	/**
	 * @param preambleInitialTxPower
	 *            the preambleInitialTxPower to set
	 */
	public void setPreambleInitialTxPower(Integer preambleInitialTxPower) {
		this.preambleInitialTxPower = preambleInitialTxPower;
	}

	/**
	 * @return the raRNTI
	 */
	public Integer getRaRNTI() {
		return raRNTI;
	}

	/**
	 * @param raRNTI
	 *            the raRNTI to set
	 */
	public void setRaRNTI(Integer raRNTI) {
		this.raRNTI = raRNTI;
	}

	/**
	 * @return the raTimingAdvance
	 */
	public Integer getRaTimingAdvance() {
		return raTimingAdvance;
	}

	/**
	 * @param raTimingAdvance
	 *            the raTimingAdvance to set
	 */
	public void setRaTimingAdvance(Integer raTimingAdvance) {
		this.raTimingAdvance = raTimingAdvance;
	}

	public String getDlCp() {
		return dlCp;
	}

	public void setDlCp(String dlCp) {
		this.dlCp = dlCp;
	}

	public String getUlCp() {
		return ulCp;
	}

	public void setUlCp(String ulCp) {
		this.ulCp = ulCp;
	}

	/**
	 * @return the avgULPRB
	 */
	public Double[] getAvgULPRB() {
		return avgULPRB;
	}

	/**
	 * @param avgULPRB
	 *            the avgULPRB to set
	 */
	public void setAvgULPRB(Double[] avgULPRB) {
		this.avgULPRB = avgULPRB;
	}

	public String getThreshholdCause() {
		return threshholdCause;
	}

	public void setThreshholdCause(String threshholdCause) {
		this.threshholdCause = threshholdCause;
	}

	public String getRsrpThresholdValue() {
		return rsrpThresholdValue;
	}

	public void setRsrpThresholdValue(String rsrpThresholdValue) {
		this.rsrpThresholdValue = rsrpThresholdValue;
	}

	public String getSinrThresholdValue() {
		return sinrThresholdValue;
	}

	public void setSinrThresholdValue(String sinrThresholdValue) {
		this.sinrThresholdValue = sinrThresholdValue;
	}

	public Integer getReselectionSuccess() {
		return reselectionSuccess;
	}

	public void setReselectionSuccess(Integer reselectionSuccess) {
		this.reselectionSuccess = reselectionSuccess;
	}

	public String getDlModulationType() {
		return dlModulationType;
	}

	public void setDlModulationType(String dlModulationType) {
		this.dlModulationType = dlModulationType;
	}

	public String getUlModulationType() {
		return ulModulationType;
	}

	public void setUlModulationType(String ulModulationType) {
		this.ulModulationType = ulModulationType;
	}

	public Double[] getB173DlPRB() {
		return b173DlPRB;
	}

	public void setB173DlPRB(Double[] b173DlPRB) {
		this.b173DlPRB = b173DlPRB;
	}

	public Integer getRrcConnectionReestablishmentRequest() {
		return rrcConnectionReestablishmentRequest;
	}

	public void setRrcConnectionReestablishmentRequest(Integer rrcConnectionReestablishmentRequest) {
		this.rrcConnectionReestablishmentRequest = rrcConnectionReestablishmentRequest;
	}

	public Integer getRrcConnectionReestablishmentReject() {
		return rrcConnectionReestablishmentReject;
	}

	public void setRrcConnectionReestablishmentReject(Integer rrcConnectionReestablishmentReject) {
		this.rrcConnectionReestablishmentReject = rrcConnectionReestablishmentReject;
	}

	public Integer getRrcConnectionReestablishmentComplete() {
		return rrcConnectionReestablishmentComplete;
	}

	public void setRrcConnectionReestablishmentComplete(Integer rrcConnectionReestablishmentComplete) {
		this.rrcConnectionReestablishmentComplete = rrcConnectionReestablishmentComplete;
	}

	public Double[] getJitterAggregated() {
		return jitterAggregated;
	}

	public void setJitterAggregated(Double[] jitterAggregated) {
		this.jitterAggregated = jitterAggregated;
	}

	public Double[] getPacketLossValueAndCount() {
		return packetLossValueAndCount;
	}

	public void setPacketLossValueAndCount(Double[] packetLossValueAndCount) {
		this.packetLossValueAndCount = packetLossValueAndCount;
	}

	public Double[] getDlPRBUtilization() {
		return dlPRBUtilization;
	}

	public void setDlPRBUtilization(Double[] dlPRBUtilization) {
		this.dlPRBUtilization = dlPRBUtilization;
	}

	public Double[] getUlPRBUtilization() {
		return ulPRBUtilization;
	}

	public void setUlPRBUtilization(Double[] ulPRBUtilization) {
		this.ulPRBUtilization = ulPRBUtilization;
	}
}

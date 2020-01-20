package com.inn.foresight.module.nv.layer3.qmdlParser.wrappers;

import java.util.List;

/** The Class QMDLLogCodeWrapper. */
public class QMDLLogCodeWrapper {

	/** The lat. */
	private Double lat;
	/** The lon. */
	private Double lon;

	private Long tempTimeStamp;
	private Long timeStamp;
	private Long etimeStamp;
	private Integer earfcn;
	private Integer pci;
	private String servingCellIndex;
	private Integer currentSFN;
	private Integer cellTiming0;
	private Integer cellTiming1;
	private Integer cellTimingSFN0;
	private Integer cellTimingSFN1;

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
	private Double[] numRB;
	private Double[] puschTxPower;

	/** The dl earfcn. */
	private Long dlEarfcn;

	/** The ul earfcn. */
	private Long ulEarfcn;
	private Integer tddConfig;
	private String dlCp;
	private String ulCp;
	private Integer specialSubframeConfiguration;
	private Integer b0c2Pci;
	/** The dl bandwidth. */
	private String dl_Bandwidth;
	private String technologyBand;

	/** The ul bandwidth. */
	private String ul_Bandwidth;
	private Integer tracking_Area_Code;
	private Integer mcc;
	private Integer mnc;
	private Integer timingAdvance;
	private Integer mcs;
	private String modulationType;
	private String numberTxAntennas;
	private String numberRxAntennas;
	private String spatialRank;
	private Long rBAllocationSlot0;
	private Long rBAllocationSlot1;
	private Integer indexPMI;
	private Double[] outOfSyncBler;
	private Double[] b173numRbs;
	/** The ul through put. */
	private Double[] ulThroughPut;

	private Double[] pingPacketLoss;

	/** The dl through put. */
	private Double[] dlThroughPut;
	/** The latency. */
	private Double[] latency;

	
	private Integer callInitiateCount;
	private Integer callDropCount;
	private Integer callFailureCount;
	private Integer callSuccessCount;
        private Integer callSetupSuccessCount;
        private Integer callSetupCount;
	
	private Integer cellChangeCount;
	private String address;
	private Integer rrcInitiate;
	private Integer rrcSucess;
	private Integer cellid;
	private String band;
	private Integer voLTECallDropCount;
	private Double[] xpoint;
	private Double[] ypoint;
	private Double[] avgJitter;
	private Double[] avgReponseTime;
	private Double[] wifiRssi;
	private Double[] wifiSnr;
	private String ssid;
	private String bssid;
	private Integer channel;
	private Integer linkSpeed;

	private String coverage;
	private String testType;
	private Double[] handoverLatencyTime;
	private Integer sourcePci;
	private Integer targetPci;

	private Double[] httpDl;
	private Double[] httpUl;
	private Double[] ftpDl;
	private Double[] ftpUl;
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
	private Double[] connectionSetupTime;
	private String callNeStats;
	private Integer totalErab;
	private Integer erabDrop;
	private Double[] callConnectionSetupTime;
	private Double[] packetLoss;
	private Double[] finalMosG711Onnet;
	private Double[] finalMosILbcOnnet;
	private Double[] finalMosG711Offnet;
	private Double[] finalMosILbcOffnet;
	private Double[] callConnectionSetupTimeOnnet;
	private Double[] callConnectionSetupTimeOffnet;
	private String callStatus;
	private Integer sectorId;
	private Integer eNodebId;
	private String caType;
	private Integer cgi;
	private Integer httpAttempt;
	private Integer httpSucess;
	private Integer httpFailure;
	private Integer httpDrop;

	private Double[] httpDownLoadTime;
	private List<NeighbourDetailWrapper> neighbourDataList;
	private List<NvSMSDetailWrapper> listNvSMSDetailWrapper;
	private String mosStatus;
	private Integer smsAttempt;
	private Integer smsSucess;
	private Integer smsFailure;

	/** Wcdma : 3g data */
	private Double[] rscp;
	private Double[] ecio;
	private Double[] rssi;
	private String technologyName;

	private Integer tauAttempt;
	private Integer tauComplete;
	private Integer tauFailure;
	private Double[] tauTime;
	private Double[] instantaneousMos;

	private Double[] pdschThroughput;
	private Double[] pdschThroughputPriCell;
	private Double[] pdschThroughputSecCell1;
	private Double[] pdschThroughputSecCell2;
	private Double[] pdschThroughputSecCell3;
	private Double[] pdschThroughputSecCell4;
	private Double[] pdschThroughputSecCell5;

	private Double[] puschThroughput;
	private Double[] puschMcsIndex;
	private String puschModulationType;
	private Double[] pCellPUSCHThroughput;
	private Double[] sCell1PUSCHThroughput;
	private Double[] sCell2PUSCHThroughput;
	private Double[] sCell3PUSCHThroughput;
	private Double[] sCell4PUSCHThroughput;
	private Double[] sCell5PUSCHThroughput;
	private Double[] sCell6PUSCHThroughput;
	private Double[] sCell7PUSCHThroughput;
	private Double ueTxPower;

	/** Csfb call data. */
	private Integer csfbCallAttempt;
	private Integer csfbCallSuccess;
	private Integer csfbCallDrop;
	private Integer csfbCallFailed;
	private Integer csfbCallSetupSuccess;
	private Integer csfbCallSetupFailed;

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

	/* Inbuilding new parameters */
	private Double speedTestDlRate;
	private Double speedTestUlRate;
	private Integer speedTestPinNumber;
	private Double downloadTimeGoogle;
	private Double downloadTimeFacebook;
	private Double downloadTimeYoutube;
	private String transmissionMode;

	/* 2g data */
	/** RxLev RxQual BCCH Channel BSIC. */
	private Double[] rxLev;
	private Double[] rxQual;
	private Integer bCCHChannel;
	private Integer bSIC;

	private Integer preambleCountMax;
	private Integer preambleInitialTxPower;
	private Integer raRNTI;
	private Integer raTimingAdvance;

	private Double[] dominantChannelRSSI;
	private Double[] dominantChannelRSSIRx0;
	private Double[] dominantChannelRSSIRx1;
	private Double maxTTLPDSCHThroughput;
	private Double maxTTLPUSCHThroughput;
	private Integer dominantChannelPCI;
	private Double[] dominantChannelRSRQ;
	private Double[] dominantChannelRSRQRx0;
	private Double[] dominantChannelRSRQRx1;
	private Double[] dominantChannelRSRP;
	private Double[] dominantChannelRSRPRx0;
	private Double[] dominantChannelRSRPRx1;

	private Integer tb0McsIndex;
	private Integer tb1McsIndex;
	private String tb0ModulationType;
	private String tb1ModulationType;

	private Double avgDLTb0Size;
	private Double avgDLTb1Size;
	private Double avgDLTbSize;
	private Double avgULTBSize;

	private Double[] macUlThroughput;
	private Double prachTxPower;
	private Integer powerHeadroomdata;
	private Integer ulPUSCHTxPower;
	private Long lteDominantFrequency;
	private Double[] pdschBLER;
	private Double[] handoverInterruption;
	private Double[] handoverInterruptionTimeOfQCI1;
	private Double[] handoverInterruptionTimeOfQCI9DL;
	private Double[] handoverInterruptionTimeOfQCI9UL;

	private Integer attachRequest;
	private Integer attachAccept;
	private Integer b0C0Count;
	private Integer message3Count;
	private Integer message1Count;

	private String emmState;
	private String rrcState;
	private String emmSubState;

	private Double[] avgULPRB;
	private String geoHash;

	private Integer voiceBearerActivationRequest;
	private Integer voiceBearerActivationSuccess;
	private Integer voiceBearerActivationFailure;
	private Integer voiceBearerDeactivationRequest;

	private Integer initialIMSRegistrationSuccess;
	private Integer initialIMSRegistrationFailure;

	private Integer attachComplete;
	private Integer detachRequest;
	private Integer lteEMMRegisteredEvent;
	private Integer ltePDNConnectionRequest;

	private Integer lteRACHPreambleSuccess;
	private Integer lteRACHProcedureSuccess;
	private Integer lteRACHPreambleFailure;
	private Integer lteRACHProcedureFailure;

	private Integer radioLinkFailure;
	private Integer pagingMessageCount;
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

	private Integer erabSetup;
	private Integer erabSetupSuccess;

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

	private Integer reselectionSuccess;
	private String pciChangeStatus;

	private Double[] latencyBufferSize32Bytes;
	private Double[] latencyBufferSize1000Bytes;
	private Double[] latencyBufferSize1500Bytes;
	private Integer pingBufferSize;

	private String dlModulationType;
	private String ulModulationType;

	private Double[] b173DlPRB;
	private Double[] imsRegistrationSetupTime;

	private Integer rrcConnectionReestablishmentRequest;
	private Integer rrcConnectionReestablishmentReject;
	private Integer rrcConnectionReestablishmentComplete;

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

	private Integer sourceEarfcn;
	private Integer targetEarfcn;
	private Double[] macDlThroughput;

	private Double[] jitter;
	private Double[] packetLossValueAndCount;
	private Double[] dlPRBUtilization;
	private Double[] ulPRBUtilization;

	private Double[] pMos;
	
	private Double[] pdcpThroughput;
	
	private Boolean isCallSetup;
	
	private Double numberOfRtpPacketsLost;
	private Double totalPacketCount;
	private String host;
	
	private Double[] rlcThroughput;
	
	
	private Double[] callSetupSuccessTime;
	
	public Double[] getCallSetupSuccessTime() {
		return callSetupSuccessTime;
	}

	public void setCallSetupSuccessTime(Double[] callSetupSuccessTime) {
		this.callSetupSuccessTime = callSetupSuccessTime;
	}

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

	public Double[] getRlcThroughput() {
		return rlcThroughput;
	}

	public void setRlcThroughput(Double[] rlcThroughput) {
		this.rlcThroughput = rlcThroughput;
	}

	public String getHost() {
		return host;
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public Double getTotalPacketCount() {
		return totalPacketCount;
	}

	public void setTotalPacketCount(Double totalPacketCount) {
		this.totalPacketCount = totalPacketCount;
	}
	
    public Double getNumberOfRtpPacketsLost() {
        return numberOfRtpPacketsLost;
    }

    public void setNumberOfRtpPacketsLost(Double numberOfRtpPacketsLost) {
        this.numberOfRtpPacketsLost = numberOfRtpPacketsLost;
    }
	
	public Boolean getIsCallSetup() {
		return isCallSetup;
	}

	public void setIsCallSetup(Boolean isCallSetup) {
		this.isCallSetup = isCallSetup;
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
	 * @param volteMTCallFailure the volteMTCallFailure to set
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
	 * @param volteMTCallDrop the volteMTCallDrop to set
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
	 * @param volteMOCallFailure the volteMOCallFailure to set
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
	 * @param volteMOCallDrop the volteMOCallDrop to set
	 */
	public void setVolteMOCallDrop(Integer volteMOCallDrop) {
		this.volteMOCallDrop = volteMOCallDrop;
	}

	

	/**
	 * @return the lat
	 */
	public Double getLat() {
		return lat;
	}

	/**
	 * @param lat the lat to set
	 */
	public void setLat(Double lat) {
		this.lat = lat;
	}

	/**
	 * @return the lon
	 */
	public Double getLon() {
		return lon;
	}

	/**
	 * @param lon the lon to set
	 */
	public void setLon(Double lon) {
		this.lon = lon;
	}

	/**
	 * @return the tempTimeStamp
	 */
	public Long getTempTimeStamp() {
		return tempTimeStamp;
	}

	/**
	 * @param tempTimeStamp the tempTimeStamp to set
	 */
	public void setTempTimeStamp(Long tempTimeStamp) {
		this.tempTimeStamp = tempTimeStamp;
	}

	/**
	 * @return the timeStamp
	 */
	public Long getTimeStamp() {
		return timeStamp;
	}

	/**
	 * @param timeStamp the timeStamp to set
	 */
	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}

	/**
	 * @return the etimeStamp
	 */
	public Long getEtimeStamp() {
		return etimeStamp;
	}

	/**
	 * @param etimeStamp the etimeStamp to set
	 */
	public void setEtimeStamp(Long etimeStamp) {
		this.etimeStamp = etimeStamp;
	}

	/**
	 * @return the earfcn
	 */
	public Integer getEarfcn() {
		return earfcn;
	}

	/**
	 * @param earfcn the earfcn to set
	 */
	public void setEarfcn(Integer earfcn) {
		this.earfcn = earfcn;
	}

	/**
	 * @return the pci
	 */
	public Integer getPci() {
		return pci;
	}

	/**
	 * @param pci the pci to set
	 */
	public void setPci(Integer pci) {
		this.pci = pci;
	}

	/**
	 * @return the servingCellIndex
	 */
	public String getServingCellIndex() {
		return servingCellIndex;
	}

	/**
	 * @param servingCellIndex the servingCellIndex to set
	 */
	public void setServingCellIndex(String servingCellIndex) {
		this.servingCellIndex = servingCellIndex;
	}

	/**
	 * @return the currentSFN
	 */
	public Integer getCurrentSFN() {
		return currentSFN;
	}

	/**
	 * @param currentSFN the currentSFN to set
	 */
	public void setCurrentSFN(Integer currentSFN) {
		this.currentSFN = currentSFN;
	}

	/**
	 * @return the cellTiming0
	 */
	public Integer getCellTiming0() {
		return cellTiming0;
	}

	/**
	 * @param cellTiming0 the cellTiming0 to set
	 */
	public void setCellTiming0(Integer cellTiming0) {
		this.cellTiming0 = cellTiming0;
	}

	/**
	 * @return the cellTiming1
	 */
	public Integer getCellTiming1() {
		return cellTiming1;
	}

	/**
	 * @param cellTiming1 the cellTiming1 to set
	 */
	public void setCellTiming1(Integer cellTiming1) {
		this.cellTiming1 = cellTiming1;
	}

	/**
	 * @return the cellTimingSFN0
	 */
	public Integer getCellTimingSFN0() {
		return cellTimingSFN0;
	}

	/**
	 * @param cellTimingSFN0 the cellTimingSFN0 to set
	 */
	public void setCellTimingSFN0(Integer cellTimingSFN0) {
		this.cellTimingSFN0 = cellTimingSFN0;
	}

	/**
	 * @return the cellTimingSFN1
	 */
	public Integer getCellTimingSFN1() {
		return cellTimingSFN1;
	}

	/**
	 * @param cellTimingSFN1 the cellTimingSFN1 to set
	 */
	public void setCellTimingSFN1(Integer cellTimingSFN1) {
		this.cellTimingSFN1 = cellTimingSFN1;
	}

	/**
	 * @return the rSRPRx0Data
	 */
	public Double[] getrSRPRx0Data() {
		return rSRPRx0Data;
	}

	/**
	 * @param rSRPRx0Data the rSRPRx0Data to set
	 */
	public void setrSRPRx0Data(Double[] rSRPRx0Data) {
		this.rSRPRx0Data = rSRPRx0Data;
	}

	/**
	 * @return the rSRPRx1Data
	 */
	public Double[] getrSRPRx1Data() {
		return rSRPRx1Data;
	}

	/**
	 * @param rSRPRx1Data the rSRPRx1Data to set
	 */
	public void setrSRPRx1Data(Double[] rSRPRx1Data) {
		this.rSRPRx1Data = rSRPRx1Data;
	}

	/**
	 * @return the measureRSRPData
	 */
	public Double[] getMeasureRSRPData() {
		return measureRSRPData;
	}

	/**
	 * @param measureRSRPData the measureRSRPData to set
	 */
	public void setMeasureRSRPData(Double[] measureRSRPData) {
		this.measureRSRPData = measureRSRPData;
	}

	/**
	 * @return the rSRQRx0Data
	 */
	public Double[] getrSRQRx0Data() {
		return rSRQRx0Data;
	}

	/**
	 * @param rSRQRx0Data the rSRQRx0Data to set
	 */
	public void setrSRQRx0Data(Double[] rSRQRx0Data) {
		this.rSRQRx0Data = rSRQRx0Data;
	}

	/**
	 * @return the rSRQRx1Data
	 */
	public Double[] getrSRQRx1Data() {
		return rSRQRx1Data;
	}

	/**
	 * @param rSRQRx1Data the rSRQRx1Data to set
	 */
	public void setrSRQRx1Data(Double[] rSRQRx1Data) {
		this.rSRQRx1Data = rSRQRx1Data;
	}

	/**
	 * @return the measureRSRQData
	 */
	public Double[] getMeasureRSRQData() {
		return measureRSRQData;
	}

	/**
	 * @param measureRSRQData the measureRSRQData to set
	 */
	public void setMeasureRSRQData(Double[] measureRSRQData) {
		this.measureRSRQData = measureRSRQData;
	}

	/**
	 * @return the rSSIRx0Data
	 */
	public Double[] getrSSIRx0Data() {
		return rSSIRx0Data;
	}

	/**
	 * @param rSSIRx0Data the rSSIRx0Data to set
	 */
	public void setrSSIRx0Data(Double[] rSSIRx0Data) {
		this.rSSIRx0Data = rSSIRx0Data;
	}

	/**
	 * @return the rSSIRx1Data
	 */
	public Double[] getrSSIRx1Data() {
		return rSSIRx1Data;
	}

	/**
	 * @param rSSIRx1Data the rSSIRx1Data to set
	 */
	public void setrSSIRx1Data(Double[] rSSIRx1Data) {
		this.rSSIRx1Data = rSSIRx1Data;
	}

	/**
	 * @return the rSSIData
	 */
	public Double[] getrSSIData() {
		return rSSIData;
	}

	/**
	 * @param rSSIData the rSSIData to set
	 */
	public void setrSSIData(Double[] rSSIData) {
		this.rSSIData = rSSIData;
	}

	/**
	 * @return the sINRData
	 */
	public Double[] getsINRData() {
		return sINRData;
	}

	/**
	 * @param sINRData the sINRData to set
	 */
	public void setsINRData(Double[] sINRData) {
		this.sINRData = sINRData;
	}

	/**
	 * @return the sINRRx0Data
	 */
	public Double[] getsINRRx0Data() {
		return sINRRx0Data;
	}

	/**
	 * @param sINRRx0Data the sINRRx0Data to set
	 */
	public void setsINRRx0Data(Double[] sINRRx0Data) {
		this.sINRRx0Data = sINRRx0Data;
	}

	/**
	 * @return the sINRRx1Data
	 */
	public Double[] getsINRRx1Data() {
		return sINRRx1Data;
	}

	/**
	 * @param sINRRx1Data the sINRRx1Data to set
	 */
	public void setsINRRx1Data(Double[] sINRRx1Data) {
		this.sINRRx1Data = sINRRx1Data;
	}

	/**
	 * @return the rankIndex
	 */
	public String getRankIndex() {
		return rankIndex;
	}

	/**
	 * @param rankIndex the rankIndex to set
	 */
	public void setRankIndex(String rankIndex) {
		this.rankIndex = rankIndex;
	}

	/**
	 * @return the cqiCwo
	 */
	public Double[] getCqiCwo() {
		return cqiCwo;
	}

	/**
	 * @param cqiCwo the cqiCwo to set
	 */
	public void setCqiCwo(Double[] cqiCwo) {
		this.cqiCwo = cqiCwo;
	}

	/**
	 * @return the carrierIndex
	 */
	public String getCarrierIndex() {
		return carrierIndex;
	}

	/**
	 * @param carrierIndex the carrierIndex to set
	 */
	public void setCarrierIndex(String carrierIndex) {
		this.carrierIndex = carrierIndex;
	}

	/**
	 * @return the numRB
	 */
	public Double[] getNumRB() {
		return numRB;
	}

	/**
	 * @param numRB the numRB to set
	 */
	public void setNumRB(Double[] numRB) {
		this.numRB = numRB;
	}

	/**
	 * @return the puschTxPower
	 */
	public Double[] getPuschTxPower() {
		return puschTxPower;
	}

	/**
	 * @param puschTxPower the puschTxPower to set
	 */
	public void setPuschTxPower(Double[] puschTxPower) {
		this.puschTxPower = puschTxPower;
	}

	/**
	 * @return the dlEarfcn
	 */
	public Long getDlEarfcn() {
		return dlEarfcn;
	}

	/**
	 * @param dlEarfcn the dlEarfcn to set
	 */
	public void setDlEarfcn(Long dlEarfcn) {
		this.dlEarfcn = dlEarfcn;
	}

	/**
	 * @return the ulEarfcn
	 */
	public Long getUlEarfcn() {
		return ulEarfcn;
	}

	/**
	 * @param ulEarfcn the ulEarfcn to set
	 */
	public void setUlEarfcn(Long ulEarfcn) {
		this.ulEarfcn = ulEarfcn;
	}

	/**
	 * @return the tddConfig
	 */
	public Integer getTddConfig() {
		return tddConfig;
	}

	/**
	 * @param tddConfig the tddConfig to set
	 */
	public void setTddConfig(Integer tddConfig) {
		this.tddConfig = tddConfig;
	}

	/**
	 * @return the dlCp
	 */
	public String getDlCp() {
		return dlCp;
	}

	/**
	 * @param dlCp the dlCp to set
	 */
	public void setDlCp(String dlCp) {
		this.dlCp = dlCp;
	}

	/**
	 * @return the ulCp
	 */
	public String getUlCp() {
		return ulCp;
	}

	/**
	 * @param ulCp the ulCp to set
	 */
	public void setUlCp(String ulCp) {
		this.ulCp = ulCp;
	}

	/**
	 * @return the specialSubframeConfiguration
	 */
	public Integer getSpecialSubframeConfiguration() {
		return specialSubframeConfiguration;
	}

	/**
	 * @param specialSubframeConfiguration the specialSubframeConfiguration to set
	 */
	public void setSpecialSubframeConfiguration(Integer specialSubframeConfiguration) {
		this.specialSubframeConfiguration = specialSubframeConfiguration;
	}

	/**
	 * @return the b0c2Pci
	 */
	public Integer getB0c2Pci() {
		return b0c2Pci;
	}

	/**
	 * @param b0c2Pci the b0c2Pci to set
	 */
	public void setB0c2Pci(Integer b0c2Pci) {
		this.b0c2Pci = b0c2Pci;
	}

	/**
	 * @return the dl_Bandwidth
	 */
	public String getDl_Bandwidth() {
		return dl_Bandwidth;
	}

	/**
	 * @param dl_Bandwidth the dl_Bandwidth to set
	 */
	public void setDl_Bandwidth(String dl_Bandwidth) {
		this.dl_Bandwidth = dl_Bandwidth;
	}

	/**
	 * @return the technologyBand
	 */
	public String getTechnologyBand() {
		return technologyBand;
	}

	/**
	 * @param technologyBand the technologyBand to set
	 */
	public void setTechnologyBand(String technologyBand) {
		this.technologyBand = technologyBand;
	}

	/**
	 * @return the ul_Bandwidth
	 */
	public String getUl_Bandwidth() {
		return ul_Bandwidth;
	}

	/**
	 * @param ul_Bandwidth the ul_Bandwidth to set
	 */
	public void setUl_Bandwidth(String ul_Bandwidth) {
		this.ul_Bandwidth = ul_Bandwidth;
	}

	/**
	 * @return the tracking_Area_Code
	 */
	public Integer getTracking_Area_Code() {
		return tracking_Area_Code;
	}

	/**
	 * @param tracking_Area_Code the tracking_Area_Code to set
	 */
	public void setTracking_Area_Code(Integer tracking_Area_Code) {
		this.tracking_Area_Code = tracking_Area_Code;
	}

	/**
	 * @return the mcc
	 */
	public Integer getMcc() {
		return mcc;
	}

	/**
	 * @param mcc the mcc to set
	 */
	public void setMcc(Integer mcc) {
		this.mcc = mcc;
	}

	/**
	 * @return the mnc
	 */
	public Integer getMnc() {
		return mnc;
	}

	/**
	 * @param mnc the mnc to set
	 */
	public void setMnc(Integer mnc) {
		this.mnc = mnc;
	}

	/**
	 * @return the timingAdvance
	 */
	public Integer getTimingAdvance() {
		return timingAdvance;
	}

	/**
	 * @param timingAdvance the timingAdvance to set
	 */
	public void setTimingAdvance(Integer timingAdvance) {
		this.timingAdvance = timingAdvance;
	}

	/**
	 * @return the mcs
	 */
	public Integer getMcs() {
		return mcs;
	}

	/**
	 * @param mcs the mcs to set
	 */
	public void setMcs(Integer mcs) {
		this.mcs = mcs;
	}

	/**
	 * @return the modulationType
	 */
	public String getModulationType() {
		return modulationType;
	}

	/**
	 * @param modulationType the modulationType to set
	 */
	public void setModulationType(String modulationType) {
		this.modulationType = modulationType;
	}

	/**
	 * @return the numberTxAntennas
	 */
	public String getNumberTxAntennas() {
		return numberTxAntennas;
	}

	/**
	 * @param numberTxAntennas the numberTxAntennas to set
	 */
	public void setNumberTxAntennas(String numberTxAntennas) {
		this.numberTxAntennas = numberTxAntennas;
	}

	/**
	 * @return the numberRxAntennas
	 */
	public String getNumberRxAntennas() {
		return numberRxAntennas;
	}

	/**
	 * @param numberRxAntennas the numberRxAntennas to set
	 */
	public void setNumberRxAntennas(String numberRxAntennas) {
		this.numberRxAntennas = numberRxAntennas;
	}

	/**
	 * @return the spatialRank
	 */
	public String getSpatialRank() {
		return spatialRank;
	}

	/**
	 * @param spatialRank the spatialRank to set
	 */
	public void setSpatialRank(String spatialRank) {
		this.spatialRank = spatialRank;
	}

	/**
	 * @return the rBAllocationSlot0
	 */
	public Long getrBAllocationSlot0() {
		return rBAllocationSlot0;
	}

	/**
	 * @param rBAllocationSlot0 the rBAllocationSlot0 to set
	 */
	public void setrBAllocationSlot0(Long rBAllocationSlot0) {
		this.rBAllocationSlot0 = rBAllocationSlot0;
	}

	/**
	 * @return the rBAllocationSlot1
	 */
	public Long getrBAllocationSlot1() {
		return rBAllocationSlot1;
	}

	/**
	 * @param rBAllocationSlot1 the rBAllocationSlot1 to set
	 */
	public void setrBAllocationSlot1(Long rBAllocationSlot1) {
		this.rBAllocationSlot1 = rBAllocationSlot1;
	}

	/**
	 * @return the indexPMI
	 */
	public Integer getIndexPMI() {
		return indexPMI;
	}

	/**
	 * @param indexPMI the indexPMI to set
	 */
	public void setIndexPMI(Integer indexPMI) {
		this.indexPMI = indexPMI;
	}

	/**
	 * @return the outOfSyncBler
	 */
	public Double[] getOutOfSyncBler() {
		return outOfSyncBler;
	}

	/**
	 * @param outOfSyncBler the outOfSyncBler to set
	 */
	public void setOutOfSyncBler(Double[] outOfSyncBler) {
		this.outOfSyncBler = outOfSyncBler;
	}

	/**
	 * @return the b173numRbs
	 */
	public Double[] getB173numRbs() {
		return b173numRbs;
	}

	/**
	 * @param b173numRbs the b173numRbs to set
	 */
	public void setB173numRbs(Double[] b173numRbs) {
		this.b173numRbs = b173numRbs;
	}

	/**
	 * @return the ulThroughPut
	 */
	public Double[] getUlThroughPut() {
		return ulThroughPut;
	}

	/**
	 * @param ulThroughPut the ulThroughPut to set
	 */
	public void setUlThroughPut(Double[] ulThroughPut) {
		this.ulThroughPut = ulThroughPut;
	}

	/**
	 * @return the pingPacketLoss
	 */
	public Double[] getPingPacketLoss() {
		return pingPacketLoss;
	}

	/**
	 * @param pingPacketLoss the pingPacketLoss to set
	 */
	public void setPingPacketLoss(Double[] pingPacketLoss) {
		this.pingPacketLoss = pingPacketLoss;
	}

	/**
	 * @return the dlThroughPut
	 */
	public Double[] getDlThroughPut() {
		return dlThroughPut;
	}

	/**
	 * @param dlThroughPut the dlThroughPut to set
	 */
	public void setDlThroughPut(Double[] dlThroughPut) {
		this.dlThroughPut = dlThroughPut;
	}

	/**
	 * @return the latency
	 */
	public Double[] getLatency() {
		return latency;
	}

	/**
	 * @param latency the latency to set
	 */
	public void setLatency(Double[] latency) {
		this.latency = latency;
	}

	/**
	 * @return the callInitiateCount
	 */
	public Integer getCallInitiateCount() {
		return callInitiateCount;
	}

	/**
	 * @param callInitiateCount the callInitiateCount to set
	 */
	public void setCallInitiateCount(Integer callInitiateCount) {
		this.callInitiateCount = callInitiateCount;
	}

	/**
	 * @return the callDropCount
	 */
	public Integer getCallDropCount() {
		return callDropCount;
	}

	/**
	 * @param callDropCount the callDropCount to set
	 */
	public void setCallDropCount(Integer callDropCount) {
		this.callDropCount = callDropCount;
	}

	/**
	 * @return the callFailureCount
	 */
	public Integer getCallFailureCount() {
		return callFailureCount;
	}

	/**
	 * @param callFailureCount the callFailureCount to set
	 */
	public void setCallFailureCount(Integer callFailureCount) {
		this.callFailureCount = callFailureCount;
	}

	/**
	 * @return the callSuccessCount
	 */
	public Integer getCallSuccessCount() {
		return callSuccessCount;
	}

	/**
	 * @param callSuccessCount the callSuccessCount to set
	 */
	public void setCallSuccessCount(Integer callSuccessCount) {
		this.callSuccessCount = callSuccessCount;
	}

	/**
	 * @return the cellChangeCount
	 */
	public Integer getCellChangeCount() {
		return cellChangeCount;
	}

	/**
	 * @param cellChangeCount the cellChangeCount to set
	 */
	public void setCellChangeCount(Integer cellChangeCount) {
		this.cellChangeCount = cellChangeCount;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the rrcInitiate
	 */
	public Integer getRrcInitiate() {
		return rrcInitiate;
	}

	/**
	 * @param rrcInitiate the rrcInitiate to set
	 */
	public void setRrcInitiate(Integer rrcInitiate) {
		this.rrcInitiate = rrcInitiate;
	}

	/**
	 * @return the rrcSucess
	 */
	public Integer getRrcSucess() {
		return rrcSucess;
	}

	/**
	 * @param rrcSucess the rrcSucess to set
	 */
	public void setRrcSucess(Integer rrcSucess) {
		this.rrcSucess = rrcSucess;
	}

	/**
	 * @return the cellid
	 */
	public Integer getCellid() {
		return cellid;
	}

	/**
	 * @param cellid the cellid to set
	 */
	public void setCellid(Integer cellid) {
		this.cellid = cellid;
	}

	/**
	 * @return the band
	 */
	public String getBand() {
		return band;
	}

	/**
	 * @param band the band to set
	 */
	public void setBand(String band) {
		this.band = band;
	}

	/**
	 * @return the voLTECallDropCount
	 */
	public Integer getVoLTECallDropCount() {
		return voLTECallDropCount;
	}

	/**
	 * @param voLTECallDropCount the voLTECallDropCount to set
	 */
	public void setVoLTECallDropCount(Integer voLTECallDropCount) {
		this.voLTECallDropCount = voLTECallDropCount;
	}

	/**
	 * @return the xpoint
	 */
	public Double[] getXpoint() {
		return xpoint;
	}

	/**
	 * @param xpoint the xpoint to set
	 */
	public void setXpoint(Double[] xpoint) {
		this.xpoint = xpoint;
	}

	/**
	 * @return the ypoint
	 */
	public Double[] getYpoint() {
		return ypoint;
	}

	/**
	 * @param ypoint the ypoint to set
	 */
	public void setYpoint(Double[] ypoint) {
		this.ypoint = ypoint;
	}

	/**
	 * @return the avgJitter
	 */
	public Double[] getAvgJitter() {
		return avgJitter;
	}

	/**
	 * @param avgJitter the avgJitter to set
	 */
	public void setAvgJitter(Double[] avgJitter) {
		this.avgJitter = avgJitter;
	}

	/**
	 * @return the avgReponseTime
	 */
	public Double[] getAvgReponseTime() {
		return avgReponseTime;
	}

	/**
	 * @param avgReponseTime the avgReponseTime to set
	 */
	public void setAvgReponseTime(Double[] avgReponseTime) {
		this.avgReponseTime = avgReponseTime;
	}

	/**
	 * @return the wifiRssi
	 */
	public Double[] getWifiRssi() {
		return wifiRssi;
	}

	/**
	 * @param wifiRssi the wifiRssi to set
	 */
	public void setWifiRssi(Double[] wifiRssi) {
		this.wifiRssi = wifiRssi;
	}

	/**
	 * @return the wifiSnr
	 */
	public Double[] getWifiSnr() {
		return wifiSnr;
	}

	/**
	 * @param wifiSnr the wifiSnr to set
	 */
	public void setWifiSnr(Double[] wifiSnr) {
		this.wifiSnr = wifiSnr;
	}

	/**
	 * @return the ssid
	 */
	public String getSsid() {
		return ssid;
	}

	/**
	 * @param ssid the ssid to set
	 */
	public void setSsid(String ssid) {
		this.ssid = ssid;
	}

	/**
	 * @return the bssid
	 */
	public String getBssid() {
		return bssid;
	}

	/**
	 * @param bssid the bssid to set
	 */
	public void setBssid(String bssid) {
		this.bssid = bssid;
	}

	/**
	 * @return the channel
	 */
	public Integer getChannel() {
		return channel;
	}

	/**
	 * @param channel the channel to set
	 */
	public void setChannel(Integer channel) {
		this.channel = channel;
	}

	/**
	 * @return the linkSpeed
	 */
	public Integer getLinkSpeed() {
		return linkSpeed;
	}

	/**
	 * @param linkSpeed the linkSpeed to set
	 */
	public void setLinkSpeed(Integer linkSpeed) {
		this.linkSpeed = linkSpeed;
	}

	/**
	 * @return the coverage
	 */
	public String getCoverage() {
		return coverage;
	}

	/**
	 * @param coverage the coverage to set
	 */
	public void setCoverage(String coverage) {
		this.coverage = coverage;
	}

	/**
	 * @return the testType
	 */
	public String getTestType() {
		return testType;
	}

	/**
	 * @param testType the testType to set
	 */
	public void setTestType(String testType) {
		this.testType = testType;
	}

	/**
	 * @return the handoverLatencyTime
	 */
	public Double[] getHandoverLatencyTime() {
		return handoverLatencyTime;
	}

	/**
	 * @param handoverLatencyTime the handoverLatencyTime to set
	 */
	public void setHandoverLatencyTime(Double[] handoverLatencyTime) {
		this.handoverLatencyTime = handoverLatencyTime;
	}

	/**
	 * @return the sourcePci
	 */
	public Integer getSourcePci() {
		return sourcePci;
	}

	/**
	 * @param sourcePci the sourcePci to set
	 */
	public void setSourcePci(Integer sourcePci) {
		this.sourcePci = sourcePci;
	}

	/**
	 * @return the targetPci
	 */
	public Integer getTargetPci() {
		return targetPci;
	}

	/**
	 * @param targetPci the targetPci to set
	 */
	public void setTargetPci(Integer targetPci) {
		this.targetPci = targetPci;
	}

	/**
	 * @return the httpDl
	 */
	public Double[] getHttpDl() {
		return httpDl;
	}

	/**
	 * @param httpDl the httpDl to set
	 */
	public void setHttpDl(Double[] httpDl) {
		this.httpDl = httpDl;
	}

	/**
	 * @return the httpUl
	 */
	public Double[] getHttpUl() {
		return httpUl;
	}

	/**
	 * @param httpUl the httpUl to set
	 */
	public void setHttpUl(Double[] httpUl) {
		this.httpUl = httpUl;
	}

	/**
	 * @return the ftpDl
	 */
	public Double[] getFtpDl() {
		return ftpDl;
	}

	/**
	 * @param ftpDl the ftpDl to set
	 */
	public void setFtpDl(Double[] ftpDl) {
		this.ftpDl = ftpDl;
	}

	/**
	 * @return the ftpUl
	 */
	public Double[] getFtpUl() {
		return ftpUl;
	}

	/**
	 * @param ftpUl the ftpUl to set
	 */
	public void setFtpUl(Double[] ftpUl) {
		this.ftpUl = ftpUl;
	}

	/**
	 * @return the callInitiateOnNetCount
	 */
	public Integer getCallInitiateOnNetCount() {
		return callInitiateOnNetCount;
	}

	/**
	 * @param callInitiateOnNetCount the callInitiateOnNetCount to set
	 */
	public void setCallInitiateOnNetCount(Integer callInitiateOnNetCount) {
		this.callInitiateOnNetCount = callInitiateOnNetCount;
	}

	/**
	 * @return the callDropOnNetCount
	 */
	public Integer getCallDropOnNetCount() {
		return callDropOnNetCount;
	}

	/**
	 * @param callDropOnNetCount the callDropOnNetCount to set
	 */
	public void setCallDropOnNetCount(Integer callDropOnNetCount) {
		this.callDropOnNetCount = callDropOnNetCount;
	}

	/**
	 * @return the callFailureOnNetCount
	 */
	public Integer getCallFailureOnNetCount() {
		return callFailureOnNetCount;
	}

	/**
	 * @param callFailureOnNetCount the callFailureOnNetCount to set
	 */
	public void setCallFailureOnNetCount(Integer callFailureOnNetCount) {
		this.callFailureOnNetCount = callFailureOnNetCount;
	}

	/**
	 * @return the callSuccessOnNetCount
	 */
	public Integer getCallSuccessOnNetCount() {
		return callSuccessOnNetCount;
	}

	/**
	 * @param callSuccessOnNetCount the callSuccessOnNetCount to set
	 */
	public void setCallSuccessOnNetCount(Integer callSuccessOnNetCount) {
		this.callSuccessOnNetCount = callSuccessOnNetCount;
	}

	/**
	 * @return the callInitiateOffNetCount
	 */
	public Integer getCallInitiateOffNetCount() {
		return callInitiateOffNetCount;
	}

	/**
	 * @param callInitiateOffNetCount the callInitiateOffNetCount to set
	 */
	public void setCallInitiateOffNetCount(Integer callInitiateOffNetCount) {
		this.callInitiateOffNetCount = callInitiateOffNetCount;
	}

	/**
	 * @return the callDropOffNetCount
	 */
	public Integer getCallDropOffNetCount() {
		return callDropOffNetCount;
	}

	/**
	 * @param callDropOffNetCount the callDropOffNetCount to set
	 */
	public void setCallDropOffNetCount(Integer callDropOffNetCount) {
		this.callDropOffNetCount = callDropOffNetCount;
	}

	/**
	 * @return the callFailureOffNetCount
	 */
	public Integer getCallFailureOffNetCount() {
		return callFailureOffNetCount;
	}

	/**
	 * @param callFailureOffNetCount the callFailureOffNetCount to set
	 */
	public void setCallFailureOffNetCount(Integer callFailureOffNetCount) {
		this.callFailureOffNetCount = callFailureOffNetCount;
	}

	/**
	 * @return the callSuccessOffNetCount
	 */
	public Integer getCallSuccessOffNetCount() {
		return callSuccessOffNetCount;
	}

	/**
	 * @param callSuccessOffNetCount the callSuccessOffNetCount to set
	 */
	public void setCallSuccessOffNetCount(Integer callSuccessOffNetCount) {
		this.callSuccessOffNetCount = callSuccessOffNetCount;
	}

	/**
	 * @return the finalMosG711
	 */
	public Double[] getFinalMosG711() {
		return finalMosG711;
	}

	/**
	 * @param finalMosG711 the finalMosG711 to set
	 */
	public void setFinalMosG711(Double[] finalMosG711) {
		this.finalMosG711 = finalMosG711;
	}

	/**
	 * @return the finalMosILbc
	 */
	public Double[] getFinalMosILbc() {
		return finalMosILbc;
	}

	/**
	 * @param finalMosILbc the finalMosILbc to set
	 */
	public void setFinalMosILbc(Double[] finalMosILbc) {
		this.finalMosILbc = finalMosILbc;
	}

	/**
	 * @return the connectionSetupTime
	 */
	public Double[] getConnectionSetupTime() {
		return connectionSetupTime;
	}

	/**
	 * @param connectionSetupTime the connectionSetupTime to set
	 */
	public void setConnectionSetupTime(Double[] connectionSetupTime) {
		this.connectionSetupTime = connectionSetupTime;
	}

	/**
	 * @return the callNeStats
	 */
	public String getCallNeStats() {
		return callNeStats;
	}

	/**
	 * @param callNeStats the callNeStats to set
	 */
	public void setCallNeStats(String callNeStats) {
		this.callNeStats = callNeStats;
	}

	/**
	 * @return the totalErab
	 */
	public Integer getTotalErab() {
		return totalErab;
	}

	/**
	 * @param totalErab the totalErab to set
	 */
	public void setTotalErab(Integer totalErab) {
		this.totalErab = totalErab;
	}

	/**
	 * @return the erabDrop
	 */
	public Integer getErabDrop() {
		return erabDrop;
	}

	/**
	 * @param erabDrop the erabDrop to set
	 */
	public void setErabDrop(Integer erabDrop) {
		this.erabDrop = erabDrop;
	}

	/**
	 * @return the callConnectionSetupTime
	 */
	public Double[] getCallConnectionSetupTime() {
		return callConnectionSetupTime;
	}

	/**
	 * @param callConnectionSetupTime the callConnectionSetupTime to set
	 */
	public void setCallConnectionSetupTime(Double[] callConnectionSetupTime) {
		this.callConnectionSetupTime = callConnectionSetupTime;
	}

	/**
	 * @return the packetLoss
	 */
	public Double[] getPacketLoss() {
		return packetLoss;
	}

	/**
	 * @param packetLoss the packetLoss to set
	 */
	public void setPacketLoss(Double[] packetLoss) {
		this.packetLoss = packetLoss;
	}

	/**
	 * @return the finalMosG711Onnet
	 */
	public Double[] getFinalMosG711Onnet() {
		return finalMosG711Onnet;
	}

	/**
	 * @param finalMosG711Onnet the finalMosG711Onnet to set
	 */
	public void setFinalMosG711Onnet(Double[] finalMosG711Onnet) {
		this.finalMosG711Onnet = finalMosG711Onnet;
	}

	/**
	 * @return the finalMosILbcOnnet
	 */
	public Double[] getFinalMosILbcOnnet() {
		return finalMosILbcOnnet;
	}

	/**
	 * @param finalMosILbcOnnet the finalMosILbcOnnet to set
	 */
	public void setFinalMosILbcOnnet(Double[] finalMosILbcOnnet) {
		this.finalMosILbcOnnet = finalMosILbcOnnet;
	}

	/**
	 * @return the finalMosG711Offnet
	 */
	public Double[] getFinalMosG711Offnet() {
		return finalMosG711Offnet;
	}

	/**
	 * @param finalMosG711Offnet the finalMosG711Offnet to set
	 */
	public void setFinalMosG711Offnet(Double[] finalMosG711Offnet) {
		this.finalMosG711Offnet = finalMosG711Offnet;
	}

	/**
	 * @return the finalMosILbcOffnet
	 */
	public Double[] getFinalMosILbcOffnet() {
		return finalMosILbcOffnet;
	}

	/**
	 * @param finalMosILbcOffnet the finalMosILbcOffnet to set
	 */
	public void setFinalMosILbcOffnet(Double[] finalMosILbcOffnet) {
		this.finalMosILbcOffnet = finalMosILbcOffnet;
	}

	/**
	 * @return the callConnectionSetupTimeOnnet
	 */
	public Double[] getCallConnectionSetupTimeOnnet() {
		return callConnectionSetupTimeOnnet;
	}

	/**
	 * @param callConnectionSetupTimeOnnet the callConnectionSetupTimeOnnet to set
	 */
	public void setCallConnectionSetupTimeOnnet(Double[] callConnectionSetupTimeOnnet) {
		this.callConnectionSetupTimeOnnet = callConnectionSetupTimeOnnet;
	}

	/**
	 * @return the callConnectionSetupTimeOffnet
	 */
	public Double[] getCallConnectionSetupTimeOffnet() {
		return callConnectionSetupTimeOffnet;
	}

	/**
	 * @param callConnectionSetupTimeOffnet the callConnectionSetupTimeOffnet to set
	 */
	public void setCallConnectionSetupTimeOffnet(Double[] callConnectionSetupTimeOffnet) {
		this.callConnectionSetupTimeOffnet = callConnectionSetupTimeOffnet;
	}

	/**
	 * @return the callStatus
	 */
	public String getCallStatus() {
		return callStatus;
	}

	/**
	 * @param callStatus the callStatus to set
	 */
	public void setCallStatus(String callStatus) {
		this.callStatus = callStatus;
	}

	/**
	 * @return the sectorId
	 */
	public Integer getSectorId() {
		return sectorId;
	}

	/**
	 * @param sectorId the sectorId to set
	 */
	public void setSectorId(Integer sectorId) {
		this.sectorId = sectorId;
	}

	/**
	 * @return the eNodebId
	 */
	public Integer geteNodebId() {
		return eNodebId;
	}

	/**
	 * @param eNodebId the eNodebId to set
	 */
	public void seteNodebId(Integer eNodebId) {
		this.eNodebId = eNodebId;
	}

	/**
	 * @return the caType
	 */
	public String getCaType() {
		return caType;
	}

	/**
	 * @param caType the caType to set
	 */
	public void setCaType(String caType) {
		this.caType = caType;
	}

	/**
	 * @return the cgi
	 */
	public Integer getCgi() {
		return cgi;
	}

	/**
	 * @param cgi the cgi to set
	 */
	public void setCgi(Integer cgi) {
		this.cgi = cgi;
	}

	/**
	 * @return the httpAttempt
	 */
	public Integer getHttpAttempt() {
		return httpAttempt;
	}

	/**
	 * @param httpAttempt the httpAttempt to set
	 */
	public void setHttpAttempt(Integer httpAttempt) {
		this.httpAttempt = httpAttempt;
	}

	/**
	 * @return the httpSucess
	 */
	public Integer getHttpSucess() {
		return httpSucess;
	}

	/**
	 * @param httpSucess the httpSucess to set
	 */
	public void setHttpSucess(Integer httpSucess) {
		this.httpSucess = httpSucess;
	}

	/**
	 * @return the httpFailure
	 */
	public Integer getHttpFailure() {
		return httpFailure;
	}

	/**
	 * @param httpFailure the httpFailure to set
	 */
	public void setHttpFailure(Integer httpFailure) {
		this.httpFailure = httpFailure;
	}

	/**
	 * @return the httpDrop
	 */
	public Integer getHttpDrop() {
		return httpDrop;
	}

	/**
	 * @param httpDrop the httpDrop to set
	 */
	public void setHttpDrop(Integer httpDrop) {
		this.httpDrop = httpDrop;
	}

	/**
	 * @return the httpDownLoadTime
	 */
	public Double[] getHttpDownLoadTime() {
		return httpDownLoadTime;
	}

	/**
	 * @param httpDownLoadTime the httpDownLoadTime to set
	 */
	public void setHttpDownLoadTime(Double[] httpDownLoadTime) {
		this.httpDownLoadTime = httpDownLoadTime;
	}

	/**
	 * @return the neighbourDataList
	 */
	public List<NeighbourDetailWrapper> getNeighbourDataList() {
		return neighbourDataList;
	}

	/**
	 * @param neighbourDataList the neighbourDataList to set
	 */
	public void setNeighbourDataList(List<NeighbourDetailWrapper> neighbourDataList) {
		this.neighbourDataList = neighbourDataList;
	}

	/**
	 * @return the listNvSMSDetailWrapper
	 */
	public List<NvSMSDetailWrapper> getListNvSMSDetailWrapper() {
		return listNvSMSDetailWrapper;
	}

	/**
	 * @param listNvSMSDetailWrapper the listNvSMSDetailWrapper to set
	 */
	public void setListNvSMSDetailWrapper(List<NvSMSDetailWrapper> listNvSMSDetailWrapper) {
		this.listNvSMSDetailWrapper = listNvSMSDetailWrapper;
	}

	/**
	 * @return the mosStatus
	 */
	public String getMosStatus() {
		return mosStatus;
	}

	/**
	 * @param mosStatus the mosStatus to set
	 */
	public void setMosStatus(String mosStatus) {
		this.mosStatus = mosStatus;
	}

	/**
	 * @return the smsAttempt
	 */
	public Integer getSmsAttempt() {
		return smsAttempt;
	}

	/**
	 * @param smsAttempt the smsAttempt to set
	 */
	public void setSmsAttempt(Integer smsAttempt) {
		this.smsAttempt = smsAttempt;
	}

	/**
	 * @return the smsSucess
	 */
	public Integer getSmsSucess() {
		return smsSucess;
	}

	/**
	 * @param smsSucess the smsSucess to set
	 */
	public void setSmsSucess(Integer smsSucess) {
		this.smsSucess = smsSucess;
	}

	/**
	 * @return the smsFailure
	 */
	public Integer getSmsFailure() {
		return smsFailure;
	}

	/**
	 * @param smsFailure the smsFailure to set
	 */
	public void setSmsFailure(Integer smsFailure) {
		this.smsFailure = smsFailure;
	}

	/**
	 * @return the rscp
	 */
	public Double[] getRscp() {
		return rscp;
	}

	/**
	 * @param rscp the rscp to set
	 */
	public void setRscp(Double[] rscp) {
		this.rscp = rscp;
	}

	/**
	 * @return the ecio
	 */
	public Double[] getEcio() {
		return ecio;
	}

	/**
	 * @param ecio the ecio to set
	 */
	public void setEcio(Double[] ecio) {
		this.ecio = ecio;
	}

	/**
	 * @return the rssi
	 */
	public Double[] getRssi() {
		return rssi;
	}

	/**
	 * @param rssi the rssi to set
	 */
	public void setRssi(Double[] rssi) {
		this.rssi = rssi;
	}

	/**
	 * @return the technologyName
	 */
	public String getTechnologyName() {
		return technologyName;
	}

	/**
	 * @param technologyName the technologyName to set
	 */
	public void setTechnologyName(String technologyName) {
		this.technologyName = technologyName;
	}

	/**
	 * @return the tauAttempt
	 */
	public Integer getTauAttempt() {
		return tauAttempt;
	}

	/**
	 * @param tauAttempt the tauAttempt to set
	 */
	public void setTauAttempt(Integer tauAttempt) {
		this.tauAttempt = tauAttempt;
	}

	/**
	 * @return the tauComplete
	 */
	public Integer getTauComplete() {
		return tauComplete;
	}

	/**
	 * @param tauComplete the tauComplete to set
	 */
	public void setTauComplete(Integer tauComplete) {
		this.tauComplete = tauComplete;
	}

	/**
	 * @return the tauFailure
	 */
	public Integer getTauFailure() {
		return tauFailure;
	}

	/**
	 * @param tauFailure the tauFailure to set
	 */
	public void setTauFailure(Integer tauFailure) {
		this.tauFailure = tauFailure;
	}

	/**
	 * @return the tauTime
	 */
	public Double[] getTauTime() {
		return tauTime;
	}

	/**
	 * @param tauTime the tauTime to set
	 */
	public void setTauTime(Double[] tauTime) {
		this.tauTime = tauTime;
	}

	/**
	 * @return the instantaneousMos
	 */
	public Double[] getInstantaneousMos() {
		return instantaneousMos;
	}

	/**
	 * @param instantaneousMos the instantaneousMos to set
	 */
	public void setInstantaneousMos(Double[] instantaneousMos) {
		this.instantaneousMos = instantaneousMos;
	}

	/**
	 * @return the pdschThroughput
	 */
	public Double[] getPdschThroughput() {
		return pdschThroughput;
	}

	/**
	 * @param pdschThroughput the pdschThroughput to set
	 */
	public void setPdschThroughput(Double[] pdschThroughput) {
		this.pdschThroughput = pdschThroughput;
	}

	/**
	 * @return the pdschThroughputPriCell
	 */
	public Double[] getPdschThroughputPriCell() {
		return pdschThroughputPriCell;
	}

	/**
	 * @param pdschThroughputPriCell the pdschThroughputPriCell to set
	 */
	public void setPdschThroughputPriCell(Double[] pdschThroughputPriCell) {
		this.pdschThroughputPriCell = pdschThroughputPriCell;
	}

	/**
	 * @return the pdschThroughputSecCell1
	 */
	public Double[] getPdschThroughputSecCell1() {
		return pdschThroughputSecCell1;
	}

	/**
	 * @param pdschThroughputSecCell1 the pdschThroughputSecCell1 to set
	 */
	public void setPdschThroughputSecCell1(Double[] pdschThroughputSecCell1) {
		this.pdschThroughputSecCell1 = pdschThroughputSecCell1;
	}

	/**
	 * @return the pdschThroughputSecCell2
	 */
	public Double[] getPdschThroughputSecCell2() {
		return pdschThroughputSecCell2;
	}

	/**
	 * @param pdschThroughputSecCell2 the pdschThroughputSecCell2 to set
	 */
	public void setPdschThroughputSecCell2(Double[] pdschThroughputSecCell2) {
		this.pdschThroughputSecCell2 = pdschThroughputSecCell2;
	}

	/**
	 * @return the pdschThroughputSecCell3
	 */
	public Double[] getPdschThroughputSecCell3() {
		return pdschThroughputSecCell3;
	}

	/**
	 * @param pdschThroughputSecCell3 the pdschThroughputSecCell3 to set
	 */
	public void setPdschThroughputSecCell3(Double[] pdschThroughputSecCell3) {
		this.pdschThroughputSecCell3 = pdschThroughputSecCell3;
	}

	/**
	 * @return the pdschThroughputSecCell4
	 */
	public Double[] getPdschThroughputSecCell4() {
		return pdschThroughputSecCell4;
	}

	/**
	 * @param pdschThroughputSecCell4 the pdschThroughputSecCell4 to set
	 */
	public void setPdschThroughputSecCell4(Double[] pdschThroughputSecCell4) {
		this.pdschThroughputSecCell4 = pdschThroughputSecCell4;
	}

	/**
	 * @return the pdschThroughputSecCell5
	 */
	public Double[] getPdschThroughputSecCell5() {
		return pdschThroughputSecCell5;
	}

	/**
	 * @param pdschThroughputSecCell5 the pdschThroughputSecCell5 to set
	 */
	public void setPdschThroughputSecCell5(Double[] pdschThroughputSecCell5) {
		this.pdschThroughputSecCell5 = pdschThroughputSecCell5;
	}

	/**
	 * @return the puschThroughput
	 */
	public Double[] getPuschThroughput() {
		return puschThroughput;
	}

	/**
	 * @param puschThroughput the puschThroughput to set
	 */
	public void setPuschThroughput(Double[] puschThroughput) {
		this.puschThroughput = puschThroughput;
	}

	/**
	 * @return the puschMcsIndex
	 */
	public Double[] getPuschMcsIndex() {
		return puschMcsIndex;
	}

	/**
	 * @param puschMcsIndex the puschMcsIndex to set
	 */
	public void setPuschMcsIndex(Double[] puschMcsIndex) {
		this.puschMcsIndex = puschMcsIndex;
	}

	/**
	 * @return the puschModulationType
	 */
	public String getPuschModulationType() {
		return puschModulationType;
	}

	/**
	 * @param puschModulationType the puschModulationType to set
	 */
	public void setPuschModulationType(String puschModulationType) {
		this.puschModulationType = puschModulationType;
	}

	/**
	 * @return the pCellPUSCHThroughput
	 */
	public Double[] getpCellPUSCHThroughput() {
		return pCellPUSCHThroughput;
	}

	/**
	 * @param pCellPUSCHThroughput the pCellPUSCHThroughput to set
	 */
	public void setpCellPUSCHThroughput(Double[] pCellPUSCHThroughput) {
		this.pCellPUSCHThroughput = pCellPUSCHThroughput;
	}

	/**
	 * @return the sCell1PUSCHThroughput
	 */
	public Double[] getsCell1PUSCHThroughput() {
		return sCell1PUSCHThroughput;
	}

	/**
	 * @param sCell1PUSCHThroughput the sCell1PUSCHThroughput to set
	 */
	public void setsCell1PUSCHThroughput(Double[] sCell1PUSCHThroughput) {
		this.sCell1PUSCHThroughput = sCell1PUSCHThroughput;
	}

	/**
	 * @return the sCell2PUSCHThroughput
	 */
	public Double[] getsCell2PUSCHThroughput() {
		return sCell2PUSCHThroughput;
	}

	/**
	 * @param sCell2PUSCHThroughput the sCell2PUSCHThroughput to set
	 */
	public void setsCell2PUSCHThroughput(Double[] sCell2PUSCHThroughput) {
		this.sCell2PUSCHThroughput = sCell2PUSCHThroughput;
	}

	/**
	 * @return the sCell3PUSCHThroughput
	 */
	public Double[] getsCell3PUSCHThroughput() {
		return sCell3PUSCHThroughput;
	}

	/**
	 * @param sCell3PUSCHThroughput the sCell3PUSCHThroughput to set
	 */
	public void setsCell3PUSCHThroughput(Double[] sCell3PUSCHThroughput) {
		this.sCell3PUSCHThroughput = sCell3PUSCHThroughput;
	}

	/**
	 * @return the sCell4PUSCHThroughput
	 */
	public Double[] getsCell4PUSCHThroughput() {
		return sCell4PUSCHThroughput;
	}

	/**
	 * @param sCell4PUSCHThroughput the sCell4PUSCHThroughput to set
	 */
	public void setsCell4PUSCHThroughput(Double[] sCell4PUSCHThroughput) {
		this.sCell4PUSCHThroughput = sCell4PUSCHThroughput;
	}

	/**
	 * @return the sCell5PUSCHThroughput
	 */
	public Double[] getsCell5PUSCHThroughput() {
		return sCell5PUSCHThroughput;
	}

	/**
	 * @param sCell5PUSCHThroughput the sCell5PUSCHThroughput to set
	 */
	public void setsCell5PUSCHThroughput(Double[] sCell5PUSCHThroughput) {
		this.sCell5PUSCHThroughput = sCell5PUSCHThroughput;
	}

	/**
	 * @return the sCell6PUSCHThroughput
	 */
	public Double[] getsCell6PUSCHThroughput() {
		return sCell6PUSCHThroughput;
	}

	/**
	 * @param sCell6PUSCHThroughput the sCell6PUSCHThroughput to set
	 */
	public void setsCell6PUSCHThroughput(Double[] sCell6PUSCHThroughput) {
		this.sCell6PUSCHThroughput = sCell6PUSCHThroughput;
	}

	/**
	 * @return the sCell7PUSCHThroughput
	 */
	public Double[] getsCell7PUSCHThroughput() {
		return sCell7PUSCHThroughput;
	}

	/**
	 * @param sCell7PUSCHThroughput the sCell7PUSCHThroughput to set
	 */
	public void setsCell7PUSCHThroughput(Double[] sCell7PUSCHThroughput) {
		this.sCell7PUSCHThroughput = sCell7PUSCHThroughput;
	}

	/**
	 * @return the ueTxPower
	 */
	public Double getUeTxPower() {
		return ueTxPower;
	}

	/**
	 * @param ueTxPower the ueTxPower to set
	 */
	public void setUeTxPower(Double ueTxPower) {
		this.ueTxPower = ueTxPower;
	}

	/**
	 * @return the csfbCallAttempt
	 */
	public Integer getCsfbCallAttempt() {
		return csfbCallAttempt;
	}

	/**
	 * @param csfbCallAttempt the csfbCallAttempt to set
	 */
	public void setCsfbCallAttempt(Integer csfbCallAttempt) {
		this.csfbCallAttempt = csfbCallAttempt;
	}

	/**
	 * @return the csfbCallSuccess
	 */
	public Integer getCsfbCallSuccess() {
		return csfbCallSuccess;
	}

	/**
	 * @param csfbCallSuccess the csfbCallSuccess to set
	 */
	public void setCsfbCallSuccess(Integer csfbCallSuccess) {
		this.csfbCallSuccess = csfbCallSuccess;
	}

	/**
	 * @return the csfbCallDrop
	 */
	public Integer getCsfbCallDrop() {
		return csfbCallDrop;
	}

	/**
	 * @param csfbCallDrop the csfbCallDrop to set
	 */
	public void setCsfbCallDrop(Integer csfbCallDrop) {
		this.csfbCallDrop = csfbCallDrop;
	}

	/**
	 * @return the csfbCallFailed
	 */
	public Integer getCsfbCallFailed() {
		return csfbCallFailed;
	}

	/**
	 * @param csfbCallFailed the csfbCallFailed to set
	 */
	public void setCsfbCallFailed(Integer csfbCallFailed) {
		this.csfbCallFailed = csfbCallFailed;
	}

	/**
	 * @return the csfbCallSetupSuccess
	 */
	public Integer getCsfbCallSetupSuccess() {
		return csfbCallSetupSuccess;
	}

	/**
	 * @param csfbCallSetupSuccess the csfbCallSetupSuccess to set
	 */
	public void setCsfbCallSetupSuccess(Integer csfbCallSetupSuccess) {
		this.csfbCallSetupSuccess = csfbCallSetupSuccess;
	}

	/**
	 * @return the csfbCallSetupFailed
	 */
	public Integer getCsfbCallSetupFailed() {
		return csfbCallSetupFailed;
	}

	/**
	 * @param csfbCallSetupFailed the csfbCallSetupFailed to set
	 */
	public void setCsfbCallSetupFailed(Integer csfbCallSetupFailed) {
		this.csfbCallSetupFailed = csfbCallSetupFailed;
	}

	/**
	 * @return the rrcConnectionComplete
	 */
	public Integer getRrcConnectionComplete() {
		return rrcConnectionComplete;
	}

	/**
	 * @param rrcConnectionComplete the rrcConnectionComplete to set
	 */
	public void setRrcConnectionComplete(Integer rrcConnectionComplete) {
		this.rrcConnectionComplete = rrcConnectionComplete;
	}

	/**
	 * @return the rrcConnectionSetupOk
	 */
	public Integer getRrcConnectionSetupOk() {
		return rrcConnectionSetupOk;
	}

	/**
	 * @param rrcConnectionSetupOk the rrcConnectionSetupOk to set
	 */
	public void setRrcConnectionSetupOk(Integer rrcConnectionSetupOk) {
		this.rrcConnectionSetupOk = rrcConnectionSetupOk;
	}

	/**
	 * @return the rrcConnectionAttempt
	 */
	public Integer getRrcConnectionAttempt() {
		return rrcConnectionAttempt;
	}

	/**
	 * @param rrcConnectionAttempt the rrcConnectionAttempt to set
	 */
	public void setRrcConnectionAttempt(Integer rrcConnectionAttempt) {
		this.rrcConnectionAttempt = rrcConnectionAttempt;
	}

	/**
	 * @return the rrcConnectionRelease
	 */
	public Integer getRrcConnectionRelease() {
		return rrcConnectionRelease;
	}

	/**
	 * @param rrcConnectionRelease the rrcConnectionRelease to set
	 */
	public void setRrcConnectionRelease(Integer rrcConnectionRelease) {
		this.rrcConnectionRelease = rrcConnectionRelease;
	}

	/**
	 * @return the rrcConnectionFailed
	 */
	public Integer getRrcConnectionFailed() {
		return rrcConnectionFailed;
	}

	/**
	 * @param rrcConnectionFailed the rrcConnectionFailed to set
	 */
	public void setRrcConnectionFailed(Integer rrcConnectionFailed) {
		this.rrcConnectionFailed = rrcConnectionFailed;
	}

	/**
	 * @return the rrcConnectionSetupTime
	 */
	public Double[] getRrcConnectionSetupTime() {
		return rrcConnectionSetupTime;
	}

	/**
	 * @param rrcConnectionSetupTime the rrcConnectionSetupTime to set
	 */
	public void setRrcConnectionSetupTime(Double[] rrcConnectionSetupTime) {
		this.rrcConnectionSetupTime = rrcConnectionSetupTime;
	}

	/**
	 * @return the rrcReestablishmentSuccess
	 */
	public Integer getRrcReestablishmentSuccess() {
		return rrcReestablishmentSuccess;
	}

	/**
	 * @param rrcReestablishmentSuccess the rrcReestablishmentSuccess to set
	 */
	public void setRrcReestablishmentSuccess(Integer rrcReestablishmentSuccess) {
		this.rrcReestablishmentSuccess = rrcReestablishmentSuccess;
	}

	/**
	 * @return the rrcReestablishmentFailed
	 */
	public Integer getRrcReestablishmentFailed() {
		return rrcReestablishmentFailed;
	}

	/**
	 * @param rrcReestablishmentFailed the rrcReestablishmentFailed to set
	 */
	public void setRrcReestablishmentFailed(Integer rrcReestablishmentFailed) {
		this.rrcReestablishmentFailed = rrcReestablishmentFailed;
	}

	/**
	 * @return the rrcConnectionSetup
	 */
	public Integer getRrcConnectionSetup() {
		return rrcConnectionSetup;
	}

	/**
	 * @param rrcConnectionSetup the rrcConnectionSetup to set
	 */
	public void setRrcConnectionSetup(Integer rrcConnectionSetup) {
		this.rrcConnectionSetup = rrcConnectionSetup;
	}

	/**
	 * @return the cqiCw1
	 */
	public Double[] getCqiCw1() {
		return cqiCw1;
	}

	/**
	 * @param cqiCw1 the cqiCw1 to set
	 */
	public void setCqiCw1(Double[] cqiCw1) {
		this.cqiCw1 = cqiCw1;
	}

	/**
	 * @return the dlPrb
	 */
	public Double[] getDlPrb() {
		return dlPrb;
	}

	/**
	 * @param dlPrb the dlPrb to set
	 */
	public void setDlPrb(Double[] dlPrb) {
		this.dlPrb = dlPrb;
	}

	/**
	 * @return the pdschnumRb
	 */
	public Double[] getPdschnumRb() {
		return pdschnumRb;
	}

	/**
	 * @param pdschnumRb the pdschnumRb to set
	 */
	public void setPdschnumRb(Double[] pdschnumRb) {
		this.pdschnumRb = pdschnumRb;
	}

	/**
	 * @return the pdschCwoModulation
	 */
	public String getPdschCwoModulation() {
		return pdschCwoModulation;
	}

	/**
	 * @param pdschCwoModulation the pdschCwoModulation to set
	 */
	public void setPdschCwoModulation(String pdschCwoModulation) {
		this.pdschCwoModulation = pdschCwoModulation;
	}

	/**
	 * @return the pdschCwoMcs
	 */
	public Integer getPdschCwoMcs() {
		return pdschCwoMcs;
	}

	/**
	 * @param pdschCwoMcs the pdschCwoMcs to set
	 */
	public void setPdschCwoMcs(Integer pdschCwoMcs) {
		this.pdschCwoMcs = pdschCwoMcs;
	}

	/**
	 * @return the speedTestDlRate
	 */
	public Double getSpeedTestDlRate() {
		return speedTestDlRate;
	}

	/**
	 * @param speedTestDlRate the speedTestDlRate to set
	 */
	public void setSpeedTestDlRate(Double speedTestDlRate) {
		this.speedTestDlRate = speedTestDlRate;
	}

	/**
	 * @return the speedTestUlRate
	 */
	public Double getSpeedTestUlRate() {
		return speedTestUlRate;
	}

	/**
	 * @param speedTestUlRate the speedTestUlRate to set
	 */
	public void setSpeedTestUlRate(Double speedTestUlRate) {
		this.speedTestUlRate = speedTestUlRate;
	}

	/**
	 * @return the speedTestPinNumber
	 */
	public Integer getSpeedTestPinNumber() {
		return speedTestPinNumber;
	}

	/**
	 * @param speedTestPinNumber the speedTestPinNumber to set
	 */
	public void setSpeedTestPinNumber(Integer speedTestPinNumber) {
		this.speedTestPinNumber = speedTestPinNumber;
	}

	/**
	 * @return the downloadTimeGoogle
	 */
	public Double getDownloadTimeGoogle() {
		return downloadTimeGoogle;
	}

	/**
	 * @param downloadTimeGoogle the downloadTimeGoogle to set
	 */
	public void setDownloadTimeGoogle(Double downloadTimeGoogle) {
		this.downloadTimeGoogle = downloadTimeGoogle;
	}

	/**
	 * @return the downloadTimeFacebook
	 */
	public Double getDownloadTimeFacebook() {
		return downloadTimeFacebook;
	}

	/**
	 * @param downloadTimeFacebook the downloadTimeFacebook to set
	 */
	public void setDownloadTimeFacebook(Double downloadTimeFacebook) {
		this.downloadTimeFacebook = downloadTimeFacebook;
	}

	/**
	 * @return the downloadTimeYoutube
	 */
	public Double getDownloadTimeYoutube() {
		return downloadTimeYoutube;
	}

	/**
	 * @param downloadTimeYoutube the downloadTimeYoutube to set
	 */
	public void setDownloadTimeYoutube(Double downloadTimeYoutube) {
		this.downloadTimeYoutube = downloadTimeYoutube;
	}

	/**
	 * @return the transmissionMode
	 */
	public String getTransmissionMode() {
		return transmissionMode;
	}

	/**
	 * @param transmissionMode the transmissionMode to set
	 */
	public void setTransmissionMode(String transmissionMode) {
		this.transmissionMode = transmissionMode;
	}

	/**
	 * @return the rxLev
	 */
	public Double[] getRxLev() {
		return rxLev;
	}

	/**
	 * @param rxLev the rxLev to set
	 */
	public void setRxLev(Double[] rxLev) {
		this.rxLev = rxLev;
	}

	/**
	 * @return the rxQual
	 */
	public Double[] getRxQual() {
		return rxQual;
	}

	/**
	 * @param rxQual the rxQual to set
	 */
	public void setRxQual(Double[] rxQual) {
		this.rxQual = rxQual;
	}

	/**
	 * @return the bCCHChannel
	 */
	public Integer getbCCHChannel() {
		return bCCHChannel;
	}

	/**
	 * @param bCCHChannel the bCCHChannel to set
	 */
	public void setbCCHChannel(Integer bCCHChannel) {
		this.bCCHChannel = bCCHChannel;
	}

	/**
	 * @return the bSIC
	 */
	public Integer getbSIC() {
		return bSIC;
	}

	/**
	 * @param bSIC the bSIC to set
	 */
	public void setbSIC(Integer bSIC) {
		this.bSIC = bSIC;
	}

	/**
	 * @return the preambleCountMax
	 */
	public Integer getPreambleCountMax() {
		return preambleCountMax;
	}

	/**
	 * @param preambleCountMax the preambleCountMax to set
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
	 * @param preambleInitialTxPower the preambleInitialTxPower to set
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
	 * @param raRNTI the raRNTI to set
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
	 * @param raTimingAdvance the raTimingAdvance to set
	 */
	public void setRaTimingAdvance(Integer raTimingAdvance) {
		this.raTimingAdvance = raTimingAdvance;
	}

	/**
	 * @return the dominantChannelRSSI
	 */
	public Double[] getDominantChannelRSSI() {
		return dominantChannelRSSI;
	}

	/**
	 * @param dominantChannelRSSI the dominantChannelRSSI to set
	 */
	public void setDominantChannelRSSI(Double[] dominantChannelRSSI) {
		this.dominantChannelRSSI = dominantChannelRSSI;
	}

	/**
	 * @return the dominantChannelRSSIRx0
	 */
	public Double[] getDominantChannelRSSIRx0() {
		return dominantChannelRSSIRx0;
	}

	/**
	 * @param dominantChannelRSSIRx0 the dominantChannelRSSIRx0 to set
	 */
	public void setDominantChannelRSSIRx0(Double[] dominantChannelRSSIRx0) {
		this.dominantChannelRSSIRx0 = dominantChannelRSSIRx0;
	}

	/**
	 * @return the dominantChannelRSSIRx1
	 */
	public Double[] getDominantChannelRSSIRx1() {
		return dominantChannelRSSIRx1;
	}

	/**
	 * @param dominantChannelRSSIRx1 the dominantChannelRSSIRx1 to set
	 */
	public void setDominantChannelRSSIRx1(Double[] dominantChannelRSSIRx1) {
		this.dominantChannelRSSIRx1 = dominantChannelRSSIRx1;
	}

	/**
	 * @return the maxTTLPDSCHThroughput
	 */
	public Double getMaxTTLPDSCHThroughput() {
		return maxTTLPDSCHThroughput;
	}

	/**
	 * @param maxTTLPDSCHThroughput the maxTTLPDSCHThroughput to set
	 */
	public void setMaxTTLPDSCHThroughput(Double maxTTLPDSCHThroughput) {
		this.maxTTLPDSCHThroughput = maxTTLPDSCHThroughput;
	}

	/**
	 * @return the maxTTLPUSCHThroughput
	 */
	public Double getMaxTTLPUSCHThroughput() {
		return maxTTLPUSCHThroughput;
	}

	/**
	 * @param maxTTLPUSCHThroughput the maxTTLPUSCHThroughput to set
	 */
	public void setMaxTTLPUSCHThroughput(Double maxTTLPUSCHThroughput) {
		this.maxTTLPUSCHThroughput = maxTTLPUSCHThroughput;
	}

	/**
	 * @return the dominantChannelPCI
	 */
	public Integer getDominantChannelPCI() {
		return dominantChannelPCI;
	}

	/**
	 * @param dominantChannelPCI the dominantChannelPCI to set
	 */
	public void setDominantChannelPCI(Integer dominantChannelPCI) {
		this.dominantChannelPCI = dominantChannelPCI;
	}

	/**
	 * @return the dominantChannelRSRQ
	 */
	public Double[] getDominantChannelRSRQ() {
		return dominantChannelRSRQ;
	}

	/**
	 * @param dominantChannelRSRQ the dominantChannelRSRQ to set
	 */
	public void setDominantChannelRSRQ(Double[] dominantChannelRSRQ) {
		this.dominantChannelRSRQ = dominantChannelRSRQ;
	}

	/**
	 * @return the dominantChannelRSRQRx0
	 */
	public Double[] getDominantChannelRSRQRx0() {
		return dominantChannelRSRQRx0;
	}

	/**
	 * @param dominantChannelRSRQRx0 the dominantChannelRSRQRx0 to set
	 */
	public void setDominantChannelRSRQRx0(Double[] dominantChannelRSRQRx0) {
		this.dominantChannelRSRQRx0 = dominantChannelRSRQRx0;
	}

	/**
	 * @return the dominantChannelRSRQRx1
	 */
	public Double[] getDominantChannelRSRQRx1() {
		return dominantChannelRSRQRx1;
	}

	/**
	 * @param dominantChannelRSRQRx1 the dominantChannelRSRQRx1 to set
	 */
	public void setDominantChannelRSRQRx1(Double[] dominantChannelRSRQRx1) {
		this.dominantChannelRSRQRx1 = dominantChannelRSRQRx1;
	}

	/**
	 * @return the dominantChannelRSRP
	 */
	public Double[] getDominantChannelRSRP() {
		return dominantChannelRSRP;
	}

	/**
	 * @param dominantChannelRSRP the dominantChannelRSRP to set
	 */
	public void setDominantChannelRSRP(Double[] dominantChannelRSRP) {
		this.dominantChannelRSRP = dominantChannelRSRP;
	}

	/**
	 * @return the dominantChannelRSRPRx0
	 */
	public Double[] getDominantChannelRSRPRx0() {
		return dominantChannelRSRPRx0;
	}

	/**
	 * @param dominantChannelRSRPRx0 the dominantChannelRSRPRx0 to set
	 */
	public void setDominantChannelRSRPRx0(Double[] dominantChannelRSRPRx0) {
		this.dominantChannelRSRPRx0 = dominantChannelRSRPRx0;
	}

	/**
	 * @return the dominantChannelRSRPRx1
	 */
	public Double[] getDominantChannelRSRPRx1() {
		return dominantChannelRSRPRx1;
	}

	/**
	 * @param dominantChannelRSRPRx1 the dominantChannelRSRPRx1 to set
	 */
	public void setDominantChannelRSRPRx1(Double[] dominantChannelRSRPRx1) {
		this.dominantChannelRSRPRx1 = dominantChannelRSRPRx1;
	}

	/**
	 * @return the tb0McsIndex
	 */
	public Integer getTb0McsIndex() {
		return tb0McsIndex;
	}

	/**
	 * @param tb0McsIndex the tb0McsIndex to set
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
	 * @param tb1McsIndex the tb1McsIndex to set
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
	 * @param tb0ModulationType the tb0ModulationType to set
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
	 * @param tb1ModulationType the tb1ModulationType to set
	 */
	public void setTb1ModulationType(String tb1ModulationType) {
		this.tb1ModulationType = tb1ModulationType;
	}

	/**
	 * @return the avgDLTb0Size
	 */
	public Double getAvgDLTb0Size() {
		return avgDLTb0Size;
	}

	/**
	 * @param avgDLTb0Size the avgDLTb0Size to set
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
	 * @param avgDLTb1Size the avgDLTb1Size to set
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
	 * @param avgDLTbSize the avgDLTbSize to set
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
	 * @param avgULTBSize the avgULTBSize to set
	 */
	public void setAvgULTBSize(Double avgULTBSize) {
		this.avgULTBSize = avgULTBSize;
	}

	/**
	 * @return the macUlThroughput
	 */
	public Double[] getMacUlThroughput() {
		return macUlThroughput;
	}

	/**
	 * @param macUlThroughput the macUlThroughput to set
	 */
	public void setMacUlThroughput(Double[] macUlThroughput) {
		this.macUlThroughput = macUlThroughput;
	}

	/**
	 * @return the prachTxPower
	 */
	public Double getPrachTxPower() {
		return prachTxPower;
	}

	/**
	 * @param prachTxPower the prachTxPower to set
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
	 * @param powerHeadroomdata the powerHeadroomdata to set
	 */
	public void setPowerHeadroomdata(Integer powerHeadroomdata) {
		this.powerHeadroomdata = powerHeadroomdata;
	}

	/**
	 * @return the ulPUSCHTxPower
	 */
	public Integer getUlPUSCHTxPower() {
		return ulPUSCHTxPower;
	}

	/**
	 * @param ulPUSCHTxPower the ulPUSCHTxPower to set
	 */
	public void setUlPUSCHTxPower(Integer ulPUSCHTxPower) {
		this.ulPUSCHTxPower = ulPUSCHTxPower;
	}

	/**
	 * @return the lteDominantFrequency
	 */
	public Long getLteDominantFrequency() {
		return lteDominantFrequency;
	}

	/**
	 * @param lteDominantFrequency the lteDominantFrequency to set
	 */
	public void setLteDominantFrequency(Long lteDominantFrequency) {
		this.lteDominantFrequency = lteDominantFrequency;
	}

	/**
	 * @return the pdschBLER
	 */
	public Double[] getPdschBLER() {
		return pdschBLER;
	}

	/**
	 * @param pdschBLER the pdschBLER to set
	 */
	public void setPdschBLER(Double[] pdschBLER) {
		this.pdschBLER = pdschBLER;
	}

	/**
	 * @return the handoverInterruption
	 */
	public Double[] getHandoverInterruption() {
		return handoverInterruption;
	}

	/**
	 * @param handoverInterruption the handoverInterruption to set
	 */
	public void setHandoverInterruption(Double[] handoverInterruption) {
		this.handoverInterruption = handoverInterruption;
	}

	/**
	 * @return the handoverInterruptionTimeOfQCI1
	 */
	public Double[] getHandoverInterruptionTimeOfQCI1() {
		return handoverInterruptionTimeOfQCI1;
	}

	/**
	 * @param handoverInterruptionTimeOfQCI1 the handoverInterruptionTimeOfQCI1 to set
	 */
	public void setHandoverInterruptionTimeOfQCI1(Double[] handoverInterruptionTimeOfQCI1) {
		this.handoverInterruptionTimeOfQCI1 = handoverInterruptionTimeOfQCI1;
	}

	/**
	 * @return the handoverInterruptionTimeOfQCI9DL
	 */
	public Double[] getHandoverInterruptionTimeOfQCI9DL() {
		return handoverInterruptionTimeOfQCI9DL;
	}

	/**
	 * @param handoverInterruptionTimeOfQCI9DL the handoverInterruptionTimeOfQCI9DL to set
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
	 * @param handoverInterruptionTimeOfQCI9UL the handoverInterruptionTimeOfQCI9UL to set
	 */
	public void setHandoverInterruptionTimeOfQCI9UL(Double[] handoverInterruptionTimeOfQCI9UL) {
		this.handoverInterruptionTimeOfQCI9UL = handoverInterruptionTimeOfQCI9UL;
	}

	/**
	 * @return the attachRequest
	 */
	public Integer getAttachRequest() {
		return attachRequest;
	}

	/**
	 * @param attachRequest the attachRequest to set
	 */
	public void setAttachRequest(Integer attachRequest) {
		this.attachRequest = attachRequest;
	}

	/**
	 * @return the attachAccept
	 */
	public Integer getAttachAccept() {
		return attachAccept;
	}

	/**
	 * @param attachAccept the attachAccept to set
	 */
	public void setAttachAccept(Integer attachAccept) {
		this.attachAccept = attachAccept;
	}

	/**
	 * @return the b0C0Count
	 */
	public Integer getB0C0Count() {
		return b0C0Count;
	}

	/**
	 * @param b0c0Count the b0C0Count to set
	 */
	public void setB0C0Count(Integer b0c0Count) {
		b0C0Count = b0c0Count;
	}

	/**
	 * @return the message3Count
	 */
	public Integer getMessage3Count() {
		return message3Count;
	}

	/**
	 * @param message3Count the message3Count to set
	 */
	public void setMessage3Count(Integer message3Count) {
		this.message3Count = message3Count;
	}

	/**
	 * @return the message1Count
	 */
	public Integer getMessage1Count() {
		return message1Count;
	}

	/**
	 * @param message1Count the message1Count to set
	 */
	public void setMessage1Count(Integer message1Count) {
		this.message1Count = message1Count;
	}

	/**
	 * @return the emmState
	 */
	public String getEmmState() {
		return emmState;
	}

	/**
	 * @param emmState the emmState to set
	 */
	public void setEmmState(String emmState) {
		this.emmState = emmState;
	}

	/**
	 * @return the rrcState
	 */
	public String getRrcState() {
		return rrcState;
	}

	/**
	 * @param rrcState the rrcState to set
	 */
	public void setRrcState(String rrcState) {
		this.rrcState = rrcState;
	}

	/**
	 * @return the emmSubState
	 */
	public String getEmmSubState() {
		return emmSubState;
	}

	/**
	 * @param emmSubState the emmSubState to set
	 */
	public void setEmmSubState(String emmSubState) {
		this.emmSubState = emmSubState;
	}

	/**
	 * @return the avgULPRB
	 */
	public Double[] getAvgULPRB() {
		return avgULPRB;
	}

	/**
	 * @param avgULPRB the avgULPRB to set
	 */
	public void setAvgULPRB(Double[] avgULPRB) {
		this.avgULPRB = avgULPRB;
	}

	/**
	 * @return the geoHash
	 */
	public String getGeoHash() {
		return geoHash;
	}

	/**
	 * @param geoHash the geoHash to set
	 */
	public void setGeoHash(String geoHash) {
		this.geoHash = geoHash;
	}

	/**
	 * @return the voiceBearerActivationRequest
	 */
	public Integer getVoiceBearerActivationRequest() {
		return voiceBearerActivationRequest;
	}

	/**
	 * @param voiceBearerActivationRequest the voiceBearerActivationRequest to set
	 */
	public void setVoiceBearerActivationRequest(Integer voiceBearerActivationRequest) {
		this.voiceBearerActivationRequest = voiceBearerActivationRequest;
	}

	/**
	 * @return the voiceBearerActivationSuccess
	 */
	public Integer getVoiceBearerActivationSuccess() {
		return voiceBearerActivationSuccess;
	}

	/**
	 * @param voiceBearerActivationSuccess the voiceBearerActivationSuccess to set
	 */
	public void setVoiceBearerActivationSuccess(Integer voiceBearerActivationSuccess) {
		this.voiceBearerActivationSuccess = voiceBearerActivationSuccess;
	}

	/**
	 * @return the voiceBearerActivationFailure
	 */
	public Integer getVoiceBearerActivationFailure() {
		return voiceBearerActivationFailure;
	}

	/**
	 * @param voiceBearerActivationFailure the voiceBearerActivationFailure to set
	 */
	public void setVoiceBearerActivationFailure(Integer voiceBearerActivationFailure) {
		this.voiceBearerActivationFailure = voiceBearerActivationFailure;
	}

	/**
	 * @return the voiceBearerDeactivationRequest
	 */
	public Integer getVoiceBearerDeactivationRequest() {
		return voiceBearerDeactivationRequest;
	}

	/**
	 * @param voiceBearerDeactivationRequest the voiceBearerDeactivationRequest to set
	 */
	public void setVoiceBearerDeactivationRequest(Integer voiceBearerDeactivationRequest) {
		this.voiceBearerDeactivationRequest = voiceBearerDeactivationRequest;
	}

	/**
	 * @return the initialIMSRegistrationSuccess
	 */
	public Integer getInitialIMSRegistrationSuccess() {
		return initialIMSRegistrationSuccess;
	}

	/**
	 * @param initialIMSRegistrationSuccess the initialIMSRegistrationSuccess to set
	 */
	public void setInitialIMSRegistrationSuccess(Integer initialIMSRegistrationSuccess) {
		this.initialIMSRegistrationSuccess = initialIMSRegistrationSuccess;
	}

	/**
	 * @return the initialIMSRegistrationFailure
	 */
	public Integer getInitialIMSRegistrationFailure() {
		return initialIMSRegistrationFailure;
	}

	/**
	 * @param initialIMSRegistrationFailure the initialIMSRegistrationFailure to set
	 */
	public void setInitialIMSRegistrationFailure(Integer initialIMSRegistrationFailure) {
		this.initialIMSRegistrationFailure = initialIMSRegistrationFailure;
	}

	/**
	 * @return the attachComplete
	 */
	public Integer getAttachComplete() {
		return attachComplete;
	}

	/**
	 * @param attachComplete the attachComplete to set
	 */
	public void setAttachComplete(Integer attachComplete) {
		this.attachComplete = attachComplete;
	}

	/**
	 * @return the detachRequest
	 */
	public Integer getDetachRequest() {
		return detachRequest;
	}

	/**
	 * @param detachRequest the detachRequest to set
	 */
	public void setDetachRequest(Integer detachRequest) {
		this.detachRequest = detachRequest;
	}

	/**
	 * @return the lteEMMRegisteredEvent
	 */
	public Integer getLteEMMRegisteredEvent() {
		return lteEMMRegisteredEvent;
	}

	/**
	 * @param lteEMMRegisteredEvent the lteEMMRegisteredEvent to set
	 */
	public void setLteEMMRegisteredEvent(Integer lteEMMRegisteredEvent) {
		this.lteEMMRegisteredEvent = lteEMMRegisteredEvent;
	}

	/**
	 * @return the ltePDNConnectionRequest
	 */
	public Integer getLtePDNConnectionRequest() {
		return ltePDNConnectionRequest;
	}

	/**
	 * @param ltePDNConnectionRequest the ltePDNConnectionRequest to set
	 */
	public void setLtePDNConnectionRequest(Integer ltePDNConnectionRequest) {
		this.ltePDNConnectionRequest = ltePDNConnectionRequest;
	}

	/**
	 * @return the lteRACHPreambleSuccess
	 */
	public Integer getLteRACHPreambleSuccess() {
		return lteRACHPreambleSuccess;
	}

	/**
	 * @param lteRACHPreambleSuccess the lteRACHPreambleSuccess to set
	 */
	public void setLteRACHPreambleSuccess(Integer lteRACHPreambleSuccess) {
		this.lteRACHPreambleSuccess = lteRACHPreambleSuccess;
	}

	/**
	 * @return the lteRACHProcedureSuccess
	 */
	public Integer getLteRACHProcedureSuccess() {
		return lteRACHProcedureSuccess;
	}

	/**
	 * @param lteRACHProcedureSuccess the lteRACHProcedureSuccess to set
	 */
	public void setLteRACHProcedureSuccess(Integer lteRACHProcedureSuccess) {
		this.lteRACHProcedureSuccess = lteRACHProcedureSuccess;
	}

	/**
	 * @return the lteRACHPreambleFailure
	 */
	public Integer getLteRACHPreambleFailure() {
		return lteRACHPreambleFailure;
	}

	/**
	 * @param lteRACHPreambleFailure the lteRACHPreambleFailure to set
	 */
	public void setLteRACHPreambleFailure(Integer lteRACHPreambleFailure) {
		this.lteRACHPreambleFailure = lteRACHPreambleFailure;
	}

	/**
	 * @return the lteRACHProcedureFailure
	 */
	public Integer getLteRACHProcedureFailure() {
		return lteRACHProcedureFailure;
	}

	/**
	 * @param lteRACHProcedureFailure the lteRACHProcedureFailure to set
	 */
	public void setLteRACHProcedureFailure(Integer lteRACHProcedureFailure) {
		this.lteRACHProcedureFailure = lteRACHProcedureFailure;
	}

	/**
	 * @return the radioLinkFailure
	 */
	public Integer getRadioLinkFailure() {
		return radioLinkFailure;
	}

	/**
	 * @param radioLinkFailure the radioLinkFailure to set
	 */
	public void setRadioLinkFailure(Integer radioLinkFailure) {
		this.radioLinkFailure = radioLinkFailure;
	}

	/**
	 * @return the pagingMessageCount
	 */
	public Integer getPagingMessageCount() {
		return pagingMessageCount;
	}

	/**
	 * @param pagingMessageCount the pagingMessageCount to set
	 */
	public void setPagingMessageCount(Integer pagingMessageCount) {
		this.pagingMessageCount = pagingMessageCount;
	}

	/**
	 * @return the serviceRequest
	 */
	public Integer getServiceRequest() {
		return serviceRequest;
	}

	/**
	 * @param serviceRequest the serviceRequest to set
	 */
	public void setServiceRequest(Integer serviceRequest) {
		this.serviceRequest = serviceRequest;
	}

	/**
	 * @return the rrcConnReconfiguration
	 */
	public Integer getRrcConnReconfiguration() {
		return rrcConnReconfiguration;
	}

	/**
	 * @param rrcConnReconfiguration the rrcConnReconfiguration to set
	 */
	public void setRrcConnReconfiguration(Integer rrcConnReconfiguration) {
		this.rrcConnReconfiguration = rrcConnReconfiguration;
	}

	/**
	 * @return the rrcConnReconfSuccess
	 */
	public Integer getRrcConnReconfSuccess() {
		return rrcConnReconfSuccess;
	}

	/**
	 * @param rrcConnReconfSuccess the rrcConnReconfSuccess to set
	 */
	public void setRrcConnReconfSuccess(Integer rrcConnReconfSuccess) {
		this.rrcConnReconfSuccess = rrcConnReconfSuccess;
	}

	/**
	 * @return the rrcConnectionDropped
	 */
	public Integer getRrcConnectionDropped() {
		return rrcConnectionDropped;
	}

	/**
	 * @param rrcConnectionDropped the rrcConnectionDropped to set
	 */
	public void setRrcConnectionDropped(Integer rrcConnectionDropped) {
		this.rrcConnectionDropped = rrcConnectionDropped;
	}

	/**
	 * @return the serviceReject
	 */
	public Integer getServiceReject() {
		return serviceReject;
	}

	/**
	 * @param serviceReject the serviceReject to set
	 */
	public void setServiceReject(Integer serviceReject) {
		this.serviceReject = serviceReject;
	}

	/**
	 * @return the serviceFailure
	 */
	public Integer getServiceFailure() {
		return serviceFailure;
	}

	/**
	 * @param serviceFailure the serviceFailure to set
	 */
	public void setServiceFailure(Integer serviceFailure) {
		this.serviceFailure = serviceFailure;
	}

	/**
	 * @return the authenticationResponse
	 */
	public Integer getAuthenticationResponse() {
		return authenticationResponse;
	}

	/**
	 * @param authenticationResponse the authenticationResponse to set
	 */
	public void setAuthenticationResponse(Integer authenticationResponse) {
		this.authenticationResponse = authenticationResponse;
	}

	/**
	 * @return the authenticationFailure
	 */
	public Integer getAuthenticationFailure() {
		return authenticationFailure;
	}

	/**
	 * @param authenticationFailure the authenticationFailure to set
	 */
	public void setAuthenticationFailure(Integer authenticationFailure) {
		this.authenticationFailure = authenticationFailure;
	}

	/**
	 * @return the authenticationReject
	 */
	public Integer getAuthenticationReject() {
		return authenticationReject;
	}

	/**
	 * @param authenticationReject the authenticationReject to set
	 */
	public void setAuthenticationReject(Integer authenticationReject) {
		this.authenticationReject = authenticationReject;
	}

	/**
	 * @return the authenticationRequest
	 */
	public Integer getAuthenticationRequest() {
		return authenticationRequest;
	}

	/**
	 * @param authenticationRequest the authenticationRequest to set
	 */
	public void setAuthenticationRequest(Integer authenticationRequest) {
		this.authenticationRequest = authenticationRequest;
	}

	/**
	 * @return the mtAccess
	 */
	public Integer getMtAccess() {
		return mtAccess;
	}

	/**
	 * @param mtAccess the mtAccess to set
	 */
	public void setMtAccess(Integer mtAccess) {
		this.mtAccess = mtAccess;
	}

	/**
	 * @return the moSignalling
	 */
	public Integer getMoSignalling() {
		return moSignalling;
	}

	/**
	 * @param moSignalling the moSignalling to set
	 */
	public void setMoSignalling(Integer moSignalling) {
		this.moSignalling = moSignalling;
	}

	/**
	 * @return the securityModeComplete
	 */
	public Integer getSecurityModeComplete() {
		return securityModeComplete;
	}

	/**
	 * @param securityModeComplete the securityModeComplete to set
	 */
	public void setSecurityModeComplete(Integer securityModeComplete) {
		this.securityModeComplete = securityModeComplete;
	}

	/**
	 * @return the reestablishmentFailure
	 */
	public Integer getReestablishmentFailure() {
		return reestablishmentFailure;
	}

	/**
	 * @param reestablishmentFailure the reestablishmentFailure to set
	 */
	public void setReestablishmentFailure(Integer reestablishmentFailure) {
		this.reestablishmentFailure = reestablishmentFailure;
	}

	/**
	 * @return the rrcSetupFailure
	 */
	public Integer getRrcSetupFailure() {
		return rrcSetupFailure;
	}

	/**
	 * @param rrcSetupFailure the rrcSetupFailure to set
	 */
	public void setRrcSetupFailure(Integer rrcSetupFailure) {
		this.rrcSetupFailure = rrcSetupFailure;
	}

	/**
	 * @return the rrcConnReconfFailure
	 */
	public Integer getRrcConnReconfFailure() {
		return rrcConnReconfFailure;
	}

	/**
	 * @param rrcConnReconfFailure the rrcConnReconfFailure to set
	 */
	public void setRrcConnReconfFailure(Integer rrcConnReconfFailure) {
		this.rrcConnReconfFailure = rrcConnReconfFailure;
	}

	/**
	 * @return the rrcEutra
	 */
	public Integer getRrcEutra() {
		return rrcEutra;
	}

	/**
	 * @param rrcEutra the rrcEutra to set
	 */
	public void setRrcEutra(Integer rrcEutra) {
		this.rrcEutra = rrcEutra;
	}

	/**
	 * @return the rrcGeran
	 */
	public Integer getRrcGeran() {
		return rrcGeran;
	}

	/**
	 * @param rrcGeran the rrcGeran to set
	 */
	public void setRrcGeran(Integer rrcGeran) {
		this.rrcGeran = rrcGeran;
	}

	/**
	 * @return the rrcUtraFdd
	 */
	public Integer getRrcUtraFdd() {
		return rrcUtraFdd;
	}

	/**
	 * @param rrcUtraFdd the rrcUtraFdd to set
	 */
	public void setRrcUtraFdd(Integer rrcUtraFdd) {
		this.rrcUtraFdd = rrcUtraFdd;
	}

	/**
	 * @return the rrcUtraTdd
	 */
	public Integer getRrcUtraTdd() {
		return rrcUtraTdd;
	}

	/**
	 * @param rrcUtraTdd the rrcUtraTdd to set
	 */
	public void setRrcUtraTdd(Integer rrcUtraTdd) {
		this.rrcUtraTdd = rrcUtraTdd;
	}

	/**
	 * @return the lteUmtsRequest
	 */
	public Integer getLteUmtsRequest() {
		return lteUmtsRequest;
	}

	/**
	 * @param lteUmtsRequest the lteUmtsRequest to set
	 */
	public void setLteUmtsRequest(Integer lteUmtsRequest) {
		this.lteUmtsRequest = lteUmtsRequest;
	}

	/**
	 * @return the lteUmtsSuccess
	 */
	public Integer getLteUmtsSuccess() {
		return lteUmtsSuccess;
	}

	/**
	 * @param lteUmtsSuccess the lteUmtsSuccess to set
	 */
	public void setLteUmtsSuccess(Integer lteUmtsSuccess) {
		this.lteUmtsSuccess = lteUmtsSuccess;
	}

	/**
	 * @return the erabSetup
	 */
	public Integer getErabSetup() {
		return erabSetup;
	}

	/**
	 * @param erabSetup the erabSetup to set
	 */
	public void setErabSetup(Integer erabSetup) {
		this.erabSetup = erabSetup;
	}

	/**
	 * @return the erabSetupSuccess
	 */
	public Integer getErabSetupSuccess() {
		return erabSetupSuccess;
	}

	/**
	 * @param erabSetupSuccess the erabSetupSuccess to set
	 */
	public void setErabSetupSuccess(Integer erabSetupSuccess) {
		this.erabSetupSuccess = erabSetupSuccess;
	}

	/**
	 * @return the serviceRequestSuccess
	 */
	public Integer getServiceRequestSuccess() {
		return serviceRequestSuccess;
	}

	/**
	 * @param serviceRequestSuccess the serviceRequestSuccess to set
	 */
	public void setServiceRequestSuccess(Integer serviceRequestSuccess) {
		this.serviceRequestSuccess = serviceRequestSuccess;
	}

	/**
	 * @return the umtsLteRequest
	 */
	public Integer getUmtsLteRequest() {
		return umtsLteRequest;
	}

	/**
	 * @param umtsLteRequest the umtsLteRequest to set
	 */
	public void setUmtsLteRequest(Integer umtsLteRequest) {
		this.umtsLteRequest = umtsLteRequest;
	}

	/**
	 * @return the umtsLteSuccess
	 */
	public Integer getUmtsLteSuccess() {
		return umtsLteSuccess;
	}

	/**
	 * @param umtsLteSuccess the umtsLteSuccess to set
	 */
	public void setUmtsLteSuccess(Integer umtsLteSuccess) {
		this.umtsLteSuccess = umtsLteSuccess;
	}

	/**
	 * @return the moCsfbRequest
	 */
	public Integer getMoCsfbRequest() {
		return moCsfbRequest;
	}

	/**
	 * @param moCsfbRequest the moCsfbRequest to set
	 */
	public void setMoCsfbRequest(Integer moCsfbRequest) {
		this.moCsfbRequest = moCsfbRequest;
	}

	/**
	 * @return the mtCsfbRequest
	 */
	public Integer getMtCsfbRequest() {
		return mtCsfbRequest;
	}

	/**
	 * @param mtCsfbRequest the mtCsfbRequest to set
	 */
	public void setMtCsfbRequest(Integer mtCsfbRequest) {
		this.mtCsfbRequest = mtCsfbRequest;
	}

	/**
	 * @return the moCsfbSuccess
	 */
	public Integer getMoCsfbSuccess() {
		return moCsfbSuccess;
	}

	/**
	 * @param moCsfbSuccess the moCsfbSuccess to set
	 */
	public void setMoCsfbSuccess(Integer moCsfbSuccess) {
		this.moCsfbSuccess = moCsfbSuccess;
	}

	/**
	 * @return the mtCsfbSuccess
	 */
	public Integer getMtCsfbSuccess() {
		return mtCsfbSuccess;
	}

	/**
	 * @param mtCsfbSuccess the mtCsfbSuccess to set
	 */
	public void setMtCsfbSuccess(Integer mtCsfbSuccess) {
		this.mtCsfbSuccess = mtCsfbSuccess;
	}

	/**
	 * @return the rrcReconfigCompleteMissing
	 */
	public Integer getRrcReconfigCompleteMissing() {
		return rrcReconfigCompleteMissing;
	}

	/**
	 * @param rrcReconfigCompleteMissing the rrcReconfigCompleteMissing to set
	 */
	public void setRrcReconfigCompleteMissing(Integer rrcReconfigCompleteMissing) {
		this.rrcReconfigCompleteMissing = rrcReconfigCompleteMissing;
	}

	/**
	 * @return the moCsfbFailure
	 */
	public Integer getMoCsfbFailure() {
		return moCsfbFailure;
	}

	/**
	 * @param moCsfbFailure the moCsfbFailure to set
	 */
	public void setMoCsfbFailure(Integer moCsfbFailure) {
		this.moCsfbFailure = moCsfbFailure;
	}

	/**
	 * @return the mtCsfbFailure
	 */
	public Integer getMtCsfbFailure() {
		return mtCsfbFailure;
	}

	/**
	 * @param mtCsfbFailure the mtCsfbFailure to set
	 */
	public void setMtCsfbFailure(Integer mtCsfbFailure) {
		this.mtCsfbFailure = mtCsfbFailure;
	}

	/**
	 * @return the returnCSCallEnd
	 */
	public Integer getReturnCSCallEnd() {
		return returnCSCallEnd;
	}

	/**
	 * @param returnCSCallEnd the returnCSCallEnd to set
	 */
	public void setReturnCSCallEnd(Integer returnCSCallEnd) {
		this.returnCSCallEnd = returnCSCallEnd;
	}

	/**
	 * @return the reselectionSuccess
	 */
	public Integer getReselectionSuccess() {
		return reselectionSuccess;
	}

	/**
	 * @param reselectionSuccess the reselectionSuccess to set
	 */
	public void setReselectionSuccess(Integer reselectionSuccess) {
		this.reselectionSuccess = reselectionSuccess;
	}

	/**
	 * @return the pciChangeStatus
	 */
	public String getPciChangeStatus() {
		return pciChangeStatus;
	}

	/**
	 * @param pciChangeStatus the pciChangeStatus to set
	 */
	public void setPciChangeStatus(String pciChangeStatus) {
		this.pciChangeStatus = pciChangeStatus;
	}

	/**
	 * @return the latencyBufferSize32Bytes
	 */
	public Double[] getLatencyBufferSize32Bytes() {
		return latencyBufferSize32Bytes;
	}

	/**
	 * @param latencyBufferSize32Bytes the latencyBufferSize32Bytes to set
	 */
	public void setLatencyBufferSize32Bytes(Double[] latencyBufferSize32Bytes) {
		this.latencyBufferSize32Bytes = latencyBufferSize32Bytes;
	}

	/**
	 * @return the latencyBufferSize1000Bytes
	 */
	public Double[] getLatencyBufferSize1000Bytes() {
		return latencyBufferSize1000Bytes;
	}

	/**
	 * @param latencyBufferSize1000Bytes the latencyBufferSize1000Bytes to set
	 */
	public void setLatencyBufferSize1000Bytes(Double[] latencyBufferSize1000Bytes) {
		this.latencyBufferSize1000Bytes = latencyBufferSize1000Bytes;
	}

	/**
	 * @return the latencyBufferSize1500Bytes
	 */
	public Double[] getLatencyBufferSize1500Bytes() {
		return latencyBufferSize1500Bytes;
	}

	/**
	 * @param latencyBufferSize1500Bytes the latencyBufferSize1500Bytes to set
	 */
	public void setLatencyBufferSize1500Bytes(Double[] latencyBufferSize1500Bytes) {
		this.latencyBufferSize1500Bytes = latencyBufferSize1500Bytes;
	}

	/**
	 * @return the pingBufferSize
	 */
	public Integer getPingBufferSize() {
		return pingBufferSize;
	}

	/**
	 * @param pingBufferSize the pingBufferSize to set
	 */
	public void setPingBufferSize(Integer pingBufferSize) {
		this.pingBufferSize = pingBufferSize;
	}

	/**
	 * @return the dlModulationType
	 */
	public String getDlModulationType() {
		return dlModulationType;
	}

	/**
	 * @param dlModulationType the dlModulationType to set
	 */
	public void setDlModulationType(String dlModulationType) {
		this.dlModulationType = dlModulationType;
	}

	/**
	 * @return the ulModulationType
	 */
	public String getUlModulationType() {
		return ulModulationType;
	}

	/**
	 * @param ulModulationType the ulModulationType to set
	 */
	public void setUlModulationType(String ulModulationType) {
		this.ulModulationType = ulModulationType;
	}

	/**
	 * @return the b173DlPRB
	 */
	public Double[] getB173DlPRB() {
		return b173DlPRB;
	}

	/**
	 * @param b173DlPRB the b173DlPRB to set
	 */
	public void setB173DlPRB(Double[] b173DlPRB) {
		this.b173DlPRB = b173DlPRB;
	}

	/**
	 * @return the imsRegistrationSetupTime
	 */
	public Double[] getImsRegistrationSetupTime() {
		return imsRegistrationSetupTime;
	}

	/**
	 * @param imsRegistrationSetupTime the imsRegistrationSetupTime to set
	 */
	public void setImsRegistrationSetupTime(Double[] imsRegistrationSetupTime) {
		this.imsRegistrationSetupTime = imsRegistrationSetupTime;
	}

	/**
	 * @return the rrcConnectionReestablishmentRequest
	 */
	public Integer getRrcConnectionReestablishmentRequest() {
		return rrcConnectionReestablishmentRequest;
	}

	/**
	 * @param rrcConnectionReestablishmentRequest the rrcConnectionReestablishmentRequest to set
	 */
	public void setRrcConnectionReestablishmentRequest(Integer rrcConnectionReestablishmentRequest) {
		this.rrcConnectionReestablishmentRequest = rrcConnectionReestablishmentRequest;
	}

	/**
	 * @return the rrcConnectionReestablishmentReject
	 */
	public Integer getRrcConnectionReestablishmentReject() {
		return rrcConnectionReestablishmentReject;
	}

	/**
	 * @param rrcConnectionReestablishmentReject the rrcConnectionReestablishmentReject to set
	 */
	public void setRrcConnectionReestablishmentReject(Integer rrcConnectionReestablishmentReject) {
		this.rrcConnectionReestablishmentReject = rrcConnectionReestablishmentReject;
	}

	/**
	 * @return the rrcConnectionReestablishmentComplete
	 */
	public Integer getRrcConnectionReestablishmentComplete() {
		return rrcConnectionReestablishmentComplete;
	}

	/**
	 * @param rrcConnectionReestablishmentComplete the rrcConnectionReestablishmentComplete to set
	 */
	public void setRrcConnectionReestablishmentComplete(Integer rrcConnectionReestablishmentComplete) {
		this.rrcConnectionReestablishmentComplete = rrcConnectionReestablishmentComplete;
	}

	/**
	 * @return the voltePagingAttempts
	 */
	public Integer getVoltePagingAttempts() {
		return voltePagingAttempts;
	}

	/**
	 * @param voltePagingAttempts the voltePagingAttempts to set
	 */
	public void setVoltePagingAttempts(Integer voltePagingAttempts) {
		this.voltePagingAttempts = voltePagingAttempts;
	}

	/**
	 * @return the voltePagingSuccess
	 */
	public Integer getVoltePagingSuccess() {
		return voltePagingSuccess;
	}

	/**
	 * @param voltePagingSuccess the voltePagingSuccess to set
	 */
	public void setVoltePagingSuccess(Integer voltePagingSuccess) {
		this.voltePagingSuccess = voltePagingSuccess;
	}

	/**
	 * @return the volteMTCallAttempts
	 */
	public Integer getVolteMTCallAttempts() {
		return volteMTCallAttempts;
	}

	/**
	 * @param volteMTCallAttempts the volteMTCallAttempts to set
	 */
	public void setVolteMTCallAttempts(Integer volteMTCallAttempts) {
		this.volteMTCallAttempts = volteMTCallAttempts;
	}

	/**
	 * @return the volteMTCallSuccess
	 */
	public Integer getVolteMTCallSuccess() {
		return volteMTCallSuccess;
	}

	/**
	 * @param volteMTCallSuccess the volteMTCallSuccess to set
	 */
	public void setVolteMTCallSuccess(Integer volteMTCallSuccess) {
		this.volteMTCallSuccess = volteMTCallSuccess;
	}

	/**
	 * @return the volteMOCallAttempts
	 */
	public Integer getVolteMOCallAttempts() {
		return volteMOCallAttempts;
	}

	/**
	 * @param volteMOCallAttempts the volteMOCallAttempts to set
	 */
	public void setVolteMOCallAttempts(Integer volteMOCallAttempts) {
		this.volteMOCallAttempts = volteMOCallAttempts;
	}

	/**
	 * @return the volteMOCallSuccess
	 */
	public Integer getVolteMOCallSuccess() {
		return volteMOCallSuccess;
	}

	/**
	 * @param volteMOCallSuccess the volteMOCallSuccess to set
	 */
	public void setVolteMOCallSuccess(Integer volteMOCallSuccess) {
		this.volteMOCallSuccess = volteMOCallSuccess;
	}

	/**
	 * @return the newHandOverIntiateCount
	 */
	public Integer getNewHandOverIntiateCount() {
		return newHandOverIntiateCount;
	}

	/**
	 * @param newHandOverIntiateCount the newHandOverIntiateCount to set
	 */
	public void setNewHandOverIntiateCount(Integer newHandOverIntiateCount) {
		this.newHandOverIntiateCount = newHandOverIntiateCount;
	}

	/**
	 * @return the newHandOverFailureCount
	 */
	public Integer getNewHandOverFailureCount() {
		return newHandOverFailureCount;
	}

	/**
	 * @param newHandOverFailureCount the newHandOverFailureCount to set
	 */
	public void setNewHandOverFailureCount(Integer newHandOverFailureCount) {
		this.newHandOverFailureCount = newHandOverFailureCount;
	}

	/**
	 * @return the newHandOverSuccessCount
	 */
	public Integer getNewHandOverSuccessCount() {
		return newHandOverSuccessCount;
	}

	/**
	 * @param newHandOverSuccessCount the newHandOverSuccessCount to set
	 */
	public void setNewHandOverSuccessCount(Integer newHandOverSuccessCount) {
		this.newHandOverSuccessCount = newHandOverSuccessCount;
	}

	/**
	 * @return the newHandOverIntiateInterCount
	 */
	public Integer getNewHandOverIntiateInterCount() {
		return newHandOverIntiateInterCount;
	}

	/**
	 * @param newHandOverIntiateInterCount the newHandOverIntiateInterCount to set
	 */
	public void setNewHandOverIntiateInterCount(Integer newHandOverIntiateInterCount) {
		this.newHandOverIntiateInterCount = newHandOverIntiateInterCount;
	}

	/**
	 * @return the newHandOverFailureInterCount
	 */
	public Integer getNewHandOverFailureInterCount() {
		return newHandOverFailureInterCount;
	}

	/**
	 * @param newHandOverFailureInterCount the newHandOverFailureInterCount to set
	 */
	public void setNewHandOverFailureInterCount(Integer newHandOverFailureInterCount) {
		this.newHandOverFailureInterCount = newHandOverFailureInterCount;
	}

	/**
	 * @return the newHandOverSuccessInterCount
	 */
	public Integer getNewHandOverSuccessInterCount() {
		return newHandOverSuccessInterCount;
	}

	/**
	 * @param newHandOverSuccessInterCount the newHandOverSuccessInterCount to set
	 */
	public void setNewHandOverSuccessInterCount(Integer newHandOverSuccessInterCount) {
		this.newHandOverSuccessInterCount = newHandOverSuccessInterCount;
	}

	/**
	 * @return the newHandOverIntiateIntraCount
	 */
	public Integer getNewHandOverIntiateIntraCount() {
		return newHandOverIntiateIntraCount;
	}

	/**
	 * @param newHandOverIntiateIntraCount the newHandOverIntiateIntraCount to set
	 */
	public void setNewHandOverIntiateIntraCount(Integer newHandOverIntiateIntraCount) {
		this.newHandOverIntiateIntraCount = newHandOverIntiateIntraCount;
	}

	/**
	 * @return the newHandOverFailureIntraCount
	 */
	public Integer getNewHandOverFailureIntraCount() {
		return newHandOverFailureIntraCount;
	}

	/**
	 * @param newHandOverFailureIntraCount the newHandOverFailureIntraCount to set
	 */
	public void setNewHandOverFailureIntraCount(Integer newHandOverFailureIntraCount) {
		this.newHandOverFailureIntraCount = newHandOverFailureIntraCount;
	}

	/**
	 * @return the newHandOverSuccessIntraCount
	 */
	public Integer getNewHandOverSuccessIntraCount() {
		return newHandOverSuccessIntraCount;
	}

	/**
	 * @param newHandOverSuccessIntraCount the newHandOverSuccessIntraCount to set
	 */
	public void setNewHandOverSuccessIntraCount(Integer newHandOverSuccessIntraCount) {
		this.newHandOverSuccessIntraCount = newHandOverSuccessIntraCount;
	}

	/**
	 * @return the sourceEarfcn
	 */
	public Integer getSourceEarfcn() {
		return sourceEarfcn;
	}

	/**
	 * @param sourceEarfcn the sourceEarfcn to set
	 */
	public void setSourceEarfcn(Integer sourceEarfcn) {
		this.sourceEarfcn = sourceEarfcn;
	}

	/**
	 * @return the targetEarfcn
	 */
	public Integer getTargetEarfcn() {
		return targetEarfcn;
	}

	/**
	 * @param targetEarfcn the targetEarfcn to set
	 */
	public void setTargetEarfcn(Integer targetEarfcn) {
		this.targetEarfcn = targetEarfcn;
	}

	/**
	 * @return the macDlThroughput
	 */
	public Double[] getMacDlThroughput() {
		return macDlThroughput;
	}

	/**
	 * @param macDlThroughput the macDlThroughput to set
	 */
	public void setMacDlThroughput(Double[] macDlThroughput) {
		this.macDlThroughput = macDlThroughput;
	}

	/**
	 * @return the jitter
	 */
	public Double[] getJitter() {
		return jitter;
	}

	/**
	 * @param jitter the jitter to set
	 */
	public void setJitter(Double[] jitter) {
		this.jitter = jitter;
	}

	/**
	 * @return the packetLossValueAndCount
	 */
	public Double[] getPacketLossValueAndCount() {
		return packetLossValueAndCount;
	}

	/**
	 * @param packetLossValueAndCount the packetLossValueAndCount to set
	 */
	public void setPacketLossValueAndCount(Double[] packetLossValueAndCount) {
		this.packetLossValueAndCount = packetLossValueAndCount;
	}

	/**
	 * @return the dlPRBUtilization
	 */
	public Double[] getDlPRBUtilization() {
		return dlPRBUtilization;
	}

	/**
	 * @param dlPRBUtilization the dlPRBUtilization to set
	 */
	public void setDlPRBUtilization(Double[] dlPRBUtilization) {
		this.dlPRBUtilization = dlPRBUtilization;
	}

	/**
	 * @return the ulPRBUtilization
	 */
	public Double[] getUlPRBUtilization() {
		return ulPRBUtilization;
	}

	/**
	 * @param ulPRBUtilization the ulPRBUtilization to set
	 */
	public void setUlPRBUtilization(Double[] ulPRBUtilization) {
		this.ulPRBUtilization = ulPRBUtilization;
	}

	/**
	 * @return the pMos
	 */
	public Double[] getpMos() {
		return pMos;
	}

	/**
	 * @param pMos the pMos to set
	 */
	public void setpMos(Double[] pMos) {
		this.pMos = pMos;
	}
	
	

}

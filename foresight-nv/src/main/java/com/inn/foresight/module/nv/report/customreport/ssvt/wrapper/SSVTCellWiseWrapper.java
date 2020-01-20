package com.inn.foresight.module.nv.report.customreport.ssvt.wrapper;

public class SSVTCellWiseWrapper {

	private String mnc;
	private String mcc;
	private String tac;
	private String lac;
	private String eurfcn;
	private Integer pci;
	
	private String rsPowerPa;
	private String rsPowerPb;
	
	private Double avgRsrp;
	private Double avgSinr;
	
	private Double setupSuccessRate;
	private Double setupAbnormalRelaseRate;
	private Double setupEstTime;
	
	private Double tcpDl;
	private Integer dlMaxCqi;
	private Integer dlPeakMcs;
	private Double dlPeakRbAllocation;
	
	private Double tcpUl;
	private Integer ulMaxCqi;
	private Integer ulPeakMcs;
	private Double ulPeakRbAllocation;
	
	private String interSite;
	private String intraSite;
	private String fddTdd;
	private String fddTddTo3g2g;
	
	private Double hoInterruptionTime;
	
	private Double lteTo3gTime;
	private Double lteTo2gTime;
	private Double tddToFddTime;
	private  Double fddToTddTime;
	private Double threegToLteTime;
	private Double twogToLteTime;
    private String sibScheduled;
    private String reSelectionPriorites;
    private String reSelectionPrioritesInter;
    private String reSelectionPrioritesGSM;
    private String reSelectionPrioritesWCDMA;


    private Double attachedTime;
    private Double attachedSuccessRate;
    private Double detachedTime;
    private Double detachedSuccessRate;
	
    private String msgFgiBits;
    private Double latency;
    // CSFB KPI
    private Double csfbMoCallSetupTime;
    private Double csfbMtCallSetupTime;
    private Double csfbSuccessRate;
    private Double csfbAbnormalRate;
    private Double csfbDropRate;
    private String isFastReturnLte;
    private String csfbCause;
    // Others Kpi 
    private String isSubServiceRsTime;
    private Integer rachAttemptCount;
    private String isRaLaUpdates;
    private String laUpdateDuringCall;
    private Double tauSuccessRatio;
    private Double fastReturnTime; 
    private String fastReturnTechnolgy;
    private String rrcConfiguration;
    
    //Volte kpi
    private Double volteMoSetupTime;
    private Double volteMtSetupTime;
    private Double srvccSuccessRate;
    private Double volteSetupSuccessRate;
    private Double interhoInterruptionTime;
    private Double intrahoInterruptionTime;
    private Double srvccInterruptionTime;
    private Double oneWayAudioPercent;
    private String isVolteCallConfrence;
    private Double volteAbnormalRelaseRate;
    private Double volteDropRate;
    private String callConferencingRate;
    private String videocallRate;
    private Double rtpPacketLossRate;
    //CA kpis
    private Double sCellPdschThroughput;
    private Double maxSCellPdschThroughput;
    private Double maxPCellPdschThroughput;
    private Double maxPdschThroughput;
    private Double avgDl;
    
	public String getMnc() {
		return mnc;
	}
	public void setMnc(String mnc) {
		this.mnc = mnc;
	}
	public String getMcc() {
		return mcc;
	}
	public void setMcc(String mcc) {
		this.mcc = mcc;
	}
	public String getTac() {
		return tac;
	}
	public void setTac(String tac) {
		this.tac = tac;
	}
	public String getLac() {
		return lac;
	}
	public void setLac(String lac) {
		this.lac = lac;
	}
	public String getEurfcn() {
		return eurfcn;
	}
	public void setEurfcn(String eurfcn) {
		this.eurfcn = eurfcn;
	}
	public Integer getPci() {
		return pci;
	}
	public void setPci(Integer pci) {
		this.pci = pci;
	}
	public String getRsPowerPa() {
		return rsPowerPa;
	}
	public void setRsPowerPa(String rsPowerPa) {
		this.rsPowerPa = rsPowerPa;
	}
	public String getRsPowerPb() {
		return rsPowerPb;
	}
	public void setRsPowerPb(String rsPowerPb) {
		this.rsPowerPb = rsPowerPb;
	}
	public Double getAvgRsrp() {
		return avgRsrp;
	}
	public void setAvgRsrp(Double avgRsrp) {
		this.avgRsrp = avgRsrp;
	}
	public Double getAvgSinr() {
		return avgSinr;
	}
	public void setAvgSinr(Double avgSinr) {
		this.avgSinr = avgSinr;
	}
	public Double getSetupSuccessRate() {
		return setupSuccessRate;
	}
	public void setSetupSuccessRate(Double setupSuccessRate) {
		this.setupSuccessRate = setupSuccessRate;
	}
	public Double getSetupAbnormalRelaseRate() {
		return setupAbnormalRelaseRate;
	}
	public void setSetupAbnormalRelaseRate(Double setupAbnormalRelaseRate) {
		this.setupAbnormalRelaseRate = setupAbnormalRelaseRate;
	}
	public Double getSetupEstTime() {
		return setupEstTime;
	}
	public void setSetupEstTime(Double setupEstTime) {
		this.setupEstTime = setupEstTime;
	}
	public Double getTcpDl() {
		return tcpDl;
	}
	public void setTcpDl(Double tcpDl) {
		this.tcpDl = tcpDl;
	}
	public Integer getDlMaxCqi() {
		return dlMaxCqi;
	}
	public void setDlMaxCqi(Integer dlMaxCqi) {
		this.dlMaxCqi = dlMaxCqi;
	}
	
	public Double getDlPeakRbAllocation() {
		return dlPeakRbAllocation;
	}
	public void setDlPeakRbAllocation(Double dlPeakRbAllocation) {
		this.dlPeakRbAllocation = dlPeakRbAllocation;
	}
	public Double getTcpUl() {
		return tcpUl;
	}
	public void setTcpUl(Double tcpUl) {
		this.tcpUl = tcpUl;
	}
	public Integer getUlMaxCqi() {
		return ulMaxCqi;
	}
	public void setUlMaxCqi(Integer ulMaxCqi) {
		this.ulMaxCqi = ulMaxCqi;
	}
	
	public Integer getDlPeakMcs() {
		return dlPeakMcs;
	}
	public void setDlPeakMcs(Integer dlPeakMcs) {
		this.dlPeakMcs = dlPeakMcs;
	}
	public Integer getUlPeakMcs() {
		return ulPeakMcs;
	}
	public void setUlPeakMcs(Integer ulPeakMcs) {
		this.ulPeakMcs = ulPeakMcs;
	}
	public Double getUlPeakRbAllocation() {
		return ulPeakRbAllocation;
	}
	public void setUlPeakRbAllocation(Double ulPeakRbAllocation) {
		this.ulPeakRbAllocation = ulPeakRbAllocation;
	}
	public String getInterSite() {
		return interSite;
	}
	public void setInterSite(String interSite) {
		this.interSite = interSite;
	}
	public String getIntraSite() {
		return intraSite;
	}
	public void setIntraSite(String intraSite) {
		this.intraSite = intraSite;
	}
	public String getFddTdd() {
		return fddTdd;
	}
	public void setFddTdd(String fddTdd) {
		this.fddTdd = fddTdd;
	}
	public String getFddTddTo3g2g() {
		return fddTddTo3g2g;
	}
	public void setFddTddTo3g2g(String fddTddTo3g2g) {
		this.fddTddTo3g2g = fddTddTo3g2g;
	}
	public Double getHoInterruptionTime() {
		return hoInterruptionTime;
	}
	public void setHoInterruptionTime(Double hoInterruptionTime) {
		this.hoInterruptionTime = hoInterruptionTime;
	}
	public Double getLteTo3gTime() {
		return lteTo3gTime;
	}
	public void setLteTo3gTime(Double lteTo3gTime) {
		this.lteTo3gTime = lteTo3gTime;
	}
	public Double getTddToFddTime() {
		return tddToFddTime;
	}
	public void setTddToFddTime(Double tddToFddTime) {
		this.tddToFddTime = tddToFddTime;
	}
	public Double getFddToTddTime() {
		return fddToTddTime;
	}
	public void setFddToTddTime(Double fddToTddTime) {
		this.fddToTddTime = fddToTddTime;
	}
	public Double getThreegToLteTime() {
		return threegToLteTime;
	}
	public void setThreegToLteTime(Double threegToLteTime) {
		this.threegToLteTime = threegToLteTime;
	}
	public Double getTwogToLteTime() {
		return twogToLteTime;
	}
	public void setTwogToLteTime(Double twogToLteTime) {
		this.twogToLteTime = twogToLteTime;
	}
	public String getSibScheduled() {
		return sibScheduled;
	}
	public void setSibScheduled(String sibScheduled) {
		this.sibScheduled = sibScheduled;
	}
	public String getReSelectionPriorites() {
		return reSelectionPriorites;
	}
	public void setReSelectionPriorites(String reSelectionPriorites) {
		this.reSelectionPriorites = reSelectionPriorites;
	}
	public Double getAttachedTime() {
		return attachedTime;
	}
	public void setAttachedTime(Double attachedTime) {
		this.attachedTime = attachedTime;
	}
	public Double getAttachedSuccessRate() {
		return attachedSuccessRate;
	}
	public void setAttachedSuccessRate(Double attachedSuccessRate) {
		this.attachedSuccessRate = attachedSuccessRate;
	}
	public Double getDetachedTime() {
		return detachedTime;
	}
	public void setDetachedTime(Double detachedTime) {
		this.detachedTime = detachedTime;
	}
	public Double getDetachedSuccessRate() {
		return detachedSuccessRate;
	}
	public void setDetachedSuccessRate(Double detachedSuccessRate) {
		this.detachedSuccessRate = detachedSuccessRate;
	}
	public String getMsgFgiBits() {
		return msgFgiBits;
	}
	public void setMsgFgiBits(String msgFgiBits) {
		this.msgFgiBits = msgFgiBits;
	}
	public Double getLatency() {
		return latency;
	}
	public void setLatency(Double latency) {
		this.latency = latency;
	}
	public Double getCsfbMoCallSetupTime() {
		return csfbMoCallSetupTime;
	}
	public void setCsfbMoCallSetupTime(Double csfbMoCallSetupTime) {
		this.csfbMoCallSetupTime = csfbMoCallSetupTime;
	}
	public Double getCsfbMtCallSetupTime() {
		return csfbMtCallSetupTime;
	}
	public void setCsfbMtCallSetupTime(Double csfbMtCallSetupTime) {
		this.csfbMtCallSetupTime = csfbMtCallSetupTime;
	}
	public Double getCsfbSuccessRate() {
		return csfbSuccessRate;
	}
	public void setCsfbSuccessRate(Double csfbSuccessRate) {
		this.csfbSuccessRate = csfbSuccessRate;
	}
	public Double getCsfbAbnormalRate() {
		return csfbAbnormalRate;
	}
	public void setCsfbAbnormalRate(Double csfbAbnormalRate) {
		this.csfbAbnormalRate = csfbAbnormalRate;
	}
	public Double getCsfbDropRate() {
		return csfbDropRate;
	}
	public void setCsfbDropRate(Double csfbDropRate) {
		this.csfbDropRate = csfbDropRate;
	}
	public String getIsFastReturnLte() {
		return isFastReturnLte;
	}
	public void setIsFastReturnLte(String isFastReturnLte) {
		this.isFastReturnLte = isFastReturnLte;
	}
	public String getIsSubServiceRsTime() {
		return isSubServiceRsTime;
	}
	public void setIsSubServiceRsTime(String isSubServiceRsTime) {
		this.isSubServiceRsTime = isSubServiceRsTime;
	}
	public Integer getRachAttemptCount() {
		return rachAttemptCount;
	}
	public void setRachAttemptCount(Integer rachAttemptCount) {
		this.rachAttemptCount = rachAttemptCount;
	}
	public String getIsRaLaUpdates() {
		return isRaLaUpdates;
	}
	public void setIsRaLaUpdates(String isRaLaUpdates) {
		this.isRaLaUpdates = isRaLaUpdates;
	}
	
	public Double getTauSuccessRatio() {
		return tauSuccessRatio;
	}
	public void setTauSuccessRatio(Double tauSuccessRatio) {
		this.tauSuccessRatio = tauSuccessRatio;
	}
	public Double getFastReturnTime() {
		return fastReturnTime;
	}
	public void setFastReturnTime(Double fastReturnTime) {
		this.fastReturnTime = fastReturnTime;
	}
	public String getFastReturnTechnolgy() {
		return fastReturnTechnolgy;
	}
	public void setFastReturnTechnolgy(String fastReturnTechnolgy) {
		this.fastReturnTechnolgy = fastReturnTechnolgy;
	}
	public Double getVolteMoSetupTime() {
		return volteMoSetupTime;
	}
	public void setVolteMoSetupTime(Double volteMoSetupTime) {
		this.volteMoSetupTime = volteMoSetupTime;
	}
	public Double getVolteMtSetupTime() {
		return volteMtSetupTime;
	}
	public void setVolteMtSetupTime(Double volteMtSetupTime) {
		this.volteMtSetupTime = volteMtSetupTime;
	}
	public Double getSrvccSuccessRate() {
		return srvccSuccessRate;
	}
	public void setSrvccSuccessRate(Double srvccSuccessRate) {
		this.srvccSuccessRate = srvccSuccessRate;
	}
	public Double getVolteSetupSuccessRate() {
		return volteSetupSuccessRate;
	}
	public void setVolteSetupSuccessRate(Double volteSetupSuccessRate) {
		this.volteSetupSuccessRate = volteSetupSuccessRate;
	}
	public Double getInterhoInterruptionTime() {
		return interhoInterruptionTime;
	}
	public void setInterhoInterruptionTime(Double interhoInterruptionTime) {
		this.interhoInterruptionTime = interhoInterruptionTime;
	}
	public Double getIntrahoInterruptionTime() {
		return intrahoInterruptionTime;
	}
	public void setIntrahoInterruptionTime(Double intrahoInterruptionTime) {
		this.intrahoInterruptionTime = intrahoInterruptionTime;
	}
	public Double getSrvccInterruptionTime() {
		return srvccInterruptionTime;
	}
	public void setSrvccInterruptionTime(Double srvccInterruptionTime) {
		this.srvccInterruptionTime = srvccInterruptionTime;
	}
	public Double getOneWayAudioPercent() {
		return oneWayAudioPercent;
	}
	public void setOneWayAudioPercent(Double oneWayAudioPercent) {
		this.oneWayAudioPercent = oneWayAudioPercent;
	}
	public String getIsVolteCallConfrence() {
		return isVolteCallConfrence;
	}
	public void setIsVolteCallConfrence(String isVolteCallConfrence) {
		this.isVolteCallConfrence = isVolteCallConfrence;
	}
	public Double getVolteAbnormalRelaseRate() {
		return volteAbnormalRelaseRate;
	}
	public void setVolteAbnormalRelaseRate(Double volteAbnormalRelaseRate) {
		this.volteAbnormalRelaseRate = volteAbnormalRelaseRate;
	}
	public Double getVolteDropRate() {
		return volteDropRate;
	}
	public void setVolteDropRate(Double volteDropRate) {
		this.volteDropRate = volteDropRate;
	}
	public String getRrcConfiguration() {
		return rrcConfiguration;
	}
	public void setRrcConfiguration(String rrcConfiguration) {
		this.rrcConfiguration = rrcConfiguration;
	}
	public String getCsfbCause() {
		return csfbCause;
	}
	public void setCsfbCause(String csfbCause) {
		this.csfbCause = csfbCause;
	}
	public String getCallConferencingRate() {
		return callConferencingRate;
	}
	public void setCallConferencingRate(String callConferencingRate) {
		this.callConferencingRate = callConferencingRate;
	}
	public String getVideocallRate() {
		return videocallRate;
	}
	public void setVideocallRate(String videocallRate) {
		this.videocallRate = videocallRate;
	}
	public Double getsCellPdschThroughput() {
		return sCellPdschThroughput;
	}
	public void setsCellPdschThroughput(Double sCellPdschThroughput) {
		this.sCellPdschThroughput = sCellPdschThroughput;
	}
	public Double getAvgDl() {
		return avgDl;
	}
	public void setAvgDl(Double avgDl) {
		this.avgDl = avgDl;
	}
	public String getReSelectionPrioritesInter() {
		return reSelectionPrioritesInter;
	}
	public void setReSelectionPrioritesInter(String reSelectionPrioritesInter) {
		this.reSelectionPrioritesInter = reSelectionPrioritesInter;
	}
	public String getReSelectionPrioritesGSM() {
		return reSelectionPrioritesGSM;
	}
	public void setReSelectionPrioritesGSM(String reSelectionPrioritesGSM) {
		this.reSelectionPrioritesGSM = reSelectionPrioritesGSM;
	}
	public String getReSelectionPrioritesWCDMA() {
		return reSelectionPrioritesWCDMA;
	}
	public void setReSelectionPrioritesWCDMA(String reSelectionPrioritesWCDMA) {
		this.reSelectionPrioritesWCDMA = reSelectionPrioritesWCDMA;
	}
	public Double getLteTo2gTime() {
		return lteTo2gTime;
	}
	public void setLteTo2gTime(Double lteTo2gTime) {
		this.lteTo2gTime = lteTo2gTime;
	}
	public Double getRtpPacketLossRate() {
		return rtpPacketLossRate;
	}
	public void setRtpPacketLossRate(Double rtpPacketLossRate) {
		this.rtpPacketLossRate = rtpPacketLossRate;
	}
	public String getLaUpdateDuringCall() {
		return laUpdateDuringCall;
	}
	public void setLaUpdateDuringCall(String laUpdateDuringCall) {
		this.laUpdateDuringCall = laUpdateDuringCall;
	}
	public Double getMaxSCellPdschThroughput() {
		return maxSCellPdschThroughput;
	}
	public void setMaxSCellPdschThroughput(Double maxSCellPdschThroughput) {
		this.maxSCellPdschThroughput = maxSCellPdschThroughput;
	}
	public Double getMaxPCellPdschThroughput() {
		return maxPCellPdschThroughput;
	}
	public void setMaxPCellPdschThroughput(Double maxPCellPdschThroughput) {
		this.maxPCellPdschThroughput = maxPCellPdschThroughput;
	}
	public Double getMaxPdschThroughput() {
		return maxPdschThroughput;
	}
	public void setMaxPdschThroughput(Double maxPdschThroughput) {
		this.maxPdschThroughput = maxPdschThroughput;
	}
}

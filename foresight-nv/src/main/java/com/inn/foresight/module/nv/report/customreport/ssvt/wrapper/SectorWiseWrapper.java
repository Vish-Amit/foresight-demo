package com.inn.foresight.module.nv.report.customreport.ssvt.wrapper;

/**
 * @author ist
 */
public class SectorWiseWrapper {

	String cellId;
	String azimuth;
	String mTilt;
	String eTilt;
	String pci;
	String earfcn;

	String attachSuccessRate;
	String detachSuccessRate;
	String rachSuccessRate;

	String attachLatency;
	String voLTECallSetup;
	String imsRegistration;
	String voLTERTRPktLoss;
	String pagingSucessRate;

	String avgRsrp;
	String avgRsrq;

	String avgDL;
	String avgUL;
	String avgSinr;
	String latency;
	String jitter;

	String peakDL;
	String peakUL;

	Integer rachAttempt;
	Integer rachSucess;
	Integer attachSucess;
	Integer attachRequest;
	Integer detachSuccess;
	Integer detachRequest;
	Integer callSetupAttempt;
	Integer callSetupSuccess;
	Integer mtCallAttempt;
	Integer mtCallSuccess;

	Double avgULSample;
	Double avgDLSample;
	Double avgSinrSample;
	Double avgRsrpSample;
	Double macDlSample;
	Double macUlSample;
	Double avgLatencySample;
	Double avgJitterSample;
	Double attachLatencySample;
	Double imsRegistrationTimeSample;
	Double rtpPacketLossSample;
	Integer overlappingServerSample;
	Double[] stallingCountSample;

	String overlappingServers;
	String avgMacDl;
	String minMacDl;
	String maxMacDl;
	String avgMacUl;
	String minMacUl;
	String maxMacUl;
	String macDlPercentage;
	String macUlPercentage;
	String macDlStatus;
	String macUlStatus;
	String mobilityRsrp;
	String mobilitySinr;
	String callSetupTime;
	String latency32Bytes;
	String latency1000Bytes;
	String latency1500Bytes;
	String pageLoadTime;
	String youtubeStalling;
	String interCallSetupTime;
	String interCallSetupSuccessRate;
	String plmnCallSetupSuccessRate;
	boolean isMobility;
	String overallStatus;

	String rcsVoice;
	String rcsVideo;
	String rcsMessaging;
	
	Double hoInitiateCount;
	Double hoSuccessCount;
	Double hoInterruptionTimeSum;
	Double hoInterruptionTimeCount;
	String hoInterruptionTime;
	String hoSuccessStatus;

	
	public String getHoInterruptionTime() {
		return hoInterruptionTime;
	}

	public void setHoInterruptionTime(String hoInterruptionTime) {
		this.hoInterruptionTime = hoInterruptionTime;
	}

	public String getHoSuccessStatus() {
		return hoSuccessStatus;
	}

	public void setHoSuccessStatus(String hoSuccessStatus) {
		this.hoSuccessStatus = hoSuccessStatus;
	}

	public Double getHoInitiateCount() {
		return hoInitiateCount;
	}

	public void setHoInitiateCount(Double hoInitiateCount) {
		this.hoInitiateCount = hoInitiateCount;
	}

	public Double getHoSuccessCount() {
		return hoSuccessCount;
	}

	public void setHoSuccessCount(Double hoSuccessCount) {
		this.hoSuccessCount = hoSuccessCount;
	}

	public Double getHoInterruptionTimeSum() {
		return hoInterruptionTimeSum;
	}

	public void setHoInterruptionTimeSum(Double hoInterruptionTimeSum) {
		this.hoInterruptionTimeSum = hoInterruptionTimeSum;
	}

	public Double getHoInterruptionTimeCount() {
		return hoInterruptionTimeCount;
	}

	public void setHoInterruptionTimeCount(Double hoInterruptionTimeCount) {
		this.hoInterruptionTimeCount = hoInterruptionTimeCount;
	}

	public String getCellId() {
		return cellId;
	}

	public void setCellId(String cellId) {
		this.cellId = cellId;
	}

	public String getAzimuth() {
		return azimuth;
	}

	public void setAzimuth(String azimuth) {
		this.azimuth = azimuth;
	}

	public String getmTilt() {
		return mTilt;
	}

	public void setmTilt(String mTilt) {
		this.mTilt = mTilt;
	}

	public String geteTilt() {
		return eTilt;
	}

	public void seteTilt(String eTilt) {
		this.eTilt = eTilt;
	}

	public String getPci() {
		return pci;
	}

	public void setPci(String pci) {
		this.pci = pci;
	}

	public String getEarfcn() {
		return earfcn;
	}

	public void setEarfcn(String earfcn) {
		this.earfcn = earfcn;
	}

	public String getAttachSuccessRate() {
		return attachSuccessRate;
	}

	public void setAttachSuccessRate(String attachSucessRate) {
		this.attachSuccessRate = attachSucessRate;
	}

	public String getDetachSuccessRate() {
		return detachSuccessRate;
	}

	public void setDetachSuccessRate(String detachSucessRate) {
		this.detachSuccessRate = detachSucessRate;
	}

	public String getRachSuccessRate() {
		return rachSuccessRate;
	}

	public void setRachSuccessRate(String rachSucessRate) {
		this.rachSuccessRate = rachSucessRate;
	}

	public String getAttachLatency() {
		return attachLatency;
	}

	public void setAttachLatency(String attachLatency) {
		this.attachLatency = attachLatency;
	}

	public String getVoLTECallSetup() {
		return voLTECallSetup;
	}

	public void setVoLTECallSetup(String voLTECallSetup) {
		this.voLTECallSetup = voLTECallSetup;
	}

	public String getImsRegistration() {
		return imsRegistration;
	}

	public void setImsRegistration(String imsRegistration) {
		this.imsRegistration = imsRegistration;
	}

	public String getVoLTERTRPktLoss() {
		return voLTERTRPktLoss;
	}

	public void setVoLTERTRPktLoss(String voLTERTRPktLoss) {
		this.voLTERTRPktLoss = voLTERTRPktLoss;
	}

	public String getPagingSucessRate() {
		return pagingSucessRate;
	}

	public void setPagingSucessRate(String pagingSucessRate) {
		this.pagingSucessRate = pagingSucessRate;
	}

	public String getAvgRsrp() {
		return avgRsrp;
	}

	public void setAvgRsrp(String avgRsrp) {
		this.avgRsrp = avgRsrp;
	}

	public String getAvgRsrq() {
		return avgRsrq;
	}

	public void setAvgRsrq(String avgRsrq) {
		this.avgRsrq = avgRsrq;
	}

	public String getAvgDL() {
		return avgDL;
	}

	public void setAvgDL(String avgDL) {
		this.avgDL = avgDL;
	}

	public String getAvgUL() {
		return avgUL;
	}

	public void setAvgUL(String avgUL) {
		this.avgUL = avgUL;
	}

	public String getAvgSinr() {
		return avgSinr;
	}

	public void setAvgSinr(String avgSinr) {
		this.avgSinr = avgSinr;
	}

	public String getLatency() {
		return latency;
	}

	public void setLatency(String latency) {
		this.latency = latency;
	}

	public String getJitter() {
		return jitter;
	}

	public void setJitter(String jitter) {
		this.jitter = jitter;
	}

	public String getPeakDL() {
		return peakDL;
	}

	public void setPeakDL(String peakDL) {
		this.peakDL = peakDL;
	}

	public String getPeakUL() {
		return peakUL;
	}

	public void setPeakUL(String peakUL) {
		this.peakUL = peakUL;
	}

	public Integer getRachAttempt() {
		return rachAttempt;
	}

	public void setRachAttempt(Integer rachAttemp) {
		this.rachAttempt = rachAttemp;
	}

	public Integer getRachSucess() {
		return rachSucess;
	}

	public void setRachSucess(Integer rachSucess) {
		this.rachSucess = rachSucess;
	}

	public Integer getAttachSucess() {
		return attachSucess;
	}

	public void setAttachSucess(Integer attachSucess) {
		this.attachSucess = attachSucess;
	}

	public Integer getAttachRequest() {
		return attachRequest;
	}

	public void setAttachRequest(Integer attachRequest) {
		this.attachRequest = attachRequest;
	}

	public Double getAvgULSample() {
		return avgULSample;
	}

	public void setAvgULSample(Double avgULSample) {
		this.avgULSample = avgULSample;
	}

	public Double getAvgDLSample() {
		return avgDLSample;
	}

	public void setAvgDLSample(Double avgDLSample) {
		this.avgDLSample = avgDLSample;
	}

	public Double getAvgSinrSample() {
		return avgSinrSample;
	}

	public void setAvgSinrSample(Double avgSinrSample) {
		this.avgSinrSample = avgSinrSample;
	}

	public Double getAvgLatencySample() {
		return avgLatencySample;
	}

	public void setAvgLatencySample(Double avgLatencySample) {
		this.avgLatencySample = avgLatencySample;
	}

	public Double getAvgJitterSample() {
		return avgJitterSample;
	}

	public void setAvgJitterSample(Double avgJitterSample) {
		this.avgJitterSample = avgJitterSample;
	}

	public Integer getDetachSuccess() {
		return detachSuccess;
	}

	public void setDetachSuccess(Integer detachSuccess) {
		this.detachSuccess = detachSuccess;
	}

	public Integer getDetachRequest() {
		return detachRequest;
	}

	public void setDetachRequest(Integer detachRequest) {
		this.detachRequest = detachRequest;
	}

	public Double getAttachLatencySample() {
		return attachLatencySample;
	}

	public void setAttachLatencySample(Double attachLatencySample) {
		this.attachLatencySample = attachLatencySample;
	}

	public Integer getCallSetupAttempt() {
		return callSetupAttempt;
	}

	public void setCallSetupAttempt(Integer callSetupAttempt) {
		this.callSetupAttempt = callSetupAttempt;
	}

	public Integer getCallSetupSuccess() {
		return callSetupSuccess;
	}

	public void setCallSetupSuccess(Integer callSetupSuccess) {
		this.callSetupSuccess = callSetupSuccess;
	}

	public Integer getMtCallAttempt() {
		return mtCallAttempt;
	}

	public void setMtCallAttempt(Integer mtCallAttempt) {
		this.mtCallAttempt = mtCallAttempt;
	}

	public Integer getMtCallSuccess() {
		return mtCallSuccess;
	}

	public void setMtCallSuccess(Integer mtCallSuccess) {
		this.mtCallSuccess = mtCallSuccess;
	}

	public Double getImsRegistrationTimeSample() {
		return imsRegistrationTimeSample;
	}

	public void setImsRegistrationTimeSample(Double imsRegistrationTimeSample) {
		this.imsRegistrationTimeSample = imsRegistrationTimeSample;
	}

	public String getOverlappingServers() {
		return overlappingServers;
	}

	public void setOverlappingServers(String overlappingServers) {
		this.overlappingServers = overlappingServers;
	}

	public String getAvgMacDl() {
		return avgMacDl;
	}

	public void setAvgMacDl(String avgMacDl) {
		this.avgMacDl = avgMacDl;
	}

	public String getMinMacDl() {
		return minMacDl;
	}

	public void setMinMacDl(String minMacDl) {
		this.minMacDl = minMacDl;
	}

	public String getMaxMacDl() {
		return maxMacDl;
	}

	public void setMaxMacDl(String maxMacDl) {
		this.maxMacDl = maxMacDl;
	}

	public String getAvgMacUl() {
		return avgMacUl;
	}

	public void setAvgMacUl(String avgMacUl) {
		this.avgMacUl = avgMacUl;
	}

	public String getMinMacUl() {
		return minMacUl;
	}

	public void setMinMacUl(String minMacUl) {
		this.minMacUl = minMacUl;
	}

	public String getMaxMacUl() {
		return maxMacUl;
	}

	public void setMaxMacUl(String maxMacUl) {
		this.maxMacUl = maxMacUl;
	}

	public String getMacDlPercentage() {
		return macDlPercentage;
	}

	public void setMacDlPercentage(String macDlPercentage) {
		this.macDlPercentage = macDlPercentage;
	}

	public String getMacUlPercentage() {
		return macUlPercentage;
	}

	public void setMacUlPercentage(String macUlPercentage) {
		this.macUlPercentage = macUlPercentage;
	}

	public String getMacDlStatus() {
		return macDlStatus;
	}

	public void setMacDlStatus(String macDlStatus) {
		this.macDlStatus = macDlStatus;
	}

	public String getMacUlStatus() {
		return macUlStatus;
	}

	public void setMacUlStatus(String macUlStatus) {
		this.macUlStatus = macUlStatus;
	}

	public Double getAvgRsrpSample() {
		return avgRsrpSample;
	}

	public void setAvgRsrpSample(Double avgRsrpSample) {
		this.avgRsrpSample = avgRsrpSample;
	}

	public Double getMacDlSample() {
		return macDlSample;
	}

	public void setMacDlSample(Double macDlSample) {
		this.macDlSample = macDlSample;
	}

	public Double getMacUlSample() {
		return macUlSample;
	}

	public void setMacUlSample(Double macUlSample) {
		this.macUlSample = macUlSample;
	}

	public boolean isMobility() {
		return isMobility;
	}

	public void setMobility(boolean mobility) {
		isMobility = mobility;
	}

	public String getMobilityRsrp() {
		return mobilityRsrp;
	}

	public void setMobilityRsrp(String mobilityRsrp) {
		this.mobilityRsrp = mobilityRsrp;
	}

	public String getMobilitySinr() {
		return mobilitySinr;
	}

	public void setMobilitySinr(String mobilitySinr) {
		this.mobilitySinr = mobilitySinr;
	}

	public Integer getOverlappingServerSample() {
		return overlappingServerSample;
	}

	public void setOverlappingServerSample(Integer overlappingServerSample) {
		this.overlappingServerSample = overlappingServerSample;
	}

	public Double getRtpPacketLossSample() {
		return rtpPacketLossSample;
	}

	public void setRtpPacketLossSample(Double rtpPacketLossSample) {
		this.rtpPacketLossSample = rtpPacketLossSample;
	}

	public String getCallSetupTime() {
		return callSetupTime;
	}

	public void setCallSetupTime(String callSetupTime) {
		this.callSetupTime = callSetupTime;
	}

	public String getLatency32Bytes() {
		return latency32Bytes;
	}

	public void setLatency32Bytes(String latency32Bytes) {
		this.latency32Bytes = latency32Bytes;
	}

	public String getLatency1000Bytes() {
		return latency1000Bytes;
	}

	public void setLatency1000Bytes(String latency1000Bytes) {
		this.latency1000Bytes = latency1000Bytes;
	}

	public String getLatency1500Bytes() {
		return latency1500Bytes;
	}

	public void setLatency1500Bytes(String latency1500Bytes) {
		this.latency1500Bytes = latency1500Bytes;
	}

	public String getPageLoadTime() {
		return pageLoadTime;
	}

	public void setPageLoadTime(String pageLoadTime) {
		this.pageLoadTime = pageLoadTime;
	}

	public String getYoutubeStalling() {
		return youtubeStalling;
	}

	public void setYoutubeStalling(String youtubeStalling) {
		this.youtubeStalling = youtubeStalling;
	}

	public Double[] getStallingCountSample() {
		return stallingCountSample;
	}

	public void setStallingCountSample(Double[] stallingCountSample) {
		this.stallingCountSample = stallingCountSample;
	}

	public String getOverallStatus() {
		return overallStatus;
	}

	public void setOverallStatus(String overallStatus) {
		this.overallStatus = overallStatus;
	}

	public String getInterCallSetupTime() {
		return interCallSetupTime;
	}

	public void setInterCallSetupTime(String interCallSetupTime) {
		this.interCallSetupTime = interCallSetupTime;
	}

	public String getInterCallSetupSuccessRate() {
		return interCallSetupSuccessRate;
	}

	public void setInterCallSetupSuccessRate(String interCallSetupSuccessRate) {
		this.interCallSetupSuccessRate = interCallSetupSuccessRate;
	}

	public String getPlmnCallSetupSuccessRate() {
		return plmnCallSetupSuccessRate;
	}

	public void setPlmnCallSetupSuccessRate(String plmnCallSetupSuccessRate) {
		this.plmnCallSetupSuccessRate = plmnCallSetupSuccessRate;
	}

	public String getRcsVoice() {
		return rcsVoice;
	}

	public void setRcsVoice(String rcsVoice) {
		this.rcsVoice = rcsVoice;
	}

	public String getRcsVideo() {
		return rcsVideo;
	}

	public void setRcsVideo(String rcsVideo) {
		this.rcsVideo = rcsVideo;
	}

	public String getRcsMessaging() {
		return rcsMessaging;
	}

	public void setRcsMessaging(String rcsMessaging) {
		this.rcsMessaging = rcsMessaging;
	}
}

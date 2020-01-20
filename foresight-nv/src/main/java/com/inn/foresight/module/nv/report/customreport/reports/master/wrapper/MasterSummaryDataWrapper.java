package com.inn.foresight.module.nv.report.customreport.reports.master.wrapper;

import com.inn.core.generic.wrapper.JpaWrapper;

@JpaWrapper
public class MasterSummaryDataWrapper {

	Integer attachAttempt;
	Integer attachSuccess;
	Double attachSuccessRatio;
	Integer detachAttempt;
	Integer detachSuccess;
	Double detachSuccessRatio;
	Double minRsrp;
	Double maxRsrp;
	Double avgRsrp;
	Double minRsrq;
	Double maxRsrq;
	Double avgRsrq;
	Double minSinr;
	Double maxSinr;
	Double avgSinr;
	Double minDl;
	Double maxDl;
	Double avgDl;
	Double minUl;
	Double maxUl;
	Double avgUl;
	Double minPing32;
	Double maxPing32;
	Double avgPing32;
	Double minPing1000;
	Double maxPing1000;
	Double avgPing1000;
	Double minPing1500;
	Double maxPing1500;
	Double avgPing1500;
	Double minBrowsingLoadTime;
	Double maxBrowsingLoadTime;
	Double avgBrowsingLoadTime;
	Double minBufferingTime;
	Double maxBufferingTime;
	Double avgBufferingTime;
	Integer reselectionAttempt;
	Integer reselectionSuccess;
	Double reselectionSuccessRatio;
	Integer handoverAttempt;
	Integer handoverSuccess;
	Double handoverSuccessRatio;
	Double minInterruptionTime;
	Double maxInterruptionTime;
	Double avgInterruptionTime;
	//Volte KPI's
	Double moSetupSuccessRate;
	Double mtSetupSuccessRate;
	Double iMSRegistration;
	Double volteRTPPktLossRate;
	Double jitter;

	public Integer getAttachAttempt() {
		return attachAttempt;
	}

	public void setAttachAttempt(Integer attachAttempt) {
		this.attachAttempt = attachAttempt;
	}

	public Integer getAttachSuccess() {
		return attachSuccess;
	}

	public void setAttachSuccess(Integer attachSuccess) {
		this.attachSuccess = attachSuccess;
	}

	public Double getAttachSuccessRatio() {
		return attachSuccessRatio;
	}

	public void setAttachSuccessRatio(Double attachSuccessRatio) {
		this.attachSuccessRatio = attachSuccessRatio;
	}

	public Integer getDetachAttempt() {
		return detachAttempt;
	}

	public void setDetachAttempt(Integer detachAttempt) {
		this.detachAttempt = detachAttempt;
	}

	public Integer getDetachSuccess() {
		return detachSuccess;
	}

	public void setDetachSuccess(Integer detachSuccess) {
		this.detachSuccess = detachSuccess;
	}

	public Double getDetachSuccessRatio() {
		return detachSuccessRatio;
	}

	public void setDetachSuccessRatio(Double detachSuccessRatio) {
		this.detachSuccessRatio = detachSuccessRatio;
	}

	public Double getMinRsrp() {
		return minRsrp;
	}

	public void setMinRsrp(Double minRsrp) {
		this.minRsrp = minRsrp;
	}

	public Double getMaxRsrp() {
		return maxRsrp;
	}

	public void setMaxRsrp(Double maxRsrp) {
		this.maxRsrp = maxRsrp;
	}

	public Double getAvgRsrp() {
		return avgRsrp;
	}

	public void setAvgRsrp(Double avgRsrp) {
		this.avgRsrp = avgRsrp;
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

	public Double getAvgRsrq() {
		return avgRsrq;
	}

	public void setAvgRsrq(Double avgRsrq) {
		this.avgRsrq = avgRsrq;
	}

	public Double getMinSinr() {
		return minSinr;
	}

	public void setMinSinr(Double minSinr) {
		this.minSinr = minSinr;
	}

	public Double getMaxSinr() {
		return maxSinr;
	}

	public void setMaxSinr(Double maxSinr) {
		this.maxSinr = maxSinr;
	}

	public Double getAvgSinr() {
		return avgSinr;
	}

	public void setAvgSinr(Double avgSinr) {
		this.avgSinr = avgSinr;
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

	public Double getAvgDl() {
		return avgDl;
	}

	public void setAvgDl(Double avgDl) {
		this.avgDl = avgDl;
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

	public Double getAvgUl() {
		return avgUl;
	}

	public void setAvgUl(Double avgUl) {
		this.avgUl = avgUl;
	}

	public Double getMinPing32() {
		return minPing32;
	}

	public void setMinPing32(Double minPing32) {
		this.minPing32 = minPing32;
	}

	public Double getMaxPing32() {
		return maxPing32;
	}

	public void setMaxPing32(Double maxPing32) {
		this.maxPing32 = maxPing32;
	}

	public Double getAvgPing32() {
		return avgPing32;
	}

	public void setAvgPing32(Double avgPing32) {
		this.avgPing32 = avgPing32;
	}

	public Double getMinPing1000() {
		return minPing1000;
	}

	public void setMinPing1000(Double minPing1000) {
		this.minPing1000 = minPing1000;
	}

	public Double getMaxPing1000() {
		return maxPing1000;
	}

	public void setMaxPing1000(Double maxPing1000) {
		this.maxPing1000 = maxPing1000;
	}

	public Double getAvgPing1000() {
		return avgPing1000;
	}

	public void setAvgPing1000(Double avgPing1000) {
		this.avgPing1000 = avgPing1000;
	}

	public Double getMinPing1500() {
		return minPing1500;
	}

	public void setMinPing1500(Double minPing1500) {
		this.minPing1500 = minPing1500;
	}

	public Double getMaxPing1500() {
		return maxPing1500;
	}

	public void setMaxPing1500(Double maxPing1500) {
		this.maxPing1500 = maxPing1500;
	}

	public Double getAvgPing1500() {
		return avgPing1500;
	}

	public void setAvgPing1500(Double avgPing1500) {
		this.avgPing1500 = avgPing1500;
	}

	public Double getMinBrowsingLoadTime() {
		return minBrowsingLoadTime;
	}

	public void setMinBrowsingLoadTime(Double minBrowsingLoadTime) {
		this.minBrowsingLoadTime = minBrowsingLoadTime;
	}

	public Double getMaxBrowsingLoadTime() {
		return maxBrowsingLoadTime;
	}

	public void setMaxBrowsingLoadTime(Double maxBrowsingLoadTime) {
		this.maxBrowsingLoadTime = maxBrowsingLoadTime;
	}

	public Double getAvgBrowsingLoadTime() {
		return avgBrowsingLoadTime;
	}

	public void setAvgBrowsingLoadTime(Double avgBrowsingLoadTime) {
		this.avgBrowsingLoadTime = avgBrowsingLoadTime;
	}

	public Double getMinBufferingTime() {
		return minBufferingTime;
	}

	public void setMinBufferingTime(Double minBufferingTime) {
		this.minBufferingTime = minBufferingTime;
	}

	public Double getMaxBufferingTime() {
		return maxBufferingTime;
	}

	public void setMaxBufferingTime(Double maxBufferingTime) {
		this.maxBufferingTime = maxBufferingTime;
	}

	public Double getAvgBufferingTime() {
		return avgBufferingTime;
	}

	public void setAvgBufferingTime(Double avgBufferingTime) {
		this.avgBufferingTime = avgBufferingTime;
	}

	public Integer getReselectionAttempt() {
		return reselectionAttempt;
	}

	public void setReselectionAttempt(Integer reselectionAttempt) {
		this.reselectionAttempt = reselectionAttempt;
	}

	public Integer getReselectionSuccess() {
		return reselectionSuccess;
	}

	public void setReselectionSuccess(Integer reselectionSuccess) {
		this.reselectionSuccess = reselectionSuccess;
	}

	public Double getReselectionSuccessRatio() {
		return reselectionSuccessRatio;
	}

	public void setReselectionSuccessRatio(Double reselectionSuccessRatio) {
		this.reselectionSuccessRatio = reselectionSuccessRatio;
	}

	public Integer getHandoverAttempt() {
		return handoverAttempt;
	}

	public void setHandoverAttempt(Integer handoverAttempt) {
		this.handoverAttempt = handoverAttempt;
	}

	public Integer getHandoverSuccess() {
		return handoverSuccess;
	}

	public void setHandoverSuccess(Integer handoverSuccess) {
		this.handoverSuccess = handoverSuccess;
	}

	public Double getHandoverSuccessRatio() {
		return handoverSuccessRatio;
	}

	public void setHandoverSuccessRatio(Double handoverSuccessRatio) {
		this.handoverSuccessRatio = handoverSuccessRatio;
	}

	public Double getMinInterruptionTime() {
		return minInterruptionTime;
	}

	public void setMinInterruptionTime(Double minInterruptionTime) {
		this.minInterruptionTime = minInterruptionTime;
	}

	public Double getMaxInterruptionTime() {
		return maxInterruptionTime;
	}

	public void setMaxInterruptionTime(Double maxInterruptionTime) {
		this.maxInterruptionTime = maxInterruptionTime;
	}

	public Double getAvgInterruptionTime() {
		return avgInterruptionTime;
	}

	public void setAvgInterruptionTime(Double avgInterruptionTime) {
		this.avgInterruptionTime = avgInterruptionTime;
	}

	
	public Double getVolteRTPPktLossRate() {
		return volteRTPPktLossRate;
	}

	
	public void setVolteRTPPktLossRate(Double volteRTPPktLossRate) {
		this.volteRTPPktLossRate = volteRTPPktLossRate;
	}

	
	public Double getJitter() {
		return jitter;
	}

	public void setJitter(Double jitter) {
		this.jitter = jitter;
	}

	/**
	 * @return the moSetupSuccessRate
	 */
	public Double getMoSetupSuccessRate() {
		return moSetupSuccessRate;
	}

	/**
	 * @param moSetupSuccessRate the moSetupSuccessRate to set
	 */
	public void setMoSetupSuccessRate(Double moSetupSuccessRate) {
		this.moSetupSuccessRate = moSetupSuccessRate;
	}

	/**
	 * @return the mtSetupSuccessRate
	 */
	public Double getMtSetupSuccessRate() {
		return mtSetupSuccessRate;
	}

	/**
	 * @param mtSetupSuccessRate the mtSetupSuccessRate to set
	 */
	public void setMtSetupSuccessRate(Double mtSetupSuccessRate) {
		this.mtSetupSuccessRate = mtSetupSuccessRate;
	}

	/**
	 * @return the iMSRegistration
	 */
	public Double getiMSRegistration() {
		return iMSRegistration;
	}

	/**
	 * @param iMSRegistration the iMSRegistration to set
	 */
	public void setiMSRegistration(Double iMSRegistration) {
		this.iMSRegistration = iMSRegistration;
	}

	
	@Override
	public String toString() {
		return "MasterSummaryDataWrapper [attachAttempt=" + attachAttempt + ", attachSuccess=" + attachSuccess
				+ ", attachSuccessRatio=" + attachSuccessRatio + ", detachAttempt=" + detachAttempt + ", detachSuccess="
				+ detachSuccess + ", detachSuccessRatio=" + detachSuccessRatio + ", minRsrp=" + minRsrp + ", maxRsrp="
				+ maxRsrp + ", avgRsrp=" + avgRsrp + ", minRsrq=" + minRsrq + ", maxRsrq=" + maxRsrq + ", avgRsrq="
				+ avgRsrq + ", minSinr=" + minSinr + ", maxSinr=" + maxSinr + ", avgSinr=" + avgSinr + ", minDl="
				+ minDl + ", maxDl=" + maxDl + ", avgDl=" + avgDl + ", minUl=" + minUl + ", maxUl=" + maxUl + ", avgUl="
				+ avgUl + ", minPing32=" + minPing32 + ", maxPing32=" + maxPing32 + ", avgPing32=" + avgPing32
				+ ", minPing1000=" + minPing1000 + ", maxPing1000=" + maxPing1000 + ", avgPing1000=" + avgPing1000
				+ ", minPing1500=" + minPing1500 + ", maxPing1500=" + maxPing1500 + ", avgPing1500=" + avgPing1500
				+ ", minBrowsingLoadTime=" + minBrowsingLoadTime + ", maxBrowsingLoadTime=" + maxBrowsingLoadTime
				+ ", avgBrowsingLoadTime=" + avgBrowsingLoadTime + ", minBufferingTime=" + minBufferingTime
				+ ", maxBufferingTime=" + maxBufferingTime + ", avgBufferingTime=" + avgBufferingTime
				+ ", reselectionAttempt=" + reselectionAttempt + ", reselectionSuccess=" + reselectionSuccess
				+ ", reselectionSuccessRatio=" + reselectionSuccessRatio + ", handoverAttempt=" + handoverAttempt
				+ ", handoverSuccess=" + handoverSuccess + ", handoverSuccessRatio=" + handoverSuccessRatio
				+ ", minInterruptionTime=" + minInterruptionTime + ", maxInterruptionTime=" + maxInterruptionTime
				+ ", avgInterruptionTime=" + avgInterruptionTime + ", moSetupSuccessRate=" + moSetupSuccessRate
				+ ", mtSetupSuccessRate=" + mtSetupSuccessRate + ", iMSRegistration=" + iMSRegistration
				+ ", volteRTPPktLossRate=" + volteRTPPktLossRate + ", jitter=" + jitter + "]";
	}


}

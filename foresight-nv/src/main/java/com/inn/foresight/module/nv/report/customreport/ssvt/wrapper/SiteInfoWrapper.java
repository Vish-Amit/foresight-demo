package com.inn.foresight.module.nv.report.customreport.ssvt.wrapper;

import java.util.List;

public class SiteInfoWrapper {

	String siteId;

	String siteName;

	String lat;

	String lng;

	String mcc;

	String mnc;

	String tac;

	String tal;

	String voLteHandover;

	String dlFtpHandover;

	List<SectorWiseWrapper> alpha;

	List<SectorWiseWrapper> beta;

	List<SectorWiseWrapper> gamma;

	List<SectorWiseWrapper> modelAlpha;

	List<SectorWiseWrapper> modelBeta;

	List<SectorWiseWrapper> modelGamma;

	String handoverSuccessRate;

	String handoverInterruptionTime;

	String bandwidth;

	String overallStatus;

	Integer cellId;

	public List<SectorWiseWrapper> getModelAlpha() {
		return modelAlpha;
	}

	public void setModelAlpha(List<SectorWiseWrapper> modelAlpha) {
		this.modelAlpha = modelAlpha;
	}

	public List<SectorWiseWrapper> getModelBeta() {
		return modelBeta;
	}

	public String getVoLteHandover() {
		return voLteHandover;
	}

	public void setVoLteHandover(String voLteHandover) {
		this.voLteHandover = voLteHandover;
	}

	public String getDlFtpHandover() {
		return dlFtpHandover;
	}

	public void setDlFtpHandover(String dlFtpHandover) {
		this.dlFtpHandover = dlFtpHandover;
	}

	public void setModelBeta(List<SectorWiseWrapper> modelBeta) {
		this.modelBeta = modelBeta;
	}

	public List<SectorWiseWrapper> getModelGamma() {
		return modelGamma;
	}

	public void setModelGamma(List<SectorWiseWrapper> modelGamma) {
		this.modelGamma = modelGamma;
	}

	public List<SectorWiseWrapper> getAlpha() {
		return alpha;
	}

	public void setAlpha(List<SectorWiseWrapper> alpha) {
		this.alpha = alpha;
	}

	public List<SectorWiseWrapper> getBeta() {
		return beta;
	}

	public void setBeta(List<SectorWiseWrapper> beta) {
		this.beta = beta;
	}

	public List<SectorWiseWrapper> getGamma() {
		return gamma;
	}

	public void setGamma(List<SectorWiseWrapper> gamma) {
		this.gamma = gamma;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getMcc() {
		return mcc;
	}

	public void setMcc(String mcc) {
		this.mcc = mcc;
	}

	public String getMnc() {
		return mnc;
	}

	public void setMnc(String mnc) {
		this.mnc = mnc;
	}

	public String getTac() {
		return tac;
	}

	public void setTac(String tac) {
		this.tac = tac;
	}

	public String getTal() {
		return tal;
	}

	public void setTal(String tal) {
		this.tal = tal;
	}

	public String getHandoverSuccessRate() {
		return handoverSuccessRate;
	}

	public void setHandoverSuccessRate(String handoverSuccessRate) {
		this.handoverSuccessRate = handoverSuccessRate;
	}

	public String getHandoverInterruptionTime() {
		return handoverInterruptionTime;
	}

	public void setHandoverInterruptionTime(String handoverInterruptionTime) {
		this.handoverInterruptionTime = handoverInterruptionTime;
	}

	public String getBandwidth() {
		return bandwidth;
	}

	public void setBandwidth(String bandwidth) {
		this.bandwidth = bandwidth;
	}

	public String getOverallStatus() {
		return overallStatus;
	}

	public void setOverallStatus(String overallStatus) {
		this.overallStatus = overallStatus;
	}

	public Integer getCellId() {
		return cellId;
	}

	public void setCellId(Integer cellId) {
		this.cellId = cellId;
	}	
}

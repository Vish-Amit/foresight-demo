package com.inn.foresight.module.nv.report.wrapper;

import java.util.List;

public class SSVTSiteInfoWrapper {

	String siteId;

	String siteName;

	String lat;

	String lng;

	String mcc;

	String mnc;

	String tac;

	String tal;

	List<SSVTSectorWiseWrapper> alpha;

	List<SSVTSectorWiseWrapper> beta;

	List<SSVTSectorWiseWrapper> gamma;

	public List<SSVTSectorWiseWrapper> getAlpha() {
		return alpha;
	}

	public void setAlpha(List<SSVTSectorWiseWrapper> alpha) {
		this.alpha = alpha;
	}

	public List<SSVTSectorWiseWrapper> getBeta() {
		return beta;
	}

	public void setBeta(List<SSVTSectorWiseWrapper> beta) {
		this.beta = beta;
	}

	public List<SSVTSectorWiseWrapper> getGamma() {
		return gamma;
	}

	public void setGamma(List<SSVTSectorWiseWrapper> gamma) {
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

}

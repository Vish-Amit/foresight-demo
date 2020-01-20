package com.inn.foresight.module.nv.feedback.wrapper;

import java.io.Serializable;

public class CustomFeedbackRequestWrapper implements Serializable {

	private static final long serialVersionUID = 1L;

	private Double sWLat;
	private Double sWLng;
	private Double nELat;
	private Double nELng;
	private String toDate;
	private String fromDate;
	private Integer zoomLevel;
	private String band;
	private String kpi;
	private String locationType;  // comma seperated values
	private String searchType;    // comma seperated values
	private String orderBy;
	private String orderType;

	public Double getsWLat() {
		return sWLat;
	}

	public void setsWLat(Double sWLat) {
		this.sWLat = sWLat;
	}

	public Double getsWLng() {
		return sWLng;
	}

	public void setsWLng(Double sWLng) {
		this.sWLng = sWLng;
	}

	public Double getnELat() {
		return nELat;
	}

	public void setnELat(Double nELat) {
		this.nELat = nELat;
	}

	public Double getnELng() {
		return nELng;
	}

	public void setnELng(Double nELng) {
		this.nELng = nELng;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public Integer getZoomLevel() {
		return zoomLevel;
	}

	public void setZoomLevel(Integer zoomLevel) {
		this.zoomLevel = zoomLevel;
	}

	public String getBand() {
		return band;
	}

	public void setBand(String band) {
		this.band = band;
	}

	public String getKpi() {
		return kpi;
	}

	public void setKpi(String kpi) {
		this.kpi = kpi;
	}

	public String getLocationType() {
		return locationType;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	@Override
	public String toString() {
		return "CustomFeedbackRequestWrapper [sWLat=" + sWLat + ", sWLng=" + sWLng + ", nELat=" + nELat + ", nELng=" + nELng + ", toDate=" + toDate
				+ ", fromDate=" + fromDate + ", zoomLevel=" + zoomLevel + ", band=" + band + ", kpi=" + kpi + ", locationType=" + locationType
				+ ", searchType=" + searchType + ", orderBy=" + orderBy + ", orderType=" + orderType + "]";
	}

}

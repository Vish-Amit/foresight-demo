package com.inn.foresight.module.nv.report.wrapper.kpicomparison;

public class KPIComparisonDataWrapper {

	private Double lat;
	private Double lon;
	private String kpiTitle;
	private Double kpiValue;
	private String kpiRange;
	private Double delta;
	private Double predictionKpi;
	private Double dtKpi;

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLon() {
		return lon;
	}

	public void setLon(Double lon) {
		this.lon = lon;
	}

	public String getKpiTitle() {
		return kpiTitle;
	}

	public void setKpiTitle(String kpiTitle) {
		this.kpiTitle = kpiTitle;
	}

	public Double getKpiValue() {
		return kpiValue;
	}

	public void setKpiValue(Double kpiValue) {
		this.kpiValue = kpiValue;
	}

	public String getKpiRange() {
		return kpiRange;
	}

	public void setKpiRange(String kpiRange) {
		this.kpiRange = kpiRange;
	}

	public Double getDtKpi() {
		return dtKpi;
	}

	public void setDtKpi(Double dtKpi) {
		this.dtKpi = dtKpi;
	}

	public Double getDelta() {
		return delta;
	}

	public void setDelta(Double delta) {
		this.delta = delta;
	}

	public Double getPredictionKpi() {
		return predictionKpi;
	}

	public void setPredictionKpi(Double predictionKpi) {
		this.predictionKpi = predictionKpi;
	}

	@Override public String toString() {
		return "KPIComparisonDataWrapper{" + "lat=" + lat + ", lon=" + lon + ", kpiTitle='" + kpiTitle + '\''
				+ ", kpiValue=" + kpiValue + ", kpiRange=" + kpiRange + ", delta=" + delta + ", predictionKpi="
				+ predictionKpi + ", dtKpi=" + dtKpi + '}';
	}
}

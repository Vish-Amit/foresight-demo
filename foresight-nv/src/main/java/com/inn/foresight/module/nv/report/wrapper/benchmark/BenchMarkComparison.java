package com.inn.foresight.module.nv.report.wrapper.benchmark;

import com.inn.core.generic.wrapper.JpaWrapper;

@JpaWrapper
public class BenchMarkComparison {
	private String operatorName;
	private String avgRsrp;
	private String avgSinr;
	private String avgDl;
	private String avgUl;
	private String avgMimo;
	private String avgCqi;
	private String avgMos;
	private String voiceKpi;
	private String voiceValue;
	private Double rsrpPercent;
	private Double sinrPercent;
	private Double dlPercent;
	private Double ulPercent;
	private Double mosPercent;
	private Double voiceAccessPercent;
	private Double voiceRetainPercent;
	private String operatorImage;

	public String getVoiceKpi() {
		return voiceKpi;
	}

	public void setVoiceKpi(String voiceKpi) {
		this.voiceKpi = voiceKpi;
	}

	public String getVoiceValue() {
		return voiceValue;
	}

	public void setVoiceValue(String voiceValue) {
		this.voiceValue = voiceValue;
	}

	public String getAvgMimo() {
		return avgMimo;
	}

	public void setAvgMimo(String avgMimo) {
		this.avgMimo = avgMimo;
	}

	public String getAvgCqi() {
		return avgCqi;
	}

	public void setAvgCqi(String avgCqi) {
		this.avgCqi = avgCqi;
	}

	public String getAvgMos() {
		return avgMos;
	}

	public void setAvgMos(String avgMos) {
		this.avgMos = avgMos;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getAvgRsrp() {
		return avgRsrp;
	}

	public void setAvgRsrp(String avgRsrp) {
		this.avgRsrp = avgRsrp;
	}

	public String getAvgSinr() {
		return avgSinr;
	}

	public void setAvgSinr(String avgSinr) {
		this.avgSinr = avgSinr;
	}

	public String getAvgDl() {
		return avgDl;
	}

	public void setAvgDl(String avgDl) {
		this.avgDl = avgDl;
	}

	public String getAvgUl() {
		return avgUl;
	}

	public void setAvgUl(String avgUl) {
		this.avgUl = avgUl;
	}

	public String getOperatorImage() {
		return operatorImage;
	}

	public void setOperatorImage(String operatorImage) {
		this.operatorImage = operatorImage;
	}

	public Double getRsrpPercent() {
		return rsrpPercent;
	}

	public void setRsrpPercent(Double rsrpPercent) {
		this.rsrpPercent = rsrpPercent;
	}

	public Double getSinrPercent() {
		return sinrPercent;
	}

	public void setSinrPercent(Double sinrPercent) {
		this.sinrPercent = sinrPercent;
	}

	public Double getDlPercent() {
		return dlPercent;
	}

	public void setDlPercent(Double dlPercent) {
		this.dlPercent = dlPercent;
	}

	public Double getUlPercent() {
		return ulPercent;
	}

	public void setUlPercent(Double ulPercent) {
		this.ulPercent = ulPercent;
	}

	public Double getVoiceAccessPercent() {
		return voiceAccessPercent;
	}

	public void setVoiceAccessPercent(Double voiceAccessPercent) {
		this.voiceAccessPercent = voiceAccessPercent;
	}

	public Double getVoiceRetainPercent() {
		return voiceRetainPercent;
	}

	public void setVoiceRetainPercent(Double voiceRetainPercent) {
		this.voiceRetainPercent = voiceRetainPercent;
	}

	public Double getMosPercent() {
		return mosPercent;
	}

	public void setMosPercent(Double mosPercent) {
		this.mosPercent = mosPercent;
	}

	@Override
	public String toString() {
		return "BenchMarkComparison [operatorName=" + operatorName + ", avgRsrp=" + avgRsrp + ", avgSinr=" + avgSinr
				+ ", avgDl=" + avgDl + ", avgUl=" + avgUl + ", avgMimo=" + avgMimo + ", avgCqi=" + avgCqi + ", avgMos="
				+ avgMos + ", voiceKpi=" + voiceKpi + ", voiceValue=" + voiceValue + ", rsrpPercent=" + rsrpPercent
				+ ", sinrPercent=" + sinrPercent + ", dlPercent=" + dlPercent + ", ulPercent=" + ulPercent
				+ ", mosPercent=" + mosPercent + ", voiceAccessPercent=" + voiceAccessPercent + ", voiceRetainPercent="
				+ voiceRetainPercent + ", operatorImage=" + operatorImage + "]";
	}

}

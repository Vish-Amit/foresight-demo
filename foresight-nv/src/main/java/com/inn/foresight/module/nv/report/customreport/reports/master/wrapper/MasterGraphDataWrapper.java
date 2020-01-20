package com.inn.foresight.module.nv.report.customreport.reports.master.wrapper;

import com.inn.core.generic.wrapper.JpaWrapper;

@JpaWrapper
public class MasterGraphDataWrapper {

	String from;
	String to;
	String kpiName;
	Double alphaValue;
	Double betaValue;
	Double gammaValue;
	String alphaLabel;
	String betaLabel;
	String gammaLabel;

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getKpiName() {
		return kpiName;
	}

	public void setKpiName(String kpiName) {
		this.kpiName = kpiName;
	}

	public Double getAlphaValue() {
		return alphaValue;
	}

	public void setAlphaValue(Double alphaValue) {
		this.alphaValue = alphaValue;
	}

	public Double getBetaValue() {
		return betaValue;
	}

	public void setBetaValue(Double betaValue) {
		this.betaValue = betaValue;
	}

	public Double getGammaValue() {
		return gammaValue;
	}

	public void setGammaValue(Double gammaValue) {
		this.gammaValue = gammaValue;
	}

	public String getAlphaLabel() {
		return alphaLabel;
	}

	public void setAlphaLabel(String alphaLabel) {
		this.alphaLabel = alphaLabel;
	}

	public String getBetaLabel() {
		return betaLabel;
	}

	public void setBetaLabel(String betaLabel) {
		this.betaLabel = betaLabel;
	}

	public String getGammaLabel() {
		return gammaLabel;
	}

	public void setGammaLabel(String gammaLabel) {
		this.gammaLabel = gammaLabel;
	}

	@Override public String toString() {
		return "MasterGraphDataWrapper{" + "from='" + from + '\'' + ", to='" + to + '\'' + ", kpiName='" + kpiName
				+ '\'' + ", alphaValue=" + alphaValue + ", betaValue=" + betaValue + ", gammaValue=" + gammaValue
				+ ", alphaLabel='" + alphaLabel + '\'' + ", betaLabel='" + betaLabel + '\'' + ", gammaLabel='"
				+ gammaLabel + '\'' + '}';
	}
}

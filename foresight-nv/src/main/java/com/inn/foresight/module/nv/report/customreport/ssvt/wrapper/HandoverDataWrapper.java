package com.inn.foresight.module.nv.report.customreport.ssvt.wrapper;

import java.io.InputStream;
import java.util.List;

import com.inn.core.generic.wrapper.JpaWrapper;

@JpaWrapper
public class HandoverDataWrapper {

	String handoverPlot;
	InputStream hoInitateLegend;
	InputStream hoSuccessLegend;
	InputStream hoFailureLegend;
	String hoInitate;
	String hoSuccess;
	String hoFailure;
	String hoSuccessRate;
	String hoFailureRate;

	List<HOItemWrapper> hoAnalysisList;
	List<HOItemWrapper> hoCriteriaList;

	public String getHandoverPlot() {
		return handoverPlot;
	}

	public void setHandoverPlot(String handoverPlot) {
		this.handoverPlot = handoverPlot;
	}

	public InputStream getHoInitateLegend() {
		return hoInitateLegend;
	}

	public void setHoInitateLegend(InputStream hoInitateLegend) {
		this.hoInitateLegend = hoInitateLegend;
	}

	public InputStream getHoSuccessLegend() {
		return hoSuccessLegend;
	}

	public void setHoSuccessLegend(InputStream hoSuccessLegend) {
		this.hoSuccessLegend = hoSuccessLegend;
	}

	public InputStream getHoFailureLegend() {
		return hoFailureLegend;
	}

	public void setHoFailureLegend(InputStream hoFailureLegend) {
		this.hoFailureLegend = hoFailureLegend;
	}

	public String getHoInitate() {
		return hoInitate;
	}

	public void setHoInitate(String hoInitate) {
		this.hoInitate = hoInitate;
	}

	public String getHoSuccess() {
		return hoSuccess;
	}

	public void setHoSuccess(String hoSuccess) {
		this.hoSuccess = hoSuccess;
	}

	public String getHoFailure() {
		return hoFailure;
	}

	public void setHoFailure(String hoFailure) {
		this.hoFailure = hoFailure;
	}

	public String getHoSuccessRate() {
		return hoSuccessRate;
	}

	public void setHoSuccessRate(String hoSuccessRate) {
		this.hoSuccessRate = hoSuccessRate;
	}

	public String getHoFailureRate() {
		return hoFailureRate;
	}

	public void setHoFailureRate(String hoFailureRate) {
		this.hoFailureRate = hoFailureRate;
	}

	public List<HOItemWrapper> getHoAnalysisList() {
		return hoAnalysisList;
	}

	public void setHoAnalysisList(List<HOItemWrapper> hoAnalysisList) {
		this.hoAnalysisList = hoAnalysisList;
	}

	public List<HOItemWrapper> getHoCriteriaList() {
		return hoCriteriaList;
	}

	public void setHoCriteriaList(List<HOItemWrapper> hoCriteriaList) {
		this.hoCriteriaList = hoCriteriaList;
	}

	

}

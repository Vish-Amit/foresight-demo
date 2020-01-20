package com.inn.foresight.module.nv.report.customreport.ssvt.wrapper;

import java.io.InputStream;
import java.util.List;

public class CallAnalysisDataWrapper {

	String callInitiated;
	String callSuccess;
	String callFailed;
	String callDropped;
	InputStream initiateLegend;
	InputStream droppedLegend;
	InputStream failedLegend;
	InputStream successLegend;
	String callPlot;

	List<CallAnalysisItemWrapper> callDataList;

	public String getCallInitiated() {
		return callInitiated;
	}

	public void setCallInitiated(String callInitiated) {
		this.callInitiated = callInitiated;
	}

	public String getCallSuccess() {
		return callSuccess;
	}

	public void setCallSuccess(String callSuccess) {
		this.callSuccess = callSuccess;
	}

	public String getCallFailed() {
		return callFailed;
	}

	public void setCallFailed(String callFailed) {
		this.callFailed = callFailed;
	}

	public String getCallDropped() {
		return callDropped;
	}

	public void setCallDropped(String callDropped) {
		this.callDropped = callDropped;
	}

	public InputStream getInitiateLegend() {
		return initiateLegend;
	}

	public void setInitiateLegend(InputStream initiateLegend) {
		this.initiateLegend = initiateLegend;
	}

	public InputStream getDroppedLegend() {
		return droppedLegend;
	}

	public void setDroppedLegend(InputStream droppedLegend) {
		this.droppedLegend = droppedLegend;
	}

	public InputStream getFailedLegend() {
		return failedLegend;
	}

	public void setFailedLegend(InputStream failedLegend) {
		this.failedLegend = failedLegend;
	}

	public InputStream getSuccessLegend() {
		return successLegend;
	}

	public void setSuccessLegend(InputStream successLegend) {
		this.successLegend = successLegend;
	}

	public String getCallPlot() {
		return callPlot;
	}

	public void setCallPlot(String callPlot) {
		this.callPlot = callPlot;
	}

	public List<CallAnalysisItemWrapper> getCallDataList() {
		return callDataList;
	}

	public void setCallDataList(List<CallAnalysisItemWrapper> callDataList) {
		this.callDataList = callDataList;
	}

	

}

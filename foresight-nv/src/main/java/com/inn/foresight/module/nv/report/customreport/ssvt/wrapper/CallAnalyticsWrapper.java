package com.inn.foresight.module.nv.report.customreport.ssvt.wrapper;

import java.util.List;

public class CallAnalyticsWrapper {
	
	String callInitiated;
	String callSuccess;
	String callFailed;
	String callDropped;
	String initiateLegend;
	String droppedLegend;
	String failedLegend;
	String successLegend;
	String callPlot;
	
	List<CallDataWrapper> callDataList;

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

	public String getInitiateLegend() {
		return initiateLegend;
	}

	public void setInitiateLegend(String initiateLegend) {
		this.initiateLegend = initiateLegend;
	}

	public String getDroppedLegend() {
		return droppedLegend;
	}

	public void setDroppedLegend(String droppedLegend) {
		this.droppedLegend = droppedLegend;
	}

	public String getFailedLegend() {
		return failedLegend;
	}

	public void setFailedLegend(String failedLegend) {
		this.failedLegend = failedLegend;
	}

	public String getSuccessLegend() {
		return successLegend;
	}

	public void setSuccessLegend(String successLegend) {
		this.successLegend = successLegend;
	}

	public String getCallPlot() {
		return callPlot;
	}

	public void setCallPlot(String callPlot) {
		this.callPlot = callPlot;
	}

	public List<CallDataWrapper> getCallDataList() {
		return callDataList;
	}

	public void setCallDataList(List<CallDataWrapper> callDataList) {
		this.callDataList = callDataList;
	}

	@Override
	public String toString() {
		return "CallAnalyticsWrapper [callInitiated=" + callInitiated + ", callSuccess=" + callSuccess + ", callFailed="
				+ callFailed + ", callDropped=" + callDropped + ", initiateLegend=" + initiateLegend
				+ ", droppedLegend=" + droppedLegend + ", failedLegend=" + failedLegend + ", successLegend="
				+ successLegend + ", callPlot=" + callPlot + ", callDataList=" + callDataList + "]";
	}
}

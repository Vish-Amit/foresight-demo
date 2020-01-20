package com.inn.foresight.module.nv.report.wrapper.stealth;

public class StealthDateWiseStatusWrapper {

	String date;
	Integer success;
	Integer failure;
	Integer closed;
	Integer inProgress;
	Double successPercent;
	Double failurePercent;
	Double inProgressPercent;
	Double closedPercent;

	public Integer getInProgress() {
		return inProgress;
	}

	public void setInProgress(Integer inProgress) {
		this.inProgress = inProgress;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Integer getSuccess() {
		return success;
	}

	public void setSuccess(Integer success) {
		this.success = success;
	}

	public Integer getFailure() {
		return failure;
	}

	public void setFailure(Integer failure) {
		this.failure = failure;
	}

	public Integer getClosed() {
		return closed;
	}

	public void setClosed(Integer closed) {
		this.closed = closed;
	}

	public Double getSuccessPercent() {
		return successPercent;
	}

	public void setSuccessPercent(Double successPercent) {
		this.successPercent = successPercent;
	}

	public Double getFailurePercent() {
		return failurePercent;
	}

	public void setFailurePercent(Double failurePercent) {
		this.failurePercent = failurePercent;
	}

	public Double getInProgressPercent() {
		return inProgressPercent;
	}

	public void setInProgressPercent(Double inProgressPercent) {
		this.inProgressPercent = inProgressPercent;
	}

	public Double getClosedPercent() {
		return closedPercent;
	}

	public void setClosedPercent(Double closedPercent) {
		this.closedPercent = closedPercent;
	}

	@Override
	public String toString() {
		return "StealthDateWiseStatusWrapper [date=" + date + ", success=" + success + ", failure=" + failure
				+ ", closed=" + closed + ", inProgress=" + inProgress + ", successPercent=" + successPercent
				+ ", failurePercent=" + failurePercent + ", inProgressPercent=" + inProgressPercent + ", closedPercent="
				+ closedPercent + "]";
	}

}

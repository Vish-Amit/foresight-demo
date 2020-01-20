package com.inn.foresight.module.nv.report.wrapper;

import java.util.List;

public class SectorSwapWrapper {

	private String siteId;
	private String sectorId;
	private String expectedPci;
	private String measuredPci;
	private String status;
	private List<MessageKpiWrapper> messageList;

	public String getSiteId() {
		return siteId;
	}
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	public String getSectorId() {
		return sectorId;
	}
	public void setSectorId(String sectorId) {
		this.sectorId = sectorId;
	}
	public String getExpectedPci() {
		return expectedPci;
	}
	public void setExpectedPci(String expectedPci) {
		this.expectedPci = expectedPci;
	}
	public String getMeasuredPci() {
		return measuredPci;
	}
	public void setMeasuredPci(String measuredPci) {
		this.measuredPci = measuredPci;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<MessageKpiWrapper> getMessageList() {
		return messageList;
	}
	public void setMessageList(List<MessageKpiWrapper> messageList) {
		this.messageList = messageList;
	}
	@Override
	public String toString() {
		return "SectorSwapWrapper [siteId=" + siteId + ", sectorId=" + sectorId + ", expectedPci=" + expectedPci
				+ ", measuredPci=" + measuredPci + ", status=" + status + ", messageList=" + messageList + "]";
	}
	
	
	
}

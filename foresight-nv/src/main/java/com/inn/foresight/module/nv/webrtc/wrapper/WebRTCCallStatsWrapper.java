package com.inn.foresight.module.nv.webrtc.wrapper;

import java.util.List;

import com.inn.core.generic.wrapper.RestWrapper;

@RestWrapper
public class WebRTCCallStatsWrapper {
private List<WebRTCCallDataWrapper>senderDataList;
private List<WebRTCCallDataWrapper>reciverDataList;
private WebRTCCallDataWrapper senderSummary;
private WebRTCCallDataWrapper reciverSummary;
private List<List<String>>senderLatLongList;
private List<List<String>>reciverLatLongList;
public List<WebRTCCallDataWrapper> getSenderDataList() {
	return senderDataList;
}
public void setSenderDataList(List<WebRTCCallDataWrapper> senderDataList) {
	this.senderDataList = senderDataList;
}
public List<WebRTCCallDataWrapper> getReciverDataList() {
	return reciverDataList;
}
public void setReciverDataList(List<WebRTCCallDataWrapper> reciverDataList) {
	this.reciverDataList = reciverDataList;
}
public WebRTCCallDataWrapper getSenderSummary() {
	return senderSummary;
}
public void setSenderSummary(WebRTCCallDataWrapper senderSummary) {
	this.senderSummary = senderSummary;
}
public WebRTCCallDataWrapper getReciverSummary() {
	return reciverSummary;
}
public void setReciverSummary(WebRTCCallDataWrapper reciverSummary) {
	this.reciverSummary = reciverSummary;
}

public List<List<String>> getSenderLatLongList() {
	return senderLatLongList;
}
public void setSenderLatLongList(List<List<String>> senderLatLongList) {
	this.senderLatLongList = senderLatLongList;
}
public List<List<String>> getReciverLatLongList() {
	return reciverLatLongList;
}
public void setReciverLatLongList(List<List<String>> reciverLatLongList) {
	this.reciverLatLongList = reciverLatLongList;
}

@Override
public String toString() {
	return "WebRTCCallStatsWrapper [senderDataList=" + senderDataList + ", reciverDataList=" + reciverDataList
			+ ", senderSummary=" + senderSummary + ", reciverSummary=" + reciverSummary + ", senderLatLongList="
			+ senderLatLongList + ", reciverLatLongList=" + reciverLatLongList + "]";
}



}

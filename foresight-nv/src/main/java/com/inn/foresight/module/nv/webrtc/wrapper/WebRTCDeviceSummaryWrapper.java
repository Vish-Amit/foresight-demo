package com.inn.foresight.module.nv.webrtc.wrapper;

import java.util.List;
import java.util.Map;

import com.inn.core.generic.wrapper.RestWrapper;

@RestWrapper
public class WebRTCDeviceSummaryWrapper {

	List<List<String>> callList ;
	Map<String, String> allCallDetails ;
	Map<String, Map<String, String>> callMosDetailsForWeek ;
	Map<String, Map<String, Long>> mosAndCallReleaseData ;
	Map<String, Map<String, Long>> codecData ;
	Map<String, Map<String, Long>> countOfNetworkType ;
	
	public List<List<String>> getCallList() {
		return callList;
	}
	public void setCallList(List<List<String>> callList) {
		this.callList = callList;
	}
	public Map<String, String> getAllCallDetails() {
		return allCallDetails;
	}
	public void setAllCallDetails(Map<String, String> allCallCounts2) {
		this.allCallDetails = allCallCounts2;
	}
	public Map<String, Map<String, String>> getCallMosDetailsForWeek() {
		return callMosDetailsForWeek;
	}
	public void setCallMosDetailsForWeek(Map<String, Map<String, String>> callMosDetailsForWeek2) {
		this.callMosDetailsForWeek = callMosDetailsForWeek2;
	}
	public Map<String, Map<String, Long>> getMosAndCallReleaseData() {
		return mosAndCallReleaseData;
	}
	public void setMosAndCallReleaseData(Map<String, Map<String, Long>> mosAndCallReleaseData) {
		this.mosAndCallReleaseData = mosAndCallReleaseData;
	}
	public Map<String, Map<String, Long>> getCodecData() {
		return codecData;
	}
	public void setCodecData(Map<String, Map<String, Long>> codecData) {
		this.codecData = codecData;
	}
	public Map<String, Map<String, Long>> getCountOfNetworkType() {
		return countOfNetworkType;
	}
	public void setCountOfNetworkType(Map<String, Map<String, Long>> countOfNetworkType) {
		this.countOfNetworkType = countOfNetworkType;
	}
	@Override
	public String toString() {
		return "WebRTCDeviceSummaryWrapper [callList=" + callList + ", allCallDetails=" + allCallDetails
				+ ", callMosDetailsForWeek=" + callMosDetailsForWeek + ", mosAndCallReleaseData="
				+ mosAndCallReleaseData + ", codecData=" + codecData + ", countOfNetworkType=" + countOfNetworkType
				+ "]";
	}


}

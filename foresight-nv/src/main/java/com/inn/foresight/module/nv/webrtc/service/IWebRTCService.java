package com.inn.foresight.module.nv.webrtc.service;

import java.text.ParseException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.inn.foresight.module.nv.webrtc.wrapper.CallStatsWrapper;
import com.inn.foresight.module.nv.webrtc.wrapper.WebRTCCallStatsWrapper;
import com.inn.foresight.module.nv.webrtc.wrapper.WebRTCDeviceSummaryWrapper;
import com.inn.foresight.module.nv.webrtc.wrapper.WebRTCViberDashBoardDataWrapper;

public interface IWebRTCService {

	String getDataByIMSI(String imsi, String startDate, String endDate);

	WebRTCDeviceSummaryWrapper getDeviceSummaryDataByDeviceId(String devicId, String startDate, String endDate);

	WebRTCCallStatsWrapper getWebRtcDataByCallToken(String callToken);

	String getCallDataByCallToken(String callToken, HttpServletRequest request);

   	WebRTCViberDashBoardDataWrapper getViberDashboardCallData(String startDate, String endDate,
                                                              String technology, String operator, String country, String nvModule);


	WebRTCViberDashBoardDataWrapper getKpiDistribution(String startDate, String endDate, String technology,
													   String operator, String country, String nvModule);


	byte[] getFileFromHDFS(String callToken, String callType, Boolean isRCS) throws Exception;


	Map<String, CallStatsWrapper> processLastSevenDaysData(String startDate, String endDate, String technology,
														   String operator, String country, String nvModule);

	Map<String, ?> getLayerData(Double nELat, Double nELng, Double sWLat, Double sWLng, Integer zoom, String mediaType,
								String callDirection, String releaseType, String os, Long startTime, Long endTime, String minValue,
								String maxValue, String layerType) throws ParseException;

}

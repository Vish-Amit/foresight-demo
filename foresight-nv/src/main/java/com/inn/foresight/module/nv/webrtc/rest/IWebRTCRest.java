package com.inn.foresight.module.nv.webrtc.rest;

import java.io.IOException;
import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public interface IWebRTCRest {



	Response getDataByIMSI(String imsi, String startDate, String endDate) throws IOException;

	Response getCallDataByCallToken(String callToken, HttpServletRequest request);

    Response getViberDashboardData(String startDate, String endDate, String technology, String operator, String country, String nvModule);

    Response getKpiDistribution(String startDate, String endDate, String technology, String operator, String country, String nvModule);
	
	Response getLastSevenDaysData(String startDate,String endDate, String technology, String operator, String country, String nvModule);

    Response downloadFile(String callToken, String callType, Boolean isRCS) throws IOException;

    Response getViberLayer(Double nELat, Double nELng, Double sWLat, Double sWLng, Integer zoom, String mediaType,
                           String callDirection, String releaseType, String os, Long startTime, Long endTime, String minValue,
                           String maxValue, String layerType) throws ParseException;
}

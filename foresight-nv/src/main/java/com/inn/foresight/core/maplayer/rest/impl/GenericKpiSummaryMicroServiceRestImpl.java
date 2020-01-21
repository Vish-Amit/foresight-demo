package com.inn.foresight.core.maplayer.rest.impl;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.maplayer.model.GenKpiSummaryWrapper;
import com.inn.foresight.core.maplayer.service.IGenericKpiSummaryService;

@Path("/ms/kpiSummary")
public class GenericKpiSummaryMicroServiceRestImpl {
	private static final Logger logger = LogManager.getLogger(GenericKpiSummaryMicroServiceRestImpl.class);

	@Autowired
	IGenericKpiSummaryService genericKpiSummaryService;

	@POST
	@Path("getKpiSummaryData")
	@Consumes(ForesightConstants.APPLICATION_SLASH_JSON)
	@Produces(ForesightConstants.APPLICATION_SLASH_JSON)
	public String getKpiSummaryData(GenKpiSummaryWrapper kpiWrapper) {
		if (kpiWrapper != null) {
			String json = new Gson().toJson(kpiWrapper);
			logger.info("log=> KPI Wrapper json {}", json);
			if (kpiWrapper.getGeographyList() != null && kpiWrapper.getGeographyList().size() > ForesightConstants.ZERO) {
				/* this method return kpi summary data for Geography list */
				return genericKpiSummaryService.getKpiSummaryDataByGeographyList(kpiWrapper);
			} else if (kpiWrapper.getPolyList() != null && kpiWrapper.getPolyList().size() > ForesightConstants.ZERO) {
				/* this method return kpi summary data for Polygon list */
				return genericKpiSummaryService.getKpiSummaryDataByPolyList(kpiWrapper);
			} else {
				logger.info("Going to get KPI summary data  {} ", json);
				/* this method return kpi summary data for View port */
				return genericKpiSummaryService.getKpiSummaryData(kpiWrapper);
			}
		} else {
			logger.info("Getting Empty request wrapper for KPI summary {}", kpiWrapper);
		}
		return ForesightConstants.EMPTY_SQUARE_BRACKET;
	}
}
package com.inn.foresight.core.kpicomparison.rest.impl;

import java.awt.image.BufferedImage;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.kpicomparison.service.IKPIComparisonService;
import com.inn.foresight.core.kpicomparison.wrapper.KPIComparisonRequest;

/**
 * The Class ComparisionRestImpl.
 */
@Path("/comparison")
@Produces("application/json")
@Consumes("application/json")
@Service("ComparisonRestImpl")
public class KPIComparisonRestImpl {
	
	@Autowired
	private IKPIComparisonService kpiComparisonService;

	/** The logger. */
	private Logger logger = LogManager.getLogger(KPIComparisonRestImpl.class);

	@POST
	@Path("getKpiComparisionImage")
	public BufferedImage getKPIComparision(@QueryParam("tileId") String tileId, @QueryParam("siteStatus") String siteStatus,
			@QueryParam("frequency") String frequency, KPIComparisonRequest request) {
		try {
			return kpiComparisonService.getKPIComparison(tileId, siteStatus, frequency, request);
		} catch (Exception e) {
			logger.error("Exception inside getKPIComparision {} ", Utils.getStackTrace(e));
		}
		return null;
	}

	@POST
	@Path("getKPIComparisionBins")
	public List<List<String>> getNVDataComparision(KPIComparisonRequest request) {
		try {
			return kpiComparisonService.getKPIDataComparison(request);
		} catch (Exception e) {
			logger.error("Exception inside getNVDataComparision {} ", Utils.getStackTrace(e));
		}
		return null;
	}
	
	@POST
	@Path("ms/getKPIComparison")
	public Response getKPIComparisionForMS(@QueryParam("tileId") String tileId, @QueryParam("siteStatus") String siteStatus,
			@QueryParam("band") String frequency, KPIComparisonRequest request) {

		if (tileId != null && request.getKpi1() != null && request.getKpi1Column() != null
				&& request.getKpi1Date() != null && request.getKpi2() != null && request.getTable() != null
				&& request.getKpi2Column() != null && request.getKpi2Date() != null) {
			try {
				BufferedImage image = kpiComparisonService.getKPIComparisonForMS(tileId, siteStatus, frequency, request);
				return Response.ok(image).build();
			} catch (Exception e) {
				return Response.ok("{\"response\":\"error getting data\"}").build();
			}
		} else {
			return Response.ok("{\"response\":\"invalid params\"}").build();
		}
	}

}

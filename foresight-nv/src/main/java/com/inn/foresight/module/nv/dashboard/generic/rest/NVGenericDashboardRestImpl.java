package com.inn.foresight.module.nv.dashboard.generic.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.dashboard.generic.service.NVGenericDashboardService;

@Path("/NVGenericDashboard")
@Produces(ForesightConstants.APPLICATION_SLASH_JSON)
@Consumes(ForesightConstants.APPLICATION_SLASH_JSON)
@Service("NVGenericDashboardRestImpl")
public class NVGenericDashboardRestImpl {
	
	private Logger logger = LogManager.getLogger(NVGenericDashboardRestImpl.class);
	
	@Autowired
	NVGenericDashboardService service;
	
	/**
	 * @param appliedTo
	 * @return
	 */
	@GET
	@Path("/findLegend")
	public Response findLegendDetailsByAppliedTo(@QueryParam("appliedTo") String appliedTo) {
		logger.info("Going to get Legends for applied To {}", appliedTo);
		if (Utils.hasValidValue(appliedTo)) {
			return Response.ok(service.getLegendRangesDetail(appliedTo)).build();
		}
		return Response.ok(ForesightConstants.INVALID_PARAMETER).build();
	}
}

package com.inn.foresight.module.nv.dashboard.passive.rest.impl;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.inn.commons.lang.MapUtils;
import com.inn.commons.lang.StringUtils;
import com.inn.foresight.module.nv.NVConstant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.module.nv.dashboard.passive.rest.INVPassiveDashboardRest;
import com.inn.foresight.module.nv.dashboard.passive.service.INVPassiveDashboardService;

import java.util.Map;

@Path("/NVPassiveDashboard")
@Produces(ForesightConstants.APPLICATION_SLASH_JSON)
@Consumes(ForesightConstants.APPLICATION_SLASH_JSON)
@Service("NVPassiveDashboardRestImpl")
public class NVPassiveDashboardRestImpl implements INVPassiveDashboardRest {

	private Logger logger = LogManager.getLogger(NVPassiveDashboardRestImpl.class);

	@Autowired
	INVPassiveDashboardService iNVPassiveDashboardService;

	@GET
	@Path("/getNVPassiveData")
	@Override
	public Response getPassiveDashboardData(@QueryParam("date") String date,
			@QueryParam("level") String level,
			@QueryParam("geographyId") Integer geographyId,
			@QueryParam("duplexType") String duplexType,
			@QueryParam("tagType") String tagType,
			@QueryParam("appName") String appName) {
		logger.info("Going to get NV Passive Dashboard Data for Date: {}, level: {} geography Id: {} , duple Type: {} , Tag Type : {},appName {}", date, level,
				geographyId,duplexType,tagType,appName);
		if (date != null && geographyId != null && duplexType !=  null && tagType != null) {
			Map<String, Map<String, ?>> passiveDashboardData = iNVPassiveDashboardService.getPassiveDashboardData(date, level, geographyId, duplexType, tagType, appName);
			if (MapUtils.isNotEmpty(passiveDashboardData)) {
				return  Response.ok(passiveDashboardData).build();
			}
		}
		return null;
	}
	
	@GET
	@Path("/getDeviceDistribution")
	@Override
	public Response getDeviceDistribution(@QueryParam("date") String date,
			@QueryParam("level") String level,
			@QueryParam("geographyId") Integer geographyId,
			@QueryParam("duplexType") String duplexType,
			@QueryParam("tagType") String tagType,
			@QueryParam("appName") String appName) {
		logger.info("Going to get NV Passive Dashboard Data for Date: {}, level: {} geography Id: {} , duple Type: {} , Tag Type : {},appName {}", date, level,
				geographyId,duplexType,tagType,appName);
		if (date != null && geographyId != null && duplexType !=  null && tagType != null) {
			String deviceDistributionData = iNVPassiveDashboardService.getDeviceDistributionData(date, level, geographyId, duplexType, tagType, appName);
			if (StringUtils.isNotEmpty(deviceDistributionData)) {
				return  Response.ok(deviceDistributionData).build();
			}
		}
		return null;
	}
	

}

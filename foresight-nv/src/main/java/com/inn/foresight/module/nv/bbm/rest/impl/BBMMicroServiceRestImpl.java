package com.inn.foresight.module.nv.bbm.rest.impl;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.module.nv.bbm.constant.BSPConstants;
import com.inn.foresight.module.nv.bbm.service.IBBMService;
import com.inn.foresight.module.nv.bbm.utils.wrapper.BBMDetailWrapper;

@Path("/ms/BBM")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BBMMicroServiceRestImpl {

	

	private Logger logger = LogManager.getLogger(BBMMicroServiceRestImpl.class);


		@Autowired
		private IBBMService iBBMService;

		@GET
		@Path("getBBMLocationByDeviceId")
		public List<BBMDetailWrapper> getBBMLocationByDeviceId(@QueryParam("deviceIdPrefix") String deviceIdPrefix) {
			logger.info("Going to get BBMLocation By deviceId {} ", deviceIdPrefix);
			return iBBMService.getBBMDetailByDeviceIdPrefix(deviceIdPrefix, null, null);
		}

		@GET
		@Path("getBBMDetailByDeviceIdPrefix")
		public List<BBMDetailWrapper> getBBMDetailByDeviceIdPrefix(@QueryParam("deviceIdPrefix") String deviceIdPrefix,
				@QueryParam("minTimeRange") String minTimeRange, @QueryParam("maxTimeRange") String maxTimeRange) {
			logger.info("Inside getBBMDetailByDeviceIdPrefix");
			if (minTimeRange != null && maxTimeRange != null) {
				return iBBMService.getBBMDetailByDeviceIdPrefix(deviceIdPrefix, Long.parseLong(minTimeRange), Long.parseLong(maxTimeRange));
			} else {
				throw new RestException(BSPConstants.INVALID_PARAMETERS_JSON);
			}
		}
	
}

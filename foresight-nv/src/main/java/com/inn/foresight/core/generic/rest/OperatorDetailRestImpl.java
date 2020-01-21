package com.inn.foresight.core.generic.rest;

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

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.service.IOperatorDetailService;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;

@Path("/GenericOperator")
@Produces("application/json")
@Consumes("application/json")
@Service("OperatorDetailRestImpl")
public class OperatorDetailRestImpl {

	Logger logger = LogManager.getLogger(OperatorDetailRestImpl.class);
	
	@Autowired
	IOperatorDetailService iOpeatorService;

	@GET
	@Path("getSearchWiseData")
	public Response getSearchWiseResult(@QueryParam(ForesightConstants.SEARCH_TYPE) String searchType,
			@QueryParam(ForesightConstants.COUNTRY_NAME) String countryName,
			@QueryParam(ForesightConstants.MODULE) String module) {
		logger.info("Validating Input Parameters searchType {} , countryName {} , module {}",searchType,countryName,module);
		if (Utils.hasValidValue(searchType) && Utils.hasValidValue(countryName) && Utils.hasValidValue(module)) {
			return Response.ok(iOpeatorService.getOperatorData(searchType, countryName, module)).build();
		} else {
			throw new RestException(ForesightConstants.INVALID_PARAMETER);
		}
	}
}

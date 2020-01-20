package com.inn.foresight.module.nv.arm.rest;

import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.inn.foresight.core.generic.exceptions.ValueNotFoundException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.arm.service.ARMService;
import com.inn.foresight.module.nv.arm.wrapper.ARMRequestWrapper;

@Path("/ARMRest")
@Produces(ForesightConstants.APPLICATION_SLASH_JSON)
@Consumes(ForesightConstants.APPLICATION_SLASH_JSON)
@Service("ARMRestImpl")
public class ARMRestImpl {

	private Logger logger = LogManager.getLogger(ARMRestImpl.class);

	@Autowired
	ARMService armService;

	@POST
	@Path("/getAppDetails/{lLimit}/{uLimit}")
	public Response getAppDetails(String wrapper, @PathParam("lLimit") Integer lLimit,
			@PathParam("uLimit") Integer uLimit) {
		logger.info("Going to get Application for {} lower limit {} and upper limit {}", wrapper, lLimit, uLimit);
		if (Utils.hasValidValue(wrapper) && parseARMWrapper(wrapper)) {
			try {
				ARMRequestWrapper armWrapper = new Gson().fromJson(wrapper, ARMRequestWrapper.class);
				return	Response.ok(armService.getAppData(armWrapper, lLimit, uLimit)).build();
			} catch (ValueNotFoundException e) {
				logger.info("Value not found for request {}", Utils.getStackTrace(e));
			}
		} else {
			return Response.ok(ForesightConstants.INVALID_PARAMETERS_JSON).build();
		}
		return Response.ok(ForesightConstants.FAILURE_JSON).build();
	}

	private boolean parseARMWrapper(String wrapper) {
		try {
			new Gson().fromJson(wrapper, ARMRequestWrapper.class);
			return true;
		} catch (JsonSyntaxException e) {
			return false;
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updateARMDetails")
	public Response updateAppDetails(String wrapper) {
		if (Utils.hasValidValue(wrapper) && parseARMWrapper(wrapper)) {
			try {
				ARMRequestWrapper armWrapper = new Gson().fromJson(wrapper, ARMRequestWrapper.class);
				logger.info("Going to update Application for  APKdetail {} , NV profile {} , License Master {}",
						armWrapper.getApkDetail(), armWrapper.getNvProfile(), armWrapper.getLicenseMaster());
				if (armWrapper.getApkDetail() != null || armWrapper.getNvProfile() != null
						|| armWrapper.getLicenseMaster() != null) {
					return	Response.ok(armService.updateAppDetails(armWrapper)).build();
				} else {
					return	Response.ok(ForesightConstants.INVALID_PARAMETERS_JSON).build();
				} 
			} catch (Exception e) {
				logger.info("Unable to update {}", Utils.getStackTrace(e));
			}
		} else {
			return Response.ok(ForesightConstants.INVALID_PARAMETERS_JSON).build();
		}
		return Response.ok(ForesightConstants.FAILURE_JSON).build();
	}

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Path("/uploadAPK")
	public Response uploadAPK(@QueryParam("id") Integer id,
			@Multipart(value = ForesightConstants.FILE) InputStream inputFile, @QueryParam("appType") String appType) {
		logger.info("Found the id {} and appType {}",id,appType);
		if (id != null && Utils.hasValidValue(appType) && inputFile != null) {
			try {
				return Response.ok(armService.uploadAPK(id, inputFile, appType)).build();
			} catch (Exception e) {
				logger.info("Unable to upload APK {}", Utils.getStackTrace(e));
			}
		} else {
			return Response.ok(ForesightConstants.INVALID_PARAMETERS_JSON).build();
		}
		return Response.ok(ForesightConstants.FAILURE_JSON).build();
	}

}

package com.inn.foresight.module.nv.layer3.rest;


import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.module.nv.layer3.constants.NVLayer3Constants;
import com.inn.foresight.module.nv.layer3.model.Layer3Preset;
import com.inn.foresight.module.nv.layer3.service.ILayer3PresetService;
import com.inn.product.um.user.model.User;
import com.inn.product.um.user.service.impl.UserContextServiceImpl;


/** The Class Layer3PresetRestImpl. */
@Path("/Layer3Preset")
@Produces(ForesightConstants.APPLICATION_SLASH_JSON)
@Consumes(ForesightConstants.APPLICATION_SLASH_JSON)
@Service("Layer3PresetRestImpl")
public class Layer3PresetRestImpl {
	
	
	
	/** The logger. */
	private Logger logger = LogManager.getLogger(Layer3PresetRestImpl.class);

	/** The i NV Layer 3 Preset service. */
	@Autowired
	private ILayer3PresetService iLayer3PresetService;

	/**
	 * Gets the all NV Layer3Preset.
	 *
	 * @return the all NV Layer3Preset
	 */
	@GET
	@Path("getLayer3PresetByUserId")
	public Response getLayer3PresetByUserId(@QueryParam(NVLayer3Constants.PRESET_ID) String presetId) {
		User user = UserContextServiceImpl.getUserInContext();
		try {
			if(user!=null&&user.getUserid()!=null) {
				logger.info("In getLayer3PresetByUserId {} PRESET_ID  {}",user.getUserid(),presetId);
				return Response.ok( iLayer3PresetService.getLayer3PresetByUserId(user.getUserid(),presetId)).build();
			}else {
				return Response.ok(NVLayer3Constants.INVALID_USER).build();			
			}
		} catch (RestException e) {
			logger.error("Error while Getting  getLayer3PresetByUserId Data {}", e.getMessage());
			return Response.ok(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG).build();
		}
	}
	
	/**
	 * Gets the all NV Layer3Preset.
	 *
	 * @return the all NV Layer3Preset
	 */
	@GET
	@Path("getLayer3PresetDataById")
	public Response getLayer3PresetDataById(@QueryParam(NVLayer3Constants.ID) Integer id) {
		logger.info("In getLayer3PresetDataById {} ",id);
			try {
			if(id!=null) {
				return Response.ok(iLayer3PresetService.getLayer3PresetDataByPK(id)).build();
			}else {
				return Response.ok(NVLayer3Constants.INVALID_PARAMETER_JSON).build();			
			}
		} catch (RestException e) {
			logger.error("Error while Getting  getLayer3PresetDataById Data {}", e.getMessage());
			return Response.ok(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG).build();
		}
	}
	
	
	/**
	 * Gets the all NV Layer3Preset.
	 *
	 * @return the all NV Layer3Preset
	 */
	@POST
	@Path("createNVLayer3Preset")
	public Response createNVLayer3Preset(Layer3Preset layer3Preset) {
		logger.info("In createNVLayer3Preset {}" ,layer3Preset);
		User user = UserContextServiceImpl.getUserInContext();
			try {
			if(layer3Preset!=null&&user!=null) {
				return Response.ok(iLayer3PresetService.createNVLayer3Preset(layer3Preset,user)).build();
			}
			else {
				return Response.ok(NVLayer3Constants.INVALID_PARAMETER_JSON).build();			
			}
			
		} catch (RestException e) {
			logger.error("Error while creating NVLayer3Preset Data {}", e.getMessage());
			return Response.ok(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG).build();
		}
	}
	
	
	}
	
	


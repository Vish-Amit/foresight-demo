package com.inn.foresight.module.savedWorkspace.rest.impl;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.foresight.core.generic.utils.Constants;
import com.inn.foresight.module.savedWorkspace.model.SavedWorkspace;
import com.inn.foresight.module.savedWorkspace.service.ISavedWorkspaceService;

/**
 * The Class SavedWorkspaceRestImpl.
 *
 * @author innoeye
 */
@Path("/SavedWorkspace")
@Produces("application/json")
@Consumes("application/json")
@Service("SavedWorkspaceRestImpl")
public class SavedWorkspaceRestImpl {
	
	/** The logger. */
	private Logger logger = LogManager.getLogger(SavedWorkspaceRestImpl.class);


	/** The saved workspace service. */
	@Autowired
	ISavedWorkspaceService iSavedWorkspaceService;


	@GET
	@Path("getSavedWorkspaces")
	public List<SavedWorkspace> getSavedWorkspaces() {
		logger.info("Going to get all saved workspaces");
		return iSavedWorkspaceService.getSavedWorkspaces(); 
	}
	
	@GET
	@Path("getSavedWorkspaceById/{id}")
	public SavedWorkspace getSavedWorkspaceById(@NotNull @PathParam("id") Integer id) {
		logger.info("Going to get saved workspace by id : {}", id);
		return iSavedWorkspaceService.getSavedWorkspaceById(id);
	}
	
	@POST
	@Path("saveWorkspace")
	public Response saveWorkspace(@NotNull Map<String, String> json) {
		logger.info("Going to save workspace : {}", json);
		SavedWorkspace saveWorkspace = iSavedWorkspaceService.saveWorkspace(json);
		return (saveWorkspace.getId() != null) ? Response.ok(saveWorkspace).build() : Response.ok(saveWorkspace.getName()).build(); 
	}
	
	
	@GET
	@Path("getSavedPresetListByModuleId")
	public Response getSavedPresetListByModuleId(@QueryParam("moduleId") Integer moduleId){
		logger.info("Going to get preset list for module id: {}",moduleId);
		try {
			return iSavedWorkspaceService.getSavedPresetListByModuleId(moduleId);
			}catch(Exception e) {
			logger.error("Error while getting preset list for module id: {}, {}",moduleId, ExceptionUtils.getStackTrace(e));
			return Response.ok(Constants.ATTACHMENT_FAILURE_RESPONSE).build();
		}
	}
	
	
	@POST
	@Path("updateWorkspace")
	public Response updateWorkspace(@NotNull Map<String, String> json) {
		logger.info("Going to update workspace : {}", json);
		try {
		SavedWorkspace saveWorkspace = iSavedWorkspaceService.updateWorkspace(json);
		if(saveWorkspace != null) {
			logger.info("Preset with id: {} updated successfully",saveWorkspace.getId());
			return Response.ok(Constants.ATTACHMENT_SUCCESS_RESPONSE).build();
		}else {
			logger.error("Unable to update preset");
			return Response.ok(Constants.ATTACHMENT_FAILURE_RESPONSE).build();
		}
	
		}catch(Exception e) {
			logger.error("Error while update existing workspace: {}",e.getMessage());
			return Response.ok(Constants.ATTACHMENT_FAILURE_RESPONSE).build();
		}
	}

	@POST
	@Path("deleteWorkspace")
	public Response deleteWorkspace(@QueryParam("presetId") Integer presetId) {
		logger.info("Going to delete workspace with id : {}", presetId);
		try {
			return iSavedWorkspaceService.deleteWorkspace(presetId);
		} catch (Exception e) {
			logger.error("Error while removing preset by id: {}, {}", presetId, e.getMessage());
			return Response.ok(Constants.ATTACHMENT_FAILURE_RESPONSE).build();
		}
	}

}

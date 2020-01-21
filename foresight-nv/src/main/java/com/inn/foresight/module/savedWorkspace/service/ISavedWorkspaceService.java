/**
 * 
 */
package com.inn.foresight.module.savedWorkspace.service;

import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.module.savedWorkspace.model.SavedWorkspace;

/**
 * @author root
 *
 */
public interface ISavedWorkspaceService extends IGenericService<Integer, SavedWorkspace>{
	
	public List<SavedWorkspace> getSavedWorkspaces();
	public SavedWorkspace getSavedWorkspaceById(Integer id);
	public SavedWorkspace saveWorkspace(Map<String, String> json);
	public Response getSavedPresetListByModuleId(Integer moduleId);
	public SavedWorkspace updateWorkspace(Map<String, String> json);
	public Response deleteWorkspace(Integer presetId);
	
}
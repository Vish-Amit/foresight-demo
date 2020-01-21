package com.inn.foresight.module.savedWorkspace.dao;

import java.util.List;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.foresight.module.savedWorkspace.model.SavedWorkspace;

public interface ISavedWorkspaceDao extends IGenericDao<Integer, SavedWorkspace>{
	public List<SavedWorkspace> getSavedWorkspaces();
	public SavedWorkspace getSavedWorkspaceById(Integer id);
	public List<SavedWorkspace> getSavedWorkspaceByName(String workspaceName);
	public List<SavedWorkspace> getSavedPresetListByModuleId(Integer moduleId);
}

package com.inn.foresight.module.savedWorkspace.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.service.impl.AbstractService;
import com.inn.foresight.core.generic.utils.Constants;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.savedWorkspace.dao.ISavedWorkspaceDao;
import com.inn.foresight.module.savedWorkspace.model.SavedWorkspace;
import com.inn.foresight.module.savedWorkspace.service.ISavedWorkspaceService;
import com.inn.product.um.module.dao.ModuleDao;
import com.inn.product.um.module.model.Module;
import com.inn.product.um.user.service.impl.UserContextServiceImpl;

/**
 * The Class SavedWorkspaceServiceImpl.
 *
 * @author innoeye
 */
@Service("SavedWorkspaceServiceImpl")
public class SavedWorkspaceServiceImpl extends AbstractService<Integer, SavedWorkspace>
		implements ISavedWorkspaceService {

	/** The isaved workspace dao. */
	@Autowired
	private ISavedWorkspaceDao isavedWorkspaceDao;

	@Autowired
	private ModuleDao iModuleDao;

	/** The logger. */
	private Logger logger = LogManager.getLogger(SavedWorkspaceServiceImpl.class);

	@Override
	public SavedWorkspace saveWorkspace(Map<String, String> json) {
		SavedWorkspace createdEntity = new SavedWorkspace();
		try {
			if (json.isEmpty() || !(json.containsKey(ForesightConstants.NAME))
					|| !(json.containsKey(ForesightConstants.CONFIGURATION))) {
				throw new RestException("Please provide Valid JSON");
			}
			String workspaceName = json.get(ForesightConstants.NAME);
			if (isExist(workspaceName)) {
				throw new RestException("Workspace Already Exists");
			}
			SavedWorkspace workspace = new SavedWorkspace();
			workspace.setName(workspaceName);
			workspace.setConfiguration(json.get(ForesightConstants.CONFIGURATION));
			workspace.setUser(UserContextServiceImpl.getUserInContext());
			workspace.setDeleted(false);
			Module module = getModuleByPk(json);
			workspace.setModule(module);
			createdEntity = isavedWorkspaceDao.create(workspace);
		} catch (RestException e) {
			throw e;
		} catch (DataIntegrityViolationException e) {
			logger.error("Error in saveWorkspace error message due to DataIntegrityViolationException {}" + ExceptionUtils.getStackTrace(e));
			createdEntity.setName(ForesightConstants.INVALID_PARAMETERS_JSON);
		} catch (NullPointerException e) {
			logger.error("Error in saveWorkspace error message due to NullPointerException {}" + ExceptionUtils.getStackTrace(e));
			createdEntity.setName(ForesightConstants.NULL_STRING);

		} catch (Exception e) {
			logger.error("Error in saveWorkspace error message due to unknown exception {}" + ExceptionUtils.getStackTrace(e));
			createdEntity.setName(ForesightConstants.FAILURE_JSON);
		}
		return createdEntity;
	}

	private Module getModuleByPk(Map<String, String> json) {
		Module module = null;
		try {
			Integer moduleId = Integer.parseInt(json.get("moduleId"));
			logger.info("Getting module by id: {}", moduleId);
			if (moduleId != null) {
				module = iModuleDao.findByPk(moduleId);
			}
		} catch (Exception e) {
			logger.error("Error while getting module by module id: {}", e.getMessage());
		}
		return module;
	}

	private Boolean isExist(String name) {
		List<SavedWorkspace> workspaces = isavedWorkspaceDao.getSavedWorkspaceByName(name);
		return !(workspaces.isEmpty());
	}

	public List<SavedWorkspace> getSavedWorkspaces() {
		try {
			return isavedWorkspaceDao.getSavedWorkspaces();
		} catch (Exception e) {
			logger.error("Error in geting getSavedWorkspaces error message {}", e.getMessage());
		}
		return Collections.emptyList();
	}

	@Override
	public SavedWorkspace getSavedWorkspaceById(Integer id) {
		try {
			return isavedWorkspaceDao.getSavedWorkspaceById(id);
		} catch (Exception e) {
			logger.error("Error in geting getSavedWorkspaceById error message {}", e.getMessage());
		}
		return new SavedWorkspace();
	}

	@Override
	public Response getSavedPresetListByModuleId(Integer moduleId) {
		try {
			return Response.ok(isavedWorkspaceDao.getSavedPresetListByModuleId(moduleId)).build();
		} catch (Exception e) {
			logger.error("Error while getting saved preset list: {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(Constants.ATTACHMENT_FAILURE_RESPONSE).build();
		}
	}

	@Override
	public SavedWorkspace updateWorkspace(Map<String, String> json) {
		try {

			String presetid = json.get("presetId");
			String configuration = json.get("configuration");
			String name = json.get("name");
			Integer presetId = null;
			if (Utils.hasValidValue(presetid) && configuration != null) {
				presetId = Integer.parseInt(json.get("presetId"));
				SavedWorkspace oldPreset = isavedWorkspaceDao.findByPk(presetId);
				oldPreset.setConfiguration(configuration);
				if (name != null) {
					oldPreset.setName(name);
				}
				return isavedWorkspaceDao.update(oldPreset);
			} else {
				throw new RestException("Invalid input");
			}
		} catch (RestException e) {
			logger.error("Error while update existing preset: {}", ExceptionUtils.getStackTrace(e));
			throw new RestException(Constants.ATTACHMENT_FAILURE_JSON);
		}
	}

	@Override
	@Transactional
	public Response deleteWorkspace(Integer presetId) {
		try {
			isavedWorkspaceDao.deleteByPk(presetId);
			logger.info("Preset deleted successfully");
			return Response.ok(Constants.ATTACHMENT_SUCCESS_RESPONSE).build();
		} catch (Exception e) {
			logger.error("Error while deleting preset with id: {}, {}", presetId, ExceptionUtils.getStackTrace(e));
			return Response.ok(Constants.ATTACHMENT_FAILURE_RESPONSE).build();
		}
	}

}

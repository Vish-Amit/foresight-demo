package com.inn.foresight.module.savedWorkspace.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.module.savedWorkspace.model.SavedWorkspace;
import com.inn.product.um.user.model.User;
import com.inn.product.um.user.service.impl.UserContextServiceImpl;

@Repository("SavedWorkspaceDaoImpl")
public class SavedWorkspaceDaoImpl extends HibernateGenericDao<Integer, SavedWorkspace> implements ISavedWorkspaceDao{

	/** The logger. */
	private Logger logger = LogManager.getLogger(SavedWorkspaceDaoImpl.class);
	
	/**
	 * Instantiates a new saved workspace dao impl.
	 */
	public SavedWorkspaceDaoImpl() {
		super(SavedWorkspace.class);
	}
	
	@Override
	public List<SavedWorkspace> getSavedWorkspaceByName(String workspaceName){
		try{
			User user = UserContextServiceImpl.getUserInContext();
			TypedQuery<SavedWorkspace> query = getEntityManager().createNamedQuery("getWorkspaceByName",SavedWorkspace.class);
			query.setParameter("workspaceName", workspaceName);
			query.setParameter(ForesightConstants.KEY_USER_ID, user.getUserid());
			return query.getResultList();
		} catch(NoResultException e) {
			logger.error("Error in geting getSavedWorkspaceByName error message {}", e.getMessage());
		}
		return Collections.emptyList();
	}

	@Override
	public List<SavedWorkspace> getSavedWorkspaces() {
		logger.info("inside @method getSavedWorkspaces");
		try{
			User user = UserContextServiceImpl.getUserInContext();
			TypedQuery<SavedWorkspace> query = getEntityManager().createNamedQuery("getUserWorkspaces",SavedWorkspace.class);
			query.setParameter(ForesightConstants.KEY_USER_ID, user.getUserid());
			return query.getResultList();
		} catch(NoResultException e) {
			logger.error("Error in geting getSavedWorkspaces error message {}", e.getMessage());
		}
		return Collections.emptyList();
	}

	@Override
	public SavedWorkspace getSavedWorkspaceById(Integer id) {
		try{
			User user = UserContextServiceImpl.getUserInContext();
			TypedQuery<SavedWorkspace> query = getEntityManager().createNamedQuery("getUserWorkspaceById",SavedWorkspace.class);
			query.setParameter("id", id);
			query.setParameter(ForesightConstants.KEY_USER_ID, user.getUserid());
			return query.getSingleResult();
		} catch(NoResultException e) {
			logger.error("Error in geting getSavedWorkspaces error message {}", e.getMessage());
		}
		return new SavedWorkspace();
	}

	@Override
	public List<SavedWorkspace> getSavedPresetListByModuleId(Integer moduleId) {
		List<SavedWorkspace> presetList = new ArrayList<>();
		try {
			Query query = getEntityManager().createNamedQuery("getSavedPresetListByModuleId");
			query.setParameter("moduleId", moduleId);
			presetList = query.getResultList();
			logger.info("Returning total: {} records for module id: {}",presetList.size(),moduleId);
			return presetList;			
		}catch(Exception e) {
			logger.error("Error while getting saved preset list: {}",ExceptionUtils.getStackTrace(e));
			logger.info("Returning total: {} records for module id: {}",presetList.size(),moduleId);
		}
		return presetList;
	}
	
}

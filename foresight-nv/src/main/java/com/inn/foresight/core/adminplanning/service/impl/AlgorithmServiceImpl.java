package com.inn.foresight.core.adminplanning.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.service.impl.AbstractService;
import com.inn.core.generic.utils.Preconditions;
import com.inn.foresight.core.adminplanning.dao.IAlgorithmDao;
import com.inn.foresight.core.adminplanning.model.Algorithm;
import com.inn.foresight.core.adminplanning.model.AlgorithmProperty;
import com.inn.foresight.core.adminplanning.service.IAlgorithmService;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.product.um.user.model.User;
import com.inn.product.um.user.service.impl.UserContextServiceImpl;

/**
 * The Class AlgorithmServiceImpl.
 */
@Service("AlgorithmServiceImpl")

public class AlgorithmServiceImpl extends AbstractService<Integer, Algorithm> implements IAlgorithmService {

	/** The logger. */
	private Logger logger = LogManager.getLogger(AlgorithmServiceImpl.class);

	/** The i algorithm dao. */
	private IAlgorithmDao iAlgorithmDao;

	/**
	 * Sets the dao.
	 *
	 * @param dao the new dao
	 */
	@Autowired
	public void setDao(IAlgorithmDao dao) {
		super.setDao(dao);
		this.iAlgorithmDao = dao;

	}

	/* (non-Javadoc)
	 * @see com.inn.foresight.core.adminplanning.service.IAlgorithmService#searchAlgorithmData(java.lang.String)
	 */
	@Transactional
	@Override
	public List<Algorithm> searchAlgorithmData(String displayName) {
		logger.info("inside @searchAlgorithmData service method displayName : {}", displayName);
		try {
			return iAlgorithmDao.searchAlgorithmData(displayName);
		} catch (DaoException e) {
			logger.error("Exception in @searchAlgorithmData method with error : {}", Utils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.inn.foresight.core.adminplanning.service.IAlgorithmService#
	 * getAllAlgorithms(java.lang.Integer, java.lang.Integer)
	 */
	@Transactional
	@Override
	public List<Algorithm> getAllAlgorithms() {
		try {
			return iAlgorithmDao.getAllAlgorithms();
		} catch (DaoException de) {
			logger.error("Exception in getAllAlgorithms : {}", de.getMessage());
			throw new RestException(de.getMessage());
		}

	}
	
	/* (non-Javadoc)
	 * @see com.inn.foresight.core.adminplanning.service.IAlgorithmService#createAlgorithm(com.inn.foresight.core.adminplanning.model.Algorithm)
	 */
	@Transactional
	@Override
	public void createAlgorithm(Algorithm algorithm) {
		try {
			modifyAlgorithm(algorithm, false);
			iAlgorithmDao.create(algorithm);
		} catch (DaoException e) {
			logger.error("Exception in createAlgorithm : {}", e.getMessage());
			throw new RestException(e.getMessage());
		}
	}

	/**
	 * Modify algorithm.
	 *
	 * @param algorithm the algorithm
	 * @param isDeleted is algorithm deleted
	 */
	private void modifyAlgorithm(Algorithm algorithm, Boolean isDeleted) {
		User user = UserContextServiceImpl.getUserInContext();
		Date currentDate = new Date();
		algorithm.setModifier(user);
		algorithm.setModifiedTime(currentDate);
		algorithm.setCreationTime(currentDate);
		algorithm.setDeleted(isDeleted);
		Set<AlgorithmProperty> properties = algorithm.getAlgorithmProperty();
		for (AlgorithmProperty childAlgo : properties) {
			childAlgo.setModifier(user);
			childAlgo.setModificationTime(currentDate);
			childAlgo.setCreationTime(currentDate);
			algorithm.setDeleted(isDeleted);
			if (childAlgo.getConfiguration() != null) {
				childAlgo.setUiConfiguration(new Gson().toJson(childAlgo.getConfiguration()));
			}
		}
	}

	@Transactional
	@Override
	public void deleteAlgorithm(Integer algorithmId) {
		try {
			Algorithm algorithm = iAlgorithmDao.findByPk(algorithmId);
			Preconditions.checkNotNull(algorithm);
			modifyAlgorithm(algorithm, true); // setting true as the algorithm is deleted.
			iAlgorithmDao.update(algorithm);
		} catch (RestException rest) {
			rest.setGuiMessage("Algorithm does not exist.");
			throw rest;
		} catch (DaoException dao) {
			logger.error("Exception in transaction : {}", Utils.getStackTrace(dao));
			throw new RestException(dao);
		} catch (Exception e) {
			logger.error("Exception in deleting algorithm : {} {}", algorithmId, Utils.getStackTrace(e));
			throw new RestException(e);
		}
	}

	@Transactional
	@Override
	public void updateAlgorithm(Algorithm algorithm) {
		try {
			Algorithm existingAlgorithm = iAlgorithmDao.findByPk(algorithm.getId());
			copyParameters(algorithm, existingAlgorithm);
			existingAlgorithm.setModifiedTime(new Date());
			existingAlgorithm.setModifier(UserContextServiceImpl.getUserInContext());
			iAlgorithmDao.update(existingAlgorithm);
		} catch (DaoException dao) {
			logger.error("Exception in transaction : {}", Utils.getStackTrace(dao));
			throw new RestException(dao);
		} catch (Exception e) {
			logger.error("Exception in updating algorithm : {} {}", algorithm.getId(), Utils.getStackTrace(e));
			throw new RestException(e);
		}

	}

	private void copyParameters(Algorithm algorithm, Algorithm existingAlgorithm) {
		if (algorithm.getName() != null) {
			existingAlgorithm.setName(algorithm.getName());
		}
		if (algorithm.getExceptionEnabled() != null) {
			existingAlgorithm.setExceptionEnabled(algorithm.getExceptionEnabled());
		}
		if (algorithm.getMailEnabled() != null) {
			existingAlgorithm.setMailEnabled(algorithm.getMailEnabled());
		}
		if (algorithm.getRecipients() != null) {
			existingAlgorithm.setRecipients(algorithm.getRecipients());
		}
		if (algorithm.getDisplayName() != null) {
			existingAlgorithm.setDisplayName(algorithm.getDisplayName());
		}
	}
}

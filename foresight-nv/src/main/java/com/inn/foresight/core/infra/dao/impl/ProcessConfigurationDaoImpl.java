package com.inn.foresight.core.infra.dao.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.utils.ExceptionUtil;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.infra.dao.IProcessConfigurationDao;
import com.inn.foresight.core.infra.model.ProcessConfiguration;

@Repository("ProcessConfigurationDaoImpl")
public class ProcessConfigurationDaoImpl extends HibernateGenericDao<Integer, ProcessConfiguration> implements IProcessConfigurationDao {

	/** The logger. */
	private Logger logger = LogManager.getLogger(ProcessConfigurationDaoImpl.class);

	/**
	 * Instantiates a new system configuration dao impl.
	 */
	public ProcessConfigurationDaoImpl() {
		super(ProcessConfiguration.class);
	}

	@Override
	public List<ProcessConfiguration> findAll() {
		try {
			return super.findAll();
		} catch (Exception e) {
			logger.error("Exception While findAll  {} ", ExceptionUtils.getStackTrace(e));
			throw new DaoException(ExceptionUtil.generateExceptionCode(ForesightConstants.DAO, ForesightConstants.PROCESS_CONFIGURATION, e));
		}
	}
	
	@Override
	public List<ProcessConfiguration> getProcessConfigurationByType(String type) {
		try {
			Query query = getEntityManager().createNamedQuery("getProcessConfigurationByType");
			query.setParameter("type", type);
			return query.getResultList();
		} catch (Exception e) {
			logger.error("Exception While getProcessConfigurationByType  {} ", ExceptionUtils.getStackTrace(e));
			throw new DaoException(ExceptionUtil.generateExceptionCode(ForesightConstants.DAO, ForesightConstants.PROCESS_CONFIGURATION, e));
		}
	}
	
	@Override
    public Long getProcessConfigurationCount() {
			  try {
				  Query query = getEntityManager().createNamedQuery("getProcessConfigurationCount");
				  return (Long) query.getSingleResult();
			  }catch(Exception e) {
				  logger.error("Exception occured while getting counts for getProcessConfigurationCount");
				  throw new DaoException(ExceptionUtil.generateExceptionCode(ForesightConstants.DAO, ForesightConstants.PROCESS_CONFIGURATION, e));
			  }
		  }
	@Override
	public List<ProcessConfiguration> getProcessConfigurationByNameList(List<String> nameList) {
		try {
			Query query = getEntityManager().createNamedQuery("getProcessConfigurationByNameList");
			query.setParameter("nameList", nameList);
			return query.getResultList();
		} catch (Exception e) {
			logger.error("Exception While getProcessConfigurationByNameList  {} ", ExceptionUtils.getStackTrace(e));
			throw new DaoException(ExceptionUtil.generateExceptionCode(ForesightConstants.DAO, ForesightConstants.PROCESS_CONFIGURATION, e));
		}
	}
	
	@Override
	public List<ProcessConfiguration> getProcessConfiguration(Integer upperLimit, Integer lowerLimit) {
		try {
			Query query = getEntityManager().createNamedQuery("getProcessConfiguration");
			if (lowerLimit != null && upperLimit != null) {
				query.setFirstResult(lowerLimit);
				query.setMaxResults(upperLimit - lowerLimit );
			}
			return query.getResultList();
		} catch (NoResultException e) {
			logger.error("NoResultException While getting ProcessConfiguration  {} ", ExceptionUtils.getStackTrace(e));
			throw new DaoException(ExceptionUtil.generateExceptionCode(ForesightConstants.DAO, ForesightConstants.PROCESS_CONFIGURATION, e));
		}catch (Exception e) {
			logger.error("Exception While  getting ProcessConfiguration  {} ", ExceptionUtils.getStackTrace(e));
			throw new DaoException(ExceptionUtil.generateExceptionCode(ForesightConstants.DAO, ForesightConstants.PROCESS_CONFIGURATION, e));
		}
	}
	
	@Override
	public ProcessConfiguration update(ProcessConfiguration processConf) {
		logger.info("updating users by entity : {}", processConf);
		ProcessConfiguration entityUsers = findByPk(processConf.getId());
		processConf.setModificationTime(new Date());
		processConf.setCreationTime(entityUsers.getCreationTime());
		return update(processConf);
	}
	
	@Override
	public List<ProcessConfiguration> getAllProcessConfiguration() {
		try {
			Query query = getEntityManager().createNamedQuery("getAllProcessConfiguration");
			return query.getResultList();
		} catch (Exception e) {
			logger.error("Exception While getAllProcessConfiguration  {} ", ExceptionUtils.getStackTrace(e));
			throw new DaoException(ExceptionUtil.generateExceptionCode(ForesightConstants.DAO, ForesightConstants.PROCESS_CONFIGURATION, e));
		}
		
	}
	}


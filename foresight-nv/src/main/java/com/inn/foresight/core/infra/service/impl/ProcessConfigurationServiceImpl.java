package com.inn.foresight.core.infra.service.impl;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.service.impl.AbstractService;
import com.inn.core.generic.utils.ExceptionUtil;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.infra.dao.IProcessConfigurationDao;
import com.inn.foresight.core.infra.model.ProcessConfiguration;
import com.inn.foresight.core.infra.service.IProcessConfigurationService;

@Service("ProcessConfigurationServiceImpl")
public class ProcessConfigurationServiceImpl extends AbstractService<Integer, ProcessConfiguration> implements IProcessConfigurationService {

	/** The logger. */
	private Logger logger = LogManager.getLogger(ProcessConfigurationServiceImpl.class);
	
	
	
	
	/** The dao. */
	@Autowired
	private IProcessConfigurationDao iprocessconfigurationDao;

	
	 
	public void setDao(IProcessConfigurationDao dao) {
		super.setDao(dao);
		this.iprocessconfigurationDao = dao;
	}
	
	@Override
	public List<ProcessConfiguration> findAll() {
		try {
			return iprocessconfigurationDao.findAll();
		} catch (DaoException e) {
			logger.error("DaoException While findAll  {} ", ExceptionUtils.getStackTrace(e));
			throw new RestException(e);
		} catch (Exception e) {
			logger.error("Exception While findAll  {} ", ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtil.generateExceptionCode(ForesightConstants.SERVICE, "ProcessConfiguration", e));
		}
	}
	
	@Override
	public List<ProcessConfiguration> getProcessConfigurationByType(String type) {
		try {
			if (type != null) {
				return iprocessconfigurationDao.getProcessConfigurationByType(type);
			} else {
				throw new RestException("name or type should not be Empty");
			}
		} catch (DaoException e) {
			logger.error("DaoException While getProcessConfigurationByType  {} ", ExceptionUtils.getStackTrace(e));
			throw new RestException(e);
		} catch (Exception e) {
			logger.error("Exception While getProcessConfigurationByType  {} ", ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtil.generateExceptionCode(ForesightConstants.SERVICE, ForesightConstants.PROCESS_CONFIGURATION, e));
		}
	}
		@Transactional
		@Override
		public Map<String,String> updateProcessConfiguration(ProcessConfiguration processconfiguration){
			Map<String,String> map = new HashMap<>();
			try {
				if(processconfiguration != null && processconfiguration.getId() != null) {		
					iprocessconfigurationDao.update(processconfiguration);
					map.put(ForesightConstants.MESSAGE, ForesightConstants.PROCESS_CONFIG_UPDATED_SUCCESSFULLY);
				}else {
					logger.error("Id cannot be null");
					throw new RestException("Id cannot be null");
				}
			}catch (Exception e) {
				logger.error("Exception occured while updating id {} in system configuration , Message : {} ",processconfiguration!=null?processconfiguration.getId():null , ExceptionUtils.getStackTrace(e));
				throw new RestException(ExceptionUtil.generateExceptionCode(ForesightConstants.SERVICE, ForesightConstants.PROCESS_CONFIGURATION, e));
			}
			return map;
		}	
	

	@Override
	public List<ProcessConfiguration> getProcessConfigurationByNameList(List<String> nameList) {
		try {
			if (nameList != null && !nameList.isEmpty()) {
				return iprocessconfigurationDao.getProcessConfigurationByNameList(nameList);
			} else {
				throw new RestException("name or type should not be Empty");
			}
		} catch (DaoException e) {
			logger.error("DaoException While getProcessConfigurationByType  {} ", ExceptionUtils.getStackTrace(e));
			throw new RestException(e);
		} catch (Exception e) {
			logger.error("Exception While getProcessConfigurationByType  {} ", ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtil.generateExceptionCode(ForesightConstants.SERVICE, ForesightConstants.PROCESS_CONFIGURATION, e));
		}
	}
	
	@Override
	public Long getProcessConfigurationCount(){
		try {
			return iprocessconfigurationDao.getProcessConfigurationCount();
		} catch (DaoException e) {
			logger.error("DaoException While getting count for ProcessConfiguration data {} ", ExceptionUtils.getStackTrace(e));
			throw new RestException(e);
		} catch (Exception e) {
			logger.error("Exception While getting  count for ProcessConfiguration data {} ", ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtil.generateExceptionCode(ForesightConstants.SERVICE, ForesightConstants.PROCESS_CONFIGURATION, e));
		}
	}
	
	@Override
	public List<ProcessConfiguration> getProcessConfiguration(Integer upperLimit, Integer lowerLimit){
		try {
			return iprocessconfigurationDao.getProcessConfiguration(upperLimit, lowerLimit);
		} catch (DaoException e) {
			logger.error("DaoException While getting All ProcessConfiguration data {} ", ExceptionUtils.getStackTrace(e));
			throw new RestException(e);
		} catch (Exception e) {
			logger.error("Exception While getting All ProcessConfiguration data {} ", ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtil.generateExceptionCode(ForesightConstants.SERVICE, ForesightConstants.PROCESS_CONFIGURATION, e));
		}
	}
	
	@Override
    public List<ProcessConfiguration> getAllProcessConfiguration(){
		try {
			return iprocessconfigurationDao.getAllProcessConfiguration();
		} catch (DaoException e) {
			logger.error("DaoException While getting All ProcessConfiguration data {} ", ExceptionUtils.getStackTrace(e));
			throw new RestException(e);
		} catch (Exception e) {
			logger.error("Exception While getting All ProcessConfiguration data {} ", ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtil.generateExceptionCode(ForesightConstants.SERVICE, ForesightConstants.PROCESS_CONFIGURATION, e));
		}
	}

	@Override
	@Transactional
	public ProcessConfiguration createProcessConf(ProcessConfiguration processconfiguration) {
		try {
			processconfiguration.setCreationTime(new Date());
			processconfiguration.setModificationTime(new Date());
			return iprocessconfigurationDao.create(processconfiguration);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception While create  {} ", ExceptionUtils.getStackTrace(e));
			
		}
		return processconfiguration;
	}
	
	}


package com.inn.foresight.core.infra.rest.impl;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.cxf.jaxrs.ext.search.SearchContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.rest.AbstractCXFRestService;
import com.inn.core.generic.service.IGenericService;
import com.inn.core.generic.utils.ExceptionUtil;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.infra.model.ProcessConfiguration;
import com.inn.foresight.core.infra.service.IProcessConfigurationService;

@Path("ProcessConfiguration")
@Produces("application/json")
@Consumes("application/json")
@Service("ProcessConfigurationRestImpl")
public class ProcessConfigurationRestImpl extends AbstractCXFRestService<Integer, ProcessConfiguration> {

	private Logger logger = LogManager.getLogger(ProcessConfigurationRestImpl.class);
	
	@Autowired
	private IProcessConfigurationService iProcessConfigurationService;
	
	private static final String PROCESS_CONFIGURATION = "ProcessConfiguration";
	
	public ProcessConfigurationRestImpl() {
		super(ProcessConfiguration.class);
	}
	@Override
	@GET
	@Path("findById/{id}")
	public ProcessConfiguration findById(@PathParam("id") Integer id) {
		try {
			return iProcessConfigurationService.findById(id);
		} catch (RestException e) {
			logger.error("RestException While findbyid  {} ", ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getGuiMessage());
		} catch (Exception e) {
			logger.error("Exception While findbyid  {} ", ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtil.generateExceptionCode(ForesightConstants.REST, PROCESS_CONFIGURATION, e));
		}
	}





	@Override
	public List<ProcessConfiguration> search(ProcessConfiguration entity) {
		try {
			return iProcessConfigurationService.search(entity);
		} catch (RestException exception) {
			throw new RestException(exception.getGuiMessage());
		} catch (Exception exception) {
			logger.error("Exception While findAll  {} ", ExceptionUtils.getStackTrace(exception));
					throw new RestException(ExceptionUtil.generateExceptionCode(ForesightConstants.REST, PROCESS_CONFIGURATION, exception));
		}
	}


	
	@Path("createProcessConf")
	@POST
	public ProcessConfiguration createProcessConf(ProcessConfiguration processconfiguration) {
		try {
			return iProcessConfigurationService.createProcessConf(processconfiguration);
		} catch (Exception e) {
			logger.error("Exception While create  {} ", ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtil.generateExceptionCode(ForesightConstants.REST, PROCESS_CONFIGURATION, e));
		}
	}



	@Override
	@Path("update")
	@POST
	public ProcessConfiguration update(ProcessConfiguration processconfiguration) {
		try {
			return iProcessConfigurationService.update(processconfiguration);
		} catch (RestException e) {
			logger.error("RestException While update  {} ", ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getGuiMessage());
		} catch (Exception e) {
			logger.error("Exception While update  {} ", ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtil.generateExceptionCode(ForesightConstants.REST, PROCESS_CONFIGURATION, e));
		}
	}



	@Override
	public boolean remove(@Valid ProcessConfiguration anEntity) {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public void removeById(@NotNull Integer primaryKey) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public IGenericService<Integer, ProcessConfiguration> getService() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public SearchContext getSearchContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@POST
	@Path("getProcessConfigurationByNameList")
	public List<ProcessConfiguration> getProcessConfigurationByNameList(List<String> nameList) {
		logger.info("going to get SystemConfiguration By NameList {}  ", nameList);
		try {
			return iProcessConfigurationService.getProcessConfigurationByNameList(nameList);
		} catch (RestException e) {
			logger.error("RestException While getProcessConfigurationByNameList   {} ", ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getGuiMessage());
		} catch (Exception e) {
			logger.error("Exception While getProcessConfigurationByNameList  {} ", ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtil.generateExceptionCode(ForesightConstants.REST, PROCESS_CONFIGURATION, e));
		}
	}
	
	@GET
	@Path("getProcessConfigurationByType/{type}")
	public List<ProcessConfiguration> getProcessConfigurationByType(@PathParam("type") String type) {
		logger.info("going to get ProcessConfiguration By type {}  ", type);
		try {
			return iProcessConfigurationService.getProcessConfigurationByType(type);
		} catch (RestException e) {
			logger.error("RestException While getProcessConfigurationByType   {} ", ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getGuiMessage());
		} catch (Exception e) {
			logger.error("Exception While getProcessConfigurationByType  {} ", ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtil.generateExceptionCode(ForesightConstants.REST, PROCESS_CONFIGURATION, e));
		}
	}
	
	@GET
	@Path("getProcessConfigurations")
	public List<ProcessConfiguration> getProcessConfigurations(@QueryParam("upperLimit") Integer upperLimit , @QueryParam("lowerLimit") Integer lowerLimit) {
		try {
			return iProcessConfigurationService.getProcessConfiguration(upperLimit, lowerLimit);
		} catch (RestException e) {
			logger.error("RestException While getting Process configurations {} ", ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getGuiMessage());
		} catch (Exception e) {
			logger.error("Exception While getting Process configurations {} ", ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtil.generateExceptionCode(ForesightConstants.REST, PROCESS_CONFIGURATION, e));
		}
	}

	@GET
	@Path("getProcessConfigurationCount")
	public Long getProcessConfigurationCount() {
		try {
			return iProcessConfigurationService.getProcessConfigurationCount();
		} catch (RestException e) {
			logger.error("RestException While getting Process configuration count {} ", ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getGuiMessage());
		} catch (Exception e) {
			logger.error("Exception While getting Process configuration count {} ", ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtil.generateExceptionCode(ForesightConstants.REST, PROCESS_CONFIGURATION, e));
		}
	}
	
	@GET
	@Path("getAllProcessConfiguration")
	public List<ProcessConfiguration> getAllProcessConfiguration() {
		try {
			return iProcessConfigurationService.getAllProcessConfiguration();
		} catch (RestException e) {
			logger.error("RestException While getting All Process configuration  {} ", ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getGuiMessage());
		} catch (Exception e) {
			logger.error("Exception While getting Process All configuration {} ", ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtil.generateExceptionCode(ForesightConstants.REST, PROCESS_CONFIGURATION, e));
		}
	}
	@Override
	public ProcessConfiguration create(@Valid ProcessConfiguration anEntity) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<ProcessConfiguration> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}

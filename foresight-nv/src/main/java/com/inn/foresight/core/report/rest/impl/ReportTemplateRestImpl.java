package com.inn.foresight.core.report.rest.impl;

import java.util.Base64;
import java.util.List;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.cxf.jaxrs.ext.search.SearchContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.encoder.AESUtils;
import com.inn.commons.http.HttpGetRequest;
import com.inn.commons.http.HttpRequest;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.rest.AbstractCXFRestService;
import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.report.model.ReportTemplate;
import com.inn.foresight.core.report.service.IReportTemplateService;
import com.inn.foresight.core.report.wrapper.ReportTemplateWrapper;

@Path("/ReportTemplate")
@Produces("application/json")
@Consumes("application/json")
@Service("ReportTemplateRestImpl")
public class ReportTemplateRestImpl extends AbstractCXFRestService<Integer, ReportTemplate> {
	public ReportTemplateRestImpl() {
		super(ReportTemplate.class);
	}

	private Logger logger = LogManager.getLogger(ReportTemplateRestImpl.class);

	@Autowired
	private IReportTemplateService service;

	@GET
	@Path("search")
	@Override
	public List<ReportTemplate> search(@QueryParam("") ReportTemplate entity) {
		logger.info("Searching  Configuration entity : {} ",entity);
		try {
			return service.search(entity);
		} catch (RestException e) {
			throw new RestException(e.getMessage());
		}
	}

	@GET
	@Override
	@Path("findById")
	public ReportTemplate findById(@QueryParam("id") Integer id) {
		logger.info("Finding ReportTemplate by id {} ", id);
		try {
			return service.findById(id);
		} catch (RestException e) {
			throw new RestException(e.getMessage());
		}
	}

	@GET
	@Path("findAll")
	public List<ReportTemplate> findAll() {
		try {
			return service.findAll();
		} catch (RestException e) {
			throw new RestException(e.getMessage());
		}
	}

	@POST
	@Path("update")
	public ReportTemplate updateReportTemplate(ReportTemplate reportTemplate) {
		logger.info("Updating  Template by an entity :{} ", reportTemplate);
		try {
			return service.update(reportTemplate);
		} catch (RestException e) {
			throw new RestException(e.getMessage());
		}
	}

	@POST
	@Path("remove")
	@Override
	public boolean remove(ReportTemplate reportTemplate) {
		logger.info("Removing  Template record by an entity :{}" , reportTemplate);
		try {
			service.remove(reportTemplate);
			return true;
		} catch (RestException e) {
			throw new RestException(e.getMessage());
		}
	}

	@Override
	public IGenericService<Integer, ReportTemplate> getService() {
		return service;
	}

	@POST
	@Path("create")
	public ReportTemplate createReportTemplate(ReportTemplate anEntity) {
		logger.info("Create Configuration by an entity :{} ", anEntity);
		try {
			return service.create(anEntity);
		} catch (RestException e) {
			throw new RestException(e.getMessage());
		}
	}
	
	@GET
	@Path("deleteReportTemplateById")
	public boolean deleteReportTemplateById(@QueryParam("id")Integer id) {
		logger.info("Removing Template record by Id :{}" , id);
		try {
			service.deleteReportTemplateById(id);
			return true;
		} catch (RestException e) {
			throw new RestException(e.getMessage());
		}
	}

	@Override
	public SearchContext getSearchContext() {
		return null;
	}
	
	@GET
	@Path("getAllReportMeasure")
	public List<String> getAllReportMeasure() {
		try {
			return service.getAllReportMeasure();
		} catch (RestException e) {
			throw new RestException(e.getMessage());
		} catch (Exception e) {
			logger.error("Exception in getAllReportMeasure : {} ",Utils.getStackTrace(e));
			throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}
	
	@GET
	@Path("getAllReportTypeByReportMeasure/{reportMeasure}")
	public List<String> getAllReportTypeByReportMeasure(@PathParam("reportMeasure") String reportMeasure) {
		try {
			return service.getAllReportTypeByReportMeasure(reportMeasure);
		} catch (RestException e) {
			throw new RestException(e.getMessage());
		} catch (Exception e) {
			logger.error("Exception in getAllReportTypeByReportMeasure : {} ",Utils.getStackTrace(e));
			throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}

	@GET
	@Path("getAllTemplateReportType")
	public List<String> getAllTemplateReportType() {
		try {
			return service.getAllTemplateReportType();
		} catch (RestException e) {
			throw new RestException(e.getMessage());
		} catch (Exception e) {
			logger.error("Exception in getAllTemplateReportType : {} ",Utils.getStackTrace(e));
			throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}
	
	@GET
	@Path("getAllReportTemplate")
	public List<ReportTemplate> getAllReportTemplate() {
		try {
			return service.getAllReportTemplate();
		} catch (RestException e) {
			throw new RestException(e.getMessage());
		} catch (Exception e) {
			logger.error("Exception in getAllReportTemplate : {} ",Utils.getStackTrace(e));
			throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public ReportTemplate create(@Valid ReportTemplate anEntity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReportTemplate update(@Valid ReportTemplate anEntity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeById(Integer primaryKey) {
		// TODO Auto-generated method stub
		
	}
	@GET
	@Path("getReportTemplateCountByFilter")
	public Long getReportTemplateCountByFilter(@QueryParam("module") String module,	@QueryParam("date") Long date) {
		try {
			return service.getReportTemplateCountByFilter(module, date);
		} catch (RestException e) {
			throw new RestException(e.getMessage());
		} catch (Exception e) {
			logger.error("Exception in getReportTemplateCountByFilter : {} ",Utils.getStackTrace(e));
			throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}
	
	@GET
	@Path("getReportTemplateByFilter")
	public List<ReportTemplateWrapper> getReportTemplateByFilter(@QueryParam("module") String module, @QueryParam("date") Long date,@QueryParam("lowerLimit") Integer lowerLimit,@QueryParam("upperLimit") Integer upperLimit) {
		try {
			logger.info("Inside ReportTemplateRestImpl @getReportTemplateByFilter: module:{},date:{}, lowerLimit:{} ,upperLimit:{}",module,date,lowerLimit,upperLimit);
			return service.getReportTemplateByFilter(module,date,lowerLimit,upperLimit);
		} catch (RestException e) {
			throw new RestException(e.getMessage());
		} catch (Exception e) {
			logger.error("Exception in getReportTemplateByFilter : {} ",Utils.getStackTrace(e));
			throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}
	
	@GET
	@Path("getAllJobs")
	public String getAllProcessRunnerJobs() {
		try {
			
			String credential = AESUtils.decrypt(ConfigUtils.getString(ForesightConstants.PROCESS_RUNNER_CREDENTIAL));
			String url = AESUtils.decrypt(ConfigUtils.getString(ForesightConstants.PROCESS_RUNNER_GET_ALL_JOBS));
			HttpRequest httpRequest = new HttpGetRequest(url);
			httpRequest.addHeader("Authorization", "Basic " + Base64.getEncoder().encodeToString(credential.getBytes()));
			return httpRequest.getString();
		} catch (Exception e) {
			logger.error("Exception in getAllReportTemplate : {} ",Utils.getStackTrace(e));
			throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}
	
	@GET
	@Path("getReportTemplateByReportType/{reportType}")
	public ReportTemplate getReportTemplateByReportType(@PathParam("reportType") String reportType) {
		try {
			return service.getReportTemplateByReportType(reportType);
		} catch (RestException e) {
			throw new RestException(e.getMessage());
		} catch (Exception e) {
			logger.error("Exception in getReportTemplateByReportType : {} ",Utils.getStackTrace(e));
			throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}
	
	@GET
	@Path("getReportMeasureByModule/{reportModule}")
	public List<String> getReportMeasureByModule(@PathParam("reportModule") String reportModule) {
		logger.info("getReportMeasureByModule: reportModule:{}",reportModule);
		try {
			return service.getReportMeasureByModule(reportModule);
		} catch (RestException e) {
			throw new RestException(e.getMessage());
		} catch (Exception e) {
			logger.error("Exception in getReportMeasureByModule : {} ",Utils.getStackTrace(e));
			throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}
	
	@GET
	@Path("getReportCategoryByModuleAndMeasure/{reportModule}/{reportMeasure}")
	public List<String> getReportCategoryByModuleAndMeasure(@PathParam("reportModule") String reportModule,@PathParam("reportMeasure") String reportMeasure) {
		logger.info("getReportCategoryByModuleAndMeasure: reportModule:{}, reportMeasure:{}",reportModule,reportMeasure);
		try {
			return service.getReportCategoryByModuleAndMeasure(reportModule,reportMeasure);
		} catch (RestException e) {
			throw new RestException(e.getMessage());
		} catch (Exception e) {
			logger.error("Exception in getReportCategoryByModuleAndMeasure : {} ",Utils.getStackTrace(e));
			throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}
	
	@POST
	@Path("getReportTemplateByModuleMeasureAndCategory")
	public ReportTemplateWrapper getReportTemplateByModuleMeasureAndCategory( ReportTemplateWrapper reportTemplateWrapper) {
		logger.info("getReportTemplateByModuleMeasureAndCategory: reportTemplate:{}",reportTemplateWrapper.toString());
		try {
			return service.getReportTemplateByModuleMeasureAndCategory(reportTemplateWrapper.getModule(),reportTemplateWrapper.getReportMeasure(),reportTemplateWrapper.getReportType());
		} catch (RestException e) {
			throw new RestException(e.getMessage());
		} catch (Exception e) {
			logger.error("Exception in getReportTemplateByFilter : {} ",Utils.getStackTrace(e));
			throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}
	
}

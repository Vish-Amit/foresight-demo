package com.inn.foresight.core.report.service.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.service.impl.AbstractService;
import com.inn.foresight.core.generic.exceptions.application.ValidationFailedException;
import com.inn.foresight.core.generic.utils.DateUtil;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.report.dao.IAnalyticsTemplatesDao;
import com.inn.foresight.core.report.dao.IReportTemplateDao;
import com.inn.foresight.core.report.model.AnalyticsTemplates;
import com.inn.foresight.core.report.model.AnalyticsTemplates.Generation;
import com.inn.foresight.core.report.model.AnalyticsTemplates.TemplateType;
import com.inn.foresight.core.report.model.ReportTemplate;
import com.inn.foresight.core.report.service.IReportTemplateService;
import com.inn.foresight.core.report.wrapper.ReportTemplateWrapper;
import com.inn.product.um.user.model.User;
import com.inn.product.um.user.service.impl.UserContextServiceImpl;

@Service("ReportTemplateServiceImpl")
@Transactional
public class ReportTemplateServiceImpl extends AbstractService<Integer, ReportTemplate> implements IReportTemplateService {

	private Logger logger = LogManager
			.getLogger(ReportTemplateServiceImpl.class);

	@Autowired
	private IAnalyticsTemplatesDao  analyticsTemplatesDao;
	
	@Autowired
	private IReportTemplateDao dao;
	
	public ReportTemplateServiceImpl() {
		super();
	}

	@Autowired
	public ReportTemplateServiceImpl(IReportTemplateDao dao) {
		super();
		super.setDao(dao);
		this.dao = dao;
	}
	
	@Override
	public List<ReportTemplate> search(ReportTemplate entity) {
		logger.info("Finding  Configuration list by Configuration :{} ", entity);
		return super.search(entity);
	}

	@Override
	public List<ReportTemplate> findAll() {
		try {
			return dao.findAll();
		} catch (NoResultException ex) {
			logger.error("No Data Found err={} ",ex.getMessage());
			throw new RestException(ex);
		} catch (EmptyResultDataAccessException | DaoException ex) {
				logger.error(ex.getMessage());
				throw new RestException(ex);
		}
	}

	@Override
	public ReportTemplate create(ReportTemplate anEntity) {
		logger.info("Creating  Configuration by an Configuration :{} ", anEntity);
		try {
			User user = UserContextServiceImpl.getUserInContext();
			anEntity.setCreator(user);
			anEntity.setIsDeleted(ForesightConstants.FALSE);
			AnalyticsTemplates template = null;
			if (anEntity != null) {
				template = new AnalyticsTemplates();
				template.setCreatorid_fk(user.getUserid());
				template.setLastmodifierid_fk(user.getUserid());
				template.setType(TemplateType.CUSTOM);
				template.setGeneration(Generation.ON_DEMAND);
				template.setModuleName(anEntity.getModule());
				logger.info("anEntity.getIsDeleted() {} , anEntity.getIsEnabled() {}  ",anEntity.getIsDeleted(),anEntity.getIsEnabled());
				if((anEntity.getIsDeleted().equals(ForesightConstants.FALSE)) && (anEntity.getIsEnabled().equals(ForesightConstants.TRUE))) {
					logger.info(" condition ok ");
					template.setDeleted(ForesightConstants.FALSE);
				}else {
					template.setDeleted(ForesightConstants.TRUE);
				}
				Type type = new TypeToken<HashMap<String, String>>() {}.getType();
				Map<String,String> reportType=new HashMap<>();
				if(anEntity.getReportType()!=null && !anEntity.getReportType().isEmpty()) {
				reportType=new Gson().fromJson(anEntity.getReportType(), type);
				logger.info("reportType ",reportType);
					if(reportType!=null) {
						template.setName(reportType.get(ForesightConstants.NAME));
						template.setWidgetName(reportType.get(ForesightConstants.NAME));
					}
				}
				 
				if (anEntity.getGeographyConfig() != null && !anEntity.getGeographyConfig().isEmpty()) {
					anEntity.setGeographyConfig(anEntity.getGeographyConfig().replaceAll("'", "\""));
				}
				if (anEntity.getReportConfig() != null && !anEntity.getReportConfig().isEmpty()) {
					anEntity.setReportConfig(anEntity.getReportConfig().replaceAll("'", "\""));

				}
				if (anEntity.getReportNameConfig() != null && !anEntity.getReportNameConfig().isEmpty()) {
					anEntity.setReportNameConfig(anEntity.getReportNameConfig().replaceAll("'", "\""));

				}
			}
			ReportTemplate reportTemplate= super.create(anEntity);
			if (reportTemplate!=null) {
			template.setWidgetId(reportTemplate.getId());
			analyticsTemplatesDao.create(template);
			}
			return reportTemplate;
					
		}catch (DataIntegrityViolationException ex) {
			logger.error(ex.getMessage());
			throw new ValidationFailedException(ex);
		} catch (ConstraintViolationException ex) {
			logger.error(ex.getMessage());
			throw new RestException(ex);
		} catch(Exception e) {
			logger.error(Utils.getStackTrace(e));
			throw new RestException(e);
		}
	}

	@Override
	public ReportTemplate update(ReportTemplate anEntity) {
		logger.info("Updating Configuration by an Configuration :{} ", anEntity);
		ReportTemplate persistedReportTemplate=null;
		boolean flag=ForesightConstants.FALSE;
		if(anEntity.getId() != null)
		persistedReportTemplate=dao.getAllReportTemplateByID(anEntity.getId());
		if(persistedReportTemplate != null) {
		try {
			User user = UserContextServiceImpl.getUserInContext();
			AnalyticsTemplates analyticsTemplates = analyticsTemplatesDao.getAnalyticsTemplatebyWidgetidAndType(anEntity.getId(),TemplateType.CUSTOM);
			logger.info("find record by id AnalyticsTemplates analyticsTemplates :  {} ", analyticsTemplates);
			if (anEntity.getGeographyConfig() != null && !anEntity.getGeographyConfig().isEmpty()) {
					persistedReportTemplate.setGeographyConfig(anEntity.getGeographyConfig().replaceAll("'", "\""));
				}else if(anEntity.getGeographyConfig() == null) {
					persistedReportTemplate.setGeographyConfig(null);
				}
				if (anEntity.getReportConfig() != null && !anEntity.getReportConfig().isEmpty()) {
					persistedReportTemplate.setReportConfig(anEntity.getReportConfig().replaceAll("'", "\""));
				}else if(anEntity.getReportConfig() == null) {
					persistedReportTemplate.setReportConfig(null);
				}
				if (anEntity.getReportNameConfig() != null && !anEntity.getReportNameConfig().isEmpty()) {
					persistedReportTemplate.setReportNameConfig(anEntity.getReportNameConfig().replaceAll("'", "\""));
				}else if(anEntity.getReportNameConfig() == null) {
					persistedReportTemplate.setReportNameConfig(null);
				}
				
				persistedReportTemplate.setBuilderClass(anEntity.getBuilderClass());
				persistedReportTemplate.setDownloadPath(anEntity.getDownloadPath());
				if (anEntity.getExtraConfig() != null && !anEntity.getExtraConfig().isEmpty()) {
				persistedReportTemplate.setExtraConfig(anEntity.getExtraConfig().replaceAll("'", "\""));
				}else if(anEntity.getExtraConfig() == null) {
					persistedReportTemplate.setExtraConfig(null);
				}
				persistedReportTemplate.setGenerationConfig(anEntity.getGenerationConfig());
				persistedReportTemplate.setOndemand(anEntity.getOndemand());
				persistedReportTemplate.setReportGenType(anEntity.getReportGenType());
				persistedReportTemplate.setStorageType(anEntity.getStorageType());
				
				if(analyticsTemplates!=null) {
					flag=ForesightConstants.TRUE;
					analyticsTemplates.setLastmodifierid_fk(user.getUserid());
					analyticsTemplates.setModuleName(anEntity.getModule());
				}else {
					analyticsTemplates = new AnalyticsTemplates();
					analyticsTemplates.setCreatorid_fk(user.getUserid());
					analyticsTemplates.setLastmodifierid_fk(user.getUserid());
					analyticsTemplates.setGeneration(Generation.ON_DEMAND);
					analyticsTemplates.setType(TemplateType.CUSTOM);
					analyticsTemplates.setModuleName(anEntity.getModule());
					analyticsTemplates.setWidgetId(anEntity.getId());
					Type type = new TypeToken<HashMap<String, String>>() {}.getType();
					Map<String,String> reportType = new HashMap<>();
					if(anEntity.getReportType()!=null && !anEntity.getReportType().isEmpty()) {
					reportType=new Gson().fromJson(anEntity.getReportType(), type);
					logger.info("reportType ",reportType);
						if(reportType!=null) {
							analyticsTemplates.setName(reportType.get(ForesightConstants.NAME));
							analyticsTemplates.setWidgetName(reportType.get(ForesightConstants.NAME));
						}
					}
				}
				logger.info("anEntity.getIsDeleted() =  {} , anEntity.getIsEnabled() = {}  ",anEntity.getIsDeleted(),anEntity.getIsEnabled());
				if((anEntity.getIsDeleted().equals(ForesightConstants.FALSE)) && (anEntity.getIsEnabled().equals(ForesightConstants.TRUE))) {
					logger.info("set in Update false");
					analyticsTemplates.setDeleted(ForesightConstants.FALSE);
				}else {
					analyticsTemplates.setDeleted(ForesightConstants.TRUE);
				}
				ReportTemplate reportTemplate = dao.update(persistedReportTemplate);
				if(reportTemplate!=null) {
					if(flag) {
					analyticsTemplatesDao.update(analyticsTemplates);
					}else {
					logger.info("going to create");
					analyticsTemplatesDao.create(analyticsTemplates);
					}
				}
			logger.info("Returning reportTemplate::"+reportTemplate);	
			return reportTemplate; 
		} catch (DataIntegrityViolationException ex) {
			logger.error(ex.getMessage());
			throw new ValidationFailedException(ex);
		} catch (ConstraintViolationException ex) {
			logger.error(ex.getMessage());
			throw new RestException(ex);
		} catch(Exception e) {
			logger.error(Utils.getStackTrace(e));
			throw new RestException(e);
		}
	}else 
		throw new RestException("Unable to update Report Template");
	}

	@Override
	public void remove(ReportTemplate anEntity) {
		logger.info("Removing Configuration by an Configuration :{} ",anEntity);
		super.remove(anEntity);

	}

	@Override
	public void removeById(Integer primaryKey) {
		logger.info("Removing report Template by primaryKey {} ", + primaryKey);
		try {
			super.removeById(primaryKey);
		}  catch (DataIntegrityViolationException ex) {
			logger.error(ex.getMessage());
			throw new ValidationFailedException(ex);
		} catch (ConstraintViolationException ex) {
			logger.error(ex.getMessage());
			throw new RestException(ex);
		}

	}


	@Override
	public List<String> getAllReportMeasure(){
		try {
			logger.info("Going to getAllReportMeasure ");
			return dao.getAllMeasure();
		} catch (Exception e) {
			throw new RestException("unable to get All ReportMeasure err={} ",e.getMessage());
		}
	}
	
	@Override
	public List<String> getAllReportTypeByReportMeasure(String reportMeasure) {
		try {
			logger.info("Going to getAllReportTypeByReportMeasure :reportMeasure={} ",reportMeasure);
			return dao.getAllReportTypeByMeasure(reportMeasure);
		} catch (Exception e) {
			throw new RestException("unable to get All ReportType By ReportMeasure err={} ",e.getMessage());
		}
	}
	
	@Override
	public List<String> getAllTemplateReportType() {
		try {
			logger.info("Going to getAllReportType " );
			return dao.getAllTemplateReportType();
		} catch (Exception e) {
			logger.error("Exception in getAllTemplateReportType err={} ",Utils.getStackTrace(e));
			throw new RestException("unable to get All Template ReportType err={} ",e.getMessage());
		}
	}
	
	
	@Override
	public List<ReportTemplate> getAllReportTemplate() {
		try {
			logger.info("Going to getAllReportType " );
			return dao.getAllReportTemplate();
		} catch (Exception e) {
			logger.error("Exception in getAllTemplateReportType err={} ",Utils.getStackTrace(e));
			throw new RestException("unable to get All Template ReportType err={} ",e.getMessage());
		}
	}
	
	
	@Override
	public void deleteReportTemplateById(Integer primaryKey) {
		logger.info("Removing report Template by primaryKey {} ", + primaryKey);
		try {
			dao.deleteReportTemplateById(primaryKey);
		}  catch (DataIntegrityViolationException ex) {
			logger.error(ex.getMessage());
			throw new ValidationFailedException(ex);
		} catch (ConstraintViolationException ex) {
			logger.error(ex.getMessage());
			throw new RestException(ex);
		}
	}
	
	@Override
	public List<ReportTemplateWrapper> getReportTemplateByFilter(String module,Long date,Integer lowerLimit,Integer upperLimit){
		List<ReportTemplateWrapper> reportTemplateWrapperList = new ArrayList<>();
		String dateStr= null;
		try {
			logger.info("going to get report Instance by module {} date {}",module, date);
			User user = UserContextServiceImpl.getUserInContext();
			if(date!=null) {
				dateStr= DateUtil.parseDateToString(ForesightConstants.DATEMONTHYEAR_SPACE_SEPRATED, new Date(date));
			}
			reportTemplateWrapperList = dao.getReportTemplateByFilter(module,user.getUserid(),dateStr,lowerLimit,upperLimit); 


		} catch (RestException e){
			throw new RestException(e.getMessage());
		} catch (Exception e){
			logger.error("Exception in getReportTemplateByFilter :module {} date {}  err:{}",module, date,e.getMessage());
		}
		return reportTemplateWrapperList;
	}
	
	@Override
	public Long getReportTemplateCountByFilter(String module,Long date){
		Long count = 0L ;
		try {
			String dateStr=null;
			logger.info("going to get report Instance by reportName {} date {}",module, date);
			User user = UserContextServiceImpl.getUserInContext();
			if(date!=null) {
				dateStr= DateUtil.parseDateToString(ForesightConstants.DATEMONTHYEAR_SPACE_SEPRATED, new Date(date));
			}
			count = dao.getReportTemplateCountByFilter(module,user.getUserid(),dateStr);
		} catch (RestException e){
			throw new RestException(e.getMessage());
		} catch (Exception e){
			logger.error("Exception in getReportTemplateCountByFilter :reportName {} date {}  err:{}",module, date,e.getMessage());
		}
		return count;
	}
	
	@Override
	public ReportTemplate getReportTemplateByReportType(String reportType) {
		ReportTemplate reportTemplate = null;
		try {
			reportTemplate = dao.getReportTemplateByReportType(reportType);
		} catch (Exception e) {
			logger.error("Unable to get reports template by reporttype {},exception{}",reportType,Utils.getStackTrace(e));
		}
		return reportTemplate;
	}

	@Override
	public List<String> getReportMeasureByModule(String reportModule) {
		List<String> reportMeasure = null ;
		try {
			reportMeasure = dao.getReportMeasureByModule(reportModule);
		} catch (Exception e) {
			logger.error("Inside getReportMeasureByModule: Unable to get reports measure by reportModule {},exception{}",reportModule,Utils.getStackTrace(e));
		}
		return reportMeasure;
	}

	@Override
	public List<String> getReportCategoryByModuleAndMeasure(String reportModule, String reportMeasure) {
		List<String> reportCategory = null;
		try {
			reportCategory = dao.getReportCategoryByModuleAndMeasure(reportModule,reportMeasure);
		} catch (Exception e) {
			logger.error("Inside getReportCategoryByModuleAndMeasure: Unable to get reports measure by reportModule {} & reportMeasure {} ,exception{}",reportModule,reportMeasure,Utils.getStackTrace(e));
		}
		return reportCategory;
	}

	@Override
	public ReportTemplateWrapper getReportTemplateByModuleMeasureAndCategory(String reportModule, String reportMeasure, String reportCategory) {
		logger.info("Inside ReportTemplateServiceImpl @getReportTemplateByModuleMeasureAndCategory: reportModule:{}, reportMeasure:{} ,reportCategory:{}",reportModule,reportMeasure,reportCategory);
		ReportTemplateWrapper reportTemplateWrapper = new ReportTemplateWrapper();
		try {
			reportTemplateWrapper = dao.getReportTemplateByModuleMeasureAndCategory(reportModule,reportMeasure,reportCategory);
		} catch (Exception e) {
			logger.error("Inside getReportTemplateByFilter: Unable to get reports template by reportModule {} & reportMeasure {} & reportCategory {},exception{}",reportModule,reportMeasure,reportCategory,Utils.getStackTrace(e));
		}
		return reportTemplateWrapper;
	}

}
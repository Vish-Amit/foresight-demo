package com.inn.foresight.core.report.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.validation.Valid;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.report.dao.IReportTemplateDao;
import com.inn.foresight.core.report.model.ReportTemplate;
import com.inn.foresight.core.report.wrapper.ReportTemplateWrapper;

@Repository("ReportTemplateDaoImpl")
public class ReportTemplateDaoImpl extends HibernateGenericDao<Integer, ReportTemplate> implements IReportTemplateDao {
	private Logger logger = LogManager.getLogger(ReportTemplateDaoImpl.class);

	public ReportTemplateDaoImpl() {
		super(ReportTemplate.class);
	}

	@Override
	public ReportTemplate create(@Valid ReportTemplate reportTemplate) {
		return super.create(reportTemplate);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ReportTemplate update(@Valid ReportTemplate reportTemplate){
		return super.update(reportTemplate);
	}

	/**
	 * 
	 * Method to remove reportWidget record
	 * 
	 * @parameter reportWidget of type ReportWidget
	 * 
	 */

	@Override
	public void delete(ReportTemplate reportTemplate){
		super.delete(reportTemplate);
	}

	/**
	 * 
	 * Method to remove Widget record by primary key
	 * 
	 * @parameter primary key of type Integer
	 * 
	 */

	@Override
	public void deleteByPk(Integer reportWidgetPk){
		super.deleteByPk(reportWidgetPk);
	}

	/**
	 * 
	 * Returns the list of ReportWidget record
	 * 
	 * @returns eportWidget record
	 * 
	 */

	@Override
	public List<ReportTemplate> findAll() {
		return super.findAll();
	}

	@Override
	public ReportTemplate findByPk(Integer reportTemplatepk){
		return super.findByPk(reportTemplatepk);
	}

	@Override
	public List<String> getAllMeasure() {
		try {
			Query query =getEntityManager().createNamedQuery("getAllMeasure");
			return query.getResultList();
		} catch (Exception e) {
			logger.error("Exception in getAllMeasure : error={}",e.getMessage());
			throw new RestException("Unable to getAllMeasure error:{}"+Utils.getStackTrace(e));
		}
	}

	@Override
	public List<String> getAllReportTypeByMeasure(String reportMeasure) {
		try {
			Query query =getEntityManager().createNamedQuery("getAllReportTypeByMeasure");
			query.setParameter("reportMeasure", reportMeasure);
			return query.getResultList();
		} catch (Exception e) {
			logger.error("Exception in getAllReportTypeByMeasure forreportMeasure={}  error={}",reportMeasure,e.getMessage());
			throw new RestException("Unable to getAllReportTypeByMeasure error:{}"+Utils.getStackTrace(e));
		}
	}
	
	@Override
	public List<String> getAllTemplateReportType() {
		try {
			Query query =getEntityManager().createNamedQuery("getAllTemplateReportType");
			return query.getResultList();
		} catch (Exception e) {
			logger.error("Exception in getAllTemplateReportType err={} ",Utils.getStackTrace(e));
			throw new RestException("Unable to getAllTemplateReportType error:{}"+Utils.getStackTrace(e));
		}
	}
	
	@Override
	public List<ReportTemplate> getAllReportTemplate() {
		try {
			Query query =getEntityManager().createNamedQuery("getAllReportTemplate");
			return query.getResultList();
		} catch (Exception e) {
			logger.error("Exception in getAllReportTemplate err={} ",Utils.getStackTrace(e));
			throw new RestException("Unable to getAllReportTemplate error:{}"+Utils.getStackTrace(e));
		}
	}
	
	@Override
	public void deleteReportTemplateById(Integer id) {
		try {
			Query query =getEntityManager().createNamedQuery("deleteReportTemplateById");
			query.setParameter("id", id);
			int deletedRecords = query.executeUpdate();
			logger.info("deleteReportTemplateById deletedRecords= {} ",deletedRecords);
		} catch (Exception e) {
			logger.error("Exception in deleteReportTemplateById err={} ",Utils.getStackTrace(e));
			throw new RestException("Unable to deleteReportTemplateById error:{}"+Utils.getStackTrace(e));
		}
	}

	@Override
	public List<ReportTemplateWrapper> getReportTemplateByFilter(String module,Integer userId,String date,Integer lowerLimit,Integer upperLimit){
		List<ReportTemplateWrapper> reportTemplateWrapper= new ArrayList<>();
		try {
				String dynamicQuery = "select new com.inn.foresight.core.report.wrapper.ReportTemplateWrapper (r.id, r.module,r.reportMeasure, r.reportType) from ReportTemplate r where r.isDeleted=false";
				if(module!=null) {
					dynamicQuery+= " and r.module=:module";
				}
				if(date!=null) {
					dynamicQuery+= " and date_format(r.creationTime,'%d %m %y') = (:date)";
				}
				dynamicQuery+= " order by r.creationTime desc";
				logger.info("query= {} ",dynamicQuery);
				Query query =getEntityManager().createQuery(dynamicQuery);
				if(module!=null) {
					query.setParameter("module", module);
				}
				if(date!=null) {
					query.setParameter("date", date);
				}
				if (upperLimit!=null && upperLimit >= 0) {
					query.setMaxResults(upperLimit - lowerLimit + 1);
				}

				if (lowerLimit!=null && lowerLimit >= 0) {
					query.setFirstResult(lowerLimit);
				}
				reportTemplateWrapper=query.getResultList();
		} catch (NoResultException e){
			logger.error("No Data Found for module {} , userId{}, date{} ",module,userId, date);
			throw new RestException("No Data Found") ;
		} catch (Exception e){
			throw new RestException("Unable to get reports {}"+Utils.getStackTrace(e));
		}
		return reportTemplateWrapper;
	}

	@Override
	public Long getReportTemplateCountByFilter(String module,Integer userId,String date){
		try {
				String dynamicQuery = "select count(r.id) from ReportTemplate r where isDeleted=false  ";
				if(module!=null) {
					dynamicQuery+= " and r.module=:module";
				}
				if(date!=null) {
					dynamicQuery+= " and date_format(r.creationTime,'%d %m %y') = (:date)";
				}
				logger.info("getReportTemplateCountByFilter dynamicQuery={} ",dynamicQuery);
				Query query =getEntityManager().createQuery(dynamicQuery);
				if(module!=null) {
					query.setParameter("module", module);
				}
				if(date!=null) {
					query.setParameter("date", date);
				}
				return (Long) query.getSingleResult();
		} catch (NoResultException e){
			logger.error("No Data Found for module {} ,userId{}, date{} ",module,userId, date);
			throw new RestException("No Data Found") ;
		} catch (Exception e){
			throw new RestException("Unable to get reports count  {}"+Utils.getStackTrace(e));
		}
	}
	
	@Override
	public ReportTemplate getReportTemplateByReportType(String reportType){
		try {
				Query query = getEntityManager().createNamedQuery("getReportTemplateByReportType");
				query.setParameter("reportType", reportType);
				return (ReportTemplate) query.getSingleResult();
		} catch (Exception e){
			throw new RestException("Unable to get reports count  {}"+Utils.getStackTrace(e));
		}
	}
	@Override
	public ReportTemplate getAllReportTemplateByID(Integer id) {
		try {
			Query query =getEntityManager().createNamedQuery("getAllReportTemplateByID");
			query.setParameter(ForesightConstants.ID, id);
			return (ReportTemplate) query.getSingleResult();
		} catch (Exception e) {
			logger.error("Exception in getAllReportTemplateByID err={} ",Utils.getStackTrace(e));
			throw new RestException("Unable to getAllReportTemplateByID error:{}"+Utils.getStackTrace(e));
		}
	}
	
	
	@Override
	public List<ReportTemplate> getReportTemplateByModule(String moduleName) {
		try {
			Query query =getEntityManager().createNamedQuery("getReportTemplateByModule").setParameter("moduleName", moduleName);
			return query.getResultList();
		} catch (Exception e) {
			logger.error("Exception inside getReportTemplateByModule due to {}",ExceptionUtils.getStackTrace(e));
			throw new RestException("Unable to getReportTemplateByModule due to {}",ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public List<String> getReportMeasureByModule(String reportModule) {
		try {
			Query query =getEntityManager().createNamedQuery("getReportMeasureByModule").setParameter("reportModule", reportModule);
			return query.getResultList();
		} catch (Exception e) {
			logger.error("Exception inside getReportMeasureByModule due to {}",ExceptionUtils.getStackTrace(e));
			throw new RestException("Unable to getReportMeasureByModule due to {}",ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public List<String> getReportCategoryByModuleAndMeasure(String reportModule, String reportMeasure) {
		try {
			Query query =getEntityManager().createNamedQuery("getReportCategoryByModuleAndMeasure").setParameter("reportModule", reportModule).setParameter("reportMeasure", reportMeasure);
			return query.getResultList();
		} catch (Exception e) {
			logger.error("Exception inside getReportCategoryByModuleAndMeasure due to {}",ExceptionUtils.getStackTrace(e));
			throw new RestException("Unable to getReportCategoryByModuleAndMeasure due to {}",ExceptionUtils.getStackTrace(e));
		}
	}

	@Override
	public ReportTemplateWrapper getReportTemplateByModuleMeasureAndCategory(String reportModule, String reportMeasure, String reportCategory) {
		logger.info("Inside ReportTemplateDaoImpl @getReportTemplateByModuleMeasureAndCategory: reportModule:{}, reportMeasure:{} ,reportCategory:{}",reportModule,reportMeasure,reportCategory);
		ReportTemplateWrapper reportTemplateWrapper= new ReportTemplateWrapper();
		try {
			Query query =getEntityManager().createNamedQuery("getReportTemplateByModuleMeasureAndCategory").setParameter("reportModule", reportModule).setParameter("reportMeasure", reportMeasure).setParameter("reportCategory", reportCategory);
			reportTemplateWrapper= (ReportTemplateWrapper) query.getSingleResult();
		} catch(NoResultException e) {
			logger.error("Exception in getReportTemplateByFilter err={} ",e.getMessage());
		}
		catch (Exception e) {
			logger.error("Exception in getReportTemplateByFilter err={} ",Utils.getStackTrace(e));
			throw new RestException("Unable to getReportTemplateByFilter error:{}"+Utils.getStackTrace(e));
		}
		return reportTemplateWrapper;
	}
	
}